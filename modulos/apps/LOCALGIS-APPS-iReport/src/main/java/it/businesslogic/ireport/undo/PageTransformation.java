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
 * PageTransformation.java
 * 
 * Created on June 5, 2005 11:31 RL
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.gui.JReportFrame;

/**
 * @author Robert Lamping
 * When changing the page size during an elementtransaction, (like in FormatCommandShrink),
 * then there is a need to undo this
 *
 * TO DO
 * 1. Recalc columnwidth!!!
 */
public class PageTransformation implements it.businesslogic.ireport.UndoOperation, ITransformation 
{
    // the target in question
    JReportFrame jrf;
    
    //OLD
    private int oldWidth;
    private int oldHeight;
    private String oldReportFormat;
    private int oldColumnWidth;
    
    //NEW
    private int newWidth;
    private int newHeight;
    private String newReportFormat;
    private int newColumnWidth;
    
    public PageTransformation()
    {
    }
    
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof JReportFrame)
        {
           if (this.jrf != null ) {
            isEqual = this.jrf.equals( (JReportFrame) obj );
           }
        } 
        return isEqual;
    }
    
    
    // As the element will not be removed.
    // the element should be able to undo itself
    public void undo()
    {
        this.jrf.getReport().setWidth( oldWidth );
        this.jrf.getReport().setHeight( oldHeight );
        this.jrf.getReport().setReportFormat(oldReportFormat );
        this.jrf.getReport().setColumnWidth( oldColumnWidth );
    }
    
    public void redo()
    {
        this.jrf.getReport().setWidth( newWidth );
        this.jrf.getReport().setHeight( newHeight );
        this.jrf.getReport().setReportFormat( newReportFormat );
        this.jrf.getReport().setColumnWidth( newColumnWidth );
    }
    
    /**
     * Let this class decide on its own which information is captures
     * for a later redo.
     */
    public void captureCurrent(Object obj)
    {
        // TODO you might wish to check whether this the obj is instanceof JReportFrame
        // or enclose with try/catch
        this.jrf = (JReportFrame) obj;
        oldWidth = jrf.getReport().getWidth();
        oldHeight = jrf.getReport().getHeight();
        oldReportFormat = jrf.getReport().getReportFormat();
        oldColumnWidth = jrf.getReport().getColumnWidth();
        
    }
    
    /**
     * Let this class decide on its own which information is captures
     * for a later unddo
     */
    public void captureModified( Object obj)
    {
        JReportFrame jrf = (JReportFrame) obj;
        newWidth = jrf.getReport().getWidth();
        newHeight = jrf.getReport().getHeight();
        newReportFormat = jrf.getReport().getReportFormat();
        newColumnWidth = jrf.getReport().getColumnWidth();

    }
    
}
