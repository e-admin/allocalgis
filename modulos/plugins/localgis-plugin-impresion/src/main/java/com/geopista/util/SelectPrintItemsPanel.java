/**
 * SelectPrintItemsPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.CUtilidadesComponentes;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.SelectPrintItemsTableModel;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.render.CheckBoxRenderer;
import com.geopista.util.render.CheckBoxTableEditor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
/**

 */

public class SelectPrintItemsPanel extends javax.swing.JPanel implements WizardPanel{

	private static final long serialVersionUID = 1L;

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard  = aplicacion.getBlackboard();
	private WizardContext wizardContext;
	private PlugInContext context;

	private String nextID = null;
	private String localId = null;

	private JRadioButton radioInvertir = null;
	private JRadioButton radioNoVacios = null;
	private JRadioButton radioTodos = null;
	private JLabel numHojasLabel = null;
	private JTable listaEltosImprimirTable;
	private SelectPrintItemsTableModel eltosImprimirTableModel;

	public SelectPrintItemsPanel(String id, String nextId, PlugInContext context2) {
		this.context=context2;
		this.nextID = nextId;
		this.localId = id;

		initialize();
	}

	private  void initialize() {
		this.setName((UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.Name"))); 
		this.setLayout(new GridBagLayout());
		this.setSize(365, 274);
		this.add(getPanelDatosImprimir(),  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(10, 10, 0, 10), 0, 0));
	}

	private JPanel getPanelDatosImprimir() {
		int posFila = 0;
		numHojasLabel = new JLabel("");
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.seleccioneDatos")),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(getOpcionesSeleccion (),  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 10, 0, 10), 0, 0));
		panel.add(getDataTable (), new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(numHojasLabel,  new GridBagConstraints(0, posFila++, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.CENTER,  new Insets(5, 20, 0, 10), 0, 0));

		return panel;
	}

	private void clearDataTable () {
		//Vaciamos DT
		CUtilidadesComponentes.clearTable(eltosImprimirTableModel);
		//Vaciamos lista datos 
		eltosImprimirTableModel.clearListaDatos();
	}
	
	private JPanel getOpcionesSeleccion () 
	{
		radioTodos = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.seleccionarTodos"));
		radioTodos.setSelected(true);
		radioTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeRadioTodos ();
			}
		});	
		
