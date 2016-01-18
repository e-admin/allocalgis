package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.panels.LonjasMercadosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class LonjasMercadosDialog extends JDialog{
	
	private LonjasMercadosPanel LonjasMercadosPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 780;
    public static final int DIM_Y = 600;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getLonjasMercadosPanel().datosMinimosYCorrectos()){
                            String errorMessage = getLonjasMercadosPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        LonjasMercadosDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getLonjasMercadosPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            LonjasMercadosDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(LonjasMercadosDialog.this,
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
    
    public LonjasMercadosDialog(LonjasMercadosEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.lm"));       
        this.isEditable = isEditable;
        getLonjasMercadosPanel().loadData (elemento);
        if (getLonjasMercadosPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getLonjasMercadosPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getLonjasMercadosPanel(), isEditable);
        	getLonjasMercadosPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getLonjasMercadosPanel().getJComboBoxNumInventario().setEnabled(false);
        }
        if(elemento!=null)
        	EdicionUtils.enablePanel(getLonjasMercadosPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getLonjasMercadosPanel().getJTextFieldClave().setEnabled(false);
        getLonjasMercadosPanel().getJComboBoxProvincia().setEnabled(false);
        getLonjasMercadosPanel().getJComboBoxMunicipio().setEnabled(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public LonjasMercadosDialog(){
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public LonjasMercadosDialog(boolean isEditable){
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
        this.setContentPane(getLonjasMercadosPanel());
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
        LonjasMercadosDialog dialog = new LonjasMercadosDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public LonjasMercadosPanel getLonjasMercadosPanel(){
        if (LonjasMercadosPanel == null){
        	LonjasMercadosPanel = new LonjasMercadosPanel(new GridBagLayout());
        	LonjasMercadosPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return LonjasMercadosPanel;
    }
    
    /* DATOS */
    public LonjasMercadosEIEL getLonjasMercados(LonjasMercadosEIEL elemento){
    	return getLonjasMercadosPanel().getLonjasMercados(elemento);
    }
    
}
