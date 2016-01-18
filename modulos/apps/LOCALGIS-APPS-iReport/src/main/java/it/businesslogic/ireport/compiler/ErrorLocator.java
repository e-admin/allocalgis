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
 * ErrorLocator.java
 *
 * Created on March 14, 2007, 4:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.compiler;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.ChartReportElement2;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.HyperLinkableReportElement;
import it.businesslogic.ireport.ImageReportElement;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.SubReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.chart.AreaChart;
import it.businesslogic.ireport.chart.Bar3DChart;
import it.businesslogic.ireport.chart.BarChart;
import it.businesslogic.ireport.chart.BubbleChart;
import it.businesslogic.ireport.chart.CandlestickChart;
import it.businesslogic.ireport.chart.CategoryDatasetPanel;
import it.businesslogic.ireport.chart.CategorySeriesDialog;
import it.businesslogic.ireport.chart.Chart;
import it.businesslogic.ireport.chart.HighLowChart;
import it.businesslogic.ireport.chart.HighLowDatasetPanel;
import it.businesslogic.ireport.chart.LineChart;
import it.businesslogic.ireport.chart.MeterChart;
import it.businesslogic.ireport.chart.MultiAxisChart;
import it.businesslogic.ireport.chart.Pie3DChart;
import it.businesslogic.ireport.chart.PieChart;
import it.businesslogic.ireport.chart.PieDatasetPanel;
import it.businesslogic.ireport.chart.ScatterChart;
import it.businesslogic.ireport.chart.StackedAreaChart;
import it.businesslogic.ireport.chart.StackedBar3DChart;
import it.businesslogic.ireport.chart.StackedBarChart;
import it.businesslogic.ireport.chart.ThermometerChart;
import it.businesslogic.ireport.chart.TimePeriodDatasetPanel;
import it.businesslogic.ireport.chart.TimePeriodSeriesDialog;
import it.businesslogic.ireport.chart.TimeSeriesChart;
import it.businesslogic.ireport.chart.TimeSeriesDatasetPanel;
import it.businesslogic.ireport.chart.TimeSeriesDialog;
import it.businesslogic.ireport.chart.ValueDatasetPanel;
import it.businesslogic.ireport.chart.XYAreaChart;
import it.businesslogic.ireport.chart.XYBarChart;
import it.businesslogic.ireport.chart.XYDatasetPanel;
import it.businesslogic.ireport.chart.XYLineChart;
import it.businesslogic.ireport.chart.XYSeriesDialog;
import it.businesslogic.ireport.chart.XYZDatasetPanel;
import it.businesslogic.ireport.chart.XYZSeriesDialog;
import it.businesslogic.ireport.chart.gui.ChartPropertiesDialog;
import it.businesslogic.ireport.chart.gui.SectionItemHyperlinkPanel;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.gui.CrosstabEditor;
import it.businesslogic.ireport.crosstab.gui.CrosstabGroupDialog;
import it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog;
import it.businesslogic.ireport.crosstab.gui.MeasureDialog;
import it.businesslogic.ireport.gui.ElementPropertiesDialog;
import it.businesslogic.ireport.gui.JRLinkParameterDialog;
import it.businesslogic.ireport.gui.JRParameterDialog;
import it.businesslogic.ireport.gui.JRSubreportParameterDialog;
import it.businesslogic.ireport.gui.JRVariableDialog;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.sheet.MeterIntervalDialog;
import it.businesslogic.ireport.gui.sheet.MeterIntervalsDialog;
import it.businesslogic.ireport.gui.style.StyleDialog;
import it.businesslogic.ireport.gui.subdataset.GroupDialog;
import it.businesslogic.ireport.gui.subdataset.GroupsDialog;
import it.businesslogic.ireport.gui.subdataset.SubDatasetDialog;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author gtoffoli
 * Please not that this class is not thread safe.
 */
public class ErrorLocator {
    
    private String errorString = "";
    
    // Update the relative ELEMENTS_CLASS when changing this array!!!
    static final String[] ELEMENTS = new String[]{
        "break",
        "line",
        "rectangle",
        "ellipse",
        "image",
        "staticText",
        "textField",
        "subreport",
        "pieChart",
        "pie3DChart",
        "barChart",
        "bar3DChart",
        "xyBarChart",
        "stackedBarChart",
        "stackedBar3DChart",
        "lineChart",
        "xyLineChart",
        "areaChart",
        "xyAreaChart",
        "scatterChart",
        "bubbleChart",
        "timeSeriesChart",
        "highLowChart",
        "candlestickChart",
        "meterChart",
        "thermometerChart",
        "multiAxisChart",
        "stackedAreaChart",
        //"elementGroup",
        "crosstab",
        "frame"
    };
    
    static final String[] ELEMENTS_CLASS = new String[]{
        "BreakReportElement",
        "LineReportElement",
        "RectangleReportElement",
        "EllipseReportElement",
        "ImageReportElement",
        "StaticTextReportElement",
        "TextFieldReportElement",
        "SubReportElement",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        "ChartReportElement2",
        //"elementGroup",
        "CrosstabReportElement",
        "FrameReportElement"
    };
    
    static final String[] BANDS = new String[]{"background","title","pageHeader","pageFooter",
                  "columnHeader","columnFooter","detail","lastPageFooter","summary"};
    
    private JReportFrame jReportFrame = null;
    
    /** Creates a new instance of ErrorLocator */
    public ErrorLocator() {
    }
    
    /**
     * This method get the index of the first node specified. Please note: node 0 = 1
     * ex:
     *  /variable[1] will return 0
     *  /variable[2] will return 1
     *  /variable will return 0;
     *  /a/b[7] return 0
     *  /a[7] return 6
     *  null return 0
     *  [] return 0
     */
    final public int getNodeIndex(String exp)
    {
        
        if (exp == null || exp.length() == 0) return 0;
        if (exp.startsWith("/")) return getNodeIndex(exp.substring(1));
        
        int index = 0;
        
        if (exp.indexOf("/") > 0)
        {
            return getNodeIndex(exp.substring(0, exp.indexOf("/")));
        }
        
        if (exp.indexOf("[") < 0) return 0;
        
        try {
            index = Integer.parseInt( exp.substring(exp.indexOf("[")+1, exp.lastIndexOf("]")) );
            if (index > 0) index--;
        } catch (Exception ex)
        {
            
        }
        
        return index;
    }
    
