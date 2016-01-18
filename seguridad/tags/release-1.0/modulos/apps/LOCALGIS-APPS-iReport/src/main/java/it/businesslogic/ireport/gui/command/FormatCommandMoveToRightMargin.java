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
 * FormatCommandMoveToRightMargin.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import java.awt.Point;
import java.util.Enumeration;

/**
 * Move all elements towards to the right margin over a distance depicted
 * by the distance of the element with the right most EDGE to the right margin
 * A distance left of the right margin is considered positive.
 * Right of the right margin is considered negative.
 */
public class FormatCommandMoveToRightMargin extends FormatCommand {
    {
        operationType = OperationType.MOVE_TO_RIGHT_MARGIN;
    }
    
    int deltaRight;
    
    public void preparation() {
        // Find the element width the right most edge
        ReportElement re = null;
        ReportElement rightMostRe = (ReportElement) this.getSelectedElements().firstElement();
        
        Enumeration enum2 = this.getSelectedElements().elements();
        while (enum2.hasMoreElements()) {
            re = (ReportElement) enum2.nextElement();
            if ( (re.getPosition().x + re.getWidth()) > (rightMostRe.getPosition().x + rightMostRe.getWidth()) ) {
                rightMostRe = re;
            }
        }
        
        deltaRight = jrf.getReport().getRightMargin() - rightMostRe.getPosition().x;
        
    }
    
    public void modify() {
        re.setPosition(new Point(re.getPosition().x + 10 + deltaRight,  re.getPosition().y));
    }
    
}
