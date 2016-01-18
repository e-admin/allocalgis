package com.geopista.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeUtil {

    public static Long getLong(ResultSet rs, String nombreCampo){
    	
    	try {
			long valor=rs.getLong(nombreCampo);
			if (rs.wasNull())
				return null;
			else
				return new Long(valor);
		} catch (SQLException e) {
			return null;
		}
    }
    
    public static Integer getInteger(ResultSet rs, String nombreCampo){
    	
    	try {
    		int valor=rs.getInt(nombreCampo);
			if (rs.wasNull())
				return null;
			else
				return new Integer(valor);
		} catch (SQLException e) {
			return null;
		}
    }
   
    
    public static Float getFloat(ResultSet rs, String nombreCampo){
    	
    	try {
    		float valor=rs.getFloat(nombreCampo);
			if (rs.wasNull())
				return null;
			else
				return new Float(valor);
		} catch (SQLException e) {
			return null;
		}
    }   
    
    
    public static Double getDouble(ResultSet rs, String nombreCampo){
    	
    	try {
    		double valor=rs.getDouble(nombreCampo);
			if (rs.wasNull())
				return null;
			else
				return new Double(valor);
		} catch (SQLException e) {
			return null;
		}
    }    
    
    public static long getSimpleLong(ResultSet rs, String nombreCampo){
    	
    	try {
			long valor=rs.getLong(nombreCampo);
			if (rs.wasNull())
				return -1;
			else
				return valor;
		} catch (SQLException e) {
			return -1;
		}
    }    
    public static int getSimpleInteger(ResultSet rs, String nombreCampo){
    	
    	try {
    		int valor=rs.getInt(nombreCampo);
			if (rs.wasNull())
				return -1;
			else
				return valor;
		} catch (SQLException e) {
			return -1;
		}
    }
    public static float getSimpleFloat(ResultSet rs, String nombreCampo){
    	
    	try {
    		float valor=rs.getFloat(nombreCampo);
			if (rs.wasNull())
				return -1.0f;
			else
				return valor;
		} catch (SQLException e) {
			return -1.0f;
		}
    }    
    
    public static double getSimpleDouble(ResultSet rs, String nombreCampo){
    	
    	try {
    		double valor=rs.getDouble(nombreCampo);
			if (rs.wasNull())
				return -1.0;
			else
				return valor;
		} catch (SQLException e) {
			return -1.0;
		}
    }
}
