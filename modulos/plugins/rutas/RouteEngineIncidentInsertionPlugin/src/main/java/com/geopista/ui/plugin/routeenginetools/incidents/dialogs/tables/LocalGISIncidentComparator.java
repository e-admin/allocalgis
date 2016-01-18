/**
 * LocalGISIncidentComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.incidents.dialogs.tables;

import java.util.Comparator;

import com.localgis.route.graph.structure.basic.LocalGISIncident;

public class LocalGISIncidentComparator implements Comparator{

	public int compare(Object o1, Object o2) {
		int result = 1;
		if(o1 instanceof LocalGISIncident && o2 instanceof LocalGISIncident){
			LocalGISIncident nb1 = (LocalGISIncident)o1;
			LocalGISIncident nb2 = (LocalGISIncident)o2;
			if(nb1.getDateStart() != null && nb1.getDateStart() == null){
				return 1;
			}
			if(nb1.getDateStart() == null && nb1.getDateStart() != null){
				return -1;
			}
			if(nb1.getDateStart().before(nb2.getDateStart())){
				return 1;
			}
			if(nb1.getDateStart().after(nb2.getDateStart())){
				return -1;
			}
			if(nb1.getDateStart().compareTo(nb2.getDateStart()) == 0){
				if(nb1.getDateEnd() != null && nb2.getDateEnd() == null){
					return 1;
				}
				if(nb1.getDateEnd() == null && nb2.getDateEnd() != null){
					return -1;
				}
				if(nb1.getDateEnd() == null && nb2.getDateEnd() == null)
					return 1;
				if(nb1.getDateEnd().before(nb2.getDateEnd())){
					return 1;
				}
				if(nb1.getDateEnd().after(nb2.getDateEnd())){
					return -1;
				}
			}
		}
		return result;
	}

}
