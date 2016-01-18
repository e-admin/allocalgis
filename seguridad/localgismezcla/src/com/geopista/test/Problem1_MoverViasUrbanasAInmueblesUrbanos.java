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


public class Problem1_MoverViasUrbanasAInmueblesUrbanos {

	private static final Log logger = LogFactory.getLog(Problem1_MoverViasUrbanasAInmueblesUrbanos.class);

	private static boolean COMMIT=true;
	private static String ENTORNO="DEV";
	/**
	 * Obtenemos los identificadores de municipios
	 */
	public void startProcess(String idMunicipioEntrada){
		
		Connection conn=null;
		
		String sSQL;
		if (idMunicipioEntrada==null)
			sSQL="select id,nombreoficial from municipios where id_provincia=33 order by id";
		else
			sSQL="select id,nombreoficial from municipios where id_provincia=33 and id="+idMunicipioEntrada+" order by id";


		try {
			conn = CommonProblem.getConnection(ENTORNO);
			Statement statement = conn.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sSQL);
			while (resultSet.next()){
				
				long idMunicipio=resultSet.getLong("id");
				String nombreoficial=resultSet.getString("nombreoficial");
				logger.info("Convirtiendo elementos del municipio:"+nombreoficial+"("+idMunicipio+")");
				solveProblem(conn,String.valueOf(idMunicipio));
			}
			resultSet.close();
			statement.close();
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

	public boolean solveProblem(Connection conn,String idMunicipioEntrada) throws Exception {
		
		/*String sSQL="select id,id_municipio from bienes_inventario where tipo="+TIPO_VIA_URBANA+" and " +
						"id_municipio=33041 and numInventario='1.1.42'";*/
		/*String sSQL="select id,id_municipio,numInventario from bienes_inventario where tipo="+CommonProblem.TIPO_VIA_URBANA+" and " +
		"id_municipio=33071 order by id";*/
		
		if (idMunicipioEntrada!=null){
			
		}
		
		
		String cadena1=null;
		String cadena2=null;
		if (idMunicipioEntrada!=null){
			cadena1="and id_municipio="+idMunicipioEntrada+" and revision_expirada=9999999999 and id not in (select distinct(bienes_inventario.id) ";
			cadena2="where tipo=4 and id_municipio="+idMunicipioEntrada+" and ";
		}
		else{
			cadena1="and revision_expirada=9999999999 and id not in (select distinct(bienes_inventario.id) ";
			cadena2="where tipo=4 and ";
		}

		
		
		String sSQL="select id,id_municipio,numInventario from bienes_inventario where tipo=4 " +		
				cadena1+				
				"from bienes_inventario inner join " +
				"observaciones_inventario on bienes_inventario.id=observaciones_inventario.id_bien " +
				cadena2 +
				"bienes_inventario.revision_expirada=9999999999 and" +
				" bienes_inventario.revision_actual=observaciones_inventario.revision_actual " +
				"and observaciones_inventario.descripcion ilike '%Clase urbana: Vial%' " +
				"order by id) order by id";
		
		/*String sSQL_Count="select count(*) as total from bienes_inventario where tipo="+CommonProblem.TIPO_VIA_URBANA+" and " +
		"id=? and id_municipio=?";*/
		
		String sSQL_Count="select count(*) as total from bienes_inventario where tipo=4 " +
		"and id=? and id_municipio=? and id not in (select distinct(bienes_inventario.id) " +
		"from bienes_inventario inner join " +
		"observaciones_inventario on bienes_inventario.id=observaciones_inventario.id_bien " +
		"where tipo=4	and id_municipio=? and " +
		"bienes_inventario.revision_expirada=9999999999 and" +
		" bienes_inventario.revision_actual=observaciones_inventario.revision_actual " +
		"and observaciones_inventario.descripcion ilike '%Clase urbana: Vial%' " +
		"order by id) group  by id order by id";
		
		
		

		Statement statement = conn.createStatement();
		
		logger.info("Ejecutando query");
		ResultSet resultSet = statement.executeQuery(sSQL);
		logger.info("Query ejecutada");
		int numElementos=0;
		int numElementosMalos=0;
		StringBuffer sb=new StringBuffer();
		while (resultSet.next()){
			
			long id=resultSet.getLong("id");
			int idMunicipio=resultSet.getInt("id_municipio");
			String numInventario=resultSet.getString("numInventario");
			
			logger.info("Municipio:"+idMunicipio+" Numero de Inventario:"+numInventario);
			try{
				CommonProblem.testExecuteQuery2(conn,sSQL_Count,numInventario,idMunicipio,id,idMunicipio);
			}catch (Exception e){
				sb.append("Municipio:"+idMunicipio+" Numero de Inventario:"+numInventario+"\n");
				numElementosMalos++;
				continue;
			}
			
			insertarBien(conn,id,numInventario,idMunicipio);
			
			numElementos++;

		}
		resultSet.close();
		statement.close();
		if (COMMIT){
			logger.info("Realizando commit");
			conn.commit();
		}
		else{
			logger.error("Realizando rollback");
			conn.rollback();
		}

		logger.info("Total Elementos:"+numElementos);
		logger.info("Malos:\n"+sb.toString());
		logger.info("Total Elementos Malos:"+numElementosMalos);
		if (numElementosMalos>0)
			CommonProblem.logErrors("Problem1_MoverViasUrbanasAInmueblesUrbanos","\nIdMunicipio:"+idMunicipioEntrada+"\n"+"Errores:"+sb.toString());
	
		return true;
	}
	
	
	
	private void insertarBien(Connection conn, long id,
						String numInventario,int idMunicipio) throws Exception{

		String sSQL0="select * from vias_inventario where id=?";
		
		String sSQL0_Count="select count(*) as total from vias_inventario where id=?";
		
		String sSQL1="insert into inmuebles(id,adquisicion,revision_actual,revision_expirada,patrimonio_municipal_suelo) values (?,?,?,?,0)";
		String sSQL2="insert into inmuebles_urbanos(id,revision_actual,revision_expirada) values (?,?,?)";

		String sSQL3="update bienes_inventario set tipo=2 where id=? and id_municipio=?";

		
		String sSQL3_1="select max(id) as max from observaciones_inventario";
		
		String sSQL3_2="insert into observaciones_inventario(id,descripcion,fecha,id_bien,revision_actual,revision_expirada) " +
				" values (?,?,?,?,?,?)";

		String sSQL4="update versiones set revision=?,id_table_versionada=12003 " +
						"where revision=? and id_table_versionada=12008";
		
		String sSQL4_count="select count(*) as total from versiones where revision=? and id_table_versionada=12008";
		String sSQL4_count1="select count(*) as total from versiones where revision=? and id_table_versionada=12003";

		String sSQL5="delete from vias_inventario where id=?";
		
		PreparedStatement ps= null;
		ResultSet rs=null;
		ResultSet rs1=null;
		
		try{
		
			CommonProblem.testExecuteQuery(conn,sSQL0_Count,numInventario,idMunicipio,id,-1);
			
			ps= conn.prepareStatement(sSQL0);
	        ps.setLong(1, id);
	        rs=ps.executeQuery();
	        while (rs.next()){
	        	String adquisicion=rs.getString("adquisicion");
	        	long revision_actual=rs.getLong("revision_actual");
	        	long revision_expirada=rs.getLong("revision_expirada");
	        	
	        	//Verificamos que solo existe un elemento en la tabla de versiones
	        	CommonProblem.testExecuteQuery(conn,sSQL4_count,numInventario,idMunicipio,revision_actual,-1);
	        	
	        	//Insertamos en inmuebles
	        	ps= conn.prepareStatement(sSQL1);
	            ps.setLong(1, id);
	            ps.setString(2, adquisicion);
	            ps.setLong(3, revision_actual);
	            ps.setLong(4, revision_expirada);
	            ps.execute();
	
	            
	        	//Insertamos en inmuebles_urbanos
	        	ps= conn.prepareStatement(sSQL2);
	            ps.setLong(1, id);
	            ps.setLong(2, revision_actual);
	            ps.setLong(3, revision_expirada);
	            ps.execute();
	            
	            
	            //Actualizamos el tipo de la tabla bien_inventario
	        	ps= conn.prepareStatement(sSQL3);
	            ps.setLong(1, id);
	            ps.setInt(2, idMunicipio);
	            ps.execute();
	            
	            
	            //Obtenemos el id de secuencia
	            long secuencia=0;
	            ps= conn.prepareStatement(sSQL3_1);
		        rs1=ps.executeQuery();
		        if (rs1.next()){
		        	secuencia=rs1.getLong("max");
		        }
	            
	            //Actualizamos la observacion para que ya no refleje vial
	            //Para los viales en el proceso habia un error y no se guardaba
	            //ninguna observacion asociada por lo que en lugar de actualizar, insertamos.
	        	ps= conn.prepareStatement(sSQL3_2);
	            ps.setLong(1,secuencia+1);
	            ps.setString(2, "Clase urbana: Finca");
	            ps.setDate(3,new Date(System.currentTimeMillis()));
	            ps.setLong(4, id);
	            ps.setLong(5,revision_actual);
	            ps.setLong(6,revision_expirada);
	            ps.execute();
	            
	            //Insertamos una observacion como indicando que ha sido reubicada
	            //habria que borrarla luego
	            ps= conn.prepareStatement(sSQL3_2);
	            ps.setLong(1,secuencia+2);
	            ps.setString(2, "Estado: Inmueble Reubicado");
	            ps.setDate(3,new Date(System.currentTimeMillis()));
	            ps.setLong(4, id);
	            ps.setLong(5,revision_actual);
	            ps.setLong(6,revision_expirada);
	            ps.execute();
	            
	            
	            //Si ya existe una entrada, no hacemos nada. No se porque
	            //pasa esto pero tiene pinta de que hay algo mal cargado.
	            int total=CommonProblem.testExecuteQuery(conn,sSQL4_count1,numInventario,idMunicipio,revision_actual,-1);
	            if (total==0){
		        	ps= conn.prepareStatement(sSQL4);
		            ps.setLong(1, revision_actual);
		            ps.setLong(2, revision_actual);
		            ps.execute();
	            }
	
	        }
       	        
	        
	        //Borramos la via
	    	ps= conn.prepareStatement(sSQL5);
	        ps.setLong(1, id);
	        ps.execute();
	        //logger.info("Elementos reubicados para Numero Inventario:"+numInventario);
		}
		finally{
			if (rs!=null)		
				rs.close();			
			if (rs1!=null)		
				rs1.close();
			if (ps!=null)
				ps.close();
		}
			
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		logger.info("Numero de argumentos:"+args.length);
		String idMunicipio=null;
		String entorno=null;
		String commit=null;
		
		if (args.length<2){
			logger.info("Uso <Programa> <Entorno> S/N (Commit) <Municipio>");
			return;
		}
		entorno=args[0];
		commit=args[1];		
		if (args.length>2)
			idMunicipio=args[2];
		
		if (commit.equals("S"))
			COMMIT=true;
		else
			COMMIT=false;
		ENTORNO=entorno;

		logger.error("Realizando COMMIT:"+COMMIT);
		logger.error("ENTORNO:"+ENTORNO);
		
		
		long startMils=Calendar.getInstance().getTimeInMillis();
		new Problem1_MoverViasUrbanasAInmueblesUrbanos().startProcess(idMunicipio);
		long endMils=Calendar.getInstance().getTimeInMillis();
		logger.info("Tiempo:"+(endMils-startMils)/1000+" segundos");
	}

}
