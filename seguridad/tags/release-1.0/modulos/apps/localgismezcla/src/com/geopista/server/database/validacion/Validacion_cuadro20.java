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

public class Validacion_cuadro20 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {       
			str.append(Messages.getString("cuadro20") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_RED_DISTRIBUCION";
			String sql_distinct = "select distinct PROVINCIA, MUNICIPIO, ENTIDAD, NUCLEO from v_RED_DISTRIBUCION";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_3";
			String sql_2 = "select * from v_CAP_AGUA_NUCLEO";
			String sql_3 = "select * from v_COND_AGUA_NUCLEO";
			String sql_4 = "select * from v_DEPOSITO_AGUA_NUCLEO";
			String sql_5 = "select * from v_TRAT_POTA_NUCLEO";
			String sql_6 = "select * from v_NUCL_ENCUESTADO_4";
			String sql_7 = "select * from v_NUCL_ENCUESTADO_2";
		
			ArrayList lstred_distribucion = new ArrayList();
			ArrayList lstred_distribucion_distinct = new ArrayList();
			ArrayList lstnucl_encuestado_3 = new ArrayList();
			ArrayList lstcap_agua_nucleo = new ArrayList();
			ArrayList lstcond_agua_nucleo = new ArrayList();
			ArrayList lstdeposito_agua_nucleo = new ArrayList();
			ArrayList lsttrat_pota_nucleo = new ArrayList();
			ArrayList lstnucl_encuestado_4 = new ArrayList();
			ArrayList lstnucl_encuestado_2 = new ArrayList();
			
		
			
			ps = connection.prepareStatement(sql);
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
		
			ps = connection.prepareStatement(sql_distinct);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_red_distribucion_bean red_distribucion_bean = new V_red_distribucion_bean();
				red_distribucion_bean.setProvincia(rs.getString("PROVINCIA"));
				red_distribucion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				red_distribucion_bean.setEntidad(rs.getString("ENTIDAD"));
				red_distribucion_bean.setNucleo(rs.getString("NUCLEO"));		
				
				lstred_distribucion_distinct.add(red_distribucion_bean);
				
			}
		
			ps = connection.prepareStatement(sql_1);
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
					V_cond_agua_nucleo_bean cond_agua_nucleo_bean = new V_cond_agua_nucleo_bean();

					cond_agua_nucleo_bean.setProvincia(rs.getString("provincia"));
					cond_agua_nucleo_bean.setMunicipio(rs.getString("municipio"));
					cond_agua_nucleo_bean.setEntidad(rs.getString("entidad"));
					cond_agua_nucleo_bean.setNucleo(rs.getString("nucleo"));
					cond_agua_nucleo_bean.setClave(rs.getString("clave"));
					cond_agua_nucleo_bean.setCond_provi(rs.getString("cond_provi"));
					cond_agua_nucleo_bean.setCond_munic(rs.getString("cond_munic"));
					cond_agua_nucleo_bean.setOrden_cond(rs.getString("orden_cond"));	

