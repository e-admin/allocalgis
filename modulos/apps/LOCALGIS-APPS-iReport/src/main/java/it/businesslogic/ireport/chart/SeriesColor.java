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
 * SeriesColor.java
 * 
 * Created on September 27, 2006, 12:40 AM
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author gtoffoli
 */
public class SeriesColor implements Comparable {
    
    private int seriesOrder = 1;
    private java.awt.Color color = null;
    /** Creates a new instance of SeriesColor */
    public SeriesColor() {
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public int getSeriesOrder() {
        return seriesOrder;
    }

    public void setSeriesOrder(int seriesOrder) {
        this.seriesOrder = seriesOrder;
    }

    public int compareTo(Object o) {
        
        if (o instanceof SeriesColor)
        {
            SeriesColor sc = (SeriesColor)o;
            
            return getSeriesOrder() - sc.getSeriesOrder();
        }
        
        return 0;
    }
    
}
