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
 * SubReportElement.java
 * 
 * Created on 28 febbraio 2003, 22.53
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;


public class SubReportElement extends ReportElement {
    
        private boolean isUsingCache = true;
	private Vector subreportParameters=null;
        private Vector returnValues=null;
	private String subreportExpression="";
	private String subreportExpressionClass="";
	private String connectionExpression="";
	private String dataSourceExpression="";
	private String parametersMapExpression="";
	private boolean useConnection = false;
        
        public static Image img=null;
	
	public SubReportElement(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	        setKey("subreport");
                
                subreportParameters = new Vector();
                returnValues = new Vector();
		subreportExpressionClass = "java.lang.String";
		useConnection = false;
                this.transparentDefault = "Transparent";
                if (img == null)
                      img = Misc.loadBufferedImageFromResources(new java.awt.Panel() ,"it/businesslogic/ireport/icons/subreportTool1.jpg");
	}
        
        public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
           
                position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
		
		this.zoom_factor = zoom_factor;
                
                Color ccc = new Color(204,204,204,150);
                
                g.setColor( ccc );
                Paint paint=g.getPaint();
                g.setPaint( new GradientPaint( getZoomedDim(position.x)-x_shift_origin, 
                            getZoomedDim(position.y)-y_shift_origin,
                            ccc, getZoomedDim(position.x)-x_shift_origin+getZoomedDim(width),
                            getZoomedDim(position.x)-x_shift_origin + getZoomedDim(height), ccc.brighter() ) );

                g.fillRect( getZoomedDim(position.x)-x_shift_origin,
				getZoomedDim(position.y)-y_shift_origin,
				getZoomedDim(width),
				getZoomedDim(height));
                g.setPaint(paint);
                
		position.x += 10;
		position.y += 10;
		x_shift_origin += 10;
		y_shift_origin += 10;
                
