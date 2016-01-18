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
 * FormatCommandIncreaseSpaceH.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;


/**
 *
 *
 */
public class FormatCommandIncreaseSpaceH extends FormatCommand {
    {
        operationType = OperationType.INCREASE_SPACE_H;
    }
    
    boolean preCondition(){
        return ( this.getSelectedElements().size() > 1);
    }
    
    void executeDeeper(){
        Vector bands = getBands();  // bands with selected elements
        for (Iterator h = bands.iterator(); h.hasNext(); ) {
            Band b = (Band) h.next();
            Vector bandElements = getBandElements( b );
            bandElements = sortXY( bandElements.elements());
            // first element in each band will not be moved.
            bandElements.remove(0);
            processElements(bandElements.elements());
        }
    }
    
    public void modify() {
        // first element is the second in the band 
        re.setPosition(new Point(re.getPosition().x+5*(getCurrentElementPosition()+1), re.getPosition().y) );
    }
    
}
