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
 * IReportCustomizer.java
 * 
 * Created on June 8, 2006, 9:18 AM
 *
 */

package it.businesslogic.ireport.examples.chart;

import java.awt.Font;

import net.sf.jasperreports.engine.JRChart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

/**
 *
 * @author gtoffoli
 */
public class IReportCustomizer implements net.sf.jasperreports.engine.JRChartCustomizer {
    
   
    public void customize(JFreeChart jFreeChart, JRChart jRChart) {
   
        CategoryItemRenderer renderer = jFreeChart.getCategoryPlot().getRenderer();
        renderer.setPositiveItemLabelPosition(
                new ItemLabelPosition( ItemLabelAnchor.OUTSIDE12, org.jfree.ui.TextAnchor.BOTTOM_CENTER) );
        renderer.setItemLabelFont(new Font("SansSerif", Font.BOLD, 10));
        renderer.setItemLabelsVisible(true);
    }
   
}
