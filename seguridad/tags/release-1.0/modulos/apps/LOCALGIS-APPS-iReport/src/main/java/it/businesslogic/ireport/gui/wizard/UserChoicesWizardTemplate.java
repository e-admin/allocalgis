/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
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
 *
 *
 * UserChoicesWizardTemplate.java
 *
 * Created on December 19, 2006, 9:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.wizard;

import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.ReportWriter;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.Misc;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import javax.xml.transform.TransformerException;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author gtoffoli
 */
public class UserChoicesWizardTemplate {
    
    private String name = "";
    private String templateFile = "";
    private java.util.List groupExpressions = new java.util.ArrayList();
    private java.util.List displayFields = new java.util.ArrayList();
    private String query = "";
    private String query_language = "";
    private String iRDatasourceName = "";
    private boolean saveFieldDescriptions = true;
    
    
    /**
     * Creates a new instance of UserChoicesWizardTemplate
     */
    public UserChoicesWizardTemplate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.List getGroupExpressions() {
        return groupExpressions;
    }

    public void setGroupExpressions(java.util.List groupExpressions) {
        this.groupExpressions = groupExpressions;
    }

    public java.util.List getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(java.util.List displayFields) {
        this.displayFields = displayFields;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIRDatasourceName() {
        return iRDatasourceName;
    }

    public void setIRDatasourceName(String iRDatasourceName) {
        this.iRDatasourceName = iRDatasourceName;
    }
    
    public String toString()
    {
        if (getName() != null) return getName();
        return "Unnamed template";
        
    }
    
    
    /**
     *  Load the user choices templates from  IREPORT_USER_HOME_DIR/wizardUserTemplates.xml
     *  A void list is returned if the file does not exists.
     */
    public static java.util.List loadWizardTemplates()
    {
         java.util.List  templates = new java.util.ArrayList();
         
         String locationFileName = MainFrame.getMainInstance().IREPORT_USER_HOME_DIR + java.io.File.separator + "wizardUserTemplates.xml";
         
         
         java.io.File source = new java.io.File( locationFileName  );
         if (!source.exists())
         {
             System.out.println("locationFileName does not exisit");
             return templates;
         }
         
         
            DOMParser parser = new DOMParser();
                //  Parse the Document
                //  and traverse the DOM
                try {
                    
                    parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                        /* Code by Teodor Danciu */
                        public org.xml.sax.InputSource resolveEntity(
                                String publicId,
                                String systemId
                                ) throws SAXException//, java.io.IOException
                        {
                            org.xml.sax.InputSource inputSource = null;
                            
                            if (systemId != null) {
                                String dtd = null;
                                
                                if ( systemId.equals("http://ireport.sourceforge.net/dtds/userWizardChoicesTemplate.dtd") ) {
                                    dtd = "it/businesslogic/ireport/dtds/userWizardChoicesTemplate.dtd";
                                } else {
                                    return new org.xml.sax.InputSource(systemId);
                                }
                                
                                
                                ClassLoader classLoader = this.getClass().getClassLoader();
                                
                                java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                                if (is != null) {
                                    inputSource = new org.xml.sax.InputSource(is);
                                }
                                
                            }
                            
                            return inputSource;
                        }
                    });
                    /* End Code by Teodor Danciu */
                    InputStream input_source = null;
                    if ( source instanceof java.io.File ) {
                        input_source = new FileInputStream((java.io.File)source);
                        
                    }
                    //else if ( source instanceof java.net.URL){
                    //    
                    //    input_source = ((java.net.URL)source).openStream();
                    //    
                    //}
                    
                    parser.parse(new org.xml.sax.InputSource( input_source ));
                    Document document = parser.getDocument();
                    
                    //System.out.println("traverse");
                    Node node = document.getDocumentElement();
                    
                    NodeList list = XPathAPI.selectNodeList(node, "/userWizardChoicesTemplateSet/userWizardChoicesTemplate");
                    Node childnode = null;
                    
                    for (int n=0; n < list.getLength(); n++) {
                        UserChoicesWizardTemplate template = new UserChoicesWizardTemplate();
                        childnode = XPathAPI.selectSingleNode(list.item(n), "@name");
                        if (childnode != null) template.setName(childnode.getNodeValue());
                        
                        childnode = XPathAPI.selectSingleNode(list.item(n), "@saveFieldDescriptions");
                        if (childnode != null) template.setSaveFieldDescriptions(childnode.getNodeValue().equals("true"));
                        
                        childnode = XPathAPI.selectSingleNode(list.item(n), "templateFile/text()");
                        if (childnode != null) template.setTemplateFile( childnode.getNodeValue());
                        
                        NodeList listExpressions = null;
                        listExpressions = XPathAPI.selectNodeList(list.item(n), "groupExpression");
                        
                        for (int n2=0; n2 < listExpressions.getLength(); n2++) {
                            
                            childnode = XPathAPI.selectSingleNode(listExpressions.item(n2), "text()");
                            if (childnode != null) template.getGroupExpressions().add( childnode.getNodeValue());
                            
                        }
                        // Read fields...
                        NodeList listFields = XPathAPI.selectNodeList(list.item(n), "displayField");
                        for (int n2=0; n2 < listFields.getLength(); n2++) {
                            
                            String fname = "";
                            String fclass = "";
                            String fdesc = "";
                            
                            childnode = XPathAPI.selectSingleNode(listFields.item(n2), "@name");
                            if (childnode != null) fname = childnode.getNodeValue();
                            
                            childnode = XPathAPI.selectSingleNode(listFields.item(n2), "@class");
                            if (childnode != null) fclass = childnode.getNodeValue();
                            
                            childnode = XPathAPI.selectSingleNode(listFields.item(n2), "fieldDescription/text()");
                            if (childnode != null) fdesc = childnode.getNodeValue();
                            
                            JRField jrField = new JRField(fname, fclass);
                            jrField.setDescription(fdesc);
                            
                            template.getDisplayFields().add(jrField);
                            
                        }
                        
                        childnode = XPathAPI.selectSingleNode(list.item(n), "query/text()");
                        if (childnode != null) template.setQuery( childnode.getNodeValue());
                        
                        childnode = XPathAPI.selectSingleNode(list.item(n), "query/@language");
                        if (childnode != null) template.setQuery_language( childnode.getNodeValue());
                        
                        childnode = XPathAPI.selectSingleNode(list.item(n), "iRDatasourceName/text()");
                        if (childnode != null) template.setIRDatasourceName( childnode.getNodeValue());
                        
                        templates.add(template);
                    }
                } catch (SAXException e) {
                    System.err.println(e);
                } catch (java.io.IOException e) {
                    System.err.println(e);
                } catch (Exception e) {
                    System.err.println(e);
                }
            
