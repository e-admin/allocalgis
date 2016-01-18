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
 * ChartDefinition.java
 * 
 * Created on 2 ottobre 2004, 10.23
 *
 */

package it.businesslogic.ireport.chart;
import java.util.*;
/** 
 *
 * @author  Administrator
 */
public class ChartDefinition {
    
    private javax.swing.ImageIcon icon;
    private String[] neededSeries;
    private String chartCategory = "";
    private String chartName = "";
    private Vector sheetProperties = null;
    
        
    /** Creates a new instance of ChartDefinition */
    public ChartDefinition() {
        setSheetProperties(new Vector());
    }

    public javax.swing.ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(javax.swing.ImageIcon icon) {
        this.icon = icon;
    }

    public String[] getNeededSeries() {
        return neededSeries;
    }

    public void setNeededSeries(String[] neededSeries) {
        this.neededSeries = neededSeries;
    }

    public String getType() {
        return chartCategory;
    }

    public void setType(String type) {
        this.chartCategory = type;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }
    
    public String toString()
    {
        return getChartName();
        
    }

    private String factory;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public Vector getSheetProperties() {
        return sheetProperties;
    }

    public void setSheetProperties(Vector sheetProperties) {
        this.sheetProperties = sheetProperties;
    }
}
