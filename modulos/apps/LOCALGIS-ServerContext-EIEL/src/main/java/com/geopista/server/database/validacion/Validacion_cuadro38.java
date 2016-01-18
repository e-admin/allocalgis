/**
 * Validacion_cuadro38.java
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

public class Validacion_cuadro38 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro38.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro38") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_DEPURADORA_ENC_M50";
			String sql_1 = "select * from v_DEP_AGUA_NUCLEO";
			String sql_2 = "select * from v_DEPURADORA_ENC_2_M50";
			String sql_3 = "select * from v_PADRON";
			String sql_4 = "select * from v_MUN_ENC_DIS";
			

		        
			ArrayList lstdepuradora_enc_m50 = new ArrayList();
			ArrayList lstdep_agua_nucleo = new ArrayList();
			ArrayList lstdepuradora_enc_2_m50 = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstmun_enc_dis = new ArrayList();


			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_m50_bean depuradora_enc_m50_bean = new V_depuradora_enc_m50_bean();
				depuradora_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_m50_bean.setClave(rs.getString("CLAVE"));
				depuradora_enc_m50_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_m50_bean.setTrat_pr_1(rs.getString("TRAT_PR_1"));
				depuradora_enc_m50_bean.setTrat_pr_2(rs.getString("TRAT_PR_2"));
				depuradora_enc_m50_bean.setTrat_pr_3(rs.getString("TRAT_PR_3"));
				depuradora_enc_m50_bean.setTrat_sc_1(rs.getString("TRAT_SC_1"));
				depuradora_enc_m50_bean.setTrat_sc_2(rs.getString("TRAT_SC_2"));
				depuradora_enc_m50_bean.setTrat_sc_3(rs.getString("TRAT_SC_3"));
				depuradora_enc_m50_bean.setTrat_av_1(rs.getString("TRAT_AV_1"));
				depuradora_enc_m50_bean.setTrat_av_2(rs.getString("TRAT_AV_2"));
				depuradora_enc_m50_bean.setTrat_av_3(rs.getString("TRAT_AV_3"));
				depuradora_enc_m50_bean.setTrat_ld_1(rs.getString("TRAT_LD_1"));
				depuradora_enc_m50_bean.setTrat_ld_2(rs.getString("TRAT_LD_2"));
				depuradora_enc_m50_bean.setTrat_ld_3(rs.getString("TRAT_LD_3"));

				lstdepuradora_enc_m50.add(depuradora_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_1);
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
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_2_m50_bean depuradora_enc_2_m50 = new V_depuradora_enc_2_m50_bean();

				depuradora_enc_2_m50.setClave(rs.getString("CLAVE"));
				depuradora_enc_2_m50.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_2_m50.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_2_m50.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_2_m50.setTitular(rs.getString("TITULAR"));
				depuradora_enc_2_m50.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					depuradora_enc_2_m50.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					depuradora_enc_2_m50.setCapacidad(0);				depuradora_enc_2_m50.setProblem_1(rs.getString("PROBLEM_1"));
				depuradora_enc_2_m50.setProblem_2(rs.getString("PROBLEM_2"));
				depuradora_enc_2_m50.setProblem_3(rs.getString("PROBLEM_3"));
				depuradora_enc_2_m50.setLodo_gest(rs.getString("LODO_GEST"));
				depuradora_enc_2_m50.setLodo_vert(new Integer(rs.getString("LODO_VERT")));
				depuradora_enc_2_m50.setLodo_inci(new Integer(rs.getString("LODO_INCI")));
				depuradora_enc_2_m50.setLodo_con_a(new Integer(rs.getString("LODO_CON_A")));
				depuradora_enc_2_m50.setLodo_sin_a(new Integer(rs.getString("LODO_SIN_A")));
				depuradora_enc_2_m50.setLodo_ot(new Integer(rs.getString("LODO_OT")));
				
				lstdepuradora_enc_2_m50.add(depuradora_enc_2_m50);
				
			}
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_padron_bean  padron_bean = new V_padron_bean();

				padron_bean.setCodprov(rs.getString("PROVINCIA"));
				padron_bean.setCodmunic(rs.getString("MUNICIPIO"));
				padron_bean.setN_hombre_a1(new Integer(rs.getString("HOMBRES")));
				padron_bean.setN_mujeres_a1(new Integer(rs.getString("MUJERES")));
				padron_bean.setTotal_poblacion_a1(new Integer(rs.getString("TOTAL_POB")));
				lstpadron.add(padron_bean);
				
			}
			

			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			
			ArrayList lstNumEncDis = new ArrayList();
			while (rs.next()) {		
				V_mun_enc_dis_bean mun_enc_dis_bean = new V_mun_enc_dis_bean();

				mun_enc_dis_bean.setCodprov(rs.getString("PROVINCIA"));
				mun_enc_dis_bean.setCodmunic(rs.getString("MUNICIPIO"));
				mun_enc_dis_bean.setPadron(new Integer(rs.getString("PADRON")));
				mun_enc_dis_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
				mun_enc_dis_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				mun_enc_dis_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				mun_enc_dis_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				mun_enc_dis_bean.setLongitud(new Integer(rs.getString("LONGITUD")));
				mun_enc_dis_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
				mun_enc_dis_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
				mun_enc_dis_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
				mun_enc_dis_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
				mun_enc_dis_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
				mun_enc_dis_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
				mun_enc_dis_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
				mun_enc_dis_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
				mun_enc_dis_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
				mun_enc_dis_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
				mun_enc_dis_bean.setAau_vivien(new Integer(rs.getString("AAU_VIVIEN")));
				mun_enc_dis_bean.setAau_pob_re(new Integer(rs.getString("aau_pob_re")));
				mun_enc_dis_bean.setAau_pob_es(new Integer(rs.getString("aau_pob_es")));
				mun_enc_dis_bean.setAau_def_vi(new Integer(rs.getString("aau_def_vi")));
				mun_enc_dis_bean.setAau_def_re(new Integer(rs.getString("aau_def_re")));
				mun_enc_dis_bean.setAau_def_es(new Integer(rs.getString("aau_def_es")));
				mun_enc_dis_bean.setAau_fecont(new Integer(rs.getString("aau_fecont")));
				mun_enc_dis_bean.setAau_fencon(new Integer(rs.getString("AAU_FENCON")));
				mun_enc_dis_bean.setLongi_ramal(new Integer(rs.getString("LONGIT_RAM")));
				mun_enc_dis_bean.setSyd_v_cone(new Integer(rs.getString("SYD_V_CONE")));
				mun_enc_dis_bean.setSyd_v_ncon(new Integer(rs.getString("SYD_V_NCON")));
				mun_enc_dis_bean.setSyd_l_defi(new Integer(rs.getString("SYD_L_DEFI")));
				mun_enc_dis_bean.setSyd_v_defi(new Integer(rs.getString("SYD_V_DEFI")));
				mun_enc_dis_bean.setSyd_c_desa(new Integer(rs.getString("SYD_C_DESA")));
				mun_enc_dis_bean.setSyd_c_trat(new Integer(rs.getString("SYD_C_TRAT")));
				mun_enc_dis_bean.setSau_vivien(new Integer(rs.getString("SAU_VIVIEN")));
				mun_enc_dis_bean.setSau_pob_re(new Integer(rs.getString("SAU_POB_RE")));
				mun_enc_dis_bean.setSau_pob_es(new Integer(rs.getString("SAU_POB_ES")));
				mun_enc_dis_bean.setSau_vi_def(new Integer(rs.getString("SAU_VI_DEF")));
				mun_enc_dis_bean.setSau_re_def(new Integer(rs.getString("SAU_RE_DEF")));
				mun_enc_dis_bean.setSau_es_def(new Integer(rs.getString("SAU_ES_DEF")));
				mun_enc_dis_bean.setProdu_basu(new Integer(rs.getString("PRODU_BASU")));
				mun_enc_dis_bean.setContenedores(new Integer(rs.getString("CONTENEDOR")));
				mun_enc_dis_bean.setRba_v_sser(new Integer(rs.getString("RBA_V_SSER")));
				mun_enc_dis_bean.setRba_pr_sse(new Integer(rs.getString("RBA_PR_SSE")));
				mun_enc_dis_bean.setRba_pe_sse(new Integer(rs.getString("RBA_PE_SSE")));
				mun_enc_dis_bean.setRba_plalim(new Integer(rs.getString("RBA_PLALIM")));
				mun_enc_dis_bean.setPuntos_luz(new Integer(rs.getString("PUNTOS_LUZ")));
				mun_enc_dis_bean.setAlu_v_sin(new Integer(rs.getString("ALU_V_SIN")));
				mun_enc_dis_bean.setAlu_l_sin(new Integer(rs.getString("ALU_L_SIN")));
				
				lstNumEncDis.add(mun_enc_dis_bean);
	
			}
		
			if(lstValCuadros.contains("v01")){
				 //ERROR DEL MPT -> (FALSO ERROR)
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					if(depuradora_enc_m50_bean.getOrden_depu().length() < 3){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro38.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia() + 
								 depuradora_enc_m50_bean.getMunicipio()  + depuradora_enc_m50_bean.getOrden_depu()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
		            {
						V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
						
						if(dep_agua_nucleo_bean.getDe_provinc().equals(depuradora_enc_m50_bean.getProvincia()) && 
								dep_agua_nucleo_bean.getDe_municip().equals(depuradora_enc_m50_bean.getMunicipio()) && 
								dep_agua_nucleo_bean.getOrden_depu().equals(depuradora_enc_m50_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro38.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia() + 
								 depuradora_enc_m50_bean.getMunicipio()  + depuradora_enc_m50_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstdepuradora_enc_2_m50.size(); j++)
		            {
						V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean  = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(j);
						
						if(depuradora_enc_2_m50_bean.getProvincia().equals(depuradora_enc_m50_bean.getProvincia()) && 
								depuradora_enc_2_m50_bean.getMunicipio().equals(depuradora_enc_m50_bean.getMunicipio()) && 
								depuradora_enc_2_m50_bean.getOrden_depu().equals(depuradora_enc_m50_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro38.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia() + 
								 depuradora_enc_m50_bean.getMunicipio()  + depuradora_enc_m50_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					if(depuradora_enc_m50_bean.getTrat_pr_1().equals("NO") && depuradora_enc_m50_bean.getTrat_pr_2().equals("NO") && depuradora_enc_m50_bean.getTrat_pr_3().equals("NO") &&
							depuradora_enc_m50_bean.getTrat_sc_1().equals("NO") && depuradora_enc_m50_bean.getTrat_sc_2().equals("NO") && depuradora_enc_m50_bean.getTrat_sc_3().equals("NO") &&
							depuradora_enc_m50_bean.getTrat_av_1().equals("NO") && depuradora_enc_m50_bean.getTrat_av_2().equals("NO") && depuradora_enc_m50_bean.getTrat_av_3().equals("NO") &&
							depuradora_enc_m50_bean.getTrat_ld_1().equals("NO") && depuradora_enc_m50_bean.getTrat_ld_2().equals("NO") && depuradora_enc_m50_bean.getTrat_ld_3().equals("NO")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro38.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia() + 
								 depuradora_enc_m50_bean.getMunicipio()  + depuradora_enc_m50_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					int count = 0;
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean  = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron  = (V_padron_bean)lstpadron.get(j);
						if(padron.getCodprov().equals(depuradora_enc_m50_bean.getProvincia()) &&
								padron.getCodmunic().equals(depuradora_enc_m50_bean.getMunicipio()) && 
								padron.getTotal_poblacion_a1() > 50000){
							count ++;
						}
		            }
					if(count == 0){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro38.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia()+ 
								 depuradora_enc_m50_bean.getMunicipio() + depuradora_enc_m50_bean.getOrden_depu() +"\t");
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
				for (int i = 0; i < lstdepuradora_enc_m50.size(); i++)
	            {
					int count = 0;
					V_depuradora_enc_m50_bean depuradora_enc_m50_bean  = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(i);
					for (int j = 0; j < lstNumEncDis.size(); j++)
		            {
						V_mun_enc_dis_bean munEncDis = (V_mun_enc_dis_bean)lstNumEncDis.get(j);
						
						if(munEncDis.getCodprov().equals(depuradora_enc_m50_bean.getProvincia()) && 
								munEncDis.getCodmunic().equals(depuradora_enc_m50_bean.getMunicipio())){
							count ++;
						}
		            }
					if(count != 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro38.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_m50_bean.getClave() + depuradora_enc_m50_bean.getProvincia()+ 
								 depuradora_enc_m50_bean.getMunicipio() + depuradora_enc_m50_bean.getOrden_depu() +"\t");
	
					}		
					
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro38.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
