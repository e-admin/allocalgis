package jimm.datavision.source.sql;
import jimm.datavision.*;
import jimm.datavision.source.*;
import java.sql.*;
import java.util.*;

/**
 * A database knows about the tables in a database.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class Database extends DataSource {

protected static final String[] DB_OBJECT_TYPES = { "TABLE", "VIEW" };
protected String driverClassName;
protected String connInfo;
protected String name;
protected String username;
protected TreeMap tables;
protected HashMap tableCacheMap;
protected String schemaName;
//protected Connection conn;
/** Satec */
public Connection conn;
private String password;
protected boolean hasPassword;
protected boolean connectionOwnedByMe;
protected boolean storesLowerCaseIdentifiers;
protected boolean storesUpperCaseIdentifiers;

public Database(Connection conn, Report report) throws SQLException {
    super(report);

    query = new SQLQuery(report);
    this.driverClassName = "";
    this.connInfo = "";
    this.name = "";
    this.username = "";
    this.password = "";		// Not null so we won't bother asking
    hasPassword = true;
    tables = null;

    this.conn = conn;
    connectionOwnedByMe = false;
    loadAllTables();
}

/**
 * Constructor.
 *
 * @param driverClassName database driver class name
 * @param connInfo database connection info string
 * @param report the report using this database
 * @param name the database name
 * @param username the user name to use when logging in to the database
 * @param password the database password
 */
public Database(String driverClassName, String connInfo, Report report,
		String name, String user)
    throws SQLException, ClassNotFoundException, InstantiationException,
	   IllegalAccessException, UserCancellationException
{
    this(driverClassName, connInfo, report, name, user, null, false);
}

/**
 * Constructor.
 *
 * @param driverClassName database driver class name
 * @param connInfo database connection info string
 * @param report the report using this database
 * @param name the database name
 * @param username the user name to use when logging in to the database
 * @param password the database password
 */
public Database(String driverClassName, String connInfo, Report report,
		String name, String user, String password)
    throws SQLException, ClassNotFoundException, InstantiationException,
	   IllegalAccessException, UserCancellationException
{
    this(driverClassName, connInfo, report, name, user, password, true);
}


/**
 * Constructor.
 *
 * @param driverClassName database driver class name
 * @param connInfo database connection info string
 * @param report the report using this database
 * @param name the database name
 * @param username the user name to use when logging in to the database
 * @param password the database password
 * @param givenPassword if <code>true</code>, the password was passed in
 * to some other constructor
 */
protected Database(String driverClassName, String connInfo, Report report,
		   String name, String user, String password,
		   boolean givenPassword)
    throws SQLException, ClassNotFoundException, InstantiationException,
	   IllegalAccessException, UserCancellationException
{
    super(report);

    query = new SQLQuery(report);
    this.driverClassName = driverClassName;
    this.connInfo = connInfo;
    this.name = name;
    this.username = (user == null) ? "" : user;
    this.password = password;
    tables = null;
    hasPassword = givenPassword;

    initializeConnection();
    loadAllTables();
}

public boolean canJoinTables() { return true; }
public boolean isSQLGenerated() { return true; }
public boolean isConnectionEditable() { return true; }
public boolean areRecordsSelectable() { return true; }
public boolean areRecordsSortable() { return true; }
public boolean canGroupRecords() { return true; }

/**
 * Given an id (a column name), returns the column that has that id. If no
 * column with the specified id exists, returns <code>null</code>. Uses
 * <code>Table.findColumn</code>.
 *
 * @param id a column id
 * @return a column, or <code>null</code> if no column with the specified
 * id exists
 * @see Table#findColumn
 */
public Column findColumn(Object id) {
    if (tables == null)
	return null;

    String str = id.toString();
    int pos = str.lastIndexOf('.');
    String tableName = str.substring(0, pos);
    Table t = findTable(tableName);
    return t == null ? null : t.findColumn(id);
}

/**
 * Given a table name, find the table. The table name may have a schema name
 * or not. We look for the table first using <var>tableName</var> as-is,
 * then we use this database's schema name, then we try no schema name
 * at all.
 *
 * @param tableName a table name, perhaps including a schema name.
 */
