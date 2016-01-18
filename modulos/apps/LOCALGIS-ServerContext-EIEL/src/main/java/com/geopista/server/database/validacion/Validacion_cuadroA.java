/**
 * Validacion_cuadroA.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_provincia_bean;

public class Validacion_cuadroA {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroA.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ArrayList lstProvincia= new ArrayList();
		try
        {
			str.append(Messages.getString("cuadroA") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "SELECT * FROM v_PROVINCIA";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			int contTexto = 0;
			while (rs.next()) {		
				V_provincia_bean provincia_bean = new V_provincia_bean();
				provincia_bean.setProvincia(rs.getString("PROVINCIA"));
				provincia_bean.setDenominaci(rs.getString("DENOMINACI"));
				
				lstProvincia.add(provincia_bean);			
			}
			
			for(int i=0; i<lstProvincia.size(); i++){
				V_provincia_bean provincia_bean = (V_provincia_bean)lstProvincia.get(i);
				int contador = 0;
				 sql = "SELECT COUNT(*) AS total FROM v_PADRON WHERE PROVINCIA="+provincia_bean.getProvincia();
				 ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {	
					contador= rs.getInt("total");
				}

				if(contador == 0){
					if (contTexto == 0)
					 {
						str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroA.V_01") + "\n");	
					 }
					str.append( provincia_bean.getProvincia() + "\t");				
				}
			}
			
			if(lstProvincia.isEmpty()){
				if (contTexto == 0)
				 {
					 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroA.falsoerror.V_1") + "\n");
					 contTexto++;
				 }

			}
			
//			if(!lstProvincia.isEmpty()){
//				
//				str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroA.V_01") + "\n");
//				for(int i=0; i<lstProvincia.size(); i++){
//					str.append(((V_provincia_bean)lstProvincia.get(i)).getProvincia());
//					str.append("\t");
//				}
//			}
			str.append("\n\n");
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadroA.class +" " +  e.getMessage());
        	 str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
