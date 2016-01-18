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
 * BatchFileTokenMarker.java
 * 
 */

package org.syntax.jedit.tokenmarker;

import javax.swing.text.Segment;

import org.syntax.jedit.SyntaxUtilities;

/**
 * Batch file token marker.
 *
 * @author Slava Pestov
 * @version $Id: BatchFileTokenMarker.java,v 1.1 2011/12/14 18:15:21 satec Exp $
 */
public class BatchFileTokenMarker extends TokenMarker
{
	public byte markTokensImpl(byte token, Segment line, int lineIndex)
	{
		char[] array = line.array;
		int offset = line.offset;
		int lastOffset = offset;
		int length = line.count + offset;

		if(SyntaxUtilities.regionMatches(true,line,offset,"rem"))
		{
			addToken(line.count,Token.COMMENT1);
			return Token.NULL;
		}

loop:		for(int i = offset; i < length; i++)
		{
			int i1 = (i+1);

			switch(token)
			{
			case Token.NULL:
				switch(array[i])
				{
				case '%':
					addToken(i - lastOffset,token);
					lastOffset = i;
					if(length - i <= 3 || array[i+2] == ' ')
					{
						addToken(2,Token.KEYWORD2);
						i += 2;
						lastOffset = i;
					}
					else
						token = Token.KEYWORD2;
					break;
				case '"':
					addToken(i - lastOffset,token);
					token = Token.LITERAL1;
					lastOffset = i;
					break;
				case ':':
					if(i == offset)
					{
						addToken(line.count,Token.LABEL);
						lastOffset = length;
						break loop;
					}
					break;
				case ' ':
					if(lastOffset == offset)
					{
						addToken(i - lastOffset,Token.KEYWORD1);
						lastOffset = i;
					}
					break;
				}
				break;
			case Token.KEYWORD2:
				if(array[i] == '%')
				{
					addToken(i1 - lastOffset,token);
					token = Token.NULL;
					lastOffset = i1;
				}
				break;
			case Token.LITERAL1:
				if(array[i] == '"')
				{
					addToken(i1 - lastOffset,token);
					token = Token.NULL;
					lastOffset = i1;
				}
				break;
			default:
				throw new InternalError("Invalid state: " + token);
			}
		}

		if(lastOffset != length)
		{
			if(token != Token.NULL)
				token = Token.INVALID;
			else if(lastOffset == offset)
				token = Token.KEYWORD1;
			addToken(length - lastOffset,token);
		}
		return Token.NULL;
	}

	public boolean supportsMultilineTokens()
	{
		return false;
	}
}
