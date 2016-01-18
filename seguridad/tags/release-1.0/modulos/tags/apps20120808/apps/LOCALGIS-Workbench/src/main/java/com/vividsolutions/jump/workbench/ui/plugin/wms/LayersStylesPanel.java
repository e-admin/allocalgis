package com.vividsolutions.jump.workbench.ui.plugin.wms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import com.geopista.ui.wms.Style;
import com.vividsolutions.wms.MapLayer;
import com.vividsolutions.wms.WMService;

public class LayersStylesPanel extends JPanel {
	private List choosenLayers;
	private JTable jtable;
	private JScrollPane jspLayerStyles;
	private WMService service;
	private HashMap layersStyles;
	private final static int LAYERS_COLUMN=0;
	private final static int STYLES_COLUMN=1;
	 private GridBagLayout gridBagLayout = new GridBagLayout();

	
	/**En esta map se inserta otra map con los estilos seleccionados por el usuario para cada capa*/
	private Map dataMap;
	/**Tabla hash en la que se almacena para cada capa el estilos seleccionado por el usuario*/
	private HashMap selectedStyles;
	/**Cte para acceder a la tabla hash que tiene los estilos seleccionados por el usuario*/
	public final static String SELECTED_STYLES="selectedStyles";	
	final String[] columnNames = {"Capa", 
     "Estilos"};
	final Object[][] dataTable;
	private JLabel jlabelNoStyles;
	
	
	
	
	
	
	 public LayersStylesPanel(WMService service,Map dataMap){
		 this.service=service;
		 this.dataMap=dataMap;
		 selectedStyles=new HashMap();
		 this.choosenLayers=(List) dataMap.get(MapLayerWizardPanel.LAYERS_KEY);
		 dataTable=new Object[choosenLayers.size()][2];
		 layersStyles=new HashMap();
		try {
			jbInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }//fin del constructor
	 
	
	 
	public void jbInit()throws Exception{
		this.setLayout(gridBagLayout);
		int i=0;
	
		this.layersStyles=service.getCapabilities().getStyles();
		 Iterator it=choosenLayers.iterator();
			while(it.hasNext()){
			MapLayer mapLayer=(MapLayer)it.next();
			dataTable[i][LAYERS_COLUMN]=mapLayer.getName();
			
			
			ArrayList styles=(ArrayList) layersStyles.get(mapLayer.getName());
        	Iterator it1=styles.iterator();
			while(it1.hasNext()){
				dataTable[i][STYLES_COLUMN]=((Style)it1.next()).getName();
				break;
			}
			i++;
			}//fin del while
		
		jtable=new JTable(dataTable,columnNames);
		jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumn col = jtable.getColumnModel().getColumn(STYLES_COLUMN);
		 col.setCellEditor(new JComboBoxDefaultCellEditor(new JComboBox()));
		jspLayerStyles=new JScrollPane(jtable);
		jspLayerStyles.setPreferredSize(new Dimension(300,300));
		//this.add(jspLayerStyles,BorderLayout.CENTER);
		 this.add(jspLayerStyles,
		            new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
		                new Insets(0, 0, 0, 0), 0, 0));
		
		    
	}//fin jbInit
	
	
	
	
	/**Guarda en una tabla hash los estilos seleccionados por el usuario*/
	 public void saveSelectedValues(){
		
		 for (int i=0;i<jtable.getRowCount();i++){
		 String layerName=(String) jtable.getModel().getValueAt(i,LAYERS_COLUMN);
		 String styleName=(String) jtable.getModel().getValueAt(i,STYLES_COLUMN);
		 selectedStyles.put(layerName,styleName);
		 }//fin del for
		 dataMap.put(SELECTED_STYLES, selectedStyles);
		 
	 }//fin saveSelectedValues
	
	

	
	
	 private class JComboBoxDefaultCellEditor extends DefaultCellEditor{
		 private JComboBox jcb;

		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return jcb.getSelectedItem();
		}

				 
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			
			jcb.removeAllItems();
			String layerName=(String) table.getValueAt(row, LAYERS_COLUMN);
        	ArrayList styles=(ArrayList) layersStyles.get(layerName);
        	Iterator it=styles.iterator();
			while(it.hasNext()){
			Style s=(Style)it.next();
			jcb.addItem(s.getName());
			}
			return jcb;
			
		}

		public void cancelCellEditing() {
			// TODO Auto-generated method stub
			super.cancelCellEditing();
		}

		

		public Component getComponent() {
			// TODO Auto-generated method stub
			return super.getComponent();
		}

	

		public JComboBoxDefaultCellEditor(JComboBox jcb) {
			super(jcb);
			this.jcb=jcb;
			// TODO Auto-generated constructor stub
		}
			
	 }//fin de la clase JComboBoxDefaultCellEditor
	 
	 
	 
	
	

	
	
	
}//fin clase
