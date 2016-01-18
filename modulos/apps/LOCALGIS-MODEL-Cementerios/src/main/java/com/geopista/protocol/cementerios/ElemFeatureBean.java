/**
 * ElemFeatureBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;

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
