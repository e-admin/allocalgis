/**
 * FincaExtendedInfoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.FincaExtendedInfoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class FincaExtendedInfoDialog extends JDialog
{   
    private FincaExtendedInfoPanel fincaExtendedInfoPanel = null;
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;
    
    public static final int DIM_X = 780;
    public static final int DIM_Y = 500;
    
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
                        if(getFincaExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            getFincaExtendedInfoPanel().okPressed(true);

                            String errorMessage = getFincaExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        FincaExtendedInfoDialog.this,
                                        I18N.get("ValidacionMensajesError", errorMessage),
                                        errorMessage,
                                        JOptionPane.ERROR_MESSAGE);

                                getFincaExtendedInfoPanel().okPressed(false);

                                return;
                            } else
                            {
                                try
                                {
                                    getFincaExtendedInfoPanel().okPressed(true);
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            FincaExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.norecupera.finca"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(FincaExtendedInfoDialog.this,
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
     */
    public FincaExtendedInfoDialog(FincaCatastro fc, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize();
        this.isEditable = isEditable;
        
        //Carga los valores en los campos correspondientes si el bean 
        //FincaCatastro tiene datos
        getFincaExtendedInfoPanel().loadData(fc);
        EdicionUtils.enablePanel(getFincaExtendedInfoPanel(), isEditable);
    }   
    
    public FincaExtendedInfoDialog(FincaCatastro fc, boolean isEditable, boolean isNew)
    {
    	super(AppContext.getApplicationContext().getMainFrame());
        initialize(isNew);
        this.isEditable = isEditable;
        
        //Carga los valores en los campos correspondientes si el bean 
        //FincaCatastro tiene datos
        getFincaExtendedInfoPanel().loadData(fc);
        EdicionUtils.enablePanel(getFincaExtendedInfoPanel(), isEditable);
    }   
   
    public FincaExtendedInfoDialog()
    {
        this(false);
    }
    public FincaExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setModal(true);
        this.setContentPane(getFincaExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "finca.extended.dialog.titulo"));
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
    
    private void initialize(boolean isNew)
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setModal(true);
        this.setContentPane(getFincaExtendedInfoPanel(isNew));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "finca.extended.dialog.titulo"));
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
        FincaExtendedInfoDialog feif = new FincaExtendedInfoDialog();
        feif.setVisible(true);
        
    }
    
    private FincaExtendedInfoPanel getFincaExtendedInfoPanel()
    {
        if (fincaExtendedInfoPanel == null)
        {
            fincaExtendedInfoPanel = new FincaExtendedInfoPanel(new GridBagLayout());
            fincaExtendedInfoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return fincaExtendedInfoPanel;
    }
    
    private FincaExtendedInfoPanel getFincaExtendedInfoPanel(boolean isNew)
    {
        if (fincaExtendedInfoPanel == null)
        {
            fincaExtendedInfoPanel = new FincaExtendedInfoPanel(new GridBagLayout(),isNew);
            fincaExtendedInfoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return fincaExtendedInfoPanel;
    }
    
    public FincaCatastro getFincaCatastro(FincaCatastro finca)
    {
        return getFincaExtendedInfoPanel().getFincaCatastro(finca);
    }
}
