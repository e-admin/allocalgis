/**
 * ReloadMapPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.reload;

import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.utils.LoadedLayers;
import com.geopista.app.eiel.utils.LocalGISEIELUtils;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.server.administradorCartografia.CancelException;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ReloadMapPlugIn extends AbstractPlugIn
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(ReloadMapPlugIn.class);

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();

    private String toolBarCategory = "MunicipalitiesPlugIn.category";


    public ReloadMapPlugIn(){
    }


    public String getName()
    {
        return "Lista Mapas EIEL";
    }

    public void initialize(PlugInContext context) throws Exception
    {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        WorkbenchToolBar workbenchToolBar = ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory);
        workbenchToolBar.addPlugIn(null, this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext(),getPanelMapas());
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        reportNothingToUndoYet(context);
        
      

        return true;
    }

   
    
    //Se visualizará una lista con los municipios que forma parte de la entidad supramunicipal del
    //usuario, en la que se podrá seleccionar en todo momento un municipio por defecto
    private JComboBox getComboMapas() {
        JComboBox comboMapas = new JComboBox();
        
        aplicacion.getBlackboard().put(UserPreferenceConstants.MAPAS_COMBO, comboMapas);
        comboMapas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JComboBox cb = (JComboBox)evt.getSource();
            	
            	
            	StringTokenizer st = new StringTokenizer((String)cb.getSelectedItem(),"-");
    	        if (st.hasMoreTokens()){
    	        	String nombreMapa = (String)st.nextToken();
    	        	final Integer idMapa = Integer.parseInt((String)st.nextToken());
    	        	
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

    		    							forceLoadMap(idMapa,progressDialog);
    		    							LoadedLayers.remove();


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
        });
    	return comboMapas;
    }
    
    
    /***
     * Carga un mapa
     * @param idMapa
     * @param progressDialog 
     * @throws CancelException 
     * @throws HeadlessException 
     */
    public static void forceLoadMap(int idMapa, TaskMonitorDialog progressDialog) throws HeadlessException, CancelException{
		GeopistaEditorPanel panel = (GeopistaEditorPanel) AppContext.getApplicationContext().getBlackboard().get("GeopistaEditor");
    	GeopistaEditor geopistaEditor=panel.getEditor();

    	//AQUI SE CARGA EL MAPA
    	aplicacion.getBlackboard().put(Const.KEY_LOAD_FEATURE_LAYER,0);
    	aplicacion.getBlackboard().put(Const.KEY_USE_SAME_SRID,1);
    	String filtro[]={"EIEL_GLOBAL","pnoa","Catastro"};
    	
    	if (idMapa==ConstantesLocalGISEIEL.MAPA_EIEL_GLOBAL){
    		aplicacion.getBlackboard().put(Const.KEY_LOAD_FEATURE_LAYER,1);    		    		    	        		
    		filtro=null;
    	}
    	
		if (!LocalGISEIELUtils.forceLoadGeopistaMap(AppContext.getApplicationContext().getMainFrame(), geopistaEditor, idMapa, false, null, null,filtro,progressDialog))
		{
			new JOptionPane(I18N.get("LocalGISEIEL","localgiseiel.editorimportador.panel.ErrorLoadingMap")+"\n"+I18N.get("LocalGISEIEL",
					"localgiseiel.editorimportador.panel.ErrorLoadingMap3"),JOptionPane.ERROR_MESSAGE).createDialog(panel, "ERROR").show();
		}
    	aplicacion.getBlackboard().remove(Const.KEY_LOAD_FEATURE_LAYER);
    	aplicacion.getBlackboard().remove(Const.KEY_USE_SAME_SRID);
    }
    

    /*private JToggleButton getToggleButton (){
    	JToggleButton jToggleButton = new JToggleButton(aplicacion.getI18nString("SeleccionAutomatica"));
    	jToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if (((JToggleButton)evt.getSource()).isSelected())
            		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, true);
            	else
            		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, false);
            }
    	});
		AppContext.getApplicationContext().getBlackboard().put(AppContext.SEL_MUNI_AUTO, false);
    	return jToggleButton;
    }*/
    
    private JPanel getPanelMapas(){
    	JPanel jPanel = new JPanel();
		//jPanel.setBorder( new javax.swing.border.MatteBorder(2, 2, 2, 2, Color.BLACK) );

		//jPanel.setBorder(border)
    	jPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    	jPanel.add(this.getComboMapas());
    	//jPanel.add(getToggleButton());
    	return jPanel;
    }
    
    
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        //EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        GeopistaEnableCheckFactory checkFactoryGeopista = new GeopistaEnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
        		checkFactoryGeopista.createWindowWithLayerManagerMustBeActiveCheck()).add(
//                checkFactoryGeopista.createDisconnectedDisableMunicipalities());//.add(
                checkFactoryGeopista.createWindowWithAssociatedTaskFrameMustBeActiveCheck());

    }

}
