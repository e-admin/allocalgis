/**
 * printPanel01.java
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
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**

 */

public class printPanel01 extends javax.swing.JPanel implements WizardPanel{
	
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext context;

	private String nextID = null;
	private String localId = null;

	private JTextField tituloTextField;
	private JTextArea descripcionTextArea;

	public printPanel01(String id, String nextId, PlugInContext context2) {
		this.context=context2;
		this.nextID = nextId;
		this.localId = id;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		setName(UtilsPrintPlugin.getMessageI18N("PrintPanel01.Name"));
		setLayout(new GridBagLayout());
		setSize(365, 274);
		add(getPanelInformacionAdicional(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}

	private JPanel getPanelInformacionAdicional() {
		int posFila = 0;
		
		tituloTextField = new JTextField();
		tituloTextField.setPreferredSize(new java.awt.Dimension(239, 21));
		tituloTextField.setSize(100, 20);
		tituloTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent evt) {
				onChangeTituloMapa();
			}
		});
		
		descripcionTextArea = new JTextArea();
		descripcionTextArea.setLineWrap(true);
		descripcionTextArea.setRows(4);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(descripcionTextArea);
        
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("PrintPanel01.titulo")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(tituloTextField,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("PrintPanel01.descripcion")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		panel.add(scroll,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		
		tituloTextField.grabFocus();
		tituloTextField.requestFocus();
		
		return panel;
	}
	
	
	/** Auto-generated event handler method */
	protected void onChangeTituloMapa(){
		wizardContext.inputChanged();
	}

	
	public void enteredFromLeft(Map dataMap) {
		//Obtenemos configuracion y cargamos datos en pantalla
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		tituloTextField.setText((config != null)? config.getTitulo() : "");
		descripcionTextArea.setText((config != null)? config.getDescripcion() : "");
		
		tituloTextField.grabFocus();
		tituloTextField.requestFocus();
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		//Obtenemos configuracion y actualizar datos establecidos
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		config.setTitulo(tituloTextField.getText());
		config.setDescripcion(descripcionTextArea.getText());
	}
	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	
	public String getTitle() {
		return this.getName();
	}
	public String getInstructions() {
		return  (UtilsPrintPlugin.getMessageI18N("PrintPanel01.Instructions")); 

	}
	public boolean isInputValid() {
		boolean isValid = false;
		if (tituloTextField.getText() != null && !tituloTextField.getText().trim().equals(""))
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
		return nextID;
	}
	public void add(InputChangedListener listener){
	}
	public void remove(InputChangedListener listener) {
	}
	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}
	
}
