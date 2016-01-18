/**
 * InfoSigmPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.sigm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.ui.utils.TableColumnAdjuster;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.localgis.client.sigm.SigmClient;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;
import com.vividsolutions.jump.util.Blackboard;

public class InfoSigmPanel extends JPanel implements 
		FeatureExtendedPanel {
	private static final Log logger = LogFactory
			.getLog(InfoSigmPanel.class);


	private AppContext aplicacion;
	private FeatureDialogHome fd;

	JTabbedPane jTabbedPane = null;

	/* constructor de la clase q llama al método que inicializa el panel */
	public InfoSigmPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			logger.error("Error en la llamada al constructor de la clase ", e);
		}
	}

	/* método xa inicializar el panel */
	private void jbInit() throws Exception {
		try{
			aplicacion = (AppContext) AppContext.getApplicationContext();
			
			this.setName("SiGM");
			this.setLayout(new BorderLayout());
			//this.setSize(new Dimension(800, 550));
	
			JPanel jPanelSiGMAttributes = new JPanel();
			jPanelSiGMAttributes.setPreferredSize(new Dimension(400, 400));
			jPanelSiGMAttributes.setBorder(BorderFactory.createTitledBorder("Datos Asociados SiGM"));
					
			SigmClient sigmClient = new SigmClient(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,"", false) + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.SIGM_SERVLET);
			
		    Blackboard Identificadores=aplicacion.getBlackboard();
		    Object [] selectedFeatures=(Object [])Identificadores.get("feature");
		    if(selectedFeatures!=null && selectedFeatures.length>0){
		   
		    	HashMap<String,String> propertyAndNameAsHashMap = sigmClient.getPropertyAndNameAsHashMap(((GeopistaFeature)selectedFeatures[0]).getLayer().getSystemId());
		    	if(propertyAndNameAsHashMap!=null && propertyAndNameAsHashMap.size()>0){
			  
		    		GeopistaFeature geopistaFeature = ((GeopistaFeature)selectedFeatures[0]);		  
		    		GeopistaSchema geopistaSchema = ((GeopistaSchema)geopistaFeature.getSchema());	
		 
		    		PropertyAndValue [] propertyAndValues = sigmClient.getInfoAll(aplicacion.getIdEntidad(), geopistaFeature.getLayer().getSystemId(), ((String)geopistaFeature.getAttribute(geopistaSchema.getAttributeByColumn("id_feature"))));
		    		if(propertyAndValues!= null && propertyAndValues.length>0){
						String [] property = new String [propertyAndValues.length];
						String [][] value = new String [propertyAndValues.length][1];
				
						DefaultTableModel model = new DefaultTableModel() {
						    @Override
						    public boolean isCellEditable(int row, int column) {
						        return false;
						    }
						};
						model.addColumn("Name");
						model.addColumn("Value");
						for(PropertyAndValue propertyAndValue : propertyAndValues){
							model.addRow(new Object []{propertyAndNameAsHashMap.get(propertyAndValue.getProperty()),propertyAndValue.getValue()});
						}
						JTable jTable = new JTable(model);
						jTable.setTableHeader(null);
						JScrollPane scroll = new JScrollPane(jTable);	
						(new TableColumnAdjuster(jTable)).adjustColumns();
						//scroll.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
						scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
						
						setWidthAsPercentages(jTable, 0.50, 0.50);
						jTable.setOpaque(false);
						((DefaultTableCellRenderer)jTable.getDefaultRenderer(Object.class)).setOpaque(false);
						scroll.setOpaque(false);
						scroll.getViewport().setOpaque(false);
						
						scroll.setPreferredSize(jPanelSiGMAttributes.getPreferredSize());
				        jPanelSiGMAttributes.add(scroll, BorderLayout.PAGE_START);				
						this.add(jPanelSiGMAttributes, BorderLayout.CENTER);			
					}
					else{
						JLabel errorLabel = new JLabel("Error al recuperar la información");
						errorLabel.setHorizontalAlignment(JLabel.CENTER);
						this.add(errorLabel, BorderLayout.CENTER);	
					} 
		    	}
				else{
					 JLabel errorLabel = new JLabel("Error al recuperar la información");
					 errorLabel.setHorizontalAlignment(JLabel.CENTER);
					 this.add(errorLabel, BorderLayout.CENTER);	
				}
		    }
		    else{
		    	JLabel errorLabel = new JLabel("Error al recuperar la información");
				errorLabel.setHorizontalAlignment(JLabel.CENTER);
				this.add(errorLabel, BorderLayout.CENTER);	
			} 	
		}
	    catch(Exception ex){
	    	logger.error("localgis-plugin-sigm - InfoSigmPanel: " + ex);
	    }
		//this.add(jPanelButtons, BorderLayout.SOUTH);
	}
	
	private static void setWidthAsPercentages(JTable table,
	        double... percentages) {
	    final double factor = 10000;
	 
	    TableColumnModel model = table.getColumnModel();
	    for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
	        TableColumn column = model.getColumn(columnIndex);
	        column.setPreferredWidth((int) (percentages[columnIndex] * factor));
	    }
	}
	
	public void setFd(FeatureDialogHome fd) {
		this.fd = fd;
	}
	
	/*
	 * método xa abrir el directorio dd queremos dejar el documento que estamos
	 * abriendo xa visualizar
	 */
	private String seleccionaFichero(String filename) throws Exception {
		File f = new File(filename);
		/* Dialogo para seleccionar donde dejar el fichero */
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setSelectedFile(f);
		if (chooser.showSaveDialog(aplicacion.getMainFrame()) != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = chooser.getSelectedFile();
		if (selectedFile == null)
			return null;
		String tmpDir = "";
		String tmpFile = selectedFile.getAbsolutePath();
		if (tmpFile.lastIndexOf(selectedFile.getName()) != -1) {
			tmpDir = tmpFile.substring(0,
					tmpFile.lastIndexOf(selectedFile.getName()));
		}
		/** Comprobamos si existe el directorio. */
		try {
			File dir = new File(tmpDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception ex) {
			logger.error("Error al seleccionar un fichero ", ex);
		}
		return selectedFile.getAbsolutePath();
	}

	/* Cuando nos movemos x la ventana de atributos */
	public void enter() {
		// De momento va vacío
	}

	/* De momento no se usa */
	public void exit() {
		// De momento va vacío
	}
	
//	private void getSelectedFeatures(){
//	 List capasVisibles = context.getWorkbenchContext().getLayerNamePanel()
//             .getLayerManager().getVisibleLayers(true);
//     Iterator capasVisiblesIter = capasVisibles.iterator();
//
//     final LockManager lockManager = (LockManager) context.getActiveTaskComponent()
//             .getLayerViewPanel().getBlackboard().get(LockManager.LOCK_MANAGER_KEY);
//
//     localFeature=null;
//     while (capasVisiblesIter.hasNext())
//     {
//         Layer capaActual = (Layer) capasVisiblesIter.next();
//         Collection featuresSeleccionadas = context.getWorkbenchContext()
//                 .getLayerViewPanel().getSelectionManager()
//                 .getFeaturesWithSelectedItems(capaActual);
//         // Almacenamos en este ArrayList el resultado de la operacion de
//         // bloqueo
//         Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
//         boolean cancelWhile = false;
//         if (capaActual instanceof GeopistaLayer && (((GeopistaLayer)capaActual).isLocal() || ((GeopistaLayer)capaActual).isExtracted()))
//            continue;
//
//         while (featuresSeleccionadasIter.hasNext())
//         {
//             if (cancelWhile == true)//En la feature anterior se cancelo, ahora se pide confirmación
//             {
//                 if (JOptionPane.showConfirmDialog((Component) context.getWorkbenchGuiComponent(),
//                                 aplicacion.getI18nString("GeopistaFeatureSchemaPlugIn.RestoFeatures"),
//                                 aplicacion.getI18nString("GeopistaFeatureSchemaPlugIn.EditarMultiplesEntidades"),
//                                 JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
//                     return false;
//                 else
//                     cancelWhile = false;
//             }
//             localFeature = (Feature) featuresSeleccionadasIter.next();
//             String systemId = ((GeopistaFeature)localFeature).getSystemId();
//                final ArrayList lockResultaArrayList = new ArrayList();
//             // capa de sistema. La feature debe bloquearse
//             boolean bloquear=false;
//             if( capaActual instanceof GeopistaLayer   &&
//                     !((GeopistaLayer)capaActual).isLocal() &&
//                     !((GeopistaLayer)capaActual).isExtracted() &&
//                     capaActual.isEditable()&& systemId!=null &&
//                     !((GeopistaFeature)localFeature).isTempID() &&
//                     !systemId.equals(""))
//             {
//                    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
//                     .getMainFrame(), context.getErrorHandler());
//                    progressDialog.setTitle(aplicacion.getI18nString("LockFeatures"));
//                    progressDialog.addComponentListener(new ComponentAdapter()
//                    {
//                         public void componentShown(ComponentEvent e)
//                         {
//                                new Thread(new Runnable(){
//                                     public void run(){
//                                         try{
//                                             Integer lockID = lockManager.lockFeature(localFeature, progressDialog);
//                                             lockResultaArrayList.add(lockID);
//                                         } catch (Exception e){}
//                                         finally{
//                                             progressDialog.setVisible(false);
//                                         }
//                                     }
//                             }).start();
//                     }});
//                     GUIUtil.centreOnWindow(progressDialog);
//                     progressDialog.setVisible(true);
//             }else
//                 bloquear=true;
//
//
//             Vector feature=new Vector();
//             feature.add(localFeature);
//    }
	

}
