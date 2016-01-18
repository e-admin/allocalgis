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
 * FieldNode.java
 * 
 * Created on 22 giugno 2004, 15.09
 *
 */

package it.businesslogic.ireport.connection;

/**
 *
 * @author  Administrator
 */
public class FieldNode {
    
   /** Attributi del nodo */
    private java.util.Properties attributes;
    /** Nodi figli di questo nodo */
    private java.util.Vector children;
    
    /** Nome del nodo di configurazione */
    private String name = "";
    
    /** Valore del nodo */
    private String value = null;
    
    private int childIndex = 0;
    
    private boolean consumed = false;
        
    /**
     * Creates a new instance of FieldNode
     * @param name Nome del nodo
     */
    public FieldNode(String name) {
        attributes = new java.util.Properties();
        children = new java.util.Vector();
        this.name = name;
    }
    
    /**
     * Attributi del nodo.
     * @return Value of property attributes.
     */
    public java.util.Properties getAttributes() {
        return attributes;
    }    
    
    /**
     * Setter for property attributes.
     * @param attributes New value of property attributes.
     */
    public void setAttributes(java.util.Properties attributes) {
        this.attributes = attributes;
    }    
       
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     * Restituisce il valore di questo nodo come stringa
     * @return Value of property value.
     */
    public java.lang.String getValue() {
        return value;
    }
    
        
    /**
     * Restituisce il valore di questo nodo come stringa
     * @return Value of property value.
     */
    public java.lang.String getValue(String def) {
        if (value == null || value.length() == 0) return def;
        return value;
    }
    
    /**
     * Restituisce il valore di questo nodo come booleano
     * @return Value of property value.
     */
    public boolean getValueAsBoolean() {
        if (value !=null && value.trim().equalsIgnoreCase("true")) return true;
        return false;
    }
    
    
    /**
     * Restituisce il valore di questo nodo come intero
     * @return Value of property value.
     */
    public int getValueAsInteger() {
        try {
            return Integer.parseInt( value + "" );            
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }
    
    /**
     * Ritorna il child specificato.
     * @param childName Nome del nodo figlio
     * @param noNull Se true, verra' restituito un FieldNode invece di null. Il nuovo FieldNode verra' inserito nella lista dei childs.
     * @return Ritorna il nodo richiesto
     */
    public FieldNode getChild(java.lang.String childName, boolean noNull) {
        
        java.util.Enumeration enum_children = children.elements();
        while (enum_children.hasMoreElements())
        {
            FieldNode cn = (FieldNode)enum_children.nextElement();
            if (cn.getName().equals(childName) ) return cn;
        }
        
        if (noNull)
        {
            FieldNode cn =  new FieldNode(childName);
            this.children.add(cn);
            return cn;
        }
        return null;
    }
    
    
    /**
     * Ritorna un vettore di children di nome childName
     * @param childName Nome del nodo figlio
     * @return Ritorna il nodo richiesto
     */
    public java.util.Vector getChilddren(java.lang.String childName) {
        
        java.util.Vector result = new java.util.Vector();
        java.util.Enumeration enum_children = children.elements();
        while (enum_children.hasMoreElements())
        {
            FieldNode cn = (FieldNode)enum_children.nextElement();
            if (cn.getName().equals(childName) ) 
            {
                result.add(cn);
            }
        }
        return result;
    }
    
    /**
     * Ritorna il child specificato. Se il child non esiste, verra' restituito un FieldNode nuovo invece di null.
     * @param childName Nome del nodo figlio
     * @return Ritorna il nodo richiesto (non ritorna mai null)
     */
    public FieldNode getChild(java.lang.String childName) {
       return getChild(childName, true);
    }
    
    
        /**
     * Ritorna il child specificato. Se il child non esiste, verra' restituito un FieldNode nuovo invece di null.
     * @param childName Nome del nodo figlio
     * @return Ritorna il nodo richiesto (non ritorna mai null)
     */
    public FieldNode getNextChild() {
       
       
       if (this.getChildren().size() == 0)
       {
            return new FieldNode("Unnamed");
       }
       if (childIndex < 0)
       {
           childIndex = 0;
       }
       return (FieldNode)this.getChildren().elementAt( childIndex );
    }
    
    
    /**
     * Restituisce il valore di questo attributo come stringa
     * @return Ritorna il valore come stringa. (Null se l'attributo richiesto non esiste)
     * @param attrName Nome dell'attributo da ritornare
     */
    public java.lang.String getAttribute(String attrName) {
        return attributes.getProperty(attrName);
    }
    
     /**
     * Restituisce il valore di questo attributo come stringa
     * @return Ritorna il valore come stringa. (il valore di default specificato se l'attributo richiesto non esiste)
     * @param attrName Nome dell'attributo da ritornare
     * @param def Valore di default dell'attributo
     */
    public java.lang.String getAttribute(String attrName, String def) {
        return attributes.getProperty(attrName,def);
    }
    
    /**
     * Restituisce il valore di questo attributo come booleano
     * @return Ritorna true se l'attributo e' uguale alla stringa "true" (case insensitive).
     * @param attrName Nome dell'attributo da ritornare
     */
    public boolean getAttributeAsBoolean(String attrName) {
        String val = attributes.getProperty(attrName);
        if (val !=null && val.trim().equalsIgnoreCase("true")) return true;
        return false;
    }
    
    
    /**
     * Restituisce il valore di questo attributo come intero
     * @return 0 se l'attributo non e' parsabile come intero, altrimenti il valore intero.
     * @param attrName Nome dell'attributo da ritornare
     */
    public int getAttributeAsInteger(String attrName) {
        try {
            String val = attributes.getProperty(attrName);
            return Integer.parseInt( val + "" );            
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * Getter for property children.
     * @return Value of property children.
     */
    public java.util.Vector getChildren() {
        return children;
    }
    
    /**
     * Setter for property children.
     * @param children New value of property children.
     */
    public void setChildren(java.util.Vector children) {
        this.children = children;
    }
    
    public boolean next(String xmlPath) {
        // Se sono una foglia ritorno false
        
        //System.out.println(">>>" + xmlPath + " " + childIndex + "/" +);
        if (xmlPath.startsWith("/"))
        {
         xmlPath = xmlPath.substring(1);
        }
        
        if (xmlPath.indexOf("/") < 0)
        {
            // I am a leaf!!!
            if (!consumed) { return consumed = true; }
            else return false;
        }
        
        //xmlPath = xmlPath.substring(1);
        xmlPath = xmlPath.substring( xmlPath.indexOf("/"));

        // The first time return ever true.
        if (childIndex == 0 && getChildren().size() == 0)
        {
            if (!consumed) { return consumed = true; }
            else return false;
        }
               
        // See if our current child has other paths to explore...
        if (!getNextChild().next(xmlPath))
        {
            childIndex++;
            // if they are other childs, return true
            if (childIndex < getChildren().size()) return getNextChild().next(xmlPath);
        }
        else
        {
            return true;            
        }
        //for (; childIndex<getChildren().size(); ++childIndex)
            
        return false;
    }
        
    public String getChildsPath(String xmlPath)
    {
        if (xmlPath.indexOf("/") == xmlPath.lastIndexOf("/"))
        {
            // I am a leaf!!!
            return "";
        }
        
        xmlPath = xmlPath.substring(xmlPath.indexOf("/")+1);
        return this.getNextChild().getName() +"(" + childIndex +")::" + this.getNextChild().getChildsPath(xmlPath);
        
    }
} 
