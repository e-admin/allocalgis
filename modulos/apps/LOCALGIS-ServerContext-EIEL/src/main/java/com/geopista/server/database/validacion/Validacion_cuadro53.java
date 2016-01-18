/**
 * Validacion_cuadro53.java
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

public class Validacion_cuadro53 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro53.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro53") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_PARQUE";
			
			ArrayList lstparque = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_parque_bean  parque_bean = new V_parque_bean();

				parque_bean.setClave(rs.getString("CLAVE"));
				parque_bean.setProvincia(rs.getString("PROVINCIA"));
				parque_bean.setMunicipio(rs.getString("MUNICIPIO"));
				parque_bean.setEntidad(rs.getString("ENTIDAD"));
				parque_bean.setPoblamient(rs.getString("POBLAMIENT"));
				parque_bean.setOrden_parq(rs.getString("ORDEN_PARQ"));
				parque_bean.setNombre(rs.getString("NOMBRE"));
				parque_bean.setTipo_parq(rs.getString("TIPO_PARQ"));
				parque_bean.setTitular(rs.getString("TITULAR"));
				parque_bean.setGestion(rs.getString("GESTION"));
				
				if(rs.getString("S_CUBI")!=null&&!rs.getString("S_CUBI").equals(""))				
					parque_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				else
					parque_bean.setS_cubi(0);
				
				//parque_bean.setS_cubi(new Integer(rs.getString("S_CUBI")));
				
				if(rs.getString("S_AIRE")!=null&&!rs.getString("S_AIRE").equals(""))				
					parque_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				else
					parque_bean.setS_aire(0);
				//parque_bean.setS_aire(new Integer(rs.getString("S_AIRE")));
				
				if(rs.getString("S_AIRE")!=null&&!rs.getString("S_AIRE").equals(""))				
					parque_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				else
					parque_bean.setS_sola(0);
				//NULLparque_bean.setS_sola(new Integer(rs.getString("S_SOLA")));
				parque_bean.setAgua(rs.getString("AGUA"));
				parque_bean.setSaneamient(rs.getString("SANEAMIENT"));
				parque_bean.setElectricid(rs.getString("ELECTRICID"));
				parque_bean.setComedor(rs.getString("COMEDOR"));
				parque_bean.setJuegos_inf(rs.getString("JUEGOS_INF"));
				parque_bean.setOtras(rs.getString("OTRAS"));
				parque_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
				parque_bean.setEstado(rs.getString("ESTADO"));
	
				lstparque.add(parque_bean);
	
			}
		
		
			//FALSOERROR DEL MPT ->
			for (int i = 0; i < lstparque.size(); i++)
            {
				V_parque_bean parque_bean  = (V_parque_bean)lstparque.get(i);
				
				if(parque_bean.getEstado().equals("")){
				
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro53.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( parque_bean.getClave() + parque_bean.getProvincia() + 
							 parque_bean.getMunicipio()  + parque_bean.getPoblamient()+
							 parque_bean.getOrden_parq()+"\t");
					 error = true;
					}		
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstparque.size(); i++)
	            {
					V_parque_bean parque_bean  = (V_parque_bean)lstparque.get(i);
	
					if(parque_bean.getS_cubi() == 0 && parque_bean.getS_aire() == 0 &&
							!parque_bean.getEstado().equals("E"))	{
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro53.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( parque_bean.getClave() + parque_bean.getProvincia() + 
								 parque_bean.getMunicipio()  + parque_bean.getEntidad() +
								 parque_bean.getPoblamient() + parque_bean.getOrden_parq()+"\t");
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
				for (int i = 0; i < lstparque.size(); i++)
	            {
					V_parque_bean parque_bean  = (V_parque_bean)lstparque.get(i);
					if( (parque_bean.getS_cubi() + parque_bean.getS_aire()) <= 0 )	{
						
					
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro53.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( parque_bean.getClave() + parque_bean.getProvincia() + 
								 parque_bean.getMunicipio()  + parque_bean.getEntidad() +
								 parque_bean.getPoblamient() + parque_bean.getOrden_parq()+"\t");
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
				for (int i = 0; i < lstparque.size(); i++)
	            {
					V_parque_bean parque_bean  = (V_parque_bean)lstparque.get(i);
		
					if( (!parque_bean.getTipo_parq().equals("AN") || !parque_bean.getTipo_parq().equals("PN") ||
							!parque_bean.getTipo_parq().equals("ZR")) && parque_bean.getS_aire()  > 1000000)	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro53.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( parque_bean.getClave() + parque_bean.getProvincia() + 
								 parque_bean.getMunicipio()  + parque_bean.getEntidad() +
								 parque_bean.getPoblamient() + parque_bean.getOrden_parq()+"\t");
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
				for (int i = 0; i <lstparque.size(); i++)
	            {
					V_parque_bean parque_bean  = (V_parque_bean)lstparque.get(i);
		
					if( parque_bean.getS_aire()  > parque_bean.getS_sola())	{
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro53.V_04") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( parque_bean.getClave() + parque_bean.getProvincia() + 
								 parque_bean.getMunicipio()  + parque_bean.getEntidad() +
								 parque_bean.getPoblamient() + parque_bean.getOrden_parq()+"\t");
					}
	
	            }
			}
			str.append("\n\n");
		
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadro53.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
