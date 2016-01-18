/**
 * Utils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.loadEIELData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.geopista.app.loadEIELData.beans.ColumnInfo;
import com.geopista.app.loadEIELData.beans.ConnectionInfo;
import com.geopista.app.loadEIELData.beans.LoadInfo;
import com.geopista.app.loadEIELData.beans.MunicipalityInfo;
import com.geopista.app.loadEIELData.beans.PrimaryKeyInfo;
import com.geopista.app.loadEIELData.beans.PrimaryKeysAndColumns;
import com.geopista.ui.plugin.external.ExternalDataSource;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.ShapefileReader;

public class Utils {
	public static PrintStream logger = null;
	public static PrintStream loggerError = null;
	public static PrintStream loggerContains= null;
	//public static PrintStream loggerMatchingSpecial = null;
	private static Driver pluginDrivers[] = null;

	public static final String CLAVE_PRIMARIA = "PK";
	public static final String VALOR_FIJO = "FIJO";
	public static final String VALOR_FILTRO = "FILTRO";
	public static final String VALOR_PK = "PK";

	public static final String TIPO_STRING = "STRING";
	public static final String TIPO_GEOMETRY = "GEOMETRY";

	public static final String TIPO_LOG_INFO = "INFO";
	public static final String TIPO_LOG_ERROR = "ERROR";

	public static final String SHP = "shp";
	public static final String BBDD = "bbdd";
	public static final String SHP_BBDD = "shp_bbdd";
	public static final String SQL_QUERY = "sql_query";

	public static final String INNER_JOIN = "INNER JOIN";
	public static final String RIGHT_JOIN = "RIGHT JOIN";

	public static final String ORIGEN = "origen";
	public static final String DESTINO = "destino";

	public static final String SI = "SI";
	public static final String NO = "NO";
	public static final String NO_EXISTE = "NO EXISTE";
	
	public static final String FIELD_ID_MUNICIPIO = "id_municipio";
	public static final String FIELD_CODPROV_CODMUNIC = "codprov_codmunic";	

	public static final String PROPERTY_DESTINY_DRIVER = "driverDestino";
	public static final String PROPERTY_DESTINY_BBDD_CONNECTION_PATH = "cadenaConexionDestino";
	public static final String PROPERTY_DESTINY_USER = "userDestino";
	public static final String PROPERTY_DESTINY_PASSWORD = "passwordDestino";

	public static final String PROPERTY_INE_PROVINCE = "ineProvincia";
	public static final String PROPERTY_INE_MUNICIPALITY = "ineMunicipio";
	public static final String PROPERTY_SHP_FILES_PATH = "rutaFicherosSHP";
	public static final String PROPERTY_ORIGIN_DRIVER = "driverOrigen";
	public static final String PROPERTY_ORIGIN_BBDD_CONNECTION_PATH = "cadenaConexionOrigenBBDD";
	public static final String PROPERTY_ORIGIN_USER = "userOrigen";
	public static final String PROPERTY_ORIGIN_PASSWORD = "passwordOrigen";
	public static final String PROPERTY_SHP_DIRECTORY = "directorioSHP";
	public static final String PROPERTY_BBDD_FILE = "ficheroBBDD";
	
	public static StringBuffer infoMatch1=new StringBuffer();
	public static StringBuffer infoMatch2=new StringBuffer();
	public static StringBuffer infoMatch3=new StringBuffer();
	public static StringBuffer infoMatchTemp=new StringBuffer();
	
	public static int numClavesCoincidentes=0;
	public static int numClavesCoincidentesActual=0;
	
	
	
	public static int numClavesCoincidentesSinMatchContains=0;
	public static int numClavesCoincidentesSinMatchContainsActual=0;

	//public static StringBuffer infoMatchSpecial=new StringBuffer();
	
	public static int contador=0;
	public static int contadorBD=0;
	public static int contadorTotal=0;

	public static final String SQL_SELECT_MUNICIPIOS = "SELECT nombreoficial,srid_proyeccion FROM municipios WHERE id_provincia=? AND id_ine=? LIMIT 1";

	private static final String UTM_30N_ETRS89="25830";
	static {
		File logCarga = new File("cargaEIEL_" + (new Date()).getDate() + ".log");
		File logCargaError = new File("cargaEIEL_Error_" + (new Date()).getDate() + ".log");
		File logCargaContains = new File("cargaEIEL_MatchContains_" + (new Date()).getDate() + ".log");
		try {
			logCarga.createNewFile();
			logCargaError.createNewFile();
			logCargaContains.createNewFile();
			logger = new PrintStream(new FileOutputStream(logCarga, false),true);
			logger.println("Inicio");
			loggerError = new PrintStream(new FileOutputStream(logCargaError, false),true);
			loggerError.println("Inicio");
			loggerContains = new PrintStream(new FileOutputStream(logCargaContains, false),true);
			loggerContains.println("Inicio");
			//loggerMatchingSpecial = new PrintStream(new FileOutputStream(logCargaMatchingSpecial, false),true);
			//loggerMatchingSpecial.println("Inicio");
		} catch (IOException e) {
			printLog(e.getMessage());
		}
	}

	public static Properties getProperties(String rutaProperties) {
		Properties properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File(rutaProperties));
			properties.load(fis);
			return properties;
		} catch (FileNotFoundException e) {
			printLog(e.getMessage());
		} catch (IOException e) {
			printLog(e.getMessage());
		} catch (NullPointerException e) {
			printLog(e.getMessage());
		}
		return null;
	}

	// Recupera datos de conexion --------------------->

	public static ConnectionInfo getConnectionInfo(
			Properties connectionProperties, String connectionName) {
		ConnectionInfo connectionInfo = new ConnectionInfo();
		int id = 1;
		boolean fin = false;
		while (fin != true) {
			String tempConnectionName = getConnectionName(connectionProperties,
					id);
			if (tempConnectionName.equals(connectionName)) {
				connectionInfo.setName(connectionName);
				connectionInfo.setDriver(getConnectionProperty(
						PROPERTY_DESTINY_DRIVER, connectionProperties, id)
						.trim());
				connectionInfo.setConnectionPath(getConnectionProperty(
						PROPERTY_DESTINY_BBDD_CONNECTION_PATH,
						connectionProperties, id).trim());
				connectionInfo
						.setUser(getConnectionProperty(PROPERTY_DESTINY_USER,
								connectionProperties, id).trim());
				connectionInfo.setPassword(getConnectionProperty(
						PROPERTY_DESTINY_PASSWORD, connectionProperties, id)
						.trim());
				fin = true;
			} else if (tempConnectionName.equals(NO_EXISTE))
				fin = true;
			else
				id++;
		}
		return connectionInfo;
	}

	public static String getConnectionName(Properties properties, int id) {
		return properties.getProperty("conexion" + id + ".nombre", NO_EXISTE);
	}

	public static String getConnectionProperty(String propertyName,
			Properties properties, int id) {
		return properties.getProperty("conexion" + id + "." + propertyName, "");
	}

	// ------------------------------------------------->

	// Recupera Informacion de carga --------------------->

	public static LoadInfo getLoadInfo(Properties loadProperties,
			String loadName, String municipiosFijos,Connection connection) {
		LoadInfo loadInfo = new LoadInfo();
		int loadId = getLoadId(loadProperties, loadName);
		if (loadId > 0) {
			loadInfo.setName(loadName);
			loadInfo.setId(loadId);
			loadInfo.setLoadFilePath(getLoadFilePath(loadProperties, loadId));
			loadInfo.setLoadFiles(getLoadFiles(loadProperties, loadId));
			loadInfo.setLoadMunicipalities(getLoadMunicipalities(
					loadProperties, loadId, municipiosFijos,connection));
		}
		return loadInfo;
	}

	public static int getLoadId(Properties properties, String loadName) {
		int loadId = 1;
		boolean fin = false;
		while (fin != true) {
			String tempLoadName = getLoadName(properties, loadId);
			if (tempLoadName.equals(loadName))
				fin = true;
			else if (tempLoadName.equals("")) {
				loadId = 0;
				fin = true;
			} else
				loadId++;
		}
		return loadId;
	}

	public static String getLoadName(Properties properties, int id) {
		return properties.getProperty("carga" + id + ".nombre");
	}

	public static String getLoadFilePath(Properties properties, int id) {
		return properties.getProperty("carga" + id + ".rutaFicherosCarga");
	}

	public static ArrayList<String> getLoadFiles(Properties properties,
			int loadId) {
		ArrayList<String> loadFiles = new ArrayList<String>();
		int loadFileId = 1;
		boolean fin = false;
		while (fin != true) {
			String loadFileName = getLoadFile(properties, loadId, loadFileId);
			if (loadFileName.equals(NO_EXISTE))
				fin = true;
			else if (!loadFileName.equals(NO))
				loadFiles.add(loadFileName);
			loadFileId++;
		}
		return loadFiles;
	}

	public static String getLoadFile(Properties properties, int id,
			int loadFileId) {
		String doLoad = properties.getProperty("carga" + id + ".ficheroCarga"
				+ loadFileId + ".hacer", NO_EXISTE);
		if (doLoad.equals(SI)) {
			return properties.getProperty("carga" + id + ".ficheroCarga"
					+ loadFileId + ".nombre");
		}
		return doLoad;
	}

	@SuppressWarnings("unused")
	public static ArrayList<MunicipalityInfo> getLoadMunicipalities(
			Properties properties, int loadId, String municipiosFijos,Connection connection) {
		ArrayList<MunicipalityInfo> loadMunicipalities = new ArrayList<MunicipalityInfo>();
		int loadMunicipalityId = 1;
		boolean fin = false;
		while (fin != true) {
			MunicipalityInfo loadMunicipality = getLoadMunicipality(properties,
					loadId, loadMunicipalityId, municipiosFijos,connection);
			if (loadMunicipality == null)
				fin = true;
			else if (loadMunicipality.getName() != null)
				loadMunicipalities.add(loadMunicipality);
			loadMunicipalityId++;
		}
		return loadMunicipalities;
	}

	public static MunicipalityInfo getLoadMunicipality(Properties properties,
			int id, int loadMunicipalityId, String municipiosFijos,Connection connection) {
		MunicipalityInfo loadMunicipality = new MunicipalityInfo();
		String doLoad = properties.getProperty("carga" + id + ".municipio"
				+ loadMunicipalityId + ".hacer", NO_EXISTE);
		
		if (doLoad.equals(SI)) {
			String ineProvincia = getMunicipalityProperty(
					PROPERTY_INE_PROVINCE, properties, id, loadMunicipalityId);
			String ineMunicipio = getMunicipalityProperty(
					PROPERTY_INE_MUNICIPALITY, properties, id,
					loadMunicipalityId);
			if (municipiosFijos!=null){
				if (!municipiosFijos.contains(ineMunicipio))
					return loadMunicipality;				
			}
			
			String shpDirectory = getMunicipalityProperty(
					PROPERTY_SHP_DIRECTORY, properties, id, loadMunicipalityId);
			String shpFilesPath = getMunicipalityProperty(
					PROPERTY_SHP_FILES_PATH, properties, id, loadMunicipalityId)
					+ shpDirectory + "/";
			String originDriver = getMunicipalityProperty(
					PROPERTY_ORIGIN_DRIVER, properties, id, loadMunicipalityId);
			String bbddFile = getMunicipalityProperty(PROPERTY_BBDD_FILE,
					properties, id, loadMunicipalityId);
			String originBBDDConnectionPath = getMunicipalityProperty(
					PROPERTY_ORIGIN_BBDD_CONNECTION_PATH, properties, id,
					loadMunicipalityId)
					+ bbddFile;
			String originUser = getMunicipalityProperty(PROPERTY_ORIGIN_USER,
					properties, id, loadMunicipalityId);
			
			String originPassword = getMunicipalityProperty(
					PROPERTY_ORIGIN_PASSWORD, properties, id,
					loadMunicipalityId);
			

			if (ineProvincia.length() == 2 && ineMunicipio.length() == 3) {
				loadMunicipality.setIne(ineProvincia + ineMunicipio);
				loadMunicipality.setOriginDriver(originDriver);
				loadMunicipality
						.setOriginBBDDConnectionPath(originBBDDConnectionPath);
				loadMunicipality.setOriginUser(originUser);
				loadMunicipality.setOriginPassword(originPassword);
				loadMunicipality.setShpFilesPath(shpFilesPath);
				if ( !ineMunicipio.equals("000") ){
					PreparedStatement preparedStatement;
					ResultSet rs = null;
					try {
						preparedStatement = connection
								.prepareStatement(SQL_SELECT_MUNICIPIOS);
						preparedStatement.setString(1, ineProvincia);
						preparedStatement.setString(2, ineMunicipio);
						rs = preparedStatement.executeQuery();
						while (rs.next()) {
							loadMunicipality.setName(rs.getString("nombreoficial"));
							loadMunicipality.setSrid(rs.getString("srid_proyeccion"));
						}
					} catch (SQLException e) {
						logger.println(e);
						return null;
					}
				}
				else{
					loadMunicipality.setName("GENERIC");
					loadMunicipality.setSrid(UTM_30N_ETRS89);
				}
			}
		} else if (doLoad.equals(NO_EXISTE))
			return null;
		return loadMunicipality;
	}

	public static String getMunicipalityProperty(String propertyName,
			Properties properties, int id, int municipalityId) {
		return properties.getProperty("carga" + id + ".municipio"
				+ municipalityId + "." + propertyName,
				properties.getProperty("carga" + id + "." + propertyName, ""));
	}

	// ------------------------------------------------->

	public static ExternalDataSource generateExternalDataSource(
			String origen_destino, String driver, String cadena_conexion,
			String user, String password) {
		ExternalDataSource externalDataSource = new ExternalDataSource();
		externalDataSource.setDriver(driver);
		externalDataSource.setConnectString(cadena_conexion);
		externalDataSource.setUserName(user);
		externalDataSource.setPassword(password);
		logger.println("generaExternalDataSource " + origen_destino
				+ ": Driver: " + driver);
		logger.println("generaExternalDataSource " + origen_destino
				+ ": Cadena conexion: " + cadena_conexion);
		logger.println("generaExternalDataSource " + origen_destino
				+ ": User: " + user);
		logger.println("generaExternalDataSource " + origen_destino
				+ ": Password: " + password);
		return externalDataSource;
	}

	public static Connection getConnection(ExternalDataSource externalDataSource) {
		Connection connection = null;
		try {
			Driver d = (Driver) Class.forName(externalDataSource.getDriver())
					.newInstance();
		} catch (Exception e) {
			printLog(e.getMessage());
		}
		try {
			// Si la cadena de conexion empiza por "DBQ=" entendemos que es una
			// conexion mdb y nos conectamos directamente al fichero,
			// sin conexion jdbc.odbc.
			if (externalDataSource.getConnectString().startsWith("DBQ=")) {
				connection = DriverManager.getConnection(
						"jdbc:odbc:MS Access Database;"
								+ externalDataSource.getConnectString(),
						externalDataSource.getUserName(),
						externalDataSource.getPassword());
			} else {
				connection = DriverManager.getConnection(
						externalDataSource.getConnectString(),
						externalDataSource.getUserName(),
						externalDataSource.getPassword());
			}
			return connection;
		} catch (SQLException e) {
			printLog(e.getMessage());
			print("Error en la conexión con la base de datos: " + externalDataSource.getConnectString());
			print("Revisa en sistemas de 64 bits que esta utilizando la jre de 32 bits");
			return null;
		}
	}

	public static void executeQuery(Connection conn, String consulta) throws SQLException {
		Statement stmt;
		try {
			// stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			// ResultSet.CONCUR_READ_ONLY);
			stmt = conn.createStatement();
			stmt.executeUpdate(consulta);
		} catch (SQLException e) {
			throw e;
		}
	}

	public static int executeQuerys(Connection conn, ArrayList consultas, int[] errores) {
		int totalQuerys = 0;
		int totalInserted = 0;
		try {
			if (consultas != null) {
				Iterator i = consultas.iterator();
				while (i.hasNext()) {
					String consulta = (String) i.next();
					totalQuerys++;
					try{
						executeQuery(conn, consulta);	
						totalInserted++;
					}
					catch(SQLException e){						
						errores[0]++;
						String errorMsg = "ERROR: Operacion no completada con exito: \n"
							+ consulta + " \n" + e.getMessage();
						logger.println(errorMsg);
						System.out.println(errorMsg);					
					}
				}
			}
		} catch (Exception e) {
			logger.println("lanzamosConsultasBBDD: Operacion no completada con exito.");
			logger.println("lanzamosConsultasBBDD - : Inserciones completadas con exito: "
					+ totalInserted);
		}
		logger.println("lanzamosConsultasBBDD - : Operacion completada con exito.");
		logger.println("lanzamosConsultasBBDD - : Inserciones completadas con exito: "
				+ totalInserted + " / " + totalQuerys);
		return totalInserted;
	}

	public static ArrayList<PrimaryKeysAndColumns> getElementsBBDD(
			Connection conn, String nameBBDD, String where,
			Hashtable<String, PrimaryKeyInfo> primaryKeys,
			ArrayList<ColumnInfo> columns, int relation) {

		ArrayList<PrimaryKeysAndColumns> elementos = new ArrayList<PrimaryKeysAndColumns>();

		/*
		 * GENERAMOS SELECT:
		 */
		StringBuffer select = new StringBuffer("SELECT ");

		try {
			// Alamacena las claves primarias con sus valores
			// Iterator it = primaryKeys.iterator();
			// while (it.hasNext()) {
			// Hashtable<String, PrimaryKeyInfo> pk = (Hashtable<String,
			// PrimaryKeyInfo>) it
			// .next();
			Enumeration keys = primaryKeys.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if (select.length() > 7)
					select.append(", ");
				select.append(primaryKeys.get(key).getName());
			}
			// }
			// almacena los campos consultados con sus valores
			Iterator it = columns.iterator();
			while (it.hasNext()) {
				ColumnInfo column = (ColumnInfo) it.next();
				if (!column.getCharacteristic().equals(VALOR_FIJO)) {
					if (select.length() > 7)
						select.append(", ");
					select.append(column.getOriginName());
				}
			}
		} catch (Exception e) {
			printLog(e.getMessage());
		}

		select.append(" FROM " + nameBBDD);
		// Por si añadimos una condición en el properties:
		if (where != null)
			select.append(" " + where);

		/*
		 * LANZAMOS SELECT:
		 */
		logger.println("obtenemosElementosBBDD: " + select);
		Statement stmt;
		ResultSet rs = null;
		try {
			PrimaryKeysAndColumns pkcFinal = null;

			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(select.toString());
			ResultSetMetaData rsmd = rs.getMetaData();
			Hashtable<String, String> columnValue;

			while (rs.next()) {
				// Carga los nombres de columna junto con sus valores de la BBDD
				// en un Hashtable
				int columnId = 1;
				columnValue = new Hashtable<String, String>();
				while (columnId <= rsmd.getColumnCount()) {
					String value = rs.getString(columnId);
					if (value == null)
						value = "";
					columnValue.put(rsmd.getColumnName(columnId), value);
					columnId++;
				}

				// Carga los nombres de columnas de tipo claves primarias junto
				// con sus valores de la BBDD en un Hashtable
				Enumeration keys = primaryKeys.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					if (columnValue.get(key) == null)
						columnValue.put(key,
								rs.getObject(primaryKeys.get(key).getName())
										.toString());
				}

				// Rellena un ArrayList con claves primarias y sus valores de la
				// BBDD
				ArrayList<ColumnInfo> columnsAndValues = new ArrayList<ColumnInfo>();
				Hashtable primaryKeyFinal = new Hashtable();
				Hashtable primaryValueFinal = new Hashtable();
				Hashtable<String,String> primaryKeyFinalOriginal = new Hashtable<String,String>();
				pkcFinal = new PrimaryKeysAndColumns();
				keys = primaryKeys.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					PrimaryKeyInfo primaryKey = new PrimaryKeyInfo();
					PrimaryKeyInfo value=(PrimaryKeyInfo)primaryKeys.get(key);
					String valor=value.getName();
					// primaryKeyFinal.put(key,
					// rs.getObject(primaryKeys.get(key).getName())
					// .toString());
					primaryKeyFinal.put(key,
							columnValue.get(primaryKeys.get(key).getName()));
					if (valor!=null){
						primaryValueFinal.put(key,valor);
						primaryKeyFinalOriginal.put(key,valor);
					}
					else
						System.out.println("No hay valor");
					
				}
				pkcFinal.setPrimaryKeys(primaryKeyFinal);
				pkcFinal.setPrimaryKeysOriginal(primaryKeyFinalOriginal);
				pkcFinal.setValueKeys(primaryValueFinal);

				// almacena los campos consultados con sus valores
				Iterator it = columns.iterator();
				while (it.hasNext()) {
					ColumnInfo column = (ColumnInfo) it.next();
					ColumnInfo columnFinal = new ColumnInfo(column);

					if (!column.getCharacteristic().equals(VALOR_FIJO))
						// columnFinal.setValue(rs.getObject(
						// column.getOriginName()).toString());
						columnFinal.setValue(columnValue.get(column
								.getOriginName()));
					columnsAndValues.add(columnFinal);
				}

				pkcFinal.setColumns(columnsAndValues);
				elementos.add(pkcFinal);

			}
		} catch (SQLException e) {
			printLog(e.getMessage());
		} catch (NullPointerException e) {
			printLog(e.getMessage());
		}
		return elementos;
	}

	public static ArrayList<PrimaryKeysAndColumns> getElementsBBDD(
			Connection conn, String nameBBDD, String where,
			ArrayList<ColumnInfo> columns, int relation) {

		ArrayList<PrimaryKeysAndColumns> elementos = new ArrayList<PrimaryKeysAndColumns>();

		/*
		 * GENERAMOS SELECT:
		 */
		StringBuffer select = new StringBuffer("SELECT ");

		try {
			// almacena los campos consultados con sus valores
			Iterator it = columns.iterator();
			while (it.hasNext()) {
				ColumnInfo column = (ColumnInfo) it.next();
				if (!column.getCharacteristic().equals(VALOR_FIJO)) {
					if (select.length() > 7)
						select.append(", ");
					select.append(column.getOriginName());
				}
			}
		} catch (Exception e) {
			printLog(e.getMessage());
		}

		select.append(" FROM " + nameBBDD);
		// Por si añadimos una condición en el properties:
		if (where != null)
			select.append(" " + where);

		/*
		 * LANZAMOS SELECT:
		 */
		logger.println("obtenemosElementosBBDD: " + select);
		Statement stmt;
		ResultSet rs = null;
		try {
			PrimaryKeysAndColumns pkcFinal = null;

			stmt = conn.createStatement();
			rs = stmt.executeQuery(select.toString());

			ResultSetMetaData rsmd = rs.getMetaData();
			Hashtable<String, String> columnValue;

			while (rs.next()) {
				int columnId = 1;
				columnValue = new Hashtable<String, String>();
				while (columnId <= rsmd.getColumnCount()) {
					String value = rs.getString(columnId);
					if (value == null)
						value = "";
					columnValue.put(rsmd.getColumnName(columnId), value);
					columnId++;
				}

				ArrayList<ColumnInfo> columnsAndValues = new ArrayList<ColumnInfo>();
				pkcFinal = new PrimaryKeysAndColumns();
				// almacena los campos consultados con sus valores
				Iterator it = columns.iterator();
				while (it.hasNext()) {
					ColumnInfo column = (ColumnInfo) it.next();
					if(column.getOriginName().contains("."))
						column.setOriginName(column.getOriginName().substring(column.getOriginName().indexOf(".")+1));
					if(column.getDestinyName().contains("."))
						column.setDestinyName(column.getDestinyName().substring(column.getDestinyName().indexOf(".")+1));		
					if(column.getValue().contains("."))
						column.setValue(column.getValue().substring(column.getValue().indexOf(".")+1));		
					ColumnInfo columnFinal = new ColumnInfo(column);
					
					if (!column.getCharacteristic().equals(VALOR_FIJO))
						// columnFinal.setValue(rs.getObject(
						// column.getOriginName()).toString());
						columnFinal.setValue(columnValue.get(column
								.getOriginName()));
					
					columnsAndValues.add(columnFinal);
				}

				pkcFinal.setColumns(columnsAndValues);
				elementos.add(pkcFinal);
			}
		} catch (SQLException e) {
			printLog(e.getMessage());
		}
		return elementos;
	}

	public static String getBBDDWhere(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation
				+ ".bbdd.origen.where");
	}
	
	public static boolean containsColumnWithValue(ArrayList<ColumnInfo> columns, String columnName, String columnValue){
		boolean contain = false;
		Iterator itColumns = columns.iterator();
		while(itColumns.hasNext()){
			ColumnInfo column = (ColumnInfo) itColumns.next();
			if(column.getDestinyName().equals(columnName) && column.getValue().equals(columnValue)){
				contain = true;
			}
		}		
		return contain;
	}	

	public static ArrayList<PrimaryKeysAndColumns> getElementsSHP(
			String ubicacion, String nameSHP,
			Hashtable<String, PrimaryKeyInfo> primaryKeys,
			Hashtable<String, Hashtable<String, String>> primaryKeyFilters,
			ArrayList<ColumnInfo> columns, Properties properties,int relation, String relationCharacteristic) {

		ArrayList<PrimaryKeysAndColumns> elementos = new ArrayList<PrimaryKeysAndColumns>();
		FeatureCollection featureCollection = null;
		try {
			PrimaryKeysAndColumns pkcFinal = null;
			
			HashMap hashFiltros=getFiltro(properties,SHP,relation);
			
			featureCollection = loadShapefile(ubicacion + nameSHP,hashFiltros);
			if (featureCollection != null) {
				List featList = featureCollection.getFeatures();
				Iterator j = featList.iterator();
				while (j.hasNext()) {
					ArrayList<ColumnInfo> columnsAndValues = new ArrayList<ColumnInfo>();
					Feature f = (Feature) j.next();
					Hashtable<String,String> primaryKeyFinal = new Hashtable<String,String>();
					Hashtable<String,String> primaryKeyFinalOriginal = new Hashtable<String,String>();
					Hashtable<String,String> primaryValueFinal = new Hashtable<String,String>();
					pkcFinal = new PrimaryKeysAndColumns();
					// Almacena las claves primarias con sus valores
					// Iterator it = primaryKeys.iterator();
					// while (it.hasNext()) {
					// Hashtable<String, PrimaryKeyInfo> pk = (Hashtable<String,
					// PrimaryKeyInfo>) it
					// .next();

					Enumeration keys = primaryKeys.keys();
					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();

						PrimaryKeyInfo value1=(PrimaryKeyInfo)primaryKeys.get(key);
						String valor=value1.getName();
						if (valor!=null)
							primaryValueFinal.put(key,valor);
						
						
						String value;
						String valueOriginal;
						if (!primaryKeys.get(key).getCharacteristic()
								.equals(VALOR_FIJO)){
							value = f.getAttribute(
									primaryKeys.get(key).getName().toString())
									.toString();
							valueOriginal=value;
						}
						else{
							value = primaryKeys.get(key).getName().toString();
							valueOriginal=value;
						}
						if (primaryKeys.get(key).getCharacteristic()
								.equals(VALOR_FILTRO)) {
							String filterValue = primaryKeyFilters.get(key)
									.get(value.toUpperCase());
							if(filterValue!=null){
								valueOriginal=value;
								value = filterValue;
							}
							else
								logger.println("ERROR FILTRO: No encontrado filtro para " + value + " (Clave primaria: " + primaryKeys.get(key).getName() + ")");
						}
						primaryKeyFinal.put(key, value);
						if (valueOriginal!=null)
							primaryKeyFinalOriginal.put(key, valueOriginal);
						else
							primaryKeyFinalOriginal.put(key, "");
						primaryValueFinal.put(key, valor);
					}
					pkcFinal.setPrimaryKeys(primaryKeyFinal);
					pkcFinal.setPrimaryKeysOriginal(primaryKeyFinalOriginal);
					pkcFinal.setValueKeys(primaryValueFinal);
					// }
					// almacena los campos consultados con sus valores
					Iterator it = columns.iterator();
					while (it.hasNext()) {
						ColumnInfo column = (ColumnInfo) it.next();
						ColumnInfo columnFinal = new ColumnInfo(column);
						if (!column.getCharacteristic().equals(VALOR_FIJO))
							columnFinal.setValue(f.getAttribute(
									column.getOriginName()).toString());
						columnsAndValues.add(columnFinal);
					}
					pkcFinal.setColumns(columnsAndValues);
					elementos.add(pkcFinal);
				}
			} else {			
				Hashtable primaryKeyFinal = new Hashtable();
				pkcFinal = new PrimaryKeysAndColumns();
				Enumeration keys = primaryKeys.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();

					String value = "";
					if (primaryKeys.get(key).getCharacteristic()
							.equals(VALOR_FIJO))
						value = primaryKeys.get(key).getName().toString();
					if (primaryKeys.get(key).getCharacteristic()
							.equals(VALOR_FILTRO)) {
						String filterValue = primaryKeyFilters.get(key).get(
								value);
						value = filterValue;
					}
					if(value==null)
						value = "";
					primaryKeyFinal.put(key, value);

				}
				ArrayList<ColumnInfo> columnsAndValues = new ArrayList<ColumnInfo>();
				ColumnInfo columnFinal = null;

				Iterator itColumns = columns.iterator();
				while (itColumns.hasNext()) {
					ColumnInfo column = (ColumnInfo) itColumns.next();
					
					columnFinal = new ColumnInfo();
					columnFinal.setDestinyName(column.getDestinyName());
					columnFinal.setOriginName(column.getOriginName());
					columnFinal.setCharacteristic(column.getCharacteristic());
					columnFinal.setType(column.getType());
					String value = "";
					if (column.getCharacteristic()
							.equals(VALOR_FIJO))
						value = column.getValue();
					columnFinal.setValue(value);
					
					columnsAndValues.add(columnFinal);
				}
				pkcFinal.setColumns(columnsAndValues);
				pkcFinal.setPrimaryKeys(primaryKeyFinal);
				elementos.add(pkcFinal);
			}
		} catch (Exception e) {
			printLog(e.getMessage());
		}
		return elementos;
	}

	public static ArrayList<PrimaryKeysAndColumns> getElementsSHP(
			String ubicacion, String nameSHP, ArrayList<ColumnInfo> columns,
			Properties properties,int relation) {

		ArrayList<PrimaryKeysAndColumns> elementos = new ArrayList<PrimaryKeysAndColumns>();
		FeatureCollection featureCollection = null;
		try {
			PrimaryKeysAndColumns pkcFinal = null;
			
			HashMap hashFiltros=getFiltro(properties,SHP,relation);
			featureCollection = loadShapefile(ubicacion + nameSHP,hashFiltros);
			List featList = featureCollection.getFeatures();
			Iterator j = featList.iterator();
			while (j.hasNext()) {
				ArrayList<ColumnInfo> columnsAndValues = new ArrayList<ColumnInfo>();
				Feature f = (Feature) j.next();
				pkcFinal = new PrimaryKeysAndColumns();
				// almacena los campos consultados con sus valores
				Iterator it = columns.iterator();
				while (it.hasNext()) {
					ColumnInfo column = (ColumnInfo) it.next();
					ColumnInfo columnFinal = new ColumnInfo(column);
					if (!column.getCharacteristic().equals(VALOR_FIJO))
						columnFinal.setValue(f.getAttribute(
								column.getOriginName()).toString());
					columnsAndValues.add(columnFinal);
				}
				pkcFinal.setColumns(columnsAndValues);
				elementos.add(pkcFinal);
			}
		} catch (Exception e) {
			printLog(e.getMessage());
		}
		return elementos;
	}

	public static ArrayList<ColumnInfo> getColumnsInfo(Properties properties,
			String dataType, int relation) {
		ArrayList<ColumnInfo> columns = new ArrayList<ColumnInfo>();
		int i = 1;
		boolean fin = false;
		while (!fin) {
			String originName;
			String destinyName;
			String value;
			String type;
			String characteristic;

			originName = properties.getProperty("relacion" + relation + "."
					+ dataType + ".origen.columna" + i + ".valor");
			destinyName = properties.getProperty("relacion" + relation + "."
					+ dataType + ".destino.columna" + i);
			value = properties.getProperty("relacion" + relation + "."
					+ dataType + ".origen.columna" + i + ".valor");
			type = properties.getProperty("relacion" + relation + "."
					+ dataType + ".origen.columna" + i + ".tipo");
			characteristic = properties.getProperty("relacion" + relation + "."
					+ dataType + ".origen.columna" + i + ".caracteristica");

			if (originName == null)
				originName = "";
			if (destinyName == null)
				destinyName = "";
			if (value == null)
				value = "";
			if (type == null)
				type = "";
			if (characteristic == null)
				characteristic = "";
			columns.add(new ColumnInfo(originName.trim(), destinyName.trim(),
					value.trim(), type.trim(), characteristic.trim()));
			int siguiente = i + 1;
			if (properties.getProperty("relacion" + relation + "." + dataType
					+ ".origen.columna" + siguiente + ".valor") == null)
				fin = true;

			i++;
		}
		return columns;
	}

	public static Hashtable<String, Hashtable<String, String>> getPrimaryKeyFilters(
			Properties properties, String dataType, int relation) {
		Hashtable<String, Hashtable<String, String>> pkFilters = new Hashtable<String, Hashtable<String, String>>();
		int i = 1;
		boolean fin = false;
		while (!fin) {
			String characteristic = properties
					.getProperty("relacion" + relation + "." + dataType + ".pk"
							+ i + ".caracteristica");
			if (characteristic != null && characteristic.equals(VALOR_FILTRO)) {
				String originFilters = properties.getProperty("relacion"
						+ relation + "." + dataType + ".pk" + i
						+ ".filtro.origen");
				String destinyFilters = properties.getProperty("relacion"
						+ relation + "." + dataType + ".pk" + i
						+ ".filtro.destino");
				if (originFilters != null && destinyFilters != null) {
					String[] originFilter = originFilters.split(",");
					String[] destinyFilter = destinyFilters.split(",");
					if (originFilter.length == destinyFilter.length) {
						Hashtable<String, String> filter = new Hashtable<String, String>();
						int j = 0;
						while (originFilter.length > j) {
							if (originFilter[j] != null
									&& destinyFilter[j] != null){
								String filterActual=filter.get(originFilter[j]);
								if (filterActual!=null){
									filter.put(originFilter[j], filterActual+","+destinyFilter[j]);
								}
								else{
									filter.put(originFilter[j], destinyFilter[j]);
								}
							}
							j++;
						}
						pkFilters.put("pk" + i, filter);
					}
				}
			}
			int siguiente = i + 1;
			if (properties.getProperty("relacion" + relation + "." + dataType
					+ ".pk" + siguiente) == null)
				fin = true;
			i++;
		}
		return pkFilters;
	}

	public static Hashtable<String, PrimaryKeyInfo> getPrimaryKeysInfo(
			Properties properties, String dataType, int relation) {

		Hashtable<String, PrimaryKeyInfo> pk = new Hashtable<String, PrimaryKeyInfo>();
		int i = 1;
		boolean fin = false;
		while (!fin) {
			String name;
			String characteristic;
			name = properties.getProperty("relacion" + relation + "."
					+ dataType + ".pk" + i, "");

			characteristic = properties.getProperty("relacion" + relation + "."
					+ dataType + ".pk" + i + ".caracteristica", "");

			pk.put("pk" + i, new PrimaryKeyInfo(name, characteristic));
			int siguiente = i + 1;
			if (properties.getProperty("relacion" + relation + "." + dataType
					+ ".pk" + siguiente) == null)
				fin = true;
			i++;
		}
		return pk;
	}

	public static FeatureCollection loadShapefile(String filename,HashMap filtros)
			throws Exception {
		try {
			ShapefileReader rdr = new ShapefileReader();
			
			if (filtros!=null)
				rdr.setFilters(filtros);
			DriverProperties dp = new DriverProperties();
			dp.set("File", filename);
			if ((new File(filename)).exists()) {
				return rdr.read(dp);
			}
			else{
				print("Error en la carga del fichero SHP ORIGEN: " + filename);
			}
		} catch (Exception e) {
			print("Error en la carga del fichero SHP ORIGEN: " + filename);
			return null;
		}
		return null;
	}

	public static ArrayList<ColumnInfo> generateColumnsBBDDEmpty(
			ArrayList<ColumnInfo> columnsBBDD, Hashtable<String, PrimaryKeyInfo> primaryKeysBBDD) {
		ArrayList<ColumnInfo> columnsBBDDEmpty = new ArrayList<ColumnInfo>();
		Iterator itBBDD = columnsBBDD.iterator();
		while (itBBDD.hasNext()) {
			ColumnInfo columnBBDD = (ColumnInfo) itBBDD.next();
			String value;
			if (columnBBDD.getType().equals(TIPO_STRING))
				value = "";
			else
				value = "0";
			
			String valor = VALOR_FIJO;			
			//Miramos si el valor que se generara en blanco contiene una PK para mas tarde recuperar el equivalente de SHP 
			Enumeration keys = primaryKeysBBDD.keys();
			while(keys.hasMoreElements()){
				String pkKey = (String) keys.nextElement();
				if(columnBBDD.getOriginName().equals(primaryKeysBBDD.get(pkKey).getName())){
					valor = VALOR_PK;
					value = pkKey;
					break;
				}
			}
			
			columnsBBDDEmpty.add(new ColumnInfo(columnBBDD.getOriginName(),
					columnBBDD.getDestinyName(), value, columnBBDD.getType(),
					valor));
		}
		return columnsBBDDEmpty;
	}
	
	public static ArrayList<ColumnInfo> generateColumnsSHPEmpty(
			ArrayList<ColumnInfo> columnsSHP, Hashtable<String, PrimaryKeyInfo> primaryKeysSHP) {
		ArrayList<ColumnInfo> columnsSHPEmpty = new ArrayList<ColumnInfo>();
		Iterator itSHP = columnsSHP.iterator();
		while (itSHP.hasNext()) {
			ColumnInfo columnSHP = (ColumnInfo) itSHP.next();
			String value = columnSHP.getValue();
			if(!columnSHP.getCharacteristic().equals(VALOR_FIJO)){
				if (columnSHP.getType().equals(TIPO_STRING) || columnSHP.getType().equals(TIPO_GEOMETRY))
					value = "";
				else
					value = "0";
			}			
			
//			String valor = VALOR_FIJO;			
//			Miramos si el valor que se generara en blanco contiene una PK para mas tarde recuperar el equivalente de BBDD 
//			Enumeration keys = primaryKeysSHP.keys();
//			while(keys.hasMoreElements()){
//				String pkKey = (String) keys.nextElement();
//				if(columnSHP.getOriginName().equals(primaryKeysSHP.get(pkKey).getName())){
//					valor = VALOR_PK;
//					value = pkKey;
//					break;
//				}
//			}
//			columnsSHPEmpty.add(new ColumnInfo(columnSHP.getOriginName(),
//					columnSHP.getDestinyName(), value, columnSHP.getType(),
//					valor));
		
			columnsSHPEmpty.add(new ColumnInfo(columnSHP.getOriginName(),
					columnSHP.getDestinyName(), value, columnSHP.getType(),
					VALOR_FIJO));
		}
		return columnsSHPEmpty;
	}

	private static void removeDuplicates(ArrayList<ColumnInfo> element, String columnName){
		
		for (int i=0;i<element.size();i++){
			ColumnInfo info=element.get(i);
			if (info.getDestinyName().equals(columnName)){
				element.remove(i);
				break;
			}
		}
	}
	
	private static boolean existeElemento(ArrayList<ColumnInfo> element, String columnName){
		
		for (int i=0;i<element.size();i++){
			ColumnInfo info=element.get(i);
			if (info.getDestinyName().equals(columnName)){
				return true;
			}
		}
		return false;
	}
	
	private static void replaceKeys(Properties properties, ArrayList<ColumnInfo> element){
		
		for (int i=0;i<element.size();i++){
			ColumnInfo info=element.get(i);
			info.getDestinyName();
			
			String originFilters = properties.getProperty(info.getDestinyName()+".filtro.origen");
			String destinyFilters = properties.getProperty(info.getDestinyName()+".filtro.destino");
			if ((originFilters!=null) && (destinyFilters!=null)){
				String[]filtrosOrigenes=originFilters.split(",");
				String[]filtrosDestinos=destinyFilters.split(",");
				for (int j=0;j<filtrosOrigenes.length;j++){
					String filtroOrigen=(String)filtrosOrigenes[j];
					if (filtroOrigen.equalsIgnoreCase(info.getValue())){
						info.setValue(filtrosDestinos[j]);
					}
				}
				
			}
		}
	}
	
	/**
	 * 
	 * @param columns
	 * @param elementsSHP
	 * @param elementsBBDD
	 * @param columnsBBDDEmpty
	 * @param columnsSHPEmpty
	 * @param primaryKeysSHP
	 * @param relation
	 * @param relationCharacteristic
	 * @param nonGeometryField
	 * @param loadMunicipality
	 * @param tableNameSHP
	 * @return
	 */
	public static ArrayList getElements(ArrayList<ColumnInfo> columns,
			ArrayList elementsSHP, ArrayList elementsBBDD,
			ArrayList<ColumnInfo> columnsBBDDEmpty,
			ArrayList<ColumnInfo> columnsSHPEmpty,
			Hashtable<String, PrimaryKeyInfo> primaryKeysSHP, int relation,
			String relationCharacteristic, String nonGeometryField,
			MunicipalityInfo loadMunicipality,String tableNameSHP,
			String nombreRelacion,Properties properties) {
		ArrayList elements = new ArrayList();

		//*********************************************************************
		// Obtenemos los elementos SHP relacionados con y sin relacion con BBDD
		//*********************************************************************
		Iterator<PrimaryKeysAndColumns> itSHP = elementsSHP.iterator();
		contador=0;
		if(!relationCharacteristic.equals(INNER_JOIN) && !relationCharacteristic.equals(RIGHT_JOIN)){		
			while (itSHP.hasNext()) {
				PrimaryKeysAndColumns primaryKeysAndColumnsSHP = itSHP.next();
	
				ArrayList<ColumnInfo> element = null;
				// Datos de SHP se cargan aunque no tengan correspondencia con BBDD
				element = new ArrayList<ColumnInfo>(primaryKeysAndColumnsSHP.getColumns());
				
				//En el SHP normalmente los datos bien sin traducir, los traducimos por si acaso
				//Por ejemplo en carreteras la sinuosidad viene como MAL DIMENSIONADA pero debe ser traducidad
				//e insertada como MD. Si no se hace esto al insertar en la BD fallaria.
				replaceKeys(properties,element);
				
				
				boolean fin = false;
				Iterator<PrimaryKeysAndColumns> itBBDD = elementsBBDD.iterator();
				
				
				Utils.numClavesCoincidentes=0;
				Utils.numClavesCoincidentesActual=0;	
				
				
				Utils.numClavesCoincidentesSinMatchContains=0;
				Utils.numClavesCoincidentesSinMatchContainsActual=0;
				while (itBBDD.hasNext() && fin != true) {
					//System.out.println("-------------------------");
					PrimaryKeysAndColumns primaryKeysAndColumnsBBDD = itBBDD.next();
					if (primaryKeysAndColumnsSHP.equals(primaryKeysAndColumnsBBDD,false,false)) {
						// Datos de SHP se cargan solo cuando tengan correspondencia con BBDD
						Iterator<ColumnInfo> itPkcBBDD = primaryKeysAndColumnsBBDD.getColumns().iterator();
						while (itPkcBBDD.hasNext()){
							
							//El MDB tiene preferencia si la columna ya se hubiera insertado en el SHP
							//la borramos
							ColumnInfo info=itPkcBBDD.next();
							removeDuplicates(element,info.getDestinyName());
							element.add(info);
						}
						fin = true;
					}
					else{
						if (numClavesCoincidentesActual>numClavesCoincidentes){
							infoMatch1.delete(0,infoMatch1.length());
							infoMatch1.append(infoMatchTemp);
							numClavesCoincidentes=numClavesCoincidentesActual;
						}
						//System.out.println("No coincide 1");
					}
				}
				
				Utils.numClavesCoincidentes=0;
				Utils.numClavesCoincidentesActual=0;
				
				Utils.numClavesCoincidentesSinMatchContains=0;
				Utils.numClavesCoincidentesSinMatchContainsActual=0;
				
				//Si no la encontramos en un matchAbsoluto lo buscamos en un match contains
				if (fin == false){
					PrimaryKeysAndColumns primaryKeysAndColumnsBBDDOptimo=null;
					itBBDD = elementsBBDD.iterator();
					while (itBBDD.hasNext() && fin != true) {						
						PrimaryKeysAndColumns primaryKeysAndColumnsBBDD = itBBDD.next();
						if (primaryKeysAndColumnsSHP.equals(primaryKeysAndColumnsBBDD,true,false)) {
							
							//Hemos encontrado una mejor coincidencia. La anotamos y seguimos iterando hasta
							//encontrar la mejor (La que tiene mas claves coincidentes puras (sin match contains)
							if (numClavesCoincidentesSinMatchContainsActual>numClavesCoincidentesSinMatchContains){
								primaryKeysAndColumnsBBDDOptimo=primaryKeysAndColumnsBBDD;
								//System.out.println("Claves coincidentes:"+Utils.numClavesCoincidentesSinMatchContainsActual+" Anterior:"+Utils.numClavesCoincidentesSinMatchContains);
								numClavesCoincidentesSinMatchContains=numClavesCoincidentesSinMatchContainsActual;
							}
							
							
							/*Iterator<ColumnInfo> itPkcBBDD = primaryKeysAndColumnsBBDD.getColumns().iterator();
							while (itPkcBBDD.hasNext())
								element.add(itPkcBBDD.next());
							fin = true;
							infoMatch3.delete(0,infoMatch3.length());
							infoMatch3.append(infoMatchTemp);
							*/
						}
						else{
							if (numClavesCoincidentesActual>numClavesCoincidentes){
								infoMatch2.delete(0,infoMatch2.length());
								infoMatch2.append(infoMatchTemp);

								numClavesCoincidentes=numClavesCoincidentesActual;
							}
							//System.out.println("No coincide 2");
						}
					}
					//Insertamos la informacion en el sistema con las claves coincidentes.
					
					if (primaryKeysAndColumnsBBDDOptimo!=null){
						
						//Ejecutamos la ultima sentencia para quedarnos con la traza buena y que salga en el
						//log en caso contrario salen las ultimas correspondencias que pueden estar mal.
						Utils.numClavesCoincidentesSinMatchContains=0;
						Utils.numClavesCoincidentesSinMatchContainsActual=0;
						primaryKeysAndColumnsSHP.equals(primaryKeysAndColumnsBBDDOptimo,true,false);
						//System.out.println("REVISION SHP: Claves coincidentes:"+Utils.numClavesCoincidentesSinMatchContainsActual+" Anterior:"+Utils.numClavesCoincidentesSinMatchContains);
							
						Iterator<ColumnInfo> itPkcBBDD = primaryKeysAndColumnsBBDDOptimo.getColumns().iterator();
						while (itPkcBBDD.hasNext()){
							ColumnInfo info=itPkcBBDD.next();
							removeDuplicates(element,info.getDestinyName());
							
							element.add(info);
						}
						fin = true;
						infoMatch3.delete(0,infoMatch3.length());
						infoMatch3.append(infoMatchTemp);
						infoMatch3.append("Claves coincidentes:"+numClavesCoincidentesSinMatchContainsActual);
					}
					

					//System.out.println("---");
				}
				
				
				
				
				if (fin == false) {
					if (!relationCharacteristic.equals(INNER_JOIN)){
						if (Utils.infoMatch2.length()>0){
							String titleMunicMsg = "--------------------- ERROR EN:" + tableNameSHP+"("+nombreRelacion+")" +" Municipio:"+loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
							loggerError.println(titleMunicMsg);
							loggerError.println("MATCH2:"+Utils.infoMatch2.toString());
						}
						else if (Utils.infoMatch1.length()>0){
							String titleMunicMsg = "--------------------- ERROR EN:" + tableNameSHP+"("+nombreRelacion+")" +" Municipio:"+loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
							loggerError.println(titleMunicMsg);
							loggerError.println("MATCH1:"+Utils.infoMatch1.toString());
						}
							
						//NUEVO
						//En caso de que no existiese registro de bd relacionado con geometria 
						//y una clave primaria se encontrase en dicha geometria
						//cambia los valores vacios por el valor equivalente en la geometria
						ArrayList<ColumnInfo> tempColumnsBBDDEmpty = new ArrayList<ColumnInfo>();						
						Iterator itColumnsBBDDEmpty = columnsBBDDEmpty.iterator();
						while(itColumnsBBDDEmpty.hasNext()){
							ColumnInfo columnInfo = (ColumnInfo) itColumnsBBDDEmpty.next();
							ColumnInfo tempColumnInfo = new ColumnInfo(columnInfo);
							if(tempColumnInfo.getCharacteristic().equals(VALOR_PK)){	
								String valor=primaryKeysAndColumnsSHP.getPrimaryKeys().get(tempColumnInfo.getValue());

								//Si hay mas de un elemento nos quedamos con el primero
								if (valor.contains(",")){
									valor=valor.substring(0,valor.indexOf(","));
								}
								tempColumnInfo.setValue(valor);
								tempColumnInfo.setCharacteristic(VALOR_FIJO);								
							}	
							
							if (!existeElemento(element,tempColumnInfo.getDestinyName()))
								tempColumnsBBDDEmpty.add(tempColumnInfo);
						}						
						element.addAll(tempColumnsBBDDEmpty);
						//FIN NUEVO
						
						//element.addAll(columnsBBDDEmpty);
						contador++;
						contadorTotal++;
					}
					else
						element = null;
				}
				else{
					
					if (infoMatch3.length()>0){
						String titleMunicMsg = "--------------------- CASADO CON MATCH CONTAINS EN:" + tableNameSHP+" Municipio:"+loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
						loggerContains.println(titleMunicMsg);						
						loggerContains.println("MATCH3:"+Utils.infoMatch3.toString());
					}

					//System.out.println("CASADO.....................");
					infoMatch1.delete(0,infoMatch1.length());
					infoMatch2.delete(0,infoMatch2.length());
					infoMatch3.delete(0,infoMatch3.length());
				}
				if (element != null)
					elements.add(element);
	
			}
		}
		if (contador>0){
			String titleMunicMsg = "\nWARN1 EN:" + tableNameSHP+"("+nombreRelacion+")" +" Municipio:"+loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
			Utils.print(titleMunicMsg);

			Utils.print("ERROR. Se han encontrado un total de elementos:"+contador+" que no casan");
		}
		
		
		//********************************************************************
		// Obtenemos elementos BBDD sin relacion con SHP (si la relacion no es
		// absoluta)
		//********************************************************************
		contadorBD=0;
		infoMatch3.delete(0,infoMatch3.length());
		if (!relationCharacteristic.equals(INNER_JOIN)) {
			Iterator<PrimaryKeysAndColumns> itBBDD = elementsBBDD.iterator();
			while (itBBDD.hasNext()) {
				PrimaryKeysAndColumns primaryKeysAndColumnsBBDD = itBBDD.next();
				ArrayList element = null;
				// Datos de SHP se cargan aunque no tengan correspondencia con
				// BBDD
				boolean fin = true;
				itSHP = elementsSHP.iterator();
				PrimaryKeysAndColumns primaryKeysAndColumnsSHP = null;
				
				while (itSHP.hasNext() && fin != false) {
					primaryKeysAndColumnsSHP = itSHP.next();
					if (primaryKeysAndColumnsBBDD.equals(primaryKeysAndColumnsSHP,false,true)) {
						fin = false;
					}
					
					/*else{
						System.out.println("OTRO");
					}*/
				}
				

				
				//Para el casado de Base de Dastos no comprobamos (Match Contains) porque
				//puede provocar problemas de registros que no sean correctos.
				/*if (fin == true){
					itSHP = elementsSHP.iterator();

					Utils.numClavesCoincidentesSinMatchContains=0;
					Utils.numClavesCoincidentesSinMatchContainsActual=0;
					
					PrimaryKeysAndColumns primaryKeysAndColumnsSHPOptimo=null;
					while (itSHP.hasNext() && fin != false) {
						primaryKeysAndColumnsSHP = itSHP.next();
						if (primaryKeysAndColumnsBBDD.equals(primaryKeysAndColumnsSHP,true,true)) {
							
							//Hemos encontrado una mejor coincidencia. La anotamos y seguimos iterando hasta
							//encontrar la mejor (La que tiene mas claves coincidentes puras (sin match contains)
							if (numClavesCoincidentesSinMatchContainsActual>numClavesCoincidentesSinMatchContains){
								primaryKeysAndColumnsSHPOptimo=primaryKeysAndColumnsSHP;
								//System.out.println("BD. Claves coincidentes:"+Utils.numClavesCoincidentesSinMatchContainsActual+" Anterior:"+Utils.numClavesCoincidentesSinMatchContains);
								numClavesCoincidentesSinMatchContains=numClavesCoincidentesSinMatchContainsActual;
							}
							
							fin = false;
							//infoMatch3.delete(0,infoMatch3.length());
							//infoMatch3.append(infoMatchTemp);
						}
						
						
					}
				}*/
				
				//Si no ha encontrado nada es que no existe correspondencia en el SHP.
				if (fin == true && columnsSHPEmpty != null) {
					contadorBD++;
					infoMatch3.append(primaryKeysAndColumnsBBDD+"\n");
						Hashtable<String, String> finalPrimaryKeys = new Hashtable<String, String>();
						Enumeration primaryKeysBBDDKeys = primaryKeysAndColumnsBBDD.getPrimaryKeys().keys();
						while (primaryKeysBBDDKeys.hasMoreElements()) {
							String primarykeyBBDDKey = (String) primaryKeysBBDDKeys.nextElement();
							PrimaryKeyInfo primaryKeyInfo = (PrimaryKeyInfo) primaryKeysSHP.get(primarykeyBBDDKey);
							finalPrimaryKeys.put(primaryKeyInfo.getName(),
									primaryKeysAndColumnsBBDD.getPrimaryKeys().get(primarykeyBBDDKey));
						}
	
						element = new ArrayList<ColumnInfo>();
						Iterator itSHPColumns = columnsSHPEmpty.iterator();
						while (itSHPColumns.hasNext()) {
							ColumnInfo columnSHP = (ColumnInfo) itSHPColumns.next();
							String value = "";
							if (finalPrimaryKeys.get(columnSHP.getOriginName()) != null)
								value = finalPrimaryKeys.get(columnSHP.getOriginName());
							else if (!columnSHP.getType().equals(TIPO_GEOMETRY))
								value = columnSHP.getValue();
	
							element.add(new ColumnInfo(columnSHP.getOriginName(),
									columnSHP.getDestinyName(), value, columnSHP
											.getType(), columnSHP
											.getCharacteristic()));
						}
	
						Iterator<ColumnInfo> itPkcBBDD = primaryKeysAndColumnsBBDD
								.getColumns().iterator();
						while (itPkcBBDD.hasNext()){
							
							//La MDB tiene prioridad
							ColumnInfo info=itPkcBBDD.next();
							removeDuplicates(element,info.getDestinyName());
							
							element.add(info);
						}
	
						element.add(new ColumnInfo(nonGeometryField,
								nonGeometryField, "-1", TIPO_STRING, ""));
									
				}
				
				
				
				if (element != null)
					elements.add(element);
			}
			
			if (contadorBD>0){
				String titleMunicMsg = "\nWARN2 EN:" + tableNameSHP+"("+nombreRelacion+")" +" Municipio:"+loadMunicipality.getName() + " (" + loadMunicipality.getIne() + ")" + " ---------------------";
				Utils.print(titleMunicMsg);

				//logger.println("ERROR. Se han encontrado un total de elementos en la BD:"+contador+" que no casan");
				Utils.print("ERROR. Se han encontrado un total de elementos en la BD:"+contadorBD+" que no casan");
				loggerError.print("ELEMENTO BD:"+infoMatch3);
			}
		}
		return elements;
	}

	public static ArrayList getElements(ArrayList<ColumnInfo> columns,
			ArrayList originElements, int relation) {

		ArrayList elements = new ArrayList();
		Iterator<PrimaryKeysAndColumns> it = originElements.iterator();
		while (it.hasNext()) {
			PrimaryKeysAndColumns primaryKeysAndColumns = it.next();
			ArrayList element = new ArrayList<ColumnInfo>(
					primaryKeysAndColumns.getColumns());
			if (element != null)
				elements.add(element);
		}
		return elements;
	}

	public static String getMunicipio(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation	+ ".destino.id_municipio");
	}

	public static String getRelationCharacteristic(Properties properties,
			int relation) {
		return properties.getProperty("relacion" + relation	+ ".origen.caracteristica", "");
	}

	public static String getSRID(Properties properties, String type,
			int relation) {
		return getSRID(properties, type, relation, "");
	}
	
	public static String getSRID(Properties properties, String type,
			int relation, String defaultValue) {
		return properties.getProperty("relacion" + relation + ".shp.srid"+ type,defaultValue);
	}

	public static String getNonGeometryField(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation	+ ".destino.camposingeometria");
	}

	public static String getOriginType(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation + ".origen.tipo",NO_EXISTE);
	}

	public static ArrayList generateInsert(String tableNameEIEL,
			ArrayList elements, int relation, String municipio,
			String id_municipio, String originSRID, String destinySRID) {
		ArrayList<String> listaInsert = new ArrayList<String>();
		try {
			Iterator itElements = elements.iterator();
			while (itElements.hasNext()) {
				StringBuffer insert = new StringBuffer("INSERT INTO ");
				insert.append(tableNameEIEL + " (");

				ArrayList element = (ArrayList) itElements.next();

				Iterator itElement = element.iterator();
				while (itElement.hasNext()) {
					ColumnInfo column = (ColumnInfo) itElement.next();
					insert.append(column.getDestinyName());
					if (itElement.hasNext())
						insert.append(", ");
				}

				if ((municipio != null) && !municipio.equals(""))
					insert.append(", " + municipio);
				insert.append(") VALUES (");

				itElement = element.iterator();
				while (itElement.hasNext()) {
					ColumnInfo column = (ColumnInfo) itElement.next();
					if (column.getType().equals(TIPO_STRING))
						if (column.getValue() != "")
							insert.append("'" + column.getValue().replace(",", ".")
									+ "'");
						else
							insert.append("' '");
					else if (column.getType().equals(TIPO_GEOMETRY))
						if (column.getValue() != "")
							insert.append("transform(geomfromtext('"
									+ column.getValue() + "'," + originSRID
									+ "), " + destinySRID + ")");
						else
							insert.append("null");
					else
						insert.append(column.getValue().replace(",", "."));
					if (itElement.hasNext())
						insert.append(", ");
				}

				if ((municipio != null) && !municipio.equals(""))
					insert.append(", " + id_municipio);

				insert.append(");");
				listaInsert.add(insert.toString());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return listaInsert;
	}

	public static ArrayList generateInsert(String tableNameEIEL,
			ArrayList elements, int relation, String municipio,
			String id_municipio) {
		ArrayList<String> listaInsert = new ArrayList<String>();
		try {
			Iterator itElements = elements.iterator();
			while (itElements.hasNext()) {
				StringBuffer insert = new StringBuffer("INSERT INTO ");
				insert.append(tableNameEIEL + " (");

				ArrayList element = (ArrayList) itElements.next();

				Iterator itElement = element.iterator();
				while (itElement.hasNext()) {
					ColumnInfo column = (ColumnInfo) itElement.next();
					insert.append(column.getDestinyName());
					if (itElement.hasNext())
						insert.append(", ");
				}

				if ((municipio != null) && !municipio.equals(""))
					insert.append(", " + municipio);
				insert.append(") VALUES (");

				itElement = element.iterator();
				while (itElement.hasNext()) {
					ColumnInfo column = (ColumnInfo) itElement.next();
					// Se reemplaza la coma por punto para decimales
					if (column.getType().equals(TIPO_STRING))
						insert.append("'" + column.getValue().replace(",", ".")
								+ "'");
					else
						insert.append(column.getValue().replace(",", "."));
					if (itElement.hasNext())
						insert.append(", ");
				}

				if ((municipio != null) && !municipio.equals(""))
					insert.append(", " + id_municipio);

				insert.append(");");
				listaInsert.add(insert.toString());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return listaInsert;
	}

	public static int totalElementsSHP(String ubicacion, Properties properties,
			int relation) {
		int total = 0;
		try {
			String nombreSHP = getOriginName(properties, SHP, relation);
			
			HashMap hashFiltros=getFiltro(properties,SHP,relation);
			
						
			FeatureCollection featureCollection = null;
			if (nombreSHP != null) {
				featureCollection = loadShapefile(ubicacion + nombreSHP,hashFiltros);
				if (featureCollection != null)
					total = featureCollection.size();
				logger.println("totalElementosSHP - " + nombreSHP + ": "
						+ total);
			}
		} catch (Exception e) {
			printLog(e.getMessage());
		}
		return total;
	}

	public static int totalElementsBBDD(Connection conn, Properties properties,
			int relation) {
		int total = 0;
		String where = properties.getProperty("relacion" + relation
				+ ".bbdd.origen.where");
		StringBuffer select = new StringBuffer("SELECT count(*) FROM ");
		String nombreTabla = getOriginName(properties, BBDD, relation);
		select.append(nombreTabla);
		if (where != null)
			select.append(" " + where);
		if (nombreTabla != null) {
			Statement stmt;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(select.toString());
				if (rs.next())
					total = rs.getInt(1);
			} catch (SQLException e) {
				print();
			} catch (NullPointerException e) {
				
			}
		}
		logger.println("totalElementosBBDD - " + nombreTabla + ": " + total);
		return total;
	}

	public static String getOriginName(Properties properties, String dataType,
			int relation) {
		return properties.getProperty("relacion" + relation + "." + dataType
				+ ".origen.nombre");
	}
	
	public static HashMap getFiltro(Properties properties, String dataType,int relation) {
		
		HashMap hashFiltros=null;
		String filtrorelacion=properties.getProperty("relacion" + relation + ".shp.filtros");
		if (filtrorelacion!=null){
			String[] listaFiltros=filtrorelacion.split(",");
			hashFiltros=new HashMap();
			for (int i=0;i<listaFiltros.length;i++){
				String filtro=listaFiltros[i];
				String[] separadores=filtro.split("=");
				hashFiltros.put(separadores[0],separadores[1]);				
			}
			return hashFiltros;
		}
		else
			return hashFiltros;
	}
	
	
	
	
	
	

	public static void closeLogFile() {
		try {
			logger.flush();
		} catch (Exception e) {
		}
		try {
			logger.close();
			logger = null;
		} catch (Exception e) {
		}
	}

	public static boolean doRelation(Properties properties, int relation,String relacionesFijas) {
		
		if (relacionesFijas!=null){
			String nombreRelacion = getName(properties, relation)+",";	
			//System.out.println("Nombre relacion:"+nombreRelacion);
		
			if ((nombreRelacion!=null) && (relacionesFijas.contains(nombreRelacion)))
				return true;
			else
				return false;
		}
		else{
			String hacer = getRelation(properties, relation);
			if ((hacer != null) && hacer.equals(SI))
				return true;
			else
				return false;
		}
	}

	public static String getName(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation + ".destino.nombre");
	}
	public static String getRelation(Properties properties, int relation) {
		return properties.getProperty("relacion" + relation + ".hacer");
	}

	public static String getDestinyName(Properties properties, int relation) {
		return properties
				.getProperty("relacion" + relation + ".destino.nombre","");
	}

	public static ArrayList getSQLQuerys(Properties properties, int relation,
			MunicipalityInfo municipialityInfo, String municipio, String srid) {
		ArrayList<String> sqlQuerys = new ArrayList<String>();
		int i = 1;
		boolean fin = false;
		String query;
		while (!fin) {
			query = properties.getProperty("relacion" + relation + ".sqlquery"
					+ i);
			if (municipio!=null){				
				if(municipio.equals(FIELD_ID_MUNICIPIO)){
					if(!query.contains("WHERE"))
						query += " WHERE ";
					else query += " AND ";
					query += municipio + "=" + municipialityInfo.getIne();
				}
				else if(municipio.equals(FIELD_CODPROV_CODMUNIC)){
					query = query.replace("#codprov#", municipialityInfo.getIneProvincia());
					query = query.replace("#codmunic#", municipialityInfo.getIneMunicipio());
				}
			}
			if(srid!=null)
				query = query.replace("#srid_municipio#", srid);
			if(!query.contains("#"))
				sqlQuerys.add(query);
			int siguiente = i + 1;
			if (properties.getProperty("relacion" + relation + ".sqlquery"
					+ siguiente) == null)
				fin = true;

			i++;
		}
		return sqlQuerys;
	}

	public static void printLogs(ArrayList resumen) {
		Iterator i = resumen.iterator();
		while (i.hasNext()) {
			printLog((String) i.next());
		}
	}

	public static void printLog(String texto) {
		logger.println(texto);
	}
	
	
	public static void print(){
		print("");
	}
	
	public static void print(String message){
		printLog(message);
		System.out.println(message);	
	}
}
