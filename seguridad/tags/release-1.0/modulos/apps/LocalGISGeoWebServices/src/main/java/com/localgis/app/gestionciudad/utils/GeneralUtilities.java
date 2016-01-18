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
