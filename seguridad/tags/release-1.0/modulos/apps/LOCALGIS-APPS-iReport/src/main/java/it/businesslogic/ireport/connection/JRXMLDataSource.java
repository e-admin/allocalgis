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
 * JRXMLDataSource.java
 * 
 * Created on 22 giugno 2004, 14.55
 *
 */

package it.businesslogic.ireport.connection;


import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * The XML datasource define a row as a path.
 * A path can be truncated.
 * @author  Administrator
 */
public class JRXMLDataSource implements net.sf.jasperreports.engine.JRDataSource {

    private FieldNode rootFieldNode = null;
    private String rowPath = "/";
    private FieldNode actualPath = null;
    
    public JRXMLDataSource(String uri, String rowPath)
    {
        this.rowPath = rowPath;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            org.w3c.dom.Document doc = db.parse( uri );
            build(doc);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    } 
    
    public JRXMLDataSource(java.io.File file, String rowPath)
    {
        this.rowPath = rowPath;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            org.w3c.dom.Document doc = db.parse( file );
            build(doc);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    } 
        
    public JRXMLDataSource(java.io.InputStream is, String rowPath)
    {
        this.rowPath = rowPath;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            org.w3c.dom.Document doc = db.parse( is );
            build(doc);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }  

    public JRXMLDataSource(FieldNode rootFieldNode, String rowPath)
    {
        this.rowPath = rowPath;
        this.rootFieldNode = rootFieldNode;
    }  
        
    private void build(org.w3c.dom.Document doc) throws Exception
    {
            Element element = doc.getDocumentElement();
            rootFieldNode = createNode(element, null); 
    }  
    
