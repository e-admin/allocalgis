package com.gestorfip.ws.xml.beans.importacion.layers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiccionarioLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827526807311248622L;

	private CaracterDeterminacionLayerBean[] caracteresDeterminacion;

	public CaracterDeterminacionLayerBean[] getCaracteresDeterminacion() {
		return caracteresDeterminacion;
	}

	public void setCaracteresDeterminacion(
			CaracterDeterminacionLayerBean[] caracteresDeterminacion) {
		this.caracteresDeterminacion = caracteresDeterminacion;
	}

	
	
	

}
