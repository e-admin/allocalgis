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
import com.geopista.server.database.validacion.beans.V_cent_cultural_bean;
import com.geopista.server.database.validacion.beans.V_cent_cultural_usos_bean;
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

public class Validacion_cuadro54 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro54") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_LONJA_MERC_FERIA";
			
			ArrayList lstlonja_merc_feria = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_lonja_merc_feria_bean lonja_merc_feria_bean = new V_lonja_merc_feria_bean();


				lonja_merc_feria_bean.setClave(rs.getString("CLAVE"));
				lonja_merc_feria_bean.setProvincia(rs.getString("PROVINCIA"));
				lonja_merc_feria_bean.setMunicipio(rs.getString("MUNICIPIO"));
				lonja_merc_feria_bean.setEntidad(rs.getString("ENTIDAD"));
				lonja_merc_feria_bean.setPoblamient(rs.getString("POBLAMIENT"));
				lonja_merc_feria_bean.setOrden_lmf(rs.getString("ORDEN_LMF"));
				lonja_merc_feria_bean.setNombre(rs.getString("NOMBRE"));
				lonja_merc_feria_bean.setTipo_lonj(rs.getString("TIPO_LONJ"));
				lonja_merc_feria_bean.setTitular(rs.getString("TITULAR"));
				lonja_merc_feria_bean.setGestion(rs.getString("GESTION"));
				lonja_merc_feria_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				lonja_merc_feria_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				lonja_merc_feria_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				lonja_merc_feria_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				lonja_merc_feria_bean.setEstado(rs.getString("ESTADO"));

				
				lstlonja_merc_feria.add(lonja_merc_feria_bean);
	
			}
		
		
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstlonja_merc_feria.size(); i++)
            {
				V_lonja_merc_feria_bean lonja_merc_feria_bean  = (V_lonja_merc_feria_bean)lstlonja_merc_feria.get(i);
				
				if(lonja_merc_feria_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro54.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( lonja_merc_feria_bean.getClave() + lonja_merc_feria_bean.getProvincia() + 
							 lonja_merc_feria_bean.getMunicipio()  + lonja_merc_feria_bean.getEntidad()+
							 lonja_merc_feria_bean.getPoblamient() + lonja_merc_feria_bean.getOrden_lmf()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i <lstlonja_merc_feria.size(); i++)
	            {
					V_lonja_merc_feria_bean lonja_merc_feria_bean  = (V_lonja_merc_feria_bean)lstlonja_merc_feria.get(i);
	
					if(lonja_merc_feria_bean.getS_cubi() == 0 && lonja_merc_feria_bean.getS_aire() == 0 &&
							!lonja_merc_feria_bean.getEstado().equals("E"))	{
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro54.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( lonja_merc_feria_bean.getClave() + lonja_merc_feria_bean.getProvincia() + 
								 lonja_merc_feria_bean.getMunicipio()  + lonja_merc_feria_bean.getEntidad()+
								 lonja_merc_feria_bean.getPoblamient() + lonja_merc_feria_bean.getOrden_lmf()+"\t");
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
				for (int i = 0; i < lstlonja_merc_feria.size(); i++)
	            {
					V_lonja_merc_feria_bean lonja_merc_feria_bean  = (V_lonja_merc_feria_bean)lstlonja_merc_feria.get(i);
	
					if( (lonja_merc_feria_bean.getS_cubi() + lonja_merc_feria_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro54.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( lonja_merc_feria_bean.getClave() + lonja_merc_feria_bean.getProvincia() + 
								 lonja_merc_feria_bean.getMunicipio()  + lonja_merc_feria_bean.getEntidad()+
								 lonja_merc_feria_bean.getPoblamient() + lonja_merc_feria_bean.getOrden_lmf()+"\t");;
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
				for (int i = 0; i < lstlonja_merc_feria.size(); i++)
	            {
					V_lonja_merc_feria_bean lonja_merc_feria_bean  = (V_lonja_merc_feria_bean)lstlonja_merc_feria.get(i);
	
		
					if( lonja_merc_feria_bean.getS_aire()  > lonja_merc_feria_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro54.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( lonja_merc_feria_bean.getClave() + lonja_merc_feria_bean.getProvincia() + 
								 lonja_merc_feria_bean.getMunicipio()  + lonja_merc_feria_bean.getEntidad()+
								 lonja_merc_feria_bean.getPoblamient() + lonja_merc_feria_bean.getOrden_lmf()+"\t");
	
					}
	
	            }	
			}
			str.append("\n\n");
			
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro54.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
