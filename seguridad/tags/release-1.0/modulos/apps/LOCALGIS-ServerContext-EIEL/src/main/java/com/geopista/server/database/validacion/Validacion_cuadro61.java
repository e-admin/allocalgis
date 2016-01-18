package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_alumbrado_bean;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_cementerio_bean;
import com.geopista.server.database.validacion.beans.V_cent_cultural_bean;
import com.geopista.server.database.validacion.beans.V_cent_cultural_usos_bean;
import com.geopista.server.database.validacion.beans.V_centro_asistencial_bean;
import com.geopista.server.database.validacion.beans.V_centro_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_centro_sanitario_bean;
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
import com.geopista.server.database.validacion.beans.V_lonja_merc_feria_bean;
import com.geopista.server.database.validacion.beans.V_matadero_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_7_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_parque_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_ramal_saneamiento_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_sanea_autonomo_bean;
import com.geopista.server.database.validacion.beans.V_tanatorio_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro61 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro61") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_NIVEL_ENSENANZA";
	        String sql_1 = "select * from v_CENTRO_ENSENANZA";
			

			ArrayList lstcentro_ensenanza_bean = new ArrayList();
			ArrayList lstnivel_ensenanza_bean = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nivel_ensenanza_bean  nivel_ensenanza_bean = new V_nivel_ensenanza_bean();


				nivel_ensenanza_bean.setClave(rs.getString("CLAVE"));
				nivel_ensenanza_bean.setProvincia(rs.getString("PROVINCIA"));
				nivel_ensenanza_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nivel_ensenanza_bean.setEntidad(rs.getString("ENTIDAD"));
				nivel_ensenanza_bean.setPoblamient(rs.getString("POBLAMIENT"));
				nivel_ensenanza_bean.setOrden_cent(rs.getString("ORDEN_CENT"));
				nivel_ensenanza_bean.setNivel(rs.getString("NIVEL"));
				nivel_ensenanza_bean.setUnidades(new Integer(rs.getString("UNIDADES")));
				nivel_ensenanza_bean.setPlazas(new Integer(rs.getString("PLAZAS")));
				nivel_ensenanza_bean.setAlumnos(new Integer(rs.getString("ALUMNOS")));


				lstnivel_ensenanza_bean.add(nivel_ensenanza_bean);
	
			}
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_centro_ensenanza_bean centro_ensenanzaBean = new V_centro_ensenanza_bean();

				centro_ensenanzaBean.setClave(rs.getString("clave"));
				centro_ensenanzaBean.setProvincia(rs.getString("provincia"));
				centro_ensenanzaBean.setMunicipio(rs.getString("municipio"));
				centro_ensenanzaBean.setEntidad(rs.getString("entidad"));
				centro_ensenanzaBean.setPoblamient(rs.getString("poblamient"));
				centro_ensenanzaBean.setOrden_cent(rs.getString("orden_cent"));
				centro_ensenanzaBean.setNombre(rs.getString("nombre"));
				centro_ensenanzaBean.setAmbito(rs.getString("ambito"));
				centro_ensenanzaBean.setTitular(rs.getString("titular"));
				centro_ensenanzaBean.setS_cubi(new Integer(rs.getString("s_cubi")));
				centro_ensenanzaBean.setS_aire(new Integer(rs.getString("s_aire")));
				centro_ensenanzaBean.setS_sola(new Integer(rs.getString("s_sola")));
				centro_ensenanzaBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
				centro_ensenanzaBean.setEstado(rs.getString("estado"));

				
				lstcentro_ensenanza_bean.add(centro_ensenanzaBean);
	
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstnivel_ensenanza_bean.size(); i++)
	            {
					V_nivel_ensenanza_bean nivel_ensenanza_bean   = (V_nivel_ensenanza_bean)lstnivel_ensenanza_bean.get(i);
					if(nivel_ensenanza_bean.getPlazas() == 0 || nivel_ensenanza_bean.getAlumnos() == 0 || nivel_ensenanza_bean.getUnidades() == 0){
						for (int j = 0; j < lstcentro_ensenanza_bean.size(); j++)
			            {
							V_centro_ensenanza_bean centro_ensenanza_bean   = (V_centro_ensenanza_bean)lstcentro_ensenanza_bean.get(j);
							
							if(centro_ensenanza_bean.getClave().equals(nivel_ensenanza_bean.getClave()) &&
									centro_ensenanza_bean.getProvincia().equals(nivel_ensenanza_bean.getProvincia()) &&
									centro_ensenanza_bean.getMunicipio().equals(nivel_ensenanza_bean.getMunicipio()) &&
									centro_ensenanza_bean.getEntidad().equals(nivel_ensenanza_bean.getEntidad()) &&
									centro_ensenanza_bean.getPoblamient().equals(nivel_ensenanza_bean.getPoblamient()) &&
									centro_ensenanza_bean.getOrden_cent().equals(nivel_ensenanza_bean.getOrden_cent()) &&
									!centro_ensenanza_bean.getEstado().equals("E")){
								
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro61.V_01") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append( nivel_ensenanza_bean.getClave() + nivel_ensenanza_bean.getProvincia() + 
										 nivel_ensenanza_bean.getMunicipio()  + nivel_ensenanza_bean.getEntidad() +
										 nivel_ensenanza_bean.getPoblamient()+	 nivel_ensenanza_bean.getOrden_cent()+"\t");
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
			if(lstValCuadros.contains("v02")){
				//ERROR DEL MPT -> (V_02) 
				for (int i = 0; i < lstnivel_ensenanza_bean.size(); i++)
	            {
					V_nivel_ensenanza_bean nivel_ensenanza_bean   = (V_nivel_ensenanza_bean)lstnivel_ensenanza_bean.get(i);
					if(nivel_ensenanza_bean.getPlazas() == 0 || nivel_ensenanza_bean.getAlumnos() == 0 || nivel_ensenanza_bean.getUnidades() == 0){
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro61.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nivel_ensenanza_bean.getClave() + nivel_ensenanza_bean.getProvincia() + 
								 nivel_ensenanza_bean.getMunicipio()  + nivel_ensenanza_bean.getEntidad() +
								 nivel_ensenanza_bean.getPoblamient()+	 nivel_ensenanza_bean.getOrden_cent()+"\t");
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
				for (int i = 0; i < lstcentro_ensenanza_bean.size(); i++)
	            {
					V_centro_ensenanza_bean centro_ensenanza_bean   = (V_centro_ensenanza_bean)lstcentro_ensenanza_bean.get(i);
					
					if( centro_ensenanza_bean.getS_aire()  >= centro_ensenanza_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro60.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( centro_ensenanza_bean.getClave() + centro_ensenanza_bean.getProvincia() + 
								 centro_ensenanza_bean.getMunicipio()  + centro_ensenanza_bean.getEntidad() +
								 centro_ensenanza_bean.getPoblamient()+	 centro_ensenanza_bean.getOrden_cent()+"\t");
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
				for (int i = 0; i < lstcentro_ensenanza_bean.size(); i++)
	            {
					V_centro_ensenanza_bean centro_ensenanza_bean   = (V_centro_ensenanza_bean)lstcentro_ensenanza_bean.get(i);
					int count = 0;
					for (int j = 0; j < lstnivel_ensenanza_bean.size(); j++)
		            {
						V_nivel_ensenanza_bean nivel_ensenanza_bean   = (V_nivel_ensenanza_bean)lstnivel_ensenanza_bean.get(j);
						
						if( nivel_ensenanza_bean.getClave().equals(centro_ensenanza_bean.getClave()) &&
								nivel_ensenanza_bean.getProvincia().equals(centro_ensenanza_bean.getProvincia()) &&
								nivel_ensenanza_bean.getMunicipio().equals(centro_ensenanza_bean.getMunicipio()) &&
								nivel_ensenanza_bean.getEntidad().equals(centro_ensenanza_bean.getEntidad()) &&
								nivel_ensenanza_bean.getPoblamient().equals(centro_ensenanza_bean.getPoblamient()) &&
								nivel_ensenanza_bean.getOrden_cent().equals(centro_ensenanza_bean.getOrden_cent()))	{
						
							count ++;
						}
		            }
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro60.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_ensenanza_bean.getClave() + centro_ensenanza_bean.getProvincia() + 
								 centro_ensenanza_bean.getMunicipio()  + centro_ensenanza_bean.getEntidad() +
								 centro_ensenanza_bean.getPoblamient()+	 centro_ensenanza_bean.getOrden_cent()+"\t");
						 str.append("\n");
					}
	
	            }	
			}
			str.append("\n\n");
		
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro61.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
