/**
 * TypeUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
