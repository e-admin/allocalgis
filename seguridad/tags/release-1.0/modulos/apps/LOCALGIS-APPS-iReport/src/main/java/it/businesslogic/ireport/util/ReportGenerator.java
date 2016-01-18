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
 * ReportGenerator.java
 *
 * Created on December 20, 2006, 8:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.util;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.StaticTextReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.gui.wizard.UserChoicesWizardTemplate;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author gtoffoli
 */
public class ReportGenerator {
    
    public static Report createReport(UserChoicesWizardTemplate ucwt) throws Exception
    {
        if (ucwt.getTemplateFile().toUpperCase().endsWith("C.XML"))
        {
            return createColumnarReport(ucwt);
        }
        else
        {
            return createTabularReport(ucwt);
        }
    }
    
    /**********************************************************
     *
     */
    static public Report createColumnarReport(UserChoicesWizardTemplate ucwt) throws Exception
    {
        Report template = new Report(ucwt.getTemplateFile());
        
        template.incrementReportChanges();
        template.setFilename(null);
        
        //2. Find detail and column header bands...
        Band detail=null;
        Band columns=null;
        
        Enumeration e = template.getBands().elements();
        while (e.hasMoreElements()) {
            Band band = (Band)e.nextElement();
            if (band.getName().equals("detail")) {
                detail = band;
            } else if (band.getName().equals("columnHeader")) {
                columns = band;
            }
        }
        
        // 1. Normalize position to band...
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(0,- template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        //1. Adding groups...
        if (ucwt.getGroupExpressions().size() > 0) {
            Group g = template.getGroupByName("Group1");
            Group g1 = new Group( template,Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(0)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g1.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(0) + "}" );
            template.addGroup(g1);
            
            // Add to g1 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g1.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g1.getGroupFooter() );
                // Set text to Group1 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G1Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(0));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G1Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(0)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(0);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 1) {
            Group g = template.getGroupByName("Group2");
            Group g2 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(1)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g2.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(1) + "}" );
            template.addGroup(g2);
            
            // Add to g2 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g2.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g2.getGroupFooter() );
                // Set text to Group2 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G2Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(1));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G2Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(1)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(1);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 2) {
            Group g = template.getGroupByName("Group3");
            Group g3 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(2)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g3.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(2) + "}" );
            template.addGroup(g3);
            
            // Add to g3 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g3.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g3.getGroupFooter() );
                // Set text to Group3 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G3Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(2));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G3Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(2)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(2);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 3) {
            Group g = template.getGroupByName("Group4");
            Group g4 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(3)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g4.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(3) + "}" );
            template.addGroup(g4);
            
            // Add to g4 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g4.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g4.getGroupFooter() );
                // Set text to Group4 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G4Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(3));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G4Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(3)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(3);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        //1. Adding fields...
        int currentx = template.getLeftMargin()+10;
        int currenty = 10;
        e = template.getElements().elements();
        StaticTextReportElement detailLabel = null;
        TextFieldReportElement detailField = null;
        while (e.hasMoreElements() && (detailLabel==null || detailField == null) ) {
            ReportElement rElement = (ReportElement)e.nextElement();
            if (rElement instanceof StaticTextReportElement) {
                StaticTextReportElement stre = (StaticTextReportElement)rElement;
                if (stre.getText().equalsIgnoreCase("DetailLabel"))
                    detailLabel =stre;
            } else if (rElement instanceof TextFieldReportElement) {
                TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                if (tfre.getText().equalsIgnoreCase("DetailField"))
                    detailField = tfre;
            }
        }
        
        if (detailField != null)
            template.getElements().removeElement(detailField);
        if (detailLabel != null)
            template.getElements().removeElement(detailLabel);
        
        if (detailField == null)
            detailField = new TextFieldReportElement(0,0,100,20);
        if (detailLabel == null)
            detailLabel = new StaticTextReportElement(0,0,100,18);
        
        int nfields = ucwt.getDisplayFields().size();
        
        
        if (ucwt.getGroupExpressions().size()>0) nfields--;
        if (ucwt.getGroupExpressions().size()>1) nfields--;
        if (ucwt.getGroupExpressions().size()>2) nfields--;
        if (ucwt.getGroupExpressions().size()>3) nfields--;
        
        int fwidth = template.getColumnWidth()/ ( (nfields <= 0) ? 1 : nfields);
        int ffheight =  detailField.getPosition().y;
        
        for (int i=0; i<ucwt.getDisplayFields().size(); ++i) {
            // FIELD
            it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)ucwt.getDisplayFields().get(i);
            template.addField(f);
            
