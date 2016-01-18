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
 * CategorySeries.java
 * 
 * Created on 17 agosto 2005, 10.56
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class CategorySeries {
    
    private String seriesExpression = "";
    private String categoryExpression = "";
    private String valueExpression = "";
    private String labelExpression = "";
    private SectionItemHyperlink sectionItemHyperlink = new SectionItemHyperlink();
    
    /** Creates a new instance of CategorySeries */
    public CategorySeries() {
    }

    public String getSeriesExpression() {
        return seriesExpression;
    }

    public void setSeriesExpression(String seriesExpression) {
        this.seriesExpression = seriesExpression;
    }

    public String getCategoryExpression() {
        return categoryExpression;
    }

    public void setCategoryExpression(String categoryExpression) {
        this.categoryExpression = categoryExpression;
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
        
        return "Category series [" + str + "]";
    }
    
    public CategorySeries cloneMe()
    {
        CategorySeries cs = new CategorySeries();
        cs.setSeriesExpression( this.getSeriesExpression());
        cs.setCategoryExpression( this.getCategoryExpression());
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
