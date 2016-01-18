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
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaSectoresDto;
import es.dc.a21l.visualizacion.cu.GestorCUEstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectoresRepositorio;

public class GestorCUEstiloVisualizacionDiagramaSectoresImpl implements GestorCUEstiloVisualizacionDiagramaSectores {
	
	private EstiloVisualizacionDiagramaSectoresRepositorio estiloVisualizacionDiagramaSectoresRepositorio;
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
	public void setEstiloVisualizacionDiagramaSectoresRepositorio(EstiloVisualizacionDiagramaSectoresRepositorio estiloVisualizacionDiagramaSectoresRepositorio) {
		this.estiloVisualizacionDiagramaSectoresRepositorio = estiloVisualizacionDiagramaSectoresRepositorio;
	}
	
	public EstiloVisualizacionDiagramaSectoresDto cargaPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador){
		//El estilo correspondiente es el del usuario logueado, pero sino tiene estilo definido
		//se muestra el estilo definido por el creador del indicador. Si tampoc tiene estilo definido,
		//se muestra el estilo por defecto.
		EstiloVisualizacionDiagramaSectoresDto estilo = new EstiloVisualizacionDiagramaSectores2EstiloVisualizacionDiagramaSectoresDtoTransformer(mapper).transform(estiloVisualizacionDiagramaSectoresRepositorio.cargaPorIdUsuarioEIdIndicador(idUsuario,idIndicador));
		if (estilo==null || estilo.getId()<=0) {
			IndicadorDto indicador = gestorCUIndicador.cargaPorId(idIndicador);
			UsuarioDto usuarioCreador = gestorCUUsuario.cargaUsuarioPorLogin(indicador.getLoginCreador());
			if ( usuarioCreador == null || usuarioCreador.getId()<=0 )
				estilo = null;
			else {
				if ( gestorCUIndicadorUsuario.cargaEsUsuarioCreadorIndicador(idIndicador, usuarioCreador.getId())) {
					estilo = new EstiloVisualizacionDiagramaSectores2EstiloVisualizacionDiagramaSectoresDtoTransformer(mapper).transform(estiloVisualizacionDiagramaSectoresRepositorio.cargaPorIdUsuarioEIdIndicador(usuarioCreador.getId(),idIndicador));
				} else
					estilo = null;
			}
		}
		return estilo;
	}
	
	public EstiloVisualizacionDiagramaSectoresDto guarda(EstiloVisualizacionDiagramaSectoresDto estiloVisualizacionDiagramaSectoresDto, EncapsuladorErroresSW erroresSW){
		//validador iria aqui si hiciese falta		
		if(erroresSW.getHashErrors())
			return null;
		EstiloVisualizacionDiagramaSectores estiloActual = estiloVisualizacionDiagramaSectoresRepositorio.cargaPorIdUsuarioEIdIndicador(estiloVisualizacionDiagramaSectoresDto.getUsuario().getId(),estiloVisualizacionDiagramaSectoresDto.getIndicador().getId());
		EstiloVisualizacionDiagramaSectores estilo = new EstiloVisualizacionDiagramaSectores();
		if ( estiloActual!=null && estiloActual.getId()>0) {
			estiloActual.setDiametro(estiloVisualizacionDiagramaSectoresDto.getDiametro());
			estiloActual.setTamanhoFuente(estiloVisualizacionDiagramaSectoresDto.getTamanhoFuente());
			estiloActual.setColores(estiloVisualizacionDiagramaSectoresDto.getColores());
			estiloActual.setTipoFuente(estiloVisualizacionDiagramaSectoresDto.getTipoFuente());
			estilo = estiloVisualizacionDiagramaSectoresRepositorio.guarda(estiloActual);
		} else
			estilo = estiloVisualizacionDiagramaSectoresRepositorio.guarda(new EstiloVisualizacionDiagramaSectoresDto2EstiloVisualizacionDiagramaSectoresTransformer(mapper, estiloVisualizacionDiagramaSectoresRepositorio).transform(estiloVisualizacionDiagramaSectoresDto));
		return new EstiloVisualizacionDiagramaSectores2EstiloVisualizacionDiagramaSectoresDtoTransformer(mapper).transform(estilo);
	}
	
	public EstiloVisualizacionDiagramaSectoresDto borra(Long id, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionDiagramaSectoresDto estiloDto = new EstiloVisualizacionDiagramaSectores2EstiloVisualizacionDiagramaSectoresDtoTransformer(mapper).transform(estiloVisualizacionDiagramaSectoresRepositorio.carga(id));
		estiloVisualizacionDiagramaSectoresRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
	
	public EstiloVisualizacionDiagramaSectoresDto borraPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador, EncapsuladorErroresSW erroresSW ){
		EstiloVisualizacionDiagramaSectoresDto estiloDto = cargaPorIdUsuarioEIdIndicador(idUsuario, idIndicador);
		estiloVisualizacionDiagramaSectoresRepositorio.borra(estiloDto.getId());	
		return estiloDto;
	}
}
