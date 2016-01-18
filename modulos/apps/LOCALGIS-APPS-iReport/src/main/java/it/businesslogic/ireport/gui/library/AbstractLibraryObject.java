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
 * AbstractLibraryObject.java
 * 
 * Created on 17 settembre 2004, 18.08
 *
 */

package it.businesslogic.ireport.gui.library;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
/**
 *
 * @author  Administrator
 */
abstract public class AbstractLibraryObject {
    
    private static javax.swing.ImageIcon defaultIcon;
    
    static {
        
        defaultIcon = new javax.swing.ImageIcon(AbstractLibraryObject.class.getResource("/it/businesslogic/ireport/icons/library/default.png"));
    }
    
    
    /** Creates a new instance of AbstractLibraryObject */
    public AbstractLibraryObject() {
    }
    
    protected JReportFrame getReportFrame()
    {
        MainFrame mf = MainFrame.getMainInstance();
        return mf.getActiveReportFrame();
    }
    
    protected Report getReport()
    {
        return getReportFrame().getReport();
    }
    
    protected void updateAllUI()
    {
        MainFrame mf = MainFrame.getMainInstance();
        mf.setActiveReportForm( mf.getActiveReportFrame() );
    }
    
    protected void addVariable(JRVariable variable)
    {
        getReport().addVariable(variable);
    }
    
    protected void addParameter(JRVariable variable)
    {
        getReport().addVariable(variable);

    }
    
    protected void addField(JRVariable variable)
    {
        getReport().addVariable(variable);
    }
    
    public abstract void drop(java.awt.dnd.DropTargetDropEvent dtde);
    
    public javax.swing.ImageIcon getIcon()
    {
        return defaultIcon;
    }
    
    public abstract String getName();
    
    public String toString()
    {
        return getName();
    }
}
