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

import it.businesslogic.ireport.ExpressionContext;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.SubDataset;
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
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;

import java.awt.Color;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

/**
 *
 * @author  Administrator
 */
public class ReportObjectSheetPanel extends CategorySheetPanel implements ReportListener, LanguageChangedListener, ReportFrameActivatedListener {
    
    private SheetProperty spParameterName;
    private SheetProperty spParameterDescription;
    private ExpressionSheetProperty spParameterDefaultValueExpression;
    private PropertiesSheetProperty spParameterProperties;
    private SheetProperty spParameterClassType;
    private SheetProperty spParameterIsForPrompting;
    
    private SheetProperty spFieldName;
    private SheetProperty spFieldDescription;
    private PropertiesSheetProperty spFieldProperties;
    private SheetProperty spFieldClassType;
    
    private SheetProperty spVariableName;
    private SheetProperty spVariableClassType;
    private SheetProperty spVariableCalculationType;
    private SheetProperty spVariableResetType;
    private ComboBoxSheetProperty spVariableResetGroup;
    private SheetProperty spVariableIncrementType;
    private ComboBoxSheetProperty spVariableIncrementGroup;
    private SheetProperty spVariableIncrementerClass;
    private ExpressionSheetProperty spVariableExpression;
    private ExpressionSheetProperty spVariableInitialValueExpression;
    
    
    public  static java.awt.Color sharedDifferentValueLabelColor = java.awt.Color.red.darker().darker();
    public  static java.awt.Color mandatoryPropertiesLabelColor = java.awt.Color.black;
    //public  static java.awt.Color notMandatoryPropertiesLabelColor = java.awt.Color.black;
        
    private JReportFrame jrf = null;

    
    private boolean init = false;   
    
    private Vector objectSelection = new Vector();
    
