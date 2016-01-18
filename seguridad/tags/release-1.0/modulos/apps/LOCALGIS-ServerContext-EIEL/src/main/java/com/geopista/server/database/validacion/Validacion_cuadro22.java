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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
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

public class Validacion_cuadro22 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {       
			str.append(Messages.getString("cuadro22") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_NUCL_ENCUESTADO_3";
			String sql_filtro = "select * from v_NUCL_ENCUESTADO_3 WHERE (aag_v_cone + aag_v_ncon) > 0";
			String sql_1 = "select * from v_RED_DISTRIBUCION";
			String sql_2 = "select * from v_CAP_AGUA_NUCLEO";
			String sql_3 = "select * from v_NUCL_ENCUESTADO_1";		
			String sql_4 = "select * from v_NUCL_ENCUESTADO_4";
			String sql_5 = "select * from v_INFRAESTR_VIARIA";
		
			ArrayList lstnucl_encuestado_3 = new ArrayList();
			ArrayList lstnucl_encuestado_3_filtro = new ArrayList();
			ArrayList lstred_distribucion = new ArrayList();
			ArrayList lstcap_agua_nucleo = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstnucl_encuestado_4 = new ArrayList();
			ArrayList lstinfraestr_viaria = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_3_bean  nucl_encuestado_3_bean = new V_nucl_encuestado_3_bean();

				nucl_encuestado_3_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_3_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_3_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_3_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_3_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
				nucl_encuestado_3_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
				nucl_encuestado_3_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
				nucl_encuestado_3_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
				nucl_encuestado_3_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
				nucl_encuestado_3_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
				nucl_encuestado_3_bean.setAag_perdid(new Integer(rs.getString("AAG_PERDID")));
				nucl_encuestado_3_bean.setAag_calida(rs.getString("AAG_CALIDA"));
				nucl_encuestado_3_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
				nucl_encuestado_3_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
				nucl_encuestado_3_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
				nucl_encuestado_3_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
				lstnucl_encuestado_3.add(nucl_encuestado_3_bean);
				
			}
			

			ps = connection.prepareStatement(sql_filtro);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_3_bean  nucl_encuestado_3_bean = new V_nucl_encuestado_3_bean();

				nucl_encuestado_3_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_3_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_3_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_3_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_3_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
				nucl_encuestado_3_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
				nucl_encuestado_3_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
				nucl_encuestado_3_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
				nucl_encuestado_3_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
				nucl_encuestado_3_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
				nucl_encuestado_3_bean.setAag_perdid(new Integer(rs.getString("AAG_PERDID")));
				nucl_encuestado_3_bean.setAag_calida(rs.getString("AAG_CALIDA"));
				nucl_encuestado_3_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
				nucl_encuestado_3_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
				nucl_encuestado_3_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
				nucl_encuestado_3_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
				lstnucl_encuestado_3_filtro.add(nucl_encuestado_3_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_red_distribucion_bean red_distribucion_bean = new V_red_distribucion_bean();

				red_distribucion_bean.setProvincia(rs.getString("PROVINCIA"));
				red_distribucion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				red_distribucion_bean.setEntidad(rs.getString("ENTIDAD"));
				red_distribucion_bean.setNucleo(rs.getString("NUCLEO"));
				red_distribucion_bean.setTipo_rdis(rs.getString("TIPO_RDIS"));
				red_distribucion_bean.setSist_trans(rs.getString("SIST_TRANS"));
				red_distribucion_bean.setEstado(rs.getString("ESTADO"));
				red_distribucion_bean.setTitular(rs.getString("TITULAR"));
				red_distribucion_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					red_distribucion_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					red_distribucion_bean.setLongitud(0);	
				
				lstred_distribucion.add(red_distribucion_bean);
				
			}
			
			
			 ps = connection.prepareStatement(sql_2);
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
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
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

				
				lstnucl_encuestado_1.add(nucl_encuestado_1_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_4_bean  nucl_encuestado_4_bean = new V_nucl_encuestado_4_bean();

				nucl_encuestado_4_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_4_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_4_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_4_bean.setNucleo(rs.getString("NUCLEO"));
				if(rs.getString("AAU_VIVIEN")!=null&&!rs.getString("AAU_VIVIEN").equals("")){
					nucl_encuestado_4_bean.setAau_vivien(new Integer(rs.getString("AAU_VIVIEN")));
				}else
					nucl_encuestado_4_bean.setAau_vivien(0);
				if(rs.getString("AAU_POB_RE")!=null&&!rs.getString("AAU_POB_RE").equals("")){
					nucl_encuestado_4_bean.setAau_pob_re(new Integer(rs.getString("AAU_POB_RE")));
				}else
					nucl_encuestado_4_bean.setAau_pob_re(0);
				if(rs.getString("AAU_POB_ES")!=null&&!rs.getString("AAU_POB_ES").equals("")){
					nucl_encuestado_4_bean.setAau_pob_es(new Integer(rs.getString("AAU_POB_ES")));
				}else	
					nucl_encuestado_4_bean.setAau_pob_es(0);
				if(rs.getString("AAU_DEF_VI")!=null&&!rs.getString("AAU_DEF_VI").equals("")){
					nucl_encuestado_4_bean.setAau_def_vi(new Integer(rs.getString("AAU_DEF_VI")));
				}else
					nucl_encuestado_4_bean.setAau_def_vi(0);
				if(rs.getString("AAU_DEF_RE")!=null&&!rs.getString("AAU_DEF_RE").equals("")){
					nucl_encuestado_4_bean.setAau_def_re(new Integer(rs.getString("AAU_DEF_RE")));
				}else
					nucl_encuestado_4_bean.setAau_def_re(0);
				if(rs.getString("AAU_DEF_ES")!=null&&!rs.getString("AAU_DEF_ES").equals("")){
					nucl_encuestado_4_bean.setAau_def_es(new Integer(rs.getString("AAU_DEF_ES")));
				}else
					nucl_encuestado_4_bean.setAau_def_es(0);
				if(rs.getString("AAU_FECONT")!=null&&!rs.getString("AAU_FECONT").equals("")){
					nucl_encuestado_4_bean.setAau_fecont(new Integer(rs.getString("AAU_FECONT")));
				}else
					nucl_encuestado_4_bean.setAau_fecont(0);
				if(rs.getString("AAU_FENCON")!=null&&!rs.getString("AAU_FENCON").equals("")){
					nucl_encuestado_4_bean.setAau_fencon(new Integer(rs.getString("AAU_FENCON")));
				}else
					nucl_encuestado_4_bean.setAau_fencon(0);

				nucl_encuestado_4_bean.setAau_caudal(rs.getString("AAU_CAUDAL"));


				
				lstnucl_encuestado_4.add(nucl_encuestado_4_bean);
				
			}
				
