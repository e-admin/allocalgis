/**
 * CultivoComunExtendedInfoDialog.java
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
import com.geopista.app.catastro.intercambio.edicion.dialogs.CultivoExtendedInfoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.vividsolutions.jump.I18N;



public class CultivoComunExtendedInfoDialog extends JDialog
{   
    private CultivoExtendedInfoPanel cultivoPanel = null;
    
    private OKDistributionCancelPanel _botoneraPanel = null;
   
    public static final int DIM_X = 600;
    public static final int DIM_Y = 500;
    
    private boolean isEditable = false;
    private Cultivo cultivo = null;
    
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
                        if(getCultivoExtendedInfoPanel().datosMinimosYCorrectosComun())
                        {
                            if(reparto!=null || (cultivo.getCodModalidadReparto()!=null &&
                                !cultivo.getCodModalidadReparto().equalsIgnoreCase("")))
                            {
                                String errorMessage = getCultivoExtendedInfoPanel().validateInput();

                                if (errorMessage != null)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            CultivoComunExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.norecupera.cultivo"),
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else
                                {
                                    try
                                    {
                                        getCultivoExtendedInfoPanel().okPressed();
                                    }
                                    catch (Exception e1)
                                    {
                                        JOptionPane
                                        .showMessageDialog(
                                                CultivoComunExtendedInfoDialog.this,
                                                errorMessage,
                                                I18N.get("Expedientes", "mensajes.novalida.cultivo"),
                                                JOptionPane.ERROR_MESSAGE);
                                    }

                                   // return;
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(CultivoComunExtendedInfoDialog.this,
                                        I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgRepartoElementoComun"));
                                return;
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CultivoComunExtendedInfoDialog.this,
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
                	if(getCultivoExtendedInfoPanel().datosMinimosYCorrectosComun())
                        {
                            cultivo = getCultivoExtendedInfoPanel().datosMinimosParaReparto(cultivo);
                             RepartoExtendedInfoDialog dialog =
                                 new RepartoExtendedInfoDialog(cultivo, isEditable);
                             dialog.setVisible(true);

                             //reparto = dialog.getReparto();
                             reparto = dialog.getReparto(cultivo);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CultivoComunExtendedInfoDialog.this,I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.msgDatosMinimosYCorrectos"));
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
    public CultivoComunExtendedInfoDialog(Cultivo cultivo, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        cultivoPanel=getCultivoExtendedInfoPanel();
        cultivoPanel.loadData(cultivo);
        cultivoPanel.updateUI();
        this.isEditable = isEditable;
        this.cultivo = cultivo;
        initialize();  
        EdicionUtils.enablePanel(getCultivoExtendedInfoPanel(), isEditable);
        EdicionUtils.enableComponent(getCultivoExtendedInfoPanel(), false, "numeroorden");
    }
    
    public CultivoComunExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    
    public CultivoComunExtendedInfoDialog()
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
        this.setContentPane(getCultivoExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "cultivo.extended.dialog.titulo"));
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
        CultivoComunExtendedInfoDialog feif = new CultivoComunExtendedInfoDialog();
        feif.setVisible(true);
        
    }
    
    private CultivoExtendedInfoPanel getCultivoExtendedInfoPanel()
    {
        if (cultivoPanel == null)
        {
        	cultivoPanel = new CultivoExtendedInfoPanel();
        	cultivoPanel.add(getBotoneraPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return cultivoPanel;
    }
    
    public Cultivo getCultivo(Cultivo cult)
    {
    	return getCultivoExtendedInfoPanel().getCultivo(cult);
    }
    
    public Cultivo getCultivo(Cultivo cult, String tipoMovimiento)
    {
    	return getCultivoExtendedInfoPanel().getCultivo(cult, tipoMovimiento);
    }
    
    public RepartoCatastro getReparto()
    {
    	return reparto;
    }
    

}
