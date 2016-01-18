/**
 * Validacion_cuadro45.java
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
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadro45 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro45.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {     
			str.append(Messages.getString("cuadro45") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_VERT_ENCUESTADO";
			String sql_1 = "select * from v_VERTEDERO_NUCLEO";
		        
			ArrayList lstvert_encuestado = new ArrayList();
			ArrayList lstvertedero_nucleo = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vert_encuestado_bean  vert_encuestado_bean = new V_vert_encuestado_bean();

				vert_encuestado_bean.setClave(rs.getString("CLAVE"));
				vert_encuestado_bean.setProvincia(rs.getString("PROVINCIA"));
				vert_encuestado_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vert_encuestado_bean.setOrden_ver(rs.getString("ORDEN_VER"));
				vert_encuestado_bean.setTipo_ver(rs.getString("TIPO_VER"));
				vert_encuestado_bean.setTitular(rs.getString("TITULAR"));
				vert_encuestado_bean.setGestion(rs.getString("GESTION"));
				vert_encuestado_bean.setOlores(rs.getString("OLORES"));
				vert_encuestado_bean.setHumos(rs.getString("HUMOS"));
				vert_encuestado_bean.setCont_anima(rs.getString("CONT_ANIMA"));
				vert_encuestado_bean.setR_inun(rs.getString("R_INUN"));
				vert_encuestado_bean.setFiltracion(rs.getString("FILTRACION"));
				vert_encuestado_bean.setImpacto_v(rs.getString("IMPACTO_V"));
				vert_encuestado_bean.setFrec_averi(rs.getString("FREC_AVERI"));
				vert_encuestado_bean.setSaturacion(rs.getString("SATURACION"));
				vert_encuestado_bean.setInestable(rs.getString("INESTABLE"));
				vert_encuestado_bean.setOtros(rs.getString("OTROS"));
				if(rs.getString("CAPAC_TOT")!=null&&!rs.getString("CAPAC_TOT").equals(""))
					vert_encuestado_bean.setCapac_tot(new Integer(rs.getString("CAPAC_TOT")));
				else
					vert_encuestado_bean.setCapac_tot(0);
				if(rs.getString("CAPAC_PORC")!=null&&!rs.getString("CAPAC_PORC").equals(""))
					vert_encuestado_bean.setCapac_porc(new Integer(rs.getString("CAPAC_PORC")));
				else
					vert_encuestado_bean.setCapac_porc(0);
				vert_encuestado_bean.setCapac_ampl(rs.getString("CAPAC_AMPL"));
				
				if(rs.getString("CAPAC_TRAN")!=null&&!rs.getString("CAPAC_TRAN").equals(""))
					vert_encuestado_bean.setCapac_tran(new Integer(rs.getString("CAPAC_TRAN")));
				else
					vert_encuestado_bean.setCapac_tran(0);
				vert_encuestado_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("VIDA_UTIL")!=null&&!rs.getString("VIDA_UTIL").equals(""))
					vert_encuestado_bean.setVida_util(new Integer(rs.getString("VIDA_UTIL")));
				else
					vert_encuestado_bean.setVida_util(0);
				vert_encuestado_bean.setCategoria(rs.getString("CATEGORIA"));
				vert_encuestado_bean.setActividad(rs.getString("ACTIVIDAD"));
				

				lstvert_encuestado.add(vert_encuestado_bean);
				
			}

			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vertedero_nucleo_bean vertedero_nucleo_bean = new V_vertedero_nucleo_bean();

				vertedero_nucleo_bean.setClave(rs.getString("CLAVE"));
				vertedero_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				vertedero_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vertedero_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				vertedero_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				vertedero_nucleo_bean.setVer_provin(rs.getString("VER_PROVIN"));
				vertedero_nucleo_bean.setVer_munici(rs.getString("VER_MUNICI"));
				vertedero_nucleo_bean.setVer_codigo(rs.getString("VER_CODIGO"));

				lstvertedero_nucleo.add(vertedero_nucleo_bean);
				
			}
		
				
				//FALSOERROR DEL MPT -> 
			for (int i = 0; i < lstvert_encuestado.size(); i++)
            {
				V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);

				if(vert_encuestado_bean.getOrden_ver().length() < 3)
				{
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro45.falsoerror.V_1") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
							 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
					 error = true;

	            }
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
				
			//FALSOERROR DEL MPT 
			for (int i = 0; i < lstvert_encuestado.size(); i++)
            {
				V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);
				if(vert_encuestado_bean.getTipo_ver().equals("")){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro45.falsoerror.V_2") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
							 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
					 error = true;

				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
				
	
		
			//FALSOERROR DEL MPT 
			for (int i = 0; i < lstvert_encuestado.size(); i++)
            {
				V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);
				if(vert_encuestado_bean.getActividad().equals("")){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro45.falsoerror.V_3") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
							 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
					 error = true;

				}
            }	
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				for (int i = 0; i < lstvert_encuestado.size(); i++)
	            {
					V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);
					int count = 0;
					for (int j = 0; j < lstvertedero_nucleo.size(); j++)
		            {
						V_vertedero_nucleo_bean vertedero_nucleo_bean   = (V_vertedero_nucleo_bean)lstvertedero_nucleo.get(j);
						if(vertedero_nucleo_bean.getVer_provin().equals(vert_encuestado_bean.getProvincia()) &&
								vertedero_nucleo_bean.getVer_munici().equals(vert_encuestado_bean.getMunicipio()) &&
								vertedero_nucleo_bean.getVer_codigo().equals(vert_encuestado_bean.getOrden_ver())){
							count ++;
						}
						
		            }
					if (count == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro45.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
								 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
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
				for (int i = 0; i < lstvert_encuestado.size(); i++){
					V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);
					if(vert_encuestado_bean.getActividad().equals("EN") &&
							!vert_encuestado_bean.getCategoria().equals("VIN") &&
							vert_encuestado_bean.getVida_util() <= 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro45.V_02") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
								 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
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
				for (int i = 0; i < lstvert_encuestado.size(); i++){
					V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvert_encuestado.get(i);
					if(vert_encuestado_bean.getCapac_porc() > 100){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro45.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( vert_encuestado_bean.getClave() + vert_encuestado_bean.getProvincia() + 
								 vert_encuestado_bean.getMunicipio()  + vert_encuestado_bean.getOrden_ver()+"\t");
						 str.append("\n");
						
						
					}	
				}
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro45.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
