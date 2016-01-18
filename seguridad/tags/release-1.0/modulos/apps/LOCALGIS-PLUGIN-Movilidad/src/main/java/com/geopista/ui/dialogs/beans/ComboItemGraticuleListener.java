package com.geopista.ui.dialogs.beans;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.mobile.MobilePluginI18NResource;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;

/**
 * Combo asociado a una feature para que al seleccionar un elemento seleccione la feature y lo deseleccione en caso contrario
 * @author irodriguez
 *
 */
public class ComboItemGraticuleListener implements ItemListener{

	public final static String SIN_ASIGNAR = AppContext.getApplicationContext().getI18nString(MobilePluginI18NResource.ComboItemGraticuleListener_sinAsignar);
	private Feature feature;
	private ILayerViewPanel layerViewPanel;
	private ILayer layer;
	private JLabel labelCelda;
	private WizardContext wizardContext;
	
	public ComboItemGraticuleListener(ILayerViewPanel layerViewPanel2, ILayer layer, Feature feature, JLabel labelCelda, WizardContext wizardContext){
		this.layerViewPanel = layerViewPanel2;
		this.layer = layer;
		this.feature = feature;
		this.labelCelda = labelCelda;
		this.wizardContext = wizardContext;
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object item = e.getItem();
		if(item.equals(SIN_ASIGNAR)){
			layerViewPanel.getSelectionManager().getFeatureSelection().unselectItems(layer, feature);
			labelCelda.setForeground(Color.RED);
		}
		else {
			layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(layer, feature);
			labelCelda.setForeground(Color.BLUE);
		}
		
		wizardContext.inputChanged(); //indicamos que ya se puede habilitar el boton
	}
}
