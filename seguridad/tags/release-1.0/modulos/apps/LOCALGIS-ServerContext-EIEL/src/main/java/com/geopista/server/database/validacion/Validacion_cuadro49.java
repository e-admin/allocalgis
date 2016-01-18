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
import com.geopista.server.database.validacion.beans.V_inst_depor_deporte_bean;
import com.geopista.server.database.validacion.beans.V_instal_deportiva_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_7_bean;
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
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro49 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro49") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_INSTAL_DEPORTIVA";
			String sql_v02 = "select * from v_INSTAL_DEPORTIVA where S_AIRE > S_SOLA";
			String sql_v03 = "select * from v_INSTAL_DEPORTIVA where (S_AIRE+S_CUBI)<=0";
	        String sql_1 = "select * from v_INST_DEPOR_DEPORTE";

			
			ArrayList lstinstal_deportiva = new ArrayList();
			ArrayList lstinstal_deportiva_v02 = new ArrayList();
			ArrayList lstinstal_deportiva_v03= new ArrayList();
			ArrayList lstinst_depor_deporte = new ArrayList();

			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_instal_deportiva_bean instal_deportiva_bean = new V_instal_deportiva_bean();


				instal_deportiva_bean.setClave(rs.getString("CLAVE"));
				instal_deportiva_bean.setProvincia(rs.getString("PROVINCIA"));
				instal_deportiva_bean.setMunicipio(rs.getString("MUNICIPIO"));
				instal_deportiva_bean.setEntidad(rs.getString("ENTIDAD"));
				instal_deportiva_bean.setPoblamient(rs.getString("POBLAMIENT"));
				instal_deportiva_bean.setOrden_inst(rs.getString("ORDEN_INST"));
				instal_deportiva_bean.setNombre(rs.getString("NOMBRE"));
				instal_deportiva_bean.setTipo_insde(rs.getString("TIPO_INSDE"));
				instal_deportiva_bean.setTitular(rs.getString("TITULAR"));
				instal_deportiva_bean.setGestion(rs.getString("GESTION"));
				instal_deportiva_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				instal_deportiva_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				instal_deportiva_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				instal_deportiva_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				instal_deportiva_bean.setEstado(rs.getString("ESTADO"));

				lstinstal_deportiva.add(instal_deportiva_bean);
	
			}
			
			ps = connection.prepareStatement(sql_v02);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_instal_deportiva_bean instal_deportiva_bean = new V_instal_deportiva_bean();


				instal_deportiva_bean.setClave(rs.getString("CLAVE"));
				instal_deportiva_bean.setProvincia(rs.getString("PROVINCIA"));
				instal_deportiva_bean.setMunicipio(rs.getString("MUNICIPIO"));
				instal_deportiva_bean.setEntidad(rs.getString("ENTIDAD"));
				instal_deportiva_bean.setPoblamient(rs.getString("POBLAMIENT"));
				instal_deportiva_bean.setOrden_inst(rs.getString("ORDEN_INST"));
				instal_deportiva_bean.setNombre(rs.getString("NOMBRE"));
				instal_deportiva_bean.setTipo_insde(rs.getString("TIPO_INSDE"));
				instal_deportiva_bean.setTitular(rs.getString("TITULAR"));
				instal_deportiva_bean.setGestion(rs.getString("GESTION"));
				instal_deportiva_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				instal_deportiva_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				instal_deportiva_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				instal_deportiva_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				instal_deportiva_bean.setEstado(rs.getString("ESTADO"));

				lstinstal_deportiva_v02.add(instal_deportiva_bean);
	
			}
			
			ps = connection.prepareStatement(sql_v03);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_instal_deportiva_bean instal_deportiva_bean = new V_instal_deportiva_bean();


				instal_deportiva_bean.setClave(rs.getString("CLAVE"));
				instal_deportiva_bean.setProvincia(rs.getString("PROVINCIA"));
				instal_deportiva_bean.setMunicipio(rs.getString("MUNICIPIO"));
				instal_deportiva_bean.setEntidad(rs.getString("ENTIDAD"));
				instal_deportiva_bean.setPoblamient(rs.getString("POBLAMIENT"));
				instal_deportiva_bean.setOrden_inst(rs.getString("ORDEN_INST"));
				instal_deportiva_bean.setNombre(rs.getString("NOMBRE"));
				instal_deportiva_bean.setTipo_insde(rs.getString("TIPO_INSDE"));
				instal_deportiva_bean.setTitular(rs.getString("TITULAR"));
				instal_deportiva_bean.setGestion(rs.getString("GESTION"));
				instal_deportiva_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				instal_deportiva_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				instal_deportiva_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				instal_deportiva_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				instal_deportiva_bean.setEstado(rs.getString("ESTADO"));

				lstinstal_deportiva_v03.add(instal_deportiva_bean);
	
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_inst_depor_deporte_bean inst_depor_deporte_bean = new V_inst_depor_deporte_bean();

				inst_depor_deporte_bean.setClave(rs.getString("CLAVE"));
				inst_depor_deporte_bean.setProvincia(rs.getString("PROVINCIA"));
				inst_depor_deporte_bean.setMunicipio(rs.getString("MUNICIPIO"));
				inst_depor_deporte_bean.setEntidad(rs.getString("ENTIDAD"));
				inst_depor_deporte_bean.setPoblamient(rs.getString("POBLAMIENT"));
				inst_depor_deporte_bean.setOrden_inst(rs.getString("ORDEN_INST"));
				inst_depor_deporte_bean.setTipo_depor(rs.getString("TIPO_DEPOR"));

				lstinst_depor_deporte.add(inst_depor_deporte_bean);
	
			}
			
			
			//FALSOERROR DEL MPT
			for (int i = 0; i < lstinstal_deportiva.size(); i++)
            {
				V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva.get(i);
				
				if( instal_deportiva_bean.getEstado().equals("")){
			
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro49.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
							 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
							 instal_deportiva_bean.getOrden_inst() +"\t");
					 error = true;

				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			//FALSOERROR DEL MPT
			for (int i = 0; i < lstinstal_deportiva.size(); i++)
            {
				V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva.get(i);
				
				if( instal_deportiva_bean.getOrden_inst().equals("")){
			
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro49.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
							 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
							 instal_deportiva_bean.getOrden_inst() +"\t");
					 error = true;
				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)  
				for (int i = 0; i < lstinstal_deportiva.size(); i++)
	            {
					V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva.get(i);
					
					if( instal_deportiva_bean.getS_cubi() == 0 && instal_deportiva_bean.getS_aire() == 0 &&
							!instal_deportiva_bean.getEstado().equals("E")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro49.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
								 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
								 instal_deportiva_bean.getOrden_inst() +"\t");
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
				for (int i = 0; i < lstinstal_deportiva_v02.size(); i++)
	            {
					V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva_v02.get(i);
					
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro49.V_02") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
							 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
							 instal_deportiva_bean.getOrden_inst() +"\t");
					 error = true;
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03) 
				for (int i = 0; i < lstinstal_deportiva_v03.size(); i++)
	            {
					V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva_v03.get(i);
					
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro49.V_03") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
							 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
							 instal_deportiva_bean.getOrden_inst() +"\t");
					 error = true;
	            }	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v04")){
			
				//ERROR DEL MPT -> (V_04) 
				for (int i = 0; i < lstinstal_deportiva_v03.size(); i++)
	            {
					V_instal_deportiva_bean instal_deportiva_bean    = (V_instal_deportiva_bean)lstinstal_deportiva_v03.get(i);
					int count =0;
					for (int j = 0; j < lstinst_depor_deporte.size(); j++)
		            {
						V_inst_depor_deporte_bean inst_depor_deporte_bean    = (V_inst_depor_deporte_bean)lstinst_depor_deporte.get(j);
						if( inst_depor_deporte_bean.getProvincia().equals(instal_deportiva_bean.getProvincia()) &&
								inst_depor_deporte_bean.getMunicipio().equals(instal_deportiva_bean.getMunicipio()) &&
								inst_depor_deporte_bean.getEntidad().equals(instal_deportiva_bean.getEntidad()) &&
								inst_depor_deporte_bean.getPoblamient().equals(instal_deportiva_bean.getPoblamient()) &&
								inst_depor_deporte_bean.getOrden_inst().equals(instal_deportiva_bean.getOrden_inst())){
							count ++;
						}
		            }
					
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro49.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( instal_deportiva_bean.getClave() + instal_deportiva_bean.getProvincia() + 
								 instal_deportiva_bean.getMunicipio() + instal_deportiva_bean.getEntidad()+
								 instal_deportiva_bean.getOrden_inst() +"\t");
						
					}
	            }
			}
			str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro49.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
