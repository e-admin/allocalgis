package es.enxenio.util.sql;


/**
 * An implementation of <code>EntityIdentifierGenerator</code> for
 * PostgreSQL database.
 */
public class PostgreSQLEntityIdentifierGenerator 
	extends AbstractEntityIdentifierGenerator {
	
	protected String getQueryString(String sequenceName) {
		return "SELECT nextval('" + sequenceName + "')";
	}
}
