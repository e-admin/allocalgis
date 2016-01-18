/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.IndicadorUsuarioDto2IndicadorUsuarioModificacionTransformer;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuarioModificacion;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioModificacion;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioModificacionRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class GestorCUIndicadorUsuarioModificacionImpl implements GestorCUIndicadorUsuarioModificacion{
	
	private Mapper mapper;
	private IndicadorUsuarioModificacionRepositorio indicadorUsuarioRepositorio;
	private IndicadorRepositorio indicadorRepositorio;
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	
	@Autowired
	public void setIndicadorUsuarioRepositorio(
			IndicadorUsuarioModificacionRepositorio indicadorUsuarioRepositorio) {
		this.indicadorUsuarioRepositorio = indicadorUsuarioRepositorio;
	}


	@Autowired
	public void setIndicadorRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}
	
	@Autowired
	public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}
	
	
	public boolean guarda(IndicadorUsuarioDto indicadorUsuarioDto, EncapsuladorErroresSW errores){
		Validador<IndicadorUsuarioDto> validador= new IndicadorUsuarioDtoValidador();
		validador.valida(indicadorUsuarioDto, errores);
		if(errores.getHashErrors())
			return false;
		
		IndicadorUsuarioModificacion indicadorUsuario= new IndicadorUsuarioDto2IndicadorUsuarioModificacionTransformer(mapper, indicadorUsuarioRepositorio, indicadorRepositorio, usuarioRepositorio).transform(indicadorUsuarioDto);
		indicadorUsuarioRepositorio.guarda(indicadorUsuario);
		return true;
	}
	
	public void borraPorIdIndicador(Long idIndicador){
		indicadorUsuarioRepositorio.borraPorIdIndicador(idIndicador);		
	}

}
