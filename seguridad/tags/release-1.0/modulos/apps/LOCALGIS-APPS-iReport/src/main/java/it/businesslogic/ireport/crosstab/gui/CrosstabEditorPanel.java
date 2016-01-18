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
 * CrosstabEditorPanel.java
 * 
 * Created on January 12, 2006, 4:19 PM
 *
 */

package it.businesslogic.ireport.crosstab.gui;

import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.FrameReportElement;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.ReportElementFactory;
import it.businesslogic.ireport.ReportElementType;
import it.businesslogic.ireport.StaticTextReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.undo.CrosstabColumnDraggedOperation;
import it.businesslogic.ireport.undo.CrosstabRowDraggedOperation;
import it.businesslogic.ireport.undo.InsertElementOperation;
import it.businesslogic.ireport.undo.TransformElementsOperation;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.TextReportElement;
import it.businesslogic.ireport.gui.FieldPatternDialog;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.command.FormatCommand;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.undo.ChangeEmentsOrderOperation;
import it.businesslogic.ireport.undo.DeleteElementsOperation;
import it.businesslogic.ireport.undo.PasteStyleOperation;
import it.businesslogic.ireport.undo.ReplacedElementsOperation;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class CrosstabEditorPanel extends javax.swing.JPanel implements java.awt.dnd.DropTargetListener {

    // Menus...
    private javax.swing.JMenu jMenuAlign;
    private javax.swing.JMenuItem jMenuItemAlignLeft;
    private javax.swing.JMenuItem jMenuItemAlignRight;
    private javax.swing.JMenuItem jMenuItemAlignTop;
    private javax.swing.JMenuItem jMenuItemAlignBottom;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JMenuItem jMenuItemAlignVerticalAxis;
    private javax.swing.JMenuItem jMenuItemAlignHorizontalAxis;
    private javax.swing.JMenu jMenuSize;
    private javax.swing.JMenuItem jMenuItemSameWidth;
    private javax.swing.JMenuItem jMenuItemSameWidthMax;
    private javax.swing.JMenuItem jMenuItemSameWidthMin;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JMenuItem jMenuItemSameHeight;
    private javax.swing.JMenuItem jMenuItemSameHeightMax;
    private javax.swing.JMenuItem jMenuItemSameHeightMin;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JMenuItem jMenuItemSameSize;
    private javax.swing.JMenu jMenuPosition;
    private javax.swing.JMenuItem jMenuItemCenterH;
    private javax.swing.JMenuItem jMenuItemCenterV;
    private javax.swing.JMenuItem jMenuItemCenterInCell;
    private javax.swing.JMenuItem jMenuItemJoinLeft;
    private javax.swing.JMenuItem jMenuItemJoinRight;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JMenuItem jMenuHSpacing;
    private javax.swing.JMenuItem jMenuItemHSMakeEqual;
    private javax.swing.JMenuItem jMenuItemHSIncrease;
    private javax.swing.JMenuItem jMenuItemHSDecrease;
    private javax.swing.JMenuItem jMenuItemHSRemove;
    private javax.swing.JMenuItem jMenuVSpacing;
    private javax.swing.JMenuItem jMenuItemVSMakeEqual;
    private javax.swing.JMenuItem jMenuItemVSIncrease;
    private javax.swing.JMenuItem jMenuItemVSDecrease;
    private javax.swing.JMenuItem jMenuItemVSRemove;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JMenuItem jMenuItemBringToFront;
    private javax.swing.JMenuItem jMenuItemSendToBack;

    private javax.swing.JMenuItem jMenuItemFillCell;
    private javax.swing.JMenuItem jMenuItemFillCellH;
    private javax.swing.JMenuItem jMenuItemFillCellV;

    private double zoomFactor = 2.0;
    private boolean animation = false;

    private java.util.List openedNodesDocumentStructure = null;

    private boolean defaultCellMode = false;
    /**
     *  It contains the number of row ready to be dragged.
     **/
    private int readyToDragCellVertically = -1;

    /**
     *  It contains the number of column ready to be dragged
     **/
    private int readyToDragCellHorizontally = -1;

    /**
     * Point from wich a drag operation is started
     **/
    private Point dragging_origin = null;

    /**
     *  True if I'm dragging a cell...
     **/
    private boolean cell_dragging = false;

    private int newObjectType = ReportElementType.NONE;
    private boolean resistenceExceeded = false;

    /**
     * This flag is true if we are trasforming elements...
     */
    private boolean trasforming = false;
    /**
     * The type of transformation (see TransformationTypes)...
     */
    private int transformation_type= TransformationType.TRANSFORMATION_NONE;
    /**
     * Point to start the calculation of transformation delta
     */
    private Point transformation_origin=null;
    /**
     * Point to end the calculation of transformation delta
     */
    private Point transformation_origin_end=null;
    private Point transformation_undo_delta=null;
    private boolean firstXORDrawTransforming = true;
    private boolean firstXORDraw = true ;
    private Rectangle transformationOldBound = null;

    private JReportFrame parentReportFrame = null;

    /**
     * Point to start the calculation of the new object
     */
    private Point newObjectOrigin = null;

    /**
     * This flag is true if we are selecting elements drawing a rectangle...
     */
    private boolean drag_selection_mode = false;
    /**
     * The origin of the selection rectangle...
     */
    private java.awt.Point drag_selection_origin = null;
    /**
     * The end of the selection rectangle...
     */
    private java.awt.Point drag_selection_end = null;
    /**
     * The flag is used to handle XOR operation on rectangle selection
     */
    private boolean first_draw_selection_rect = true;

    /**
     * Stroke to use when draw selection rectangle
     */
    private java.awt.Stroke selectionStroke = null;

    /**
     * Position of the mouse of the last draw operation. Used to restore XOR draw.
     **/
    private Point mouse = new Point(0,0);

    private Vector rowBands = new Vector();
    private Vector columnBands = new Vector();

    /**
     * This array stores vertical lines positions (the number of lines is:
     * [# of row groups] + [# of column groups] + [# of total column groups]
     */
    private List columns = new ArrayList();
    /**
     * This array stores horizontal lines positions (the number of lines is:
     * [# of column groups] + [# of row groups] + [# of total row groups]
     */
    private List rows = new ArrayList();

    /**
     * this vector contains the current selection in the editor
     */
    private Vector selectedElements = new Vector();


    /**
     * CrosstabCell selected when right mouse is pressed
     */
    private CrosstabCell selectedCell = null;


    private static final javax.swing.ImageIcon crosstabCornerImage = new javax.swing.ImageIcon(CrosstabEditorPanel.class.getResource("/it/businesslogic/ireport/icons/crosstabcorner.png"));
    private static final javax.swing.ImageIcon crosstabBorderImage = new javax.swing.ImageIcon(CrosstabEditorPanel.class.getResource("/it/businesslogic/ireport/icons/crosstabborder.png"));
    private static final javax.swing.ImageIcon crosstabBorderHImage = new javax.swing.ImageIcon(CrosstabEditorPanel.class.getResource("/it/businesslogic/ireport/icons/crosstabborderh.png"));

    private CrosstabEditor editor = null;
    /** Creates new form CrosstabEditorPanel */
    public CrosstabEditorPanel() {
        initComponents();

        selectionStroke = new java.awt.BasicStroke(
                (float)(2f),
                java.awt.BasicStroke.CAP_BUTT,
                java.awt.BasicStroke.JOIN_BEVEL,
                0f,
                new float[]{5f, 3f},
                0f);

         addFormatItemsToMenu(jPopupMenuCrosstabReporteElement);
         new DropTarget( this, // component
        DnDConstants.ACTION_COPY_OR_MOVE, // actions
         this); // DropTargetListener
         
         
         I18n.addOnLanguageChangedListener( new LanguageChangedListener() {
             public void languageChanged(LanguageChangedEvent evt) {
                applyI18n();
             }
         });
         applyI18n();
    }

    public void updateSize() {
        int maxWidth  = 0;
        int maxHeight = 0;

        if (getColumns().size() > 0) maxWidth = ((Integer)getColumns().get( getColumns().size() -1)).intValue();
        if (getRows().size() > 0) maxHeight = ((Integer)getRows().get( getRows().size() -1)).intValue();

        if (getRows().size() == 0) maxHeight += getRowHeight("");
        if (getColumns().size() == 0) maxWidth += getColumnWidth("");



        if (getCrosstabElement().getHeight() > maxHeight) maxHeight = getCrosstabElement().getHeight();
        if (getCrosstabElement().getWidth() > maxWidth) maxWidth = getCrosstabElement().getWidth();
        Dimension d = new Dimension( (int)(maxWidth*getZoomFactor()+20),(int)(maxHeight*getZoomFactor()+20));
        this.setSize( d );
        this.setPreferredSize(d);
        this.setMinimumSize(d);
        this.setMaximumSize( d);
    }

    public CrosstabReportElement getCrosstabElement() {
        if (getEditor() != null) return getEditor().getCrosstabElement();
        return null;
    }

    /**
     * This method paint the wthite surface of the crossrtab
     **/
    public void paintDocument(Graphics2D g2) {
        CrosstabReportElement crosstabElement = getCrosstabElement();

        if (crosstabElement == null) return;

        g2.setPaint( java.awt.Color.WHITE);

        int maxWidth  = 0;
        int maxHeight = 0;

        if (getColumns().size() > 0) maxWidth = ((Integer)getColumns().get( getColumns().size() -1)).intValue();
        if (getRows().size() > 0) maxHeight = ((Integer)getRows().get( getRows().size() -1)).intValue();

        if (getRows().size() == 0) maxHeight += getRowHeight("");
        if (getColumns().size() == 0) maxWidth += getColumnWidth("");

        if (getCrosstabElement().getHeight() > maxHeight) maxHeight = getCrosstabElement().getHeight();
        if (getCrosstabElement().getWidth() > maxWidth) maxWidth = getCrosstabElement().getWidth();

        int zoomedCrosstabWidth = getZoomedDim(maxWidth);
        int zoomedCrosstabHeight = getZoomedDim(maxHeight);

        g2.fillRect(10,10, zoomedCrosstabWidth, zoomedCrosstabHeight);

        // BORDER TOP...
        AffineTransform at = new AffineTransform();
        at.translate(17,0);
        at.scale(zoomedCrosstabWidth-14,1);
        g2.drawImage(crosstabBorderImage.getImage(),at, this);

        // BORDER BOTTOM
        at = new AffineTransform();
        at.translate(17,zoomedCrosstabHeight+20);
        at.scale(zoomedCrosstabWidth-14,1);
        at.scale(1,-1);
        g2.drawImage(crosstabBorderImage.getImage(),at, this);

        // LEFT
        at = new AffineTransform();
        at.translate(0,17);
        at.scale(1,zoomedCrosstabHeight-14);
        g2.drawImage(crosstabBorderHImage.getImage(),at, this);

        // RIGHT
        at = new AffineTransform();
        at.translate(zoomedCrosstabWidth+20,17);
        at.scale(1,zoomedCrosstabHeight-14);
        at.scale(-1,1);
        g2.drawImage(crosstabBorderHImage.getImage(),at, this);


        // CORNERS
        g2.drawImage(crosstabCornerImage.getImage(),0,0, this);
        at = new AffineTransform();
        at.translate(0, zoomedCrosstabHeight+crosstabCornerImage.getIconHeight()+3);
        at.scale(1,-1);
        g2.drawImage(crosstabCornerImage.getImage(),at, this);

        at = new AffineTransform();

        //at.scale(-1,1); // Horizontal flip
        at.translate(zoomedCrosstabWidth +crosstabCornerImage.getIconWidth()+3,0);
        at.scale(-1,1);
        g2.drawImage(crosstabCornerImage.getImage(),at, this);

        at.translate(0, zoomedCrosstabHeight+crosstabCornerImage.getIconHeight()+3);
        at.scale(1,-1);
        g2.drawImage(crosstabCornerImage.getImage(),at, this);

        // Now we have to paint the cells...



        maxWidth  = 0;
        maxHeight = 0;



        g2.setColor(new Color(220,220,255));

        if (getColumns().size() > 0) maxWidth = ((Integer)getColumns().get( getColumns().size() -1)).intValue();
        if (getRows().size() > 0) maxHeight = ((Integer)getRows().get( getRows().size() -1)).intValue();

        for (int i=0; i< getColumns().size(); ++i) {
            int x = ((Integer)getColumns().get(i)).intValue();
            if (x>0)  g2.drawLine(  10 + getZoomedDim(x)-1, 10, 10 + getZoomedDim(x)-1, getZoomedDim(maxHeight)-1+10);
        }

        for (int i=0; i< getRows().size(); ++i) {
            int y = ((Integer)getRows().get(i)).intValue();

            if (y>0) g2.drawLine( 10,  10 + getZoomedDim(y)-1, getZoomedDim(maxWidth)-1+10, 10+ getZoomedDim(y)-1);
        }


        //g2.setColor(new Color(250,250,250));
        //g2.fillRect(10,10, getZoomedDim( ((Integer)getColumns().get( getCrosstabElement().getRowGroups().size() )).intValue())-1,
        //        getZoomedDim( ((Integer)getRows().get( getCrosstabElement().getColumnGroups().size() )).intValue())-1);


        // FIRST DROW THE BACKGROUND FOR EACH CELLS
        for (int i=0; i<getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);

            if (cell.getType() != cell.NODATA_CELL &&  cell.getWidth() > 0 && cell.getHeight() > 0) {
                cell.drawCell( g2, getZoomFactor());
                cell.drawCellBox(g2, getZoomFactor(),
                (cell.getType() == cell.HEADER_CELL  || cell.getType() == cell.CT_HEADER_CELL) && cell.getTop() == ((Integer)rows.get(getCrosstabElement().getColumnGroups().size())).intValue(),
                (cell.getType() == cell.HEADER_CELL  || cell.getType() == cell.CT_HEADER_CELL) && cell.getLeft() == ((Integer)columns.get(getCrosstabElement().getRowGroups().size())).intValue());
            }
        }

        // DRAW ALL ELEMENTS...
        for (int i=0; i< getCrosstabElement().getElements().size(); ++i) {
                ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
                if (re.getCell().getType() != CrosstabCell.NODATA_CELL)
                {
                    re.drawObject(g2,getZoomFactor(),0,0);
                }
        }

        // THEN DRAW THE BOX FOR EACH CELL...
        for (int i=0; i<getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() != cell.NODATA_CELL &&  cell.getWidth() > 0 && cell.getHeight() > 0) {

               // cell.drawCellBox(g2, getZoomFactor());
            }
        }

        if (isDefaultCellMode())
        {
            //g2.setColor(new Color(255,255,255,200));
            //g2.fillRect(10,10, zoomedCrosstabWidth, zoomedCrosstabHeight);

            for (int i=0; i<getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() == cell.NODATA_CELL) {
                cell.drawCell( g2, getZoomFactor());
                cell.drawCellBox(g2, getZoomFactor(), false, false);

                    // DRAW ALL ELEMENTS...
                    for (int j=0; j< getCrosstabElement().getElements().size(); ++j) {
                            ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
                            if (re.getCell() == cell)
                            {
                                re.drawObject(g2,getZoomFactor(),0,0);
                            }
                    }
                }
            }
        }

        // DRAW SELECTION...
        for (int i=0; i< getCrosstabElement().getElements().size(); ++i) {
                ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
                if (getSelectedElements().contains(re))
                {
                    re.drawCorona(g2,getZoomFactor(),0,0, re == getSelectedElements().firstElement() );
                }
        }

    }


    public void paint(Graphics g) {
        if (animation) return;

        Graphics2D g2 = (Graphics2D)g;
        Shape s = g2.getClip();
        AffineTransform at = g2.getTransform();
                
        try {
        super.paint(g);
                
                if (getCrosstabElement() == null) return;

                if (MainFrame.getMainInstance().getProperties().getProperty("Antialias","true").equals("false"))
                {

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
                    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_PURE);
                    g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                            RenderingHints.VALUE_DITHER_DISABLE);
                }
                else
                {

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_DEFAULT);
                    g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                            RenderingHints.VALUE_DITHER_ENABLE);
                }


                    paintDocument(g2);

                    g2.setClip(s);
         
        
        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (s != null && g2 != null)
            {
                g2.setClip(s);
            }
            
            if (at != null && g2 != null)
            {
                g2.setTransform(at);
            }
        }
        
    }

    public CrosstabEditor getEditor() {
        return editor;
    }

    public void setEditor(CrosstabEditor editor) {
        this.editor = editor;
        this.updateGrid();
        this.invalidate();
        this.repaint();
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        updateSize();
        this.repaint();
    }


    /**
     *  Return an int.  It performs zoomFactor*dim and a round.
     */
    public int getZoomedDim(int dim) {
        if (zoomFactor == 1.0 || dim == 0) return dim;
        return (int)((double)dim*zoomFactor);
    }

    /**
     *  Return an int.  It performs zoomFactor*dim and a round.
     */
    public int getRealDim(int dim) {
        if (zoomFactor == 1.0 || dim == 0) return dim;
        return (int)((double)dim/zoomFactor);
    }



    /**
     * This method update the cell matrix according with groups and cells contained in
     * the CrosstabElement...
     **/
    public void updateGrid() {
        if (getCrosstabElement() == null) return;

        try {
            getColumns().clear();
            getRows().clear();
            int current_x = 0;
            int current_y = 0;

            // Adjusting cells dimensions...
            CrosstabCell mainCell = findCell("","");
            for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);

                cell.setParent( this.getCrosstabElement());

                if (cell.getType() == cell.DETAIL_CELL) {
                    if (cell.getHeight() == 0) cell.setHeight( getRowHeight( cell.getRowTotalGroup() ) );
                    if (cell.getWidth() == 0) cell.setWidth( getColumnWidth( cell.getColumnTotalGroup() ) );
                }
            }

            addNotDefinedCells();

            getColumns().add( new Integer(current_x) );

            for (int i=0; i<getCrosstabElement().getRowGroups().size(); ++i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getRowGroups().elementAt(i);
                cg.getHeaderCell().setLeft( current_x );
                cg.getTotalCell().setLeft( current_x );
                current_x = current_x + cg.getSize();
                getColumns().add( new Integer(current_x) );
            }

            ArrayList columnWidths = new ArrayList();
            for (int i=getCrosstabElement().getColumnGroups().size()-1; i>=0; --i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getColumnGroups().elementAt(i);

                if (i == getCrosstabElement().getColumnGroups().size()-1)
                {
                    columnWidths.add( new Integer( getColumnWidth("")));
                }
                if (!cg.getTotalPosition().equals("Start"))
                {
                   columnWidths.add( new Integer( getColumnWidth(cg.getName())));
                }
                else
                {
                    columnWidths.add(0, new Integer( getColumnWidth(cg.getName())));
                }

            }

            for (int i=0; i<columnWidths.size(); ++i)
            {
                current_x += ((Integer)columnWidths.get(i)).intValue();
                getColumns().add( new Integer(current_x));
            }
            if (getCrosstabElement().getColumnGroups().size() == 0) getColumns().add( new Integer(current_x += getColumnWidth("")));

            // Look for all rows...

            getRows().add( new Integer(current_y) );
            for (int i=0; i<getCrosstabElement().getColumnGroups().size(); ++i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getColumnGroups().elementAt(i);
                cg.getHeaderCell().setTop( current_y );
                cg.getTotalCell().setTop( current_y );
                current_y = current_y + cg.getSize();
                getRows().add( new Integer(current_y) );

            }

            ArrayList rowHeights = new ArrayList();
            for (int i=getCrosstabElement().getRowGroups().size()-1; i>=0; --i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getRowGroups().elementAt(i);

                if (i == getCrosstabElement().getRowGroups().size()-1)
                {
                    rowHeights.add( new Integer( getRowHeight("")));
                }
                if (!cg.getTotalPosition().equals("Start"))
                {
                   rowHeights.add( new Integer( getRowHeight(cg.getName())));
                }
                else
                {
                    rowHeights.add(0, new Integer( getRowHeight(cg.getName())));
                }
            }

            for (int i=0; i<rowHeights.size(); ++i)
            {
                current_y += ((Integer)rowHeights.get(i)).intValue();
                getRows().add( new Integer(current_y));
            }
            if (getCrosstabElement().getRowGroups().size() == 0) getRows().add( new Integer(current_y += getRowHeight("")));

            int columnGroups = getCrosstabElement().getColumnGroups().size();
            int rowGroups = getCrosstabElement().getRowGroups().size();




            int currentTopRowNumber = columnGroups;
            int currentBottomRowNumber = columnGroups + rowGroups + 1;

            for (int i=0; i<getCrosstabElement().getRowGroups().size(); ++i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getRowGroups().elementAt(i);

                cg.getHeaderCell().setLeftIndex( i );
                cg.getHeaderCell().setLeft( ((Integer)getColumns().get(i)).intValue() );
                cg.getHeaderCell().setTopIndex( (cg.getTotalPosition().equals("Start")) ? currentTopRowNumber+1  : currentTopRowNumber );
                cg.getHeaderCell().setTop( ((Integer)getRows().get( cg.getHeaderCell().getTopIndex() )).intValue() );
                cg.getHeaderCell().setRightIndex( i+1 );
                cg.getHeaderCell().setWidth( cg.getSize() );
                cg.getHeaderCell().setBottomIndex( (cg.isHasTotal() && cg.getTotalPosition().equals("Start")) ? currentBottomRowNumber  : currentBottomRowNumber - 1 );
                cg.getHeaderCell().setHeight(  ((Integer)getRows().get( cg.getHeaderCell().getBottomIndex() )).intValue() -cg.getHeaderCell().getTop());

                cg.getTotalCell().setLeftIndex( i );
                cg.getTotalCell().setLeft( cg.getHeaderCell().getLeft());
                cg.getTotalCell().setTopIndex( (cg.getTotalPosition().equals("Start")) ? currentTopRowNumber : currentBottomRowNumber-1 );
                cg.getTotalCell().setTop( ((Integer)getRows().get( cg.getTotalCell().getTopIndex() )).intValue() );
                cg.getTotalCell().setRightIndex( rowGroups );
                cg.getTotalCell().setWidth(  ((Integer)getColumns().get( rowGroups)).intValue() - cg.getTotalCell().getLeft() );
                cg.getTotalCell().setBottomIndex( (cg.isHasTotal() && cg.getTotalPosition().equals("Start")) ? currentTopRowNumber +1 : currentBottomRowNumber );
                cg.getTotalCell().setHeight( ((Integer)getRows().get( cg.getTotalCell().getBottomIndex() )).intValue() - cg.getTotalCell().getTop() );

                if (cg.getTotalPosition().equals("Start")) currentTopRowNumber++;
                else currentBottomRowNumber--;

                // Update all cells with rowTotalGroup this group
                for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                    CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);

                    if (cell.getRowTotalGroup().equals(cg.getName())) {

                        cell.setTop(cg.getTotalCell().getTop());
                        cell.setHeight( cg.getTotalCell().getHeight());
                    }
                }
            }

            int currentLeftColumnNumber = rowGroups;
            int currentRightColumnNumber = columnGroups + rowGroups + 1;
            for (int i=0; i<getCrosstabElement().getColumnGroups().size(); ++i) {
                CrosstabGroup cg = (CrosstabGroup)getCrosstabElement().getColumnGroups().elementAt(i);

                // Count preceding total rows...

                cg.getHeaderCell().setLeftIndex( (cg.getTotalPosition().equals("Start")) ? currentLeftColumnNumber+1  : currentLeftColumnNumber );
                cg.getHeaderCell().setLeft( ((Integer)getColumns().get( cg.getHeaderCell().getLeftIndex())).intValue() );
                cg.getHeaderCell().setTopIndex( i );
                cg.getHeaderCell().setTop( ((Integer)getRows().get( i )).intValue() );
                cg.getHeaderCell().setRightIndex((cg.isHasTotal() && cg.getTotalPosition().equals("Start")) ? currentRightColumnNumber  : currentRightColumnNumber - 1);
                cg.getHeaderCell().setWidth( ((Integer)getColumns().get(cg.getHeaderCell().getRightIndex()) ).intValue() -cg.getHeaderCell().getLeft());
                cg.getHeaderCell().setBottomIndex( i+1 );
                cg.getHeaderCell().setHeight( cg.getSize());

                cg.getTotalCell().setLeftIndex( (cg.getTotalPosition().equals("Start")) ? currentLeftColumnNumber  : currentRightColumnNumber-1 );
                cg.getTotalCell().setLeft( ((Integer)getColumns().get(  cg.getTotalCell().getLeftIndex() )).intValue() );
                cg.getTotalCell().setTopIndex( i );
                cg.getTotalCell().setTop( ((Integer)getRows().get( i)).intValue() );
                cg.getTotalCell().setRightIndex((cg.isHasTotal() && cg.getTotalPosition().equals("Start")) ? currentLeftColumnNumber +1 : currentRightColumnNumber);
                cg.getTotalCell().setWidth( ((Integer)getColumns().get( cg.getTotalCell().getRightIndex() )).intValue() - cg.getTotalCell().getLeft() );
                cg.getTotalCell().setBottomIndex( columnGroups );
                cg.getTotalCell().setHeight( ((Integer)getRows().get( columnGroups)).intValue() - cg.getTotalCell().getTop() );

                if (cg.getTotalPosition().equals("Start")) currentLeftColumnNumber++;
                else currentRightColumnNumber--;

                for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                    CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);

                    if (cell.getColumnTotalGroup().equals(cg.getName())) {
                        cell.setLeft(cg.getTotalCell().getLeft());
                        cell.setWidth( cg.getTotalCell().getWidth());
                    }
                }

            }

            // Update coordinates for the A0 cell

            if (getCrosstabElement().getRowGroups().size() > 0)
            {
                mainCell.setTopIndex(((CrosstabGroup)getCrosstabElement().getRowGroups().lastElement()).getHeaderCell().getTopIndex() );
                mainCell.setTop(((CrosstabGroup)getCrosstabElement().getRowGroups().lastElement()).getHeaderCell().getTop());
            }
            else
            {
                mainCell.setTop(getCrosstabElement().getColumnGroups().size());
                mainCell.setTop(((Integer)getRows().get(getCrosstabElement().getColumnGroups().size())).intValue() );
            }
            mainCell.setBottomIndex(mainCell.getTopIndex() + 1);

            if (getCrosstabElement().getColumnGroups().size() > 0)
            {
                mainCell.setLeftIndex(((CrosstabGroup)getCrosstabElement().getColumnGroups().lastElement()).getHeaderCell().getLeftIndex() );
                mainCell.setLeft(((CrosstabGroup)getCrosstabElement().getColumnGroups().lastElement()).getHeaderCell().getLeft());
            }
            else
            {
                mainCell.setLeftIndex(getCrosstabElement().getRowGroups().size());
                mainCell.setLeft(((Integer)getColumns().get(getCrosstabElement().getRowGroups().size())).intValue());
            }
            mainCell.setRightIndex(mainCell.getLeftIndex() + 1);


            for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);

                if (cell.getType() == cell.DETAIL_CELL)
                {
                    if (cell.getRowTotalGroup().equals("")) {

                        cell.setTop(mainCell.getTop());
                        cell.setTopIndex(mainCell.getTopIndex());
                        cell.setBottomIndex(mainCell.getBottomIndex());
                    }
                    if (cell.getColumnTotalGroup().equals("")) {

                        cell.setLeft(mainCell.getLeft());
                        cell.setLeftIndex(mainCell.getLeftIndex());
                        cell.setRightIndex(mainCell.getRightIndex());
                    }

                    cell.setTopIndex( getTotalRowTopIndex(cell.getRowTotalGroup()) );
                    cell.setBottomIndex( cell.getTopIndex() +1);

                    cell.setLeftIndex( getTotalColumnLeftIndex(cell.getColumnTotalGroup()) );
                    cell.setRightIndex( cell.getLeftIndex() +1);
                }
            }

            // adding DEFAULT NO DATA CELL....
            CrosstabCell detailCell = findCell( "", "");
            boolean found = false;
            for (int i=0; i<getCrosstabElement().getCells().size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);
                if (cell.getType() == cell.NODATA_CELL)
                {
                    cell.setTop( 0 );
                    cell.setLeft( 0 );
                    cell.setWidth( this.getCrosstabElement().getWidth() );
                    cell.setHeight( this.getCrosstabElement().getHeight());
                    cell.setTopIndex( 0 );
                    cell.setLeftIndex( 0 );
                    cell.setBottomIndex( 0 );
                    cell.setRightIndex( 0 );
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                CrosstabCell defaultWhenNoDataCell= detailCell.cloneMe();
                defaultWhenNoDataCell.setType( defaultWhenNoDataCell.NODATA_CELL);
                defaultWhenNoDataCell.setParent( this.getCrosstabElement());
                defaultWhenNoDataCell.setName(null);
                defaultWhenNoDataCell.setTop( 0 );
                defaultWhenNoDataCell.setLeft( 0 );
                defaultWhenNoDataCell.setWidth( this.getCrosstabElement().getWidth() );
                defaultWhenNoDataCell.setHeight( this.getCrosstabElement().getHeight());
                defaultWhenNoDataCell.setTopIndex( 0 );
                defaultWhenNoDataCell.setLeftIndex( 0 );
                defaultWhenNoDataCell.setBottomIndex( 0 );
                defaultWhenNoDataCell.setRightIndex( 0 );
                getCrosstabElement().getCells().add(defaultWhenNoDataCell);
            }

            found = false;

            // adding DEFAULT HEADER CELL....
            for (int i=0; i<getCrosstabElement().getCells().size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);
                if (cell.getType() == cell.CT_HEADER_CELL)
                {
                    cell.setTop( 0 );
                    cell.setTopIndex(  0 );
                    cell.setLeft( 0 );
                    cell.setLeftIndex( 0 );
                    cell.setRightIndex(  getCrosstabElement().getRowGroups().size() );
                    cell.setWidth( ((Integer)getColumns().get( cell.getRightIndex() )).intValue()  );
                    cell.setBottomIndex( getCrosstabElement().getColumnGroups().size() );
                    cell.setHeight( ((Integer)getRows().get( cell.getBottomIndex() )).intValue() );
                    found = true;
                    break;
                }
            }

            if (!found)
            {

                CrosstabCell crossTabHeaderCell= new CrosstabCell();
                crossTabHeaderCell.setType( crossTabHeaderCell.CT_HEADER_CELL);
                crossTabHeaderCell.setParent( this.getCrosstabElement());
                crossTabHeaderCell.setName(null);
                crossTabHeaderCell.setTop( 0 );
                crossTabHeaderCell.setTopIndex(  0 );
                crossTabHeaderCell.setLeft( 0 );
                crossTabHeaderCell.setLeftIndex( 0 );
                crossTabHeaderCell.setRightIndex(  getCrosstabElement().getRowGroups().size() );
                crossTabHeaderCell.setWidth( ((Integer)getColumns().get( crossTabHeaderCell.getRightIndex() )).intValue()  );
                crossTabHeaderCell.setBottomIndex( getCrosstabElement().getColumnGroups().size() );
                crossTabHeaderCell.setHeight( ((Integer)getRows().get( crossTabHeaderCell.getBottomIndex() )).intValue() );
                getCrosstabElement().getCells().add(crossTabHeaderCell);
            }

            getRowBands().clear();

            for (int i=1; i<getRows().size(); ++i)
            {
                Vector rowBandContents = new Vector();
                for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                    CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);
                    if (cell.getBottomIndex() == i)
                    {
                        rowBandContents.add(cell);
                    }
                }
                getRowBands().add(rowBandContents);
            }

            getColumnBands().clear();

            for (int i=1; i<getColumns().size(); ++i)
            {
                Vector columnBandContents = new Vector();
                for (int k=0; k<getCrosstabElement().getCells().size(); ++k) {
                    CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(k);
                    if (cell.getRightIndex() == i)
                    {
                        columnBandContents.add(cell);
                    }
                }
                getColumnBands().add(columnBandContents);
            }



            // Update  all elements positions...
            for (int i=0; i< getCrosstabElement().getElements().size(); ++i) {
                ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);

                re.getPosition().x = re.getRelativePosition().x + re.getCell().getLeft()+10;
                re.getPosition().y = re.getRelativePosition().y + re.getCell().getTop()+10;

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        updateSize();

    }

    public CrosstabCell findCell(String rowGroup, String columnGroup) {
        if (getCrosstabElement() == null) return null;
        for (int i=0; i< getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell) getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() == cell.DETAIL_CELL &&
                    cell.getRowTotalGroup().equals(rowGroup) &&
                    cell.getColumnTotalGroup().equals(columnGroup)) {
                return cell;
            }
        }
        return null;
    }


    /**
     * Return the maximum width of a certain column. The column "" is the A0 cell width
     */
    public int getColumnWidth(String columnTotalGroup) {
        int tmpWidth =-1;
        for (int i=0; i< getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell) getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() == cell.DETAIL_CELL &&
                    cell.getColumnTotalGroup().equals(columnTotalGroup) &&
                    cell.getWidth() > tmpWidth) {
                tmpWidth = cell.getWidth();
            }
        }

        if (tmpWidth < 0)
        {
            return getColumnWidth("");
        }

        return tmpWidth;
    }

    /**
     * Return the maximum height of a certain row. The row "" is the A0 cell row
     */
    public int getRowHeight(String rowTotalGroup) {
        int tmpHeight = -1;
        for (int i=0; i< getCrosstabElement().getCells().size(); ++i) {
            CrosstabCell cell = (CrosstabCell) getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() == cell.DETAIL_CELL &&
                    cell.getRowTotalGroup().equals(rowTotalGroup) &&
                    cell.getHeight() > tmpHeight) {
                tmpHeight = cell.getHeight();
            }
        }

        if (tmpHeight < 0 && !rowTotalGroup.equals(""))
        {
            return getRowHeight("");
        }
        else if (tmpHeight < 0 && rowTotalGroup.equals(""))
        {
            return 0;
        }

        return tmpHeight;
    }

    /**
     * Return the maximum height of a certain row. The row "" is the A0 cell row
     */
    public int getTotalRowTopIndex(String rowTotalGroup) {
        for (int i=0; i<getCrosstabElement().getRowGroups().size(); ++i)
        {
            CrosstabGroup group = (CrosstabGroup)getCrosstabElement().getRowGroups().elementAt(i);
            if (group.getName().equals(rowTotalGroup))
            {
                return group.getTotalCell().getTopIndex();
            }
        }
        return findCell("","").getTopIndex();
    }

    /**
     * Return the maximum height of a certain row. The row "" is the A0 cell row
     */
    public int getTotalColumnLeftIndex(String columnTotalGroup) {
        for (int i=0; i<getCrosstabElement().getColumnGroups().size(); ++i)
        {
            CrosstabGroup group = (CrosstabGroup)getCrosstabElement().getColumnGroups().elementAt(i);
            if (group.getName().equals(columnTotalGroup))
            {
                return group.getTotalCell().getLeftIndex();
            }
        }
        return findCell("","").getLeftIndex();
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPopupMenuCrosstabReporteElement = new javax.swing.JPopupMenu();
        jMenuItemElementProperties = new javax.swing.JMenuItem();
        jMenuItemCrosstabProperties = new javax.swing.JMenuItem();
        jMenuItemCellProperties = new javax.swing.JMenuItem();
        jCheckBoxMenuItemDefaultCellEdit = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemCopyStyle = new javax.swing.JMenuItem();
        jMenuItemPasteStyle = new javax.swing.JMenuItem();
        jMenuItemTransformStaticText = new javax.swing.JMenuItem();
        jMenuItemPattern = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jPopupMenuCrosstab = new javax.swing.JPopupMenu();
        jMenuItemCrosstabProperties1 = new javax.swing.JMenuItem();
        jMenuItemCellProperties1 = new javax.swing.JMenuItem();
        jCheckBoxMenuItemDefaultCellEdit1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItemPaste1 = new javax.swing.JMenuItem();

        jMenuItemElementProperties.setText(it.businesslogic.ireport.util.I18n.getString("elementProperties","Element properties"));
        jMenuItemElementProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemElementProperties);

        jMenuItemCrosstabProperties.setText("Crosstab properties");
        jMenuItemCrosstabProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCrosstabPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemCrosstabProperties);

        jMenuItemCellProperties.setText("Cell properties");
        jMenuItemCellProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCellPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemCellProperties);

        jCheckBoxMenuItemDefaultCellEdit.setText("Edit When-No-Data default cell");
        jCheckBoxMenuItemDefaultCellEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemDefaultCellEditActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jCheckBoxMenuItemDefaultCellEdit);

        jPopupMenuCrosstabReporteElement.add(jSeparator1);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png")));
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setEnabled(false);
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemCut);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png")));
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setEnabled(false);
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemCopy);

        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png")));
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setEnabled(false);
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemPaste);

        jMenuItemDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png")));
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.setEnabled(false);
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemDelete);

        jPopupMenuCrosstabReporteElement.add(jSeparator3);

        jMenuItemCopyStyle.setEnabled(false);
        jMenuItemCopyStyle.setLabel("Copy style");
        jMenuItemCopyStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyStyleActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemCopyStyle);

        jMenuItemPasteStyle.setEnabled(false);
        jMenuItemPasteStyle.setLabel("Paste style");
        jMenuItemPasteStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteStyleActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemPasteStyle);

        jMenuItemTransformStaticText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItemTransformStaticText.setLabel("Transform in Textfield");
        jMenuItemTransformStaticText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTransformStaticTextActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemTransformStaticText);

        jMenuItemPattern.setText("Field pattern");
        jMenuItemPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPatternActionPerformed(evt);
            }
        });

        jPopupMenuCrosstabReporteElement.add(jMenuItemPattern);

        jPopupMenuCrosstabReporteElement.add(jSeparator4);

        jMenuItemCrosstabProperties1.setText("Crosstab properties");
        jMenuItemCrosstabProperties1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCrosstabPropertiesActionPerformed1(evt);
            }
        });

        jPopupMenuCrosstab.add(jMenuItemCrosstabProperties1);

        jMenuItemCellProperties1.setText("Cell properties");
        jMenuItemCellProperties1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCellPropertiesActionPerformed1(evt);
            }
        });

        jPopupMenuCrosstab.add(jMenuItemCellProperties1);

        jCheckBoxMenuItemDefaultCellEdit1.setText("Edit When-No-Data default cell");
        jCheckBoxMenuItemDefaultCellEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemDefaultCellEdit1ActionPerformed(evt);
            }
        });

        jPopupMenuCrosstab.add(jCheckBoxMenuItemDefaultCellEdit1);

        jMenuItemPaste1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png")));
        jMenuItemPaste1.setText("Paste");
        jMenuItemPaste1.setEnabled(false);
        jMenuItemPaste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed1(evt);
            }
        });

        jPopupMenuCrosstab.add(jMenuItemPaste1);

        setLayout(null);

        setBackground(new java.awt.Color(204, 204, 204));
        setFocusCycleRoot(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxMenuItemDefaultCellEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemDefaultCellEdit1ActionPerformed
        setDefaultCellMode(jCheckBoxMenuItemDefaultCellEdit1.isSelected());
    }//GEN-LAST:event_jCheckBoxMenuItemDefaultCellEdit1ActionPerformed

    private void jCheckBoxMenuItemDefaultCellEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemDefaultCellEditActionPerformed

        setDefaultCellMode(jCheckBoxMenuItemDefaultCellEdit.isSelected());

    }//GEN-LAST:event_jCheckBoxMenuItemDefaultCellEditActionPerformed

    private void jMenuItemPasteActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed1

        getParentReportFrame().paste();

    }//GEN-LAST:event_jMenuItemPasteActionPerformed1

    private void jMenuItemCellPropertiesActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCellPropertiesActionPerformed1
    jMenuItemCellPropertiesActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemCellPropertiesActionPerformed1

    private void jMenuItemCrosstabPropertiesActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCrosstabPropertiesActionPerformed1
    jMenuItemCrosstabPropertiesActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemCrosstabPropertiesActionPerformed1

    private void jMenuItemPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPatternActionPerformed
        String pattern = "";
        FieldPatternDialog pd = new FieldPatternDialog( MainFrame.getMainInstance() ,true);
        pd.setVisible(true);
        if (pd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION ) {


            Enumeration selectedElementEnum = getSelectedElements().elements();

            while (selectedElementEnum.hasMoreElements()) {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                if (re instanceof TextFieldReportElement) {
                    ((TextFieldReportElement)re).setPattern( pd.getPattern() );
                }
            }
        }
    }//GEN-LAST:event_jMenuItemPatternActionPerformed

    private void jMenuItemTransformStaticTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTransformStaticTextActionPerformed
        transformStaticInTextFields();
    }//GEN-LAST:event_jMenuItemTransformStaticTextActionPerformed

    private void jMenuItemPasteStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteStyleActionPerformed
        pasteStyle();
    }//GEN-LAST:event_jMenuItemPasteStyleActionPerformed

    private void jMenuItemCopyStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyStyleActionPerformed
        copyStyle();
    }//GEN-LAST:event_jMenuItemCopyStyleActionPerformed

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        deleteSelectedElements();
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        getParentReportFrame().paste();
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        getParentReportFrame().copy();
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
        getParentReportFrame().cut();
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jMenuItemElementPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementPropertiesActionPerformed
        this.getParentReportFrame().openElementPropertiesDialog();
    }//GEN-LAST:event_jMenuItemElementPropertiesActionPerformed

    private void jMenuItemCellPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCellPropertiesActionPerformed

        if (selectedCell != null)
        {
            editCell(selectedCell);
        }

    }//GEN-LAST:event_jMenuItemCellPropertiesActionPerformed


    public void editCell(CrosstabCell editCell)
    {
        CrosstabCellDialog ctd = new CrosstabCellDialog(MainFrame.getMainInstance(), true);
            ctd.setTmpCell( editCell );
            ctd.setVisible(true);
            if (ctd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
            {
                editCell.setBackcolor( ctd.getTmpCell().getBackcolor() );
                editCell.setBox( ctd.getTmpCell().getBox() );
                editCell.setMode( ctd.getTmpCell().getMode() );
                editCell.setStyle( ctd.getTmpCell().getStyle() );

                if (editCell.getWidth() != ctd.getTmpCell().getWidth())
                {
                    // Modify all with for cell with this column index,
                    // Modify all left coords for elements of cells width index > of this column index
                    // Modify all cell left

                    int columnIndex = editCell.getLeftIndex();
                    int delta = ctd.getTmpCell().getWidth() - editCell.getWidth();
                    for (int i=0; i< this.getCrosstabElement().getCells().size(); ++i)
                    {
                        CrosstabCell cell = (CrosstabCell)this.getCrosstabElement().getCells().elementAt(i);
                        if (cell.getLeftIndex() == columnIndex  || cell.getRightIndex() == columnIndex + 1)
                        {
                            cell.setWidth(cell.getWidth() + delta);
                        }
                        if (cell.getLeftIndex() > columnIndex)
                        {
                            cell.setLeft(cell.getLeft() + delta );
                            for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
                            {
                                   ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
                                   if (re.getCell() == cell)
                                   {
                                      re.trasform(new Point(delta,0), TransformationType.TRANSFORMATION_MOVE);
                                   }
                            }
                        }
                    }

                    for (int i=columnIndex+1; i< columns.size(); ++i)
                    {
                        int columnPosition = ((Integer)columns.get(i)).intValue();
                        columnPosition = columnPosition += delta;
                        columns.set(i,new Integer(columnPosition));
                    }
                }

                if (editCell.getHeight() != ctd.getTmpCell().getHeight())
                {
                    // Modify all with for cell with this column index,
                    // Modify all left coords for elements of cells width index > of this column index
                    // Modify all cell left

                    int rowIndex = editCell.getTopIndex();
                    int delta = ctd.getTmpCell().getHeight() - editCell.getHeight();
                    for (int i=0; i< this.getCrosstabElement().getCells().size(); ++i)
                    {
                        CrosstabCell cell = (CrosstabCell)this.getCrosstabElement().getCells().elementAt(i);

                        if (cell.getTopIndex() == rowIndex || cell.getBottomIndex() == rowIndex + 1)
                        {
                            cell.setHeight(cell.getHeight() + delta);
                        }
                        else if (cell.getTopIndex() > rowIndex)
                        {
                            cell.setTop(cell.getTop() + delta );
                            for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
                            {
                                   ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
                                   if (re.getCell() == cell)
                                   {
                                      re.trasform(new Point(0,delta), TransformationType.TRANSFORMATION_MOVE);
                                   }
                            }
                        }
                    }

                    for (int i=rowIndex+1; i< rows.size(); ++i)
                    {
                        int rowPosition = ((Integer)rows.get(i)).intValue();
                        rowPosition = rowPosition += delta;
                        rows.set(i,new Integer(rowPosition));
                    }
                }

                this.repaint();
                this.getCrosstabElement().notifyChange();
            }
    }

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped


    public void panelKeyPressed(java.awt.event.KeyEvent evt)
    {
        formKeyPressed(evt);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed



        if (this.getSelectedElements().size() > 0) evt.consume();
        // Standard speed:
        int x=1, y=1;

        // Increase the speed of movement if the Shift key is pressed
        if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0) {
            x = 10;
            y = 10;
        }

        //if ((evt.getModifiers() & evt.ALT_MASK) != 0)
        //    System.out.println("Tasto:"+evt.getKeyCode());
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
            this.deleteSelectedElements();
            return;
        }
        /* Begin code Robert Lamping, 12 July 2004 */
        /* Short cut keys for quick alignment */
        else  if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) &&
                (( evt.getModifiers() & evt.CTRL_MASK) != 0) ) {

            /* all key presses when SHIFT/CTRL are both pressed */

            /* Short cuts for alignments */
            /* most of them deactivated, since they are activated bia the menu options and their
             * short cuts
             */
            switch ( evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_S:
                    /* shrink A3-A4 */
                    FormatCommand.getCommand(OperationType.SHRINK).execute();
                    break;
            }

        }
        /* End code Robert Lamping, 12 July 2004 */

        else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
            y *= -1;
            x = 0;

            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustCell( getCrosstabElement().getCells() );
                changed_elements.add(element);
            }
            getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), changed_elements , ReportElementChangedEvent.CHANGED));

            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            this.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT) {
            x *= -1;
            y = 0;

            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustCell(getCrosstabElement().getCells());
                changed_elements.add(element);
            }
            getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            this.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT) {
            y=0;
            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustCell(getCrosstabElement().getCells());
                changed_elements.add(element);
            }
            getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();

            this.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
            x = 0;
            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustCell(getCrosstabElement().getCells());
                changed_elements.add(element);
            }
            getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            this.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F2) {
            // Edit static Text

            DeleteElementsOperation undoOp = new DeleteElementsOperation(getParentReportFrame(), getCrosstabElement());
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement re = (ReportElement) i.next();
                if(re instanceof TextReportElement){
                    String te = ((TextReportElement)re).getText();
                    String result = javax.swing.JOptionPane.showInputDialog(this, I18n.getString("messages.enterNewValue","Please enter a new Value"), te);
                    if(result != null && !result.equals("")){
                        ((TextReportElement)re).setText(result);
                        undoOp.addElement(  re, getCrosstabElement().getElements().indexOf(re));
                        getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), re , ReportElementChangedEvent.CHANGED));
                    }
                }
            }
            getParentReportFrame().addUndoOperation(undoOp);
            getMainFrame().getElementPropertiesDialog().updateSelection();
            this.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F3) {

            getParentReportFrame().transformStaticInTextFields();
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
        }
        /* Begin code by Robert Lamping, 12 july 2004 */
        /* Copy and paste via CTRL/INS and SHIFT insert */
        else if ( (( evt.getModifiers() & evt.CTRL_MASK) != 0 ) && evt.getKeyCode() == java.awt.event.KeyEvent.VK_INSERT) {
            getParentReportFrame().copy();
        } else if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) && evt.getKeyCode() == java.awt.event.KeyEvent.VK_INSERT) {
            getParentReportFrame().paste();
        }
        /* End code Robert Lamping, 12 July 2004 */



    }//GEN-LAST:event_formKeyPressed

    private void jMenuItemCrosstabPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCrosstabPropertiesActionPerformed


        it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
        cpd.setCurrentCrosstabReportElement( getCrosstabElement());
        cpd.setVisible(true);

    }//GEN-LAST:event_jMenuItemCrosstabPropertiesActionPerformed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

        int mouseX = (int)evt.getPoint().getX();
        int mouseY = (int)evt.getPoint().getY();

        if (drag_selection_mode && evt.getButton() == evt.BUTTON1) {

            drag_selection_mode = false;
            Graphics2D gg = (Graphics2D)this.getGraphics();
            gg.setXORMode(Color.GREEN);

            Stroke s = gg.getStroke();
            gg.setStroke(selectionStroke);

            if (!first_draw_selection_rect) {
                gg.drawRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                        (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                        (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
                        (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            }

            gg.setPaintMode();
            if (s != null) {
                gg.setStroke(s);
            }
            drag_selection_mode = false;
            first_draw_selection_rect = true;

            if (( evt.getModifiers() & evt.SHIFT_MASK ) == 0){
                setSelectedElement(null);
            }

            int delta_w = evt.getX()- drag_selection_origin.x;
            int delta_h = evt.getY()- drag_selection_origin.y;
            int delta_x = (delta_w < 0) ? delta_w: 0;
            int delta_y = (delta_h < 0) ? delta_h : 0;

            int originX = getRealDim(  Math.min( drag_selection_origin.x,drag_selection_end.x)-10)+10;
            int originY = getRealDim(  Math.min( drag_selection_origin.y, drag_selection_end.y)-10)+10;
            int width  =  getRealDim(  Math.abs( delta_w ) );
            int height =  getRealDim(  Math.abs( delta_h ) );

            // We need logical coordinates...
            java.awt.Rectangle rect = new java.awt.Rectangle( originX, originY, width, height );
            boolean selection_changed = false;
            for (Iterator i = getCrosstabElement().getElements().iterator(); i.hasNext(); ) {
                ReportElement re = (ReportElement) i.next();
                if ( re.intersects( rect ) ) {
                    if (!selection_changed) selection_changed = true;
                    addSelectedElement(re,false);
                }
            }
            if (selection_changed) {
                fireSelectionChangedEvent();
            }

        } else if (evt.getButton() == evt.BUTTON1 &&  cell_dragging && readyToDragCellVertically>0) {
            Graphics gg = this.getGraphics();
            gg.setXORMode(Color.WHITE);
            gg.drawLine( 0, mouse.y , this.getWidth(), mouse.y);
            gg.setPaintMode();

            // Calculate DELTA...
            int oldPosition = ((Integer)getRows().get(readyToDragCellVertically)).intValue();
            int delta = getRealDim( mouseY -10 - getZoomedDim(oldPosition) );

            if (oldPosition + delta <= ((Integer)getRows().get(readyToDragCellVertically-1)).intValue())
            {
               delta = ((Integer)getRows().get(readyToDragCellVertically-1)).intValue() - oldPosition;
            }

            // Max height = Document height - Crosstab position...
            int maxHeight = getParentReportFrame().getReport().getHeight() - getCrosstabElement().getPosition().y;

            if ( ((Integer)getRows().get( getRows().size()-1)).intValue() + delta > maxHeight)
            {
                delta = maxHeight-((Integer)getRows().get( getRows().size()-1)).intValue();
            }
            if (delta == 0) return;

            int newPosition = ((Integer)getRows().get(readyToDragCellVertically)).intValue() + delta;

            // Save relative position for all elements...
            for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
            {
                ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
                re.setRelativePosition(new Point( re.getPosition().x - re.getCell().getLeft() - 10, re.getPosition().y - re.getCell().getTop() - 10 ));
            }

            if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0)
            {
                // 1. Perform a reverse search for readyToDragCellVertically


                for (int i=getRows().size()-1; i>0; --i)
                {
                    int position = ((Integer)getRows().get(i)).intValue();
                    if (oldPosition == position)
                    {
                        readyToDragCellVertically = i;
                        break;
                    }
                }
            }

            // Modify hight of all cell in band readyToDragCellVertically...
            Vector cells = (Vector)getRowBands().elementAt(readyToDragCellVertically-1);
            for (int i=0; i<cells.size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
                cell.setHeight(cell.getHeight() + delta );
            }
            for (int j=readyToDragCellVertically; j<getRowBands().size(); ++j)
            {
                cells = (Vector)getRowBands().elementAt(j);
                for (int i=0; i<cells.size(); ++i)
                {
                    CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
                    if (cell.getTopIndex() >= readyToDragCellVertically)
                    {
                        cell.setTop(cell.getTop() + delta );
                    }
                    else
                    {
                        cell.setHeight(cell.getHeight() + delta );
                    }
                }
            }

            // Adjust size value of all groups...
            for (int i=0; i< getCrosstabElement().getColumnGroups().size(); ++i)
            {
                CrosstabGroup group = (CrosstabGroup)getCrosstabElement().getColumnGroups().elementAt(i);
                group.setSize( group.getHeaderCell().getHeight() );
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


            for (int i=readyToDragCellVertically; i<getRows().size(); ++i)
            {
                int rowPosition = ((Integer)getRows().get(i)).intValue() + delta;
                getRows().set(i, new Integer(rowPosition));
            }

            //

            cell_dragging = false;
            updateSize();
            repaint();
            getParentReportFrame().addUndoOperation(new CrosstabRowDraggedOperation(this,getCrosstabElement(),readyToDragCellVertically,delta));
        }
        else if (evt.getButton() == evt.BUTTON1 && cell_dragging && readyToDragCellHorizontally > 0) {
            Graphics gg = this.getGraphics();
            gg.setXORMode(Color.WHITE);
            gg.drawLine( mouse.x, 0, mouse.x, this.getHeight());
            gg.setPaintMode();

            // Calculate DELTA...
            int oldPosition = ((Integer)getColumns().get(readyToDragCellHorizontally)).intValue();
            int delta = getRealDim( mouseX -10 - getZoomedDim(oldPosition) );

            if (oldPosition + delta <= ((Integer)getColumns().get(readyToDragCellHorizontally-1)).intValue())
            {
               delta = ((Integer)getColumns().get(readyToDragCellHorizontally-1)).intValue() - oldPosition;
            }

            // Max height = Document height - Crosstab position...
            int maxWidth = getParentReportFrame().getReport().getWidth() - getCrosstabElement().getPosition().x;

            if ( ((Integer)getColumns().get( getColumns().size()-1)).intValue() + delta > maxWidth)
            {
                delta = maxWidth-((Integer)getColumns().get( getColumns().size()-1)).intValue();
            }
            if (delta == 0) return;

            int newPosition = ((Integer)getColumns().get(readyToDragCellHorizontally)).intValue() + delta;

            // Save relative position for all elements...
            for (int j=0; j<getCrosstabElement().getElements().size(); ++j)
            {
                ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(j);
                re.setRelativePosition(new Point( re.getPosition().x - re.getCell().getLeft() - 10, re.getPosition().y - re.getCell().getTop() - 10 ));
            }

            if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0)
            {
                // 1. Perform a reverse search for readyToDragCellVertically


                for (int i=getColumns().size()-1; i>0; --i)
                {
                    int position = ((Integer)getColumns().get(i)).intValue();
                    if (oldPosition == position)
                    {
                        readyToDragCellHorizontally = i;
                        break;
                    }
                }
            }

            // Modify hight of all cell in band readyToDragCellVertically...
            Vector cells = (Vector)getColumnBands().elementAt(readyToDragCellHorizontally-1);
            for (int i=0; i<cells.size(); ++i)
            {
                CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
                cell.setWidth(cell.getWidth() + delta );
            }
            for (int j=readyToDragCellHorizontally; j<getRowBands().size(); ++j)
            {
                cells = (Vector)getColumnBands().elementAt(j);
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


            for (int i=readyToDragCellHorizontally; i<getColumns().size(); ++i)
            {
                int rowPosition = ((Integer)getColumns().get(i)).intValue() + delta;
                getColumns().set(i, new Integer(rowPosition));
            }

            cell_dragging = false;
            updateSize();
            repaint();
            getParentReportFrame().addUndoOperation(new CrosstabColumnDraggedOperation(this,getCrosstabElement(),readyToDragCellHorizontally,delta));
        }
        else if (evt.getButton() ==  evt.BUTTON1)
        {
            if (newObjectType != ReportElementType.NONE) {
                // The second point was clicked

                firstXORDraw = true;

                // Find the band to associate to the new element...

                int delta_w = evt.getX()-newObjectOrigin.x; //gridMultiple(evt.getX()-newObjectOrigin.x);
                int delta_h = evt.getY()-newObjectOrigin.y; //gridMultiple(evt.getY()-newObjectOrigin.y);
                int delta_x = (delta_w < 0) ? delta_w : 0;
                int delta_y = (delta_h < 0) ? delta_h : 0;

                int originX = getRealDim( Math.min(newObjectOrigin.x-10,
                        newObjectOrigin.x+delta_x-10))+10;
                int originY = getRealDim( Math.min(newObjectOrigin.y-10,
                        newObjectOrigin.y+delta_y-10))+10;
                int width = getRealDim( Math.abs(delta_w) );
                int height = getRealDim( Math.abs(delta_h) );


                if (newObjectType == ReportElementType.LINE_ELEMENT) {
                    /* When shift button is pressed too, then there will be no align ment on the grid */
                    if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                        Point straight = straighten( delta_w, delta_h);
                        delta_w = straight.x;
                        delta_h = straight.y;
                    }
                }

                CrosstabCell bname = getCellByPoint( new Point( getRealDim(evt.getX()-10), getRealDim( evt.getY()-10)));

                if (bname != null)
                {
                    ReportElement re = ReportElementFactory.create( newObjectType, originX, originY, getRealDim(delta_w), getRealDim(delta_h));

                    if (getParentReportFrame().getReport().getStyles().size() > 0)
                    {
                        for (int i=0; i<getParentReportFrame().getReport().getStyles().size(); ++i )
                        {
                           Style s = (Style)getParentReportFrame().getReport().getStyles().elementAt(i);

                           if (s.getAttributeBoolean(s.ATTRIBUTE_isDefault,false) == true)
                           {
                               re.setStyle( s );
                           }
                        }
                    }
                    /* Begin Code Robert Lamping, 13 july 2004 */
                    // Element does not know about other elements, the frame does, so here we reset the key.
                    // barcode becomes barcode-1, or higher depending on what the highest sub number is.
                    // Same for graphcs image, or whatever initial name
                    re.setKey( getParentReportFrame().getNextElementKey( re.getKey()) ) ;

                    /* End Code Robert Lamping, 13 july 2004 */

                    re.setCell( bname );
                    re.setRelativePosition( new Point(re.getPosition().x - re.getCell().getLeft() - 10,  re.getPosition().y - re.getCell().getTop() - 10));
                    for (int elnum = getCrosstabElement().getElements().size()-1; elnum >= 0; --elnum)
                    {
                        ReportElement container_re = (ReportElement)getCrosstabElement().getElements().elementAt(elnum);
                        if (container_re instanceof FrameReportElement && container_re.getCell() == re.getCell())
                        {
                            if ( container_re.getBounds().contains( re.getBounds()))
                            {
                                re.setParentElement( container_re);
                                break;
                            }
                        }
                    }


                    getCrosstabElement().getElements().addElement(re);

                    getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), re , ReportElementChangedEvent.ADDED));

                    getParentReportFrame().addUndoOperation( new  InsertElementOperation( getParentReportFrame(), getCrosstabElement(), re ) );
                    setSelectedElement(re);

                }
                else
                {
                    setSelectedElement(null);
                }
                newObjectOrigin = null;

                newObjectType = ReportElementType.NONE;

                this.setCursor( Cursor.getDefaultCursor());
                getMainFrame().setActiveTool(0);


            }
            else if (trasforming) {
                trasforming = false;

                this.setCursor( Cursor.getDefaultCursor());

                if (transformation_type != TransformationType.TRANSFORMATION_MOVE || resistenceExceeded == true) {
                    Point p = new Point(transformation_origin_end);
                    p.x = getRealDim(p.x);
                    p.y = getRealDim(p.y);
                    p.x -= getRealDim(transformation_origin.x);
                    p.y -= getRealDim(transformation_origin.y);

                    Enumeration e = getSelectedElements().elements();
                    TransformElementsOperation undoOp = new TransformElementsOperation(getMainFrame().getMainInstance().getActiveReportFrame(), this.getCrosstabElement());
                    Vector changed_elements = new Vector();
                    while (e.hasMoreElements()) {
                        ReportElement re = (ReportElement)e.nextElement();

                        undoOp.addElement( re );

                        re.trasform(p, transformation_type);
                        re.adjustCell(getCrosstabElement().getCells());
                        undoOp.captureUniqueModified(re);
                        getParentReportFrame().addUndoOperation(undoOp);

                        changed_elements.add( re );

                    }
                    if (changed_elements.size() > 0) {
                        getMainFrame().getMainInstance().getActiveReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getMainFrame().getMainInstance().getActiveReportFrame(), getCrosstabElement(), changed_elements , ReportElementChangedEvent.CHANGED));
                    }

                    this.repaint();
                }

                transformation_undo_delta = new Point(0,0);
                transformation_type = TransformationType.TRANSFORMATION_NONE;
                getMainFrame().getElementPropertiesDialog().updateSelection();
            }
        }



        mouse.y = mouseY;
        mouse.x = mouseX;

    }//GEN-LAST:event_formMouseReleased

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged

        int mouseX = (int)evt.getPoint().getX();
        int mouseY = (int)evt.getPoint().getY();

        if (drag_selection_mode == true) {
            Graphics2D gg = (Graphics2D)this.getGraphics();
            gg.setXORMode(Color.GREEN);
            Stroke s = gg.getStroke();
            gg.setStroke(selectionStroke);
            if (!first_draw_selection_rect) {
                gg.drawRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                        (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                        (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
                        (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            }

            drag_selection_end = new java.awt.Point(evt.getX(), evt.getY());
            // Draw the new rectangle...
            gg.drawRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                    (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                    (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
                    (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            gg.setPaintMode();
            first_draw_selection_rect = false;
            if (s != null) gg.setStroke(s);
        }
        else if (cell_dragging && readyToDragCellVertically>0) {
            Graphics gg = this.getGraphics();
            gg.setXORMode(Color.WHITE);
            gg.drawLine( 0, mouse.y , this.getWidth(), mouse.y);
            gg.drawLine( 0, mouseY, this.getWidth(), mouseY);
            gg.setPaintMode();
        }
        else if (cell_dragging && readyToDragCellHorizontally>0) {
            Graphics gg = this.getGraphics();
            gg.setXORMode(Color.WHITE);
            gg.drawLine( mouse.x, 0 , mouse.x, this.getHeight());
            gg.drawLine( mouseX, 0, mouseX, this.getHeight());
            gg.setPaintMode();
        }
        else if (newObjectType != ReportElementType.NONE && newObjectOrigin!= null) {

            /* In the process of defing the second point. */

            Graphics gg = this.getGraphics();
            gg.setXORMode(Color.WHITE);
            int delta_x = 0;
            int delta_y = 0;

            if (newObjectType != ReportElementType.LINE_ELEMENT && !firstXORDraw) {
                delta_x = mouse.x- newObjectOrigin.x;
                delta_y = mouse.y-newObjectOrigin.y;

                delta_x = (delta_x <0) ? delta_x : 0;
                delta_y = (delta_y<0) ? delta_y : 0;
            }


            int delta_w = 0;
            int delta_h = 0;
            if (!firstXORDraw) {
                delta_w = mouse.x-newObjectOrigin.x;
                delta_h = mouse.y-newObjectOrigin.y;
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
            }

            if (newObjectType == ReportElementType.LINE_ELEMENT) {

                /* overwrite the other one, so that it disappears */
                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                    Point straight = straighten( delta_w, delta_h);
                    delta_w = straight.x;
                    delta_h = straight.y;
                }


                if (!firstXORDraw) {
                    gg.drawLine( newObjectOrigin.x, newObjectOrigin.y, newObjectOrigin.x+delta_w,newObjectOrigin.y+delta_h);
                }

                delta_w = evt.getX()-newObjectOrigin.x;
                delta_h = evt.getY()-newObjectOrigin.y;

                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                    Point straight = straighten( delta_w, delta_h);
                    delta_w = straight.x;
                    delta_h = straight.y;
                }

                gg.drawLine( newObjectOrigin.x, newObjectOrigin.y, newObjectOrigin.x+delta_w,newObjectOrigin.y+delta_h);
                mouse.x = newObjectOrigin.x+delta_w;
                mouse.y = newObjectOrigin.x+delta_y;

            } else if (newObjectType == ReportElementType.ELLIPSE_ELEMENT) {
                if (!firstXORDraw)  gg.drawOval( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));

                delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                gg.drawOval( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
            } else if (newObjectType == ReportElementType.ROUND_RECTANGLE_ELEMENT) {
                if (!firstXORDraw) {
                    gg.drawRoundRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h),20,20);
                }
                delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                //gg.drawRect( snapToGridOrizzontally(newObjectOrigin.x+w_delta_x), snapToGridVertically(newObjectOrigin.y+delta_y), gridMultiple(Math.abs(evt.getX()-newObjectOrigin.x)), gridMultiple(Math.abs(evt.getY()-newObjectOrigin.y)));
                gg.drawRoundRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h),20,20);
            } else {
                if (!firstXORDraw) {
                    gg.drawRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
                }
                delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                //gg.drawRect( snapToGridOrizzontally(newObjectOrigin.x+w_delta_x), snapToGridVertically(newObjectOrigin.y+delta_y), gridMultiple(Math.abs(evt.getX()-newObjectOrigin.x)), gridMultiple(Math.abs(evt.getY()-newObjectOrigin.y)));
                gg.drawRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
                //gg.drawRect( newObjectOrigin.x+delta_x, newObjectOrigin.y+delta_y, Math.abs(delta_from_origin), Math.abs(evt.getY()-newObjectOrigin.y));
            }

            firstXORDraw = false;
            gg.setPaintMode();

            //mouse.x = mouse.x + delta_from_origin;
            //mouse.y = evt.getY();
            //return;
        }
        else if (selectedElements.size() >0 && newObjectType == ReportElementType.NONE && transformation_origin_end != null) {
            int new_transformation_origin_end_x = transformation_origin_end.x;
            int new_transformation_origin_end_y = transformation_origin_end.y;

            if (transformation_type >=0 && trasforming) {
                Graphics gg = this.getGraphics();
                gg.setXORMode(Color.WHITE);
                if (!firstXORDrawTransforming) {
                    // redraw old rectangles...
                    // transformation_origin
                    // transformation_origin_end
                    // Pain a rectangle....

                    // if no modifications is needed, return...

                    // MAGNET EFFECT
                    new_transformation_origin_end_y = evt.getY();
                    new_transformation_origin_end_x = evt.getX();

                    //if (this.isSnapToGrid()) {
                    //    if (Math.abs( transformation_origin_end.x - evt.getX())%(getGridSize()*getZoomFactor())  == 0)
                    //        new_transformation_origin_end_x = evt.getX();
                    //    if (Math.abs( transformation_origin_end.y - evt.getY() )%(getGridSize()*getZoomFactor())  == 0)
                    //        new_transformation_origin_end_y = evt.getY();
                    //
                    //    if (new_transformation_origin_end_x == transformation_origin_end.x &&
                    //            new_transformation_origin_end_y == transformation_origin_end.y) {
                    //        return;
                    //    }
                    //} else {
                    //    new_transformation_origin_end_x = evt.getX();
                    //    new_transformation_origin_end_y = evt.getY();
                    //}
                    Enumeration e = getSelectedElements().elements();
                    while (e.hasMoreElements()) {
                        ReportElement re = (ReportElement)e.nextElement();
                        Rectangle bounds = new Rectangle(getZoomedDim(re.getPosition().x-10)+10,
                                getZoomedDim(re.getPosition().y-10)+10,
                                getZoomedDim(re.getWidth()),
                                getZoomedDim(re.getHeight()));
                        // Scale rectangle...
                        if (transformation_type == TransformationType.TRANSFORMATION_MOVE) {
                            // First of all we must see if the resistence was Exceeded...
                            if (resistenceExceeded) {
                                // Change location...
                                bounds.translate(  transformation_origin_end.x-transformation_origin.x,
                                        transformation_origin_end.y-transformation_origin.y);
                                gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                            }
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_S ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) {
                            // Change location...
                            int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) ? transformation_origin_end.x-transformation_origin.x : 0;
                            int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_S) ? transformation_origin_end.y-transformation_origin.y : 0;
                            bounds.setSize( Math.max(0, bounds.width + x_delta),
                                    Math.max(0, bounds.height + y_delta));
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_N  ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) {
                            // Change location...
                            int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) ? transformation_origin_end.x-transformation_origin.x : 0;
                            int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_N) ? transformation_origin_end.y-transformation_origin.y : 0;
                            int height_grow = Math.min( y_delta,bounds.height);
                            int width_grow = Math.min( x_delta,bounds.width);
                            bounds.translate(width_grow,height_grow);
                            bounds.setSize( bounds.width - width_grow, bounds.height - height_grow);
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE) {
                            // Change location...
                            int x_delta = Math.max(-bounds.width,(transformation_origin_end.x-transformation_origin.x));
                            int y_delta = Math.min(bounds.height,  (transformation_origin_end.y-transformation_origin.y) );
                            bounds.y += y_delta;
                            bounds.height -= y_delta;
                            bounds.width += x_delta;
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW) {
                            // Change location...
                            int x_delta = Math.min(bounds.width,  (transformation_origin_end.x-transformation_origin.x) );
                            int y_delta = Math.max(-bounds.height,(transformation_origin_end.y-transformation_origin.y));
                            bounds.x += x_delta;
                            bounds.width -= x_delta;
                            bounds.height += y_delta;
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        }
                    }
                }

                    /*
                    if (Math.abs( transformation_origin_end.x - evt.getX())%(getGridSize()*getZoomFactor())  == 0)
                        transformation_origin_end.x = evt.getX();
                    if (Math.abs( transformation_origin_end.y - evt.getY() )%(getGridSize()*getZoomFactor())  == 0)
                        transformation_origin_end.y = evt.getY();
                     */
                transformation_origin_end.x = new_transformation_origin_end_x;
                transformation_origin_end.y = new_transformation_origin_end_y;

                Enumeration e = getSelectedElements().elements();
                while (e.hasMoreElements()) {
                    ReportElement re = (ReportElement)e.nextElement();
                    Rectangle bounds = new Rectangle(getZoomedDim(re.getPosition().x-10)+10,
                            getZoomedDim(re.getPosition().y-10)+10,
                            getZoomedDim(re.getWidth()),
                            getZoomedDim(re.getHeight()));
                    // Scale rectangle...
                    if (transformation_type == TransformationType.TRANSFORMATION_MOVE) {
                        // Change location...
                        if (Math.abs(transformation_origin_end.x-transformation_origin.x) >5 ||
                                Math.abs(transformation_origin_end.y-transformation_origin.y) >5)
                            resistenceExceeded = true;
                        if (resistenceExceeded) {
                            // Search the closest x,y that match a grid intersection...

                            int pex = gridMultiple( transformation_origin_end.x-10 )+10;
                            int pey = gridMultiple( transformation_origin_end.y-10 )+10;
                            int pox = gridMultiple( transformation_origin.x-10 )+10;
                            int poy = gridMultiple( transformation_origin.y-10 )+10;

                            //System.out.println("x: "+transformation_origin_end + " y:" + transformation_origin_end.y);

                            //this.getGraphics().drawOval( grid_multiple_x, grid_multiple_y, 5,5);

                            bounds.translate( transformation_origin_end.x-transformation_origin.x,
                                    transformation_origin_end.y-transformation_origin.y);
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        }
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_S ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) {
                        // Change location...
                        int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) ? transformation_origin_end.x-transformation_origin.x : 0;
                        int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_S) ? transformation_origin_end.y-transformation_origin.y : 0;
                        bounds.setSize( Math.max(0, bounds.width + x_delta),
                                Math.max(0, bounds.height + y_delta));
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_N  ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) {
                        // Change location...
                        int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) ? transformation_origin_end.x-transformation_origin.x : 0;
                        int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_N) ? transformation_origin_end.y-transformation_origin.y : 0;
                        int height_grow = Math.min( y_delta,bounds.height);
                        int width_grow = Math.min( x_delta,bounds.width);
                        bounds.translate(width_grow,height_grow);
                        bounds.setSize( bounds.width - width_grow, bounds.height - height_grow);
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE) {
                        // Change location...
                        int x_delta = Math.max(-bounds.width,(transformation_origin_end.x-transformation_origin.x));
                        int y_delta = Math.min(bounds.height, (transformation_origin_end.y-transformation_origin.y) );
                        bounds.y += y_delta;
                        bounds.height -= y_delta;
                        bounds.width += x_delta;
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW) {
                        // Change location...
                        int x_delta = Math.min(bounds.width, (transformation_origin_end.x-transformation_origin.x) );
                        int y_delta = Math.max(-bounds.height , (transformation_origin_end.y-transformation_origin.y));
                        bounds.x += x_delta;
                        bounds.width -= x_delta;
                        bounds.height += y_delta;
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                    }
                }

                firstXORDrawTransforming = false;
                gg.setPaintMode();
            }

        }
        mouse.y = mouseY;
        mouse.x = mouseX;
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

        this.requestFocus();
        int mouseX = (int)evt.getPoint().getX();
        int mouseY = (int)evt.getPoint().getY();


        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() < 2)
        {

            if (readyToDragCellVertically > 0)
            {
                Graphics gg = this.getGraphics();
                gg.setXORMode(Color.WHITE);
                gg.drawLine( 0, mouseY, this.getWidth(), mouseY);
                gg.setPaintMode();
                // Save
                mouse.y = mouseY;
                mouse.x = mouseX;
                cell_dragging = true;
                return;
            }
            else if (readyToDragCellHorizontally > 0)
            {
                Graphics gg = this.getGraphics();
                gg.setXORMode(Color.WHITE);
                gg.drawLine( mouseX, 0, mouseX, this.getHeight());
                gg.setPaintMode();
                // Save
                mouse.y = mouseY;
                mouse.x = mouseX;
                cell_dragging = true;
                return;
            }
            else if (newObjectType != ReportElementType.NONE)
            {
                firstXORDraw = false;
                newObjectOrigin = new Point(evt.getX(),evt.getY());
                mouse.x = newObjectOrigin.x;
                mouse.y = newObjectOrigin.y;
                return;
            }

            if (selectedElements.size() > 0 && (( evt.getModifiers() & evt.SHIFT_MASK ) == 0))
            {
                // We are transformation mode?
                if (transformation_type >=0)
                {
                    trasforming = true;
                    firstXORDrawTransforming = true;
                    transformation_origin = new Point(evt.getX(),evt.getY());
                    transformation_undo_delta = new Point(0,0);
                    transformation_origin_end = new Point(evt.getX(),evt.getY());
                    return;
                }
            }

            boolean foundElement = false;
            // Look if I clicked over an element...
            for (int i=getCrosstabElement().getElements().size()-1; i>=0; --i)
            {

                 ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
                 if ((isDefaultCellMode() == (re.getCell().getType() == CrosstabCell.NODATA_CELL))  && re.intersects(new Point(getRealDim(evt.getX()-10)+10,
                            getRealDim(evt.getY()-10)+10)))
                    {
                        foundElement = true;
                        if (selectedElements.size()>0 && re == ((ReportElement)selectedElements.firstElement())) continue;

                        if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0)
                        {
                            if (getSelectedElements().contains(re))
                            {
                                getSelectedElements().remove(re);
                                repaint();
                            }
                            else
                            {
                                addSelectedElement(re);
                                repaint();
                            }
                        }
                        else
                        {
                            setSelectedElement(re);
                        }
                        trasforming = true;
                        firstXORDrawTransforming = true;
                        transformation_origin = new Point(evt.getX(),evt.getY());
                        transformation_origin_end = new Point(evt.getX(),evt.getY());

                        //if (getMainFrame().isEMMActive())
                        //    transformation_type = TransformationType.TRANSFORMATION_NONE;
                        //else
                        transformation_type = TransformationType.TRANSFORMATION_MOVE;

                        this.setCursor(Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ));
                    }
            }

            if (!foundElement)
            {
                if (getSelectedElements().size() > 0 && ( evt.getModifiers() & evt.SHIFT_MASK ) == 0)
                {
                    setSelectedElement(null);
                }

                // We can enter in drag selection mode...
                drag_selection_mode = true;
                first_draw_selection_rect = true;
                drag_selection_origin = new Point(evt.getX(),evt.getY());
                drag_selection_end = new java.awt.Point(evt.getX(), evt.getY());
            }

        }
        else if (evt.getButton() == evt.BUTTON3)
        {

            if (getSelectedElements().size() > 0)
            {
                jMenuItemCellProperties.setEnabled( false );
                selectedCell = getCellByPoint( new Point( getRealDim(evt.getX()-10), getRealDim( evt.getY()-10)));
                jMenuItemCellProperties.setEnabled( selectedCell != null );

                jMenuItemCopy.setEnabled(true);
                jMenuItemCut.setEnabled(true);
                jMenuItemDelete.setEnabled(true);
                jMenuItemCopyStyle.setEnabled(true);
                jMenuItemPasteStyle.setEnabled(getMainFrame().getStyleClipboard() != null);

                jMenuItemPaste.setEnabled(getMainFrame().getClipBoard().size() > 0);
                jCheckBoxMenuItemDefaultCellEdit.setSelected( isDefaultCellMode() );

                jPopupMenuCrosstabReporteElement.show(this,evt.getX(),evt.getY());

            }
            else
            {
                jMenuItemCellProperties1.setEnabled( true );
                selectedCell = getCellByPoint( new Point( getRealDim(evt.getX()-10), getRealDim( evt.getY()-10)));
                jMenuItemCellProperties1.setEnabled( selectedCell != null );
                jMenuItemPaste1.setEnabled(getMainFrame().getClipBoard().size() > 0);
                jCheckBoxMenuItemDefaultCellEdit1.setSelected( isDefaultCellMode() );

                jPopupMenuCrosstab.show(this,evt.getX(),evt.getY());
            }

        }

    }//GEN-LAST:event_formMousePressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            if (getSelectedElements().size() > 0 )  getParentReportFrame().openElementPropertiesDialog();
        }

    }//GEN-LAST:event_formMouseClicked

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

        double mouseX = evt.getPoint().getX();
        double mouseY = evt.getPoint().getY();

        readyToDragCellVertically = -1;
        readyToDragCellHorizontally = -1;

        try {

            if (newObjectType == ReportElementType.NONE)
            {
                if (getSelectedElements().size() == 0)
                {
                    // Check if we are near to a cell line...
                    for (int i=1; i<getRows().size(); ++i)
                    {
                        int position = ((Integer)getRows().get(i)).intValue();
                        position = getZoomedDim( position ) + 10;
                        if (mouseY > position -2 && mouseY < position +2)
                        {
                            readyToDragCellVertically = i;
                            this.setCursor( Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                            break;
                        }
                    }

                    // Check if we are near to a cell line...
                    for (int i=1; readyToDragCellVertically < 0 && i< getColumns().size(); ++i)
                    {
                        int position = ((Integer)getColumns().get(i)).intValue();
                        position = getZoomedDim( position ) + 10;
                        if (mouseX > position -2 && mouseX < position +2)
                        {
                            readyToDragCellHorizontally = i;
                            this.setCursor( Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                            break;
                        }
                    }

                    if (readyToDragCellVertically <=0 && readyToDragCellHorizontally <= 0)
                    {
                        this.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }

                if (selectedElements.size() >0 && newObjectType == ReportElementType.NONE) {
                // We are trasformation mode?
                {
                    // Vediamo se siamo sopra la figura...
                    Enumeration e = selectedElements.elements();
                    boolean found = false;
                    boolean sizeall = false;
                    while (!found && e.hasMoreElements() ) {

                        ReportElement selectedRe = (ReportElement)e.nextElement();
                        Rectangle grip = new Rectangle(getZoomedDim(selectedRe.position.x-10)-5+10, getZoomedDim(selectedRe.position.y-10)-5+10,
                                getZoomedDim(selectedRe.width)+10,getZoomedDim(selectedRe.height)+10);
                        Rectangle cursor = new Rectangle(evt.getX(),
                                evt.getY(),
                                1,1);

                        if (cursor.intersects(grip)) {
                            sizeall = true;
                            // Vediamo se interseca una grip...
                            grip.width  = 5;
                            grip.height = 5;

                            if (cursor.intersects(grip)) {
                                this.setCursor(Cursor.getPredefinedCursor( Cursor.NW_RESIZE_CURSOR ));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_NW;
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x-10+selectedRe.width)+10;
                                grip.y = getZoomedDim(selectedRe.position.y-10)+10-5;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.NE_RESIZE_CURSOR ));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_NE;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x-10)+10-5;
                                grip.y = getZoomedDim(selectedRe.position.y+(selectedRe.height/2)-10)+10-2;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_W;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x-10)+10-5;
                                grip.y = getZoomedDim(selectedRe.position.y-10+selectedRe.height)+10;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.SW_RESIZE_CURSOR));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_SW;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x+(selectedRe.width/2)-10)+10-2;
                                grip.y = getZoomedDim(selectedRe.position.y-10)+10-5;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_N;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x+(selectedRe.width/2)-10)+10-2;
                                grip.y = getZoomedDim(selectedRe.position.y+selectedRe.height-10)+10;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_S;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x+selectedRe.width-10)+10;
                                grip.y = getZoomedDim(selectedRe.position.y+selectedRe.height-10)+10;
                                grip.width  = 5;
                                grip.height = 5;
                                if (cursor.intersects(grip)) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR));
                                    found =true;
                                    transformation_type = TransformationType.TRANSFORMATION_RESIZE_SE;
                                }
                            }

                            if (!found) {
                                grip.x = getZoomedDim(selectedRe.position.x+selectedRe.width-10)+10;
                                    grip.y = getZoomedDim(selectedRe.position.y+(selectedRe.height/2)-10)+10-2;
                                    grip.width  = 5;
                                    grip.height = 5;
                                    if (cursor.intersects(grip)) {
                                        this.setCursor(Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR));
                                        found =true;
                                        transformation_type = TransformationType.TRANSFORMATION_RESIZE_E;
                                    }
                                }

                                if (!found) {
                                    this.setCursor(Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR));

                                    if (getMainFrame().isEMMActive())
                                        transformation_type = TransformationType.TRANSFORMATION_NONE;
                                    else {
                                        transformation_type = TransformationType.TRANSFORMATION_MOVE;
                                        resistenceExceeded = false;
                                    }
                                }
                            }
                        }
                        if (!sizeall) {
                            this.setCursor(Cursor.getDefaultCursor());
                            transformation_type = TransformationType.TRANSFORMATION_NONE;
                        }
                    }
                }

            }
            else
            {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_formMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemDefaultCellEdit;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemDefaultCellEdit1;
    private javax.swing.JMenuItem jMenuItemCellProperties;
    private javax.swing.JMenuItem jMenuItemCellProperties1;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCopyStyle;
    private javax.swing.JMenuItem jMenuItemCrosstabProperties;
    private javax.swing.JMenuItem jMenuItemCrosstabProperties1;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemElementProperties;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemPaste1;
    private javax.swing.JMenuItem jMenuItemPasteStyle;
    private javax.swing.JMenuItem jMenuItemPattern;
    private javax.swing.JMenuItem jMenuItemTransformStaticText;
    private javax.swing.JPopupMenu jPopupMenuCrosstab;
    private javax.swing.JPopupMenu jPopupMenuCrosstabReporteElement;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables


    public List getColumns() {
        return columns;
    }

    public void setColumns(List columns) {
        this.columns = columns;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    /**
     *   This method fill all not defined cells with defaults taken by inherited cell...
     *   The alghoritm to find the cell from wich a cell inherits its defaults works as follow:
     *   1. Check for the previos row cell with the same width
     *   2. Check for the previos column cell with the same height
     *   An updateGrid on CrosstabReportPanel will fix all positions...
     *   In this case it's not important the total position (Start/End/Node)
     **/
    public void addNotDefinedCells()
    {
        if (getCrosstabElement() == null)  return;

        // First of all we have to calc what cells we have and in what position they are...
        int cellGridWidth = getCrosstabElement().getColumnGroups().size();
        int cellGridHeight = getCrosstabElement().getRowGroups().size();

        // We assume that cell D/D (detail/detail) is defined.
        // Cell x,y is the cell with totalColumnGroup = colums(x) and totalRowGroup the rows(y)

        for (int y = cellGridHeight; y >= 0; --y)
        {


            for (int x = cellGridWidth; x >= 0; --x)
            {

                    String totalRowGroupName = "";
                    if (y < cellGridHeight) totalRowGroupName = ((CrosstabGroup)getCrosstabElement().getRowGroups().get(y)).getName();

                    String totalColumnGroupName = "";
                    if (x < cellGridWidth) totalColumnGroupName = ((CrosstabGroup)getCrosstabElement().getColumnGroups().get(x)).getName();

                    CrosstabCell cell = findCell( totalRowGroupName,
                                                  totalColumnGroupName);

                    if (cell == null)
                    {
                       // we have to find a cell on the same row that matchs the width to inherit by...
                       int cellHeight = getRowHeight(  totalRowGroupName );
                       int cellWidth  = getColumnWidth( totalColumnGroupName );


                       // look for a good row cell with the same width on the same row...
                       CrosstabCell templateCell = null;
                       for (int k=x+1; k < cellGridWidth; ++k)
                       {
                            templateCell = findCell( totalRowGroupName,
                                                     ((CrosstabGroup)getCrosstabElement().getColumnGroups().get(k)).getName());
                            if (templateCell == null)
                            {
                                continue;
                            }

                            if (templateCell.getWidth() == cellWidth)
                            {
                                break;
                            }
                            else
                            {
                                templateCell = null;
                            }
                       }
                       if (templateCell == null)
                       {
                           templateCell = findCell(  totalRowGroupName, "");
                           if (templateCell != null && templateCell.getWidth() != cellWidth) templateCell = null;
                       }

                       if (templateCell == null) // Look on the same column...
                       {
                           for (int k=y+1; k < cellGridHeight; ++k)
                           {
                                templateCell = findCell( ((CrosstabGroup)getCrosstabElement().getRowGroups().get(k)).getName(),
                                                         totalColumnGroupName);

                                if (templateCell.getHeight() == cellHeight)
                                {
                                    // FOUND IT!
                                    break;
                                }
                                else
                                {
                                    templateCell = null;
                                }
                           }
                           if (templateCell == null)
                           {
                               templateCell = findCell(  "", totalColumnGroupName);
                               if (templateCell != null && templateCell.getHeight() != cellHeight) templateCell = null;
                           }
                       }

                       if (templateCell == null)
                       {
                           // Default not found!!!! Our cell will be void!
                           cell = new CrosstabCell();
                           cell.setParent( this.getCrosstabElement());
                           cell.setWidth(cellWidth);
                           cell.setHeight(cellHeight);
                           cell.setColumnTotalGroup( totalColumnGroupName);
                           cell.setRowTotalGroup( totalRowGroupName );
                       }
                       else
                       {
                           cell = templateCell.cloneMe();
                           cell.setColumnTotalGroup( totalColumnGroupName);
                           cell.setRowTotalGroup( totalRowGroupName );
                           cell.setParent( this.getCrosstabElement());

                           // Duplicate all elements of this cell, and reassign parent cell...
                           int currentElements = getCrosstabElement().getElements().size();
                           for (int i=0; i<currentElements; ++i)
                           {
                               ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
                               // WARNING this copy does not support container and group elements!!!
                               if (re.getCell() == templateCell)
                               {
                                   re = re.cloneMe();
                                   cell.setColumnTotalGroup(totalColumnGroupName );
                                   cell.setRowTotalGroup(totalRowGroupName );
                                   re.setCell( cell );
                                   getCrosstabElement().getElements().add(re);
                               }
                           }
                       }

                       getCrosstabElement().getCells().add( cell );
                    }
            }
        }

    }

    public Vector getSelectedElements() {
        return selectedElements;
    }

    public void setSelectedElements(Vector selectedElements) {
        this.selectedElements = selectedElements;
    }

    /**
     *  Point is a point il logical coordinates, so you have to perform a conversion before call it:
     *  getCellByPoint( new Point( getRealDim(x), getRealDim(y)))
     *  this if you are using mouse coordinates...
     **/
    public CrosstabCell getCellByPoint(Point p) {

        for (int i=0; i<getCrosstabElement().getCells().size(); ++i)
        {
            CrosstabCell cell = (CrosstabCell)this.getCrosstabElement().getCells().elementAt(i);
            if (cell.getBounds().contains(p) && (isDefaultCellMode() == (cell.getType() == CrosstabCell.NODATA_CELL)))
            {
                return cell;
            }
        }

        return null;
    }

    public Vector getRowBands() {
        return rowBands;
    }

    public Vector getColumnBands() {
        return columnBands;
    }


    /**
     * This method remove from selection vector elements no longer part of this crosstab
     */
    public void validateSelection()
    {
        for (int i = 0; i<getSelectedElements().size(); ++i)
        {
           ReportElement re = (ReportElement)getSelectedElements().elementAt(i);
           if (!getCrosstabElement().getElements().contains( re ))
           {
               getSelectedElements().remove(re);
               --i;
           }
        }
        // UPDATE SELECTION NOW!!!
    }

    /**
     * Shortcut for  MainFrame.getMainInstance();
     **/
    public MainFrame getMainFrame()
    {
        return MainFrame.getMainInstance();
    }


    public void setSelectedElement(ReportElement e) {
        setSelectedElement(e, true);
    }

    /**
     * Set the selected element
     *
     */
    public void setSelectedElement(ReportElement el, boolean fireEvent) {

        getSelectedElements().removeAllElements();


        if (el != null && (isDefaultCellMode() == (el.getCell().getType() == CrosstabCell.NODATA_CELL))) {

            selectedElements.addElement(el);
            getMainFrame().setCutCopyEnabled(true);
        } else {
            getMainFrame().setCutCopyEnabled(false);
        }
        repaint();

        if (fireEvent) {
            fireSelectionChangedEvent();
        }
    }


    public void addSelectedElement(ReportElement el) {
        addSelectedElement(el, true);
    }

    /**
     * Add an element to the selection...
     *
     */
    public void addSelectedElement(ReportElement el, boolean fireEvent) {
        if (selectedElements.contains(el)) return;

        if (isDefaultCellMode() != (el.getCell().getType() == CrosstabCell.NODATA_CELL)) return;
        //label1.setText("ADDED");
        // Faccio una corona non selected attorno al nuovo elemento...
        Rectangle rect = new Rectangle(el.position.x-5,el.position.y-5,el.width+10, el.height+10);
        selectedElements.addElement(el);

        this.repaint();
        if (fireEvent) {
            fireSelectionChangedEvent();
        }

        getMainFrame().setCutCopyEnabled(true);
    }

    public int gridMultiple( int zoomedDim) {
        //if (!isSnapToGrid()) {
            return zoomedDim;
        //}

        //int zoomedGridSize = getZoomedDim(this.gridSize);
        //double val = Math.abs(zoomedDim);
        //if ( zoomedGridSize > 1.0 ) {
        //    val =  Math.floor( ( val +  (0.5 * zoomedGridSize)) / zoomedGridSize ) * zoomedGridSize;
        //}

        //return (int) ( (zoomedDim >= 0) ? val : -val );
    }

    public int magnetEffect(List spots, int position)
    {
        for (int i=0; i<spots.size(); ++i)
        {
            int spotPoint = ((Integer)spots.get(i)).intValue();
            spotPoint = getZoomedDim(spotPoint)+10;
            if (position > spotPoint - 5 && position < spotPoint + 5)
            {
                return spotPoint;
            }
        }
        return position;
    }


    /** Getter for property newObjectType.
     * @return Value of property newObjectType.
     *
     */
    public int getNewObjectType() {
        return newObjectType;
    }

    /** Setter for property newObjectType.
     * @param newObjectType New value of property newObjectType.
     *
     */
    public void setNewObjectType(int newObjectType) {

        if (this.newObjectType == newObjectType) return;

        if (newObjectType == ReportElementType.NONE) {
            this.setCursor( java.awt.Cursor.getDefaultCursor());
        } else {
            this.setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        }
        this.newObjectType = newObjectType;
    }

    public Point straighten( int delta_w, int delta_h  ){
        // straighten on 90 % degrees
        double clipAngle = 15;

        double R = Math.sqrt(  (delta_w * delta_w) + (delta_h*delta_h) );
        double cos =  delta_w /  R;
        double angle = 180 * Math.acos( cos )/ Math.PI ;

        int repeat = (int) Math.floor((angle + (0.5 * clipAngle)) / clipAngle) ;

        double newAngle = repeat * clipAngle ;

        // keep the sign the same!
        delta_h =  (int) (Math.abs(( R * Math.sin( (newAngle / 180 )* Math.PI))) * delta_h / Math.abs(delta_h) );

        delta_w = (int) ( Math.abs(( R * Math.cos( (newAngle / 180) * Math.PI)))  * delta_w / Math.abs(delta_w) ) ;

        /*
        // aligning on  the grid if necessary
        if (Math.abs(delta_h) == Math.abs(delta_w) ) {
            delta_h = gridMultiple( delta_h);
            delta_w = gridMultiple( delta_w);
        } else {
            if (delta_w == 0) {
                delta_h = gridMultiple( delta_h);
            }
            if (delta_h == 0) {
                delta_w = gridMultiple( delta_w);
            }
        }
        */

        return new Point( delta_w, delta_h);
    }


    /**
     *  Not implemented, just returns p.
     **/
    public int snapToGridVertically(int p)
    {
        return p;
    }

    public void deleteSelectedElements() {
        // Remove selected elements...
        Enumeration e = selectedElements.elements();

        DeleteElementsOperation undoOp = new DeleteElementsOperation(getParentReportFrame(), getCrosstabElement());
        Vector deletedElements = new Vector();

        while (e.hasMoreElements()) {


            ReportElement re = (ReportElement) e.nextElement();

            if ( getCrosstabElement().getElements().contains( re))
            {
                undoOp.addElement(  re, getCrosstabElement().getElements().indexOf(re));
                //System.out.println("Deleting " + re + " " +  getCrosstabElement().getElements().indexOf(re));
                getCrosstabElement().getElements().remove( re );
                deletedElements.add(re);
                if (re instanceof FrameReportElement)
                {
                    removeSubElements(re, undoOp, deletedElements);
                }
            }
            //fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.REMOVED));
        }

        getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(),getCrosstabElement(), deletedElements,  ReportElementChangedEvent.REMOVED ));
        this.setSelectedElement(null);
        getParentReportFrame().addUndoOperation(undoOp);
        //selectedElements.removeAllElements();

        this.repaint();
    }

    protected void removeSubElements(ReportElement parentElement, DeleteElementsOperation undoOp, Vector deletedElements)
    {
        for (int i=0; i<getCrosstabElement().getElements().size(); ++i)
        {
            ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
            if (re.getParentElement() == parentElement)
            {
                undoOp.addElement(  re, getCrosstabElement().getElements().indexOf(re));
                getCrosstabElement().getElements().remove( re );
                i--;
                deletedElements.add(re);
                if (re instanceof FrameReportElement)
                {
                    removeSubElements(re, undoOp, deletedElements);
                }
            }
        }
    }

    public void fireSelectionChangedEvent() {

        ReportElementsSelectionEvent rece = new ReportElementsSelectionEvent(getParentReportFrame(),getCrosstabElement(), this.getSelectedElements()) ;
        this.getParentReportFrame().fireReportListenerReportElementsSelectionChanged(rece );
        getMainFrame().getElementPropertiesDialog().updateSelection();

    }


    public void copyStyle() {
        // We should copy this element to use it as style template...
        if (this.getSelectedElements().size() == 0) return;
        this.getMainFrame().setStyleClipbardContent( ((ReportElement)this.getSelectedElements().elementAt(0)).cloneMe() );

    }

    public void pasteStyle() {
        if (this.getSelectedElements().size() == 0) return;
        if (this.getMainFrame().getStyleClipboard() == null) return;

        // Style are relative to:
        ReportElement templateElement = this.getMainFrame().getStyleClipboard();

        PasteStyleOperation undoOp = new PasteStyleOperation(getParentReportFrame(), getCrosstabElement());

        Enumeration elements = getSelectedElements().elements();
        while (elements.hasMoreElements()) {
            ReportElement re = (ReportElement)elements.nextElement();
            undoOp.addElement(re, re.cloneMe(), templateElement);
            getParentReportFrame().applyStyle(re, templateElement);
        }
        getParentReportFrame().addUndoOperation(undoOp);
        getParentReportFrame().fireReportListenerReportElementsSelectionChanged(new ReportElementsSelectionEvent( getParentReportFrame(),getCrosstabElement(), getSelectedElements()));
        this.repaint();
    }


    public void  transformStaticInTextFields() {

        String keyStaticText = (new StaticTextReportElement(0,0,0,0)).getKey();
        String keyTextField  = (new TextFieldReportElement(0,0,0,0)).getKey();
        // End code Robert Lamping, 13 july 2004


        // Edit static Text
        Enumeration e = selectedElements.elements();

        ReplacedElementsOperation undoOp = new ReplacedElementsOperation(getParentReportFrame(), this.getCrosstabElement());

        while (e.hasMoreElements()) {
            ReportElement re = (ReportElement) e.nextElement();
            if(re instanceof StaticTextReportElement){
                // 1.
                TextFieldReportElement tfre = new TextFieldReportElement(re.getPosition().x, re.getPosition().y, re.width, re.height);
                tfre.setRelativePosition(new Point(re.getRelativePosition().x, re.getRelativePosition().y));
                // Set base characteristics...
                tfre.copyBaseReportElement(tfre, re);
                // Set Text characteristics...
                tfre.setBold(((TextReportElement)re).isBold() );
                tfre.setUnderline( ((TextReportElement)re).isUnderline() );
                tfre.setStrikeTrought( ((TextReportElement)re).isStrikeTrought() );
                tfre.setItalic( ((TextReportElement)re).isItalic() );

                tfre.setLineSpacing( ((TextReportElement)re).getLineSpacing() );
                tfre.setPDFFontName( ((TextReportElement)re).getPDFFontName() );
                tfre.setPdfEncoding( ((TextReportElement)re).getPdfEncoding() );
                tfre.setReportFont( ((TextReportElement)re).getReportFont() );
                tfre.setRotate( ((TextReportElement)re).getRotate() );
                tfre.setTTFFont( ((TextReportElement)re).getTTFFont() );
                tfre.setVerticalAlign( ((TextReportElement)re).getVerticalAlign());
                tfre.setFont( ((TextReportElement)re).getFont() );
                tfre.setFontName( ((TextReportElement)re).getFontName() );
                tfre.setFontSize(((TextReportElement)re).getFontSize() );

                tfre.setClassExpression( "java.lang.String" );

                // Begin code Robert Lamping, 13 july 2004
                // Changing key element name
                if (re.getKey().indexOf( keyStaticText + "-" ) == 0 ) {
                    // key indeed begins with "staticText-"
                    //Okay then request the next available key for a normal field text
                    tfre.setKey( getParentReportFrame().getNextElementKey( keyTextField ) );
                }

                // End code Robert Lamping, 13 july 2004

                String te = ((TextReportElement)re).getText();
                String result = javax.swing.JOptionPane.showInputDialog(this, I18n.getString("messages.enterNewValue","Please enter a new Value"), "\"" +te + "\"");
                if(result != null && !result.equals("")){
                    ((TextReportElement)tfre).setText(result);
                    // 1. remove element
                    int index = getCrosstabElement().getElements().indexOf( re );
                    getCrosstabElement().getElements().remove( re );
                    getCrosstabElement().getElements().add( index, tfre);

                    int index2 = getSelectedElements().indexOf( re );
                    getSelectedElements().remove( re );
                    getSelectedElements().add( index2, tfre);

                    // 2. add element
                    undoOp.addElement( re, tfre);
                    getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(),  re , ReportElementChangedEvent.REMOVED));
                    getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), tfre , ReportElementChangedEvent.ADDED));

                }
            }
        }
        getParentReportFrame().addUndoOperation(undoOp);
        getParentReportFrame().fireReportListenerReportElementsSelectionChanged(new ReportElementsSelectionEvent( getParentReportFrame(), getCrosstabElement(), getSelectedElements()));
        repaint();
    }


     /**
     *
     */
    public void bringToFront() {
        Enumeration e = this.getSelectedElements().elements();

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(getParentReportFrame(), getCrosstabElement());

        while  (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();

            int oldPosition = getCrosstabElement().getElements().indexOf(element);
            getCrosstabElement().getElements().remove(element);
            getCrosstabElement().getElements().addElement(element);
            int newPosition = getCrosstabElement().getElements().indexOf(element);
            undoOp.addElement(element,oldPosition, newPosition);
        }

        if (undoOp.getElements().size() > 0) {
            getParentReportFrame().addUndoOperation(undoOp);
        }
        this.repaint();
        // We must update the tree... the order i changed...
        /* TODO */
    }

    public void sendToBack() {
        Enumeration e = this.getSelectedElements().elements();

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(getParentReportFrame(), getCrosstabElement());

        while  (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();

            int oldPosition = getCrosstabElement().getElements().indexOf(element);
            getCrosstabElement().getElements().remove(element);
            if (element.getParentElement() != null)
            {
                getCrosstabElement().getElements().insertElementAt( element, getCrosstabElement().getElements().indexOf(element.getParentElement())+1);
            }
            else
            {
                 getCrosstabElement().getElements().insertElementAt(element,0);
            }
            int newPosition = 0;
            undoOp.addElement(element,oldPosition, newPosition);
        }

        if (undoOp.getElements().size() > 0) {
            getParentReportFrame().addUndoOperation(undoOp);
        }
        this.repaint();
        // We must update the tree... the order is changed...
        /* TODO */
    }


    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
        dtde.acceptDrag(dtde.getDropAction());
    }

    public void dragExit(java.awt.dnd.DropTargetEvent dte) {
    }

    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) {
        dtde.acceptDrag(dtde.getDropAction());
    }

    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
       try {

            DropTargetContext context = dtde.getDropTargetContext();

            Transferable tr = dtde.getTransferable();

            DataFlavor[] df = tr.getTransferDataFlavors();


            if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.crosstab.Measure"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.crosstab.Measure.class, it.businesslogic.ireport.crosstab.Measure.class.getName());
                it.businesslogic.ireport.crosstab.Measure field = (it.businesslogic.ireport.crosstab.Measure)tr.getTransferData( myFlavor );
                // Add a field!!!
                //System.out.println("Field name:" + field.getName() +" from "+ field.getClassType() );

                // Create a standard field...
                this.dropNewTextField( dtde.getLocation(), "$V{"+ field.getName() +"}",  field.getClassType(),"Now" );
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.crosstab.CrosstabRowGroup"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.crosstab.CrosstabRowGroup.class, it.businesslogic.ireport.crosstab.CrosstabRowGroup.class.getName());
                it.businesslogic.ireport.crosstab.CrosstabGroup field = (it.businesslogic.ireport.crosstab.CrosstabGroup)tr.getTransferData( myFlavor );
                this.dropNewTextField( dtde.getLocation(), "$V{"+ field.getName() +"}",  field.getBucketExpressionClass(),"Now" );
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.crosstab.CrosstabColumnGroup"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.crosstab.CrosstabColumnGroup.class, it.businesslogic.ireport.crosstab.CrosstabColumnGroup.class.getName());
                it.businesslogic.ireport.crosstab.CrosstabGroup field = (it.businesslogic.ireport.crosstab.CrosstabGroup)tr.getTransferData( myFlavor );
                this.dropNewTextField( dtde.getLocation(), "$V{"+ field.getName() +"}",  field.getBucketExpressionClass(),"Now" );
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.crosstab.GroupTotal"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.crosstab.GroupTotal.class, it.businesslogic.ireport.crosstab.GroupTotal.class.getName());
                it.businesslogic.ireport.crosstab.GroupTotal field = (it.businesslogic.ireport.crosstab.GroupTotal)tr.getTransferData( myFlavor );
                this.dropNewTextField( dtde.getLocation(), "$V{"+ field.getVarName() +"}",  field.getClassType(),"Now" );
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.crosstab.CrosstabParameter"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.crosstab.CrosstabParameter.class, it.businesslogic.ireport.crosstab.CrosstabParameter.class.getName());
                it.businesslogic.ireport.crosstab.CrosstabParameter field = (it.businesslogic.ireport.crosstab.CrosstabParameter)tr.getTransferData( myFlavor );
                this.dropNewTextField( dtde.getLocation(), "$P{"+ field.getName() +"}",  field.getClassType(),"Now" );
            }
            else if (df[0].getHumanPresentableName().equals("it.businesslogic.ireport.Style"))
            {
                java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(it.businesslogic.ireport.Style.class, it.businesslogic.ireport.JRParameter.class.getName());
                it.businesslogic.ireport.Style var = (it.businesslogic.ireport.Style)tr.getTransferData( myFlavor );
                // Add a field!!!


                ReportElement re = this.getElementAt(dtde.getLocation());
                if (re != null)
                {

                    if (!getParentReportFrame().getReport().getStyles().contains( var ))
                    {
                        boolean found = false;
                        // Look for a style with the same name...
                        for (int i=0; i<getParentReportFrame().getReport().getStyles().size(); ++i)
                        {
                            Style s = (Style)getParentReportFrame().getReport().getStyles().elementAt(i);
                            if (s.getName() != null && s.getName().equals( var.getName()))
                            {
                                var = s;
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            var = new Style(var);
                            getParentReportFrame().getReport().addStyle( var );
                        }
                    }

                    re.setStyle( var );
                    getParentReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(getParentReportFrame(), getCrosstabElement(), re, ReportElementChangedEvent.CHANGED));
                    this.repaint();
                }
            }
            else
            {
                Class c = Class.forName( df[0].getHumanPresentableName() );
                if (it.businesslogic.ireport.gui.library.AbstractLibraryObject.class.isAssignableFrom( c ))
                {
                    java.awt.datatransfer.DataFlavor myFlavor = new java.awt.datatransfer.DataFlavor(c, df[0].getHumanPresentableName());
                   Object obj = tr.getTransferData( myFlavor );
                    ((it.businesslogic.ireport.gui.library.AbstractLibraryObject )obj).drop(dtde);
                }
                else // if (.equals("it.businesslogic.ireport.JRParameter"))
                {
                    //System.out.println("Dropped a "+df[0].getHumanPresentableName());
                }
            }

            context.dropComplete(true);
        } catch (Exception ex)
        {

            ex.printStackTrace();
        }
    }

    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) {
    }

    public TextFieldReportElement dropNewTextField(Point newObjectOrigin, String textfieldExpression, String classExpression, String time) {
        TextFieldReportElement re = null;
        // Find the band to associate to the new element...

        CrosstabCell cell = getCellByPoint(  new Point( getRealDim(newObjectOrigin.x-10), getRealDim( newObjectOrigin.y-10)) );
        if (cell == null) {

            javax.swing.JOptionPane.showMessageDialog(this,I18n.getString("messages.dropObjectInsideTheCell","Please drop the object inside a cell."),"",javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int originX = cell.getLeft()+10;
        int originY = cell.getTop()+10;
        int width  =  cell.getWidth();
        int height =  cell.getHeight();

        re = new TextFieldReportElement( originX,originY,width,height);
        re.setRelativePosition(new Point(0,0));

        re.setCell( cell );
        re.setText(textfieldExpression);
        re.setMatchingClassExpression( classExpression , true);
        re.setEvaluationTime( time );

        getCrosstabElement().getElements().addElement(re);
        getParentReportFrame().addUndoOperation( new  InsertElementOperation( getParentReportFrame(), getCrosstabElement(), re ) );
        this.setSelectedElement(re);
        this.repaint();
        return re;
    }

    public boolean isDefaultCellMode() {
        return defaultCellMode;
    }

    public void setDefaultCellMode(boolean defaultCellMode) {
        this.defaultCellMode = defaultCellMode;

        if (defaultCellMode)
        {
            CrosstabCell noDataCell = getWhenNoDataCell();
            noDataCell.setPaintSelection(false);

            CellAnimationEffect ae = new CellAnimationEffect();
            ae.setNoDataCell(noDataCell );
            ae.setEditor( this );
            javax.swing.SwingUtilities.invokeLater( ae );
        }

        if (this.getSelectedElements().size() > 0)
        {
            this.setSelectedElement(null);
        }
        else
        {
            this.invalidate();
            this.repaint();
        }




    }


    /**
     * This method returns the WhenNoDataCell (that *should* be ever present after an updateGgrid...
     *
     */
    public CrosstabCell getWhenNoDataCell()
    {
        for (int i=0; i<getCrosstabElement().getCells().size(); ++i)
        {
            CrosstabCell cell = (CrosstabCell)getCrosstabElement().getCells().elementAt(i);
            if (cell.getType() == cell.NODATA_CELL)
            {
                return cell;
            }
        }
        return null;
    }

    public java.util.List getOpenedNodesDocumentStructure() {
        return openedNodesDocumentStructure;
    }

    public void setOpenedNodesDocumentStructure(java.util.List openedNodesDocumentStructure) {
        this.openedNodesDocumentStructure = openedNodesDocumentStructure;
    }


    /**
     * This method adds to the standard popup menu for elements all the format tool items
     *
     */
    private void addFormatItemsToMenu(javax.swing.JComponent m){


        jMenuItemFillCell = new javax.swing.JMenuItem();
        jMenuItemFillCell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_maximise.png")));
        jMenuItemFillCell.setText(it.businesslogic.ireport.util.I18n.getString("fillCell", "Fill the cell"));
        jMenuItemFillCell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormatCommand.getCommand( OperationType.ELEMENT_MAXIMIZE).execute();
            }
        });

        m.add(jMenuItemFillCell);

        jMenuItemFillCellH = new javax.swing.JMenuItem();
        jMenuItemFillCellH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_hmaximise.png")));
        jMenuItemFillCellH.setText(it.businesslogic.ireport.util.I18n.getString("fillCellHorizontally", "Fill the cell (horizontally)"));
        jMenuItemFillCellH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormatCommand.getCommand( OperationType.ELEMENT_MAXIMIZE_H).execute();
            }
        });

        m.add(jMenuItemFillCellH);

        jMenuItemFillCellV = new javax.swing.JMenuItem();

        jMenuItemFillCellV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vmaximise.png")));
        jMenuItemFillCellV.setText(it.businesslogic.ireport.util.I18n.getString("fillCellVertically", "Fill the cell (vertically)"));
        jMenuItemFillCellV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FormatCommand.getCommand( OperationType.ELEMENT_MAXIMIZE_V).execute();
            }
        });

        m.add(jMenuItemFillCellV);


        jMenuAlign = new javax.swing.JMenu();
        jMenuAlign.setText(it.businesslogic.ireport.util.I18n.getString("align", "Align..."));

        jMenuItemAlignLeft = new javax.swing.JMenuItem();

        jMenuItemAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_left.png")));
        jMenuItemAlignLeft.setText(it.businesslogic.ireport.util.I18n.getString("alignLeft", "Align left"));
        jMenuItemAlignLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignLeftActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignLeft);

        jMenuItemAlignRight = new javax.swing.JMenuItem();
        jMenuItemAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_right.png")));
        jMenuItemAlignRight.setText(it.businesslogic.ireport.util.I18n.getString("alignRight", "Align right"));
        jMenuItemAlignRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignRightActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignRight);

        jMenuItemAlignTop = new javax.swing.JMenuItem();
        jMenuItemAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_top.png")));
        jMenuItemAlignTop.setText(it.businesslogic.ireport.util.I18n.getString("alignTop", "Align top"));
        jMenuItemAlignTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignTopActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignTop);

        jMenuItemAlignBottom = new javax.swing.JMenuItem();
        jMenuItemAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_bottom.png")));
        jMenuItemAlignBottom.setText(it.businesslogic.ireport.util.I18n.getString("alignBottom", "Align bottom"));
        jMenuItemAlignBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignBottomActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignBottom);

        jSeparator19 = new javax.swing.JSeparator();
        jMenuAlign.add(jSeparator19);

        jMenuItemAlignVerticalAxis = new javax.swing.JMenuItem();
        jMenuItemAlignVerticalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_center_axis.png")));
        jMenuItemAlignVerticalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignVerticalAxis", "Align vertical axis"));
        jMenuItemAlignVerticalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignVerticalAxisActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignVerticalAxis);

        jMenuItemAlignHorizontalAxis = new javax.swing.JMenuItem();
        jMenuItemAlignHorizontalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter_axis.png")));
        jMenuItemAlignHorizontalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignHorizontalAxis", "Align horizontal axis"));
        jMenuItemAlignHorizontalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignHorizontalAxisActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignHorizontalAxis);

        m.add(jMenuAlign);

        jMenuSize = new javax.swing.JMenu();
        jMenuSize.setText(it.businesslogic.ireport.util.I18n.getString("size", "Size..."));
        jMenuItemSameWidth = new javax.swing.JMenuItem();
        jMenuItemSameWidth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize.png")));
        jMenuItemSameWidth.setText(it.businesslogic.ireport.util.I18n.getString("sameWidth", "Same width"));
        jMenuItemSameWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidth);

        jMenuItemSameWidthMax = new javax.swing.JMenuItem();
        jMenuItemSameWidthMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_plus.png")));
        jMenuItemSameWidthMax.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMax", "Same width (max)"));
        jMenuItemSameWidthMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthMaxActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidthMax);

        jMenuItemSameWidthMin = new javax.swing.JMenuItem();
        jMenuItemSameWidthMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_min.png")));
        jMenuItemSameWidthMin.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMin", "Same width (min)"));
        jMenuItemSameWidthMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthMinActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidthMin);

        jSeparator17 = new javax.swing.JSeparator();
        jMenuSize.add(jSeparator17);

        jMenuItemSameHeight = new javax.swing.JMenuItem();
        jMenuItemSameHeight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize.png")));
        jMenuItemSameHeight.setText(it.businesslogic.ireport.util.I18n.getString("sameHeight", "Same height"));
        jMenuItemSameHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeight);

        jMenuItemSameHeightMin = new javax.swing.JMenuItem();
        jMenuItemSameHeightMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_min.png")));
        jMenuItemSameHeightMin.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMin", "Same height (min)"));
        jMenuItemSameHeightMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightMinActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeightMin);

        jMenuItemSameHeightMax = new javax.swing.JMenuItem();
        jMenuItemSameHeightMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_plus.png")));
        jMenuItemSameHeightMax.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMax", "Same height (max)"));
        jMenuItemSameHeightMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightMaxActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeightMax);

        jSeparator18 = new javax.swing.JSeparator();
        jMenuSize.add(jSeparator18);

        jMenuItemSameSize = new javax.swing.JMenuItem();
        jMenuItemSameSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_size.png")));
        jMenuItemSameSize.setText(it.businesslogic.ireport.util.I18n.getString("sameSize", "Same size"));
        jMenuItemSameSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameSizeActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameSize);

        m.add(jMenuSize);

        jMenuPosition = new javax.swing.JMenu();
        jMenuPosition.setText(it.businesslogic.ireport.util.I18n.getString("position", "Position..."));
        jMenuItemCenterH = new javax.swing.JMenuItem();
        jMenuItemCenterH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_hcenter.png")));
        jMenuItemCenterH.setText(it.businesslogic.ireport.util.I18n.getString("centerHorizontallyCellBased", "Center horizontally (cell based)"));
        jMenuItemCenterH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterHActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterH);

        jMenuItemCenterV = new javax.swing.JMenuItem();
        jMenuItemCenterV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter.png")));
        jMenuItemCenterV.setText(it.businesslogic.ireport.util.I18n.getString("centerVerticallyCellBased", "Center vertically (cell based)"));
        jMenuItemCenterV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterVActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterV);

        jMenuItemCenterInCell = new javax.swing.JMenuItem();
        jMenuItemCenterInCell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_ccenter.png")));
        jMenuItemCenterInCell.setText(it.businesslogic.ireport.util.I18n.getString("centerInCell", "Center in cell"));
        jMenuItemCenterInCell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterInBandActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterInCell);

        jMenuItemJoinLeft = new javax.swing.JMenuItem();
        jMenuItemJoinLeft.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesLeft", "Join sides left"));
        jMenuItemJoinLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemJoinLeftActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemJoinLeft);

        jMenuItemJoinRight = new javax.swing.JMenuItem();
        jMenuItemJoinRight.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesRight", "Join sides right"));
        jMenuItemJoinRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemJoinRightActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemJoinRight);

        m.add(jMenuPosition);

        jSeparator5 = new javax.swing.JSeparator();
        m.add(jSeparator5);

        jMenuHSpacing = new javax.swing.JMenu();
        jMenuHSpacing.setText(it.businesslogic.ireport.util.I18n.getString("horizontalSpacing", "Horizontal spacing..."));

        jMenuItemHSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemHSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemHSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSMakeEqualActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSMakeEqual);

        jMenuItemHSIncrease = new javax.swing.JMenuItem();
        jMenuItemHSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemHSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSIncreaseActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSIncrease);

        jMenuItemHSDecrease = new javax.swing.JMenuItem();
        jMenuItemHSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemHSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSDecreaseActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSDecrease);

        jMenuItemHSRemove = new javax.swing.JMenuItem();
        jMenuItemHSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuItemHSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSRemoveActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSRemove);

        m.add(jMenuHSpacing);

        jMenuVSpacing = new javax.swing.JMenu();
        jMenuVSpacing.setText(it.businesslogic.ireport.util.I18n.getString("verticalSpacing", "Vertical spacing"));
        jMenuItemVSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemVSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemVSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSMakeEqualActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSMakeEqual);

        jMenuItemVSIncrease = new javax.swing.JMenuItem();
        jMenuItemVSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemVSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSIncreaseActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSIncrease);

        jMenuItemVSDecrease = new javax.swing.JMenuItem();
        jMenuItemVSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemVSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSDecreaseActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSDecrease);

        jMenuItemVSRemove = new javax.swing.JMenuItem();
        jMenuItemVSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuItemVSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSRemoveActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSRemove);

        m.add(jMenuVSpacing);

        jSeparator8 = new javax.swing.JSeparator();
        m.add(jSeparator8);

        jMenuItemBringToFront = new javax.swing.JMenuItem();
        jMenuItemBringToFront.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtofront.png")));
        jMenuItemBringToFront.setText(it.businesslogic.ireport.util.I18n.getString("bringToFront", "Bring to front"));
        jMenuItemBringToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemBringToFrontActionPerformed(evt);
            }
        });

        m.add(jMenuItemBringToFront);

        jMenuItemSendToBack = new javax.swing.JMenuItem();
        jMenuItemSendToBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtoback.png")));
        jMenuItemSendToBack.setText(it.businesslogic.ireport.util.I18n.getString("sendToBack", "Send to back"));
        jMenuItemSendToBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSendToBackActionPerformed(evt);
            }
        });

        m.add(jMenuItemSendToBack);

    }

    public JReportFrame getParentReportFrame() {

        if (parentReportFrame == null)
        {
            parentReportFrame = getMainFrame().getActiveReportFrame( );
        }

        return parentReportFrame;
    }

    public void setParentReportFrame(JReportFrame parentReportFrame) {
        this.parentReportFrame = parentReportFrame;
    }


    /**
     * Return the element at the point location (if any...)
     */
    public ReportElement getElementAt(Point location)
    {
        Point p = new Point(getRealDim((int)location.getX()-10)+10,
                            getRealDim((int)location.getY()-10)+10);

        for (int i=getCrosstabElement().getElements().size()-1; i>=0; --i)
        {
            ReportElement re = (ReportElement)getCrosstabElement().getElements().elementAt(i);
            if ((isDefaultCellMode() == (re.getCell().getType() == CrosstabCell.NODATA_CELL))  && re.intersects(p))
            {
                return re;
            }
        }

        return null;
    }

    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxMenuItemDefaultCellEdit.setText(I18n.getString("crosstabEditorPanel.checkBoxMenuItemDefaultCellEdit","Edit When-No-Data default cell"));
                jCheckBoxMenuItemDefaultCellEdit1.setText(I18n.getString("crosstabEditorPanel.checkBoxMenuItemDefaultCellEdit1","Edit When-No-Data default cell"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jMenuItemCellProperties.setText(I18n.getString("crosstabEditorPanel.menuItemCellProperties","Cell properties"));
                jMenuItemCellProperties1.setText(I18n.getString("crosstabEditorPanel.menuItemCellProperties1","Cell properties"));
                jMenuItemCopy.setText(I18n.getString("crosstabEditorPanel.menuItemCopy","Copy"));
                jMenuItemCrosstabProperties.setText(I18n.getString("crosstabEditorPanel.menuItemCrosstabProperties","Crosstab properties"));
                jMenuItemCrosstabProperties1.setText(I18n.getString("crosstabEditorPanel.menuItemCrosstabProperties1","Crosstab properties"));
                jMenuItemCut.setText(I18n.getString("crosstabEditorPanel.menuItemCut","Cut"));
                jMenuItemDelete.setText(I18n.getString("crosstabEditorPanel.menuItemDelete","Delete"));
                jMenuItemPaste.setText(I18n.getString("crosstabEditorPanel.menuItemPaste","Paste"));
                jMenuItemPaste1.setText(I18n.getString("crosstabEditorPanel.menuItemPaste1","Paste"));
                jMenuItemPattern.setText(I18n.getString("crosstabEditorPanel.menuItemPattern","Field pattern"));
                // End autogenerated code ----------------------
    }
}
