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

public class Validacion_cuadro36 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro36") + "\n");
			str.append("______________________________________________________________________\n\n");
			
		        
			String sql = "select DISTINCT CLAVE, DE_PROVINC, DE_MUNICIP, ORDEN_DEPU from v_DEP_AGUA_NUCLEO";
			String sql_1 = "select * from v_PADRON";
			String sql_2 = "select * from v_DEPURADORA_ENC_M50";
			String sql_3 = "select * from v_DEPURADORA_ENC_2_M50";
			
			ArrayList lstdep_agua_nucleo = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstdepuradora_enc_m50 = new ArrayList();
			ArrayList lstdepuradora_enc_2_m50 = new ArrayList();
			
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_dep_agua_nucleo_bean dep_agua_nucleo_bean = new V_dep_agua_nucleo_bean();

				dep_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
				dep_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));
				dep_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));
				dep_agua_nucleo_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				lstdep_agua_nucleo.add((dep_agua_nucleo_bean));
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
				V_depuradora_enc_m50_bean depuradora_enc_m50_bean = new V_depuradora_enc_m50_bean();
				depuradora_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_m50_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));

				lstdepuradora_enc_m50.add(depuradora_enc_m50_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_3);
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
			
			if(lstValCuadros.contains("v01")){
			
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstdep_agua_nucleo.size(); i++)
	            {
					V_dep_agua_nucleo_bean dep_agua_nucleo_bean   = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean  padron_bean = (V_padron_bean)lstpadron.get(j);
						if( padron_bean.getCodprov().equals(dep_agua_nucleo_bean.getDe_provinc()) &&
								padron_bean.getCodmunic().equals(dep_agua_nucleo_bean.getDe_municip()) &&
								padron_bean.getTotal_poblacion_a1() > 50000){
							count ++;
	
						}
		            }
					if(count != 0){//si existe en padron con mas de 50000h
	              	
						int count1 = 0;
						int count2 = 0;
						for (int j = 0; j < lstdepuradora_enc_m50.size(); j++)
			            {
							V_depuradora_enc_m50_bean depuradora_enc_m50_bean  = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(j);
							
							if(depuradora_enc_m50_bean.getProvincia().equals(dep_agua_nucleo_bean.getDe_provinc()) && 
									depuradora_enc_m50_bean.getMunicipio().equals(dep_agua_nucleo_bean.getDe_municip()) && 
									depuradora_enc_m50_bean.getOrden_depu().equals(dep_agua_nucleo_bean.getOrden_depu())){
								count1 ++;
							}
			            }
				
						for (int j = 0; j < lstdepuradora_enc_2_m50.size(); j++)
			            {
							V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean  = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(j);
							
							if(depuradora_enc_2_m50_bean.getProvincia().equals(dep_agua_nucleo_bean.getDe_provinc()) && 
									depuradora_enc_2_m50_bean.getMunicipio().equals(dep_agua_nucleo_bean.getDe_municip()) && 
									depuradora_enc_2_m50_bean.getOrden_depu().equals(dep_agua_nucleo_bean.getOrden_depu())){
								count2 ++;
							}
			            }
						
						if ( count1 == 0 || count2 == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro36.V_01") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( dep_agua_nucleo_bean.getClave() + dep_agua_nucleo_bean.getDe_provinc() + 
									 dep_agua_nucleo_bean.getDe_municip()  + dep_agua_nucleo_bean.getOrden_depu()+"\t");
	
						}
	
						
					}
				
	            }
				
			}
			str.append("\n\n");
		
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro36.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
