/**
 * Validacion_cuadro59.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

public class Validacion_cuadro59 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro59.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro59") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CENTRO_ASISTENCIAL";
			
			ArrayList lstcentro_asistencial_bean = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_centro_asistencial_bean centro_asistencialBean = new V_centro_asistencial_bean();

				centro_asistencialBean.setClave(rs.getString("clave"));
				centro_asistencialBean.setProvincia(rs.getString("provincia"));
				centro_asistencialBean.setMunicipio(rs.getString("municipio"));
				centro_asistencialBean.setEntidad(rs.getString("entidad"));
				centro_asistencialBean.setPoblamient(rs.getString("poblamient"));
				centro_asistencialBean.setOrden_casi(rs.getString("orden_casi"));
				centro_asistencialBean.setNombre(rs.getString("nombre"));
				centro_asistencialBean.setTipo_casis(rs.getString("tipo_casis"));
				centro_asistencialBean.setTitular(rs.getString("titular"));
				centro_asistencialBean.setGestion(rs.getString("gestion"));
				centro_asistencialBean.setPlazas(new Integer(rs.getString("plazas")));
				centro_asistencialBean.setS_cubi(new Integer(rs.getString("s_cubi")));
				centro_asistencialBean.setS_aire(new Integer(rs.getString("s_aire")));
				centro_asistencialBean.setS_sola(new Integer(rs.getString("s_sola")));
				centro_asistencialBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
				centro_asistencialBean.setEstado(rs.getString("estado"));


				lstcentro_asistencial_bean.add(centro_asistencialBean);
	
			}
		
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstcentro_asistencial_bean.size(); i++)
            {
				V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
				
				if(centro_asistencial_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro59.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
							 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
							 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstcentro_asistencial_bean.size(); i++)
	            {
					V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
					
					if(centro_asistencial_bean.getS_cubi() == 0 && centro_asistencial_bean.getS_aire() == 0 &&
							!centro_asistencial_bean.getEstado().equals("E"))	{
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro59.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
								 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
								 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
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
				for (int i = 0; i <lstcentro_asistencial_bean.size(); i++)
	            {
					V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
					
					if( (centro_asistencial_bean.getS_cubi() + centro_asistencial_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro59.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
								 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
								 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
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
				for (int i = 0; i < lstcentro_asistencial_bean.size(); i++)
	            {
					V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
					
					if( centro_asistencial_bean.getS_aire()  >= centro_asistencial_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro59.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
								 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
								 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
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
				for (int i = 0; i < lstcentro_asistencial_bean.size(); i++)
	            {
					V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
					
					if( ( centro_asistencial_bean.getTipo_casis().equals("RA") ||
							centro_asistencial_bean.getTipo_casis().equals("GI") || centro_asistencial_bean.getTipo_casis().equals("AL") ||
							centro_asistencial_bean.getTipo_casis().equals("CE") || centro_asistencial_bean.getTipo_casis().equals("IN")) &&
							centro_asistencial_bean.getPlazas() == 0 && !centro_asistencial_bean.getEstado().equals("E"))	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro59.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
								 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
								 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
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
				for (int i = 0; i < lstcentro_asistencial_bean.size(); i++)
	            {
					V_centro_asistencial_bean centro_asistencial_bean   = (V_centro_asistencial_bean)lstcentro_asistencial_bean.get(i);
					
					if( ( centro_asistencial_bean.getTipo_casis().equals("RA") ||
							centro_asistencial_bean.getTipo_casis().equals("GI") || centro_asistencial_bean.getTipo_casis().equals("AL") ||
							centro_asistencial_bean.getTipo_casis().equals("CE") || centro_asistencial_bean.getTipo_casis().equals("IN")) &&
							centro_asistencial_bean.getPlazas() <= 0)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro59.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( centro_asistencial_bean.getClave() + centro_asistencial_bean.getProvincia() + 
								 centro_asistencial_bean.getMunicipio()  + centro_asistencial_bean.getEntidad() +
								 centro_asistencial_bean.getPoblamient()+	 centro_asistencial_bean.getOrden_casi()+"\t");
						
					}
	
	            }	
				str.append("\n\n");
				contTexto = 0;
			 }
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadro59.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
