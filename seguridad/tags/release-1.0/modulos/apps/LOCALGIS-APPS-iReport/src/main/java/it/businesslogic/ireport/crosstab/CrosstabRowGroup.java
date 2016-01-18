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
 * CrosstabRowGroup.java
 * 
 * Created on 5 gennaio 2006, 16.03
 *
 */

package it.businesslogic.ireport.crosstab;

import it.businesslogic.ireport.crosstab.*;

/**
 *
 * @author Administrator
 */
public class CrosstabRowGroup extends CrosstabGroup {
        
    /** Creates a new instance of CrosstabRowGroup */
    public CrosstabRowGroup() {
        
        this.headerPosition = "Top";
        this.setSize(100);
    }

    public int getWidth() {
        return getSize();
    }

    public void setWidth(int width) {
        this.setSize(width);
    }
    
}
