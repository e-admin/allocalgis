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
import com.geopista.server.database.validacion.beans.V_edific_pub_sin_uso_bean;
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

public class Validacion_cuadro65 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro65") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_EDIFIC_PUB_SIN_USO";

			ArrayList lstedific_sin_uso = new ArrayList();

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_edific_pub_sin_uso_bean edific_pub_sin_uso_bean = new V_edific_pub_sin_uso_bean();

				edific_pub_sin_uso_bean.setClave(rs.getString("CLAVE"));
				edific_pub_sin_uso_bean.setProvincia(rs.getString("PROVINCIA"));
				edific_pub_sin_uso_bean.setMunicipio(rs.getString("MUNICIPIO"));
				edific_pub_sin_uso_bean.setEntidad(rs.getString("ENTIDAD"));
				edific_pub_sin_uso_bean.setPoblamient(rs.getString("POBLAMIENT"));
				edific_pub_sin_uso_bean.setOrden_edif(rs.getString("ORDEN_EDIF"));
				edific_pub_sin_uso_bean.setNombre(rs.getString("NOMBRE"));
				edific_pub_sin_uso_bean.setTitular(rs.getString("TITULAR"));
				edific_pub_sin_uso_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				edific_pub_sin_uso_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				edific_pub_sin_uso_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				edific_pub_sin_uso_bean.setEstado(rs.getString("ESTADO"));
				edific_pub_sin_uso_bean.setUsoant(rs.getString("USOANT"));
				

				lstedific_sin_uso.add(edific_pub_sin_uso_bean);
	
			}
			
			
			
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstedific_sin_uso.size(); i++)
            {
				V_edific_pub_sin_uso_bean edific_pub_sin_uso_bean   = (V_edific_pub_sin_uso_bean)lstedific_sin_uso.get(i);
				
				if(edific_pub_sin_uso_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro65.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( edific_pub_sin_uso_bean.getClave() + edific_pub_sin_uso_bean.getProvincia() + 
							 edific_pub_sin_uso_bean.getMunicipio()  + edific_pub_sin_uso_bean.getEntidad() +
							 edific_pub_sin_uso_bean.getPoblamient()+	 edific_pub_sin_uso_bean.getOrden_edif()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstedific_sin_uso.size(); i++)
	            {
					V_edific_pub_sin_uso_bean edific_pub_sin_uso_bean   = (V_edific_pub_sin_uso_bean)lstedific_sin_uso.get(i);
					
					if(edific_pub_sin_uso_bean.getS_cubi() <= 0 && edific_pub_sin_uso_bean.getS_aire() <= 0)	{
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro65.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( edific_pub_sin_uso_bean.getClave() + edific_pub_sin_uso_bean.getProvincia() + 
								 edific_pub_sin_uso_bean.getMunicipio()  + edific_pub_sin_uso_bean.getEntidad() +
								 edific_pub_sin_uso_bean.getPoblamient()+	 edific_pub_sin_uso_bean.getOrden_edif()+"\t");
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
				for (int i = 0; i < lstedific_sin_uso.size(); i++)
	            {
					V_edific_pub_sin_uso_bean edific_pub_sin_uso_bean   = (V_edific_pub_sin_uso_bean)lstedific_sin_uso.get(i);
						
					if( edific_pub_sin_uso_bean.getS_aire()  >= edific_pub_sin_uso_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro65.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( edific_pub_sin_uso_bean.getClave() + edific_pub_sin_uso_bean.getProvincia() + 
								edific_pub_sin_uso_bean.getMunicipio()  + edific_pub_sin_uso_bean.getEntidad() +
								edific_pub_sin_uso_bean.getPoblamient()+	 edific_pub_sin_uso_bean.getOrden_edif()+"\t");
	
					}
	
	            }	
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro65.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
