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
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_m50_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;

public class Validacion_cuadroN {
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroN") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;
			
			String sql = "select * from  v_DEPURADORA";
			String sql_1 = "SELECT * FROM v_DEP_AGUA_NUCLEO";
			String sql_2 = "SELECT * FROM v_DEPURADORA_ENC";
			String sql_3 = "SELECT * FROM v_DEPURADORA_ENC_2";
			String sql_4 = "SELECT * FROM v_DEPURADORA_ENC_M50 ";
			String sql_5 = "SELECT * FROM v_DEPURADORA_ENC_2_M50 ";
	    
			
	        ArrayList lstdepuradora = new ArrayList();
	    	ArrayList lstdep_agua_nucleo = new ArrayList();
	    	ArrayList lstdepuradora_enc = new ArrayList();
		    ArrayList lstdepuradora_enc_2 = new ArrayList();
			ArrayList lstdepuradora_enc_m50 = new ArrayList();
			ArrayList lstdepuradora_enc_2_m50 = new ArrayList();
			
			
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_bean depuradora_bean = new V_depuradora_bean();
				depuradora_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_bean.setMunicipio(rs.getString("MUNICIPIO"));	
				depuradora_bean.setClave(rs.getString("CLAVE"));
				depuradora_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				lstdepuradora.add((depuradora_bean));
			}
			
			
			ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_dep_agua_nucleo_bean dep_agua_nucleo_bean = new V_dep_agua_nucleo_bean();
				dep_agua_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				dep_agua_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				dep_agua_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				dep_agua_nucleo_bean.setNucleo(rs.getString("NUCLEO"));	
				dep_agua_nucleo_bean.setClave(rs.getString("CLAVE"));
				dep_agua_nucleo_bean.setDe_provinc(rs.getString("DE_PROVINC"));
				dep_agua_nucleo_bean.setDe_municip(rs.getString("DE_MUNICIP"));
				dep_agua_nucleo_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				lstdep_agua_nucleo.add((dep_agua_nucleo_bean));
			}

			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_bean depuradora_enc_bean = new V_depuradora_enc_bean();
				depuradora_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));

				lstdepuradora_enc.add(depuradora_enc_bean);
			
			
			
			
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_2_bean depuradora_enc_2_bean = new V_depuradora_enc_2_bean();

				depuradora_enc_2_bean.setClave(rs.getString("CLAVE"));
				depuradora_enc_2_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_2_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_2_bean.setTitular(rs.getString("TITULAR"));
				depuradora_enc_2_bean.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					depuradora_enc_2_bean.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					depuradora_enc_2_bean.setCapacidad(0);

				depuradora_enc_2_bean.setProblem_1(rs.getString("PROBLEM_1"));
				depuradora_enc_2_bean.setProblem_2(rs.getString("PROBLEM_2"));
				depuradora_enc_2_bean.setProblem_3(rs.getString("PROBLEM_3"));
				depuradora_enc_2_bean.setLodo_gest(rs.getString("LODO_GEST"));
				depuradora_enc_2_bean.setLodo_vert(new Integer(rs.getString("LODO_VERT")));
				depuradora_enc_2_bean.setLodo_inci(new Integer(rs.getString("LODO_INCI")));
				depuradora_enc_2_bean.setLodo_con_a(new Integer(rs.getString("LODO_CON_A")));
				depuradora_enc_2_bean.setLodo_sin_a(new Integer(rs.getString("LODO_SIN_A")));
				depuradora_enc_2_bean.setLodo_ot(new Integer(rs.getString("LODO_OT")));
				
				
				lstdepuradora_enc_2.add(depuradora_enc_2_bean);
			}
			
			
			ps = connection.prepareStatement(sql_4);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_m50_bean depuradora_enc_m50_bean = new V_depuradora_enc_m50_bean();
				depuradora_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_m50_bean.setOrden_depu(rs.getString("ORDEN_DEPU"));

				lstdepuradora_enc_m50.add(depuradora_enc_m50_bean);
				
			}
			
			
			ps = connection.prepareStatement(sql_5);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_depuradora_enc_2_m50_bean depuradora_enc_2_m50 = new V_depuradora_enc_2_m50_bean();

				depuradora_enc_2_m50.setClave(rs.getString("CLAVE"));
				depuradora_enc_2_m50.setProvincia(rs.getString("PROVINCIA"));
				depuradora_enc_2_m50.setMunicipio(rs.getString("MUNICIPIO"));
				depuradora_enc_2_m50.setOrden_depu(rs.getString("ORDEN_DEPU"));
				depuradora_enc_2_m50.setTitular(rs.getString("TITULAR"));
				depuradora_enc_2_m50.setGestion(rs.getString("GESTION"));
				if(rs.getString("CAPACIDAD")!=null&&!rs.getString("CAPACIDAD").equals(""))
					depuradora_enc_2_m50.setCapacidad(new Integer(rs.getString("CAPACIDAD")));
				else
					depuradora_enc_2_m50.setCapacidad(0);				depuradora_enc_2_m50.setProblem_1(rs.getString("PROBLEM_1"));
				depuradora_enc_2_m50.setProblem_2(rs.getString("PROBLEM_2"));
				depuradora_enc_2_m50.setProblem_3(rs.getString("PROBLEM_3"));
				depuradora_enc_2_m50.setLodo_gest(rs.getString("LODO_GEST"));
				depuradora_enc_2_m50.setLodo_vert(new Integer(rs.getString("LODO_VERT")));
				depuradora_enc_2_m50.setLodo_inci(new Integer(rs.getString("LODO_INCI")));
				depuradora_enc_2_m50.setLodo_con_a(new Integer(rs.getString("LODO_CON_A")));
				depuradora_enc_2_m50.setLodo_sin_a(new Integer(rs.getString("LODO_SIN_A")));
				depuradora_enc_2_m50.setLodo_ot(new Integer(rs.getString("LODO_OT")));
				
				lstdepuradora_enc_2_m50.add(depuradora_enc_2_m50);
				
			}
			
			
			
			
			
			
			
			
			
			
			
			
			for (int i = 0; i < lstdepuradora.size(); i++)
            {
				V_depuradora_bean depuradora_bean   = (V_depuradora_bean)lstdepuradora.get(i);
				int contador = 0;
				if(!depuradora_bean.getProvincia().equals("") || !depuradora_bean.getMunicipio().equals("")){
					  if (!depuradora_bean.getOrden_depu().equals(""))
                      {
							for (int j = 0; j < lstdep_agua_nucleo.size(); j++)
				            {
								V_dep_agua_nucleo_bean dep_agua_nucleo_bean  = (V_dep_agua_nucleo_bean)lstdep_agua_nucleo.get(j);
								if(dep_agua_nucleo_bean.getProvincia().equals(depuradora_bean.getProvincia()) &&
										dep_agua_nucleo_bean.getMunicipio().equals(depuradora_bean.getMunicipio())){
									if(dep_agua_nucleo_bean.getOrden_depu().equals(depuradora_bean.getOrden_depu()) &&
											dep_agua_nucleo_bean.getClave().equals(depuradora_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							
							if (contador == 0){
								for (int j = 0; j < lstdepuradora_enc.size(); j++)
					            {
									V_depuradora_enc_bean V_depuradora_enc_bean   = (V_depuradora_enc_bean)lstdepuradora_enc.get(j);
									if(V_depuradora_enc_bean.getProvincia().equals(depuradora_bean.getProvincia()) &&
											V_depuradora_enc_bean.getMunicipio().equals(depuradora_bean.getMunicipio())){
										if(V_depuradora_enc_bean.getOrden_depu().equals(depuradora_bean.getOrden_depu()) &&
												V_depuradora_enc_bean.getClave().equals(depuradora_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}
							
							if (contador == 0){
								for (int j = 0; j < lstdepuradora_enc_2.size(); j++)
					            {
									V_depuradora_enc_2_bean depuradora_enc_2_bean  = (V_depuradora_enc_2_bean)lstdepuradora_enc_2.get(j);
									if(depuradora_enc_2_bean.getProvincia().equals(depuradora_bean.getProvincia()) &&
											depuradora_enc_2_bean.getMunicipio().equals(depuradora_bean.getMunicipio())){
										if(depuradora_enc_2_bean.getOrden_depu().equals(depuradora_bean.getOrden_depu()) &&
												depuradora_enc_2_bean.getClave().equals(depuradora_bean.getClave())){
											contador ++;
										}
										
									}
					            }
							}	
							
							for (int j = 0; j < lstdepuradora_enc_m50.size(); j++)
				            {
								V_depuradora_enc_m50_bean depuradora_enc_m50_bean   = (V_depuradora_enc_m50_bean)lstdepuradora_enc_m50.get(j);
								if(depuradora_enc_m50_bean.getProvincia().equals(depuradora_bean.getProvincia()) &&
										depuradora_enc_m50_bean.getMunicipio().equals(depuradora_bean.getMunicipio())){
									if(depuradora_enc_m50_bean.getOrden_depu().equals(depuradora_bean.getOrden_depu()) &&
											depuradora_enc_m50_bean.getClave().equals(depuradora_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							
							for (int j = 0; j < lstdepuradora_enc_2_m50.size(); j++)
				            {
								V_depuradora_enc_2_m50_bean depuradora_enc_2_m50_bean   = (V_depuradora_enc_2_m50_bean)lstdepuradora_enc_2_m50.get(j);
								if(depuradora_enc_2_m50_bean.getProvincia().equals(depuradora_bean.getProvincia()) &&
										depuradora_enc_2_m50_bean.getMunicipio().equals(depuradora_bean.getMunicipio())){
									if(depuradora_enc_2_m50_bean.getOrden_depu().equals(depuradora_bean.getOrden_depu()) &&
											depuradora_enc_2_m50_bean.getClave().equals(depuradora_bean.getClave())){
										contador ++;
									}
									
								}
				            }
							

							if (contador == 0){
								if (contTexto == 0)
								 {
									str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroL.V_01") + "\n");
									 contTexto++;
								 }
								 str.append(  depuradora_bean.getProvincia() + depuradora_bean.getMunicipio() + depuradora_bean.getOrden_depu()+ "\t");
							}
						  
                      }
					  else{
						  str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroN.falsoerror.V_2") + "\n");
					  }
						  
				}
				else{

					 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadroN.falsoerror.V_1") + "\n");
				}
			
            }
			
			}
			 str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroN.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
