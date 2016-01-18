/**
 * FeatureByTablesBuilder.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.autoforms;

import java.beans.PropertyChangeEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.feature.TreeDomain;
import com.geopista.protocol.control.ISesion;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.ui.feature.AttributeFilter;
import com.geopista.util.TreeDomainGUIListener;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/** 
 * @uml.stereotype uml_id="Archetype::party" 
 */
public class FeatureByTablesBuilder implements  FormBuilder {

	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormBuilder#buildForm()
	 */
	public void buildForm() {
		buildDialogByGroupingTables();
		
	}

	  /**
     * Add a new field to Dialog
     * 
     */
     private void addField(String attName)
    {
        GeopistaSchema schema = (GeopistaSchema) ((Feature) formContext.getModel()).getSchema();
         Domain domain = schema.getAttributeDomain(attName);
        Column column = schema.getColumnByAttribute(attName);

        /**
         * Constructs form field depending on domain type
         */
        if (domain==null) 
        	addField(attName,Domain.getDomainForType(String.class), Domain.PATTERN);
        else
        if (domain.getType() == Domain.TREE)
        {

            TreeDomain td = (TreeDomain) domain;
            // Intenta localizar el dominio que se aplica a este atributo.
            Domain EffectiveDomain = td.getKeyDomainByColumn(column, ((Feature) formContext.getModel()));
            if (EffectiveDomain == null)
                EffectiveDomain = td.getKeyDomainByColumn(column); // sin
                                                                    // ajustarse
                                                                    // a valores
            // create component
            if (EffectiveDomain.getChildren().size() > 0)
                addField(attName, td, ((Domain) EffectiveDomain.getChildren().get(0))
                        .getType());// Crea el componente de formulario del
                                    // primer hijo. (Deben ser del mismo tipo
                                    // todos.)
            else
                addField(attName, td, EffectiveDomain.getType());
            // Strategy: add listeners if has subordinates
            int level = td.getLevelByColumn(schema.getColumnByAttribute(attName));
            if (level < td.getTreeDepth() - 1) // tiene subniveles
            {
                final JComponent thisField = (JComponent) formContext.getForm().getComponentByFieldName(attName);
                final TreeDomainGUIListener tDListener = new TreeDomainGUIListener(attName, td,
                		((Feature) formContext.getModel()), formContext.getForm());
                if (thisField instanceof JComboBox)
                {
                    ((JComboBox) thisField).addActionListener(tDListener);
                }
                else
                if (thisField instanceof JFormattedTextField)
                {
                    ((JFormattedTextField) thisField).addPropertyChangeListener("value",
                            tDListener);
                }
                else
                if (thisField instanceof JTextField)
                    {
                        ((JTextField) thisField).getDocument().addDocumentListener(
                        new DocumentListener(){

							public void insertUpdate(DocumentEvent arg0)
							{
							tDListener.propertyChange(new PropertyChangeEvent(thisField,"value",null,((JTextField)thisField).getText()));
							
							
							}

							public void removeUpdate(DocumentEvent arg0)
							{
							tDListener.propertyChange(new PropertyChangeEvent(thisField,"value",null,((JTextField)thisField).getText()));
							
							}

							public void changedUpdate(DocumentEvent evt)
							{
							tDListener.propertyChange(new PropertyChangeEvent(thisField,"value",null,((JTextField)thisField).getText()));
							
							}});
                    }
            }

            // TODO: assign tree EnableCheck using fieldNameToEnableCheckListMap
        } else
            addField(attName, domain, domain.getType());
    }

