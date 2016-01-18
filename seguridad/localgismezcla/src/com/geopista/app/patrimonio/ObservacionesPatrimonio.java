package com.geopista.app.patrimonio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.JTextField;
import javax.swing.JTextArea;
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

//public class ObservacionesPatrimonio extends JPanel {
public class ObservacionesPatrimonio extends  JPanel implements FeatureExtendedPanel {

  private JTextField txtFecha = new JTextField();
  private JLabel jLabel30 = new JLabel();
  private JButton quitarObservacionBtn1 = new JButton();
  private JButton anadirObservacionBtn = new JButton();
  private JTextArea txtObservaciones = new JTextArea(6, 42);
  private JLabel jLabel74 = new JLabel();
  private DefaultTableModel model = new DefaultTableModel();
  private JPanel jPanel4 = new JPanel();
  private JTable tablaCampos = new JTable(model);
  private boolean ALLOW_ROW_SELECTION = true;
  private int ID_Bien=2;
  private int ID_Observacion;
  private boolean alta=false;
  private JButton btnAceptar = new JButton();
  private int filas=0;
  private int selectedRow=-1;
  private int ID_Inmueble;
  
  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  
  public ObservacionesPatrimonio()
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
    ObservacionesPatrimonio ObservacionesPatrimonio = new ObservacionesPatrimonio();
    frame1.getContentPane().add(ObservacionesPatrimonio);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.observaciones.titulo"));
    this.setSize(new Dimension(710, 477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    jLabel30.setText(app.getI18nString("patrimonio.observaciones.fecha")+" (aaaa-mm-dd)");
    quitarObservacionBtn1.setText(app.getI18nString("patrimonio.observaciones.eliminar"));
    quitarObservacionBtn1.setHorizontalTextPosition(SwingConstants.CENTER);
    quitarObservacionBtn1.setVerticalTextPosition(SwingConstants.TOP);
    anadirObservacionBtn.setText(app.getI18nString("patrimonio.observaciones.anadir"));
    anadirObservacionBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    anadirObservacionBtn.setVerticalTextPosition(SwingConstants.TOP);
    jLabel74.setText(app.getI18nString("patrimonio.observaciones.observaciones"));
    btnAceptar.setBounds(440, 345, 75, 25);
    txtFecha.setBounds(125, 345, 130, 25);
    jLabel30.setBounds(15, 345, 110, 35);
    quitarObservacionBtn1.setBounds(440, 315, 75, 25);
    anadirObservacionBtn.setBounds(440, 285, 75, 25);
    txtObservaciones.setBounds(125, 230, 300, 110);
    //txtObservaciones.setMaximumSize(new Dimension (230, 110));
    txtObservaciones.setFont(new Font("Serif", Font.PLAIN, 12));
    txtObservaciones.setBorder(BorderFactory.createLoweredBevelBorder());
    txtObservaciones.setLineWrap(true);
    txtObservaciones.setWrapStyleWord(true);
    
    /*JScrollPane areaScrollPane = new JScrollPane(txtObservaciones);
    areaScrollPane.setVerticalScrollBarPolicy(
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(230, 110));
    areaScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
    areaScrollPane.setBounds(115, 230, 300, 110);
    */
    
    jLabel74.setBounds(15, 260, 90, 35);
    
    this.add(btnAceptar, null);
    this.add(txtFecha, null);
    this.add(jLabel30, null);
    this.add(quitarObservacionBtn1, null);
    this.add(anadirObservacionBtn, null);
    this.add(txtObservaciones, null);
    //this.add(areaScrollPane, null);
    this.add(jLabel74, null);

    JTableHeader header = tablaCampos.getTableHeader();
    quitarObservacionBtn1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          quitarObservacionBtn1_actionPerformed(e);
        }
      });
    btnAceptar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAceptar_actionPerformed(e);
        }
      });
    btnAceptar.setVerticalTextPosition(SwingConstants.TOP);
    btnAceptar.setHorizontalTextPosition(SwingConstants.CENTER);
    btnAceptar.setText(app.getI18nString("patrimonio.observaciones.ok"));
    anadirObservacionBtn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          anadirObservacionBtn_actionPerformed(e);
        }
      });
    jPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel4.setLayout(null);
    jPanel4.setLayout(null);
    jPanel4.setBounds(new Rectangle(20, 50, 490, 160));
    tablaCampos.setBounds(new Rectangle(10, 5, 465, 145));
    


    model.addColumn("Identificador");
    model.addColumn("Observación");
    model.addColumn("Fecha");
    jPanel4.add(tablaCampos, null);
    jPanel4.add(header, BorderLayout.NORTH);
    jPanel4.add(tablaCampos, BorderLayout.CENTER);
    this.add(jPanel4, null);

    /*
    //Fija el tamaño de las columnas de la tabla
    TableColumn col = tablaCampos.getColumnModel().getColumn(0);
    col.setPreferredWidth(20);
    col = tablaCampos.getColumnModel().getColumn(1);
    col.setPreferredWidth(50);
    col = tablaCampos.getColumnModel().getColumn(2);
    col.setPreferredWidth(30);
    */
    
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
                        Object[] observacion = new Object[3];
                        observacion[0]= model.getValueAt(selectedRow,0);
                        observacion[1]= model.getValueAt(selectedRow,1);
                        observacion[2]= model.getValueAt(selectedRow,2);
                        ID_Observacion=Integer.parseInt(observacion[0].toString());