            return templates;
    }
    
    /**
     *  Load the user choices templates from  IREPORT_USER_HOME_DIR/wizardUserTemplates.xml
     *  A void list is returned if the file does not exists.
     */
    public static void storeWizardTemplates(java.util.List list)
    {
        String locationFileName = MainFrame.getMainInstance().IREPORT_USER_HOME_DIR + java.io.File.separator + "wizardUserTemplates.xml";
        java.io.File destination = new java.io.File( locationFileName  );
         
        StringBuffer output = new StringBuffer();
        
        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        output.append("<userWizardChoicesTemplateSet>\n");
        
        for (int i=0; i<list.size(); ++i)
        {
            UserChoicesWizardTemplate template = (UserChoicesWizardTemplate)list.get(i);
            output.append("\t<userWizardChoicesTemplate name=\"" + Misc.xmlEscape( template.getName() )  + "\" saveFieldDescriptions=\"" + template.isSaveFieldDescriptions()  + "\">\n");
            output.append("\t\t<templateFile>" + ReportWriter.getCDATAString( template.getTemplateFile(), 2) + "</templateFile>\n");

            for (int k=0; k<template.getGroupExpressions().size(); ++k)
                output.append("\t\t<groupExpression>" + ReportWriter.getCDATAString( template.getGroupExpressions().get(k)+"", 2) + "</groupExpression>\n");
    
            for (int k=0; k<template.getDisplayFields().size(); ++k)
            {
                JRField field =  (JRField)template.getDisplayFields().get(k);
                output.append("\t\t<displayField name=\"" + Misc.xmlEscape( field.getName() )  + "\" class=\"" + Misc.xmlEscape( field.getClassType() )  + "\">\n");
                output.append("\t\t<fieldDescription>" + ReportWriter.getCDATAString( field.getDescription(), 2) + "</fieldDescription>\n");
                output.append("\t\t</displayField>\n");
            }
            
            if (template.getQuery() != null && template.getQuery().length() > 0)
            output.append("\t\t<query language=\"" +  Misc.xmlEscape(template.getQuery_language() ) + "\">" + ReportWriter.getCDATAString( template.getQuery(), 2) + "</query>\n");
            
            if (template.getIRDatasourceName() != null && template.getIRDatasourceName().length() > 0)
            output.append("\t\t<iRDatasourceName>" + ReportWriter.getCDATAString( template.getIRDatasourceName(), 2) + "</iRDatasourceName>\n");
                       
            output.append("\t</userWizardChoicesTemplate>\n");
        }
        
        output.append("</userWizardChoicesTemplateSet>\n");
        
        FileWriter fos = null;
        try {
            fos = new FileWriter(destination);
            fos.write( output.toString() );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally {
            if (fos != null)
            {
                try { fos.close(); } catch (Exception ex2) {}
            }
        }
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getQuery_language() {
        return query_language;
    }

    public void setQuery_language(String query_language) {
        this.query_language = query_language;
    }

    public boolean isSaveFieldDescriptions() {
        return saveFieldDescriptions;
    }

    public void setSaveFieldDescriptions(boolean saveFieldDescriptions) {
        this.saveFieldDescriptions = saveFieldDescriptions;
    }
}
