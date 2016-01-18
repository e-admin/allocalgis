package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.CentrosCulturalesEIEL;
import com.geopista.app.eiel.panels.CentroCulturalPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CentroCulturalDialog extends JDialog
{

    
    private CentroCulturalPanel centroCulturalPanel = null;   
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 720;
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
                        if(getCentroCulturalPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getCentroCulturalPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CentroCulturalDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getCentroCulturalPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            CentroCulturalDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CentroCulturalDialog.this,
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
    public CentroCulturalDialog(CentrosCulturalesEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.cu"));       
        this.isEditable = isEditable;
        getCentroCulturalPanel().loadData (elemento);
        if (getCentroCulturalPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getCentroCulturalPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getCentroCulturalPanel(), isEditable);
        	getCentroCulturalPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getCentroCulturalPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        
        if(elemento!=null)
        	EdicionUtils.enablePanel(getCentroCulturalPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getCentroCulturalPanel().getJTextFieldClave().setEnabled(false);
        getCentroCulturalPanel().getJComboBoxProvincia().setEnabled(false);
        getCentroCulturalPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public CentroCulturalDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public CentroCulturalDialog(boolean isEditable)
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
        this.setContentPane(getCentroCulturalPanel());
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
        CentroCulturalDialog dialog = 
            new CentroCulturalDialog();
        dialog.setVisible(true);
        
    }
    
    public CentroCulturalPanel getCentroCulturalPanel()
    {
        if (centroCulturalPanel == null)
        {
        	centroCulturalPanel = new CentroCulturalPanel(new GridBagLayout());
        	centroCulturalPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return centroCulturalPanel;
    }
    
    public CentrosCulturalesEIEL getCentroCultural(CentrosCulturalesEIEL elemento)
    {
    	return getCentroCulturalPanel().getCentroCultural(elemento);
    }
    
}
