/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.dc.a21l.NEM.cu.GestorCUAtributoNEM;
import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorMapSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.base.utils.enumerados.TipoModificacionEnum;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.cu.ElementoJerarquiaDto;
import es.dc.a21l.elementoJerarquia.cu.GestorCUCategoria;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicador;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuario;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuarioModificacion;
import es.dc.a21l.elementoJerarquia.cu.IndicadorBusquedaAvanzadaDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.modelo.CategoriaRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.metadatos.cu.GestorCUMetadatos;
import es.dc.a21l.metadatos.cu.MetadatosDto;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.GestorCUUsuarioPermiso;
import es.dc.a21l.usuario.cu.UsuarioDto;

public class GestorCUIndicadorImpl implements GestorCUIndicador {

	private static final Logger LOG = LoggerFactory.getLogger(GestorCUIndicadorImpl.class);
	
	private Mapper mapper;
	private IndicadorRepositorio indicadorRepositorio;
	private CategoriaRepositorio categoriaRepositorio;
	private GestorCUUsuario gestorCUUsuario;
	private GestorCUMetadatos gestorCUMetadatos;
	private GestorCUCategoria gestorCUCategoria;
	private GestorCUIndicadorUsuario gestorCUIndicadorUsuario;
	private GestorCUIndicadorUsuarioModificacion gestorCUIndicadorUsuarioModificacion;
	private GestorCUAtributoNEM gestorCUAtributoNEM;
	private GestorCUUsuarioPermiso gestorCUUsuarioPermiso;
	
	
	@Autowired
	public void setGestorCUUsuarioPermiso(GestorCUUsuarioPermiso gestorCUUsuarioPermiso) {
		this.gestorCUUsuarioPermiso = gestorCUUsuarioPermiso;
	}

	@Autowired
	public void setGestorCUAtributoNEM(GestorCUAtributoNEM gestorCUAtributoNEM) {
		this.gestorCUAtributoNEM = gestorCUAtributoNEM;
	}

	@Autowired
	public void setGestorCUIndicadorUsuario(
			GestorCUIndicadorUsuario gestorCUIndicadorUsuario) {
		this.gestorCUIndicadorUsuario = gestorCUIndicadorUsuario;
	}

	@Autowired
	public void setGestorCUCategoria(GestorCUCategoria gestorCUCategoria) {
		this.gestorCUCategoria = gestorCUCategoria;
	}

	@Autowired
	public void setGestorCUMetadatos(GestorCUMetadatos gestorCUMetadatos) {
		this.gestorCUMetadatos = gestorCUMetadatos;
	}

	@Autowired
	public void setGestorCUUsuario(GestorCUUsuario gestorCUUsuario) {
		this.gestorCUUsuario = gestorCUUsuario;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	@Autowired
	public void setIndicadorRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}
	
	@Autowired
	public void setGestorCUIndicadorUsuarioModificacion(
			GestorCUIndicadorUsuarioModificacion gestorCUIndicadorUsuarioModificacion) {
		this.gestorCUIndicadorUsuarioModificacion = gestorCUIndicadorUsuarioModificacion;
	}

	@Autowired
	public void setCategoriaRepositorio(CategoriaRepositorio categoriaRepositorio) {
		this.categoriaRepositorio = categoriaRepositorio;
	}
	
