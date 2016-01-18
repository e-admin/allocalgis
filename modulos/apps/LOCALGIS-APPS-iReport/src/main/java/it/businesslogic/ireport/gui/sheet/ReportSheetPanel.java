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
 * ReportElementSheetPanel.java
 * 
 * Created on 16 febbraio 2005, 6.57
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.gui.JNumberField;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.FontsListChangedEvent;
import it.businesslogic.ireport.gui.event.FontsListChangedListener;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedListener;
import it.businesslogic.ireport.gui.event.ReportListener;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.util.Enumeration;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 *
 * @author  Administrator
 */
public class ReportSheetPanel extends CategorySheetPanel implements ReportListener, LanguageChangedListener, ReportFrameActivatedListener {
    
    
    // Sheet properties
    private SheetProperty spName;
    
    private SheetProperty spHeight;
    private SheetProperty spWidth;
    private SheetProperty spOrientation;
    
    private SheetProperty spMarginTop;
    private SheetProperty spMarginLeft;
    private SheetProperty spMarginRight;
    private SheetProperty spMarginBottom;
    
    private SheetProperty spColumns;
    private SheetProperty spColumnWidth;
    private SheetProperty spColumnSpacing;
    private SheetProperty spScriptletMode;
    private SheetProperty spScriptletClass;
    private SheetProperty spLanguage;
    
    private SheetProperty spTitleOnNewPage;
    private SheetProperty spSummaryOnNewPage;
    private SheetProperty spFloatingColumnFooter;
    private SheetProperty spIgnorePagination;
    
    private SheetProperty spPrintOrder;
    private SheetProperty spWhenNoData;
    
    private SheetProperty spFormatFactoryClass;
    private SheetProperty spResourceBundleBaseName;
    private SheetProperty spWhenResourceMissingType;
    
    public  static java.awt.Color mandatoryPropertiesLabelColor = java.awt.Color.blue;
    public  static java.awt.Color notMandatoryPropertiesLabelColor = java.awt.Color.black;
    
        
    private JReportFrame jrf = null;
    private boolean init = false;   
    
    /** Creates a new instance of ReportElementSheetPanel */
    public ReportSheetPanel() {
        super();
        
        initSheetProperties();
        // We have to register for element changes...
        MainFrame mf = MainFrame.getMainInstance();
        mf.addReportListener( this );
        mf.addReportFrameActivatedListener( this);
        
        I18n.addOnLanguageChangedListener( this );
        
        MainFrame.getMainInstance().addFontsListChangedListener( new FontsListChangedListener() {
            public void fontsListChanged(FontsListChangedEvent evt) {
                
                boolean localinit = isInit();
                setInit(true);
                setInit(localinit);
            }
        } );
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
        updateSelection(evt.getReportFrame());
    }
    
    public void languageChanged(LanguageChangedEvent evt) {
        this.removeAllProperties();
        initSheetProperties();
        updateSelection();
    }

    
     public void updateSelection()
     {
         
         JReportFrame newJrf = MainFrame.getMainInstance().getActiveReportFrame();
         updateSelection(newJrf);
     }
     
     /**
      * Numbers do not change when the focus is lost due to a selection change.
      * This apply the value....
      */
     public void applyValueForNumbers()
     {
         Enumeration e = getProperties().elements();
         while (e.hasMoreElements())
         {
             SheetProperty sp = (SheetProperty)e.nextElement();

             //if (sp instanceof NumberComboBoxSheetProperty)
             //{
             //    JNumberComboBox c = (JNumberComboBox)sp.getEditor();
             //    System.out.println("NNNN");
             //    if (c.hasFocus())
             //    {
             //       FocusManager.getCurrentManager().clearGlobalFocusOwner();
             //       System.out.println(FocusManager.getCurrentManager().getFocusOwner());
             //       return; // Only a component can be focused at time...
             //    }
             //}
             if (sp.getType() == sp.INTEGER || sp.getType() == sp.NUMBER)
             {
                 JComponent c = sp.getEditor();
                 if (c.hasFocus() && c instanceof JNumberField)
                 {
                    ((JNumberField)c).focusLost(null);
                    return; // Only a component can be focused at time...
                 }
             }
             
         }
     }
     
