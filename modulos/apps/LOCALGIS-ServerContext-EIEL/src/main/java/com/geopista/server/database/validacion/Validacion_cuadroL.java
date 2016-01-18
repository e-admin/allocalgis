/**
 * Validacion_cuadroL.java
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
import com.geopista.server.database.validacion.beans.V_colector_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;

public class Validacion_cuadroL {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroL.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroL") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;
	        String sql = "select * from  v_COLECTOR";
	        String sql_2 = "SELECT * FROM v_COLECTOR_NUCLEO";
	        String sql_3 = "SELECT * FROM v_COLECTOR_ENC";
	        String sql_4 = "SELECT * FROM v_COLECTOR_ENC_M50";
			
	        ArrayList lstcolector = new ArrayList();
	    	ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lstcolector_enc = new ArrayList();
			ArrayList lstcolector_enc_m50 = new ArrayList();
			
			
			 ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_bean colector_bean = new V_colector_bean();
				colector_bean.setProvincia(rs.getString("PROVINCIA"));
				colector_bean.setMunicipio(rs.getString("MUNICIPIO"));
				colector_bean.setOrden_cole(rs.getString("ORDEN_COLE"));
				colector_bean.setClave(rs.getString("CLAVE"));
				
				lstcolector.add(colector_bean);
				
			}
				
	        ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_nucleo_bean colector_nucleoBean = new V_colector_nucleo_bean();

				colector_nucleoBean.setProvincia(rs.getString("provincia"));
				colector_nucleoBean.setMunicipio(rs.getString("municipio"));
				colector_nucleoBean.setEntidad(rs.getString("entidad"));
				colector_nucleoBean.setNucleo(rs.getString("nucleo"));
				colector_nucleoBean.setClave(rs.getString("clave"));
				colector_nucleoBean.setC_provinci(rs.getString("c_provinc"));
				colector_nucleoBean.setC_municipi(rs.getString("c_municip"));
				colector_nucleoBean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_nucleo.add(colector_nucleoBean);
				
			}
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_enc_bean colectorEncBean = new V_colector_enc_bean();
				
				colectorEncBean.setClave(rs.getString("clave"));
				colectorEncBean.setProvincia(rs.getString("provincia"));
				colectorEncBean.setMunicipio(rs.getString("municipio"));
				colectorEncBean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_enc.add(colectorEncBean);
				
			}
			
	        
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_enc_m50_bean colectorEncM50Bean = new V_colector_enc_m50_bean();
				
				colectorEncM50Bean.setClave(rs.getString("clave"));
				colectorEncM50Bean.setProvincia(rs.getString("provincia"));
				colectorEncM50Bean.setMunicipio(rs.getString("municipio"));
				colectorEncM50Bean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_enc_m50.add(colectorEncM50Bean);
				
			}
	        
			
			for (int i = 0; i < lstcolector.size(); i++)
            {
				V_colector_bean colector_bean   = (V_colector_bean)lstcolector.get(i);
				int contador = 0;
				if(!colector_bean.getProvincia().equals("") || !colector_bean.getMunicipio().equals("")){
					  if (!colector_bean.getOrden_cole().equals(""))
                      {
							for (int j = 0; j < lstcolector_nucleo.size(); j++)
				            {
								V_colector_nucleo_bean colector_nucleo_bean   = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
								if(colector_nucleo_bean.getProvincia().equals(colector_bean.getProvincia()) &&
										colector_nucleo_bean.getMunicipio().equals(colector_bean.getMunicipio())){
									if(colector_nucleo_bean.getOrden_cole().equals(colector_bean.getOrden_cole()) &&
											colector_nucleo_bean.getClave().equals(colector_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							
							if (contador == 0){
								for (int j = 0; j < lstcolector_enc.size(); j++)
					            {
									V_colector_enc_bean colector_enc_bean   = (V_colector_enc_bean)lstcolector_enc.get(j);
									if(colector_enc_bean.getProvincia().equals(colector_bean.getProvincia()) &&
											colector_enc_bean.getMunicipio().equals(colector_bean.getMunicipio())){
										if(colector_enc_bean.getOrden_cole().equals(colector_bean.getOrden_cole()) &&
												colector_enc_bean.getClave().equals(colector_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}
							
							if (contador == 0){
								for (int j = 0; j < lstcolector_enc_m50.size(); j++)
					            {
									V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(j);
									if(colector_enc_m50_bean.getProvincia().equals(colector_bean.getProvincia()) &&
											colector_enc_m50_bean.getMunicipio().equals(colector_bean.getMunicipio())){
										if(colector_enc_m50_bean.getOrden_cole().equals(colector_bean.getOrden_cole()) &&
												colector_enc_m50_bean.getClave().equals(colector_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}	
							

							if (contador == 0){
								if (contTexto == 0)
								 {
									str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroL.V_01") + "\n");
									 contTexto++;
								 }
								 str.append(  colector_bean.getProvincia() + colector_bean.getMunicipio() + colector_bean.getOrden_cole()+ "\t");
							}
						  
                      }
					  else{
						  str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroL.falsoerror.V_2") + "\n");
					  }
						  
				}
				else{
					 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroL.falsoerror.V_1") + "\n");
				}
			
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadroL.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