//protected Table findTable(String tableName) {
/** Satec */
public Table findTable(String tableName) {
    // First try a simple exact match using tableCacheMap. This will often
    // fail the first time, but after we have found a table using the quite
    // convoluted search below we store the table in tableCacheMap.
    Table t = (Table)tableCacheMap.get(tableName);
    if (t != null)
	return t;

    String schemaName = null;
    int pos = tableName.indexOf('.');
    if (pos >= 0) {
	schemaName = tableName.substring(0, pos);
	tableName = tableName.substring(pos + 1);
    }

    boolean caseSensitive = getReport().caseSensitiveDatabaseNames();
    if (!caseSensitive) {
	if (schemaName != null) schemaName = schemaName.toLowerCase();
	tableName = tableName.toLowerCase();
    }

    // First try with table's schema name, if any.
    if (schemaName != null) {
	String target = schemaName + '.' + tableName;
	for (Iterator iter = tables.keySet().iterator(); iter.hasNext(); ) {
	    String key = (String)iter.next();
	    if (caseSensitive) {
		if (key.equals(target)) {
		    t = (Table)tables.get(key);
		    tableCacheMap.put(tableName, t); // Store table in cache
		    return t;
		}
	    }
	    else {
		if (key.toLowerCase().equals(target.toLowerCase())) {
		    t = (Table)tables.get(key);
		    tableCacheMap.put(tableName, t); // Put in cache
		    return t;
		}
	    }
	}
    }

    // Now try with database's schema name if it's different from the
    // table's schema name.
    if (name != null && !name.equals(schemaName)) {
	String target = name + '.' + tableName;
	for (Iterator iter = tables.keySet().iterator(); iter.hasNext(); ) {
	    String key = (String)iter.next();
	    if (caseSensitive) {
		if (key.equals(target)) {
		    t = (Table)tables.get(key);
		    tableCacheMap.put(tableName, t);
		    return t;
		}
	    }
	    else {
		if (key.toLowerCase().equals(target.toLowerCase())) {
		    t = (Table)tables.get(key);
		    tableCacheMap.put(tableName, t);
		    return t;
		}
	    }
	}
    }

    // Finally, try with no schema name.
    String target = tableName;
    for (Iterator iter = tables.keySet().iterator(); iter.hasNext(); ) {
	String key = (String)iter.next();
	if (caseSensitive) {
	    if (key.equals(target)) {
		t = (Table)tables.get(key);
		tableCacheMap.put(tableName, t);
		return t;
	    }
	}
	else {
	    if (key.toLowerCase().equals(target.toLowerCase())) {
		t = (Table)tables.get(key);
		tableCacheMap.put(tableName, t);
		return t;
	    }
	}
    }

    return null;
}

public Iterator tables() {
    return tables.values().iterator();
}


public Iterator tablesUsedInReport() {
        return ((SQLQuery)query).getTablesUsed().iterator();
}

public Iterator columns() {
    return new ColumnIterator(tables.values().iterator());
}

