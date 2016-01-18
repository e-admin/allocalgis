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
 * IReportKeywordLookup.java
 * 
 */

package org.syntax.jedit;

import java.util.ArrayList;

import javax.swing.text.Segment;

import org.syntax.jedit.tokenmarker.Token;

public class IReportKeywordLookup implements KeywordLookupIF
{
        private ArrayList keys = new ArrayList();

        public IReportKeywordLookup()
        {
        }

        public void addKeyword(String keyword)
        {
          addKeyword(keyword, Token.PARAMETER_OK);
        }

        public void addKeyword(String keyword, byte token)
        {
          //System.out.println("add : " + keyword);
          keys.add(new Key(keyword, token));
        }

        public void removeKeyword(String keyword)
        {
          Key key;

          for(int i=0; i<keys.size(); i++)
          {
            key = (Key) keys.get(i);
            if(key.keyword.equals(keyword))
            {
              keys.remove(key);
            }
          }
        }

	/**
	 * Looks up a key.
	 * @param text The text segment
	 * @param offset The offset of the substring within the text segment
	 * @param length The length of the substring
	 */
	public byte lookup(Segment text, int offset, int length)
        {
          Key key;
          String keyword;
          boolean found;

          for(int i=0; i<keys.size(); i++)
          {
            key = (Key) keys.get(i);
            keyword = key.keyword;

            if(keyword.length() != length)
            {
              continue;
            }

            found = true;
            for(int j=0; j<keyword.length(); j++)
            {
              if(keyword.charAt(j) != text.array[offset + j])
              {
                found = false;
                break;
              }
            }

            if(found)
            {
              return key.token;
            }
          }

          return Token.NULL;
        }

        private class Key
        {
          String keyword;
          byte token;

          Key(String keyword, byte token)
          {
            this.keyword = keyword;
            this.token = token;
          }
        }

}
