/**
 * CentroEnsenianzaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.panels.CentroEnsenianzaPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CentroEnsenianzaDialog extends JDialog
{

    
    private CentroEnsenianzaPanel centroEnsenianzaPanel = null;   
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 690;
    public static final int DIM_Y = 700;
        
    private OKCancelPanel getOkCancelPanel()
    {
        if (_okCancelPanel == null)
        {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    
                    if (_okCancelPanel.wasOKPressed() && isEditable)
                    {
                        if(getCentroEnsenianzaPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getCentroEnsenianzaPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CentroEnsenianzaDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getCentroEnsenianzaPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            CentroEnsenianzaDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CentroEnsenianzaDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
                    }
                    dispose();                    
                }
                    });
        }
        return _okCancelPanel;
    }
    
    /**
     * This method initializes
     * 
     */
    public CentroEnsenianzaDialog(CentrosEnsenianzaEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.en"));       
        this.isEditable = isEditable;
        getCentroEnsenianzaPanel().loadData (elemento);
        if (getCentroEnsenianzaPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getCentroEnsenianzaPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getCentroEnsenianzaPanel(), isEditable);
        	getCentroEnsenianzaPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getCentroEnsenianzaPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        
        this.setLocationRelativeTo(null);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getCentroEnsenianzaPanel().getJPanelDatosIdentificacion(), false);
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getCentroEnsenianzaPanel().getJTextFieldClave().setEnabled(false);
        getCentroEnsenianzaPanel().getJComboBoxProvincia().setEnabled(false);
        getCentroEnsenianzaPanel().getJComboBoxMunicipio().setEnabled(false);
    }
    
    public CentroEnsenianzaDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public CentroEnsenianzaDialog(boolean isEditable)
    {
        this(null, isEditable);
        this.setLocationRelativeTo(null);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title)
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setModal(true);
        this.setContentPane(getCentroEnsenianzaPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(title);
        this.setResizable(true);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter()
                {
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                dispose();
            }
                });              
    }    
   
    public static void main(String[] args)
    {
        CentroEnsenianzaDialog dialog = 
            new CentroEnsenianzaDialog();
        dialog.setVisible(true);
        
    }
    
    public CentroEnsenianzaPanel getCentroEnsenianzaPanel()
    {
        if (centroEnsenianzaPanel == null)
        {
        	centroEnsenianzaPanel = new CentroEnsenianzaPanel(new GridBagLayout());
        	centroEnsenianzaPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return centroEnsenianzaPanel;
    }
    
    public CentrosEnsenianzaEIEL getCentroEnsenianza(CentrosEnsenianzaEIEL elemento)
    {
    	return getCentroEnsenianzaPanel().getCentroEnsenianza(elemento);
    }
    
}
