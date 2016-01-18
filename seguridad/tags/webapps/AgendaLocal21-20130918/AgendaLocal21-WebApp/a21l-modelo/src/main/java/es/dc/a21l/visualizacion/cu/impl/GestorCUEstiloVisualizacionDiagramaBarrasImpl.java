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
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarrasRepositorio;

public class GestorCUEstiloVisualizacionDiagramaBarrasImpl implements GestorCUEstiloVisualizacionDiagramaBarras {
	
	private EstiloVisualizacionDiagramaBarrasRepositorio estiloVisualizacionDiagramaBarrasRepositorio;
	private Mapper mapper;
	private GestorCUIndicador gestorCUIndicador;
	private GestorCUUsuario gestorCUUsuario;
	private GestorCUIndicadorUsuario gestorCUIndicadorUsuario;
	
	@Autowired
	public void setGestorCUIndicadorUsuario(GestorCUIndicadorUsuario gestorCUIndicadorUsuario) {
		this.gestorCUIndicadorUsuario = gestorCUIndicadorUsuario;
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
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setEstiloVisualizacionDiagramaBarrasRepositorio(EstiloVisualizacionDiagramaBarrasRepositorio estiloVisualizacionDiagramaBarrasRepositorio) {
		this.estiloVisualizacionDiagramaBarrasRepositorio = estiloVisualizacionDiagramaBarrasRepositorio;
	}
	
	public EstiloVisualizacionDiagramaBarrasDto cargaPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador){
		//El estilo correspondiente es el del usuario logueado, pero sino tiene estilo definido
		//se muestra el estilo definido por el creador del indicador. Si tampoc tiene estilo definido,
		//se muestra el estilo por defecto.
		EstiloVisualizacionDiagramaBarrasDto estilo = new EstiloVisualizacionDiagramaBarras2EstiloVisualizacionDiagramaBarrasDtoTransformer(mapper).transform(estiloVisualizacionDiagramaBarrasRepositorio.cargaPorIdUsuarioEIdIndicador(idUsuario,idIndicador));
		if (estilo==null || estilo.getId()<=0) {
			IndicadorDto indicador = gestorCUIndicador.cargaPorId(idIndicador);
			UsuarioDto usuarioCreador = gestorCUUsuario.cargaUsuarioPorLogin(indicador.getLoginCreador());
			if ( usuarioCreador == null || usuarioCreador.getId()<=0 )
				estilo = null;
			else {
				if ( gestorCUIndicadorUsuario.cargaEsUsuarioCreadorIndicador(idIndicador, usuarioCreador.getId())) {
					estilo = new EstiloVisualizacionDiagramaBarras2EstiloVisualizacionDiagramaBarrasDtoTransformer(mapper).transform(estiloVisualizacionDiagramaBarrasRepositorio.cargaPorIdUsuarioEIdIndicador(usuarioCreador.getId(),idIndicador));
				} else
					estilo = null;
			}
		}
		return estilo;
	}
	
	public EstiloVisualizacionDiagramaBarrasDto guarda(EstiloVisualizacionDiagramaBarrasDto estiloVisualizacionDiagramaBarrasDto, EncapsuladorErroresSW erroresSW){
		//validador iria aqui si hiciese falta		
		if(erroresSW.getHashErrors())
			return null;
		EstiloVisualizacionDiagramaBarras estiloActual = estiloVisualizacionDiagramaBarrasRepositorio.cargaPorIdUsuarioEIdIndicador(estiloVisualizacionDiagramaBarrasDto.getUsuario().getId(),estiloVisualizacionDiagramaBarrasDto.getIndicador().getId());
		EstiloVisualizacionDiagramaBarras estilo = new EstiloVisualizacionDiagramaBarras();
		if ( estiloActual!=null && estiloActual.getId()>0) {
			estiloActual.setTamanho(estiloVisualizacionDiagramaBarrasDto.getTamanho());
			estiloActual.setTamanhoFuente(estiloVisualizacionDiagramaBarrasDto.getTamanhoFuente());
			estiloActual.setColor(estiloVisualizacionDiagramaBarrasDto.getColor());
			estiloActual.setTipoFuente(estiloVisualizacionDiagramaBarrasDto.getTipoFuente());
			estilo = estiloVisualizacionDiagramaBarrasRepositorio.guarda(estiloActual);
		} else
			estilo = estiloVisualizacionDiagramaBarrasRepositorio.guarda(new EstiloVisualizacionDiagramaBarrasDto2EstiloVisualizacionDiagramaBarrasTransformer(mapper, estiloVisualizacionDiagramaBarrasRepositorio).transform(estiloVisualizacionDiagramaBarrasDto));
		return new EstiloVisualizacionDiagramaBarras2EstiloVisualizacionDiagramaBarrasDtoTransformer(mapper).transform(estilo);
	}
	
	public EstiloVisualizacionDiagramaBarrasDto borra(Long id, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionDiagramaBarrasDto estiloDto = new EstiloVisualizacionDiagramaBarras2EstiloVisualizacionDiagramaBarrasDtoTransformer(mapper).transform(estiloVisualizacionDiagramaBarrasRepositorio.carga(id));
		estiloVisualizacionDiagramaBarrasRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
	
	public EstiloVisualizacionDiagramaBarrasDto borraPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionDiagramaBarrasDto estiloDto = cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador);
		estiloVisualizacionDiagramaBarrasRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
}
