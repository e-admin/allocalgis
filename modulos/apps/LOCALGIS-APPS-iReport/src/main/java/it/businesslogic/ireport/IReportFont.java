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
 * IReportFont.java
 * 
 * Created on 6 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport;

import java.awt.font.TextAttribute;
import java.util.Map;

/**
 *
 * @author  Administrator
 */
public class IReportFont extends IReportHashMapBean {
    
    static public final String IS_DEFAULT_FONT = "defaultFont";
    static public final String REPORT_FONT = "reportFont";
    static public final String FONT_NAME = "fontName";
    static public final String PDF_FONT_NAME = "PDFFontName";
    static public final String FONT_SIZE = "fontSize";
    static public final String TTF_FONT_NAME = "PDFFontName";
    static public final String IS_BOLD = "bold";
    static public final String IS_UNDERLINE = "underline";
    static public final String IS_ITALIC = "italic";
    static public final String IS_STRIKETROUGHT = "strikeTrought";
    static public final String IS_PDF_EMBEDDED = "pdfEmbedded";
    static public final String PDF_ENCODING = "pdfEncoding";
    
    static public final boolean  DEFAULT_IS_DEFAULT_FONT = false;
    static public final String  DEFAULT_REPORT_FONT = "";
    static public final String  DEFAULT_FONT_NAME = "SansSerif";
    static public final String  DEFAULT_PDF_FONT_NAME = "Helvetica";
    static public final int     DEFAULT_FONT_SIZE = 10;
    static public final String  DEFAULT_TTF_FONT_NAME = "";
    static public final boolean DEFAULT_IS_BOLD = false;
    static public final boolean DEFAULT_IS_UNDERLINE = false;
    static public final boolean DEFAULT_IS_ITALIC = false;
    static public final boolean DEFAULT_IS_STRIKETROUGHT = false;
    static public final boolean DEFAULT_IS_PDF_EMBEDDED = false;
    static public final String  DEFAULT_PDF_ENCODING = "Cp1252";
    
    
    
    /** Creates a new instance of IReportFont */
    public IReportFont() {
    }
    
    public Object clone()
    {
            IReportFont newBean = new IReportFont();
            return super.clone(newBean);
    }
        
    /** Getter for property bold.
     * @return Value of property bold.
     *
     */
    public boolean isBold() {
        return getBooleanValue( IS_BOLD, DEFAULT_IS_BOLD );
    }
    
    public boolean isSet(String property)
    {
        return (getBeanProperties().get(property) != null);
    }
    
    /** Setter for property bold.
     * @param bold New value of property bold.
     *
     */
    public void setBold(boolean bold) {
        getBeanProperties().put(IS_BOLD, ""+bold);
    }
    
    /** Getter for property fontName.
     * @return Value of property fontName.
     *
     */
    public java.lang.String getFontName() {
        return getStringValue(FONT_NAME, DEFAULT_FONT_NAME);
    }
    
    /** Setter for property fontName.
     * @param fontName New value of property fontName.
     *
     */
    public void setFontName(java.lang.String fontName) {
        getBeanProperties().put(FONT_NAME, fontName);
    }
    
    /** Getter for property fontSize.
     * @return Value of property fontSize.
     *
     */
    public int getFontSize() {
        return getIntValue(FONT_SIZE, DEFAULT_FONT_SIZE);
    }
    
    /** Setter for property fontSize.
     * @param fontSize New value of property fontSize.
     *
     */
    public void setFontSize(int fontSize) {
        getBeanProperties().put(FONT_SIZE, ""+fontSize);
    }
    
    /** Getter for property italic.
     * @return Value of property italic.
     *
     */
    public boolean isItalic() {
        return getBooleanValue( IS_ITALIC, DEFAULT_IS_ITALIC );
    }
    
    /** Setter for property italic.
     * @param italic New value of property italic.
     *
     */
    public void setItalic(boolean italic) {
       getBeanProperties().put(IS_ITALIC, ""+italic);
    }
    
    /** Getter for property pdfEmbedded.
     * @return Value of property pdfEmbedded.
     *
     */
    public boolean isPdfEmbedded() {
        return getBooleanValue( IS_PDF_EMBEDDED, DEFAULT_IS_PDF_EMBEDDED );
    }
    
