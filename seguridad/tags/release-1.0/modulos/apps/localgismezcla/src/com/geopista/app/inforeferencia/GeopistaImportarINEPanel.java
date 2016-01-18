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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class GeopistaImportarINEPanel extends JPanel implements WizardPanel
{

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JScrollPane scpErrores = new JScrollPane();
  private JLabel lblImagen = new JLabel();
  private JLabel lblFichero = new JLabel();
  private JTextField txtFichero = new JTextField();
  private JButton btnAbrir = new JButton();
  private JLabel lblErrores = new JLabel();
    
	private javax.swing.JLabel jLabel = null;
  private JLabel lblTipoFichero = new JLabel();
  private JComboBox cmbTipoInfo = new JComboBox();
  private JEditorPane ediError = new JEditorPane();

  public GeopistaImportarINEPanel()
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
    
  /*public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Importar topónimos y otros ficheros");
    GeopistaImportarToponimos geopistaImportar = new GeopistaImportarToponimos();

    frame1.getContentPane().add(geopistaImportar);
    frame1.setSize(750, 600);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
 
  }*/

  private void jbInit() throws Exception
  {
    this.setLayout(null);
   
    scpErrores.setBounds(new Rectangle(135, 130, 595, 380));

    cmbTipoInfo.addItem("Datos de Pseudovías");
    cmbTipoInfo.addItem("Datos de Tramos de Vías");
    cmbTipoInfo.addItem("Datos de Unidades Poblaciones");
    cmbTipoInfo.addItem("Datos de Vías");
    cmbTipoInfo.addItem("Cartografía");
    
    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
          
    lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    
    lblFichero.setText("Fichero a importar:");
    lblFichero.setBounds(new Rectangle(136, 55, 140, 20));
    
    txtFichero.setBounds(new java.awt.Rectangle(283,55,365,20));
    
    btnAbrir.setBounds(new java.awt.Rectangle(663,53,35,25));
    btnAbrir.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAbrir_actionPerformed(e);
        }
      });
      
    lblErrores.setText("Errores de validación:");
    lblErrores.setBounds(new java.awt.Rectangle(136,105,175,20));

    lblTipoFichero.setText("Tipo de Fichero:");
    lblTipoFichero.setBounds(new Rectangle(136, 22, 140, 20));
    
    cmbTipoInfo.setBackground(new Color(255,255,255));
    cmbTipoInfo.setBounds(new Rectangle(283, 20, 290, 20));
    
    cmbTipoInfo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbTipoFichero_actionPerformed(e);
        }
      });
    ediError.setText("jEditorPane1");
    ediError.setContentType("text/html");
    ediError.setEditable(false);
    
    //cmbTipoInfo.setBounds(283, 22, 290, 20);
    
    this.setSize(750,600);    
    this.add(cmbTipoInfo, null);
    this.add(lblTipoFichero, null);
    this.add(lblErrores, null);
    this.add(btnAbrir, null);
    this.add(txtFichero, null);
    this.add(lblFichero, null);
    this.add(lblImagen, null);

    scpErrores.getViewport().add(ediError, null);
    this.add(scpErrores, null);
//    this.add(getJLabel(), null);
   
  }//jbinit

public void enteredFromLeft(Map dataMap)
  {
    
  }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     
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
  private void btnAbrir_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser(); 
    GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
    int valor_combo = cmbTipoInfo.getSelectedIndex();
    if((valor_combo == 0)||(valor_combo == 1)||(valor_combo == 2)||(valor_combo == 3))
    {
      filter.addExtension("txt");
      filter.setDescription("Ficheros de texto");
    }
    else
    {
      filter.addExtension("e00");
      filter.setDescription("Ficheros E00");
    }
    /*filter.addExtension("dxf");
    filter.setDescription("Ficheros dxf");*/
    fc.setFileFilter(filter);
    fc.setAcceptAllFileFilterUsed(false); //  QUITA LA OPCION ALL FILES(*.*)
   
    int returnVal = fc.showOpenDialog(this); 
    if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fichero;
            fichero=fc.getSelectedFile().getPath();
            this.txtFichero.setText(fichero); //meto el fichero seleccionado en el campo
            String cadenaTexto;
            cadenaTexto = "<font face=SansSerif size=3>Comenzando la importación de: <b>" + fc.getSelectedFile().getName()+"</b>";
            cadenaTexto = cadenaTexto + "<p>Esta operación puede durar varios minutos ...</p></font>";
            
            cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#009900>Importación finalizada</font></p>";
            ediError.setText(cadenaTexto);
     }
   
  }

  private void cmbTipoFichero_actionPerformed(ActionEvent e)
  {
     txtFichero.setText("");
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

  /*	LO QUITO PQ DA ERROR/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	/*private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setBounds(138, 22, 138, 19);
			jLabel.setText("Tipo de fichero :");
		}
		return jLabel;
	}*/

 }
