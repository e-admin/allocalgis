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
