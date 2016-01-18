/**
 * ContaPatrimonioPanel.java
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

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class ContaPatrimonioPanel extends JPanel 
{



  private JTextField totalAmortizado = new JTextField();
  private JTextField annoTxt = new JTextField();
  private JTextField porcentajeTxt = new JTextField();
  private JLabel jLabel66 = new JLabel();
  private JLabel jLabel65 = new JLabel();
  private JLabel jLabel64 = new JLabel();
  private JLabel jLabel63 = new JLabel();
  private JTextField cuentaAmortizacionTxt = new JTextField();
  private JComboBox amortizacionCmb = new JComboBox();
  private JSeparator jSeparator10 = new JSeparator();
  private JSeparator jSeparator9 = new JSeparator();
  private JLabel jLabel62 = new JLabel();
  private JComboBox contableCmb = new JComboBox();
  private JTextField cuentaContableTxt = new JTextField();
  private JLabel jLabel61 = new JLabel();
  private JSeparator jSeparator8 = new JSeparator();
  private JLabel jLabel60 = new JLabel();
  public ContaPatrimonioPanel()
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
    this.setSize(new Dimension(580, 415));
    this.setBounds(new Rectangle(10, 5, 605, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    totalAmortizado.setBounds(new Rectangle(190, 255, 120, 20));
    annoTxt.setBounds(new Rectangle(190, 205, 65, 20));
    porcentajeTxt.setBounds(new Rectangle(190, 225, 65, 20));
    jLabel66.setText("Total Amortizado");
    jLabel66.setBounds(new Rectangle(55, 260, 125, 20));
    jLabel65.setText("%");
    jLabel65.setBounds(new Rectangle(55, 235, 45, 20));
    jLabel64.setText("Años");
    jLabel64.setBounds(new Rectangle(55, 210, 45, 20));
    jLabel63.setText("Cuenta Amortización");
    jLabel63.setBounds(new Rectangle(35, 165, 140, 30));
    cuentaAmortizacionTxt.setBounds(new Rectangle(190, 170, 65, 20));
    amortizacionCmb.setBackground(new Color(254, 255, 255));
    amortizacionCmb.setBounds(new Rectangle(280, 170, 285, 20));
    jSeparator10.setBounds(new Rectangle(0, 140, 595, 10));
    jSeparator9.setBounds(new Rectangle(5, 35, 585, 10));
    jLabel62.setText("Amortización");
    jLabel62.setBounds(new Rectangle(15, 110, 80, 30));
    contableCmb.setBackground(new Color(254, 255, 255));
    contableCmb.setBounds(new Rectangle(280, 60, 285, 20));
    cuentaContableTxt.setBounds(new Rectangle(190, 60, 65, 20));
    jLabel61.setText("Cuenta Contable");
    jLabel61.setBounds(new Rectangle(15, 55, 140, 30));
    jSeparator8.setBounds(new Rectangle(5, 35, 585, 10));
    jLabel60.setText("Contabilidad");
    jLabel60.setBounds(new Rectangle(20, 5, 80, 30));
    this.add(totalAmortizado, null);
    this.add(annoTxt, null);
    this.add(porcentajeTxt, null);
    this.add(jLabel66, null);
    this.add(jLabel65, null);
    this.add(jLabel64, null);
    this.add(jLabel63, null);
    this.add(cuentaAmortizacionTxt, null);
    this.add(amortizacionCmb, null);
    this.add(jSeparator10, null);
    this.add(jSeparator9, null);
    this.add(jLabel62, null);
    this.add(contableCmb, null);
    this.add(cuentaContableTxt, null);
    this.add(jLabel61, null);
    this.add(jSeparator8, null);
    this.add(jLabel60, null);

  }
}