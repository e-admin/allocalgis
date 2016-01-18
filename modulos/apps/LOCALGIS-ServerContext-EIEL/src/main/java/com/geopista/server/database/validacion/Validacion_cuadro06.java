/**
 * Validacion_cuadro06.java
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
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro06 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro06.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList lstcap_agua_nucleo = new ArrayList();
	    ArrayList lstred_distribucion = new ArrayList();
	    ArrayList lstnucl_encuestado_2 = new ArrayList();
	    ArrayList lstnucl_encuestado_3 = new ArrayList();
	    ArrayList lstcond_agua_nucleo = new ArrayList();
	    ArrayList lstdeposito_agua_nucleo = new ArrayList();
	    ArrayList lstpadron = new ArrayList();
	    ArrayList lstcaptacion_enc_m50 = new ArrayList();
	        
	    boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			
			str.append(Messages.getString("cuadro06") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select DISTINCT PROVINCIA, MUNICIPIO, ENTIDAD, NUCLEO from v_CAP_AGUA_NUCLEO";
		    String sql_1 = "select * from v_RED_DISTRIBUCION";
		    String sql_2 = "select * from v_NUCL_ENCUESTADO_2";
	        String sql_3 = "select * from v_NUCL_ENCUESTADO_3";
	        String sql_4 = "select * from v_COND_AGUA_NUCLEO";
	        String sql_5 = "select * from v_DEPOSITO_AGUA_NUCLEO";
	        String sql_6 = "select * from v_PADRON";
	        String sql_7 = "select * from v_CAPTACION_ENC_M50";
	   
	       
	        
	        ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_cap_agua_nucleo_bean cap_agua_nucleo = new V_cap_agua_nucleo_bean();
				cap_agua_nucleo.setProvincia(rs.getString("PROVINCIA"));
				cap_agua_nucleo.setMunicipio(rs.getString("MUNICIPIO"));
				cap_agua_nucleo.setEntidad(rs.getString("ENTIDAD"));
				cap_agua_nucleo.setNucleo(rs.getString("NUCLEO"));		
				
				lstcap_agua_nucleo.add(cap_agua_nucleo);
				
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_red_distribucion_bean red_distribucion_bean = new V_red_distribucion_bean();

				red_distribucion_bean.setProvincia(rs.getString("PROVINCIA"));
				red_distribucion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				red_distribucion_bean.setEntidad(rs.getString("ENTIDAD"));
				red_distribucion_bean.setNucleo(rs.getString("NUCLEO"));
				red_distribucion_bean.setTipo_rdis(rs.getString("TIPO_RDIS"));
				red_distribucion_bean.setSist_trans(rs.getString("SIST_TRANS"));
				red_distribucion_bean.setEstado(rs.getString("ESTADO"));
				red_distribucion_bean.setTitular(rs.getString("TITULAR"));
				red_distribucion_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					red_distribucion_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					red_distribucion_bean.setLongitud(0);	
				
				lstred_distribucion.add(red_distribucion_bean);
				
			}
			
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_2_bean  nucl_encuestado_2_bean = new V_nucl_encuestado_2_bean();

				nucl_encuestado_2_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_2_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_2_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_2_bean.setAag_caudal(rs.getString("AAG_CAUDAL"));
				nucl_encuestado_2_bean.setAag_restri(rs.getString("AAG_RESTRI"));
				nucl_encuestado_2_bean.setAag_contad(rs.getString("AAG_CONTAD"));
				nucl_encuestado_2_bean.setAag_tasa(rs.getString("AAG_TASA"));
				nucl_encuestado_2_bean.setAag_instal(rs.getString("AAG_INSTAL"));
				nucl_encuestado_2_bean.setAag_hidran(rs.getString("AAG_HIDRAN"));
				nucl_encuestado_2_bean.setAag_est_hi(rs.getString("AAG_EST_HI"));
				nucl_encuestado_2_bean.setAag_valvul(rs.getString("AAG_VALVUL"));
				nucl_encuestado_2_bean.setAag_est_va(rs.getString("AAG_EST_VA"));
				nucl_encuestado_2_bean.setAag_bocasr(rs.getString("AAG_BOCASR"));
				nucl_encuestado_2_bean.setAag_est_bo(rs.getString("AAG_EST_BO"));
				nucl_encuestado_2_bean.setCisterna(rs.getString("CISTERNA"));
			
				lstnucl_encuestado_2.add(nucl_encuestado_2_bean);
				
			}

			ps = connection.prepareStatement(sql_3);
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
			
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_cond_agua_nucleo_bean cond_agua_nucleo_bean = new V_cond_agua_nucleo_bean();

				cond_agua_nucleo_bean.setProvincia(rs.getString("provincia"));
				cond_agua_nucleo_bean.setMunicipio(rs.getString("municipio"));
				cond_agua_nucleo_bean.setEntidad(rs.getString("entidad"));
				cond_agua_nucleo_bean.setNucleo(rs.getString("nucleo"));
				cond_agua_nucleo_bean.setClave(rs.getString("clave"));
				cond_agua_nucleo_bean.setCond_provi(rs.getString("cond_provi"));
				cond_agua_nucleo_bean.setCond_munic(rs.getString("cond_munic"));
				cond_agua_nucleo_bean.setOrden_cond(rs.getString("orden_cond"));

				lstcond_agua_nucleo.add(cond_agua_nucleo_bean);
				
			}

			ps = connection.prepareStatement(sql_5);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean = new V_deposito_agua_nucleo_bean();
				deposito_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				deposito_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				deposito_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				deposito_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				deposito_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
				deposito_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));
				deposito_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));
				deposito_agua_nucleo_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
	
				lstdeposito_agua_nucleo.add(deposito_agua_nucleo_bean);
				
			}

			ps = connection.prepareStatement(sql_6);
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
			
			ps = connection.prepareStatement(sql_7);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_captacion_enc_m50_bean captacion_enc_m50_bean = new V_captacion_enc_m50_bean();

				captacion_enc_m50_bean.setProvincia(rs.getString("provincia"));
				captacion_enc_m50_bean.setMunicipio(rs.getString("municipio"));
				captacion_enc_m50_bean.setClave(rs.getString("clave"));
				captacion_enc_m50_bean.setOrden_capt(rs.getString("orden_capt"));
				captacion_enc_m50_bean.setDenominaci(rs.getString("denominaci"));
				captacion_enc_m50_bean.setTipo_capt(rs.getString("tipo_capt"));
				captacion_enc_m50_bean.setTitular(rs.getString("titular"));
				captacion_enc_m50_bean.setGestion(rs.getString("gestion"));
				captacion_enc_m50_bean.setSistema_ca(rs.getString("sistema_ca"));
				captacion_enc_m50_bean.setEstado(rs.getString("estado"));
				captacion_enc_m50_bean.setUso(rs.getString("uso"));
				captacion_enc_m50_bean.setProteccion(rs.getString("proteccion"));
				captacion_enc_m50_bean.setContador(rs.getString("contador"));

				lstcaptacion_enc_m50.add(captacion_enc_m50_bean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				 //ERROR DEL MPT -> (V_01) 
				for (int i=0; i< lstcap_agua_nucleo.size(); i++){
					V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(i);
					
					int contador = 0;
					for (int j=0; j< lstred_distribucion.size(); j++){
						V_red_distribucion_bean red_distribucion_bean = (V_red_distribucion_bean)lstred_distribucion.get(j);
						
						if(red_distribucion_bean.getProvincia().equals(cap_agua_nucleo.getProvincia()) && 
								red_distribucion_bean.getMunicipio().equals(cap_agua_nucleo.getMunicipio()) &&
								red_distribucion_bean.getEntidad().equals(cap_agua_nucleo.getEntidad()) &&
								red_distribucion_bean.getNucleo().equals(cap_agua_nucleo.getNucleo()) ){
							contador ++;
						}
					}
					
					if(contador == 0){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro06.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(cap_agua_nucleo.getProvincia()+ cap_agua_nucleo.getMunicipio() + 
								 cap_agua_nucleo.getEntidad() + cap_agua_nucleo.getNucleo() + "\t");
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
				for (int i=0; i< lstcap_agua_nucleo.size(); i++){
					V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(i);
					
					int contador = 0;
					for (int j=0; j< lstnucl_encuestado_2.size(); j++){
						V_nucl_encuestado_2_bean nucl_encuestado_2_bean = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(j);
						
						if(nucl_encuestado_2_bean.getProvincia().equals(cap_agua_nucleo.getProvincia()) && 
								nucl_encuestado_2_bean.getMunicipio().equals(cap_agua_nucleo.getMunicipio()) &&
								nucl_encuestado_2_bean.getEntidad().equals(cap_agua_nucleo.getEntidad()) &&
								nucl_encuestado_2_bean.getNucleo().equals(cap_agua_nucleo.getNucleo()) ){
							if(!nucl_encuestado_2_bean.getAag_caudal().equals("SF") && 
									!nucl_encuestado_2_bean.getAag_caudal().equals("IN")){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro06.V_02") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(cap_agua_nucleo.getProvincia()+ cap_agua_nucleo.getMunicipio() + 
										 cap_agua_nucleo.getEntidad() + cap_agua_nucleo.getNucleo() +"\t");
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
			
			if(lstValCuadros.contains("v03")){
				 //ERROR DEL MPT -> (V_03) 
				for (int i=0; i< lstcap_agua_nucleo.size(); i++){
					V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(i);
					
					int contador = 0;
					for (int j=0; j< lstnucl_encuestado_3.size(); j++){
						V_nucl_encuestado_3_bean nucl_encuestado_3_bean = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
						
						if(nucl_encuestado_3_bean.getProvincia().equals(cap_agua_nucleo.getProvincia()) && 
								nucl_encuestado_3_bean.getMunicipio().equals(cap_agua_nucleo.getMunicipio()) &&
								nucl_encuestado_3_bean.getEntidad().equals(cap_agua_nucleo.getEntidad()) &&
								nucl_encuestado_3_bean.getNucleo().equals(cap_agua_nucleo.getNucleo()) ){
							if((nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon()) <= 0){
								if (contTexto == 0)
								 {
									 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro06.V_03") + "\n");
									 str.append("\n");
									 contTexto++;
								 }
								 str.append(cap_agua_nucleo.getProvincia()+ cap_agua_nucleo.getMunicipio() + 
										 cap_agua_nucleo.getEntidad() + cap_agua_nucleo.getNucleo()+"\t");
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
			if(lstValCuadros.contains("v04")){
				
				 //ERROR DEL MPT -> (V_04) 
				for (int i=0; i< lstcap_agua_nucleo.size(); i++){
					V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo.get(i);
					
					int contador = 0;
					
					for (int j=0; j< lstcond_agua_nucleo.size(); j++){
						V_cond_agua_nucleo_bean cond_agua_nucleo_bean = (V_cond_agua_nucleo_bean)lstcond_agua_nucleo.get(j);
						
						if(cond_agua_nucleo_bean.getProvincia().equals(cap_agua_nucleo.getProvincia()) && 
								cond_agua_nucleo_bean.getMunicipio().equals(cap_agua_nucleo.getMunicipio()) &&
								cond_agua_nucleo_bean.getEntidad().equals(cap_agua_nucleo.getEntidad()) &&
								cond_agua_nucleo_bean.getNucleo().equals(cap_agua_nucleo.getNucleo()) ){
							contador ++;
						}
					}
					
					if(contador == 0){//si no esta en cuadro 9 comprobamos si esta en el 14
						int contador2 = 0;
						for (int j=0; j< lstdeposito_agua_nucleo.size(); j++){
							V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean = (V_deposito_agua_nucleo_bean)lstdeposito_agua_nucleo.get(j);
							
							if(deposito_agua_nucleo_bean.getProvincia().equals(cap_agua_nucleo.getProvincia()) && 
									deposito_agua_nucleo_bean.getMunicipio().equals(cap_agua_nucleo.getMunicipio()) &&
									deposito_agua_nucleo_bean.getEntidad().equals(cap_agua_nucleo.getEntidad()) &&
									deposito_agua_nucleo_bean.getNucleo().equals(cap_agua_nucleo.getNucleo()) ){
								contador2 ++;
							}
						}
						
						if(contador2 == 0){//Si no esta en ninguno de los dos error
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro06.V_04") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(cap_agua_nucleo.getProvincia()+ cap_agua_nucleo.getMunicipio() + 
									 cap_agua_nucleo.getEntidad() + cap_agua_nucleo.getNucleo() + "\t");
							 error = true;
						}
					}
					
					
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
				
			if(lstValCuadros.contains("v05")){
				 //ERROR DEL MPT -> (V_05) 
				ArrayList lstcap_agua_nucleo_v_05 = new ArrayList();
				String sqlV_05 = "select DISTINCT CLAVE, C_PROVINC, C_MUNICIP, ORDEN_CAPT from v_CAP_AGUA_NUCLEO";
				ps = connection.prepareStatement(sqlV_05);
				rs = ps.executeQuery();
	
				while (rs.next()) {	
					V_cap_agua_nucleo_bean cap_agua_nucleo = new V_cap_agua_nucleo_bean();
		
					cap_agua_nucleo.setClave(rs.getString("CLAVE"));
					cap_agua_nucleo.setC_provinc(rs.getString("C_PROVINC"));
					cap_agua_nucleo.setC_municip(rs.getString("C_MUNICIP"));
					cap_agua_nucleo.setOrden_capt(rs.getString("ORDEN_CAPT"));
					
					lstcap_agua_nucleo_v_05.add(cap_agua_nucleo);
					
				}
				
				for (int i=0; i< lstcap_agua_nucleo_v_05.size(); i++){
					V_cap_agua_nucleo_bean cap_agua_nucleo = (V_cap_agua_nucleo_bean)lstcap_agua_nucleo_v_05.get(i);
					
					int contador = 0;	
					for (int j=0; j< lstpadron.size(); j++){
						V_padron_bean padron_bean = (V_padron_bean)lstpadron.get(j);
						
						if(padron_bean.getCodprov().equals(cap_agua_nucleo.getC_provinc()) && 
								padron_bean.getCodmunic().equals(cap_agua_nucleo.getC_municip()) &&
								padron_bean.getTotal_poblacion_a1() > 50000){
							contador ++;
						}
						
					}
					
					if(contador != 0){
						int contador2 = 0;
						for (int j=0; j< lstcaptacion_enc_m50.size(); j++){
							V_captacion_enc_m50_bean captacion_enc_m50_bean = (V_captacion_enc_m50_bean)lstcaptacion_enc_m50.get(j);
							//comprobamos si existe en CAPTACION_ENC_M50
							if(captacion_enc_m50_bean.getProvincia().equals(cap_agua_nucleo.getC_provinc()) && 
									captacion_enc_m50_bean.getMunicipio().equals(cap_agua_nucleo.getC_municip()) &&
									captacion_enc_m50_bean.getOrden_capt().equals(cap_agua_nucleo.getOrden_capt())){
								contador2 ++;
							}
							
						}
						
						if(contador2 == 0){
							//si no EXISTE
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro06.V_05") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(cap_agua_nucleo.getClave() + cap_agua_nucleo.getC_provinc()+ 
									 cap_agua_nucleo.getC_municip() + cap_agua_nucleo.getOrden_capt()+"\t");
	
						}
					}
					
					
				}
			}
				str.append("\n\n");
				
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro06.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
