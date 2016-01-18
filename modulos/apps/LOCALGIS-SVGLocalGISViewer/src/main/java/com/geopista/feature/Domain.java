/**
 * Domain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.satec.sld.SVG.SVGNodeFeature;
/**
 *
 * El dominio define el contenido y los valores posibles de un atributo de una capa o 
 * una columna de una base de datos.
 * Un dominio restringe un valor si no tiene hijos.
 * Si tiene hijos se aplica el dominio del primero de sus hijos en el que el valor encaja.
 * 
 * @author juacas
 * @see StringDomain, TreeDomain, CodedEntryDomain, AutoFieldDomain, NumberDomain, DateDomain, BooleanDomain
 */
public abstract class Domain {
	/**
	 * @param lastErrorMessage The lastErrorMessage to set.
	 */
	public void setLastErrorMessage(String lastErrorMessage) {
		this.lastErrorMessage = lastErrorMessage;
	}
	/**
	 * Restriction domain of the value of an attribute
	 */
	public static final int PATTERN = 1;
	public static final int TREE = 2;
	public static final int NUMBER = 3;
	public static final int CODEBOOK = 4;
	public static final int BOOLEAN = 5;
	public static final int DATE = 6;
	public static final int CODEDENTRY = 7;
	public static final int AUTO=8;
	
	/**
	 * Especifica el nivel de seguridad para cambios asociado
	 * al dominio.
	 * Se entiende lo siguiente:
	 * <code>SYSTEMLEVEL</code>  Dominio de configuración, en principio no se debería alterar.
	 * 	<code>MEDIUMLEVEL</code>	 Dominio personalizable a nivel de supramunicipal
	 * 	<code>FREELEVEL</code>	 Dominio personalizable a nivel de municipio
	 */
	
	public static final int SYSTEMLEVEL =1;
	public static final int MEDIUMLEVEL =2;
	public static final int FREELEVEL =3;
	private ArrayList children = new ArrayList();

	      
	/**
	 * <code>systemID</code> Identificador de sistema del dominio. (Para persistencia en la BBDD)
	 */ 
	private int systemID;
	private String Name;
	protected String pattern;
	protected String originalPattern;
	private String format;
	private int aproxLenght;
	private String Description; // Description of the restriction for information
	public static final String NOT_NULLABLE_MSG = "Domain.NotNullableField";
	/**
	 * En atributo <code>permissionLevel</code> define el nivel de acceso 
	 * para ciertas operaciones como la modificación. Mediante este atributo
	 * se habilita la posibilidad de definir Permisos distintos según la información 
	 * marcada en la definición del dominio. Se hará coincidir con permisos numerados
	 * que las aplicaciones deberán comprobar en el interfaz de usuario.
	 */
	private int permissionLevel=1;
	private boolean nullable=false;
	private String lastErrorMessage;
	/**
	 * Empty constructor needed for Java2XML
	 */
	public Domain()
	{}
	/**
	 * Initialize a Domain with a Name and a Description
	 * @param Name	Identificador del dominio en el sistema
	 * @param Description Texto descriptivo de utilidad para los interfaces de usuario
	 */
	public Domain(String Name, String Description) {
		super();
		this.Name=Name;
		setDescription(Description);
	}
	/**
	 * Check if attribute is conformant to domain
	 *  
	 * @param feature Feature on which the attribute to check resides.If the domain needs to check coherence a feature object is needed
	 * 					otherwise null is passed
	 * @param Value	String representation of value. Value to check. Feature Attribute's value is used if null
	 */
	public  abstract boolean validate(SVGNodeFeature feature, String Name, Object Value);
	
	/**
	 * @return Returns the children.
	 */
	public ArrayList getChildren() {
		return children;
	}

	public void addChild(Domain child_domain) {
		//TODO: comprobar qeu no se mezclan tipos en los hijos
		children.add(child_domain);
	}
	/**
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return originalPattern;
	}
	/**
	 * Si el patrón está vacío lo registra con el comodín general ".*"
	 * Si el patrón incluye el caracter '?' en primer lugar
	 * lo extrae y cualifica el dominio como opcional. (El valor puede adoptar el valor null )
	 * @param pattern
	 *            The pattern to set.
	 */
	public void setPattern(String pattern) {
	    originalPattern=pattern;
	    if (pattern==null)
	    	return;
		if (pattern.length()==0)
			this.pattern=".*";
		else
		if (pattern.charAt(0)=='?')
		{
			this.nullable=true;
			this.pattern = pattern.substring(1);
		}
		else
			this.pattern=pattern;
	}
	/**
	 * @return Returns the type.
	 */
	public abstract int getType(); 

