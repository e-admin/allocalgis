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
 * LanguageChangedEvent.java
 * 
 * Created on 21 maggio 2004, 9.33
 *
 */

package it.businesslogic.ireport.util;

/**
 *
 * @author  Administrator
 */
public class LanguageChangedEvent {
    
    private java.util.Locale locale;
    
    /** Creates a new instance of LanguageChangedEvent */
    public LanguageChangedEvent(java.util.Locale locale) {
        
        this.locale = locale;
        
    }
    
    /** Getter for property locale.
     * @return Value of property locale.
     *
     */
    public java.util.Locale getLocale() {
        return locale;
    }
    
    /** Setter for property locale.
     * @param locale New value of property locale.
     *
     */
    public void setLocale(java.util.Locale locale) {
        this.locale = locale;
    }
    
}
