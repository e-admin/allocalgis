/**
 * Validacion_listados.java
 * � MINETUR, Government of Spain
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
import com.geopista.server.database.validacion.beans.V_alumbrado_bean;
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
import com.geopista.server.database.validacion.beans.V_inst_depor_deporte_bean;
import com.geopista.server.database.validacion.beans.V_instal_deportiva_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_7_bean;
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

public class Validacion_listados {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Validacion_listados.class);
	public static void validacion(Connection connection, StringBuffer str){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {  
			str.append(Messages.getString("listados") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_NUCL_ENCUESTADO_1";


			
			ArrayList lstnucl_encuestado_1 = new ArrayList();


			
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
				
				//NULLnucl_encuestado_1_bean.setAltitud(new Integer(rs.getString("ALTITUD")));
				
				if(rs.getString("VIV_TOTAL")!=null&&!rs.getString("VIV_TOTAL").equals(""))
					nucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				else
					nucl_encuestado_1_bean.setViv_total(0);
				
				//NULLnucl_encuestado_1_bean.setViv_total(new Integer(rs.getString("VIV_TOTAL")));
				
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

				lstnucl_encuestado_1.add(nucl_encuestado_1_bean);
	
			}
		
			ArrayList lstTabla1 = new ArrayList();
			for (int i = 0; i < lstnucl_encuestado_1.size(); i++)
            {
				CodIne_bean codIne_bean = new CodIne_bean();
				
				codIne_bean.setProvincia(((V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i)).getProvincia());
				codIne_bean.setMunicipio(((V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i)).getMunicipio());
				codIne_bean.setEntidad(((V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i)).getEntidad());
				codIne_bean.setNucleo(((V_nucl_encuestado_1_bean)lstnucl_encuestado_1.get(i)).getNucleo());
				
				lstTabla1.add(codIne_bean);
            }
			//Listado 001
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_CAP_AGUA_NUCLEO", "LISTADO_001", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 002
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_cond_agua_nucleo", "LISTADO_002", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 003
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_deposito_agua_nucleo", "LISTADO_003", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 004
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_trat_pota_nucleo", "LISTADO_004", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 005
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_red_distribucion", "LISTADO_005", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 006
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_ramal_saneamiento", "LISTADO_006", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 007
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_colector_nucleo", "LISTADO_007", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 008
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_emisario_nucleo", "LISTADO_008", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 009
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_dep_agua_nucleo", "LISTADO_009", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 010
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_recogida_basura", "LISTADO_010", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 011
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_vertedero_nucleo", "LISTADO_011", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
			
			//Listado 012
			contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_alumbrado", "LISTADO_012", str);
			if (contTexto != 0)
				str.append("\n\n");
			contTexto = 0;
		
			str.append("\n\n");
        }
        catch (Exception e)
        {
         	logger.error("Exception:",e);
        	str.append(Messages.getString("exception") + " " +Validacion_listados.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
