/**
 * ConstruccionesPanel.java
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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;


//public class ConstruccionesPanel extends JPanel {
public class ConstruccionesPanel extends  JPanel implements FeatureExtendedPanel {

  private JPanel jPanel7 = new JPanel();
  private JLabel lblSuperficie_Imputable_Local = new JLabel();
  private JTextField txtSuperficie_Imputable_Local = new JTextField();
  private JLabel lblSuperficie_Terrazas_Local = new JLabel();
  private JTextField txtSuperficie_Terrazas_Local = new JTextField();
  private JTextField txtSuperficie_Total_Local = new JTextField();
  private JLabel supsolarlbl1 = new JLabel();
  private JPanel jPanel8 = new JPanel();
  private JLabel lblEscalera = new JLabel();
  private JLabel lblBloque = new JLabel();
  private JTextField txtBloque = new JTextField();
  private JTextField txtEscalera = new JTextField();
  private JLabel lblPlanta = new JLabel();
  private JTextField txtPlanta = new JTextField();
  private JLabel lblPuerta = new JLabel();
  private JTextField txtPuerta = new JTextField();
  private JPanel jPanel18 = new JPanel();
  private JComboBox cmbreformacons = new JComboBox();
  private JLabel reformaconslbl = new JLabel();
  private JTextField txtannoreforma = new JTextField();
  private JLabel lblAnio_Reforma = new JLabel();
  private JTextField txtdestinocons = new JTextField();
  private JLabel lblCodigo_Destino_DGC = new JLabel();
  private JCheckBox chkIndicador_Local_Interior = new JCheckBox();
  private JPanel jPanel17 = new JPanel();
  private JTextField txtapreciacioncons = new JTextField();
  private JLabel lblCorrector_Apreciacion_Economica = new JLabel();
  private JLabel coef1lbl4 = new JLabel();
  private JCheckBox chkviviendacons = new JCheckBox();
  private JComboBox cmbusocons = new JComboBox();
  private JLabel TIPOLOGIACONSLBL = new JLabel();
  private JTextField txttipologiacons = new JTextField();
  private JLabel tipoviaparlbl3 = new JLabel();
  private JTextField txtcategoriacons = new JTextField();
  private JLabel categoriaconslbl = new JLabel();
  private JComboBox cmbreparto1cons = new JComboBox();
  private JLabel tipoviaparlbl4 = new JLabel();
  private JComboBox cmbreparto2cons = new JComboBox();
  private JComboBox cmbreparto3cons = new JComboBox();
  private JComboBox cmbtipovalorcons = new JComboBox();
  private JLabel tipovalorconslbl = new JLabel();
  private JPanel jPanel19 = new JPanel();
  private JLabel referenciaconslbl = new JLabel();
  private JTextField txtreferenciacons = new JTextField();
  private JLabel annoexpconslbl = new JLabel();
  private JTextField txtannoexpcons = new JTextField();
  private JTextField txtcargocons = new JTextField();
  private JLabel lblNumero_Cargo = new JLabel();
  private JButton btnBorrarConstruccion = new JButton();
  private JButton btnModificarConstruccion = new JButton();
  private JButton btnNuevaConstruccion = new JButton();
  private JLabel annoexpconslbl1 = new JLabel();
  private JTextField txtFechaAlta = new JTextField();
  private JTextField txtFechaBaja = new JTextField();
  private JLabel annoexpconslbl2 = new JLabel();
  public String ID_Construccion="";
  DefaultListModel lstCons = new DefaultListModel();
  JList padronModel = new JList(lstCons);
  private ArrayList Construcciones = new ArrayList();
  private int  ID_Parcela;
  private boolean alta =false;
  private boolean change=false;
  private int ID_Unidad;

  
  public ConstruccionesPanel()
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

    ConstruccionesPanel geopistaEditarDatos = new ConstruccionesPanel(10);

    frame1.getContentPane().add(geopistaEditarDatos);
    frame1.setSize(675, 725);
    frame1.setVisible(true);
    frame1.setLocation(150, 90);

  }*/
  private void jbInit() throws Exception
  {
    this.setName("Construcciones");
    this.setLayout(null);
    this.setSize(new java.awt.Dimension(676,552));
//Actualizamos el arraylist
    Construcciones.add (cmbreparto1cons);
    Construcciones.add (cmbreparto2cons);
    Construcciones.add (cmbreparto3cons);
    Construcciones.add(txtBloque);
    Construcciones.add(txtEscalera);
    Construcciones.add(txtPlanta);
    Construcciones.add(txtPuerta);
    Construcciones.add(txtdestinocons);
    Construcciones.add(cmbreformacons);
    Construcciones.add(txtannoreforma);
    Construcciones.add(txtSuperficie_Total_Local);
    Construcciones.add(txtSuperficie_Terrazas_Local);
    Construcciones.add(txtSuperficie_Imputable_Local);
    Construcciones.add(txttipologiacons);
    Construcciones.add(cmbusocons);
    Construcciones.add(txtcategoriacons);
    Construcciones.add(cmbtipovalorcons);
    Construcciones.add(txtapreciacioncons);
    Construcciones.add(txtannoexpcons);
    Construcciones.add(txtreferenciacons);
    Construcciones.add(chkIndicador_Local_Interior);
    Construcciones.add(chkviviendacons);

    jPanel7.setLayout(null);
    jPanel7.setBorder(BorderFactory.createTitledBorder("Datos Físicos"));
    lblSuperficie_Imputable_Local.setText("Superficie Imputable:");
    lblSuperficie_Imputable_Local.setBounds(new java.awt.Rectangle(430,51,106,16));
    txtSuperficie_Imputable_Local.setText("");
    txtSuperficie_Imputable_Local.setBounds(new java.awt.Rectangle(542,48,65,20));
    txtSuperficie_Imputable_Local.setSize(new Dimension(65, 20));
    lblSuperficie_Terrazas_Local.setText("Superficie Porches/Terrazas:");
    lblSuperficie_Terrazas_Local.setBounds(new java.awt.Rectangle(179,51,151,16));
    txtSuperficie_Terrazas_Local.setText("");
    txtSuperficie_Terrazas_Local.setBounds(new Rectangle(345, 50, 65, 20));
    txtSuperficie_Terrazas_Local.setPreferredSize(new Dimension(53, 20));
    txtSuperficie_Terrazas_Local.setSize(new Dimension(65, 20));
    txtSuperficie_Total_Local.setText("");
    txtSuperficie_Total_Local.setBounds(new Rectangle(105, 50, 65, 20));
    supsolarlbl1.setText("Superficie Total:");
    supsolarlbl1.setBounds(new java.awt.Rectangle(10,54,88,17));
    jPanel8.setLayout(null);
    jPanel8.setBorder(BorderFactory.createTitledBorder("Domicilio Tributario"));
    lblEscalera.setText("Escalera:");
    lblEscalera.setBounds(new java.awt.Rectangle(100,23,57,16));
    lblBloque.setText("Bloque:");
    lblBloque.setBounds(new java.awt.Rectangle(6,20,43,15));
    txtBloque.setText("");
    txtBloque.setBounds(new Rectangle(55, 20, 35, 20));
    txtEscalera.setText("");
    txtEscalera.setBounds(new Rectangle(165, 20, 40, 20));
    lblPlanta.setText("Planta:");
    lblPlanta.setBounds(new java.awt.Rectangle(220,23,54,15));
    txtPlanta.setText("");
    txtPlanta.setBounds(new Rectangle(280, 20, 35, 20));
    lblPuerta.setText("Puerta:");
    lblPuerta.setBounds(new java.awt.Rectangle(327,22,47,16));
    txtPuerta.setText("");
    txtPuerta.setBounds(new Rectangle(390, 20, 40, 20));
    jPanel18.setLayout(null);
    jPanel18.setBorder(BorderFactory.createTitledBorder("Identificación"));
    cmbreformacons.setBounds(new Rectangle(265, 20, 45, 20));
    reformaconslbl.setText("Tipo Reforma:");
    reformaconslbl.setBounds(new java.awt.Rectangle(164,20,82,17));
    txtannoreforma.setText("");
    txtannoreforma.setBounds(new Rectangle(415, 20, 45, 20));
    lblAnio_Reforma.setText("Año Reforma:");
    lblAnio_Reforma.setBounds(new java.awt.Rectangle(335,21,74,18));
    txtdestinocons.setText("");
    txtdestinocons.setBounds(new Rectangle(100, 20, 40, 20));
    txtdestinocons.setSize(new Dimension(40, 20));
    lblCodigo_Destino_DGC.setText("Código Destino:");
    lblCodigo_Destino_DGC.setBounds(new java.awt.Rectangle(11,23,85,15));
    chkIndicador_Local_Interior.setText("Local Interior");
    chkIndicador_Local_Interior.setBounds(new Rectangle(480, 20, 110, 20));
    jPanel17.setLayout(null);
    jPanel17.setBorder(BorderFactory.createTitledBorder("Datos de Valoración"));
    txtapreciacioncons.setText("");
    txtapreciacioncons.setBounds(new Rectangle(245, 120, 40, 20));
    lblCorrector_Apreciacion_Economica.setText("Apreciación / Depreciación Económica:");
    lblCorrector_Apreciacion_Economica.setBounds(new java.awt.Rectangle(18,120,195,17));
    coef1lbl4.setText("Coeficientes Correctores Conjuntos del Valor del Suelo y de las Construcciones.");
    coef1lbl4.setBounds(new java.awt.Rectangle(5,90,405,16));
    chkviviendacons.setText("Vivienda / Local Interior");
    chkviviendacons.setBounds(new Rectangle(355, 120, 195, 20));
    cmbusocons.setBounds(new java.awt.Rectangle(301,23,170,20));
    TIPOLOGIACONSLBL.setText("Tipología Constructiva:");
    TIPOLOGIACONSLBL.setBounds(new java.awt.Rectangle(8,26,117,18));
    txttipologiacons.setText("");
    txttipologiacons.setBounds(new Rectangle(135, 25, 50, 20));
    tipoviaparlbl3.setText("Uso Predominante:");
    tipoviaparlbl3.setBounds(new java.awt.Rectangle(195,27,96,15));
    txtcategoriacons.setText("");
    txtcategoriacons.setBounds(new java.awt.Rectangle(565,22,25,20));
    categoriaconslbl.setText("Categoría:");
    categoriaconslbl.setBounds(new java.awt.Rectangle(490,25,64,17));
    cmbreparto1cons.setBounds(new Rectangle(125, 60, 115, 20));
    tipoviaparlbl4.setText("Modalidad Reparto:");
    tipoviaparlbl4.setBounds(new java.awt.Rectangle(12,60,104,17));
    cmbreparto2cons.setBounds(new Rectangle(250, 60, 115, 20));
    cmbreparto3cons.setBounds(new Rectangle(375, 60, 115, 20));
    cmbtipovalorcons.setBounds(new Rectangle(560, 60, 50, 20));
    tipovalorconslbl.setText("Tipo Valor:");
    tipovalorconslbl.setBounds(new java.awt.Rectangle(495,61,58,17));
    jPanel19.setLayout(null);
    jPanel19.setBorder(BorderFactory.createTitledBorder("Datos del Movimiento"));
    jPanel19.setBounds(new Rectangle(15, 430, 625, 95));
    referenciaconslbl.setText("Referencia Expediente:");
    referenciaconslbl.setBounds(new java.awt.Rectangle(185,20,130,19));
    txtreferenciacons.setText("");
    txtreferenciacons.setBounds(new java.awt.Rectangle(319,20,110,20));
    txtreferenciacons.setSize(new Dimension(110, 20));
    annoexpconslbl.setText("Año Expediente:");
    annoexpconslbl.setBounds(new java.awt.Rectangle(13,18,95,21));
    txtannoexpcons.setText("");
    txtannoexpcons.setBounds(new Rectangle(110, 20, 40, 20));
    txtannoexpcons.setPreferredSize(new Dimension(53, 20));
    txtannoexpcons.setSize(new Dimension(40, 20));
    txtcargocons.setText("");
    txtcargocons.setBounds(new Rectangle(475, 35, 50, 20));
    lblNumero_Cargo.setText("Nº Cargo al que se imputa:");
    lblNumero_Cargo.setBounds(new java.awt.Rectangle(340,39,130,16));

    btnBorrarConstruccion.setText("Baja");
    btnBorrarConstruccion.setBounds(new Rectangle(155, 35, 78, 20));
    btnModificarConstruccion.setText("Aplicar");
    btnModificarConstruccion.setBounds(new Rectangle(25, 65, 78, 20));
    btnModificarConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnModificarConstruccion_actionPerformed(e);
        }
      });
    btnNuevaConstruccion.setText("Alta");
    btnNuevaConstruccion.setBounds(new Rectangle(155, 10, 78, 20));
    btnNuevaConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnNuevaConstruccion_actionPerformed(e);
        }
      });
    annoexpconslbl1.setText("Fecha Alta:");
    annoexpconslbl1.setBounds(new Rectangle(30, 480, 95, 20));
    txtFechaAlta.setBounds(new Rectangle(130, 480, 100, 20));
    txtFechaBaja.setBounds(new Rectangle(370, 480, 100, 20));
    annoexpconslbl2.setText("Fecha Baja:");
    annoexpconslbl2.setBounds(new Rectangle(270, 480, 95, 20));
    padronModel.setBounds(new Rectangle(5, 5, 100, 55));
      cmbreformacons.addItem("R");
    cmbreformacons.addItem("E");
    cmbreformacons.addItem("O");
    cmbreformacons.addItem("I");
    cmbreformacons.addItem(" ");
    cmbreparto1cons.addItem("T - A todos");
    cmbreparto1cons.addItem("A - A alguno");
    cmbreparto2cons.addItem("L - A locales");
    cmbreparto2cons.addItem("C - A cargos");
    cmbreparto3cons.addItem("1 - Por partes");
    cmbreparto3cons.addItem("2 - Por superficie de los locales");
    cmbreparto3cons.addItem("3 - Por coeficiente de propiedad");
    cmbreparto3cons.addItem("4 - Por coeficiente específicamente determinado");
    cmbtipovalorcons.addItem("0");
    cmbtipovalorcons.addItem("1");
    cmbtipovalorcons.addItem("2");
    cmbtipovalorcons.addItem("3");
    cmbtipovalorcons.addItem("4");
    cmbtipovalorcons.addItem("5");
    cmbtipovalorcons.addItem("6");
    cmbtipovalorcons.addItem("7");
    cmbtipovalorcons.addItem("8");
    cmbtipovalorcons.addItem("9");
    cmbtipovalorcons.addItem("N");
    jPanel19.add(referenciaconslbl, null);
    jPanel19.add(txtreferenciacons, null);
    jPanel19.add(annoexpconslbl, null);
    jPanel19.add(txtannoexpcons, null);
    jPanel17.add(tipovalorconslbl, null);
    jPanel17.add(cmbtipovalorcons, null);
    jPanel17.add(cmbreparto3cons, null);
    jPanel17.add(cmbreparto2cons, null);
    jPanel17.add(tipoviaparlbl4, null);
    jPanel17.add(cmbreparto1cons, null);
    jPanel17.add(categoriaconslbl, null);
    jPanel17.add(txtcategoriacons, null);
    jPanel17.add(txtapreciacioncons, null);
    jPanel17.add(lblCorrector_Apreciacion_Economica, null);
    jPanel17.add(coef1lbl4, null);
    jPanel17.add(chkviviendacons, null);
    jPanel17.add(cmbusocons, null);
    jPanel17.add(TIPOLOGIACONSLBL, null);
    jPanel17.add(txttipologiacons, null);
    jPanel17.add(tipoviaparlbl3, null);
    cmbusocons.addItem("V - Vivienda");
    jPanel18.add(padronModel, null);
    jPanel18.add(btnNuevaConstruccion, null);
    jPanel18.add(btnModificarConstruccion, null);
    jPanel18.add(btnBorrarConstruccion, null);
    jPanel18.add(lblNumero_Cargo, null);
    jPanel18.add(txtcargocons, null);
    jPanel7.add(chkIndicador_Local_Interior, null);
    jPanel7.add(lblCodigo_Destino_DGC, null);
    jPanel7.add(txtdestinocons, null);
    jPanel7.add(lblAnio_Reforma, null);
    jPanel7.add(txtannoreforma, null);
    jPanel7.add(reformaconslbl, null);
    jPanel7.add(cmbreformacons, null);
    jPanel7.add(lblSuperficie_Imputable_Local, null);
    jPanel7.add(txtSuperficie_Imputable_Local, null);
    jPanel7.add(lblSuperficie_Terrazas_Local, null);
    jPanel7.add(txtSuperficie_Terrazas_Local, null);
    jPanel7.add(txtSuperficie_Total_Local, null);
    jPanel7.add(supsolarlbl1, null);
    jPanel7.setBounds(17, 178, 625, 85);
    jPanel17.setBounds(18, 270, 610, 145);
    jPanel18.setBounds(16, 19, 625, 90);
    this.add(annoexpconslbl2, null);
    this.add(txtFechaBaja, null);
    this.add(txtFechaAlta, null);
    this.add(jPanel18, null);
    jPanel8.add(txtPuerta, null);
    jPanel8.add(lblPuerta, null);
    jPanel8.add(txtPlanta, null);
    jPanel8.add(lblPlanta, null);
    jPanel8.add(lblEscalera, null);
    jPanel8.add(lblBloque, null);
    jPanel8.add(txtBloque, null);
    jPanel8.add(txtEscalera, null);
    jPanel8.setBounds(16, 115, 625, 55);
    this.add(jPanel8, null);
    this.add(jPanel7, null);
    this.add(jPanel17, null);
    jPanel19.setBounds(17, 428, 625, 50);
    this.add(jPanel19, null);
    this.add(annoexpconslbl1, null);

   

    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        String cargo =padronModel.getSelectedValue().toString();
      Iterator alIt1 = Construcciones.iterator();
        
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
        CatastroActualizarPostgre Construcc = new CatastroActualizarPostgre();
        ArrayList Datos= Construcc.DatosConstrucciones(cargo, ID_Parcela);
        Iterator alIt = Datos.iterator();
        Iterator itControles = Construcciones.iterator();
       txtcargocons.setText(cargo);