	/**
	 * @return Returns the aproxLenght.
	 */
	public int getAproxLenght() {
		return aproxLenght;
	}
	/**
	 * Longitud previsible que se utilizará en los interfaces de usuario.
	 * @param aproxLenght
	 *            The aproxLenght to set.
	 */
	public void setAproxLenght(int aproxLenght) {
		this.aproxLenght = aproxLenght;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		Description = description;
	}
	/**
	 * Returns the String representation of this domain default value.
	 * @return TODO
	 */
	public abstract String getRepresentation();
	/**
	 * Search a matching child domain for the value
	 * @param feature
	 * @param attName
	 * @param value
	 * @return
	 */
	public Domain getMatchedChildDomain(SVGNodeFeature feature, String attName,Object value)
	{
		Iterator children= getChildren().iterator(); // get root, must have children
				
		//  busca en los hijos
		while(children.hasNext())
		{
		Domain child=(Domain) children.next();	
		if (child.validate(feature,attName,value)==true)
			return child; // this domain matches
		else
			setLastErrorMessage(child.getLastError());
		
		}
		
		return null; // no child matches
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getRepresentation();
	}
	/**
	 * @return Returns the permissionLevel.
	 */
	public int getPermissionLevel() {
		return permissionLevel;
	}
	/**
	 * Entero que se relaciona con permisos numerados en los ACLs de las 
	 * aplicaciones.
	 * @param permissionLevel The permissionLevel to set.
	 */
	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	/**
	 * @return Returns the systemID.
	 */
	public int getSystemID() {
		return systemID;
	}
	/**
	 * @param systemID The systemID to set.
	 */
	public void setSystemID(int systemID) {
		this.systemID = systemID;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * Tras una validación se ofrece una descripción del sistema sobre el motivo que ha provocado la no validación
	 * @return
	 */
	public String getLastError() {
		
		return lastErrorMessage;
	}
	/**
	 * Devuelve el formato que se usará para formatear los valores.
	 * @return Returns the format.
	 */
	public String getFormat()
	{
		return format;
	}
	/**
	 * Fija el formato que se usa para la representación de la información al usuario
	 * @param format The format to set.
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}
	/**
	 * Informa de si el valor NULL se considera correcto o no aceptable
	 * @return
	 */
	public boolean isNullable()
	{
		return nullable;
	}
//	static public Domain getDefaultDomain(GeopistaSchema gSchema, String attname) {
//	
//	
//		if (attname.equalsIgnoreCase("ID"))
//		{
//		    return new AutoFieldDomain("ID", "Dominio Por Defecto");// StringDomain("[.*]","Dominio
//		                                                                    // Por
//		                                                                    // Defecto.");
//		} else
//		{
//	
//		   if (gSchema.getAttributeType(attname) == AttributeType.DATE)
//		    {
//		        return new DateDomain("?[*:*]", "Dominio Por Defecto");
//		    }
//		   else
//			   return getDomainForType(gSchema.getAttributeType(attname).toJavaClass());
//		}
//		
//	}
	static public Domain getDomainForType(Class c)
	{
		Domain defDomain=null;
		  if (c == String.class)
		    {
		        defDomain = new StringDomain("?[.*]", "Dominio Por Defecto");
		    } else if (c== Double.class)
		    {
		        defDomain = new NumberDomain("?[-INF:INF]", "Dominio Por Defecto");
		    } else if (c==Float.class)
		    {
		        defDomain = new NumberDomain("?[-INF:INF]", "Dominio Por Defecto");
		    } else if (c==Integer.class)
		    {
		        defDomain = new NumberDomain("?[-INF:INF]", "Dominio Por Defecto");
		    } else if (c==Date.class || c==Calendar.class)
		    {
		        defDomain = new DateDomain("?[*:*]", "Dominio Por Defecto");
		    }
		  return defDomain;
	}
}