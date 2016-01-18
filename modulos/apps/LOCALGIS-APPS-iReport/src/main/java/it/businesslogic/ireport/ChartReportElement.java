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
 * ChartReportElement.java
 * 
 */

package it.businesslogic.ireport;
/*
 * ChartReportElement.java
 *
 *  iReport  --  Visual designer for generating JasperReports Documents
 *  Copyright (C) 2002-2003  Giulio Toffoli gt@businesslogic.it
 *
 *  This program is free software; you can redistribute  and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  Giulio Toffoli
 *  Via T.Aspetti, 233
 *  35100 Padova ITALY
 *  gt@businesslogic.it
 *
 *
 * Created on 3 giugno 2003, 23.28
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author  Administrator
 */
public class ChartReportElement extends it.businesslogic.ireport.ImageReportElement {
    
    private java.util.Properties props;

    /** Creates a new instance of ChartReportElement */
   // private JFreeChart chart = null;
    
    public ChartReportElement(int x, int y, int width, int height)
    {
		super(x,y,width,height);
                //setGraphicElementPen("Thin");
		this.setBgcolor(Color.WHITE);
		this.setFgcolor(Color.BLACK);
                setKey("chart");
                
             
        setImg( null );
	setIsUsingCache(false);
	setImageExpression("");
	setImageClass("java.awt.Image");
	setScaleImage("RetainShape");
	setGraphicElementPen("None");
        setHyperlinkType("None");
        setAnchorNameExpression("");
        
        setImg(it.businesslogic.ireport.chart.AvailableCharts.getChartIcon( ""  ).getImage());
       
        props = new java.util.Properties();
        props.setProperty("width", ""+width );
        props.setProperty("height", ""+height );
    }
    
    
    public void drawObject(Graphics2D g, double zoom_factor, int x_shift_origin, int y_shift_origin)
    {
          
	if (this.getImg() == null) 
	{	
                      updateChartImage();                    
	}
                
        super.drawObject(g,  zoom_factor, x_shift_origin, y_shift_origin);
    }

    public java.util.Properties getProps() {
        return props;
    }

    public void setProps(java.util.Properties props) {
        this.props=props;
    }
         
    public void parseProperties( Vector reportProperties )
    {
        for (int i=0; i< reportProperties.size(); ++i  )
        {
            JRProperty property = (JRProperty)reportProperties.elementAt(i);
            if (property.getName().startsWith("chart." + getName() ))
            {
                props.setProperty( property.getName().substring(("chart."+getName()+".").length()) , property.getValue());   
            }
        }
        
        // Try to get Icon...
        /*
        if (props.getProperty("chartName") != null)
        {
            setImg( it.businesslogic.ireport.chart.AvailableCharts.getChartIcon( props.getProperty("chartName")  ).getImage() );
        }
        */
        
    }
   /*     
     public void parseExpression(String expression)
     {
         expression = expression.trim();
         expression = Misc.string_replace("","(java.awt.Image)it.businesslogic.ireport.chart.IReportChartFactory.iReportChart(", expression);
         // REmove last )
         if (expression.endsWith(")"))
             expression = expression.substring(0, expression.length() -1);
             
         StringTokenizer st = new StringTokenizer(expression, "\n");    
     }
    */
    
    public void updateChartImage()
    {
        try {
            String[] params_strings = null;
            params_strings = new String[this.getProps().size()];
            
            Enumeration enum_keys = getProps().keys();
            int i=0;
            while (enum_keys.hasMoreElements())
            {
                String key = (String)enum_keys.nextElement();
                String val = ""+getProps().get(key);
                
                if (key.startsWith("serie"))
                {
                    val = "";
                }
                
                params_strings[i] = key + "=" + val;
                
                //
                i++;
            }
            java.awt.Image imgx =  it.businesslogic.ireport.chart.DefaultChartFactory.drawChart(params_strings, null);
            this.setImg( imgx );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }	
    
    public void updateBounds() {
        bounds = new Rectangle(position.x,position.y,width,height);
    } 
    
    public Point trasform(Point delta, int type) {
       Point p = super.trasform(delta,type);
       getProps().setProperty("width",""+width);
        getProps().setProperty("height",""+height);
        updateChartImage();
        return p;
    }
    
}
