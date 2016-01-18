/**
 * ExportarSHPDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.exportshp.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.model.ExportLayersBean;
import com.geopista.model.ExportLayersFamilyBean;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterNode;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.util.exportshp.panels.ExportarSHPSelectionPanel;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.ShapefileWriter;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;



public class ExportarSHPDialog extends JDialog{
	
	static Logger logger = Logger.getLogger(ExportarSHPDialog.class);
	private ExportarSHPSelectionPanel exportarSHPSelectionPanel = null;  
	private OKCancelPanel _okCancelPanel = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public static final int DIM_X = 710;
    public static final int DIM_Y = 600;
    
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                	            		                    
                if (_okCancelPanel.wasOKPressed()){  
                		
	                final int srid_destino = exportarSHPSelectionPanel.getExportSHP().getExportMap().getSrid(); 
                	if(srid_destino!=0){

                		final ArrayList lstLayers = new ArrayList();
                		final Hashtable hs = new Hashtable();
                		TreePath checkedPaths[] = exportarSHPSelectionPanel.getCheckTreeManager().getSelectionModel().getSelectionPaths(); 
                		
                		if(checkedPaths != null){
                			dispose();
                			
                			for (int i=0; i<checkedPaths.length; i++){
                				
                				if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
                						ExportLayersFamilyBean){
               					
                					ExportLayersFamilyBean layersFamily = ((ExportLayersFamilyBean)((HideableNode)checkedPaths[i].getPath()[1]).getUserObject());
                					logger.info("ExportarSHPDialog - Familia: "+ layersFamily.getNameLayerFamily());
                					Iterator iterador = layersFamily.getExportSHPLayers().iterator();

            			            while (iterador.hasNext()){
            			            	ExportLayersBean layers = (ExportLayersBean) iterador.next();
            			            	logger.info("ExportarSHPDialog - Capa: "+ layers.getNameLayer());
            			        		lstLayers.add(layers);
            			        	 }
                					
                				}
                				else if (((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject() instanceof
                						ExportLayersBean){
                						ExportLayersBean layers = (ExportLayersBean)((DefaultMutableTreeNode)checkedPaths[i].getLastPathComponent()).getUserObject();
                						logger.info("ExportarSHPDialog - Capa: "+ layers.getNameLayer());
                						lstLayers.add(layers);
                				}
                				else{
                					//estan seleccionados todos
                					Enumeration enumLayersSHP = exportarSHPSelectionPanel.getExportSHP().getExportMap().getHtLayers().elements();
                					while (enumLayersSHP.hasMoreElements()) {
                						ExportLayersFamilyBean layersFamily = (ExportLayersFamilyBean) enumLayersSHP.nextElement();
                			            Iterator iterador = layersFamily.getExportSHPLayers().iterator();
                    					logger.info("ExportarSHPDialog - Todos seleccionados - Familia: "+ layersFamily.getNameLayerFamily());

                			            while (iterador.hasNext()){
                			            	ExportLayersBean layers = (ExportLayersBean) iterador.next();
                			            	logger.info("ExportarSHPDialog - Todos seleccionados - Capa: "+ layers.getNameLayer());
                			        		lstLayers.add(layers);
                			        	 }
                			        
                			        }
                				}
                			}
		
	                		if (exportarSHPSelectionPanel.abrirJFileChooser() == JFileChooser.APPROVE_OPTION){
	                          		
		                		final String path=exportarSHPSelectionPanel.getDirectorySelected();
		                		if(path!=null && !path.equals("")){
		                			exportarSHPSelectionPanel.setVisible(false);
			    					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
			    					progressDialog.setTitle("TaskMonitorDialog.Wait");
			    					progressDialog.addComponentListener(new ComponentAdapter()
			    					{
			    						public void componentShown(ComponentEvent e)
			    						{
			    							new Thread(new Runnable()
			    							{
			    								public void run()
			    								{
			    									
			    									try
			    									{       
			    										dispose();
			    										
			    										
			    										//Actualizamos el valor de la encuesta
			    										
			    										try {
															String añoEncuesta=exportarSHPSelectionPanel.getAñoEncuesta();
															eielClient.actualizarAñoEncuesta(añoEncuesta);
															logger.info("Actualizando año de la encuesta a: "+ añoEncuesta);
														} catch (Exception e) {
														}  
			    										
		    											//Exportar capas seleccionadas
		    											Iterator<ExportLayersBean> iter = lstLayers.iterator();
		    											while (iter.hasNext()){
		    	                			            	ExportLayersBean layers = (ExportLayersBean) iter.next();
		    	                			            	
		    	                			            	//Los shp emisario y emisario_enc se deben de llamar de otra forma
		    	                			            	//Por lo que se cambia simplemente los nombres que ve el usuario.
		    	                			            	
		    	                			        		if(layers.getNameLayer().equals("emisario_enc"))
		    	                			        			progressDialog.report(I18N.get("LocalGISExportSHP", "export.shp.exportando")+" "+"punto_vertido"); 
 		    	                			        		else if (layers.getNameLayer().equals("emisario_enc_m50"))
		    	                			        			progressDialog.report(I18N.get("LocalGISExportSHP", "export.shp.exportando")+" "+"punto_vertido_m50"); 
 		    	                			        		else
				    										progressDialog.report(I18N.get("LocalGISExportSHP", "export.shp.exportando")+" "+layers.getNameLayer());  

		    	                			            	logger.info("ExportarSHPDialog - Exportando Capa: "+ layers.getNameLayer());    	                			                
		    	                			                
		    	                			                Geometry g = null;
		    	                			                FilterNode filter = null;            
		    	                			   	        
		    	                			                final String sUrlPrefix = aplicacion.getString("geopista.conexion.servidor");
             			                
		    	                			                AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
		    	                			   			    	 sUrlPrefix + "/AdministradorCartografiaServlet");
		    	                			   	         	GeopistaMap mapGeopista = new GeopistaMap(); 
		    	                			   	         
		    	                			   	         	mapGeopista.setName(exportarSHPSelectionPanel.getExportSHP().getExportMap().getName());
		    	                			   	         	mapGeopista.setSystemId(exportarSHPSelectionPanel.getExportSHP().getExportMap().getId_map());
		    	                			                
		    	                			                GeopistaLayer localLayer = administradorCartografiaClient.loadLayerExportSHP(mapGeopista, layers.getIdLayer(), UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true), g, filter, true,srid_destino, progressDialog, exportarSHPSelectionPanel.getExportSHP().getEntidad().getId());
		    	                			                
		    	                			                //En todas las capas eliminamos el atributo id_municipio
		    	                			                HashMap<String, String> excludeFields=new HashMap();
		    	                			                excludeFields.put("id_municipio", "id_municipio");
		    	                			                
		    	                			            //tramo_colector.shp y tramo_colector_m50.shp tienen que contener los atributos:
		    	                			                //tipo_colec; sist_trans; estado; titular; gestion
		    	                			                
		    	                			                /*
		    	                			                if(layers.getNameLayer().equals("tramo_colector")){
		    	                			                	excludeFields.put("sist_trans", "sist_trans");
		    	                			                	excludeFields.put("estado", "estado");
		    	                			                	excludeFields.put("titular", "titular");
		    	                			                	excludeFields.put("gestion", "gestion");
		    	                			                }
		    	                			                if(layers.getNameLayer().equals("tramo_colector_m50")){
		    	                			                	excludeFields.put("sist_trans", "sist_trans");
		    	                			                	excludeFields.put("estado", "estado");
		    	                			                	excludeFields.put("titular", "titular");
		    	                			                	excludeFields.put("gestion", "gestion");
		    	                			                }*/
		    	                			                
		    	                			           //tramo_conduccion.shp y tramo_conduccion_m50.shp tienen que contener los atributos: tipo_tcond;
		    	                			                //estado; titular; gestion

		    	                			             /**if(layers.getNameLayer().equals("tramo_conduccion")){
		    	                			                	excludeFields.put("tipo_tcond", "tipo_tcond");
		    	                			                }
		    	                			                if(layers.getNameLayer().equals("tramo_conduccion_m50")){
		    	                			                	excludeFields.put("tipo_tcond", "tipo_tcond");
		    	                			                }*/
		    	                			                
		    	                			              //tramo_emisario.shp y tramo_emisario_m50.shp tienen que contener los atributos: tipo_mat; estado
		    	                			                
		    	                			               /** if(layers.getNameLayer().equals("tramo_emisario")){
		    	                			                	excludeFields.put("tipo_mat", "tipo_mat");
		    	                			                	excludeFields.put("estado", "estado");
		    	                			                }
		    	                			                if(layers.getNameLayer().equals("tramo_emisario_m50")){
		    	                			                	excludeFields.put("tipo_mat", "tipo_mat");
		    	                			                	excludeFields.put("estado", "estado");
		    	                			                }*/
		    	                			                
		    	                			                //excludeFields.put("fase", "fase");
		    	                			                //Adaptamos determinados atributos para que cumplan el modelo del MPT.
		    	                			                //localLayer.getFeatureCollectionWrapper().getFeatureSchema().deleteAttribute("id_municipio");
		    	                			                
		    	                			                DriverProperties dp = new DriverProperties();
		    	                			                ShapefileWriter shapeWriter = new ShapefileWriter();

		    	                			        		
		    	                			        		if(layers.getNameLayer().equals("emisario_enc"))
		    	                			        			 dp.set("File", path+File.separator+"punto_vertido"+".shp");
 		    	                			        		else if (layers.getNameLayer().equals("emisario_enc_m50"))
		    	                			        			 dp.set("File", path+File.separator+"punto_vertido_m50"+".shp");
 		    	                			        		else
		    	                			                dp.set("File", path+File.separator+layers.getNameLayer()+".shp");
		    	                			                shapeWriter.write(localLayer.getFeatureCollectionWrapper(), dp,excludeFields,8);
			    										}
  										
		    											JOptionPane.showMessageDialog(
																ExportarSHPDialog.this,
								                                "Exportación realizada correctamente.",null,JOptionPane.INFORMATION_MESSAGE);
			    									}
			    									catch(Exception e)
			    									{
			    										logger.error("Error ", e);
			    										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
			    										return;
			    									}
			    									finally
			    									{
			    										logger.info((new StringBuilder("Cerramos  Exportar SHP--")).append((new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date(System.currentTimeMillis()))).toString());
			    										progressDialog.setVisible(false);                                
			    										progressDialog.dispose();								
			    										dispose();
			    									}
			    								}
			    							}).start();
			    						}
			    					});
			    					GUIUtil.centreOnWindow(progressDialog);
			    					progressDialog.setResizable(false);
			    					progressDialog.setVisible(true);
			
			    					show();
			                	}else{
			                		JOptionPane.showMessageDialog(
			                				ExportarSHPDialog.this,
			                                I18N.get("LocalGISExportSHP", "export.shp.directorio.incorrecto"),
			                                null,
			                                JOptionPane.INFORMATION_MESSAGE);
			                	
			                	}
	                		}
                		}
                		else{
                			JOptionPane.showMessageDialog(
                					ExportarSHPDialog.this,
	                                I18N.get("LocalGISExportSHP", "export.shp.seleccione.capas"),
	                                null,
	                                JOptionPane.INFORMATION_MESSAGE);
                		}
                	}
            		else{
            			JOptionPane.showMessageDialog(
            					ExportarSHPDialog.this,I18N.get("LocalGISExportSHP", "export.shp.seleccione.sistemacoordenadas"),
                                null, JOptionPane.INFORMATION_MESSAGE);
            		}                		
            	}
            	else{
            		dispose();
            	}	
                	
            }
        });        	
            
    }//ok
        return _okCancelPanel;
} 
    
