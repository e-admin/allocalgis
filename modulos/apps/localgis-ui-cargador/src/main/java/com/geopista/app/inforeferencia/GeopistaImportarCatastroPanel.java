/**
 * GeopistaImportarCatastroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
public class GeopistaImportarCatastroPanel extends JPanel implements WizardPanel
{

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JScrollPane scpErrores = new JScrollPane();
  private JLabel lblImagen = new JLabel();
  private JLabel lblFichero = new JLabel();
  private JTextField txtFichero = new JTextField();
  private JButton btnAbrir = new JButton();
  private JLabel lblErrores = new JLabel();

  private javax.swing.JLabel jLabel = null;
  //private GeopistaEditor geopistaEditor1 = new GeopistaEditor();
  public GeopistaEditor geopistaEditor1;
  private String nombreFicheroEjecutar;
  private String nombreFicheroRequerido;
  private String descripcionFicheroEjecutar;
  private JOptionPane CuadroDialogo;
  private JEditorPane ediError = new JEditorPane();
  private JLabel lblTipo = new JLabel();
  //private JComboBox cmbTipoInfo = new JComboBox();

  //public GeopistaImportarCatastroPanel()
  public GeopistaImportarCatastroPanel(GeopistaEditor geopistaEditor)
  {
    this.geopistaEditor1 = geopistaEditor;
    this.geopistaEditor1.reset();
    this.setName("Importar información de Catastro.");
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
    
    this.setLayout(null);

    scpErrores.setBounds(new Rectangle(135, 130, 195, 380));

    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));

    lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

    lblFichero.setText("Directorio a importar:");
    lblFichero.setBounds(new Rectangle(136, 55, 140, 20));

    txtFichero.setBounds(new java.awt.Rectangle(283,55,365,20));

    btnAbrir.setBounds(new java.awt.Rectangle(663,53,35,25));
    btnAbrir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAbrir_actionPerformed(e);
        }
      });

    lblErrores.setText("Errores de validación:");
    lblErrores.setBounds(new java.awt.Rectangle(136,105,175,20));


  	//cmbTipoInfo = new JComboBox( new Object[]{ "Números de policía"} );
/*    cmbTipoInfo.addItem("Vías y Tramos de Vía");
    cmbTipoInfo.addItem("Base Cartográfica");*/

    geopistaEditor1.setBounds(new Rectangle(335, 90, 365, 420));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));

    this.setSize(750,600);
    this.add(lblTipo, null);
    this.add(geopistaEditor1, null);
    this.add(lblErrores, null);
    this.add(btnAbrir, null);
    this.add(txtFichero, null);
    this.add(lblFichero, null);
    this.add(lblImagen, null);
    scpErrores.getViewport().add(ediError, null);
    this.add(scpErrores, null);



    //ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    lblTipo.setBounds(new Rectangle(135, 25, 190, 20));
    lblTipo.setText("Importación de Números de Policía:");
    ediError.setContentType("text/html");
    ediError.setEditable(false);
    //geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
    //geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    //geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");

    geopistaEditor1.showLayerName(false);

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
      return "Selección de los datos a importar en GEOPISTA.";
    }

    public String getID()
    {
      return "2";
    }

    public String getInstructions()
    {
     return " ";
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
     public void setWizardContext(WizardContext wd)
    {
      
    }

    
  private void btnAbrir_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
    fc.setFileSelectionMode(1); //para que solo pueda seleccionar directorios.

    //filter.addExtension("");
    /*filter.addExtension("ejes.shp");
    filter.addExtension("elemlin.shp");
    filter.setDescription("Ficheros Elemtex, Elemlin y Ejes");*/

    //fc.setFileFilter(filter);
    fc.setAcceptAllFileFilterUsed(false); //  QUITA LA OPCION ALL FILES(*.*)
    

    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
            /*String fichero;
            String nombreFicheroSelec;*/
           
            int ok = 0;
            int elemtex = 0;
            int ejes = 0;
            int carvia = 0;
            int elemlin = 0;
            String cadenaTexto;
            String directorio;
            
            directorio = fc.getSelectedFile().getPath().toString();
            this.txtFichero.setText(directorio);
            String directorio2 = directorio;
            directorio2 = directorio2.replace('\\', '/');
            //System.out.println(directorio);
            File file= new File(directorio);
            File[] miembros= file.listFiles();
            for (int i= 0; i < miembros.length; i++)
            {
                  String nombre = miembros[i].getName();
                  if(nombre.toLowerCase().equals("elemtex.shp"))
                  {
                    elemtex = 1;
                  }
                   if(nombre.toLowerCase().equals("ejes.shp"))
                  {
                    ejes = 1;
                  }
                   if(nombre.toLowerCase().equals("carvia.dbf"))
                  {
                    carvia = 1;
                  }
                    if(nombre.toLowerCase().equals("elemlin.shp"))
                  {
                    elemlin = 1;
                  }
            }
            System.out.println(directorio);

            cadenaTexto = "<font face=SansSerif size=3><strong>Comenzando la importación de Números de Policía</strong>";
            
            
            if(elemtex == 1)
            {
              cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>Comenzando la importación del fichero <b>elemtex.shp</b></p>";
              cadenaTexto = cadenaTexto + "<font face=SansSerif size=3 color=#009900>Importación del fichero <b>elemtex.shp</b> finalizada</font>";
              try
              {
                  String nombreLayer = directorio2 + "\\elemtex.shp";
                  GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData(nombreLayer,"InfoReferencia");
                  layer01.setActiva(true);
                  layer01.setVisible(true);
               }
                catch(Exception excepcion)
                {
                    excepcion.printStackTrace();
                }
            }
            else
            {
                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#ff0000>No se ha encontrado en el directorio " + directorio + " el fichero elemtex.shp </font></p>";
            }

             if(ejes == 1)
            {
              cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>Comenzando la importación del fichero <b>ejes.shp</b></p>";
              if(carvia == 1)
              {
                cadenaTexto = cadenaTexto + "<font face=SansSerif size=3 color=#009900>Importación del fichero <b>ejes.shp</b> finalizada</font>";
                try
                {
                  String nombreLayer = directorio2 + "\\ejes.shp";
                  GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData(nombreLayer,"InfoReferencia");
                  layer01.setActiva(true);
                  layer01.setVisible(true);
                 }
                  catch(Exception excepcion)
                  {
                      excepcion.printStackTrace();
                  }
              }
              else
              {
                 cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#ff0000>No se ha encontrado en el directorio " + directorio + " el fichero <b>carvia.dbf</b>, necesario para poder importar el fichero <b>ejes.shp</b> </font></p>";
              }
            }
            else
            {
                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#ff0000>No se ha encontrado en el directorio " + directorio + " el fichero ejes.shp </font></p>";
            }

            if(elemlin == 1)
            {
              cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>Comenzando la importación del fichero elemlin.shp</b></p>";
              cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#009900>Importación del fichero elemlin.shp finalizada</font></p>";
              try
              {
                  String nombreLayer = directorio2 + "\\elemlin.shp";
                  GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData(nombreLayer,"InfoReferencia");
                  layer01.setActiva(true);
                  layer01.setVisible(true);
               }
                catch(Exception excepcion)
                {
                    excepcion.printStackTrace();
                }
            }
            else
            {
                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#ff0000>No se ha encontrado en el directorio " + directorio + " el fichero elemlin.shp </font></p>";
            }
            ediError.setText(cadenaTexto);

    }


  }

  private void cmbTipoInfo_actionPerformed(ActionEvent e)
  {
//    System.out.println("entra onchange");
      txtFichero.setText("");
       try
       {
          GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData("","");
          layer01.setActiva(true);
          layer01.setVisible(true);
          layer01.addStyle(new BasicStyle(new Color(255,255,255)));
        }
        catch(Exception excepcion)
        {
           excepcion.printStackTrace();
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
