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
 * Box.java
 * 
 * Created on 29 novembre 2004, 17.09
 *
 */

package it.businesslogic.ireport;
import java.awt.Color;
/**
 *
 * @author  Administrator
 */
public class Box {
    
    /** Creates a new instance of Box */
    public Box() {
    }
    
    private String border = "None";
    private Color borderColor = java.awt.Color.BLACK;
    private int padding = 0;
    
    private String topBorder = "None";
    private Color topBorderColor = java.awt.Color.BLACK;
    private int topPadding = 0;
    
    private String bottomBorder = "None";
    private Color bottomBorderColor = java.awt.Color.BLACK;
    private int bottomPadding = 0;
    
    private String leftBorder = "None";
    private Color leftBorderColor = java.awt.Color.BLACK;
    private int leftPadding = 0;
    
    private String rightBorder = "None";
    private Color rightBorderColor = java.awt.Color.BLACK;
    private int rightPadding = 0;

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public String getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(String topBorder) {
        this.topBorder = topBorder;
    }

    public Color getTopBorderColor() {
        return topBorderColor;
    }

    public void setTopBorderColor(Color topBorderColor) {
        this.topBorderColor = topBorderColor;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public String getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(String bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public Color getBottomBorderColor() {
        return bottomBorderColor;
    }

    public void setBottomBorderColor(Color bottomBorderColor) {
        this.bottomBorderColor = bottomBorderColor;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public String getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(String leftBorder) {
        this.leftBorder = leftBorder;
    }

    public Color getLeftBorderColor() {
        return leftBorderColor;
    }

    public void setLeftBorderColor(Color leftBorderColor) {
        this.leftBorderColor = leftBorderColor;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public String getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(String rightBorder) {
        this.rightBorder = rightBorder;
    }

    public Color getRightBorderColor() {
        return rightBorderColor;
    }

    public void setRightBorderColor(Color rightBorderColor) {
        this.rightBorderColor = rightBorderColor;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }
    
    public Box cloneMe()
    {
        Box bb = new Box();
        bb.setRightBorderColor( this.getRightBorderColor());
        bb.setRightBorder( this.getRightBorder());
        bb.setRightPadding( this.getRightPadding());
        
        bb.setLeftBorderColor( this.getLeftBorderColor());
        bb.setLeftBorder( this.getLeftBorder());
        bb.setLeftPadding( this.getLeftPadding());
        
        bb.setTopBorderColor( this.getTopBorderColor());
        bb.setTopBorder( this.getTopBorder());
        bb.setTopPadding( this.getTopPadding());
        
        bb.setBottomBorderColor( this.getBottomBorderColor());
        bb.setBottomBorder( this.getBottomBorder());
        bb.setBottomPadding( this.getBottomPadding());
        
        bb.setBorderColor( this.getBorderColor());
        bb.setBorder( this.getBorder());
        bb.setPadding( this.getPadding());
        
        return bb;
    }
    
}
