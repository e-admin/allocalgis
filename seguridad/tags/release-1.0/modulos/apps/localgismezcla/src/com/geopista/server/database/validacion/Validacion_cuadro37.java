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

public class Validacion_cuadro37 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro37") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_DEPURADORA_ENC";
			String sql_1 = "select * from v_DEP_AGUA_NUCLEO";
			String sql_2 = "select * from v_DEPURADORA_ENC_2";
		        
			
			ArrayList lstdepuradora_enc = new ArrayList();
			ArrayList lstdep_agua_nucleo = new ArrayList();
			ArrayList lstdepuradora_enc_2 = new ArrayList();

			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_bean depuradora_enc_bean = new V_depuradora_enc_bean();
				depuradora_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_bean.setClave(rs.getString("CLAVE"));
				depuradora_enc_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_bean.setTrat_pr_1(rs.getString("TRAT_PR_1"));
				depuradora_enc_bean.setTrat_pr_2(rs.getString("TRAT_PR_2"));
				depuradora_enc_bean.setTrat_pr_3(rs.getString("TRAT_PR_3"));
				depuradora_enc_bean.setTrat_sc_1(rs.getString("TRAT_SC_1"));
				depuradora_enc_bean.setTrat_sc_2(rs.getString("TRAT_SC_2"));
				depuradora_enc_bean.setTrat_sc_3(rs.getString("TRAT_SC_3"));
				depuradora_enc_bean.setTrat_av_1(rs.getString("TRAT_AV_1"));
				depuradora_enc_bean.setTrat_av_2(rs.getString("TRAT_AV_2"));
				depuradora_enc_bean.setTrat_av_3(rs.getString("TRAT_AV_3"));
				depuradora_enc_bean.setTrat_ld_1(rs.getString("TRAT_LD_1"));
				depuradora_enc_bean.setTrat_ld_2(rs.getString("TRAT_LD_2"));
				depuradora_enc_bean.setTrat_ld_3(rs.getString("TRAT_LD_3"));

				lstdepuradora_enc.add(depuradora_enc_bean);
				
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
				V_depuradora_enc_2_bean depuradora_enc_2_bean = new V_depuradora_enc_2_bean();

				depuradora_enc_2_bean.setClave(rs.getString("CLAVE"));
				depuradora_enc_2_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_2_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_2_bean.setTitular(rs.getString("TITULAR"));
				depuradora_enc_2_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					depuradora_enc_2_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					depuradora_enc_2_bean.setCapacidad(0);

				depuradora_enc_2_bean.setProblem_1(rs.getString("PROBLEM_1"));
				depuradora_enc_2_bean.setProblem_2(rs.getString("PROBLEM_2"));
				depuradora_enc_2_bean.setProblem_3(rs.getString("PROBLEM_3"));
				depuradora_enc_2_bean.setLodo_gest(rs.getString("LODO_GEST"));
				depuradora_enc_2_bean.setLodo_vert(new Integer(rs.getString("LODO_VERT")));
				depuradora_enc_2_bean.setLodo_inci(new Integer(rs.getString("LODO_INCI")));
				depuradora_enc_2_bean.setLodo_con_a(new Integer(rs.getString("LODO_CON_A")));
				depuradora_enc_2_bean.setLodo_sin_a(new Integer(rs.getString("LODO_SIN_A")));
				depuradora_enc_2_bean.setLodo_ot(new Integer(rs.getString("LODO_OT")));
				
				lstdepuradora_enc_2.add(depuradora_enc_2_bean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				 //ERROR DEL MPT -> (FALSO ERROR)
				for (int i = 0; i < lstdepuradora_enc.size(); i++)
	            {
					V_depuradora_enc_bean depuradora_enc_bean   = (V_depuradora_enc_bean)lstdepuradora_enc.get(i);
					if(depuradora_enc_bean.getOrden_depu().length() < 3){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro37.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_bean.getClave() + depuradora_enc_bean.getProvincia() + 
								 depuradora_enc_bean.getMunicipio()  + depuradora_enc_bean.getOrden_depu()+"\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
				
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstdepuradora_enc.size(); i++)
	            {
					V_depuradora_enc_bean depuradora_enc_bean   = (V_depuradora_enc_bean)lstdepuradora_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
		            {
						V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
						
						if(dep_agua_nucleo_bean.getDe_provinc().equals(depuradora_enc_bean.getProvincia()) && 
								dep_agua_nucleo_bean.getDe_municip().equals(depuradora_enc_bean.getMunicipio()) && 
								dep_agua_nucleo_bean.getOrden_depu().equals(depuradora_enc_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro37.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_bean.getClave() + depuradora_enc_bean.getProvincia() + 
								 depuradora_enc_bean.getMunicipio()  + depuradora_enc_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i < lstdepuradora_enc.size(); i++)
	            {
					V_depuradora_enc_bean depuradora_enc_bean   = (V_depuradora_enc_bean)lstdepuradora_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstdepuradora_enc_2.size(); j++)
		            {
						V_depuradora_enc_2_bean depuradora_enc_2_bean  = (V_depuradora_enc_2_bean)lstdepuradora_enc_2.get(j);
						
						if(depuradora_enc_2_bean.getProvincia().equals(depuradora_enc_bean.getProvincia()) && 
								depuradora_enc_2_bean.getMunicipio().equals(depuradora_enc_bean.getMunicipio()) && 
								depuradora_enc_2_bean.getOrden_depu().equals(depuradora_enc_bean.getOrden_depu())){
							count ++;
						}
		            }
					if(count  == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro37.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_bean.getClave() + depuradora_enc_bean.getProvincia() + 
								 depuradora_enc_bean.getMunicipio()  + depuradora_enc_bean.getOrden_depu()+"\t");
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
				for (int i = 0; i <lstdepuradora_enc.size(); i++)
	            {
					V_depuradora_enc_bean depuradora_enc_bean   = (V_depuradora_enc_bean)lstdepuradora_enc.get(i);
					if(depuradora_enc_bean.getTrat_pr_1().equals("NO") && depuradora_enc_bean.getTrat_pr_2().equals("NO") && depuradora_enc_bean.getTrat_pr_3().equals("NO") &&
							depuradora_enc_bean.getTrat_sc_1().equals("NO") && depuradora_enc_bean.getTrat_sc_2().equals("NO") && depuradora_enc_bean.getTrat_sc_3().equals("NO") &&
							depuradora_enc_bean.getTrat_av_1().equals("NO") && depuradora_enc_bean.getTrat_av_2().equals("NO") && depuradora_enc_bean.getTrat_av_3().equals("NO") &&
							depuradora_enc_bean.getTrat_ld_1().equals("NO") && depuradora_enc_bean.getTrat_ld_2().equals("NO") && depuradora_enc_bean.getTrat_ld_3().equals("NO")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro37.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( depuradora_enc_bean.getClave() + depuradora_enc_bean.getProvincia() + 
								 depuradora_enc_bean.getMunicipio()  + depuradora_enc_bean.getOrden_depu()+"\t");
						 str.append("\n");
					}
					
	            }	
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro37.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
