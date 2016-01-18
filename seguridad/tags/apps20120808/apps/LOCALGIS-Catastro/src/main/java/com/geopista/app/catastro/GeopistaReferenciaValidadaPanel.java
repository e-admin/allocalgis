/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/
package com.geopista.app.catastro;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import com.geopista.ui.images.IconLoader;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
//import com.vividsolutions.jump.workbench.ui.wizard.*;
import com.geopista.ui.wizard.*;

public class GeopistaReferenciaValidadaPanel extends JPanel implements WizardPanel
{

  private JLabel lblImagen = new JLabel();
  private JLabel lblReferencia = new JLabel();
  private JTextField txtMunicipio = new JTextField();
  private JLabel lblMunicipio = new JLabel();

  public GeopistaReferenciaValidadaPanel()
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
    
 /* public static void main(String[] args)
  {
    JDialog dlgVentana = new JDialog();
    GeopistaReferenciaValidadaPanel iniciar= new GeopistaReferenciaValidadaPanel();
    dlgVentana.setTitle("Validar Referencia Catastral");
    dlgVentana.getContentPane().add(iniciar);
    dlgVentana.setSize(750, 600);
    dlgVentana.setVisible(true); 
    dlgVentana.setLocation(150, 90);
    
  }*/

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    
  
    
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

    lblReferencia.setText("La Referencia es válida");
    lblReferencia.setBounds(new Rectangle(135, 50, 355, 25));
    txtMunicipio.setBounds(new Rectangle(235, 90, 80, 20));
 //   jPanel1.setBounds(new Rectangle(430, 130, 80, 80));
    

    lblMunicipio.setText("Municipio");
    lblMunicipio.setBounds(new Rectangle(135, 90, 70, 15));

    this.setSize(750,600);
    //jScrollPane3.getViewport().add(lblImagen, null);

    this.add(lblMunicipio, null);
    //this.add(jPanel1, null);
    this.add(txtMunicipio, null);
    this.add(lblReferencia, null);
    this.add(lblImagen, null);
    //this.add(jScrollPane1, null);

   //CuadroDialogo.showMessageDialog(this,"Vd. no tiene acceso"); 
   
  }//jbinit
   public void enteredFromLeft(Map dataMap)
  {
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     System.out.println("saliendo de panel 1"); 
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
      
    }

    public void remove(InputChangedListener listener)
    {
      
    }

    public String getTitle()
    {
      return "ValiadarReferenciaPanel";
    }

    public String getID()
    {
      return "2";
    }

    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }


    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return nextID;
    }
    
  public void setWizardContext(WizardContext wd)
    {
    }
  

  private void btnSiguiente_actionPerformed(ActionEvent e)
  {
/*jb1.addMouseListener( new MouseAdapter()

{ public void mousePressed(MouseEvent e){*/

}

private void btnAnterior_actionPerformed(ActionEvent e)
  {
     //CREA UNA VENTANA NUEVA, NO CIERRA LA ACTUAL*******          
      /*    JFrame dlgVentana = new JFrame("Consultar Catastro");
          GeopistaReferenciaValidada geopistaReferenciaValidada = new GeopistaReferenciaValidada();
          dlgVentana.getContentPane().add(geopistaReferenciaValidada);
          dlgVentana.setSize(750, 600);
          dlgVentana.setVisible(true); 
          dlgVentana.setLocation(150, 90);
          this.setVisible(false);*/
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
  

 }
