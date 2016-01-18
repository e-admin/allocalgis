package com.geopista.server.database.validacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import com.geopista.server.database.COperacionesEIEL;
import com.geopista.server.database.validacion.beans.CodIne_bean;
import com.geopista.server.database.validacion.beans.V_alumbrado_bean;
import com.geopista.server.database.validacion.beans.V_cap_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_captacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_bean;
import com.geopista.server.database.validacion.beans.V_colector_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_colector_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_cond_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_bean;
import com.geopista.server.database.validacion.beans.V_conduccion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_dep_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_agua_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_bean;
import com.geopista.server.database.validacion.beans.V_deposito_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_2_m50_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_bean;
import com.geopista.server.database.validacion.beans.V_depuradora_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_bean;
import com.geopista.server.database.validacion.beans.V_emisario_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_emisario_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_infraestr_viaria_bean;
import com.geopista.server.database.validacion.beans.V_inst_depor_deporte_bean;
import com.geopista.server.database.validacion.beans.V_instal_deportiva_bean;
import com.geopista.server.database.validacion.beans.V_mun_enc_dis_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_1_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_2_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_3_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_4_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_5_bean;
import com.geopista.server.database.validacion.beans.V_nucl_encuestado_7_bean;
import com.geopista.server.database.validacion.beans.V_ot_serv_municipal_bean;
import com.geopista.server.database.validacion.beans.V_padron_bean;
import com.geopista.server.database.validacion.beans.V_plan_urbanistico_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_bean;
import com.geopista.server.database.validacion.beans.V_potabilizacion_enc_m50_bean;
import com.geopista.server.database.validacion.beans.V_ramal_saneamiento_bean;
import com.geopista.server.database.validacion.beans.V_red_distribucion_bean;
import com.geopista.server.database.validacion.beans.V_sanea_autonomo_bean;
import com.geopista.server.database.validacion.beans.V_tramo_carretera_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_bean;
import com.geopista.server.database.validacion.beans.V_tramo_colector_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_bean;
import com.geopista.server.database.validacion.beans.V_tramo_conduccion_m50_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_bean;
import com.geopista.server.database.validacion.beans.V_tramo_emisario_m50_bean;
import com.geopista.server.database.validacion.beans.V_trat_pota_nucleo_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_bean;
import com.geopista.server.database.validacion.beans.V_vert_encuestado_m50_bean;
import com.geopista.server.database.validacion.beans.V_vertedero_nucleo_bean;

