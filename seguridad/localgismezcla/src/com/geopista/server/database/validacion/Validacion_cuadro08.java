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
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro08 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro08") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CAPTACION_ENC_M50";
			String sql_1 = "select * from v_PADRON";
			String sql_2 = "select * from v_MUN_ENC_DIS";
			String sql_3 = "select * from v_CAP_AGUA_NUCLEO";
			
			ArrayList lstcaptacion_enc_m50 = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstmun_enc_dis = new ArrayList();
			ArrayList lstcap_agua_nucleo = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_captacion_enc_m50_bean captacion_enc_m50_bean = new V_captacion_enc_m50_bean();

				captacion_enc_m50_bean.setProvincia(rs.getString("provincia"));
				captacion_enc_m50_bean.setMunicipio(rs.getString("municipio"));
				captacion_enc_m50_bean.setClave(rs.getString("clave"));
				captacion_enc_m50_bean.setOrden_capt(rs.getString("orden_capt"));
				captacion_enc_m50_bean.setDenominaci(rs.getString("denominaci"));
				captacion_enc_m50_bean.setTipo_capt(rs.getString("tipo_capt"));
				captacion_enc_m50_bean.setTitular(rs.getString("titular"));
				captacion_enc_m50_bean.setGestion(rs.getString("gestion"));
				captacion_enc_m50_bean.setSistema_ca(rs.getString("sistema_ca"));
				captacion_enc_m50_bean.setEstado(rs.getString("estado"));
				captacion_enc_m50_bean.setUso(rs.getString("uso"));
				captacion_enc_m50_bean.setProteccion(rs.getString("proteccion"));
				captacion_enc_m50_bean.setContador(rs.getString("contador"));

				lstcaptacion_enc_m50.add(captacion_enc_m50_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
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
		
			ps = connection.prepareStatement(sql_2);
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
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_cap_agua_nucleo_bean cap_agua_nucleoBean = new V_cap_agua_nucleo_bean();

				cap_agua_nucleoBean.setProvincia(rs.getString("provincia"));
				cap_agua_nucleoBean.setMunicipio(rs.getString("municipio"));
				cap_agua_nucleoBean.setEntidad(rs.getString("entidad"));
				cap_agua_nucleoBean.setNucleo(rs.getString("nucleo"));
				cap_agua_nucleoBean.setClave(rs.getString("clave"));
				cap_agua_nucleoBean.setC_provinc(rs.getString("c_provinc"));
				cap_agua_nucleoBean.setC_municip(rs.getString("c_municip"));
				cap_agua_nucleoBean.setOrden_capt(rs.getString("orden_capt"));
		
				lstcap_agua_nucleo.add(cap_agua_nucleoBean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstcaptacion_enc_m50.size(); i++)
	            {
					V_captacion_enc_m50_bean captacion_enc_m50_bean = (V_captacion_enc_m50_bean)lstcaptacion_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstcap_agua_nucleo.size(); j++)
		            {
						V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(j);
						if( cap_agua_nucleo.getC_provinc().equals(captacion_enc_m50_bean.getProvincia()) &&
								cap_agua_nucleo.getC_municip().equals(captacion_enc_m50_bean.getMunicipio()) &&
								cap_agua_nucleo.getOrden_capt().equals(captacion_enc_m50_bean.getOrden_capt())){
							count ++;
						}
		            }
					
					if (count == 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro08.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(captacion_enc_m50_bean.getClave() + captacion_enc_m50_bean.getProvincia()+ 
								 captacion_enc_m50_bean.getMunicipio() + captacion_enc_m50_bean.getOrden_capt() +"\t");
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
				for (int i = 0; i < lstcaptacion_enc_m50.size(); i++)
	            {
					V_captacion_enc_m50_bean captacion_enc_m50_bean = (V_captacion_enc_m50_bean)lstcaptacion_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron_bean = (V_padron_bean)lstpadron.get(j);
						if( padron_bean.getCodprov().equals(captacion_enc_m50_bean.getProvincia()) &&
								padron_bean.getCodmunic().equals(captacion_enc_m50_bean.getMunicipio()) &&
								padron_bean.getTotal_poblacion_a1() > 50000){
							count ++;
						}
		            }
					
					if (count == 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro08.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(captacion_enc_m50_bean.getClave() + captacion_enc_m50_bean.getProvincia()+ 
								 captacion_enc_m50_bean.getMunicipio() + captacion_enc_m50_bean.getOrden_capt() +"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_03)
				ArrayList lstDistcintcaptacion_enc_m50 = new ArrayList();
				sql = "select DISTINCT PROVINCIA, MUNICIPIO  from v_CAPTACION_ENC_M50";
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {	
					V_captacion_enc_m50_bean captacion_enc_m50_bean = new V_captacion_enc_m50_bean();
					captacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
					captacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
	
					lstDistcintcaptacion_enc_m50.add(captacion_enc_m50_bean);
					
				}
				
				for (int i = 0; i < lstDistcintcaptacion_enc_m50.size(); i++)
	            {
					V_captacion_enc_m50_bean captacion_enc_m50_bean = (V_captacion_enc_m50_bean)lstDistcintcaptacion_enc_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstmun_enc_dis.size(); j++)
		            {
						V_mun_enc_dis_bean mun_enc_dis_bean = (V_mun_enc_dis_bean)lstmun_enc_dis.get(j);
						if( mun_enc_dis_bean.getCodprov().equals(captacion_enc_m50_bean.getProvincia()) &&
								mun_enc_dis_bean.getCodmunic().equals(captacion_enc_m50_bean.getMunicipio())){
							count ++;
						}
		            }
					
					if (count != 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro08.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( captacion_enc_m50_bean.getProvincia()+ 
								 captacion_enc_m50_bean.getMunicipio()  +"\t");
						 str.append("\t");
					}
	            }
			}
			str.append("\n\n");
	
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro08.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
