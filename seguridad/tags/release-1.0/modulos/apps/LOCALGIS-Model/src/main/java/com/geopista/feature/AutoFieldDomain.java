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
 * Created on 07-jun-2004 by juacas
 *
 * 
 */
package com.geopista.feature;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;


import com.geopista.app.AppContext;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.util.expression.FeatureExpresionParser;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;


/**
 * @author juacas
 *
 * Un atributo que recibe el valor de otras propiedades de las capas o tablas
 * Se define para los siguientes tipos:
 * 		-FORMULA: con el patrón (<code>FORMULA:expresion </code>
 * 					Expresión general con los atributos de la Feature.
 * 					Soporta las funciones:
 * sin
 * cos
 * tan
 * asin
 * acos
 * atan
 * sinh
 * cosh
 * tanh
 * asinh
 * acosh
 * atanh
 * log
 * ln
 * sqrt
 * angle
 * abs
 * mod
 * sum
 * rand
 * double
 * str
 * substr
 * pow
 * area
 * length
 * 			
 * 	(deprecated)	-AREA: con el patrón AREA:format
 * 	(deprecated)	-LONGITUD: con el patrón LENGHT:format
 * 		-ID: Identificador del sistema. Si está vacío se interpreta como que está pendiente de establecerse.
 * 		-ENV_VAR: Consulta las variables de entorno del sistema local  ENV_VAR:nombrevalor
 * @see DecimalFormat
 */
public class AutoFieldDomain extends Domain {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(AutoFieldDomain.class);

	private String type;
	private static final String AUTOAREA= "AREA";
	private static final String AUTOLENGTH = "LENGTH";
	private static final String AUTOFORMULA = "FORMULA";
	private static final String AUTOENV_VAR = "ENV_VAR";
	private static final String AUTOID	=	"ID";
	private static final String NUMBER_FORMAT_ERROR_MSG = "AutoFieldDomain.NumberFormatError";
	private static final String FORMULA_MISMATCH_ERROR_MSG = "AutoFieldDomain.FormulaMismatchError";

