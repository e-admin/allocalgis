/**
 * VirtualNodeInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.structure.Edge;

import com.vividsolutions.jts.geom.LineString;

public class VirtualNodeInfo {
	private Point point;
	private Edge edge;
	private LineString linestringAtoV;
	private LineString linestringVtoB;
	//Punto sobre el LineString mas cercano en formato jts para operaciones de distancia
	private com.vividsolutions.jts.geom.Point virtualNodePoint;
	public com.vividsolutions.jts.geom.Point getVirtualNodePoint() {
		return virtualNodePoint;
	}
	public void setVirtualNodePoint(
			com.vividsolutions.jts.geom.Point virtualNodePoint) {
		this.virtualNodePoint = virtualNodePoint;
	}
	public LineString getLinestringAtoV() {
		return linestringAtoV;
	}
	public void setLinestringAtoV(LineString linestringAtoV) {
		this.linestringAtoV = linestringAtoV;
	}
	public LineString getLinestringVtoB() {
		return linestringVtoB;
	}
	public void setLinestringVtoB(LineString linestringVtoB) {
		this.linestringVtoB = linestringVtoB;
	}
	public Edge getEdge() {
		return edge;
	}
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	private double ratio;
	private String networkName;
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	private double distance;
}
