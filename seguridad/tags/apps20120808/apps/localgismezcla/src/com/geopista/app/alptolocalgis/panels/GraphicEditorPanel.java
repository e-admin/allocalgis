package com.geopista.app.alptolocalgis.panels;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import com.geopista.app.AppContext;
import com.geopista.app.alptolocalgis.beans.ConstantesAlp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.editor.GeopistaEditor;
import com.geopista.ui.cursortool.GeopistaMeasureTool;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.PanTool;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;


import java.awt.GridBagLayout;

public class GraphicEditorPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static GeopistaEditor geopistaEditor=null;  
	
	public boolean insertarCapasGeopista=true;
	
	public String textoAscTemp=null;
	
    public GraphicEditorPanel()
    {
        super(new GridLayout(1,0));
        initialize();
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.alptolocalgis.languages.AlpToLocalGISi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("AlpToLocalGIS",bundle);        
    }    
    
    public static GeopistaEditor getEditor(){
    	
    	return geopistaEditor;
    }
    
    private void initialize() {
		
    		this.setLayout(new GridBagLayout());

    		geopistaEditor = new GeopistaEditor("workbench-properties-localgis-simple.xml");
    		geopistaEditor.getToolBar().addCursorTool("Zoom In/Out", new ZoomTool());
    		geopistaEditor.getToolBar().addCursorTool("Measure", new GeopistaMeasureTool());
    		geopistaEditor.getToolBar().addCursorTool("Pan", new PanTool());		
    		geopistaEditor.setVisible(true);
    		geopistaEditor.showLayerName(true);
    		geopistaEditor.hideLayerNamePanel();    		
    		    		
    		this.setBorder(BorderFactory.createTitledBorder
    	            (null, I18N.get("AlpToLocalGIS", "alptolocalgis.panel.graphiceditor.title"),
    	            		TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font(null, java.awt.Font.BOLD, 13)));
    	    		    		
    		this.add(geopistaEditor, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
    				GridBagConstraints.CENTER,GridBagConstraints.BOTH, 
    				new Insets (0,10,5,10), 0,0));
    		    		    		
	}
    
    public void initEditor() {

    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
    	progressDialog.setTitle("TaskMonitorDialog.Wait");
    	progressDialog.report(I18N.get("AlpToLocalGIS","alptolocalgis.panel.InitializingEditor"));
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
    						progressDialog.report(I18N.get("AlpToLocalGIS","alptolocalgis.panel.LoadingEditor"));

    						try {

    							progressDialog.report(I18N.get("AlpToLocalGIS",
    							"alptolocalgis.panel.LoadingEditor"));
    							
    							int idMapa = obtenerIdMapa("Mapa ALP");
    							
    							if (!UtilRegistroExp.showGeopistaMap(AppContext.getApplicationContext().getMainFrame(), geopistaEditor, idMapa, false, null, null,progressDialog))
    							{
    								new JOptionPane(I18N.get("AlpToLocalGIS",
    								"alptolocalgis.panel.ErrorLoadingMap"),
    								JOptionPane.ERROR_MESSAGE).createDialog(GraphicEditorPanel.this, "ERROR").show();
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
    
    private int obtenerIdMapa(String nombreMapa){
    	
    	int idMapa = -1;
    	
    	try{
    		idMapa = ConstantesAlp.clienteAlp.getIdMapa(nombreMapa);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return idMapa;
    }

}