/* CONSTRUCTORES */    
          
	private LocalGISEIELClient eielClient;
	
    public ExportarSHPDialog(){
        	 this(UserPreferenceConstants.DEFAULT_DATA_PATH);
    }
    
    public ExportarSHPDialog(String rutaFichero){
    	 super(AppContext.getApplicationContext().getMainFrame());
    	 initialize(I18N.get("LocalGISExportSHP", "export.shp.title"));         
        this.setLocationRelativeTo(null);
        

        String _urlEiel = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.EIEL_WEBAPP_NAME;
		String _urlEielServlet = _urlEiel + ServletConstants.EIEL_SERVLET_NAME;
		eielClient= new LocalGISEIELClient(_urlEielServlet);

    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title){
    	
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.util.exportshp.language.LocalGISExportSHPi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISExportSHP",bundle);
        this.setModal(false);
        this.setSize(DIM_X, DIM_Y);
        this.setMinimumSize(new Dimension(DIM_X, DIM_Y));

        this.setResizable(true);
        this.setLayout(new GridBagLayout());
        this.add(getExportarSHPPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.add(getOkCancelPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0001, 0.0001,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(title);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        ExportarSHPDialog dialog = new ExportarSHPDialog();
        dialog.setVisible(true);
    }
    
    public ExportarSHPSelectionPanel getExportarSHPPanel() {
    	if (exportarSHPSelectionPanel == null){
    		exportarSHPSelectionPanel = new ExportarSHPSelectionPanel(new GridBagLayout());
    	}
		return exportarSHPSelectionPanel;
	}


	public void setExportarSHPPanel(
			ExportarSHPSelectionPanel exportarSHPSelectionPanel) {
		this.exportarSHPSelectionPanel = exportarSHPSelectionPanel;
	}
  
}
