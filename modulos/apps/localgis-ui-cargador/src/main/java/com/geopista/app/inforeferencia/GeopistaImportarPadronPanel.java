/**
 * GeopistaImportarPadronPanel.java
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
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.sql.GeopistaSQL;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaImportarPadronPanel extends JPanel implements WizardPanel
{
  private JScrollPane scpScrollPane1 = new JScrollPane();
  private JList lstOpcion = new JList();
  private JLabel lblOpcion = new JLabel();
  private JScrollPane scpScrollPane2 = new JScrollPane();
  private JButton btnDescripcion = new JButton();
  private JButton btnCancelar = new JButton();
  private JLabel lblImagen = new JLabel();
  private JLabel lblFichero = new JLabel();
  private JTextField txtFichero = new JTextField();
  private JButton btnAbrir = new JButton();
  private JLabel lblErrores = new JLabel();
  private JEditorPane ediError = new JEditorPane();
  public static JOptionPane op = new JOptionPane();
  
  public GeopistaImportarPadronPanel() {
  
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
//    pnlPanel.setLayout(null);
   this.setLayout(null);

    scpScrollPane2.setBounds(new Rectangle(135, 130, 595, 380));
    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));

    lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

    lblFichero.setText("Fichero a importar:");
    lblFichero.setBounds(new java.awt.Rectangle(136,29,140,20));
    txtFichero.setBounds(new java.awt.Rectangle(282,29,365,20));
    btnAbrir.setBounds(new java.awt.Rectangle(663,26,35,25));
    btnAbrir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAbrir_actionPerformed(e);
        }
      });
    lblErrores.setText("Errores de validación:");
    lblErrores.setBounds(new java.awt.Rectangle(136,105,175,20));
    ediError.setText("jEditorPane1");
    ediError.setEditable(false);
    ediError.setContentType("text/html");
    ediError.setEnabled(false);

    this.setSize(750,600);
    this.setMinimumSize(new Dimension(750,600));
    this.add(lblErrores, null);
    this.add(btnAbrir, null);
    this.add(txtFichero, null);
    this.add(lblFichero, null);
    this.add(lblImagen, null);
    this.add(btnDescripcion, null);
    scpScrollPane2.getViewport().add(ediError, null);
    this.add(scpScrollPane2, null);
    this.add(scpScrollPane1, null);

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
      return " ";
    }

    public String getID()
    {
      return "1";
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
    
    filter.addExtension("txt");
    filter.setDescription("Ficheros txt");
    fc.setFileFilter(filter);
    fc.setAcceptAllFileFilterUsed(false); //  QUITA LA OPCION ALL FILES(*.*)

    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fichero;
//            fichero=fc.getSelectedFile().getName(); //solo obtiene el nombre del fichero
            fichero = fc.getSelectedFile().getPath();
            this.txtFichero.setText(fichero); //meto el fichero seleccionado en el campo
            String cadenaTexto;
            cadenaTexto = "<font face=SansSerif size=3>Comenzando la importación de: <b>" + fc.getSelectedFile().getName()+"</b>";
            cadenaTexto = cadenaTexto + "<p>Esta operación puede durar varios minutos ...</p></font>";
            /*File file = new File(fichero);    
            String contenido = getContents(file);*/
            String contenido = getLine(fichero);
            
          
            cadenaTexto = cadenaTexto + "<p>"+contenido+"</p>";
            //cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#009900>Importación finalizada</font></p>";
            ediError.setText(cadenaTexto);
            op.showMessageDialog(null,"Importación finalizada con éxito");
     } 

  }
 // @SuppressWarnings("finally")
static public String getLine(String file)
  {
    ApplicationContext appContext=AppContext.getApplicationContext();
    String content="";
    try {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        // DOMICILIO
        String idAyuntamiento;
        String idDistrito;   // busca en la tabla distritos_censales comparando con el codigo INE para obtener el distrito
        String idSeccion;   // busca en la tabla secciones_censales comparando con seccion para obtener el id_seccion
        String idEntidad;   // busca en la tabala entidades colectivas comparando con nombreoficialcorto para obtener el id_entidadcolectiva
        String idNucleo;    // busca en la tabla nucleos_y_diseminados comparando con nombreoficialcorto para obtener el id_nucleo

        String codigoVia;
        String tipoVia;
        // HABITANTES
        String cp;
        String cm;
        String nombre;
        String part1;
        String ape1;
        String part2;
        String ape2;
        String nacprov;
        String nacmuni;
        String nacfecha;
        String dni;
        String letradni;
        String sexo;
        String ocupacion;
        String id_domicilio;
        String hojapadron;
        String params;
        int id = 0;
        ResultSet rs = null;
        Connection conn = appContext.getConnection();
        while ((str = in.readLine()) != null) {
            // DOMICILIO
            
            // HABITANTES
            cp = str.substring(0,2);
            cm = str.substring(2,5);
            nombre = str.substring(414,434);
            part1 = str.substring(434,440);
            ape1 = str.substring(440,465);
            part2 = str.substring(465,471);
            ape2 = str.substring(471,496);
            nacprov = str.substring(497,499);
            nacmuni = str.substring(499,502);
            nacfecha = str.substring(502,510);
            dni = str.substring(512,520);
            letradni = str.substring(520);
            sexo = str.substring(496);
            ocupacion = str.substring(541,str.length());
            //id_domicilio;
            hojapadron = str.substring(393,403);
                 
            
            rs = GeopistaSQL.Query("inforeferenciapadronmunicipalhabitantesselect","",conn);
            if (rs == null)
            {
              id=1;
            }else
            {
              id++;
            }
            params = id +";"+ cp + ";" + cm + ";"+ nombre + ";"+ part1 + ";"+ ape1 + ";"+ part2 + ";"+ ape2 + ";"+ nacprov + ";"+ nacmuni + ";"+ nacfecha + ";"+ dni + ";"+ letradni + ";"+ sexo + ";"+ ocupacion + ";"+ hojapadron + ";";
            // TODO: DEBE INSERTAR EN LA TABLA DE HABITANTES Y DE DOMICILIO
            //GeopistaSQL.UpdateInsert("inforeferenciapadronmunicipalhabitantesinsert",params,conn);
            content = content + nombre + part1 + ape1 +part2 +ape2 + nacprov +nacmuni+nacfecha+dni+letradni+sexo+ocupacion+hojapadron +"<br>***********************[Status: OK]********************<br><br>";
        }
        conn.close();
        in.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Errores de Importación");
        e.printStackTrace();
    } finally 
    {
      //return content;
    }
	return content;
  }
  static public String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuffer contents = new StringBuffer();

    //declared here only to make visible to finally clause
    BufferedReader input = null;
    try {
      //use buffering
      //this implementation reads one line at a time
      input = new BufferedReader( new FileReader(aFile) );
      String line = null; //not declared within while loop
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(System.getProperty("line.separator"));
      }
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    finally {
      try {
        if (input!= null) {
          //flush and close both "input" and its underlying FileReader
          input.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return contents.toString();
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}



}
