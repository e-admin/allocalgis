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
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.reports.GeopistaInformacionReferenciaVias;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaGenerarInformePanel extends JPanel implements WizardPanel
{

  private JLabel lblImagen = new JLabel();
  //DefaultListModel listModel = new DefaultListModel();
  private JLabel lblTipoInforme = new JLabel();
  private JComboBox cmbTipoInforme = new JComboBox();
 private JOptionPane CuadroDialogo; 

  public GeopistaGenerarInformePanel()
  {
    try
    {
    //  jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
   private void jbInit() throws Exception
  {   
    
    lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    lblTipoInforme.setText("Seleccione el tipo de informe a producir:");
    lblTipoInforme.setBounds(new Rectangle(135, 20, 275, 30));
   
    cmbTipoInforme.setBounds(new Rectangle(415, 25, 255, 20));
    cmbTipoInforme.setBackground(new Color (255,255,255));
    cmbTipoInforme.addItem("");
    cmbTipoInforme.addItem("Vias");
//  cmbTipoInforme.addItem("Diámetros");
//  cmbTipoInforme.addItem("Antigüedad");
    
    this.setSize(750, 600);
    this.setLayout(null);   
    this.add(lblTipoInforme, null);
    this.add(lblImagen, null);
    this.add(cmbTipoInforme, null);
    
cmbTipoInforme.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt){
            wizardContext.inputChanged(); 
        };

      });   
  }

  public void enteredFromLeft(Map dataMap)
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
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        GeopistaInformacionReferenciaVias geo = new GeopistaInformacionReferenciaVias();
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
      return " ";
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
     if (cmbTipoInforme.getSelectedIndex()==0){
        return false;
     }else{
        return true;
     }
    }
    private WizardContext wizardContext;
 public void setWizardContext(WizardContext wd)
    {
      wizardContext=wd;
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
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
    
}