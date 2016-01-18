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
 *
 *
 * ReportRefactor.java
 *
 * Created on May 4, 2007, 2:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.refactoring;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.ChartReportElement2;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.HyperLinkableReportElement;
import it.businesslogic.ireport.ImageReportElement;
import it.businesslogic.ireport.JRLinkParameter;
import it.businesslogic.ireport.JRSubreportParameter;
import it.businesslogic.ireport.JRSubreportReturnValue;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.SubReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.chart.AreaPlot;
import it.businesslogic.ireport.chart.Axis;
import it.businesslogic.ireport.chart.Bar3DPlot;
import it.businesslogic.ireport.chart.BarPlot;
import it.businesslogic.ireport.chart.BubblePlot;
import it.businesslogic.ireport.chart.CandlestickPlot;
import it.businesslogic.ireport.chart.CategoryDataset;
import it.businesslogic.ireport.chart.CategorySeries;
import it.businesslogic.ireport.chart.Chart;
import it.businesslogic.ireport.chart.Dataset;
import it.businesslogic.ireport.chart.HighLowDataset;
import it.businesslogic.ireport.chart.HighLowPlot;
import it.businesslogic.ireport.chart.LinePlot;
import it.businesslogic.ireport.chart.MeterInterval;
import it.businesslogic.ireport.chart.MeterPlot;
import it.businesslogic.ireport.chart.MultiAxisPlot;
import it.businesslogic.ireport.chart.Pie3DPlot;
import it.businesslogic.ireport.chart.PieDataset;
import it.businesslogic.ireport.chart.PiePlot;
import it.businesslogic.ireport.chart.Plot;
import it.businesslogic.ireport.chart.ScatterPlot;
import it.businesslogic.ireport.chart.SectionItemHyperlink;
import it.businesslogic.ireport.chart.ThermometerPlot;
import it.businesslogic.ireport.chart.TimePeriodDataset;
import it.businesslogic.ireport.chart.TimePeriodSeries;
import it.businesslogic.ireport.chart.TimeSeries;
import it.businesslogic.ireport.chart.TimeSeriesDataset;
import it.businesslogic.ireport.chart.TimeSeriesPlot;
import it.businesslogic.ireport.chart.ValueDataset;
import it.businesslogic.ireport.chart.XYDataset;
import it.businesslogic.ireport.chart.XYSeries;
import it.businesslogic.ireport.chart.XYZDataset;
import it.businesslogic.ireport.chart.XYZSeries;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent;
import it.businesslogic.ireport.util.Misc;

import java.util.Vector;

/**
 *
 * @author gtoffoli
 */
public class ReportRefactor {
    
    /*
     * Replace with newClassType the expression class for textfields in elements having expression as TextFieldExpression... 
     * Return a set of changed elements...
     */ 
    public static  java.util.List replaceTextfieldClass(String expression, String newClassType, java.util.List elements)
    {
        java.util.List list = new java.util.ArrayList();
        if (expression == null) return list;
        for (int i=0; i<elements.size(); ++i)
        {
            ReportElement re = (ReportElement)elements.get(i);
            if (re instanceof TextFieldReportElement)
            {
                if ( ((TextFieldReportElement)re).getText().trim().equals( expression ) )
                {
                    ((TextFieldReportElement)re).setMatchingClassExpression( newClassType, true );
                    list.add(re);
                }
            }
        }
        return list;
    }
    
    
    public static boolean  updateDatasetRunExpressions(Dataset dataset, String oldString, String newString)
    {
        boolean add = false;
        if (dataset == null) return false;
        // Check in dataset run parameters...
       if (dataset.getConnectionExpression().indexOf(oldString) >= 0)
        {
            dataset.setConnectionExpression(Misc.string_replace(newString, oldString, dataset.getConnectionExpression()) );
            add=true;
        }

        if (dataset.getDataSourceExpression().indexOf(oldString) >= 0)
        {
            dataset.setDataSourceExpression(Misc.string_replace(newString, oldString, dataset.getDataSourceExpression()) );
            add=true;
        }

        if (dataset.getParametersMapExpression().indexOf(oldString) >= 0)
        {
            dataset.setParametersMapExpression(Misc.string_replace(newString, oldString, dataset.getParametersMapExpression()) );
            add=true;
        }

        for (int k=0; k<dataset.getSubreportParameters().size(); ++k)
        {
            JRSubreportParameter para = (JRSubreportParameter)dataset.getSubreportParameters().get(k);
            if (para.getExpression().indexOf(oldString) >= 0)
            {
                para.setExpression(Misc.string_replace(newString, oldString, para.getExpression()) );
                add=true;
            }
        }
        return add;
    }
    
