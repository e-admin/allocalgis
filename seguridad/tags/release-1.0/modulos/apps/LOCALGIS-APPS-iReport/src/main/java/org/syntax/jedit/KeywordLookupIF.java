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
 * KeywordLookupIF.java
 * 
 */

package org.syntax.jedit;

import org.syntax.jedit.tokenmarker.*;
import javax.swing.text.Segment;

/**
 * A <code>KeywordLookupIF</code> provides a mean to extends the number of
 * keywords for a CTokenMarker easily.
 *
 * @author Kees Kuip 
 * @version $Id: KeywordLookupIF.java,v 1.1 2011/12/14 18:15:22 satec Exp $
 */
public interface KeywordLookupIF
{
	/**
	 * Looks up a key.
	 * @param text The text segment
	 * @param offset The offset of the substring within the text segment
	 * @param length The length of the substring
	 */
	public byte lookup(Segment text, int offset, int length);

}
