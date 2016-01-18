package com.geopista.app.eiel.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.panels.TratamientosPotabilizacionNucleosPanel;
import com.geopista.app.eiel.panels.TratamientosPotabilizacionPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class TratamientosPotabilizacionDialog extends JDialog{
	
	private TratamientosPotabilizacionPanel TratamientosPotabilizacionPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 930;
    public static final int DIM_Y = 550;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getTratamientosPotabilizacionPanel().datosMinimosYCorrectos()){
                            String errorMessage = getTratamientosPotabilizacionPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        TratamientosPotabilizacionDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getTratamientosPotabilizacionPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            TratamientosPotabilizacionDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(TratamientosPotabilizacionDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
                    }
                    else if (_okCancelPanel.wasCancelPressed()){
                   	 dispose(); 
                   }
                   else if (!isEditable) {
                           JOptionPane.showMessageDialog(TratamientosPotabilizacionDialog.this,
                                   I18N.get("LocalGISEIEL", "localgiseiel.mensajes.localgiseiel.mensajes.no_editable"));
                           return;
                   }
                    dispose();                    
                }
            });
        }
        return _okCancelPanel;
    }
    
    
/* CONSTRUCTORES */    
    
    public TratamientosPotabilizacionDialog(TratamientosPotabilizacionEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.abast_tp"));       
        this.isEditable = isEditable;
        getTratamientosPotabilizacionPanel().loadData (elemento);
        EdicionUtils.enablePanel(getTratamientosPotabilizacionPanel(), isEditable);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getTratamientosPotabilizacionPanel().getJTextFieldClave().setEnabled(false);
        getTratamientosPotabilizacionPanel().getJComboBoxProvincia().setEnabled(false);
        getTratamientosPotabilizacionPanel().getJComboBoxMunicipio().setEnabled(false);
        
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        this.setLocationRelativeTo(null);
    }
    
    public TratamientosPotabilizacionDialog(){
        this(false);
        this.setLocationRelativeTo(null);
    }
    
    public TratamientosPotabilizacionDialog(boolean isEditable){
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
        this.setContentPane(getTabbedPane());
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
    
	private JTabbedPane getTabbedPane() {
		if (this.tabbedPane == null){
			this.tabbedPane = new JTabbedPane();
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getTratamientosPotabilizacionPanel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getTratamientosPotabilizacionNucleosPanel());
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.nucleostabbedpane"), scroll);
			
			tabbedPane.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					onTabbedPageChange(e);
				}
			});

		}
		return tabbedPane;
	}
    
    private void onTabbedPageChange(ChangeEvent e) {
		JTabbedPane pane = (JTabbedPane)e.getSource();
        int sel = pane.getSelectedIndex();
        if (sel == 1){
        	this.getTratamientosPotabilizacionNucleosPanel().tabbedChanged(getTratamientosPotabilizacionPanel().getTratamientosPotabilizacionData());
        }
	}
   
    public static void main(String[] args){
        TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog();
        dialog.setVisible(true);
    }
    
    /* PANEL */
    public TratamientosPotabilizacionPanel getTratamientosPotabilizacionPanel(){
        if (TratamientosPotabilizacionPanel == null){
        	TratamientosPotabilizacionPanel = new TratamientosPotabilizacionPanel(new GridBagLayout());
        	TratamientosPotabilizacionPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return TratamientosPotabilizacionPanel;
    }
    
    private TratamientosPotabilizacionNucleosPanel tratamientosNucleosPanel = null;
	public TratamientosPotabilizacionNucleosPanel getTratamientosPotabilizacionNucleosPanel(){
		if (tratamientosNucleosPanel == null){
			tratamientosNucleosPanel = new TratamientosPotabilizacionNucleosPanel();
			
		}
		return tratamientosNucleosPanel;
	}
    
    /* DATOS */
    public TratamientosPotabilizacionEIEL getTratamientosPotabilizacion(TratamientosPotabilizacionEIEL elemento){
    	return getTratamientosPotabilizacionPanel().getTratamientosPotabilizacion(elemento);
    }
    
}
