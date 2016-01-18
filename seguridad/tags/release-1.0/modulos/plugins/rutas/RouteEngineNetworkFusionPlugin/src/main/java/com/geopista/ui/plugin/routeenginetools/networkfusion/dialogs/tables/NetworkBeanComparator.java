package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.tables;

import java.util.Comparator;

import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;

public class NetworkBeanComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		int result = 0;
		if(o1 instanceof NetworkBean && o2 instanceof NetworkBean){
			NetworkBean nb1 = (NetworkBean)o1;
			NetworkBean nb2 = (NetworkBean)o2;
			result = nb1.getNetworkName().compareToIgnoreCase(nb2.getNetworkName());
		}
		return result;
	}

}
