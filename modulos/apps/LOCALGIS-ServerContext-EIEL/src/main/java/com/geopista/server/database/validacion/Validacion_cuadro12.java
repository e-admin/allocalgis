/**
 * Validacion_cuadro12.java
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
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
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

public class Validacion_cuadro12 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro12.class);
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro12") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_TRAMO_CONDUCCION where LONGITUD = 0 and ESTADO <> 'E'";
			
			ArrayList lsttramo_conduccion = new ArrayList();

			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {	
				V_tramo_conduccion_bean tramo_conduccion_bean = new V_tramo_conduccion_bean();

				tramo_conduccion_bean.setClave(rs.getString("CLAVE"));
				tramo_conduccion_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_conduccion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_conduccion_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				tramo_conduccion_bean.setTipo_tcond(rs.getString("TIPO_TCOND"));
				tramo_conduccion_bean.setEstado(rs.getString("ESTADO"));
				tramo_conduccion_bean.setTitular(rs.getString("TITULAR"));
				tramo_conduccion_bean.setGestion(rs.getString("GESTION"));				
				if(rs.getString("LONGITUD")!=null&&!rs.getString("LONGITUD").equals(""))
					tramo_conduccion_bean.setLongitud(new Double(Math.rint(new Double(rs.getString("LONGITUD")))).intValue());
				else
					tramo_conduccion_bean.setLongitud(0);	
	
				lsttramo_conduccion.add(tramo_conduccion_bean);
				
			}

			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lsttramo_conduccion.size(); i++)
	            {
					V_tramo_conduccion_bean tramo_conduccion_bean = (V_tramo_conduccion_bean)lsttramo_conduccion.get(i);
					
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro12.V_01") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(tramo_conduccion_bean.getClave() + tramo_conduccion_bean.getProvincia()+ 
							 tramo_conduccion_bean.getMunicipio() + tramo_conduccion_bean.getOrden_cond() +"\t");
					 str.append("\t");
				}
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro12.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
