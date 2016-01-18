/**
 * CoordinateSystemRegistry.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.coordsys;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implements a registry for {@link CoordinateSystem}s.
 */
public class CoordinateSystemRegistry {

	private static CoordinateSystemRegistry instance;
	private HashMap nameToCoordinateSystemMap = new HashMap();

	public static CoordinateSystemRegistry instance() {
		if (instance == null) {
			instance = new CoordinateSystemRegistry();
		}
		return instance;
	}

	private CoordinateSystemRegistry() {
		add(CoordinateSystem.UNSPECIFIED);
		add(PredefinedCoordinateSystems.UTM_07N_WGS_84);
		add(PredefinedCoordinateSystems.UTM_08N_WGS_84);
		add(PredefinedCoordinateSystems.UTM_09N_WGS_84);
		add(PredefinedCoordinateSystems.UTM_10N_WGS_84);
		add(PredefinedCoordinateSystems.UTM_11N_WGS_84);    
		add(PredefinedCoordinateSystems.createUTMNorth(28));
		add(PredefinedCoordinateSystems.createUTMNorth(29));
		add(PredefinedCoordinateSystems.createUTMNorth(30));
		add(PredefinedCoordinateSystems.createUTMNorth(31));
		add(PredefinedCoordinateSystems.createUTMNorth(32));
		add(PredefinedCoordinateSystems.UTM_29N_ED50);    
		add(PredefinedCoordinateSystems.UTM_30N_ED50);    
		add(PredefinedCoordinateSystems.UTM_31N_ED50);    
		add(PredefinedCoordinateSystems.UTM_28N_ED50);    
		add(PredefinedCoordinateSystems.UTM_29N_ETRS89);    
		add(PredefinedCoordinateSystems.UTM_30N_ETRS89);    
		add(PredefinedCoordinateSystems.UTM_31N_ETRS89);    
		add(PredefinedCoordinateSystems.UTM_28N_ETRS89);    
	}

	public void add(CoordinateSystem coordinateSystem) {
		nameToCoordinateSystemMap.put(coordinateSystem.getName(), coordinateSystem);
	}

	public Collection getCoordinateSystems() {
		SortedMap nameToSortedCoordinateSystemMap = new TreeMap(nameToCoordinateSystemMap);
		return Collections.unmodifiableCollection(nameToSortedCoordinateSystemMap.values());
	}

	public CoordinateSystem get(String name) {
		return (CoordinateSystem) nameToCoordinateSystemMap.get(name);
	}

	/**
	 * Search CoordinateSystemRegistry for an epsgCode
	 * 
	 * @param epsgCode
	 * @return
	 */
	public CoordinateSystem get(int epsgCode) {
		Collection coords=getCoordinateSystems();
		for (Iterator iter = coords.iterator(); iter.hasNext();)
		{
			CoordinateSystem element = (CoordinateSystem) iter.next();
			if (element.getEPSGCode()==epsgCode)
				return element;
		}
		return CoordinateSystem.UNSPECIFIED;
	}
}
