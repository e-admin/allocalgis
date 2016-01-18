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
 * Band.java
 * 
 * Created on 12 febbraio 2003, 20.26
 *
 */

package it.businesslogic.ireport;

public class Band
{

    private Report parent;
    private String name = "";
    private String printWhenExpression = "";
    private int height = 0;
    private boolean splitAllowed = true;
    
    private boolean groupHeader = false;

    public boolean isGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(boolean groupHeader) {
        this.groupHeader = groupHeader;
    }

    public boolean isGroupFooter() {
        return groupFooter;
    }

    public void setGroupFooter(boolean groupFooter) {
        this.groupFooter = groupFooter;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    private boolean groupFooter = false;
    private Group group = null;
    
    
    /**
     * Creates a new Band object.
     *
     * @param parent DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public Band(Report parent, String name, int height)
    {
        this.parent = parent;
        this.name = name;
        this.height = height;
        this.splitAllowed = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {

        return this.name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBandYLocation()
    {

        return parent.getBandYLocation(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getUsableWidth()
    {

        return parent.getWidth() - parent.getLeftMargin() -
               parent.getRightMargin();
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
     * Getter for property parent.
     *
     * @return Value of property parent.
     */
    public it.businesslogic.ireport.Report getParent()
    {

        return parent;
    }

    /**
     * Setter for property parent.
     *
     * @param parent New value of property parent.
     */
    public void setParent(it.businesslogic.ireport.Report parent)
    {
        this.parent = parent;
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
     * Getter for property splitAllowed.
     *
     * @return Value of property splitAllowed.
     */
    public boolean isSplitAllowed()
    {

        return splitAllowed;
    }

    /**
     * Setter for property splitAllowed.
     *
     * @param splitAllowed New value of property splitAllowed.
     */
    public void setSplitAllowed(boolean splitAllowed)
    {
        this.splitAllowed = splitAllowed;
    }
}
