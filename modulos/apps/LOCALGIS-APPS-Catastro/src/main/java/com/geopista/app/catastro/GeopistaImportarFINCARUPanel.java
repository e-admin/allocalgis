/**
 * GeopistaImportarFINCARUPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * La clase GeopistaImportarFINCARUPanel, importa el Fichero informático de Bienes Inmuebles de Naturaleza Rústica FINCARU-DGC.
 */
public class GeopistaImportarFINCARUPanel extends JPanel implements WizardPanel
{
  private boolean Correcto=true;  
  private boolean continuar = true;
  private int i=0;
  private int cuenta_lineas=0;
  private String elementVector;
  private int identificador = 0;
  private int ID_Parcela=0;
  private int ID_Subparcela=0;
  private int ID_Unidad=0;
  private int ID_Cons=0;
  private int ID_Titular=0;
  private String cadenaError="";
  private String cadenaFin;
  private int ID_Cargo=0;
  private GeopistaValidarFINURB_FINCARU Validar = new GeopistaValidarFINURB_FINCARU();
  private GeopistaFINCARUIncluirParcela parcela = new GeopistaFINCARUIncluirParcela();
  private GeopistaFINCARUIncluirSubparcela subparcela = new GeopistaFINCARUIncluirSubparcela();
  private GeopistaFINCARUIncluirConstruccion construccion = new GeopistaFINCARUIncluirConstruccion();
  private GeopistaFINCARUIncluirTitular titular = new GeopistaFINCARUIncluirTitular();

