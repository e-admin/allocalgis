/**
 * AutoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.autoforms;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;
import com.geopista.util.TreeDomainGUIListener;
import com.geopista.util.domainEnableCheck;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/**
 * Generador automático de formularios para la aplicación GEOPISTA
 * Incluye la validación de los datos y la realimentación al usuario.
 *  
 * @author Juan Pablo de Castro
 **/
public class AutoDialog extends MultiInputDialog implements FieldPanelAccessor
{
private Feature feature=null;
private int maxRows=5;
// Mapping TreeDomains Attrs
private HashMap TreeDomainToAttrsMap=new HashMap();

private JPanel currentGroupingPanel=null;
	/**
	 * 
	 */
	public AutoDialog() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param frame
	 * @param title
	 * @param modal
	 */
	public AutoDialog(Frame frame, String title, boolean modal, Feature feature) {
		super(frame, title, modal);
		setFeature(feature);
		// TODO Auto-generated constructor stub
	}
	public void setFeature(Feature feature)
	{
		this.feature=feature;
	}
	/**
	 * Construye el diálogo utilizando paneles decorados
	 * para agrupar los campos según su tabla de procedencia
	 *
	 */
	public void buidDialogByGroupingTables()
	{

//		 gets metadata
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		int count=schema.getAttributeCount();
		
	/**
	 * traverse columns grouping by table
	 */
		HashMap tables=new HashMap();
		for (int i=0;i<count;i++)
		{
			Column col=schema.getColumnByAttribute(i);
			Table table= col.getTable();
			Collection columns;
			if ((columns=(Collection) tables.get(table))==null) // get previous table registered if any
			{	// Not registered yet
				columns=new ArrayList();
				tables.put(table,columns);
			}
			columns.add(col); //Adds the column to table schema
		}
		
		
		
		
		Iterator iterTables = tables.entrySet().iterator();
		int rows=0;
		int dialogColumns=1;
		
		while (iterTables.hasNext())
		{
		Map.Entry entry = (Map.Entry)iterTables.next();
		Table table= (Table) entry.getKey();
		Collection cols= (Collection) entry.getValue();
		JPanel dialogMainPanel=currentMainPanel;// backups main panel
		currentMainPanel.setLayout(new BoxLayout(dialogMainPanel, BoxLayout.PAGE_AXIS));
		//currentMainPanel.setLayout(new VerticalFlowLayout());
		
		currentGroupingPanel= new JPanel(new GridBagLayout());
		currentGroupingPanel.setBorder(BorderFactory.createTitledBorder(table.getName()));
				
		currentMainPanel=currentGroupingPanel; // cheat MultiDialog to use my grouping panel
		
		//this.addLabel(table.getName());rows++;
		//this.addSeparator();rows++;
		Iterator iterCols=cols.iterator();
			while (iterCols.hasNext())
			{
				Column col = (Column)iterCols.next();
				addField(schema.getAttribute(col)); rows++;
				/**
				 * Checks whether we need more than a column of fields
				 */
				if (count > maxRows && rows > count/2 && dialogColumns!=2)
					{
					currentMainPanel=dialogMainPanel; // cheat MultiDialog to use my grouping panel
					
					this.startNewColumn();
					dialogColumns=2;
					dialogMainPanel=currentMainPanel; //nuevo panel
					currentMainPanel=currentGroupingPanel; //cheats
					}
			}
		dialogMainPanel.add(currentGroupingPanel);
		currentMainPanel = dialogMainPanel;
		}
		
	
	}
	public void buildDialogByTables(boolean fragmentTables)
	{
//		 gets metadata
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		int count=schema.getAttributeCount();
		
	/**
	 * traverse columns grouping by table
	 */
		HashMap tables=new HashMap();
		for (int i=0;i<count;i++)
		{
			Column col=schema.getColumnByAttribute(i);

      //parche temporal para que no de problemas la geometria que la cuenta como un
      //atributo mas
      if(col!=null)
      {
        Table table= col.getTable();
        Collection columns;
        if ((columns=(Collection) tables.get(table))==null) // get previous table registered if any
        {	// Not registered yet
          columns=new ArrayList();
          tables.put(table,columns);
        }
        columns.add(col); //Adds the column to table schema
      }
		}
		
		
		
		
		Iterator iterTables = tables.entrySet().iterator();
		int rows=0;
		int dialogColumns=1;
		
		while (iterTables.hasNext())
		{
		Map.Entry entry = (Map.Entry)iterTables.next();
		Table table= (Table) entry.getKey();
		Collection cols= (Collection) entry.getValue();
		/**
		 * Try to add a new Column before the table if next table don't fit
		 * only if fragmentTables=false
		 */
		if (fragmentTables==false && rows>0 && cols.size()+2+rows > maxRows/2 && dialogColumns!=2)
			{
			this.startNewColumn();
			dialogColumns=2;
			}
		this.addLabel(table.getName());rows++;
		this.addSeparator();rows++;
		Iterator iterCols=cols.iterator();
			while (iterCols.hasNext())
			{
				Column col = (Column)iterCols.next();
				addField(schema.getAttribute(col)); rows++;
				/**
				 * Checks whether we need more than a column of fields
				 */
				if (count > maxRows && rows > count/2 && dialogColumns!=2)
					{
					this.startNewColumn();
					dialogColumns=2;
					}
			}
		
		}
		
	}
	/**
	 * Add a new field to Dialog
	 * @param string
	 */
	private void addField(String attName) {
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		AttributeType type = schema.getAttributeType(attName);
		Domain domain= schema.getAttributeDomain(attName);
		Column column= schema.getColumnByAttribute(attName);
		
		/**
		 * Constructs form field depending on domain type
		 */
		
		
		if (domain.getType() == Domain.TREE)
		{
		
			TreeDomain td= (TreeDomain) domain;
			// Intenta localizar el dominio que se aplica a este atributo.
			Domain EffectiveDomain = td.getKeyDomainByColumn(column,feature);
			if (EffectiveDomain==null)
				EffectiveDomain=td.getKeyDomainByColumn(column); // sin ajustarse a valores
			// create component
			if (EffectiveDomain.getChildren().size()>0)
				addField(attName,td, ((Domain)EffectiveDomain.getChildren().get(0)).getType());
			else
				addField(attName,td,EffectiveDomain.getType());
			// Strategy: add listeners if has subordinates
			int level=td.getLevelByColumn(schema.getColumnByAttribute(attName));
			if (level< td.getTreeDepth()-1) // tiene subniveles
			{
				JComponent thisField = (JComponent)fieldNameToComponentMap.get(attName);
				TreeDomainGUIListener TDListener= new TreeDomainGUIListener(attName,td,feature, this);
				if (thisField instanceof JComboBox)
				{
				((JComboBox) thisField).addActionListener(TDListener);
				}
				if (thisField instanceof JTextField)
				{
				((JTextField) thisField).addActionListener(TDListener);
				}
			}

			
			// TODO: assign tree EnableCheck using fieldNameToEnableCheckListMap [Juan Pablo]
		}
		else
		addField(attName, domain, domain.getType());
	}
	
