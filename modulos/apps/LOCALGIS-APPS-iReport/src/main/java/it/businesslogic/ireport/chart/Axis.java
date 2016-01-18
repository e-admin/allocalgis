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
 * Axis.java
 * 
 * Created on September 29, 2006, 11:11 PM
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.ChartReportElement2;

/**
 *
 * @author gtoffoli
 */
public class Axis {
    
    private String position = "leftOrTop";
    private ChartReportElement2 chartReportElement = null;
    
    /** Creates a new instance of Axis */
    public Axis() {
        setChartReportElement(new ChartReportElement2(0, 0, 0, 0));
        getChartReportElement().setChart( new BarChart() );   
    }

    public Axis cloneMe()
    {
        Axis obj = new Axis();
        obj.setPosition( new String( getPosition()));
        obj.setChartReportElement( (ChartReportElement2)getChartReportElement().cloneMe() );
        
        return obj;
    }
    
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    

    public ChartReportElement2 getChartReportElement() {
        return chartReportElement;
    }

    public void setChartReportElement(ChartReportElement2 chartReportElement) {
        this.chartReportElement = chartReportElement;
    }
    
    public String toString()
    {
        return getChartReportElement().getChart().getName();
    }
}