            if (ucwt.getGroupExpressions().size()>0 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(0) ))
                continue;
            if (ucwt.getGroupExpressions().size()>1 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(1) ))
                continue;
            if (ucwt.getGroupExpressions().size()>2 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(2) ))
                continue;
            if (ucwt.getGroupExpressions().size()>3 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(3) ))
                continue;
            
            
            TextFieldReportElement re = (TextFieldReportElement)detailField.cloneMe();
            re.setPosition(new java.awt.Point(detailField.getPosition().x, ffheight));
            re.updateBounds();
            re.setText("$F{"+ f.getName() +"}");
            
            re.setBand(detail);
            re.setMatchingClassExpression( f.getClassType(), true );
            
            template.getElements().addElement(re);
            
            // COLUMN LABEL...
            StaticTextReportElement sre = (StaticTextReportElement)detailLabel.cloneMe();
            sre.setPosition(new java.awt.Point(detailLabel.getPosition().x,ffheight));
            sre.setWidth(fwidth);
            sre.updateBounds();
            
            //Get field description from datasource
            //added by Felix Firgau on Oct, 11th 2006
            String columnLabelText=f.getName();
            if(ucwt.isSaveFieldDescriptions()) {
                String description = f.getDescription();
                if(description!=null && description.length() > 0)
                    columnLabelText = description;
            }
            
            sre.setText(""+ columnLabelText +"");
            sre.setBand(detail);
            template.getElements().addElement(sre);
            
            ffheight +=  detailField.position.y+detailField.height-10;
        }
        
        detail.setHeight(ffheight);
        
        // Remove template groups...****************
        e = template.getElements().elements();
        Vector elements_to_delete = new Vector();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            Group g = null;
            if ((g=template.getGroupByName("Group1"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group2"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group3"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group4"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
        }
        e =elements_to_delete.elements();
        while (e.hasMoreElements()) {
            template.getElements().removeElement(e.nextElement());
        }
        
        Group g;
        if ((g=template.getGroupByName("Group1"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group2"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group3"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        if ((g=template.getGroupByName("Group4"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        
        // Positioning columns band...
        /*
        if ((g=template.getGroupByName("Columns"))!=null)
        {
                template.getGroups().removeElement(g);
                template.getBands().removeElement(g.getGroupHeader());
                template.getBands().removeElement(g.getGroupFooter());
                template.addGroup(g);
                // we must adjust band position...
        }
         */
        // Adjust vertical position of elements
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(0,+ template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        template.setQuery( ucwt.getQuery() );
        template.setQueryLanguage( ucwt.getQuery_language() );
        
        return template;
    }
    
    
    
        
     /**
      * Create a Report from a template of user choices
      *
      */
    public static Report createTabularReport(UserChoicesWizardTemplate ucwt) throws Exception
    {

        Report template = new Report(ucwt.getTemplateFile());
        
        template.incrementReportChanges();
        template.setFilename(null);
        
        //2. Find detail and column header bands...
        Band detail=null;
        Band columns=null;
        
        Enumeration e = template.getBands().elements();
        while (e.hasMoreElements()) {
            Band band = (Band)e.nextElement();
            if (band.getName().equals("detail")) {
                detail = band;
            } else if (band.getName().equals("columnHeader")) {
                columns = band;
            }
        }
        
        // 1. Normalize position to band...
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(0,- template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        //1. Adding groups...
        if (ucwt.getGroupExpressions().size() > 0) {
            Group g = template.getGroupByName("Group1");
            Group g1 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(0)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g1.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(0) + "}" );
            template.addGroup(g1);
            
            // Add to g1 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g1.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g1.getGroupFooter() );
                // Set text to Group1 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G1Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(0));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G1Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(0)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(0);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 1) {
            Group g = template.getGroupByName("Group2");
            Group g2 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(1)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g2.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(1) + "}" );
            template.addGroup(g2);
            
            // Add to g2 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g2.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g2.getGroupFooter() );
                // Set text to Group2 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G2Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(1));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G2Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(1)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(1);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 2) {
            Group g = template.getGroupByName("Group3");
            Group g3 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(2)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g3.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(2) + "}" );
            template.addGroup(g3);
            
            // Add to g3 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g3.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g3.getGroupFooter() );
                // Set text to Group3 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G3Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(2));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G3Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(2)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(2);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        if (ucwt.getGroupExpressions().size() > 3) {
            Group g = template.getGroupByName("Group4");
            Group g4 = new Group( template, Misc.string_replace("_"," ",""+ucwt.getGroupExpressions().get(3)),  g.getGroupFooter().getHeight(),g.getGroupHeader().getHeight());
            g4.setGroupExpression("$F{"+ ucwt.getGroupExpressions().get(3) + "}" );
            template.addGroup(g4);
            
            // Add to g4 all elements attached to g.header and g.footer...
            e = template.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement rElement = (ReportElement)e.nextElement();
                if (rElement.getBand() == g.getGroupHeader())
                    rElement.setBand(g4.getGroupHeader() );
                else if (rElement.getBand() == g.getGroupFooter())
                    rElement.setBand(g4.getGroupFooter() );
                // Set text to Group4 label...
                if (rElement instanceof StaticTextReportElement) {
                    StaticTextReportElement stre = (StaticTextReportElement)rElement;
                    if (stre.getText().equals("G4Label"))
                        stre.setText( ""+ucwt.getGroupExpressions().get(3));
                } else if (rElement instanceof TextFieldReportElement) {
                    TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                    if (tfre.getText().equals("G4Field")) {
                        tfre.setText("$F{"+ucwt.getGroupExpressions().get(3)+"}");
                        // find the class type...
                        String fname = ""+ucwt.getGroupExpressions().get(3);
                        String fclass = "java.lang.String";
                        for (int i=0; i<ucwt.getDisplayFields().size(); ++i)
                        {
                            JRField f = (JRField)ucwt.getDisplayFields().get(i);
                            if (f.getName().equals(fname))
                            {
                                fclass = f.getClassType();
                                break;
                            }
                        }
                        tfre.setMatchingClassExpression( fclass, true );
                    }
                }
            }
        }
        
        //1. Adding fields...
        int currentx = template.getLeftMargin()+10;
        int currenty = 10;
        e = template.getElements().elements();
        StaticTextReportElement detailLabel = null;
        TextFieldReportElement detailField = null;
        while (e.hasMoreElements() && (detailLabel==null || detailField == null) ) {
            ReportElement rElement = (ReportElement)e.nextElement();
            if (rElement instanceof StaticTextReportElement) {
                StaticTextReportElement stre = (StaticTextReportElement)rElement;
                if (stre.getText().equalsIgnoreCase("DetailLabel"))
                    detailLabel =stre;
            } else if (rElement instanceof TextFieldReportElement) {
                TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                if (tfre.getText().equalsIgnoreCase("DetailField"))
                    detailField = tfre;
            }
        }
        
        if (detailField != null)
            template.getElements().removeElement(detailField);
        if (detailLabel != null)
            template.getElements().removeElement(detailLabel);
        
        if (detailField == null)
            detailField = new TextFieldReportElement(0,0,100,20);
        if (detailLabel == null)
            detailLabel = new StaticTextReportElement(0,0,100,18);
        
        int nfields = ucwt.getDisplayFields().size();
        if (ucwt.getGroupExpressions().size()>0) nfields--;
        if (ucwt.getGroupExpressions().size()>1) nfields--;
        if (ucwt.getGroupExpressions().size()>2) nfields--;
        if (ucwt.getGroupExpressions().size()>3) nfields--;
        
        int fwidth = template.getColumnWidth()/( (nfields <= 0) ? 1 : nfields);
        
        for (int i=0; i<ucwt.getDisplayFields().size(); ++i) {
            // FIELD
            it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)ucwt.getDisplayFields().get(i);
            template.addField(f);
            
            if (ucwt.getGroupExpressions().size()>0 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(0) ))
                continue;
            if (ucwt.getGroupExpressions().size()>1 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(1) ))
                continue;
            if (ucwt.getGroupExpressions().size()>2 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(2) ))
                continue;
            if (ucwt.getGroupExpressions().size()>3 && f.getName().equalsIgnoreCase( ""+ucwt.getGroupExpressions().get(3) ))
                continue;
            
            
            TextFieldReportElement re = (TextFieldReportElement)detailField.cloneMe();
            re.setPosition(new java.awt.Point(currentx,detailField.getPosition().y));
            re.setWidth( fwidth);
            re.updateBounds();
            re.setText("$F{"+ f.getName() +"}");
            
            re.setBand(detail);
            re.setMatchingClassExpression( f.getClassType(), true );
            template.getElements().addElement(re);
            
            // COLUMN LABEL...
            StaticTextReportElement sre = (StaticTextReportElement)detailLabel.cloneMe();
            sre.setPosition(new java.awt.Point(currentx,detailLabel.getPosition().y));
            sre.setWidth(fwidth);
            sre.updateBounds();
            
            //Get field description from datasource
            //added by Felix Firgau on Oct, 11th 2006
            String columnLabelText=f.getName();
            if(ucwt.isSaveFieldDescriptions()) {
                String description = f.getDescription();
                if(description!=null && description.length() > 0)
                    columnLabelText = description;
            }
            
            sre.setText(""+ columnLabelText +"");
            sre.setBand(columns);
            template.getElements().addElement(sre);
            
            currentx += fwidth;
            currentx = Math.min(template.getWidth() - template.getRightMargin()+10-30, currentx);
        }
        
        // Remove template groups...****************
        e = template.getElements().elements();
        Vector elements_to_delete = new Vector();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            Group g = null;
            if ((g=template.getGroupByName("Group1"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group2"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group3"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
            else if ((g=template.getGroupByName("Group4"))!=null && (rElement.getBand() == g.getGroupHeader() || rElement.getBand() == g.getGroupFooter()))
                elements_to_delete.addElement(rElement);
        }
        e =elements_to_delete.elements();
        while (e.hasMoreElements()) {
            template.getElements().removeElement(e.nextElement());
        }
        
        Group g;
        if ((g=template.getGroupByName("Group1"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group2"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group3"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        if ((g=template.getGroupByName("Group4"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        // Positioning columns band...
        /*
        if ((g=template.getGroupByName("Columns"))!=null)
        {
                template.getGroups().removeElement(g);
                template.getBands().removeElement(g.getGroupHeader());
                template.getBands().removeElement(g.getGroupFooter());
                template.addGroup(g);
                // we must adjust band position...
        }
         */
        // Adjust vertical position of elements
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(0,+ template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        template.setQuery( ucwt.getQuery() );
        template.setQueryLanguage( ucwt.getQuery_language() );
        
        return template;
    }
}
