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
import com.geopista.server.database.validacion.beans.V_nivel_ensenanza_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_tra_potabilizacion_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadroK {
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
        { 
			str.append(Messages.getString("cuadroK") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			int contTexto = 0;

	        
			String sql = "select * from  v_TRA_POTABILIZACION";
			String sql_1 = "SELECT * FROM v_TRAT_POTA_NUCLEO";
			String sql_2 = "SELECT * FROM v_POTABILIZACION_ENC";
			String sql_3 = "SELECT * FROM v_POTABILIZACION_ENC_M50";
		        
			
	        ArrayList lsttrat_potabilizacion = new ArrayList();
	    	ArrayList lsttrat_pota_nucleo = new ArrayList();
			ArrayList lstpotabilizacion_enc = new ArrayList();
			ArrayList lstpotabilizacion_enc_m50 = new ArrayList();
			
			
			 ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_tra_potabilizacion_bean tra_potabilizacion_bean = new V_tra_potabilizacion_bean();
				tra_potabilizacion_bean.setProvincia(rs.getString("PROVINCIA"));
				tra_potabilizacion_bean.setMunicipio(rs.getString("MUNICIPIO"));
				tra_potabilizacion_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				tra_potabilizacion_bean.setClave(rs.getString("CLAVE"));
				
				lsttrat_potabilizacion.add(tra_potabilizacion_bean);
				
			}
				
	        ps = connection.prepareStatement(sql_1);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_trat_pota_nucleo_bean trat_pota_nucleo_bean = new V_trat_pota_nucleo_bean();

				trat_pota_nucleo_bean.setClave(rs.getString("CLAVE"));
				trat_pota_nucleo_bean.setProvincia(rs.getString("PROVINCIA"));
				trat_pota_nucleo_bean.setMunicipio(rs.getString("MUNICIPIO"));
				trat_pota_nucleo_bean.setEntidad(rs.getString("ENTIDAD"));
				trat_pota_nucleo_bean.setNucleo(rs.getString("NUCLEO"));
				trat_pota_nucleo_bean.setPo_provin(rs.getString("PO_PROVIN"));
				trat_pota_nucleo_bean.setPo_munipi(rs.getString("PO_MUNIPI"));
				trat_pota_nucleo_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));

				lsttrat_pota_nucleo.add(trat_pota_nucleo_bean);
				
			}
	        
			ps = connection.prepareStatement(sql_2);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_bean  potabilizacion_enc_bean = new V_potabilizacion_enc_bean();

				potabilizacion_enc_bean.setClave(rs.getString("CLAVE"));
				potabilizacion_enc_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_bean.setMunicipio(rs.getString("MUNICIPIO"));
				potabilizacion_enc_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				potabilizacion_enc_bean.setTipo_tra(rs.getString("TIPO_TRA"));
				potabilizacion_enc_bean.setUbicacion(rs.getString("UBICACION"));
				potabilizacion_enc_bean.setS_desinf(rs.getString("S_DESINF"));
				potabilizacion_enc_bean.setCat_a1(rs.getString("CAT_A1"));
				potabilizacion_enc_bean.setCat_a2(rs.getString("CAT_A2"));
				potabilizacion_enc_bean.setCat_a3(rs.getString("CAT_A3"));
				potabilizacion_enc_bean.setDesaladora(rs.getString("DESALADORA"));
				potabilizacion_enc_bean.setOtros(rs.getString("OTROS"));
				potabilizacion_enc_bean.setDesinf_1(rs.getString("DESINF_1"));
				potabilizacion_enc_bean.setDesinf_2(rs.getString("DESINF_2"));
				potabilizacion_enc_bean.setDesinf_3(rs.getString("DESINF_3"));
				potabilizacion_enc_bean.setPeriodicid(rs.getString("PERIODICID"));
				potabilizacion_enc_bean.setOrganismo(rs.getString("ORGANISMO"));
				potabilizacion_enc_bean.setEstado(rs.getString("ESTADO"));
				
				lstpotabilizacion_enc.add(potabilizacion_enc_bean);
				
			}
			
	        
			ps = connection.prepareStatement(sql_3);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_potabilizacion_enc_m50_bean  potabilizacion_enc_m50_bean = new V_potabilizacion_enc_m50_bean();

				potabilizacion_enc_m50_bean.setClave(rs.getString("CLAVE"));
				potabilizacion_enc_m50_bean.setProvincia(rs.getString("PROVINCIA"));
				potabilizacion_enc_m50_bean.setMunicipio(rs.getString("MUNICIPIO"));
				potabilizacion_enc_m50_bean.setOrden_trat(rs.getString("ORDEN_TRAT"));
				potabilizacion_enc_m50_bean.setTipo_tra(rs.getString("TIPO_TRA"));
				potabilizacion_enc_m50_bean.setUbicacion(rs.getString("UBICACION"));
				potabilizacion_enc_m50_bean.setS_desinf(rs.getString("S_DESINF"));
				potabilizacion_enc_m50_bean.setCat_a1(rs.getString("CAT_A1"));
				potabilizacion_enc_m50_bean.setCat_a2(rs.getString("CAT_A2"));
				potabilizacion_enc_m50_bean.setCat_a3(rs.getString("CAT_A3"));
				potabilizacion_enc_m50_bean.setDesaladora(rs.getString("DESALADORA"));
				potabilizacion_enc_m50_bean.setOtros(rs.getString("OTROS"));
				potabilizacion_enc_m50_bean.setDesinf_1(rs.getString("DESINF_1"));
				potabilizacion_enc_m50_bean.setDesinf_2(rs.getString("DESINF_2"));
				potabilizacion_enc_m50_bean.setDesinf_3(rs.getString("DESINF_3"));
				potabilizacion_enc_m50_bean.setPeriodicid(rs.getString("PERIODICID"));
				potabilizacion_enc_m50_bean.setOrganismo(rs.getString("ORGANISMO"));
				potabilizacion_enc_m50_bean.setEstado(rs.getString("ESTADO"));
				lstpotabilizacion_enc_m50.add(potabilizacion_enc_m50_bean);
				
			}
	        
			
			for (int i = 0; i < lsttrat_potabilizacion.size(); i++)
            {
				V_tra_potabilizacion_bean tra_potabilizacion_bean   = (V_tra_potabilizacion_bean)lsttrat_potabilizacion.get(i);
				int contador = 0;
				for (int j = 0; j < lsttrat_pota_nucleo.size(); j++)
	            {
					V_trat_pota_nucleo_bean trat_pota_nucleo_bean    = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(j);
					if(trat_pota_nucleo_bean.getProvincia().equals(tra_potabilizacion_bean.getProvincia()) &&
							trat_pota_nucleo_bean.getMunicipio().equals(tra_potabilizacion_bean.getMunicipio())){
						if(trat_pota_nucleo_bean.getOrden_trat().equals(tra_potabilizacion_bean.getOrden_trat()) &&
								trat_pota_nucleo_bean.getClave().equals(tra_potabilizacion_bean.getClave())){
							contador ++;
						}
						
					}
	            }
				
				if (contador == 0){
					for (int j = 0; j < lstpotabilizacion_enc.size(); j++)
		            {
						V_potabilizacion_enc_bean potabilizacion_enc_bean   = (V_potabilizacion_enc_bean)lstpotabilizacion_enc.get(j);
						if(potabilizacion_enc_bean.getProvincia().equals(tra_potabilizacion_bean.getProvincia()) &&
								potabilizacion_enc_bean.getMunicipio().equals(tra_potabilizacion_bean.getMunicipio())){
							if(potabilizacion_enc_bean.getOrden_trat().equals(tra_potabilizacion_bean.getOrden_trat()) &&
									potabilizacion_enc_bean.getClave().equals(tra_potabilizacion_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}
				
				if (contador == 0){
					for (int j = 0; j < lstpotabilizacion_enc_m50.size(); j++)
		            {
						V_potabilizacion_enc_m50_bean   potabilizacion_enc_m50_bean = (V_potabilizacion_enc_m50_bean)lstpotabilizacion_enc_m50.get(j);
							if(potabilizacion_enc_m50_bean.getProvincia().equals(tra_potabilizacion_bean.getProvincia()) &&
								potabilizacion_enc_m50_bean.getMunicipio().equals(tra_potabilizacion_bean.getMunicipio())){
							if(potabilizacion_enc_m50_bean.getOrden_trat().equals(tra_potabilizacion_bean.getOrden_trat()) &&
									potabilizacion_enc_m50_bean.getClave().equals(tra_potabilizacion_bean.getClave())){
								contador ++;
							}
							
						}
		            }
				}	
				

				if (contador == 0){
					if (contTexto == 0)
					 {
						str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadroK.V_01") + "\n");
						 contTexto++;
					 }
					 str.append(  tra_potabilizacion_bean.getProvincia() + tra_potabilizacion_bean.getMunicipio() + tra_potabilizacion_bean.getOrden_trat()+ "\t");
				}
	
            }
			 str.append("\n\n");
			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadroK.class + " " + e.getMessage());
        	 str.append("\n\n");
        }    
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