//                      txtID.setText(prueba[0].toString());
                        txtObservaciones.setText(observacion[1].toString());
                        txtFecha.setText(observacion[2].toString());                       
                    }
                }
            });

     }
     
     
  }
  public void enter()
   {
    AppContext app =(AppContext) AppContext.getApplicationContext();
  	Blackboard Identificadores = app.getBlackboard();
  	String idst = Identificadores.get("IdInmueble").toString();
   	ID_Inmueble = Integer.parseInt(idst);
  	 
  	txtObservaciones.setText("");
    txtFecha.setText("");      
  	
  	//Mostramos los datos para el inmueble    
    PatrimonioPostgre Observa = new PatrimonioPostgre();
    ArrayList Datos= Observa.DatosObservacion (ID_Inmueble);
    int rows = model.getRowCount();
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
    //txtID.setText("");
    txtObservaciones.setText("");
    txtFecha.setText("");
  }

  private void btnAceptar_actionPerformed(ActionEvent e)
  {
    String Observacion=txtObservaciones.getText();
    
    if(Observacion.length()>255)
        Observacion=Observacion.substring(0, 255);
    
    String Fecha=txtFecha.getText();
    //String ID= txtID.getText();
    //Actualizamos la información almacenada
    PatrimonioPostgre Observa = new PatrimonioPostgre();
    if (alta==false){
     String Result = Observa.ActualizarObservacion(ID_Observacion, Observacion, Fecha);
     //System.out.println(Result);
     if (Result.equals("Correcto")){
         model.setValueAt(String.valueOf(ID_Observacion),selectedRow,0);
         model.setValueAt(Observacion,selectedRow,1);
         model.setValueAt(Fecha,selectedRow,2);}
    }else{
     ID_Observacion = Observa.altaObservacion(ID_Inmueble, Observacion, Fecha);
     //System.out.println(ID_Observacion);
     alta=false;
     //Actualizamos la columna seleccionada
     if (ID_Observacion != 0 ){
         Object[] cadena2 = new Object[3];
         cadena2[0]= String.valueOf(ID_Observacion);
         cadena2[1]= Observacion;
         cadena2[2]= Fecha;
         model.insertRow(filas, cadena2);
     	}

     }
    }

  private void quitarObservacionBtn1_actionPerformed(ActionEvent e)
  {
  PatrimonioPostgre Observa = new PatrimonioPostgre();
  String Result = Observa.BorrarObservacion(ID_Inmueble, ID_Observacion);
  //System.out.println(Result);
  model.removeRow(selectedRow);
  txtObservaciones.setText(" ");
  txtFecha.setText(" ");
  }
  

}