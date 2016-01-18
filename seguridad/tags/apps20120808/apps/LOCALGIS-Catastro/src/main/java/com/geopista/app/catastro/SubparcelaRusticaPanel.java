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
import com.geopista.app.catastro.*;
import com.geopista.util.*;
import com.geopista.ui.*;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;


public class SubparcelaRusticaPanel extends JPanel implements FeatureExtendedPanel {
//public class SubparcelaRusticaPanel extends JPanel {

private JTextField txtR2 = new JTextField();
  private JTextField txtR1 = new JTextField();
  private JLabel entmenorprlbl10 = new JLabel();
  private JLabel entmenorprlbl2 = new JLabel();
  private JTextField txtIntensidadanterior = new JTextField();
  private JTextField txtIntensidadactual = new JTextField();
  private JLabel entmenorprlbl8 = new JLabel();
  private JLabel entmenorprlbl9 = new JLabel();
  private JLabel coef1lbl3 = new JLabel();
  private JTextField txtCalificacionanterior = new JTextField();
  private JTextField txtCalificacionactual = new JTextField();
  private JLabel entmenorprlbl7 = new JLabel();
  private JLabel entmenorprlbl6 = new JLabel();
  private JLabel coef1lbl = new JLabel();
  private JLabel entmenorprlbl5 = new JLabel();
  private JTextField txtHectareas = new JTextField();
  private JLabel bloqueparlbl9 = new JLabel();
  private JLabel bloqueparlbl8 = new JLabel();
  private JTextField txtAreas = new JTextField();
  private JTextField txtCentiArea = new JTextField();
  private JLabel bloqueparlbl7 = new JLabel();
  private JLabel fondosubparlbl = new JLabel();
  private JTextField txtFinalizacion = new JTextField();
  private JComboBox cmbCoeficientefiscal = new JComboBox();
  private JLabel longfachadasubparlbl = new JLabel();
  private JTextField txtletra = new JTextField();
  private JLabel ordensubparlbl = new JLabel();
  private JLabel tipofachadasubparlbl = new JLabel();
  private JComboBox cmbTipo = new JComboBox();
  private JButton delsubparbtn = new JButton();
  private JButton editsubparbtn = new JButton();
  private JButton addsubparbtn = new JButton();
  private int ID_Parcela=1;
  public int ID_Subparcela;
  private ArrayList Subparcela= new ArrayList();
  public boolean change= false;
  private boolean alta= false;
  DefaultListModel lstSubparcelas = new DefaultListModel();
  JList padronModel = new JList(lstSubparcelas);


  public SubparcelaRusticaPanel()
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
    
    SubparcelaRusticaPanel SubparcelaRusticaPanel = new SubparcelaRusticaPanel();

    frame1.getContentPane().add(SubparcelaRusticaPanel);
    frame1.setSize(675, 505);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }
  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setSize(new Dimension(676, 800));
        Subparcela.add(txtletra);
        Subparcela.add(cmbTipo);
        Subparcela.add(cmbCoeficientefiscal);
        Subparcela.add(txtFinalizacion);
        Subparcela.add(txtHectareas);
        Subparcela.add(txtAreas);
        Subparcela.add(txtCentiArea);
        Subparcela.add(txtCalificacionanterior);
        Subparcela.add(txtIntensidadanterior);
        Subparcela.add(txtCalificacionactual);
        Subparcela.add(txtIntensidadactual);
        Subparcela.add(txtR1);
        Subparcela.add(txtR2);


        
