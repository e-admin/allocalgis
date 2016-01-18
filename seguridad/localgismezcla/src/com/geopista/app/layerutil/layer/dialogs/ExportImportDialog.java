package com.geopista.app.layerutil.layer.dialogs;

import java.awt.Checkbox;
import java.awt.Font;
import javax.swing.JDialog;

import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;
import com.geopista.app.layerutil.layer.controls.CheckBoxList;
import com.geopista.app.layerutil.layer.exportimport.utils.ExportImportUtils;
import com.geopista.feature.Table;
import com.vividsolutions.jump.I18N;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ExportImportDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel layersPanel;
	private JPanel dataExportImportPanel;
	private JPanel importConditionsPanel;
	private JPanel selectorLayersPanel;	
	private JPanel buttonPanel;	
	private JScrollPane layerScrollPane;
	private CheckBoxList layerCheckBoxList;
	private Checkbox checkSelectAll;	
	private Checkbox checkUseCoincidentTable;
	private Checkbox checkUseCoincidentLayers;
	private Checkbox checkExportImportTables;
	private Checkbox checkExportImportDomains;
	private Checkbox checkExportImportLayer;
	private Checkbox checkExportImportLayerFamilies;
	private JButton acceptButton;
	private JButton cancelButton;		
	
	private boolean result;	
	private int mode = 0;		
	private String locale;
//	private String currentName;
	private JComboBox cmbLayer;
	private File [] files;
	private DefaultListModel listModel;
	
	public static final int MODE_EXPORT = 1;
	public static final int MODE_IMPORT = 2;
	private Checkbox checkUseCoincidentLayerFamilies;
	
	public ExportImportDialog() {
		this.mode = MODE_EXPORT;
		initComponents();
	}
	
	public ExportImportDialog(int mode) {
		this.mode = mode;
		initComponents();
	}
	
	//BORRAR