    private FieldNode createNode(Node element, FieldNode parent) throws Exception
    {
        
        FieldNode cn = new FieldNode( element.getNodeName() );
        
        // Aggiungiamo gli attributi...
        NamedNodeMap attributes = element.getAttributes();
        for (int i=0; i< attributes.getLength(); ++i)
        {
            Node node = attributes.item(i);
            cn.getAttributes().setProperty( node.getNodeName(), node.getNodeValue() );
        }
        NodeList nl = element.getChildNodes();
        for (int i=0; i< nl.getLength(); ++i)
        {
                 Node node = nl.item(i);
                 if (node.getNodeType() == Node.ELEMENT_NODE)
                 {
                    createNode( node, cn);                
                 }
                 else if (node.getNodeType() == Node.CDATA_SECTION_NODE ||
                         node.getNodeType() == Node.TEXT_NODE)
                 {
                     cn.setValue( node.getNodeValue() );
                 }
        }
        
        if (parent != null)
        {
            parent.getChildren().add( cn);        
        }
        else
        {
            parent = cn;
        }
        
        return parent;
    }
    
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jRField) throws net.sf.jasperreports.engine.JRException {
        
        String path = jRField.getDescription();
        
        Object val = getPathValue(rootFieldNode, path);
        //if (jRField.getClass() == String.class)
        {
            return val;            
        }
        //return null;
    }
    
    public boolean next() throws net.sf.jasperreports.engine.JRException {       
        // Create the next path.
        return rootFieldNode.next( this.rowPath );
    }
    
   
    public String getActualPath(){       
        
        String childPath = this.rowPath;
        childPath = rowPath.substring( rootFieldNode.getName().length() + 1);
        return rootFieldNode.getName() + "::" + rootFieldNode.getChildsPath( rowPath );
    }
    
    public static void main(String[] argv)
    {
        JRXMLDataSource ds = new JRXMLDataSource("C:\\test_ireport.xml","/addressbook/address/ciccio");
        try {
            System.out.println("starting");
        while (ds.next())
        {
            System.out.println(ds.getActualPath());
        }
            System.out.println("finishing");
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private Object getPathValue(FieldNode startingNode, String path)
    {
        String tag = "";
        if (path == null) return startingNode.getValue();
        
        if (path.startsWith("/")) path = path.substring(1);
        
        String sub_path = "";
        if (path.indexOf("+") >= 0 )
        {
            sub_path = path.substring(path.indexOf("+")+1);
            path = path.substring(0,path.indexOf("+")+1);
        }
        
        if (path.indexOf("/") <0)
        {
            if (path.indexOf("@") >=0)
            {
                tag = path.substring(path.indexOf("@") + 1);
                return startingNode.getAttribute(tag);
                //path = path.substring(0, path.indexOf("@"));
            }
            else if (path.indexOf("*") >=0)
            {
                String childName = path.substring(path.indexOf("*") + 1);
                // Create a datasource based on this type of chils...
                FieldNode fn = new FieldNode( startingNode.getName() );
                fn.setAttributes( startingNode.getAttributes() );
                fn.setChildren( startingNode.getChilddren( childName ));
                return new JRXMLDataSource(fn, "/" + startingNode.getName() + "/" + childName );
                //path = path.substring(0, path.indexOf("@"));
            }
            else if (path.indexOf("+") >=0)
            {
                String childToTake = sub_path;       
            childToTake = getNextNodeName( sub_path);
                return getSubPathValue(startingNode.getChild(childToTake) , sub_path);
            }
            else
            {
                return startingNode.getValue();
            }
        }
        
        path = path.substring(path.indexOf("/") + 1);
        path += sub_path;
        /*
        if (path.indexOf("/") > 0)
        {
            // go to the next path child...
            String childToTake = path.substring(0,path.indexOf("/"));
            System.out.println("taking child " + childToTake);
            return getPathValue(startingNode.getChild(childToTake), path );
        }
        */
        
        return getPathValue(startingNode.getNextChild(), path );
        
    }
    

    private Object getSubPathValue(FieldNode startingNode, String path)
    {
        String tag = "";
        if (path == null) return startingNode.getValue()+"[" + path + "]";
        
        
        //System.out.println("Resolving path " + path + "  now in " + startingNode.getName());
        //System.out.flush();
        
        if (path.startsWith("/")) path = path.substring(1);
        
        if (path.indexOf("/") <0)
        {
            if (path.indexOf("@") >=0)
            {
                tag = path.substring(path.indexOf("@") + 1);
                return startingNode.getAttribute(tag); //+ "(tag of " + startingNode.getName() + ")";
                //path = path.substring(0, path.indexOf("@"));
            }
            else if (path.indexOf("*") >=0)
            {
                String childName = path.substring(path.indexOf("*") + 1);
                // Create a datasource based on this type of chils...
                FieldNode fn = new FieldNode( startingNode.getName() );
                fn.setAttributes( startingNode.getAttributes() );
                fn.setChildren( startingNode.getChilddren( childName ));
                return new JRXMLDataSource(fn, "/" + startingNode.getName() + "/" + childName );
                //path = path.substring(0, path.indexOf("@"));
            }
            else
            {
                return startingNode.getValue(); //+" " + startingNode.getAttributes().size() +" ("+startingNode.getName() + ")";
            }
        }
        else
        {
            path = path.substring(path.indexOf("/") + 1);
            
            String childToTake = path;       
            childToTake = getNextNodeName( path);
            return getSubPathValue(startingNode.getChild(childToTake), path); //"(child of "+startingNode.getName() + " " + startingNode.getAttributes().size() + ")";
            
        }
        
        
        //return getSubPathValue(startingNode.getNextChild(), path );
        
    }
    
    private static String getNextNodeName( String path)
    {
        
        if (path == null || path.length() ==0) return "";
        if (path.startsWith("/")) path = path.substring(1);
        
        String childToTake = path;
        if (path.indexOf("/") >= 0)
        {
                childToTake = path.substring(0,path.indexOf("/"));
        }
        
        if (childToTake.indexOf("@") >= 0)
        {
            childToTake = childToTake.substring(0,path.indexOf("@"));
        }
        
        if (childToTake.indexOf("*") >= 0)
        {
            childToTake = childToTake.substring(0,path.indexOf("*"));
        }
        
        return childToTake;
    }
}