    /** Setter for property pdfEmbedded.
     * @param pdfEmbedded New value of property pdfEmbedded.
     *
     */
    public void setPdfEmbedded(boolean pdfEmbedded) {
        getBeanProperties().put(IS_PDF_EMBEDDED, ""+pdfEmbedded);
    }
    
    /** Getter for property pdfEncoding.
     * @return Value of property pdfEncoding.
     *
     */
    public java.lang.String getPdfEncoding() {
        return getStringValue(PDF_ENCODING, DEFAULT_PDF_ENCODING);
    }
    
    /** Setter for property pdfEncoding.
     * @param pdfEncoding New value of property pdfEncoding.
     *
     */
    public void setPdfEncoding(java.lang.String pdfEncoding) {
        getBeanProperties().put(PDF_ENCODING, ""+pdfEncoding);
    }
    
    /** Getter for property PDFFontName.
     * @return Value of property PDFFontName.
     *
     */
    public java.lang.String getPDFFontName() {
        return getStringValue(PDF_FONT_NAME, DEFAULT_PDF_FONT_NAME);
    }
    
    /** Setter for property PDFFontName.
     * @param PDFFontName New value of property PDFFontName.
     *
     */
    public void setPDFFontName(java.lang.String PDFFontName) {
        getBeanProperties().put(PDF_FONT_NAME, ""+PDFFontName);
	//see it.bussinesslogic.ireport.Report.java line 2041
	if(PDFFontName != null && PDFFontName.toUpperCase().endsWith(".TTF")){
                getBeanProperties().put(TTF_FONT_NAME, ""+PDFFontName);
	}
    }
    
    /** Getter for property reportFont.
     * @return Value of property reportFont.
     *
     */
    public java.lang.String getReportFont() {
        return getStringValue(REPORT_FONT, DEFAULT_REPORT_FONT);
    }
    
    /** Setter for property reportFont.
     * @param reportFont New value of property reportFont.
     *
     */
    public void setReportFont(java.lang.String reportFont) {
        getBeanProperties().put(REPORT_FONT, ""+reportFont);
    }
    
    /** Getter for property strikeTrought.
     * @return Value of property strikeTrought.
     *
     */
    public boolean isStrikeTrought() {
        return getBooleanValue( IS_STRIKETROUGHT, DEFAULT_IS_STRIKETROUGHT );
    }
    
    /** Setter for property strikeTrought.
     * @param strikeTrought New value of property strikeTrought.
     *
     */
    public void setStrikeTrought(boolean strikeTrought) {
        getBeanProperties().put(IS_STRIKETROUGHT, ""+strikeTrought);
    }
    
    /** Getter for property TTFFont.
     * @return Value of property TTFFont.
     *
     */
    public java.lang.String getTTFFont() {
        return getStringValue(TTF_FONT_NAME, DEFAULT_TTF_FONT_NAME);
    }
    
    /** Setter for property TTFFont.
     * @param TTFFont New value of property TTFFont.
     *
     */
    public void setTTFFont(java.lang.String TTFFont) {
        getBeanProperties().put(TTF_FONT_NAME, ""+TTFFont);
    }
    
    /** Getter for property underline.
     * @return Value of property underline.
     *
     */
    public boolean isUnderline() {
        return getBooleanValue( IS_UNDERLINE, DEFAULT_IS_UNDERLINE );
    }
    
    /** Setter for property underline.
     * @param underline New value of property underline.
     *
     */
    public void setUnderline(boolean underline) {
        getBeanProperties().put(IS_UNDERLINE, ""+underline);
    }
        
    public String toString()
    {
        return this.getReportFont();
    }
    
    public String getDescription()
    {
        return this.getFontName()+" "+this.getFontSize();
    }
    
    /** Getter for property defaultFont.
     * @return Value of property defaultFont.
     *
     */
    public boolean isDefaultFont() {
        return getBooleanValue( IS_DEFAULT_FONT, DEFAULT_IS_DEFAULT_FONT );
    }
    
    /** Setter for property defaultFont.
     * @param defaultFont New value of property defaultFont.
     *
     */
    public void setDefaultFont(boolean defaultFont) {
        getBeanProperties().put(IS_DEFAULT_FONT, ""+defaultFont);
    }
    
    
    /** Convert to java.awt.Font.
    */    
    public java.awt.Font getJavaAWTFont(){

             int style = java.awt.Font.PLAIN;
             style = style & (isBold() ? java.awt.Font.BOLD : 0);
             style = style & (isItalic() ? java.awt.Font.ITALIC : 0);

             java.awt.Font font = new java.awt.Font(getFontName(), style, getFontSize());

             Map fontAttributes = font.getAttributes();
             if (isUnderline()){
                    fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
             }
             if (isStrikeTrought()){
                    fontAttributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
             }
             return new java.awt.Font(fontAttributes);

    }

