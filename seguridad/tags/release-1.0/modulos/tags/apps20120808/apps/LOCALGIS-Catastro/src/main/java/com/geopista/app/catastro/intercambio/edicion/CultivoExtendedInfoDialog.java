package com.geopista.app.catastro.intercambio.edicion;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.CultivoExtendedInfoPanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.LocalExtendedInfoPanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CultivoExtendedInfoDialog extends JDialog
{   
    private CultivoExtendedInfoPanel cultivoPanel = null;
    
    private OKCancelPanel _okCancelPanel = null;
   
    public static final int DIM_X = 600;
    public static final int DIM_Y = 500;
    
    private boolean isEditable = false;
    
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
                        if(getCultivoExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getCultivoExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CultivoExtendedInfoDialog.this,
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
                                            CultivoExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.novalida.cultivo"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CultivoExtendedInfoDialog.this,
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
    public CultivoExtendedInfoDialog(Cultivo cultivo, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize();        
        this.isEditable = isEditable;
        getCultivoExtendedInfoPanel().loadData(cultivo);
        EdicionUtils.enablePanel(getCultivoExtendedInfoPanel(), isEditable);
    }
    
    public CultivoExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    
    public CultivoExtendedInfoDialog()
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
        CultivoExtendedInfoDialog feif = new CultivoExtendedInfoDialog();
        feif.setVisible(true);
        
    }
    
    private CultivoExtendedInfoPanel getCultivoExtendedInfoPanel()
    {
        if (cultivoPanel == null)
        {
        	cultivoPanel = new CultivoExtendedInfoPanel();
        	cultivoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return cultivoPanel;
    }
    
    public Cultivo getCultivo(Cultivo cultivo)
    {
    	return getCultivoExtendedInfoPanel().getCultivo(cultivo);
    }
}
