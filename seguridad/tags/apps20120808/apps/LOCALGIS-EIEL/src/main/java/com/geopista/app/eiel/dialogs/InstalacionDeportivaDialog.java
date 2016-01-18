package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.panels.InstalacionDeportivaPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class InstalacionDeportivaDialog extends JDialog
{

    
    private InstalacionDeportivaPanel instalacionDeportivaPanel = null;   
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 950;
    public static final int DIM_Y = 700;
        
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
                        if(getInstalacionDeportivaPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getInstalacionDeportivaPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        InstalacionDeportivaDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getInstalacionDeportivaPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            InstalacionDeportivaDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(InstalacionDeportivaDialog.this,
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
    public InstalacionDeportivaDialog(InstalacionesDeportivasEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.id"));       
        this.isEditable = isEditable;
        getInstalacionDeportivaPanel().loadData (elemento);
        if (getInstalacionDeportivaPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getInstalacionDeportivaPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getInstalacionDeportivaPanel(), isEditable);
        	getInstalacionDeportivaPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getInstalacionDeportivaPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null)
        	EdicionUtils.enablePanel(getInstalacionDeportivaPanel().getJPanelDatosIdentificacion(), false);
      
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getInstalacionDeportivaPanel().getJTextFieldClave().setEnabled(false);
        getInstalacionDeportivaPanel().getJComboBoxProvincia().setEnabled(false);
        getInstalacionDeportivaPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public InstalacionDeportivaDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public InstalacionDeportivaDialog(boolean isEditable)
    {
        this(null, isEditable);
        this.setLocationRelativeTo(null);
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
        this.setContentPane(getInstalacionDeportivaPanel());
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
        InstalacionDeportivaDialog dialog = 
            new InstalacionDeportivaDialog();
        dialog.setVisible(true);
        
    }
    
    public InstalacionDeportivaPanel getInstalacionDeportivaPanel()
    {
        if (instalacionDeportivaPanel == null)
        {
        	instalacionDeportivaPanel = new InstalacionDeportivaPanel(new GridBagLayout());
        	instalacionDeportivaPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));           
        }
        return instalacionDeportivaPanel;
    }
    
    public InstalacionesDeportivasEIEL getInstalacionDeportiva(InstalacionesDeportivasEIEL elemento)
    {
    	return getInstalacionDeportivaPanel().getInstalacionDeportiva(elemento);
    }
    
}
