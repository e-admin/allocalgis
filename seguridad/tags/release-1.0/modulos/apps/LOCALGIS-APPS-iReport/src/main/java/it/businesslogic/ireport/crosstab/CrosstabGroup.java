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
 * CrosstabGroup.java
 * 
 * Created on 5 gennaio 2006, 15.52
 *
 */

package it.businesslogic.ireport.crosstab;

/**
 *
 * @author Administrator
 */
public class CrosstabGroup {
    
    
    protected CrosstabCell headerCell = null;
    protected CrosstabCell totalCell = null;
    private int size = 0;
    
    private boolean hasHeader = true;
    private boolean hasTotal = true;
    
    protected String name = "";
    protected String totalPosition = "None";
    protected String headerPosition = "";
    
    private String bucketExpression = "";
    private String bucketExpressionClass = "";
    private String bucketOrder = "Ascending";
    private String bucketComparatorExpression = "";
    
    /** Creates a new instance of CrosstabGroup */
    public CrosstabGroup() {
    }

    public CrosstabCell getHeaderCell() {
        return headerCell;
    }

    public void setHeaderCell(CrosstabCell headerCell) {
        this.headerCell = headerCell;
    }

    public CrosstabCell getTotalCell() {
        return totalCell;
    }

    public void setTotalCell(CrosstabCell totalCell) {
        this.totalCell = totalCell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPosition() {
        return totalPosition;
    }

    public void setTotalPosition(String totalPosition) {
        this.totalPosition = totalPosition;
    }

    public String getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderPosition(String headerPosition) {
        this.headerPosition = headerPosition;
    }

    public String getBucketExpression() {
        return bucketExpression;
    }

    public void setBucketExpression(String bucketExpression) {
        this.bucketExpression = bucketExpression;
    }

    public String getBucketOrder() {
        return bucketOrder;
    }

    public void setBucketOrder(String bucketOrder) {
        this.bucketOrder = bucketOrder;
    }

    public String getBucketComparatorExpression() {
        return bucketComparatorExpression;
    }

    public void setBucketComparatorExpression(String bucketComparatorExpression) {
        this.bucketComparatorExpression = bucketComparatorExpression;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isHasHeader() {
        return true;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public String getBucketExpressionClass() {
        return bucketExpressionClass;
    }

    public void setBucketExpressionClass(String bucketExpressionClass) {
        this.bucketExpressionClass = bucketExpressionClass;
    }

    public boolean isHasTotal() {
        return !getTotalPosition().equals("None");
    }

    public void setHasTotal(boolean hasTotal) {
        this.hasTotal = hasTotal;
    }
    
    public String toString()
    {
        return "" + name;
    }
    
    
}
