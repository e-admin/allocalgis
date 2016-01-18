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
 * DefaultChartFactory.java
 * 
 * Created on 26 settembre 2004, 18.58
 *
 */

package it.businesslogic.ireport.chart;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.util.*;
import java.awt.Image;
import org.jfree.data.*;
import org.jfree.chart.*;
import org.jfree.ui.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class DefaultChartFactory extends ChartFactory {
    
    static final String PIE3D = "Pie3D";
    static final String PIE = "Pie";
    static final String BAR = "Bar";
    static final String BAR3D = "Bar3D";
    static final String LINE = "Line";
    static final String AREA = "Area";
    static final String CANDLESTICK = "Candlestick";
    
    public static Image drawChart(String[] parameters, IReportScriptlet scriptlet)
    {
        java.util.Properties props = parseProperties(parameters);
        int width =   getParameterAsInteger("width",props,250);
        int height =  getParameterAsInteger("height",props,100);
        int quality = getParameterAsInteger("quality",props,1);
        String chartName = props.getProperty("chartName");
        
        boolean showLegend =  getParameterAsBoolean("legend",props,false);
        boolean showTooltips =  getParameterAsBoolean("tooltips",props,false);
        
        
         if ( chartName.equals(PIE3D))
        {
            Vector labels = getSeries("serie0", props,scriptlet);
            Vector values = getSeries("serie1", props,scriptlet);
            
            DefaultKeyedValues dkv = new  DefaultKeyedValues();

            for (int i=0; i<values.size(); ++i)
            {

                String key = (i+1)+"";
                if (labels != null)
                {
                    key = ""+labels.get(i);
                }
                dkv.addValue( key, new Double(""+ ((values.get( i ) != null) ? values.get( i ) : "0"))  );
            }

            double depthFactor = getParameterAsDouble("depthFactor",props, 0.2);
            JFreeChart chart = org.jfree.chart.ChartFactory.createPieChart3D("", new DefaultPieDataset(dkv), showLegend, showTooltips, false);  
            ((PiePlot3D)(chart.getPlot())).setDepthFactor( depthFactor );
            
            ((PiePlot3D)(chart.getPlot())).setForegroundAlpha( (float)getParameterAsDouble("foregroundAlpha",props, 0.0));
            setChartProperties(props, chart);
            
            
            return chart.createBufferedImage(width*quality,height*quality); 
        }
        else if ( chartName.equals(PIE))
        {
            Vector labels = getSeries("serie0", props,scriptlet);
            Vector values = getSeries("serie1", props,scriptlet);
            
            DefaultKeyedValues dkv = new  DefaultKeyedValues();

            for (int i=0; i<values.size(); ++i)
            {

                String key = (i+1)+"";
                if (labels != null)
                {
                    key = ""+labels.get(i);
                }
                dkv.addValue( key, new Double(""+ ((values.get( i ) != null) ? values.get( i ) : "0") ));
            }

            JFreeChart chart = org.jfree.chart.ChartFactory.createPieChart("", new DefaultPieDataset(dkv), showLegend, showTooltips, false); 
            setChartProperties(props, chart);
            return chart.createBufferedImage(width*quality,height*quality); 
        }
        else if ( chartName.equals(BAR) || chartName.equals(BAR3D))
        {
            Vector values = getSeries("serie0", props,scriptlet);
            Vector theCategories = getSeries("serie1", props,scriptlet);
            Vector theSeries = getSeries("serie2", props,scriptlet);
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            if (scriptlet == null)
            {
                dataset = getSampleCategoryDataset();
            }
            else
            {
                    for (int i=0; i<values.size(); ++i)
                    {

                        String category = (i+1)+"";
                        if (theCategories != null && theCategories.size() > i)
                        {
                            category = ""+theCategories.get(i);
                        }

                        String theSerie = "";
                        if (theSeries != null && theSeries.size() > i)
                        {
                            theSerie = ""+theSeries.get(i);
                        }


                        dataset.addValue(new Double(""+ ((values.get( i ) != null) ? values.get( i ) : "0")) ,theSerie,  category);
                    }
            }

            int plotOrientation = getParameterAsInteger("plotOrientation", props, 1);
            
            JFreeChart chart = null;
            
            if ( chartName.equals(BAR)  )
            {
                chart = org.jfree.chart.ChartFactory.createBarChart("",
                Misc.nvl(props.getProperty("categoryLabel"),""),
                Misc.nvl(props.getProperty("valueLabel"),""),
                dataset,
                (plotOrientation == 1) ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL, // orientation
                showLegend,                    // include legend
                showTooltips,                     // tooltips?
                false                     // URLs?
                );
            }
            else
            {
                chart = org.jfree.chart.ChartFactory.createBarChart3D("",
                Misc.nvl(props.getProperty("categoryLabel"),""),
                Misc.nvl(props.getProperty("valueLabel"),""),
                dataset,
                (plotOrientation == 1) ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL, // orientation
                showLegend,                    // include legend
                showTooltips,                     // tooltips?
                false                     // URLs?
                );
            }
            setChartProperties(props, chart);
            return chart.createBufferedImage(width*quality,height*quality); 
        }
        else if ( chartName.equals(LINE) )
        {
            Vector valuesX = getSeries("serie0", props,scriptlet);
            Vector valuesY = getSeries("serie1", props,scriptlet);
            Vector theSeries = getSeries("serie2", props,scriptlet);
            
            //XYSeries dataset = new XYSeries("");
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            int plotOrientation = getParameterAsInteger("plotOrientation", props, 1);
            if (scriptlet == null)
            {
                dataset = getSampleCategoryDataset();
            }
            else
            {
                    for (int i=0; i<valuesX.size(); ++i)
                    {
                        String theSerie = "";
                        if (theSeries != null && theSeries.size() > i)
                        {
                            theSerie = ""+theSeries.get(i);
                        }

                        dataset.addValue( (Number)new Double(""+ ((valuesX.get( i ) != null) ? valuesX.get( i ) : "0")),
                                     (Comparable)theSerie, 
                                     (Comparable)new Double(""+ ((valuesY.get( i ) != null) ? valuesY.get( i ) : "0")));
                    }
            }
            
            JFreeChart chart = null;

            chart = org.jfree.chart.ChartFactory.createLineChart("", 
            Misc.nvl(props.getProperty("categoryLabel"),""),
            Misc.nvl(props.getProperty("valueLabel"),""),
            dataset,
            (plotOrientation == 1) ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL, // orientation
            showLegend,                    // include legend
            showTooltips,                     // tooltips?
            false                     // URLs?
            );

            setChartProperties(props, chart);
            return chart.createBufferedImage(width*quality,height*quality); 
        }
        else if ( chartName.equals(AREA) )
        {
            Vector valuesX = getSeries("serie0", props,scriptlet);
            Vector valuesY = getSeries("serie1", props,scriptlet);
            Vector theSeries = getSeries("serie2", props,scriptlet);
            
            //XYSeries dataset = new XYSeries("");
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            int plotOrientation = getParameterAsInteger("plotOrientation", props, 1);
            
            if (scriptlet == null)
            {
                dataset = getSampleCategoryDataset();
            }
            else
            {
                for (int i=0; i<valuesX.size(); ++i)
                {
                    String theSerie = "";
                    if (theSeries != null && theSeries.size() > i)
                    {
                        theSerie = ""+theSeries.get(i);
                    }

                    dataset.addValue( (Number)new Double(""+ ((valuesX.get( i ) != null) ? valuesX.get( i ) : "0")),
                                 (Comparable)theSerie, 
                                 (Comparable)new Double(""+ ((valuesY.get( i ) != null) ? valuesY.get( i ) : "0")));
                }

            }
            JFreeChart chart = null;

            chart = org.jfree.chart.ChartFactory.createAreaChart("", 
            Misc.nvl(props.getProperty("categoryLabel"),""),
            Misc.nvl(props.getProperty("valueLabel"),""),
            dataset,
            (plotOrientation == 1) ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL, // orientation
            showLegend,                    // include legend
            showTooltips,                     // tooltips?
            false                     // URLs?
            );

            setChartProperties(props, chart);
            CategoryPlot cplot = (CategoryPlot)chart.getPlot();
            cplot.setForegroundAlpha( (float)getParameterAsDouble("foregroundAlpha",props, 0.0) );
            
            return chart.createBufferedImage(width*quality,height*quality); 
        }
       
        
        return null;
    }
    
    protected static void  setChartProperties(java.util.Properties props,JFreeChart chart)
    {
        String title = props.getProperty("title");
        if (title!=null && title.length() >0)
        {
               chart.setTitle(title);
               int titlePosition = getParameterAsInteger("titlePosition", props, 1);
               if (titlePosition == 1) chart.getTitle().setPosition(RectangleEdge.TOP );
               if (titlePosition == 2) chart.getTitle().setPosition(RectangleEdge.BOTTOM );
               if (titlePosition == 3) chart.getTitle().setPosition(RectangleEdge.LEFT );
               if (titlePosition == 4) chart.getTitle().setPosition(RectangleEdge.RIGHT );
               
        }
        
        title = props.getProperty("subtitle");
        if (title!=null && title.length() >0)
        {
            org.jfree.chart.title.TextTitle subtitle1 = new org.jfree.chart.title.TextTitle(title);
            chart.addSubtitle(subtitle1);
        }
        
        java.awt.Color bgColor = getParameterAsColor("chartBackground",props, java.awt.Color.WHITE);
        if (bgColor!=null) chart.setBackgroundPaint( bgColor );
        
        bgColor = getParameterAsColor("plotBackground",props, java.awt.Color.WHITE);
        if (bgColor!=null) chart.getPlot().setBackgroundPaint( bgColor );
        
    }
    
    
    
    protected static java.util.Properties  parseProperties(String[] properties)
    {
        java.util.Properties props = new java.util.Properties();
        
        for (int i=0; i<properties.length; ++i)
        {
            String key = properties[i].substring(0, properties[i].indexOf("="));
            String val = properties[i].substring(key.length()+1);
            props.setProperty(key,val);
        }
        return props;
    }
    
    protected static DefaultCategoryDataset getSampleCategoryDataset()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "Series 1", "Type 1");
        dataset.addValue(4.0, "Series 1", "Type 2");
        dataset.addValue(3.0, "Series 1", "Type 3");
        dataset.addValue(5.0, "Series 1", "Type 4");
        dataset.addValue(5.0, "Series 1", "Type 5");
        dataset.addValue(7.0, "Series 1", "Type 6");
        dataset.addValue(7.0, "Series 1", "Type 7");
        dataset.addValue(8.0, "Series 1", "Type 8");
        dataset.addValue(5.0, "Series 2", "Type 1");
        dataset.addValue(7.0, "Series 2", "Type 2");
        dataset.addValue(6.0, "Series 2", "Type 3");
        dataset.addValue(8.0, "Series 2", "Type 4");
        dataset.addValue(4.0, "Series 2", "Type 5");
        dataset.addValue(4.0, "Series 2", "Type 6");
        dataset.addValue(2.0, "Series 2", "Type 7");
        dataset.addValue(1.0, "Series 2", "Type 8");
        dataset.addValue(4.0, "Series 3", "Type 1");
        dataset.addValue(3.0, "Series 3", "Type 2");
        dataset.addValue(2.0, "Series 3", "Type 3");
        dataset.addValue(3.0, "Series 3", "Type 4");
        dataset.addValue(6.0, "Series 3", "Type 5");
        dataset.addValue(3.0, "Series 3", "Type 6");
        dataset.addValue(4.0, "Series 3", "Type 7");
        dataset.addValue(3.0, "Series 3", "Type 8");
        return dataset;
    }
}
