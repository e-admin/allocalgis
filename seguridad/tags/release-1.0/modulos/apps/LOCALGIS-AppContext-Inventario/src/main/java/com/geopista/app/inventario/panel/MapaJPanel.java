package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;
import javax.swing.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.geom.NoninvertibleTransformException;

import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.model.IGeopistaLayer;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.editor.GeopistaEditor;
import com.geopista.app.AppContext;
//import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp_LCGIII;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.app.inventario.InventarioInternalFrame;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.NoIDException;

import com.geopista.util.ApplicationContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.io.DriverProperties;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 28-ago-2006
 * Time: 14:09:32
 * To change this template use File | Settings | File Templates.
 */

public class MapaJPanel  extends javax.swing.JPanel {
    Logger logger= Logger.getLogger(MapaJPanel.class);

    private javax.swing.JFrame desktop;
    private GeopistaEditor geopistaEditor;

    private ArrayList actionListeners= new ArrayList();
    private ApplicationContext aplicacion;

    private boolean doAction=false;

	private InventarioInternalFrame inventarioInternalFrame;

    /** Creates new form MapaJPanel 
     * @param inventarioInternalFrame */
    public MapaJPanel(javax.swing.JFrame desktop, InventarioInternalFrame inventarioInternalFrame) {
        this.desktop= desktop;
        this.inventarioInternalFrame=inventarioInternalFrame;
        aplicacion= (AppContext)AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.GridLayout(1, 0));
        geopistaEditor= new GeopistaEditor("workbench-properties-inventario-simple.xml");
        //Para que se vea el panel de selección de capas pero minimizado al principio
        geopistaEditor.showLayerName(true);
        geopistaEditor.hideLayerNamePanel();
        
