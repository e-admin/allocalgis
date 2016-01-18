/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.cu.impl;


import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;
import es.dc.a21l.fuente.modelo.TablaFuenteDatosRepositorio;


public class TablaFuenteDatosDto2TablaFuenteDatosTransformer extends  TransformadorDtoBase2EntidadBase<TablaFuenteDatosDto, TablaFuenteDatos>  {
    
	public TablaFuenteDatosDto2TablaFuenteDatosTransformer(Mapper mapper, TablaFuenteDatosRepositorio tablaFuenteDatosRepositorio) {
        super(mapper);
        setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<TablaFuenteDatosDto, TablaFuenteDatos>(tablaFuenteDatosRepositorio, this));
    }

}