/**
 * UtilsEspacioPublico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.gestionciudad.utils;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.feature.Feature;

/**
 * Clase de utilidades que implementa fuciones que pueden usar varias clases. Clase estatica
 * que puede ser accesible desde la parte cliente.
 * */

public class UtilsEspacioPublico
{
	
	private static Logger logger = Logger.getLogger(UtilsEspacioPublico.class);
	@SuppressWarnings("unchecked")
	

	/**
	 * Metodo que devuelve un String con la fecha del dia actual, con formato DD/MM/AAAA.
	 *
	 * @return String La fecha actual.
	 */
	public static String showToday()
	{
		Calendar cal = new GregorianCalendar();
		Locale locale = Locale.getDefault();
		cal = Calendar.getInstance(TimeZone.getTimeZone(locale.toString()));
		int anno = cal.get(Calendar.YEAR);
		int mes = cal.get(Calendar.MONTH) + 1; // 0 == Enero
		int dia = cal.get(Calendar.DAY_OF_MONTH);
		String sMes = "";
		String sDia = "";
		if (mes < 10)
			sMes = "0" + mes;
		else
			sMes = "" + mes;
		if (dia < 10)
			sDia = "0" + dia;
		else
			sDia = "" + dia;

		return sDia + "/" + sMes + "/" + anno;
	}

	/**
	 * Metodo que no permite que la pantalla se minimize mas de 600 en largo y del ancho pasado por parametro.
	 *
	 * @param desktop La ventana a analizar.
	 * @param wid El ancho pasado.
	 * @param hig El largo pasado.
	 */
	public static void ajustaVentana(JFrame desktop, int wid, int hig)
	{
		if (desktop.getWidth()<wid || desktop.getHeight()<600)
		{
			desktop.setSize(wid, hig);
		}
	}



	/**
	 * Metodo que añade un asterisco a la derecha a un String.
	 *
	 * @param s String al que añadir el asterisco.
	 * @return String El resultado de añadir el asterisco.
	 */
	public static String annadirAsterisco(String s)
	{
		return "<html><p>" + s + " "+  "<FONT COLOR=\"#FF0000\" size=\"5\"><b>*</b></FONT></p></html>";
	}

	/**
	 * Metodo que añade un asterisco a la izquierda de un label.
	 *
	 * @param s String al que añadir el asterisco.
	 * @return String El resultado de añadir el asterisco
	 */
	public static String getLabelConAsterisco(String s)
	{
		return "<html><p><FONT COLOR=\"#FF0000\">*</FONT>" + " " + s + "</p></html>";
	}



	/**
	 * Metodo que borra caracteres de componentes editables.
	 *
	 * @param comp El componente en el que borrar los caracteres.
	 * @param maxLong El tamaño maximo que se quiere en el componente.
	 * */
	public static void retrocedeCaracter(final JTextComponent comp, final int maxLong)
	{
		Runnable checkLength = new Runnable()
		{
			public void run()
			{
				comp.setText(comp.getText().substring(0,maxLong));
			}
		};
		SwingUtilities.invokeLater(checkLength);
	}


	/**
	 * Devuelve un booleano indicando si un String tiene formato de fecha o no.
	 *
	 * @param fecha String de la fecha.
	 * @return boolean Indica si es una fecha o no.
	 * */


	/**
	 * Metodo que convierte una fecha en formato Date a una fecha en formato String de la forma DD/MM/AAAA
	 *
	 * @param fecha La fecha que se desea convertir.
	 * @return String El resultado.
	 * */
	public static String formatFecha(Date fecha)
	{
		if (fecha == null)
		{
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
	}

	/**
	 * Metodo que convierte una hora en formato Time a una hora en formato String de la forma HH:MM:SS
	 *
	 * @param hora La hora que se desea convertir.
	 * @return String El resultado.
	 * */
	public static String formatHora(Time hora)
	{
		if (hora == null)
		{
			return "";
		}
		return new SimpleDateFormat("HH:MM:SS").format(hora);
	}

	/**
	 * Metodo que devuelve un arrayList con features que concuerden con los parametros pasados.
	 *
	 * @param geopistaLayer Capa geopista donde buscar.
	 * @param attributeName El nombre del atributo buscado
	 * @param value El valor del atributo buscado.
	 * @return Collection El resultado.
	 * */
	@SuppressWarnings("unchecked")
	public static Collection<Feature> searchByAttribute(GeopistaLayer geopistaLayer, String attributeName, String value)
	{
		Collection<Feature> finalFeatures = new ArrayList<Feature>();

		List<Feature> allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
		Iterator<Feature> allFeaturesListIter = allFeaturesList.iterator();
		while (allFeaturesListIter.hasNext())
		{
			Feature localFeature = (Feature) allFeaturesListIter.next();

			String nombreAtributo = localFeature.getString(attributeName).trim();

			if (nombreAtributo.equals(value))
			{
				finalFeatures.add(localFeature);
			}
		}
		return finalFeatures;
	}

	/**
	 * Metodo que comprueba si un objeto es null, si es devuelve "", si no devuelve el objeto en formato string.
	 *
	 * @param o Objeto a analizar.
	 * @return String El obejto en formato string.
	 * */
	public static String checkNull(Object o)
	{
		if (o == null)
		{
			return "";
		}
		return o.toString();
	}

	public static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null){
			return AppContext.getApplicationContext().getMainFrame();
		}
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog){
			return (Window)parentComponent;
		}
		return getWindowForComponent(parentComponent.getParent());
	}
}
