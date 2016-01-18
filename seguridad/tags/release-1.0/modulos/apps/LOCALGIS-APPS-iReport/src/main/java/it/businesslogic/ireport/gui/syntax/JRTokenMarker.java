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
 * JRTokenMarker.java
 * 
 */

package it.businesslogic.ireport.gui.syntax;

import org.syntax.jedit.*;
import org.syntax.jedit.tokenmarker.*;
import javax.swing.text.Segment;

/**
 * Java token marker.
 *
 * @author Slava Pestov
 * @version $Id: JRTokenMarker.java,v 1.1 2011/12/14 18:15:24 satec Exp $
 */
public class JRTokenMarker extends TokenMarker
{
        public byte FIELD_KEYWORD = 0;
        public byte VARIABLE_KEYWORD = 0;
        public byte PARAMETER_KEYWORD = 0;
                
	public JRTokenMarker()
	{
		this(getKeywords());
	}

	public JRTokenMarker(KeywordMap keywords)
	{
		this.keywords = keywords;
                FIELD_KEYWORD = Token.KEYWORD1;
                VARIABLE_KEYWORD = Token.KEYWORD2;
                PARAMETER_KEYWORD = Token.KEYWORD3;
                
	}

	public byte markTokensImpl(byte token, Segment line, int lineIndex)
	{
		char[] array = line.array;
		int offset = line.offset;
		lastOffset = offset;
		lastKeyword = offset;
		int length = line.count + offset;
		boolean backslash = false;

loop:		for(int i = offset; i < length; i++)
		{
			int i1 = (i+1);

			char c = array[i];
			if(c == '\\')
			{
				backslash = !backslash;
				continue;
			}

			switch(token)
			{
			case Token.NULL:
				switch(c)
				{
				case '#':
					if(backslash)
						backslash = false;
					else if(cpp)
					{
						if(doKeyword(line,i,c))
							break;
						addToken(i - lastOffset,token);
						addToken(length - i,Token.KEYWORD2);
						lastOffset = lastKeyword = length;
						break loop;
					}
					break;
				case '"':
					doKeyword(line,i,c);
					if(backslash)
						backslash = false;
					else
					{
						addToken(i - lastOffset,token);
						token = Token.LITERAL1;
						lastOffset = lastKeyword = i;
					}
					break;
				case '\'':
					doKeyword(line,i,c);
					if(backslash)
						backslash = false;
					else
					{
						addToken(i - lastOffset,token);
						token = Token.LITERAL2;
						lastOffset = lastKeyword = i;
					}
					break;
				case ':':
					if(lastKeyword == offset)
					{
						if(doKeyword(line,i,c))
							break;
						backslash = false;
						addToken(i1 - lastOffset,Token.LABEL);
						lastOffset = lastKeyword = i1;
					}
					else if(doKeyword(line,i,c))
						break;
					break;
				case '/':
					backslash = false;
					doKeyword(line,i,c);
					if(length - i > 1)
					{
						switch(array[i1])
						{
						case '*':
							addToken(i - lastOffset,token);
							lastOffset = lastKeyword = i;
							if(length - i > 2 && array[i+2] == '*')
								token = Token.COMMENT2;
							else
								token = Token.COMMENT1;
							break;
						case '/':
							addToken(i - lastOffset,token);
							addToken(length - i,Token.COMMENT1);
							lastOffset = lastKeyword = length;
							break loop;
						}
					}
					break;
				default:
					backslash = false;
					if(!Character.isLetterOrDigit(c)
						&& c != '_')
						doKeyword(line,i,c);
					break;
				}
				break;
			case Token.COMMENT1:
			case Token.COMMENT2:
				backslash = false;
				if(c == '*' && length - i > 1)
				{
					if(array[i1] == '/')
					{
						i++;
						addToken((i+1) - lastOffset,token);
						token = Token.NULL;
						lastOffset = lastKeyword = i+1;
					}
				}
				break;
			case Token.LITERAL1:
				if(backslash)
					backslash = false;
				else if(c == '"')
				{
					addToken(i1 - lastOffset,token);
					token = Token.NULL;
					lastOffset = lastKeyword = i1;
				}
				break;
			case Token.LITERAL2:
				if(backslash)
					backslash = false;
				else if(c == '\'')
				{
					addToken(i1 - lastOffset,Token.LITERAL1);
					token = Token.NULL;
					lastOffset = lastKeyword = i1;
				}
				break;
			default:
				throw new InternalError("Invalid state: "
					+ token);
			}
		}

		if(token == Token.NULL)
			doKeyword(line,length,'\0');

		switch(token)
		{
		case Token.LITERAL1:
		case Token.LITERAL2:
			addToken(length - lastOffset,Token.INVALID);
			token = Token.NULL;
			break;
		case Token.KEYWORD2:
			addToken(length - lastOffset,token);
			if(!backslash)
				token = Token.NULL;
		default:
			addToken(length - lastOffset,token);
			break;
		}

		return token;
	}

