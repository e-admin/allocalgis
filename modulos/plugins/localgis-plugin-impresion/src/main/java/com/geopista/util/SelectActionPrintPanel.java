/**
 * SelectActionPrintPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**

 */

public class SelectActionPrintPanel extends javax.swing.JPanel implements WizardPanel{

	private static final long serialVersionUID = 1L;

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext context;

	private String[] arrayNextID = null;
	private String nextID = null;
	private String localId = null;

	private JRadioButton radioCrear = null;
	private JRadioButton radioModificar = null;
	private JRadioButton radioImprimirSerie = null;

	public SelectActionPrintPanel(String id, String[] arrayNextID, PlugInContext context) {
		this.context=context;
		this.arrayNextID = arrayNextID;
		this.localId = id;

		initialize();
	}

	private  void initialize() {
		this.setName((UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.Name"))); 
		this.setLayout(new GridBagLayout());
		this.setSize(365, 274);
		this.add(getPanelAccionesDisponibles(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}

	private JPanel getPanelAccionesDisponibles() {
		int posFila = 0;
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.seleccioneAccion")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(getOpcionesSeleccion (),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		
		return panel;
	}
	
	
	private JPanel getOpcionesSeleccion () 
	{
		radioCrear = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.crearPlantilla"));
		radioCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeOptionRadio ();
			}
		});	
		
		radioModificar = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.modificarPlantilla"));
		radioModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeOptionRadio ();
			}
		});	
		
		radioImprimirSerie = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.imprimirSerie"));
		radioImprimirSerie.setSelected(true);
		radioImprimirSerie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeOptionRadio ();
			}
		});	
		
		ButtonGroup groupRadio = new ButtonGroup();
		groupRadio.add(radioCrear);
		groupRadio.add(radioModificar);	
		groupRadio.add(radioImprimirSerie);
		
		int posFila = 0;
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(radioImprimirSerie,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(radioCrear,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(radioModificar, new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		
		radioImprimirSerie.grabFocus();
		radioImprimirSerie.requestFocus();
		
		return panel;
	}
	
	private void onChangeOptionRadio() {
		wizardContext.inputChanged();
	}
	public void enteredFromLeft(Map dataMap) {
		//Obtenemos configuracion y cargamos datos en pantalla
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		//Cargar accion seleccionada
		radioCrear.setSelected(config.getEsAccionCrearPlantilla());
		radioModificar.setSelected(config.getEsAccionModificarPlantilla());
		radioImprimirSerie.setSelected(config.getEsAccionImprimirSerie());
		
		
		radioImprimirSerie.grabFocus();
		radioImprimirSerie.requestFocus();
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		//Registrar opcion marcada
		config.establecerAccionPlugin(radioCrear.isSelected(), radioModificar.isSelected(), false, radioImprimirSerie.isSelected());
		//Procesar accion crear plantilla para finalizar, si no se continua en el wizard
		if (radioCrear.isSelected())
			UtilsPrintPlugin.realizarAccionPlugin(context, this, config);
	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	public void add(InputChangedListener listener) {
	}

	public void remove(InputChangedListener listener) {
	}
	public String getTitle() {
		return this.getName();
	}

	public String getInstructions() {
		return UtilsPrintPlugin.getMessageI18N("SelectActionPrintPanel.Instructions"); 
	}
	public boolean isInputValid() {
		boolean isValid = false;
		if (radioCrear.isSelected() || radioModificar.isSelected() || radioImprimirSerie.isSelected())
			isValid = true;
		return isValid;
	}
	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}
	public String getID() {
		return localId;
	}

	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		//Determinar siguiente panel segun  accion seleccionada
		String id = null;
		if (radioCrear.isSelected())
			id = arrayNextID[0];
		else if (radioModificar.isSelected())
			id = arrayNextID[1];
		else if (radioImprimirSerie.isSelected())
			id = arrayNextID[2];
		return id;
	}
	

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}
}
