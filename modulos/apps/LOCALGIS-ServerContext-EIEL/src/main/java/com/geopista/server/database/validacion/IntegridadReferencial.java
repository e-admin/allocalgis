/**
 * IntegridadReferencial.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.OrderToMPT;
import com.geopista.server.database.validacion.beans.V_alumbrado_bean;
import com.geopista.server.database.validacion.beans.V_cabildo_consejo_bean;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_agua_bean;
import com.geopista.server.database.validacion.beans.V_carretera_bean;
import com.geopista.server.database.validacion.beans.V_casa_con_uso_bean;
import com.geopista.server.database.validacion.beans.V_casa_consitorial_bean;
import com.geopista.server.database.validacion.beans.V_cementerio_bean;
import com.geopista.server.database.validacion.beans.V_cent_cultural_bean;
import com.geopista.server.database.validacion.beans.V_cent_cultural_usos_bean;
import com.geopista.server.database.validacion.beans.V_centro_asistencial_bean;
import com.geopista.server.database.validacion.beans.V_centro_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_centro_sanitario_bean;
import com.geopista.server.database.validacion.beans.V_colector_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_bean;
import com.geopista.server.database.validacion.beans.V_edific_pub_sin_uso_bean;
import com.geopista.server.database.validacion.beans.V_emisario_bean;
import com.geopista.server.database.validacion.beans.V_emisario_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_entidad_singular_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_inst_depor_deporte_bean;
import com.geopista.server.database.validacion.beans.V_instal_deportiva_bean;
import com.geopista.server.database.validacion.beans.V_lonja_merc_feria_bean;
import com.geopista.server.database.validacion.beans.V_matadero_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_municipio_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_nuc_abandonado_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucleo_poblacion_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_parque_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_poblamiento_bean;
import com.geopista.server.database.validacion.beans.V_proteccion_civil_bean;
import com.geopista.server.database.validacion.beans.V_ramal_saneamiento_bean;
import com.geopista.server.database.validacion.beans.V_recogida_basura_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_sanea_autonomo_bean;
import com.geopista.server.database.validacion.beans.V_tanatorio_bean;
import com.geopista.server.database.validacion.beans.V_tra_potabilizacion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;
import com.geopista.server.database.validacion.beans.ValidateQueryMPTBean;
import com.geopista.util.LocalGISEIELUtils;
import com.geopista.util.config.UserPreferenceStore;

public class IntegridadReferencial {
	
	
	public static void validacion(Connection connection, StringBuffer str,String fase){
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    ArrayList lstVistas = new ArrayList ();
	    
		try
        {  
			str.append(Messages.getString("integridadReferencial") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select query_validate_mpt.tipo,query_validate_mpt.nombre,query_validate_mpt.query AS vista,dictionary.traduccion AS descripcion from query_validate_mpt" +
							" left join dictionary on query_validate_mpt.id_descripcion=dictionary.id_vocablo AND locale='"+UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", false)+"'";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				ValidateQueryMPTBean  validateQueryMPTBean = new ValidateQueryMPTBean();

				validateQueryMPTBean.setTipo(rs.getString("TIPO"));
				validateQueryMPTBean.setName(rs.getString("NOMBRE"));
				validateQueryMPTBean.setVista(rs.getString("VISTA"));
				validateQueryMPTBean.setDescripcion(rs.getString("DESCRIPCION"));

				lstVistas.add(validateQueryMPTBean);
	
			}
			ArrayList lstType=null;
			Hashtable<String, ArrayList> datosMPTErroneos=new Hashtable<String, ArrayList>() ;
			for (Iterator iterator = lstVistas.iterator(); iterator.hasNext();) {
				ValidateQueryMPTBean vista = (ValidateQueryMPTBean) iterator.next();
				String sql_vista="select * from "+vista.getVista();
				lstType=new ArrayList();
				ps = connection.prepareStatement(sql_vista);
				rs = ps.executeQuery();
				if (vista.getVista().equals("check_mpt_alumbrado")){
					while (rs.next()) 
					{
									
							V_alumbrado_bean alumbradoBean = new V_alumbrado_bean();
							alumbradoBean.setProvincia(rs.getString("provincia"));
							alumbradoBean.setMunicipio(rs.getString("municipio"));
							alumbradoBean.setEntidad(rs.getString("entidad"));
							alumbradoBean.setNucleo(rs.getString("nucleo"));
							alumbradoBean.setAh_ener_rl(rs.getString("ah_ener_rl"));
							alumbradoBean.setAh_ener_ri(rs.getString("ah_ener_ri"));
							alumbradoBean.setCalidad(rs.getString("calidad"));
							if(rs.getString("pot_instal")!=null&&!rs.getString("pot_instal").equals(""))
								alumbradoBean.setPot_instal(Double.parseDouble(rs.getString("pot_instal") ));
							else
								alumbradoBean.setPot_instal(0.0);
							alumbradoBean.setPuntos_luz(rs.getInt("n_puntos"));
							lstType.add(alumbradoBean);
					}
				}else if (vista.getVista().equals("check_mpt_infraestr_viaria")){
					while (rs.next()) {	
						V_infraestr_viaria_bean infraestr_viaria_bean = new V_infraestr_viaria_bean();

						infraestr_viaria_bean.setProvincia(rs.getString("PROVINCIA"));
						infraestr_viaria_bean.setMunicipio(rs.getString("MUNICIPIO"));
						infraestr_viaria_bean.setEntidad(rs.getString("ENTIDAD"));
						infraestr_viaria_bean.setNucleo(rs.getString("POBLAMIENT"));
						infraestr_viaria_bean.setTipo_infr(rs.getString("TIPO_INFR"));
						infraestr_viaria_bean.setEstado(rs.getString("ESTADO"));
						if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
							infraestr_viaria_bean.setLongitud(new Integer(rs.getString("LONGITUD")));
						else
							infraestr_viaria_bean.setLongitud(0);
						if(rs.getString("SUPERFICIE")!=null&&!rs.getString("SUPERFICIE").equals(""))
							infraestr_viaria_bean.setSuperficie(new Integer(rs.getString("SUPERFICIE")));
						else
							infraestr_viaria_bean.setSuperficie(0);
						infraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));

						lstType.add(infraestr_viaria_bean);

					}
				}else if (vista.getVista().equals("check_mpt_recogida_basura")){
					while (rs.next()) {	
						V_recogida_basura_bean v_recogida_basura_bean = new V_recogida_basura_bean();

						v_recogida_basura_bean.setProvincia(rs.getString("PROVINCIA"));
						v_recogida_basura_bean.setMunicipio(rs.getString("MUNICIPIO"));
						v_recogida_basura_bean.setEntidad(rs.getString("ENTIDAD"));
						v_recogida_basura_bean.setNucleo(rs.getString("NUCLEO"));
						v_recogida_basura_bean.setTipo_rbas(rs.getString("TIPO_RBAS"));
						v_recogida_basura_bean.setGestion(rs.getString("GESTION"));
						v_recogida_basura_bean.setPeriodicid(rs.getString("PERIODICID"));
						v_recogida_basura_bean.setCalidad(rs.getString("CALIDAD"));
						if(rs.getString("PRODU_BASU")!=null&&!rs.getString("PRODU_BASU").equals(""))
							v_recogida_basura_bean.setProdu_basu(LocalGISEIELUtils.redondear(Double.parseDouble(rs.getString("PRODU_BASU")),1));
						else
							v_recogida_basura_bean.setProdu_basu(Double.parseDouble(rs.getString("0.0")));
						if(rs.getString("CONTENEDOR")!=null&&!rs.getString("CONTENEDOR").equals(""))
							v_recogida_basura_bean.setContenedor(new Integer(rs.getString("CONTENEDOR")));
						else
							v_recogida_basura_bean.setContenedor(0);

						lstType.add(v_recogida_basura_bean);
					}
				}else if (vista.getVista().equals("check_mpt_red_distribucion")){
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
						lstType.add(red_distribucion_bean);
					}
				}else if (vista.getVista().equals("check_mpt_cap_agua")){
					while (rs.next()) {	
						V_captacion_agua_bean captacion_aguaBean = new V_captacion_agua_bean();

						captacion_aguaBean.setClave(rs.getString("clave"));
						captacion_aguaBean.setProvincia(rs.getString("provincia"));
						captacion_aguaBean.setMunicipio(rs.getString("municipio"));
						captacion_aguaBean.setOrden_capt(rs.getString("orden_capt"));

						lstType.add(captacion_aguaBean);

						
					}
				}else if (vista.getVista().equals("check_mpt_cap_agua_nucleo")){
					while (rs.next()) {	
						V_cap_agua_nucleo_bean cap_agua_nucleoBean = new V_cap_agua_nucleo_bean();

						cap_agua_nucleoBean.setProvincia(rs.getString("provincia"));
						cap_agua_nucleoBean.setMunicipio(rs.getString("municipio"));
						cap_agua_nucleoBean.setEntidad(rs.getString("entidad"));
						cap_agua_nucleoBean.setNucleo(rs.getString("nucleo"));
						cap_agua_nucleoBean.setClave(rs.getString("clave"));
						cap_agua_nucleoBean.setC_provinc(rs.getString("c_provinc"));
						cap_agua_nucleoBean.setC_municip(rs.getString("c_municip"));
						cap_agua_nucleoBean.setOrden_capt(rs.getString("orden_capt"));

						lstType.add(cap_agua_nucleoBean);

						
					}
				}else if (vista.getVista().equals("check_mpt_mun_enc_dis")){
					while (rs.next()) {	
						V_mun_enc_dis_bean mun_enc_dis_bean = new V_mun_enc_dis_bean();

						mun_enc_dis_bean.setCodprov(rs.getString("PROVINCIA"));
						mun_enc_dis_bean.setCodmunic(rs.getString("MUNICIPIO"));
						if(rs.getString("PADRON")!=null&&!rs.getString("PADRON").equals(""))
							mun_enc_dis_bean.setPadron(new Integer(rs.getString("PADRON")));
						else
							mun_enc_dis_bean.setPadron(0);
						if(rs.getString("POB_ESTACI")!=null&&!rs.getString("POB_ESTACI").equals(""))
							mun_enc_dis_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
						else
							mun_enc_dis_bean.setPob_estaci(0);

						if(rs.getString("VIV_TOTAL")!=null&&!rs.getString("VIV_TOTAL").equals(""))
							mun_enc_dis_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
						else
							mun_enc_dis_bean.setViv_total(0);

						if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))
							mun_enc_dis_bean.setHoteles(new Integer(rs.getString("HOTELES")));
						else
							mun_enc_dis_bean.setHoteles(0);
						if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))
							mun_enc_dis_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
						else
							mun_enc_dis_bean.setCasas_rural(0);
						if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
							mun_enc_dis_bean.setLongitud(new Integer(rs.getString("LONGITUD")));
						else
							mun_enc_dis_bean.setLongitud(0);
						if(rs.getString("AAG_V_CONE")!=null&&!rs.getString("AAG_V_CONE").equals(""))
							mun_enc_dis_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
						else
							mun_enc_dis_bean.setAag_v_cone(0);
						if(rs.getString("AAG_V_NCON")!=null&&!rs.getString("AAG_V_NCON").equals(""))
							mun_enc_dis_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
						else
							mun_enc_dis_bean.setAag_v_ncon(0);
						if(rs.getString("AAG_C_INVI")!=null&&!rs.getString("AAG_C_INVI").equals(""))
							mun_enc_dis_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
						else
							mun_enc_dis_bean.setAag_c_invi(0);
						if(rs.getString("AAG_C_VERA")!=null&&!rs.getString("AAG_C_VERA").equals(""))
							mun_enc_dis_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
						else
							mun_enc_dis_bean.setAag_c_vera(0);
						if(rs.getString("AAG_V_EXPR")!=null&&!rs.getString("AAG_V_EXPR").equals(""))
							mun_enc_dis_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
						else
							mun_enc_dis_bean.setAag_v_expr(0);
						if(rs.getString("AAG_V_DEPR")!=null&&!rs.getString("AAG_V_DEPR").equals(""))
							mun_enc_dis_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
						else
							mun_enc_dis_bean.setAag_v_depr(0);
						if(rs.getString("AAG_L_DEFI")!=null&&!rs.getString("AAG_L_DEFI").equals(""))
							mun_enc_dis_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
						else
							mun_enc_dis_bean.setAag_l_defi(0);
						if(rs.getString("AAG_V_DEFI")!=null&&!rs.getString("AAG_V_DEFI").equals(""))
							mun_enc_dis_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
						else
							mun_enc_dis_bean.setAag_v_defi(0);
						if(rs.getString("AAG_PR_DEF")!=null&&!rs.getString("AAG_PR_DEF").equals(""))
							mun_enc_dis_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
						else
							mun_enc_dis_bean.setAag_pr_def(0);
						if(rs.getString("AAG_PE_DEF")!=null&&!rs.getString("AAG_PE_DEF").equals(""))
							mun_enc_dis_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
						else
							mun_enc_dis_bean.setAag_pe_def(0);
						if(rs.getString("AAU_VIVIEN")!=null&&!rs.getString("AAU_VIVIEN").equals(""))
							mun_enc_dis_bean.setAau_vivien(new Integer(rs.getString("AAU_VIVIEN")));
						else
							mun_enc_dis_bean.setAau_vivien(0);
						if(rs.getString("aau_pob_re")!=null&&!rs.getString("aau_pob_re").equals(""))
							mun_enc_dis_bean.setAau_pob_re(new Integer(rs.getString("aau_pob_re")));
						else
							mun_enc_dis_bean.setAau_pob_re(0);

						if(rs.getString("aau_pob_es")!=null&&!rs.getString("aau_pob_es").equals(""))
							mun_enc_dis_bean.setAau_pob_es(new Integer(rs.getString("aau_pob_es")));
						else
							mun_enc_dis_bean.setAau_pob_es(0);
						if(rs.getString("aau_def_vi")!=null&&!rs.getString("aau_def_vi").equals(""))
							mun_enc_dis_bean.setAau_def_vi(new Integer(rs.getString("aau_def_vi")));
						else
							mun_enc_dis_bean.setAau_def_vi(0);
						if(rs.getString("aau_def_re")!=null&&!rs.getString("aau_def_re").equals(""))
							mun_enc_dis_bean.setAau_def_re(new Integer(rs.getString("aau_def_re")));
						else
							mun_enc_dis_bean.setAau_def_re(0);
						if(rs.getString("aau_def_es")!=null&&!rs.getString("aau_def_es").equals(""))
							mun_enc_dis_bean.setAau_def_es(new Integer(rs.getString("aau_def_es")));
						else
							mun_enc_dis_bean.setAau_def_es(0);
						if(rs.getString("aau_fecont")!=null&&!rs.getString("aau_fecont").equals(""))
							mun_enc_dis_bean.setAau_fecont(new Integer(rs.getString("aau_fecont")));
						else
							mun_enc_dis_bean.setAau_fecont(0);
						if(rs.getString("AAU_FENCON")!=null&&!rs.getString("AAU_FENCON").equals(""))
							mun_enc_dis_bean.setAau_fencon(new Integer(rs.getString("AAU_FENCON")));
						else
							mun_enc_dis_bean.setAau_fencon(0);
						if(rs.getString("LONGIT_RAM")!=null&&!rs.getString("LONGIT_RAM").equals(""))
							mun_enc_dis_bean.setLongi_ramal(new Integer(rs.getString("LONGIT_RAM")));
						else
							mun_enc_dis_bean.setLongi_ramal(0);
						if(rs.getString("SYD_V_CONE")!=null&&!rs.getString("SYD_V_CONE").equals(""))
							mun_enc_dis_bean.setSyd_v_cone(new Integer(rs.getString("SYD_V_CONE")));
						else
							mun_enc_dis_bean.setSyd_v_cone(0);
						if(rs.getString("SYD_V_NCON")!=null&&!rs.getString("SYD_V_NCON").equals(""))
							mun_enc_dis_bean.setSyd_v_ncon(new Integer(rs.getString("SYD_V_NCON")));
						else
							mun_enc_dis_bean.setSyd_v_ncon(0);
						if(rs.getString("SYD_L_DEFI")!=null&&!rs.getString("SYD_L_DEFI").equals(""))
							mun_enc_dis_bean.setSyd_l_defi(new Integer(rs.getString("SYD_L_DEFI")));
						else
							mun_enc_dis_bean.setSyd_l_defi(0);
						if(rs.getString("SYD_V_DEFI")!=null&&!rs.getString("SYD_V_DEFI").equals(""))
							mun_enc_dis_bean.setSyd_v_defi(new Integer(rs.getString("SYD_V_DEFI")));
						else
							mun_enc_dis_bean.setSyd_v_defi(0);
						if(rs.getString("SYD_C_DESA")!=null&&!rs.getString("SYD_C_DESA").equals(""))
							mun_enc_dis_bean.setSyd_c_desa(new Integer(rs.getString("SYD_C_DESA")));
						else
							mun_enc_dis_bean.setSyd_c_desa(0);
						if(rs.getString("SYD_C_TRAT")!=null&&!rs.getString("SYD_C_TRAT").equals(""))
							mun_enc_dis_bean.setSyd_c_trat(new Integer(rs.getString("SYD_C_TRAT")));
						else
							mun_enc_dis_bean.setSyd_c_trat(0);
						if(rs.getString("SAU_VIVIEN")!=null&&!rs.getString("SAU_VIVIEN").equals(""))
							mun_enc_dis_bean.setSau_vivien(new Integer(rs.getString("SAU_VIVIEN")));
						else
							mun_enc_dis_bean.setSau_vivien(0);
						if(rs.getString("SAU_POB_RE")!=null&&!rs.getString("SAU_POB_RE").equals(""))
							mun_enc_dis_bean.setSau_pob_re(new Integer(rs.getString("SAU_POB_RE")));
						else
							mun_enc_dis_bean.setSau_pob_re(0);
						if(rs.getString("SAU_POB_ES")!=null&&!rs.getString("SAU_POB_ES").equals(""))
							mun_enc_dis_bean.setSau_pob_es(new Integer(rs.getString("SAU_POB_ES")));
						else
							mun_enc_dis_bean.setSau_pob_es(0);
						if(rs.getString("SAU_VI_DEF")!=null&&!rs.getString("SAU_VI_DEF").equals(""))
							mun_enc_dis_bean.setSau_vi_def(new Integer(rs.getString("SAU_VI_DEF")));
						else
							mun_enc_dis_bean.setSau_vi_def(0);
						if(rs.getString("SAU_RE_DEF")!=null&&!rs.getString("SAU_RE_DEF").equals(""))
							mun_enc_dis_bean.setSau_re_def(new Integer(rs.getString("SAU_RE_DEF")));
						else
							mun_enc_dis_bean.setSau_re_def(0);
						if(rs.getString("SAU_ES_DEF")!=null&&!rs.getString("SAU_ES_DEF").equals(""))
							mun_enc_dis_bean.setSau_es_def(new Integer(rs.getString("SAU_ES_DEF")));
						else
							mun_enc_dis_bean.setSau_es_def(0);
						if(rs.getString("PRODU_BASU")!=null&&!rs.getString("PRODU_BASU").equals(""))
							mun_enc_dis_bean.setProdu_basu(new Integer(rs.getString("PRODU_BASU")));
						else
							mun_enc_dis_bean.setProdu_basu(0);
						if(rs.getString("CONTENEDOR")!=null&&!rs.getString("CONTENEDOR").equals(""))
							mun_enc_dis_bean.setContenedores(new Integer(rs.getString("CONTENEDOR")));
						else
							mun_enc_dis_bean.setContenedores(0);
						if(rs.getString("RBA_V_SSER")!=null&&!rs.getString("RBA_V_SSER").equals(""))
							mun_enc_dis_bean.setRba_v_sser(new Integer(rs.getString("RBA_V_SSER")));
						else
							mun_enc_dis_bean.setRba_v_sser(0);
						if(rs.getString("RBA_PR_SSE")!=null&&!rs.getString("RBA_PR_SSE").equals(""))
							mun_enc_dis_bean.setRba_pr_sse(new Integer(rs.getString("RBA_PR_SSE")));
						else
							mun_enc_dis_bean.setRba_pr_sse(0);
						if(rs.getString("RBA_PE_SSE")!=null&&!rs.getString("RBA_PE_SSE").equals(""))
							mun_enc_dis_bean.setRba_pe_sse(new Integer(rs.getString("RBA_PE_SSE")));
						else
							mun_enc_dis_bean.setRba_pe_sse(0);
						if(rs.getString("RBA_PLALIM")!=null&&!rs.getString("RBA_PLALIM").equals(""))
							mun_enc_dis_bean.setRba_plalim(new Integer(rs.getString("RBA_PLALIM")));
						else
							mun_enc_dis_bean.setRba_plalim(0);
						if(rs.getString("PUNTOS_LUZ")!=null&&!rs.getString("PUNTOS_LUZ").equals(""))
							mun_enc_dis_bean.setPuntos_luz(new Integer(rs.getString("PUNTOS_LUZ")));
						else
							mun_enc_dis_bean.setPuntos_luz(0);
						if(rs.getString("ALU_V_SIN")!=null&&!rs.getString("ALU_V_SIN").equals(""))
							mun_enc_dis_bean.setAlu_v_sin(new Integer(rs.getString("ALU_V_SIN")));
						else
							mun_enc_dis_bean.setAlu_v_sin(0);
						if(rs.getString("ALU_L_SIN")!=null&&!rs.getString("ALU_L_SIN").equals(""))
							mun_enc_dis_bean.setAlu_l_sin(new Integer(rs.getString("ALU_L_SIN")));
						else
							mun_enc_dis_bean.setAlu_l_sin(0);


						lstType.add(mun_enc_dis_bean);
					}
				}else if (vista.getVista().equals("check_mpt_carretera")){
					while (rs.next()) {	
						V_carretera_bean carreteraBean = new V_carretera_bean();

						carreteraBean.setProvincia(rs.getString("provincia"));
						carreteraBean.setCod_carrt(rs.getString("cod_carrt"));
						if(rs.getString("denominaci")!=null)
							carreteraBean.setDenominaci(rs.getString("denominaci"));
						else
							carreteraBean.setDenominaci("-");
						
						lstType.add(carreteraBean);
						
					}
				}else if (vista.getVista().equals("check_mpt_tramo_carretera")){
					while (rs.next()) {	
						V_tramo_carretera_bean  tramo_carretera_bean = new V_tramo_carretera_bean();

						tramo_carretera_bean.setProvincia(rs.getString("PROVINCIA"));
						tramo_carretera_bean.setMunicipio(rs.getString("MUNICIPIO"));
						tramo_carretera_bean.setCod_carrt(rs.getString("COD_CARRT"));
						if(rs.getString("PK_INICIAL")!=null&&!rs.getString("PK_INICIAL").equals(""))		
							tramo_carretera_bean.setPk_inicial(Double.parseDouble(rs.getString("PK_INICIAL")) );
						else
							tramo_carretera_bean.setPk_inicial(Double.parseDouble(rs.getString("0.0")) );
						if(rs.getString("PK_FINAL")!=null&&!rs.getString("PK_FINAL").equals(""))		
							tramo_carretera_bean.setPk_final(Double.parseDouble(rs.getString("PK_FINAL") ));
						else
							tramo_carretera_bean.setPk_final(Double.parseDouble(rs.getString("0.0")));
						tramo_carretera_bean.setTitular(rs.getString("TITULAR"));
						tramo_carretera_bean.setGestion(rs.getString("GESTION"));
						tramo_carretera_bean.setSenaliza(rs.getString("SENALIZA"));
						tramo_carretera_bean.setFirme(rs.getString("FIRME"));
						tramo_carretera_bean.setEstado(rs.getString("ESTADO"));
						if(rs.getString("ANCHO")!=null&&!rs.getString("ANCHO").equals(""))		
							tramo_carretera_bean.setAncho(Double.parseDouble(rs.getString("ANCHO") ));
						else
							tramo_carretera_bean.setAncho(Double.parseDouble(rs.getString("0.0")) );
						if(rs.getString("LONGITUD")!=null && !rs.getString("LONGITUD").equals(""))	
							tramo_carretera_bean.setLongitud(LocalGISEIELUtils.redondear(Double.parseDouble(rs.getString("LONGITUD")) ,1));	
						else
							tramo_carretera_bean.setLongitud(Double.parseDouble("0.0") );	
						if(rs.getString("PASOS_NIVE")!=null&&!rs.getString("PASOS_NIVE").equals(""))		
							tramo_carretera_bean.setPasos_nive(new Integer(rs.getString("PASOS_NIVE")));
						else
							tramo_carretera_bean.setPasos_nive(0);
						tramo_carretera_bean.setDimensiona(rs.getString("DIMENSIONA"));
						tramo_carretera_bean.setMuy_sinuos(rs.getString("MUY_SINUOS"));
						tramo_carretera_bean.setPte_excesi(rs.getString("PTE_EXCESI"));
						tramo_carretera_bean.setFre_estrec(rs.getString("FRE_ESTREC"));


						lstType.add(tramo_carretera_bean);

					}
				}else if (vista.getVista().equals("check_mpt_centro_asistencial")){
					while (rs.next()) {	
						V_centro_asistencial_bean centro_asistencialBean = new V_centro_asistencial_bean();

						centro_asistencialBean.setClave(rs.getString("clave"));
						centro_asistencialBean.setProvincia(rs.getString("provincia"));
						centro_asistencialBean.setMunicipio(rs.getString("municipio"));
						centro_asistencialBean.setEntidad(rs.getString("entidad"));
						centro_asistencialBean.setPoblamient(rs.getString("poblamient"));
						centro_asistencialBean.setOrden_casi(rs.getString("orden_casi"));
						centro_asistencialBean.setNombre(rs.getString("nombre"));
						centro_asistencialBean.setTipo_casis(rs.getString("tipo_casis"));
						centro_asistencialBean.setTitular(rs.getString("titular"));
						centro_asistencialBean.setGestion(rs.getString("gestion"));
						if(rs.getString("plazas")!=null&&!rs.getString("plazas").equals(""))
							centro_asistencialBean.setPlazas(new Integer(rs.getString("plazas")));
						else
							centro_asistencialBean.setPlazas(0);
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							centro_asistencialBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							centro_asistencialBean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							centro_asistencialBean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							centro_asistencialBean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							centro_asistencialBean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							centro_asistencialBean.setS_sola(0);
						centro_asistencialBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						centro_asistencialBean.setEstado(rs.getString("estado"));

						lstType.add(centro_asistencialBean);

					}
				}else if (vista.getVista().equals("check_mpt_centro_sanitario")){
					while (rs.next()) {	
						V_centro_sanitario_bean centro_sanitarioBean = new V_centro_sanitario_bean();

						centro_sanitarioBean.setClave(rs.getString("clave"));
						centro_sanitarioBean.setProvincia(rs.getString("provincia"));
						centro_sanitarioBean.setMunicipio(rs.getString("municipio"));
						centro_sanitarioBean.setEntidad(rs.getString("entidad"));
						centro_sanitarioBean.setPoblamient(rs.getString("poblamient"));
						centro_sanitarioBean.setOrden_csan(rs.getString("orden_csan"));
						centro_sanitarioBean.setNombre(rs.getString("nombre"));
						centro_sanitarioBean.setTipo_csan(rs.getString("tipo_csan"));
						centro_sanitarioBean.setTitular(rs.getString("titular"));
						centro_sanitarioBean.setGestion(rs.getString("gestion"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							centro_sanitarioBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							centro_sanitarioBean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							centro_sanitarioBean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							centro_sanitarioBean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							centro_sanitarioBean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							centro_sanitarioBean.setS_sola(0);
						centro_sanitarioBean.setUci(rs.getString("uci"));
						if(rs.getString("camas")!=null&&!rs.getString("camas").equals(""))
							centro_sanitarioBean.setCamas(new Integer(rs.getString("camas")));
						else
							centro_sanitarioBean.setCamas(0);
						centro_sanitarioBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						centro_sanitarioBean.setEstado(rs.getString("estado"));

						lstType.add(centro_sanitarioBean);
					}
				}else if (vista.getVista().equals("check_mpt_matadero")){
					while (rs.next()) {	
						V_matadero_bean matadero_bean = new V_matadero_bean();


						matadero_bean.setClave(rs.getString("CLAVE"));
						matadero_bean.setProvincia(rs.getString("PROVINCIA"));
						matadero_bean.setMunicipio(rs.getString("MUNICIPIO"));
						matadero_bean.setEntidad(rs.getString("ENTIDAD"));
						matadero_bean.setPoblamient(rs.getString("POBLAMIENT"));
						matadero_bean.setOrden_mata(rs.getString("ORDEN_MATA"));
						matadero_bean.setNombre(rs.getString("NOMBRE"));
						matadero_bean.setClase_mat(rs.getString("CLASE_MAT"));
						matadero_bean.setTitular(rs.getString("TITULAR"));
						matadero_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							matadero_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							matadero_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							matadero_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							matadero_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							matadero_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							matadero_bean.setS_sola(0);
						matadero_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						matadero_bean.setEstado(rs.getString("ESTADO"));
						if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
							matadero_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
						else
							matadero_bean.setCapacidad(0);
						if(rs.getString("UTILIZACIO")!=null&&!rs.getString("UTILIZACIO").equals(""))
							matadero_bean.setUtilizacio(new Integer(rs.getString("UTILIZACIO")));
						else
							matadero_bean.setUtilizacio(0);
						matadero_bean.setTunel(rs.getString("TUNEL"));
						matadero_bean.setBovino(rs.getString("BOVINO"));
						matadero_bean.setOvino(rs.getString("OVINO"));
						matadero_bean.setPorcino(rs.getString("PORCINO"));
						matadero_bean.setOtros(rs.getString("OTROS"));

						lstType.add(matadero_bean);

					}
				}else if (vista.getVista().equals("check_mpt_cementerio")){
					while (rs.next()) {	
						V_cementerio_bean cementerioBean = new V_cementerio_bean();

						cementerioBean.setClave(rs.getString("clave"));
						cementerioBean.setProvincia(rs.getString("provincia"));
						cementerioBean.setMunicipio(rs.getString("municipio"));
						cementerioBean.setEntidad(rs.getString("entidad"));
						cementerioBean.setPoblamient(rs.getString("poblamient"));
						cementerioBean.setOrden_ceme(rs.getString("orden_ceme"));
						cementerioBean.setNombre(rs.getString("nombre"));
						cementerioBean.setTitular(rs.getString("titular"));
						if(rs.getString("distancia")!=null&&!rs.getString("distancia").equals(""))
							cementerioBean.setDistancia(Double.parseDouble(rs.getString("distancia")));//KM 
						else
							cementerioBean.setDistancia(Double.parseDouble(rs.getString("0.0") ));

						cementerioBean.setAcceso(rs.getString("acceso"));
						cementerioBean.setCapilla(rs.getString("capilla"));
						cementerioBean.setDeposito(rs.getString("deposito"));
						cementerioBean.setAmpliacion(rs.getString("ampliacion"));
						if(rs.getString("saturacion")!=null&&!rs.getString("saturacion").equals(""))
							cementerioBean.setSaturacion(new Integer((int) Math.rint(new Double(rs.getString("saturacion")))));//%
						else
							cementerioBean.setSaturacion(Integer.parseInt(rs.getString("0") ));
						if(rs.getString("superficie")!=null&&!rs.getString("superficie").equals(""))
							cementerioBean.setSuperficie(new Integer(rs.getString("superficie")));
						else
							cementerioBean.setSuperficie(0);
						cementerioBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						cementerioBean.setCrematorio(rs.getString("crematorio"));

						lstType.add(cementerioBean);
					}
				}else if (vista.getVista().equals("check_mpt_tanatorio")){
					while (rs.next()) {	
						V_tanatorio_bean  tanatorio_bean = new V_tanatorio_bean();

						tanatorio_bean.setClave(rs.getString("CLAVE"));
						tanatorio_bean.setProvincia(rs.getString("PROVINCIA"));
						tanatorio_bean.setMunicipio(rs.getString("MUNICIPIO"));
						tanatorio_bean.setEntidad(rs.getString("ENTIDAD"));
						tanatorio_bean.setPoblamient(rs.getString("POBLAMIENT"));
						tanatorio_bean.setOrden_tana(rs.getString("ORDEN_TANA"));
						tanatorio_bean.setNombre(rs.getString("NOMBRE"));
						tanatorio_bean.setTitular(rs.getString("TITULAR"));
						tanatorio_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							tanatorio_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							tanatorio_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							tanatorio_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							tanatorio_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							tanatorio_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							tanatorio_bean.setS_sola(0);
						if(rs.getString("SALAS")!=null&&!rs.getString("SALAS").equals(""))
							tanatorio_bean.setSalas(new Integer(rs.getString("SALAS")));
						else
							tanatorio_bean.setSalas(0);
						tanatorio_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						tanatorio_bean.setEstado(rs.getString("ESTADO"));
						
						lstType.add(tanatorio_bean);
					}
				}else if (vista.getVista().equals("check_mpt_colector")){
					while (rs.next()) {	
						V_colector_bean colectorBean = new V_colector_bean();

						colectorBean.setClave(rs.getString("clave"));
						colectorBean.setProvincia(rs.getString("provincia"));
						colectorBean.setMunicipio(rs.getString("municipio"));
						colectorBean.setOrden_cole(rs.getString("orden_cole"));			
							
						lstType.add(colectorBean);
					}
				}else if (vista.getVista().equals("check_mpt_colector_nucleo")){
					while (rs.next()) {	
						V_colector_nucleo_bean colector_nucleoBean = new V_colector_nucleo_bean();

						colector_nucleoBean.setProvincia(rs.getString("provincia"));
						colector_nucleoBean.setMunicipio(rs.getString("municipio"));
						colector_nucleoBean.setEntidad(rs.getString("entidad"));
						colector_nucleoBean.setNucleo(rs.getString("nucleo"));
						colector_nucleoBean.setClave(rs.getString("clave"));
						colector_nucleoBean.setC_provinci(rs.getString("c_provinc"));
						colector_nucleoBean.setC_municipi(rs.getString("c_municip"));
						colector_nucleoBean.setOrden_cole(rs.getString("orden_cole"));
					
						lstType.add(colector_nucleoBean);
					}
				}else if (vista.getVista().equals("check_mpt_conduccion")){				while (rs.next()) {	
					while (rs.next()) {	
						V_conduccion_bean conduccion_bean = new V_conduccion_bean();

						conduccion_bean.setClave(rs.getString("clave"));
						conduccion_bean.setProvincia(rs.getString("provincia"));
						conduccion_bean.setMunicipio(rs.getString("municipio"));
						conduccion_bean.setOrden_cond(rs.getString("orden_cond"));
						
						lstType.add(conduccion_bean);
					}					
				}
				}else if (vista.getVista().equals("check_mpt_conducccion_nucleo")){
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

						lstType.add(cond_agua_nucleo_bean);

					}
				}else if (vista.getVista().equals("check_mpt_depositos")){
					while (rs.next()) {	
						V_deposito_bean deposito_bean = new V_deposito_bean();
						
						deposito_bean.setProvincia(rs.getString("PROVINCIA"));
						deposito_bean.setMunicipio(rs.getString("MUNICIPIO"));
						deposito_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
						deposito_bean.setClave(rs.getString("CLAVE"));
						
						lstType.add(deposito_bean);
					}
				}else if (vista.getVista().equals("check_mpt_depositos_nucleo")){
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

						lstType.add(deposito_agua_nucleo_bean);

					}
				}else if (vista.getVista().equals("check_mpt_depuradora")){
					while (rs.next()) {	
						V_depuradora_bean depuradora_bean = new V_depuradora_bean();
						
						depuradora_bean.setProvincia(rs.getString("PROVINCIA"));
						depuradora_bean.setMunicipio(rs.getString("MUNICIPIO"));
						depuradora_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
						depuradora_bean.setClave(rs.getString("CLAVE"));
						
						lstType.add(depuradora_bean);
					}
				}else if (vista.getVista().equals("check_mpt_depuradora_nucleo")){
					while (rs.next()) {	
						V_dep_agua_nucleo_bean dep_agua_nucleo_bean = new V_dep_agua_nucleo_bean();
						dep_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
						dep_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
						dep_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
						dep_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));		
						dep_agua_nucleo_bean.setClave(rs.getString("CLAVE"));		
						dep_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));		
						dep_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));		
						dep_agua_nucleo_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));			

						lstType.add(dep_agua_nucleo_bean);
					
					}
				}else if (vista.getVista().equals("check_mpt_emisario")){
					while (rs.next()) {	
						V_emisario_bean emisario_bean = new V_emisario_bean();
						
						emisario_bean.setProvincia(rs.getString("PROVINCIA"));
						emisario_bean.setMunicipio(rs.getString("MUNICIPIO"));
						emisario_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
						emisario_bean.setClave(rs.getString("CLAVE"));
						
						lstType.add(emisario_bean);
					}
				}else if (vista.getVista().equals("check_mpt_emisaro_nucleo")){
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

						lstType.add(emisario_nucleo_bean);
					}
				}else if (vista.getVista().equals("check_mpt_proteccion")){
					while (rs.next()) {	
						V_proteccion_civil_bean  proteccion_civil_bean = new V_proteccion_civil_bean();

						proteccion_civil_bean.setClave(rs.getString("CLAVE"));
						proteccion_civil_bean.setProvincia(rs.getString("PROVINCIA"));
						proteccion_civil_bean.setMunicipio(rs.getString("MUNICIPIO"));
						proteccion_civil_bean.setEntidad(rs.getString("ENTIDAD"));
						proteccion_civil_bean.setPoblamient(rs.getString("POBLAMIENT"));
						proteccion_civil_bean.setOrden_prot(rs.getString("ORDEN_PROT"));
						proteccion_civil_bean.setNombre(rs.getString("NOMBRE"));
						proteccion_civil_bean.setTipo_pciv(rs.getString("TIPO_PCIV"));
						proteccion_civil_bean.setTitular(rs.getString("TITULAR"));
						proteccion_civil_bean.setAmbito(rs.getString("AMBITO"));
						proteccion_civil_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("PLAN_PROFE")!=null&&!rs.getString("PLAN_PROFE").equals(""))
							proteccion_civil_bean.setPlan_profe(new Integer(rs.getString("PLAN_PROFE")));
						else
							proteccion_civil_bean.setPlan_profe(0);
						if(rs.getString("PLAN_VOLUN")!=null&&!rs.getString("PLAN_VOLUN").equals(""))
							proteccion_civil_bean.setPlan_volun(new Integer(rs.getString("PLAN_VOLUN")));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							proteccion_civil_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							proteccion_civil_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							proteccion_civil_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							proteccion_civil_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							proteccion_civil_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							proteccion_civil_bean.setS_sola(0);
						proteccion_civil_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						proteccion_civil_bean.setEstado(rs.getString("ESTADO"));
						if(rs.getString("VEHIC_INCE")!=null&&!rs.getString("VEHIC_INCE").equals(""))
							proteccion_civil_bean.setVehic_ince(new Integer(rs.getString("VEHIC_INCE")));
						else
							proteccion_civil_bean.setVehic_ince(0);
						if(rs.getString("VEHIC_RESC")!=null&&!rs.getString("VEHIC_RESC").equals(""))
							proteccion_civil_bean.setVehic_resc(new Integer(rs.getString("VEHIC_RESC")));
						else
							proteccion_civil_bean.setVehic_resc(0);
						if(rs.getString("AMBULANCIA")!=null&&!rs.getString("AMBULANCIA").equals(""))
							proteccion_civil_bean.setAmbulancia(new Integer(rs.getString("AMBULANCIA")));
						else
							proteccion_civil_bean.setAmbulancia(0);
						if(rs.getString("MEDIOS_AER")!=null&&!rs.getString("MEDIOS_AER").equals(""))
							proteccion_civil_bean.setMedios_aer(new Integer(rs.getString("MEDIOS_AER")));
						else
							proteccion_civil_bean.setMedios_aer(0);
						if(rs.getString("OTROS_VEHI")!=null&&!rs.getString("OTROS_VEHI").equals(""))
							proteccion_civil_bean.setOtros_vehi(new Integer(rs.getString("OTROS_VEHI")));
						else
							proteccion_civil_bean.setOtros_vehi(0);
						if(rs.getString("QUITANIEVE")!=null&&!rs.getString("QUITANIEVE").equals(""))
							proteccion_civil_bean.setQuitanieve(new Integer(rs.getString("QUITANIEVE")));
						else
							proteccion_civil_bean.setQuitanieve(0);
						if(rs.getString("DETEC_INCE")!=null&&!rs.getString("DETEC_INCE").equals(""))
							proteccion_civil_bean.setDetec_ince(new Integer(rs.getString("DETEC_INCE")));
						else
							proteccion_civil_bean.setDetec_ince(0);
						if(rs.getString("OTROS")!=null&&!rs.getString("OTROS").equals(""))
							proteccion_civil_bean.setOtros(new Integer(rs.getString("OTROS")));
						else
							proteccion_civil_bean.setOtros(0);

						lstType.add(proteccion_civil_bean);
					}
				}else if (vista.getVista().equals("check_mpt_centro_ensenaza")){
					while (rs.next()) {	
						V_centro_ensenanza_bean centro_ensenanzaBean = new V_centro_ensenanza_bean();

						centro_ensenanzaBean.setClave(rs.getString("clave"));
						centro_ensenanzaBean.setProvincia(rs.getString("provincia"));
						centro_ensenanzaBean.setMunicipio(rs.getString("municipio"));
						centro_ensenanzaBean.setEntidad(rs.getString("entidad"));
						centro_ensenanzaBean.setPoblamient(rs.getString("poblamient"));
						centro_ensenanzaBean.setOrden_cent(rs.getString("orden_cent"));
						centro_ensenanzaBean.setNombre(rs.getString("nombre"));
						centro_ensenanzaBean.setAmbito(rs.getString("ambito"));
						centro_ensenanzaBean.setTitular(rs.getString("titular"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							centro_ensenanzaBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							centro_ensenanzaBean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							centro_ensenanzaBean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							centro_ensenanzaBean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							centro_ensenanzaBean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							centro_ensenanzaBean.setS_sola(0);
						centro_ensenanzaBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						centro_ensenanzaBean.setEstado(rs.getString("estado"));

						lstType.add(centro_ensenanzaBean);
					}
				}else if (vista.getVista().equals("check_mpt_nivel_ensenanza")){
					while (rs.next()) {	
						V_nivel_ensenanza_bean  nivel_ensenanza_bean = new V_nivel_ensenanza_bean();


						nivel_ensenanza_bean.setClave(rs.getString("CLAVE"));
						nivel_ensenanza_bean.setProvincia(rs.getString("PROVINCIA"));
						nivel_ensenanza_bean.setMunicipio(rs.getString("MUNICIPIO"));
						nivel_ensenanza_bean.setEntidad(rs.getString("ENTIDAD"));
						nivel_ensenanza_bean.setPoblamient(rs.getString("POBLAMIENT"));
						nivel_ensenanza_bean.setOrden_cent(rs.getString("ORDEN_CENT"));
						nivel_ensenanza_bean.setNivel(rs.getString("NIVEL"));
						if(rs.getString("UNIDADES")!=null&&!rs.getString("UNIDADES").equals(""))
							nivel_ensenanza_bean.setUnidades(new Integer(rs.getString("UNIDADES")));
						else
							nivel_ensenanza_bean.setUnidades(0);
						if(rs.getString("PLAZAS")!=null&&!rs.getString("PLAZAS").equals(""))
							nivel_ensenanza_bean.setPlazas(new Integer(rs.getString("PLAZAS")));
						else
							nivel_ensenanza_bean.setPlazas(0);
						if(rs.getString("ALUMNOS")!=null&&!rs.getString("ALUMNOS").equals(""))
							nivel_ensenanza_bean.setAlumnos(new Integer(rs.getString("ALUMNOS")));
						else
							nivel_ensenanza_bean.setAlumnos(0);

						lstType.add(nivel_ensenanza_bean);
					}
				}else if (vista.getVista().equals("check_mpt_edific")){
					while (rs.next()) {	
						V_edific_pub_sin_uso_bean edific_pub_sin_uso_bean = new V_edific_pub_sin_uso_bean();

						edific_pub_sin_uso_bean.setClave(rs.getString("CLAVE"));
						edific_pub_sin_uso_bean.setProvincia(rs.getString("PROVINCIA"));
						edific_pub_sin_uso_bean.setMunicipio(rs.getString("MUNICIPIO"));
						edific_pub_sin_uso_bean.setEntidad(rs.getString("ENTIDAD"));
						edific_pub_sin_uso_bean.setPoblamient(rs.getString("POBLAMIENT"));
						edific_pub_sin_uso_bean.setOrden_edif(rs.getString("ORDEN_EDIF"));
						edific_pub_sin_uso_bean.setNombre(rs.getString("NOMBRE"));
						edific_pub_sin_uso_bean.setTitular(rs.getString("TITULAR"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							edific_pub_sin_uso_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							edific_pub_sin_uso_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							edific_pub_sin_uso_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							edific_pub_sin_uso_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							edific_pub_sin_uso_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							edific_pub_sin_uso_bean.setS_sola(0);
						edific_pub_sin_uso_bean.setEstado(rs.getString("ESTADO"));
						edific_pub_sin_uso_bean.setUsoant(rs.getString("USOANT"));
						
						lstType.add(edific_pub_sin_uso_bean);

					}
				}else if (vista.getVista().equals("check_mpt_casa_consistorial")){
					while (rs.next()) {	
						V_casa_consitorial_bean casa_consitorialBean = new V_casa_consitorial_bean();

						casa_consitorialBean.setClave(rs.getString("clave"));
						casa_consitorialBean.setProvincia(rs.getString("provincia"));
						casa_consitorialBean.setMunicipio(rs.getString("municipio"));
						casa_consitorialBean.setEntidad(rs.getString("entidad"));
						casa_consitorialBean.setPoblamient(rs.getString("poblamient"));
						casa_consitorialBean.setOrden_casa(rs.getString("orden_casa"));
						casa_consitorialBean.setNombre(rs.getString("nombre"));
						casa_consitorialBean.setTipo(rs.getString("tipo"));
						casa_consitorialBean.setTitular(rs.getString("titular"));
						casa_consitorialBean.setTenencia(rs.getString("tenencia"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							casa_consitorialBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							casa_consitorialBean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							casa_consitorialBean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							casa_consitorialBean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							casa_consitorialBean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							casa_consitorialBean.setS_sola(0);

						casa_consitorialBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						casa_consitorialBean.setEstado(rs.getString("estado"));


						lstType.add(casa_consitorialBean);
						
					}
				}else if (vista.getVista().equals("check_mpt_casa_uso")){
					while (rs.next()) {	
						V_casa_con_uso_bean casa_con_usoBean = new V_casa_con_uso_bean();

						casa_con_usoBean.setClave(rs.getString("clave"));
						casa_con_usoBean.setProvincia(rs.getString("provincia"));
						casa_con_usoBean.setMunicipio(rs.getString("municipio"));
						casa_con_usoBean.setEntidad(rs.getString("entidad"));
						casa_con_usoBean.setPoblamient(rs.getString("poblamient"));
						casa_con_usoBean.setOrden_casa(rs.getString("orden_casa"));
						casa_con_usoBean.setUso(rs.getString("uso"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							casa_con_usoBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							casa_con_usoBean.setS_cubi(0);
						lstType.add(casa_con_usoBean);
					}
				}else if (vista.getVista().equals("check_mpt_cent_cultural")){
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
						cent_culturalBean.setGestion(rs.getString("GESTION"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							cent_culturalBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							cent_culturalBean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							cent_culturalBean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							cent_culturalBean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							cent_culturalBean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							cent_culturalBean.setS_sola(0);
						cent_culturalBean.setAcceso_s_ruedas(rs.getString("acceso_s_ruedas"));
						cent_culturalBean.setEstado(rs.getString("estado"));

						lstType.add(cent_culturalBean);
					}
				}else if (vista.getVista().equals("check_mpt_cent_cultural_usos")){
					while (rs.next()) {	
						V_cent_cultural_usos_bean cent_cultural_usosBean = new V_cent_cultural_usos_bean();

						cent_cultural_usosBean.setClave(rs.getString("clave"));
						cent_cultural_usosBean.setProvincia(rs.getString("provincia"));
						cent_cultural_usosBean.setMunicipio(rs.getString("municipio"));
						cent_cultural_usosBean.setEntidad(rs.getString("entidad"));
						cent_cultural_usosBean.setPoblamient(rs.getString("poblamient"));
						cent_cultural_usosBean.setOrden_cent(rs.getString("orden_cent"));
						cent_cultural_usosBean.setUso(rs.getString("uso"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							cent_cultural_usosBean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							cent_cultural_usosBean.setS_cubi(0);

						lstType.add(cent_cultural_usosBean);

					}
				}else if (vista.getVista().equals("check_mpt_instal_deportiva")){
					while (rs.next()) {	
						V_instal_deportiva_bean instal_deportiva_bean = new V_instal_deportiva_bean();


						instal_deportiva_bean.setClave(rs.getString("CLAVE"));
						instal_deportiva_bean.setProvincia(rs.getString("PROVINCIA"));
						instal_deportiva_bean.setMunicipio(rs.getString("MUNICIPIO"));
						instal_deportiva_bean.setEntidad(rs.getString("ENTIDAD"));
						instal_deportiva_bean.setPoblamient(rs.getString("POBLAMIENT"));
						instal_deportiva_bean.setOrden_inst(rs.getString("ORDEN_INST"));
						instal_deportiva_bean.setNombre(rs.getString("NOMBRE"));
						instal_deportiva_bean.setTipo_insde(rs.getString("TIPO_INSDE"));
						instal_deportiva_bean.setTitular(rs.getString("TITULAR"));
						instal_deportiva_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							instal_deportiva_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							instal_deportiva_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							instal_deportiva_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							instal_deportiva_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							instal_deportiva_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							instal_deportiva_bean.setS_sola(0);
						instal_deportiva_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						instal_deportiva_bean.setEstado(rs.getString("ESTADO"));

						lstType.add(instal_deportiva_bean);

					}
				}else if (vista.getVista().equals("check_mpt_inst_depor_deporte")){
					while (rs.next()) {	
						V_inst_depor_deporte_bean inst_depor_deporte_bean = new V_inst_depor_deporte_bean();

						inst_depor_deporte_bean.setClave(rs.getString("CLAVE"));
						inst_depor_deporte_bean.setProvincia(rs.getString("PROVINCIA"));
						inst_depor_deporte_bean.setMunicipio(rs.getString("MUNICIPIO"));
						inst_depor_deporte_bean.setEntidad(rs.getString("ENTIDAD"));
						inst_depor_deporte_bean.setPoblamient(rs.getString("POBLAMIENT"));
						inst_depor_deporte_bean.setOrden_inst(rs.getString("ORDEN_INST"));
						inst_depor_deporte_bean.setTipo_depor(rs.getString("TIPO_DEPOR"));

						lstType.add(inst_depor_deporte_bean);
					}
				}else if (vista.getVista().equals("check_mpt_parque")){
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
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							parque_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							parque_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							parque_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							parque_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							parque_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							parque_bean.setS_sola(0);
						parque_bean.setAgua(rs.getString("AGUA"));
						parque_bean.setSaneamient(rs.getString("SANEAMIENT"));
						parque_bean.setElectricid(rs.getString("ELECTRICID"));
						parque_bean.setComedor(rs.getString("COMEDOR"));
						parque_bean.setJuegos_inf(rs.getString("JUEGOS_INF"));
						parque_bean.setOtras(rs.getString("OTRAS"));
						parque_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						parque_bean.setEstado(rs.getString("ESTADO"));

						lstType.add(parque_bean);
					}
				}else if (vista.getVista().equals("check_mpt_lonja_merc_feria")){
					while (rs.next()) {	
						V_lonja_merc_feria_bean lonja_merc_feria_bean = new V_lonja_merc_feria_bean();


						lonja_merc_feria_bean.setClave(rs.getString("CLAVE"));
						lonja_merc_feria_bean.setProvincia(rs.getString("PROVINCIA"));
						lonja_merc_feria_bean.setMunicipio(rs.getString("MUNICIPIO"));
						lonja_merc_feria_bean.setEntidad(rs.getString("ENTIDAD"));
						lonja_merc_feria_bean.setPoblamient(rs.getString("POBLAMIENT"));
						lonja_merc_feria_bean.setOrden_lmf(rs.getString("ORDEN_LMF"));
						lonja_merc_feria_bean.setNombre(rs.getString("NOMBRE"));
						lonja_merc_feria_bean.setTipo_lonj(rs.getString("TIPO_LONJ"));
						lonja_merc_feria_bean.setTitular(rs.getString("TITULAR"));
						lonja_merc_feria_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("s_cubi")!=null&&!rs.getString("s_cubi").equals(""))
							lonja_merc_feria_bean.setS_cubi(new Integer(rs.getString("s_cubi")));
						else
							lonja_merc_feria_bean.setS_cubi(0);
						if(rs.getString("s_aire")!=null&&!rs.getString("s_aire").equals(""))
							lonja_merc_feria_bean.setS_aire(new Integer(rs.getString("s_aire")));
						else
							lonja_merc_feria_bean.setS_aire(0);
						if(rs.getString("s_sola")!=null&&!rs.getString("s_sola").equals(""))
							lonja_merc_feria_bean.setS_sola(new Integer(rs.getString("s_sola")));
						else
							lonja_merc_feria_bean.setS_sola(0);
						lonja_merc_feria_bean.setAcceso_s_ruedas(rs.getString("ACCESO_S_RUEDAS"));
						lonja_merc_feria_bean.setEstado(rs.getString("ESTADO"));

						lstType.add(lonja_merc_feria_bean);
					}
				}else if (vista.getVista().equals("check_mpt_plan_urbanistico")){
					while (rs.next()) {	
						V_plan_urbanistico_bean plan_urbanistico_bean = new V_plan_urbanistico_bean();

						plan_urbanistico_bean.setProvincia(rs.getString("PROVINCIA"));
						plan_urbanistico_bean.setMunicipio(rs.getString("MUNICIPIO"));
						plan_urbanistico_bean.setTipo_urba(rs.getString("TIPO_URBA"));
						plan_urbanistico_bean.setEstado_tra(rs.getString("ESTADO_TRA"));
						if(rs.getString("DENOMINACI")!=null)
							plan_urbanistico_bean.setDenominaci(rs.getString("DENOMINACI"));
						else
							plan_urbanistico_bean.setDenominaci("-");
						if(rs.getString("SUPERFICIE")!=null&&!rs.getString("SUPERFICIE").equals(""))
							plan_urbanistico_bean.setSuperficie(new Double(rs.getString("SUPERFICIE")));
						else
							plan_urbanistico_bean.setSuperficie(0.0);
						if(rs.getString("BO")!=null&&!rs.getString("BO").equals(""))
							plan_urbanistico_bean.setBo(LocalGISEIELUtils.formatFecha(rs.getDate("BO")));
						else
							plan_urbanistico_bean.setBo(null);
						if(rs.getString("URBAN")!=null&&!rs.getString("URBAN").equals(""))
							plan_urbanistico_bean.setUrban(Double.parseDouble(rs.getString("URBAN")));
						else
							plan_urbanistico_bean.setUrban(0);
						if(rs.getString("NO_URBABLE")!=null&&!rs.getString("NO_URBABLE").equals(""))
							plan_urbanistico_bean.setNo_urbable(Double.parseDouble(rs.getString("NO_URBABLE")));
						else
							plan_urbanistico_bean.setNo_urbable(0);
						if(rs.getString("NOURBABLE_")!=null&&!rs.getString("NOURBABLE_").equals(""))
							plan_urbanistico_bean.setNourbable_(Double.parseDouble(rs.getString("NOURBABLE_")));
						else
							plan_urbanistico_bean.setNourbable_(0);
						lstType.add(plan_urbanistico_bean);
					}
				}else if (vista.getVista().equals("check_mpt_ot_serv_municipal")){
					while (rs.next()) {	
						V_ot_serv_municipal_bean  ot_serv_municipal_bean = new V_ot_serv_municipal_bean();

						ot_serv_municipal_bean.setProvincia(rs.getString("PROVINCIA"));
						ot_serv_municipal_bean.setMunicipio(rs.getString("MUNICIPIO"));
						ot_serv_municipal_bean.setSw_inf_grl(rs.getString("SW_INF_GRL"));
						ot_serv_municipal_bean.setSw_inf_tur(rs.getString("SW_INF_TUR"));	
						ot_serv_municipal_bean.setSw_gb_elec(rs.getString("SW_GB_ELEC"));	
						ot_serv_municipal_bean.setOrd_soterr(rs.getString("ORD_SOTERR"));	
						ot_serv_municipal_bean.setEn_eolica(rs.getString("EN_EOLICA"));	
						if(rs.getString("KW_EOLICA")!=null&&!rs.getString("KW_EOLICA").equals(""))
							ot_serv_municipal_bean.setKw_eolica(new Integer(rs.getString("KW_EOLICA")));
						ot_serv_municipal_bean.setEn_solar(rs.getString("EN_SOLAR"));	
						if(rs.getString("KW_SOLAR")!=null&&!rs.getString("KW_SOLAR").equals(""))
							ot_serv_municipal_bean.setKw_solar(new Integer(rs.getString("KW_SOLAR")));
						else
							ot_serv_municipal_bean.setKw_solar(0);
						ot_serv_municipal_bean.setPl_mareo(rs.getString("PL_MAREO"));	
						if(rs.getString("KW_MAREO")!=null&&!rs.getString("KW_MAREO").equals(""))
							ot_serv_municipal_bean.setKw_mareo(new Integer(rs.getString("KW_MAREO")));
						else
							ot_serv_municipal_bean.setKw_mareo(0);
						ot_serv_municipal_bean.setOt_energ(rs.getString("OT_ENERG"));	
						if(rs.getString("KW_ENERG")!=null&&!rs.getString("KW_ENERG").equals(""))
							ot_serv_municipal_bean.setKw_energ(new Integer(rs.getString("KW_ENERG")));
						else
							ot_serv_municipal_bean.setKw_energ(0);
						ot_serv_municipal_bean.setCob_serv_telf_m(rs.getString("COB_SERV_TELF_M"));	
						ot_serv_municipal_bean.setTv_dig_calbe(rs.getString("TV_DIG_CABLE"));	

						lstType.add(ot_serv_municipal_bean);
					}
				}else if (vista.getVista().equals("check_mpt_ramal_sanemaiento")){
					while (rs.next()) {	
						V_ramal_saneamiento_bean  ramal_saneamiento_bean = new V_ramal_saneamiento_bean();

						ramal_saneamiento_bean.setProvincia(rs.getString("PROVINCIA"));
						ramal_saneamiento_bean.setMunicipio(rs.getString("MUNICIPIO"));
						ramal_saneamiento_bean.setEntidad(rs.getString("ENTIDAD"));
						ramal_saneamiento_bean.setNucleo(rs.getString("NUCLEO"));
						ramal_saneamiento_bean.setTipo_rama(rs.getString("TIPO_RAMA"));
						ramal_saneamiento_bean.setSist_trans(rs.getString("SIST_TRANS"));
						ramal_saneamiento_bean.setEstado(rs.getString("ESTADO"));
						ramal_saneamiento_bean.setTipo_red(rs.getString("TIPO_RED"));
						ramal_saneamiento_bean.setTitular(rs.getString("TITULAR"));
						ramal_saneamiento_bean.setGestion(rs.getString("GESTION"));
						if(rs.getString("LONGIT_RAM")!=null&&!rs.getString("LONGIT_RAM").equals(""))
							ramal_saneamiento_bean.setLongit_ram(new Double(Math.rint(new Double(rs.getString("LONGIT_RAM")))).intValue());
						else
							ramal_saneamiento_bean.setLongit_ram(0);
						
						lstType.add(ramal_saneamiento_bean);
					}
				}else if (vista.getVista().equals("check_mpt_sanea_autonomo")){
					while (rs.next()) {	
						V_sanea_autonomo_bean  sanea_autonomo_bean = new V_sanea_autonomo_bean();

						sanea_autonomo_bean.setProvincia(rs.getString("PROVINCIA"));
						sanea_autonomo_bean.setMunicipio(rs.getString("MUNICIPIO"));
						sanea_autonomo_bean.setEntidad(rs.getString("ENTIDAD"));
						sanea_autonomo_bean.setNucleo(rs.getString("NUCLEO"));
						sanea_autonomo_bean.setTipo_sanea(rs.getString("TIPO_SANEA"));
						sanea_autonomo_bean.setEstado(rs.getString("ESTADO"));
						sanea_autonomo_bean.setAdecuacion(rs.getString("ADECUACION"));
						if(rs.getString("SAU_VIVIEN")!=null&&!rs.getString("SAU_VIVIEN").equals(""))
							sanea_autonomo_bean.setSau_vivien(new Integer(rs.getString("SAU_VIVIEN")));
						else
							sanea_autonomo_bean.setSau_vivien(0);
						if(rs.getString("SAU_POB_RE")!=null&&!rs.getString("SAU_POB_RE").equals(""))
							sanea_autonomo_bean.setSau_pob_re(new Integer(rs.getString("SAU_POB_RE")));
						else
							sanea_autonomo_bean.setSau_pob_re(0);
						if(rs.getString("SAU_POB_ES")!=null&&!rs.getString("SAU_POB_ES").equals(""))
							sanea_autonomo_bean.setSau_pob_es(new Integer(rs.getString("SAU_POB_ES")));
						else
							sanea_autonomo_bean.setSau_pob_es(0);
						if(rs.getString("SAU_VI_DEF")!=null&&!rs.getString("SAU_VI_DEF").equals(""))
							sanea_autonomo_bean.setSau_vi_def(new Integer(rs.getString("SAU_VI_DEF")));
						else
							sanea_autonomo_bean.setSau_vi_def(0);
						if(rs.getString("SAU_RE_DEF")!=null&&!rs.getString("SAU_RE_DEF").equals(""))
							sanea_autonomo_bean.setSau_re_def(new Integer(rs.getString("SAU_RE_DEF")));
						else
							sanea_autonomo_bean.setSau_re_def(0);
						if(rs.getString("SAU_ES_DEF")!=null&&!rs.getString("SAU_ES_DEF").equals(""))
							sanea_autonomo_bean.setSau_es_def(new Integer(rs.getString("SAU_ES_DEF")));
						else
							sanea_autonomo_bean.setSau_es_def(0);
						
						lstType.add(sanea_autonomo_bean);
					}
				}else if (vista.getVista().equals("check_mpt_trat_potabilizacion")){
					while (rs.next()) {	
						V_tra_potabilizacion_bean tra_potabilizacion_bean = new V_tra_potabilizacion_bean();

						tra_potabilizacion_bean.setProvincia(rs.getString("PROVINCIA"));
						tra_potabilizacion_bean.setClave(rs.getString("CLAVE"));
						tra_potabilizacion_bean.setMunicipio(rs.getString("MUNICIPIO"));
						tra_potabilizacion_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));

						lstType.add(tra_potabilizacion_bean);

					}
				}else if (vista.getVista().equals("check_mpt_trat_pota_nucleo")){
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

						lstType.add(trat_pota_nucleo_bean);
					}
				}else if (vista.getVista().equals("check_mpt_vertedero")){
					while (rs.next()) {	
						V_vertedero_bean vertedero_bean = new V_vertedero_bean();

						vertedero_bean.setClave(rs.getString("CLAVE"));
						vertedero_bean.setProvincia(rs.getString("PROVINCIA"));
						vertedero_bean.setMunicipio(rs.getString("MUNICIPIO"));
						vertedero_bean.setOrden_ver(rs.getString("ORDEN_VER"));

						lstType.add(vertedero_bean);
					}
				}else if (vista.getVista().equals("check_mpt_vertedero_nucleo")){
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

						lstType.add(vertedero_nucleo_bean);
					}
				}else if (vista.getVista().equals("check_mpt_municipio")){
					while (rs.next()) {	
						V_municipio_bean municipio_bean = new V_municipio_bean();
						
						municipio_bean.setProvincia(rs.getString("PROVINCIA"));
						municipio_bean.setMunicipio(rs.getString("MUNICIPIO"));
						municipio_bean.setIsla("");
						if(rs.getString("DENOMINACI")!=null)
							municipio_bean.setDenominaci(rs.getString("DENOMINACI"));
						else
							municipio_bean.setDenominaci("-");
						lstType.add(municipio_bean);
					}
				}else if (vista.getVista().equals("check_mpt_cabildo_consejo")){
					while (rs.next()) {	
						V_cabildo_consejo_bean cabildo_consejoBean = new V_cabildo_consejo_bean();

						cabildo_consejoBean.setProvincia(rs.getString("provincia"));
						cabildo_consejoBean.setIsla("");//rs.getString("isla"));
						if(rs.getString("denominaci")!=null)
							cabildo_consejoBean.setDenominaci(rs.getString("denominaci"));
						else
							cabildo_consejoBean.setDenominaci("-");

						lstType.add(cabildo_consejoBean);

					}
				}else if (vista.getVista().equals("check_mpt_entidad_sing")){
					while (rs.next()) {	
						V_entidad_singular_bean entidad_singular_bean = new V_entidad_singular_bean();
						
						entidad_singular_bean.setProvincia(rs.getString("PROVINCIA"));
						entidad_singular_bean.setMunicipio(rs.getString("MUNICIPIO"));
						entidad_singular_bean.setEntidad(rs.getString("ENTIDAD"));
						if(rs.getString("DENOMINACI")!=null)
							entidad_singular_bean.setDenominaci(rs.getString("DENOMINACI"));
						else
							entidad_singular_bean.setDenominaci("-");
						
						lstType.add(entidad_singular_bean);
					}
				}else if (vista.getVista().equals("check_mpt_poblamiento")){
					while (rs.next()) {	
						V_poblamiento_bean poblamiento_bean = new V_poblamiento_bean();

						poblamiento_bean.setProvincia(rs.getString("PROVINCIA"));
						poblamiento_bean.setMunicipio(rs.getString("MUNICIPIO"));
						poblamiento_bean.setEntidad(rs.getString("ENTIDAD"));
						poblamiento_bean.setPoblamient(rs.getString("POBLAMIENTO"));
						
						lstType.add(poblamiento_bean);
					}
				}else if (vista.getVista().equals("check_mpt_nucleo_poblacion")){
					while (rs.next()) {	
						V_nucleo_poblacion_bean  nucleo_poblacion_bean = new V_nucleo_poblacion_bean();

						nucleo_poblacion_bean.setProvincia(rs.getString("PROVINCIA"));
						nucleo_poblacion_bean.setMunicipio(rs.getString("MUNICIPIO"));
						nucleo_poblacion_bean.setEntidad(rs.getString("ENTIDAD"));
						nucleo_poblacion_bean.setPoblamient(rs.getString("POBLAMIENT"));
						if(rs.getString("DENOMINACI")!=null)
							nucleo_poblacion_bean.setDenominaci(rs.getString("DENOMINACI"));
						else
							nucleo_poblacion_bean.setDenominaci("-");
					
						lstType.add(nucleo_poblacion_bean);

					}
				}else if (vista.getVista().equals("check_mpt_nucleo_abandonado")){
					while (rs.next()) {	
						V_nuc_abandonado_bean  nuc_abandonado_bean = new V_nuc_abandonado_bean();

						nuc_abandonado_bean.setProvincia(rs.getString("PROVINCIA"));
						nuc_abandonado_bean.setMunicipio(rs.getString("MUNICIPIO"));
						nuc_abandonado_bean.setEntidad(rs.getString("ENTIDAD"));
						nuc_abandonado_bean.setPoblamiento(rs.getString("POBLAMIENT"));
						nuc_abandonado_bean.setA_abandono(rs.getString("A_ABANDONO"));
						nuc_abandonado_bean.setCausa_aban(rs.getString("CAUSA_ABAN"));
						nuc_abandonado_bean.setTitular_ab(rs.getString("TITULAR_AB"));
						nuc_abandonado_bean.setRehabilita(rs.getString("REHABILITA"));
						nuc_abandonado_bean.setAcceso_nuc(rs.getString("ACCESO_NUC"));
						nuc_abandonado_bean.setServ_agua(rs.getString("SERV_AGUA"));
						nuc_abandonado_bean.setServ_elect(rs.getString("SERV_ELECT"));

						lstType.add(nuc_abandonado_bean);
					}
				}else if (vista.getVista().equals("check_mpt_nucleo_encuestado")){
					while (rs.next()) {	
						V_nucl_encuestado_1_bean  nucl_encuestado_1_bean = new V_nucl_encuestado_1_bean();

						nucl_encuestado_1_bean.setProvincia(rs.getString("PROVINCIA"));
						nucl_encuestado_1_bean.setMunicipio(rs.getString("MUNICIPIO"));
						nucl_encuestado_1_bean.setEntidad(rs.getString("ENTIDAD"));
						nucl_encuestado_1_bean.setNucleo(rs.getString("NUCLEO"));
						if(rs.getString("PADRON")!=null&&!rs.getString("PADRON").equals(""))
							nucl_encuestado_1_bean.setPadron(new Integer(rs.getString("PADRON")));
						else
							nucl_encuestado_1_bean.setPadron(0);
						if(rs.getString("POB_ESTACI")!=null&&!rs.getString("POB_ESTACI").equals(""))
							nucl_encuestado_1_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
						else
							nucl_encuestado_1_bean.setPob_estaci(0);
						if(rs.getString("ALTITUD")!=null&&!rs.getString("ALTITUD").equals(""))
							nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
						else
							nucl_encuestado_1_bean.setAltitud(0);
						if(rs.getString("VIV_TOTAL")!=null&&!rs.getString("VIV_TOTAL").equals(""))
							nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
						else
							nucl_encuestado_1_bean.setViv_total(0);
						if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))
							nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
						else
							nucl_encuestado_1_bean.setHoteles(0);
						if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))
							nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
						else
							nucl_encuestado_1_bean.setCasas_rural(0);
						nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));


						lstType.add(nucl_encuestado_1_bean);

					}
				}else if (vista.getVista().equals("check_mpt_nucleo_encuestado_2")){

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
						if(rs.getString("AAG_HIDRAN")!=null&&!rs.getString("AAG_HIDRAN").equals("NO"))
							nucl_encuestado_2_bean.setAag_est_hi(rs.getString("AAG_EST_HI"));
						else
							nucl_encuestado_2_bean.setAag_est_hi("");		
						nucl_encuestado_2_bean.setAag_valvul(rs.getString("AAG_VALVUL"));
						if(rs.getString("AAG_VALVUL")!=null&&!rs.getString("AAG_VALVUL").equals("NO"))
							nucl_encuestado_2_bean.setAag_est_va(rs.getString("AAG_EST_VA"));
						else
							nucl_encuestado_2_bean.setAag_est_va("");
						nucl_encuestado_2_bean.setAag_bocasr(rs.getString("AAG_BOCASR"));
						if(rs.getString("AAG_BOCASR")!=null&&!rs.getString("AAG_BOCASR").equals("NO"))
							nucl_encuestado_2_bean.setAag_est_bo(rs.getString("AAG_EST_BO"));
						else
							nucl_encuestado_2_bean.setAag_est_bo("");

						nucl_encuestado_2_bean.setCisterna(rs.getString("CISTERNA"));

						lstType.add(nucl_encuestado_2_bean);
					}
				}				
				if(lstType.size()>0){
					if(datosMPTErroneos.get(vista.getDescripcion())!=null){
						ArrayList lstTypeAux=datosMPTErroneos.get(vista.getDescripcion());
						lstTypeAux.addAll(lstType);
						datosMPTErroneos.put(vista.getDescripcion(),lstTypeAux);
					}else
						datosMPTErroneos.put(vista.getDescripcion(),lstType);
				}
			}//fin del for
			
			
				
			int errores=0;	
			for (Enumeration<String> iterator = datosMPTErroneos.keys(); iterator.hasMoreElements();) {
				String keyDatosErroneosByType = iterator.nextElement();
				ArrayList datosErroneosByType=datosMPTErroneos.get(keyDatosErroneosByType);
				str.append(keyDatosErroneosByType+"\n");
				errores+=datosErroneosByType.size();
				for (int i = 0; i < datosErroneosByType.size(); i++) {
					Object element=datosErroneosByType.get(i);

					ArrayList valores=null;
					OrderToMPT ordenacion=new OrderToMPT();
					valores=ordenacion.getOrder(element);							
					String rowToInsertInFile=LocalGISEIELUtils.getRowToMPT(fase, "|", valores);//la fase se recogerá como parametro.
					str.append(rowToInsertInFile+"\n");
				}
				str.append("\n\n");
            }
			if(errores==0)
				str.append("Sin errores de Integridad Referencial");
			else
				str.append("Total de errores : "+errores);
			

        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +IntegridadReferencial.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
