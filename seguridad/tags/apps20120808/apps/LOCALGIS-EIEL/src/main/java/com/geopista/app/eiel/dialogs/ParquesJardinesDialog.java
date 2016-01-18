package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.panels.ParquesJardinesPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class ParquesJardinesDialog extends JDialog
{

    
    private ParquesJardinesPanel parquesJardinesPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 850;
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
                        if(getParquesJardinesPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getParquesJardinesPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		ParquesJardinesDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getParquesJardinesPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		ParquesJardinesDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(ParquesJardinesDialog.this,
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
    public ParquesJardinesDialog(ParquesJardinesEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.pj"));       
        this.isEditable = isEditable;
        getParquesJardinesPanel().loadData (elemento);
        if (getParquesJardinesPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getParquesJardinesPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getParquesJardinesPanel(), isEditable);
        	getParquesJardinesPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getParquesJardinesPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getParquesJardinesPanel().getjTextFieldClave().setEnabled(false);
        getParquesJardinesPanel().getJComboBoxProvincia().setEnabled(false);
        getParquesJardinesPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public ParquesJardinesDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public ParquesJardinesDialog(boolean isEditable)
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
        this.setContentPane(getParquesJardinesPanel());
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
    	ParquesJardinesDialog dialog = 
            new ParquesJardinesDialog();
        dialog.setVisible(true);
        
    }
    
    public ParquesJardinesPanel getParquesJardinesPanel()
    {
        if (parquesJardinesPanel == null)
        {
        	parquesJardinesPanel = new ParquesJardinesPanel(new GridBagLayout());
        	parquesJardinesPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return parquesJardinesPanel;
    }
    
    public ParquesJardinesEIEL getParquesJardines(ParquesJardinesEIEL elemento)
    {
    	return getParquesJardinesPanel().getParquesJardines(elemento);
    }
    
}
