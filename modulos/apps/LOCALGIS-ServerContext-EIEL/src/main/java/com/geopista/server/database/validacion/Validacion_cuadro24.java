/**
 * Validacion_cuadro24.java
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
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
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
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro24 {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro24.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro24") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			
			String sql = "select * from v_RAMAL_SANEAMIENTO";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_5";	
			String sql_2 = "select * from v_SANEA_AUTONOMO";
			String sql_3 = "select * from v_COLECTOR_NUCLEO";
			String sql_4 = "select * from v_NUCL_ENCUESTADO_1";	

			ArrayList lstramal_saneamiento = new ArrayList();
			ArrayList lstnucl_encuestado_5 = new ArrayList();
			ArrayList lstsanea_autonomo = new ArrayList();
			ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
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
				V_sanea_autonomo_bean sanea_autonomo_bean = new V_sanea_autonomo_bean();
				sanea_autonomo_bean.setProvincia(rs.getString("PROVINCIA"));
				sanea_autonomo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				sanea_autonomo_bean.setEntidad(rs.getString("ENTIDAD"));
				sanea_autonomo_bean.setNucleo(rs.getString("NUCLEO"));		

				lstsanea_autonomo.add(sanea_autonomo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_3);
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
			
			ps = connection.prepareStatement(sql_4);
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
				//NULLnucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));

				lstnucl_encuestado_1.add(nucl_encuestado_1_bean);
				
			}
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					if(ramal_saneamiento_bean.getLongit_ram() == 0 && !ramal_saneamiento_bean.getEstado().equals("E")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
								 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
						 error = true;
						
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
				
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					if(ramal_saneamiento_bean.getEstado().equals("")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro24.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
								 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						if (nucl_encuestado_5_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
								nucl_encuestado_5_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
							
							if( (nucl_encuestado_5_bean.getSyd_v_cone() +nucl_encuestado_5_bean.getSyd_v_ncon()) == 0){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_02") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
										 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						if (nucl_encuestado_5_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
								nucl_encuestado_5_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
							
							if( (nucl_encuestado_5_bean.getSyd_v_cone() +nucl_encuestado_5_bean.getSyd_v_ncon()) == 
								nucl_encuestado_5_bean.getSyd_v_defi()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_03") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
										 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					if(ramal_saneamiento_bean.getEstado().equals("E")){
						int count_5 = 0;
						int count_sanea = 0;
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean_encontrado = null;
						V_sanea_autonomo_bean sanea_autonomo_bean_encontrado = null;
						
						for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
			            {
							V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
							if (nucl_encuestado_5_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
									nucl_encuestado_5_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
									nucl_encuestado_5_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
									nucl_encuestado_5_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
								count_5 ++;
								nucl_encuestado_5_bean_encontrado = nucl_encuestado_5_bean;
							}
			            }
						
						for (int j = 0; j < lstsanea_autonomo.size(); j++)
			            {
							V_sanea_autonomo_bean sanea_autonomo_bean   = (V_sanea_autonomo_bean)lstsanea_autonomo.get(j);
							if (sanea_autonomo_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
									sanea_autonomo_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
									sanea_autonomo_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
									sanea_autonomo_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
								count_sanea ++;
								sanea_autonomo_bean_encontrado = sanea_autonomo_bean;
							}
			            }
						if(count_5 > 0 && count_sanea > 0){
							if((nucl_encuestado_5_bean_encontrado.getSyd_v_cone() + nucl_encuestado_5_bean_encontrado.getSyd_v_ncon()) ==
								sanea_autonomo_bean_encontrado.getSau_vi_def()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_04") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
										 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v05")){
				
				//ERROR DEL MPT -> (V_05)
				for (int i = 0; i < lstramal_saneamiento.size(); i++)
	            {
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
					if(ramal_saneamiento_bean.getEstado().equals("E")){
				
						for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
			            {
							V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
							if (nucl_encuestado_5_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
									nucl_encuestado_5_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
									nucl_encuestado_5_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
									nucl_encuestado_5_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
								
								if((nucl_encuestado_5_bean.getSyd_v_cone() + nucl_encuestado_5_bean.getSyd_v_ncon()) ==
									nucl_encuestado_5_bean.getSyd_v_defi()){
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_05") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
											 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
									 str.append("\n");
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
			if(lstValCuadros.contains("v06")){
				//ERROR DEL MPT -> (V_06)
				for (int i = 0; i < lstramal_saneamiento.size(); i++) 
				{
					V_ramal_saneamiento_bean ramal_saneamiento_bean   = (V_ramal_saneamiento_bean)lstramal_saneamiento.get(i);
	
					int count_5 = 0;
					int count_colector = 0;
					V_nucl_encuestado_5_bean nucl_encuestado_5_bean_encontrado = null;
					V_colector_nucleo_bean colector_nucleo_bean_encontrado = null;
						
					for (int j = 0; j < lstnucl_encuestado_5.size(); j++)
		            {
						V_nucl_encuestado_5_bean nucl_encuestado_5_bean   = (V_nucl_encuestado_5_bean)lstnucl_encuestado_5.get(j);
						if (nucl_encuestado_5_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
								nucl_encuestado_5_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
								nucl_encuestado_5_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
								nucl_encuestado_5_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
							count_5 ++;
							nucl_encuestado_5_bean_encontrado = nucl_encuestado_5_bean;
						}
		            }
							
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean   = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if (colector_nucleo_bean.getProvincia().equals(ramal_saneamiento_bean.getProvincia()) && 
								colector_nucleo_bean.getMunicipio().equals(ramal_saneamiento_bean.getMunicipio()) &&
								colector_nucleo_bean.getEntidad().equals(ramal_saneamiento_bean.getEntidad()) &&
								colector_nucleo_bean.getNucleo().equals(ramal_saneamiento_bean.getNucleo()) ){
							count_colector ++;
							colector_nucleo_bean_encontrado = colector_nucleo_bean;
						}
		            }
					if(count_5 == 0 && count_colector == 0){
						if((nucl_encuestado_5_bean_encontrado.getSyd_v_cone() + nucl_encuestado_5_bean_encontrado.getSyd_v_ncon()) != 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro24.V_06") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(  ramal_saneamiento_bean.getProvincia() + ramal_saneamiento_bean.getMunicipio() +
									 ramal_saneamiento_bean.getEntidad() + ramal_saneamiento_bean.getNucleo() + "\t");
	
						}
						
					}
				}
			}
			str.append("\n\n");

        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro24.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
