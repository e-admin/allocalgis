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
 * WalkableWrapper.java
 * 
 * Created on June 20, 2006, 5:14 PM
 *
 */

package it.businesslogic.ireport.data.olap;

import it.businesslogic.ireport.util.Misc;

import javax.swing.tree.DefaultMutableTreeNode;

import mondrian.olap.FunCall;
import mondrian.olap.Hierarchy;
import mondrian.olap.Level;
import mondrian.olap.Member;
import mondrian.olap.QueryAxis;
/**
 *
 * @author gtoffoli
 */
public class WalkableWrapper {
    
    Object walkable = null;
    String name = "";
    private DefaultMutableTreeNode parentNode = null;
    
    
    /** Creates a new instance of WalkableWrapper */
    public WalkableWrapper(Object obj, DefaultMutableTreeNode parentNode) {
        this.walkable = obj;
        
        this.parentNode = parentNode;
        if (walkable instanceof QueryAxis)
        {
            name = ((QueryAxis)walkable).getAxisName();
        }
        else if (walkable instanceof FunCall)
        {
            name = ((FunCall)walkable).getFunName();
        }
        else if (walkable instanceof Level)
        {
            name = ((Level)walkable).getName();
        }
        else if (walkable instanceof Hierarchy)
        {
            name = ((Hierarchy)walkable).getName();
        }
        else if (walkable instanceof Member)
        {
            name = ((Member)walkable).getName();
        }
        else
        {
            name = getWalkable() + " (" + walkable.getClass().getName();
        }
    }
    
    public boolean isMeasure()
    {
        return (walkable instanceof Member) && ((Member)walkable).isMeasure();
    }
    
    public String getExpression()
    {
        if (isMeasure())
        {
           int qmarks = parentNode.getChildCount();
           String s = "Data(" + walkable + "";
           for (int i=0; i<qmarks-1; ++i)
           {
               s +=",?";
           }
           s +=")";
           return  s;
        }
        else if (walkable instanceof Level)
        {
            // Find the ancestor dimension...
            DefaultMutableTreeNode node = getParentNode();
            DefaultMutableTreeNode nodeParent = (DefaultMutableTreeNode)getParentNode().getParent();
            while (nodeParent.getParent() != null)
            {
                node = nodeParent;
                nodeParent = (DefaultMutableTreeNode)nodeParent.getParent();
            }
            int axisIndex = nodeParent.getIndex( node );
            
            String s = "";
            switch (axisIndex)
            {
                case 0:
                    s = "Columns";
                    break;
                case 1:
                    s = "Rows";
                    break;
                case 2:
                    s = "Pages";
                    break;
                case 3:
                    s = "Chapters";
                    break;
                case 4:
                    s = "Sections";
                    break; 
                default:
                    s = "Axis(" + axisIndex +")";
            }
            
            s = s + Misc.string_replace("][","].[", ""+ walkable) + "";
            
            return s;
        }
        else
        {
            return null;
        }
        
        //return walkable+"";
    }
    
    public Object getWalkable()
    {
        return walkable;
    }
    
    public void setWalkable(Object w)
    {
        walkable = w;
    }
    
    public String toString()
    {
        return name;
        
    }

    public DefaultMutableTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(DefaultMutableTreeNode parentNode) {
        this.parentNode = parentNode;
    }
    
}
