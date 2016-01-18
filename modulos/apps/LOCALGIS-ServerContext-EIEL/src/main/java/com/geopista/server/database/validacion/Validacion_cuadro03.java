/**
 * Validacion_cuadro03.java
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

public class Validacion_cuadro03 {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_cuadro03.class);

	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro03") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_OT_SERV_MUNICIPAL";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList lstot_serv_municipal = new ArrayList();
			while (rs.next()) {	
				
				V_ot_serv_municipal_bean  ot_serv_municipal_bean = new V_ot_serv_municipal_bean();

				ot_serv_municipal_bean.setProvincia(rs.getString("PROVINCIA"));
				ot_serv_municipal_bean.setMunicipio(rs.getString("MUNICIPIO"));
		
				lstot_serv_municipal.add(ot_serv_municipal_bean);
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lstot_serv_municipal.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_ot_serv_municipal_bean)lstot_serv_municipal.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_ot_serv_municipal_bean)lstot_serv_municipal.get(i)).getMunicipio());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro03.V_01", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
        }
        catch (Exception e)
        {
        	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro03.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
