/**
 * MapComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.util;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;

import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;

public class MapComparator implements Comparator{
    

    public int compare(Object map, Object otherMap) {
    	Collator collator = RuleBasedCollator.getInstance();
    	if (map instanceof GeopistaMap) {
    	   	GeopistaMap mapToCompare,otherMapToCompare; 
            mapToCompare = (GeopistaMap) map;
            otherMapToCompare = (GeopistaMap) otherMap;
            return collator.compare(mapToCompare.getName(),otherMapToCompare.getName());
    	}
    	else  if (map instanceof LocalgisMap) {
    		LocalgisMap mapToCompare,otherMapToCompare; 
            mapToCompare = (LocalgisMap) map;
            otherMapToCompare = (LocalgisMap) otherMap;
            return collator.compare(mapToCompare.getName(),otherMapToCompare.getName());
    		}
    	else {
    		String mapHashCode = Integer.toString(map.hashCode());
    		String otherMapHashCode = Integer.toString(otherMap.hashCode());
    		return mapHashCode.compareTo(otherMapHashCode);
    	}

    }
}


