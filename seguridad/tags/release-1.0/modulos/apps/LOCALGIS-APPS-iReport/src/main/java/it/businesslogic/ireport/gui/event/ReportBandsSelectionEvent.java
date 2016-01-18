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
 * ReportElementsSelectionEvent.java
 * 
 * Created on 17 giugno 2003, 1.12
 *
 */

package it.businesslogic.ireport.gui.event;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.gui.*;
/**
 * Save all info required by a ReportListenerElementsSelectionEvent
 * Return the vector of selected elements (taked by the JReportFrame...)
 * @author  Administrator
 */
public class ReportBandsSelectionEvent {
    
    private JReportFrame jReportFrame;
    private CrosstabReportElement crosstabReportElement = null;
    
    private java.util.Vector selectedBands;
    /** Creates a new instance of ReportListenerElementsSelectionEvent */
    public ReportBandsSelectionEvent(JReportFrame jReportFrame, java.util.Vector selectedBands) {
        this.jReportFrame = jReportFrame;
        this.setSelectedBands(selectedBands);
    }
    
    public ReportBandsSelectionEvent(JReportFrame jReportFrame, CrosstabReportElement crosstabReportElement, java.util.Vector selectedBands) {
        this.jReportFrame = jReportFrame;
        this.crosstabReportElement = crosstabReportElement;
        this.setSelectedBands(selectedBands);
    }
    

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }

    public java.util.Vector getSelectedBands() {
        return selectedBands;
    }

    public void setSelectedBands(java.util.Vector selectedBands) {
        this.selectedBands = selectedBands;
    }
    
}
