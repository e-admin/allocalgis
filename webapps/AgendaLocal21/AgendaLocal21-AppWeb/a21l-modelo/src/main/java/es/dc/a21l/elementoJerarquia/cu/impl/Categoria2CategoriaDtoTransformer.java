/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.elementoJerarquia.cu.CategoriaDto;
import es.dc.a21l.elementoJerarquia.modelo.Categoria;

public class Categoria2CategoriaDtoTransformer extends TransformadorEntidadBase2DtoBase<Categoria, CategoriaDto> {

	public Categoria2CategoriaDtoTransformer(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(Categoria origen,CategoriaDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setIdCategoriaPadre((origen.getCategoriaPadre()!=null && origen.getCategoriaPadre().getId()!=0)?origen.getCategoriaPadre().getId():null);
	}

}
