package com.geopista.app.reports;


import com.geopista.app.AppContext;
import com.geopista.app.reports.GeopistaGeneradorListadosConexionBD;
import com.geopista.app.reports.GeopistaObjetoInformeEncabezado;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.SearchView;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import java.awt.Color;



public class GeopistaGeneradorListadosEncabezado  extends JPanel implements WizardPanel
{
  
  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private JPanel jPanel1 = new JPanel();
  
//  private GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();
  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();
  private JSeparator jSeparator1 = new JSeparator();
  private JCheckBox chkLogotipo = new JCheckBox();
  private JCheckBox chkTituloInforme = new JCheckBox();
  private JCheckBox chkTitulo2 = new JCheckBox();
  private JCheckBox chkNombre = new JCheckBox();
  private JCheckBox chkDescripcion = new JCheckBox();
  private JTextField txtRutaLogotipo = new JTextField();
  private JTextField txtTituloInforme = new JTextField();
  private JTextField txtTitulo2 = new JTextField();
  private JTextField txtNombre = new JTextField();
  private JTextField txtDescripcion = new JTextField();
  private JCheckBox chkTituloImagen = new JCheckBox();
  private JTextField txtTituloImagen = new JTextField();
  private JCheckBox chkImagenEnInforme = new JCheckBox();
  private JButton btnBuscarLogo = new JButton();
  private JSeparator jSeparator2 = new JSeparator();
  private JLabel lblCampoNomre = new JLabel();
  private JLabel lblDescripcion = new JLabel();
  private JComboBox cmbNombre = new JComboBox();
  private JComboBox cmbDescripcion = new JComboBox();
  private JLabel lblTabla = new JLabel();
  private JComboBox cmbTabla = new JComboBox();
  private JLabel lblCamposLogotipo = new JLabel();
  private JLabel lblImagen = new JLabel();
  private Connection con =null;
   
 
  
  

/**
 * Clase para recoger los datos que aparecerán en la parte del encabezado del informe
 **/
 
