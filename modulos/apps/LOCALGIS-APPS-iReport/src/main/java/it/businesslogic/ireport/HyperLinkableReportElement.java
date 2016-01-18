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
 * HyperLinkableReportElement.java
 * 
 * Created on 26 maggio 2003, 10.47
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public interface HyperLinkableReportElement {
    /** Getter for property hyperlinkType.
         * @return Value of property hyperlinkType.
         *
         */
        public java.lang.String getHyperlinkType();
        
        /** Setter for property hyperlinkType.
         * @param hyperlinkType New value of property hyperlinkType.
         *
         */
        public void setHyperlinkType(java.lang.String hyperlinkType);
        
        /** Getter for property anchorNameExpression.
         * @return Value of property anchorNameExpression.
         *
         */
        public java.lang.String getAnchorNameExpression();
        
        /** Setter for property anchorNameExpression.
         * @param anchorNameExpression New value of property anchorNameExpression.
         *
         */
        public void setAnchorNameExpression(java.lang.String anchorNameExpression);
        
        /** Getter for property hyperlinkReferenceExpression.
         * @return Value of property hyperlinkReferenceExpression.
         *
         */
        public java.lang.String getHyperlinkReferenceExpression();
        
        /** Setter for property hyperlinkReferenceExpression.
         * @param hyperlinkReferenceExpression New value of property hyperlinkReferenceExpression.
         *
         */
        public void setHyperlinkReferenceExpression(java.lang.String hyperlinkReferenceExpression);
        
        /** Getter for property hyperlinkAnchorExpression.
         * @return Value of property hyperlinkAnchorExpression.
         *
         */
        public java.lang.String getHyperlinkAnchorExpression();
        
        /** Setter for property hyperlinkAnchorExpression.
         * @param hyperlinkAnchorExpression New value of property hyperlinkAnchorExpression.
         *
         */
        public void setHyperlinkAnchorExpression(java.lang.String hyperlinkAnchorExpression);
        
        /** Getter for property hyperlinkPageExpression.
         * @return Value of property hyperlinkPageExpression.
         *
         */
        public java.lang.String getHyperlinkPageExpression() ;
        
        /** Setter for property hyperlinkPageExpression.
         * @param hyperlinkPageExpression New value of property hyperlinkPageExpression.
         *
         */
        public void setHyperlinkPageExpression(java.lang.String hyperlinkPageExpression);
        
        /** Getter for property hyperlinkTarget.
         * @return Value of property hyperlinkTarget.
         *
         */
        public java.lang.String getHyperlinkTarget() ;
        
        /** Setter for property hyperlinkTarget.
         * @param hyperlinkTarget New value of property hyperlinkTarget.
         *
         */
        public void setHyperlinkTarget(java.lang.String hyperlinkTarget);
        
        /** Getter for property bookmarkLevel.
         * @return Value of property bookmarkLevel.
         *
         */
        public int getBookmarkLevel() ;
        
        /** Setter for property bookmarkLevel.
         * @param bookmarkLevel New value of property bookmarkLevel.
         *
         */
        public void setBookmarkLevel(int bookmarkLevel);
        
        /** Getter for property TooltipExpression.
         * @return Value of property linkParameters.
         *
         */
        public String getTooltipExpression() ;
        
        /** Setter for property TooltipExpression.
         * @param bookmarkLevel New value of property linkParameters.
         *
         */
        public void setTooltipExpression(String s);
        
        /** Getter for property linkParameters.
         * @return Value of property linkParameters.
         *
         */
        public java.util.List getLinkParameters() ;
        
        /** Setter for property linkParameters.
         * @param bookmarkLevel New value of property linkParameters.
         *
         */
        public void setLinkParameters(java.util.List linkParameters);
}
