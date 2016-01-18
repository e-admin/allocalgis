/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.elementoJerarquia.modelo.Categoria;
import es.dc.a21l.elementoJerarquia.modelo.CategoriaRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquia;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquiaRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.Indicador;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.usuario.cu.GestorCUUsuarioPermiso;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.usuario.cu.UsuarioElementoJerarquiaDto;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquia;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class GestorCUUsuarioPermisoImpl implements GestorCUUsuarioPermiso {

	private static final Logger log = LoggerFactory.getLogger(GestorCUUsuarioPermisoImpl.class);
	private UsuarioElementoJerarquiaRepositorio usuarioEltoJerarquiaRepositorio;
	private Mapper mapper;
	private UsuarioRepositorio usuarioRepositorio;
	private ElementoJerarquiaRepositorio eltoJerarquiarolRepositorio;
	private CategoriaRepositorio categoriaRepositorio;
	private IndicadorRepositorio indicadorRepositorio;
	
	
	@Autowired
	public void setIndicadorRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}

	@Autowired
	public void setCategoriaRepositorio(CategoriaRepositorio categoriaRepositorio) {
		this.categoriaRepositorio = categoriaRepositorio;
	}

	@Autowired
	public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Autowired
	public void setEltoJeraquiaRepositorio(ElementoJerarquiaRepositorio eltoJerarquiarolRepositorio) {
		this.eltoJerarquiarolRepositorio = eltoJerarquiarolRepositorio;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setUsuarioEltoJerarquiaRepositorio(UsuarioElementoJerarquiaRepositorio usuarioEltoJerarquiaRepositorio) {
		this.usuarioEltoJerarquiaRepositorio = usuarioEltoJerarquiaRepositorio;
	}

	
	public EncapsuladorListSW<UsuarioElementoJerarquiaDto> cargarPermisosPorUsuario(Long idUsuario){
		List<UsuarioElementoJerarquia> lista = usuarioEltoJerarquiaRepositorio.cargarElementosJerarquiaPorUsuario(idUsuario);
		EncapsuladorListSW<UsuarioElementoJerarquiaDto> result = new EncapsuladorListSW<UsuarioElementoJerarquiaDto>();
		UsuarioElementoJerarquia2UsuarioElementoJerarquiaDtoTransformer transformer = new UsuarioElementoJerarquia2UsuarioElementoJerarquiaDtoTransformer(mapper);
		for(UsuarioElementoJerarquia temp:lista){
			result.add(transformer.transform(temp));
		}
		return result;
	}
	
	public void actualizarListaPermisosUsuario(Long idUsuario, List<Long> listaEltosJerarquia){
		List<Long> idsEJsUser= usuarioEltoJerarquiaRepositorio.cargarIdsEJsUsuario(idUsuario);
		List<Long> idsEjsUserTemp= new ArrayList<Long>(idsEJsUser);
		
		idsEjsUserTemp.removeAll(listaEltosJerarquia);
		usuarioEltoJerarquiaRepositorio.borrarListaEJsPorUsuario(idUsuario, idsEjsUserTemp);
		
		List<Long> listaEjsUserTemp = new ArrayList<Long>(listaEltosJerarquia);
		listaEjsUserTemp.removeAll(idsEJsUser);
		
		UsuarioElementoJerarquia usuarioEJ;
		Usuario usuario= usuarioRepositorio.carga(idUsuario);
		
		for(Long idEJ:listaEjsUserTemp){
			usuarioEJ= new UsuarioElementoJerarquia();
			usuarioEJ.setId(0L);
			usuarioEJ.setUsuario(usuario);
			usuarioEJ.setElementoJerarquia(eltoJerarquiarolRepositorio.carga(idEJ));
			usuarioEltoJerarquiaRepositorio.guarda(usuarioEJ);	
		}
	}
	
	public Boolean cargaEsUsuarioConPermisosVerCategoria(Long idCategoria, UsuarioDto usuarioDto){
		if(idCategoria==null || idCategoria==0L)
			return true;
		
		if(usuarioDto==null || usuarioDto.getEsAdmin())
			return true;
		
		List<Long> idsCategoriaPermiso=categoriaRepositorio.cargaIdsCategoriasUsuarioPermisos(usuarioDto.getId());
		Categoria categoria= categoriaRepositorio.carga(idCategoria);
		
		while(true){
			if(idsCategoriaPermiso.contains(categoria.getId()))
				return true;
			if(categoria.getCategoriaPadre()==null || categoria.getCategoriaPadre().getId()==0)
				return false;
			categoria= categoriaRepositorio.carga(categoria.getCategoriaPadre().getId());
		}
	}
	
	public Boolean cargaUsuarioConPermisosVerIndicador(Long idIndicador,UsuarioDto usuarioDto){
		if(usuarioDto!=null && usuarioDto.getEsAdmin())
			return true;
		
		if(usuarioDto==null){
			Indicador indicador= indicadorRepositorio.carga(idIndicador);
			return  indicador.getPublico() && !indicador.isPteAprobacionPublico();
		}
			
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			if(usuarioEltoJerarquiaRepositorio.cargaTienePermisosPropiosSobreIndicador(idIndicador, usuarioDto.getId()))
				return true;
			Indicador indicador= indicadorRepositorio.carga(idIndicador);
			if(indicador.getPublico() && !indicador.isPteAprobacionPublico())
				return  indicador.getCategoria()==null || cargaEsUsuarioConPermisosVerCategoria(indicador.getCategoria().getId(), usuarioDto);
		}
		
		return false;	
	}
	
	public Boolean cargaEsUsuarioConPermisosEdicionIndicador(Long idIndicador,UsuarioDto usuarioDto){
		if(usuarioDto!=null && usuarioDto.getEsAdmin())
			return true;
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			if(usuarioEltoJerarquiaRepositorio.cargaTienePermisosPropiosSobreIndicador(idIndicador, usuarioDto.getId()))
				return true;
		}
		
		return false;	
	}
	
	public Boolean esUsuarioConPermisosSobreIndicador(Long idIndicador,UsuarioDto usuarioDto){
		if(usuarioDto!=null && usuarioDto.getEsAdmin())
			return true;
		if(usuarioDto!=null && !usuarioDto.getEsAdmin()){
			if(usuarioEltoJerarquiaRepositorio.cargaTienePermisosPropiosSobreIndicador(idIndicador, usuarioDto.getId())){
				return true;
			}else{
				Indicador indicador= indicadorRepositorio.carga(idIndicador);
				if(indicador.getPublico() && !indicador.isPteAprobacionPublico()){
					return true;
				}
			}
		}
		
		return false;	
	}
	
	public Boolean guardaPermisoUsuario(Long idUsuario, Long idIndicador) {
		try {
			UsuarioElementoJerarquia entidad = new UsuarioElementoJerarquia();
			ElementoJerarquia eltoJerarquia = eltoJerarquiarolRepositorio.carga(idIndicador);
			entidad.setElementoJerarquia(eltoJerarquia);
			Usuario usuario = usuarioRepositorio.carga(idUsuario);
			entidad.setUsuario(usuario);
			usuarioEltoJerarquiaRepositorio.guarda(entidad);
			return true;
		} catch (Exception ex) {
			log.debug("Error al insertar permiso para el usuario :"+idUsuario+" y el indicador: "+idIndicador);
			return false;
		}
	}
}
