package com.geopista.app.catastro;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.util.*;
import com.geopista.util.*;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;


//public class Unidades_ConstructivasPanel extends JPanel {
public class Unidades_ConstructivasPanel extends JPanel implements  FeatureExtendedPanel {
  
  
  private JPanel jPanel15 = new JPanel();
  private JPanel jPanel16 = new JPanel();
  private JPanel jPanel5 = new JPanel();
  private JPanel jPanel14 = new JPanel();
  private JPanel jPanel13 = new JPanel();
  
  private JLabel ordenuclbl = new JLabel();
  private JTextField txtordenuc = new JTextField();
  
  private JLabel alumbradosubparlb1 = new JLabel();
  private JComboBox cmbalumbradouc = new JComboBox();
  private JLabel desmontesubparlb1 = new JLabel();
  private JComboBox cmbdesmonteuc = new JComboBox();
  private JLabel alcantarilladosubparlb1 = new JLabel();
  private JComboBox cmbalcantarilladouc = new JComboBox();
  private JLabel pavimentacionsubparlbl1 = new JLabel();
  private JComboBox cmbpavimentacionuc = new JComboBox();
  private JTextField txtcargasuc = new JTextField();
  private JLabel apreciacionlbl1 = new JLabel();
  private JCheckBox chkdepreciacionuc = new JCheckBox();
  private JCheckBox chkespecialuc = new JCheckBox();
  private JCheckBox chknolucrativouc = new JCheckBox();
  private JCheckBox chklongfachadauc = new JCheckBox();
  private JLabel coef1lbl5 = new JLabel();
  private JComboBox cmbelectricidaduc = new JComboBox();
  private JLabel Electricidadsubparlb1 = new JLabel();
  private JComboBox cmbaguauc = new JComboBox();
  private JLabel aguasubparlb6 = new JLabel();
  
  private JTextField txtdirnoestrucuc = new JTextField();
  private JLabel kmparlbl1 = new JLabel();
  private JLabel bloqueparlbl1 = new JLabel();
  private JTextField txtbloqueuc = new JTextField();
  private JTextField txtkilometrouc = new JTextField();
  private JLabel dirnoestrucparlbl1 = new JLabel();
  private JLabel letra2parlbl1 = new JLabel();
  private JLabel numero2parlbl2 = new JLabel();
  private JTextField txtnumero2uc = new JTextField();
  private JTextField txtletra2uc = new JTextField();
  private JLabel letra1parlbl1 = new JLabel();
  private JTextField txtletra1uc = new JTextField();
  private JLabel numero1parlbl1 = new JLabel();
  private JTextField txtnumero1uc = new JTextField();
  private JLabel codigopostaluclbl = new JLabel();
  private JTextField txtcodigopostaluc = new JTextField();
  private JLabel nombreviauclbl = new JLabel();
  private JTextField txtnombreviauc = new JTextField();
  private JLabel tipoviauclbl = new JLabel();
  private JTextField txttipoviauc = new JTextField();
  private JLabel viapublicauclbl = new JLabel();
  private JTextField txtcodviauc = new JTextField();
  private JTextField txtentmenoruc = new JTextField();
  private JLabel entmenoruclbl = new JLabel();
  
  private JLabel annoconstuclbl = new JLabel();
  private JTextField txtannoconsuc = new JTextField();
  private JTextField txtlongfachadauc = new JTextField();
  private JLabel longfachadasubparlbl1 = new JLabel();
  private JLabel exactannouclbl = new JLabel();
  private JComboBox cmbexactitudannouc = new JComboBox();
  
  private JLabel supcubiertaparlbl5 = new JLabel();
  private JTextField txtrefexpuc = new JTextField();
  private JLabel annoexpparlbl3 = new JLabel();
  private JTextField txtannoexpuc = new JTextField();
  private JButton btndeluc = new JButton();
  private JButton btnedituc = new JButton();
  private JButton btnadduc = new JButton();
  private JComboBox cmbestadoconservacion = new JComboBox();
  private int ID_Unidad;
  private boolean change= false;
  private JCheckBox chkusoagrario = new JCheckBox();
  private JTextField txtfachadas = new JTextField();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  DefaultListModel lstUCs = new DefaultListModel();
  JList padronModel = new JList(lstUCs);
  private ArrayList Unidades = new ArrayList();
  private boolean alta= false;
  private int ID_Parcela;
  
