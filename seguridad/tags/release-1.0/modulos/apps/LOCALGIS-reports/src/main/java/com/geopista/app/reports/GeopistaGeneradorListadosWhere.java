package com.geopista.app.reports;


import com.geopista.app.AppContext;
import com.geopista.app.reports.GeopistaEstructuraCampos;
import com.geopista.app.reports.GeopistaGeneradorListadosConexionBD;
import com.geopista.app.reports.GeopistaObjetoInformeWhere;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.app.reports.GeopistaSeleccionElementosTabla;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.net.URL;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Enumeration;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

//import oracle.jdeveloper.layout.VerticalFlowLayout;





public class GeopistaGeneradorListadosWhere  extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  private JPanel jPanel1 = new JPanel();
//  private GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();
  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();
  private JComboBox cmbTabla = new JComboBox();
  private JLabel lblCampo = new JLabel();
  private JComboBox cmbCampo = new JComboBox();
  private JPanel jPanel2 = new JPanel();
  private JLabel lblNombreTabla = new JLabel();
  private JLabel lblCamposInforme = new JLabel();
  private JLabel lblNoEspaciales = new JLabel();
  private JTextField txtValor1 = new JTextField();
  private JButton btnAnadir = new JButton();
  private JPanel jPanel3 = new JPanel();
  private JLabel jLabel2 = new JLabel();
  private JComboBox cmbTabla1 = new JComboBox();
  private JComboBox cmbEspacial = new JComboBox();
  private JComboBox cmbTabla2 = new JComboBox();

  private DefaultTableModel model = new DefaultTableModel()
  {
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }
    };

  
  private JTable tablaCampos = new JTable(model);

  private JButton btnAnadir2 = new JButton();
  
  private int filas = 0;
  private JComboBox cmbOperadores = new JComboBox();
  private JTextField txtValor2 = new JTextField();
  private JLabel lblUnion = new JLabel();
  private JTextField txtMetros = new JTextField();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private CardLayout cardLayout1 = new CardLayout();
  private BorderLayout borderLayout1 = new BorderLayout();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private GridBagLayout gridBagLayout4 = new GridBagLayout();
  private GridBagLayout gridBagLayout5 = new GridBagLayout();
  private GridBagLayout gridBagLayout6 = new GridBagLayout();
  private GridBagLayout gridBagLayout7 = new GridBagLayout();
  private GridLayout gridLayout1 = new GridLayout();
 
  private BorderLayout borderLayout3 = new BorderLayout();
  private GridBagLayout gridBagLayout8 = new GridBagLayout();

  private JPanel jPanel5 = new JPanel();
  private JTextField txtWhere = new JTextField();
  private JComboBox cmbUnion = new JComboBox();
  private JCheckBox chkNegado = new JCheckBox();
  private JComboBox cmbCondiciones = new JComboBox();
  private JScrollPane jPanel4 = new JScrollPane();
  private JButton btnAnadirCondicion = new JButton();
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  
  private Connection con =null;
  private GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
  private ArrayList aLcamposEnTabla = new ArrayList();

  public GeopistaGeneradorListadosWhere ()
  {
    try
    {
       //blackboardInformes.put("hola","Saludos desde el primer panel");
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
    this.setSize(new Dimension(782, 515));
    jPanel1.setBounds(new Rectangle(15, 0, 750, 500));
    jPanel1.setLayout(null);
    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
     DefaultListModel modeloList = new DefaultListModel();
    //Cargamos la lista de capas
     ArrayList capas = geopistaListados.capasGeopista();
     Iterator i = capas.iterator();
    cmbTabla.setBounds(new Rectangle(45, 30, 235, 20));
        //Iniciamos la ayuda
    try {
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda
      hb.enableHelpKey(this,"generadorInformeSeleccionRegistros",hs);
    }catch (Exception e) {e.printStackTrace();}
  
    lblCampo.setText(aplicacion.getI18nString("generador.app.reports.where.campo"));
    lblCampo.setBounds(new Rectangle(10, 60, 50, 15));
    cmbCampo.setBounds(new Rectangle(45, 55, 235, 20));
    jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel2.setLayout(null);
    
    lblNombreTabla.setText(aplicacion.getI18nString("generador.app.reports.where.tabla"));
    lblNombreTabla.setBounds(new Rectangle(10, 30, 40, 20));
    
    lblCamposInforme.setText(aplicacion.getI18nString("generador.app.reports.where.seleccion.registros"));

    lblNoEspaciales.setText(aplicacion.getI18nString("generador.app.reports.where.no.espaciales"));
    lblNoEspaciales.setBounds(new Rectangle(10, 5, 95, 25));
    txtValor1.setBounds(new Rectangle(375, 55, 60, 20));
    
    //btnAnadir.setText(aplicacion.getI18nString("generador.app.reports.where.anadir"));

    btnAnadir.setIcon(IconLoader.icon("anadir_campo.gif"));
        
    btnAnadir.setBounds(new Rectangle(555, 50, 35, 30));

    jPanel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel3.setLayout(null);
    
    jLabel2.setText(aplicacion.getI18nString("generador.app.reports.where.espaciales"));
    jLabel2.setBounds(new Rectangle(10, 10, 95, 15));
    cmbTabla1.setBounds(new Rectangle(10, 30, 320, 20));
    cmbEspacial.setBounds(new Rectangle(10, 55, 320, 20));

    
    cmbTabla2.setBounds(new Rectangle(10, 80, 320, 20));
    btnAnadir2.setIcon(IconLoader.icon("anadir_campo.gif"));
//    btnAnadir2.setText(aplicacion.getI18nString("generador.app.reports.where.anadir"));
    btnAnadir2.setBounds(new Rectangle(560, 75, 35, 30));
    
     while (i.hasNext())
     {
        modeloList.addElement(i.next());
     }
        


     //Creamos la lista con el modelo anteriormente definido      
    jPanel1.setSize(new Dimension(750, 500));

    
    
    //Ponemos a mano los Módulos , prueba

    //Ponemos a mano la horientacion
    
    btnAnadirCondicion.setIcon(IconLoader.icon("anadir_campo.gif"));
    jButton1.setIcon(IconLoader.icon("verificar_formula.gif"));
    jPanel5.add(jButton1, null);
    jPanel5.add(btnAnadirCondicion, null);
    jPanel5.add(txtWhere, null);
    jPanel5.add(cmbUnion, null);
    jPanel5.add(chkNegado, null);
    jPanel5.add(cmbCondiciones, null);


tablaCampos.addMouseListener(new java.awt.event.MouseAdapter() { 
	public void mouseClicked(java.awt.event.MouseEvent e) {    
	    
	//Incializamos todos los datos.
	    int nFila;
	    cmbTabla.setSelectedIndex(-1);
	    cmbCampo.setSelectedIndex(-1);
	    cmbOperadores.setSelectedIndex(-1);
	    txtValor1.setText("");
	    txtValor2.setText("");
	    txtValor2.setVisible(false);
	    
	    cmbTabla1.setSelectedIndex(-1);
	    cmbTabla2.setSelectedIndex(-1);
	    cmbEspacial.setSelectedIndex(-1);
	    txtMetros.setText("");
	    txtMetros.setVisible(false);
	    
	    //Con la fila seleccionada
	    nFila = tablaCampos.getSelectedRow();
	    String orden = (String) tablaCampos.getValueAt(nFila,0);
	    ArrayList tmp = new ArrayList();
	    Iterator i = aLcamposEnTabla.iterator();
	    while (i.hasNext()){
	        GeopistaSeleccionElementosTabla campos = (GeopistaSeleccionElementosTabla)i.next();
	        if (campos.getOrden().equals(orden)){
	            //Hemos encontrado el registro vamos a colocar los campos
	            if (campos.getTipo().equals("N")){
	                //Es Normal.
	                //Localizamos la tabla.
	                for (int j=0; j<cmbTabla.getItemCount();j++){
	                    if (cmbTabla.getItemAt(j).equals(campos.getTabla())){
	                        cmbTabla.setSelectedIndex(j);
	                        break;
	                    }
	                }
	                //Localizamos el campo 
	                for (int j=0; j<cmbCampo.getItemCount();j++){
	                    if (cmbCampo.getItemAt(j).equals(campos.getCampo())){
	                        cmbCampo.setSelectedIndex(j);
	                        break;
	                    }
	                }
	                //Localizamos el Operador
	                for (int j=0; j<cmbOperadores.getItemCount();j++){
	                    if (cmbOperadores.getItemAt(j).equals(campos.getOperador())){
	                        cmbOperadores.setSelectedIndex(j);
	                        break;
	                    }
	                }
	               //Ponemos el valor 1
	                txtValor1.setText((String)campos.getValor1());
	               //Ponemos el valor 2 
	                if (!campos.getValor2().equals("-999")){
	                    lblUnion.setVisible(true);
	                    txtValor2.setText((String) campos.getValor2());
	                }
	                
	                
	            }else{
	                //El campo es espacial.
	                //Tabla 1
	                for(int k=0; k<cmbTabla1.getItemCount();k++){
	                    if (cmbTabla1.getItemAt(k).equals((String)campos.getTabla1())){
	                        cmbTabla1.setSelectedIndex(k);
	                        break;
	                    }
	                }
//	              Tabla 2
	                for(int k=0; k<cmbTabla2.getItemCount();k++){
	                    if (cmbTabla2.getItemAt(k).equals((String)campos.getTabla2())){
	                        cmbTabla2.setSelectedIndex(k);
	                        break;
	                    }
	                }
	                
	                //Campo Espacial
	                for (int k=0; k<cmbEspacial.getItemCount();k++){
	                    if (cmbEspacial.getItemAt(k).equals((String) campos.getOperador2())){
	                        cmbEspacial.setSelectedIndex(k);
	                        break;
	                    }
	                }
	              //La Distancia
	              if (!campos.getDistancia().equals("-999")){
	                  txtMetros.setVisible(true);
	                  txtMetros.setText((String) campos.getDistancia());
	              }
	            }
	            break;
	        }
	        
	    };
	    
	

	    
	    
	}
});
btnAnadirCondicion.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt) {

         String condicion1 ="";
         String condicion2 ="";
         String condicion3 ="";
         // recogemos los valores del chkNegado, el CmbCondiciones, y cmbUnion
         if (chkNegado.isSelected())
            { condicion1 ="NOT "; }
         else
            {condicion1 =" " ;}
          // con las condiciones
          condicion2 = (String) cmbCondiciones.getSelectedItem();
          // la Unión 
          if (cmbUnion.getSelectedItem().equals("--")) 
            {
              condicion3 ="";
            }else{
              if (cmbUnion.getSelectedItem().equals("Y")){
                  condicion3 = " AND ";
              }else{
                condicion3=" OR ";
              }
            }
            
            txtWhere.setText(txtWhere.getText() + condicion3 + condicion1 + condicion2 ); 
            	
            
        }
    });



    
    
    jPanel2.add(lblUnion, null);
    jPanel2.add(txtValor2, null);
    jPanel2.add(cmbOperadores, null);
    jPanel2.add(btnAnadir, null);
    jPanel2.add(txtValor1, null);
    jPanel2.add(lblNoEspaciales, null);
    jPanel2.add(lblNombreTabla, null);
    jPanel2.add(cmbTabla, null);
    jPanel2.add(cmbCampo, null);
    jPanel2.add(lblCampo, null);
    txtValor2.setVisible(false);
    jPanel3.add(txtMetros, null);
    jPanel3.add(btnAnadir2, null);
    jPanel3.add(cmbTabla2, null);
    jPanel3.add(cmbEspacial, null);
    jPanel3.add(cmbTabla1, null);
    jPanel3.add(jLabel2, null);
    jPanel1.add(lblImagen, null);
    jPanel4.getViewport().add(tablaCampos, null);
    jPanel1.add(jPanel4, null);
    jPanel1.add(jPanel3, null);
    jPanel1.add(lblCamposInforme, null);
    jPanel1.add(jPanel2, null);
    jPanel1.add(jButton2, null);

    //Cargamos las capas en la lista
     ArrayList tablasGeometria = new ArrayList(); 
      tablasGeometria = geopistaListados.tablasConGeometria();
      Iterator iGeometria = tablasGeometria.iterator();
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    btnAnadir2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAnadir2_actionPerformed(e);
        }
      });
    btnAnadir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAnadir_actionPerformed(e);
        }
      });
    jButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
    jButton2.setBounds(new Rectangle(700, 350, 35, 30));
