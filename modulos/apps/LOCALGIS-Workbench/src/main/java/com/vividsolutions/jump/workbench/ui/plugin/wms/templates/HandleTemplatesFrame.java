/**
 * HandleTemplatesFrame.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.wms.templates;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.WMSTemplate;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.wms.LayerStylesWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.LayersStylesPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.MapLayerWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.OneSRSWizardPanel;
import com.vividsolutions.jump.workbench.ui.plugin.wms.SRSWizardPanel;
import com.vividsolutions.wms.MapLayer;
import com.vividsolutions.wms.WMService;


/**Panel de gestión de plantillas WMS.
 * @author Silvia García
 *  *
 */
public class HandleTemplatesFrame extends JFrame{
	private static String cachedURL = "http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?";
    private static String lastWMSVersion = WMService.WMS_1_1_1;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private AdministradorCartografiaClient client;
		
	//panel de plantillas
	private JPanel jpWMSTemplates;
	
	//tabla de plantillas
	private JScrollPane jspWMSTemplates;
	private JTable jtWMSTemplates;
	private LinkedList wmsTemplates;


	//botones
	private JPanel jpButtons;
	private JButton jbAdd;
	private JButton jbDelete;
	private JButton jbEdit;
	private JButton jbExit;
	
	
	private EditWMSTemplatePanel editPane;
	private com.geopista.ui.wizard.WizardDialog d;
	

/**Constructor
 * @param wmsTemplates Lista de plantillas WMS
 */
public HandleTemplatesFrame(LinkedList wmsTemplates){
	 String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
      client = new AdministradorCartografiaClient(
             sUrlPrefix + "AdministradorCartografiaServlet");
	this.wmsTemplates=wmsTemplates;
	try{
		jbInit();
	}catch(Exception e){
		System.out.println("Se ha producido un error en la inicialización del panel de gestión de plantillas WMS");
	}
}//fin constructor


/**Inicializa los componentes del panel
 */
private void jbInit ()throws Exception{
	this.setTitle(GeopistaFunctionUtils.i18n_getname("HandleWMSTemplatesPlugin.name"));
	//panel de plantillas
	jpWMSTemplates=new JPanel();
	jpWMSTemplates.setBorder(new javax.swing.border.TitledBorder(GeopistaFunctionUtils.i18n_getname("HandleTemplatesPanel.jpWMSTemplates.title")));
	add(jpWMSTemplates, BorderLayout.CENTER);
	
	//tabla de plantillas
	TableModel model=new TableModel();
	model.setModelData(wmsTemplates);
	jtWMSTemplates=new JTable(model);

	jtWMSTemplates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	jspWMSTemplates=new JScrollPane(jtWMSTemplates);
	jpWMSTemplates.add(jspWMSTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 350, 470));
	 ListSelectionModel rowSM = jtWMSTemplates.getSelectionModel();
     rowSM.addListSelectionListener(new ListSelectionListener() 
    	 {
             public void valueChanged(ListSelectionEvent e) {
            	 jtWMSTemplate_ListSelectionListener(e);
             }
         });
	
	//botones
	jpButtons=new JPanel();
	add(jpButtons,BorderLayout.SOUTH);
	GridLayout gl=new GridLayout();
	gl.setColumns(3);
	gl.setRows(1);
	jpButtons.setLayout(gl);
	
	//botón Añadir
	jbAdd=new JButton(GeopistaFunctionUtils.i18n_getname("HandleTemplatesPanel.jbAdd"));
	jbAdd.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	jbAddActionPerformed(evt);
        }
    });
	jpButtons.add(jbAdd);
	
	//botón Eliminar
	jbDelete=new JButton(GeopistaFunctionUtils.i18n_getname("HandleTemplatesPanel.jbDelete"));
	jbDelete.setEnabled(false);
	jbDelete.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	jbDeleteActionPerformed(evt);
        }
    });
	jpButtons.add(jbDelete);
	
	//botón Editar
	jbEdit=new JButton(GeopistaFunctionUtils.i18n_getname("HandleTemplatesPanel.jbEdit"));
	jbEdit.setEnabled(false);
	jbEdit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	jbEditActionPerformed(evt);
        }
    });
	jpButtons.add(jbEdit);
	
	//botón salir
	jbExit=new JButton(GeopistaFunctionUtils.i18n_getname("HandleTemplatesPanel.jbExit"));
	jbExit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        	jbExitActionPerformed(evt);
        }
    });
	jpButtons.add(jbExit);
}//fin método jbInit


