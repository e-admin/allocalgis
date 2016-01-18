package com.geopista.ui.plugin.routeenginetools.routeutil;

import org.uva.route.network.InterNetworker;
import org.uva.route.network.NetworkManager;

import com.localgis.route.network.LocalGISInterNetworker;
import com.localgis.route.network.LocalGISNetworkManager;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class FuncionesAuxiliares {
	public FuncionesAuxiliares() {

	} 

	/*
	 * public static int getFactorial (int x) { if (x < 0) { return 0; }
	 * 
	 * int factorial = 1;
	 * 
	 * while (x > 1) { factorial = factorial * x; x = x - 1; } return factorial;
	 * }
	 */

	public static NetworkManager getNetworkManager(PlugInContext context) {
		NetworkManager networkMgr = (NetworkManager) context
				.getWorkbenchContext().getBlackboard().get("RedesDefinidas");
		if (networkMgr == null) {
			networkMgr = new LocalGISNetworkManager();
			context.getWorkbenchContext().getBlackboard().put("RedesDefinidas",
					networkMgr);
			InterNetworker networker = new LocalGISInterNetworker();
			networker.addNetworkManager(networkMgr);
			networkMgr.setInterNetworker(networker);
		}
		
		return networkMgr;
	}
	
	
	
	public static NetworkManager getNetworkManager(Blackboard blackboard) {
		NetworkManager networkMgr = (NetworkManager) blackboard.get("RedesDefinidas");
		if (networkMgr == null) {
			networkMgr = new LocalGISNetworkManager();
			blackboard.put("RedesDefinidas",
					networkMgr);
			InterNetworker networker = new LocalGISInterNetworker();
			networker.addNetworkManager(networkMgr);
			networkMgr.setInterNetworker(networker);
		}
		
		return networkMgr;
	}
}