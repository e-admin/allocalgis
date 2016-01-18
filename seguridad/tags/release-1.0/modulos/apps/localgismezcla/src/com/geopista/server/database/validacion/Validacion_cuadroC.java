package com.geopista.server.database.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.database.COperacionesEIEL;

public class Validacion_cuadroC {
	
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
			String idProvincia = String.valueOf(idMunicipio).substring(0, 2);
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
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroC.class +" " +  e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        	COperacionesEIEL.safeClose(rs1, ps1, null);
        }
	}

}
