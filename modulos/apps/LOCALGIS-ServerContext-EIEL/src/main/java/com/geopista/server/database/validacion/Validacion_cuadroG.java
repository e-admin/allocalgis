/**
 * Validacion_cuadroG.java
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

import com.geopista.protocol.control.Sesion;
import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_carretera_bean;
import com.geopista.server.database.validacion.beans.V_centro_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadroG {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadroG.class);

	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroG") + "\n");
			str.append("______________________________________________________________________\n\n");

			
			String sql = "select * from v_CARRETERA";
	        String sql_1 = "select * from v_TRAMO_CARRETERA";
			
	        ArrayList lstcarretera = new ArrayList();
			ArrayList lsttramo_carretera = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_carretera_bean carreteraBean = new V_carretera_bean();

				carreteraBean.setProvincia(rs.getString("provincia"));
				carreteraBean.setCod_carrt(rs.getString("cod_carrt"));
				carreteraBean.setDenominaci(rs.getString("denominaci"));
				

				lstcarretera.add(carreteraBean);
	
			}
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_carretera_bean  tramo_carretera_bean = new V_tramo_carretera_bean();

				tramo_carretera_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_carretera_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_carretera_bean.setCod_carrt(rs.getString("COD_CARRT"));
				if(rs.getString("PK_INICIAL")!=null&&!rs.getString("PK_INICIAL").equals(""))		
					tramo_carretera_bean.setPk_inicial(new Double(rs.getString("PK_INICIAL")));
				else
					tramo_carretera_bean.setPk_inicial(0.0);
				if(rs.getString("PK_FINAL")!=null&&!rs.getString("PK_FINAL").equals(""))		
					tramo_carretera_bean.setPk_final(new Double(rs.getString("PK_FINAL")));
				else
					tramo_carretera_bean.setPk_final(0.0);
				tramo_carretera_bean.setTitular(rs.getString("TITULAR"));
				tramo_carretera_bean.setGestion(rs.getString("GESTION"));
				tramo_carretera_bean.setSenaliza(rs.getString("SENALIZA"));
				tramo_carretera_bean.setFirme(rs.getString("FIRME"));
				tramo_carretera_bean.setEstado(rs.getString("ESTADO"));

				if(rs.getString("ANCHO")!=null&&!rs.getString("ANCHO").equals(""))		
					tramo_carretera_bean.setAncho(new Double(rs.getString("ANCHO")));	
				else
					tramo_carretera_bean.setAncho(0.0);	
				//tramo_carretera_bean.setAncho(new Double(rs.getString("ANCHO")));
				
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))		
					tramo_carretera_bean.setLongitud(new Double(rs.getString("LONGITUD")));	
				else
					tramo_carretera_bean.setLongitud(0.0);	
				if(rs.getString("PASOS_NIVE")!=null&&!rs.getString("PASOS_NIVE").equals(""))		
					tramo_carretera_bean.setPasos_nive(new Integer(rs.getString("PASOS_NIVE")));
				else
					tramo_carretera_bean.setPasos_nive(0);
				tramo_carretera_bean.setDimensiona(rs.getString("DIMENSIONA"));
				tramo_carretera_bean.setMuy_sinuos(rs.getString("MUY_SINUOS"));
				tramo_carretera_bean.setPte_excesi(rs.getString("PTE_EXCESI"));
				tramo_carretera_bean.setFre_estrec(rs.getString("FRE_ESTREC"));
				
				lsttramo_carretera.add(tramo_carretera_bean);
	
			}
			
			int contTexto = 0;
	        
			for (int i = 0; i < lstcarretera.size(); i++)
            {
				V_carretera_bean carretera_bean   = (V_carretera_bean)lstcarretera.get(i);
				int count = 0;
				for (int j = 0; j < lsttramo_carretera.size(); j++)
	            {
					V_tramo_carretera_bean tramo_carretera_bean   = (V_tramo_carretera_bean)lsttramo_carretera.get(j);
					if(tramo_carretera_bean.getProvincia().equals(carretera_bean.getProvincia()) &&
							tramo_carretera_bean.getCod_carrt().equals(carretera_bean.getCod_carrt())){
						count ++;
					}
	            }
				if(count == 0){
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroG.V_01") + "\n");
						 contTexto++;
					 }
					 str.append( carretera_bean.getCod_carrt() +"\t");
				}	
            }
			 str.append("\n\n");
	       
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroG.class +" " +  e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}

}