  private JEditorPane ediError = new JEditorPane();
  private JLabel lblErrores = new JLabel();
  private JLabel lblImagen = new JLabel();
  public int errorFich = 0;
  private JOptionPane OpCuadroDialogo;
  private int valorProgreso = 0;
  private ApplicationContext appcont = AppContext.getApplicationContext();
  Vector vectorParcela=new Vector();
  Vector vectorSubparcela=new Vector();
  Vector vectorConstruccion=new Vector();
  Vector vectorCargo=new Vector();
  public boolean acceso;
  public GeopistaImportarFINCARUPanel()
  {


    try
    {


      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
 private String nextID="2";
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


  private JScrollPane scpEditor = new JScrollPane();
  private JTextField txtFichero = new JTextField();
  private JSeparator jSeparator1 = new JSeparator();
  private JTextField txttipo = new JTextField();
  private JLabel lbltipo = new JLabel();
  private JTextField txtfecha = new JTextField();
  private JLabel lblFecha = new JLabel();
  private JTextField txtpersona = new JTextField();
  private JTextField txtorganismo = new JTextField();
  private JTextField txtfuente = new JTextField();
  private JLabel lblpersona = new JLabel();
  private JLabel lblorganismo = new JLabel();
  private JLabel lblfuente = new JLabel();
  private JButton btnAbrir = new JButton();
  private JLabel lblFichero = new JLabel();

  private void jbInit() throws Exception
  {
	  GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Catastro.Importar.fichero.FINCARU");
	      acceso = appcont.checkPermission(geopistaPerm,"Catastro");

	      if(acceso)
	      {
	         btnAbrir.setEnabled(true);
	      }
	      else
	      {
	        btnAbrir.setEnabled(false);
    }
    this.setSize(new Dimension(797, 606));
    this.setLayout(null);
    ediError.setEditable(false);
    ediError.setContentType("text/html");
    lblErrores.setText(appcont.getI18nString("ErrorValidacion")+":");
    lblErrores.setBounds(new Rectangle(135, 205, 175, 20));
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    scpEditor.setBounds(new Rectangle(135, 230, 595, 280));
    txtFichero.setBounds(new Rectangle(135, 180, 555, 20));
    txtFichero.setEnabled(false);
    txtFichero.setBackground(new Color(200, 200, 200));
    jSeparator1.setBounds(new Rectangle(135, 145, 600, 5));
    txttipo.setBounds(new Rectangle(435, 110, 295, 20));
    lbltipo.setText(appcont.getI18nString("Tipo") + ":");
    lbltipo.setBounds(new Rectangle(400, 110, 30, 20));
    txtfecha.setBounds(new Rectangle(240, 110, 145, 20));
    txtfecha.setEnabled(false);
    txtfecha.setBackground(new Color(200, 200, 200));
    lblFecha.setText(appcont.getI18nString("Fecha") + ":");
    lblFecha.setBounds(new Rectangle(135, 110, 70, 20));
    txtpersona.setBounds(new Rectangle(240, 80, 490, 20));
    txtorganismo.setBounds(new Rectangle(240, 50, 490, 20));
    txtfuente.setBounds(new Rectangle(240, 20, 490, 20));
    lblpersona.setText(appcont.getI18nString("Persona") + ":");
    lblpersona.setBounds(new Rectangle(135, 80, 85, 20));
    lblorganismo.setText(appcont.getI18nString("Organismo") + ":");
    lblorganismo.setBounds(new Rectangle(135, 50, 75, 20));
    lblfuente.setText(appcont.getI18nString("Fuente") + ":");
    lblfuente.setBounds(new Rectangle(135, 20, 95, 20));
    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
    btnAbrir.setBounds(new Rectangle(695, 180, 35, 20));
    btnAbrir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAbrir_actionPerformed(e);
        }
      });
    lblFichero.setText(appcont.getI18nString("FicheroImportar") + ":");
    lblFichero.setBounds(new Rectangle(135, 155, 555, 20));
    scpEditor.getViewport().add(ediError, null);
    this.add(lblFichero, null);
    this.add(btnAbrir, null);
    this.add(lblfuente, null);
    this.add(lblorganismo, null);
    this.add(lblpersona, null);
    this.add(txtfuente, null);
    this.add(txtorganismo, null);
    this.add(txtpersona, null);
    this.add(lblFecha, null);
    this.add(txtfecha, null);
    this.add(lbltipo, null);
    this.add(txttipo, null);
    this.add(jSeparator1, null);
    this.add(txtFichero, null);
    this.add(scpEditor, null);
    this.add(lblImagen, null);
    this.add(lblErrores, null);
  }

  private void btnAbrir_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
    filter.addExtension("txt");
    filter.setDescription("Ficheros txt");

    fc.setFileFilter(filter);
    fc.setFileSelectionMode(0);
    fc.setAcceptAllFileFilterUsed(false);

    int returnVal = fc.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
    {
     String linea;
     String Ruta = fc.getSelectedFile().getPath();

      String cadenaTexto;
      String nombreFichero;
      txtFichero.setText(Ruta);

      /* * * * *  Código para empezar a importar * * * * */
      File file = new File(Ruta);
      boolean Validado=true;

      /* * * * * * * * * * * * * * * * * * * * * * * * * */

     try {
          FileReader fr = null;
          fr = new FileReader(file);
          BufferedReader br = new BufferedReader (fr);
          while ( (linea = br.readLine()) != null )
          {
            cuenta_lineas = cuenta_lineas + 1;
            if (Correcto=true)
            {
              if(linea.length()>2)
              {
                  if (linea.substring(0,2).equals("PR"))
                    { Correcto= Validar.ValidarParcelaRustica (linea); }
                  else if (linea.substring(0,2).equals("SP"))
                    { Correcto= Validar.ValidarParcelaRustica (linea);  }
                  else if (linea.substring(0,2).equals("SC"))
                    { Correcto= Validar.ValidarConstruccionRustica (linea); }
                  else if (linea.substring(0,2).equals("TL"))
                    { Correcto= Validar.ValidarTitularRustico (linea);  }
                  else {Correcto=true;}
                  continuar=true;
              }
              else
              {
                continuar=false;
              }
            }
            else{Validado=false;}
          }
         }catch (Exception ex)
         {
          ex.printStackTrace();
        }

      cadenaTexto = "<font face=SansSerif size=3>"+appcont.getI18nString("ImportacionComenzar")+": <b>" + fc.getSelectedFile().getName()+"</b>";
      cadenaTexto = cadenaTexto + "<p>"+ appcont.getI18nString("OperacionMinutos")+"...</p></font>";

      ediError.setText(cadenaTexto);

      /* * * * *  Código para empezar a importar * * * * */

      File file2 = new File(Ruta);
      try {
          FileReader fr = null;
          fr = new FileReader(file2);
          BufferedReader br = new BufferedReader (fr);
          while ( (linea = br.readLine()) != null )
          {
              if(continuar == true)
              {
                if (linea.substring(0,2).equals("PR"))
                {
                    ID_Parcela = parcela.IncluirParcelaRustica(linea);
                    vectorParcela.add(new Integer(ID_Parcela));
                    if (ID_Parcela == 0 )
                    {
                      continuar = false;
                      cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINCARUParcela")+"</font></p>";
                    }
                }
                 /*if (linea.substring(0,2).equals("SP"))
                {
                    ID_Subparcela= subparcela.IncluirSubparcelaRustica (linea, ID_Parcela);
                    vectorSubparcela.add(new Integer(ID_Subparcela));
                    if (ID_Subparcela == 0 )
                    {
                      continuar = false;
                      cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINCARUSubparcela")+"</font></p>";
                    }
                }
                if (linea.substring(0,2).equals("SC"))
                {
                    ID_Cons= construccion.IncluirConstruccionRustica (linea, ID_Subparcela);
                    vectorConstruccion.add(new Integer(ID_Cons));
                    if (ID_Cons == 0 )
                    {
                      continuar = false;
                      cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINCARUConstruccion")+"</font></p>";
                    }
                }*/
                 if (linea.substring(0,2).equals("TL"))
                {
                    ID_Titular = titular.IncluirTitularRustico(linea);
                    if (ID_Titular == 0 )
                    {
                      continuar = false;
                      cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINCARUTitular")+"</font></p>";
                    }
                }
              }

              if (continuar == false)
              {
                if (vectorConstruccion.size()!=0)
                {
                  for (i=0;i<=vectorParcela.size()-1;i++)
                  {
                    elementVector = vectorConstruccion.get(i).toString();
                    identificador = Integer.parseInt(elementVector);
                    construccion.eliminarConstruccionRustica(identificador);
                  }
                  vectorConstruccion.clear();
                }
                 if (vectorSubparcela.size()!=0)
                {
                  for (i=0;i<=vectorSubparcela.size()-1;i++)
                  {
                    elementVector = vectorSubparcela.get(i).toString();
                    identificador = Integer.parseInt(elementVector);
                    subparcela.eliminarSubparcelaRustica(identificador);
                  }
                  vectorSubparcela.clear();
                }
                if (vectorParcela.size()!=0)
                {
                  for (i=0;i<=vectorParcela.size()-1;i++)
                  {
                    elementVector = vectorParcela.get(i).toString();
                    identificador = Integer.parseInt(elementVector);
                    parcela.eliminarParcelaRustica(identificador);
                  }
                  vectorParcela.clear();
                }

                cadenaFin = cadenaTexto + "<p><font color=#000000 face=SansSerif size=3><b>"+appcont.getI18nString("ErrorImportacion")+"</b></font></p>";
                cadenaFin = cadenaFin + cadenaError;
                cadenaFin = cadenaFin + "<p><font color=#FF0000 face=SansSerif size=3><b>"+appcont.getI18nString("ImportacionFinalizadaMal")+"!!!</b></font></p>";
                ediError.setText(cadenaFin);
                break;
              }

          }
          if (continuar==true)
          {
            cadenaFin = cadenaTexto + "<p><font face=SansSerif size=3 color=#009900>"+appcont.getI18nString("ImportacionFinalizadaBien")+"</font></p>";
            ediError.setText(cadenaFin);
          }

      }catch (Exception ex)
      {
          ex.printStackTrace();
      }

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