/**
 * LinderoEditDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.dialogs.asceditdialog.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedienteConst;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.asceditdialog.main.ASCEditDialog;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.ASCCatastro;
import com.geopista.app.catastro.model.beans.LinderoCatastro;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.vividsolutions.jump.I18N;

public class LinderoEditDialog extends JDialog{
	
	/**
	 * Auto generated serial version.
	 */
	private static final long serialVersionUID = 658792911453567444L;
	
	//TextFields para el dialogo
	JTextField codigoViaField = null;
	ComboBoxEstructuras siglaViaComboBox = null;
	JTextField nombreViaField = null;
	JTextField numeroField = null;
	JTextField letraDuplicadoField = null;
	JTextField idLinderoField = null;
	JComboBox seleccionarLinderoComboBox = null;
	
	
	//Labels para el dialogo
	JLabel codigoViaLabel = null;
	JLabel siglaViaLabel = null;
	JLabel nombreViaLabel = null;
	JLabel numeroLabel = null;
	JLabel letraDuplicadoLabel = null;
	JLabel idLinderoLabel = null;
	
	// boton para buscar las vias
	JButton buscarViaButton = null;
	
	// panel
	JPanel linderoPanel = null;
	
	// botones aceptar y cancelar
	JButton acceptButton = null;
	JButton cancelButton = null;
	
	LinderoCatastro linderoCatastro = null;
	ASCCatastro asc = null;
	ASCEditDialog ascEditDialog = null;
	
	static String[] listaLinderoComboBox={"",LinderoCatastro.DR,LinderoCatastro.IZ,LinderoCatastro.FD};

	/**
	 * 
	 */
	public LinderoEditDialog(ASCEditDialog ascEdit) {
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		this.extractResourcBundle(bundle);

		this.setLocationRelativeTo(null); 

		// poner tamaño, posicion y visible.
		
		this.initializeDialogPosition();  
		this.setVisible(true);
		
		this.initializeDialog();
		
		this.loadDataLindero();
		
		this.setTitle("Modifiar Lindero");
		
		this.ascEditDialog = ascEdit;
		this.ascEditDialog.setEnabled(false);
		this.setSize(600, 275);
		this.setLocationRelativeTo(null); 
	}

	@SuppressWarnings("unchecked")
	private Object extractResourcBundle(ResourceBundle bundle) {
		return I18N.plugInsResourceBundle.put("Expedientes",bundle);
	}
	
	/**
	 * 
	 */
	public LinderoEditDialog(ASCEditDialog ascEdit, LinderoCatastro lindero) {
		super(AppContext.getApplicationContext().getMainFrame());

		Locale loc=I18N.getLocaleAsObject();         
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
		extractResourcBundle(bundle);

		this.setLocationRelativeTo(null); 

		// poner tamaño, posicion y visible.
		
		this.initializeDialogPosition();  
		this.setVisible(true);
		
		this.setResizable(false);
		
		this.linderoCatastro = lindero;
		
		this.initializeDialog();
		
		// cargar los datos del lindero
		this.loadDataLindero(lindero);
		
		this.setTitle("Modifiar Lindero  (" + this.getClass().getSimpleName() + ").");
		
		this.ascEditDialog = ascEdit;
		this.ascEditDialog.setEnabled(false);
		this.getParent().setEnabled(false);
		this.setSize(600, 130);
		this.setLocationRelativeTo(null); 
	}
	
	private void initializeDialog() {
		// TODO Auto-generated method stub
		this.addWindowListener(new java.awt.event.WindowAdapter(){
	           public void windowClosing(WindowEvent e){
	        	   getParent().setEnabled(true);
	        	   ascEditDialog.setEnabled(true);
	           }
	        }
	        );
		
		this.setLayout(new GridBagLayout());

		this.add(getJPanelLindero(), new GridBagConstraints(
				0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getButtonsPanel(), new GridBagConstraints(
				0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
				new Insets(0, 5, 0, 5), 0, 0));

	}



	private void loadDataLindero() {
		// TODO Auto-generated method stub
		if(this.linderoCatastro == null){
			this.linderoCatastro = new LinderoCatastro();
		}
		
		this.getIdLinderoField().setText(this.linderoCatastro.getTipoLindero());
		this.getCodigoViaField().setText(String.valueOf(this.linderoCatastro.getCodVia()));
		this.getSiglaViaComboBox().setSelectedPatron(this.linderoCatastro.getSiglaVia());
		this.getNombreViaField().setText(this.linderoCatastro.getNombreVia());
		this.getNumeroField().setText(String.valueOf(this.linderoCatastro.getNumVia()));
		this.getLetraDuplicadoField().setText(this.linderoCatastro.getLetraDuplicado());
	}



	private void loadDataLindero(LinderoCatastro lindero) {
		// TODO Auto-generated method stub
		if (lindero != null){
			this.getIdLinderoField().setText(lindero.getTipoLindero());
			this.getCodigoViaField().setText(String.valueOf(lindero.getCodVia()));
			this.getSiglaViaComboBox().setSelectedPatron(this.linderoCatastro.getSiglaVia());
			this.getNombreViaField().setText(lindero.getNombreVia());
			this.getNumeroField().setText(String.valueOf(lindero.getNumVia()));
			this.getLetraDuplicadoField().setText(lindero.getLetraDuplicado());
		}
	}

	/**
	 * 
	 */
	private void initializeDialogPosition() {
		//Get the screen size  
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		Dimension screenSize = toolkit.getScreenSize();  
		int x = (screenSize.width - this.getWidth()) / 2;  
		int y = (screenSize.height - this.getHeight()) / 2;    
		//Set the new frame location  
		this.setLocation(x, y);
		

	}

	public JTextField getIdLinderoField() {
		if (idLinderoField == null) {
			idLinderoField = new JTextField();
			idLinderoField.setHorizontalAlignment((int) TextField.CENTER_ALIGNMENT);
			idLinderoField.setDocument(new LimitadorCaracteres(idLinderoField,2));
			idLinderoField.setEditable(false);
		}
		return idLinderoField;
	}

	public JTextField getCodigoViaField() {
		if (codigoViaField == null) {
			codigoViaField = new JTextField();
			codigoViaField.setDocument(new LimitadorCaracteres(codigoViaField,5));
			codigoViaField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					codigoViaField.selectAll();
				}
			}
			);
		}
		return codigoViaField;
	}

	public ComboBoxEstructuras getSiglaViaComboBox() {
		if (siglaViaComboBox == null) {
        	Estructuras.cargarEstructura("Tipo de vía");
            siglaViaComboBox = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, 
                    ((AppContext) AppContext.getApplicationContext()).
                    getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
		}
		return siglaViaComboBox;
	}


	public JComboBox getSeleccionarLinderoComboBox(){
		if (seleccionarLinderoComboBox == null){
			seleccionarLinderoComboBox = new JComboBox(listaLinderoComboBox);
			seleccionarLinderoComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					String tipoLindero = ( (String) seleccionarLinderoComboBox.getSelectedItem());
					String texto = "Añadir lindero ";
					if(tipoLindero.equals(LinderoCatastro.IZ)){
						texto = texto + "IZQUIERDO";
					} else if(tipoLindero.equals(LinderoCatastro.DR)){
						texto = texto + "DERECHO";
					} else if(tipoLindero.equals(LinderoCatastro.FD)){
						texto = texto + "FONDO";
					}
					
					linderoPanel.setBorder(BorderFactory.createTitledBorder(
							null, texto,
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
									Font.BOLD, 12)));
				}

			});
		}
		
		return seleccionarLinderoComboBox;
	}
	
	public JTextField getNombreViaField() {
		if (nombreViaField == null) {
			nombreViaField = new JTextField();
			nombreViaField.setDocument(new LimitadorCaracteres(nombreViaField,25));
			nombreViaField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					nombreViaField.selectAll();
				}
			}
			);
		}
		return nombreViaField;
	}


	public JTextField getNumeroField() {
		if ( numeroField == null) {
			 numeroField = new JTextField();
			 numeroField.setDocument(new LimitadorCaracteres(numeroField,4));
			 numeroField.addMouseListener(new MouseInputAdapter() {
					public void mouseClicked(MouseEvent e) {
						// Recogemos la informacion del panel actual
						numeroField.selectAll();
					}
				}
				);
		}
		return numeroField;
	}


	public JTextField getLetraDuplicadoField() {
		if (letraDuplicadoField == null) {
			letraDuplicadoField = new JTextField();
			letraDuplicadoField.setDocument(new LimitadorCaracteres(letraDuplicadoField,1));
			letraDuplicadoField.addMouseListener(new MouseInputAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Recogemos la informacion del panel actual
					letraDuplicadoField.selectAll();
				}
			}
			);
		}
		return letraDuplicadoField;
	}

	public JButton getBuscarViaButton() {
		if(buscarViaButton == null){
			buscarViaButton = new JButton();
			buscarViaButton.setIcon(IconLoader.icon(GestionExpedienteConst.ICONO_BUSCAR));

			buscarViaButton.setMaximumSize(new Dimension(20,20));
			buscarViaButton.setSize(new Dimension(20,20));

			buscarViaButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					String tipoVia = "";
					if(siglaViaComboBox.getSelectedPatron()!=null){
						tipoVia = siglaViaComboBox.getSelectedPatron().toString();
					}

					ViasSistemaDialog dialog = new ViasSistemaDialog(nombreViaField.getText(),tipoVia);
					dialog.setVisible(true);     
					String nombreVia = "";
					if(dialog.getVia().length() >= 25){
						nombreVia =dialog.getVia().substring(0, 25);
					}
					else{
						nombreVia = dialog.getVia();
					}
					nombreViaField.setText(nombreVia);
					siglaViaComboBox.setSelectedPatron(dialog.getTipoVia());
					codigoViaField.setText(String.valueOf(dialog.getCodigoVia()));
				}
			});
		}
		return buscarViaButton;
	}
	
	public JButton getAcceptButton() {
		if(acceptButton == null){
			acceptButton = new JButton("Aceptar");
			acceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onAcceptButtonDo();
				}
			}
			);
		}
		return acceptButton;
	}

	public JButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new JButton("Cancelar");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onCancelButtonDo();
				}
			}
			);
		}
		return cancelButton;
	}

	private JPanel getJPanelLindero() {
		if (linderoPanel == null) {

			linderoPanel = new JPanel(new GridBagLayout());
			linderoPanel.setSize(new Dimension(400, 80));
			linderoPanel.setPreferredSize(new Dimension(400,80));
			linderoPanel.setMaximumSize(linderoPanel.getPreferredSize());
			linderoPanel.setMinimumSize(linderoPanel.getPreferredSize());
			
			String texto ="";
			if (this.linderoCatastro != null){
				texto = "Modificar lindero ";
				if(this.linderoCatastro.getTipoLindero().equals(LinderoCatastro.IZ)){
					texto = texto + "IZQUIERDO";
				} else if(this.linderoCatastro.getTipoLindero().equals(LinderoCatastro.DR)){
					texto = texto + "DERECHO";
				} else if(this.linderoCatastro.getTipoLindero().equals(LinderoCatastro.FD)){
					texto = texto + "FONDO";
				}
			} else{
				texto = "Añadir lindero";
			}
			
			linderoPanel.setBorder(BorderFactory.createTitledBorder(
					null, texto,
					TitledBorder.LEADING, TitledBorder.TOP, new Font(null,
							Font.BOLD, 12)));
			
			//Añadir los labels.
			this.anniadirLinderoEditDialogLabelsToPanel(linderoPanel);
			
			// Añadir los edits
			this.anniadirLinderoEditDialogFieldsToPanel(linderoPanel);
			
			
		}
		
		return linderoPanel;
	}



	private JPanel getButtonsPanel() {
		// TODO Auto-generated method stub
		
		// panel Aparte para los botones...
		JPanel jPanel1 = new JPanel(new GridBagLayout());
		jPanel1.setSize(new Dimension(200, 50));
		jPanel1.setPreferredSize(new Dimension(200, 50));
		jPanel1.setMaximumSize(jPanel1.getPreferredSize());
		jPanel1.setMinimumSize(jPanel1.getPreferredSize());
		
		
		jPanel1.add(new JLabel(),
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 300, 0));
		
		jPanel1.add(getAcceptButton(),
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(new JLabel(),
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		jPanel1.add(getCancelButton(),
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		return jPanel1;
		
	}

	/**
	 * 
	 */
	private void anniadirLinderoEditDialogLabelsToPanel(JPanel panel) {
		
		idLinderoLabel = new JLabel("Lindero");
		panel.add(idLinderoLabel,
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
		
		codigoViaLabel = new JLabel("Cód. Vía");
		panel.add(codigoViaLabel,
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
		
		siglaViaLabel = new JLabel("Sigla Vía");
		panel.add(siglaViaLabel,
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		nombreViaLabel = new JLabel("Nombre de la Vía");
		panel.add(nombreViaLabel,
				new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 100, 0));
		
		numeroLabel = new JLabel("Número");
		panel.add(numeroLabel,
				new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
		
		letraDuplicadoLabel = new JLabel("Letra Duplicado");
		panel.add(letraDuplicadoLabel,
				new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private void anniadirLinderoEditDialogFieldsToPanel(JPanel panel) {
		// TODO Auto-generated method stub
		
		if( this.linderoCatastro != null ){
			panel.add(this.getIdLinderoField(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 3));
		} else {
			panel.add(this.getSeleccionarLinderoComboBox(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 4));	
		}
		
		panel.add(this.getCodigoViaField(),
				new GridBagConstraints(1,1, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 3));
		
		panel.add(this.getSiglaViaComboBox(),
				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 6));
		
		panel.add(this.getNombreViaField(),
				new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 3));
		
		panel.add(this.getBuscarViaButton(),
				new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		panel.add(this.getNumeroField(),
				new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 3));

		panel.add(this.getLetraDuplicadoField(),
				new GridBagConstraints(6, 1, 1, 1, 0.1, 0.1,
				GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 3));
	}
	
	public boolean onAcceptButtonDo(){

		// Si es añadir.
		if ( this.linderoCatastro == null ) {
			// si se ha elejido el tipo lindero que se quiere añadir
			if (!this.getSeleccionarLinderoComboBox().getSelectedItem().equals("")){
				// si ya existe el lindero en la lista
				if (ascEditDialog.existeLinderoTipo( ((String) this.getSeleccionarLinderoComboBox().getSelectedItem()) ) ) {
					// si no se quiere sonbre escribir... sale.
					if (! this.confirmarSobrescribir() ){
						return false;
					}
				} 
				// no existe el lindero en la lista o se desea sobreescribir.
				if (comprobarCamposLindero()){
					this.linderoCatastro = new LinderoCatastro(
							(String )this.getSeleccionarLinderoComboBox().getSelectedItem(),
							Integer.parseInt(this.getCodigoViaField().getText()),
							this.getSiglaViaComboBox().getSelectedPatron(),
							this.getNombreViaField().getText(),
							Integer.parseInt(this.getNumeroField().getText()),
							this.getLetraDuplicadoField().getText()
					);
				} else {
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(this, "Elija el tipo de lindero que desea añadir.");
				return false;
			}
		}


		if (this.guardarDatosLindero()){
			if (ascEditDialog.modificarLindero(this.linderoCatastro)){
				this.ascEditDialog.setEnabled(true);
				this.getParent().setEnabled(true);
				this.ascEditDialog.show(true);
				this.dispose();	
			} else {
				JOptionPane.showMessageDialog(this, "Error al modificar los datos del lindero. Compruebe los datos o cierre el esta ventana.");
			}
		}
		
		return true;
	}
	
	private boolean confirmarSobrescribir() {
		// TODO Auto-generated method stub
		// mensaje del dialogo.
		String mensaje = "Ya existe un lindero ";
		if(this.getSeleccionarLinderoComboBox().getSelectedItem().equals(LinderoCatastro.IZ)){
			mensaje = mensaje + "'IZQUIERDO'. ";
		} else if(this.getSeleccionarLinderoComboBox().getSelectedItem().equals(LinderoCatastro.DR)){
			mensaje = mensaje + "'DERECHO'. ";
		} else if(this.getSeleccionarLinderoComboBox().getSelectedItem().equals(LinderoCatastro.FD)){
			mensaje = mensaje + "'FONDO'. ";
		}
		mensaje = mensaje + "¿Desea sobreescribirlo?";
		
		int seleccion = JOptionPane.showOptionDialog(
				   this,
				   mensaje, 
				   "Seleccciona 1 opción",
				   JOptionPane.YES_NO_OPTION,
				   JOptionPane.QUESTION_MESSAGE,
				   null,    // null para icono por defecto.
				   new Object[] { "Aceptar", "cancelar"},   // null para YES, NO y CANCEL
				   "cancelar");

		if (seleccion == 0)
			return true;

		return false;
	}

	private boolean guardarDatosLindero() {
		// TODO Auto-generated method stub
		if ( this.comprobarCamposLindero()){
			this.linderoCatastro.setCodVia(Integer.parseInt(this.codigoViaField.getText()));
			this.linderoCatastro.setLetraDuplicado(this.letraDuplicadoField.getText());
			this.linderoCatastro.setNombreVia(this.nombreViaField.getText());
			this.linderoCatastro.setNumVia(Integer.parseInt(this.numeroField.getText()));
			this.linderoCatastro.setSiglaVia(this.siglaViaComboBox.getSelectedPatron());
			return true;
		} 
		return false;
	}

	private boolean comprobarCamposLindero() {
		// TODO Auto-generated method stub
		try{
		
		if ( Integer.parseInt(this.codigoViaField.getText()) < 0  || Integer.parseInt(this.codigoViaField.getText()) > 99999 ){
			JOptionPane.showMessageDialog(this, "Código de vía '" + codigoViaField.getText()  + "' erroneo. Debería ser un valor numérico entre 0 y 99999.");
			return false;
		}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Código de vía '" + codigoViaField.getText()  + "' erroneo. Debería ser un valor numérico entre 0 y 99999.");
			return false;
		}
		
		if( this.siglaViaComboBox.getSelectedPatron().equals("")){
			JOptionPane.showMessageDialog(this, "Seleccione un tipo de via (Si no, seleccion [NO ESPECIFICADO]).");
			return false;
		}
		
		if ( this.nombreViaField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Introduzca el nombre de la vía.");
			return false;
		}
		
		try{
			if ( Integer.parseInt(this.numeroField.getText()) < 0  || Integer.parseInt(this.numeroField.getText()) > 9999 ){
				JOptionPane.showMessageDialog(this, "Número de lindero '" + numeroField.getText()  + "' erróneo. Debería ser un valor numérico entre 0 y 9999.");
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(this, "Número de lindero '" + numeroField.getText()  + "' erróneo. Debería ser un valor numérico entre 0 y 9999.");
			return false;
		}

		if( this.letraDuplicadoField.getText().equals("")){
			JOptionPane.showMessageDialog(this, "Introduzca la letra de duplicado del lindero (1 letras).");
			return false;
		}
		return true;
	}

	public boolean onCancelButtonDo(){
		this.ascEditDialog.setEnabled(true);
		this.ascEditDialog.show(true);
		this.getParent().setEnabled(true);
		this.dispose();
		return true;
	}
	

}
