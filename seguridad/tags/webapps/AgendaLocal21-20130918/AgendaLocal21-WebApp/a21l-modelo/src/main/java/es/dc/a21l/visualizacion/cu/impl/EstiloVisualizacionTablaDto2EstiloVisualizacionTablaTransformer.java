/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.visualizacion.cu.EstiloVisualizacionTablaDto;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionTabla;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionTablaRepositorio;

public class EstiloVisualizacionTablaDto2EstiloVisualizacionTablaTransformer extends TransformadorDtoBase2EntidadBase<EstiloVisualizacionTablaDto, EstiloVisualizacionTabla> {

	private EstiloVisualizacionTablaRepositorio estiloVisualizacionTablaRepositorio;
	
	public EstiloVisualizacionTablaDto2EstiloVisualizacionTablaTransformer(Mapper mapper, EstiloVisualizacionTablaRepositorio estiloVisualizacionTablaRepositorio) {
		super(mapper);
		this.estiloVisualizacionTablaRepositorio = estiloVisualizacionTablaRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<EstiloVisualizacionTablaDto, EstiloVisualizacionTabla>(estiloVisualizacionTablaRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(EstiloVisualizacionTablaDto origen, EstiloVisualizacionTabla destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
	}
	

}
