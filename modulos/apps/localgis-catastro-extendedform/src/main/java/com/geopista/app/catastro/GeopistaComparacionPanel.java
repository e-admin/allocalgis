/**
 * GeopistaComparacionPanel.java
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
//import com.vividsolutions.jump.workbench.ui.wizard.*;

public class GeopistaComparacionPanel extends JPanel implements WizardPanel
{

  private JLabel lblComparacion = new JLabel();
  private JLabel lblImagen = new JLabel();
  private JRadioButton rdbNif = new JRadioButton();
  private JRadioButton rdbReferencia = new JRadioButton();
  private JButton btnFinalizar = new JButton();
  private JLabel lblTermino = new JLabel();
  private JTextField txtNif = new JTextField();
  private JLabel lblGuion = new JLabel();
  private JTextField txtCaracterNif= new JTextField();
  private JTextField txtReferencia = new JTextField();

  public GeopistaComparacionPanel()
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
    lblComparacion.setText("Ver comparación IBI-PMH");
    lblComparacion.setBounds(new Rectangle(135, 20, 375, 20));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));

    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    rdbNif.setText("NIF Titular:");
    rdbNif.setBounds(new Rectangle(135, 100, 90, 20));
    rdbReferencia.setText("Referencia Catastral:");
    rdbReferencia.setBounds(new Rectangle(135, 130, 140, 25));

    ButtonGroup grupoOpciones = new ButtonGroup();
    lblTermino.setText("Introduzca el término de búsqueda:");
    lblTermino.setBounds(new Rectangle(135, 60, 275, 20));
    txtNif.setBounds(new Rectangle(225, 100, 100, 20));
    lblGuion.setText("-");
    lblGuion.setBounds(new Rectangle(325, 100, 10, 20));
    txtCaracterNif.setBounds(new Rectangle(330, 100, 20, 20));
    txtReferencia.setBounds(new Rectangle(290, 130, 170, 20));
    grupoOpciones.add(rdbNif);
    grupoOpciones.add(rdbReferencia);
    rdbNif.setSelected(true);
    btnFinalizar.setText("Finalizar");
    btnFinalizar.setBounds(new Rectangle(645, 530, 90, 25));
    /*btnSiguiente.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnSiguiente_actionPerformed(e);
        }
      });*/
   /* btnAnterior.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAnterior_actionPerformed(e);
        }
      });*/
    this.setSize(750, 600);
    this.add(txtReferencia, null);
    this.add(txtCaracterNif, null);
    this.add(lblGuion, null);
    this.add(txtNif, null);
    this.add(lblTermino, null);
    this.add(btnFinalizar, null);
    this.add(rdbReferencia, null);
    this.add(rdbNif, null);
    this.add(lblImagen, null);
    this.add(lblComparacion, null);
  }
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
      return " ";
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
  
  public static void main(String[] args)
  {
    JDialog dlgVentana = new JDialog();
    dlgVentana.setResizable(false);
    
    GeopistaComparacionPanel iniciar = new GeopistaComparacionPanel();

    dlgVentana.getContentPane().add(iniciar);
    dlgVentana.setSize(750, 600);
    dlgVentana.setVisible(true); 
    dlgVentana.setLocation(150, 90);
    dlgVentana.setTitle("Discrepancias de la Información");
    dlgVentana.setResizable(false);
    
  }

  private void btnSiguiente_actionPerformed(ActionEvent e)
  {
  }

  private void btnAnterior_actionPerformed(ActionEvent e)
  {
  }
/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
}