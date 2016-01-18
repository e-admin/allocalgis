/**
 * GeopistaBuscarPatrimonio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class GeopistaBuscarPatrimonio extends JPanel
{

  private JPanel pnlOpciones = new JPanel();
  private JPanel jPanel1 = new JPanel();
  private JLabel jLabel1 = new JLabel();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel jLabel2 = new JLabel();
  private JTextField txtReferencia = new JTextField();
  private JLabel jLabel3 = new JLabel();
  private JTextField txtNombre = new JTextField();
  private JButton cmdBuscar = new JButton();
  private JButton cmdCancelar = new JButton();
  private JSeparator jSeparator2 = new JSeparator();
  private JComboBox cmbNombre = new JComboBox();
  private JComboBox cmbFecha = new JComboBox();
  private JLabel jLabel4 = new JLabel();
  private JTextField txtFechaAlta = new JTextField();
  private JComboBox cmbTipo = new JComboBox();
  private JLabel jLabel5 = new JLabel();
  private JComboBox cmbTipoBien = new JComboBox();
  private JComboBox cmbDestino = new JComboBox();
  private JLabel jLabel6 = new JLabel();
  private JTextField txtDestino = new JTextField();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private DefaultTableModel model = new DefaultTableModel();
  private JLabel jLabel7 = new JLabel();
  private boolean inicio= true;
 
    private JTable tablaCampos = new JTable(model);
    
  public GeopistaBuscarPatrimonio()
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
  {
    this.setLayout(null);
   
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    jPanel1.setLayout(null);
    jLabel1.setText("Consulta Bienes Patrimonio");
    jLabel2.setText("Por Referencia Catastral:");
    jLabel3.setText("Por Nombre:");
    jLabel3.setHorizontalAlignment(SwingConstants.LEFT);
    cmdBuscar.setText("Buscar");
    cmdBuscar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmdBuscar_actionPerformed(e);
        }
      });
    cmdCancelar.setText("Comenzar");
      cmdCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmdCancelar_actionPerformed(e);
        }
      });
    cmbNombre.setBackground(new Color(254, 255, 255));
    cmbFecha.setBackground(new Color(254, 255, 255));
    jLabel4.setText("Fecha de Alta:");
    jLabel4.setHorizontalAlignment(SwingConstants.LEFT);
    cmbTipo.setBackground(new Color(254, 255, 255));
    jLabel5.setText("Tipo de Bien:");
    jLabel5.setHorizontalAlignment(SwingConstants.LEFT);
    cmbTipoBien.setBackground(new Color(254, 255, 255));
    cmbDestino.setBackground(new Color(254, 255, 255));
    jLabel6.setText("Destino:");
    jLabel6.setHorizontalAlignment(SwingConstants.LEFT);
    jLabel7.setText("Resultados Búsqueda:");
    jScrollPane1.getViewport().add(tablaCampos, null);
    jLabel7.setBounds(10, 180, 155, 15);
    jScrollPane1.setBounds(5, 200, 490, 125);
    txtDestino.setBounds(170, 140, 325, 25);
    jLabel6.setBounds(55, 145, 110, 20);
    cmbDestino.setBounds(5, 145, 45, 20);
    cmbTipoBien.setBounds(170, 115, 325, 20);
    jLabel5.setBounds(55, 120, 110, 20);
    cmbTipo.setBounds(5, 120, 45, 20);
    txtFechaAlta.setBounds(170, 90, 325, 20);
    jLabel4.setBounds(55, 90, 110, 20);
    cmbFecha.setBounds(5, 90, 45, 20);
    cmbNombre.setBounds(5, 60, 45, 20);
    jSeparator2.setBounds(0, 175, 500, 5);
    cmdCancelar.setBounds(410, 330, 85, 25);
    cmdBuscar.setBounds(320, 330, 85, 25);
    txtNombre.setBounds(170, 60, 325, 20);
    jLabel3.setBounds(55, 60, 110, 20);
    txtReferencia.setBounds(170, 30, 325, 20);
    jLabel2.setBounds(5, 30, 140, 20);
    jSeparator1.setBounds(0, 20, 500, 5);
    jLabel1.setBounds(5, 5, 305, 15);
    jPanel1.setBounds(5, 5, 505, 360);
    
    jPanel1.add(jLabel7, null);
    jPanel1.add(jScrollPane1, null);
    jPanel1.add(txtDestino, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(cmbDestino, null);
    jPanel1.add(cmbTipoBien, null);
    jPanel1.add(jLabel5, null);
    jPanel1.add(cmbTipo, null);
    jPanel1.add(txtFechaAlta, null);
    jPanel1.add(jLabel4, null);
    jPanel1.add(cmbFecha, null);
    jPanel1.add(cmbNombre, null);
    jPanel1.add(jSeparator2, null);
    jPanel1.add(cmdCancelar, null);
    jPanel1.add(cmdBuscar, null);
    jPanel1.add(txtNombre, null);
    jPanel1.add(jLabel3, null);
    jPanel1.add(txtReferencia, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(jSeparator1, null);
    jPanel1.add(jLabel1, null);
    this.add(jPanel1, null);

  //Tipo de bien

    cmbTipoBien.addItem("1"); 
    cmbTipoBien.addItem("2"); 
  // Tipo
    cmbTipo.addItem("--");
    cmbTipo.addItem("0");
    cmbTipo.addItem("Y");
 // Nombre   
    cmbNombre.addItem("--");
    cmbNombre.addItem("0");
    cmbNombre.addItem("Y");
// Fecha de Alta
    cmbFecha.addItem("--");
    cmbFecha.addItem("0");
    cmbFecha.addItem("Y");

// Linderos
    cmbDestino.addItem("--");
    cmbDestino.addItem("0");
    cmbDestino.addItem("Y");

    JTableHeader header = tablaCampos.getTableHeader();
    cmdCancelar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmdCancelar_actionPerformed(e);
        }
      });
    model.addColumn("Número Inventario");
    model.addColumn("Nombre");
    model.addColumn("Descripción");
    model.addColumn("Tipo");
  }

  

public static void main(String[] args)
 {
    JDialog frame1 = new JDialog();
    frame1.setTitle("Búsqueda de Bienes del Patrimonio Municipal");
    
    GeopistaBuscarPatrimonio geopistaBuscarPatrimonio = new GeopistaBuscarPatrimonio();

    frame1.getContentPane().add(geopistaBuscarPatrimonio);
    frame1.setSize(520,400);
    frame1.setResizable(false);
    frame1.setVisible(true);
   }

  private void cmdBuscar_actionPerformed(ActionEvent e)
  {
      //Construimos la consulta SQL. Para ello deberemos ir recorriendo cada uno de los controles
      String SQL="";
      inicio=true;
      if (txtReferencia.getText().equals("")){
      }else
      {   inicio=false;
          String refpar = txtReferencia.getText().substring(0,7);
          String refpla = txtReferencia.getText().substring(7,14);
          SQL= "((inmuebles.RefPar)='" + refpar + "') AND ((inmuebles.refpla)='" + refpla + "') ";
      }
      if (!txtNombre.getText().equals("")){
          if (inicio==true){
              SQL="((bienes.nombre)='" + txtNombre.getText() + "') ";
              inicio=false;
          }else{
              if (cmbNombre.getSelectedIndex()==2){
                  SQL= SQL + "AND ((bienes.nombre)='" + txtNombre.getText() + "')" ;}
              else{
                  SQL= SQL + "OR ((bienes.nombre)='" + txtNombre.getText() + "')" ;}  
          }          
      }
        if (!txtFechaAlta.getText().equals("")){
            if (inicio==true){
              SQL="((bienes.fechaalta)='" + txtFechaAlta.getText() + "')" ;
              inicio=false;
          }else{
              if (cmbFecha.getSelectedIndex()==2){
                  SQL= SQL + "AND ((bienes.fechaalta)='" + txtFechaAlta.getText() + "')" ;}
              else{
                  SQL= SQL + "OR ((bienes.fechaalta)='" + txtFechaAlta.getText() + "')" ;}  
          }          
      }
       if (!cmbTipoBien.getSelectedItem().equals("")){
         if (inicio==true){
                SQL="((bienes.tipo_bien)='" + cmbTipoBien.getSelectedItem() + "')" ;
                inicio=false;
          }else{
              if (cmbTipo.getSelectedIndex()==2){
                  SQL= SQL + "AND ((bienes.tipo_bien)='" + cmbTipoBien.getSelectedItem() + "')" ;}
              else{
                  SQL= SQL + "OR ((bienes.tipo_bien)='" + cmbTipoBien.getSelectedItem() + "')" ;}  
          }          
      }
       if (!txtDestino.getText().equals("")){
         if (inicio==true){
                SQL="((bienes.destino)='" + txtDestino.getText() + "')" ;
                inicio=false;
          }else{
              if (cmbDestino.getSelectedIndex()==2){
                  SQL= SQL + "AND ((bienes.destino)='" + txtDestino.getText() + "')" ;}
              else{
                  SQL= SQL + "OR ((bienes.destino)='" + txtDestino.getText() + "')" ;}  
          }          
      }

      String query= "SELECT bienes.num_inventario, bienes.nombre, bienes.descripcion, bienes.tipo_bien " +
                  " FROM bienes INNER JOIN inmuebles ON bienes.ID_Bien = inmuebles.ID_inmueble " +
                  " WHERE ( " + SQL + ")";
      System.out.println(query);
      //En QUERY ya tenemos almacenada la consulta
      //La ejecutamos y mostramos los resultados en la tabla

        PatrimonioPostgre Consulta = new PatrimonioPostgre();
        ArrayList Datos= Consulta.EjecutarConsulta(query);
             if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     int filas=0;
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

  private void cmdCancelar_actionPerformed(ActionEvent e)
  {

    for (int n=0; n<model.getRowCount(); n++){
      model.removeRow(n);}

    
  }


} //De la clase 
