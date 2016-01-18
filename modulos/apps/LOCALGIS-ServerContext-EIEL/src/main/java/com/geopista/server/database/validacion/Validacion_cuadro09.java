/**
 * Validacion_cuadro09.java
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
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
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

public class Validacion_cuadro09 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro09.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro09") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql_distinct = "select DISTINCT PROVINCIA, MUNICIPIO, ENTIDAD, NUCLEO from v_COND_AGUA_NUCLEO";
			String sql_distinct1 = "select DISTINCT CLAVE, COND_PROVI, COND_MUNIC, ORDEN_COND from v_COND_AGUA_NUCLEO";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_3";
		    String sql_2 = "select * from v_PADRON";
		    String sql_3 = "select * from v_CONDUCCION_ENC_M50";
		    String sql_4 = "select * from v_TRAMO_CONDUCCION_M50";
			
			ArrayList lstcond_agua_nucleo_distinct = new ArrayList();
			ArrayList lstcond_agua_nucleo_distinct1 = new ArrayList();
			ArrayList lstnucl_encuestado_3 = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstconduccion_enc_m50 = new ArrayList();
			ArrayList lsttramo_conduccion_m50 = new ArrayList();
			
			ps = connection.prepareStatement(sql_distinct);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cond_agua_nucleo_bean cond_agua_nucleo_bean = new V_cond_agua_nucleo_bean();
				cond_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				cond_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				cond_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				cond_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));

				lstcond_agua_nucleo_distinct.add(cond_agua_nucleo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_distinct1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cond_agua_nucleo_bean cond_agua_nucleo_bean = new V_cond_agua_nucleo_bean();
				cond_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
				cond_agua_nucleo_bean.setCond_provi(rs.getString("COND_PROVI"));
				cond_agua_nucleo_bean.setCond_munic(rs.getString("COND_MUNIC"));
				cond_agua_nucleo_bean.setOrden_cond(rs.getString("ORDEN_COND"));

				lstcond_agua_nucleo_distinct1.add(cond_agua_nucleo_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_3_bean  nucl_encuestado_3_bean = new V_nucl_encuestado_3_bean();

				nucl_encuestado_3_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_3_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_3_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_3_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_3_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
				nucl_encuestado_3_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
				nucl_encuestado_3_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
				nucl_encuestado_3_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
				nucl_encuestado_3_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
				nucl_encuestado_3_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
				nucl_encuestado_3_bean.setAag_perdid(new Integer(rs.getString("AAG_PERDID")));
				nucl_encuestado_3_bean.setAag_calida(rs.getString("AAG_CALIDA"));
				nucl_encuestado_3_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
				nucl_encuestado_3_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
				nucl_encuestado_3_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
				nucl_encuestado_3_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
				
				lstnucl_encuestado_3.add(nucl_encuestado_3_bean);
				
			}
						
			ps = connection.prepareStatement(sql_2);
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
		
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_conduccion_enc_m50_bean conduccion_enc_m50_bean = new V_conduccion_enc_m50_bean();
				
				conduccion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				conduccion_enc_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));		
	
				lstconduccion_enc_m50.add(conduccion_enc_m50_bean);
				
			}
			
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_tramo_conduccion_m50_bean tramo_conduccion_m50_bean = new V_tramo_conduccion_m50_bean();

				tramo_conduccion_m50_bean.setClave(rs.getString("CLAVE"));
				tramo_conduccion_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_conduccion_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_conduccion_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				tramo_conduccion_m50_bean.setTipo_tcond(rs.getString("TIPO_TCOND"));
				tramo_conduccion_m50_bean.setEstado(rs.getString("ESTADO"));
				tramo_conduccion_m50_bean.setTitular(rs.getString("TITULAR"));
				tramo_conduccion_m50_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					tramo_conduccion_m50_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					tramo_conduccion_m50_bean.setLongitud(0);
				lsttramo_conduccion_m50.add(tramo_conduccion_m50_bean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstcond_agua_nucleo_distinct.size(); i++)
	            {
					V_cond_agua_nucleo_bean cond_agua_nucleo_bean = (V_cond_agua_nucleo_bean)lstcond_agua_nucleo_distinct.get(i);
					int count = 0;
					for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
		            {
						V_nucl_encuestado_3_bean  nucl_encuestado_3_bean = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
						if( nucl_encuestado_3_bean.getProvincia().equals(cond_agua_nucleo_bean.getProvincia()) &&
								nucl_encuestado_3_bean.getMunicipio().equals(cond_agua_nucleo_bean.getMunicipio()) &&
								nucl_encuestado_3_bean.getEntidad().equals(cond_agua_nucleo_bean.getEntidad()) &&
								nucl_encuestado_3_bean.getNucleo().equals(cond_agua_nucleo_bean.getNucleo())){
							
							if((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) <=0){
								 if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro09.V_01") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(cond_agua_nucleo_bean.getProvincia() + cond_agua_nucleo_bean.getMunicipio()+ 
										 cond_agua_nucleo_bean.getEntidad() + cond_agua_nucleo_bean.getNucleo() +"\t");
								 error = true;
							}
						}
		            }
	
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_02)
				for (int i = 0; i < lstcond_agua_nucleo_distinct1.size(); i++)
	            {
					V_cond_agua_nucleo_bean cond_agua_nucleo_bean = (V_cond_agua_nucleo_bean)lstcond_agua_nucleo_distinct1.get(i);
					int count = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean  padron_bean = (V_padron_bean)lstpadron.get(j);
						if( padron_bean.getCodprov().equals(cond_agua_nucleo_bean.getCond_provi()) &&
								padron_bean.getCodmunic().equals(cond_agua_nucleo_bean.getCond_munic()) &&
								padron_bean.getTotal_poblacion_a1() > 50000){
							count ++;
						}
		            }
	
					if(count != 0){
						int contador_enc_m50 = 0;
						int contador_conduccion_m50 = 0;
						for (int j = 0; j < lstconduccion_enc_m50.size(); j++)
			            {
							V_conduccion_enc_m50_bean conduccion_enc_m50_bean = (V_conduccion_enc_m50_bean)lstconduccion_enc_m50.get(j);
							if( conduccion_enc_m50_bean.getProvincia().equals(cond_agua_nucleo_bean.getProvincia()) &&
									conduccion_enc_m50_bean.getMunicipio().equals(cond_agua_nucleo_bean.getMunicipio()) &&
									conduccion_enc_m50_bean.getOrden_cond().equals(cond_agua_nucleo_bean.getOrden_cond())){
								contador_enc_m50 ++;
							}
			            }
						
						for (int j = 0; j < lsttramo_conduccion_m50.size(); j++)
			            {
							V_tramo_conduccion_m50_bean  tramo_conduccion_m50_bean = (V_tramo_conduccion_m50_bean)lsttramo_conduccion_m50.get(j);
							if( tramo_conduccion_m50_bean.getProvincia().equals(cond_agua_nucleo_bean.getProvincia()) &&
									tramo_conduccion_m50_bean.getMunicipio().equals(cond_agua_nucleo_bean.getMunicipio()) &&
									tramo_conduccion_m50_bean.getOrden_cond().equals(cond_agua_nucleo_bean.getOrden_cond())){
								contador_conduccion_m50 ++;
							}
			            }
						
						if(contador_enc_m50 == 0 || contador_conduccion_m50 == 0){
							 if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro09.V_02") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(cond_agua_nucleo_bean.getClave() + cond_agua_nucleo_bean.getCond_provi() + 
									 cond_agua_nucleo_bean.getCond_munic() +"\t");
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
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro09.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
