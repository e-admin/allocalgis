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
 * HighLowDataset.java
 * 
 * Created on 15 agosto 2005, 17.49
 *
 */

package it.businesslogic.ireport.chart;
/**
 *
 * @author Administrator
 */
public class HighLowDataset extends Dataset {
    
    private String seriesExpression = "";
    private String dateExpression = "";
    private String highExpression = "";
    private String lowExpression = "";
    private String openExpression = "";
    private String closeExpression = "";
    private String volumeExpression = "";
    private SectionItemHyperlink sectionHyperLink = new SectionItemHyperlink();
    
    /** Creates a new instance of PieDataset */
    public HighLowDataset() {
        super();
    }   

    public String getSeriesExpression() {
        return seriesExpression;
    }

    public void setSeriesExpression(String seriesExpression) {
        this.seriesExpression = seriesExpression;
    }

    public String getDateExpression() {
        return dateExpression;
    }

    public void setDateExpression(String dateExpression) {
        this.dateExpression = dateExpression;
    }

    public String getHighExpression() {
        return highExpression;
    }

    public void setHighExpression(String highExpression) {
        this.highExpression = highExpression;
    }

    public String getLowExpression() {
        return lowExpression;
    }

    public void setLowExpression(String lowExpression) {
        this.lowExpression = lowExpression;
    }

    public String getOpenExpression() {
        return openExpression;
    }

    public void setOpenExpression(String openExpression) {
        this.openExpression = openExpression;
    }

    public String getCloseExpression() {
        return closeExpression;
    }

    public void setCloseExpression(String closeExpression) {
        this.closeExpression = closeExpression;
    }

    public String getVolumeExpression() {
        return volumeExpression;
    }

    public void setVolumeExpression(String volumeExpression) {
        this.volumeExpression = volumeExpression;
    }
    
    public Dataset cloneMe()
    {
        HighLowDataset obj = new HighLowDataset();
        copyBaseDataset(obj);
        obj.setSeriesExpression(getSeriesExpression());
        obj.setDateExpression( getDateExpression());
        obj.setCloseExpression( getCloseExpression());
        obj.setOpenExpression( getOpenExpression());
        obj.setHighExpression( getHighExpression());
        obj.setLowExpression( getLowExpression() );
        obj.setVolumeExpression( getVolumeExpression());
        obj.setItemHyperLink( getItemHyperLink().cloneMe());
        
        return obj;
    }

    public SectionItemHyperlink getItemHyperLink() {
        return sectionHyperLink;
    }

    public void setItemHyperLink(SectionItemHyperlink secionHyperLink) {
        this.sectionHyperLink = secionHyperLink;
    }
}
