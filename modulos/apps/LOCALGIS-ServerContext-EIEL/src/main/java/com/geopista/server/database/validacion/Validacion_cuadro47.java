/**
 * Validacion_cuadro47.java
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
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_alumbrado_bean;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_m50_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_emisario_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_7_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_ramal_saneamiento_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_sanea_autonomo_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro47 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro47.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro47") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_ALUMBRADO order by provincia,municipio,entidad,nucleo";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_1";
			String sql_2 = "select * from v_NUCL_ENCUESTADO_7";
		       
		
			ArrayList lstalumbrado = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstnucl_encuestado_7 = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			String provincia=null;
			String municipio=null;
			String entidad=null;
			String nucleo=null;
			while (rs.next()) {
						
					V_alumbrado_bean alumbrado_bean = new V_alumbrado_bean();
					
					alumbrado_bean.setProvincia(rs.getString("provincia"));
					provincia=rs.getString("provincia");
					alumbrado_bean.setMunicipio(rs.getString("municipio"));
					municipio=rs.getString("municipio");
					alumbrado_bean.setEntidad(rs.getString("entidad"));
					entidad=rs.getString("entidad");
					alumbrado_bean.setNucleo(rs.getString("nucleo"));
					nucleo=rs.getString("nucleo");
					alumbrado_bean.setAh_ener_rl(rs.getString("ah_ener_rl"));
					alumbrado_bean.setAh_ener_ri(rs.getString("ah_ener_ri"));
					alumbrado_bean.setCalidad(rs.getString("calidad"));
					if(rs.getString("pot_instal")!=null&&!rs.getString("pot_instal").equals(""))
						alumbrado_bean.setPot_instal(Double.parseDouble(rs.getString("pot_instal") ));
					else
						alumbrado_bean.setPot_instal(0.0);
					alumbrado_bean.setPuntos_luz(rs.getInt("n_puntos"));
					
					lstalumbrado.add(alumbrado_bean);
	
			}

	
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_1_bean  nucl_encuestado_1_bean = new V_nucl_encuestado_1_bean();

				nucl_encuestado_1_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_1_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_1_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_1_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_1_bean.setPadron(new Integer(rs.getString("PADRON")));
				nucl_encuestado_1_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
				
				
				if(rs.getString("ALTITUD")!=null&&!rs.getString("ALTITUD").equals(""))				
					nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				else
					nucl_encuestado_1_bean.setAltitud(0);
				
				//nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				
				if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))
					nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				else
					nucl_encuestado_1_bean.setHoteles(0);
				//nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))
					nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				else
					nucl_encuestado_1_bean.setCasas_rural(0);
				//nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));

				
				lstnucl_encuestado_1.add(nucl_encuestado_1_bean);
	
			}
			

			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_7_bean  nucl_encuestado_7_bean = new V_nucl_encuestado_7_bean();

				nucl_encuestado_7_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_7_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_7_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_7_bean.setNucleo(rs.getString("POBLAMIENT"));
				nucl_encuestado_7_bean.setTv_ant(rs.getString("TV_ANT"));
				nucl_encuestado_7_bean.setTv_ca(rs.getString("TV_CA"));
				nucl_encuestado_7_bean.setTm_gsm(rs.getString("TM_GSM"));
				nucl_encuestado_7_bean.setTm_umts(rs.getString("TM_UMTS"));
				nucl_encuestado_7_bean.setTm_gprs(rs.getString("TM_GPRS"));
				nucl_encuestado_7_bean.setCorreo(rs.getString("CORREO"));
				nucl_encuestado_7_bean.setBa_rd(rs.getString("BA_RD"));
				nucl_encuestado_7_bean.setBa_xd(rs.getString("BA_XD"));
				nucl_encuestado_7_bean.setBa_wi(rs.getString("BA_WI"));
				nucl_encuestado_7_bean.setBa_ca(rs.getString("BA_CA"));
				nucl_encuestado_7_bean.setBa_rb(rs.getString("BA_RB"));
				nucl_encuestado_7_bean.setBa_st(rs.getString("BA_ST"));
				nucl_encuestado_7_bean.setCapi(rs.getString("CAPI"));
				nucl_encuestado_7_bean.setElectricid(rs.getString("ELECTRICID"));
				nucl_encuestado_7_bean.setGas(rs.getString("GAS"));
				nucl_encuestado_7_bean.setAlu_v_sin(new Integer(rs.getString("ALU_V_SIN")));
				nucl_encuestado_7_bean.setAlu_l_sin(new Integer(rs.getString("ALU_L_SIN")));
				lstnucl_encuestado_7.add(nucl_encuestado_7_bean);
	
			}
			
		
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)  
				for (int i = 0; i < lstalumbrado.size(); i++)
	            {
					V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(i);
	
					if(alumbrado_bean.getPot_instal() == 0 && !alumbrado_bean.getCalidad().equals("E"))
					{
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( alumbrado_bean.getProvincia() + alumbrado_bean.getMunicipio() + 
								 alumbrado_bean.getEntidad()  + alumbrado_bean.getNucleo()+"\t");
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
				for (int i = 0; i < lstalumbrado.size(); i++)
	            {
					V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(i);
	
					if(alumbrado_bean.getPuntos_luz() == 0 && !alumbrado_bean.getCalidad().equals("E")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( alumbrado_bean.getProvincia() + alumbrado_bean.getMunicipio() + 
								 alumbrado_bean.getEntidad()  + alumbrado_bean.getNucleo()+"\t");
						 error = true;
	
					}
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03)  
				for (int i = 0; i < lstalumbrado.size(); i++)
	            {
					V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(i);
	
					if(alumbrado_bean.getPuntos_luz() > 0 && alumbrado_bean.getCalidad().equals("E")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( alumbrado_bean.getProvincia() + alumbrado_bean.getMunicipio() + 
								 alumbrado_bean.getEntidad()  + alumbrado_bean.getNucleo()+"\t");
						 error = true;
					}
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04) 
				for (int i = 0; i < lstalumbrado.size(); i++)
	            {
					V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(i);
	
					if(alumbrado_bean.getPot_instal() <= 0 && !alumbrado_bean.getCalidad().equals("E")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( alumbrado_bean.getProvincia() + alumbrado_bean.getMunicipio() + 
								 alumbrado_bean.getEntidad()  + alumbrado_bean.getNucleo()+"\t");
						 error = true;
	
					}
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05) 
				for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i);
					int count = 0;
					V_nucl_encuestado_7_bean nucl_encuestado_7_encontrado = null;
					for (int j = 0; j < lstalumbrado.size(); j++)
		            {
						V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(j);
						if( alumbrado_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) &&
								alumbrado_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) &&
								alumbrado_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								alumbrado_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							count ++;
							
						}
		            }
	
					for (int j = 0; j < lstnucl_encuestado_7.size(); j++)
		            {
						V_nucl_encuestado_7_bean nucl_encuestado_7_bean  = (V_nucl_encuestado_7_bean)lstnucl_encuestado_7.get(j);
						if( nucl_encuestado_7_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) &&
								nucl_encuestado_7_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) &&
								nucl_encuestado_7_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								nucl_encuestado_7_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							
							nucl_encuestado_7_encontrado = nucl_encuestado_7_bean;
						}
		            }
					
					if(count == 0){
						if (nucl_encuestado_7_encontrado==null ||(nucl_encuestado_1_bean.getViv_total() != nucl_encuestado_7_encontrado.getAlu_v_sin())){
							if (contTexto == 0){
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_05") + "\n");
								 str.append("\n");
								 contTexto++;
							}
								 str.append( nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() + 
										 nucl_encuestado_1_bean.getEntidad()  + nucl_encuestado_1_bean.getNucleo()+"\t");
								 error = true;
							}
					}	
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}	
			if(lstValCuadros.contains("v06")){
			
				//ERROR DEL MPT -> (V_06) 
				for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i);
					int count = 0;
					V_nucl_encuestado_7_bean nucl_encuestado_7_encontrado = null;
					for (int j = 0; j < lstalumbrado.size(); j++)
		            {
						V_alumbrado_bean alumbrado_bean   = (V_alumbrado_bean)lstalumbrado.get(j);
						if( alumbrado_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) &&
								alumbrado_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) &&
								alumbrado_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								alumbrado_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							count ++;
							
						}
		            }
	
					for (int j = 0; j < lstnucl_encuestado_7.size(); j++)
		            {
						V_nucl_encuestado_7_bean nucl_encuestado_7_bean  = (V_nucl_encuestado_7_bean)lstnucl_encuestado_7.get(j);
						if( nucl_encuestado_7_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) &&
								nucl_encuestado_7_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) &&
								nucl_encuestado_7_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								nucl_encuestado_7_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							
							nucl_encuestado_7_encontrado = nucl_encuestado_7_bean;
						}
		            }
					
					if(count == 0){
						if(nucl_encuestado_7_encontrado!=null && nucl_encuestado_7_encontrado.getAlu_l_sin() <= 0){
							if (contTexto == 0){
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro47.V_06") + "\n");
								 str.append("\n");
								 contTexto++;
							}
								 str.append( nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() + 
										 nucl_encuestado_1_bean.getEntidad()  + nucl_encuestado_1_bean.getNucleo()+"\t");
							}
					}	
				}
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro47.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
