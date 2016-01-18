/**
 * LoadExternalLayersPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin.externalLayers;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.app.AppContext;
import com.geopista.app.loadEIELData.service.LoadEIELDataProxy;
import com.geopista.app.loadEIELData.vo.EIELLayer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**Panel que muestra el listado de capas externas y almacena aquéllas que hayan sido seleccionadas por el usuario.
 * @author Silvia García
 *
 */
public class LoadExternalLayersPanel  extends JPanel implements WizardPanel{
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardLayers  = aplicacion.getBlackboard();
  private String localId = null;
  private WizardContext wizardContext;
  private PlugInContext context;
 
  private String nextID = null;
  private JScrollPane jScrollPane = new JScrollPane();
  private JList jList = new JList();

  
  /**Constructor
   * @param id
   * @param nextId
   * @param context
   */
  public LoadExternalLayersPanel(String id, String nextId, PlugInContext context){
    this.nextID = nextId;
    this.localId = id;
    this.context = context;
    
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }// fin del constructor

  
  
  //Inicialización de componentes del panel
  private void jbInit() throws Exception{
	  if(!aplicacion.isLogged())
	    {
	         aplicacion.setProfile("Geopista");
	         aplicacion.login();
	    }
	    this.setLayout(null);
	    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
	            context.getErrorHandler());
	    progressDialog.setTitle("FindExternalLayers");
	    progressDialog.report(aplicacion.getI18nString("FindExternalLayers"));
	    progressDialog.addComponentListener(new ComponentAdapter() {
	        public void componentShown(ComponentEvent e) {
	            //Wait for the dialog to appear before starting the task. Otherwise
	            //the task might possibly finish before the dialog appeared and the
	            //dialog would never close. [Jon Aquino]
	            new Thread(new Runnable(){
	                public void run()
	                {
	                	try{
    
	                		//Cargar lista de capas externas
	                		Locale locale=aplicacion.getI18NResource().getLocale();
	     
	                		LoadEIELDataProxy eielProxy=new LoadEIELDataProxy();
	                		EIELLayer[] layers=null;
	                		
	                		if(locale.toString().equalsIgnoreCase("gl_ES"))
	                			layers=eielProxy.getEIELLayerList("gl");
	                		
	                		else 
                				layers=eielProxy.getEIELLayerList("es");
	                		
    
	                		jScrollPane.setBounds(new Rectangle(65, 40, 280, 205));

						    DefaultListModel dlmEIELLayers = new DefaultListModel();
						    jList.setCellRenderer(new ExternalLayerRenderer());
						    jList.setModel(dlmEIELLayers);
						    
						    //ordenamos las capas
						    Comparator comparator = new Comparator() {
	                            public int compare(Object o1, Object o2) {
	                                return ((((EIELLayer) o1).getId()).toUpperCase()).compareTo((((EIELLayer) o2).getId()).toUpperCase());
	                            }
	                        };

	                        Arrays.sort(layers,comparator);

						    
						    for(int n=0; n<layers.length; n++){
						    	EIELLayer layer=layers[n];	
						    	if(!layer.getId().equals("Provincia"))//la capa de provincias no se añade al listado de capas disponibles
						    		dlmEIELLayers.addElement(layer);
						    }//fin for
	                	}catch(Exception e){
	                	}finally
	                    {
	                        progressDialog.setVisible(false);
	                    } 
	                }
	            }).start();
	        }
	    });
	    GUIUtil.centreOnWindow(progressDialog);
	    progressDialog.setVisible(true);
    
    jScrollPane.getViewport().add(jList, null);
    this.add(jScrollPane, null);
  }//fin jbInit

  
  

  /**Método ejecutado al entrar en el panel
   */
  public void enteredFromLeft(Map dataMap){
  }
  

  
    /**Método ejecutado cuando el usuario pulsa el botón finalizar
     */
    public void exitingToRight() throws Exception {
    //Almacenar en blackboardLayers las capas seleccionadas por el usuario	
    	Object[] selectedLayers =  (Object[]) jList.getSelectedValues();
         ArrayList selectedExternalLayers = new ArrayList();
         for(int n=0;n<selectedLayers.length;n++)
         {
        	 selectedExternalLayers.add(selectedLayers[n]);
           
         }

         blackboardLayers.put("SelectedExternalLayers",selectedExternalLayers);
    }

   
    public void add(InputChangedListener listener){
    }

    
    
    public void remove(InputChangedListener listener){ 
    }

    
    
    
    public String getTitle(){
      return aplicacion.getI18nString("LoadExternalLayersPanel.title");
    }
    

    public String getID(){
      return localId;
    }
    

    public String getInstructions(){
      return aplicacion.getI18nString("LoadExternalLayersPanel.instructions");
    }

    
    public boolean isInputValid(){
      return true;
    }
    

    public void setWizardContext(WizardContext wd){
      wizardContext = wd;
    }
    

   
    public void setNextID(String nextID){
    	this.nextID=nextID;
    }
    
    
    
    public String getNextID(){
      return nextID;
    }
    
    

    
    public void exiting(){
    }


	
	
	
}
