package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.panels.CasaConsistorialPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class CasaConsistorialDialog extends JDialog
{

    
    private CasaConsistorialPanel casaConsistorialPanel = null;   
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 900;
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
                        if(getCasaConsistorialPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getCasaConsistorialPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        CasaConsistorialDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getCasaConsistorialPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            CasaConsistorialDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(CasaConsistorialDialog.this,
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
    public CasaConsistorialDialog(CasasConsistorialesEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
         
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.cc"));      
        
        this.isEditable = isEditable;

        getCasaConsistorialPanel().loadData (elemento);
        if (getCasaConsistorialPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getCasaConsistorialPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getCasaConsistorialPanel(), isEditable);
        	getCasaConsistorialPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getCasaConsistorialPanel().getJComboBoxNumInventario().setEnabled(false);
        }

        if(elemento!=null){
        	EdicionUtils.enablePanel(getCasaConsistorialPanel().getJPanelDatosIdentificacion(), false);
    		 EdicionUtils.enablePanel(getCasaConsistorialPanel().getJPanelUsosCasasConsistoriales(), true);
        } else
    		 EdicionUtils.enablePanel(getCasaConsistorialPanel().getJPanelUsosCasasConsistoriales(), false); 
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getCasaConsistorialPanel().getJTextFieldClave().setEnabled(false);
        getCasaConsistorialPanel().getJComboBoxProvincia().setEnabled(false);
        getCasaConsistorialPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public CasaConsistorialDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public CasaConsistorialDialog(boolean isEditable)
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
        this.setContentPane(getCasaConsistorialPanel());
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
        CasaConsistorialDialog dialog = 
            new CasaConsistorialDialog();
        dialog.setVisible(true);
        
    }
    
    public CasaConsistorialPanel getCasaConsistorialPanel()
    {
        if (casaConsistorialPanel == null)
        {
        	casaConsistorialPanel = new CasaConsistorialPanel(new GridBagLayout());
        	casaConsistorialPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return casaConsistorialPanel;
    }
    
    public CasasConsistorialesEIEL getCasaConsistorial(CasasConsistorialesEIEL elemento)
    {
    	return getCasaConsistorialPanel().getCasaConsistorial(elemento);
    }
    
}