					lstcond_agua_nucleo.add(cond_agua_nucleo_bean);
					
				}
			
		
				ps = connection.prepareStatement(sql_4);
				rs = ps.executeQuery();
				while (rs.next()) {	
					V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean = new V_deposito_agua_nucleo_bean();
					deposito_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
					deposito_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
					deposito_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
					deposito_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
					deposito_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
					deposito_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));
					deposito_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));
					deposito_agua_nucleo_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
		
					lstdeposito_agua_nucleo.add(deposito_agua_nucleo_bean);
					
				}
			
			
				ps = connection.prepareStatement(sql_5);
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
			
			
				ps = connection.prepareStatement(sql_6);
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
			
				ps = connection.prepareStatement(sql_7);
				rs = ps.executeQuery();
				while (rs.next()) {	
					V_nucl_encuestado_2_bean  nucl_encuestado_2_bean = new V_nucl_encuestado_2_bean();

					nucl_encuestado_2_bean.setProvincia(rs.getString("PROVINCIA"));
					nucl_encuestado_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
					nucl_encuestado_2_bean.setEntidad(rs.getString("ENTIDAD"));
					nucl_encuestado_2_bean.setNucleo(rs.getString("NUCLEO"));
					nucl_encuestado_2_bean.setAag_caudal(rs.getString("AAG_CAUDAL"));
					nucl_encuestado_2_bean.setAag_restri(rs.getString("AAG_RESTRI"));
					nucl_encuestado_2_bean.setAag_contad(rs.getString("AAG_CONTAD"));
					nucl_encuestado_2_bean.setAag_tasa(rs.getString("AAG_TASA"));
					nucl_encuestado_2_bean.setAag_instal(rs.getString("AAG_INSTAL"));
					nucl_encuestado_2_bean.setAag_hidran(rs.getString("AAG_HIDRAN"));
					nucl_encuestado_2_bean.setAag_est_hi(rs.getString("AAG_EST_HI"));
					nucl_encuestado_2_bean.setAag_valvul(rs.getString("AAG_VALVUL"));
					nucl_encuestado_2_bean.setAag_est_va(rs.getString("AAG_EST_VA"));
					nucl_encuestado_2_bean.setAag_bocasr(rs.getString("AAG_BOCASR"));
					nucl_encuestado_2_bean.setAag_est_bo(rs.getString("AAG_EST_BO"));
					nucl_encuestado_2_bean.setCisterna(rs.getString("CISTERNA"));
					
					lstnucl_encuestado_2.add(nucl_encuestado_2_bean);
					
				}
				
		
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
	
					if( red_distribucion_bean.getLongitud() == 0  &&
							!red_distribucion_bean.getEstado().equals("E")){
	
									
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
								 red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
	
					for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
		            {
						V_nucl_encuestado_3_bean nucl_encuestado_3_bean = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
						
						if( nucl_encuestado_3_bean.getProvincia().equals(red_distribucion_bean.getProvincia())  &&
								nucl_encuestado_3_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) && 
								nucl_encuestado_3_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
								nucl_encuestado_3_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
										
							if ((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) <= 0)
							 {
								if (contTexto == 0)
								 {	
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_02") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
										 red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
	
					codIne_bean.setProvincia(((V_red_distribucion_bean)lstred_distribucion.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_red_distribucion_bean)lstred_distribucion.get(i)).getMunicipio());
					codIne_bean.setNucleo(((V_red_distribucion_bean)lstred_distribucion.get(i)).getNucleo());
					codIne_bean.setEntidad(((V_red_distribucion_bean)lstred_distribucion.get(i)).getEntidad());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_CAP_AGUA_NUCLEO", "cuadro20.V_03", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
	
				contTexto = 0;
			}
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04)
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					for (int j = 0; j < lstred_distribucion_distinct.size(); j++)
		            {
						V_red_distribucion_bean red_distribucion_bean  = (V_red_distribucion_bean)lstred_distribucion_distinct.get(j);
						if(red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
							count ++;
							
						}
						
		            }
					
					if(count == 0){
						if((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon() ) != 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_04") + "\n");
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
			if(lstValCuadros.contains("v05")){
			//ERROR DEL MPT -> (V_05)
				 //recorremos nucl_encuestado 3 y comprobamos si todos los registros estan en red_distribucion
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					for (int j = 0; j < lstred_distribucion_distinct.size(); j++)
		            {
						V_red_distribucion_bean red_distribucion_bean  = (V_red_distribucion_bean)lstred_distribucion_distinct.get(j);
						if(red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
							count ++;
							
						}
		            }
						
						//si no estan en red de distribucion comprobamos si esta en los demas cuadros
					if(count == 0){
						int cap_agua_nucleo = 0;
		                int cond_agua_nucleo = 0;
		                int deposito_agua_nucleo = 0;
		                int trat_pota_nucleo = 0;
	
						for (int h = 0; h < lstcap_agua_nucleo.size(); h++)
			            {
							V_cap_agua_nucleo_bean cap_agua_nucleo_bean  = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(h);
							if(cap_agua_nucleo_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
									cap_agua_nucleo_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									cap_agua_nucleo_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									cap_agua_nucleo_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
								cap_agua_nucleo ++;
								
							}
			            }
					
						for (int h = 0; h < lstcond_agua_nucleo.size(); h++)
			            {
							V_cond_agua_nucleo_bean cond_agua_nucleo_bean  = (V_cond_agua_nucleo_bean)lstcond_agua_nucleo.get(h);
							if(cond_agua_nucleo_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
									cond_agua_nucleo_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									cond_agua_nucleo_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									cond_agua_nucleo_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
								cond_agua_nucleo ++;
								
							}
			            }
						
						for (int h = 0; h < lstdeposito_agua_nucleo.size(); h++)
			            {
							V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean  = (V_deposito_agua_nucleo_bean)lstdeposito_agua_nucleo.get(h);
							if(deposito_agua_nucleo_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
									deposito_agua_nucleo_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									deposito_agua_nucleo_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									deposito_agua_nucleo_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
								deposito_agua_nucleo ++;
								
							}
			            }
						
						for (int h = 0; h < lsttrat_pota_nucleo.size(); h++)
			            {
							V_trat_pota_nucleo_bean trat_pota_nucleo_bean  = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(h);
							if(trat_pota_nucleo_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
									trat_pota_nucleo_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
									trat_pota_nucleo_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
									trat_pota_nucleo_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
								trat_pota_nucleo ++;
								
							}
			            }
						
						if ((cap_agua_nucleo != 0) || (cond_agua_nucleo != 0) || (deposito_agua_nucleo != 0) || (trat_pota_nucleo != 0))
	                    {
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_05") + "\n");
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
			if(lstValCuadros.contains("v06")){
				
				//ERROR DEL MPT -> (V_06)
				 //recorremos nucl_encuestado 3 y comprobamos si todos los registros estan en red_distribucion
				for (int i = 0; i < lstnucl_encuestado_3.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(i);
					for (int j = 0; j < lstred_distribucion_distinct.size(); j++){
						V_red_distribucion_bean red_distribucion_bean  = (V_red_distribucion_bean)lstred_distribucion_distinct.get(j);
						if(red_distribucion_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
							count ++;
							
						}
					}
					
					if (count == 0)//si no estan en red de distribucion
	                {
						if (nucl_encuestado_3_bean.getAag_l_defi() == 0 && nucl_encuestado_3_bean.getAag_v_defi() == 0 && 
								nucl_encuestado_3_bean.getAag_pr_def() == 0 && nucl_encuestado_3_bean.getAag_pe_def() == 0)
	                    {
							for (int j = 0; j < lstnucl_encuestado_4.size(); j++){
								V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
								
								if(nucl_encuestado_4_bean.getProvincia().equals(nucl_encuestado_3_bean.getProvincia()) &&
										nucl_encuestado_4_bean.getMunicipio().equals(nucl_encuestado_3_bean.getMunicipio()) &&
										nucl_encuestado_4_bean.getEntidad().equals(nucl_encuestado_3_bean.getEntidad()) &&
										nucl_encuestado_4_bean.getNucleo().equals(nucl_encuestado_3_bean.getNucleo())){
								if((nucl_encuestado_4_bean.getAau_vivien() +  nucl_encuestado_4_bean.getAau_pob_re() +
											nucl_encuestado_4_bean.getAau_pob_es() +  nucl_encuestado_4_bean.getAau_def_vi() +
											nucl_encuestado_4_bean.getAau_def_re() +  nucl_encuestado_4_bean.getAau_def_es()) <= 0){
									
										if (contTexto == 0)
										 {
											 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_06") + "\n");
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
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07)
				 //recorremos nucl_encuestado 2y comprobamos si todos los registros estan en red_distribucion
				for (int i = 0; i < lstnucl_encuestado_2.size(); i++)
	            {
					int count = 0;
					V_nucl_encuestado_2_bean nucl_encuestado_2_bean = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i);
					for (int j = 0; j < lstred_distribucion_distinct.size(); j++){
						V_red_distribucion_bean red_distribucion_bean  = (V_red_distribucion_bean)lstred_distribucion_distinct.get(j);
						if(red_distribucion_bean.getProvincia().equals(nucl_encuestado_2_bean.getProvincia()) &&
								red_distribucion_bean.getMunicipio().equals(nucl_encuestado_2_bean.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(nucl_encuestado_2_bean.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(nucl_encuestado_2_bean.getNucleo())){
							count ++;
							
						}
					}
					if (count == 0)//si no estan en red de distribucion
	                {
						if(!nucl_encuestado_2_bean.getAag_caudal().equals("NO") || 
								!nucl_encuestado_2_bean.getAag_restri().equals("NO")){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_07") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
								str.append(  nucl_encuestado_2_bean.getProvincia() + nucl_encuestado_2_bean.getMunicipio() +
										nucl_encuestado_2_bean.getEntidad() + nucl_encuestado_2_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
					for (int j = 0; j < lstnucl_encuestado_2.size(); j++){
						V_nucl_encuestado_2_bean nucl_encuestado_2_bean = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(j);
						if(nucl_encuestado_2_bean.getProvincia().equals(red_distribucion_bean.getProvincia()) &&
								nucl_encuestado_2_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) &&
								nucl_encuestado_2_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
								nucl_encuestado_2_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
							if(!nucl_encuestado_2_bean.getAag_caudal().equals("SF") && 
									!nucl_encuestado_2_bean.getAag_caudal().equals("IN")){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_08") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
									str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
											red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
					if(red_distribucion_bean.getEstado().equals("E")){
						
						for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
			            {
							V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
							
							if(nucl_encuestado_3_bean.getProvincia().equals(red_distribucion_bean.getProvincia()) && 
									nucl_encuestado_3_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) &&
									nucl_encuestado_3_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
									nucl_encuestado_3_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
								
								if((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) == 
										nucl_encuestado_3_bean.getAag_v_defi()	){	
								}
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_09") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
										str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
												red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
					if(red_distribucion_bean.getEstado().equals("E")){
						
						int aau_def_vi = 0;
						for (int j = 0; j < lstnucl_encuestado_4.size(); j++){
							V_nucl_encuestado_4_bean nucl_encuestado_4_bean  = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
							
							if(nucl_encuestado_4_bean.getProvincia().equals(red_distribucion_bean.getProvincia()) && 
									nucl_encuestado_4_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) &&
									nucl_encuestado_4_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
									nucl_encuestado_4_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
								aau_def_vi = nucl_encuestado_4_bean.getAau_def_vi();
							}
			            }
						
						
						for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
			            {
							V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
							
							if(nucl_encuestado_3_bean.getProvincia().equals(red_distribucion_bean.getProvincia()) && 
									nucl_encuestado_3_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) &&
									nucl_encuestado_3_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
									nucl_encuestado_3_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
								
								if((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) == 
									aau_def_vi	){	
									if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_10") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
										str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
												red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
			if(lstValCuadros.contains("v11")){
				//ERROR DEL MPT -> (V_11)
				for (int i = 0; i < lstred_distribucion.size(); i++)
	            {
					V_red_distribucion_bean red_distribucion_bean   = (V_red_distribucion_bean)lstred_distribucion.get(i);
					if(red_distribucion_bean.getEstado().equals("E")){
						
						for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
			            {
							V_nucl_encuestado_3_bean nucl_encuestado_3_bean  = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
							
							if(nucl_encuestado_3_bean.getProvincia().equals(red_distribucion_bean.getProvincia()) && 
									nucl_encuestado_3_bean.getMunicipio().equals(red_distribucion_bean.getMunicipio()) &&
									nucl_encuestado_3_bean.getEntidad().equals(red_distribucion_bean.getEntidad()) &&
									nucl_encuestado_3_bean.getNucleo().equals(red_distribucion_bean.getNucleo())){
								
								
							
								 if ((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) <= 0)
			                     {
									 if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_11") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
										str.append(  red_distribucion_bean.getProvincia() + red_distribucion_bean.getMunicipio() +
												red_distribucion_bean.getEntidad() + red_distribucion_bean.getNucleo() + "\t");
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
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro20.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
