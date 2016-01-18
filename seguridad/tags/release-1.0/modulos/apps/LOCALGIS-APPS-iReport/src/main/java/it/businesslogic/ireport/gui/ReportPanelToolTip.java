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
 * ReportPanelToolTip.java
 * 
 * Created on 3 giugno 2005, 0.38
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.*;
/**
 *
 * @author  Administrator
 */
public class ReportPanelToolTip extends javax.swing.JToolTip {
    
    private JReportFrame reportFrame = null;
    
    private Band band = null;
    private ReportElement reportElement = null;
    
    /** Creates a new instance of ReportPanelToolTip */
    public ReportPanelToolTip(JReportFrame rf) {
                this.setReportFrame(rf);
    }
    
    public String getTipText()
    {
        String toolTipText = "";
        if (band == null)
        {
            return null;
        }
        toolTipText = band.getName();
        return toolTipText;
    }
    
    public JReportFrame getReportFrame() {
        return reportFrame;
    }

    public void setReportFrame(JReportFrame reportFrame) {
        this.reportFrame = reportFrame;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public ReportElement getReportElement() {
        return reportElement;
    }

    public void setReportElement(ReportElement reportElement) {
        this.reportElement = reportElement;
    }
    
}
