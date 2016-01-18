package es.dc.a21l.elementoJerarquia.cu.impl;

import org.dozer.Mapper;

import es.dc.a21l.base.cu.impl.TransformadorEntidadBase2DtoBase;
import es.dc.a21l.elementoJerarquia.cu.CapaBaseDto;
import es.dc.a21l.elementoJerarquia.cu.IndicadorExpresionDto;
import es.dc.a21l.elementoJerarquia.modelo.CapaBase;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresion;

public class CapaBase2CapaBaseDtoTransformer extends TransformadorEntidadBase2DtoBase<CapaBase, CapaBaseDto> {

	public CapaBase2CapaBaseDtoTransformer(Mapper mapper) {
		super(mapper);
	}
	
	@Override
	protected void aplicaPropiedadesEstendidas(CapaBase origen,CapaBaseDto destino) {
		super.aplicaPropiedadesEstendidas(origen, destino);
		destino.setId(origen.getId());
		destino.setNombre(origen.getNombre());
		destino.setMapa(origen.getMapa());	
		destino.setLayer(origen.getLayer());
		destino.setOpenStreetMap(origen.isOpenStreetMap());
	}

}
