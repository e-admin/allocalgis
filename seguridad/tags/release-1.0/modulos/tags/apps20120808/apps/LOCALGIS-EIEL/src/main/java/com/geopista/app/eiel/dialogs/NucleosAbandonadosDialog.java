package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.panels.NucleosAbandonadosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class NucleosAbandonadosDialog extends JDialog
{

    
    private NucleosAbandonadosPanel nucleosAbandonadosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 950;
    public static final int DIM_Y = 450;
        
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
                        if(getNucleosAbandonadosPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getNucleosAbandonadosPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                		NucleosAbandonadosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getNucleosAbandonadosPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                    		NucleosAbandonadosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(NucleosAbandonadosDialog.this,
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
    public NucleosAbandonadosDialog(NucleosAbandonadosEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.aband"));       
        this.isEditable = isEditable;
        getNucleosAbandonadosPanel().loadData (elemento);
        EdicionUtils.enablePanel(getNucleosAbandonadosPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getNucleosAbandonadosPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        //getNucleosAbandonadosPanel().getjTextFieldClave().setEnabled(false);
        getNucleosAbandonadosPanel().getJComboBoxProvincia().setEnabled(false);
        getNucleosAbandonadosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public NucleosAbandonadosDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public NucleosAbandonadosDialog(boolean isEditable)
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
        this.setContentPane(getNucleosAbandonadosPanel());
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
    	NucleosAbandonadosDialog dialog = 
            new NucleosAbandonadosDialog();
        dialog.setVisible(true);
        
    }
    
    public NucleosAbandonadosPanel getNucleosAbandonadosPanel()
    {
        if (nucleosAbandonadosPanel == null)
        {
        	nucleosAbandonadosPanel = new NucleosAbandonadosPanel(new GridBagLayout());
        	nucleosAbandonadosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return nucleosAbandonadosPanel;
    }
    
    public NucleosAbandonadosEIEL getNucleosAbandonados(NucleosAbandonadosEIEL elemento)
    {
    	return getNucleosAbandonadosPanel().getNucleoAbandonado(elemento);
    }
    
}
