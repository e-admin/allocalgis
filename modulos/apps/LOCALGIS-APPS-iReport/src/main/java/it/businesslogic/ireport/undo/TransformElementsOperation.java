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
 * TransformElementsOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.UndoOperation;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;

import java.util.Vector;

/**
 *  This class handle the Insert operation.
 *  As all operations, the costructor take the JReportFrame (owner of the element)
 *  The ReportElement is not cloned, this can be a problem if not all undo operations
 *  are correctly logged and handled.
 *  @author  Giulio Toffoli
 *  Modified by Robert Lamping, June 3, 2005
 *  // Also other Transformation can now be included
 *  // Once this works it should be redesigned again.
 *  // E.g. make a factory to retrieve the required transformation class.
 *  //
 */
public class TransformElementsOperation implements it.businesslogic.ireport.UndoOperation
{
    
    /*
     *  The report elements that was inserted.
     */
    private Vector transformations = null;
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of InsertElementOperation */
    public TransformElementsOperation(JReportFrame jrf)
    {
        this.transformations = new Vector();
        this.jrf = jrf;
    }
    
    /** Creates a new instance of InsertElementOperation */
    public TransformElementsOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement)
    {
        this.transformations = new Vector();
        this.crosstabReportElement = crosstabReportElement;
        this.jrf = jrf;
    }
    
    public void redo()
    {
        if (jrf == null) return;
        
        if (crosstabReportElement != null)
        {
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().setSelectedElement(null);
        }
        else
        {
            jrf.setSelectedElement(null);
        }
        
        Vector changed_elements = new Vector();
        
        for (int i= 0; i < this.getTransformations().size(); ++i)
        {
            ( (UndoOperation) (getTransformations().get(i))).redo();
            
            if ( getTransformations().get(i) instanceof ElementTransformation )
            {
                ElementTransformation pe = (ElementTransformation) getTransformations().get(i);
                ReportElement element = pe.element;
                // Add element....]
                if (crosstabReportElement != null)
                {
                    jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().addSelectedElement( element, false );
                }
                else
                {
                    jrf.addSelectedElement( element, false );
                }
                
                changed_elements.add( element ) ;
            }
            
        }
        
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, changed_elements , ReportElementChangedEvent.CHANGED));
        jrf.getMainFrame().getElementPropertiesDialog().updateSelection();
        
        if (crosstabReportElement != null)
        {
            jrf.getReportPanel().repaint();
        }
    }
    
    public void undo()
    {
        
        if (jrf == null) return;
        
        if (crosstabReportElement != null)
        {
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().setSelectedElement(null);
        }
        else
        {
            jrf.setSelectedElement(null);
        }
        Vector changed_elements = new Vector();
        
        for (int i= 0; i < this.getTransformations().size(); ++i)
        {
            // no matter what kind of transformation is used: element, page whatever
            // the undo operation should simply do its work.
            // By typecasting to UndoOperation this is achieved.
            // do not typecast to ElementTransformation
            // Reason: also the Page resize transformation should be able to be called here
            // Shrink command.
            
            ( (UndoOperation) (getTransformations().get(i))).undo();
            
            if ( getTransformations().get(i) instanceof ElementTransformation )
            {
                ElementTransformation pe = (ElementTransformation)getTransformations().get(i);
                ReportElement element = pe.element;
                // Add element....
                
                if (crosstabReportElement != null)
                {
                    jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().addSelectedElement( element, false );
                }
                else
                {
                    jrf.addSelectedElement( element, false );
                }
                
                changed_elements.add( element ) ;
            }
            
        }
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, changed_elements , ReportElementChangedEvent.CHANGED));
        jrf.getMainFrame().getElementPropertiesDialog().updateSelection();
        
        if (crosstabReportElement != null)
        {
            jrf.getReportPanel().repaint();
        }
    }
    
    public String toString()
    {
        return "transformation";
    }
    
    /** Getter for property elements.
     * @return Value of property elements.
     *
     *  To add an element, use the addElement method. Don't access directly
     *  addElement from the vector elements
     *
     */
    public java.util.Vector getTransformations()
    {
        return transformations;
    }
    
    /** Setter for property elements.
     * @param elements New value of property elements.
     *
     */
    public void setTransformations(java.util.Vector transformations)
    {
        this.transformations = transformations;
    }
    
    /*
     *  Add an element to the list of elements handled by this
     *  undo operation.
     *  You must supply the old and the new bound of you report.
     *  This make more simple the undo/redo operation...
     *
     */
    
    public void addElement(Object object)
    {
        ITransformation et = null;
        
        // Kind of factorylike structure.
        // you might wish to pass this to a superclass of ElementTransformation and PageTransformation
        // e.g. Transformation.java and use something like:  getFactoryTransformation( object );
        // for now (10 june 2005) this will suffice.

        if (object instanceof ReportElement)
        {
            et = new ElementTransformation();
        }
        else if (object instanceof JReportFrame)
        {
            et = new PageTransformation();
        }
        
        // ITransformation et = Transformation.getTransformation( object );
        try
        {
            this.getTransformations().add(et);
            et.captureCurrent( object );
        }
        catch (Exception ex)
        {
            // if not registered, then the object
            // can't be undone.
            // TODO: see whether you wish to raise a special exception
        }
    }
    
    public void captureUniqueModified(Object obj)
    {
        int pos = findTransformationElement( obj );
        try
        {
            ((ITransformation) getTransformations().elementAt( pos ) ).captureModified( obj);
        }
        catch ( Exception e )
        {
            
        }
    }
    
    private int findTransformationElement( Object object)
    {
        int pos = -1; // force exception when used in array
        
        for (int i= 0; i < this.getTransformations().size(); ++i)
        {
            if ( ((ITransformation) getTransformations().get(i)).equals(object) )
            {
                pos = i;
                break;
            }
        }
        return pos;
    }
    
}

