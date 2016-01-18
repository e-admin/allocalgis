/**
 * EntityIdentifierGeneratorFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
