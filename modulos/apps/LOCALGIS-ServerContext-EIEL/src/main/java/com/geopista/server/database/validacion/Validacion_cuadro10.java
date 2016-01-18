/**
 * Validacion_cuadro10.java
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

public class Validacion_cuadro10 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro10.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro10") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CONDUCCION_ENC";
			String sql_sin_nucleo = "select * from v_conduccion_enc_sin_nucleo";
			String sql_1 = "select * from v_COND_AGUA_NUCLEO";
			String sql_2 = "select * from v_TRAMO_CONDUCCION";
			
			ArrayList lstcond_conduccion_enc = new ArrayList();
			ArrayList lstcond_conduccion_enc_sin_nucleo = new ArrayList();
			ArrayList lstcond_cond_agua_nucleo = new ArrayList();
			ArrayList lsttramo_conduccion = new ArrayList();

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_conduccion_enc_bean conduccion_enc_bean = new V_conduccion_enc_bean();
				conduccion_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				conduccion_enc_bean.setClave(rs.getString("CLAVE"));

				lstcond_conduccion_enc.add(conduccion_enc_bean);
			}
			ps = connection.prepareStatement(sql_sin_nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_conduccion_enc_bean conduccion_enc_bean = new V_conduccion_enc_bean();
				conduccion_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				conduccion_enc_bean.setClave(rs.getString("CLAVE"));
				lstcond_conduccion_enc_sin_nucleo.add(conduccion_enc_bean);
				
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cond_agua_nucleo_bean cond_agua_nucleo_bean = new V_cond_agua_nucleo_bean();

				cond_agua_nucleo_bean.setProvincia(rs.getString("provincia"));
				cond_agua_nucleo_bean.setMunicipio(rs.getString("municipio"));
				cond_agua_nucleo_bean.setEntidad(rs.getString("entidad"));
				cond_agua_nucleo_bean.setNucleo(rs.getString("nucleo"));
				cond_agua_nucleo_bean.setClave(rs.getString("clave"));
				cond_agua_nucleo_bean.setCond_provi(rs.getString("cond_provi"));
				cond_agua_nucleo_bean.setCond_munic(rs.getString("cond_munic"));
				cond_agua_nucleo_bean.setOrden_cond(rs.getString("orden_cond"));

				lstcond_cond_agua_nucleo.add(cond_agua_nucleo_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_conduccion_bean tramo_conduccion_bean = new V_tramo_conduccion_bean();

				tramo_conduccion_bean.setClave(rs.getString("CLAVE"));
				tramo_conduccion_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_conduccion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_conduccion_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				tramo_conduccion_bean.setTipo_tcond(rs.getString("TIPO_TCOND"));
				tramo_conduccion_bean.setEstado(rs.getString("ESTADO"));
				tramo_conduccion_bean.setTitular(rs.getString("TITULAR"));
				tramo_conduccion_bean.setGestion(rs.getString("GESTION"));				
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					tramo_conduccion_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					tramo_conduccion_bean.setLongitud(0);
		
				lsttramo_conduccion.add(tramo_conduccion_bean);
				
			}
						
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstcond_conduccion_enc.size(); i++)
	            {
					V_conduccion_enc_bean conduccion_enc_bean = (V_conduccion_enc_bean)lstcond_conduccion_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstcond_cond_agua_nucleo.size(); j++)
		            {
						V_cond_agua_nucleo_bean cond_agua_nucleo_bean = (V_cond_agua_nucleo_bean)lstcond_cond_agua_nucleo.get(j);
						if( cond_agua_nucleo_bean.getCond_provi().equals(conduccion_enc_bean.getProvincia()) &&
								cond_agua_nucleo_bean.getCond_munic().equals(conduccion_enc_bean.getMunicipio()) &&
								cond_agua_nucleo_bean.getOrden_cond().equals(conduccion_enc_bean.getOrden_cond())){
							
							count ++;
						}
		            }
					
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro10.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(conduccion_enc_bean.getClave() + conduccion_enc_bean.getProvincia()+ 
								 conduccion_enc_bean.getMunicipio() + conduccion_enc_bean.getOrden_cond() +"\t");
						error = true;
					}
	
	            }
				for (int i = 0; i < lstcond_conduccion_enc_sin_nucleo.size(); i++)
	            {
					V_conduccion_enc_bean conduccion_enc_bean = (V_conduccion_enc_bean)lstcond_conduccion_enc_sin_nucleo.get(i);
					int count = 0;									
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro10.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(conduccion_enc_bean.getClave() + conduccion_enc_bean.getProvincia()+ 
								 conduccion_enc_bean.getMunicipio() + conduccion_enc_bean.getOrden_cond() +"\t");
						error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v02")){

				//ERROR DEL MPT -> (V_02)
				for (int i = 0; i < lstcond_conduccion_enc.size(); i++)
	            {
					V_conduccion_enc_bean conduccion_enc_bean = (V_conduccion_enc_bean)lstcond_conduccion_enc.get(i);
					int count = 0;
					for (int j = 0; j < lsttramo_conduccion.size(); j++)
		            {
						V_tramo_conduccion_bean tramo_conduccion_bean  = (V_tramo_conduccion_bean)lsttramo_conduccion.get(j);
						if( tramo_conduccion_bean.getProvincia().equals(conduccion_enc_bean.getProvincia()) &&
								tramo_conduccion_bean.getMunicipio().equals(conduccion_enc_bean.getMunicipio()) &&
								tramo_conduccion_bean.getOrden_cond().equals(conduccion_enc_bean.getOrden_cond())){
	
							count ++;
						}
		            }
					
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro10.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(conduccion_enc_bean.getClave() + conduccion_enc_bean.getProvincia()+ 
								 conduccion_enc_bean.getMunicipio() + conduccion_enc_bean.getOrden_cond() +"\t");
	
					}
	
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro10.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