Object obj;
        ID_Construccion=((obj=alIt.next())!=null)?obj.toString():"";
        String Reparto= ((obj=alIt.next())!=null)?obj.toString():"";

        ((JComboBox)itControles.next()).setSelectedItem(Reparto.substring(0,1));
        ((JComboBox)itControles.next()).setSelectedItem(Reparto.substring(1,2));
        ((JComboBox)itControles.next()).setSelectedItem(Reparto.substring(2,3));        
        while (alIt.hasNext()) 
        {
            try
            {
               JComponent comp=(JComponent)itControles.next();
               if (comp instanceof JTextField)((JTextField)comp).setText(alIt.next().toString());
               if (comp instanceof JCheckBox){
                  String check = ((obj=alIt.next())!=null)?obj.toString():"";;
                  if (check == "TRUE"){
                    ((JCheckBox)comp).setSelected(true);}
                  else{
                    ((JCheckBox)comp).setSelected(false);}}
               if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem(alIt.next().toString());
            }
            catch(Exception A)
            {
                A.printStackTrace();
            }
        }}
  };
    btnBorrarConstruccion.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnBorrarConstruccion_actionPerformed(e);
        }
      });
    txtFechaBaja.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtFechaBaja_actionPerformed(e);
        }
      });
    txtFechaAlta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtFechaAlta_actionPerformed(e);
        }
      });
    txtreferenciacons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtreferenciacons_actionPerformed(e);
        }
      });
    txtannoexpcons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtannoexpcons_actionPerformed(e);
        }
      });
    chkviviendacons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkviviendacons_actionPerformed(e);
        }
      });
    txtapreciacioncons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtapreciacioncons_actionPerformed(e);
        }
      });
    cmbtipovalorcons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbtipovalorcons_actionPerformed(e);
        }
      });
    cmbreparto3cons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbreparto3cons_actionPerformed(e);
        }
      });
    cmbreparto2cons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbreparto2cons_actionPerformed(e);
        }
      });
    cmbreparto1cons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbreparto1cons_actionPerformed(e);
        }
      });
    txtcategoriacons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcategoriacons_actionPerformed(e);
        }
      });
    cmbusocons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbusocons_actionPerformed(e);
        }
      });
    txttipologiacons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txttipologiacons_actionPerformed(e);
        }
      });
    txtSuperficie_Imputable_Local.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtSuperficie_Imputable_Local_actionPerformed(e);
        }
      });
    txtSuperficie_Terrazas_Local.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtSuperficie_Terrazas_Local_actionPerformed(e);
        }
      });
    txtSuperficie_Total_Local.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtSuperficie_Total_Local_actionPerformed(e);
        }
      });
    chkIndicador_Local_Interior.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          chkIndicador_Local_Interior_actionPerformed(e);
        }
      });
    txtannoreforma.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtannoreforma_actionPerformed(e);
        }
      });
    cmbreformacons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbreformacons_actionPerformed(e);
        }
      });
    txtdestinocons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtdestinocons_actionPerformed(e);
        }
      });
    txtPuerta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtPuerta_actionPerformed(e);
        }
      });
    txtPlanta.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtPlanta_actionPerformed(e);
        }
      });
    txtEscalera.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtEscalera_actionPerformed(e);
        }
      });
    txtBloque.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtBloque_actionPerformed(e);
        }
      });
    txtcargocons.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          txtcargocons_actionPerformed(e);
        }
      });
      padronModel.addMouseListener(mouseListener);
    padronModel.setBounds(new Rectangle(5, 5, 100, 55));
   
   
  }
  public void enter()
  {
    //Recuperamos el ID_Parcela y el ID_UnidadConstruccion
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());
    //ID_Unidad= Integer.parseInt(Identificadores.get("ID_Unidad").toString());

    //Rellenamos el Listbox
      lstCons.clear();
     try{
        CatastroActualizarPostgre Cons = new CatastroActualizarPostgre();    
        ArrayList Datos =  Cons.Cons(ID_Parcela);
        Iterator alIt = Datos.iterator();
        while (alIt.hasNext()) 
          {
              lstCons.addElement(alIt.next());   
          }
    }catch(Exception e)
          {
           e.printStackTrace();
          }   

  }

