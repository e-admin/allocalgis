package com.geopista.app.reports;


import com.geopista.app.AppContext;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import java.net.URL;

import java.sql.Connection;

import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.SearchView;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import java.awt.Color;

 


/**
 * Clase que mostrará la información general que se incluirá en el informe
 */

public class GeopistaGeneradorListadosSeleccionInforme extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private DefaultListModel modeloList = new DefaultListModel();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private JPanel jPanel1 = new JPanel();
  private JLabel lblSeleccionInformes = new JLabel();
  private JSeparator jSeparator2 = new JSeparator();
  private JRadioButton optNuevoInforme = new JRadioButton();
  private JRadioButton optSeleccionInforme = new JRadioButton();
  private JLabel lstFicheros = new JLabel();
  private JList lstInformes = new JList(modeloList);
  private JScrollPane jScrollPane1 = new JScrollPane(lstInformes);

  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel lblDescripcion = new JLabel();
  private JTextField txtDescripcion = new JTextField();
  private JLabel lblTitulo = new JLabel();
  private JTextField txtTituloInforme = new JTextField();
  private boolean accesoInfReferencia;
  private boolean accesoPatrimonio;
  private boolean accesoInfraestructuras;  
  public Connection con =null;
  private File selectFile = null;

  public GeopistaGeneradorListadosSeleccionInforme()
  {
    try
    {
        
//--          if (conectado)
//--          {
            jbInit();
            
//--          }else{
           
//--          }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  
  private void jbInit() throws Exception
  {
    //
     
      GeopistaPermission paso = new GeopistaPermission("Geopista.InfReferencia.GeneradorInforme");
      accesoInfReferencia = aplicacion.checkPermission(paso,"Informacion de Referencia");
      accesoPatrimonio = aplicacion.checkPermission(paso,"Patrimonio");
      accesoInfraestructuras= aplicacion.checkPermission(paso,"Infraestructuras");
    //Iniciamos la ayuda
      
      String helpHS="ayuda.hs";
      com.geopista.app.help.HelpLoader.installHelp(helpHS,"generadorInformeSeleccion",this);

     //fin de la ayuda
    //Seleccionamos la opción de nuevo informe por defecto
      optNuevoInforme.setSelected(false);
      lstInformes.setSelectionMode(0);
      lstInformes.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          lstInformes_mouseClicked(e);
        }
      });
      lstInformes.addKeyListener(new java.awt.event.KeyAdapter() {   
      	public void keyReleased(java.awt.event.KeyEvent e) {    
      		String cadena = (String) lstInformes.getSelectedValue();
      		lstInformes.ensureIndexIsVisible(lstInformes.getSelectedIndex());
      		lstInformes.setSelectedValue((String)cadena,true);
      	} 
      
      });
    
    this.setLayout(null);
    this.setSize(new Dimension(614, 500));
    jPanel1.setLayout(null);
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    lblSeleccionInformes.setText(aplicacion.getI18nString("informe.datos.seleccion.informe"));
    lblSeleccionInformes.setBounds(new Rectangle(125, 5, 185, 20));
    jPanel1.setBounds(new Rectangle(10, 15, 750, 500));
    jSeparator1.setBounds(new Rectangle(0, 90, 665, 5));
    lblTitulo.setText(aplicacion.getI18nString("informe.datos.seleccion.titulo.fichero"));
    lblTitulo.setBounds(new Rectangle(140, 230, 95, 15));
    txtTituloInforme.setBounds(new Rectangle(140, 250, 440, 20));
    
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setIcon(IconLoader.icon((String) blackboardInformes.get("tipoBanner")));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblDescripcion.setText(aplicacion.getI18nString("informe.datos.seleccion.descripcion"));
    lblDescripcion.setBounds(new Rectangle(140, 290, 95, 20));
    txtDescripcion.setBounds(new Rectangle(140, 320, 445, 95));
    jSeparator2.setBounds(new Rectangle(5, 205, 665, 5));
    optNuevoInforme.setText(aplicacion.getI18nString("informe.datos.seleccion.informe.nuevo"));
    optNuevoInforme.setBounds(new Rectangle(125, 30, 300, 20));
    optNuevoInforme.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          optNuevoInforme_actionPerformed(e);
        }
      });
    optSeleccionInforme.setText(aplicacion.getI18nString("informe.datos.seleccion.informe.seleccionar"));
    optSeleccionInforme.setBounds(new Rectangle(125, 55, 210, 20));
    optSeleccionInforme.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          optSeleccionInforme_actionPerformed(e);
        }
      });
    lstFicheros.setText(aplicacion.getI18nString("informe.datos.seleccion.informe.listado.ficheros"));
   
    jScrollPane1.setBounds(new Rectangle(130, 115, 460, 85));
    lstInformes.setBounds(new Rectangle(130, 115, 460, 85));
    jScrollPane1.getViewport().add(lstInformes, null);
    txtDescripcion.setEditable(false);
    txtTituloInforme.setEditable(false);
    
    jPanel1.add(jScrollPane1, null);
    jPanel1.add(lstFicheros, null);
    jPanel1.add(optSeleccionInforme, null);
    jPanel1.add(optNuevoInforme, null);
    jPanel1.add(jSeparator2, null);
    jPanel1.add(txtTituloInforme, null);
    jPanel1.add(lblTitulo, null);
    jPanel1.add(txtDescripcion, null);
    jPanel1.add(lblDescripcion, null);
    jPanel1.add(jSeparator1, null);
    jPanel1.add(lblSeleccionInformes, null);
    this.add(lblImagen, null);
    this.add(jPanel1, null);
    
    
  }
 
  public void enteredFromLeft(Map dataMap)
  {
   accesoInfReferencia=false;
   accesoPatrimonio=false;
   accesoInfraestructuras=false;
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      
        blackboardInformes.put("fichero",selectFile);
      
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
    private WizardContext wizardContext;
 
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
     if (optNuevoInforme.isSelected()){
             //comprobamos los permisos
            if ((accesoInfReferencia) && (accesoPatrimonio) && (accesoInfraestructuras)){
                return true;
            }else{
              return false;
            }
     }else{
        //comprobar que realmente existan ficheros
        if (lstInformes.getModel().getSize()==0){
          return false;
        }else{
          if ((lstInformes.getSelectedValue())==null){
            return false;
          }else{
                 //comprobamos los permisos
            if ((accesoInfReferencia) && (accesoPatrimonio) && (accesoInfraestructuras)){
                return true;
            }else{
              return false;
            }
          }
        }
     }
     
    }

    private String nextID=null;
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
    if (nextID==null){
     if (!optNuevoInforme.isSelected()){
           return "7";
     }else{
          return "2";
     }
    }else{
      return nextID;
    }
    }

  private void optNuevoInforme_actionPerformed(ActionEvent e)
  {
    if (optNuevoInforme.isSelected()){
        //desactivamos el resto
        modeloList.removeAllElements();
        txtDescripcion.setText(null);
        txtTituloInforme.setText(null);
        optSeleccionInforme.setSelected(false);
         accesoInfReferencia=true;
         accesoPatrimonio=true;
         accesoInfraestructuras=true;
        wizardContext.inputChanged();

    }    
  }

  private void optSeleccionInforme_actionPerformed(ActionEvent e)
  {
        if (optSeleccionInforme.isSelected()){
         accesoInfReferencia=false;
         accesoPatrimonio=false;
         accesoInfraestructuras=false;
        //desactivamos el resto
         File directorio = new File(aplicacion.getPath("informes.guardados") );
         String[] children = directorio.list();
            if (children == null) {
              // Either dir does not exist or is not a directory
          } else {
              for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                modeloList.addElement(children[i]);
          }
       }

        optNuevoInforme.setSelected(false);
        wizardContext.inputChanged();
    }    
  }

  private void lstInformes_mouseClicked(MouseEvent e)
  {
     String fichero = (String) lstInformes.getSelectedValue();
     //Con este nombre de fichero tengo que ir a su directorio, someterlo
     //a una lectura y sacar los campos de Nombre y Descripcion.
     String ruta = aplicacion.getPath("informes.guardados");
       accesoInfReferencia=true;
       accesoPatrimonio=true;
       accesoInfraestructuras=true;
     try {
     SAXBuilder builder = new SAXBuilder(false);
     selectFile = new File(ruta,fichero);
     Document doc=builder.build(selectFile); 
     //Saco el raiz
    Element raiz = doc.getRootElement();
    txtTituloInforme.setText(raiz.getAttributeValue("title"));
    Element descripcion = raiz.getChild("description");
    txtDescripcion.setText(descripcion.getText());
    wizardContext.inputChanged();
        
     }catch (Exception excp){
        excp.printStackTrace();
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