//Acción realizada cuando se selecciona un elemento de la lista
    private void jtWMSTemplate_ListSelectionListener(ListSelectionEvent e){
    	this.jbDelete.setEnabled(true);
    	this.jbEdit.setEnabled(true);
    }//fin jtWMSTemplate_ListSelectionListener
     
    
    
    //Acción realizada al pulsar el botón Añadir
	private void jbAddActionPerformed(ActionEvent e){
		  d = new com.geopista.ui.wizard.WizardDialog(this,
				  GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.NewWMSTemplate"), null);
	        d.init(new com.geopista.ui.wizard.WizardPanel[] {new URLAndDescriptionWizardPanel(cachedURL, lastWMSVersion), 
	        		new MapLayerWizardPanel(),new LayerStylesWizardPanel(),
	                new SRSWizardPanel(), new OneSRSWizardPanel()
	            });
	        d.setSize(500, 400);
	        GUIUtil.centreOnWindow(d);
	        d.setVisible(true);
	        if (!d.wasFinishPressed()) {
	            return;
	        }
	        //ya hemos creado la plantilla, ahora vamos a almacenarla
	        cachedURL = (String) d.getData(URLAndDescriptionWizardPanel.URL_KEY);
	        lastWMSVersion = (String) d.getData( URLAndDescriptionWizardPanel.VERSION_KEY );
	        String srs=(String) d.getData(SRSWizardPanel.SRS_KEY);
	        List layerNames= toLayerNames((List) d.getData(MapLayerWizardPanel.LAYERS_KEY));
	        String format=((String) d.getData(SRSWizardPanel.FORMAT_KEY));
	        HashMap styles=(HashMap)d.getData(LayersStylesPanel.SELECTED_STYLES);
			String descripcion=(String) d.getData(URLAndDescriptionWizardPanel.DESCRIPTION_KEY);
			if(descripcion==null)
				descripcion="Plantilla sin descripcion";
			else if(descripcion.equals(""))
				descripcion="Plantilla sin descripcion";
			
			WMSTemplate wmsTemplate=new WMSTemplate();
			wmsTemplate.setService("wms");
			wmsTemplate.setUrl(cachedURL);
			wmsTemplate.setVersion(lastWMSVersion);
			wmsTemplate.setSrs(srs);
			wmsTemplate.setLayers(layerNames);
			wmsTemplate.setFormat(format);
			wmsTemplate.setStyles(styles);
			wmsTemplate.setActiva(true);
			wmsTemplate.setVisible(true);
			wmsTemplate.setDescripcion(descripcion);
			try{
			int template_id=client.addWMSTemplate(wmsTemplate);
			if(template_id!=-1){
			wmsTemplate.setId(template_id);
			WMSTemplate row[] = { wmsTemplate };
			((TableModel)jtWMSTemplates.getModel()).addRow(row);
			}else{
				JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.AddError"),
						"Error",JOptionPane.ERROR_MESSAGE);	
			}
			}catch(ACException exception){
				JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.AddError"),
						"Error",JOptionPane.ERROR_MESSAGE);	
			}
			
	}//fin jbAddActionPerformed
	
	
	
	/**Genera una lista de nombres de capas a partir de una lista de capas
	 */
	private List toLayerNames(List mapLayers) {
        ArrayList names = new ArrayList();
        for (Iterator i = mapLayers.iterator(); i.hasNext();) {
            MapLayer layer = (MapLayer) i.next();
            names.add(layer.getName());
        }

        return names;
    }//fin método toLayerNames
	
	
	
	//Acción realizada al pulsar el botón Eliminar
	private void jbDeleteActionPerformed(ActionEvent e){
		int row=this.jtWMSTemplates.getSelectedRow();
		WMSTemplate template=(WMSTemplate) this.jtWMSTemplates.getModel().getValueAt(row, 0);
		List maps=client.getWMSTemplatesMap(template.getId());
		ListMapsTemplateFrame frame;
		if(maps!=null&&maps.size()>0)
				frame=showMapListFrame((LinkedList)maps,false);//hay mapas relacionados con la plantilla
		
		else{//no hay mapas relacionados con la plantillas
			//BORRADO 
			System.out.println("Realizamos el borrado");
			int answer=JOptionPane.showConfirmDialog(this, GeopistaFunctionUtils.i18n_getname("jopTemplatesNoMap.text.confirm"),
					GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.title"),JOptionPane.INFORMATION_MESSAGE);
			if(answer==0)
				deleteTemplate(template,row);
		}//fin else
	}    
	
	
	
	/**Muestra la lista de mapas asociados a una plantilla
	 */
	private ListMapsTemplateFrame showMapListFrame(LinkedList maps,boolean edition){
		ListMapsTemplateFrame frame=new ListMapsTemplateFrame(maps,edition,this);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setLocation((screenSize.width/2 -
                frame.getWidth()) / 2,
                (screenSize.height/2 - frame.getHeight()) / 2);
        frame.pack();
        frame.setVisible(true);
        return frame;
	}//fin método
	
	
	
	/**Continúa el borrado tras preguntar al usuario si desea seguir con la acción
	 */
	protected void continueDeleteOperationAfterShowMapList(ListMapsTemplateFrame frame){
		if(frame.getUserAnswer_IsYes()){//el usuario continúa con la operación
			//BORRADO
			System.out.println("Realizamos el borrado");
			int row=this.jtWMSTemplates.getSelectedRow();
			WMSTemplate template=(WMSTemplate) this.jtWMSTemplates.getModel().getValueAt(row, 0);
			deleteTemplate(template,row);
		}
	}
	
	
	
	/**Borra una plantilla
	 */
	private void deleteTemplate(WMSTemplate template, int row){
		try{	
			client.deleteWMSTemplate(template.getId());
			((TableModel)jtWMSTemplates.getModel()).removeRow(row);
			jtWMSTemplates.repaint();
			if(((TableModel)jtWMSTemplates.getModel()).getRowCount()==0){
				jbDelete.setEnabled(false);
				jbEdit.setEnabled(false);
			}
			}catch(ACException exception){
				JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.deleteError"),
						"Error",JOptionPane.ERROR_MESSAGE);
			}
	}//fin deleteTemplate
	
	
    
	
	
	//Acción realizada al pulsar el botón Editar
	private void jbEditActionPerformed(ActionEvent e){
		int row=this.jtWMSTemplates.getSelectedRow();
		WMSTemplate selectedTemplate=(WMSTemplate) this.jtWMSTemplates.getModel().getValueAt(row, 0);
		int template_id=selectedTemplate.getId();
		try {
			//obtenemos la plantilla que queremos editar
			WMSTemplate templateToBeModified = client.getWMSTemplate(template_id);
			WMService service = new WMService(templateToBeModified.getUrl(),
					templateToBeModified.getVersion());
			service.initialize();
			
			//Mostramos el diálogo de edición de la plantilla
			MultiInputDialog dialog = new MultiInputDialog((this),StringUtil.toFriendlyName(GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editDialog.title")), true);
			dialog.setInset(0);
			dialog.setSideBarImage(IconLoader.icon("EditWMSLayer.jpg"));
			dialog.setSideBarDescription(GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editDialog.description"));
			editPane = new EditWMSTemplatePanel(service,
					templateToBeModified.getLayers(), templateToBeModified.getSrs(), templateToBeModified.getAlpha(),templateToBeModified.getFormat(),
					templateToBeModified.getStyles(),templateToBeModified.getDescripcion());
		     editPane.setPreferredSize(new Dimension(700, 500));
		     dialog.addRow("Chosen Layers", new JLabel(""), editPane, editPane.getEnableChecks(), null);
		     dialog.setVisible(true);
		     
		     if (dialog.wasOKPressed()) {//El usuario ha pulsado el botón Aceptar
		    	 //Editamos la plantilla
		    	 editTemplate(selectedTemplate,templateToBeModified,editPane,row);
		     }//fin if
			
		} catch (ACException acE) {
			JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editError"),
					"Error",JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioE) {
			JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editIOError"),
					"Error",JOptionPane.ERROR_MESSAGE);
		}
	}//fin jbEditActionPerformed
	
	
	/**Se encarga de editar una plantilla WMS, una vez que el usuario ha realizado los cambios pertinentes sobre la misma
	 * y ha pulsado el botón aceptar
	 */
	private void editTemplate(WMSTemplate selectedTemplate,WMSTemplate templateToBeModified,EditWMSTemplatePanel panel,int row){
		List maps=client.getWMSTemplatesMap(selectedTemplate.getId());
		ListMapsTemplateFrame frame;
		if(maps!=null&&maps.size()>0)
				frame=showMapListFrame((LinkedList)maps,true);//hay mapas relacionados con la plantilla
				
		
		else{//no hay mapas relacionados con la plantillas
			System.out.println("Realizamos las modificaciones sobre la plantilla");
			JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("jopTemplatesNoMap.text"),
					GeopistaFunctionUtils.i18n_getname("ListMapsTemplateFrame.title"),JOptionPane.INFORMATION_MESSAGE);
			//Creamos la nueva capa con los atributos asignados por el usuario
			templateToBeModified.setId(selectedTemplate.getId());
			//descripción
			String descripcion=panel.getDescription();
			if(descripcion==null)
				descripcion="Plantilla sin descripcion";
			else if(descripcion.equals(""))
				descripcion="Plantilla sin descripcion";
			templateToBeModified.setDescripcion(descripcion);
			//capas
			templateToBeModified.removeAllLayers();
			 for (Iterator i = panel.getChosenMapLayers().iterator();
             	i.hasNext();) {
				 MapLayer mapLayer = (MapLayer) i.next();
				 templateToBeModified.addLayer(mapLayer.getName());
			 }//fin for
			 templateToBeModified.setSrs(panel.getEPSG());
			 templateToBeModified.setAlpha(panel.getAlpha());
			 templateToBeModified.setFormat(panel.getFormat());
			 panel.updateStyles();
			try{
			client.updateWMSTemplate(templateToBeModified);
			//actualizamos el nombre de la plantilla en la tabla
			selectedTemplate.setDescripcion(descripcion);
			jtWMSTemplates.repaint();
			JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editOK"),
					GeopistaFunctionUtils.i18n_getname("HandleWMSTemplatesPlugin.name"),JOptionPane.INFORMATION_MESSAGE);	
			}catch(ACException acE){
				JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editError1"),
						"Error",JOptionPane.ERROR_MESSAGE);	
			}
		}//fin else
	}//fin método editTemplate
	
	
	/**Continúa el borrado tras preguntar al usuario si desea seguir con la acción
	 */
	protected void continueEditOperationAfterShowMapList(ListMapsTemplateFrame frame){
		if(frame.getUserAnswer_IsYes()){//el usuario continúa con la operación
			System.out.println("Realizamos las modificaciones sobre la plantilla");
			//Creamos la nueva capa con los atributos asignados por el usuario
			WMSTemplate newTemplate=new WMSTemplate();
			int row=this.jtWMSTemplates.getSelectedRow();
			WMSTemplate selected_template=(WMSTemplate) this.jtWMSTemplates.getModel().getValueAt(row, 0);
			newTemplate.setId(selected_template.getId());
			//descripción
			String descripcion=editPane.getDescription();
			if(descripcion==null)
				descripcion="Plantilla sin descripcion";
			else if(descripcion.equals(""))
				descripcion="Plantilla sin descripcion";
			newTemplate.setDescripcion(descripcion);
			//capas
			 for (Iterator i = editPane.getChosenMapLayers().iterator();
             	i.hasNext();) {
				 MapLayer mapLayer = (MapLayer) i.next();
				 newTemplate.addLayer(mapLayer.getName());
			 }//fin for
			 newTemplate.setSrs(editPane.getEPSG());
			 newTemplate.setAlpha(editPane.getAlpha());
			 newTemplate.setFormat(editPane.getFormat());
			 editPane.updateStyles();
			 newTemplate.setStyles((HashMap) editPane.getChoosenStyles());
			try{
			client.updateWMSTemplate(newTemplate);
			JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editOK"),
					GeopistaFunctionUtils.i18n_getname("HandleWMSTemplatesPlugin.name"),JOptionPane.INFORMATION_MESSAGE);
			//actualizamos el nombre de la plantilla en la tabla
			WMSTemplate selectedTemplate=(WMSTemplate) this.jtWMSTemplates.getModel().getValueAt(row, 0);
			selectedTemplate.setDescripcion(descripcion);
			jtWMSTemplates.repaint();
			}catch(ACException acE){
				JOptionPane.showMessageDialog(this, GeopistaFunctionUtils.i18n_getname("HandleTemplatesFrame.editError1"),
						"Error",JOptionPane.ERROR_MESSAGE);	
			}
		}
	}
	
	
	
	
     
//Acción realizada al pulsar el botón salir
	private void jbExitActionPerformed(ActionEvent e){
		dispose();
	}
	
	
	
	
	private class TableModel extends DefaultTableModel {
		String descripcion = GeopistaFunctionUtils
				.i18n_getname("HandleTemplatesPanel.descripcion");
		private String[] columnNames = { descripcion };

		public void setModelData(LinkedList wmsTemplates) {
			for (int i = 0; i < wmsTemplates.size(); i++) {
				WMSTemplate template = (WMSTemplate) wmsTemplates.get(i);
				WMSTemplate row[] = { template };
				this.addRow(row);
			}
			fireTableDataChanged();
		}
		public int getColumnCount() {
			return 1;
		}
		public String getColumnName(int col) {
			return columnNames[col];
		}
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}//fin clase TableModel
	
	
	
	
	
	
		
	
}//fin clase HandleTemplatesPanel
