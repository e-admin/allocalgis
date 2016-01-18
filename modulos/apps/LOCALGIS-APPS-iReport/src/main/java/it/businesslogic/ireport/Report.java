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
 * Report.java
 * 
 * Created on 10 febbraio 2003, 19.32
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent;
import it.businesslogic.ireport.gui.event.StyleChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener;
import it.businesslogic.ireport.gui.event.TemplateChangedEvent;
import it.businesslogic.ireport.refactoring.ReportRefactor;
import it.businesslogic.ireport.util.Misc;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import it.businesslogic.ireport.util.*;

/**
 *
 * @author  Administrator
 */
public class Report extends SubDataset implements SubDatasetObjectChangedListener {

    public static int SCRIPTLET_IREPORT_INTERNAL_HANDLING = 1;
    public static int SCRIPTLET_NOT_USED = 0;
    public static int SCRIPTLET_CLASSIC_HANDLING = 2;

    private int nextGroupId = 0;

    private IReportFont defaultFont = new IReportFont();

    private boolean readOnly = false;

    private int height = 0;

    private int width = 0;

    private Vector elements = new Vector();

    private Vector bands = new Vector();

    private Vector imports = new Vector();
    
    private Vector templates = new Vector();

    public Vector getTemplates() {
        return templates;
    }

    public void setTemplates(Vector templates) {
        this.templates = templates;
    }

    private String filename = "";

    private Vector subDatasets = new Vector();

    private String encoding = "UTF-8";

    private int columnCount = 1;

    private String printOrder = "Vertical";

    private String orientation = "Portrait";

    private String whenNoDataType = "NoPages";

    private int columnWidth = 535;

    private int columnSpacing = 0 ;

    private int leftMargin = 20;

    private int rightMargin = 20;

    private int topMargin = 30;

    private int bottomMargin = 30;

    private boolean isTitleNewPage = false;

    private boolean isSummaryNewPage = false;

    private long loadTime = 0;

    private Vector fonts = new Vector();

    private Vector styles = new Vector();

    private JReportFrame reportFrame;

    private String reportFormat;

    static int untitledId = 1;

    private int reportChanges = 1; // By default, a report is changed...

    private boolean floatColumnFooter = false;

    private boolean ignorePagination = false;

    /** used to tell the report when outputting to XML that it needs to set isCode="true" in expressions */
    protected boolean usingMultiLineExpressions = false;

    private String language = "java";

    private ScriptletCode scripletCode;

    private int scriptletHandling = 2;

    private java.util.HashMap proposedValues = null;

    private boolean loadingFile = false;
    
    private String formatFactoryClass = "";

    /** Creates a new instance of Report */

