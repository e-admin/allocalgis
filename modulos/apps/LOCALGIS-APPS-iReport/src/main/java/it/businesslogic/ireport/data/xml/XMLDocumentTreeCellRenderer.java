/**
 * XMLDocumentTreeCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * XMLDocumentTreeCellRenderer.java
 * 
 * Created on May 15, 2007, 12:41:05 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.data.xml;


import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.Node;

/**
 *
 * @author gtoffoli
 */
public class XMLDocumentTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon tagIcon;
    static ImageIcon attributeIcon;
    static ImageIcon errorIcon;
    
    
    XMLFieldMappingEditor mappingEditor = null;

    public XMLFieldMappingEditor getMappingEditor() {
        return mappingEditor;
    }

    public void setMappingEditor(XMLFieldMappingEditor mappingEditor) {
        this.mappingEditor = mappingEditor;
    }
    
    public XMLDocumentTreeCellRenderer(XMLFieldMappingEditor mappingEditor) {
        super();
        
        this.mappingEditor = mappingEditor;
        if (tagIcon == null) tagIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/xml/tag.png"));
        if (attributeIcon == null) attributeIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/xml/attribute.png"));
        if (errorIcon == null) errorIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/problems/error.png"));
        

    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        
            
        
        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        
          try {
            if (value != null && value instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)value;
                if (dmtn.getUserObject() != null && dmtn.getUserObject() instanceof Node)
                {
                    Node node = (Node)dmtn.getUserObject();
                    String s = node.getNodeName();
                    if (node.getNodeValue() != null){
                        s += " (" + node.getNodeValue() + ")";
                    }
                    
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        setIcon(tagIcon);
                    }
                    if (node.getNodeType() == Node.ATTRIBUTE_NODE)
                    {
                        setIcon(attributeIcon);
                    }
                    
                    boolean needBold = false;
                    if (getMappingEditor() != null &&
                        getMappingEditor().getRecordNodes().contains(node))
                    {
                        needBold = true;
                    }  
                    
                    java.awt.Font f = getFont();
                    
                    if (f.isBold() && !needBold)
                    {
                        setFont( f.deriveFont( Font.PLAIN) );
                    }
                    else if (!f.isBold() && needBold)
                    {
                        setFont( f.deriveFont( Font.BOLD ) );
                    }
                     
                    setText(s);
                } else
                {
                    setIcon(errorIcon);
                }
            }
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
        }
         
        return this;
    }


}


