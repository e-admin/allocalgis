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
 * JRulePanel.java
 * 
 * Created on 12 febbraio 2003, 22.52
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.util.Unit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author  Administrator
 */
public class JRulePanel extends JPanel  {
    
    public static int TYPE_HORIZONTAL = 0;
    public static int TYPE_VERTICAL = 1;
    
    private boolean fontSet = false;
    private JReportFrame jReportFrame = null;
    
    private int type = TYPE_HORIZONTAL;
    
    private int cursorPosition;    
    
    private boolean dragging = false;
    private java.awt.image.BufferedImage savedImage = null;
    private ImageIcon horizontalRuleStopIcon = null;
    
    private java.util.List guideLines = new java.util.ArrayList();
    private int lastTempGuidePosition = -1; 
    private Stroke dottedStroke = null;

    
    /** Creates a new instance of JRulePane */
    public JRulePanel() {
        setCursorPosition(-1);
        Font f = this.getFont();
        this.setFont( new Font( f.getName(), 0, 10)); //f.getFamily())
        
        setDottedStroke((Stroke) new BasicStroke((float) (1f), 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_BEVEL, 0f, 
                                            new float[] { 1f, 1f }, 0f));
                                            
        horizontalRuleStopIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/rulestop.png"));
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                panelMouseMoved(evt);
            }
        });
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                panelMouseReleased(evt);
            }
        });
    }           
               
    public void setRightFont(Graphics g)
    {
        if (fontSet) return;
        
        if (g == null) return;
        /*
        Font f = g.getFont();
        f = new Font( f.getName(), 0, 10); //f.getFamily()
        g.setFont(f);
        */
         //setFont(f); //f.getFamily()
        //fontSet = true;
    }

    /**
     * Paints the container. This forwards the paint to any lightweight
     * components that are children of this container. If this method is
     * reimplemented, super.paint(g) should be called so that lightweight
     * components are properly rendered. If a child component is entirely
     * clipped by the current clipping setting in g, paint() will not be
     * forwarded to that child.
     *
     * @param g the specified Graphics window
     * @see   Component#update(Graphics)
     *
     *    0     10    x1                                   x2    x3
     *    +-----+-----+------------------------------------+-----+-------
     *    |     |:::::|::::::::::::::::::::::::::::::::::::|:::::|
     *    |     |:::::|::::::::::::::::::::::::::::::::::::|:::::|
     *
     *    <============= scroll_h =========================================>
     *     
     *    x1 = Margin left position
     *    x2 = Margin right position
     *    x3 = Page width position
     *    scroll_h = horizontal position of the scroll bar.
     *    
     */
    public void paint(Graphics g) {
        
        if (getJReportFrame() == null) return;
        setRightFont(g);
        g.setFont( this.getFont());
        setCursorPosition(-1);
        
        //g.setColor(new Color( 255,255,255));
        g.setColor(this.getBackground());
        g.fillRect(0,0, this.getWidth(), this.getHeight());

        String unitName = MainFrame.getMainInstance().getProperties().getProperty("DefaultUnit","cm");
        double unit = Unit.CENTIMETERS;
        if (unitName.equals("cm")) unit = Unit.CENTIMETERS;
        else if (unitName.equals("pixels")) unit = Unit.PIXEL;
        else if (unitName.equals("mm")) unit = Unit.MILLIMETERS;
        else if (unitName.equals("inches")) unit = Unit.INCHES;

        g.setColor(new Color( 255,255,255));


        double k=0;

        double zoomfactor = getJReportFrame().getZoomFactor();   

        int line=0;
        int i=0;
        //int i = 10-HScrollBar1.getValue();
        int oldi=-100;
        double module = 2;

        boolean isMillimeters = (unit == Unit.MILLIMETERS);
        if (isMillimeters)
        {
            unit = Unit.CENTIMETERS;
        }

        boolean isPixel = false;
        // Choose module...
        int tick_space = 50;
        if (unit == Unit.PIXEL)
        {
            isPixel = true;
            unit = 50;
            tick_space = 100;
        }

        if (((int)(convertUnitToPixel(1,unit)*zoomfactor)) >= tick_space)
        {
            module = 10;
        }

        if (getType() == TYPE_HORIZONTAL)
        {
                int x1=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getLeftMargin()) - getJReportFrame().getHScroll();
                int x2=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getWidth() - getJReportFrame().getReport().getRightMargin() ) - getJReportFrame().getHScroll();
                int x3=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getWidth()) - getJReportFrame().getHScroll()-1;

                g.fillRect(Math.max(x1,0) ,0, Math.min(x2, this.getWidth()) - Math.max(x1,0), this.getHeight());

                java.awt.Color c = this.getBackground().darker();
                if (x1 > 0)
                {
                    g.setColor(c);
                    g.fillRect(Math.max(10 - getJReportFrame().getHScroll(),0), 0, x1-Math.max(10 - getJReportFrame().getHScroll(),0), this.getHeight());        
                    g.setColor(c.darker());
                    g.drawRect(Math.max(10 - getJReportFrame().getHScroll(),0), 0, x1-Math.max(10 - getJReportFrame().getHScroll(),0), this.getHeight());    
                }

                if (x2 < this.getWidth())
                {
                    g.setColor(c);
                    g.fillRect(x2, 0, Math.min(this.getWidth(), x3) - x2, this.getHeight());        
                    g.setColor(c.darker());
                    g.drawRect(x2, 0, Math.min(this.getWidth(), x3) - x2, this.getHeight());    
                }

                g.setColor(new Color(0,0,0));
                g.drawLine(0,this.getHeight()-1, this.getWidth(), this.getHeight()-1);

                i = x1;


                while (i< this.getWidth())
                {
                        if (i>=0)
                        {
                                if ((line%module)==0)
                                {
                                        if (i-oldi > 20)
                                        {
                                                String s = ""+(int)k;
                                                if (isMillimeters) s += "0";
                                                if (isPixel) s = ""+((int)k*50);

                                                int w = g.getFontMetrics().stringWidth(s);
                                                g.drawString(s ,i - (w/2), (g.getFontMetrics().getHeight()/2)+3);
                                                //writeRotateString((Graphics2D)g,i - (w/2), (g.getFontMetrics().getHeight()/2)+3, s);
                                                oldi= i;
                                                g.drawLine(i,16,i,12);
                                        }
                                        else
                                        {
                                            g.drawLine(i,5,i,10);
                                        }

                                }
                                else 
                                {
                                    if (module == 10 && (line%5) !=0)
                                    {
                                        g.drawLine(i,7,i,8);
                                    }
                                    else
                                    {
                                        g.drawLine(i,6,i,9);
                                    }
                                }
                        }
                        line++;
                        k = line*(1.0/module);
                        i = x1 + (int)(convertUnitToPixel(k,unit) * zoomfactor);	


                }


                line=1;
                k = 1.0/module;
                oldi=x1;
                i = x1 - (int)(convertUnitToPixel(k,unit) * zoomfactor);
                while (x1 > 0 && i > -10)
                {
                        if (i>=0)
                        {
                                if ((line%module)==0)
                                {
                                        if (oldi-i > 20)
                                        {
                                                String s = ""+(int)k;
                                                if (isMillimeters) s += "0";
                                                if (isPixel) s = ""+((int)k*50);

                                                int w = g.getFontMetrics().stringWidth(s);
                                                g.drawString(s ,i - (w/2), (g.getFontMetrics().getHeight()/2)+3);
                                                oldi= i;
                                                g.drawLine(i,16,i,12);
                                        }
                                        else
                                        {
                                            g.drawLine(i,5,i,10);
                                        }

                                }
                                else 
                                {
                                    if (module == 10 && (line%5) !=0)
                                    {
                                        g.drawLine(i,7,i,8);
                                    }
                                    else
                                    {
                                        g.drawLine(i,6,i,9);
                                    }
                                }
                        }
                        line++;
                        k = line*(1.0/module);
                        i = x1 - (int)(convertUnitToPixel(k,unit) * zoomfactor);			
                }
                
                for (i=0; i<getGuideLines().size(); ++i)
                {
                    Integer pos = (Integer)getGuideLines().get(i);
                    int posI = pos.intValue();
                    // Calc posI....
                    posI = 10 + (int)(posI*zoomfactor) - getJReportFrame().getHScroll();
                    g.drawImage(horizontalRuleStopIcon.getImage(),posI-4, 7, this);
                }

        } 
        else   // VERTICAL.....
        {
                int y1=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getTopMargin()) - getJReportFrame().getVScroll();
                int y2=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getDesignHeight() - getJReportFrame().getReport().getBottomMargin() ) - getJReportFrame().getVScroll();
                int y3=10 + getJReportFrame().getZoomedDim( getJReportFrame().getReport().getDesignHeight()) - getJReportFrame().getVScroll()-1;

                g.fillRect(0, Math.max(y1,0), this.getWidth(), Math.min(y2, this.getHeight()) - Math.max(y1,0));

                java.awt.Color c = this.getBackground().darker();
                if (y1 > 0)
                {
                    g.setColor(c);
                    g.fillRect(0, Math.max(10 - getJReportFrame().getVScroll(),0), this.getWidth(), y1-Math.max(10 - getJReportFrame().getVScroll(),0));        
                    g.setColor(c.darker());
                    g.drawRect(0, Math.max(10 - getJReportFrame().getVScroll(),0), this.getWidth(), y1-Math.max(10 - getJReportFrame().getVScroll(),0));           
                }

                
                if (y2 < this.getHeight())
                {
                    g.setColor(c);
                    g.fillRect(0,y2, this.getWidth(), Math.min(this.getHeight(), y3) - y2);        
                    g.setColor(c.darker());
                    g.drawRect(0,y2, this.getWidth(), Math.min(this.getHeight(), y3) - y2);           
                }

                // Write final gray....
                if (y2 < this.getHeight())
                {
                    g.setColor(c);
                    g.fillRect(0,y2, this.getWidth(), Math.min(this.getHeight(), y3) - y2);        
                    g.setColor(c.darker());
                    g.drawRect(0,y2, this.getWidth(), Math.min(this.getHeight(), y3) - y2);           
                }
                
                g.setColor(new Color(0,0,0));
                g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight());
                     
                Iterator iterBands = getJReportFrame().getReport().getBands().iterator();
                
                // Draw bands colors...
                Color mygray = new Color(220,220,220);
                boolean useWhite = true;
                while (iterBands.hasNext())
                {
                     Band currentBand = (Band)iterBands.next();
                     if (currentBand.getHeight() == 0) continue;
                     
                     int bandStartY = getJReportFrame().getReport().getBandYLocation(currentBand);
                     bandStartY = 10 + getJReportFrame().getZoomedDim(bandStartY) - getJReportFrame().getVScroll();  
                     
                     int bandEndY = getJReportFrame().getReport().getBandYLocation(currentBand) + currentBand.getHeight();
                     bandEndY = 10 + getJReportFrame().getZoomedDim(bandEndY) - getJReportFrame().getVScroll();  
                     
                     if (bandStartY < this.getHeight() && bandEndY > 0)
                     {
                        g.setColor( ( useWhite ) ? Color.WHITE : mygray);
                        g.fillRect(0, bandStartY, this.getWidth(), bandEndY - bandStartY);     
                     }
                     useWhite = !useWhite;
                     
                     g.setColor( Color.BLACK);
                     // Write thicks between bandStartY and bandEndY...
                     i=bandStartY;
                     line=0;
                     k = 1.0/module;
                     oldi=-100;
                     
                     while (i<bandEndY)
                     {

                            if (i>=0)
                            {
                                    if ((line%module)==0)
                                    {
                                            if (i-oldi > 20)
                                            {
                                                    String s = ""+(int)k;
                                                    if (isMillimeters) s += "0";
                                                    if (isPixel) s = ""+((int)k*50);
                                                    
                                                    if (bandEndY-i > 8)
                                                    {
                                                        writeRotateString((Graphics2D)g, 4, i, s);
                                                    }
                                                    //g.drawString(s ,1, i + g.getFontMetrics().getDescent());
                                                    oldi= i;
                                                    g.drawLine(16,i,12,i);
                                            }
                                            else
                                            {
                                                g.drawLine(5,i,10,i);
                                            }

                                    }
                                    else 
                                    {
                                        if (module == 10 && (line%5) !=0)
                                        {
                                            g.drawLine(7,i,8,i);
                                        }
                                        else
                                        {
                                            g.drawLine(6,i,9,i);
                                        }
                                    }
                            }
                            line++;
                            k = line*(1.0/module);
                            i = bandStartY + (int)(convertUnitToPixel(k,unit) * zoomfactor);	
                    }
                     
                     if (bandEndY > this.getHeight()) break;
                }
                
                // Draw the lines on top of the page limit...
                line=1;
                k = 1.0/module;
                oldi=y1;
                
                g.setColor(Color.BLACK);
                
                i = y1 - (int)(convertUnitToPixel(k,unit) * zoomfactor);
                while (y1 > 0 && i > -10)
                {
                        if (i>=0)
                        {
                                if ((line%module)==0)
                                {
                                        if (i-oldi > 20)
                                        {
                                                String s = ""+(int)k;
                                                if (isMillimeters) s += "0";
                                                if (isPixel) s = ""+((int)k*50);

                                                int w = g.getFontMetrics().stringWidth(s);
                                                writeRotateString((Graphics2D)g, 4, i, s);
                                                //g.drawString(s ,1, i + g.getFontMetrics().getDescent());
                                                oldi= i;
                                                g.drawLine(16,i,12,i);
                                        }
                                        else
                                        {
                                            g.drawLine(5,i,10,i);
                                        }

                                }
                                else 
                                {
                                    if (module == 10 && (line%5) !=0)
                                    {
                                        g.drawLine(7,i,8,i);
                                    }
                                    else
                                    {
                                        g.drawLine(6,i,9,i);
                                    }
                                }
                        }
                        line++;
                        k = line*(1.0/module);
                        i = y1 - (int)(convertUnitToPixel(k,unit) * zoomfactor);			
                }      
                
                for (i=0; i<getGuideLines().size(); ++i)
                {
                    Integer pos = (Integer)getGuideLines().get(i);
                    int posI = pos.intValue();
                    // Calc posI....
                    posI = 10 + (int)(posI*zoomfactor) - getJReportFrame().getHScroll();
                    g.drawImage(horizontalRuleStopIcon.getImage(),7,posI-4, this);
                }

        }   
    }
    
    
    
   public void writeRotateString(Graphics2D g2, int x, int yCenter, String s)
  {
        
       
  	java.awt.geom.Rectangle2D sb = g2.getFontMetrics().getStringBounds(s, g2) ;
  	int sw = (int)sb.getWidth();
  	int sh = (int)(sb.getHeight()/2);
  	
        //g2.drawString(s,x - sw/3, yCenter+sh/2);
  	
        
        AffineTransform oldAr = g2.getTransform();
  	
  	int rotX = x;
  	int rotY = yCenter;
  	
        AffineTransform at =  g2.getTransform();
        at.rotate(-Math.PI/2.0, x, yCenter);
        at.translate(-(sw/2),sh);
  	//AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2.0, x, yCenter);
  	//AffineTransform at2 = AffineTransform.getTranslateInstance(-(sw/2),sh);
  	g2.setTransform(at);
  	
  	g2.drawString(s,x,yCenter);//s,x - (sw/2) , yCenter + sh);
  	g2.setTransform(oldAr);
        
  }
    
    /** Getter for property cursorPosition.
     * @return Value of property cursorPosition.
     *
     */
    public int getCursorPosition() {
        return cursorPosition;
    }
    
    /** Setter for property cursorPosition.
     * @param cursorPosition New value of property cursorPosition.
     *
     */
    public void setCursorPosition(int cursorPosition) {
        Graphics g = this.getGraphics();
        if (g==null) return;
        g.setXORMode(Color.WHITE);
        
        if (getType() == TYPE_HORIZONTAL)
        {
            g.drawLine(this.cursorPosition, 0,this.cursorPosition, this.getHeight());
            this.cursorPosition = cursorPosition;
            g.drawLine(this.cursorPosition, 0,this.cursorPosition, this.getHeight());
        }
        else
        {
            g.drawLine( 0, this.cursorPosition, this.getWidth(), this.cursorPosition);
            this.cursorPosition = cursorPosition;
            g.drawLine(0, this.cursorPosition, this.getWidth(), this.cursorPosition);
        }
        
        g.setPaintMode();
   }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    }
    
    public int convertUnitToPixel( double value, double unit)
    {
        if (unit == Unit.PIXEL) return (int)value;
        return (int)Unit.convertToPixels(value,unit);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if (type != this.type)
        {
            horizontalRuleStopIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/toolbars/rulestop" + ((type == TYPE_VERTICAL) ? "v" : "") + ".png"));
            this.type = type;
        }
    }

    public void panelMousePressed(java.awt.event.MouseEvent evt) {
        
        // Look for an existing gridline....
        int currentLine = -1;
        for (int i=0; i<getGuideLines().size(); ++i)
        {
            Integer pos = (Integer)getGuideLines().get(i);
            int posI = pos.intValue();
            // Calc posI....
            int scroll = (getType() == TYPE_HORIZONTAL) ? getJReportFrame().getHScroll() : getJReportFrame().getVScroll();
            posI = 10 + (int)(posI*getJReportFrame().getZoomFactor()) - scroll;
            
            int mousePos = (getType() == TYPE_HORIZONTAL) ? evt.getX() : evt.getY();
            
            if (mousePos > posI-3 && mousePos < posI+3)
            {
                currentLine = posI;
                getGuideLines().remove(i);
                break;
            }
        }
        
        savedImage = ((java.awt.image.BufferedImage)this.createImage(this.getWidth(), this.getHeight()));
        Graphics2D savedGraphics = savedImage.createGraphics();
        this.paint( savedGraphics );    
        
        if (getType() == TYPE_HORIZONTAL) this.getGraphics().drawImage(horizontalRuleStopIcon.getImage(),evt.getX()-4, 7, this);
        else this.getGraphics().drawImage(horizontalRuleStopIcon.getImage(),7, evt.getY()-4, this);
        
        lastTempGuidePosition = (getType() == TYPE_HORIZONTAL) ? evt.getX() : evt.getY();
        Graphics2D jrfGraphics = (Graphics2D)this.getJReportFrame().getReportPanel().getGraphics();
        Stroke oldStroke = jrfGraphics.getStroke();
        jrfGraphics.setXORMode(java.awt.Color.YELLOW);
        jrfGraphics.setStroke(getDottedStroke());
        if (lastTempGuidePosition >= 0)
        {
            if (getType() == TYPE_HORIZONTAL) jrfGraphics.drawLine(currentLine, 0,currentLine,this.getJReportFrame().getHeight());
            else jrfGraphics.drawLine(0,currentLine, this.getJReportFrame().getWidth(), currentLine);
        }
        if (getType() == TYPE_HORIZONTAL) jrfGraphics.drawLine(lastTempGuidePosition, 0,lastTempGuidePosition,this.getJReportFrame().getHeight());
        else jrfGraphics.drawLine(0,lastTempGuidePosition, this.getJReportFrame().getWidth(), lastTempGuidePosition);
        jrfGraphics.setPaintMode();
        jrfGraphics.setStroke(oldStroke);
    }
    
    public void panelMouseReleased(java.awt.event.MouseEvent evt) {
        
        this.getGraphics().drawImage(savedImage,0, 0, this);
       
        if (getType() == TYPE_HORIZONTAL)
        {
            if (evt.getX() > 0 && evt.getX() < this.getWidth())
            {
                int newPosition = getJReportFrame().getLogicalDim((int)evt.getX()+getJReportFrame().getHScroll()-10);
                this.getGuideLines().add( new Integer(newPosition) );
                this.repaint();
                this.getJReportFrame().repaint();
            }
        }
        else
        {
            if (evt.getY() > 0 && evt.getY() < this.getHeight())
            {
                int newPosition = getJReportFrame().getLogicalDim((int)evt.getY()+getJReportFrame().getVScroll()-10);
                this.getGuideLines().add( new Integer(newPosition) );
                this.repaint();
                this.getJReportFrame().repaint();
            }
        }
    }

    public void panelMouseDragged(MouseEvent e) {
  
        this.getGraphics().drawImage(savedImage,0, 0, this);
        
        if (getType() == TYPE_HORIZONTAL)
        {
            this.getGraphics().drawImage(horizontalRuleStopIcon.getImage(),e.getX()-4, 7, this);
        
        }
        else
        {
            this.getGraphics().drawImage(horizontalRuleStopIcon.getImage(),7, e.getY()-4, this);
        }
        
        Graphics2D jrfGraphics = (Graphics2D)this.getJReportFrame().getReportPanel().getGraphics();
        Stroke oldStroke = jrfGraphics.getStroke();
        jrfGraphics.setXORMode(java.awt.Color.YELLOW);
        jrfGraphics.setStroke(getDottedStroke());
        if (lastTempGuidePosition >= 0)
        {
            if (getType() == TYPE_HORIZONTAL) jrfGraphics.drawLine(lastTempGuidePosition, 0,lastTempGuidePosition,this.getJReportFrame().getHeight());
            else jrfGraphics.drawLine(0,lastTempGuidePosition, this.getJReportFrame().getWidth(), lastTempGuidePosition);
        }
        lastTempGuidePosition = (getType() == TYPE_HORIZONTAL) ? e.getX() : e.getY();
        if (getType() == TYPE_HORIZONTAL) jrfGraphics.drawLine(lastTempGuidePosition, 0,lastTempGuidePosition,this.getJReportFrame().getHeight());
        else jrfGraphics.drawLine(0,lastTempGuidePosition, this.getJReportFrame().getWidth(), lastTempGuidePosition);
        jrfGraphics.setPaintMode();
        jrfGraphics.setStroke(oldStroke);
    }

    public void panelMouseMoved(MouseEvent e) {
        setCursorPosition( getType() == TYPE_HORIZONTAL ? e.getX() : e.getY());
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public java.util.List getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(java.util.List guideLines) {
        this.guideLines = guideLines;
    }

    public Stroke getDottedStroke() {
        return dottedStroke;
    }

    public void setDottedStroke(Stroke dottedStroke) {
        this.dottedStroke = dottedStroke;
    }
}
