/**
 * Validacion_cuadro31.java
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

public class Validacion_cuadro31 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro31.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro31") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_EMISARIO_ENC";
			String sql_sin_nucleo = "select * from v_EMISARIO_ENC_sin_nucleo";
			String sql_1 = "select * from v_EMISARIO_NUCLEO";
			String sql_2 = "select * from v_TRAMO_EMISARIO";
			

			ArrayList lstemisario_enc = new ArrayList();
			ArrayList lstemisario_enc_sin_nucleo = new ArrayList();
			ArrayList lstemisario_nucleo = new ArrayList();
			ArrayList lsttramo_emisario = new ArrayList();

			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_emisario_enc_bean emisario_enc_bean = new V_emisario_enc_bean();

				emisario_enc_bean.setClave(rs.getString("CLAVE"));
				emisario_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_enc_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				emisario_enc_bean.setTipo_vert(rs.getString("TIPO_VERT"));
				emisario_enc_bean.setZona_vert(rs.getString("ZONA_VERT"));
				if(rs.getString("DISTANCIA")!=null && !rs.getString("DISTANCIA").equals("") )
					emisario_enc_bean.setDistancia(new Integer(rs.getString("DISTANCIA")));
				else
					emisario_enc_bean.setDistancia(0);

				lstemisario_enc.add(emisario_enc_bean);
			}
			ps = connection.prepareStatement(sql_sin_nucleo);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_emisario_enc_bean emisario_enc_bean = new V_emisario_enc_bean();
				emisario_enc_bean.setClave(rs.getString("CLAVE"));
				emisario_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_enc_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				lstemisario_enc_sin_nucleo.add(emisario_enc_bean);
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	

				V_emisario_nucleo_bean emisario_nucleo_bean = new V_emisario_nucleo_bean();


				emisario_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				emisario_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				emisario_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				emisario_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				emisario_nucleo_bean.setClave(rs.getString("CLAVE"));
				emisario_nucleo_bean.setEm_provinc(rs.getString("EM_PROVINC"));
				emisario_nucleo_bean.setEm_municip(rs.getString("EM_MUNICIP"));
				emisario_nucleo_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));

				lstemisario_nucleo.add(emisario_nucleo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_emisario_bean tramo_emisario_bean = new V_tramo_emisario_bean();

				tramo_emisario_bean.setClave(rs.getString("CLAVE"));
				tramo_emisario_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_emisario_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_emisario_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				tramo_emisario_bean.setTipo_mat(rs.getString("TIPO_MAT"));
				tramo_emisario_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("LONG_TERRE")!=null&&!rs.getString("LONG_TERRE").equals(""))
					tramo_emisario_bean.setLong_terre(new Double(Math.rint(new Double(rs.getString("LONG_TERRE")))).intValue());
				else
					tramo_emisario_bean.setLong_terre(0);
				if(rs.getString("LONG_MARIT")!=null&&!rs.getString("LONG_MARIT").equals(""))
					tramo_emisario_bean.setLong_marit(new Double(Math.rint(new Double(rs.getString("LONG_MARIT")))).intValue());
				else
					tramo_emisario_bean.setLong_marit(0);

				lsttramo_emisario.add(tramo_emisario_bean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstemisario_enc.size(); i++)
	            {
					V_emisario_enc_bean emisario_enc_bean    = (V_emisario_enc_bean)lstemisario_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstemisario_nucleo.size(); j++)
		            {
						V_emisario_nucleo_bean emisario_nucleo_bean   = (V_emisario_nucleo_bean)lstemisario_nucleo.get(j);
						if(emisario_nucleo_bean.getEm_provinc().equals(emisario_enc_bean.getProvincia()) && 
								emisario_nucleo_bean.getEm_municip().equals(emisario_enc_bean.getMunicipio()) && 
								emisario_nucleo_bean.getOrden_emis().equals(emisario_enc_bean.getOrden_emis())){
							
							count ++;
						}
	
		            }
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro31.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( emisario_enc_bean.getClave() + emisario_enc_bean.getProvincia() + 
								 emisario_enc_bean.getMunicipio()  + emisario_enc_bean.getOrden_emis()+"\t");
						 error = true;
					}
	            }
				for (int i = 0; i < lstemisario_enc_sin_nucleo.size(); i++)
	            {
					V_emisario_enc_bean emisario_enc_bean    = (V_emisario_enc_bean)lstemisario_enc_sin_nucleo.get(i);
					int count = 0;
					if(count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro31.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( emisario_enc_bean.getClave() + emisario_enc_bean.getProvincia() + 
								 emisario_enc_bean.getMunicipio()  + emisario_enc_bean.getOrden_emis()+"\t");
						 error = true;
						
					}
				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
				
				for (int i = 0; i < lstemisario_enc.size(); i++)
	            {
					V_emisario_enc_bean emisario_enc_bean    = (V_emisario_enc_bean)lstemisario_enc.get(i);
					if(emisario_enc_bean.getOrden_emis().length() < 3){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro31.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( emisario_enc_bean.getClave() + emisario_enc_bean.getProvincia() + 
								 emisario_enc_bean.getMunicipio()  + emisario_enc_bean.getOrden_emis()+"\t");
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
				for (int i = 0; i < lstemisario_enc.size(); i++)
	            {
					V_emisario_enc_bean emisario_enc_bean    = (V_emisario_enc_bean)lstemisario_enc.get(i);
					int count = 0;
					ArrayList lst = new ArrayList();
					for (int j = 0; j < lstemisario_enc.size(); j++)
		            {
						
						V_emisario_nucleo_bean emisario_nucleo_bean   = (V_emisario_nucleo_bean)lstemisario_nucleo.get(j);
						if(emisario_nucleo_bean.getEm_provinc().equals(emisario_enc_bean.getProvincia()) && 
								emisario_nucleo_bean.getEm_municip().equals(emisario_enc_bean.getMunicipio()) && 
								emisario_nucleo_bean.getOrden_emis().equals(emisario_enc_bean.getOrden_emis())){
							
							lst.add(emisario_nucleo_bean);
						}
		            }
					
					for (int j = 0; j < lst.size(); j++)
		            {
						V_emisario_nucleo_bean emisario_nucleo_bean_lst  = (V_emisario_nucleo_bean)lst.get(j);
						int contador = 0;
						for (int g = 0; g < lsttramo_emisario.size(); g++)
			            {
							V_tramo_emisario_bean tramo_emisario_bean   = (V_tramo_emisario_bean)lsttramo_emisario.get(g);
							if(tramo_emisario_bean.getProvincia().equals(emisario_nucleo_bean_lst.getProvincia()) && 
									tramo_emisario_bean.getMunicipio().equals(emisario_nucleo_bean_lst.getMunicipio()) && 
									tramo_emisario_bean.getOrden_emis().equals(emisario_nucleo_bean_lst.getOrden_emis())){
								contador ++;
							}
			            }
						if(contador == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro31.V_02") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append( emisario_nucleo_bean_lst.getProvincia() + emisario_nucleo_bean_lst.getMunicipio() + 
									 emisario_nucleo_bean_lst.getOrden_emis()+"\t");
							 error = true;
						}
		            }
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03) 
				for (int i = 0; i < lstemisario_enc.size(); i++)
	            {
					V_emisario_enc_bean emisario_enc_bean    = (V_emisario_enc_bean)lstemisario_enc.get(i);
					if(emisario_enc_bean.getDistancia() <= 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro30.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( emisario_enc_bean.getClave() + emisario_enc_bean.getProvincia() + 
								 emisario_enc_bean.getMunicipio()  + emisario_enc_bean.getOrden_emis() + "\t");
						
					}
	            }
			}
			str.append("\n\n");

        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro31.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