		radioNoVacios = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.seleccionarNoVacios"));
		radioNoVacios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeRadioNoVacios ();
			}
		});	
		
		radioInvertir = new JRadioButton(UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.invertirSeleccion"));
		radioInvertir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeRadioInvertir ();
			}
		});	
		
		ButtonGroup groupRadio = new ButtonGroup();
		groupRadio.add(radioTodos);
		groupRadio.add(radioNoVacios);	
		groupRadio.add(radioInvertir);
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(radioTodos,  new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(0, 10, 0, 10), 0, 0));
		panel.add(radioNoVacios, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		panel.add(radioInvertir,  new GridBagConstraints(2, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,  new Insets(5, 20, 0, 10), 0, 0));
		
		return panel;
	}
	
	private void onChangeRadioTodos() {
		CheckBoxRenderer renderCheck = null;
		for (int i = 0; i < listaEltosImprimirTable.getRowCount(); i++) {
			renderCheck = getCheckBoxRendererDataTable(i, true);
			eltosImprimirTableModel.setValueAt(renderCheck.isSelected(), i, 0);
		}
		//Actualizar descripcion impresion
		onChangeCheckSeleccion();
	}
	private void onChangeRadioNoVacios() {
		String value = "";
		CheckBoxRenderer renderCheck = null;
		for (int i = 0; i < listaEltosImprimirTable.getRowCount(); i++) {
			value = (String) eltosImprimirTableModel.getValueAt(i, 1);
			renderCheck = getCheckBoxRendererDataTable(i, (value != null && !value.equals("")));
			eltosImprimirTableModel.setValueAt(renderCheck.isSelected(), i, 0);
		}
		//Actualizar descripcion impresion
		onChangeCheckSeleccion();
	}
	private void onChangeRadioInvertir() {
		CheckBoxRenderer renderCheck = null;
		for (int i = 0; i < listaEltosImprimirTable.getRowCount(); i++) {
			renderCheck = (CheckBoxRenderer) listaEltosImprimirTable.getCellRenderer(i, 0);
			listaEltosImprimirTable.prepareRenderer(renderCheck, i, 0);
			renderCheck = getCheckBoxRendererDataTable(i, !renderCheck.isSelected());
			eltosImprimirTableModel.setValueAt(renderCheck.isSelected(), i, 0);
		}
		//Actualizar descripcion impresion
		onChangeCheckSeleccion();
	}
	
	private JScrollPane getDataTable() {
		eltosImprimirTableModel = new SelectPrintItemsTableModel(); 
		listaEltosImprimirTable = new JTable(eltosImprimirTableModel);
		listaEltosImprimirTable.setPreferredScrollableViewportSize(new Dimension(90, 130));

		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel = new JScrollPane();
		scrollPanel.setPreferredSize(new Dimension(90, 130));
		scrollPanel.setBorder(new LineBorder(listaEltosImprimirTable.getBackground()));
		scrollPanel.setViewportView(listaEltosImprimirTable);

		initDataTable ();
		loaldDataDataTable ();

		return scrollPanel;
	}

	private void loaldDataDataTable()  {
		//Obtenemos configuracion establecida
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		List lstFeatures = ((config != null && config.getCapaIteracion() != null)? config.getCapaIteracion().getFeatureCollectionWrapper().getFeatures() : new ArrayList());
		//Procesar features para obtener valores del attibute seleccionado
		for (int i = 0; i < lstFeatures.size(); i++) 
			addRowDataTable(i, config.getNombreCampoIteracion(), (GeopistaFeature) lstFeatures.get(i));
	}
	
	private void initDataTable () {
		String[] columnNamesOtrosInformes= {"Sel.", "Valor"};
		SelectPrintItemsTableModel.setColumnNames(columnNamesOtrosInformes);

		int j=0;
		TableColumn column = null;

		//Check seleccion
		column = listaEltosImprimirTable.getColumnModel().getColumn(j++);
		column.setMinWidth(30);
		column.setMaxWidth(40);
		column.setWidth(40);
		column.setPreferredWidth(40);
		column.setResizable(true);
		
		JCheckBox check = new JCheckBox();
		check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	onChangeCheckSeleccion ();
            }
        });
		column.setCellEditor(new CheckBoxTableEditor(check));
		column.setCellRenderer(new CheckBoxRenderer());
		
		//Valor
		column = listaEltosImprimirTable.getColumnModel().getColumn(j++);
		column.setMinWidth(50);
		column.setMaxWidth(200);
		column.setWidth(50);
		column.setPreferredWidth(50);
		column.setResizable(true);
	}


	private void addRowDataTable (int row, String attributeSelected, GeopistaFeature datos) {
		try{
			//Obtener check finalizado con datos actualizados
			CheckBoxRenderer renderCheck = getCheckBoxRendererDataTable(row, true);
			//Incluimos elementos en DT
			eltosImprimirTableModel.addRow(new Object[]{ 
							new Boolean(renderCheck.isSelected()), 
							String.valueOf(datos.getAttribute(attributeSelected))
			});
			//actualizamos lista de datos almacenados
			eltosImprimirTableModel.addDatos((Object)datos);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Obtener checkBoxRender para representacion finalizado en DT otros informes
	 * @param row
	 * @param isSelect
	 * @return
	 */
	private CheckBoxRenderer getCheckBoxRendererDataTable (int row, boolean isSelect)  {
		int colCheck = 0;
		//Marcar/Desmarcar check finalizado de la fila correspondiente
		CheckBoxRenderer renderCheck = (CheckBoxRenderer) listaEltosImprimirTable.getCellRenderer(row, colCheck);
		renderCheck.setSelected(isSelect);
		
		return renderCheck;
	}


	private List procesarSeleccionDatos () {
		List lstCuadriculasSeleccion = new ArrayList ();
		CheckBoxRenderer renderCheck = null;
		for (int i = 0; i < listaEltosImprimirTable.getRowCount(); i++) {
			renderCheck = (CheckBoxRenderer) listaEltosImprimirTable.getCellRenderer(i, 0);
			listaEltosImprimirTable.prepareRenderer(renderCheck, i, 0);
			if (renderCheck.isSelected())
				lstCuadriculasSeleccion.add(eltosImprimirTableModel.getDatos(i));
		}
		return lstCuadriculasSeleccion;
	}
	
	
	
	private void onChangeCheckSeleccion() {
		String textLabel = "";
		//Actualizar label con numero de hojas total
		List lstSeleccion = procesarSeleccionDatos ();
		int numHojas = ((lstSeleccion!= null)? lstSeleccion.size() : 0);
		if (numHojas > 0)
			textLabel = UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.generarNHojas", new Object[] {numHojas});
		numHojasLabel.setText(textLabel);
		wizardContext.inputChanged();
	}

	public void enteredFromLeft(Map dataMap) {
		clearDataTable ();
		loaldDataDataTable ();
		onChangeCheckSeleccion ();
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception {
		ConfigPrintPlugin config = (ConfigPrintPlugin) blackboard.get(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN);
		config.setLstCuadriculas(procesarSeleccionDatos ());
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
		return UtilsPrintPlugin.getMessageI18N("SelectItemPrintPanel.Instructions"); 
	}
	public boolean isInputValid() {
		boolean isValid = false;
		List lstSeleccion  = procesarSeleccionDatos ();
		if (lstSeleccion != null && lstSeleccion.size() > 0)
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

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting() {
		// TODO Auto-generated method stub
	}
}
