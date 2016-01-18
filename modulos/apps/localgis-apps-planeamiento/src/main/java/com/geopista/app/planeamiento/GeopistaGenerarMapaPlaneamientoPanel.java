/**
 * GeopistaGenerarMapaPlaneamientoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.planeamiento;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaGenerarMapaPlaneamientoPanel extends JPanel implements WizardPanel
{
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));

  private JPanel pnlVentana = new JPanel();
  private JLabel lblOpcion = new JLabel();
  private JOptionPane opCuadroDialogo;
  private JLabel lblImagen = new JLabel();
  private JPanel pnlGeneral = new JPanel();
  private JTextField txtPlaneamiento = new JTextField();
//  private GeopistaEditor geopistaEditor1 = new GeopistaEditor();


  public GeopistaGenerarMapaPlaneamientoPanel()
  {
    try
    {
      //jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }


  public void jbInit() throws Exception
  {

    this.setLayout(null);
    this.setSize(new Dimension(771, 539));
	
    ApplicationContext permiso = AppContext.getApplicationContext();

    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.Mapa Planeamiento");
    boolean acceso = permiso.checkPermission(geopistaPerm,"Planeamiento");

	if (!acceso) {         
 	   JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoPermisos"));
 	   wizardContext.cancelWizard();
 	   return ;
 	   
      }

  
	lblOpcion.setText("Previsualización:");
    lblOpcion.setBounds(new Rectangle(135, 10, 375, 30));

    //Iniciamos la ayuda
            try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento01",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda

   

    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));

    pnlGeneral.setBounds(new Rectangle(135, 35, 600, 475));
    pnlGeneral.setBorder(BorderFactory.createTitledBorder(""));
    pnlGeneral.setLayout(null);
    geopistaEditor1.setBounds(new Rectangle(5, 5, 580, 395));
    geopistaEditor1.showLayerName(true);
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
   
    ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);

//    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

    pnlGeneral.add(geopistaEditor1, null);
    pnlGeneral.add(txtPlaneamiento, null);

    String rutaMapa =  aplicacion.getString("url.mapa.planeamiento");
      try{
      geopistaEditor1.loadMap(rutaMapa);
      }
      catch(Exception a)
      {
        a.printStackTrace();
      }


 
    txtPlaneamiento.setBounds(new Rectangle(0, 410, 585, 20));


    this.add(pnlGeneral, null);
    this.add(lblOpcion, null);
    this.add(lblImagen, null);


    geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(IAbstractSelection abtractSelection)
      {
          System.out.println("Recibiendo en cliente evento de cambio de seleccion de feature: "+abtractSelection.getSelectedItems());

      }

      public void featureAdded(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de nueva Feature: "+e.getType());
      }

      public void featureRemoved(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de borrado de Feature: "+e.getType());

      }

      public void featureModified(FeatureEvent e)
      {
        System.out.println("Recibiendo en cliente evento de Modificacion de Feature: "+e.getType());
      }

      public void featureActioned(IAbstractSelection abtractSelection)
      {
        System.out.println("Recibiendo en cliente evento de cambio de accion en feature: "+abtractSelection.getSelectedItems());
      }

    });
  }//jbinit


 public void enteredFromLeft(Map dataMap)
  {
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     System.out.println("saliendo de panel 1"); 
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
      return "";
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }


    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return nextID;
    } 
    
    private WizardContext wizardContext;
      public void setWizardContext(WizardContext wd)
    {
    	  wizardContext=wd;
    }

  private void btnImprimir_actionPerformed(ActionEvent e)
  {
      GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
      GeopistaUtil.executePlugIn(geopistaPrint, geopistaEditor1.getContext(),new TaskMonitorManager());
  
  }

 
public GeopistaEditor getGeopistaEditor(){
  return geopistaEditor1;
}


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
 
}

