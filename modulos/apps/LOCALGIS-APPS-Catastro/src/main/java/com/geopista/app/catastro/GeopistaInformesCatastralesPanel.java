/**
 * GeopistaInformesCatastralesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.GeopistaInformeCatastral;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;
public class GeopistaInformesCatastralesPanel extends JPanel implements WizardPanel
{
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
  private boolean acceso;
  public JLabel eventLabel = new JLabel();
  public JPanel eventPanel = new JPanel();
  private int numeroParcela=-1;
  private String referencia="";
  private String tipoReferencia ="";
  private JOptionPane CuadroDialogo;

  Layer layer09 = null;
  
  private JPanel pnlRadio = new JPanel();
  private JRadioButton rdbUrbana = new JRadioButton();
  private JRadioButton rdbRustica = new JRadioButton();
  private JLabel lblIdentificacion = new JLabel();
  private JLabel lblUrbana = new JLabel();
  private JLabel lblRustica = new JLabel();
  private JLabel lblRefUrbana= new JLabel();
  private JLabel lblRefRustica= new JLabel();
  private JTextField txtRefUrbana= new JTextField();
  private JTextField txtRefRustica = new JTextField();
  private JLabel lblImagen = new JLabel();
  private String rutaMapa="";
  

  public GeopistaInformesCatastralesPanel()
  {
    try
    {
    try{
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda
   hb.enableHelpKey(this,"catastroGeneracionInformesCatastrales",hs); 
  }catch (Exception excp){ }
  
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setSize(new Dimension(527, 548));
    
    GeopistaPermission paso = new GeopistaPermission("Geopista.Generacion.Informes.Catastrales");
    aplicacion.setProfile("Geopista");
    acceso=aplicacion.checkPermission(paso,"Catastro");
    
    geopistaEditor1.setBounds(new Rectangle(150, 80, 580, 450));
    this.add(lblIdentificacion, null);
    this.add(lblRefUrbana, null);
    this.add(lblRefRustica, null);
    this.add(txtRefRustica, null);

    

    this.add(pnlRadio, null);
    
    this.add(geopistaEditor1, null);
    this.add(lblRustica, null);
    this.add(rdbRustica, null);
    this.add(rdbUrbana, null);
    this.add(lblUrbana, null);
    this.add(txtRefUrbana, null);
    this.add(lblImagen);
    
    lblImagen.setBounds(new Rectangle(25, 15, 110, 500));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    

    geopistaEditor1.showLayerName(false);
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");

    ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);

    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");


    this.setLocation(150,50);
    this.setVisible(true);

    // DEFINICION DE CAPAS

    
/*    GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.catastro.parcela"),"Catastro");
    layer01.setActiva(true);
    layer01.setVisible(true);
    layer01.addStyle(new BasicStyle(new Color(255,255,128)));*/

    geopistaEditor1.loadMap(aplicacion.getString("url.mapa.catastro"));
    GeopistaLayer layer09 = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer("parcelas");
    layer09.setActiva(true);
    layer09.setVisible(true);




    lblIdentificacion.setText( aplicacion.getI18nString("parcelas.identificacion.parcela"));
    lblIdentificacion.setBounds(new Rectangle(145, 10, 275, 20));
    
    lblRefUrbana.setBounds(new Rectangle(240, 30, 80, 25));
    lblRefUrbana.setText(aplicacion.getI18nString("parcelas.referencia"));
    lblRefRustica.setBounds(new Rectangle(240, 60, 105, 15));
    lblRefRustica.setText(aplicacion.getI18nString("parcelas.referencia"));

    txtRefRustica.setEnabled(false);
    txtRefUrbana.setEnabled(false);
    txtRefRustica.setBounds(new Rectangle(305, 55, 200, 20));
    txtRefUrbana.setBounds(new Rectangle(305, 30, 200, 20));

    //PANEL GROUP
    ButtonGroup grupo= new ButtonGroup(); 
    txtRefUrbana.addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e){};
        public void focusLost(FocusEvent e){
            //Buscamos el atributo en la feature 
            
            Layer capaParcela = geopistaEditor1.getLayerManager().getLayer("parcelas");
            Collection f =  geopistaEditor1.searchByAttribute("parcelas","Referencia catastral",txtRefUrbana.getText());
            geopistaEditor1.getLayerViewPanel().getSelectionManager().clear();
            Feature parcela = (Feature) ((ArrayList)f).get(0);
            geopistaEditor1.select(capaParcela,parcela);
            geopistaEditor1.zoomToSelected();
            numeroParcela= Integer.parseInt(parcela.getString("ID de parcela"));
         try {
             Image imagen = GeopistaFunctionUtils.printMap(370,500,  geopistaEditor1.getLayerViewPanel());
             String ruta = aplicacion.getString(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY);
             String s = String.valueOf(System.currentTimeMillis());
             File fichero  = new File(ruta,"mapaCatastro_"+s+".jpg");
             ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
             rutaMapa = fichero.getPath(); 
             fichero.deleteOnExit();
            }catch (Exception ex){ex.printStackTrace();}
            wizardContext.inputChanged();
        };
      });
    lblUrbana.setBounds(new Rectangle(185, 30, 50, 20));
    rdbUrbana.setBounds(new Rectangle(160, 30, 20, 20));
    rdbRustica.setBounds(new Rectangle(160, 55, 25, 20));
    lblRustica.setBounds(new Rectangle(185, 60, 50, 15));
     txtRefRustica.addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e){};
        public void focusLost(FocusEvent e){
            //Buscamos el atributo en la feature 
            
            Layer capaParcela = geopistaEditor1.getLayerManager().getLayer("parcelas");
            Collection f =  geopistaEditor1.searchByAttribute("parcelas","Referencia catastral",txtRefRustica.getText());
            geopistaEditor1.getLayerViewPanel().getSelectionManager().clear();
            Feature parcela = (Feature) ((ArrayList)f).get(0);
            geopistaEditor1.select(capaParcela,parcela);
            geopistaEditor1.zoomToSelected();
            numeroParcela= Integer.parseInt(parcela.getString("ID de parcela"));
         try {
             Image imagen = GeopistaFunctionUtils.printMap(370,500,  geopistaEditor1.getLayerViewPanel());
             
             String ruta = aplicacion.getString(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY);
             String s = String.valueOf(System.currentTimeMillis());             
             File fichero  = new File(ruta,"mapaCatastro_"+s+".jpg");
             
             ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
             rutaMapa = fichero.getPath(); 
             fichero.deleteOnExit();
            }catch (Exception ex){ex.printStackTrace();}
            wizardContext.inputChanged();
        };
      });

    rdbUrbana.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          rdbUrbana_actionPerformed(e);
        }
      });
      
      rdbRustica.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          rdbRustica_actionPerformed(e);
        }
      });

    pnlRadio.setBounds(new Rectangle(620, 15, 80, 80));
    lblUrbana.setText(aplicacion.getI18nString("parcelas.urabana"));
    lblRustica.setText(aplicacion.getI18nString("parcelas.rustica"));
     
    grupo.add(rdbUrbana);
    grupo.add(rdbRustica); 
    pnlRadio.setVisible(true);


 


    geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(IAbstractSelection abtractSelection)
      {
      try {
          
          ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("parcelas"));
          Iterator featuresCollectionIter = featuresCollection.iterator();
          if(!featuresCollectionIter.hasNext()) return;
          Feature actualFeature = (Feature) featuresCollectionIter.next();

      
          //Tabla ambitos_gestion:
          numeroParcela = Integer.parseInt(actualFeature.getString("ID de parcela"));//nombre del campo
          geopistaEditor1.zoomTo(actualFeature);
          
          //Recuperar el valor del tipo y colocarlo en su parcela corresondiente
          referencia = actualFeature.getString("Referencia catastral") ; 
          tipoReferencia = actualFeature.getString("Tipo");

             //BufferedImage imagen = (BufferedImage) geopistaEditor1.printMap(370,200);
             Image imagen = GeopistaFunctionUtils.printMap(370,500,  geopistaEditor1.getLayerViewPanel());

            
             String ruta = aplicacion.getString(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY);
             String s = String.valueOf(System.currentTimeMillis());
             File fichero  = new File(ruta + "mapaCatastro_"+s+".jpg");
             ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
             rutaMapa = fichero.getPath(); 
             fichero.deleteOnExit();

          
          if (tipoReferencia.equals("U"))
            {
              txtRefUrbana.setEnabled(true);
              txtRefUrbana.setText(referencia);
              rdbUrbana.doClick();
              txtRefRustica.setText(null);
              txtRefRustica.setEnabled(false);
            }else{
              txtRefUrbana.setEnabled(false);
              txtRefUrbana.setText(null);
              rdbRustica.doClick();
              txtRefRustica.setEnabled(true);
              txtRefRustica.setText(referencia);
            }

              
              wizardContext.inputChanged();
          
      }catch (Exception e){e.printStackTrace();}

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

      public void featureActioned(IAbstractSelection abtractSelection)
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

      public void exitingToRight() throws Exception
        {

       GeopistaInformeCatastral catas = new GeopistaInformeCatastral(this.numeroParcela,rutaMapa); ;
         
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
       wizardContext = wd;
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
          if (acceso){
          rdbRustica.setEnabled(true);
          rdbUrbana.setEnabled(true);
     
          return true;
          }else{
          rdbRustica.setEnabled(false);
          rdbUrbana.setEnabled(false);
          txtRefUrbana.setText(null);
          txtRefRustica.setText(null);
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

  

  private void rdbUrbana_actionPerformed(ActionEvent e)
  {
//activo el campo refecencia
    if(rdbUrbana.isSelected()== true )
    {
      
      txtRefUrbana.setEnabled(true);
      txtRefRustica.setEnabled(false);
    }    
  }

private void rdbRustica_actionPerformed(ActionEvent e)
  {
//activo el campo refecencia
    if(rdbRustica.isSelected()== true )
    {

      txtRefRustica.setEnabled(true);
      txtRefUrbana.setEnabled(false);
    }
    
 }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

}

