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
