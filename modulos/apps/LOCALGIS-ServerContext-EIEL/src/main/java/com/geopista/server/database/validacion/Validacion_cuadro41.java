/**
 * Validacion_cuadro41.java
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

public class Validacion_cuadro41 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro41.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro41") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_SANEA_AUTONOMO";
			String sql_filtro = "select * from v_SANEA_AUTONOMO where ADECUACION='AD' AND (ESTADO='B' OR ESTADO='E') AND SAU_VI_DEF=SAU_VIVIEN AND  SAU_VI_DEF>0 ";
			String sql_filtro1 = "select * from v_SANEA_AUTONOMO where ADECUACION='AD' AND (ESTADO='B' OR ESTADO='E') AND SAU_RE_DEF=SAU_POB_RE  and SAU_RE_DEF>0 ";
			String sql_filtro2 = "select * from v_SANEA_AUTONOMO where ADECUACION='AD' AND (ESTADO='B' OR ESTADO='E') AND SAU_ES_DEF=SAU_POB_ES  and SAU_ES_DEF>0 ";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_5";
			String sql_2 = "select * from v_NUCL_ENCUESTADO_1";
			String sql_3 = "select * from v_RAMAL_SANEAMIENTO";
			String sql_4 = "select * from v_COLECTOR_NUCLEO";
			String sql_5 = "select * from v_EMISARIO_NUCLEO";

			ArrayList lstsanea_autonomo = new ArrayList();
			ArrayList lstsanea_autonomo_filtro = new ArrayList();
			ArrayList lstsanea_autonomo_filtro1 = new ArrayList();
			ArrayList lstsanea_autonomo_filtro2 = new ArrayList();
			ArrayList lstnucl_encuestado_5 = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstramal_saneamiento = new ArrayList();
			ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lstemisario_nucleo = new ArrayList();
			
			ps = connection.prepareStatement(sql);
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
			
			
			ps = connection.prepareStatement(sql_filtro);
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

				lstsanea_autonomo_filtro.add(sanea_autonomo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_filtro1);
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

				lstsanea_autonomo_filtro1.add(sanea_autonomo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_filtro2);
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

				lstsanea_autonomo_filtro2.add(sanea_autonomo_bean);
				
			}

			ps = connection.prepareStatement(sql_1);
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
			
			ps = connection.prepareStatement(sql_2);
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
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int count = 0;
					for (int j = 0; j <lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean  = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						
						if(nucl_encuestado_5_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_5_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							
							if(sanea_autonomo_bean.getSau_vi_def() > 0 && 
									sanea_autonomo_bean.getSau_vi_def() == nucl_encuestado_5_bean.getSyd_v_defi()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_01") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
										 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
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
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int count = 0;
					for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean  = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						
						if(nucl_encuestado_5_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_5_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							
							if(sanea_autonomo_bean.getSau_re_def() > 0 && 
									sanea_autonomo_bean.getSau_re_def() == nucl_encuestado_5_bean.getSyd_pr_def()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_02") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
										 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
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
			if(lstValCuadros.contains("v03")){
			
				//ERROR DEL MPT -> (V_03) 
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int count = 0;
					for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean  = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						
						if(nucl_encuestado_5_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_5_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							
							if(sanea_autonomo_bean.getSau_es_def() > 0 && 
									sanea_autonomo_bean.getSau_es_def() == nucl_encuestado_5_bean.getSyd_pe_def()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_03") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
										 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
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
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04) 
				for (int i = 0; i < lstsanea_autonomo_filtro.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo_filtro.get(i);
	
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_04") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
							 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
					 error = true;
		           
	            }	
				if (error)
					str.append("\n\n");
				contTexto = 0;
			}
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05)
				for (int i = 0; i < lstsanea_autonomo_filtro1.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo_filtro1.get(i);
	
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_05") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
							 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
					 error = true;
		           
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v06")){
				//ERROR DEL MPT -> (V_06)
				for (int i = 0; i < lstsanea_autonomo_filtro2.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo_filtro2.get(i);
	
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_06") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio() + 
							 sanea_autonomo_bean.getEntidad()  + sanea_autonomo_bean.getNucleo()+"\t");
					 error = true;
		           
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07)
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int suma = sanea_autonomo_bean.getSau_vivien() + sanea_autonomo_bean.getSau_vi_def();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_1_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							if(suma > nucl_encuestado_1_bean.getViv_total()){		
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_07") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
										 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
			if(lstValCuadros.contains("v08")){
				//ERROR DEL MPT -> (V_08)
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int suma = sanea_autonomo_bean.getSau_pob_re() + sanea_autonomo_bean.getSau_re_def();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_1_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							if(suma > nucl_encuestado_1_bean.getPadron()){		
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_08") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
										 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
			if(lstValCuadros.contains("v09")){

				//ERROR DEL MPT -> (V_09)
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					int suma = sanea_autonomo_bean.getSau_pob_es() + sanea_autonomo_bean.getSau_es_def();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_1_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							if(suma > nucl_encuestado_1_bean.getPob_estaci()){		
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_09") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
										 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
			if(lstValCuadros.contains("v10")){
				//ERROR DEL MPT -> (V_10)
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					
					if(sanea_autonomo_bean.getSau_vi_def() > 0 && sanea_autonomo_bean.getSau_pob_es() <= 0 ){
							
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_10") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
								 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					
					if(sanea_autonomo_bean.getSau_vivien() > 0 && sanea_autonomo_bean.getSau_pob_es() <= 0 ){
							
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro41.V_11") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
								 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					int suma = 0;
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_1_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							if(sanea_autonomo_bean.getSau_vivien() == nucl_encuestado_1_bean.getViv_total()){
								
								V_nucl_encuestado_5_bean nucl_encuestado_5_bean_encontrado = null;
								int count =0;
								for (int h = 0; h < lstnucl_encuestado_5.size(); h++)
					            {
									V_nucl_encuestado_5_bean nucl_encuestado_5_bean  = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(h);
									
									if(nucl_encuestado_5_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
											nucl_encuestado_5_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
											nucl_encuestado_5_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
											nucl_encuestado_5_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
										count ++;
										nucl_encuestado_5_bean_encontrado	= nucl_encuestado_5_bean;
										
//										suma = nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon() +
//											nucl_encuestado_5_bean.getSyd_v_defi();
											
					            	}
									
					            }
								if(count != 0){
									suma = nucl_encuestado_5_bean_encontrado.getSyd_v_cone() + nucl_encuestado_5_bean_encontrado.getSyd_v_ncon() +
									nucl_encuestado_5_bean_encontrado.getSyd_v_defi();
								
									if(suma != 0){
										if (contTexto == 0)
										 {
											 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro41.falsoerror.V_1") + "\n");
											 str.append("\n");
											 contTexto++;
										 }
										 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
												 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
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
			if(lstValCuadros.contains("v13")){
				
				//ERROR DEL MPT -> (V_13)
				for (int i = 0; i < lstsanea_autonomo.size(); i++)
	            {
					int suma = 0;
					V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(i);
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
								nucl_encuestado_1_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
							
							if(sanea_autonomo_bean.getSau_vivien() == nucl_encuestado_1_bean.getViv_total()){
								int count = 0;
								int count1 = 0;
								int count2 = 0;
								for (int h = 0; h < lstramal_saneamiento.size(); h++)
					            {
									V_ramal_saneamiento_bean ramal_saneamiento_bean  = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(h);
									if(ramal_saneamiento_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
											ramal_saneamiento_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
											ramal_saneamiento_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
											ramal_saneamiento_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
										count ++;
									}
					            }
								for (int h = 0; h < lstcolector_nucleo.size(); h++)
					            {
									V_colector_nucleo_bean colector_nucleo_bean  = (V_colector_nucleo_bean)lstcolector_nucleo.get(h);
									if(colector_nucleo_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
											colector_nucleo_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
											colector_nucleo_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
											colector_nucleo_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
										count1 ++;
									}
					            }
								for (int h = 0; h < lstemisario_nucleo.size(); h++)
					            {
									V_emisario_nucleo_bean emisario_nucleo_bean   = (V_emisario_nucleo_bean)lstemisario_nucleo.get(h);
									if(emisario_nucleo_bean.getProvincia().equals(sanea_autonomo_bean.getProvincia()) && 
											emisario_nucleo_bean.getMunicipio().equals(sanea_autonomo_bean.getMunicipio()) && 
											emisario_nucleo_bean.getEntidad().equals(sanea_autonomo_bean.getEntidad()) &&
											emisario_nucleo_bean.getNucleo().equals(sanea_autonomo_bean.getNucleo())){
										count2 ++;
									}
					            }
								
								if(count != 0 || count1 != 0 || count2 != 0){
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro41.falsoerror.V_2") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(sanea_autonomo_bean.getProvincia() + sanea_autonomo_bean.getMunicipio()+ 
											 sanea_autonomo_bean.getEntidad() + sanea_autonomo_bean.getNucleo() +"\t");
		
								}
							}
						}
		            }
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro41.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
