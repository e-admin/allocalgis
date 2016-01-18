package com.geopista.ui.plugin.infcatfisicoeconomico.edicion;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.LocalExtendedInfoPanel;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.ui.plugin.infcatfisicoeconomico.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class LocalExtendedInfoDialog extends JDialog
{   
    private LocalExtendedInfoPanel localPanel = null;
    
    private OKCancelPanel _okCancelPanel = null;
   
    public static final int DIM_X = 550;
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
                        if(getLocalExtendedInfoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getLocalExtendedInfoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        LocalExtendedInfoDialog.this,
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
                                            LocalExtendedInfoDialog.this,
                                            errorMessage,
                                            I18N.get("Expedientes", "mensajes.novalida.local"),
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(LocalExtendedInfoDialog.this,
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
    public LocalExtendedInfoDialog(ConstruccionCatastro construc, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        initialize();        
        this.isEditable = isEditable;
        getLocalExtendedInfoPanel().loadData(construc);
        EdicionUtils.enablePanel(getLocalExtendedInfoPanel(), isEditable);
//    	EdicionUtils.enableComponent(getLocalExtendedInfoPanel(), false, "tipovalor");
    }
    
    public LocalExtendedInfoDialog(boolean isEditable)
    {
        this(null, isEditable);
    }
    
    public LocalExtendedInfoDialog()
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
        LocalExtendedInfoDialog feif = new LocalExtendedInfoDialog();
        feif.setVisible(true);
        
    }
    
    private LocalExtendedInfoPanel getLocalExtendedInfoPanel()
    {
        if (localPanel == null)
        {
        	localPanel = new LocalExtendedInfoPanel();
        	localPanel.add(getOkCancelPanel(), 
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
}
