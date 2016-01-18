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

public class Validacion_cuadro55 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro55") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_MATADERO";
			
			ArrayList lstmatadero = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_matadero_bean matadero_bean = new V_matadero_bean();


				matadero_bean.setClave(rs.getString("CLAVE"));
				matadero_bean.setProvincia(rs.getString("PROVINCIA"));
				matadero_bean.setMunicipio(rs.getString("MUNICIPIO"));
				matadero_bean.setEntidad(rs.getString("ENTIDAD"));
				matadero_bean.setPoblamient(rs.getString("POBLAMIENT"));
				matadero_bean.setOrden_mata(rs.getString("ORDEN_MATA"));
				matadero_bean.setNombre(rs.getString("NOMBRE"));
				matadero_bean.setClase_mat(rs.getString("CLASE_MAT"));
				matadero_bean.setTitular(rs.getString("TITULAR"));
				matadero_bean.setGestion(rs.getString("GESTION"));
				matadero_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				matadero_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				matadero_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				matadero_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				matadero_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					matadero_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					matadero_bean.setCapacidad(0);
				if(rs.getString("UTILIZACIO")!=null&&!rs.getString("UTILIZACIO").equals(""))
					matadero_bean.setUtilizacio(new Integer(rs.getString("UTILIZACIO")));
				else
					matadero_bean.setUtilizacio(0);
				matadero_bean.setTunel(rs.getString("TUNEL"));
				matadero_bean.setBovino(rs.getString("BOVINO"));
				matadero_bean.setOvino(rs.getString("OVINO"));
				matadero_bean.setPorcino(rs.getString("PORCINO"));
				matadero_bean.setOtros(rs.getString("OTROS"));

				
				lstmatadero.add(matadero_bean);
	
			}
		
		
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstmatadero.size(); i++)
            {
				V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
				if(matadero_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro55.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
							 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
							 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if(matadero_bean.getS_cubi() == 0 && matadero_bean.getS_aire() == 0 &&
							!matadero_bean.getEstado().equals("E"))	{
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( (matadero_bean.getS_cubi() + matadero_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i <lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( matadero_bean.getS_aire()  >= matadero_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro54.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( matadero_bean.getS_cubi() <= 0	)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( !matadero_bean.getBovino().equals("SI") &&
							!matadero_bean.getOvino().equals("SI") &&
							!matadero_bean.getPorcino().equals("SI") &&
							!matadero_bean.getOtros().equals("SI"))	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
						 error = true;
					}
	
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			
				//FALSOERROR DEL MPT -> (V_05) 
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( matadero_bean.getBovino().equals("") ||
							matadero_bean.getOvino().equals("") ||
							matadero_bean.getPorcino().equals("") ||
							matadero_bean.getOtros().equals(""))	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro55.falsoerror.V_2") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i <lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( (matadero_bean.getS_aire()+ matadero_bean.getS_cubi()) < matadero_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_06") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
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
				for (int i = 0; i < lstmatadero.size(); i++)
	            {
					V_matadero_bean matadero_bean  = (V_matadero_bean)lstmatadero.get(i);
				
					if( matadero_bean.getUtilizacio() > 100)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro55.V_07") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( matadero_bean.getClave() + matadero_bean.getProvincia() + 
								 matadero_bean.getMunicipio()  + matadero_bean.getEntidad() +
								 matadero_bean.getPoblamient()+	 matadero_bean.getOrden_mata()+"\t");
						 str.append("\n");
					}
	
	            }	
				
			}
			str.append("\n\n");


        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro55.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
