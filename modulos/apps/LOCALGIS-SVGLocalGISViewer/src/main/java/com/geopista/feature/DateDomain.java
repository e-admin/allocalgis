/**
 * DateDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.satec.sld.SVG.SVGNodeFeature;

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
	private static final Logger logger = (Logger) Logger.getInstance(DateDomain.class);

	private static final String DATE_FORMAT_MSG = "DateDomain.DateFormatError";
	private static final String DATE_RANGE_MSG = "DateDomain.DateRangeError";
	
	private Date initialDate; //inicio del rango
	private Date finalDate; // final del rango
	SimpleDateFormat df= new SimpleDateFormat(); // Cuando se invoca toString en un Date se usa el formato largo de fecha y hora

	/**
	 * @param name
	 * @param Description
	 */
	public DateDomain(String name, String Description) {
		super(name, Description);

	}
	public int getType() {
		return Domain.DATE;
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(com.vividsolutions.jump.feature.Feature, java.lang.String, java.lang.String)
	 */
	protected boolean validateLocal(SVGNodeFeature feature, String Name, Object valueObj) {
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
			else {
				String value = valueObj.toString();
				if (value==null || value.trim().equals("")) {
					if (!isNullable())
						setLastErrorMessage(Domain.NOT_NULLABLE_MSG);// asigna el error.
					return isNullable();
				}
				date_value=df.parse(value);// intenta parsear a través de un String
			}

			boolean passed=true;
			if (initialDate!=null)
				passed=passed & initialDate.before(date_value);
			if (finalDate!=null)
				passed=passed & finalDate.after(date_value);
			if (!passed)
				setLastErrorMessage(DATE_RANGE_MSG);// asigna el error por defecto.

			return passed;
		}catch (ParseException e)
		{
			setLastErrorMessage(DATE_FORMAT_MSG);	
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
