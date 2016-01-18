/**
 * LocalComunExtendedInfoDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion;


import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.LocalExtendedInfoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.vividsolutions.jump.I18N;



public class LocalComunExtendedInfoDialog extends JDialog
{   
    private LocalExtendedInfoPanel localPanel = null;
    
    private OKDistributionCancelPanel _botoneraPanel = null;
   
    public static final int DIM_X = 550;
    public static final int DIM_Y = 500;
    
    private boolean isEditable = false;
    private ConstruccionCatastro construccion = null;
    private RepartoCatastro reparto = null;
    
    private OKDistributionCancelPanel getBotoneraPanel()
    {
    
        if (_botoneraPanel == null)
        {
            _botoneraPanel = new OKDistributionCancelPanel(isEditable);
            _botoneraPanel.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    
                    if (_botoneraPanel.wasOKPressed() && isEditable)
                    {
                        if(getLocalExtendedInfoPanel().datosMinimosYCorrectosComun())
                        {
                            if(reparto!=null || (construccion.getDatosEconomicos().getCodModalidadReparto()!=null &&
                                !construccion.getDatosEconomicos().getCodModalidadReparto().equalsIgnoreCase("")))
                            {
                                String errorMessage = getLocalExtendedInfoPanel().validateInput();

                                if (errorMessage != null)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            LocalComunExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.norecupera.local"),
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else
                                {
                                    try
                                    {
                                        getLocalExtendedInfoPanel().okPressed();
                                    }
                                    catch (Exception e1)
                                    {
                                        JOptionPane
                                        .showMessageDialog(
                                                LocalComunExtendedInfoDialog.this,
                                                errorMessage,
                                                I18N.get("Expedientes", "mensajes.novalida.local"),
                                                JOptionPane.ERROR_MESSAGE);
                                    }

                                //    return;
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(LocalComunExtendedInfoDialog.this,
                                        I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgRepartoElementoComun"));
                                return;
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(LocalComunExtendedInfoDialog.this,
                                    I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
                            return;
                        }
                    }
                    dispose();                    
                }
                    });            
            
            _botoneraPanel.getJButtonReparto().addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    if(getLocalExtendedInfoPanel().datosMinimosYCorrectosComun())
                    {
                        construccion = getLocalExtendedInfoPanel().datosMinimosParaReparto(construccion);
                        RepartoExtendedInfoDialog dialog =
                            new RepartoExtendedInfoDialog(construccion, isEditable);
                        dialog.setVisible(true);

                        //reparto = dialog.getReparto();
                        reparto = dialog.getReparto(construccion);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(LocalComunExtendedInfoDialog.this,I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
                    }
                }
                    });            
        }
        return _botoneraPanel;
    }
    
    /**
     * This method initializes
     * 
     */
    public LocalComunExtendedInfoDialog(ConstruccionCatastro construc, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize();        
        this.isEditable = isEditable;
        this.construccion = construc;
        _botoneraPanel.setEditable(isEditable);
        getLocalExtendedInfoPanel().loadData(construc);
        EdicionUtils.enablePanel(getLocalExtendedInfoPanel(), isEditable);
        EdicionUtils.enableComponent(getLocalExtendedInfoPanel(), false, "numerocargo");
    }
    
    public LocalComunExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    
    public LocalComunExtendedInfoDialog()
    {
        this(false);
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
        this.setContentPane(getLocalExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "local.extended.dialog.titulo"));
        this.setResizable(true);
        this.getBotoneraPanel().setVisible(true);
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
        LocalComunExtendedInfoDialog feif = new LocalComunExtendedInfoDialog();
        feif.setVisible(true);
        
    }
    
    private LocalExtendedInfoPanel getLocalExtendedInfoPanel()
    {
        if (localPanel == null)
        {
        	localPanel = new LocalExtendedInfoPanel();
        	localPanel.add(getBotoneraPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return localPanel;
    }
    
    public ConstruccionCatastro getConstruccionCatastro(ConstruccionCatastro cons)
    {
        return getLocalExtendedInfoPanel().getConstruccionCatastro(cons);
    }
    
    public ConstruccionCatastro getConstruccionCatastro(ConstruccionCatastro cons, String tipoMovimiento)
    {
        return getLocalExtendedInfoPanel().getConstruccionCatastro(cons, tipoMovimiento);
    }
    
    public RepartoCatastro getReparto()
    {
    	return reparto;
    }
}
