/**
 * I18NUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18NUtils {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(I18NUtils.class);

	public static String i18n_getname(String nombre)
	{
		return i18n_getname(nombre,ResourceBundle.getBundle("GeoPistai18n", Locale.getDefault()));
	}

    /**
     * 
     * @param nombre 
     * @param resource 
     * @return 
     */
	public static String i18n_getname(String nombre, ResourceBundle resource) 

	{
		ResourceBundle Rb = null;

		try
		{

			Rb = resource;

			nombre = nombre.replaceAll(" ", "");
			return Rb.getString(nombre);
		}
		catch (RuntimeException e)
		{
			// Si hay algún problema notifica en consola y deja el término sin
			// traducir

			logger.warn("i18n_getname(...) - TODO: Traducir \"" + nombre
					+ "\" en el locale:" + Locale.getDefault() + "\n ["
					+ where(5));
			logger.warn("Valor de resource:"+resource);
			logger.warn(e);
			System.out.println("Nombre:"+nombre);

			return nombre;
		}
	}

	/**
	 * 
	 * @param prof nivel de llamada a detallar si es 0 incluye toda la pila.
	 * @return
	 */
	public static final String where(int prof)
	{
		Exception ex=new Exception();
		StackTraceElement[] elems = ex.getStackTrace();
		if (prof==0)
		{
			StringWriter sw=new StringWriter();
			PrintWriter pw= new PrintWriter(sw);
			//sw.write( "TODO: Translate:\n");
			for (int i = 0; i < elems.length; i++)
			{
				StackTraceElement element = elems[i];
				sw.write("\tat ");
				sw.write(elems[prof].getClassName());
				sw.write(".");
				sw.write(elems[i].getMethodName());
				if (elems[i].getFileName()!=null)
				{
					sw.write("(" + elems[i].getFileName() + ":" + elems[i].getLineNumber() + ")");
				}
				sw.write("\n");
			}
//			ex.printStackTrace(pw);
			return sw.toString();
		}
		else

			return (elems.length < prof+1) ? "" : elems[prof].getFileName() + "@" + elems[prof].getLineNumber() + ":" + elems[prof].getClassName() + "."
					+ elems[prof].getMethodName() + "()";
	}
}
