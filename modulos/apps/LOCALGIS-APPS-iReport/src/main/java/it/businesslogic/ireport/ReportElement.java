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
 * ReportElement.java
 * 
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.Misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;


public class ReportElement extends IReportHashMapBean
{

    private static ImageIcon gripIcon = new ImageIcon( ReportElement.class.getResource("/it/businesslogic/ireport/icons/grip.png"));
        
    static int id_gen = 1;
    
    private int elementID = 0;
    
    /**
     * Key for element properties handled using the IReportHashMapBean
     */
    public static final String BGCOLOR =  "BGCOLOR";
    public static final String FGCOLOR =  "FGCOLOR";
    public static final String MODE =  "MODE"; // transparent or opaque...
    public static final String REMOVE_LINE_WHEN_BLANK =  "REMOVE_LINE_WHEN_BLANK";
    public static final String PRINT_REPEATED_VALUES =  "PRINT_REPEATED_VALUES";
    public static final String PRINT_IN_FIRST_WHOLE_BAND =  "DEFAULT_PRINT_IN_FIRST_WHOLE_BAND";
    public static final String PRINT_WHEN_DETAIL_OVERFLOW =  "PRINT_WHEN_DETAIL_OVERFLOW";
    public static final String POSITION_TYPE =  "POSITION_TYPE";
    
    /**
     * Default values. If a value can change for different elements,
     * it is not listed here (i.e. MODE).
     */
    public static final Color DEFAULT_BGCOLOR =  Color.WHITE;
    public static final Color DEFAULT_FGCOLOR =  Color.BLACK;
    public static final boolean DEFAULT_REMOVE_LINE_WHEN_BLANK = false;
    public static final boolean DEFAULT_PRINT_REPEATED_VALUES =  true;
    public static final boolean DEFAULT_PRINT_IN_FIRST_WHOLE_BAND =  false;
    public static final boolean DEFAULT_PRINT_WHEN_DETAIL_OVERFLOW =  false;
    public static final String DEFAULT_POSITION_TYPE =  "FixRelativeToTop";
    
    /**
     * Values that can be overridden by subclasses....
     */
    public String transparentDefault = "Opaque";
    
    
    public String name;
    public Point position;
    
    /**
     * Relative position is used in crosstab editor only
     **/
    private Point relativePosition; 
    public int width;
    public int height;
    Rectangle bounds;
    public static BufferedImage hached = null;
    public Band band;
    private CrosstabCell cell;
    
    public String printWhenExpression = "";
    //public String positionType = "FixRelativeToTop";
    //public boolean isPrintRepeatedValues = true;
    //public boolean isRemoveLineWhenBlank = false;
    //public boolean isPrintInFirstWholeBand = false;
    //public boolean isPrintWhenDetailOverflows = false;
    
    public static Color lightcolor;
    
    public String printWhenGroupChanges = "";
    double zoom_factor = 1.0;

    protected String stretchType;
    private String elementGroup = "";
    public Vector intersections = new Vector();
    
    private Style style = null;
    
    private ReportElement parentElement = null;

