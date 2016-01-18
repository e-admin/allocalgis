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
 * FormatCommandSameSize.java
 * 
 * Created on 9 mei 2005, 21:36
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
public class FormatCommandSameSize extends FormatCommand {
    {
        operationType = OperationType.SAME_SIZE;
    }
    
    
    int width;
    int height;
    
    
    public void preparation() {
        ReportElement re = ((ReportElement) this.getSelectedElements().firstElement());
        width = re.getWidth();
        height = re.getHeight();
    }
    
    public void modify() {
        re.trasform(new Point(width - re.getWidth(), height - re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
    }
    
}
