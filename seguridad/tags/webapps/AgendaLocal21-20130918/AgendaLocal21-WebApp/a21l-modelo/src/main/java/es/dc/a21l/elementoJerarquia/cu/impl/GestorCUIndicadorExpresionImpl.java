/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.validadores.Validador;
import es.dc.a21l.elementoJerarquia.cu.GestorCUIndicadorExpresion;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresion;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresionRepositorio;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorRepositorio;
import es.dc.a21l.expresion.cu.ExpresionDto;
import es.dc.a21l.expresion.cu.GestorCUExpresion;
import es.dc.a21l.expresion.modelo.ExpresionRepositorio;

public class GestorCUIndicadorExpresionImpl implements GestorCUIndicadorExpresion {

	
	private Mapper mapper;
	private IndicadorExpresionRepositorio indicadorExpresionRepositorio;
	private GestorCUExpresion gestorCUExpresion;
	private ExpresionRepositorio expresionRepositorio;
	private IndicadorRepositorio indicadorRepositorio;
	
	
	@Autowired
	public void setExpresionRepositorio(ExpresionRepositorio expresionRepositorio) {
		this.expresionRepositorio = expresionRepositorio;
	}

	@Autowired
	public void setIndicadorRepositorio(IndicadorRepositorio indicadorRepositorio) {
		this.indicadorRepositorio = indicadorRepositorio;
	}

	@Autowired
	public void setGestorCUExpresion(GestorCUExpresion gestorCUExpresion) {
		this.gestorCUExpresion = gestorCUExpresion;
	}

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setIndicadorExpresionRepositorio(IndicadorExpresionRepositorio indicadorExpresionRepositorio) {
		this.indicadorExpresionRepositorio = indicadorExpresionRepositorio;
	}



	public IndicadorExpresionDto cargaPorId(Long id){
		return new IndicadorExpresion2IndicadorExpresionDtoTransformer(mapper).transform(indicadorExpresionRepositorio.carga(id));
	}
	
	public IndicadorExpresionDto guardar(IndicadorExpresionDto indicadorExpresionDto,EncapsuladorErroresSW errores, String caracterSeparadorDecimales){
		
		Validador<IndicadorExpresionDto> validador= new IndicadorExpresionDtoValidador();
		validador.valida(indicadorExpresionDto, errores);
		if(errores.getHashErrors())
			return null;
		
		//Expresion Nueva
		if(indicadorExpresionDto.getId()==0){
			//ExpresionDto expresionGuardar = gestorCUExpresion.guardaExpresion(indicadorExpresionDto.getExpresionTransformada().replaceAll(" ", ""),indicadorExpresionDto.getIdIndicador());
			ExpresionDto expresionGuardar = gestorCUExpresion.guardaExpresion(indicadorExpresionDto.getExpresionTransformada().trim(),indicadorExpresionDto.getIdIndicador(), caracterSeparadorDecimales);
			if ( expresionGuardar == null || expresionGuardar.getId()<=0)
				return null;
			indicadorExpresionDto.setIdExpresion(expresionGuardar.getId());
			
		}else{
			//Expresion ya existente
			IndicadorExpresionDto indicadorExpresionDto2= cargaPorId(indicadorExpresionDto.getId());
			if(!indicadorExpresionDto2.getExpresionTransformada().equals(indicadorExpresionDto.getExpresionTransformada())){
				//ExpresionDto expresionDto=gestorCUExpresion.guardaExpresion(indicadorExpresionDto.getExpresionTransformada().replaceAll(" ", ""),indicadorExpresionDto2.getIdIndicador());
				ExpresionDto expresionDto=gestorCUExpresion.guardaExpresion(indicadorExpresionDto.getExpresionTransformada().trim(),indicadorExpresionDto2.getIdIndicador(), caracterSeparadorDecimales);
				gestorCUExpresion.borra(indicadorExpresionDto2.getIdExpresion());
				indicadorExpresionDto.setIdExpresion(expresionDto.getId());
			}
			
		}
		
		if ( indicadorExpresionDto == null || indicadorExpresionDto.getIdExpresion()==null || indicadorExpresionDto.getIdExpresion()<=0 )
			return null;
		
		IndicadorExpresion indicadorExpresion= new IndicadorExpresionDto2IndicadorExpresionTransformer(mapper, indicadorExpresionRepositorio, expresionRepositorio, indicadorRepositorio).transform(indicadorExpresionDto);
		indicadorExpresionRepositorio.guarda(indicadorExpresion);
		return new IndicadorExpresion2IndicadorExpresionDtoTransformer(mapper).transform(indicadorExpresion);
	
	}
	
	public IndicadorExpresionDto borra(Long id) {
        IndicadorExpresionDto indicadorExpresionDto = cargaPorId(id);
        indicadorExpresionRepositorio.borra(id);
        gestorCUExpresion.borra(indicadorExpresionDto.getIdExpresion());        
        return indicadorExpresionDto;
    }
	
	public void borraPorIdIndicador(Long id) {
        List<IndicadorExpresion> lista = new ArrayList<IndicadorExpresion>();
        lista = indicadorExpresionRepositorio.cargaPorIdIndicador(id);
        for ( IndicadorExpresion indi : lista ) {
        	indicadorExpresionRepositorio.borraPorIdIndicador(id);
        	gestorCUExpresion.borra(indi.getExpresion().getId());
        }
    }
}
