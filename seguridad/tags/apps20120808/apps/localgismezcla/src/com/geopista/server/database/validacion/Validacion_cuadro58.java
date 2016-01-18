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

public class Validacion_cuadro58 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro58") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CENTRO_SANITARIO";
			
			ArrayList lstcentro_sanitario = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_centro_sanitario_bean centro_sanitarioBean = new V_centro_sanitario_bean();

				centro_sanitarioBean.setClave(rs.getString("clave"));
				centro_sanitarioBean.setProvincia(rs.getString("provincia"));
				centro_sanitarioBean.setMunicipio(rs.getString("municipio"));
				centro_sanitarioBean.setEntidad(rs.getString("entidad"));
				centro_sanitarioBean.setPoblamient(rs.getString("poblamient"));
				centro_sanitarioBean.setOrden_csan(rs.getString("orden_csan"));
				centro_sanitarioBean.setNombre(rs.getString("nombre"));
				centro_sanitarioBean.setTipo_csan(rs.getString("tipo_csan"));
				centro_sanitarioBean.setTitular(rs.getString("titular"));
				centro_sanitarioBean.setGestion(rs.getString("gestion"));
				centro_sanitarioBean.setS_cubi(new Integer(rs.getString("s_cubi")));
				centro_sanitarioBean.setS_aire(new Integer(rs.getString("s_aire")));
				centro_sanitarioBean.setS_sola(new Integer(rs.getString("s_sola")));
				centro_sanitarioBean.setUci(rs.getString("uci"));
				centro_sanitarioBean.setCamas(new Integer(rs.getString("camas")));
				centro_sanitarioBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
				centro_sanitarioBean.setEstado(rs.getString("estado"));

				
				lstcentro_sanitario.add(centro_sanitarioBean);
	
			}
		
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstcentro_sanitario.size(); i++)
            {
				V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
				
				if(centro_sanitario_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro58.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
							 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
							 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstcentro_sanitario.size(); i++)
	            {
					V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
					
					if(centro_sanitario_bean.getS_cubi() == 0 && centro_sanitario_bean.getS_aire() == 0 &&
							!centro_sanitario_bean.getEstado().equals("E"))	{
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro58.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
								 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
								 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
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
				for (int i = 0; i < lstcentro_sanitario.size(); i++)
	            {
					V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
					
					if( (centro_sanitario_bean.getS_cubi() + centro_sanitario_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro58.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
								 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
								 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
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
				for (int i = 0; i < lstcentro_sanitario.size(); i++)
	            {
					V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
					
					if( centro_sanitario_bean.getS_aire()  >= centro_sanitario_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro58.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
								 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
								 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
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
				for (int i = 0; i < lstcentro_sanitario.size(); i++)
	            {
					V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
					
					if( (centro_sanitario_bean.getTipo_csan().equals("HGL") || centro_sanitario_bean.getTipo_csan().equals("HQU") ||
							centro_sanitario_bean.getTipo_csan().equals("HIN") || centro_sanitario_bean.getTipo_csan().equals("HPS") ||
							centro_sanitario_bean.getTipo_csan().equals("HLE") || centro_sanitario_bean.getTipo_csan().equals("HOE")) &&
							centro_sanitario_bean.getCamas() == 0 && !centro_sanitario_bean.getEstado().equals("E"))	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro58.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
								 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
								 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
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
				for (int i = 0; i < lstcentro_sanitario.size(); i++)
	            {
					V_centro_sanitario_bean centro_sanitario_bean   = (V_centro_sanitario_bean)lstcentro_sanitario.get(i);
					
					if( (centro_sanitario_bean.getTipo_csan().equals("HGL") || centro_sanitario_bean.getTipo_csan().equals("HQU") ||
							centro_sanitario_bean.getTipo_csan().equals("HIN") || centro_sanitario_bean.getTipo_csan().equals("HPS") ||
							centro_sanitario_bean.getTipo_csan().equals("HLE") || centro_sanitario_bean.getTipo_csan().equals("HOE")) &&
							centro_sanitario_bean.getCamas() <= 0)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro58.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_sanitario_bean.getClave() + centro_sanitario_bean.getProvincia() + 
								 centro_sanitario_bean.getMunicipio()  + centro_sanitario_bean.getEntidad() +
								 centro_sanitario_bean.getPoblamient()+	 centro_sanitario_bean.getOrden_csan()+"\t");
					}
	
	            }	
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro58.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
