/**
 * Validacion_cuadro26.java
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
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro26 {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro26.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			
			str.append(Messages.getString("cuadro26") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			
			String sql = "select * from v_COLECTOR_ENC";
			String sql_sin_nucleo = "select * from v_colector_enc_sin_nucleo";
			String sql_1 = "select * from v_COLECTOR_NUCLEO";
			String sql_2 = "select * from v_TRAMO_COLECTOR";

			ArrayList lstcolector_enc = new ArrayList();
			ArrayList lstcolector_enc_sin_nucleo = new ArrayList();
			ArrayList lstcolector_nucleo = new ArrayList();
			ArrayList lsttramo_colector = new ArrayList();
			

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_enc_bean colector_enc_bean = new V_colector_enc_bean();

				colector_enc_bean.setProvincia(rs.getString("provincia"));
				colector_enc_bean.setMunicipio(rs.getString("municipio"));
				colector_enc_bean.setClave(rs.getString("clave"));
				colector_enc_bean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_enc.add(colector_enc_bean);
			}
			ps = connection.prepareStatement(sql_sin_nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_enc_bean colector_enc_bean = new V_colector_enc_bean();
				colector_enc_bean.setProvincia(rs.getString("provincia"));
				colector_enc_bean.setMunicipio(rs.getString("municipio"));
				colector_enc_bean.setClave(rs.getString("clave"));
				colector_enc_bean.setOrden_cole(rs.getString("orden_cole"));
				lstcolector_enc_sin_nucleo.add(colector_enc_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_colector_nucleo_bean colector_nucleo_bean = new V_colector_nucleo_bean();
				colector_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				colector_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				colector_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				colector_nucleo_bean.setNucleo(rs.getString("NUCLEO"));		
				colector_nucleo_bean.setC_municipi(rs.getString("C_MUNICIP"));
				colector_nucleo_bean.setC_provinci(rs.getString("C_PROVINC"));
				colector_nucleo_bean.setOrden_cole(rs.getString("ORDEN_COLE"));
				
				lstcolector_nucleo.add(colector_nucleo_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_colector_bean tramo_colector_bean = new V_tramo_colector_bean();

				tramo_colector_bean.setClave(rs.getString("CLAVE"));
				tramo_colector_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_colector_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_colector_bean.setOrden_cole(rs.getString("ORDEN_COLE"));
				tramo_colector_bean.setTipo_colec(rs.getString("TIPO_COLEC"));
				tramo_colector_bean.setSist_trans(rs.getString("SIST_TRANS"));
				tramo_colector_bean.setEstado(rs.getString("ESTADO"));
				tramo_colector_bean.setTitular(rs.getString("TITULAR"));
				tramo_colector_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONG_TRAMO")!=null&&!rs.getString("LONG_TRAMO").equals(""))
					tramo_colector_bean.setLong_tramo(new Double(Math.rint(new Double(rs.getString("LONG_TRAMO")))).intValue());
				else
					tramo_colector_bean.setLong_tramo(0);
				lsttramo_colector.add(tramo_colector_bean);
				
			}
			
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstcolector_enc.size(); i++)
	            {
					V_colector_enc_bean colector_enc_bean   = (V_colector_enc_bean)lstcolector_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstcolector_nucleo.size(); j++)
		            {
						V_colector_nucleo_bean colector_nucleo_bean   = (V_colector_nucleo_bean)lstcolector_nucleo.get(j);
						if(colector_nucleo_bean.getC_provinci().equals(colector_enc_bean.getProvincia()) && 
								colector_nucleo_bean.getC_municipi().equals(colector_enc_bean.getMunicipio()) && 
								colector_nucleo_bean.getOrden_cole().equals(colector_enc_bean.getOrden_cole() )){
							
							count ++;
							
						}
					}
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro26.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_bean.getClave() + colector_enc_bean.getProvincia() + 
								 colector_enc_bean.getMunicipio() + colector_enc_bean.getOrden_cole() + "\t");
						 error = true;
					}
	            }
				for (int i = 0; i < lstcolector_enc_sin_nucleo.size(); i++)
	            {
					V_colector_enc_bean colector_enc_bean   = (V_colector_enc_bean)lstcolector_enc_sin_nucleo.get(i);
					int count = 0;
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro26.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( colector_enc_bean.getClave() + colector_enc_bean.getProvincia() + 
								 colector_enc_bean.getMunicipio() + colector_enc_bean.getOrden_cole() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			
			
				for (int i = 0; i < lstcolector_enc.size(); i++)
	            {
					V_colector_enc_bean colector_enc_bean   = (V_colector_enc_bean)lstcolector_enc.get(i);
					if(colector_enc_bean.getOrden_cole().length() < 3){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro26.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  colector_enc_bean.getProvincia() + colector_enc_bean.getMunicipio() + "\t");
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
	
				for (int i = 0; i < lstcolector_enc.size(); i++)
	            {
					ArrayList lst = new ArrayList();
					V_colector_enc_bean colector_enc_bean   = (V_colector_enc_bean)lstcolector_enc.get(i);
					for (int j = 0; j < lstcolector_enc.size(); j++)
		            {
						V_colector_enc_bean colector_enc_bean_1   = (V_colector_enc_bean)lstcolector_enc.get(j);
						
						if(colector_enc_bean_1.getProvincia().equals(colector_enc_bean.getProvincia()) &&
								colector_enc_bean_1.getMunicipio().equals(colector_enc_bean.getMunicipio()) &&
								colector_enc_bean_1.getOrden_cole().equals(colector_enc_bean.getOrden_cole())){
							lst.add(colector_enc_bean_1);
						}
		            }
					
					for (int j = 0; j < lst.size(); j++){
						int count = 0;
						V_colector_enc_bean colector_enc_bean_lst   = (V_colector_enc_bean)lst.get(j);
						for (int g = 0; g < lsttramo_colector.size(); g++){
							V_tramo_colector_bean tramo_colector_bean = (V_tramo_colector_bean)lsttramo_colector.get(g);
							
							if(tramo_colector_bean.getProvincia().equals(colector_enc_bean_lst.getProvincia()) &&
									tramo_colector_bean.getMunicipio().equals(colector_enc_bean_lst.getMunicipio()) &&
									tramo_colector_bean.getOrden_cole().equals(colector_enc_bean_lst.getOrden_cole())){
								count ++;
							}
						}
						if(count == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro26.V_02") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( colector_enc_bean.getClave() + colector_enc_bean.getProvincia() + 
									 colector_enc_bean.getMunicipio() + colector_enc_bean.getOrden_cole() + "\t");
	
						}
					}
	            }
			}
			str.append("\n\n");

        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro26.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
