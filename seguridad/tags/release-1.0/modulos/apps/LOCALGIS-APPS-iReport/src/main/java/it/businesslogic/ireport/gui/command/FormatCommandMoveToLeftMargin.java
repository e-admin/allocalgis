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
 * FormatCommandMoveToLeftMargin.java
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
 * Move all elements towards to the left margin over a distance depicted
 * by the distance of the left most element to the left margin
 *
 * From the introduction this command the distance of the first element to the left margin had been taken.
 * This resulted in unwanted behaviour. If the first element was the most right element, then most elements were shifted
 * left of the left margin.
 *
 * Now this is corrected.
 */
public class FormatCommandMoveToLeftMargin extends FormatCommand {
    
    {
        operationType = OperationType.MOVE_TO_LEFT_MARGIN;
    }
    int deltaLeft = 0;
    
    void executeDeeper() {
        
        Vector bands = getBands();  // bands with selected elements
        for (Iterator h = bands.iterator(); h.hasNext(); ) {
            Band b = (Band) h.next();
            Vector bandElements = getBandElements( b );
            bandElements = sortXY( bandElements.elements());
            
            ReportElement re = (ReportElement) bandElements.elementAt(0);
            deltaLeft = re.getPosition().x - jrf.getReport().getLeftMargin();
            
            processElements(bandElements.elements());
        }
        
    }
    
    public void modify() {
        re.setPosition(new Point(re.getPosition().x + 10 - deltaLeft,  re.getPosition().y));
    }
    
}
