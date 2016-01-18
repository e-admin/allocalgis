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
 * MeterInterval.java
 * 
 * Created on September 28, 2006, 4:56 PM
 *
 */

package it.businesslogic.ireport.chart;

import java.awt.Color;

/**
 *
 * @author gtoffoli
 */
public class MeterInterval {
    
    private DataRange dataRange = new DataRange();
    private String label = "";
    private Color color = null;
    private double alpha = 1.0;
    
    /** Creates a new instance of MeterInterval */
    public MeterInterval() {
    }

    public MeterInterval cloneMe()
    {
        MeterInterval obj = new MeterInterval();
        if (getColor() != null) obj.setColor( new Color( getColor().getRGB()));
        obj.setLabel( new String( label ));
        obj.setAlpha( getAlpha() );
        obj.setDataRange(  getDataRange().cloneMe());
        
        return obj;
    }
    
    public DataRange getDataRange() {
        return dataRange;
    }

    public void setDataRange(DataRange dataRange) {
        this.dataRange = dataRange;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    
}
