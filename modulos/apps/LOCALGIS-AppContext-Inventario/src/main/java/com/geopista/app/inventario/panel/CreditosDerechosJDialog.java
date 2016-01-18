/**
 * CreditosDerechosJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.BienJDialog;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 22-sep-2006
 * Time: 10:44:13
 * To change this template use File | Settings | File Templates.
 */
public class CreditosDerechosJDialog extends JDialog implements BienJDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(CreditosDerechosJDialog.class);

	private DatosGeneralesCreditoDerechoJPanel datosGenerales2JPanel;

	private DatosGeneralesComunesJPanel datosGenerales1JPanel;

	private BotoneraAceptarCancelarJPanel botoneraAceptarCancelarJPanel;

	private ApplicationContext aplicacion;

	private CreditoDerechoBean credito;

	private ArrayList actionListeners = new ArrayList();

	private javax.swing.JPanel datosGeneralesJPanel;

	private javax.swing.JTabbedPane datosCreditoDerechoJTabbedPane;

	private ObservacionesJPanel observacionesJPanel;
	
	private GestionDocumentalJPanel documentosJPanel;

	private String operacion;
	private String locale;

	/**
	 * Método que genera el dialogo que muestra los datos de un credito y derecho
	 * @param desktop
	 * @param locale
	 */
	public CreditosDerechosJDialog(JFrame desktop, String locale)
			throws Exception {
		super(desktop);
		this.aplicacion = (AppContext) AppContext.getApplicationContext();
		this.locale=locale;
		getContentPane().setLayout(new BorderLayout());
		renombrarComponentes();
		setModal(true);

		datosCreditoDerechoJTabbedPane = new javax.swing.JTabbedPane();
		datosCreditoDerechoJTabbedPane
				.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
		datosCreditoDerechoJTabbedPane
				.setFont(new java.awt.Font("Arial", 0, 10));

		datosGenerales1JPanel = new DatosGeneralesComunesJPanel(locale);
		datosGenerales2JPanel = new DatosGeneralesCreditoDerechoJPanel(desktop, locale);

		/** Montamos el panel de datos generales */
		datosGeneralesJPanel = new JPanel();
		datosGeneralesJPanel.setBorder(new javax.swing.border.TitledBorder(
				aplicacion.getI18nString("inventario.datosGenerales.tag1")));
		datosGeneralesJPanel.setLayout(new BorderLayout());
		datosGeneralesJPanel.add(datosGenerales1JPanel, BorderLayout.CENTER);
		datosGeneralesJPanel.add(datosGenerales2JPanel, BorderLayout.SOUTH);

		datosCreditoDerechoJTabbedPane.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab1"),
				datosGeneralesJPanel);
	
		observacionesJPanel = new ObservacionesJPanel();
		datosCreditoDerechoJTabbedPane.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab8"),
				observacionesJPanel);

		documentosJPanel = new GestionDocumentalJPanel(false);
		datosCreditoDerechoJTabbedPane.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab12"),
				documentosJPanel);
		if ((operacion != null)
				&& (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))) {
			documentosJPanel.modificarJButtonSetEnabled(false);
		}

		botoneraAceptarCancelarJPanel = new BotoneraAceptarCancelarJPanel();
		botoneraAceptarCancelarJPanel
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						botoneraAceptarCancelarJPanel_actionPerformed();
					}
				});

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		getContentPane()
				.add(datosCreditoDerechoJTabbedPane, BorderLayout.NORTH);
		getContentPane().add(botoneraAceptarCancelarJPanel, BorderLayout.SOUTH);
		//setSize(470, 650);
		setSize(570, 670);
		//setLocation(150, 90);
		 GUIUtil.centreOnWindow(this);
		addAyudaOnline();
	}

	/**
	 * Ayuda Online
	 * 
	 */
	private void addAyudaOnline() {
		getRootPane()
				.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		getRootPane().getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				int indice = datosCreditoDerechoJTabbedPane.getSelectedIndex();
				String uriRelativa = "";
				switch (indice) {
				case 0:
					uriRelativa = "/Geocuenca:Inventario:Créditos_y_Derechos_Personales#Datos_Generales";
					break;
				case 1:
					uriRelativa = "/Geocuenca:Inventario:Créditos_y_Derechos_Personales#Observaciones";
					break;
				case 2:
					uriRelativa = "/Geocuenca:Inventario:Créditos_y_Derechos_Personales#Documentos";
					break;
				default:
					break;
				}
				GeopistaBrowser.openURL(aplicacion
						.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});

	}

	/**
	 * Método que actualiza la operacion que se esta realizando desde el panel padre
	 * @param s operacion
	 */
	public void setOperacion(String s) {
		this.operacion = s;
	}

	/**
	 * Método que carga un credito y derecho en la ventana de dialogo
	 * @param credito a cargar
	 * @param editable true si el dialogo se abre en modo edicion, false en caso contrario
	 */
	public void load(CreditoDerechoBean credito, boolean editable)
			throws Exception {
		setCredito(credito);
		if (operacion == null)
			return;
		if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)) {
			datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(false);
		} else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)) {
			datosGenerales1JPanel.numeroInventarioJLabelSetEnabled(true);
		}
		datosGenerales1JPanel.setEnabled(editable);
		datosGenerales2JPanel.setEnabled(editable);
		datosGenerales1JPanel.load(credito);
		datosGenerales2JPanel.load(credito);

		/** cargamos las observaciones */
		observacionesJPanel.load(credito);
		observacionesJPanel.setEnabled(editable);
		observacionesJPanel.setOperacion(operacion);
	
		/** cargamos los documentos */
		documentosJPanel.load(credito);
		documentosJPanel.setEnabled(editable);
		
		/**añadimos los bienes revertibles*/
		if (credito.getBienesRevertibles()!=null && credito.getBienesRevertibles().size()>0){
        	 JTabbedPane auxJPanel=new JTabbedPane();
       	 datosCreditoDerechoJTabbedPane.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
        	 for (Iterator <BienRevertible>it=credito.getBienesRevertibles().iterator();it.hasNext();)
       	 {
       		 BienesRevertiblesPanel bienesJPanel= new BienesRevertiblesPanel((BienRevertible)it.next(),locale);
       		 bienesJPanel.setEnabled(false);
       		 auxJPanel.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienrevertible") , bienesJPanel);
       	 }
        }
     
	}

	public void renombrarComponentes() {
		try {
			datosGeneralesJPanel
					.setBorder(new javax.swing.border.TitledBorder(aplicacion
							.getI18nString("inventario.datosGenerales.tag1")));
		} catch (Exception e) {
		}
	}

	public void botoneraAceptarCancelarJPanel_actionPerformed() {
		if (!botoneraAceptarCancelarJPanel.aceptarPressed()
				|| (botoneraAceptarCancelarJPanel.aceptarPressed()
						&& operacion
								.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) ? !confirmOption()
						: false))
			credito = null;
		else {
			datosGenerales1JPanel
					.actualizarDatosGeneralesComunes((BienBean) credito);
			datosGenerales2JPanel.actualizarDatosGenerales(credito);

			observacionesJPanel.actualizarDatos((BienBean) credito);
			documentosJPanel.actualizarDatos((BienBean) credito);
		}
		fireActionPerformed();
	}

	/**
	 * Método que abre una ventana de confirmacion sobre la operacion que se esta llevando a cabo
	 */
	private boolean confirmOption() {
		int ok = -1;
		ok = JOptionPane.showConfirmDialog(this, aplicacion
				.getI18nString("inventario.optionpane.tag1"), aplicacion
				.getI18nString("inventario.optionpane.tag2"),
				JOptionPane.YES_NO_OPTION);
		if (ok == JOptionPane.NO_OPTION) {
			return false;
		}
		return true;
	}

	public void addActionListener(ActionListener l) {
		this.actionListeners.add(l);
	}

	public void removeActionListener(ActionListener l) {
		this.actionListeners.remove(l);
	}

	private void fireActionPerformed() {
		for (Iterator i = actionListeners.iterator(); i.hasNext();) {
			ActionListener l = (ActionListener) i.next();
			l.actionPerformed(new ActionEvent(this, 0, null));
		}
	}

	public void setCredito(CreditoDerechoBean credito) {
		this.credito = credito;
	}

	public Object getCredito() {
		return credito;
	}

	private void exitForm(java.awt.event.WindowEvent evt) {
		setCredito(null);
		fireActionPerformed();
	}

	public GestionDocumentalJPanel getDocumentosJPanel() {
		return documentosJPanel;
	}
	public void pmsChecked(){
	    	datosGenerales2JPanel.patrimonioChecked();
	}

	@Override
	public BienBean getBien() {
		return credito;
	}
}
