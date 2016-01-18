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


package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.geopista.app.AppContext;
import com.geopista.app.planeamiento.GeopistaPlaneamientoInformesPostgreCon;
import com.geopista.app.reports.GeopistaParcelasAfectadas;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaSigapPanel  extends JPanel implements WizardPanel
{  
  //PARA EL CONTROL DE JUAN
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getI18nString("fichero.patrimonio"));

  public JLabel eventLabel = new JLabel();
  public JPanel eventPanel = new JPanel(); 
  Layer layer09 = null;
  private boolean acceso;
  private JScrollPane jScrollPane1 = new JScrollPane();
  //private JButton btnAnterior = new JButton();
  //private JButton btnCancelar = new JButton();
  private JOptionPane CuadroDialogo;
 

  private JScrollPane mapasp = new JScrollPane();
  private JLabel lblMapa = new JLabel();
  private JPanel pnlSuperficies = new JPanel();
  private JLabel lblReferneciaCatastral = new JLabel();
  private JSeparator jSeparator1 = new JSeparator();
  private JTextField txtReferencia = new JTextField();

 public GeopistaSigapPanel () {


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
    GeopistaPermission paso = new GeopistaPermission("Geopista.Patrimonio.Diferentes.Superficies");
    acceso = aplicacion.checkPermission(paso,"Patrimonio");

       try{
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda
   hb.enableHelpKey(this,"PatrimonioFichasInventario",hs); 
  }catch (Exception excp){ }
  
    this.setLayout(null);
  //


geopistaEditor1.setBounds(new Rectangle(10, 0, 720,450));

   this.add(geopistaEditor1,null);
   // this.add(btnCancelar, null);
   // this.add(btnAnterior, null);

   geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn");
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");



    this.setLocation(150,50);
    this.setVisible(true);    
    this.add(pnlSuperficies, null);
   //CuadroDialogo.showMessageDialog(this,"Vd. no tiene acceso");

   
    geopistaEditor1.loadMap(aplicacion.getString("url.mapa.catastro"));
    GeopistaLayer layer09 = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer("parcelas");
    layer09.setActiva(true);
    layer09.setVisible(true);


    geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(AbstractSelection abtractSelection)
      {
        //tengo que obtener las features y calcular su extension
          ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("parcelas"));
          Iterator featuresCollectionIter = featuresCollection.iterator();
          if(!featuresCollectionIter.hasNext()) return;
          Feature actualFeature = (Feature) featuresCollectionIter.next();
          //Obtenemos la referencia Catastral y la buscamos con una sql
          String referencia  = actualFeature.getString("Referencia catastral");
          GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();
          txtReferencia.setText(referencia);


      }

      public void featureAdded(FeatureEvent e)
      {
      }

      public void featureRemoved(FeatureEvent e)
      {
      }

      public void featureModified(FeatureEvent e)
      {
      }

      public void featureActioned(AbstractSelection abtractSelection)
      {
      }

    });


    pnlSuperficies.setBounds(new Rectangle(10, 450, 720, 80));
    pnlSuperficies.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    pnlSuperficies.setLayout(null);

    lblReferneciaCatastral.setText("Referencia Catastral");
    lblReferneciaCatastral.setBounds(new Rectangle(90, 10, 195, 20));
    jSeparator1.setBounds(new Rectangle(80, 0, 25, 80));
    jSeparator1.setOrientation(SwingConstants.VERTICAL);
    txtReferencia.setBounds(new Rectangle(310, 5, 210, 25));


    this.setSize(750,600);
    pnlSuperficies.add(txtReferencia, null);
    pnlSuperficies.add(lblReferneciaCatastral, null);
    pnlSuperficies.add(jSeparator1, null);
   //CuadroDialogo.showMessageDialog(this,"Vd. no tiene acceso");

  }//jbinit

private void btnAnterior_actionPerformed(ActionEvent e)
  {

  }

  private void btnCancelar_actionPerformed(ActionEvent e)
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
    if (acceso){
      return true;
     }else{
      return false;
      }
    }

    private WizardContext wizardContext;
   public void setWizardContext(WizardContext wd)
    {
      wizardContext=wd;
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

 public void remove(InputChangedListener listener)
    {
      
    }

   public void add(InputChangedListener listener)
    {
      
    }

    public void enteredFromLeft(Map dataMap)
  {
    //Cargaremos en el combo los valores de los ambitos de Gestión.

    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        //  Lanzamos el informe de sigap con el id bien
        //  Analizar el código de la referencia de catastral
        //  
    	
    	Image imagen = GeopistaUtil.printMap(500,250,  geopistaEditor1.getLayerViewPanel());
    	String ruta = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,true);;
        String s = String.valueOf(System.currentTimeMillis());
        File fichero  = new File(ruta,"patrimonio_"+s+".jpg");    	
    	ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
    	String rutaMapa = fichero.getPath();
    	
        String refpar = this.txtReferencia.getText().substring(0,7);
        String refpla = this.txtReferencia.getText().substring(7,14);

        //buscamos en la tabla inmuebles los que coincidan con la refpar reflpa
          GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();
          ArrayList idBienes = conexion.buscarIdPatrimonio(refpar,refpla);
         GeopistaFichaLibroInventario geo = new GeopistaFichaLibroInventario(idBienes,rutaMapa);
         fichero.deleteOnExit();
        
     }



    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}// De la clase




