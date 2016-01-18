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
 * XYZSeries.java
 * 
 * Created on 17 agosto 2005, 10.56
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class XYZSeries {
    
    private String seriesExpression = "";
    private String xValueExpression = "";
    private String yValueExpression = "";
    private String zValueExpression = "";
    private SectionItemHyperlink sectionItemHyperlink = new SectionItemHyperlink();
    
    /** Creates a new instance of CategorySeries */
    public XYZSeries() {
    }

    public String getSeriesExpression() {
        return seriesExpression;
    }

    public void setSeriesExpression(String seriesExpression) {
        this.seriesExpression = seriesExpression;
    }
    
    public String toString()
    {
        String str = getSeriesExpression();
        if (str == null) str = "";
        if (str.length() > 50) str = str.substring(0,30);
        
        return "XYZ series [" + str + "]";
    }

    public String getXValueExpression() {
        return xValueExpression;
    }

    public void setXValueExpression(String xValueExpression) {
        this.xValueExpression = xValueExpression;
    }

    public String getYValueExpression() {
        return yValueExpression;
    }

    public void setYValueExpression(String yValueExpression) {
        this.yValueExpression = yValueExpression;
    }

    public String getZValueExpression() {
        return zValueExpression;
    }

    public void setZValueExpression(String zValueExpression) {
        this.zValueExpression = zValueExpression;
    }
    
    public XYZSeries cloneMe()
    {
        XYZSeries cs = new XYZSeries();
        cs.setSeriesExpression( this.getSeriesExpression());
        cs.setXValueExpression( this.getXValueExpression());
        cs.setYValueExpression( this.getYValueExpression());
        cs.setZValueExpression( this.getZValueExpression());
        cs.setSectionItemHyperlink( this.getSectionItemHyperlink().cloneMe());
        
        return cs;
    }

    public SectionItemHyperlink getSectionItemHyperlink() {
        return sectionItemHyperlink;
    }

    public void setSectionItemHyperlink(SectionItemHyperlink sectionItemHyperlink) {
        this.sectionItemHyperlink = sectionItemHyperlink;
    }
    
}
