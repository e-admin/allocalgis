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
 * ElementTransformation.java
 * 
 * Created on 29 giugno 2003, 0.32
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import java.awt.*;


/**
 *
 * @author Giulio
 * Modified by Robert Lamping, June 3, 2005
 * Consideration:
 *   An ElementTransformation should be able by itself to undo or redo
 *   its transformation. This is behaviour that should be encapsulated.
 *
 *   How to inform the environment that the element has changed is another thing to
 *   research.
 *
 *   What to capture to be able to restore the old situation should be
 *   as much as possible be the responsibility of the ElementTransformation.
 *
 */
public class ElementTransformation implements it.businesslogic.ireport.UndoOperation, ITransformation
{
    public  ReportElement element;
    
    public Rectangle oldBounds;
    public Rectangle newBounds;
    
    public Band oldBand;
    public Band newBand;
    
    public CrosstabCell oldCell;
    public CrosstabCell newCell;
    
    private Object oldFontSize;
    private Object newFontSize;
    
    
    public ElementTransformation()
    {
        // oldBounds = newBounds = new Rectangle();
    }
    
    // As the element will not be removed.
    // the element should be able to undo itself
    
    
    
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        if (obj instanceof ReportElement)
        {
            if (this.element != null )
            {
                isEqual = this.element.equals( (ReportElement) obj );
            }
        }
        
        return isEqual;
    }
    
    public void undo()
    {
        //System.out.println("Old bound: " + this.oldBounds);
        
        element.setPosition( new Point( this.oldBounds.x, this.oldBounds.y) ) ;
        
        element.setWidth( this.oldBounds.width);
        element.setHeight(this.oldBounds.height);
        
        element.updateBounds();
        
        // band
        if ( this.oldBand != null)
        {
            element.setBand( this.oldBand );
        }
        
        if ( this.oldCell != null)
        {
            element.setCell( this.oldCell );
        }
        
        // Restore relative position...
        if (element.getCell() != null)
        {
            element.setRelativePosition( new Point(element.getPosition().x - element.getCell().getLeft()- 10, element.getPosition().y - element.getCell().getTop()- 10)); 
        }
        
        // font
        if ( element instanceof TextReportElement)
        {
            try
            {
                ((TextReportElement) element).getIReportFont().setPropertyValue( IReportFont.FONT_SIZE,  oldFontSize);
            }
            catch (Exception e )
            {
                // fontsize not properly set
            }
        }
        
    }
    
    public void redo()
    {
        // location and size
        if (element == null || this.newBounds == null) return;
        element.setPosition( new Point( this.newBounds.x, this.newBounds.y) );
        
        element.setWidth(this.newBounds.width);
        element.setHeight(this.newBounds.height);
        
        element.updateBounds( );
        
        // band
        if ( this.newBand != null)
        {
            element.setBand( this.newBand );
        }
        
        if ( this.newCell != null)
        {
            element.setCell( this.newCell );
        }
        
        // Restore relative position...
        if (element.getCell() != null)
        {
            element.setRelativePosition( new Point(element.getPosition().x - element.getCell().getLeft()- 10, element.getPosition().y - element.getCell().getTop()- 10)); 
        }
        
        // font
        if ( element instanceof TextReportElement)
        {
            try
            {    
                 ((TextReportElement) element).getIReportFont().setPropertyValue( IReportFont.FONT_SIZE,  newFontSize);
            }
            catch (Exception e )
            {
                // fontsize not properly set
            }
        }
        
    }
    
    /**
     * Let this class decide on its own which information is captures
     * for a later redo.
     */
    public void captureCurrent( Object obj)
    {
        ReportElement element = (ReportElement) obj;
        this.element = element;
        this.oldBounds = new Rectangle( element.getBounds() );
        this.oldBand = element.getBand();
        this.oldCell = element.getCell();
        
        if ( element instanceof TextReportElement)
        {
            this.oldFontSize = ((TextReportElement) element).getIReportFont().getPropertyValue( IReportFont.FONT_SIZE);
        }
    }
    
    /**
     * Let this class decide on its own which information is captures
     * for a later unddo
     */
    public void captureModified(Object obj)    // implicit typecast from Object to ReportElement?? NO!!
    {
        ReportElement element = (ReportElement) obj;
        this.newBounds = new Rectangle( element.getBounds() );
        this.newBand = element.getBand();
        this.newCell = element.getCell();
        
        if ( element instanceof TextReportElement)
        {
            this.newFontSize = ((TextReportElement) element).getIReportFont().getPropertyValue( IReportFont.FONT_SIZE);
        }
        
    }
    
    
}
