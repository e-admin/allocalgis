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
