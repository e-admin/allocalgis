package com.geopista.instalador.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

//import oracle.sdoapi.OraSpatialManager;
//import oracle.sdoapi.adapter.AdapterSDO;
//import oracle.sdoapi.geom.GeometryFactory;
//import oracle.sdoapi.sref.SRManager;
//import oracle.sdoapi.sref.SpatialReference;
//import org.geotools.data.oracle.attributeio.AdapterJTS;
import org.postgis.PGgeometry;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;


/**
 * @author alfonsodiez
 *
 */
public class UTMED50ToGeoETRS89Converter {
//    private static Logger logger = Logger.getLogger(UTMED50ToGeoED50Converter.class);
    
    
//    private static String driverClassName = "org.postgresql.Driver";    
//    private static String serverName = "jdbc:postgresql://192.168.247.104";
//    private static String port = "5432";
//    private static String nombreBD = "geopista";
//    private static String user = "geopista";
//    private static String password = "01g7PT3a";
	
	private static Properties properties;
	
	private static final String LOCALGIS_DATABASE_DRIVERCLASSNAME = "localgis.database.driverClassName";
	private static final String LOCALGIS_DATABASE_SERVERNAME = "localgis.database.serverName";
	private static final String LOCALGIS_DATABASE_PORT = "localgis.database.port";
	private static final String LOCALGIS_DATABASE_NOMBREBD = "localgis.database.nombreBD";
	private static final String LOCALGIS_DATABASE_PASSWORD = "localgis.database.password";
	private static final String LOCALGIS_DATABASE_USERNAME = "localgis.database.username";
	
    static int SRID= 4230;
    static GeometryFactory fact= new GeometryFactory(new PrecisionModel(1E10), SRID);
    
    public UTMED50ToGeoETRS89Converter() {     
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        UTMED50ToGeoETRS89Converter uTMED50ToGeoED50Converter = new UTMED50ToGeoETRS89Converter();
        uTMED50ToGeoED50Converter.run(args);            
    }
    
    
    public void run(String[] args) {  
        doAction(args);
    }
    
    private void doAction(String[] args) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        boolean error = false;

        System.out.println("*********************");
        System.out.println("Comenzando...");
        System.out.println("*********************");
        
      //Cargamos datos del fichero de propiedades
        properties= new Properties();
        //Si el metodo es estatico utilizar <NOMBRE_CLASE>.class.getResourceAsStream()
        
        //InputStream resourceAsStream = UTMED50ToGeoETRS89Converter.class.getClassLoader().getResourceAsStream(args[1]);
        try {
        	FileInputStream entradaParametro=new FileInputStream(args[1]);
			properties.load(entradaParametro);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("No encuentra el fichero de configuración. Lo buscamos en la raiz");
			String systemPath = "./"+args[1];
			try {
				FileInputStream entradaDefecto=new FileInputStream(systemPath);
				properties.load(entradaDefecto);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("El fichero de configuración no se encuentra en el directorio raíz. Operación cancelada");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("No carga correctamente el properties. Operación cancelada");
				return;
			}
		}
        
        String driverClassName = properties.getProperty(LOCALGIS_DATABASE_DRIVERCLASSNAME);
        String serverName = properties.getProperty(LOCALGIS_DATABASE_SERVERNAME);
        String port = properties.getProperty(LOCALGIS_DATABASE_PORT);
        String nombreBD = properties.getProperty(LOCALGIS_DATABASE_NOMBREBD);
        String user = properties.getProperty(LOCALGIS_DATABASE_USERNAME);
        String  password = properties.getProperty(LOCALGIS_DATABASE_PASSWORD);
        
