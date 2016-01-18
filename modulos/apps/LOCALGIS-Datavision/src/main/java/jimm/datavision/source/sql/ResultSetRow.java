/**
 * ResultSetRow.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jimm.datavision.source.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jimm.datavision.ErrorHandler;
import jimm.datavision.source.Row;

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
