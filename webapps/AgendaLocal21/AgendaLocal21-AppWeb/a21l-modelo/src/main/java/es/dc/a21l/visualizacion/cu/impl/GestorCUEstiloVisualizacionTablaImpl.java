/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicador;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuario;
import es.dc.a21l.elementoJerarquia.cu.IndicadorDto;
import es.dc.a21l.usuario.cu.GestorCUUsuario;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionTabla;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionTabla;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionTablaRepositorio;

public class GestorCUEstiloVisualizacionTablaImpl implements GestorCUEstiloVisualizacionTabla {
	
	private EstiloVisualizacionTablaRepositorio estiloVisualizacionTablaRepositorio;
	private GestorCUIndicador gestorCUIndicador;
	private GestorCUUsuario gestorCUUsuario;
	private GestorCUIndicadorUsuario gestorCUIndicadorUsuario;
	private Mapper mapper;
	
	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setGestorCUIndicador(GestorCUIndicador gestorCUIndicador) {
		this.gestorCUIndicador = gestorCUIndicador;
	}
	
	@Autowired
	public void setGestorCUUsuario(GestorCUUsuario gestorCUUsuario) {
		this.gestorCUUsuario = gestorCUUsuario;
	}
	
	@Autowired
	public void setEstiloVisualizacionTablaRepositorio(EstiloVisualizacionTablaRepositorio estiloVisualizacionTablaRepositorio) {
		this.estiloVisualizacionTablaRepositorio = estiloVisualizacionTablaRepositorio;
	}
	
	@Autowired
	public void setGestorCUIndicadorUsuario(GestorCUIndicadorUsuario gestorCUIndicadorUsuario) {
		this.gestorCUIndicadorUsuario = gestorCUIndicadorUsuario;
	}
	
	public EstiloVisualizacionTablaDto cargaPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador){
		//El estilo correspondiente es el del usuario logueado, pero sino tiene estilo definido
		//se muestra el estilo definido por el creador del indicador. Si tampoc tiene estilo definido,
		//se muestra el estilo por defecto.
		EstiloVisualizacionTablaDto estilo = new EstiloVisualizacionTabla2EstiloVisualizacionTablaDtoTransformer(mapper).transform(estiloVisualizacionTablaRepositorio.cargaPorIdUsuarioEIdIndicador(idUsuario,idIndicador));
		if (estilo==null || estilo.getId()<=0) {
			IndicadorDto indicador = gestorCUIndicador.cargaPorId(idIndicador);
			UsuarioDto usuarioCreador = gestorCUUsuario.cargaUsuarioPorLogin(indicador.getLoginCreador());
			if ( usuarioCreador == null || usuarioCreador.getId()<=0 )
				estilo = null;
			else {
				if ( gestorCUIndicadorUsuario.cargaEsUsuarioCreadorIndicador(idIndicador, usuarioCreador.getId())) {
					estilo = new EstiloVisualizacionTabla2EstiloVisualizacionTablaDtoTransformer(mapper).transform(estiloVisualizacionTablaRepositorio.cargaPorIdUsuarioEIdIndicador(usuarioCreador.getId(),idIndicador));
				} else
					estilo = null;
			}
		}
		return estilo;
	}
	
	public EstiloVisualizacionTablaDto guarda(EstiloVisualizacionTablaDto estiloVisualizacionTablaDto, EncapsuladorErroresSW erroresSW){
		//validador iria aqui si hiciese falta		
		if(erroresSW.getHashErrors())
			return null;
		EstiloVisualizacionTabla estiloActual = estiloVisualizacionTablaRepositorio.cargaPorIdUsuarioEIdIndicador(estiloVisualizacionTablaDto.getUsuario().getId(),estiloVisualizacionTablaDto.getIndicador().getId());
		EstiloVisualizacionTabla estilo = new EstiloVisualizacionTabla();
		if ( estiloActual!=null && estiloActual.getId()>0) {
			estiloActual.setDecimales(estiloVisualizacionTablaDto.getDecimales());
			estiloActual.setTamanhoFuente(estiloVisualizacionTablaDto.getTamanhoFuente());
			estiloActual.setTipoFecha(estiloVisualizacionTablaDto.getTipoFecha());
			estiloActual.setTipoFuente(estiloVisualizacionTablaDto.getTipoFuente());
			estilo = estiloVisualizacionTablaRepositorio.guarda(estiloActual);
		} else
			estilo = estiloVisualizacionTablaRepositorio.guarda(new EstiloVisualizacionTablaDto2EstiloVisualizacionTablaTransformer(mapper, estiloVisualizacionTablaRepositorio).transform(estiloVisualizacionTablaDto));
		return new EstiloVisualizacionTabla2EstiloVisualizacionTablaDtoTransformer(mapper).transform(estilo);
	}
	
	public EstiloVisualizacionTablaDto borra(Long id, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionTablaDto estiloDto = new EstiloVisualizacionTabla2EstiloVisualizacionTablaDtoTransformer(mapper).transform(estiloVisualizacionTablaRepositorio.carga(id));
		estiloVisualizacionTablaRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
	
	public EstiloVisualizacionTablaDto borraPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionTablaDto estiloDto = cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador);
		estiloVisualizacionTablaRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
}
