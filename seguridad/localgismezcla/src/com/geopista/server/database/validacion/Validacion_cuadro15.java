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
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
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

public class Validacion_cuadro15 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {
			str.append(Messages.getString("cuadro15") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_DEPOSITO_ENC";
			String sql_filtro = "select * from v_DEPOSITO_ENC where CAPACIDAD = 0 and ESTADO <> 'E'";
			String sql_1 = "select * from v_DEPOSITO_AGUA_NUCLEO";
	        
			ArrayList lstdeposito_enc = new ArrayList();
			ArrayList lstdeposito_enc_filtro = new ArrayList();
			ArrayList lstdeposito_agua_nucleo = new ArrayList();
			
			ps = connection.prepareStatement(sql);
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
	
				lstdeposito_enc.add(deposito_enc_bean);
				
			}
			
			ps = connection.prepareStatement(sql_filtro);
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
	
				lstdeposito_enc_filtro.add(deposito_enc_bean);
				
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
			
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstdeposito_enc.size(); i++)
	            {
					int count = 0;
					V_deposito_enc_bean  deposito_enc_bean  = (V_deposito_enc_bean)lstdeposito_enc.get(i);
					for (int j = 0; j < lstdeposito_agua_nucleo.size(); j++)
		            {
						V_deposito_agua_nucleo_bean  deposito_agua_nucleo_bean = (V_deposito_agua_nucleo_bean)lstdeposito_agua_nucleo.get(j);
						
						if( deposito_agua_nucleo_bean.getDe_provinc().equals(deposito_enc_bean.getProvincia()) &&
								deposito_agua_nucleo_bean.getDe_municip().equals(deposito_enc_bean.getMunicipio()) &&
								deposito_agua_nucleo_bean.getOrden_depo().equals(deposito_enc_bean.getOrden_depo()) ){
							
							count ++;
						}
		            }
					
					if (count == 0){
											
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro15.V_01") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(deposito_enc_bean.getClave() + deposito_enc_bean.getProvincia()+ 
								 deposito_enc_bean.getMunicipio() + deposito_enc_bean.getOrden_depo() +"\t");
						 error = true;
					}
	
				}
	
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			if(lstValCuadros.contains("v02")){
				//ERROR DEL MPT -> (V_02)
				for (int i = 0; i < lstdeposito_enc_filtro.size(); i++)
	            {
					V_deposito_enc_bean  deposito_enc_bean  = (V_deposito_enc_bean)lstdeposito_enc_filtro.get(i);
					if (contTexto == 0)
					 {
						 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro15.V_02") + "\n");
						 str.append("\n");
						 contTexto++;
					 }
					 str.append(deposito_enc_bean.getClave() + deposito_enc_bean.getProvincia()+ 
							 deposito_enc_bean.getMunicipio() + deposito_enc_bean.getOrden_depo() +"\t");
	
				}
			}
			str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro15.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
