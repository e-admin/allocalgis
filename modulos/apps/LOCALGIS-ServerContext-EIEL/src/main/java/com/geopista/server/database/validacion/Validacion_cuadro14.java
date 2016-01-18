/**
 * Validacion_cuadro14.java
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
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;

public class Validacion_cuadro14 {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro14.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro14") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select DISTINCT CLAVE, DE_PROVINC, DE_MUNICIP, ORDEN_DEPO from v_DEPOSITO_AGUA_NUCLEO";                
			String sql_1 = "select * from v_PADRON";
	        String sql_2 = "select * from v_DEPOSITO_ENC_M50";
	        

			ArrayList lstdeposito_agua_nucleo = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstdeposito_enc_m50 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean = new V_deposito_agua_nucleo_bean();
				deposito_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));
				deposito_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));
				deposito_agua_nucleo_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));	
				deposito_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
	
				lstdeposito_agua_nucleo.add(deposito_agua_nucleo_bean);
				
			}
		
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_padron_bean  padron_bean = new V_padron_bean();

				padron_bean.setCodprov(rs.getString("PROVINCIA"));
				padron_bean.setCodmunic(rs.getString("MUNICIPIO"));
				padron_bean.setN_hombre_a1(new Integer(rs.getString("HOMBRES")));
				padron_bean.setN_mujeres_a1(new Integer(rs.getString("MUJERES")));
				padron_bean.setTotal_poblacion_a1(new Integer(rs.getString("TOTAL_POB")));
	
				lstpadron.add(padron_bean);
				
			}
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_deposito_enc_m50_bean deposito_enc_m50_bean = new V_deposito_enc_m50_bean();

				deposito_enc_m50_bean.setClave(rs.getString("CLAVE"));
				deposito_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				deposito_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				deposito_enc_m50_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
				deposito_enc_m50_bean.setUbicacion(rs.getString("UBICACION"));
				deposito_enc_m50_bean.setTitular(rs.getString("TITULAR"));
				deposito_enc_m50_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&rs.getString("LIMPIEZA")!="")
					deposito_enc_m50_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					deposito_enc_m50_bean.setCapacidad(0);
				deposito_enc_m50_bean.setEstado(rs.getString("ESTADO"));
				deposito_enc_m50_bean.setProteccion(rs.getString("PROTECCION"));
				if(rs.getString("LIMPIEZA")!=null &&!rs.getString("LIMPIEZA").equals(""))
					deposito_enc_m50_bean.setLimpieza(new Integer(rs.getString("LIMPIEZA")));
				else
					deposito_enc_m50_bean.setLimpieza(0);
				deposito_enc_m50_bean.setContador(rs.getString("CONTADOR"));

		
				lstdeposito_enc_m50.add(deposito_enc_m50_bean);
				
			}

			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstdeposito_agua_nucleo.size(); i++)
	            {
					int count = 0;
					V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean   = (V_deposito_agua_nucleo_bean)lstdeposito_agua_nucleo.get(i);
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron_bean = (V_padron_bean)lstpadron.get(j);
						
						if( padron_bean.getCodprov().equals(deposito_agua_nucleo_bean.getDe_provinc()) &&
								padron_bean.getCodmunic().equals(deposito_agua_nucleo_bean.getDe_municip()) &&
								padron_bean.getTotal_poblacion_a1() > 50000 ){
							
							count ++;
						}
		            }
					
					if (count != 0){
						int contador = 0;
						for (int j = 0; j < lstdeposito_enc_m50.size(); j++)
			            {
							V_deposito_enc_m50_bean deposito_enc_m50_bean = (V_deposito_enc_m50_bean)lstdeposito_enc_m50.get(j);
							
							if(deposito_enc_m50_bean.getProvincia().equals(deposito_agua_nucleo_bean.getDe_provinc()) &&
								deposito_enc_m50_bean.getMunicipio().equals(deposito_agua_nucleo_bean.getDe_municip()) &&
								deposito_enc_m50_bean.getOrden_depo().equals(deposito_agua_nucleo_bean.getOrden_depo())){
								contador ++;
							}
			            }
						
						if (contador == 0){
							
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro14.V_01") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(deposito_agua_nucleo_bean.getClave() + deposito_agua_nucleo_bean.getDe_provinc()+ 
									 deposito_agua_nucleo_bean.getDe_municip() + deposito_agua_nucleo_bean.getOrden_depo() +"\t");
							 str.append("\t");
						}
					}
				}
			}
			str.append("\n\n");
			
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro14.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
