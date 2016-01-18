package com.geopista.app.inventario.component;

import java.awt.Font;

public class InventarioTextField extends com.geopista.app.utilidades.TextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InventarioTextField(){
		  super();
		  setFont(new Font("arial", Font.PLAIN, 10));
	}

	public InventarioTextField(int maxLength){
		  super(maxLength);
		  setFont(new Font("arial", Font.PLAIN, 10));
	}

}
