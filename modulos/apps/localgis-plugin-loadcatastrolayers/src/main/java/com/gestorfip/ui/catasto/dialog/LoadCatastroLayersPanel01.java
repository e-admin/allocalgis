/**
 * LoadCatastroLayersPanel01.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.ui.catasto.dialog;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.server.administradorCartografia.ACLayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.IACLayerFamily;
import com.geopista.server.administradorCartografia.IAdministradorCartografiaClient;
import com.geopista.ui.dialogs.LayerFamilyRenderer;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class LoadCatastroLayersPanel01  extends JPanel implements WizardPanel
{
 
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardLayers  = aplicacion.getBlackboard();
  private String localId = null;
  private WizardContext wizardContext;
  private PlugInContext context;
 
  private String nextID = null;
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JList jList1 = new JList();

  public LoadCatastroLayersPanel01(String id, String nextId, PlugInContext context)
  {
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

  }

  private void jbInit() throws Exception
  {



    if(!aplicacion.isLogged())
    {
         aplicacion.setProfile("Geopista");
         aplicacion.login();
    }
    this.setLayout(null);
    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
            context.getErrorHandler());
    progressDialog.setTitle(aplicacion.getI18nString("BuscandoLayerFamilies"));
    progressDialog.report(aplicacion.getI18nString("BuscandoLayerFamilies"));
    progressDialog.addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent e) {
            //Wait for the dialog to appear before starting the task. Otherwise
            //the task might possibly finish before the dialog appeared and the
            //dialog would never close. [Jon Aquino]
            new Thread(new Runnable(){
                public void run()
                {
                    try
                    {
                    	String sUrlPrefix = aplicacion.getString(Constantes.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
                		AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);
                		
                        IACLayerFamily[] layerFamiliesIds = administradorCartografiaClient.getLayerFamilies();
                        Collection layerFamiliesIdsCollection = new ArrayList();
                        
                        jScrollPane1.setBounds(new Rectangle(65, 40, 280, 205));

                        DefaultListModel layerFamiliesModel = new DefaultListModel();
                        jList1.setCellRenderer(new LayerFamilyRenderer());
                        jList1.setModel(layerFamiliesModel);

                        Comparator layerFamilesComparator = new Comparator() {
                            public int compare(Object o1, Object o2) {
                                return ((((ACLayerFamily) o1).getName()).toUpperCase()).compareTo((((ACLayerFamily) o2).getName()).toUpperCase());
                            }
                        };

                        Arrays.sort(layerFamiliesIds,layerFamilesComparator);

                        for(int n=0; n<layerFamiliesIds.length; n++)
                        {
                           layerFamiliesModel.addElement(layerFamiliesIds[n]);
                        }

                        
                    }catch(Exception e)
                    {
                        
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
    
    jScrollPane1.getViewport().add(jList1, null);
    this.add(jScrollPane1, null);
        
    
  }


  public void enteredFromLeft(Map dataMap)
  {
 
  

    
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      Object[] layerFamiliesIDs =  (Object[]) jList1.getSelectedValues();
      ArrayList layerFamiliesIDsList = new ArrayList();
      for(int n=0;n<layerFamiliesIDs.length;n++)
      {
        layerFamiliesIDsList.add(layerFamiliesIDs[n]);
        
      }

      blackboardLayers.put("SelectedLayerFamilies",layerFamiliesIDsList);
      
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
      
    }

    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return aplicacion.getI18nString("LoadSystemLayersPanel01Title");
    }

    public String getID()
    {
      return localId;
    }

    public String getInstructions()
    {
      return aplicacion.getI18nString("LoadSystemLayersPanel01Instructions");
    }

    public boolean isInputValid()
    {
      return true;
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */



    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }


	
	
	
}