    private void addField(String attName, Domain asDomain, int domainType)
    {
        GeopistaSchema schema = (GeopistaSchema) ((Feature) formContext.getModel()).getSchema();
        FeatureFieldPanel form=((FeatureFieldPanel)formContext.getForm());
        Domain domain = asDomain;
        Column column = schema.getColumnByAttribute(attName);
        if (domainType == Domain.PATTERN)
        {
        	 String valor="";
			if(column.getName().equals(ConstantesEIEL.KEY_CLAVE)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_CLAVE)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_CLAVE));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}else if(column.getName().equals(ConstantesEIEL.KEY_COD_PROV)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_PROV)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_PROV));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}else if(column.getName().equals(ConstantesEIEL.KEY_COD_MUNIC)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_MUNIC)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_MUNIC));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
        	}else if(column.getName().startsWith(ConstantesEIEL.KEY_COD_ORDEN)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
    		}else if(column.getName().equals(ConstantesEIEL.KEY_COD_ENTIDAD)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ENTIDAD)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ENTIDAD));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}else if(column.getName().equals(ConstantesEIEL.KEY_COD_POBLAMIENTO)){
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_POBLAMIENTO)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_POBLAMIENTO));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
		
			}
			else if(column.getName().equals(ConstantesEIEL.KEY_TRAMO_EM)){
				// se añade esta condicion debido a que la capa de emisarios, no contiene el campo cod_orden y si tramo_em
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}
			else if(column.getName().equals(ConstantesEIEL.KEY_TRAMO_CL)){
				// se añade esta condicion debido a que la capa de colectores, no contiene el campo cod_orden y si tramo_em
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}
			else if(column.getName().equals(ConstantesEIEL.KEY_TRAMO_CN)){
				// se añade esta condicion debido a que la capa de colectores, no contiene el campo cod_orden y si tramo_em
				if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN)!=null)
					valor =new String((String) AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN));
				else
					valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}
			else{
				  valor = (String) ((Feature) formContext.getModel()).getString(attName);
			}
    	    // si el componente es de tipo UrlTextField, y el texto es demasiado grande
    	    // para mostrarlo en el campo de texto se pone un TextArea.
            
            // Comprobamos si valor es una url para que lo considere como UrlTextField
            if (checkURL(valor)){
            	form.addUrlTextField(attName, valor, domain.getAproxLenght(),
	            		domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE), asDomain.getFormat(), column
	                            .getDescription());
            }
            else{
	            if (valor.length()>36){
	            	form.addJScrollPane(attName, valor, domain.getAproxLenght(),
		            		domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE), asDomain.getFormat(), column
	                        .getDescription());
	            }
	            else{
	            	if ((domain!=null) && (domain instanceof StringDomain) && ((StringDomain)domain).getMaxLength()>36){
	                	form.addJScrollPane(attName, valor, domain.getAproxLenght(),
	    	            		domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE), asDomain.getFormat(), column
	                            .getDescription());            		
	            	}
	            	else{
		            form.addUrlTextField(attName, valor, domain.getAproxLenght(),
		            		domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE), asDomain.getFormat(), column
		                            .getDescription());
	            	}
	            }
            }

        } else if (domainType == Domain.NUMBER || domainType == Domain.AUTO)
        {
            String valor = (String) ((Feature) formContext.getModel()).getString(attName);
            if (asDomain.getPattern().equals(Const.KEY_ID_MUNI)){
            	addMunicipalitiesCombo(attName, domain);
            }else{
	            form.addTextField(attName, valor, domain.getAproxLenght(), null,
	                    domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE), asDomain.getFormat(), column
	                            .getDescription());
            }
        }
        // else
        // if (domainType == Domain.NUMBER || domainType==Domain.AUTO)
        // {
        //			
        //				
        // if (type.equals(AttributeType.INTEGER))
        // {
        // Integer valor = (Integer) ((Feature) formContext.getModel()).getAttribute(attName);
        // addIntegerField(attName, valor.intValue(), domain.getAproxLenght(),
        // column.getDescription());
        // }
        // //if (domain.getType() == Domain.FLOAT)
        // if (type.equals(AttributeType.DOUBLE))
        // {
        // Double valor = (Double) ((Feature) formContext.getModel()).getAttribute(attName);
        // addDoubleField(attName, valor.doubleValue(),
        // domain.getAproxLenght());
        // }
        // if (domainType==Domain.AUTO)
        // {
        // getComponent(attName).setEnabled(false);
        // }
        // }
        else if (domain.getType() == Domain.DATE)
        {
            // Añade un campo de fecha con un JCalendar
            Date fecha;
            Object val=((Feature) formContext.getModel()).getAttribute(attName);
            if (((Feature) formContext.getModel()).getSchema().getAttributeType(attName) == AttributeType.DATE && val instanceof Date)
                {
           		fecha = (Date)val ;
                }
            else
                try
                {
                    // Intenta el parseo
                    fecha = DateFormat.getDateInstance(DateFormat.SHORT).parse(((Feature) formContext.getModel()).getString(attName));
                } catch (ParseException e)
                {
                    // Si no consigue generar la fecha se deja nula
                    fecha = null;
                    //String form=DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
                   // logger.debug("Fallo al parsear fecha. Debe ser con formato p.ejemplo:"+form,e);
                    
                }
            form.addDateField(attName, fecha, column.getDescription(), ((DateDomain)domain).getInitialDate(),((DateDomain)domain).getFinalDate(),domainType != Domain.AUTO && schema.getAttributeAccess(attName).equals(GeopistaSchema.READ_WRITE));
        }// Fin Domain.DATE
        else if (domainType == Domain.CODEDENTRY || domainType == Domain.CODEBOOK
                || domainType == Domain.TREE)
        {

            // Constructs the ComboBox
            addCodeBookCombo(attName, (TreeDomain) domain);
        } else if (domainType == Domain.BOOLEAN)
        {
            // Constructs the CheckBox
            String valor = ((Feature) formContext.getModel()).getString(attName);
            boolean booleanValue = false;
            //if(valor != null) booleanValue = (new Boolean(valor)).booleanValue();
            if (valor == null)
                valor = "0";
            form.addCheckBox(attName, valor.compareTo("1") == 0, column.getDescription());
            //form.addCheckBox(attName, booleanValue, column.getDescription());
        }
    }
    /**
     * Adds a new Combo control with a list of options
     * 
     * @param attName
     * @param tdDomain
     */
    private void addCodeBookCombo(String attName, TreeDomain tdDomain)
    {
        GeopistaSchema schema = (GeopistaSchema) ((Feature) formContext.getModel()).getSchema();
        FeatureFieldPanel form = ((FeatureFieldPanel)formContext.getForm());
        // Obtiene el dominio que se aplica como padre de este nivel
        // y para los valores de esta feature
        Domain parentDomain = tdDomain.getKeyDomainByColumn(schema
                .getColumnByAttribute(attName), ((Feature) formContext.getModel()));
        Vector combo = new Vector();
        Domain selected = null;
        if (parentDomain != null)
        {
            Iterator domainChildren = parentDomain.getChildren().iterator();

            /**
             * get current value of the attribute
             */
            String val = ((Feature) formContext.getModel()).getString(schema.getAttributeIndex(attName));
            if (val == null)
                val = "";
            while (domainChildren.hasNext())
            {
                Domain domainChild = (Domain) domainChildren.next();
                String value = domainChild.getRepresentation();
                String code = domainChild.getPattern();
                if (val.equals(code))
                    selected = domainChild;
                combo.add(domainChild);
            }
        }
        form.addComboBox(attName, selected, combo, tdDomain.getDescription(schema
                .getColumnByAttribute(attName)));

    }


    /**
     * Adds a new Combo control with a list of options
     * 
     * @param attName
     * @param tdDomain
     */
    private void addMunicipalitiesCombo(String attName, Domain tdDomain)
    {
        GeopistaSchema schema = (GeopistaSchema) ((Feature) formContext.getModel()).getSchema();
        FeatureFieldPanel form = ((FeatureFieldPanel)formContext.getForm());
        Vector combo = new Vector();
        Domain selected = null;
        ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
        Iterator itMunicipios = iSesion.getAlMunicipios().iterator();
        while (itMunicipios.hasNext()){
        	Municipio municipio = (Municipio)itMunicipios.next();
            combo.add(String.valueOf(municipio.getId()));
        }
        String sMunicipioSelected = ((Feature) formContext.getModel()).getAttribute(schema.getAttributeIndex(attName)).toString();
        if (sMunicipioSelected == null)
            sMunicipioSelected = iSesion.getIdMunicipio();
        form.addComboBox(attName, sMunicipioSelected, combo, "");
    }

    /**
	 * @uml.property  name="formContext"
	 * @uml.associationEnd  inverse="featureByTablesBuilder:com.geopista.ui.autoforms.FormContext"
	 * @uml.association  name="definition"
	 */
	private FeatureFormContext formContext;


	/* (non-Javadoc)
	 * @see com.geopista.ui.autoforms.FormBuilder#setContext(com.geopista.ui.autoforms.FormContext)
	 */
	public void setContext(FormContext formContext) {
		this.formContext = (FeatureFormContext) formContext;
	}
	  /**
     * Construye el diálogo utilizando paneles decorados para agrupar los campos
     * según su tabla de procedencia
     * 
     */
    private void buildDialogByGroupingTables()
    {
        Feature feature= (Feature) formContext.getModel();
        FeatureFieldPanel panel= (FeatureFieldPanel) formContext.getForm();
    	if (feature == null)
            return;
        // get metadata
        if (!(feature.getSchema() instanceof GeopistaSchema))
        	{
            feature=GeopistaSchema.vampiriceSchema(feature); // Intenta
                                                                // generar un
                                                                // esquema
                                                                // básico por
                                                                // defecto.
            formContext.setModel(feature);
        	}
        GeopistaSchema schema = (GeopistaSchema) feature.getSchema();
        int count = schema.getAttributeCount();

        /**
         * traverse columns grouping by table
         */
//        HashMap tables = new HashMap();
        
        Comparator comparatorTable = new Comparator(){
            public int compare(Object o1, Object o2) {
    			Table t1 = (Table) o1;
    			Table t2 = (Table) o2;
    			
    			String field1 = t1.getName();
    			String field2 = t2.getName();
    			
    			return field1.compareToIgnoreCase(field2);
    		}
        };
        TreeMap tables = new TreeMap(comparatorTable);
        
        
        
        for (int i = 0; i < count; i++)
        {
            if (schema.getGeometryIndex() == i)
                continue;

            Column col = schema.getColumnByAttribute(i);
            Table table = col.getTable();
            Collection columns;
            if ((columns = (Collection) tables.get(table)) == null) // get
                                                                    // previous
                                                                    // table
                                                                    // registered
                                                                    // if any
            { // Not registered yet
                columns = new ArrayList();
                tables.put(table, columns);
            }
            columns.add(col); // Adds the column to table schema
        }

        Iterator iterTables = tables.entrySet().iterator();

        int dialogColumns = 1;

        // currentMainPanel.setLayout(new VerticalFlowLayout());
        

        while (iterTables.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterTables.next();
            Table table = (Table) entry.getKey();
            Collection cols = (Collection) entry.getValue();

            panel.createGroupingPanel(table.getName());

            AttributeFilter attributeFilter = AttributeFilter.getInstance();            
            // this.addLabel(table.getName());rows++;
            // this.addSeparator();rows++;
            Iterator iterCols = cols.iterator();
            while (iterCols.hasNext())
            {
                Column col = (Column) iterCols.next();
                if (attributeFilter.isBlackListed(col.getName())){
                	continue;
                }
                addField(schema.getAttribute(col));
                /**
                 * Checks whether we need more than a column of fields
                 */

                if (panel.getCurrentRowCount() > count / 2 && dialogColumns != 2)
                {
                    panel.incrementCurrentRowCount();
                    panel.incrementCurrentRowCount();
                  
                    panel.startNewColumn();
                    dialogColumns = 2;
                    panel.createGroupingPanel(table.getName());
                   

                  
                   
                }
            }
           
        }
    }
    
    private boolean checkURL(String texto)
	{
		try
		{
		URL url=new URL(texto);
		} catch (MalformedURLException e)
		{
		return false;
		}
		return true;
	}

}