//    jButton2.setText(aplicacion.getI18nString("generador.app.reports.where.borrar"));
      jButton2.setIcon(IconLoader.icon("borrar_campo.gif"));
    jButton1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton1_actionPerformed(e);
        }
      });
    jButton1.setBounds(new Rectangle(545, 40, 35, 30));

    txtMetros.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          txtMetros_focusLost(e);
        }
      });
    cmbOperadores.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          cmbOperadores_itemStateChanged(e);
        }
      });
    cmbEspacial.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          cmbEspacial_itemStateChanged(e);
        }
      });
    cmbOperadores.addPropertyChangeListener(new PropertyChangeListener()
      {
        public void propertyChange(PropertyChangeEvent e)
        {
          cmbOperadores_propertyChange(e);
        }
      });
    btnAnadirCondicion.setBounds(new Rectangle(545, 5, 35, 30));

    jPanel4.setBounds(new Rectangle(135, 240, 560, 140));
      while (iGeometria.hasNext()){
          String geometria = (String) iGeometria.next();
          cmbTabla1.addItem(geometria);
          cmbTabla2.addItem(geometria);
      }
 

      cmbTabla.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
          //Recogemos el tipo de inmuebles
          //JComboBox combo =(JComboBox) evt.getSource();

              cmbCampo.removeAllItems(); 
              String  nombre = (String) cmbTabla.getSelectedItem();
              ArrayList capas = geopistaListados.camposDeUnaTabla(nombre);
              Iterator i = capas.iterator();
              while (i.hasNext()){
                  String Campo = (String)i.next();
                  cmbCampo.addItem(Campo);
              }
        

            }
         });  
 
   // añadimos en el de operacion la suma y longitud

   // en el cruce de las tablas Contener e Interseccion
   
     cmbEspacial.addItem(aplicacion.getI18nString("generador.app.reports.where.intersecciona.con"));
     cmbEspacial.addItem(aplicacion.getI18nString("generador.app.reports.where.contenido.en"));
     cmbEspacial.addItem(aplicacion.getI18nString("generador.app.reports.where.a.distancia.de"));

   
     
    // Creamos las columnas

    tablaCampos.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    cmbOperadores.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbOperadores_actionPerformed(e);
        }
      });
    cmbCondiciones.setBounds(new Rectangle(140, 12, 90, 20));
    
    chkNegado.setText(aplicacion.getI18nString("generador.app.reports.where.negado"));
    chkNegado.setBounds(new Rectangle(10, 15, 120, 15));
    cmbUnion.setBounds(new Rectangle(240, 12, 155, 20));
    cmbUnion.addItem("--");
    cmbUnion.addItem("Y");
    cmbUnion.addItem("O");
    
    txtWhere.setBounds(new Rectangle(10, 40, 510, 25));
    jPanel5.setBounds(new Rectangle(155, 405, 590, 75));
    jPanel5.setLayout(null);
    jPanel5.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    lblCamposInforme.setBounds(new Rectangle(135, 10, 180, 15));
    jPanel2.setBounds(new Rectangle(135, 30, 600, 85));
    jPanel3.setBounds(new Rectangle(135, 120, 600, 110));
    txtMetros.setBounds(new Rectangle(345, 55, 80, 20));
    lblUnion.setBounds(new Rectangle(445, 55, 40, 25));
    lblUnion.setText("Y");
    lblUnion.setVisible(false);
    txtValor2.setBounds(new Rectangle(490, 55, 60, 20));
    cmbOperadores.setBounds(new Rectangle(285, 55, 85, 20));
    //Añadimos los operadores

    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.igual"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.mayor.que"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.mayor.igual"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.menor.que"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.menor.o.igual"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.distinto"));
    cmbOperadores.addItem(aplicacion.getI18nString("generador.app.reports.where.entre"));
