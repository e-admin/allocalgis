package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.panels.EdificiosSinUsoPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class EdificiosSinUsoDialog extends JDialog
{

    
    private EdificiosSinUsoPanel edificiosSinUsoPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 750;
    public static final int DIM_Y = 600;
        
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
                        if(getEdificiosSinUsoPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getEdificiosSinUsoPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		EdificiosSinUsoDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getEdificiosSinUsoPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		EdificiosSinUsoDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(EdificiosSinUsoDialog.this,
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
    public EdificiosSinUsoDialog(EdificiosSinUsoEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.su"));       
        this.isEditable = isEditable;
        getEdificiosSinUsoPanel().loadData (elemento);
        if (getEdificiosSinUsoPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getEdificiosSinUsoPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getEdificiosSinUsoPanel(), isEditable);
        	getEdificiosSinUsoPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getEdificiosSinUsoPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null)
        	EdicionUtils.enablePanel(getEdificiosSinUsoPanel().getJPanelDatosIdentificacion(), false);
       
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getEdificiosSinUsoPanel().getjTextFieldClave().setEnabled(false);
        getEdificiosSinUsoPanel().getJComboBoxProvincia().setEnabled(false);
        getEdificiosSinUsoPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public EdificiosSinUsoDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public EdificiosSinUsoDialog(boolean isEditable)
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
        this.setContentPane(getEdificiosSinUsoPanel());
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
    	EdificiosSinUsoDialog dialog = 
            new EdificiosSinUsoDialog();
        dialog.setVisible(true);
        
    }
    
    public EdificiosSinUsoPanel getEdificiosSinUsoPanel()
    {
        if (edificiosSinUsoPanel == null)
        {
        	edificiosSinUsoPanel = new EdificiosSinUsoPanel(new GridBagLayout());
        	edificiosSinUsoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return edificiosSinUsoPanel;
    }
    
    public EdificiosSinUsoEIEL getEdificiosSinUso(EdificiosSinUsoEIEL elemento)
    {
    	return getEdificiosSinUsoPanel().getEdificiosSinUso(elemento);
    }
    
}
