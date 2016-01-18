/*
 * Report.java
 *
 *  iReport  --  Visual designer for generating JasperReports Documents
 *  Copyright (C) 2002  Giulio Toffoli gt@businesslogic.it
 *
*  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.Styl
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
import it.businesslogic.ireport.chart.SectionItemHyperlink;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent;
import it.businesslogic.ireport.chart.Axis;
import it.businesslogic.ireport.util.I18n;
import java.io.ByteArrayInputStream;
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


import org.apache.xerces.parsers.DOMParser;

//import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author  Administrator
 */
public class ReportWriter {

   private Report report = null;

   public ReportWriter(Report report)
   {
       this.setReport(report);
   }

    public void saveXMLFile( String aFilename ) {


        String scriptletFileName = getReport().getScriptletFileName(aFilename);

        String className = "";
        java.io.File classFile = new  java.io.File(scriptletFileName);

        className = classFile.getName().substring(0, classFile.getName().length()-5);

        if (getReport().getScriptletHandling() == getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
            getReport().setScriptletClass( className );
        }

        for (int pk= 0; pk < getReport().getJRproperties().size(); ++pk) {
            JRProperty prop = (JRProperty)getReport().getJRproperties().elementAt( pk );
            if (prop.getName().equals("ireport.scriptlethandling") ||
                prop.getName().equals("ireport.encoding")) {
                getReport().getJRproperties().remove( prop );
                pk--;
            }
        }

        JRProperty prop = new JRProperty();
        prop.setName( "ireport.scriptlethandling" );
        prop.setValue( ""+ getReport().getScriptletHandling() );
        getReport().getJRproperties().add( prop );

        prop = new JRProperty();
        prop.setName( "ireport.encoding" );
        prop.setValue( ""+ getReport().getEncoding() );
        getReport().getJRproperties().add( prop );

        try {

            // Default file encoding
            String fileencoding = "UTF-8";
            if (getReport().getEncoding() != null && !getReport().getEncoding().trim().equals("")) {
                fileencoding = getReport().getEncoding();
                if (fileencoding.equalsIgnoreCase("UTF-8")){
                    fileencoding = "UTF8"; // Hack for J++
                }
            } else{
                getReport().setEncoding("UTF-8"); // Default XML encoding
            }

            writeToOutputStream(new java.io.FileOutputStream( aFilename ), fileencoding); //Changed by Felix Firgau

            //getReport().setCursor(Cursor.DEFAULT);
            getReport().setLoadTime( Misc.getLastWriteTime( getReport().getFilename() ));

            getReport().setReportChanges(0);

            // saving the scriptlet...
            if (getReport().getScriptletHandling() == getReport().SCRIPTLET_IREPORT_INTERNAL_HANDLING) {
                try {

                    String allcode = new String(getReport().getScripletCode().getAll() +"");

                    allcode = Misc.string_replace(className,"<ScriptletClassName>", allcode);
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(scriptletFileName);
                    fos.write( allcode.getBytes() );
                    fos.close();

                } catch (Exception ex) {
                    java.io.StringWriter s = new java.io.StringWriter();
                    ex.printStackTrace(new java.io.PrintWriter(s));
                    javax.swing.JOptionPane.showMessageDialog(getReport().getReportFrame().getMainFrame(),ex.getMessage()+" "+s,I18n.getString("message.title.exception","Exception"),javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }

            if (aFilename != null) {	/*
                                com.ms.wfc.io.File f = new com.ms.wfc.io.File(filename, com.ms.wfc.io.FileMode.OPEN);
                                getReport().loadTime = f.getLastWriteTime().toLong();
                                f.close();
             */
            }
        } catch (Exception ex) {
            //getReport().setCursor(Cursor.DEFAULT);
            //MessageBox.show(ex.getMessage());

            java.io.StringWriter s = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter(s));
            javax.swing.JOptionPane.showMessageDialog(getReport().getReportFrame().getMainFrame(),ex.getMessage()+" "+s,I18n.getString("message.title.exception","Exception"),javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * saveToOutputStream
     * saves a Report to an OutputStream
     * Does not support Scriptlet-saving
     *
     * save time and dirty status is not updated automatically.
     *
     * Author: Felix Firgau
     *
     * @param outputStream OutputStream
     * @param fileencoding String
     */
    public void writeToOutputStream(java.io.OutputStream outputStream) {
        writeToOutputStream(outputStream, "UTF-8");
    }

    /**
     * saveToOutputStream
     * saves a Report to an OutputStream
     * Does not support Scriptlet-saving
     *
     * save time and dirty status is not updated automatically.
     *
     * Author: Felix Firgau
     *
     * @param outputStream OutputStream
     * @param fileencoding String
     */
    public void writeToOutputStream(java.io.OutputStream outputStream, String fileencoding) {
        try {
            //getReport().setCursor(Cursor.WAIT );
            //java.io.PrintWriter pw = new java.io.PrintWriter( new java.io.FileOutputStream( filename ));
            PrintWriter pw = new PrintWriter( new java.io.OutputStreamWriter( outputStream, fileencoding )); //UTF8

            pw.print("<?xml version=\"1.0\"");
            if (getReport().getEncoding() != null && getReport().getEncoding().length()>0) {
                pw.print(" encoding=\""+ getReport().getEncoding() +"\" ");
            }
            pw.println(" ?>");
            pw.println("<!-- Created with iReport - A designer for JasperReports -->");
            pw.println("<!DOCTYPE jasperReport PUBLIC \"//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">");
            //pw.println("-->");
            // Jasper report element...
            pw.println("<jasperReport");
            pw.println("\t\t name=\""+ getReport().getName()  +"\"");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR066 && getReport().getLanguage() != null &&
               !getReport().getLanguage().trim().equals("") && !getReport().getLanguage().equals("java") )
            {
                pw.print("\n\t\t language=\"" + getReport().getLanguage().trim() + "\"");
            }

            pw.println("\t\t columnCount=\""+ getReport().getColumnCount()  +"\"");
            pw.println("\t\t printOrder=\""+ getReport().getPrintOrder() +"\"");
            pw.println("\t\t orientation=\""+ getReport().getOrientation() +"\"");
            pw.println("\t\t pageWidth=\""+ getReport().getWidth() +"\"");
            pw.println("\t\t pageHeight=\""+ getReport().getHeight() +"\"");
            pw.println("\t\t columnWidth=\""+  getReport().getColumnWidth() +"\"");
            pw.println("\t\t columnSpacing=\""+ getReport().getColumnSpacing()  +"\"");
            pw.println("\t\t leftMargin=\""+ getReport().getLeftMargin() +"\"");
            pw.println("\t\t rightMargin=\""+ getReport().getRightMargin() +"\"");
            pw.println("\t\t topMargin=\""+ getReport().getTopMargin() +"\"");
            pw.println("\t\t bottomMargin=\""+ getReport().getBottomMargin() +"\"");
            pw.println("\t\t whenNoDataType=\""+ getReport().getWhenNoDataType() +"\"");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR063 && getReport().isFloatColumnFooter() )
            {
                    pw.println("\t\t isFloatColumnFooter=\"true\"");
            }

            if (getReport().getScriptletHandling() != Report.SCRIPTLET_NOT_USED && getReport().getScriptletClass() != null && getReport().getScriptletClass().length()>0){
                pw.println("\t\t scriptletClass=\""+ getReport().getScriptletClass() +"\"");
            }

            pw.println("\t\t isTitleNewPage=\""+ getReport().isIsTitleNewPage()  +"\"");
            pw.print("\t\t isSummaryNewPage=\""+ getReport().isIsSummaryNewPage() +"\"");
             if ( CompatibilitySupport.version >= CompatibilitySupport.JR062 && getReport().getResourceBundleBaseName()!=null
             	  && !getReport().getResourceBundleBaseName().trim().equals(""))
            {
                pw.print("\n\t\t resourceBundle=\"" + getReport().getResourceBundleBaseName().trim() + "\"");
            }

             if ( CompatibilitySupport.version >= CompatibilitySupport.JR100 && getReport().getWhenResourceMissingType()!=null
             	  && !getReport().getWhenResourceMissingType().trim().equals("Null"))
            {
                pw.print("\n\t\t whenResourceMissingType=\"" + getReport().getWhenResourceMissingType().trim() + "\"");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR111 && getReport().isIgnorePagination() )
            {
                    pw.print("\n\t\t isIgnorePagination=\"true\"");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR128 &&
                 getReport().getFormatFactoryClass() != null &&
                 getReport().getFormatFactoryClass().trim().length() > 0)
            {
                    pw.print("\n\t\t formatFactoryClass=\"" +
                            Misc.xmlEscape(getReport().getFormatFactoryClass().trim()) + "\"");
            }


            pw.println(">");

            // Properties...
            Enumeration e = null;

            writeXMLProperties(getReport().getJRproperties(), true, pw, "\t");

            // For any chart element...
            Enumeration enum_chartElements = getReport().getElements().elements();
            while (enum_chartElements.hasMoreElements())
            {
                    ReportElement re = (ReportElement)enum_chartElements.nextElement();
                    if (re instanceof ChartReportElement)
                    {
                        Properties props = ((ChartReportElement)re).getProps();
                        String name = re.getName();

                        Iterator keys = props.keySet().iterator();
                        int i=0;
                        while (keys.hasNext())
                        {
                            String key = ""+keys.next();
                            String val = props.getProperty(key);
                            pw.print("\t<property name=\""+ "chart." + name + "."+key + "\" ");
                            pw.println("value=\""+ Misc.xmlEscape( val) + "\" />");
                        }
                    }
            }

            // For IMPORTS..
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR062 )
            {
                e = getReport().getImports().elements();
                while (e.hasMoreElements()) {
                    String _import = (String)e.nextElement();
                    pw.print("\t<import ");
                    pw.println("value=\""+ _import + "\" />");
                }
            }
            
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR200 )
            {
                e = getReport().getTemplates().elements();
                while (e.hasMoreElements()) {
                    Template template = (Template)e.nextElement();
                    pw.print("\t<template");
                    if (template.getExpressionClass() != null &&
                        !template.getExpressionClass().equals("java.lang.String"))
                    {
                        pw.print(" class=\""+ template.getExpressionClass() + "\"");
                    }
                    pw.print(">");
                    pw.println("<![CDATA[" + template.getExpression() + "]]></template>");
                }
            }

            e = getReport().getFonts().elements();

            while (e.hasMoreElements()) {
                IReportFont font = (IReportFont)e.nextElement();
                pw.print("\t<reportFont name=\""+ font.getReportFont() + "\" ");
                pw.print("isDefault=\""+ font.isDefaultFont() + "\" ");
                pw.print("fontName=\""+ font.getFontName() + "\" ");
                pw.print("size=\""+ font.getFontSize() + "\" ");
                pw.print("isBold=\""+ font.isBold() + "\" ");
                pw.print("isItalic=\""+ font.isItalic() + "\" ");
                pw.print("isUnderline=\""+ font.isUnderline() + "\" ");
                pw.print("isStrikeThrough=\""+ font.isStrikeTrought() + "\" ");
                if (Misc.nvl(font.getPDFFontName(), "").indexOf("TTF") > 0)
                    //TODO: this line cause write "" to xml when pdfFontName terminate with "TTF".
                    //this error was solved in IReportFont.setPDFFontName() take a look.
                    pw.print("pdfFontName=\""+ font.getTTFFont() + "\" ");
                else
                    pw.print("pdfFontName=\""+ font.getPDFFontName() + "\" ");
                pw.print("pdfEncoding=\""+ font.getPdfEncoding() + "\" ");
                pw.println("isPdfEmbedded=\""+ font.isPdfEmbedded() + "\"/>");
            }

            // Styles
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR111 )
            {
                writeXMLStyles(getReport().getStyles(), pw, "\t");
            }

            // SubDataset...
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR110)
            {
                writeXMLSubDatasets(getReport().getSubDatasets(), pw, "\t");
            }

            // Parameters
            writeXMLParameters(getReport().getParameters(), pw, "\t");

            // QueryString
            if (getReport().getQuery() != null && getReport().getQuery().trim().length() >0 ) {

                pw.print("\t<queryString");

                if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 &&
                     !getReport().getQueryLanguage().equals("sql"))
                {
                    pw.print(" language=\"" + getReport().getQueryLanguage() + "\"");
                }
                pw.println(">"+ getCDATAString(getReport().getQuery(),1) + "</queryString>");
            }

            // fileds...
            writeXMLFields(getReport().getFields(), pw, "\t");


            if ( CompatibilitySupport.version >= CompatibilitySupport.JR128)
            {
                    writeXMLSortFields(getReport().getSortFields(), pw, "\t");
            }

