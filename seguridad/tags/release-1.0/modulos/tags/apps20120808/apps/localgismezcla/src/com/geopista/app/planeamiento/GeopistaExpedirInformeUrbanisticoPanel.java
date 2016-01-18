
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


package com.geopista.app.planeamiento;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.reports.GeopistaInformeUrbanistico;

import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaExpedirInformeUrbanisticoPanel extends JPanel implements WizardPanel
{
  private JPanel pnlVentana = new JPanel();
  private JButton btnSiguiente = new JButton();
  private JButton btnCancelar = new JButton();
  private JOptionPane opCuadroDialogo;
  private JLabel lblImagen = new JLabel();
  private JPanel pnlGeneral = new JPanel();
  private JPanel pnlRustico = new JPanel();
  private JPanel pnlUrbano = new JPanel();
  private JLabel lblPoligonoRustico = new JLabel();
  private JTextField txtPoligonoRustico = new JTextField();
  private JLabel lblParcelaRustico = new JLabel();
  private JTextField txtParcela = new JTextField();
  private JTextField txtParcelaUrbano = new JTextField();
  private JLabel lblParcelaUrbano = new JLabel();
  private JTextField txtPoligonoUrbano = new JTextField();
  private JLabel lblPoligonoUrbano = new JLabel();
  private GeopistaEditor geopistaEditor1 = null;
  private int numeroParcela=-1;
  private String referencia="";
  private String tipoReferencia ="";
  private JOptionPane CuadroDialogo;
  private String rutaMapa="";
  private boolean acceso;
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

  public GeopistaExpedirInformeUrbanisticoPanel()
  {
    try
    {
  //    jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception
  {
    ApplicationContext permiso = AppContext.getApplicationContext();
    
    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.Expedir.Informe.Urbanistico");
    acceso = permiso.checkPermission(geopistaPerm,"Planeamiento");

   
    com.geopista.app.help.HelpLoader.installHelp("ayuda.hs","planeamientoExpedirInformeUrbanistico01",this);

    this.setLayout(null);
  	this.setSize(750,600);
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));

    pnlGeneral.setBounds(new Rectangle(135, 20, 600, 490));
    pnlGeneral.setBorder(BorderFactory.createTitledBorder(""));
    pnlGeneral.setLayout(null);
    geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
    geopistaEditor1.showLayerName(false);
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
    
    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
    
    pnlRustico.setBounds(new Rectangle(5, 390, 555, 45));
    pnlRustico.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("parcelas.rustica")));
    pnlRustico.setLayout(null);
    pnlUrbano.setBounds(new Rectangle(5, 440, 555, 45));
    pnlUrbano.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("parcelas.urabana")));
    pnlUrbano.setLayout(null);
    lblPoligonoRustico.setText(aplicacion.getI18nString("texto.poligono"));
    lblPoligonoRustico.setBounds(new Rectangle(10, 15, 65, 20));

    txtPoligonoRustico.setBounds(new Rectangle(85, 15, 200, 20));
    txtPoligonoRustico.setHorizontalAlignment(JTextField.RIGHT);

    lblParcelaRustico.setText(aplicacion.getI18nString("informe.patrimonio.inventario.parcela"));
    lblParcelaRustico.setBounds(new Rectangle(295, 15, 55, 20));

    txtParcela.setBounds(new Rectangle(360, 15, 185, 20));
    txtParcela.setHorizontalAlignment(JTextField.RIGHT);

    txtParcelaUrbano.setBounds(new Rectangle(360, 15, 185, 20));
    txtParcelaUrbano.setHorizontalAlignment(JTextField.RIGHT);
    txtParcelaUrbano.setText("");
    lblParcelaUrbano.setText(aplicacion.getI18nString("informe.patrimonio.inventario.parcela"));
    lblParcelaUrbano.setBounds(new Rectangle(295, 15, 55, 20));

    txtPoligonoUrbano.setBounds(new Rectangle(85, 15, 200, 20));
    txtPoligonoUrbano.setHorizontalAlignment(JTextField.RIGHT);
    txtPoligonoUrbano.setText("");

    lblPoligonoUrbano.setText(aplicacion.getI18nString("texto.manzana"));
    lblPoligonoUrbano.setBounds(new Rectangle(10, 15, 65, 20));
    geopistaEditor1.setBounds(new Rectangle(150, 30, 540, 370));

    pnlUrbano.add(lblPoligonoUrbano, null);
    pnlUrbano.add(txtPoligonoUrbano, null);
    pnlUrbano.add(lblParcelaUrbano, null);
    pnlUrbano.add(txtParcelaUrbano, null);

    pnlGeneral.add(pnlUrbano, null);

    pnlRustico.add(txtParcela, null);
    pnlRustico.add(lblParcelaRustico, null);
    pnlRustico.add(txtPoligonoRustico, null);
    pnlRustico.add(lblPoligonoRustico, null);

    pnlGeneral.add(pnlRustico, null);

    /*
    GeopistaLayer  layer04 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.calificacion.suelo"),"Planeamiento");
    layer04.setActiva(false);
    layer04.setVisible(true);

    GeopistaLayer layer05 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.ambitos.planeamiento"),"Planeamiento");
    layer05.setActiva(false);
    layer05.setVisible(true);

    GeopistaLayer layer06 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.clasificacion.suelo"),"Planeamiento");
    this.setSize(new Dimension(527, 548));
    layer06.setActiva(false);
    layer06.setVisible(true);

    GeopistaLayer layer07 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.ambitos.gestion"),"Planeamiento");
    this.setSize(new Dimension(527, 548));
    layer07.setActiva(false);
    layer07.setVisible(true);

    GeopistaLayer layer08 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.tabla.planeamiento"),"Planeamiento");
    this.setSize(new Dimension(674, 548));
    layer08.setActiva(false);
    layer08.setVisible(true);


    GeopistaLayer layer09 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.catastro.parcela"),"Planeamiento");
    this.setSize(new Dimension(527, 548));
    layer09.setActiva(true);
    layer09.setVisible(true);
    */
    geopistaEditor1.loadMap(aplicacion.getString("url.mapa.planeamiento.informe.urbanistico"));
    GeopistaLayer layer09 = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer("parcelas");
    layer09.setActiva(true);
    
    this.add(geopistaEditor1, null);
    this.add(pnlGeneral, null);
    this.add(btnCancelar, null);
    this.add(btnSiguiente, null);
    this.add(lblImagen, null);

  geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(AbstractSelection abtractSelection)
      {
         // System.out.println("Recibiendo en cliente evento de cambio de seleccion de feature: "+abtractSelection.getSelectedItems());
         //Capturamos la parcela y la manzana del gml
         try {
          ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("parcelas"));
          Iterator featuresCollectionIter = featuresCollection.iterator();
          if(!featuresCollectionIter.hasNext()) return;
          Feature actualFeature = (Feature) featuresCollectionIter.next();
          

          //Tabla ambitos_gestion:

          numeroParcela = Integer.parseInt(actualFeature.getString(1));//nombre del campo
          geopistaEditor1.zoomTo(actualFeature);


          wizardContext.inputChanged();
             BufferedImage imagen = (BufferedImage)GeopistaUtil.printMap(370,500,  geopistaEditor1.getLayerViewPanel());
             String ruta = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,true);

             String s = String.valueOf(System.currentTimeMillis());             
             File fichero  = new File(ruta,"mapaPlaneamiento_"+s+".jpg");
             ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
             rutaMapa = fichero.getPath();

             fichero.deleteOnExit();
             
          //Recuperar el valor del tipo y colocarlo en su parcela corresondiente
          referencia = actualFeature.getString(2) ; //La Referencia Catastral
          tipoReferencia = actualFeature.getString(5); //El Tipo

          if (tipoReferencia.equals("U"))
            {

             txtPoligonoUrbano.setText(referencia.substring(0,5));
             txtParcelaUrbano.setText(referencia.substring(5,7) );
             txtParcela.setText(null);
             txtPoligonoRustico.setText(null);

            }else{

            }

          }catch (Exception e){
              e.printStackTrace();
          }

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


  }
 public void enteredFromLeft(Map dataMap)
  {
    try {
        jbInit();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      
    	Image imagen = GeopistaUtil.printMap(500,250,  geopistaEditor1.getLayerViewPanel());
    	String ruta = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,true);;
    	String s = String.valueOf(System.currentTimeMillis());
    	File fichero  = new File(ruta,"mapaPlaneamiento_"+s+".jpg");    	
    	ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
    	String rutaMapa = fichero.getPath();
    	
    	GeopistaInformeUrbanistico geo = new GeopistaInformeUrbanistico(this.numeroParcela,imagen);    	
    
        fichero.deleteOnExit();
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
     private WizardContext wizardContext;
    public void setWizardContext(WizardContext wd)
    {
      wizardContext=wd;
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
        Collection lista = null;
        lista = geopistaEditor1.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("parcelas"));
        if (lista.size()==1)
        if (acceso) {
            return true;
           }
           else{
            return false;
           }
          
        else
        
          return false;
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



    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}

