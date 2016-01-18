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



//public class TitularRusticaPanel extends JPanel {
public class TitularRusticaPanel extends JPanel implements  FeatureExtendedPanel {

  
  private JPanel jPanel22 = new JPanel();
  private JPanel jPanel111 = new JPanel();
  
  
  private JLabel escaleralbl2 = new JLabel();
  private JTextField txtescaleratitular = new JTextField();
  private JLabel plantaconslbl2 = new JLabel();
  private JTextField txtplantatitular = new JTextField();
  private JLabel puertaconslbl2 = new JLabel();
  private JTextField txtpuertatitular = new  JTextField();
  
  private JTextField txtdirnoestructitular = new JTextField();
  private JLabel kmparlbl3 = new JLabel();
  private JLabel bloqueparlbl4 = new JLabel();
  private JTextField txtbloquetitular = new JTextField();
  private JTextField txtkilometrotitular = new JTextField();
  private JLabel dirnoestrucparlbl3 = new JLabel();
  private JLabel letra2parlbl3 = new JLabel();
  private JLabel numero2parlbl6 = new JLabel();
  private JTextField txtnumero2titular = new JTextField();
  private JTextField txtletra2titular = new JTextField();
  private JLabel letra1parlbl3 = new JLabel();
  private JTextField txtletra1titular = new JTextField();
  private JLabel numero1cargolbl1 = new JLabel();
  private JTextField txtnumero1titular = new JTextField();
  private JLabel nombreviacargolbl1 = new JLabel();
  private JTextField txtDireccion = new JTextField();
  
  private JComboBox cmbpersonalidadtitular = new JComboBox();
  private JLabel fondosubparlbl1 = new JLabel();
  private JTextField txtImputacion = new JTextField();
  private JLabel superficiesubparlbl1 = new JLabel();
  private JTextField txtnombretitular = new JTextField();
  private JLabel personalidadtitularlbl = new JLabel();
  private JTextField txtniftitular = new JTextField();
  private JLabel niftitularlbl = new JLabel();
  private JTextField txtcontrolniftitular = new JTextField();
  private JTextField txtClaveExencion = new JTextField();
  private JLabel tipoviacargolbl4 = new JLabel();
  private JTextField txtCodigoPostal = new JTextField();
  private JLabel apartadocorreostitularlbl = new JLabel();
  private JTextField txtMunicipioTitular = new JTextField();
  private JLabel nombrepaistitulartxt2 = new JLabel();
  public boolean alta=false;
  public boolean change=false;
  public int ID_Parcela=2;
  public int ID_Titular;
  private JButton btnModificarConstruccion = new JButton();
  private JButton btnNuevaConstruccion = new JButton();
  private JButton btnBorrarConstruccion = new JButton();
  private ArrayList Titulares= new ArrayList();
  private JTextField txtParticipacion = new JTextField();
  private JLabel fondosubparlbl2 = new JLabel();
  private JLabel nombrepaistitulartxt3 = new JLabel();
  private JTextField txtDelegacionTitular = new JTextField();
  
  public TitularRusticaPanel()
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
    
    TitularRusticaPanel TitularRusticaPanel = new TitularRusticaPanel();

