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
 * FormatCommandAlignCenterHorizontally.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;

import java.awt.Point;

/**
 *
 */
public class FormatCommandAlignCenterHorizontally extends FormatCommand {
    {
        operationType = OperationType.ALIGN_CENTER_HORIZONTALLY; 
    }
    
    int center;
    
    
    public void preparation() {
        center = 10 + jrf.getReport().getWidth()/2;
    }
    
    public void modify() {
        re.setPosition(new Point(center - (re.getWidth() / 2), re.getPosition().y));
    }
    
}
