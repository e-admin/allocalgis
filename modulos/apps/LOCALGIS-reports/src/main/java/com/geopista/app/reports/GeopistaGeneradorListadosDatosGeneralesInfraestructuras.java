/**
 * GeopistaGeneradorListadosDatosGeneralesInfraestructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.geopista.app.AppContext;
import com.geopista.sql.GeopistaInformesPostgresCon;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


 


/**
 * Clase que mostrará la información general que se incluirá en el informe
 */

public class GeopistaGeneradorListadosDatosGeneralesInfraestructuras extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private DefaultListModel modeloList = new DefaultListModel();
  private JList lstCapas = new JList(modeloList);   
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private JPanel jPanel1 = new JPanel();
  private JLabel lblDatosGenerales = new JLabel();
  private JLabel lblModulo = new JLabel();
  private JComboBox cmbModulos = new JComboBox();
  private JLabel lblCapa = new JLabel();
  



  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel lblNombreFichero = new JLabel();
  private JTextField txtNombreFichero = new JTextField();
  private JLabel lblOrientacion = new JLabel();
  private JLabel lblDescripcion = new JLabel();
  private JTextField txtDescripcion = new JTextField();
  private JLabel lblTitulo = new JLabel();
  private JTextField txtTituloInforme = new JTextField();
  private String tipoInfraestructura; 
  
  
  
  public Connection con =null;

  public GeopistaGeneradorListadosDatosGeneralesInfraestructuras(String sTipo)
  	
  {
    try
    {
        this.tipoInfraestructura=sTipo;
       jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  
  private void jbInit() throws Exception
  {
    //Leemos la conexion
  




    //
//    con = getDBConnection();
    GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
   
    this.setLayout(null);
    this.setSize(new Dimension(854, 500));
    jPanel1.setLayout(null);
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      lblDatosGenerales.setText(aplicacion.getI18nString("generador.app.reports.datos.generales"));
      lblModulo.setText(aplicacion.getI18nString("generador.app.reports.modulo"));
      lblCapa.setText(aplicacion.getI18nString("generador.app.reports.capa"));

    //Cargamos la lista de capas
       ArrayList capas = geopistaListados.capasGeopista();
        Iterator i = capas.iterator();
        lblDatosGenerales.setBounds(new Rectangle(135, 5, 185, 20));
        lblModulo.setBounds(new Rectangle(135, 23, 60, 25));
        lblCapa.setBounds(new Rectangle(135, 50, 45, 30));
        jPanel1.setBounds(new Rectangle(10, 15, 750, 500));
        jSeparator1.setBounds(new Rectangle(0, 135, 735, 5));
        
      String nombre;  
      while (i.hasNext())
       {
          
          nombre =(String)i.next();
          if (tipoInfraestructura.equals("S")){
              //obtener el Id de la tabla 
              if (nombre.equals("colectores")){
                  modeloList.addElement(nombre);
              }else{
                  if (nombre.equals("depuradoras")){
                      modeloList.addElement(nombre);
                  }else{
                      if (nombre.equals("elementossaneamiento")){
                          modeloList.addElement(nombre);
                      }else{
                          if (nombre.equals("emisarios")){
                              modeloList.addElement(nombre);
                          }else{
                              if (nombre.equals("tramossaneamiento")){
                                  modeloList.addElement(nombre);
                              }else{
                                  if (nombre.equals("saneamientoautonomo")){
                                      modeloList.addElement(nombre);
                                  }else{
                                      
                                  }
                              }
                          }
                      }
                  }
                  
              }//fin del saneamiento
              
          }else{
              //Preguntar por las capas de abastecimiento.
              if (nombre.equals("captaciones")){
                  modeloList.addElement(nombre);
              }else{
                  if (nombre.equals("conducciones")){
                      modeloList.addElement(nombre);
                  }else{
                      if (nombre.equals("depositos")){
                          modeloList.addElement(nombre);
                      }else{
                          if (nombre.equals("piezas")){
                              modeloList.addElement(nombre);
                          }else{
                              if (nombre.equals("potabilizadoras")){
                                  modeloList.addElement(nombre);
                              }else{
                                  if (nombre.equals("tramosabastecimiento")){
                                         modeloList.addElement(nombre);                                      
                                  }else{
                                  
                              	}
                                  
                              }
                          }
                      }
                  }
              }
                  
              
              
             // modeloList.addElement(i.next());    
          }
          
       }
        


     //Creamos la lista con el modelo anteriormente definido      
       
//--    jPanel1.setSize(new Dimension(750, 600));
        cmbModulos.setBackground(new Color(254, 255, 255));
    cmbModulos.setBounds(new Rectangle(215, 25, 520, 20));



       lstCapas.setSelectionModel(listSelectionModel1);

       lblTitulo.setText(aplicacion.getI18nString("informe.datos.seleccion.titulo.fichero"));
       lblTitulo.setBounds(new Rectangle(140, 215, 95, 15));
       txtTituloInforme.setBounds(new Rectangle(140, 230, 595, 20));
        


      lstCapas.addKeyListener(new java.awt.event.KeyAdapter() { 
      	public void keyReleased(java.awt.event.KeyEvent e) {    
      		String cadena = (String) lstCapas.getSelectedValue();
      		lstCapas.ensureIndexIsVisible(lstCapas.getSelectedIndex());
      		lstCapas.setSelectedValue((String)cadena,true);
      	}
      });
      lstCapas.addMouseListener(new MouseListener(){
             public void mouseClicked(MouseEvent e){
              wizardContext.inputChanged();
          }
         public void mousePressed(MouseEvent e){}
         public void mouseReleased(MouseEvent e){}
         public void mouseEntered(MouseEvent e){}{}
         public void mouseExited(MouseEvent e){}
    
     });  
    
    lblDescripcion.setText(aplicacion.getI18nString("generador.app.reports.descripcion.informe"));
    lblDescripcion.setBounds(new Rectangle(140, 255, 95, 20));
    txtDescripcion.setBounds(new Rectangle(140, 280, 595, 95));
    lblOrientacion.setText(aplicacion.getI18nString("generador.app.reports.orientacion.informe"));
    lblOrientacion.setBounds(new Rectangle(135, 178, 85, 25));
    lblNombreFichero.setText(aplicacion.getI18nString("generador.app.reports.nombre.fichero"));
    lblNombreFichero.setBounds(new Rectangle(135, 145, 120, 30));
    txtNombreFichero.setBounds(new Rectangle(215, 150, 520, 20));
     
//    hb.enableHelpKey(this,"encabezado",hs);     
    //Ponemos a mano los Módulos , prueba
//    cmbModulos.addItem((String) aplicacion.getI18nString("generador.app.reports.modulo.planeamiento"));
//    cmbModulos.addItem(aplicacion.getI18nString("generador.app.reports.modulo.catastro"));
//    cmbModulos.addItem(aplicacion.getI18nString("generador.app.reports.modulo.informacion"));
//    cmbModulos.addItem(aplicacion.getI18nString("generador.app.reports.modulo.patrimonio"));
      cmbModulos.addItem(aplicacion.getI18nString("generador.app.reports.modulo.infraestructuras"));
    //Ponemos las explicaciones de las secciones
  /*  lblExplicacionDatosInformes.setText(aplicacion.getI18nString("informe.datos.generales.explicacion.comentarios"));
    txtTituloInforme.setToolTipText(aplicacion.getI18nString("informe.datos.generales.explicacion.titulo.fichero"));
    txtDescripcion.setToolTipText(aplicacion.getI18nString("informe.datos.generales.explicacion.detalle"));
    txtNombreFichero.setToolTipText(aplicacion.getI18nString("informe.datos.generales.explicacion.nombre.fichero"));
    lblUbicacion.setText(aplicacion.getI18nString("informe.datos.generales.explicacion.ubicacion"));*/
    
    
    //Ponemos a mano la orientacion
     optVertical.setText(aplicacion.getI18nString("generador.app.reports.orientacion.vertical"));
     optHorizontal.setText(aplicacion.getI18nString("generador.app.reports.orientacion.horizontal"));

    //Nuevo Listener para ver cuando cambia la caja de texto
    txtTituloInforme.addKeyListener(new KeyListener(){
      public void keyTyped(KeyEvent e){}
      public void keyPressed(KeyEvent e){
         wizardContext.inputChanged();}
      public void keyReleased(KeyEvent e){}
    });
    optVertical.setText(aplicacion.getI18nString("generador.app.reports.orientacion.vertical"));
    optVertical.setSelected(true);
    optVertical.setBounds(new Rectangle(215, 180, 125, 20));
    optVertical.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          optVertical_actionPerformed(e);
        }
      });
    optHorizontal.setText(aplicacion.getI18nString("generador.app.reports.orientacion.horizontal"));
    optHorizontal.setBounds(new Rectangle(360, 180, 135, 20));
    optHorizontal.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          optHorizontal_actionPerformed(e);
        }
      });
    jScrollPane1.setBounds(new Rectangle(215, 60, 520, 70));
    jSeparator2.setBounds(new Rectangle(5, 205, 730, 5));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));



    txtNombreFichero.addKeyListener(new KeyListener(){
     public void keyTyped(KeyEvent e){}
     public void keyPressed(KeyEvent e){
         wizardContext.inputChanged();}
      public void keyReleased(KeyEvent e){}
    });
    
    jScrollPane1.getViewport().add(lstCapas, null);
    jPanel1.add(jSeparator2, null);
    jPanel1.add(jScrollPane1, null);
    jPanel1.add(optHorizontal, null);
    jPanel1.add(optVertical, null);
    jPanel1.add(txtTituloInforme, null);
    jPanel1.add(lblTitulo, null);
    jPanel1.add(txtDescripcion, null);
    jPanel1.add(lblDescripcion, null);
    jPanel1.add(lblOrientacion, null);
    jPanel1.add(txtNombreFichero, null);
    jPanel1.add(lblNombreFichero, null);
    jPanel1.add(jSeparator1, null);
    jPanel1.add(lblCapa, null);
    jPanel1.add(cmbModulos, null);
    jPanel1.add(lblModulo, null);
    jPanel1.add(lblDatosGenerales, null);
    this.add(lblImagen, null);
        this.add(jPanel1, null);

    //Cargamos las capas en la lista

    
    
   

    
  }
 
  public void enteredFromLeft(Map dataMap)
  {
    try {
      //HelpSet de Ayuda      
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
      hb.enableHelpKey(this,"generadorInformeDatosGenerales",hs); 
    }catch (Exception e){e.printStackTrace();}
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      // ponemos en un objeto listaTablas las capas qeu se utilizan
      
     // DefaultListModel recuperar = (DefaultListModel) lstCapas.getModel();
     
      java.util.List listaTablasInformes = null;
      GeopistaObjetoInformeDatosGenerales objetoDatosGenerales = new GeopistaObjetoInformeDatosGenerales();
      listaTablasInformes =  Arrays.asList(lstCapas.getSelectedValues());

      //Pasamos los datos al objeto  
      objetoDatosGenerales.setCapas(listaTablasInformes);
      objetoDatosGenerales.setModulo((String)cmbModulos.getSelectedItem());
      objetoDatosGenerales.setTitulo(txtTituloInforme.getText());
      if (optVertical.isSelected()){
        objetoDatosGenerales.setOrientacion(optVertical.getText());        
      }else{
        objetoDatosGenerales.setOrientacion(optHorizontal.getText());
      }      

      objetoDatosGenerales.setNombreFichero(txtNombreFichero.getText());
      objetoDatosGenerales.setDescripcion(txtDescripcion.getText());
     
       blackboardInformes.put("datosGenerales",objetoDatosGenerales);
      
 
      
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
      return "2";
    }
    private WizardContext wizardContext;
    private JRadioButton optVertical = new JRadioButton();
    private JRadioButton optHorizontal = new JRadioButton();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JSeparator jSeparator2 = new JSeparator();
 
    public void setWizardContext(WizardContext wd)
    {

      wizardContext = wd;
    }
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {

      if (txtNombreFichero.getText().equals(""))
      {

        return false;
      }else
      {

        //Comprobar que hay capas selecionadas
        
        if (lstCapas.getSelectedIndices().length==0) {
          return false;
        }else{
          if (txtTituloInforme.getText().equals("")) {
            return false;
          }else{
            return true;
          }
        }

     }
    }


  private String nextID="3";
  private JLabel lblImagen = new JLabel();
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


  private void cmbOrientacion_actionPerformed(ActionEvent e)
  {
  }

/**
 * Devuevle la conexion para acceso a la base de datos
 * @return conexion para acceder a la base de datos
 */

public static Connection getDBConnection() throws SQLException
{
  GeopistaInformesPostgresCon  geoConex = new GeopistaInformesPostgresCon();
  Connection  conn=null;
  try {
  conn = geoConex.abrirConexion();
  conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}

  private void optVertical_actionPerformed(ActionEvent e)
  {
    optHorizontal.setSelected(false);
  }

  private void optHorizontal_actionPerformed(ActionEvent e)
  {
    optVertical.setSelected(false);
  }


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}




} 