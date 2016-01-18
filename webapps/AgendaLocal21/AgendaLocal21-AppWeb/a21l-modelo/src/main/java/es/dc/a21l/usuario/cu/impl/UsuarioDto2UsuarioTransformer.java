/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.usuario.cu.impl;


import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.usuario.cu.UsuarioDto;
import es.dc.a21l.usuario.modelo.Usuario;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;


public class UsuarioDto2UsuarioTransformer extends  TransformadorDtoBase2EntidadBase<UsuarioDto, Usuario>  {
    
	public UsuarioDto2UsuarioTransformer(Mapper mapper, UsuarioRepositorio usuarioRepositorio) {
        super(mapper);
        setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<UsuarioDto, Usuario>(usuarioRepositorio, this));
    }
	
	@Override
	protected void aplicaPropiedadesEstendidas(UsuarioDto dto, Usuario vo) {
		super.aplicaPropiedadesEstendidas(dto, vo);
	}

}