private void btnModificarConstruccion_actionPerformed(ActionEvent e)
  {
     //Con esta opción modificamos los datos de la subparcela
    //En la variable ID tenemos el identificador de la subparcela
    //Creamos un ArrayList con los datos recogidos del formulario en el mismo orden que la base de datos
    ArrayList UC= new ArrayList();
    ArrayList UCTipo= new ArrayList();
    ArrayList Checks = new ArrayList();
    Checks.add(chkIndicador_Local_Interior);
    Checks.add(chkviviendacons);
    
    UC.add(ID_Construccion);
    UCTipo.add("1");
    UC.add(txtcargocons.getText());
    UCTipo.add("0");
    UC.add(txtBloque.getText());
    UCTipo.add("1");
    UC.add(txtEscalera.getText());
    UCTipo.add("1");    
    UC.add(txtPlanta.getText());
    UCTipo.add("1");   
    UC.add(txtPuerta.getText());
    UCTipo.add("1");    
    UC.add(txtdestinocons.getText());
    UCTipo.add("1");
    UC.add(cmbreformacons.getSelectedItem());
    UCTipo.add("1");     
    UC.add(txtannoreforma.getText());
    UCTipo.add("0");
    UC.add(txtSuperficie_Total_Local.getText());
    UCTipo.add("0");
    UC.add(txtSuperficie_Terrazas_Local.getText());
    UCTipo.add("0");
    UC.add(txtSuperficie_Imputable_Local.getText());
    UCTipo.add("0");
    UC.add(txttipologiacons.getText());
    UCTipo.add("1");    
    UC.add(cmbusocons.getSelectedItem().toString().substring(0,1));
    UCTipo.add("1");    
    UC.add(txtcategoriacons.getText());
    UCTipo.add("1");
    //Calculamos el reparto
    String Reparto = cmbreparto1cons.getSelectedItem().toString().substring(0,1);
    Reparto = Reparto + cmbreparto2cons.getSelectedItem().toString().substring(0,1);
    Reparto = Reparto + cmbreparto3cons.getSelectedItem().toString().substring(0,1);
    System.out.println(Reparto);
    UC.add (Reparto);
    UCTipo.add("1");   
    UC.add(cmbtipovalorcons.getSelectedItem());
    UCTipo.add("1");   
    UC.add(txtapreciacioncons.getText());
    UCTipo.add("0");    
    if (alta == true) {
       UC.add("A");
       UCTipo.add("1");   
    }else{
       UC.add("M");
       UCTipo.add("1");   
   }
    UC.add(txtannoexpcons.getText());
    UCTipo.add("0");
    UC.add(txtreferenciacons.getText());
    UCTipo.add("1");  
    Iterator CheckIt = Checks.iterator();    
    while (CheckIt.hasNext()) 
    {
        if(((JCheckBox)CheckIt.next()).isSelected()==true){
        UC.add("TRUE");    
        }else{UC.add("FALSE");}       
        UCTipo.add("1");
     }
    //Actualizamos la información almacenada
     CatastroActualizarPostgre ActualizarConstruccion = new CatastroActualizarPostgre();
   if (alta==false){
     String Result = ActualizarConstruccion.ActualizarConstruccion(ID_Construccion, UC, UCTipo);
       System.out.println(Result);}
    else{
     String Result = ActualizarConstruccion.AltaConstruccion(ID_Parcela, txtcargocons.getText(), ID_Unidad, UC, UCTipo);
       System.out.println(Result);
       alta=false;}
    }

  private void btnNuevaConstruccion_actionPerformed(ActionEvent e)
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

  private void txtcargocons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtBloque_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtEscalera_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtPlanta_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtPuerta_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtdestinocons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbreformacons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtannoreforma_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkIndicador_Local_Interior_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtSuperficie_Total_Local_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtSuperficie_Terrazas_Local_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtSuperficie_Imputable_Local_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txttipologiacons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbusocons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtcategoriacons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbreparto1cons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbreparto2cons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbreparto3cons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void cmbtipovalorcons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtapreciacioncons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void chkviviendacons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtannoexpcons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtreferenciacons_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtFechaAlta_actionPerformed(ActionEvent e)
  { change=true;
  }

  private void txtFechaBaja_actionPerformed(ActionEvent e)
  { change=true;
  }

/*  public void setID(int ID)
  {ID_Parcela=ID;
  }*/

 public void exit()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
     Identificadores.put ("ID_Construccion", ID_Construccion);
    
  }

  private void btnBorrarConstruccion_actionPerformed(ActionEvent e)
  {
   CatastroActualizarPostgre ActualizarConstruccion = new CatastroActualizarPostgre();
     String Result = ActualizarConstruccion.BajaConstruccion (ID_Construccion);
  }

 
}