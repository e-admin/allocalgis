
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/


package com.geopista.ui.dialogs;
import java.awt.BorderLayout;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayerTreeModel;
import com.geopista.ui.GeopistaLayerNamePanel;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.MapDocument;
import com.geopista.util.PrintPreviewPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

public class GeopistaVistaPreliminarPanel extends JPanel implements WizardPanel
{
  
  private JSplitPane jSplitPane1 = new JSplitPane();
  private GeopistaLayerNamePanel legendPanel = null;
  private JPanel imagePanel = new JPanel();
  private String accion = null;
  private WizardContext wizardContext;
  private PlugInContext context;
  private MapDocument mapDocument = null;
  private Book bookDocument = null;
  private PageFormat pf = null;
  private PrinterJob job = null;
  private String localId = null;
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

  private Blackboard blackboardInformes  = aplicacion.getBlackboard();

  public GeopistaVistaPreliminarPanel(String id, String nextId, PlugInContext context) //throws Exception
  {
      this.nextID = nextId;
      this.localId = id;
      this.context = context;
      setName(aplicacion.getI18nString("PrintPreviewPanelTitle"));
      jbInit();
   
  }
    

  private void jbInit() //throws Exception
  {
    this.setLayout(new BorderLayout());
   
    this.add(jSplitPane1, BorderLayout.CENTER);
//    jSplitPane1.setBounds(new Rectangle(45, 40, 650, 430));
//    jSplitPane1.setBackground(new Color(212, 211, 211));
   // this.setSize(750,600);  
   
  }//jbinit
  
   /**
     * @return null to turn the Next button into a Finish button
     */
    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

     public void enteredFromLeft(Map dataMap)
    {
        accion = null;


        job = (PrinterJob) blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRJOB);
        pf = (PageFormat) blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRPF);

          LayerViewPanel layerViewPanel = context.getWorkbenchContext().getLayerViewPanel();

          //Miramos si se debe mostrar la leyenda y si no es asi no la creamos
          boolean showLegend = ((Boolean) blackboardInformes.get(GeopistaPrintPlugIn.MOSTRARLEYENDAIMPRIMIR)).booleanValue();
          legendPanel = null;
          if(showLegend)
          {
            legendPanel = new GeopistaLayerNamePanel(
                    layerViewPanel,
                    new GeopistaLayerTreeModel(layerViewPanel),
                    layerViewPanel.getRenderingManager(),
                    new HashMap());
            jSplitPane1.setLeftComponent(legendPanel);
          }
          
          jSplitPane1.setLeftComponent(legendPanel);
            
          String scale = (String) blackboardInformes.get(GeopistaPrintPlugIn.ESCALAIMPRIMIRMAPA);//"EscalaImprimirMapa");
         
          

          mapDocument = new MapDocument(legendPanel, layerViewPanel, pf, Integer.parseInt(scale));

          //Sacamos la escala a la que queremos imprimir el Mapa
          bookDocument = new Book();
          bookDocument.append(mapDocument,pf);
          
          imagePanel = new PrintPreviewPanel(bookDocument);

          
          jSplitPane1.setRightComponent(imagePanel);
          

      }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {

      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRMAPDOCUMENT,bookDocument);
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRPF,pf);  
      accion = "print";
      blackboardInformes.put(GeopistaPrintPlugIn.IMPRIMIRACCION,accion);
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
      return this.getName();
    }

    public String getID()
    {
      return localId;
    }

    public String getInstructions()
    {
    	   return aplicacion.getI18nString("VistaPreliminarPanelInstrucctions");
    	   
    }

    public boolean isInputValid()
    {
      return true;
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }


    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
}