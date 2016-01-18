/**
 * PermissionVO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.validation.vo;

import java.io.Serializable;

public class PermissionVO implements Serializable {

	private static final long serialVersionUID = -4003617322946337242L;

	private String layer;
	private int idPerm;
	
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public int getIdPerm() {
		return idPerm;
	}
	public void setIdPerm(int idPerm) {
		this.idPerm = idPerm;
	}
	
	public String toString() {
		return "(" + layer + ", " + idPerm + ")";
	}
	public boolean equals(Object o) {
		if (o instanceof PermissionVO) {
			PermissionVO p = (PermissionVO) o;
			if (p.layer == null) {
				return layer == null && p.idPerm == idPerm;
			}
			else {
				return p.layer.equals(layer) && p.idPerm == idPerm;
			}
		}
		return false;
	}
}