//    jPanel1.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario"));
    txtR2.setText("");
    txtR2.setBounds(new Rectangle(305, 305, 110, 20));
    txtR2.setSize(new Dimension(110, 20));
    txtR1.setText("");
    txtR1.setBounds(new Rectangle(305, 270, 110, 20));
    txtR1.setSize(new Dimension(110, 20));
    entmenorprlbl10.setText("Referencia catastral de la construcción:");
    entmenorprlbl10.setBounds(new Rectangle(15, 295, 235, 40));
    entmenorprlbl2.setText("Referencia catastral urbana de la construcción:");
    entmenorprlbl2.setBounds(new Rectangle(15, 260, 315, 40));
    txtIntensidadanterior.setText("");
    txtIntensidadanterior.setBounds(new Rectangle(315, 200, 50, 20));
    txtIntensidadactual.setText("");
    txtIntensidadactual.setBounds(new Rectangle(315, 230, 50, 20));
    txtIntensidadactual.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          intensidadactualtxt_actionPerformed(e);
        }
      });
    entmenorprlbl8.setText("Actual:");
    entmenorprlbl8.setBounds(new Rectangle(245, 220, 100, 40));
    entmenorprlbl9.setText("Anterior:");
    entmenorprlbl9.setBounds(new Rectangle(245, 190, 100, 40));
    coef1lbl3.setText("Intensidad Productiva:");
    coef1lbl3.setBounds(new Rectangle(200, 165, 135, 35));
    txtCalificacionanterior.setText("22");
    txtCalificacionanterior.setBounds(new Rectangle(130, 200, 50, 20));
    txtCalificacionactual.setText("22");
    txtCalificacionactual.setBounds(new Rectangle(130, 230, 50, 20));
    entmenorprlbl7.setText("Actual:");
    entmenorprlbl7.setBounds(new Rectangle(60, 220, 100, 40));
    entmenorprlbl6.setText("Anterior:");
    entmenorprlbl6.setBounds(new Rectangle(60, 190, 100, 40));
    coef1lbl.setText("Calificación Catastral:");
    coef1lbl.setBounds(new Rectangle(15, 165, 135, 35));
    entmenorprlbl5.setText("Superficie:");
    entmenorprlbl5.setBounds(new Rectangle(15, 130, 100, 40));
    txtHectareas.setText("55555");
    txtHectareas.setBounds(new Rectangle(85, 140, 50, 20));
    bloqueparlbl9.setText("Ha.");
    bloqueparlbl9.setBounds(new Rectangle(145, 135, 30, 35));
    bloqueparlbl8.setText("A.");
    bloqueparlbl8.setBounds(new Rectangle(235, 135, 30, 35));
    txtAreas.setText("55555");
    txtAreas.setBounds(new Rectangle(175, 140, 50, 20));
    txtCentiArea.setText("");
    txtCentiArea.setBounds(new Rectangle(265, 140, 50, 20));
    bloqueparlbl7.setText("Ca.");
    bloqueparlbl7.setBounds(new Rectangle(325, 135, 30, 35));
    fondosubparlbl.setText("Finalización Ejercicio:");
    fondosubparlbl.setBounds(new Rectangle(370, 135, 140, 35));
    txtFinalizacion.setText("");
    txtFinalizacion.setBounds(new Rectangle(490, 140, 55, 20));
    txtFinalizacion.setSize(new Dimension(55, 20));
    cmbCoeficientefiscal.setBounds(new Rectangle(125, 110, 300, 20));
    longfachadasubparlbl.setText("Coeficiente Fiscal:");
    longfachadasubparlbl.setBounds(new Rectangle(10, 100, 145, 40));
    txtletra.setText("PPP4");
    txtletra.setBounds(new Rectangle(315, 20, 50, 20));
    ordensubparlbl.setText("Letra:");
    ordensubparlbl.setBounds(new Rectangle(270, 10, 45, 40));
    tipofachadasubparlbl.setText("Tipo:");
    tipofachadasubparlbl.setBounds(new Rectangle(250, 45, 125, 40));
    cmbTipo.setBounds(new Rectangle(295, 55, 295, 20));
    delsubparbtn.setText("Baja");
    delsubparbtn.setBounds(new Rectangle(150, 60, 80, 20));
    editsubparbtn.setText("Edición");
    editsubparbtn.setBounds(new Rectangle(150, 40, 80, 20));
    addsubparbtn.setText("Alta");
    addsubparbtn.setBounds(new Rectangle(150, 20, 80, 20));
    padronModel.setBounds(new Rectangle(20, 25, 100, 55));
//    jPanel10.setBorder(BorderFactory.createTitledBorder("Datos Físicos"));
//    jPanel8.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario"));
//    jPanel18.setBorder(BorderFactory.createTitledBorder("Identificación"));
//    jPanel111.setBorder(BorderFactory.createTitledBorder("Identificación del Titular"));
//    editsubparbtn.setIcon(Edit);
//    addsubparbtn.setIcon(Add);
   
//    delsubparbtn.setIcon(Del);

//    Datostb.add("Parcela", parceltb);
   

    cmbCoeficientefiscal.addItem("  - Tributación General sin Beneficicios ");
    cmbCoeficientefiscal.addItem("A - Permanente Subjetiva");
    cmbCoeficientefiscal.addItem("J - Permanente Objetiva");
    cmbCoeficientefiscal.addItem("4 - Temporal");
    cmbTipo.addItem("T - Subparcela corresponde a una unidad de terreno");
    cmbTipo.addItem("C - Subparcela corresponde a una unidad de construcción");
    cmbTipo.addItem("D - Subparcela corresponde a una unidad de terreno considerado Descuento Planimétrico");
    cmbTipo.addItem("A - Subparcela corresponde a una subparcela abstracta");
    cmbTipo.addItem("V - Vuelo");
    this.add(padronModel, null);
    this.add(addsubparbtn, null);
    this.add(editsubparbtn, null);
    this.add(delsubparbtn, null);
    this.add(cmbTipo, null);
    this.add(tipofachadasubparlbl, null);
    this.add(ordensubparlbl, null);
    this.add(txtletra, null);
    this.add(longfachadasubparlbl, null);
    this.add(cmbCoeficientefiscal, null);
    this.add(txtFinalizacion, null);
    this.add(fondosubparlbl, null);
    this.add(bloqueparlbl7, null);
    this.add(txtCentiArea, null);
    this.add(txtAreas, null);
    this.add(bloqueparlbl8, null);
    this.add(bloqueparlbl9, null);
    this.add(txtHectareas, null);
    this.add(entmenorprlbl5, null);
    this.add(coef1lbl, null);
    this.add(entmenorprlbl6, null);
    this.add(entmenorprlbl7, null);
    this.add(txtCalificacionactual, null);
    this.add(txtCalificacionanterior, null);
    this.add(coef1lbl3, null);
    this.add(entmenorprlbl9, null);
    this.add(entmenorprlbl8, null);
    this.add(txtIntensidadactual, null);
    this.add(txtIntensidadanterior, null);
    this.add(entmenorprlbl2, null);
    this.add(entmenorprlbl10, null);
    this.add(txtR1, null);
    this.add(txtR2, null);

