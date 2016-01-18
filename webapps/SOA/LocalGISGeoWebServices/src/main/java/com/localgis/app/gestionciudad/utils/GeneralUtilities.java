/**
 * GeneralUtilities.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.ConfigurationException;

import com.localgis.app.gestionciudad.beans.LocalGISIntervention;

public class GeneralUtilities {
	public static byte[] buildThumbnail(byte[] parentImage){
		//TODO: Generar thumbnail a partir de la imagen original
		return parentImage;
	}

	public static GregorianCalendar getNextCalendarWarning(LocalGISIntervention warning){
		LocalGISWarning warn = null;
		try {
			warn = new LocalGISWarning(warning.getPattern());
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(System.currentTimeMillis()));
		return warn.getNextCalendarWarning(calendar);
	}
}