public Row execute() throws SQLException {
    //return new ResultSetRow(conn, (SQLQuery)query);
    /** Satec - annadimos el parametro oracle, que determina si la conexion es Oracle */
    return new ResultSetRow(conn, (SQLQuery)query, ((driverClassName != null) && (driverClassName.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")))?true:false);
}



public boolean storesLowerCaseIdentifiers() {
    return storesLowerCaseIdentifiers;
}

public boolean storesUpperCaseIdentifiers() {
    return storesUpperCaseIdentifiers;
}

/**
 * Initializes the connection to the database.
 *
 * @return a connection to the database
 */
public void initializeConnection()
    throws ClassNotFoundException, InstantiationException,
	   IllegalAccessException, UserCancellationException
{
    // Keep trying until we succeed. We will only exit if we succeed,
    // if the user cancels, or if something bad happens while obtaining
    // the database connection.
    boolean ok = false;
    while (!ok) {
	if (!hasPassword) {
	    // Calling askForPassword sets our user name and password
	    report.askForPassword(this);
	    hasPassword = true;
	}
	if (password == null)
	    throw new UserCancellationException(I18N.get("Database.cancelled"));

	try {
	    if (connInfo == null || connInfo.length() == 0)
		throw new IllegalArgumentException(I18N.get("Database.missing_conn_info"));
	    if (username == null || username.length() == 0)
		throw new IllegalArgumentException(I18N.get("Database.missing_user_name"));
	    if (password == null)
		throw new IllegalArgumentException(I18N.get("Database.null_password"));

	    // Load the database JDBC driver
	    Driver d = (Driver)Class.forName(driverClassName).newInstance();
	    DriverManager.registerDriver(d);

	    // Connect to the database
	    conn = DriverManager.getConnection(connInfo, username, password);
	    connectionOwnedByMe = true;

	    ok = true;
	}
	catch (SQLException sqle) {	// Force another password request
	    ErrorHandler.error(sqle);
	    hasPassword = false;
	    report.setDatabasePassword(null);
	}
    }
}

/**
 * Returns a connection to the database.
 *
 * @return a connection to the database
 */
public Connection getConnection() { return conn; }

/**
 * Reset key instance variables, closes current connection, and "reloads"
 * all table information (compares new info with existing info and complains
 * if any existing info is not in the new info).
 * <p>
 * <em>Note:</em> if the connection we currently have was created by this
 * object, we close it. If the connection was handed to us, we do
 * <em>not</em> close the connection.
 *
 * @param driverClassName database driver class name
 * @param connInfo database connection info string
 * @param dbName database name
 * @param username the user name to use when logging in to the database
 */
public void reset(String driverClassName, String connInfo, String dbName,
		  String username, String password)
    throws SQLException, ClassNotFoundException, InstantiationException,
	   IllegalAccessException, UserCancellationException
{
    setDriverClassName(driverClassName);
    setConnectionInfo(connInfo);
    setName(dbName);
    setUserName(username);
    this.password = password;
    hasPassword = true;

    if (conn != null) {
	if (connectionOwnedByMe)
	    conn.close();
	conn = null;
    }
    initializeConnection();
    loadAllTables();

    report.reloadColumns();
}

/**
 * Loads information about all tables in the database. If no tables are
 * found when using the database schema name, try again with a
 * <code>null</code> schema name.
 */
protected void loadAllTables() throws SQLException {
    tables = new TreeMap();
    tableCacheMap = new HashMap();
    schemaName = null;

    DatabaseMetaData dbmd = getConnection().getMetaData();
    storesLowerCaseIdentifiers = dbmd.storesLowerCaseIdentifiers();
    storesUpperCaseIdentifiers = dbmd.storesUpperCaseIdentifiers();

    try {
	// Specify both schema name and DB_OBJECT_TYPES.
	loadTablesUsingSchemaNameAndTypes(dbmd, name, DB_OBJECT_TYPES);
    } catch (SQLException e) {}
    catch (NullPointerException npe) {}

    try {
	if (tables.isEmpty() && name != null) // Only schema name
	    loadTablesUsingSchemaNameAndTypes(dbmd, name, null);
    } catch (SQLException e) {}
    catch (NullPointerException npe) {}

    try {
	if (tables.isEmpty())	// No schema name, use types
	    loadTablesUsingSchemaNameAndTypes(dbmd, null, DB_OBJECT_TYPES);
    } catch (SQLException e) {}
    catch (NullPointerException npe) {}

    // If no tables found, try again with null database name. This time,
    // throw an exception if there is a problem.
    if (tables.isEmpty())	// No schema name, no types
	loadTablesUsingSchemaNameAndTypes(dbmd, null, null);
}

/**
 * Loads our list of tables using a database meta data object and a
 * schema name. The schema name may be <code>null</code>.
 *
 * @param dbmd the database meta data object
 * @param objectTypes a list of database object types
 * @param schema the schema name; may be <code>null</code>
 */
protected void loadTablesUsingSchemaNameAndTypes(DatabaseMetaData dbmd,
						 String schema,
						 String[] objectTypes)
    throws SQLException
{
    ResultSet rset = dbmd.getTables(null, schema, "%", objectTypes);
    if (rset == null) return;

    boolean schemaNameFailed = false; // Avoid banging our head against a wall
    while (rset.next()) {
	String name = rset.getString("TABLE_NAME").trim();

	if (!schemaNameFailed) {
	    try {
		schemaName = rset.getString("TABLE_SCHEM");
	    }
	    catch (SQLException sqle) {
		schemaNameFailed = true;
	    }
	    if (schemaName != null && schemaName.length() > 0)
		name = schemaName.trim() + '.' + name;
	}

	SQLTable t = new SQLTable(this, name, dbmd);
	tables.put(t.getId().toString(), t);
    }
    rset.close();
}

/**
 * Returns the driver class name.
 *
 * @return the driver class name
 */
public String getDriverClassName() { return driverClassName; }

/**
 * Sets the driver class name.
 *
 * @param newDriverClassName the driver class name
 */
protected void setDriverClassName(String newDriverClassName) {
    driverClassName = newDriverClassName;
}

/**
 * Returns the connection info string.
 *
 * @return the connection info string
 */
public String getConnectionInfo() { return connInfo; }

/**
 * Sets the connection info string.
 *
 * @param newConnectionInfo the connection info string
 */
protected void setConnectionInfo(String newConnectionInfo) {
    connInfo = newConnectionInfo;
}

/**
 * Returns the database name.
 *
 * @return the database name
 */
public String getName() { return name; }

/**
 * Sets the name.
 *
 * @param newName the new name
 */
protected void setName(String newName) { name = newName; }

/**
 * Returns the user name.
 *
 * @return the user name
 */
public String getUserName() { return username; }

/**
 * Sets the user name.
 *
 * @param newUserName the new user name
 */
public void setUserName(String newUserName) { username = newUserName; }

/**
 * Returns the password.
 *
 * @return the password
 */
public String getPassword() { return hasPassword ? password : null; }

/**
 * Sets the password.
 *
 * @param newPassword the new password
 */
public void setPassword(String newPassword) {
    password = newPassword;
    hasPassword = true;
}

/**
 * Writes this database and all its tables as an XML tag.
 *
 * @param out a writer that knows how to write XML
 */
protected void doWriteXML(XMLWriter out) {
    out.startElement("database");
    out.attr("driverClassName", driverClassName);
    out.attr("connInfo", connInfo);
    out.attr("name", name);
    out.attr("username", username);
    out.endElement();
}

}
