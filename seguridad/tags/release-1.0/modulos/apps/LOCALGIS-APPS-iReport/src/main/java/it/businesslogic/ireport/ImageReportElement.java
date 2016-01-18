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
 * ImageReportElement.java
 * 
 * Created on 28 febbraio 2003, 19.20
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRImageLoader;

public class ImageReportElement extends GraphicReportElement implements ImageObserver, HyperLinkableReportElement,  BoxElement
{
      
    static public final String SCALE = "SCALE";
    static public final String VERTICAL_ALIGN = "VERTICAL_ALIGN";
    static public final String HORIZONTAL_ALIGN = "HORIZONTAL_ALIGN";
    static public final String USING_CACHE = "USING_CACHE";
    
    static public final String DEFAULT_SCALE = "FillFrame";
    static public final String DEFAULT_VERTICAL_ALIGN = "Top";
    static public final String DEFAULT_HORIZONTAL_ALIGN = "Left";
    static public final boolean DEFAULT_USING_CACHE = false;
    
    private java.util.List linkParameters = new java.util.ArrayList();
    
    private Image img=null;
	static private Image defimg=null;
	private String imageExpression;
	private String imageClass;
	//private String scaleImage;
	private String hyperlinkType="None";
	private String evaluationTime="Now";
	private String evaluationGroup="";
	
	//private String verticalAlignment="Top";
	//private String horizontalAlignment="Left";
	
	private String anchorNameExpression="";
	private String hyperlinkReferenceExpression="";
	private String hyperlinkAnchorExpression="";
	private String hyperlinkPageExpression="";
        private String tooltipExpression = "";
    

        
        private String onErrorType = "Error";
        
        private int bookmarkLevel = 0;
        
    //private boolean isUsingCache;
    private boolean isLazy=false;
    private File reportDirectory = null;
    private boolean triedToLoadImg = false; 
    
    private Box box = null;
    
	public ImageReportElement(int x, int y, int width, int height)
	{
		super(x,y, Math.abs(width), Math.abs(height));
                //setGraphicElementPen("Thin");
		//this.bgcolor = Color.WHITE;
		//this.fgcolor = Color.BLACK;
                setKey("image");
                
                if (defimg == null)
                {
                    // Load default image...
                    defimg = Misc.loadImageFromResources("it/businesslogic/ireport/icons/layout/img.gif");
                }
                
                img = null;
		//isUsingCache=false;
		imageExpression=""; 
		imageClass="java.lang.String";
		//scaleImage="FillFrame";
		//graphicElementPen = "None";
                this.hyperlinkType = "None";
                this.anchorNameExpression = "";
                box = new Box();
	}
        

	
        /*
	public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
                position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
		
		this.zoom_factor = zoom_factor;
                
                g.setColor( bgcolor );
                if (!getTransparent().equalsIgnoreCase("Transparent"))
                    g.fillRect( getZoomedDim(position.x)-x_shift_origin,
				getZoomedDim(position.y)-y_shift_origin,
				getZoomedDim(width),
				getZoomedDim(height));
                g.setColor( this.fgcolor );
                              
		position.x += 10;
		position.y += 10;
		x_shift_origin += 10;
		y_shift_origin += 10;
                
                drawGraphicsElement(g,this.getGraphicElementPen(), zoom_factor,  x_shift_origin,y_shift_origin, 0);
	}*/
        
