/**
 * SubStringFunction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.util;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;


public class SubStringFunction extends PostfixMathCommand
{
	public SubStringFunction()
	{
	numberOfParameters=3;
	}
	public void run(Stack inStack) throws ParseException
		{
		// check the stack
		checkStack(inStack);
		// get the parameter from the stack
		Object param3 = inStack.pop();
		Object param2 =  inStack.pop();
		Object param1 =  inStack.pop();
		int from=((Double)param2).intValue();
		int to=((Double)param3).intValue();
		
		String r = ((String)param1).substring(from,to);
		
		inStack.push(r); 
		}
}