    /**
     * Update all the strings related to the dataset.
     * If the dataset is a plain dataset (not a chart dataset), the only think checked is the IncrementWhenExpression
     *
     */
    public static boolean  updateDatasetExpressions(Dataset dataset, String oldString, String newString)
    {
        boolean add = false;
        if (dataset == null) return false;
        
        
        if (dataset.getIncrementWhenExpression().indexOf(oldString) >= 0)
        {
            dataset.setIncrementWhenExpression(Misc.string_replace(newString, oldString, dataset.getIncrementWhenExpression()) );
            add=true;
        }
        
        
        if (dataset instanceof PieDataset)
        {
            PieDataset ds = (PieDataset)dataset;

            if (ds.getKeyExpression().indexOf(oldString) >= 0)
            {
                ds.setKeyExpression(Misc.string_replace(newString, oldString, ds.getKeyExpression()) );
                add=true;
            }

            if (ds.getLabelExpression().indexOf(oldString) >= 0)
            {
                ds.setLabelExpression(Misc.string_replace(newString, oldString, ds.getLabelExpression()) );
                add=true;
            }

            if (ds.getValueExpression().indexOf(oldString) >= 0)
            {
                ds.setValueExpression(Misc.string_replace(newString, oldString, ds.getValueExpression()) );
                add=true;
            }

            if (updateSectionHyperLink( ds.getSectionHyperLink(), oldString, newString ) )
            {
                add=true;
            }

        } 
        else if (dataset instanceof CategoryDataset)
        {
            CategoryDataset ds = (CategoryDataset)dataset;

            for (int k=0; k<ds.getCategorySeries().size(); ++k)
            {
                CategorySeries series = (CategorySeries)ds.getCategorySeries().get(k);
                if (updateSectionHyperLink( series.getSectionItemHyperlink(), oldString, newString ) )
                {
                    add=true;
                }

                if (series.getCategoryExpression().indexOf(oldString) >= 0)
                {
                    series.setCategoryExpression(Misc.string_replace(newString, oldString, series.getCategoryExpression()) );
                    add=true;
                }

                if (series.getLabelExpression().indexOf(oldString) >= 0)
                {
                    series.setLabelExpression(Misc.string_replace(newString, oldString, series.getLabelExpression()) );
                    add=true;
                }

                if (series.getSeriesExpression().indexOf(oldString) >= 0)
                {
                    series.setSeriesExpression(Misc.string_replace(newString, oldString, series.getSeriesExpression()) );
                    add=true;
                }

                if (series.getValueExpression().indexOf(oldString) >= 0)
                {
                    series.setValueExpression(Misc.string_replace(newString, oldString, series.getValueExpression()) );
                    add=true;
                }

            }
         }
        else if (dataset instanceof HighLowDataset)
        {
            HighLowDataset ds = (HighLowDataset)dataset;

            if (updateSectionHyperLink( ds.getItemHyperLink(), oldString, newString ) )
            {
                add=true;
            }

            if (ds.getCloseExpression().indexOf(oldString) >= 0)
            {
                ds.setCloseExpression(Misc.string_replace(newString, oldString, ds.getCloseExpression()) );
                add=true;
            }

            if (ds.getDateExpression().indexOf(oldString) >= 0)
            {
                ds.setDateExpression(Misc.string_replace(newString, oldString, ds.getDateExpression()) );
                add=true;
            }

            if (ds.getHighExpression().indexOf(oldString) >= 0)
            {
                ds.setHighExpression(Misc.string_replace(newString, oldString, ds.getHighExpression()) );
                add=true;
            }

            if (ds.getLowExpression().indexOf(oldString) >= 0)
            {
                ds.setLowExpression(Misc.string_replace(newString, oldString, ds.getLowExpression()) );
                add=true;
            }

            if (ds.getOpenExpression().indexOf(oldString) >= 0)
            {
                ds.setOpenExpression(Misc.string_replace(newString, oldString, ds.getOpenExpression()) );
                add=true;
            }

        }
        else if (dataset instanceof TimePeriodDataset)
        {
            TimePeriodDataset ds = (TimePeriodDataset)dataset;

            for (int k=0; k<ds.getTimePeriodSeries().size(); ++k)
            {
                TimePeriodSeries series = (TimePeriodSeries)ds.getTimePeriodSeries().get(k);
                if (updateSectionHyperLink( series.getSectionItemHyperlink(), oldString, newString ) )
                {
                    add=true;
                }

                if (series.getEndDateExpression().indexOf(oldString) >= 0)
                {
                    series.setEndDateExpression(Misc.string_replace(newString, oldString, series.getEndDateExpression()) );
                    add=true;
                }

                if (series.getLabelExpression().indexOf(oldString) >= 0)
                {
                    series.setLabelExpression(Misc.string_replace(newString, oldString, series.getLabelExpression()) );
                    add=true;
                }

                if (series.getSeriesExpression().indexOf(oldString) >= 0)
                {
                    series.setSeriesExpression(Misc.string_replace(newString, oldString, series.getSeriesExpression()) );
                    add=true;
                }

                if (series.getStartDateExpression().indexOf(oldString) >= 0)
                {
                    series.setStartDateExpression(Misc.string_replace(newString, oldString, series.getStartDateExpression()) );
                    add=true;
                }

                if (series.getValueExpression().indexOf(oldString) >= 0)
                {
                    series.setValueExpression(Misc.string_replace(newString, oldString, series.getValueExpression()) );
                    add=true;
                }

            }
        }
        else if (dataset instanceof TimeSeriesDataset)
        {
            TimeSeriesDataset ds = (TimeSeriesDataset)dataset;

            for (int k=0; k<ds.getTimeSeries().size(); ++k)
            {
                TimeSeries series = (TimeSeries)ds.getTimeSeries().get(k);
                if (updateSectionHyperLink( series.getSectionItemHyperlink(), oldString, newString ) )
                {
                    add=true;
                }

                if (series.getTimePeriodExpression().indexOf(oldString) >= 0)
                {
                    series.setTimePeriodExpression(Misc.string_replace(newString, oldString, series.getTimePeriodExpression()) );
                    add=true;
                }

                if (series.getLabelExpression().indexOf(oldString) >= 0)
                {
                    series.setLabelExpression(Misc.string_replace(newString, oldString, series.getLabelExpression()) );
                    add=true;
                }

                if (series.getSeriesExpression().indexOf(oldString) >= 0)
                {
                    series.setSeriesExpression(Misc.string_replace(newString, oldString, series.getSeriesExpression()) );
                    add=true;
                }

                if (series.getValueExpression().indexOf(oldString) >= 0)
                {
                    series.setValueExpression(Misc.string_replace(newString, oldString, series.getValueExpression()) );
                    add=true;
                }

            }
        }
        else if (dataset instanceof ValueDataset)
        {
            ValueDataset ds = (ValueDataset)dataset;
            if (ds.getValueExpression().indexOf(oldString) >= 0)
            {
                ds.setValueExpression(Misc.string_replace(newString, oldString, ds.getValueExpression()) );
                add=true;
            }
        }
        else if (dataset instanceof XYDataset)
        {
            XYDataset ds = (XYDataset)dataset;

            for (int k=0; k<ds.getXYSeries().size(); ++k)
            {
                XYSeries series = (XYSeries)ds.getXYSeries().get(k);

                if (updateSectionHyperLink( series.getSectionItemHyperlink(), oldString, newString ) )
                {
                    add=true;
                }

                if (series.getLabelExpression().indexOf(oldString) >= 0)
                {
                    series.setLabelExpression(Misc.string_replace(newString, oldString, series.getLabelExpression()) );
                    add=true;
                }

                if (series.getSeriesExpression().indexOf(oldString) >= 0)
                {
                    series.setSeriesExpression(Misc.string_replace(newString, oldString, series.getSeriesExpression()) );
                    add=true;
                }

                if (series.getXValueExpression().indexOf(oldString) >= 0)
                {
                    series.setXValueExpression(Misc.string_replace(newString, oldString, series.getXValueExpression()) );
                    add=true;
                }

                if (series.getYValueExpression().indexOf(oldString) >= 0)
                {
                    series.setYValueExpression(Misc.string_replace(newString, oldString, series.getYValueExpression()) );
                    add=true;
                }

            }
        }
        else if (dataset instanceof XYZDataset)
        {
            XYZDataset ds = (XYZDataset)dataset;

            for (int k=0; k<ds.getXYZSeries().size(); ++k)
            {
                XYZSeries series = (XYZSeries)ds.getXYZSeries().get(k);

                if (updateSectionHyperLink( series.getSectionItemHyperlink(), oldString, newString ) )
                {
                    add=true;
                }

                if (series.getSeriesExpression().indexOf(oldString) >= 0)
                {
                    series.setSeriesExpression(Misc.string_replace(newString, oldString, series.getSeriesExpression()) );
                    add=true;
                }

                if (series.getXValueExpression().indexOf(oldString) >= 0)
                {
                    series.setXValueExpression(Misc.string_replace(newString, oldString, series.getXValueExpression()) );
                    add=true;
                }

                if (series.getYValueExpression().indexOf(oldString) >= 0)
                {
                    series.setYValueExpression(Misc.string_replace(newString, oldString, series.getYValueExpression()) );
                    add=true;
                }

                if (series.getZValueExpression().indexOf(oldString) >= 0)
                {
                    series.setZValueExpression(Misc.string_replace(newString, oldString, series.getZValueExpression()) );
                    add=true;
                }

            }
        }
        
       return add;
    }
    
