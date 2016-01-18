package com.geopista.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.server.administradorCartografia.PostGISConnection;
import com.geopista.server.inventario.InventarioConnection;


public class Test_BorrarInventarioMunicipio {

	private static final Log logger = LogFactory.getLog(Test_BorrarInventarioMunicipio.class);

	private static boolean COMMIT=true;
	private static String ENTORNO="DEV";
	/**
	 * Obtenemos los identificadores de municipios
	 */
	public void startProcess(String idMunicipioEntrada){
		
		Connection conn=null;
				
		try {
			conn = CommonProblem.getConnection(ENTORNO);
			
			InventarioConnection inventarioConnection=new InventarioConnection(idMunicipioEntrada);
			inventarioConnection.returnEliminarTodoInventario(conn,null,null);
			if (COMMIT){
				logger.info("Realizando final commit");
				conn.commit();
			}
			else{
				logger.error("Realizando final rollback");
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				logger.error("Realizando rollback");
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e1){	
			e1.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		finally{
			try {				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String idMunicipio="33034";

		ENTORNO="PRO";
		long startMils=Calendar.getInstance().getTimeInMillis();
		new Test_BorrarInventarioMunicipio().startProcess(idMunicipio);
		long endMils=Calendar.getInstance().getTimeInMillis();
		logger.info("Tiempo:"+(endMils-startMils)/1000+" segundos");
	}

}
