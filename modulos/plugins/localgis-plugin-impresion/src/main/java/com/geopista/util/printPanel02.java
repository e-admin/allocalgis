/**
 * printPanel02.java
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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class printPanel02 extends JPanel implements  com.geopista.ui.wizard.WizardPanel {
	
	private static final Log logger	= LogFactory.getLog(printPanel02.class);
	
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext context;

	private String nextID = null;
	private String localId = null;
	
	private Map datosEscalas = null;
	private JComboBox escalasComboBox = null;
	private JCheckBox incluirLeyendaCheckBox = null;
	
	public printPanel02(String id, String nextId, PlugInContext context) {
		this.nextID = nextId;
		this.localId = id;
		this.context=context;
		
		initialize();
	}		

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setName(UtilsPrintPlugin.getMessageI18N("PrintPanel02.Name"));
		setLayout(new GridBagLayout());
		setSize(365, 274);
		add(getPanelDatosEscalaLeyenda(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}
	
	
	private JPanel getPanelDatosEscalaLeyenda() {
		int posFila = 0;
		//Establecer valores escala
		if (datosEscalas == null)
			datosEscalas = UtilsPrintPlugin.getDatosEscalasDisponibles ();
		//Cargamos combo
		escalasComboBox = new JComboBox(datosEscalas.values().toArray());
		escalasComboBox.setEditable(true);
		escalasComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				wizardContext.inputChanged();
			}
		});
        
		incluirLeyendaCheckBox = new JCheckBox (UtilsPrintPlugin.getMessageI18N("PrintPanel02.Leyenda"));
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("PrintPanel02.Escala")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(escalasComboBox,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(incluirLeyendaCheckBox,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		
		return panel;
	}

	public void enteredFromLeft(Map dataMap) {
		//Obtenemos configuracion y cargamos datos en pantalla
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		
		escalasComboBox.setSelectedItem(datosEscalas.get((config != null && config.getIdEscala()!= null)? Integer.valueOf(config.getIdEscala()) : -2));
		incluirLeyendaCheckBox.setSelected(config.isMostrarLeyenda());
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		//Obtenemos configuracion y actualizar datos establecidos
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		config.setDatosEscalas(datosEscalas);
		config.setIdEscala(UtilsPrintPlugin.getClaveMapaPorValor(datosEscalas, escalasComboBox.getSelectedItem().toString()));
		config.setMostrarLeyenda(incluirLeyendaCheckBox.isSelected());
	}

	
	public boolean isInputValid()
	{
		boolean isValid = false;
		try
		{
			//TODO:
			double aa = context.getWorkbenchContext().getLayerViewPanel().getViewport().getScale();
			double escaleNormalice = context.getWorkbenchContext().getLayerViewPanel().getNormalizedScale(aa); 
			
			String valorEscala = (String) escalasComboBox.getSelectedItem();
			//Comprobamos si el valor es valido: pertenece a la lista de valores de por defecto tiene formato adecuado
			if (valorEscala != null) {
				String idEscala = UtilsPrintPlugin.getClaveMapaPorValor(datosEscalas, valorEscala);
				//Pertenece lista valores inicial 
				if (idEscala != null) 
					isValid = true;
				else {
					//Entrada manual: comprobamos formato
					String escalaReal[] =  valorEscala.split(":");
					if ("1".equals(escalaReal[0]) && Integer.valueOf(escalaReal[1]).intValue() > 0) {
						isValid = true;
						datosEscalas.put(Integer.valueOf(escalaReal[1]), valorEscala);
						escalasComboBox.addItem(valorEscala);
					}
				}
			}
		} catch (NumberFormatException e) {
			logger.debug("isInputValid() - Formato incorrecto en la escala.");
		}
		return isValid;
	}
	

	public void add(InputChangedListener listener) {
	}

	public void remove(InputChangedListener listener){
	}

	public String getTitle() {
		return this.getName();
	}

	public String getID() {
		return localId;
	}

	public String getInstructions() {
		return UtilsPrintPlugin.getMessageI18N("PrintPanel02.Instructions");
	}

	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}
	public void setNextID(String nextID) {
		this.nextID=nextID;
	}
	public String getNextID() {
		return nextID;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}
}  //  @jve:decl-index=0:
