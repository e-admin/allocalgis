package com.geopista.app.catastro.intercambio.edicion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.CodigoRegistroPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CodigoRegistroDialog extends JDialog
{
    private OKCancelPanel _okCancelPanel = null;
    
    private String codigoProvincia;
    
    private CodigoRegistroPanel codigoRegistroPanel;
    
    public static final int DIM_X = 240;
    public static final int DIM_Y = 300;
    
    private OKCancelPanel getOkCancelPanel()
    {
        if (_okCancelPanel == null)
        {
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    
                    if (_okCancelPanel.wasOKPressed())
                    {
                        String errorMessage = getCodigoRegistroPanel().validateInput();
                        
                        if (errorMessage != null)
                        {
                            JOptionPane
                            .showMessageDialog(
                                    CodigoRegistroDialog.this,
                                    errorMessage,
                                    I18N.get("Expedientes", "mensajes.novalida.codigoregistro"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else
                        {
                            try
                            {
                                getCodigoRegistroPanel().okPressed();
                            } 
                            catch (Exception e1)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CodigoRegistroDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.norecupera.codigoregistro"),
                                        JOptionPane.ERROR_MESSAGE);
                            }                            
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
    public CodigoRegistroDialog(String nomCodigoProvincia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.codigoProvincia = nomCodigoProvincia;
        initialize();
        
    }
        
    
    public CodigoRegistroDialog()
    {
        this(null);
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
        this.setContentPane(getCodigoRegistroPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setLocationRelativeTo(null);
        this.setTitle(I18N.get("Expedientes", "busqueda.codigoregistro.dialog.titulo"));
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
        CodigoRegistroDialog dialog = new CodigoRegistroDialog();
        dialog.setVisible(true);        
    }    
    
    private CodigoRegistroPanel getCodigoRegistroPanel()
    {
        if (codigoRegistroPanel == null)
        {
            
        	codigoRegistroPanel = new CodigoRegistroPanel(new GridBagLayout(), this.codigoProvincia);
        	codigoRegistroPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return codigoRegistroPanel;
    }  
    
    public String getCodigoRegistro()
    {
    	return getCodigoRegistroPanel().getCodigoRegistro();
    }
}
