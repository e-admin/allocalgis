package com.geopista.instalador.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.postgis.PGgeometry;
import org.postgresql.util.PSQLException;

import com.geopista.instalador.UTMED50ToGeoETRS89Converter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;

public class Ed50toetrs89Utils {

	/**
	 * Función que convierte de un objeto org.postgis.Geometry a un objeto
	 * com.vivid.solutions.jts.geom.Geometry.
	 * 
	 * @param pgGeometry
	 * @return objeto com.vivid.solutions.jts.geom.Geometry
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static Geometry convertGeometryPostGISToVividSolutions(
			PGgeometry pgGeometry) throws SQLException, ParseException {
		String geometryString = pgGeometry.getGeometry().toString();
	
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(1E10),
				pgGeometry.getGeometry().getSrid());
	
		WKTReader wktReader = new WKTReader(fact);
		Geometry geom;
		// ADD SRID
		if (geometryString.indexOf(';') != -1) {
			String[] temp = PGgeometry.splitSRID(geometryString);
	
			geom = (Geometry) wktReader.read(temp[1]);
			geom.setSRID(pgGeometry.getGeometry().getSrid());
		} else {
			geom = (Geometry) wktReader.read(geometryString);
		}
		return geom;
	}

	public static void updateTransformGeometryColumn(Connection con,
			String tabla, long uniqueKeyValue,String uniqueKeyName , PGgeometry pgGeometry,
			String geometryColumnName) throws SQLException, ParseException {
		PreparedStatement ps2 = null;
	
		try {
			
			
			Geometry geometry = convertGeometryPostGISToVividSolutions(pgGeometry);
		
			geometry = Ed50toetrs89Utils.convertGeometryED50ToETRS89(geometry);
			
			String sSQL = "update \"" + tabla + "\" set \""
					+ geometryColumnName + "\" = ST_GeomFromText('"
					+ geometry.toText() + "'," + geometry.getSRID()
					+ ") where "+uniqueKeyName+"=?";
			
			ps2 = con.prepareStatement(sSQL);
			ps2.setLong(1, uniqueKeyValue);
			ps2.execute();
			
		} finally {
	
			Ed50toetrs89Connection.closeConnection(null, ps2, null);
		}
	
	}

	public static void updateTableGeometryColumn(Connection con, String tabla,
			String geometryColumnName, int srid) throws SQLException,
			ParseException {
		PreparedStatement ps2 = null;
	
		try {
	
			String sSQL = "update \"geometry_columns\" set srid = ? where f_table_name = ? and f_geometry_column = ?";
			ps2 = con.prepareStatement(sSQL);
			ps2.setInt(1, srid);
			ps2.setString(2, tabla);
			ps2.setString(3, geometryColumnName);
	
			ps2.execute();
		} finally {
	
			Ed50toetrs89Connection.closeConnection(null, ps2, null);
		}
	
	}

	public static  void addGeometryColumn(Connection con, String tabla,
			String geometryColumnName) throws SQLException {
		PreparedStatement ps2 = null;
		try {
			// Se crea la nueva columna, que será la que
			// contenga los datos en geográficas ETRS89
			String sSQL = "alter table \"" + tabla + "\" add column \""
					+ geometryColumnName + "\" geometry";
			ps2 = con.prepareStatement(sSQL);
			ps2.execute();
			UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.7")
					+ tabla
					+ Messages.getString("UTMED50ToGeoETRS89Converter.135")
					+ geometryColumnName);
	
		} catch (SQLException e) {
			e.printStackTrace();
			UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.136")
					+ geometryColumnName);
			throw e;
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps2, null);
		}
	
	}

	/** Renombra la columna de tipo GEOMETRY para realizar la copia de los datos originales.
	 * Nueva columna es el mismo nombre añadiendo el sufijo old50
	 * @param con
	 * @param tabla
	 * @param geometryColumnName
	 * @throws SQLException
	 */
	public static void renameGeometryColumnToGeometryBackup(Connection con,
			String tabla, String geometryColumnName) throws SQLException {
		PreparedStatement ps2 = null;
		try {
			// Se renombra la columna, la cuál contendrá los
			// datos originales en geográficas ED50
			String sSQL = "alter table \"" + tabla + "\" rename column \""
					+ geometryColumnName + "\" to \""
					+ (geometryColumnName + UTMED50ToGeoETRS89Converter.BACUP_SUFFIX) + "\"";
			ps2 = con.prepareStatement(sSQL);
			ps2.execute();
	
			UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.125")
					+ geometryColumnName
					+ Messages.getString("UTMED50ToGeoETRS89Converter.6")
					+ tabla
					+ Messages.getString("UTMED50ToGeoETRS89Converter.127")
					+ (geometryColumnName + UTMED50ToGeoETRS89Converter.BACUP_SUFFIX));
	
		} catch (PSQLException e) {
	
			UTMED50ToGeoETRS89Converter.log.error(e);
			UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.128")
					+ geometryColumnName
					+ Messages.getString("UTMED50ToGeoETRS89Converter.129")
					+ tabla
					+ Messages.getString("UTMED50ToGeoETRS89Converter.130"));
			throw e;
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps2, null);
		}
	
	}
	
	public static void copyGeometryColumnToGeometryBackup(Connection con,
			String tabla, String geometryColumnName) throws SQLException {
		PreparedStatement ps2 = null;
		boolean autoCommit = true;
		
		try {
			
			autoCommit = con.getAutoCommit();
			con.setAutoCommit(false);
			
			UTMED50ToGeoETRS89Converter.printMessage("Creando Columna "+geometryColumnName+UTMED50ToGeoETRS89Converter.BACUP_SUFFIX+" para copia de seguridad");
			
			// Se renombra la columna, la cuál contendrá los
			// datos originales en geográficas ED50
			String sSQL = "ALTER TABLE "+tabla+" ADD COLUMN \""+geometryColumnName+UTMED50ToGeoETRS89Converter.BACUP_SUFFIX +"\" geometry";
					
			ps2 = con.prepareStatement(sSQL);
			ps2.execute();
	
			sSQL = "update "+tabla+" set \""+ geometryColumnName+UTMED50ToGeoETRS89Converter.BACUP_SUFFIX+ "\" = \"" + geometryColumnName+"\"";
			
			UTMED50ToGeoETRS89Converter.printMessage("Copiando contenido de la Columna "+geometryColumnName+" en la columna "+geometryColumnName+UTMED50ToGeoETRS89Converter.BACUP_SUFFIX);
			
			ps2 = con.prepareStatement(sSQL);
			ps2.execute();
	
			UTMED50ToGeoETRS89Converter.printMessage("Eliminando contenido de la Columna "+geometryColumnName);
			
			
			sSQL = "update "+tabla+" set \""+ geometryColumnName+ "\" = " + null;
			
			ps2 = con.prepareStatement(sSQL);
			ps2.execute();
			
			con.commit();
			
			UTMED50ToGeoETRS89Converter.printMessage("Columna " +geometryColumnName + " copiada correctamente en columna "+geometryColumnName+UTMED50ToGeoETRS89Converter.BACUP_SUFFIX);
			
	
		} catch (PSQLException e) {
	
			con.rollback();
			UTMED50ToGeoETRS89Converter.log.error(e);
			UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("La columna no se ha podido copiar, anulando conversión de la tabla "+tabla));
			throw e;
		} finally {
			con.setAutoCommit(autoCommit);
			Ed50toetrs89Connection.closeConnection(null, ps2, null);
		}
	
	}

	/** Busqueda de las tablas que contienen columnas de tipo GEOMETRY
	 * @param con
	 * @param geometryColumnName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTablesWithGeometryColumn(Connection con,
			String geometryColumnName) throws SQLException {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> tableList = new ArrayList<String>();
	
		UTMED50ToGeoETRS89Converter.printMessage(Messages.getString("UTMED50ToGeoETRS89Converter.110")
				+ geometryColumnName);
	
		try {
	
			String sqlTableNames = "select distinct table_name from information_schema.columns where data_type = 'USER-DEFINED' and (column_name = '"
					+ geometryColumnName
					+ "' or column_name = '"
					+ (geometryColumnName + UTMED50ToGeoETRS89Converter.BACUP_SUFFIX)
					+ "') and is_updatable = 'YES' order by table_name";
			ps = con.prepareStatement(sqlTableNames);
			rs = ps.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString("table_name");
				tableList.add(tableName);
			}
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps, rs);
		}
		return tableList;
	}

	/** Muestra un listado del nombre de columnas que se han encontrado en la BBDD de tipo GEOMETRY.
	 * Ej: columna: GEOMETRY
	 * @param con Conexion a la BBDD
	 * @throws SQLException
	 */
	public static void showDistinctGeometryColumns(Connection con) throws SQLException {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
	
			String sqlTableNames = "select distinct column_name from information_schema.columns where data_type = 'USER-DEFINED'  and is_updatable = 'YES'";
			ps = con.prepareStatement(sqlTableNames);
			rs = ps.executeQuery();
			while (rs.next()) {
	
				String geometryColumnName = rs.getString("column_name");
	
				UTMED50ToGeoETRS89Converter.printMessage("columna: " + geometryColumnName);
			}
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps, rs);
		}
	
	}

	/** Muestra un listado de las tablas que tienen alguna columna de tipo GEOMETRY
	 *  ej:  tabla: actividad_contaminante columna: GEOMETRY
	 * @param con Conexion a la BBDD
	 * @throws SQLException
	 */
	public static void showTablesWithGeometryColumn(Connection con)
			throws SQLException {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
	
			String sqlTableNames = "select table_name, column_name from information_schema.columns where data_type = 'USER-DEFINED'  and is_updatable = 'YES' order by table_name";
			ps = con.prepareStatement(sqlTableNames);
			rs = ps.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString("table_name");
				String geometryColumnName = rs.getString("column_name");
	
				UTMED50ToGeoETRS89Converter.printMessage(Messages
						.getString("UTMED50ToGeoETRS89Converter.4")
						+ tableName
						+ Messages.getString("UTMED50ToGeoETRS89Converter.5")
						+ geometryColumnName);
			}
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps, rs);
		}
	
	}
	
	/**  Devuelve el SRID al cual se desea convertir las geometrias
	 * @param sourceSRID
	 * @return
	 */
	public static int getTargetSrid(int sourceSRID) {
		int targetSRID = 0;
	
		if (sourceSRID == 4230) {
			targetSRID = 4258;
		} else if (sourceSRID == 23030) {
			targetSRID = 25830;
		} else if (sourceSRID == 23029) {
			targetSRID = 25829;
		} else if (sourceSRID == 23028) {
			targetSRID = 25828;
		}
		return targetSRID;
	}

	/**
	 * @param con
	 * @param tabla
	 * @param geometryColumnName
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkTableConversion(Connection con, String tabla,
			String geometryColumnName) throws SQLException {
		PreparedStatement psComprobarNulos = null;
		ResultSet rsComprobarNulos = null;
	
		String sqlRegistrosNulos = "select count(*) as resta from \"" + tabla
				+ "\" where \"" + geometryColumnName + "\" is null and \""
				+ (geometryColumnName + UTMED50ToGeoETRS89Converter.BACUP_SUFFIX) + "\" is not null";
	
		psComprobarNulos = con.prepareStatement(sqlRegistrosNulos);
		rsComprobarNulos = psComprobarNulos.executeQuery();
		int restaNulos = 0;
		if (rsComprobarNulos.next()) {
			restaNulos = rsComprobarNulos.getInt("resta");
	
		}
		if (restaNulos > 0) {
			return true;
		} else {
			return false;
		}
	}

	
	/** Conversion de los tipos de Geometry de ED50 a ETRS89
	 * @param geometry
	 * @return
	 * @throws ParseException
	 */
	public static Geometry convertGeometryED50ToETRS89(Geometry geometry)
			throws ParseException {
		// convertimos los tipos de Geometry con
		// esta función, para podre ser usado en
		// el método
		// Reprojector.instance().reproject
	
		int sourceSRID = geometry.getSRID();
		int targetSRID = getTargetSrid(sourceSRID);
	
		if (targetSRID == 0) {
			throw new ParseException(
					Messages.getString("UTMED50ToGeoETRS89Converter.3")
							+ sourceSRID);
		}
		final CoordinateSystem source = PredefinedCoordinateSystems
				.getCoordinateSystem(sourceSRID);
		final CoordinateSystem destination = PredefinedCoordinateSystems
				.getCoordinateSystem(targetSRID);
		Reprojector.instance().reproject(geometry, source, destination);
	
		return geometry;
	
	}

	/**
	 * Validaciones necesarias para las operaciones de modificación de las
	 * tablas.
	 * 
	 * @param con
	 * @param tabla
	 * @param campoTabla
	 * @param campoGeometry50
	 * @param campoGeometry
	 * @throws SQLException
	 */
	public static boolean[] validaciones(Connection con, String tabla,
			String geometryColumnName) throws SQLException {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean[] campos = new boolean[] { false, false };
		String campoTabla = null;
	
		try {
	
			String sqlNombreCampos = "SELECT a.attname AS nombrecampo FROM pg_class c, pg_attribute a "
					+ " WHERE a.attrelid = c.oid and a.attnum > 0 AND c.relname='"
					+ tabla + "'";
			ps = con.prepareStatement(sqlNombreCampos);
			rs = ps.executeQuery();
			while (rs.next()) {
				campoTabla = rs.getString("nombrecampo");
				if ((campoTabla.equals(geometryColumnName + UTMED50ToGeoETRS89Converter.BACUP_SUFFIX))) {
					campos[0] = true;
				}
				if ((campoTabla.equalsIgnoreCase(geometryColumnName))) {
	
					campos[1] = true;
				}
			}
		} finally {
			Ed50toetrs89Connection.closeConnection(null, ps, rs);
		}
		return campos;
	}
	
	public static List<String> getLstTablesNotInclude(String tablesNotIncludesTransform){
		
		return Arrays.asList(tablesNotIncludesTransform.split(";"));
		
	}
	

}
