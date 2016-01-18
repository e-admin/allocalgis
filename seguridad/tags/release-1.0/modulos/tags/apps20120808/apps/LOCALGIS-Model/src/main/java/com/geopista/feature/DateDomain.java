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
 * Created on 27-may-2004 by juacas
 *
 * 
 */
package com.geopista.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author juacas
 *
 * dominio que codifica una fecha. Permite especificar un rango de fechas válidas
 * incluyendo la fecha actual del sistema como referencia.
 * El formato del patrón especificación puede  ser:
 * [DATE1:DATE2]PATTERN
 * [DATE1:NOW]PATTERN
 * [NOW:DATE2]PATTERN
 * [DATE1:*]PATTERN
 * [*:DATE2]PATTERN
 * después se adjuntará el formato de presentación según SimpleDateFormat
 * @see java.text.SimpleDateFormat
 */
public class DateDomain extends PatternBasedDomain {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(DateDomain.class);

	public AttributeType getAttributeType()
	{
		
		return AttributeType.DATE;
	}
private Date initialDate; //inicio del rango
private Date finalDate; // final del rango
private static final String DATE_OUT_OF_RANGE_MSG="DateDomain.DateOutOfRangeError";
private static final String DATE_FORMAT_ERROR_MSG="DateDomain.DateFormatError";
SimpleDateFormat df= new SimpleDateFormat(); // Cuando se invoca toString en un Date se usa el formato largo de fecha y hora

  public DateDomain()
  {
    
  }
	/**
	 * @param pattern
	 * @param Description
	 */
	public DateDomain(String pattern, String Description) {
		super(pattern, Description);
		
	}
	public int getType() {
		return Domain.DATE;
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(com.vividsolutions.jump.feature.Feature, java.lang.String, java.lang.String)
	 */
	protected boolean validateLocal(Feature feature, String Name, Object valueObj) {
		
		Date date_value;
		
		
		try
		{
			if (valueObj==null)
				valueObj= feature.getAttribute(Name);

      //if temporal para pasar el problema de que el value en las capas nuevas en nulo
      if(valueObj==null) 
      	{
      	if (!isNullable())
      		setLastErrorMessage(Domain.NOT_NULLABLE_MSG);// asigna el error.
		
      	return isNullable();
      	}

          if (valueObj instanceof Date) 
              date_value=(Date)valueObj;
             else
              date_value=df.parse(valueObj.toString());// intenta parsear a través de un String
			
			boolean passed=true;
			if (initialDate!=null)
				passed=passed & initialDate.before(date_value);
			if (finalDate!=null)
				passed=passed & finalDate.after(date_value);
			if (!passed)
				setLastErrorMessage(I18N.getMessage(DateDomain.DATE_OUT_OF_RANGE_MSG,new Object[]{initialDate,finalDate}));// asigna el error por defecto.
			
			return passed;
		}catch (ParseException e)
		{
		setLastErrorMessage(I18N.get(DateDomain.DATE_FORMAT_ERROR_MSG));	
		return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#setPattern(java.lang.String)
	 */
	public void setPattern(String pattern) {
		// Analiza la cadena para configurar el dominio
		// el formato es [DATE1:DATE2]###.###
		super.setPattern(pattern);
		
		pattern=this.pattern;
		Date initialDate=null; // Valores temporales
		Date finalDate=null;
		int tokenIndex= pattern.indexOf(']'); // busca fin de rangos
		setFormat(pattern.substring(tokenIndex+1));
		if (getFormat().length()<5) 
			setAproxLenght(8); 
			else
			setAproxLenght(getFormat().length());
		try
		{
			
		if (pattern.charAt(0)=='[') // hay rango
		{
		 tokenIndex= pattern.indexOf(':');
		String val = pattern.substring(1,tokenIndex);
		if (val.equals("NOW"))
			initialDate= new Date();
		else
		if (val.equals("*"))
			initialDate=null;
		else
			initialDate=df.parse(val);
		
		val = pattern.substring(tokenIndex+1, pattern.indexOf(']'));
		if (val.equals("NOW"))
			finalDate= new Date();
		else
			if (val.equals("*"))
				finalDate=null;
			else
			finalDate=df.parse(val);
		
		}
		
		
		this.initialDate=initialDate;
		this.finalDate=finalDate;
		}catch(ParseException e){
			logger.error("setPattern(String)", e);
			throw new IllegalArgumentException("Formato erróneo de fecha:"+pattern);}
	}
	public void setFormat(String format)
	{
		
		super.setFormat(format);
		df=new SimpleDateFormat(format);
	
	}
	public Date getFinalDate()
	{
	return finalDate;
	}
	public Date getInitialDate()
	{
	return initialDate;
	}
}