	private void addField(String attName, Domain asDomain, int domainType)
	{
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		
		AttributeType type = schema.getAttributeType(attName);
		
		Domain domain= asDomain;
		Column column= schema.getColumnByAttribute(attName);
		if (domainType == Domain.PATTERN || domainType==Domain.NUMBER)
			//
			{
			
			//if (type.equals(AttributeType.STRING))
			//{
				String valor = (String) feature.getString(attName);
				addTextField(attName,valor,domain.getAproxLenght(), null , column.getDescription());
			//}
			}
		else
		if (domainType == Domain.NUMBER)
		{
			if (type.equals(AttributeType.INTEGER))
			{
				Integer valor = (Integer) feature.getAttribute(attName);
				addIntegerField(attName, valor.intValue(), domain.getAproxLenght(), column.getDescription());
			}
			else
			{
				Double valor = (Double) feature.getAttribute(attName);
				addDoubleField(attName, valor.doubleValue(), domain.getAproxLenght());
			}
		}
		
		if (domainType == Domain.CODEBOOK ||	domainType==Domain.TREE )
		{
			
			// Constructs the ComboBox
			addCodeBookCombo(attName, (TreeDomain) domain);
		}
		if (domainType == Domain.BOOLEAN )
		{	
			// Constructs the CheckBox
			Integer valor = (Integer) feature.getAttribute(attName);
			if (valor==null) valor=new Integer(0);
			this.addCheckBox(attName,valor.intValue()==1 , column.getDescription());
		}
	}

