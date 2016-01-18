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
 * TextFieldReportElement.java
 * 
 * Created on 24 maggio 2003, 10.22
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.MainFrame;

/**
 *
 * @author  Administrator
 */
public class TextFieldReportElement extends it.businesslogic.ireport.TextReportElement  implements  HyperLinkableReportElement {
    
    
    public final static String PATTERN = "PATTERN";
    public final static String DEFAULT_PATTERN = "";
    
    private String classExpression;
    
    private String group;
    
    private String evaluationTime;
    
    private boolean blankWhenNull;
    
    private boolean stretchWithOverflow;
    
    //private String pattern;
    
    private String anchorNameExpression = "";
    
    private String hyperlinkAnchorExpression = "";
    
    private String hyperlinkPageExpression = "";
    
    private String hyperlinkReferenceExpression = "";
    
    private String hyperlinkType = "None";
    
    private String tooltip = "";
    
    private int bookmarkLevel = 0;
    
    private java.util.List linkParameters = new java.util.ArrayList();
    
    static private java.util.List acceptedTextfieldClasses = null;
    static {
        acceptedTextfieldClasses = new java.util.ArrayList();

        acceptedTextfieldClasses.add("java.lang.Boolean");
        acceptedTextfieldClasses.add("java.lang.Byte");
        acceptedTextfieldClasses.add("java.util.Date");
        acceptedTextfieldClasses.add("java.sql.Timestamp");
        acceptedTextfieldClasses.add("java.sql.Time");
        acceptedTextfieldClasses.add("java.lang.Double");
        acceptedTextfieldClasses.add("java.lang.Float");
        acceptedTextfieldClasses.add("java.lang.Integer");
        acceptedTextfieldClasses.add("java.lang.Long");
        acceptedTextfieldClasses.add("java.lang.Short");
        acceptedTextfieldClasses.add("java.math.BigDecimal");
        acceptedTextfieldClasses.add("java.lang.Number");
        acceptedTextfieldClasses.add("java.lang.String");
    }
    
    
    /** Creates a new instance of TextFieldReportElement */
    public TextFieldReportElement(int x, int y, int width, int height)
    {
		super(x,y,width,height);
                this.setText("$F{Field}");
                //this.pattern = "";
                this.stretchWithOverflow = false;
                this.blankWhenNull = false;
                this.group = "";
                this.evaluationTime = "Now";
                this.classExpression = "java.lang.String";     
                this.hyperlinkType = "None";
                this.anchorNameExpression = "";
                
                setKey("textField");
    }    
    
    /** Getter for property blankWhenNull.
     * @return Value of property blankWhenNull.
     *
     */
    public boolean isBlankWhenNull() {
        return blankWhenNull;
    }
    
    /** Setter for property blankWhenNull.
     * @param blankWhenNull New value of property blankWhenNull.
     *
     */
    public void setBlankWhenNull(boolean blankWhenNull) {
        this.blankWhenNull = blankWhenNull;
    }
    
    /** Getter for property classExpression.
     * @return Value of property classExpression.
     *
     */
    public java.lang.String getClassExpression() {
        return classExpression;
    }
    
