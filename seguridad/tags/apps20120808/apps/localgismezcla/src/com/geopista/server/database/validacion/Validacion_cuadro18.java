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
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro18 {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {

			str.append(Messages.getString("cuadro18") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "select * from v_POTABILIZACION_ENC";
			String sql_filtro = "select * from v_POTABILIZACION_ENC " +
					"	where S_DESINF<>'SI' AND  CAT_A1<>'SI' AND CAT_A2<>'SI' AND CAT_A3<>'SI'  AND  DESALADORA<>'SI'  AND  OTROS<>'SI' ";
	        String sql_1 = "select * from v_TRAT_POTA_NUCLEO";

	        
	    	ArrayList lstpotabilizacion_enc = new ArrayList();
	    	ArrayList lstpotabilizacion_enc_filtro = new ArrayList();
			ArrayList lsttrat_pota_nucleo = new ArrayList();

			
			ps = connection.prepareStatement(sql);
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
			
			ps = connection.prepareStatement(sql_filtro);
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
				
				
				lstpotabilizacion_enc_filtro.add(potabilizacion_enc_bean);
				
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
			
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstpotabilizacion_enc.size(); i++)
	            {
					int count = 0;
	                
	                V_potabilizacion_enc_bean potabilizacion_enc_bean  = (V_potabilizacion_enc_bean)lstpotabilizacion_enc.get(i);
					for (int j = 0; j < lsttrat_pota_nucleo.size(); j++)
		            {
						V_trat_pota_nucleo_bean trat_pota_nucleo_bean = (V_trat_pota_nucleo_bean)lsttrat_pota_nucleo.get(j);
						
						if( trat_pota_nucleo_bean.getPo_provin().equals(potabilizacion_enc_bean.getProvincia()) &&
								trat_pota_nucleo_bean.getPo_munipi().equals(potabilizacion_enc_bean.getMunicipio()) &&
								trat_pota_nucleo_bean.getOrden_trat().equals(potabilizacion_enc_bean.getOrden_trat())){
							
							count ++;
						}
		            }
					
			
					if (count == 0){
									
						
						if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro18.V_01") + "\n");
							 contTexto++;
						 }
						 str.append( potabilizacion_enc_bean.getClave() + potabilizacion_enc_bean.getProvincia() + 
								 potabilizacion_enc_bean.getMunicipio() + potabilizacion_enc_bean.getOrden_trat() + "\t");
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
				for (int i = 0; i < lstpotabilizacion_enc_filtro.size(); i++)
	            {
					V_potabilizacion_enc_bean potabilizacion_enc_bean = (V_potabilizacion_enc_bean)lstpotabilizacion_enc_filtro.get(i);
					
					if (contTexto == 0)
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro18.V_02") + "\n");
							 contTexto++;
						 }
						 str.append( potabilizacion_enc_bean.getClave() + potabilizacion_enc_bean.getProvincia()+ 
								 potabilizacion_enc_bean.getMunicipio() +  potabilizacion_enc_bean.getOrden_trat() +"\t");
	
				}
			}
			str.append("\n\n");

			
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro18.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
