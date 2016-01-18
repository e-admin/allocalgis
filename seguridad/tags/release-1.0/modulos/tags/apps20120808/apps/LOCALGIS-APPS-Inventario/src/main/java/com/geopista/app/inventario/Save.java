package com.geopista.app.inventario;

import java.awt.Rectangle;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.Municipio;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase tiene las funciones comunes de SaveInventario y SaveCatalogo.
 * 
 * @author fernando
 * 
 */
public abstract class Save extends JDialog {
	protected Logger logger = Logger.getLogger(Save.class);

	private static final long serialVersionUID = 1L;
	protected ResourceBundle messages;
	protected String NOMBRE_ESQUEMA = "";
	protected static File lastDirectory;
	protected Municipio municipio;
	protected int ok = 0;
	protected int mal = 0;
	protected int total = 0;
	protected AppContext aplicacion;

	private String fileText="";

	public Save(java.awt.Frame parent, boolean modal, ResourceBundle messages,
			Municipio municipio) {
		super(parent, modal);
		this.messages = messages;
		this.municipio = municipio;
		this.aplicacion = (AppContext) AppContext.getApplicationContext();

		initComponents();
		addEspecificComponents();
		changeScreenLang(messages);
	}

	public abstract void changeScreenLang(ResourceBundle messages);

	/**
	 * 
	 * @param progressDialog informador del proceso
	 * @return String documento
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	protected abstract String getContentOfDocument(TaskMonitorDialog progressDialog) throws ParserConfigurationException, TransformerException;

	/**
	 * Inicializa los componentes de la ventana
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		jLabelRadio = new JLabel();
		jButtonCancelar = new JButton();
		jButtonGuardar = new JButton();

		jPanelSave = new JPanel();

		jLabelRadio.setBounds(new Rectangle(20, 35, 200, 20));
		jButtonCancelar.setBounds(new Rectangle(200, 180, 85, 20));
		jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				salir();
			}
		});
		jButtonGuardar.setBounds(new Rectangle(60, 180, 130, 20));
		jButtonGuardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				guardarFichero();
			}
		});

		jPanelSave.setLayout(null);

		jPanelSave.add(jLabelRadio, null);
		jPanelSave.add(jButtonGuardar, null);
		jPanelSave.add(jButtonCancelar, null);


		getContentPane().setLayout(new java.awt.BorderLayout());
		getContentPane().add(jPanelSave, java.awt.BorderLayout.CENTER);
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
	 * Guarda en disco el fichero
	 */
	protected abstract void guardarFichero();

	/**
     * 
     */
	protected abstract void addEspecificComponents();

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

	/**
	 * Muestra la pantalla de espera
	 * 
	 * @param titulo
	 */
	protected void initProgressDialog(String titulo){

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);

		progressDialog.setTitle(titulo);
		progressDialog.report(messages
				.getString("inventario.save.dialogo.report1"));
		progressDialog
				.addComponentListener(new java.awt.event.ComponentAdapter() {
					public void componentShown(java.awt.event.ComponentEvent e) {
						new Thread(new Runnable() {
							public void run() {
								String fileTextAux="";
									try {
										fileTextAux = getContentOfDocument(progressDialog);
										setContentOfDoc(fileTextAux);
									} catch (ParserConfigurationException e) {
										logger.error(
												"Se ha producido un error al guardar el inventario",
												e);
										ErrorDialog
												.show(progressDialog,
														"ERROR",
														messages.getString("inventario.saveinventario.error"),
														StringUtil.stackTrace(e));
									} catch (TransformerException e) {
										logger.error(
												"Se ha producido un error al guardar el inventario",
												e);
										ErrorDialog
												.show(progressDialog,
														"ERROR",
														messages.getString("inventario.saveinventario.error"),
														StringUtil.stackTrace(e));								
									}finally {
										progressDialog.setVisible(false);
								}
							}

						}).start();
					}
				});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
	}

	protected void setContentOfDoc(String fichero) {
		this.fileText = fichero;

	}

	protected String getContentOfDoc() {
		return fileText;
	}

	/**
	 * Calcula el numero total de elementos
	 * 
	 * @param doc
	 */
	protected int numeroTotalCatalogo(Document doc) {
		int total = 0;
		Element root = (Element) doc.getDocumentElement();

		NodeList patrimonio = root.getChildNodes();
		for (int i = 0; i < patrimonio.getLength(); i++) {
			if (patrimonio.item(i) instanceof Element) {
				total++;
			}
		}
		return total;
	}

	/**
	 * Crea un documento xml
	 * 
	 * @throws ParserConfigurationException
	 */
	protected Document createDocument() throws ParserConfigurationException {
		// get the factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Using factory get an instance of document builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		DOMImplementation impl = builder.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);

		return doc;
	}

	/**
	 * Parse documento to XML
	 * 
	 * @throws TransformerException
	 */
	protected String parseDocumentToXML(Document doc)
			throws TransformerException {
		// transform the Document into a String
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		// transformer.setOutputProperty
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		// transformer.setOutputProperty
		// ("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		java.io.StringWriter sw = new java.io.StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		String xml = sw.toString();
		return xml;
	}

	protected javax.swing.JButton jButtonCancelar;
	protected javax.swing.JButton jButtonGuardar;
	// protected javax.swing.JPanel jPanelBotonera;
	protected javax.swing.JPanel jPanelSave;
	// protected javax.swing.JTextPane jTextPaneComentario;
	// protected OKCancelPanel okCancelPanel ;
	// protected javax.swing.JButton jButtonAbrir;
	protected javax.swing.JLabel jLabelRadio;
	// protected JRadioButton jRadioAllBienes;
	// protected JRadioButton jRadioListOfBienes;

}
