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
public class ReportObjectTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
    
    /** Creates a new instance of DocumentStructureTreeNode */
    public ReportObjectTreeNode() {
        super();
    }
    
    public ReportObjectTreeNode(Object userObject) {
        super(userObject);
    }
    
    public ReportObjectTreeNode(Object userObject, boolean allowChilds) {
        super(userObject, allowChilds);
    }
    
    public String getNodeId()
    {
        String nodeId = "unknow";
        if (this.getUserObject() != null) nodeId = "" + this.getUserObject();
        if (this.getParent() != null && this.getParent() instanceof ReportObjectTreeNode)
        {
            nodeId = ((ReportObjectTreeNode)this.getParent()).getNodeId() + "." + nodeId;
        }
        else
        {
            System.out.println("Unable to go up with " + this.getParent() + " (" + this +" root: " + this.getRoot() );
        }
        return nodeId;
    }
   /*
    public String toString()
    {
        return getNodeId();
    }
    */
    
    public boolean equals(Object obj)
    {
        //if (obj instanceof ReportObjectTreeNode)
        //{
        //     return ((ReportObjectTreeNode)obj).getNodeId().equalsIgnoreCase( getNodeId() );
        //}
        return super.equals( obj );
    }
}
