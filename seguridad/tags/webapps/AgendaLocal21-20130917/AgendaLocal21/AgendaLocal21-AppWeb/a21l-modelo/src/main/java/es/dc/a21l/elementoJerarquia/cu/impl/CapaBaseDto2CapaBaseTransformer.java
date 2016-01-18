package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorDtoBase2EntidadBase;
import es.dc.a21l.elementoJerarquia.cu.CapaBaseDto;
import es.dc.a21l.elementoJerarquia.modelo.CapaBase;

public class CapaBaseDto2CapaBaseTransformer extends TransformadorDtoBase2EntidadBase<CapaBaseDto, CapaBase> {

	public CapaBaseDto2CapaBaseTransformer(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(CapaBaseDto origen,CapaBase destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setId(origen.getId());
		destino.setNombre(origen.getNombre());
		destino.setMapa(origen.getMapa());	
		destino.setLayer(origen.getLayer());
		destino.setOpenStreetMap(origen.isOpenStreetMap());
	}

}
