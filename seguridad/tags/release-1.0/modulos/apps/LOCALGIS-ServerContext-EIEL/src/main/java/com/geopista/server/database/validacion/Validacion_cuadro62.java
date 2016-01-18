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
import com.geopista.server.database.validacion.beans.V_proteccion_civil_bean;
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

public class Validacion_cuadro62 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro62") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_PROTECCION_CIVIL";
	     
			ArrayList lstproteccion_civil = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_proteccion_civil_bean  proteccion_civil_bean = new V_proteccion_civil_bean();

				proteccion_civil_bean.setClave(rs.getString("CLAVE"));
				proteccion_civil_bean.setProvincia(rs.getString("PROVINCIA"));
				proteccion_civil_bean.setMunicipio(rs.getString("MUNICIPIO"));
				proteccion_civil_bean.setEntidad(rs.getString("ENTIDAD"));
				proteccion_civil_bean.setPoblamient(rs.getString("POBLAMIENT"));
				proteccion_civil_bean.setOrden_prot(rs.getString("ORDEN_PROT"));
				proteccion_civil_bean.setNombre(rs.getString("NOMBRE"));
				proteccion_civil_bean.setTipo_pciv(rs.getString("TIPO_PCIV"));
				proteccion_civil_bean.setTitular(rs.getString("TITULAR"));
				proteccion_civil_bean.setAmbito(rs.getString("AMBITO"));
				proteccion_civil_bean.setGestion(rs.getString("GESTION"));
				proteccion_civil_bean.setPlan_profe(new Integer(rs.getString("PLAN_PROFE")));
				proteccion_civil_bean.setPlan_volun(new Integer(rs.getString("PLAN_VOLUN")));
				proteccion_civil_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				proteccion_civil_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				proteccion_civil_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				proteccion_civil_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				proteccion_civil_bean.setEstado(rs.getString("ESTADO"));
				proteccion_civil_bean.setVehic_ince(new Integer(rs.getString("VEHIC_INCE")));
				proteccion_civil_bean.setVehic_resc(new Integer(rs.getString("VEHIC_RESC")));
				proteccion_civil_bean.setAmbulancia(new Integer(rs.getString("AMBULANCIA")));
				proteccion_civil_bean.setMedios_aer(new Integer(rs.getString("MEDIOS_AER")));
				proteccion_civil_bean.setOtros_vehi(new Integer(rs.getString("OTROS_VEHI")));
				proteccion_civil_bean.setQuitanieve(new Integer(rs.getString("QUITANIEVE")));
				proteccion_civil_bean.setDetec_ince(new Integer(rs.getString("DETEC_INCE")));
				if(rs.getString("OTROS")!=null&&!rs.getString("OTROS").equals(""))
					proteccion_civil_bean.setOtros(new Integer(rs.getString("OTROS")));
				else
					proteccion_civil_bean.setOtros(0);

				lstproteccion_civil.add(proteccion_civil_bean);
	
			}
			
			
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstproteccion_civil.size(); i++)
            {
				V_proteccion_civil_bean proteccion_civil    = (V_proteccion_civil_bean)lstproteccion_civil.get(i);
				
				if(proteccion_civil.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro62.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
							 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
							 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i <lstproteccion_civil.size(); i++)
	            {
					V_proteccion_civil_bean proteccion_civil    = (V_proteccion_civil_bean)lstproteccion_civil.get(i);
						
					if(proteccion_civil.getS_cubi() == 0 && proteccion_civil.getS_aire() == 0 &&
							!proteccion_civil.getEstado().equals("E"))	{
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro62.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
								 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
								 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+"\t");
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
				for (int i = 0; i < lstproteccion_civil.size(); i++)
	            {
					V_proteccion_civil_bean proteccion_civil    = (V_proteccion_civil_bean)lstproteccion_civil.get(i);
					
					if( (proteccion_civil.getPlan_profe() + proteccion_civil.getPlan_volun()) <= 0 )	{
		
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro62.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						
						if(proteccion_civil.getEstado().equals("E")){
						 str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
								 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
								 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+ ". Estado=Ejecución" + "\t");
						}
						else{
							str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
									 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
									 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+"\t");
						}
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
				for (int i = 0; i <lstproteccion_civil.size(); i++)
	            {
					V_proteccion_civil_bean proteccion_civil    = (V_proteccion_civil_bean)lstproteccion_civil.get(i);
					
					if( (proteccion_civil.getS_cubi() + proteccion_civil.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro62.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
								 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
								 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+"\t");
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
				for (int i = 0; i < lstproteccion_civil.size(); i++)
	            {
					V_proteccion_civil_bean proteccion_civil    = (V_proteccion_civil_bean)lstproteccion_civil.get(i);
					
					if( proteccion_civil.getS_aire()  > proteccion_civil.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro62.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( proteccion_civil.getClave() + proteccion_civil.getProvincia() + 
								 proteccion_civil.getMunicipio()  + proteccion_civil.getEntidad() +
								 proteccion_civil.getPoblamient()+	 proteccion_civil.getOrden_prot()+"\t");
						 str.append("\n");
					}
	
	            }	
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro62.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
