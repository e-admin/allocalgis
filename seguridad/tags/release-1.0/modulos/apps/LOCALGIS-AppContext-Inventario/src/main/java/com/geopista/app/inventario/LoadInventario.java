package com.geopista.app.inventario;

import java.awt.HeadlessException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.apache.log4j.Logger;
import org.deegree.xml.XMLTools;

import com.geopista.app.AppContext;
import com.geopista.app.metadatos.xml.XMLTranslator_LCGIII;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.Version;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.Documentable;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.CompanniaSeguros;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InmuebleRusticoBean;
import com.geopista.protocol.inventario.InmuebleUrbanoBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.Mejora;
import com.geopista.protocol.inventario.MuebleBean;
import com.geopista.protocol.inventario.Observacion;
import com.geopista.protocol.inventario.ReferenciaCatastral;
import com.geopista.protocol.inventario.RegistroBean;
import com.geopista.protocol.inventario.Seguro;
import com.geopista.protocol.inventario.SemovienteBean;
import com.geopista.protocol.inventario.UsoFuncional;
import com.geopista.protocol.inventario.ValorMobiliarioBean;
import com.geopista.protocol.inventario.VehiculoBean;
import com.geopista.protocol.inventario.ViaBean;

import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;

import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase importa el inventario de patrimonio de un fichero xml que sigue un
 * esquema a la base de datos.
 * 
 * User: angeles Date: 7-Abril-2010 Time: 19:15:03
 */
public class LoadInventario extends Load {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TIPO_FICHERO = "patrimonio";
	private InventarioClient inventarioClient = null;
	private DocumentClient documentClient = null;
	private GeopistaEditor geopistaEditor;
	private String mensajeResultado = "";
	private boolean showErrorMessage = true;
	private int numBienRevertible = 0;
	private int numLote = 0;
	private int numDocLote = 0;
	private int numCreditoDerecho = 0;
	private int numDocCreditoDerecho = 0;
	private int numArrendamiento = 0;
	private int numDocArrendamiento = 0;
	private int numDerechoReal = 0;
	private int numDocDerechoReal = 0;
	private int numInmuebleRustico = 0;
	private int numDocInmuebleRustico = 0;
	private int numInmuebleUrbano = 0;
	private int numDocInmuebleUrbano = 0;
	private int numMueble = 0;
	private int numDocMueble = 0;
	private int numSemoviente = 0;
	private int numDocSemoviente = 0;
	private int numVehiculo = 0;
	private int numDocVehiculo = 0;
	private int numViaUrbana = 0;
	private int numDocViaUrbana = 0;
	private int numViaRustica = 0;
	private int numDocViaRustica = 0;
	private int numHistoricoArtistico = 0;
	private int numDocHistoricoArtistico = 0;
	private int numValorMobiliario = 0;
	private int numDocValorMobiliario = 0;
	private int numDocBienRevertible = 0;
	private int documentosOK = 0;
	private int documentosError = 0;
	private boolean actualizacionTotal = false;
	private boolean actualizacionIncrementalConservar = false;
	private String DOCUMENT_PATH = null;
	private String DOCUMENT_SERVER_PATH = null;

	private String fileName;

	private boolean modoDesatendido=false;

	private String documentPath;
	
	
	private boolean MODO_CARGA=true;

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
	public LoadInventario(GeopistaEditor geopistaEditor,
						InventarioClient inventarioClient,DocumentClient documentClient,
						Municipio municipio,ResourceBundle messages) {
		super(municipio,messages);
		this.geopistaEditor = geopistaEditor;
		this.inventarioClient = inventarioClient;
		this.documentClient = documentClient;
		this.modoDesatendido=true;
		this.aplicacion= (AppContext) AppContext.getApplicationContext();
		NOMBRE_ESQUEMA = "patrimonio.xsd";

	}
	
	public LoadInventario(java.awt.Frame parent, boolean modal,
			ResourceBundle messages, GeopistaEditor geopistaEditor,
			Municipio municipio) {
		super(parent, modal, messages, municipio);
		this.geopistaEditor = geopistaEditor;
		com.geopista.app.inventario.UtilidadesComponentes.inicializar();
		inventarioClient = new InventarioClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ ServletConstants.INVENTARIO_SERVLET_NAME);
		documentClient = new DocumentClient(
				aplicacion
						.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
						+ ServletConstants.DOCUMENT_SERVLET_NAME);