    /** Creates a new instance of ReportElementSheetPanel */
    public ReportObjectSheetPanel() {
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
     * Update all the element properties...
     * 
     */
     public void updateSelection(JReportFrame newJrf)
     {
         
        // Improving speed...
        // Do nothing if there are elements selected...
         if (newJrf != null && newJrf.getSelectedElements().size() > 0) return;

        // Fix for numbers focus losing...
        if (newJrf == null)
        {
            this.setSelection(new Vector());
        }
        else
        {
            this.setSelection( newJrf.getSelectedObjects() );
        }
        
        setInit(true);
                
        this.removeAllProperties();
        
        this.jrf = newJrf;
        
        if (jrf == null || getSelection().size() == 0)
        {
            this.recreateSheet();
            return;
        }
      
        try {
        Vector selectedElements = getSelection();
        
        
        boolean sameParameterDescription = true;
        boolean sameParameterDefaultValueExpression = true;
        boolean sameParameterClassType = true;
        boolean sameParameterIsForPrompting = true;
        
        boolean sameFieldDescription = true;
        boolean sameFieldClassType = true;
        
        
        boolean sameVariableResetType = true;
        boolean sameVariableResetGroup = true;
        boolean sameVariableCalculationType = true;
        boolean sameVariableClassType = true;
        boolean sameVariableExpression = true;
        boolean sameVariableInitialValueExpression = true;
        boolean sameVariableIncrementGroup = true;
        boolean sameVariableIncrementType = true;
        boolean sameVariableIncrementerClass = true;
        
        boolean areAllparameters = true;
        boolean areAllfields = true;
        boolean areAllvariables = true;
        boolean isTheFirstElement = true;
        
        boolean areBuiltInParameters = false; // True if one ore more parameter is builtin...
        boolean areBuiltInVariables = false; // True if one ore more parameter is builtin...
        
        SubDataset subdataset = null;
        
        for (int i=0; i<selectedElements.size(); ++i)
        {
            Object obj = selectedElements.elementAt(i);
            
            if (!(obj instanceof JRParameter))
            {
                areAllparameters = false;
            }
            
            if (!(obj instanceof JRField))
            {
                areAllfields = false;
            }
            
            if (!(obj instanceof JRVariable))
            {
                areAllvariables = false;
            }
            
            if (isTheFirstElement)
            {
                subdataset = Misc.getObjectSubDataset(jrf.getReport(), obj);
                updateAllComboBoxes(subdataset);
            }
            else if (subdataset != null)
            {
                SubDataset s2 = Misc.getObjectSubDataset(subdataset, obj);
                if (s2 != subdataset)
                {
                    subdataset = null;
                }
            }
            
            if (areAllparameters)
            {
                JRParameter param = (JRParameter)selectedElements.elementAt(i);
             
                if (!areBuiltInParameters)
                {
                    areBuiltInParameters = param.isBuiltin();
                }
                
                if (selectedElements.size() == 1)
                {
                    // Single parameter selectes...
                    setTextArea(isTheFirstElement, param.getName(), spParameterName);
                    spParameterProperties.setValue( param.getProperties() );
                }
                 if(sameParameterDescription) sameParameterDescription = setTextArea(isTheFirstElement, param.getDescription(), spParameterDescription);
                 if(sameParameterDefaultValueExpression) sameParameterDefaultValueExpression = setTextArea(isTheFirstElement, param.getDefaultValueExpression(), spParameterDefaultValueExpression);
                 if(sameParameterClassType) sameParameterClassType = setGenericSheetProperty(isTheFirstElement, param.getClassType(), spParameterClassType);
                 if(sameParameterIsForPrompting) sameParameterIsForPrompting = this.setCheckBox(isTheFirstElement, param.isIsForPrompting(), false, spParameterIsForPrompting );
                
            }
            
            if (areAllfields)
            {
                JRField field = (JRField)selectedElements.elementAt(i);
                
                if (selectedElements.size() == 1)
                {
                    // Single parameter selectes...
                    setTextArea(isTheFirstElement, field.getName(), spFieldName);
                    spFieldProperties.setValue( field.getProperties() );
                }
                if(sameFieldDescription) sameFieldDescription = setTextArea(isTheFirstElement, field.getDescription(), spFieldDescription);
                if(sameFieldClassType) sameFieldClassType = setGenericSheetProperty(isTheFirstElement, field.getClassType(), spFieldClassType);
           }
            
            if (areAllvariables)
            {
                JRVariable variable = (JRVariable)selectedElements.elementAt(i);
                
                if (!areBuiltInVariables)
                {
                    areBuiltInVariables = variable.isBuiltin();
                }
                
                if (selectedElements.size() == 1)
                {
                    // Single parameter selectes...
                    setTextArea(isTheFirstElement, variable.getName(), spVariableName);
                }
                
                if (subdataset != null)
                {
                        if (sameVariableResetType) sameVariableResetType = setTagComboBox(isTheFirstElement, variable.getResetType(),spVariableResetType);
                        if (sameVariableResetGroup) sameVariableResetGroup = setTagComboBox(isTheFirstElement, variable.getResetGroup(), spVariableResetGroup);
                        if (sameVariableIncrementType) sameVariableIncrementType = setTagComboBox(isTheFirstElement, variable.getIncrementType(), spVariableIncrementType);
                        if (sameVariableIncrementGroup) sameVariableIncrementGroup = setTagComboBox(isTheFirstElement, variable.getIncrementGroup(), spVariableIncrementGroup);
                }
                
                if (sameVariableCalculationType) sameVariableCalculationType = setTagComboBox(isTheFirstElement, variable.getCalculation(), spVariableCalculationType);
                if (sameVariableIncrementerClass) sameVariableIncrementerClass = setTextArea(isTheFirstElement, variable.getIncrementerFactoryClass(), spVariableIncrementerClass);
                if (sameVariableClassType) sameVariableClassType = setGenericSheetProperty(isTheFirstElement, variable.getClassType(), spVariableClassType);
                if (sameVariableExpression) sameVariableExpression = setTextArea(isTheFirstElement, variable.getExpression(), spVariableExpression);
                if (sameVariableInitialValueExpression) sameVariableInitialValueExpression = setTextArea(isTheFirstElement, variable.getInitialValueExpression(), spVariableInitialValueExpression);
            }
            
            
            isTheFirstElement = false;
        }
        
        // TO DO: change this!
        
        // get the common subdataset...
        if (subdataset != null)
        {
            ExpressionContext ec = new ExpressionContext();
            ec.setSubDataset( subdataset );
            spParameterDefaultValueExpression.setExpressionContext( ec );
            spVariableExpression.setExpressionContext( ec );
            spVariableInitialValueExpression.setExpressionContext( ec );
        }
        
        spParameterDefaultValueExpression.setLabelError(null);
        spParameterDefaultValueExpression.updateLabel();
        
        spVariableExpression.setLabelError(null);
        spVariableExpression.updateLabel();
        
        spVariableInitialValueExpression.setLabelError(null);
        spVariableInitialValueExpression.updateLabel();
        
        
        
        
        if (areAllparameters)
        {
            
            
            String commonStr = it.businesslogic.ireport.util.I18n.getString("parameterProperties","Parameter Properties");
            if (getSelection().size() == 1) this.addSheetProperty(commonStr, spParameterName);
            spParameterIsForPrompting.setDefaultValue( new Boolean(true));
            this.addSheetProperty(commonStr, spParameterClassType);
            spParameterClassType.setLabelColor( (sameParameterClassType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                
            if (!areBuiltInParameters)
            {
                spParameterName.setReadOnly(false);
                spParameterClassType.setReadOnly(false);
                spParameterIsForPrompting.setLabelColor( (sameParameterIsForPrompting) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spParameterIsForPrompting);
                spParameterDefaultValueExpression.setLabelColor( (sameParameterDefaultValueExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spParameterDefaultValueExpression);
                if (getSelection().size() == 1) this.addSheetProperty(commonStr, spParameterProperties);
            }
            else
            {
                spParameterName.setReadOnly(true);
                spParameterClassType.setReadOnly(true);
            }
            
        }
        else if (areAllfields)
        {
            String commonStr = it.businesslogic.ireport.util.I18n.getString("fieldProperties","Field Properties");
            if (getSelection().size() == 1) this.addSheetProperty(commonStr, spFieldName);
            spFieldClassType.setLabelColor( (sameFieldClassType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
            this.addSheetProperty(commonStr, spFieldClassType);
            spFieldDescription.setLabelColor( (sameFieldDescription) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
            this.addSheetProperty(commonStr, spFieldDescription);
            if (getSelection().size() == 1) this.addSheetProperty(commonStr, spFieldProperties);
            
        }
        else if (areAllvariables)
        {
            String commonStr = it.businesslogic.ireport.util.I18n.getString("variableProperties","Variable Properties");
            if (getSelection().size() == 1) this.addSheetProperty(commonStr, spVariableName);
            
            spVariableClassType.setLabelColor( (sameVariableClassType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
            this.addSheetProperty(commonStr, spVariableClassType);
            
            if (!areBuiltInVariables)
            {
                spVariableName.setReadOnly(false);
                spVariableClassType.setReadOnly(false);
                
                spVariableCalculationType.setLabelColor( (sameVariableCalculationType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spVariableCalculationType);
                
                if (subdataset != null)
                {
                    spVariableResetType.setLabelColor( (sameVariableResetType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                    this.addSheetProperty(commonStr, spVariableResetType);
                    spVariableResetGroup.setLabelColor( (sameVariableResetGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                    this.addSheetProperty(commonStr, spVariableResetGroup);
                    spVariableIncrementType.setLabelColor( (sameVariableIncrementType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                    this.addSheetProperty(commonStr, spVariableIncrementType);
                    spVariableIncrementGroup.setLabelColor( (sameVariableIncrementGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                    this.addSheetProperty(commonStr, spVariableIncrementGroup);
                    
                    
                    if (!sameVariableResetType || !spVariableResetType.getValue().equals("Group"))
                    {
                        spVariableResetGroup.setReadOnly(true);
                    }
                    else
                    {
                        spVariableResetGroup.setReadOnly(false);
                    }
                    
                    if (!sameVariableIncrementType || !spVariableIncrementType.getValue().equals("Group"))
                    {
                        spVariableIncrementGroup.setReadOnly(true);
                    }
                    else
                    {
                        spVariableIncrementGroup.setReadOnly(false);
                    }
                }
                spVariableIncrementerClass.setLabelColor( (sameVariableIncrementerClass) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spVariableIncrementerClass);
                spVariableExpression.setLabelColor( (sameVariableExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spVariableExpression);
                spVariableInitialValueExpression.setLabelColor( (sameVariableInitialValueExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
                this.addSheetProperty(commonStr, spVariableInitialValueExpression);
                
            }
            else
            {
                spVariableName.setReadOnly(true);
                spVariableClassType.setReadOnly(true);
            }
        }
        
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
         
         
         try {
             setInit(true);
             //System.out.println("Changed: " + evt.getPropertyName());
             //if (isNullItem((SheetProperty)evt.getSource())) return;

             //removeNullItem( (SheetProperty)evt.getSource() );

             Vector selectedElements = getSelection();

             Vector modified_parameters = new Vector();
             Vector original_parameters = new Vector();

             Vector modified_fields = new Vector();
             Vector original_fields = new Vector();

             Vector modified_variables = new Vector();
             Vector original_variables = new Vector();

             for (int i=0; i<selectedElements.size(); ++i)
             {

                 Object object = selectedElements.elementAt(i);

                 if (object instanceof JRParameter)
                 {
                     JRParameter param = (JRParameter)object;
                     if (param.isBuiltin()) continue;
                     JRParameter originalParam = param.cloneMe();
                     if (applyNewParameterProperty(param,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()))
                     {
                        modified_parameters.add(object);
                        original_parameters.add(originalParam);
                     }
                 }
                 else if (object instanceof JRField)
                 {
                     JRField field = (JRField)object;
                     JRField originalField = field.cloneMe();
                     if (applyNewFieldProperty(field,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()))
                     {
                        modified_fields.add(object);
                        original_fields.add(originalField);
                     }
                 }
                 else if (object instanceof JRVariable)
                 {
                     JRVariable variable = (JRVariable)object;
                     JRVariable originalVariable = variable.cloneMe();
                     if (applyNewVariableProperty(variable,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()))
                     {
                        modified_variables.add(object);
                        original_variables.add(originalVariable);
                     }
                 }


                 // don't listen to these events...

             
             
                 for (int k=0; k<modified_parameters.size(); ++k)
                 {
                        JRParameter param = (JRParameter)modified_parameters.get(k);
                        JRParameter oldParam = (JRParameter)original_parameters.get(k);
                        SubDataset sd = Misc.getObjectSubDataset(getJrf().getReport(), param);
                        sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(sd, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.PARAMETER, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                       oldParam, 
                                                                                       param));
                 }

                 for (int k=0; k<modified_fields.size(); ++k)
                 {
                        JRField field = (JRField)modified_fields.get(k);
                        JRField oldField = (JRField)original_fields.get(k);
                        SubDataset sd = Misc.getObjectSubDataset(getJrf().getReport(), field);
                        sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(sd, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.FIELD, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                       oldField, 
                                                                                       field));
                 }

                 for (int k=0; k<modified_variables.size(); ++k)
                 {
                        JRVariable variable = (JRVariable)modified_variables.get(k);
                        JRVariable oldVariable = (JRVariable)original_variables.get(k);
                        SubDataset sd = Misc.getObjectSubDataset(getJrf().getReport(), variable);
                        sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(sd, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.VARIABLE, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                       oldVariable, 
                                                                                       variable));
                 }
             
            }
             
            getJrf().getReport().incrementReportChanges();

         } finally {
            setInit(false);
             
         }
         
         
         
         
     }
     
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      * return true if the object is modified...
      */
     private boolean applyNewParameterProperty(JRParameter param, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return false;
         
         boolean objectModified = true;
         
         if (propertyName.equals("parameterName"))
         {
            SubDataset paramSubdataset = Misc.getObjectSubDataset(getJrf().getReport() , param);
            if (paramSubdataset != null && newValue != null)
            {
                for (int i=0; i< paramSubdataset.getParameters().size(); ++i)
                {
                     JRParameter f = (JRParameter)paramSubdataset.getParameters().get(i);
                     if (f.getName().equals(newValue))
                     {
                         ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(I18n.getString( "messages.jRParameterDialog.DuplicatedParameterName","A parameter with this name already exists!"));
                         ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
                         return false;
                     }
                }
                
                param.setName(""+newValue);
                ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
                ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
            }
         }
         else if (propertyName.equals("parameterIsForPrompting"))
         {
            param.setIsForPrompting( ((Boolean)newValue).booleanValue() );
         }
         else if (propertyName.equals("parameterDescription"))
         {
            if (newValue != null)
            {
                param.setDescription(""+newValue );
            }
         }
         else if (propertyName.equals("parameterProperties"))
         {
            if (newValue != null && newValue instanceof List)
            {
                param.setProperties((List)newValue );
            }
         }
         else if (propertyName.equals("parameterClassType"))
         {
            if (newValue != null)
            {
                param.setClassType(""+newValue );
            }
         }
         else if (propertyName.equals("parameterDefaultValueExpression"))
         {
            if (newValue != null)
            {
                param.setDefaultValueExpression(""+newValue );
            }
            ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
            ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
         }
         
         return objectModified;         
     }
     
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      * return true if the object is modified...
      */
     private boolean applyNewFieldProperty(JRField field, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return false;
         
         boolean objectModified = true;
         
         if (propertyName.equals("fieldName"))
         {
            SubDataset paramSubdataset = Misc.getObjectSubDataset(getJrf().getReport() , field);
            if (paramSubdataset != null && newValue != null)
            {
                for (int i=0; i< paramSubdataset.getFields().size(); ++i)
                {
                     JRField f = (JRField)paramSubdataset.getFields().get(i);
                     if (f.getName().equals(newValue))
                     {
                         ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(I18n.getString( "messages.JRFieldDialog.DuplicatedFieldName","A field with this name already exists!"));
                         ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
                         return false;
                     }
                }
                
                field.setName(""+newValue);
                ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
                ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
            }
         }
         else if (propertyName.equals("fieldDescription"))
         {
            if (newValue != null)
            {
                field.setDescription(""+newValue );
            }
         }
         else if (propertyName.equals("fieldProperties"))
         {
            if (newValue != null && newValue instanceof List)
            {
                field.setProperties((List)newValue );
            }
         }
         else if (propertyName.equals("fieldClassType"))
         {
            if (newValue != null)
            {
                field.setClassType(""+newValue );
            }
         }
         
         return objectModified;         
     }
     
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      * return true if the object is modified...
      */
     private boolean applyNewVariableProperty(JRVariable variable, String propertyName, Object oldValue, Object newValue)
     {
         if (propertyName == null) return false;
         
         boolean objectModified = true;
         
         if (propertyName.equals("variableName"))
         {
            SubDataset paramSubdataset = Misc.getObjectSubDataset(getJrf().getReport() , variable);
            if (paramSubdataset != null && newValue != null)
            {
                for (int i=0; i< paramSubdataset.getVariables().size(); ++i)
                {
                     JRVariable f = (JRVariable)paramSubdataset.getVariables().get(i);
                     if (f.getName().equals(newValue))
                     {
                         ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(I18n.getString( "messages.JRVariableDialog.DuplicatedVariableName","A variable with this name already exists!"));
                         ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
                         return false;
                     }
                }
                
                variable.setName(""+newValue);
                ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
                ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
            }
         }
         else if (propertyName.equals("variableClassType"))
         {
            if (newValue != null)
            {
                variable.setClassType(""+newValue );
            }
         }
         else if (propertyName.equals("variableCalculationType"))
         {
            if (newValue != null)
            {
                variable.setCalculation(""+newValue );
            }
         }
         else if (propertyName.equals("variableResetType"))
         {
            if (newValue != null)
            {
                variable.setResetType("" + newValue );
            }
            if (newValue != null && newValue.equals("Group"))
            {
                spVariableResetGroup.setReadOnly(false);
                variable.setResetGroup( spVariableResetGroup.getValue() +"");
            }
            else
            {
                 spVariableResetGroup.setReadOnly(true);
                 variable.setResetGroup("");
            }
            spVariableResetGroup.updateLabel();
         }
         else if (propertyName.equals("variableResetGroup"))
         {
            variable.setResetGroup( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("variableIncrementType"))
         {
            if (newValue != null)
            {
                variable.setIncrementType("" + newValue );
            }
            if (newValue != null && newValue.equals("Group"))
            {
                spVariableIncrementGroup.setReadOnly(false);
                variable.setResetGroup( spVariableIncrementGroup.getValue() +"");
            }
            else
            {
                 spVariableIncrementGroup.setReadOnly(true);
                 variable.setIncrementGroup("");
            }
            spVariableIncrementGroup.updateLabel();
         }
         else if (propertyName.equals("variableIncrementGroup"))
         {
            variable.setIncrementGroup( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("variableIncrementerClass"))
         {
            variable.setIncrementerFactoryClass( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("variableExpression"))
         {
            if (newValue != null)
            {
                variable.setExpression(""+newValue );
            }
            ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
            ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
         }
         else if (propertyName.equals("variableInitialValueExpression"))
         {
            if (newValue != null)
            {
                variable.setInitialValueExpression(""+newValue );
            }
            ((SheetProperty)this.getSheetProperty(propertyName)).setLabelError(null);
            ((SheetProperty)this.getSheetProperty(propertyName)).updateLabel();
         }
         
         return objectModified;         
     }
     
     /*
      *     This method is called when a band is removed, changed or added.
      */ 
     public void reportBandChanged(ReportBandChangedEvent evt ) {
     }
     
     protected void initSheetProperties()
     {
        spParameterName = new SheetProperty("parameterName",I18n.getString("crosstabParameterDialog.label1","Parameter name"), SheetProperty.TEXT);
        spParameterName.setShowResetButton(false);
        
        spParameterDescription = new SheetProperty("parameterDescription",I18n.getString("jRParameterDialog.label4","Parameter Description"), SheetProperty.TEXT);
        spParameterDescription.setShowResetButton(false);
        
        spParameterDefaultValueExpression = new ExpressionSheetProperty("parameterDefaultValueExpression",it.businesslogic.ireport.util.I18n.getString("jRParameterDialog.label3","Default Value Expression"));
        spParameterDefaultValueExpression.setShowResetButton(false);
        
        spParameterProperties = new PropertiesSheetProperty("parameterProperties",it.businesslogic.ireport.util.I18n.getString("parameterProperties","Parameter Properties"));
        spParameterProperties.setShowResetButton(false);
        
        spParameterClassType = new ComboBoxSheetProperty("parameterClassType",it.businesslogic.ireport.util.I18n.getString("jRParameterDialog.label2","Parameter Class Type"));
        spParameterClassType.setShowResetButton(false);
        JComboBox classTypeCombo = (JComboBox)spParameterClassType.getEditor();
        
        classTypeCombo.setEditable(true);
        
        classTypeCombo.addItem("java.lang.Boolean");
        classTypeCombo.addItem("java.lang.Byte");
        classTypeCombo.addItem("java.util.Date");
        classTypeCombo.addItem("java.sql.Timestamp");
        classTypeCombo.addItem("java.sql.Time");
        classTypeCombo.addItem("java.lang.Double");
        classTypeCombo.addItem("java.lang.Float");
        classTypeCombo.addItem("java.lang.Integer");
        classTypeCombo.addItem("java.lang.Long");
        classTypeCombo.addItem("java.lang.Short");
        classTypeCombo.addItem("java.math.BigDecimal");
        classTypeCombo.addItem("java.lang.Number");
        classTypeCombo.addItem("java.lang.String");
        classTypeCombo.addItem("java.util.Collection");
        classTypeCombo.addItem("java.util.List");
        classTypeCombo.addItem("java.lang.Object");
        classTypeCombo.addItem("java.io.InputStream");
        classTypeCombo.addItem("net.sf.jasperreports.engine.JREmptyDataSource");
        
        spParameterIsForPrompting = new SheetProperty("parameterIsForPrompting",it.businesslogic.ireport.util.I18n.getString("jRParameterDialog.checkBoxIsForPrompting","Use as a Prompt"), SheetProperty.BOOLEAN);
        spParameterIsForPrompting.setShowResetButton(false);
        
        
        spFieldName = new SheetProperty("fieldName",I18n.getString("jRFieldDialog.label1","Field name"), SheetProperty.TEXT);
        spFieldName.setShowResetButton(false);
        
        spFieldDescription = new SheetProperty("fieldDescription",I18n.getString("jRFieldDialog.label4","Field Description"), SheetProperty.TEXT);
        spFieldDescription.setShowResetButton(false);
        
        spFieldClassType = new ComboBoxSheetProperty("fieldClassType",it.businesslogic.ireport.util.I18n.getString("jRFieldDialog.label2","Field Class Type"));
        spFieldClassType.setShowResetButton(false);
        classTypeCombo = (JComboBox)spFieldClassType.getEditor();
        
        classTypeCombo.setEditable(true);
        classTypeCombo.addItem("java.lang.Boolean");
        classTypeCombo.addItem("java.lang.Byte");
        classTypeCombo.addItem("java.util.Date");
        classTypeCombo.addItem("java.sql.Timestamp");
        classTypeCombo.addItem("java.sql.Time");
        classTypeCombo.addItem("java.lang.Double");
        classTypeCombo.addItem("java.lang.Float");
        classTypeCombo.addItem("java.lang.Integer");
        classTypeCombo.addItem("java.lang.Long");
        classTypeCombo.addItem("java.lang.Short");
        classTypeCombo.addItem("java.math.BigDecimal");
        classTypeCombo.addItem("java.lang.Number");
        classTypeCombo.addItem("java.lang.String");
        classTypeCombo.addItem("java.util.Collection");
        classTypeCombo.addItem("java.util.List");
        
        
        spFieldProperties = new PropertiesSheetProperty("fieldProperties",it.businesslogic.ireport.util.I18n.getString("fieldProperties","Field Properties"));
        spFieldProperties.setShowResetButton(false);
        
        
        
        spVariableName = new SheetProperty("variableName",I18n.getString("jRVariableDialog.label1","Variable name"), SheetProperty.TEXT);
        spVariableName.setShowResetButton(false);
        
        spVariableClassType = new ComboBoxSheetProperty("variableClassType",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label5","Variable Class Type"));
        spVariableClassType.setShowResetButton(false);
        classTypeCombo = (JComboBox)spVariableClassType.getEditor();
        classTypeCombo.setEditable(true);
        classTypeCombo.addItem("java.lang.Boolean");
        classTypeCombo.addItem("java.lang.Byte");
        classTypeCombo.addItem("java.util.Date");
        classTypeCombo.addItem("java.sql.Timestamp");
        classTypeCombo.addItem("java.sql.Time");
        classTypeCombo.addItem("java.lang.Double");
        classTypeCombo.addItem("java.lang.Float");
        classTypeCombo.addItem("java.lang.Integer");
        classTypeCombo.addItem("java.lang.Long");
        classTypeCombo.addItem("java.lang.Short");
        classTypeCombo.addItem("java.math.BigDecimal");
        classTypeCombo.addItem("java.lang.Number");
        classTypeCombo.addItem("java.lang.String");
        classTypeCombo.addItem("java.util.Collection");
        classTypeCombo.addItem("java.util.List");
        classTypeCombo.addItem("java.lang.Object");
        classTypeCombo.addItem("java.io.InputStream");
        classTypeCombo.addItem("net.sf.jasperreports.engine.JREmptyDataSource");
        
        
        spVariableCalculationType = new SheetProperty("variableCalculationType",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label2","Calculation Type"), SheetProperty.COMBOBOX);
        Vector tags = new Vector();
        tags.add(new Tag("Nothing", I18n.getString("variable.calculationType.Nothing", "Nothing") ));
        tags.add(new Tag("Count", I18n.getString("variable.calculationType.Count", "Count")));
        tags.add(new Tag("DistinctCount", I18n.getString("variable.calculationType.DistinctCount", "Distinct count")));
        tags.add(new Tag("Sum", I18n.getString("variable.calculationType.Sum", "Sum")));
        tags.add(new Tag("Average", I18n.getString("variable.calculationType.Average", "Average")));
        tags.add(new Tag("Lowest", I18n.getString("variable.calculationType.Lowest", "Lowest")));
        tags.add(new Tag("Highest", I18n.getString("variable.calculationType.Highest", "Highest")));
        tags.add(new Tag("StandardDeviation", I18n.getString("variable.calculationType.StandardDeviation", "Standard deviation")));
        tags.add(new Tag("Variance", I18n.getString("variable.calculationType.Variance", "Variance") ));
        tags.add(new Tag("System", I18n.getString("variable.calculationType.System", "System") ));
        tags.add(new Tag("First", I18n.getString("variable.calculationType.First", "First") ));
        spVariableCalculationType.setTags(tags);
        spVariableCalculationType.setDefaultValue("Now");
        spVariableCalculationType.setShowResetButton(false);
        spVariableResetType = new it.businesslogic.ireport.gui.sheet.SheetProperty("variableResetType", it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label7", "Reset Type"), it.businesslogic.ireport.gui.sheet.SheetProperty.COMBOBOX);
        spVariableResetType.setShowResetButton(false);
        tags = new Vector();
        tags.add(new Tag("None",it.businesslogic.ireport.util.I18n.getString("resetType.None","None")));
        tags.add(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("resetType.Report","Report")));
        tags.add(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("resetType.Page","Page")));
        tags.add(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("resetType.Column","Column")));
        tags.add(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("resetType.Group","Group")));
        spVariableResetType.setTags(tags);
        spVariableResetType.setDefaultValue("Report");
        
        spVariableResetGroup = new ComboBoxSheetProperty("variableResetGroup",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label6","Reset Group"));
        spVariableResetGroup.setShowResetButton(false);
        
        spVariableIncrementType = new SheetProperty("variableIncrementType",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime","Eval. time"), SheetProperty.COMBOBOX);
        tags = new Vector();
        tags.add(new Tag("None",it.businesslogic.ireport.util.I18n.getString("incrementType.None","None")));
        tags.add(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("incrementType.Report","Report")));
        tags.add(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("incrementType.Page","Page")));
        tags.add(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("incrementType.Column","Column")));
        tags.add(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("incrementType.Group","Group")));
        spVariableIncrementType.setShowResetButton(false);
        spVariableIncrementType.setTags(tags);
        spVariableIncrementType.setDefaultValue("None");
        
        spVariableIncrementGroup = new ComboBoxSheetProperty("variableIncrementGroup",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label10","Increment Group"));
        spVariableIncrementGroup.setShowResetButton(false);
        
        spVariableIncrementerClass = new SheetProperty("variableIncrementerClass",I18n.getString("jRVariableDialog.label8","Custom Incrementer Factory Class"), SheetProperty.TEXT);
        spVariableIncrementerClass.setShowResetButton(false);
        
        spVariableExpression = new ExpressionSheetProperty("variableExpression",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label3","Variable Expression"));
        spVariableExpression.setShowResetButton(false);
        
        spVariableInitialValueExpression = new ExpressionSheetProperty("variableInitialValueExpression",it.businesslogic.ireport.util.I18n.getString("jRVariableDialog.label4","Initial Value Expression"));
        spVariableInitialValueExpression.setShowResetButton(false);
        
    }
     
    /**
      * Update groups
      */
     protected void updateAllComboBoxes(SubDataset subDataset)
     {
         if (jrf == null) return;
         // Use the name of the group and not the group object....
         Vector group_names = new Vector();
         
         if (subDataset != null)
         {
             Enumeration e = subDataset.getGroups().elements();
             while (e.hasMoreElements()) {
                group_names.addElement(""+e.nextElement());
             }
         }
         spVariableIncrementGroup.updateValues( group_names,false);
         spVariableResetGroup.updateValues( group_names,false);
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
    
    public Vector getSelection() {
        return objectSelection;
    }

    public void setSelection(Vector newSelection) {
        this.objectSelection.removeAllElements();
        if (newSelection == null) return;
        this.objectSelection.addAll(newSelection);
    }

    public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt) {
    }

    public void reportElementsChanged(ReportElementChangedEvent evt) {
    }

    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
        updateSelection();
    }
    
    
}
