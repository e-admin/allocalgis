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
 * TimePeriodSeries.java
 * 
 * Created on 17 agosto 2005, 10.56
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class TimePeriodSeries {
    
    private String seriesExpression = "";
    private String startDateExpression = "";
    private String endDateExpression = "";
    private String valueExpression = "";
    private String labelExpression = "";
    private SectionItemHyperlink sectionItemHyperlink = new SectionItemHyperlink();
    
    /** Creates a new instance of CategorySeries */
    public TimePeriodSeries() {
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
        
        return "Time period series [" + str + "]";
    }

    public String getStartDateExpression() {
        return startDateExpression;
    }

    public void setStartDateExpression(String startDateExpression) {
        this.startDateExpression = startDateExpression;
    }

    public String getEndDateExpression() {
        return endDateExpression;
    }

    public void setEndDateExpression(String endDateExpression) {
        this.endDateExpression = endDateExpression;
    }
    
    public TimePeriodSeries cloneMe()
    {
        TimePeriodSeries cs = new TimePeriodSeries();
        cs.setSeriesExpression( this.getSeriesExpression());
        cs.setStartDateExpression( this.getStartDateExpression());
        cs.setEndDateExpression( this.getEndDateExpression());
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
