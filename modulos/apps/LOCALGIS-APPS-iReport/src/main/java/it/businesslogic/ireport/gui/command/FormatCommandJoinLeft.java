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
 * FormatCommandJoinLeft.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 *
 */
public class FormatCommandJoinLeft extends FormatCommand {
    int actual_x;
    
    {
        operationType = OperationType.JOIN_LEFT;
    }
    
    void executeDeeper() {
        
        Vector bands = getBands();
        
        if (bands.size() == 0 && getSelectedElements().size() > 0)
        {
            // We are working on a crosstab...
            Vector myElements = (Vector)getSelectedElements().clone();
            myElements = sortXY( myElements.elements() );
            actual_x = ((ReportElement) myElements.elementAt( 0 )).getPosition().x;
            processElements( getSelectedElements().elements() );
        }
        else
        {
            for (Iterator h = bands.iterator(); h.hasNext(); ) {

                Band b = (Band) h.next();

                Vector myElements = getBandElements(b);
                myElements = sortXY( myElements.elements());

                // Repositioning elements...
                actual_x = ((ReportElement) myElements.elementAt( 0 )).getPosition().x;

                // use myELements instead of jrf.getElements()
                processElements( myElements.elements() );

            }
        }
        
    }
    
    public void modify() {
        re.setPosition( new Point(actual_x, re.getPosition().y) );
        actual_x += re.getWidth();
        
    }
    
}
