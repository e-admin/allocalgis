package com.geopista.server.database.validacion.utils;

import java.text.DecimalFormat;

public class UtilsValidacion {

	
	public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
       
        return Double.valueOf(twoDForm.format(d).toString().replace(",", ".")).doubleValue();
	}
	
	public static double roundOneDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
       
        return Double.valueOf(twoDForm.format(d).toString().replace(",", ".")).doubleValue();
	}
}
