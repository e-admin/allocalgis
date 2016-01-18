/*
 * 
 * Created on 23-jun-2005 by juacas
 *
 * 
 */
package com.geopista.util.expression;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class TrimFunction extends PostfixMathCommand
{
	/**
	 * 
	 */
	public TrimFunction()
	{
	numberOfParameters=1;
	}
	
	public void run(Stack inStack) throws ParseException
		{
		// check the stack
		checkStack(inStack);
		// get the parameter from the stack
		Object param = inStack.pop();
		// check whether the argument is of the right type
		if (param instanceof String) {
		// calculate the result
		String r = ((String)param).trim();
		// push the result on the inStack
		inStack.push(r); 
		}
		else
			throw new ParseException("Invalid parameter type");
		
		
		}
}
