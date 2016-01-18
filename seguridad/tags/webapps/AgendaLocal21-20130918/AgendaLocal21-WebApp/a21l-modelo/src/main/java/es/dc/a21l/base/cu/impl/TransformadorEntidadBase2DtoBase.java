/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.base.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.DtoBase;
import es.dc.a21l.base.modelo.EntidadBase;

public class TransformadorEntidadBase2DtoBase<E extends EntidadBase, D extends DtoBase>  extends TransformadorBase<E, D> {
	
	public TransformadorEntidadBase2DtoBase(Mapper mapper) {
		super(mapper);
		setEstrategiaCreacion(new EstrategiaCreacionNuevo(this));
	}

	protected void aplicaPropiedadesEstendidas(E origen, D destino) {
	}
}
