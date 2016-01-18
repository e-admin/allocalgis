package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Collection;

public class ElemFeatureBean extends ElemCementerioBean implements Serializable {

	private static final long	serialVersionUID	= 3546643200656945977L;
	
	private int idElem;
	
	/**Métodos getter y setter**/
	
	public int getIdElem() {
		return idElem;
	}
	public void setIdElem(int idElem) {
		this.idElem = idElem;
	}
	
	
}
