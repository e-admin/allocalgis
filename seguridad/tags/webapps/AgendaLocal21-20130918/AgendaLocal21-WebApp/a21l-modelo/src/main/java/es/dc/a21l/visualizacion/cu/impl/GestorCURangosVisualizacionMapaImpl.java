/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.visualizacion.cu.GestorCURangosVisualizacionMapa;
import es.dc.a21l.visualizacion.cu.RangosVisualizacionMapaDto;
import es.dc.a21l.visualizacion.modelo.RangosVisualizacionMapa;
import es.dc.a21l.visualizacion.modelo.RangosVisualizacionMapaRepositorio;

public class GestorCURangosVisualizacionMapaImpl implements GestorCURangosVisualizacionMapa {
	
	private RangosVisualizacionMapaRepositorio rangosVisualizacionMapaRepositorio;
	private Mapper mapper;
	
	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setRangosVisualizacionMapaRepositorio(RangosVisualizacionMapaRepositorio rangosVisualizacionMapaRepositorio) {
		this.rangosVisualizacionMapaRepositorio = rangosVisualizacionMapaRepositorio;
	}
	
	public EncapsuladorListSW<RangosVisualizacionMapaDto> cargaPorIdEstiloMapa(Long idEstiloMapa){
		List<RangosVisualizacionMapa> listaRangos = rangosVisualizacionMapaRepositorio.cargaPorIdEstiloMapa(idEstiloMapa);
		EncapsuladorListSW<RangosVisualizacionMapaDto> listaRangosDto = new EncapsuladorListSW();
		for ( RangosVisualizacionMapa rango : listaRangos ) {
			listaRangosDto.add(new RangosVisualizacionMapa2RangosVisualizacionMapaDtoTransformer(mapper).transform(rango));
		}
		return listaRangosDto;
	}
	
	public RangosVisualizacionMapaDto guarda(RangosVisualizacionMapaDto rangosVisualizacionMapaDto, EncapsuladorErroresSW erroresSW){
		//validador iria aqui si hiciese falta		
		if(erroresSW.getHashErrors())
			return null;
		RangosVisualizacionMapa rangosActual = rangosVisualizacionMapaRepositorio.carga(rangosVisualizacionMapaDto.getId());
		RangosVisualizacionMapaDto2RangosVisualizacionMapaTransformer transformer = new RangosVisualizacionMapaDto2RangosVisualizacionMapaTransformer(mapper, rangosVisualizacionMapaRepositorio);
		RangosVisualizacionMapa rango = rangosVisualizacionMapaRepositorio.guarda(transformer.transform(rangosVisualizacionMapaDto));
		return new RangosVisualizacionMapa2RangosVisualizacionMapaDtoTransformer(mapper).transform(rango);
	}
	
	public RangosVisualizacionMapaDto borra(Long id ){
		RangosVisualizacionMapaDto rangosDto = new RangosVisualizacionMapa2RangosVisualizacionMapaDtoTransformer(mapper).transform(rangosVisualizacionMapaRepositorio.carga(id));
		rangosVisualizacionMapaRepositorio.borra(rangosDto.getId());	
		return rangosDto;
	}
	
	public List<RangosVisualizacionMapaDto> borraPorIdEstiloMapa(Long idEstiloMapa ){
		List<RangosVisualizacionMapaDto> listaRangosDto = cargaPorIdEstiloMapa(idEstiloMapa);
		for ( RangosVisualizacionMapaDto rango : listaRangosDto ) {
			rangosVisualizacionMapaRepositorio.borra(rango.getId());
		}
		return listaRangosDto;
	}
}
