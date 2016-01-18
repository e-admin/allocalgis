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