    /**
     * This method get the child node (the next step in the path)
     * ex:
     *  /a/b returns b
     *  a/b  returns b
     *  /    returns ""
     *  /a   returns ""
     *  ""   returns ""
     *  a    returns ""
     *  //b   returns b
     */
    final public String childNode(String exp)
    {
        
        if (exp == null || exp.length() == 0) return "";
        if (exp.startsWith("/")) return childNode(exp.substring(1));
        
        if (exp.indexOf("/") > 0)
        {
            return exp.substring(exp.indexOf("/"));
        }
        
        return "";
    }
    /**
     * This method get a JReportFrame and an xpath expression and tries to locate the referenced error
     * opening the right windows if necessary and hilighting the error position...
     *
     */
    public void parseError(String xpath, String errormsg)
    {
        errorString = (errormsg == null) ? "" : errormsg;
        
        if (getJReportFrame() == null || xpath == null) return;
        
          Report report = getJReportFrame().getReport();
          if (isNode(xpath, "jasperReport"))
          {
                parseError( childNode(xpath), errormsg);
                return;
          }
          
          if (isNode(xpath, "field"))
          {
              int itemIndex = getNodeIndex(xpath);
              JRField field = (JRField)report.getFields().get( itemIndex  );
              
              // TODO Select the variable in the tree...
              if (field != null)
              {
                MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(field);
                MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyField( field, report); 
              }
          }
          else if (isNode(xpath, "variable"))
          {
              int itemIndex = getNodeIndex(xpath);
              JRVariable var = getVariableAt(report, itemIndex  );
              
              // TODO Select the variable in the tree...
              if (var != null)
              {
                MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(var);
                parseVariable(report, var, childNode(xpath) );
              }
          }
          else if (isNode(xpath, "parameter"))
          {
              int itemIndex = getNodeIndex(xpath);
              JRParameter param = getParameterAt(report, itemIndex  );
              
              // TODO Select the variable in the tree...
              if (param != null)
              {
                MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(param);
                parseParameter(report, param, childNode(xpath) );
              }
          }
          else if (isNode(xpath, "filterExpression"))
          {
              // open the query window...
              MainFrame.getMainInstance().getReportQueryDialog().setVisible(true);
              MainFrame.getMainInstance().getReportQueryDialog().openFilterExpressionDialog(true);
          }
          else if (isNode(xpath, "style"))
          {
             int itemIndex = getNodeIndex(xpath);
             Style style = (Style)report.getStyles().get(itemIndex);
             StyleDialog cd = new StyleDialog(MainFrame.getMainInstance(),true);
             cd.setStyle( style );
             xpath = childNode(xpath);
             if (isNode(xpath, "conditionalStyle"))
             {
                 itemIndex = getNodeIndex(xpath);
                 String subPath = childNode(xpath);
                 if (isNode(subPath, "conditionExpression"))
                 {
                    cd.setOpenCondition(itemIndex);
                 }
             }
             cd.setVisible(true);
          }
          else if (isNode(xpath, "group"))
          {
             int itemIndex = getNodeIndex(xpath);
             Group group = (Group)report.getGroups().get(itemIndex);
                
             xpath = childNode(xpath);
             if (isNode(xpath, "groupExpression"))
             {
                 
                 MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(group.getGroupHeader());
                 MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("groupExpression",errorString);
                 
                 /*
                 JRGroupDialog gd = new JRGroupDialog( MainFrame.getMainInstance(),true);
                 gd.setSubdataset( report );
                 gd.setGroup(group);
                 gd.setFocusedExpression( SubDatasetDialog.COMPONENT_FILTER_EXPRESSION );
                 gd.setVisible(true);
                 */
             }
             else if (isNode(xpath, "groupHeader") || isNode(xpath, "groupFooter"))
             {
                 boolean isHeader = isNode(xpath, "groupHeader");
                 xpath = childNode(xpath); // /band
                 xpath = childNode(xpath); // inside the band...
                 parseBand(report, (isHeader) ? group.getGroupHeader() : group.getGroupFooter(), xpath);
             }
             
          }
          else if (isNode(xpath, "subDataset"))
          {
             int itemIndex = getNodeIndex(xpath);
             SubDataset subdataset = (SubDataset)report.getSubDatasets().get(itemIndex);
             xpath = childNode(xpath);
             
             parseSubDataset(subdataset, childNode(xpath) );
             
          }
          else if (isNode(xpath, BANDS ))
          {
             if (xpath.indexOf("[1]") > 0)
             {
                String bandName = xpath.substring(1, xpath.indexOf("[1]"));
                xpath = childNode(xpath); // /band
                xpath = childNode(xpath); // inside the band...
                parseBand(report, report.getBandByName(bandName), xpath);
             }
          } 
    }
    
    
    /**
     * Get the variable number "number". Number must be 0 indexed.
     * The method skip all the built-in variables
     */
    private JRVariable getVariableAt(SubDataset ds, int number)
    {
        Enumeration var_enum = ds.getVariables().elements();
        int count = 0;
        while (var_enum.hasMoreElements())
        {
            JRVariable var = (JRVariable)var_enum.nextElement();
            if (!var.isBuiltin())
            {
                if (number == count)
                {
                    return var;
                }
                count++;
            }
        }
        
        return null;
    }
    
    /**
     * Get the parameter number "number". Number must be 0 indexed.
     * The method skip all the built-in parameters
     */
    private JRParameter getParameterAt(SubDataset ds, int number)
    {
        Enumeration par_enum = ds.getParameters().elements();
        int count = 0;
        while (par_enum.hasMoreElements())
        {
            JRParameter par = (JRParameter)par_enum.nextElement();
            if (!par.isBuiltin())
            {
                if (number == count)
                {
                    return par;
                }
                count++;
            }
        }
        
        return null;
    }
    
    
    /**
     * Opens the variable dialog and highlights the bagous expression
     */
    public void parseVariable(SubDataset subDataset, JRVariable variable, String xpath)
    {
        if (isNode(xpath, "variableExpression"))
        {
            MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyVariable( variable, subDataset, JRVariableDialog.COMPONENT_VARIABLE_EXPRESSION );
        }
        else if (isNode(xpath, "initialValueExpression"))
        {
            MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyVariable( variable, subDataset, JRVariableDialog.COMPONENT_VARIABLE_INIT_EXPRESSION );
        }
    }
    
