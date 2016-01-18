/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.modelo.ElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.cu.UsuarioElementoJerarquiaDto;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquia;
import es.dc.a21l.usuario.modelo.UsuarioElementoJerarquiaRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class UsuarioElementoJerarquiaDto2UsuarioElementoJerarquiaTransformer extends TransformadorDtoBase2EntidadBase<UsuarioElementoJerarquiaDto, UsuarioElementoJerarquia> {

	private UsuarioRepositorio usuarioRepositorio;
	private ElementoJerarquiaRepositorio eltoJerarquiaRepositorio;
	
	public UsuarioElementoJerarquiaDto2UsuarioElementoJerarquiaTransformer(Mapper mapper, UsuarioElementoJerarquiaRepositorio usuarioElementoJerarquiaRepositorio,UsuarioRepositorio usuarioRepositorio, ElementoJerarquiaRepositorio elementoJerarquiaRepositorio) {
		super(mapper);
		this.usuarioRepositorio = usuarioRepositorio;
		this.eltoJerarquiaRepositorio = elementoJerarquiaRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<UsuarioElementoJerarquiaDto, UsuarioElementoJerarquia>(usuarioElementoJerarquiaRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(UsuarioElementoJerarquiaDto origen,UsuarioElementoJerarquia destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setElementoJerarquia(eltoJerarquiaRepositorio.carga(origen.getElementoJerarquiaDto().getId()));
		destino.setUsuario(usuarioRepositorio.carga(origen.getIdUsuario()));
	}

}
