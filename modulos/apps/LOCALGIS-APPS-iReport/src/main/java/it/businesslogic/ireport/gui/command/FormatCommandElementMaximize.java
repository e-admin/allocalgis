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
 * FormatCommandElementMaximize.java
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
public class FormatCommandElementMaximize extends FormatCommand {
    
    {
        operationType = OperationType.ELEMENT_MAXIMIZE;
    }
    
    void executeDeeper() {
        
        resetEnumeration();

        processElements();  //register all elements once!
        
        sleepUndo = true;
        
        getCommand(OperationType.ALIGN_TOP_TO_BAND).execute();
        getCommand(OperationType.ALIGN_TO_LEFT_MARGIN).execute();
        
        int height = 0;
        int insideWidth = jrf.getReport().getWidth() - jrf.getReport().getRightMargin() - jrf.getReport().getLeftMargin() ;
        
        // this is the class processElements and here without the undo registration
        resetEnumeration();
        while (e.hasMoreElements()) {
            re = (ReportElement) e.nextElement();
            
            if (re.getBand() != null)
            {
                height = re.getBand().getHeight();
            
                re.trasform(new Point(insideWidth-re.getWidth(), height-re.getHeight()), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
            }
            else
            {
                re.trasform(new Point(re.getCell().getWidth()-re.getWidth(), re.getCell().getHeight()-re.getHeight()), TransformationType.TRANSFORMATION_RESIZE_SE);
                re.updateBounds();
            }
        }
        
        updateElements();
        
        sleepUndo = false;
    }
    
}