  public GeopistaGeneradorListadosEncabezado ()
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

  
  private void jbInit() throws Exception
  {
//    con  = getDBConnection();
    GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
    
    lblBotonImagen = new JLabel();
    this.setLayout(null);
    this.setSize(new Dimension(857, 500));
    jPanel1.setBounds(new Rectangle(10, 15, 750, 500));
    jPanel1.setLayout(null);
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    DefaultListModel modeloList = new DefaultListModel();

    try
    {
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda

      hb.enableHelpKey(this,"generadorInformeEncabezado",hs); 
    }catch(Exception e)
    {
        e.printStackTrace();
    }


    //Cargamos la lista de capas
     ArrayList capas = geopistaListados.capasGeopista();
     Iterator i = capas.iterator();



     //Creamos la lista con el modelo anteriormente definido      
    jPanel1.setSize(new Dimension(750, 500));
    jSeparator1.setBounds(new Rectangle(5, 295, 725, 5));
    
   chkLogotipo.setText(aplicacion.getI18nString("generador.app.reports.encabezado.logo"));
    chkLogotipo.setBounds(new Rectangle(140, 40, 135, 15));
    
    chkImagenEnInforme.addItemListener(new java.awt.event.ItemListener() { 
    	public void itemStateChanged(java.awt.event.ItemEvent e) {    
    		
    	}
    });
    chkImagenEnInforme.addItemListener(new java.awt.event.ItemListener() { 
    	public void itemStateChanged(java.awt.event.ItemEvent e) {    
    		if (e.getStateChange()==e.SELECTED)
    		{
    		    //lblBotonImagen.setText("No");
    		    cmdImagen.setEnabled(true);
    	
    		    //Añadir el código del botón
    		      
    		}else{
    		    //lblBotonImagen.setText("Si");
    		    cmdImagen.setEnabled(false);
 
    		} 	
    	}
    });
    chkLogotipo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkLogotipo_actionPerformed(e);
        }
      });
    chkTituloInforme.setText(aplicacion.getI18nString("generador.app.reports.encabezado.titulo.informe"));
    chkTituloInforme.setBounds(new Rectangle(140, 65, 130, 15));
    chkTituloInforme.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkTituloInforme_actionPerformed(e);
        }
      });
    chkTitulo2.setText(aplicacion.getI18nString("generador.app.reports.encabezado.titulo.dos"));
    chkTitulo2.setBounds(new Rectangle(140, 95, 130, 15));
    chkTitulo2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkTitulo2_actionPerformed(e);
        }
      });
    chkNombre.setText(aplicacion.getI18nString("generador.app.reports.encabezado.nombre"));
    chkNombre.setBounds(new Rectangle(140, 225, 110, 15));
    chkNombre.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkNombre_actionPerformed(e);
        }
      });
    chkDescripcion.setText(aplicacion.getI18nString("generador.app.reports.encabezado.descripcion"));
    chkDescripcion.setBounds(new Rectangle(140, 265, 115, 15));
    chkDescripcion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkDescripcion_actionPerformed(e);
        }
      });
   
    txtRutaLogotipo.setEnabled(false);
    txtRutaLogotipo.setBounds(new Rectangle(280, 35, 345, 20));
    txtTituloInforme.setEnabled(false);
    txtTituloInforme.setBounds(new Rectangle(280, 65, 455, 20));
    txtTitulo2.setEnabled(false);
    txtTitulo2.setBounds(new Rectangle(280, 95, 455, 20));
    txtNombre.setEnabled(false);
    txtNombre.setBounds(new Rectangle(265, 220, 135, 20));
    txtDescripcion.setEnabled(false);
    txtDescripcion.setBounds(new Rectangle(265, 260, 135, 20));
    chkTituloImagen.setText(aplicacion.getI18nString("generador.app.reports.encabezado.titulo.imagen"));
    chkTituloImagen.setBounds(new Rectangle(140, 335, 115, 30));
    chkTituloImagen.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkTituloImagen_actionPerformed(e);
        }
      });
    txtTituloImagen.setEnabled(false);
    txtTituloImagen.setBounds(new Rectangle(140, 370, 575, 20));
    chkImagenEnInforme.setText(aplicacion.getI18nString("generador.app.reports.encabezado.crear.imagen"));
    chkImagenEnInforme.setBounds(new Rectangle(140, 400, 180, 20));
    btnBuscarLogo.setText("...");
    btnBuscarLogo.setBounds(new Rectangle(630, 35, 25, 20));
    btnBuscarLogo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnBuscarLogo_actionPerformed(e);
        }
      });
    jSeparator2.setBounds(new Rectangle(5, 145, 725, 5));
      
    lblCampoNomre.setText(aplicacion.getI18nString("generador.app.reports.encabezado.campo"));
    lblCampoNomre.setBounds(new Rectangle(425, 220, 50, 20));
    lblDescripcion.setText(aplicacion.getI18nString("generador.app.reports.encabezado.campo"));
    lblDescripcion.setBounds(new Rectangle(425, 260, 90, 20));
    cmbNombre.setBounds(new Rectangle(520, 220, 215, 20));
    cmbDescripcion.setBounds(new Rectangle(520, 260, 215, 20));
    lblTabla.setText(aplicacion.getI18nString("generador.app.reports.encabezado.tabla"));
    lblTabla.setBounds(new Rectangle(15, 185, 40, 25));
    cmbTabla.setBounds(new Rectangle(140, 185, 600, 20));

    lblCamposLogotipo.setText(aplicacion.getI18nString("generador.app.reports.encabezado.campos.encabezado"));
    lblCamposLogotipo.setBounds(new Rectangle(140, 5, 370, 20));
    lblImagen.setText(aplicacion.getI18nString("generador.app.reports.encabezado.imagen.en.listado"));
    lblImagen.setBounds(new Rectangle(140, 300, 160, 25));
    lblImagen1.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen1.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    
    jPanel1.add(lblImagen, null);
     
    jPanel1.add(lblCamposLogotipo, null);
    jPanel1.add(cmbTabla, null);
    jPanel1.add(lblTabla, null);
    jPanel1.add(cmbDescripcion, null);
    jPanel1.add(cmbNombre, null);
    jPanel1.add(lblDescripcion, null);
    jPanel1.add(lblCampoNomre, null);
    jPanel1.add(jSeparator2, null);
    jPanel1.add(btnBuscarLogo, null);
    jPanel1.add(chkImagenEnInforme, null);
    jPanel1.add(txtTituloImagen, null);
    jPanel1.add(chkTituloImagen, null);
    jPanel1.add(txtDescripcion, null);
    jPanel1.add(txtNombre, null);
    jPanel1.add(txtTitulo2, null);
    jPanel1.add(txtTituloInforme, null);
    jPanel1.add(txtRutaLogotipo, null);
    jPanel1.add(chkDescripcion, null);
    jPanel1.add(chkNombre, null);
    jPanel1.add(chkTitulo2, null);
    jPanel1.add(chkTituloInforme, null);
    jPanel1.add(chkLogotipo, null);
    jPanel1.add(jSeparator1, null);

    //Cargamos las capas en la lista

      while (i.hasNext())
      {
        cmbTabla.addItem((String)i.next());
      }
      cmbTabla.setSelectedIndex(-1);
      lblBotonImagen.setBounds(326, 441, 397, 25);
      lblBotonImagen.setText("");
 
     cmbTabla.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
              cmbNombre.removeAllItems();
              cmbDescripcion.removeAllItems();
              String  nombre = (String) cmbTabla.getSelectedItem();
               GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
              ArrayList capas = geopistaListados.camposDeUnaTabla(nombre);
              Iterator i = capas.iterator();
              while (i.hasNext()){
                  String Campo = (String)i.next();
                  cmbNombre.addItem(Campo);
                  cmbDescripcion.addItem(Campo);
              }

            }
         });  
    this.add(lblImagen1, null);
    this.add(jPanel1, null);
    jPanel1.add(getCmdImagen(), null);
    jPanel1.add(lblBotonImagen, null);
     
   

    
  }
 
  public void enteredFromLeft(Map dataMap)
  {
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
       
      //Abrimos el blackBoard y pasamos los datos de la 2 Ventana.
      
     GeopistaObjetoInformeEncabezado objetoEncabezado = new GeopistaObjetoInformeEncabezado();

      objetoEncabezado.setLogo(txtRutaLogotipo.getText());
      objetoEncabezado.setTituloInforme(txtTituloInforme.getText());
      objetoEncabezado.setSubTituloInforme(txtTitulo2.getText());
      objetoEncabezado.setTablaEncabezado((String) cmbTabla.getSelectedItem());
      objetoEncabezado.setEtiquetaEncabezado(txtNombre.getText());
      objetoEncabezado.setEtiquetaDescripcion(txtDescripcion.getText());
      objetoEncabezado.setCampoEncabezado((String) cmbNombre.getSelectedItem());
      objetoEncabezado.setCampoDescripcion((String) cmbDescripcion.getSelectedItem());
      objetoEncabezado.setTituloImagen(txtTituloImagen.getText());
      objetoEncabezado.setImagenEnInforme(lblBotonImagen.getText());
      if (chkImagenEnInforme.isSelected()){
         objetoEncabezado.setCrearImagen("S");
      }else{
         objetoEncabezado.setCrearImagen("N");
      }
      blackboardInformes.put("datosEncabezado",objetoEncabezado);
 
      
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
      return "3";
    }

    private WizardContext wizardContext;
    public void setWizardContext(WizardContext wd)
    {
         
      wizardContext = wd;

    }
    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
     
        return true;
    
    }

  private String nextID="4";
  private JLabel lblImagen1 = new JLabel();
	private JButton cmdImagen = null;
	private JLabel lblBotonImagen = null;
	private JLabel lblBotonImagen1 = null;
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

  private void cmbCapas_actionPerformed(ActionEvent e)
  {

  }

  private void cmbModulos_actionPerformed(ActionEvent e)
  {
  }

  private void cmbHorientacion_actionPerformed(ActionEvent e)
  {
  }