	/** Copy java.awt.Font common attributes.
	*/    
	public void setJavaAWTFont(java.awt.Font font){
		
		//not logical name
		setFontName( font.getFontName() );
		
		setFontSize( font.getSize() );
   	
		int style = font.getStyle();
		setBold( (style & java.awt.Font.BOLD) > 0 );
		setItalic( (style & java.awt.Font.ITALIC) > 0);
	
		Map fontAttributes = font.getAttributes();
		setUnderline( (fontAttributes.containsKey(TextAttribute.UNDERLINE) && fontAttributes.get(TextAttribute.UNDERLINE).equals(TextAttribute.UNDERLINE_ON)));
		setStrikeTrought( (fontAttributes.containsKey(TextAttribute.STRIKETHROUGH) && fontAttributes.get(TextAttribute.STRIKETHROUGH).equals(TextAttribute.STRIKETHROUGH_ON)));
	
	}
        
        
        public static boolean isTTFFont(String fontName)
        {
            if (fontName.equals("Helvetica")) return false;
            if (fontName.equals("Helvetica-Bold")) return false;
            if (fontName.equals("Helvetica-BoldOblique")) return false;
            if (fontName.equals("Helvetica-Oblique")) return false;
            if (fontName.equals("Courier")) return false;
            if (fontName.equals("Courier-Bold")) return false;
            if (fontName.equals("Courier-BoldOblique")) return false;
            if (fontName.equals("Courier-Oblique")) return false;
            if (fontName.equals("Symbol")) return false;
            if (fontName.equals("Times-Roman")) return false;
            if (fontName.equals("Times-Bold")) return false;
            if (fontName.equals("Times-BoldItalic")) return false;
            if (fontName.equals("Times-Italic")) return false;
            if (fontName.equals("ZapfDingbats")) return false;
            if (fontName.equals("STSong-Light")) return false;
            if (fontName.equals("MHei-Medium")) return false;
            if (fontName.equals("MSung-Light")) return false;
            if (fontName.equals("HeiseiKakuGo-W5")) return false;
            if (fontName.equals("HeiseiMin-W3")) return false;
            if (fontName.equals("HYGoThic-Medium")) return false;
            if (fontName.equals("HYSMyeongJo-Medium")) return false;
            return true;
        }
        
        
        /**
         * Try to match the most appropriate pdf font
         *
         */
        public static String matchFont(String fontName, boolean bold, boolean italic)
        {
            if ( !isTTFFont( fontName ))
            {
                if (fontName.startsWith("Helvetica") && !bold && !italic) return "Helvetica";
                if (fontName.startsWith("Helvetica") && bold && !italic) return "Helvetica-Bold";
                if (fontName.startsWith("Helvetica") && bold && italic) return "Helvetica-BoldOblique";
                if (fontName.startsWith("Helvetica") && !bold && italic) return "Helvetica-Oblique";
                
                if (fontName.startsWith("Courier") && !bold && !italic) return "Courier";
                if (fontName.startsWith("Courier") && bold && !italic) return "Courier-Bold";
                if (fontName.startsWith("Courier") && bold && italic) return "Courier-BoldOblique";
                if (fontName.startsWith("Courier") && !bold && italic) return "Courier-Oblique";
                
                if (fontName.startsWith("Times") && !bold && !italic) return "Times-Roman";
                if (fontName.startsWith("Times") && bold && !italic) return "Times-Bold";
                if (fontName.startsWith("Times") && bold && italic) return "Times-BoldItalic";
                if (fontName.startsWith("Times") && !bold && italic) return "Times-Italic";
                
            }
            return fontName;
       }
       
        
        /**
        * Set the more appropriate pdf font name reading the bold and italic properties
        * of element. 
        */
        public static boolean adjustPdfFontName(TextReportElement element)
        {
               String newPdfFontName = matchFont(element.getPDFFontName(), element.isBold(), element.isItalic());
               if (newPdfFontName != null && !newPdfFontName.equals(element.getPDFFontName()))
               {
                   element.setPDFFontName(newPdfFontName);
                   return true;
               }
               
               return false;
        }
}
