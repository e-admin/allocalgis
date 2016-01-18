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
 * ReportDocumentStatusChangedEvent.java
 * 
 * Created on 17 giugno 2003, 1.12
 *
 */

package it.businesslogic.ireport.gui.event;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.gui.*;
import java.util.*;
/**
 * Save all info required by a ReportListenerElementsSelectionEvent
 * Return the vector of selected elements (taked by the JReportFrame...)
 * @author  Administrator
 */
public class ReportDocumentStatusChangedEvent {
    
    private Report report;
    private boolean statusChanged = false;
    

    /** Creates a new instance of ReportListenerElementChangedEvent */
    public ReportDocumentStatusChangedEvent(Report report, boolean statusChanged) {
        this.report = report;
        this.setStatusChanged(statusChanged);
    }
    
    /** Getter for property jReportFrame.
     * @return Value of property jReportFrame.
     *
     */
    public it.businesslogic.ireport.Report getJReport() {
        return report;
    }    

    public boolean isStatusChanged() {
        return statusChanged;
    }

    public void setStatusChanged(boolean statusChanged) {
        this.statusChanged = statusChanged;
    }
}