        Class driverClass = null;
        try {
            driverClass = Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            // logger.error(e.toString(), e);
            // throw e;
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            con = DriverManager.getConnection(serverName + ":" + port + "/" + nombreBD, user,
                    password);
            
//            System.out.println("serverName: " + serverName);
//            System.out.println("port: " + port);
//            System.out.println("nombreBD: " + nombreBD);
//            System.out.println("user: " + user);
//            System.out.println("password: " + password);
            System.out.println("Conectamos con BBDD...");

            /*
             * Add the geometry types to the connection. Note that you must
             * cast the connection to the pgsql-specific connection *
             * implementation before calling the addDataType() method.
             */
            ((org.postgresql.jdbc3.Jdbc3Connection) con).addDataType("geometry", "org.postgis.PGgeometry");
            ((org.postgresql.jdbc3.Jdbc3Connection) con).addDataType("box3d", "org.postgis.PGbox3d");

            con.setAutoCommit(true);

            String sSQL = "select distinct relname from pg_class pgc, pg_tables pgt,pg_attribute pga where pgc.relkind = 'r' " +
            		" and pgt.schemaname='public' and pgc.relname = pgt.tablename and pga.attrelid = pgc.oid " +
            		" and (pga.attname = 'GEOMETRY50' or pga.attname = 'GEOMETRY')";
            
            ps = con.prepareStatement(sSQL);
            rs = ps.executeQuery();
            String tabla = null;
            String campoTabla =null;
            int id = 0;
            String idComunidades = ""; 
            Geometry geometria = null;

            while (rs.next()) {
            	//Para cada tabla inicializamos las variables
                boolean campoGeometry = false;
                boolean campoGeometry50 = false;
                boolean [] campos = new boolean []{false,false};
                tabla = rs.getString("relname");   
                
                if (!tabla.equalsIgnoreCase("ConstruccionesGeom") && !tabla.equalsIgnoreCase("CultivosGeom")
                		&& !tabla.equalsIgnoreCase("parcelasBSQ")){
                
                	//Realizamos una consulta para obtener los nombres de los campos de las tablas y comprobar si
                	//GEOMETRY50 y GEOMETRY ya están creadas
                	campos= validaciones(con,tabla,campoTabla,campoGeometry50,campoGeometry);
                	campoGeometry50 = campos[0];
                	campoGeometry = campos[1];
                	if (!campoGeometry50){               		
	                	try {
	                	//Se renombra la columna, la cuál contendrá los datos originales en geográficas ED50
	                    sSQL= "alter table " + tabla + " rename column \"GEOMETRY\" to \"GEOMETRY50\"";
	                    ps2 = con.prepareStatement(sSQL);
	                    ps2.execute();
	                    //con.commit();                    
	                    ps2.close();
	                    } catch (SQLException se) {
	                    	error = true;
	                        System.out.println("SQLException - Error en el renombramiento de la columna");
	                        se.printStackTrace();
	                        System.out.println(se.getMessage());
	                    }catch (Exception ex2) {
	                        ex2.printStackTrace();
	                        error = true;
	                        System.out.println("Error en el renombramiento de la columna");
	                        System.out.println(ex2.getMessage());
	                        continue;
	                    }
	                    if (!error){
	                    	System.out.println("Tabla " + tabla + " con columna GEOMETRY renombrada a GEOMETRY50");
	                    	campoGeometry50 = true;
	                    	campoGeometry = false;
	                    }
                	}
                	else{
                		System.out.println("El campo GEOMETRY50 de la tabla " + tabla + " ya estaba creado");
                	}
                	if(campoGeometry50 && !campoGeometry){
	                    try {
	                    //Se crea la nueva columna, que será la que contenga los datos en geográficas ETRS89
	                    sSQL= "alter table " + tabla + " add column \"GEOMETRY\" geometry";
	                    ps2 = con.prepareStatement(sSQL);
	                    ps2.execute();
	                    //con.commit();                    
	                    ps2.close();
	                    } catch (SQLException se) {
	                    	error = true;
	                        System.out.println("SQLException - Error al añadir la columna GEOMETRY");
	                        se.printStackTrace();
	                        System.out.println(se.getMessage());
	                    }catch (Exception ex2) {
	                        ex2.printStackTrace();
	                        error = true;
	                        System.out.println("Error al añadir la columna GEOMETRY");
	                        System.out.println(ex2.getMessage());
	                        continue;
	                    }
	                    if (!error){
	                    	System.out.println("Tabla " + tabla + " con columna GEOMETRY añadida");
	                    	campoGeometry = true;
	                    }
                	}
                	else{
                		System.out.println("El campo GEOMETRY de la tabla " + tabla + " ya estaba creado");
                	}
                	if(campoGeometry50 && campoGeometry){
                		PreparedStatement psComprobarNulos = null;
	                    ResultSet rsComprobarNulos = null;
                		String sqlRegistrosNulos = "select count(*) as numeroNulos from " + tabla + " where \"GEOMETRY\" is null";
                		psComprobarNulos = con.prepareStatement(sqlRegistrosNulos);
                		rsComprobarNulos = psComprobarNulos.executeQuery();
	                    int numeroNulos=0;
	                    if(rsComprobarNulos.next()){
	                    	numeroNulos = rsComprobarNulos.getInt("numeroNulos");
	                    }
	                    
	                    //Si existen columnas nulas en GEOMETRY es que se necesita realizar la transformación
	                    if (numeroNulos >0){
		                    try {
		                    PreparedStatement psActualizarDatos = null;
		                    ResultSet rsActualizarDatos = null;
		                    //Transformación de coordenadas ED50 a ETRS89 y posterior almacenamiento en la BBDD
		                    sSQL = "select oid, \"GEOMETRY50\" from " + tabla;
		                    psActualizarDatos = con.prepareStatement(sSQL);
		                    rsActualizarDatos = psActualizarDatos.executeQuery();
		                    //recorremos cada uno de los registros para actualizar su geometría. Si no se ha devuelto ningún registro
		                    //es que la tabla estaba vacía, y no se transformarán, dado que no hay registro alguno
		                    while (rsActualizarDatos.next()) {
//		                    	if (!tabla.equalsIgnoreCase("com_autonomas")){
		                    		id = rsActualizarDatos.getInt("oid"); 
//		                    	}
//		                    	else{
//		                    		idComunidades = rsActualizarDatos.getString("id");
//		                    	}

		                    	PGgeometry pgGeometry=(PGgeometry)rsActualizarDatos.getObject("GEOMETRY50");
		                    	
		                    	//Si es nulo es que no tiene dato de Geometría y por lo tanto no se podrá convertir
		                    	if (pgGeometry != null){		                    		

			                    	//convertimos los tipos de Geometry con esta función, para podre ser usado en el método
			                    	//Reprojector.instance().reproject
			                    	geometria = convertGeometryPostGISToVividSolutions(pgGeometry);
			
			                    	final CoordinateSystem source = PredefinedCoordinateSystems.GEOGRAPHICS_ED50;
			                		final CoordinateSystem destination = PredefinedCoordinateSystems.GEOGRAPHICS_ETRS89;
			                		Reprojector.instance().reproject(geometria, source, destination);
			                		
//			                		if (!tabla.equalsIgnoreCase("com_autonomas")){
				                    	sSQL = "update " + tabla
				                                + " set \"GEOMETRY\"= ST_GeomFromText('" + geometria.toText() + "'," + 
				                                geometria.getSRID() + ") where oid=" + id;
				                                ps2 = con.prepareStatement(sSQL);
				                                ps2.execute();
				                                //con.commit();
//			                		}
//			                		else{
//			                			sSQL = "update " + tabla
//				                                + " set \"GEOMETRY\"= ST_GeomFromText('" + geometria.toText() + "'," + 
//				                                geometria.getSRID() + ") where id='" + idComunidades + "'";
//				                                ps2 = con.prepareStatement(sSQL);
//				                                ps2.execute();
//			                		}
				                    ps2.close();
		                    	}
		                    }
		                    
		                    

		                    psActualizarDatos.close();
		                    rsActualizarDatos.close();
		                    psComprobarNulos.close();
		                    rsComprobarNulos.close();
		                    } catch (SQLException se) {
		                    	error = true;
		                        System.out.println("SQLException - Error al transformar los datos");
		                        se.printStackTrace();
		                        System.out.println(se.getMessage());
		                    }catch (Exception e) {
		                        // logger.error(e.toString(), e);
		                        e.printStackTrace();
		                        error = true;
		                        System.out.println("Error al transformar los datos");
		                        System.out.println(e.getMessage());
		                        continue;
		                    }                    
		                    if (!error){
		                    	System.out.println("Tabla " + tabla + " con columna GEOMETRY transformada");
		                    }  
	                    }  
	                    else{
	                    	System.out.println("Todos los registros  de la tabla " + tabla + " estaban transformados");
	                    }
                    }   
                	else{
                    	System.out.println("Alguno de los campos  de la tabla " + tabla + " no estaba correctamente pretratado");
                    }

                }
            }
            System.out.println("Conversión finalizada");

        } catch (SQLException se) {
            // logger.error(se.toString(), se);
            se.printStackTrace();
            System.out.println(se.getMessage());
        } catch (Exception e) {
            // logger.error(e.toString(), e);
            // throw e;
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                // logger.error(e.toString(), e);
                e.printStackTrace();  
                System.out.println(e.getMessage());
            }
            try {
                ps.close();
            } catch (Exception e) {
                // logger.error(e.toString(), e);
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            try {
                ps2.close();
            } catch (Exception e) {
                // logger.error(e.toString(), e);
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            try {
                con.close();
            } catch (Exception e) {
                // logger.error(e.toString(), e);
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

    }

    
    /**
     * Función que convierte de un objeto com.vivid.solutions.jts.geom.Geometry a un objeto org.postgis.Geometry
     * @param pg 
     * @return objeto org.postgis.Geometry
     * @throws SQLException
     * @throws ParseException
     */
    static public PGgeometry convertGeometryVividSolutionsToPostGIS(Geometry geom) throws SQLException,
    ParseException
    {
    	String PGgeometryStr =
                "SRID=" + geom.getSRID() + ";" + geom.toString();
           PGgeometry  pg = new PGgeometry(PGgeometry.geomFromString(PGgeometryStr));
            return pg;
    }
    
    
    /**
     * Función que convierte de un objeto org.postgis.Geometry a un objeto com.vivid.solutions.jts.geom.Geometry
     * @param pg 
     * @return objeto com.vivid.solutions.jts.geom.Geometry
     * @throws SQLException
     * @throws ParseException
     */
    static public Geometry convertGeometryPostGISToVividSolutions(PGgeometry pg) throws SQLException,
    ParseException
    {
          String geometryString= pg.getGeometry().toString();
      WKTReader r= new WKTReader(fact);
                 Geometry geom;
                 // ADD SRID
                 if (geometryString.indexOf(';') != -1)
                 {
                       String []temp= PGgeometry.splitSRID(geometryString);
                       int srid= Integer.parseInt(temp[0].substring(5));
                       geom= (Geometry) r.read(temp[1]);
                       geom.setSRID(SRID);
                 }

                 else
                       geom= (Geometry) r.read(geometryString);
                 return geom;
    }
    
    
    /**
     * Validaciones necesarias para las operaciones de modificación de las tablas
     * @param con
     * @param tabla
     * @param campoTabla
     * @param campoGeometry50
     * @param campoGeometry
     * @throws SQLException
     */
    public boolean[] validaciones (Connection con, String tabla,String campoTabla,boolean campoGeometry50,boolean campoGeometry) throws SQLException{
    	
    	PreparedStatement ps = null;
        ResultSet rs = null;
        boolean [] campos = new boolean []{false,false};
    	String sqlNombreCampos = "SELECT a.attname AS nombrecampo FROM pg_class c, pg_attribute a "
    			+ " WHERE a.attrelid = c.oid and a.attnum > 0 AND c.relname='" + tabla + "'";
    	ps = con.prepareStatement(sqlNombreCampos);
        rs = ps.executeQuery();
        while (rs.next()) {
        	campoTabla = rs.getString("nombrecampo");                     	
        	if ((campoTabla.equalsIgnoreCase("GEOMETRY50")) || (campoTabla.equalsIgnoreCase("\"GEOMETRY50\""))){
        		campos[0] = true;
        	}
        	if ((campoTabla.equalsIgnoreCase("GEOMETRY")) || (campoTabla.equalsIgnoreCase("\"GEOMETRY\""))){
        		campos[1] = true;
        	}
        }
        
        return campos;
    }

}
