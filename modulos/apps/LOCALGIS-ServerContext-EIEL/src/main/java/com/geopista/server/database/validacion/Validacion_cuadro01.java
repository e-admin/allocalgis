/**
 * Validacion_cuadro01.java
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
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;

public class Validacion_cuadro01 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro01.class);

	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean error = false;
		try
        {
			str.append(Messages.getString("cuadro01") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_NUCL_ENCUESTADO_1";
			String sql_distinct = "select distinct MUNICIPIO, PROVINCIA from v_NUCL_ENCUESTADO_1";
			String sql_1 = "SELECT * FROM v_MUN_ENC_DIS";
			
			
			ArrayList lstNuclEncuestado = new ArrayList();
			ArrayList lstNuclEncuestado1_distinct = new ArrayList ();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_1_bean  nucl_encuestado_1_bean = new V_nucl_encuestado_1_bean();

				nucl_encuestado_1_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_1_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_1_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_1_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_1_bean.setPadron(new Integer(rs.getString("PADRON")));
				nucl_encuestado_1_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
				
				if(rs.getString("ALTITUD")!=null&&!rs.getString("ALTITUD").equals(""))
					nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				else
					nucl_encuestado_1_bean.setAltitud(0);
					
				//NULL.nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				
				
				if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))
					nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				else
					nucl_encuestado_1_bean.setHoteles(0);
					
				//NULLnucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				
				if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))
					nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				else
					nucl_encuestado_1_bean.setCasas_rural(0);
				
				//NULLnucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));

				
				lstNuclEncuestado.add(nucl_encuestado_1_bean);
	
			}
			
			ps = connection.prepareStatement(sql_distinct);
			rs = ps.executeQuery();
			while (rs.next()) {		
				V_nucl_encuestado_1_bean  nucl_encuestado_1_bean = new V_nucl_encuestado_1_bean();

				nucl_encuestado_1_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_1_bean.setProvincia(rs.getString("PROVINCIA"));
				
				lstNuclEncuestado1_distinct.add(nucl_encuestado_1_bean);
				
				
			}
			
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			
			ArrayList lstNumEncDis = new ArrayList();
			while (rs.next()) {		
				V_mun_enc_dis_bean munEncDis = new V_mun_enc_dis_bean();
				V_mun_enc_dis_bean mun_enc_dis_bean = new V_mun_enc_dis_bean();

				mun_enc_dis_bean.setCodprov(rs.getString("PROVINCIA"));
				mun_enc_dis_bean.setCodmunic(rs.getString("MUNICIPIO"));
				mun_enc_dis_bean.setPadron(new Integer(rs.getString("PADRON")));
				mun_enc_dis_bean.setPob_estaci(new Integer(rs.getString("POB_ESTACI")));
				mun_enc_dis_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				mun_enc_dis_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				mun_enc_dis_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				mun_enc_dis_bean.setLongitud(new Integer(rs.getString("LONGITUD")));
				mun_enc_dis_bean.setAag_v_cone(new Integer(rs.getString("AAG_V_CONE")));
				mun_enc_dis_bean.setAag_v_ncon(new Integer(rs.getString("AAG_V_NCON")));
				mun_enc_dis_bean.setAag_c_invi(new Integer(rs.getString("AAG_C_INVI")));
				mun_enc_dis_bean.setAag_c_vera(new Integer(rs.getString("AAG_C_VERA")));
				mun_enc_dis_bean.setAag_v_expr(new Integer(rs.getString("AAG_V_EXPR")));
				mun_enc_dis_bean.setAag_v_depr(new Integer(rs.getString("AAG_V_DEPR")));
				mun_enc_dis_bean.setAag_l_defi(new Integer(rs.getString("AAG_L_DEFI")));
				mun_enc_dis_bean.setAag_v_defi(new Integer(rs.getString("AAG_V_DEFI")));
				mun_enc_dis_bean.setAag_pr_def(new Integer(rs.getString("AAG_PR_DEF")));
				mun_enc_dis_bean.setAag_pe_def(new Integer(rs.getString("AAG_PE_DEF")));
				mun_enc_dis_bean.setAau_vivien(new Integer(rs.getString("AAU_VIVIEN")));
				mun_enc_dis_bean.setAau_pob_re(new Integer(rs.getString("aau_pob_re")));
				mun_enc_dis_bean.setAau_pob_es(new Integer(rs.getString("aau_pob_es")));
				mun_enc_dis_bean.setAau_def_vi(new Integer(rs.getString("aau_def_vi")));
				mun_enc_dis_bean.setAau_def_re(new Integer(rs.getString("aau_def_re")));
				mun_enc_dis_bean.setAau_def_es(new Integer(rs.getString("aau_def_es")));
				mun_enc_dis_bean.setAau_fecont(new Integer(rs.getString("aau_fecont")));
				mun_enc_dis_bean.setAau_fencon(new Integer(rs.getString("AAU_FENCON")));
				mun_enc_dis_bean.setLongi_ramal(new Integer(rs.getString("LONGIT_RAM")));
				mun_enc_dis_bean.setSyd_v_cone(new Integer(rs.getString("SYD_V_CONE")));
				mun_enc_dis_bean.setSyd_v_ncon(new Integer(rs.getString("SYD_V_NCON")));
				mun_enc_dis_bean.setSyd_l_defi(new Integer(rs.getString("SYD_L_DEFI")));
				mun_enc_dis_bean.setSyd_v_defi(new Integer(rs.getString("SYD_V_DEFI")));
				mun_enc_dis_bean.setSyd_c_desa(new Integer(rs.getString("SYD_C_DESA")));
				mun_enc_dis_bean.setSyd_c_trat(new Integer(rs.getString("SYD_C_TRAT")));
				mun_enc_dis_bean.setSau_vivien(new Integer(rs.getString("SAU_VIVIEN")));
				mun_enc_dis_bean.setSau_pob_re(new Integer(rs.getString("SAU_POB_RE")));
				mun_enc_dis_bean.setSau_pob_es(new Integer(rs.getString("SAU_POB_ES")));
				mun_enc_dis_bean.setSau_vi_def(new Integer(rs.getString("SAU_VI_DEF")));
				mun_enc_dis_bean.setSau_re_def(new Integer(rs.getString("SAU_RE_DEF")));
				mun_enc_dis_bean.setSau_es_def(new Integer(rs.getString("SAU_ES_DEF")));
				mun_enc_dis_bean.setProdu_basu(new Integer(rs.getString("PRODU_BASU")));
				mun_enc_dis_bean.setContenedores(new Integer(rs.getString("CONTENEDOR")));
				mun_enc_dis_bean.setRba_v_sser(new Integer(rs.getString("RBA_V_SSER")));
				mun_enc_dis_bean.setRba_pr_sse(new Integer(rs.getString("RBA_PR_SSE")));
				mun_enc_dis_bean.setRba_pe_sse(new Integer(rs.getString("RBA_PE_SSE")));
				mun_enc_dis_bean.setRba_plalim(new Integer(rs.getString("RBA_PLALIM")));
				mun_enc_dis_bean.setPuntos_luz(new Integer(rs.getString("PUNTOS_LUZ")));
				mun_enc_dis_bean.setAlu_v_sin(new Integer(rs.getString("ALU_V_SIN")));
				mun_enc_dis_bean.setAlu_l_sin(new Integer(rs.getString("ALU_L_SIN")));
				
				lstNumEncDis.add(munEncDis);
	
			}
				
			sql = "SELECT * FROM v_PADRON";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			
			ArrayList lstPadron = new ArrayList();
			while (rs.next()) {	

				V_padron_bean  padron_bean = new V_padron_bean();

				padron_bean.setCodprov(rs.getString("PROVINCIA"));
				padron_bean.setCodmunic(rs.getString("MUNICIPIO"));
				padron_bean.setN_hombre_a1(new Integer(rs.getString("HOMBRES")));
				padron_bean.setN_mujeres_a1(new Integer(rs.getString("MUJERES")));
				padron_bean.setTotal_poblacion_a1(new Integer(rs.getString("TOTAL_POB")));
				
				lstPadron.add(padron_bean);
			}
			
			
			int contTexto = 0;
			
			ArrayList lstTabla1 = new ArrayList();
			for (int i = 0; i < lstNuclEncuestado.size(); i++)
            {
				CodIne_bean codIne_bean = new CodIne_bean();
				
				codIne_bean.setProvincia(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getProvincia());
				codIne_bean.setMunicipio(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getMunicipio());
				codIne_bean.setEntidad(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getEntidad());
				codIne_bean.setNucleo(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getNucleo());
				
				
				lstTabla1.add(codIne_bean);
            }
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				//ArrayList lstTabla1 = new ArrayList();
				for(int i=0; i<lstNuclEncuestado.size(); i++){
					
					V_nucl_encuestado_1_bean nuclEnc1_aux = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i);
					
					CodIne_bean codIne_bean = new CodIne_bean();
					codIne_bean.setProvincia(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getMunicipio());
					codIne_bean.setEntidad(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getEntidad());
					codIne_bean.setNucleo(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i)).getNucleo());
					
					//lstTabla1.add(codIne_bean);
					
					
					String codine = nuclEnc1_aux.getProvincia() + nuclEnc1_aux.getMunicipio() + 
									nuclEnc1_aux.getEntidad() + nuclEnc1_aux.getNucleo();
					 
					 if (nuclEnc1_aux.getPadron() <= 0)
		             {
		                 if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_01") + "\n");
		                     contTexto++;
		                 }
		                 str.append(codine + "\t");
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
				for(int i=0; i<lstNuclEncuestado.size(); i++){
					
					V_nucl_encuestado_1_bean nuclEnc1_aux = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i);
					
					String codine = nuclEnc1_aux.getProvincia() + nuclEnc1_aux.getMunicipio() + 
									nuclEnc1_aux.getEntidad() + nuclEnc1_aux.getNucleo();
					 
					if (!(nuclEnc1_aux.getPob_estaci() >= nuclEnc1_aux.getPadron()))
					{
					     if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_02") + "\n");
		                     contTexto++;
		                 }
		                 str.append(codine + "\t");
		                 error = true;
		             }
				}
				if (error)
					str.append("\n\n");
				contTexto = 0;
				error = false;
			}
			
			if(lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03)
				for(int i=0; i<lstNuclEncuestado.size(); i++){
					
					V_nucl_encuestado_1_bean nuclEnc1_aux = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i);
					
					String codine = nuclEnc1_aux.getProvincia() + nuclEnc1_aux.getMunicipio() + 
									nuclEnc1_aux.getEntidad() + nuclEnc1_aux.getNucleo();
					 
					if (nuclEnc1_aux.getAltitud() <= 0)
					{
					     if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_03") + "\n");
		                     contTexto++;
		                 }
		                 str.append(codine + "\t");
		                 error = true;
		             }
				}
				if (error)
					str.append("\n\n");
				contTexto = 0;
				error = false;
			}
			
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04)
				for(int i=0; i<lstNuclEncuestado.size(); i++){
					
					V_nucl_encuestado_1_bean nuclEnc1_aux = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i);
					
					String codine = nuclEnc1_aux.getProvincia() + nuclEnc1_aux.getMunicipio()+ 
									nuclEnc1_aux.getEntidad() + nuclEnc1_aux.getNucleo();
					 
					if (nuclEnc1_aux.getViv_total() <= 0)
					{
					     if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_04") + "\n");
		                     contTexto++;
		                 }
		                 str.append(codine + "\t");
		                 error = true;
		             }
				}
				if (error)
					str.append("\n\n");
				contTexto = 0;
				error = false;
			}
		
			if(lstValCuadros.contains("v05")){
				for(int i=0; i<lstNuclEncuestado1_distinct.size(); i++){
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean_distinct = (V_nucl_encuestado_1_bean)lstNuclEncuestado1_distinct.get(i);
				
					int padronNucEnc = 0;
					ArrayList lstNuclEncuestado_encontrado = new ArrayList();
					//Obtenemos la suma del campo padron para ese provinica \municipio
					for(int j=0; j<lstNuclEncuestado.size(); j++){
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j);
					
						if(//nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_1_bean_distinct.getProvincia()) &&
								nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_1_bean_distinct.getMunicipio())){
							padronNucEnc += nucl_encuestado_1_bean.getPadron();
							
							lstNuclEncuestado_encontrado.add(nucl_encuestado_1_bean);
						}
				
					}
					
					int padronMunEnc = 0;
					int padronPadron = 0;
					for(int j=0; j<lstNuclEncuestado_encontrado.size(); j++){
						padronMunEnc = 0;
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstNuclEncuestado_encontrado.get(j);
	
						int count =0;
						for(int h=0; h<lstNumEncDis.size(); h++){
							V_mun_enc_dis_bean munEncDis = (V_mun_enc_dis_bean)lstNumEncDis.get(h);
	
							if(munEncDis.getCodprov().equals(nucl_encuestado_1_bean.getProvincia()) &&
									munEncDis.getCodmunic().equals(nucl_encuestado_1_bean_distinct.getMunicipio())){
								
								count ++;
							}
			
						}
						
						if(count != 0){
							padronMunEnc = nucl_encuestado_1_bean.getPadron();
						}
						else{
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro01.falsoerror.V_1") + "\n");
	
								 contTexto++;
							 }
							 str.append( nucl_encuestado_1_bean_distinct.getProvincia() + nucl_encuestado_1_bean_distinct.getMunicipio()+"\t");
							 //contTexto = 0;
							// str.append("\n\n");
							 error = true;
						}				
					}
					if (error)
						str.append("\n\n");
					contTexto = 0;
					error = false;
					for(int j=0; j<lstNuclEncuestado_encontrado.size(); j++){
						padronPadron= 0;
						V_nucl_encuestado_1_bean nucl_encuestado_1_bean = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j);
	
	//					if(nucl_encuestado_1_bean.getProvincia().equals(nucl_encuestado_1_bean.getProvincia()) &&
	//							nucl_encuestado_1_bean.getMunicipio().equals(nucl_encuestado_1_bean.getMunicipio())){
							
							for(int h=0; h<lstPadron.size(); h++){
								V_padron_bean padron = (V_padron_bean)lstPadron.get(h);
	
								if(padron.getCodprov().equals(nucl_encuestado_1_bean.getProvincia()) &&
										padron.getCodmunic().equals(nucl_encuestado_1_bean.getMunicipio())){
									padronPadron = padron.getTotal_poblacion_a1();
									
									if(padronPadron == 0){
										if (contTexto == 0)
										 {
											 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro01.falsoerror.V_1") + "\n");
											 str.append("\n");
											 contTexto++;
										 }
										 str.append(  nucl_encuestado_1_bean_distinct.getProvincia() + nucl_encuestado_1_bean_distinct.getMunicipio()+"\t");
										// contTexto = 0;
										// str.append("\n\n");
										 error = true;
									}
									
								}
								
												
							}
					//	}
					}
					if (error)
						str.append("\n\n");
					contTexto = 0;
					error = false;
					if ((padronNucEnc + padronMunEnc) != padronPadron){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro01.falsoerror.V_1") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append( nucl_encuestado_1_bean_distinct.getProvincia() + nucl_encuestado_1_bean_distinct.getMunicipio()+"\t");
						 //contTexto = 0;
						// str.append("\n\n");
						 error = true;
					}
	
				}
				if (error)
					str.append("\n\n");
				contTexto = 0;
				error = false;
			}
			
			if(lstValCuadros.contains("v06")){
				//ERROR DEL MPT -> (V_06)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE_poblamient(connection, lstTabla1, "v_INFRAESTR_VIARIA", "cuadro01.V_06", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_2", "cuadro01.V_07", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v08")){
				//ERROR DEL MPT -> (V_08)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_3", "cuadro01.V_08", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
				
			if(lstValCuadros.contains("v09")){
				//ERROR DEL MPT -> (V_09)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_4", "cuadro01.V_09", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v10")){
				//ERROR DEL MPT -> (V_10)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_5", "cuadro01.V_10", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
        	}
		
			if(lstValCuadros.contains("v11")){
				//ERROR DEL MPT -> (V_11)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_6", "cuadro01.V_11", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v12")){
				//ERROR DEL MPT -> (V_12)
				contTexto = FuncionesComunes.ValidaExistenciaCODINE_poblamient(connection, lstTabla1, "v_NUCL_ENCUESTADO_7", "cuadro01.V_12", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v13")){
				//ERROR DEL MPT -> (V_13)
				for (int i = 0; i < lstPadron.size(); i++)
	            {
					V_padron_bean padron   = (V_padron_bean)lstPadron.get(i);
					
					if(padron.getTotal_poblacion_a1() <= 50000){
						int count = 0;
						for (int j = 0; j < lstNuclEncuestado.size(); j++)
			            {
							V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j);
							if(nucl_encuestado_1_bean.getProvincia().equals(padron.getCodprov()) &&
									nucl_encuestado_1_bean.getMunicipio().equals(padron.getCodprov())){
								count ++;
							}
			            }
						if(count == 0){
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_13") + "\n");
								 contTexto++;
							 }
							 str.append(  padron.getCodprov() + padron.getCodprov() + "\t");
							 error = true;
						}
					}
	            }
				if (error)
					str.append("\n\n");
				contTexto = 0;
				error = false;
        	}	
			
			if(lstValCuadros.contains("v14")){
				//ERROR DEL MPT -> (V_14)
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_PLAN_URBANISTICO", "cuadro01.V_14", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v15")){
				//ERROR DEL MPT -> (V_15)
	
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_OT_SERV_MUNICIPAL", "cuadro01.V_15", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v16")){
				//ERROR DEL MPT -> (V_16)
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_TRAMO_CARRETERA", "cuadro01.V_16", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v17")){
				//ERROR DEL MPT -> (V_17)
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_CASA_CONSISTORIAL", "cuadro01.V_17", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			//ERROR DEL MPT -> (V_18)
			if(lstValCuadros.contains("v18")){
				for (int i = 0; i < lstNuclEncuestado.size(); i++)
	            {
					V_nucl_encuestado_1_bean nucl_encuestado_1_bean   = (V_nucl_encuestado_1_bean)lstNuclEncuestado.get(i);
					if(nucl_encuestado_1_bean.getNucleo().equals("99")){
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro01.V_18") + "\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_1_bean.getProvincia() + nucl_encuestado_1_bean.getMunicipio() +
								 nucl_encuestado_1_bean.getEntidad() + nucl_encuestado_1_bean.getNucleo() +"\t");
					}
	            }	
			}
			str.append("\n\n");
			
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro01.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
