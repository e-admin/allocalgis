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
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.panels.PuntosVertidoNucleosPanel;
import com.geopista.app.eiel.panels.PuntosVertidoPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class PuntosVertidoDialog extends JDialog{
	
	private PuntosVertidoPanel PuntosVertidoPanel = null;    
    private OKCancelPanel _okCancelPanel = null;
    private JTabbedPane tabbedPane = null;
    private boolean isEditable = false;       
    public static final int DIM_X = 650;
    public static final int DIM_Y = 400;
        
    private OKCancelPanel getOkCancelPanel(){
        if (_okCancelPanel == null){
            _okCancelPanel = new OKCancelPanel();
            _okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent e){
                    if (_okCancelPanel.wasOKPressed() && isEditable){
                        if(getPuntosVertidoPanel().datosMinimosYCorrectos()){
                            String errorMessage = getPuntosVertidoPanel().validateInput();
                            if (errorMessage != null){
                                JOptionPane.showMessageDialog(
                                        PuntosVertidoDialog.this,
                                        errorMessage,
                                        I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else{
                                try{
                                    getPuntosVertidoPanel().okPressed();
                                }catch (Exception e1){
                                    JOptionPane.showMessageDialog(
                                            PuntosVertidoDialog.this,
                                            errorMessage,
                                            I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(PuntosVertidoDialog.this,
                                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
                            return;
                        }
                    }
                    else if (_okCancelPanel.wasCancelPressed()){
                   	 dispose(); 
                   }
                   else if (!isEditable) {
                           JOptionPane.showMessageDialog(PuntosVertidoDialog.this,
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
    
    public PuntosVertidoDialog(PuntosVertidoEIEL elemento, boolean isEditable){
        super(AppContext.getApplicationContext().getMainFrame());
        initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.pv"));       
        this.isEditable = isEditable;
        getPuntosVertidoPanel().loadData (elemento);
        EdicionUtils.enablePanel(getPuntosVertidoPanel(), isEditable);
        if(elemento!=null)
        	EdicionUtils.enablePanel(getPuntosVertidoPanel().getJPanelDatosIdentificacion(), false);
        
        
        
        getPuntosVertidoPanel().getJTextFieldClave().setEnabled(false);
        getPuntosVertidoPanel().getJComboBoxProvincia().setEnabled(false);
        getPuntosVertidoPanel().getJComboBoxMunicipio().setEnabled(false);
        
        if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        this.setLocationRelativeTo(null);
    }
    
    public PuntosVertidoDialog(){
        this(false);
        
        this.setLocationRelativeTo(null);
    }
    
    public PuntosVertidoDialog(boolean isEditable){
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
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getPuntosVertidoPanel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getPuntosVertidoNucleosPanel());
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
		// Get current tab
        int sel = pane.getSelectedIndex();
        if (sel == 1){
        	this.getPuntosVertidoNucleosPanel().tabbedChanged(getPuntosVertidoPanel().getPuntosVertidoData());
        }
	}
   
    public static void main(String[] args){
        PuntosVertidoDialog dialog = new PuntosVertidoDialog();
        dialog.setVisible(true);
    }
    
    private PuntosVertidoNucleosPanel puntosVertidoNucleosPanel = null;
	public PuntosVertidoNucleosPanel getPuntosVertidoNucleosPanel(){
		if (puntosVertidoNucleosPanel == null){
			puntosVertidoNucleosPanel = new PuntosVertidoNucleosPanel();
			
		}
		return puntosVertidoNucleosPanel;
	}
    
    /* PANEL */
    public PuntosVertidoPanel getPuntosVertidoPanel(){
        if (PuntosVertidoPanel == null){
        	PuntosVertidoPanel = new PuntosVertidoPanel(new GridBagLayout());
        	PuntosVertidoPanel.add(getOkCancelPanel(), 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 5, 0, 5), 0, 0));           
        }
        return PuntosVertidoPanel;
    }
    
    
    
    /* DATOS */
    public PuntosVertidoEIEL getPuntosVertido(PuntosVertidoEIEL elemento){
    	return getPuntosVertidoPanel().getPuntosVertido(elemento);
    }
    
}