//    cmbOperadores.addItem("Distinto de");
    tablaCampos.setAutoscrolls(true);
    // Add header in NORTH slot
    // Add table itself to CENTER slot
    this.add(jPanel5, null);
    this.add(jPanel1, null);
    model.addColumn(aplicacion.getI18nString("generador.app.reports.where.tabla.orden"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.where.tabla.consulta"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.where.tabla.etiqueta"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.where.tabla.campo"));
    //quitamos el orden y el campo
     tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(3));
     tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(0));
 

   }
 
  public void enteredFromLeft(Map dataMap)
  {
      // Pasamos el list del blackBoard al combo
    cmbTabla.removeAllItems();
      GeopistaObjetoInformeDatosGenerales datosGenerales = new GeopistaObjetoInformeDatosGenerales();
      datosGenerales = (GeopistaObjetoInformeDatosGenerales) blackboardInformes.get("datosGenerales");
      java.util.List listas = (java.util.List) datosGenerales.getCapas();
      Iterator i = listas.iterator();
      while (i.hasNext())
        {
          cmbTabla.addItem((String) i.next());
        }
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      
        ArrayList alistaWheres = new ArrayList();

        for (int i=0;i<model.getRowCount();i++)
          {
          GeopistaObjetoInformeWhere objetoCampos = new GeopistaObjetoInformeWhere();
          /*objetoCampos.setOrden((String) tablaCampos.getValueAt(i,0));
          objetoCampos.setConsulta((String) tablaCampos.getValueAt(i,1));
          objetoCampos.setEtiqueta((String) tablaCampos.getValueAt(i,2));
          objetoCampos.setCampo((String) tablaCampos.getValueAt(i,3));*/
          objetoCampos.setOrden((String) tablaCampos.getModel().getValueAt(i,0));
          objetoCampos.setConsulta((String) tablaCampos.getModel().getValueAt(i,1));
          objetoCampos.setEtiqueta((String) tablaCampos.getModel().getValueAt(i,2));
          objetoCampos.setCampo((String) tablaCampos.getModel().getValueAt(i,3));
       
                                        
          alistaWheres.add(objetoCampos);
          } 

        //Almacenar en el blackboard el ArrayList
        blackboardInformes.put("listaWheres",alistaWheres);
        String sentencia = txtWhere.getText();

        for (int k=0;k<cmbCondiciones.getItemCount() ;k++)
             {
             String cX = (String) cmbCondiciones.getItemAt(k);
             String cY = localizarCampo(model,cX);
             sentencia = replace(sentencia,(String) cX,cY);
             }

             blackboardInformes.put("SentenciaTransformada",sentencia);

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
      return "5";
    }
    private WizardContext wizardContext;    
    public void setWizardContext(WizardContext wd)
    {
      wizardContext =wd;
    }
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
       
        return true;
      
    }


  private String nextID="6";
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
      return nextID ;
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

  private void jButton2_actionPerformed(ActionEvent e)
  {
    if (tablaCampos.getSelectedRow() ==-1)
      {
        JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.seleccione.fila"));
      }else{
        String consulta =(String) model.getValueAt(tablaCampos.getSelectedRow(),1);
        for(int p=0;p<cmbCondiciones.getItemCount();p++)
        {
          String condicion = (String) (cmbCondiciones.getItemAt(p));
          if (condicion.equals(consulta))
              {
                cmbCondiciones.removeItemAt(p);
                break;
              }
        }
        
        model.removeRow(tablaCampos.getSelectedRow());
        wizardContext.inputChanged();
      }
    
  }

  private void jComboBox1_actionPerformed(ActionEvent e)
  {
  }




  private void cmbOperadores_actionPerformed(ActionEvent e)
  {
  }

  private void cmbOperadores_propertyChange(PropertyChangeEvent e)
  {
  
  }

  private void cmbTabla_actionPerformed(ActionEvent e)
  {
  }

  private void cmbEspacial_itemStateChanged(ItemEvent e)
  {
    String valor = (String) e.getItem();
    if (valor.equals(aplicacion.getI18nString("generador.app.reports.where.a.distancia.de"))) {
    txtMetros.setVisible(true);
    }else{
    txtMetros.setVisible(false);
    }
  }

  private void cmbOperadores_itemStateChanged(ItemEvent e)
  {
    String cadena = (String) e.getItem();
    if (cadena.equals(aplicacion.getI18nString("generador.app.reports.where.entre"))){
      lblUnion.setVisible(true);
      txtValor2.setVisible(true);
    }else{
      lblUnion.setVisible(false);
      txtValor2.setVisible(false);
    }
  }

  private void txtMetros_focusLost(FocusEvent e)
  {
    try {
      double valor =Double.parseDouble(String.valueOf(txtMetros.getText()));
    }catch(Exception ex){
      txtMetros.setText("0");
      txtMetros.setFocusable(true);

    }
  }

  private void jButton1_actionPerformed(ActionEvent e)
  {
        //Comprobar que no existen ; 


        if (txtWhere.getText().length()!=0)    
        {
              if (txtWhere.getText().indexOf(";")!=-1) {
                //Con ; no existe la sentencia correcta.
              }else{
                  int parentesisIzq =0;
                  int parentesisDer=0;
                  for (int i=0;i<txtWhere.getText().length();i++)
                  {
                    if (txtWhere.getText().charAt(i)=='(')
                    {
                      parentesisIzq=parentesisIzq+1;
                    }else{
                      if (txtWhere.getText().charAt(i)==')') {
                        parentesisDer=parentesisDer+1;
                      }
                    }                
                  }
            
                  if (parentesisDer==parentesisIzq) {
                      // comprobar que es correcta la sintaxis
                      //Transformamos la cadena a true's
                      String cadenaWhere = txtWhere.getText();
                      for (int k=0;k<cmbCondiciones.getItemCount() ;k++)
                      {
                        cadenaWhere = replace(cadenaWhere,(String) cmbCondiciones.getItemAt(k),"false");
                      }
                       if (!cadenaWhere.substring(0,3).toUpperCase().equals("NOT")) 
                         {
                          cadenaWhere="Not " + cadenaWhere;
                         }
                      //   cadenaWhere= cadenaWhere.toUpperCase();
                         
                
                      cadenaWhere = "Select 1 Where " + cadenaWhere;
                      try {
                          String exito = compruebaCondiciones(cadenaWhere);
                                            if (exito.equals("N")){
                                                JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.error.sentencia"));
                                            }else{
                                               JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.sentencia.correcta"));}
                      }catch (java.sql.SQLException exx)
                        {
                          JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.error.sentencia"));
                        }
/*
                   if (exito.equals("N")) {
                        
                      }else{
  
                      }*/
                  }else{
                    JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.parentesis"));
                    parentesisIzq=0;
                    parentesisDer=0;
                  }
              }
        }
      } 


/**
 * Metodo que remplaza una cadena de texto por otra daa
 * @param String str, cadena
 * @param String pattern, cadena a buscar
 * @param String replace, cadena de reemplazo
 * @return String replace,cadena transformada
 */
static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

  private void btnAnadir_actionPerformed(ActionEvent e)
  {
       GeopistaEstructuraCampos campos = new GeopistaEstructuraCampos();
       GeopistaSeleccionElementosTabla camposTablas = new GeopistaSeleccionElementosTabla();
       
        if  ((cmbCampo.getSelectedIndex()!=-1))
            {
                //Obtenemos el tipo de datos para poner el valor
                GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();
                //dejarlo pendiente voy a validar la clausula sql
                int tipoDatos = geopistaListados.tipoDatosCampo((String) cmbTabla.getSelectedItem(),(String) cmbCampo.getSelectedItem());
       
            // orden 
            filas = tablaCampos.getRowCount();
            campos.setOrden(filas);

            // Comprobamos los operadores
            if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.igual"))) {

            switch (tipoDatos) {
            case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" = '" + txtValor1.getText()+"'");
                      campos.setCampo(cmbTabla.getSelectedItem()+"."+ cmbCampo.getSelectedItem() +" = '" + txtValor1.getText()+"'"); 
                      break;
            default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" = " + txtValor1.getText());
                      campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" = " + txtValor1.getText());
                      break;
            }
                
            }else{
                if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.mayor.que"))) {
                     switch (tipoDatos) {
                                case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" > '" + txtValor1.getText()+"'");
                                campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" > '" + txtValor1.getText()+"'"); 
                      break;
                      default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" > " + txtValor1.getText());
                                campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" > " + txtValor1.getText());
                                break;
                     }

                }else{
                   if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.menor.que"))) {
                          switch (tipoDatos) {
                          case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" < '" + txtValor1.getText()+"'");
                                    campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" < '" + txtValor1.getText()+"'"); 
                                    break;
                          default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" < " + txtValor1.getText());
                                    campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" < " + txtValor1.getText());
                                    break;
                          }

                   }else{
                     if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.mayor.igual"))) {
                         switch (tipoDatos) {
                          case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" >= '" + txtValor1.getText()+"'");
                                    campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" >= '" + txtValor1.getText()+"'"); 
                                    break;
                          default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" >= " + txtValor1.getText());
                                    campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" >= " + txtValor1.getText());
                                    break;
                         }

                     }else{
                        if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.menor.o.igual"))) {
                            switch (tipoDatos) {
                            case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" <= '" + txtValor1.getText()+"'");
                                      campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" <= '" + txtValor1.getText()+"'"); 
                                      break;
                            default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" <= " + txtValor1.getText());
                                      campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" <= " + txtValor1.getText());
                                      break;
                            }

                        }else{
                          if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.distinto"))) {
                            switch (tipoDatos) {
                            case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" <> '" + txtValor1.getText()+"'");
                                      campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" <> '" + txtValor1.getText()+"'"); 
                                      break;
                            default:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" <> " + txtValor1.getText());
                                      campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" <> " + txtValor1.getText());
                                      break;
                            }

                          }else{

                              switch (tipoDatos) {
                              case 12:  campos.setEtiqueta(cmbCampo.getSelectedItem() +" "+ aplicacion.getI18nString("generador.app.reports.where.entre") +"  " + txtValor1.getText() +" y " + txtValor2.getText());
                                        campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" >= '" + txtValor1.getText()+"' and " + cmbCampo.getSelectedItem() +" <= '" + txtValor2.getText()+"'"); 
                                        break;
                              default: campos.setEtiqueta(cmbCampo.getSelectedItem() +" "+ aplicacion.getI18nString("generador.app.reports.where.entre") +" " + txtValor1.getText() +" y " + txtValor2.getText());
                                        campos.setCampo(cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" >= " + txtValor1.getText()+" and " + cmbTabla.getSelectedItem()+"."+cmbCampo.getSelectedItem() +" <= " + txtValor2.getText()+""); 
                                        break;
                              }

                          }
                        }
                     }
                   }
                }
            }


            //Se hara esto si se ha llenado el campo y el valor 1 o el valor 2.

            if (cmbOperadores.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.entre"))){
                if ((txtValor1.getText().equals("")) && (txtValor2.getText().equals(""))) {
                    JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.limites"));
                }else{
                      Object[] cadena = new Object[4];
                      cadena[0]= String.valueOf(filas);
                      cadena[1]="C" + filas;
                      cadena[2]=campos.getEtiqueta();
                      cadena[3]=campos.getCampo();
                      cmbCondiciones.addItem(cadena[1]);
                      model.insertRow(filas, cadena);
                      
                      //añadimos a la estructura GeopistaSeleccionElementosTabla los datos
                      camposTablas.setOrden("C"+filas);
                      camposTablas.setTipo("N");
                      camposTablas.setTabla((String) cmbTabla.getSelectedItem());
                      camposTablas.setCampo((String)cmbCampo.getSelectedItem());
                      camposTablas.setOperador((String) cmbOperadores.getSelectedItem());
                      camposTablas.setValor1(txtValor1.getText());
                      if (txtValor2.isVisible()){
                         camposTablas.setValor2(txtValor2.getText());
                      }else{
                          camposTablas.setValor2("-999");
                      } 
                      
                      camposTablas.setTabla1("-999");
                      camposTablas.setOperador2("-999");
                      camposTablas.setTabla2("-999");
                      camposTablas.setDistancia("-999");
                      aLcamposEnTabla.add(camposTablas);
                      
                      
                      lblUnion.setVisible(false);
                      txtValor1.setText(null);
                      txtValor2.setText(null);
                      txtValor2.setVisible(false);
                      cmbTabla.setSelectedIndex(-1);
                      cmbCampo.setSelectedIndex(-1);
                      wizardContext.inputChanged();
                      
                }
                
            }else{

            if (txtValor1.getText().equals("")){
               JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.valor"));            
            }else{
              Object[] cadena = new Object[4];
              cadena[0]= String.valueOf(filas);
              cadena[1]="C" + filas;
              cadena[2]=campos.getEtiqueta();
              cadena[3]=campos.getCampo();
              cmbCondiciones.addItem(cadena[1]);
              
              model.insertRow(filas, cadena);
//            añadimos a la estructura GeopistaSeleccionElementosTabla los datos
              camposTablas.setOrden("C"+filas);
              camposTablas.setTipo("N");
              camposTablas.setTabla((String) cmbTabla.getSelectedItem());
              camposTablas.setCampo((String)cmbCampo.getSelectedItem());
              camposTablas.setOperador((String) cmbOperadores.getSelectedItem());
              camposTablas.setValor1(txtValor1.getText());
              if (txtValor2.isVisible()){
                 camposTablas.setValor2(txtValor2.getText());
              }else{
                  camposTablas.setValor2("-999");
              } 
              
              camposTablas.setTabla1("-999");
              camposTablas.setOperador2("-999");
              camposTablas.setTabla2("-999");
              camposTablas.setDistancia("-999");
              aLcamposEnTabla.add(camposTablas);
              lblUnion.setVisible(false);
              txtValor1.setText(null);
              txtValor2.setText(null);
              txtValor2.setVisible(false);
              cmbTabla.setSelectedIndex(-1);
              cmbCampo.setSelectedIndex(-1);
              wizardContext.inputChanged();
             }
            }
        
            }else{
            JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.seleccion.campo"));
            //Nada porqu es erroneo
            }

  }

  private void btnAnadir2_actionPerformed(ActionEvent e)
  {
   //Recoger los datos : -Tabla, Nombre del Campo, Agrupamiento      
            GeopistaEstructuraCampos campos = new GeopistaEstructuraCampos();
            // orden 

            String orden="0";
            if ((cmbTabla1.getSelectedIndex()!=-1) &&
               (!cmbTabla1.getSelectedItem().equals("--")) &&
               (cmbTabla2.getSelectedIndex()!=-1) &&
               (!cmbTabla2.getSelectedItem().equals("--")))
              {
                    
                    if (tablaCampos.getRowCount()==0)
                    {
                      orden="0";
                      campos.setOrden(Integer.parseInt(orden));
                    }else{ 
                       filas = tablaCampos.getRowCount();
                       orden= (String) model.getValueAt(filas-1,0);
                       campos.setOrden(Integer.parseInt(orden)+1);
                    }

                    
                    
                    String agrupamiento = (String) cmbEspacial.getSelectedItem();
                    if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.where.intersecciona.con"))){
                      campos.setCampo("intersects(" + cmbTabla1.getSelectedItem() + ".\"GEOMETRY\"" + "," + cmbTabla2.getSelectedItem() +".\"GEOMETRY\")");
                      campos.setEtiqueta(cmbTabla1.getSelectedItem() + " " + cmbEspacial.getSelectedItem() +" " + cmbTabla2.getSelectedItem());
                  }else{
                    if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.where.contenido.en"))){
                        campos.setCampo("contains(" + cmbTabla1.getSelectedItem() + ".\"GEOMETRY\"" + "," + cmbTabla2.getSelectedItem() +".\"GEOMETRY\")");
                        campos.setEtiqueta(cmbTabla1.getSelectedItem() + " " + cmbEspacial.getSelectedItem() +" " + cmbTabla2.getSelectedItem());
                    }else{
                        campos.setCampo("Distance(" + cmbTabla1.getSelectedItem() + ".\"GEOMETRY\"" + "," + cmbTabla2.getSelectedItem() +".\"GEOMETRY\")>= " + txtMetros.getText());
                        campos.setEtiqueta(cmbTabla1.getSelectedItem() + " " + cmbEspacial.getSelectedItem() +" " + cmbTabla2.getSelectedItem() + "de " + txtMetros.getText());
                      }
                    }
                

            //Comenzmos con los campos espaciales.
           


            //Pasar los datos del campo a la tabla
           if (cmbEspacial.getSelectedItem().equals(aplicacion.getI18nString("generador.app.reports.where.a.distancia.de"))) {
              if (txtMetros.getText().equals("")){
                JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.meter.distancia"));
              }else{
                  Object[] cadena = new Object[4];
                  cadena[0]= String.valueOf(campos.getOrden());
                  cadena[1]= "C" + campos.getOrden();
                  cadena[2]= campos.getEtiqueta();
                  cadena[3]=campos.getCampo();
                  cmbCondiciones.addItem(cadena[1]);
                  model.insertRow(filas, cadena);
                  //añadimos a la estructura GeopistaSeleccionElementosTabla los datos
                  GeopistaSeleccionElementosTabla camposTablas = new GeopistaSeleccionElementosTabla();
                  camposTablas.setOrden("C"+filas);
                  camposTablas.setTipo("E");
                  camposTablas.setTabla("-999");
                  camposTablas.setCampo("-999");
                  camposTablas.setOperador("-999");
                  camposTablas.setValor1("-999");
                  camposTablas.setValor2("-999");
                   
                  
                  camposTablas.setTabla1((String)cmbTabla1.getSelectedItem());
                  camposTablas.setOperador2((String)cmbEspacial.getSelectedItem());
                  camposTablas.setTabla2((String)cmbTabla2.getSelectedItem());
                  if (txtMetros.isVisible()){
                      camposTablas.setDistancia(txtMetros.getText());
                  }else{
                      camposTablas.setDistancia("-999");    
                  }
                  aLcamposEnTabla.add(camposTablas);
                  
                  
                  
                  txtMetros.setVisible(false);
                  txtMetros.setText(null);
                  cmbTabla1.setSelectedIndex(-1);
                  cmbTabla2.setSelectedIndex(-1);
                  wizardContext.inputChanged();

              }
           }else{
                       Object[] cadena = new Object[4];
            cadena[0]= String.valueOf(campos.getOrden());
            cadena[1]= "C" + campos.getOrden();
            cadena[2]= campos.getEtiqueta();
            cadena[3]=campos.getCampo();
            cmbCondiciones.addItem(cadena[1]);
            model.insertRow(filas, cadena);
//          añadimos a la estructura GeopistaSeleccionElementosTabla los datos
            GeopistaSeleccionElementosTabla camposTablas = new GeopistaSeleccionElementosTabla();
            camposTablas.setOrden("C"+filas);
            camposTablas.setTipo("E");
            camposTablas.setTabla("-999");
            camposTablas.setCampo("-999");
            camposTablas.setOperador("-999");
            camposTablas.setValor1("-999");
            camposTablas.setValor2("-999");
             
            
            camposTablas.setTabla1((String)cmbTabla1.getSelectedItem());
            camposTablas.setOperador2((String)cmbEspacial.getSelectedItem());
            camposTablas.setTabla2((String)cmbTabla2.getSelectedItem());
            if (txtMetros.isVisible()){
                camposTablas.setDistancia(txtMetros.getText());
            }else{
                camposTablas.setDistancia("-999");    
            }
            aLcamposEnTabla.add(camposTablas);
                txtMetros.setVisible(false);
                txtMetros.setText(null);
                cmbTabla1.setSelectedIndex(-1);
                cmbTabla2.setSelectedIndex(-1);
                wizardContext.inputChanged();

           }        
            }else{
              JOptionPane.showMessageDialog(this,aplicacion.getI18nString("generador.app.reports.where.origen.destino"));
            }
            
          }