        public void drawObject(Graphics2D g, double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
                
                position.x += -10 + box.getLeftPadding();
		position.y += -10 + box.getTopPadding();
                width = width - box.getLeftPadding() - box.getRightPadding();
                height = height - box.getTopPadding() - box.getBottomPadding();

		x_shift_origin -= 10;
		y_shift_origin -= 10;
                
		this.zoom_factor = zoom_factor;
		Image imgx = img;
                
		if (imgx==null) 
		{	
                    // Try to load the image from imageExpression...
                    
			String fname = imageExpression;
			
            if(!triedToLoadImg && imageClass.equals("java.lang.String") && fname.length()>3 && fname.charAt(0)=='\"' &&
			fname.charAt(fname.length()-1 ) == '\"'){
            	
				// Could be a file...
				fname = Misc.string_replace("\\","\\\\",fname);
				fname = Misc.string_replace("","\"",fname);
            	
            	//first try read image from file
				File file = new File(fname);
				if(!file.isAbsolute() && reportDirectory != null){
					file = new File(reportDirectory, fname);
				}
				if(file.exists()){             	
					img = Misc.loadImageFromFile(file.getAbsolutePath());
				}else{
					
					try{
                                                
						img = JRImageLoader.loadImage(JRImageLoader.loadImageDataFromURL( MainFrame.getMainInstance().getReportClassLoader().getResource( fname )));
					}catch(Exception jrex){
                                        }
                                }
            
				imgx = img;
				triedToLoadImg = true;//try only once
            }
                        
			if (imgx==null)
				 imgx=defimg;
		}	

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
                    
                    Java2DUtil.setClip(g,getZoomedDim(position.x) - x_shift_origin, 
                               getZoomedDim(position.y) - y_shift_origin, 
                                getZoomedDim(width), getZoomedDim(height));

                    
			if (getScaleImage().equalsIgnoreCase("RetainShape"))
			{
				if (imageWidth <= width && imageHeight<= height)
				{
					Rectangle destination = new Rectangle(-x_shift_origin+ getZoomedDim( position.x) ,-y_shift_origin+getZoomedDim(position.y), getZoomedDim( imageWidth), getZoomedDim( imageHeight));
					Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
					// Calculate y shift based on hAlign...
					int elem_height = getZoomedDim(this.height);
					elem_height -= getZoomedDim( imageHeight);
					if (getVerticalAlignment().equals("Middle")) destination.y += elem_height/2;
					else if (getVerticalAlignment().equals("Bottom")) destination.y += elem_height;
					
					// Calculate x shift based on hAlign...
					int elem_width = getZoomedDim(this.width);
					elem_width -= getZoomedDim( imageWidth);
					if (getHorizontalAlignment().equals("Center")) destination.x += elem_width/2;
					else if (getHorizontalAlignment().equals("Right")) destination.x += elem_width;
					
                                        
					g.drawImage( imgx,destination.x, destination.y, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,this);
                                        
				}
				else if (width>0 && height>0)// Resize based on minor x/WIDTH... e y/HEIGHT
				{
					if ((double)((double)imageWidth/(double)width)> (double)((double)imageHeight/(double)height))
					{
						
						Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
						Rectangle destination = new Rectangle(-x_shift_origin+ getZoomedDim( position.x) ,-y_shift_origin+getZoomedDim(position.y),getZoomedDim(width) ,getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height)) );						
						
						// Calculate y shift based on hAlign...
						int elem_height = getZoomedDim(this.height);
						elem_height -= getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height));
						if (getVerticalAlignment().equals("Middle")) destination.y += elem_height/2;
						else if (getVerticalAlignment().equals("Bottom")) destination.y += elem_height;
						
                                               
						g.drawImage( imgx,destination.x, destination.y, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,this);
                                                
                                        }
					else
					{
						Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
						Rectangle destination = new Rectangle(-x_shift_origin+ getZoomedDim( position.x) ,-y_shift_origin+getZoomedDim(position.y), getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width)) ,getZoomedDim( height) );
						
						// Calculate x shift based on hAlign...
						int elem_width = getZoomedDim(this.width);
						elem_width -= getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width));
						if (getHorizontalAlignment().equals("Center")) destination.x += elem_width/2;
						else if (getHorizontalAlignment().equals("Right")) destination.x += elem_width;
							
						
						g.drawImage( imgx,destination.x, destination.y, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,this);
                                                 
					}
				}
				
			}
			else if (getScaleImage().equalsIgnoreCase("FillFrame"))
			{
                            
				g.drawImage(imgx, getZoomedDim(position.x)-x_shift_origin,
                                            getZoomedDim(position.y)-y_shift_origin,
                                            getZoomedDim(position.x+width)-x_shift_origin,
                                            getZoomedDim(position.y+height)-y_shift_origin,
                                            0,0,imageWidth,imageHeight,null,this);

                                //-x_shift_origin+ getZoomedDim( position.x-10)+10 ,-y_shift_origin+getZoomedDim(position.y)+10, getZoomedDim( this.width), getZoomedDim( this.height),
                                
                                
			}
			else
			{
				Rectangle destination = new Rectangle(-x_shift_origin+ getZoomedDim( position.x) ,-y_shift_origin+getZoomedDim(position.y),getZoomedDim( Math.min(imageWidth, width)),getZoomedDim( Math.min(imageHeight, height)));
				Rectangle source = new Rectangle(0,0,Math.min( imageWidth, width) ,Math.min(imageHeight,  height));

				// Calculate y shift based on hAlign...
				int elem_height = getZoomedDim( height);
				elem_height -= getZoomedDim( imageHeight );
				if (getVerticalAlignment().equals("Middle")) { 
					destination.y += elem_height/2;  
					if (destination.y<--y_shift_origin+getZoomedDim(position.y)+10 )
					{
						source.y += -y_shift_origin+getZoomedDim(position.y)+10-destination.y;
						destination.y = -y_shift_origin+getZoomedDim(position.y)+10;
					}
				}
				else if (getVerticalAlignment().equals("Bottom"))  { 
					destination.y += elem_height;  
					if (destination.y<-y_shift_origin+getZoomedDim(position.y)+10 )
					{
						source.y += -y_shift_origin+getZoomedDim(position.y)+10-destination.y;
						destination.y = -y_shift_origin+getZoomedDim(position.y)+10;
					}
				}
				
				// Calculate x shift based on hAlign...
				int elem_width = getZoomedDim( width);
				elem_width -= getZoomedDim( imageWidth );
				if (getHorizontalAlignment().equals("Center")) { 
					destination.x += elem_width/2;  
					if (destination.x<-x_shift_origin+ getZoomedDim( position.x-10)+10 )
					{
						source.x += -x_shift_origin+ getZoomedDim( position.x-10)+10-destination.x;
						destination.x = -x_shift_origin+ getZoomedDim( position.x-10)+10;
					}
				}
				else if (getHorizontalAlignment().equals("Right"))  { 
					destination.x += elem_width;  
					if (destination.x<-x_shift_origin+ getZoomedDim( position.x-10)+10 )
					{
						source.x += -x_shift_origin+ getZoomedDim( position.x-10)+10-destination.x;
						destination.x = -x_shift_origin+ getZoomedDim( position.x-10)+10;
					}
				}
					
				g.drawImage( imgx,destination.x, destination.y, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,this);
			}
                    
                        Java2DUtil.resetClip(g);
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
		drawGraphicsElement((Graphics2D)g,this.getGraphicElementPen(), zoom_factor,  x_shift_origin,y_shift_origin);
                drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin,getBox());
	}
	
        
    public ReportElement cloneMe()
    {
	ImageReportElement newReportElement = new ImageReportElement(position.x, position.y, width, height);
	copyBaseReportElement(newReportElement, this);
        newReportElement.setIsLazy( this.isIsLazy());
	return newReportElement;
    }
        
    public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof ImageReportElement &&
                    source instanceof ImageReportElement )
                {
                    destination.setPropertyValue(SCALE, source.getPropertyValue(SCALE));
                    destination.setPropertyValue(VERTICAL_ALIGN, source.getPropertyValue(VERTICAL_ALIGN));
                    destination.setPropertyValue(HORIZONTAL_ALIGN, source.getPropertyValue(HORIZONTAL_ALIGN));
                    destination.setPropertyValue(USING_CACHE, source.getPropertyValue(USING_CACHE));

                    ((ImageReportElement)destination).setEvaluationGroup(  new String( ((ImageReportElement)source).getEvaluationGroup() ));
                    ((ImageReportElement)destination).setEvaluationTime( new String(  ((ImageReportElement)source).getEvaluationTime() ));
                    //((ImageReportElement)destination).setHorizontalAlignment( new String(  ((ImageReportElement)source).getHorizontalAlignment() ));
                    ((ImageReportElement)destination).setImageClass( new String(  ((ImageReportElement)source).getImageClass() ));
                    ((ImageReportElement)destination).setImageExpression( new String(  ((ImageReportElement)source).getImageExpression() ));
                    //((ImageReportElement)destination).setIsUsingCache( ((ImageReportElement)source).isIsUsingCache());
                    //((ImageReportElement)destination).setScaleImage( new String(  ((ImageReportElement)source).getScaleImage() ));
                    //((ImageReportElement)destination).setVerticalAlignment(  new String(  ((ImageReportElement)source).getVerticalAlignment() ));
		    ((ImageReportElement)destination).setReportDirectory(   ((ImageReportElement)source).getReportDirectory() );

                    ((HyperLinkableReportElement)destination).setAnchorNameExpression(new String(  ((HyperLinkableReportElement)source).getAnchorNameExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkAnchorExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkAnchorExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkPageExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkPageExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkReferenceExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkReferenceExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkType(new String(  ((HyperLinkableReportElement)source).getHyperlinkType() ));
                }
        }
	
	public void  drawGraphicsElement(Graphics2D g, String pen, double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
		
		Stroke stroke = getPenStroke( pen,zoom_factor );
		g.setColor( getFgcolor());
                
		this.zoom_factor = zoom_factor;
		if (stroke==null || pen.equalsIgnoreCase("None")) return;
		
		position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
                
                Stroke oldStroke = g.getStroke();
                g.setStroke(stroke);
		g.drawRect(
                    getZoomedDim(position.x)-x_shift_origin,
		    getZoomedDim(position.y)-y_shift_origin,
                    getZoomedDim(width),getZoomedDim(height));

		position.x += 10;
		position.y += 10;               
                
                g.setStroke(oldStroke);
        }
        
        /** Getter for property horizontalAlignment.
         * @return Value of property horizontalAlignment.
         *
         */
        public java.lang.String getHorizontalAlignment() {
            if (getPropertyValue( HORIZONTAL_ALIGN) == null)
           {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeString( getStyle().ATTRIBUTE_hAlign, DEFAULT_HORIZONTAL_ALIGN, true);
                }
            }
            return getStringValue( HORIZONTAL_ALIGN, DEFAULT_HORIZONTAL_ALIGN );
        }
        
        /** Setter for property horizontalAlignment.
         * @param horizontalAlignment New value of property horizontalAlignment.
         *
         */
        public void setHorizontalAlignment(java.lang.String horizontalAlignment) {
             setPropertyValue( HORIZONTAL_ALIGN, horizontalAlignment );
        }
        
        /** Getter for property imageClass.
         * @return Value of property imageClass.
         *
         */
        public java.lang.String getImageClass() {
            return imageClass;
        }
        
        /** Setter for property imageClass.
         * @param imageClass New value of property imageClass.
         *
         */
        public void setImageClass(java.lang.String imageClass) {
            this.imageClass = imageClass;
        }
        
        /** Getter for property verticalAlignment.
         * @return Value of property verticalAlignment.
         *
         */
        public java.lang.String getVerticalAlignment() {
            if (getPropertyValue( VERTICAL_ALIGN) == null)
           {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeString( getStyle().ATTRIBUTE_vAlign, DEFAULT_VERTICAL_ALIGN, true);
                }
            }
            return getStringValue( VERTICAL_ALIGN, DEFAULT_VERTICAL_ALIGN );
        }
        
        /** Setter for property verticalAlignment.
         * @param verticalAlignment New value of property verticalAlignment.
         *
         */
        public void setVerticalAlignment(java.lang.String verticalAlignment) {
            setPropertyValue( VERTICAL_ALIGN, verticalAlignment );
        }
        
        /** Getter for property evaluationGroup.
         * @return Value of property evaluationGroup.
         *
         */
        public java.lang.String getEvaluationGroup() {
            return evaluationGroup;
        }
        
        /** Setter for property evaluationGroup.
         * @param evaluationGroup New value of property evaluationGroup.
         *
         */
        public void setEvaluationGroup(java.lang.String evaluationGroup) {
            this.evaluationGroup = evaluationGroup;
        }
        
        /** Getter for property imageExpression.
         * @return Value of property imageExpression.
         *
         */
        public java.lang.String getImageExpression() {
            return imageExpression;
        }
        
        /** Setter for property imageExpression.
         * @param imageExpression New value of property imageExpression.
         *
         */
        public void setImageExpression(java.lang.String imageExpression) {
            this.imageExpression = imageExpression;
            this.triedToLoadImg = false;
        }
        
        /** Getter for property evaluationTime.
         * @return Value of property evaluationTime.
         *
         */
        public java.lang.String getEvaluationTime() {
            return evaluationTime;
        }
        
        /** Setter for property evaluationTime.
         * @param evaluationTime New value of property evaluationTime.
         *
         */
        public void setEvaluationTime(java.lang.String evaluationTime) {
            this.evaluationTime = evaluationTime;
        }
        
        /** Getter for property img.
         * @return Value of property img.
         *
         */
        public java.awt.Image getImg() {
            return img;
        }
        
        public java.awt.Image getImgDef() {
            return defimg;
        }
        
        /** Setter for property img.
         * @param img New value of property img.
         *
         */
        public void setImg(java.awt.Image img) {
            this.img = img;
        }
        
        /** Getter for property hyperlinkType.
         * @return Value of property hyperlinkType.
         *
         */
        public java.lang.String getHyperlinkType() {
            return hyperlinkType;
        }
        
        /** Setter for property hyperlinkType.
         * @param hyperlinkType New value of property hyperlinkType.
         *
         */
        public void setHyperlinkType(java.lang.String hyperlinkType) {
            this.hyperlinkType = hyperlinkType;
        }
        
        /** Getter for property anchorNameExpression.
         * @return Value of property anchorNameExpression.
         *
         */
        public java.lang.String getAnchorNameExpression() {
            return anchorNameExpression;
        }
        
        /** Setter for property anchorNameExpression.
         * @param anchorNameExpression New value of property anchorNameExpression.
         *
         */
        public void setAnchorNameExpression(java.lang.String anchorNameExpression) {
            this.anchorNameExpression = anchorNameExpression;
        }
        
        /** Getter for property scaleImage.
         * @return Value of property scaleImage.
         *
         */
        public java.lang.String getScaleImage() {
            if (getPropertyValue( SCALE) == null)
           {
                // Look for a fgcolor in the stylesheet...
                if (getStyle() != null)
                {
                   return getStyle().getAttributeString( getStyle().ATTRIBUTE_scaleImage, DEFAULT_SCALE, true);
                }
            }
            return getStringValue( SCALE, DEFAULT_SCALE );
        }
        
        /** Setter for property scaleImage.
         * @param scaleImage New value of property scaleImage.
         *
         */
        public void setScaleImage(java.lang.String scaleImage) {
            setPropertyValue( SCALE, scaleImage );
        }
        
        /** Getter for property hyperlinkReferenceExpression.
         * @return Value of property hyperlinkReferenceExpression.
         *
         */
        public java.lang.String getHyperlinkReferenceExpression() {
            return hyperlinkReferenceExpression;
        }
        
        /** Setter for property hyperlinkReferenceExpression.
         * @param hyperlinkReferenceExpression New value of property hyperlinkReferenceExpression.
         *
         */
        public void setHyperlinkReferenceExpression(java.lang.String hyperlinkReferenceExpression) {
            this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
        }
        
        /** Getter for property hyperlinkAnchorExpression.
         * @return Value of property hyperlinkAnchorExpression.
         *
         */
        public java.lang.String getHyperlinkAnchorExpression() {
            return hyperlinkAnchorExpression;
        }
        
        /** Setter for property hyperlinkAnchorExpression.
         * @param hyperlinkAnchorExpression New value of property hyperlinkAnchorExpression.
         *
         */
        public void setHyperlinkAnchorExpression(java.lang.String hyperlinkAnchorExpression) {
            this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
        }
        
        /** Getter for property hyperlinkPageExpression.
         * @return Value of property hyperlinkPageExpression.
         *
         */
        public java.lang.String getHyperlinkPageExpression() {
            return hyperlinkPageExpression;
        }
        
        /** Setter for property hyperlinkPageExpression.
         * @param hyperlinkPageExpression New value of property hyperlinkPageExpression.
         *
         */
        public void setHyperlinkPageExpression(java.lang.String hyperlinkPageExpression) {
            this.hyperlinkPageExpression = hyperlinkPageExpression;
        }
        
        /** This method is called when information about an image which was
         * previously requested using an asynchronous interface becomes
         * available.  Asynchronous interfaces are method calls such as
         * getWidth(ImageObserver) and drawImage(img, x, y, ImageObserver)
         * which take an ImageObserver object as an argument.  Those methods
         * register the caller as interested either in information about
         * the overall image itself (in the case of getWidth(ImageObserver))
         * or about an output version of an image (in the case of the
         * drawImage(img, x, y, [w, h,] ImageObserver) call).
         *
         * <p>This method
         * should return true if further updates are needed or false if the
         * required information has been acquired.  The image which was being
         * tracked is passed in using the img argument.  Various constants
         * are combined to form the infoflags argument which indicates what
         * information about the image is now available.  The interpretation
         * of the x, y, width, and height arguments depends on the contents
         * of the infoflags argument.
         * <p>
         * The <code>infoflags</code> argument should be the bitwise inclusive
         * <b>OR</b> of the following flags: <code>WIDTH</code>,
         * <code>HEIGHT</code>, <code>PROPERTIES</code>, <code>SOMEBITS</code>,
         * <code>FRAMEBITS</code>, <code>ALLBITS</code>, <code>ERROR</code>,
         * <code>ABORT</code>.
         *
         * @param     img   the image being observed.
         * @param     infoflags   the bitwise inclusive OR of the following
         *               flags:  <code>WIDTH</code>, <code>HEIGHT</code>,
         *               <code>PROPERTIES</code>, <code>SOMEBITS</code>,
         *               <code>FRAMEBITS</code>, <code>ALLBITS</code>,
         *               <code>ERROR</code>, <code>ABORT</code>.
         * @param     x   the <i>x</i> coordinate.
         * @param     y   the <i>y</i> coordinate.
         * @param     width    the width.
         * @param     height   the height.
         * @return    <code>false</code> if the infoflags indicate that the
         *            image is completely loaded; <code>true</code> otherwise.
         *
         * @see #WIDTH
         * @see #HEIGHT
         * @see #PROPERTIES
         * @see #SOMEBITS
         * @see #FRAMEBITS
         * @see #ALLBITS
         * @see #ERROR
         * @see #ABORT
         * @see Image#getWidth
         * @see Image#getHeight
         * @see java.awt.Graphics#drawImage
         *
         */
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true;
        }
        
        public static void setImgDef(Image defImg)
        {
            defimg = defImg;
        }
        
        /** Getter for property isUsingCache.
         * @return Value of property isUsingCache.
         *
         */
        public boolean isIsUsingCache() {

            return getBooleanValue( USING_CACHE, DEFAULT_USING_CACHE );
        }
        
        /** Setter for property isUsingCache.
         * @param isUsingCache New value of property isUsingCache.
         *
         */
        public void setIsUsingCache(boolean isUsingCache) {
            setPropertyValue( USING_CACHE, new Boolean( isUsingCache ) );
        }
        
	/**
	 * @return
	 */
	public File getReportDirectory() {
		return reportDirectory;
	}

	/**
	 * @param directory
	 */
	public void setReportDirectory(File directory) {
		reportDirectory = directory;
	}
        
        protected String hyperlinkTarget = "Self";
    /** Getter for property hyperlinkTarget.
         * @return Value of property hyperlinkTarget.
         *
         */
        public java.lang.String getHyperlinkTarget(){ return hyperlinkTarget; }
        
        /** Setter for property hyperlinkTarget.
         * @param hyperlinkTarget New value of property hyperlinkTarget.
         *
         */
        public void setHyperlinkTarget(java.lang.String hyperlinkTarget) { this.hyperlinkTarget = hyperlinkTarget; }
        
            public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public boolean isIsLazy() {
        return isLazy;
    }

    public void setIsLazy(boolean isLazy) {
        this.isLazy = isLazy;
    }

    public int getBookmarkLevel() {
        return bookmarkLevel;
    }

    public void setBookmarkLevel(int bookmarkLevel) {
        this.bookmarkLevel = bookmarkLevel;
    }

    public String getOnErrorType() {
        return onErrorType;
    }

    public void setOnErrorType(String onErrorType) {
        this.onErrorType = onErrorType;
    }

    
    public void setStyle(Style style) {
     
        super.setStyle(style);
        
        if (style != null)
        {            
            //this.setHorizontalAlignment(style.getAttributeString( style.ATTRIBUTE_hAlign, getHorizontalAlignment(), true));
            //this.setVerticalAlignment( style.getAttributeString( style.ATTRIBUTE_vAlign, getVerticalAlignment(), true));
            
            //this.setScaleImage( style.getAttributeString( style.ATTRIBUTE_scaleImage, getScaleImage(), true));
            
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
        return linkParameters;
    }

    public void setLinkParameters(java.util.List linkParameters) {
        this.linkParameters = linkParameters;
    }

    public String getTooltipExpression() {
        return tooltipExpression;
    }

    public void setTooltipExpression(String tooltipExpression) {
        this.tooltipExpression = tooltipExpression;
    }
    
}
