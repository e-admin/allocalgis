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

//public class CargosPanel extends JPanel {
public class CargosPanel extends JPanel implements FeatureExtendedPanel {

  private JPanel cargopanel = new JPanel();
  private JPanel jPanel110 = new JPanel();
  private JLabel supcubiertaparlbl6 = new JLabel();
  private JTextField txtrefexpcargo = new JTextField();
  private JLabel annoexpparlbl4 = new JLabel();
  private JTextField txtannoexpcargo = new JTextField();
  private JPanel jPanel6 = new JPanel();
  private JTextField txtdirnoestruccargo = new JTextField();
  private JLabel kmparlbl2 = new JLabel();
  private JLabel bloqueparlbl3 = new JLabel();
  private JTextField txtbloquecargo = new JTextField();
  private JTextField txtkilometrocargo = new JTextField();
  private JLabel dirnoestrucparlbl2 = new JLabel();
  private JLabel letra2parlbl2 = new JLabel();
  private JLabel numero2parlbl3 = new JLabel();
  private JTextField txtnumero2cargo = new JTextField();
  private JTextField txtletra2cargo = new JTextField();
  private JLabel letra1parlbl2 = new JLabel();
  private JTextField txtletra1cargo = new JTextField();
  private JLabel numero1cargolbl = new JLabel();
  private JTextField txtnumero1cargo = new JTextField();
  private JLabel codigopostalcargolbl = new JLabel();
  private JTextField txtcodigopostalcargo = new JTextField();
  private JLabel nombreviacargolbl = new JLabel();
  private JTextField txtnombreviacargo = new JTextField();
  private JLabel tipoviacargolbl = new JLabel();
  private JTextField txttipoviacargo = new JTextField();
  private JLabel viapublicacargolbl = new JLabel();
  private JTextField txtcodviacargo = new JTextField();
  private JTextField txtentmenorcargo = new JTextField();
  private JLabel entmenorcargolbl = new JLabel();
  private JPanel jPanel112 = new JPanel();
  private JLabel participacioncargolbl = new JLabel();
  private JTextField txtparticipacioncargo = new JTextField();
  private JLabel fijocargolbl = new JLabel();
  private JTextField txtfijocargo = new JTextField();
  private JLabel cargocargolbl = new JLabel();
  private JTextField txtcargo = new JTextField();
  private JTextField txtcontrol1cargo = new JTextField();
  private JLabel controlcargolbl = new JLabel();
  private JTextField txtcontrol2cargo = new JTextField();
  private JTextField txtcontrolfijocargo = new JTextField();
  private JLabel controlfijocargolbl = new JLabel();
  private JTextField txtdistritocargo = new JTextField();
  private JLabel distritocargolbl = new JLabel();
  private JTextField txtpuertacargo = new JTextField();
  private JLabel puertaconslbl1 = new JLabel();
  private JTextField txtplantacargo = new JTextField();
  private JLabel plantaconslbl1 = new JLabel();
  private JTextField txtescaleracargo = new JTextField();
  private JLabel escaleralbl1 = new JLabel();
  private JPanel jPanel20 = new JPanel();
  private JLabel numero2parlbl4 = new JLabel();
  private JLabel formacalculoparlbl1 = new JLabel();
  private JTextField txtannonotificacion = new JTextField();
  private JLabel aprobacionparlbl1 = new JLabel();
  private JTextField txtannorevision = new JTextField();
  private JLabel supconsparlbl2 = new JLabel();
  private JTextField txtvalorcatastralcargo = new JTextField();
  private JTextField txtannovalorcargo = new JTextField();
  private JLabel supsolarlbl2 = new JLabel();
  private JTextField txtvalorsuelocargo = new JTextField();
  private JLabel supconsparlbl3 = new JLabel();
  private JTextField txtbaseliquidablecargo = new JTextField();
  private JLabel supconsparlbl4 = new JLabel();
  private JTextField txtvalorconstruccioncargo = new JTextField();
  private JLabel valorconstruccioncargolbl = new JLabel();
  private JTextField txtclaveusocargo = new JTextField();
  private JLabel tipoviacargolbl1 = new JLabel();
  private JTextField txtnotificacioncargo = new JTextField();
  private JTextField txtcoeficientepropcargo = new JTextField();
  private JLabel supcubiertaparlbl7 = new JLabel();
  private JTextField txtsupsuelocargo = new JTextField();
  private JLabel supconsparlbl5 = new JLabel();
  private JTextField txtsupconstruccioncargo = new JTextField();
  private JLabel supsolarlbl3 = new JLabel();
  private JTextField txtannofinvalorcargo = new JTextField();
  private JLabel supsolarlbl4 = new JLabel();
  private JCheckBox chkpreciocargo = new JCheckBox();
  private JLabel supcubiertaparlbl8 = new JLabel();
  private JComboBox cmbtipopropiedadcargo = new JComboBox();
  private JButton btndelcargo = new JButton();
  private JButton btnaddcargo = new JButton();
  private JButton btneditcargo = new JButton();
  private JButton cargo = new JButton();
  private int ID_Cargo;
  private int ID_Titular;
  private String ID_Construccion;
  public boolean alta= false;
  public boolean change= false;
  DefaultListModel lstCargos = new DefaultListModel();
  private ArrayList Cargos = new ArrayList();
  JList padronModel = new JList(lstCargos);
 
  
  public CargosPanel()
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

/* public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    
    CargosPanel geopistaEditarDatos = new CargosPanel("0671805UN9107S0003");

    frame1.getConteantPane().add(geopistaEditarDatos);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }*/
  private void jbInit() throws Exception
  {
    this.setName("Cargos");
    this.setLayout(null);
    this.setSize(new Dimension(676, 800));


    Cargos.add(txtcargo);
    Cargos.add(txtcontrol1cargo);
    Cargos.add(txtcontrol2cargo);
    Cargos.add(txtfijocargo);
    Cargos.add(txtparticipacioncargo);
    Cargos.add(txtentmenorcargo);
    Cargos.add(txtnumero1cargo);
    Cargos.add(txtletra1cargo);
    Cargos.add(txtnumero2cargo);
    Cargos.add(txtletra2cargo);
    Cargos.add(txtkilometrocargo);
    Cargos.add(txtbloquecargo);
    Cargos.add(txtdirnoestruccargo);
    Cargos.add(txtcodigopostalcargo);
    Cargos.add(txtescaleracargo);
    Cargos.add(txtplantacargo);
    Cargos.add(txtpuertacargo);
    Cargos.add(txtannovalorcargo);
    Cargos.add(txtvalorcatastralcargo);
    Cargos.add(txtvalorsuelocargo);
    Cargos.add(txtvalorconstruccioncargo);
    Cargos.add(txtbaseliquidablecargo);
    Cargos.add(txtclaveusocargo);
    Cargos.add(txtannorevision);
    Cargos.add(txtannonotificacion);
    Cargos.add(txtnotificacioncargo);
    Cargos.add(txtsupconstruccioncargo);
    Cargos.add(txtsupsuelocargo);
    Cargos.add(txtcoeficientepropcargo);
    Cargos.add(txtannofinvalorcargo);
    Cargos.add(cmbtipopropiedadcargo);
    Cargos.add(txtannoexpcargo);
    Cargos.add(txtrefexpcargo);
    Cargos.add(txtcodviacargo);
    Cargos.add(txttipoviacargo);
    Cargos.add(txtnombreviacargo);
    cargopanel.setLayout(null);
    jPanel110.setBounds(new Rectangle(5, 480, 625, 50));
    jPanel110.setLayout(null);
    jPanel110.setBorder(BorderFactory.createTitledBorder("Datos del Movimiento"));
    supcubiertaparlbl6.setText("Referencia Expediente:");
    txtrefexpcargo.setText("");
    txtrefexpcargo.setBounds(new Rectangle(345, 20, 110, 20));
    txtrefexpcargo.setSize(new Dimension(110, 20));
    annoexpparlbl4.setText("Año Expediente:");
    txtannoexpcargo.setText("");
    txtannoexpcargo.setBounds(new Rectangle(110, 20, 40, 20));
    txtannoexpcargo.setPreferredSize(new Dimension(53, 20));
    txtannoexpcargo.setSize(new Dimension(40, 20));
    jPanel6.setBounds(new Rectangle(5, 100, 625, 185));
    jPanel6.setLayout(null);
    jPanel6.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario del Elemento"));
    txtdirnoestruccargo.setText("");
    txtdirnoestruccargo.setBounds(new Rectangle(410, 120, 205, 20));
    kmparlbl2.setText("Kilómetro:");
    bloqueparlbl3.setText("Bloque:");
    txtbloquecargo.setText("");
    txtbloquecargo.setBounds(new Rectangle(55, 120, 35, 20));
    txtkilometrocargo.setText("");
    txtkilometrocargo.setBounds(new Rectangle(165, 120, 80, 20));
    dirnoestrucparlbl2.setText("Direccion No Estructurada:");
    letra2parlbl2.setText("Segunda Letra:");
    numero2parlbl3.setText("Segundo Número:");
    txtnumero2cargo.setText("4444");
    txtnumero2cargo.setBounds(new Rectangle(405, 85, 35, 20));
    txtletra2cargo.setText("");
    txtletra2cargo.setBounds(new Rectangle(565, 85, 25, 20));
    letra1parlbl2.setText("Primera Letra:");
    txtletra1cargo.setText("");
    txtletra1cargo.setBounds(new Rectangle(245, 85, 25, 20));
    numero1cargolbl.setText("Primer Número:");
    txtnumero1cargo.setText("");
    txtnumero1cargo.setBounds(new Rectangle(100, 85, 35, 20));
    codigopostalcargolbl.setText("Código Postal:");
    txtcodigopostalcargo.setText("1");
    txtcodigopostalcargo.setBounds(new Rectangle(425, 55, 50, 20));
    nombreviacargolbl.setText("Nombre Vía:");
    txtnombreviacargo.setText("");
    txtnombreviacargo.setBounds(new Rectangle(90, 55, 230, 20));
    tipoviacargolbl.setText("Tipo de Vía:");
    txttipoviacargo.setText("");
    txttipoviacargo.setBounds(new Rectangle(510, 25, 45, 20));
    viapublicacargolbl.setText("Código Vía Pública:");
    viapublicacargolbl.setBounds(new java.awt.Rectangle(204,27,135,15));
    txtcodviacargo.setText("");
    txtcodviacargo.setBounds(new Rectangle(345, 25, 50, 20));
    txtentmenorcargo.setText("");
    txtentmenorcargo.setBounds(new Rectangle(150, 25, 35, 20));
    entmenorcargolbl.setText("Código Entidad Menor:");
    entmenorcargolbl.setBounds(new java.awt.Rectangle(6,26,121,15));
    jPanel112.setBounds(new Rectangle(5, 10, 625, 90));
    jPanel112.setLayout(null);
    jPanel112.setBorder(BorderFactory.createTitledBorder("Identificación"));
    participacioncargolbl.setText("Coeficiente Paticipación:");
    txtparticipacioncargo.setText("");
    txtparticipacioncargo.setBounds(new Rectangle(355, 60, 60, 20));
    fijocargolbl.setText("Nº Fijo Bien Inmueble:");
    txtfijocargo.setText("");
    txtfijocargo.setBounds(new Rectangle(335, 35, 95, 20));
    
    cargocargolbl.setText("Nº Cargo:");
    txtcargo.setText("");
    txtcargo.setBounds(new Rectangle(285, 10, 50, 20));
    txtcontrol1cargo.setText("");
    txtcontrol1cargo.setBounds(new Rectangle(470, 10, 25, 20));
    controlcargolbl.setText("Caracteres Control:");
    txtcontrol2cargo.setText("");
    txtcontrol2cargo.setBounds(new Rectangle(505, 10, 25, 20));
    txtcontrolfijocargo.setText("");
    txtcontrolfijocargo.setBounds(new Rectangle(550, 35, 25, 20));
    controlfijocargolbl.setText("Carácter Control:");
    txtdistritocargo.setText("");
    txtdistritocargo.setBounds(new Rectangle(535, 60, 40, 20));
    distritocargolbl.setText("Distrito Censal:");
    txtpuertacargo.setText("");
    txtpuertacargo.setBounds(new Rectangle(305, 150, 40, 20));
    puertaconslbl1.setText("Puerta:");
    txtplantacargo.setText("");
    txtplantacargo.setBounds(new Rectangle(180, 150, 35, 20));
    plantaconslbl1.setText("Planta:");
    txtescaleracargo.setText("");
    txtescaleracargo.setBounds(new Rectangle(70, 150, 40, 20));
    escaleralbl1.setText("Escalera:");
    jPanel20.setBounds(new Rectangle(5, 290, 625, 185));
    jPanel20.setLayout(null);
    jPanel20.setBorder(BorderFactory.createTitledBorder("Datos Económicos Valorativos del Bien Inmueble"));
    numero2parlbl4.setText("Nº Última Notificación:");
    formacalculoparlbl1.setText("Año Notificación:");
    txtannonotificacion.setText("");
    txtannonotificacion.setBounds(new Rectangle(235, 90, 40, 20));
    aprobacionparlbl1.setText("Año Revisión:");
    txtannorevision.setText("");
    txtannorevision.setBounds(new Rectangle(90, 90, 35, 20));
    supconsparlbl2.setText("Valor Catastral:");
    txtvalorcatastralcargo.setText("");
    txtvalorcatastralcargo.setBounds(new Rectangle(290, 25, 95, 20));
    txtvalorcatastralcargo.setPreferredSize(new Dimension(53, 20));
    txtvalorcatastralcargo.setSize(new Dimension(95, 20));
    txtannovalorcargo.setText("");
    txtannovalorcargo.setBounds(new Rectangle(125, 25, 45, 20));
    supsolarlbl2.setText("Año Valor Catastral:");
    txtvalorsuelocargo.setText("");
    txtvalorsuelocargo.setBounds(new Rectangle(520, 25, 95, 20));
    txtvalorsuelocargo.setPreferredSize(new Dimension(53, 20));
    txtvalorsuelocargo.setSize(new Dimension(95, 20));
    supconsparlbl3.setText("Valor Suelo:");
    txtbaseliquidablecargo.setText("");
    txtbaseliquidablecargo.setBounds(new Rectangle(380, 60, 95, 20));
    txtbaseliquidablecargo.setPreferredSize(new Dimension(53, 20));
    txtbaseliquidablecargo.setSize(new Dimension(95, 20));
    supconsparlbl4.setText("Base Liquidable:");
    txtvalorconstruccioncargo.setText("");
    txtvalorconstruccioncargo.setBounds(new Rectangle(150, 60, 95, 20));
    txtvalorconstruccioncargo.setPreferredSize(new Dimension(53, 20));
    txtvalorconstruccioncargo.setSize(new Dimension(95, 20));
    valorconstruccioncargolbl.setText("Valor  Construcción:");
    txtclaveusocargo.setText("");
    txtclaveusocargo.setBounds(new Rectangle(570, 60, 45, 20));
    tipoviacargolbl1.setText("Clave Uso:");
    txtnotificacioncargo.setText("");
    txtnotificacioncargo.setBounds(new Rectangle(425, 90, 95, 20));
    txtnotificacioncargo.setPreferredSize(new Dimension(53, 20));
    txtnotificacioncargo.setSize(new Dimension(95, 20));
    txtcoeficientepropcargo.setText("");
    txtcoeficientepropcargo.setBounds(new Rectangle(555, 120, 65, 20));
    txtcoeficientepropcargo.setSize(new Dimension(65, 20));
    supcubiertaparlbl7.setText("Coef. Propiedad:");
    txtsupsuelocargo.setText("");
    txtsupsuelocargo.setBounds(new Rectangle(360, 120, 65, 20));
    txtsupsuelocargo.setPreferredSize(new Dimension(53, 20));
    txtsupsuelocargo.setSize(new Dimension(65, 20));
    supconsparlbl5.setText("Superficie Suelo:");
    txtsupconstruccioncargo.setText("");
    txtsupconstruccioncargo.setBounds(new Rectangle(175, 120, 65, 20));
    supsolarlbl3.setText("Sup. Elementos Constructivos:");
    txtannofinvalorcargo.setText("");
    txtannofinvalorcargo.setBounds(new Rectangle(290, 150, 45, 20));
    supsolarlbl4.setText("Año Finalización Valoración:");
    chkpreciocargo.setText("Precio Máximo ");
    chkpreciocargo.setBounds(new Rectangle(5, 148, 110, 25));
    supcubiertaparlbl8.setText("Tipo Propiedad:");
    cmbtipopropiedadcargo.setBounds(new Rectangle(465, 150, 155, 20));
   
    btndelcargo.setText("Baja");
    btndelcargo.setBounds(new Rectangle(120, 60, 78, 20));
    btneditcargo.setText("Edición");
    btneditcargo.setBounds(new Rectangle(120, 40, 78, 20));
    btnaddcargo.setText("Alta");
    btnaddcargo.setBounds(new Rectangle(120, 20, 78, 20));

        
    cmbtipopropiedadcargo.addItem("T - Sobre Todo");
    cmbtipopropiedadcargo.addItem("S - Sobre Suelo");
    cmbtipopropiedadcargo.addItem("C - Sobre Construcción");
    cmbtipopropiedadcargo.addItem("V - Sobre Vuelo");
    cmbtipopropiedadcargo.addItem("I - Sobre Inmueble");
    cmbtipopropiedadcargo.addItem("N - No informado");            
    jPanel20.add(cmbtipopropiedadcargo, null);
    jPanel20.add(supcubiertaparlbl8, null);
    jPanel20.add(chkpreciocargo, null);
    jPanel20.add(supsolarlbl4, null);
    jPanel20.add(txtannofinvalorcargo, null);
    jPanel20.add(supsolarlbl3, null);
    jPanel20.add(txtsupconstruccioncargo, null);
    jPanel20.add(supconsparlbl5, null);
    jPanel20.add(txtsupsuelocargo, null);
    jPanel20.add(supcubiertaparlbl7, null);
    jPanel20.add(txtcoeficientepropcargo, null);
    jPanel20.add(txtnotificacioncargo, null);
    jPanel20.add(tipoviacargolbl1, null);
    jPanel20.add(txtclaveusocargo, null);
    jPanel20.add(valorconstruccioncargolbl, null);
    jPanel20.add(txtvalorconstruccioncargo, null);
    jPanel20.add(supconsparlbl4, null);
    jPanel20.add(txtbaseliquidablecargo, null);
    jPanel20.add(supconsparlbl3, null);
    jPanel20.add(txtvalorsuelocargo, null);
    jPanel20.add(numero2parlbl4, null);
    jPanel20.add(formacalculoparlbl1, null);
    jPanel20.add(txtannonotificacion, null);
    jPanel20.add(aprobacionparlbl1, null);
    jPanel20.add(txtannorevision, null);
    jPanel20.add(supconsparlbl2, null);
    jPanel20.add(txtvalorcatastralcargo, null);
    jPanel20.add(txtannovalorcargo, null);
    jPanel20.add(supsolarlbl2, null);
    jPanel112.add(padronModel, null);
    jPanel112.add(btnaddcargo, null);
    jPanel112.add(btneditcargo, null);
    jPanel112.add(btndelcargo, null);
    jPanel112.add(distritocargolbl, null);
    jPanel112.add(txtdistritocargo, null);
    jPanel112.add(controlfijocargolbl, null);
    jPanel112.add(txtcontrolfijocargo, null);
    jPanel112.add(txtcontrol2cargo, null);
    jPanel112.add(controlcargolbl, null);
    jPanel112.add(txtcontrol1cargo, null);
    jPanel112.add(participacioncargolbl, null);
    jPanel112.add(txtparticipacioncargo, null);
    jPanel112.add(fijocargolbl, null);
    jPanel112.add(txtfijocargo, null);
    jPanel112.add(cargocargolbl, null);
    jPanel112.add(txtcargo, null);
    cargopanel.add(jPanel20, null);
    cargopanel.add(jPanel112, null);
    cargopanel.add(jPanel110, null);
    cargopanel.add(jPanel6, null);
    jPanel110.add(supcubiertaparlbl6, null);
    jPanel110.add(txtrefexpcargo, null);
    jPanel110.add(annoexpparlbl4, null);
    jPanel110.add(txtannoexpcargo, null);
    formacalculoparlbl1.setSize(91, 15);
    formacalculoparlbl1.setLocation(140, 93);
    supconsparlbl4.setSize(89, 15);
    supconsparlbl4.setLocation(280, 62);
    codigopostalcargolbl.setSize(90, 15);
    codigopostalcargolbl.setLocation(330, 58);
    controlfijocargolbl.setSize(105, 15);
    controlfijocargolbl.setLocation(440, 38);
    plantaconslbl1.setSize(37, 15);
    plantaconslbl1.setLocation(135, 151);
    tipoviacargolbl.setSize(66, 15);
    tipoviacargolbl.setLocation(419, 28);
    dirnoestrucparlbl2.setSize(136, 15);
    dirnoestrucparlbl2.setLocation(256, 124);
    aprobacionparlbl1.setSize(73, 15);
    aprobacionparlbl1.setLocation(10, 93);
    kmparlbl2.setSize(51, 15);
    kmparlbl2.setLocation(106, 124);
    supconsparlbl2.setSize(90, 15);
    supconsparlbl2.setLocation(190, 28);
    fijocargolbl.setSize(111, 15);
    fijocargolbl.setLocation(215, 37);
    puertaconslbl1.setSize(47, 15);
    puertaconslbl1.setLocation(254, 152);
    numero1cargolbl.setSize(85, 15);
    numero1cargolbl.setLocation(4, 87);
    jPanel6.add(escaleralbl1, null);
    jPanel6.add(txtescaleracargo, null);
    jPanel6.add(plantaconslbl1, null);
    jPanel6.add(txtplantacargo, null);
    jPanel6.add(puertaconslbl1, null);
    jPanel6.add(txtpuertacargo, null);
    jPanel6.add(txtdirnoestruccargo, null);
    jPanel6.add(kmparlbl2, null);
    jPanel6.add(bloqueparlbl3, null);
    jPanel6.add(txtbloquecargo, null);
    jPanel6.add(txtkilometrocargo, null);
    jPanel6.add(dirnoestrucparlbl2, null);
    jPanel6.add(letra2parlbl2, null);
    jPanel6.add(numero2parlbl3, null);
    jPanel6.add(txtnumero2cargo, null);
    jPanel6.add(txtletra2cargo, null);
    jPanel6.add(letra1parlbl2, null);
    jPanel6.add(txtletra1cargo, null);
    jPanel6.add(numero1cargolbl, null);
    jPanel6.add(txtnumero1cargo, null);
    jPanel6.add(codigopostalcargolbl, null);
    jPanel6.add(txtcodigopostalcargo, null);
    jPanel6.add(nombreviacargolbl, null);
    jPanel6.add(txtnombreviacargo, null);
    jPanel6.add(tipoviacargolbl, null);
    jPanel6.add(txttipoviacargo, null);
    jPanel6.add(viapublicacargolbl, null);
    jPanel6.add(txtcodviacargo, null);
    jPanel6.add(txtentmenorcargo, null);
    jPanel6.add(entmenorcargolbl, null);
    supcubiertaparlbl7.setSize(94, 15);
    supcubiertaparlbl7.setLocation(454, 121);
    numero2parlbl3.setSize(93, 15);
    numero2parlbl3.setLocation(305, 87);
    annoexpparlbl4.setSize(95, 15);
    annoexpparlbl4.setLocation(9, 21);
    supconsparlbl5.setSize(85, 15);
    supconsparlbl5.setLocation(264, 123);
    escaleralbl1.setSize(51, 15);
    escaleralbl1.setLocation(11, 153);
    letra1parlbl2.setSize(75, 15);
    letra1parlbl2.setLocation(164, 88);
    nombreviacargolbl.setSize(68, 15);
    nombreviacargolbl.setLocation(8, 58);
    letra2parlbl2.setSize(81, 15);
    letra2parlbl2.setLocation(475, 88);
    supsolarlbl2.setSize(103, 15);
    supsolarlbl2.setLocation(10, 27);
    participacioncargolbl.setSize(123, 15);
    participacioncargolbl.setLocation(213, 62);
    supcubiertaparlbl8.setSize(92, 15);
    supcubiertaparlbl8.setLocation(368, 153);
    supconsparlbl3.setSize(72, 15);
    supconsparlbl3.setLocation(435, 27);
    distritocargolbl.setSize(81, 15);
    distritocargolbl.setLocation(443, 62);
    bloqueparlbl3.setSize(44, 15);
    bloqueparlbl3.setLocation(8, 122);
    controlcargolbl.setSize(100, 15);
    controlcargolbl.setLocation(354, 13);
    numero2parlbl4.setSize(125, 15);
    numero2parlbl4.setLocation(290, 93);
    tipoviacargolbl1.setSize(60, 15);
    tipoviacargolbl1.setLocation(505, 62);
    cargocargolbl.setSize(59, 15);
    cargocargolbl.setLocation(214, 13);
    supcubiertaparlbl6.setSize(119, 15);
    supcubiertaparlbl6.setLocation(210, 22);
    valorconstruccioncargolbl.setSize(107, 15);
    valorconstruccioncargolbl.setLocation(9, 64);
    supsolarlbl3.setSize(158, 15);
    supsolarlbl3.setLocation(9, 122);
    supsolarlbl4.setSize(142, 15);
    supsolarlbl4.setLocation(141, 154);

    btnaddcargo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnaddcargo_actionPerformed(e);
        }
      });

  btneditcargo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btneditcargo_actionPerformed(e);
        }
      });

       btndelcargo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btndelcargo_actionPerformed(e);
        }
      });

    this.add(jPanel110);
    this.add(jPanel6);
    this.add(jPanel112);
    this.add(jPanel20); 