    /**
     * Replace oldString with newString in all the expressions related to the gived subDataset...
     * The method can fire required events
     *
     *  Checks for:
     *  
     *  - Filter expression
     *  - Query
     *  - variables (initial expression and expression)
     *  - group expressions
     *  
     *  If Report instance:
     *  
     *  - band print when
     *  - elements PrintWhenExpression
     *     - TextFieldReportElement expression
     *     - ImageReportElement imageExpression
     *     - SubReportElement connection, subreportExpression, parametersExpressions, returnValue variable names
     *     - Hyperlinks (anchor, parameters, etc...)
     *     - Crosstab parameters...
     *     - Crosstab and Chart datasetRun
     *
     * TODO: 
     *   - SortFields...
     *   - Visit all the chart expressions checking the used datasource
     *   - Explore sub-elements
     *   - change the class for textfields (if possible)
     *
     * It throws an event is an element is changed... 
     *
     */
     public static void replaceInReportExpressions(String oldString, String newString, SubDataset subDataset, Report referenceReport)
     {
        Report report = null;
        if (subDataset instanceof Report)
        {
            report = (Report)subDataset;
        }
        
        if (subDataset.getFilterExpression() != null && subDataset.getFilterExpression().indexOf(oldString) >= 0)
        {
            subDataset.setFilterExpression( Misc.string_replace(newString, oldString, subDataset.getFilterExpression()) );
            if (report != null)  report.incrementReportChanges();
        }
        
        if (subDataset.getQuery() != null && subDataset.getQuery().indexOf(oldString) >= 0)
        {
            subDataset.setQuery( Misc.string_replace(newString, oldString, subDataset.getQuery()) );
            subDataset.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(
                       new SubDatasetObjectChangedEvent(subDataset, SubDatasetObjectChangedEvent.QUERY, SubDatasetObjectChangedEvent.MODIFIED, subDataset.getQuery(),subDataset.getQuery()));
        }
        
        for (int i=0; i<subDataset.getVariables().size(); ++i)
        {
            JRVariable var = (JRVariable)subDataset.getVariables().get(i);
            boolean fireEvent = false;
            if (var.getInitialValueExpression().indexOf(oldString) >= 0)
            {
                var.setInitialValueExpression( Misc.string_replace(newString, oldString, var.getInitialValueExpression()) );
                // alert
                fireEvent = true;
            }
            
            if (var.getExpression().indexOf(oldString) >= 0)
            {
                var.setExpression( Misc.string_replace(newString, oldString, var.getExpression()) );
                // alert
                fireEvent = true;
            }
            
            if (fireEvent)
            {
               subDataset.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(
                       new SubDatasetObjectChangedEvent(subDataset, SubDatasetObjectChangedEvent.VARIABLE, SubDatasetObjectChangedEvent.MODIFIED, var,var));
            }
        }
        
        for (int i=0; i<subDataset.getGroups().size(); ++i)
        {
            Group grp = (Group)subDataset.getGroups().get(i);
            if (grp.getGroupExpression().indexOf(oldString) >= 0)
            {
                grp.setGroupExpression( Misc.string_replace(newString, oldString, grp.getGroupExpression()) );
            }
        }
        
        Vector changedElements = new Vector();
        
        if (subDataset instanceof Report)
        {
            Report rep = (Report)subDataset;
        
            for (int i=0; i<rep.getBands().size(); ++i)
            {
                Band band = (Band)rep.getBands().get(i);
                if (band.getPrintWhenExpression().indexOf(oldString) >= 0)
                {
                    band.setPrintWhenExpression( Misc.string_replace(newString, oldString, band.getPrintWhenExpression()) );
                }
            }
            
            // Modify elements...
            
            for (int i=0; i<rep.getElements().size(); ++i)
            {
                boolean add=false;
                ReportElement re = (ReportElement)rep.getElements().get(i);
                if (re.getPrintWhenExpression().indexOf(oldString) >= 0)
                {
                    re.setPrintWhenExpression(Misc.string_replace(newString, oldString, re.getPrintWhenExpression()) );
                    add=true;
                }
                
                if (re instanceof TextFieldReportElement)
                {
                    TextFieldReportElement tre = (TextFieldReportElement)re;
                    if (tre.getText().indexOf(oldString) >= 0)
                    {
                        tre.setText(Misc.string_replace(newString, oldString, tre.getText()) );
                        add=true;
                    }
                }
                
                if (re instanceof ImageReportElement)
                {
                    ImageReportElement tre = (ImageReportElement)re;
                    if (tre.getImageExpression().indexOf(oldString) >= 0)
                    {
                        tre.setImageExpression(Misc.string_replace(newString, oldString, tre.getImageExpression()) );
                        add=true;
                    }
                }
                
                if (re instanceof SubReportElement)
                {
                    SubReportElement tre = (SubReportElement)re;
                    if (tre.getSubreportExpression().indexOf(oldString) >= 0)
                    {
                        tre.setSubreportExpression(Misc.string_replace(newString, oldString, tre.getSubreportExpression()) );
                        add=true;
                    }
                    
                    if (tre.getParametersMapExpression().indexOf(oldString) >= 0)
                    {
                        tre.setParametersMapExpression(Misc.string_replace(newString, oldString, tre.getParametersMapExpression()) );
                        add=true;
                    }
                    
                    if (tre.getDataSourceExpression().indexOf(oldString) >= 0)
                    {
                        tre.setDataSourceExpression(Misc.string_replace(newString, oldString, tre.getDataSourceExpression()) );
                        add=true;
                    }
                    
                    if (tre.getConnectionExpression().indexOf(oldString) >= 0)
                    {
                        tre.setConnectionExpression(Misc.string_replace(newString, oldString, tre.getConnectionExpression()) );
                        add=true;
                    }
                    
                    for (int k=0; k<tre.getSubreportParameters().size(); ++k)
                    {
                        JRSubreportParameter para = (JRSubreportParameter)tre.getSubreportParameters().get(k);
                        if (para.getExpression().indexOf(oldString) >= 0)
                        {
                            para.setExpression(Misc.string_replace(newString, oldString, para.getExpression()) );
                            add=true;
                        }
                    }
                    
                    for (int k=0; k<tre.getReturnValues().size(); ++k)
                    {
                        JRSubreportReturnValue para = (JRSubreportReturnValue)tre.getReturnValues().get(k);
                        if (oldString.equals("$V{" + para.getToVariable() + "}"))
                        {
                            String nv = newString.substring(3, newString.length()-1);
                            para.setToVariable(nv);
                            add=true;
                        }
                    }
                }
                
                if (re instanceof HyperLinkableReportElement)
                {
                    HyperLinkableReportElement tre = (HyperLinkableReportElement)re;
                    if (tre.getAnchorNameExpression().indexOf(oldString) >= 0)
                    {
                        tre.setAnchorNameExpression(Misc.string_replace(newString, oldString, tre.getAnchorNameExpression()) );
                        add=true;
                    }
                    if (tre.getHyperlinkAnchorExpression().indexOf(oldString) >= 0)
                    {
                        tre.setHyperlinkAnchorExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkAnchorExpression()) );
                        add=true;
                    }
                    if (tre.getHyperlinkPageExpression().indexOf(oldString) >= 0)
                    {
                        tre.setHyperlinkPageExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkPageExpression()) );
                        add=true;
                    }
                    if (tre.getHyperlinkReferenceExpression().indexOf(oldString) >= 0)
                    {
                        tre.setHyperlinkReferenceExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkReferenceExpression()) );
                        add=true;
                    }
                    for (int k=0; k<tre.getLinkParameters().size(); ++k)
                    {
                        JRLinkParameter para = (JRLinkParameter)tre.getLinkParameters().get(k);
                        if (para.getExpression().indexOf(oldString) >= 0)
                        {
                            para.setExpression(Misc.string_replace(newString, oldString, para.getExpression()) );
                            add=true;
                        }
                    }
                }
                
                if (re instanceof CrosstabReportElement)
                {
                    CrosstabReportElement tre = (CrosstabReportElement)re;
                    
                    for (int k=0; k<tre.getCrosstabParameters().size(); ++k)
                    {
                        CrosstabParameter para = (CrosstabParameter)tre.getCrosstabParameters().get(k);
                        if (para.getParameterValueExpression().indexOf(oldString) >= 0)
                        {
                            para.setParameterValueExpression(Misc.string_replace(newString, oldString, para.getParameterValueExpression()) );
                            add=true;
                        }
                    }
                    
                    if (updateDatasetRunExpressions(tre.getDataset(), oldString, newString))
                    {
                        add=true;
                    }
                    
                    // if the crosstab uses the main report datasource... update all the other expressions too...
                    if (!tre.isUseDataset() || tre.getDataset().getSubDataset() == null)
                    {
                        if ( updateDatasetCrosstabExpressions( tre, oldString, newString)  )
                        {
                            add=true;
                        }
                    }
                }
                
                if (re instanceof ChartReportElement2)
                {
                    // Check in the sundat
                    ChartReportElement2 tre = (ChartReportElement2)re;
                    
                    if (updateDatasetRunExpressions(tre.getChart().getDataset(), oldString, newString))
                    {
                        add=true;
                    }
                    
                    if (tre.getChart().getDataset().getSubDataset() == null )
                    {
                        // update the dataset run and the chart dataset if we the chart element is the same as the element we are modifying
                        if (updateDatasetExpressions(tre.getChart().getDataset(), oldString, newString))
                        {
                            add=true;
                        }
                    }
                }
                
                if (add)
                {
                    changedElements.add(re);
                }
            }
            
            
        }
        else
        {
            if (referenceReport != null)
            {
                // check for crosstabs or charts that refers to this dataset...
                for (int i=0; i<referenceReport.getElements().size(); ++i)
                {
                    boolean add=false;
                    ReportElement re = (ReportElement)referenceReport.getElements().get(i);
                    
                    if (re instanceof CrosstabReportElement)
                    {
                        CrosstabReportElement tre = (CrosstabReportElement)re;
                        if (tre.isUseDataset() && tre.getDataset().getSubDataset() == subDataset)
                        {
                            if ( updateDatasetCrosstabExpressions( tre, oldString, newString)  )
                            {
                                add=true;
                            }
                        }
                    }
                    else if (re instanceof ChartReportElement2)
                    {
                        // Check in the sundat
                        ChartReportElement2 tre = (ChartReportElement2)re;

                        System.out.println("Checking this chart element...");
                        if (tre.getChart().getDataset().getSubDataset() == subDataset )
                        {
                            // update the dataset run and the chart dataset if we the chart element is the same as the element we are modifying
                            if (updateDatasetExpressions(tre.getChart().getDataset(), oldString, newString))
                            {
                                add=true;
                            }
                        }
                    }
                    
                    if (add)
                    {
                        changedElements.add(re);
                    }
                    
                }
                
            }
        }
        
        if (referenceReport != null && changedElements.size() > 0)
            {
                CrosstabReportElement cre = ( referenceReport.getReportFrame().getSelectedCrosstabEditorPanel() != null) ? referenceReport.getReportFrame().getSelectedCrosstabEditorPanel().getCrosstabElement() : null;
                referenceReport.getReportFrame().fireReportListenerReportElementsChanged(
                        new ReportElementChangedEvent(referenceReport.getReportFrame(),cre,changedElements , ReportElementChangedEvent.CHANGED));
                        
                referenceReport.getReportFrame().repaint();
                MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
            }
        
    }
     
    
    /**
     * Replace the strings in an SectionItemHyperlink
     */
    private static boolean updateSectionHyperLink(SectionItemHyperlink tre, String oldString, String newString) {
        
        boolean add = false;
        if (tre.getHyperlinkAnchorExpression().indexOf(oldString) >= 0)
        {
            tre.setHyperlinkAnchorExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkAnchorExpression()) );
            add=true;
        }
        if (tre.getHyperlinkPageExpression().indexOf(oldString) >= 0)
        {
            tre.setHyperlinkPageExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkPageExpression()) );
            add=true;
        }
        if (tre.getHyperlinkReferenceExpression().indexOf(oldString) >= 0)
        {
            tre.setHyperlinkReferenceExpression(Misc.string_replace(newString, oldString, tre.getHyperlinkReferenceExpression()) );
            add=true;
        }
        for (int k=0; k<tre.getHyperlinkParameters().size(); ++k)
        {
            JRLinkParameter para = (JRLinkParameter)tre.getHyperlinkParameters().get(k);
            if (para.getExpression().indexOf(oldString) >= 0)
            {
                para.setExpression(Misc.string_replace(newString, oldString, para.getExpression()) );
                add=true;
            }
        }
        
        return add;
    }

    /**
     * This method update all the expressions that refers to the params/vars/field of the dataset tied to the crosstab.
     * This method should be used when something changes in the crosstab dataset. Please note that a crosstab dataset
     * can be a subdataset or the report itself.
     *
     */
    private static boolean updateDatasetCrosstabExpressions(CrosstabReportElement tre, String oldString, String newString) {
        
        // Check into the crosstab 
        boolean add=false;
        
        if (updateDatasetExpressions( tre.getDataset(), oldString, newString ))
        {
            add=true;
        }
        
        for (int i=0; i<tre.getColumnGroups().size(); ++i)
        {
            CrosstabGroup grp = (CrosstabGroup)tre.getColumnGroups().get(i);
             if (grp.getBucketExpression().indexOf(oldString) >= 0)
            {
                grp.setBucketExpression(Misc.string_replace(newString, oldString, grp.getBucketExpression() ) );
                add=true;
            }
        }
        
        for (int i=0; i<tre.getRowGroups().size(); ++i)
        {
            CrosstabGroup grp = (CrosstabGroup)tre.getRowGroups().get(i);
             if (grp.getBucketExpression().indexOf(oldString) >= 0)
            {
                grp.setBucketExpression(Misc.string_replace(newString, oldString, grp.getBucketExpression() ) );
                add=true;
            }
        }
        
        return add;
    }
    
    
    /**
     * This method update all the expressions that refers to the params/vars/field of the dataset tied to the chart.
     * This method should be used when something changes in the chart dataset. Please note that a chart dataset
     * can be a subdataset or the report itself.
     *
     * Walk trought all the chart expressions....
     * Returns true if at least one expression is changed....
     *
     * chartElement hyperlink is not updated here (since it uses report dataset as all the other elements, infacts a
     * chartElement is automatically treated as HyperLinkableReportElement in replaceInReportExpressions)
     * 
     */
    private static boolean updateDatasetChartExpressions(ChartReportElement2 tre, String oldString, String newString) {
        
        // Check into the crosstab 
        boolean add=false;
        
        if (updateDatasetExpressions( tre. getChart().getDataset(), oldString, newString ))
        {
            add=true;
        }
        
        Chart chart = tre.getChart();
        // hyperlink expressions ignoerd (they are already been checked.
        Plot plot = chart.getPlot();
        
        
        if (chart.getTitle().getTitleExpression().indexOf(oldString) >= 0)
        {
            chart.getTitle().setTitleExpression(Misc.string_replace(newString, oldString, chart.getTitle().getTitleExpression() ) );
            add=true;
        }
        
        if (chart.getSubTitle().getTitleExpression().indexOf(oldString) >= 0)
        {
            chart.getSubTitle().setTitleExpression(Misc.string_replace(newString, oldString, chart.getSubTitle().getTitleExpression() ) );
            add=true;
        }
        
        // Let's check inside Plot...
        if (chart.getPlot() instanceof AreaPlot)
        {
            AreaPlot pl = (AreaPlot)chart.getPlot();
            
            if (pl.getCategoryAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setCategoryAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getCategoryAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof Bar3DPlot)
        {
            Bar3DPlot pl = (Bar3DPlot)chart.getPlot();
            if (pl.getCategoryAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setCategoryAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getCategoryAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof BarPlot)
        {
            BarPlot pl = (BarPlot)chart.getPlot();
            if (pl.getCategoryAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setCategoryAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getCategoryAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof BubblePlot)
        {
            BubblePlot pl = (BubblePlot)chart.getPlot();
            
            if (pl.getXAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setXAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getXAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getYAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setYAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getYAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof CandlestickPlot)
        {
            CandlestickPlot pl = (CandlestickPlot)chart.getPlot();

            if (pl.getTimeAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setTimeAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getTimeAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof HighLowPlot)
        {
            HighLowPlot pl = (HighLowPlot)chart.getPlot();
            if (pl.getTimeAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setTimeAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getTimeAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof LinePlot)
        {
            LinePlot pl = (LinePlot)chart.getPlot();
            if (pl.getCategoryAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setCategoryAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getCategoryAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof MeterPlot)
        {
            MeterPlot pl = (MeterPlot)chart.getPlot();
            
            if (pl.getDataRange().getHighExpression().indexOf(oldString) >= 0)
            {
                pl.getDataRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getHighExpression() ) );
                add=true;
            }
            
            if (pl.getDataRange().getLowExpression().indexOf(oldString) >= 0)
            {
                pl.getDataRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getLowExpression() ) );
                add=true;
            }
            
            for (int k=0; k<pl.getMeterIntervals().size(); ++k)
            {
                MeterInterval mi = (MeterInterval)pl.getMeterIntervals().get(k);
                
                if (mi.getDataRange().getHighExpression().indexOf(oldString) >= 0)
                {
                    mi.getDataRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getHighExpression() ) );
                    add=true;
                }

                if (mi.getDataRange().getLowExpression().indexOf(oldString) >= 0)
                {
                    mi.getDataRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getLowExpression() ) );
                    add=true;
                }
            }
            
        }
        if (chart.getPlot() instanceof MultiAxisPlot)
        {
            MultiAxisPlot pl = (MultiAxisPlot)chart.getPlot();
            for (int k=0; k<pl.getAxis().size(); ++k)
            {
                Axis ax = (Axis)pl.getAxis().get(k);
                if (ax.getChartReportElement().getChart().getDataset() == chart.getDataset() )
                {
                    if (updateDatasetChartExpressions(ax.getChartReportElement(), oldString, newString))
                    {
                        add=true;
                    }
                }
            }
        }
        if (chart.getPlot() instanceof Pie3DPlot)
        {
            Pie3DPlot pl = (Pie3DPlot)chart.getPlot();
            // Nothing to do
        }
        if (chart.getPlot() instanceof PiePlot)
        {
            PiePlot pl = (PiePlot)chart.getPlot();
            // Nothing to do
        }
        if (chart.getPlot() instanceof ScatterPlot)
        {
            ScatterPlot pl = (ScatterPlot)chart.getPlot();
            
            if (pl.getXAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setXAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getXAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getYAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setYAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getYAxisLabelExpression()) );
                add=true;
            }
        }
        if (chart.getPlot() instanceof ThermometerPlot)
        {
            ThermometerPlot pl = (ThermometerPlot)chart.getPlot();
            
            // getDataRange
            if (pl.getDataRange().getHighExpression().indexOf(oldString) >= 0)
            {
                pl.getDataRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getHighExpression() ) );
                add=true;
            }

            if (pl.getDataRange().getLowExpression().indexOf(oldString) >= 0)
            {
                pl.getDataRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getDataRange().getLowExpression() ) );
                add=true;
            }
            
            // getHighRange
            if (pl.getHighRange().getHighExpression().indexOf(oldString) >= 0)
            {
                pl.getHighRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getHighRange().getHighExpression() ) );
                add=true;
            }

            if (pl.getHighRange().getLowExpression().indexOf(oldString) >= 0)
            {
                pl.getHighRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getHighRange().getLowExpression() ) );
                add=true;
            }
            
            // getHighRange
            if (pl.getLowRange().getHighExpression().indexOf(oldString) >= 0)
            {
                pl.getLowRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getLowRange().getHighExpression() ) );
                add=true;
            }

            if (pl.getLowRange().getLowExpression().indexOf(oldString) >= 0)
            {
                pl.getLowRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getLowRange().getLowExpression() ) );
                add=true;
            }
            
            // getMediumRange
            if (pl.getMediumRange().getHighExpression().indexOf(oldString) >= 0)
            {
                pl.getLowRange().setHighExpression(Misc.string_replace(newString, oldString, pl.getMediumRange().getHighExpression() ) );
                add=true;
            }

            if (pl.getMediumRange().getLowExpression().indexOf(oldString) >= 0)
            {
                pl.getLowRange().setLowExpression(Misc.string_replace(newString, oldString, pl.getMediumRange().getLowExpression() ) );
                add=true;
            }
            
        }
        if (chart.getPlot() instanceof TimeSeriesPlot)
        {
            TimeSeriesPlot pl = (TimeSeriesPlot)chart.getPlot();

            if (pl.getTimeAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setTimeAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getTimeAxisLabelExpression()) );
                add=true;
            }
            
            if (pl.getValueAxisLabelExpression().indexOf(oldString) >= 0)
            {
                pl.setValueAxisLabelExpression(Misc.string_replace(newString, oldString, pl.getValueAxisLabelExpression()) );
                add=true;
            }
        }
   
        
        return add;
    }
    
}
