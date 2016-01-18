/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 * ExtendedJRJdtCompiler.java
 *
 * Created on March 14, 2007, 2:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.compiler;

import it.businesslogic.ireport.compiler.xml.SourceLocation;
import it.businesslogic.ireport.compiler.xml.SourceTraceDigester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRCompilationSourceCode;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import net.sf.jasperreports.engine.design.JRJdtCompiler;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

/**
 *
 * @author gtoffoli
 */
public class ExtendedJRJdtCompiler extends JRJdtCompiler {
    
    private JasperReportErrorHandler errorHandler = null;
    private SourceTraceDigester digester = null;
    private ICompilerRequestor superCompilerRequestor = null;
    
    protected ICompilerRequestor getCompilerRequestor(JRCompilationUnit[] units, StringBuffer problemBuffer)
    {
            return new CompilerRequestor(super.getCompilerRequestor(units, problemBuffer), units);
    }
    
    
    protected class CompilerRequestor implements ICompilerRequestor
	{
                private ICompilerRequestor superCompilerRequestor = null;
		private final Map expressionErrors = new HashMap();
		private final JRCompilationUnit[] units;

		protected CompilerRequestor(ICompilerRequestor superCompilerRequestor, JRCompilationUnit[] units)
		{
                        this.superCompilerRequestor = superCompilerRequestor;
			this.units = units;
		}

		public void acceptResult(CompilationResult result)
		{
                        if (superCompilerRequestor != null) superCompilerRequestor.acceptResult(result);
			if (result.hasErrors())
			{
				String className = String.valueOf(result.getCompilationUnit().getMainTypeName());
				
				JRCompilationUnit unit = null;
				for (int classIdx = 0; classIdx < units.length; ++classIdx)
				{
					if (className.equals(units[classIdx].getName()))
					{
						unit = units[classIdx];
						break;
					}
				}
				
				IProblem[] errors = result.getErrors();
				for (int i = 0; i < errors.length; i++)
				{
					IProblem problem = errors[i];
					int line = problem.getSourceLineNumber();
					JRCompilationSourceCode sourceCode = unit.getCompilationSource();
					JRExpression expression = sourceCode.getExpressionAtLine(line);
					if (expression == null)
					{
						getErrorHandler().addMarker(problem, null);
					}
					else if (addExpressionError(expression, problem))
					{
						SourceLocation location = getDigester().getLocation(expression);
						getErrorHandler().addMarker(problem, location);
					}
				}
			}
		}
		
		protected boolean addExpressionError(JRExpression expression, IProblem problem)
		{
			Set errors = (Set) expressionErrors.get(expression);
			if (errors == null)
			{
				errors = new HashSet();
				expressionErrors.put(expression, errors);
			}
			return errors.add(new ExpressionErrorKey(problem));
		}
	}
	
	protected static class ExpressionErrorKey
	{
		private final IProblem problem;
		private final int hash;
		
		public ExpressionErrorKey(IProblem problem)
		{
			this.problem = problem;
			this.hash = computeHash();
		}

		private int computeHash()
		{
			int h = problem.getMessage().hashCode();
			return h;
		}
		
		public int hashCode()
		{
			return hash;
		}
		
		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof ExpressionErrorKey) || this.hash != o.hashCode())
			{
				return false;
			}
			
			if (this == o)
			{
				return true;
			}
			
			ExpressionErrorKey k = (ExpressionErrorKey) o;
			return problem.getMessage().equals(k.problem.getMessage());
		}
	}

    public JasperReportErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(JasperReportErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public SourceTraceDigester getDigester() {
        return digester;
    }

    public void setDigester(SourceTraceDigester digester) {
        this.digester = digester;
    }
}
