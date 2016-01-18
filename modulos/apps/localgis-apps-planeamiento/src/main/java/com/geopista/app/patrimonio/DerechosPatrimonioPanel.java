/**
 * DerechosPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import javax.swing.SwingConstants;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class DerechosPatrimonioPanel extends JPanel 
public class DerechosPatrimonioPanel extends JPanel implements FeatureExtendedPanel 
{
  private JLabel jLabel512 = new JLabel();
  private JSeparator jSeparator13 = new JSeparator();
  private JSeparator jSeparator12 = new JSeparator();
  private JLabel jLabel511 = new JLabel();
  private JSeparator jSeparator11 = new JSeparator();
  private JLabel jLabel510 = new JLabel();
  private JLabel jLabel59 = new JLabel();
  private JLabel jLabel58 = new JLabel();
  private JLabel jLabel57 = new JLabel();
  private JTextField txtValorFrutos = new JTextField();
  private JTextField txtFrutos = new JTextField();
  private JTextField txtValorDerechosFavor = new JTextField();
  private JTextField txtDerechosPersonales = new JTextField();
  private JTextField txtDerechosContra = new JTextField();
  private JTextField txtDerechosFavor = new JTextField();
  private JLabel jLabel56 = new JLabel();
  private JLabel jLabel55 = new JLabel();
  private JLabel jLabel53 = new JLabel();
  private JLabel jLabel52 = new JLabel();
  private JLabel jLabel51 = new JLabel();
  private JLabel jLabel50 = new JLabel();
  private JTextField txtValorDerechosContra = new JTextField();
  private JLabel jLabel54 = new JLabel();
  private int ID_Bien;
  private int ID_Inmueble;
  private ArrayList Derechos= new ArrayList();
  private JPanel jPanel1 = new JPanel();
  private JButton btnAplicar = new JButton();

  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  public DerechosPatrimonioPanel()
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
    DerechosPatrimonioPanel DerechosPatrimonioPanel = new DerechosPatrimonioPanel();
    frame1.getContentPane().add(DerechosPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.derechos.titulo"));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setLayout(null);
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    Derechos.add(txtDerechosFavor);
    Derechos.add(txtValorDerechosFavor);
    Derechos.add(txtDerechosContra);
    Derechos.add(txtValorDerechosContra);
    Derechos.add(txtDerechosPersonales);    
    Derechos.add(txtFrutos);
    Derechos.add(txtValorFrutos);
    
    jLabel512.setText(app.getI18nString("patrimonio.derechos.descripcionderechos"));
    jLabel511.setText(app.getI18nString("patrimonio.derechos.personales"));
    jLabel510.setText(app.getI18nString("patrimonio.derechos.reales"));
    jLabel59.setText("€");
    jLabel59.setFont(new Font("Dialog", 1, 11));
    jLabel59.setAlignmentY((float)0.0);
    jLabel59.setHorizontalTextPosition(SwingConstants.LEFT);
    jLabel59.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel58.setText("€");
    jLabel58.setFont(new Font("Dialog", 1, 11));
    jLabel58.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel57.setText("€");
    jLabel57.setFont(new Font("Dialog", 1, 11));
    jLabel57.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel56.setText(app.getI18nString("patrimonio.derechos.importefrutos"));
    jLabel55.setText(app.getI18nString("patrimonio.derechos.frutos"));
    jLabel53.setText(app.getI18nString("patrimonio.derechos.valorderechosafavor"));
    jLabel52.setText(app.getI18nString("patrimonio.derechos.descripcionderechos"));
    jLabel51.setText(app.getI18nString("patrimonio.derechos.encontra"));
    jLabel50.setText(app.getI18nString("patrimonio.derechos.afavor"));
    jLabel54.setText(app.getI18nString("patrimonio.derechos.valorderechosencontra"));
    jPanel1.add(btnAplicar, null);
    jPanel1.setBounds(420, 355, 100, 40);
    jLabel54.setBounds(10, 245, 235, 25);
    txtValorDerechosContra.setBounds(250, 245, 125, 20);
    jLabel512.setBounds(10, 310, 105, 25);
    jSeparator13.setBounds(5, 295, 520, 5);
    jSeparator12.setBounds(5, 140, 520, 2);
    jLabel511.setBounds(5, 110, 155, 40);
    jSeparator11.setBounds(5, 40, 520, 5);
    jLabel510.setBounds(5, 10, 155, 35);
    jLabel59.setBounds(290, 345, 20, 25);
    jLabel58.setBounds(370, 245, 20, 25);
    jLabel57.setBounds(370, 200, 20, 25);
    txtValorFrutos.setBounds(160, 345, 125, 20);
    txtFrutos.setBounds(160, 315, 335, 20);
    txtValorDerechosFavor.setBounds(250, 200, 125, 20);
    txtDerechosPersonales.setBounds(250, 160, 245, 20);
    txtDerechosContra.setBounds(160, 85, 335, 20);
    txtDerechosFavor.setBounds(160, 50, 335, 20);
    jLabel56.setBounds(10, 340, 155, 40);
    jLabel55.setBounds(5, 275, 45, 25);
    jLabel53.setBounds(10, 200, 235, 25);
    jLabel52.setBounds(10, 150, 155, 40);
    jLabel51.setBounds(10, 80, 155, 30);
    jLabel50.setBounds(10, 45, 50, 35);
    this.add(jPanel1, null);
    this.add(jLabel54, null);
    this.add(txtValorDerechosContra, null);
    this.add(jLabel512, null);
    this.add(jSeparator13, null);
    this.add(jSeparator12, null);
    this.add(jLabel511, null);
    this.add(jSeparator11, null);
    this.add(jLabel510, null);
    this.add(jLabel59, null);
    this.add(jLabel58, null);
    this.add(jLabel57, null);
    this.add(txtValorFrutos, null);
    this.add(txtFrutos, null);
    this.add(txtValorDerechosFavor, null);
    this.add(txtDerechosPersonales, null);
    this.add(txtDerechosContra, null);
    this.add(txtDerechosFavor,null);
    this.add(jLabel56, null);
    this.add(jLabel55, null);
    this.add(jLabel53, null);
    this.add(jLabel52, null);
    this.add(jLabel51, null);
    this.add(jLabel50, null);

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
    PatrimonioPostgre Derecho = new PatrimonioPostgre();
     ArrayList Datos= Derecho.DatosDerecho (ID_Inmueble);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = Derechos.iterator();     
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
    Valor.add(txtDerechosFavor.getText());
    Tipo.add("1");
    Valor.add(txtValorDerechosFavor.getText());
    Tipo.add("1");
    Valor.add(txtDerechosContra.getText());
    Tipo.add("1");
    Valor.add(txtValorDerechosContra.getText());
    Tipo.add("1");
    Valor.add(txtDerechosPersonales.getText());
    Tipo.add("1");
    Valor.add(txtFrutos.getText());
    Tipo.add("1");
    Valor.add(txtValorFrutos.getText());
    Tipo.add("1");
    //Actualizamos la información almacenada
     PatrimonioPostgre Derecho = new PatrimonioPostgre();
     String Result = Derecho.ActualizarDerechos(ID_Inmueble, Valor, Tipo);
     //System.out.println(Result);
  }

}