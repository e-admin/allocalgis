package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.utils.UtilsValidacion;

public class Validacion_cuadro02 {

	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		int contCabecera = 0;
	    int contTexto = 0;
	    boolean error = false;
		try
        {
			str.append(Messages.getString("cuadro02") + "\n");
			str.append("______________________________________________________________________\n\n");
			String sql = "select * from v_PLAN_URBANISTICO";
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			ArrayList lstPlanUrban = new ArrayList();
			while (rs.next()) {	
				
				V_plan_urbanistico_bean plan_urbanistico_bean = new V_plan_urbanistico_bean();

				plan_urbanistico_bean.setProvincia(rs.getString("PROVINCIA"));
				plan_urbanistico_bean.setMunicipio(rs.getString("MUNICIPIO"));
				plan_urbanistico_bean.setTipo_urba(rs.getString("TIPO_URBA"));
				plan_urbanistico_bean.setEstado_tra(rs.getString("ESTADO_TRA"));
				plan_urbanistico_bean.setDenominaci(rs.getString("DENOMINACI"));
				plan_urbanistico_bean.setSuperficie(new Double(rs.getString("SUPERFICIE")));
				plan_urbanistico_bean.setBo(LocalGISEIELUtils.formatFecha(rs.getDate("BO")));
				plan_urbanistico_bean.setUrban(new Double(rs.getString("URBAN")));
				plan_urbanistico_bean.setNo_urbable(new Double(rs.getString("NO_URBABLE")));
				plan_urbanistico_bean.setNourbable_(new Double(rs.getString("NOURBABLE_")));
				
				lstPlanUrban.add(plan_urbanistico_bean);
			}
			
			if (lstValCuadros.contains("v01")){
				//ERROR DEL MPT -> (V_01)
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					V_plan_urbanistico_bean planUrbanistico = (V_plan_urbanistico_bean)lstPlanUrban.get(i);
					ArrayList lst_provincia_municipio =new ArrayList();
					for (int j = 0; j < lstPlanUrban.size(); j++)
		            {
						V_plan_urbanistico_bean planUrban = (V_plan_urbanistico_bean)lstPlanUrban.get(j);
						
						if((planUrban.getTipo_urba().equals("P.S") || planUrban.getTipo_urba().equals("P.G") ||
								planUrban.getTipo_urba().equals("N.M") || planUrban.getTipo_urba().equals("D.S") ||
								planUrban.getTipo_urba().equals("D.C")) && planUrban.getEstado_tra().equals("AD") &&
								planUrban.getMunicipio().equals(planUrbanistico.getMunicipio())){
							
							lst_provincia_municipio.add(planUrban.getProvincia()+planUrban.getMunicipio());
							
						}
		            }
					
					if(lst_provincia_municipio.size() > 1){
						
						for (int h = 0; h <lst_provincia_municipio.size(); h++){
							 if (contTexto == 0)
			                 {
			                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_01") + "\n");
			                	 str.append("\n");
			                     contTexto++;
			                 }
			                 str.append(lst_provincia_municipio.get(h) + "\t");
			                 error = true;
						}
					}
					lst_provincia_municipio = new ArrayList();
	            }
				if (error)
					str.append("\n\n");
				error = false;
				 
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v02")){
				//ERROR DEL MPT -> (V_02)
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					double sumaSuelos = 0;
					V_plan_urbanistico_bean planUrbanistico = (V_plan_urbanistico_bean)lstPlanUrban.get(i);
					
					for (int j = 0; j < lstPlanUrban.size(); j++)
		            {
						V_plan_urbanistico_bean planUrban = (V_plan_urbanistico_bean)lstPlanUrban.get(j);
						
						if((planUrban.getTipo_urba().equals("P.S") || planUrban.getTipo_urba().equals("P.G") ||
								planUrban.getTipo_urba().equals("N.M") || planUrban.getTipo_urba().equals("D.S") ||
								planUrban.getTipo_urba().equals("D.C")) && planUrban.getEstado_tra().equals("AD") &&
								planUrban.getMunicipio().equals(planUrbanistico.getMunicipio())){
							 
							sumaSuelos = UtilsValidacion.roundTwoDecimals(planUrban.getUrban()) + UtilsValidacion.roundTwoDecimals(planUrban.getNo_urbable()) + 
								UtilsValidacion.roundTwoDecimals(planUrban.getNourbable_());

							if(UtilsValidacion.roundTwoDecimals(sumaSuelos) != UtilsValidacion.roundTwoDecimals(planUrban.getSuperficie()))
							{
								 if (contTexto == 0)
				                 {
				                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_02") + "\n");
				                	 str.append("\n");
				                     contTexto++;
				                 }
				                 str.append(planUrban.getProvincia()+planUrban.getMunicipio() + "\t");
				                 error = true;
							}
							
						}
		            }
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
				
			for (int i = 0; i < lstPlanUrban.size(); i++)
            {
				if(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("")){
					 if (contTexto == 0)
	                 {
	                	 str.append(Messages.getString("falsoErrorMPT") + " " +Messages.getString("cuadro01.falsoerror.V_01") + "\n");
	                	 str.append("\n");
	                     contTexto++;
	                 }
	                 str.append( ((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getProvincia() +
	                 			((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getMunicipio() + "\t");
	                 error = true;
				}
            }
			if (error)
				str.append("\n\n");
			error = false;
			contTexto = 0;
			
			if (lstValCuadros.contains("v03")){
				//ERROR DEL MPT -> (V_03) 
				ArrayList lstV_03 = new ArrayList();
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					V_plan_urbanistico_bean planUrbanistico = (V_plan_urbanistico_bean)lstPlanUrban.get(i);
					
					if(planUrbanistico.getEstado_tra().equals("AD") && !planUrbanistico.getTipo_urba().equals("N.P") &&
							planUrbanistico.getUrban() <= 0){
						lstV_03.add(planUrbanistico);
					}
	            }
				for(int i=0; i<lstV_03.size(); i++){
					 if (contTexto == 0)
	                 {
	                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_03") + "\n");
	                	 str.append("\n");
	                     contTexto++;
	                 }
	                 str.append(((V_plan_urbanistico_bean)lstV_03.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstV_03.get(i)).getMunicipio() + "\t");
	                 error = true;
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v041")){
				//ERROR DEL MPT -> (V_04) 
				ArrayList lstV_04 = new ArrayList();
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					V_plan_urbanistico_bean planUrbanistico = (V_plan_urbanistico_bean)lstPlanUrban.get(i);
					
					if(planUrbanistico.getTipo_urba().equals("P.E") &&
							planUrbanistico.getUrban() <= 0){
						lstV_04.add(planUrbanistico);
					}
	            }
				for(int i=0; i<lstV_04.size(); i++){
					if( ((V_plan_urbanistico_bean)lstV_04.get(i)).getNourbable_() <= 0){
						
						 if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_04") + "\n");
		                	 str.append("\n");
		                     contTexto++;
		                 }
		                 str.append(((V_plan_urbanistico_bean)lstV_04.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstV_04.get(i)).getMunicipio() + "\t");
		                 error = true;
					}
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v05")){
					
				//ERROR DEL MPT -> (V_05) 
				double sumaSuelo = 0;
				ArrayList lstV_05 = new ArrayList();
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					V_plan_urbanistico_bean planUrbanistico = (V_plan_urbanistico_bean)lstPlanUrban.get(i);
					
					if(planUrbanistico.getTipo_urba().equals("P.E")){
						lstV_05.add(planUrbanistico);
					}
	            }
				for(int i=0; i<lstV_05.size(); i++){
					
					sumaSuelo = UtilsValidacion.roundTwoDecimals(((V_plan_urbanistico_bean)lstV_05.get(i)).getNourbable_()) + 
						UtilsValidacion.roundTwoDecimals(((V_plan_urbanistico_bean)lstV_05.get(i)).getNo_urbable());
					if(UtilsValidacion.roundTwoDecimals(sumaSuelo) !=  
							UtilsValidacion.roundTwoDecimals(((V_plan_urbanistico_bean)lstV_05.get(i)).getSuperficie())){
						
						 if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_05") + "\n");
		                	 str.append("\n");
		                     contTexto++;
		                 }
		                 str.append(((V_plan_urbanistico_bean)lstV_05.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstV_05.get(i)).getMunicipio() + "\t");
		                 error = true;
					}
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v06")){
	
				//ERROR DEL MPT -> (V_06) 
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					if(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getSuperficie() <= 0){
	
						 if (contTexto == 0)
		                 {
		                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_06") + "\n");
		                	 str.append("\n");
		                     contTexto++;
		                 }
		                 str.append(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getMunicipio() + "\t");
		                 error = true;
					}
	            }
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v07")){
				//ERROR DEL MPT -> (V_07) 
				
				String sqlV7 = "select distinct(BO) from  v_PLAN_URBANISTICO where tipo_urba='N.P' and estado_tra='AD' ";  
				ps = connection.prepareStatement(sqlV7);
				rs = ps.executeQuery();
				ArrayList lstPlanUrbanBOV7 = new ArrayList();
				while (rs.next()) {	
					V_plan_urbanistico_bean planUrban = new V_plan_urbanistico_bean();	
					planUrban.setBo(LocalGISEIELUtils.formatFecha(rs.getDate("BO")));			
					lstPlanUrbanBOV7.add(planUrban);
				}
				
				ArrayList lstPlanUrbanV7 = new ArrayList();
				for(int i = 0; i < lstPlanUrban.size(); i++){
					
					if(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("N.P") && 
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getEstado_tra().equals("AD")){
						lstPlanUrbanV7.add(lstPlanUrban.get(i));
					}
				}
				
				for(int i = 0; i < lstPlanUrbanBOV7.size(); i++){
					
					for(int j = 0; j < lstPlanUrbanV7.size(); j++){
						
						if(((V_plan_urbanistico_bean)lstPlanUrbanV7.get(j)).getBo().toString().
								equals(((V_plan_urbanistico_bean)lstPlanUrbanBOV7.get(i)).getBo().toString())){
							if (contTexto == 0)
			                 {
			                	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_07") + "\n");
			                	 str.append("\n");
			                     contTexto++;
			                 }
			                 str.append(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getMunicipio() + "\t");
			                 error = true;
						}
					}
				}
				if (error)
					str.append("\n\n");
				error = false;
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v08")){
				//ERROR DEL MPT -> (V_08) 
				ArrayList lstTabla1 = new ArrayList();
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					CodIne_bean codIne_bean = new CodIne_bean();
					codIne_bean.setProvincia(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getProvincia());
					codIne_bean.setMunicipio(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getMunicipio());
					
					lstTabla1.add(codIne_bean);
	            }
				contTexto = FuncionesComunes.ValidaExistenciaCODMUN(connection, lstTabla1, "v_NUCL_ENCUESTADO_1", "cuadro02.V_08", str);
				if (contTexto != 0)
					str.append("\n\n");
				contTexto = 0;
			}
			
			if (lstValCuadros.contains("v09")){
				//ERROR DEL MPT -> (V_09) 
				ArrayList lstV09 = new ArrayList();;
				for (int i = 0; i < lstPlanUrban.size(); i++)
	            {
					if((((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("P.S") || 
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("P.G") ||
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("N.M") ||
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("D.S") ||
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("D.C") ||
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getTipo_urba().equals("N.P")) &&
							((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getEstado_tra().equals("AD")){
						
						lstV09.add(lstPlanUrban.get(i));
					}
					
	            }
				for (int i = 0; i < lstV09.size(); i++){
					if(((V_plan_urbanistico_bean)lstV09.get(i)).getBo() == null ){
						if (contTexto == 0)
		                {
		               	 str.append(Messages.getString("errorMPT") + " " +Messages.getString("cuadro02.V_09") + "\n");
		               	 str.append("\n");
		                    contTexto++;
		                }
		                str.append(((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getProvincia()+((V_plan_urbanistico_bean)lstPlanUrban.get(i)).getMunicipio() + "\t");
					}
				}
			}
			 str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_cuadro02.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(rs, ps, null);
        }
	}
}
