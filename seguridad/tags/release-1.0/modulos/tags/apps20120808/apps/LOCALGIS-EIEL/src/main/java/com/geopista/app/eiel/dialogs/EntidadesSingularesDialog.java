package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.panels.EntidadesSingularesPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class EntidadesSingularesDialog extends JDialog{
	
	private EntidadesSingularesPanel EntidadesSingularesPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 650;
    public static final int DIM_Y = 350;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getEntidadesSingularesPanel().datosMinimosYCorrectos()){
                            String errorMessage = getEntidadesSingularesPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        EntidadesSingularesDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getEntidadesSingularesPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            EntidadesSingularesDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(EntidadesSingularesDialog.this,
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
    
    public EntidadesSingularesDialog(EntidadesSingularesEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.ent_sing"));       
        this.isEditable = isEditable;
        getEntidadesSingularesPanel().loadData (elemento);
        EdicionUtils.enablePanel(getEntidadesSingularesPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getEntidadesSingularesPanel().getJPanelDatosIdentificacion(), false);
      
        //getServiciosSaneamientoPanel().getJTextFieldClave().setEnabled(false);
        getEntidadesSingularesPanel().getJComboBoxProvincia().setEnabled(false);
        getEntidadesSingularesPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public EntidadesSingularesDialog(){
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public EntidadesSingularesDialog(boolean isEditable){
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
        this.setContentPane(getEntidadesSingularesPanel());
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
        EntidadesSingularesDialog dialog = new EntidadesSingularesDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public EntidadesSingularesPanel getEntidadesSingularesPanel(){
        if (EntidadesSingularesPanel == null){
        	EntidadesSingularesPanel = new EntidadesSingularesPanel(new GridBagLayout());
        	EntidadesSingularesPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return EntidadesSingularesPanel;
    }
    
    /* DATOS */
    public EntidadesSingularesEIEL getEntidadesSingulares(EntidadesSingularesEIEL elemento){
    	return getEntidadesSingularesPanel().getEntidadesSingulares(elemento);
    }
    
}
