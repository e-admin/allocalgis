/**
 * ConstruccionRusticaPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;


public class ConstruccionRusticaPanel extends  JPanel implements FeatureExtendedPanel {
//public class ConstruccionRusticaPanel extends JPanel {
  private JLabel tipoviaparlbl3 = new JLabel();
  private JComboBox cmbUso = new JComboBox();
  private JTextField txtTipologia = new JTextField();
  private JLabel TIPOLOGIACONSLBL = new JLabel();
  private JLabel annoexpconslbl = new JLabel();
  private JTextField txtAnioConstruccion = new JTextField();
  private JLabel referenciaconslbl = new JLabel();
  private JLabel reformaconslbl = new JLabel();
  private JLabel tipoviaparlbl4 = new JLabel();
  private JComboBox cmbReforma = new JComboBox();
  private JComboBox cmbExactitud = new JComboBox();
  private JLabel referenciaconslbl1 = new JLabel();
  private JLabel annoreformalbl = new JLabel();
  private JComboBox cmbCargas = new JComboBox();
  private JTextField txtAnioReforma = new JTextField();
  private JComboBox cmbConservacion = new JComboBox();
  private JTextField txtSuperficieVolumen = new JTextField();
  private JLabel puertaconslbl = new JLabel();
  private JTextField txtAlto = new JTextField();
  private JLabel plantaconslbl = new JLabel();
  private JTextField txtAncho = new JTextField();
  private JLabel escaleralbl = new JLabel();
  private JLabel bloqueparlbl2 = new JLabel();
  private JTextField txtLargo = new JTextField();
  private JTextField txtOrden = new JTextField();
  private JLabel ordenconslbl = new JLabel();
  private JButton delconsbtn = new JButton();
  private JButton editconsbtn = new JButton();
  private JButton addconsbtn = new JButton();
  private int ID_Construccion;
  private int ID_Subparcela=2;
  private ArrayList Construcciones= new ArrayList();
  public boolean change= false;
  private boolean alta= false;
  DefaultListModel lstConstrucciones = new DefaultListModel();
  JList padronModel = new JList(lstConstrucciones);
  private SubparcelaRusticaPanel SubparcelaRusticaPanel;

   
  public ConstruccionRusticaPanel()
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
    JFrame frame1 = new JFrame("Construcciones Rústicas");
    
    ConstruccionRusticaPanel ConstruccionRusticaPanel = new ConstruccionRusticaPanel();

    frame1.getContentPane().add(ConstruccionRusticaPanel);
    frame1.setSize(675, 505);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }
  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setSize(new Dimension(676, 800));

    Construcciones.add (txtOrden);
    Construcciones.add (txtLargo);
    Construcciones.add (txtAncho);
    Construcciones.add(txtAlto);
    Construcciones.add(txtSuperficieVolumen);
    Construcciones.add(cmbExactitud);
    Construcciones.add(txtTipologia);
    Construcciones.add(txtAnioReforma);
    Construcciones.add(cmbReforma);
    Construcciones.add(cmbUso);
    Construcciones.add(cmbCargas);
    Construcciones.add(cmbConservacion);
  
//    jPanel1.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario"));
    tipoviaparlbl3.setText("Uso Predominante:");
    tipoviaparlbl3.setBounds(new Rectangle(15, 235, 115, 35));
    cmbUso.setBounds(new Rectangle(135, 240, 130, 20));
    txtTipologia.setText("55555");
    txtTipologia.setBounds(new Rectangle(145, 210, 50, 20));
    TIPOLOGIACONSLBL.setText("Tipología Constructiva:");
    TIPOLOGIACONSLBL.setBounds(new Rectangle(15, 200, 135, 40));
    annoexpconslbl.setText("Año Construcción:");
    annoexpconslbl.setBounds(new Rectangle(15, 165, 135, 40));
    txtAnioConstruccion.setText("4444");
    txtAnioConstruccion.setBounds(new Rectangle(145, 175, 40, 20));
    txtAnioConstruccion.setPreferredSize(new Dimension(53, 20));
    txtAnioConstruccion.setSize(new Dimension(40, 20));
    referenciaconslbl.setText("Exactitud Año:");
    referenciaconslbl.setBounds(new Rectangle(195, 165, 185, 35));
    reformaconslbl.setText("Tipo Reforma:");
    reformaconslbl.setBounds(new Rectangle(215, 200, 120, 40));
    tipoviaparlbl4.setText("Cargas Singulares:");
    tipoviaparlbl4.setBounds(new Rectangle(275, 235, 115, 35));
    cmbReforma.setBounds(new Rectangle(315, 210, 95, 20));
    cmbExactitud.setBounds(new Rectangle(285, 175, 105, 20));
    referenciaconslbl1.setText("Conservación:");
    referenciaconslbl1.setBounds(new Rectangle(410, 165, 185, 35));
    annoreformalbl.setText("Año Reforma:");
    annoreformalbl.setBounds(new Rectangle(430, 200, 105, 40));
    cmbCargas.setBounds(new Rectangle(395, 240, 170, 20));
    txtAnioReforma.setText("2004");
    txtAnioReforma.setBounds(new Rectangle(525, 210, 45, 20));
    cmbConservacion.setBounds(new Rectangle(510, 175, 100, 20));
    txtSuperficieVolumen.setText("666666");
    txtSuperficieVolumen.setBounds(new Rectangle(460, 140, 70, 20));
    txtSuperficieVolumen.setSize(new Dimension(70, 20));
    puertaconslbl.setText("Superficie Volumen:");
    puertaconslbl.setBounds(new Rectangle(345, 135, 125, 35));
    txtAlto.setText("4444");
    txtAlto.setBounds(new Rectangle(290, 140, 35, 20));
    plantaconslbl.setText("Alto:");
    plantaconslbl.setBounds(new Rectangle(230, 135, 55, 35));
    txtAncho.setText("AA");
    txtAncho.setBounds(new Rectangle(160, 140, 55, 20));
    escaleralbl.setText("Ancho:");
    escaleralbl.setBounds(new Rectangle(110, 135, 70, 35));
    bloqueparlbl2.setText("Largo:");
    bloqueparlbl2.setBounds(new Rectangle(15, 135, 60, 35));
    txtLargo.setText("333");
    txtLargo.setBounds(new Rectangle(65, 140, 35, 20));
    txtOrden.setText("4444");
    txtOrden.setBounds(new Rectangle(470, 40, 50, 20));
    ordenconslbl.setText("Nº Orden Construcción:");
    ordenconslbl.setBounds(new Rectangle(285, 30, 210, 40));
    delconsbtn.setText("Baja");
    delconsbtn.setBounds(new Rectangle(155, 60, 80, 20));
    editconsbtn.setText("Edición");
    editconsbtn.setBounds(new Rectangle(155, 40, 80, 20));
    addconsbtn.setText("Alta");
    addconsbtn.setBounds(new Rectangle(155, 20, 80, 20));
    padronModel.setBounds(new Rectangle(20, 25, 100, 55));
//    jPanel10.setBorder(BorderFactory.createTitledBorder("Datos Físicos"));
//    jPanel8.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario"));
//    jPanel18.setBorder(BorderFactory.createTitledBorder("Identificación"));
//    jPanel111.setBorder(BorderFactory.createTitledBorder("Identificación del Titular"));
//    editsubparbtn.setIcon(Edit);
//    addsubparbtn.setIcon(Add);
   
//    delsubparbtn.setIcon(Del);

//    Datostb.add("Parcela", parceltb);
   

    cmbUso.addItem("V - Vivienda");
    cmbReforma.addItem("R - 100%");
    cmbReforma.addItem("O - 75%");
    cmbReforma.addItem("E - 50%");
    cmbReforma.addItem("I - 25%");
    cmbExactitud.addItem("C - Estimado");
    cmbExactitud.addItem("- - Anterior");
    cmbExactitud.addItem("+ - Posterior");
    cmbExactitud.addItem("E - Exacto");
    cmbCargas.addItem("I - Integral");
    cmbCargas.addItem("E - Estructural");
    cmbCargas.addItem("A - Ambiental");
    cmbCargas.addItem("No tiene");
    cmbConservacion.addItem("Bueno");
    cmbConservacion.addItem("Malo");
    cmbConservacion.addItem("Regular");
    this.add(padronModel, null);
    this.add(addconsbtn, null);
    this.add(editconsbtn, null);
    this.add(delconsbtn, null);
    this.add(ordenconslbl, null);
    this.add(txtOrden, null);
    this.add(txtLargo, null);
    this.add(bloqueparlbl2, null);
    this.add(escaleralbl, null);
    this.add(txtAncho, null);
    this.add(plantaconslbl, null);
    this.add(txtAlto, null);
    this.add(puertaconslbl, null);
    this.add(txtSuperficieVolumen, null);
    this.add(cmbConservacion, null);
    this.add(txtAnioReforma, null);
    this.add(cmbCargas, null);
    this.add(annoreformalbl, null);
    this.add(referenciaconslbl1, null);
    this.add(cmbExactitud, null);
    this.add(cmbReforma, null);
    this.add(tipoviaparlbl4, null);
    this.add(reformaconslbl, null);
    this.add(referenciaconslbl, null);
    this.add(txtAnioConstruccion, null);
    this.add(annoexpconslbl, null);
    this.add(TIPOLOGIACONSLBL, null);
    this.add(txtTipologia, null);
    this.add(cmbUso, null);
    this.add(tipoviaparlbl3, null);


//Implementamos aquí el método ENTER
    lstConstrucciones.clear();
    try{
       CatastroRusticaActualizarPostgre Cons = new CatastroRusticaActualizarPostgre();    
       ArrayList Datos =  Cons.Construcciones(ID_Subparcela);
       Iterator alIt = Datos.iterator();
       while (alIt.hasNext()) 
       {
          lstConstrucciones.addElement( alIt.next().toString());
       }
    } catch(Exception e)
    {
       e.printStackTrace();
    }
//ENTER

    MouseListener mouseListener = new MouseAdapter() {
   public void mouseClicked(MouseEvent e) {
    int NumeroOrden =Integer.parseInt(padronModel.getSelectedValue()==null?"":padronModel.getSelectedValue().toString());
   
      Iterator alIt1 = Construcciones.iterator();
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
        CatastroRusticaActualizarPostgre Construc = new CatastroRusticaActualizarPostgre();
        ArrayList Datos= Construc.DatosConstrucciones(NumeroOrden, ID_Subparcela);
        if(Datos==null)return;
        Iterator alIt = Datos.iterator();
        Iterator itControles = Construcciones.iterator();
        txtOrden.setText(padronModel.getSelectedValue().toString());
        ID_Construccion=Integer.parseInt(((obj=alIt.next())!=null)?obj.toString():"0");
        System.out.println(ID_Construccion);
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
    delconsbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          delconsbtn_actionPerformed(e);
        }
      });
    editconsbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          editconsbtn_actionPerformed(e);
        }
      });
    addconsbtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          addconsbtn_actionPerformed(e);
        }
      });
    padronModel.setBounds(new Rectangle(10, 15, 100, 55));
    padronModel.addMouseListener(mouseListener);
  }

   public void enter()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Subparcela= Integer.parseInt(Identificadores.get("ID_SubparcelaRustica").toString());


    lstConstrucciones.clear();
    try{
       CatastroRusticaActualizarPostgre Cons = new CatastroRusticaActualizarPostgre();    
       ArrayList Datos =  Cons.Construcciones(ID_Subparcela);
       Iterator alIt = Datos.iterator();
       while (alIt.hasNext()) 
       {
          lstConstrucciones.addElement( alIt.next().toString());
       }
    } catch(Exception e)
    {
       e.printStackTrace();
    }
  }

  private void addconsbtn_actionPerformed(ActionEvent e)
  {
  alta=true; 
        Iterator alIt = Construcciones.iterator();
        
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

  private void editconsbtn_actionPerformed(ActionEvent e)
  {
    ArrayList UC= new ArrayList();
    ArrayList UCTipo= new ArrayList();
      
    UC.add(String.valueOf(ID_Construccion));
    UCTipo.add("0");
    UC.add(String.valueOf(ID_Subparcela));
    UCTipo.add("0");
    UC.add(txtOrden.getText());
    UCTipo.add("0");
    UC.add(txtLargo.getText());
    UCTipo.add("0");
    UC.add(txtAncho.getText());
    UCTipo.add("0");
    UC.add(txtAlto.getText());
    UCTipo.add("0");
    UC.add(txtSuperficieVolumen.getText());
    UCTipo.add("0");
    UC.add(txtAnioConstruccion.getText());
    UCTipo.add("0");
    UC.add(cmbExactitud.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1"); 
    UC.add(txtTipologia.getText());
    UCTipo.add("1");
    UC.add(txtAnioReforma.getText());
    UCTipo.add("0");
    UC.add(cmbReforma.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1"); 
    UC.add(cmbUso.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1"); 
    UC.add(cmbCargas.getSelectedItem().toString().substring(0,1));
    UCTipo.add("0"); 
    UC.add(cmbConservacion.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1"); 
    
     //Actualizamos la información almacenada
     CatastroRusticaActualizarPostgre ActualizarConstruccion = new CatastroRusticaActualizarPostgre();
     if (alta==false){
           String Result = ActualizarConstruccion.ActualizarConstruccion(ID_Construccion, UC, UCTipo);
           System.out.println(Result);}
    else{
           String Result = ActualizarConstruccion.AltaConstruccion(ID_Construccion,  UC, UCTipo);
           System.out.println(Result);
           alta=false; }
  }

  private void delconsbtn_actionPerformed(ActionEvent e)
  {
    CatastroRusticaActualizarPostgre ActualizarCons = new CatastroRusticaActualizarPostgre();
    String Result = ActualizarCons.BajaConstruccion (ID_Construccion);
  }

 public void setSubparcelaPanel (SubparcelaRusticaPanel Panel)
  {
    SubparcelaRusticaPanel=Panel;
  }

  public void exit()
  {
        
  }

 

}