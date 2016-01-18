package com.geopista.app.catastro;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import com.geopista.app.catastro.*;
import javax.swing.*;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.geopista.util.*;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;
import java.lang.Integer;

public class SubparcelasPanel extends  JPanel implements FeatureExtendedPanel {
//public class SubparcelasPanel extends JPanel {
  
  private JButton btnBorrar_Subparcela = new JButton();
  private JButton btnNueva_Subparcela = new JButton();
  private JButton btnModificar_Subparcela = new JButton();
  private JLabel lblNumero_Orden = new JLabel();
  private JTextField txtNumero_Orden = new JTextField();
  private JLabel lblLongitud_Fachada = new JLabel();
  private JTextField txtLongitud_Fachada = new JTextField();
  private JLabel lblTipo_Fachada = new JLabel();
  private JComboBox cmbTipo_Fachada = new JComboBox();
  private JLabel lblSuperficie_Elemento_Suelo = new JLabel();
  private JTextField txtSuperficie_Elemento_Suelo = new JTextField();  
  private JLabel lblFondo_Elemento_Suelo = new JLabel();
  private JTextField txtFondo_Elemento_Suelo = new JTextField();
  private JLabel lblCodigo_Tipo_Valor = new JLabel();
  private JComboBox cmbCodigo_Tipo_Valor = new JComboBox();
  private JLabel lblNumero_Fachadas = new JLabel();
  private JTextField txtNumero_Fachadas = new JTextField();
  private JCheckBox chkCorrector_Longitud_Fachada = new JCheckBox();
  private JCheckBox chkCorrector_Forma_Irregular = new JCheckBox();
  private JCheckBox chkCorrector_Desmonte_Excesivo = new JCheckBox();
  private JCheckBox chkCorrector_Profundidad_Firme = new JCheckBox();
  private JCheckBox chkCorrector_Fondo_Excesivo = new JCheckBox();
  private JCheckBox chkCorrector_Superficie_Distinta = new JCheckBox();
  private JLabel lblCorrector_Apreciacion = new JLabel();
  private JTextField txtCorrector_Apreciacion = new JTextField();
  private JCheckBox chkCorrector_Depreciacion_Funcional = new JCheckBox();
  private JLabel lblCorrector_Cargas_Singulares = new JLabel();
  private JTextField txtCorrector_Cargas_Singulares = new JTextField();
  private JCheckBox chkCorrector_Situaciones_Especiales = new JCheckBox();
  private JCheckBox chkCorrector_Uso_No_Lucrativo = new JCheckBox();
  private JLabel lblAgua = new JLabel();
  private JComboBox cmbAgua = new JComboBox();
  private JLabel lblElectricidad = new JLabel();
  private JComboBox cmbElectricidad = new JComboBox();  
  private JLabel lblAlumbrado = new JLabel();
  private JComboBox cmbAlumbrado = new JComboBox();
  private JLabel lblDesmonte = new JLabel();
  private JComboBox cmbDesmonte = new JComboBox();
  private JLabel lblPavimentacion = new JLabel();
  private JComboBox cmbPavimentacion = new JComboBox();  
  private JLabel lblAlcantarillado = new JLabel();
  private JComboBox cmbAlcantarillado = new JComboBox();
  private JLabel lblAnnoExpediente = new JLabel();
  private JTextField txtAnnoExpediente = new JTextField();
  private JLabel lblReferenciaExpediente = new JLabel();
  private JTextField txtReferenciaExpediente = new JTextField();
  private JLabel lblCoefCorr1 = new JLabel();
  private JLabel lblCoefCorr2 = new JLabel();
  private JLabel lblCoefCorr3 = new JLabel();
  private int ID;
  private int ID_Parcela;
  private ArrayList Subparcela= new ArrayList();
  public boolean change= false;
  private boolean alta= false;

  DefaultListModel lstSubparcelas = new DefaultListModel();
  JList padronModel = new JList(lstSubparcelas);
  
