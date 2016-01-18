package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.panels.MataderosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class MataderosDialog extends JDialog{
	
	private MataderosPanel MataderosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 770;
    public static final int DIM_Y = 700;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getMataderosPanel().datosMinimosYCorrectos()){
                            String errorMessage = getMataderosPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        MataderosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getMataderosPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            MataderosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(MataderosDialog.this,
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
    
    public MataderosDialog(MataderosEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.mt"));       
        this.isEditable = isEditable;
        getMataderosPanel().loadData (elemento);
        if (getMataderosPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getMataderosPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getMataderosPanel(), isEditable);
        	getMataderosPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getMataderosPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null)
        	EdicionUtils.enablePanel(getMataderosPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getMataderosPanel().getJTextFieldClave().setEnabled(false);
        getMataderosPanel().getJComboBoxProvincia().setEnabled(false);
        getMataderosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public MataderosDialog(){
        this(false);
        this.setLocationRelativeTo(null);
    }
    
    public MataderosDialog(boolean isEditable){
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
        this.setContentPane(getMataderosPanel());
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
        MataderosDialog dialog = new MataderosDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public MataderosPanel getMataderosPanel(){
        if (MataderosPanel == null){
        	MataderosPanel = new MataderosPanel(new GridBagLayout());
        	MataderosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return MataderosPanel;
    }
    
    /* DATOS */
    public MataderosEIEL getMataderos(MataderosEIEL elemento){
    	return getMataderosPanel().getMataderos(elemento);
    }
    
}
