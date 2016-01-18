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
 * DocumentStructureTreeNode.java
 * 
 * Created on 28 novembre 2005, 16.07
 *
 */

package it.businesslogic.ireport.gui.documentstructure;

import it.businesslogic.ireport.ElementGroup;
import it.businesslogic.ireport.ReportElement;

/**
 *
 * @author Administrator
 */
public class DocumentStructureTreeNode extends ReportObjectTreeNode {
    
    /** Creates a new instance of DocumentStructureTreeNode */
    public DocumentStructureTreeNode() {
        super();
    }
    
    public DocumentStructureTreeNode(Object userObject) {
        super(userObject);
    }
    
    public DocumentStructureTreeNode(Object userObject, boolean allowChilds) {
        super(userObject, allowChilds);
    }
    
    public String getNodeId()
    {
        if (this.getUserObject() == null) return "";
        if (this.getUserObject() instanceof ElementGroup)
        {
            return "group-" + ((ElementGroup)(this.getUserObject())).getName();
        }
        if (this.getUserObject() instanceof ReportElement)
        {
            return "element-" + ((ReportElement)(this.getUserObject())).getElementID()+"";
        }
    
        
        return "" + this.getUserObject();
    }
   
}
