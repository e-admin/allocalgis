package com.geopista.ui.autoforms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.Column;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.StringDomain;
import com.geopista.feature.ValidationError;
import com.geopista.ui.components.DateField;
import com.geopista.ui.components.TwoColumnsFieldPanel;
import com.geopista.ui.components.UrlTextField;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;


public abstract class AbstractFieldPanel extends JPanel implements PropertyChangeListener, ItemListener, com.geopista.ui.autoforms.AutoForm {



	public abstract boolean checkPanel(boolean updateView);

	public boolean checkPanels() {
	    return checkPanel(false);
	}

	
	

	public abstract void disableAll();

	public void enter() {
	    // TODO Auto-generated method stub
	
	}

	public void exit() {
	    // TODO Auto-generated method stub
	
	}

	

	abstract public JComponent getComponent(String fieldName) ;

	abstract public JComponent getComponentByFieldName(String attName);

	abstract int getCurrentRowCount();

	
	
	/**
	 * @return
	 */
	public abstract Collection getFieldComponents();

	/**
	 * @return
	 */
	public abstract Collection getFieldLabels();

	/**
	 * @return
	 */
	public abstract Set getFieldNames() ;
	

	
	public abstract AbstractValidator getValidator();

	/**
	 * Devuelve el valor asociado a cada campo. Un Date si es un DateField si es
	 * DATE Un Integer si es tipo INTEGER Un Double si es tipo DOUBLE Un String
	 * si es tipo STRING Lanza una assertion si es tipo GEOMETRY o OBJECT
	 * Devuelve Null si hay un error de parse
	 * 
	 * @param attName
	 * @return
	 */
	public abstract Object getValue(String attName);

	public abstract boolean highLightFieldError(ValidationError err);

	
	protected abstract void initialize();

	public void itemStateChanged(ItemEvent e) {
	    checkPanel(true);
	
	}

	public void propertyChange(PropertyChangeEvent evt) {
	    if (evt.getPropertyName().equals("value"))
	    {
	        // revisa el formulario
	        checkPanel(true);
	    }
	
	}



	public void setApplicationContext(ApplicationContext context) {
	    // TODO Auto-generated method stub
	
	}

	public abstract void setCurrentTargetPanel(JPanel panel);



}
