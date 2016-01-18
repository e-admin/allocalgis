/**
 * FuncionesComunes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class FuncionesComunes {

	//Funcion comun a varios cuadros que compruba que los nucleos que figuran en el cuadro tabla1
    //esten tambien en el cuadro tabla2
	 public static int ValidaExistenciaCODINE(Connection connection, ArrayList lstTabla1, String tabla2, String tiperror, StringBuffer str)
     {
		PreparedStatement ps = null;
		ResultSet rs = null;
	
         String pro = "";
         String mun = "";
         String ent = "";
         String nuc = "";
         int contTexto = 0;
         String sqlTabla2 = "SELECT * FROM " + tabla2;
         
         try
         {
        	ps = connection.prepareStatement(sqlTabla2);
   			rs = ps.executeQuery();
               
   			ArrayList lstTabla2 = new ArrayList();
   			while (rs.next()) {	
   				CodIne_bean objCodIne = new CodIne_bean();
   				
   				objCodIne.setProvincia(rs.getString("PROVINCIA"));
   				objCodIne.setMunicipio(rs.getString("MUNICIPIO"));
   				objCodIne.setEntidad(rs.getString("ENTIDAD"));
   				objCodIne.setNucleo(rs.getString("NUCLEO"));
   				
   				lstTabla2.add(objCodIne);
   			}


             for (int i = 0; i < lstTabla1.size(); i++)
             {
            	 CodIne_bean objCodIneTabla1 = (CodIne_bean)lstTabla1.get(i);
            	 
                 pro = objCodIneTabla1.getProvincia();
                 mun = objCodIneTabla1.getMunicipio();
                 ent = objCodIneTabla1.getEntidad();
                 nuc = objCodIneTabla1.getNucleo();

                 int encontrados = 0;
                 for (int j = 0; j < lstTabla2.size(); j++){
                	 
                	 if(((CodIne_bean)lstTabla2.get(j)).getProvincia().equals(pro) &&
                			 ((CodIne_bean)lstTabla2.get(j)).getMunicipio().equals(mun) &&
                			 ((CodIne_bean)lstTabla2.get(j)).getEntidad().equals(ent) &&
                			 ((CodIne_bean)lstTabla2.get(j)).getNucleo().equals(nuc)){
                		 encontrados ++;
                	 }
                	 
                 }
                 
                 if (encontrados == 0){
                	//NO EXISTE EN LA TABLA Tabla2
                	 if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString(tiperror) + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(pro + mun + ent + nuc +"\t");
					 str.append("\t");
                 }
                 
             }
         }
         catch (Exception e)
         {
        	 str.append(Messages.getString("exception") + " " +FuncionesComunes.class + e.getMessage());
         }
         finally
         {
        		COperacionesEIEL.safeClose(rs, ps, null);
         }
         
         return contTexto;

     }
	 
	//Funcion comun a varios cuadros que compruba que los nucleos que figuran en el cuadro tabla1
	    //esten tambien en el cuadro tabla2
		 public static int ValidaExistenciaCODINE_poblamient(Connection connection, ArrayList lstTabla1, String tabla2, String tiperror, StringBuffer str)
	     {
			PreparedStatement ps = null;
			ResultSet rs = null;
		
	         String pro = "";
	         String mun = "";
	         String ent = "";
	         String nuc = "";

	         String sqlTabla2 = "SELECT * FROM " + tabla2;
	         int contTexto = 0;
	         try
	         {
	        	ps = connection.prepareStatement(sqlTabla2);
	   			rs = ps.executeQuery();
	               
	   			ArrayList lstTabla2 = new ArrayList();
	   			while (rs.next()) {	
	   				CodIne_bean objCodIne = new CodIne_bean();
	   				
	   				objCodIne.setProvincia(rs.getString("PROVINCIA"));
	   				objCodIne.setMunicipio(rs.getString("MUNICIPIO"));
	   				objCodIne.setEntidad(rs.getString("ENTIDAD"));
	   				objCodIne.setNucleo(rs.getString("POBLAMIENT"));
	   				
	   				lstTabla2.add(objCodIne);
	   			}
	   			
	           

	             for (int i = 0; i < lstTabla1.size(); i++)
	             {
	            	 CodIne_bean objCodIneTabla1 = (CodIne_bean)lstTabla1.get(i);
	            	 
	                 pro = objCodIneTabla1.getProvincia();
	                 mun = objCodIneTabla1.getMunicipio();
	                 ent = objCodIneTabla1.getEntidad();
	                 nuc = objCodIneTabla1.getNucleo();

	                 int encontrados = 0;
	                 for (int j = 0; j < lstTabla2.size(); j++){
	                	 
	                	 if(((CodIne_bean)lstTabla2.get(j)).getProvincia().equals(pro) &&
	                			 ((CodIne_bean)lstTabla2.get(j)).getMunicipio().equals(mun) &&
	                			 ((CodIne_bean)lstTabla2.get(j)).getEntidad().equals(ent) &&
	                			 ((CodIne_bean)lstTabla2.get(j)).getNucleo().equals(nuc)){
	                		 encontrados ++;
	                	 }
	                	 
	                 }
	                 
	                 if (encontrados == 0){
	                	//NO EXISTE EN LA TABLA Tabla2
	                	 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString(tiperror) + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(pro + mun + ent + nuc +"\t");
						 str.append("\t");
	                 }
	                 
	             }
	         }
	         catch (Exception e)
	         {
	        	 str.append(Messages.getString("exception") + " " +FuncionesComunes.class + e.getMessage());
	         }
	         finally
	         {
	        		COperacionesEIEL.safeClose(rs, ps, null);
	         }
	         
	         return contTexto;

	     }
	 
	 //Funcion comun a varios cuadros que compruba que los municipios que figuran en el cuadro DtTabla1
     //esten tambien en el cuadro tabla2
	
     public static int ValidaExistenciaCODMUN(Connection connection, ArrayList lstTabla1, String tabla2, String tiperror, StringBuffer str)
     {
    	PreparedStatement ps = null;
 		ResultSet rs = null;
 		

        String pro = "";
        String mun = "";
        
 		String sqlTabla2 = "SELECT  * FROM " + tabla2;
 		int contTexto = 0;
 		 try
         {
 			
        	ps = connection.prepareStatement(sqlTabla2);
   			rs = ps.executeQuery();
 		
   			ArrayList lstTabla2 = new ArrayList();
   			while (rs.next()) {	
   				CodIne_bean objCodIne = new CodIne_bean();
   				
   				objCodIne.setProvincia(rs.getString("PROVINCIA"));
   				objCodIne.setMunicipio(rs.getString("MUNICIPIO"));
   				
   				lstTabla2.add(objCodIne);
   			}
   			
	   			
	   		 for (int i = 0; i < lstTabla1.size(); i++)
	         {
	        	 CodIne_bean objCodIneTabla1 = (CodIne_bean)lstTabla1.get(i);
	        	 
	             pro = objCodIneTabla1.getProvincia();
	             mun = objCodIneTabla1.getMunicipio();
	
	
	             int encontrados = 0;
	             for (int j = 0; j < lstTabla2.size(); j++){
	            	 
	            	 if(((CodIne_bean)lstTabla2.get(j)).getProvincia().equals(pro) &&
	            			 ((CodIne_bean)lstTabla2.get(j)).getMunicipio().equals(mun)){
	            		 encontrados ++;
	            	 }
	            	 
	             }
	             
	             if (encontrados == 0){
	            	//NO EXISTE EN LA TABLA Tabla2
	            	 if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString(tiperror) + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(pro + mun+"\t");
					 str.append("\t");
	             }
	             
	         }
 
         }
         catch (Exception e)
         {
        	 str.append(Messages.getString("exception") + " " +FuncionesComunes.class + e.getMessage());
         }
         finally
         {
        		COperacionesEIEL.safeClose(rs, ps, null);
         }
         
         return contTexto;
	
     }
     
}