	private static final String	FEATURE_HAVE_NO_GEOMETRY	= "AutoFieldDomain.HaveNoGeometry";
	private  FeatureExpresionParser expParser = null;
	/**
	 * 
	 */
	public AutoFieldDomain() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pattern
	 * @param Description
	 */
	public AutoFieldDomain(String pattern, String Description) {
		super(pattern, Description);
		setPattern(pattern);
	}
	public  boolean validate(Feature feature, String Name, Object Value)
	{
	return validateLocal(feature,Name,Value);
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(com.vividsolutions.jump.feature.Feature, java.lang.String, java.lang.String)
	 */
	protected boolean validateLocal(Feature feature, String Name, Object valueObj) {
		
		if (type.equals(AutoFieldDomain.AUTOID)) return true;
		
		double dValue;
		if (valueObj==null)
			valueObj=feature.getAttribute(Name);
		if (valueObj==null)return false;
		try {
			dValue = ( (valueObj instanceof Double)?
					((Double)valueObj).doubleValue()
					: Double.parseDouble(valueObj.toString()));
		} catch (NumberFormatException ex) {
			ex.getLocalizedMessage();
			setLastErrorMessage(I18N.get(AutoFieldDomain.NUMBER_FORMAT_ERROR_MSG));// asigna el error por defecto.
			
			return false;
		}
		
		
		if (type.equals(AutoFieldDomain.AUTOAREA))
			if(feature.getSchema().getGeometryIndex()==-1)
				{
				setLastErrorMessage(I18N.get(FEATURE_HAVE_NO_GEOMETRY));
				return false;
				}
			else 
				return dValue==feature.getGeometry().getArea();
		else
		if (type.equals(AutoFieldDomain.AUTOLENGTH))
			if(feature.getSchema().getGeometryIndex()==-1)
				{
				setLastErrorMessage(I18N.get(FEATURE_HAVE_NO_GEOMETRY));
				return false;
				}
			else 
				return dValue==feature.getGeometry().getLength();
		else
		if (type.equals(AutoFieldDomain.AUTOFORMULA))
		{
			return valueObj.equals(getRightValue(feature));
		}
        
		else if (type.equals(AutoFieldDomain.AUTOENV_VAR))
		{
			//No hago validación porque supongo que este caso sólo va a aplicar al municipio y en el desplegable
			//ya tengo determinados los valores válidos
		    //return valueObj.toString().equals(getRightValue(feature));
			return true;
		}
	setLastErrorMessage(I18N.getMessage(AutoFieldDomain.FORMULA_MISMATCH_ERROR_MSG,new Object[]{expression}));// asigna el error por defecto.
		
	return false;
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
	
		return Domain.AUTO;
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getRepresentation()
	 */
	public String getRepresentation() {
		return getName();
	}

	/* El patrón de definición reconoce varias Cadenas clave:
	 * <pre>
	 * AREA:format
	 * LENGHT:format
	 * FORMULA:expresión matemática
	 * </pre>
	 **/
	
	private String expression;
	public void setPattern(String pattern) {
		
		super.setPattern(pattern);
		pattern=this.pattern;
		
		int posParam = pattern.indexOf(':');
		int posPattern= pattern.indexOf(':',posParam+1)+1;
		
		if (posParam==-1) posParam=pattern.length();
		if (posPattern == 0) posPattern=pattern.length();
		String command=pattern.substring(0,posParam);
		type=command;
	if (type.equals(AutoFieldDomain.AUTOFORMULA))
		{
		this.expParser=new FeatureExpresionParser(null);
		expression=pattern.substring(posParam+1,posPattern);
		setFormat(pattern.substring(posPattern));
		}
	else
	if (type.equals(AutoFieldDomain.AUTOID))
	{
		return;
	}
	else
		if (type.equals(AutoFieldDomain.AUTOENV_VAR))
			{
			expression=pattern.substring(posParam+1,posPattern);
			setFormat(pattern.substring(posPattern));
			}
		else
		{
		posPattern=posParam;
		setFormat(pattern.substring(posPattern));
		}
	
	if (getFormat().length()<5) 
		setAproxLenght(8); 
		else
		setAproxLenght(getFormat().length());
	}
    
    public Object getRightValue(Feature feature)
    {
        return getRightValue(feature,null);
    }
    
    
    public Object getRightValue(Feature feature, int attIndex)
    {
        return getRightValue(feature,feature.getSchema().getAttributeName(attIndex));
    }
	
	/**
	 * Obtiene el valor correcto según este dominio para la feature
	 * 
	 * @param feature
	 * 
	 * @return Object con el valor adecuado. null si no hay que cambiar el valor existente
	 */
	public Object getRightValue(Feature feature, String attName)
	{
		if (type.equals(AutoFieldDomain.AUTOFORMULA) && expParser!=null)
		{
		if (expParser.getLastFeature()!=feature) // reevalua solo si no se ha definido ya. Evita bucles infinitos
			expParser.setFeature(feature);
		expParser.parseExpression(expression);
		return expParser.getValueAsObject();
		}
		else
		if (type.equals(AutoFieldDomain.AUTOAREA))
		{
			try
		{
		double area = feature.getGeometry().getArea();
		return new Double(area);
		} catch (ArrayIndexOutOfBoundsException e)// Ocurre cuando una feature no tiene geometria (generalmente en codigo de pruebas.)
		{
			logger.debug("getRightValue("+feature+"): Feature sin GEOMETRY.");
		
		}
		}
		else
		if (type.equals(AutoFieldDomain.AUTOLENGTH))
		{
			return new Double(feature.getGeometry().getLength());
		}
		else
		if (type.equals(AutoFieldDomain.AUTOENV_VAR))
			{
				if (attName == null)
					return AppContext.getApplicationContext().getUserPreference(expression,null,true);
				String cadena = type+":"+expression;
				if (cadena.equals(Const.KEY_ID_MUNI)){
					Object sMunicipio = (Object)((GeopistaFeature)feature).getAttributesDirectly()[feature.getSchema().getAttributeIndex(attName)];
					if (sMunicipio != null)
						return sMunicipio;
				}
				return AppContext.getApplicationContext().getUserPreference(expression,null,true);
			}
		else
		if (feature instanceof GeopistaFeature && type.equals(AutoFieldDomain.AUTOID))
			{
                if(((GeopistaFeature)feature).isTempID() && attName!=null) 
                    return ((GeopistaFeature)feature).getAttributesDirectly()[feature.getSchema().getAttributeIndex(attName)];
                else if (((GeopistaFeature)feature).getSystemId().trim().equals(""))
                    return "";
                else
                    return new Long(((GeopistaFeature)feature).getSystemId());
			}
		return null;// indica que no hay que cambiar el valor
		
	}
		


	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getAttributeType()
	 */
	public AttributeType getAttributeType()
	{
		return AttributeType.STRING;
	}
}
