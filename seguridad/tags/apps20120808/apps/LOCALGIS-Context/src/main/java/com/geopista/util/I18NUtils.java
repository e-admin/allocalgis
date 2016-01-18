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
