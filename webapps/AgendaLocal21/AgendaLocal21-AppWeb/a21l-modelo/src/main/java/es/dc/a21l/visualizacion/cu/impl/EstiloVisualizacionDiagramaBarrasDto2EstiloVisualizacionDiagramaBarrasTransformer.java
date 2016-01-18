/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionDiagramaBarrasDto;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarrasRepositorio;

public class EstiloVisualizacionDiagramaBarrasDto2EstiloVisualizacionDiagramaBarrasTransformer extends TransformadorDtoBase2EntidadBase<EstiloVisualizacionDiagramaBarrasDto, EstiloVisualizacionDiagramaBarras> {

	private EstiloVisualizacionDiagramaBarrasRepositorio estiloVisualizacionDiagramaBarrasRepositorio;
	
	public EstiloVisualizacionDiagramaBarrasDto2EstiloVisualizacionDiagramaBarrasTransformer(Mapper mapper, EstiloVisualizacionDiagramaBarrasRepositorio estiloVisualizacionDiagramaBarrasRepositorio) {
		super(mapper);
		this.estiloVisualizacionDiagramaBarrasRepositorio = estiloVisualizacionDiagramaBarrasRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<EstiloVisualizacionDiagramaBarrasDto, EstiloVisualizacionDiagramaBarras>(estiloVisualizacionDiagramaBarrasRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(EstiloVisualizacionDiagramaBarrasDto origen, EstiloVisualizacionDiagramaBarras destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
	}
	

}
