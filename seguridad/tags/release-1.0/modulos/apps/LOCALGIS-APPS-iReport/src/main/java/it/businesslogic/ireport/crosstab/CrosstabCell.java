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
 * CrosstabCell.java
 * 
 * Created on 27 settembre 2005, 17.55
 *
 */

package it.businesslogic.ireport.crosstab;
import it.businesslogic.ireport.Box;
import it.businesslogic.ireport.BoxElement;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.util.Java2DUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

/**
 *
 * @author Administrator
 */
public class CrosstabCell implements BoxElement {
    
    
    public static final int HEADER_CELL = 1; // Total header, row header, group header
    public static final int CT_HEADER_CELL = 3; // Crosstab header cell
    public static final int DETAIL_CELL = 0; // Detail cell
    public static final int NODATA_CELL = 2; // When no data cell
    
    private int type = DETAIL_CELL;
    private String name = null;
    private int top = 0;
    private int left = 0;
    private int width = 0;
    private int height = 0;
    private String mode = "Transparent";
    
    private boolean paintSelection = false;
    
    private Style style = null;
    
    /**
     *  In the grid the line index of the top
     **/
    private int topIndex = 0;
    
    /**
     *  In the grid the line index of the bottom
     **/
    private int bottomIndex = 0;
    
    /**
     *  In the grid the line index of the left side
     **/
    private int leftIndex = 0;
    
    /**
     *  In the grid the line index of the right side
     **/
    private int rightIndex = 0;
    
    
    private String rowTotalGroup = "";
    private String columnTotalGroup = "";
    
    private CrosstabReportElement parent = null;
    
    private java.awt.Color backcolor = null;
    
    private Box box = new Box();
    
    /**
     * The last zoomFactor used in a draw operation
     **/
    private double zoomFactor = 1.0;
    
    /** Creates a new instance of CrosstabCell */
    public CrosstabCell() {
    }

