/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;
import es.dc.a21l.elementoJerarquia.cu.GestorCUCategoria;
import es.dc.a21l.elementoJerarquia.modelo.Categoria;
import es.dc.a21l.elementoJerarquia.modelo.CategoriaRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.GestorCUUsuarioPermiso;
import es.dc.a21l.usuario.cu.UsuarioDto;

public class GestorCUCategoriaImpl implements GestorCUCategoria {

	private IndicadorRepositorio indicadorRepositorio;
	private CategoriaRepositorio categoriaRepositorio;
	private Mapper mapper;
	private GestorCUUsuarioPermiso gestorCUUsuarioPermiso;
	private GestorCUUsuario gestorCUUsuario;
	
	
	@Autowired
	public void setGestorCUUsuario(GestorCUUsuario gestorCUUsuario) {
		this.gestorCUUsuario = gestorCUUsuario;
	}

	@Autowired
	public void setGestorCUUsuarioPermiso(GestorCUUsuarioPermiso gestorCUUsuarioPermiso) {
		this.gestorCUUsuarioPermiso = gestorCUUsuarioPermiso;
	}
	
	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setCategoriaRepositorio(CategoriaRepositorio categoriaRepositorio) {
		this.categoriaRepositorio = categoriaRepositorio;
	}
	@Autowired
	public void setCategoriaRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadre(){
		List<Categoria> lista= categoriaRepositorio.cargaCategoriasPadre();
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		for(Categoria temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePublicados()
	{	
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPublicados();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					
					while (categoriaActual.getCategoriaPadre() != null)
					{
						categoriaActual = categoriaActual.getCategoriaPadre();
					}
					
					// Se añade la categoría a la lista de categorías
					if (!listaIdsCategorias.contains(categoriaActual.getId()))
					{
						listaCategorias.add(categoriaActual);
						listaIdsCategorias.add(categoriaActual.getId());
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadreParaFuentes(Long idFuente)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPorFuente(idFuente);
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					
					while (categoriaActual.getCategoriaPadre() != null)
					{
						categoriaActual = categoriaActual.getCategoriaPadre();
					}
					
					// Se añade la categoría a la lista de categorías
					if (!listaIdsCategorias.contains(categoriaActual.getId()))
					{
						listaCategorias.add(categoriaActual);
						listaIdsCategorias.add(categoriaActual.getId());
					}
				}
			}
		}
		
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadre(Long idCategoriaPadre){
		List<Categoria> lista= categoriaRepositorio.cargaCategoriasPorPadre(idCategoriaPadre);
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		for(Categoria temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadreParaFuentes(Long idCategoriaPadre, Long idFuente)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPorFuente(idFuente);
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					// Si se buscan hijos del nodo raíz
					if (idCategoriaPadre <= 0L)
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							categoriaActual = categoriaActual.getCategoriaPadre();
						}
						
						// Se añade la categoría a la lista de categorías
						if (!listaIdsCategorias.contains(categoriaActual.getId()))
						{
							listaCategorias.add(categoriaActual);
							listaIdsCategorias.add(categoriaActual.getId());
						}
					}
					else
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							// Si es la buscada
							if (categoriaActual.getCategoriaPadre().getId() == idCategoriaPadre)
							{
								// Se añade la categoría a la lista de categorías
								if (!listaIdsCategorias.contains(categoriaActual.getId()))
								{
									listaCategorias.add(categoriaActual);
									listaIdsCategorias.add(categoriaActual.getId());
								}
								
								break;
							}
							else
							{
								categoriaActual = categoriaActual.getCategoriaPadre();
							}
						}
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePublicos()
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPublicos();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					
					while (categoriaActual.getCategoriaPadre() != null)
					{
						categoriaActual = categoriaActual.getCategoriaPadre();
					}
					
