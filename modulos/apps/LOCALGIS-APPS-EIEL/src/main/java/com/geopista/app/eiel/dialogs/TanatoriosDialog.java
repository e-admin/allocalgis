/**
 * TanatoriosDialog.java
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
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.panels.TanatoriosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class TanatoriosDialog extends JDialog
{

    
    private TanatoriosPanel tanatoriosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 780;
    public static final int DIM_Y = 600;
        
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
                        if(getTanatoriosPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getTanatoriosPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		TanatoriosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getTanatoriosPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		TanatoriosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(TanatoriosDialog.this,
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
    public TanatoriosDialog(TanatoriosEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.ta"));       
        this.isEditable = isEditable;
        getTanatoriosPanel().loadData (elemento);
        if (getTanatoriosPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getTanatoriosPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getTanatoriosPanel(), isEditable);
        	getTanatoriosPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getTanatoriosPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getTanatoriosPanel().getjTextFieldClave().setEnabled(false);
        getTanatoriosPanel().getJComboBoxProvincia().setEnabled(false);
        getTanatoriosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public TanatoriosDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public TanatoriosDialog(boolean isEditable)
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
        this.setContentPane(getTanatoriosPanel());
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
    	TanatoriosDialog dialog = 
            new TanatoriosDialog();
        dialog.setVisible(true);
        
    }
    
    public TanatoriosPanel getTanatoriosPanel()
    {
        if (tanatoriosPanel == null)
        {
        	tanatoriosPanel = new TanatoriosPanel(new GridBagLayout());
        	tanatoriosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return tanatoriosPanel;
    }
    
    public TanatoriosEIEL getTanatorios(TanatoriosEIEL elemento)
    {
    	return getTanatoriosPanel().getTanatorios(elemento);
    }
    
}
