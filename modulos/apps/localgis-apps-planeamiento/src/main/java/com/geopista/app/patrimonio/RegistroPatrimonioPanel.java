/**
 * RegistroPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class RegistroPatrimonioPanel extends JPanel {
public class RegistroPatrimonioPanel extends  JPanel implements FeatureExtendedPanel {


  private JTextField txtNumero = new JTextField();
  private JLabel lblInscripcion = new JLabel();
  private JTextField txtFinca = new JTextField();
  private JLabel lblFinca = new JLabel();
  private JTextField txtFolio = new JTextField();
  private JLabel lblFolio = new JLabel();
  private JTextField txtLibro = new JTextField();
  private JLabel lblLibro = new JLabel();
  private JTextField txtTomo = new JTextField();
  private JLabel lblTomo = new JLabel();
  private JLabel lblRegistro = new JLabel();
  private JTextField txtNotario = new JTextField();
  private JTextField txtProtocolo = new JTextField();
  private JLabel lblProtocolo = new JLabel();
  private JTextField txtRegistro = new JTextField();
  private JLabel lblNotario = new JLabel();
  private JPanel jPanel1 = new JPanel();
  private JButton btnAplicar = new JButton();
  private ArrayList Registro = new ArrayList();
  private boolean alta =false;
  private int ID_Bien;
  private int ID_Inmueble;

  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  
  public RegistroPatrimonioPanel()
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
    
    RegistroPatrimonioPanel RegistroPatrimonio = new RegistroPatrimonioPanel();

    frame1.getContentPane().add(RegistroPatrimonio);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }
*/
  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.registro.titulo"));    
    this.setSize(new Dimension(710, 477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    Registro.add(txtTomo);
    Registro.add(txtFolio);
    Registro.add(txtLibro);    
    Registro.add(txtFinca);
    Registro.add(txtNumero);
    Registro.add(txtProtocolo);
    Registro.add(txtNotario);
    Registro.add(txtRegistro);  

    
    lblInscripcion.setText(app.getI18nString("patrimonio.registro.inscripcion"));
    lblFinca.setText(app.getI18nString("patrimonio.registro.finca"));
    lblFolio.setText(app.getI18nString("patrimonio.registro.folio"));
    lblLibro.setText(app.getI18nString("patrimonio.registro.libro"));
    lblTomo.setText(app.getI18nString("patrimonio.registro.tomo"));
    lblRegistro.setText(app.getI18nString("patrimonio.registro.registro"));
    lblProtocolo.setText(app.getI18nString("patrimonio.registro.protocolo"));
    lblNotario.setText(app.getI18nString("patrimonio.registro.notario"));
    btnAplicar.setText(app.getI18nString("patrimonio.aplicar"));
    btnAplicar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAplicar_actionPerformed(e);
        }
      });
    jPanel1.add(btnAplicar, null);

    jPanel1.setBounds(350, 335, 140, 40);
    txtNumero.setBounds(110, 290, 110, 20);
    lblInscripcion.setBounds(5, 295, 130, 15);
    txtFinca.setBounds(110, 249, 110, 20);
    lblFinca.setBounds(5, 254, 50, 15);
    txtFolio.setBounds(110, 209, 110, 20);
    lblFolio.setBounds(5, 214, 50, 15);
    txtLibro.setBounds(110, 168, 110, 20);
    lblLibro.setBounds(5, 173, 50, 15);
    txtTomo.setBounds(110, 127, 110, 20);
    lblTomo.setBounds(5, 132, 50, 15);
    lblRegistro.setBounds(5, 91, 90, 15);
    txtNotario.setBounds(110, 5, 400, 20);
    txtProtocolo.setBounds(110, 46, 400, 20);
    lblProtocolo.setBounds(5, 51, 90, 15);
    txtRegistro.setBounds(110, 86, 400, 20);
    lblNotario.setBounds(5, 10, 70, 15);
    
    this.add(jPanel1, null);
    this.add(txtNumero, null);
    this.add(lblInscripcion, null);
    this.add(txtFinca, null);
    this.add(lblFinca, null);
    this.add(txtFolio, null);
    this.add(lblFolio, null);
    this.add(txtLibro, null);
    this.add(lblLibro, null);
    this.add(txtTomo, null);
    this.add(lblTomo, null);
    this.add(lblRegistro, null);
    this.add(txtNotario, null);
    this.add(txtProtocolo, null);
    this.add(lblProtocolo, null);
    this.add(txtRegistro, null);
    this.add(lblNotario, null);

  }

  private void btnAplicar_actionPerformed(ActionEvent e)
  {
    ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
  
    //Tipo es 0 para enteros y 1 para cadenas
    Valor.add(txtTomo.getText());
    Tipo.add("1");
    Valor.add(txtFolio.getText());
    Tipo.add("1");
    Valor.add(txtLibro.getText());
    Tipo.add("1");
    Valor.add(txtFinca.getText());
    Tipo.add("1");
    Valor.add(txtNumero.getText());
    Tipo.add("1");
    Valor.add(txtProtocolo.getText());
    Tipo.add("1");
    Valor.add(txtNotario.getText());
    Tipo.add("1");
    Valor.add(txtRegistro.getText());
    Tipo.add("1");
    //Actualizamos la información almacenada
     PatrimonioPostgre Registro = new PatrimonioPostgre();
     String Result = Registro.ActualizarRegistro(ID_Inmueble, Valor, Tipo);
     //System.out.println(Result);
  }

  private void btnNuevo_actionPerformed(ActionEvent e)
  {
  }

  private void btnEliminar_actionPerformed(ActionEvent e)
  {
  }

  public void enter()
  {
    Blackboard Identificadores = app.getBlackboard();
  	ID_Inmueble = Integer.parseInt(Identificadores.get("IdInmueble").toString());
  	     
    //Mostramos los datos para el inmueble
    PatrimonioPostgre General = new PatrimonioPostgre();
    ArrayList Datos= General.DatosRegistro(ID_Inmueble);
    if (Datos==null)return;
    Iterator alIt = Datos.iterator();
    Iterator itControles = Registro.iterator();     
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

}