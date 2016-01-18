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
 * TimeSeries.java
 * 
 * Created on 17 agosto 2005, 10.56
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class TimeSeries {
    
    private String seriesExpression = "";
    private String timePeriodExpression = "";
    private String valueExpression = "";
    private String labelExpression = "";
    private SectionItemHyperlink sectionItemHyperlink = new SectionItemHyperlink();
    
    /** Creates a new instance of CategorySeries */
    public TimeSeries() {
    }

    public String getSeriesExpression() {
        return seriesExpression;
    }

    public void setSeriesExpression(String seriesExpression) {
        this.seriesExpression = seriesExpression;
    }

    public String getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    public String getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(String labelExpression) {
        this.labelExpression = labelExpression;
    }
    
    public String toString()
    {
        String str = getSeriesExpression();
        if (str == null) str = "";
        if (str.length() > 50) str = str.substring(0,30);
        
        return "Time series [" + str + "]";
    }

    public String getTimePeriodExpression() {
        return timePeriodExpression;
    }

    public void setTimePeriodExpression(String timePeriodExpression) {
        this.timePeriodExpression = timePeriodExpression;
    }
    
    public TimeSeries cloneMe()
    {
        TimeSeries cs = new TimeSeries();
        cs.setSeriesExpression( this.getSeriesExpression());
        cs.setTimePeriodExpression( this.getTimePeriodExpression());
        cs.setValueExpression( this.getValueExpression());
        cs.setLabelExpression( this.getLabelExpression());
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
