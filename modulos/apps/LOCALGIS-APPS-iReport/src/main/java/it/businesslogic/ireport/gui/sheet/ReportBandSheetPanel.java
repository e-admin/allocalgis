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

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.ChartReportElement;
import it.businesslogic.ireport.ExpressionContext;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.ImageReportElement;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.gui.BandsDialog;
import it.businesslogic.ireport.gui.JNumberField;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedListener;
import it.businesslogic.ireport.gui.event.ReportListener;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent;
import it.businesslogic.ireport.refactoring.ReportRefactor;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;

/**
 *
 * @author  Administrator
 */
public class ReportBandSheetPanel extends CategorySheetPanel implements ReportListener, LanguageChangedListener, ReportFrameActivatedListener {
    
    // Sheet properties
    private SheetProperty spHeight;
    private SheetProperty spSplitAllowed;
    private ExpressionSheetProperty spPrintWhenExpression;
    
    // Group related property sheets
    private SheetProperty spGroupName;
    private ExpressionSheetProperty spGroupExpression;
    private SheetProperty spGroupOnNewPage;
    private SheetProperty spGroupOnNewColumn;
    private SheetProperty spGroupResetPageNumber;
    private SheetProperty spGroupPrintHeaderOnEachPage;
    private SheetProperty spGroupMinHeightToStartOnNewPage;
    
    public  static java.awt.Color sharedDifferentValueLabelColor = java.awt.Color.red.darker().darker();
    public  static java.awt.Color mandatoryPropertiesLabelColor = java.awt.Color.blue;
    public  static java.awt.Color notMandatoryPropertiesLabelColor = java.awt.Color.black;
        
    private JReportFrame jrf = null;

    
    private boolean init = false;   
    
    private Vector bandSelection = new Vector();
    
