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

//public class SegurosPatrimonioPanel extends JPanel {
public class SegurosPatrimonioPanel extends  JPanel implements FeatureExtendedPanel {

  private JTextField txtPrima = new JTextField();
  private JLabel jLabel30 = new JLabel();
  private JButton quitarObservacionBtn1 = new JButton();
  private JButton anadirObservacionBtn = new JButton();
  private JTextField txtCompannia = new JTextField();
  private JLabel jLabel74 = new JLabel();
  private DefaultTableModel model = new DefaultTableModel();
  private JPanel jPanel4 = new JPanel();
  private JTable tablaCampos = new JTable(model);
  private boolean ALLOW_ROW_SELECTION = true;
  private int ID_Bien=2;
  private int ID_Seguro;
  private boolean alta=false;
  private JButton btnAceptar = new JButton();
  private int filas=0;
  private JTextField txtPoliza = new JTextField();
  private JLabel jLabel31 = new JLabel();
  private  int selectedRow =0;
  private JTextField txtDescripcion = new JTextField();
  private JLabel jLabel75 = new JLabel();
  private JTextField txtFechaFin = new JTextField();
  private JLabel jLabel32 = new JLabel();
  private JTextField txtFechaInicio = new JTextField();
  private JLabel jLabel33 = new JLabel();
  public SegurosPatrimonioPanel()
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
/*  public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    SegurosPatrimonioPanel SegurosPatrimonioPanel = new SegurosPatrimonioPanel();
    frame1.getContentPane().add(SegurosPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }*/

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName("Seguros");    
    this.setSize(new Dimension(688, 477));
    this.setBounds(new Rectangle(5, 10, 605, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    jLabel30.setText("Prima:");
    quitarObservacionBtn1.setText("-");
    quitarObservacionBtn1.setHorizontalTextPosition(SwingConstants.CENTER);
    quitarObservacionBtn1.setVerticalTextPosition(SwingConstants.BOTTOM);
    anadirObservacionBtn.setText("+");
    anadirObservacionBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    anadirObservacionBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    jLabel74.setText("Reforma:");

    jLabel33.setBounds(15, 345, 90, 35);
    txtFechaInicio.setBounds(115, 350, 130, 25);
    jLabel32.setBounds(260, 345, 90, 35);
    txtFechaFin.setBounds(360, 350, 130, 25);
    jLabel75.setBounds(15, 285, 90, 35);
    txtDescripcion.setBounds(115, 285, 410, 30);
    jLabel31.setBounds(260, 315, 90, 35);
    txtPoliza.setBounds(360, 320, 130, 25);
    btnAceptar.setBounds(535, 345, 45, 20);
    txtPrima.setBounds(115, 320, 130, 25);
    jLabel30.setBounds(15, 315, 90, 35);
    quitarObservacionBtn1.setBounds(535, 315, 45, 20);
    anadirObservacionBtn.setBounds(535, 290, 45, 20);
    txtCompannia.setBounds(115, 250, 410, 30);
    jLabel74.setBounds(15, 250, 90, 35);

    this.add(jLabel33, null);
    this.add(txtFechaInicio, null);
    this.add(jLabel32, null);
    this.add(txtFechaFin, null);
    this.add(jLabel75, null);
    this.add(txtDescripcion, null);
    this.add(jLabel31, null);
    this.add(txtPoliza, null);
    this.add(btnAceptar, null);
    this.add(txtPrima, null);
    this.add(jLabel30, null);
    this.add(quitarObservacionBtn1, null);
    this.add(anadirObservacionBtn, null);
    this.add(txtCompannia, null);
    this.add(jLabel74, null);



    JTableHeader header = tablaCampos.getTableHeader();
    jLabel33.setText("Fecha Inicio:");
    jLabel32.setText("Fecha Fin:");
    jLabel75.setText("Descripción::");
    
    quitarObservacionBtn1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          quitarObservacionBtn1_actionPerformed(e);
        }
      });
    jLabel31.setText("Poliza:");
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
    model.addColumn("Compañía");
    model.addColumn("Descripción");
    model.addColumn("Póliza");
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

                 ID_Seguro=Integer.parseInt(prueba[0].toString());
                 System.out.println(ID_Seguro);
                 PatrimonioPostgre Seguro = new PatrimonioPostgre();
                ArrayList Seguros= Seguro.RecuperarDatosSeguro(ID_Seguro);
                if (Seguros==null)return;
                Iterator alIt = Seguros.iterator();
                while (alIt.hasNext()) 
                  {
                      txtCompannia.setText(alIt.next().toString());
                      txtDescripcion.setText(alIt.next().toString());          
                      txtPrima.setText(alIt.next().toString());                      
                      txtPoliza.setText(alIt.next().toString());                      
                      txtFechaInicio.setText(alIt.next().toString());
                      txtFechaFin.setText(alIt.next().toString());   
                  }
                }}});

/*//ENTER
     PatrimonioPostgre Seguro = new PatrimonioPostgre();
     ArrayList Datos= Seguro.DatosSeguro(ID_Bien);
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
//ENTER*/

  }
  }
  public void enter()
   {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Bien= Integer.parseInt(Identificadores.get("IdBien").toString());
    //Mostramos los datos para la parcela
     PatrimonioPostgre Seguro = new PatrimonioPostgre();
     ArrayList Datos= Seguro.DatosSeguro(ID_Bien);

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
    txtCompannia.setText("");
    txtDescripcion.setText("");
    txtPrima.setText("");
    txtPoliza.setText("");
    txtFechaInicio.setText("");
    txtFechaFin.setText("");    
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {

   ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
    Valor.add(txtCompannia.getText());
    Tipo.add("1");
    Valor.add(txtDescripcion.getText());
    Tipo.add("1");
    Valor.add(txtPrima.getText());
    Tipo.add("0");
    Valor.add(txtPoliza.getText());
    Tipo.add("0");
    Valor.add(txtFechaInicio.getText());
    Tipo.add("1");
    Valor.add(txtFechaFin.getText());
    Tipo.add("1");

    //Actualizamos la información almacenada
    PatrimonioPostgre Seguro = new PatrimonioPostgre();
    if (alta==false){
     String Result = Seguro.ActualizarSeguro(ID_Seguro, Valor, Tipo);
     System.out.println(Result);
      if (Result.equals("Correcto")){
     
         model.setValueAt(String.valueOf(ID_Seguro),selectedRow,0);
         model.setValueAt(txtCompannia.getText(),selectedRow,1);
         model.setValueAt(txtDescripcion.getText(),selectedRow,2);
         model.setValueAt(txtPoliza.getText(),selectedRow,3);}
    }else{
     ID_Seguro = Seguro.AltaSeguro  (ID_Bien, Valor, Tipo);
     alta=false;
      Object[] cadena2 = new Object[4];
     cadena2[0]= String.valueOf(ID_Seguro);
     cadena2[1]= txtCompannia.getText();
     cadena2[2]= txtDescripcion.getText();
     cadena2[3]=txtPoliza.getText();
     model.insertRow(filas, cadena2);
    }  
 }
   

  private void quitarObservacionBtn1_actionPerformed(ActionEvent e)
  {
  PatrimonioPostgre Seguro = new PatrimonioPostgre();
  String Result = Seguro.BorrarSeguro( ID_Bien, ID_Seguro);
  System.out.println(Result);
  model.removeRow(selectedRow);
  txtCompannia.setText(" ");
  txtDescripcion.setText(" ");
  txtPrima.setText(" ");
  txtPoliza.setText(" ");
  txtFechaInicio.setText("");
  txtFechaFin.setText("");
  }

 


}