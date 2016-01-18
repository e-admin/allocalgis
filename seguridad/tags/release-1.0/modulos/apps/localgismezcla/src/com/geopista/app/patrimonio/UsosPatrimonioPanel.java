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
import javax.swing.JComboBox;
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


//public class UsosPatrimonioPanel extends JPanel {
public class UsosPatrimonioPanel extends  JPanel implements FeatureExtendedPanel {

  private JTextField txtSuperficie = new JTextField();
  private JLabel jLabel30 = new JLabel();
  private JButton quitarObservacionBtn1 = new JButton();
  private JButton anadirObservacionBtn = new JButton();
  private JLabel jLabel74 = new JLabel();
  private DefaultTableModel model = new DefaultTableModel();
  private JPanel jPanel4 = new JPanel();
  private JTable tablaCampos = new JTable(model);
  private boolean ALLOW_ROW_SELECTION = true;
  private int ID_Bien=2;
  private int ID_Uso;
  private boolean alta=false;
  private JButton btnAceptar = new JButton();
  private int filas=0;
  private JComboBox cmbUso = new JComboBox();

  public UsosPatrimonioPanel()
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
    UsosPatrimonioPanel UsosPatrimonioPanel = new UsosPatrimonioPanel();
    frame1.getContentPane().add(UsosPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName("Usos");    
    this.setSize(new Dimension(688, 477));
    this.setBounds(new Rectangle(5, 10, 605, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    jLabel30.setText("Superficie:");
    quitarObservacionBtn1.setText("-");
    quitarObservacionBtn1.setHorizontalTextPosition(SwingConstants.CENTER);
    quitarObservacionBtn1.setVerticalTextPosition(SwingConstants.BOTTOM);
    anadirObservacionBtn.setText("+");
    anadirObservacionBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    anadirObservacionBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    jLabel74.setText("Uso Funcional:");
    cmbUso.setBounds(100, 295, 250, 20);
    btnAceptar.setBounds(535, 345, 45, 20);
    txtSuperficie.setBounds(420, 295, 80, 25);
    jLabel30.setBounds(365, 290, 90, 35);
    quitarObservacionBtn1.setBounds(535, 315, 45, 20);
    anadirObservacionBtn.setBounds(535, 290, 45, 20);
    jLabel74.setBounds(15, 290, 90, 35);
    cmbUso.addItem("Residencial");
    cmbUso.addItem("Industrial");
    cmbUso.addItem("Terciario");
           
    this.add(cmbUso, null);
    this.add(btnAceptar, null);
    this.add(txtSuperficie, null);
    this.add(jLabel30, null);
    this.add(quitarObservacionBtn1, null);
    this.add(anadirObservacionBtn, null);
    this.add(jLabel74, null);



    JTableHeader header = tablaCampos.getTableHeader();
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
    model.addColumn("Uso Funcional");
    model.addColumn("Superficie");
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
                        int selectedRow = lsm.getMinSelectionIndex();
                        Object[] prueba = new Object[3];
                        prueba[0]= model.getValueAt(selectedRow,0);
                        prueba[1]= model.getValueAt(selectedRow,1);
                        prueba[2]= model.getValueAt(selectedRow,2);
                        ID_Uso= Integer.parseInt(prueba[0].toString());
                       cmbUso.setSelectedItem(prueba[1].toString());   
                       txtSuperficie.setText(prueba[2].toString());
                    }
                }
            });

/*            
//ENTER
 PatrimonioPostgre UsoPostgre = new PatrimonioPostgre();
     ArrayList Datos= UsoPostgre.DatosUsos(ID_Bien);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();

     while (alIt.hasNext()) 
      {
        filas = tablaCampos.getRowCount();
        while (alIt.hasNext()) 
        {
           Object[] cadena = new Object[3];
           cadena[0]= alIt.next().toString();
           cadena[1]= alIt.next().toString();
           cadena[2]= alIt.next().toString();
           model.insertRow(filas, cadena);

        }
      }

  //ENTER
  */
  


  }
  }
  public void enter()
   {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Bien= Integer.parseInt(Identificadores.get("IdBien").toString());
    //Mostramos los datos para la parcela
     PatrimonioPostgre UsoPostgre = new PatrimonioPostgre();
     ArrayList Datos= UsoPostgre.DatosUsos(ID_Bien);
    int rows=   model.getRowCount();
    System.out.println("Filas " + rows);
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
           Object[] cadena = new Object[3];
           cadena[0]= alIt.next().toString();
           cadena[1]= alIt.next().toString();
           cadena[2]= alIt.next().toString();
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
    cmbUso.setSelectedIndex(0);
    txtSuperficie.setText("");
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {
    String Uso=cmbUso.getSelectedItem().toString();
    int Superficie=Integer.parseInt(txtSuperficie.getText().toString());

    //Actualizamos la información almacenada
    PatrimonioPostgre UsoPostgre = new PatrimonioPostgre();
    if (alta==false){
     String Result = UsoPostgre.ActualizarUso(ID_Uso, Uso, Superficie);
     System.out.println(Result);
    }else{
     String Result = UsoPostgre.AltaUso(ID_Bien,Uso, Superficie);
     System.out.println(Result);
     if (Result.equals("Correcto")){
     alta=false;}
    }
  }

}