package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaListener;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.style.sld.ui.plugin.ChangeSLDStylesPlugIn;
import com.geopista.ui.cursortool.GeopistaMeasureTool;
import com.geopista.ui.cursortool.GeopistaSelectFeaturesEIELTool;
import com.geopista.ui.cursortool.GeopistaSelectFeaturesTool;
import com.geopista.ui.plugin.conectividad.LocalgisEIELSelectFeaturesRedTool;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.cursortool.FeatureInfoTool;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.PanToolNew;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;

public class GeopistaEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static GeopistaEditor geopistaEditor=null;  

	public boolean insertarCapasGeopista=true;

	public String textoAscTemp=null;
	
	private ArrayList actionListeners= new ArrayList();
	
	private ApplicationContext aplicacion;

	private JInternalFrame jif;

	
	public GeopistaEditorPanel()
	{
		super(new GridLayout(1,0));

		Locale loc=I18N.getLocaleAsObject(); 
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);   
		aplicacion= (AppContext)AppContext.getApplicationContext();

		initialize();        
	}    

	public static GeopistaEditor getEditor(){

		return geopistaEditor;
	}

	private void initialize() {

		if (geopistaEditor == null){
			this.setLayout(new GridBagLayout());

			//Para cargar el panel de la EIEL entra por aqui
			geopistaEditor = new GeopistaEditor("workbench-properties-eiel-edicion.xml");
			geopistaEditor.getToolBar().addCursorTool("Select tool", new GeopistaSelectFeaturesTool());
			geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());
			geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
			geopistaEditor.getToolBar().addCursorTool("Pan", new PanToolNew());
			geopistaEditor.getToolBar().addCursorTool("FeatureInfoTool", new FeatureInfoTool());
			geopistaEditor.getToolBar().addCursorTool("Select EIEL tool", new GeopistaSelectFeaturesEIELTool());
			geopistaEditor.getToolBar().addCursorTool("Herramienta Conectividad", new LocalgisEIELSelectFeaturesRedTool());
			
		
			geopistaEditor.setVisible(true);
			geopistaEditor.showLayerName(true);

			this.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("LocalGISEIEL", "localgiseiel.editor.panel.graphiceditor.title"),
							TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(null, java.awt.Font.BOLD, 13)));

			this.add(geopistaEditor, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER,GridBagConstraints.BOTH, 
					new Insets (0,10,5,10), 0,0));
			
			
			geopistaEditor.addGeopistaListener(new GeopistaListener() {
	            public void selectionChanged(AbstractSelection abtractSelection) {
	               
	                //fireActionPerformed(ConstantesLocalGISEIEL.EVENT_EIEL_MAP_SELECTION_CHANGED,abtractSelection);	                
	                /*ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
	                System.out.println("Feature:"+featuresCollection.size());*/
	                
	                /*ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
	                Iterator featuresCollectionIter = featuresCollection.iterator();
	                Feature actualFeature = (Feature) featuresCollectionIter.next();*/
	                fireActionPerformed(abtractSelection,ConstantesLocalGISEIEL.EVENT_EIEL_MAP_SELECTION_CHANGED);
	            }
	            public void featureAdded(FeatureEvent e) {
	            	fireActionPerformed(e,ConstantesLocalGISEIEL.EVENT_EIEL_MAP_FEATURE_CHANGED);
	            }
	            public void featureRemoved(FeatureEvent e) {
	            	System.out.println("Feature borrada");
	            }
	            public void featureModified(FeatureEvent e) {
	                
	            	//System.out.println("Feature modificada");
	            	fireActionPerformed(e,ConstantesLocalGISEIEL.EVENT_EIEL_MAP_FEATURE_CHANGED);
	            	//fireActionPerformed(abtractSelection,ConstantesLocalGISEIEL.EVENT_EIEL_MAP_FEATURE_CHANGED);
	            }
	            public void featureActioned(AbstractSelection abtractSelection) {
	                fireActionPerformed(null,null);
	            }
	        });
			
		}
	}	
	
	private void fireActionPerformed(Object source,String event) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            if (source==null)
            	l.actionPerformed(new ActionEvent(this, 0, event));
            else
            	l.actionPerformed(new ActionEvent(source, 0, event));
        }
    }
	
	public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }
    
    /*public void changeSelection(){
    	fireActionPerformed(null);
    }*/
	
	 public void initEditor(JInternalFrame jif) {

		 	this.jif=jif;
	    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    	progressDialog.setTitle("TaskMonitorDialog.Wait");
	    	progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.editorimportador.panel.LoadingEditor"));
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
	    						progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.editorimportador.panel.LoadingEditor"));

	    						try {

	    							progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.editorimportador.panel.LoadingEditor"));
	    							
	    							int idMapa = ConstantesLocalGISEIEL.MAPA_EIEL_CARGA;
	    							
	    							
	    							//AQUI SE CARGA EL MAPA
	    							if (!LocalGISEIELUtils.showGeopistaMap(AppContext.getApplicationContext().getMainFrame(), geopistaEditor, idMapa, false, null, null,progressDialog))
	    							{
	    								new JOptionPane(I18N.get("LocalGISEIEL",
	    								"localgiseiel.editorimportador.panel.ErrorLoadingMap"),
	    								JOptionPane.ERROR_MESSAGE).createDialog(GeopistaEditorPanel.this, "ERROR").show();
	    							}
	    							//System.out.println("Mapa Cargado");
	    						}
	    						catch (CancelException e1){
	   	   							 stopApp();
	   		    					
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
	    
	 private void  stopApp(){
		 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada. Reinicie la aplicacion");
		 if (jif!=null){
			 jif.dispose();
			 jif=null;
		 }
		
    	/*toolMenu.setEnabled(false);
    	configMenu.setEnabled(false);
    	idiomaMenu.setEnabled(false);*/
    }
}
