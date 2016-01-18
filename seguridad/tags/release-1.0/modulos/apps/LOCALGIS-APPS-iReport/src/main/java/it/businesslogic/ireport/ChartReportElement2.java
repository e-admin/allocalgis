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
 * ChartReportElement2.java
 * 
 * Created on 8 luglio 2005, 17.06
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.chart.Chart;
import java.awt.*;
import java.util.Vector;
/**
 *
 * @author Administrator
 */
public class ChartReportElement2 extends ReportElement implements BoxElement, java.awt.image.ImageObserver, HyperLinkableReportElement {
    
    private Box box = null;
    private Chart chart = null;
    private String evaluationTime="Now";
    private String evaluationGroup="";
    private String tooltipExpression = "";
    
    /** Creates a new instance of JRChartReportElement */
    public ChartReportElement2(int x, int y, int width, int height)
   {
	  super(x,y,width,height);
      	  setBox(new Box());	  
          
   }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }
    
   public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
   {
          position.x += -10 + box.getLeftPadding();
		position.y += -10 + box.getTopPadding();
                width = width - box.getLeftPadding() - box.getRightPadding();
                height = height - box.getTopPadding() - box.getBottomPadding();

		x_shift_origin -= 10;
		y_shift_origin -= 10;
                
		this.zoom_factor = zoom_factor;
		Image imgx = getChart().getChartImage();
		
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                
		if (imgx != null)
		{
                    int imageWidth = imgx.getWidth(this);
                    int imageHeight = imgx.getHeight(this);
                    /*
			if (imgx instanceof Image)
			{
				((Image)imgx).setTransparent(true);
			}
                     */
			
			// 
			
                            
                    g.drawImage(imgx, getZoomedDim(position.x)-x_shift_origin,
                    getZoomedDim(position.y)-y_shift_origin,
                    getZoomedDim(position.x+width)-x_shift_origin,
                    getZoomedDim(position.y+height)-y_shift_origin,
                    0,0,imageWidth,imageHeight,null,this);

                                //-x_shift_origin+ getZoomedDim( position.x-10)+10 ,-y_shift_origin+getZoomedDim(position.y)+10, getZoomedDim( this.width), getZoomedDim( this.height),
                        
		}
                
                if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("Antialias","true").equals("false"))
                {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
                }
                //position.x += 10;
		//position.y += 10;
                
                position.x += 10 - box.getLeftPadding();
		position.y += 10 - box.getTopPadding();
                width = width + box.getLeftPadding() + box.getRightPadding();
                height = height + box.getTopPadding() + box.getBottomPadding();

                
		x_shift_origin += 10;
		y_shift_origin += 10;
                
                super.drawBorder((Graphics2D)g, zoom_factor,  x_shift_origin,y_shift_origin);
                drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin,getBox());
   }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getEvaluationGroup() {
        return evaluationGroup;
    }

    public void setEvaluationGroup(String evaluationGroup) {
        this.evaluationGroup = evaluationGroup;
    }
    
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
    
    public ReportElement cloneMe()
    {
	ChartReportElement2 newReportElement = new ChartReportElement2(position.x, position.y, width, height);
	copyBaseReportElement(newReportElement, this);
	return newReportElement;
    }
    
    public void copyBaseReportElement(ReportElement destination, ReportElement source)
    {
            super.copyBaseReportElement(destination, source);

            if (destination instanceof ChartReportElement2 &&
                source instanceof ChartReportElement2 )
            {
                ((ChartReportElement2)destination).setBox( ((ChartReportElement2)source).getBox().cloneMe() );
                ((ChartReportElement2)destination).setChart( ((ChartReportElement2)source).getChart().cloneMe() );
                ((ChartReportElement2)destination).setEvaluationGroup( ((ChartReportElement2)source).getEvaluationGroup() );
                ((ChartReportElement2)destination).setEvaluationTime( ((ChartReportElement2)source).getEvaluationTime() );
            }
    }

    public String getAnchorNameExpression() {
        return getChart().getAnchorNameExpression();
    }

    public String getHyperlinkAnchorExpression() {
        return getChart().getHyperlinkAnchorExpression();
    }

    public String getHyperlinkPageExpression() {
        return getChart().getHyperlinkPageExpression();
    }

    public String getHyperlinkReferenceExpression() {
        return getChart().getHyperlinkReferenceExpression();
    }

    public String getHyperlinkTarget() {
        return getChart().getHyperlinkTarget();
    }

    public String getHyperlinkType() {
        return getChart().getHyperlinkType();
    }

    public void setAnchorNameExpression(String anchorNameExpression) {
        getChart().setAnchorNameExpression(anchorNameExpression);
    }

    public void setHyperlinkAnchorExpression(String hyperlinkAnchorExpression) {
        getChart().setHyperlinkAnchorExpression(hyperlinkAnchorExpression);
    }

    public void setHyperlinkPageExpression(String hyperlinkPageExpression) {
        getChart().setHyperlinkPageExpression(hyperlinkPageExpression);
    }

    public void setHyperlinkReferenceExpression(String hyperlinkReferenceExpression) {
        getChart().setHyperlinkReferenceExpression(hyperlinkReferenceExpression);
    }

    public void setHyperlinkTarget(String hyperlinkTarget) {
        getChart().setHyperlinkTarget(hyperlinkTarget);
    }

    public void setHyperlinkType(String hyperlinkType) {
        getChart().setHyperlinkType(hyperlinkType);
    }

    public int getBookmarkLevel() {
        return getChart().getBookmarkLevel();
    }

    public void setBookmarkLevel(int bookmarkLevel) {
        getChart().setBookmarkLevel(bookmarkLevel);
    }
    
    public void setStyle(Style style) {
     
        super.setStyle(style);
        
        if (style != null)
        {            
            // BOX
            if (style.getAttributeString(style.ATTRIBUTE_border, null, true) != null) 
                this.getBox().setBorder(  style.getAttributeString(style.ATTRIBUTE_border, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true) != null) 
                this.getBox().setBorderColor(  style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_padding, null, true) != null) 
                this.getBox().setPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_padding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) != null) 
                this.getBox().setTopBorder(  style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true) != null) 
                this.getBox().setTopBorderColor(  style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) != null) 
                this.getBox().setTopPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) != null) 
                this.getBox().setLeftBorder(  style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true) != null) 
                this.getBox().setLeftBorderColor(  style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) != null) 
                this.getBox().setLeftPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) != null) 
                this.getBox().setRightBorder(  style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true) != null) 
                this.getBox().setRightBorderColor(  style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) != null) 
                this.getBox().setRightPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) ));
            
            if (style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) != null) 
                this.getBox().setBottomBorder(  style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) );
            if (style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true) != null) 
                this.getBox().setBottomBorderColor(  style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true));
            if (style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) != null) 
                this.getBox().setBottomPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) ));
            
        }
     }
    
    public java.util.List getLinkParameters() {
        return getChart().getLinkParameters();
    }

    public void setLinkParameters(java.util.List linkParameters) {
        getChart().setLinkParameters(linkParameters);
    }

    public String getTooltipExpression() {
        return tooltipExpression;
    }

    public void setTooltipExpression(String tooltipExpression) {
        this.tooltipExpression = tooltipExpression;
    }

}
