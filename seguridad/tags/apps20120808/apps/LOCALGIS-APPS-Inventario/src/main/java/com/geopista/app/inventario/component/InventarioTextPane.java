package com.geopista.app.inventario.component;

import java.awt.Font;

public class InventarioTextPane extends com.geopista.app.utilidades.TextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InventarioTextPane(int maxLength){
		 super (maxLength);
		 setFont(new Font("arial", Font.PLAIN, 10));
	}

}