//	public ExportImportDialog(int mode, String currentName) {
//		this.mode = mode;
//		this.currentName = currentName;
//		initComponents();
//	}
	
	public ExportImportDialog(int mode, String locale, JComboBox cmbLayer) {
		this.mode = mode;
		this.locale = locale;
		this.cmbLayer = cmbLayer;
		initComponents();
	}
	
	public ExportImportDialog(int mode, File [] files) {
		this.mode = mode;
		this.files = files;
		initComponents();
	}
	
	private void initComponents() {	
		setSize(421, 356);
		getContentPane().setLayout(null);			
		this.setDefaultCloseOperation(ExportImportDialog.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.getContentPane().setPreferredSize(new Dimension(getSize()));
		setResizable(false);		
		setModal(true);	
		this.pack();
		
		initializeMode(mode);

		getContentPane().add(getLayersPanel());		
		getContentPane().add(getSelectorLayersPanel());
		getContentPane().add(getDataExportImportPanel());		
		getContentPane().add(getButtonPanel());
	}

	private void initializeMode(int mode){
		if(mode==MODE_EXPORT){
			this.setTitle(I18N.get("GestorCapas","layerutil.exportimport.title.exportar"));
//			if(currentName!=null) 
//				this.setTitle(this.getTitle() + " \"" + currentName + "\"");
			getAcceptButton().setText(I18N.get("GestorCapas","layerutil.exportimport.button.exportar"));
			getDataExportImportPanel().setBorder(new TitledBorder(null, I18N.get("GestorCapas","layerutil.exportimport.button.exportar"), TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));			
		}
		else if(mode==MODE_IMPORT){
			this.setTitle(I18N.get("GestorCapas","layerutil.exportimport.title.importar"));
			getAcceptButton().setText(I18N.get("GestorCapas","layerutil.exportimport.button.importar"));
			getDataExportImportPanel().setBorder(new TitledBorder(null, I18N.get("GestorCapas","layerutil.exportimport.button.importar"), TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
			getContentPane().add(getImportConditionsPanel());
		}
	}
	
	private void acceptButtonClick(){
		buttonResult(true);
	}
	
	private void cancelButtonClick(){
		buttonResult(false);		
	}
	
	private void buttonResult(boolean result){
		this.result = result;
		this.setVisible(false);
	}
		
	public boolean showDialog(){
		this.setVisible(true);
		return result;
	}	
	
	public boolean isCheckExportImportTablesChecked(){
		return checkExportImportTables.getState();
	}
	
	public boolean isCheckExportImportDomainsChecked(){
		return checkExportImportDomains.getState();
	}
	
	public boolean isCheckExportImportLayerChecked(){	
		return checkExportImportLayer.getState();
	}
	
	public boolean isCheckExportImportLayerFamiliesChecked(){
		return checkExportImportLayerFamilies.getState();
	}
	
	public boolean isCheckUseCoincidentTableChecked(){
		return checkUseCoincidentTable.getState();
	}
	
	public boolean isCheckUseCoincidentLayersChecked(){
		return checkUseCoincidentLayers.getState();
	}	
	
	public boolean isCheckCoincidentUseLayerFamiliesChecked(){
		return checkUseCoincidentLayerFamilies.getState();
	}	
	
	public boolean getResult(){
		return result;
	}	
	
	public List getSelectedLayerIndexes(){
		return getLayerCheckBoxList().getCheckedIdexes();
	}	
	
	public int getNumSelectedLayer(){
		return getLayerCheckBoxList().getNumChecked();
	}		
	
	private void acceptButtonActive(){
		getAcceptButton().setEnabled(((getNumSelectedLayer()>0) && (isCheckExportImportTablesChecked() || isCheckExportImportDomainsChecked() || isCheckExportImportLayerChecked() || isCheckExportImportLayerFamiliesChecked())));
	}

	
	/*
	 * Definición de Componentes
	 */
		
	private JPanel getLayersPanel() {
		if (layersPanel == null) {
			layersPanel = new JPanel();		
			layersPanel.setBounds(21, 15, 216, 283);
			layersPanel.setLayout(null);
			layersPanel.add(getLayerScrollPane());
		}
		return layersPanel;
	}	
	
	private JPanel getSelectorLayersPanel() {
		if (selectorLayersPanel == null) {
			selectorLayersPanel = new JPanel();
			selectorLayersPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
			selectorLayersPanel.setLayout(null);
			selectorLayersPanel.setBounds(250, 15, 150, 42);
			selectorLayersPanel.add(getCheckSelectAll());
		}
		return selectorLayersPanel;
	}
	
	private JPanel getDataExportImportPanel() {
		if (dataExportImportPanel == null) {
			dataExportImportPanel = new JPanel();			
			dataExportImportPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			dataExportImportPanel.setBounds(250, 68, 150, 121);	
			dataExportImportPanel.setLayout(null);
			dataExportImportPanel.add(getCheckExportImportTables());
			dataExportImportPanel.add(getCheckExportImportDomains());
			dataExportImportPanel.add(getCheckExportImportLayer());
			dataExportImportPanel.add(getCheckExportImportLayerFamilies());	
		}
		return dataExportImportPanel;
	}
	
	private JPanel getImportConditionsPanel() {
		if (importConditionsPanel == null) {
			importConditionsPanel = new JPanel();
			importConditionsPanel.setBorder(new TitledBorder(null, I18N.get("GestorCapas","layerutil.exportimport.title.usarcoincidentes"), TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
			importConditionsPanel.setBounds(250, 199, 150, 100);
			importConditionsPanel.setLayout(null);
			importConditionsPanel.add(getCheckUseCoincidentTable());
			importConditionsPanel.add(getCheckUseCoincidentLayers());
			importConditionsPanel.add(getCheckUseCoincidentLayerFamilies());
		}
		return importConditionsPanel;
	}
		
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();			
			buttonPanel.setBounds(0, 316, 425, 33);					
			buttonPanel.add(getAcceptButton());				
			buttonPanel.add(getCancelButton());
		}
		return buttonPanel;
	}
	
	private CheckBoxList getLayerCheckBoxList() {
		if(layerCheckBoxList==null){									
	        layerCheckBoxList = new CheckBoxList(getListModel(false));
	        layerCheckBoxList.setLayoutOrientation(JList.VERTICAL);
	        layerCheckBoxList.setValueIsAdjusting(true);
	        layerCheckBoxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        layerCheckBoxList.setBackground(Color.WHITE);
	        layerCheckBoxList.setBounds(0, 0, 216, 283);
	        layerCheckBoxList.setBorder(null);
	        layerCheckBoxList.ensureIndexIsVisible(layerCheckBoxList.getSelectedIndex());	
	        layerCheckBoxList.addMouseListener(new MouseAdapter() {
			      @Override
			      public void mousePressed(MouseEvent e) {
			    	  acceptButtonActive();
				  }
	        });    
		}
		return layerCheckBoxList;
	}
	
	private DefaultListModel getListModel(boolean selected){
		listModel = new DefaultListModel();		
		if(mode==MODE_EXPORT){
			for (int i = 1; i < cmbLayer.getItemCount(); i++) {
				listModel.add(i-1,new JCheckBox((String)((LayerTable)cmbLayer.getItemAt(i)).getHtNombre().get(locale), selected));
			}
			
			//TEMP EIEL
//			if(selected==true){
//				for (int i = 1; i < cmbLayer.getItemCount(); i++) {
//						selected=false;
//						String [] eielArray = {"CA","DE","TCN","TP","RD","AR","TEM","TCL","ED","RS","PV","PR","ID","CU","PJ","LM","MT","CE","TN","SA","ASL","EN","IP","CC","SU","TU","carreteras","CMP","eea","PL","VT","parroquias","edificiosing","CO","TTMM","TTMMpunto","NP","Nucleos","parcelas_eiel","Provincia"};
//						for (int j = 0; j < eielArray.length; j++) {
//							if (((LayerTable)cmbLayer.getItemAt(i)).getLayer().getDescription().equals(eielArray[j]))
//								selected=true;
//						}						
//						listModel.add(i-1,new JCheckBox((String)((LayerTable)cmbLayer.getItemAt(i)).getHtNombre().get(locale), selected));									
//				}		
//				acceptButtonActive();
//			}
//			else{
//				for (int i = 1; i < cmbLayer.getItemCount(); i++) {
//					listModel.add(i-1,new JCheckBox((String)((LayerTable)cmbLayer.getItemAt(i)).getHtNombre().get(locale), selected));
//				}
//			}
		}
		else if(mode==MODE_IMPORT){
			for (int i = 0; i < files.length; i++) {
			   	listModel.add(i,new JCheckBox(files[i].getName(), selected));
			}
		}
		return listModel;
	}
	
	private JScrollPane getLayerScrollPane(){
		if(layerScrollPane==null){
			layerScrollPane = new JScrollPane(getLayerCheckBoxList());
			layerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			layerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			layerScrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, new Color(64, 64, 64), new Color(64, 64, 64), null, null));
			layerScrollPane.setBorder(null);
			layerScrollPane.setLocation(0, 0);
			layerScrollPane.setSize(216, 283);
		}
		return layerScrollPane;
	}
	
	private Checkbox getCheckSelectAll() {
		if (checkSelectAll == null) {
			checkSelectAll = new Checkbox();			
			checkSelectAll.setState(false);
			checkSelectAll.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.seleccionartodo"));
			checkSelectAll.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkSelectAll.setBounds(15, 10, 117, 22);			
			checkSelectAll.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED)
						getLayerCheckBoxList().setModel(getListModel(true));	
					else
						getLayerCheckBoxList().setModel(getListModel(false));
					acceptButtonActive();
				}
			});
		}
		return checkSelectAll;
	}
	
	private Checkbox getCheckExportImportTables(){
		if(checkExportImportTables==null){
			checkExportImportTables = new Checkbox();
			checkExportImportTables.setState(true);
			checkExportImportTables.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkExportImportTables.setBounds(15, 25, 112, 22);
			checkExportImportTables.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.tablas"));
			checkExportImportTables.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
			    	  acceptButtonActive();
				  }
	        });   
		}
		return checkExportImportTables;
	}
	
	private Checkbox getCheckExportImportDomains(){
		if(checkExportImportDomains==null){
			checkExportImportDomains = new Checkbox();
			checkExportImportDomains.setState(true);
			checkExportImportDomains.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkExportImportDomains.setBounds(15, 45, 112, 22);
			checkExportImportDomains.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.dominios"));
			checkExportImportDomains.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
			    	  acceptButtonActive();
				  }
	        }); 
		}
		return checkExportImportDomains;
	}
	
	private Checkbox getCheckExportImportLayer(){
		if(checkExportImportLayer==null){
			checkExportImportLayer = new Checkbox();
			checkExportImportLayer.setState(true);
			checkExportImportLayer.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkExportImportLayer.setBounds(15, 65, 112, 22);
			checkExportImportLayer.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.capa"));
			checkExportImportLayer.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
			    	  acceptButtonActive();
				  }
	        }); 
		}
		return checkExportImportLayer;
	}
	
	private Checkbox getCheckExportImportLayerFamilies(){
		if(checkExportImportLayerFamilies==null){
			checkExportImportLayerFamilies = new Checkbox();
			checkExportImportLayerFamilies.setState(true);
			checkExportImportLayerFamilies.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkExportImportLayerFamilies.setBounds(15, 85, 112, 22);
			checkExportImportLayerFamilies.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.familiascapa"));
			checkExportImportLayerFamilies.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
			    	  acceptButtonActive();
				  }
	        }); 
		}
		return checkExportImportLayerFamilies;
	}
		
	private Checkbox getCheckUseCoincidentTable() {
		if (checkUseCoincidentTable == null) {
			checkUseCoincidentTable = new Checkbox();
			checkUseCoincidentTable.setState(true);
			checkUseCoincidentTable.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.tablas"));
			checkUseCoincidentTable.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkUseCoincidentTable.setBounds(15, 25, 112, 22);
		}
		return checkUseCoincidentTable;
	}
	
	private Checkbox getCheckUseCoincidentLayers() {
		if (checkUseCoincidentLayers == null) {
			checkUseCoincidentLayers = new Checkbox();
			checkUseCoincidentLayers.setState(true);
			checkUseCoincidentLayers.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.capa"));
			checkUseCoincidentLayers.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkUseCoincidentLayers.setBounds(15, 45, 112, 22);
		}
		return checkUseCoincidentLayers;
	}
	
	private Checkbox getCheckUseCoincidentLayerFamilies() {
		if (checkUseCoincidentLayerFamilies == null) {
			checkUseCoincidentLayerFamilies = new Checkbox();
			checkUseCoincidentLayerFamilies.setState(true);
			checkUseCoincidentLayerFamilies.setLabel(I18N.get("GestorCapas","layerutil.exportimport.checkbox.familiascapa"));
			checkUseCoincidentLayerFamilies.setFont(new Font("Dialog", Font.PLAIN, 11));
			checkUseCoincidentLayerFamilies.setBounds(15, 65, 112, 22);
		}
		return checkUseCoincidentLayerFamilies;
	}
	
	
	private JButton getAcceptButton(){
		if(acceptButton==null){
			acceptButton = new JButton();
			acceptButton.setEnabled(false);
			acceptButton.setNextFocusableComponent(getCancelButton());  
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acceptButtonClick();
				}
			});
		}
		return acceptButton;
	}
	
	private JButton getCancelButton(){
		if(cancelButton==null){
			cancelButton = new JButton();
			cancelButton.setText(I18N.get("GestorCapas","layerutil.exportimport.button.cancelar"));
			cancelButton.setNextFocusableComponent(getAcceptButton());  
			cancelButton.setSelected(true);	
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancelButtonClick();
				}
			});			
		}
		return cancelButton;
	}

}
