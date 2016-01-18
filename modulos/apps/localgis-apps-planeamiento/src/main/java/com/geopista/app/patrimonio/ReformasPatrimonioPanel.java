/**
 * ReformasPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class ReformasPatrimonioPanel extends JPanel {
public class ReformasPatrimonioPanel extends  JPanel implements FeatureExtendedPanel {

  private JTextField txtFecha = new JTextField();
  private JLabel jLabel30 = new JLabel();
  private JButton quitarObservacionBtn1 = new JButton();
  private JButton anadirObservacionBtn = new JButton();
  private JTextField txtReforma = new JTextField();
  private JLabel jLabel74 = new JLabel();
  private DefaultTableModel model = new DefaultTableModel();
  private JPanel jPanel4 = new JPanel();
  private JTable tablaCampos = new JTable(model);
  private boolean ALLOW_ROW_SELECTION = true;
  private int ID_Bien=2;
  private int ID_Mejora;
  private boolean alta=false;
  private JButton btnAceptar = new JButton();
  private int filas=0;
  private JTextField txtImporte = new JTextField();
  private JLabel jLabel31 = new JLabel();
  private  int selectedRow =0;
  public ReformasPatrimonioPanel()
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
    ReformasPatrimonioPanel ReformasPatrimonioPanel = new ReformasPatrimonioPanel();
    frame1.getContentPane().add(ReformasPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName("Reformas");    
    this.setSize(new Dimension(710, 477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    jLabel30.setText("Fecha");
    quitarObservacionBtn1.setText("-");
    quitarObservacionBtn1.setHorizontalTextPosition(SwingConstants.CENTER);
    quitarObservacionBtn1.setVerticalTextPosition(SwingConstants.BOTTOM);
    anadirObservacionBtn.setText("+");
    anadirObservacionBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    anadirObservacionBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    jLabel74.setText("Reforma");

    jLabel31.setBounds(260, 345, 90, 35);
    txtImporte.setBounds(360, 345, 130, 25);
    txtReforma.setBounds(115, 290, 400, 35);
    btnAceptar.setBounds(535, 345, 45, 20);
    txtFecha.setBounds(115, 345, 130, 25);
    jLabel30.setBounds(15, 345, 90, 35);
    quitarObservacionBtn1.setBounds(535, 315, 45, 20);
    anadirObservacionBtn.setBounds(535, 290, 45, 20);
    anadirObservacionBtn.setBounds(115, 290, 410, 50);
    jLabel74.setBounds(15, 290, 90, 35);
    
    this.add(jLabel31, null);
    this.add(txtImporte, null);
    this.add(btnAceptar, null);
    this.add(txtFecha, null);
    this.add(jLabel30, null);
    this.add(quitarObservacionBtn1, null);
    this.add(anadirObservacionBtn, null);
    this.add(txtReforma, null);
    this.add(jLabel74, null);



    JTableHeader header = tablaCampos.getTableHeader();
    quitarObservacionBtn1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          quitarObservacionBtn1_actionPerformed(e);
        }
      });
    jLabel31.setText("Importe:");
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });
    btnAceptar.setVerticalTextPosition(SwingConstants.BOTTOM);
    btnAceptar.setHorizontalTextPosition(SwingConstants.CENTER);
    btnAceptar.setText("OK");
    anadirObservacionBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          anadirObservacionBtn_actionPerformed(e);
        }
      });
    jPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel4.setLayout(null);
    jPanel4.setLayout(new BorderLayout());
    jPanel4.setBounds(new Rectangle(20, 50, 515, 180));
    tablaCampos.setBounds(new Rectangle(10, 5, 485, 160));
     

    model.addColumn("Identificador");
    model.addColumn("Observación");
    model.addColumn("Fecha");
    model.addColumn("Importe");
    jPanel4.add(tablaCampos, null);
    jPanel4.add(header, BorderLayout.NORTH);
    jPanel4.add(tablaCampos, BorderLayout.CENTER);
    this.add(jPanel4, null);

    tablaCampos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    if (ALLOW_ROW_SELECTION) { // true by default
          ListSelectionModel rowSM = tablaCampos.getSelectionModel();
          rowSM.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
              ListSelectionModel lsm = (ListSelectionModel)e.getSource();
              if (lsm.isSelectionEmpty()) {
                 System.out.println("No rows are selected.");
              } else {
                 selectedRow = lsm.getMinSelectionIndex();
                  Object[] prueba = new Object[4];
                  prueba[0]= model.getValueAt(selectedRow,0);
                  prueba[1]= model.getValueAt(selectedRow,1);
                  prueba[2]= model.getValueAt(selectedRow,2);
                  prueba[3]= model.getValueAt(selectedRow,3);

                 ID_Mejora=Integer.parseInt(prueba[0].toString());
                 System.out.println(ID_Mejora);
                 txtReforma.setText(prueba[1].toString());
                 txtFecha.setText(prueba[2].toString());                       
                 txtImporte.setText(prueba[3].toString());   
              }
            }
            });




  }
  }
  public void enter()
   {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Bien= Integer.parseInt(Identificadores.get("IdBien").toString());
    //Mostramos los datos para la parcela
     PatrimonioPostgre Mejora = new PatrimonioPostgre();
     ArrayList Datos= Mejora.DatosMejoras (ID_Bien);

    int rows=   model.getRowCount();
      if (rows !=0)
    {
      while (rows!=0)
      {
          int borrar= rows-1;
          model.removeRow(borrar);
          rows=borrar;
      }
    } 
    
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();

     while (alIt.hasNext()) 
      {
        filas = tablaCampos.getRowCount();
        while (alIt.hasNext()) 
        {
           Object[] cadena = new Object[4];
           cadena[0]= alIt.next().toString();
           cadena[1]= alIt.next().toString();
           cadena[2]= alIt.next().toString();
           cadena[3]= alIt.next().toString();
           model.insertRow(filas, cadena);

        }
      }
  }


  public void exit()
  {
  }

  private void anadirObservacionBtn_actionPerformed(ActionEvent e)
  {
    alta=true;
    txtReforma.setText("");
    txtFecha.setText("");
    txtImporte.setText("");
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {

    String Reforma=txtReforma.getText();
    String Fecha=txtFecha.getText();
    int Importe = Integer.parseInt(txtImporte.getText());
    //Actualizamos la información almacenada
    PatrimonioPostgre Mejora = new PatrimonioPostgre();
    if (alta==false){
     String Result = Mejora.ActualizarMejora(ID_Mejora, Reforma, Fecha, Importe);
     if (Result.equals("Correcto")){
         model.setValueAt(String.valueOf(ID_Mejora),selectedRow,0);
         model.setValueAt(Reforma,selectedRow,1);
         model.setValueAt(Fecha,selectedRow,2);
         model.setValueAt(String.valueOf(Importe),selectedRow,3);}

    }else{
      ID_Mejora = Mejora.altaMejora(ID_Bien,  Reforma, Fecha, Importe);
      alta=false;
     //Actualizamos la columna seleccionada
       Object[] cadena2 = new Object[4];
           cadena2[0]= String.valueOf(ID_Mejora);
           cadena2[1]= Reforma;
           cadena2[2]= Fecha;
           cadena2[3]= String.valueOf(Importe);
           model.insertRow(filas, cadena2);


     }
    }

  private void quitarObservacionBtn1_actionPerformed(ActionEvent e)
  {
  PatrimonioPostgre Mejora = new PatrimonioPostgre();
  String Result = Mejora.BorrarMejora( ID_Bien, ID_Mejora);
  System.out.println(Result);
  model.removeRow(selectedRow);
  txtReforma.setText(" ");
  txtFecha.setText(" ");
  txtImporte.setText(" ");
  }
  


}