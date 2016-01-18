/*
 * Report.java
 *
 *  iReport  --  Visual designer for generating JasperReports Documents
 *  Copyright (C) 2002  Giulio Toffoli gt@businesslogic.it
 *
*  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.Styl"
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  Giulio Toffoli
 *  Via T.Aspetti, 233
 *  35100 Padova ITALY
 *  gt@businesslogic.it
 *
 * Created on 10 febbraio 2003, 19.32
 *
 * Updated by Robert Lamping:
 * dtd updated with the new package path net/sf/  etc...
 * Added Try catch to catch a missing dtd.
 * Changed Mainframe.openFile to catch this exception and stop opening a file in case of an exception.
 */

package it.businesslogic.ireport;

//import java.util.*;
import it.businesslogic.ireport.ReportWriter;
import it.businesslogic.ireport.chart.SectionItemHyperlink;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent;
import it.businesslogic.ireport.util.I18n;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Iterator;
import java.util.Properties;
import java.util.Enumeration;

//import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.util.Misc;
import it.businesslogic.ireport.util.PageSize;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.chart.*;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabColumnGroup;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabRowGroup;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.event.StyleChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


import org.apache.xerces.parsers.DOMParser;

//import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import com.geopista.app.reports.maps.MapImageExpressionManager;
import com.geopista.reports.gui.MapImageReportElement;
import com.geopista.reports.parameters.IReportParameterManager;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author  Administrator
 */
public class ReportReader {

    private Report report = null ;

    public ReportReader ( Report report )
    {
        this.report = report ;
    }

    public Report getReport ()
    {
        return report ;
    }

    public void setReport ( Report report )
    {
        this.report = report ;
    }

    public Report readFile ( String xmlFile )
            throws IOException
    {

        // Set the real default...
        getReport ().setLanguage ( "java" ) ;


        //reset bands, prevents page height overflow for unused bands
        for ( int i = 0 ; i < getReport ().getBands ().size () ; i++ )
        {
            ( ( Band ) getReport ().getBands ().elementAt ( i ) ).setHeight ( 0 ) ;
        }

        java.io.InputStream input_source = null ;
        try
        {
            if ( xmlFile.toLowerCase ().endsWith ( ".jasper" ) )
            {
                try {
                java.io.ByteArrayOutputStream bos = new java.io.
                        ByteArrayOutputStream () ;
                // 1. We havo to load the design...
                JRReport jreport = (JRReport)JRLoader.loadObject(xmlFile);
                
                net.sf.jasperreports.engine.JasperCompileManager.
                        writeReportToXmlStream (
                                jreport , bos ) ;
                
                input_source = new ByteArrayInputStream ( bos.toByteArray () ) ;
                
                } catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
            else
            {

                //  Create a Xerces DOM Parser
                //  Parse the Document
                //  and traverse the DOM

                getReport ().setFilename ( xmlFile ) ;
                getReport ().checkReadOnlyFlag () ;

                // set load time...
                getReport ().setLoadTime ( Misc.getLastWriteTime ( xmlFile ) ) ;

                File ffile = new File ( xmlFile ) ;
                if ( ffile.exists () )
                {
                    input_source = new java.io.FileInputStream ( ffile ) ;

                }
                else
                {

                    input_source = getReport ().getClass ().getClassLoader ().
                                   getResourceAsStream ( xmlFile ) ;
                }

                // Change to the file directory...

                
            }
            
            return readFromStream(input_source); //Changed by Felix Firgau
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            throw e ;

       // } catch (net.sf.jasperreports.engine.JRException e) {
       //     System.out.println(e);
       //     e.printStackTrace();

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
        finally{
            try { 
                if (input_source != null)
                {
                    input_source.close();
                }
            } catch (Exception ex) {}
        }
        return getReport();
    }
    /**
     * readFromStream
     * reads an report from an InputStream
     *
     * Author: Felix Firgau
     *
     * @param input_source InputStream
     * @return Report
     * @throws IOException
     */
    public Report readFromStream(java.io.InputStream input_source)
            throws IOException
    {
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

                            if ( systemId.equals("http://jasperreports.sourceforge.net/dtds/jasperreport.dtd") ||
                            systemId.equals("http://www.jasperreports.com/dtds/jasperreport.dtd") ) {
                                //dtd = "dori/jasper/engine/dtds/jasperreport.dtd";
                                dtd = "net/sf/jasperreports/engine/dtds/jasperreport.dtd";

                            } else if (
                            systemId.equals("http://jasperreports.sourceforge.net/dtds/jasperprint.dtd") ||
                            systemId.equals("http://www.jasperreports.com/dtds/jasperprint.dtd") ) {
                                dtd = "net/sf/jasperreports/engine/dtds/jasperprint.dtd";
                            } else {

                                return new org.xml.sax.InputSource(systemId);
                            }


                            ClassLoader classLoader = getReport().getClass().getClassLoader();
                            java.net.URL url = null;

                            if (classLoader != null) {
                                url = classLoader.getResource(dtd);
                                if (url == null) {
                                    classLoader = getReport().getClass().getClassLoader();
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
                                throw new java.io.IOException( "iReport Internal error in report.java: Could not find: " + dtd + "\n" );
                            }
                        }
                        return inputSource;

                    }
                });
                /* End Code by Teodor Danciu */

                    //String f = new java.io.File(xmlFile).toURI().toString();
                    org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource( input_source );
                    input_sss.setSystemId("file:///" + getReport().getFilename());

                    parser.parse( input_sss );

                    //System.out.println(parser.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes"));

                    Document document = parser.getDocument();
                    getReport().setEncoding(Misc.nvl(input_sss.getEncoding(),"UTF-8"));

                    traverse(document.getDocumentElement());



            /* Begin Code by Robert Lamping
             * 2 July 2004
             * Now height and width are known and a format can be guessed.
             * using PageSize.deductPageFormat();
             */

            getReport().setReportFormat( PageSize.deductPageFormat( getReport().getWidth(), getReport().getHeight()) );


            for (int i = 0; i < getReport().getGroups().size(); i++) {
                it.businesslogic.ireport.Group grp = (it.businesslogic.ireport.Group)getReport().getGroups().elementAt(i);
                getReport().addGroup(grp,false);
            }

            /*
            for (int i=0; i<getBands().size(); ++i)
            {
                Band b = (Band)getBands().elementAt(i);
                getReport().elementGroupResort( b );
            }
             */

            // Translate coords to iReport coord system...
            for (int i =0; i < getReport().getElements().size(); ++i) {
                ReportElement re = (ReportElement)getReport().getElements().elementAt(i);


                if (re.getParentElement() != null)
                {
                    re.trasform(new java.awt.Point((int)re.getParentElement().getPosition().getX(), (int)re.getParentElement().getPosition().getY()), TransformationType.TRANSFORMATION_MOVE);
                }
                else
                {
                    re.trasform(new java.awt.Point(getReport().getLeftMargin()+10, getReport().getBandYLocation(re.getBand())+10), TransformationType.TRANSFORMATION_MOVE);
                }
            }




            // Scriptlet loading....

            // Process custom properties (ireport.* properties).
            // We cut out ireport properties...
            //System.out.println("Analizing properties...");
            //System.out.println("Possible file: "+getReport().getScriptletFileName());

            if (getReport().getScriptletFileName() != null)
            {
                getReport().setScriptletHandling( getReport().SCRIPTLET_CLASSIC_HANDLING);
            }

            for (int pk= 0; pk < getReport().getJRproperties().size(); ++pk) {
                JRProperty prop = (JRProperty)getReport().getJRproperties().elementAt( pk );

                //System.out.println(""+prop.getName());

                if (prop.getName().equals("ireport.scriptlethandling")) {
                    if (prop.getValue().equals("0")) {
                        getReport().setScriptletHandling(0);
                    } else if (prop.getValue().equals("1")) {
                        getReport().setScriptletHandling( getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING );
                        // Try to load the source file...
                        File scriptletFile = new File(getReport().getScriptletFileName());
                        if (scriptletFile.exists()) {
                            getReport().setScripletCode( new ScriptletCode(getReport().getScriptletFileName()) );
                            //System.out.println("Caricato scriptlet");
                        }
                    }
                }

                if (prop.getName().equals("ireport.encoding")) {
                    getReport().setEncoding(prop.getValue());
                }

                if (prop.getValue().startsWith("ireport.")) {
                    getReport().getJRproperties().remove( prop );
                    pk--;
                }
            }

            if (getReport().getScriptletHandling() == 2 &&
            	(getReport().getScriptletClass() == null || getReport().getScriptletClass().equals("")) )
            	{
            		getReport().setScriptletHandling(0);
            	}

            // } catch (SAXException e) {
            //            System.err.println(e);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            throw e ;

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        // Set the report status to Unchanged...
        // This will thrown an ReportStateChanged
        getReport().setReportChanges( 0 );

