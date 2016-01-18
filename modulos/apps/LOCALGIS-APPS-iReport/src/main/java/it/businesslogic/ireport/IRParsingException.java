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
 * IRParsingException.java
 * 
 * Created on September 21, 2006, 3:53 PM
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author gtoffoli
 */
public class IRParsingException extends Exception {
    
    String chunk = null;
    /** Creates a new instance of IRParsingException */
    public IRParsingException(String msg, String chunk) {
        super(msg);
        this.chunk = chunk;
    }
    
    public IRParsingException(String msg) {
        this(msg,null);
    }
    
    public String getChunk()
    {
        return chunk;
    }
    
    public void setChunk(String c)
    {
        this.chunk = c;
    }
    
}
