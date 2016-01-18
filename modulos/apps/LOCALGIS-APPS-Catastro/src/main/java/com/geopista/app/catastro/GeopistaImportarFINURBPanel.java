/**
 * GeopistaImportarFINURBPanel.java
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.help.HelpBroker;
import javax.help.HelpSet;
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
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/**
 * La clase GeopistaImportarFINURBPanel, importa el Fichero informático de catastro urbana FINURB-DGC.
 */
 
public class GeopistaImportarFINURBPanel extends JPanel implements WizardPanel
{
  private ApplicationContext appcont = AppContext.getApplicationContext();
  private boolean Correcto=true;
  private boolean continuar;
  private WizardContext wizardContext;
  private int i=0;
  private int cuenta_lineas=0;
  private String elementVector;
  private int identificador = 0;
  private int ID_Parcela=0;
  private int ID_Subparcela=0;
  private int ID_Unidad=0;
  private String ID_Cons;
  private String cadenaError="";
  private String cadenaFin;
  private int ID_Cargo=0;
  private boolean acceso;
  private int cuentaLinea;
  
  private GeopistaValidarFINURB_FINCARU Validar = new GeopistaValidarFINURB_FINCARU();   
  private GeopistaFINURBIncluirParcela parcela = new GeopistaFINURBIncluirParcela();    
  
  private GeopistaFINURBIncluirSubparcela subparcela = new GeopistaFINURBIncluirSubparcela();    
  private GeopistaFINURBIncluirUnidad unidad = new GeopistaFINURBIncluirUnidad();    
  private GeopistaFINURBIncluirConstruccion construccion = new GeopistaFINURBIncluirConstruccion();    
  private GeopistaFINURBIncluirCargo cargo = new GeopistaFINURBIncluirCargo();    
  private Blackboard blackImportar = new Blackboard();
  private JLabel lblImagen = new JLabel();
  public int errorFich = 0;
  private JOptionPane OpCuadroDialogo;
  private int valorProgreso = 0;
  private Vector vectorParcela=new Vector();
  private Vector vectorSubparcela=new Vector();
  private Vector vectorUnidad=new Vector();
  private Vector vectorConstruccion=new Vector();
  private Vector vectorCargo=new Vector();
  
