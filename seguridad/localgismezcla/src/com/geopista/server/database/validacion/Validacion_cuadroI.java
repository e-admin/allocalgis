package com.geopista.server.database.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_agua_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;

public class Validacion_cuadroI {
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroI") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;

			String sql = "select * from  v_CONDUCCION";
			String sql_1 = "SELECT * FROM v_COND_AGUA_NUCLEO";
			String sql_2 = "SELECT * FROM v_CONDUCCION_ENC";
			String sql_3 = "SELECT * FROM v_CONDUCCION_ENC_M50";


			
	        ArrayList lstconduccion = new ArrayList();
	    	ArrayList lstcond_agua_nucleo = new ArrayList();
			ArrayList lstconduccion_enc = new ArrayList();
			ArrayList lstconduccion_enc_m50 = new ArrayList();
			
			
			 ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_conduccion_bean conduccion_bean = new V_conduccion_bean();
				conduccion_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				conduccion_bean.setClave(rs.getString("CLAVE"));
				
				lstconduccion.add(conduccion_bean);
				
			}
				
	        ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
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
				lstcond_agua_nucleo.add(cond_agua_nucleo_bean);
				
			}
	        
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_conduccion_enc_bean conduccion_enc_bean  = new V_conduccion_enc_bean();
				conduccion_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_bean.setOrden_cond(rs.getString("ORDEN_COND"));
				conduccion_enc_bean.setClave(rs.getString("CLAVE"));
				lstconduccion_enc.add(conduccion_enc_bean);
				
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
V_conduccion_enc_m50_bean conduccion_enc_m50_bean = new V_conduccion_enc_m50_bean();
				
				conduccion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				conduccion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				conduccion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				conduccion_enc_m50_bean.setOrden_cond(rs.getString("ORDEN_COND"));	
				lstconduccion_enc_m50.add(conduccion_enc_m50_bean);
				
			}
	        
			
			for (int i = 0; i < lstconduccion.size(); i++)
            {
				V_conduccion_bean conduccion_bean   = (V_conduccion_bean)lstconduccion.get(i);
				int contador = 0;
				 
				for (int j = 0; j < lstcond_agua_nucleo.size(); j++)
	            {
					V_cond_agua_nucleo_bean cond_agua_nucleo_bean    = (V_cond_agua_nucleo_bean)lstcond_agua_nucleo.get(j);
					if(cond_agua_nucleo_bean.getProvincia().equals(conduccion_bean.getProvincia()) &&
							cond_agua_nucleo_bean.getMunicipio().equals(conduccion_bean.getMunicipio())){
						if(cond_agua_nucleo_bean.getOrden_cond().equals(conduccion_bean.getOrden_cond()) &&
								cond_agua_nucleo_bean.getClave().equals(conduccion_bean.getClave())){
							contador ++;
						}
						
					}
	            }
				
				if (contador == 0){
					for (int j = 0; j < lstconduccion_enc.size(); j++)
		            {
						V_conduccion_enc_bean conduccion_enc_bean   = (V_conduccion_enc_bean)lstconduccion_enc.get(j);
						if(conduccion_enc_bean.getProvincia().equals(conduccion_bean.getProvincia()) &&
								conduccion_enc_bean.getMunicipio().equals(conduccion_bean.getMunicipio())){
							if(conduccion_enc_bean.getOrden_cond().equals(conduccion_bean.getOrden_cond()) &&
									conduccion_enc_bean.getClave().equals(conduccion_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}
				
				if (contador == 0){
					for (int j = 0; j < lstconduccion_enc_m50.size(); j++)
		            {
						V_conduccion_enc_m50_bean conduccion_enc_m50_bean   = (V_conduccion_enc_m50_bean)lstconduccion_enc_m50.get(j);
						if(conduccion_enc_m50_bean.getProvincia().equals(conduccion_bean.getProvincia()) &&
								conduccion_enc_m50_bean.getMunicipio().equals(conduccion_bean.getMunicipio())){
							if(conduccion_enc_m50_bean.getOrden_cond().equals(conduccion_bean.getOrden_cond()) &&
									conduccion_enc_m50_bean.getClave().equals(conduccion_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}	
				

				if (contador == 0){
					if (contTexto == 0)
					 {
						str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroI.V_01") + "\n");
						 contTexto++;
					 }
					 str.append(  conduccion_bean.getProvincia() + conduccion_bean.getMunicipio() + conduccion_bean.getOrden_cond()+ "\t");
				}
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroI.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
