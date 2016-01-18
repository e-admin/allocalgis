/**
 * GeopistaBajaParcelaPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GeopistaBajaParcelaPanel extends JPanel
{
  private JLabel lblBaja = new JLabel();
  private JButton btnAceptar = new JButton();
  private JButton btnCancelar= new JButton();

  public GeopistaBajaParcelaPanel()
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
    
    lblBaja.setText("¿Realmente desea dar de baja esta parcela?");
    lblBaja.setBounds(new Rectangle(45, 25, 260, 20));
    btnAceptar.setText("Aceptar");
    btnAceptar.setBounds(new Rectangle(75, 65, 85, 25));
    btnCancelar.setText("Cancelar");
    btnCancelar.setBounds(new Rectangle(165, 65, 85, 25));
    this.setLayout(null);
    this.setSize(new Dimension(314, 151));
    this.add(btnCancelar, null);
    this.add(btnAceptar, null);
    this.add(lblBaja, null);    
  }
}