			ps = connection.prepareStatement(sql_5);
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
				infraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));

			
				lstinfraestr_viaria.add(infraestr_viaria_bean);
			}
		
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstnucl_encuestado_3_filtro.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3_filtro.get(i);
					for (int j = 0; j < lstred_distribucion.size(); j++)
		            {
						
						V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(j);
						if (red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							count ++;
							
						}
						
		            }
					if (count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstnucl_encuestado_3_filtro.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3_filtro.get(i);
					for (int j = 0; j < lstcap_agua_nucleo.size(); j++)
		            {
						
						V_cap_agua_nucleo_bean cap_agua_nucleo   = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(j);
						if (cap_agua_nucleo.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								cap_agua_nucleo.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								cap_agua_nucleo.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								cap_agua_nucleo.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							count ++;
							
						}	
		            }
					
					if (count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				int sumaViviendas = 0;
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					sumaViviendas = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					sumaViviendas = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon() +
										nucl_encuestado_3_bean.getAag_v_defi();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							
							if(sumaViviendas > nucl_encuestado_1_bean.getViv_total()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_03") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					int count =0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);	
					for (int j = 0; j < lstred_distribucion.size(); j++)
					{
						V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(j);
						if (red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
							red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
							red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
							red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							count ++;
						}
					 	
					}
					if (count == 0){ //no existe en Red de distribucion
						 for (int h = 0; h < lstnucl_encuestado_4.size(); h++)
				         {
							 V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(h);
							 if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
										nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
										nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
										nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) &&
										(nucl_encuestado_4_bean.getAau_vivien() + nucl_encuestado_4_bean.getAau_pob_re() + nucl_encuestado_4_bean.getAau_pob_es() +
												nucl_encuestado_4_bean.getAau_def_vi() + nucl_encuestado_4_bean.getAau_def_re() + nucl_encuestado_4_bean.getAau_def_re()) == 0){
	
	
								for (int g = 0; g < lstnucl_encuestado_1.size(); g++)
								{
									V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(g);
									
									if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
											nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
											nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
											nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
										if(nucl_encuestado_3_bean.getAag_v_defi() != nucl_encuestado_1_bean.getViv_total()){
											
											if (contTexto == 0)
											 {
												 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_04") + "\n");
												 str.append("\n");
												 contTexto++;
											 }
											 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
													 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
											 error = true;
											
										}
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
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int padron = 0;
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							padron = nucl_encuestado_1_bean.getPadron();
						}
						
		            }
					int count = 0;
					for (int j = 0; j < lstred_distribucion.size(); j++)
		            {
						
						V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(j);
						if (red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
							red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
							red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
							red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							count ++;
						}
						
		            }
					
					if (count == 0){//no existe en Red de distribucion
						
						for (int h = 0; h < lstnucl_encuestado_4.size(); h++)
			            {
							V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(h);
							if ((nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
									nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())) && 
									(nucl_encuestado_4_bean.getAau_vivien() + nucl_encuestado_4_bean.getAau_pob_re() + nucl_encuestado_4_bean.getAau_pob_es() +
											nucl_encuestado_4_bean.getAau_def_vi() + nucl_encuestado_4_bean.getAau_def_re() + nucl_encuestado_4_bean.getAau_def_es() == 0)){
						
								if(nucl_encuestado_3_bean.getAag_pr_def() != padron){
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_05") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
											 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v06")){
				//ERROR DEL MPT -> (V_06)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int pob_estaci = 0;
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							pob_estaci = nucl_encuestado_1_bean.getPob_estaci();
						}
						
		            }
					int count = 0;
					for (int j = 0; j < lstred_distribucion.size(); j++)
		            {
						
						V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(j);
						if (red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
							red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
							red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
							red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
						count ++;
						}
						
		            }
					if (count == 0){//no existe en Red de distribucion
						
						for (int h = 0; h < lstnucl_encuestado_4.size(); h++)
			            {
							V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(h);
							if ((nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
									nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())) && 
									(nucl_encuestado_4_bean.getAau_vivien() + nucl_encuestado_4_bean.getAau_pob_re() + nucl_encuestado_4_bean.getAau_pob_es() +
											nucl_encuestado_4_bean.getAau_def_vi() + nucl_encuestado_4_bean.getAau_def_re() + nucl_encuestado_4_bean.getAau_def_es() == 0)){
						
								if(nucl_encuestado_3_bean.getAag_pr_def() != pob_estaci){
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_06") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
											 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int suma = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon();
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
	
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							
							if(suma == nucl_encuestado_1_bean.getViv_total()){
								
								if((nucl_encuestado_3_bean.getAag_l_defi() + nucl_encuestado_3_bean.getAag_v_defi() +
										nucl_encuestado_3_bean.getAag_pr_def() + nucl_encuestado_3_bean.getAag_pr_def()) != 0){
									
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_07") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
											 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v08")){
				//ERROR DEL MPT -> (V_08)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int suma = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon() +
									nucl_encuestado_3_bean.getAag_v_defi();
					
					int aau_def_vi = 0;
					int viv_total = 0;
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							aau_def_vi = nucl_encuestado_4_bean.getAau_def_vi();
						}
		            }
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							viv_total = nucl_encuestado_1_bean.getViv_total();
						}
		            }
					
					if((suma + aau_def_vi) > viv_total){
	
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_08") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v09")){
				//ERROR DEL MPT -> (V_09)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int suma = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon() +
									nucl_encuestado_3_bean.getAag_v_defi();
					
					int aau_vivien = 0;
					int aau_pob_re = 0;
					int aau_pob_es = 0;
					int aau_def_vi = 0;
					int aau_def_re = 0;
					int aau_def_es = 0;
					int viv_total = 0;
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							
							aau_vivien = nucl_encuestado_4_bean.getAau_vivien(); 
							aau_pob_re = nucl_encuestado_4_bean.getAau_pob_re();
							aau_pob_es = nucl_encuestado_4_bean.getAau_pob_es();
							aau_def_vi = nucl_encuestado_4_bean.getAau_def_vi();
							aau_def_re = nucl_encuestado_4_bean.getAau_def_re();
							aau_def_es = nucl_encuestado_4_bean.getAau_def_es();
							
						}
		            }
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							viv_total = nucl_encuestado_1_bean.getViv_total();
						}
		            }
					
					if(suma == viv_total){
	
						if( (aau_vivien + aau_pob_re + aau_pob_es + aau_def_vi + aau_def_re + aau_def_es) != 0){
	
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_09") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
									 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
							 error = true;
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
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					int count = 0;
					int aau_def_vi = 0;
					int viv_total = 0;
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean   = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							aau_def_vi = nucl_encuestado_4_bean.getAau_def_vi();
						}
		            }
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							viv_total = nucl_encuestado_1_bean.getViv_total();
						}
		            }
					
					for (int j = 0; j < lstred_distribucion.size(); j++)
		            {
						
						V_red_distribucion_bean red_distribucion_bean  = (V_red_distribucion_bean)lstred_distribucion.get(j);
						if (red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							count ++;
						}
	
		            }
					
					if(count == 0){//no existe en Red de distribucion
						
						if((aau_def_vi + nucl_encuestado_3_bean.getAag_v_defi()) != viv_total){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_10") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
									 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
							 error = true;
						}
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v11")){
				 //ERROR DEL MPT -> (V_11)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if(nucl_encuestado_3_bean.getAag_c_invi() > nucl_encuestado_3_bean.getAag_c_vera()){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro22.V_11") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
						 error = true;
					}
				
	            }
				str.append("\n\n");
				contTexto = 0;
			}
			if(lstValCuadros.contains("v12")){
				//ERROR DEL MPT -> (V_12)	
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean  = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						if (nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_1_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_1_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( (nucl_encuestado_3_bean.getAag_v_expr() + nucl_encuestado_3_bean.getAag_v_depr()) >
										nucl_encuestado_1_bean.getViv_total()){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_12") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v13")){
				 //ERROR DEL MPT -> (V_13)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_v_defi() ==  nucl_encuestado_4_bean.getAau_def_vi() &&
												nucl_encuestado_3_bean.getAag_v_defi() == 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro22.falsoerror.V_1") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
								 error = true;
							}
						}
		            }
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			
			
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_v_defi() ==  nucl_encuestado_4_bean.getAau_def_vi() &&
												nucl_encuestado_3_bean.getAag_v_defi() != 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_13") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			
			
			if(lstValCuadros.contains("v14")){
				//ERROR DEL MPT -> (V_14)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_pr_def() ==  nucl_encuestado_4_bean.getAau_def_re() &&
												nucl_encuestado_3_bean.getAag_pr_def() == 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro22.falsoerror.V_2") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
								 error = true;
							}
						}
		            }
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
				
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_pr_def() ==  nucl_encuestado_4_bean.getAau_def_re() &&
												nucl_encuestado_3_bean.getAag_pr_def() != 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_14") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			
			if(lstValCuadros.contains("v15")){
				//ERROR DEL MPT -> (V_15)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_pe_def() ==  nucl_encuestado_4_bean.getAau_def_es() &&
												nucl_encuestado_3_bean.getAag_pe_def() == 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro22.falsoerror.V_3") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
								 error = true;
							}
						}
		            }
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						if (nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
	
							if( nucl_encuestado_3_bean.getAag_pe_def() ==  nucl_encuestado_4_bean.getAau_def_es() &&
												nucl_encuestado_3_bean.getAag_pe_def() == 0){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_15") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
										 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v16")){
				//ERROR DEL MPT -> (V_16)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if (nucl_encuestado_3_bean.getAag_v_defi() > 0  && 
							nucl_encuestado_3_bean.getAag_l_defi() <= 0  ){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_16") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v17")){
				//ERROR DEL MPT -> (V_17)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if (nucl_encuestado_3_bean.getAag_l_defi() > 0  && 
							(nucl_encuestado_3_bean.getAag_v_defi() <= 0  || nucl_encuestado_3_bean.getAag_pe_def() <= 0)){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_17") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v18")){
				//ERROR DEL MPT -> (V_18)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					int suma = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					for (int j = 0; j < lstinfraestr_viaria.size(); j++)
		            {
						V_infraestr_viaria_bean infraestr_viaria  = (V_infraestr_viaria_bean)lstinfraestr_viaria.get(j);
						if (infraestr_viaria.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) && 
								infraestr_viaria.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								infraestr_viaria.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								infraestr_viaria.getNucleo().equals(nucl_encuestado_3_bean.getNucleo()) ){
							
							suma +=  infraestr_viaria.getLongitud();
						}
		            }
					
					if(nucl_encuestado_3_bean.getAag_l_defi() >= suma){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_18") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if(nucl_encuestado_3_bean.getAag_v_defi() > 0 && nucl_encuestado_3_bean.getAag_pe_def() <= 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_19") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i)).getMunicipio());
					codIne_bean.setEntidad(((V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i)).getEntidad());
					codIne_bean.setNucleo(((V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i)).getNucleo());
					
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro22.V_20", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			if(lstValCuadros.contains("v21")){
				//ERROR DEL MPT -> (V_21)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if(nucl_encuestado_3_bean.getAag_perdid() > 100){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_21") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean   = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					if(nucl_encuestado_3_bean.getAag_calida().equals("NO") && (nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) != 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro22.V_22") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_3_bean.getProvincia() + nucl_encuestado_3_bean.getMunicipio() +
								 nucl_encuestado_3_bean.getEntidad() + nucl_encuestado_3_bean.getNucleo() + "\t");
						 
					}
	            }
			}
			str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro22.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
