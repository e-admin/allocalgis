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
 * FormatCommandElementMaximizeH.java
 * 
 * Created on 17 mei 2005, 21:00
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
public class FormatCommandElementMaximizeH extends FormatCommand {
    // Preparation is called before the while loop
    {
        operationType = OperationType.ELEMENT_MAXIMIZE_H;
    }
    
    void executeDeeper() {
        
        resetEnumeration();

        registerElements();  //register all elements once with no modification
                             // registerElements does not call modify().
                             // it simply register the elements to the undoOp stack.
        
        sleepUndo = true;
    
        getCommand(OperationType.ALIGN_TO_LEFT_MARGIN).execute();
        int insideWidth = jrf.getReport().getWidth() - jrf.getReport().getRightMargin() - jrf.getReport().getLeftMargin() ;
        
        // this is the class processElements and here without the undo registration
        resetEnumeration();
        while (e.hasMoreElements()) {
            re = (ReportElement) e.nextElement();
            
            if (re.getCell() != null)
            {
                re.setWidth(re.getCell().getWidth());
                //re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
                
            }
            else
            {
                re.setWidth( insideWidth );
                //re.trasform(new Point(insideWidth - re.getWidth(),0), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
            }
        }
        
        updateElements();
        
        sleepUndo = false;
    }
    
}
