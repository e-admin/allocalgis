package es.dc.a21l.elementoJerarquia.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import es.dc.a21l.base.modelo.EntidadBase;

@Entity
@Table(name="Tb_A21l_capa_base")
public class CapaBase extends EntidadBase implements Serializable {
	
	@Column(name="nombre",nullable=true)
	private String nombre;
	
	@Column(name="mapa",nullable=true)
	private String mapa;
	
	@Column(name="layer",nullable=true)
	private String layer;
	
	@Column(name="openStreetMap",nullable=true)
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
