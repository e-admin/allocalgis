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
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;

public class Validacion_cuadroJ {
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroJ") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;

			String sql = "select * from  v_DEPOSITO";
			String sql_1 = "SELECT * FROM v_DEPOSITO_AGUA_NUCLEO";
			String sql_2 = "SELECT * FROM v_DEPOSITO_ENC";
			String sql_3 = "SELECT * FROM v_DEPOSITO_ENC_M50";
			
	        ArrayList lstdepostio = new ArrayList();
	    	ArrayList lstdeposito_agua_nucleo = new ArrayList();
			ArrayList lstdeposito_enc = new ArrayList();
			ArrayList lstdeposito_m50 = new ArrayList();
			
			
			 ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_deposito_bean deposito_bean = new V_deposito_bean();
				deposito_bean.setProvincia(rs.getString("PROVINCIA"));
				deposito_bean.setMunicipio(rs.getString("MUNICIPIO"));
				deposito_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
				deposito_bean.setClave(rs.getString("CLAVE"));
				
				lstdepostio.add(deposito_bean);
				
			}
				
	        ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
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
				lstdeposito_agua_nucleo.add(deposito_agua_nucleo_bean);
				
			}
	        
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_deposito_enc_bean deposito_enc_bean = new V_deposito_enc_bean();

				deposito_enc_bean.setClave(rs.getString("CLAVE"));
				deposito_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				deposito_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				deposito_enc_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
				deposito_enc_bean.setUbicacion(rs.getString("UBICACION"));
				deposito_enc_bean.setTitular(rs.getString("TITULAR"));
				deposito_enc_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&rs.getString("LIMPIEZA")!="")
					deposito_enc_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					deposito_enc_bean.setCapacidad(0);
				deposito_enc_bean.setEstado(rs.getString("ESTADO"));
				deposito_enc_bean.setProteccion(rs.getString("PROTECCION"));
				if(rs.getString("LIMPIEZA")!=null &&!rs.getString("LIMPIEZA").equals(""))
					deposito_enc_bean.setLimpieza(new Integer(rs.getString("LIMPIEZA")));
				else
					deposito_enc_bean.setLimpieza(0);
				deposito_enc_bean.setContador(rs.getString("CONTADOR"));
				
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_deposito_enc_m50_bean deposito_enc_m50_bean = new V_deposito_enc_m50_bean();

				deposito_enc_m50_bean.setClave(rs.getString("CLAVE"));
				deposito_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				deposito_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				deposito_enc_m50_bean.setOrden_depo(rs.getString("ORDEN_DEPO"));
				deposito_enc_m50_bean.setUbicacion(rs.getString("UBICACION"));
				deposito_enc_m50_bean.setTitular(rs.getString("TITULAR"));
				deposito_enc_m50_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&rs.getString("LIMPIEZA")!="")
					deposito_enc_m50_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					deposito_enc_m50_bean.setCapacidad(0);
				deposito_enc_m50_bean.setEstado(rs.getString("ESTADO"));
				deposito_enc_m50_bean.setProteccion(rs.getString("PROTECCION"));
				if(rs.getString("LIMPIEZA")!=null &&!rs.getString("LIMPIEZA").equals(""))
					deposito_enc_m50_bean.setLimpieza(new Integer(rs.getString("LIMPIEZA")));
				else
					deposito_enc_m50_bean.setLimpieza(0);
				deposito_enc_m50_bean.setContador(rs.getString("CONTADOR"));

				lstdeposito_m50.add(deposito_enc_m50_bean);
				
			}
	        
			
			for (int i = 0; i < lstdepostio.size(); i++)
            {
				V_deposito_bean deposito_bean   = (V_deposito_bean)lstdepostio.get(i);
				int contador = 0;
				 
				for (int j = 0; j < lstdeposito_agua_nucleo.size(); j++)
	            {
					V_deposito_agua_nucleo_bean deposito_agua_nucleo_bean    = (V_deposito_agua_nucleo_bean)lstdeposito_agua_nucleo.get(j);
					if(deposito_agua_nucleo_bean.getProvincia().equals(deposito_bean.getProvincia()) &&
							deposito_agua_nucleo_bean.getMunicipio().equals(deposito_bean.getMunicipio())){
						if(deposito_agua_nucleo_bean.getOrden_depo().equals(deposito_bean.getOrden_depo()) &&
								deposito_agua_nucleo_bean.getClave().equals(deposito_bean.getClave())){
							contador ++;
						}
						
					}
	            }
				
				if (contador == 0){
					for (int j = 0; j < lstdeposito_enc.size(); j++)
		            {
						V_deposito_enc_bean deposito_enc_bean   = (V_deposito_enc_bean)lstdeposito_enc.get(j);
						if(deposito_enc_bean.getProvincia().equals(deposito_bean.getProvincia()) &&
								deposito_enc_bean.getMunicipio().equals(deposito_bean.getMunicipio())){
							if(deposito_enc_bean.getOrden_depo().equals(deposito_bean.getOrden_depo()) &&
									deposito_enc_bean.getClave().equals(deposito_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}
				
				if (contador == 0){
					for (int j = 0; j < lstdeposito_m50.size(); j++)
		            {
						V_deposito_enc_m50_bean deposito_enc_m50_bean   = (V_deposito_enc_m50_bean)lstdeposito_m50.get(j);
						if(deposito_enc_m50_bean.getProvincia().equals(deposito_bean.getProvincia()) &&
								deposito_enc_m50_bean.getMunicipio().equals(deposito_bean.getMunicipio())){
							if(deposito_enc_m50_bean.getOrden_depo().equals(deposito_bean.getOrden_depo()) &&
									deposito_enc_m50_bean.getClave().equals(deposito_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}	
				

				if (contador == 0){
					if (contTexto == 0)
					 {
						str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroJ.V_01") + "\n");
						 contTexto++;
					 }
					 str.append(  deposito_bean.getProvincia() + deposito_bean.getMunicipio() + deposito_bean.getOrden_depo()+ "\t");
				}
						  
                    
				
			
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroJ.class + " " +  e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
