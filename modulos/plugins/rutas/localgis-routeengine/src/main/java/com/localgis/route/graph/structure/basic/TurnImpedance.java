/**
 * TurnImpedance.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.structure.basic;

import java.io.Serializable;

public class TurnImpedance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idEdgeStart;
	private Integer idEdgeEnd;
	private double impedance = Double.MAX_VALUE;
	public TurnImpedance(Integer idEdgeStart2, Integer idEdgeEnd2, double impedance2) {
		this.idEdgeStart=idEdgeStart2;
		this.idEdgeEnd=idEdgeEnd2;
		this.impedance=impedance2;
	}
	public Integer getIdEdgeStart() {
		return idEdgeStart;
	}
	public void setIdEdgeStart(Integer idEdgeStart) {
		this.idEdgeStart = idEdgeStart;
	}
	public Integer getIdEdgeEnd() {
		return idEdgeEnd;
	}
	public void setIdEdgeEnd(Integer idEdgeEnd) {
		this.idEdgeEnd = idEdgeEnd;
	}
	public double getImpedance() {
		return impedance;
	}
	public void setImpedance(double impedance) {
		this.impedance = impedance;
	}
	
	public boolean equals(Object o) {
		if(o instanceof TurnImpedance){
			TurnImpedance t = (TurnImpedance)o;
			if(t.getIdEdgeStart().intValue() == this.idEdgeStart &&
			   t.getIdEdgeEnd().intValue() == this.idEdgeEnd)
				return true;
		}
		return false;
		
	}
}
