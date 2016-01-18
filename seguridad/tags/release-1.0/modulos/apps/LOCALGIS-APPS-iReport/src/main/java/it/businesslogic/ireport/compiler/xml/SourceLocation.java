/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
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
 */

package it.businesslogic.ireport.compiler.xml;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: SourceLocation.java,v 1.1 2011/12/14 18:15:22 satec Exp $
 */
public class SourceLocation
{

	private int lineNumber;
	private int columnNumber;
	private String xPath;
	
	public SourceLocation()
	{
	}

	
	public int getColumnNumber()
	{
		return columnNumber;
	}

	
	public void setColumnNumber(int columnNumber)
	{
		this.columnNumber = columnNumber;
	}

	
	public int getLineNumber()
	{
		return lineNumber;
	}

	
	public void setLineNumber(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	
	public String getXPath()
	{
		return xPath;
	}

	
	public void setXPath(String path)
	{
		xPath = path;
	}
	
}
