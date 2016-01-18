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
 * SectionItemHyperlink.java
 * 
 * Created on September 1, 2006, 1:44 PM
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.JRLinkParameter;

/**
 *
 * @author gtoffoli
 */
public class SectionItemHyperlink {
    
    /* Item or section hyperlink infos */
    private String hyperlinkReferenceExpression = "";
    private String hyperlinkAnchorExpression = "";
    private String hyperlinkPageExpression = "";
    private String hyperlinkTooltipExpression = "";
    private String hyperlinkType = "None";
    private String hyperlinkTarget = "Self";
    private java.util.List hyperlinkParameters = new java.util.ArrayList();
    
    /** Creates a new instance of SectionItemHyperlink */
    public SectionItemHyperlink() {
    }
    
    public SectionItemHyperlink cloneMe()
    {
        SectionItemHyperlink s = new SectionItemHyperlink();
        s.setHyperlinkReferenceExpression( new String(hyperlinkReferenceExpression) );
        s.setHyperlinkAnchorExpression( new String(hyperlinkAnchorExpression) );
        s.setHyperlinkPageExpression( new String(hyperlinkPageExpression) );
        s.setHyperlinkTooltipExpression( new String(hyperlinkTooltipExpression) );
        s.setHyperlinkType( new String(hyperlinkType) );
        s.setHyperlinkTarget( new String(hyperlinkTarget) );
        
        for (int i=0; i<hyperlinkParameters.size(); ++i)
        {
            JRLinkParameter lp = (JRLinkParameter)hyperlinkParameters.get(i);
            s.getHyperlinkParameters().add( new JRLinkParameter(lp.getName(), lp.getExpression()));
        }
        
        return s;
    }
    
    public String getHyperlinkReferenceExpression() {
        return hyperlinkReferenceExpression;
    }

    public void setHyperlinkReferenceExpression(String hyperlinkReferenceExpression) {
        this.hyperlinkReferenceExpression = hyperlinkReferenceExpression;
    }

    public String getHyperlinkAnchorExpression() {
        return hyperlinkAnchorExpression;
    }

    public void setHyperlinkAnchorExpression(String hyperlinkAnchorExpression) {
        this.hyperlinkAnchorExpression = hyperlinkAnchorExpression;
    }

    public String getHyperlinkPageExpression() {
        return hyperlinkPageExpression;
    }

    public void setHyperlinkPageExpression(String hyperlinkPageExpression) {
        this.hyperlinkPageExpression = hyperlinkPageExpression;
    }

    public String getHyperlinkTooltipExpression() {
        return hyperlinkTooltipExpression;
    }

    public void setHyperlinkTooltipExpression(String hyperlinkTooltipExpression) {
        this.hyperlinkTooltipExpression = hyperlinkTooltipExpression;
    }

    public String getHyperlinkType() {
        return hyperlinkType;
    }

    public void setHyperlinkType(String hyperlinkType) {
        this.hyperlinkType = hyperlinkType;
    }

    public String getHyperlinkTarget() {
        return hyperlinkTarget;
    }

    public void setHyperlinkTarget(String hyperlinkTarget) {
        this.hyperlinkTarget = hyperlinkTarget;
    }

    public java.util.List getHyperlinkParameters() {
        return hyperlinkParameters;
    }

    public void setHyperlinkParameters(java.util.List hyperlinkParameters) {
        this.hyperlinkParameters = hyperlinkParameters;
    }
    
}
