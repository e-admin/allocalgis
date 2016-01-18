/**
 * Validacion_cuadro52.java
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

public class Validacion_cuadro52 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro52.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("cuadro52") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CENT_CULTURAL";
			String sql_1 = "select * from v_CENT_CULTURAL_USOS";
			
			ArrayList lstcent_cultural = new ArrayList();
			ArrayList lstcent_cultural_usos = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cent_cultural_bean cent_culturalBean = new V_cent_cultural_bean();

				cent_culturalBean.setClave(rs.getString("clave"));
				cent_culturalBean.setProvincia(rs.getString("provincia"));
				cent_culturalBean.setMunicipio(rs.getString("municipio"));
				cent_culturalBean.setEntidad(rs.getString("entidad"));
				cent_culturalBean.setPoblamient(rs.getString("poblamient"));
				cent_culturalBean.setOrden_cent(rs.getString("orden_cent"));
				cent_culturalBean.setNombre(rs.getString("nombre"));
				cent_culturalBean.setTipo_cent(rs.getString("tipo_cent"));
				cent_culturalBean.setTitular(rs.getString("titular"));
				cent_culturalBean.setTitular(rs.getString("titular"));
				cent_culturalBean.setS_cubi(new Integer(rs.getString("s_cubi")));
				
				
				if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))				
					cent_culturalBean.setS_aire(new Integer(rs.getString("s_aire")));
				else
					cent_culturalBean.setS_aire(0);
				
				//cent_culturalBean.setS_aire(new Integer(rs.getString("s_aire")));
				cent_culturalBean.setS_sola(new Integer(rs.getString("s_sola")));
				cent_culturalBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
				cent_culturalBean.setEstado(rs.getString("estado"));

				lstcent_cultural.add(cent_culturalBean);
	
			}
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cent_cultural_usos_bean cent_cultural_usosBean = new V_cent_cultural_usos_bean();

				cent_cultural_usosBean.setClave(rs.getString("clave"));
				cent_cultural_usosBean.setProvincia(rs.getString("provincia"));
				cent_cultural_usosBean.setMunicipio(rs.getString("municipio"));
				cent_cultural_usosBean.setEntidad(rs.getString("entidad"));
				cent_cultural_usosBean.setPoblamient(rs.getString("poblamient"));
				cent_cultural_usosBean.setOrden_cent(rs.getString("orden_cent"));
				cent_cultural_usosBean.setUso(rs.getString("uso"));
				cent_cultural_usosBean.setS_cubi(new Integer(rs.getString("s_cubi")));

				lstcent_cultural_usos.add(cent_cultural_usosBean);
	
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstcent_cultural.size(); i++)
	            {
					V_cent_cultural_bean cent_cultural_bean    = (V_cent_cultural_bean)lstcent_cultural.get(i);
					int count = 0;
					int suma = 0;
					for (int j = 0; j < lstcent_cultural_usos.size(); j++)
		            {
						V_cent_cultural_usos_bean cent_cultural_usos_bean    = (V_cent_cultural_usos_bean)lstcent_cultural_usos.get(j);
						if(cent_cultural_usos_bean.getProvincia().equals(cent_cultural_bean.getProvincia()) &&
								cent_cultural_usos_bean.getProvincia().equals(cent_cultural_bean.getProvincia()) &&
								cent_cultural_usos_bean.getMunicipio().equals(cent_cultural_bean.getMunicipio()) &&
								cent_cultural_usos_bean.getEntidad().equals(cent_cultural_bean.getEntidad()) &&
								cent_cultural_usos_bean.getPoblamient().equals(cent_cultural_bean.getPoblamient()) &&
								cent_cultural_usos_bean.getOrden_cent().equals(cent_cultural_bean.getOrden_cent())){
							count ++;
							suma += cent_cultural_usos_bean.getS_cubi();
						}
		            }
					if (count != 0){
						if(suma != cent_cultural_bean.getS_cubi()){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro52.V_01") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( cent_cultural_bean.getClave() + cent_cultural_bean.getProvincia() + 
									 cent_cultural_bean.getMunicipio()  + cent_cultural_bean.getEntidad()+
									 cent_cultural_bean.getPoblamient() + cent_cultural_bean.getOrden_cent()+"\t");
						}
					}
					
	            }
			}
			str.append("\n\n");
				
			
        }
        catch (Exception e)
        {
           	logger.error("Exception:",e);

        	str.append(Messages.getString("exception") + " " +Validacion_cuadro52.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
