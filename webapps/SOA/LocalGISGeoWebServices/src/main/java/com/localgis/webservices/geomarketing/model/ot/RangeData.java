/**
 * RangeData.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.ot;

import java.io.Serializable;

public class RangeData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7879100759655778757L;
	private static String COLUMN = "((current_date-nacfecha)/365)";
	private Integer value;
	private Integer startRange;
	private Integer endRange;
	public String getAlias() {
		String alias = "";
		if(startRange != null && endRange != null)
			alias =  "range_"+startRange+"_"+endRange;
		else if(startRange != null)
			alias =  "range_major_"+startRange;
		else if(endRange != null)
			alias =  "range_minor_"+endRange;
		return alias;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getStartRange() {
		return startRange;
	}
	public void setStartRange(Integer startRange) {
		this.startRange = startRange;
	}
	public Integer getEndRange() {
		return endRange;
	}
	public void setEndRange(Integer endRange) {
		this.endRange = endRange;
	}
	public String getSelectQueryColumn(){
		String query = "";
		if(startRange != null && endRange != null)
			query = "CASE WHEN " + RangeData.COLUMN + " >= " + startRange + " and " + RangeData.COLUMN + " < " + endRange + " then 1 else 0 end as "+getAlias();
		else if(startRange != null)
			query = "CASE WHEN " + RangeData.COLUMN + " >= " + startRange + " then 1 else 0 end as "+getAlias();
		else if(endRange != null)//Caso en que sea de tipo 
			query = "CASE WHEN " + RangeData.COLUMN + " < " + endRange + " then 1 else 0 end as "+getAlias();
		return query;
		
	}
}
