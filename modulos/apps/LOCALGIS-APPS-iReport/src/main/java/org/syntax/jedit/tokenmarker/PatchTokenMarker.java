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
 * PatchTokenMarker.java
 * 
 */

package org.syntax.jedit.tokenmarker;

import javax.swing.text.Segment;

/**
 * Patch/diff token marker.
 *
 * @author Slava Pestov
 * @version $Id: PatchTokenMarker.java,v 1.1 2011/12/14 18:15:21 satec Exp $
 */
public class PatchTokenMarker extends TokenMarker
{
	public byte markTokensImpl(byte token, Segment line, int lineIndex)
	{
		if(line.count == 0)
			return Token.NULL;
		switch(line.array[line.offset])
		{
		case '+': case '>':
			addToken(line.count,Token.KEYWORD1);
			break;
		case '-': case '<':
			addToken(line.count,Token.KEYWORD2);
			break;
		case '@': case '*':
			addToken(line.count,Token.KEYWORD3);
			break;
	        default:
			addToken(line.count,Token.NULL);
			break;
		}
		return Token.NULL;
	}

	public boolean supportsMultilineTokens()
	{
		return false;
	}
}
