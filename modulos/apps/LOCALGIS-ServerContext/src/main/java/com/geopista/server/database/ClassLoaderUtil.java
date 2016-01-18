/**
 * ClassLoaderUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.io.InputStream;

/**
 * Utilidad para instanciar clases del classloader.
 *
 */
public class ClassLoaderUtil {
	
	/** Cargador de clases. */
	private static final ClassLoader CLASS_LOADER = 
		ClassLoaderUtil.class.getClassLoader();


	/**
	 * Comprueba si una clase es accesible o no por el classloader.
	 * @param className nombre de la clase.
	 * @return true si la clase es accesible para el classloader; false en caso
	 *         contrario
	 */
	public static boolean findClass(String className) {
		boolean found = false;
		try {
			CLASS_LOADER.loadClass(className);
			found = true;
		} catch (ClassNotFoundException e) {
			found = false;
		}
		return found;
	}

	/**
	 * Devuelve una instancia de la clase cuyo nombre es pasado como parámetro.
	 * @param className nombre de la clase.
	 * @return objeto de la clase; null si no se pudo obtener instancia
	 * @exception ISPACException si el classloader no encuentra la clase
	 */
	public static Object getInstance(String className)  {
		Object result = null;
		try {
			Class clazz = CLASS_LOADER.loadClass(className);
			if (clazz != null) {
				result = clazz.newInstance();
			}
		} catch (Exception e) {
			
		}
		return result;
	}

	/**
	 * Devuelve una instancia de la clase cuyo nombre es pasado como parámetro.
	 * @param className nombre de la clase.
	 * @return objeto de la clase; null si no se pudo obtener instancia
	 * @exception ISPACException si el classloader no encuentra la clase
	 */
	public static Object getInstance(Class clazz) {
		Object result = null;
		try {
			if (clazz != null) {
				result = clazz.newInstance();
			}
		} catch (Exception e) {
			
		}
		return result;
	}

	/**
	 * Informa de si una clase implementa o no una determinada interfaz.
	 * @param className nombre de la clase
	 * @param interfaceName nombre de la interfaz
	 * @return true si la clase implementa interfaz; false en caso contrario
	 * @exception ISPACException si el classloader no encuentra la clase o no se puede
	 * obtener una instancia de la clase
	 */
	public static boolean implementsInterface(String className, 
			String interfaceName)  {

		Class clazz = null;

		try {
			clazz = CLASS_LOADER.loadClass (className);
		} catch (ClassNotFoundException e) {
		
		}

		if (clazz != null) {
			Class[] interfaces = clazz.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				if (interfaces[i].getName().equals(interfaceName)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Obtiene un recurso a partir de nombre.
	 * @param resourceName Nombre del recurso.
	 * @return IinputStream del recurso.
	 */
	public static InputStream getResourceAsStream(String resourceName) {
		return CLASS_LOADER.getResourceAsStream(resourceName);
	}


}

