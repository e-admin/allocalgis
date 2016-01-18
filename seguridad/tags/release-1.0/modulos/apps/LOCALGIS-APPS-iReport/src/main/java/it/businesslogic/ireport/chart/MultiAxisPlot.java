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
 * MultiAxisPlot.java
 * 
 * Created on 16 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.chart.Axis;

/**
 *
 * @author Administrator
 */
public class MultiAxisPlot extends Plot{
    
    private java.util.List axis = new java.util.ArrayList();
    
    /** Creates a new instance of PiePlot */
    public MultiAxisPlot() {
    }
    
    public Plot cloneMe()
    {
        MultiAxisPlot obj = new MultiAxisPlot();
        copyBasePlot(obj);
        for (int i=0; i<getAxis().size(); ++i)
        {
            obj.getAxis().add( ((Axis)getAxis().get(i)).cloneMe()  );
        }
        
        return obj;
    }

    public java.util.List getAxis() {
        return axis;
    }

    public void setAxis(java.util.List axis) {
        this.axis = axis;
    }
}
