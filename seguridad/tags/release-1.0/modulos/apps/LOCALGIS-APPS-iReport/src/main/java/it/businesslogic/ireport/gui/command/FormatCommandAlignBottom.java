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
 * FormatCommandAlignBottom.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;

import java.awt.Point;


public class FormatCommandAlignBottom
        extends FormatCommand {
    FormatCommandAlignBottom() {
        operationType = OperationType.ALIGN_TOP;
    }
    
    int bottom;
    
    // Preparation is called before the while loop
    
    
    /**
     * DOCUMENT ME!
     */
    void preparation() {
        bottom = ((ReportElement)this.getSelectedElements().firstElement()).getPosition().y +
                ((ReportElement)this.getSelectedElements().firstElement()).height;
        
    }
    
    /**
     * DOCUMENT ME!
     */
    void modify() {
        re.setPosition(new Point(re.getPosition().x, bottom - re.height));
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param bottom DOCUMENT ME!
     */
    void setBottom(int bottom) {
        this.bottom = bottom;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getBottom() {
        
        return bottom;
    }
}
