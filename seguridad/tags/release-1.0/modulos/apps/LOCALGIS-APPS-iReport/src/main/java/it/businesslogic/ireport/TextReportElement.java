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
 * TextReportElement.java
 * 
 * Created on 28 febbraio 2003, 19.20
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.util.*;
import java.util.List;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;



public abstract class TextReportElement extends ReportElement implements BoxElement {
   static public Rotation ROTATION_NONE;
   static public Rotation ROTATION_LEFT;
   static public Rotation ROTATION_RIGHT;
   static public Rotation ROTATION_UPSIDEDOWN;
  
   static
   { 
     ROTATION_NONE = new Rotation("None", 0);
     ROTATION_LEFT = new Rotation("Left", 1);
     ROTATION_RIGHT = new Rotation("Right", 2);
     ROTATION_UPSIDEDOWN = new Rotation("UpsideDown", 3);
   }

   private String text="";
   
   static public String ALIGN = "ALIGN";
   static public String VERTICAL_ALIGN = "VERTICAL_ALIGN";
   static public String IS_STYLED_TEXT = "IS_STYLED_TEXT";
   static public String LINE_SPACING = "LINE_SPACING";
   static public String ROTATE = "ROTATE";

   
   static public String DEFAULT_ALIGN = "Left";
   static public String DEFAULT_VERTICAL_ALIGN = "Top";
   static public boolean DEFAULT_IS_STYLED_TEXT = false;
   static public String DEFAULT_LINE_SPACING = "Single";
   static public String DEFAULT_ROTATE = "None";
   
   //private String reportFont="";
   //private String fontName="";
   //private String PDFFontName="";
   //private int fontSize=10;
   //private String TTFFont="";
   //private boolean bold=false;
   //private boolean underline=false;
   //private boolean italic=false;
   //private boolean strikeTrought=false;
   //private String lineSpacing="";
   //private String align="";
   //private String verticalAlign="";
   //private boolean pdfEmbedded=false;
   //private String pdfEncoding="";
   
   private java.awt.Font font=null;


   //private String rotate = "None";
   //private boolean isStyledText = false;
   
    private Box box = null;
    private IReportFont iReportFont;

   
   public TextReportElement(int x, int y, int width, int height)
   {
	  super(x,y,width,height);
      
	  //see net.sf.jasperreports.engine.base.JRBaseFont
	  //setGraphicElementPen("Thin");
	  //this.bgcolor = Color.WHITE;
	  //this.fgcolor = Color.BLACK;
	  this.text = "Static text\nsecond line";
	  //this.verticalAlign = "Top";
	  //this.align = "Left";
	  //this.lineSpacing = "Single";
          box = new Box();
	  //set font
          transparentDefault = "Transparent";
	  setIReportFont(new IReportFont());	  

   }
   
   public void setIReportFont(IReportFont newIreportFont){
   	
   	if(newIreportFont == null){
   		this.iReportFont = new IReportFont();
   	}
        else
        {
            this.iReportFont = (IReportFont)newIreportFont.clone();
        }
   	this.font = iReportFont.getJavaAWTFont();
   }

   /** Return a instance of IReportFont. */
   public IReportFont getIReportFont(){
        if (iReportFont == null) iReportFont = new IReportFont();
   	return iReportFont;
   }
   