  public Unidades_ConstructivasPanel()
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

 public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    
    Unidades_ConstructivasPanel geopistaEditarDatos = new Unidades_ConstructivasPanel();

    frame1.getContentPane().add(geopistaEditarDatos);
    frame1.setSize(775, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }
  private void jbInit() throws Exception
  {
  this.setName("Unidades Constructivas");
	this.setLayout(null);
	this.setSize(new Dimension(625, 579));
	this.setBounds(new Rectangle(5, 10, 625, 470)); 
  
 
    Unidades.add(txtordenuc);
    Unidades.add(txtentmenoruc);
    Unidades.add(txtnumero1uc);
    Unidades.add(txtletra1uc);
    Unidades.add(txtnumero2uc);
    Unidades.add(txtletra2uc);
    Unidades.add(txtkilometrouc);
    Unidades.add(txtbloqueuc);
    Unidades.add(txtdirnoestrucuc);
    Unidades.add(txtcodigopostaluc);
    Unidades.add(txtannoconsuc);
    Unidades.add(txtlongfachadauc);
    Unidades.add(txtfachadas);
    Unidades.add(cmbestadoconservacion);
    Unidades.add(txtcargasuc);
    Unidades.add(chkusoagrario);
    Unidades.add(chklongfachadauc);
    Unidades.add(chkdepreciacionuc);
    Unidades.add(chkespecialuc);
    Unidades.add(chknolucrativouc);
    Unidades.add(cmbaguauc);
    Unidades.add(cmbelectricidaduc);
    Unidades.add(cmbalumbradouc);
    Unidades.add(cmbdesmonteuc);
    Unidades.add(cmbpavimentacionuc);
    Unidades.add(cmbalcantarilladouc);
    Unidades.add(cmbexactitudannouc);
    Unidades.add(txtannoexpuc);
    Unidades.add(txtrefexpuc);
    Unidades.add(txtcodviauc);
    Unidades.add(txttipoviauc);
    Unidades.add(txtnombreviauc);
    
 
    jPanel15.setBounds(new Rectangle(5, 10, 625, 90));
    jPanel15.setLayout(null);
    jPanel15.setBorder(BorderFactory.createTitledBorder("Identificación"));
    ordenuclbl.setText("Código Unidad Constructiva");
    txtordenuc.setText("4444");
    txtordenuc.setBounds(new Rectangle(415, 25, 50, 20));
    jPanel16.setBounds(new Rectangle(5, 305, 625, 205));
    jPanel16.setLayout(null);
    jPanel16.setBorder(BorderFactory.createTitledBorder("Datos de Valoración"));
    jPanel16.setSize(new Dimension(625, 205));
    alumbradosubparlb1.setText("Alumbrado:");
    cmbalumbradouc.setBounds(new Rectangle(410, 140, 40, 15));
    desmontesubparlb1.setText("Desmonte:");
    cmbdesmonteuc.setBounds(new Rectangle(545, 140, 40, 15));
    alcantarilladosubparlb1.setText("Alcantarillado:");
    cmbalcantarilladouc.setBounds(new Rectangle(260, 175, 40, 15));
    pavimentacionsubparlbl1.setText("Pavimentación:");
    cmbpavimentacionuc.setBounds(new Rectangle(95, 175, 40, 15));
    txtcargasuc.setText("333");
    txtcargasuc.setBounds(new Rectangle(95, 110, 40, 20));
    apreciacionlbl1.setText("Cargas Singulares:");
    apreciacionlbl1.setBounds(new Rectangle(5, 110, 95, 15));
    chkdepreciacionuc.setText("Depreciación Funcional");
    chkdepreciacionuc.setBounds(new Rectangle(150, 110, 145, 20));
    chkespecialuc.setText("Situaciones Especiales");
    chkespecialuc.setBounds(new Rectangle(300, 110, 175, 20));
    chknolucrativouc.setText("No Lucrativo");
    chknolucrativouc.setBounds(new Rectangle(480, 110, 110, 20));
    chklongfachadauc.setText("Longitud Fachada");
    chklongfachadauc.setBounds(new Rectangle(145, 80, 130, 20));
    coef1lbl5.setText("Coeficientes Correctores del Valor del Suelo y del Valor de las Constucciones");
    cmbelectricidaduc.setBounds(new Rectangle(265, 140, 40, 15));
    Electricidadsubparlb1.setText("Electricidad:");
    cmbaguauc.setBounds(new Rectangle(95, 140, 40, 15));
    aguasubparlb6.setText("Agua:");
    jPanel5.setBounds(new Rectangle(5, 100, 625, 160));
    jPanel5.setLayout(null);
    jPanel5.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario de la Unidad Constructiva"));
    txtdirnoestrucuc.setText("PPPPPPPPPPPPPPPPPPPPPPP25");
    txtdirnoestrucuc.setBounds(new Rectangle(410, 120, 205, 20));
    kmparlbl1.setText("Kilómetro:");
    bloqueparlbl1.setText("Bloque:");
    txtbloqueuc.setText("4444");
    txtbloqueuc.setBounds(new Rectangle(55, 120, 35, 20));
    txtkilometrouc.setText("1");
    txtkilometrouc.setBounds(new Rectangle(165, 120, 80, 20));
    dirnoestrucparlbl1.setText("Direccion No Estructurada:");
    letra2parlbl1.setText("Segunda Letra:");
    numero2parlbl2.setText("Segundo Número:");
    txtnumero2uc.setText("4444");
    txtnumero2uc.setBounds(new Rectangle(405, 85, 35, 20));
    txtletra2uc.setText("A");
    txtletra2uc.setBounds(new Rectangle(565, 85, 25, 20));
    letra1parlbl1.setText("Primera Letra:");
    txtletra1uc.setText("A");
    txtletra1uc.setBounds(new Rectangle(245, 85, 25, 20));
    numero1parlbl1.setText("Primer Número:");
    txtnumero1uc.setText("4444");
    txtnumero1uc.setBounds(new Rectangle(100, 85, 35, 20));
    codigopostaluclbl.setText("Código Postal:");
    txtcodigopostaluc.setText("09001");
    txtcodigopostaluc.setBounds(new Rectangle(425, 55, 50, 20));
    nombreviauclbl.setText("Nombre Vía:");
    txtnombreviauc.setText("PPPPPPPPPPPPPPPPPPPPPPP25");
    txtnombreviauc.setBounds(new Rectangle(90, 55, 230, 20));
    tipoviauclbl.setText("Tipo de Vía:");
    txttipoviauc.setText("PPPP5");
    txttipoviauc.setBounds(new Rectangle(510, 25, 45, 20));
    viapublicauclbl.setText("Código Vía Pública:");
    txtcodviauc.setText("55555");
    txtcodviauc.setBounds(new Rectangle(345, 25, 50, 20));
    txtentmenoruc.setText("PP");
    txtentmenoruc.setBounds(new Rectangle(150, 25, 35, 20));
    entmenoruclbl.setText("Código Entidad Menor:");
    jPanel14.setBounds(new Rectangle(5, 260, 625, 50));
    jPanel14.setLayout(null);
    jPanel14.setBorder(BorderFactory.createTitledBorder("Datos Físicos"));
    annoconstuclbl.setText("Año de la Construcción:");
    txtannoconsuc.setText("2004");
    txtannoconsuc.setBounds(new Rectangle(150, 20, 40, 20));
    txtannoconsuc.setSize(new Dimension(40, 20));
    txtlongfachadauc.setText("55555");
    txtlongfachadauc.setBounds(new Rectangle(365, 20, 45, 20));
    longfachadasubparlbl1.setText("Longitud Fachada (cm):");
    exactannouclbl.setText("Exactitud Año Construcción:");
    cmbexactitudannouc.setBounds(new Rectangle(515, 175, 40, 15));
    jPanel13.setBounds(new Rectangle(5, 530, 625, 50));
    jPanel13.setLayout(null);
    jPanel13.setBorder(BorderFactory.createTitledBorder("Datos del Movimiento"));
    supcubiertaparlbl5.setText("Referencia Expediente:");
    txtrefexpuc.setText("AAAAAAAAAAA13");
    txtrefexpuc.setBounds(new Rectangle(345, 20, 110, 20));
    txtrefexpuc.setSize(new Dimension(110, 20));
    annoexpparlbl3.setText("Año Expediente:");
    txtannoexpuc.setText("4444");
    txtannoexpuc.setBounds(new Rectangle(110, 20, 40, 20));
    txtannoexpuc.setPreferredSize(new Dimension(53, 20));
    txtannoexpuc.setSize(new Dimension(40, 20));
   
    btndeluc.setText("Baja");
    btndeluc.setBounds(new Rectangle(130, 35, 78, 20));
    btnedituc.setText("Aplicar");
    btnedituc.setBounds(new Rectangle(130, 60, 78, 20));
    btnadduc.setText("Alta");
    btnadduc.setBounds(new Rectangle(130, 10, 78, 20));
    cmbestadoconservacion.setBounds(new Rectangle(415, 80, 45, 20));
    chkusoagrario.setText("Indicador Uso Agrario");
    chkusoagrario.setBounds(new Rectangle(435, 20, 165, 20));
    chkusoagrario.setActionCommand("ckkusoagrario");
    txtfachadas.setText("333");
    txtfachadas.setBounds(new Rectangle(95, 80, 40, 20));
    jLabel1.setText("Nº Fachadas:");
    jLabel1.setBounds(new Rectangle(10, 80, 70, 20));
    jLabel2.setText("Estado Conservación:");
    jLabel2.setBounds(new Rectangle(280, 80, 125, 25));
   
  
    
    cmbexactitudannouc.addItem("E");
    cmbexactitudannouc.addItem("+");
    cmbexactitudannouc.addItem("-");
    cmbexactitudannouc.addItem("C");
   
    cmbestadoconservacion.addItem("B");
    cmbestadoconservacion.addItem("M");
    cmbestadoconservacion.addItem("R");
    jPanel13.add(supcubiertaparlbl5, null);
    jPanel13.add(txtrefexpuc, null);
    jPanel13.add(annoexpparlbl3, null);
    jPanel13.add(txtannoexpuc, null);
    jPanel14.add(chkusoagrario, null);
    jPanel14.add(annoconstuclbl, null);
    jPanel14.add(txtannoconsuc, null);
    jPanel14.add(txtlongfachadauc, null);
    jPanel14.add(longfachadasubparlbl1, null);
    jPanel5.add(txtdirnoestrucuc, null);
    jPanel5.add(kmparlbl1, null);
    jPanel5.add(bloqueparlbl1, null);
    jPanel5.add(txtbloqueuc, null);
    jPanel5.add(txtkilometrouc, null);
    jPanel5.add(dirnoestrucparlbl1, null);
    jPanel5.add(letra2parlbl1, null);
    jPanel5.add(numero2parlbl2, null);
    jPanel5.add(txtnumero2uc, null);
    jPanel5.add(txtletra2uc, null);
    jPanel5.add(letra1parlbl1, null);
    jPanel5.add(txtletra1uc, null);
    jPanel5.add(numero1parlbl1, null);
    jPanel5.add(txtnumero1uc, null);
    jPanel5.add(codigopostaluclbl, null);
    jPanel5.add(txtcodigopostaluc, null);
    jPanel5.add(nombreviauclbl, null);
    jPanel5.add(txtnombreviauc, null);
    jPanel5.add(tipoviauclbl, null);
    jPanel5.add(txttipoviauc, null);
    jPanel5.add(viapublicauclbl, null);
    jPanel5.add(txtcodviauc, null);
    jPanel5.add(txtentmenoruc, null);
    jPanel5.add(entmenoruclbl, null);
    jPanel15.add(padronModel, null);
    jPanel15.add(btnadduc, null);
    jPanel15.add(btnedituc, null);
    jPanel15.add(btndeluc, null);
    jPanel15.add(ordenuclbl, null);
    jPanel15.add(txtordenuc, null);
    jPanel16.add(jLabel2, null);
    jPanel16.add(jLabel1, null);
    jPanel16.add(txtfachadas, null);
    jPanel16.add(cmbestadoconservacion, null);
    jPanel16.add(cmbexactitudannouc, null);
    jPanel16.add(exactannouclbl, null);
    jPanel16.add(alumbradosubparlb1, null);
    jPanel16.add(cmbalumbradouc, null);
    jPanel16.add(desmontesubparlb1, null);
    jPanel16.add(cmbdesmonteuc, null);
    jPanel16.add(alcantarilladosubparlb1, null);
    jPanel16.add(cmbalcantarilladouc, null);
    jPanel16.add(pavimentacionsubparlbl1, null);
    jPanel16.add(cmbpavimentacionuc, null);
    jPanel16.add(txtcargasuc, null);
    jPanel16.add(apreciacionlbl1, null);
    jPanel16.add(chkdepreciacionuc, null);
    jPanel16.add(chkespecialuc, null);
    jPanel16.add(chknolucrativouc, null);
    jPanel16.add(chklongfachadauc, null);
    jPanel16.add(coef1lbl5, null);
    jPanel16.add(cmbelectricidaduc, null);
    jPanel16.add(Electricidadsubparlb1, null);
    jPanel16.add(cmbaguauc, null);
    jPanel16.add(aguasubparlb6, null);
    longfachadasubparlbl1.setSize(123, 15);
    longfachadasubparlbl1.setLocation(238, 20);
    letra2parlbl1.setSize(78, 15);
    letra2parlbl1.setLocation(482, 87);
    coef1lbl5.setSize(595, 15);
    coef1lbl5.setLocation(5, 50);
    letra1parlbl1.setSize(76, 15);
    letra1parlbl1.setLocation(161, 88);
    tipoviauclbl.setSize(65, 15);
    tipoviauclbl.setLocation(441, 28);
    numero1parlbl1.setSize(80, 15);
    numero1parlbl1.setLocation(8, 87);
    exactannouclbl.setSize(142, 15);
    exactannouclbl.setLocation(361, 173);
    entmenoruclbl.setSize(115, 15);
    entmenoruclbl.setLocation(9, 27);
    alumbradosubparlb1.setSize(64, 15);
    alumbradosubparlb1.setLocation(340, 140);
    Electricidadsubparlb1.setSize(64, 15);
    Electricidadsubparlb1.setLocation(188, 138);
    annoexpparlbl3.setSize(95, 15);
    annoexpparlbl3.setLocation(12, 20);
    supcubiertaparlbl5.setSize(121, 15);
    supcubiertaparlbl5.setLocation(214, 22);
    alcantarilladosubparlb1.setSize(75, 15);
    alcantarilladosubparlb1.setLocation(182, 175);
    bloqueparlbl1.setSize(39, 15);
    bloqueparlbl1.setLocation(5, 124);
    desmontesubparlb1.setSize(59, 15);
    desmontesubparlb1.setLocation(477, 139);
    numero2parlbl2.setSize(96, 15);
    numero2parlbl2.setLocation(308, 87);
    pavimentacionsubparlbl1.setSize(78, 15);
    pavimentacionsubparlbl1.setLocation(7, 174);
    dirnoestrucparlbl1.setSize(135, 15);
    dirnoestrucparlbl1.setLocation(272, 122);
    viapublicauclbl.setSize(106, 15);
    viapublicauclbl.setLocation(237, 27);
    ordenuclbl.setSize(148, 15);
    ordenuclbl.setLocation(250, 28);
    codigopostaluclbl.setSize(78, 15);
    codigopostaluclbl.setLocation(344, 58);
    kmparlbl1.setSize(54, 15);
    kmparlbl1.setLocation(104, 123);
    annoconstuclbl.setSize(123, 15);
    annoconstuclbl.setLocation(19, 22);
    aguasubparlb6.setSize(45, 15);
    aguasubparlb6.setLocation(7, 140);
    nombreviauclbl.setSize(69, 15);
    nombreviauclbl.setLocation(9, 56);
    apreciacionlbl1.setSize(75, 15);
    apreciacionlbl1.setLocation(5, 110);
    cmbalumbradouc.addItem("N");
    cmbalumbradouc.addItem("0");
    cmbalumbradouc.addItem("1");
    cmbdesmonteuc.addItem("N");
    cmbdesmonteuc.addItem("0");
    cmbdesmonteuc.addItem("1");
    cmbalcantarilladouc.addItem("N");
    cmbalcantarilladouc.addItem("0");
    cmbalcantarilladouc.addItem("1");
    cmbpavimentacionuc.addItem("N");
    cmbpavimentacionuc.addItem("0");
    cmbpavimentacionuc.addItem("1");
    cmbelectricidaduc.addItem("N");
    cmbelectricidaduc.addItem("0");
    cmbelectricidaduc.addItem("1");
    cmbaguauc.addItem("N");
    cmbaguauc.addItem("0");
    cmbaguauc.addItem("1");

    this.add(jPanel15, null);
    this.add(jPanel16, null);
    this.add(jPanel5, null);
    this.add(jPanel14, null);
    this.add(jPanel13, null);

    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        String codigo =padronModel.getSelectedValue().toString();
      Iterator alIt1 = Unidades.iterator();
        
        while (alIt1.hasNext()) 
        {
            try
            {
               JComponent comp=(JComponent)alIt1.next();
               if (comp instanceof JTextField)((JTextField)comp).setText("");
              if (comp instanceof JCheckBox) ((JCheckBox)comp).setSelected(false);
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedIndex(0);
              }

            catch(Exception A)
            {
                A.printStackTrace();
            }
        }
        CatastroActualizarPostgre UC = new CatastroActualizarPostgre();
        ArrayList Datos= UC.DatosUC(codigo, ID_Parcela);
        Iterator alIt = Datos.iterator();
        Iterator itControles = Unidades.iterator();
        ID_Unidad=Integer.parseInt(alIt.next().toString());
        while (alIt.hasNext()) 
        {
            try
            {
               JComponent comp=(JComponent)itControles.next();
               Object objVal=alIt.next(); // Lo he sacado de las funciones de asignación de valor porque generan nullpointerexceptions
               String value;
               if (objVal==null) value=""; else value=objVal.toString();
               if (comp instanceof JTextField)((JTextField)comp).setText(value);
              if (comp instanceof JCheckBox){
                  String check = value;
                  if (check == "TRUE"){
                    ((JCheckBox)comp).setSelected(true);}
                  else{
                    ((JCheckBox)comp).setSelected(false);}}
             if (comp instanceof JComboBox)
             	((JComboBox)comp).setSelectedItem(value);
              }

            catch(Exception A)
            {
                A.printStackTrace();
            }
    }}
  };
    btndeluc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btndeluc_actionPerformed(e);
        }
      });
    cmbdesmonteuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbdesmonteuc_actionPerformed(e);
        }
      });
    cmbalumbradouc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbalumbradouc_actionPerformed(e);
        }
      });
    cmbelectricidaduc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbelectricidaduc_actionPerformed(e);
        }
      });
    cmbaguauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbaguauc_actionPerformed(e);
        }
      });
    chknolucrativouc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chknolucrativouc_actionPerformed(e);
        }
      });
    chkespecialuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkespecialuc_actionPerformed(e);
        }
      });
    chkdepreciacionuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkdepreciacionuc_actionPerformed(e);
        }
      });
    txtcargasuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcargasuc_actionPerformed(e);
        }
      });
    cmbestadoconservacion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbestadoconservacion_actionPerformed(e);
        }
      });
    chklongfachadauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chklongfachadauc_actionPerformed(e);
        }
      });
    txtfachadas.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtfachadas_actionPerformed(e);
        }
      });
    chkusoagrario.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkusoagrario_actionPerformed(e);
        }
      });
    txtlongfachadauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtlongfachadauc_actionPerformed(e);
        }
      });
    txtannoconsuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtannoconsuc_actionPerformed(e);
        }
      });
    txtdirnoestrucuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtdirnoestrucuc_actionPerformed(e);
        }
      });
    txtkilometrouc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtkilometrouc_actionPerformed(e);
        }
      });
    txtbloqueuc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtbloqueuc_actionPerformed(e);
        }
      });
    txtletra2uc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtletra2uc_actionPerformed(e);
        }
      });
    txtnumero2uc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnumero2uc_actionPerformed(e);
        }
      });
    txtletra1uc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtletra1uc_actionPerformed(e);
        }
      });
    txtnumero1uc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnumero1uc_actionPerformed(e);
        }
      });
    txtcodigopostaluc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcodigopostaluc_actionPerformed(e);
        }
      });
    txtnombreviauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnombreviauc_actionPerformed(e);
        }
      });
    txttipoviauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txttipoviauc_actionPerformed(e);
        }
      });
    txtcodviauc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcodviauc_actionPerformed(e);
        }
      });
    txtentmenoruc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtentmenoruc_actionPerformed(e);
        }
      });
    txttipoviauc.setEditable(false);
    txtnombreviauc.setEditable(false);
    txtnombreviauc.setEditable(false);
    txtcodviauc.setEditable(false);
    txtcodviauc.setEditable(false);
    txttipoviauc.setEditable(false);
    padronModel.addMouseListener(mouseListener);

    btnadduc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnadduc_actionPerformed(e);
        }
      });
    btnedituc.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnedituc_actionPerformed(e);
        }
      });
    padronModel.setBounds(new Rectangle(10, 20, 100, 55));
  }

  private void btnedituc_actionPerformed(ActionEvent e)
  {
     //Con esta opción modificamos los datos de la subparcela
    //En la variable ID tenemos el identificador de la subparcela
    //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos
    ArrayList UC= new ArrayList();
    ArrayList UCTipo= new ArrayList();
    ArrayList Checks = new ArrayList();
    Checks.add(chkusoagrario);
    Checks.add(chklongfachadauc);
    Checks.add(chkdepreciacionuc);
    Checks.add(chkespecialuc);
    Checks.add(chknolucrativouc);
    
    UC.add(String.valueOf(ID_Unidad));
    UCTipo.add("0");
    UC.add(txtordenuc.getText());
    UCTipo.add("1");
    UC.add(txtentmenoruc.getText());
    UCTipo.add("1");
    UC.add(txtnumero1uc.getText());
    UCTipo.add("0");    
    UC.add(txtletra1uc.getText());
    UCTipo.add("1");   
    UC.add(txtnumero2uc.getText());
    UCTipo.add("0");    
    UC.add(txtletra2uc.getText());
    UCTipo.add("1");
    UC.add(txtkilometrouc.getText());
    UCTipo.add("0");
    UC.add(txtbloqueuc.getText());
    UCTipo.add("1");
    UC.add(txtdirnoestrucuc.getText());
    UCTipo.add("1");    
    UC.add(txtcodigopostaluc.getText());
    UCTipo.add("0");  
    
    UC.add(txtannoconsuc.getText());
    UCTipo.add("0");    
    UC.add(txtlongfachadauc.getText());
    UCTipo.add("0");
    UC.add(txtfachadas.getText());
    UCTipo.add("0");  
    UC.add(cmbestadoconservacion.getSelectedItem());
    UCTipo.add("1");   
    UC.add(txtcargasuc.getText());
    UCTipo.add("0");   
    Iterator CheckIt = Checks.iterator();    
    while (CheckIt.hasNext()) 
    {
        if(((JCheckBox)CheckIt.next()).isSelected()==true){
        UC.add("TRUE");    
        }else{UC.add("FALSE");}       
        UCTipo.add("1");
     }
    UC.add(cmbaguauc.getSelectedItem());
    UCTipo.add("1");   
    UC.add(cmbelectricidaduc.getSelectedItem());
    UCTipo.add("1");
    UC.add(cmbalumbradouc.getSelectedItem());
    UCTipo.add("1");
    UC.add(cmbdesmonteuc.getSelectedItem());
    UCTipo.add("1");
    UC.add(cmbpavimentacionuc.getSelectedItem());
    UCTipo.add("1");
    UC.add(cmbalcantarilladouc.getSelectedItem());
    UCTipo.add("1");
    UC.add(cmbexactitudannouc.getSelectedItem());
    UCTipo.add("1");
     if (alta == true) {
       UC.add("A");
       UCTipo.add("1");   
    }else{
       UC.add("M");
       UCTipo.add("1");   
    }
    UC.add(txtannoexpuc.getText());
    UCTipo.add("0");   
    UC.add(txtrefexpuc.getText());
    UCTipo.add("1");  
    
    //Actualizamos la información almacenada
     CatastroActualizarPostgre ActualizarUC = new CatastroActualizarPostgre();
    if (alta==false){
     String Result = ActualizarUC.ActualizarUC(ID_Unidad, UC, UCTipo);
       System.out.println(Result);}
    else{
     String Result = ActualizarUC.AltaUC(ID_Unidad,ID_Parcela, UC, UCTipo);
       System.out.println(Result);
       alta=false;}
  }    
    public void enter()
  {
   AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());
     //Rellenamos los datos
     lstUCs.clear();
    try{
         CatastroActualizarPostgre UCs = new CatastroActualizarPostgre();    
          ArrayList Datos =  UCs.UCs(ID_Parcela);
          Iterator alIt = Datos.iterator();
          while (alIt.hasNext()) 
          {
              lstUCs.addElement(alIt.next());
          
          }
        } catch(Exception e)
          {
           e.printStackTrace();
          }   
  }


  private void btnadduc_actionPerformed(ActionEvent e)
  {
       alta=true; 
        Iterator alIt = Unidades.iterator();
        
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

  private void txtentmenoruc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtcodviauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txttipoviauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnombreviauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtcodigopostaluc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnumero1uc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtletra1uc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnumero2uc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtletra2uc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtbloqueuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtkilometrouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtdirnoestrucuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtannoconsuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtlongfachadauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void chkusoagrario_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtfachadas_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void chklongfachadauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbestadoconservacion_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtcargasuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void chkdepreciacionuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void chkespecialuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void chknolucrativouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbaguauc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbelectricidaduc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbalumbradouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbdesmonteuc_actionPerformed(ActionEvent e)
  {    change=true;
  }
  

   private void cmbpavimentacionuc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbalcantarilladouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbexactitudannouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbtipomovimientouc_actionPerformed(ActionEvent e)
  {    change=true;
  }

    private void txtannoexpuc_actionPerformed(ActionEvent e)
  {    change=true;
  }
    private void txtrefexpuc_actionPerformed(ActionEvent e)
  {    change=true;
  }
   public void setID (int ID)
  {
    ID_Parcela=ID;
  }
  public void exit()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
     Identificadores.put ("ID_Unidad", ID_Unidad);
  }

  private void btndeluc_actionPerformed(ActionEvent e)
  {   
    CatastroActualizarPostgre ActualizarUC = new CatastroActualizarPostgre();
     String Result = ActualizarUC.BajaUC (ID_Unidad);
  }
}