		NOMBRE_ESQUEMA = "patrimonio.xsd";

	}
	

	/**
	 * Funciòn que se ejecuta cuando el usuario pincha la opción aceptar.
	 */
	protected void aceptar() {
		try {
			if (okCancelPanel.wasOKPressed()) {
				if (jTextFieldFichero.getText().trim().length() == 0) {
					JOptionPane optionPane = new JOptionPane(GeopistaUtil
							.i18n_getname("inventario.load.seleccionar",
									messages), JOptionPane.WARNING_MESSAGE);
					JDialog dialog = optionPane.createDialog(this, GeopistaUtil
							.i18n_getname("inventario.loadinventario.title",
									messages));
					dialog.setVisible(true);
					return;
				}
				if (validateSAXdocumentWithXSD(jTextFieldFichero.getText(),
						TIPO_FICHERO) <= 0) {
					JOptionPane optionError = new JOptionPane(GeopistaUtil
							.i18n_getname("inventario.loadinventario.nofiles",
									messages)
							+ ": " + TIPO_FICHERO, JOptionPane.ERROR_MESSAGE);
					JDialog dialogError = optionError.createDialog(this,
							GeopistaUtil
									.i18n_getname(
											"inventario.loadinventario.error",
											messages));
					dialogError.setVisible(true);
					return;
				}
				Object[] options = {
						GeopistaUtil.i18n_getname(
								"inventario.loadinventario.actualizaciontotal",
								messages),
						GeopistaUtil
								.i18n_getname(
										"inventario.loadinventario.actualizacionincremental",
										messages),
						GeopistaUtil.i18n_getname(
								"inventario.loadinventario.opcion4", messages) };
				String[] literalMunicipio = { municipio.getNombre() };
				int opcionElegida = JOptionPane
						.showOptionDialog(this, getStringWithParameters(
								messages,
								"inventario.loadinventario.atencion1",
								literalMunicipio), GeopistaUtil.i18n_getname(
								"inventario.tools.tag2", messages),
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);

				if (opcionElegida == 0) {
					actualizacionTotalInventario();

				} else if (opcionElegida == 1) {
					actualizacionIncrementalInventario();
				}

			} else {
				salir();
			}
		} catch (org.xml.sax.SAXException saex) {
			logger.error("Se ha producido un error al fichero con su esquema",
					saex);
			ErrorDialog.show(this, "ERROR", GeopistaUtil.i18n_getname(
					"inventario.loadinventario.error.esquema", messages),
					StringUtil.stackTrace(saex));
		} catch (Exception ex) {
			logger.error("Se ha producido un error al cargar el fichero", ex);
			ErrorDialog.show(this, "ERROR", GeopistaUtil.i18n_getname(
					"inventario.loadinventario.error", messages), StringUtil
					.stackTrace(ex));
		}
	}

	/**
	 * Metodo para gestionar la actualizacion total del inventario. Los bienes
	 * se borran y se empieza de cero.
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	private void actualizacionTotalInventario() throws HeadlessException,
			Exception {

		int opcion = JOptionPane.showOptionDialog(this, GeopistaUtil
				.i18n_getname("inventario.loadinventario.cargatotal")
				+ municipio.getNombre(), GeopistaUtil
				.i18n_getname("inventario.loadinventario.estaseguro"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);
		if (opcion != 0)
			return;
		actualizacionTotal = true;
		initProgressDialog(GeopistaUtil.i18n_getname(
				"inventario.loadinventario.dialogo.title", messages));
		if (total > 0) {
			Object[] resultados = { new Integer(ok), new Integer(total),
					new Integer(mal) };
			if (mal == 0) {
				ErrorDialog.show(this, JOptionPane.INFORMATION_MESSAGE,
						"RESULTADO", getStringWithParameters(messages,
								"inventario.loadinventario.ok", resultados),
						mensajeResultado);
			} else {
				ErrorDialog.show(this, "RESULTADO",
						getStringWithParameters(messages,
								"inventario.loadinventario.nook", resultados),
						mensajeResultado);
			}
		}
		salir();
	}

	/**
	 * Metodo para gestionar la actualizacion incremental del inventario
	 * 
	 * @throws HeadlessException
	 * @throws Exception
	 */
	private void actualizacionIncrementalInventario() throws HeadlessException,
			Exception {

		actualizacionTotal = false;
		Object[] options = {
				GeopistaUtil.i18n_getname(
						"inventario.loadinventario.actualizar", messages),
				GeopistaUtil.i18n_getname(
						"inventario.loadinventario.conservar", messages),
				GeopistaUtil.i18n_getname("inventario.loadinventario.opcion4",
						messages) };

		String[] literalMunicipio = { municipio.getNombre() };
		int opcionElegida = JOptionPane
				.showOptionDialog(this,
						getStringWithParameters(messages,
								"inventario.loadinventario.atencion2",
								literalMunicipio), GeopistaUtil.i18n_getname(
								"inventario.tools.tag2", messages),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		// TODO Realizar casos distintos
		if ((opcionElegida == 0) || (opcionElegida == 1)) {
			actualizacionIncrementalConservar = (opcionElegida == 1);
			initProgressDialog(GeopistaUtil.i18n_getname(
					"inventario.loadinventario.dialogo.title", messages));
			if (total > 0) {
				Object[] resultados = { new Integer(ok), new Integer(total),
						new Integer(mal) };
				if (mal == 0) {
					ErrorDialog
							.show(this, JOptionPane.INFORMATION_MESSAGE,
									"RESULTADO", getStringWithParameters(
											messages,
											"inventario.loadinventario.ok",
											resultados), mensajeResultado);
				} else {
					ErrorDialog.show(this, "RESULTADO",
							getStringWithParameters(messages,
									"inventario.loadinventario.nook",
									resultados), mensajeResultado);
				}
			}
			salir();
		}

	}

	private boolean municipioCorrecto(Element root) {
		String idMunicipio = root.getAttribute("id_municipio");
		if (idMunicipio == null)
			return true;
		return municipio.getId().endsWith(idMunicipio);
	}
	private void setDocumentPath(String documentPath){
		this.documentPath=documentPath;
	}
	
	private String getDocumentPath(){
		return this.documentPath;
	}
	
	private void setFileName(String fileName){
		this.fileName=fileName;
	}

	private String getFileName(){
		if (this.fileName==null)
			return jTextFieldFichero.getText();
		else
			return this.fileName;
	}
	/***
	 * Carga los valores del inventario
	 * 
	 * @param fileName
	 */
	protected void cargaDatos(TaskMonitorDialog progressDialog)
			throws Exception {
		
		String nombreFichero=getFileName();
		Document doc = parseXmlFile(nombreFichero);
		try {
			Element root = (Element) doc.getDocumentElement();
			
			if (!municipioCorrecto(root)) {
				ErrorDialog.show(this,JOptionPane.INFORMATION_MESSAGE,"RESULTADO",
								GeopistaUtil.i18n_getname("inventario.loadinventario.nocodigo"),
								GeopistaUtil.i18n_getname("inventario.loadinventario.nocodigodetalle")
										+ " " + municipio.getId());
				logger.error("El municipio del fichero no es el adecuado");
				return;
			}
			// Si es actualizacion total borramos lo anterior
			if (actualizacionTotal) {
				jLabelInformacion
						.setText(GeopistaUtil
								.i18n_getname("inventario.loadinventario.eliminandodatos"));
				progressDialog
						.report(GeopistaUtil
								.i18n_getname("inventario.loadinventario.eliminandodatos"));
				inventarioClient.eliminarTodoInventario();
			}

			HashMap<String, Integer> totalElementos = numeroTotalInventario(doc);
			total = totalElementos.get(Constantes.TIPOS_INVENTARIOS[0]);
			
			String texto="Elementos a cargar:\n"+ getElementosACargar(totalElementos);
			if (modoDesatendido)
				logger.info(texto);
			else
				JOptionPane.showMessageDialog(this, texto, "Resultado",JOptionPane.INFORMATION_MESSAGE);

			int numeroElemento = 0;
			ok = 0;
			mal = 0;

			NodeList patrimonio = root.getChildNodes();
			for (int i = 0; i < patrimonio.getLength()
					&& !progressDialog.isCancelRequested(); i++) {
				if (patrimonio.item(i) instanceof Element) {
					numeroElemento++;
					Object[] resultados = { new Integer(numeroElemento),new Integer(total) };
					
					if (jLabelInformacion!=null)
							jLabelInformacion.setText(getStringWithParameters(messages,
									"inventario.load.dialogo.report2", resultados));
					progressDialog.report(getStringWithParameters(messages,	"inventario.load.dialogo.report2", resultados));
					Element elementoPatrimonio = (Element) patrimonio.item(i);
					Object bien = null;
					long startMils=Calendar.getInstance().getTimeInMillis();
					logger.info("Importando:"+ elementoPatrimonio.getNodeName());
					try {
						if (elementoPatrimonio.getNodeName().equalsIgnoreCase("creditos_derechos"))
							bien = createBeanCreditoDerecho(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("derechos_reales"))
							bien = createBeanDerechoReal(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("inmuebles_rusticos")) {
							// Verificamos si es inmueble rustico de tipo via
							if (isInmuebleTipoVia(elementoPatrimonio,"datos_rusticos","cat_clase_rust"))
								bien = createBeanViaRustica(elementoPatrimonio);
							else
								bien = createBeanInmuebleRustico(elementoPatrimonio);
						} else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("inmuebles_urbanos")){
							if (isInmuebleTipoVia(elementoPatrimonio,"datos_urbanos","cat_clase_urb"))
								bien = createBeanViaUrbana(elementoPatrimonio);
							else
								bien = createBeanInmuebleUrbano(elementoPatrimonio);							
						}
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("historicos_artisticos"))
							bien = createBeanHistoricoArtistico(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("otros_muebles"))
							bien = createBeanOtroMueble(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("semovientes"))
							bien = createBeanSemoviente(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("valores_mobiliarios"))
							bien = createBeanValorMobiliario(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("vehiculos"))
							bien = createBeanVehiculo(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("vias_rusticas"))
							bien = createBeanViaRustica(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("vias_urbanas"))
							bien = createBeanViaUrbana(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("bienes_revertibles"))
							bien = createBeanBienRevertible(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("lotes"))
							bien = createBeanLote(elementoPatrimonio);
						else if (elementoPatrimonio.getNodeName().equalsIgnoreCase("resumen_contenido")) {
							continue;
						}
						// geopistaEditor.getLayerViewPanel().getLayerManager().getLayers();
						if (bien == null) {
							logger.error("Tipo de bien no encontrado: "
									+ elementoPatrimonio.getNodeName());
							mal++;
							mensajeResultado += "\n**** Error: " + mal
									+ " - Tipo de bien no encontrado: "
									+ elementoPatrimonio.getNodeName() + " \n";
						} else {
							if (MODO_CARGA)
								insertar(bien);
						}
						long endMils=Calendar.getInstance().getTimeInMillis();
						logger.info("End Time:"+elementoPatrimonio.getNodeName()+" :"+(endMils-startMils)/1000+" segundos");
					} catch (Throwable t) {
						mal++;
						String numInventario="";
						if (bien instanceof BienBean){
							numInventario=((BienBean) bien).getNumInventario();
							mensajeResultado += "\n**** Error: "
									+ mal
									+ " - Error al cargar el bien numero "
									+ numeroElemento
									+ " de tipo "
									+ elementoPatrimonio.getNodeName()
									+ " Numero inventario: "
									+ ((BienBean) bien).getNumInventario()
									+ " Error:"
									+ (t.getMessage() != null ? t.getMessage()
											: (t.getCause() != null
													&& t.getCause()
															.getMessage() != null ? t
													.getCause().getMessage()
													: t));
						}
						else
							mensajeResultado += "\n**** Error: "
									+ mal
									+ " - Error al cargar el bien numero "
									+ numeroElemento
									+ " de tipo "
									+ elementoPatrimonio.getNodeName()
									+ ": "
									+ (t.getMessage() != null ? t.getMessage()
											: (t.getCause() != null
													&& t.getCause()
															.getMessage() != null ? t
													.getCause().getMessage()
													: t));
						logger.error("Error al cargar el bien", t);
						logger.error("Tipo de bien erroneo:"+ elementoPatrimonio.getNodeName()+" Num Inventario:"+numInventario);
					}

				}
			}
		} finally {
			doc = null;
		}
		Object[] resultados = { new Integer(ok), new Integer(total) };
		if (jLabelInformacion!=null)
			jLabelInformacion.setText(getStringWithParameters(messages,
				"inventario.load.dialogo.report3", resultados));

		logger.info("Documentos encontrados:" + documentosOK);
		Object[] resultadosDoc = { new Integer(documentosOK),
				new Integer(documentosError),
				new Integer(documentosOK + documentosError) };
		Object[] resultadosFinales = { new Integer(numInmuebleUrbano),
				new Integer(numDocInmuebleUrbano),
				new Integer(numInmuebleRustico),
				new Integer(numDocInmuebleRustico), new Integer(numViaUrbana),
				new Integer(numDocViaUrbana), new Integer(numViaRustica),
				new Integer(numDocViaRustica), new Integer(numDerechoReal),
				new Integer(numDocDerechoReal),
				new Integer(numHistoricoArtistico),
				new Integer(numDocHistoricoArtistico),
				new Integer(numValorMobiliario),
				new Integer(numDocValorMobiliario),
				new Integer(numCreditoDerecho),
				new Integer(numDocCreditoDerecho),
				new Integer(numArrendamiento),
				new Integer(numDocArrendamiento), new Integer(numVehiculo),
				new Integer(numDocVehiculo), new Integer(numSemoviente),
				new Integer(numDocSemoviente), new Integer(numMueble),
				new Integer(numDocMueble), new Integer(numLote),
				new Integer(numDocLote), new Integer(numBienRevertible),
				new Integer(numDocBienRevertible) };

		try {
			Logger loggerLoad = Logger.getLogger("load");
			org.apache.log4j.FileAppender fileAppenderNew = null;
			try {

				Date date = new Date();
				String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
				long mils = date.getTime();

				org.apache.log4j.FileAppender fileAppender = (org.apache.log4j.FileAppender) loggerLoad
						.getAppender("load");
				org.apache.log4j.Layout layout = fileAppender.getLayout();
				// org.apache.log4j.FileAppender fileAppenderNew =new
				// org.apache.log4j.FileAppender(layout,"FicheroCargaPatrimonio-"+municipio.getId()+"_"+municipio.getNombre()+"_"+fecha+"_"+mils+".log",
				// false);
				fileAppenderNew = new org.apache.log4j.FileAppender(layout,
						"FicheroCargaPatrimonio-" + municipio.getId() + "_"
								+ municipio.getNombre() + "_" + fecha + ".log");
				fileAppenderNew.setAppend(true);
				loggerLoad.addAppender(fileAppenderNew);

			} catch (Exception ex) {

			}

			org.apache.log4j.MDC.put("sesion",
					com.geopista.security.SecurityManager.getUserPrincipal()
							.getName());
			org.apache.log4j.MDC.put("municipio", municipio.getNombre());
			org.apache.log4j.MDC.put("CodigoIne", municipio.getId());
			loggerLoad
					.info("\n\n***************************************************");
			loggerLoad.info("********* CARGA INVENTARIO PARA: "
					+ municipio.getNombre());
			loggerLoad.info(getStringWithParameters(messages,
					"inventario.load.dialogo.report3", resultados));
			loggerLoad.info(getStringWithParameters(messages,
					"inventario.load.dialogo.report5", resultadosDoc).replace(
					'\n', ' '));
			loggerLoad.info(getStringWithParameters(messages,
					"inventario.load.dialogo.report4", resultadosFinales));
			loggerLoad
					.info("********* ERRORES Y WARNINGS ***********************");
			loggerLoad.info(mensajeResultado);
			loggerLoad.info("********* FIN CARGA PARA: "
					+ municipio.getNombre());
			loggerLoad
					.info("***************************************************");
			loggerLoad.removeAppender(fileAppenderNew);

		} catch (Exception ex) {
		}
		mensajeResultado += getStringWithParameters(messages,
				"inventario.load.dialogo.report5", resultadosDoc);
		mensajeResultado += getStringWithParameters(messages,
				"inventario.load.dialogo.report4", resultadosFinales);
		updateVersion();
	}

	private void insertar(Object bien) throws Exception {
		String numInventario=null;
		try {

			
			// int
			// numDocumentosIniciales=((BienBean)bien).getDocumentos().size();
			if (bien instanceof BienBean) {
				numInventario=((BienBean)bien).getNumInventario();
				insertarBien((BienBean) bien);
			} else if (bien instanceof Lote) {
				numInventario=((Lote)bien).getNombre_lote();
				insertarLote((Lote) bien);
			} else {
				numInventario=((BienRevertible)bien).getNumInventario();
				insertarBienRevertible((BienRevertible) bien);
			}
			if (DOCUMENT_SERVER_PATH != null && bien instanceof Documentable
					&& ((Documentable) bien).getDocumentos() != null) {
				logger.info("Numero de documentos de bien:"
						+ ((Documentable) bien).getDocumentos().size());
				actualizaDatosDocOk(bien, ((Documentable) bien).getDocumentos().size());
			}
		} catch (Exception ex) {
			if (DOCUMENT_SERVER_PATH == null)
				throw ex;
			if (ex.getCause() != null
					&& ((ex.getCause().toString().indexOf("FileNotFound") >= 0) ||
					(ex.getCause().toString().indexOf("Documento vacio") >= 0))){
				logger.error("Error al anexar el documento: "+ ex.getCause().getMessage(), ex);

				if (showErrorMessage) {
					Object[] options = {
							aplicacion
									.getI18nString("inventario.loadinventario.opcion1"),
							aplicacion
									.getI18nString("inventario.loadinventario.opcion2"),
							aplicacion
									.getI18nString("inventario.loadinventario.opcion3"),
							aplicacion
									.getI18nString("inventario.loadinventario.opcion4") };
					int opcion = JOptionPane
							.showOptionDialog(this,
									GeopistaUtil.i18n_getname("inventario.loadinventario.errorficheroremoto")
											+ " \n"+ ex.getCause().getMessage(),
									GeopistaUtil
											.i18n_getname("inventario.loadinventario.errorficheroremototitle"),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.ERROR_MESSAGE, null, options,
									options[0]);
					if (opcion == 1)
						showErrorMessage = false;
					if (opcion == 2) {
						DOCUMENT_SERVER_PATH = getDocumentDirectory();
						insertar(bien);
						return;
					}
					if (opcion == 3)
						throw new Exception(
								"No se ha podido anexar el fichero remoto: \n"+ ex.getCause().getMessage());
				}
				// *por aqui viene si se acepta 1 o todos
				documentosError++;
				mensajeResultado += "\n**** WARNING: Error al anexar el documento: "+ ex.getCause().getMessage()+" para el bien:"+numInventario;
				// quitamos el documento
				Collection<DocumentBean> documentos = ((Documentable) bien).getDocumentos();
				Vector<DocumentBean> documentosAux = new Vector();
				for (Iterator<DocumentBean> it = documentos.iterator(); it
						.hasNext();) {
					DocumentBean documento = (DocumentBean) it.next();
					if (ex.getCause().getMessage().indexOf(documento.getFileName()) < 0) {
						documentosAux.add(documento);
					}
				}
				((Documentable) bien).setDocumentos(documentosAux);
				// lo volvemos a intentar
				insertar(bien);
			} else
				throw ex;

		}

	}

	/**
	 * Inserta un bien revertible
	 * 
	 * @param bien
	 *            --> Bien revertible a insertar en la base de datos
	 * @throws Exception
	 */
	private void insertarBienRevertible(BienRevertible bien) throws Exception {
		if (!actualizacionTotal) {

			BienRevertible bienOld = inventarioClient
					.getBienRevertibleByNumInventario(bien.getNumInventario());
			if (bienOld != null) {
				if (actualizacionIncrementalConservar) {
					// tiene referencia catastral y no se ha encontrado lo
					// apuntamos como un WARNING y añadimos la observación
					mensajeResultado += "\n**** INFO: "
							+ "Se ha conservado el bien con Número de Inventario: "
							+ bien.getNumInventario();
					ok++;
					return;
				} else {
					bien.setId(bienOld.getId());
					mensajeResultado += "\n**** INFO: "
							+ "Se ha actualizado el bien con Número de Inventario: "
							+ bien.getNumInventario();
				}
			}
		}
		bien = (BienRevertible) inventarioClient.updateInventario(bien,
				getDocumentFiles(bien.getDocumentos(), bien));
		ok++;
	}

	/**
	 * Inserta un lote en la base de datos
	 * 
	 * @param lote
	 *            lote a insertar
	 * @throws Exception
	 */
	private void insertarLote(Lote lote) throws Exception {
		if (!actualizacionTotal) {
			// Lote loteAux=inventarioClient.getL
		}

		inventarioClient.updateInventario(lote, getDocumentFiles(lote
				.getDocumentos(), lote));
		ok++;
	}

	/**
	 * Devuelve un string con los elementos a cargar
	 * 
	 * @param elementos
	 * @return
	 */
	private String getElementosACargar(HashMap<String, Integer> elementos) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < Constantes.TIPOS_INVENTARIOS.length; i++) {
			if (elementos.get(Constantes.TIPOS_INVENTARIOS[i]) != null)
				sb
						.append(Constantes.TIPOS_INVENTARIOS[i]
								+ " "
								+ elementos
										.get(Constantes.TIPOS_INVENTARIOS[i])
								+ "\n");
			else
				sb.append(Constantes.TIPOS_INVENTARIOS[i] + " " + 0 + "\n");
		}
		return sb.toString();
	}

	private void insertarBien(BienBean bien) throws Exception {
		Object features[] = bien.getIdFeatures();
		
		//Obtenemos la informacion del Identificador de la feature. No
		//podemos serialiar el objeto GeopistaFeature completo por lo que
		//al bien en lugar de meterle un array de GeopistaFeature le 
		//metemos un array de Objetos.
		Object bienCopia= null;
        Object[] idFeatures;
        Object[] idLayers;
        if (features!=null){
	        idFeatures= new Object[features.length];
	        idLayers= new Object[features.length];
	        for (int i=0;i<features.length;i++){
	            GeopistaFeature feature= (GeopistaFeature)features[i];
	            idFeatures[i]= (String)feature.getSystemId();
	            idLayers[i]= (String)feature.getLayer().getSystemId();
	        }
        }
        else{
        	idFeatures= new Object[0];
	        idLayers= new Object[0];
        }
		
		
		try {
			// Si viene algo en la referencia catastral y no se ha encontrado
			// se devuelve un error
			if (!actualizacionTotal) {
				BienBean bienOld = inventarioClient.getBienByNumInventario(bien.getNumInventario());
				if (bienOld != null) {
					if (actualizacionIncrementalConservar) {
						mensajeResultado += "\n**** INFO: "
								+ "Se ha conservado el bien con Número de Inventario: "
								+ bien.getNumInventario();
						ok++;
						return;
					} else {
						bien.setId(bienOld.getId());
						
						bien.setIdFeatures((Collection) null);
						bien.setIdLayers((Collection) null);
						bien.setIdFeatures(idFeatures);
						bien.setIdLayers(idLayers);
						
						checkGeorreferenciacion(bien,features,idFeatures,idLayers);
						
						mensajeResultado += "\n**** INFO: "
								+ "Se ha actualizado el bien con Número de Inventario: "
								+ bien.getNumInventario();
						bien = (BienBean) inventarioClient.updateInventario(bien, getDocumentFiles(bien.getDocumentos(),bien));
						ok++;
						return;
					}
				}
			}
						
			checkGeorreferenciacion(bien,features,idFeatures,idLayers);
						
				
			// ASO 15-Dic-2010 insertInventario puede devolver
			// un vector de bienes en el caso de los lotes
			Object respuesta = inventarioClient.insertInventario(features,
					bien, getDocumentFiles(bien.getDocumentos(), bien));
			if (respuesta instanceof java.util.Vector) // Si se devuelve un
														// vector tomamos el
														// primer elemento
				bien = (BienBean) ((java.util.Vector) respuesta).get(0);
			else
				bien = (BienBean) respuesta;
			ok++;
		} catch (Exception ex) {
			((BienBean) bien).setObjectFeatures(features);
			throw ex;
		}

	}

	/**
	 * Modificacion del mensaje de error
	 * @param bien
	 */
	private void checkGeorreferenciacion(BienBean bien,Object features[],
						Object[] idFeatures,Object[] idLayers){
		
		if (bien.getTipoGeorreferenciacion()==BienBean.GEO_CATASTRAL){
			
			if (bien.getRefCatastralOrigen() == null
					|| bien.getRefCatastralOrigen().length() == 0){
				mensajeResultado += "\n**** WARNING: "
					+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.referenciablanco"):"")
					+ " - Número de Inventario: "
					+ ((BienBean) bien).getNumInventario();
				bien.setIdFeatures((Collection) null);
			}
			else if (bien.getRefCatastralOrigen() == null
						|| bien.getRefCatastralOrigen().length() == 0
						|| features != null && features.length > 0) {
				bien.setIdFeatures((Collection) null);
				bien.setIdLayers((Collection) null);
				bien.setIdFeatures(idFeatures);
				bien.setIdLayers(idLayers);				
			}
			else{			
				//Si no se ha encontrado ningun elemento pero tiene una referencia
				//y es una via cambiamos el tipo de mensaje.
				if ((bien.getRefCatastralOrigen() != null) &&
						(bien.getTipo()==Const.PATRON_VIAS_PUBLICAS_RUSTICAS ||
								bien.getTipo()==Const.PATRON_VIAS_PUBLICAS_URBANAS)
								&& (features == null || features.length == 0)) {
					mensajeResultado += "\n**** WARNING: "
						+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.referencianoencontrada"):"")
						+ " [" + bien.getRefCatastralOrigen()
						+ "] para el bien - Número de Inventario: "
						+ ((BienBean) bien).getNumInventario()
						+ " [ Warning. El bien es una via y tiene referencia catastral] ";
				bien.setIdFeatures((Collection) null);
				Observacion obs = new Observacion(
						"WARNING - "
								+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.referencianoencontrada"):"")
								+ " [" + bien.getRefCatastralOrigen() + "]"
								+ " [ Warning. El bien es una via y tiene referencia catastral] ");
				bien.addObservacion(obs);
				}
				else{
					if ((bien.getRefCatastralOrigen() != null) 
						&& (features == null || features.length == 0)){
					// tiene referencia catastral y no se ha encontrado lo apuntamos
					// como un WARNING y añadimos la observación
					mensajeResultado += "\n**** WARNING: "
							+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.referencianoencontrada"):"")
							+ " [" + bien.getRefCatastralOrigen()
							+ "] para el bien - Número de Inventario: "
							+ ((BienBean) bien).getNumInventario();
					bien.setIdFeatures((Collection) null);
					Observacion obs = new Observacion(
							"WARNING - "
									+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.referencianoencontrada"):"")
									+ " [" + bien.getRefCatastralOrigen() + "]");
					bien.addObservacion(obs);
					}
				}
			}						
		}
		else if (bien.getTipoGeorreferenciacion()==BienBean.GEO_XY){
			if ((bien.getCoordenadasXY() != null) 
					&& (features == null || features.length == 0)){
				// tiene coordenadasXY y no se ha encontrado lo apuntamos
				// como un WARNING y añadimos la observación
				mensajeResultado += "\n**** WARNING: "
						+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.coordenadaxynoencontrada"):"")
						+ " [" + bien.getCoordenadasXY()
						+ "] para el bien - Número de Inventario: "
						+ ((BienBean) bien).getNumInventario();
				bien.setIdFeatures((Collection) null);
				Observacion obs = new Observacion(
						"WARNING - "
								+ (aplicacion!=null?aplicacion.getI18nString("inventario.loadinventario.coordenadaxynoencontrada"):"")
								+ " [" + bien.getCoordenadasXY() + "]");
				bien.addObservacion(obs);
				}
		}
	}
	
	/**
	 * Actualiza la version
	 */
	private void updateVersion() {
		try {
			String hora = (String) inventarioClient
					.getHora(Const.ACTION_GET_HORA);
			String fechaVersion = (String) new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date())
					+ " " + hora;
			Version version = new Version();
			version.setFecha(fechaVersion);
			version.setFeaturesActivas(true);
			AppContext.getApplicationContext().getBlackboard().put(
					AppContext.VERSION, version);
			if (getInventarioFrame() != null)
				getInventarioFrame().setFecha(
						(String) new SimpleDateFormat("dd-MM-yyyy")
								.format(new Date())
								+ " " + hora);
			com.geopista.protocol.inventario.Const.fechaVersion = fechaVersion;
		} catch (Exception ex) {
			logger.error("Error al actualizar la version despues de la carga:",
					ex);
		}
	}

	/**
	 * Este metodo solo se puede utilizar para la aplicacion de Inventario para
	 * el plugin interno que se utiliza dentro del editor no funciona
	 * 
	 * @return
	 */
	private InventarioInternalFrame getInventarioFrame() {
		try {
			return ((InventarioInternalFrame) ((IMainInventario) AppContext
					.getApplicationContext().getMainFrame()).getIFrame());
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Selecciona y abre un fichero para ser exportado
	 */
	protected void abrirFichero() {
		abrirFichero(GeopistaUtil.i18n_getname(
				"inventario.loadinventario.fichero", messages));
	}

	/**
	 * Cambia el lenguaje de los textos
	 * 
	 * @param messages
	 */
	public void changeScreenLang(ResourceBundle messages) {

		try {
			setTitle(GeopistaUtil.i18n_getname(
					"inventario.loadinventario.title", messages));
			jPanelLoad.setToolTipText(GeopistaUtil.i18n_getname(
					"inventario.loadinventario.title", messages));
			jButtonCancelar.setText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonCancelar.setToolTipText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonAceptar.setText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.OK", messages));
			jButtonAceptar.setToolTipText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.OK", messages));
			jButtonObtenerSchema.setText(GeopistaUtil.i18n_getname(
					"inventario.load.esquema", messages));
			jButtonObtenerSchema.setToolTipText(GeopistaUtil.i18n_getname(
					"inventario.load.esquema", messages));
			jLabelFichero.setText(GeopistaUtil.i18n_getname(
					"inventario.loadinventario.fichero", messages)
					+ ":");
			String[] literalMunicipio = { this.municipio.getNombre() };
			jTextPaneComentario
					.setText(getStringWithParameters(messages,
							"inventario.loadinventario.texto.esquema",
							literalMunicipio)
							+ "   ");

			jTextPaneComentario.setCaretPosition(jTextPaneComentario
					.getStyledDocument().getLength());
			jTextPaneComentario.insertComponent(jButtonObtenerSchema);

		} catch (Exception ex) {
			logger.error("Falta algun recurso:", ex);
		}
	}

	/**
	 * Crea un bien de tipo ElementoPatrimonio
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanCreditoDerecho(Element elementoPatrimonio)
			throws Exception {
		CreditoDerechoBean creditoDerecho = new CreditoDerechoBean();
		actualizarDatosComunes(creditoDerecho, elementoPatrimonio);
   	    creditoDerecho.setSeguro(getDatosSeguro(elementoPatrimonio));

    	creditoDerecho.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio)); //todo está en el xml pero no en la aplicacion

		actualizarDatosCreditoDerecho(creditoDerecho, elementoPatrimonio);
		// creditoDerecho.setRegistro(getRegistro, elementoPatrimonio); todo
		// está en el xml pero no en la aplicacion
		// actualizarDatosContable(creditoDerecho,elementoPatrimonio); todo está
		// en el xml pero no en la aplicacion
		creditoDerecho.addObservaciones(getObservaciones(elementoPatrimonio));
		creditoDerecho
				.addObservaciones(getDatosAdicionales(elementoPatrimonio));
		if (creditoDerecho.isArrendamiento())
			numArrendamiento++;
		else
			numCreditoDerecho++;
		return creditoDerecho;
	}

	/**
	 * Crea un bien de tipo ElementoPatrimonio
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanDerechoReal(Element elementoPatrimonio)
			throws Exception {
		DerechoRealBean derechoReal = new DerechoRealBean();
		actualizarDatosComunes(derechoReal, elementoPatrimonio);
        derechoReal.setSeguro(getDatosSeguro(elementoPatrimonio));
	    derechoReal.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));//todo está en el xml pero no en la aplicacion

		actualizarDatosDerecho(derechoReal, elementoPatrimonio);
		derechoReal.setRegistro(getDatosRegistro(elementoPatrimonio));
		// actualizarDatosContable(derechoReal,elementoPatrimonio);todo está en
		// el xml pero no en la aplicacion
		derechoReal.addObservaciones(getObservaciones(elementoPatrimonio));
		derechoReal.addObservaciones(getDatosAdicionales(elementoPatrimonio));
		numDerechoReal++;
		return derechoReal;
	}

	/**
	 * Crea un bien de tipo InmuebleRustico
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanInmuebleRustico(Element elementoPatrimonio)
			throws Exception {
		InmuebleBean inmuebleRustico = new InmuebleBean();
		inmuebleRustico.setTipo(Const.PATRON_INMUEBLES_RUSTICOS);
		actualizarDatosComunes(inmuebleRustico, elementoPatrimonio);
		inmuebleRustico.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosInmueble(inmuebleRustico, elementoPatrimonio);
		inmuebleRustico.setRegistro(getDatosRegistro(elementoPatrimonio));
		actualizarDatosInmuebleRustico(inmuebleRustico, elementoPatrimonio);
   	 	inmuebleRustico.setSeguro(getDatosSeguro(elementoPatrimonio));

		// actualizarDatosContables(inmuebleRustico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		actualizarMejoras(inmuebleRustico, elementoPatrimonio);
		inmuebleRustico.addObservaciones(getObservaciones(elementoPatrimonio));
		actualizarRefCatastrales(inmuebleRustico, elementoPatrimonio);
		actualizarUsosFuncionales(inmuebleRustico, elementoPatrimonio);
		inmuebleRustico.addObservaciones(getDatosAdicionales(elementoPatrimonio));
		numInmuebleRustico++;
		return inmuebleRustico;
	}

	/**
	 * Crea un bien de tipo InmuebleRustico
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private boolean isInmuebleTipoVia(Element elementoPatrimonio,String nodo_busqueda,String categoria_busqueda)
			throws Exception {

		Element nodoTipoClase = XMLTranslator_LCGIII.recuperarHijo(elementoPatrimonio,
				nodo_busqueda);

		if (nodoTipoClase == null)
			return false;

		Element nodo = XMLTranslator_LCGIII.recuperarHijo(nodoTipoClase,
				categoria_busqueda);
		if (nodo == null)
			return false;

		//Element nodoTipo = XMLTranslator_LCGIII.recuperarHijo(nodo, "tipo");
		//String nombreNodo = XMLTools.getValue(nodoTipo);

		Element nodoId = XMLTranslator_LCGIII.recuperarHijo(nodo, "id");
		if (nodoId == null)
			return false;
		String id = XMLTools.getValue(nodoId);

		if (id != null) {
			DomainNode domainNode;
			if (nodo_busqueda.equals("datos_urbanos"))
				domainNode = Estructuras.getListaClaseUrbana()
						.getDomainNode(id);
			else
				domainNode = Estructuras.getListaClaseRustica()
				.getDomainNode(id);
			if (domainNode == null)
				return false;
			if ((domainNode.getFirstTerm()!=null) && (domainNode.getFirstTerm().equals("Vial")))	
				return true;			
		}
		return false;
	}

	/**
	 * Crea un bien de tipo InmuebleUrbano
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanInmuebleUrbano(Element elementoPatrimonio)
			throws Exception {
		InmuebleBean inmuebleUrbano = new InmuebleBean();
		inmuebleUrbano.setTipo(Const.PATRON_INMUEBLES_URBANOS);
		actualizarDatosComunes(inmuebleUrbano, elementoPatrimonio);
		inmuebleUrbano
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosInmueble(inmuebleUrbano, elementoPatrimonio);
		inmuebleUrbano.setRegistro(getDatosRegistro(elementoPatrimonio));
		actualizarDatosInmuebleUrbano(inmuebleUrbano, elementoPatrimonio);
		inmuebleUrbano.setSeguro(getDatosSeguro(elementoPatrimonio));
         
		// actualizarDatosContables(inmuebleRustico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		actualizarMejoras(inmuebleUrbano, elementoPatrimonio);
		inmuebleUrbano.addObservaciones(getObservaciones(elementoPatrimonio));

		actualizarRefCatastrales(inmuebleUrbano, elementoPatrimonio);
		actualizarUsosFuncionales(inmuebleUrbano, elementoPatrimonio);
		inmuebleUrbano
				.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numInmuebleUrbano++;
		return inmuebleUrbano;
	}

	/**
	 * Crea un bien de tipo HistoricoArtistico
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanHistoricoArtistico(Element elementoPatrimonio)
			throws Exception {
		MuebleBean historicoArtistico = new MuebleBean();
		historicoArtistico.setTipo(Const.PATRON_MUEBLES_HISTORICOART);
		actualizarDatosComunes(historicoArtistico, elementoPatrimonio);
		historicoArtistico
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosMueble(historicoArtistico, elementoPatrimonio);
     	 historicoArtistico.setSeguro(getDatosSeguro(elementoPatrimonio));

		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		historicoArtistico
				.addObservaciones(getObservaciones(elementoPatrimonio));
		historicoArtistico
				.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numHistoricoArtistico++;
		return historicoArtistico;
	}

	/**
	 * Crea un bien de tipo Otro
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanOtroMueble(Element elementoPatrimonio)
			throws Exception {
		MuebleBean otro = new MuebleBean();
		otro.setTipo(Const.PATRON_BIENES_MUEBLES);
		actualizarDatosComunes(otro, elementoPatrimonio);
		otro.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosMueble(otro, elementoPatrimonio);
		otro.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		otro.addObservaciones(getObservaciones(elementoPatrimonio));
		otro.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numMueble++;
		return otro;
	}

	/**
	 * Crea un bien de tipo Otro
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanSemoviente(Element elementoPatrimonio)
			throws Exception {
		SemovienteBean semoviente = new SemovienteBean();
		actualizarDatosComunes(semoviente, elementoPatrimonio);
		semoviente
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosSemoviente(semoviente, elementoPatrimonio);
		semoviente.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		semoviente.addObservaciones(getObservaciones(elementoPatrimonio));
		semoviente.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numSemoviente++;
		return semoviente;
	}

	/**
	 * Crea un bien de tipo ValorMobiliario
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanValorMobiliario(Element elementoPatrimonio)
			throws Exception {
		ValorMobiliarioBean mobiliario = new ValorMobiliarioBean();
		actualizarDatosComunes(mobiliario, elementoPatrimonio);
		mobiliario
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosValorMobiliario(mobiliario, elementoPatrimonio);
		// mobiliario.setRegistro(getDatosRegistro(elementoPatrimonio)); //TODO
		// no existe en la clase el registrp
		mobiliario.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		mobiliario.addObservaciones(getObservaciones(elementoPatrimonio));
		mobiliario.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numValorMobiliario++;
		return mobiliario;
	}

	/**
	 * Crea un bien de tipo Vehiculo
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanVehiculo(Element elementoPatrimonio)
			throws Exception {
		VehiculoBean vehiculo = new VehiculoBean();
		actualizarDatosComunes(vehiculo, elementoPatrimonio);
		vehiculo
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosVehiculo(vehiculo, elementoPatrimonio);
		vehiculo.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		vehiculo.addObservaciones(getObservaciones(elementoPatrimonio));
		vehiculo.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numVehiculo++;
		return vehiculo;
	}

	/**
	 * Crea un bien de tipo Via Rustica
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanViaRustica(Element elementoPatrimonio)
			throws Exception {
		ViaBean viaRustica = new ViaBean();
		viaRustica.setTipo(Const.PATRON_VIAS_PUBLICAS_RUSTICAS);
		actualizarDatosComunes(viaRustica, elementoPatrimonio);
		viaRustica
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosVia(viaRustica, elementoPatrimonio);
		viaRustica.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		actualizarMejoras(viaRustica, elementoPatrimonio);
		viaRustica.addObservaciones(getObservaciones(elementoPatrimonio));
		viaRustica.addObservaciones(getDatosAdicionales(elementoPatrimonio));
		actualizarRefCatastrales(viaRustica, elementoPatrimonio);
		numViaRustica++;
		return viaRustica;
	}

	/**
	 * Crea un bien de tipo Via Urbana
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienBean createBeanViaUrbana(Element elementoPatrimonio)
			throws Exception {
		ViaBean viaUrbana = new ViaBean();
		viaUrbana.setTipo(Const.PATRON_VIAS_PUBLICAS_URBANAS);
		actualizarDatosComunes(viaUrbana, elementoPatrimonio);
		viaUrbana
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		actualizarDatosVia(viaUrbana, elementoPatrimonio);
		viaUrbana.setSeguro(getDatosSeguro(elementoPatrimonio));
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		actualizarMejoras(viaUrbana, elementoPatrimonio);
		viaUrbana.addObservaciones(getObservaciones(elementoPatrimonio));
		viaUrbana.addObservaciones(getDatosAdicionales(elementoPatrimonio));

		numViaUrbana++;
		return viaUrbana;
	}

	/**
	 * Crea un bien de tipo BienRevertible
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private BienRevertible createBeanBienRevertible(Element elementoPatrimonio)
			throws Exception {
		BienRevertible bienRevertible = new BienRevertible();
		actualizarDatosBienRevertible(bienRevertible, elementoPatrimonio);
		bienRevertible
				.setCuentaAmortizacion(getDatosAmortizacion(elementoPatrimonio));
		bienRevertible.addObservaciones(getObservaciones(elementoPatrimonio));
		bienRevertible
				.addObservaciones(getDatosAdicionales(elementoPatrimonio));
		bienRevertible.setDocumentos(getListadoDocumentos(elementoPatrimonio));
		bienRevertible.setSeguro(getDatosSeguro(elementoPatrimonio));
		numBienRevertible++;
		return bienRevertible;
	}

	/**
	 * Crea un bien de tipo Lote <lotes><nombre><![CDATA[Contenedor]]></nombre>
	 * <num_lote>1</num_lote> <fecha_alta>27/07/2010</fecha_alta>
	 * <fecha_ultima_modificacion>27/07/2010</fecha_ultima_modificacion>
	 * <listado_documentos
	 * ><documento><![CDATA[3108D357_Mobiliario.pdf]]></documento
	 * ></listado_documentos> <descripcion><![CDATA[De plástico de 1100
	 * litros]]></descripcion> <bienes_lote>
	 * <id_inventario>3.2.30</id_inventario>
	 * <id_inventario>3.2.19</id_inventario>
	 * <id_inventario>3.2.31</id_inventario>
	 * <id_inventario>3.2.16</id_inventario>
	 * <id_inventario>3.2.20</id_inventario>
	 * <id_inventario>3.2.18</id_inventario>
	 * <id_inventario>3.2.24</id_inventario>
	 * <id_inventario>3.2.26</id_inventario>
	 * <id_inventario>3.2.15</id_inventario>
	 * <id_inventario>3.2.34</id_inventario>
	 * <id_inventario>3.2.21</id_inventario>
	 * <id_inventario>3.2.6</id_inventario> <id_inventario>3.2.3</id_inventario>
	 * <id_inventario>3.2.35</id_inventario>
	 * <id_inventario>3.2.2</id_inventario>
	 * <id_inventario>3.2.10</id_inventario>
	 * <id_inventario>3.2.13</id_inventario>
	 * <id_inventario>3.2.14</id_inventario>
	 * <id_inventario>3.2.11</id_inventario>
	 * <id_inventario>3.2.1</id_inventario>
	 * <id_inventario>3.2.5</id_inventario><id_inventario
	 * >3.2.33</id_inventario><
	 * id_inventario>3.2.22</id_inventario><id_inventario
	 * >3.2.40</id_inventario><
	 * id_inventario>3.2.27</id_inventario><id_inventario
	 * >3.2.8</id_inventario><id_inventario
	 * >3.2.7</id_inventario><id_inventario>3.2
	 * .12</id_inventario><id_inventario>
	 * 3.2.23</id_inventario><id_inventario>3.2
	 * .36</id_inventario><id_inventario>
	 * 3.2.29</id_inventario><id_inventario>3.2
	 * .37</id_inventario><id_inventario>
	 * 3.2.32</id_inventario><id_inventario>3.2
	 * .38</id_inventario><id_inventario>
	 * 3.2.28</id_inventario><id_inventario>3.2
	 * .17</id_inventario><id_inventario>
	 * 3.2.9</id_inventario><id_inventario>3.2.25
	 * </id_inventario><id_inventario>3.2
	 * .39</id_inventario><id_inventario>3.2.4<
	 * /id_inventario></bienes_lote></lotes>
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private Lote createBeanLote(Element elementoPatrimonio) throws Exception {
		Lote lote = new Lote();
		actualizarDatosLote(lote, elementoPatrimonio);
		lote.setDocumentos(getListadoDocumentos(elementoPatrimonio));
		lote.setSeguro(getDatosSeguro(elementoPatrimonio));
		numLote++;
		return lote;
	}

	/**
	 * Actualiza los datos comunes de todos lo BienBean
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	private void actualizarDatosComunes(BienBean bienBean, Element datos)
			throws Exception {

		bienBean.setOrganizacion(municipio.getNombre() + " - "
				+ municipio.getProvincia());
		Element nodoBien = XMLTranslator_LCGIII.recuperarHijo(datos,"bienes_inventario");
		Element nodoGeorreferenciacion = XMLTranslator_LCGIII.recuperarHijo(datos,"georreferenciacion");

		//Actualizamos la informacion geografica del bien
		actualizarListaFeatures(bienBean, nodoBien,nodoGeorreferenciacion);
		
		bienBean.setNumInventario(getString(nodoBien, "numinventario"));
		bienBean.setFechaAlta(getFecha(nodoBien, "fecha_alta"));
		bienBean.setFechaAprobacionPleno(getFecha(nodoBien,"fecha_aprobacion_pleno"));
		bienBean.setFechaBaja(getFecha(nodoBien, "fecha_baja"));
		bienBean.setFechaUltimaModificacion(getFecha(nodoBien,"fecha_ultima_modificacion"));
		bienBean.setNombre(getString(nodoBien, "nombre"));
		bienBean.setDescripcion(getString(nodoBien, "descripcion"));
		bienBean.setUso(getId(nodoBien, "cat_uso_jur", Estructuras.getListaUsoJuridico(), bienBean.getNumInventario()));
		bienBean.setPatrimonioMunicipalSuelo(getString(nodoBien,"patrimonio_municipal_suelo"));
		bienBean.setFechaAdquisicion(getFecha(nodoBien, "fecha_adquisicion"));
		bienBean.setAdquisicion(getId(nodoBien, "cat_adquisicion", Estructuras.getListaFormaAdquisicion(), bienBean.getNumInventario()));
		bienBean.setRevisionActual(getLong(nodoBien, "revision_actual"));
		bienBean.setRevisionExpirada(getLong(nodoBien, "revision_expirada"));
		bienBean.setFrutos(getString(nodoBien, "frutos"));
		bienBean.setImporteFrutos(getDouble(nodoBien, "importe_frutos"));
		bienBean.setDocumentos(getListadoDocumentos(nodoBien));
		actualizarObservacion(bienBean, nodoBien, "ultimo_inventario","inventario.loadinventario.ultimo_inventario");
		actualizarDatosDiagnosis(bienBean, nodoBien);
		actualizarObservacion(bienBean, nodoBien, "modificado","inventario.loadinventario.modificado");

		return;

	}

	/**
	 * Actualiza una observación con algún campo de los que se encuentra en la
	 * url pero no en BD
	 */
	private void actualizarDatosDiagnosis(BienBean bienBean, Element element) {
		// Recogemos el ultimo inventario
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, "datos_diagnosis");
		actualizarObservacion(bienBean, nodo, "descripcion_diagnosis",
				"inventario.loadinventario.descripcion_diagnosis");
		String id = getId(nodo, "cat_diagnosis", Estructuras
				.getListaDiagnosis(), bienBean.getNumInventario());
		if (id != null) {
			DomainNode nodoAux = Estructuras.getListaDiagnosis().getDomainNode(
					id);
			if (nodoAux != null)
				bienBean.addObservacion(new Observacion(GeopistaUtil
						.i18n_getname("inventario.loadinventario.diagnosis")
						+ ": " + nodoAux.getTerm(Constantes.Locale)));
		}
	}

	/**
	 * Actualiza una observación con algún campo de los que se encuentra en la
	 * url pero no en BD
	 */
	private void actualizarObservacion(BienBean bienBean, Element nodoBien,
			String campo, String etiqueta) {
		// Recogemos el ultimo inventario
		String valorCampo = getString(nodoBien, campo);
		if (valorCampo != null && valorCampo.length() > 0)
			bienBean.addObservacion(new Observacion(GeopistaUtil
					.i18n_getname(etiqueta)
					+ ": " + valorCampo));
	}

	/**
	 * Actualiza los datos de amortizacion
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	private CuentaAmortizacion getDatosAmortizacion(Element datos)
			throws Exception {

		Element nodoAmortizacion = XMLTranslator_LCGIII.recuperarHijo(datos,
				"amortizacion");

		if (nodoAmortizacion == null)
			return null;
		CuentaAmortizacion cuentaAmortizacion = new CuentaAmortizacion();
		cuentaAmortizacion.setCuenta(getString(nodoAmortizacion, "cuenta"));
		cuentaAmortizacion.setDescripcion(getString(nodoAmortizacion,
				"descripcion"));
		cuentaAmortizacion.setAnnos(getInt(nodoAmortizacion, "anios"));
		cuentaAmortizacion.setPorcentaje(getDouble(nodoAmortizacion,
				"porcentaje"));
		cuentaAmortizacion.setTotalAmortizado(getDouble(nodoAmortizacion,
				"total_amortizado"));
		cuentaAmortizacion.setTipoAmortizacion(getId(nodoAmortizacion,
				"cat_amortizacion", Estructuras.getListaTipoAmortizacion(),
				null));
		return cuentaAmortizacion;

	}

	/**
	 * Método que actualiza los datos generales de un bien credito y derecho
	 * 
	 * @param credito
	 *            a actualizar
	 * @param datos
	 *            de actualizacion
	 */
	private void actualizarDatosCreditoDerecho(CreditoDerechoBean credito,
			Element datos) throws Exception {
		Element nodoCredito = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_credito");
		if (nodoCredito == null)
			return;
		credito.setConcepto(getId(nodoCredito, "cat_cred_der", Estructuras
				.getListaConceptosCreditosDerechos(), credito
				.getNumInventario()));
		credito.setDeudor(getString(nodoCredito, "deudor"));
		credito.setDestino(getString(nodoCredito, "destino"));
		credito.setImporte(getDouble(nodoCredito, "importe"));
		credito
				.setArrendamiento((getString(nodoCredito, "arrendamiento") != null && getString(
						nodoCredito, "arrendamiento").equalsIgnoreCase("SI")));
		credito.setFechaVencimiento(getFecha(nodoCredito, "fecha_vencimiento"));
		credito.setConceptoDesc(getString(nodoCredito, "concepto_desc"));
		credito.setCaracteristicas(getString(nodoCredito, "descripcion"));

		actualizarObservacion(credito, nodoCredito, "fecha_inicio",
				"inventario.loadinventario.fecha_inicio");
		actualizarObservacion(credito, nodoCredito, "caracteristicas",
				"inventario.loadinventario.caracteristicas");
		actualizarObservacion(credito, nodoCredito, "amortizacion_anual",
				"inventario.loadinventario.amortizacion_anual");
		actualizarObservacion(credito, nodoCredito, "acreedor",
				"inventario.loadinventario.acreedor");

		// Se han añadido los campos:
		credito.setClase(getId(nodoCredito, "cat_clase_cred", Estructuras.getListaClaseCredito(), credito.getNumInventario()));
		credito.setSubClase(getId(nodoCredito, "cat_subclase", Estructuras.getListaSubclaseCredito(), credito.getNumInventario()));

		// ASO 15-dic-2010 eliminado según bug 0002533
		// actualizarObservacion(credito,nodoCredito, "arrendamiento",
		// "inventario.loadinventario.cat_arrendamiento");

	}

	/**
	 * Rellena los datos de los derechos reales
	 * 
	 * @param derechoReal
	 * @param elementoPatrimonio
	 */
	private void actualizarDatosDerecho(DerechoRealBean derechoReal,
			Element datos) throws Exception {
		/*
		 * la cat_clase_der es un tipo para el catalogo, que lo contempla el
		 * serpa [11:42:05] <Francisco Javier Castro> y que en localgis no
		 * existe (se ha añadido en la base de datos
		 */

		Element nodoDerecho = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_derecho");
		if (nodoDerecho == null)
			return;
		derechoReal.setDestino(getString(nodoDerecho, "destino"));
		derechoReal.setBien(getString(nodoDerecho, "bien"));
		derechoReal.setCosteAdquisicion(getDouble(nodoDerecho, "coste"));
		derechoReal.setValorActual(getDouble(nodoDerecho, "valor"));
		// cat_clase_der se ha añadido en la base de datos
		derechoReal.setClase(getId(nodoDerecho, "cat_clase_der", Estructuras.getListaClaseDerechosReales(), derechoReal.getNumInventario()));
	}

	/**
	 * Actualiza los datos de inmueble
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	private void actualizarDatosInmueble(InmuebleBean inmueble, Element datos)
			throws Exception {

		/**
		 * private Date fechaAdquisicionSuelo;
		 */
		Element nodoInmueble = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_inmuebles");

		if (nodoInmueble == null)
			return;
		inmueble.setDireccion(getString(nodoInmueble, "direccion"));
		inmueble.setLinderoNorte(getString(nodoInmueble, "lindero_norte"));
		inmueble.setLinderoSur(getString(nodoInmueble, "lindero_sur"));
		inmueble.setLinderoEste(getString(nodoInmueble, "lindero_este"));
		inmueble.setLinderoOeste(getString(nodoInmueble, "lindero_oeste"));
		inmueble.setCalificacion(getString(nodoInmueble, "calificacion"));
		inmueble.setSuperficieRegistralSuelo(getDouble(nodoInmueble,
				"superficie_registral_suelo"));
		inmueble.setSuperficieCatastralSuelo(getDouble(nodoInmueble,
				"superficie_catastral_suelo"));
		inmueble.setSuperficieRealSuelo(getDouble(nodoInmueble,
				"superficie_real_suelo"));
		inmueble.setSuperficieRegistralConstruccion(getDouble(nodoInmueble,
				"superficie_registral_construccion"));
		inmueble.setSuperficieCatastralConstruccion(getDouble(nodoInmueble,
				"superficie_catastral_construccion"));
		inmueble.setSuperficieRealConstruccion(getDouble(nodoInmueble,
				"superficie_real_construccion"));
		inmueble.setDerechosRealesFavor(getString(nodoInmueble,
				"derechosrealesfavor"));
		inmueble.setValorDerechosFavor(getDouble(nodoInmueble,
				"valor_derechos_favor"));
		inmueble.setDerechosRealesContra(getString(nodoInmueble,
				"derechosrealescontra"));
		inmueble.setValorDerechosContra(getDouble(nodoInmueble,
				"valor_derechos_contra"));
		inmueble.setDerechosPersonales(getString(nodoInmueble,
				"derechospersonales"));
		inmueble.setFechaObra(getFecha(nodoInmueble, "fechaobra"));
		inmueble.setDestino(getString(nodoInmueble, "destino"));
		inmueble.setPropiedad(getId(nodoInmueble, "cat_propiedad", Estructuras
				.getListaPropiedadPatrimonial(), inmueble.getNumInventario()));
		inmueble.setRefCatastral(getString(nodoInmueble, "ref_catastral"));
		/*
		 * if (inmueble.getRefCatastral()!=null &&
		 * inmueble.getRefCatastral().length()>14){
		 * logger.debug("Referencia catastral demasiado grande transformada a:"
		 * +inmueble.getRefCatastral());
		 * inmueble.setRefCatastral(inmueble.getRefCatastral().substring(0,
		 * 14)); }
		 */
		inmueble.setNumPlantas(getString(nodoInmueble, "num_plantas"));
		inmueble.setEstadoConservacion(getId(nodoInmueble, "cat_est_cons",
				Estructuras.getListaEstadoConservacion(), inmueble
						.getNumInventario()));
		inmueble.setTipoConstruccion(getId(nodoInmueble, "cat_construccion",
				Estructuras.getListaTipoConstruccion(), inmueble
						.getNumInventario()));
		inmueble.setCubierta(getId(nodoInmueble, "cat_cubierta", Estructuras
				.getListaTipoCubierta(), inmueble.getNumInventario()));
		inmueble.setCarpinteria(getId(nodoInmueble, "cat_carpinteria",
				Estructuras.getListaTipoCarpinteria(), inmueble
						.getNumInventario()));
		inmueble.setFachada(getId(nodoInmueble, "cat_fachada", Estructuras
				.getListaTipoFachada(), inmueble.getNumInventario()));
		inmueble.setEdificabilidad(getDouble(nodoInmueble, "edificabilidad"));
		inmueble.setFachadaDesc(getString(nodoInmueble, "fachada_desc"));
		inmueble.setCubiertaDesc(getString(nodoInmueble, "cubierta_desc"));
		inmueble
				.setCarpinteriaDesc(getString(nodoInmueble, "carpinteria_desc"));
		inmueble.setTipoConstruccionDesc(getString(nodoInmueble,
				"tipoconstruccion_desc"));
		inmueble.setEstadoConservacionDesc(getString(nodoInmueble,
				"estadoconservacion_desc"));
		inmueble.setSuperficieOcupadaConstruccion(getDouble(nodoInmueble,
				"superficie_ocupada_construccion"));
		inmueble.setSuperficieConstruidaConstruccion(getDouble(nodoInmueble,
				"superficie_construida_construccion"));
		inmueble.setSuperficieEnPlantaConstruccion(getDouble(nodoInmueble,
				"superficie_enplanta_construccion"));
		inmueble.setValorAdquisicionSuelo(getDouble(nodoInmueble,
				"valor_adquisicion_suelo"));
		inmueble.setValorCatastralSuelo(getDouble(nodoInmueble,
				"valor_catastral_suelo"));
		inmueble.setValorActualSuelo(getDouble(nodoInmueble,
				"valor_actual_suelo"));
		inmueble.setValorAdquisicionConstruccion(getDouble(nodoInmueble,
				"valor_adquisicion_construccion"));
		inmueble.setValorCatastralConstruccion(getDouble(nodoInmueble,
				"valor_catastral_construccion"));
		inmueble.setValorActualConstruccion(getDouble(nodoInmueble,
				"valor_actual_construccion"));
		inmueble.setValorAdquisicionInmueble(getDouble(nodoInmueble,
				"valor_adquisicion_inmueble"));
		inmueble.setValorActualInmueble(getDouble(nodoInmueble,
				"valor_actual_inmueble"));
		inmueble.setNumeroOrden(getString(nodoInmueble, "numero_orden"));
		inmueble
				.setNumeroPropiedad(getString(nodoInmueble, "numero_propiedad"));
		actualizarObservacion(inmueble, nodoInmueble, "anio_valor_catastral",
				"inventario.loadinventario.anio_valor_catastral");
		actualizarObservacion(inmueble, nodoInmueble,
				"edificabilidad_descripcion",
				"inventario.loadinventario.edificabilidad_descripcion");
		inmueble.setRegistroDesc(getString(nodoInmueble,"registro"));
        inmueble.setCalificacion(getString(nodoInmueble,"calificacion"));
        inmueble.setValorCatastralInmueble(getDouble(nodoInmueble,"valor_catastral_inmueble"));
        inmueble.setAnioValorCatastral(getInt(nodoInmueble,"anio_valor_catastral"));
        inmueble.setEdificabilidadDesc(getString(nodoInmueble,"edificabilidad_descripcion"));
        inmueble.setFechaAdquisicionObra(getFecha(nodoInmueble,"fecha_adquisicion_obra"));
		if (Const.PATRON_INMUEBLES_URBANOS.equals(inmueble.getTipo()))
			inmueble.setClase(getId(nodoInmueble, "cat_clase_urb", Estructuras.getListaClaseUrbana(), inmueble.getNumInventario()));
		else
			if (Const.PATRON_INMUEBLES_RUSTICOS.equals(inmueble.getTipo()))
				inmueble.setClase(getId(nodoInmueble, "cat_clase_rust", Estructuras.getListaClaseRustica(), inmueble.getNumInventario()));
	}

	/**
	 * Actualiza los datos de Inmueble rustico
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosInmuebleRustico(InmuebleBean inmueble,
			Element datos) throws Exception {

		Element nodoRustico = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_rusticos");

		if (nodoRustico == null)
			return;
		InmuebleRusticoBean rustico = new InmuebleRusticoBean();
		rustico.setPoligono(getString(nodoRustico, "poligono"));
		rustico.setSubparcela(getString(nodoRustico, "subparcela"));
		rustico.setParaje(getString(nodoRustico, "paraje"));
		rustico.setParcela(getString(nodoRustico, "parcela"));
		rustico.setAprovechamiento(getId(nodoRustico, "cat_aprovechamiento",
				Estructuras.getListaAprovechamiento(), inmueble
						.getNumInventario()));
		inmueble.setInmuebleRustico(rustico);

		actualizarObservacion(inmueble, nodoRustico, "cultivo",
				"inventario.loadinventario.cultivo");
		actualizarObservacion(inmueble, nodoRustico, "zona",
				"inventario.loadinventario.zona");

	}

	/**
	 * Actualiza los datos de Inmueble urbano
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosInmuebleUrbano(InmuebleBean inmueble,
			Element datos) throws Exception {
		Element nodoUrbano = XMLTranslator_LCGIII
				.recuperarHijo(datos, "datos_urbanos");
		if (nodoUrbano == null){
			InmuebleUrbanoBean urbano = new InmuebleUrbanoBean();
			inmueble.setInmuebleUrbano(urbano);

			return;
		}
		InmuebleUrbanoBean urbano = new InmuebleUrbanoBean();
		urbano.setParcela(getString(nodoUrbano, "parcela"));
		urbano.setManzana(getString(nodoUrbano, "manzana"));
		inmueble.setInmuebleUrbano(urbano);

		actualizarObservacion(inmueble, nodoUrbano, "uso",
				"inventario.loadinventario.uso");
	}

	/**
	 * Actualiza los datos de EIEL
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private Seguro getDatosSeguro( Element datos) throws Exception {
    	
    	Element nodoSeguro=XMLTranslator_LCGIII.recuperarHijo(datos, "datos_seguros");
	    if (nodoSeguro==null) return null;
        
	    Seguro seguro= new Seguro();
	    CompanniaSeguros compania= new CompanniaSeguros();
	    compania.setNombre(getString(nodoSeguro,"nombre"));
	    compania.setDescripcion(getString(nodoSeguro,"descripcion"));
	    seguro.setCompannia(compania);
	    
	    seguro.setPrima(getDouble(nodoSeguro,"prima"));
	    seguro.setPoliza(getLong(nodoSeguro,"poliza"));
	    
	    seguro.setDescripcion(getString(nodoSeguro,"descripcion_seguro"));
	    seguro.setFechaInicio(getFecha(nodoSeguro,"fecha_inicio"));
	    seguro.setFechaVencimiento(getFecha(nodoSeguro,"fecha_vencimiento"));
	    return seguro;
    }
	
	/**
	 * Actualiza los datos de Seguro
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private InventarioEIELBean getDatosEIEL( Element datos) throws Exception {
    	
    	Element nodoEIEL=XMLTranslator_LCGIII.recuperarHijo(datos, "datos_eiel");
	    if (nodoEIEL==null) return null;
        
	    InventarioEIELBean eiel= new InventarioEIELBean();
	    
	    eiel.setNombreEIEL(getString(nodoEIEL,"nombreEIEL"));
	    eiel.setEstadoEIEL(getString(nodoEIEL,"estadoEIEL"));
	    eiel.setTipoEIEL(getString(nodoEIEL,"tipoEIEL"));
	    eiel.setGestionEIEL(getString(nodoEIEL,"gestionEIEL"));
	    
	    return eiel;
    }

	/**
	 * Actualiza los datos de Semoviente
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosSemoviente(SemovienteBean semoviente,
			Element datos) throws Exception {
		// String identificacion;
		Element nodoSemoviente = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_semovientes");
		if (nodoSemoviente == null)
			return;
		semoviente.setPropiedad(getId(nodoSemoviente, "cat_propiedad",
				Estructuras.getListaPropiedadPatrimonial(), semoviente
						.getNumInventario()));
		// semoviente.setRaza(getString(nodoSemoviente, "raza"));
		semoviente.setRaza(getId(nodoSemoviente, "cat_clase", Estructuras
				.getListaRazaSemoviente(), semoviente.getNumInventario()));
		semoviente.setConservacion(getId(nodoSemoviente, "cat_est_cons",
				Estructuras.getListaEstadoConservacion(), semoviente
						.getNumInventario()));
		semoviente.setEspecie(getString(nodoSemoviente, "especie"));
		semoviente.setDestino(getString(nodoSemoviente, "destino"));
		semoviente.setCantidad(getInt(nodoSemoviente, "cantidad"));
		semoviente.setCosteAdquisicion(getDouble(nodoSemoviente,
				"coste_adquisicion"));
		semoviente.setValorActual(getDouble(nodoSemoviente, "valor_actual"));
		semoviente.setFechaNacimiento(getFecha(nodoSemoviente,
				"fecha_nacimiento"));
		semoviente.setCaracteristicas(getString(nodoSemoviente, "descripcion"));

		actualizarObservacion(semoviente, nodoSemoviente, "raza",
				"inventario.loadinventario.raza");
		// cat_crec_desarrollo se encuentra en el xml pero no en la base de
		// datos
		String id = getId(nodoSemoviente, "cat_crec_desarrollo", Estructuras
				.getListaCrecDesarrollo(), semoviente.getNumInventario());
		if (id != null) {
			DomainNode nodo = Estructuras.getListaCrecDesarrollo()
					.getDomainNode(id);
			if (nodo != null)
				semoviente
						.addObservacion(new Observacion(
								GeopistaUtil
										.i18n_getname("inventario.loadinventario.cat_crec_desarrollo")
										+ ": "
										+ nodo.getTerm(Constantes.Locale)));
		}

	}

	/**
	 * Actualiza los datos de Valor Mobiliario
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosValorMobiliario(ValorMobiliarioBean mobiliario,
			Element datos) throws Exception {
		Element nodoMobiliario = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_mobiliario");
		if (nodoMobiliario == null)
			return;
		mobiliario.setDepositadoEn(getString(nodoMobiliario, "depositado_en"));
		mobiliario.setEmitidoPor(getString(nodoMobiliario, "emitido_por"));
		mobiliario.setNumero(getString(nodoMobiliario, "numero"));
		mobiliario.setSerie(getString(nodoMobiliario, "serie"));
		mobiliario.setNumTitulos(getInt(nodoMobiliario, "num_titulos"));
		mobiliario.setDestino(getString(nodoMobiliario, "destino"));
		mobiliario.setClase(getId(nodoMobiliario, "cat_mobiliario", Estructuras
				.getListaClasesValorMobiliario(), mobiliario.getNumInventario()));
		mobiliario.setCosteAdquisicion(getDouble(nodoMobiliario,
				"coste_adquisicion"));
		mobiliario.setValorActual(getDouble(nodoMobiliario, "valor_actual"));
		mobiliario.setPrecio(getDouble(nodoMobiliario, "precio"));
		mobiliario.setCapital(getDouble(nodoMobiliario, "capital"));
		mobiliario.setFechaAcuerdo(getFecha(nodoMobiliario, "fecha_acuerdo"));
	}

	/**
	 * Actualiza los datos de Vehiculo
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosVehiculo(VehiculoBean vehiculo, Element datos)
			throws Exception {

		Element nodoVehiculo = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_vehiculo");
		if (nodoVehiculo == null)
			return;
		vehiculo.setMatriculaVieja(getString(nodoVehiculo, "matricula_vieja"));
		vehiculo.setMatriculaNueva(getString(nodoVehiculo, "matricula_nueva"));
		vehiculo.setNumBastidor(getString(nodoVehiculo, "num_bastidor"));
		vehiculo.setMarca(getString(nodoVehiculo, "marca"));
		// vehiculo.setModelo(getString(nodoVehiculo, "modelo"));
		vehiculo.setMotor(getString(nodoVehiculo, "motor"));
		vehiculo.setServicio(getString(nodoVehiculo, "servicio"));
		vehiculo.setDestino(getString(nodoVehiculo, "destino"));

		vehiculo.setTipoVehiculo(getId(nodoVehiculo, "cat_vehiculo",
				Estructuras.getListaTiposVehiculo(), vehiculo
						.getNumInventario()));

		vehiculo.setEstadoConservacion(getId(nodoVehiculo, "cat_est_cons",
				Estructuras.getListaEstadoConservacion(), vehiculo
						.getNumInventario()));
		vehiculo.setTraccion(getId(nodoVehiculo, "cat_traccion", Estructuras
				.getListaTraccion(), vehiculo.getNumInventario()));
		vehiculo.setPropiedad(getId(nodoVehiculo, "cat_propiedad", Estructuras
				.getListaPropiedadPatrimonial(), vehiculo.getNumInventario()));
		vehiculo.setCosteAdquisicion(getDouble(nodoVehiculo,
				"coste_adquisicion"));
		vehiculo.setValorActual(getDouble(nodoVehiculo, "valor_actual"));
		actualizarObservacion(vehiculo, nodoVehiculo, "modelo",
				"inventario.loadinventario.modelo");
	}

	/**
	 * Actualiza los datos de Via
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void actualizarDatosVia(ViaBean via, Element datos)
			throws Exception {
		Element nodoVia = XMLTranslator_LCGIII.recuperarHijo(datos, "datos_via");
		if (nodoVia == null)
			return;
		via.setCategoria(getString(nodoVia, "categoria"));
		via.setCodigo(getString(nodoVia, "codigo"));
		via.setNombreVia(getString(nodoVia, "nombre"));
		via.setInicioVia(getString(nodoVia, "inicio"));
		via.setFinVia(getString(nodoVia, "fin"));
		via.setDestino(getString(nodoVia, "destino"));
		via.setNumApliques(getLong(nodoVia, "num_apliques"));
		via.setNumBancos(getLong(nodoVia, "num_bancos"));
		via.setNumPapeleras(getLong(nodoVia, "num_papeleras"));
		via.setMetrosPavimentados(getDouble(nodoVia, "metros_pavimentados"));
		via
				.setMetrosNoPavimentados(getDouble(nodoVia,
						"metros_no_pavimentados"));
		via.setZonasVerdes(getDouble(nodoVia, "zonas_verdes"));
		via.setLongitud(getDouble(nodoVia, "longitud"));
		via.setAncho(getDouble(nodoVia, "ancho"));
		via.setValorActual(getDouble(nodoVia, "valor_actual"));
		if (Const.PATRON_VIAS_PUBLICAS_URBANAS.equals(via.getTipo()))
			via.setClase(getId(nodoVia, "cat_clase_urb", Estructuras.getListaClaseUrbana(), via.getNumInventario()));
		else
			if (Const.PATRON_VIAS_PUBLICAS_RUSTICAS.equals(via.getTipo()))
				via.setClase(getId(nodoVia, "cat_clase_rust", Estructuras.getListaClaseRustica(), via.getNumInventario()));
	}

	/**
	 * Actualiza los datos del bien revertible
	 * 
	 * @param bienRevertible
	 * @param datos
	 * @return
	 * 
	 */

	private void actualizarDatosBienRevertible(BienRevertible bien,
			Element datos) throws Exception {
		Element nodoBien = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_bienes_revertibles");
		if (nodoBien == null)
			return;

		bien.setNumInventario(getString(nodoBien, "numinventario"));
		// cat_clase se encuentra en el xml pero no en la pantalla
		bien.setClase(getId(nodoBien, "cat_clase", Estructuras
				.getListaClaseBienRevertible(), bien.getNumInventario()));
		if (bien.getClase() != null) {
			DomainNode nodo = Estructuras.getListaClaseBienRevertible()
					.getDomainNode(bien.getClase());
			if (nodo != null)
				bien.addObservacion(new Observacion(GeopistaUtil
						.i18n_getname("inventario.loadinventario.cat_clase")
						+ ": " + nodo.getTerm(Constantes.Locale)));
		}
		bien.setDescripcion_bien(getString(nodoBien, "descripcion_bien"));
		bien.setFechaTransmision(getFecha(nodoBien, "fecha_transmision"));
		bien.setFechaInicio(getFecha(nodoBien, "fecha_inicio"));
		bien.setFechaVencimiento(getFecha(nodoBien, "fecha_vencimiento"));
		bien.setPoseedor(getString(nodoBien, "poseedor"));
		bien.setTituloPosesion(getString(nodoBien, "titulo_posesion"));
		bien.setCondicionesReversion(getString(nodoBien,
				"condiciones_reversion"));
		bien.setCatTransmision(getId(nodoBien, "cat_transmision", Estructuras
				.getListaTransmision(), bien.getNumInventario()));
		bien.setImporte(getDouble(nodoBien, "importe"));
		bien.setFechaAlta(getFecha(nodoBien, "fecha_alta"));
		bien.setFechaBaja(getFecha(nodoBien, "fecha_baja"));
		bien.setFechaUltimaModificacion(getFecha(nodoBien,
				"fecha_ultima_modificacion"));
		bien.setDetalles(getString(nodoBien, "descripcion"));
		bien.setNombre(getString(nodoBien, "nombre"));
		bien.setOrganizacion(getString(nodoBien, "organizacion"));
		bien.setPatrimonioMunicipalSuelo(getString(nodoBien,
				"patrimonio_municipal_suelo"));
		bien.setFecha_adquisicion(getFecha(nodoBien, "fecha_adquisicion"));
		bien.setFecha_aprobacion_pleno(getFecha(nodoBien,
				"fecha_aprobacion_pleno"));
		bien.setAdquisicion(getId(nodoBien, "cat_adquisicion", Estructuras
				.getListaFormaAdquisicion(), bien.getNumInventario()));
		String cadena = getString(nodoBien, "ultimo_inventaro");
		if (cadena != null)
			bien
					.addObservacion(new Observacion(
							GeopistaUtil
									.i18n_getname("inventario.loadinventario.ultimo_inventario")
									+ ": " + cadena));
		bien.setRevisionActual(getLong(nodoBien, "revision_actual"));

		// lista de bienes
	    bien.setBienes(getBienes(nodoBien,"listado_bienes","id_bien_inventario", bien.getNumInventario(),"bien revertible"));


		// Actualizamos los datos de diganosis
		Element nodoHijo = XMLTranslator_LCGIII.recuperarHijo(nodoBien,
				"datos_diagnosis");
		cadena = getString(nodoHijo, "descripcion_diagnosis");
		if (cadena != null)
			bien
					.addObservacion(new Observacion(
							GeopistaUtil
									.i18n_getname("inventario.loadinventario.descripcion_diagnosis")
									+ ": " + cadena));
		String id = getId(nodoHijo, "cat_diagnosis", Estructuras
				.getListaDiagnosis(), bien.getNumInventario());
		bien.setDiagnosis(id);
		if (id != null) {
			DomainNode nodoAux = Estructuras.getListaDiagnosis().getDomainNode(
					id);
			if (nodoAux != null)
				bien.addObservacion(new Observacion(GeopistaUtil
						.i18n_getname("inventario.loadinventario.diagnosis")
						+ ": " + nodoAux.getTerm(Constantes.Locale)));
		}

	}

	 /**
     * Devuelve la lista de bienes asociados a un lote o a un bien revertible
     * @param datos Nodo donde se encuentran los datos
     * @param nombreListado Nombre del listado
     * @param nombreIdBien Nombre del campo del xml donde se encuentra el id del bien
     * @param numInventario Identificador del nodo padre. (Solo se usa para comentarios)
     * @param tipo Literal con el nombre del tipo al que pertenece el padre "lote" o "bienes revertibles". (Solo se usa para comentarios)
     * @return
     */
    private Collection<BienBean> getBienes(Element datos, String nombreListado, String nombreIdBien,String numInventario, String tipo){
    	Vector<BienBean> listaBienes= new Vector();
    	try{
    		Element nodoBienes = XMLTranslator_LCGIII.recuperarHijo(datos,nombreListado);
    		if (nodoBienes==null) {
    			mensajeResultado+="\n**** WARNING: El "+tipo+" :"+numInventario+" no tiene lista de bienes";
    			return null;
    		}
    		Vector<String> listaNumInventarioBienes = XMLTranslator_LCGIII.recuperarHojasAsVector(nodoBienes, nombreIdBien);
    		if (listaNumInventarioBienes==null || listaNumInventarioBienes.isEmpty()){
    			mensajeResultado+="\n**** WARNING: El "+tipo+" :"+numInventario+" no tiene lista de bienes";
            	return null;
    		}
    		for (Enumeration<String> e=listaNumInventarioBienes.elements();e.hasMoreElements();){
    			String numInventarioBien=(String)e.nextElement();
    			BienBean bien=inventarioClient.getBienByNumInventario(numInventarioBien);
    			if (bien!=null)
    				listaBienes.add(bien);
    			else{
    				mensajeResultado+="\n**** WARNING: No existe el bien padre:"+numInventarioBien+", para el "+tipo+":"+numInventario;
    				bien=new BienBean();
    				bien.setNumInventario(numInventarioBien);
    				listaBienes.add(bien);
    			}

    		}
    	}catch (Exception ex){}
    	return listaBienes;
    	
    }

	/**
	 * Actualiza los datos del bien revertible
	 * <lotes><nombre><![CDATA[Contenedor]]></nombre> <num_lote>1</num_lote>
	 * <fecha_alta>27/07/2010</fecha_alta>
	 * <fecha_ultima_modificacion>27/07/2010</fecha_ultima_modificacion>
	 * <listado_documentos
	 * ><documento><![CDATA[3108D357_Mobiliario.pdf]]></documento
	 * ></listado_documentos><descripcion><![CDATA[De plástico de 1100
	 * litros]]></descripcion>
	 * <bienes_lote><id_inventario>3.2.30</id_inventario>
	 * <id_inventario>3.2.19</id_inventario
	 * ><id_inventario>3.2.31</id_inventario>
	 * <id_inventario>3.2.16</id_inventario
	 * ><id_inventario>3.2.20</id_inventario>
	 * <id_inventario>3.2.18</id_inventario
	 * ><id_inventario>3.2.24</id_inventario>
	 * <id_inventario>3.2.26</id_inventario
	 * ><id_inventario>3.2.15</id_inventario>
	 * <id_inventario>3.2.34</id_inventario
	 * ><id_inventario>3.2.21</id_inventario>
	 * <id_inventario>3.2.6</id_inventario>
	 * <id_inventario>3.2.3</id_inventario><id_inventario
	 * >3.2.35</id_inventario><
	 * id_inventario>3.2.2</id_inventario><id_inventario>
	 * 3.2.10</id_inventario><id_inventario
	 * >3.2.13</id_inventario><id_inventario>
	 * 3.2.14</id_inventario><id_inventario
	 * >3.2.11</id_inventario><id_inventario>
	 * 3.2.1</id_inventario><id_inventario>
	 * 3.2.5</id_inventario><id_inventario>3.2
	 * .33</id_inventario><id_inventario>3.2
	 * .22</id_inventario><id_inventario>3.2
	 * .40</id_inventario><id_inventario>3.2
	 * .27</id_inventario><id_inventario>3.2
	 * .8</id_inventario><id_inventario>3.2.7
	 * </id_inventario><id_inventario>3.2.12
	 * </id_inventario><id_inventario>3.2.23
	 * </id_inventario><id_inventario>3.2.36
	 * </id_inventario><id_inventario>3.2.29
	 * </id_inventario><id_inventario>3.2.37
	 * </id_inventario><id_inventario>3.2.32
	 * </id_inventario><id_inventario>3.2.38
	 * </id_inventario><id_inventario>3.2.28
	 * </id_inventario><id_inventario>3.2.17
	 * </id_inventario><id_inventario>3.2.9<
	 * /id_inventario><id_inventario>3.2.25<
	 * /id_inventario><id_inventario>3.2.39<
	 * /id_inventario><id_inventario>3.2.4</id_inventario></bienes_lote></lotes>
	 * 
	 * @param bienRevertible
	 * @param datos
	 * @return
	 */

	private void actualizarDatosLote(Lote lote, Element nodoLote)
			throws Exception {
		lote.setNombre_lote(getString(nodoLote, "nombre"));
		// lote.setNumLote(getString(nodoLote,"num_lote"));
		lote.setFecha_alta(getFecha(nodoLote, "fecha_alta"));
		lote.setFecha_ultima_modificacion(getFecha(nodoLote,
				"fecha_ultima_modificacion"));
		lote.setDestino(getString(nodoLote, "destino"));
		lote.setDescripcion(getString(nodoLote, "descripcion"));
        lote.setBienes(getBienes(nodoLote,"bienes_lote","id_inventario",lote.getNombre_lote(),"lote"));


	}

	/***
	 * getDatosRegisto
	 * 
	 * @param datos
	 *            del elemeneto
	 * @return
	 */
	private RegistroBean getDatosRegistro(Element datos) {

		Element nodoRegistro = XMLTranslator_LCGIII.recuperarHijo(datos,
				"datos_registro");
		if (nodoRegistro == null)
			return null;

		RegistroBean registro = new RegistroBean();
		registro.setNotario(getString(nodoRegistro, "registro_notario"));
		registro.setProtocolo(getString(nodoRegistro, "registro_protocolo"));
		registro.setPropiedad(getString(nodoRegistro, "registro_propiedad"));
		registro.setTomo(getString(nodoRegistro, "registro_tomo"));
		registro.setLibro(getString(nodoRegistro, "registro_libro"));
		registro.setFolio(getString(nodoRegistro, "registro_folio"));
		registro.setFinca(getString(nodoRegistro, "registro_finca"));
		registro
				.setInscripcion(getString(nodoRegistro, "registro_inscripcion"));
		return registro;
	}

	/**
	 * Actualiza los datos de la cuenta contable
	 * 
	 * @param credito
	 *            a actualizar
	 * @param datos
	 *            de actualizacion
	 */
	private void actualizarDatosCuentaContable(BienBean bien, Element datos) {
		CuentaContable cuentaContable = new CuentaContable();
		cuentaContable.setCuenta("");
		cuentaContable.setDescripcion("");
		bien.setCuentaContable(cuentaContable);
	}

	/**
	 * Actualiza las mejoras de un bien
	 * 
	 * @param bien
	 * @param datos
	 */
	private void actualizarMejoras(BienBean bien, Element datos) {
		Vector<Mejora> newMejoras = new Vector<Mejora>();

		Vector<Element> mejoras = XMLTranslator_LCGIII.recuperarHijosAsVector(datos,
				"datos_mejoras");
		for (Enumeration<Element> e = mejoras.elements(); e.hasMoreElements();) {
			Element nodoMejora = (Element) e.nextElement();
			Mejora mejora = new Mejora();
			mejora.setDescripcion(getString(nodoMejora, "descripcion"));
			mejora.setFechaEntrada(getFecha(nodoMejora, "fecha"));
			mejora.setFechaEjecucion(getFecha(nodoMejora, "fecha_ejecucion"));
			mejora.setFechaUltimaModificacion(getFecha(nodoMejora,
					"fecha_ultima_modificacion"));
			mejora.setImporte(getDouble(nodoMejora, "importe"));
			newMejoras.add(mejora);
		}
		bien.setMejoras(newMejoras);
	}

	/**
	 * Actualiza los datos de tipo mueble
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	private void actualizarDatosMueble(MuebleBean mueble, Element datos)
			throws Exception {
		Element nodoMueble = XMLTranslator_LCGIII.recuperarHijo(datos, "datos_mueble");
		if (nodoMueble == null)
			return;
		mueble.setEstadoConservacion(getId(nodoMueble, "cat_est_cons",
				Estructuras.getListaEstadoConservacion(), mueble
						.getNumInventario()));
		mueble.setCaracteristicas(getString(nodoMueble, "caracteristicas"));
		mueble.setCosteAdquisicion(getDouble(nodoMueble, "coste_adquisicion"));
		mueble.setDestino(getString(nodoMueble, "destino"));
		mueble.setFechaFinGarantia(getFecha(nodoMueble, "fecha_fin_garantia"));
		mueble.setNumSerie(getString(nodoMueble, "num_serie"));
		mueble.setMarca(getString(nodoMueble, "marca"));
		mueble.setModelo(getString(nodoMueble, "modelo"));
		mueble.setDireccion(getString(nodoMueble, "direccion"));
		mueble.setUbicacion(getString(nodoMueble, "ubicacion"));
		mueble.setValorActual(getDouble(nodoMueble, "valor_actual"));
		mueble.setMaterial(getString(nodoMueble, "material"));
		mueble.setArtista(getString(nodoMueble, "autor"));
		mueble.setPropiedad(getId(nodoMueble, "cat_propiedad", Estructuras
				.getListaPropiedadPatrimonial(), mueble.getNumInventario()));
		// cat_clase_cre se encuentra en el xml pero no en la base de datos
		mueble.setClase(getId(nodoMueble, "cat_clase", Estructuras.getListaClaseMuebles(), mueble.getNumInventario()));
	}

	/**
	 * Actualiza Referencias Catastrales
	 * 
	 * @param bien
	 * @param datos
	 */
	private void actualizarRefCatastrales(InmuebleBean inmueble, Element datos) {

		Vector<ReferenciaCatastral> newReferenciasCatastrales = new Vector<ReferenciaCatastral>();

		Vector<Element> refCatastrales = XMLTranslator_LCGIII.recuperarHijosAsVector(
				datos, "datos_refcatastrales");
		for (Enumeration<Element> e = refCatastrales.elements(); e
				.hasMoreElements();) {

			Element nodoCatastral = (Element) e.nextElement();
			ReferenciaCatastral refCatastral = new ReferenciaCatastral();
			refCatastral.setRefCatastral(getString(nodoCatastral,
					"ref_catastral"));
			/*
			 * if (refCatastral.getRefCatastral()!=null &&
			 * refCatastral.getRefCatastral().length()>14){
			 * logger.debug("Referencia catastral demasiado grande transformada a:"
			 * +refCatastral.getRefCatastral());
			 * refCatastral.setRefCatastral(refCatastral
			 * .getRefCatastral().substring(0, 14)); }
			 */
			refCatastral
					.setDescripcion(getString(nodoCatastral, "descripcion"));
			newReferenciasCatastrales.add(refCatastral);
		}
		inmueble.setReferenciasCatastrales(newReferenciasCatastrales);
	}
	
	/**
	 * Actualiza Referencias Catastrales
	 * 
	 * @param bien
	 * @param datos
	 */
	private void actualizarRefCatastrales(ViaBean via, Element datos) {

		Vector<ReferenciaCatastral> newReferenciasCatastrales = new Vector<ReferenciaCatastral>();

		Vector<Element> refCatastrales = XMLTranslator_LCGIII.recuperarHijosAsVector(
				datos, "datos_refcatastrales");
		for (Enumeration<Element> e = refCatastrales.elements(); e
				.hasMoreElements();) {

			Element nodoCatastral = (Element) e.nextElement();
			ReferenciaCatastral refCatastral = new ReferenciaCatastral();
			refCatastral.setRefCatastral(getString(nodoCatastral,
					"ref_catastral"));
			/*
			 * if (refCatastral.getRefCatastral()!=null &&
			 * refCatastral.getRefCatastral().length()>14){
			 * logger.debug("Referencia catastral demasiado grande transformada a:"
			 * +refCatastral.getRefCatastral());
			 * refCatastral.setRefCatastral(refCatastral
			 * .getRefCatastral().substring(0, 14)); }
			 */
			refCatastral
					.setDescripcion(getString(nodoCatastral, "descripcion"));
			newReferenciasCatastrales.add(refCatastral);
		}
		via.setReferenciasCatastrales(newReferenciasCatastrales);
	}

	/**
	 * Actualiza Usos Funcionales
	 * 
	 * @param bien
	 * @param datos
	 */
	private void actualizarUsosFuncionales(InmuebleBean inmueble, Element datos) {
		Vector<UsoFuncional> newUsosFuncionales = new Vector<UsoFuncional>();
		Vector<Element> usosFuncionales = XMLTranslator_LCGIII.recuperarHijosAsVector(
				datos, "datos_usos");
		for (Enumeration<Element> e = usosFuncionales.elements(); e
				.hasMoreElements();) {

			Element nodoUso = (Element) e.nextElement();
			UsoFuncional uso = new UsoFuncional();
			uso.setUso(getString(nodoUso, "cat_uso_func"));
			uso.setSuperficie(getDouble(nodoUso, "superficie"));
			uso.setFecha(getFecha(nodoUso, "fecha"));
			newUsosFuncionales.add(uso);
		}
		inmueble.setUsosFuncionales(newUsosFuncionales);
	}

	/**
	 * Actualiza las observaciones de un bien
	 * 
	 * @param bien
	 * @param datos
	 */
	private Vector<Observacion> getObservaciones(Element datos) {
		Vector<Observacion> newObservaciones = new Vector<Observacion>();
		Vector<Element> observaciones = XMLTranslator_LCGIII.recuperarHijosAsVector(
				datos, "datos_observaciones");
		for (Enumeration<Element> e = observaciones.elements(); e
				.hasMoreElements();) {
			Element nodoObservacion = (Element) e.nextElement();
			Observacion observacion = new Observacion();
			observacion
					.setDescripcion(getString(nodoObservacion, "descripcion"));
			observacion.setFecha(getFecha(nodoObservacion, "fecha"));
			observacion.setFechaUltimaModificacion(getFecha(nodoObservacion,
					"fecha_ultima_modificacion"));
			newObservaciones.add(observacion);
		}
		return newObservaciones;
	}

	/**
	 * Actualiza los datos adicionales de un bien
	 * 
	 * @param datos
	 *            nodo donde se encuentran los datos adicionales
	 */
	private Vector<Observacion> getDatosAdicionales(Element datos) {
		Vector<Observacion> newObservaciones = new Vector<Observacion>();
		Vector<Element> observaciones = XMLTranslator_LCGIII.recuperarHijosAsVector(
				datos, "datos_adicionales");
		for (Enumeration<Element> e = observaciones.elements(); e
				.hasMoreElements();) {
			Element nodoObservacion = (Element) e.nextElement();
			Observacion observacion = new Observacion(getString(
					nodoObservacion, "nombre")
					+ ": " + getString(nodoObservacion, "descripcion"));
			newObservaciones.add(observacion);
		}
		return newObservaciones;
	}

	/**
	 * Actualiza la lista de features a las que está asociada
	 * <datos_refcatastrales> <ref_catastral>String</ref_catastral>
	 * <descripcion>String</descripcion> </datos_refcatastrales>
	 **/
	private void actualizarListaFeatures(BienBean bienBean, Element datos,Element datosGeorreferenciacion)
			throws Exception {
		
		//Primeramente buscamos la georrefenciacion en el nodo "georreferenciacion"
		//Si no se encuentra porque son inventarios antiguos lo buscamos en bienes_inventario
		boolean encontrado=false;
		encontrado=InventarioUtil.
				georreferenciacionPorReferenciaCatastral(geopistaEditor,
											bienBean,datosGeorreferenciacion);
		
		if (!encontrado){
			encontrado=InventarioUtil.georreferenciacionPorVia(geopistaEditor,
						bienBean,datosGeorreferenciacion);		
		}
		if (!encontrado){
			encontrado=InventarioUtil.georreferenciacionPorXY(geopistaEditor,
						bienBean,datosGeorreferenciacion);		
		}
		
		if (!encontrado){
			encontrado=InventarioUtil.georreferenciacionPorBienInventario(geopistaEditor,
					bienBean,datos);		
			
		}
	}
	
	
	

	
	/***
	 * Devuelve el valor String de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private String getString(Element element, String nombre_campo) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null)
			return null;
		return XMLTools.getValue(nodo);
	}

	/***
	 * Devuelve el valor double de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private double getDouble(Element element, String nombre_campo) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null)
			return -1;
		return Double.parseDouble(XMLTools.getValue(nodo));
	}

	/***
	 * Devuelve el valor int de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private int getInt(Element element, String nombre_campo) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null)
			return -1;
		return Integer.parseInt(XMLTools.getValue(nodo));
	}

	/***
	 * Devuelve el valor long de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private long getLong(Element element, String nombre_campo) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null)
			return -1;
		return Long.parseLong(XMLTools.getValue(nodo));
	}

	/***
	 * Devuelve el valor ID de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private String getId(Element element, String nombre_campo,
			ListaEstructuras listaEstructura, String numInventario) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null)
			return null;

		Element nodoTipo = XMLTranslator_LCGIII.recuperarHijo(nodo, "tipo");
		String nombreNodo = XMLTools.getValue(nodoTipo);
		if (!Estructuras.existDomain(nombreNodo)) {
			mensajeResultado += "\n**** WARNING: No existe el dominio:"
					+ nombreNodo
					+ " en la lista de dominios"
					+ (numInventario != null ? ",  para el bien - Número de Inventario: "
							+ numInventario
							: "");
		}

		Element nodoId = XMLTranslator_LCGIII.recuperarHijo(nodo, "id");
		if (nodoId == null)
			return null;
		String id = XMLTools.getValue(nodoId);
		if (id != null) {
			DomainNode domainNode = listaEstructura.getDomainNode(id);
			if (domainNode == null)
				mensajeResultado += "\n**** WARNING: No existe el patrón: "
						+ id
						+ ", para el dominio "
						+ nombreNodo
						+ " Dominio padre:"
						+ listaEstructura.getDominioPadre()
						+ (numInventario != null ? ",  para el bien - Número de Inventario: "
								+ numInventario
								: "");
		}
		return id;
	}

	/***
	 * Devuelve el listado de los documentos
	 * 
	 * @param element
	 * @return
	 */
	private Collection getListadoDocumentos(Element element) {
		Vector<DocumentBean> vectorDocumentBean = new Vector();
		Element nodoPadre = XMLTranslator_LCGIII.recuperarHijo(element,
				"listado_documentos");
		if (nodoPadre == null)
			return null;
		Vector<Element> listaDocumentos = XMLTranslator_LCGIII.recuperarHijosAsVector(
				nodoPadre, "documento");
		if (listaDocumentos == null)
			return null;
		for (Enumeration<Element> e = listaDocumentos.elements(); e
				.hasMoreElements();) {
			Element elemento = e.nextElement();
			String nombreDocumento = XMLTools.getValue(elemento);
			if (nombreDocumento == null || nombreDocumento.trim().length() == 0)
				continue;
			DocumentBean documentBean = new DocumentBean();
			documentBean.setFileName(nombreDocumento);
			documentBean.setFechaEntradaSistema(new Date());
			vectorDocumentBean.add(documentBean);
		}
		return vectorDocumentBean;
	}

	private Vector<File> getDocumentFiles(Collection<DocumentBean> documentos,
			Object bien) throws Exception {
		if (documentos == null || documentos.size() == 0)
			return null;
		if (DOCUMENT_PATH == null && DOCUMENT_SERVER_PATH == null) {
			if (getDocumentPath()!=null)
				DOCUMENT_PATH=getDocumentPath();
			else
				getDocumentDirectory();
		}
		Vector<File> ficheros = new Vector<File>();
		Vector<DocumentBean> auxDocumentos = new Vector<DocumentBean>();
		auxDocumentos.addAll(documentos);
		for (Iterator<DocumentBean> it = auxDocumentos.iterator(); it.hasNext();) {
			DocumentBean documento = (DocumentBean) it.next();
			try {
				// primero buscamos el documento por si existe
				Collection docBuscados = documentClient
						.findDocuments(documento);
				if (docBuscados != null && docBuscados.size() > 0) {
					documento.setId(((DocumentBean) docBuscados.iterator()
							.next()).getId());
					if (DOCUMENT_SERVER_PATH == null)
						actualizaDatosDocOk(bien, 1);
					continue;
				}

				if (DOCUMENT_SERVER_PATH == null) {
					File file = new File((DOCUMENT_PATH == null ? ""
							: DOCUMENT_PATH)
							+ File.separator + documento.getFileName());
					if (!file.exists() || (file.isDirectory())) {
						documentos.remove(documento);
						throw new Exception("No existe el documento");
					}
					documento.setFileName(file.getAbsolutePath());
					documento.setSize(file.length());
					documento = documentClient.updateTipo(documento);
					documento.setTypeByExtension();
					if (documento.isImagen())
						documento
								.setThumbnail(com.geopista.protocol.document.Thumbnail
										.createThumbnail(
												file.getAbsolutePath(), 20, 20));
					ficheros.add(file);
					actualizaDatosDocOk(bien, 1);
				} else {
					documento.setServerPath(DOCUMENT_SERVER_PATH);
				}

			} catch (Exception ex) {
				logger.error("Error al anexar el fichero "
						+ documento.getFileName(), ex);
				Object[] options = {
						aplicacion
								.getI18nString("inventario.loadinventario.opcion1"),
						aplicacion
								.getI18nString("inventario.loadinventario.opcion2"),
						aplicacion
								.getI18nString("inventario.loadinventario.opcion3"),
						aplicacion
								.getI18nString("inventario.loadinventario.opcion4") };
				if (showErrorMessage) {
					int opcion = JOptionPane.showOptionDialog(this,
							"No se ha podido anexar el documento: \n"
									+ (DOCUMENT_PATH == null ? ""
											: DOCUMENT_PATH) + File.separator
									+ documento.getFileName() + "\nCausa: "
									+ ex.getMessage(),
							"Error al anexar documento",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE, null, options,
							options[0]);
					if (opcion == 1)
						showErrorMessage = false;
					if (opcion == 2) {
						DOCUMENT_PATH = getDocumentDirectory();
						return getDocumentFiles(documentos, bien);
					}
					if (opcion == 3)
						throw new Exception(
								"No se ha podido anexar el documento: \n"
										+ (DOCUMENT_PATH == null ? ""
												: DOCUMENT_PATH)
										+ File.separator
										+ documento.getFileName() + "\nCausa: "
										+ ex.getMessage());
				}
				documentosError++;
				mensajeResultado += "\n**** WARNING: Error al anexar el documento: "
						+ DOCUMENT_PATH
						+ File.separator
						+ documento.getFileName();

			}
		}
		return ficheros;
	}

	private void actualizaDatosDocOk(Object bien, int num) {
		documentosOK += num;
		logger.info("Actualizando documentos:" + documentosOK	+ " Numero a incrementar:" + num);
		if (bien instanceof Lote) {
			numDocLote += num;
			return;
		}
		if (bien instanceof BienRevertible) {
			numDocBienRevertible += num;
			return;
		}
		if (bien instanceof CreditoDerechoBean) {
			if (((CreditoDerechoBean) bien).isArrendamiento())
				numDocArrendamiento += num;
			else
				numDocCreditoDerecho += num;
			return;
		}
		if (bien instanceof DerechoRealBean) {
			numDocDerechoReal += num;
			return;
		}
		if (bien instanceof InmuebleBean) {
			if (((BienBean) bien).getTipo().equals(
					Const.PATRON_INMUEBLES_RUSTICOS))
				numDocInmuebleRustico += num;
			else
				numDocInmuebleUrbano += num;
			return;
		}

		if (bien instanceof SemovienteBean) {
			numDocSemoviente += num;
			return;
		}
		if (bien instanceof VehiculoBean) {
			numDocVehiculo += num;
			return;
		}
		if (bien instanceof ViaBean) {
			if (Const.PATRON_VIAS_PUBLICAS_RUSTICAS.equals(((ViaBean) bien)
					.getTipo()))
				numDocViaRustica += num;
			else
				numDocViaUrbana += num;
			return;
		}

		if (bien instanceof MuebleBean) {
			if (Const.PATRON_MUEBLES_HISTORICOART.equals(((MuebleBean) bien)
					.getTipo()))
				numDocHistoricoArtistico += num;
			else
				numDocMueble += num;
			return;
		}
		if (bien instanceof ValorMobiliarioBean) {
			numDocValorMobiliario += num;
			return;
		}

		logger.error("no se ha encontrado la clase del documento");
	}

	/**
	 * Seleccion del directorio local o remoto.
	 * 
	 * @return
	 */
	private String getDocumentDirectory() {
		Object[] options = { "Seleccionar local", "Seleccionar remoto",
				"Cancelar" };
		int n = JOptionPane
				.showOptionDialog(
						this,
						"Seleccione el directorio donde se encuentran los documentos del inventario.\nSeleccionar local: carga los documentos de un directorio local y los envía al servidor."
								+ "\nSeleccionar remoto: obtiene los documentos de un directorio donde se encuentre el servidor.",
						"Seleccionar directorio",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (n == 0) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			if (lastDirectory != null) {
				File currentDirectory = lastDirectory;
				fc.setCurrentDirectory(currentDirectory);
			}
			if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				DOCUMENT_PATH = null;
			DOCUMENT_PATH = fc.getSelectedFile().getAbsolutePath();
			DOCUMENT_SERVER_PATH = null;
		}
		if (n == 1) {

			String seleccion = JOptionPane
					.showInputDialog(
							this,
							"Escriba el directorio remoto donde se encuentran los documentos.",
							Constantes.ACTUAL_REMOTE_DIRECTORY); // el icono
																	// sera un
																	// iterrogante
			DOCUMENT_SERVER_PATH = seleccion;
			DOCUMENT_PATH = null;
			Constantes.ACTUAL_REMOTE_DIRECTORY = seleccion;
		}
		return null;
	}

	/***
	 * Devuelve el valor Date de un nodo
	 * 
	 * @param element
	 * @return
	 */
	private Date getFecha(Element element, String nombre_campo) {
		Element nodo = XMLTranslator_LCGIII.recuperarHijo(element, nombre_campo);
		if (nodo == null || XMLTools.getValue(nodo) == null
				|| XMLTools.getValue(nodo).length() == 0)
			return null;

	    String sFecha=XMLTools.getValue(nodo);
	    if (sFecha.startsWith("00/"))
	    	sFecha=sFecha.replace("00/", "01/");
	    if (sFecha.indexOf("/00/")>=0)
	    	sFecha=sFecha.replace("/00/", "/01/");
	    if (sFecha.endsWith("/00"))
	    	sFecha=sFecha.replace("/00", "/01");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			return sdf.parse(sFecha);
		} catch (Exception ex) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				sdf.setLenient(false);
				return sdf.parse(sFecha);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static void main(String args[]){
		String url1="http://localhost:8081/Geopista/AdministradorCartografiaServlet/InventarioServlet";
		String url2="http://localhost:8081/Geopista/AdministradorCartografiaServlet/DocumentServlet";
	    
        try {
        	
        	ApplicationContext aplicacion= (AppContext)AppContext.getApplicationContext();
			String sUrlPrefix="http://localhost:8081/Geopista/";
			com.geopista.security.SecurityManager.setsUrl(sUrlPrefix);
			//com.geopista.security.SecurityManager.login("satec_pro", "satec12345","Geopista");
			//com.geopista.security.SecurityManager.login("root33030", "satec12345","Geopista");
			com.geopista.security.SecurityManager.login("root33042", "satec12345","Geopista");

	        GeopistaEditor geopistaEditor= new GeopistaEditor("workbench-properties-inventario-simple.xml");
	        geopistaEditor.loadMap("geopista:///"+Constantes.idMapaInventario,null);
	        
			InventarioClient inventarioClient=new InventarioClient(url1);
			DocumentClient documentClient=new DocumentClient(url2);
			//33030. Illas
			//33042. Noreña
			//Municipio municipio=new Municipio("33030","","Illas","");
			Municipio municipio=new Municipio("33042","","Noreña","");
			
			
			String path="c:\\Documents and Settings\\fjgarcia\\Mis documentos\\1.CargaInventario\\01_serpa\\";
			//String path2=path+"30_ILLAS\\";
			String path2=path+"42_Noreña\\";
			LoadInventario loadInventario=new LoadInventario(geopistaEditor,inventarioClient,documentClient,municipio,aplicacion.getI18NResource());
			//loadInventario.setFileName(path2+"Illas_20101229.xml");
			loadInventario.setFileName(path2+"Noreña_20101229.xml");
			loadInventario.setDocumentPath(path2);
			loadInventario.actualizacionTotal=false;
			
			TaskMonitorDialog taskMonitorDialog=new TaskMonitorDialog();
			loadInventario.cargaDatos(taskMonitorDialog);
			System.exit(1);
        }
        catch (Exception e){
        	e.printStackTrace();
        }
	}

}
