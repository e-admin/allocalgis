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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro34 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {       
			str.append(Messages.getString("cuadro34") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_TRAMO_EMISARIO_M50 where (LONG_TERRE + LONG_MARIT)= 0 AND ESTADO <> 'E'";

		
			ArrayList lsttramo_emisario_m50 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tramo_emisario_m50_bean tramo_emisario_m50_bean = new V_tramo_emisario_m50_bean();

				tramo_emisario_m50_bean.setClave(rs.getString("CLAVE"));
				tramo_emisario_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				tramo_emisario_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tramo_emisario_m50_bean.setOrden_emis(rs.getString("ORDEN_EMIS"));
				tramo_emisario_m50_bean.setTipo_mat(rs.getString("TIPO_MAT"));
				tramo_emisario_m50_bean.setEstado(rs.getString("ESTADO"));
				if(rs.getString("LONG_TERRE")!=null&&!rs.getString("LONG_TERRE").equals(""))
					tramo_emisario_m50_bean.setLong_terre(new Double(Math.rint(new Double(rs.getString("LONG_TERRE")))).intValue());
				else
					tramo_emisario_m50_bean.setLong_terre(0);
				if(rs.getString("LONG_MARIT")!=null&&!rs.getString("LONG_MARIT").equals(""))
					tramo_emisario_m50_bean.setLong_marit(new Double(Math.rint(new Double(rs.getString("LONG_MARIT")))).intValue());
				else
					tramo_emisario_m50_bean.setLong_marit(0);

				lsttramo_emisario_m50.add(tramo_emisario_m50_bean);
				
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i <lsttramo_emisario_m50.size(); i++)
	            {
					V_tramo_emisario_m50_bean tramo_emisario_m50_bean = (V_tramo_emisario_m50_bean)lsttramo_emisario_m50.get(i);
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro20.V_01") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(  tramo_emisario_m50_bean.getClave() + tramo_emisario_m50_bean.getProvincia() +
							 tramo_emisario_m50_bean.getMunicipio() + tramo_emisario_m50_bean.getOrden_emis() + "\t");
	
	
				}
			}
			str.append("\n\n");
			
			
		
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro34.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
