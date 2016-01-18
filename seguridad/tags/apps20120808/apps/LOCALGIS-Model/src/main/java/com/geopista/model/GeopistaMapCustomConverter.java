package com.geopista.model;

import java.util.Date;

import com.vividsolutions.jump.util.java2xml.XMLBinder.CustomConverter;

public class GeopistaMapCustomConverter {

	
	 public static CustomConverter getMapDateCustomConverter()
	  {
	    return new CustomConverter() {
	          public Object toJava(String value) {
	            Date mapTime = new Date(Long.parseLong(value));
	            return mapTime;
	        
	            }
	            
	          public String toXML(Object object) {
	            Date actualTime = new Date();
	            long actualMilliseconds = actualTime.getTime();
	            return String.valueOf(actualMilliseconds);
	            }
	        };
	  }
}
