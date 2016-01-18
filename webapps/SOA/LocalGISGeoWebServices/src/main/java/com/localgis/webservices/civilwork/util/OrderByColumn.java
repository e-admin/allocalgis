/**
 * OrderByColumn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.util;

import java.io.Serializable;

public class OrderByColumn  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String column;
	private Boolean isAsc;
	public OrderByColumn(String column){
		this.column = column;
	}
	public OrderByColumn(){
		super();
	}
	public OrderByColumn(String column,Boolean isAsc){
		this.column = column;
		this.isAsc = isAsc;	
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public boolean isAsc() {
		return isAsc;
	}
	public void setAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}
	public String toString(){
		String result = column;
		if(isAsc != null){
			if(isAsc)
				result +=" ASC";
			else
				result += " DESC";
		}
		return result;
	}
	
}