                drawGraphicsElement(g,zoom_factor,  x_shift_origin,y_shift_origin);
	}
		
	public void  drawGraphicsElement(Graphics2D g, double zoom_factor, int x_shift_origin, int y_shift_origin)
	{
                drawGraphicsElement(g,"Thin", zoom_factor,  x_shift_origin,y_shift_origin);
                int correction = 0; //(zoom_factor <= 1) ? -1 : 0;
                
		position.x -= 10;
		position.y -= 10;
		x_shift_origin -= 10;
		y_shift_origin -= 10;
                
                
                if (img != null)
                {/*
                    g.drawImage(img, 
                        getZoomedDim(position.x)-x_shift_origin,
                        getZoomedDim(position.y)-y_shift_origin,
                        getZoomedDim(width)+correction,getZoomedDim(height)+correction, null);
                */
                    int imageWidth = img.getWidth(null);
                    int imageHeight = img.getHeight(null);
                    /*
			if (imgx instanceof Image)
			{
				((Image)imgx).setTransparent(true);
			}
                     */
			
			// 
			if (imageWidth < width && imageHeight< height)
			{
				Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin,getZoomedDim( imageWidth), getZoomedDim( imageHeight-1));
				Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
				// Calculate y shift based on hAlign...
				int elem_height = getZoomedDim(this.height);
				elem_height -= getZoomedDim( imageHeight);
				// Calculate x shift based on hAlign...
				int elem_width = getZoomedDim(this.width);
				elem_width -= getZoomedDim( imageWidth);
				g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width+1, destination.y+destination.height+1, 
                                                       source.x,source.y,source.width,source.height,
                                                       null,null);
			}
			else if (width>0 && height>0)// Resize based on minor x/WIDTH... e y/HEIGHT
			{
				if ((double)((double)imageWidth/(double)width)> (double)((double)imageHeight/(double)height))
				{
					
					Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
					Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin,getZoomedDim(width) ,getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height-1)) );						
					
					// Calculate y shift based on hAlign...
					int elem_height = getZoomedDim(this.height);
					elem_height -= getZoomedDim(Math.min( (imageHeight*width)/imageWidth, height));
						
					g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,null);
					}
					else
					{
						Rectangle source = new Rectangle(0,0,imageWidth,imageHeight);
						Rectangle destination = new Rectangle(getZoomedDim(position.x)-x_shift_origin,
                                                                      getZoomedDim(position.y)-y_shift_origin, getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width)) ,getZoomedDim( height-1) );
						
						// Calculate x shift based on hAlign...
						int elem_width = getZoomedDim(this.width);
						elem_width -= getZoomedDim( Math.min( (imageWidth*height)/imageHeight, width));
					
						g.drawImage( img,destination.x+1, destination.y+1, destination.x+destination.width, destination.y+destination.height, 
                                                        source.x,source.y,source.width,source.height,
                                                        null,null);
					}
                                }
                        }                    							
                position.x += 10;
		position.y += 10;
		               
        }        
        
        /** Getter for property connectionExpression.
         * @return Value of property connectionExpression.
         *
         */
        public java.lang.String getConnectionExpression() {
            return connectionExpression;
        }
        
        /** Setter for property connectionExpression.
         * @param connectionExpression New value of property connectionExpression.
         *
         */
        public void setConnectionExpression(java.lang.String connectionExpression) {
            this.connectionExpression = connectionExpression;
        }
        
        /** Getter for property dataSourceExpression.
         * @return Value of property dataSourceExpression.
         *
         */
        public java.lang.String getDataSourceExpression() {
            return dataSourceExpression;
        }
        
        /** Setter for property dataSourceExpression.
         * @param dataSourceExpression New value of property dataSourceExpression.
         *
         */
        public void setDataSourceExpression(java.lang.String dataSourceExpression) {
            this.dataSourceExpression = dataSourceExpression;
        }
        
        /** Getter for property isUsingCache.
         * @return Value of property isUsingCache.
         *
         */
        public boolean isIsUsingCache() {
            return isUsingCache;
        }
        
        /** Setter for property isUsingCache.
         * @param isUsingCache New value of property isUsingCache.
         *
         */
        public void setIsUsingCache(boolean isUsingCache) {
            this.isUsingCache = isUsingCache;
        }
        
        /** Getter for property parametersMapExpression.
         * @return Value of property parametersMapExpression.
         *
         */
        public java.lang.String getParametersMapExpression() {
            return parametersMapExpression;
        }
        
        /** Setter for property parametersMapExpression.
         * @param parametersMapExpression New value of property parametersMapExpression.
         *
         */
        public void setParametersMapExpression(java.lang.String parametersMapExpression) {
            this.parametersMapExpression = parametersMapExpression;
        }
        
        /** Getter for property subreportExpression.
         * @return Value of property subreportExpression.
         *
         */
        public java.lang.String getSubreportExpression() {
            return subreportExpression;
        }
        
        /** Setter for property subreportExpression.
         * @param subreportExpression New value of property subreportExpression.
         *
         */
        public void setSubreportExpression(java.lang.String subreportExpression) {
            this.subreportExpression = subreportExpression;
        }
        
        /** Getter for property subreportExpressionClass.
         * @return Value of property subreportExpressionClass.
         *
         */
        public java.lang.String getSubreportExpressionClass() {
            return subreportExpressionClass;
        }
        
        /** Setter for property subreportExpressionClass.
         * @param subreportExpressionClass New value of property subreportExpressionClass.
         *
         */
        public void setSubreportExpressionClass(java.lang.String subreportExpressionClass) {
            this.subreportExpressionClass = subreportExpressionClass;
        }
        
        /** Getter for property subreportParameters.
         * @return Value of property subreportParameters.
         *
         */
        public java.util.Vector getSubreportParameters() {
            return subreportParameters;
        }
        
        /** Setter for property subreportParameters.
         * @param subreportParameters New value of property subreportParameters.
         *
         */
        public void setSubreportParameters(java.util.Vector subreportParameters) {
            this.subreportParameters = subreportParameters;
        }
        
        /** Getter for property useConnection.
         * @return Value of property useConnection.
         *
         */
        public boolean isUseConnection() {
            return useConnection;
        }
        
        /** Setter for property useConnection.
         * @param useConnection New value of property useConnection.
         *
         */
        public void setUseConnection(boolean useConnection) {
            this.useConnection = useConnection;
        }
        
        
        public ReportElement cloneMe()
    {
	SubReportElement newReportElement = new SubReportElement(position.x, position.y, width, height);
	copyBaseReportElement(newReportElement, this);
	return newReportElement;
    }
        
    public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof SubReportElement &&
                    source instanceof SubReportElement )
                {
                    
                    
                    ((SubReportElement)destination).setIsUsingCache( ((SubReportElement)source).isIsUsingCache());
                    ((SubReportElement)destination).setParametersMapExpression( new String(  ((SubReportElement)source).getParametersMapExpression() ));
                    ((SubReportElement)destination).setSubreportExpression ( new String(  ((SubReportElement)source).getSubreportExpression() ));
                    ((SubReportElement)destination).setSubreportExpressionClass( new String(  ((SubReportElement)source).getSubreportExpressionClass() ));
                    ((SubReportElement)destination).setUseConnection(  ((SubReportElement)source).isUseConnection() );
                    if ( ((SubReportElement)destination).isUseConnection())
                        ((SubReportElement)destination).setConnectionExpression(  new String( ((SubReportElement)source).getConnectionExpression() ));
                    else
                        ((SubReportElement)destination).setDataSourceExpression( new String(  ((SubReportElement)source).getDataSourceExpression() ));
                    
                    Enumeration e = ((SubReportElement)source).getSubreportParameters().elements();
                    while (e.hasMoreElements())
                    {
			JRSubreportParameter jp = (JRSubreportParameter)e.nextElement();
			((SubReportElement)destination).getSubreportParameters().addElement(jp.cloneMe());
                    }	
                }
        }

    public Vector getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(Vector returnValues) {
        this.returnValues = returnValues;
    }
}
