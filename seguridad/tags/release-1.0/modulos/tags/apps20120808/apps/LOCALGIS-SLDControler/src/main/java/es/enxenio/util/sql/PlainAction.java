package es.enxenio.util.sql;

import java.sql.Connection;

import es.enxenio.util.exceptions.InternalErrorException;
import es.enxenio.util.exceptions.ModelException;

/**
 * The base interface of all plain actions. A plain action is an bussiness
 * action executed with plain Java classes, using JDBC as database accesss
 * technology.
 */ 
public interface PlainAction {

	Object execute(Connection connection) throws ModelException,
		InternalErrorException;

}
