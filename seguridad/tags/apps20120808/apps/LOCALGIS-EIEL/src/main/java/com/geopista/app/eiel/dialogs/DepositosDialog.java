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
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.panels.DepositosNucleosPanel;
import com.geopista.app.eiel.panels.DepositosPanel;
import com.geopista.app.eiel.utils.EdicionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;



public class DepositosDialog extends JDialog{

	private DepositosPanel DepositosPanel = null;    
	private OKCancelPanel _okCancelPanel = null;
	private boolean isEditable = false;       
	public static final int DIM_X = 750;
	public static final int DIM_Y = 550;

	private JTabbedPane tabbedPane = null;

	private OKCancelPanel getOkCancelPanel(){
		if (_okCancelPanel == null){
			_okCancelPanel = new OKCancelPanel();
			_okCancelPanel.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
					if (_okCancelPanel.wasOKPressed() && isEditable){
						if(getDepositosPanel().datosMinimosYCorrectos()){
							String errorMessage = getDepositosPanel().validateInput();
							if (errorMessage != null){
								JOptionPane.showMessageDialog(
										DepositosDialog.this,
										errorMessage,
										I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
										JOptionPane.ERROR_MESSAGE);
								return;
							} else{
								try{
									getDepositosPanel().okPressed();
								}catch (Exception e1){
									JOptionPane.showMessageDialog(
											DepositosDialog.this,
											errorMessage,
											I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"),
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}else{
							JOptionPane.showMessageDialog(DepositosDialog.this,
									I18N.get("LocalGISEIEL", "localgiseiel.mensajes.datosnocorrectos"));
							return;
						}
					}
					else if (_okCancelPanel.wasCancelPressed()){
                   	 dispose(); 
                   }
                   else if (!isEditable) {
                           JOptionPane.showMessageDialog(DepositosDialog.this,
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

	public DepositosDialog(DepositosEIEL elemento, boolean isEditable){
		super(AppContext.getApplicationContext().getMainFrame());
		initialize(I18N.get("LocalGISEIEL", "localgiseiel.dialog.titulo.de"),elemento);       
		this.isEditable = isEditable;
		getDepositosPanel().loadData (elemento);
        if (getDepositosPanel().getJComboBoxEpigInventario().isEnabled())    	
        	EdicionUtils.enablePanel(getDepositosPanel(), isEditable);
        else{
        	EdicionUtils.enablePanel(getDepositosPanel(), isEditable);
        	getDepositosPanel().getJComboBoxEpigInventario().setEnabled(false);
        	getDepositosPanel().getJComboBoxNumInventario().setEnabled(false);
        }
		if (!isEditable){
        	_okCancelPanel.setCancelEnabled(true);
        	//_okCancelPanel.setOKEnabled(true);
        }
        if(elemento!=null)
        	EdicionUtils.enablePanel(getDepositosPanel().getJPanelDatosIdentificacion(), false);
        
        //Deshabilitamos la clave, el codigo de provincia y el codigo de municipio
        getDepositosPanel().getJTextFieldClave().setEnabled(false);
        getDepositosPanel().getJComboBoxProvincia().setEnabled(false);
        getDepositosPanel().getJComboBoxMunicipio().setEnabled(false);
    	
		this.setLocationRelativeTo(null);
	}

	public DepositosDialog(){
		this(false);
		this.setLocationRelativeTo(null);
	}

	public DepositosDialog(boolean isEditable){
		this(null, isEditable);
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(String title, DepositosEIEL deposito){
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
			this.tabbedPane.add(I18N.get("LocalGISEIEL", "localgiseiel.dialogs.datatabbedpane"),getDepositosPanel());
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(getDepositosNucleosPanel());
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
        	this.getDepositosNucleosPanel().tabbedChanged(getDepositosPanel().getDepositoData());
        }
	}


	public static void main(String[] args){
		DepositosDialog dialog = new DepositosDialog();
		dialog.setVisible(true);
	}

	/* PANEL */
	public DepositosPanel getDepositosPanel(){
		if (DepositosPanel == null){
			DepositosPanel = new DepositosPanel(new GridBagLayout());
			DepositosPanel.add(getOkCancelPanel(), 
					new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
							GridBagConstraints.CENTER, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 0, 0));           
		}
		return DepositosPanel;
	}
	
	private DepositosNucleosPanel depositosNucleosPanel = null;
	public DepositosNucleosPanel getDepositosNucleosPanel(){
		if (depositosNucleosPanel == null){
			depositosNucleosPanel = new DepositosNucleosPanel();
			
		}
		return depositosNucleosPanel;
	}

	/* DATOS */
	public DepositosEIEL getDepositos(DepositosEIEL elemento){
		return getDepositosPanel().getDepositos(elemento);
	}

}
