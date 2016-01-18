/**
 * MapaJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.utilidades.ShowMaps;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.NoIDException;
import com.geopista.ui.plugin.selectonecemeteryitem.SelectOneCemeteryItemTool;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;



public class MapaJPanel  extends javax.swing.JPanel {
    Logger logger= Logger.getLogger(MapaJPanel.class);

    private javax.swing.JFrame desktop;
    private GeopistaEditor geopistaEditor;

    private ArrayList actionListeners= new ArrayList();
    private ApplicationContext aplicacion;
    
    private boolean hayCementerio = true;


    /** Creates new form MapaJPanel */
    public MapaJPanel(javax.swing.JFrame desktop) {
        this.desktop= desktop;
        aplicacion= (AppContext)AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    
    
    public boolean isHayCementerio() {
		return hayCementerio;
	}



	public void setHayCementerio(boolean hayCementerio) {
		this.hayCementerio = hayCementerio;
	}



	private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.GridLayout(1, 0));
        geopistaEditor= new GeopistaEditor("workbench-properties-cementerio-simple.xml");
        geopistaEditor.showLayerName(true);
        geopistaEditor.setVisible(true);
    }

    public void renombrarComponentes(){
        setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.mapaJPanel.tag1")));
    }

    public void initEditor() {

    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
    	progressDialog.report(aplicacion.getI18nString("cementerio.mapaJPanel.tag3"));
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
    						progressDialog.report(aplicacion.getI18nString("cementerio.mapaJPanel.tag3"));

    						try {

    							progressDialog.report(aplicacion.getI18nString("cementerio.mapaJPanel.tag3"));
    							    	
    							//SATEC. Le pasamos el control de progreso para posteriormente poder cancelar
    							if (!showGeopistaMap(AppContext.getApplicationContext().getMainFrame(), geopistaEditor, Constantes.idMapaCementerios, false, null, null,progressDialog))
    							{
    								new JOptionPane(aplicacion.getI18nString("cementerio.mapaJPanel.tag2"),
    								JOptionPane.ERROR_MESSAGE).createDialog(MapaJPanel.this, "ERROR").show();
    							}

    							
    							JComboBox jc = (JComboBox)aplicacion.getBlackboard().get(Constantes.COMBO_CEMENT);
                      			if (jc != null){
                      				GeopistaLayer layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("cementerios");
                      				
                      				if (layer.getFeatureCollectionWrapper().size() == 0){
                      					String message = aplicacion.getI18nString("cementerio.mapaJPanel.nocementerio");
                      					
                      					JOptionPane.showMessageDialog(desktop, StringUtils.center(message, message.length()),
                                				"Cementerios no encontrados",
                                		        JOptionPane.WARNING_MESSAGE);
                      							hayCementerio = false;
                      							
                      				}
                      				
                      				Iterator itCementerios = layer.getFeatureCollectionWrapper().iterator();
                      	    		while(itCementerios.hasNext()){
                      	    			Feature localFeature = (Feature)itCementerios.next();
                      	    			String nombreCementerio = localFeature.getString("Nombre").trim();
                      	    			String idCementerio = localFeature.getString("ID").trim();
                      	    			nombreCementerio = StringUtil.limitLength(nombreCementerio, 25);
                      		        	jc.addItem(idCementerio+"-"+nombreCementerio);
                      		        	
                      		        	if(aplicacion.getBlackboard().get(Constantes.ID_CEMENTERIO) == null)
                      		        		aplicacion.getBlackboard().put(Constantes.ID_CEMENTERIO, (Integer.parseInt(idCementerio)));
                      	    		}
                      			
                      			jc.addActionListener(new java.awt.event.ActionListener() {
                      	           
                      				public void actionPerformed(java.awt.event.ActionEvent evt) {
                      	            	JComboBox cb = (JComboBox)evt.getSource();
                      	    	        StringTokenizer st = new StringTokenizer((String)cb.getSelectedItem(),"-");
                      	    	        if (st.hasMoreTokens()){
                      	    	        	String cementerio = (String)st.nextToken();
                      	    	        	aplicacion.getBlackboard().put(Constantes.ID_CEMENTERIO, (Integer.parseInt(cementerio)));
                      	    	        	
                      	    	        	GeopistaLayer layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("cementerios");
                              				Iterator itCementerios = layer.getFeatureCollectionWrapper().iterator();
                              	    		while(itCementerios.hasNext()){
                              	    			Feature localFeature = (Feature)itCementerios.next();
                              	    			String idCementerio = localFeature.getString("ID").trim();
                              	    			if(cementerio.equalsIgnoreCase(idCementerio)){
                              	    				try {
														geopistaEditor.getLayerViewPanel().getViewport().zoom(localFeature.getGeometry().getEnvelopeInternal());
													} catch (NoninvertibleTransformException e) {
														e.printStackTrace();
													}
                              	    			}
                              	    		}
                      	    	        }
                      	            }
                      	        });
                      		
    						} 
    						}
    						catch (CancelException e1){
    							 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada");
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
			public void selectionChanged(AbstractSelection abtractSelection) {
				
			}
        });
 
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
    			GeopistaLayer localLayer = (GeopistaLayer) ((GeopistaFeature) updateFeatures[0])
    			.getLayer();

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
        progressDialog.setTitle(aplicacion.getI18nString("cementerio.mapaJPanel.tag3"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                 new Thread(new Runnable(){
                          public void run(){
                              try{
                                  progressDialog.report(aplicacion.getI18nString("cementerio.mapaJPanel.tag3"));
                                  if (com.geopista.app.licencias.CUtilidadesComponentes_LCGIII.showGeopistaMap(desktop, panel, geopistaEditor, mapa, editable)){
                                      GeopistaLayer layer=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("cementerios");
                                      if (layer!=null){
                                          layer.setEditable(false);
                                          layer.setActiva(false);
                                      	  	
                                      }else{
                                          new JOptionPane(aplicacion.getI18nString("cementerio.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                      }
                                      GeopistaLayer layervias=(GeopistaLayer)geopistaEditor.getLayerManager().getLayer("unidad_enterramiento");
                                      if (layervias!=null){
                                          layervias.setEditable(true);
                                          layervias.setActiva(true);
                                      }else{
                                          new JOptionPane(aplicacion.getI18nString("cementerio.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();
                                      }
                                  }else new JOptionPane(aplicacion.getI18nString("cementerio.mapaJPanel.tag2"), JOptionPane.ERROR_MESSAGE).createDialog(desktop, "ERROR").show();

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
			public void selectionChanged(AbstractSelection abtractSelection) {
				
			}
        });

        
    }

	public boolean showGeopistaMap(JFrame padre,
			GeopistaEditor geopistaEditor, int id_map, boolean editable,
			String visible, String novisible, TaskMonitor monitor)
			throws CancelException {
		try {
			geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
			geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
			geopistaEditor.addCursorTool("Measure", "com.geopista.ui.cursortool.GeopistaMeasureTool");
	        
			geopistaEditor.getToolBar().addCursorTool(new SelectOneCemeteryItemTool());
			
			
			 // comprobamos que ya haya sido cargado el mapa para no volver a hacerlo
			 
			if ((geopistaEditor.getLayerManager().getLayers() != null)
					&& (geopistaEditor.getLayerManager().getLayers().size() > 0)) {
				ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
				geopistaEditor.getSelectionManager().clear();
				geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
				return true;
			}
			
			try {
				geopistaEditor.loadMap("geopista:///" + id_map, monitor);

				for (Iterator it = geopistaEditor.getLayerManager().getLayers().iterator(); it.hasNext();) {
					GeopistaLayer layer = (GeopistaLayer) it.next();
					layer.setEditable(false);
					layer.setActiva(false);
				}
				
				GeopistaLayer layerUnidadEnterramiento = (GeopistaLayer)geopistaEditor.getLayerManager().getLayer("unidad_enterramiento");
				layerUnidadEnterramiento.setEditable(true);
				layerUnidadEnterramiento.setActiva(true);
				
			} catch (CancelException e1) {
				throw e1;
			} catch (Exception e) {
				return false;
			}

			ShowMaps.setLayersVisible(geopistaEditor, visible, novisible);
			geopistaEditor.getSelectionManager().clear();
			geopistaEditor.getLayerViewPanel().getViewport().zoomToFullExtent();
			return true;
			

		} catch (CancelException e1) {
			throw e1;
		} catch (Exception e) {
			String sMensaje = (e.getMessage() != null
					&& e.getMessage().length() > 0 ? e.getMessage() : e.toString());
			Throwable t = e.getCause();
			int i = 0;
			while (t != null && i < 10) {
				sMensaje = (t.getMessage() != null
						&& t.getMessage().length() > 0 ? t.getMessage() : t.toString());
				t = t.getCause();
				i++;
			}
			JOptionPane.showMessageDialog(padre,"Error al mostrar el mapa.\nERROR: " + sMensaje);
			logger.error("Error al cargar el mapa de Registro de expediente: ",e);
			return false;
		}
	}

    
    /**
     * Metodo que hace zoom en el mapa a la lista de ids de features que recibe como parametro
     * @param featuresId
     */
    public void refreshFeatureSelection(Object[] featuresId, Object[] layersId) {
		try {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();

//            GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_parcelas");
			GeopistaLayer geopistaLayer= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("unidad_enterramiento");
           
            if(geopistaLayer==null)
            	return;

            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
	            while (allFeatures.hasNext()) {
	                Feature feature= (Feature)allFeatures.next();
	                for (int i=0; i<featuresId.length; i++){
	  //                  if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i]))){
	                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase(String.valueOf(featuresId[i]))){
	                        geopistaEditor.select(geopistaLayer, feature);
	                        break;
	                    }
	                }
	            }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		if (geopistaLayer.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
            			listaFeatures.add(featuresId[i]);
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayer, listaFeatures.toArray());
            }
            
          /*  GeopistaLayer geopistaLayerVias= (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("inventario_vias");
            if (!(geopistaLayer instanceof DynamicLayer)){
	            Iterator allFeaturesVias= geopistaLayerVias.getFeatureCollectionWrapper().getFeatures().iterator();
	            while (allFeaturesVias.hasNext()) {
	                Feature feature= (Feature)allFeaturesVias.next();
	                for (int i=0; i<featuresId.length; i++){
	                    if (((GeopistaFeature)feature).getSystemId().equalsIgnoreCase((String)featuresId[i]) && geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i]))){
	                        geopistaEditor.select(geopistaLayer, feature);
	                        break;
	                    }
	                }
	            }
            }else{
            	java.util.List listaFeatures = new ArrayList();
            	int n = featuresId.length;
            	for (int i=0;i<n;i++){
            		if (geopistaLayerVias.getId_LayerDataBase() == (Integer.parseInt((String)layersId[i])))
            			listaFeatures.add(featuresId[i]);
            	}
            	if (listaFeatures.size() > 0)
            		selectDynamicFeatures(geopistaLayerVias, listaFeatures.toArray());
            }
*/
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