/*    try{
      CatastroActualizarPostgre Cargo = new CatastroActualizarPostgre();    
      System.out.println("Rellenamos lst " + ID_Construccion);
      ArrayList Datos =  Cargo.Cargos(ID_Construccion);
      lstCargos.clear();
      Iterator alIt = Datos.iterator();
      while (alIt.hasNext()) 
          {
              lstCargos.addElement(alIt.next().toString());
          }
    }catch(Exception e)
          {
           e.printStackTrace();
          }   

*/
    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        String cargo =padronModel.getSelectedValue()==null?"":padronModel.getSelectedValue().toString();
      Iterator alIt1 = Cargos.iterator();
        Object obj;
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
        CatastroActualizarPostgre Cargo = new CatastroActualizarPostgre();
        System.out.println(ID_Construccion);
        ArrayList Datos= Cargo.DatosCargos(cargo, ID_Construccion);
        if(Datos==null)return;
        Iterator alIt = Datos.iterator();
        Iterator itControles = Cargos.iterator();
        txtcargo.setText(cargo);
        ID_Cargo=Integer.parseInt(((obj=alIt.next())!=null)?obj.toString():"0");
        ID_Titular= Integer.parseInt(((obj=alIt.next())!=null)?obj.toString():"0");
        while (alIt.hasNext()) 
        {
            try
            {
               JComponent comp=(JComponent)itControles.next();
               if (comp instanceof JTextField)((JTextField)comp).setText(((obj=alIt.next())!=null)?obj.toString():"");
              if (comp instanceof JCheckBox){
                  String check = ((obj=alIt.next())!=null)?obj.toString():"";
                  if (check == "TRUE"){
                    ((JCheckBox)comp).setSelected(true);}
                  else{
                    ((JCheckBox)comp).setSelected(false);}}
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem(((obj=alIt.next())!=null)?obj.toString():"");
              }

            catch(Exception A)
            {
                A.printStackTrace();
            }
    }}
  };
    padronModel.setBounds(new Rectangle(10, 15, 100, 55));
    padronModel.addMouseListener(mouseListener);

  
  }
 public void enter()
  {

    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Construccion= Identificadores.get("ID_Construccion").toString();

    lstCargos.clear();
    try{
       CatastroActualizarPostgre Cargo = new CatastroActualizarPostgre();    
       System.out.println("Rellenamos lst " + ID_Construccion);
       ArrayList Datos =  Cargo.Cargos(ID_Construccion);
       Iterator alIt = Datos.iterator();
       while (alIt.hasNext()) 
       {
          lstCargos.addElement( alIt.next().toString());
       }
    } catch(Exception e)
    {
       e.printStackTrace();
    }
  }

  private void btneditcargo_actionPerformed(ActionEvent e)
  {
     //Con esta opción modificamos los datos de la subparcela
    //En la variable ID tenemos el identificador de la subparcela
    //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos
    ArrayList UC= new ArrayList();
    ArrayList UCTipo= new ArrayList();
      
    UC.add(String.valueOf(ID_Cargo));
    UCTipo.add("0");
    UC.add(txtcargo.getText());
    UCTipo.add("1");
    UC.add(txtcontrol1cargo.getText());
    UCTipo.add("1");
    UC.add(txtcontrol2cargo.getText());
    UCTipo.add("1");
    UC.add(txtfijocargo.getText());
    UCTipo.add("0");
    UC.add(txtparticipacioncargo.getText());
    UCTipo.add("0");
    UC.add(txtentmenorcargo.getText());
    UCTipo.add("1");
    UC.add(txtnumero1cargo.getText());
    UCTipo.add("0");
    UC.add(txtletra1cargo.getText());
    UCTipo.add("1");
    UC.add(txtnumero2cargo.getText());
    UCTipo.add("0");
    UC.add(txtletra2cargo.getText());
    UCTipo.add("1");
    UC.add(txtkilometrocargo.getText());
    UCTipo.add("0");
    UC.add(txtbloquecargo.getText());
    UCTipo.add("1");
    UC.add(txtdirnoestruccargo.getText());
    UCTipo.add("1");
    UC.add(txtcodigopostalcargo.getText());
    UCTipo.add("0");
    UC.add(txtescaleracargo.getText());
    UCTipo.add("1");    
    UC.add(txtplantacargo.getText());
    UCTipo.add("1");   
    UC.add(txtpuertacargo.getText());
    UCTipo.add("1");    
    UC.add(txtannovalorcargo.getText());
    UCTipo.add("0");
    UC.add(txtvalorcatastralcargo.getText());
    UCTipo.add("0");
    UC.add(txtvalorsuelocargo.getText());
    UCTipo.add("0");
    UC.add(txtvalorconstruccioncargo.getText());
    UCTipo.add("0");
    UC.add(txtbaseliquidablecargo.getText());
    UCTipo.add("0");
    UC.add(txtclaveusocargo.getText());
    UCTipo.add("1");
    UC.add(txtannorevision.getText());
    UCTipo.add("0");
    UC.add(txtannonotificacion.getText());
    UCTipo.add("0");
    UC.add(txtnotificacioncargo.getText());
    UCTipo.add("0");
    UC.add(txtsupconstruccioncargo.getText());
    UCTipo.add("0");
    UC.add(txtsupsuelocargo.getText());
    UCTipo.add("0");
    UC.add(txtcoeficientepropcargo.getText());
    UCTipo.add("0");
    UC.add(txtannofinvalorcargo.getText());
    UCTipo.add("0");
    UC.add(cmbtipopropiedadcargo.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1"); 
    if (alta == true) {
       UC.add("A");
       UCTipo.add("1");   
    }else{
       UC.add("M");
       UCTipo.add("1");   
   }
    UC.add(txtannoexpcargo.getText());
    UCTipo.add("0");
    UC.add(txtrefexpcargo.getText());
    UCTipo.add("1");  
    
    //Actualizamos la información almacenada
     CatastroActualizarPostgre ActualizarCargo = new CatastroActualizarPostgre();
     if (alta==false){
           String Result = ActualizarCargo.ActualizarCargo(ID_Cargo, UC, UCTipo);
           System.out.println(Result);}
    else{
           String Result = ActualizarCargo.AltaCargo(ID_Construccion,  UC, UCTipo);
           System.out.println(Result);
           alta=false;
        }
       
  }

  private void btnaddcargo_actionPerformed(ActionEvent e)
  {
        alta=true; 
        Iterator alIt = Cargos.iterator();
        
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

  public void exit()
  {
  //Será necesario pasar al Panel de Titulares el ID_Titular
  AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
      Identificadores.put ("ID_Titular", ID_Titular);
  }
 
 private void btndelcargo_actionPerformed(ActionEvent e)
  {
    CatastroActualizarPostgre ActualizarCargo = new CatastroActualizarPostgre();
    String Result = ActualizarCargo.BajaCargo (ID_Cargo);
  }


}