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
 * RemoveMarginsOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.gui.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.util.*;


/**
 *
 * @author  Administrator
 */
public class RemoveMarginsOperation implements it.businesslogic.ireport.UndoOperation {
    
    
    private int oldTop = 0;
    private int oldLeft = 0;
    private int oldBottom = 0;
    private int oldRight = 0;
    
    private JReportFrame jrf = null;
    
    
    /** Creates a new instance of RemoveMarginsOperation */
    public RemoveMarginsOperation(JReportFrame jrf, int oldTop, int oldBottom, int oldLeft, int oldRight ) {
        
        this.oldTop = oldTop;
        this.oldBottom = oldBottom;
        this.oldLeft = oldLeft;
        this.oldRight = oldRight;
                
        this.jrf = jrf;
    }
        
     public void redo()
    {
        if (jrf == null) return;

        jrf.getReport().setTopMargin( 0 );
        jrf.getReport().setBottomMargin( 0 );
        jrf.getReport().setLeftMargin( 0 );
        jrf.getReport().setRightMargin( 0 );
        
        jrf.getReport().setWidth( jrf.getReport().getWidth() - oldLeft - oldRight );
        
        if (oldTop != 0 || oldLeft != 0) {
                    Enumeration e = jrf.getReport().getElements().elements();
                    while (e.hasMoreElements()) {
                        ReportElement re = (ReportElement)e.nextElement();
                        re.trasform(new java.awt.Point(-oldLeft,-oldTop), TransformationType.TRANSFORMATION_MOVE);
                    }
                }
                
        jrf.setIsDocDirty(true);
        jrf.getReportPanel().repaint();
    }
    
    public void undo()
    {
        if (jrf == null) return;

        jrf.getReport().setTopMargin( oldTop );
        jrf.getReport().setBottomMargin( oldBottom );
        jrf.getReport().setLeftMargin( oldLeft );
        jrf.getReport().setRightMargin( oldRight );
        
        jrf.getReport().setWidth( jrf.getReport().getWidth() + oldLeft + oldRight );
        
        if (oldTop != 0 || oldLeft != 0) {
                    Enumeration e = jrf.getReport().getElements().elements();
                    while (e.hasMoreElements()) {
                        ReportElement re = (ReportElement)e.nextElement();
                        re.trasform(new java.awt.Point(oldLeft,oldTop), TransformationType.TRANSFORMATION_MOVE);
                    }
                }
                
        jrf.setIsDocDirty(true);
        jrf.getReportPanel().repaint();
    }   
    
    public String toString()
    {
        return "remove margins";
    }
}