    /**
     * Update all the element properties...
     * 
     */
     public void updateSelection(JReportFrame newJrf)
     {
         // Improving speed...
         // Do nothing if there are elements selected...
         if (newJrf != null && newJrf.getSelectedElements().size() > 0) return;
         
        // Fix for numbers focus losing...
        applyValueForNumbers();
                
        setInit(true);
        
        this.removeAllProperties();
        
        this.jrf = newJrf;
        
        if (jrf == null)
        {
            this.recreateSheet();
            return;
        }
      
        try {
        
            Report report = jrf.getReport();
            
            
            setGenericSheetProperty(true, report.getName(),spName);
            
            setTagComboBox(true,report.getOrientation(), spOrientation);
            setElementNumber(true, (double)report.getWidth(), (JNumberField)spWidth.getEditor());
            setElementNumber(true, (double)report.getHeight(), (JNumberField)spHeight.getEditor());
            
            setElementNumber(true, (double)report.getLeftMargin(), (JNumberField)spMarginLeft.getEditor());
            setElementNumber(true, (double)report.getRightMargin(), (JNumberField)spMarginRight.getEditor());
            setElementNumber(true, (double)report.getTopMargin(), (JNumberField)spMarginTop.getEditor());
            setElementNumber(true, (double)report.getBottomMargin(), (JNumberField)spMarginBottom.getEditor());
            
            setElementNumber(true, (double)report.getColumnCount(), (JNumberField)spColumns.getEditor());
            setElementNumber(true, (double)report.getColumnWidth(), (JNumberField)spColumnWidth.getEditor());
            setElementNumber(true, (double)report.getColumnSpacing(), (JNumberField)spColumnSpacing.getEditor());
            
            setTagComboBox(true,report.getScriptletHandling()+"", spScriptletMode);
            setGenericSheetProperty(true, report.getScriptletClass(),spScriptletClass);
            
            if (report.getScriptletHandling() != Report.SCRIPTLET_CLASSIC_HANDLING)
            {
                spScriptletClass.setReadOnly(true);
                spScriptletClass.setValue("");
            }
            else
            {
                spScriptletClass.setReadOnly(false);
            }
            spScriptletClass.updateLabel();
            
            setTagComboBox(true,report.getLanguage(), spLanguage);
            
            setCheckBox(true,report.isIsTitleNewPage(), false, spTitleOnNewPage);
            setCheckBox(true,report.isIsSummaryNewPage(), false, spSummaryOnNewPage);
            setCheckBox(true,report.isFloatColumnFooter(), false, spFloatingColumnFooter);
            setCheckBox(true,report.isIgnorePagination(), false, spIgnorePagination);
            
            setTagComboBox(true,report.getPrintOrder(), spPrintOrder);
            setTagComboBox(true,report.getWhenNoDataType(), spWhenNoData);
            
            setTagComboBox(true, report.getFormatFactoryClass(),spFormatFactoryClass);
            setTagComboBox(true, report.getResourceBundleBaseName(),spResourceBundleBaseName);
            setTagComboBox(true, report.getWhenResourceMissingType(),spWhenResourceMissingType);
            
    
            
            spName.setLabelError( validateProperty("name", spName.getValue()));
            //spOrientation.setLabelColor( mandatoryPropertiesLabelColor );
            //spWidth.setLabelColor( mandatoryPropertiesLabelColor );
            //spHeight.setLabelColor( mandatoryPropertiesLabelColor );
        
            String commonStr = it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.title","Report properties");
            this.addSheetProperty(commonStr, spName);
            
            
            
            String pageStr = it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.PageSize","Page size");
            this.addSheetProperty(pageStr, spOrientation);
            this.addSheetProperty(pageStr, spWidth);
            this.addSheetProperty(pageStr, spHeight);
            
            String marginsStr = it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.PageMargin","Page margin");
            this.addSheetProperty(marginsStr, spMarginLeft);
            this.addSheetProperty(marginsStr, spMarginRight);
            this.addSheetProperty(marginsStr, spMarginTop);
            this.addSheetProperty(marginsStr, spMarginBottom);
            
            String columnsStr = it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.ReportColumns","Report columns");
            this.addSheetProperty(columnsStr, spColumns);
            this.addSheetProperty(columnsStr, spColumnWidth);
            this.addSheetProperty(columnsStr, spColumnSpacing);
            
            
            commonStr = it.businesslogic.ireport.util.I18n.getString("more", "More...");
            this.addSheetProperty(commonStr, spScriptletMode);
            this.addSheetProperty(commonStr, spScriptletClass);
            this.addSheetProperty(commonStr, spLanguage);    
            this.addSheetProperty(commonStr, spTitleOnNewPage);
            this.addSheetProperty(commonStr, spSummaryOnNewPage);
            this.addSheetProperty(commonStr, spFloatingColumnFooter);
            this.addSheetProperty(commonStr, spIgnorePagination);
            this.addSheetProperty(commonStr, spPrintOrder);
            this.addSheetProperty(commonStr, spWhenNoData);
            this.addSheetProperty(commonStr, spFormatFactoryClass);
            this.addSheetProperty(commonStr, spResourceBundleBaseName);
            this.addSheetProperty(commonStr, spWhenResourceMissingType);
            
       
        this.recreateSheet();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
        
        setInit(false);
     }
     
     
     /*
      * This method remove the first entry in comboboxes if the first entry
      * is something like "<Different values>"....
      *
      */
     public void removeNullItem(SheetProperty comboProperty)
     {
         if (comboProperty == null) return;
          if (comboProperty.getEditor() instanceof JComboBox)
          {
             JComboBox combobox = (JComboBox)comboProperty.getEditor();
             if (combobox.getSelectedIndex() > 0)
             {
                Object obj = combobox.getItemAt(0);
                if ((obj+"").equals(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.differentValues","<different values>")))
                {
                    combobox.removeItemAt(0);
                }
             }
          }
     }
     
     /*
      * This method says if the selected value is actually something like "<Different values>"....
      *
      */
     public boolean isNullItem(SheetProperty comboProperty)
     {
         if (comboProperty == null) return false;
          if (comboProperty.getEditor() instanceof JComboBox)
          {
             JComboBox combobox = (JComboBox)comboProperty.getEditor();
             if (combobox.getSelectedIndex() == 0)
             {
                Object obj = combobox.getItemAt(0);
                if ((obj+"").equals(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.differentValues","<different values>")))
                {
                    return true;
                }
             }
          }
         return false;
     }
     
     
     /**
      * This methos is called when a property changes...
      */
     public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
     {
         if (isInit()) return;
         
         applyNewProperty(getJrf().getReport(),evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
          
         
         repaintEditor();
         
         /*
         ReportElementChangedEvent changedEvent = new ReportElementChangedEvent(jrf , selectedElements , ReportElementChangedEvent.CHANGED);
         changedEvent.setEventSource( this );
         if (evt.getPropertyName().equals("textfieldEvaluationTime"))
         {
            changedEvent.setPropertyChanged( evt.getPropertyName() );
            changedEvent.setNewValue( evt.getNewValue() );
         }
         jrf.fireReportListenerReportElementsChanged(changedEvent);
         MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
         */
     }
     
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      */
     private void applyNewProperty(Report report, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return;
         if (isInit()) return;
         
         boolean fireEvent = true;
         if (propertyName.equals("name"))
         {
            String s = (String)newValue;
            spName.setLabelError( validateProperty(propertyName, newValue));
            spName.updateLabel();
            
            report.setName( ""+newValue);
            MainFrame.getMainInstance().setTitle(MainFrame.getRebrandedTitle() + " ["+ report.getName() + " "+ report.getWidth()+"x"+report.getHeight()+" "+report.getFilename()+" "+(report.isReadOnly()?"(READ ONLY) ":"")+"]");
         }
         else if (propertyName.equals("orientation"))
         {
             adjustSizes(""+newValue, report);
             report.setOrientation(""+newValue);
             recalcColumnWidth(report);
             updateSizeMeasures(report);
             
             //this.updateSelection(getJrf());
             getJrf().updateScrollBars();
             getJrf().getJPanelReport().repaint();
         }
         else if (propertyName.equals("width"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setWidth( val );
                recalcColumnWidth(report);
                adjustOrientation(report);
                updateSizeMeasures(report);
            } else fireEvent = false;
         }
         else if (propertyName.equals("height"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setHeight( val );
                adjustOrientation(report);
                updateSizeMeasures(report);
            } else fireEvent = false;
         }
         else if (propertyName.equals("marginTop"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                int delta = val - report.getTopMargin();
                report.setTopMargin( val );
                adjustElementPositions(report, delta, 0);
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("marginLeft"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                int delta = val - report.getLeftMargin();
                report.setLeftMargin( val );
                adjustElementPositions(report, 0, delta);
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("marginRight"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                int delta = val - report.getRightMargin();
                report.setRightMargin( val );
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("marginBottom"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                int delta = val - report.getBottomMargin();
                report.setBottomMargin( val );
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("columns"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setColumnCount( val );
                recalcColumnWidth(report);
                updateSizeMeasures(report);
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("columnWidth"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setColumnWidth( val );
                recalcColumnWidth(report);
                updateSizeMeasures(report);
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("columnSpacing"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setColumnSpacing( val );
                recalcColumnWidth(report);
                updateSizeMeasures(report);
                getJrf().updateScrollBars();
                getJrf().getJPanelReport().repaint();
            } else fireEvent = false;
         }
         else if (propertyName.equals("scriptletMode"))
         {
            if (newValue != null && !newValue.equals("") )
            {   
                int val = Integer.parseInt(""+newValue);
                report.setScriptletHandling( val );
                
                if (val != Report.SCRIPTLET_CLASSIC_HANDLING)
                {
                    spScriptletClass.setReadOnly(true);
                    spScriptletClass.setValue("");
                    report.setScriptletClass("");
                }
                else
                {
                    spScriptletClass.setReadOnly(false);
                }
                spScriptletClass.updateLabel();
                
            } else fireEvent = false;
         }
         else if (propertyName.equals("scriptletClass"))
         {
             if (newValue != null)
             {
                report.setScriptletClass( "" + newValue );
            }
         }
         else if (propertyName.equals("language"))
         {
             if (newValue != null)
             {
                report.setLanguage( ""+newValue);
             }
         }
         else if (propertyName.equals("titleOnNewPage"))
         {
                try {
                     report.setIsTitleNewPage( Boolean.valueOf( newValue+"").booleanValue() );
                } catch (Exception ex) {}
         }
         else if (propertyName.equals("titleOnNewPage"))
         {
                try {
                     report.setIsTitleNewPage( Boolean.valueOf( newValue+"").booleanValue() );
                } catch (Exception ex) {}
         }
         else if (propertyName.equals("summaryOnNewPage"))
         {
                try {
                     report.setIsSummaryNewPage( Boolean.valueOf( newValue+"").booleanValue() );
                } catch (Exception ex) {}
         }
         else if (propertyName.equals("floatingColumnFooter"))
         {
                try {
                     report.setFloatColumnFooter( Boolean.valueOf( newValue+"").booleanValue() );
                } catch (Exception ex) {}
         }
         else if (propertyName.equals("ignorePagination"))
         {
                try {
                     report.setFloatColumnFooter( Boolean.valueOf( newValue+"").booleanValue() );
                } catch (Exception ex) {}
         }
         
         else if (propertyName.equals("printOrder"))
         {
                if (newValue != null)
                 {
                    report.setPrintOrder(""+newValue);
                 }
         }
         else if (propertyName.equals("whenNoData"))
         {
                if (newValue != null)
                 {
                    report.setWhenNoDataType(""+newValue);
                 }
         }
         else if (propertyName.equals("formatClass"))
         {
                if (newValue != null)
                 {
                    report.setFormatFactoryClass(""+newValue);
                 }
         }
         else if (propertyName.equals("resourceBundleBaseName"))
         {
                if (newValue != null)
                 {
                    report.setResourceBundleBaseName(""+newValue);
                 }
         }
         else if (propertyName.equals("whenResourceMissingType"))
         {
                if (newValue != null)
                 {
                    report.setWhenResourceMissingType(""+newValue);
                 }
         }
         
         
                 
         if (fireEvent)
         {
             report.incrementReportChanges();
         }
        
     }
          
     /** 
      * The method return null if the value for the property is ok, or a localized not null String with
      * the error description otherwise.
      */
     public String validateProperty(String propertyName, Object newValue)
     {
         
        if (propertyName.equals("name"))
         {
            String s = (String)newValue;
            if (s.trim().length() > 0)
            {
                return null;
            }
            else
            {
                return I18n.getString("error.reportNameNull","The report name can not be a blank string");
            }
         }
      
        return null;
     }
     
     /**
      *     This method is called when a new element is selected,
      *     or deselected.
      */
     public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt){
         
     }
   
     /*
      *     This method is called when an element is removed, changed or added.
      */
      public void reportElementsChanged(ReportElementChangedEvent evt){
        
          
      }
      
     /*
      *     This method is called when a band is removed, changed or added.
      */ 
     public void reportBandChanged(ReportBandChangedEvent evt ) {
         
     }
     
     protected void initSheetProperties()
     {
        spOrientation = new SheetProperty("orientation",it.businesslogic.ireport.util.I18n.getString("orientation","Orientation"), SheetProperty.COMBOBOX); 
        spOrientation.setTags( new Tag[]{ 
            new Tag("Portrait", it.businesslogic.ireport.util.I18n.getString("pageOrientation.Portrait","Portrait")),
            new Tag("Landscape", it.businesslogic.ireport.util.I18n.getString("pageOrientation.Landscape","Landscape")) });
        
        spOrientation.setShowResetButton(false);
                                           
        spWidth =  new SheetProperty("width",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.width","Width"), SheetProperty.INTEGER);
        spWidth.setShowResetButton(false);
        
        spHeight =  new SheetProperty("height",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.height","Height"), SheetProperty.INTEGER);
        spHeight.setShowResetButton(false);
        
        
        spName = new SheetProperty("name",it.businesslogic.ireport.util.I18n.getString("reportName","Report name"), SheetProperty.STRING); 
        spName.setShowResetButton(false);

        spMarginTop = new SheetProperty("marginTop",it.businesslogic.ireport.util.I18n.getString("top","Top"), SheetProperty.INTEGER);
        spMarginTop.setShowResetButton(false);
        spMarginLeft = new SheetProperty("marginLeft",it.businesslogic.ireport.util.I18n.getString("left","Left"), SheetProperty.INTEGER);
        spMarginLeft.setShowResetButton(false);
        spMarginRight = new SheetProperty("marginRight",it.businesslogic.ireport.util.I18n.getString("right","Right"), SheetProperty.INTEGER);
        spMarginRight.setShowResetButton(false);
        spMarginBottom = new SheetProperty("marginBottom",it.businesslogic.ireport.util.I18n.getString("bottom","Bottom"), SheetProperty.INTEGER);
        spMarginBottom.setShowResetButton(false);

        spColumns = new SheetProperty("columns",it.businesslogic.ireport.util.I18n.getString("columns","Columns"), SheetProperty.INTEGER);
        spColumns.setShowResetButton(false);
        spColumnWidth = new SheetProperty("columnWidth",it.businesslogic.ireport.util.I18n.getString("width","Width"), SheetProperty.INTEGER);
        spColumnWidth.setShowResetButton(false);
        spColumnSpacing = new SheetProperty("columnSpacing",it.businesslogic.ireport.util.I18n.getString("spacing","Spacing"), SheetProperty.INTEGER);
        spColumnSpacing.setShowResetButton(false);
        
        spScriptletMode = new SheetProperty("scriptletMode",it.businesslogic.ireport.util.I18n.getString("scriptletMode","Scriptlet"), SheetProperty.COMBOBOX); 
        spScriptletMode.setTags( new Tag[]{ 
                    new Tag("0", it.businesslogic.ireport.util.I18n.getString("noScriptlet", "Don't use scriptlet")),
                    new Tag("1", it.businesslogic.ireport.util.I18n.getString("iReportScriptlet", "Use iReport internal scriptlet support")),
                    new Tag("2", it.businesslogic.ireport.util.I18n.getString("customScriptlet", "Use this scriptlet class..."))} );
        spScriptletMode.setShowResetButton(false);
        
        spScriptletClass = new SheetProperty("scriptletClass",it.businesslogic.ireport.util.I18n.getString("scriptlet","Scriptlet class"), SheetProperty.STRING); 
        spScriptletClass.setShowResetButton(false);
        
        spLanguage = new SheetProperty("language",it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.label18","Language"), SheetProperty.COMBOBOX); 
        spLanguage.setTags( new Tag[]{ 
                    new Tag("java", "Java"),
                    new Tag("groovy", "Groovy")} );
        spLanguage.setShowResetButton(false);
        
        spTitleOnNewPage = new SheetProperty("titleOnNewPage",it.businesslogic.ireport.util.I18n.getString("titleOnANewPage","Title on a new page"), SheetProperty.BOOLEAN);
        spTitleOnNewPage.setShowResetButton(false);
        spSummaryOnNewPage = new SheetProperty("summaryOnNewPage",it.businesslogic.ireport.util.I18n.getString("summaryInANewPage","Summary on a new page"), SheetProperty.BOOLEAN);
        spSummaryOnNewPage.setShowResetButton(false);
        spFloatingColumnFooter = new SheetProperty("floatingColumnFooter",it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.checkBoxFloatColumnFooter","Floating column footer"), SheetProperty.BOOLEAN);
        spFloatingColumnFooter.setShowResetButton(false);
        spIgnorePagination = new SheetProperty("ignorePagination",it.businesslogic.ireport.util.I18n.getString("ignorePagination", "Ignore pagination"), SheetProperty.BOOLEAN);
        spIgnorePagination.setShowResetButton(false);
        
        spPrintOrder = new SheetProperty("printOrder",it.businesslogic.ireport.util.I18n.getString("printOrder","Print order"), SheetProperty.COMBOBOX); 
        spPrintOrder.setTags( new Tag[]{ 
                    new Tag("Vertical", it.businesslogic.ireport.util.I18n.getString("printOrder.Vertical","Vertical")),
                    new Tag("Horizontal", it.businesslogic.ireport.util.I18n.getString("printOrder.Horizontal","Horizontal"))} );        
        spPrintOrder.setShowResetButton(false);
        
        spWhenNoData = new SheetProperty("whenNoData",it.businesslogic.ireport.util.I18n.getString("whenNoData","When no data"), SheetProperty.COMBOBOX); 
        spWhenNoData.setTags( new Tag[]{ 
                    new Tag("NoPages", it.businesslogic.ireport.util.I18n.getString("whenNoData.NoPages","No pages")),
                    new Tag("BlankPage", it.businesslogic.ireport.util.I18n.getString("whenNoData.BlankPage","Blank page")),
                    new Tag("AllSectionsNoDetail", it.businesslogic.ireport.util.I18n.getString("whenNoData.AllSectionsNoDetail","All sections, no detail")), 
                    new Tag("NoDataSection", it.businesslogic.ireport.util.I18n.getString("whenNoData.NoDataSection","No-data section")) } );
        
        spWhenNoData.setShowResetButton(false);  
                    
        spFormatFactoryClass = new SheetProperty("formatClass",it.businesslogic.ireport.util.I18n.getString("formatFactoryClass","Format factory class"), SheetProperty.STRING ); 
        spFormatFactoryClass.setShowResetButton(false);
        
        spResourceBundleBaseName = new SheetProperty("resourceBundleBaseName",it.businesslogic.ireport.util.I18n.getString("resourceBundleBaseName","Resource bundle base name"), SheetProperty.STRING);
        spResourceBundleBaseName.setShowResetButton(false);
        
        spWhenResourceMissingType = new SheetProperty("whenResourceMissingType",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType","When resource missing type"), SheetProperty.COMBOBOX); 
        spWhenResourceMissingType.setTags( new Tag[]{ 
                    new Tag("Null",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Null","Null")),
                    new Tag("Empty",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Empty","Empty")),
                    new Tag("Key",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Key","Key")),
                    new Tag("Error",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Error","Error"))} );
        spWhenResourceMissingType.setShowResetButton(false);
        
        
     }
     
    
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    public JReportFrame getJrf() {
        return jrf;
    }

    public void setJrf(JReportFrame jrf) {
        this.jrf = jrf;
    }
    
    
    /**
     * Redraw the correct editor panel (JReportPanel or the active CrosstabPanel)
     *
     */
    public void repaintEditor()
    {
        if (jrf == null) return;
        if (jrf.getSelectedCrosstabEditorPanel() == null)
        {
            jrf.getJPanelReport().repaint( );
        }
        else
        {
            jrf.getSelectedCrosstabEditorPanel().repaint();
        }
    }
   
    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
    }
    
    
    
    /**
     * This method sets height and width according to the orientation.
     * @return Void
     * @since July 3, 2004
     * @author Robert Lamping
     */
    public void adjustSizes(String orientation, Report report) {
        int reportSwitch = 0;
        if ( orientation.equals("Landscape") ) {
            // set Width to largest value of ReportWidth, ReportHeight
            if ( report.getWidth() < report.getHeight() ) {
                reportSwitch = report.getHeight();
                report.setHeight( report.getWidth() );
                report.setWidth( reportSwitch );
            }
        } else {
            // Orientation = Portrait
            if ( report.getWidth() > report.getHeight() ) {
                reportSwitch = report.getHeight();
                report.setHeight( report.getWidth() );
                report.setWidth( reportSwitch );
            }
        }
    }
    
    /**
     * This method recalcs the column width accordly to the available report width
     *
     */
    void recalcColumnWidth(Report report) {

        int columns = report.getColumnCount();
        // calculate space...
        int avail = report.getWidth() - report.getLeftMargin() - report.getRightMargin() - (report.getColumnCount()-1)*report.getColumnSpacing();
        int dim = avail;
        dim = (int)( (double)dim/(double)columns);

        while ( (dim*columns) > avail) {
            dim--;
        }
        report.setColumnWidth(dim);
    }
    
    void updateSizeMeasures(Report report)
    {
        boolean oldInit = isInit();
        setInit(true);
        
        setTagComboBox(true,report.getOrientation(), spOrientation);
        setElementNumber(true, (double)report.getWidth(), (JNumberField)spWidth.getEditor());
        setElementNumber(true, (double)report.getHeight(), (JNumberField)spHeight.getEditor());

        setElementNumber(true, (double)report.getLeftMargin(), (JNumberField)spMarginLeft.getEditor());
        setElementNumber(true, (double)report.getRightMargin(), (JNumberField)spMarginRight.getEditor());
        setElementNumber(true, (double)report.getTopMargin(), (JNumberField)spMarginTop.getEditor());
        setElementNumber(true, (double)report.getBottomMargin(), (JNumberField)spMarginBottom.getEditor());

        setElementNumber(true, (double)report.getColumnCount(), (JNumberField)spColumns.getEditor());
        setElementNumber(true, (double)report.getColumnWidth(), (JNumberField)spColumnWidth.getEditor());
        setElementNumber(true, (double)report.getColumnSpacing(), (JNumberField)spColumnSpacing.getEditor());
        MainFrame.getMainInstance().setTitle(MainFrame.getRebrandedTitle() + " ["+ report.getName() + " "+ report.getWidth()+"x"+report.getHeight()+" "+report.getFilename()+" "+(report.isReadOnly()?"(READ ONLY) ":"")+"]");
        setInit(oldInit);
    }
    
    public void adjustOrientation(Report report) {
        if ( report.getWidth() <= report.getHeight() ) {
            report.setOrientation("Portrait");
        } else {
            report.setOrientation("Landscape");
        }
    }
    
    public void adjustElementPositions(Report report, int differenceY, int differenceX)
    {
        if (differenceX != 0 || differenceY != 0) {
            Enumeration e = report.getElements().elements();
            while (e.hasMoreElements()) {
                ReportElement re = (ReportElement)e.nextElement();
                re.trasform(new java.awt.Point(differenceX,differenceY), TransformationType.TRANSFORMATION_MOVE);
            }
        }
    }

}
