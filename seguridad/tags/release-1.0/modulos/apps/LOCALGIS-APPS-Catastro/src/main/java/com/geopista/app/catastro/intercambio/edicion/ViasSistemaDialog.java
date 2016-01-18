package com.geopista.app.catastro.intercambio.edicion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.ViasSistemaPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class ViasSistemaDialog extends JDialog
{
    private OKCancelPanel _okCancelPanel = null;
    
    private ViasSistemaPanel viasSistemaPanel;
    private String nomVia = "";
    private String tipoVia = "";
    private String idMunicipio = "";
    


	public static final int DIM_X = 400;
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
                        String errorMessage = getViasSistemaPanel().validateInput();
                        
                        if (errorMessage != null)
                        {
                            JOptionPane
                            .showMessageDialog(
                                    ViasSistemaDialog.this,
                                    errorMessage,
                                    I18N.get("Expedientes", "mensajes.novalida.vias"),
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        } else
                        {
                            try
                            {
                                getViasSistemaPanel().okPressed();
                            } 
                            catch (Exception e1)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        ViasSistemaDialog.this,
                                        errorMessage,
                                        I18N.get("Expedientes", "mensajes.norecupera.vias"),
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
    public ViasSistemaDialog(String nomVia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        initialize();
        
    }
    
    public ViasSistemaDialog(String nomVia, String tipoVia)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        initialize();
        
    }
    
    public ViasSistemaDialog(String nomVia, String tipoVia, String idMunicipio)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        this.nomVia = nomVia;
        this.tipoVia = tipoVia;
        this.idMunicipio = idMunicipio;
        initialize();
        
    }
    
    public ViasSistemaDialog()
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
        this.setContentPane(getViasSistemaPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(I18N.get("Expedientes", "busqueda.vias.dialog.titulo"));
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
        ViasSistemaDialog dialog = new ViasSistemaDialog();
        dialog.setVisible(true);        
    }    
    
    private ViasSistemaPanel getViasSistemaPanel()
    {
        if (viasSistemaPanel == null)
        {
        	viasSistemaPanel = new ViasSistemaPanel(new GridBagLayout(), nomVia, tipoVia);
            viasSistemaPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));
        }
        return viasSistemaPanel;
    }  
    
    public String getVia()
    {
    	return getViasSistemaPanel().getNombreVia();
    }
    
    public String getTipoVia() {
		return getViasSistemaPanel().getTipoVia();
	}
    
    public int getCodigoVia(){
    	return getViasSistemaPanel().getCodigoVia();
    }

}
