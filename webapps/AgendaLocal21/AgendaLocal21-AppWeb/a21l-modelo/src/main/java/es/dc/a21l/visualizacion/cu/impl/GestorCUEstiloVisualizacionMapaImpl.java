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
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionMapaDto;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionMapa;
import es.dc.a21l.visualizacion.cu.GestorCURangosVisualizacionMapa;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionMapa;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionMapaRepositorio;

public class GestorCUEstiloVisualizacionMapaImpl implements GestorCUEstiloVisualizacionMapa {
	
	private EstiloVisualizacionMapaRepositorio estiloVisualizacionMapaRepositorio;
	private GestorCURangosVisualizacionMapa gestorCURangosVisualizacionMapa;
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
	public void setGestorCURangosVisualizacionMapa(GestorCURangosVisualizacionMapa gestorCURangosVisualizacionMapa) {
		this.gestorCURangosVisualizacionMapa = gestorCURangosVisualizacionMapa;
	}
	
	@Autowired
	public void setEstiloVisualizacionMapaRepositorio(EstiloVisualizacionMapaRepositorio estiloVisualizacionMapaRepositorio) {
		this.estiloVisualizacionMapaRepositorio = estiloVisualizacionMapaRepositorio;
	}
	
	public EstiloVisualizacionMapaDto cargaPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador){
		//El estilo correspondiente es el del usuario logueado, pero sino tiene estilo definido
		//se muestra el estilo definido por el creador del indicador. Si tampoc tiene estilo definido,
		//se muestra el estilo por defecto.
		EstiloVisualizacionMapaDto estilo = new EstiloVisualizacionMapa2EstiloVisualizacionMapaDtoTransformer(mapper).transform(estiloVisualizacionMapaRepositorio.cargaPorIdUsuarioEIdIndicador(idUsuario,idIndicador));
		if (estilo==null || estilo.getId()<=0) {
			IndicadorDto indicador = gestorCUIndicador.cargaPorId(idIndicador);
			UsuarioDto usuarioCreador = gestorCUUsuario.cargaUsuarioPorLogin(indicador.getLoginCreador());
			if ( usuarioCreador == null || usuarioCreador.getId()<=0 )
				estilo = null;
			else {
				if ( gestorCUIndicadorUsuario.cargaEsUsuarioCreadorIndicador(idIndicador, usuarioCreador.getId())) {
					estilo = new EstiloVisualizacionMapa2EstiloVisualizacionMapaDtoTransformer(mapper).transform(estiloVisualizacionMapaRepositorio.cargaPorIdUsuarioEIdIndicador(usuarioCreador.getId(),idIndicador));
				} else
					estilo = null;
			}
		}
		return estilo;
	}
	
	public EstiloVisualizacionMapaDto guarda(EstiloVisualizacionMapaDto estiloVisualizacionMapaDto, EncapsuladorErroresSW erroresSW){
		//validador iria aqui si hiciese falta		
		if(erroresSW.getHashErrors())
			return null;
		EstiloVisualizacionMapa estiloActual = estiloVisualizacionMapaRepositorio.cargaPorIdUsuarioEIdIndicador(estiloVisualizacionMapaDto.getUsuario().getId(),estiloVisualizacionMapaDto.getIndicador().getId());
		EstiloVisualizacionMapa estilo = new EstiloVisualizacionMapa();
		if ( estiloActual!=null && estiloActual.getId()>0) {
			gestorCURangosVisualizacionMapa.borraPorIdEstiloMapa(estiloActual.getId());
			estiloActual.setRangos(estilo.getRangos());
			estilo = estiloVisualizacionMapaRepositorio.guarda(estiloActual);
		} else
			estilo = estiloVisualizacionMapaRepositorio.guarda(new EstiloVisualizacionMapaDto2EstiloVisualizacionMapaTransformer(mapper, estiloVisualizacionMapaRepositorio).transform(estiloVisualizacionMapaDto));
		return new EstiloVisualizacionMapa2EstiloVisualizacionMapaDtoTransformer(mapper).transform(estilo);
	}
	
	public EstiloVisualizacionMapaDto borra(Long id ){
		EstiloVisualizacionMapaDto estiloDto = new EstiloVisualizacionMapa2EstiloVisualizacionMapaDtoTransformer(mapper).transform(estiloVisualizacionMapaRepositorio.carga(id));
		estiloVisualizacionMapaRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
	
	public EstiloVisualizacionMapaDto borraPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador ){
		EstiloVisualizacionMapaDto estiloDto = cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador);
		estiloVisualizacionMapaRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
}
