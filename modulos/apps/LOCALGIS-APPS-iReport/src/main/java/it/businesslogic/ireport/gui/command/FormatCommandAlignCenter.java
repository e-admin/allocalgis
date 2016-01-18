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
 * FormatCommandAlignCenter.java
 * 
 * Created on 9 mei 2005, 21:36
 *
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.OperationType;

import java.awt.Point;


public class FormatCommandAlignCenter
    extends FormatCommand
{

    /**
     * Creates a new FormatCommandAlignCenter object.
     */
    FormatCommandAlignCenter()
    {
        operationType = OperationType.ALIGN_CENTER;
    }

    int centerx;

    /**
     * DOCUMENT ME!
     */
    public void preparation()
    {
        centerx = 10 + jrf.getReport().getWidth() / 2;
    }

    /**
     * DOCUMENT ME!
     */
    public void modify()
    {

        int centery = jrf.getReport().getBandYLocation(re.getBand()) + 10 + 
                      re.getBand().getHeight() / 2;
        re.setPosition(new Point(centerx - (re.getWidth() / 2), 
                                 centery - (re.getHeight() / 2)));
    }

    /**
     * Returns the value of centerx.
     * @return centerx!
     */
    public int getCenterx()
    {

        return centerx;
    }

    /**
     * Sets the value of centerx.
     * 
     * @param centerx The value to assign centerx.
     */
    public void setCenterx(int centerx)
    {
        this.centerx = centerx;
    }
}
