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
 * FormatCommand.java
 * 
 */

package it.businesslogic.ireport.gui.command;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.undo.FormatElementsOperation;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * FormatCommand.java. Created on 9 mei 2005, 21:32 By Robert Lamping Using
 * the Command pattern
 */
/**
 * @author Fourdim
 */
public class FormatCommand
        implements ICommand
{
    
    /**
     * The attribute sleepUndo is introduced to repeat multiple commands. When
     * sleepUndo = true then it prevents the storage of multiple undo
     * transitions.
     */
    static boolean sleepUndo = false;

    
    
    /** mf is a reference to the Mainframe instance. */
    private MainFrame mf = null;
    
    /** mf is a reference to the current JReportFrame instance. */
    JReportFrame jrf = null;
    Vector oldSelectedElements = null;
    
    /** e is the enumeration of the selected elements. */
    Enumeration e = null;
    
    boolean treatAllElements = false;
        
    ReportElement re = null;
    int operationType = 0;
    int currentElementPosition = 0;
    FormatElementsOperation undoOp = null;
    
    
    public Vector getSelectedElements()
    {
        jrf = MainFrame.getMainInstance().getActiveReportFrame();
        if (jrf == null) return null;
        return MainFrame.getMainInstance().getActiveElementSelection(jrf);
    }
    
    public void setSelectedElements(Vector elements)
    {
        jrf = MainFrame.getMainInstance().getActiveReportFrame();
        if (jrf == null) return;
        
        if (jrf.getSelectedCrosstabEditorPanel() != null)
        {
            jrf.getSelectedCrosstabEditorPanel().setSelectedElements(elements);
        }
        else
        {
            jrf.setSelectedElements(elements);
        }
    }
    
    
    /**
     * DOCUMENT ME!
     */
    public void execute()
    {
        mf = MainFrame.getMainInstance();
        
        if (mf.getJMDIDesktopPane().getSelectedFrame() != null &&
                mf.getJMDIDesktopPane().getSelectedFrame() instanceof JReportFrame)
        {
            // everytime the current selectedFrame must be chosen
            jrf = (JReportFrame)mf.getJMDIDesktopPane().getSelectedFrame();
          
            if ( getTreatAllElements() ){
                oldSelectedElements = (Vector) getSelectedElements().clone();
                this.setSelectedElements((Vector) jrf.getReport().getElements().clone() );
            }
            
            // TODO: Buttons should listen to number of selectelements and
            // enable or disable themselves.
            if (getSelectedElements().isEmpty())
            {
                // do nothing
            }
            else
            {
                if (preCondition())
                {
                    re = null;
                    CrosstabReportElement cre = null;
                    if (jrf.getSelectedCrosstabEditorPanel() != null)
                    {
                        cre = jrf.getSelectedCrosstabEditorPanel().getCrosstabElement();
                    }
                    undoOp = new FormatElementsOperation(jrf, cre, operationType);
                    executeDeeper();
                    
                    //Compulsary:
                    postAction();
                }
            }
            
            // reset selectedElements selection
             if ( getTreatAllElements() ){
                this.setSelectedElements((Vector) oldSelectedElements.clone() );
            }
        }
    }
    
    /**
     * DOCUMENT ME!
     */
    void executeDeeper()
    {
        resetEnumeration();
        preparation(); // <========= method in subclass is called if present
        resetEnumeration();
        processElements();
    }
    
    /**
     * This is the regular processElements(0 in which the current selected elements
     * are processed
     * It is possible to use the variant where you pass your own selection of elements
     */
    void processElements()
    {
        processElements(this.e);
    }
    
    
    /**
     * Register the elements in the UndoOp instaed of misusing processElements.
     * in registerElements the method modify() is not executed.
     * It is use for multi command FormatCommands, such as FormatCommandOrganizeAsATable.
     * @param e DOCUMENT ME!
     */
    void registerElements()
    {
        Enumeration e = getSelectedElements().elements();
        while (e.hasMoreElements())
        {
            undoOp.addElement( e.nextElement() );
        }
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    void processElements(Enumeration e)
    {
        
        currentElementPosition = -1;
        
        while (e.hasMoreElements())
        {
            currentElementPosition++;
            re = (ReportElement)e.nextElement();
            
            // Undo preparation, phase 1 (pre element transition)
            // Rectangle oldBounds = new Rectangle(re.getBounds());
            
            if ( ! getSleepUndo() )
            {
                undoOp.addElement(re);
            }
            // else: "multicommandmode' more than one command is execute in one Undo Operation
            
            //undoOp.captureCurrent(re);  // acts on last element on stack
            
            // mf.logOnConsole( re.toString() );
            modify(); // <========= method in subclass is called
            
            // adjusting the boundary of the report element
            // to the current position, width and height.
            re.updateBounds();
            
            // Undo preparation, phase 2 (post element transition)
            // Rectangle newBounds = new Rectangle(re.getBounds());
            
            // se FormatCommand.java for undoOp.
            //mf.logOnConsole("NewBounds" + re.toString() + "\n");
            
            if ( ! getSleepUndo() )
            {
                undoOp.captureUniqueModified(re);
            }
            
            //undoOp.addElement(re, oldBounds, newBounds);
        }
    }
    
    /**
     * DOCUMENT ME!
     */
    void updateElements()
    {
        Enumeration e = getSelectedElements().elements();
        
        while (e.hasMoreElements())
        {
            try
            {
                // modify this so that also JReportFrame objects can be passed
                undoOp.captureUniqueModified( (ReportElement) e.nextElement() );
            }
            catch (Exception ex)
            {
                // exception occurs when element is diffent from ReportElement
            }
        }
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int getCurrentElementPosition()
    {
        
        return currentElementPosition;
    }
    
    /**
     * DOCUMENT ME!
     */
    void preparation()
    {
        //dummy
    }
    
    // TODO: Restrict access, make it private check with interface
    
    /**
     * DOCUMENT ME!
     */
    void modify()
    {
        // if the subclass has a method modify then that method will be executed
        // otherwise this one.
        // Fake, the method of the subclass will normally be performed.
    }
    
    /**
     * DOCUMENT ME!
     */
    void resetEnumeration()
    {
        e = getSelectedElements().elements();
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean preCondition()
    {
        
        return true;
    }
    
    /**
     * DOCUMENT ME!
     */
    void postAction()
    {
        if (jrf.getSelectedCrosstabEditorPanel() == null)
        {
            jrf.getReportPanel().repaint();

            if (!sleepUndo)
            {
                jrf.addUndoOperation(undoOp);

                mf.getDocumentStructurePanel().updateDocumentStructureTree(jrf);

                // this part should be move to a listener
                jrf.getReportPanel().repaint();
            }
        }
        else
        {
            jrf.getSelectedCrosstabEditorPanel().repaint();
            if (!sleepUndo)
            {
                jrf.addUndoOperation(undoOp);

                //mf.getDocumentStructurePanel().updateDocumentStructureTree(jrf);

                // this part should be move to a listener
                //jrf.getReportPanel().repaint();
            }
        }
             
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param enum2 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    Vector sortYX(Enumeration enum2)
    {
        
        Vector myElements = new Vector();
        
        while (enum2.hasMoreElements())
        {
            re = (ReportElement)enum2.nextElement();
            
            // insert this element in the right position...
            if (myElements.size() == 0)
            {
                myElements.add(re);
            }
            else
            {
                
                boolean inserted = false;
                
                for (int i = 0; i < myElements.size(); ++i)
                {
                    
                    ReportElement re2 = (ReportElement)myElements.elementAt(i);
                    
                    if (re.getPosition().y < re2.getPosition().y)
                    {
                        myElements.insertElementAt(re, i);
                        inserted = true;
                        
                        break;
                    }
                    else if (re.getPosition().y == re2.getPosition().y)
                    {
                        
                        if (re.getPosition().x < re2.getPosition().x)
                        {
                            myElements.insertElementAt(re, i);
                            inserted = true;
                            
                            break;
                        }
                    }
                }
                
                if (!inserted)
                {
                    myElements.addElement(re);
                }
            }
        }
        
        return myElements;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param enum2 DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    Vector sortXY(Enumeration enum2)
    {
        
        Vector myElements = new Vector();
        
        while (enum2.hasMoreElements())
        {
            re = (ReportElement)enum2.nextElement();
            
            // insert this element in the right position...
            if (myElements.size() == 0)
            {
                myElements.add(re);
            }
            else
            {
                
                boolean inserted = false;
                
                for (int i = 0; i < myElements.size(); ++i)
                {
                    
                    ReportElement re2 = (ReportElement)myElements.elementAt(i);
                    
                    if (re.getPosition().x < re2.getPosition().x)
                    {
                        myElements.insertElementAt(re, i);
                        inserted = true;
                        
                        break;
                    }
                    else if (re.getPosition().x == re2.getPosition().x)
                    {
                        
                        if (re.getPosition().y < re2.getPosition().y)
                        {
                            myElements.insertElementAt(re, i);
                            inserted = true;
                            
                            break;
                        }
                    }
                }
                
                if (!inserted)
                {
                    myElements.addElement(re);
                }
            }
        }
        
        return myElements;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    Vector getBandElements(Band b)
    {
        
        Vector bandElements = new Vector();
        
        for (Iterator i = getSelectedElements().iterator(); i.hasNext();)
        {
            
            ReportElement re = (ReportElement)i.next();
            
            if (re.getBand() == b)
            {
                bandElements.add(re);
            }
        }
        
        return bandElements;
    }
    
    /*
     * getBands() returns a unique list of bands that have selected elements in it.
     **/
    
    /**
     * getBands() selects all the bands that have selected elements in it.
     *
     * @return Vector with all the bands that have selected elements in it
     */
    Vector getBands()
    {
        
        Vector bands = new Vector();
        
        for (Iterator h = getSelectedElements().iterator(); h.hasNext();)
        {
            
            ReportElement bandRe = (ReportElement)h.next();
            
            if (bandRe.getBand() != null && !bands.contains(bandRe.getBand()))
            {
                bands.add(bandRe.getBand());
            }
        }
        
        return bands;
    }
    
    static public FormatCommand getCommand(int operationType)
    {
        
        // this must be move to a factory
        // MainFrame.getMainInstance().logOnConsole("" + operationType);
        switch (operationType)
        {
            
            case OperationType.ALIGN_TOP:
                return (new FormatCommandAlignTop());
                
            case OperationType.ALIGN_BOTTOM:
                return (new FormatCommandAlignBottom());
                
            case OperationType.ALIGN_LEFT:
                return (new FormatCommandAlignLeft());
                
            case OperationType.ALIGN_RIGHT:
                return (new FormatCommandAlignRight());
                
            case OperationType.ALIGN_HORIZONTAL_AXIS:
                return (new FormatCommandAlignHorizontalAxis());
                
            case OperationType.ALIGN_VERTICAL_AXIS:
                return (new FormatCommandAlignVerticalAxis());
                
            case OperationType.ALIGN_CENTER_HORIZONTALLY:
                return (new FormatCommandAlignCenterHorizontally());
                
            case OperationType.ALIGN_CENTER_VERTICALLY:
                return (new FormatCommandAlignCenterVertically());
                
            case OperationType.ALIGN_CENTER:
                return (new FormatCommandAlignCenter());
                
            case OperationType.SAME_HEIGHT:
                return (new FormatCommandSameHeight());
                
            case OperationType.SAME_HEIGHT_MAX:
                return (new FormatCommandSameHeightMax());
                
            case OperationType.SAME_HEIGHT_MIN:
                return (new FormatCommandSameHeightMin());
                
            case OperationType.SAME_WIDTH:
                return (new FormatCommandSameWidth());
                
            case OperationType.SAME_WIDTH_MAX:
                return (new FormatCommandSameWidthMax());
                
            case OperationType.SAME_WIDTH_MIN:
                return (new FormatCommandSameWidthMin());
                
            case OperationType.SAME_SIZE:
                return (new FormatCommandSameSize());
                
            case OperationType.ALIGN_TOP_TO_BAND:
                return (new FormatCommandAlignTopToBand());
                
            case OperationType.ALIGN_BOTTOM_TO_BAND:
                return (new FormatCommandAlignBottomToBand());
                
            case OperationType.ALIGN_TO_LEFT_MARGIN:
                return (new FormatCommandAlignToLeftMargin());
                
            case OperationType.ALIGN_TO_RIGHT_MARGIN:
                return (new FormatCommandAlignToRightMargin());
                
            case OperationType.MOVE_TO_LEFT_MARGIN:
                return (new FormatCommandMoveToLeftMargin());
                
            case OperationType.MOVE_TO_RIGHT_MARGIN:
                return (new FormatCommandMoveToRightMargin());
                
            case OperationType.JOIN_LEFT:
                return (new FormatCommandJoinLeft());
                
            case OperationType.JOIN_RIGHT:
                return (new FormatCommandJoinRight());
                
            case OperationType.EQUALS_SPACE_H:
                return (new FormatCommandEqualsSpaceH());
                
            case OperationType.EQUALS_SPACE_V:
                return (new FormatCommandEqualsSpaceV());
                
            case OperationType.INCREASE_SPACE_V:
                return (new FormatCommandIncreaseSpaceV());
                
            case OperationType.DECREASE_SPACE_V:
                return (new FormatCommandDecreaseSpaceV());
                
            case OperationType.DECREASE_SPACE_H:
                return (new FormatCommandDecreaseSpaceH());
                
            case OperationType.INCREASE_SPACE_H:
                return (new FormatCommandIncreaseSpaceH());
                
            case OperationType.REMOVE_SPACE_V:
                return (new FormatCommandRemoveSpaceV());
                
            case OperationType.ORGANIZE_AS_A_TABLE: // multicommand
                return (new FormatCommandOrganizeAsATable());
                
            case OperationType.ELEMENT_MAXIMIZE:
                return (new FormatCommandElementMaximize());
                
            case OperationType.ELEMENT_MAXIMIZE_H:
                return (new FormatCommandElementMaximizeH());
                
            case OperationType.ELEMENT_MAXIMIZE_V:
                return (new FormatCommandElementMaximizeV());
                
            case OperationType.CENTER_IN_BAND:
                return (new FormatCommandCenterInBand());
                
            case OperationType.CENTER_IN_BAND_V:
                return (new FormatCommandCenterInBandV());
                
            case OperationType.CENTER_IN_BAND_H:
                return (new FormatCommandCenterInBandH());
                
            case OperationType.SHRINK:
                return (new FormatCommandShrink());
                
            case OperationType.SHRINK_ALL:
                return (new FormatCommandShrink( OperationType.SHRINK_ALL ));  
                
            case OperationType.CENTER_IN_BACKGROUND:
                return (new FormatCommandCenterInBackground());
            default:
                return null;
        }
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MainFrame getMainFrame()
    {
        
        return mf;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param mf DOCUMENT ME!
     */
    void setMainFrame(MainFrame mf)
    {
        this.mf = mf;
    }
    
    /**
     * Setter for variable sleepUndo
     *
     * @param bool. Boolean value to set sleepUndo
     */
    void setSleepUndo(boolean bool)
    {
        this.sleepUndo = bool;
    }
    boolean getSleepUndo()
    {
        return this.sleepUndo;
    }
    
    
    void setTreatAllElements(boolean bool)
    {
        this.treatAllElements = bool;
    }
    boolean getTreatAllElements()
    {
        return this.treatAllElements;
    }

    
    
}
