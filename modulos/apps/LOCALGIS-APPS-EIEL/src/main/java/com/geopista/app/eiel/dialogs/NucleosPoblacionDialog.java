/**
 * NucleosPoblacionDialog.java
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
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.panels.NucleosPoblacionPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class NucleosPoblacionDialog extends JDialog
{

    
    private NucleosPoblacionPanel nucleosPoblacionPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 950;
    public static final int DIM_Y = 400;
        
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
                        if(getNucleosPoblacionPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getNucleosPoblacionPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		NucleosPoblacionDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getNucleosPoblacionPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		NucleosPoblacionDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(NucleosPoblacionDialog.this,
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
    public NucleosPoblacionDialog(NucleosPoblacionEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.np"));       
        this.isEditable = isEditable;
        getNucleosPoblacionPanel().loadData (elemento);
        EdicionUtils.enablePanel(getNucleosPoblacionPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getNucleosPoblacionPanel().getJPanelDatosIdentificacion(), false);
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        //getNucleosAbandonadosPanel().getjTextFieldClave().setEnabled(false);
        getNucleosPoblacionPanel().getJComboBoxProvincia().setEnabled(false);
        getNucleosPoblacionPanel().getJComboBoxMunicipio().setEnabled(false);
        
    }
    
    public NucleosPoblacionDialog()
    {
        this(false);
    }
    public NucleosPoblacionDialog(boolean isEditable)
    {
        this(null, isEditable);
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
        this.setContentPane(getNucleosPoblacionPanel());
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
        NucleosPoblacionDialog dialog = 
            new NucleosPoblacionDialog();
        dialog.setVisible(true);
        
    }
    
    public NucleosPoblacionPanel getNucleosPoblacionPanel()
    {
        if (nucleosPoblacionPanel == null)
        {
        	nucleosPoblacionPanel = new NucleosPoblacionPanel(new GridBagLayout());
        	nucleosPoblacionPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return nucleosPoblacionPanel;
    }
    
    public NucleosPoblacionEIEL getNucleosPoblacion(NucleosPoblacionEIEL elemento)
    {
    	return getNucleosPoblacionPanel().getNucleosPoblacion(elemento);
    }
    
}
