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
 * ChartDatasetPanel.java
 * 
 * Created on February 14, 2006, 2:02 PM
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.SubDataset;

/**
 *
 * @author gtoffoli
 */
public interface ChartDatasetPanel {
    public void setSubDataset(SubDataset s);

    /**
     * This method is used to higlight and focus a component that is generating an error.
     * Since the expression to show can be nested in some remote window, the array hold
     * the complete "path" required to open the right expression.
     *
     * The simplest case is a single Integer value (like for the PieChart)
     * A complicated case can be an error in an hyperlink parameter in some category series
     * that would make the array to be something like:
     * [0] CATEGORY_LIST
     * [1] index of the category
     * [2] (used for the category window) COMPONENT_HYPERLINK
     * [3] (for the hyper link component) COMPONENT_PARAMETERS
     * [4] Index of the parameter
     * [5] Expression to hilight in the parameter link window
     *
     */
    public void setFocusedExpression(Object[] expressionInfo);
    
    /**
     * this method is called when the container window is opened...
     */
    public void containerWindowOpened();
    
}
