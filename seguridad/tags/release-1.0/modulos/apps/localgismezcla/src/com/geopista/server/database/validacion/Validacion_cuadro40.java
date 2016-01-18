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

public class Validacion_cuadro40 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro40") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_DEPURADORA_ENC_2_M50";
			String sql_1 = "select * from v_DEP_AGUA_NUCLEO";
			String sql_2 = "select * from v_DEPURADORA_ENC_M50";
			String sql_3 = "select * from v_NUCL_ENCUESTADO_5";
       
			ArrayList lstdepuradora_enc_2_m50 = new ArrayList();
			ArrayList lstdep_agua_nucleo = new ArrayList();
			ArrayList lstdepuradora_enc_m50 = new ArrayList();
			ArrayList lstnucl_encuestado_5 = new ArrayList();


			ps = connection.prepareStatement(sql);
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
				V_depuradora_enc_m50_bean depuradora_enc_m50_bean = new V_depuradora_enc_m50_bean();
				depuradora_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_m50_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_m50_bean.setClave(rs.getString("CLAVE"));
				lstdepuradora_enc_m50.add(depuradora_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_3);
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
				nucl_encuestado_5_bean.setSyd_c_trat(new Integer(rs.getString("SYD_C_TRAT")));
				nucl_encuestado_5_bean.setSyd_re_urb(new Integer(rs.getString("SYD_RE_URB")));
				nucl_encuestado_5_bean.setSyd_re_rus(new Integer(rs.getString("SYD_RE_RUS")));
				nucl_encuestado_5_bean.setSyd_re_ind(new Integer(rs.getString("SYD_RE_IND")));

				lstnucl_encuestado_5.add(nucl_encuestado_5_bean);
				
			}
			
			 //ERROR DEL MPT -> (FALSO ERROR)
			for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
            {
				V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
				if(depuradora_enc_2_m50_bean.getOrden_depu().length() < 3){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro40.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia() + 
							 depuradora_enc_2_m50_bean.getMunicipio()  + depuradora_enc_2_m50_bean.getOrden_depu()+"\t");
					 str.append("\n");
				}
            }
			str.append("\n\n");
			contTexto = 0;
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
		            {
						V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
						
						if(dep_agua_nucleo_bean.getDe_provinc().equals(depuradora_enc_2_m50_bean.getProvincia()) && 
								dep_agua_nucleo_bean.getDe_municip().equals(depuradora_enc_2_m50_bean.getMunicipio()) && 
								dep_agua_nucleo_bean.getOrden_depu().equals(depuradora_enc_2_m50_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia() + 
								 depuradora_enc_2_m50_bean.getMunicipio()  + depuradora_enc_2_m50_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
					int count = 0;
					for (int j = 0; j < lstdepuradora_enc_m50.size(); j++)
		            {
						V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(j);
						
						if(depuradora_enc_m50_bean.getProvincia().equals(depuradora_enc_2_m50_bean.getProvincia()) && 
								depuradora_enc_m50_bean.getMunicipio().equals(depuradora_enc_2_m50_bean.getMunicipio()) && 
								depuradora_enc_m50_bean.getOrden_depu().equals(depuradora_enc_2_m50_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia() + 
								 depuradora_enc_2_m50_bean.getMunicipio()  + depuradora_enc_2_m50_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
					
					if(depuradora_enc_2_m50_bean.getLodo_vert() > 100){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia()+ 
								 depuradora_enc_2_m50_bean.getMunicipio() + depuradora_enc_2_m50_bean.getOrden_depu() +"\t");
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
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
					
					if(depuradora_enc_2_m50_bean.getLodo_inci() > 100){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia()+ 
								 depuradora_enc_2_m50_bean.getMunicipio() + depuradora_enc_2_m50_bean.getOrden_depu() +"\t");
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
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
						
					if(depuradora_enc_2_m50_bean.getLodo_con_a() > 100){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia()+ 
								 depuradora_enc_2_m50_bean.getMunicipio() + depuradora_enc_2_m50_bean.getOrden_depu() +"\t");
						 error = true;
	
					}		
					
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v06")){
				
				//ERROR DEL MPT -> (V_06)
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
						
					if(depuradora_enc_2_m50_bean.getLodo_sin_a() > 100){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_06") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia()+ 
								 depuradora_enc_2_m50_bean.getMunicipio() + depuradora_enc_2_m50_bean.getOrden_depu() +"\t");
						 error = true;
	
					}		
					
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07)
				for (int i = 0; i < lstdepuradora_enc_2_m50.size(); i++)
	            {
					V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(i);
					
					if(depuradora_enc_2_m50_bean.getLodo_ot() > 100){
								
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro40.V_07") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(depuradora_enc_2_m50_bean.getClave() + depuradora_enc_2_m50_bean.getProvincia()+ 
								 depuradora_enc_2_m50_bean.getMunicipio() + depuradora_enc_2_m50_bean.getOrden_depu() +"\t");
						 str.append("\n");
	
					}		
					
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro40.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
