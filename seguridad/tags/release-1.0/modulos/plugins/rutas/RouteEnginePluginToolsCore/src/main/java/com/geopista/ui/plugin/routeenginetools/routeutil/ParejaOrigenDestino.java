package com.geopista.ui.plugin.routeenginetools.routeutil;

/*
 * Esta clase crea una key dados 2 numeros entreros
 * params origen y destino
 * el hash devuelve una clave que será usada en un Hashtable
 */

public class ParejaOrigenDestino {
	int entero1, entero2;

	public ParejaOrigenDestino(int origen, int destino) {
		this.entero1 = origen;
		this.entero2 = destino;
	}

	public int hash() {
		return 100000 * entero1 + entero2;

	}

}