    public String getName() {
        
        if (name == null)
        {
            if (getType() == NODATA_CELL)
            {
                setName("When no data");
            }
            else if (getType() == DETAIL_CELL)
            {
               setName( ((getRowTotalGroup().equals("")) ? "Detail" : getRowTotalGroup()) + " / " + ((getColumnTotalGroup().equals("")) ? "Detail" : getColumnTotalGroup())  );
            }
           else if (getType() == CT_HEADER_CELL)
           {
                setName("Crosstab header");
           }
        }
        
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRowTotalGroup() {
        return rowTotalGroup;
    }

    public void setRowTotalGroup(String rowTotalGroup) {
        this.rowTotalGroup = rowTotalGroup;
    }

    public String getColumnTotalGroup() {
        return columnTotalGroup;
    }

    public void setColumnTotalGroup(String columnTotalGroup) {
        this.columnTotalGroup = columnTotalGroup;
    }

    public java.awt.Color getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(java.awt.Color backcolor) {
        this.backcolor = backcolor;
    }

    public CrosstabReportElement getParent() {
        return parent;
    }

    public void setParent(CrosstabReportElement parent) {
        this.parent = parent;
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle(left,top,width,height);
    }
    
    
    public void drawCell(Graphics2D g, double zoom_factor)
    {
        this.zoomFactor = zoom_factor;
        
        
        Color bgcolor = (this.getBackcolor() == null || getMode().equals("Transparent")) ? Color.WHITE : this.getBackcolor();
        
        
        if (getType() == NODATA_CELL)
        {
            g.setColor(new Color( bgcolor.getRed(), bgcolor.getGreen(), bgcolor.getBlue(), 200 ));  
        }
        else
        {
            g.setColor( bgcolor );
        }
        g.fillRect(10 +  getZoomedDim(this.getLeft()), 10 +  getZoomedDim(this.getTop()),
                getZoomedDim(this.getWidth())-1, getZoomedDim(this.getHeight())-1);
                
                
        
        g.setFont( new Font( "Arial", Font.BOLD, 12)); //getZoomedDim(10)));
        // We must center the text..
        //int txt_width = g.getFontMetrics().stringWidth( this.getName() )/2;
        int txt_height = g.getFontMetrics().getHeight();
        txt_height -= g.getFontMetrics().getMaxDescent();
        Color c = (getBackcolor() == null) ? Color.WHITE : getBackcolor();
        g.setColor(new Color(255-c.getRed(),255 - c.getGreen(),255-c.getBlue(),32));

        try {
        Java2DUtil.setClip(g,
                (Math.max(2,getZoomedDim(2))) + 10 +  getZoomedDim(this.getLeft()), 
                (Math.max(2,getZoomedDim(2))) + 10 +  getZoomedDim(this.getTop()),
                getZoomedDim(this.getWidth())-1-(Math.max(2,getZoomedDim(2))), 
                getZoomedDim(this.getHeight())-1-(Math.max(2,getZoomedDim(2))));

        g.drawString( this.getName(),
        (Math.max(2,getZoomedDim(2))) + 10 + getZoomedDim(this.getLeft()),
        (Math.max(2,getZoomedDim(2))) + 10 + getZoomedDim(this.getTop()) + g.getFontMetrics().getAscent());
        
        
        c = (getBackcolor() == null) ? Color.WHITE : getBackcolor();
        g.setColor(new Color(255-c.getRed(),255 - c.getGreen(),255-c.getBlue(),200));

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            Java2DUtil.resetClip(g);
        }
        
        if (isPaintSelection())
        {
            for (int j=1; j<=20; ++j)
            {
                 g.setColor(new Color(255,168,0,j));
                 int i=10;
                 g.fillRect(getZoomedDim(this.getLeft())-i+10, getZoomedDim(this.getTop())-i+10,
                            getZoomedDim(this.getWidth())+(i*2), i);

                 g.fillRect(getZoomedDim(this.getLeft())-i+10, getZoomedDim(this.getTop())+10,
                           i, getZoomedDim(this.getHeight()));

                 g.fillRect(getZoomedDim(this.getLeft())+10-i, getZoomedDim(this.getTop())+getZoomedDim(this.getHeight())+10,
                           getZoomedDim(this.getWidth())+(i*2), i);

                 g.fillRect(getZoomedDim(this.getLeft())+getZoomedDim(this.getWidth())+10, getZoomedDim(this.getTop())+10,
                           i, getZoomedDim(this.getHeight()));
            }
        }

    }
    
    
    /**
     * This method draw the box
     * This method is called after draw elements.
     * 
     */
    public void drawCellBox(Graphics2D g, double zoom_factor, boolean isFirstInColumn, boolean isFirstInRow)
    {
       this.zoomFactor = zoom_factor;
       // int correction = (zoom_factor <= 1) ? -1   : 0;

        Stroke oldStroke = g.getStroke();
        Color  oldColor = g.getColor();

        if (box != null)
        {
            // Left side
            int ax = getZoomedDim(getLeft())+10;
            int ay = getZoomedDim(getTop())+10;
            int bx = ax + getZoomedDim(getWidth());
            int by = ay + getZoomedDim(getHeight());

            Stroke newBoxStroke = null;

            if (box.getLeftBorderColor() != null && (newBoxStroke = ReportElement.getPenStroke(box.getLeftBorder(), zoom_factor)) != null)
            {
                g.setColor(box.getLeftBorderColor());
                if ((newBoxStroke = ReportElement.getPenStroke(box.getLeftBorder(), zoom_factor)) != null)
                {
                    g.setStroke(newBoxStroke);
                    g.drawLine(ax, ay, ax, by);
                }
                
                newBoxStroke = null;
            } 
            else if (isFirstInRow && box.getRightBorderColor() != null)
            {
                g.setColor(box.getRightBorderColor());
                if ((newBoxStroke = ReportElement.getPenStroke(box.getRightBorder(), zoom_factor)) != null)
                {
                    g.setStroke(newBoxStroke);
                    g.drawLine(ax, ay, ax, by);
                }
            }

            if (box.getTopBorderColor() != null && (newBoxStroke = ReportElement.getPenStroke(box.getTopBorder(), zoom_factor)) != null)
            {
                g.setColor(box.getTopBorderColor());
                g.setStroke(newBoxStroke);
                g.drawLine(ax, ay, bx, ay);
            }
            else if (isFirstInColumn && box.getBottomBorderColor() != null)
            {
                g.setColor(box.getBottomBorderColor());
                if ((newBoxStroke = ReportElement.getPenStroke(box.getBottomBorder(), zoom_factor)) != null)
                {
                    g.setStroke(newBoxStroke);
                    g.drawLine(ax, ay, bx, ay);
                }
            }
            
            if (box.getRightBorderColor() != null)
            {
                g.setColor(box.getRightBorderColor());
                if ((newBoxStroke = ReportElement.getPenStroke(box.getRightBorder(), zoom_factor)) != null)
                {
                    g.setStroke(newBoxStroke);
                    g.drawLine(bx, ay, bx, by);
                }
            }
            
            if (box.getBottomBorderColor() != null)
            {
                g.setColor(box.getBottomBorderColor());
                if ((newBoxStroke = ReportElement.getPenStroke(box.getBottomBorder(), zoom_factor)) != null)
                {
                    g.setStroke(newBoxStroke);
                    g.drawLine(ax, by, bx, by);
                }
            }
        }
        g.setStroke(oldStroke);
    }
    
    /**
     *  Return an int.  It performs zoomFactor*dim and a round.
     */
    public int getZoomedDim(int dim) {
        if (getZoomFactor() == 1.0) return dim;
        return (int)((double)dim*getZoomFactor());
    }
    
    /**
     * Cell duplication (elements are not duplicated!)
     *
     **/
    public CrosstabCell cloneMe()
    {
        CrosstabCell cell = new CrosstabCell();
        cell.setTop( this.getTop());
        cell.setLeft( this.getLeft());
        cell.setHeight( this.getHeight());
        cell.setWidth( this.getWidth());
        cell.setBox( this.getBox().cloneMe());
        cell.setBackcolor( this.getBackcolor());
        cell.setColumnTotalGroup( this.getColumnTotalGroup() );
        cell.setRowTotalGroup( this.getRowTotalGroup() );
        cell.setParent( this.getParent());
        cell.setType( this.getType());
        
        return cell;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public int getTopIndex() {
        return topIndex;
    }

    public void setTopIndex(int topIndex) {
        this.topIndex = topIndex;
    }

    public int getBottomIndex() {
        return bottomIndex;
    }

    public void setBottomIndex(int bottomIndex) {
        this.bottomIndex = bottomIndex;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(int leftIndex) {
        this.leftIndex = leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }
    
    public String toString()
    {
        return getName();
    }

    public boolean isPaintSelection() {
        return paintSelection;
    }

    public void setPaintSelection(boolean paintSelection) {
        this.paintSelection = paintSelection;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        if (this.style != style)
        {
            this.style = style;
            if (style != null)
            {
                this.setBackcolor( style.getAttributeColor( style.ATTRIBUTE_backcolor,getBackcolor(), true) );
                this.setMode( style.getAttributeString( style.ATTRIBUTE_mode, getMode(), true) );

                
                // BOX
                if (style.getAttributeString(style.ATTRIBUTE_border, null, true) != null) 
                    this.getBox().setBorder(  style.getAttributeString(style.ATTRIBUTE_border, null, true) );
                if (style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true) != null) 
                    this.getBox().setBorderColor(  style.getAttributeColor(style.ATTRIBUTE_borderColor, null, true));
                if (style.getAttributeString(style.ATTRIBUTE_padding, null, true) != null) 
                    this.getBox().setPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_padding, null, true) ));

                if (style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) != null) 
                    this.getBox().setTopBorder(  style.getAttributeString(style.ATTRIBUTE_topBorder, null, true) );
                if (style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true) != null) 
                    this.getBox().setTopBorderColor(  style.getAttributeColor(style.ATTRIBUTE_topBorderColor, null, true));
                if (style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) != null) 
                    this.getBox().setTopPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_topPadding, null, true) ));

                if (style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) != null) 
                    this.getBox().setLeftBorder(  style.getAttributeString(style.ATTRIBUTE_leftBorder, null, true) );
                if (style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true) != null) 
                    this.getBox().setLeftBorderColor(  style.getAttributeColor(style.ATTRIBUTE_leftBorderColor, null, true));
                if (style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) != null) 
                    this.getBox().setLeftPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_leftPadding, null, true) ));

                if (style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) != null) 
                    this.getBox().setRightBorder(  style.getAttributeString(style.ATTRIBUTE_rightBorder, null, true) );
                if (style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true) != null) 
                    this.getBox().setRightBorderColor(  style.getAttributeColor(style.ATTRIBUTE_rightBorderColor, null, true));
                if (style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) != null) 
                    this.getBox().setRightPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_rightPadding, null, true) ));

                if (style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) != null) 
                    this.getBox().setBottomBorder(  style.getAttributeString(style.ATTRIBUTE_bottomBorder, null, true) );
                if (style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true) != null) 
                    this.getBox().setBottomBorderColor(  style.getAttributeColor(style.ATTRIBUTE_bottomBorderColor, null, true));
                if (style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) != null) 
                    this.getBox().setBottomPadding( Integer.parseInt( style.getAttributeString(style.ATTRIBUTE_bottomPadding, null, true) ));
            }
        }
    }
}
