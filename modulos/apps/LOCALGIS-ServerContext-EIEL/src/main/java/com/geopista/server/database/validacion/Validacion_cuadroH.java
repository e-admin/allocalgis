/**
 * Validacion_cuadroH.java
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
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_agua_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;

public class Validacion_cuadroH {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroH.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroH") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;

	        
	        String sql = "select * from  v_CAPTACION_AGUA";
	        String sql_1 = "SELECT * FROM  v_CAP_AGUA_NUCLEO";
	        String sql_2 = "SELECT * FROM v_CAPTACION_ENC";
	        String sql_3 = "SELECT * FROM v_CAPTACION_ENC_M50";

			
	        ArrayList lstcaptacion_agua = new ArrayList();
	    	ArrayList lstcap_agua_nucleo = new ArrayList();
			ArrayList lstcaptacion_enc = new ArrayList();
			ArrayList lstcaptacion_enc_m50 = new ArrayList();
			
			
			 ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_captacion_agua_bean captacion_aguaBean = new V_captacion_agua_bean();

				captacion_aguaBean.setClave(rs.getString("clave"));
				captacion_aguaBean.setProvincia(rs.getString("provincia"));
				captacion_aguaBean.setMunicipio(rs.getString("municipio"));
				captacion_aguaBean.setOrden_capt(rs.getString("orden_capt"));

				lstcaptacion_agua.add(captacion_aguaBean);
				
			}
				
	        ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	

				V_cap_agua_nucleo_bean cap_agua_nucleoBean = new V_cap_agua_nucleo_bean();

				cap_agua_nucleoBean.setProvincia(rs.getString("provincia"));
				cap_agua_nucleoBean.setMunicipio(rs.getString("municipio"));
				cap_agua_nucleoBean.setEntidad(rs.getString("entidad"));
				cap_agua_nucleoBean.setNucleo(rs.getString("nucleo"));
				cap_agua_nucleoBean.setClave(rs.getString("clave"));
				cap_agua_nucleoBean.setC_provinc(rs.getString("c_provinc"));
				cap_agua_nucleoBean.setC_municip(rs.getString("c_municip"));
				cap_agua_nucleoBean.setOrden_capt(rs.getString("orden_capt"));

				lstcap_agua_nucleo.add(cap_agua_nucleoBean);
				
			}
	        
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_captacion_enc_bean captacion_encBean = new V_captacion_enc_bean();

				captacion_encBean.setProvincia(rs.getString("provincia"));
				captacion_encBean.setMunicipio(rs.getString("municipio"));
				captacion_encBean.setClave(rs.getString("clave"));
				captacion_encBean.setOrden_capt(rs.getString("orden_capt"));
				captacion_encBean.setDenominaci(rs.getString("denominaci"));
				captacion_encBean.setTipo_capt(rs.getString("tipo_capt"));
				captacion_encBean.setTitular(rs.getString("titular"));
				captacion_encBean.setGestion(rs.getString("gestion"));
				captacion_encBean.setSistema_ca(rs.getString("sistema_ca"));
				captacion_encBean.setEstado(rs.getString("estado"));
				captacion_encBean.setUso(rs.getString("uso"));
				captacion_encBean.setProteccion(rs.getString("proteccion"));
				captacion_encBean.setContador(rs.getString("contador"));
				lstcaptacion_enc.add(captacion_encBean);
				
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_captacion_enc_m50_bean captacion_enc_m50_bean = new V_captacion_enc_m50_bean();

				captacion_enc_m50_bean.setProvincia(rs.getString("provincia"));
				captacion_enc_m50_bean.setMunicipio(rs.getString("municipio"));
				captacion_enc_m50_bean.setClave(rs.getString("clave"));
				captacion_enc_m50_bean.setOrden_capt(rs.getString("orden_capt"));
				captacion_enc_m50_bean.setDenominaci(rs.getString("denominaci"));
				captacion_enc_m50_bean.setTipo_capt(rs.getString("tipo_capt"));
				captacion_enc_m50_bean.setTitular(rs.getString("titular"));
				captacion_enc_m50_bean.setGestion(rs.getString("gestion"));
				captacion_enc_m50_bean.setSistema_ca(rs.getString("sistema_ca"));
				captacion_enc_m50_bean.setEstado(rs.getString("estado"));
				captacion_enc_m50_bean.setUso(rs.getString("uso"));
				captacion_enc_m50_bean.setProteccion(rs.getString("proteccion"));
				captacion_enc_m50_bean.setContador(rs.getString("contador"));
				lstcaptacion_enc_m50.add(captacion_enc_m50_bean);
				
			}
	        
			
			for (int i = 0; i < lstcaptacion_agua.size(); i++)
            {
				V_captacion_agua_bean captacion_agua_bean   = (V_captacion_agua_bean)lstcaptacion_agua.get(i);
				int contador = 0;
				 
				for (int j = 0; j < lstcap_agua_nucleo.size(); j++)
	            {
					V_cap_agua_nucleo_bean cap_agua_nucleo_bean    = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(j);
					if(cap_agua_nucleo_bean.getProvincia().equals(captacion_agua_bean.getProvincia()) &&
							cap_agua_nucleo_bean.getMunicipio().equals(captacion_agua_bean.getMunicipio())){
						if(cap_agua_nucleo_bean.getOrden_capt().equals(captacion_agua_bean.getOrden_capt()) &&
								cap_agua_nucleo_bean.getClave().equals(captacion_agua_bean.getClave())){
							contador ++;
						}
						
					}
	            }
				
				if (contador == 0){
					for (int j = 0; j < lstcaptacion_enc.size(); j++)
		            {
						V_captacion_enc_bean captacion_enc_bean   = (V_captacion_enc_bean)lstcaptacion_enc.get(j);
						if(captacion_enc_bean.getProvincia().equals(captacion_agua_bean.getProvincia()) &&
								captacion_enc_bean.getMunicipio().equals(captacion_agua_bean.getMunicipio())){
							if(captacion_enc_bean.getOrden_capt().equals(captacion_agua_bean.getOrden_capt()) &&
									captacion_enc_bean.getClave().equals(captacion_agua_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}
				
				if (contador == 0){
					for (int j = 0; j < lstcaptacion_enc_m50.size(); j++)
		            {
						V_captacion_enc_m50_bean captacion_enc_m50_bean   = (V_captacion_enc_m50_bean)lstcaptacion_enc_m50.get(j);
						if(captacion_enc_m50_bean.getProvincia().equals(captacion_agua_bean.getProvincia()) &&
								captacion_enc_m50_bean.getMunicipio().equals(captacion_agua_bean.getMunicipio())){
							if(captacion_enc_m50_bean.getOrden_capt().equals(captacion_agua_bean.getOrden_capt()) &&
									captacion_enc_m50_bean.getClave().equals(captacion_agua_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}	
				

				if (contador == 0){
					if (contTexto == 0)
					 {
						str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroH.V_01") + "\n");
						 contTexto++;
					 }
					 str.append(  captacion_agua_bean.getProvincia() + captacion_agua_bean.getMunicipio() + captacion_agua_bean.getOrden_capt()+ "\t");
				}
						  
                    
				
			
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadroH.class +" " +  e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
