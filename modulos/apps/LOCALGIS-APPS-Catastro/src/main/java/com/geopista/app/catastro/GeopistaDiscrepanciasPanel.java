/**
 * GeopistaDiscrepanciasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import com.geopista.app.inicio.CatastroInicioExtended;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaDiscrepanciasPanel extends JPanel implements WizardPanel
{

  private JLabel lblLocalizar = new JLabel();
  private JLabel lblImagen = new JLabel();
  private JRadioButton rdbInformacioncatastral= new JRadioButton();
  private JRadioButton rdbIbi = new JRadioButton();

  public GeopistaDiscrepanciasPanel()
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
    lblLocalizar.setText("Localizar errores y discrepancias de la información:");
    lblLocalizar.setBounds(new Rectangle(135, 20, 375, 20));

    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    rdbInformacioncatastral.setText("Ver errores información catastral");
    rdbInformacioncatastral.setBounds(new Rectangle(135, 60, 260, 20));
    rdbIbi.setText("Ver comparación IBI-PMH");
    rdbIbi.setBounds(new Rectangle(135, 90, 280, 25));

    ButtonGroup grupoOpciones = new ButtonGroup();
    grupoOpciones.add(rdbInformacioncatastral);
    grupoOpciones.add(rdbIbi);
    rdbInformacioncatastral.setSelected(true);
    /*btnSiguiente.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnSiguiente_actionPerformed(e);
        }
      });*/
  
    this.setSize(750, 600);
    this.add(rdbIbi, null);
    this.add(rdbInformacioncatastral, null);
    this.add(lblImagen, null);
    this.add(lblLocalizar, null);
  }
  
    public void enteredFromLeft(Map dataMap)
  {
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
//     System.out.println("saliendo de panel 1"); 
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
      return "";
    }

    public String getID()
    {
      return "1";
    }

    public String getInstructions()
    {
     return "";
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
      if(nextID==null)
      {
        if (rdbInformacioncatastral.isSelected()==true){
          return "2";
        }else if (rdbIbi.isSelected()==true){
          return null;
        }else
        {
          return null;
        }
      }
      else
      {
        return nextID;
      }
    }
      public void setWizardContext(WizardContext wd)
    {
    }
  

  private void btnFinalizar_actionPerformed(ActionEvent e)
  {
    CatastroInicioExtended Ini = new CatastroInicioExtended();
    this.setVisible(false);
    JDialog Dialog = (JDialog) SwingUtilities.getWindowAncestor(this );
    Dialog.setVisible(false);

    Ini.setVisible(true);
    //Ini.displayPage("E:/Trabajos/SIT FEMP/Desarrollo/geopista/Browser/Catastro.htm");
  }
/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

 /*  public static void main(String[] args)
  {
    JDialog dlgVentana = new JDialog();
   // dlgVentana.setTitle("Localización de Errores");
    dlgVentana.setResizable(false);    
    GeopistaDiscrepanciasPanel ventana = new GeopistaDiscrepanciasPanel();
    dlgVentana.getContentPane().add(ventana);
    dlgVentana.setSize(750, 600);
    dlgVentana.setVisible(true); 
    dlgVentana.setLocation(150, 90);
    
  }

  private void btnSiguiente_actionPerformed(ActionEvent e)
  {
  }*/

  
}