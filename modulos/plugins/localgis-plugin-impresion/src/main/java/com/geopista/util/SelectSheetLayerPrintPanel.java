/**
 * SelectSheetLayerPrintPanel.java
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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.SeriePrintPlugIn;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class SelectSheetLayerPrintPanel extends JPanel implements WizardPanel
{
	private JComboBox capaIteracionComboBox = null;
	private JComboBox campoIteracionComboBox = null;
	private JTextArea textoIteracionTextArea;
	
	private String localId = null;
	private String nextId = null;

	private Blackboard blackboard  = ((AppContext) AppContext.getApplicationContext()).getBlackboard();
	private WorkbenchContext context;
	private WizardContext wizardContext;

	public SelectSheetLayerPrintPanel(String id, String nextID, WorkbenchContext context) {
		super();

		this.localId = id;
		this.nextId = nextID;
		this.context=context;
		initialize();
	}
	/**
	 * This is the default constructor
	 */
	public SelectSheetLayerPrintPanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		setName(UtilsPrintPlugin.getMessageI18N("SelectSheetLayerPrintPanel.Name"));
		setLayout(new GridBagLayout());
		setSize(365, 274);
		add(getPanelDatosIteracion(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}

	private JPanel getPanelDatosIteracion() {
		int posFila = 0;
		capaIteracionComboBox = new JComboBox();
		capaIteracionComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onLayerSelectedDo();
			}
		});
		campoIteracionComboBox  = new JComboBox(new Object[]{});
		campoIteracionComboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onCampoSelectedDo();
			}
		});
		
		textoIteracionTextArea = new JTextArea();
		textoIteracionTextArea.setLineWrap(true);
		textoIteracionTextArea.setRows(4);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(textoIteracionTextArea);
        
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("SelectSheetLayerPrintPanel.capaIteracion")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(capaIteracionComboBox,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("SelectSheetLayerPrintPanel.campoIteracion")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		panel.add(campoIteracionComboBox,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("SelectSheetLayerPrintPanel.textoIteracion")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		panel.add(scroll,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		
		return panel;
	}


	private void onLayerSelectedDo() {
		Layer selected =(Layer) capaIteracionComboBox.getSelectedItem();
		if (selected != null) {
			//Caraga combo campo de iteracion
			campoIteracionComboBox.removeAllItems();
			//Incluir opcion vacia
			campoIteracionComboBox.addItem("");
			for (int i = 0; i < selected.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++)
				campoIteracionComboBox.addItem(selected.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			
			wizardContext.inputChanged();
		}
	}

	/** Auto-generated event handler method */
	protected void onCampoSelectedDo(){
		wizardContext.inputChanged();
	}

	public void setNextID(String nextId) {
		this.nextId = nextId;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#enteredFromLeft(java.util.Map)
	 */
	public void enteredFromLeft(Map dataMap)
	{
		Layer initialSelectLayer = (Layer) context.getLayerManager().getLayer(SeriePrintPlugIn.DEFAULT_LAYER_ITERACION);
		if(initialSelectLayer==null) initialSelectLayer=context.getLayerManager().getLayer(0);
		capaIteracionComboBox = GeopistaFunctionUtils.initializeLayerComboBox("SheetLayer", capaIteracionComboBox, initialSelectLayer, null, context.getLayerManager().getLayers());
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exitingToRight()
	 */
	public void exitingToRight() throws Exception {
		//Obtenemos configuracion
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		//Actualizar datos
		config.setCapaIteracion((Layer)capaIteracionComboBox.getSelectedItem());
		config.setNombreCampoIteracion((String)campoIteracionComboBox.getSelectedItem());
		config.setTextoIteracion((String)textoIteracionTextArea.getText());
		//Almacenar nueva configuracion
		blackboard.put(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN, config);
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#add(com.vividsolutions.jump.workbench.ui.InputChangedListener)
	 */
	public void add(InputChangedListener listener) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#remove(com.vividsolutions.jump.workbench.ui.InputChangedListener)
	 */
	public void remove(InputChangedListener listener) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getTitle()
	 */
	public String getTitle() {
		return this.getName();
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getID()
	 */
	public String getID() {
		// TODO Auto-generated method stub
		return localId;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getInstructions()
	 */
	public String getInstructions() {
		return UtilsPrintPlugin.getMessageI18N("SelectSheetLayerPrintPanel.Instructions");
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#isInputValid()
	 */
	public boolean isInputValid() {
		boolean isValid = false;
		if (capaIteracionComboBox.getSelectedItem() != null && campoIteracionComboBox.getSelectedItem() != null && !campoIteracionComboBox.getSelectedItem().equals(""))
			isValid = true;
		return isValid;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#setWizardContext(com.geopista.ui.wizard.WizardContext)
	 */
	public void setWizardContext(WizardContext wd) {
		wizardContext = wd;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#getNextID()
	 */
	public String getNextID() {
		// TODO Auto-generated method stub
		return nextId;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
