package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.panels.Encuestados2Panel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class Encuestados2Dialog extends JDialog
{

    
    private Encuestados2Panel encuestados2Panel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 750;
    public static final int DIM_Y = 550;
        
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
                        if(getEncuestados2Panel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getEncuestados2Panel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		Encuestados2Dialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getEncuestados2Panel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		Encuestados2Dialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(Encuestados2Dialog.this,
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
    public Encuestados2Dialog(Encuestados2EIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.enc2"));       
        this.isEditable = isEditable;
        getEncuestados2Panel().loadData (elemento);
        EdicionUtils.enablePanel(getEncuestados2Panel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getEncuestados2Panel().getJPanelDatosIdentificacion(), false);
      
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio

        getEncuestados2Panel().getJComboBoxProvincia().setEnabled(false);
        getEncuestados2Panel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null); 
    }
    
    public Encuestados2Dialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public Encuestados2Dialog(boolean isEditable)
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
        this.setContentPane(getEncuestados2Panel());
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
    	Encuestados2Dialog dialog = 
            new Encuestados2Dialog();
        dialog.setVisible(true);
        
    }
    
    public Encuestados2Panel getEncuestados2Panel()
    {
        if (encuestados2Panel == null)
        {
        	encuestados2Panel = new Encuestados2Panel(new GridBagLayout());
        	encuestados2Panel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return encuestados2Panel;
    }
    
    public Encuestados2EIEL getEncuestados2(Encuestados2EIEL elemento)
    {
    	return getEncuestados2Panel().getEncuestados2(elemento);
    }
    
}
