package jimm.datavision.source.sql;
import jimm.datavision.ErrorHandler;
import jimm.datavision.source.Row;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

/**
 * A concrete subclass of <code>Row</code> that wraps a JDBC result set.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class ResultSetRow extends Row {

protected PreparedStatement stmt;
protected ResultSet rset;
protected int numSelectables;
protected boolean noMoreData;

/** Satec */
private boolean isOracle= false;
/**/

ResultSetRow(Connection conn, SQLQuery query) throws SQLException {
    // Suggested by Konstantin. Though it works for his Oracle driver,
    // it doesn't work for my PostgreSQL driver. These args are also
    // legal for prepared statements.
    //  	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
    //  				    ResultSet.CONCUR_READ_ONLY);

    String preparedStmtString = query.toPreparedStatementString();
    if (preparedStmtString != null && preparedStmtString.length() > 0) {
	stmt = conn.prepareStatement(preparedStmtString);
	query.setParameters(stmt);
	rset = stmt.executeQuery();
	numSelectables = query.getNumSelectables();
	noMoreData = false;
    }
    else {
	numSelectables = 0;
	noMoreData = true;
    }
}
   /** Satec */
   /*   Permite visualizar correctamente los tipos de dato oracle.sql.TIMESTAMP
        al ejecutar la plantilla desde la propia herramienta de Datavision.
   */
    ResultSetRow(Connection conn, SQLQuery query, boolean oracle) throws SQLException {
        // Suggested by Konstantin. Though it works for his Oracle driver,
        // it doesn't work for my PostgreSQL driver. These args are also
        // legal for prepared statements.
        //  	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        //  				    ResultSet.CONCUR_READ_ONLY);

        isOracle= oracle;
        String preparedStmtString = query.toPreparedStatementString();
        if (preparedStmtString != null && preparedStmtString.length() > 0) {
        stmt = conn.prepareStatement(preparedStmtString);
        query.setParameters(stmt, isOracle);
        rset = stmt.executeQuery();
        numSelectables = query.getNumSelectables();
        noMoreData = false;
        }
        else {
        numSelectables = 0;
        noMoreData = true;
        }
    }

public List readRowData() {
    // Avoid calling rset.next() if it has already returned false. Doing so
    // appears harmless in most cases but seems to be causing a problem
    // with at least one user's DB2 JDBC driver.
    if (noMoreData)
	return null;

    try {
	if (!rset.next()) {
	    noMoreData = true;
	    return null;
	}
    }
    catch (SQLException sqle) {
	ErrorHandler.error(sqle);
	return null;
    }

    ArrayList list = new ArrayList();
    try {
        /* Satec - estaba asi
        for (int i = 1; i <= numSelectables; ++i)
            list.add(rset.getObject(i));
        */

        /** Satec - Permite visualizar correctamente los tipos de dato oracle.sql.TIMESTAMP
            al ejecutar la plantilla desde la propia herramienta de Datavision.
        */
        for (int i= 1; i <= numSelectables; ++i){
            if (isOracle){
                if ((rset.getObject(i) != null) && (rset.getObject(i) instanceof oracle.sql.TIMESTAMP)){
                    list.add(oracle.sql.TIMESTAMP.toDate(((oracle.sql.TIMESTAMP)rset.getObject(i)).getBytes()));
                }
            }else list.add(rset.getObject(i));
        }
    }
    catch (SQLException sqle) {
	ErrorHandler.error(sqle);
    }
    return list;
}

public void close() {
    try {
	if (rset != null) rset.close();
	if (stmt != null) stmt.close();
    }
    catch (SQLException sqle) {
	ErrorHandler.error(sqle);
    }
    finally {
	rset = null;
	stmt = null;
    }
}

}