  public GeopistaImportarFINURBPanel()
  {  
                try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"catastroImportacionFinUrb01",hs); 
          }catch (Exception excp){
          }                        

    try
    {
      setName(appcont.getI18nString("importar.finca.urbana.titulo.1"));
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
       wizardContext = wd; 
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
      return this.getName();
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
      if(acceso)
      {
        if(continuar)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
      else
      {
        return false;
      }

    }

  private JTextField txtfuente = new JTextField();
  private JLabel lblfuente = new JLabel();
  private JTextField txtorganismo = new JTextField();
  private JLabel lblorganismo = new JLabel();
  private JTextField txtpersona = new JTextField();
  private JLabel lblpersona = new JLabel();
  //private JLabel lbltipo = new JLabel();
  private JTextField txtfecha = new JTextField();
  private JLabel lblFecha = new JLabel();
  private JButton btnAbrir = new JButton();
  private JTextField txtFichero = new JTextField();
  private JLabel lblFichero = new JLabel();
  private JLabel lblErrores = new JLabel();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JEditorPane ediError = new JEditorPane();
  private JSeparator jSeparator1 = new JSeparator();
  private JSeparator jSeparator4 = new JSeparator();
  //private JComboBox jComboBox1 = new JComboBox();
  

  private void jbInit() throws Exception
  {
        
    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Catastro.Importar.fichero.FINURB");
    acceso = appcont.checkPermission(geopistaPerm,"Catastro");

    if(acceso)
    {
      btnAbrir.setEnabled(true);
      txtpersona.setEnabled(true);
      txtorganismo.setEnabled(true);
      txtfuente.setEnabled(true);
    }
    else
    {
      btnAbrir.setEnabled(false);
      txtpersona.setEnabled(false);
      txtorganismo.setEnabled(false);
      txtfuente.setEnabled(false);
    }
    this.setSize(new Dimension(797, 606));
    this.setLayout(null);
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    txtfuente.setBounds(new Rectangle(240, 30, 490, 20));
    lblfuente.setText(appcont.getI18nString("Fuente") + ":");
    lblfuente.setBounds(new Rectangle(135, 30, 95, 20));
    txtorganismo.setBounds(new Rectangle(240, 60, 490, 20));
    lblorganismo.setText(appcont.getI18nString("Organismo") + ":");
    lblorganismo.setBounds(new Rectangle(135, 60, 75, 20));
    txtpersona.setBounds(new Rectangle(240, 90, 490, 20));
    lblpersona.setText(appcont.getI18nString("Persona") + ":");
    lblpersona.setBounds(new Rectangle(135, 90, 85, 20));
    
    txtfecha.setBounds(new Rectangle(240, 120, 145, 20));
    txtfecha.setEnabled(false);
    DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    String date = (String) formatter.format(new Date());
    txtfecha.setText(date);
    txtfecha.setBackground(new Color(200, 200, 200));
    lblFecha.setText(appcont.getI18nString("Fecha") + ":");
    lblFecha.setBounds(new Rectangle(135, 120, 70, 20));
    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
    btnAbrir.setBounds(new Rectangle(695, 180, 25, 25));
    btnAbrir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAbrir_actionPerformed(e);
        }
      });
    txtFichero.setBounds(new Rectangle(135, 180, 555, 20));
    txtFichero.setEnabled(false);
    txtFichero.setBackground(new Color(200, 200, 200));
    lblFichero.setText(appcont.getI18nString("importar.finca.urbana.titulo.1") + ":");
    lblFichero.setBounds(new Rectangle(135, 155, 555, 20));
    lblErrores.setText(appcont.getI18nString("ErrorValidacion") + ":");
    lblErrores.setBounds(new Rectangle(135, 205, 555, 20));
    jScrollPane1.setBounds(new Rectangle(135, 230, 595, 280));
    ediError.setEditable(false);
    ediError.setContentType("text/html");
    jSeparator1.setBounds(new Rectangle(135, 145, 600, 5));
    jSeparator4.setBounds(new Rectangle(135, 25, 595, 5));
    
    jScrollPane1.getViewport().add(ediError, null);
    
    this.add(jSeparator4, null);
    this.add(jSeparator1, null);
    this.add(jScrollPane1, null);
    this.add(lblErrores, null);
    this.add(lblFichero, null);
    this.add(txtFichero, null);
    this.add(btnAbrir, null);
    this.add(lblFecha, null);
    this.add(txtfecha, null);
    
    this.add(lblpersona, null);
    this.add(txtpersona, null);
    this.add(lblorganismo, null);
    this.add(txtorganismo, null);
    this.add(lblfuente, null);
    this.add(txtfuente, null);
    this.add(lblImagen, null);
  }

  private void btnAbrir_actionPerformed(ActionEvent e)
  {
/*    GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Catastro.Importar.fichero.FINURB");
    acceso = appcont.checkPermission(geopistaPerm,"Catastro");
    if(acceso)
    {*/
      cuentaLinea = 0;
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
        continuar = true;
        /* * * * * * * * * * * * * * * * * * * * * * * * * */

/*       try {
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
                    if (linea.substring(0,2).equals("21"))
                      { Correcto= Validar.ValidarParcela (linea); }
                    else if (linea.substring(0,2).equals("22"))
                      { Correcto= Validar.ValidarSubparcela (linea);  }
                    else if (linea.substring(0,2).equals("23"))
                      { Correcto= Validar.ValidarUC (linea); }
                    else if (linea.substring(0,2).equals("24"))
                      { Correcto= Validar.ValidarConstruccion (linea);  }
                    else if (linea.substring(0,2).equals("25"))
                      { Correcto= Validar.ValidarCargo (linea);  }   
                    else {
                  //    Correcto=false;
                      continuar = false;
                    }
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
  */    
        cadenaTexto = "<font face=SansSerif size=3>"+appcont.getI18nString("validar.comenzar")+" <b>" + fc.getSelectedFile().getName()+"</b>";
        cadenaTexto = cadenaTexto + "<p>"+ appcont.getI18nString("OperacionMinutos")+" ...</p></font>";
      

        

        ediError.setText(cadenaTexto);
     
        /* * * * *  Código para empezar a importar * * * * */
       
        File file2 = new File(Ruta);
        try {
            FileReader fr = null;
            fr = new FileReader(file2);
            BufferedReader br = new BufferedReader (fr);
            while ( (linea = br.readLine()) != null ) 
            {
                if((continuar == true)&&(linea.length()>=2))
                {
                 
                  if (linea.substring(0,2).equals("21"))
                  {
                      cuentaLinea = cuentaLinea + 1;
                      ID_Parcela = parcela.IncluirParcela (linea); 
                      vectorParcela.add(new Integer(ID_Parcela));
                      if (ID_Parcela == 0 )
                      {
                        continuar = false;
                        cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINURBParcela")+"</font></p>";
                      }
                  }
                  //else if (linea.substring(0,2).equals("22"))
                  //{
         /*             cuentaLinea = cuentaLinea + 1;
                      ID_Subparcela= subparcela.IncluirSubparcela (linea, ID_Parcela); 
                      vectorSubparcela.add(new Integer(ID_Subparcela));
                      if (ID_Subparcela == 0 )
                      {
                        continuar = false;
                        cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINURBSubparcela")+"</font></p>";
                      }*/
                  //}

                  //else if (linea.substring(0,2).equals("23"))
                  //{
                        /*cuentaLinea = cuentaLinea + 1;
                        ID_Unidad= unidad.IncluirUC(linea, ID_Parcela); 
                        vectorUnidad.addElement(new Integer(ID_Unidad));
                        if(ID_Unidad == 0)
                        {
                          continuar = false;
                          cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINURBUC")+"</font></p>";
                        }*/
                  //}
                  //else if (linea.substring(0,2).equals("24"))
                  //{
                        /*cuentaLinea = cuentaLinea + 1;
                        ID_Cons = construccion.IncluirConstruccion(linea, ID_Unidad);
                        if(ID_Cons == null)
                        {
                          continuar = false;
                          cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINURBConstruccion")+"</font></p>";
                        }
                        else
                        {
                           vectorConstruccion.addElement(new String(ID_Cons));
                        }*/
                  //}
                  else if (linea.substring(0,2).equals("22")){
                        cuentaLinea = cuentaLinea + 1;
                        ID_Cargo= cargo.IncluirCargo(linea);
                        vectorCargo.addElement(new Integer(ID_Cargo));
                        if(ID_Cargo == 0)
                        {
                          continuar = false;
                          cadenaError = cadenaError + "<p><font face=SansSerif size=3 color=#FF0000> - "+appcont.getI18nString("ErrorFINURBCargo")+"</font></p>";
                        }
                    
                  }
                }
            
               if ((continuar == false)||(cuentaLinea == 0))
                {
                   if (vectorCargo.size()!=0)
                  {
                    for (i=0;i<=vectorCargo.size()-1;i++)
                    {
                      elementVector = vectorCargo.get(i).toString();
                      identificador = Integer.parseInt(elementVector);
                      cargo.eliminarCargo(identificador);
                    }
                    vectorCargo.clear();
                  }
                  /*if (vectorConstruccion.size()!=0)
                  {
                    for (i=0;i<=vectorConstruccion.size()-1;i++)
                    {
                      elementVector = vectorConstruccion.get(i).toString();
                      construccion.eliminarConstruccion(elementVector);
                    }
                    vectorConstruccion.clear();
                  }
                   if (vectorUnidad.size()!=0)
                  {
                    for (i=0;i<=vectorUnidad.size()-1;i++)
                    {
                      elementVector = vectorUnidad.get(i).toString();
                      identificador = Integer.parseInt(elementVector);
                      unidad.eliminarUnidad(identificador);
                    }
                    vectorUnidad.clear();
                  }
                  if (vectorSubparcela.size()!=0)
                  {
                    for (i=0;i<=vectorSubparcela.size()-1;i++)
                    {
                      elementVector = vectorSubparcela.get(i).toString();
                      identificador = Integer.parseInt(elementVector);
                      subparcela.eliminarSubParcela(identificador);
                    }
                    vectorSubparcela.clear();
                  }*/
                  if (vectorParcela.size()!=0)
                  {
                    for (i=0;i<=vectorParcela.size()-1;i++)
                    {
                      elementVector = vectorParcela.get(i).toString();
                      identificador = Integer.parseInt(elementVector);
                      parcela.eliminarParcela(identificador);
                    }
                    vectorParcela.clear();
                  }
              
                  cadenaFin = cadenaTexto + cadenaError;
                  cadenaFin = cadenaFin + appcont.getI18nString("validar.fin.incorrecto");
                  ediError.setText(cadenaFin);
                  continuar = false;
                  if (continuar == false)
                  {
                    break;
                  }
                }
          
            }
            if ((continuar==true)&&(cuentaLinea!=0))
            {
              blackImportar = appcont.getBlackboard();
              blackImportar.put("totalParcelas",  vectorParcela.size());
              blackImportar.put("totalSubParcelas",  vectorSubparcela.size());
              blackImportar.put("totalUnidad",  vectorUnidad.size());
              blackImportar.put("totalConstruccion",  vectorConstruccion.size());
              blackImportar.put("totalCargo",  vectorCargo.size());
              cadenaFin = cadenaTexto +appcont.getI18nString("validar.fin.correcto");
              ediError.setText(cadenaFin);
            }
         wizardContext.inputChanged();
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