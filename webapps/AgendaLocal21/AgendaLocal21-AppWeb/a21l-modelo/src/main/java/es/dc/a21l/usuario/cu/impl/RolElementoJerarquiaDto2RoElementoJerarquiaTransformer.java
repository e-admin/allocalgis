/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.cu.RolElementoJerarquiaDto;
import es.dc.a21l.usuario.modelo.RolElementoJerarquia;
import es.dc.a21l.usuario.modelo.RolElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.modelo.RolRepositorio;

public class RolElementoJerarquiaDto2RoElementoJerarquiaTransformer extends  TransformadorDtoBase2EntidadBase<RolElementoJerarquiaDto, RolElementoJerarquia> {

	private RolRepositorio rolRepositorio;
	private ElementoJerarquiaRepositorio elementoJerarquiaRepositorio;
	
	public RolElementoJerarquiaDto2RoElementoJerarquiaTransformer(Mapper mapper,RolElementoJerarquiaRepositorio rolElementoJerarquiaRepositorio,RolRepositorio repositorio, ElementoJerarquiaRepositorio elementoJerarquiaRepositorio) {
		super(mapper);
		this.rolRepositorio=repositorio;
		this.elementoJerarquiaRepositorio=elementoJerarquiaRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<RolElementoJerarquiaDto, RolElementoJerarquia>(rolElementoJerarquiaRepositorio, this));
	
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(RolElementoJerarquiaDto dto, RolElementoJerarquia vo) {
		super.aplicaPropiedadesEstendidas(dto, vo);
		vo.setElementoJerarquia(elementoJerarquiaRepositorio.carga(dto.getIdElementoJerarquia()));
		vo.setRol(rolRepositorio.carga(dto.getRolDto().getId()));
	}

}
