package com.geopista.test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	public TestDate(){
		
	}
	
	public Timestamp getFechaFormateada(Object fecha){
		
		Timestamp tms=null;
		Date today;
		if (fecha instanceof java.lang.String){
			try {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				today = df.parse((String)fecha);
				
			} catch (ParseException e) {				
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				try {
					today = df.parse((String)fecha);
				} catch (ParseException e1) {
					today=new Date();
				}
			}
			tms=new Timestamp(today.getTime());
		}
		else{		
			 tms=new Timestamp(((Date)fecha).getTime());
		}
		return tms;
	}

	
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String value="11-x01-2011";
		new TestDate().getFechaFormateada(value);			
	}

}
