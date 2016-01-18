package es.enxenio.util.sql;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.exceptions.InternalErrorException;

// For testing.
//import javax.sql.DataSource;
//import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

/**
 * A factory to get <code>EntityIdentifierGenerator</code> objects.
 * <p>
 * Required configuration properties:
 * <ul>
 * <li><code>EntityIdentifierGeneratorFactory/generatorClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>EntityIdentifierGenerator</code>.</li>
 * </ul>
 */
public final class EntityIdentifierGeneratorFactory {
	
	private final static String GENERATOR_CLASS_NAME_PARAMETER =
		"EntityIdentifierGeneratorFactory/generatorClassName";
		
	private final static Class generatorClass = getGeneratorClass();
	
	private EntityIdentifierGeneratorFactory() {}
	
	private static Class getGeneratorClass() {
	
		Class theClass = null;
	
		try {
		
			String generatorClassName = 
				ConfigurationParametersManager.getParameter(
					GENERATOR_CLASS_NAME_PARAMETER);
			
			theClass = Class.forName(generatorClassName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return theClass;
		
	}
	
	public static EntityIdentifierGenerator getGenerator() 
		throws InternalErrorException {	
		
		try {		
        	return (EntityIdentifierGenerator) generatorClass.newInstance();
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		
	}

	/* 
	 * Test code. Uncomment for testing. Tests this class and any class
     * implementing "EntityIdentifierGenerator".
	 */
//	public static void main (String[] args) {
//
//		java.sql.Connection connection = null;	
//		
//		try {							
//		
//			/* Test. */	
//			DataSource dataSource = new SimpleDataSource();		
//			connection = dataSource.getConnection();
//			
//			EntityIdentifierGenerator generator = 
//				EntityIdentifierGeneratorFactory.getGenerator();
//			System.out.println("Generated identifier: " +
//				generator.nextIdentifier(connection, "Account"));
//				
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				GeneralOperations.closeConnection(connection);
//			} catch (InternalErrorException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}

}