    frame1.getContentPane().add(TitularRusticaPanel);
    frame1.setSize(650, 475);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);

    
  }
 

  private void jbInit() throws Exception
  {
  this.setName("Titular");
	this.setLayout(null);
	this.setSize(new Dimension(600, 550));
	this.setBounds(new java.awt.Rectangle(5,10,636,493)); 
    fondosubparlbl2.setSize(115, 15);
    fondosubparlbl2.setLocation(5, 88);
    //Creamos el array de ocntroles TITULARES
    nombrepaistitulartxt3.setSize(102, 15);
    nombrepaistitulartxt3.setLocation(8, 237);
    Titulares.add(txtImputacion);
    Titulares.add(txtParticipacion);
    Titulares.add(cmbpersonalidadtitular);
    Titulares.add(txtniftitular);
    Titulares.add(txtcontrolniftitular);
    Titulares.add(txtnombretitular);
    Titulares.add(txtClaveExencion);
    Titulares.add(txtDelegacionTitular);
    Titulares.add(txtMunicipioTitular);
    Titulares.add(txtnumero1titular);
    Titulares.add(txtletra1titular);
    Titulares.add(txtnumero2titular);
    Titulares.add(txtletra2titular);
    Titulares.add(txtkilometrotitular);
    Titulares.add(txtbloquetitular);
    Titulares.add(txtdirnoestructitular);
    Titulares.add(txtescaleratitular);
    Titulares.add(txtplantatitular);
    Titulares.add(txtpuertatitular);
    Titulares.add(txtCodigoPostal);
    Titulares.add(txtDireccion);


  
    jPanel22.setBounds(new Rectangle(5, 140, 625, 175));
    jPanel22.setLayout(null);
    jPanel22.setBorder(BorderFactory.createTitledBorder("Domicilio Fiscal del Titular"));
    escaleralbl2.setText("Escalera:");
    txtescaleratitular.setText("AA");
    txtescaleratitular.setBounds(new Rectangle(70, 135, 40, 20));
    plantaconslbl2.setText("Planta:");
    txtplantatitular.setText("4444");
    txtplantatitular.setBounds(new Rectangle(180, 135, 35, 20));
    puertaconslbl2.setText("Puerta:");
    txtpuertatitular.setText("AAA");
    txtpuertatitular.setBounds(new Rectangle(305, 135, 40, 20));
    txtdirnoestructitular.setText("");
    txtdirnoestructitular.setBounds(new Rectangle(410, 105, 205, 20));
    kmparlbl3.setText("Kilómetro:");
    bloqueparlbl4.setText("Bloque:");
    txtbloquetitular.setText("");
    txtbloquetitular.setBounds(new Rectangle(55, 105, 35, 20));
    txtkilometrotitular.setText("");
    txtkilometrotitular.setBounds(new Rectangle(165, 105, 80, 20));
    dirnoestrucparlbl3.setText("Direccion No Estructurada:");
    letra2parlbl3.setText("Segunda Letra:");
    numero2parlbl6.setText("Segundo Número:");
    txtnumero2titular.setText("");
    txtnumero2titular.setBounds(new Rectangle(405, 70, 35, 20));
    txtletra2titular.setText("");
    txtletra2titular.setBounds(new Rectangle(565, 70, 25, 20));
    letra1parlbl3.setText("Primera Letra:");
    txtletra1titular.setText("");
    txtletra1titular.setBounds(new Rectangle(245, 70, 25, 20));
    numero1cargolbl1.setText("Primer Número:");
    txtnumero1titular.setText("");
    txtnumero1titular.setBounds(new Rectangle(100, 70, 35, 20));
    nombreviacargolbl1.setText("Dirección:");
    txtDireccion.setText("");
    txtDireccion.setBounds(new Rectangle(90, 40, 500, 20));
    jPanel111.setBounds(new Rectangle(5, 10, 625, 130));
    jPanel111.setLayout(null);
    jPanel111.setBorder(BorderFactory.createTitledBorder("Identificación del Titular"));
    cmbpersonalidadtitular.setBounds(new Rectangle(430, 25, 155, 20));
    fondosubparlbl1.setText("Número Imputación:");
    txtImputacion.setText("");
    txtImputacion.setBounds(new Rectangle(115, 85, 80, 20));
    txtImputacion.setSize(new Dimension(80, 20));
    superficiesubparlbl1.setText("Nombre y Apellidos:");
    txtnombretitular.setText("");
    txtnombretitular.setBounds(new Rectangle(135, 55, 470, 20));
    txtnombretitular.setSize(new Dimension(565, 20));
    personalidadtitularlbl.setText("Personalidad:");
    txtniftitular.setText("");
    txtniftitular.setBounds(new Rectangle(85, 25, 145, 20));
    niftitularlbl.setText("NIF Titular:");
    txtcontrolniftitular.setText("");
    txtcontrolniftitular.setBounds(new Rectangle(240, 25, 25, 20));
    txtClaveExencion.setText("");
    txtClaveExencion.setBounds(new Rectangle(560, 85, 45, 20));
    tipoviacargolbl4.setText("Clave Exención:");
    txtCodigoPostal.setText("");
    txtCodigoPostal.setBounds(new Rectangle(495, 135, 50, 20));
    apartadocorreostitularlbl.setText("Código Postal:");
    txtMunicipioTitular.setText("");
    txtMunicipioTitular.setBounds(new Rectangle(115, 10, 210, 20));
    txtMunicipioTitular.setSize(new Dimension(210, 20));
    nombrepaistitulartxt2.setText("Nombre Municipio:");
    btnModificarConstruccion.setText("Aplicar");
    btnModificarConstruccion.setBounds(new Rectangle(315, 320, 80, 20));
    btnModificarConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnModificarConstruccion_actionPerformed(e);
        }
      });
    btnNuevaConstruccion.setText("Alta");
    btnNuevaConstruccion.setBounds(new Rectangle(435, 320, 80, 20));
    btnNuevaConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnNuevaConstruccion_actionPerformed(e);
        }
      });
    btnBorrarConstruccion.setText("Baja");
    btnBorrarConstruccion.setBounds(new Rectangle(530, 320, 80, 20));
    jPanel111.add(fondosubparlbl2, null);
    jPanel111.add(txtParticipacion, null);
   
    jPanel111.add(tipoviacargolbl4, null);
    jPanel111.add(txtClaveExencion, null);
    jPanel111.add(txtcontrolniftitular, null);
    jPanel111.add(cmbpersonalidadtitular, null);
    jPanel111.add(fondosubparlbl1, null);
    jPanel111.add(txtImputacion, null);
    jPanel111.add(superficiesubparlbl1, null);
    jPanel111.add(txtnombretitular, null);
    jPanel111.add(personalidadtitularlbl, null);
    jPanel111.add(txtniftitular, null);
    jPanel111.add(niftitularlbl, null);
    cmbpersonalidadtitular.addItem("F - Física");
    cmbpersonalidadtitular.addItem("J - Jurídica");
    cmbpersonalidadtitular.addItem("E - Entidades");
    escaleralbl2.setSize(50, 15);
    escaleralbl2.setLocation(6, 173);
    bloqueparlbl4.setSize(42, 15);
    bloqueparlbl4.setLocation(5, 143);
    plantaconslbl2.setSize(41, 15);
    plantaconslbl2.setLocation(134, 174);
    letra1parlbl3.setSize(73, 15);
    letra1parlbl3.setLocation(162, 108);
    dirnoestrucparlbl3.setSize(133, 15);
    dirnoestrucparlbl3.setLocation(261, 142);
    kmparlbl3.setSize(52, 15);
    kmparlbl3.setLocation(108, 144);
    nombrepaistitulartxt2.setSize(102, 15);
    nombrepaistitulartxt2.setLocation(8, 237);
    apartadocorreostitularlbl.setSize(108, 15);
    apartadocorreostitularlbl.setLocation(385, 173);
    numero2parlbl6.setSize(101, 15);
    numero2parlbl6.setLocation(290, 109);
    nombreviacargolbl1.setSize(68, 15);
    nombreviacargolbl1.setLocation(5, 79);
    puertaconslbl2.setLocation(251, 172);
    puertaconslbl2.setSize(44, 15);
    numero1cargolbl1.setSize(82, 15);
    numero1cargolbl1.setLocation(5, 108);
    letra2parlbl3.setSize(95, 15);
    letra2parlbl3.setLocation(475, 107);
    jPanel22.add(txtDelegacionTitular, null);
    jPanel22.add(nombrepaistitulartxt3, null);
    jPanel22.add(nombrepaistitulartxt2, null);
    jPanel22.add(txtMunicipioTitular, null);
    jPanel22.add(apartadocorreostitularlbl, null);
    jPanel22.add(txtCodigoPostal, null);
    jPanel22.add(escaleralbl2, null);
    jPanel22.add(txtescaleratitular, null);
    jPanel22.add(plantaconslbl2, null);
    jPanel22.add(txtplantatitular, null);
    jPanel22.add(puertaconslbl2, null);
    jPanel22.add(txtpuertatitular, null);
    jPanel22.add(txtdirnoestructitular, null);
    jPanel22.add(kmparlbl3, null);
    jPanel22.add(bloqueparlbl4, null);
    jPanel22.add(txtbloquetitular, null);
    jPanel22.add(txtkilometrotitular, null);
    jPanel22.add(dirnoestrucparlbl3, null);
    jPanel22.add(letra2parlbl3, null);
    jPanel22.add(numero2parlbl6, null);
    jPanel22.add(txtnumero2titular, null);
    jPanel22.add(txtletra2titular, null);
    jPanel22.add(letra1parlbl3, null);
    jPanel22.add(txtletra1titular, null);
    jPanel22.add(numero1cargolbl1, null);
    jPanel22.add(txtnumero1titular, null);
    jPanel22.add(nombreviacargolbl1, null);
    jPanel22.add(txtDireccion, null);
    superficiesubparlbl1.setSize(107, 15);
    superficiesubparlbl1.setLocation(5, 58);
    tipoviacargolbl4.setSize(90, 15);
    tipoviacargolbl4.setLocation(464, 87);
    personalidadtitularlbl.setSize(95, 15);
    personalidadtitularlbl.setLocation(331, 29);
    niftitularlbl.setSize(61, 15);
    niftitularlbl.setLocation(7, 26);
    fondosubparlbl1.setSize(115, 15);
    fondosubparlbl1.setLocation(5, 88);
    this.add(btnBorrarConstruccion, null);
    this.add(btnNuevaConstruccion, null);
    this.add(btnModificarConstruccion, null);
    this.add(jPanel22, null);
    this.add(jPanel111, null);

        Iterator alIt1 = Titulares.iterator();
    txtDelegacionTitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnombremunicipiotitular_actionPerformed(e);
        }
      });
    txtDelegacionTitular.setSize(new Dimension(210, 20));
    txtDelegacionTitular.setBounds(new Rectangle(495, 10, 85, 20));
    txtDelegacionTitular.setText("");
    nombrepaistitulartxt3.setBounds(new Rectangle(345, 15, 130, 15));
    nombrepaistitulartxt3.setText("Código Delegación MEH:");
    nombreviacargolbl1.setBounds(new Rectangle(5, 45, 68, 15));
    numero1cargolbl1.setBounds(new Rectangle(5, 75, 82, 15));
    letra1parlbl3.setBounds(new Rectangle(165, 75, 73, 15));
    plantaconslbl2.setBounds(new Rectangle(135, 140, 41, 15));
    escaleralbl2.setBounds(new Rectangle(10, 140, 50, 15));
    bloqueparlbl4.setBounds(new Rectangle(5, 110, 42, 15));
    kmparlbl3.setBounds(new Rectangle(110, 110, 52, 15));
    puertaconslbl2.setBounds(new Rectangle(255, 140, 44, 15));
    apartadocorreostitularlbl.setBounds(new Rectangle(385, 140, 108, 15));
    numero2parlbl6.setBounds(new Rectangle(290, 75, 101, 15));
    dirnoestrucparlbl3.setBounds(new Rectangle(265, 110, 133, 15));
    nombrepaistitulartxt2.setBounds(new Rectangle(5, 10, 102, 15));
    fondosubparlbl2.setBounds(new Rectangle(215, 90, 135, 15));
    fondosubparlbl2.setText("Ceoficiente Participación:");
    txtParticipacion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcomplementotitular_actionPerformed(e);
        }
      });
    txtParticipacion.setSize(new Dimension(80, 20));
    txtParticipacion.setBounds(new Rectangle(355, 85, 80, 20));
    txtParticipacion.setText("1234567");
    fondosubparlbl1.setBounds(new Rectangle(5, 90, 105, 15));
    btnBorrarConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnBorrarConstruccion_actionPerformed(e);
        }
      });
        
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
       

      txtMunicipioTitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnombremunicipiotitular_actionPerformed(e);
        }
      });
    txtCodigoPostal.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtapartadocorreostitular_actionPerformed(e);
        }
      });
    txtpuertatitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtpuertatitular_actionPerformed(e);
        }
      });
    txtplantatitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtplantatitular_actionPerformed(e);
        }
      });
    txtescaleratitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtescaleratitular_actionPerformed(e);
        }
      });
    txtdirnoestructitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtdirnoestructitular_actionPerformed(e);
        }
      });
    txtkilometrotitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtkilometrotitular_actionPerformed(e);
        }
      });
    txtbloquetitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtbloquetitular_actionPerformed(e);
        }
      });
    txtletra2titular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtletra2titular_actionPerformed(e);
        }
      });
    letra2parlbl3.setBounds(new Rectangle(475, 70, 95, 15));
    txtnumero2titular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnumero2titular_actionPerformed(e);
        }
      });
    txtletra1titular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtletra1titular_actionPerformed(e);
        }
      });
    txtnumero1titular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnumero1titular_actionPerformed(e);
        }
      });
    txtDireccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnombreviatitular_actionPerformed(e);
        }
      });
    txtClaveExencion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txttitularcomponentes_actionPerformed(e);
        }
      });
    txtImputacion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcomplementotitular_actionPerformed(e);
        }
      });
    txtnombretitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtnombretitular_actionPerformed(e);
        }
      });
    cmbpersonalidadtitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbpersonalidadtitular_actionPerformed(e);
        }
      });
    txtcontrolniftitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcontrolniftitular_actionPerformed(e);
        }
      });
    txtniftitular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtniftitular_actionPerformed(e);
        }
      });
     


