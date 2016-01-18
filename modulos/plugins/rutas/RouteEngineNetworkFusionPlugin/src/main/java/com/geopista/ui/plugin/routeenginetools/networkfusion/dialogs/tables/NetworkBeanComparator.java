/**
 * NetworkBeanComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
