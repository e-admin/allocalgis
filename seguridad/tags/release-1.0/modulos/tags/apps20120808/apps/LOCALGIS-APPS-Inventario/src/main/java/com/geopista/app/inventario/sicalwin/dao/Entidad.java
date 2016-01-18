package com.geopista.app.inventario.sicalwin.dao;

public class Entidad {

	private String codigo;
	private String nombre;
	
	
	public Entidad() {
		super();
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Override
	public String toString() {
		return "Entidad [codigo=" + codigo + ", nombre=" + nombre + "]";
	}
	
	
	public void load(String[] valores) {
		this.codigo=valores[0];
		this.nombre=valores[1];
		
		
	}
	
	
	
}
