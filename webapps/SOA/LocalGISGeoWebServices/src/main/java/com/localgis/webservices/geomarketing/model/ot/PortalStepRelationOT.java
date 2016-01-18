/**
 * PortalStepRelationOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.ot;

public class PortalStepRelationOT {
	private Integer idPortal;
	private Integer idTramo;
	private Double distance;
	public Integer getIdPortal() {
		return idPortal;
	}
	public void setIdPortal(Integer idPortal) {
		this.idPortal = idPortal;
	}
	public Integer getIdTramo() {
		return idTramo;
	}
	public void setIdTramo(Integer idTramo) {
		this.idTramo = idTramo;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