    /** Creates a new instance of ReportElementSheetPanel */
    public ReportBandSheetPanel() {
        super();
        this.setBackground( Color.BLUE);
        initSheetProperties();
        // We have to register for element changes...
        MainFrame mf = MainFrame.getMainInstance();
        mf.addReportListener( this );
        mf.addReportFrameActivatedListener( this);       
        I18n.addOnLanguageChangedListener( this );
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
    
        if (MainFrame.getMainInstance().getActiveReportFrame() == this.getJrf()) return;
        updateSelection(MainFrame.getMainInstance().getActiveReportFrame());
        
        
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
        if (newJrf == null)
        {
            this.setBandSelection(new Vector());
        }
        else
        {
            this.setBandSelection( newJrf.getSelectedBands() );
        }
        
        setInit(true);
                
        this.removeAllProperties();
        
        this.jrf = newJrf;
        
        if (jrf == null || getBandSelection().size() == 0)
        {
            this.recreateSheet();
            return;
        }
      
        try {
        Vector selectedElements = getBandSelection();
        
        boolean sameHeight = true;
        boolean sameSplitAllowed = true;
        boolean samePrintWhenExpression = true;
        
        boolean isTheFirstElement = true;
        
        boolean sameIsGroup = true;
        boolean sameGroupName = true;
        boolean sameGroupExpression = true;
        boolean sameGroupOnNewPage = true;
        boolean sameGroupOnNewColumn = true;
        boolean sameGroupResetPageNumber = true;
        boolean sameGroupPrintHeaderOnEachPage = true;
        boolean sameGroupMinHeightToStartOnNewPage = true;
        
        for (int i=0; i<selectedElements.size(); ++i)
        {
            Band band = (Band)selectedElements.elementAt(i);
           
            //if (sameName)  sameName = this.setGenericSheetProperty(isTheFirstElement, band.getName(), false);
            if (sameHeight)  sameHeight = this.setElementNumber(isTheFirstElement, band.getHeight(), (JNumberField)spHeight.getEditor());
            if (sameSplitAllowed)  sameSplitAllowed = this.setCheckBox(isTheFirstElement, band.isSplitAllowed(), false, spSplitAllowed );
            if (samePrintWhenExpression) samePrintWhenExpression = setTextArea(isTheFirstElement, band.getPrintWhenExpression(), spPrintWhenExpression);
            
            if (sameIsGroup)
            {
                
                sameIsGroup = sameIsGroup & band.getGroup() != null;
                        
                if (sameIsGroup)
                {
                    Group group = band.getGroup();
                    
                    if (sameGroupExpression) sameGroupExpression = setTextArea(isTheFirstElement, group.getGroupExpression(), spGroupExpression);
                    if (sameGroupName) sameGroupName = setTextArea(isTheFirstElement, group.getName(), spGroupName);
                    if (sameGroupOnNewPage)  sameGroupOnNewPage = this.setCheckBox(isTheFirstElement, group.isIsStartNewPage(), false, spGroupOnNewPage );
                    if (sameGroupOnNewColumn)  sameGroupOnNewColumn = this.setCheckBox(isTheFirstElement, group.isIsStartNewColumn(), false, spGroupOnNewColumn );
                    if (sameGroupResetPageNumber)  sameGroupResetPageNumber = this.setCheckBox(isTheFirstElement, group.isIsResetPageNumber(), false, spGroupResetPageNumber );
                    if (sameGroupPrintHeaderOnEachPage)  sameGroupPrintHeaderOnEachPage = this.setCheckBox(isTheFirstElement, group.isIsReprintHeaderOnEachPage(), false, spGroupPrintHeaderOnEachPage );
                    if (sameGroupMinHeightToStartOnNewPage)  sameGroupMinHeightToStartOnNewPage = this.setElementNumber(isTheFirstElement, group.getMinHeightToStartNewPage(), (JNumberField)spGroupMinHeightToStartOnNewPage.getEditor());
                }
            }
            
            isTheFirstElement = false;
        }
        
        spHeight.setLabelColor( (sameHeight) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        //spSplitAllowed.setLabelColor( (sameSplitAllowed) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        //spPrintWhenExpression.setLabelColor( (samePrintWhenExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        
        ExpressionContext ec = new ExpressionContext();
        ec.setSubDataset( jrf.getReport() );
        
        spPrintWhenExpression.setExpressionContext( ec );
        
        spPrintWhenExpression.setLabelError(null);
        spPrintWhenExpression.updateLabel();
        
        
        
        String commonStr = it.businesslogic.ireport.util.I18n.getString("bandProperties","Band Properties");
        this.addSheetProperty(commonStr, spHeight);
        this.addSheetProperty(commonStr, spSplitAllowed);
        this.addSheetProperty(commonStr, spPrintWhenExpression);
        
        if (sameIsGroup)
        {
            String groupStr = it.businesslogic.ireport.util.I18n.getString("groupProperties","Group Properties");
            this.addSheetProperty(groupStr, spGroupName);
            this.addSheetProperty(groupStr, spGroupExpression);
            this.addSheetProperty(groupStr, spGroupOnNewPage);
            this.addSheetProperty(groupStr, spGroupOnNewColumn);
            this.addSheetProperty(groupStr, spGroupResetPageNumber);
            this.addSheetProperty(groupStr, spGroupPrintHeaderOnEachPage);
            this.addSheetProperty(groupStr, spGroupMinHeightToStartOnNewPage);
            
            spGroupExpression.setLabelError(null);
            spGroupExpression.updateLabel();
        }
        
        spSplitAllowed.setDefaultValue( new Boolean(true));
        
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
     
     
     /**
      * This methos is called when a property changes...
      */
     public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
     {
         if (isInit()) return;
         
         //System.out.println("Changed: " + evt.getPropertyName());
         //if (isNullItem((SheetProperty)evt.getSource())) return;
         
         //removeNullItem( (SheetProperty)evt.getSource() );
         
         Vector selectedElements = getBandSelection();
         
         Vector modified_groups = new Vector();
         Vector modified_bands = new Vector();
         for (int i=0; i<selectedElements.size(); ++i)
         {
             
             Band band = (Band)selectedElements.elementAt(i);
             if (applyNewBandProperty(band,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()))
             {
                 modified_bands.add(band);
             }
         
             if (band.getGroup() != null)
             {
                 Group g = band.getGroup();
                 if (!modified_groups.contains(g))
                 {
                    modified_groups.add(g);
                    if (applyNewGroupProperty(band, g,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()) )
                    {
                        if (!modified_bands.contains(g.getGroupFooter()))
                            modified_bands.add(g.getGroupFooter());
                        if (!modified_bands.contains(g.getGroupHeader()))
                            modified_bands.add(g.getGroupHeader());
                  }
                 }
             }
             
         }
         
         for (int i=0; i<modified_bands.size(); ++i)
         {
             Band band = (Band)modified_bands.elementAt(i);
             getJrf().fireReportListenerReportBandChanged(new ReportBandChangedEvent(getJrf(),band, ReportBandChangedEvent.CHANGED, this));
         }
         
         repaintEditor();
         getJrf().getReport().incrementReportChanges();
         
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
     private boolean applyNewBandProperty(Band b, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return false;
         if (isInit()) return false;
         
         boolean fireEvent = true;
         if (propertyName.equals("height"))
         {
            if (newValue != null && !newValue.equals(""))
            {   
                int val = Integer.parseInt(""+newValue);

                BandsDialog.updateBandHeight(getJrf().getReport(), b, val);
                int newHeight = b.getHeight();
                if (newHeight != val)
                {
                    boolean oldInit = isInit();
                    setInit(true);
                    spHeight.setValue("" + newHeight);
                    setInit(oldInit);
                }
            }
         }
         else if (propertyName.equals("splitAllowed"))
         {
            b.setSplitAllowed( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("printWhenExpression"))
         {
            if (newValue != null)
            {
                b.setPrintWhenExpression( ""+newValue );
            }
            ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
            ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
            //MainFrame.getMainInstance().getBandsDialog().updateBands();
            //fireEvent = false;
         }
         
         // Groups...
         
         
         return fireEvent;         
     }
     
     
     
     public void changeGroupName(Report report, SubDataset subDataset, Group group, String oldName)
     {
            if (group.getName().equals(oldName)) return;
            
            group.getGroupHeader().setName(group.getName()+"Header");
            group.getGroupFooter().setName(group.getName()+"Footer");
            
            for (int k=subDataset.getVariables().size()-1; k>=0; --k)
            {
                JRVariable var = (JRVariable)subDataset.getVariables().elementAt(k);
                if (var.isBuiltin() && var.getName().equalsIgnoreCase(oldName+"_COUNT"))
                {
                    JRVariable oldVar = var.cloneMe();
                    var.setName(group.getName()+"_COUNT");
                    // Notify the variable change...
                    subDataset.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(
                    new SubDatasetObjectChangedEvent(
                        subDataset, 
                        SubDatasetObjectChangedEvent.VARIABLE,
                        SubDatasetObjectChangedEvent.MODIFIED,
                        oldVar,var));
                    break;
                }
            }
            
            // Update All variables...
            Enumeration e = subDataset.getVariables().elements();
            while (e.hasMoreElements()) {
                JRVariable var = (JRVariable)e.nextElement();
                if (var.getResetGroup() != null &&
                var.getResetGroup().equals(oldName)) {
                    var.setResetGroup( group.getName() );
                
                    // Does not make sense notify this change...
                }
            }
            
            // Adjust elements
            if (subDataset instanceof Report)
            {
                e = ((Report)subDataset).getElements().elements();
                while (e.hasMoreElements()) {
                    ReportElement re = (ReportElement)e.nextElement();
                    if (re.printWhenGroupChanges.equals(oldName) )
                        re.printWhenGroupChanges = group.getName();

                    if (re instanceof TextFieldReportElement &&
                    ((TextFieldReportElement)re).getGroup().equals(oldName))
                        ((TextFieldReportElement)re).setGroup(group.getName());
                    
                    if (re instanceof ImageReportElement &&
                    ((ImageReportElement)re).getEvaluationGroup().equals(oldName))
                        ((ImageReportElement)re).setEvaluationGroup(group.getName());
                    
                    if (re instanceof ChartReportElement &&
                    ((ChartReportElement)re).getEvaluationGroup().equals(oldName))
                        ((ChartReportElement)re).setEvaluationGroup(group.getName());
                }
            }
            
            ReportRefactor.replaceInReportExpressions("$V{" + oldName+"_COUNT}", "$V{" + group.getName()+"_COUNT}", subDataset, report);
                    
     }
     
     
     /**
      *  Check if the new group name is ok for the current report.
      *  
      * @param report The group's report.
      * @param group The group to change, or null if the group does not yet exists
      * @param newName The candidate new name of the group. Must be Not null.
      * @return returns true if the new name is valid
      */
     public static  boolean isValidGroupName(SubDataset report, Group group, String newName)
     {
         if (newName.equals("page")) return false;
         else if (newName.equals("column")) return false;
         else if (newName.equals("lastPage")) return false;
         
         for (int i=0; i<report.getGroups().size(); ++i)
         {
             Group tmpGroup = (Group)report.getGroups().get(i);
             if (tmpGroup != group && tmpGroup.getName().equals(newName))
             {
                 return false;
             }
         }
         
         return true;
     }
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      * 
      * If a change was made, the report is set as modified...
      * 
      * @return The method return true if a band changed event is required
      */
     private boolean applyNewGroupProperty(Band band, Group group, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return false;
         if (isInit()) return false;
         
         boolean needBandChangedEvent = false;
         
         if (propertyName.equals("groupName"))
         {
            String oldName = group.getName();
            
            if (newValue == null || !isValidGroupName(getJrf().getReport(), group, ""+newValue))
            {
                ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(I18n.getString("messages.invalidGroupName","Invalid group name!"));
                ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
                
            }
            else
            {
                ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
                ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
                
                String newName = ("" + newValue).trim();
                group.setName(newName);
                changeGroupName(getJrf().getReport(), getJrf().getReport(), group, oldName);
                needBandChangedEvent = true;
                // update expression fields...
                boolean oldInit = isInit();
                
                setInit(true);
                spPrintWhenExpression.setValue("" + band.getPrintWhenExpression() );
                spGroupExpression.setValue("" + group.getGroupExpression() );
                setInit(oldInit);
            }
         }
         else if (propertyName.equals("groupExpression"))
         {
            if (newValue != null)
            {
                group.setGroupExpression(""+newValue);
            }
            ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
            ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
         }
         else if (propertyName.equals("groupStartOnNewPage"))
         {
            group.setIsStartNewPage( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("groupStartOnNewColumn"))
         {
            group.setIsStartNewColumn( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("groupResetPageNumber"))
         {
            group.setIsResetPageNumber( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("groupPrintHeaderOnEachPage"))
         {
            group.setIsReprintHeaderOnEachPage( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("groupMinHeightToStartOnNewPage"))
         {
            if (newValue != null && !newValue.equals(""))
            {   
                int val = 0;
                
                try {
                    val = Integer.parseInt(""+newValue);
                    int newHeight = val;
                    if (val < 0) newHeight = 0;
                    int maxH = getJrf().getReport().getHeight() - getJrf().getReport().getTopMargin() + getJrf().getReport().getBottomMargin();
                    maxH -= getJrf().getReport().getBandByName("pageHeader").getHeight();
                    maxH -= getJrf().getReport().getBandByName("pageFooter").getHeight();
                    if (val > maxH) newHeight = maxH;

                    //group.setMinHeightToStartNewPage(newHeight);
                    if (newHeight != val)
                    {
                        boolean oldInit = isInit();
                        setInit(true);
                        spGroupMinHeightToStartOnNewPage.setValue("" + newHeight);
                        setInit(oldInit);
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
         }
         return needBandChangedEvent;
     }
      
     /*
      *     This method is called when a band is removed, changed or added.
      */ 
     public void reportBandChanged(ReportBandChangedEvent evt ) {
         if (evt.getSource() != null && evt.getSource() == this) return;
         updateSelection();
     }
     
     protected void initSheetProperties()
     {
        spHeight =  new SheetProperty("height",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.height","Height"), SheetProperty.INTEGER);
        spHeight.setShowResetButton(false);
        
        spSplitAllowed =  new SheetProperty("splitAllowed",it.businesslogic.ireport.util.I18n.getString("gui.bandpropertiessheet.splitAllowed","Is split allowed"), SheetProperty.BOOLEAN);
        spSplitAllowed.setShowResetButton(false);
        spPrintWhenExpression = new ExpressionSheetProperty("printWhenExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printWhenExpression","Print When Expression"));
        spPrintWhenExpression.setShowResetButton(false);

        // GROUP SECTION:
        
        spGroupExpression = new ExpressionSheetProperty("groupExpression",I18n.getString("jRGroupDialog.label3","Group expression"));
        spGroupExpression.setShowResetButton(false);
        
        spGroupName = new SheetProperty("groupName",I18n.getString("jRGroupDialog.label1","Group name"), SheetProperty.TEXT);
        spGroupName.setShowResetButton(false);
          
        spGroupOnNewPage =  new SheetProperty("groupStartOnNewPage",it.businesslogic.ireport.util.I18n.getString("jRGroupDialog.checkBoxStartNewPage","Start on a new page"), SheetProperty.BOOLEAN);
        spGroupOnNewPage.setShowResetButton(false);
        
        spGroupOnNewColumn =  new SheetProperty("groupStartOnNewColumn",it.businesslogic.ireport.util.I18n.getString("jRGroupDialog.checkBoxStartNewColumn","Start on a new column"), SheetProperty.BOOLEAN);
        spGroupOnNewColumn.setShowResetButton(false);
        
        spGroupResetPageNumber =  new SheetProperty("groupResetPageNumber",it.businesslogic.ireport.util.I18n.getString("jRGroupDialog.checkBoxResetPageNumber","Reset page number"), SheetProperty.BOOLEAN);
        spGroupResetPageNumber.setShowResetButton(false);
        
        spGroupPrintHeaderOnEachPage =  new SheetProperty("groupPrintHeaderOnEachPage",it.businesslogic.ireport.util.I18n.getString("jRGroupDialog.checkBoxPrintHeaderEachPage","Print header on each page"), SheetProperty.BOOLEAN);
        spGroupPrintHeaderOnEachPage.setShowResetButton(false);
                
        spGroupMinHeightToStartOnNewPage =  new SheetProperty("groupMinHeightToStartOnNewPage",it.businesslogic.ireport.util.I18n.getString("jRGroupDialog.label5","Min height to start new page"), SheetProperty.INTEGER);
        spGroupMinHeightToStartOnNewPage.setShowResetButton(false);
       
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
        jrf.getJPanelReport().repaint( );
    }
    
    public Vector getBandSelection() {
        return bandSelection;
    }

    public void setBandSelection(Vector newBandSelection) {
        this.bandSelection.removeAllElements();
        if (newBandSelection == null) return;
        this.bandSelection.addAll(newBandSelection);
    }

    public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt) {
    }

    public void reportElementsChanged(ReportElementChangedEvent evt) {
    }

    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
        updateSelection();
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
    }
}