    /**
     * Open the parameter dialog and highlights the bagous expression
     */
    public void parseParameter(SubDataset subDataset, JRParameter param, String xpath)
    {
        if (isNode(xpath, "defaultValueExpression"))
        {
            MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyParameter( param, subDataset, JRParameterDialog.COMPONENT_DEFAULT_EXPRESSION );
        }
        else
        {
            MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyParameter( param, subDataset );
        }
    }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    }

    private void parseSubDataset(SubDataset subdataset, String xpath) {
             
            if (isNode(xpath, "filterExpression"))
            {
                 SubDatasetDialog sdd = new SubDatasetDialog(MainFrame.getMainInstance(),true);
                 sdd.setSubDataset(subdataset);
                 MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(subdataset);
                 sdd.setFocusedExpression( SubDatasetDialog.COMPONENT_FILTER_EXPRESSION );
                 sdd.setVisible(true);
            }
            else if (isNode(xpath, "group"))
            {
                int itemIndex = getNodeIndex(xpath);
                Group grp = (Group)subdataset.getGroups().get( itemIndex );
                //System.out.println("Pre Group child " + xpath);
                //xpath = childNode(xpath);
                
                if (isNode(xpath, "groupExpression"))
                {
                    GroupsDialog.modifyGroup(subdataset, grp, MainFrame.getMainInstance(), GroupDialog.COMPONENT_EXPRESSION );
                }
            }
    }

    private void parseBand(Report report, Band band, String xpath) {
        
        if (isNode(xpath, "printWhenExpression"))
        {
            if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
            {
                 this.getJReportFrame().setSelectedCrosstabEditor( null );
            }
            MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(band);
            MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("printWhenExpression",errorString);
        }
        else if (isNode(xpath, ELEMENTS))
        {
             // find the right element...
             Vector bandElements = new Vector();
             for (int i=0; i<report.getElements().size(); ++i)
             {
                 ReportElement bElement = (ReportElement)report.getElements().get(i);
                 if (bElement.getBand() != null && bElement.getBand() == band)
                 {
                     bandElements.add(bElement);
                 }
             }
             ReportElement re = findElement(bandElements, xpath); 
             if (re == null) return;
             
             String subXpath = childNode(xpath); 
             if (isNode(subXpath, "reportElement"))
             {
                 subXpath = childNode(subXpath); 
                 parseReportElement(re, subXpath, null);
             }
             else if (isNode(subXpath, "chart"))
             {
                 subXpath = childNode(subXpath); 

                 if (isNode(subXpath, "reportElement"))
                 {
                    subXpath = childNode(subXpath); 
                    parseReportElement(re, subXpath, null);
                 }
                 else //if (isNode(subXpath, new String[]{"anchorNameExpression","hyperlinkReferenceExpression","hyperlinkAnchorExpression","hyperlinkPageExpression","hyperlinkTooltipExpression","hyperlinkParameter"}))
                 {
                    parseElement(re, subXpath, null);
                 }            
             } 
             else
             {
                 parseElement(re, subXpath, null);
             }
        }
        else if (isNode(xpath, "elementGroup"))
        {
            parseBand(report, band, childNode(xpath));
        }
    }
    
    /**
     * parse element specific errors like the content of
     * a crosstab, an image expression, a textfield expression, etc...
     */
    public void parseElement(ReportElement re, String xpath, CrosstabReportElement cre)
    {
        //String subPath = childNode(xpath);
        if (xpath.length() == 0)
        {
            System.out.println("No sub path for xpath " + xpath);
            if (cre == null)
            {
                if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                {
                     this.getJReportFrame().setSelectedCrosstabEditor( null );
                }
                MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
            }
            else
            {
                CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                    this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                {
                    this.getJReportFrame().setSelectedCrosstabEditor( cre );
                }
                ceditor.getPanelEditor().setSelectedElement( re );
            }
            return;
        }
        
        if (re instanceof HyperLinkableReportElement)
        {
            int expressionID = -1;
            int elementID = -1;
            int subexpressionID = -1;
                    
            if (isNode(xpath, "anchorNameExpression")) expressionID = ElementPropertiesDialog.COMPONENT_ANCHORNAME_EXPRESSION;
            else if (isNode(xpath, "hyperlinkReferenceExpression")) expressionID = ElementPropertiesDialog.COMPONENT_HYPERLINK_REFERENCE_EXPRESSION;
            else if (isNode(xpath, "hyperlinkAnchorExpression")) expressionID = ElementPropertiesDialog.COMPONENT_HYPERLINK_ANCHOR_EXPRESSION;
            else if (isNode(xpath, "hyperlinkPageExpression")) expressionID = ElementPropertiesDialog.COMPONENT_HYPERLINK_PAGE_EXPRESSION;
            else if (isNode(xpath, "hyperlinkTooltipExpression")) expressionID = ElementPropertiesDialog.COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION;
            else if (isNode(xpath, "hyperlinkParameter")){
                
                expressionID = ElementPropertiesDialog.COMPONENT_HYPERLINK_PARAMETERS;
                elementID = getNodeIndex(xpath);
                
                xpath = childNode(xpath);
                if (isNode(xpath, "hyperlinkParameterExpression"))
                {
                    subexpressionID = JRLinkParameterDialog.COMPONENT_PARAM_EXPRESSION;
                }
                // ...
            }
            
            if (expressionID > 0)
            {
                if (cre == null)
                {
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                         this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                }
                else
                {
                    CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                        this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( cre );
                    }
                    ceditor.getPanelEditor().setSelectedElement( re );
                }
                MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
                MainFrame.getMainInstance().getElementPropertiesDialog().setFocusedExpression(expressionID, elementID);
                if (subexpressionID > 0)
                {
                    MainFrame.getMainInstance().getElementPropertiesDialog().modifyLinkParameter(subexpressionID);
                }
                // path completely resolved
                return;
            }
        }
        
        if (re instanceof ImageReportElement)
        {
            if (isNode(xpath, "imageExpression"))
            {
                if (cre == null)
                {
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                         this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                    MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("imageExpression",errorString);
                }
                else
                {
                    CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                        this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( cre );
                    }
                    ceditor.getPanelEditor().setSelectedElement( re );
                    MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("imageExpression",errorString);
                }
            }
        }
        else if (re instanceof TextFieldReportElement)
        {
            if (isNode(xpath, "textFieldExpression"))
            {
                if (cre == null)
                {
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                         this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                    MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("textfieldExpression",errorString);
                }
                else
                {
                    CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                        this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( cre );
                    }
                    ceditor.getPanelEditor().setSelectedElement( re );
                    MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("textfieldExpression",errorString);
                }
            }
        }
        else if (re instanceof ChartReportElement2)
        {
            if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
            {
                 this.getJReportFrame().setSelectedCrosstabEditor( null );
            }
            MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
            
            boolean openChartFrame = false;
            String errorLabel = null;
            Object[] expressionInfo = null;
            
            if (isNode(xpath, "chartTitle"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "titleExpression"))
                {
                    openChartFrame = true;
                    errorLabel = "chartTitle";
                }
            }
            else if (isNode(xpath, "chartSubtitle"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "subtitleExpression"))
                {
                    openChartFrame = true;
                    errorLabel = "chartSubTitle";
                }
            }
            else if (isNode(xpath, "barPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "categoryAxisLabelExpression")) {
                    errorLabel = "categoryAxisLabelExpressionBarPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionBarPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "bar3DPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "categoryAxisLabelExpression")) {
                    errorLabel = "categoryAxisLabelExpressionBar3DPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionBar3DPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "linePlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "categoryAxisLabelExpression")) {
                    errorLabel = "categoryAxisLabelExpressionLinePlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionLinePlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "areaPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "categoryAxisLabelExpression")) {
                    errorLabel = "categoryAxisLabelExpressionAreaPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionAreaPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "scatterPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "xAxisLabelExpression")) {
                    errorLabel = "xAxisLabelExpressionScatterPlot";
                }
                else if (isNode(xpath, "yAxisLabelExpression")){
                    errorLabel = "yAxisLabelExpressionScatterPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "bubblePlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "xAxisLabelExpression")) {
                    errorLabel = "xAxisLabelExpressionBubblePlot";
                }
                else if (isNode(xpath, "yAxisLabelExpression")){
                    errorLabel = "yAxisLabelExpressionBubblePlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "timeSeriesPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "timeAxisLabelExpression")) {
                    errorLabel = "timeAxisLabelExpressionTimeSeriesPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionTimeSeriesPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "highLowPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "timeAxisLabelExpression")) {
                    errorLabel = "timeAxisLabelExpressionHighLowPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionHighLowPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "candlestickPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "timeAxisLabelExpression")) {
                    errorLabel = "timeAxisLabelExpressionCandlestickPlot";
                }
                else if (isNode(xpath, "valueAxisLabelExpression")){
                    errorLabel = "valueAxisLabelExpressionCandlestickPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "meterPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "dataRange")) {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "lowExpression")) {
                        errorLabel = "dataRangeLowExpressionMeterPlot";
                    } else if (isNode(xpath, "highExpression")) {
                        errorLabel = "dataRangeHighExpressionMeterPlot";
                    }
                }
                else if (isNode(xpath, "meterInterval")) {
                    int intervalIndex = getNodeIndex(xpath);
                    expressionInfo = new Object[]{
                            new Integer(ChartPropertiesDialog.COMPONENT_METER_INTERVALS),
                            errorString,
                            new Integer(MeterIntervalsDialog.COMPONENT_INTERVALS_LIST),
                            new Integer(intervalIndex), };
                    
                    xpath = childNode(xpath);
                    if (isNode(xpath, "dataRange"))
                    {
                        xpath = childNode(xpath);
                        if (isNode(xpath, "lowExpression")) expressionInfo = appendToArray(expressionInfo, new Integer( MeterIntervalDialog.COMPONENT_LOW_EXPRESSION ));
                        else if (isNode(xpath, "highExpression")) expressionInfo = appendToArray(expressionInfo, new Integer( MeterIntervalDialog.COMPONENT_HIGH_EXPRESSION ));
                        else expressionInfo = appendToArray(expressionInfo, new Integer( MeterIntervalDialog.COMPONENT_NONE ));

                    }
                    else
                    {
                        expressionInfo = appendToArray(expressionInfo, new Integer( MeterIntervalDialog.COMPONENT_NONE ));
                    }
                    
                    //    errorLabel = "timeAxisLabelExpressionCandlestickPlot";
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "thermometerPlot"))
            {
                xpath = childNode(xpath);
                if (isNode(xpath, "dataRange")) {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "lowExpression")) { errorLabel = "dataRangeLowExpressionThermometerPlot"; }
                    else if (isNode(xpath, "highExpression")) { errorLabel = "dataRangeHighExpressionThermometerPlot"; }
                }
                else if (isNode(xpath, "lowRange")) {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "dataRange")) { xpath = childNode(xpath);
                        if (isNode(xpath, "lowExpression")) { errorLabel = "lowRangeLowExpressionThermometerPlot"; }
                        else if (isNode(xpath, "highExpression")) { errorLabel = "lowRangeHighExpressionThermometerPlot"; }
                    }
                }
                else if (isNode(xpath, "mediumRange")) {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "dataRange")) { xpath = childNode(xpath);
                        if (isNode(xpath, "lowExpression")) { errorLabel = "mediumRangeLowExpressionThermometerPlot"; }
                        else if (isNode(xpath, "highExpression")) { errorLabel = "mediumRangeHighExpressionThermometerPlot"; }
                   }
                }
                else if (isNode(xpath, "highRange")) {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "dataRange")) { xpath = childNode(xpath);
                        if (isNode(xpath, "lowExpression")) { errorLabel = "highRangeLowExpressionThermometerPlot"; }
                        else if (isNode(xpath, "highExpression")) { errorLabel = "highRangeHighExpressionThermometerPlot"; }
                    }
                }
                openChartFrame = true;
            }
            else if (isNode(xpath, "pieDataset")) {
                xpath = childNode(xpath);
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "keyExpression"))
                {
                    expressionInfo = new Object[]{
                            new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION),
                            new Integer(PieDatasetPanel.COMPONENT_KEY_EXPRESSION)
                        };
                } else if  (isNode(xpath, "valueExpression"))
                {
                    expressionInfo = new Object[]{
                            new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION),
                            new Integer(PieDatasetPanel.COMPONENT_VALUE_EXPRESSION)
                        };
                } else if (isNode(xpath, "labelExpression"))
                {
                    expressionInfo = new Object[]{
                            new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION),
                            new Integer(PieDatasetPanel.COMPONENT_LABEL_EXPRESSION)
                        };
                } else if (isNode(xpath, "sectionHyperlink"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = new Object[]{
                            new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION),
                            new Integer(PieDatasetPanel.COMPONENT_HYPERLINK)
                        };
                        
                    expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                }
                
            }
            else if (isNode(xpath, "categoryDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "categorySeries"))
                {
                    expressionInfo = appendToArray(expressionInfo, new Integer( CategoryDatasetPanel.COMPONENT_CATEGORY_SERIES_LIST ));
                    expressionInfo = appendToArray(expressionInfo, new Integer( getNodeIndex(xpath) ));
                    xpath = childNode(xpath);
                    if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( CategorySeriesDialog.COMPONENT_SERIES_EXPRESSION )); }
                    else if (isNode(xpath, "categoryExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( CategorySeriesDialog.COMPONENT_CATEGORY_EXPRESSION )); }
                    else if (isNode(xpath, "valueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( CategorySeriesDialog.COMPONENT_VALUE_EXPRESSION )); }
                    else if (isNode(xpath, "labelExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( CategorySeriesDialog.COMPONENT_LABEL_EXPRESSION )); }
                    else if (isNode(xpath, "itemHyperlink"))
                    {
                        xpath = childNode(xpath);
                        expressionInfo = appendToArray(expressionInfo, new Integer( CategorySeriesDialog.COMPONENT_HYPERLINK ));
                        expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                    }
                }
            }
            else if (isNode(xpath, "timePeriodDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "timePeriodSeries"))
                {
                    expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodDatasetPanel.COMPONENT_PERIOD_SERIES_LIST ));
                    expressionInfo = appendToArray(expressionInfo, new Integer( getNodeIndex(xpath) ));
                    xpath = childNode(xpath);
                    if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_SERIES_EXPRESSION )); }
                    else if (isNode(xpath, "startDateExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_START_DATE_EXPRESSION )); }
                    else if (isNode(xpath, "endDateExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_END_DATE_EXPRESSION )); }
                    else if (isNode(xpath, "valueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_VALUE_EXPRESSION )); }
                    else if (isNode(xpath, "labelExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_LABEL_EXPRESSION )); }
                    else if (isNode(xpath, "itemHyperlink"))
                    {
                        xpath = childNode(xpath);
                        expressionInfo = appendToArray(expressionInfo, new Integer( TimePeriodSeriesDialog.COMPONENT_HYPERLINK ));
                        expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                    }
                }
            }
            else if (isNode(xpath, "xyDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "xySeries"))
                {
                    expressionInfo = appendToArray(expressionInfo, new Integer( XYDatasetPanel.COMPONENT_XY_LIST ));
                    expressionInfo = appendToArray(expressionInfo, new Integer( getNodeIndex(xpath) ));
                    xpath = childNode(xpath);
                    if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYSeriesDialog.COMPONENT_SERIES_EXPRESSION )); }
                    else if (isNode(xpath, "xValueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYSeriesDialog.COMPONENT_X_EXPRESSION )); }
                    else if (isNode(xpath, "yValueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYSeriesDialog.COMPONENT_Y_EXPRESSION )); }
                    else if (isNode(xpath, "labelExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYSeriesDialog.COMPONENT_LABEL_EXPRESSION )); }
                    else if (isNode(xpath, "itemHyperlink"))
                    {
                        xpath = childNode(xpath);
                        expressionInfo = appendToArray(expressionInfo, new Integer( XYSeriesDialog.COMPONENT_HYPERLINK ));
                        expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                    }
                }
            }
            else if (isNode(xpath, "xyzDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "xyzSeries"))
                {
                    expressionInfo = appendToArray(expressionInfo, new Integer( XYZDatasetPanel.COMPONENT_XYZ_LIST ));
                    expressionInfo = appendToArray(expressionInfo, new Integer( getNodeIndex(xpath) ));
                    xpath = childNode(xpath);
                    if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYZSeriesDialog.COMPONENT_SERIES_EXPRESSION )); }
                    else if (isNode(xpath, "xValueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYZSeriesDialog.COMPONENT_X_EXPRESSION )); }
                    else if (isNode(xpath, "yValueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYZSeriesDialog.COMPONENT_Y_EXPRESSION )); }
                    else if (isNode(xpath, "zValueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( XYZSeriesDialog.COMPONENT_Z_EXPRESSION )); }
                    else if (isNode(xpath, "itemHyperlink"))
                    {
                        xpath = childNode(xpath);
                        expressionInfo = appendToArray(expressionInfo, new Integer( XYZSeriesDialog.COMPONENT_HYPERLINK ));
                        expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                    }
                }
            }
            else if (isNode(xpath, "timeSeriesDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "timeSeries"))
                {
                    expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDatasetPanel.COMPONENT_TIME_SERIES_LIST ));
                    expressionInfo = appendToArray(expressionInfo, new Integer( getNodeIndex(xpath) ));
                    xpath = childNode(xpath);
                    if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDialog.COMPONENT_SERIES_EXPRESSION )); }
                    else if (isNode(xpath, "timePeriodExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDialog.COMPONENT_TIME_EXPRESSION )); }
                    else if (isNode(xpath, "valueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDialog.COMPONENT_VALUE_EXPRESSION )); }
                    else if (isNode(xpath, "labelExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDialog.COMPONENT_LABEL_EXPRESSION )); }
                    else if (isNode(xpath, "itemHyperlink"))
                    {
                        xpath = childNode(xpath);
                        expressionInfo = appendToArray(expressionInfo, new Integer( TimeSeriesDialog.COMPONENT_HYPERLINK ));
                        expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                    }
                }
            }
            else if (isNode(xpath, "highLowDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_SERIES_EXPRESSION )); }
                else if (isNode(xpath, "seriesExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_SERIES_EXPRESSION )); }
                else if (isNode(xpath, "dateExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_DATA_EXPRESSION )); }
                else if (isNode(xpath, "highExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_HIGH_EXPRESSION )); }
                else if (isNode(xpath, "lowExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_LOW_EXPRESSION )); }
                else if (isNode(xpath, "openExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_OPEN_EXPRESSION )); }
                else if (isNode(xpath, "closeExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_CLOSE_EXPRESSION )); }
                else if (isNode(xpath, "volumeExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_VOLUME_EXPRESSION )); }
                else if (isNode(xpath, "itemHyperlink"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = appendToArray(expressionInfo, new Integer( HighLowDatasetPanel.COMPONENT_HYPERLINK ));
                    expressionInfo = addSectionHyperlinkExpressionsInfo(expressionInfo, xpath);
                }
            }
            else if (isNode(xpath, "valueDataset")) {
                xpath = childNode(xpath);
                expressionInfo = new Object[]{new Integer(ChartPropertiesDialog.COMPONENT_DATASET_SPECIFIC_EXPRESSION)};
                
                if (isNode(xpath, "dataset"))
                {
                    xpath = childNode(xpath);
                    expressionInfo = parseDataset(xpath);
                }
                else if (isNode(xpath, "valueExpression")) { expressionInfo = appendToArray(expressionInfo, new Integer( ValueDatasetPanel.COMPONENT_VALUE_EXPRESSION )); }
            }
            
            if (openChartFrame || expressionInfo != null)
            {
                it.businesslogic.ireport.chart.gui.ChartPropertiesDialog cpd = new it.businesslogic.ireport.chart.gui.ChartPropertiesDialog(MainFrame.getMainInstance(),true);
                    
                try {
                    cpd.setJReportFrame( getJReportFrame() );
                    cpd.setChartElement((ChartReportElement2)re);
                    if (errorLabel != null)
                    {
                        cpd.setPropertyLabelError(errorLabel ,errorString);
                    }
                    else if (expressionInfo != null)
                    {
                        cpd.setFocusedExpression( expressionInfo );
                    }
                    cpd.setVisible(true);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        else if (re instanceof SubReportElement)
        {
            int expressionID = -1;
            int elementID = -1;
            int subexpressionID = -1;
                    
            if (isNode(xpath, "parametersMapExpression")) expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_MAP_EXPRESSION;
            else if (isNode(xpath, "connectionExpression")) expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_DS_CONN_EXPRESSION;
            else if (isNode(xpath, "dataSourceExpression")) expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_DS_CONN_EXPRESSION;
            else if (isNode(xpath, "subreportExpression")) expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_EXPRESSION;
            else if (isNode(xpath, "subreportParameter")){
                
                expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_PARAMETERS;
                elementID = getNodeIndex(xpath);
                
                xpath = childNode(xpath);
                if (isNode(xpath, "subreportParameterExpression"))
                {
                    subexpressionID = JRSubreportParameterDialog.COMPONENT_PARAM_EXPRESSION;
                }
                // ...
            }
            else if (isNode(xpath, "returnValue")){
                
                expressionID = ElementPropertiesDialog.COMPONENT_SUBREPORT_RETURN_VALUES;
                elementID = getNodeIndex(xpath);
                
                //xpath = childNode(xpath);
                //if (isNode(xpath, "hyperlinkParameterExpression"))
                // {
                //    subexpressionID = JRLinkParameterDialog.COMPONENT_PARAM_EXPRESSION;
                // }
                // ...
            }
            
            if (expressionID > 0)
            {
                if (cre == null)
                {
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                         this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                }
                else
                {
                    CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                        this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( cre );
                    }
                    ceditor.getPanelEditor().setSelectedElement( re );
                }
                MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
                MainFrame.getMainInstance().getElementPropertiesDialog().setFocusedExpression(expressionID, elementID);
                if (subexpressionID > 0)
                {
                    MainFrame.getMainInstance().getElementPropertiesDialog().modifySubreportParameter(subexpressionID);
                }
            }
        }
        else if (re instanceof CrosstabReportElement)
         {
             int expressionID = -1;
             int elementID = -1;
             int subexpressionID = -1;
                    
             if (isNode(xpath, "parametersMapExpression")) expressionID = ElementPropertiesDialog.COMPONENT_CROSSTAB_MAP_EXPRESSION;
             else if (isNode(xpath, "crosstabParameter")){
                
                expressionID = ElementPropertiesDialog.COMPONENT_CROSSTAB_PARAMETERS;
                elementID = getNodeIndex(xpath);
                
                xpath = childNode(xpath);
                if (isNode(xpath, "parameterValueExpression"))
                {
                    subexpressionID = CrosstabParameterDialog.COMPONENT_PARAM_EXPRESSION;
                }
                // ...
             }
             else if (xpath.indexOf("/cellContents") >= 0) // check if we are working with the content of a cell...
             {
                 String cellContentPath = xpath.substring(xpath.indexOf("/cellContents"));
                 cellContentPath = childNode(cellContentPath); 
                 CrosstabCell cell = findCell((CrosstabReportElement)re, xpath);

                 ReportElement sre = findElement( ((CrosstabReportElement)re).getElements(), cellContentPath, cell);
                 
                 if (sre != null)
                 {
                    String elementPath = childNode(cellContentPath); 
                    if (isNode(elementPath, "reportElement"))
                    {
                        elementPath = childNode(elementPath);
                        parseReportElement(sre, elementPath, (CrosstabReportElement)re);
                    }
                    else
                    {
                        parseElement(sre, elementPath, (CrosstabReportElement)re);
                    }
                 }
             }
             else if (isNode(xpath, "rowGroup")) // check if we are working with the content of a cell...
             {
                    int groupIndex = getNodeIndex(xpath);
                    xpath = childNode(xpath);
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                 
                    if (isNode(xpath, "bucket"))
                    {
                        xpath = childNode(xpath);
                        int groupExp = CrosstabGroupDialog.COMPONENT_NONE;
                        if (isNode(xpath,"bucketExpression")) groupExp = CrosstabGroupDialog.COMPONENT_BUCKET_EXPRESSION;
                        else if (isNode(xpath,"comparatorExpression")) groupExp = CrosstabGroupDialog.COMPONENT_COMPARATOR_EXPRESSION;
                        
                        it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
                        cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                        cpd.openRowBucket(groupIndex,groupExp);
                        cpd.setVisible(true);
                    }
             }
             else if (isNode(xpath, "columnGroup")) // check if we are working with the content of a cell...
             {
                    int groupIndex = getNodeIndex(xpath);
                    xpath = childNode(xpath);
                    
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                    
                    if (isNode(xpath, "bucket"))
                    {
                        xpath = childNode(xpath);
                        int groupExp = CrosstabGroupDialog.COMPONENT_NONE;
                        if (isNode(xpath,"bucketExpression")) groupExp = CrosstabGroupDialog.COMPONENT_BUCKET_EXPRESSION;
                        else if (isNode(xpath,"comparatorExpression")) groupExp = CrosstabGroupDialog.COMPONENT_COMPARATOR_EXPRESSION;
                        
                        it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
                        cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                        cpd.openColumnBucket(groupIndex,groupExp);
                        cpd.setVisible(true);
                    }
             }
             else if (isNode(xpath, "measure")) // check if we are working with the content of a cell...
             {
                    int groupIndex = getNodeIndex(xpath);
                    xpath = childNode(xpath);
                    
                    if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                    {
                        this.getJReportFrame().setSelectedCrosstabEditor( null );
                    }
                    MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                    
                    int measureExp = CrosstabGroupDialog.COMPONENT_NONE;
                    if (isNode(xpath,"measureExpression")) measureExp = MeasureDialog.COMPONENT_MEASURE_EXPRESSION;
                       
                    it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
                    cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                    cpd.openMeasure(groupIndex,measureExp);
                    cpd.setVisible(true);
             }
             else if (isNode(xpath, "crosstabDataset"))
             {
                 xpath = childNode(xpath);
                 if (isNode(xpath, "dataset"))
                 {
                    xpath = childNode(xpath);
                    Object[] expressionInfo = parseDataset(xpath);
                    it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
                    cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                    cpd.setFocusedExpression(expressionInfo);
                    cpd.setVisible(true);
                 }
             }
             
             
             if (expressionID > 0)
             {
                 if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                 {
                     this.getJReportFrame().setSelectedCrosstabEditor( null );
                 }
                 MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                 
                 MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
                 MainFrame.getMainInstance().getElementPropertiesDialog().setFocusedExpression(expressionID, elementID);
                 if (subexpressionID > 0)
                {
                    MainFrame.getMainInstance().getElementPropertiesDialog().modifyCrosstabParameter(subexpressionID);
                }
             }
        }
    }
    
    /**
     * Return true if xpath starts with one of the strings in the key array
     * If xpath starts with "/", the first character is removed, so
     * 
     * "/test", {"test"} returns true
     * "test", {"/test"} returns false
     */
    final static private boolean isNode(String xpath, String[] keys)
    {
        if (xpath == null || keys == null) return false;
        if (xpath.startsWith("/")) xpath = xpath.substring(1);
        for (int i=0; i< keys.length; ++i)
        {
            if (isNode(xpath, keys[i])) return true;
        }
        return false;
    }
    
    /**
     * Check if the root node of the path is a nodeName...
     *
     * "/test/dsafasfd/asdfa/dsaf", "test" returns true
     * "test", "/test" returns false
     */
    final static private boolean isNode(String xpath, String nodeName)
    {
        if (xpath == null || nodeName == null || nodeName.length() == 0) return false;
        if (xpath.startsWith("/")) xpath = xpath.substring(1);
        if (xpath.startsWith(nodeName))
        {
            xpath = xpath.substring(nodeName.length());
            if (xpath.length() == 0 || xpath.startsWith("/") || xpath.startsWith("[")) return true;
        }
        return false;
    }
    
    /**
     * This method finds the element pointed by the first node of xpath in the given vector
     * In the element is not found, the method returns null
     */ 
    public ReportElement findElement(Vector elements, String xpath)
    {
        return findElement(elements, xpath,null);
    }
    
    /**
     * This method finds the element pointed by the first node of xpath in the given vector
     * having (if not null) the cell as parent cell
     */ 
    public ReportElement findElement(Vector elements, String xpath, CrosstabCell cell)
    {
        if (elements == null || xpath == null) return null;
        int index =  getNodeIndex(xpath);
        if (xpath.startsWith("/")) xpath = xpath.substring(1);
        String class_name = null;
        String element_type = null;
        for (int i=0; i< ELEMENTS.length; ++i)
        {
            if (isNode(xpath, ELEMENTS[i]))
            {
                class_name = "it.businesslogic.ireport." + ELEMENTS_CLASS[i];
                element_type = ELEMENTS[i];
                break;
            }
        }
        
        Class clazz = null;
        try {
            clazz = Class.forName(class_name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
        // get the right element match...
        for (int i=0; i<elements.size(); ++i)
        {
            ReportElement re = (ReportElement)elements.get(i);
            if (cell != null && re.getCell() != cell) continue;
            
            if (clazz.isInstance( re ) )
            {
                if (re instanceof ChartReportElement2)
                {
                    // more investigation is required...
                    Chart chart = ((ChartReportElement2)re).getChart();
                    if ( (element_type.equals("pie3DChart") && chart instanceof Pie3DChart) ||
                         (element_type.equals("pieChart") && chart instanceof PieChart) ||
                         (element_type.equals("barChart") && chart instanceof BarChart) ||
                         (element_type.equals("bar3DChart") && chart instanceof Bar3DChart) ||
                         (element_type.equals("xyBarChart") && chart instanceof XYBarChart) ||
                         (element_type.equals("stackedBarChart") && chart instanceof StackedBarChart) ||
                         (element_type.equals("stackedBar3DChart") && chart instanceof StackedBar3DChart) ||
                         (element_type.equals("lineChart") && chart instanceof LineChart) ||
                         (element_type.equals("xyLineChart") && chart instanceof XYLineChart) ||
                         (element_type.equals("areaChart") && chart instanceof AreaChart) ||
                         (element_type.equals("xyAreaChart") && chart instanceof XYAreaChart) ||
                         (element_type.equals("scatterChart") && chart instanceof ScatterChart) ||
                         (element_type.equals("bubbleChart") && chart instanceof BubbleChart) ||
                         (element_type.equals("timeSeriesChart") && chart instanceof TimeSeriesChart) ||
                         (element_type.equals("highLowChart") && chart instanceof HighLowChart) ||
                         (element_type.equals("candlestickChart") && chart instanceof CandlestickChart) ||
                         (element_type.equals("meterChart") && chart instanceof MeterChart) ||
                         (element_type.equals("thermometerChart") && chart instanceof ThermometerChart) ||
                         (element_type.equals("multiAxisChart") && chart instanceof MultiAxisChart) ||
                         (element_type.equals("stackedAreaChart") && chart instanceof StackedAreaChart))
                    {
                        if (index == 0) return re;
                        index--;
                    }
                }
                else
                {
                    if (index == 0) return re;
                    index--;
                }
            }
        }
        return null;
    }

    private void parseReportElement(ReportElement re, String xpath, CrosstabReportElement cre) {
        
        if (isNode(xpath, "printWhenExpression"))
        {
            if (cre == null)
            {
                if (this.getJReportFrame().getSelectedCrosstabEditorPanel() != null)
                {
                     this.getJReportFrame().setSelectedCrosstabEditor( null );
                }
                MainFrame.getMainInstance().getDocumentStructurePanel().setSelectedObject(re);
                MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("printWhenExpression",errorString);
            }
            else
            {
                CrosstabEditor ceditor = this.getJReportFrame().getCrosstabEditor(cre);
                if (this.getJReportFrame().getSelectedCrosstabEditorPanel() == null ||
                    this.getJReportFrame().getSelectedCrosstabEditorPanel() != ceditor.getPanelEditor())
                {
                    this.getJReportFrame().setSelectedCrosstabEditor( cre );
                }
                ceditor.getPanelEditor().setSelectedElement( re );
                MainFrame.getMainInstance().getReportSheetPanel().setPropertyLabelError("printWhenExpression",errorString);
            }
        }
    }

    private CrosstabCell findCell(CrosstabReportElement re, String xpath) {
        
        if (isNode(xpath, "crosstabHeaderCell"))
        {
            for (int i=0; i<re.getCells().size(); ++i)
            {
                CrosstabCell cc = (CrosstabCell)re.getCells().get(i);
                if (cc.getType() == CrosstabCell.CT_HEADER_CELL) return cc;
            }
            return null;
        }
        else if (isNode(xpath, "rowGroup"))
        {
            // get the right row group...
            int itemIndex = getNodeIndex(xpath);
            CrosstabGroup cg = (CrosstabGroup)re.getRowGroups().get(itemIndex);
            // find what cell in this group...
            xpath = childNode(xpath);
            
            if (isNode(xpath, "crosstabRowHeader"))
            {
                return cg.getHeaderCell();
            }
            else if (isNode(xpath, "crosstabTotalRowHeader"))
            {
                return cg.getTotalCell();
            }
            
        }
        else if (isNode(xpath, "columnGroup"))
        {
            // get the right row group...
            int itemIndex = getNodeIndex(xpath);
            CrosstabGroup cg = (CrosstabGroup)re.getColumnGroups().get(itemIndex);
            // find what cell in this group...
            xpath = childNode(xpath);
            
            if (isNode(xpath, "crosstabColumnHeader"))
            {
                return cg.getHeaderCell();
            }
            else if (isNode(xpath, "crosstabTotalColumnHeader"))
            {
                return cg.getTotalCell();
            }
        }
        else if (isNode(xpath, "crosstabCell"))
        {
            int cellIndex = getNodeIndex(xpath);
            for (int i=0; i<re.getCells().size(); ++i)
            {
                CrosstabCell cc = (CrosstabCell)re.getCells().get(i);
                if (cc.getType() == CrosstabCell.DETAIL_CELL)
                {
                    if (cellIndex == 0) return cc;
                    cellIndex--;
                }
            }
            return null;
        }
        else if (isNode(xpath, "whenNoDataCell"))
        {
            for (int i=0; i<re.getCells().size(); ++i)
            {
                CrosstabCell cc = (CrosstabCell)re.getCells().get(i);
                if (cc.getType() == CrosstabCell.NODATA_CELL) return cc;
            }
            return null;
        }
        
        return null;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
    
    
    /**
     * Append to startingExpressionInfo all the IDs required to show the error of the sectionHyperlink
     * It is used for charts only.
     */
    public Object[] addSectionHyperlinkExpressionsInfo(Object[] startingExpressionInfo, String xpath)
    {
        int itemToAppend = SectionItemHyperlinkPanel.COMPONENT_NONE;
        if (isNode(xpath, "hyperlinkReferenceExpression"))
        {
            itemToAppend = SectionItemHyperlinkPanel.COMPONENT_HYPERLINK_REFERENCE_EXPRESSION;
        }
        else if (isNode(xpath, "hyperlinkPageExpression"))
        {
            itemToAppend = SectionItemHyperlinkPanel.COMPONENT_HYPERLINK_PAGE_EXPRESSION;
        }
        else if (isNode(xpath, "hyperlinkAnchorExpression"))
        {
            itemToAppend = SectionItemHyperlinkPanel.COMPONENT_HYPERLINK_ANCHOR_EXPRESSION;
        }
        else if (isNode(xpath, "hyperlinkTooltipExpression"))
        {
            itemToAppend = SectionItemHyperlinkPanel.COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION;
        }
        else if (isNode(xpath, "hyperlinkParameter"))
        {
            int paramIndex = getNodeIndex(xpath);
            
            itemToAppend = SectionItemHyperlinkPanel.COMPONENT_HYPERLINK_PARAMETERS;
            startingExpressionInfo = appendToArray(startingExpressionInfo, new Integer(itemToAppend) );
            startingExpressionInfo = appendToArray(startingExpressionInfo, new Integer(paramIndex) );
            xpath = childNode(xpath);
            
            itemToAppend = JRLinkParameterDialog.COMPONENT_NONE;
            if (isNode(xpath, "hyperlinkParameterExpression"))
            {
                itemToAppend = JRLinkParameterDialog.COMPONENT_PARAM_EXPRESSION;
            }
        }

        return appendToArray(startingExpressionInfo, new Integer(itemToAppend) );
    }
    
    /**
     * Append element at the end of array, returning a new array with lenght
     * equals to array.length + 1
     */
    public static final Object[] appendToArray(Object[] array, Object element)
    {
        Object[] newArray = new Object[array.length+1];
        for (int i=0; i<array.length; ++i)
        {
            newArray[i] = array[i];
        }
        newArray[array.length] = element;
        return newArray;
    }
    
    public Object[] parseDataset(String xpath)
    {
                Object[] expressionInfo = null;
                if (isNode(xpath, "incrementWhenExpression"))
                {
                    expressionInfo = new Object[]{ new Integer(ChartPropertiesDialog.COMPONENT_INCREMENT_WHEN_EXPRESSION)};
                }
                else if (isNode(xpath, "datasetRun"))
                {
                    xpath = childNode(xpath);
                    if (isNode(xpath, "parametersMapExpression"))
                    {
                        expressionInfo = new Object[]{ new Integer(ChartPropertiesDialog.COMPONENT_DATASETRUN_MAP_EXPRESSION)};
                    }
                    else if (isNode(xpath, new String[]{"connectionExpression","dataSourceExpression"}))
                    {
                        expressionInfo = new Object[]{ new Integer(ChartPropertiesDialog.COMPONENT_DATASETRUN_DS_CONN_EXPRESSION)};
                    }
                    else if (isNode(xpath, "datasetParameter"))
                    {
                        int paramIndex = getNodeIndex(xpath);
                        expressionInfo = new Object[]{ new Integer(ChartPropertiesDialog.COMPONENT_DATASETRUN_PARAMETERS),
                                new Integer(paramIndex)};
                        xpath = childNode(xpath);
                        if (isNode(xpath, "datasetParameterExpression"))
                        {
                           expressionInfo = appendToArray(expressionInfo, new Integer( JRSubreportParameterDialog.COMPONENT_PARAM_EXPRESSION )); 
                        }
                        else
                        {
                            expressionInfo = appendToArray(expressionInfo, new Integer( JRSubreportParameterDialog.COMPONENT_NONE ));
                        }
                    }
                }
                
                return expressionInfo;
    }

}