            // variables...
            writeXMLVariables(getReport().getVariables(), pw, "\t");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR125 &&
                 getReport().getFilterExpression() != null &&
                 getReport().getFilterExpression().trim().length() >0 ) {

                pw.println("\t<filterExpression>"+ getCDATAString(getReport().getFilterExpression(),1) + "</filterExpression>");
            }

            // groups...
            writeXMLGroups(getReport().getGroups(), pw,true,2);

            // if ( CompatibilitySupport.saveBackground)
            {
                writeXMLSection("background", pw);
            }

            writeXMLSection("title", pw);
            writeXMLSection("pageHeader", pw);
            writeXMLSection("columnHeader", pw);
            writeXMLSection("detail", pw);
            writeXMLSection("columnFooter", pw);
            writeXMLSection("pageFooter", pw);
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR061 )
            {
                writeXMLSection("lastPageFooter", pw);
            }
            writeXMLSection("summary", pw);
            
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR200 )
            {
                writeXMLSection("noData", pw);
            }
            

            pw.println("</jasperReport>");
            pw.close();

        } catch (Exception ex) {
            //getReport().setCursor(Cursor.DEFAULT);
            //MessageBox.show(ex.getMessage());

            java.io.StringWriter s = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter(s));
            javax.swing.JOptionPane.showMessageDialog(getReport().getReportFrame().getMainFrame(),ex.getMessage()+" "+s,I18n.getString("message.title.exception","Exception"),javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }

    void writeXMLSection(String section, PrintWriter pw) {
        // Find band...
        Band band = null;

        for (Iterator i = getReport().getBands().iterator(); i.hasNext(); ) {
            band = (Band) i.next();
            if (band.getName().equalsIgnoreCase(section)) {
                break;
            }
        }
        if (band == null) {
            return;
        }

        if (section.equals("lastPageFooter")
            && band.getHeight() == 0) return;
        
        if (section.equals("noData")
            && band.getHeight() == 0) return;

        pw.println("\t\t<"+ section +">");
        writeXMLBand(band, pw);
        pw.println("\t\t</"+ section +">");
    }

    void writeXMLSubDatasets(Vector subDatasets, PrintWriter pw, String tabs) {
            Enumeration e = subDatasets.elements();

            while (e.hasMoreElements()) {

                    pw.println("");

                    it.businesslogic.ireport.SubDataset subDataset = (it.businesslogic.ireport.SubDataset)e.nextElement();

                    pw.print(tabs + "<subDataset name=\""+ subDataset.getName() +"\" ");
                    if (subDataset.getScriptletClass() != null && subDataset.getScriptletClass().length()>0){
                        pw.println( tabs + "\t scriptletClass=\""+ subDataset.getScriptletClass() +"\"");
                    }

                    if (subDataset.getResourceBundleBaseName()!=null && !subDataset.getResourceBundleBaseName().trim().equals(""))
                    {
                        pw.print("\n" + tabs +"\t resourceBundle=\"" + getReport().getResourceBundleBaseName().trim() + "\"");
                    }

                    if ( subDataset.getWhenResourceMissingType()!=null && !subDataset.getWhenResourceMissingType().trim().equals("Null"))
                    {
                        pw.print("\n" + tabs + "\t whenResourceMissingType=\"" + getReport().getWhenResourceMissingType().trim() + "\"");
                    }
                    pw.println(">");

                    if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 )
                    {
                        writeXMLProperties(subDataset.getJRproperties(), false, pw, tabs+"\t");
                    }

                    // Parameters
                    writeXMLParameters(subDataset.getParameters(), pw, "\t\t");

                    // QueryString
                    if (subDataset.getQuery() != null && subDataset.getQuery().trim().length() >0 ) {
                        pw.print(tabs+ "\t<queryString");

                        if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 &&
                             !subDataset.getQueryLanguage().equals("sql"))
                        {
                            pw.print(" language=\"" + subDataset.getQueryLanguage() + "\"");
                        }
                        pw.println(">"+  getCDATAString(subDataset.getQuery(),1) + "</queryString>");
                    }

                    // fileds...

                    if ( CompatibilitySupport.version >= CompatibilitySupport.JR110)
                    {
                            writeXMLFields(subDataset.getFields(), pw, "\t\t");
                    }

                    writeXMLSortFields(subDataset.getSortFields(), pw, "\t\t");
                    // variables...
                    writeXMLVariables(subDataset.getVariables(), pw, "\t\t");


                    if ( CompatibilitySupport.version >= CompatibilitySupport.JR125 &&
                         subDataset.getFilterExpression() != null &&
                         subDataset.getFilterExpression().trim().length() >0 ) {

                         pw.println("\t\t<filterExpression>"+ getCDATAString(subDataset.getFilterExpression(),2) + "</filterExpression>");
                    }

                    // groups...
                    writeXMLGroups(subDataset.getGroups(), pw,false,2);
                    pw.println(tabs + "</subDataset>");
            }
    }

    void writeXMLParameters(Vector parameters, PrintWriter pw, String tabs) {
            Enumeration e = parameters.elements();
            if (parameters.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.JRParameter p = (it.businesslogic.ireport.JRParameter)e.nextElement();
                if (p.isBuiltin()) continue;

                pw.print(tabs + "<parameter name=\""+ p.getName() +"\" ");
                if (p.getClassType() == null ) p.setClassType("java.lang.String");
                pw.print("isForPrompting=\""+ p.isIsForPrompting() +"\" ");
                pw.print("class=\""+ p.getClassType() +"\"");

                if (p.getDefaultValueExpression().trim().length()> 0 ||
                p.getDescription().trim().length()>0 || p.getProperties().size() >0) {
                    pw.println(">");

                    if (p.getProperties().size() >0 && CompatibilitySupport.version >= CompatibilitySupport.JR132)
                    {
                        writeXMLProperties( p.getProperties(), false, pw, tabs+"\t");
                    }

                    if (p.getDescription().trim().length()> 0)
                        pw.println(tabs + "\t<parameterDescription>"+ getCDATAString(p.getDescription(),2) +"</parameterDescription>");
                    if (p.getDefaultValueExpression().trim().length()> 0) {
                        pw.print(tabs + "\t<defaultValueExpression ");
                                                /*
                                                if( getReport().isUsingMultiLineExpressions() ) {
                                                        pw.print(" isCode=\"true\" ");
                                                }
                                                 */
                        pw.println(">" + getCDATAString(p.getDefaultValueExpression(),2 ) +"</defaultValueExpression>");
                    }
                    pw.println(tabs + "</parameter>");
                }
                else
                    pw.println("/>");

            }

            // reset dirty operations...

    }

    void writeXMLFields(Vector fields, PrintWriter pw, String tabs)
    {
            Enumeration e = fields.elements();
            if (fields.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                if(!(o instanceof it.businesslogic.ireport.JRField))
                  continue;

                it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)o;
                pw.print(tabs + "<field name=\""+ f.getName() +"\" ");
                if (f.getClassType() == null ) f.setClassType("java.lang.String");
                pw.print("class=\""+ f.getClassType() +"\"");

                if ((f.getDescription() != null && f.getDescription().trim().length()>0) ||
                        f.getProperties().size() > 0) {
                    pw.println(">");

                    if (f.getProperties().size() >0 && CompatibilitySupport.version >= CompatibilitySupport.JR132)
                    {
                        writeXMLProperties( f.getProperties(), false, pw, tabs+"\t");
                    }

                    if (f.getDescription().trim().length()> 0)
                        pw.println(tabs + "\t<fieldDescription>"+ getCDATAString(f.getDescription(),2) +"</fieldDescription>");
                    pw.println(tabs + "</field>");
                }
                else
                    pw.println("/>");
            }
    }

    void writeXMLSortFields(Vector fields, PrintWriter pw, String tabs)
    {
            Enumeration e = fields.elements();
            if (fields.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                SortField f = (SortField)e.nextElement();
                pw.print(tabs + "<sortField name=\""+ Misc.xmlEscape( f.getFieldName() ) +"\" ");
                if (f.isDesc())
                {
                    pw.print("order=\"Descending\" ");
                }
                pw.println("/>");
            }
    }

    void writeXMLVariables(Vector variables, PrintWriter pw, String tabs)
    {
            Enumeration e = variables.elements();
            if (variables.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.JRVariable f = (it.businesslogic.ireport.JRVariable)e.nextElement();
                if (f.isBuiltin()) continue;
                pw.print(tabs + "<variable name=\""+ f.getName() +"\" ");
                if (f.getClassType() == null ) f.setClassType("java.lang.String");
                pw.print("class=\""+ f.getClassType() +"\" ");
                pw.print("resetType=\""+ f.getResetType() +"\" ");

                // From JasperReport 0.5.3
                if (f.getIncrementerFactoryClass() != null &&
                f.getIncrementerFactoryClass().trim().length() > 0) {
                    pw.print("incrementerFactoryClass=\""+ f.getIncrementerFactoryClass() +"\" ");
                }

                if ( CompatibilitySupport.version >= CompatibilitySupport.JR066 && f.getIncrementType() !=null
             	  && !f.getIncrementType().trim().equals("None"))
                {
                    pw.print("incrementType=\"" + f.getIncrementType().trim() + "\" ");
                    if (f.getIncrementType().equals("Group"))
                    {
                        pw.print("incrementGroup=\"" + f.getIncrementGroup().trim() + "\" ");
                    }
                }

                if (f.getResetGroup().length() >0 ) pw.print("resetGroup=\""+ f.getResetGroup() +"\" ");
                pw.println("calculation=\""+ f.getCalculation()  +"\">");
                if (f.getExpression().length()>0) {
                    pw.print(tabs + "\t<variableExpression");
                                        /*
                                        if(getReport().isUsingMultiLineExpressions()) {
                                                pw.print(" isCode=\"true\" ");
                                        }
                                         */
                    pw.println(">"+ getCDATAString(f.getExpression(),2) + "</variableExpression>");
                }
                if (f.getInitialValueExpression().length()>0) {
                    pw.print(tabs + "\t<initialValueExpression");
                                        /*
                                        if(getReport().isUsingMultiLineExpressions()) {
                                                pw.print(" isCode=\"true\" ");
                                        }
                                         */
                    pw.println(">"+getCDATAString(f.getInitialValueExpression(),2) +"</initialValueExpression>");
                }
                pw.println(tabs + "</variable>");
            }
    }

    void writeXMLMeasures(Vector measures, PrintWriter pw, String tabs)
    {
            Enumeration e = measures.elements();
            if (measures.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.crosstab.Measure f = (it.businesslogic.ireport.crosstab.Measure)e.nextElement();

                pw.print(tabs + "<measure name=\""+ f.getName() +"\"");
                if (f.getClassType() == null ) f.setClassType("java.lang.String");
                pw.print(" class=\""+ f.getClassType() +"\"");
                if (!f.getCalculation().equals("Nothing"))  pw.print(" calculation=\""+ f.getCalculation() +"\"");
                if (f.getIncrementerFactoryClass().trim().length() > 0)  pw.print(" incrementerFactoryClass=\""+ f.getIncrementerFactoryClass() +"\"");
                if (!f.getPercentageOf().equals("None"))  pw.print(" percentageOf=\""+ f.getPercentageOf() +"\"");
                if (f.getPercentageCalculatorClass().trim().length() > 0)  pw.print(" percentageCalculatorClass=\""+ f.getPercentageCalculatorClass() +"\"");
                pw.println(">");

                if (f.getExpression().length()>0) {
                    pw.print(tabs + "\t<measureExpression>");
                    pw.println(getCDATAString(f.getExpression(),2) + "</measureExpression>");
                }

                pw.println(tabs + "</measure>");
            }
    }

    void writeXMLCrosstabParameters(Vector parameters, PrintWriter pw, String tabs)
    {
            Enumeration e = parameters.elements();
            if (parameters.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.crosstab.CrosstabParameter f = (it.businesslogic.ireport.crosstab.CrosstabParameter)e.nextElement();

                pw.print(tabs + "<crosstabParameter name=\""+ f.getName() +"\" ");
                if (f.getClassType() == null ) f.setClassType("java.lang.String");
                pw.print("class=\""+ f.getClassType() +"\" ");
                pw.print(">");

                if (f.getParameterValueExpression().length()>0) {
                    pw.print(tabs + "\t<parameterValueExpression>");
                    pw.println(""+ getCDATAString(f.getParameterValueExpression(), tabs.length()+1 ) + "</parameterValueExpression>");
                }
                
                pw.println(tabs + "</crosstabParameter>");
            }
    }

    void writeXMLHyperlinkParameters(java.util.List parameters, PrintWriter pw, String tabs)
    {
            Iterator e = parameters.iterator();
            if (parameters.size() > 0) pw.println("");
            while (e.hasNext()) {
                it.businesslogic.ireport.JRLinkParameter f = (it.businesslogic.ireport.JRLinkParameter)e.next();

                pw.println(tabs + "<hyperlinkParameter name=\""+ f.getName() +"\">");
                pw.print(tabs + "\t<hyperlinkParameterExpression>");
                pw.println(""+ getCDATAString(f.getExpression(), tabs.length()+1 ) + "</hyperlinkParameterExpression>");
                pw.println(tabs + "</hyperlinkParameter>");
            }
    }


    void writeXMLGroups(Vector groups, java.io.PrintWriter pw, boolean writeAll, int tabCount) {
        // Find band...
        if (groups.size() > 0) pw.println("");
        for (int i=0; i< groups.size(); i++) {
            Group grp = (Group)groups.elementAt(i);
            for (int t=0; t<tabCount; ++t) pw.print("\t");
            pw.print("<group ");
            pw.print(" name=\""+grp.getName()  +"\"");
            if (grp.isIsStartNewColumn()) pw.print(" isStartNewColumn=\""+grp.isIsStartNewColumn() +"\"");
            if (grp.isIsStartNewPage()) pw.print(" isStartNewPage=\""+grp.isIsStartNewPage() +"\"");
            if (grp.isIsResetPageNumber()) pw.print(" isResetPageNumber=\""+grp.isIsResetPageNumber() +"\"");
            if (grp.isIsReprintHeaderOnEachPage()) pw.print(" isReprintHeaderOnEachPage=\""+grp.isIsReprintHeaderOnEachPage() +"\"");
            if (grp.getMinHeightToStartNewPage() != 0) pw.print(" minHeightToStartNewPage=\""+grp.getMinHeightToStartNewPage() +"\"");
            pw.println(" >");
            pw.print("\t\t\t<groupExpression");
                        /*
                        if(getReport().isUsingMultiLineExpressions()) {
                                pw.print(" isCode=\"true\" ");
                        }
                         */
            pw.println(">" + getCDATAString(grp.getGroupExpression() ,tabCount + 1) +"</groupExpression>");
            if (writeAll)
            {
                pw.println("\t\t\t<groupHeader>");
                writeXMLBand(grp.getGroupHeader(), pw);
                pw.println("\t\t\t</groupHeader>");
                pw.println("\t\t\t<groupFooter>");
                writeXMLBand(grp.getGroupFooter(), pw);
                pw.println("\t\t\t</groupFooter>");
            }
            for (int t=0; t<tabCount; ++t) pw.print("\t");
            pw.println("</group>");
        }
    }

    void writeXMLStyles(Vector styles, java.io.PrintWriter pw, String tabs)
    {

            Enumeration e = styles.elements();
            if (styles.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.Style style = (it.businesslogic.ireport.Style)e.nextElement();

                if (style instanceof UndefinedStyle)
                {
                    continue;
                }
                    
                
                String tabs2 = tabs;
                if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 )
                {
                    if (style instanceof ConditionedStyle)
                    {
                        pw.println(tabs2 + "<conditionalStyle>");
                        tabs += "\t";
                        pw.print(tabs + "<conditionExpression");
                        pw.println(">" + getCDATAString(((ConditionedStyle)style).getCondition() ,tabs2.length()+1) +"</conditionExpression>");
                    }

                }

                pw.println(tabs + "<style ");
                for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
                {
                    if ( style.getAttributes().containsKey(Style.JRXMLStyleAttributes[i]) &&
                         style.getAttributes().get( Style.JRXMLStyleAttributes[i] ) != null) {
                         Object obj = style.getAttributes().get(Style.JRXMLStyleAttributes[i]);
                         String value = ""+obj;
                         if (obj instanceof java.awt.Color)
                         {
                             value = writeEncodedColor( (java.awt.Color)obj);
                         }
                         if (Style.JRXMLStyleAttributes[i].toLowerCase().endsWith("color") && value.startsWith("["))
                         {
                             // The color is in the form [r,g,b]...
                             try {
                               value =   writeEncodedColor(it.businesslogic.ireport.gui.sheet.ColorSelectorPanel.parseColorString(value));
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

                if (CompatibilitySupport.version < CompatibilitySupport.JR120 || style.getConditionedStyles().size() == 0)
                {
                    pw.println(tabs + "/>");
                }
                else
                {
                    pw.println(tabs + ">");
                    writeXMLStyles(style.getConditionedStyles(), pw, tabs+"\t");
                    pw.println(tabs + "</style>");
                }
                if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 )
                {
                    if (style instanceof ConditionedStyle)
                    {
                        pw.println(tabs2 + "</conditionalStyle>");
                    }
                }
            }
    }

    void writeXMLBand(Band band, PrintWriter pw) {
        pw.print("\t\t\t<band height=\""+ band.getHeight() + "\" ");

        //if () //JasperReports 0.5.0 is supported...
        {
            pw.print(" isSplitAllowed=\"" + band.isSplitAllowed() + "\" ");
        }
        pw.println(">");

        if (band.getPrintWhenExpression().trim().length() > 0) {
            pw.print("\t\t\t\t<printWhenExpression");
                        /*
                        if(getReport().isUsingMultiLineExpressions()) {
                                pw.print(" isCode=\"true\" ");
                        }
                         */
            pw.println(">" + getCDATAString(band.getPrintWhenExpression(),4) + "</printWhenExpression>");
        }

        writeBandElements(pw, band, null);
        pw.println("\t\t\t</band>");
    }


    public void writeBandElements(PrintWriter pw, Band band, ReportElement parent)
    {
        String actualElementGroup = "";
        for (Iterator i = getReport().getElements().iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            if (re.band == band && re.getParentElement() == parent) {
                    actualElementGroup = writeOpenCloseGroup(pw,re.getElementGroup(), actualElementGroup);
                    writeXMLReportElement(re,pw);
            }
        }
        writeOpenCloseGroup(pw, "", actualElementGroup);
    }

    public String writeOpenCloseGroup(PrintWriter pw, String newGroup, String oldGroup)
    {
        if (oldGroup.equals(newGroup)) return newGroup;
        while (!newGroup.startsWith( oldGroup) && oldGroup.length() > 0)
        {
                pw.println("\t\t\t\t</elementGroup><!-- End " + oldGroup + " !-->");
                if (oldGroup.lastIndexOf(".") >= 0)
                {
                    oldGroup = oldGroup.substring(0, oldGroup.lastIndexOf("."));
                }
                else
                {
                    oldGroup="";
                }
        }

        String remainNewGroup = newGroup;
        if (oldGroup.length() > 0 && newGroup.startsWith(oldGroup))
        {
            remainNewGroup = newGroup.substring(oldGroup.length());
            while (remainNewGroup.startsWith("."))
            {
                remainNewGroup = remainNewGroup.substring(1);
            }

        }

        String groupName = remainNewGroup;
        while (remainNewGroup.length() > 0)
        {
            pw.print("\t\t\t\t<elementGroup>");

            if (remainNewGroup.indexOf(".") >= 0)
            {
                groupName = ((oldGroup.length() > 0)  ? oldGroup + "." : "") + remainNewGroup.substring(0,remainNewGroup.indexOf("."));
                oldGroup = remainNewGroup.substring(0,remainNewGroup.indexOf("."));
                remainNewGroup = remainNewGroup.substring(remainNewGroup.indexOf(".")+1);
            }
            else
            {
                groupName = ((oldGroup.length() > 0)  ? oldGroup + "." : "") + remainNewGroup;
                remainNewGroup="";
            }
            pw.println("<!--  Start: " + groupName + " !-->");
        }

        return newGroup;
    }

    void writeXMLReportElement(ReportElement re, PrintWriter pw) {
        if (re instanceof StaticTextReportElement) {
            pw.println("\t\t\t\t<staticText>");

            writeXMLReportElementElement(re, pw);
            writeXMLBox( ((TextReportElement)re).getBox(), pw );
            writeXMLTextElementElement((TextReportElement)re, pw);
            pw.println("\t\t\t\t<text>"+getCDATAString(((StaticTextReportElement)re).getText(), 4)+"</text>");
            pw.println("\t\t\t\t</staticText>");

        }
        else if (re instanceof FrameReportElement)
        {
            FrameReportElement fre = (FrameReportElement)re;
            pw.print("\t\t\t\t<frame>");
            writeXMLReportElementElement(fre, pw);
            writeXMLBox( ((BoxElement)fre).getBox(), pw );
            if (re instanceof FrameReportElement)
            {
                   writeBandElements(pw, re.getBand(), re);
            }
            pw.print("\t\t\t\t</frame>");
        }
        else if (re instanceof TextFieldReportElement) {
            TextFieldReportElement tfre = (TextFieldReportElement)re;
            pw.print("\t\t\t\t<textField");
            pw.print(" isStretchWithOverflow=\""+ tfre.isStretchWithOverflow() +"\"");

            if (tfre.getPropertyValue(tfre.PATTERN) != null)
            {
                pw.print(" pattern=\"" + tfre.getPattern() + "\"");
            }

            pw.print(" isBlankWhenNull=\"" + tfre.isBlankWhenNull() +"\"");
            pw.print(" evaluationTime=\""+ tfre.getEvaluationTime() +"\"");
            if (tfre.getEvaluationTime().equals("Group")) {
                pw.print(" evaluationGroup=\"" + tfre.getGroup() +"\" ");
            }
            pw.print(" hyperlinkType=\"" + tfre.getHyperlinkType() +"\" ");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR062 )
            {
                pw.print(" hyperlinkTarget=\"" + tfre.getHyperlinkTarget() +"\" ");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR100 )
            {
                if (tfre.getBookmarkLevel() != 0 ) pw.print(" bookmarkLevel=\"" + tfre.getBookmarkLevel() +"\" ");
            }

            pw.println(">");

            writeXMLReportElementElement(tfre, pw);
            writeXMLBox(((TextReportElement)re).getBox(), pw);
            writeXMLTextElementElement(tfre, pw);
            pw.print("\t\t\t\t<textFieldExpression ");
            if (tfre.getClassExpression().length() > 0) {
                pw.print("  class=\""+ tfre.getClassExpression()+"\"");
            }
                        /*
                        if(getReport().isUsingMultiLineExpressions()) {
                                pw.print(" isCode=\"true\" ");
                        }
                         */
            pw.println(">"+getCDATAString(((TextFieldReportElement)re).getText(),4)+"</textFieldExpression>");

            writeHyperLinkExpressions(tfre, pw);

            pw.println("\t\t\t\t</textField>");
        }
        else if (re instanceof ImageReportElement) {
            ImageReportElement ire = (ImageReportElement)re;
            pw.print("\t\t\t\t<image ");

            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(ire.getScaleImage()+"",Style.ATTRIBUTE_scaleImage,re.getStyle(), getDefaultStyle()))
            if (ire.getPropertyValue( ire.SCALE) != null)
            {
                pw.print(" scaleImage=\""+ ire.getScaleImage()  +"\"");
            }

            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(ire.getVerticalAlignment()+"",Style.ATTRIBUTE_vAlign,re.getStyle(), getDefaultStyle()))
            if (ire.getPropertyValue( ire.VERTICAL_ALIGN) != null)
            {
                pw.print(" vAlign=\""+ ire.getVerticalAlignment()  +"\"");
            }
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(ire.getHorizontalAlignment()+"",Style.ATTRIBUTE_hAlign,re.getStyle(), getDefaultStyle()))
            if (ire.getPropertyValue( ire.HORIZONTAL_ALIGN) != null)
            {
                pw.print(" hAlign=\""+ ire.getHorizontalAlignment()  +"\"");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR065 && ire.isIsLazy() == true)
            {
                pw.print(" isLazy=\""+ ire.isIsLazy() +"\"");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR103 && !ire.getOnErrorType().equals("Error"))
            {
                pw.print(" onErrorType=\""+ ire.getOnErrorType() +"\"");
            }

            if (ire.getPropertyValue( ire.USING_CACHE) != null)
            {
                pw.print(" isUsingCache=\""+ ire.isIsUsingCache() +"\"");
            }

            pw.print(" evaluationTime=\""+ ire.getEvaluationTime() + "\"");

            if (((ImageReportElement)re).getEvaluationTime().equals("Group")) {
                pw.print(" evaluationGroup=\"" + ire.getEvaluationGroup() +"\" ");
            }

            pw.print(" hyperlinkType=\"" + ire.getHyperlinkType()+"\" ");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR062 )
            {
                pw.print(" hyperlinkTarget=\"" + ire.getHyperlinkTarget() +"\" ");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR100 )
            {
                if (ire.getBookmarkLevel() != 0 ) pw.print(" bookmarkLevel=\"" + ire.getBookmarkLevel() +"\" ");
            }

            pw.println(">");


            writeXMLReportElementElement(re, pw);
            writeXMLBox( ((ImageReportElement)re).getBox(),  pw);
            writeXMLGraphicElement(ire,pw);

            if (re instanceof ChartReportElement) {
                pw.print("\t\t\t\t\t<imageExpression class=\"java.awt.Image\"");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.print(">"
                // + getCDATAString( getExpressionString( ire.getImageClass(), ire.getImageExpression() ), 5) +
                + getCDATAString(
                writeChartExpression( (ChartReportElement)re)
                , 5) + "</imageExpression>\n" );
            }
            else {
                pw.print("\t\t\t\t\t<imageExpression class=\""+ ire.getImageClass() + "\"");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">"
                // + getCDATAString( getExpressionString( ire.getImageClass(), ire.getImageExpression() ), 5) +
                + getCDATAString(  ire.getImageExpression(), 5) + "</imageExpression>" );
            }

            writeHyperLinkExpressions(ire, pw);
            pw.println("\t\t\t\t</image>");

        } else if (re instanceof LineReportElement) {
            LineReportElement lre = (LineReportElement)re;
            pw.println("\t\t\t\t<line direction=\""+ lre.getDirection() +"\">");
            writeXMLReportElementElement(re, pw);
            writeXMLGraphicElement(lre,pw);
            pw.println("\t\t\t\t</line>");

        } else if (re instanceof BreakReportElement) {

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR129)
            {
                BreakReportElement lre = (BreakReportElement)re;
                pw.print("\t\t\t\t<break");
                if (!lre.getType().equals("Page"))
                {
                    pw.print(" type=\""+ lre.getType() +"\"");
                }
                pw.println(">");
                writeXMLReportElementElement(re, pw);
                pw.println("\t\t\t\t</break>");
            }

        } else if (re instanceof RectangleReportElement) {
            RectangleReportElement rre = (RectangleReportElement)re;
            pw.print("\t\t\t\t<rectangle");
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(rre.getRadius()+"",Style.ATTRIBUTE_radius,re.getStyle(), getDefaultStyle()))
            if (rre.getPropertyValue( rre.RADIUS) != null)
            {
                pw.print(" radius=\""+ rre.getRadius() + "\" ");
            }
            pw.println(">");
            writeXMLReportElementElement(re, pw);
            writeXMLGraphicElement(rre,pw);
            pw.println("\t\t\t\t</rectangle>");

        } else if (re instanceof EllipseReportElement ) {
            //if ( CompatibilitySupport.saveEllipse )
            {
                EllipseReportElement ere = (EllipseReportElement)re;
                pw.println("\t\t\t\t<ellipse>");
                writeXMLReportElementElement(ere, pw);
                writeXMLGraphicElement(ere,pw);
                pw.println("\t\t\t\t</ellipse>");
            }
        }
        else if (re instanceof SubReportElement) {
            SubReportElement sre = (SubReportElement)re;
            pw.println("\t\t\t\t<subreport  isUsingCache=\""+ sre.isIsUsingCache()  +"\">");
            writeXMLReportElementElement(re, pw);
            if (sre.getParametersMapExpression() != null &&
            sre.getParametersMapExpression().trim().length() > 0) {
                pw.print("\t\t\t\t\t<parametersMapExpression");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">"+getCDATAString(sre.getParametersMapExpression(),5)+"</parametersMapExpression>");
            }

            Enumeration e = sre.getSubreportParameters().elements();
            while (e.hasMoreElements()) {
                JRSubreportParameter je = (JRSubreportParameter)e.nextElement();
                pw.println("\t\t\t\t\t<subreportParameter  name=\""+ je.getName() +"\">");
                pw.print("\t\t\t\t\t\t<subreportParameterExpression");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">"+getCDATAString(je.getExpression(),6)+"</subreportParameterExpression>");
                pw.println("\t\t\t\t\t</subreportParameter>");
            }
            if (sre.isUseConnection() && sre.getConnectionExpression().trim().length() > 0 ) {
                pw.print("\t\t\t\t\t<connectionExpression");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">"+getCDATAString(sre.getConnectionExpression(),5 )+ "</connectionExpression>");
            }
            else if (!sre.isUseConnection()  && sre.getDataSourceExpression().length() > 0) {
                pw.print("\t\t\t\t\t<dataSourceExpression");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">"+getCDATAString(sre.getDataSourceExpression(),5 )+"</dataSourceExpression>");
            }

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR100)
            {
                Enumeration e_sr = sre.getReturnValues().elements();
                while (e_sr.hasMoreElements()) {
                    it.businesslogic.ireport.JRSubreportReturnValue jerv = (it.businesslogic.ireport.JRSubreportReturnValue)e_sr.nextElement();
                    pw.print("\t\t\t\t\t<returnValue");
                    if (jerv.getSubreportVariable()!= null && jerv.getSubreportVariable().length()>0) pw.print(" subreportVariable=\""+ jerv.getSubreportVariable() +"\"");
                    if (jerv.getToVariable()!= null && jerv.getToVariable().length()>0) pw.print(" toVariable=\""+ jerv.getToVariable() +"\"");
                    if (jerv.getCalculation() != null && !jerv.getCalculation().equals("Nothing")) pw.print(" calculation=\""+ jerv.getCalculation() +"\"");
                    if (jerv.getIncrementFactoryClass()!= null && jerv.getIncrementFactoryClass().length()>0) pw.print(" incrementerFactoryClass=\""+ jerv.getIncrementFactoryClass() +"\"");
                    pw.println("/>");
                }
            }

            if (sre.getSubreportExpression()  != null &&
            sre.getSubreportExpression().trim().length() > 0) {
                pw.print("\t\t\t\t\t<subreportExpression  class=\""+ sre.getSubreportExpressionClass()  +"\"");
                                /*
                                if(getReport().isUsingMultiLineExpressions()) {
                                        pw.print(" isCode=\"true\" ");
                                }
                                 */
                pw.println(">" +
                getCDATAString(sre.getSubreportExpression(),5) + "</subreportExpression>");
            }

            pw.println("\t\t\t\t</subreport>");
        } else if (re instanceof ChartReportElement2 ) {
            writeChartElement((ChartReportElement2)re, pw);
        } else if (re instanceof CrosstabReportElement) {
            CrosstabReportElement cre = (CrosstabReportElement)re;
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR111 )
            {
                pw.print("\t\t\t\t<crosstab ");
                    if (!cre.isRepeatColumnHeaders()) pw.print("isRepeatColumnHeaders=\""+ cre.isRepeatColumnHeaders() +"\" ");
                    if (!cre.isRepeatRowHeaders()) pw.print("isRepeatRowHeaders=\""+ cre.isRepeatRowHeaders() +"\" ");
                    if (cre.getColumnBreakOffset() != 10) pw.print("columnBreakOffset=\""+ cre.getColumnBreakOffset() +"\" ");
                
                    if ( CompatibilitySupport.version >= CompatibilitySupport.JR134 && cre.getRunDirection() !=null
                        && !cre.getRunDirection().trim().equals("LTR"))
                    {
                        pw.print("runDirection=\"" + cre.getRunDirection().trim() + "\" ");
                    }
                pw.println(">");
                writeXMLReportElementElement(re, pw);
                writeXMLCrosstabParameters(cre.getCrosstabParameters(),pw,"\t\t\t\t\t");

                if (cre.getParametersMapExpression() != null && cre.getParametersMapExpression().trim().length() > 0) {
                    pw.print("\t\t\t\t\t<parametersMapExpression>");
                    pw.println(getCDATAString(cre.getParametersMapExpression(),5)+"</parametersMapExpression>");
                }

                if (cre.isUseDataset() ) {
                    pw.print("\t\t\t\t\t<crosstabDataset");
                        if (cre.isPreSorted()) pw.print(" isDataPreSorted=\""+ cre.isPreSorted() +"\" ");
                    pw.println(">");
                    if (cre.getDataset() != null)
                    {
                        writeDatasetDataset( cre.getDataset(), pw );
                    }
                    pw.println("\t\t\t\t\t</crosstabDataset>");
                }

                if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 )
                {
                    for (int i=0; i<cre.getCells().size(); ++i)
                    {
                        CrosstabCell cell = (CrosstabCell)cre.getCells().elementAt(i);
                        if (cell.getType() == cell.CT_HEADER_CELL)
                        {
                            pw.print("\t\t\t\t\t<crosstabHeaderCell>");
                            writeXMLCrosstabCellcontents( cell, cre.getElements(), pw, "\t\t\t\t\t\t");
                            pw.print("\t\t\t\t\t</crosstabHeaderCell>");
                        }
                    }
                }

                writeXMLCrosstabRowGroups(cre.getRowGroups(), cre.getElements(), pw);
                writeXMLCrosstabColumnGroups(cre.getColumnGroups(),  cre.getElements(), pw);
                writeXMLMeasures(cre.getMeasures(), pw, "\t\t\t\t\t");
                writeXMLCrosstabCells(cre.getCells(), cre.getElements(), pw, "\t\t\t\t\t");

                for (int i=0; i<cre.getCells().size(); ++i)
                {
                    CrosstabCell cell = (CrosstabCell)cre.getCells().elementAt(i);
                    if (cell.getType() == cell.NODATA_CELL)
                    {
                        pw.print("\t\t\t\t\t<whenNoDataCell>");
                        writeXMLCrosstabCellcontents( cell, cre.getElements(), pw, "\t\t\t\t\t\t");
                        pw.print("\t\t\t\t\t</whenNoDataCell>");
                    }
                }



                pw.println("\t\t\t\t\t</crosstab>");
            }
        }

    }

    void writeXMLBox(Box box, PrintWriter pw) {

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR063 )
        {
            if (box == null) return;
            pw.print("\t\t\t\t\t<box");
            if (box.getTopBorder() != null) pw.print(" topBorder=\"" + box.getTopBorder() + "\"");
            if (box.getTopBorderColor() != null) pw.print(" topBorderColor=\"" +  writeEncodedColor( box.getTopBorderColor() ) + "\"");
            if (box.getTopPadding() > 0) pw.print(" topPadding=\"" + box.getTopPadding() + "\"");

            if (box.getLeftBorder() != null) pw.print(" leftBorder=\"" + box.getLeftBorder() + "\"");
            if (box.getLeftBorderColor() != null) pw.print(" leftBorderColor=\"" +  writeEncodedColor( box.getLeftBorderColor() ) + "\"");
            if (box.getLeftPadding() > 0) pw.print(" leftPadding=\"" + box.getLeftPadding() + "\"");

            if (box.getRightBorder() != null) pw.print(" rightBorder=\"" + box.getRightBorder() + "\"");
            if (box.getRightBorderColor() != null) pw.print(" rightBorderColor=\"" +  writeEncodedColor( box.getRightBorderColor() ) + "\"");
            if (box.getRightPadding() > 0) pw.print(" rightPadding=\"" + box.getRightPadding() + "\"");

            if (box.getBottomBorder() != null) pw.print(" bottomBorder=\"" + box.getBottomBorder() + "\"");
            if (box.getBottomBorderColor() != null) pw.print(" bottomBorderColor=\"" +  writeEncodedColor( box.getBottomBorderColor() ) + "\"");
            if (box.getBottomPadding() > 0) pw.print(" bottomPadding=\"" + box.getBottomPadding() + "\"");
            pw.println("/>");
        }
    }

        void writeFontElement(IReportFont font, PrintWriter pw) {

            if (font == null) return;
            pw.print("\t\t\t\t\t<font");

            IReportFont base = getReport().getReportFontByName( font.getReportFont() );

            if (font.getReportFont() != null && font.getReportFont().length() > 0) pw.print(" reportFont=\"" +  font.getReportFont() + "\"");

            if (base != null && base.getFontName().equals( font.getFontName())) { /* use default value */ }
            else if (font.getFontName() != null && font.getFontName().length() > 0) pw.print(" fontName=\"" +  font.getFontName() + "\"");

            if ( base != null && base.getPDFFontName().equals( font.getPDFFontName())) { /* use default value */ }
            else if (font.getPDFFontName() != null && font.getPDFFontName().length() > 0) pw.print(" pdfFontName=\"" +  font.getPDFFontName() + "\"");

            if ( base != null && base.getFontSize() == font.getFontSize() ) { /* use default value */ }
            else pw.print(" size=\"" +  font.getFontSize() + "\"");

            if ( base != null && base.isBold() == font.isBold() ) { /* use default value */ }
            else pw.print(" isBold=\"" +  ((font.isBold()) ? "true" : "false") + "\"");

            if ( base != null && base.isItalic() == font.isItalic() ) { /* use default value */ }
            else pw.print(" isItalic=\"" +  ((font.isItalic()) ? "true" : "false") + "\"");

            if ( base != null && base.isUnderline() == font.isUnderline() ) { /* use default value */ }
            else pw.print(" isUnderline=\"" +  ((font.isUnderline()) ? "true" : "false") + "\"");

            if ( base != null && base.isStrikeTrought() == font.isStrikeTrought() ) { /* use default value */ }
            else pw.print(" isStrikeThrough=\"" +  ((font.isStrikeTrought()) ? "true" : "false") + "\"");

            if ( base != null && base.isPdfEmbedded() == font.isPdfEmbedded() ) { /* use default value */ }
            else pw.print(" isPdfEmbedded=\"" +  ((font.isPdfEmbedded()) ? "true" : "false") + "\"");

            if ( base != null && base.getPdfEncoding().equals(font.getPdfEncoding()) ) { /* use default value */ }
            else if (font.getPdfEncoding() != null && font.getPdfEncoding().length() > 0) pw.print(" pdfEncoding=\"" +  font.getPdfEncoding() + "\"");

            pw.println("/>");
    }


    void writeXMLReportElementElement(ReportElement re, PrintWriter pw) {
        pw.print("\t\t\t\t\t<reportElement");

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR111 )
        {
            if ( re.getStyle() != null)
               pw.print("\n\t\t\t\t\t\tstyle=\""+  re.getStyle() + "\"");
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getTransparent(),Style.ATTRIBUTE_mode,re.getStyle(), getDefaultStyle()))
        {
            if (re.getPropertyValue(re.MODE) != null)
            {
                pw.print("\n\t\t\t\t\t\tmode=\""+ re.getTransparent() + "\"");
            }
        }

        int position_x = re.getPosition().x;

        if (re.getParentElement() != null) position_x -= re.getParentElement().getPosition().x;
        else if (re.getCell() != null) position_x = position_x - re.getCell().getLeft()- 10;
        else position_x = position_x - getReport().getLeftMargin() - 10;

        pw.print("\n\t\t\t\t\t\tx=\""+ position_x + "\"");

        int position_y = re.getPosition().y;
        if (re.getParentElement() != null) position_y -= re.getParentElement().getPosition().y;
        else if (re.getCell() != null) position_y = position_y - re.getCell().getTop()- 10;
        else position_y = position_y - 10 - getReport().getBandYLocation(re.getBand());

        pw.print("\n\t\t\t\t\t\ty=\""+ position_y +"\"");

        pw.print("\n\t\t\t\t\t\twidth=\""+ re.getWidth() + "\"");
        pw.print("\n\t\t\t\t\t\theight=\""+ re.getHeight() + "\"");


        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getFgcolor(),Style.ATTRIBUTE_forecolor,re.getStyle(), getDefaultStyle()))
        {
            if (re.getColorValue(re.FGCOLOR, null) != null)
            {
                pw.print("\n\t\t\t\t\t\tforecolor=\""+ writeEncodedColor(re.getColorValue(re.FGCOLOR, null)) +"\"");
            }
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 || !Style.isDefaultValue(re.getBgcolor(),Style.ATTRIBUTE_backcolor,re.getStyle(), getDefaultStyle()) )
        {
            if (re.getColorValue(re.BGCOLOR, null) != null)
            {
                pw.print("\n\t\t\t\t\t\tbackcolor=\""+ writeEncodedColor(re.getColorValue(re.BGCOLOR, null)) +"\"");
            }
        }

        // if =052 compatibility....
        if (!re.getKey().equals(""))
        {
            pw.print("\n\t\t\t\t\t\tkey=\""+ re.getKey() +"\"");
        }

        if (!re.getStretchType().equals("NoStretch"))
        {
            pw.print("\n\t\t\t\t\t\tstretchType=\""+ re.getStretchType() +"\"");
        }

        if (!re.getPositionType().equals("FixRelativeToTop"))
        {
            pw.print("\n\t\t\t\t\t\tpositionType=\""+ re.getPositionType() +"\"");
        }

        if (re.isIsPrintRepeatedValues() != true)
        {
            pw.print("\n\t\t\t\t\t\tisPrintRepeatedValues=\""+ re.isIsPrintRepeatedValues() +"\"");
        }
        if (re.isIsRemoveLineWhenBlank() != false)
        {
            pw.print("\n\t\t\t\t\t\tisRemoveLineWhenBlank=\""+ re.isIsRemoveLineWhenBlank() +"\"");
        }
        if (re.isIsPrintInFirstWholeBand() != false)
        {
            pw.print("\n\t\t\t\t\t\tisPrintInFirstWholeBand=\""+ re.isIsPrintInFirstWholeBand() +"\"");
        }

        if (re.getPrintWhenGroupChanges() != null &&  re.getPrintWhenGroupChanges().trim().length()>0)
            pw.print("\n\t\t\t\t\t\tprintWhenGroupChanges=\""+ re.getPrintWhenGroupChanges() +"\"");

        if (re.isIsPrintWhenDetailOverflows() != false)
        {
            pw.print("\n\t\t\t\t\t\tisPrintWhenDetailOverflows=\""+ re.isIsPrintWhenDetailOverflows() +"\"");
        }

        if (re.getPrintWhenExpression().length()>0) {
            pw.println(">");
            pw.print("\t\t\t\t\t\t\t<printWhenExpression");
                        /*
                        if(getReport().isUsingMultiLineExpressions()) {
                                pw.print(" isCode=\"true\" ");
                        }
                         */
            pw.println(">"+getCDATAString(re.getPrintWhenExpression(),7) + "</printWhenExpression>");
            pw.println("\t\t\t\t\t\t</reportElement>");
        }
        else{
            pw.println("/>");
        }

    }

    public static String getCDATAString( String string, int tabCount ) {
        //if ( ! Utils.needToBeInsideCDATA( string ) )
        //	return string;
        String CRLF = System.getProperty("line.separator");
        String tab = "";
        for ( int i = 0; i < tabCount; i++ ) {
            tab += "\t";
        }
        return "<![CDATA[" + string + "]]>"; // + CRLF + tab ;
    }

    void writeXMLGraphicElement(GraphicReportElement re, PrintWriter pw) {
        pw.print("\t\t\t\t\t<graphicElement");

        pw.print(" stretchType=\"" + re.getStretchType() + "\"");
        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getGraphicElementPen(),Style.ATTRIBUTE_pen,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue(re.PEN) != null)
        {
            pw.print(" pen=\""+ re.getGraphicElementPen() + "\"");
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getFill(),Style.ATTRIBUTE_fill,re.getStyle(), getDefaultStyle()))

        if (re.getPropertyValue(GraphicReportElement.FILL) != null)
        {
            pw.print(" fill=\"" + re.getFill() + "\" ");
        }

        pw.println("/>");
    }

    void writeXMLCrosstabRowGroups(Vector crosstabRowGroups, Vector crosstabElements, PrintWriter pw) {

        for (int i=0; i<crosstabRowGroups.size(); ++i)
        {
           CrosstabRowGroup group = (CrosstabRowGroup)crosstabRowGroups.elementAt(i);

           pw.print("\t\t\t\t\t<rowGroup");
           pw.print(" name=\"" + group.getName() + "\"");
           pw.print(" width=\""+ group.getWidth() + "\"");
           if (!group.getTotalPosition().equals("None"))  pw.print(" totalPosition=\""+ group.getTotalPosition() + "\"");
           if (!group.getHeaderPosition().equals("Top"))  pw.print(" headerPosition=\""+ group.getHeaderPosition() + "\"");
           pw.println(">");

           // BUCKET
           writeXMLBucket(group, pw);

           if (group.getHeaderCell().getHeight() > 0 && group.getHeaderCell().getWidth() > 0)
           {
               pw.println("\t\t\t\t\t\t<crosstabRowHeader>");
               writeXMLCrosstabCellcontents(group.getHeaderCell(), crosstabElements, pw, "\t\t\t\t\t\t\t");
               pw.println("\t\t\t\t\t\t</crosstabRowHeader>");
           }

           if (group.getTotalCell().getHeight() > 0 && group.getTotalCell().getWidth() > 0)
           {
               pw.println("\t\t\t\t\t\t<crosstabTotalRowHeader>");
               writeXMLCrosstabCellcontents(group.getTotalCell(), crosstabElements, pw, "\t\t\t\t\t\t\t");
               pw.println("\t\t\t\t\t\t</crosstabTotalRowHeader>");
           }

           pw.println("\t\t\t\t\t</rowGroup>");
        }

    }

    void writeXMLCrosstabColumnGroups(Vector crosstabColumnGroups, Vector crosstabElements, PrintWriter pw) {

        for (int i=0; i<crosstabColumnGroups.size(); ++i)
        {
           CrosstabColumnGroup group = (CrosstabColumnGroup)crosstabColumnGroups.elementAt(i);

           pw.print("\t\t\t\t\t<columnGroup");
           pw.print(" name=\"" + group.getName() + "\"");
           pw.print(" height=\""+ group.getHeight() + "\"");
           if (!group.getTotalPosition().equals("None"))  pw.print(" totalPosition=\""+ group.getTotalPosition() + "\"");
           if (!group.getHeaderPosition().equals("Left"))  pw.print(" headerPosition=\""+ group.getHeaderPosition() + "\"");
           pw.println(">");

           // BUCKET
           writeXMLBucket(group, pw);

           if (group.getHeaderCell().getHeight() > 0 && group.getHeaderCell().getWidth() > 0)
           {
               pw.println("\t\t\t\t\t\t<crosstabColumnHeader>");
               writeXMLCrosstabCellcontents(group.getHeaderCell(), crosstabElements, pw, "\t\t\t\t\t\t\t");
               pw.println("\t\t\t\t\t\t</crosstabColumnHeader>");
           }

           if (group.getTotalCell().getHeight() > 0 && group.getTotalCell().getWidth() > 0)
           {
               pw.println("\t\t\t\t\t\t<crosstabTotalColumnHeader>");
               writeXMLCrosstabCellcontents(group.getTotalCell(), crosstabElements, pw, "\t\t\t\t\t\t\t");
               pw.println("\t\t\t\t\t\t</crosstabTotalColumnHeader>");
           }

           pw.println("\t\t\t\t\t</columnGroup>");
        }

    }

    void writeXMLCrosstabCellcontents(CrosstabCell cell, Vector elements, PrintWriter pw, String tabs) {

        pw.print("\t\t\t\t\t\t<cellContents");
        if (cell.getBackcolor() != null)  pw.print(" backcolor=\""+ writeEncodedColor( cell.getBackcolor() ) + "\"");
        if ( CompatibilitySupport.version >= CompatibilitySupport.JR120)
        {
            pw.print(" mode=\""+ cell.getMode() + "\"");
        }

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR120 && cell.getStyle() != null )
        {
            pw.print(" style=\""+ cell.getStyle() + "\"");
        }

        pw.println(">");

        if (cell.getBox() != null)
        {
            writeXMLBox(cell.getBox(), pw);
        }

        for (int i=0; i<elements.size(); ++i)
        {
            ReportElement re = (ReportElement)elements.elementAt(i);
            if (re.getCell() == cell)
            {
                writeXMLReportElement(re, pw);
            }
        }
        pw.println("\t\t\t\t\t\t</cellContents>");
    }

    void writeXMLCrosstabCells(Vector cells, Vector elements, PrintWriter pw, String tabs) {

        for (int i=0; i<cells.size(); ++i)
        {
            CrosstabCell cell = (CrosstabCell)cells.elementAt(i);
            // Write the cell only if it is a DETAIL cell and if at least one of this expressions is true:
            // height>0 and width > 0
            // is the pure detail cell (columnTotal and RowTotal are blank)
            if (cell.getType() == cell.DETAIL_CELL)
                //&& ( (cell.getWidth() > 0 && cell.getHeight()>0) ||
                //  (cell.getColumnTotalGroup().equals("") && cell.getRowTotalGroup().equals(""))))
            {
                pw.print(tabs + "<crosstabCell");
                pw.print(" width=\"" + cell.getWidth()+"\"");
                pw.print(" height=\"" + cell.getHeight()+"\"");
                if (cell.getRowTotalGroup().trim().length() > 0)  pw.print(" rowTotalGroup=\""+ cell.getRowTotalGroup() + "\"");
                if (cell.getColumnTotalGroup().trim().length() > 0)  pw.print(" columnTotalGroup=\""+ cell.getColumnTotalGroup() + "\"");
                pw.println(">");
                writeXMLCrosstabCellcontents(cell, elements, pw, tabs+"\t");

                pw.println(tabs + "</crosstabCell>");
            }
        }
    }

    void writeXMLBucket(CrosstabGroup group, PrintWriter pw) {

           pw.print("\t\t\t\t\t\t<bucket");
           if (!group.getBucketOrder().equals("Ascending"))  pw.print(" order=\""+ group.getBucketOrder() + "\"");
           pw.println(">");
           pw.print("\t\t\t\t\t\t\t<bucketExpression");
           pw.print(" class=\""+ group.getBucketExpressionClass() + "\"");
           pw.print(">");
           pw.print(getCDATAString(group.getBucketExpression(),8));
           pw.println("</bucketExpression>");

           if (group.getBucketComparatorExpression().trim().length() > 0)
           {
               pw.print("\t\t\t\t\t\t\t<comparatorExpression>");
               pw.print(getCDATAString(group.getBucketComparatorExpression(),8));
               pw.println("</comparatorExpression>");
           }
           pw.println("\t\t\t\t\t\t</bucket>");
    }

    void writeXMLTextElementElement(TextReportElement re, PrintWriter pw) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(0);


        pw.print("\t\t\t\t\t<textElement");

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getAlign()+"",Style.ATTRIBUTE_hAlign,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue( re.ALIGN) != null)
        {
            pw.print(" textAlignment=\""+ re.getAlign()  +"\"");
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getVerticalAlign()+"",Style.ATTRIBUTE_vAlign,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue( re.VERTICAL_ALIGN) != null)
        {
            pw.print(" verticalAlignment=\""+ re.getVerticalAlign()  +"\"");
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getRotate()+"",Style.ATTRIBUTE_rotation,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue( re.ROTATE) != null)
        {
            pw.print(" rotation=\""+ re.getRotate() +"\"");
        }

        // From JasperReport 0.5.3
        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isIsStyledText()+"",Style.ATTRIBUTE_isStyledText,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue( re.IS_STYLED_TEXT) != null)
        {
            if (re.isIsStyledText()) {
                pw.print(" isStyledText=\"true\"");
            }
        }

        //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getLineSpacing()+"",Style.ATTRIBUTE_lineSpacing,re.getStyle(), getDefaultStyle()))
        if (re.getPropertyValue( re.LINE_SPACING) != null)
        {
            pw.print(" lineSpacing=\""+ re.getLineSpacing() +"\"");
        }
        // FONT SECTION. This code saves only difference between
        // the report font (if sepcified)...

        pw.println(">");
        pw.print("\t\t\t\t\t\t<font");

        IReportFont font = null;
        if (re.getReportFont() != null && re.getReportFont().length()>0) {
            // Search the right font structure...
            for (int fn =0; fn < getReport().getFonts().size(); ++fn) {
                font = (IReportFont)getReport().getFonts().elementAt(fn);
                if (font != null && font.getReportFont().equals(re.getReportFont())) {
                    break;
                }
                else{
                    font = null;
                }
            }
            pw.print(" reportFont=\""+ re.getReportFont() +"\"");
        }

        if (font == null || !font.getFontName().equals(re.getFontName() ))
        {
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getFontName()+"",Style.ATTRIBUTE_fontName,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.FONT_NAME) != null)
            {
                pw.print(" fontName=\"" + re.getFontName() +"\"");
            }
        }


        if (font == null || !font.getPDFFontName().equalsIgnoreCase(re.getPDFFontName()))
        {
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getPDFFontName()+"",Style.ATTRIBUTE_pdfFontName,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.PDF_FONT_NAME ) != null)
            {
                pw.print(" pdfFontName=\"" + re.getPDFFontName() +"\"");
            }
        }

        //pw.print(" pdfEncoding=\"Times-Roman\"");
        if (font == null || font.getFontSize() != re.getFontSize())
        {
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getFontSize()+"",Style.ATTRIBUTE_fontSize,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.FONT_SIZE) != null)
            {
                pw.print(" size=\"" +  re.getFontSize()  +"\"");
            }
        }

        if (font == null || font.isBold() != re.isBold()) {

            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isBold()+"",Style.ATTRIBUTE_isBold,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.IS_BOLD) != null)
            {
                pw.print(" isBold=\"" + ((re.isBold()) ? "true" : "false")+"\"");
            }
        }

        if (font == null || font.isItalic() != re.isItalic()) {
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isItalic()+"",Style.ATTRIBUTE_isItalic,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.IS_ITALIC) != null)
            {
                pw.print(" isItalic=\"" + ((re.isItalic()) ? "true" : "false")+"\"");
            }
        }

        if (font == null || font.isUnderline() != re.isUnderline()){
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isUnderline()+"",Style.ATTRIBUTE_isUnderline,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.IS_UNDERLINE) != null)
            {
                pw.print(" isUnderline=\"" + ((re.isUnderline() ) ? "true" : "false")+"\"");
            }
        }

        if (font == null || font.isPdfEmbedded() != re.isPdfEmbedded()){
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isPdfEmbedded()+"",Style.ATTRIBUTE_isPdfEmbedded,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.IS_PDF_EMBEDDED) != null)
            {
                pw.print(" isPdfEmbedded =\"" + ((re.isPdfEmbedded() ) ? "true" : "false")+"\"");
            }
        }

        if (font == null || font.getPdfEncoding() == null || font.getPdfEncoding().length() == 0) {
            //if (re.getPdfEncoding() != null && re.getPdfEncoding().length() > 0) {
                //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.getPdfEncoding()+"",Style.ATTRIBUTE_pdfEncoding,re.getStyle(), getDefaultStyle()))
                if (re.getIReportFont().getPropertyValue( IReportFont.PDF_ENCODING) != null &&
                    re.getPdfEncoding().length() > 0)
                {
                    pw.print(" pdfEncoding =\"" + re.getPdfEncoding() +"\"");
                }
            //}
        }
        if (font == null || font.isStrikeTrought() != re.isStrikeTrought()){
            //if (CompatibilitySupport.version < CompatibilitySupport.JR111 ||  !Style.isDefaultValue(re.isStrikeTrought()+"",Style.ATTRIBUTE_isStrikeThrough,re.getStyle(), getDefaultStyle()))
            if (re.getIReportFont().getPropertyValue( IReportFont.IS_STRIKETROUGHT) != null)
            {
                pw.print(" isStrikeThrough=\"" + ((re.isStrikeTrought() ) ? "true" : "false")+"\" ");
            }
        }

        pw.println("/>");
        pw.println("\t\t\t\t\t</textElement>");
    }

    public static String writeEncodedColor(java.awt.Color c) {
        String nums = "0123456789ABCDEF";
        String s = "#";
        s += nums.charAt( c.getRed()/16 );
        s += nums.charAt( c.getRed()%16 );
        s += nums.charAt( c.getGreen()/16 );
        s += nums.charAt( c.getGreen()%16 );
        s += nums.charAt( c.getBlue()/16 );
        s += nums.charAt( c.getBlue()%16 );
        return s;
    }

    private String writeChartExpression(ChartReportElement chartElement) {
        String expression = "";

        expression = "(java.awt.Image)it.businesslogic.ireport.chart.DefaultChartFactory.drawChart(new String[]{";

        Properties props = chartElement.getProps();
        Iterator keys = props.keySet().iterator();
        int i=0;
        while (keys.hasNext())
        {
            String key = ""+keys.next();
            String val = props.getProperty(key);
            if (i>0) expression +=",";
            val = Misc.string_replace("\\\"", "\"", val );
            expression += "\"" + key + "=" + val +"\"";
            i++;
        }
        expression += "}, (it.businesslogic.ireport.IReportScriptlet)$P{REPORT_SCRIPTLET})";

        return expression;
    }

    private void writeChartElement(ChartReportElement2 chartElement, PrintWriter pw) {

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR100 )
        {
            String chartType = "";
            if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.PieChart) chartType = "pieChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.Pie3DChart) chartType = "pie3DChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.BarChart) chartType = "barChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.Bar3DChart) chartType = "bar3DChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.XYBarChart) chartType = "xyBarChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.StackedBarChart) chartType = "stackedBarChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.StackedBar3DChart) chartType = "stackedBar3DChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.LineChart) chartType = "lineChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.XYLineChart) chartType = "xyLineChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.AreaChart) chartType = "areaChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.StackedAreaChart) chartType = "stackedAreaChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.XYAreaChart) chartType = "xyAreaChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.ScatterChart) chartType = "scatterChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.BubbleChart) chartType = "bubbleChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.TimeSeriesChart) chartType = "timeSeriesChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.HighLowChart) chartType = "highLowChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.CandlestickChart) chartType = "candlestickChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.MeterChart) chartType = "meterChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.ThermometerChart) chartType = "thermometerChart";
            else if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.MultiAxisChart) chartType = "multiAxisChart";

            if (chartElement.getChart() instanceof it.businesslogic.ireport.chart.StackedAreaChart &&
                CompatibilitySupport.version < CompatibilitySupport.JR131)
            {
                return;
            }

            pw.println("\t\t\t\t<" + chartType + ">");
            writeChartNode(chartElement, pw);
            writeDataset(chartElement, pw );
            writePlot(chartElement, pw );
            pw.println("\t\t\t\t</" + chartType + ">");
        }
    }


    private void writeChartNode(ChartReportElement2 chartElement, PrintWriter pw)
    {
        pw.print("\t\t\t\t\t<chart ");
        if (!chartElement.getChart().isShowLegend()) pw.print("isShowLegend=\"false\" ");
        if (!chartElement.getEvaluationTime().equals("Now")) pw.print("evaluationTime=\"" + chartElement.getEvaluationTime() + "\" ");
        if (!chartElement.getEvaluationGroup().equals("")) pw.print("evaluationGroup=\"" + chartElement.getEvaluationGroup() + "\" ");
        if (!chartElement.getChart().getHyperlinkType().equals("None")) pw.print(" hyperlinkType=\"" + chartElement.getChart().getHyperlinkType() +"\" ");
        if (!chartElement.getChart().getHyperlinkTarget().equals("None")) pw.print(" hyperlinkTarget=\"" + chartElement.getChart().getHyperlinkTarget() +"\" ");
        if (chartElement.getChart().getBookmarkLevel() != 0) pw.print(" bookmarkLevel=\"" + chartElement.getChart().getBookmarkLevel() +"\" ");
        if (chartElement.getChart().getCustomizerClass().length() != 0) pw.print(" customizerClass=\"" + chartElement.getChart().getCustomizerClass()  +"\" ");
        pw.println(">");
        writeXMLReportElementElement(chartElement, pw);
        writeXMLBox(chartElement.getBox(), pw);
        writeChartTitleNode(chartElement, pw);
        writeChartSubTitleNode(chartElement, pw);

        //if (chartElement.getChart().isShowLegend())
        if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
        {
            writeChartLegendNode(chartElement, pw);
        }

        writeHyperLinkExpressions(chartElement.getChart(), pw);
        pw.println("\t\t\t\t\t</chart>");
    }

    private void writeSectionItemHyperLinkExpressions(SectionItemHyperlink hlink, boolean isItem, PrintWriter pw)
    {
        String tag = "sectionHyperlink";
        if (isItem) tag = "itemHyperlink";

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR126 )
        {
            pw.print("\t\t\t\t<" + tag +" ");
            if (hlink.getHyperlinkType() != null &&
                !hlink.getHyperlinkType().equals("None") &&
                hlink.getHyperlinkType().length() > 0) {
                pw.print("  hyperlinkType=\""+ hlink.getHyperlinkType() +"\"");
            }
            if (hlink.getHyperlinkTarget() != null &&
                !hlink.getHyperlinkTarget().equals("Self")) {
                pw.print("  hyperlinkTarget=\""+ hlink.getHyperlinkTarget() +"\"");
            }
            pw.println(">");

            if (hlink.getHyperlinkType() != null && hlink.getHyperlinkType().length() > 0) {
                if ( hlink.getHyperlinkReferenceExpression().length() > 0 ) {
                    pw.println("\t\t\t\t\t\t<hyperlinkReferenceExpression>"+getCDATAString(hlink.getHyperlinkReferenceExpression(),6) +"</hyperlinkReferenceExpression>");
                }

                if ( hlink.getHyperlinkAnchorExpression().length() > 0 ) {
                    pw.println("\t\t\t\t\t\t<hyperlinkAnchorExpression>"+getCDATAString(hlink.getHyperlinkAnchorExpression(),6)+"</hyperlinkAnchorExpression>");
                }

                if ( hlink.getHyperlinkPageExpression().length() > 0 ) {
                    pw.println("\t\t\t\t\t\t<hyperlinkPageExpression>"+getCDATAString(hlink.getHyperlinkPageExpression(),6) +"</hyperlinkPageExpression>");
                }

                if ( hlink.getHyperlinkTooltipExpression().length() > 0 ) {
                    pw.println("\t\t\t\t\t\t<hyperlinkTooltipExpression>"+getCDATAString(hlink.getHyperlinkTooltipExpression(),6) +"</hyperlinkTooltipExpression>");
                }

                writeXMLHyperlinkParameters( hlink.getHyperlinkParameters(), pw, "\t\t\t\t\t\t");
            }

            pw.println("\t\t\t\t</" + tag +">");
        }
    }

    private void writeHyperLinkExpressions(HyperLinkableReportElement hlink, PrintWriter pw)
    {
        if (hlink.getAnchorNameExpression() != null && hlink.getAnchorNameExpression().length() > 0) {
                pw.println("\t\t\t\t\t\t<anchorNameExpression>"+getCDATAString(hlink.getAnchorNameExpression(),6)+"</anchorNameExpression>");
        }
        if (hlink.getHyperlinkType() != null && hlink.getHyperlinkType().length() > 0) {

            if ( hlink.getHyperlinkReferenceExpression().length() > 0 ) {
                pw.println("\t\t\t\t\t\t<hyperlinkReferenceExpression>"+getCDATAString(hlink.getHyperlinkReferenceExpression(),6) +"</hyperlinkReferenceExpression>");
            }

            if ( hlink.getHyperlinkAnchorExpression().length() > 0 ) {
                pw.println("\t\t\t\t\t\t<hyperlinkAnchorExpression>"+getCDATAString(hlink.getHyperlinkAnchorExpression(),6)+"</hyperlinkAnchorExpression>");
            }

            if ( hlink.getHyperlinkPageExpression().length() > 0 ) {
                pw.println("\t\t\t\t\t\t<hyperlinkPageExpression>"+getCDATAString(hlink.getHyperlinkPageExpression(),6) +"</hyperlinkPageExpression>");
            }


            if ( CompatibilitySupport.version >= CompatibilitySupport.JR126 )
            {
                if ( hlink.getTooltipExpression().length() > 0 ) {
                    pw.println("\t\t\t\t\t\t<hyperlinkTooltipExpression>"+getCDATAString(hlink.getTooltipExpression(),6) +"</hyperlinkTooltipExpression>");
                }
                writeXMLHyperlinkParameters( hlink.getLinkParameters(), pw, "\t\t\t\t\t\t");
            }
        }
    }

    private void writeChartTitleNode(ChartReportElement2 chartElement, PrintWriter pw)
    {
        if (chartElement.getChart().getTitle().getTitleExpression().trim().length() > 0)
        {
            pw.print("\t\t\t\t\t\t<chartTitle ");
            if (!chartElement.getChart().getTitle().getPosition().equals("Top")) pw.print("position=\"" + chartElement.getChart().getTitle().getPosition() + "\" ");
            if (chartElement.getChart().getTitle().getColor() != null) pw.print("color=\"" +  writeEncodedColor(chartElement.getChart().getTitle().getColor() ) +  "\" ");
            pw.println(">");
            writeFontElement(chartElement.getChart().getTitle().getFont(), pw );
            pw.println("\t\t\t\t\t\t\t\t<titleExpression>" + getCDATAString(chartElement.getChart().getTitle().getTitleExpression(),8 ) + "</titleExpression>");
            pw.println("\t\t\t\t\t</chartTitle>");
        }
    }


    private void writeChartSubTitleNode(ChartReportElement2 chartElement, PrintWriter pw)
    {
        if (chartElement.getChart().getSubTitle().getTitleExpression().trim().length() > 0)
        {
            pw.print("\t\t\t\t\t\t<chartSubtitle ");
            if (chartElement.getChart().getSubTitle().getColor() != null) pw.print("color=\"" +  writeEncodedColor(chartElement.getChart().getSubTitle().getColor() ) +  "\" ");
            pw.println(">");
            writeFontElement(chartElement.getChart().getSubTitle().getFont(), pw );
            pw.println("\t\t\t\t\t\t\t\t<subtitleExpression>" + getCDATAString(chartElement.getChart().getSubTitle().getTitleExpression(),8 ) + "</subtitleExpression>");
            pw.println("\t\t\t\t\t</chartSubtitle>");
        }
    }

    private void writeChartLegendNode(ChartReportElement2 chartElement, PrintWriter pw)
    {
        if (chartElement.getChart().isShowLegend())
        {
            pw.print("\t\t\t\t\t\t<chartLegend ");
            if (chartElement.getChart().getLegend().getTextColor() != null) pw.print("textColor=\"" +  writeEncodedColor(chartElement.getChart().getLegend().getTextColor() ) +  "\" ");
            if (chartElement.getChart().getLegend().getBackgroundColor() != null) pw.print("backgroundColor=\"" +  writeEncodedColor(chartElement.getChart().getLegend().getBackgroundColor() ) +  "\" ");
            pw.println(">");
            writeFontElement(chartElement.getChart().getLegend().getFont(), pw );
            pw.println("\t\t\t\t\t</chartLegend>");
        }
    }

    private void writeDataset(ChartReportElement2 chartElement, PrintWriter pw)
    {
        if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.PieDataset)
        {
            it.businesslogic.ireport.chart.PieDataset pdataset = (it.businesslogic.ireport.chart.PieDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<pieDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            pw.println("\t\t\t\t\t\t<keyExpression>" + getCDATAString(pdataset.getKeyExpression() ,6 ) + "</keyExpression>");
            pw.println("\t\t\t\t\t\t<valueExpression>" + getCDATAString(pdataset.getValueExpression() ,6 ) + "</valueExpression>");
            if (pdataset.getLabelExpression().trim().length() > 0)
            {
                pw.println("\t\t\t\t\t\t<labelExpression>" + getCDATAString(pdataset.getLabelExpression() ,6 ) + "</labelExpression>");
            }
            writeSectionItemHyperLinkExpressions(pdataset.getSectionHyperLink(), false, pw);
            pw.println("\t\t\t\t\t</pieDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.CategoryDataset)
        {
            it.businesslogic.ireport.chart.CategoryDataset pdataset = (it.businesslogic.ireport.chart.CategoryDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<categoryDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            for (int i=0; i<pdataset.getCategorySeries().size(); ++i)
            {
                writeCategorySeries( (CategorySeries)pdataset.getCategorySeries().elementAt(i) ,  pw);
            }
            pw.println("\t\t\t\t\t</categoryDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.TimePeriodDataset)
        {
            it.businesslogic.ireport.chart.TimePeriodDataset pdataset = (it.businesslogic.ireport.chart.TimePeriodDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<timePeriodDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            for (int i=0; i<pdataset.getTimePeriodSeries().size(); ++i)
            {
                writeTimePeriodSeries( (TimePeriodSeries)pdataset.getTimePeriodSeries().elementAt(i) ,  pw);
            }
            pw.println("\t\t\t\t\t</timePeriodDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.TimeSeriesDataset)
        {
            it.businesslogic.ireport.chart.TimeSeriesDataset pdataset = (it.businesslogic.ireport.chart.TimeSeriesDataset)chartElement.getChart().getDataset();
            pw.print("\t\t\t\t\t<timeSeriesDataset");
            if (!pdataset.getTimePeriod().equals("Day")) pw.println(" timePeriod=\"" + pdataset.getTimePeriod() + "\"");
            pw.println(">");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            for (int i=0; i<pdataset.getTimeSeries().size(); ++i)
            {
                writeTimeSeries((TimeSeries)pdataset.getTimeSeries().elementAt(i) ,  pw);
            }
            pw.println("\t\t\t\t\t</timeSeriesDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.XYDataset)
        {
            it.businesslogic.ireport.chart.XYDataset pdataset = (it.businesslogic.ireport.chart.XYDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<xyDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            for (int i=0; i<pdataset.getXYSeries().size(); ++i)
            {
                writeXYSeries((XYSeries)pdataset.getXYSeries().elementAt(i) ,  pw);
            }
            pw.println("\t\t\t\t\t</xyDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.XYZDataset)
        {
            it.businesslogic.ireport.chart.XYZDataset pdataset = (it.businesslogic.ireport.chart.XYZDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<xyzDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            for (int i=0; i<pdataset.getXYZSeries().size(); ++i)
            {
                writeXYZSeries((XYZSeries)pdataset.getXYZSeries().elementAt(i) ,  pw);
            }
            pw.println("\t\t\t\t\t</xyzDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.HighLowDataset)
        {
            it.businesslogic.ireport.chart.HighLowDataset pdataset = (it.businesslogic.ireport.chart.HighLowDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<highLowDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            pw.println("\t\t\t\t\t\t<seriesExpression>" + getCDATAString(pdataset.getSeriesExpression() ,6 ) + "</seriesExpression>");
            pw.println("\t\t\t\t\t\t<dateExpression>" + getCDATAString(pdataset.getDateExpression() ,6 ) + "</dateExpression>");
            pw.println("\t\t\t\t\t\t<highExpression>" + getCDATAString(pdataset.getHighExpression() ,6 ) + "</highExpression>");
            pw.println("\t\t\t\t\t\t<lowExpression>" + getCDATAString(pdataset.getLowExpression() ,6 ) + "</lowExpression>");
            pw.println("\t\t\t\t\t\t<openExpression>" + getCDATAString(pdataset.getOpenExpression() ,6 ) + "</openExpression>");
            pw.println("\t\t\t\t\t\t<closeExpression>" + getCDATAString(pdataset.getCloseExpression() ,6 ) + "</closeExpression>");
            pw.println("\t\t\t\t\t\t<volumeExpression>" + getCDATAString(pdataset.getVolumeExpression() ,6 ) + "</volumeExpression>");
            writeSectionItemHyperLinkExpressions(pdataset.getItemHyperLink(), true, pw);
            pw.println("\t\t\t\t\t</highLowDataset>");
        }
        else if (chartElement.getChart().getDataset() instanceof it.businesslogic.ireport.chart.ValueDataset)
        {
            it.businesslogic.ireport.chart.ValueDataset pdataset = (it.businesslogic.ireport.chart.ValueDataset)chartElement.getChart().getDataset();
            pw.println("\t\t\t\t\t<valueDataset>");
            writeDatasetDataset( chartElement.getChart().getDataset(),  pw);
            pw.println("\t\t\t\t\t\t<valueExpression>" + getCDATAString(pdataset.getValueExpression() ,6 ) + "</valueExpression>");
            pw.println("\t\t\t\t\t</valueDataset>");
        }
    }

    private void writeCategorySeries(CategorySeries cs, PrintWriter pw)
    {
        pw.println("\t\t\t\t\t\t<categorySeries>");
        pw.println("\t\t\t\t\t\t\t<seriesExpression>" + getCDATAString( cs.getSeriesExpression() ,7 ) + "</seriesExpression>");
        pw.println("\t\t\t\t\t\t\t<categoryExpression>" + getCDATAString( cs.getCategoryExpression() ,7 ) + "</categoryExpression>");
        pw.println("\t\t\t\t\t\t\t<valueExpression>" + getCDATAString( cs.getValueExpression() ,7 ) + "</valueExpression>");
        if (cs.getLabelExpression().trim().length() != 0)
            pw.println("\t\t\t\t\t\t\t<labelExpression>" + getCDATAString( cs.getLabelExpression() ,7 ) + "</labelExpression>");
        writeSectionItemHyperLinkExpressions(cs.getSectionItemHyperlink(), true, pw);
        pw.println("\t\t\t\t\t\t</categorySeries>");
    }

    private void writeXYSeries(XYSeries cs, PrintWriter pw)
    {
        pw.println("\t\t\t\t\t\t<xySeries>");
        pw.println("\t\t\t\t\t\t\t<seriesExpression>" + getCDATAString( cs.getSeriesExpression() ,7 ) + "</seriesExpression>");
        pw.println("\t\t\t\t\t\t\t<xValueExpression>" + getCDATAString( cs.getXValueExpression() ,7 ) + "</xValueExpression>");
        pw.println("\t\t\t\t\t\t\t<yValueExpression>" + getCDATAString( cs.getYValueExpression() ,7 ) + "</yValueExpression>");
        if (cs.getLabelExpression().trim().length() != 0)
            pw.println("\t\t\t\t\t\t\t<labelExpression>" + getCDATAString( cs.getLabelExpression() ,7 ) + "</labelExpression>");
        writeSectionItemHyperLinkExpressions(cs.getSectionItemHyperlink(), true, pw);
        pw.println("\t\t\t\t\t\t</xySeries>");
    }

    private void writeXYZSeries(XYZSeries cs, PrintWriter pw)
    {
        pw.println("\t\t\t\t\t\t<xyzSeries>");
        pw.println("\t\t\t\t\t\t\t<seriesExpression>" + getCDATAString( cs.getSeriesExpression() ,7 ) + "</seriesExpression>");
        pw.println("\t\t\t\t\t\t\t<xValueExpression>" + getCDATAString( cs.getXValueExpression() ,7 ) + "</xValueExpression>");
        pw.println("\t\t\t\t\t\t\t<yValueExpression>" + getCDATAString( cs.getYValueExpression() ,7 ) + "</yValueExpression>");
        if (cs.getZValueExpression().trim().length() != 0)
            pw.println("\t\t\t\t\t\t\t<zValueExpression>" + getCDATAString( cs.getZValueExpression() ,7 ) + "</zValueExpression>");
        writeSectionItemHyperLinkExpressions(cs.getSectionItemHyperlink(), true, pw);
        pw.println("\t\t\t\t\t\t</xyzSeries>");
    }

    private void writeTimePeriodSeries(TimePeriodSeries cs, PrintWriter pw)
    {
        pw.println("\t\t\t\t\t\t<timePeriodSeries>");
        pw.println("\t\t\t\t\t\t\t<seriesExpression>" + getCDATAString( cs.getSeriesExpression() ,7 ) + "</seriesExpression>");
        pw.println("\t\t\t\t\t\t\t<startDateExpression>" + getCDATAString( cs.getStartDateExpression() ,7 ) + "</startDateExpression>");
        pw.println("\t\t\t\t\t\t\t<endDateExpression>" + getCDATAString( cs.getEndDateExpression() ,7 ) + "</endDateExpression>");
        pw.println("\t\t\t\t\t\t\t<valueExpression>" + getCDATAString( cs.getValueExpression() ,7 ) + "</valueExpression>");
        if (cs.getLabelExpression().trim().length() != 0)
            pw.println("\t\t\t\t\t\t\t<labelExpression>" + getCDATAString( cs.getLabelExpression() ,7 ) + "</labelExpression>");
        writeSectionItemHyperLinkExpressions(cs.getSectionItemHyperlink(), true, pw);
        pw.println("\t\t\t\t\t\t</timePeriodSeries>");
    }

    private void writeTimeSeries(TimeSeries cs, PrintWriter pw)
    {
        pw.println("\t\t\t\t\t\t<timeSeries>");
        pw.println("\t\t\t\t\t\t\t<seriesExpression>" + getCDATAString( cs.getSeriesExpression() ,7 ) + "</seriesExpression>");
        pw.println("\t\t\t\t\t\t\t<timePeriodExpression>" + getCDATAString( cs.getTimePeriodExpression() ,7 ) + "</timePeriodExpression>");
        pw.println("\t\t\t\t\t\t\t<valueExpression>" + getCDATAString( cs.getValueExpression() ,7 ) + "</valueExpression>");
        if (cs.getLabelExpression().trim().length() != 0)
            pw.println("\t\t\t\t\t\t\t<labelExpression>" + getCDATAString( cs.getLabelExpression() ,7 ) + "</labelExpression>");
        writeSectionItemHyperLinkExpressions(cs.getSectionItemHyperlink(), true, pw);
        pw.println("\t\t\t\t\t\t</timeSeries>");
    }



    private void writeDatasetDataset(Dataset dataset, PrintWriter pw)
    {
        pw.print("\t\t\t\t\t\t<dataset ");
        if (!dataset.getResetType().equals("Report")) pw.print("resetType=\"" + dataset.getResetType() +  "\" ");
        if (!dataset.getResetGroup().equals("")) pw.print("resetGroup=\"" + dataset.getResetGroup() + "\" ");
        if (!dataset.getIncrementType().equals("None")) pw.print("incrementType=\"" + dataset.getIncrementType() +  "\" ");
        if (!dataset.getIncrementGroup().equals("")) pw.print("incrementGroup=\"" + dataset.getIncrementGroup() + "\" ");
        pw.println(">");

        if ( CompatibilitySupport.version >= CompatibilitySupport.JR125 && dataset.getIncrementWhenExpression().length() > 0)
        {
            pw.println("\t\t\t\t\t\t\t<incrementWhenExpression>" + getCDATAString(dataset.getIncrementWhenExpression().trim(), 9 ) + "</incrementWhenExpression>");

        }
        if ( CompatibilitySupport.version >= CompatibilitySupport.JR110 )
        {
            if (dataset.getSubDataset() != null)
            {
                pw.println("\t\t\t\t\t\t\t<datasetRun subDataset=\"" + dataset.getSubDataset().getName()  +"\">");
                if (dataset.getParametersMapExpression() != null &&
                    dataset.getParametersMapExpression().trim().length() > 0)
                {
                    pw.println("\t\t\t\t\t\t\t\t<parametersMapExpression>" + getCDATAString(dataset.getParametersMapExpression().trim(), 9 ) + "</parametersMapExpression>");
                }


                 Enumeration e = dataset.getSubreportParameters().elements();
                while (e.hasMoreElements()) {
                    JRSubreportParameter je = (JRSubreportParameter)e.nextElement();
                    pw.println("\t\t\t\t\t\t\t\t<datasetParameter  name=\""+ je.getName() +"\">");
                    pw.print("\t\t\t\t\t\t\t\t\t<datasetParameterExpression");
                     pw.println(">"+getCDATAString(je.getExpression(),9)+"</datasetParameterExpression>");
                    pw.println("\t\t\t\t\t\t\t\t</datasetParameter>");
                }

                if (dataset.isUseConnection() && dataset.getConnectionExpression().trim().length() > 0 ) {
                    pw.print("\t\t\t\t\t\t\t\t<connectionExpression");
                    pw.println(">"+getCDATAString(dataset.getConnectionExpression(),8 )+ "</connectionExpression>");
                }
                else if (!dataset.isUseConnection()  && dataset.getDataSourceExpression().length() > 0) {
                    pw.print("\t\t\t\t\t<dataSourceExpression");
                    pw.println(">"+getCDATAString(dataset.getDataSourceExpression(),8 )+"</dataSourceExpression>");
                }

                pw.println("\t\t\t\t\t\t\t</datasetRun>");
            }
        }

        pw.println("\t\t\t\t\t\t</dataset>");
    }

    private void writePlot(ChartReportElement2 chartElement, PrintWriter pw)
    {
        if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.PiePlot)
        {
            it.businesslogic.ireport.chart.PiePlot plot = (it.businesslogic.ireport.chart.PiePlot)chartElement.getChart().getPlot();
            pw.println("\t\t\t\t\t<piePlot ");
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR200 && plot.isCircular())
            {
                pw.print("isCircular=\"" + plot.isCircular() + "\" ");
            }
            pw.println(">");
            writePlotPlot( chartElement,  pw);
            pw.println("\t\t\t\t\t</piePlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.Pie3DPlot)
        {
            it.businesslogic.ireport.chart.Pie3DPlot plot = (it.businesslogic.ireport.chart.Pie3DPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<pie3DPlot ");
            
            if ( CompatibilitySupport.version >= CompatibilitySupport.JR200 && plot.isCircular())
            {
                 pw.print("isCircular=\"" + plot.isCircular() + "\" ");
            }
            
            if (plot.getDepthFactor() != 0.2) pw.print("depthFactor=\"" + plot.getDepthFactor() +  "\" ");
            pw.println(">");
            writePlotPlot( chartElement,  pw);
            pw.println("\t\t\t\t\t</pie3DPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.BarPlot)
        {
            it.businesslogic.ireport.chart.BarPlot plot = (it.businesslogic.ireport.chart.BarPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<barPlot ");
            if (plot.isShowLabels()) pw.print("isShowLabels=\"" + plot.isShowLabels() +  "\" ");
            if (!plot.isShowTickMarks()) pw.print("isShowTickMarks=\"" + plot.isShowTickMarks() +  "\" ");
            if (!plot.isShowTickLabels()) pw.print("isShowTickLabels=\"" + plot.isShowTickLabels() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getCategoryAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<categoryAxisLabelExpression>" + getCDATAString(plot.getCategoryAxisLabelExpression() ,6 ) + "</categoryAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</barPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.Bar3DPlot)
        {
            it.businesslogic.ireport.chart.Bar3DPlot plot = (it.businesslogic.ireport.chart.Bar3DPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<bar3DPlot ");
            if (plot.isShowLabels()) pw.print("isShowLabels=\"" + plot.isShowLabels() +  "\" ");
            if (plot.getXOffset() != org.jfree.chart.renderer.category.BarRenderer3D.DEFAULT_X_OFFSET) pw.print("xOffset=\"" + plot.getXOffset() +  "\" ");
            if (plot.getYOffset() != org.jfree.chart.renderer.category.BarRenderer3D.DEFAULT_Y_OFFSET) pw.print("yOffset=\"" + plot.getYOffset() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getCategoryAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<categoryAxisLabelExpression>" + getCDATAString(plot.getCategoryAxisLabelExpression() ,6 ) + "</categoryAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");


            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</bar3DPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.LinePlot)
        {
            it.businesslogic.ireport.chart.LinePlot plot = (it.businesslogic.ireport.chart.LinePlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<linePlot ");
            if (!plot.isShowLines()) pw.print("isShowLines=\"" + plot.isShowLines() +  "\" ");
            if (!plot.isShowShapes()) pw.print("isShowShapes=\"" + plot.isShowShapes() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getCategoryAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<categoryAxisLabelExpression>" + getCDATAString(plot.getCategoryAxisLabelExpression() ,6 ) + "</categoryAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</linePlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.AreaPlot)
        {
            it.businesslogic.ireport.chart.AreaPlot plot = (it.businesslogic.ireport.chart.AreaPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<areaPlot>");

            writePlotPlot( chartElement,  pw);

            if (plot.getCategoryAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<categoryAxisLabelExpression>" + getCDATAString(plot.getCategoryAxisLabelExpression() ,6 ) + "</categoryAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("categoryAxisFormat", plot.getCategoryAxisFormat() ,pw);
            }


            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</areaPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.ScatterPlot)
        {
            it.businesslogic.ireport.chart.ScatterPlot plot = (it.businesslogic.ireport.chart.ScatterPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<scatterPlot ");
            if (!plot.isShowLines()) pw.print("isShowLines=\"" + plot.isShowLines() +  "\" ");
            if (!plot.isShowShapes()) pw.print("isShowShapes=\"" + plot.isShowShapes() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getXAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<xAxisLabelExpression>" + getCDATAString(plot.getXAxisLabelExpression() ,6 ) + "</xAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("xAxisFormat", plot.getXAxisFormat() ,pw);
            }

            if (plot.getYAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<yAxisLabelExpression>" + getCDATAString(plot.getYAxisLabelExpression() ,6 ) + "</yAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("yAxisFormat", plot.getYAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</scatterPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.BubblePlot)
        {
            it.businesslogic.ireport.chart.BubblePlot plot = (it.businesslogic.ireport.chart.BubblePlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<bubblePlot ");
            if (!plot.getScaleType().equals("RangeAxis") ) pw.print("scaleType=\"" + plot.getScaleType() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getXAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<xAxisLabelExpression>" + getCDATAString(plot.getXAxisLabelExpression() ,6 ) + "</xAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("xAxisFormat", plot.getXAxisFormat() ,pw);
            }


            if (plot.getYAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<yAxisLabelExpression>" + getCDATAString(plot.getYAxisLabelExpression() ,6 ) + "</yAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("yAxisFormat", plot.getYAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</bubblePlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.TimeSeriesPlot)
        {
            it.businesslogic.ireport.chart.TimeSeriesPlot plot = (it.businesslogic.ireport.chart.TimeSeriesPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<timeSeriesPlot ");
            if (!plot.isShowLines()) pw.print("isShowLines=\"" + plot.isShowLines() +  "\" ");
            if (!plot.isShowShapes()) pw.print("isShowShapes=\"" + plot.isShowShapes() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getTimeAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<timeAxisLabelExpression>" + getCDATAString(plot.getTimeAxisLabelExpression() ,6 ) + "</timeAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("timeAxisFormat", plot.getTimeAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</timeSeriesPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.HighLowPlot)
        {
            it.businesslogic.ireport.chart.HighLowPlot plot = (it.businesslogic.ireport.chart.HighLowPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<highLowPlot ");
            if (!plot.isShowCloseTicks()) pw.print("isShowCloseTicks=\"" + plot.isShowCloseTicks() +  "\" ");
            if (!plot.isShowOpenTicks()) pw.print("isShowOpenTicks=\"" + plot.isShowOpenTicks() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getTimeAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<timeAxisLabelExpression>" + getCDATAString(plot.getTimeAxisLabelExpression() ,6 ) + "</timeAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("timeAxisFormat", plot.getTimeAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }

            pw.println("\t\t\t\t\t</highLowPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.CandlestickPlot)
        {
            it.businesslogic.ireport.chart.CandlestickPlot plot = (it.businesslogic.ireport.chart.CandlestickPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<candlestickPlot ");
            if (!plot.isShowVolume()) pw.print("isShowVolume=\"" + plot.isShowVolume() +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);

            if (plot.getTimeAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<timeAxisLabelExpression>" + getCDATAString(plot.getTimeAxisLabelExpression() ,6 ) + "</timeAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("timeAxisFormat", plot.getTimeAxisFormat() ,pw);
            }

            if (plot.getValueAxisLabelExpression().trim().length() != 0)
                pw.println("\t\t\t\t\t<valueAxisLabelExpression>" + getCDATAString(plot.getValueAxisLabelExpression() ,6 ) + "</valueAxisLabelExpression>");

            if ( CompatibilitySupport.version >= CompatibilitySupport.JR127 )
            {
                writeAxisFormat("valueAxisFormat", plot.getValueAxisFormat() ,pw);
            }
            pw.println("\t\t\t\t\t</candlestickPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.MeterPlot
                && CompatibilitySupport.version >= CompatibilitySupport.JR127)
        {
            it.businesslogic.ireport.chart.MeterPlot plot = (it.businesslogic.ireport.chart.MeterPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<meterPlot ");

            if (plot.getShape() != null && !plot.getShape().equals("pie")) pw.print("shape=\"" + plot.getShape() +  "\" ");
            if (plot.getAngle() != 180) pw.print("angle=\"" + plot.getAngle() +  "\" ");
            if (plot.getUnits() != null && plot.getUnits().trim().length() > 0) pw.print("units=\"" + Misc.xmlEscape( plot.getUnits() ) +  "\" ");
            if (plot.getTickInterval() != 10) pw.print("tickInterval=\"" + plot.getTickInterval() +  "\" ");
            if (plot.getMeterColor() != null) pw.print("meterColor=\"" +  writeEncodedColor(plot.getMeterColor() ) +  "\" ");
            if (plot.getNeedleColor() != null) pw.print("needleColor=\"" +  writeEncodedColor(plot.getNeedleColor() ) +  "\" ");
            if (plot.getTickColor() != null) pw.print("tickColor=\"" +  writeEncodedColor(plot.getTickColor() ) +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);
            writeValueDisplay( plot.getValueDisplay(),  pw);
            writeDataRange( plot.getDataRange(),  pw);

            for (int interval_c = 0; interval_c < plot.getMeterIntervals().size(); ++interval_c)
            {
                writeMeterInterval( (MeterInterval) plot.getMeterIntervals().get(interval_c),  pw);
            }

            pw.println("\t\t\t\t\t</meterPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.ThermometerPlot
                && CompatibilitySupport.version >= CompatibilitySupport.JR127)
        {
            it.businesslogic.ireport.chart.ThermometerPlot plot = (it.businesslogic.ireport.chart.ThermometerPlot)chartElement.getChart().getPlot();
            pw.print("\t\t\t\t\t<thermometerPlot ");

            if (plot.getValueLocation() != null && !plot.getValueLocation().equals("bulb")) pw.print("valueLocation=\"" + plot.getValueLocation() +  "\" ");
            if (plot.isShowValueLines() != false) pw.print("isShowValueLines=\"" + plot.isShowValueLines() +  "\" ");
            if (plot.getMercuryColor() != null) pw.print("mercuryColor=\"" +  writeEncodedColor(plot.getMercuryColor() ) +  "\" ");
            pw.println(">");

            writePlotPlot( chartElement,  pw);
            writeValueDisplay( plot.getValueDisplay(),  pw);
            writeDataRange( plot.getDataRange(),  pw);
            writeDataRange("lowRange", plot.getLowRange(),  pw);
            writeDataRange("mediumRange", plot.getMediumRange(),  pw);
            writeDataRange("highRange", plot.getHighRange(),  pw);

            pw.println("\t\t\t\t\t</thermometerPlot>");
        }
        else if (chartElement.getChart().getPlot() instanceof it.businesslogic.ireport.chart.MultiAxisPlot
                && CompatibilitySupport.version >= CompatibilitySupport.JR127)
        {
            it.businesslogic.ireport.chart.MultiAxisPlot plot = (it.businesslogic.ireport.chart.MultiAxisPlot)chartElement.getChart().getPlot();
            pw.println("\t\t\t\t\t<multiAxisPlot>");

            writePlotPlot( chartElement,  pw);

            for (int axis_c = 0; axis_c < plot.getAxis().size(); ++axis_c)
            {
                writeAxis( (Axis) plot.getAxis().get(axis_c),  pw);
            }


            pw.println("\t\t\t\t\t</multiAxisPlot>");
        }
    }



    private void writePlotPlot(ChartReportElement2 chartElement, PrintWriter pw)
    {
        pw.print("\t\t\t\t\t\t<plot ");
        if (chartElement.getChart().getPlot().getBackcolor() != null) pw.print("backcolor=\"" +  writeEncodedColor(chartElement.getChart().getPlot().getBackcolor() ) +  "\" ");
        if (!chartElement.getChart().getPlot().getOrientation().equals("Vertical")) pw.print("orientation=\"" + chartElement.getChart().getPlot().getOrientation() + "\" ");
        if (chartElement.getChart().getPlot().getBackgroundAlpha() != 1.0) pw.print("backgroundAlpha=\"" + chartElement.getChart().getPlot().getBackgroundAlpha() + "\" ");
        if (chartElement.getChart().getPlot().getForegroundAlpha() != 1.0) pw.print("foregroundAlpha=\"" + chartElement.getChart().getPlot().getForegroundAlpha() + "\" ");
        if (chartElement.getChart().getPlot().getLabelRotation() != 0 && CompatibilitySupport.version >= CompatibilitySupport.JR127)
        {
            pw.print("labelRotation=\"" + chartElement.getChart().getPlot().getLabelRotation() + "\" ");
        }

        if (chartElement.getChart().getPlot().getSeriesColors().size() > 0 && CompatibilitySupport.version >= CompatibilitySupport.JR127)
        {
            pw.println(">");

            for (int i=0; i<chartElement.getChart().getPlot().getSeriesColors().size(); ++i)
            {
                SeriesColor sc = (SeriesColor)chartElement.getChart().getPlot().getSeriesColors().get(i);
                pw.println("\t\t\t\t\t\t\t<seriesColor seriesOrder=\"" + sc.getSeriesOrder() + "\" color=\"" + writeEncodedColor(sc.getColor() ) +"\"/>");

            }


            pw.println("</plot>");
        }
        else
        {

            pw.println("/>");
        }
    }

    private void writeAxisFormat(String axisName, AxisFormat axisFormat, PrintWriter pw) {

        if (axisFormat == null) return;
        pw.println("\t\t\t\t\t\t<" + axisName + ">");
        pw.print("\t\t\t\t\t\t\t<axisFormat ");
        if (axisFormat.getLabelColor() != null) pw.print("labelColor=\"" +  writeEncodedColor(axisFormat.getLabelColor() ) +  "\" ");
        if (axisFormat.getTickLabelColor() != null) pw.print("tickLabelColor=\"" +  writeEncodedColor(axisFormat.getTickLabelColor() ) +  "\" ");
        if (axisFormat.getTickLabelMask().trim().length() > 0) pw.print("tickLabelMask=\"" + Misc.xmlEscape( axisFormat.getTickLabelMask()) +  "\" ");
        if (axisFormat.getAxisLineColor() != null) pw.print("axisLineColor=\"" +  writeEncodedColor(axisFormat.getAxisLineColor() ) +  "\" ");
        pw.println(">");

        if (axisFormat.getLabelFont() != null)
        {
            pw.println("\t\t\t\t\t\t\t\t<labelFont>");
            writeFontElement(axisFormat.getLabelFont(), pw );
            pw.println("\t\t\t\t\t\t\t\t</labelFont>");
        }
        if (axisFormat.getLabelFont() != null)
        {
            pw.println("\t\t\t\t\t\t\t\t<tickLabelFont>");
            writeFontElement(axisFormat.getTickLabelFont(), pw );
            pw.println("\t\t\t\t\t\t\t\t</tickLabelFont>");
        }

        pw.println("\t\t\t\t\t\t\t</axisFormat>");
        pw.println("\t\t\t\t\t\t</" + axisName + ">");
    }

    private void writeValueDisplay(ValueDisplay valueDisplay, PrintWriter pw) {

        if (valueDisplay == null) return;

        pw.print("\t\t\t\t\t\t\t<valueDisplay ");
        if (valueDisplay.getColor() != null) pw.print("color=\"" +  writeEncodedColor(valueDisplay.getColor() ) +  "\" ");
        if (valueDisplay.getMask().trim().length() > 0) pw.print("mask=\"" + Misc.xmlEscape( valueDisplay.getMask()) +  "\" ");
        pw.println(">");
        writeFontElement(valueDisplay.getFont(), pw );
        pw.println("\t\t\t\t\t\t\t</valueDisplay>");
    }

    private void writeDataRange(DataRange dataRange, PrintWriter pw) {
            writeDataRange(null, dataRange, pw);
    }

    private void writeDataRange(String outTagName, DataRange dataRange, PrintWriter pw) {

        if (dataRange == null) return;

        if (outTagName != null) pw.println("\t\t\t\t\t\t<" + outTagName + ">");

        pw.println("\t\t\t\t\t\t\t<dataRange>");
        pw.println("\t\t\t\t\t\t\t\t<lowExpression>" + getCDATAString(dataRange.getLowExpression() ,8 ) + "</lowExpression>");
        pw.println("\t\t\t\t\t\t\t\t<highExpression>" + getCDATAString(dataRange.getHighExpression() ,8 ) + "</highExpression>");
        pw.println("\t\t\t\t\t\t\t</dataRange>");

        if (outTagName != null) pw.println("\t\t\t\t\t\t</" + outTagName + ">");
    }

    private void writeMeterInterval(MeterInterval meterInterval, PrintWriter pw) {

        if (meterInterval == null) return;

        pw.print("\t\t\t\t\t\t\t<meterInterval ");

        if (meterInterval.getLabel().trim().length() > 0) pw.print("label=\"" + Misc.xmlEscape( meterInterval.getLabel()) +  "\" ");
        if (meterInterval.getColor() != null) pw.print("color=\"" +  writeEncodedColor(meterInterval.getColor() ) +  "\" ");
        if (meterInterval.getAlpha() != 1) pw.print("alpha=\"" + meterInterval.getAlpha() +  "\" ");

        pw.println(">");

        writeDataRange(meterInterval.getDataRange(), pw );

        pw.println("\t\t\t\t\t\t\t</meterInterval>");
    }

    private void writeAxis(Axis axis, PrintWriter pw)
    {

        if (axis == null) return;

        pw.print("\t\t\t\t\t\t\t<axis ");
        if (!axis.getPosition().equals("leftOrTop")) pw.print("position=\"" + Misc.xmlEscape( axis.getPosition()) +  "\" ");
        pw.println(">");

        writeChartElement(axis.getChartReportElement(), pw );

        pw.println("\t\t\t\t\t\t\t</axis>");
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void writeXMLProperties(java.util.Collection collection, boolean filter, PrintWriter pw, String tabs)
    {
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
                JRProperty property = (JRProperty)iter.next();
                if (filter == false || !property.getName().startsWith("chart."))
                {
                    pw.print(tabs +"<property name=\""+ Misc.xmlEscape(property.getName()) + "\" ");
                    pw.println("value=\""+ Misc.xmlEscape(property.getValue()) + "\" />");
                }

            }
    }
}
