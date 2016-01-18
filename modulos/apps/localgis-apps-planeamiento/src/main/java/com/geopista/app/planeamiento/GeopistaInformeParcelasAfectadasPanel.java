/**
 * GeopistaInformeParcelasAfectadasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.planeamiento;


import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.GeopistaParcelasAfectadas;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaInformeParcelasAfectadasPanel extends JPanel implements WizardPanel
{
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private JPanel pnlVentana = new JPanel();
  private JLabel lblOpcion = new JLabel();
  private JOptionPane opCuadroDialogo;
  private JLabel lblImagen = new JLabel();
  private JPanel pnlGeneral = new JPanel();
  private JComboBox cmbAmbito = new JComboBox();
  private JLabel lblGestion = new JLabel();
  private String numeroAmbito="";
  private String rutaMapa="";
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getI18nString("fichero.patrimonio"));
  private boolean acceso;
 

  public GeopistaInformeParcelasAfectadasPanel()
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

    GeopistaPermission paso = new GeopistaPermission("Geopista.Planeamiento.Informe Parcelas Afectadas");
    acceso = aplicacion.checkPermission(paso,"Planeamiento");

    if(!acceso){
        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoPermisos"));
        wizardContext.cancelWizard();
        return;
    }

    try{
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda
   hb.enableHelpKey(this,"planeamientoInformeParcelasAfectadas01",hs); 
  }catch (Exception excp){ }
  
    
    lblOpcion.setBounds(new Rectangle(135, 10, 375, 30));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
    pnlGeneral.setBounds(new Rectangle(135, 35, 600, 475));
    pnlGeneral.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    pnlGeneral.setLayout(null);


    cmbAmbito.setBounds(new Rectangle(170, 440, 230, 20));
    cmbAmbito.setBackground(Color.white);


    ArrayList ListaAmbitos=new ArrayList();
   GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();
  
  try {
    ListaAmbitos = conexion.leerNumeroAmbitos();
    Iterator i = ListaAmbitos.iterator();
    while (i.hasNext()){
      this.cmbAmbito.addItem((String) i.next());
    }
    cmbAmbito.setSelectedIndex(-1);
    
  }catch(Exception e){
    e.printStackTrace();
  }
    cmbAmbito.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt){
     //Buscamos el atributo en la feature 

            GeopistaLayer capaAmbitos = (GeopistaLayer)geopistaEditor1.getLayerManager().getLayer("ambitos_gestion");
            
            Collection f = geopistaEditor1.searchByAttribute("ambitos_gestion","Número",(String) cmbAmbito.getSelectedItem());
            
            
            geopistaEditor1.getLayerViewPanel().getSelectionManager().clear();
            
            if (!f.isEmpty()){
	            Feature ambitos = (Feature) ((ArrayList)f).get(0);
	            geopistaEditor1.select(capaAmbitos,ambitos);
	            geopistaEditor1.zoomToSelected();
	            numeroAmbito= ambitos.getString("Número");
	            
            }else{
              JOptionPane.showMessageDialog((Component)aplicacion.getMainFrame(),aplicacion.getI18nString("valor.ambito.gestion.no.disponible"));

              //System.out.println("No existe ningun ambito del tip en plano");      
            }
            	
        
            wizardContext.inputChanged(); 
        };

      });
    
    lblGestion.setText("Ambito de Gestión:");
    lblGestion.setBounds(new Rectangle(5, 440, 155, 20));
    geopistaEditor1.setBounds(new Rectangle(10, 10, 580, 415));

    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));

    pnlGeneral.setBounds(new Rectangle(135, 20, 600, 490));
    pnlGeneral.setBorder(BorderFactory.createTitledBorder(""));
    pnlGeneral.setLayout(null);

  //No mostrar los nombres de las capas
    geopistaEditor1.showLayerName(true);
    
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");

    ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
    geopistaEditor1.loadMap(aplicacion.getString("url.mapa.planeamiento")); 
    /*GeopistaLayer layer04 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.clasificacion.suelo"),aplicacion.getI18nString("informe.parcelas.afectadas.planeamiento1"));
    layer04.setActiva(false);
    layer04.setVisible(true);

    GeopistaLayer layer05 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.catastro.parcela"),aplicacion.getI18nString("informe.parcelas.afectadas.planeamiento1"));
    layer05.setActiva(false);
    layer05.setVisible(true);
    
    GeopistaLayer layer06 = (GeopistaLayer) geopistaEditor1.loadData(aplicacion.getString("url.capas.planeamiento.ambitos.gestion"),aplicacion.getI18nString("informe.parcelas.afectadas.planeamiento1"));
    layer06.setActiva(true);
    layer06.setVisible(true);*/

    ////Zoom a la capa seleccionada
    //geopistaEditor1.getContext().getLayerViewPanel().getViewport().zoomToFullExtent(); 
    //geopistaEditor1.getContext().getLayerViewPanel().getViewport().zoom(layer06.getFeatureCollectionWrapper().getEnvelope());
    //geopistaEditor1.getContext().getLayerViewPanel().getViewport().zoom(geopistaEditor1.getContext().getLayerManager().getLayer("ambitos_gestion").getFeatureCollectionWrapper().getEnvelope());  
    //Debemos poner la capa de ambitos de gestion como activa
    
    //Recorrer todas las features del Layer08.
    GeopistaLayer  capaTramosVias = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer("ambitos_gestion");
    capaTramosVias.setActiva(true);
    
    this.setSize(750,600);
    pnlGeneral.add(geopistaEditor1, null);

    pnlGeneral.add(lblGestion, null);
    pnlGeneral.add(cmbAmbito, null);
    this.add(pnlGeneral, null);
    this.add(lblOpcion, null);
    this.add(lblImagen, null);



geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(IAbstractSelection abtractSelection)
      {
          //Colocaremos el valor de la selección en el combo.
           try {
              ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("ambitos_gestion"));
              Iterator featuresCollectionIter = featuresCollection.iterator();
              if(!featuresCollectionIter.hasNext()) return;
              Feature actualFeature = (Feature) featuresCollectionIter.next();
               
           
          //Tabla ambitos_gestion:
         
           numeroAmbito = (actualFeature.getString("Número"));//nombre del campo
          for (int i=0; i<cmbAmbito.getItemCount();i++)
            {
              if (cmbAmbito.getItemAt(i).equals(numeroAmbito)){
                  cmbAmbito.setSelectedIndex(i);
                break;
              }  
            }
          
          geopistaEditor1.zoomTo(actualFeature);

//        geopistaEditor1.getContext().getLayerViewPanel().getViewport().zoom(geopistaEditor1.getContext().getLayerManager().getLayer("ambitos_gestion").getFeatureCollectionWrapper().getEnvelope());  
    //      layer06.getFeatureCollectionWrapper().getEnvelope()
          //Que se haya seleccionado algo con el mapa
          wizardContext.inputChanged();

           
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

      public void featureActioned(IAbstractSelection abtractSelection)
      {

      }
      
    });


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
     if (cmbAmbito.getSelectedIndex()==-1)
     {

     }else{
        //Generamos la imagen y llamamos al informe
                       
             //BufferedImage imagen = (BufferedImage) geopistaEditor1.printMap(370,200);
             Image imagen = GeopistaUtil.printMap(574,326,  geopistaEditor1.getLayerViewPanel());
             
             String ruta = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH,true);;
             String s = String.valueOf(System.currentTimeMillis());
            
             File fichero = new File(ruta,"mapaParcelasAfectadas_"+s+".jpg");
             ImageIO.write( (BufferedImage) imagen,"JPG",fichero);
             rutaMapa = fichero.getPath(); 
             GeopistaParcelasAfectadas geo = new GeopistaParcelasAfectadas(this.numeroAmbito,rutaMapa); 
             fichero.deleteOnExit();
             
                 
     }
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
        lista = geopistaEditor1.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(geopistaEditor1.getLayerManager().getLayer("ambitos_gestion"));
        if (lista.size()==1)    
           {
              //Comprobamos que hay parcelas debajo de la capa
              GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();
               ArrayList tmpParcelas = conexion.parcelasAfectadasAmbitoGestion(this.numeroAmbito);
               if (tmpParcelas.size()==0){
            	   //cmbAmbito.setEnabled(true);
   					JOptionPane.showMessageDialog(this,"No hay parcelas afectadas para este informe");
                  return false; 
                  
               }else{
                if (acceso){ 
	                cmbAmbito.setEnabled(true);
	                return true;
                }else{
                	cmbAmbito.setEnabled(false);
                
                return false;
                }
               }
           } else{
              return false;
          }
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




  private void siguientebtn_actionPerformed(ActionEvent e)
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

