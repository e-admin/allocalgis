package es.dc.a21l.elementoJerarquia.cu;

import es.dc.a21l.base.cu.DtoBase;

public class CapaBaseDto extends DtoBase {
	
	private String nombre;
	
	private String mapa;
	
	private String layer;
	
	private boolean openStreetMap;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getMapa() {
		return mapa;
	}
	
	public void setMapa(String mapa) {
		this.mapa = mapa;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public boolean isOpenStreetMap() {
		return openStreetMap;
	}

	public void setOpenStreetMap(boolean openStreetMap) {
		this.openStreetMap = openStreetMap;
	}
	
	
}
