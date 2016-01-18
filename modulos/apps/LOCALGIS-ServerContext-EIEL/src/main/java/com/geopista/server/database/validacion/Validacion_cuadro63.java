/**
 * Validacion_cuadro63.java
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
import com.geopista.server.database.validacion.beans.V_casa_con_uso_bean;
import com.geopista.server.database.validacion.beans.V_casa_consitorial_bean;
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

public class Validacion_cuadro63 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro63.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro63") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_CASA_CONSISTORIAL";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_1";
			String sql_2 = "select * from v_CASA_CON_USO";
		

			ArrayList lstcasa_consistorial = new ArrayList();
			ArrayList lstnucl_encuestado_1 = new ArrayList();
			ArrayList lstcasa_con_uso = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_casa_consitorial_bean casa_consitorialBean = new V_casa_consitorial_bean();

				casa_consitorialBean.setClave(rs.getString("clave"));
				casa_consitorialBean.setProvincia(rs.getString("provincia"));
				casa_consitorialBean.setMunicipio(rs.getString("municipio"));
				casa_consitorialBean.setEntidad(rs.getString("entidad"));
				casa_consitorialBean.setPoblamient(rs.getString("poblamient"));
				casa_consitorialBean.setOrden_casa(rs.getString("orden_casa"));
				casa_consitorialBean.setNombre(rs.getString("nombre"));
				casa_consitorialBean.setTipo(rs.getString("tipo"));
				casa_consitorialBean.setTitular(rs.getString("titular"));
				casa_consitorialBean.setS_cubi(new Integer(rs.getString("s_cubi")));
				casa_consitorialBean.setS_aire(new Integer(rs.getString("s_aire")));
				casa_consitorialBean.setS_sola(new Integer(rs.getString("s_sola")));
				casa_consitorialBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
				casa_consitorialBean.setEstado(rs.getString("estado"));



				lstcasa_consistorial.add(casa_consitorialBean);
	
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
				
				if(rs.getString("ALTITUD")!=null&&!rs.getString("ALTITUD").equals(""))				
					nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				else
					nucl_encuestado_1_bean.setAltitud(0);
				
				//nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))
					nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				else
					nucl_encuestado_1_bean.setHoteles(0);
				//NULLnucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));

				if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))
					nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				else
					nucl_encuestado_1_bean.setCasas_rural(0);

				//NULLnucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));

				
				lstnucl_encuestado_1.add(nucl_encuestado_1_bean);
				
			}
			

			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_casa_con_uso_bean casa_con_uso_bean = new V_casa_con_uso_bean();
				casa_con_uso_bean.setProvincia(rs.getString("PROVINCIA"));
				casa_con_uso_bean.setMunicipio(rs.getString("MUNICIPIO"));
				casa_con_uso_bean.setEntidad(rs.getString("ENTIDAD"));
				casa_con_uso_bean.setPoblamient(rs.getString("POBLAMIENT"));
				casa_con_uso_bean.setS_cubi(rs.getInt("S_CUBI"));
				casa_con_uso_bean.setOrden_casa(rs.getString("ORDEN_CASA"));
				casa_con_uso_bean.setClave(rs.getString("CLAVE"));

				lstcasa_con_uso.add(casa_con_uso_bean);
	
			}
			
			
			
			
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstcasa_consistorial.size(); i++)
            {
				V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
				
				if(casa_consitorial_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro63.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
							 casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
							 casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
					int count = 0;
					for (int j = 0; j < lstnucl_encuestado_1.size(); j++)
		            {
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean    = (V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(j);
						
						if(nucl_encuestado_1_bean.getProvincia().equals(casa_consitorial_bean.getProvincia()) &&
								nucl_encuestado_1_bean.getMunicipio().equals(casa_consitorial_bean.getMunicipio())){
							count ++;
						}
		            }
					if (count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								 casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								 casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
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
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
					
					if(casa_consitorial_bean.getS_cubi() == 0 && casa_consitorial_bean.getS_aire() == 0 &&
							!casa_consitorial_bean.getEstado().equals("E"))	{
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								 casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								 casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
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
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
					
					if( (casa_consitorial_bean.getS_cubi() + casa_consitorial_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
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
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
					
					if( casa_consitorial_bean.getS_aire()  >= casa_consitorial_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
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
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
					int count = 0;
					for (int j = 0; j < lstcasa_con_uso.size(); j++)
		            {
						V_casa_con_uso_bean casa_con_uso_bean    = (V_casa_con_uso_bean)lstcasa_con_uso.get(j);
						if(casa_con_uso_bean.getClave().equals(casa_consitorial_bean.getClave()) && 
								casa_con_uso_bean.getProvincia().equals(casa_consitorial_bean.getProvincia()) && 
								casa_con_uso_bean.getMunicipio().equals(casa_consitorial_bean.getMunicipio()) && 
								casa_con_uso_bean.getEntidad().equals(casa_consitorial_bean.getEntidad()) &&
								casa_con_uso_bean.getPoblamient().equals(casa_consitorial_bean.getPoblamient()) &&
								casa_con_uso_bean.getOrden_casa().equals(casa_consitorial_bean.getOrden_casa())){
							count ++;
						}
		            }
					if( count == 0)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_05") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
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
				for (int i = 0; i < lstcasa_consistorial.size(); i++)
	            {
					V_casa_consitorial_bean casa_consitorial_bean    = (V_casa_consitorial_bean)lstcasa_consistorial.get(i);
	
					int suma = 0;
					for (int j = 0; j < lstcasa_con_uso.size(); j++)
		            {
						V_casa_con_uso_bean casa_con_uso_bean    = (V_casa_con_uso_bean)lstcasa_con_uso.get(j);
						if(casa_con_uso_bean.getClave().equals(casa_consitorial_bean.getClave()) && 
								casa_con_uso_bean.getProvincia().equals(casa_consitorial_bean.getProvincia()) && 
								casa_con_uso_bean.getMunicipio().equals(casa_consitorial_bean.getMunicipio()) && 
								casa_con_uso_bean.getEntidad().equals(casa_consitorial_bean.getEntidad()) &&
								casa_con_uso_bean.getPoblamient().equals(casa_consitorial_bean.getPoblamient()) &&
								casa_con_uso_bean.getOrden_casa().equals(casa_consitorial_bean.getOrden_casa())){
							suma +=  casa_con_uso_bean.getS_cubi();
						}
		            }
					if( suma !=  casa_consitorial_bean.getS_cubi())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro63.V_06") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						str.append( casa_consitorial_bean.getClave() + casa_consitorial_bean.getProvincia() + 
								casa_consitorial_bean.getMunicipio()  + casa_consitorial_bean.getEntidad() +
								casa_consitorial_bean.getPoblamient()+	 casa_consitorial_bean.getOrden_casa()+"\t");
	
					}
	
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadro63.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
