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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_6_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_ramal_saneamiento_bean;
import com.geopista.server.database.validacion.beans.V_recogida_basura_bean;
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

public class Validacion_cuadro42 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro42") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_RECOGIDA_BASURA";
		    String sql_1 = "select * from v_NUCL_ENCUESTADO_1";
	        String sql_2 = "select * from v_NUCL_ENCUESTADO_6";


			ArrayList lstrecogida_basura = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstnucl_encuestado_6 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_recogida_basura_bean recogida_basura_bean = new V_recogida_basura_bean();

				recogida_basura_bean.setProvincia(rs.getString("PROVINCIA"));
				recogida_basura_bean.setMunicipio(rs.getString("MUNICIPIO"));
				recogida_basura_bean.setEntidad(rs.getString("ENTIDAD"));
				recogida_basura_bean.setNucleo(rs.getString("NUCLEO"));
				recogida_basura_bean.setTipo_rbas(rs.getString("TIPO_RBAS"));
				recogida_basura_bean.setGestion(rs.getString("GESTION"));
				recogida_basura_bean.setPeriodicid(rs.getString("PERIODICID"));
				recogida_basura_bean.setCalidad(rs.getString("CALIDAD"));
				recogida_basura_bean.setProdu_basu(new Double(rs.getString("PRODU_BASU")));
				recogida_basura_bean.setContenedor(new Integer(rs.getString("CONTENEDOR")));


				lstrecogida_basura.add(recogida_basura_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
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
			
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_6_bean nucl_encuestado_6_bean = new V_nucl_encuestado_6_bean();
				nucl_encuestado_6_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_6_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_6_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_6_bean.setNucleo(rs.getString("NUCLEO"));	
				nucl_encuestado_6_bean.setRba_v_sser(rs.getInt("RBA_V_SSER"));
				nucl_encuestado_6_bean.setRba_serlim(rs.getString("RBA_SERLIM"));
				nucl_encuestado_6_bean.setRba_pr_sse(rs.getInt("RBA_PR_SSE"));
				nucl_encuestado_6_bean.setRba_pe_sse(rs.getInt("RBA_PE_SSE"));
				nucl_encuestado_6_bean.setRba_plalim(rs.getInt("RBA_PLALIM"));
				
				lstnucl_encuestado_6.add(nucl_encuestado_6_bean);
				
			}

			 //ERROR DEL MPT -> (V_01)
            //COMPRUEBA QUE SI EXISTE RECOGIDA DE RESIDUOS URBANOS, TIENE QUE HABER, COMO MÍNIMO, RECOGIDA NO SELECCIONADA, TIPO RN.
            // No realizo la comprobacion por que es imposible que se de, el tipo de basura es obligatorio.
			if(lstValCuadros.contains("v02")){
				
				//ERROR DEL MPT -> (V_02) 
				for (int i = 0; i < lstrecogida_basura.size(); i++)
	            {
					V_recogida_basura_bean recogida_basura_bean   = (V_recogida_basura_bean)lstrecogida_basura.get(i);
		
					if(recogida_basura_bean.getProdu_basu() <= 0){
	
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro42.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( recogida_basura_bean.getProvincia() + recogida_basura_bean.getMunicipio() + 
								 recogida_basura_bean.getEntidad()  + recogida_basura_bean.getNucleo()+"\t");
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
				for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean    = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i);
		
					int count = 0;
					for (int j = 0; j < lstrecogida_basura.size(); j++)
		            {
						V_recogida_basura_bean recogida_basura_bean   = (V_recogida_basura_bean)lstrecogida_basura.get(j);
			
						if(recogida_basura_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
								recogida_basura_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
								recogida_basura_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								recogida_basura_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							
							count ++;
						}			
		            }
					if(count == 0){
						int count1 = 0;
						V_nucl_encuestado_6_bean nucl_encuestado_6_bean_encontrado = null;
						for (int h = 0; h < lstnucl_encuestado_6.size(); h++)
			            {
							V_nucl_encuestado_6_bean nucl_encuestado_6_bean   = (V_nucl_encuestado_6_bean)lstnucl_encuestado_6.get(h);
							if(nucl_encuestado_6_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
									nucl_encuestado_6_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
									nucl_encuestado_6_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
									nucl_encuestado_6_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
								count1  ++;
								nucl_encuestado_6_bean_encontrado = nucl_encuestado_6_bean;
							}
			            }
						if(count1 != 0){
							if (nucl_encuestado_1_bean.getViv_total() != nucl_encuestado_6_bean_encontrado.getRba_v_sser()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro42.V_03") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() + 
										 nucl_encuestado_1_bean.getEntidad()  + nucl_encuestado_1_bean.getNucleo()+"\t");
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
				for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean    = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i);
		
					int count = 0;
					for (int j = 0; j < lstrecogida_basura.size(); j++)
		            {
						V_recogida_basura_bean recogida_basura_bean   = (V_recogida_basura_bean)lstrecogida_basura.get(j);
			
						if(recogida_basura_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
								recogida_basura_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
								recogida_basura_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								recogida_basura_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							
							count  ++;
						}
		            }
					if(count == 0){
						int count1 = 0;
						V_nucl_encuestado_6_bean nucl_encuestado_6_bean_encontrado = null;
						for (int h = 0; h < lstnucl_encuestado_6.size(); h++)
			            {
							V_nucl_encuestado_6_bean nucl_encuestado_6_bean   = (V_nucl_encuestado_6_bean)lstnucl_encuestado_6.get(h);
							if(nucl_encuestado_6_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
									nucl_encuestado_6_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
									nucl_encuestado_6_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
									nucl_encuestado_6_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
								count1  ++;
								nucl_encuestado_6_bean_encontrado = nucl_encuestado_6_bean;
							}
			            }
						if(count1 != 0){
							if (nucl_encuestado_1_bean.getPadron() != nucl_encuestado_6_bean_encontrado.getRba_pr_sse()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro42.V_04") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() + 
										 nucl_encuestado_1_bean.getEntidad()  + nucl_encuestado_1_bean.getNucleo()+"\t");
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
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05)
				for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean    = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i);
		
					int count = 0;
					for (int j = 0; j < lstrecogida_basura.size(); j++)
		            {
						V_recogida_basura_bean recogida_basura_bean   = (V_recogida_basura_bean)lstrecogida_basura.get(j);
			
						if(recogida_basura_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
								recogida_basura_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
								recogida_basura_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
								recogida_basura_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
							
							count  ++;
						}
	
		            }
					if(count == 0){
						int count1 = 0;
						V_nucl_encuestado_6_bean nucl_encuestado_6_bean_encontrado = null;
						for (int h = 0; h < lstnucl_encuestado_6.size(); h++)
			            {
							V_nucl_encuestado_6_bean nucl_encuestado_6_bean   = (V_nucl_encuestado_6_bean)lstnucl_encuestado_6.get(h);
							if(nucl_encuestado_6_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) && 
									nucl_encuestado_6_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio()) && 
									nucl_encuestado_6_bean.getEntidad().equals(nucl_encuestado_1_bean.getEntidad()) &&
									nucl_encuestado_6_bean.getNucleo().equals(nucl_encuestado_1_bean.getNucleo())){
								count1  ++;
								nucl_encuestado_6_bean_encontrado = nucl_encuestado_6_bean;
							}
			            }
						if(count1 != 0){
							if (nucl_encuestado_1_bean.getPob_estaci() != nucl_encuestado_6_bean_encontrado.getRba_pe_sse()){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro42.V_05") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() + 
										 nucl_encuestado_1_bean.getEntidad()  + nucl_encuestado_1_bean.getNucleo()+"\t");
	
							}
						}
					}
	            }	
			}
			str.append("\n\n");

        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro42.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
