/**
 * Rango.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;


public class Rango {

	private double max;
	private double min;


	public Rango(
			double val) {
		this.min = val;
		this.max = val;
	
	}
	
	public Rango(
			double min,
			double max) {
		this.min = min;
		this.max = max;
	
	}
	public boolean equals(Object obj) {

		boolean isEquals = false;
		if (obj instanceof Rango)
		{
			if(max == ((Rango)obj).getMax() && min ==((Rango)obj).getMin())
				isEquals = true;
		}
		return isEquals;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}
	

	public void setMax(double object) {
		max = object;
	}

	public void setMin(double object) {
		min = object;
	}

	public boolean contains (Rango r)
	{
		boolean isContained = false;
		
		if (r.getMin()>= this.getMin() 
				&& r.getMax()<= this.getMax() )
			isContained = true;
		
		return isContained;
	}
	
	public int hashCode ()
	{
		return 0;
	}

}