	public static KeywordMap getKeywords()
	{
		if(cKeywords == null)
		{
			cKeywords = new KeywordMap(false);
			cKeywords.add("package",Token.KEYWORD2);
			cKeywords.add("import",Token.KEYWORD2);
			cKeywords.add("byte",Token.KEYWORD3);
			cKeywords.add("char",Token.KEYWORD3);
			cKeywords.add("short",Token.KEYWORD3);
			cKeywords.add("int",Token.KEYWORD3);
			cKeywords.add("long",Token.KEYWORD3);
			cKeywords.add("float",Token.KEYWORD3);
			cKeywords.add("double",Token.KEYWORD3);
			cKeywords.add("boolean",Token.KEYWORD3);
			cKeywords.add("void",Token.KEYWORD3);
			cKeywords.add("class",Token.KEYWORD3);
			cKeywords.add("interface",Token.KEYWORD3);
			cKeywords.add("abstract",Token.KEYWORD1);
			cKeywords.add("final",Token.KEYWORD1);
			cKeywords.add("private",Token.KEYWORD1);
			cKeywords.add("protected",Token.KEYWORD1);
			cKeywords.add("public",Token.KEYWORD1);
			cKeywords.add("static",Token.KEYWORD1);
			cKeywords.add("synchronized",Token.KEYWORD1);
			cKeywords.add("native",Token.KEYWORD1);
			cKeywords.add("volatile",Token.KEYWORD1);
			cKeywords.add("transient",Token.KEYWORD1);
			cKeywords.add("break",Token.KEYWORD1);
			cKeywords.add("case",Token.KEYWORD1);
			cKeywords.add("continue",Token.KEYWORD1);
			cKeywords.add("default",Token.KEYWORD1);
			cKeywords.add("do",Token.KEYWORD1);
			cKeywords.add("else",Token.KEYWORD1);
			cKeywords.add("for",Token.KEYWORD1);
			cKeywords.add("if",Token.KEYWORD1);
			cKeywords.add("instanceof",Token.KEYWORD1);
			cKeywords.add("new",Token.KEYWORD1);
			cKeywords.add("return",Token.KEYWORD1);
			cKeywords.add("switch",Token.KEYWORD1);
			cKeywords.add("while",Token.KEYWORD1);
			cKeywords.add("throw",Token.KEYWORD1);
			cKeywords.add("try",Token.KEYWORD1);
			cKeywords.add("catch",Token.KEYWORD1);
			cKeywords.add("extends",Token.KEYWORD1);
			cKeywords.add("finally",Token.KEYWORD1);
			cKeywords.add("implements",Token.KEYWORD1);
			cKeywords.add("throws",Token.KEYWORD1);
			cKeywords.add("this",Token.LITERAL2);
			cKeywords.add("null",Token.LITERAL2);
			cKeywords.add("super",Token.LITERAL2);
			cKeywords.add("true",Token.LITERAL2);
			cKeywords.add("false",Token.LITERAL2);
                        cKeywords.add("$PPPQ",Token.LITERAL2);
                        cKeywords.add("giulio",Token.LITERAL2);
		}
		return cKeywords;
	}

	// private members
	private static KeywordMap cKeywords;

	private boolean cpp;
	protected KeywordMap keywords;
	private int lastOffset;
	private int lastKeyword;

	private boolean doKeyword(Segment line, int i, char c)
	{
		int i1 = i+1;

		int len = i - lastKeyword;
		byte id = keywords.lookup(line,lastKeyword,len);
		if(id != Token.NULL)
		{
			if(lastKeyword != lastOffset)
				addToken(lastKeyword - lastOffset,Token.NULL);
			addToken(len,id);
			lastOffset = i;
		}
		lastKeyword = i1;
		return false;
	}
        
        public void addKeyword(String key, byte id)
        {
            keywords.add( key, id);
            
        }
        
}
