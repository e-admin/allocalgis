/**
 * GestorFipModuloPlaneamientoMapPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.ui.cursortool.GeopistaMeasureTool;
import com.geopista.ui.cursortool.GeopistaSelectFeaturesTool;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.gestorfip.ui.fichaurbanistica.tool.FichaUrbanisticaTool;
import com.gestorfip.ui.rpm.tool.RPMTool;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.cursortool.FeatureInfoTool;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.PanTool;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;

public class GestorFipModuloPlaneamientoMapPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static GeopistaEditor geopistaEditor=null;  

	public boolean insertarCapasGeopista=true;

	public String textoAscTemp=null;
	
	private ArrayList<ActionListener> actionListeners= new ArrayList<ActionListener>();

	AppContext appContext = (AppContext) AppContext.getApplicationContext();
	GestorFipGestorPlaneamientoPanel gestorFipGestorPlaneamiento = null;
	

	public GestorFipModuloPlaneamientoMapPanel(GestorFipGestorPlaneamientoPanel gestorFipGestorPlaneamiento)
	{
		super(new GridLayout(1,0));
		this.gestorFipGestorPlaneamiento = gestorFipGestorPlaneamiento;
		initialize();
		
	}    

	public  GeopistaEditor getEditor(){

		return geopistaEditor;
	}

	private void initialize() {

		if (geopistaEditor == null){
			this.setLayout(new GridBagLayout());

			geopistaEditor = new GeopistaEditor("workbench-properties-gestorfip.xml");
			
			appContext.getBlackboard().put(ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES, geopistaEditor);
			
			geopistaEditor.getToolBar().addCursorTool("Select tool", new GeopistaSelectFeaturesTool());
			geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());
			geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
			geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());
			geopistaEditor.getToolBar().addCursorTool("FeatureInfoTool", new FeatureInfoTool());
			geopistaEditor.getToolBar().addCursorTool("RPM tool", new RPMTool());
			geopistaEditor.getToolBar().addCursorTool("Ficha Urbanistica tool", new FichaUrbanisticaTool());

			geopistaEditor.setVisible(true);
			geopistaEditor.showLayerName(true);

			this.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("LocalGISGestorFip", "gestorFip.planeamiento.graphiceditor.title"),
							TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(null, java.awt.Font.BOLD, 13)));

			this.add(geopistaEditor, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER,GridBagConstraints.BOTH, 
					new Insets (0,10,5,10), 0,0));
			

			geopistaEditor.addGeopistaListener(new GeopistaListener() {
	            public void selectionChanged(AbstractSelection abtractSelection) {
	                fireActionPerformed();
	                try {


	                	Iterator it = geopistaEditor.getLayerManager().getOrderLayers().iterator();
	                    for (; it.hasNext();){
	                    	GeopistaLayer  layer = (GeopistaLayer)it.next();
	                    	ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(
	                    												geopistaEditor.getLayerManager().getLayer(layer.getName()));
	                    	
	                    	Iterator featuresCollectionIter = featuresCollection.iterator();
	                        if(featuresCollectionIter.hasNext()){
	                        	
	                        	Feature actualFeature = (Feature) featuresCollectionIter.next();
	                        	if(actualFeature instanceof GeopistaFeature){
	                        		int idFeature = Integer.valueOf(((GeopistaFeature)actualFeature).getSystemId());
	                        		
	                        		getGestorFipGestorPlaneamiento().searchEntidadesSelectedMap(layer.getId_LayerDataBase(), idFeature);
		    	                	
	                        	}
	                        }
	                    }
	                	
						
					} catch (Exception e){
						e.printStackTrace();
					}
	            }
	            public void featureAdded(FeatureEvent e) {
	            }
	            public void featureRemoved(FeatureEvent e) {
	            }
	            public void featureModified(FeatureEvent e) {
	            }
	            public void featureActioned(AbstractSelection abtractSelection) {
	                fireActionPerformed();
	            }
				@Override
				public void featureActioned(IAbstractSelection arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void selectionChanged(IAbstractSelection arg0) {
					// TODO Auto-generated method stub
					
				}
	        });	
			
		}
		
	}	
	
	public GestorFipGestorPlaneamientoPanel getGestorFipGestorPlaneamiento() {
		return gestorFipGestorPlaneamiento;
	}

	public void setGestorFipGestorPlaneamiento(
			GestorFipGestorPlaneamientoPanel gestorFipGestorPlaneamiento) {
		this.gestorFipGestorPlaneamiento = gestorFipGestorPlaneamiento;
	}

	
	private void fireActionPerformed() {
        for (Iterator<ActionListener> i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }
	
	public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }
    
    public void changeSelection(){
    	fireActionPerformed();
    }
	
	 public void initEditor() {
		 if( this.gestorFipGestorPlaneamiento != null){
	    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    	progressDialog.setTitle("TaskMonitorDialog.Wait");
	    	progressDialog.report(I18N.get("LocalGISGestorFip","gestorFip.editorimportador.panel.LoadingEditor"));
	    	progressDialog.addComponentListener(new ComponentAdapter()
	    	{
	    		public void componentShown(ComponentEvent e)
	    		{   
	    			new Thread(new Runnable()
	    			{
	    				@SuppressWarnings("deprecation")
						public void run()
	    				{
	    					try
	    					{   
	    						progressDialog.report(I18N.get("LocalGISGestorFip","gestorFip.editorimportador.panel.LoadingEditor"));

	    						try {

	    							progressDialog.report(I18N.get("LocalGISGestorFip",
	    									"gestorFip.editorimportador.panel.LoadingEditor"));
	    							
	    							int idMapa = ConstantesGestorFIP.MAPA_GESTORFIP;
	    							boolean isMapReloadAsocDetEnt = false;
	    							if(AppContext.getApplicationContext().getBlackboard().get(ConstantesGestorFIP.IS_MAP_RELOAD_ASOC_DET_ENT) != null){
	    								isMapReloadAsocDetEnt = (Boolean)AppContext.getApplicationContext().getBlackboard().get(ConstantesGestorFIP.IS_MAP_RELOAD_ASOC_DET_ENT);
	    							}
	    							
	    							if (!GestorFipUtils.showGeopistaMap(AppContext.getApplicationContext().getMainFrame(), 
	    											geopistaEditor, idMapa, false, null, null, 
	    											ConstantesGestorFIP.GEOPISTA_EDITOR_ASOCIACION_DETERMINACIONES_ENTIDADES, 
	    											isMapReloadAsocDetEnt))
	    							{
	    								new JOptionPane(I18N.get("LocalGISGestorFip",
	    											"gestorFip.editorimportador.panel.ErrorLoadingMap"),
	    								JOptionPane.ERROR_MESSAGE).createDialog(GestorFipModuloPlaneamientoMapPanel.this, "ERROR").show();
	    							}

	    						} catch (Exception e) {

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
	    }
	 }
	    
	
}
