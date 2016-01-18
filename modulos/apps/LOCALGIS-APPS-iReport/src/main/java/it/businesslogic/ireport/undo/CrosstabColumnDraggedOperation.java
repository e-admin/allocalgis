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
 * CrosstabColumnDraggedOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.gui.CrosstabEditorPanel;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.CrosstabLayoutChangedEvent;

import java.awt.Point;
import java.util.Vector;

/**
 *  This class handle the band dragged operation.
 *  As all operations, the costructor take the JReportFrame (owner of the element)
 *  The ReportElement is not cloned, this can be a problem if not all undo operations
 *  are correctly logged and handled.
 *  This undo operation contains the band dragged and all elements that was repositioned
 *  after the draging.
 *  It reuse a lot of code of TransformElementsOperation.
 * @author  Giulio Toffoli
 */
public class CrosstabColumnDraggedOperation implements it.businesslogic.ireport.UndoOperation {
    private int delta = 0;
    
    private CrosstabEditorPanel editor = null;
    private CrosstabReportElement crosstabElement;
    private int draggedLineIndex = 0;
    
    /** Creates a new instance of InsertElementOperation */
    public CrosstabColumnDraggedOperation(CrosstabEditorPanel editor,  CrosstabReportElement element, int draggedLineIndex,int delta) {
        this.setEditor(editor);
        this.setCrosstabElement(element);
        this.setDraggedLineIndex(draggedLineIndex);
        this.setDelta(delta);
    }
    
    public void redo()
    {
        if (editor == null) return;

        int readyToDragCellHorizontally = draggedLineIndex;
        // Save relative position for all elements...
        // Save relative position for all elements...
        for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
        {
            ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
            re.setRelativePosition(new Point( re.getPosition().x - re.getCell().getLeft() - 10, re.getPosition().y - re.getCell().getTop() - 10 ));
        }

        // Modify hight of all cell in band readyToDragCellVertically...
        Vector cells = (Vector)getEditor().getColumnBands().elementAt(readyToDragCellHorizontally-1);
        for (int i=0; i<cells.size(); ++i)
        {
            CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
            cell.setWidth(cell.getWidth() + delta );
        }
        for (int j=readyToDragCellHorizontally; j<getEditor().getRowBands().size(); ++j)
        {
            cells = (Vector)getEditor().getColumnBands().elementAt(j);
            for (int i=0; i<cells.size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
                if (cell.getLeftIndex() >= readyToDragCellHorizontally)
                {
                    cell.setLeft(cell.getLeft() + delta );
                }
                else
                {
                    cell.setWidth(cell.getWidth() + delta );
                }
            }
        }

        // Adjust size value of all groups...
        for (int i=0; i< getCrosstabElement().getRowGroups().size(); ++i)
        {
            CrosstabGroup group = (CrosstabGroup)getCrosstabElement().getRowGroups().elementAt(i);
            group.setSize( group.getHeaderCell().getWidth() );
        }


        // Adjust all elements position with new cell positions...
        for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
        {
            ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
            re.getPosition().x = re.getRelativePosition().x + re.getCell().getLeft()+10;
            re.getPosition().y = re.getRelativePosition().y + re.getCell().getTop()+10;

            re.setPosition(re.position);
            re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
        }

        for (int i=readyToDragCellHorizontally; i<getEditor().getColumns().size(); ++i)
        {
            int rowPosition = ((Integer)getEditor().getColumns().get(i)).intValue() + delta;
            getEditor().getColumns().set(i, new Integer(rowPosition));
        }

        MainFrame.getMainInstance().getActiveReportFrame().setIsDocDirty(true);
        getCrosstabElement().fireCrosstabLayoutChangedListenerCrosstabLayoutChanged(new CrosstabLayoutChangedEvent(this, this.getCrosstabElement()));
    }
    
    public void undo()
    {
        if (editor == null) return;
        
        delta *= -1;

        this.redo();
            
        delta *= -1;
    }
    
    
    public String toString()
    {
        return "column resize";
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getDraggedLineIndex() {
        return draggedLineIndex;
    }

    public void setDraggedLineIndex(int draggedLineIndex) {
        this.draggedLineIndex = draggedLineIndex;
    }

    public CrosstabReportElement getCrosstabElement() {
        return crosstabElement;
    }

    public void setCrosstabElement(CrosstabReportElement crosstabElement) {
        this.crosstabElement = crosstabElement;
    }

    public CrosstabEditorPanel getEditor() {
        return editor;
    }

    public void setEditor(CrosstabEditorPanel editor) {
        this.editor = editor;
    }
    
        
}

