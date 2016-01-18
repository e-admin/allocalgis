/**
 * Validacion_cuadro46.java
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
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro46 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro46.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro46") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_VERT_ENCUESTADO_M50";
			String sql_1 = "select * from v_VERTEDERO_NUCLEO";
	        String sql_2 = "select * from v_PADRON";
	        String sql_3 = "select * from v_MUN_ENC_DIS";
	        
	        ArrayList lstvert_encuestado_m50 = new ArrayList();
			ArrayList lstvertedero_nucleo = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstmun_enc_dis = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vert_encuestado_m50_bean  vert_encuestado_m50_bean = new V_vert_encuestado_m50_bean();

				vert_encuestado_m50_bean.setClave(rs.getString("CLAVE"));
				vert_encuestado_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				vert_encuestado_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vert_encuestado_m50_bean.setOrden_ver(rs.getString("ORDEN_VER"));
				vert_encuestado_m50_bean.setTipo_ver(rs.getString("TIPO_VER"));
				vert_encuestado_m50_bean.setTitular(rs.getString("TITULAR"));
				vert_encuestado_m50_bean.setGestion(rs.getString("GESTION"));
				vert_encuestado_m50_bean.setOlores(rs.getString("OLORES"));
				vert_encuestado_m50_bean.setHumos(rs.getString("HUMOS"));
				vert_encuestado_m50_bean.setCont_anima(rs.getString("CONT_ANIMA"));
				vert_encuestado_m50_bean.setR_inun(rs.getString("R_INUN"));
				vert_encuestado_m50_bean.setFiltracion(rs.getString("FILTRACION"));
				vert_encuestado_m50_bean.setImpacto_v(rs.getString("IMPACTO_V"));
				vert_encuestado_m50_bean.setFrec_averi(rs.getString("FREC_AVERI"));
				vert_encuestado_m50_bean.setSaturacion(rs.getString("SATURACION"));
				vert_encuestado_m50_bean.setInestable(rs.getString("INESTABLE"));
				vert_encuestado_m50_bean.setOtros(rs.getString("OTROS"));
				if(rs.getString("CAPAC_TOT")!=null&&!rs.getString("CAPAC_TOT").equals(""))
					vert_encuestado_m50_bean.setCapac_tot(new Integer(rs.getString("CAPAC_TOT")));
				else
					vert_encuestado_m50_bean.setCapac_tot(0);
				if(rs.getString("CAPAC_PORC")!=null&&!rs.getString("CAPAC_PORC").equals(""))
					vert_encuestado_m50_bean.setCapac_porc(new Integer(rs.getString("CAPAC_PORC")));
				else
					vert_encuestado_m50_bean.setCapac_porc(0);
				vert_encuestado_m50_bean.setCapac_ampl(rs.getString("CAPAC_AMPL"));
				
				if(rs.getString("CAPAC_TRAN")!=null&&!rs.getString("CAPAC_TRAN").equals(""))
					vert_encuestado_m50_bean.setCapac_tran(new Integer(rs.getString("CAPAC_TRAN")));
				else
					vert_encuestado_m50_bean.setCapac_tran(0);
				vert_encuestado_m50_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("VIDA_UTIL")!=null&&!rs.getString("VIDA_UTIL").equals(""))
					vert_encuestado_m50_bean.setVida_util(new Integer(rs.getString("VIDA_UTIL")));
				else
					vert_encuestado_m50_bean.setVida_util(0);
				vert_encuestado_m50_bean.setCategoria(rs.getString("CATEGORIA"));
				vert_encuestado_m50_bean.setActividad(rs.getString("ACTIVIDAD"));
				

				lstvert_encuestado_m50.add(vert_encuestado_m50_bean);
				
			}

			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vertedero_nucleo_bean vertedero_nucleo_bean = new V_vertedero_nucleo_bean();

				vertedero_nucleo_bean.setClave(rs.getString("CLAVE"));
				vertedero_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				vertedero_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vertedero_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				vertedero_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				vertedero_nucleo_bean.setVer_provin(rs.getString("VER_PROVIN"));
				vertedero_nucleo_bean.setVer_munici(rs.getString("VER_MUNICI"));
				vertedero_nucleo_bean.setVer_codigo(rs.getString("VER_CODIGO"));

				lstvertedero_nucleo.add(vertedero_nucleo_bean);
				
			}
		
			ps = connection.prepareStatement(sql_2);
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
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
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
				
				lstmun_enc_dis.add(mun_enc_dis_bean);
				
			}
		
			
			//FALSOERROR DEL MPT -> 
			for (int i = 0; i < lstvert_encuestado_m50.size(); i++)
            {
				V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);

				if(vert_encuestado_m50_bean.getOrden_ver().length() < 3)
				{
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro46.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
							 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
					 error = true;

	            }
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			//FALSOERROR DEL MPT 
			for (int i = 0; i < lstvert_encuestado_m50.size(); i++)
            {
				V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);

				if(vert_encuestado_m50_bean.getTipo_ver().equals("")){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro46.falsoerror.V_2") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
							 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
					 error = true;

				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			//FALSOERROR DEL MPT 
			for (int i = 0; i < lstvert_encuestado_m50.size(); i++)
            {
				V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);

				if(vert_encuestado_m50_bean.getActividad().equals("")){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro46.falsoerror.V_3") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
							 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
					 error = true;

				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;

			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstvert_encuestado_m50.size(); i++)
	            {
					V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstvertedero_nucleo.size(); j++)
		            {
						V_vertedero_nucleo_bean vertedero_nucleo_bean   = (V_vertedero_nucleo_bean)lstvertedero_nucleo.get(i);
						if(vertedero_nucleo_bean.getVer_provin().equals(vert_encuestado_m50_bean.getProvincia()) &&
								vertedero_nucleo_bean.getVer_munici().equals(vert_encuestado_m50_bean.getMunicipio()) &&
								vertedero_nucleo_bean.getVer_codigo().equals(vert_encuestado_m50_bean.getOrden_ver())){
							count ++;
						}
						
		            }
					if (count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro46.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
								 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
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
				for (int i = 0; i < lstvert_encuestado_m50.size(); i++){
					V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);
					if(vert_encuestado_m50_bean.getActividad().equals("EN") &&
							!vert_encuestado_m50_bean.getCategoria().equals("VIN") &&
							vert_encuestado_m50_bean.getVida_util() <= 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro46.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
								 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
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
				for (int i = 0; i < lstvert_encuestado_m50.size(); i++){
					V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);
					if(vert_encuestado_m50_bean.getCapac_porc() > 100){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro46.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia() + 
								 vert_encuestado_m50_bean.getMunicipio()  + vert_encuestado_m50_bean.getOrden_ver()+"\t");
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
				for (int i = 0; i < lstvert_encuestado_m50.size(); i++){
					V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron_bean = (V_padron_bean)lstpadron.get(j);
						
						if( padron_bean.getCodprov().equals(vert_encuestado_m50_bean.getProvincia()) &&
								padron_bean.getCodmunic().equals(vert_encuestado_m50_bean.getMunicipio()) &&
								padron_bean.getTotal_poblacion_a1() < 50000 ){
							
							count ++;
						}
		            }
					if (count == 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro46.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia()+ 
								 vert_encuestado_m50_bean.getMunicipio() + vert_encuestado_m50_bean.getOrden_ver() +"\t");
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
				for (int i = 0; i < lstvert_encuestado_m50.size(); i++){
					V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstmun_enc_dis.size(); j++)
		            {
						V_mun_enc_dis_bean mun_enc_dis_bean = (V_mun_enc_dis_bean)lstmun_enc_dis.get(j);
						if(mun_enc_dis_bean.getCodprov().equals(vert_encuestado_m50_bean.getProvincia()) &&
								mun_enc_dis_bean.getCodmunic().equals(vert_encuestado_m50_bean.getMunicipio())){
							count ++;
						}
		            }
					if (count != 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro46.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(vert_encuestado_m50_bean.getClave() + vert_encuestado_m50_bean.getProvincia()+ 
								 vert_encuestado_m50_bean.getMunicipio() + vert_encuestado_m50_bean.getOrden_ver() +"\t");
					}
				}
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro46.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
