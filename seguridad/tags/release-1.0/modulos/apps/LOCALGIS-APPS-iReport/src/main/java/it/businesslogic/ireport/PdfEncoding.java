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
 * PdfEncoding.java
 * 
 * Created on 24 maggio 2003, 0.40
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class PdfEncoding {
    
    private String encoding="";
    
    private String description="";
    
    /** Creates a new instance of PdfEncoding */
    public PdfEncoding(String encoding,String description) {
        this.encoding = encoding;
        this.description = description;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property encoding.
     * @return Value of property encoding.
     *
     */
    public java.lang.String getEncoding() {
        return encoding;
    }
    
    /** Setter for property encoding.
     * @param encoding New value of property encoding.
     *
     */
    public void setEncoding(java.lang.String encoding) {
        this.encoding = encoding;
    }
    
    public String toString()
    {
        return getDescription()+"";
    }
    
}
