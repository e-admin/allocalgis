/**
 * JasperTemplate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JasperTemplate.java
 * 
 * Created on Aug 21, 2007, 3:55:35 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*
 * Copyright (C) 2006 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */

package it.businesslogic.ireport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 *  A JasperTemplate is a container of a set of predefined styles normally contained in external files.
 * @author gtoffoli
 */
public class JasperTemplate {

    /**
     *  If the JasperTemplate is stored in a file, save the complete path here...
     */
    private String filename = null;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    private JasperTemplate parent = null;

    public JasperTemplate getParent() {
        return parent;
    }

    public void setParent(JasperTemplate parent) {
        this.parent = parent;
    }
    
    public JasperTemplate()
    {
        // Try to load this expression....
        reloadTemplate();
    }
    
    public JasperTemplate(String expression)
    {
        this.setExpression(expression);
        // Try to load this expression....
    }
    
    /**
     * This method tries to resolve the current expression as file...
     * If the file is correctly loaded, filename is set to the file path,
     * otherwise it is set to null.
     */
    public void reloadTemplate()
    {
        if (filename == null) return;

        try {
        /*    
            File f = new File(getExpression());
            if (!f.exists())
            {
                // try to resolve all as resource...
                URL url = MainFrame.getMainInstance().getReportClassLoader().getResource(getExpression());
                if (url != null)
                {
                    filename = url.getFile();
                }
            }
            else
            {
                filename = f.toString();
            }
        */    
            readTemplateFile(filename);
            
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        
    }
    
    private void readTemplateFile(String xmlFile) throws Exception
    {
        this.getStyles().clear();
        this.getTemplates().clear();
        
        File ffile = new File ( xmlFile ) ;
        java.io.InputStream input_source = null ;
        
        if ( ffile.exists () )
        {
            input_source = new java.io.FileInputStream ( ffile ) ;
            try {
                DOMParser parser = new DOMParser();

                parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                    
                    /* Code by Teodor Danciu */
                    public org.xml.sax.InputSource resolveEntity( String publicId, String systemId )
                           throws SAXException, IOException
                    {
                        org.xml.sax.InputSource inputSource = null;

                        //System.out.println("Resolving : " + publicId + " " + systemId);

                        if (systemId != null) {
                            String dtd = null;

                            if ( systemId.equals("http://jasperreports.sourceforge.net/dtds/jaspertemplate.dtd") ||
                            systemId.equals("http://www.jasperreports.com/dtds/jaspertemplate.dtd") ) {
                                //dtd = "dori/jasper/engine/dtds/jasperreport.dtd";
                                dtd = "net/sf/jasperreports/engine/dtds/jaspertemplate.dtd";

                            }

                            ClassLoader classLoader = this.getClass().getClassLoader();
                            java.net.URL url = null;

                            if (classLoader != null) {
                                url = classLoader.getResource(dtd);
                                if (url == null) {
                                    classLoader = this.getClass().getClassLoader();
                                }

                            } else {
                                // url is certainly null
                                // classLoader stays null
                            }


                            java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                            if (is != null) {
                                java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                                inputSource = new org.xml.sax.InputSource(isr);
                            } else {
                                // dtd could not be found
                                // this error occurs e.g. when the package name / path changes to this dtd
                                // the error will be caught by MainFrame.openFile() en the report file won't open
                                throw new java.io.IOException( "iReport Internal error in JasperTemplate.java: Could not find: " + dtd + "\n" );
                            }
                        }
                        return inputSource;

                    }
                });
                
                org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource( input_source );
                input_sss.setSystemId("file:///" + xmlFile);

                 parser.parse( input_sss );
                    //System.out.println(parser.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes"));

                 Document document = parser.getDocument();
                 Node node = document.getDocumentElement();
                 
                 if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("jasperTemplate")) {
                    
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        for (int i=0; i< children.getLength(); i++)
                        {
                            Node child = (Node)children.item(i);
                        
                            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("style")) {
                                getStyles().add( readStyle(child) );
                            }
                            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("template")) {
                                
                                String newTemplate = Report.readPCDATA( child );
                                
                                JasperTemplate jt = new JasperTemplate(newTemplate);
                                getTemplates().add( jt );
                                jt.setParent(this);
                            }
                        }
                    }
                 }
            
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    private TemplateStyle readStyle(Node styleNode)
    {
        TemplateStyle style = new TemplateStyle();
        style.setJasperTemplate(this);
        NamedNodeMap nnm = styleNode.getAttributes();

        for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
        {
             if ( nnm.getNamedItem(Style.JRXMLStyleAttributes[i]) != null) {
                style.getAttributes().put(Style.JRXMLStyleAttributes[i], nnm.getNamedItem(Style.JRXMLStyleAttributes[i]).getNodeValue());
             }
        }
        return style;
    }
    
    public boolean canWrite()
    {
        try {
            if (this.filename == null) return false;
            File f = new File(filename);
            return f.canWrite();
        
        } catch (Exception ex)
        {
            return false;
        }
    }
    
    public  void saveTemplateFile() throws Exception
    {
        if (this.filename == null) throw new Exception("Unable to save this template!");
        saveTemplateFile(this.filename);
    }
    
    private void saveTemplateFile(String xmlFile) throws Exception
    {
        PrintWriter pw = new java.io.PrintWriter( xmlFile );
        this.filename = xmlFile;
        
        try {
        pw.println("<?xml version=\"1.0\"?>");
        pw.println("<!DOCTYPE jasperTemplate PUBLIC \"-//JasperReports//DTD Template//EN\" \"http://jasperreports.sourceforge.net/dtds/jaspertemplate.dtd\">");
        pw.println("<!-- Created by iReport -->");
        pw.println("<jasperTemplate>");
        for (JasperTemplate jt : getTemplates())
        {
            pw.println("\t<template><![CDATA[" + jt.getExpression() + "]]></template>");
        }
        
        for (TemplateStyle style : getStyles())
        {
            String tabs = "\t";

                pw.println(tabs + "<style ");
                for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
                {
                    if ( style.getAttributes().containsKey(Style.JRXMLStyleAttributes[i]) &&
                         style.getAttributes().get( Style.JRXMLStyleAttributes[i] ) != null) {
                         Object obj = style.getAttributes().get(Style.JRXMLStyleAttributes[i]);
                         String value = ""+obj;
                         if (obj instanceof java.awt.Color)
                         {
                             value = Report.writeEncodedColor( (java.awt.Color)obj);
                         }
                         if (Style.JRXMLStyleAttributes[i].toLowerCase().endsWith("color") && value.startsWith("["))
                         {
                             // The color is in the form [r,g,b]...
                             try {
                               value =   Report.writeEncodedColor(it.businesslogic.ireport.gui.sheet.ColorSelectorPanel.parseColorString(value));
                             } catch (Exception ex)
                             {
                                 value="black";
                             }
                         }
                         if (Style.JRXMLStyleAttributes[i].equals("style"))
                         {
                             if (value == null || value.trim().length() == 0) continue;
                         }
                         pw.println(tabs + "\t" + Style.JRXMLStyleAttributes[i] + "=\"" + value +"\"");
                    }
                }
                pw.println(tabs + "/>");
        }
        
        
        pw.println("</jasperTemplate>");
        
        } finally {
            pw.close();
        }
        
    }
            
    /**
     * List of the styles contained in this template
     */
    private ArrayList<TemplateStyle> styles = new ArrayList<TemplateStyle>();
    
    /**
     * List of sub-templates
     */
    private ArrayList<JasperTemplate> templates = new ArrayList<JasperTemplate>();
    
    private String expression = "";

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public ArrayList<JasperTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<JasperTemplate> templates) {
        this.templates = templates;
    }

    
    public ArrayList<TemplateStyle> getStyles() {
        return styles;
    }

    public void setStyles(ArrayList<TemplateStyle> styles) {
        this.styles = styles;
    }
    
    public String toString()
    {
        if (getFilename() != null && getFilename().length() >0)
        {
            File f = new File(getFilename());
            return f.getName();
        }
        
        return getExpression();
    }
}
