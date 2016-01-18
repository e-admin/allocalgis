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
 * Created on 03-oct-2004 by juacas
 *
 * 
 */
package com.geopista.util.expression;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO Documentación
 * @author cotesa
 *
 */
public class MapFunction extends PostfixMathCommand
{
	Hashtable htMap = new Hashtable();
	
	
	public MapFunction() {
		super();
		this.numberOfParameters=3;

	}
	public void run(Stack inStack) throws ParseException
	{
		// check the stack
		checkStack(inStack);
		// get the parameter from the stack
		Object param3 =  inStack.pop();
		Object param2 =  inStack.pop();
		Object param1 =  inStack.pop();
				
		if(param2 instanceof String && param3 instanceof String)
		{	
			loadMap ((String)param2, (String)param3);
						
			// calculate the result			
			String orig = String.valueOf(param1).trim();
			//La cadena puede estar vacía porque sean espacios en blanco. Para contemplar estos
			//valores por defecto, se introduce un espacio " "
			if (orig.length()==0)
				orig = " ";
			String r = (String)htMap.get(orig);
			//String r = (String)htMap.get(String.valueOf(param1));
			if (r==null)
				r="Mapeo no válido";
			// push the result on the inStack
			inStack.push(r); 
		}
		else
		{
			throw new ParseException("Invalid parameter type");
		}

	}
	
	private void loadMap(String param1, String param2) {
		
		String [] keys   = param1.split(";");
		String [] values = param2.split(";");
		htMap = new Hashtable();
		
		for (int i=0; i< keys.length; i++){
			htMap.put(keys[i], values[i]);
		}				
	}
}
