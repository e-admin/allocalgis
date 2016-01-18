package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class ValoracionesPatrimonioPanel extends JPanel {
public class ValoracionesPatrimonioPanel extends  JPanel implements FeatureExtendedPanel {

  private JLabel jLabel413 = new JLabel();
  private JLabel jLabel412 = new JLabel();
  private JLabel jLabel411 = new JLabel();
  private JLabel jLabel410 = new JLabel();
  private JLabel jLabel49 = new JLabel();
  private JLabel jLabel48 = new JLabel();
  private JLabel jLabel47 = new JLabel();
  private JLabel jLabel46 = new JLabel();
  private JLabel jLabel45 = new JLabel();
  private JLabel jLabel44 = new JLabel();
  private JLabel jLabel43 = new JLabel();
  private JLabel jLabel42 = new JLabel();
  private JLabel jLabel41 = new JLabel();
  private JLabel jLabel40 = new JLabel();
  private JTextField txtValorActual = new JTextField();
  private JTextField txtValorAdquisicion = new JTextField();
  private JTextField txtConsValorAdq = new JTextField();
  private JTextField txtConsValorCatastral = new JTextField();
  private JTextField txtConsValorActual = new JTextField();
  private JTextField txtConsSupReal = new JTextField();
  private JTextField txtConsSupCatastral = new JTextField();
  private JTextField txtConsSupRegistral = new JTextField();
  private JTextField txtSueloSupReal = new JTextField();
  private JTextField txtSueloValorActual = new JTextField();
  private JTextField txtSueloValorCatastral = new JTextField();
  private JTextField txtSueloSupCatastral = new JTextField();
  private JTextField txtSueloValorAdq = new JTextField();
  private JTextField txtSueloSupRegistral = new JTextField();
  private JLabel lblValorAdquisicion = new JLabel();
  private JLabel lblValorActual = new JLabel();
  private JSeparator jSeparator7 = new JSeparator();
  private JLabel lblInmueble = new JLabel();
  private JLabel lblValorAdquisicion2 = new JLabel();
  private JLabel lblValorCatastral = new JLabel();
  private JLabel lblValorActual2 = new JLabel();
  private JLabel lblSuperficieReal = new JLabel();
  private JLabel lblSuperficieRegistral = new JLabel();
  private JLabel lblSuperficieCatastral = new JLabel();
  private JSeparator jSeparator6 = new JSeparator();
  private JLabel lblConstruccion = new JLabel();
  private JLabel lblValorActual3 = new JLabel();
  private JLabel lblValorCatastral2 = new JLabel();
  private JLabel lblValorAdquisicion3 = new JLabel();
  private JLabel lblSuperficieReal2 = new JLabel();
  private JLabel lblSuperficieCatastral2 = new JLabel();
  private JLabel lblSuperficieRegistral2 = new JLabel();
  private JLabel lblParcela = new JLabel();
  private JSeparator jSeparator5 = new JSeparator();
  private int ID_Bien=2;
  private ArrayList Valoraciones= new ArrayList();
  private JPanel jPanel1 = new JPanel();
  private JButton btnAplicar = new JButton();
  private int ID_Inmueble;
  
  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  public ValoracionesPatrimonioPanel()
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
    ValoracionesPatrimonioPanel ValoracionesPatrimonioPanel = new ValoracionesPatrimonioPanel();
    frame1.getContentPane().add(ValoracionesPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.valoraciones.titulo"));        
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setLayout(null);
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    Valoraciones.add(txtSueloSupRegistral);
    Valoraciones.add(txtSueloSupCatastral);
    Valoraciones.add(txtSueloSupReal);
    Valoraciones.add(txtSueloValorAdq);
    Valoraciones.add(txtSueloValorCatastral);
    Valoraciones.add(txtSueloValorActual);
    Valoraciones.add(txtConsSupRegistral);
    Valoraciones.add(txtConsSupCatastral);
    Valoraciones.add(txtConsSupReal);
    Valoraciones.add(txtConsValorAdq);
    Valoraciones.add(txtConsValorCatastral);
    Valoraciones.add(txtConsValorActual);
    Valoraciones.add(txtValorAdquisicion);
    Valoraciones.add(txtValorActual);


    
    jLabel413.setText("m2");
    jLabel413.setFont(new Font("Dialog", 1, 11));
    jLabel412.setText("m2");
    jLabel412.setFont(new Font("Dialog", 1, 11));
    jLabel411.setText("m2");
    jLabel411.setFont(new Font("Dialog", 1, 11));
    jLabel410.setText("m2");
    jLabel410.setFont(new Font("Dialog", 1, 11));
    jLabel49.setText("m2");
    jLabel49.setFont(new Font("Dialog", 1, 11));
    jLabel48.setText("m2");
    jLabel48.setFont(new Font("Dialog", 1, 11));
    jLabel47.setText("€");
    jLabel47.setFont(new Font("Dialog", 1, 11));
    jLabel46.setText("€");
    jLabel46.setFont(new Font("Dialog", 1, 11));
    jLabel45.setText("€");
    jLabel45.setFont(new Font("Dialog", 1, 11));
    jLabel44.setText("€");
    jLabel44.setFont(new Font("Dialog", 1, 11));
    jLabel43.setText("€");
    jLabel43.setFont(new Font("Dialog", 1, 11));
    jLabel42.setText("€");
    jLabel42.setFont(new Font("Dialog", 1, 11));
    jLabel41.setText("€");
    jLabel41.setFont(new Font("Dialog", 1, 11));
    jLabel40.setText("m2");
    jLabel40.setFont(new Font("Dialog", 1, 11));
    
    lblValorAdquisicion.setText(app.getI18nString("patrimonio.valoraciones.valoradquisicion"));
    lblValorActual.setText(app.getI18nString("patrimonio.valoraciones.valoractual"));
    lblInmueble.setText(app.getI18nString("patrimonio.valoraciones.inmueble"));
    lblInmueble.setFont(new Font("Dialog", 1, 11));
    lblValorAdquisicion2.setText(app.getI18nString("patrimonio.valoraciones.valoradquisicion"));
    lblValorCatastral.setText(app.getI18nString("patrimonio.valoraciones.valorcatastral"));
    lblValorActual2.setText(app.getI18nString("patrimonio.valoraciones.valoractual"));
    lblSuperficieReal.setText(app.getI18nString("patrimonio.valoraciones.superficiereal"));
    lblSuperficieRegistral.setText(app.getI18nString("patrimonio.valoraciones.superficieregistral"));
    lblSuperficieCatastral.setText(app.getI18nString("patrimonio.valoraciones.superficiecatastral"));
    lblConstruccion.setText(app.getI18nString("patrimonio.valoraciones.construccion"));
    lblConstruccion.setFont(new Font("Dialog", 1, 11));
    lblValorActual3.setText(app.getI18nString("patrimonio.valoraciones.valoractual"));
    lblValorCatastral2.setText(app.getI18nString("patrimonio.valoraciones.valorcatastral"));
    lblValorAdquisicion3.setText(app.getI18nString("patrimonio.valoraciones.valoradquisicion"));
    lblSuperficieReal2.setText(app.getI18nString("patrimonio.valoraciones.superficiereal"));
    lblSuperficieCatastral2.setText(app.getI18nString("patrimonio.valoraciones.superficiecatastral"));
    lblSuperficieRegistral2.setText(app.getI18nString("patrimonio.valoraciones.superficieregistral"));
    lblParcela.setText(app.getI18nString("patrimonio.valoraciones.parcela"));
    lblParcela.setFont(new Font("Dialog", 1, 11));
    jPanel1.add(btnAplicar, null);

    jPanel1.setBounds(350, 380, 155, 40);
    jLabel413.setBounds(250, 340, 20, 20);
    jLabel412.setBounds(250, 255, 20, 20);
    jLabel411.setBounds(250, 230, 20, 20);
    jLabel410.setBounds(250, 205, 20, 20);
    jLabel49.setBounds(250, 120, 20, 20);
    jLabel48.setBounds(250, 95, 20, 20);
    jLabel47.setBounds(495, 340, 10, 20);
    jLabel46.setBounds(495, 200, 10, 20);
    jLabel45.setBounds(495, 225, 10, 20);
    jLabel44.setBounds(495, 255, 10, 20);
    jLabel43.setBounds(495, 120, 10, 20);
    jLabel42.setBounds(495, 90, 10, 20);
    jLabel41.setBounds(495, 65, 10, 20);
    jLabel40.setBounds(250, 70, 20, 20);
    txtValorActual.setBounds(390, 340, 100, 20);
    txtValorAdquisicion.setBounds(145, 340, 100, 20);
    txtConsValorAdq.setBounds(390, 200, 100, 20);
    txtConsValorCatastral.setBounds(390, 225, 100, 20);
    txtConsValorActual.setBounds(390, 255, 100, 20);
    txtConsSupReal.setBounds(145, 255, 100, 20);
    txtConsSupCatastral.setBounds(145, 225, 100, 20);
    txtConsSupRegistral.setBounds(145, 200, 100, 20);
    txtSueloSupReal.setBounds(145, 120, 100, 20);
    txtSueloValorActual.setBounds(390, 120, 100, 20);
    txtSueloValorCatastral.setBounds(390, 90, 100, 20);
    txtSueloSupCatastral.setBounds(145, 90, 100, 20);
    txtSueloValorAdq.setBounds(390, 65, 100, 20);
    txtSueloSupRegistral.setBounds(145, 65, 100, 20);
    lblValorAdquisicion.setBounds(35, 335, 115, 30);
    lblValorActual.setBounds(290, 335, 115, 30);
    jSeparator7.setBounds(5, 305, 520, 2);
    lblInmueble.setBounds(5, 280, 115, 30);
    lblValorAdquisicion2.setBounds(290, 195, 115, 30);
    lblValorCatastral.setBounds(290, 220, 115, 30);
    lblValorActual2.setBounds(290, 250, 115, 30);
    lblSuperficieReal.setBounds(35, 250, 140, 30);
    lblSuperficieRegistral.setBounds(35, 195, 115, 30);
    lblSuperficieCatastral.setBounds(35, 220, 115, 30);
    jSeparator6.setBounds(5, 180, 520, 2);
    lblConstruccion.setBounds(5, 150, 115, 30);
    lblValorActual3.setBounds(290, 115, 115, 30);
    lblValorCatastral2.setBounds(290, 85, 115, 30);
    lblValorAdquisicion3.setBounds(290, 60, 115, 30);
    lblSuperficieReal2.setBounds(35, 115, 115, 30);
    lblSuperficieCatastral2.setBounds(35, 85, 115, 30);
    lblSuperficieRegistral2.setBounds(35, 60, 115, 30);
    lblParcela.setBounds(5, 30, 115, 30);
    jSeparator5.setBounds(5, 55, 520, 2);



    this.add(jPanel1, null);
    this.add(jLabel413, null);
    this.add(jLabel412, null);
    this.add(jLabel411, null);
    this.add(jLabel410, null);
    this.add(jLabel49, null);
    this.add(jLabel48, null);
    this.add(jLabel47, null);
    this.add(jLabel46, null);
    this.add(jLabel45, null);
    this.add(jLabel44, null);
    this.add(jLabel43, null);
    this.add(jLabel42, null);
    this.add(jLabel41, null);
    this.add(jLabel40, null);
    this.add(txtValorActual, null);
    this.add(txtValorAdquisicion, null);
    this.add(txtConsValorAdq, null);
    this.add(txtConsValorCatastral, null);
    this.add(txtConsValorActual, null);
    this.add(txtConsSupReal, null);
    this.add(txtConsSupCatastral, null);
    this.add(txtConsSupRegistral, null);
    this.add(txtSueloSupReal, null);
    this.add(txtSueloValorActual, null);
    this.add(txtSueloValorCatastral, null);
    this.add(txtSueloSupCatastral, null);
    this.add(txtSueloValorAdq, null);
    this.add(txtSueloSupRegistral, null);
    this.add(lblValorAdquisicion, null);
    this.add(lblValorActual, null);
    this.add(jSeparator7, null);
    this.add(lblInmueble, null);
    this.add(lblValorAdquisicion2, null);
    this.add(lblValorCatastral, null);
    this.add(lblValorActual2, null);
    this.add(lblSuperficieReal, null);
    this.add(lblSuperficieRegistral, null);
    this.add(lblSuperficieCatastral, null);
    this.add(jSeparator6, null);
    this.add(lblConstruccion, null);
    this.add(lblValorActual3, null);
    this.add(lblValorCatastral2, null);
    this.add(lblValorAdquisicion3, null);
    this.add(lblSuperficieReal2, null);
    this.add(lblSuperficieCatastral2, null);
    this.add(lblSuperficieRegistral2, null);
    this.add(lblParcela, null);
    this.add(jSeparator5, null);
    
    btnAplicar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAplicar_actionPerformed(e);
        }
      });
    btnAplicar.setText(app.getI18nString("patrimonio.aplicar"));

  }

  public void enter()
  {
    
  	Blackboard Identificadores = app.getBlackboard();
  	ID_Inmueble = Integer.parseInt(Identificadores.get("IdInmueble").toString());
  	      
  	//Mostramos los datos para el inmueble
    PatrimonioPostgre Valora = new PatrimonioPostgre();
    ArrayList Datos= Valora.DatosValoracion (ID_Inmueble);
    if (Datos==null)return;
    Iterator alIt = Datos.iterator();
    Iterator itControles = Valoraciones.iterator();     
    while (itControles.hasNext()) 
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

  private void btnAplicar_actionPerformed(ActionEvent e)
  {
    ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
    Valor.add(txtSueloSupRegistral.getText());
    Tipo.add("1");
    Valor.add(txtSueloSupCatastral.getText());
    Tipo.add("1");
    Valor.add(txtSueloSupReal.getText());
    Tipo.add("1");
    Valor.add(txtSueloValorAdq.getText());
    Tipo.add("1");
    Valor.add(txtSueloValorCatastral.getText());
    Tipo.add("1");
    Valor.add(txtSueloValorActual.getText());
    Tipo.add("1");
    Valor.add(txtConsSupRegistral.getText());
    Tipo.add("1");
    Valor.add(txtConsSupCatastral.getText());
    Tipo.add("1");
    Valor.add(txtConsSupReal.getText());
    Tipo.add("1");
    Valor.add(txtConsValorAdq.getText());
    Tipo.add("1");
    Valor.add(txtConsValorCatastral.getText());
    Tipo.add("1");
    Valor.add(txtConsValorActual.getText());
    Tipo.add("1");
    Valor.add(txtValorAdquisicion.getText());
    Tipo.add("1");
    Valor.add(txtValorActual.getText());
    Tipo.add("1");

    //Actualizamos la información almacenada
     PatrimonioPostgre Valora = new PatrimonioPostgre();
     String Result = Valora.ActualizarValoracion(ID_Inmueble, Valor, Tipo);
     //System.out.println(Result);
  }


}