/**
 * Método que Localiza el campo dentro de una tabla 
 * @param DefaultTableModel 
 * @param String cX, campo a buscar
 * @return String valor del campo buscado
 */

public String localizarCampo(DefaultTableModel model,String cX){
{
  String resultado="";
  for (int i=0;i<model.getRowCount();i++)
    if (model.getValueAt(i,1).equals(cX))
      {
        resultado=(String) model.getValueAt(i,3);
        break;
      }
    return resultado;
  }
    
}


/**
 * Método que comprueba si una sentencia sql es correcta sintacticamente
 * @param String sql 
 * @return String , S--Correcta, N--Erronea
 */

public String compruebaCondiciones(String sql) throws SQLException

{
    String Resultado  = "N"; 
    Statement s = null;
    ResultSet r = null;
    Connection conn=null;
    GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();  
  
  try
  {
     

    conn = conexion.abrirConexionPostgres();
    s = conn.createStatement();
    r = s.executeQuery(sql);

    if (r.next())
    {
        Resultado="S";   
    }
    
    return Resultado;
  } catch (Exception ex)
  
  {
    Resultado="N";   
    ex.printStackTrace();
    return Resultado;
  }//catch  
  finally{
              Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
    aplicacion.closeConnection(conn,null,s,r);
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
  GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();
  Connection  conn=null;
  try {
  conn = geopistaListados.abrirConexion();
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
}  

 



