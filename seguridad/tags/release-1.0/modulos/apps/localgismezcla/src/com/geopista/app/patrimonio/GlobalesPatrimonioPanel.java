package com.geopista.app.patrimonio;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GlobalesPatrimonioPanel extends JPanel 
{



  private JTextField inventarioTxt = new JTextField();
  private JComboBox usoCmb = new JComboBox();
  private JLabel jLabel6 = new JLabel();
  private JLabel jLabel5 = new JLabel();
  private JComboBox tipoCmb = new JComboBox();
  private JLabel jLabel4 = new JLabel();
  private JTextField descripcionTxt = new JTextField();
  private JTextField nombreTxt = new JTextField();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel municipioLbl = new JLabel();
  private JLabel jLabel1 = new JLabel();
  public GlobalesPatrimonioPanel()
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
    this.setSize(new Dimension(623, 300));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setSize(new Dimension(700, 80));
    this.setLayout(null);
    this.setBounds(new Rectangle(5, 5, 730, 80));
    usoCmb.setPreferredSize(new Dimension(124, 25));
    usoCmb.setBackground(new Color(255, 255, 252));
    usoCmb.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          usoCmb_actionPerformed(e);
        }
      });
    jLabel6.setText("Uso");
    jLabel5.setText("Nº Inventario");
    tipoCmb.setBackground(new Color(255, 252, 252));
    jLabel4.setText("Tipo");
    descripcionTxt.setPreferredSize(new Dimension(8, 25));
    jLabel3.setText("Descripción");
    jLabel2.setText("Nombre");
    municipioLbl.setText("Medina de Rioseco");
    jLabel1.setText("Municipio");
    inventarioTxt.setBounds(385, 5, 85, 20);
    usoCmb.setBounds(510, 30, 210, 20);
    jLabel6.setBounds(480, 33, 70, 15);
    jLabel5.setBounds(280, 8, 90, 15);
    tipoCmb.setBounds(510, 5, 210, 20);
    jLabel4.setBounds(475, 8, 35, 15);
    descripcionTxt.setBounds(85, 55, 635, 20);
    nombreTxt.setBounds(85, 30, 385, 20);
    jLabel3.setBounds(10, 58, 70, 15);
    jLabel2.setBounds(10, 33, 55, 15);
    municipioLbl.setBounds(90, 8, 125, 15);
    jLabel1.setBounds(10, 10, 55, 15);
    
    this.add(inventarioTxt, null);
    this.add(usoCmb, null);
    this.add(jLabel6, null);
    this.add(jLabel5, null);
    this.add(tipoCmb, null);
    this.add(jLabel4, null);
    this.add(descripcionTxt, null);
    this.add(nombreTxt, null);
    this.add(jLabel3, null);
    this.add(jLabel2, null);
    this.add(municipioLbl, null);
    this.add(jLabel1, null);

  }

  private void usoCmb_actionPerformed(ActionEvent e)
  {
  }

  private void tipoCmb_actionPerformed(ActionEvent e)
  {
  }
}