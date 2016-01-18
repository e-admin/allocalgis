/**
 * ParejaOrigenDestino.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
