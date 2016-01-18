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
 *
 *
 *
 *
 * IDLTokenMarker.java
 * 
 */

package org.syntax.jedit.tokenmarker;

import org.syntax.jedit.*;
import javax.swing.text.Segment;

/**
 * IDL token marker.
 *
 * @author Slava Pestov
 * @author Juha Lindfors
 * @version $Id: IDLTokenMarker.java,v 1.1 2011/12/14 18:15:21 satec Exp $
 */
public class IDLTokenMarker extends CTokenMarker
{
	public IDLTokenMarker()
	{
		super(true,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(idlKeywords == null)
		{
			idlKeywords = new KeywordMap(false);

			idlKeywords.add("any",      Token.KEYWORD3);
			idlKeywords.add("attribute",Token.KEYWORD1);
			idlKeywords.add("boolean",  Token.KEYWORD3);
			idlKeywords.add("case",     Token.KEYWORD1);
			idlKeywords.add("char",     Token.KEYWORD3);
			idlKeywords.add("const",    Token.KEYWORD1);
			idlKeywords.add("context",  Token.KEYWORD1);
			idlKeywords.add("default",  Token.KEYWORD1);
			idlKeywords.add("double",   Token.KEYWORD3);
			idlKeywords.add("enum",     Token.KEYWORD3);
			idlKeywords.add("exception",Token.KEYWORD1);
			idlKeywords.add("FALSE",    Token.LITERAL2);
			idlKeywords.add("fixed",    Token.KEYWORD1);
			idlKeywords.add("float",    Token.KEYWORD3);
			idlKeywords.add("in",       Token.KEYWORD1);
			idlKeywords.add("inout",    Token.KEYWORD1);
			idlKeywords.add("interface",Token.KEYWORD1);
			idlKeywords.add("long",     Token.KEYWORD3);
			idlKeywords.add("module",   Token.KEYWORD1);
			idlKeywords.add("Object",   Token.KEYWORD3);
			idlKeywords.add("octet",    Token.KEYWORD3);
			idlKeywords.add("oneway",   Token.KEYWORD1);
			idlKeywords.add("out",      Token.KEYWORD1);
			idlKeywords.add("raises",   Token.KEYWORD1);
			idlKeywords.add("readonly", Token.KEYWORD1);
			idlKeywords.add("sequence", Token.KEYWORD3);
			idlKeywords.add("short",    Token.KEYWORD3);
			idlKeywords.add("string",   Token.KEYWORD3);
			idlKeywords.add("struct",   Token.KEYWORD3);
			idlKeywords.add("switch",   Token.KEYWORD1);
			idlKeywords.add("TRUE",     Token.LITERAL2);
			idlKeywords.add("typedef",  Token.KEYWORD3);
			idlKeywords.add("unsigned", Token.KEYWORD3);
			idlKeywords.add("union",    Token.KEYWORD3);
			idlKeywords.add("void",     Token.KEYWORD3);
			idlKeywords.add("wchar",    Token.KEYWORD3);
			idlKeywords.add("wstring",  Token.KEYWORD3);
		}
		return idlKeywords;
	}

	// private members
	private static KeywordMap idlKeywords;
}
