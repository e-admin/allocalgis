/**
 * ScheduleUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.taskscheduler.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScheduleUtils {

	/**
	 * Clase que invoca por reflexión el método de la clase de la tarea a ejecutar
	 * @param cls: Clase de la tarea a ejecutar
	 * @param method: Nombre del método de la tarea a ejecutar
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void executeClass(Class<?> cls, String method,String [] params) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method meth = cls.getMethod(method, String[].class);
	    //String[] params = null;
	    meth.invoke(null, (Object) params); 
	}
	
}
