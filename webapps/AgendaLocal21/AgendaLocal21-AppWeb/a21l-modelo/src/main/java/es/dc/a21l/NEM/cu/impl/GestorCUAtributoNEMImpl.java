/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.NEM.cu.impl;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.NEM.cu.AtributoNEMDto;
import es.dc.a21l.NEM.cu.GestorCUAtributoNEM;
import es.dc.a21l.NEM.modelo.AtributoNEM;
import es.dc.a21l.NEM.modelo.AtributoNEMRepositorio;
import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;

public class GestorCUAtributoNEMImpl implements GestorCUAtributoNEM {

	private Mapper mapper;
	
	private AtributoNEMRepositorio atributoNEMRepositorio;

	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	@Autowired
	public void setAtributoNEMRepositorio(
			AtributoNEMRepositorio atributoNEMRepositorio) {
		this.atributoNEMRepositorio = atributoNEMRepositorio;
	}
	
	
	public EncapsuladorListSW<AtributoNEMDto> cargaTodos(){
		List<AtributoNEM> lista= atributoNEMRepositorio.cargaTodos();
		AtributoNEM2AtributoNEMDtoTransformer transformer= new AtributoNEM2AtributoNEMDtoTransformer(mapper);
		EncapsuladorListSW<AtributoNEMDto> result= new EncapsuladorListSW<AtributoNEMDto>();
		
		for(AtributoNEM temp:lista)	
			result.add(transformer.transform(temp));
		
		return result;
	}
	
	
	public AtributoNEMDto cargaPorId(Long id){
		return new AtributoNEM2AtributoNEMDtoTransformer(mapper).transform(atributoNEMRepositorio.carga(id));
	}
	
	public EncapsuladorListSW<AtributoNEMDto> cargaPorListaIds(List<Long> ids){
		List<AtributoNEM> lista= atributoNEMRepositorio.cargaPorListaIds(ids);
		AtributoNEM2AtributoNEMDtoTransformer transformer= new AtributoNEM2AtributoNEMDtoTransformer(mapper);
		EncapsuladorListSW<AtributoNEMDto> result= new EncapsuladorListSW<AtributoNEMDto>();
		
		for(AtributoNEM temp:lista)
			result.add(transformer.transform(temp));
		return result;
	}
	
}