	/**
	 *Adds a new Combo control with a list of options  
	 * @param attName
	 * @param cbDomain
	 */
	private void addCodeBookCombo(String attName, TreeDomain tdDomain)
	{
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		
		// Obtiene el dominio que se aplica como padre de este nivel 
		// y para los valores de esta feature
		Domain parentDomain = tdDomain.getKeyDomainByColumn(schema.getColumnByAttribute(attName),feature);
		Vector combo= new Vector();
		Domain selected=null;
		if(parentDomain!=null)
			{
			Iterator domainChildren = parentDomain.getChildren().iterator();
			
			/**
			 * get current value of the attribute
			 */
			String val= feature.getString(schema.getAttributeIndex(attName));
			
			while (domainChildren.hasNext())
			{
				Domain domainChild=(Domain)domainChildren.next();
				String value= domainChild.getRepresentation();
				String code = domainChild.getPattern();
				if (val.compareTo(code)== 0) selected=domainChild;
				combo.add(domainChild);
			}
			}
		this.addComboBox(attName,selected,combo,tdDomain.getDescription(schema.getColumnByAttribute(attName)));
		
			
	}
	
	public void buildDialog()
	{
		// gets metadata
		GeopistaSchema schema= (GeopistaSchema) feature.getSchema();
		int count=schema.getAttributeCount();
		int rows=0;
		int dialogColumns=1;
		
		for (int i=0;i<count;i++)
		{
		String name = schema.getAttributeName(i);
		addField(name);rows++;
		/**
		 * Checks whether we need more than a column of fields
		 */
		if (count > maxRows && rows > count/2 && dialogColumns!=2)
			{
			this.startNewColumn();
			dialogColumns=2;
			}
		}
	}
/**
 * @return Returns  maxRows.
 */
public int getMaxRows() {
	return maxRows;
}
/**
 * @param maxRows The maxRows to set.
 */
public void setMaxRows(int maxRows) {
	this.maxRows = maxRows;
}
private EnableCheck createDomainEnableCheck(final String fieldName)
{
    return new domainEnableCheck(); 
    }
/**
 * @param attName
 * @return
 */
public JComponent getComponentByFieldName(String attName) {
	return (JComponent) fieldNameToComponentMap.get(attName);
}
// Obtiene los errores del formulario
private HashMap Errors = new HashMap();

// report errors
protected void reportValidationError(String errorMessage) {
	
	// ccnstruye el mensaje de error.
	
	for (Iterator i=Errors.values().iterator();i.hasNext();)
	{
		errorMessage = errorMessage + (String)i.next();
	}
	
    JOptionPane.showMessageDialog(
        this,
        errorMessage,
        AppContext.getMessage("GeopistaName"),
        JOptionPane.ERROR_MESSAGE);
}
private boolean  ValidationErrorMessages() {
	Errors.clear();
    for (Iterator i = fieldNameToEnableCheckListMap.keySet().iterator();
        i.hasNext();
        ) {
        String fieldName = (String) i.next();
        for (Iterator j =
            fieldNameToEnableCheckListMap.getItems(fieldName).iterator();
            j.hasNext();
            ) {
        	
            EnableCheck enableCheck = (EnableCheck) j.next();
            String message = enableCheck.check(null);
            if (message != null) {
                 Errors.put(fieldName,message);
            }
        }
    }
    // Valida frente al esquema
    AbstractValidator validator=new SchemaValidator(null);
    //TODO: obtener el validator del objeto Layer
    
    // clona la feature y la rellena con los campos del formulario.
    Feature cloneFeature = getClonePopulatedFeature();
       
	validator.validateFeature(cloneFeature);
	for (Iterator i=validator.getErrorListIterator();i.hasNext();)
	{
		String errAtt=(String) i.next();
		Errors.put(errAtt,"Schema mismatch.");
	}
    return Errors.size()!=0;
    }

