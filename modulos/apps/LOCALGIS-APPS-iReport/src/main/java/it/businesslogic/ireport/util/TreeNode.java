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
 * TreeNode.java
 * 
 * Created on 28 novembre 2005, 12.12
 *
 */

package it.businesslogic.ireport.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author Administrator
 */
public class TreeNode {
    
    private List childs = new ArrayList();
    private Object  userObject = null;
    private TreeNode parent = null;
    
    /** Creates a new instance of TreeNode */
    public TreeNode(Object aUserObject) {
        setUserObject( aUserObject );
    }
    
    public String getName()
    {
        return "" + getUserObject();
    }

    public List getChilds() {
        return childs;
    }

    public boolean isLeaf() {
        return getChilds().isEmpty();
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
    
    public void addChild(TreeNode node)
    {
        node.setParent( this );
        getChilds().add(node);
    }
    
    public void addChilds(Collection nodes)
    {
        Iterator myIterator = nodes.iterator();
        while (myIterator.hasNext())
        {
            addChild( (TreeNode)(myIterator.next()));
        }
    }
    
   
    public boolean moveUp( Object  userObject)
    {
        if ( ((TreeNode)(getChilds().get(0))).getUserObject() == userObject ) return true;
        else 
        {
            for (int i=1; i<getChilds().size(); ++i)
            {
                TreeNode node = (TreeNode)(getChilds().get(i));
                if (node.getUserObject() == userObject)
                {
                    int oldPosition = i;
                    getChilds().remove( i );
                    getChilds().add( i-1, node );
                    return true;
                }
            }            
        }
        
        // Search for the node in the 
        for (int i=0; i< getChilds().size(); ++i)
        {
            TreeNode parent =  (TreeNode)getChilds().get(i);
            if (parent.isLeaf()) continue;
            else
            {
                if (parent.moveUp( userObject )) return true;
            }
        }
       
        return false;
    }
    
    public boolean moveDown(Object userObject)
    {
        if ( ((TreeNode)(getChilds().get(getChilds().size()-1))).getUserObject() == userObject ) return true;
        else 
        {
            for (int i=0; i<getChilds().size()-1; ++i)
            {
                TreeNode node = (TreeNode)(getChilds().get(i));
                if (node.getUserObject() == userObject)
                {
                    int oldPosition = i;
                    getChilds().remove( i );
                    getChilds().add( i+1, node );
                    return true;
                }
            }            
        }
        
        // Search for the node in the 
        for (int i=0; i< getChilds().size(); ++i)
        {
            TreeNode parent =  (TreeNode)getChilds().get(i);
            if (parent.isLeaf()) continue;
            else
            {
                if (parent.moveDown( userObject )) return true;
            }
        }
       
        return false;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    
}
