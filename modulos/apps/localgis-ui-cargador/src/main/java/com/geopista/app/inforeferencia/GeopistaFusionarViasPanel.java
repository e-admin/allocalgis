/**
 * GeopistaFusionarViasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.analysis.GeopistaFusionViasStandalonePlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

/**
 * GeopistaFusionarViasPanel
 * Panel que carga el mapa de inforeferencia para fusionar los tramos de via 
 */

public class GeopistaFusionarViasPanel extends JPanel implements WizardPanel
{
  ApplicationContext appContext=AppContext.getApplicationContext();
  private JPanel pnlMap = new JPanel();
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor();
  private JLabel lblImagen = new JLabel();
  public String myID;
  public String siguiente;
  private boolean acceso;
  
  public GeopistaFusionarViasPanel(String myID, String siguiente)
  {
      this.myID = myID;
      this.siguiente = siguiente;
      
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
    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.InfReferencia.FusionTramosVias");
    acceso = appContext.checkPermission(geopistaPerm,"Informacion de Referencia");
    if(acceso){
              this.setLayout(null);
              this.setSize(new Dimension(800, 600));
              pnlMap.setBounds(new Rectangle(25, 20, 768, 640));
              pnlMap.setLayout(null);
              geopistaEditor1.setSize(new Dimension(512, 512));

              geopistaEditor1.showLayerName(false);
              geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
              geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
              geopistaEditor1.addCursorTool("Measure", "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");
              geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
              ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
              geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
    
              //GeopistaFusionViasPlugIn geopistaFusionViasPlugIn = new GeopistaFusionViasPlugIn();
              GeopistaFusionViasStandalonePlugIn geopistaFusionViasPlugIn = new GeopistaFusionViasStandalonePlugIn();
              geopistaEditor1.addPlugIn(geopistaFusionViasPlugIn);
    
              lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
              lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
              lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    

    
   
              // TODO: CAMBIAR A CARGAR MAPA
              geopistaEditor1.loadMap(appContext.getString("url.mapa.inforeferencia"));
              //Fin cargar mapa

              geopistaEditor1.setVisible(true);
              geopistaEditor1.setBounds(150,0,512,512);
              geopistaEditor1.setPreferredSize(new Dimension(512, 512));
              
              try {
      			//Hacemos zoom a la maxima extension
      			WorkbenchContext wb=geopistaEditor1.getContext();
      			PlugInContext plugInContext = wb.createPlugInContext();
      			plugInContext.getLayerViewPanel().getViewport().zoomToFullExtent();
      		} catch (NoninvertibleTransformException e1) {

      		}
              
              pnlMap.add(lblImagen, null);
              pnlMap.add(geopistaEditor1, null);
              this.add(pnlMap, null);
    }else{}
     }

public void enteredFromLeft(Map dataMap)
  {

  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     //System.out.println("saliendo de panel 1");
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
      return myID;
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
      if(nextID==null)
      {
        return siguiente;
      }
      else
      {
        return nextID;
      }
    }
      public void setWizardContext(WizardContext wd)
    {
      
    }
  private void btnFinalizar_actionPerformed(ActionEvent e)
  {
   
  }

  private void btnFusionarVias_actionPerformed(ActionEvent e)
  {
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}


}