   public void drawObject(Graphics2D g,double zoom_factor, int x_shift_origin, int y_shift_origin)
   {
      //System.out.println(new java.util.Date() + " Print text " + this.getText() + " " +  x_shift_origin +" " +y_shift_origin);
      
      position.x -= 10;
      position.y -= 10;
      x_shift_origin -= 10;
      y_shift_origin -= 10;

      
      this.zoom_factor = zoom_factor;
      
      g.setColor( getBgcolor() );
      
      if (!getTransparent().equalsIgnoreCase("Transparent"))
      {
         g.fillRect(getZoomedDim(position.x)-x_shift_origin,
                    getZoomedDim(position.y)-y_shift_origin,
                    getZoomedDim(width),
                    getZoomedDim(height));
      }
      g.setColor( getFgcolor() );
      
      // Set font to default font....
      //Font oldFont = g.getFont();
      
      if (font == null)
      {
         int style = 0;
         if (isBold()) style |= Font.BOLD;
         if (isItalic()) style |= Font.ITALIC;
         if (style == 0) style = Font.PLAIN;
         font = new Font( this.getFontName(), style , getZoomedDim( this.getFontSize() ));
      }
           
        // Code for rotation by gt (taked by jasperreports...
                
        int gfx_x = getZoomedDim(position.x + getBox().getLeftPadding() )-x_shift_origin;
        int gfx_y = getZoomedDim(position.y + getBox().getTopPadding())-y_shift_origin;
        int gfx_width = getZoomedDim(width - getBox().getLeftPadding() - getBox().getRightPadding());
        int gfx_height = getZoomedDim(height - getBox().getTopPadding() - getBox().getBottomPadding());

      
        /*
      Java2DUtil.setClip(g,
      gfx_x,
      gfx_y,
      gfx_width,
      gfx_height);
      */
      
      
        double angle = 0;
        double angle_restore = 0;

        // Apply the transformation "rotation"
         // - Do nothing when rotate = "none"
         AffineTransform transform = null;
         AffineTransform savedAT = g.getTransform();        
           
         if(getRotate().equals(ROTATION_LEFT.getName()))
         {
           transform = g.getTransform();
           transform.rotate(-Math.PI / 2, gfx_x, gfx_y);
           transform.translate(-gfx_height , -gfx_height);
           gfx_y = gfx_y + gfx_height;
           gfx_height = getZoomedDim(width - getBox().getLeftPadding() - getBox().getRightPadding());
           gfx_width = getZoomedDim(height - getBox().getTopPadding() - getBox().getBottomPadding());
 
         }
         else if(getRotate().equals(ROTATION_RIGHT.getName()))
         {
           transform = g.getTransform();

           transform.rotate(Math.PI / 2, gfx_x, gfx_y);
           transform.translate(-gfx_width, -gfx_width);
           gfx_x = gfx_x + gfx_width;
           gfx_width = getZoomedDim(height);
           gfx_height = getZoomedDim(width - getBox().getLeftPadding() - getBox().getRightPadding());
           gfx_width = getZoomedDim(height - getBox().getTopPadding() - getBox().getBottomPadding());
         }
         else if(getRotate().equals(ROTATION_UPSIDEDOWN.getName()))
         {
           transform = g.getTransform();

           transform.rotate(Math.PI, gfx_x, gfx_y);
           transform.translate(-gfx_width, -gfx_height);
           //gfx_x = gfx_x + gfx_width;
           //gfx_y = gfx_y + gfx_height;
           //gfx_width = getZoomedDim(height);
           //gfx_height = 
           //gfx_width = getZoomedDim(height - getBox().getTopPadding() - getBox().getBottomPadding());
           
         }
                
         if (this.getText() != null && this.getText().length() > 0)
         {
             drawText(g, transform, gfx_x, gfx_y, gfx_height, gfx_width);
         }
          
        
      
      //g.setClip(null);
      //g.setClip(orgClipBounds);
         
      // ----- Java2DUtil.resetClip(g);
        
    
        
      position.x += 10;
      position.y += 10;
      x_shift_origin += 10;
      y_shift_origin += 10;
      
      drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin);
      drawBorder( g, zoom_factor,  x_shift_origin,y_shift_origin,getBox());
      //drawGraphicsElement(g,this.getGraphicElementPen(), zoom_factor,  x_shift_origin,y_shift_origin, 0);
   }
   
   /** Getter for property align.
    * @return Value of property align.
    *
    */
   public java.lang.String getAlign()
   {
       if (getPropertyValue(ALIGN) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_hAlign, DEFAULT_ALIGN, true);
            }
        }
        return getStringValue(ALIGN, DEFAULT_ALIGN );
   }
   
   /** Setter for property align.
    * @param align New value of property align.
    *
    */
   public void setAlign(java.lang.String align)
   {
      this.setPropertyValue(ALIGN,  align);
   }
   
   /** Getter for property bold.
    * @return Value of property bold.
    *
    */
   public boolean isBold()
   {
       if (getIReportFont().getPropertyValue( IReportFont.IS_BOLD) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isBold, IReportFont.DEFAULT_IS_BOLD, true);
            }
        }
        return getIReportFont().getBooleanValue( IReportFont.IS_BOLD, IReportFont.DEFAULT_IS_BOLD );
   }
   
   /** Setter for property bold.
    * @param bold New value of property bold.
    *
    */
   public void setBold(boolean bold)
   {
      getIReportFont().setPropertyValue( IReportFont.IS_BOLD, ""+bold);
   }
   
   /** Getter for property font.
    * @return Value of property font.
    *
    */
   public java.awt.Font getFont()
   {
      return font;
   }
   
   /** Setter for property font.
    * @param font New value of property font.
    *
    */
   public void setFont(java.awt.Font font)
   {
      this.font = font;
   }
   
   /** Getter for property fontName.
    * @return Value of property fontName.
    *
    */
   public java.lang.String getFontName()
   {
      if (getIReportFont().getPropertyValue( IReportFont.FONT_NAME) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_fontName, IReportFont.DEFAULT_FONT_NAME, true);
            }
        }
        return getIReportFont().getStringValue( IReportFont.FONT_NAME, IReportFont.DEFAULT_FONT_NAME );
   }
   
   /** Setter for property fontName.
    * @param fontName New value of property fontName.
    *
    */
   public void setFontName(java.lang.String fontName)
   {
      getIReportFont().setPropertyValue( IReportFont.FONT_NAME, fontName);
   }
   
   /** Getter for property fontSize.
    * @return Value of property fontSize.
    *
    */
   public int getFontSize()
   {
       if (getIReportFont().getPropertyValue( IReportFont.FONT_SIZE) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeInteger( getStyle().ATTRIBUTE_fontSize, IReportFont.DEFAULT_FONT_SIZE, true);
            }
        }
        return getIReportFont().getIntValue( IReportFont.FONT_SIZE, IReportFont.DEFAULT_FONT_SIZE );
   }
   
   /** Setter for property fontSize.
    * @param fontSize New value of property fontSize.
    *
    */
   public void setFontSize(int fontSize)
   {
      getIReportFont().setPropertyValue( IReportFont.FONT_SIZE, ""+fontSize);
   }
   
   /** Setter for property fontSize.
    * @param fontSize New value of property fontSize.
    *
    */
   public void modifyFontSize(int delta)
   {
       int newFonsSize = getFontSize() + delta;
       if ( newFonsSize >= 5) 
        this.setFontSize(newFonsSize );
   }
   
   /** Getter for property italic.
    * @return Value of property italic.
    *
    */
   public boolean isItalic()
   {
      if (getIReportFont().getPropertyValue( IReportFont.IS_ITALIC) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isItalic, IReportFont.DEFAULT_IS_ITALIC, true);
            }
        }
        return getIReportFont().getBooleanValue( IReportFont.IS_ITALIC, IReportFont.DEFAULT_IS_ITALIC );
   }
   
   /** Setter for property italic.
    * @param italic New value of property italic.
    *
    */
   public void setItalic(boolean italic)
   {
      getIReportFont().setPropertyValue( IReportFont.IS_ITALIC, ""+italic);
   }
   
   /** Getter for property lineSpacing.
    * @return Value of property lineSpacing.
    *
    */
   public java.lang.String getLineSpacing()
   {
       if (getPropertyValue( LINE_SPACING) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_lineSpacing, DEFAULT_LINE_SPACING, true);
            }
        }
        return getStringValue( LINE_SPACING, DEFAULT_LINE_SPACING );
   }
   
   /** Setter for property lineSpacing.
    * @param lineSpacing New value of property lineSpacing.
    *
    */
   public void setLineSpacing(java.lang.String lineSpacing)
   {
      setPropertyValue( LINE_SPACING, lineSpacing);
   }
   
   /** Getter for property pdfEmbedded.
    * @return Value of property pdfEmbedded.
    *
    */
   public boolean isPdfEmbedded()
   {
      if (getIReportFont().getPropertyValue( IReportFont.IS_PDF_EMBEDDED) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isPdfEmbedded, IReportFont.DEFAULT_IS_PDF_EMBEDDED, true);
            }
        }
        return getIReportFont().getBooleanValue( IReportFont.IS_PDF_EMBEDDED, IReportFont.DEFAULT_IS_PDF_EMBEDDED );
   }
   
   /** Setter for property pdfEmbedded.
    * @param pdfEmbedded New value of property pdfEmbedded.
    *
    */
   public void setPdfEmbedded(boolean pdfEmbedded)
   {
      getIReportFont().setPropertyValue( IReportFont.IS_PDF_EMBEDDED, ""+pdfEmbedded);
   }
   
   /** Getter for property pdfEncoding.
    * @return Value of property pdfEncoding.
    *
    */
   public java.lang.String getPdfEncoding()
   {
      if (getIReportFont().getPropertyValue( IReportFont.PDF_ENCODING) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_pdfEncoding, IReportFont.DEFAULT_PDF_ENCODING, true);
            }
        }
        return getIReportFont().getStringValue( IReportFont.PDF_ENCODING, IReportFont.DEFAULT_PDF_ENCODING );
   }
   
   /** Setter for property pdfEncoding.
    * @param pdfEncoding New value of property pdfEncoding.
    *
    */
   public void setPdfEncoding(java.lang.String pdfEncoding)
   {
      getIReportFont().setPropertyValue( IReportFont.PDF_ENCODING, pdfEncoding);
   }
   
   /** Getter for property PDFFontName.
    * @return Value of property PDFFontName.
    *
    */
   public java.lang.String getPDFFontName()
   {
       if (getIReportFont().getPropertyValue( IReportFont.PDF_FONT_NAME) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_pdfFontName, IReportFont.DEFAULT_PDF_FONT_NAME, true);
            }
        }
        return getIReportFont().getStringValue( IReportFont.PDF_FONT_NAME, IReportFont.DEFAULT_PDF_FONT_NAME );
   }
   
   /** Setter for property PDFFontName.
    * @param PDFFontName New value of property PDFFontName.
    *
    */
   public void setPDFFontName(java.lang.String PDFFontName)
   {
      getIReportFont().setPropertyValue( IReportFont.PDF_FONT_NAME, PDFFontName);
   }
   
   /** Getter for property reportFont.
    * @return Value of property reportFont.
    *
    */
   public java.lang.String getReportFont()
   {
      return getIReportFont().getStringValue( IReportFont.REPORT_FONT, IReportFont.DEFAULT_REPORT_FONT);
   }
   
   /** Setter for property reportFont.
    * @param reportFont New value of property reportFont.
    *
    */
   public void setReportFont(java.lang.String reportFont)
   {
      getIReportFont().setPropertyValue( IReportFont.REPORT_FONT, reportFont);
   }
   
   /** Getter for property strikeTrought.
    * @return Value of property strikeTrought.
    *
    */
   public boolean isStrikeTrought()
   {
      if (getIReportFont().getPropertyValue( IReportFont.IS_STRIKETROUGHT) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isStrikeThrough, IReportFont.DEFAULT_IS_STRIKETROUGHT, true);
            }
        }
        return getIReportFont().getBooleanValue( IReportFont.IS_STRIKETROUGHT, IReportFont.DEFAULT_IS_STRIKETROUGHT );
   }
   
   /** Setter for property strikeTrought.
    * @param strikeTrought New value of property strikeTrought.
    *
    */
   public void setStrikeTrought(boolean strikeTrought)
   {
      getIReportFont().setPropertyValue( IReportFont.IS_STRIKETROUGHT, ""+strikeTrought);
   }
   
   /** Getter for property text.
    * @return Value of property text.
    *
    */
   public java.lang.String getText()
   {
      return text;
   }
   
   /** Setter for property text.
    * @param text New value of property text.
    *
    */
   public void setText(java.lang.String text)
   {
      this.text = text;
   }
   
   /** Getter for property TTFFont.
    * @return Value of property TTFFont.
    *
    */
   public java.lang.String getTTFFont()
   {
      if (getIReportFont().getPropertyValue( IReportFont.TTF_FONT_NAME) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_pdfFontName, IReportFont.DEFAULT_TTF_FONT_NAME, true);
            }
        }
        return getIReportFont().getStringValue( IReportFont.TTF_FONT_NAME, IReportFont.DEFAULT_TTF_FONT_NAME );
   }
   
   /** Setter for property TTFFont.
    * @param TTFFont New value of property TTFFont.
    *
    */
   public void setTTFFont(java.lang.String TTFFont)
   {
      getIReportFont().setPropertyValue( IReportFont.TTF_FONT_NAME, TTFFont );
   }
   
   /** Getter for property underline.
    * @return Value of property underline.
    *
    */
   public boolean isUnderline()
   {
       if (getIReportFont().getPropertyValue( IReportFont.IS_UNDERLINE) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isUnderline, IReportFont.DEFAULT_IS_UNDERLINE, true);
            }
        }
        return getIReportFont().getBooleanValue( IReportFont.IS_UNDERLINE, IReportFont.DEFAULT_IS_UNDERLINE);
   }
   
   /** Setter for property underline.
    * @param underline New value of property underline.
    *
    */
   public void setUnderline(boolean underline)
   {
      getIReportFont().setPropertyValue( IReportFont.IS_UNDERLINE, ""+underline );
   }
   
   /** Getter for property verticalAlign.
    * @return Value of property verticalAlign.
    *
    */
   public java.lang.String getVerticalAlign()
   {
      if (getPropertyValue( VERTICAL_ALIGN) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_vAlign, DEFAULT_VERTICAL_ALIGN, true);
            }
        }
        return getStringValue( VERTICAL_ALIGN, DEFAULT_VERTICAL_ALIGN );
   }
   
   /** Setter for property verticalAlign.
    * @param verticalAlign New value of property verticalAlign.
    *
    */
   public void setVerticalAlign(java.lang.String verticalAlign)
   {
      setPropertyValue( VERTICAL_ALIGN, verticalAlign );
   }
   
   /** Getter for property Rotate.
    *  Rotate can be one of the values: "none", "left", "right"
    * @return Value of property Rotate.
    */
   public java.lang.String getRotate()
   {
       if (getPropertyValue( ROTATE) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_rotation, DEFAULT_ROTATE, true);
            }
        }
        return getStringValue( ROTATE, DEFAULT_ROTATE );
   }
   
   /** Setter for property Rotate.
    * @param Rotate New value of property Rotate.
    *
    */
   public void setRotate(java.lang.String rotate)
   {
      setPropertyValue( ROTATE, rotate );
   }
   
   public int getTextHeight(java.awt.FontMetrics fm)
   {
      //
      return fm.getAscent() + (this.getLineCount()-1)*(fm.getHeight());
   }
   
   public int getLineCount()
   {
      // For any \n, return a line...
      String text = this.getText();
      text = text.replace('\r', ' ');
      int line=1;
      while (text.indexOf('\n')>0)
      {
         line++;
         text = text.substring( text.indexOf('\n') + 1);
      }
      return line;
   }
 
   public void copyBaseReportElement(ReportElement destination, ReportElement source)
   {
      super.copyBaseReportElement(destination, source);
      
      if (destination instanceof TextReportElement &&
      source instanceof TextReportElement )
      {
          TextReportElement tre = (TextReportElement)source;
          TextReportElement tred = (TextReportElement)destination;
          source.clone(tred);
          tre.getIReportFont().clone( tred.getIReportFont() ); 
          /*
          ((TextReportElement)destination).setAlign(new String(  ((TextReportElement)source).getAlign() ));
         ((TextReportElement)destination).setReportFont(((TextReportElement)source).getReportFont());
         ((TextReportElement)destination).setFontName(new String(  ((TextReportElement)source).getFontName() ));
         ((TextReportElement)destination).setPDFFontName(new String(  ((TextReportElement)source).getPDFFontName() ));
         ((TextReportElement)destination).setFontSize( ((TextReportElement)source).getFontSize() );
         ((TextReportElement)destination).setTTFFont(new String(  ((TextReportElement)source).getTTFFont() ));
         ((TextReportElement)destination).setBold( ((TextReportElement)source).isBold() );
         ((TextReportElement)destination).setItalic( ((TextReportElement)source).isItalic() );
         ((TextReportElement)destination).setUnderline(  ((TextReportElement)source).isUnderline() );
         ((TextReportElement)destination).setStrikeTrought(  ((TextReportElement)source).isStrikeTrought() );
         ((TextReportElement)destination).setLineSpacing(new String(  ((TextReportElement)source).getLineSpacing() ));
         ((TextReportElement)destination).setVerticalAlign(new String(  ((TextReportElement)source).getVerticalAlign() ));
         ((TextReportElement)destination).setPdfEmbedded( ((TextReportElement)source).isPdfEmbedded());
         ((TextReportElement)destination).setPdfEncoding(new String(  ((TextReportElement)source).getPdfEncoding() ));
         */
         ((TextReportElement)destination).setText(new String(  ((TextReportElement)source).getText() ));
         ((TextReportElement)destination).setBox( ((TextReportElement)source).getBox().cloneMe() );
      }
   }


   private class TextReportElementLayout {
       private TextLayout layout;
       private float x;
       private float y;


       private TextReportElementLayout(TextLayout layout, float x, float y) {
           this.layout = layout;
           this.x = x;
           this.y = y;
       }


       void drawWithOffset(Graphics2D g2, float yOffset) {
           layout.draw(g2, x, y + yOffset);
       }
   }


   static List getRotations() {
       return Rotation.rotations;
   }

   /** Getter for property isStyledText.
    * @return Value of property isStyledText.
    *
    */
   public boolean isIsStyledText() {
       if (getPropertyValue( IS_STYLED_TEXT) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeBoolean( getStyle().ATTRIBUTE_isStyledText, DEFAULT_IS_STYLED_TEXT, true);
            }
        }
        return getBooleanValue( IS_STYLED_TEXT, DEFAULT_IS_STYLED_TEXT );
   }   

   /** Setter for property isStyledText.
    * @param isStyledText New value of property isStyledText.
    *
    */
   public void setIsStyledText(boolean isStyledText) {
      setPropertyValue( IS_STYLED_TEXT, "" + isStyledText );
   }
   
   static class Rotation {
       static private ArrayList rotations;
       private String name;
       private int number;


       Rotation(String name, int number) {
           this.name = name;
           this.number = number;
           rotations = new ArrayList();
           rotations.add(this);
       }


       public int getNumber() {
         return number;
       }


       public String getName() {
           return name;
       }


       public String toString() {
           return getName();
       }
   }
   

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
    
     public void setStyle(Style style) {
     
        super.setStyle(style);
        
        if (style != null)
        {            
            //this.setAlign( style.getAttributeString( style.ATTRIBUTE_hAlign, getAlign(), true));
            //this.setFontName( style.getAttributeString( style.ATTRIBUTE_fontName, getFontName(), true));
            //this.setPDFFontName( style.getAttributeString( style.ATTRIBUTE_pdfFontName, getPDFFontName(), true));
            //this.setFontSize( Integer.parseInt( style.getAttributeString( style.ATTRIBUTE_fontSize, getFontSize()+"", true)));
            //this.setTTFFont( style.getAttributeString( style.ATTRIBUTE_pdfFontName, getTTFFont(), true));
            //this.setBold( style.getAttributeBoolean( style.ATTRIBUTE_isBold, isBold(), true));
            //this.setItalic(style.getAttributeBoolean( style.ATTRIBUTE_isItalic, isItalic(), true));
            //this.setUnderline(style.getAttributeBoolean( style.ATTRIBUTE_isUnderline, isUnderline(), true));
            //this.setStrikeTrought(style.getAttributeBoolean( style.ATTRIBUTE_isStrikeThrough, isStrikeTrought(), true));
            //this.setLineSpacing( style.getAttributeString( style.ATTRIBUTE_lineSpacing, getLineSpacing(), true));
            //this.setVerticalAlign( style.getAttributeString( style.ATTRIBUTE_vAlign, getVerticalAlign(), true));
            //this.setPdfEmbedded( style.getAttributeBoolean( style.ATTRIBUTE_isPdfEmbedded, isPdfEmbedded(), true));
            //this.setPdfEncoding( style.getAttributeString( style.ATTRIBUTE_pdfEncoding, getPdfEncoding(), true));
            //this.setRotate(style.getAttributeString( style.ATTRIBUTE_rotation, getRotate(), true)  );
            
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
     
     
     private void drawText(Graphics2D g, AffineTransform transform, int gfx_x, int gfx_y, int gfx_height, int gfx_width)
     {
          int zoomedFieldHeight = gfx_height;
         String allText = Misc.treatNewLineChars(this.getText());
         float formatWidth = (float)gfx_width;
         float verticalOffset = 0f;
         TextReportElementLayout textReportElementLayout;
         
         
         ArrayList               textLayouts;
        float                   x, y;
         
         
         FontRenderContext fontRenderContext = g.getFontRenderContext();
         java.util.Map fontAttributes = font.getAttributes();
         fontAttributes.put(TextAttribute.SIZE, new Float(getZoomedDim( this.getFontSize() )) );
         fontAttributes.put(TextAttribute.FAMILY, this.getFontName() );
         if (this.isBold())
         {
            fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
         }
         if (this.isItalic())
         {
            fontAttributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
         }
         if (this.isUnderline())
         {
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
         }
         if (this.isStrikeTrought())
         {
            fontAttributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
         }
         
         float lineSpacing = 1f;
         if (this.getLineSpacing().equals("Single")) lineSpacing = 1f;
         else if (this.getLineSpacing().equals("1_1_2")) lineSpacing = 1.5f;
         else if (this.getLineSpacing().equals("Double")) lineSpacing = 2f;
         
         AttributedString atext;
         AttributedCharacterIterator paragraph;
         int paragraphStart;
         int paragraphEnd;
         LineBreakMeasurer lineMeasurer;
         TextLayout layout = null;
         
         
         String paragr_text = "";
         boolean isMaxHeightReached = false;
         
         StringTokenizer tkzer = new StringTokenizer(allText, "\n");
         
         float drawPosY = 0;
         float drawPosX = 0;
        
         paragr_text = "";
         isMaxHeightReached = false;
         
         tkzer = new StringTokenizer(allText, "\n");


         textLayouts = new ArrayList();
        
         // Calculate the layouts. (But don't draw yet because we don't know yet
         //   the offset which is needed if we align the text "middle" or "bottom")
         while(tkzer.hasMoreTokens() && !isMaxHeightReached)
         {
            paragr_text = tkzer.nextToken();
            
            atext = new AttributedString(paragr_text, fontAttributes);
            paragraph = atext.getIterator();
            paragraphStart = paragraph.getBeginIndex();
            paragraphEnd = paragraph.getEndIndex();
            lineMeasurer = new LineBreakMeasurer(paragraph, fontRenderContext);
            lineMeasurer.setPosition(paragraphStart);
            
            layout = null;
            while (lineMeasurer.getPosition() < paragraphEnd && !isMaxHeightReached)
            {
               layout = lineMeasurer.nextLayout(formatWidth);
               
               drawPosY += layout.getLeading() + lineSpacing * layout.getAscent();
               
               if (drawPosY + layout.getDescent() <= zoomedFieldHeight+1)
               {
                  if (this.getAlign().equals("Justify"))
                  {
                     if (layout.isLeftToRight())
                     {
                        drawPosX = 0;
                     }
                     else
                     {
                        drawPosX = formatWidth - layout.getAdvance();
                     }
                     if (lineMeasurer.getPosition() < paragraphEnd)
                     {
                        layout = layout.getJustifiedLayout(formatWidth);
                     }
                  }
                  else if (this.getAlign().equals("Right"))
                  {
                     //if (layout.isLeftToRight())
                     //{
                        drawPosX = formatWidth - layout.getAdvance();
                     //}
                     //else
                     //{
                     //   drawPosX = formatWidth;
                     //}
                  }
                  else if (this.getAlign().equals("Center"))
                  {
                     drawPosX = (formatWidth - layout.getAdvance()) / 2;
                  }
                  else //if (this.getAlign().equals("Left"))
                  {
                     //if (layout.isLeftToRight())
                     //{
                        drawPosX = 0;
                     //}
                     //else
                     //{
                     //   drawPosX = formatWidth - layout.getAdvance();
                     //}
                  }
                
                  x = drawPosX + gfx_x; //getZoomedDim(position.x)-x_shift_origin;
                  y = drawPosY + gfx_y; //getZoomedDim(position.y)-y_shift_origin;
                  textReportElementLayout = new TextReportElementLayout(layout, x, y);
                  textLayouts.add(textReportElementLayout);


                  drawPosY += layout.getDescent();
               }
               else
               {
                  drawPosY -= layout.getLeading() + lineSpacing * layout.getAscent();
                  isMaxHeightReached = true;
               }
            }
         }


         // Calculate the offset when aligning the text.
         float textHeight = drawPosY;
         if (this.getVerticalAlign().equals("Top"))
         {
             verticalOffset = 0f;
         }
         else if (this.getVerticalAlign().equals("Middle"))
         {
            verticalOffset = ((float)zoomedFieldHeight- textHeight) / 2f;
         }
         else if (this.getVerticalAlign().equals("Bottom"))
         {
            verticalOffset = (float)zoomedFieldHeight - (float)textHeight;
         }

         // Put affine transformatio  here!!!
         
         AffineTransform savedAT = g.getTransform();
         if (transform != null) g.setTransform( transform);
      
         // Now draw the text according to the calculated layouts.
         for(Iterator i=textLayouts.iterator(); i.hasNext();)
         {
           textReportElementLayout = (TextReportElementLayout) i.next();
           textReportElementLayout.drawWithOffset(g, verticalOffset);
         }
         
         if(transform != null)
         { 
            g.setTransform(savedAT);
         }
         
         
     }
}
