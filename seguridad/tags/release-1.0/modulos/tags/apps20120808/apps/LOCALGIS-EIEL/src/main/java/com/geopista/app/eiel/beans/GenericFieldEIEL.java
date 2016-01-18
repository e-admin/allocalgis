package com.geopista.app.eiel.beans;

import java.io.Serializable;

public class GenericFieldEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4711286707856974570L;
	String nombrecampo;
	int type; //From CampoFiltro
	
	public GenericFieldEIEL(){
									
	}
	

	public GenericFieldEIEL(String nombrecampo, int type) {
		super();
		this.nombrecampo = nombrecampo;
		this.type = type;
	}


	public String getNombrecampo() {
		return nombrecampo;
	}

	public void setNombrecampo(String nombrecampo) {
		this.nombrecampo = nombrecampo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	

}