        return getReport();
    }


    //  Traverse DOM Tree.  Print out Element Names
    private void traverse(Node node) {

        boolean seeInside = false;
        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("jasperReport")) {

            seeInside = true;
            // Find encoding...
            NamedNodeMap nnm = node.getAttributes();

            getReport().setName((( nnm.getNamedItem("name") != null) ? nnm.getNamedItem("name").getNodeValue() : ""));

            if ( nnm.getNamedItem("columnCount") != null) {
                getReport().setColumnCount( Integer.parseInt(nnm.getNamedItem("columnCount").getNodeValue()));
            }
            if ( nnm.getNamedItem("printOrder") != null) {
                getReport().setPrintOrder( nnm.getNamedItem("printOrder").getNodeValue());
            }
            if ( nnm.getNamedItem("orientation") != null) {
                getReport().setOrientation( nnm.getNamedItem("orientation").getNodeValue());
            }
            if ( nnm.getNamedItem("scriptletClass") != null) {
                getReport().setScriptletClass( nnm.getNamedItem("scriptletClass").getNodeValue());
            }
            if ( nnm.getNamedItem("resourceBundle") != null) {
                getReport().setResourceBundleBaseName( nnm.getNamedItem("resourceBundle").getNodeValue());
            }
            if ( nnm.getNamedItem("pageWidth") != null) {
                getReport().setWidth( Integer.parseInt(nnm.getNamedItem("pageWidth").getNodeValue()));
            }
            if ( nnm.getNamedItem("pageHeight") != null) {
                getReport().setHeight( Integer.parseInt(nnm.getNamedItem("pageHeight").getNodeValue()));
            }
            if ( nnm.getNamedItem("columnWidth") != null) {
                getReport().setColumnWidth( Integer.parseInt(nnm.getNamedItem("columnWidth").getNodeValue()));
            }
            if ( nnm.getNamedItem("columnSpacing") != null) {
                getReport().setColumnSpacing( Integer.parseInt(nnm.getNamedItem("columnSpacing").getNodeValue()));
            }
            if ( nnm.getNamedItem("leftMargin") != null) {
                getReport().setLeftMargin( Integer.parseInt(nnm.getNamedItem("leftMargin").getNodeValue()));
            }
            if ( nnm.getNamedItem("rightMargin") != null) {
                getReport().setRightMargin( Integer.parseInt(nnm.getNamedItem("rightMargin").getNodeValue()));
            }
            if ( nnm.getNamedItem("topMargin") != null) {
                getReport().setTopMargin( Integer.parseInt(nnm.getNamedItem("topMargin").getNodeValue()));
            }
            if ( nnm.getNamedItem("bottomMargin") != null) {
                getReport().setBottomMargin( Integer.parseInt(nnm.getNamedItem("bottomMargin").getNodeValue()));
            }
            if ( nnm.getNamedItem("whenNoDataType") != null) {
                getReport().setWhenNoDataType( nnm.getNamedItem("whenNoDataType").getNodeValue());
            }
            if ( nnm.getNamedItem("isTitleNewPage") != null) {
                getReport().setIsTitleNewPage(nnm.getNamedItem("isTitleNewPage").getNodeValue().equalsIgnoreCase("true") );
            }
            if ( nnm.getNamedItem("isSummaryNewPage") != null) {
                getReport().setIsSummaryNewPage(  nnm.getNamedItem("isSummaryNewPage").getNodeValue().equalsIgnoreCase("true"));
            }

            if ( nnm.getNamedItem("isFloatColumnFooter") != null) {
                getReport().setFloatColumnFooter(nnm.getNamedItem("isFloatColumnFooter").getNodeValue().equalsIgnoreCase("true") );
            }

            if ( nnm.getNamedItem("language") != null) {
                getReport().setLanguage( nnm.getNamedItem("language").getNodeValue());
            }

            if ( nnm.getNamedItem("whenResourceMissingType") != null) {
                getReport().setWhenResourceMissingType( nnm.getNamedItem("whenResourceMissingType").getNodeValue());
            }

            if ( nnm.getNamedItem("isIgnorePagination") != null) {
                getReport().setIgnorePagination( nnm.getNamedItem("isIgnorePagination").getNodeValue().equalsIgnoreCase("true"));
            }
            
            if ( nnm.getNamedItem("formatFactoryClass") != null) {
                getReport().setFormatFactoryClass( nnm.getNamedItem("formatFactoryClass").getNodeValue());
            }



        } else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("property")) {

            // Load report property...
            it.businesslogic.ireport.JRProperty property = readPropertyElement(node);
            if (property.getName() != null && property.getName().length() != 0) {
                    getReport().addJRProperty( property );
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("import")) {

            // Load report IMPORT...
            NamedNodeMap nnm = node.getAttributes();
            if ( nnm.getNamedItem("value") != null) {
                getReport().addImport( nnm.getNamedItem("value").getNodeValue() );
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("template")) {

            // Load report IMPORT...
            String clazz = "java.lang.String";
            String exp = readPCDATA(node, true);
            
            NamedNodeMap nnm = node.getAttributes();
            if ( nnm.getNamedItem("class") != null) {
                clazz =  nnm.getNamedItem("class").getNodeValue();
            }
            
            if (exp.length() > 0)
            {
                Template template = new Template(exp,clazz);
                getReport().getTemplates().add(template);
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("reportFont")) {

            // Load report font...
            boolean isDefaultFont = false;
            IReportFont font = new IReportFont();

            NamedNodeMap nnm = node.getAttributes();

            for (int kkk=0; kkk<nnm.getLength(); ++kkk)
            {
                //System.out.println( + " " + );

                String propName = nnm.item(kkk).getNodeName();
                String propValue = nnm.item(kkk).getNodeValue();
                if (propName != null && propValue != null)
                {
                    if (propName.equals("name"))
                        font.setReportFont(propValue);
                    if (propName.equals("isDefault"))
                    {
                        isDefaultFont = propValue.equals("true");
                        font.setDefaultFont(isDefaultFont);
                    }
                    if (propName.equals("fontName"))
                        font.setFontName(propValue);
                    else if (propName.equals("pdfFontName"))
                        font.setPDFFontName(propValue);
                    else if (propName.equals("size"))
                        font.setFontSize( Integer.parseInt(""+propValue) );
                    else if (propName.equals("isBold"))
                        font.setBold( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isItalic"))
                        font.setItalic( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isUnderline"))
                        font.setUnderline( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isStrikeThrough"))
                        font.setStrikeTrought( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isPdfEmbedded"))
                        font.setPdfEmbedded((new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("pdfEncoding"))
                        font.setPdfEncoding( ""+propValue );
                }
            }


            if (isDefaultFont){
                getReport().setDefaultFont( (IReportFont) font.clone() );
            }

            getReport().getFonts().addElement( font );
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("style")) {

            Style style = readStyle(node);
            getReport().getStyles().addElement( style );

            // Update Styles parents...
            for (int i=0; i<getReport().getStyles().size(); ++i)
            {
               Style s = (Style)getReport().getStyles().elementAt(i);
               if (s.getAttribute(s.ATTRIBUTE_style) != null && !(s.getAttribute(s.ATTRIBUTE_style) instanceof Style))
               {
                   for (int j=0; j<getReport().getStyles().size(); ++j)
                   {
                        Style sparent = (Style)getReport().getStyles().elementAt(j);
                        if (sparent.getName().equals( s.getAttribute(s.ATTRIBUTE_style)+""))
                        {
                            s.getAttributes().put(s.ATTRIBUTE_style, sparent);
                        }
                   }
               }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("parameter")) {

            // Load parameter...
            it.businesslogic.ireport.JRParameter parameter = readParameterElement(node);

            if (parameter.getName() != null && parameter.getName().length() != 0) {
                getReport().addParameter( parameter );
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("queryString")) {
            // Load queryString
            NamedNodeMap nnm = node.getAttributes();
            if ( nnm.getNamedItem("language") != null) {
                getReport().setQueryLanguage( nnm.getNamedItem("language").getNodeValue());
            }

            getReport().setQuery( readPCDATA(node) );
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("filterExpression")) {
            getReport().setFilterExpression( readPCDATA(node) );
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("field")) {
            // Load field
            it.businesslogic.ireport.JRField field = readFieldElement(node);

            if (field.getName() != null && field.getName().length() != 0) {
                getReport().addField( field );
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("variable")) {
            // Load variable
            it.businesslogic.ireport.JRVariable variable = readVariableElement( node );

            if (variable.getName() != null && variable.getName().length() != 0) {
                getReport().addVariable( variable );
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("sortField")) {
            it.businesslogic.ireport.SortField sortField = readSortFieldElement( node );
            getReport().addSortField(sortField);
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("subDataset")) {
            // Load field
            it.businesslogic.ireport.SubDataset subDataset = new it.businesslogic.ireport.SubDataset();

            NamedNodeMap nnm = node.getAttributes();
            if ( nnm.getNamedItem("scriptletClass") != null) {
                subDataset.setScriptletClass( nnm.getNamedItem("scriptletClass").getNodeValue());
            }
            if ( nnm.getNamedItem("resourceBundle") != null) {
                subDataset.setResourceBundleBaseName( nnm.getNamedItem("resourceBundle").getNodeValue());
            }
            if ( nnm.getNamedItem("whenResourceMissingType") != null) {
                subDataset.setWhenResourceMissingType( nnm.getNamedItem("whenResourceMissingType").getNodeValue());
            }
            subDataset.setName((( nnm.getNamedItem("name") != null) ? nnm.getNamedItem("name").getNodeValue() : "SubDataset"));

            NodeList children = node.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);
                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("property")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.JRProperty property = readPropertyElement(nodeChild);
                        if (property.getName() != null && property.getName().length() != 0) {
                                subDataset.addJRProperty( property );
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("parameter")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.JRParameter parameter = readParameterElement(nodeChild);
                        if (parameter.getName() != null && parameter.getName().length() != 0) {
                                subDataset.addParameter( parameter );
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("field")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.JRField field = readFieldElement(nodeChild);

                        if (field.getName() != null && field.getName().length() != 0) {
                                subDataset.addField( field );
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("variable")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.JRVariable variable = readVariableElement(nodeChild);
                        if (variable.getName() != null && variable.getName().length() != 0) {
                                subDataset.addVariable( variable );
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("queryString")) {
                        // Take the CDATA...
                        NamedNodeMap nnmChild = nodeChild.getAttributes();
                        if ( nnmChild.getNamedItem("language") != null) {
                            subDataset.setQueryLanguage( nnmChild.getNamedItem("language").getNodeValue());
                        }
                        subDataset.setQuery( readPCDATA(nodeChild) );
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("filterExpression")) {
                       subDataset.setFilterExpression( readPCDATA(nodeChild) );
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("group")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.Group group = readGroupElement(subDataset, nodeChild, false);
                        subDataset.getGroups().add(group);
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("sortField")) {
                        // Take the CDATA...
                        it.businesslogic.ireport.SortField sortField = readSortFieldElement( nodeChild );
                        subDataset.addSortField(sortField);
                    }
                }
            }

            getReport().addSubDataset(subDataset );

        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("group")) {
            // Load group

            it.businesslogic.ireport.Group group = readGroupElement(getReport(), node, true);
            if (group.getName() != null && group.getName().length() != 0) {
                getReport().getGroups().addElement(group); // We don't use here addGroup method!
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("background")) {
            // Load background band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("background");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("title")) {
            // Load title band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("title");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("pageHeader")) {
            // Load pageHeader band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("pageHeader");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("columnHeader")) {
            // Load columnHeader band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("columnHeader");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("detail")) {
            // Load detail band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("detail");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("columnFooter")) {
            // Load columnFooter band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("columnFooter");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("pageFooter")) {
            // Load pageFooter band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("pageFooter");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("lastPageFooter")) {
            // Load title band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("lastPageFooter");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("summary")) {
            // Load summary band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("summary");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );

                    readBandElements(bandNode,b);
                }
            }
        }
        else if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("noData")) {
            // Load summary band
            NodeList list_child = node.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node bandNode = (Node)list_child.item(ck);
                if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                    // Read the band...
                    NamedNodeMap bandAttributes = bandNode.getAttributes();
                    Band b = getReport().getBandByName("noData");
                    if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                    if ( bandAttributes.getNamedItem("isSplitAllowed") != null) b.setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );

                    readBandElements(bandNode,b);
                }
            }
        }
        else
        {
            if (node.getNodeType() == node.ENTITY_REFERENCE_NODE)
            {
                seeInside = true;
            }
        }

        if (seeInside)
        {
            NodeList children = node.getChildNodes();
            if (children != null) {
                for (int i=0; i< children.getLength(); i++)
                    traverse(children.item(i));
            }
        }

    }

    private void readBandElements(Node bandNode, Band band) {
            readBandElements("", bandNode, band, null,null,null);
    }

     private void readBandElements(String parentElementGroup, Node bandNode, Band band, ReportElement parent) {
            readBandElements(parentElementGroup, bandNode, band, parent,null,null);
    }

    private it.businesslogic.ireport.JRParameter readParameterElement(Node parameterNode)
    {
        // Load parameter...
        it.businesslogic.ireport.JRParameter parameter = new it.businesslogic.ireport.JRParameter("","java.lang.String",true,"");

        NamedNodeMap nnm = parameterNode.getAttributes();
        if ( nnm.getNamedItem("name") != null) {
            parameter.setName( nnm.getNamedItem("name").getNodeValue());
        }
        if ( nnm.getNamedItem("class") != null) {
            parameter.setClassType( nnm.getNamedItem("class").getNodeValue() );
        }
        if ( nnm.getNamedItem("isForPrompting") != null) {
            parameter.setIsForPrompting( nnm.getNamedItem("isForPrompting").getNodeValue().equalsIgnoreCase("true") );
        }

        // Check for description and expression...
        NodeList children = parameterNode.getChildNodes();
        if (children != null) {
            for (int k=0; k< children.getLength(); k++) {
                Node nodeChild = (Node)children.item(k);
                if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("parameterDescription")) {
                    // Take the CDATA...

                    NodeList list_child = nodeChild.getChildNodes();
                    for (int ck = 0; ck< list_child.getLength(); ck++) {
                        Node child_child = (Node)list_child.item(ck);
                        if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                        child_child.getNodeType() == Node.TEXT_NODE) {
                            parameter.setDescription( child_child.getNodeValue() );
                        }
                    }
                } else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("defaultValueExpression")) {
                    // Take the CDATA...
                    NodeList list_child = nodeChild.getChildNodes();
                    for (int ck = 0; ck < list_child.getLength(); ck++) {
                        Node child_child = (Node)list_child.item(ck);
                        if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                        child_child.getNodeType() == Node.TEXT_NODE) {
                            parameter.setDefaultValueExpression( child_child.getNodeValue() );
                        }
                    }
                } else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("property")) {
                    
                    parameter.getProperties().add(readPropertyElement(nodeChild));
                    
                }
            }
        }

        return parameter;
    }

    private it.businesslogic.ireport.JRField readFieldElement(Node fieldNode)
    {
            it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField("","java.lang.String");
            field.setDescription("");

            NamedNodeMap nnm = fieldNode.getAttributes();
            if ( nnm.getNamedItem("name") != null) {
                field.setName( nnm.getNamedItem("name").getNodeValue());
            }
            if ( nnm.getNamedItem("class") != null) {
                field.setClassType( nnm.getNamedItem("class").getNodeValue() );
            }

            NodeList children = fieldNode.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);

                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("fieldDescription")) {
                        // Take the CDATA...
                        NodeList list_child = nodeChild.getChildNodes();

                        for (int ck=0; ck< list_child.getLength(); ck++) {

                            Node child_child = (Node)list_child.item(ck);
                            if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                            child_child.getNodeType() == Node.TEXT_NODE) {
                                field.setDescription( child_child.getNodeValue() );
                            }
                        }
                    }   else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("property")) {
                    
                        field.getProperties().add(readPropertyElement(nodeChild));
                    
                    }
                }

            }

            return field;
    }

    private SortField readSortFieldElement(Node fieldNode)
    {
            SortField field = new SortField();

            NamedNodeMap nnm = fieldNode.getAttributes();
            if ( nnm.getNamedItem("name") != null) {
                field.setFieldName( nnm.getNamedItem("name").getNodeValue());
            }
            if ( nnm.getNamedItem("order") != null) {
                field.setDesc( nnm.getNamedItem("order").getNodeValue().equals("Descending") );
            }

            return field;
    }
    
    private it.businesslogic.ireport.JRProperty readPropertyElement(Node propertyNode)
    {
            // Load report property...
            JRProperty property = new JRProperty();
            NamedNodeMap nnm = propertyNode.getAttributes();
            if ( nnm.getNamedItem("name") != null) {
                property.setName( nnm.getNamedItem("name").getNodeValue());
            }
            if ( nnm.getNamedItem("value") != null) {
                property.setValue( nnm.getNamedItem("value").getNodeValue());
            }

            return property;
    }

    private it.businesslogic.ireport.JRVariable readVariableElement(Node variableNode)
    {
        // Load variable
            it.businesslogic.ireport.JRVariable variable = new it.businesslogic.ireport.JRVariable("",false);
            variable.setResetType("Report"); //Default value...
            variable.setResetGroup(""); //Default value...

            variable.setIncrementType("None"); //Default value...
            variable.setIncrementGroup(""); //Default value...

            variable.setCalculation("Nothing"); //Default value...

            NamedNodeMap nnm = variableNode.getAttributes();
            if ( nnm.getNamedItem("name") != null) {
                variable.setName( nnm.getNamedItem("name").getNodeValue());
            }
            if ( nnm.getNamedItem("class") != null) {
                variable.setClassType( nnm.getNamedItem("class").getNodeValue() );
            }
            if ( nnm.getNamedItem("resetType") != null) {
                variable.setResetType( nnm.getNamedItem("resetType").getNodeValue() );
            }
            if ( nnm.getNamedItem("resetGroup") != null) {
                variable.setResetGroup( nnm.getNamedItem("resetGroup").getNodeValue() );
            }
            if ( nnm.getNamedItem("calculation") != null) {
                variable.setCalculation( nnm.getNamedItem("calculation").getNodeValue() );
            }
            if ( nnm.getNamedItem("incrementerFactoryClass") != null) {
                variable.setIncrementerFactoryClass( nnm.getNamedItem("incrementerFactoryClass").getNodeValue() );
            }
            if ( nnm.getNamedItem("incrementType") != null) {
                variable.setIncrementType( nnm.getNamedItem("incrementType").getNodeValue());
            }
            if ( nnm.getNamedItem("incrementGroup") != null) {
                variable.setIncrementGroup( nnm.getNamedItem("incrementGroup").getNodeValue());
            }

            // Check for description and expression...
            NodeList children = variableNode.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);
                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("variableExpression")) {
                        // Take the CDATA...
                        NodeList list_child = nodeChild.getChildNodes();
                        for (int ck=0; ck< list_child.getLength(); ck++) {
                            Node child_child = (Node)list_child.item(ck);
                            if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                            child_child.getNodeType() == Node.TEXT_NODE) {
                                variable.setExpression( child_child.getNodeValue() );
                            }
                        }
                    }
                    else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("initialValueExpression")) {
                        // Take the CDATA...
                        NodeList list_child = nodeChild.getChildNodes();
                        for (int ck=0; ck< list_child.getLength(); ck++) {
                            Node child_child = (Node)list_child.item(ck);
                            if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                            child_child.getNodeType() == Node.TEXT_NODE) {
                                variable.setInitialValueExpression( child_child.getNodeValue() );
                            }
                        }
                    }
                }
            }

            return variable;
    }

    private it.businesslogic.ireport.crosstab.Measure readMeasureElement(Node measureNode)
    {
        // Load variable
            it.businesslogic.ireport.crosstab.Measure measure = new it.businesslogic.ireport.crosstab.Measure("");

            NamedNodeMap nnm = measureNode.getAttributes();
            if ( nnm.getNamedItem("name") != null) {
                measure.setName( nnm.getNamedItem("name").getNodeValue());
            }
            if ( nnm.getNamedItem("class") != null) {
                measure.setClassType( nnm.getNamedItem("class").getNodeValue() );
            }
            if ( nnm.getNamedItem("calculation") != null) {
                measure.setCalculation( nnm.getNamedItem("calculation").getNodeValue() );
            }
            if ( nnm.getNamedItem("incrementerFactoryClass") != null) {
                measure.setIncrementerFactoryClass( nnm.getNamedItem("incrementerFactoryClass").getNodeValue() );
            }
            if ( nnm.getNamedItem("percentageOf") != null) {
                measure.setPercentageOf( nnm.getNamedItem("percentageOf").getNodeValue());
            }
            if ( nnm.getNamedItem("percentageCalculatorClass") != null) {
                measure.setPercentageCalculatorClass( nnm.getNamedItem("percentageCalculatorClass").getNodeValue());
            }

            // Check for description and expression...
            NodeList children = measureNode.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);
                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("measureExpression")) {
                        measure.setExpression(readPCDATA(nodeChild));
                    }
                }
            }

            return measure;
    }





    private it.businesslogic.ireport.Style readStyle(Node styleNode)
    {
        return readStyle(styleNode, null);
    }

    /**
     * If a ConditionedStyle, the style is interpreted like part of the tag conditionalStyle
     */
    private it.businesslogic.ireport.Style readStyle(Node styleNode, ConditionedStyle cStyle)
    {
        Style style = new Style();
        if (cStyle != null) style = cStyle;
        NamedNodeMap nnm = styleNode.getAttributes();

        for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
        {
             if ( nnm.getNamedItem(Style.JRXMLStyleAttributes[i]) != null) {
                style.getAttributes().put(Style.JRXMLStyleAttributes[i], nnm.getNamedItem(Style.JRXMLStyleAttributes[i]).getNodeValue());
             }
        }

        //conditionalStyle
        // Check for description and expression...
        NodeList children = styleNode.getChildNodes();
        if (children != null) {
            for (int k=0; k< children.getLength(); k++) {
                Node nodeChild = (Node)children.item(k);
                if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("conditionalStyle")) {
                    ConditionedStyle childStyle = readConditionalStyle(nodeChild);
                    style.getConditionedStyles().add(childStyle);
                }
            }
        }

        return style;
    }

    /**
     * If a ConditionedStyle, the style is interpreted like part of the tag conditionalStyle
     */
    private it.businesslogic.ireport.ConditionedStyle readConditionalStyle(Node styleNode)
    {
        ConditionedStyle style = new ConditionedStyle();

        //conditionalStyle
        // Check for description and expression...
        NodeList children = styleNode.getChildNodes();
        if (children != null) {
            for (int k=0; k< children.getLength(); k++) {
                Node nodeChild = (Node)children.item(k);
                if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("conditionExpression")) {
                    style.setCondition(readPCDATA(nodeChild));
                }
                else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("style")) {
                    style = (ConditionedStyle)readStyle(nodeChild, style);
                }
            }
        }

        return style;
    }

    private it.businesslogic.ireport.Group readGroupElement(SubDataset ds, Node groupNode, boolean fullRead)
    {
        NamedNodeMap nnm = groupNode.getAttributes();
        it.businesslogic.ireport.Group group = new it.businesslogic.ireport.Group(ds, ""+nnm.getNamedItem("name").getNodeValue());

        if ( nnm.getNamedItem("isStartNewColumn") != null) group.setIsStartNewColumn( nnm.getNamedItem("isStartNewColumn").getNodeValue().equals("true") );
        if ( nnm.getNamedItem("isStartNewPage") != null) group.setIsStartNewPage( nnm.getNamedItem("isStartNewPage").getNodeValue().equals("true") );
        if ( nnm.getNamedItem("isResetPageNumber") != null) group.setIsResetPageNumber( nnm.getNamedItem("isResetPageNumber").getNodeValue().equals("true") );
        if ( nnm.getNamedItem("isReprintHeaderOnEachPage") != null) group.setIsReprintHeaderOnEachPage( nnm.getNamedItem("isReprintHeaderOnEachPage").getNodeValue().equals("true") );
        if ( nnm.getNamedItem("minHeightToStartNewPage") != null) group.setMinHeightToStartNewPage( Integer.parseInt(nnm.getNamedItem("minHeightToStartNewPage").getNodeValue()));

        // Looking for header, footer and expression...
        NodeList children = groupNode.getChildNodes();
        if (children != null) {
            for (int k=0; k< children.getLength(); k++) {
                Node nodeChild = (Node)children.item(k);
                if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("groupExpression")) {
                    // Take the CDATA...
                    NodeList list_child = nodeChild.getChildNodes();
                    for (int ck=0; ck< list_child.getLength(); ck++) {
                        Node child_child = (Node)list_child.item(ck);
                        if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                        child_child.getNodeType() == Node.TEXT_NODE) {
                            group.setGroupExpression( child_child.getNodeValue() );
                        }
                    }
                }
                else if (fullRead && nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("groupHeader")) {
                    // Take the band...
                    NodeList list_child = nodeChild.getChildNodes();
                    for (int ck=0; ck< list_child.getLength(); ck++) {
                        Node bandNode = (Node)list_child.item(ck);
                        if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                            // Read the band...
                            NamedNodeMap bandAttributes = bandNode.getAttributes();
                            if ( bandAttributes.getNamedItem("height") != null) group.getGroupHeader().setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                            if ( bandAttributes.getNamedItem("isSplitAllowed") != null) group.getGroupHeader().setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                            readBandElements(bandNode,group.getGroupHeader());
                        }
                    }
                }
                else if (fullRead && nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("groupFooter")) {
                    // Take the band...
                    NodeList list_child = nodeChild.getChildNodes();
                    for (int ck=0; ck< list_child.getLength(); ck++) {
                        Node bandNode = (Node)list_child.item(ck);
                        if (bandNode.getNodeType() == Node.ELEMENT_NODE && bandNode.getNodeName().equals("band")) {
                            // Read the band...
                            NamedNodeMap bandAttributes = bandNode.getAttributes();
                            if ( bandAttributes.getNamedItem("height") != null) group.getGroupFooter().setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                            if ( bandAttributes.getNamedItem("isSplitAllowed") != null) group.getGroupFooter().setSplitAllowed( bandAttributes.getNamedItem("isSplitAllowed").getNodeValue().equals("true") );
                            readBandElements(bandNode,group.getGroupFooter());
                        }
                    }
                }
            }
        }

        return group;
    }


    private void readBandElements(String parentElementGroup, Node bandNode, Band band, ReportElement parentElement, CrosstabReportElement crosstabReportElement, CrosstabCell cell) {

        ReportElement rElement = null;
        Style defualtStyle = getReport().getDefaultStyle();

        NodeList list_child = bandNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child = (Node)list_child.item(ck);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("printWhenExpression")) {
                // Read the band...
                //NamedNodeMap bandAttributes = bandNode.getAttributes();
                //if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
                // Take subelements and lloking for the expression...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    if (child_child.getNodeType() == Node.CDATA_SECTION_NODE ||
                    child_child.getNodeType() == Node.TEXT_NODE) {
                        band.setPrintWhenExpression(child_child.getNodeValue());
                    }
                }
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("staticText")) {
                StaticTextReportElement re = new StaticTextReportElement(0,0,0,0);
                re.setIReportFont( getReport().getDefaultFont());

                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("textElement")) {
                        readXMLTextElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("text")) {
                        re.setText( readPCDATA(child_child, false) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("box")) {
                        readBoxElement(child_child,re);
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("image")) {
                ImageReportElement re = new ImageReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                // Modificacion para geopista
                boolean mapImage = false;
                // Fin de modificacion para geopista
                if (nodeAttributes.getNamedItem("isUsingCache") != null)
                    re.setIsUsingCache( (""+nodeAttributes.getNamedItem("isUsingCache").getNodeValue()).equals("true") );
                if (nodeAttributes.getNamedItem("isLazy") != null)
                    re.setIsLazy( (""+nodeAttributes.getNamedItem("isLazy").getNodeValue()).equals("true") );
                if (nodeAttributes.getNamedItem("onErrorType") != null)
                    re.setOnErrorType(""+nodeAttributes.getNamedItem("onErrorType").getNodeValue());
                if (nodeAttributes.getNamedItem("scaleImage") != null)
                    re.setScaleImage(""+nodeAttributes.getNamedItem("scaleImage").getNodeValue());
                if (nodeAttributes.getNamedItem("vAlign") != null)
                    re.setVerticalAlignment( ""+nodeAttributes.getNamedItem("vAlign").getNodeValue());
                if (nodeAttributes.getNamedItem("hAlign") != null)
                    re.setHorizontalAlignment(""+nodeAttributes.getNamedItem("hAlign").getNodeValue());
                if (nodeAttributes.getNamedItem("evaluationTime") != null)
                    re.setEvaluationTime( ""+nodeAttributes.getNamedItem("evaluationTime").getNodeValue());
                if (nodeAttributes.getNamedItem("evaluationGroup") != null)
                    re.setEvaluationGroup( ""+nodeAttributes.getNamedItem("evaluationGroup").getNodeValue());
                //if (nodeAttributes.getNamedItem("hyperlinkType") != null)
                //    re.setHyperlinkType(""+nodeAttributes.getNamedItem("hyperlinkType").getNodeValue());
                //if (nodeAttributes.getNamedItem("hyperlinkTarget") != null)
                //    re.setHyperlinkTarget(""+nodeAttributes.getNamedItem("hyperlinkTarget").getNodeValue());
                //if (nodeAttributes.getNamedItem("bookmarkLevel") != null)
                //    re.setBookmarkLevel( Integer.parseInt( nodeAttributes.getNamedItem("bookmarkLevel").getNodeValue() ) );

                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }		// Element properties...
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("graphicElement")) {
                        if (subNodeAttributes.getNamedItem("pen") != null) {
                            re.setGraphicElementPen( ""+subNodeAttributes.getNamedItem("pen").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("fill") != null) {
                            re.setPropertyValue(GraphicReportElement.FILL, ""+subNodeAttributes.getNamedItem("fill").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("stretchType") != null) {
                            re.setStretchType(""+subNodeAttributes.getNamedItem("stretchType").getNodeValue());
                        }
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("imageExpression")) {
                        if (subNodeAttributes.getNamedItem("class") != null) {
                            re.setImageClass(""+subNodeAttributes.getNamedItem("class").getNodeValue());
                        }
                        re.setImageExpression( readPCDATA(child_child));
                        re.setReportDirectory(new java.io.File(getReport().getFilename()).getParentFile());
                        
                        // Modificacion para Geopista
                        mapImage = MapImageExpressionManager.isMapImageExpression(re.getImageExpression());
                        
                        // Fin de modificacion
                    }
                    /*
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("anchorNameExpression")) {
                        re.setAnchorNameExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkAnchorExpression")) {
                        re.setHyperlinkAnchorExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkPageExpression")) {
                        re.setHyperlinkPageExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkReferenceExpression")) {
                        re.setHyperlinkReferenceExpression( readPCDATA(child_child) );
                    }
                    */
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("box")) {
                        readBoxElement(child_child,re);
                    }
                }
                readHyperlink(child, re);
                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);

                if (re.getImageExpression().indexOf("it.businesslogic.ireport.chart.DefaultChartFactory.drawChart(") >= 0) {
                    ChartReportElement chart = new ChartReportElement( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    chart.setBand( re.getBand() );
                    chart.setElementGroup( re.getElementGroup());
                    chart.setParentElement( re.getParentElement());
                    chart.setHyperlinkReferenceExpression( re.getHyperlinkReferenceExpression());
                    chart.setHyperlinkPageExpression( re.getHyperlinkPageExpression());
                    chart.setHyperlinkAnchorExpression( re.getHyperlinkAnchorExpression());
                    chart.setHyperlinkType( re.getHyperlinkType());
                    chart.setBgcolor( re.getBgcolor());
                    chart.setEvaluationGroup( re.getEvaluationGroup());
                    chart.setEvaluationTime( re.getEvaluationTime());
                    chart.setFgcolor( re.getFgcolor());
                    chart.setFill( re.getFill());
                    //chart.setGraphicElementPen( re.getGraphicElementPen());
                    chart.setHorizontalAlignment( re.getHorizontalAlignment());
                    chart.setImageClass( re.getImageClass());
                    chart.setImageExpression( re.getImageExpression());
                    chart.setName( re.getName());
                    chart.setPositionType( re.getPositionType());
                    chart.setPrintWhenExpression( re.getPrintWhenExpression() );
                    chart.setPrintWhenGroupChanges( re.getPrintWhenGroupChanges());
                    chart.setScaleImage( re.getScaleImage());
                    chart.setStretchType( re.getStretchType());
                    chart.setTransparent( re.getTransparent());
                    chart.setVerticalAlignment( re.getVerticalAlignment());
                    chart.parseProperties( getReport().getJRproperties() );
                    re = chart;
                }

                if (re.getImageExpression().indexOf("it.businesslogic.ireport.barcode.BcImage.getBarcodeImage(") >= 0) {
                    BarcodeReportElement bc = new BarcodeReportElement( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    bc.setBand( re.getBand() );
                    bc.setCell( cell );
                    bc.setElementGroup( re.getElementGroup());
                    bc.setParentElement( re.getParentElement());
                    bc.setHyperlinkReferenceExpression( re.getHyperlinkReferenceExpression());
                    bc.setHyperlinkPageExpression( re.getHyperlinkPageExpression());
                    bc.setHyperlinkAnchorExpression( re.getHyperlinkAnchorExpression());
                    bc.setHyperlinkType( re.getHyperlinkType());
                    bc.setBgcolor( re.getBgcolor());
                    bc.setEvaluationGroup( re.getEvaluationGroup());
                    bc.setEvaluationTime( re.getEvaluationTime());
                    bc.setFgcolor( re.getFgcolor());
                    bc.setFill( re.getFill());
                    //bc.setGraphicElementPen( re.getGraphicElementPen());
                    bc.setHorizontalAlignment( re.getHorizontalAlignment());
                    bc.setImageClass( re.getImageClass());
                    bc.setImageExpression( re.getImageExpression());
                    bc.setName( re.getName());
                    bc.setPositionType( re.getPositionType());
                    bc.setPrintWhenExpression( re.getPrintWhenExpression() );
                    bc.setPrintWhenGroupChanges( re.getPrintWhenGroupChanges());
                    bc.setScaleImage( re.getScaleImage());
                    bc.setStretchType( re.getStretchType());
                    bc.setTransparent( re.getTransparent());
                    bc.setVerticalAlignment( re.getVerticalAlignment());

                    re = bc;
                }
                // Modificacion para geopista
                if (mapImage) {
                    MapImageReportElement mire = new MapImageReportElement( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    mire.setBand( re.getBand() );
                    mire.setCell( cell );
                    mire.setElementGroup( re.getElementGroup());
                    mire.setParentElement( re.getParentElement());
                    mire.setHyperlinkReferenceExpression( re.getHyperlinkReferenceExpression());
                    mire.setHyperlinkPageExpression( re.getHyperlinkPageExpression());
                    mire.setHyperlinkAnchorExpression( re.getHyperlinkAnchorExpression());
                    mire.setHyperlinkType( re.getHyperlinkType());
                    mire.setBgcolor( re.getBgcolor());
                    mire.setEvaluationGroup( re.getEvaluationGroup());
                    mire.setEvaluationTime( re.getEvaluationTime());
                    mire.setFgcolor( re.getFgcolor());
                    mire.setFill( re.getFill());
                    //bc.setGraphicElementPen( re.getGraphicElementPen());
                    mire.setHorizontalAlignment( re.getHorizontalAlignment());
                    mire.setImageClass( re.getImageClass());
                    mire.setImageExpression( re.getImageExpression());
                    mire.setName( re.getName());
                    mire.setPositionType( re.getPositionType());
                    mire.setPrintWhenExpression( re.getPrintWhenExpression() );
                    mire.setPrintWhenGroupChanges( re.getPrintWhenGroupChanges());
                    mire.setScaleImage( re.getScaleImage());
                    mire.setStretchType( re.getStretchType());
                    mire.setTransparent( re.getTransparent());
                    mire.setVerticalAlignment( re.getVerticalAlignment());
                    re = mire;
                }
                // Fin de modificacion para geopista

                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("rectangle")) {
                RectangleReportElement re = new RectangleReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                /* CompatibilitySupport.saveRoundedRectangle */
                if (nodeAttributes.getNamedItem("radius") != null)
                    re.setRadius( Integer.parseInt( (""+nodeAttributes.getNamedItem("radius").getNodeValue())) );
                // Element properties...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }		// Element properties...
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("graphicElement")) {
                        if (subNodeAttributes.getNamedItem("pen") != null) {
                            re.setGraphicElementPen( ""+subNodeAttributes.getNamedItem("pen").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("fill") != null) {
                            re.setPropertyValue(GraphicReportElement.FILL, ""+subNodeAttributes.getNamedItem("fill").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("stretchType") != null) {
                            re.setStretchType(""+subNodeAttributes.getNamedItem("stretchType").getNodeValue());
                        }
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);

                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("line")) {
                LineReportElement re = new LineReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                /* CompatibilitySupport.saveRoundedRectangle */
                if (nodeAttributes.getNamedItem("direction") != null)
                    re.setDirection( ""+nodeAttributes.getNamedItem("direction").getNodeValue() );

                // Element properties...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }		// Element properties...
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("graphicElement")) {
                        if (subNodeAttributes.getNamedItem("pen") != null) {
                            re.setGraphicElementPen( ""+subNodeAttributes.getNamedItem("pen").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("fill") != null) {
                            re.setPropertyValue(GraphicReportElement.FILL, ""+subNodeAttributes.getNamedItem("fill").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("stretchType") != null) {
                            re.setStretchType(""+subNodeAttributes.getNamedItem("stretchType").getNodeValue());
                        }
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("break")) {
                BreakReportElement re = new BreakReportElement(0,0,0,0);
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                /* CompatibilitySupport.saveRoundedRectangle */
                if (nodeAttributes.getNamedItem("type") != null)
                    re.setType( ""+nodeAttributes.getNamedItem("type").getNodeValue() );

                // Element properties...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }		// Element properties...
                }
                
                // Fix height and position...
                re.setHeight(0);
                re.setWidth( report.getWidth() - report.getRightMargin() - report.getLeftMargin() );
                re.position.x=0;
                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("ellipse")) {
                EllipseReportElement re = new EllipseReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                /* CompatibilitySupport.saveRoundedRectangle */
                //if (nodeAttributes.getNamedItem("direction") != null)
                //	re.setDirection( ""+nodeAttributes.getNamedItem("direction").getNodeValue() );

                // Element properties...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }		// Element properties...
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("graphicElement")) {
                        if (subNodeAttributes.getNamedItem("pen") != null) {
                            re.setGraphicElementPen( ""+subNodeAttributes.getNamedItem("pen").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("fill") != null) {
                            re.setPropertyValue(GraphicReportElement.FILL, ""+subNodeAttributes.getNamedItem("fill").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("stretchType") != null) {
                            re.setStretchType(""+subNodeAttributes.getNamedItem("stretchType").getNodeValue());
                        }
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("textField")) {
                TextFieldReportElement re = new TextFieldReportElement(0,0,0,0);

                re.setIReportFont( getReport().getDefaultFont());
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                /* CompatibilitySupport.saveRoundedRectangle */
                if (nodeAttributes.getNamedItem("evaluationTime") != null)
                    re.setEvaluationTime( ""+nodeAttributes.getNamedItem("evaluationTime").getNodeValue() );
                if (nodeAttributes.getNamedItem("evaluationGroup") != null)
                    re.setGroup( ""+nodeAttributes.getNamedItem("evaluationGroup").getNodeValue() );
                if (nodeAttributes.getNamedItem("isStretchWithOverflow") != null)
                    re.setStretchWithOverflow( nodeAttributes.getNamedItem("isStretchWithOverflow").getNodeValue().equals("true"));
                if (nodeAttributes.getNamedItem("isBlankWhenNull") != null)
                    re.setBlankWhenNull(nodeAttributes.getNamedItem("isBlankWhenNull").getNodeValue().equals("true") );
                if (nodeAttributes.getNamedItem("pattern") != null)
                    re.setPattern( ""+nodeAttributes.getNamedItem("pattern").getNodeValue() );
                //if (nodeAttributes.getNamedItem("hyperlinkType") != null)
                //    re.setHyperlinkType( ""+nodeAttributes.getNamedItem("hyperlinkType").getNodeValue() );
                //if (nodeAttributes.getNamedItem("hyperlinkTarget") != null)
                //    re.setHyperlinkTarget( ""+nodeAttributes.getNamedItem("hyperlinkTarget").getNodeValue() );
                //if (nodeAttributes.getNamedItem("bookmarkLevel") != null)
                //    re.setBookmarkLevel( Integer.parseInt( nodeAttributes.getNamedItem("bookmarkLevel").getNodeValue() ) );

                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("textElement")) {
                        readXMLTextElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("textFieldExpression")) {
                        if (subNodeAttributes.getNamedItem("class") != null) {
                            re.setClassExpression(""+subNodeAttributes.getNamedItem("class").getNodeValue());
                        }
                        re.setText( readPCDATA(child_child).trim());
                    }
                    /*
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("anchorNameExpression")) {
                        re.setAnchorNameExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkAnchorExpression")) {
                        re.setHyperlinkAnchorExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkPageExpression")) {
                        re.setHyperlinkPageExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkReferenceExpression")) {
                        re.setHyperlinkReferenceExpression( readPCDATA(child_child) );
                    }
                    */
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("box")) {
                        readBoxElement(child_child,re);
                    }
                }
                readHyperlink(child, re);
                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("frame")) {
                FrameReportElement re = new FrameReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setCell( cell );
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();

                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("box")) {
                        readBoxElement(child_child,re);
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                rElement = re;

                if (crosstabReportElement != null)
                {
                    crosstabReportElement.getElements().addElement( rElement );
                }
                else
                {
                    getReport().getElements().addElement(rElement);
                }

                readBandElements("", child,band, re, crosstabReportElement, cell);
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("elementGroup")) {
                String newgroup = getReport().createChildGroup(parentElementGroup);
                readBandElements(newgroup, child,band, parentElement, crosstabReportElement, cell);
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("subreport")) {
                SubReportElement re = new SubReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                NamedNodeMap nodeAttributes = child.getAttributes();
                if (nodeAttributes.getNamedItem("isUsingCache") != null)
                    re.setIsUsingCache(nodeAttributes.getNamedItem("isUsingCache").getNodeValue().equals("true") );

                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("parametersMapExpression")) {
                        re.setParametersMapExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("returnValue")) {
                        it.businesslogic.ireport.JRSubreportReturnValue returnValue = new it.businesslogic.ireport.JRSubreportReturnValue();

                        if (subNodeAttributes.getNamedItem("subreportVariable") != null) {
                            returnValue.setSubreportVariable( ""+subNodeAttributes.getNamedItem("subreportVariable").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("toVariable") != null) {
                            returnValue.setToVariable( ""+subNodeAttributes.getNamedItem("toVariable").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("calculation") != null) {
                            returnValue.setCalculation( ""+subNodeAttributes.getNamedItem("calculation").getNodeValue());
                        }
                        if (subNodeAttributes.getNamedItem("incrementerFactoryClass") != null) {
                            returnValue.setIncrementFactoryClass( ""+subNodeAttributes.getNamedItem("incrementerFactoryClass").getNodeValue());
                        }
                        re.getReturnValues().addElement(returnValue );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("subreportParameter")) {
                        String name = "";
                        if (subNodeAttributes.getNamedItem("name") != null) {
                            name = ""+subNodeAttributes.getNamedItem("name").getNodeValue();
                        }
                        // Find expression in childs......
                        String expression = "";
                        NodeList childsOfChildOfChild = child_child.getChildNodes();
                        for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                            Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                            if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("subreportParameterExpression")) {
                                expression = readPCDATA(child_child_child);
                                break;
                            }
                        }
                        re.getSubreportParameters().addElement( new it.businesslogic.ireport.JRSubreportParameter( name, expression));
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("connectionExpression")) {
                        re.setConnectionExpression( readPCDATA(child_child));
                        re.setUseConnection(true);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataSourceExpression")) {
                        re.setDataSourceExpression(readPCDATA(child_child));
                        re.setUseConnection(false);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("subreportExpression")) {
                        re.setSubreportExpression(readPCDATA(child_child));
                        if (subNodeAttributes.getNamedItem("class") != null) {
                            re.setSubreportExpressionClass(""+subNodeAttributes.getNamedItem("class").getNodeValue());
                        }
                    }
                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("pieChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("pie3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("barChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bar3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyBarChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("stackedBarChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("stackedBar3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("lineChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyLineChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("areaChart") ||
                    child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("stackedAreaChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyAreaChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("scatterChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bubbleChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("timeSeriesChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("highLowChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("candlestickChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("meterChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("thermometerChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("multiAxisChart")) {
                ChartReportElement2 re = new ChartReportElement2(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);

                readChartReportElement(child, re);

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                //elements.addElement(re);
                rElement = re;
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("crosstab")) {
                CrosstabReportElement re = new CrosstabReportElement(0,0,0,0);
                if (defualtStyle != null) { re.setStyle(defualtStyle); re.setStyle(null); }
                re.setBand(band);
                re.setElementGroup(parentElementGroup);
                re.setParentElement(parentElement);
                re.setReport( getReport() );
                NamedNodeMap nodeAttributes = child.getAttributes();

                if (nodeAttributes.getNamedItem("isRepeatColumnHeaders") != null)
                    re.setRepeatColumnHeaders(nodeAttributes.getNamedItem("isRepeatColumnHeaders").getNodeValue().equals("true") );

                if (nodeAttributes.getNamedItem("isRepeatRowHeaders") != null)
                    re.setRepeatRowHeaders(nodeAttributes.getNamedItem("isRepeatRowHeaders").getNodeValue().equals("true") );

                if (nodeAttributes.getNamedItem("isRepeatRowHeaders") != null)
                {
                    try {
                        int columnBreakOffset = Integer.parseInt(nodeAttributes.getNamedItem("columnBreakOffset").getNodeValue() );
                        re.setColumnBreakOffset( columnBreakOffset );
                    } catch (Exception ex)
                    {
                        System.out.println("Invalid columnBreakOffset, using default");
                    }
                }
                
                if (nodeAttributes.getNamedItem("runDirection") != null)
                    re.setRunDirection( nodeAttributes.getNamedItem("runDirection").getNodeValue() );

                

                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();

                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                        readXMLReportElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("parametersMapExpression")) {
                        re.setParametersMapExpression( readPCDATA(child_child) );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("crosstabParameter")) {
                        String name = "";
                        String clazz = "";
                        if (subNodeAttributes.getNamedItem("name") != null) {
                            name = ""+subNodeAttributes.getNamedItem("name").getNodeValue();
                        }
                        CrosstabParameter cp = new CrosstabParameter(name);
                        if (subNodeAttributes.getNamedItem("class") != null) {
                            clazz = ""+subNodeAttributes.getNamedItem("class").getNodeValue();
                            cp.setClassType(clazz);
                        }

                        // Find expression in childs......
                        String expression = "";
                        NodeList childsOfChildOfChild = child_child.getChildNodes();
                        for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                            Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                            if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("parameterValueExpression")) {
                                expression = readPCDATA(child_child_child);
                                cp.setParameterValueExpression( expression );
                                break;
                            }
                        }

                        re.getCrosstabParameters().addElement( cp );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("crosstabDataset")) {

                        re.setUseDataset( true );
                        if (subNodeAttributes.getNamedItem("isDataPreSorted") != null) {
                            re.setPreSorted( subNodeAttributes.getNamedItem("isDataPreSorted").getNodeValue().equals("true") );

                        }

                        NodeList childsOfChildOfChild = child_child.getChildNodes();
                        for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                            Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                            if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("dataset")) {
                                readDataset(child_child_child, re.getDataset());
                                break;
                            }
                        }
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("rowGroup")) {
                        readCrosstabRowGroup(child_child, re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("columnGroup")) {
                        readCrosstabColumnGroup(child_child, re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("measure")) {
                        Measure measure = readMeasureElement(child_child);
                        re.getMeasures().add(measure);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("crosstabCell")) {
                        it.businesslogic.ireport.crosstab.CrosstabCell detailCell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                        readCellContents(child_child, detailCell, re);
                        detailCell.setType( detailCell.DETAIL_CELL );
                        detailCell.setParent(re);
                        re.getCells().add( detailCell );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("whenNoDataCell")) {
                        it.businesslogic.ireport.crosstab.CrosstabCell detailCell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                        readCellContents(child_child, detailCell, re);
                        detailCell.setType( detailCell.NODATA_CELL );
                        detailCell.setParent(re);
                        re.getCells().add( detailCell );
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("crosstabHeaderCell")) {
                        it.businesslogic.ireport.crosstab.CrosstabCell detailCell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                        readCellContents(child_child, detailCell, re);
                        detailCell.setType( detailCell.CT_HEADER_CELL );
                        detailCell.setParent(re);
                        re.getCells().add( detailCell );
                    }

                }

                re.setPosition(re.position);
                re.trasform(new java.awt.Point(0,0),TransformationType.TRANSFORMATION_RESIZE_SE);
                rElement = re;
            }


            //System.out.println(""+child.getNodeName());
            if (rElement != null && !(rElement instanceof FrameReportElement))
            {
                if (crosstabReportElement != null)
                {
                    crosstabReportElement.getElements().addElement( rElement );
                }
                else
                {
                    getReport().getElements().addElement(rElement);
                }
            }
            rElement = null;
        }

    }

    /**
     * Read all the hyperlink related attibutes and parameters
     *
     */
    private void readHyperlink(Node elementNode, HyperLinkableReportElement re)
    {
        NodeList childsOfChild = elementNode.getChildNodes();

        NamedNodeMap nodeAttributes = elementNode.getAttributes();
        if (nodeAttributes.getNamedItem("hyperlinkType") != null)
            re.setHyperlinkType( ""+nodeAttributes.getNamedItem("hyperlinkType").getNodeValue() );
        if (nodeAttributes.getNamedItem("hyperlinkTarget") != null)
            re.setHyperlinkTarget( ""+nodeAttributes.getNamedItem("hyperlinkTarget").getNodeValue() );
        if (nodeAttributes.getNamedItem("bookmarkLevel") != null)
            re.setBookmarkLevel( Integer.parseInt( nodeAttributes.getNamedItem("bookmarkLevel").getNodeValue() ) );
        
        
                
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            NamedNodeMap subNodeAttributes = child_child.getAttributes();
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkParameter")) {
                String name = "";
                if (subNodeAttributes.getNamedItem("name") != null) {
                    name = ""+subNodeAttributes.getNamedItem("name").getNodeValue();
                }
                // Find expression in childs......
                String expression = "";
                NodeList childsOfChildOfChild = child_child.getChildNodes();
                for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                    Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                    if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("hyperlinkParameterExpression")) {
                        expression = readPCDATA(child_child_child);
                        break;
                    }
                }
                re.getLinkParameters().add( new it.businesslogic.ireport.JRLinkParameter( name, expression));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("anchorNameExpression")) {
                re.setAnchorNameExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkAnchorExpression")) {
                re.setHyperlinkAnchorExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkPageExpression")) {
                re.setHyperlinkPageExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkReferenceExpression")) {
                re.setHyperlinkReferenceExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkTooltipExpression")) {
                re.setTooltipExpression(readPCDATA(child_child) );
            }
        }
    }

    private void readCrosstabRowGroup(Node rowGroupXmlNode, CrosstabReportElement re)
    {
        CrosstabRowGroup group = new CrosstabRowGroup();


        NamedNodeMap nodeAttributes = rowGroupXmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("name") != null)
            group.setName( nodeAttributes.getNamedItem("name").getNodeValue() );

        if (nodeAttributes.getNamedItem("width") != null)
        {
           try {  group.setWidth( Integer.parseInt( nodeAttributes.getNamedItem("width").getNodeValue() )); } catch (Exception ex){
            System.out.println("Invalid width for crosstab row group " + group.getName());
           }
        }

        if (nodeAttributes.getNamedItem("totalPosition") != null)
            group.setTotalPosition( nodeAttributes.getNamedItem("totalPosition").getNodeValue() );
        if (nodeAttributes.getNamedItem("headerPosition") != null)
            group.setHeaderPosition( nodeAttributes.getNamedItem("headerPosition").getNodeValue() );

        re.getRowGroups().add(group);

        NodeList list_child = rowGroupXmlNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child = (Node)list_child.item(ck);
            NamedNodeMap subNodeAttributes = child.getAttributes();
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bucket")) {

                readBucket(child, group);
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("crosstabRowHeader")) {

                it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                cell.setName(group.getName() + " header" );
                readCellContents(child, cell, re);
                group.setHeaderCell( cell );
                cell.setType( cell.HEADER_CELL );
                cell.setParent(re);
                re.getCells().add( cell );
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("crosstabTotalRowHeader")) {
                it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                cell.setName(group.getName() + " total header" );
                readCellContents(child, cell, re);
                group.setTotalCell( cell );
                cell.setType( cell.HEADER_CELL );
                cell.setParent(re);
                re.getCells().add( cell );
            }
        }

        if (group.getHeaderCell() == null)
        {
            it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
            cell.setName(group.getName() + " header" );
            cell.setType( cell.HEADER_CELL );
            cell.setParent(re);
            cell.setWidth( group.getWidth() );
            re.getCells().add( cell );
            group.setHeaderCell( cell );
         }

        if (group.getTotalCell() == null)
        {
            it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
            cell.setType( cell.HEADER_CELL );
            cell.setName(group.getName() + " total header" );
            cell.setParent(re);
            cell.setWidth( group.getWidth() );
            re.getCells().add( cell );
            group.setTotalCell( cell );
         }


    }

    private void readCrosstabColumnGroup(Node columnGroupXmlNode, CrosstabReportElement re)
    {
        CrosstabColumnGroup group = new CrosstabColumnGroup();

        NamedNodeMap nodeAttributes = columnGroupXmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("name") != null)
            group.setName( nodeAttributes.getNamedItem("name").getNodeValue() );

        if (nodeAttributes.getNamedItem("height") != null)
        {
           try {  group.setHeight( Integer.parseInt( nodeAttributes.getNamedItem("height").getNodeValue() )); } catch (Exception ex){
            System.out.println("Invalid height for crosstab column group " + group.getName());
           }
        }

        if (nodeAttributes.getNamedItem("totalPosition") != null)
            group.setTotalPosition( nodeAttributes.getNamedItem("totalPosition").getNodeValue() );
        if (nodeAttributes.getNamedItem("headerPosition") != null)
            group.setHeaderPosition( nodeAttributes.getNamedItem("headerPosition").getNodeValue() );

        re.getColumnGroups().add(group);

        NodeList list_child = columnGroupXmlNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child = (Node)list_child.item(ck);
            NamedNodeMap subNodeAttributes = child.getAttributes();
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bucket")) {

                readBucket(child, group);
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("crosstabColumnHeader")) {

                it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                cell.setName(group.getName() + " header" );
                readCellContents(child, cell, re);
                group.setHeaderCell( cell );
                cell.setType( cell.HEADER_CELL );
                cell.setParent(re);
                re.getCells().add( cell );
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("crosstabTotalColumnHeader")) {
                it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
                cell.setName(group.getName() + " total header" );
                readCellContents(child, cell, re);
                group.setTotalCell( cell );
                cell.setType( cell.HEADER_CELL );
                cell.setParent(re);
                re.getCells().add( cell );
            }

        }

        if (group.getHeaderCell() == null)
        {
            it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
            cell.setName(group.getName() + " header" );
            cell.setType( cell.HEADER_CELL );
            cell.setParent(re);
            cell.setHeight( group.getHeight() );
            re.getCells().add( cell );
            group.setHeaderCell( cell );
         }

        if (group.getTotalCell() == null)
        {
            it.businesslogic.ireport.crosstab.CrosstabCell cell = new it.businesslogic.ireport.crosstab.CrosstabCell();
            cell.setType( cell.HEADER_CELL );
            cell.setName(group.getName() + " total header" );
            cell.setParent(re);
            cell.setHeight( group.getHeight() );
            re.getCells().add( cell );
            group.setTotalCell( cell );
         }
    }

    private void readCellContents(Node cellConetensNode, it.businesslogic.ireport.crosstab.CrosstabCell cell, CrosstabReportElement re)
    {
        NamedNodeMap nodeAttributes = cellConetensNode.getAttributes();

        if (nodeAttributes.getNamedItem("width") != null)
        {
           try {  cell.setWidth( Integer.parseInt( nodeAttributes.getNamedItem("width").getNodeValue() )); } catch (Exception ex){
            System.out.println("Invalid width for crosstab cell");
           }
        }

        if (nodeAttributes.getNamedItem("height") != null)
        {
           try {  cell.setHeight( Integer.parseInt( nodeAttributes.getNamedItem("height").getNodeValue() )); } catch (Exception ex){
            System.out.println("Invalid height for crosstab cell");
           }
        }

        if (nodeAttributes.getNamedItem("rowTotalGroup") != null)
            cell.setRowTotalGroup(nodeAttributes.getNamedItem("rowTotalGroup").getNodeValue() );

        if (nodeAttributes.getNamedItem("columnTotalGroup") != null)
            cell.setColumnTotalGroup(nodeAttributes.getNamedItem("columnTotalGroup").getNodeValue() );


        // Look for the CellContents node...
        Node cellContentsElementNode = null;
        NodeList list_child = cellConetensNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child = (Node)list_child.item(ck);
            NamedNodeMap subNodeAttributes = child.getAttributes();
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("cellContents")) {
                cellContentsElementNode = child;

                if (subNodeAttributes.getNamedItem("style") != null)
                {
                   String sname = subNodeAttributes.getNamedItem("style").getNodeValue();
                   for (int j=0; j<getReport().getStyles().size(); ++j)
                   {
                        Style sparent = (Style)getReport().getStyles().elementAt(j);
                        if (sparent.getName().equals( sname))
                        {
                            cell.setStyle( sparent);
                            break;
                        }
                   }
                   if (cell.getStyle() == null)
                   {
                       UndefinedStyle us = new UndefinedStyle();
                       us.setName(sname);
                       getReport().getStyles().addElement(us);
                       cell.setStyle( us);
                   }
                }

                if (subNodeAttributes.getNamedItem("backcolor") != null)
                        cell.setBackcolor( getReport().decodeColor( subNodeAttributes.getNamedItem("backcolor").getNodeValue() ));

                if (subNodeAttributes.getNamedItem("mode") != null)
                    cell.setMode( subNodeAttributes.getNamedItem("mode").getNodeValue());
            }
        }

        if (cellContentsElementNode != null)
        {
            list_child = cellContentsElementNode.getChildNodes();
            for (int ck=0; ck< list_child.getLength(); ck++) {
                Node child = (Node)list_child.item(ck);
                NamedNodeMap subNodeAttributes = child.getAttributes();


                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("box")) {
                    readBoxElement(child, cell);
                }
            }

        }
        readBandElements("",cellContentsElementNode,null,null,re,cell);

    }

    private void readBucket(Node bucketXmlNode, CrosstabGroup grp)
    {
        NamedNodeMap nodeAttributes = bucketXmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("order") != null)
            grp.setBucketOrder( nodeAttributes.getNamedItem("order").getNodeValue() );

        NodeList list_child = bucketXmlNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child = (Node)list_child.item(ck);
            NamedNodeMap subNodeAttributes = child.getAttributes();
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bucketExpression")) {

                if (subNodeAttributes.getNamedItem("class") != null)
                    grp.setBucketExpressionClass( subNodeAttributes.getNamedItem("class").getNodeValue());

                grp.setBucketExpression(readPCDATA(child));
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("comparatorExpression")) {
                grp.setBucketComparatorExpression(readPCDATA(child));
            }
        }
    }


    private void readChartElement(Node xmlChart, ChartReportElement2 re) {
        NamedNodeMap nodeAttributes = xmlChart.getAttributes();

        if (nodeAttributes.getNamedItem("isShowLegend") != null)
            re.getChart().setShowLegend( nodeAttributes.getNamedItem("isShowLegend").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("evaluationTime") != null)
            re.setEvaluationTime( nodeAttributes.getNamedItem("evaluationTime").getNodeValue() );
        if (nodeAttributes.getNamedItem("evaluationGroup") != null)
            re.setEvaluationGroup( nodeAttributes.getNamedItem("evaluationGroup").getNodeValue() );
        //if (nodeAttributes.getNamedItem("hyperlinkType") != null)
        //    re.setHyperlinkType( ""+nodeAttributes.getNamedItem("hyperlinkType").getNodeValue() );
        //if (nodeAttributes.getNamedItem("hyperlinkTarget") != null)
        //    re.setHyperlinkTarget( ""+nodeAttributes.getNamedItem("hyperlinkTarget").getNodeValue() );
        //if (nodeAttributes.getNamedItem("bookmarkLevel") != null)
        //    re.setBookmarkLevel( Integer.parseInt( nodeAttributes.getNamedItem("bookmarkLevel").getNodeValue() ) );
        if (nodeAttributes.getNamedItem("customizerClass") != null)
            re.getChart().setCustomizerClass( nodeAttributes.getNamedItem("customizerClass").getNodeValue() );

        NodeList childsOfChild = xmlChart.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("printWhenExpression")) {
                re.setPrintWhenExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("reportElement")) {
                readXMLReportElement(child_child,  re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("box")) {
                readBoxElement(child_child,  re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("chartTitle")) {
                readChartTitleElement(child_child,  re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("chartSubtitle")) {
                readChartSubTitleElement(child_child,  re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("chartLegend")) {
                readChartLegendElement(child_child,  re);
            }
            /*
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("anchorNameExpression")) {
                re.setAnchorNameExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkAnchorExpression")) {
                re.setHyperlinkAnchorExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkPageExpression")) {
                re.setHyperlinkPageExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkReferenceExpression")) {
                re.setHyperlinkReferenceExpression( readPCDATA(child_child) );
            }
            */
        }
        readHyperlink(xmlChart, re);
    }

    private void readChartTitleElement(Node xmlElement, ChartReportElement2 re) {
        NamedNodeMap nodeAttributes = xmlElement.getAttributes();

        if (nodeAttributes.getNamedItem("position") != null)
            re.getChart().getTitle().setPosition( nodeAttributes.getNamedItem("position").getNodeValue() );
        if (nodeAttributes.getNamedItem("color") != null)
            re.getChart().getTitle().setColor( decodeColor( ""+nodeAttributes.getNamedItem("color").getNodeValue()) );

        NodeList childsOfChild = xmlElement.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("titleExpression")) {
                re.getChart().getTitle().setTitleExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("font")) {
                re.getChart().getTitle().setFont( readFontElement(child_child) );
            }
        }
    }

    private void readChartLegendElement(Node xmlElement, ChartReportElement2 re) {
        NamedNodeMap nodeAttributes = xmlElement.getAttributes();

        if (nodeAttributes.getNamedItem("textColor") != null)
            re.getChart().getLegend().setTextColor( decodeColor( ""+nodeAttributes.getNamedItem("textColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("backgroundColor") != null)
            re.getChart().getLegend().setBackgroundColor( decodeColor( ""+nodeAttributes.getNamedItem("backgroundColor").getNodeValue()) );


        NodeList childsOfChild = xmlElement.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("font")) {
                re.getChart().getLegend().setFont( readFontElement(child_child) );
            }
        }
    }

     private IReportFont readFontElement(Node xmlElement) {

            NamedNodeMap nnm = xmlElement.getAttributes();
            IReportFont font = null;

            if ( nnm.getNamedItem("reportFont") != null) {

                font = getReport().getReportFontByName( nnm.getNamedItem("reportFont").getNodeValue() );
                if (font != null)
                {
                    font = (IReportFont) font.clone();
                    font.setReportFont( nnm.getNamedItem("reportFont").getNodeValue());
                }
            }

            if (font == null)
            {
                if ( getReport().getDefaultFont() != null)
                {
                    font = (IReportFont)getReport().getDefaultFont().clone();
                }
                else
                {
                    font = new IReportFont();
                }
            }

            //System.out.println("Working on: " + xmlReportElement);
            for (int kkk=0; kkk<nnm.getLength(); ++kkk)
            {
                //System.out.println( + " " + );

                String propName = nnm.item(kkk).getNodeName();
                String propValue = nnm.item(kkk).getNodeValue();
                if (propName != null && propValue != null)
                {
                    if (propName.equals("fontName"))
                        font.setFontName(propValue);
                    else if (propName.equals("pdfFontName"))
                        font.setPDFFontName(propValue);
                    else if (propName.equals("size"))
                        font.setFontSize( Integer.parseInt(""+propValue) );
                    else if (propName.equals("isBold"))
                        font.setBold( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isItalic"))
                        font.setItalic( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isUnderline"))
                        font.setUnderline( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isStrikeThrough"))
                        font.setStrikeTrought( (new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("isPdfEmbedded"))
                        font.setPdfEmbedded((new String(""+propValue)).equalsIgnoreCase("true") );
                    else if (propName.equals("pdfEncoding"))
                        font.setPdfEncoding( ""+propValue );
                }
            }


            return font;
     }

    private void readDataset(Node xmlNode, ChartReportElement2 re) {

        readDataset(xmlNode, re.getChart().getDataset());
    }


    private void readDataset(Node xmlNode, Dataset dataset) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("resetType") != null)
            dataset.setResetType( nodeAttributes.getNamedItem("resetType").getNodeValue() );
        if (nodeAttributes.getNamedItem("resetGroup") != null)
            dataset.setResetGroup( nodeAttributes.getNamedItem("resetGroup").getNodeValue() );
        if (nodeAttributes.getNamedItem("incrementType") != null)
            dataset.setIncrementType( nodeAttributes.getNamedItem("incrementType").getNodeValue() );
        if (nodeAttributes.getNamedItem("incrementGroup") != null)
            dataset.setIncrementGroup( nodeAttributes.getNamedItem("incrementGroup").getNodeValue() );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);

            NamedNodeMap subNodeAttributes = child_child.getAttributes();
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("incrementWhenExpression")) {

                dataset.setIncrementWhenExpression( readPCDATA(child_child) );
            }
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("datasetRun")) {

                if (subNodeAttributes.getNamedItem("subDataset") != null)
                {
                        String name = subNodeAttributes.getNamedItem("subDataset").getNodeValue();
                        Enumeration enum_datasets = getReport().getSubDatasets().elements();
                        while (enum_datasets.hasMoreElements())
                        {
                            SubDataset subDataset = (SubDataset)enum_datasets.nextElement();
                            if  ( subDataset.getName().equals(name))
                            {
                                dataset.setSubDataset( subDataset );
                                break;
                            }
                        }

                        NodeList childsOfchild_child = child_child.getChildNodes();
                        for (int c_count2=0; c_count2< childsOfchild_child.getLength(); c_count2++) {
                            Node child_child2 = (Node)childsOfchild_child.item(c_count2);

                            NamedNodeMap subNodeAttributes2 = child_child2.getAttributes();

                            if (child_child2.getNodeType() == Node.ELEMENT_NODE && child_child2.getNodeName().equals("parametersMapExpression")) {
                                dataset.setParametersMapExpression( readPCDATA(child_child2) );
                            }
                            else if (child_child2.getNodeType() == Node.ELEMENT_NODE && child_child2.getNodeName().equals("datasetParameter")) {
                                name = "";
                                if (subNodeAttributes2.getNamedItem("name") != null) {
                                    name = ""+subNodeAttributes2.getNamedItem("name").getNodeValue();
                                }
                                // Find expression in childs......
                                String expression = "";
                                NodeList childsOfChildOfChild = child_child2.getChildNodes();
                                for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                                    Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                                    if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("datasetParameterExpression")) {
                                        expression = readPCDATA(child_child_child);
                                        break;
                                    }
                                }
                                dataset.getSubreportParameters().addElement( new it.businesslogic.ireport.JRSubreportParameter( name, expression));
                            }
                            else if (child_child2.getNodeType() == Node.ELEMENT_NODE && child_child2.getNodeName().equals("connectionExpression")) {
                                dataset.setConnectionExpression( readPCDATA(child_child2));
                                dataset.setUseConnection(true);
                            }
                            else if (child_child2.getNodeType() == Node.ELEMENT_NODE && child_child2.getNodeName().equals("dataSourceExpression")) {
                                dataset.setDataSourceExpression(readPCDATA(child_child2));
                                dataset.setUseConnection(false);
                            }
                        }
                }
            }
        }
    }

    private void readSectionItemHyperlink(Node elementNode, SectionItemHyperlink sih) {

        NodeList childsOfChild = elementNode.getChildNodes();

        NamedNodeMap nodeAttributes = elementNode.getAttributes();
        if (nodeAttributes.getNamedItem("hyperlinkType") != null)
            sih.setHyperlinkType( ""+nodeAttributes.getNamedItem("hyperlinkType").getNodeValue() );
        if (nodeAttributes.getNamedItem("hyperlinkTarget") != null)
            sih.setHyperlinkTarget( ""+nodeAttributes.getNamedItem("hyperlinkTarget").getNodeValue() );

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            NamedNodeMap subNodeAttributes = child_child.getAttributes();
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkParameter")) {
                String name = "";
                if (subNodeAttributes.getNamedItem("name") != null) {
                    name = ""+subNodeAttributes.getNamedItem("name").getNodeValue();
                }
                // Find expression in childs......
                String expression = "";
                NodeList childsOfChildOfChild = child_child.getChildNodes();
                for (int c_count_2=0; c_count_2< childsOfChildOfChild.getLength(); c_count_2++) {
                    Node child_child_child = (Node)childsOfChildOfChild.item(c_count_2);
                    if (child_child_child.getNodeType() == Node.ELEMENT_NODE && child_child_child.getNodeName().equals("hyperlinkParameterExpression")) {
                        expression = readPCDATA(child_child_child);
                        break;
                    }
                }
                sih.getHyperlinkParameters().add( new it.businesslogic.ireport.JRLinkParameter( name, expression));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkAnchorExpression")) {
                sih.setHyperlinkAnchorExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkPageExpression")) {
                sih.setHyperlinkPageExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkReferenceExpression")) {
                sih.setHyperlinkReferenceExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("hyperlinkTooltipExpression")) {
                sih.setHyperlinkTooltipExpression( readPCDATA(child_child) );
            }
        }

    }

    private void readPieDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("keyExpression")) {
                 ((it.businesslogic.ireport.chart.PieDataset)re.getChart().getDataset()).setKeyExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueExpression")) {
                 ((it.businesslogic.ireport.chart.PieDataset)re.getChart().getDataset()).setValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelExpression")) {
                 ((it.businesslogic.ireport.chart.PieDataset)re.getChart().getDataset()).setLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("sectionHyperlink")) {
                 readSectionItemHyperlink(child_child, ((PieDataset)re.getChart().getDataset()).getSectionHyperLink()  );
            }
        }
    }



    private void readCategoryDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        CategoryDataset cd = new CategoryDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categorySeries")) {
                CategorySeries cs = readCategorySeries(child_child);
                cd.getCategorySeries().add(cs);
            }
        }
    }

    private void readTimePeriodDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        TimePeriodDataset cd = new TimePeriodDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timePeriodSeries")) {
                TimePeriodSeries cs = readTimePeriodSeries(child_child);
                cd.getTimePeriodSeries().add(cs);
            }
        }
    }

    private void readXYDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        XYDataset cd = new XYDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xySeries")) {
                XYSeries cs = readXYSeries(child_child);
                cd.getXYSeries().add(cs);
            }
        }
    }

    private void readXYZDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        XYZDataset cd = new XYZDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xyzSeries")) {
                XYZSeries cs = readXYZSeries(child_child);
                cd.getXYZSeries().add(cs);
            }
        }
    }

    private void readTimeSeriesDataset(Node xmlNode, ChartReportElement2 re) {

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();
        NodeList childsOfChild = xmlNode.getChildNodes();

        TimeSeriesDataset cd = new TimeSeriesDataset();
        re.getChart().setDataset(cd);

        if (nodeAttributes.getNamedItem("timePeriod") != null)
            cd.setTimePeriod( nodeAttributes.getNamedItem("timePeriod").getNodeValue() );

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeSeries")) {
                TimeSeries cs = readTimeSeries(child_child);
                cd.getTimeSeries().add(cs);
            }
        }
    }

    private void readHighLowDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        HighLowDataset cd = new HighLowDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                 cd.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dateExpression")) {
                 cd.setDateExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("highExpression")) {
                 cd.setHighExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("lowExpression")) {
                 cd.setLowExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("openExpression")) {
                 cd.setOpenExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("closeExpression")) {
                 cd.setCloseExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("volumeExpression")) {
                 cd.setVolumeExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cd.getItemHyperLink());
            }

        }
    }

    private void readValueDataset(Node xmlNode, ChartReportElement2 re) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        ValueDataset cd = new ValueDataset();
        re.getChart().setDataset(cd);

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataset")) {
                readDataset(child_child, re);
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueExpression")) {
                 cd.setValueExpression( readPCDATA(child_child) );
            }


        }
    }

    private CategorySeries readCategorySeries(Node xmlNode) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        CategorySeries cs = new CategorySeries();

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                cs.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryExpression")) {
                cs.setCategoryExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueExpression")) {
                cs.setValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelExpression")) {
                cs.setLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cs.getSectionItemHyperlink() );
            }
        }

        return cs;
    }

    private TimePeriodSeries readTimePeriodSeries(Node xmlNode) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        TimePeriodSeries cs = new TimePeriodSeries();

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                cs.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("startDateExpression")) {
                cs.setStartDateExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("endDateExpression")) {
                cs.setEndDateExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueExpression")) {
                cs.setValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelExpression")) {
                cs.setLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cs.getSectionItemHyperlink() );
            }
        }

        return cs;
    }

    private TimeSeries readTimeSeries(Node xmlNode) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        TimeSeries cs = new TimeSeries();

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                cs.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timePeriodExpression")) {
                cs.setTimePeriodExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueExpression")) {
                cs.setValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelExpression")) {
                cs.setLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cs.getSectionItemHyperlink() );
            }
        }

        return cs;
    }

    private XYSeries readXYSeries(Node xmlNode) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        XYSeries cs = new XYSeries();

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                cs.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xValueExpression")) {
                cs.setXValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yValueExpression")) {
                cs.setYValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelExpression")) {
                cs.setLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cs.getSectionItemHyperlink() );
            }
        }

        return cs;
    }

    private XYZSeries readXYZSeries(Node xmlNode) {

        NodeList childsOfChild = xmlNode.getChildNodes();

        XYZSeries cs = new XYZSeries();

        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("seriesExpression")) {
                cs.setSeriesExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xValueExpression")) {
                cs.setXValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yValueExpression")) {
                cs.setYValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("zValueExpression")) {
                cs.setZValueExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("itemHyperlink")) {
                 readSectionItemHyperlink(child_child, cs.getSectionItemHyperlink() );
            }
        }

        return cs;
    }

    private void readPiePlot(Node xmlNode, PiePlot plot) {
        
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();
        plot.setCircular( nodeAttributes.getNamedItem("isCircular").getNodeValue().equals("true") );
        
        readPlot(xmlNode, plot);
    }

    private void readPie3DPlot(Node xmlNode, Pie3DPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("depthFactor") != null)
            plot.setDepthFactor( Double.parseDouble( nodeAttributes.getNamedItem("depthFactor").getNodeValue() ) );

        if (nodeAttributes.getNamedItem("isCircular") != null)
            plot.setCircular( nodeAttributes.getNamedItem("isCircular").getNodeValue().equals("true") );

        
        readPlot(xmlNode, plot);
    }

    private void readBarPlot(Node xmlNode, BarPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowLabels") != null)
                plot.setShowLabels( nodeAttributes.getNamedItem("isShowLabels").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowTickMarks") != null)
                plot.setShowTickMarks( nodeAttributes.getNamedItem("isShowTickMarks").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowTickLabels") != null)
                plot.setShowTickLabels( nodeAttributes.getNamedItem("isShowTickLabels").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisLabelExpression")) {
                plot.setCategoryAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisFormat")) {
                plot.setCategoryAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readBar3DPlot(Node xmlNode, Bar3DPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowLabels") != null)
                plot.setShowLabels( nodeAttributes.getNamedItem("isShowLabels").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("xOffset") != null)
                plot.setXOffset( Double.parseDouble( nodeAttributes.getNamedItem("xOffset").getNodeValue() ) );
        if (nodeAttributes.getNamedItem("yOffset") != null)
                plot.setYOffset( Double.parseDouble( nodeAttributes.getNamedItem("yOffset").getNodeValue() ) );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisLabelExpression")) {
                plot.setCategoryAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisFormat")) {
                plot.setCategoryAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readLinePlot(Node xmlNode, LinePlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowLines") != null)
                plot.setShowLines( nodeAttributes.getNamedItem("isShowLines").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowShapes") != null)
                plot.setShowShapes( nodeAttributes.getNamedItem("isShowShapes").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisLabelExpression")) {
                plot.setCategoryAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisFormat")) {
                plot.setCategoryAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }


    private AxisFormat readAxisFormat(Node xmlNode) {

        AxisFormat axisFormat = new AxisFormat();

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("labelColor") != null)
                axisFormat.setLabelColor( decodeColor( ""+nodeAttributes.getNamedItem("labelColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("tickLabelColor") != null)
                axisFormat.setTickLabelColor( decodeColor( ""+nodeAttributes.getNamedItem("tickLabelColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("axisLineColor") != null)
                axisFormat.setAxisLineColor( decodeColor( ""+nodeAttributes.getNamedItem("axisLineColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("tickLabelMask") != null)
                axisFormat.setTickLabelMask( ""+nodeAttributes.getNamedItem("tickLabelMask").getNodeValue() );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("labelFont")) {
                Node fontNode = findChild(child_child, "font");
                if (fontNode != null)
                axisFormat.setLabelFont( readFontElement(fontNode) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("tickLabelFont")) {
                Node fontNode = findChild(child_child, "font");
                if (fontNode != null)
                axisFormat.setTickLabelFont( readFontElement(fontNode) );
            }
        }

        return axisFormat;
    }

    private void readAreaPlot(Node xmlNode, AreaPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisLabelExpression")) {
                plot.setCategoryAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryAxisFormat")) {
                plot.setCategoryAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readScatterPlot(Node xmlNode, ScatterPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowLines") != null)
                plot.setShowLines( nodeAttributes.getNamedItem("isShowLines").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowShapes") != null)
                plot.setShowShapes( nodeAttributes.getNamedItem("isShowShapes").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xAxisLabelExpression")) {
                plot.setXAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yAxisLabelExpression")) {
                plot.setYAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xAxisFormat")) {
                plot.setXAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yAxisFormat")) {
                plot.setYAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readBubblePlot(Node xmlNode, BubblePlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("scaleType") != null)
                plot.setScaleType( nodeAttributes.getNamedItem("scaleType").getNodeValue() );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xAxisLabelExpression")) {
                plot.setXAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yAxisLabelExpression")) {
                plot.setYAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xAxisFormat")) {
                plot.setXAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("yAxisFormat")) {
                plot.setYAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readTimeSeriesPlot(Node xmlNode, TimeSeriesPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowLines") != null)
                plot.setShowLines( nodeAttributes.getNamedItem("isShowLines").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowShapes") != null)
                plot.setShowShapes( nodeAttributes.getNamedItem("isShowShapes").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisLabelExpression")) {
                plot.setTimeAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisFormat")) {
                plot.setTimeAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readHighLowPlot(Node xmlNode, HighLowPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowCloseTicks") != null)
                plot.setShowCloseTicks( nodeAttributes.getNamedItem("isShowCloseTicks").getNodeValue().equals("true") );
        if (nodeAttributes.getNamedItem("isShowOpenTicks") != null)
                plot.setShowOpenTicks( nodeAttributes.getNamedItem("isShowOpenTicks").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisLabelExpression")) {
                plot.setTimeAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisFormat")) {
                plot.setTimeAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readCandlestickPlot(Node xmlNode, CandlestickPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("isShowVolume") != null)
                plot.setShowVolume( nodeAttributes.getNamedItem("isShowVolume").getNodeValue().equals("true") );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisLabelExpression")) {
                plot.setTimeAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisLabelExpression")) {
                plot.setValueAxisLabelExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeAxisFormat")) {
                plot.setTimeAxisFormat(readAxisFormat( findChild(child_child, "axisFormat") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueAxisFormat")) {
                plot.setValueAxisFormat( readAxisFormat(findChild(child_child, "axisFormat") ));
            }
        }
    }

    private void readMeterPlot(Node xmlNode, MeterPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("shape") != null)
                plot.setShape( nodeAttributes.getNamedItem("shape").getNodeValue() );

        if (nodeAttributes.getNamedItem("angle") != null)
        {
            try {
                            plot.setAngle( Integer.parseInt(nodeAttributes.getNamedItem("angle").getNodeValue()+"") );
            } catch (Exception ex)
            {
                it.businesslogic.ireport.gui.MainFrame.getMainInstance().logOnConsole( 
                        I18n.getString("reportReader.notValidAngle","Not valid angle value for the MeterPlot tag.") );
            }
        }
        if (nodeAttributes.getNamedItem("units") != null)
                plot.setUnits( nodeAttributes.getNamedItem("units").getNodeValue() );

        if (nodeAttributes.getNamedItem("tickInterval") != null)
        {
            try {
                            plot.setTickInterval( Double.parseDouble(nodeAttributes.getNamedItem("tickInterval").getNodeValue()+"") );
            } catch (Exception ex)
            {
                it.businesslogic.ireport.gui.MainFrame.getMainInstance().logOnConsole(
                        I18n.getString("reportReader.notValidTickInterval","Not valid tickInterval value for the MeterPlot tag.")
                        );
            }
        }

        if (nodeAttributes.getNamedItem("meterColor") != null)
                plot.setMeterColor( decodeColor( ""+nodeAttributes.getNamedItem("meterColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("needleColor") != null)
                plot.setNeedleColor( decodeColor( ""+nodeAttributes.getNamedItem("needleColor").getNodeValue()) );

        if (nodeAttributes.getNamedItem("tickColor") != null)
                plot.setTickColor( decodeColor( ""+nodeAttributes.getNamedItem("tickColor").getNodeValue()) );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueDisplay")) {
                plot.setValueDisplay( readValueDisplay(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataRange")) {
                plot.setDataRange( readDataRange(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("meterInterval")) {
                plot.getMeterIntervals().add(readMeterInterval(child_child));
            }

        }
    }


    private void readThermometerPlot(Node xmlNode, ThermometerPlot plot) {
        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);

        if (nodeAttributes.getNamedItem("valueLocation") != null)
                plot.setValueLocation( nodeAttributes.getNamedItem("valueLocation").getNodeValue() );

        if (nodeAttributes.getNamedItem("isShowValueLines") != null)
                plot.setShowValueLines( nodeAttributes.getNamedItem("isShowValueLines").getNodeValue().equals("true") );

        if (nodeAttributes.getNamedItem("mercuryColor") != null)
                plot.setMercuryColor( decodeColor( ""+nodeAttributes.getNamedItem("mercuryColor").getNodeValue()) );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueDisplay")) {
                plot.setValueDisplay( readValueDisplay(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataRange")) {
                plot.setDataRange( readDataRange(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("lowRange")) {
                plot.setLowRange(  readDataRange( findChild(child_child, "dataRange") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("mediumRange")) {
                plot.setMediumRange(  readDataRange( findChild(child_child, "dataRange") ));
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("highRange")) {
                plot.setHighRange(  readDataRange( findChild(child_child, "dataRange") ));
            }

        }
    }

    private ValueDisplay readValueDisplay(Node xmlNode) {

        ValueDisplay valueDisplay = new ValueDisplay();

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("color") != null)
                valueDisplay.setColor( decodeColor( ""+nodeAttributes.getNamedItem("color").getNodeValue()) );

        if (nodeAttributes.getNamedItem("mask") != null)
                valueDisplay.setMask( ""+nodeAttributes.getNamedItem("mask").getNodeValue() );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("font")) {
                valueDisplay.setFont( readFontElement(child_child) );
            }
        }

        return valueDisplay;
    }

    private DataRange readDataRange(Node xmlNode) {

        DataRange dataRange = new DataRange();

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("lowExpression")) {
                dataRange.setLowExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("highExpression")) {
                dataRange.setHighExpression( readPCDATA(child_child) );
            }
        }

        return dataRange;
    }

    private MeterInterval readMeterInterval(Node xmlNode) {

        MeterInterval meterInterval = new MeterInterval();

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("label") != null)
                meterInterval.setLabel( ""+nodeAttributes.getNamedItem("label").getNodeValue() );

        if (nodeAttributes.getNamedItem("color") != null)
                meterInterval.setColor( decodeColor( ""+nodeAttributes.getNamedItem("color").getNodeValue()) );


        if (nodeAttributes.getNamedItem("alpha") != null)
        {
            try {
                            meterInterval.setAlpha( Double.parseDouble(nodeAttributes.getNamedItem("alpha").getNodeValue()+"") );
            } catch (Exception ex)
            {
                it.businesslogic.ireport.gui.MainFrame.getMainInstance().logOnConsole( 
                        I18n.getString("reportReader.notValidApha","Not valid alpha value for the MeterPlot -> MeterInterval tag.")
                        );
            }
        }


        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("dataRange")) {
                meterInterval.setDataRange( readDataRange(child_child) );
            }
        }

        return meterInterval;
    }

    private void readPlot(Node xmlNodeParent, Plot plot) {
        NodeList childsOfChild = xmlNodeParent.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node xmlNode = (Node)childsOfChild.item(c_count);
            if (xmlNode.getNodeType() == Node.ELEMENT_NODE && xmlNode.getNodeName().equals("plot")) {

                NamedNodeMap nodeAttributes = xmlNode.getAttributes();

                if (nodeAttributes.getNamedItem("backcolor") != null)
                    plot.setBackcolor( decodeColor( ""+nodeAttributes.getNamedItem("backcolor").getNodeValue()) );

                if (nodeAttributes.getNamedItem("orientation") != null)
                    plot.setOrientation(""+nodeAttributes.getNamedItem("orientation").getNodeValue());

                if (nodeAttributes.getNamedItem("backgroundAlpha") != null)
                    plot.setBackgroundAlpha( Double.parseDouble(nodeAttributes.getNamedItem("backgroundAlpha").getNodeValue()) );

                if (nodeAttributes.getNamedItem("foregroundAlpha") != null)
                    plot.setForegroundAlpha( Double.parseDouble(nodeAttributes.getNamedItem("foregroundAlpha").getNodeValue()) );

                if (nodeAttributes.getNamedItem("labelRotation") != null)
                    plot.setLabelRotation( Double.parseDouble(nodeAttributes.getNamedItem("labelRotation").getNodeValue()) );


                NodeList childsOfChildPlot = xmlNode.getChildNodes();
                for (int c_plot_count=0; c_plot_count< childsOfChildPlot.getLength(); c_plot_count++) {
                    Node child_child_plot = (Node)childsOfChildPlot.item(c_plot_count);
                    if (child_child_plot.getNodeType() == Node.ELEMENT_NODE && child_child_plot.getNodeName().equals("seriesColor")) {

                        NamedNodeMap nodeAttributes2 = child_child_plot.getAttributes();

                        java.awt.Color c = java.awt.Color.red;
                        int seriesOrder = 0;

                        if (nodeAttributes2.getNamedItem("color") != null)
                            c = decodeColor( ""+nodeAttributes2.getNamedItem("color").getNodeValue() );

                        if (nodeAttributes2.getNamedItem("seriesOrder") != null)
                        {
                            try {
                            seriesOrder = Integer.parseInt(""+nodeAttributes2.getNamedItem("seriesOrder").getNodeValue() );
                            } catch (Exception ex)
                            {
                            }
                        }

                        SeriesColor sc = new SeriesColor();
                        sc.setSeriesOrder(seriesOrder);
                        sc.setColor( c );

                        plot.getSeriesColors().add(sc);
                    }
              }
              // Sort seriescolor....
              Object[] objs =  plot.getSeriesColors().toArray();
              java.util.Arrays.sort( objs );
              plot.getSeriesColors().clear();
              for (int i=0; i<objs.length; ++i)
              {
                  plot.getSeriesColors().add( objs[i] );
              }
           }
        }
    }

    private void readChartSubTitleElement(Node xmlElement, ChartReportElement2 re) {
        NamedNodeMap nodeAttributes = xmlElement.getAttributes();

        if (nodeAttributes.getNamedItem("color") != null)
            re.getChart().getSubTitle().setColor( decodeColor( ""+nodeAttributes.getNamedItem("color").getNodeValue()) );

        NodeList childsOfChild = xmlElement.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("subtitleExpression")) {
                re.getChart().getSubTitle().setTitleExpression( readPCDATA(child_child) );
            }
            else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("font")) {
                 re.getChart().getSubTitle().setFont( readFontElement(child_child) );
            }
        }
    }

    private void readXMLReportElement(Node xmlReportElement, ReportElement re) {
        NamedNodeMap nodeAttributes = xmlReportElement.getAttributes();

        if (nodeAttributes.getNamedItem("style") != null)
        {
           String sname = nodeAttributes.getNamedItem("style").getNodeValue();
           for (int j=0; j<getReport().getStyles().size(); ++j)
           {
                Style sparent = (Style)getReport().getStyles().elementAt(j);
                if (sparent.getName().equals( sname))
                {
                    re.setStyle( sparent);
                    break;
                }
           }
           if (re.getStyle() == null)
           {
               UndefinedStyle us = new UndefinedStyle();
               us.setName(sname);
               getReport().getStyles().addElement(us);
               re.setStyle( us);
           }
        }
        //if ( bandAttributes.getNamedItem("height") != null) b.setHeight( Integer.parseInt(bandAttributes.getNamedItem("height").getNodeValue()) );
        if (nodeAttributes.getNamedItem("x") != null)
        {
            re.getPosition().x = Integer.parseInt(""+nodeAttributes.getNamedItem("x").getNodeValue());
            re.getRelativePosition().x = re.getPosition().x;
        }
        if (nodeAttributes.getNamedItem("y") != null)
        {
            re.getPosition().y = Integer.parseInt(""+nodeAttributes.getNamedItem("y").getNodeValue());
            re.getRelativePosition().y = re.getPosition().y;
        }
        if (nodeAttributes.getNamedItem("width") != null)
            re.setWidth( Integer.parseInt(""+nodeAttributes.getNamedItem("width").getNodeValue()) );
        if (nodeAttributes.getNamedItem("height") != null)
            re.setHeight( Integer.parseInt(""+nodeAttributes.getNamedItem("height").getNodeValue()) );
        if (nodeAttributes.getNamedItem("key") != null)
            re.setKey( ""+nodeAttributes.getNamedItem("key").getNodeValue() );

        if (nodeAttributes.getNamedItem("stretchType") != null)
            re.setStretchType( ""+nodeAttributes.getNamedItem("stretchType").getNodeValue() );

        if (nodeAttributes.getNamedItem("isPrintRepeatedValues") != null)
            re.setIsPrintRepeatedValues( (""+nodeAttributes.getNamedItem("isPrintRepeatedValues").getNodeValue()).equalsIgnoreCase("true") );

        if (nodeAttributes.getNamedItem("positionType") != null)
            re.setPositionType( ""+nodeAttributes.getNamedItem("positionType").getNodeValue() );
        else
            re.setPositionType( "FixRelativeToTop" );

        if (nodeAttributes.getNamedItem("isPrintRepeatedValues") != null)
            re.setIsPrintRepeatedValues( (""+nodeAttributes.getNamedItem("isPrintRepeatedValues").getNodeValue()).equalsIgnoreCase("true") );

        if (nodeAttributes.getNamedItem("isRemoveLineWhenBlank") != null)
        {
            re.setIsRemoveLineWhenBlank( (""+nodeAttributes.getNamedItem("isRemoveLineWhenBlank").getNodeValue()).equalsIgnoreCase("true") ) ;
        }

        if (nodeAttributes.getNamedItem("isPrintInFirstWholeBand") != null)
            re.setIsPrintInFirstWholeBand( (""+nodeAttributes.getNamedItem("isPrintInFirstWholeBand").getNodeValue()).equalsIgnoreCase("true") );

        if (nodeAttributes.getNamedItem("isPrintWhenDetailOverflows") != null)
            re.setIsPrintWhenDetailOverflows( (""+nodeAttributes.getNamedItem("isPrintWhenDetailOverflows").getNodeValue()).equalsIgnoreCase("true") );

        if (nodeAttributes.getNamedItem("printWhenGroupChanges") != null)
            re.setPrintWhenGroupChanges( ""+nodeAttributes.getNamedItem("printWhenGroupChanges").getNodeValue() );


        if (nodeAttributes.getNamedItem("forecolor") != null) {
            String color = ""+nodeAttributes.getNamedItem("forecolor").getNodeValue();
            re.setFgcolor(decodeColor(color) );
        }
        if (nodeAttributes.getNamedItem("backcolor") != null) {
            String color = ""+nodeAttributes.getNamedItem("backcolor").getNodeValue();
            re.setBgcolor(decodeColor(color) );

        }
        if (nodeAttributes.getNamedItem("mode") != null) {
            re.setTransparent(""+nodeAttributes.getNamedItem("mode").getNodeValue());
        }

        NodeList childsOfChild = xmlReportElement.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("printWhenExpression")) {
                re.setPrintWhenExpression( readPCDATA(child_child) );
            }
        }
    }

     private void readBoxElement(Node xmlReportElement, BoxElement re) {
        NamedNodeMap nodeAttributes = xmlReportElement.getAttributes();

        Box box = new Box();
        // ------- defaults --------
        if (nodeAttributes.getNamedItem("border") != null)
        {
            box.setBorder(""+nodeAttributes.getNamedItem("border").getNodeValue() );
            box.setLeftBorder(""+nodeAttributes.getNamedItem("border").getNodeValue() );
            box.setRightBorder(""+nodeAttributes.getNamedItem("border").getNodeValue() );
            box.setTopBorder(""+nodeAttributes.getNamedItem("border").getNodeValue() );
            box.setBottomBorder(""+nodeAttributes.getNamedItem("border").getNodeValue() );
        }

        if (nodeAttributes.getNamedItem("padding") != null)
        {
            box.setPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("padding").getNodeValue() ));
            box.setLeftPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("padding").getNodeValue() ));
            box.setRightPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("padding").getNodeValue() ));
            box.setTopPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("padding").getNodeValue() ));
            box.setBottomPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("padding").getNodeValue() ));
        }

        if (nodeAttributes.getNamedItem("borderColor") != null)
        {
            String color = ""+nodeAttributes.getNamedItem("borderColor").getNodeValue();
            java.awt.Color colorObj = decodeColor(color);

            box.setBorderColor(new java.awt.Color(colorObj.getRGB()));
            box.setLeftBorderColor(new java.awt.Color(colorObj.getRGB()));
            box.setRightBorderColor(new java.awt.Color(colorObj.getRGB()));
            box.setTopBorderColor(new java.awt.Color(colorObj.getRGB()));
            box.setBottomBorderColor(new java.awt.Color(colorObj.getRGB()));

        }
        // ------- top --------
        if (nodeAttributes.getNamedItem("topBorder") != null)
        {
            box.setTopBorder(""+nodeAttributes.getNamedItem("topBorder").getNodeValue() );
        }

         if (nodeAttributes.getNamedItem("topPadding") != null)
        {
            box.setTopPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("topPadding").getNodeValue() ));
        }

        if (nodeAttributes.getNamedItem("topBorderColor") != null)
        {
            String color = ""+nodeAttributes.getNamedItem("topBorderColor").getNodeValue();
            box.setTopBorderColor(decodeColor(color));
        }

        // ------- left --------
        if (nodeAttributes.getNamedItem("leftBorder") != null)
        {
            box.setLeftBorder(""+nodeAttributes.getNamedItem("leftBorder").getNodeValue() );
        }

         if (nodeAttributes.getNamedItem("leftPadding") != null)
        {
            box.setLeftPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("leftPadding").getNodeValue() ));
        }

        if (nodeAttributes.getNamedItem("leftBorderColor") != null)
        {
            String color = ""+nodeAttributes.getNamedItem("leftBorderColor").getNodeValue();
            box.setLeftBorderColor(decodeColor(color));
        }

        // ------- right --------
        if (nodeAttributes.getNamedItem("rightBorder") != null)
        {
            box.setRightBorder(""+nodeAttributes.getNamedItem("rightBorder").getNodeValue() );
        }

         if (nodeAttributes.getNamedItem("rightPadding") != null)
        {
            box.setRightPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("rightPadding").getNodeValue() ));
        }

        if (nodeAttributes.getNamedItem("rightBorderColor") != null)
        {
            String color = ""+nodeAttributes.getNamedItem("rightBorderColor").getNodeValue();
            box.setRightBorderColor(decodeColor(color));
        }

        // ------- bottom --------
        if (nodeAttributes.getNamedItem("bottomBorder") != null)
        {
            box.setBottomBorder(""+nodeAttributes.getNamedItem("bottomBorder").getNodeValue() );
        }

         if (nodeAttributes.getNamedItem("bottomPadding") != null)
        {
            box.setBottomPadding(Integer.parseInt(""+nodeAttributes.getNamedItem("bottomPadding").getNodeValue() ));
        }

        if (nodeAttributes.getNamedItem("bottomBorderColor") != null)
        {
            String color = ""+nodeAttributes.getNamedItem("bottomBorderColor").getNodeValue();
            box.setBottomBorderColor(decodeColor(color));
        }

        if (re instanceof BoxElement) ((BoxElement)re).setBox(box);

    }




    static java.awt.Color decodeColor(String colorString)
    {
        java.awt.Color color = null;
        char firstChar = colorString.charAt(0);
        if (firstChar == '#')
        {
               color = new java.awt.Color(Integer.parseInt(colorString.substring(1), 16));
        }
        else if ('0' <= firstChar && firstChar <= '9')
        {
               color = new java.awt.Color(Integer.parseInt(colorString));
        }
        else
        {
                if (net.sf.jasperreports.engine.xml.JRXmlConstants.getColorMap().containsKey(colorString))
                {
                        color = (java.awt.Color)net.sf.jasperreports.engine.xml.JRXmlConstants.getColorMap().get(colorString);
                }
                else
                {
                        color = java.awt.Color.black;
                }
        }
        return color;

    }


    static public String readPCDATA(Node textNode) {
        return readPCDATA(textNode,true);
    }

    static public String readPCDATA(Node textNode, boolean trim) {
        NodeList list_child = textNode.getChildNodes();
        for (int ck=0; ck< list_child.getLength(); ck++) {
            Node child_child = (Node)list_child.item(ck);

            // --- start solution: if there is another node this should be the PCDATA-node
            Node ns = child_child.getNextSibling();
            if (ns != null)
            child_child = ns;
            // --- end solution

            final short nt = child_child.getNodeType();
            if ((nt == Node.CDATA_SECTION_NODE) || (nt == Node.TEXT_NODE)) {
               if (trim) return ((String)child_child.getNodeValue()).trim();
                return (String)child_child.getNodeValue();
            }
        }
        return "";
    }


    private void readXMLTextElement(Node xmlReportElement, TextReportElement re) {
        NamedNodeMap nodeAttributes = xmlReportElement.getAttributes();
        if (nodeAttributes.getNamedItem("textAlignment") != null)
            re.setAlign(""+nodeAttributes.getNamedItem("textAlignment").getNodeValue());

        if (nodeAttributes.getNamedItem("verticalAlignment") != null)
            re.setVerticalAlign(""+nodeAttributes.getNamedItem("verticalAlignment").getNodeValue());

        if (nodeAttributes.getNamedItem("lineSpacing") != null)
            re.setLineSpacing( ""+nodeAttributes.getNamedItem("lineSpacing").getNodeValue());

        if (nodeAttributes.getNamedItem("rotation") != null)
            re.setRotate(""+nodeAttributes.getNamedItem("rotation").getNodeValue());

        if (nodeAttributes.getNamedItem("isStyledText") != null)
            re.setIsStyledText( (""+nodeAttributes.getNamedItem("isStyledText").getNodeValue()).equals("true") );


        // Check for Font sub_element...
        NodeList childsOfChild = xmlReportElement.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("font")) {
                NamedNodeMap subNodeAttributes = child_child.getAttributes();
                if (subNodeAttributes.getNamedItem("reportFont") != null) {

                    IReportFont baseFont = null;
                    re.setReportFont(subNodeAttributes.getNamedItem("reportFont").getNodeValue());
                    for (int fn =0; fn < getReport().getFonts().size(); ++fn) {
                        baseFont = (IReportFont)getReport().getFonts().elementAt(fn);
                        if (baseFont != null && baseFont.getReportFont().equals(re.getReportFont())) {
                            break;
                        }
                        else
                            baseFont = null;
                    }
                    if (baseFont != null) {
                        //re.setFontSize(baseFont.getFontSize());
                        //re.setFontName(baseFont.getFontName());
                        //re.setPDFFontName( baseFont.getPDFFontName());
                        //re.setPdfEncoding( baseFont.getPdfEncoding());
                        //re.setPdfEmbedded( baseFont.isPdfEmbedded());
                        //re.setBold(baseFont.isBold()) ;
                        //re.setItalic( baseFont.isItalic());
                        //re.setStrikeTrought(baseFont.isStrikeTrought());
                        //re.setUnderline(baseFont.isUnderline());
                    }else{

                        //using default font
                        //re.setFontSize(defaultFont.getFontSize());
                        //re.setFontName(defaultFont.getFontName());
                        //re.setPDFFontName( defaultFont.getPDFFontName());
                        //re.setPdfEncoding( defaultFont.getPdfEncoding());
                        //re.setPdfEmbedded( defaultFont.isPdfEmbedded());
                        //re.setBold(defaultFont.isBold()) ;
                        //re.setItalic(defaultFont.isItalic());
                        //re.setStrikeTrought(defaultFont.isStrikeTrought());
                        //re.setUnderline(defaultFont.isUnderline());
                    }

                }

                //System.out.println("Working on: " + xmlReportElement);
                for (int kkk=0; kkk<subNodeAttributes.getLength(); ++kkk)
                {
                    //System.out.println( + " " + );

                    String propName = subNodeAttributes.item(kkk).getNodeName();
                    String propValue = subNodeAttributes.item(kkk).getNodeValue();
                    if (propName != null && propValue != null)
                    {
                        if (propName.equals("fontName"))
                            re.setFontName(propValue);
                        else if (propName.equals("pdfFontName"))
                            re.setPDFFontName(propValue);
                        else if (propName.equals("size"))
                            re.setFontSize( Integer.parseInt(""+propValue) );
                        else if (propName.equals("isBold"))
                            re.setBold( (new String(""+propValue)).equalsIgnoreCase("true") );
                        else if (propName.equals("isItalic"))
                            re.setItalic( (new String(""+propValue)).equalsIgnoreCase("true") );
                        else if (propName.equals("isUnderline"))
                            re.setUnderline( (new String(""+propValue)).equalsIgnoreCase("true") );
                        else if (propName.equals("isStrikeThrough"))
                            re.setStrikeTrought( (new String(""+propValue)).equalsIgnoreCase("true") );
                        else if (propName.equals("isPdfEmbedded"))
                            re.setPdfEmbedded((new String(""+propValue)).equalsIgnoreCase("true") );
                        else if (propName.equals("pdfEncoding"))
                            re.setPdfEncoding( ""+propValue );
                    }
                }
                re.setFont(null);
            }
        }
    }

    private Node findNextNode(Node node) {
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE){
            return node;
        }
        NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i=0; i< children.getLength(); i++) {
                return findNextNode  (children.item(i));
            }
        }
        return null;
    }


    /**
     * Find a node named childName inside parent
     *
     */
    private Node findChild(Node parent, String childName) {

        if (parent == null) return null;

        NodeList children = parent.getChildNodes();

        if (children != null) {
            for (int i=0; i< children.getLength(); i++) {

                Node node = (Node)children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(childName)){
                    return node;
                }
            }
            Node foundNode = null;
            for (int i=0; foundNode == null && i< children.getLength(); i++) {

                Node node = (Node)children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    foundNode =  findChild( node, childName);
                }
            }
            return foundNode;
        }
        return null;
    }

    private void readChartReportElement(Node child, ChartReportElement2 re) {

                if ( child.getNodeName().equals("pieChart") ) { re.setChart( new it.businesslogic.ireport.chart.PieChart()); }
                else if ( child.getNodeName().equals("pie3DChart") ) { re.setChart( new it.businesslogic.ireport.chart.Pie3DChart()); }
                else if ( child.getNodeName().equals("barChart") ) { re.setChart( new it.businesslogic.ireport.chart.BarChart()); }
                else if ( child.getNodeName().equals("bar3DChart") ) { re.setChart( new it.businesslogic.ireport.chart.Bar3DChart()); }
                else if ( child.getNodeName().equals("xyBarChart") ) { re.setChart( new it.businesslogic.ireport.chart.XYBarChart()); }
                else if ( child.getNodeName().equals("stackedBarChart") ) { re.setChart( new it.businesslogic.ireport.chart.StackedBarChart()); }
                else if ( child.getNodeName().equals("stackedBar3DChart") ) { re.setChart( new it.businesslogic.ireport.chart.StackedBar3DChart()); }
                else if ( child.getNodeName().equals("lineChart") ) { re.setChart( new it.businesslogic.ireport.chart.LineChart()); }
                else if ( child.getNodeName().equals("xyLineChart") ) { re.setChart( new it.businesslogic.ireport.chart.XYLineChart()); }
                else if ( child.getNodeName().equals("areaChart") ) { re.setChart( new it.businesslogic.ireport.chart.AreaChart()); }
                else if ( child.getNodeName().equals("stackedAreaChart") ) { re.setChart( new it.businesslogic.ireport.chart.StackedAreaChart()); }
                else if ( child.getNodeName().equals("xyAreaChart") ) { re.setChart( new it.businesslogic.ireport.chart.XYAreaChart()); }
                else if ( child.getNodeName().equals("scatterChart") ) { re.setChart( new it.businesslogic.ireport.chart.ScatterChart()); }
                else if ( child.getNodeName().equals("bubbleChart") ) { re.setChart( new it.businesslogic.ireport.chart.BubbleChart()); }
                else if ( child.getNodeName().equals("timeSeriesChart") ) { re.setChart( new it.businesslogic.ireport.chart.TimeSeriesChart()); }
                else if ( child.getNodeName().equals("highLowChart") ) { re.setChart( new it.businesslogic.ireport.chart.HighLowChart()); }
                else if ( child.getNodeName().equals("candlestickChart") ) { re.setChart( new it.businesslogic.ireport.chart.CandlestickChart()); }
                else if ( child.getNodeName().equals("meterChart") ) { re.setChart( new it.businesslogic.ireport.chart.MeterChart()); }
                else if ( child.getNodeName().equals("thermometerChart") ) { re.setChart( new it.businesslogic.ireport.chart.ThermometerChart()); }
                else if ( child.getNodeName().equals("multiAxisChart") ) { re.setChart( new it.businesslogic.ireport.chart.MultiAxisChart()); }

                // Element properties...
                NodeList childsOfChild = child.getChildNodes();
                for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
                    Node child_child = (Node)childsOfChild.item(c_count);
                    NamedNodeMap subNodeAttributes = child_child.getAttributes();
                    if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("chart")) {
                        readChartElement(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("pieDataset")) {
                        readPieDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("categoryDataset")) {
                        readCategoryDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timePeriodDataset")) {
                        readTimePeriodDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeSeriesDataset")) {
                        readTimeSeriesDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xyDataset")) {
                        readXYDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("xyzDataset")) {
                        readXYZDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("highLowDataset")) {
                        readHighLowDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("valueDataset")) {
                        readValueDataset(child_child,re);
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("piePlot")) {
                        readPiePlot(child_child, (PiePlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("pie3DPlot")) {
                        readPie3DPlot(child_child, (Pie3DPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("barPlot")) {
                        readBarPlot(child_child, (BarPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("bar3DPlot")) {
                        readBar3DPlot(child_child, (Bar3DPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("linePlot")) {
                        readLinePlot(child_child, (LinePlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("areaPlot")) {
                        readAreaPlot(child_child, (AreaPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("scatterPlot")) {
                        readScatterPlot(child_child, (ScatterPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("bubblePlot")) {
                        readBubblePlot(child_child, (BubblePlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("timeSeriesPlot")) {
                        readTimeSeriesPlot(child_child, (TimeSeriesPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("highLowPlot")) {
                        readHighLowPlot(child_child, (HighLowPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("candlestickPlot")) {
                        readCandlestickPlot(child_child, (CandlestickPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("meterPlot")) {
                        readMeterPlot(child_child, (MeterPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("thermometerPlot")) {
                        readThermometerPlot(child_child, (ThermometerPlot)re.getChart().getPlot());
                    }
                    else if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("multiAxisPlot")) {
                        readMultiAxisPlot(child_child, (MultiAxisPlot)re.getChart().getPlot());
                    }
                }
    }

    private void readMultiAxisPlot(Node xmlNode, MultiAxisPlot plot) {

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        readPlot(xmlNode, plot);


        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child_child = (Node)childsOfChild.item(c_count);
            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("axis")) {
                plot.getAxis().add( readAxis(child_child) );
            }
        }
    }

    private Axis readAxis(Node xmlNode) {

        Axis axis = new Axis();

        NamedNodeMap nodeAttributes = xmlNode.getAttributes();

        if (nodeAttributes.getNamedItem("position") != null)
                axis.setPosition( ""+nodeAttributes.getNamedItem("position").getNodeValue() );

        NodeList childsOfChild = xmlNode.getChildNodes();
        for (int c_count=0; c_count< childsOfChild.getLength(); c_count++) {
            Node child = (Node)childsOfChild.item(c_count);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("pieChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("pie3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("barChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bar3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyBarChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("stackedBarChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("stackedBar3DChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("lineChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyLineChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("areaChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("xyAreaChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("scatterChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("bubbleChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("timeSeriesChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("highLowChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("candlestickChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("meterChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("thermometerChart") ||
                     child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("multiAxisChart")) {
                ChartReportElement2 re = new ChartReportElement2(0,0,0,0);
                readChartReportElement(child, re);
                axis.setChartReportElement(re);
            }
        }

        return axis;
    }


}
