package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro04 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuffer str1 = new StringBuffer();
		StringBuffer str2 = new StringBuffer();
		StringBuffer str3 = new StringBuffer();
		StringBuffer str4 = new StringBuffer();
		StringBuffer str5 = new StringBuffer();
		int contTexto = 0;
	    int contTexto1 = 0;
	    int contTexto2 = 0;
	    int contTexto3 = 0;
	    int contTexto4 = 0;
	    int contTexto5 = 0;
	    boolean error1 = false;
	    boolean error2 = false;
	    boolean error3 = false;
	    boolean error4 = false;
	    boolean error5 = false;
	    
		try
        {
			str.append(Messages.getString("cuadro04") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_TRAMO_CARRETERA";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList lsttramo_carretera = new ArrayList();
			while (rs.next()) {	
				V_tramo_carretera_bean  tramo_carretera_bean = new V_tramo_carretera_bean();

				tramo_carretera_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_carretera_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_carretera_bean.setCod_carrt(rs.getString("COD_CARRT"));
				if(rs.getString("PK_INICIAL")!=null&&!rs.getString("PK_INICIAL").equals(""))		
					tramo_carretera_bean.setPk_inicial(new Double(rs.getString("PK_INICIAL")));
				else
					tramo_carretera_bean.setPk_inicial(0.0);
				if(rs.getString("PK_FINAL")!=null&&!rs.getString("PK_FINAL").equals(""))		
					tramo_carretera_bean.setPk_final(new Double(rs.getString("PK_FINAL")));
				else
					tramo_carretera_bean.setPk_final(0.0);
				tramo_carretera_bean.setTitular(rs.getString("TITULAR"));
				tramo_carretera_bean.setGestion(rs.getString("GESTION"));
				tramo_carretera_bean.setSenaliza(rs.getString("SENALIZA"));
				tramo_carretera_bean.setFirme(rs.getString("FIRME"));
				tramo_carretera_bean.setEstado(rs.getString("ESTADO"));
				tramo_carretera_bean.setAncho(new Double(rs.getString("ANCHO")));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))		
					tramo_carretera_bean.setLongitud(new Double(rs.getString("LONGITUD")));	
				else
					tramo_carretera_bean.setLongitud(0.0);	
				if(rs.getString("PASOS_NIVE")!=null&&!rs.getString("PASOS_NIVE").equals(""))		
					tramo_carretera_bean.setPasos_nive(new Integer(rs.getString("PASOS_NIVE")));
				else
					tramo_carretera_bean.setPasos_nive(0);
				tramo_carretera_bean.setDimensiona(rs.getString("DIMENSIONA"));
				tramo_carretera_bean.setMuy_sinuos(rs.getString("MUY_SINUOS"));
				tramo_carretera_bean.setPte_excesi(rs.getString("PTE_EXCESI"));
				tramo_carretera_bean.setFre_estrec(rs.getString("FRE_ESTREC"));
				
				lsttramo_carretera.add(tramo_carretera_bean);
			}
			
			
			
			for (int i = 0; i < lsttramo_carretera.size(); i++)
            {
				if(lstValCuadros.contains("v01")){
					//ERROR DEL MPT -> (V_01)
					 if(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getPk_inicial() >= 
						 ((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getPk_final()){
						 if (contTexto1 == 0)
						 {
							 str1.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro04.V_01") + "\n");
							 str1.append("\n");
							 contTexto1++;
						 }
						 str1.append(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia()+((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio() + 
		                		"-(" +((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getCod_carrt() + ")"+ "\t");
						 error1 = true;
					 }
				}
				 
				if(lstValCuadros.contains("v02")){
					//ERROR DEL MPT -> (V_02)
					 double diferencia =((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getPk_final() -
						((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getPk_inicial();
					if(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getLongitud() !=  diferencia){
						 if (contTexto2 == 0)
						 {
							 str2.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro04.V_02") + "\n");
							 str2.append("\n");
							 contTexto2++;
						 }
						 str2.append(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia()+((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio() + 
					 		"-(" +((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getCod_carrt() + ")"+ "\t");
						 error2 = true;
						
					}
				}
				if(lstValCuadros.contains("v03")){
					//ERROR DEL MPT -> (V_03)
					if(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getLongitud() == 0){
						
						if(!((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getEstado().equals("E")){
							 if (contTexto3 == 0)
							 {
								 str3.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro04.V_03") + "\n");
								 str3.append("\n");
								 contTexto3++;
							 }
							 str3.append(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia()+((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio() + 
			                		"-(" +((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getCod_carrt() + ")"+ "\t");
							 error3 = true;
						}
					}
				}
				if(lstValCuadros.contains("v04")){
					//ERROR DEL MPT -> (V_04)
					if(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getAncho() < 0){
						
						
						 if (contTexto4 == 0)
						 {
							 str4.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro04.V_04") + "\n");
							 str4.append("\n");
							 contTexto4++;
						 }
						 str4.append(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia()+((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio() + 
		                		"-(" +((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getCod_carrt() + ")"+ "\t");
						 error4 = true;
	
					}
				}
				if(lstValCuadros.contains("v01")){
					//ERROR DEL MPT -> (V_05)
					if(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getAncho() == 0){
						
						if(!((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getEstado().equals("E")){
							 if (contTexto5 == 0)
							 {
								 str5.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro04.V_05") + "\n");
								 str5.append("\n");
								 contTexto5++;
							 }
							 str5.append(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia()+((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio() + 
			                		"-(" +((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getCod_carrt() + ")"+ "\t");
							 error5 = true;
						}
					}
				} 
            }
			str.append(str1.toString());
			if (error1)
				str.append("\n\n");
			str.append(str2.toString());
			if (error2)
				str.append("\n\n");
			str.append(str3.toString());
			if (error3)
				str.append("\n\n");
			str.append(str4.toString());
			if (error4)
				str.append("\n\n");
			str.append(str5.toString());
			if (error5)
				str.append("\n\n");
 
			contTexto = 0;
			
			
			ArrayList lstTabla1 = new ArrayList();
			for (int i = 0; i < lsttramo_carretera.size(); i++)
            {
				CodIne_bean codIne_bean = new CodIne_bean();
				
				codIne_bean.setProvincia(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getProvincia());
				codIne_bean.setMunicipio(((V_tramo_carretera_bean)lsttramo_carretera.get(i)).getMunicipio());
				
				lstTabla1.add(codIne_bean);
            }
			
			if(lstValCuadros.contains("v06")){
				//ERROR DEL MPT -> (V_06) 
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro04.V_06", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07) 
				
				sql = "select * from v_NUCL_ENCUESTADO_1";
				
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				ArrayList lstNuclEncuestado = new ArrayList();
				while (rs.next()) {	
					V_nucl_encuestado_1_bean  nucl_encuestado_1_bean = new V_nucl_encuestado_1_bean();
	
					nucl_encuestado_1_bean.setProvincia(rs.getString("PROVINCIA"));
					nucl_encuestado_1_bean.setMunicipio(rs.getString("MUNICIPIO"));
					nucl_encuestado_1_bean.setEntidad(rs.getString("ENTIDAD"));
					nucl_encuestado_1_bean.setNucleo(rs.getString("NUCLEO"));
					nucl_encuestado_1_bean.setPadron(new Integer(rs.getString("PADRON")));
					nucl_encuestado_1_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
					nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
					nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
					nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
					nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
					nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));
	
					
					lstNuclEncuestado.add(nucl_encuestado_1_bean);
		
				}
				
				sql = "SELECT * FROM v_MUN_ENC_DIS";
				
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				
				ArrayList lstNumEncDis = new ArrayList();
				while (rs.next()) {		
					V_mun_enc_dis_bean munEncDis = new V_mun_enc_dis_bean();
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
					
					lstNumEncDis.add(munEncDis);
		
				}
				
				
				lstTabla1 = new ArrayList();
				for (int i = 0; i < lstNuclEncuestado.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getMunicipio());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_TRAMO_CARRETERA", "cuadro04.V_07", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
				
				lstTabla1 = new ArrayList();
				for (int i = 0; i < lstNumEncDis.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_mun_enc_dis_bean)lstNumEncDis.get(i)).getCodprov());
					codIne_bean.setMunicipio(((V_mun_enc_dis_bean)lstNumEncDis.get(i)).getCodmunic());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_TRAMO_CARRETERA", "cuadro04.V_07_1", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro04.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
