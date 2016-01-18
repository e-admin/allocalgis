package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.panels.EntidadesAgrupadasPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class EntidadesAgrupadasDialog extends JDialog
{

    
    private EntidadesAgrupadasPanel entidadesAgrupadasPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
   // private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    
    public static final int DIM_X = 700;
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
                    
                    if (_okCancelPanel.wasOKPressed() && isEditable)
                    {
                        if(getEntidadesAgrupadasPanel().datosMinimosYCorrectos())
                        {
                            String errorMessage = getEntidadesAgrupadasPanel().validateInput();

                            if (errorMessage != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        EntidadesAgrupadasDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else
                            {
                                try
                                {
                                    getEntidadesAgrupadasPanel().okPressed();
                                }
                                catch (Exception e1)
                                {
                                    JOptionPane
                                    .showMessageDialog(
                                            EntidadesAgrupadasDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(EntidadesAgrupadasDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
                    }
                    else if (_okCancelPanel.wasCancelPressed()){
                    	 dispose(); 
                    }
                    else if (!isEditable) {
                            JOptionPane.showMessageDialog(EntidadesAgrupadasDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.localgiseiel.mensajes.no_editable"));
                            return;
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
    public EntidadesAgrupadasDialog(EntidadesAgrupadasEIEL elemento, boolean isEditable)
    {
        super(AppContext.getApplicationContext().getMainFrame());
        
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.a6"));       
        this.isEditable = isEditable;
        getEntidadesAgrupadasPanel().loadData (elemento);

        EdicionUtils.enablePanel(getEntidadesAgrupadasPanel(), isEditable);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getEntidadesAgrupadasPanel().getJComboBoxProvincia().setEnabled(false);
        getEntidadesAgrupadasPanel().getJComboBoxMunicipio().setEnabled(false);
        
        
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        }
        this.setLocationRelativeTo(null);
    }
    
    public EntidadesAgrupadasDialog()
    {
        this(false);
        this.setLocationRelativeTo(null);
    }
    public EntidadesAgrupadasDialog(boolean isEditable)
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
        this.setContentPane(getEntidadesAgrupadasPanel());
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
        
//    private void onTabbedPageChange(ChangeEvent e) {
//		JTabbedPane pane = (JTabbedPane)e.getSource();
//		// Get current tab
//        int sel = pane.getSelectedIndex();
//        if (sel == 1){
//        
//        	//this.getAgrupaciones6000Panel().tabbedChanged(getAgrupaciones6000Panel().getAgrupaciones6000Data());
//        }
//	}
    
   
    public static void main(String[] args)
    {
        EntidadesAgrupadasDialog dialog = 
            new EntidadesAgrupadasDialog();
        dialog.setVisible(true);
        
    }
    
    public EntidadesAgrupadasPanel getEntidadesAgrupadasPanel()
    {
        if (entidadesAgrupadasPanel == null)
        {
        	entidadesAgrupadasPanel = new EntidadesAgrupadasPanel(new GridBagLayout());
        	entidadesAgrupadasPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return entidadesAgrupadasPanel;
    }
    
    
    public EntidadesAgrupadasEIEL getEntidadesAgrupadas(EntidadesAgrupadasEIEL elemento)
    {
    	return getEntidadesAgrupadasPanel().getEntidadesAgrupadas(elemento);
    }
    
}
