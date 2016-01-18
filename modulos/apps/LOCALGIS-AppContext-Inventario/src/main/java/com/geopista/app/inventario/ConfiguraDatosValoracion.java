/**
 * ConfiguraDatosValoracion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import java.awt.Rectangle;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

/**
 * Esta clase configura los datos para las valoraciones
 * 
 */
public class ConfiguraDatosValoracion extends JDialog {
	/**
 * Logger for this class
 */
private static final Logger logger = Logger.getLogger(ConfiguraDatosValoracion.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private InventarioClient inventarioClient = null;


	private ResourceBundle messages;


	private Municipio municipio;


	private AppContext aplicacion;
	
	protected javax.swing.JButton jButtonCancelar;
	protected javax.swing.JButton jButtonGuardar;
	// protected javax.swing.JPanel jPanelBotonera;
	protected javax.swing.JPanel jPanelConfig;
	protected javax.swing.JLabel jLabelValorUrbano;
	protected javax.swing.JLabel jLabelValorRustico;
	protected JNumberTextField jTextValorUrbano;
	protected JNumberTextField jTextValorRustico;
	// protected OKCancelPanel okCancelPanel ;
	// protected javax.swing.JButton jButtonAbrir;
	// protected JRadioButton jRadioAllBienes;
	// protected JRadioButton jRadioListOfBienes;



	/**
	 * Constructor de la clase
	 * 
	 * @param parent
	 *            ventana padre
	 * @param modal
	 *            indica si es modal o no
	 * @param messages
	 *            textos de la aplicación
	 */
	public ConfiguraDatosValoracion(java.awt.Frame parent, boolean modal,
			ResourceBundle messages,Municipio municipio) {
		super(parent, modal);
		this.messages = messages;
		this.municipio = municipio;
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		initComponents();
		changeScreenLang(messages);
		com.geopista.app.inventario.UtilidadesComponentes.inicializar();
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)
						+ Constantes.INVENTARIO_SERVLET_NAME);

	}
		
/**
 * Inicializa los componentes de la ventana
 */
private void initComponents() {// GEN-BEGIN:initComponents
	jLabelValorUrbano= new JLabel();
	jLabelValorRustico = new JLabel();
	jTextValorUrbano= new JNumberTextField(JNumberTextField.REAL,1000000,true);
	jTextValorRustico= new JNumberTextField(JNumberTextField.REAL,1000000,true);
	jButtonCancelar = new JButton();
	jButtonGuardar = new JButton();

	jPanelConfig = new JPanel();

	jLabelValorUrbano.setBounds(new Rectangle(30, 30, 100, 20));
	jTextValorUrbano.setBounds(new Rectangle(140, 30, 100, 20));
	jLabelValorRustico.setBounds(new Rectangle(30, 70, 100, 20));	
	jTextValorRustico.setBounds(new Rectangle(140, 70, 100, 20));
	
	jButtonCancelar.setBounds(new Rectangle(170, 120, 85, 20));
	jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			salir();
		}
	});
	jButtonGuardar.setBounds(new Rectangle(60, 120, 85, 20));
	jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			try {
				if(jTextValorUrbano.getNumber()!=null && jTextValorRustico.getNumber()!=null &&jTextValorUrbano.getNumber().doubleValue()<1000000 && jTextValorRustico.getNumber().doubleValue()<1000000){
					inventarioClient.updateDatosValoracion(jTextValorUrbano.getNumber().doubleValue(),jTextValorRustico.getNumber().doubleValue());
					salir();
				}else{
					Object[] rango = { 0,999999.999 };
					logger.error(messages.getString("inventario.getinventario.error"));
					ErrorDialog.show(ConfiguraDatosValoracion.this, "ERROR",
							getStringWithParameters(messages,"NumberDomain.NumberOutOfBounds",rango),null);
					
				}		
				
			} catch (ParseException e) {
				
				logger.error(messages.getString("inventario.getinventario.error"));
				ErrorDialog.show(ConfiguraDatosValoracion.this, "ERROR",
						messages.getString("NumberDomain.NumberFormatError"),
						StringUtil.stackTrace(e));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});

	jPanelConfig.setLayout(null);

	jPanelConfig.add(jLabelValorUrbano, null);
	jPanelConfig.add(jTextValorUrbano, null);
	jPanelConfig.add(jLabelValorRustico, null);
	jPanelConfig.add(jTextValorRustico, null);
	jPanelConfig.add(jButtonGuardar, null);
	jPanelConfig.add(jButtonCancelar, null);


	getContentPane().setLayout(new java.awt.BorderLayout());
	getContentPane().add(jPanelConfig, java.awt.BorderLayout.CENTER);
	// getContentPane().add(jScrollPane, java.awt.BorderLayout.NORTH);
	// getContentPane().add(okCancelPanel, java.awt.BorderLayout.SOUTH);
	pack();
}

/**
 * Función que se ejecuta al salir
 */
protected void salir() {
	dispose();
}
	

	/**
	 * Cambia el lenguaje de los textos
	 * 
	 * @param messages
	 */
	public void changeScreenLang(ResourceBundle messages) {

		try {
			setTitle(GeopistaUtil.i18n_getname(
					"inventario.confDatosValoracion.title", messages));
			// jPanelLoad.setToolTipText(GeopistaUtil.i18n_getname(
			// "inventario.loadinventario.title", messages));
			jLabelValorUrbano.setText(GeopistaUtil.i18n_getname("inventario.confDatosValoracion.labelUrbano",
					messages));
			jLabelValorRustico.setText(GeopistaUtil.i18n_getname("inventario.confDatosValoracion.labelRustico",
					messages));
			jButtonCancelar.setText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonGuardar.setText(GeopistaUtil.i18n_getname(
					"document.infodocument.botones.guardar", messages));


		} catch (Exception ex) {
			logger.error("Falta algun recurso:", ex);
		}
	}
	
	/**
	 * Devuelve un String del resourceBundle con parametros
	 * 
	 * @param key
	 * @param valores
	 * @return
	 */
	protected String getStringWithParameters(ResourceBundle messages,
			String key, Object[] valores) {
		try {
			MessageFormat messageForm = new MessageFormat("");
			messageForm.setLocale(messages.getLocale());
			String pattern = messages.getString(key);
			messageForm.applyPattern(pattern);
			return messageForm.format(valores, new StringBuffer(), null)
					.toString();
		} catch (Exception ex) {
			logger.error("Excepción al recoger el recurso:" + key, ex);
			return "undefined";
		}
	}

}
