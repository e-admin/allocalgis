/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.EstrategiaCreacionEntidadRepositorio;
import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioModificacion;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioModificacionRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class IndicadorUsuarioDto2IndicadorUsuarioModificacionTransformer extends TransformadorDtoBase2EntidadBase<IndicadorUsuarioDto, IndicadorUsuarioModificacion> {

	private IndicadorRepositorio indicadorRepositorio;
	private UsuarioRepositorio usuarioRepositorio;
	
	public IndicadorUsuarioDto2IndicadorUsuarioModificacionTransformer(Mapper mapper,IndicadorUsuarioModificacionRepositorio indicadorUsuarioRepositorio,IndicadorRepositorio indicadorRepositorio,UsuarioRepositorio usuarioRepositorio) {
		super(mapper);
		this.indicadorRepositorio=indicadorRepositorio;
		this.usuarioRepositorio=usuarioRepositorio;
		setEstrategiaCreacion(new EstrategiaCreacionEntidadRepositorio<IndicadorUsuarioDto, IndicadorUsuarioModificacion>(indicadorUsuarioRepositorio, this));
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(IndicadorUsuarioDto origen,IndicadorUsuarioModificacion destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setIdIndicador(origen.getIdIndicador());
		destino.setNombreIndicador(indicadorRepositorio.carga(origen.getIdIndicador()).getNombre());
		destino.setIdUsuario(origen.getIdUsuario());
		destino.setLoginUsuario(usuarioRepositorio.carga(origen.getIdUsuario()).getLogin());
		destino.setAccion(origen.getAccion());
	}
}