        geopistaEditor.setVisible(true);
    }

    public void renombrarComponentes(){
        setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.mapaJPanel.tag1")));
    }

    public void initEditor() {

    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
    	progressDialog.report(aplicacion.getI18nString("inventario.mapaJPanel.tag3"));
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
    						progressDialog.report(aplicacion.getI18nString("inventario.mapaJPanel.tag3"));

    						try {

    							progressDialog.report(aplicacion.getI18nString("inventario.mapaJPanel.tag3"));
    							    	
    							//SATEC. Le pasamos el control de progreso para posteriormente poder cancelar
    							if (!UtilRegistroExp_LCGIII.showGeopistaMap(AppContext.getApplicationContext().getMainFrame(), geopistaEditor, Constantes.idMapaInventario, false, null, null,progressDialog))
    							{
    								new JOptionPane(aplicacion.getI18nString("inventario.mapaJPanel.tag2"),
    								JOptionPane.ERROR_MESSAGE).createDialog(MapaJPanel.this, "ERROR").show();
    							}

    						} catch (CancelException e1){
    							 stopApp();
    						}
    						catch (Exception e) {
    						

    							e.printStackTrace();
    						}

    					} 
    					catch (Exception e)
    					{
    						e.printStackTrace();
    					} 
    					finally
    					{
    						progressDialog.setVisible(false);
    					}
    				}
    			}).start();
    		}
    	});
    	GUIUtil.centreOnWindow(progressDialog);
    	progressDialog.setVisible(true);
    	
    	geopistaEditor.addGeopistaListener(new GeopistaListener() {
            public void selectionChanged(IAbstractSelection abtractSelection) {
                fireActionPerformed();
                //doAction=true;
            }
            public void featureAdded(FeatureEvent e) {
            }
            public void featureRemoved(FeatureEvent e) {
            }
            public void featureModified(FeatureEvent e) {
            }
            public void featureActioned(IAbstractSelection abtractSelection) {
                /** doble click */
                fireActionPerformed();
            }
        });
    	

        /*geopistaEditor.getLayerViewPanel().addListener(new LayerViewPanelListener() {
            public void painted(Graphics graphics) {}

            public void selectionChanged() {
               if (doAction){               
            	   fireActionPerformed();
            	   doAction=false;
               }
            }

            public void cursorPositionChanged(String x, String y) {               
            }
            
            public void componentShown(ComponentEvent e){            	
            }
        });*/
    
    	
    	/*geopistaEditor.addLayerListener(new LayerListener()
        {
				public void categoryChanged(CategoryEvent e) {
				}
	
				public void featuresChanged(FeatureEvent e) {
					System.out.println("FEATURE CHANGED");
				}
	
				public void layerChanged(LayerEvent e) {
				}        
        	}
        );*/
 
    }
    private void  stopApp(){
		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada. Reinicie la aplicacion");

		 if (inventarioInternalFrame!=null){
    		inventarioInternalFrame.dispose();
    		inventarioInternalFrame=null;
    	}
    	/*toolMenu.setEnabled(false);
    	configMenu.setEnabled(false);
    	idiomaMenu.setEnabled(false);*/
    }
    
    public static void updateFeatures(Object[] updateFeatures) throws NoIDException,
    ACException, Exception
    {
    	AppContext aplicacionLocal = (AppContext) AppContext.getApplicationContext();
    	if (aplicacionLocal.isLogged())
    	{

    		if (aplicacionLocal.isLogged())
    		{
    			Collection newFeaturesCollection = Arrays.asList(updateFeatures);
    			
    			//LCGIII.
    			//GeopistaLayer localLayer = ((GeopistaFeature) updateFeatures[0]).getLayer();
    			IGeopistaLayer localLayer = ((GeopistaFeature) updateFeatures[0]).getLayer();
    			

    			Iterator newFeaturesCollectionIter = newFeaturesCollection.iterator();
    			while(newFeaturesCollectionIter.hasNext())
    			{
    				Feature currentFeature = (Feature) newFeaturesCollectionIter.next();
    				if(currentFeature  instanceof GeopistaFeature)
    				{
    					((GeopistaFeature) currentFeature).setNew(false);
    					((GeopistaFeature) currentFeature).setDeleted(false);
    					((GeopistaFeature) currentFeature).setDirty(true);
    				}
    			}

    			FeatureCollection featureCollection = new FeatureDataset(
    					newFeaturesCollection, localLayer.getFeatureCollectionWrapper()
    					.getFeatureSchema());

    			GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer
    			.getDataSourceQuery().getDataSource();
    			geopistaServerDataSource.getConnection().executeUpdate(
    					localLayer.getDataSourceQuery().getQuery(), featureCollection,
    					null);
    			
    		}

    	}

    }

    /**
     * Metodo que carga el mapa en el panel
     * @param mapa
     * @param editable
     * @param panel
     */
    public void load(final int mapa, final boolean editable, final JPanel panel){
        /** cargamos el mapa */
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop, null);
        progressDialog.setTitle(aplicacion.getI18nString("inventario.mapaJPanel.tag3"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                 new Thread(new Runnable(){
                          public void run(){
                              try{
                                  progressDialog.report(aplicacion.getI18nString("inventario.mapaJPanel.tag3"));
                                  if (com.geopista.app.licencias.CUtilidadesComponentes_LCGIII.showGeopistaMap(desktop, panel, geopistaEditor, mapa, editable)){
                                      GeopistaLayer layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("parcelas");
                                      if (layer!=null){
                                          layer.setEditable(true);
                                          layer.setActiva(true);
                                      }else{
                                          new JOptionPane(aplicacion.getI18nString("inventario.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                      }
                                      GeopistaLayer layervias=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("vias");
                                      if (layervias!=null){
                                          layervias.setEditable(true);
                                          layervias.setActiva(true);
                                      }else{
                                          new JOptionPane(aplicacion.getI18nString("inventario.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                      }
                                  }else new JOptionPane(aplicacion.getI18nString("inventario.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();

                              }catch(Exception e){
                                logger.error("Error", e);
                                ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
                              }finally{
                                progressDialog.setVisible(false);
                              }
                    }
                }).start();
            }
        });

        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        geopistaEditor.addGeopistaListener(new GeopistaListener() {
            public void selectionChanged(IAbstractSelection abtractSelection) {
                fireActionPerformed();
            }
            public void featureAdded(FeatureEvent e) {
            }
            public void featureRemoved(FeatureEvent e) {
            }
            public void featureModified(FeatureEvent e) {
            }
            public void featureActioned(IAbstractSelection abtractSelection) {
                /** doble click */
                fireActionPerformed();
            }
        });
        
        /*geopistaEditor.getLayerViewPanel().addListener(new LayerViewPanelListener() {
            public void painted(Graphics graphics) {}

            public void selectionChanged() {
               System.out.println("Selection Changed");
            }

            public void cursorPositionChanged(String x, String y) {               
            }
            
            public void componentShown(ComponentEvent e){            	
            }
        });*/
    
        
        /*geopistaEditor.addLayerListener(new LayerListener()
        {
				public void categoryChanged(CategoryEvent e) {
				}
	
				public void featuresChanged(FeatureEvent e) {
					System.out.println("FEATURE CHANGED");
				}
	
				public void layerChanged(LayerEvent e) {
				}        
        	}
        );*/

    }
        
    public void refreshFeatureSelectionEIEL(Object[] featuresId, Object[] layersId, long idBien){
    	InventarioClient inventarioClient = null;
    	inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ Constantes.INVENTARIO_SERVLET_NAME);
    	try {
    		
			Hashtable hs = inventarioClient.getElementosComprobarIntegEIELInventario(idBien);
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();

            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_parcelas");
            //TODO. Si el mapa no se carga esta comprobacion puede dar error.
            if(geopistaLayer==null)
            	return;
            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	           Collection featureC=new ArrayList();
	           
	           if (layersId!=null) {
		           for (int j = 0; j < layersId.length; j++) {
		
			            while (allFeatures.hasNext()) {
			                Feature feature= (Feature)allFeatures.next();    
			                for( int i=0;i<featuresId.length;i++){
			                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[j]))){
			                       featureC.add(feature);
			                    }
			                }
			            }
		           }
		           geopistaEditor.select(geopistaLayer, featureC);
	           }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		if (layersId!=null){
	            		if (geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
	            			listaFeatures.add(featuresId[i]);
            		}
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayer, listaFeatures.toArray());
            }            
            
            GeopistaLayer geopistaLayerVias= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_vias");                       
            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeaturesVias= geopistaLayerVias.getFeatureCollectionWrapper().getFeatures().iterator();
	            Collection featureC=new ArrayList();
	            
	            if (layersId!=null) {
		            for (int j = 0; j < layersId.length; j++) {
		            	while (allFeaturesVias.hasNext()) {
			                Feature feature= (Feature)allFeaturesVias.next();
			                for (int i=0; i<featuresId.length; i++){
			                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[j]))){
			                      featureC.add(feature);
			                       
			                    }
			                }
			            }
		            }
		            geopistaEditor.select(geopistaLayer, featureC);
	            }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		 if (layersId!=null) {
	            		if (geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
	            			listaFeatures.add(featuresId[i]);
            		 }
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayerVias, listaFeatures.toArray());
            }

            // EIEL:
            
            if (!hs.isEmpty()){
				java.util.Vector vTablas = (Vector) hs.get(0);
				java.util.Vector vFeatures = (Vector) hs.get(1);
				for (int i=0;i<vTablas.size();i++){

					int idLayer = -1;
					String tabla = (String) vTablas.get(i);
					
					GeopistaLayer geopistaLayerEIEL = returnGeopistaLayer(tabla);
					
			        //TODO. Si el mapa no se carga esta comprobacion puede dar error.
			        if(geopistaLayerEIEL==null)
			        	return;
			        
			        if (!(geopistaLayerEIEL instanceof DynamicLayer)){
			            Iterator allFeatures= geopistaLayerEIEL.getFeatureCollectionWrapper().getFeatures().iterator();
			           Collection featureC=new ArrayList();
			           while (allFeatures.hasNext()) {
				                Feature feature= (Feature)allFeatures.next();    
				                for( int j=0;j<vFeatures.size();j++){
				                	String tabla2 = (String) vTablas.get(j);
									GeopistaLayer geopistaLayerEIEL2 = returnGeopistaLayer(tabla2);
									
				                	if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)vFeatures.get(i)) && geopistaLayerEIEL.getId_LayerDataBase() == geopistaLayerEIEL2.getId_LayerDataBase()){
				                       featureC.add(feature);
				                    }
				                }
				       
			           }
			           geopistaEditor.select(geopistaLayerEIEL, featureC);

			        }else{
			        	java.util.List listaFeatures = new ArrayList();
			        	int n = vFeatures.size();
			        	for (int k=0;k<n;k++){
			        		if (idLayer!=-1){
			            		if (geopistaLayerEIEL.getId_LayerDataBase() == idLayer)
			            			listaFeatures.add((String)vFeatures.get(k));
			        		}
			        	}
			        	if (listaFeatures.size() > 0)
			        		selectDynamicFeatures(geopistaLayerEIEL, listaFeatures.toArray());
			        }
				}
    		}
    	
            geopistaEditor.zoomToSelected();

		} catch (Exception ex) {
			logger.error("Exception: " ,ex);
		}finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private GeopistaLayer returnGeopistaLayer(String tabla){
    	
 
	    GeopistaLayer geopistaLayerEIEL = null;
	    
		if (tabla.equals(ConstantesEIEL.TABLA_C_DEPOSITOS)){
			geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_DEPOSITOS);
		}
		else{
			if (tabla.equals(ConstantesEIEL.TABLA_C_CENTRO_ASISTENCIAL)){
				geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CENTRO_ASISTENCIAL);
			}
			else{
				if (tabla.equals(ConstantesEIEL.TABLA_C_CASA_CONSISTORIAL)){
					geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CASA_CONSISTORIAL);
				}
				else{
					if (tabla.equals(ConstantesEIEL.TABLA_C_CEMENTERIO)){
						geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CEMENTERIO);
					}
					else{
						if (tabla.equals(ConstantesEIEL.TABLA_C_CENTRO_CULTURAL)){
							geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CENTRO_CULTURAL);
						}
						else{
							if (tabla.equals(ConstantesEIEL.TABLA_C_CENTRO_ENSENIANZA)){
								geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CENTRO_ENSENIANZA);
							}
							else{
								if (tabla.equals(ConstantesEIEL.TABLA_C_INSTALACION_DEPORTIVA)){
									geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_INSTALACION_DEPORTIVA);
								}
								else{
									if (tabla.equals(ConstantesEIEL.TABLA_C_INCENDIOS_PROTECCION)){
										geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_INCENDIOS_PROTECCION);
									}
									else{
										if (tabla.equals(ConstantesEIEL.TABLA_C_LONJAS_MERCADOS)){
											geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_LONJAS_MERCADOS);
										}
										else{
											if (tabla.equals(ConstantesEIEL.TABLA_C_MATADEROS)){
												geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_MATADEROS);
											}
											else{
												if (tabla.equals(ConstantesEIEL.TABLA_C_PARQUES_JARDINES)){
													geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_PARQUES_JARDINES);
												}
												else{
													if (tabla.equals(ConstantesEIEL.TABLA_C_CENTRO_SANITARIO)){
														geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CENTRO_SANITARIO);
													}
													else{
														if (tabla.equals(ConstantesEIEL.TABLA_C_EDIFICIO_SIN_USO)){
															geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_EDIFICIO_SIN_USO);
														}
														else{
															if (tabla.equals(ConstantesEIEL.TABLA_C_TANATORIO)){
																geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_TANATORIO);
															}
															else{
																if (tabla.equals(ConstantesEIEL.TABLA_C_CARRETERAS)){
																	geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_CARRETERAS);
																}
																else{
																	if (tabla.equals(ConstantesEIEL.TABLA_C_ALUMBRADOS)){
																		geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_ALUMBRADOS);
																	}
																	else{
																		if (tabla.equals(ConstantesEIEL.TABLA_C_DEPURADORAS)){
																			geopistaLayerEIEL = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(ConstantesEIEL.LAYER_DEPURADORAS);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	    return geopistaLayerEIEL;
	}
    /**
     * Metodo que hace zoom en el mapa a la lista de ids de features que recibe como parametro
     * @param featuresId
     */
    public void refreshFeatureSelection(Object[] featuresId, Object[] layersId) {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();

            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_parcelas");
            //TODO. Si el mapa no se carga esta comprobacion puede dar error.
            if(geopistaLayer==null)
            	return;
            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	           Collection featureC=new ArrayList();
	           
	           if (layersId!=null) {
		           for (int j = 0; j < layersId.length; j++) {
		
			            while (allFeatures.hasNext()) {
			                Feature feature= (Feature)allFeatures.next();    
			                for( int i=0;i<featuresId.length;i++){
			                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[j]))){
			                       featureC.add(feature);
			                    }
			                }
			            }
		           }
		           geopistaEditor.select(geopistaLayer, featureC);
	           }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		if (layersId!=null){
	            		if (geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
	            			listaFeatures.add(featuresId[i]);
            		}
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayer, listaFeatures.toArray());
            }
            
            
            
            GeopistaLayer geopistaLayerVias= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_vias");                       
            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeaturesVias= geopistaLayerVias.getFeatureCollectionWrapper().getFeatures().iterator();
	            Collection featureC=new ArrayList();
	            
	            if (layersId!=null) {
		            for (int j = 0; j < layersId.length; j++) {
		            	while (allFeaturesVias.hasNext()) {
			                Feature feature= (Feature)allFeaturesVias.next();
			                for (int i=0; i<featuresId.length; i++){
			                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[j]))){
			                      featureC.add(feature);
			                       
			                    }
			                }
			            }
		            }
		            geopistaEditor.select(geopistaLayer, featureC);
	            }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		 if (layersId!=null) {
	            		if (geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
	            			listaFeatures.add(featuresId[i]);
            		 }
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayerVias, listaFeatures.toArray());
            }

            geopistaEditor.zoomToSelected();

		} catch (Exception ex) {
			logger.error("Exception: " ,ex);
		}finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

	}

    
    /**
     * Método que selecciona features en capas dinámicas
     */
    private void selectDynamicFeatures(GeopistaLayer geopistaLayer, Object[] featuresId){
        GeopistaConnection geopistaConnection = (GeopistaConnection) geopistaLayer.getDataSourceQuery().getDataSource().getConnection();
    	CoordinateSystem inCoord = geopistaLayer.getLayerManager().getCoordinateSystem();
        DriverProperties driverProperties = geopistaConnection.getDriverProperties();
    	driverProperties.put("srid_destino",inCoord.getEPSGCode());
    	java.util.List listaFeatures = geopistaConnection.loadFeatures(geopistaLayer,featuresId);
    	Iterator itFeatures = listaFeatures.iterator();
    	while (itFeatures.hasNext()){
    		geopistaEditor.select(geopistaLayer, (GeopistaFeature)itFeatures.next());
    	}
    	geopistaConnection.close();
    }
    
    public void flash(Object[] featuresId){
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (featuresId== null) return;
            LayerViewPanel layerViewPanel= geopistaEditor.getLayerViewPanel();
            if (layerViewPanel == null) return;
            ArrayList alist= new ArrayList();
            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
            while (allFeatures.hasNext()) {
                Feature feature= (Feature)allFeatures.next();
                for (int i=0; i<featuresId.length; i++){
                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i])){
                        alist.add(feature);
                        break;
                    }
                }
            }

            new ZoomToSelectedItemsPlugIn().flash(FeatureUtil.toGeometries(alist), layerViewPanel);
        }catch (NoninvertibleTransformException ex){
            logger.debug("flashFeature(feature) - Error al hacer flash a la feature.");
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void zoom(Object[] featuresId){
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (featuresId == null) return;
            LayerViewPanel layerViewPanel= geopistaEditor.getLayerViewPanel();
            if (layerViewPanel == null) return;
            ArrayList alist= new ArrayList();
            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
            while (allFeatures.hasNext()) {
                Feature feature= (Feature)allFeatures.next();
                for (int i=0; i<featuresId.length; i++){
                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i])){
                        alist.add(feature);
                        break;
                    }
                }
            }
            new ZoomToSelectedItemsPlugIn().zoom(FeatureUtil.toGeometries(alist), layerViewPanel);

        }catch (NoninvertibleTransformException ex){
            logger.debug("zoomFeature(feature) - Error al hacer zoom a la feature.");
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void clear(){
        try{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
            geopistaEditor.getSelectionManager().clear();
        }catch (Exception ex){
            logger.debug("clear - Error al hacer clear en el mapa.");
        }finally{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    public GeopistaEditor getGeopistaEditor() {
        return geopistaEditor;
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }



}
