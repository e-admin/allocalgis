/**
 * NamedVector.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.mobile;

import java.util.Vector;

class NamedVector extends Vector {
	  String name;

	  public NamedVector(String name) {
	    this.name = name;
	  }

	  public NamedVector(String name, Object elements[]) {
	    this.name = name;
	    for (int i = 0, n = elements.length; i < n; i++) {
	      add(elements[i]);
	    }
	  }

	  public String toString() {
	    return name;
	  }
	  
	  public String getName(){
		  return this.name;
	  }
	}
