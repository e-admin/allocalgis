/**
 * Validacion_cuadroM.java
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
import com.geopista.server.database.validacion.beans.V_emisario_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_emisario_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;

public class Validacion_cuadroM {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroM.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroM") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;
			
			String sql = "select * from  v_EMISARIO";
			String sql_1 = "SELECT * FROM  v_EMISARIO_NUCLEO";
			String sql_2 = "SELECT * FROM  v_EMISARIO_ENC";
			String sql_3 = "SELECT * FROM  v_EMISARIO_ENC_M50";

			
	        ArrayList lstemisario = new ArrayList();
	    	ArrayList lstemisario_nucleo = new ArrayList();
			ArrayList lstemisario_enc = new ArrayList();
			ArrayList lstemisario_enc_m50 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_emisario_bean emisario_bean = new V_emisario_bean();
				emisario_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_bean.setClave(rs.getString("CLAVE"));
				emisario_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				lstemisario.add(emisario_bean);
				
			}
				
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	

				V_emisario_nucleo_bean emisario_nucleo_bean = new V_emisario_nucleo_bean();


				emisario_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				emisario_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				emisario_nucleo_bean.setClave(rs.getString("CLAVE"));
				emisario_nucleo_bean.setEm_provinc(rs.getString("EM_PROVINC"));
				emisario_nucleo_bean.setEm_municip(rs.getString("EM_MUNICIP"));
				emisario_nucleo_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));

				lstemisario_nucleo.add(emisario_nucleo_bean);
				
			}
	        
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_emisario_enc_bean emisario_enc_bean = new V_emisario_enc_bean();

				emisario_enc_bean.setClave(rs.getString("CLAVE"));
				emisario_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_enc_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				emisario_enc_bean.setTipo_vert(rs.getString("TIPO_VERT"));
				emisario_enc_bean.setZona_vert(rs.getString("ZONA_VERT"));
				if(rs.getString("DISTANCIA")!=null && !rs.getString("DISTANCIA").equals("") )
					emisario_enc_bean.setDistancia(new Integer(rs.getString("DISTANCIA")));
				else
					emisario_enc_bean.setDistancia(0);

				lstemisario_enc.add(emisario_enc_bean);
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_emisario_enc_m50_bean emisario_enc_m50_bean = new V_emisario_enc_m50_bean();

				emisario_enc_m50_bean.setClave(rs.getString("CLAVE"));
				emisario_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_enc_m50_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				emisario_enc_m50_bean.setTipo_vert(rs.getString("TIPO_VERT"));
				emisario_enc_m50_bean.setZona_vert(rs.getString("ZONA_VERT"));
				if(rs.getString("DISTANCIA")!=null && !rs.getString("DISTANCIA").equals("") )
					emisario_enc_m50_bean.setDistancia(new Integer(rs.getString("DISTANCIA")));
				else
					emisario_enc_m50_bean.setDistancia(0);
				
				lstemisario_enc_m50.add(emisario_enc_m50_bean);
			}
	        
			
			for (int i = 0; i < lstemisario.size(); i++)
            {
				V_emisario_bean emisario_bean   = (V_emisario_bean)lstemisario.get(i);
				int contador = 0;
				if(!emisario_bean.getProvincia().equals("") || !emisario_bean.getMunicipio().equals("")){
					  if (!emisario_bean.getOrden_emis().equals(""))
                      {
							for (int j = 0; j < lstemisario_nucleo.size(); j++)
				            {
								V_emisario_nucleo_bean emisario_nucleo_bean   = (V_emisario_nucleo_bean)lstemisario_nucleo.get(j);
								if(emisario_nucleo_bean.getProvincia().equals(emisario_bean.getProvincia()) &&
										emisario_nucleo_bean.getMunicipio().equals(emisario_bean.getMunicipio())){
									if(emisario_nucleo_bean.getOrden_emis().equals(emisario_bean.getOrden_emis()) &&
											emisario_nucleo_bean.getClave().equals(emisario_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							
							if (contador == 0){
								for (int j = 0; j < lstemisario_enc.size(); j++)
					            {
									V_emisario_enc_bean emisario_enc_bean   = (V_emisario_enc_bean)lstemisario_enc.get(j);
									if(emisario_enc_bean.getProvincia().equals(emisario_bean.getProvincia()) &&
											emisario_enc_bean.getMunicipio().equals(emisario_bean.getMunicipio())){
										if(emisario_enc_bean.getOrden_emis().equals(emisario_bean.getOrden_emis()) &&
												emisario_enc_bean.getClave().equals(emisario_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}
							
							if (contador == 0){
								for (int j = 0; j < lstemisario_enc_m50.size(); j++)
					            {
									V_emisario_enc_m50_bean emisario_enc_m50_bean   = (V_emisario_enc_m50_bean)lstemisario_enc_m50.get(j);
									if(emisario_enc_m50_bean.getProvincia().equals(emisario_bean.getProvincia()) &&
											emisario_enc_m50_bean.getMunicipio().equals(emisario_bean.getMunicipio())){
										if(emisario_enc_m50_bean.getOrden_emis().equals(emisario_bean.getOrden_emis()) &&
												emisario_enc_m50_bean.getClave().equals(emisario_bean.getClave())){
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
								 str.append(  emisario_bean.getProvincia() + emisario_bean.getMunicipio() + emisario_bean.getOrden_emis()+ "\t");
							}
						  
                      }
					  else{
						  str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroM.falsoerror.V_2") + "\n");
					  }
						  
				}
				else{
					 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroM.falsoerror.V_1") + "\n");
	
				}
			
            }
			str.append("\n\n");
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadroM.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
