package com.localgis.app.gestionciudad.plugins.geomarketing.utils;

import java.io.Serializable;

public class RangeData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8518610236931414812L;
	
	private Integer value = 0;
	private Integer startRange = 0;
	private Integer endRange = 0;
	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	/**
	 * @return the startRange
	 */
	public Integer getStartRange() {
		return startRange;
	}
	/**
	 * @param startRange the startRange to set
	 */
	public void setStartRange(Integer startRange) {
		this.startRange = startRange;
	}
	/**
	 * @return the endRange
	 */
	public Integer getEndRange() {
		return endRange;
	}
	/**
	 * @param endRange the endRange to set
	 */
	public void setEndRange(Integer endRange) {
		this.endRange = endRange;
	}
	
	public void readFromString(String rangeDataString){
		if (rangeDataString!=null && !rangeDataString.equals("")){
			String[] values = rangeDataString.split(":");
			if(values.length == 3){
				int startRange = -1;
				int endRange = -1;
				int value = -1;
				if (values[0]!=null && !values[0].equals("") && !values[0].equals("null")){
					try{
						startRange = Integer.parseInt(values[0]);
					}catch (NumberFormatException e) {
						e.printStackTrace();
						startRange = -1;
					}
				}
				if (values[1]!=null && !values[1].equals("") && !values[1].equals("null")){
					try{
						endRange = Integer.parseInt(values[1]);
					}catch (NumberFormatException e) {
						e.printStackTrace();
						endRange = -1;
					}
				}
				if (values[2]!=null && !values[2].equals("") && !values[2].equals("null")){
					try{
						value = Integer.parseInt(values[2]);
					}catch (NumberFormatException e) {
						e.printStackTrace();
						value = -1;
					}
				}
				
				if (startRange >= 0){
					this.setStartRange(startRange);
				}
				if (endRange >= 0){
					this.setEndRange(endRange);
				}
				if (value >=0){
					this.setValue(value);
				}
				
			}
		}
	}
	
	public boolean compareRangesDatas(RangeData rangeData){
		if (this.getEndRange() == rangeData.getEndRange() &&
				this.getStartRange() == this.getStartRange() ){
			return true;
		}
		return false;
	}
	

}
