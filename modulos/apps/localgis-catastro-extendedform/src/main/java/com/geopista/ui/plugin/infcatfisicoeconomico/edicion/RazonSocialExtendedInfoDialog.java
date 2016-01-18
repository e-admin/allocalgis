/**
 * RazonSocialExtendedInfoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.infcatfisicoeconomico.edicion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.RazonSocialExtendedInfoPanel;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.ui.plugin.infcatfisicoeconomico.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class RazonSocialExtendedInfoDialog extends JDialog
{

    
    private RazonSocialExtendedInfoPanel razonSocialExtendedInfoPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 810;
    public static final int DIM_Y = 350;
    
    public static final int MODO_TITULAR = 0;
    public static final int MODO_REPRESENTANTE=1;
    public static final int MODO_COMUNIDAD_BIENES=2;
    
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
                        if(getRazonSocialExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getRazonSocialExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        RazonSocialExtendedInfoDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.norecupera.persona"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getRazonSocialExtendedInfoPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            RazonSocialExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.novalida.persona"),
                                            JOptionPane.ERROR_MESSAGE);
                                }

                              //  return;
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(RazonSocialExtendedInfoDialog.this,
                                    I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
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
    public RazonSocialExtendedInfoDialog(Persona pers, int mode, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        if (mode == MODO_REPRESENTANTE)
            initialize(I18N.get("Expedientes", "razonsocial.extended.dialog.titulo.representante"));
        else if (mode == MODO_TITULAR)
            initialize(I18N.get("Expedientes", "razonsocial.extended.dialog.titulo.titular"));
        else if (mode == MODO_COMUNIDAD_BIENES)
            initialize(I18N.get("Expedientes", "razonsocial.extended.dialog.titulo.cb"));
       
        this.isEditable = isEditable;
        getRazonSocialExtendedInfoPanel().loadData (pers);
        EdicionUtils.enablePanel(getRazonSocialExtendedInfoPanel(), isEditable);
    }
    
    public RazonSocialExtendedInfoDialog(int mode)
    {
        this(mode, false);
    }
    public RazonSocialExtendedInfoDialog(int mode, boolean isEditable)
    {
        this(null, mode, isEditable);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title)
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setModal(true);
        this.setContentPane(getRazonSocialExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(title);
        this.setResizable(true);
        this.getOkCancelPanel().setVisible(true);
        //this.getJPanelClose().setVisible(false);
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
        RazonSocialExtendedInfoDialog dialog = 
            new RazonSocialExtendedInfoDialog(MODO_TITULAR);
        dialog.setVisible(true);
        
    }
    
    public RazonSocialExtendedInfoPanel getRazonSocialExtendedInfoPanel()
    {
        if (razonSocialExtendedInfoPanel == null)
        {
            razonSocialExtendedInfoPanel = new RazonSocialExtendedInfoPanel(new GridBagLayout());
            razonSocialExtendedInfoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return razonSocialExtendedInfoPanel;
    }
    
    public Titular getTitular(Titular pers)
    {
    	return getRazonSocialExtendedInfoPanel().getRazonSocial(pers);
    }
    
}