//Implementamos aquí el método enter aquí
  //Con el ID_Parcela ocnseguimos el número de imputación y con él el ID_Ttitular
      
     CatastroRusticaActualizarPostgre Titul = new CatastroRusticaActualizarPostgre();
     ID_Titular= Titul.ObtenerIDTitular (ID_Parcela);
     System.out.println(ID_Titular);
     ArrayList Datos= Titul.DatosTitular(ID_Titular);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = Titulares.iterator();
     while (alIt.hasNext()) 
      {
                  try
                  {
                     JComponent comp=(JComponent)itControles.next();
                     Object obj=alIt.next();
                     if (comp instanceof JTextField)((JTextField)comp).setText((obj!=null)?obj.toString():"");
                    if (comp instanceof JCheckBox){
                        String check = (obj!=null)?obj.toString():"";
                        if (check == "TRUE"){
                          ((JCheckBox)comp).setSelected(true);}
                        else{
                          ((JCheckBox)comp).setSelected(false);}}
                   if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                    }

                  catch(Exception A)
                  {
                      A.printStackTrace();
                  }
          }



}

  private void btnModificarConstruccion_actionPerformed(ActionEvent e)

  {
   //Con esta opción modificamos los datos de la subparcela
    //En la variable ID tenemos el identificador de la subparcela
    //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos
    ArrayList UC= new ArrayList();
    ArrayList UCTipo= new ArrayList();
    UC.add(String.valueOf(ID_Titular));
    UCTipo.add("0");
    UC.add(txtImputacion.getText());
    UCTipo.add("1");
    UC.add(txtParticipacion.getText());
    UCTipo.add("1");
    UC.add(cmbpersonalidadtitular.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1");    
    UC.add(txtniftitular.getText());
    UCTipo.add("1");
    UC.add(txtcontrolniftitular.getText());
    UCTipo.add("1");
    UC.add(txtnombretitular.getText());
    UCTipo.add("1");   
    UC.add(txtClaveExencion.getText());
    UCTipo.add("1");    
    UC.add(txtDelegacionTitular.getText());
    UCTipo.add("1");
    UC.add(txtMunicipioTitular.getText());
    UCTipo.add("1");
    UC.add(txtnumero1titular.getText());
    UCTipo.add("0");
    UC.add(txtletra1titular.getText());
    UCTipo.add("1");
    UC.add(txtnumero2titular.getText());
    UCTipo.add("0");    
    UC.add(txtletra2titular.getText());
    UCTipo.add("1");  
    UC.add(txtkilometrotitular.getText());
    UCTipo.add("0");    
    UC.add(txtbloquetitular.getText());
    UCTipo.add("1");  
    UC.add(txtdirnoestructitular.getText());
    UCTipo.add("1");    
    UC.add(txtescaleratitular.getText());
    UCTipo.add("1");
    UC.add(txtplantatitular.getText());
    UCTipo.add("1");  
    UC.add(txtpuertatitular.getText());
    UCTipo.add("1");   
    UC.add(txtCodigoPostal.getText());
    UCTipo.add("0");
  
    //Actualizamos la información almacenada
     CatastroRusticaActualizarPostgre ActualizarTitular = new CatastroRusticaActualizarPostgre();
    if (alta==false){
     String Result = ActualizarTitular.ActualizarTitular(ID_Titular, UC, UCTipo);
       System.out.println(Result);}
    else{
      String Result = ActualizarTitular.AltaTitular(UC, UCTipo);
       System.out.println(Result);
       alta=false;}  
 
  }

 


  private void btnNuevaConstruccion_actionPerformed(ActionEvent e)
  {
        alta=true; 
        Iterator alIt = Titulares.iterator();
        
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

  private void txtniftitular_actionPerformed(ActionEvent e)
  {
    change=true;
  }

  private void txtcontrolniftitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void cmbpersonalidadtitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnombretitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtcomplementotitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txttitularcomponentes_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnombreviatitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtcodigopostaltitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnumero1titular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtletra1titular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnumero2titular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtletra2titular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtbloquetitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtkilometrotitular_actionPerformed(ActionEvent e)
  {
  }

  private void txtdirnoestructitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtescaleratitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtplantatitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtpuertatitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtapartadocorreostitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtpaistitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnombreprovinciatitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

  private void txtnombremunicipiotitular_actionPerformed(ActionEvent e)
  {    change=true;
  }

 public void enter()
  {
  //Con el ID_Parcela ocnseguimos el número de imputación y con él el ID_Ttitular
  AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Parcela= Integer.parseInt(Identificadores.get("ID_ParcelaRustica").toString());
    
     CatastroRusticaActualizarPostgre Titul = new CatastroRusticaActualizarPostgre();
     ID_Titular= Titul.ObtenerIDTitular (ID_Parcela);
     System.out.println(ID_Titular);
     ArrayList Datos= Titul.DatosTitular(ID_Titular);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = Titulares.iterator();
     while (alIt.hasNext()) 
      {
                  try
                  {
                     JComponent comp=(JComponent)itControles.next();
                     Object obj=alIt.next();
                     if (comp instanceof JTextField)((JTextField)comp).setText((obj!=null)?obj.toString():"");
                    if (comp instanceof JCheckBox){
                        String check = (obj!=null)?obj.toString():"";
                        if (check == "TRUE"){
                          ((JCheckBox)comp).setSelected(true);}
                        else{
                          ((JCheckBox)comp).setSelected(false);}}
                   if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                    }

                  catch(Exception A)
                  {
                      A.printStackTrace();
                  }
          }




      }
  public void exit()
  {
  }


  private void btnBorrarConstruccion_actionPerformed(ActionEvent e)
  {
     CatastroRusticaActualizarPostgre ActualizarTitular = new CatastroRusticaActualizarPostgre();
     String Result = ActualizarTitular.BajaTitular (ID_Titular);
  }
  
}  