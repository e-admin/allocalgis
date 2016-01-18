/**
 * GetApps.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.centralizadorsso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.centralizadorsso.beans.LocalGISApp;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACMessage;
import com.geopista.server.database.CPoolDatabase;

public class GetApps extends LoggerHttpServlet {
	
    private static final long serialVersionUID = 9149159948554732570L;
    
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GetApps.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.getWriter().append("Authentication Successful!"); 
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	super.doPost(request);
        ObjectOutputStream oos=null;
        try{
        	
            response.setHeader("Transfer-encoding","chunked");
            response.setHeader("Content-Encoding", "gzip");
            oos = new ObjectOutputStream(new GZIPOutputStream(response.getOutputStream()));
            oos.writeObject((HashMap<String, LocalGISApp>)getLocalgisApps());                  
            
	    }catch(Exception e){
	        e.printStackTrace();	     
	    }/** java.lang.OutOfMemoryError */catch (java.lang.Error e){
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        try{oos.writeObject(new ACException(e));}catch(Exception ex){};
	    }finally{
	        try{oos.close();}catch(Exception e){};
	    }
   }
	
	private HashMap<String, LocalGISApp> getLocalgisApps(){
		 String sSQL = "SELECT app, id_dictionary, acl, perm, app_type, path FROM apps WHERE active=true";
 	     Connection conn=null;
         PreparedStatement ps= null;
         ResultSet rs= null;
         HashMap<String,LocalGISApp> localgisApps = new HashMap<String,LocalGISApp>();                    
         try{
             conn= CPoolDatabase.getConnection();
             ps= conn.prepareStatement(sSQL);                        
             rs= ps.executeQuery();
             LocalGISApp localGISApp= null;
             while (rs.next()){
             	localGISApp = new LocalGISApp();
             	localGISApp.setApp(rs.getString("app")); 
             	localGISApp.setDictionary(getDictionary(rs.getInt("id_dictionary")));
             	localGISApp.setAcl(rs.getString("acl"));
             	localGISApp.setPerm(rs.getString("perm"));
             	localGISApp.setAppType(rs.getString("app_type"));
             	localGISApp.setPath(rs.getString("path"));
             	localGISApp.setActive(true);
             	localgisApps.put(localGISApp.getApp(), localGISApp);
             }
	     } catch(Exception ex){
	    	 logger.error("GetApps - getLocalgisApps: " + ex);	             
         }finally{
             try{rs.close();}catch(Exception e){};
             try{ps.close();}catch(Exception e){};
             try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
         }                
         return localgisApps;
	}
	
	private HashMap<String, String> getDictionary(Integer dictionaryId){
		HashMap<String, String> dictionary = new HashMap<String, String>();		
		if(dictionaryId != null){
			String sSQL = "SELECT id_vocablo, locale, traduccion FROM dictionary WHERE id_vocablo=" + dictionaryId;
		    Connection conn=null;
	        PreparedStatement ps= null;
	        ResultSet rs= null;
	        try{
	            conn= CPoolDatabase.getConnection();
	            ps= conn.prepareStatement(sSQL);                        
	            rs= ps.executeQuery();	            
	            while (rs.next()){
	            	dictionary.put(rs.getString("locale").toLowerCase(), rs.getString("traduccion"));	
	            }
	        } catch(Exception ex){
	            logger.error("GetApps - getDictionary: " + ex);	          
	        }finally{
	            try{rs.close();}catch(Exception e){};
	            try{ps.close();}catch(Exception e){};
	            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){};
	        }                    
		}
		return dictionary;
	}
	
}

