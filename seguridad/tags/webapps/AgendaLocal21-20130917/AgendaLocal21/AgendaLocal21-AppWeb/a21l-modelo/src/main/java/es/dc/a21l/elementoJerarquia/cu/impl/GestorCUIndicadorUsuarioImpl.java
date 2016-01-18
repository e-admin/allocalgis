/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorUsuario;
import es.dc.a21l.elementoJerarquia.cu.IndicadorUsuarioDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuario;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorUsuarioRepositorio;
import es.dc.a21l.usuario.modelo.UsuarioRepositorio;

public class GestorCUIndicadorUsuarioImpl implements GestorCUIndicadorUsuario {
	
	private Mapper mapper;
	private IndicadorUsuarioRepositorio indicadorUsuarioRepositorio;
	private IndicadorRepositorio indicadorRepositorio;
	private UsuarioRepositorio usuarioRepositorio;
	
	
	@Autowired
	public void setIndicadorRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}


	@Autowired
	public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}


	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	
	@Autowired
	public void setIndicadorUsuarioRepositorio(
			IndicadorUsuarioRepositorio indicadorUsuarioRepositorio) {
		this.indicadorUsuarioRepositorio = indicadorUsuarioRepositorio;
	}





	public IndicadorUsuarioDto cargaPorId(Long id){
		return new IndicadorUsuario2IndicadorUsuarioDtoTransformer(mapper).transform(indicadorUsuarioRepositorio.carga(id));
	}
	
	public IndicadorUsuarioDto guarda(IndicadorUsuarioDto indicadorUsuarioDto, EncapsuladorErroresSW errores){
		Validador<IndicadorUsuarioDto> validador= new IndicadorUsuarioDtoValidador();
		validador.valida(indicadorUsuarioDto, errores);
		if(errores.getHashErrors())
			return null;
		
		IndicadorUsuario indicadorUsuario= new IndicadorUsuarioDto2IndicadorUsuarioTransformer(mapper, indicadorUsuarioRepositorio, indicadorRepositorio, usuarioRepositorio).transform(indicadorUsuarioDto);
		indicadorUsuarioRepositorio.guarda(indicadorUsuario);
		return new IndicadorUsuario2IndicadorUsuarioDtoTransformer(mapper).transform(indicadorUsuario);
	}
	
	public void borra(Long id){
		indicadorUsuarioRepositorio.borra(id);
	}
	
	public Boolean cargaEsUsuarioCreadorIndicador(Long idIndicador,Long idUsuario){
		return indicadorUsuarioRepositorio.cargaEsUsuarioCreadorIndicador(idIndicador, idUsuario);
	}

}
