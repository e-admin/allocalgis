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
 * FormatCommandAlignTopToBand.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.TransformationType;
import java.awt.Point;

/**
 *
 */
public class FormatCommandAlignTopToBand extends FormatCommand {
    {
        operationType = OperationType.ALIGN_TOP_TO_BAND;
    }
    
    public void modify() {
        
        if (re.getBand() != null)
        {
            re.trasform(new Point(0, jrf.getReport().getBandYLocation( re.band) + 10- re.getPosition().y),
                TransformationType.TRANSFORMATION_MOVE);
    
        } else if (re.getCell() != null)
        {
            re.setPosition(new Point(re.getPosition().x, re.getCell().getTop() + 10));
            re.updateRelativePosition();
        }
    }
    
}
