package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import javax.swing.*;

import com.geopista.util.ApplicationContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.bienesRevertibles.BienesRevertiblesPanel;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.app.inventario.BienJDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: angeles Date: 12-jul-2006 Time: 15:45:06 Esta
 * clase se encarga de las AMC de los bienes del tipo Derechos reales
 */

public class JDialogDerechosReales extends JDialog implements BienJDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(InmuebleJDialog.class);

	private DatosGeneralesComunesJPanel jPanelDatosGeneralesComunes;

	private JPanelDatosGeneralesDerechos jPanelDatosGeneralesDerechos;

	private BotoneraAceptarCancelarJPanel jPanelBotonera;

	private ApplicationContext aplicacion;

	private DerechoRealBean bien;

	private ArrayList actionListeners = new ArrayList();

	private javax.swing.JPanel jPanelDatosGenerales;

	private javax.swing.JTabbedPane jTabbedPaneDatos;

	private DatosRegistralesJPanel jPanelDatosRegistrales;

	private ObservacionesJPanel jPanelObservaciones;

	private GestionDocumentalJPanel jPanelDocumentos;
	
	private String operacion;
	private String locale;

	/**
	 * Método que genera el dialogo que muestra los datos de un bien de tipo
	 * Derechos Reales
	 * 
	 * @param desktop
	 * @param locale
	 */
	public JDialogDerechosReales(JFrame desktop, String locale)
			throws Exception {
		super(desktop);
		this.aplicacion = (AppContext) com.geopista.app.AppContext
				.getApplicationContext();
		this.locale=locale;
		getContentPane().setLayout(new BorderLayout());
		renombrarComponentes();
		setModal(true);

		jTabbedPaneDatos = new javax.swing.JTabbedPane();
		jTabbedPaneDatos
				.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
		jTabbedPaneDatos.setFont(new java.awt.Font("Arial", 0, 10));

		jPanelDatosGeneralesComunes = new DatosGeneralesComunesJPanel(locale);
		jPanelDatosGeneralesDerechos = new JPanelDatosGeneralesDerechos(desktop, locale);

		jPanelDatosGenerales = new JPanel();
		// jPanelDatosGenerales.setBorder(new
		// javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag1")));

		jPanelDatosGenerales.setLayout(new BorderLayout());
		jPanelDatosGenerales.add(jPanelDatosGeneralesComunes,
				BorderLayout.CENTER);
		jPanelDatosGenerales.add(jPanelDatosGeneralesDerechos,
				BorderLayout.SOUTH);

		jPanelBotonera = new BotoneraAceptarCancelarJPanel();
		jTabbedPaneDatos.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab1"),
				jPanelDatosGenerales);
		
		jPanelDatosRegistrales = new DatosRegistralesJPanel();
		jTabbedPaneDatos.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab2"),
				jPanelDatosRegistrales);

		jPanelObservaciones = new ObservacionesJPanel();
		jTabbedPaneDatos.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab8"),
				jPanelObservaciones);

		jPanelDocumentos = new GestionDocumentalJPanel(false);
		jTabbedPaneDatos.addTab(aplicacion
				.getI18nString("inventario.inmuebleDialog.tab12"),
				jPanelDocumentos);
		if ((operacion != null)
				&& (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR))) {
			jPanelDocumentos.modificarJButtonSetEnabled(false);
		}

		jPanelBotonera.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptarCancelar();
			}
		});

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		getContentPane().add(jTabbedPaneDatos, BorderLayout.NORTH);
		getContentPane().add(jPanelBotonera, BorderLayout.SOUTH);
		setSize(470, 650);

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
				int indice = jTabbedPaneDatos.getSelectedIndex();
				String uriRelativa = "";
				switch (indice) {
				case 0:
					uriRelativa = "/Geocuenca:Inventario:Derechos_Reales#Datos_Generales";break;
				case 1:
					uriRelativa = "/Geocuenca:Inventario:Derechos_Reales#Datos_Registrales";break;
				case 2:
					uriRelativa = "/Geocuenca:Inventario:Derechos_Reales#Observaciones";break;
				case 3:
					uriRelativa = "/Geocuenca:Inventario:Derechos_Reales#Documentos";break;
				default:
					break;
				}
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});

	}

	/**
	 * Método que actualiza la operacion que se esta realizando desde el panel
	 * padre
	 * 
	 * @param s
	 *            operacion
	 */
	public void setOperacion(String s) {
		this.operacion = s;
	}

	/**
	 * Método que carga un bien inmueble en la ventana de dialogo
	 * 
	 * @param bien
	 * @param editable
	 * @throws Exception
	 */
	public void load(DerechoRealBean bien, boolean editable) throws Exception {
		setBien(bien);
		if (operacion == null)
			return;
		if (operacion.equalsIgnoreCase(Constantes.OPERACION_ANNADIR)) {
			jPanelDatosGeneralesComunes.numeroInventarioJLabelSetEnabled(false);
		} else if (operacion.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR)) {
			jPanelDatosGeneralesComunes.numeroInventarioJLabelSetEnabled(true);
		}
		jPanelDatosGeneralesComunes.setEnabled(editable);
		jPanelDatosRegistrales.setEnabled(editable);
		jPanelObservaciones.setEnabled(editable);
		jPanelDatosGeneralesDerechos.setEnabled(editable);
		jPanelDocumentos.setEnabled(editable);

		jPanelDatosGeneralesComunes.load(bien);
		jPanelDatosGeneralesDerechos.load(bien);
		jPanelDatosRegistrales.load((bien != null ? bien.getRegistro() : null));
		jPanelObservaciones.load(bien);
		jPanelObservaciones.setOperacion(operacion);
				
		jPanelDocumentos.load(bien);
		/**añadimos los bienes revertibles*/
		if (bien.getBienesRevertibles()!=null && bien.getBienesRevertibles().size()>0){
        	 JTabbedPane auxJPanel=new JTabbedPane();
        	 jTabbedPaneDatos.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienesrevertibles") , auxJPanel);
        	 for (Iterator <BienRevertible>it=bien.getBienesRevertibles().iterator();it.hasNext();)
	       	 {
	       		 BienesRevertiblesPanel bienesJPanel= new BienesRevertiblesPanel((BienRevertible)it.next(),locale);
	       		 bienesJPanel.setEnabled(false);
	       		 auxJPanel.addTab(aplicacion.getI18nString("inventario.bienesrevertibles.bienrevertible") , bienesJPanel);
	       	 }
        }

	}

	public void renombrarComponentes() {
		try {
			jPanelDatosGenerales
					.setBorder(new javax.swing.border.TitledBorder(aplicacion
							.getI18nString("inventario.datosGenerales.tag1")));
		} catch (Exception e) {
		}
	}

	public void aceptarCancelar() {
		if ((!jPanelBotonera.aceptarPressed())
				|| (jPanelBotonera.aceptarPressed()
						&& operacion
								.equalsIgnoreCase(Constantes.OPERACION_MODIFICAR) ? !confirmOption()
						: false))
			bien = null;
		else {
			jPanelDatosGeneralesComunes.actualizarDatosGeneralesComunes(bien);
			if (bien != null)
				bien.setRegistro(jPanelDatosRegistrales.actualizarDatos());
			jPanelObservaciones.actualizarDatos(bien);
			jPanelDatosGeneralesDerechos.actualizarDatosGenerales(bien);
			jPanelDocumentos.actualizarDatos(bien);
		}
		fireActionPerformed();
	}

	/**
	 * Método que abre una ventana de confirmacion sobre la operacion que se
	 * esta llevando a cabo
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

	public void setBien(DerechoRealBean bien) {
		this.bien = bien;
	}

	public DerechoRealBean getBien() {
		return bien;
	}

	private void exitForm(java.awt.event.WindowEvent evt) {
		setBien(null);
		fireActionPerformed();
	}
	public void pmsChecked(){
		jPanelDatosGeneralesDerechos.patrimonioChecked();
	}

	@Override
	public GestionDocumentalJPanel getDocumentosJPanel() {
		return jPanelDocumentos;
	}


}
