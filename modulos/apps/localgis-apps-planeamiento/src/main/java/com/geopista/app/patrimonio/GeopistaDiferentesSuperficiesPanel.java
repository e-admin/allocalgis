/**
 * GeopistaDiferentesSuperficiesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jimm.datavision.Report;
import jimm.datavision.layout.swing.SwingLE;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.planeamiento.GeopistaPlaneamientoInformesPostgreCon;
import com.geopista.app.reports.GeopistaGeneradorListadosConexionBD;
import com.geopista.app.reports.GeopistaInformeSuperficiesNoCoincidentes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaDiferentesSuperficiesPanel extends JPanel implements WizardPanel
{  
  //PARA EL CONTROL DE JUAN
  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));

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
  private JLabel lblSuperGeo = new JLabel();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel lblSuperficies = new JLabel();
  private JTextField txtSuperGeo = new JTextField();
  private JTextField txtSuperInmu = new JTextField();
  private JLabel lblSuperInmueble = new JLabel();
  
  private int IdParcela = 0;

 public GeopistaDiferentesSuperficiesPanel() {


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
   hb.enableHelpKey(this,"PatrimonioSuperficiesNoCoincidentes",hs); 
  }catch (Exception excp){ }


    this.setLayout(null);
  //


geopistaEditor1.setBounds(new Rectangle(10, 0, 720,450));

   this.add(geopistaEditor1,null);
   // this.add(btnCancelar, null);
   // this.add(btnAnterior, null);

    
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

      public void selectionChanged(IAbstractSelection abtractSelection)
      {
        //tengo que obtener las features y calcular su extension
          ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
          Iterator featuresCollectionIter = featuresCollection.iterator();
          Feature actualFeature = (Feature) featuresCollectionIter.next();
          txtSuperGeo.setText( String.valueOf(actualFeature.getGeometry().getArea()));
          
          //Obtenemos la referencia Catastral y la buscamos con una sql
          String referencia  = actualFeature.getString("Referencia catastral");
          //Obtenemos el id de la parcela 
          IdParcela = Integer.parseInt(actualFeature.getString("ID de parcela"));
          
          GeopistaPlaneamientoInformesPostgreCon conexion = new GeopistaPlaneamientoInformesPostgreCon();          
          txtSuperInmu.setText(conexion.superficieInmueble(referencia));

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


    pnlSuperficies.setBounds(new Rectangle(10, 450, 720, 80));
    pnlSuperficies.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    pnlSuperficies.setLayout(null);

    lblSuperGeo.setText(aplicacion.getI18nString("report.informes.distintas.superficies.superficie.geometrica"));
    lblSuperGeo.setBounds(new Rectangle(90, 10, 195, 20));
    jSeparator1.setBounds(new Rectangle(80, 0, 25, 80));
    jSeparator1.setOrientation(SwingConstants.VERTICAL);
    lblSuperficies.setText(aplicacion.getI18nString("report.informes.distintas.superficies.superficie"));
    lblSuperficies.setBounds(new Rectangle(5, 30, 80, 20));
    txtSuperGeo.setBounds(new Rectangle(310, 5, 210, 25));
    txtSuperInmu.setBounds(new Rectangle(310, 40, 210, 25));
    lblSuperInmueble.setText(aplicacion.getI18nString("report.informes.distintas.superficies.superficie.inmueble"));
    lblSuperInmueble.setBounds(new Rectangle(90, 45, 205, 20));


    this.setSize(750,600);
    pnlSuperficies.add(lblSuperInmueble, null);
    pnlSuperficies.add(txtSuperInmu, null);
    pnlSuperficies.add(txtSuperGeo, null);
    pnlSuperficies.add(lblSuperGeo, null);
    pnlSuperficies.add(jSeparator1, null);
    pnlSuperficies.add(lblSuperficies, null);
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

        Report report = new Report();
              SwingLE le = new SwingLE();
              GeopistaGeneradorListadosConexionBD conectar = new GeopistaGeneradorListadosConexionBD();
             
              String url=aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
              String user =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
              String pass =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
              //String pass =UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
              String driver =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
              String informe = aplicacion.getPath("report.patrimonio.superficie.distinta");
              Connection conn = conectar.abrirConexionPostgres() ;conectar.getDBConnection(user,pass,url, driver);
              report.setDatabaseConnection(conn);
              //report.readFile(informe);
              GeopistaInformeSuperficiesNoCoincidentes geo = new GeopistaInformeSuperficiesNoCoincidentes(IdParcela);
    
    
     }



    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}// De la clase

