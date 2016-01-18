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
 * Chart.java
 * 
 * Created on 8 luglio 2005, 18.00
 *
 */

package it.businesslogic.ireport.chart;
import it.businesslogic.ireport.HyperLinkableReportElement;
/**
 *
 * @author Administrator
 */
public class Chart implements HyperLinkableReportElement {
    
    private ChartTitle title = null;
    private ChartTitle subTitle = null;
    private ChartLegend legend = null;
    private boolean showLegend = true;
    protected Plot plot = null;
    private Dataset dataset = null;
    
    private String name = "Generic chart";
    
    private String customizerClass = "";
    
    private java.awt.Image chartImage = null;
    
    private String anchorNameExpression = "";
    
    private String hyperlinkAnchorExpression = "";
    
    private String hyperlinkPageExpression = "";
    
    private String hyperlinkReferenceExpression = "";
    
    private String hyperlinkType = "None";
    
    private String hyperlinkTarget = "Self";
    
    private int bookmarkLevel = 0;
    
    private String tooltipExpression = "";
    
    private java.util.List linkParameters = new java.util.ArrayList();
    
    /** Creates a new instance of Chart */
    public Chart() {
        
        setTitle(new ChartTitle(""));
        setSubTitle(new ChartTitle(""));
        setLegend(new ChartLegend());
        plot = new Plot(); // This line shold be overridden in the derived classes.. 
        dataset = new Dataset();
          
    }
    
    public ChartTitle getTitle() {
        return title;
    }

    public void setTitle(ChartTitle title) {
        this.title = title;
    }

    public ChartTitle getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(ChartTitle subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public java.awt.Image getChartImage() {
        return chartImage;
    }

    public void setChartImage(java.awt.Image chartImage) {
        this.chartImage = chartImage;
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    public Chart cloneMe()
    {
        Chart chart = cloneBaseChart();
        chart.setTitle( getTitle().cloneMe() );
        chart.setSubTitle( getSubTitle().cloneMe() );
        chart.setLegend( getLegend().cloneMe());
        chart.setPlot( getPlot().cloneMe() );
        chart.setDataset( getDataset().cloneMe() );
        chart.setShowLegend( isShowLegend() );     
        chart.setAnchorNameExpression( getAnchorNameExpression() );
        chart.setHyperlinkAnchorExpression( getHyperlinkAnchorExpression() );
        chart.setHyperlinkPageExpression( getHyperlinkPageExpression() );
        chart.setHyperlinkReferenceExpression( getHyperlinkReferenceExpression() );
        chart.setHyperlinkType( getHyperlinkType() );
        chart.setHyperlinkTarget( getHyperlinkTarget() );
        chart.setCustomizerClass( getCustomizerClass() );
        
        return chart;
    }
    
    public Chart cloneBaseChart()
    {
        return new Chart();
    }

    public String getAnchorNameExpression() {
        return anchorNameExpression;
    }

    public void setAnchorNameExpression(String anchorNameExpression) {
        this.anchorNameExpression = anchorNameExpression;
    }

    public String getHyperlinkAnchorExpression() {
        return hyperlinkAnchorExpression;
    }

    public void setHyperlinkAnchorExpression(String hyperlinkAnchorExpression) {
        this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
    }

    public String getHyperlinkPageExpression() {
        return hyperlinkPageExpression;
    }

    public void setHyperlinkPageExpression(String hyperlinkPageExpression) {
        this.hyperlinkPageExpression = hyperlinkPageExpression;
    }

    public String getHyperlinkReferenceExpression() {
        return hyperlinkReferenceExpression;
    }

    public void setHyperlinkReferenceExpression(String hyperlinkReferenceExpression) {
        this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
    }

    public String getHyperlinkType() {
        return hyperlinkType;
    }

    public void setHyperlinkType(String hyperlinkType) {
        this.hyperlinkType = hyperlinkType;
    }

    public String getHyperlinkTarget() {
        return hyperlinkTarget;
    }

    public void setHyperlinkTarget(String hyperlinkTarget) {
        this.hyperlinkTarget = hyperlinkTarget;
    }

    public int getBookmarkLevel() {
        return bookmarkLevel;
    }

    public void setBookmarkLevel(int bookmarkLevel) {
        this.bookmarkLevel = bookmarkLevel;
    }

    public String getCustomizerClass() {
        return customizerClass;
    }

    public void setCustomizerClass(String customizerClass) {
        this.customizerClass = customizerClass;
    }

    public java.util.List getLinkParameters() {
        return linkParameters;
    }

    public void setLinkParameters(java.util.List linkParameters) {
        this.linkParameters = linkParameters;
    }

    public ChartLegend getLegend() {
        return legend;
    }

    public void setLegend(ChartLegend legend) {
        this.legend = legend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTooltipExpression() {
        return tooltipExpression;
    }

    public void setTooltipExpression(String tooltipExpression) {
        this.tooltipExpression = tooltipExpression;
    }
    
}
