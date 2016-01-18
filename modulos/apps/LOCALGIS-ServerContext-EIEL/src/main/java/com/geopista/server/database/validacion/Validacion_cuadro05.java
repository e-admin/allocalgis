/**
 * Validacion_cuadro05.java
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
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro05 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro05.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuffer str1 = new StringBuffer();
		StringBuffer str2 = new StringBuffer();
		StringBuffer str3 = new StringBuffer();
		StringBuffer str4 = new StringBuffer();
		StringBuffer str5 = new StringBuffer();
		StringBuffer str6 = new StringBuffer();
		
		boolean error1 = false;
		boolean error2 = false;
		boolean error3 = false;
		boolean error4 = false;
		boolean error5 = false;
		boolean error6 = false;
		ArrayList lstTabla1 = new ArrayList();
		
	    int contTexto1 = 0;
	    int contTexto2 = 0;
	    int contTexto3 = 0;
	    int contTexto4 = 0;
	    int contTexto5 = 0;
	    int contTexto6 = 0;
	    int contTexto7 = 0;
	    
	    
		try
        {
			str.append(Messages.getString("cuadro05") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_INFRAESTR_VIARIA";
			String sql_1 = "select * from v_NUCL_ENCUESTADO_1";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList lstinfraestr_viaria = new ArrayList();
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
				
				
				if(rs.getString("VIV_AFECTA")!=null&&!rs.getString("VIV_AFECTA").equals(""))
					infraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));
				else
					infraestr_viaria_bean.setViv_afecta(0);
				
				//infraestr_viaria_bean.setViv_afecta(new Integer(rs.getString("VIV_AFECTA")));
				
				lstinfraestr_viaria.add(infraestr_viaria_bean);
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			ArrayList lstNuclEncuestado = new ArrayList();
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
				//nucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				
				if(rs.getString("HOTELES")!=null&&!rs.getString("HOTELES").equals(""))				
					nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				else
					nucl_encuestado_1_bean.setHoteles(0);
				//nucl_encuestado_1_bean.setHoteles(new Integer(rs.getString("HOTELES")));
				
				if(rs.getString("CASAS_RURA")!=null&&!rs.getString("CASAS_RURA").equals(""))				
					nucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				else
					nucl_encuestado_1_bean.setCasas_rural(0);
				//NULLnucl_encuestado_1_bean.setCasas_rural(new Integer(rs.getString("CASAS_RURA")));
				nucl_encuestado_1_bean.setAccesib(rs.getString("ACCESIB"));

				
				lstNuclEncuestado.add(nucl_encuestado_1_bean);
			}
			
			ArrayList lstCuadro1 = new ArrayList();
			for (int i = 0; i < lstinfraestr_viaria.size(); i++)
            {
				CodIne_bean codIne_bean = new CodIne_bean();
				codIne_bean.setProvincia(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia());
				codIne_bean.setMunicipio(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio());
				codIne_bean.setEntidad(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad());
				codIne_bean.setNucleo(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo());
				lstTabla1.add(codIne_bean);
				
				if(lstValCuadros.contains("v01")){
					//ERROR DEL MPT -> (V_01)
					 if(!((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEstado().equals("NP")){
						 if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getViv_afecta() != 0){
							 
							 if (contTexto1 == 0)
							 {
								 str1.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_01") + "\n");
								 str1.append("\n");
								 contTexto1++;
							 }
							 str1.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
			                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
							 error1 = true;
					 	}	
					 }
				}
				if(lstValCuadros.contains("v02")){
					//ERROR DEL MPT -> (V_02)
					 if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEstado().equals("NP")){
						 
						 for (int j = 0; j < lstNuclEncuestado.size(); j++)
				         { 
							 if(((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j)).getProvincia().equals(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()) &&
									 ((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j)).getMunicipio().equals(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio()) &&
									 ((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j)).getEntidad().equals(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad()) &&
									 ((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j)).getNucleo().equals(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo())){
								 
								 lstCuadro1.add(lstNuclEncuestado.get(j));
								 
								 if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getViv_afecta() > 0 &&  
										 ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getViv_afecta() > ((V_nucl_encuestado_1_bean)lstNuclEncuestado.get(j)).getViv_total()){
									 
									 if (contTexto2 == 0)
									 {
										 str2.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_02") + "\n");
										 str2.append("\n");
										 contTexto2++;
									 }
									 str2.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
					                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
									 error2 = true;
	 
								 }
							 }
				         }
					 }
				}
				if(lstValCuadros.contains("v03")){
					//ERROR DEL MPT -> (V_03)
					 if (((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getSuperficie() > 0){
						if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getLongitud() <= 0){
							 if (contTexto3 == 0)
							 {
								 str3.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_03") + "\n");
								 str3.append("\n");
								 contTexto3++;
							 }
							 str3.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
			                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
							 error3 = true;
						}
					}
				}
				 
				if(lstValCuadros.contains("v04")){
					//ERROR DEL MPT -> (V_04)
					 if (((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getLongitud() > 0){
						if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getSuperficie() <= 0){
							 if (contTexto4 == 0)
							 {
								 str4.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_04") + "\n");
								 str4.append("\n");
								 contTexto4++;
							 }
							 str4.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
			                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
							 error4 = true;
						}
					}
				}
				if(lstValCuadros.contains("v05")){
					//ERROR DEL MPT -> (V_05)
					 if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getSuperficie() <= 0){
							if(!((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEstado().equals("E")){
								 if (contTexto5 == 0)
								 {
									 str5.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_05") + "\n");
									 str5.append("\n");
									 contTexto5++;
								 }
								 str5.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
				                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
								 error5 = true;
							}
						}
				}
				if(lstValCuadros.contains("v06")){
					//ERROR DEL MPT -> (V_06)
					if(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getLongitud() <= 0){
						if(!((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEstado().equals("E")){
							 if (contTexto6 == 0)
							 {
								 str6.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro05.V_06") + "\n");
								 str6.append("\n");
								 contTexto6++;
							 }
							 str6.append(((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getProvincia()+((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getMunicipio() + 
			                		((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getEntidad() + ((V_infraestr_viaria_bean)lstinfraestr_viaria.get(i)).getNucleo() +"\t");
							 error6 = true;
						}
					}
				}
				 
            }
			
			str.append(str1.toString());
			if (error1)
				str.append("\n\n");
			str.append(str2.toString());
			if (error2)
				str.append("\n\n");
			str.append(str3.toString());
			if (error3)
				str.append("\n\n");
			str.append(str4.toString());
			if (error4)
				str.append("\n\n");
			str.append(str5.toString());
			if (error5)
				str.append("\n\n");
			str.append(str6.toString());
			if (error6)
				str.append("\n\n");
			
			if(lstValCuadros.contains("v07")){
				contTexto7 = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro05.V_07", str);
				if (contTexto7 != 0)
					str.append("\n\n");
			}

        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro05.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