public class Validacion_general {
	
	
	public static void validacion(Connection connection, StringBuffer str, ArrayList lstValCuadros){
		
		PreparedStatement ps = null;

		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		boolean error = false;
	    int contTexto = 0;
	    
	    ArrayList lstVistas = new ArrayList ();
	    lstVistas.add("v_PROVINCIA");
	    lstVistas.add("v_MUNICIPIO");
		lstVistas.add("v_NUCLEO_POBLACION");
		lstVistas.add("v_CAPTACION_AGUA");
		lstVistas.add("v_DEPOSITO");
		lstVistas.add("v_RED_DISTRIBUCION");
		lstVistas.add("v_CONDUCCION");
		lstVistas.add("v_CONDUCCION_ENC");
		lstVistas.add("v_CONDUCCION_ENC_M50");
		lstVistas.add("v_TRAMO_CONDUCCION");
		lstVistas.add("v_TRAMO_CONDUCCION_M50");
		lstVistas.add("v_NUCL_ENCUESTADO_4");
		lstVistas.add("v_CAPTACION_ENC");
		lstVistas.add("v_CAPTACION_ENC_M50");
		lstVistas.add("v_DEPOSITO_ENC");
		lstVistas.add("v_DEPOSITO_ENC_M50");
		lstVistas.add("v_NUCL_ENCUESTADO_3");
		lstVistas.add("v_TRA_POTABILIZACION");
		lstVistas.add("v_POTABILIZACION_ENC");
		lstVistas.add("v_POTABILIZACION_ENC_M50");
		lstVistas.add("v_CAP_AGUA_NUCLEO");
		lstVistas.add("v_DEPOSITO_AGUA_NUCLEO");
		lstVistas.add("v_TRAT_POTA_NUCLEO");
		lstVistas.add("v_COND_AGUA_NUCLEO");
		lstVistas.add("v_DEPURADORA");
		lstVistas.add("v_EMISARIO");
		lstVistas.add("v_EMISARIO_ENC");
		lstVistas.add("v_EMISARIO_ENC_M50");
		lstVistas.add("v_TRAMO_EMISARIO");
		lstVistas.add("v_TRAMO_EMISARIO_M50");
		lstVistas.add("v_COLECTOR");
		lstVistas.add("v_COLECTOR_ENC");
		lstVistas.add("v_COLECTOR_ENC_M50");
		lstVistas.add("v_TRAMO_COLECTOR");
	    lstVistas.add("v_TRAMO_COLECTOR_M50");
	    lstVistas.add("v_RAMAL_SANEAMIENTO");
	    lstVistas.add("v_DEPURADORA_ENC");
	    lstVistas.add("v_DEPURADORA_ENC_M50");
	    lstVistas.add("v_DEPURADORA_ENC_2");
	    lstVistas.add("v_DEPURADORA_ENC_2_M50");
	    lstVistas.add("v_SANEA_AUTONOMO");
	    lstVistas.add("v_NUCL_ENCUESTADO_5");
	    lstVistas.add("v_DEP_AGUA_NUCLEO");
	    lstVistas.add("v_COLECTOR_NUCLEO");
	    lstVistas.add("v_EMISARIO_NUCLEO");
	    lstVistas.add("v_CENTRO_ASISTENCIAL");
	    lstVistas.add("v_CASA_CONSISTORIAL");
	    lstVistas.add("v_CASA_CON_USO");
	    lstVistas.add("v_CEMENTERIO");
	    lstVistas.add("v_CENT_CULTURAL");
	    lstVistas.add("v_CENT_CULTURAL_USOS");
	    lstVistas.add("v_CENTRO_ENSENANZA");
	    lstVistas.add("v_NIVEL_ENSENANZA");
	    lstVistas.add("v_INSTAL_DEPORTIVA");
	    lstVistas.add("v_INST_DEPOR_DEPORTE");
	    lstVistas.add("v_PROTECCION_CIVIL");
	    lstVistas.add("v_LONJA_MERC_FERIA");
	    lstVistas.add("v_MATADERO");
	    lstVistas.add("v_PARQUE");
	    lstVistas.add("v_CENTRO_SANITARIO");
	    lstVistas.add("v_EDIFIC_PUB_SIN_USO");
	    lstVistas.add("v_TANATORIO");
	    lstVistas.add("v_INFRAESTR_VIARIA");
	    lstVistas.add("v_CARRETERA");
	    lstVistas.add("v_TRAMO_CARRETERA");
	    lstVistas.add("v_ALUMBRADO");
	    lstVistas.add("v_VERTEDERO");
	    lstVistas.add("v_VERT_ENCUESTADO");
	    lstVistas.add("v_VERT_ENCUESTADO_M50");
	    lstVistas.add("v_VERTEDERO_NUCLEO");
	    lstVistas.add("v_RECOGIDA_BASURA");
	    lstVistas.add("v_NUCL_ENCUESTADO_6");
	    lstVistas.add("v_NUCL_ENCUESTADO_1");
	    lstVistas.add("v_NUCL_ENCUESTADO_2");
	    lstVistas.add("v_PADRON");
	    lstVistas.add("v_NUCL_ENCUESTADO_7");
	    lstVistas.add("v_PLAN_URBANISTICO");
	    lstVistas.add("v_OT_SERV_MUNICIPAL");
	    lstVistas.add("v_MUN_ENC_DIS");
	    lstVistas.add("v_CABILDO_CONSEJO");
	    lstVistas.add("v_ENTIDAD_SINGULAR");
	    lstVistas.add("v_POBLAMIENTO");
	    lstVistas.add("v_NUC_ABANDONADO");
	    
		try
        {  
			//1. comprueba que todos los datos numéricos son mayores o iguales a cero.
            //2. Comprueba qu etodos los valores de las tablas con dominios corresponden con los dominios publicados por el MPT.
                    //La comprobacin 2 no se realiza, ya que no se admiten otros valores por la BBDD.
			
			str.append(Messages.getString("integridadReferencial") + "\n");
			str.append("______________________________________________________________________\n\n");
			
			String sql = "";
			boolean mensajenumerico = false;
			for(int i=0; i<lstVistas.size(); i++){
				String tabla = (String)lstVistas.get(i);
				sql = "Select * from " + tabla;

				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				ResultSetMetaData rsm = resultSet.getMetaData();

				for(int j= 1; j<=rsm.getColumnCount(); j++){

					int type =rsm.getColumnType(j);

					if(type == Types.INTEGER || type == Types.NUMERIC || type == Types.BIGINT ||
							type == Types.DECIMAL || type == Types.DOUBLE || type == Types.FLOAT){

						String campo = rsm.getColumnName(j);
						String query = "SELECT *   FROM " + tabla + " WHERE " + campo + " < 0 OR " + campo +" IS NULL";
						
						ps1 = connection.prepareStatement(query);
						rs1 = ps1.executeQuery();
						int conTexto = 0;

						while (rs1.next()) {	
							if(!mensajenumerico){
								str.append(Messages.getString("integridadReferencial.parametrosnumericos") +"\n");
								mensajenumerico = true;
							}
							if(conTexto == 0){
								str.append("La tabla " + tabla + " en el campo " + campo + " CODINE: " +
										rs1.getString("PROVINCIA") + rs1.getString("MUNICIPIO") +"\n");
								conTexto ++;
							}
						}
					}
				}
			}
			str.append("\n\n");
        }
        catch (Exception e)
        {
        	str.append(Messages.getString("exception") + " " +Validacion_general.class + e.getMessage());
        	str.append("\n\n");
        }      	
        finally{
        	COperacionesEIEL.safeClose(null, ps, null);
        	COperacionesEIEL.safeClose(rs1, ps1, null);
        }
	}
}
