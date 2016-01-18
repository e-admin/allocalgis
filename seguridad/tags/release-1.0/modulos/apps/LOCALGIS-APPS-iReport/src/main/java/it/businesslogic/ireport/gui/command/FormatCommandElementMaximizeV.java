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
 * FormatCommandElementMaximizeV.java
 * 
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.TransformationType;
import java.awt.Point;

/**
 *
 */
public class FormatCommandElementMaximizeV extends FormatCommand {
    // Preparation is called before the while loop
    {
        operationType = OperationType.ELEMENT_MAXIMIZE_V;
    }
    
    void executeDeeper() {
        
        resetEnumeration();

        processElements();  //register all elements once with no modification
        
        sleepUndo = true;
    
        getCommand(OperationType.ALIGN_TOP_TO_BAND).execute();
        int height = 0 ;
        
        // this is the class processElements and here without the undo registration
        resetEnumeration();
        while (e.hasMoreElements()) {
            re = (ReportElement) e.nextElement();
            
            if (re.getCell() != null)
            {
                height = re.getCell().getHeight();
                re.setHeight(height);
                //re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
                
            }
            else
            {
                height = re.getBand().getHeight();
                re.setHeight(height);
                //re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
            }
        }
        
        updateElements();
        
        sleepUndo = false;
    }
    
}
