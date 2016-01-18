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
 * ReportFrameActivatedEvent.java
 * 
 * Created on 10 febbraio 2003, 2.04
 *
 */

package it.businesslogic.ireport.gui.event;

import it.businesslogic.ireport.gui.JReportFrame;
/**
 *
 * @author  Administrator
 */
public class ReportFrameActivatedEvent {
    
    private JReportFrame reportFrame = null; 
    private Object source = null;
    
    /** Creates a new instance of ValueChangedEvent */
    public ReportFrameActivatedEvent(Object source, JReportFrame reportFrame) {
        
        this.setSource(source);
        this.setReportFrame(reportFrame);
    }

    public JReportFrame getReportFrame() {
        return reportFrame;
    }

    public void setReportFrame(JReportFrame reportFrame) {
        this.reportFrame = reportFrame;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
  
    
}
