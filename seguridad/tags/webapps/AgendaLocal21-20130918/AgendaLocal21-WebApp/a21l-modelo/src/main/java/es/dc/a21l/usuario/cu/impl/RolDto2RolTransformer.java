/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.usuario.cu.RolDto;
import es.dc.a21l.usuario.modelo.Rol;
import es.dc.a21l.usuario.modelo.RolRepositorio;

public class RolDto2RolTransformer extends  TransformadorDtoBase2EntidadBase<RolDto, Rol> {

	public RolDto2RolTransformer(Mapper mapper,RolRepositorio rolRepositorio) {
		super(mapper);
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<RolDto, Rol>(rolRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(RolDto dto, Rol vo) {
		super.aplicaPropiedadesEstendidas(dto, vo);
	}

}