  public SubparcelasPanel () {
    ID_Parcela=ID;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
/* public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    
    SubparcelasPanel SubparcelasPanel = new SubparcelasPanel();

    frame1.getContentPane().add(SubparcelasPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);

    
  }*/

  private void jbInit() throws Exception {
    this.setName("Subparcelas");
    System.out.println(ID_Parcela);
    this.setLayout(null);
    this.setSize(new Dimension(500, 470));
    this.setBounds(new Rectangle(5, 10, 625, 470));  
        Subparcela.add(txtNumero_Orden);
        Subparcela.add(txtLongitud_Fachada);
        Subparcela.add(cmbTipo_Fachada);
        Subparcela.add(txtSuperficie_Elemento_Suelo);
        Subparcela.add(txtFondo_Elemento_Suelo);
        Subparcela.add(cmbCodigo_Tipo_Valor);
        Subparcela.add(txtNumero_Fachadas);
        Subparcela.add(chkCorrector_Longitud_Fachada);
        Subparcela.add(chkCorrector_Forma_Irregular);
        Subparcela.add(chkCorrector_Desmonte_Excesivo);
        Subparcela.add(chkCorrector_Profundidad_Firme);
        Subparcela.add(chkCorrector_Fondo_Excesivo);
        Subparcela.add(chkCorrector_Superficie_Distinta);
        Subparcela.add(chkCorrector_Depreciacion_Funcional);
        Subparcela.add(chkCorrector_Situaciones_Especiales);
        Subparcela.add(chkCorrector_Uso_No_Lucrativo);
        Subparcela.add(txtCorrector_Apreciacion);
        Subparcela.add(txtCorrector_Cargas_Singulares);
        Subparcela.add(cmbAgua);
        Subparcela.add(cmbElectricidad);
        Subparcela.add(cmbAlumbrado);
        Subparcela.add(cmbDesmonte);
        Subparcela.add(cmbPavimentacion);
        Subparcela.add(cmbAlcantarillado);
        Subparcela.add(txtAnnoExpediente);
        Subparcela.add(txtReferenciaExpediente);
    lblNumero_Orden.setText("Número Orden Subparcela:");
    lblNumero_Orden.setBounds(new java.awt.Rectangle(211,12,160,15));
    txtNumero_Orden.setBounds(new java.awt.Rectangle(373,10,50,20));
    lblLongitud_Fachada.setText("Longitud Fachada (cm):");
    lblLongitud_Fachada.setBounds(new java.awt.Rectangle(8,85,118,15));    
    txtLongitud_Fachada.setBounds(new Rectangle(150, 80, 65, 20));
    lblTipo_Fachada.setText("Tipo Fachada:");
    lblTipo_Fachada.setBounds(new java.awt.Rectangle(239,86,85,15));    
    cmbTipo_Fachada.setBounds(new java.awt.Rectangle(327,84,65,20));   
    cmbTipo_Fachada.addItem("FA");
    cmbTipo_Fachada.addItem("FD");
    cmbTipo_Fachada.addItem("DR");
    cmbTipo_Fachada.addItem("IZ");
    cmbTipo_Fachada.addItem("SI");    
    lblSuperficie_Elemento_Suelo.setText("Superficie Subparcela (m2):");
    lblSuperficie_Elemento_Suelo.setBounds(new java.awt.Rectangle(8,106,138,20));
    txtSuperficie_Elemento_Suelo.setBounds(new java.awt.Rectangle(151,108,65,20));
    lblFondo_Elemento_Suelo.setText("Fondo del Elemento Suelo (m):");
    lblFondo_Elemento_Suelo.setBounds(new java.awt.Rectangle(241,111,150,15));
    txtFondo_Elemento_Suelo.setBounds(new java.awt.Rectangle(396,109,40,20));
    lblCodigo_Tipo_Valor.setText("Código Tipo Valor:");
    lblCodigo_Tipo_Valor.setBounds(new java.awt.Rectangle(287,61,111,17));
    cmbCodigo_Tipo_Valor.setBounds(new java.awt.Rectangle(402,60,50,20));
    cmbCodigo_Tipo_Valor.addItem("0");
    cmbCodigo_Tipo_Valor.addItem("1");
    cmbCodigo_Tipo_Valor.addItem("2");
    cmbCodigo_Tipo_Valor.addItem("3");
    cmbCodigo_Tipo_Valor.addItem("4");
    cmbCodigo_Tipo_Valor.addItem("5");
    cmbCodigo_Tipo_Valor.addItem("6");
    cmbCodigo_Tipo_Valor.addItem("7");
    cmbCodigo_Tipo_Valor.addItem("8");
    cmbCodigo_Tipo_Valor.addItem("9");    
    lblNumero_Fachadas.setText("Número fachadas:");
    lblNumero_Fachadas.setBounds(new java.awt.Rectangle(35,159,109,16));    
    txtNumero_Fachadas.setBounds(new Rectangle(150, 155, 35, 20));
    chkCorrector_Longitud_Fachada.setText("Longitud Fachada");
    chkCorrector_Longitud_Fachada.setBounds(new java.awt.Rectangle(201,157,115,20));
    chkCorrector_Forma_Irregular.setText("Forma Irregular");
    chkCorrector_Forma_Irregular.setBounds(new java.awt.Rectangle(323,157,105,20));
    chkCorrector_Desmonte_Excesivo.setText("Desmonte");
    chkCorrector_Desmonte_Excesivo.setBounds(new java.awt.Rectangle(429,155,85,20));
    chkCorrector_Profundidad_Firme.setText("Profundidad");
    chkCorrector_Profundidad_Firme.setBounds(new java.awt.Rectangle(324,179,84,20));
    chkCorrector_Fondo_Excesivo.setText("Fondo");
    chkCorrector_Fondo_Excesivo.setBounds(new java.awt.Rectangle(34,180,66,20));  
    chkCorrector_Superficie_Distinta.setText("Superficie");
    chkCorrector_Superficie_Distinta.setBounds(new java.awt.Rectangle(202,181,77,20));
    lblCorrector_Apreciacion.setText("Apreciación:");
    lblCorrector_Apreciacion.setBounds(new java.awt.Rectangle(36,229,65,25));    
    txtCorrector_Apreciacion.setBounds(new java.awt.Rectangle(101,234,40,20));    
    chkCorrector_Depreciacion_Funcional.setText("Depreciación");
    chkCorrector_Depreciacion_Funcional.setBounds(new java.awt.Rectangle(134,260,94,20));
  	lblCorrector_Cargas_Singulares.setSize(95, 19);
    lblCorrector_Cargas_Singulares.setLocation(152, 232);
  	lblCorrector_Cargas_Singulares.setText("Cargas singulares:");    
    txtCorrector_Cargas_Singulares.setBounds(new java.awt.Rectangle(249,232,126,20));
    chkCorrector_Situaciones_Especiales.setText("S. Especiales");
    chkCorrector_Situaciones_Especiales.setBounds(new java.awt.Rectangle(39,260,105,20));
    chkCorrector_Uso_No_Lucrativo.setText("No Lucrativo");
    chkCorrector_Uso_No_Lucrativo.setBounds(new java.awt.Rectangle(226,260,87,20));
    lblAgua.setText("Agua:");
    lblAgua.setBounds(new java.awt.Rectangle(39,318,35,15));
    cmbAgua.setBounds(new java.awt.Rectangle(116,319,40,15));
    cmbAgua.addItem("N");
    cmbAgua.addItem("0");
    cmbAgua.addItem("1");    
    lblElectricidad.setText("Electricidad:");
    lblElectricidad.setBounds(new java.awt.Rectangle(179,319,77,16));
    cmbElectricidad.setBounds(new java.awt.Rectangle(287,320,40,15));
    cmbElectricidad.addItem("N");
    cmbElectricidad.addItem("0");
    cmbElectricidad.addItem("1");
    lblAlumbrado.setText("Alumbrado:");
    lblAlumbrado.setBounds(new java.awt.Rectangle(342,317,75,16));
    cmbAlumbrado.setBounds(new java.awt.Rectangle(423,320,40,15));
    cmbAlumbrado.addItem("N");
    cmbAlumbrado.addItem("0");
    cmbAlumbrado.addItem("1");
    lblDesmonte.setText("Desmonte:");
    lblDesmonte.setBounds(new java.awt.Rectangle(38,347,70,17));
    cmbDesmonte.setBounds(new java.awt.Rectangle(116,349,40,15));
    cmbDesmonte.addItem("N");
    cmbDesmonte.addItem("0");
    cmbDesmonte.addItem("1");
    lblPavimentacion.setText("Pavimentación:");
    lblPavimentacion.setBounds(new java.awt.Rectangle(177,348,98,15));
    cmbPavimentacion.setBounds(new java.awt.Rectangle(287,345,40,15));
    cmbPavimentacion.addItem("N");
    cmbPavimentacion.addItem("0");
    cmbPavimentacion.addItem("1");
    lblAlcantarillado.setText("Alcantarillado:");
    lblAlcantarillado.setBounds(new java.awt.Rectangle(342,347,95,18));
    cmbAlcantarillado.setBounds(new java.awt.Rectangle(442,348,40,15));
    cmbAlcantarillado.addItem("N");
    cmbAlcantarillado.addItem("0");
    cmbAlcantarillado.addItem("1");
    lblAnnoExpediente.setText("Año Expediente:");
    lblAnnoExpediente.setBounds(new Rectangle(35, 385, 82, 20));
    txtAnnoExpediente.setBounds(new Rectangle(120, 385, 40, 20));
    lblReferenciaExpediente.setText("Referencia Expediente:");
    lblReferenciaExpediente.setBounds(new Rectangle(190, 387, 134, 16));    
    txtReferenciaExpediente.setBounds(new Rectangle(330, 385, 110, 20));


    btnBorrar_Subparcela.setText("Baja");
    btnBorrar_Subparcela.setBounds(new Rectangle(115, 35, 78, 20));
    btnNueva_Subparcela.setText("Alta");
    btnNueva_Subparcela.setBounds(new Rectangle(115, 10, 80, 20));
    btnModificar_Subparcela.setText("Aplicar");
    btnModificar_Subparcela.setBounds(new Rectangle(25, 60, 78, 20));
    btnModificar_Subparcela.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnModificar_Subparcela_actionPerformed(e);
        }
      });
    
	lblCoefCorr1.setBounds(5, 290, 267, 21);
	lblCoefCorr1.setText("Coeficientes correctores del grado de urbanización:"); 
	lblCoefCorr2.setBounds(12, 204, 405, 21);
	lblCoefCorr2.setText("Coeficientes correctores conjuntos del valor del suelo y de las construcciones:");
	lblCoefCorr3.setBounds(9, 132, 267, 21);
	lblCoefCorr3.setText("Coeficientes correctores del valor del suelo:");	
