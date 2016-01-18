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
 * Group.java
 * 
 */

package it.businesslogic.ireport;

public class Group
{
	private SubDataset parent;
	private String name;
	private String groupExpression = "";
	private boolean isStartNewColumn=false;
	private boolean isStartNewPage=false;
	private boolean isResetPageNumber=false;
	private boolean isReprintHeaderOnEachPage=false;
	private int minHeightToStartNewPage=0;
	
	private Band groupHeader=null;
	private Band groupFooter=null;

	public Group(SubDataset parent, String name)
	{
		this(parent, name, 0, 0);
	}
	
	public Group(SubDataset parent, String name, int headerHeight, int footerHeight)
	{
		this.parent = parent;		 
		this.name = name;
                if (parent instanceof Report)
                {
                    groupFooter = new Band((Report)parent, name+"Footer", headerHeight);
                    groupFooter.setGroup(this);
                    groupFooter.setGroupFooter(true);
                    groupHeader = new Band((Report)parent, name+"Header", footerHeight);
                    groupHeader.setGroup(this);
                    groupHeader.setGroupHeader(true);
                }
	}
        
        /** Getter for property groupExpression.
         * @return Value of property groupExpression.
         *
         */
        public java.lang.String getGroupExpression() {
            return groupExpression;
        }
        
        /** Setter for property groupExpression.
         * @param groupExpression New value of property groupExpression.
         *
         */
        public void setGroupExpression(java.lang.String groupExpression) {
            this.groupExpression = groupExpression;
        }
        
        /** Getter for property groupFooter.
         * @return Value of property groupFooter.
         *
         */
        public it.businesslogic.ireport.Band getGroupFooter() {
            return groupFooter;
        }
        
        /** Setter for property groupFooter.
         * @param groupFooter New value of property groupFooter.
         *
         */
        public void setGroupFooter(it.businesslogic.ireport.Band groupFooter) {
            this.groupFooter = groupFooter;
        }
        
        /** Getter for property groupHeader.
         * @return Value of property groupHeader.
         *
         */
        public it.businesslogic.ireport.Band getGroupHeader() {
            return groupHeader;
        }
        
        /** Setter for property groupHeader.
         * @param groupHeader New value of property groupHeader.
         *
         */
        public void setGroupHeader(it.businesslogic.ireport.Band groupHeader) {
            this.groupHeader = groupHeader;
        }
        
        /** Getter for property isReprintHeaderOnEachPage.
         * @return Value of property isReprintHeaderOnEachPage.
         *
         */
        public boolean isIsReprintHeaderOnEachPage() {
            return isReprintHeaderOnEachPage;
        }
        
        /** Setter for property isReprintHeaderOnEachPage.
         * @param isReprintHeaderOnEachPage New value of property isReprintHeaderOnEachPage.
         *
         */
        public void setIsReprintHeaderOnEachPage(boolean isReprintHeaderOnEachPage) {
            this.isReprintHeaderOnEachPage = isReprintHeaderOnEachPage;
        }
        
        /** Getter for property isResetPageNumber.
         * @return Value of property isResetPageNumber.
         *
         */
        public boolean isIsResetPageNumber() {
            return isResetPageNumber;
        }
        
        /** Setter for property isResetPageNumber.
         * @param isResetPageNumber New value of property isResetPageNumber.
         *
         */
        public void setIsResetPageNumber(boolean isResetPageNumber) {
            this.isResetPageNumber = isResetPageNumber;
        }
        
        /** Getter for property isStartNewColumn.
         * @return Value of property isStartNewColumn.
         *
         */
        public boolean isIsStartNewColumn() {
            return isStartNewColumn;
        }
        
        /** Setter for property isStartNewColumn.
         * @param isStartNewColumn New value of property isStartNewColumn.
         *
         */
        public void setIsStartNewColumn(boolean isStartNewColumn) {
            this.isStartNewColumn = isStartNewColumn;
        }
        
        /** Getter for property isStartNewPage.
         * @return Value of property isStartNewPage.
         *
         */
        public boolean isIsStartNewPage() {
            return isStartNewPage;
        }
        
        /** Setter for property isStartNewPage.
         * @param isStartNewPage New value of property isStartNewPage.
         *
         */
        public void setIsStartNewPage(boolean isStartNewPage) {
            this.isStartNewPage = isStartNewPage;
        }
        
        /** Getter for property minHeightToStartNewPage.
         * @return Value of property minHeightToStartNewPage.
         *
         */
        public int getMinHeightToStartNewPage() {
            return minHeightToStartNewPage;
        }
        
        /** Setter for property minHeightToStartNewPage.
         * @param minHeightToStartNewPage New value of property minHeightToStartNewPage.
         *
         */
        public void setMinHeightToStartNewPage(int minHeightToStartNewPage) {
            this.minHeightToStartNewPage = minHeightToStartNewPage;
        }
        
        /** Getter for property name.
         * @return Value of property name.
         *
         */
        public java.lang.String getName() {
            return name;
        }
        
        /** Setter for property name.
         * @param name New value of property name.
         *
         */
        public void setName(java.lang.String name) {
            this.name = name;
        }
        
        /** Getter for property parent.
         * @return Value of property parent.
         *
         */
        public it.businesslogic.ireport.SubDataset getParent() {
            return parent;
        }  
        
        public String toString()
        {
            return this.getName();
        }
}
