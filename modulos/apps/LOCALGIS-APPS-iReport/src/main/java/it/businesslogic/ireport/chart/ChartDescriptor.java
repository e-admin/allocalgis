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
 * ChartDescriptor.java
 * 
 * Created on 9 luglio 2005, 0.27
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author Administrator
 */
public class ChartDescriptor {
    
    private javax.swing.ImageIcon icon;
    private String chartKeyName = "";
    private String implementationClass = "";
    
    /** Creates a new instance of ChartDescriptor */
    public ChartDescriptor(String iconName, String chartKeyName, String implementationClass) {
        
        setIcon(new javax.swing.ImageIcon( getClass().getResource(iconName) ));
        this.setChartKeyName(chartKeyName);
        this.setImplementationClass(implementationClass);
    }

    public javax.swing.ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(javax.swing.ImageIcon icon) {
        this.icon = icon;
    }

    public String getChartKeyName() {
        return chartKeyName;
    }

    public void setChartKeyName(String chartKeyName) {
        this.chartKeyName = chartKeyName;
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString(getChartKeyName(),getChartKeyName());
    }
    
    
}
