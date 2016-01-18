package com.geopista.server.database.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.V_colector_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_cuadroO {
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroO") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;
	        String sql = "select * from  v_VERTEDERO";
	        String sql_1 = "SELECT * FROM v_VERTEDERO_NUCLEO";
	        String sql_2 = "SELECT * FROM v_VERT_ENCUESTADO";
	        String sql_3 = "SELECT * FROM v_VERT_ENCUESTADO_M50";
	        
	        
	        ArrayList lstvertedero = new ArrayList();
	    	ArrayList lstvertedero_nucleo = new ArrayList();
			ArrayList lstvertedero_enc = new ArrayList();
			ArrayList lstvertedero_enc_m50 = new ArrayList();
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vertedero_bean vertedero_bean = new V_vertedero_bean();
				vertedero_bean.setProvincia(rs.getString("PROVINCIA"));
				vertedero_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vertedero_bean.setClave(rs.getString("CLAVE"));
				vertedero_bean.setOrden_ver(rs.getString("ORDEN_VER"));

				lstvertedero.add(vertedero_bean);
				
			}
				
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vertedero_nucleo_bean vertedero_nucleo_bean = new V_vertedero_nucleo_bean();

				vertedero_nucleo_bean.setClave(rs.getString("CLAVE"));
				vertedero_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				vertedero_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vertedero_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				vertedero_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				vertedero_nucleo_bean.setVer_provin(rs.getString("VER_PROVIN"));
				vertedero_nucleo_bean.setVer_munici(rs.getString("VER_MUNICI"));
				vertedero_nucleo_bean.setVer_codigo(rs.getString("VER_CODIGO"));

				lstvertedero_nucleo.add(vertedero_nucleo_bean);
				
			}
	        
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vert_encuestado_bean vert_encuestado_bean = new V_vert_encuestado_bean();
				vert_encuestado_bean.setProvincia(rs.getString("PROVINCIA"));
				vert_encuestado_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vert_encuestado_bean.setOrden_ver(rs.getString("ORDEN_VER"));	
				vert_encuestado_bean.setClave(rs.getString("CLAVE"));
				lstvertedero_enc.add(vert_encuestado_bean);
				
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_vert_encuestado_m50_bean vert_encuestado_m50_bean = new V_vert_encuestado_m50_bean();
				vert_encuestado_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				vert_encuestado_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				vert_encuestado_m50_bean.setOrden_ver(rs.getString("ORDEN_VER"));	
				vert_encuestado_m50_bean.setClave(rs.getString("CLAVE"));
				lstvertedero_enc_m50.add(vert_encuestado_m50_bean);
				
			}
	        
			
			for (int i = 0; i < lstvertedero.size(); i++)
            {
				V_vertedero_bean vertedero_bean   = (V_vertedero_bean)lstvertedero.get(i);
				int contador = 0;
				if(!vertedero_bean.getProvincia().equals("") || !vertedero_bean.getMunicipio().equals("")){
					  if (!vertedero_bean.getOrden_ver().equals(""))
                      {
							for (int j = 0; j < lstvertedero_nucleo.size(); j++)
				            {
								V_vertedero_nucleo_bean vertedero_nucleo_bean   = (V_vertedero_nucleo_bean)lstvertedero_nucleo.get(j);
								if(vertedero_nucleo_bean.getProvincia().equals(vertedero_bean.getProvincia()) &&
										vertedero_nucleo_bean.getMunicipio().equals(vertedero_bean.getMunicipio())){
									if(vertedero_nucleo_bean.getVer_codigo().equals(vertedero_bean.getOrden_ver()) &&
											vertedero_nucleo_bean.getClave().equals(vertedero_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							
							if (contador == 0){
								for (int j = 0; j < lstvertedero_enc.size(); j++)
					            {
									V_vert_encuestado_bean vert_encuestado_bean   = (V_vert_encuestado_bean)lstvertedero_enc.get(j);
									if(vert_encuestado_bean.getProvincia().equals(vertedero_bean.getProvincia()) &&
											vert_encuestado_bean.getMunicipio().equals(vertedero_bean.getMunicipio())){
										if(vert_encuestado_bean.getOrden_ver().equals(vertedero_bean.getOrden_ver()) &&
												vert_encuestado_bean.getClave().equals(vertedero_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}
							
							if (contador == 0){
								for (int j = 0; j < lstvertedero_enc_m50.size(); j++)
					            {
									V_vert_encuestado_m50_bean vert_encuestado_m50_bean   = (V_vert_encuestado_m50_bean)lstvertedero_enc_m50.get(j);
									if(vert_encuestado_m50_bean.getProvincia().equals(vertedero_bean.getProvincia()) &&
											vert_encuestado_m50_bean.getMunicipio().equals(vertedero_bean.getMunicipio())){
										if(vert_encuestado_m50_bean.getOrden_ver().equals(vertedero_bean.getOrden_ver()) &&
												vert_encuestado_m50_bean.getClave().equals(vertedero_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}	
							

							if (contador == 0){
								if (contTexto == 0)
								 {
									str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroO.V_01") + "\n");
									 contTexto++;
								 }
								 str.append(  vertedero_bean.getProvincia() + vertedero_bean.getMunicipio() + vertedero_bean.getOrden_ver()+ "\t");
							}
						  
                      }
					  else{
						  str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroO.falsoerror.V_2") + "\n");
					  }
						  
				}
				else{
					 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroO.falsoerror.V_1") + "\n");	
				}
			
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroO.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
