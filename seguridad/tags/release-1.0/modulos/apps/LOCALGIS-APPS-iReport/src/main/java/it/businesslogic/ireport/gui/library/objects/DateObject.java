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
 * DateObject.java
 * 
 * Created on 17 settembre 2004, 19.15
 *
 */

package it.businesslogic.ireport.gui.library.objects;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.gui.FieldPatternDialog;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.library.*;
import javax.swing.JOptionPane;
/**
 *
 * @author  Administrator
 */
public class DateObject extends AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/date.png"));
    }
    
    /** Creates a new instance of PageNumberObject */
    public DateObject() {
    }
    
    public String getName()
    {
        return it.businesslogic.ireport.util.I18n.getString("gui.library.objects.currentdate","Current date");
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        
        FieldPatternDialog fpd = new FieldPatternDialog(MainFrame.getMainInstance(),true);
        fpd.setOnlyDate(true);
        fpd.setVisible(true);
        if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            TextFieldReportElement re =  getReportFrame().dropNewTextField( dtde.getLocation(), "new java.util.Date()",  "java.util.Date", "Report");
            re.setPattern(fpd.getPattern());
            getReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getReportFrame(), re, ReportElementChangedEvent.CHANGED));
        }
    }
    
     public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
}
