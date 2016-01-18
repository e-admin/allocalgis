/**
 * Validacion_cuadroC.java
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

import com.geopista.app.AppContext;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.database.COperacionesEIEL;

public class Validacion_cuadroC {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroC.class);
	public static void validacion(Connection connection, StringBuffer str, Integer idMunicipio, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		int acceso = 0;
		
		try
        {
			str.append(Messages.getString("cuadroC") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			//String idProvincia = userSesion.getIdMunicipio().substring(0, 2);
			String idProvincia = completarConCeros(String.valueOf(idMunicipio),5).substring(0, 2);
			String sql = "SELECT * FROM v_MUNICIPIO WHERE provincia = ?";
			
			ps = connection.prepareStatement(sql);
			ps.setString(1, idProvincia);
			rs = ps.executeQuery();
			
			while (rs.next()) {		
				if ((!rs.getString("provincia").equals("")) && (!rs.getString("municipio").equals("")))
                {
					String sql1 = "SELECT COUNT(*) as total FROM v_PADRON WHERE provincia = ? AND municipio = ?";
					ps1 = connection.prepareStatement(sql1);
					ps1.setString(1, rs.getString("provincia"));
					ps1.setString(2, rs.getString("municipio"));
					rs1 = ps1.executeQuery();
					int total = 0;
					while (rs1.next()) {		
						total =  rs1.getInt("total");
					}
					
					if(	total == 0){
						
						if (acceso == 0)
                        {
							str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroC.V_01") + "\n");
                        } 
						str.append(rs.getString("provincia")+rs.getString("municipio") + "\t");
					}
                }
				else{
					
				}
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadroC.class +" " +  e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        	COperacionesEIEL.safeClose(rs1, ps1, null);
        }
	}
    /**
     * 
     * @param cadena 
     * @param longitud 
     * @return 
     */
	public static String completarConCeros(String cadena, int longitud){

		while (cadena.length()< longitud){
			cadena = '0' + cadena;
		}
		return cadena;
	}  
}
