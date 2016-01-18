/**
 * CoordinateSystem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.coordsys;

/** 
 * This class represents a coordinate system.
 */
public class CoordinateSystem {
	
    private String name;
    private String datum;
    private int epsgCode;
    private int zone;

	public static final CoordinateSystem UNSPECIFIED = new CoordinateSystem("Unspecified",
	    0, null,0) {

	    public String getDatum() {
	        throw new UnsupportedOperationException();
	    }
	    public int getEPSGCode() {
	        throw new UnsupportedOperationException();
	    }
	    public int getZone() {
	        throw new UnsupportedOperationException();
	    }
	};

    public CoordinateSystem(String name, int epsgCode, String datum, int zone) {
        this.name = name;
        this.epsgCode = epsgCode;
        this.datum = datum;
        this.zone = zone;
    }
   
    
   
    public String toString() {
        return name;
    }
    public String getName() {
        return name;
    }

    public int getEPSGCode() {
        return epsgCode;
    }

    public String getDatum() {
        return datum;
    }

    public int getZone() {
        return zone;
    }

}
