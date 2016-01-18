/*
 * Created on 06-may-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.geopista.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.TreeDomain;
import com.geopista.ui.autoforms.FieldPanelAccessor;
import com.vividsolutions.jump.feature.Feature;

/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TreeDomainGUIListener implements  ItemListener, ActionListener,  PropertyChangeListener {
	
	FieldPanelAccessor base;
	String attName;
	Feature feature;
	TreeDomain td;
	
	String oldValue="";// comprueba que se haya cambiado algo
	
	/**
	 * 
	 */
	public TreeDomainGUIListener(String attName, TreeDomain td, Feature feature, FieldPanelAccessor base ) {
		super();
		this.base=base;
		this.attName=attName;
		this.feature=feature;
		this.td=td;
	}

//	/* (non-Javadoc)
//	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
//	 */
//	public void focusGained(FocusEvent e) {
//		
//		JComponent padre = (JComponent) e.getComponent();
//		if (padre instanceof JTextComponent) 
//		{
//			oldValue= ((JTextComponent)padre).getText();
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
//	 */
//	public void focusLost(FocusEvent e) {
//		
//		if (e.isTemporary())return;
//		// Recalcula el valor del componente hijo
//		JComponent padre = (JComponent) e.getComponent();
//		if (padre instanceof JTextComponent) 
//		{
//			if (((JTextComponent)padre).getText().compareTo(oldValue)!=0)
//				doUpdate(e.getSource());;//((JTextComponent)padre);
//		}
//	}
void doUpdate(Object object)
{
	String value=null;
	if (object instanceof JComboBox)
		{
		JComboBox source =(JComboBox) object;
		value= ((Domain) source.getSelectedItem()).getPattern();
		}
	if (object instanceof JTextField)
		{
		JTextField source=(JTextField) object;
		value= source.getText();
		}
	GeopistaSchema schema=(GeopistaSchema)feature.getSchema();
	// actualiza el componente destino
	int myLevel =td.getLevelByColumn(schema.getColumnByAttribute(attName));
	
	
	
	Component targetComp = base.getComponentByFieldName(schema.getAttribute(td.getColumnByLevel(myLevel+1)));
	JLabel label=(JLabel) base.getLabel(schema.getAttribute(td.getColumnByLevel(myLevel+1)));
	
	
	if (targetComp instanceof JComboBox)
		{
		
		// Recalcular el campo hijo en función del nuevo valor
		
		Domain myParentDomain = td.getKeyDomainByColumn(schema.getColumnByAttribute(attName), feature);
		//Busca el nuevo valor en el propio dominio primario
		Domain myDomain= myParentDomain.getMatchedChildDomain(feature,attName,value);
		if (myDomain==null) // el cambio realizado no es coherente con el árbol de restricciones
		{
		// do something with Object (cause of error)
			((JComponent)object).setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.red));
			return; //end processing
		}
		else
		((JComponent)object).setBorder(BasicBorders.getTextFieldBorder());
		
		// regenera el control con mi nuevo dominio.
		Iterator domainChildren = myDomain.getChildren().iterator();
		
		((JComboBox)targetComp).removeAllItems();
		while (domainChildren.hasNext())
			{
			Domain domainChild=(Domain)domainChildren.next();
	
			((JComboBox)targetComp).addItem(domainChild);
			
			}
		// Cambia la etiqueta y el tooltip
				
		label.setToolTipText(myDomain.getDescription());

		}
		
}
	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		String value;
		if (true) return;
//
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		doUpdate(e.getSource());
		
		
	}



	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		doUpdate(evt.getSource());
		return;
	}
}