//    lstSubparcelas.setBounds(new Rectangle(210, 25, 100, 55));
    padronModel.setBounds(new Rectangle(5, 5, 100, 55));

    
    this.add(padronModel, null);
//    this.add(lstSubparcelas, null);
    this.add(btnBorrar_Subparcela, null);
    this.add(btnNueva_Subparcela, null);
    this.add(btnModificar_Subparcela, null);
    this.add(lblNumero_Orden, null);
    this.add(txtNumero_Orden, null);
    this.add(lblLongitud_Fachada, null);
    this.add(txtLongitud_Fachada, null);
    this.add(lblTipo_Fachada, null);
    this.add(cmbTipo_Fachada,null);
    this.add(lblSuperficie_Elemento_Suelo, null);
    this.add(txtSuperficie_Elemento_Suelo, null);
    this.add(lblFondo_Elemento_Suelo, null);
    this.add(txtFondo_Elemento_Suelo,null);
    this.add(lblCodigo_Tipo_Valor, null);
    this.add(cmbCodigo_Tipo_Valor, null);
    this.add(lblNumero_Fachadas,null);
    this.add(txtNumero_Fachadas, null);
    this.add(chkCorrector_Longitud_Fachada, null);
    this.add(chkCorrector_Forma_Irregular, null);
    this.add(chkCorrector_Desmonte_Excesivo, null);
    this.add(chkCorrector_Profundidad_Firme, null);
    this.add(chkCorrector_Fondo_Excesivo, null);
    this.add(chkCorrector_Superficie_Distinta, null);
    this.add(lblCorrector_Apreciacion, null);
    this.add(txtCorrector_Apreciacion, null);
    this.add(chkCorrector_Depreciacion_Funcional, null);
    this.add(lblCorrector_Cargas_Singulares,null);
    this.add(txtCorrector_Cargas_Singulares, null);
    this.add(chkCorrector_Situaciones_Especiales, null);
    this.add(chkCorrector_Uso_No_Lucrativo, null);
    this.add(lblAgua, null);
    this.add(cmbAgua, null);
    this.add(lblElectricidad, null);
    this.add(cmbElectricidad, null);
    this.add(lblAlumbrado, null);
    this.add(cmbAlumbrado, null);
    this.add(lblDesmonte, null);
    this.add(cmbDesmonte, null);
    this.add(lblPavimentacion, null);
    this.add(cmbPavimentacion, null);
    this.add(lblAlcantarillado, null);
    this.add(cmbAlcantarillado, null);
    this.add(lblAnnoExpediente, null);
    this.add(txtAnnoExpediente, null);
    this.add(lblReferenciaExpediente, null);
    this.add(txtReferenciaExpediente, null);
    this.add(lblCoefCorr1, null);
  	this.add(lblCoefCorr2, null);
  	this.add(lblCoefCorr3, null);

    //Rellenamos los datos
    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        int subparcela =Integer.parseInt(padronModel.getSelectedValue().toString());
        CatastroActualizarPostgre Subparcel = new CatastroActualizarPostgre();
        ArrayList Datos=Subparcel.DatosSubparcela(subparcela, ID_Parcela);
        Iterator alIt = Datos.iterator();
        Iterator itControles = Subparcela.iterator();
         ID=Integer.parseInt(alIt.next().toString());
        while (alIt.hasNext()) 
        {
            try
            {
            	Object objVal=alIt.next();
            	String value;
            	if (objVal==null) value=""; else value=objVal.toString();
               JComponent comp=(JComponent)itControles.next();
               if (comp instanceof JTextField)((JTextField)comp).setText(value);
              if (comp instanceof JCheckBox){
                  String check = value;
                  if (check == "TRUE"){
                    ((JCheckBox)comp).setSelected(true);}
                  else{
                    ((JCheckBox)comp).setSelected(false);}}
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem(value);
              }

            catch(Exception A)
            {
                A.printStackTrace();
            }
}}
    };
    btnBorrar_Subparcela.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnBorrar_Subparcela_actionPerformed(e);
        }
      });
    txtReferenciaExpediente.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtReferenciaExpediente_actionPerformed(e);
        }
      });
    txtAnnoExpediente.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtAnnoExpediente_actionPerformed(e);
        }
      });
    cmbAlcantarillado.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbAlcantarillado_actionPerformed(e);
        }
      });
    cmbPavimentacion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbPavimentacion_actionPerformed(e);
        }
      });
    cmbDesmonte.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbDesmonte_actionPerformed(e);
        }
      });
    cmbAlumbrado.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbAlumbrado_actionPerformed(e);
        }
      });
    cmbElectricidad.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbElectricidad_actionPerformed(e);
        }
      });
    cmbAgua.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbAgua_actionPerformed(e);
        }
      });
    chkCorrector_Uso_No_Lucrativo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Uso_No_Lucrativo_actionPerformed(e);
        }
      });
    chkCorrector_Depreciacion_Funcional.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Depreciacion_Funcional_actionPerformed(e);
        }
      });
    chkCorrector_Situaciones_Especiales.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Situaciones_Especiales_actionPerformed(e);
        }
      });
    txtCorrector_Cargas_Singulares.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtCorrector_Cargas_Singulares_actionPerformed(e);
        }
      });
    txtCorrector_Apreciacion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtCorrector_Apreciacion_actionPerformed(e);
        }
      });
    chkCorrector_Profundidad_Firme.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Profundidad_Firme_actionPerformed(e);
        }
      });
    chkCorrector_Superficie_Distinta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Superficie_Distinta_actionPerformed(e);
        }
      });
    chkCorrector_Fondo_Excesivo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Fondo_Excesivo_actionPerformed(e);
        }
      });
    chkCorrector_Desmonte_Excesivo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Desmonte_Excesivo_actionPerformed(e);
        }
      });
    chkCorrector_Forma_Irregular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Forma_Irregular_actionPerformed(e);
        }
      });
    chkCorrector_Longitud_Fachada.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkCorrector_Longitud_Fachada_actionPerformed(e);
        }
      });
    txtNumero_Fachadas.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtNumero_Fachadas_actionPerformed(e);
        }
      });
    txtFondo_Elemento_Suelo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtFondo_Elemento_Suelo_actionPerformed(e);
        }
      });
    txtSuperficie_Elemento_Suelo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtSuperficie_Elemento_Suelo_actionPerformed(e);
        }
      });
    cmbCodigo_Tipo_Valor.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbCodigo_Tipo_Valor_actionPerformed(e);
        }
      });
    cmbTipo_Fachada.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbTipo_Fachada_actionPerformed(e);
        }
      });
    txtLongitud_Fachada.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtLongitud_Fachada_actionPerformed(e);
        }
      });
    txtNumero_Orden.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtNumero_Orden_actionPerformed(e);
        }
      });

    
    btnNueva_Subparcela.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnNueva_Subparcela_actionPerformed(e);
        }
      });
    
    padronModel.addMouseListener(mouseListener);




  }

 private void btnModificar_Subparcela_actionPerformed(ActionEvent e)
  {
    //Con esta opción modificamos los datos de la subparcela
    //En la variable ID tenemos el identificador de la subparcela
    //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos
    ArrayList Subparcelas= new ArrayList();
    ArrayList SubparcelaTipo= new ArrayList();
    ArrayList Checks = new ArrayList();
    Checks.add(chkCorrector_Longitud_Fachada);
    Checks.add(chkCorrector_Forma_Irregular);
    Checks.add(chkCorrector_Desmonte_Excesivo);
    Checks.add(chkCorrector_Profundidad_Firme);
    Checks.add(chkCorrector_Fondo_Excesivo);
    Checks.add(chkCorrector_Superficie_Distinta);
    Checks.add(chkCorrector_Depreciacion_Funcional);
    Checks.add(chkCorrector_Situaciones_Especiales);
    Checks.add(chkCorrector_Uso_No_Lucrativo);

    Subparcelas.add(String.valueOf(ID));
    SubparcelaTipo.add("0");
    Subparcelas.add(String.valueOf(ID_Parcela));
    SubparcelaTipo.add("0");
    Subparcelas.add(txtNumero_Orden.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(txtLongitud_Fachada.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(cmbTipo_Fachada.getSelectedItem());
    SubparcelaTipo.add("1");    
    Subparcelas.add(txtSuperficie_Elemento_Suelo.getText());
    SubparcelaTipo.add("0");   
    Subparcelas.add(txtFondo_Elemento_Suelo.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(cmbCodigo_Tipo_Valor.getSelectedItem());
    SubparcelaTipo.add("1");   
    Subparcelas.add(txtNumero_Fachadas.getText());
    SubparcelaTipo.add("1");  
    Iterator CheckIt = Checks.iterator();    
    while (CheckIt.hasNext()) 
    {
        if(((JCheckBox)CheckIt.next()).isSelected()==true){
        Subparcelas.add("TRUE");    
        }else{
        Subparcelas.add("FALSE");}       
        SubparcelaTipo.add("1");
     }
    Subparcelas.add(txtCorrector_Apreciacion.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(txtCorrector_Cargas_Singulares.getText());
    SubparcelaTipo.add("0"); 
    Subparcelas.add(cmbAgua.getSelectedItem());
    SubparcelaTipo.add("1");   
    Subparcelas.add(cmbElectricidad.getSelectedItem());
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbAlumbrado.getSelectedItem());
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbDesmonte.getSelectedItem());
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbPavimentacion.getSelectedItem());
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbAlcantarillado.getSelectedItem());
    SubparcelaTipo.add("1");
    if (alta == true) {
       Subparcelas.add("A");
       SubparcelaTipo.add("1");   
    }else{
           Subparcelas.add("M");
           SubparcelaTipo.add("1");   
   }
    Subparcelas.add(txtAnnoExpediente.getText());
    SubparcelaTipo.add("0");   
    Subparcelas.add(txtReferenciaExpediente.getText());
    SubparcelaTipo.add("1");  
//Faltan actualizar las fechas de alta y baja 
//    Subparcela.add(txtFecha_Alta.getText());
  //  SubparcelaTipo.add("2");
  //  Subparcela.add(txtFecha_Baja.getText());    
   // SubparcelaTipo.add("2");
    //Actualizamos la información almacenada
    CatastroActualizarPostgre ActualizarSubparcela = new CatastroActualizarPostgre();
    if (alta==false){
       String Result = ActualizarSubparcela.ActualizarSubparcela(ID, Subparcelas, SubparcelaTipo);
      }
    else{

       String Result = ActualizarSubparcela.AltaSubparcela(ID, Subparcelas, SubparcelaTipo);
       alta=false;}
  }

  public void enter()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());
    System.out.println("Subparcelas de la parcela "+ ID_Parcela);
    //Rellenamos el listbox
    lstSubparcelas.clear();
        try{
      CatastroActualizarPostgre Subparcelas = new CatastroActualizarPostgre();    
          ArrayList Datos =  Subparcelas.Subparcelas(ID_Parcela);
          Iterator alIt = Datos.iterator();
          while (alIt.hasNext()) 
          {
              lstSubparcelas.addElement(alIt.next());
          
          }
    }catch(Exception e)
          {
           e.printStackTrace();
          }   


  }
  public void exit()
  {
  }

  private void btnNueva_Subparcela_actionPerformed(ActionEvent e)
  {
        alta=true; 
        //LIMAPIAMOS TODOS LOS CONTROLES
        Iterator alIt = Subparcela.iterator();
        while (alIt.hasNext()) 
        {
            try
            {
               JComponent comp=(JComponent)alIt.next();
               if (comp instanceof JTextField)((JTextField)comp).setText("");
               if (comp instanceof JCheckBox) ((JCheckBox)comp).setSelected(false);
               if (comp instanceof JComboBox)((JComboBox)comp).setSelectedIndex(0);
            }

            catch(Exception A)
            {
                A.printStackTrace();
            }
        }
  }

  private void txtNumero_Orden_actionPerformed(ActionEvent e)
  {  change=true;
  }

  private void txtLongitud_Fachada_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbTipo_Fachada_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbCodigo_Tipo_Valor_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtSuperficie_Elemento_Suelo_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtFondo_Elemento_Suelo_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtNumero_Fachadas_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Longitud_Fachada_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Forma_Irregular_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Desmonte_Excesivo_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Fondo_Excesivo_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Superficie_Distinta_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Profundidad_Firme_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtCorrector_Apreciacion_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtCorrector_Cargas_Singulares_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Situaciones_Especiales_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Depreciacion_Funcional_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkCorrector_Uso_No_Lucrativo_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbAgua_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbElectricidad_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbAlumbrado_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbDesmonte_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbPavimentacion_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbAlcantarillado_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtFecha_Alta_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtAnnoExpediente_actionPerformed(ActionEvent e)
  { change=true;
  }

  

  private void txtFecha_Baja_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtReferenciaExpediente_actionPerformed(ActionEvent e)
  { change=true;
  }
 public void setID (int ID)
  {
    ID_Parcela=ID;
  }

  private void btnBorrar_Subparcela_actionPerformed(ActionEvent e)
  {
     CatastroActualizarPostgre ActualizarSubparcela = new CatastroActualizarPostgre();
     String Result = ActualizarSubparcela.BajaSubparcela (ID);
  }
 
}