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
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro27 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro27") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_COLECTOR_ENC_M50";
			String sql_1 = "select * from v_COLECTOR_NUCLEO";
			String sql_2 = "select * from v_TRAMO_COLECTOR_M50";
			String sql_3 = "select * from v_PADRON";
			String sql_4 = "select * from v_MUN_ENC_DIS";

			ArrayList lstcolector_enc_m50 = new ArrayList();
			ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lsttramo_colector_m50 = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstmun_enc_dis = new ArrayList();
			

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_enc_m50_bean colectorEncM50Bean = new V_colector_enc_m50_bean();
				
				colectorEncM50Bean.setClave(rs.getString("clave"));
				colectorEncM50Bean.setProvincia(rs.getString("provincia"));
				colectorEncM50Bean.setMunicipio(rs.getString("municipio"));
				colectorEncM50Bean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_enc_m50.add(colectorEncM50Bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
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
			
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_colector_m50_bean tramo_colector_m50_bean = new V_tramo_colector_m50_bean();

				tramo_colector_m50_bean.setClave(rs.getString("CLAVE"));
				tramo_colector_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_colector_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_colector_m50_bean.setOrden_cole(rs.getString("ORDEN_COLE"));
				tramo_colector_m50_bean.setTipo_colec(rs.getString("TIPO_COLEC"));
				tramo_colector_m50_bean.setSist_trans(rs.getString("SIST_TRANS"));
				tramo_colector_m50_bean.setEstado(rs.getString("ESTADO"));
				tramo_colector_m50_bean.setTitular(rs.getString("TITULAR"));
				tramo_colector_m50_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONG_TRAMO")!=null&&!rs.getString("LONG_TRAMO").equals(""))
					tramo_colector_m50_bean.setLong_tramo(new Double(Math.rint(new Double(rs.getString("LONG_TRAMO")))).intValue());
				else
					tramo_colector_m50_bean.setLong_tramo(0);
				lsttramo_colector_m50.add(tramo_colector_m50_bean);
				
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
				for (int i = 0; i < lstcolector_enc_m50.size(); i++)
	            {
					V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean   = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getC_provinci().equals(colector_nucleo_bean.getProvincia()) && 
								colector_nucleo_bean.getC_municipi().equals(colector_nucleo_bean.getMunicipio()) && 
								colector_nucleo_bean.getOrden_cole().equals(colector_nucleo_bean.getOrden_cole() )){
							count ++;
						}
					}
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro27.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_m50_bean.getClave() + colector_enc_m50_bean.getProvincia() + 
								 colector_enc_m50_bean.getMunicipio() + colector_enc_m50_bean.getOrden_cole() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
		
				for (int i = 0; i < lstcolector_enc_m50.size(); i++)
	            {
					V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(i);
					if(colector_enc_m50_bean.getOrden_cole().length() < 3){
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro27.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_m50_bean.getClave() + colector_enc_m50_bean.getProvincia() + 
								 colector_enc_m50_bean.getMunicipio() + colector_enc_m50_bean.getOrden_cole() + "\t");
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
				ArrayList lst = new ArrayList();
				for (int i = 0; i < lstcolector_enc_m50.size(); i++)
	            {
					V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(i);
					for (int j = 0; j < lstcolector_enc_m50.size(); j++)
		            {
						V_colector_enc_m50_bean colector_enc_m50_bean_1   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(j);
						
						if(colector_enc_m50_bean_1.getProvincia().equals(colector_enc_m50_bean.getProvincia()) &&
								colector_enc_m50_bean_1.getMunicipio().equals(colector_enc_m50_bean.getMunicipio()) &&
								colector_enc_m50_bean_1.getOrden_cole().equals(colector_enc_m50_bean.getOrden_cole())){
							lst.add(colector_enc_m50_bean_1);
						}
		            }
					
					for (int j = 0; j < lst.size(); j++){
						int count = 0;
						V_colector_enc_m50_bean colector_enc_m50_bean_lst   = (V_colector_enc_m50_bean)lst.get(j);
						for (int g = 0; g < lsttramo_colector_m50.size(); g++){
							V_tramo_colector_m50_bean tramo_colector_m50_bean = (V_tramo_colector_m50_bean)lsttramo_colector_m50.get(g);
							
							if(tramo_colector_m50_bean.getProvincia().equals(colector_enc_m50_bean_lst.getProvincia()) &&
									tramo_colector_m50_bean.getMunicipio().equals(colector_enc_m50_bean_lst.getMunicipio()) &&
									tramo_colector_m50_bean.getOrden_cole().equals(colector_enc_m50_bean_lst.getOrden_cole())){
								count ++;
							}
						}
						if(count == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro27.V_02") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( colector_enc_m50_bean_lst.getClave() + colector_enc_m50_bean_lst.getProvincia() + 
									 colector_enc_m50_bean_lst.getMunicipio() + colector_enc_m50_bean_lst.getOrden_cole() + "\t");
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
				for (int i = 0; i < lstcolector_enc_m50.size(); i++)
	            {
					V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron_bean   = (V_padron_bean)lstpadron.get(j);
						if(padron_bean.getCodprov().equals(colector_enc_m50_bean.getProvincia()) && 
								padron_bean.getCodmunic().equals(colector_enc_m50_bean.getMunicipio()) && 
								padron_bean.getTotal_poblacion_a1() > 50000 ){
							count ++;
						}
					}
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro27.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_m50_bean.getClave() + colector_enc_m50_bean.getProvincia() + 
								 colector_enc_m50_bean.getMunicipio() + colector_enc_m50_bean.getOrden_cole() + "\t");
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
				for (int i = 0; i < lstcolector_enc_m50.size(); i++)
	            {
					V_colector_enc_m50_bean colector_enc_m50_bean   = (V_colector_enc_m50_bean)lstcolector_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstmun_enc_dis.size(); j++)
		            {
						V_mun_enc_dis_bean mun_enc_dis_bean   = (V_mun_enc_dis_bean)lstmun_enc_dis.get(j);
						if(mun_enc_dis_bean.getCodprov().equals(colector_enc_m50_bean.getProvincia()) && 
								mun_enc_dis_bean.getCodmunic().equals(colector_enc_m50_bean.getMunicipio())){
							count ++;
						}
					}
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro27.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_m50_bean.getClave() + colector_enc_m50_bean.getProvincia() + 
								 colector_enc_m50_bean.getMunicipio() + colector_enc_m50_bean.getOrden_cole() + "\t");
					}
	            }
			}
			str.append("\n\n");

        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro27.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