   private Feature getClonePopulatedFeature()
    {
    Feature clone= (Feature) feature.clone();
    GeopistaSchema schema=(GeopistaSchema)feature.getSchema();
    Object value=null;
   
    for (Iterator i=fieldNameToComponentMap.keySet().iterator();
    	i.hasNext();)// recorre los campos del formulario
    	{
    	String fieldName = (String) i.next();
    	if (fieldName.compareTo("DUMMY")==0) continue; // DUMMY is used for labels and separators
    	AttributeType attType=schema.getAttributeType(fieldName);
    	if (attType==AttributeType.STRING)
    		{
    		value=this.getText(fieldName);
    		}
    	else
    	if (attType==AttributeType.DOUBLE)
    		{
    		value=new Double(getDouble(fieldName));
    		}
    	else
    	if (attType==AttributeType.INTEGER)
    		{
    		value=new Integer(getInteger(fieldName));
    		}
    	clone.setAttribute(fieldName,value);
    	
    	}
    
    
	return clone;
    }
   public String getText(String fieldName) {
    if (fieldNameToComponentMap.get(fieldName) instanceof JTextField) {
        return ((JTextField) fieldNameToComponentMap.get(fieldName)).getText();
    }
    if (fieldNameToComponentMap.get(fieldName) instanceof JComboBox) {
    	Object select =((JComboBox) fieldNameToComponentMap.get(fieldName)).getSelectedItem();
        if (select instanceof StringDomain)
        	return ((StringDomain)select).getPattern(); // pattern es el código asociado
            else
            return select.toString();
    }
    if (fieldNameToComponentMap.get(fieldName) instanceof JCheckBox) {
        return ((JCheckBox) fieldNameToComponentMap.get(fieldName)).isEnabled()==true?"1":"0";
    }
    Assert.shouldNeverReachHere(fieldName);
    return null;
}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.MultiInputDialog#isInputValid()
	 */
	protected boolean isInputValid() {
		return ValidationErrorMessages();
	}
	
	  public void addRow(
	        String fieldName,
	        JComponent label,
	        JComponent component,
	        EnableCheck[] enableChecks,
	        String toolTipText) {
	        if (toolTipText != null) {
	            label.setToolTipText(toolTipText);
	        }
	        fieldNameToLabelMap.put(fieldName, label);
	        fieldNameToComponentMap.put(fieldName, component);
	        if (enableChecks != null) {
	            addEnableChecks(fieldName, Arrays.asList(enableChecks));
	        }
	        int componentX;
	        int componentWidth;
	        int labelX;
	        int labelWidth;
	        if (component instanceof JCheckBox
	            || component instanceof JLabel
	            || component instanceof JPanel) {
	            componentX = 1;
	            componentWidth = 3;
	            labelX = 4;
	            labelWidth = 1;
	        } else {
	            labelX = 1;
	            labelWidth = 1;
	            componentX = 2;
	            componentWidth = 1;
	        }
	        currentMainPanel.add(
	            label,
	            new GridBagConstraints(
	                labelX,
	                rowCount,
	                labelWidth,
	                1,
	                0.0,
	                0.0,
	                GridBagConstraints.WEST,
	                GridBagConstraints.NONE,
	                new Insets(0, 0, 5, 10),
	                0,
	                0));
	        //HORIZONTAL especially needed by separator. [Jon Aquino]
	        currentMainPanel.add(
	            component,
	            new GridBagConstraints(
	                componentX,
	                rowCount,
	                componentWidth,
	                1,
	                0,
	                0.0,
	                GridBagConstraints.WEST,
	                component instanceof JPanel
	                    ? GridBagConstraints.HORIZONTAL
	                    : GridBagConstraints.NONE,
	                new Insets(0, 0, 5, 0),
	                0,
	                0));
	        rowCount++;
	    }
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.MultiInputDialog#getLabel(java.lang.String)
	 */
	public JComponent getLabel(String fieldName) {
		// TODO Auto-generated method stub
		return super.getLabel(fieldName);
	}
}
