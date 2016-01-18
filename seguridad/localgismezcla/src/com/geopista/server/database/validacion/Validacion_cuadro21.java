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
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;

public class Validacion_cuadro21 {
	
	
	public static void validacion(Connection connection, StringBuffer str, int fase, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean error = false;
	    int contTexto = 0;
	    
		try
        {       
			str.append(Messages.getString("cuadro21") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			
			String sql = "select * from v_NUCL_ENCUESTADO_2";
		
			ArrayList lstnucl_encuestado_2 = new ArrayList();
			
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_2_bean  nucl_encuestado_2_bean = new V_nucl_encuestado_2_bean();

				nucl_encuestado_2_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_2_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_2_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_2_bean.setAag_caudal(rs.getString("AAG_CAUDAL"));
				nucl_encuestado_2_bean.setAag_restri(rs.getString("AAG_RESTRI"));
				nucl_encuestado_2_bean.setAag_contad(rs.getString("AAG_CONTAD"));
				nucl_encuestado_2_bean.setAag_tasa(rs.getString("AAG_TASA"));
				nucl_encuestado_2_bean.setAag_instal(rs.getString("AAG_INSTAL"));
				nucl_encuestado_2_bean.setAag_hidran(rs.getString("AAG_HIDRAN"));
				nucl_encuestado_2_bean.setAag_est_hi(rs.getString("AAG_EST_HI"));
				nucl_encuestado_2_bean.setAag_valvul(rs.getString("AAG_VALVUL"));
				nucl_encuestado_2_bean.setAag_est_va(rs.getString("AAG_EST_VA"));
				nucl_encuestado_2_bean.setAag_bocasr(rs.getString("AAG_BOCASR"));
				nucl_encuestado_2_bean.setAag_est_bo(rs.getString("AAG_EST_BO"));
				nucl_encuestado_2_bean.setCisterna(rs.getString("CISTERNA"));
				
				lstnucl_encuestado_2.add(nucl_encuestado_2_bean);
				
			}
				
			
			
			ArrayList lstnucl_encuestado_2_v01 = new ArrayList();
			String sql_v01 = "select * from v_NUCL_ENCUESTADO_2 where AAG_CAUDAL<>'NO'";
			ps = connection.prepareStatement(sql_v01);
			rs = ps.executeQuery();
			while (rs.next()) {	
				V_nucl_encuestado_2_bean  nucl_encuestado_2_bean = new V_nucl_encuestado_2_bean();

				nucl_encuestado_2_bean.setProvincia(rs.getString("PROVINCIA"));
				nucl_encuestado_2_bean.setMunicipio(rs.getString("MUNICIPIO"));
				nucl_encuestado_2_bean.setEntidad(rs.getString("ENTIDAD"));
				nucl_encuestado_2_bean.setNucleo(rs.getString("NUCLEO"));
				nucl_encuestado_2_bean.setAag_caudal(rs.getString("AAG_CAUDAL"));
				nucl_encuestado_2_bean.setAag_restri(rs.getString("AAG_RESTRI"));
				nucl_encuestado_2_bean.setAag_contad(rs.getString("AAG_CONTAD"));
				nucl_encuestado_2_bean.setAag_tasa(rs.getString("AAG_TASA"));
				nucl_encuestado_2_bean.setAag_instal(rs.getString("AAG_INSTAL"));
				nucl_encuestado_2_bean.setAag_hidran(rs.getString("AAG_HIDRAN"));
				nucl_encuestado_2_bean.setAag_est_hi(rs.getString("AAG_EST_HI"));
				nucl_encuestado_2_bean.setAag_valvul(rs.getString("AAG_VALVUL"));
				nucl_encuestado_2_bean.setAag_est_va(rs.getString("AAG_EST_VA"));
				nucl_encuestado_2_bean.setAag_bocasr(rs.getString("AAG_BOCASR"));
				nucl_encuestado_2_bean.setAag_est_bo(rs.getString("AAG_EST_BO"));
				nucl_encuestado_2_bean.setCisterna(rs.getString("CISTERNA"));
				lstnucl_encuestado_2_v01.add(nucl_encuestado_2_bean);
			}
				
			ArrayList lstTabla1 = new ArrayList();
			for (int i = 0; i < lstnucl_encuestado_2_v01.size(); i++)
            {
				CodIne_bean codIne_bean = new CodIne_bean();
				
				codIne_bean.setProvincia(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2_v01.get(i)).getProvincia());
				codIne_bean.setMunicipio(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2_v01.get(i)).getMunicipio());
				codIne_bean.setEntidad(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2_v01.get(i)).getEntidad());
				codIne_bean.setNucleo(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2_v01.get(i)).getNucleo());
				
				lstTabla1.add(codIne_bean);
            }
			
			if(lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01) 
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_CAP_AGUA_NUCLEO", "cuadro21.V_01", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v02")){
				//ERROR DEL MPT -> (V_02) 
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_RED_DISTRIBUCION", "cuadro21.V_02", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}

			if(lstValCuadros.contains("v03")){	
				//ERROR DEL MPT -> (V_03) 
				for (int i = 0; i < lstnucl_encuestado_2_v01.size(); i++)
	            {
					V_nucl_encuestado_2_bean nucl_encuestado_2_bean   = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2_v01.get(i);
	
					if (!(new Integer(nucl_encuestado_2_bean.getAag_instal()) > 1900 && 
							new Integer(nucl_encuestado_2_bean.getAag_instal()) < fase)){ 
						if (contTexto == 0)
						 
						 {
							 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro21.V_03") + "\n");
							 str.append("\n");
							 contTexto++;
						 }
						 str.append(  nucl_encuestado_2_bean.getProvincia() + nucl_encuestado_2_bean.getMunicipio() +
								 nucl_encuestado_2_bean.getEntidad() + nucl_encuestado_2_bean.getNucleo() + "\t");
						 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v04")){
				//ERROR DEL MPT -> (V_04) 
				for (int i = 0; i < lstnucl_encuestado_2.size(); i++)
	            {
					V_nucl_encuestado_2_bean nucl_encuestado_2_bean   = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i);
					if(new Integer (nucl_encuestado_2_bean.getAag_instal()) == 0){
						if(!(nucl_encuestado_2_bean.getAag_caudal().equals("NO") &&
								nucl_encuestado_2_bean.getAag_contad().equals("NO") &&
								nucl_encuestado_2_bean.getAag_tasa().equals("NO") &&
								nucl_encuestado_2_bean.getAag_restri().equals("NO") &&
								nucl_encuestado_2_bean.getAag_hidran().equals("NO") &&
								nucl_encuestado_2_bean.getAag_valvul().equals("NO") &&
								nucl_encuestado_2_bean.getAag_bocasr().equals("NO"))){
							
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro21.V_04") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(  nucl_encuestado_2_bean.getProvincia() + nucl_encuestado_2_bean.getMunicipio() +
									 nucl_encuestado_2_bean.getEntidad() + nucl_encuestado_2_bean.getNucleo() + "\t");
							 error = true;
						}	
					}				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v05")){
				//ERROR DEL MPT -> (V_05)
				for (int i = 0; i < lstnucl_encuestado_2.size(); i++)
	            {
					V_nucl_encuestado_2_bean nucl_encuestado_2_bean   = (V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i);
					if(nucl_encuestado_2_bean.getAag_restri().equals("RT") ||
							nucl_encuestado_2_bean.getAag_restri().equals("RF") ||
							nucl_encuestado_2_bean.getAag_restri().equals("RM")){
						
						if(!nucl_encuestado_2_bean.getAag_caudal().equals("IN")){
							
							if (contTexto == 0)
							 {
								 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro21.V_05") + "\n");
								 str.append("\n");
								 contTexto++;
							 }
							 str.append(  nucl_encuestado_2_bean.getProvincia() + nucl_encuestado_2_bean.getMunicipio() +
									 nucl_encuestado_2_bean.getEntidad() + nucl_encuestado_2_bean.getNucleo() + "\t");
							 error = true;
						}	
					}				
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if(lstValCuadros.contains("v06")){
			//ERROR DEL MPT -> (V_06)
				lstTabla1 = new ArrayList();
				for (int i = 0; i < lstnucl_encuestado_2.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					
					codIne_bean.setProvincia(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i)).getMunicipio());
					codIne_bean.setEntidad(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i)).getEntidad());
					codIne_bean.setNucleo(((V_nucl_encuestado_2_bean)lstnucl_encuestado_2.get(i)).getNucleo());
					
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODINE(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro21.V_06", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro21.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
