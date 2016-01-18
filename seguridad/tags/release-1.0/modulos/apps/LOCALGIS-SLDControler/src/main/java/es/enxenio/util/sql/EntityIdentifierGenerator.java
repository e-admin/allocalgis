package es.enxenio.util.sql;

import java.sql.Connection;

import es.enxenio.util.exceptions.InternalErrorException;

/**
 * Defines an interface to access the underlying identifier generator for
 * databases that come with one.
 */
public interface EntityIdentifierGenerator {

	/**
     * Gets an identifier for a given entity.
     */
    public Long nextIdentifier(Connection connection, 
		String entityName) throws InternalErrorException;   

}
