/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 23-may-2004 by juacas
 *
 * 
 * Created on 08-may-2004
 *
 */
package com.geopista.feature;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.satec.sld.SVG.SVGNodeFeature;

/**
 * Un NumberDomain representa un rango de valores numéricos
 * El formato se fija con dos apartados del pattern:
 * - opcional (puede devolver null) o obligatorio: ?
 * - Rango de validez: Ej. [-1.0:+Inf]
 * - Formato de representación de texto. Según TextFormat
 * 
 * PATTERN:Ejemplos:
 * 			?[minValue:maxValue]##.##
 * 			[minValue:maxValue]
 * 			?-##.00
 * 
 * minValue y maxValue puede tomar valores Double y +INF y -INF
 * Por defecto el rango es [-INF:INF]
 * 
 * @see	TextFormat
 * @author juacas
 *
 */
public class NumberDomain extends PatternBasedDomain {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = (Logger) Logger.getInstance(NumberDomain.class);

	private double minValue;
	private double maxValue;
	private static final String NUMBER_FORMAT_MSG = "NumberDomain.NumberFormatError";
	private static final String NUMBER_RANGE_MSG = "NumberDomain.NumberOutOfRangeError";

	public NumberDomain(String name, String Description) {
		super(name, Description);
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		return Domain.NUMBER;
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(com.vividsolutions.jump.feature.Feature, java.lang.String, java.lang.String)
	 */
	protected boolean validateLocal(SVGNodeFeature feature, String Name, Object value) {
		
		if (value==null) value=feature.getAttribute(Name);
    if(value==null || "".equals(value))
    	{
    	if (!isNullable())
    		setLastErrorMessage(NUMBER_FORMAT_MSG);
    	return isNullable();
    	}
		try
		{
		
		// JPC: intenté que DecimalFormat contrastara el formato del número pero funciona defectuosamente por:
		//  * interpreta la cadena como un flujo y se para en el primer grupo de números válido
		//  * permite que haya errores en medio del número.
		//  * No he podido encontrar un formato que excluya a los decimales para distinguir a los enteros. (Incluir en GeopistaSchema:182)
		
		 NumberFormat f = NumberFormat.getNumberInstance(Locale.getDefault());
		 if (f instanceof DecimalFormat) {
		 if (!"".equals(this.getFormat()))
		     ((DecimalFormat) f).applyPattern(this.getFormat());
		 }
		
		String repres=value.toString();
		ParsePosition pos=new ParsePosition(0);
		Number res = f.parse(repres,pos); // Tries to catch a format error by matching with the pattern
		if (pos.getIndex()<repres.length()) throw new ParseException("Formato erróneo.",pos.getIndex());
			
		
			double double_value = value instanceof Double?((Double)value).doubleValue():res.doubleValue();//Double.parseDouble(repres);
			if (!(double_value>=minValue && double_value<=maxValue))
				{
				setLastErrorMessage(NUMBER_RANGE_MSG);
				return false;
				}
			else
				{
				return true;
				}
			
		}catch (NumberFormatException e)
		{
			setLastErrorMessage(NUMBER_FORMAT_MSG);
		return false;
		}
		catch (ParseException e)
			{
			// Formato erróneo
			setLastErrorMessage(NUMBER_FORMAT_MSG);
			return false;
			}
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#setPattern(java.lang.String)
	 */
	public void setPattern(String pattern) {
		super.setPattern(pattern);
		pattern=this.pattern;
		// Analiza la cadena para configurar el dominio
		// el formato es [minval:maxval]###.###
		if (pattern.charAt(0) == '[') // hay rango
		{
			int tokenIndex = pattern.indexOf(':');
			try{
				
				
				String val = pattern.substring(1, tokenIndex);
				if (val.compareTo("-INF") == 0) setMinValue(Double.NEGATIVE_INFINITY);
				else setMinValue(Double.parseDouble(val));
				
				val = pattern.substring(tokenIndex + 1, pattern.indexOf(']'));
				if (val.compareTo("INF") == 0) setMaxValue(Double.POSITIVE_INFINITY);
				else setMaxValue(Double.parseDouble(val));
				
				tokenIndex = pattern.indexOf(']');
				this.setFormat(pattern.substring(tokenIndex + 1, pattern.length()));
			}catch(Exception ex)
			{
				logger.error("setPattern(String pattern = " + pattern
						+ ") - Dominio mal definido.", ex);
			}
		}
		else
		{
			setMaxValue(Double.POSITIVE_INFINITY);
			setMinValue(Double.NEGATIVE_INFINITY);
			setFormat(pattern);
		}
		int longt;
		NumberFormat nf=NumberFormat.getNumberInstance();
		longt = Math.max(nf.format(minValue).length(), nf.format(maxValue).length());
		if (longt<8) longt=8;
		setAproxLenght(longt);
		
	}
	/**
	 * @param d
	 */
	private void setMaxValue(double d) {
		maxValue=d;		
	}
	/**
	 * @param d
	 */
	private void setMinValue(double d) {
		minValue=d;		
	}
}
