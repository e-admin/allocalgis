/**
 * Validacion_cuadro35.java
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

public class Validacion_cuadro35 {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro35.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {     
		        
			str.append(Messages.getString("cuadro35") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_NUCL_ENCUESTADO_5";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_1";
	        String sql_2 = "select * from v_SANEA_AUTONOMO";  //cuadro 41
	        String sql_3 = "select * from v_RAMAL_SANEAMIENTO";//cuadro 24
	        String sql_4 = "select * from v_COLECTOR_NUCLEO";
	        String sql_5 = "select * from v_TRAMO_COLECTOR";
	        String sql_6 = "select * from v_DEP_AGUA_NUCLEO";
	        String sql_7 = "select * from v_DEPURADORA_ENC_2";
	        String sql_8 = "select * from v_INFRAESTR_VIARIA"; 

			

			ArrayList lstnucl_encuestado_5 = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstsanea_autonomo = new ArrayList();
			ArrayList lstramal_saneamiento = new ArrayList();
			ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lsttramo_colector = new ArrayList();
			ArrayList lstdep_agua_nucleo = new ArrayList();
			ArrayList lstdepuradora_enc_2 = new ArrayList();
			ArrayList lstinfraestr_viaria = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_5_bean  nucl_encuestado_5_bean = new V_nucl_encuestado_5_bean();

				nucl_encuestado_5_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_5_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_5_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_5_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_5_bean.setSyd_pozos(rs.getString("SYD_POZOS"));
				nucl_encuestado_5_bean.setSyd_sumide(rs.getString("SYD_SUMIDE"));
				nucl_encuestado_5_bean.setSyd_ali_co(rs.getString("SYD_ALI_CO"));
				nucl_encuestado_5_bean.setSyd_ali_si(rs.getString("SYD_ALI_SI"));
				nucl_encuestado_5_bean.setSyd_calida(rs.getString("SYD_CALIDA"));		
				nucl_encuestado_5_bean.setSyd_v_cone(new Integer(rs.getString("SYD_V_CONE")));
				nucl_encuestado_5_bean.setSyd_v_ncon(new Integer(rs.getString("SYD_V_NCON")));
				nucl_encuestado_5_bean.setSyd_l_defi(new Integer(rs.getString("SYD_L_DEFI")));
				nucl_encuestado_5_bean.setSyd_v_defi(new Integer(rs.getString("SYD_V_DEFI")));
				nucl_encuestado_5_bean.setSyd_pr_def(new Integer(rs.getString("SYD_PR_DEF")));
				nucl_encuestado_5_bean.setSyd_pe_def(new Integer(rs.getString("SYD_PE_DEF")));
				nucl_encuestado_5_bean.setSyd_c_desa(new Integer(rs.getString("SYD_C_DESA")));
				if(rs.getString("SYD_C_TRAT")!=null&&!rs.getString("SYD_C_TRAT").equals(""))	
					nucl_encuestado_5_bean.setSyd_c_trat(new Integer(rs.getString("SYD_C_TRAT")));
				else
					nucl_encuestado_5_bean.setSyd_c_trat(0);
				nucl_encuestado_5_bean.setSyd_re_urb(new Integer(rs.getString("SYD_RE_URB")));
				nucl_encuestado_5_bean.setSyd_re_rus(new Integer(rs.getString("SYD_RE_RUS")));
				nucl_encuestado_5_bean.setSyd_re_ind(new Integer(rs.getString("SYD_RE_IND")));
				
				lstnucl_encuestado_5.add(nucl_encuestado_5_bean);
				
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
				//NULLnucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
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
				V_sanea_autonomo_bean  sanea_autonomo_bean = new V_sanea_autonomo_bean();

				sanea_autonomo_bean.setProvincia(rs.getString("PROVINCIA"));
				sanea_autonomo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				sanea_autonomo_bean.setEntidad(rs.getString("ENTIDAD"));
				sanea_autonomo_bean.setNucleo(rs.getString("NUCLEO"));
				sanea_autonomo_bean.setTipo_sanea(rs.getString("TIPO_SANEA"));
				sanea_autonomo_bean.setEstado(rs.getString("ESTADO"));
				sanea_autonomo_bean.setAdecuacion(rs.getString("ADECUACION"));
				sanea_autonomo_bean.setSau_vivien(new Integer(rs.getString("SAU_VIVIEN")));
				sanea_autonomo_bean.setSau_pob_re(new Integer(rs.getString("SAU_POB_RE")));
				sanea_autonomo_bean.setSau_pob_es(new Integer(rs.getString("SAU_POB_ES")));
				sanea_autonomo_bean.setSau_vi_def(new Integer(rs.getString("SAU_VI_DEF")));
				sanea_autonomo_bean.setSau_re_def(new Integer(rs.getString("SAU_RE_DEF")));
				sanea_autonomo_bean.setSau_es_def(new Integer(rs.getString("SAU_ES_DEF")));

				lstsanea_autonomo.add(sanea_autonomo_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_ramal_saneamiento_bean  ramal_saneamiento_bean = new V_ramal_saneamiento_bean();

				ramal_saneamiento_bean.setProvincia(rs.getString("PROVINCIA"));
				ramal_saneamiento_bean.setMunicipio(rs.getString("MUNICIPIO"));
				ramal_saneamiento_bean.setEntidad(rs.getString("ENTIDAD"));
				ramal_saneamiento_bean.setNucleo(rs.getString("NUCLEO"));
				ramal_saneamiento_bean.setTipo_rama(rs.getString("TIPO_RAMA"));
				ramal_saneamiento_bean.setSist_trans(rs.getString("SIST_TRANS"));
				ramal_saneamiento_bean.setEstado(rs.getString("ESTADO"));
				ramal_saneamiento_bean.setTipo_red(rs.getString("TIPO_RED"));
				ramal_saneamiento_bean.setTitular(rs.getString("TITULAR"));
				ramal_saneamiento_bean.setGestion(rs.getString("GESTION"));
				ramal_saneamiento_bean.setLongit_ram(new Integer(new Double(rs.getString("LONGIT_RAM")).intValue()));

				
				lstramal_saneamiento.add(ramal_saneamiento_bean);
				
			}
			
			ps = connection.prepareStatement(sql_4);
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
			
			ps = connection.prepareStatement(sql_5);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_colector_bean tramo_colector_bean = new V_tramo_colector_bean();

				tramo_colector_bean.setClave(rs.getString("CLAVE"));
				tramo_colector_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_colector_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_colector_bean.setOrden_cole(rs.getString("ORDEN_COLE"));
				tramo_colector_bean.setTipo_colec(rs.getString("TIPO_COLEC"));
				tramo_colector_bean.setSist_trans(rs.getString("SIST_TRANS"));
				tramo_colector_bean.setEstado(rs.getString("ESTADO"));
				tramo_colector_bean.setTitular(rs.getString("TITULAR"));
				tramo_colector_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONG_TRAMO")!=null&&!rs.getString("LONG_TRAMO").equals(""))
					tramo_colector_bean.setLong_tramo(new Double(Math.rint(new Double(rs.getString("LONG_TRAMO")))).intValue());
				else
					tramo_colector_bean.setLong_tramo(0);
				lsttramo_colector.add(tramo_colector_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_6);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_dep_agua_nucleo_bean dep_agua_nucleo_bean = new V_dep_agua_nucleo_bean();
				dep_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				dep_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				dep_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				dep_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));		
				dep_agua_nucleo_bean.setClave(rs.getString("CLAVE"));		
				dep_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));		
				dep_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));		
				dep_agua_nucleo_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));	
				lstdep_agua_nucleo.add((dep_agua_nucleo_bean));
			}
			
			ps = connection.prepareStatement(sql_7);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_2_bean depuradora_enc_2_bean = new V_depuradora_enc_2_bean();

				depuradora_enc_2_bean.setClave(rs.getString("CLAVE"));
				depuradora_enc_2_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_2_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_2_bean.setTitular(rs.getString("TITULAR"));
				depuradora_enc_2_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					depuradora_enc_2_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					depuradora_enc_2_bean.setCapacidad(0);

				depuradora_enc_2_bean.setProblem_1(rs.getString("PROBLEM_1"));
				depuradora_enc_2_bean.setProblem_2(rs.getString("PROBLEM_2"));
				depuradora_enc_2_bean.setProblem_3(rs.getString("PROBLEM_3"));
				depuradora_enc_2_bean.setLodo_gest(rs.getString("LODO_GEST"));
				depuradora_enc_2_bean.setLodo_vert(new Integer(rs.getString("LODO_VERT")));
				depuradora_enc_2_bean.setLodo_inci(new Integer(rs.getString("LODO_INCI")));
				depuradora_enc_2_bean.setLodo_con_a(new Integer(rs.getString("LODO_CON_A")));
				depuradora_enc_2_bean.setLodo_sin_a(new Integer(rs.getString("LODO_SIN_A")));
				depuradora_enc_2_bean.setLodo_ot(new Integer(rs.getString("LODO_OT")));
				
				lstdepuradora_enc_2.add(depuradora_enc_2_bean);
			}
			
			
			ps = connection.prepareStatement(sql_8);
			rs = ps.executeQuery();
			while (rs.next()) {	
				    
				V_infraestr_viaria_bean infraestr_viaria_bean = new V_infraestr_viaria_bean();

				infraestr_viaria_bean.setProvincia(rs.getString("PROVINCIA"));
				infraestr_viaria_bean.setMunicipio(rs.getString("MUNICIPIO"));
				infraestr_viaria_bean.setEntidad(rs.getString("ENTIDAD"));
				infraestr_viaria_bean.setNucleo(rs.getString("POBLAMIENT"));
				infraestr_viaria_bean.setTipo_infr(rs.getString("TIPO_INFR"));
				infraestr_viaria_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					infraestr_viaria_bean.setLongitud(new Integer(rs.getString("LONGITUD")));
				else
					infraestr_viaria_bean.setLongitud(0);
				if(rs.getString("SUPERFICIE")!=null&&!rs.getString("SUPERFICIE").equals(""))
					infraestr_viaria_bean.setSuperficie(new Integer(rs.getString("SUPERFICIE")));
				else
					infraestr_viaria_bean.setSuperficie(0);
				
				
				if(rs.getString("VIV_AFECTA")!=null&&!rs.getString("VIV_AFECTA").equals(""))
					infraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));
				else
					infraestr_viaria_bean.setViv_afecta(0);
				//NULLinfraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));

				
				
				lstinfraestr_viaria.add(infraestr_viaria_bean);
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					int suma = nucl_encuestado_5_bean.getSyd_v_defi() + nucl_encuestado_5_bean.getSyd_v_cone() + 
						nucl_encuestado_5_bean.getSyd_v_ncon();
					int count = 0;
					int count1 = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							nuclEnc1encontrado = nuclEnc1;
							count ++;
						}
		            }
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							count1 ++;
						}
		            }
					
					
					if(count1 == 0){
						if(count != 0){
							if ( suma != nuclEnc1encontrado.getViv_total()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_01") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
										 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
								 error = true;
							}
						}
						
					}
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v02")){
			
				//ERROR DEL MPT -> (V_02) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					int suma = nucl_encuestado_5_bean.getSyd_v_defi() + nucl_encuestado_5_bean.getSyd_v_cone() + 
						nucl_encuestado_5_bean.getSyd_v_ncon();
					int count = 0;
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int total = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					for (int j = 0; j < lstramal_saneamiento.size(); j++)
		            {
						V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(j);
						if(ramal_saneamiento_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								ramal_saneamiento_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								ramal_saneamiento_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								ramal_saneamiento_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count ++;
						}
		            }
					
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								colector_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								colector_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								colector_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count1 ++;
						}
		            }
					
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							count2 ++;
							total +=  sanea_autonomo_bean.getSau_vi_def();
						}
		            }
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							count3 ++;
							nuclEnc1encontrado = nuclEnc1;
						}
		            }
								
					if(count == 0 && count1 ==0 && total == 0){	
						if(nucl_encuestado_5_bean.getSyd_pr_def() != nuclEnc1encontrado.getPadron()){
	
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_02") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						}
						
					}
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v03")){
			
				//ERROR DEL MPT -> (V_03) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					int suma = nucl_encuestado_5_bean.getSyd_v_defi() + nucl_encuestado_5_bean.getSyd_v_cone() + 
						nucl_encuestado_5_bean.getSyd_v_ncon();
					int count = 0;
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int total = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					for (int j = 0; j < lstramal_saneamiento.size(); j++)
		            {
						V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(j);
						if(ramal_saneamiento_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								ramal_saneamiento_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								ramal_saneamiento_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								ramal_saneamiento_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count ++;
						}
		            }
					
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								colector_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								colector_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								colector_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count1 ++;
						}
		            }
					
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							count2 ++;
							total +=  sanea_autonomo_bean.getSau_re_def();
						}
		            }
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							count3 ++;
							nuclEnc1encontrado = nuclEnc1;
						}
		            }
								
					if(count == 0 && count1 ==0 && total == 0){	
						if(nucl_encuestado_5_bean.getSyd_pr_def() != nuclEnc1encontrado.getPadron()){
	
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_03") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						}
						
					}
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04) 
				
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					int suma = nucl_encuestado_5_bean.getSyd_v_defi() + nucl_encuestado_5_bean.getSyd_v_cone() + 
						nucl_encuestado_5_bean.getSyd_v_ncon();
					int count = 0;
					int count1 = 0;
					int count2 = 0;
					int count3 = 0;
					int total = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					for (int j = 0; j < lstramal_saneamiento.size(); j++)
		            {
						V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(j);
						if(ramal_saneamiento_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								ramal_saneamiento_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								ramal_saneamiento_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								ramal_saneamiento_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count ++;
						}
		            }
					
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								colector_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								colector_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								colector_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count1 ++;
						}
		            }
					
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							count2 ++;
							total +=  sanea_autonomo_bean.getSau_es_def();
						}
		            }
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							count3 ++;
							nuclEnc1encontrado = nuclEnc1;
						}
		            }
								
					if(count == 0 && count1 ==0 && total == 0){	
						if(nucl_encuestado_5_bean.getSyd_pr_def() != nuclEnc1encontrado.getPob_estaci()){
	
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_04") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						}
						
					}
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int count = 0 ;
					int count1 = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if((nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon() ) > 0){
						
						for (int j = 0; j < lstramal_saneamiento.size(); j++)
			            {
							V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(j);
							if(ramal_saneamiento_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
									ramal_saneamiento_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
									ramal_saneamiento_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
									ramal_saneamiento_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
								count ++;
							}
			            }
						
						for (int j = 0; j <lstcolector_nucleo.size(); j++)
			            {
							V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
							if(colector_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
									colector_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
									colector_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
									colector_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
								count1 ++;
							}
			            }
						
						if(count == 0 && count1 == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_05") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
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
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0 ;
					int total_SAU_VI_DEF = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							total_SAU_VI_DEF  += sanea_autonomo_bean.getSau_vi_def();
						}
		            }
					total = nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon() + 
							nucl_encuestado_5_bean.getSyd_v_defi() +total_SAU_VI_DEF;
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							nuclEnc1encontrado = nuclEnc1;
						}
		            }
					if(total != nuclEnc1encontrado.getViv_total()){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_06") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
					
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0 ;
					int suma = 0;
					V_nucl_encuestado_1_bean nuclEnc1encontrado = null;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					
					total = nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon();
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							nuclEnc1encontrado = nuclEnc1;
						}
		            }
					if(total == nuclEnc1encontrado.getViv_total()){
						suma = nucl_encuestado_5_bean.getSyd_l_defi() + nucl_encuestado_5_bean.getSyd_v_defi() +
							nucl_encuestado_5_bean.getSyd_pr_def() + nucl_encuestado_5_bean.getSyd_pe_def();
						if(suma != 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_07") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						}
					}
					
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v08")){
				//ERROR DEL MPT -> (V_08) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0;
		            int total_SAU_VI_DEF = 0;
		            int total_SAU_RE_DEF = 0;
		            int total_SAU_ES_DEF = 0;
	
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
	
					total = nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nuclEnc1  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if(nuclEnc1.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								nuclEnc1.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								nuclEnc1.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								nuclEnc1.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							if(total == nuclEnc1.getViv_total()){
								
								for (int h = 0; h < lstsanea_autonomo.size(); h++)
					            {
									V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(h);
									if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
											sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
											sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
											sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
										
										total_SAU_VI_DEF += sanea_autonomo_bean.getSau_vi_def();
							            total_SAU_RE_DEF += sanea_autonomo_bean.getSau_re_def();
							            total_SAU_ES_DEF += sanea_autonomo_bean.getSau_es_def();
									}
					            }
								if((total_SAU_VI_DEF + total_SAU_RE_DEF + total_SAU_ES_DEF ) != 0){
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_08") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
											 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
									 error = true;
								}
							}
						}
		            }
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v09")){
				//ERROR DEL MPT -> (V_09) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							total += sanea_autonomo_bean.getSau_vi_def();
						}
		            }
					if(nucl_encuestado_5_bean.getSyd_v_defi() < 0 && nucl_encuestado_5_bean.getSyd_v_defi() == total){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_09") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v10")){
				//ERROR DEL MPT -> (V_10) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							total += sanea_autonomo_bean.getSau_re_def();
						}
		            }
					if(nucl_encuestado_5_bean.getSyd_pr_def() < 0 && nucl_encuestado_5_bean.getSyd_pr_def() == total){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_10") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v11")){
			
				//ERROR DEL MPT -> (V_11) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					int total = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					for (int j = 0; j < lstsanea_autonomo.size(); j++)
		            {
						V_sanea_autonomo_bean sanea_autonomo_bean  = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
						if(sanea_autonomo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								sanea_autonomo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								sanea_autonomo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad())&& 
								sanea_autonomo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							
							total += sanea_autonomo_bean.getSau_es_def();
						}
		            }
					if(nucl_encuestado_5_bean.getSyd_pe_def() < 0 && nucl_encuestado_5_bean.getSyd_pe_def() == total){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_11") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v12")){
				//ERROR DEL MPT -> (V_12) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_v_defi() > 0 && nucl_encuestado_5_bean.getSyd_l_defi() == 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_12") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v13")){
				//ERROR DEL MPT -> (V_13) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_l_defi() > 0 && (nucl_encuestado_5_bean.getSyd_v_defi() +  nucl_encuestado_5_bean.getSyd_pe_def())== 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_13") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v14")){
				//ERROR DEL MPT -> (V_14) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_v_defi() > 0 && nucl_encuestado_5_bean.getSyd_pe_def() <= 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_14") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v15")){
				//ERROR DEL MPT -> (V_15) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					String pro = "";
					String mun =  "";
					String codOrden =  "";
	                int count = 0;
	                int count2 = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
	
					for (int j = 0; j <lstramal_saneamiento.size(); j++)
		            {
						V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(j);
						if(ramal_saneamiento_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								ramal_saneamiento_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								ramal_saneamiento_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								ramal_saneamiento_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							count ++;
						}
		            }
	
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								colector_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								colector_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								colector_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
							pro = colector_nucleo_bean.getC_provinci();
							mun =  colector_nucleo_bean.getC_municipi();
							codOrden =  colector_nucleo_bean.getOrden_cole();
			
							for (int h = 0; h < lsttramo_colector.size(); h++)
				            {
								V_tramo_colector_bean tramo_colector_bean  = (V_tramo_colector_bean)lsttramo_colector.get(h);
								if(tramo_colector_bean.getProvincia().equals(pro) && 
										tramo_colector_bean.getMunicipio().equals(mun) && 
										tramo_colector_bean.getOrden_cole().equals(codOrden)){
							
									
									count2 ++;
								}
				            }
						}
		            }
					if(count != 0 && count2 != 0){
						if(nucl_encuestado_5_bean.getSyd_c_desa() == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_15") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						} 
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v16")){
				//ERROR DEL MPT -> (V_16) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_c_trat() > 0){
						int count = 0;
						for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
			            {
							V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
							if(dep_agua_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
									dep_agua_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
									dep_agua_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
									dep_agua_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
								count ++;
							}
			            }
						if(count == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_16") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
									 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
							 error = true;
						}
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v17")){
				//ERROR DEL MPT -> (V_17) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_c_trat() > 0){
						int count = 0;
						ArrayList lstAguaNucleo = new ArrayList();
						for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
			            {
							V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
							if(dep_agua_nucleo_bean.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
									dep_agua_nucleo_bean.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
									dep_agua_nucleo_bean.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
									dep_agua_nucleo_bean.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
	
								count ++;
								lstAguaNucleo.add(dep_agua_nucleo_bean);
							}
			            }
						if(count != 0){
							for (int h = 0; h < lstAguaNucleo.size(); h++)
				            {
								int count_depu = 0;
								V_depuradora_enc_2_bean depuradora_enc_2_bean_encontrado = null;
								V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstAguaNucleo.get(h);
								for (int j = 0; j < lstdepuradora_enc_2.size(); j++)
					            {
									V_depuradora_enc_2_bean depuradora_enc_2_bean  = (V_depuradora_enc_2_bean)lstdepuradora_enc_2.get(j);
									if(depuradora_enc_2_bean.getProvincia().equals(dep_agua_nucleo_bean.getDe_provinc()) && 
											depuradora_enc_2_bean.getMunicipio().equals(dep_agua_nucleo_bean.getDe_municip()) && 
											depuradora_enc_2_bean.getOrden_depu().equals(dep_agua_nucleo_bean.getOrden_depu())){
										count_depu ++;
										depuradora_enc_2_bean_encontrado = depuradora_enc_2_bean;
									}
					            }
								
								if(count_depu > 0){
									if(depuradora_enc_2_bean_encontrado.getProblem_1().equals("AB") && 
											depuradora_enc_2_bean_encontrado.getProblem_2().equals("AB") &&
											depuradora_enc_2_bean_encontrado.getProblem_3().equals("AB")){
										if (contTexto == 0)
										 {
											 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_17") + "\n");
											 str.append("\n");
											 contTexto++;
										 }
										 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
												 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
										 error = true;
									}
								}
								
				            }
						}
						
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v18")){
				
				//ERROR DEL MPT -> (V_18) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_c_trat() > 0 && 
							(nucl_encuestado_5_bean.getSyd_c_desa() < nucl_encuestado_5_bean.getSyd_c_trat())){
	
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_18") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v19")){
				//ERROR DEL MPT -> (V_19) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if((nucl_encuestado_5_bean.getSyd_re_urb() + nucl_encuestado_5_bean.getSyd_re_rus() + nucl_encuestado_5_bean.getSyd_re_ind() >
					nucl_encuestado_5_bean.getSyd_c_trat())){
	
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_19") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v20")){
				//ERROR DEL MPT -> (V_20) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					if(nucl_encuestado_5_bean.getSyd_calida().equals("NO") && 
							(nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon()) != 0){
	
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_20") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v21")){
				//ERROR DEL MPT -> (V_21) 
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					long suma_viario  = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i);
					for (int j = 0; j < lstinfraestr_viaria.size(); j++)
		            {
						V_infraestr_viaria_bean infraestr_viaria  = (V_infraestr_viaria_bean)lstinfraestr_viaria.get(j);
						
						if(infraestr_viaria.getProvincia().equals(nucl_encuestado_5_bean.getProvincia()) && 
								infraestr_viaria.getMunicipio().equals(nucl_encuestado_5_bean.getMunicipio()) && 
								infraestr_viaria.getEntidad().equals(nucl_encuestado_5_bean.getEntidad()) && 
								infraestr_viaria.getNucleo().equals(nucl_encuestado_5_bean.getNucleo())){
							suma_viario +=  infraestr_viaria.getLongitud();
						}
		            }
	
					if(nucl_encuestado_5_bean.getSyd_l_defi() >= suma_viario){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro35.V_21") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_5_bean.getProvincia() + nucl_encuestado_5_bean.getMunicipio() + 
								 nucl_encuestado_5_bean.getEntidad()  + nucl_encuestado_5_bean.getNucleo()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v22")){
				//ERROR DEL MPT -> (V_22) 
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lstnucl_encuestado_5.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i)).getMunicipio());
					codIne_bean.setNucleo(((V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i)).getNucleo());
					codIne_bean.setEntidad(((V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(i)).getEntidad());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro35.V_22", str);
			}
			if (contTexto != 0)
				str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro35.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
