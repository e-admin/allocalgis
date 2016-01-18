/**
 * ComboItemGraticuleListener.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
