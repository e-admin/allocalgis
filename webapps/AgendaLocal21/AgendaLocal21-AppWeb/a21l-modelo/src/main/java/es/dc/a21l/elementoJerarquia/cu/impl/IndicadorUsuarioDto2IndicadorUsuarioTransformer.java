/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuario;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class IndicadorUsuarioDto2IndicadorUsuarioTransformer extends TransformadorDtoBase2EntidadBase<IndicadorUsuarioDto, IndicadorUsuario> {

	private IndicadorRepositorio indicadorRepositorio;
	private UsuarioRepositorio usuarioRepositorio;
	
	public IndicadorUsuarioDto2IndicadorUsuarioTransformer(Mapper mapper,IndicadorUsuarioRepositorio indicadorUsuarioRepositorio,IndicadorRepositorio indicadorRepositorio,UsuarioRepositorio usuarioRepositorio) {
		super(mapper);
		this.indicadorRepositorio=indicadorRepositorio;
		this.usuarioRepositorio=usuarioRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<IndicadorUsuarioDto, IndicadorUsuario>(indicadorUsuarioRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(IndicadorUsuarioDto origen,IndicadorUsuario destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setIndicador(indicadorRepositorio.carga(origen.getIdIndicador()));
		destino.setUsuario(usuarioRepositorio.carga(origen.getIdUsuario()));
	}

}
