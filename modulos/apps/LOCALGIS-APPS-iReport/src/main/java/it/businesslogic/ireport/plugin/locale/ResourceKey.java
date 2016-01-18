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
 * ResourceKey.java
 * 
 * Created on March 28, 2006, 9:50 PM
 *
 */

package it.businesslogic.ireport.plugin.locale;

/**
 *
 * @author gtoffoli
 */
public class ResourceKey {
    
    private String key = "";
    private boolean missing = false;
    
    /** Creates a new instance of ResourceKey */
    public ResourceKey() {
    }

    
    /** Creates a new instance of ResourceKey */
    public ResourceKey(String key, boolean missing) {
        this.key = key;
        this.missing = missing;
    }
    
    public ResourceKey(String key) {
        this(key, false);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }
    
    public String toString()
    {
        return getKey();
    }
}