					// Se añade la categoría a la lista de categorías
					if (!listaIdsCategorias.contains(categoriaActual.getId()))
					{
						listaCategorias.add(categoriaActual);
						listaIdsCategorias.add(categoriaActual.getId());
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePublicos(Long idCategoriaPadre)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPublicos();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					// Si se buscan hijos del nodo raíz
					if (idCategoriaPadre <= 0L)
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							categoriaActual = categoriaActual.getCategoriaPadre();
						}
						
						// Se añade la categoría a la lista de categorías
						if (!listaIdsCategorias.contains(categoriaActual.getId()))
						{
							listaCategorias.add(categoriaActual);
							listaIdsCategorias.add(categoriaActual.getId());
						}
					}
					else
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							// Si es la buscada
							if (categoriaActual.getCategoriaPadre().getId() == idCategoriaPadre)
							{
								// Se añade la categoría a la lista de categorías
								if (!listaIdsCategorias.contains(categoriaActual.getId()))
								{
									listaCategorias.add(categoriaActual);
									listaIdsCategorias.add(categoriaActual.getId());
								}
								
								break;
							}
							else
							{
								categoriaActual = categoriaActual.getCategoriaPadre();
							}
						}
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePendientesDePublicos()
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPendientesDePublico();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					
					while (categoriaActual.getCategoriaPadre() != null)
					{
						categoriaActual = categoriaActual.getCategoriaPadre();
					}
					
					// Se añade la categoría a la lista de categorías
					if (!listaIdsCategorias.contains(categoriaActual.getId()))
					{
						listaCategorias.add(categoriaActual);
						listaIdsCategorias.add(categoriaActual.getId());
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPadrePendientesPublicacionWeb()
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPendientesPublicacionWeb();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					
					while (categoriaActual.getCategoriaPadre() != null)
					{
						categoriaActual = categoriaActual.getCategoriaPadre();
					}
					
					// Se añade la categoría a la lista de categorías
					if (!listaIdsCategorias.contains(categoriaActual.getId()))
					{
						listaCategorias.add(categoriaActual);
						listaIdsCategorias.add(categoriaActual.getId());
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePendientesDePublicos(Long idCategoriaPadre)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPendientesDePublico();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					// Si se buscan hijos del nodo raíz
					if (idCategoriaPadre <= 0L)
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							categoriaActual = categoriaActual.getCategoriaPadre();
						}
						
						// Se añade la categoría a la lista de categorías
						if (!listaIdsCategorias.contains(categoriaActual.getId()))
						{
							listaCategorias.add(categoriaActual);
							listaIdsCategorias.add(categoriaActual.getId());
						}
					}
					else
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							// Si es la buscada
							if (categoriaActual.getCategoriaPadre().getId() == idCategoriaPadre)
							{
								// Se añade la categoría a la lista de categorías
								if (!listaIdsCategorias.contains(categoriaActual.getId()))
								{
									listaCategorias.add(categoriaActual);
									listaIdsCategorias.add(categoriaActual.getId());
								}
								
								break;
							}
							else
							{
								categoriaActual = categoriaActual.getCategoriaPadre();
							}
						}
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePendientesPublicacionWeb(Long idCategoriaPadre)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPendientesPublicacionWeb();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					// Si se buscan hijos del nodo raíz
					if (idCategoriaPadre <= 0L)
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							categoriaActual = categoriaActual.getCategoriaPadre();
						}
						
						// Se añade la categoría a la lista de categorías
						if (!listaIdsCategorias.contains(categoriaActual.getId()))
						{
							listaCategorias.add(categoriaActual);
							listaIdsCategorias.add(categoriaActual.getId());
						}
					}
					else
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							// Si es la buscada
							if (categoriaActual.getCategoriaPadre().getId() == idCategoriaPadre)
							{
								// Se añade la categoría a la lista de categorías
								if (!listaIdsCategorias.contains(categoriaActual.getId()))
								{
									listaCategorias.add(categoriaActual);
									listaIdsCategorias.add(categoriaActual.getId());
								}
								
								break;
							}
							else
							{
								categoriaActual = categoriaActual.getCategoriaPadre();
							}
						}
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPorPadrePublicados(Long idCategoriaPadre)
	{
		// Lista de Categorias
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		List<Long> listaIdsCategorias = new ArrayList<Long>();
		// Se obtienen los indicadores pendientes de ser públicos
		List<Indicador> listaIndicadoresPtes = indicadorRepositorio.cargaIndicadoresConCategoriaPublicados();
		
		// Si hai elemementos
		if (listaIndicadoresPtes != null && listaIndicadoresPtes.size() > 0)
		{
			for (Indicador indicador: listaIndicadoresPtes)
			{
				// Si el indicador es, a priori, correcto
				if (indicador != null && indicador.getCategoria() != null)
				{
					Categoria categoriaActual = indicador.getCategoria();
					// Si se buscan hijos del nodo raíz
					if (idCategoriaPadre <= 0L)
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							categoriaActual = categoriaActual.getCategoriaPadre();
						}
						
						// Se añade la categoría a la lista de categorías
						if (!listaIdsCategorias.contains(categoriaActual.getId()))
						{
							listaCategorias.add(categoriaActual);
							listaIdsCategorias.add(categoriaActual.getId());
						}
					}
					else
					{
						while (categoriaActual.getCategoriaPadre() != null)
						{
							// Si es la buscada
							if (categoriaActual.getCategoriaPadre().getId() == idCategoriaPadre)
							{
								// Se añade la categoría a la lista de categorías
								if (!listaIdsCategorias.contains(categoriaActual.getId()))
								{
									listaCategorias.add(categoriaActual);
									listaIdsCategorias.add(categoriaActual.getId());
								}
								
								break;
							}
							else
							{
								categoriaActual = categoriaActual.getCategoriaPadre();
							}
						}
					}
				}
			}
		}
		
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		
		for(Categoria temp:listaCategorias)
		{
			result.add(transformer.transform(temp));
		}
		
		return result;
	}
	
	public CategoriaDto cargaPorId(Long id){
		return new Categoria2CategoriaDtoTransformer(mapper).transform(categoriaRepositorio.carga(id));
	}
	
	public CategoriaDto guarda(CategoriaDto categoriaDto, Long idUsuario, EncapsuladorErroresSW erroresSW){
		if(categoriaDto.getNombre()!=null && (cargaExisteCategoriaConNombre(categoriaDto.getNombre(),categoriaDto.getIdCategoriaPadre(),categoriaDto.getId()))){
			if (gestorCUUsuario.esAdmin(idUsuario)){
				erroresSW.addError(CategoriaDtoFormErrorsEmun.NOMBRE_EN_USO);//Nombre en uso en una categoría en la que el usuario tiene permiso
			}
			else{
				Categoria categoriaExistente = categoriaRepositorio.cargaCategoriaPorNombre(categoriaDto.getNombre(),categoriaDto.getIdCategoriaPadre(),categoriaDto.getId());
				List<Long> idsCategoriasPermiso = cargaIdsCategoriasUsuarioPermisos(idUsuario);
				boolean tienePermiso = false;
				for (Long idCategoria : idsCategoriasPermiso) {
					if (categoriaExistente.getId() == idCategoria){
						tienePermiso = true;
						break;
					}
				}
				if (tienePermiso){
					erroresSW.addError(CategoriaDtoFormErrorsEmun.NOMBRE_EN_USO);//Nombre en uso en una categoría en la que el usuario tiene permiso
				}
				else{
					erroresSW.addError(CategoriaDtoFormErrorsEmun.NOMBRE_EN_USO_OTRO_USUARIO); //Nombre en uso y el usuario NO lo ve
				}
			}
    	}
		
		Validador<CategoriaDto> validador= new CategoriaDtoValidador();
		
		validador.valida(categoriaDto, erroresSW);
		
		if(erroresSW.getHashErrors())
			return null;

		Categoria categoria=categoriaRepositorio.guarda(new CategoriaDto2CategoriaTransformer(mapper, categoriaRepositorio).transform(categoriaDto));
		
		//Se añade el permiso necesario para este usuario para este indicador, ya que el creador siempre llevara permiso implicito!
		//Solo al crear el indicador, despues de eso los permisos se quedan como estan
		if ( categoriaDto.getId()==0L)
			gestorCUUsuarioPermiso.guardaPermisoUsuario(idUsuario, categoria.getId());
		
		return new Categoria2CategoriaDtoTransformer(mapper).transform(categoria);
		
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPrimerNivelUsuarioVisualizar(Long idUsuario)
	{
		List<Long> listaIdCategoriasPrimerNivelPermiso = categoriaRepositorio.cargaIdsCategoriasPrimerNivelUsuarioPermisos(idUsuario);
		List<Categoria> lista = categoriaRepositorio.cargaCategoriasPrimerNivelUsuarioVisualizar(listaIdCategoriasPrimerNivelPermiso, idUsuario);
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		CategoriaDto categoriaDto;
		for(Categoria temp:lista){
			categoriaDto=transformer.transform(temp);
			if(listaIdCategoriasPrimerNivelPermiso.contains(categoriaDto.getId())){
				categoriaDto.setPermisoUserNoAdmin(true);
			}else{
				categoriaDto.setPermisoUserNoAdmin(false);
			}
			result.add(categoriaDto);
		}
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaSubCategoriasUsuarioVisualizar(Long idUsuario,Long idCategoriaPadre){
		List<Long> listaIdsSubCategoriasPermiso= categoriaRepositorio.cargaIdsSubCategoriasUsuarioPermisos(idUsuario, idCategoriaPadre);
		List<Categoria> lista= categoriaRepositorio.cargaSubCategoriasUsuarioVisualizar(listaIdsSubCategoriasPermiso, idUsuario, idCategoriaPadre);
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		CategoriaDto categoriaDto;
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		for(Categoria temp:lista){
			categoriaDto= transformer.transform(temp);
			if(listaIdsSubCategoriasPermiso.contains(categoriaDto.getId())){
				categoriaDto.setPermisoUserNoAdmin(true);
			}else{
				categoriaDto.setPermisoUserNoAdmin(false);
			}
			result.add(categoriaDto);
		}
		return result;
	}
	
	
	public EncapsuladorListSW<CategoriaDto> cargaSubCategoriasUsuarioPermisosEnPadre(Long idCategoriaPadre){
		List<Categoria> lista= categoriaRepositorio.cargaCategoriasPorPadre(idCategoriaPadre);
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		CategoriaDto categoriaDto;
		for(Categoria temp:lista){
			categoriaDto= transformer.transform(temp);
			categoriaDto.setPermisoUserNoAdmin(true);
			result.add(categoriaDto);
		}
		return result;
	}
	
	public void borra(Long idCategoria){
		categoriaRepositorio.borra(idCategoria);	
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPermisosUsuario(Long idUsuario){
		List<Categoria> lista= categoriaRepositorio.cargaCategoriasPermisosUsuario(idUsuario);
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		for(Categoria temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public List<CategoriaDto> cargaPorNombreLike(String nombreCategoria){
		List<Categoria> lista= categoriaRepositorio.cargaPorNombreLike(nombreCategoria);
		List<CategoriaDto> result= new ArrayList<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		for(Categoria temp:lista)
			result.add(transformer.transform(temp));
		return result;
		
	}
	
	public List<Long> cargaIdsCategoriasUsuarioPermisos(Long idUsuario){
		return categoriaRepositorio.cargaIdsCategoriasUsuarioPermisos(idUsuario);
	}
	
	public Boolean cargaExisteCategoriaConNombre(String nombre, Long idCatPadre, Long idCategoria){
		 return categoriaRepositorio.cargaCategoriaPorNombre(nombre,idCatPadre,idCategoria)!=null;
	}
	
	public Boolean cargaTienePermisoUsuarioEdicionCategoria(Long idCategoria, Long idUsuario){
		if(idUsuario==null || idUsuario==0L)
			return false;
		
		return gestorCUUsuarioPermiso.cargaEsUsuarioConPermisosVerCategoria(idCategoria, gestorCUUsuario.carga(idUsuario)); 
	}
	
	public Boolean cargaTienePermisoUsuarioEdicionIndicador(Long idIndicador, Long idUsuario){
		if(idUsuario==null || idUsuario==0L)
			return false;
		
		return gestorCUUsuarioPermiso.cargaEsUsuarioConPermisosEdicionIndicador(idIndicador, gestorCUUsuario.carga(idUsuario)); 
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaTodas(){
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		for(Categoria temp:categoriaRepositorio.cargaTodas())
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaTodas2PrimerosNiveles(){
		EncapsuladorListSW<CategoriaDto> result= new EncapsuladorListSW<CategoriaDto>();
		Categoria2CategoriaDtoTransformer transformer= new Categoria2CategoriaDtoTransformer(mapper);
		for(Categoria temp:categoriaRepositorio.cargaCategoriasPadre()){
			result.add(transformer.transform(temp));
			for(Categoria tempC:categoriaRepositorio.cargaCategoriasPorPadre(temp.getId()))
				result.add(transformer.transform(tempC));
		}
		return ordenaListaCategoria(result);
	}
	
	public EncapsuladorListSW<CategoriaDto> cargaCategoriasPermisosEdicion(Long idCategoriaPadre, Long idUsuario)
	{
		EncapsuladorListSW<CategoriaDto> listaCategorias = new EncapsuladorListSW<CategoriaDto>();
		
		// Usuarios registrados
		if(idUsuario != null && idUsuario != 0L)
		{
			UsuarioDto usuarioDto = gestorCUUsuario.carga(idUsuario);
			
			if(usuarioDto.getEsAdmin())
			{
				if (idCategoriaPadre != null && idCategoriaPadre <= 0L)
				{
					listaCategorias = cargaCategoriasPadre();
				}
				else
				{
					listaCategorias = cargaCategoriasPorPadre(idCategoriaPadre);
				}
			}
			else
			{
				if (idCategoriaPadre != null && idCategoriaPadre <= 0L)
				{

					listaCategorias = cargaCategoriasPrimerNivelUsuarioVisualizar(idUsuario);
				}
				else
				{
					listaCategorias = cargaSubCategoriasUsuarioVisualizar(idUsuario, idCategoriaPadre);
				}
			}
		}
		// Usuarios sin registrar (usuario invitado) => se cargan las categorías PUBLICADAS
		else
		{
			listaCategorias = this.cargaCategoriasPorPadrePublicados(idCategoriaPadre);
		}
		
		return listaCategorias;
	}
	
	//FUNCIONES INTERNAS
	
	private EncapsuladorListSW<CategoriaDto> ordenaListaCategoria(EncapsuladorListSW<CategoriaDto> lista){
		Collections.sort(lista, new Comparator<ElementoJerarquiaDto>() {

			public int compare(ElementoJerarquiaDto o1, ElementoJerarquiaDto o2) {
				return o1.getNombre().compareTo(o2.getNombre());
			}
		});
		return lista;
	}
	
	private void cargaCategoriasPermisoEdicion(List<CategoriaDto> categoriasInciales, List<CategoriaDto> result,List<Long> idsCategoriaPermiso,Boolean permisoCategoriaSuperior){
		
		if(permisoCategoriaSuperior){
			for(CategoriaDto temp:categoriasInciales){
				result.add(temp);
				cargaCategoriasPermisoEdicion(cargaCategoriasPorPadre(temp.getId()), result, idsCategoriaPermiso, permisoCategoriaSuperior);
			}
			return;
		}
		
		for(CategoriaDto temp:categoriasInciales){
			if(idsCategoriaPermiso.contains(temp.getId())){
				result.add(temp);
				cargaCategoriasPermisoEdicion(cargaCategoriasPorPadre(temp.getId()), result, idsCategoriaPermiso, true);
			}else{
				cargaCategoriasPermisoEdicion(cargaCategoriasPorPadre(temp.getId()), result, idsCategoriaPermiso, false);
			}
		}
	}
}
