package com.geopista.ui.plugin.infcattitularidad.edicion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.BienInmuebleExtendedInfoPanel;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.ui.plugin.infcattitularidad.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class BienInmuebleExtendedInfoDialog extends JDialog
{
    
    private BienInmuebleExtendedInfoPanel bienInmuebleExtendedInfoPanel = null;
    
    private OKCancelPanel _okCancelPanel = null;
   
    public static final int DIM_X = 780;
    public static final int DIM_Y = 650;
    
    private boolean isEditable = false;

	private TipoExpediente tipoExpediente = null;
    
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
                        if(getBienInmuebleExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getBienInmuebleExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        BienInmuebleExtendedInfoDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.novalida.bien"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getBienInmuebleExtendedInfoPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            BienInmuebleExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.norecupera.bien"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(BienInmuebleExtendedInfoDialog.this,
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
    public BienInmuebleExtendedInfoDialog(BienInmuebleCatastro bi, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize();
        this.isEditable = isEditable;
        //Carga los valores en los campos correspondientes si el bean 
        //BienInmuebleCatastro tiene datos
        getBienInmuebleExtendedInfoPanel().loadData(bi);
        EdicionUtils.enablePanel(getBienInmuebleExtendedInfoPanel(), isEditable);
    }
    
    public BienInmuebleExtendedInfoDialog(BienInmuebleCatastro bi, boolean isEditable, TipoExpediente tipoExpediente)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.tipoExpediente  = tipoExpediente;
        initialize();
        this.isEditable = isEditable;
        //Carga los valores en los campos correspondientes si el bean 
        //BienInmuebleCatastro tiene datos
        getBienInmuebleExtendedInfoPanel().loadData(bi);
        EdicionUtils.enablePanel(getBienInmuebleExtendedInfoPanel(), isEditable);
    }
    
    public BienInmuebleExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    public BienInmuebleExtendedInfoDialog()
    {
        this (false);
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
        this.setContentPane(getBienInmuebleExtendedInfoPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "bieninmueble.extended.dialog.titulo"));
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
        BienInmuebleExtendedInfoDialog dialog = new BienInmuebleExtendedInfoDialog();
        dialog.setVisible(true);
        
    }
    
    private BienInmuebleExtendedInfoPanel getBienInmuebleExtendedInfoPanel()
    {
        if (bienInmuebleExtendedInfoPanel == null)
        {
            bienInmuebleExtendedInfoPanel = new BienInmuebleExtendedInfoPanel(new GridBagLayout(), this.tipoExpediente);
            bienInmuebleExtendedInfoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return bienInmuebleExtendedInfoPanel;
    }
    
    public BienInmuebleJuridico getBienInmueble(BienInmuebleJuridico biJ)
    {
    	return getBienInmuebleExtendedInfoPanel().getBienInmueble(biJ);
    }
    
}
