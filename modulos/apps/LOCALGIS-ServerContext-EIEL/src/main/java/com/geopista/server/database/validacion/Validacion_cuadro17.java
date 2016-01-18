/**
 * Validacion_cuadro17.java
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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro17 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro17.class);
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro17") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql_distinct = "select distinct PROVINCIA, MUNICIPIO, ENTIDAD, NUCLEO from v_TRAT_POTA_NUCLEO";
			String sql_distinct2 = "select distinct CLAVE, PO_PROVIN, PO_MUNIPI, ORDEN_TRAT from v_TRAT_POTA_NUCLEO";
			String sql = "select * from v_TRAT_POTA_NUCLEO";
	        String sql_1 = "select * from v_NUCL_ENCUESTADO_3";
	        String sql_2 = "select * from v_NUCL_ENCUESTADO_4";
	        String sql_3 = "select * from v_PADRON";
	        String sql_4 = "select * from v_POTABILIZACION_ENC_M50";
	       
		        
	        ArrayList lsttrat_pota_nucleo_distinct = new ArrayList();
	        ArrayList lsttrat_pota_nucleo_distinct2 = new ArrayList();
			ArrayList lsttrat_pota_nucleo = new ArrayList();
			ArrayList lstnucl_encuestado_3 = new ArrayList();
			ArrayList lstnucl_encuestado_4 = new ArrayList();
			ArrayList lstpadron = new ArrayList();
			ArrayList lstpotabilizacion_enc_m50 = new ArrayList();
			
			
			ps = connection.prepareStatement(sql_distinct);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_trat_pota_nucleo_bean trat_pota_nucleo_bean = new V_trat_pota_nucleo_bean();
				trat_pota_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				trat_pota_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				trat_pota_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				trat_pota_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				
				lsttrat_pota_nucleo_distinct.add(trat_pota_nucleo_bean);
				
			}
			
			ps = connection.prepareStatement(sql_distinct2);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_trat_pota_nucleo_bean trat_pota_nucleo_bean = new V_trat_pota_nucleo_bean();
				trat_pota_nucleo_bean.setClave(rs.getString("CLAVE"));
				trat_pota_nucleo_bean.setPo_provin(rs.getString("PO_PROVIN"));
				trat_pota_nucleo_bean.setPo_munipi(rs.getString("PO_MUNIPI"));
				trat_pota_nucleo_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				
				lsttrat_pota_nucleo_distinct2.add(trat_pota_nucleo_bean);
				
			}
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_trat_pota_nucleo_bean trat_pota_nucleo_bean = new V_trat_pota_nucleo_bean();

				trat_pota_nucleo_bean.setClave(rs.getString("CLAVE"));
				trat_pota_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				trat_pota_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				trat_pota_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				trat_pota_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				trat_pota_nucleo_bean.setPo_provin(rs.getString("PO_PROVIN"));
				trat_pota_nucleo_bean.setPo_munipi(rs.getString("PO_MUNIPI"));
				trat_pota_nucleo_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));

				
				lsttrat_pota_nucleo.add(trat_pota_nucleo_bean);
				
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
				V_nucl_encuestado_4_bean  nucl_encuestado_4_bean = new V_nucl_encuestado_4_bean();

				nucl_encuestado_4_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_4_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_4_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_4_bean.setNucleo(rs.getString("NUCLEO"));
				if(rs.getString("AAU_VIVIEN")!=null&&!rs.getString("AAU_VIVIEN").equals("")){
					nucl_encuestado_4_bean.setAau_vivien(new Integer(rs.getString("AAU_VIVIEN")));
				}else
					nucl_encuestado_4_bean.setAau_vivien(0);
				if(rs.getString("AAU_POB_RE")!=null&&!rs.getString("AAU_POB_RE").equals("")){
					nucl_encuestado_4_bean.setAau_pob_re(new Integer(rs.getString("AAU_POB_RE")));
				}else
					nucl_encuestado_4_bean.setAau_pob_re(0);
				if(rs.getString("AAU_POB_ES")!=null&&!rs.getString("AAU_POB_ES").equals("")){
					nucl_encuestado_4_bean.setAau_pob_es(new Integer(rs.getString("AAU_POB_ES")));
				}else	
					nucl_encuestado_4_bean.setAau_pob_es(0);
				if(rs.getString("AAU_DEF_VI")!=null&&!rs.getString("AAU_DEF_VI").equals("")){
					nucl_encuestado_4_bean.setAau_def_vi(new Integer(rs.getString("AAU_DEF_VI")));
				}else
					nucl_encuestado_4_bean.setAau_def_vi(0);
				if(rs.getString("AAU_DEF_RE")!=null&&!rs.getString("AAU_DEF_RE").equals("")){
					nucl_encuestado_4_bean.setAau_def_re(new Integer(rs.getString("AAU_DEF_RE")));
				}else
					nucl_encuestado_4_bean.setAau_def_re(0);
				if(rs.getString("AAU_DEF_ES")!=null&&!rs.getString("AAU_DEF_ES").equals("")){
					nucl_encuestado_4_bean.setAau_def_es(new Integer(rs.getString("AAU_DEF_ES")));
				}else
					nucl_encuestado_4_bean.setAau_def_es(0);
				if(rs.getString("AAU_FECONT")!=null&&!rs.getString("AAU_FECONT").equals("")){
					nucl_encuestado_4_bean.setAau_fecont(new Integer(rs.getString("AAU_FECONT")));
				}else
					nucl_encuestado_4_bean.setAau_fecont(0);
				if(rs.getString("AAU_FENCON")!=null&&!rs.getString("AAU_FENCON").equals("")){
					nucl_encuestado_4_bean.setAau_fencon(new Integer(rs.getString("AAU_FENCON")));
				}else
					nucl_encuestado_4_bean.setAau_fencon(0);

				nucl_encuestado_4_bean.setAau_caudal(rs.getString("AAU_CAUDAL"));


				lstnucl_encuestado_4.add(nucl_encuestado_4_bean);
				
			}
			
			ps = connection.prepareStatement(sql_3);
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

			
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_m50_bean  potabilizacion_enc_m50_bean = new V_potabilizacion_enc_m50_bean();

				potabilizacion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				potabilizacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				potabilizacion_enc_m50_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				potabilizacion_enc_m50_bean.setTipo_tra(rs.getString("TIPO_TRA"));
				potabilizacion_enc_m50_bean.setUbicacion(rs.getString("UBICACION"));
				potabilizacion_enc_m50_bean.setS_desinf(rs.getString("S_DESINF"));
				potabilizacion_enc_m50_bean.setCat_a1(rs.getString("CAT_A1"));
				potabilizacion_enc_m50_bean.setCat_a2(rs.getString("CAT_A2"));
				potabilizacion_enc_m50_bean.setCat_a3(rs.getString("CAT_A3"));
				potabilizacion_enc_m50_bean.setDesaladora(rs.getString("DESALADORA"));
				potabilizacion_enc_m50_bean.setOtros(rs.getString("OTROS"));
				potabilizacion_enc_m50_bean.setDesinf_1(rs.getString("DESINF_1"));
				potabilizacion_enc_m50_bean.setDesinf_2(rs.getString("DESINF_2"));
				potabilizacion_enc_m50_bean.setDesinf_3(rs.getString("DESINF_3"));
				potabilizacion_enc_m50_bean.setPeriodicid(rs.getString("PERIODICID"));
				potabilizacion_enc_m50_bean.setOrganismo(rs.getString("ORGANISMO"));
				potabilizacion_enc_m50_bean.setEstado(rs.getString("ESTADO"));
				
				lstpotabilizacion_enc_m50.add(potabilizacion_enc_m50_bean);
				
			}
			
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lsttrat_pota_nucleo_distinct.size(); i++)
	            {
					int suma_viv_3 = 0;
	                int deficitPota = 0;
	                
					V_trat_pota_nucleo_bean trat_pota_nucleo_bean  = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo_distinct.get(i);
					for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
		            {
						V_nucl_encuestado_3_bean nucl_encuestado_3_bean = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
						
						if( nucl_encuestado_3_bean.getProvincia().equals(trat_pota_nucleo_bean.getProvincia()) &&
								nucl_encuestado_3_bean.getMunicipio().equals(trat_pota_nucleo_bean.getMunicipio()) &&
								nucl_encuestado_3_bean.getEntidad().equals(trat_pota_nucleo_bean.getEntidad()) &&
								nucl_encuestado_3_bean.getNucleo().equals(trat_pota_nucleo_bean.getNucleo())){
							
							suma_viv_3 = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon();
						}
		            }
					
					for (int j = 0; j < lstnucl_encuestado_4.size(); j++)
		            {
						V_nucl_encuestado_4_bean nucl_encuestado_4_bean = (V_nucl_encuestado_4_bean)lstnucl_encuestado_4.get(j);
						
						if( nucl_encuestado_4_bean.getProvincia().equals(trat_pota_nucleo_bean.getProvincia()) &&
								nucl_encuestado_4_bean.getMunicipio().equals(trat_pota_nucleo_bean.getMunicipio()) &&
								nucl_encuestado_4_bean.getEntidad().equals(trat_pota_nucleo_bean.getEntidad()) &&
								nucl_encuestado_4_bean.getNucleo().equals(trat_pota_nucleo_bean.getNucleo())){
							
							deficitPota	 = nucl_encuestado_4_bean.getAau_def_vi();
						}
		            }
					
					if (suma_viv_3 == deficitPota){
									
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro17.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( trat_pota_nucleo_bean.getProvincia()+ trat_pota_nucleo_bean.getMunicipio() + 
								 trat_pota_nucleo_bean.getEntidad() + trat_pota_nucleo_bean.getNucleo() + "\t");
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
				for (int i = 0; i < lsttrat_pota_nucleo_distinct.size(); i++)
	            {
					int count = 0;
					int suma_viv_3 = 0;
					V_trat_pota_nucleo_bean trat_pota_nucleo_bean  = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo_distinct.get(i);
					for (int j = 0; j < lstnucl_encuestado_3.size(); j++)
		            {
						V_nucl_encuestado_3_bean nucl_encuestado_3_bean = (V_nucl_encuestado_3_bean)lstnucl_encuestado_3.get(j);
						
						if( nucl_encuestado_3_bean.getProvincia().equals(trat_pota_nucleo_bean.getProvincia()) &&
								nucl_encuestado_3_bean.getMunicipio().equals(trat_pota_nucleo_bean.getMunicipio()) &&
								nucl_encuestado_3_bean.getEntidad().equals(trat_pota_nucleo_bean.getEntidad()) &&
								nucl_encuestado_3_bean.getNucleo().equals(trat_pota_nucleo_bean.getNucleo())){
							
							suma_viv_3 = nucl_encuestado_3_bean.getAag_v_cone() + nucl_encuestado_3_bean.getAag_v_ncon();
							if(suma_viv_3 <= 0){
								if (contTexto == 0)
									 {
										 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro17.V_02") + "\n");
										 str.append("\n");
										 contTexto++;
									 }
									 str.append(trat_pota_nucleo_bean.getProvincia()+ trat_pota_nucleo_bean.getMunicipio() + 
											 trat_pota_nucleo_bean.getEntidad()+ trat_pota_nucleo_bean.getNucleo() +"\t");
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
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lsttrat_pota_nucleo.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
	
					codIne_bean.setProvincia(((V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(i)).getMunicipio());
					codIne_bean.setEntidad(((V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(i)).getEntidad());
					codIne_bean.setNucleo(((V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(i)).getNucleo());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_RED_DISTRIBUCION", "cuadro17.V_03", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04)
				for (int i = 0; i < lsttrat_pota_nucleo_distinct2.size(); i++)
	            {
					V_trat_pota_nucleo_bean trat_pota_nucleo_bean  = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo_distinct2.get(i);
					int count = 0;
					int count4 = 0;
					for (int j = 0; j < lstpadron.size(); j++)
		            {
						V_padron_bean padron  = (V_padron_bean)lstpadron.get(j);
						if(padron.getCodprov().equals(trat_pota_nucleo_bean.getPo_provin()) &&
								padron.getCodmunic().equals(trat_pota_nucleo_bean.getPo_munipi()) && 
								padron.getTotal_poblacion_a1() > 50000){
							count ++;
						}
		            }
					if(count != 0){
						
						for (int j = 0; j < lstpotabilizacion_enc_m50.size(); j++)
			            {
							V_potabilizacion_enc_m50_bean potabilizacion_enc_m50_bean  = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50.get(j);
							if(potabilizacion_enc_m50_bean.getProvincia().equals(trat_pota_nucleo_bean.getPo_provin()) &&
									potabilizacion_enc_m50_bean.getMunicipio().equals(trat_pota_nucleo_bean.getPo_munipi()) && 
									potabilizacion_enc_m50_bean.getOrden_trat().equals(trat_pota_nucleo_bean.getOrden_trat())){
								count4 ++;
							}
			            }
						
						if(count4 == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro17.V_04") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(trat_pota_nucleo_bean.getClave() + trat_pota_nucleo_bean.getPo_provin()+ 
									 trat_pota_nucleo_bean.getPo_munipi() + trat_pota_nucleo_bean.getOrden_trat() +"\t");
						}
					}		
					
	            }
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro17.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
