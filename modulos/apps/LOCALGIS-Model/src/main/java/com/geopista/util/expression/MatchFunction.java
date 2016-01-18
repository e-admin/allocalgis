/**
 * MatchFunction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.expression;

import java.util.Stack;
import java.util.regex.Pattern;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class MatchFunction extends PostfixMathCommand
{

	private Pattern patternReg=null;
	/**
	 * 
	 */
	public MatchFunction() {
		super();
		this.numberOfParameters=2;
	}
	public void run(Stack inStack) throws ParseException
	{
		// check the stack
		checkStack(inStack);
		// get the parameter from the stack
		Object pattern = inStack.pop();
		Object param = inStack.pop();
		// check whether the argument is of the right type
		if (param instanceof String && pattern instanceof String)
		{
		//if (patternReg==null) // cache the pattern parser
			patternReg = Pattern.compile((String)pattern);
		
        boolean matchFound = patternReg.matcher((String)param).matches();
		
		//boolean matchFound = Pattern.matches((String)pattern,(String)param);
		Double r = new Double(matchFound==true?1:0);
		// push the result on the inStack
		inStack.push(r); 
		}
		else 
		{
		      throw new ParseException("Invalid parameter type");
		}
		
	}
}
