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
 * Style.java
 * 
 * Created on 12 dicembre 2005, 16.35
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.sheet.ColorSelectorPanel;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
/**
 *
 * @author Administrator
 */
public class Style {
    
      	public static final String ATTRIBUTE_name = "name";
	public static final String ATTRIBUTE_isDefault = "isDefault";
	public static final String ATTRIBUTE_mode = "mode";
	public static final String ATTRIBUTE_forecolor = "forecolor";
	public static final String ATTRIBUTE_backcolor = "backcolor";
	public static final String ATTRIBUTE_style = "style";

	public static final String ATTRIBUTE_pen = "pen";
	public static final String ATTRIBUTE_fill = "fill";

	public static final String ATTRIBUTE_radius = "radius";

	public static final String ATTRIBUTE_scaleImage = "scaleImage";
	// these are inherited by both images and texts.
	public static final String ATTRIBUTE_hAlign = "hAlign";
	public static final String ATTRIBUTE_vAlign = "vAlign";

	public static final String ATTRIBUTE_border = "border";
	public static final String ATTRIBUTE_borderColor = "borderColor";
	public static final String ATTRIBUTE_padding = "padding";
	public static final String ATTRIBUTE_topBorder = "topBorder";
	public static final String ATTRIBUTE_topBorderColor = "topBorderColor";
	public static final String ATTRIBUTE_topPadding = "topPadding";
	public static final String ATTRIBUTE_leftBorder = "leftBorder";
	public static final String ATTRIBUTE_leftBorderColor = "leftBorderColor";
	public static final String ATTRIBUTE_leftPadding = "leftPadding";
	public static final String ATTRIBUTE_bottomBorder = "bottomBorder";
	public static final String ATTRIBUTE_bottomBorderColor = "bottomBorderColor";
	public static final String ATTRIBUTE_bottomPadding = "bottomPadding";
	public static final String ATTRIBUTE_rightBorder = "rightBorder";
	public static final String ATTRIBUTE_rightBorderColor = "rightBorderColor";
	public static final String ATTRIBUTE_rightPadding = "rightPadding";

	public static final String ATTRIBUTE_rotation = "rotation";
	public static final String ATTRIBUTE_lineSpacing = "lineSpacing";
	public static final String ATTRIBUTE_isStyledText = "isStyledText";
	public static final String ATTRIBUTE_pattern = "pattern";
	public static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";

	public static final String ATTRIBUTE_fontName = "fontName";
	public static final String ATTRIBUTE_isBold = "isBold";
	public static final String ATTRIBUTE_isItalic = "isItalic";
	public static final String ATTRIBUTE_isUnderline = "isUnderline";
	public static final String ATTRIBUTE_isStrikeThrough = "isStrikeThrough";
	public static final String ATTRIBUTE_fontSize = "fontSize";
	public static final String ATTRIBUTE_pdfFontName = "pdfFontName";
	public static final String ATTRIBUTE_pdfEncoding = "pdfEncoding";
	public static final String ATTRIBUTE_isPdfEmbedded = "isPdfEmbedded";
        public static final String ATTRIBUTE_condition = "condition";
        
        private Vector conditionedStyles = new Vector();
        
        public static final String[] JRXMLStyleAttributes = {"name",
                                                            "isDefault",
                                                            "style",
                                                            "mode",
                                                            "forecolor",
                                                            "backcolor",
                                                            "pen",
                                                            "fill",
                                                            "radius",
                                                            "scaleImage",
                                                            "hAlign",
                                                            "vAlign",
                                                            "border",
                                                            "borderColor",
                                                            "padding",
                                                            "topBorder",
                                                            "topBorderColor",
                                                            "topPadding",
                                                            "leftBorder",
                                                            "leftBorderColor",
                                                            "leftPadding",
                                                            "bottomBorder",
                                                            "bottomBorderColor",
                                                            "bottomPadding",
                                                            "rightBorder",
                                                            "rightBorderColor",
                                                            "rightPadding",
                                                            "rotation",
                                                            "lineSpacing",
                                                            "isStyledText",
                                                            "fontName",
                                                            "fontSize",
                                                            "isBold",
                                                            "isItalic",
                                                            "isUnderline",
                                                            "isStrikeThrough",
                                                            "pdfFontName",
                                                            "pdfEncoding",
                                                            "isPdfEmbedded",
                                                            "pattern",
                                                            "isBlankWhenNull"};
        
    private String name = "";
    private HashMap attributes = new HashMap();
    private Style defaultStyle = null;

    /**
     * Create an empty style
     */
    public Style()
    {}

    /**
     * Create a new style with the same values of s1
     * isDefault is reset to false.
     */
    public Style(Style s1)
    {
        Iterator iterator_style = s1.getAttributes().keySet().iterator();
        while (iterator_style.hasNext())
        {
            Object key = iterator_style.next();
            Object val =  s1.getAttributes().get(key);
            this.getAttributes().put(key, val);
        }
        this.getAttributes().put( this.ATTRIBUTE_isDefault, "false");
        
        getConditionedStyles().clear();
        for (int i=0; i<s1.getConditionedStyles().size(); ++i)
        {
            Style c_s = (Style)s1.getConditionedStyles().elementAt(i);
            getConditionedStyles().addElement( new ConditionedStyle(c_s));
        }
    }

    /**
     * Copy all values of s1 in the current style. The subStyles are not duplicated!
     * A cimply copy of conditionedStyles vector is performed
     */
    public void copyStyleFrom(Style s1)
    {
        Iterator iterator_style = s1.getAttributes().keySet().iterator();
        while (iterator_style.hasNext())
        {
            Object key = iterator_style.next();
            Object val =  s1.getAttributes().get(key);
            this.getAttributes().put(key, val);
        }
        this.setConditionedStyles( s1.getConditionedStyles());  
    }
    
    
    public String getName() {
        return  (getAttributes().get(ATTRIBUTE_name) == null) ? null : getAttributes().get(ATTRIBUTE_name)+"";
    }

