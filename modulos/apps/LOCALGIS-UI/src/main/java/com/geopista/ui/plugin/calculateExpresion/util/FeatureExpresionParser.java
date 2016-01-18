/**
 * FeatureExpresionParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.util;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.nfunk.jep.FunctionTable;
import org.nfunk.jep.JEP;
import org.nfunk.jep.function.Power;

import com.geopista.util.expression.AreaFunction;
import com.geopista.util.expression.LengthFunction;
import com.geopista.util.expression.TrimFunction;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
/*

/**
 * Evaluador de expresiones matemáticas utilizando como 
 * variables los campos de una feature
 * Los nombres de los atributos no pueden llevar espacios. Si los tienen se sustituyen por '_'
 * 
 *Soporta las siguientes funciones:
 *sin
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
 * @see JEP
 *  @author juacas
 *
 */
public class FeatureExpresionParser extends JEP
{
	private Collection features;
	private Feature	last_feature;
	private Hashtable documentation=new Hashtable<String, String>();

	public FeatureExpresionParser(Feature feature)
	{
		super();

		//addStandardConstants();
		//addStandardFunctions();
		addCustomFuncions();
		

		if (feature!=null)
			setFeature(feature);
	}
	public FunctionTable getFunctionTable()
	{
		return this.funTab;
	}
	/**
	 * 
	 */
	private void addCustomFuncions()
	{
		
		addFunction("abs",new OneParameterFunction());
		addFunction("area",new AreaFunction());
		addFunction("perimeter", new LengthFunction());
		addFunction("length", new LengthFunction());
		addFunction("mod",new TwoParameterFunction());
		addFunction("pow",new Power());
		addFunction("random",new ZeroParameterFunction());
		addFunction("sqrt",new OneParameterFunction());
		addFunction("substr",new SubStringFunction());	
		addFunction("trim", new TrimFunction());
		
		addDocumentation("abs","abs(x) Calcula el valor absoluto del parametro x.");
		addDocumentation("length","length(cad) devuelve la longitud en caracteres de la cadena.");
		addDocumentation("mod","mod(x,y) devuelve el modulo de la division de x entre y.");
		addDocumentation("pow","pow(x,y) calcula x elevado a y.");
		addDocumentation("random","random() devuelve un numero aleatorio entre 0 y 1.");
		addDocumentation("sqrt", "sqrt(x) devuelve la raiz cuadrada de x.");
		addDocumentation("substr","substr(cad,inicio,final) devuelve una porción de la cadena cad.");
		addDocumentation("trim","trim(x) devuelve la cadena x sin espacios.");

//		TODO: Add documentation
	}
	private void addDocumentation(String command, String doc) {
		documentation.put(command, doc);
	}
	/**
	 * Inicializa variables con valores ficticios
	 * para propósitos de comprobación de expresiones
	 * @param sch
	 */
	public void setSchema(FeatureSchema sch)
	{
		for (int i=0;i<sch.getAttributeCount();i++)
		{
			this.addVariable(sch.getAttributeName(i).replaceAll(" ","_"), 0);
		}
	}
	public Feature getLastFeature()
	{
		return last_feature;
	}

	public void setFeature(Feature feature)
	{
		last_feature=feature;
		FeatureSchema sch = feature.getSchema();

		
		// Asigna las variables con sus valores
		for (int i=0;i<sch.getAttributeCount();i++)
		{
			if (feature.getAttribute(i)!=null)
				this.addVariableAsObject( sch.getAttributeName(i).replaceAll(" ","_"), feature.getAttribute(i));
			else
			{
				AttributeType type = feature.getSchema().getAttributeType(i); 
				if(type == AttributeType.DOUBLE ||
						type == AttributeType.LONG ||
						type ==AttributeType.INTEGER)
				{
					this.addVariableAsObject(sch.getAttributeName(i).replaceAll(" ","_"), new Double(0));
				}

				if(type == AttributeType.STRING || type == AttributeType.DATE)
				{
					this.addVariableAsObject(sch.getAttributeName(i).replaceAll(" ","_"), "");
				}

				if(type == AttributeType.GEOMETRY)
				{
					Coordinate tempCoordinate = new Coordinate(0,0);
					GeometryFactory geometryFactory = new GeometryFactory();
					this.addVariableAsObject(sch.getAttributeName(i).replaceAll(" ","_"), geometryFactory.createPoint(tempCoordinate));
				}
			}
		}
	}
	public void setFeatures(Collection features)
	{
		this.features=features;
		if (features.size()!=0)
			setFeature((Feature)features.iterator().next());
	}

	/**
	 * Devuelve una lista de valores con los resultados de 
	 * la evaluación en la serie de Features introducidas.
	 */
	public  Vector getValuesAsObjects()
	{
		Iterator it=features.iterator();
		Vector values=new Vector();
		while (it.hasNext())
		{
			setFeature((Feature)it.next());
			Object value=this.getValueAsObject();
			if (value==null)
				values.add(this.getErrorInfo());
			else
				values.add(value);
		}
		return values;
	}
	/**
	 * Returns a text with documentation about a command uses documentation hashmap
	 * @param class1
	 * @return
	 */
	public String getDocumentation(String command) {

		return (String) documentation.get(command);
	}
}
