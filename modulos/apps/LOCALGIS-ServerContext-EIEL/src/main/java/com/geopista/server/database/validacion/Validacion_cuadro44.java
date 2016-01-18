/**
 * Validacion_cuadro44.java
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
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
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
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro44 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro44.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro44") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select DISTINCT CLAVE, VER_PROVIN, VER_MUNICI, VER_CODIGO from v_VERTEDERO_NUCLEO";
			String sql_1 = "select * from v_PADRON";
	        String sql_2 = "select * from v_VERT_ENCUESTADO_M50";


			ArrayList lstvertedero_nucleo = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstvert_encuestado_m50 = new ArrayList();

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vertedero_nucleo_bean vertedero_nucleo_bean = new V_vertedero_nucleo_bean();
				vertedero_nucleo_bean.setClave(rs.getString("CLAVE"));
				vertedero_nucleo_bean.setVer_provin(rs.getString("VER_PROVIN"));
				vertedero_nucleo_bean.setVer_munici(rs.getString("VER_MUNICI"));
				vertedero_nucleo_bean.setVer_codigo(rs.getString("VER_CODIGO"));

				lstvertedero_nucleo.add(vertedero_nucleo_bean);
				
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
				V_vert_encuestado_m50_bean  vert_encuestado_m50_bean = new V_vert_encuestado_m50_bean();

				vert_encuestado_m50_bean.setClave(rs.getString("CLAVE"));
				vert_encuestado_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				vert_encuestado_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vert_encuestado_m50_bean.setOrden_ver(rs.getString("ORDEN_VER"));
				vert_encuestado_m50_bean.setTipo_ver(rs.getString("TIPO_VER"));
				vert_encuestado_m50_bean.setTitular(rs.getString("TITULAR"));
				vert_encuestado_m50_bean.setGestion(rs.getString("GESTION"));
				vert_encuestado_m50_bean.setOlores(rs.getString("OLORES"));
				vert_encuestado_m50_bean.setHumos(rs.getString("HUMOS"));
				vert_encuestado_m50_bean.setCont_anima(rs.getString("CONT_ANIMA"));
				vert_encuestado_m50_bean.setR_inun(rs.getString("R_INUN"));
				vert_encuestado_m50_bean.setFiltracion(rs.getString("FILTRACION"));
				vert_encuestado_m50_bean.setImpacto_v(rs.getString("IMPACTO_V"));
				vert_encuestado_m50_bean.setFrec_averi(rs.getString("FREC_AVERI"));
				vert_encuestado_m50_bean.setSaturacion(rs.getString("SATURACION"));
				vert_encuestado_m50_bean.setInestable(rs.getString("INESTABLE"));
				vert_encuestado_m50_bean.setOtros(rs.getString("OTROS"));
				if(rs.getString("CAPAC_TOT")!=null&&!rs.getString("CAPAC_TOT").equals(""))
					vert_encuestado_m50_bean.setCapac_tot(new Integer(rs.getString("CAPAC_TOT")));
				else
					vert_encuestado_m50_bean.setCapac_tot(0);
				if(rs.getString("CAPAC_PORC")!=null&&!rs.getString("CAPAC_PORC").equals(""))
					vert_encuestado_m50_bean.setCapac_porc(new Integer(rs.getString("CAPAC_PORC")));
				else
					vert_encuestado_m50_bean.setCapac_porc(0);
				vert_encuestado_m50_bean.setCapac_ampl(rs.getString("CAPAC_AMPL"));
				
				if(rs.getString("CAPAC_TRAN")!=null&&!rs.getString("CAPAC_TRAN").equals(""))
					vert_encuestado_m50_bean.setCapac_tran(new Integer(rs.getString("CAPAC_TRAN")));
				else
					vert_encuestado_m50_bean.setCapac_tran(0);
				vert_encuestado_m50_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("VIDA_UTIL")!=null&&!rs.getString("VIDA_UTIL").equals(""))
					vert_encuestado_m50_bean.setVida_util(new Integer(rs.getString("VIDA_UTIL")));
				else
					vert_encuestado_m50_bean.setVida_util(0);
				vert_encuestado_m50_bean.setCategoria(rs.getString("CATEGORIA"));
				vert_encuestado_m50_bean.setActividad(rs.getString("ACTIVIDAD"));
				
				lstvert_encuestado_m50.add(vert_encuestado_m50_bean);
				
			}
		
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstvertedero_nucleo.size(); i++)
	            {
					V_vertedero_nucleo_bean vertedero_nucleo_bean   = (V_vertedero_nucleo_bean)lstvertedero_nucleo.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron_bean  = (V_padron_bean)lstpadron.get(j);
						
						if(padron_bean.getCodprov().equals(vertedero_nucleo_bean.getVer_provin()) && 
								padron_bean.getCodmunic().equals(vertedero_nucleo_bean.getVer_munici()) && 
								padron_bean.getTotal_poblacion_a1() > 50000){
							
							count ++;
						}
		            }
					if (count != 0){
	
						int count2=0;
						for (int h = 0; h < lstvert_encuestado_m50.size(); h++)
						{
							V_vert_encuestado_m50_bean  vert_encuestado_m50_bean = (V_vert_encuestado_m50_bean)lstvert_encuestado_m50.get(h);
							if(vert_encuestado_m50_bean.getProvincia().equals(vertedero_nucleo_bean.getVer_provin()) &&
									vert_encuestado_m50_bean.getMunicipio().equals(vertedero_nucleo_bean.getVer_munici()) &&
									vert_encuestado_m50_bean.getOrden_ver().equals(vertedero_nucleo_bean.getVer_codigo()) ){
								
								count2++;
							}
						}
						
						if (count2 == 0){
	
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro44.V_01") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( vertedero_nucleo_bean.getClave() + vertedero_nucleo_bean.getVer_provin() + 
									 vertedero_nucleo_bean.getVer_munici()  + vertedero_nucleo_bean.getVer_codigo()+"\t");
	
						}
		            }
	            }	
			}
			str.append("\n\n");
			
			
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro44.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
