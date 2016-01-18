/**
 * Node.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.coordsys.ed50toetrs89.info;

public class Node {

	private float ilat;
	private float ilon;
	private float plat;
	private float plon;

	/**
	 * Contructor.
	 */
	public Node() {
		// TODO Auto-generated constructor stub
		super();
	}

	/**
	 * Creates a Node from the specified latitude increase to pass from ED50 to
	 * ETRS89, longitude increase to pass from ED50 to ETRS89, latitude increase
	 * precision and longitude increase precision.
	 * 
	 * @param ilatitude
	 *            latitude increase.
	 * @param ifloatitude
	 *            longitude increase.
	 * @param platitude
	 *            latitude increase precision.
	 * @param plongitude
	 *            longitude increase precision.
	 */
	public Node(float ilatitude, float ifloatitude, float platitude,
			float plongitude) {
		super();
		this.ilat = ilatitude;
		this.ilon = ifloatitude;
		this.plat = platitude;
		this.plon = plongitude;
	}

	/**
	 * Gets the latitude increase to convert a coordinate from ED50 to ERTS89.
	 * 
	 * @return latitude increase.
	 */
	public float getIlat() {
		return ilat;
	}

	/**
	 * Sets the latitude increase to convert a coordinate from ED50 to ERTS89.
	 * 
	 * @param ilat
	 *            latitude increase.
	 */
	public void setIlat(float ilat) {
		this.ilat = ilat;
	}

	/**
	 * Gets the longitude increase to convert a coordinate from ED50 to ERTS89.
	 * 
	 * @return longitude increase.
	 */
	public float getIlon() {
		return ilon;
	}

	/**
	 * Sets the longitude increase to convert a coordinate from ED50 to ERTS89.
	 * 
	 * @return longitude increase.
	 */
	public void setIlon(float ilon) {
		this.ilon = ilon;
	}

	/**
	 * Gets the precision of the latitude increase.
	 * 
	 * @return latitude precision increase.
	 */
	public float getPlat() {
		return plat;
	}

	/**
	 * Sets the precision of the latitude increase.
	 * 
	 * @param plat
	 *            latitude precision increase.
	 */
	public void setPlat(float plat) {
		this.plat = plat;
	}

	/**
	 * Gets the precision of the longitude increase.
	 * 
	 * @return longitude precision increase.
	 */
	public float getPlon() {
		return plon;
	}

	/**
	 * Sets the precision of the longitude increase.
	 * 
	 * @return longitude precision increase.
	 */
	public void setPlon(float plon) {
		this.plon = plon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() pass to string all the node information.
	 */
	public String toString() {
		String a = "";

		a = a + "iLat" + this.ilat + "\n";
		a = a + "iLon" + this.ilon + "\n";
		a = a + "pLat" + this.plat + "\n";
		a = a + "pLon" + this.plon + "\n";

		return a;
	}

}