    /** Setter for property classExpression.
     * @param classExpression New value of property classExpression.
     *
     */
    public void setClassExpression(java.lang.String classExpression) {
        this.classExpression = classExpression;
    }
    
    
    /** Setter for property classExpression.
     * If newClass is not in 
     * java.lang.Boolean
     * java.lang.Byte
     * java.util.Date
     * java.sql.Timestamp
     * java.sql.Time
     * java.lang.Double
     * java.lang.Float
     * java.lang.Integer
     * java.lang.Long
     * java.lang.Short
     * java.math.BigDecimal
     * java.lang.Number
     * java.lang.String
     * the method trys to find something of similar using getSuperClass
     * it is used.
     *
     * In no solution is find, the type is set to java.lang.String an optionally is possible
     * change the expression with something like ""+<exp>. This should be very rare...
     * @param classExpression New value of property classExpression.
     * 
     */
    public void setMatchingClassExpression(java.lang.String newClassName, boolean adjustExpression) {
        
        if (!acceptedTextfieldClasses.contains(newClassName))
        {
            try {
                Class newClass = MainFrame.getMainInstance().getReportClassLoader().loadClass( newClassName );
                setMatchingClassExpression(newClass, adjustExpression);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            setClassExpression(newClassName);
        }
    }
    
    private void setMatchingClassExpression(Class newClass, boolean adjustExpression) {
        
        // try with the superclasss..
        Class superClass = newClass.getSuperclass();

        if (superClass == null)
        {
            this.classExpression = classExpression;
            if (adjustExpression)
            {
                this.setText("\"\"+" + getText());
            }
        }
        else
        {
            setMatchingClassExpression(superClass.getName(), adjustExpression);
        }
    }
    
    
    /** Getter for property evaluationTime.
     * @return Value of property evaluationTime.
     *
     */
    public java.lang.String getEvaluationTime() {
        return evaluationTime;
    }
    
    /** Setter for property evaluationTime.
     * @param evaluationTime New value of property evaluationTime.
     *
     */
    public void setEvaluationTime(java.lang.String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
    
    /** Getter for property group.
     * @return Value of property group.
     *
     */
    public java.lang.String getGroup() {
        return group;
    }
    
    /** Setter for property group.
     * @param group New value of property group.
     *
     */
    public void setGroup(java.lang.String group) {
        this.group = group;
    }
    
    /** Getter for property pattern.
     * @return Value of property pattern.
     *
     */
    public java.lang.String getPattern() {
        if (getPropertyValue( PATTERN) == null)
       {
            // Look for a fgcolor in the stylesheet...
            if (getStyle() != null)
            {
               return getStyle().getAttributeString( getStyle().ATTRIBUTE_pattern, DEFAULT_PATTERN, true);
            }
        }
        return getStringValue( PATTERN, DEFAULT_PATTERN );
    }
    
    /** Setter for property pattern.
     * @param pattern New value of property pattern.
     *
     */
    public void setPattern(java.lang.String pattern) {
        setPropertyValue(PATTERN, pattern);
    }
    
    /** Getter for property stretchWithOverflow.
     * @return Value of property stretchWithOverflow.
     *
     */
    public boolean isStretchWithOverflow() {
        return stretchWithOverflow;
    }
    
    /** Setter for property stretchWithOverflow.
     * @param stretchWithOverflow New value of property stretchWithOverflow.
     *
     */
    public void setStretchWithOverflow(boolean stretchWithOverflow) {
        this.stretchWithOverflow = stretchWithOverflow;
    }
    
    /** Getter for property anchorNameExpression.
     * @return Value of property anchorNameExpression.
     *
     */
    public java.lang.String getAnchorNameExpression() {
        return anchorNameExpression;
    }
    
    /** Setter for property anchorNameExpression.
     * @param anchorNameExpression New value of property anchorNameExpression.
     *
     */
    public void setAnchorNameExpression(java.lang.String anchorNameExpression) {
        this.anchorNameExpression = anchorNameExpression;
    }
    
    /** Getter for property hyperlinkAnchorExpression.
     * @return Value of property hyperlinkAnchorExpression.
     *
     */
    public java.lang.String getHyperlinkAnchorExpression() {
        return hyperlinkAnchorExpression;
    }
    
    /** Setter for property hyperlinkAnchorExpression.
     * @param hyperlinkAnchorExpression New value of property hyperlinkAnchorExpression.
     *
     */
    public void setHyperlinkAnchorExpression(java.lang.String hyperlinkAnchorExpression) {
        this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
    }
    
    /** Getter for property hyperlinkPageExpression.
     * @return Value of property hyperlinkPageExpression.
     *
     */
    public java.lang.String getHyperlinkPageExpression() {
        return hyperlinkPageExpression;
    }
    
    /** Setter for property hyperlinkPageExpression.
     * @param hyperlinkPageExpression New value of property hyperlinkPageExpression.
     *
     */
    public void setHyperlinkPageExpression(java.lang.String hyperlinkPageExpression) {
        this.hyperlinkPageExpression = hyperlinkPageExpression;
    }
    
    /** Getter for property hyperlinkReferenceExpression.
     * @return Value of property hyperlinkReferenceExpression.
     *
     */
    public java.lang.String getHyperlinkReferenceExpression() {
        return hyperlinkReferenceExpression;
    }
    
    /** Setter for property hyperlinkReferenceExpression.
     * @param hyperlinkReferenceExpression New value of property hyperlinkReferenceExpression.
     *
     */
    public void setHyperlinkReferenceExpression(java.lang.String hyperlinkReferenceExpression) {
        this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
    }
    
    /** Getter for property hyperlinkType.
     * @return Value of property hyperlinkType.
     *
     */
    public java.lang.String getHyperlinkType() {
        return hyperlinkType;
    }
    
    /** Setter for property hyperlinkType.
     * @param hyperlinkType New value of property hyperlinkType.
     *
     */
    public void setHyperlinkType(java.lang.String hyperlinkType) {
        this.hyperlinkType = hyperlinkType;
    }
    
    public ReportElement cloneMe()
    {
	TextFieldReportElement newReportElement = new TextFieldReportElement(position.x, position.y, width, height);
	copyBaseReportElement(newReportElement, this);
	return newReportElement;
    }
        
    public void copyBaseReportElement(ReportElement destination, ReportElement source)
        {
                super.copyBaseReportElement(destination, source);
                
                if (destination instanceof TextFieldReportElement &&
                    source instanceof TextFieldReportElement )
                {
                    destination.setPropertyValue(PATTERN, source.getPropertyValue(PATTERN));
                    ((TextFieldReportElement)destination).setBlankWhenNull( ((TextFieldReportElement)source).isBlankWhenNull());
                    ((TextFieldReportElement)destination).setClassExpression(new String(  ((TextFieldReportElement)source).getClassExpression() ));
                    ((TextFieldReportElement)destination).setEvaluationTime(new String(  ((TextFieldReportElement)source).getEvaluationTime() ));
                    ((TextFieldReportElement)destination).setGroup(new String( ((TextFieldReportElement)source).getGroup()));
                    //((TextFieldReportElement)destination).setPattern(new String(  ((TextFieldReportElement)source).getPattern() ));
                    ((TextFieldReportElement)destination).setStretchWithOverflow(  ((TextFieldReportElement)source).isStretchWithOverflow() );
                    
                    
                    ((HyperLinkableReportElement)destination).setAnchorNameExpression(new String(  ((HyperLinkableReportElement)source).getAnchorNameExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkAnchorExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkAnchorExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkPageExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkPageExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkReferenceExpression(new String(  ((HyperLinkableReportElement)source).getHyperlinkReferenceExpression() ));
                    ((HyperLinkableReportElement)destination).setHyperlinkType(new String(  ((HyperLinkableReportElement)source).getHyperlinkType() ));
                }
        }
    
    protected String hyperlinkTarget = "Self";
    /** Getter for property hyperlinkTarget.
         * @return Value of property hyperlinkTarget.
         *
         */
        public java.lang.String getHyperlinkTarget(){ return hyperlinkTarget; }
        
        /** Setter for property hyperlinkTarget.
         * @param hyperlinkTarget New value of property hyperlinkTarget.
         *
         */
        public void setHyperlinkTarget(java.lang.String hyperlinkTarget) { this.hyperlinkTarget = hyperlinkTarget; }

    public int getBookmarkLevel() {
        return bookmarkLevel;
    }

    public void setBookmarkLevel(int bookmarkLevel) {
        this.bookmarkLevel = bookmarkLevel;
    }

    public void setStyle(Style style) {
     
        super.setStyle(style);
        
        if (style != null)
        {            
            //this.setIsStyledText( style.getAttributeBoolean( style.ATTRIBUTE_isStyledText, isIsStyledText(), true));
            this.setBlankWhenNull( style.getAttributeBoolean( style.ATTRIBUTE_isBlankWhenNull, isBlankWhenNull(), true));
            //this.setPattern( style.getAttributeString( style.ATTRIBUTE_pattern, getPattern(), true));
        }
     }

    public java.util.List getLinkParameters() {
        return linkParameters;
    }

    public void setLinkParameters(java.util.List linkParameters) {
        this.linkParameters = linkParameters;
    }
    
    public String getTooltipExpression() {
        return tooltip;
    }

    public void setTooltipExpression(String s) {
        tooltip = s;
    }
}