    public void setName(String name) {
        getAttributes().put(ATTRIBUTE_name, name);
    }

    public HashMap getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap attributes) {
        this.attributes = attributes;
    }
    
    /**
     * This method return the attribute value. 
     */
    public Object getAttribute(String attribute)
    {
        if ( getAttributes().containsKey(attribute)) return getAttributes().get(attribute);
        //if ( attribute != ATTRIBUTE_parent && getParent() != null ) return getParent().getAttribute(attribute);
        return null;
    }
    
    /**
     * This method return the attribute value. 
     */
    public Object getAttribute(String attribute, boolean lookInParent)
    {
        
        return getAttributeSafe(attribute, lookInParent, null);
    }
    
    /**
     * This method return the attribute value. 
     */
    private Object getAttributeSafe(String attribute, boolean lookInParent,java.util.Collection coll)
    {
        if ( getAttributes().containsKey(attribute)) return getAttributes().get(attribute);
        
        if (lookInParent && attribute != ATTRIBUTE_style && getParent() != null )
        {
            if (coll == null) coll = new java.util.ArrayList();
            coll.add(this);
            if (coll.contains(getParent())) return null; //CYCLE!!!!! this check avoid infinite loop

            return getParent().getAttributeSafe(attribute, true,coll);
        }
        return null;
    }
    
    public String getAttributeString(String attribute, String defaultString)
    {
        
        return getAttributeString(attribute, defaultString, false);
    }
    /**
     * This method is the same as getAttribute(String attribute).
     */
    public String getAttributeString(String attribute, String defaultString, boolean lookInParentToo)
    {
        Object obj = this.getAttribute(attribute, true);
        return ((obj != null) ? ""+obj : defaultString);
    }
    
    
    public int getAttributeInteger(String attribute, int defaultInt)
    {
        return getAttributeInteger(attribute, defaultInt, false);
    }
    
    /**
     * This method is the same as getAttribute(String attribute).
     */
    public int getAttributeInteger(String attribute, int defaultInt, boolean lookInParentToo)
    {
        Object obj = this.getAttribute(attribute, true);
        if (obj == null) return defaultInt;
        if (obj instanceof Double) return ((Double)obj).intValue();
        if (obj instanceof Integer) return ((Integer)obj).intValue();
        try {
            
            return Integer.parseInt(obj+"");
        } catch (Exception ex)
        {
            
        }
        return defaultInt;
    }
    
    /**
     * This method is the same as getAttribute(String attribute, true).
     */
    public double getAttributeDouble(String attribute, double defaultDouble)
    {
        Object obj = this.getAttribute(attribute);
        if (obj == null) return defaultDouble;
        if (obj instanceof Double) return ((Double)obj).doubleValue();
        if (obj instanceof Integer) return ((Integer)obj).intValue()*1.0;
        return defaultDouble;
    }
    
     public Color getAttributeColor(String attribute, Color defaultColor)
    {
         
         return getAttributeColor(attribute, defaultColor, false);
     }
    /**
     * This method is the same as getAttribute(String attribute, true).
     */
    public Color getAttributeColor(String attribute, Color defaultColor, boolean lookInParentToo)
    {
        Object obj = this.getAttribute(attribute);
        if (obj == null) return defaultColor;
        if (obj instanceof Color) return (Color)obj;
        Color c = ColorSelectorPanel.parseColorString(obj+"");
        if (c != null) return c;
        
        return defaultColor;
    } 
    
    public boolean getAttributeBoolean(String attribute, boolean defaultBoolean)
    {
        return getAttributeBoolean(attribute, defaultBoolean, false);
    }
    
     /**
     * This method is the same as getAttribute(String attribute, true).
     */
    public boolean getAttributeBoolean(String attribute, boolean defaultBoolean, boolean lookInParentToo)
    {
        Object obj = this.getAttribute(attribute, lookInParentToo);
        if (obj == null) return defaultBoolean;
        if (obj instanceof Boolean) return ((Boolean)obj).booleanValue();
        try {
             return Boolean.valueOf(obj+"").booleanValue();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return defaultBoolean;
    } 
       
    public Style getParent()
    {
        Object obj = this.getAttributes().get(ATTRIBUTE_style);
        if (obj == null || !(obj instanceof Style))
        {
            //this.getAttributes().remove(ATTRIBUTE_style);
            return null;
        }
        
        return (Style)obj;
    }
        
    public String toString()
    {
        return this.getName()+"";
    }

    public Vector getConditionedStyles() {
        return conditionedStyles;
    }

    public void setConditionedStyles(Vector conditionedStyles) {
        this.conditionedStyles = conditionedStyles;
    }
    
    
    public static boolean isDefaultValue(Object value, String attributeName, Style elementStyle, Style defaultStyle)
    {
        
        if (elementStyle == null && defaultStyle == null)
        {
            return false;
        }
        
        Style referenceStyle = (elementStyle == null) ? defaultStyle : elementStyle;
        
        Object defaultValue = referenceStyle.getAttribute(attributeName, true);
        
        if (defaultValue == null) return false;
        
        if (attributeName.endsWith("color") && value instanceof Color)
        {
            Color color = it.businesslogic.ireport.gui.sheet.ColorSelectorPanel.parseColorString(""+defaultValue); 
            return color.equals((Color)value);
        }
        
        return defaultValue.equals(value);
    }
}
