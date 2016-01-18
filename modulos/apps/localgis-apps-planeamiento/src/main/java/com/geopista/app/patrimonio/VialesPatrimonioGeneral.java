/**
 * VialesPatrimonioGeneral.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;



public class VialesPatrimonioGeneral extends JPanel implements  FeatureExtendedPanel {
  
  private JButton finalizarCmd = new JButton();
  private JButton cancelarCmd = new JButton();
  private JPanel generalesJpn = new JPanel();
  private JTextField txtVerde = new JTextField();
  private JLabel jLabel116 = new JLabel();
  private JTextField txtSupNoPavimentada = new JTextField();
  private JLabel jLabel115 = new JLabel();
  private JTextField txtSupPavimentada = new JTextField();
  private JLabel jLabel114 = new JLabel();
  private JLabel jLabel113 = new JLabel();
  private JTextField txtAncho = new JTextField();
  private JLabel jLabel112 = new JLabel();
  private JTextField txtLongitud = new JTextField();
  private JTextField txtValorActual = new JTextField();
  private JLabel jLabel111 = new JLabel();
  private JTextField txtFinal = new JTextField();
  private JTextField txtInicio = new JTextField();
  private JLabel jLabel110 = new JLabel();
  private JLabel jLabel19 = new JLabel();
  private JTextField txtFarolas = new JTextField();
  private JTextField txtBancos = new JTextField();
  private JTextField txtApliques = new JTextField();
  private JTextField txtPapeleras = new JTextField();
  private JLabel jLabel18 = new JLabel();
  private JComboBox cmbConservacion = new JComboBox();
  private JLabel jLabel17 = new JLabel();
  private JLabel jLabel16 = new JLabel();
  private JLabel jLabel15 = new JLabel();
  private JLabel jLabel14 = new JLabel();
  private JTextField txtCategoria = new JTextField();
  private JLabel jLabel13 = new JLabel();
  private JSeparator jSeparator3 = new JSeparator();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel jLabel12 = new JLabel();
  private JTextField txtCesionario = new JTextField();
  private JTextField txtCesion = new JTextField();
  private JLabel jLabel11 = new JLabel();
  private JComboBox cmbPropiedad = new JComboBox();
  private JLabel jLabel10 = new JLabel();
  private JLabel jLabel9 = new JLabel();
  private JTextField txtFormaAdq = new JTextField();
  private JTextField txtDestino = new JTextField();
  private JLabel jLabel8 = new JLabel();
  private JTextField txtFechaAdq = new JTextField();
  private JLabel jLabel7 = new JLabel();
  private int ID_Bien;
  private ArrayList ControlesGenerales= new ArrayList();   
  
 public static void main(String args[])
  {
     
    JDialog frame1 = new JDialog();
    frame1.setTitle("Viales");
    frame1.setResizable(false);
    frame1.setModal(true);

    VialesPatrimonioGeneral VialesPatrimonioGeneral  = new VialesPatrimonioGeneral();
    frame1.getContentPane().add(VialesPatrimonioGeneral);
    frame1.setSize(750,565);
    frame1.setVisible(true);

  
    
  }

  
  //Es el Constructur de Viales
  public VialesPatrimonioGeneral()
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
 private void jbInit() throws Exception 
  { //Definimos la interface
    
    this.setLayout(null);
    ControlesGenerales.add(txtDestino);
    ControlesGenerales.add(txtFechaAdq);
    ControlesGenerales.add(txtFormaAdq);
    ControlesGenerales.add(cmbPropiedad);
    ControlesGenerales.add(txtCesion);
    ControlesGenerales.add(txtCesionario);
    ControlesGenerales.add(txtCategoria);
    ControlesGenerales.add(txtFarolas);
    ControlesGenerales.add(txtApliques);
    ControlesGenerales.add(txtBancos);
    ControlesGenerales.add(txtPapeleras);
    ControlesGenerales.add(txtSupPavimentada);
    ControlesGenerales.add(txtSupNoPavimentada);
    ControlesGenerales.add(cmbConservacion);
    ControlesGenerales.add(txtValorActual);
    ControlesGenerales.add(txtVerde);
    ControlesGenerales.add(txtInicio);
    ControlesGenerales.add(txtFinal);
    ControlesGenerales.add(txtLongitud);
    ControlesGenerales.add(txtAncho);

    finalizarCmd.setText("Aceptar");
    cancelarCmd.setText("Cancelar");
    generalesJpn.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    this.setMinimumSize(new Dimension(615, 415));
    jLabel116.setText("Z.Verde");
    jLabel115.setText("Sup.No Pavimentada");
    jLabel114.setText("Sup.Pavimentada");
    jLabel113.setText("Ancho");
    jLabel112.setText("Longitud");
    jLabel111.setText("Valor Actual");
    jLabel110.setText("Final");
    jLabel19.setText("Inicio");
    jLabel18.setText("Conservación");
    cmbConservacion.setBackground(new Color(255, 252, 252));
    jLabel17.setText("Nº Papeleras");
    jLabel16.setText("Nº Bancos");
    jLabel15.setText("Apliques");
    jLabel14.setText("Nº Farolas");
    txtCategoria.setText("25/02/1999");
    jLabel13.setText("Categoría Vial");
    jLabel12.setText("Datos Cesionario");
    txtCesionario.setText("Destino del Inmueble");
    jLabel11.setText("Datos Cesión");
    cmbPropiedad.setPreferredSize(new Dimension(124, 25));
    cmbPropiedad.setBackground(new Color(255, 255, 252));
    jLabel10.setText("Propiedad");
    jLabel9.setText("Forma Adquisición");
    txtFormaAdq.setText("Adquisición por Compra Venta");
    txtDestino.setText("Destino del Inmueble");
    jLabel8.setText("Destino");
    txtFechaAdq.setText("25/02/1999");
    jLabel7.setText("Fecha Adquisición");

    txtVerde.setBounds(115, 185, 175, 20);
    jLabel116.setBounds(5, 188, 50, 15);
    txtSupNoPavimentada.setBounds(415, 185, 175, 20);
    jLabel115.setBounds(295, 188, 120, 15);
    txtSupPavimentada.setBounds(415, 215, 175, 20);
    jLabel114.setBounds(295, 218, 105, 15);
    jLabel113.setBounds(295, 160, 40, 15);
    txtAncho.setBounds(415, 155, 175, 20);
    jLabel112.setBounds(295, 135, 60, 15);
    txtLongitud.setBounds(415, 125, 175, 20);
    txtValorActual.setBounds(115, 215, 175, 20);
    jLabel111.setBounds(5, 218, 75, 15);
    txtFinal.setBounds(415, 100, 175, 20);
    txtInicio.setBounds(115, 100, 175, 20);
    jLabel110.setBounds(295, 103, 35, 15);
    jLabel19.setBounds(5, 103, 80, 15);
    txtFarolas.setBounds(115, 155, 60, 20);
    txtBancos.setBounds(240, 155, 50, 20);
    txtApliques.setBounds(240, 125, 50, 20);
    txtPapeleras.setBounds(115, 125, 60, 20);
    jLabel18.setBounds(295, 73, 80, 15);
    cmbConservacion.setBounds(415, 70, 175, 20);
    jLabel17.setBounds(5, 128, 75, 15);
    jLabel16.setBounds(180, 160, 60, 15);
    jLabel15.setBounds(190, 128, 50, 15);
    jLabel14.setBounds(5, 160, 65, 15);
    txtCategoria.setBounds(115, 70, 175, 20);
    jLabel13.setBounds(5, 73, 105, 15);
    jSeparator3.setBounds(0, 245, 600, 5);
    jSeparator1.setBounds(0, 65, 600, 5);
    jLabel12.setBounds(5, 315, 100, 15);
    txtCesionario.setBounds(115, 310, 475, 20);
    txtCesion.setBounds(115, 285, 475, 20);
    jLabel11.setBounds(5, 290, 75, 15);
    cmbPropiedad.setBounds(115, 260, 165, 20);
    jLabel10.setBounds(5, 265, 100, 15);
    jLabel9.setBounds(5, 40, 115, 15);
    txtFormaAdq.setBounds(115, 35, 475, 20);
    txtDestino.setBounds(265, 5, 325, 20);
    jLabel8.setBounds(205, 10, 50, 15);
    txtFechaAdq.setBounds(115, 5, 70, 20);
    jLabel7.setBounds(5, 10, 105, 15);
    cancelarCmd.setBounds(650, 505, 85, 25);
    finalizarCmd.setBounds(550, 505, 85, 25);
    
    this.add(txtVerde, null);
    this.add(jLabel116, null);
    this.add(txtSupNoPavimentada, null);
    this.add(jLabel115, null);
    this.add(txtSupPavimentada, null);
    this.add(jLabel114, null);
    this.add(jLabel113, null);
    this.add(txtAncho, null);
    this.add(jLabel112, null);
    this.add(txtLongitud, null);
    this.add(txtValorActual, null);
    this.add(jLabel111, null);
    this.add(txtFinal, null);
    this.add(jLabel110, null);
    this.add(jLabel19, null);
    this.add(txtFarolas, null);
    this.add(txtBancos, null);
    this.add(txtApliques, null);
    this.add(txtPapeleras, null);
    this.add(jLabel18, null);
    this.add(cmbConservacion, null);
    this.add(jLabel17, null);
    this.add(jLabel16, null);
    this.add(jLabel15, null);
    this.add(jLabel14, null);
    this.add(txtCategoria, null);
    this.add(jLabel13, null);
    this.add(jSeparator3, null);
    this.add(jSeparator1, null);
    this.add(jLabel12, null);
    this.add(txtCesionario, null);
    this.add(txtCesion, null);
    this.add(jLabel11, null);
    this.add(cmbPropiedad, null);
    this.add(jLabel10, null);
    this.add(jLabel9, null);
    this.add(txtFormaAdq, null);
    this.add(txtDestino, null);
    this.add(jLabel8, null);
    this.add(txtFechaAdq, null);
    this.add(jLabel7, null);
    this.add(cancelarCmd, null);
    this.add(finalizarCmd, null);

    // Cargamos los combos de estado de conservacion
    cmbConservacion.addItem("--");
    cmbConservacion.addItem("Bueno");
    cmbConservacion.addItem("Malo");
    cmbConservacion.addItem("Regular");

    //Cargamos el combo de Propiedad
    cmbPropiedad.addItem("--");
    cmbPropiedad.addItem("Propio");
    cmbPropiedad.addItem("Cedido");
    cmbPropiedad.addItem("Adscrito");

 
//HASTA Q LO PODAMOS PROBAR CON CAPA
    //Mostramos los datos para la parcela
     PatrimonioPostgre GeneralViales = new PatrimonioPostgre();
     ArrayList Datos= GeneralViales.DatosGeneralesViales(ID_Bien);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = ControlesGenerales.iterator();     
    
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

 

  public void enter()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    ID_Bien= Integer.parseInt(Identificadores.get("ID_Bien").toString());
    //Mostramos los datos para la parcela
     PatrimonioPostgre GeneralViales = new PatrimonioPostgre();
     ArrayList Datos= GeneralViales.DatosGeneralesViales(ID_Bien);
     if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = ControlesGenerales.iterator();     
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
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    Identificadores.put ("ID_Bien", ID_Bien);
  }

  private void btnAplicar_actionPerformed(ActionEvent e)
  {
  //Aplicamos los cambios

    ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
    Valor.add(String.valueOf(ID_Bien));
    Tipo.add("0");
    Valor.add(txtDestino.getText());
    Tipo.add("1");
    Valor.add(txtFechaAdq.getText());
    Tipo.add("1");
    Valor.add(txtFormaAdq.getText());
    Tipo.add("1");
    Valor.add(cmbPropiedad.getSelectedItem().toString());
    Tipo.add("1");
    Valor.add(txtCesion.getText());
    Tipo.add("1");
    Valor.add(txtCesionario.getText());
    Tipo.add("1");
    //tabla VIALES
    Valor.add(txtCategoria.getText());
    Tipo.add("1");
    Valor.add(txtFarolas.getText());
    Tipo.add("0");
    Valor.add(txtApliques.getText());
    Tipo.add("0");
    Valor.add(txtBancos.getText());
    Tipo.add("0");
    Valor.add(txtPapeleras.getText());
    Tipo.add("0");
    Valor.add(txtSupPavimentada.getText());
    Tipo.add("0");
    Valor.add(txtSupNoPavimentada.getText());
    Tipo.add("0");
    Valor.add(cmbConservacion.getSelectedItem().toString());
    Tipo.add("0");
    Valor.add(txtValorActual.getText());
    Tipo.add("0");
    Valor.add(txtVerde.getText());
    Tipo.add("0");
    Valor.add(txtInicio.getText());
    Tipo.add("1");
    Valor.add(txtFinal.getText());
    Tipo.add("1");
    Valor.add(txtLongitud.getText());
    Tipo.add("0");
    Valor.add(txtAncho.getText());
    Tipo.add("0");

    //Actualizamos la información almacenada
     PatrimonioPostgre GeneralViales = new PatrimonioPostgre();
     String Result = GeneralViales.ActualizarGeneralViales(ID_Bien, Valor, Tipo);
     System.out.println(Result);
  }  

 
  
  
  }