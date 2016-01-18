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
 * PieDataset.java
 * 
 * Created on 15 agosto 2005, 17.49
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class PieDataset extends Dataset {
    
    private String keyExpression = "";
    private String valueExpression = "";
    private String labelExpression = "";
    
    private SectionItemHyperlink sectionHyperLink = new SectionItemHyperlink();
        
    /** Creates a new instance of PieDataset */
    public PieDataset() {
        super();
    }

    public String getKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(String keyExpression) {
        this.keyExpression = keyExpression;
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
    
    public Dataset cloneMe()
    {
        PieDataset obj = new PieDataset();
        copyBaseDataset(obj);
        obj.setLabelExpression(getLabelExpression());
        obj.setKeyExpression( getKeyExpression());
        obj.setValueExpression( getValueExpression());
        obj.setSectionHyperLink( getSectionHyperLink().cloneMe());
        
        return obj;
    }  

    public SectionItemHyperlink getSectionHyperLink() {
        return sectionHyperLink;
    }

    public void setSectionHyperLink(SectionItemHyperlink secionHyperLink) {
        this.sectionHyperLink = secionHyperLink;
    }
    
}
