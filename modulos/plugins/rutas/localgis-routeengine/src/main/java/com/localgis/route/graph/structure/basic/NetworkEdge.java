/**
 * NetworkEdge.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

public class NetworkEdge{

	private double distance;
	private Integer idEdge;
	private Integer networkId;
	public Integer getIdEdge() {
		return idEdge;
	}
	public void setIdEdge(Integer idEdge) {
		this.idEdge = idEdge;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Integer getNetworkId()
	{
	    return networkId;
	}
	public void setNetworkId(Integer networkId)
	{
	    this.networkId = networkId;
	}

}
