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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro19 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {       
	        
			str.append(Messages.getString("cuadro19") + "\n");
			str.append("______________________________________________________________________\n\n");
			
	        String sql = "select * from v_POTABILIZACION_ENC_M50";
	        String sql_distinct = "select distinct PROVINCIA, MUNICIPIO  from v_POTABILIZACION_ENC_M50";
	        String sql_filtro = "select * from v_POTABILIZACION_ENC_M50 " +
			"	where not (S_DESINF='SI' OR  CAT_A1='SI' OR CAT_A2='SI' OR CAT_A3='SI'  OR  DESALADORA='SI'  OR  OTROS='SI' )";
	        String sql_1 = "select * from v_TRAT_POTA_NUCLEO";
	        String sql_2 = "select * from v_PADRON";
	        String sql_3 = "select * from v_MUN_ENC_DIS";
		        

		
			ArrayList lstpotabilizacion_enc_m50 = new ArrayList();
			ArrayList lstpotabilizacion_enc_m50_distinct = new ArrayList();
			ArrayList lstpotabilizacion_enc_m50_filtro = new ArrayList();
			ArrayList lsttrat_pota_nucleo = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstmun_enc_dis = new ArrayList();
			
		
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_m50_bean  potabilizacion_enc_m50_bean = new V_potabilizacion_enc_m50_bean();

				potabilizacion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				potabilizacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				potabilizacion_enc_m50_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				potabilizacion_enc_m50_bean.setTipo_tra(rs.getString("TIPO_TRA"));
				potabilizacion_enc_m50_bean.setUbicacion(rs.getString("UBICACION"));
				potabilizacion_enc_m50_bean.setS_desinf(rs.getString("S_DESINF"));
				potabilizacion_enc_m50_bean.setCat_a1(rs.getString("CAT_A1"));
				potabilizacion_enc_m50_bean.setCat_a2(rs.getString("CAT_A2"));
				potabilizacion_enc_m50_bean.setCat_a3(rs.getString("CAT_A3"));
				potabilizacion_enc_m50_bean.setDesaladora(rs.getString("DESALADORA"));
				potabilizacion_enc_m50_bean.setOtros(rs.getString("OTROS"));
				potabilizacion_enc_m50_bean.setDesinf_1(rs.getString("DESINF_1"));
				potabilizacion_enc_m50_bean.setDesinf_2(rs.getString("DESINF_2"));
				potabilizacion_enc_m50_bean.setDesinf_3(rs.getString("DESINF_3"));
				potabilizacion_enc_m50_bean.setPeriodicid(rs.getString("PERIODICID"));
				potabilizacion_enc_m50_bean.setOrganismo(rs.getString("ORGANISMO"));
				potabilizacion_enc_m50_bean.setEstado(rs.getString("ESTADO"));
				
				lstpotabilizacion_enc_m50.add(potabilizacion_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_distinct);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean = new V_potabilizacion_enc_m50_bean();
				potabilizacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				
				lstpotabilizacion_enc_m50_distinct.add(potabilizacion_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_filtro);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean = new V_potabilizacion_enc_m50_bean();
				potabilizacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				potabilizacion_enc_m50_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				potabilizacion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				
				lstpotabilizacion_enc_m50_filtro.add(potabilizacion_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_trat_pota_nucleo_bean trat_pota_nucleo_bean = new V_trat_pota_nucleo_bean();

				trat_pota_nucleo_bean.setClave(rs.getString("CLAVE"));
				trat_pota_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				trat_pota_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				trat_pota_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				trat_pota_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				trat_pota_nucleo_bean.setPo_provin(rs.getString("PO_PROVIN"));
				trat_pota_nucleo_bean.setPo_munipi(rs.getString("PO_MUNIPI"));
				trat_pota_nucleo_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));

				
				lsttrat_pota_nucleo.add(trat_pota_nucleo_bean);
				
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
				lstmun_enc_dis.add(mun_enc_dis_bean);
	
			}
			
					
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstpotabilizacion_enc_m50.size(); i++)
	            {
					int count = 0;
	
	                V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean  = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50.get(i);
					for (int j = 0; j < lsttrat_pota_nucleo.size(); j++)
		            {
						V_trat_pota_nucleo_bean trat_pota_nucleo_bean = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(j);
						
						if( trat_pota_nucleo_bean.getPo_provin().equals(potabilizacion_enc_m50_bean.getProvincia()) &&
								trat_pota_nucleo_bean.getPo_munipi().equals(potabilizacion_enc_m50_bean.getMunicipio()) &&
								trat_pota_nucleo_bean.getOrden_trat().equals(potabilizacion_enc_m50_bean.getOrden_trat())){
							
							count ++;
						}
		            }
					
					if (count == 0){
									
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro19.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( potabilizacion_enc_m50_bean.getClave()+ potabilizacion_enc_m50_bean.getProvincia() + 
								 potabilizacion_enc_m50_bean.getMunicipio() + potabilizacion_enc_m50_bean.getOrden_trat() + "\t");
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
				for (int i = 0; i < lstpotabilizacion_enc_m50_filtro.size(); i++)
	            {
					V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50_filtro.get(i);
					
					if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro19.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( potabilizacion_enc_m50_bean.getClave() + potabilizacion_enc_m50_bean.getProvincia()+ 
								 potabilizacion_enc_m50_bean.getMunicipio() +  potabilizacion_enc_m50_bean.getOrden_trat() +"\t");
						 error = true;
	
				}
	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}

			if(lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03)
				for (int i = 0; i < lstpotabilizacion_enc_m50.size(); i++)
	            {
					int count = 0;
					V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean  = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50.get(i);
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron  = (V_padron_bean)lstpadron.get(j);
						if(padron.getCodprov().equals(potabilizacion_enc_m50_bean.getProvincia()) &&
								padron.getCodmunic().equals(potabilizacion_enc_m50_bean.getMunicipio()) && 
								padron.getTotal_poblacion_a1() > 50000){
							count ++;
						}
		            }
					if(count == 0){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro19.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(potabilizacion_enc_m50_bean.getClave() + potabilizacion_enc_m50_bean.getProvincia()+ 
								 potabilizacion_enc_m50_bean.getMunicipio() + potabilizacion_enc_m50_bean.getOrden_trat() +"\t");
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
				for (int i = 0; i < lstpotabilizacion_enc_m50_distinct.size(); i++)
	            {
					int count = 0;
					V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean  = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50_distinct.get(i);
					for (int j = 0; j < lstmun_enc_dis.size(); j++)
		            {
						V_mun_enc_dis_bean munEncDis  = (V_mun_enc_dis_bean)lstmun_enc_dis.get(j);
						if(munEncDis.getCodprov().equals(potabilizacion_enc_m50_bean.getProvincia()) &&
								munEncDis.getCodmunic().equals(potabilizacion_enc_m50_bean.getMunicipio())){
							count ++;
							
						}
						
		            }
					
					if(count != 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro19.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(potabilizacion_enc_m50_bean.getProvincia()+ potabilizacion_enc_m50_bean.getMunicipio() +"\t");
						 str.append("\n");
	
					}		
					
	            }
			}
			 str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro19.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
