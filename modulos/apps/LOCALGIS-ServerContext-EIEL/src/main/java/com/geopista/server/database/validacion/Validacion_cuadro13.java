/**
 * Validacion_cuadro13.java
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
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;

public class Validacion_cuadro13 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro13.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro13") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql= "select DISTINCT PROVINCIA, MUNICIPIO, ORDEN_COND from v_TRAMO_CONDUCCION_M50";
			String sql_v04 = "select * from v_TRAMO_CONDUCCION_M50 where LONGITUD = 0 and ESTADO <> 'E'";
			String sql_1 = "select * from v_CONDUCCION_ENC_M50";
			

			ArrayList lsttramo_conduccion_m50 = new ArrayList();
			ArrayList lsttramo_conduccion_m50_v04 = new ArrayList();
			ArrayList lstconduccion_enc_m50 = new ArrayList();
			
			ps = connection.prepareStatement(sql_v04);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_tramo_conduccion_m50_bean tramo_conduccion_m50_bean = new V_tramo_conduccion_m50_bean();

				tramo_conduccion_m50_bean.setClave(rs.getString("CLAVE"));
				tramo_conduccion_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_conduccion_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_conduccion_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				tramo_conduccion_m50_bean.setTipo_tcond(rs.getString("TIPO_TCOND"));
				tramo_conduccion_m50_bean.setEstado(rs.getString("ESTADO"));
				tramo_conduccion_m50_bean.setTitular(rs.getString("TITULAR"));
				tramo_conduccion_m50_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					tramo_conduccion_m50_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					tramo_conduccion_m50_bean.setLongitud(0);
	
				lsttramo_conduccion_m50_v04.add(tramo_conduccion_m50_bean);
				
			}
			
		
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_tramo_conduccion_m50_bean tramo_conduccion_m50_bean = new V_tramo_conduccion_m50_bean();

				tramo_conduccion_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_conduccion_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_conduccion_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));
	
				lsttramo_conduccion_m50.add(tramo_conduccion_m50_bean);
				
			}

			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_conduccion_enc_m50_bean conduccion_enc_m50_bean = new V_conduccion_enc_m50_bean();
				
				conduccion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				conduccion_enc_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));	
	
				lstconduccion_enc_m50.add(conduccion_enc_m50_bean);
				
			}
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lsttramo_conduccion_m50.size(); i++)
	            {
					int count = 0;
					V_tramo_conduccion_m50_bean  tramo_conduccion_m50_bean = (V_tramo_conduccion_m50_bean)lsttramo_conduccion_m50.get(i);
					for (int j = 0; j < lstconduccion_enc_m50.size(); j++)
		            {
						V_conduccion_enc_m50_bean conduccion_enc_m50_bean = (V_conduccion_enc_m50_bean)lstconduccion_enc_m50.get(j);
						
						if( conduccion_enc_m50_bean.getProvincia().equals(conduccion_enc_m50_bean.getProvincia()) &&
								conduccion_enc_m50_bean.getMunicipio().equals(conduccion_enc_m50_bean.getMunicipio()) &&
								conduccion_enc_m50_bean.getOrden_cond().equals(conduccion_enc_m50_bean.getOrden_cond())){
							
							count ++;
						}
		            }
					
					if (count == 0){
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro13.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(//tramo_conduccion_m50_bean.getClave() +
								 tramo_conduccion_m50_bean.getProvincia()+ 
								 tramo_conduccion_m50_bean.getMunicipio() + tramo_conduccion_m50_bean.getOrden_cond() +"\t");
	
					}
				}
	
				str.append("\n\n");
				contTexto = 0;
			}
			if(lstValCuadros.contains("v02")){
				//ERROR DEL MPT -> (V_02)
				for (int i = 0; i < lsttramo_conduccion_m50_v04.size(); i++)
	            {
					V_tramo_conduccion_m50_bean  tramo_conduccion_m50_bean = (V_tramo_conduccion_m50_bean)lsttramo_conduccion_m50_v04.get(i);
					
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro13.V_01") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(tramo_conduccion_m50_bean.getClave() + tramo_conduccion_m50_bean.getProvincia()+ 
							 tramo_conduccion_m50_bean.getMunicipio() + tramo_conduccion_m50_bean.getOrden_cond() +"\t");
	
	            }
			}
			str.append("\n\n");
				
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro13.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
