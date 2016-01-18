/**
 * ValidacionesJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 02-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro.intercambio.edicion.validacion.dialogs;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author fercam
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidacionesJDialog extends JDialog {
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	
	private JButton aceptarjButton;
	private JButton validarjButton;
	
	private JPanel jContentPane = null;
	private JPanel botoneraPanel = null;
	private JPanel datosValidacionPanel = null;
	private JPanel resultadVal = null;
	private JScrollPane jScrollPaneresultad;
	private JTextArea jTextArearesultad;
	private JLabel estadoValidacionesJLabel;
	/**
	 * This is the default constructor
	 */
	public ValidacionesJDialog() {
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(700, 500);
		this.setContentPane(getJContentPane());
		this.setTitle(I18N.get("Expedientes","expedientes.validar.title"));
		
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {

			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());


			estadoValidacionesJLabel = new JLabel();
			estadoValidacionesJLabel.setText(I18N.get("Expedientes","expedientes.validar.estado"));
			
			jContentPane.add(estadoValidacionesJLabel,new GridBagConstraints(0, 0, 1, 1, 1, 1,
					GridBagConstraints.WEST, GridBagConstraints.BOTH, 
					new Insets(0, 5, 0,	5), 0, 0));
			
			jContentPane.add(getResultadVal(),new GridBagConstraints(0, 1, 1, 1, 1, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH, 
							new Insets(0, 5, 0,	5), 0, 0));


			jContentPane.add(getBotoneraPanel(), new GridBagConstraints(0, 2, 1, 1, 1, 1, 
					GridBagConstraints.CENTER,GridBagConstraints.NONE, 
					new Insets(0, 5, 0, 5), 0, 0));

		}
		return jContentPane;
	}
	
	public JPanel getBotoneraPanel() {
		if (botoneraPanel == null) {
			botoneraPanel = new JPanel();
			botoneraPanel.setLayout(new GridBagLayout());

			botoneraPanel.add(getValidarjButton(), new GridBagConstraints(
					0, 0, 1, 1, 1, 1, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			botoneraPanel.add(getAceptarjButton(), new GridBagConstraints(
					1,0, 1, 1, 1, 1, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		}
		return botoneraPanel;
	}

	public void setBotoneraPanel(JPanel botoneraPanel) {
		this.botoneraPanel = botoneraPanel;
	}
	
	public JButton getAceptarjButton() {
		if (aceptarjButton == null) {
			aceptarjButton = new JButton();
			aceptarjButton.setName("btnAceptar");
			aceptarjButton.setText(
					I18N.get("Expedientes","expedientes.validar.boton.aceptar"));

			aceptarjButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent e) {
					dispose();
				}
			});
		}
		return aceptarjButton;
	}

	public void setAceptarjButton(JButton aceptarjButton) {
		this.aceptarjButton = aceptarjButton;
	}

	public JButton getValidarjButton() {
		if (validarjButton == null) {
			validarjButton = new JButton();
			validarjButton.setName("btnValidar");
			validarjButton.setText(
					I18N.get("Expedientes","expedientes.validar.boton.validar"));

		}
		return validarjButton;
	}

	public void setValidarjButton(JButton validarjButton) {
		this.validarjButton = validarjButton;
	}

	public JPanel getResultadVal() {
		if(resultadVal == null){
			resultadVal = new JPanel();
			resultadVal.setLayout(new GridBagLayout());
	  
			resultadVal.add(getjScrollPaneresultad(),	new GridBagConstraints(0, 0, 1, 1, 1,1,
	                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                new Insets(0, 0, 0, 0), 0, 0));
			
		}
		return resultadVal;
	}
	
	public JScrollPane getjScrollPaneresultad() {
		if(jScrollPaneresultad == null){
			jScrollPaneresultad = new JScrollPane(getjTextArearesultad());
			jScrollPaneresultad.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		}
		return jScrollPaneresultad;
	}
	
	public JTextArea getjTextArearesultad() {
		if(jTextArearesultad == null){
			jTextArearesultad = new JTextArea();
			jTextArearesultad.setEditable(false);	
			jTextArearesultad.setLineWrap(true);
			jTextArearesultad.setWrapStyleWord(true);
			jTextArearesultad.setRows(20);
			jTextArearesultad.setColumns(30);
		}
		return jTextArearesultad;
	}

	public void setjTextArearesultad(JTextArea jTextArearesultad) {
		this.jTextArearesultad = jTextArearesultad;
	}
}
