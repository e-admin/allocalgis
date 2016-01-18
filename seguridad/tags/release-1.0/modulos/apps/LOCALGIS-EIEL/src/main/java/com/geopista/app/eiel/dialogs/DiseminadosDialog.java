package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.panels.DiseminadosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class DiseminadosDialog extends JDialog{
	
	private DiseminadosPanel DiseminadosPanel = null;
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    public static final int DIM_X = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int DIM_Y = 650;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getDiseminadosPanel().datosMinimosYCorrectos()){
                            String errorMessage = getDiseminadosPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        DiseminadosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getDiseminadosPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            DiseminadosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(DiseminadosDialog.this,
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
    
    
/* CONSTRUCTORES */    
    
    public DiseminadosDialog(DiseminadosEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.diseminados"));       
        this.isEditable = isEditable;
        getDiseminadosPanel().loadData (elemento);
        EdicionUtils.enablePanel(getDiseminadosPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getDiseminadosPanel().getJPanelDatosIdentificacion(), false);
       
        //getServiciosSaneamientoPanel().getJTextFieldClave().setEnabled(false);
        getDiseminadosPanel().getJComboBoxProvincia().setEnabled(false);
        getDiseminadosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public DiseminadosDialog(){
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public DiseminadosDialog(boolean isEditable){
        this(null, isEditable);
        
        this.setLocationRelativeTo(null);
    }
    
   
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title){
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        this.setModal(true);
        this.setContentPane(getDiseminadosPanel());
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        this.setTitle(title);
        this.setResizable(true);
        this.getOkCancelPanel().setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });              
    }    
   
    public static void main(String[] args){
        DiseminadosDialog dialog = new DiseminadosDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public DiseminadosPanel getDiseminadosPanel(){
        if (DiseminadosPanel == null){
        	DiseminadosPanel = new DiseminadosPanel(new GridBagLayout());
        	DiseminadosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return DiseminadosPanel;
    }
    
    /* DATOS */
    public DiseminadosEIEL getDiseminados(DiseminadosEIEL elemento){
    	return getDiseminadosPanel().getDiseminados(elemento);
    }
    
}