//ENTER
    //Rellenamos los datos
    try{
      CatastroRusticaActualizarPostgre Subparcelas = new CatastroRusticaActualizarPostgre();    
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
//enter

    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        ID_Subparcela =Integer.parseInt(padronModel.getSelectedValue().toString());
        CatastroRusticaActualizarPostgre Subparcel = new CatastroRusticaActualizarPostgre();
        ArrayList Datos=Subparcel.DatosSubparcela(ID_Subparcela);
        Iterator alIt = Datos.iterator();
        Iterator itControles = Subparcela.iterator();
//         ID=Integer.parseInt(alIt.next().toString());
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
    delsubparbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          delsubparbtn_actionPerformed(e);
        }
      });
    editsubparbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          editsubparbtn_actionPerformed(e);
        }
      });
    addsubparbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          addsubparbtn_actionPerformed(e);
        }
      });

    padronModel.addMouseListener(mouseListener);

  }

  private void intensidadactualtxt_actionPerformed(ActionEvent e)
  {
  }

  private void addsubparbtn_actionPerformed(ActionEvent e)
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

  private void editsubparbtn_actionPerformed(ActionEvent e)
  {
    ArrayList Subparcelas= new ArrayList();
    ArrayList SubparcelaTipo= new ArrayList();

 
    Subparcelas.add(String.valueOf(ID_Subparcela));
    SubparcelaTipo.add("0");
    Subparcelas.add(String.valueOf(ID_Parcela));
    SubparcelaTipo.add("0");
    Subparcelas.add(txtletra.getText());
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbTipo.getSelectedItem().toString().substring(0,1));
    SubparcelaTipo.add("1");
    Subparcelas.add(cmbCoeficientefiscal.getSelectedItem().toString().substring(0,2));
    SubparcelaTipo.add("1");
    Subparcelas.add(txtFinalizacion.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(txtHectareas.getText());
    SubparcelaTipo.add("0");    
    Subparcelas.add(txtAreas.getText());
    SubparcelaTipo.add("0");   
    Subparcelas.add(txtCentiArea.getText());
    SubparcelaTipo.add("0");
    Subparcelas.add(txtCalificacionanterior.getText());
    SubparcelaTipo.add("1");   
    Subparcelas.add(txtIntensidadanterior.getText());
    SubparcelaTipo.add("0");  
    Subparcelas.add(txtCalificacionactual.getText());
    SubparcelaTipo.add("1");   
    Subparcelas.add(txtIntensidadactual.getText());

    SubparcelaTipo.add("0");  
    Subparcelas.add(txtR1.getText());
    SubparcelaTipo.add("1");   
    Subparcelas.add(txtR2.getText());
    SubparcelaTipo.add("1");   



    /*if (alta == true) {
       Subparcelas.add("A");
       SubparcelaTipo.add("1");   
    }else{
           Subparcelas.add("M");
           SubparcelaTipo.add("1");   
   }*/
    //Actualizamos la información almacenada
    CatastroRusticaActualizarPostgre ActualizarSubparcela = new CatastroRusticaActualizarPostgre();
    if (alta==false){
       String Result = ActualizarSubparcela.ActualizarSubparcela(ID_Subparcela, Subparcelas, SubparcelaTipo);
       System.out.println(Result);
      }
    else{

       String Result = ActualizarSubparcela.AltaSubparcela(Subparcelas, SubparcelaTipo);
       alta=false;
       System.out.println(Result);}

  }

  private void delsubparbtn_actionPerformed(ActionEvent e)
  {
      CatastroRusticaActualizarPostgre ActualizarSubparcela = new CatastroRusticaActualizarPostgre();
     String Result = ActualizarSubparcela.BajaSubparcela (ID_Subparcela);
     System.out.println(Result);
  }

    public void exit()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
     Identificadores.put ("ID_SubparcelaRustica", ID_Subparcela);
    
  }
    public void enter()
  {
    //Recuperamos el ID_Parcela y el ID_UnidadConstruccion
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Parcela= Integer.parseInt(Identificadores.get("ID_ParcelaRustica").toString());

    //Rellenamos los datos
    try{
      CatastroRusticaActualizarPostgre Subparcelas = new CatastroRusticaActualizarPostgre();    
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

}