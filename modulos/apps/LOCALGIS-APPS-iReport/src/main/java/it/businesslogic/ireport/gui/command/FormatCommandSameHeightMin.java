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
 * FormatCommandSameHeightMin.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.TransformationType;

import java.awt.Point;
import java.util.Enumeration;


/**
 *
 */
public class FormatCommandSameHeightMin extends FormatCommand {
    {
        operationType = OperationType.SAME_HEIGHT_MIN;
    }
    
    
    int height;
    
    public void preparation() {
        ReportElement re = (ReportElement) this.getSelectedElements().firstElement();
        // Height is at least the height of one of the selected elements.
        // If it were set to zero then it would never get a higher value.
        height = re.getHeight();

        Enumeration enum2 = this.getSelectedElements().elements();
        while (enum2.hasMoreElements()) {
            re = (ReportElement)enum2.nextElement();
            if (height > re.getHeight()) height = re.getHeight();
        }
        
    }
    
    public void modify() {
        re.trasform(new Point(0, height - re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
    }
    
}