//----
  public String escribirXml() throws Exception {

    String resultado="";


// Creamos elemento raiz y ponemos su valor
try {
    
    

      
}catch (Exception e) 
{
  e.printStackTrace();
}



 		return resultado;
    
		} 

  private void chkLogotipo_actionPerformed(ActionEvent e)
  {
    if (!chkLogotipo.isSelected() )
      {
        txtRutaLogotipo.setEnabled(false);
        txtRutaLogotipo.setText(null);
      }else{
        txtRutaLogotipo.setEnabled(true);
      }
  }

  private void chkTituloInforme_actionPerformed(ActionEvent e)
  {
    if (!chkTituloInforme.isSelected()){
      txtTituloInforme.setEnabled(false);
      txtTituloInforme.setText(null);
    }else{
      txtTituloInforme.setEnabled(true);
    }
  }

  private void chkTitulo2_actionPerformed(ActionEvent e)
  {
    if (!chkTitulo2.isSelected()){
      txtTitulo2.setEnabled(false);
      txtTitulo2.setText(null);
    }else{
      txtTitulo2.setEnabled(true);
    }
  }

  private void chkNombre_actionPerformed(ActionEvent e)
  {
    if (!chkNombre.isSelected()){
        txtNombre.setEnabled(false);
        txtNombre.setText(null);
    }else{
      txtNombre.setEnabled(true);
    }
  }

  private void chkDescripcion_actionPerformed(ActionEvent e)
  {
    if (!chkDescripcion.isSelected()){
        txtDescripcion.setEnabled(false);
        txtDescripcion.setText(null);
    }else{
        txtDescripcion.setEnabled(true);
    }
  }

  private void chkTituloImagen_actionPerformed(ActionEvent e)
  {
    if(!chkTituloImagen.isSelected()){
        txtTituloImagen.setEnabled(false);
        txtTituloImagen.setText(null);
    }else{
      txtTituloImagen.setEnabled(true);
    }
  }

  private void btnBuscarLogo_actionPerformed(ActionEvent e)
  {
    JFileChooser chooser = new JFileChooser();
    // Note: source for ExampleFileFilter can be found in FileChooserDemo,
    // under the demo/jfc directory in the Java 2 SDK, Standard Edition.
    

    int returnVal = chooser.showOpenDialog(null);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
    //txtRutaLogotipo.setText(chooser.getSelectedFile().getName());
    txtRutaLogotipo.setText(chooser.getSelectedFile().getAbsolutePath());
    }
 
  }

/**
 * Devuevle la conexion para acceso a la base de datos
 * @return conexion para acceder a la base de datos
 */


public static Connection getDBConnection() throws SQLException
{
 /* AppContext app = (AppContext) AppContext.getApplicationContext();
  Connection conn= app.getConnection();*/
  GeopistaGeneradorListadosConexionBD  geoConex = new GeopistaGeneradorListadosConexionBD();
  Connection  conn=null;
  try{
  conn = geoConex.abrirConexion();
  conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}




	/**
	 * This method initializes cmdImagen	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCmdImagen() {
		if (cmdImagen == null) {
			cmdImagen = new JButton();
			cmdImagen.setBounds(140, 440, 176, 27);
			cmdImagen.setText(aplicacion.getI18nString("generador.app.reports.cargar.imagen"));
			cmdImagen.setName("cmdImagen");
			cmdImagen.setEnabled(false);
			cmdImagen.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					//cargamos el fichero de imagen
				    JFileChooser chooser = new JFileChooser();
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				        lblBotonImagen.setText(chooser.getSelectedFile().getAbsolutePath());
				    }else{
				        lblBotonImagen.setText(null);
				    }
				}
			});
		}
		return cmdImagen;
	}
 } 