    /**
     * Creates a new ReportElement object.
     * 
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public ReportElement(int x, int y, int width, int height)
    {
        
        if (hached == null)
        {
            hached = Misc.loadBufferedImageFromResources(new java.awt.Panel(), 
                                                         "it/businesslogic/ireport/icons/layout/hached.gif");
        }

        this.position = new Point(x, y);
        this.setRelativePosition(new Point(x, y));
        this.width = Math.abs(width);
        this.height = Math.abs(height);

        bounds = new Rectangle(position.x, position.y, width, height);
        name = "element-" + id_gen;
        setElementID(id_gen);
        id_gen++;
        this.hached = hached;

        if (lightcolor == null)
        {
            String lc = MainFrame.getMainInstance().getProperties().getProperty("NewViewBorderColor");
            try {
                lightcolor = new java.awt.Color(Integer.parseInt( lc ));
            } catch (Exception ex)
            {
                lightcolor = Color.LIGHT_GRAY;
            }
        }

        stretchType = "NoStretch";
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     */
    public void drawObject(Graphics2D g, double zoom_factor, 
                           int x_shift_origin, int y_shift_origin)
    {
        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;

        this.zoom_factor = zoom_factor;

        //g.setPaint( new TexturePaint( red, new Rectangle2D.Double( zoomed_width+10-horizontal_scroll,9-vertical_scroll,9,9)));
        g.fillRect(getZoomedDim(position.x) - x_shift_origin, 
                   getZoomedDim(position.y) - y_shift_origin, 
                   getZoomedDim(width), getZoomedDim(height));

        //g.drawRect(getZoomedDim(position.x)-x_shift_origin, getZoomedDim(position.y)-y_shift_origin, getZoomedDim(width), getZoomedDim(height));
        position.x += 10;
        position.y += 10;
        x_shift_origin += 10;
        y_shift_origin += 10;

        drawGraphicsElement(g, "2Point", zoom_factor, x_shift_origin, 
                            y_shift_origin);
        drawBorder(g, zoom_factor, x_shift_origin, y_shift_origin);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param pen DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     */
    public void drawGraphicsElement(Graphics2D g, String pen, 
                                    double zoom_factor, int x_shift_origin, 
                                    int y_shift_origin)
    {
        drawGraphicsElement(g, pen, zoom_factor, x_shift_origin, 
                            y_shift_origin, 0);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param pen DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     * @param radius DOCUMENT ME!
     */
    public void drawGraphicsElement(Graphics2D g, String pen, 
                                    double zoom_factor, int x_shift_origin, 
                                    int y_shift_origin, int radius)
    {

        int correction = (zoom_factor <= 1 && 
                         (pen.equals("Thin") || pen.equals("1Point") || 
                             pen.equals("Dotted")))
                             ? -1
                             : 0;
        int xy_correction = (zoom_factor <= 1 && pen.equals("Dotted"))
                                ? 1
                                : 0;

        Stroke stroke = getPenStroke(pen, zoom_factor);
        g.setColor(this.getFgcolor());

        this.zoom_factor = zoom_factor;

        if (stroke == null || pen.equalsIgnoreCase("None"))
        {

            return;
        }

        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;

        Stroke oldStroke = g.getStroke();
        g.setStroke(stroke);

        if (radius != 0)
        {
            g.drawRoundRect(
                    getZoomedDim(position.x) - x_shift_origin + 
                    xy_correction, 
                    getZoomedDim(position.y) - y_shift_origin + 
                    xy_correction, getZoomedDim(width) + correction, 
                    getZoomedDim(height) + correction, getZoomedDim(radius), getZoomedDim(radius));
        }
        else
        {
            g.drawRect(getZoomedDim(position.x) - x_shift_origin + 
                       xy_correction, 
                       getZoomedDim(position.y) - y_shift_origin + 
                       xy_correction, getZoomedDim(width) + correction, 
                       getZoomedDim(height) + correction);
        }

        position.x += 10;
        position.y += 10;

        g.setStroke(oldStroke);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     */
    public void drawBorder(Graphics2D g, double zoom_factor, 
                           int x_shift_origin, int y_shift_origin)
    {
        drawBorder(g, zoom_factor, x_shift_origin, y_shift_origin, null);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     * @param box DOCUMENT ME!
     */
    public void drawBorder(Graphics2D g, double zoom_factor, 
                           int x_shift_origin, int y_shift_origin, Box box)
    {
        this.zoom_factor = zoom_factor;

        int correction = -1; //(zoom_factor <= 1)
                             //? -1
                             //: 0;

        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;

        Stroke oldStroke = g.getStroke();

        boolean b_insideBand = insideBand();
        
        if (box == null || !b_insideBand)
        {

            if (b_insideBand)
            {
                g.setColor(lightcolor);
            }
            else
            {
                g.setColor(Color.RED);
            }

            g.drawRect(getZoomedDim(position.x) - x_shift_origin, 
                       getZoomedDim(position.y) - y_shift_origin, 
                       getZoomedDim(width) + correction, 
                       getZoomedDim(height) + correction);
        }
        else
        {

            // Left side
            boolean insideBand = insideBand();
            int ax = getZoomedDim(position.x) - x_shift_origin;
            int ay = getZoomedDim(position.y) - y_shift_origin;
            int bx = ax + getZoomedDim(width) + correction;
            int by = ay + getZoomedDim(height) + correction;

            Stroke newBoxStroke = null;

            if (box.getLeftBorderColor() != null)
            {
                g.setColor(box.getLeftBorderColor());
            }
            else
            {
                g.setColor(lightcolor);
            }

            if ((newBoxStroke = getPenStroke(box.getLeftBorder(), zoom_factor)) != null)
            {
                g.setStroke(newBoxStroke);
                g.drawLine(ax, ay, ax, by);
            }

            //else g.setStroke(oldStroke);
            if (box.getTopBorderColor() != null)
            {
                g.setColor(box.getTopBorderColor());
            }
            else
            {
                g.setColor(lightcolor);
            }

            if ((newBoxStroke = getPenStroke(box.getTopBorder(), zoom_factor)) != null)
            {
                g.setStroke(newBoxStroke);
                g.drawLine(ax, ay, bx, ay);
            }

            if (box.getRightBorderColor() != null)
            {
                g.setColor(box.getRightBorderColor());
            }
            else
            {
                g.setColor(lightcolor);
            }

            if ((newBoxStroke = getPenStroke(box.getRightBorder(), zoom_factor)) != null)
            {
                g.setStroke(newBoxStroke);
                g.drawLine(bx, ay, bx, by);
            }

            if (box.getBottomBorderColor() != null)
            {
                g.setColor(box.getBottomBorderColor());
            }
            else
            {
                g.setColor(lightcolor);
            }

            if ((newBoxStroke = getPenStroke(box.getBottomBorder(), 
                                             zoom_factor)) != null)
            {
                g.setStroke(newBoxStroke);
                g.drawLine(ax, by, bx, by);
            }

            g.setStroke(oldStroke);
        }

        position.x += 10;
        position.y += 10;
        x_shift_origin += 10;
        y_shift_origin += 10;
    }
    
    public static Color getAlphaColor(Color c, int alpha)
    {
        return new Color( c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
    
    public void writeGrip(Graphics2D g,int x, int y)
    {
        g.drawImage(gripIcon.getImage(), x -3, y - 3, gripIcon.getImageObserver());
        g.fillRect(x,y, 5, 5);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param g DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @param x_shift_origin DOCUMENT ME!
     * @param y_shift_origin DOCUMENT ME!
     * @param selected DOCUMENT ME!
     */
    public void drawCorona(Graphics2D g, double zoom_factor, 
                           int x_shift_origin, int y_shift_origin, 
                           boolean selected)
    {
        this.zoom_factor = zoom_factor;

        // draw a  corona...
        position.x -= 10;
        position.y -= 10;
        x_shift_origin -= 10;
        y_shift_origin -= 10;

        
        
        Rectangle rInteger = new Rectangle(
                                getZoomedDim(position.x) - 3 - x_shift_origin, 
                                getZoomedDim(position.y) - 3 - y_shift_origin,
                                getZoomedDim(width) + 6, 
                                getZoomedDim(height) + 6);
        
        Rectangle2D r = new java.awt.geom.Rectangle2D.Double(rInteger.getX(),
                rInteger.getY(),
                rInteger.getWidth(),
                rInteger.getHeight());

        Rectangle2D r2 = new java.awt.geom.Rectangle2D.Double(rInteger.getX()+2.0,
                rInteger.getY()+2.0,
                rInteger.getWidth()-4.0,
                rInteger.getHeight()-4.0);
        
               // new Rectangle2D.Double(
               //                  getZoomedDim(position.x) - x_shift_origin-1, 
               //                  getZoomedDim(position.y) - y_shift_origin-1, 
               //                  getZoomedDim(width) + 2, getZoomedDim(height)+ 2);
        java.awt.geom.Area area = new Area(r);
        area.exclusiveOr(new Area(r2));

        /*
        if (hached == null)
        {
            g.fill(area);
        }
        else
        {
            g.setPaint(new java.awt.TexturePaint(hached, 
                                                 new Rectangle2D.Double(0, 0, 
                                                                        2, 2)));
            g.fill(area);
        }
         */
        
        g.setPaint(new Color(255,175,0, 200) );
        g.fill( area);

        // draw grips...
        g.setPaint( getAlphaColor(Color.BLUE,80));

        if (!selected)
        {
            g.setPaint( getAlphaColor(Color.GRAY,80));
        }

        // checking overlaps slows down the repainting
        if (intersectsElements())
        {

            if (enclosesOtherElement())
            {
                g.setPaint( getAlphaColor(Color.PINK,80));
            }
            else
            {
                g.setPaint( getAlphaColor(Color.GREEN,80));
            }

        }

        if (!insideBand())
        {
            g.setPaint( getAlphaColor(Color.RED,80));
        }

        
        writeGrip(g, getZoomedDim(position.x) - 5 - x_shift_origin, 
                   getZoomedDim(position.y) - 5 - y_shift_origin );
        
        writeGrip(g, getZoomedDim(position.x) - 5 - x_shift_origin, 
                   getZoomedDim(position.y) - 5 - y_shift_origin);
        
        
        writeGrip(g, getZoomedDim(position.x + width) - x_shift_origin, 
                   getZoomedDim(position.y) - 5 - y_shift_origin);
        
        writeGrip(g, getZoomedDim(position.x + width) - x_shift_origin, 
                   getZoomedDim(position.y + height) - y_shift_origin);
        
        writeGrip(g, getZoomedDim(position.x) - 5 - x_shift_origin, 
                   getZoomedDim(position.y + height) - y_shift_origin);

        writeGrip(g, getZoomedDim(position.x + (width / 2)) - 2 - x_shift_origin, 
                   getZoomedDim(position.y) - 5 - y_shift_origin);
        
        writeGrip(g, getZoomedDim(position.x + (width / 2)) - 2 - x_shift_origin, 
                   getZoomedDim(position.y + height) - y_shift_origin);
        
        writeGrip(g, getZoomedDim(position.x) - 5 - x_shift_origin, 
                   getZoomedDim(position.y + (height / 2)) - 2 - 
                   y_shift_origin);
        writeGrip(g, getZoomedDim(position.x + width) - x_shift_origin, 
                   getZoomedDim(position.y + (height / 2)) - 2 - 
                   y_shift_origin);
        
        position.x += 10;
        position.y += 10;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public boolean enclosesOtherElement()
    {

        for (Iterator i = intersections.iterator(); i.hasNext();)
        {

            ReportElement e = (ReportElement) i.next();

            if (bounds.contains(e.bounds))
            {

                return true;
            }
        }

        return false;
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public boolean intersectsElements()
    {

        int oldHeight = 0;
        boolean result = false;

        // do not stop after you found an intersection
        // list them all so that
        // later on you can see whether one is hidden or enclosed by the
        // current element.
        Iterator i = null;
        if (band == null && cell == null) return false;
        if (band != null) i = band.getParent().getElements().iterator();
        else i = cell.getParent().getElements().iterator();
        for (;i.hasNext();)
        {

            ReportElement e = (ReportElement) i.next();

            // do not compare with 'this' reportElement
            if (!this.equals(e))
            {
                oldHeight = e.height;

                // make lines a little heigher,so that they can intersect
                // with a rectangle
                if (e.height == 0)
                {
                    e.height = 10;
                }

                if (e.bounds.intersects(bounds))
                {

                    // store the intersecting rectangle
                    this.intersections.add(e);
                    result = true;
                }

                e.height = oldHeight;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param p DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public boolean intersects(Point p)
    {

        Rectangle r = new Rectangle(bounds);

        if (height == 0)
        {
            r.height = 10;
            r.y -= 5;
        }

        if (width == 0)
        {
            r.width = 10;
            r.x -= 5;
        }

        return r.intersects(p.x, p.y, 1, 1);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public boolean insideBandReal()
    {
        position.x -= 10;
        position.y -= 10;

        boolean result = insideBand();

        position.x += 10;
        position.y += 10;

        return result;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public boolean insideBand()
    {

        if (band == null)
        {
            if (cell != null)
            {
                return cell.getBounds().contains( new Rectangle(getPosition().x,getPosition().y,width,height) );
            }
            return false;
        }

        int yband = band.getBandYLocation();

        // lower left corner of element is below the band.
        // This is not a real problem....
        if (position.y - yband < 0)
        {
         //   return false;
        }

        // lower left corner of element is left from the left margin
        // This is not a real problem
        if (position.x - band.getParent().getLeftMargin() < 0)
        {
         //    return false;
        }

        // with element on the bottom the element is too high for the band
        if (position.y - yband + height > band.getHeight())
        {
            return false;
        }

        // with element on the left margin, the width is larger than usable width of page.
        if (position.x - band.getParent().getLeftMargin() + width > band.getUsableWidth())
        {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param r2 DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public boolean intersects(Rectangle r2)
    {

        Rectangle r = new Rectangle(bounds);

        if (height == 0)
        {
            r.height = 10;
            r.y -= 5;
        }

        if (width == 0)
        {
            r.width = 10;
            r.x -= 5;
        }

        return r.intersects(r2);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param p DOCUMENT ME!
     */
    public void setPosition(Point p)
    {

        if (p == null)
        {

            return;
        }

        if (p.x == position.x && p.y == position.y)
        {

            return;
        }

        position.x = p.x;
        position.y = p.y;
        bounds = new Rectangle(position.x, position.y, width, height);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param delta DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public Point trasform(Point delta, int type)
    {

        if (delta == null)
        {

            return null;
        }

        Point result = new Point(delta);
        int old_x = 0;
        int old_y = 0;

        if (type == TransformationType.TRANSFORMATION_MOVE)
        {
            position.x += delta.x;
            position.y += delta.y;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_E)
        {
            old_x = width;
            width += delta.x;

            if (width < 0)
            {
                width = 0;
            }

            result.x = width - old_x;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_W)
        {
            old_x = width;

            int d = Math.min(delta.x, width);
            width -= d;
            position.x += d;
            result.x = d;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_N)
        {

            int d = Math.min(delta.y, height);
            height -= d;
            position.y += d;
            result.y = d;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_S)
        {
            old_y = height;
            height += delta.y;

            if (height < 0)
            {
                height = 0;
            }

            result.y = height - old_y;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_SE)
        {
            old_y = height;
            old_x = width;
            height += delta.y;

            if (height < 0)
            {
                height = 0;
            }

            width += delta.x;

            if (width < 0)
            {
                width = 0;
            }

            result.x = width - old_x;
            result.y = height - old_y;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_SW)
        {
            old_y = height;
            height += delta.y;

            if (height < 0)
            {
                height = 0;
            }

            int d = Math.min(delta.x, width);
            width -= d;
            position.x += d;
            result.x = d;
            result.y = height - old_y;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_NE)
        {
            old_x = width;

            int d = Math.min(delta.y, height);
            height -= d;
            position.y += d;
            width += delta.x;

            if (width < 0)
            {
                width = 0;
            }

            result.x = width - old_x;
            result.y = d;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_NW)
        {

            int d = Math.min(delta.y, height);
            height -= d;
            position.y += d;
            result.y = d;
            d = Math.min(delta.x, width);
            width -= d;
            position.x += d;
            result.x = d;
        }

        bounds = new Rectangle(position.x, position.y, width, height);
        
        if (getCell() != null && type != TransformationType.TRANSFORMATION_RESIZE_SE)
        {
           setRelativePosition( new Point(getPosition().x - getCell().getLeft()- 10,  getPosition().y - getCell().getTop()- 10)); 
        }
        
        return result;
    }

    /**
     * Try to move the element to another band if the element is placed
     * entirely within the band.
     * 
     */
    public void adjustBand()
    {
        if (band == null) return;
        position.x -= 10;
        position.y -= 10;

        for (Iterator i = band.getParent().getBands().iterator(); i.hasNext();)
        {

            Band b = (Band) i.next();

            if (position.y - band.getParent().getBandYLocation(b) >= 0)
            {

                if (position.y - band.getParent().getBandYLocation(b) + 
                    height <= b.getHeight())
                {

                    // element is within this band.
                    band = b;

                    break;
                }
            }
        }

        position.x += 10;
        position.y += 10;
    }

    
    public void adjustCell(Vector cells)
    {
        if (getCell().getType() == CrosstabCell.NODATA_CELL) return;
        for (int i=0; i<cells.size(); ++i)
        {
            CrosstabCell tmpCell = (CrosstabCell)cells.elementAt(i);
            if (tmpCell.getType() == CrosstabCell.NODATA_CELL) continue;
            if ( tmpCell.getBounds().contains( new Point(getPosition().x-10,getPosition().y-10) ) )
            {
                this.setCell( tmpCell );
                setRelativePosition(new Point( getPosition().x - getCell().getLeft() - 10, getPosition().y - getCell().getTop() - 10 ));
                return;
            }
        }
    }
    
    
    /**
     * this methos adjust the relativo position respect to the parent cell.
     * If parent cell is null, nothing is done.
     * You should call this method after a brute setPosition
     */
    public void updateRelativePosition()
    {
        if (getCell() == null) return;
        setRelativePosition(new Point( getPosition().x - getCell().getLeft() - 10, getPosition().y - getCell().getTop() - 10 ));
    }
    
    
    /*
     *  This method should be called when you modify height, width,
     *  or position manually.
     */

    /**
     * DOCUMENT ME!
     */
    public void updateBounds()
    {
        bounds = new Rectangle(position.x, position.y, width, height);
    }
    
    
    /**
     * DOCUMENT ME!
     * 
     * @param delta DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public Point trasformTest(Point delta, int type)
    {

        if (delta == null)
        {

            return null;
        }

        Point result = new Point(delta);
        int old_x = 0;
        int old_y = 0;

        if (type == TransformationType.TRANSFORMATION_MOVE)
        {
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_E)
        {
            old_x = width;
            old_x += delta.x;

            if (old_x < 0)
            {
                old_x = 0;
            }

            result.x = old_x - width;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_W)
        {
            result.x = Math.min(delta.x, width);
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_N)
        {
            result.y = Math.min(delta.y, height);
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_S)
        {
            old_y = height;
            old_y += delta.y;

            if (old_y < 0)
            {
                old_y = 0;
            }

            result.y = old_y - height;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_SE)
        {
            old_y = height;
            old_x = width;
            old_y += delta.y;

            if (old_y < 0)
            {
                old_y = 0;
            }

            old_x += delta.x;

            if (old_x < 0)
            {
                old_x = 0;
            }

            result.x = old_x - width;
            result.y = old_y - height;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_SW)
        {
            old_y = height;
            old_y += delta.y;

            if (old_y < 0)
            {
                old_y = 0;
            }

            result.x = Math.min(delta.x, width);
            result.y = old_y - height;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_NE)
        {
            old_x = width;
            result.y = Math.min(delta.y, height);
            old_x += delta.x;

            if (old_x < 0)
            {
                old_x = 0;
            }

            result.x = old_x - width;
        }
        else if (type == TransformationType.TRANSFORMATION_RESIZE_NW)
        {
            result.y = Math.min(delta.y, height);
            result.x = Math.min(delta.x, width);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public String toString()
    {
        if (band == null) return ""+name;
        else
        {
            if (getParentElement() == null)
            {
                return name + " [" + 
                   (position.x - band.getParent().getRightMargin() - 10) + "," + 
                   (position.y - band.getBandYLocation() - 10) + "]";
            }
            else
            {
                return name + " [" + 
                   (position.x - getParentElement().getPosition().x) + "," + 
                   (position.y - getParentElement().getPosition().y) + "]";
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param dim DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public int getZoomedDim(int dim)
    {

        if (zoom_factor == 1.0)
        {

            return dim;
        }

        //if (((double)dim*(double)zoom_factor)<0.5) return 1;
        // Truncate, don't round!!
        return (int) ((double) dim * zoom_factor);

        //return (int)Math.ceil((double)dim*zoom_factor);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param dim DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public double getZoomedDim(double dim)
    {

        if (zoom_factor == 1.0)
        {

            return dim;
        }

        //if (((double)dim*(double)zoom_factor)<0.5) return 1;
        // Truncate, don't round!!
        return ((double) dim * zoom_factor);

        //return (int)Math.ceil((double)dim*zoom_factor);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param dim DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public int getLogicalDim(int dim)
    {

        if (zoom_factor == 1.0)
        {

            return dim;
        }

        //if (Math.abs(  ((double)dim/(double)zoom_factor)) < 1 &&
        //   Math.abs(  ((double)dim/(double)zoom_factor)) > 0) return 1;
        // Truncate, don't round!!
        return (int) ((double) dim / zoom_factor);

        //return (int)Math.ceil((double)dim/zoom_factor);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public ReportElement cloneMe()
    {

        ReportElement newReportElement = new ReportElement(position.x, 
                                                           position.y, width, 
                                                           height);
        newReportElement.name = new String(name);
        newReportElement.band = band;
        newReportElement.cell = cell;
        newReportElement.parentElement = parentElement;
        
        copyElementPropertiesTo(newReportElement);

        return newReportElement;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param newReportElement DOCUMENT ME!
     */
    public void copyElementPropertiesTo(ReportElement newReportElement)
    {
        newReportElement.printWhenExpression = printWhenExpression;

        newReportElement.setPropertyValue(POSITION_TYPE, this.getPropertyValue(POSITION_TYPE)); 
        newReportElement.setPropertyValue(REMOVE_LINE_WHEN_BLANK, this.getPropertyValue(REMOVE_LINE_WHEN_BLANK)); 
        newReportElement.setPropertyValue(PRINT_REPEATED_VALUES, this.getPropertyValue(PRINT_REPEATED_VALUES)   );
        newReportElement.setPropertyValue(PRINT_IN_FIRST_WHOLE_BAND, this.getPropertyValue(PRINT_IN_FIRST_WHOLE_BAND));
        newReportElement.setPropertyValue(PRINT_WHEN_DETAIL_OVERFLOW, this.getPropertyValue(PRINT_WHEN_DETAIL_OVERFLOW));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     * @param s3 DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public static String string_replace(String s1, String s2, String s3)
    {

        String string = "";
        string = "";

        if (s2 == null || s3 == null || s2.length() == 0)
        {

            return s3;
        }

        int pos_i = 0; // posizione corrente.
        int pos_f = 0; // posizione corrente finale

        int len = s2.length();

        while ((pos_f = s3.indexOf(s2, pos_i)) >= 0)
        {
            string += s3.substring(pos_i, pos_f) + s1;

            //+string.substring(pos+ s2.length());
            pos_f = pos_i = pos_f + len;

        }

        string += s3.substring(pos_i);

        return string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param pen DOCUMENT ME!
     * @param zoom_factor DOCUMENT ME!
     * @return DOCUMENT ME! 
     */
    public static Stroke getPenStroke(String pen, double zoom_factor)
    {

        if (pen == null || pen.equals("None"))
        {

            return null;
        }

        if (pen.equals("Dotted"))
        {

            return (Stroke) new BasicStroke((float) (1f * zoom_factor), 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 5f, 3f }, 0f);
        }
        else if (pen.equals("2Point"))
        {

            return (Stroke) new BasicStroke((float) (2f * zoom_factor));
        }
        else if (pen.equals("3Point"))
        {

            return (Stroke) new BasicStroke((float) (3f * zoom_factor));
        }
        else if (pen.equals("4Point"))
        {

            return (Stroke) new BasicStroke((float) (4f * zoom_factor));
        }
        else if (pen.equals("Thin"))
        {

            return (Stroke) new BasicStroke((float) (1f * zoom_factor));
        }
        else //if (pen.equals("1Point"))
        {

            return (Stroke) new BasicStroke((float) (1f * zoom_factor));
        }
    }

    /**
     * Getter for property width.
     * 
     * @return Value of property width.
     */
    public int getWidth()
    {

        return width;
    }

    /**
     * Setter for property width.
     * 
     * @param width New value of property width.
     */
    public void setWidth(int width)
    {
        this.width = width;
        updateBounds();
    }

    /**
     * Getter for property height.
     * 
     * @return Value of property height.
     */
    public int getHeight()
    {

        return height;
    }

    /**
     * Setter for property height.
     * 
     * @param height New value of property height.
     */
    public void setHeight(int height)
    {
        this.height = height;
        updateBounds();
    }

    /**
     * Getter for property position.
     * 
     * @return Value of property position.
     */
    public java.awt.Point getPosition()
    {

        return position;
    }

    /**
     * Getter for property band.
     * 
     * @return Value of property band.
     */
    public it.businesslogic.ireport.Band getBand()
    {

        return band;
    }

    /**
     * Setter for property band.
     * 
     * @param band New value of property band.
     */
    public void setBand(it.businesslogic.ireport.Band band)
    {
        this.band = band;
    }

    /**
     * Getter for property transparent.
     * 
     * @return Value of property transparent.
     */
    public java.lang.String getTransparent()
    {

        if (getStringValue(MODE, null ) == null)
        {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_mode, transparentDefault , true);
            }
        }
        return getStringValue(MODE, transparentDefault );
    }
    
    /**
     * Setter for property transparent.
     * 
     * @param transparent New value of property transparent.
     */
    public void setTransparent(java.lang.String transparent)
    {
        setPropertyValue(MODE,transparent);
    }

    /**
     * Getter for property positionType.
     * 
     * @return Value of property positionType.
     */
    public java.lang.String getPositionType()
    {

        return getStringValue(POSITION_TYPE,DEFAULT_POSITION_TYPE);
    }

    /**
     * Setter for property positionType.
     * 
     * @param positionType New value of property positionType.
     */
    public void setPositionType(java.lang.String positionType)
    {
        setPropertyValue(POSITION_TYPE,positionType);
    }

    /**
     * Getter for property isPrintInFirstWholeBand.
     * 
     * @return Value of property isPrintInFirstWholeBand.
     */
    public boolean isIsPrintInFirstWholeBand()
    {

        return getBooleanValue(PRINT_IN_FIRST_WHOLE_BAND, DEFAULT_PRINT_IN_FIRST_WHOLE_BAND );
    }

    /**
     * Setter for property isPrintInFirstWholeBand.
     * 
     * @param isPrintInFirstWholeBand New value of property
     *        isPrintInFirstWholeBand.
     */
    public void setIsPrintInFirstWholeBand(boolean isPrintInFirstWholeBand)
    {
        setPropertyValue(PRINT_IN_FIRST_WHOLE_BAND,  ""+isPrintInFirstWholeBand  );
    }

    /**
     * Getter for property printWhenExpression.
     * 
     * @return Value of property printWhenExpression.
     */
    public java.lang.String getPrintWhenExpression()
    {

        return printWhenExpression;
    }

    /**
     * Setter for property printWhenExpression.
     * 
     * @param printWhenExpression New value of property printWhenExpression.
     */
    public void setPrintWhenExpression(java.lang.String printWhenExpression)
    {
        this.printWhenExpression = printWhenExpression;
    }

    /**
     * Getter for property isPrintRepeatedValues.
     * 
     * @return Value of property isPrintRepeatedValues.
     */
    public boolean isIsPrintRepeatedValues()
    {

        return getBooleanValue(PRINT_REPEATED_VALUES, DEFAULT_PRINT_REPEATED_VALUES );
    }

    /**
     * Setter for property isPrintRepeatedValues.
     * 
     * @param isPrintRepeatedValues New value of property
     *        isPrintRepeatedValues.
     */
    public void setIsPrintRepeatedValues(boolean isPrintRepeatedValues)
    {
        setPropertyValue(PRINT_REPEATED_VALUES,  ""+isPrintRepeatedValues  );
    }

    /**
     * Getter for property fgcolor.
     * 
     * @return Value of property fgcolor.
     */
    public java.awt.Color getFgcolor()
    {
        if (getColorValue(FGCOLOR, null ) == null)
        {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeColor( getStyle().ATTRIBUTE_forecolor, DEFAULT_FGCOLOR, true);
            }
        }
        return getColorValue(FGCOLOR, DEFAULT_FGCOLOR );
    }

    /**
     * Setter for property fgcolor.
     * 
     * @param fgcolor New value of property fgcolor.
     */
    public void setFgcolor(java.awt.Color fgcolor)
    {
        setPropertyValue(FGCOLOR,fgcolor);
    }

    /**
     * Getter for property bgcolor.
     * 
     * @return Value of property bgcolor.
     */
    public java.awt.Color getBgcolor()
    {

        if (getColorValue(BGCOLOR, null ) == null)
        {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeColor( getStyle().ATTRIBUTE_backcolor, DEFAULT_BGCOLOR, true);
            }
        }
        return getColorValue(BGCOLOR, DEFAULT_BGCOLOR );
    }

    /**
     * Setter for property bgcolor.
     * 
     * @param bgcolor New value of property bgcolor.
     */
    public void setBgcolor(java.awt.Color bgcolor)
    {
        setPropertyValue(BGCOLOR,bgcolor);
    }

    /**
     * Getter for property isRemoveLineWhenBlank.
     * 
     * @return Value of property isRemoveLineWhenBlank.
     */
    public boolean isIsRemoveLineWhenBlank()
    {

        return getBooleanValue(REMOVE_LINE_WHEN_BLANK, DEFAULT_REMOVE_LINE_WHEN_BLANK );
    }

    /**
     * Setter for property isRemoveLineWhenBlank.
     * 
     * @param isRemoveLineWhenBlank New value of property
     *        isRemoveLineWhenBlank.
     */
    public void setIsRemoveLineWhenBlank(boolean isRemoveLineWhenBlank)
    {
        setPropertyValue(REMOVE_LINE_WHEN_BLANK,  ""+isRemoveLineWhenBlank  );
    }

    /**
     * Getter for property printWhenGroupChanges.
     * 
     * @return Value of property printWhenGroupChanges.
     */
    public java.lang.String getPrintWhenGroupChanges()
    {

        return printWhenGroupChanges;
    }

    /**
     * Setter for property printWhenGroupChanges.
     * 
     * @param printWhenGroupChanges New value of property
     *        printWhenGroupChanges.
     */
    public void setPrintWhenGroupChanges(java.lang.String printWhenGroupChanges)
    {
        this.printWhenGroupChanges = printWhenGroupChanges;
    }

    /**
     * Getter for property name.
     * 
     * @return Value of property name.
     */
    public java.lang.String getName()
    {

        return name;
    }

    /**
     * Setter for property name.
     * 
     * @param name New value of property name.
     */
    public void setName(java.lang.String name)
    {
        this.name = name;
    }

    /**
     * Getter for property isPrintWhenDetailOverflows.
     * 
     * @return Value of property isPrintWhenDetailOverflows.
     */
    public boolean isIsPrintWhenDetailOverflows()
    {

        return getBooleanValue(PRINT_WHEN_DETAIL_OVERFLOW, DEFAULT_PRINT_WHEN_DETAIL_OVERFLOW );
    }

    /**
     * Setter for property isPrintWhenDetailOverflows.
     * 
     * @param isPrintWhenDetailOverflows New value of property
     *        isPrintWhenDetailOverflows.
     */
    public void setIsPrintWhenDetailOverflows(boolean isPrintWhenDetailOverflows)
    {
        setPropertyValue(PRINT_WHEN_DETAIL_OVERFLOW,  ""+isPrintWhenDetailOverflows  );
    }

    /**
     * DOCUMENT ME!
     * 
     * @param destination DOCUMENT ME!
     * @param source DOCUMENT ME!
     */
    public void copyBaseReportElement(ReportElement destination, 
                                      ReportElement source)
    {

        // Canonical report elements....
        destination.setName(new String(source.getName()));

        destination.setPropertyValue(FGCOLOR, this.getPropertyValue(FGCOLOR)); 
        destination.setPropertyValue(BGCOLOR, this.getPropertyValue(BGCOLOR)); 
        
        destination.setBand(source.getBand());
        destination.setCell(source.getCell());
        destination.setPositionType(new String(source.getPositionType()));
        
        destination.setPropertyValue(REMOVE_LINE_WHEN_BLANK, this.getPropertyValue(REMOVE_LINE_WHEN_BLANK)); 
        destination.setPropertyValue(PRINT_REPEATED_VALUES, this.getPropertyValue(PRINT_REPEATED_VALUES)   );
        destination.setPropertyValue(PRINT_IN_FIRST_WHOLE_BAND, this.getPropertyValue(PRINT_IN_FIRST_WHOLE_BAND));
        destination.setPropertyValue(PRINT_WHEN_DETAIL_OVERFLOW, this.getPropertyValue(PRINT_WHEN_DETAIL_OVERFLOW));
        destination.setPropertyValue(MODE, this.getPropertyValue(MODE));
        destination.setStretchType( source.getStretchType());
        
        destination.setStyle( this.getStyle() );
        
        destination.setPrintWhenExpression(new String(source.getPrintWhenExpression()));
        destination.setPrintWhenGroupChanges(new String(source.getPrintWhenGroupChanges()));

    }

    /**
     * Getter for property key.
     * 
     * @return Value of property key.
     */
    public java.lang.String getKey()
    {

        return name;
    }

    /**
     * Setter for property key.
     * 
     * @param key New value of property key.
     */
    public void setKey(java.lang.String key)
    {
        this.name = key;
    }

    /**
     * Getter for property stretchType.
     * 
     * @return Value of property stretchType.
     */
    public java.lang.String getStretchType()
    {

        return stretchType;
    }

    /**
     * Setter for property stretchType.
     * 
     * @param stretchType New value of property stretchType.
     */
    public void setStretchType(java.lang.String stretchType)
    {
        this.stretchType = stretchType;
    }

    /**
     * Getter for property bounds.
     * 
     * @return Value of property bounds.
     */
    public java.awt.Rectangle getBounds()
    {

        return bounds;
    }

    /**
     * Setter for property bounds.
     * 
     * @param bounds New value of property bounds.
     */
    public void setBounds(java.awt.Rectangle bounds)
    {
        this.position.x = bounds.x;
        this.position.y = bounds.y;
        this.width = bounds.width;
        this.height = bounds.height;
        this.updateBounds();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param groupname DOCUMENT ME!
     */
    public void addToGroup(String groupname)
    {

        if (getElementGroup() == null || getElementGroup().equals(""))
        {
            setElementGroup(getElementGroup() + groupname);
        }
        else
        {
            setElementGroup(groupname + "." + getElementGroup());
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param groupname DOCUMENT ME!
     */
    public void removeFromGroup(String groupname)
    {

        if (getElementGroup() == null || getElementGroup().equals(""))
        {

            return;
        }

        if (getElementGroup().startsWith(groupname))
        {
            setElementGroup(getElementGroup().substring(groupname.length()));

            if (getElementGroup().startsWith("."))
            {
                setElementGroup(getElementGroup().substring(1));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void removeFromAllGroups()
    {
        setElementGroup("");
    }

    public String getElementGroup() {
        return elementGroup;
    }

    public void setElementGroup(String elementGroup) {
        this.elementGroup = elementGroup;
    }

    public ReportElement getParentElement() {
        return parentElement;
    }

    public void setParentElement(ReportElement parentElement) {
        this.parentElement = parentElement;
    }

    public int getElementID() {
        return elementID;
    }

    public void setElementID(int elementID) {
        this.elementID = elementID;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        if (style != null)
        {
            //this.setBgcolor( style.getAttributeColor( style.ATTRIBUTE_backcolor,getBgcolor(), true) );
            //this.setFgcolor( style.getAttributeColor( style.ATTRIBUTE_forecolor,getFgcolor(), true) );
            //this.setTransparent( style.getAttributeString( style.ATTRIBUTE_mode, getTransparent(), true) );
        }
    }

    public CrosstabCell getCell() {
        return cell;
    }

    public void setCell(CrosstabCell cell) {
        this.cell = cell;
    }

    public Point getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Point relativePosition) {
        this.relativePosition = relativePosition;
    }

}
