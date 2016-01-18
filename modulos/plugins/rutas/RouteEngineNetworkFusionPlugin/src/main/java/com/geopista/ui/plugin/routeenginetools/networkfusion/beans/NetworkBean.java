/**
 * NetworkBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfusion.beans;

import org.uva.route.network.Network;

import com.localgis.route.graph.structure.basic.GraphType;
import com.localgis.route.graph.structure.basic.ReaderWriterType;

public class NetworkBean {
	
	private Network network;
	private ReaderWriterType rwType;
	private GraphType gType;
	
	public NetworkBean(Network network) {
		this.network = network;
	}
	public Network getNetwork() {
		return network;
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public ReaderWriterType getRwType() {
		return rwType;
	}
	public void setRwType(ReaderWriterType rwType) {
		this.rwType = rwType;
	}
	public GraphType getgType() {
		return gType;
	}
	public void setgType(GraphType gType) {
		this.gType = gType;
	}
	public String getNetworkName(){
		return network.getName();
	}
}
