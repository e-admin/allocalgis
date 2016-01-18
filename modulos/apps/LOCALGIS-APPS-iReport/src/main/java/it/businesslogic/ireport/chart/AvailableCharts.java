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
 * AvailableCharts.java
 * 
 * Created on 2 ottobre 2004, 10.29
 *
 */

package it.businesslogic.ireport.chart;
import it.businesslogic.ireport.gui.sheet.SheetProperty;

import java.util.Vector;

/**
 * @author  Administrator
 */
public class AvailableCharts {
    
    static private java.util.Vector charts;
    static private javax.swing.ImageIcon genericChart = null;
    static {
        
        charts = new Vector();
        

        ChartDefinition cd = new ChartDefinition();
        cd.setNeededSeries(new String[]{"Labels","Serie1"});
        cd.setChartName("Pie");
        cd.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/pie.png") ) );
        cd.setType("Pie charts");
        cd.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd);
              
        charts.add(cd);
        
        ChartDefinition cd2 = new ChartDefinition();
        
        cd2.setNeededSeries(new String[]{"Labels","Serie1"});
        cd2.setChartName("Pie3D");
        cd2.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/pie3d.png") ) );
        cd2.setType("Pie charts");
        cd2.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd2);
        cd2.getSheetProperties().add(new  SheetProperty("depthFactor","Depth factor", SheetProperty.NUMBER ,"0.2"));
        cd2.getSheetProperties().add(new  SheetProperty("foregroundAlpha","Foreground Alpha", SheetProperty.NUMBER ,"0.33"));
        
        charts.add(cd2);
        
        ChartDefinition cd3 = new ChartDefinition();
        cd3.setNeededSeries(new String[]{"Values","Categories","Series"});
        cd3.setChartName("Bar");
        cd3.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/bar.png") ) );
        cd3.setType("Bar charts");
        cd3.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd3);
        SheetProperty nsp = new  SheetProperty("plotOrientation","Plot orientation",SheetProperty.COMBOBOX_NK,"2");
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(1,"Horizontal"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(2,"Vertical"));
        cd3.getSheetProperties().add(nsp);
        cd3.getSheetProperties().add(new  SheetProperty("foregroundAlpha","Foreground Alpha", SheetProperty.NUMBER ,"0.33"));
        cd3.getSheetProperties().add(new  SheetProperty("valueLabel","Value label", SheetProperty.STRING,"" ));
        cd3.getSheetProperties().add(new  SheetProperty("categoryLabel","Category label", SheetProperty.STRING,"" ));
        charts.add(cd3);
        
        ChartDefinition cd4 = new ChartDefinition();
        cd4.setNeededSeries(new String[]{"Values","Categories","Series"});
        cd4.setChartName("Bar3D");
        cd4.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/bar3d.png") ) );
        cd4.setType("Bar charts");
        cd4.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd4);
        nsp = new  SheetProperty("plotOrientation","Plot orientation",SheetProperty.COMBOBOX_NK,"2");
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(1,"Horizontal"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(2,"Vertical"));
        cd4.getSheetProperties().add(nsp);
        cd4.getSheetProperties().add(new  SheetProperty("foregroundAlpha","Foreground Alpha", SheetProperty.NUMBER ,"0.33"));
        cd4.getSheetProperties().add(new  SheetProperty("valueLabel","Value label", SheetProperty.STRING,"" ));
        cd4.getSheetProperties().add(new  SheetProperty("categoryLabel","Category label", SheetProperty.STRING,"" ));
        charts.add(cd4);
        
        ChartDefinition cd5 = new ChartDefinition();
        cd5.setNeededSeries(new String[]{"Values","Categories","Series"});
        cd5.setChartName("Line");
        cd5.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/line.png") ) );
        cd5.setType("Bar charts");
        cd5.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd5);
        nsp = new  SheetProperty("plotOrientation","Plot orientation",SheetProperty.COMBOBOX_NK,"2");
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(1,"Horizontal"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(2,"Vertical"));
        cd5.getSheetProperties().add(nsp);
        cd5.getSheetProperties().add(new  SheetProperty("foregroundAlpha","Foreground Alpha", SheetProperty.NUMBER ,"0.33"));
        cd5.getSheetProperties().add(new  SheetProperty("valueLabel","Y Label label", SheetProperty.STRING,"" ));
        cd5.getSheetProperties().add(new  SheetProperty("categoryLabel","X Label", SheetProperty.STRING,"" ));
        charts.add(cd5);
        
        
        ChartDefinition cd6 = new ChartDefinition();
        cd6.setNeededSeries(new String[]{"Values","Categories","Series"});
        cd6.setChartName("Area");
        cd6.setIcon( new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/area.png") ) );
        cd6.setType("Bar charts");
        cd6.setFactory("JFreeChart-0.21");
        addChartSheetProperties(cd6);
        nsp = new  SheetProperty("plotOrientation","Plot orientation",SheetProperty.COMBOBOX_NK,"2");
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(1,"Horizontal"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(2,"Vertical"));
        cd6.getSheetProperties().add(nsp);
        cd6.getSheetProperties().add(new  SheetProperty("foregroundAlpha","Foreground Alpha", SheetProperty.NUMBER ,"0.33"));
        cd6.getSheetProperties().add(new  SheetProperty("valueLabel","Y Label label", SheetProperty.STRING,"" ));
        cd6.getSheetProperties().add(new  SheetProperty("categoryLabel","X Label", SheetProperty.STRING,"" ));
        charts.add(cd6);
        
        genericChart = new javax.swing.ImageIcon( AvailableCharts.class.getClass().getResource("/it/businesslogic/ireport/icons/charts/genericChart.png"));
    }
    
    /** 
        Return a vector of chart definitions
    */
    public static java.util.Vector getAvailableCharts() { 
        return charts;
    }
    
    public static javax.swing.ImageIcon getChartIcon(String chartName)
    {
        for (int i=0; i<charts.size(); ++i)
        {
            ChartDefinition cd = (ChartDefinition)charts.elementAt(i);
            if (cd.getChartName().equals(""+chartName)) return cd.getIcon();     
        }
        return genericChart;
    }
    
    public static void addChartSheetProperties(ChartDefinition cd)
    {
        cd.getSheetProperties().add(new  SheetProperty("width","Width", SheetProperty.INTEGER ));
        cd.getSheetProperties().add(new  SheetProperty("height","Height", SheetProperty.INTEGER ));
        cd.getSheetProperties().add(new  SheetProperty("quality","Zoom", SheetProperty.INTEGER,"2"));
        cd.getSheetProperties().add(new  SheetProperty("title","Chart title", SheetProperty.STRING ));
        cd.getSheetProperties().add(new  SheetProperty("subtitle","Subtitle", SheetProperty.STRING ));
        SheetProperty nsp = new  SheetProperty("titlePosition","Title position",SheetProperty.COMBOBOX_NK,"1");
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(1,"Top"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(2,"Bottom"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(3,"Left"));
        nsp.getTags().add(new it.businesslogic.ireport.gui.sheet.Tag(4,"Right"));
        cd.getSheetProperties().add(nsp);
        cd.getSheetProperties().add(new  SheetProperty("chartBackground","Chart Background", SheetProperty.COLOR ,"[255,255,255]"));
        cd.getSheetProperties().add(new  SheetProperty("plotBackground","Plot Background", SheetProperty.COLOR ,"[255,255,255]"));
        cd.getSheetProperties().add(new  SheetProperty("antialias","Antialias", SheetProperty.BOOLEAN ,"true"));
        cd.getSheetProperties().add(new  SheetProperty("tooltips","Show tooltips", SheetProperty.BOOLEAN ,"false"));
        cd.getSheetProperties().add(new  SheetProperty("legend","Show legend", SheetProperty.BOOLEAN ,"false"));
    }
}
