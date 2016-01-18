package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;

public class Validacion_cuadro07 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro07") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_CAPTACION_ENC";
			String sql_1 = "select * from v_CAP_AGUA_NUCLEO";
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList lstcaptacion_enc = new ArrayList();
			while (rs.next()) {	
				    
				V_captacion_enc_bean captacion_encBean = new V_captacion_enc_bean();

				captacion_encBean.setProvincia(rs.getString("provincia"));
				captacion_encBean.setMunicipio(rs.getString("municipio"));
				captacion_encBean.setClave(rs.getString("clave"));
				captacion_encBean.setOrden_capt(rs.getString("orden_capt"));
				captacion_encBean.setDenominaci(rs.getString("denominaci"));
				captacion_encBean.setTipo_capt(rs.getString("tipo_capt"));
				captacion_encBean.setTitular(rs.getString("titular"));
				captacion_encBean.setGestion(rs.getString("gestion"));
				captacion_encBean.setSistema_ca(rs.getString("sistema_ca"));
				captacion_encBean.setEstado(rs.getString("estado"));
				captacion_encBean.setUso(rs.getString("uso"));
				captacion_encBean.setProteccion(rs.getString("proteccion"));
				captacion_encBean.setContador(rs.getString("contador"));

				lstcaptacion_enc.add(captacion_encBean);
			}
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			ArrayList lstcapt_agua_nucleo = new ArrayList();
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
				
				lstcapt_agua_nucleo.add(cap_agua_nucleoBean);
			}
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstcaptacion_enc.size(); i++)
	            {
					V_captacion_enc_bean captacion_enc = (V_captacion_enc_bean)lstcaptacion_enc.get(i);
					int count = 0;
					for (int j = 0; j < lstcapt_agua_nucleo.size(); j++)
		            {
						if( ((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getC_provinc().equals(captacion_enc.getProvincia()) &&
								((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getC_municip().equals(captacion_enc.getMunicipio()) &&
								((V_cap_agua_nucleo_bean)lstcapt_agua_nucleo.get(j)).getOrden_capt().equals(captacion_enc.getOrden_capt())){
							count ++;
						}
						
		            }
					
					if (count == 0){
						 if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro07.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(captacion_enc.getClave() + captacion_enc.getProvincia()+ 
								 captacion_enc.getMunicipio() + captacion_enc.getOrden_capt() +"\t");
					}
					
					
	            }
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro07.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
