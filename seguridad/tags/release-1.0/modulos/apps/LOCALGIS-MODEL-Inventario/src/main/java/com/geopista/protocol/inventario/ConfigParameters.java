package com.geopista.protocol.inventario;

public class ConfigParameters {
	
	
	public static int DEFAULT_LIMIT=20;
	public static int MAX_LIMIT=20;
	private int limit=-1;
	private int offset=0;
	private boolean busquedaIndividual=false;
	
	public void setLimit(int limit) {
		this.limit=limit;		
	}
	public int getLimit(){
		return limit;
	}
	
	public void setOffset(int offset) {
		this.offset=offset;		
	}
	
	public int getOffset(){
		return offset;
	}
	public void setBusquedaIndividual(boolean busquedaIndividual) {
		this.busquedaIndividual = busquedaIndividual;
	}
	public boolean isBusquedaIndividual() {
		return busquedaIndividual;
	}

}
