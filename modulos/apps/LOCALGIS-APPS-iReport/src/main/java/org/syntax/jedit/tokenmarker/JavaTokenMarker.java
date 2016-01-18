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
 * JavaTokenMarker.java
 * 
 */

package org.syntax.jedit.tokenmarker;

import org.syntax.jedit.KeywordMap;

/**
 * Java token marker.
 *
 * @author Slava Pestov
 * @version $Id: JavaTokenMarker.java,v 1.1 2011/12/14 18:15:21 satec Exp $
 */
public class JavaTokenMarker extends CTokenMarker
{
	public JavaTokenMarker()
	{
		super(false,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(javaKeywords == null)
		{
			javaKeywords = new KeywordMap(false);
			javaKeywords.add("package",Token.KEYWORD2);
			javaKeywords.add("import",Token.KEYWORD2);
			javaKeywords.add("byte",Token.KEYWORD3);
			javaKeywords.add("char",Token.KEYWORD3);
			javaKeywords.add("short",Token.KEYWORD3);
			javaKeywords.add("int",Token.KEYWORD3);
			javaKeywords.add("long",Token.KEYWORD3);
			javaKeywords.add("float",Token.KEYWORD3);
			javaKeywords.add("double",Token.KEYWORD3);
			javaKeywords.add("boolean",Token.KEYWORD3);
			javaKeywords.add("void",Token.KEYWORD3);
			javaKeywords.add("class",Token.KEYWORD3);
			javaKeywords.add("interface",Token.KEYWORD3);
			javaKeywords.add("abstract",Token.KEYWORD1);
			javaKeywords.add("final",Token.KEYWORD1);
			javaKeywords.add("private",Token.KEYWORD1);
			javaKeywords.add("protected",Token.KEYWORD1);
			javaKeywords.add("public",Token.KEYWORD1);
			javaKeywords.add("static",Token.KEYWORD1);
			javaKeywords.add("synchronized",Token.KEYWORD1);
			javaKeywords.add("native",Token.KEYWORD1);
			javaKeywords.add("volatile",Token.KEYWORD1);
			javaKeywords.add("transient",Token.KEYWORD1);
			javaKeywords.add("break",Token.KEYWORD1);
			javaKeywords.add("case",Token.KEYWORD1);
			javaKeywords.add("continue",Token.KEYWORD1);
			javaKeywords.add("default",Token.KEYWORD1);
			javaKeywords.add("do",Token.KEYWORD1);
			javaKeywords.add("else",Token.KEYWORD1);
			javaKeywords.add("for",Token.KEYWORD1);
			javaKeywords.add("if",Token.KEYWORD1);
			javaKeywords.add("instanceof",Token.KEYWORD1);
			javaKeywords.add("new",Token.KEYWORD1);
			javaKeywords.add("return",Token.KEYWORD1);
			javaKeywords.add("switch",Token.KEYWORD1);
			javaKeywords.add("while",Token.KEYWORD1);
			javaKeywords.add("throw",Token.KEYWORD1);
			javaKeywords.add("try",Token.KEYWORD1);
			javaKeywords.add("catch",Token.KEYWORD1);
			javaKeywords.add("extends",Token.KEYWORD1);
			javaKeywords.add("finally",Token.KEYWORD1);
			javaKeywords.add("implements",Token.KEYWORD1);
			javaKeywords.add("throws",Token.KEYWORD1);
			javaKeywords.add("this",Token.LITERAL2);
			javaKeywords.add("null",Token.LITERAL2);
			javaKeywords.add("super",Token.LITERAL2);
			javaKeywords.add("true",Token.LITERAL2);
			javaKeywords.add("false",Token.LITERAL2);
                        javaKeywords.add("msg",Token.KEYWORD1);
                        javaKeywords.add("str",Token.KEYWORD1);
		}
		return javaKeywords;
	}

	// private members
	private static KeywordMap javaKeywords;
}
