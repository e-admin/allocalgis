/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * DriverPool.java
 * 
 * Created on 25 maggio 2004, 16.34
 *
 */

package it.businesslogic.ireport.connection;

/*
 * Copyright 2002 by Mark A. Kobold
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is iSQL-Viewer, A Mutli-Platform Database Tool.
 *
 * The Initial Developer of the Original Code is Markus A. Kobold.
 *
 * Portions created by Mark A. Kobold are
 * Copyright (C) Copyright (C) 2000 Mark A. Kobold. All Rights Reserved.
 * Contributor(s): Mark A. Kobold  <mkobold@sprintpcs.com>.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://isql.sourceforge.net/
 */
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility object for loading and pooling JDBC drivers.
 * <p>
 *  This class was originally embedded into the DatabaseConnection object, it has
 *  been moved out for clarity. The main goal of this class is to provide a few of the 
 *  same functions provided java.sql.DriverManager object but in a way that is better 
 *  suited for the iSQL-Viewer program.
 * <p>
 *  The main shortcoming of the java.sql.DriverManager is that the driver manager
 *  will not allow you to use a registered driver that was loaded with a different
 *  classloader than classloader from the calling class. Basically in short terms
 *  the default DriverManager really only seems to work when all the code and JDBC
 *  drivers are from the same classloader, which doesn't work with the iSQL-Viewer
 *  system since there are runtime and classloader for each ServiceDefinition object.
 * 
 * @see java.sql.DriverManager
 * @author Markus A. Kobold  &lt;mkobold at sprintpcs dot com&gt;
 */
public final class DriverPool {
	private DriverPool(){
		
	}
	
	// This is a collection of Driver Objects that are registered through the
	// RegisterDriver method.
	private static final ArrayList driverSet = new ArrayList();
	/**
	 * Check method to ensure we don't have multiple instances of the same
	 * driver.
	 * <p>
	 * If the Major version and the Minors version of the Driver plus the class
	 * names are the same the the driver will be considerd all ready
	 * registered.
	 * <p>
	 * This method is generally called from the registerDriver() method.
	 * 
	 * @param jdbcDriver Driver Instance to check.
	 * @return true if similar class is registered
	 * @see #registerDriver(String, ClassLoader)
	 */
	private static boolean isDriverAllReadyRegistered(Driver jdbcDriver) {
		int verCheckSum = (jdbcDriver.getMajorVersion() * 10) + jdbcDriver.getMinorVersion();
		Iterator drivers = driverSet.iterator();
		while (drivers.hasNext()) {
			Driver driver = (Driver) drivers.next();
			int checkSum = (driver.getMajorVersion() * 10) + driver.getMinorVersion();
			if (checkSum == verCheckSum && (driver.getClass().getName().equals(jdbcDriver.getClass().getName())))
				return true;
		}
		return false;
	}

	/**
	 * Simple Driver registration method.
	 * <p>
	 * To overcome the security configuration of the java.sql.DriverManager
	 * object this method provides simple instantiation of the given driver by
	 * class name with a given ClassLoader. The java.sql.DriverManager works
	 * really only if all classes are under the same ClassLoader, for most
	 * cases this is not allways an option.
	 * <p>
	 * If a Driver is all ready registered this method will actually do
	 * nothing.
	 * 
	 * @param driverClass the fully qaulified name of the Driver e.g.
	 *            'org.my.sql.Driver'
	 * @param cl ClassLoader to try and load the Driver with.
	 * @throws ClassNotFoundException if an Error Occurs with creating the
	 *             Driver instance.
	 * @see #deregisterDriver(Driver)
	 * @see #getDriver(String)
	 */
	public static void registerDriver(String driverClass, ClassLoader cl) throws ClassNotFoundException {
		if (cl == null)
			cl = Thread.currentThread().getContextClassLoader();

		if (driverClass == null) return;

		try {
			Driver jdbcDriver = (Driver) Class.forName(driverClass, false, cl).newInstance();
			if (!isDriverAllReadyRegistered(jdbcDriver)) {
				synchronized (driverSet) {
					driverSet.add(jdbcDriver);
				}
				String[] p = new String[] { driverClass, jdbcDriver.toString()};


			}
		} catch (Throwable t) {
			throw new ClassNotFoundException(driverClass);
		}
	}

	/**
	 * Deregisters JDBC Driver object by refrence.
	 * <p>
	 * This will search through all registered drivers and remove the driver if
	 * the object refrences are the same.
	 * 
	 * @param jdbcDriver Driver Object that needs to be deregistered.
	 * @see #registerDriver(String,ClassLoader)
	 */
	public static void deregisterDriver(Driver jdbcDriver) {
		if (jdbcDriver == null) {
			return;
		}

		try {

			String[] p = new String[] { jdbcDriver.getClass().getName(), jdbcDriver.toString()};
			synchronized (driverSet) {
				int i = 0;
				for (i = 0; i < driverSet.size(); i++) {
					Driver di = (Driver) driverSet.get(i);
					if (di == jdbcDriver) {
						break;
					}
				}

				// If we can't find the driver just return.
				if (i >= driverSet.size()) {
					return;
				}

				driverSet.remove(i);
			}
		} catch (Throwable t) {
                    t.printStackTrace();
                }
	}

	/**
	 * Retrieval method for getting a driver by URL.
	 * <p>
	 * This will iterate through all registered drivers and if the value
	 * returned by the acceptsURL() of the driver is true then the driver is
	 * returned.
	 * <p>
	 * if all internally registered drivers fail against the URL then an
	 * attempt to fall back to the original DriverManager and attempt to
	 * retrieve a driver from there will be made.
	 * 
	 * @param jdbcURL URL of JDBC Connection
	 * @return Instance of the JDBC Driver that can accept the given URL
	 * @throws SQLException if the java.sql.DriverManager fallback fails.
	 */
	public static Driver getDriver(String jdbcURL) throws SQLException {
		Iterator drivers = driverSet.iterator();
		while (drivers.hasNext()) {
			Driver jdbcDriver = (Driver) drivers.next();
			try {
				if (jdbcDriver.acceptsURL(jdbcURL))
					return jdbcDriver;
			} catch (Throwable t) {
			}

		}
		return null; //DriverPool.getDriver(jdbcURL);
	}

}
