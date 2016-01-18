package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.panels.ServiciosAbastecimientosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class ServiciosAbastecimientosDialog extends JDialog{
	
	private ServiciosAbastecimientosPanel ServiciosAbastecimientosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 800;
    public static final int DIM_Y = 550;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getServiciosAbastecimientosPanel().datosMinimosYCorrectos()){
                            String errorMessage = getServiciosAbastecimientosPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        ServiciosAbastecimientosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getServiciosAbastecimientosPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            ServiciosAbastecimientosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(ServiciosAbastecimientosDialog.this,
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
    
    public ServiciosAbastecimientosDialog(ServiciosAbastecimientosEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.serv_abast"));       
        this.isEditable = isEditable;
        getServiciosAbastecimientosPanel().loadData (elemento);
        EdicionUtils.enablePanel(getServiciosAbastecimientosPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getServiciosAbastecimientosPanel().getJPanelDatosIdentificacion(), false);
        
        //getServiciosSaneamientoPanel().getJTextFieldClave().setEnabled(false);
        getServiciosAbastecimientosPanel().getJComboBoxProvincia().setEnabled(false);
        getServiciosAbastecimientosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public ServiciosAbastecimientosDialog(){
        this(false);
        this.setLocationRelativeTo(null);
    }
    
    public ServiciosAbastecimientosDialog(boolean isEditable){
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
        this.setContentPane(getServiciosAbastecimientosPanel());
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
        ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public ServiciosAbastecimientosPanel getServiciosAbastecimientosPanel(){
        if (ServiciosAbastecimientosPanel == null){
        	ServiciosAbastecimientosPanel = new ServiciosAbastecimientosPanel(new GridBagLayout());
        	ServiciosAbastecimientosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return ServiciosAbastecimientosPanel;
    }
    
    /* DATOS */
    public ServiciosAbastecimientosEIEL getServiciosAbastecimientos(ServiciosAbastecimientosEIEL elemento){
    	return getServiciosAbastecimientosPanel().getServiciosAbastecimientos(elemento);
    }
    
}
