package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.elementoJerarquia.cu.CapaBaseDto;
import es.dc.a21l.elementoJerarquia.cu.GestorCUCapaBase;
import es.dc.a21l.elementoJerarquia.modelo.CapaBase;
import es.dc.a21l.elementoJerarquia.modelo.CapaBaseRepositorio;

public class GestorCUCapaBaseImpl implements GestorCUCapaBase  {
	CapaBaseRepositorio capaBaseRepositorio;
	private Mapper mapper;
	
	@Autowired
	public void setCapaBaseRepositorio(CapaBaseRepositorio capaBaseRepositorio) {
		this.capaBaseRepositorio = capaBaseRepositorio;
	}
	
	@Autowired
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public EncapsuladorListSW<CapaBaseDto> cargaTodas(){
		EncapsuladorListSW<CapaBaseDto> result= new EncapsuladorListSW<CapaBaseDto>();
		CapaBase2CapaBaseDtoTransformer transformer= new CapaBase2CapaBaseDtoTransformer(mapper);
		for(CapaBase temp:capaBaseRepositorio.cargaTodos())
			result.add(transformer.transform(temp));
		return result;
	}
}