    public Report() {
        super();
        setName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_") + untitledId);
        untitledId++;

        proposedValues = new java.util.HashMap();
        // Add basic variables...


        getKeywordLookup().addKeyword("$R{*}");

        //addParameter(new JRParameter("COLUMN_COUNT", "", ));

        bands.addElement( new Band(this,"background",0) );
        bands.addElement( new Band(this,"title", 50));
        bands.addElement( new Band(this,"pageHeader", 50) );
        bands.addElement( new Band(this,"columnHeader",30) );
        bands.addElement( new Band(this,"detail",100));
        bands.addElement( new Band(this,"columnFooter",30) );
        bands.addElement( new Band(this,"pageFooter",50));
        bands.addElement( new Band(this,"lastPageFooter",50));
        bands.addElement( new Band(this,"summary",50) );
        bands.addElement( new Band(this,"noData",0) );

        //addImport("it.businesslogic.ireport.IReportScriptlet");
        addImport("java.util.*");
        addImport("net.sf.jasperreports.engine.*");
        addImport("net.sf.jasperreports.engine.data.*");

        try {
            scripletCode = new ScriptletCode(ScriptletCode.class.getClassLoader().getResourceAsStream("scriptlet_template.jav"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Set the user option default...
        this.setLanguage( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("DefaultScriptingLanguage","java") );

        this.addSubDatasetObjectChangedListener(this);
        
        // Add default style...
        String rDefStyle = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("DefaultStyle","");
        if (rDefStyle.length() > 0)
        {
            Vector stylesLib = (Vector)it.businesslogic.ireport.gui.MainFrame.getMainInstance().getStyleLibrarySet();
            for (int i=0; i<stylesLib.size(); ++i)
            {
                Style s = (Style)stylesLib.get(i);
                if (s.getName() != null && s.getName().equals(rDefStyle))
                {
                    Style s1 = new Style(s);
                    s1.getAttributes().put( s1.ATTRIBUTE_isDefault, "true" );
                    this.addStyle( s1 );
                }
            }
            
        }
    }

    /** Creates a new instance of Report reading it by a file...*/
    public Report(String xmlFile) throws Exception {

        this();

        untitledId--;
        
        // Remove default style...
        while (getStyles().size() > 0)
        {
            this.removeStyle( (Style) this.getStyles().get(0) );
        }
        
        // Set the real default...
        ReportReader rr = new ReportReader(this);
        rr.readFile( xmlFile );
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

    public String createChildGroup(String parentElementGroup)
    {
        nextGroupId++;
        if (parentElementGroup.length() > 0 ) parentElementGroup += ".";
        return parentElementGroup + nextGroupId;
    }

    /** Getter for property bands.
     * @return Value of property bands.
     *
     */
    public java.util.Vector getBands() {
        return bands;
    }

    /** Setter for property bands.
     * @param bands New value of property bands.
     *
     */
    public void setBands(java.util.Vector bands) {
        this.bands = bands;
        incrementReportChanges();
    }

    /** Getter for property bottomMargin.
     * @return Value of property bottomMargin.
     *
     */
    public int getBottomMargin() {
        return bottomMargin;
    }

    /** Setter for property bottomMargin.
     * @param bottomMargin New value of property bottomMargin.
     *
     */
    public void setBottomMargin(int bottomMargin) {
        if (this.bottomMargin == bottomMargin) return;
        this.bottomMargin = bottomMargin;
        incrementReportChanges();
    }

    /** Getter for property columnCount.
     * @return Value of property columnCount.
     *
     */
    public int getColumnCount() {
        return columnCount;
    }

    /** Setter for property columnCount.
     * @param columnCount New value of property columnCount.
     *
     */
    public void setColumnCount(int columnCount) {
        if (this.columnCount == columnCount) return;
        this.columnCount = columnCount;
         incrementReportChanges();
    }

    /** Getter for property columnSpacing.
     * @return Value of property columnSpacing.
     *
     */
    public int getColumnSpacing() {
        return columnSpacing;
    }

    /** Setter for property columnSpacing.
     * @param columnSpacing New value of property columnSpacing.
     *
     */
    public void setColumnSpacing(int columnSpacing) {
        if (this.columnSpacing == columnSpacing) return;
        this.columnSpacing = columnSpacing;
        incrementReportChanges();
    }

    /** Getter for property columnWidth.
     * @return Value of property columnWidth.
     *
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /** Setter for property columnWidth.
     * @param columnWidth New value of property columnWidth.
     *
     */
    public void setColumnWidth(int columnWidth) {
        if (this.columnWidth == columnWidth) return;
        this.columnWidth = columnWidth;
        incrementReportChanges();
    }

    /** Getter for property elements.
     * @return Value of property elements.
     *
     */
    public java.util.Vector getElements() {
        return elements;
    }

    /** Setter for property elements.
     * @param elements New value of property elements.
     *
     */
    public void setElements(java.util.Vector elements) {
        this.elements = elements;
        incrementReportChanges();
    }

    /** Getter for property encoding.
     * @return Value of property encoding.
     *
     */
    public java.lang.String getEncoding() {
        return encoding;
    }

    /** Setter for property encoding.
     * @param encoding New value of property encoding.
     *
     */
    public void setEncoding(java.lang.String encoding) {
        if ((this.encoding == null) ? encoding == null : this.encoding.equals(encoding)) return;
        this.encoding = encoding;
        incrementReportChanges();
    }





    /** Getter for property filename.
     * @return Value of property filename.
     *
     */
    public java.lang.String getFilename() {
        return filename;
    }

    /** Setter for property filename.
     * @param filename New value of property filename.
     *
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }

    /** Getter for property fonts.
     * @return Value of property fonts.
     *
     */
    public java.util.Vector getFonts() {
        return fonts;
    }

    /** Setter for property fonts.
     * @param fonts New value of property fonts.
     *
     */
    public void setFonts(java.util.Vector fonts) {
        this.fonts = fonts;
        incrementReportChanges();
    }



    /** Getter for property height.
     * @return Value of property height.
     *
     */
    public int getHeight() {
        return height;
    }

    /** Setter for property height.
     * @param height New value of property height.
     *
     */
    public void setHeight(int height) {
        if (this.height == height) return;
        this.height = height;
        incrementReportChanges();
    }

    /** Getter for property isSummaryNewPage.
     * @return Value of property isSummaryNewPage.
     *
     */
    public boolean isIsSummaryNewPage() {
        return isSummaryNewPage;
    }

    /** Setter for property isSummaryNewPage.
     * @param isSummaryNewPage New value of property isSummaryNewPage.
     *
     */
    public void setIsSummaryNewPage(boolean isSummaryNewPage) {
        if (this.isSummaryNewPage == isSummaryNewPage) return;
        this.isSummaryNewPage = isSummaryNewPage;
        incrementReportChanges();
    }

    /** Getter for property isTitleNewPage.
     * @return Value of property isTitleNewPage.
     *
     */
    public boolean isIsTitleNewPage() {
        return isTitleNewPage;
    }

    /** Setter for property isTitleNewPage.
     * @param isTitleNewPage New value of property isTitleNewPage.
     *
     */
    public void setIsTitleNewPage(boolean isTitleNewPage) {
        if (this.isTitleNewPage == isTitleNewPage) return;
        this.isTitleNewPage = isTitleNewPage;
        incrementReportChanges();
    }

    /** Getter for property leftMargin.
     * @return Value of property leftMargin.
     *
     */
    public int getLeftMargin() {
        return leftMargin;
    }

    /** Setter for property leftMargin.
     * @param leftMargin New value of property leftMargin.
     *
     */
    public void setLeftMargin(int leftMargin) {
        if (this.leftMargin == leftMargin) return;
        this.leftMargin = leftMargin;
        incrementReportChanges();
    }

    /** Getter for property loadTime.
     * @return Value of property loadTime.
     *
     */
    public long getLoadTime() {
        return loadTime;
    }

    /** Setter for property loadTime.
     * @param loadTime New value of property loadTime.
     *
     */
    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    /** Getter for property orientation.
     * @return Value of property orientation.
     *
     */
    public java.lang.String getOrientation() {
        return orientation;
    }

    /** Setter for property orientation.
     * @param orientation New value of property orientation.
     *
     */
    public void setOrientation(java.lang.String orientation) {
        if ((this.orientation == null) ? orientation == null : this.orientation.equals(orientation)) return;
        this.orientation = orientation;
        incrementReportChanges();
    }

    /** Getter for property printOrder.
     * @return Value of property printOrder.
     *
     */
    public java.lang.String getPrintOrder() {
        return printOrder;
    }

    /** Setter for property printOrder.
     * @param printOrder New value of property printOrder.
     *
     */
    public void setPrintOrder(java.lang.String printOrder) {
         if ((this.printOrder == null) ? printOrder == null : this.printOrder.equals(printOrder)) return;
        this.printOrder = printOrder;
        incrementReportChanges();
    }

    /** Getter for property rightMargin.
     * @return Value of property rightMargin.
     *
     */
    public int getRightMargin() {
        return rightMargin;
    }

    /** Setter for property rightMargin.
     * @param rightMargin New value of property rightMargin.
     *
     */
    public void setRightMargin(int rightMargin) {
        if (this.rightMargin == rightMargin) return;
        this.rightMargin = rightMargin;
        incrementReportChanges();
    }



    /** Getter for property topMargin.
     * @return Value of property topMargin.
     *
     */
    public int getTopMargin() {
        return topMargin;
    }

    /** Setter for property topMargin.
     * @param topMargin New value of property topMargin.
     *
     */
    public void setTopMargin(int topMargin) {
        if (this.topMargin == topMargin) return;
        this.topMargin = topMargin;
        incrementReportChanges();
    }

    public void addImport(String _import) {
        if (!imports.contains(_import)) {
            this.imports.add(_import);
            incrementReportChanges();
        }
    }

    /** Getter for property whenNoDataType.
     * @return Value of property whenNoDataType.
     *
     */
    public java.lang.String getWhenNoDataType() {
        return whenNoDataType;
    }

    /** Setter for property whenNoDataType.
     * @param whenNoDataType New value of property whenNoDataType.
     *
     */
    public void setWhenNoDataType(java.lang.String whenNoDataType) {
        if ((this.whenNoDataType == null) ? whenNoDataType == null : this.whenNoDataType.equals(whenNoDataType)) return;
        this.whenNoDataType = whenNoDataType;
        incrementReportChanges();
    }

    /** Getter for property width.
     * @return Value of property width.
     *
     */
    public int getWidth() {
        return width;
    }

    /** Setter for property width.
     * @param width New value of property width.
     *
     */
    public void setWidth(int width) {
        if (this.width == width) return;
        this.width = width;
        incrementReportChanges();
    }

    /**
     *  This function compute the Y location of the band.
     *  In the designer panel, there is as space of 10 pixels (the space
     *  between the top border of the panel, and the begin of the
     *  virtual page) that are not added to this measure. This is
     *  important because permit to multiply this height for a
     *  zoom factor.
     *
     * @param b it.businesslogic.ireport.Band The band to compute.
     */
    public int getBandYLocation(Band b) {
        int y = topMargin;

        for (Iterator i = this.bands.iterator(); i.hasNext(); ) {
            Band band = (Band) i.next();
            if (band != b) {
                y += band.getHeight();
            } else {
                break;
            }
        }

        return y;
    }

    /** Getter for property reportFrame.
     * @return Value of property reportFrame.
     *
     */
    public it.businesslogic.ireport.gui.JReportFrame getReportFrame() {
        return reportFrame;
    }

    /** Setter for property reportFrame.
     * @param reportFrame New value of property reportFrame.
     *
     */
    public void setReportFrame(it.businesslogic.ireport.gui.JReportFrame reportFrame) {
        this.reportFrame = reportFrame;
    }

    /** Getter for property reportFormat.
     * @return Value of property reportFormat.
     *
     */
    public java.lang.String getReportFormat() {
        return reportFormat;
    }

    /** Setter for property reportFormat.
     * @param reportFormat New value of property reportFormat.
     *
     */
    public void setReportFormat(java.lang.String reportFormat) {
        this.reportFormat = reportFormat;
    }

    /**
     *  This method return the height needed by a graphics to
     *  draw all the document.
     */
    public int getDesignHeight() {
        int y = topMargin + bottomMargin;

        for (Iterator i = this.bands.iterator(); i.hasNext(); ) {
            y += ((Band) i.next()).getHeight();
        }

        return y;
    }

    public int getDesignHeightNoBG() {
        int y=0;
        y  += topMargin;
        y  += bottomMargin;

        for (Iterator i = this.bands.iterator(); i.hasNext(); ) {
            Band b = (Band) i.next();
            if (!b.getName().equals("background"))
                y += b.getHeight();
        }

        return y;
    }

    /** Add a group to the reprot and perform an adjust on jReportFrame
     * @param group New group to be inserted.
     *
     */
    public void addGroup(Group grp) {
        addGroup(grp,true);
    }

    /** Add a group to the reprot and perform an adjust on jReportFrame if
     *  specified
     * @param group New group to be inserted.
     * @param adjust If true, perform an adjust on jReportFrame
     */
    public void addGroup(Group grp, boolean adjust) {
        incrementReportChanges();
        if (!getGroups().contains(grp)) {
            getGroups().addElement(grp);
        }
        String bandsstr = "";

        /*
         * start looking for the detail band.
         * and place header and footer of new group around it if (Adjust)
         */
        for (int i = 0; i < bands.size(); i++) {
            Band b = (Band)bands.elementAt(i);

            if (b.getName().equals("detail")) {

                if (adjust) {

                    int bandYLocation = getBandYLocation(b);

                    for (Iterator i2 = elements.iterator(); i2.hasNext(); ) {
                        ReportElement elem = (ReportElement) i2.next();
                        int delta_y = grp.getGroupHeader().getHeight();

                        if (elem.band == grp.getGroupHeader() || elem.band == grp.getGroupFooter()) {
                            // Do nothing
                        }
                        else if (elem.position.y >= bandYLocation + 10 + b.getHeight()) {
                            delta_y += grp.getGroupFooter().getHeight();
                            elem.trasform(new java.awt.Point(0, delta_y), TransformationType.TRANSFORMATION_MOVE);
                        }
                        else if (elem.position.y >= bandYLocation  + 10) {
                            elem.trasform(new java.awt.Point(0, delta_y), TransformationType.TRANSFORMATION_MOVE);
                        }
                    }
                }

                // All elements with top >  detail.top must be shifted of groupHeader height
                bands.insertElementAt(grp.getGroupHeader(),i);
                // All elements with top > detail.top + detail.bottom must be shifted of groupFooter height
                bands.insertElementAt(grp.getGroupFooter(),i+2);

                break;
            }
        }
        addVariable(new it.businesslogic.ireport.JRVariable(grp.getName()+"_COUNT","java.lang.Integer", true));

    }

    /**
     *  grpOne will be palce within grpTwo
     *  This means the header band of grpOne will come after the header band of grpTwo
     *  And the footer band of grpOne will come before the footer band of grpTwo
     * @author: Robert Lamping
     * @param direction  1 = down, -1 -up
     * @param grp  The group that must be moved
     */
    public synchronized void moveGroup(Group grp, int direction ) {

        Group grpOne = null;
        Group grpTwo = null;

        if ( getGroups().size() <=1 ) {
            return; // nothing to do
        }
        incrementReportChanges();
        if (direction == 1) {
            // down
            // there must be at least one element below:
            if ( getGroups().lastElement() ==  grp ) {
                return ;               // nothing to do
            }
            grpOne = grp;
            grpTwo = (Group) getGroups().get( getGroups().indexOf(grp) + 1);

        } else {
            // up
            // there must be at least one element above
            if ( getGroups().firstElement() ==  grp ) {
                return ;               // nothing to do
            }
            grpTwo = grp;
            grpOne = (Group) getGroups().get( getGroups().indexOf(grp) - 1);
        }

        Band bandGrpOneHeader = getBandByName( grpOne.getGroupHeader().getName());
        Band bandGrpTwoHeader = getBandByName( grpTwo.getGroupHeader().getName());
        Band bandGrpOneFooter = getBandByName( grpOne.getGroupFooter().getName());
        Band bandGrpTwoFooter = getBandByName( grpTwo.getGroupFooter().getName());

        int delta1 = bandGrpOneHeader.getHeight();
        int delta2 = bandGrpTwoHeader.getHeight();
        int delta3 = bandGrpOneFooter.getHeight();
        int delta4 = bandGrpTwoFooter.getHeight();

        // Move the elements
        for (Iterator i = elements.iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();

            if (re.band == bandGrpOneHeader ) {
                re.trasform(new java.awt.Point(0, delta2 ), TransformationType.TRANSFORMATION_MOVE);
            } else if (re.band ==  bandGrpTwoHeader ) {
                re.trasform(new java.awt.Point(0, -delta1 ), TransformationType.TRANSFORMATION_MOVE);
            } else if ( re.band == bandGrpTwoFooter ) {
                re.trasform(new java.awt.Point(0, delta3 ), TransformationType.TRANSFORMATION_MOVE);
            } else if ( re.band ==  bandGrpOneFooter ) {
                re.trasform(new java.awt.Point(0, -delta4 ), TransformationType.TRANSFORMATION_MOVE);
            }
        }

        // Now move the groups withing the bands array.
        // Move Band Group Headers
        int pos1 = bands.indexOf( bandGrpOneHeader);
        Band bandBuffer = (Band) bands.get(pos1);
        bands.remove(pos1);
        bands.insertElementAt(bandBuffer, pos1+1);

        // Move Band Group Footers
        int pos3 = bands.indexOf( bandGrpTwoFooter);
        bandBuffer = (Band) bands.get(pos3);
        bands.remove(pos3);
        bands.insertElementAt(bandBuffer, pos3+1);

        // Now move the groups within the group array
        int groupPos = getGroups().indexOf( grpOne );
        // groupTwo is always after grpOne
        Group groupBuffer = (Group) getGroups().get( groupPos);
        getGroups().remove(groupPos);
        getGroups().insertElementAt( groupBuffer, groupPos + 1);

    }

    public void delGroup(Group grp) {
        if (!getGroups().contains(grp)) return;
        incrementReportChanges();

        String bandsstr = "";

        for (int i=0; i<getVariables().size(); ++i)
        {
            JRVariable var = (JRVariable)getVariables().elementAt(i);
            if (var.isBuiltin() && var.getName().equalsIgnoreCase(grp.getName()+"_COUNT"))
            {
                removeVariable(var);
                break;
            }
        }

        for (int i=0; i<elements.size(); ++i) {
            // Remove all elements of this group...
            ReportElement elem = (ReportElement)elements.elementAt(i);
            if (elem.getBand() == grp.getGroupHeader() || elem.getBand() == grp.getGroupFooter()) {
                elements.removeElementAt(i);
                i--;
                continue;
            }
            // Adjust elements posisitions...
            // Il element
            int delta_y = grp.getGroupHeader().getHeight();
            if (elem.position.y >= getBandYLocation(grp.getGroupFooter())+10+grp.getGroupFooter().getHeight()) {
                delta_y += grp.getGroupFooter().getHeight();
                elem.trasform(new java.awt.Point(0, -delta_y), TransformationType.TRANSFORMATION_MOVE);
            }
            else if (elem.position.y >= getBandYLocation(grp.getGroupHeader())+10+grp.getGroupHeader().getHeight()) {
                elem.trasform(new java.awt.Point(0, -delta_y), TransformationType.TRANSFORMATION_MOVE);
            }
        }

        bands.removeElement(grp.getGroupHeader());
        bands.removeElement(grp.getGroupFooter());
        getGroups().removeElement(grp);
    }

    public Group getGroupByName(String name) {
        for (int i = 0; i < getGroups().size(); i++) {
            Group grp = (Group)getGroups().elementAt(i);
            if (grp.getName().equals(name)) {
                return grp;
            }
        }
        return null;
    }

    public Band getBandByName(String name) {
        for (int i=0; i< bands.size(); i++) {
            Band band = (Band)bands.elementAt(i);
            if (band.getName().equals(name)) {
                return band;
            }
        }
        return null;
    }



    public void saveXMLFile() {
        saveXMLFile( this.getFilename());

    }

    public void saveXMLFile( String aFilename ) {
        if(isReadOnly()){
            javax.swing.JOptionPane.showMessageDialog(getReportFrame().getMainFrame(),
            it.businesslogic.ireport.util.I18n.getString("fileReadOnly", "File is read only and cannot be saved."),
            it.businesslogic.ireport.util.I18n.getString("save","Save"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ReportWriter wr = new ReportWriter( this );
        wr.saveXMLFile( aFilename );
    }



    /**
     * Getter for property usingMultiLineExpressions.
     * @return Value of property usingMultiLineExpressions.
     */
    public boolean isUsingMultiLineExpressions() {
        return false; //usingMultiLineExpressions;
    }

    /**
     * Setter for property usingMultiLineExpressions.
     * @param usingMultiLineExpressions New value of property usingMultiLineExpressions.
     */
    public void setUsingMultiLineExpressions(boolean usingMultiLineExpressions) {
        this.usingMultiLineExpressions = false; //usingMultiLineExpressions;
    }



    /** Getter for property scripletCode.
     * @return Value of property scripletCode.
     *
     */
    public it.businesslogic.ireport.ScriptletCode getScripletCode() {
        return scripletCode;
    }

    /** Setter for property scripletCode.
     * @param scripletCode New value of property scripletCode.
     *
     */
    public void setScripletCode(it.businesslogic.ireport.ScriptletCode scripletCode) {
        this.scripletCode = scripletCode;
    }

    /** Getter for property scriptletHandling.
     * @return Value of property scriptletHandling.
     *
     */
    public int getScriptletHandling() {
        return scriptletHandling;
    }

    /** Setter for property scriptletHandling.
     * @param scriptletHandling New value of property scriptletHandling.
     *
     */
    public void setScriptletHandling(int scriptletHandling) {
        if (this.scriptletHandling == scriptletHandling) return;
        this.scriptletHandling = scriptletHandling;
        incrementReportChanges();

    }


    public String getScriptletFileName() {
        return this.getScriptletFileName( this.getFilename() );
    }

    public String getScriptletFileName(String file) {
        if (file == null) return "Scriptlet.java";

        // we assume /xxx/yyY/qqq/myreport.xml as input file name
        if (file.length() > 3) {
            file = Misc.changeFileExtension(file,  "");
            file = file.substring(0, file.length() -1 );
            File f = new File( file );
            String className = f.getName() + "Scriptlet";

            className = Misc.string_replace("_",".", className);
            className = Misc.string_replace("_"," ",className);
            if ( Character.isDigit(className.charAt( 0 )  )) {
                className = "_"+ className;
            }
            className += ".java";
            return  f.getPath().substring(0, f.getPath().length() - f.getName().length()) + className;
        }

        return "Scriptlet.java";
    }


    /**
     * Default font specified in xml file.
     * @return
     */
    public IReportFont getDefaultFont() {
        return defaultFont;
    }

    public boolean isReadOnly(){
        return readOnly;
    }
    public void checkReadOnlyFlag(){
        checkReadOnlyFlag(getFilename());
    }
    public void checkReadOnlyFlag(String oF){
        if (oF == null) return;
        File f = new File(oF);
        try {
        if (!f.exists()) {
            readOnly = false;
        } else {
            readOnly = !f.canWrite();
        }
        } catch (Exception ex) { }
        readOnly = false;
    }

    /**
     * return the band that encloses the parameter originY
     * @param originY
     * @return
     *
     * an originY < 0 will return nothing
     * an originY > designheight will return the Summary band
     */
    public Band getBandByPosition( int originY ){
        Band bname = null;
        for (Iterator i = this.getBands().iterator(); i.hasNext(); ) {
            Band b = (Band) i.next();
            if ( this.getBandYLocation(b) + 10 <= originY) {
                bname = b;
            }
        }
        if (bname != null && (this.getBandYLocation(bname) + 10 + bname.getHeight() < originY)) return null;
        return bname;
    }

    public Vector getImports() {
        return imports;
    }

    public void setImports(Vector imports) {
        this.imports = imports;
        incrementReportChanges();
    }

    public boolean isFloatColumnFooter() {
        return floatColumnFooter;
    }

    public void setFloatColumnFooter(boolean floatColumnFooter) {
        if (this.floatColumnFooter == floatColumnFooter) return;
        this.floatColumnFooter = floatColumnFooter;
        incrementReportChanges();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if ((this.language == null) ? language == null : this.language.equals(language)) return;
        this.language = language;
        incrementReportChanges();
    }

    public java.util.HashMap getProposedValues() {
        return proposedValues;
    }

    public void setProposedValues(java.util.HashMap proposedValues) {
        this.proposedValues = proposedValues;
    }

    /**
     * Recalculate the Columnwidth bases on the available space.
     * Introduces at June, 6 to allow PageTransformation.java to calculate the column width again.
     *
    */
    public void recalcColumnWidth() {

        //this.columns = (int)jNumberFieldColumns.getValue();
        // calculate space...
        //        this.setColumnCount( report.getColumnCount() );
        //        this.setColumnSpacing( report.getColumnSpacing());

        int avail = getWidth() - getLeftMargin() - getRightMargin() - (getColumnCount()-1) * getColumnSpacing();
        int dim = avail;
        dim = (int)( (double)dim / (double)this.getColumnCount());

        while ( (dim*this.getColumnCount()) > avail) {
            dim--;
        }
        this.setColumnWidth(dim);
    }

    public IReportFont getReportFontByName(String name)
    {
        if (name == null || name.length() == 0) return null;
        for (int i=0; i<getFonts().size(); ++i)
        {
                IReportFont font = (IReportFont)getFonts().elementAt(i);
                if (font.getReportFont().equals(name))
                {
                    return font;
                }
        }
        return null;
    }

    public Vector getStyles() {
        return styles;
    }

    public void setStyles(Vector styles) {
        this.styles = styles;
        incrementReportChanges();
    }

    public void elementGroupResort(Band band)
    {

        it.businesslogic.ireport.util.TreeNode root = extractBandElementsTree(band, true);
        addElementsTree( root );
        incrementReportChanges();
    }

    public it.businesslogic.ireport.util.TreeNode extractBandElementsTree(Band band, boolean remove)
    {
        java.util.List tmpElements = new java.util.ArrayList();
        it.businesslogic.ireport.util.TreeNode root = new it.businesslogic.ireport.util.TreeNode("");
        for (int i=0; i< getElements().size(); ++i)
        {
            ReportElement element = (ReportElement)getElements().elementAt(i);
            if (element.getBand() == band)
            {
                tmpElements.add(element);
                if (remove)
                {
                    getElements().removeElementAt(i);
                    i--;
                }
            }
        }

        if (tmpElements.size() > 0)
        {
            // we have to reorder all....
            for (int i=0; i< tmpElements.size(); ++i)
            {
                ReportElement element = (ReportElement)tmpElements.get(i);
                String elementGroup = element.getElementGroup();
                try {
                        if (element.getParentElement() != null)
                        {
                            it.businesslogic.ireport.util.TreeNode tmpRoot = findElementInTreeNode(root,element.getParentElement());
                            if (tmpRoot != null) addElementToGroup(tmpRoot, elementGroup, element );
                        }
                        else
                        {
                            addElementToGroup(root, elementGroup, element );
                        }
                } catch (Exception ex)
                {
                      ex.printStackTrace();
                }
            }
        }

        return root;
    }


    public void moveElementDown(ReportElement element)
    {
        incrementReportChanges();
        it.businesslogic.ireport.util.TreeNode root = extractBandElementsTree(element.getBand(), true);
        root.moveDown( element );
        addElementsTree( root );
    }

    public void moveElementUp(ReportElement element)
    {
        incrementReportChanges();

         it.businesslogic.ireport.util.TreeNode root = extractBandElementsTree(element.getBand(), true);
        root.moveUp( element );
        addElementsTree( root );

    }

    private  void addElementsTree(it.businesslogic.ireport.util.TreeNode root)
    {
        for (int i=0; i< root.getChilds().size(); ++i)
        {
            it.businesslogic.ireport.util.TreeNode node =(it.businesslogic.ireport.util.TreeNode)(root.getChilds().get(i));
            Object obj = node.getUserObject();
            if (obj instanceof ReportElement)
            {
                getElements().add( obj );
                if (obj instanceof FrameReportElement)
                {
                    addElementsTree( node );
                }
            }
            else
            {
                addElementsTree( node );
            }
        }
    }
    /*
    public void addElementToGroup(NamedList parentNode, String subGroup, ReportElement element)
    {
        // 1. We have to find the group node...
        if (subGroup.equals(""))
        {
            parentNode.add( element);
            return;
        }

        String levelGroupName = "";
        if (subGroup.indexOf(".")>=0)
        {
            levelGroupName = subGroup.substring(0, subGroup.indexOf("."));
            subGroup = subGroup.substring(subGroup.indexOf(".")+1);
        }
        else
        {
            levelGroupName = subGroup;
            subGroup = "";
        }

        // 2. Look for the node named levelGroupName
        for (int i =0; i<parentNode.size(); ++i)
        {
            Object node = parentNode.get(i);
            if (node instanceof NamedList)
            {
                NamedList nl = (NamedList)node;
                if (nl.getName().equals( levelGroupName ))
                {
                    addElementToGroup(nl, subGroup, element);
                    return;
                }
            }
        }

        // Node doesn't exists....
        NamedList nl = new NamedList(levelGroupName);
        addElementToGroup(nl, subGroup, element);
        parentNode.add( nl );
    }
    */

    public void addElementToGroup(it.businesslogic.ireport.util.TreeNode parentNode, String subGroup, ReportElement element)
    {
        incrementReportChanges();
        // 1. We have to find the group node...
        if (subGroup.equals(""))
        {
            parentNode.addChild(new it.businesslogic.ireport.util.TreeNode( element ));
            return;
        }

        String levelGroupName = "";
        if (subGroup.indexOf(".")>=0)
        {
            levelGroupName = subGroup.substring(0, subGroup.indexOf("."));
            subGroup = subGroup.substring(subGroup.indexOf(".")+1);
        }
        else
        {
            levelGroupName = subGroup;
            subGroup = "";
        }

        // 2. Look for the node named levelGroupName
        for (int i =0; i<parentNode.getChilds().size(); ++i)
        {
            it.businesslogic.ireport.util.TreeNode node = (it.businesslogic.ireport.util.TreeNode)(parentNode.getChilds().get(i));
            if (node.getName().equals( levelGroupName ) )
            {
                 addElementToGroup(node, subGroup, element);
                 return;
            }
        }

        // Node doesn't exists....
        it.businesslogic.ireport.util.TreeNode nl = new it.businesslogic.ireport.util.TreeNode(levelGroupName);
        addElementToGroup(nl, subGroup, element);
        parentNode.addChild( nl );
    }

     public it.businesslogic.ireport.util.TreeNode findElementInTreeNode(it.businesslogic.ireport.util.TreeNode parentNode, ReportElement re)
    {
        for (int k=0; k<parentNode.getChilds().size(); ++k) {
            it.businesslogic.ireport.util.TreeNode child = (it.businesslogic.ireport.util.TreeNode)(parentNode.getChilds().get(k));
            if (child.getUserObject() == re) {
                return child;
            }
            if (re.getElementGroup().equals("") || re.getParentElement() != null)
            {
                if (child.getUserObject() instanceof FrameReportElement ||
                    child.getUserObject() instanceof String)
                {
                    it.businesslogic.ireport.util.TreeNode newchild = findElementInTreeNode(child,re);
                    if (newchild != null) return newchild;
                }
            }
        }
        return null;
    }

    public Vector getSubDatasets() {
        return subDatasets;
    }

    public void setSubDatasets(Vector subDatasets) {
        this.subDatasets = subDatasets;
        incrementReportChanges();
    }

    public boolean isIgnorePagination() {
        return ignorePagination;
    }

    public void setIgnorePagination(boolean ignorePagination) {
        if (this.ignorePagination == ignorePagination) return;
        this.ignorePagination = ignorePagination;
        incrementReportChanges();
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;


    public void addSubDataset(SubDataset subDataset) {
        this.getSubDatasets().add( subDataset);
        incrementReportChanges();
        this.fireReportSubDatasetChangedListenerReportSubDatasetChanged( new ReportSubDatasetChangedEvent(this, subDataset, ReportSubDatasetChangedEvent.ADDED, ReportSubDatasetChangedEvent.OBJECT_SUBDATASET));
        subDataset.addSubDatasetObjectChangedListener( this );
    }

    public void removeSubDataset(SubDataset subDataset) {
        this.getSubDatasets().remove(subDataset);
        this.fireReportSubDatasetChangedListenerReportSubDatasetChanged( new ReportSubDatasetChangedEvent(this,subDataset, ReportSubDatasetChangedEvent.REMOVED, ReportSubDatasetChangedEvent.OBJECT_SUBDATASET));
        subDataset.removeSubDatasetObjectChangedListener( this );
        incrementReportChanges();
    }

    public void removeStyle(Style s) {

        this.getStyles().remove(s);
        removeStyleFromElements( s,  getElements());
        this.fireStyleChangedListenerStyleChanged( new StyleChangedEvent( this,StyleChangedEvent.DELETED , s,s));
        incrementReportChanges();
    }
    
    public void removeTemplate(Template t) {

        this.getTemplates().remove(t);
        this.fireStyleChangedListenerTemplateChanged( new TemplateChangedEvent( this, TemplateChangedEvent.DELETED , t,t));
        incrementReportChanges();
    }
    
    public void addTemplate(Template t) {

        this.getTemplates().add(t);
        this.fireStyleChangedListenerTemplateChanged( new TemplateChangedEvent( this, TemplateChangedEvent.ADDED , t,t));
        incrementReportChanges();
    }

    public void addStyle(Style s)
    {
        this.getStyles().add(s);
        incrementReportChanges();
        this.fireStyleChangedListenerStyleChanged( new StyleChangedEvent( this,StyleChangedEvent.ADDED , s,s));
    }

    private void removeStyleFromElements(Style s, Vector thisElements)
    {
        for (int i=0; i<thisElements.size(); ++i)
        {
            ReportElement re = (ReportElement)thisElements.elementAt(i);

            if (re.getStyle() == s) re.setStyle(null);

            if (re instanceof CrosstabReportElement)
            {
                removeStyleFromElements(s, ((CrosstabReportElement)re).getElements());
            }
        }
        incrementReportChanges();
    }

    /**
     * Registers ReportSubDatasetChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addReportSubDatasetChangedListener(it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener.class, listener);
    }

    /**
     * Removes ReportSubDatasetChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeReportSubDatasetChangedListener(it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener listener) {
        listenerList.remove (it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    public void fireReportSubDatasetChangedListenerReportSubDatasetChanged(it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener)listeners[i+1]).reportSubDatasetChanged (event);
            }
        }
    }

    
    public void subDatasetObjectChanged(it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent evt) {

        java.util.Vector changedElements = new java.util.Vector();
        if (evt.getType() == evt.FIELD && evt.getAction() == evt.MODIFIED && evt.getSource() instanceof SubDataset)
        {
            String oldString = "$F{" + evt.getOldValue() + "}";
            String newString = "$F{" + evt.getNewValue() + "}";
            if (evt.getOldValue() != null &&
                evt.getNewValue() != null &&
                !oldString.equals(newString))
            {
                System.out.println("replaceInReportExpressions for DS= " + evt.getSource());
                ReportRefactor.replaceInReportExpressions(oldString, newString, evt.getSource(), this);
            }
            
            // Change referred types in textfields...
            if (evt.getSource() == this && !((JRField)evt.getOldValue()).getClassType().equals( ((JRField)evt.getNewValue()).getClassType()) )
            {
                 changedElements.addAll( ReportRefactor.replaceTextfieldClass("$F{" + evt.getNewValue() + "}", ((JRField)evt.getNewValue()).getClassType(),  getElements()) );
                
            }
        }
        
        if (evt.getType() == evt.PARAMETER && evt.getAction() == evt.MODIFIED && evt.getSource() instanceof SubDataset)
        {
            String oldString = "$P{" + evt.getOldValue() + "}";
            String newString = "$P{" + evt.getNewValue() + "}";
            if (evt.getOldValue() != null &&
                evt.getNewValue() != null &&
                !oldString.equals(newString))
            {
                ReportRefactor.replaceInReportExpressions(oldString, newString, evt.getSource(), this);
            }
            
            if (evt.getSource() == this && !((JRParameter)evt.getOldValue()).getClassType().equals( ((JRParameter)evt.getNewValue()).getClassType()) )
            {
                changedElements.addAll( ReportRefactor.replaceTextfieldClass("$P{" + evt.getNewValue() + "}", ((JRParameter)evt.getNewValue()).getClassType(),  getElements()) );
            }
        }
        
        if (evt.getType() == evt.VARIABLE && evt.getAction() == evt.MODIFIED && evt.getSource() instanceof SubDataset)
        {
            String oldString = "$V{" + evt.getOldValue() + "}";
            String newString = "$V{" + evt.getNewValue() + "}";
            if (evt.getOldValue() != null &&
                evt.getNewValue() != null &&
                !oldString.equals(newString))
            {
                ReportRefactor.replaceInReportExpressions(oldString, newString, evt.getSource(), this);
            }
            
            if (evt.getSource() == this && !((JRVariable)evt.getOldValue()).getClassType().equals( ((JRVariable)evt.getNewValue()).getClassType()) )
            {
                changedElements.addAll( ReportRefactor.replaceTextfieldClass("$V{" + evt.getNewValue() + "}", ((JRVariable)evt.getNewValue()).getClassType(),  getElements()) );
            }
        }
        
        if (changedElements.size() > 0)
        {
            System.out.flush();
            getReportFrame().fireReportListenerReportElementsChanged(
                        new ReportElementChangedEvent(getReportFrame(),changedElements , ReportElementChangedEvent.CHANGED));
            // this window still requires a manual update...
            MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
        }
        
        //System.out.println(evt.getSource() + "   " + this);
        //fireSubDatasetObjectChangedListenerSubDatasetObjectChanged( evt );
        if (getListenerList() != null && evt.getSource() != this)
        {
            Object[] listeners = getListenerList().getListenerList ();
             for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i+1] == this) continue;
                //if (listeners[i+1] == evt.getSource()) continue;
                if (listeners[i]==it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener)listeners[i+1]).subDatasetObjectChanged (evt);
                }
             }
        }

        incrementReportChanges();
    }

    /**
     * Registers StyleChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addStyleChangedListener(it.businesslogic.ireport.gui.event.StyleChangedListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.StyleChangedListener.class, listener);
    }

    /**
     * Removes StyleChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeStyleChangedListener(it.businesslogic.ireport.gui.event.StyleChangedListener listener) {
        listenerList.remove (it.businesslogic.ireport.gui.event.StyleChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    public void fireStyleChangedListenerStyleChanged(it.businesslogic.ireport.gui.event.StyleChangedEvent event) {

        // We have to apply again the style...
        Enumeration enum_elements = getElements().elements();
        while (enum_elements.hasMoreElements())
        {
            ReportElement re = (ReportElement)enum_elements.nextElement();
            if (re.getStyle() == event.getNewValue())
            {
                re.setStyle( event.getNewValue());
            }
        }

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.StyleChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.StyleChangedListener)listeners[i+1]).styleChanged (event);
            }
        }
    }
    
    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    public void fireStyleChangedListenerTemplateChanged(it.businesslogic.ireport.gui.event.TemplateChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.StyleChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.StyleChangedListener)listeners[i+1]).templateChanged (event);
            }
        }
    }

    public Style getDefaultStyle()
    {
        for (int i=0; i<getStyles().size(); ++i)
        {
         Style s = (Style)getStyles().elementAt(i);
         if (s.getAttributeBoolean( s.ATTRIBUTE_isDefault, false ))
         {
             return s;
         }
        }
        return null;
    }



    public int getReportChanges() {
        return reportChanges;
    }

    /*
     *  Keep track of report changes. A ReportDocumentStatusChanged
     *  event is thrown in the following cases:
     *
     *   currentChanges == 0 && newChanges > 0  => ReportDocumentStatusChanged to DIRTY
     *   currentChanges > 0 && newChanges == 0  => ReportDocumentStatusChanged to NO_DIRTY
     *
     */
    public void setReportChanges(int newChanges) {

        boolean isChanged = false;

        if (reportChanges == 0 && newChanges > 0) isChanged = true;
        else if (reportChanges > 0 && newChanges == 0) isChanged = true;

        this.reportChanges = newChanges;

        //if (fireEvent)
        //{
        fireReportDocumentStatusChangedListenerReportDocumentStatusChanged( new ReportDocumentStatusChangedEvent(this, isChanged));
        //}
    }

    public void incrementReportChanges() {
        setReportChanges( getReportChanges() + 1);
    }

    public void decrementReportChanges() {
        if (getReportChanges() > 0)
        {
            setReportChanges( getReportChanges() - 1);
        }
    }

    /**
     * Alias for getReportChanges() > 0
     */
    public boolean isModified()
    {
        return getReportChanges() > 0;
    }

    public boolean isLoadingFile() {
        return loadingFile;
    }

    public void setLoadingFile(boolean loading) {
        this.loadingFile = loading;
    }

    /**
     * Registers ReportDocumentStatusChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addReportDocumentStatusChangedListener(it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener.class, listener);
    }

    /**
     * Removes ReportDocumentStatusChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeReportDocumentStatusChangedListener(it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireReportDocumentStatusChangedListenerReportDocumentStatusChanged(it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener)listeners[i+1]).reportDocumentStatusChanged (event);
            }
        }
    }

    /**
     * Return the available vertical space to add new bands or resize the current ones
     *
     */
    public int getAvailableVerticalSpace()
    {
        int ph = getHeight();
        ph -= getTopMargin();
        ph -= getBottomMargin();

        for (int i=0; i< getBands().size(); ++i)
        {
            Band b = (Band)getBands().elementAt(i);
            if (b.getName().equals("title") && isTitleNewPage) continue;
            if (b.getName().equals("background")) continue;
            //if (b.getName().equals("lastPageFooter")) continue;
            if (b.getName().equals("summary") && isSummaryNewPage) continue;

            ph -= b.getHeight();
        }

        return ph;
    }

    public void setDefaultFont(IReportFont defaultFont) {
        this.defaultFont = defaultFont;
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

    public String getFormatFactoryClass() {
        return formatFactoryClass;
    }

    public void setFormatFactoryClass(String formatFactoryClass) {
        this.formatFactoryClass = formatFactoryClass;
    }

    
}
