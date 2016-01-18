/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.usuario.cu.UsuarioRolDto;
import es.dc.a21l.usuario.modelo.UsuarioRol;

public class UsuarioRol2UsuarioRolDtoTransformer extends TransformadorEntidadBase2DtoBase<UsuarioRol, UsuarioRolDto> {

	private Mapper mapper;
	public UsuarioRol2UsuarioRolDtoTransformer(Mapper mapper) {
		super(mapper);
		this.mapper=mapper;
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(UsuarioRol origen,UsuarioRolDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setIdUsuario(origen.getUsuario().getId());
		destino.setRolDto(new Rol2RolDtoTransformer(mapper).transform(origen.getRol()));
	}

}