	public IndicadorDto borra(Long id, EncapsuladorErroresSW erros) {
        IndicadorDto indicadorDto = cargaPorId(id);
        indicadorRepositorio.borra(id);
        return indicadorDto;
    }

	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoria(Long idCategoria){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoria(idCategoria);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoriaPorFuente(Long idCategoria, Long idFuente){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoriaPorFuente(idCategoria, idFuente);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoriaPendientesDePublico(Long idCategoria){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoriaPendientesDePublico(idCategoria);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoriaPendientesPublicacionWeb(Long idCategoria){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoriaPendientesPublicacionWeb(idCategoria);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoriaPublicos(Long idCategoria){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoriaPublicos(idCategoria);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresPorCategoriaPublicados(Long idCategoria){
		List<Indicador> lista = indicadorRepositorio.cargaIndicadoresPorCategoriaPublicados(idCategoria);
		EncapsuladorListSW<IndicadorDto> result = new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPendientesDePublico(){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPendientesDePublico();
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPendientesPublicacionWeb(){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPendientesPublicacionWeb();
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPublicos(){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPublicos();
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPublicados(){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPublicados();
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoria(){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoria();
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPorFuente(Long idFuente){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPorFuente(idFuente);
		EncapsuladorListSW<IndicadorDto> result=new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
	public IndicadorDto cargaPorId(Long id){
		return new Indicador2IndicadorDtoTransformer(mapper).transform(indicadorRepositorio.carga(id));
	}
	
	public IndicadorDto guarda(IndicadorDto indicadorDto, Long idUsuario, EncapsuladorErroresSW errores){
		if(indicadorDto.getNombre() !=null && existeNombreIndicador(indicadorDto.getNombre(), indicadorDto.getIdCategoria(), indicadorDto.getId())){
			if (gestorCUUsuario.esAdmin(idUsuario)){
				errores.addError(IndicadorDtoFormErrorsEmun.NOMBRE_EN_USO);//Nombre en uso en un indicador en el que el usuario tiene permiso
			}
			else{
				Indicador indicadorExistente = indicadorRepositorio.cargarIndicadorPorNombre(indicadorDto.getNombre(), indicadorDto.getIdCategoria(), indicadorDto.getId());
				List<Long> idsIndicadoresPermiso = cargarIdsIndicadoresUsuarioPermisos(idUsuario);
				boolean tienePermiso = false;
				for (Long idIndicador : idsIndicadoresPermiso){
					if (indicadorExistente.getId() == idIndicador){
						tienePermiso = true;
						break;
					}
				}
				if (tienePermiso){
					errores.addError(IndicadorDtoFormErrorsEmun.NOMBRE_EN_USO);//Nombre en uso en un indicador en el que el usuario tiene permiso
				}
				else{
					errores.addError(IndicadorDtoFormErrorsEmun.NOMBRE_EN_USO_OTRO_USUARIO); //Nombre en uso en un indicador sobre el que el usuario no tiene permiso
				}
			}
		}
		IndicadorDto indicadorAnterior = new Indicador2IndicadorDtoTransformer(mapper).transform(indicadorRepositorio.carga(indicadorDto.getId())); 
		if(indicadorDto.getId()!=0L)
			indicadorDto.setLoginCreador(indicadorAnterior.getLoginCreador());
		
		Validador<IndicadorDto> validador= new IndicadorDtoValidador();
		validador.valida(indicadorDto, errores);
		
		//Si no era publico y ahora es publico pq alguien lo hizo publico. Se pone pteAprobacionPublico a 1
//		if ( indicadorAnterior!=null && indicadorAnterior.getId()>0 && !indicadorAnterior.getPublico() && indicadorDto.getPublico()) {
//			indicadorDto.setPteAprobacionPublico(true);
//			indicadorDto.setPublico(false);
//		}
		
		//Si es un indicador Nuevo se pone pendienteAprobacionPublico a true de serie y pteaprobacion a false, etc.
		if ( indicadorDto.getId()<=0 ) {
			indicadorDto.setPteAprobacionPublico(true);
			indicadorDto.setPteAprobacion(false);
			indicadorDto.setNumUsuariosPublicacion(0);
			indicadorDto.setLoginUltimaPeticion("");
		}
		
		if(errores.getHashErrors())
			return null;
		Indicador indicador=indicadorRepositorio.guarda(new IndicadorDto2IndicadorTransformer(mapper, indicadorRepositorio, categoriaRepositorio).transform(indicadorDto));
		
		//seguarda el creador
		if(indicadorDto.getId()==0L){
			IndicadorUsuarioDto indicadorUsuarioDto = new IndicadorUsuarioDto();
			indicadorUsuarioDto.setIdIndicador(indicador.getId());
			indicadorUsuarioDto.setIdUsuario(gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador()).getId());
			gestorCUIndicadorUsuario.guarda(indicadorUsuarioDto, errores);
			indicadorUsuarioDto.setAccion(TipoModificacionEnum.CREACION.getTipoModificacion());
			gestorCUIndicadorUsuarioModificacion.guarda(indicadorUsuarioDto, errores);
			if(errores.getHashErrors()){
				indicadorRepositorio.borra(indicador.getId());
				return null;
			}
		}else{
			IndicadorUsuarioDto indicadorUsuarioDto = new IndicadorUsuarioDto();
			indicadorUsuarioDto.setIdIndicador(indicador.getId());
			indicadorUsuarioDto.setIdUsuario(gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador()).getId());
			indicadorUsuarioDto.setAccion(TipoModificacionEnum.MODIFICACION.getTipoModificacion());
			gestorCUIndicadorUsuarioModificacion.guarda(indicadorUsuarioDto, errores);
			if(errores.getHashErrors()){
				indicadorRepositorio.borra(indicador.getId());
				return null;
			}
		}
		
		//Se añade el permiso necesario para este usuario para este indicador, ya que el creador siempre llevara permiso implicito!
		//Solo al crear el indicador, despues de eso los permisos se quedan como estan
		if ( indicadorDto.getId()==0L)
			gestorCUUsuarioPermiso.guardaPermisoUsuario(gestorCUUsuario.cargaUsuarioPorLogin(indicadorDto.getLoginCreador()).getId(), indicador.getId());
		
		return new Indicador2IndicadorDtoTransformer(mapper).transform(indicador);
		
	}
	
	/**Comprueba si el nombre del indicador ya existe en BD*/
	public Boolean existeNombreIndicador(String nombre, Long idCategoria, Long idIndicador){
		 return indicadorRepositorio.cargarIndicadorPorNombre(nombre,idCategoria,idIndicador) != null;
	}
	
	/**Devuelve los ids de los indicadores sobre los que el usuario tiene permisos*/
	public List<Long> cargarIdsIndicadoresUsuarioPermisos(Long idUsuario){
		return indicadorRepositorio.cargarIdsIndicadoresUsuarioPermisos(idUsuario);
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(Long idUsuario)
	{
		EncapsuladorListSW<IndicadorDto> resultado = new EncapsuladorListSW<IndicadorDto>();
		
		// Si es un usuario registrado
		if (idUsuario != null && idUsuario > 0L)
		{
			List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(idUsuario);
			
			Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
			
			for(Indicador temp:lista)
			{
				resultado.add(transformer.transform(temp));
			}
		}
		// Usuario invitado => se cargan los indicadores PUBLICADOS
		else
		{
			resultado = this.cargaIndicadoresSinCategoriaPublicados();
		}
		
		return resultado;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresSinCategoriaPorPermisoUsuarioPorFuente(Long idUsuario, Long idFuente){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresSinCategoriaPorPermisoUsuarioPorFuente(idUsuario, idFuente);
		EncapsuladorListSW<IndicadorDto> result= new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		
		return result;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(Long idCategoria,Long idUsuario)
	{
		EncapsuladorListSW<IndicadorDto> resultado = new EncapsuladorListSW<IndicadorDto>();
		
		// Si es un usuario registrado
		if (idUsuario != null && idUsuario > 0L)
		{
			List<Indicador> lista= indicadorRepositorio.cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(idCategoria, idUsuario);
			Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
			
			for(Indicador temp:lista)
			{
				resultado.add(transformer.transform(temp));
			}
		}
		// Si no es un usuario registrado (es invitado) => se obtienen los indicadores PUBLICADOS
		else
		{
			resultado = this.cargaIndicadoresPorCategoriaPublicados(idCategoria);
		}
		
		return resultado;
	}
	
	public EncapsuladorListSW<IndicadorDto> cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(Long idUsuario,Long idCategoria){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(idUsuario, idCategoria);
		EncapsuladorListSW<IndicadorDto> result= new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		
		return result;
	}
	
	public List<IndicadorDto> cargaIndicadorePublicosPorCategoria(Long idCategoria){
		List<Indicador> lista= indicadorRepositorio.cargaIndicadorePublicosPorCategoria(idCategoria);
		EncapsuladorListSW<IndicadorDto> result= new EncapsuladorListSW<IndicadorDto>();
		Indicador2IndicadorDtoTransformer transformer= new Indicador2IndicadorDtoTransformer(mapper);
		for(Indicador temp:lista)
			result.add(transformer.transform(temp));
		
		return result;
	}
	
	public EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> cargaBusquedaDirecta(Long idUsuario, String criterio, String rutaMetadatos) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> result= new EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>();
		Indicador2IndicadorDtoTransformer transformerI= new Indicador2IndicadorDtoTransformer(mapper);
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		
		//usuario Invitado
		if(idUsuario == 0L)
		{
			//indicadores Raiz
			List<Indicador> listaIRaiz= indicadorRepositorio.cargaIndicadorePublicosPorCategoria(0L);
			EncapsuladorListSW<ElementoJerarquiaDto> listaIRaizDto= new EncapsuladorListSW<ElementoJerarquiaDto>();
			
			for(Indicador temp:listaIRaiz)
			{
				if(evaluaIndicadorPorCriteSimple(temp, criterio,rutaMetadatos))
				{
					temp.setNombre(arreglaAcentos(temp.getNombre()));
					listaIRaizDto.add(transformerI.transform(temp));
				}
			}
			
			if(!listaIRaizDto.isEmpty())
			{
				mapaIndicadores.put(0L, listaIRaizDto);
			}
			
			for(CategoriaDto tempC: gestorCUCategoria.cargaCategoriasPadre())
			{
				buscaTodosIndicadoresCriterioPorCategoria(0L, tempC, criterio, mapaIndicadores, mapaCategorias, transformerI, rutaMetadatos, true);
			}
			
			result.put("Indicadores", mapaIndicadores);
			result.put("Categorias",mapaCategorias);
			
			return result;
		}
		
		//usuario admin
		if(gestorCUUsuario.carga(idUsuario).getEsAdmin())
		{
			//indicadores Raiz
			List<Indicador> listaIRaiz= indicadorRepositorio.cargaIndicadoresSinCategoria();
			EncapsuladorListSW<ElementoJerarquiaDto> listaIRaizDto= new EncapsuladorListSW<ElementoJerarquiaDto>();
			
			for(Indicador temp:listaIRaiz)
			{
				if(evaluaIndicadorPorCriteSimple(temp, criterio,rutaMetadatos))
				{
					temp.setNombre(arreglaAcentos(temp.getNombre()));
					listaIRaizDto.add(transformerI.transform(temp));
				}
			}
			
			if(!listaIRaizDto.isEmpty())
			{
				mapaIndicadores.put(0L, listaIRaizDto);
			}
			
			for(CategoriaDto tempC: gestorCUCategoria.cargaCategoriasPadre())
			{
				buscaTodosIndicadoresCriterioPorCategoria(0L, tempC, criterio, mapaIndicadores, mapaCategorias,transformerI,rutaMetadatos,false);
			}
		}
		else
		{
			//usuario no admin
			List<Indicador> listaIRaiz= indicadorRepositorio.cargaIndicadorePublicosPorCategoria(0L);
			EncapsuladorListSW<ElementoJerarquiaDto> listaIRaizDto= new EncapsuladorListSW<ElementoJerarquiaDto>();
			
			for(Indicador temp:listaIRaiz)
			{
				if(evaluaIndicadorPorCriteSimple(temp, criterio,rutaMetadatos))
				{
					temp.setNombre(arreglaAcentos(temp.getNombre()));
					listaIRaizDto.add(transformerI.transform(temp));
				}
			}
			
			if(!listaIRaizDto.isEmpty())
			{
				mapaIndicadores.put(0L, listaIRaizDto);
			}
			
			for(CategoriaDto tempC:gestorCUCategoria.cargaCategoriasPermisosUsuario(idUsuario))
			{
				if(buscaTodosIndicadoresCriterioPorCategoriaUserNoAdmin(tempC.getIdCategoriaPadre()==null?0L:tempC.getIdCategoriaPadre(), tempC, criterio, mapaIndicadores, mapaCategorias, transformerI, rutaMetadatos))
				{
					rellenaArlboHaciaArriba(tempC, mapaCategorias,true);
				}
			}
			
			IndicadorDto indicadorDto;
			Long idCategoria;
			
			for(Indicador tempI:indicadorRepositorio.cargaIndicadorePorPermisoUsuario(idUsuario))
			{
				if(evaluaIndicadorPorCriteSimple(tempI, criterio, rutaMetadatos))
				{
					indicadorDto=transformerI.transform(tempI);
					idCategoria=indicadorDto.getIdCategoria()==null?0L:indicadorDto.getIdCategoria();
					
					if(mapaIndicadores.get(idCategoria)!=null)
					{
						if(!mapaIndicadores.get(idCategoria).contains(indicadorDto))
						{
							mapaIndicadores.get(idCategoria).add(indicadorDto);
							ordenaListaElementoJerarquia(mapaIndicadores.get(idCategoria));
						}
					}
					else
					{
						EncapsuladorListSW<ElementoJerarquiaDto> lista= new EncapsuladorListSW<ElementoJerarquiaDto>();
						lista.add(indicadorDto);
						mapaIndicadores.put(idCategoria, lista);
					}
					if(idCategoria!=0)
					{
						rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(idCategoria), mapaCategorias,true);
					}
				}
			}
		}
		
		result.put("Indicadores", mapaIndicadores);
		result.put("Categorias",mapaCategorias);
		
		return result;
	}
	
	public EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> cargaBusquedaAvanzada(Long idUsuario,IndicadorBusquedaAvanzadaDto criterio,String rutaMetadatos) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> result= new EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>();
		Indicador2IndicadorDtoTransformer transformerI= new Indicador2IndicadorDtoTransformer(mapper);
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		List<Long> idsIndicadoresCoincidentes= new ArrayList<Long>();
		
		//caso usuario invitado
		UsuarioDto usuarioDto=null;
		if(idUsuario!=null)
			usuarioDto=gestorCUUsuario.carga(idUsuario);
		
		//Indicadores incluidos en categorias
		for(IndicadorDto temp: cargaIndicadoresIncluidoCategoriaUser(idsIndicadoresCoincidentes, criterio.getCategoriaContenedora(), usuarioDto, transformerI)){
			temp.setNombre(arreglaAcentos(temp.getNombre()));
			insertaIndicadorEnMapa(temp, mapaIndicadores);
			rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(temp.getIdCategoria()), mapaCategorias,true);
		}
		
		//Indicadores por nombre descripcion y loginCreador
		for(IndicadorDto temp:cargaIndicadoresPorNombreOdescripcionOLoginCreadorUser(idsIndicadoresCoincidentes, criterio.getNombre(), criterio.getDescripcion(), criterio.getUsuarioCreador(), usuarioDto, transformerI)){
			temp.setNombre(arreglaAcentos(temp.getNombre()));
			insertaIndicadorEnMapa(temp, mapaIndicadores);
			if(temp.getIdCategoria()!=null)
				rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(temp.getIdCategoria()), mapaCategorias,true);
		}
		
		//Indicadores por expresion NEM
		
		for(IndicadorDto temp:cargaIndicadoresEvaluacionNEM(idsIndicadoresCoincidentes, criterio.getIdsFiltroMD(), criterio.getValoresFiltroMD(), usuarioDto, transformerI, rutaMetadatos)){
			temp.setNombre(arreglaAcentos(temp.getNombre()));
			insertaIndicadorEnMapa(temp, mapaIndicadores);
			if(temp.getIdCategoria()!=null)
				rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(temp.getIdCategoria()), mapaCategorias,true);
		}
		
		result.put("Indicadores", mapaIndicadores);
		result.put("Categorias",mapaCategorias);
		return result;
	}
	
	
	public EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> cargaTodasCategoriasYIndicadoresPorUsuarioVisualizar(Long idUsuario){
		EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> result= new EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		
		EncapsuladorListSW<ElementoJerarquiaDto> listaIndiRaiz=new EncapsuladorListSW<ElementoJerarquiaDto>();
		
		if(idUsuario==null || idUsuario==0L){
			//invitado
			listaIndiRaiz.addAll(cargaIndicadoresSinCategoriaPublicados());
		}else{
		//User
			listaIndiRaiz.addAll(cargaIndicadoresSinCategoriaPublicosOPorPermisoUsuario(idUsuario));
		}
		mapaIndicadores.put(0L, listaIndiRaiz);
		cargaEJVisualizarPorUsuario(gestorCUCategoria.cargaCategoriasPadre(), idUsuario, mapaIndicadores, mapaCategorias, gestorCUCategoria.cargaIdsCategoriasUsuarioPermisos(idUsuario), false);
		
		result.put("Indicadores", mapaIndicadores);
		result.put("Categorias",mapaCategorias);
		
		return result;
	}

	
	public EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> cargaTodasCategoriasYIndicadoresPorUsuarioVisualizarPorFuente(Long idUsuario, Long idFuente)
	{
		EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>> result= new EncapsuladorMapSW<String, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias= new EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>>();
		
		creaArbolApartirDeIndicadoresYUsuarioNoAdmin(gestorCUUsuario.carga(idUsuario), indicadorRepositorio.cargaIndicadoresPorFuente(idFuente), mapaIndicadores, mapaCategorias);
			
		result.put("Indicadores", mapaIndicadores);
		result.put("Categorias",mapaCategorias);
		
		return result;
	}
	
	
	//FUNCIONES INTERNAS
	
	
	private void creaArbolApartirDeIndicadoresYUsuarioNoAdmin(UsuarioDto usuarioDto,List<Indicador> listaIndicadores,EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias) 
	{
		Indicador2IndicadorDtoTransformer transformer = new Indicador2IndicadorDtoTransformer(mapper);
		
		for(Indicador temp:listaIndicadores)
		{
			if(gestorCUUsuarioPermiso.cargaUsuarioConPermisosVerIndicador(temp.getId(), usuarioDto))
			{
				insertaIndicadorEnMapa(transformer.transform(temp), mapaIndicadores);
				
				if(temp.getCategoria() != null)
				{
					rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(temp.getCategoria().getId()), mapaCategorias,false);
				}
			}
		}
	}
	
	
	private Boolean cargaEJVisualizarPorUsuario(List<CategoriaDto> categorias,Long idUsuario, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias, List<Long> idsCategoriaUsuarioPermiso,boolean permisoCategoriaPadre){

		Boolean result= false;
		
		if(idUsuario!=0){
			for(CategoriaDto tempC:categorias){
				tempC.setPermisoUserNoAdmin(permisoCategoriaPadre || idsCategoriaUsuarioPermiso.contains(tempC.getId()));
				Boolean permisosInferiores=cargaEJVisualizarPorUsuario(gestorCUCategoria.cargaCategoriasPorPadre(tempC.getId()), idUsuario, mapaIndicadores, mapaCategorias, idsCategoriaUsuarioPermiso,tempC.getPermisoUserNoAdmin());
				if(insertaIndicadoresPorCateogriaYUsuarioPermisos(idUsuario, tempC.getId(), mapaIndicadores,tempC.getPermisoUserNoAdmin()) ||permisosInferiores || tempC.getPermisoUserNoAdmin()){
					insertaCategoriaEnMapa(tempC, mapaCategorias);
					result=true;
				}
			}
			
		}else{
			for(CategoriaDto tempC:categorias){
				tempC.setPermisoUserNoAdmin(false);
				Boolean permisosInferiores=cargaEJVisualizarPorUsuario(gestorCUCategoria.cargaCategoriasPorPadre(tempC.getId()), idUsuario, mapaIndicadores, mapaCategorias, idsCategoriaUsuarioPermiso,tempC.getPermisoUserNoAdmin());
				if(insertaIndicadoresPorCateogriaYUsuarioPermisos(idUsuario, tempC.getId(), mapaIndicadores,tempC.getPermisoUserNoAdmin()) ||permisosInferiores){
					insertaCategoriaEnMapa(tempC, mapaCategorias);
					result=true;
				}
			}
		}
		return result;
	}
	
	private Boolean insertaIndicadoresPorCateogriaYUsuarioPermisos(Long idUsuario, Long idCategoria, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores,Boolean permisoCategoria){
		List<IndicadorDto> lista=null;
		if(idUsuario==0L){
				lista=cargaIndicadorePublicosPorCategoria(idCategoria);
		}else{
			if(permisoCategoria){
				lista= cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(idCategoria, idUsuario);
			}else{
				lista=cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(idUsuario, idCategoria);
			}
		}
		
		 for(IndicadorDto iTmp:lista)
			 insertaIndicadorEnMapa(iTmp, mapaIndicadores);
		 
		 return !lista.isEmpty();
	}
	
	private void insertaCategoriaEnMapa(CategoriaDto categoriaDto, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias){
		Long idCategoriaPadre=categoriaDto.getIdCategoriaPadre()!=null?categoriaDto.getIdCategoriaPadre():0L;
		
		if(mapaCategorias.get(idCategoriaPadre)!=null){
			if(mapaCategorias.get(idCategoriaPadre).contains(categoriaDto))
				return;
			mapaCategorias.get(idCategoriaPadre).add(categoriaDto);
			ordenaListaElementoJerarquia(mapaCategorias.get(idCategoriaPadre));
			
		}else{
			EncapsuladorListSW<ElementoJerarquiaDto> lista= new EncapsuladorListSW<ElementoJerarquiaDto>();
			lista.add(categoriaDto);
			mapaCategorias.put(idCategoriaPadre, lista);
		}
	}
	

	
	private List<IndicadorDto> cargaIndicadoresEvaluacionNEM (List<Long> idsIndicadoresCoincidentes, List<Long> idsAtributosNEM, List<String> valoresNEM, UsuarioDto usuarioDto, Indicador2IndicadorDtoTransformer transformerIndicador, String path) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException{		List<IndicadorDto> result= new ArrayList<IndicadorDto>();
		
		//no hay criterios
		if(valoresNEM==null || valoresNEM.isEmpty())
			return result;
		
		List<String> nodos= new ArrayList<String>();
		for(Long idAN:idsAtributosNEM)
			nodos.add(gestorCUAtributoNEM.cargaPorId(idAN).getExpresionXpath());
		
		List<Indicador> indicadoresConMetadatos=null;
		
		if(usuarioDto==null)
			indicadoresConMetadatos= indicadorRepositorio.cargaPublicosConMetadosYExcluidos(idsIndicadoresCoincidentes);
		
		if(usuarioDto!=null && usuarioDto.getEsAdmin())
			indicadoresConMetadatos= indicadorRepositorio.cargaConMetadosYExcluidos(idsIndicadoresCoincidentes);
		
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			indicadoresConMetadatos= indicadorRepositorio.cargaPermisosUsuarioConMetadatosYExcluidos(idsIndicadoresCoincidentes, usuarioDto.getId());
			List<Indicador> indicadoresPublicos= indicadorRepositorio.cargaPublicosConMetadosYExcluidos(idsIndicadoresCoincidentes);
			indicadoresPublicos.removeAll(indicadoresConMetadatos);
			for(Indicador temp:indicadoresPublicos){
				if(gestorCUUsuarioPermiso.cargaUsuarioConPermisosVerIndicador(temp.getId(), usuarioDto))
					indicadoresConMetadatos.add(temp);
			}
		}
		
		for(Indicador indicador:indicadoresConMetadatos){
			Document doc= cargaFicheroMetadatosIndicador(indicador, path);
			for(int i=0;i<nodos.size();i++){
				if (evaluaIndicadorCo(nodos.get(i), valoresNEM.get(i), doc)){
					idsIndicadoresCoincidentes.add(indicador.getId());
					result.add(transformerIndicador.transform(indicador));
					break;
				}
			}
		}
		
		return result;
	}
	
	private Document cargaFicheroMetadatosIndicador(Indicador indicador,String path) throws SAXException, IOException, ParserConfigurationException{

		MetadatosDto metadatosDto= gestorCUMetadatos.cargaPorIdIndicador(indicador.getId());
		File contenidoMetadatos= cargaFicheroMetadatos(path, indicador.getId(), metadatosDto.getMetadatos());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc=null;
			try{
				doc= builder.parse(contenidoMetadatos);
			}catch (FileNotFoundException e) {
				LOG.error("No se encuentra el fichero para el indicador: id= "+indicador.getId());
				e.printStackTrace();
				return doc;
			}
		return doc;
			
	}
	
	
	private Boolean evaluaIndicadorCo(String nodoMetadatos, String criterio, Document doc) throws XPathExpressionException{
		
		XPath xpath = XPathFactory.newInstance().newXPath();
        String expresionEvaluar= "//"+nodoMetadatos+"//text() ";
    	XPathExpression expr = xpath.compile(expresionEvaluar);
    	NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

    	for (int i = 0; i < nodes.getLength(); i++) {
    		if(nodes.item(i).getNodeValue().contains(criterio))
    			return true;
    	}
    	return false;
	}
	
	
	
	private List<IndicadorDto> cargaIndicadoresPorNombreOdescripcionOLoginCreadorUser(List<Long> idsIndicadoresCoincidentes,String nombre, String descripcion, String LoginCreador,UsuarioDto usuarioDto,Indicador2IndicadorDtoTransformer transformerIndicador){
		List<IndicadorDto> result= new ArrayList<IndicadorDto>();
		
		
		if(usuarioDto==null){
			//Los indicadores q ve el invitado son los publicados
			for(Indicador temp:indicadorRepositorio.cargaPublicadosPorNombreOdescripcionOUsuarioCreador(nombre, descripcion, LoginCreador, idsIndicadoresCoincidentes)){
				idsIndicadoresCoincidentes.add(temp.getId());
				result.add(transformerIndicador.transform(temp));
			}
			return result;
		}
		
		if( usuarioDto!=null  && usuarioDto.getEsAdmin()){
			for(Indicador temp:indicadorRepositorio.cargaPorNombreOdescripcionOUsuarioCreador(nombre, descripcion, LoginCreador, idsIndicadoresCoincidentes)){
				idsIndicadoresCoincidentes.add(temp.getId());
				result.add(transformerIndicador.transform(temp));
			}
			return result;	
		}
		
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			for(Indicador temp:indicadorRepositorio.cargaPorNombreOdescripcionOUsuarioCreador(nombre, descripcion, LoginCreador, idsIndicadoresCoincidentes)){
				if(gestorCUUsuarioPermiso.cargaUsuarioConPermisosVerIndicador(temp.getId(), usuarioDto)){
					idsIndicadoresCoincidentes.add(temp.getId());
					result.add(transformerIndicador.transform(temp));
				}
			}
		}
		
		
		return result;
	}
	
	
	private List<IndicadorDto> cargaIndicadoresIncluidoCategoriaUser(List<Long> idsIndicadoresCoincidentes,String nombreCategoria,UsuarioDto usuarioDto,Indicador2IndicadorDtoTransformer transformerIndicador){
		List<IndicadorDto> result= new ArrayList<IndicadorDto>();
		
		if(StringUtils.isBlank(nombreCategoria))
			return result;
		
		for(CategoriaDto temp: gestorCUCategoria.cargaPorNombreLike(nombreCategoria)){
			result.addAll(cargaIndicadoresSubCategoriasPorUsuario(temp, idsIndicadoresCoincidentes, usuarioDto, transformerIndicador));
		}
			
		return result;
	}
	
	private List<IndicadorDto> cargaIndicadoresSubCategoriasPorUsuario(CategoriaDto categoriaDto,List<Long> idsIndicadoresCoincidente,UsuarioDto usuarioDto,Indicador2IndicadorDtoTransformer transformerIndicador){
		
		List<IndicadorDto> result= new ArrayList<IndicadorDto>();
		
		 List<Indicador> listaI=cargaIndicadoresPorCategoriaPermisoUsuario(categoriaDto, usuarioDto);
		
		for(Indicador temp:listaI){
			if(!idsIndicadoresCoincidente.contains(temp.getId())){
				idsIndicadoresCoincidente.add(temp.getId());
				result.add((transformerIndicador.transform(temp)));
			}
		}
		
		for(CategoriaDto tempC: gestorCUCategoria.cargaCategoriasPorPadre(categoriaDto.getId()))
			result.addAll(cargaIndicadoresSubCategoriasPorUsuario(tempC, idsIndicadoresCoincidente, usuarioDto, transformerIndicador));
		
		return result;
	}
	
	private List<Indicador> cargaIndicadoresPorCategoriaPermisoUsuario(CategoriaDto categoriaDto,UsuarioDto usuarioDto){
		List<Indicador> result=null;
		
		if(usuarioDto==null){
			result=indicadorRepositorio.cargaIndicadorePublicosPorCategoria(categoriaDto.getId());
		}
		
		if(usuarioDto!=null && usuarioDto.getEsAdmin()){
			result=indicadorRepositorio.cargaIndicadoresPorCategoria(categoriaDto.getId());
		}
		
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			if(gestorCUUsuarioPermiso.cargaEsUsuarioConPermisosVerCategoria(categoriaDto.getId(), usuarioDto)){
				result=indicadorRepositorio.cargaIndicadoresConCategoriaPublicosOPorPermisoUsuario(categoriaDto.getId(), usuarioDto.getId());
			}else{
				result=indicadorRepositorio.cargaIndicadoresConCategoriaNoPublicosPorPermisoUsuario(usuarioDto.getId(), categoriaDto.getId());
			}
		}
		
		return result;
	}
	
	private Boolean buscaTodosIndicadoresCriterioPorCategoriaUserNoAdmin(Long idCategoriaPadre,CategoriaDto categoriaDto,String criterio,EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias, Indicador2IndicadorDtoTransformer transformerIndicador,String rutaMetadatos) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		Boolean resultado=false;
		List<Indicador> listaI= indicadorRepositorio.cargaIndicadorePublicosPorCategoria(categoriaDto.getId());
		EncapsuladorListSW<ElementoJerarquiaDto> listaIndicadorDtos= mapaIndicadores.get(categoriaDto.getId())==null?new EncapsuladorListSW<ElementoJerarquiaDto>():mapaIndicadores.get(categoriaDto.getId());
		IndicadorDto indicadorDto;
		for(Indicador tempI:listaI){
			if(evaluaIndicadorPorCriteSimple(tempI, criterio,rutaMetadatos)){
				indicadorDto=transformerIndicador.transform(tempI);
				resultado=true;
				if(!listaIndicadorDtos.contains(indicadorDto))
					listaIndicadorDtos.add(indicadorDto);
			}
		}
		if( mapaIndicadores.get(categoriaDto.getId())==null&&resultado){
			mapaIndicadores.put(categoriaDto.getId(), listaIndicadorDtos);
		}
		
		for(CategoriaDto tempC:gestorCUCategoria.cargaCategoriasPorPadre(categoriaDto.getId()) ){
			if(buscaTodosIndicadoresCriterioPorCategoriaUserNoAdmin(categoriaDto.getId(), tempC, criterio, mapaIndicadores, mapaCategorias, transformerIndicador,rutaMetadatos))
				resultado=true;
		}
		
		if(resultado){
			if(mapaCategorias.get(idCategoriaPadre)!=null){
				if(!mapaCategorias.get(idCategoriaPadre).contains(categoriaDto))
					mapaCategorias.get(idCategoriaPadre).add(categoriaDto);
			}else{
				EncapsuladorListSW<ElementoJerarquiaDto> listaC= new EncapsuladorListSW<ElementoJerarquiaDto>();
				listaC.add(categoriaDto);
				mapaCategorias.put(idCategoriaPadre, listaC);
			}
		}
		
		return resultado;
	}
	
	private Boolean buscaTodosIndicadoresCriterioPorCategoria(Long idCategoriaPadre, CategoriaDto categoriaDto, String criterio, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias, Indicador2IndicadorDtoTransformer transformerIndicador,String rutaMetadatos,Boolean usuarioInvitado) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
	{
		Boolean resultado = false;
		List<Indicador> listaI;
		
		if(!usuarioInvitado)
		{
			listaI= indicadorRepositorio.cargaIndicadoresPorCategoria(categoriaDto.getId());
		}
		else
		{
			listaI= indicadorRepositorio.cargaIndicadorePublicosPorCategoria(categoriaDto.getId());
		}
		
		EncapsuladorListSW<ElementoJerarquiaDto> listaIndicadorDtos= new EncapsuladorListSW<ElementoJerarquiaDto>();
		
		for(Indicador tempI:listaI)
		{
			if(evaluaIndicadorPorCriteSimple(tempI, criterio,rutaMetadatos))
			{
				tempI.setNombre(arreglaAcentos(tempI.getNombre()));
				listaIndicadorDtos.add(transformerIndicador.transform(tempI));
			}
		}
		
		if(!listaIndicadorDtos.isEmpty())
		{
			resultado=true;
			mapaIndicadores.put(categoriaDto.getId(), listaIndicadorDtos);
		}
		
		for(CategoriaDto tempC:gestorCUCategoria.cargaCategoriasPorPadre(categoriaDto.getId()))
		{
			if(buscaTodosIndicadoresCriterioPorCategoria(categoriaDto.getId(), tempC, criterio, mapaIndicadores, mapaCategorias, transformerIndicador,rutaMetadatos,usuarioInvitado))
			{
				resultado=true;
			}
		}
		
		if(resultado)
		{
			categoriaDto.setNombre(arreglaAcentos(categoriaDto.getNombre()));
			if(mapaCategorias.get(idCategoriaPadre)!=null)
			{
				mapaCategorias.get(idCategoriaPadre).add(categoriaDto);
			}
			else
			{
				EncapsuladorListSW<ElementoJerarquiaDto> listaC = new EncapsuladorListSW<ElementoJerarquiaDto>();
				listaC.add(categoriaDto);
				mapaCategorias.put(idCategoriaPadre, listaC);
			}
		}
		
		return resultado;
	}
	
	public static String eliminaAcentos(String string) {
	    return Normalizer.normalize(string, Form.NFD)
	        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
	public static String arreglaAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String []original = {"á","é","í","ó","ú","Á","É","Í","Ó","Ú","ñ","Ñ"};
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String [] ascii = {"&aacute;","&eacute;","&iacute;","&oacute;","&uacute;","&Aacute;","&Eacute;","&Iacute;","&Oacute;","&Uacute;","&ntilde;","&Ntilde;"};
	    String output = input;
	    for (int i=0; i<original.length; i++) {
	        // Reemplazamos los caracteres especiales.
	       output = output.replace(original[i], ascii[i]);
	    }//for i
	    return output;
	}
	private Boolean evaluaIndicadorPorCriteSimple(Indicador indicador,String criterio,String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		String nomeIndicador=eliminaAcentos(indicador.getNombre());
		String nomeCriterio=eliminaAcentos(criterio);
		if(nomeIndicador.toLowerCase().contains(nomeCriterio.toLowerCase()))
			return true;
		MetadatosDto metadatosDto= gestorCUMetadatos.cargaPorIdIndicador(indicador.getId());
		if(metadatosDto!=null){
			if(StringUtils.isBlank(metadatosDto.getMetadatos()))
				return false;
			File contenidoMetadatos= cargaFicheroMetadatos(path, indicador.getId(), metadatosDto.getMetadatos());
			if(contenidoMetadatos==null)
				return false;
			
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc=null;
			try{
				doc= builder.parse(contenidoMetadatos);
			}catch (FileNotFoundException e) {
				LOG.error("No se encuentra el fichero para el indicador: id= "+indicador.getId());
				e.printStackTrace();
				return false;
			} catch ( Exception ex ) {
				LOG.error("El fichero XML de metadatos no es correcto");
				ex.printStackTrace();
				return false;
			}
			
			XPath xpath = XPathFactory.newInstance().newXPath();
	        String expresionEvaluar= "//text() ";
	    	XPathExpression expr = xpath.compile(expresionEvaluar);
	    	NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

	    	for (int i = 0; i < nodes.getLength(); i++) {
	    		if(nodes.item(i).getNodeValue().contains(criterio))
	    			return true;
	    	}
		}
		return false;
		
	}
	
	
	private void rellenaArlboHaciaArriba(CategoriaDto categoriaDto, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaCategorias,boolean esBusqueda)
	{
		if(esBusqueda){
			categoriaDto.setNombre(arreglaAcentos(categoriaDto.getNombre()));
		}
		Long idCategoriaPadre = categoriaDto.getIdCategoriaPadre() == null ? 0L : categoriaDto.getIdCategoriaPadre();
		
		if(mapaCategorias.get(idCategoriaPadre)!=null)
		{
			if(mapaCategorias.get(idCategoriaPadre).contains(categoriaDto))
			{
				return;
			}
			
			mapaCategorias.get(idCategoriaPadre).add(categoriaDto);
			ordenaListaElementoJerarquia(mapaCategorias.get(idCategoriaPadre));
		}
		else
		{
			EncapsuladorListSW<ElementoJerarquiaDto> list= new EncapsuladorListSW<ElementoJerarquiaDto>();
			list.add(categoriaDto);
			mapaCategorias.put(idCategoriaPadre, list);
		}
		
		if(idCategoriaPadre!=0)
		{
			rellenaArlboHaciaArriba(gestorCUCategoria.cargaPorId(idCategoriaPadre), mapaCategorias,esBusqueda);
		}
	}
	
	private void insertaIndicadorEnMapa(IndicadorDto indicadorDto, EncapsuladorMapSW<Long, EncapsuladorListSW<ElementoJerarquiaDto>> mapaIndicadores)
	{
		Long idCategoria = indicadorDto.getIdCategoria() != null ? indicadorDto.getIdCategoria() : 0L;
		
		if(mapaIndicadores.get(idCategoria)!=null)
		{
			if(mapaIndicadores.get(idCategoria).contains(indicadorDto))
			{
				return;
			}
			
			mapaIndicadores.get(idCategoria).add(indicadorDto);
			ordenaListaElementoJerarquia(mapaIndicadores.get(idCategoria));
		}
		else
		{
			EncapsuladorListSW<ElementoJerarquiaDto> lista= new EncapsuladorListSW<ElementoJerarquiaDto>();
			lista.add(indicadorDto);
			mapaIndicadores.put(idCategoria, lista);
		}
	}
	
	private EncapsuladorListSW<ElementoJerarquiaDto> ordenaListaElementoJerarquia(EncapsuladorListSW<ElementoJerarquiaDto> lista)
	{
		Collections.sort(lista, new Comparator<ElementoJerarquiaDto>() {

			public int compare(ElementoJerarquiaDto o1, ElementoJerarquiaDto o2) 
			{
				return o1.getNombre().compareTo(o2.getNombre());
			}
		});
		
		return lista;
	}
	
	private File cargaFicheroMetadatos(String path,Long idIndicador,String nombreFichero)
	{
		return new File(path+idIndicador+"/"+nombreFichero);
	}
	
}
