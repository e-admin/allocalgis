package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.panels.PadronMunicipiosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class PadronMunicipiosDialog extends JDialog
{

    
    private PadronMunicipiosPanel padronMunicipiosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 650;
    public static final int DIM_Y = 400;
        
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
                        if(getPadronMunicipiosPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getPadronMunicipiosPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		PadronMunicipiosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getPadronMunicipiosPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		PadronMunicipiosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(PadronMunicipiosDialog.this,
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
    public PadronMunicipiosDialog(PadronMunicipiosEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.pm"));       
        this.isEditable = isEditable;
        getPadronMunicipiosPanel().loadData (elemento);
        EdicionUtils.enablePanel(getPadronMunicipiosPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getPadronMunicipiosPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        //getNucleosAbandonadosPanel().getjTextFieldClave().setEnabled(false);
        getPadronMunicipiosPanel().getJComboBoxProvincia().setEnabled(false);
        getPadronMunicipiosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public PadronMunicipiosDialog()
    {
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    public PadronMunicipiosDialog(boolean isEditable)
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
        this.setContentPane(getPadronMunicipiosPanel());
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
    	PadronNucleosDialog dialog = 
            new PadronNucleosDialog();
        dialog.setVisible(true);
        
    }
    
    public PadronMunicipiosPanel getPadronMunicipiosPanel()
    {
        if (padronMunicipiosPanel == null)
        {
        	padronMunicipiosPanel = new PadronMunicipiosPanel(new GridBagLayout());
        	padronMunicipiosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return padronMunicipiosPanel;
    }
    
    public PadronMunicipiosEIEL getPadronMunicipios(PadronMunicipiosEIEL elemento)
    {
    	return getPadronMunicipiosPanel().getPadronMunicipios(elemento);
    }
    
}
