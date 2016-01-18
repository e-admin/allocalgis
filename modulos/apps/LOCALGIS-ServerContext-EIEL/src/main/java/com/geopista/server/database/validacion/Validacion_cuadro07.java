/**
 * Validacion_cuadro07.java
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

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro07 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro07.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro07") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CAPTACION_ENC";
			String sql_sin_nucleo = "select * from v_captacion_enc_sin_nucleo";
			String sql_1 = "select * from v_CAP_AGUA_NUCLEO";
			
			ArrayList lstcaptacion_enc = new ArrayList();
			ArrayList lstcaptacion_enc_sin_nucleo = new ArrayList();
			ps = connection.prepareStatement(sql);
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
			ps = connection.prepareStatement(sql_sin_nucleo);
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
				lstcaptacion_enc_sin_nucleo.add(captacion_encBean);
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			ArrayList lstcapt_agua_nucleo = new ArrayList();
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
				
				lstcapt_agua_nucleo.add(cap_agua_nucleoBean);
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstcaptacion_enc.size(); i++)
	            {
					V_captacion_enc_bean captacion_enc = (V_captacion_enc_bean)lstcaptacion_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstcapt_agua_nucleo.size(); j++)
		            {
						if( ((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getC_provinc().equals(captacion_enc.getProvincia()) &&
								((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getC_municip().equals(captacion_enc.getMunicipio()) &&
								((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getOrden_capt().equals(captacion_enc.getOrden_capt())){
							count ++;
						}
						
		            }
					
					if (count == 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro07.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(captacion_enc.getClave() + captacion_enc.getProvincia()+ 
								 captacion_enc.getMunicipio() + captacion_enc.getOrden_capt() +"\t");
					}
					
					
	            }
				for (int i = 0; i < lstcaptacion_enc_sin_nucleo.size(); i++)
	            {
					V_captacion_enc_bean captacion_enc = (V_captacion_enc_bean)lstcaptacion_enc_sin_nucleo.get(i);
					int count = 0;						
					if (count == 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro07.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(captacion_enc.getClave() + captacion_enc.getProvincia()+ 
								 captacion_enc.getMunicipio() + captacion_enc.getOrden_capt() +"\t");
					}
	            }
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro07.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
