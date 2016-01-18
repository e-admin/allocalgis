package com.geopista.app.inventario;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.inventario.panel.BienesJPanel;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.ServletConstants;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.document.Documentable;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.BienRevertible;
import com.geopista.protocol.inventario.CampoFiltro;
import com.geopista.protocol.inventario.ConfigParameters;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.geopista.protocol.inventario.DerechoRealBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InmuebleRusticoBean;
import com.geopista.protocol.inventario.InmuebleUrbanoBean;
import com.geopista.protocol.inventario.InventarioClient;
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
import com.geopista.protocol.inventario.Versionable;
import com.geopista.protocol.inventario.ViaBean;
import com.geopista.ui.plugin.edit.DocumentManagerPlugin;

import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase exporta el inventario de patrimonio a un fichero xml siguiendo un
 * esquema.
 * 
 */
public class SaveInventario extends Save {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PROCESADOS = "_procesados";
	private static final String TOTALES = "_totales";
	private static final String DOCUMENTOS= "_documentos";
	private static final String BIENES="_bienes";

	private InventarioClient inventarioClient = null;

	private Document doc;

	private GeopistaEditor geopistaEditor;

	private DocumentClient documentClient;

	private JRadioButton jRadioAllBienes;

	private JRadioButton jRadioListOfBienes;
	
	private DecimalFormat formateador14;
	
	private Hashtable<String, Integer> resumen = new Hashtable<String, Integer>();

	private DecimalFormat formateador8;
	
	private DecimalFormat formateador12;
	private ArrayList<DocumentBean> documents;


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
	public SaveInventario(java.awt.Frame parent, boolean modal,
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
		
		formateador14 = new DecimalFormat("##############.##");
		DecimalFormatSymbols dfs = formateador14.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');	
		formateador14.setDecimalFormatSymbols(dfs);
		formateador12=new DecimalFormat("############.##");
		formateador12.setDecimalFormatSymbols(dfs);
		formateador8= new DecimalFormat("########.##");	
		formateador8.setDecimalFormatSymbols(dfs);
		documents=new ArrayList<DocumentBean>();

	}
		
	private void saveFiles(TaskMonitorDialog  progressDialog ) throws Exception {
		
		int numeroElemento = 0;
		
		Collection<DocumentBean> documentos= getDocuments();
			for (DocumentBean documentBean : documentos) {
				numeroElemento++;
				Object[] resultados={ new Integer(numeroElemento),
						new Integer(documentos.size()) };
				progressDialog.report(getStringWithParameters(messages,
						"inventario.save.dialogo.report2", resultados)+" "+"documentos");
//				byte[] content=documentBean.getThumbnail();
//				if(content!=null&& documentBean.getFileName()!=null){
//				BufferedWriter document = new java.io.BufferedWriter(new java.io.FileWriter(lastDirectory+"\\"+documentBean.getFileName()));			
					OutputStream document = new FileOutputStream(lastDirectory+"\\"+documentBean.getFileName());
					document.write(documentClient.getAttachedByteStream(documentBean));
//					document.write(documentClient.getAttachedByteStream(document));
//				}
				
			}
			
//			numeroElemento=0;
//			Collection<DocumentBean>textos=documentClient.getTexts();
//			for (DocumentBean documentBean : textos) {
//				numeroElemento++;
//				Object[] resultados={ new Integer(numeroElemento),
//						new Integer(textos.size()) };
//				progressDialog.report(getStringWithParameters(messages,
//						"inventario.saveDocuments.dialogo.report2", resultados)+" "+"textos");
////				BufferedWriter document = new java.io.BufferedWriter(new java.io.FileWriter(lastDirectory+"\\"+documentBean.getFileName()));
//				byte[] content=documentBean.getThumbnail();
//				if(content!=null&& documentBean.getFileName()!=null){
//					OutputStream document = new FileOutputStream(lastDirectory+"\\"+documentBean.getFileName());
//					document.write(documentClient.getAttachedByteStream(documentBean));
//				}
////				document.write(documentClient.getAttachedByteStream(documentBean).toString());
//				
//			}
//			numeroElemento=0;
//			Collection<DocumentBean>imagenes=documentClient.getImages();
//			for (DocumentBean documentBean : imagenes) {
//				numeroElemento++;
//				Object[] resultados={ new Integer(numeroElemento),
//						new Integer(imagenes.size()) };
//				progressDialog.report(getStringWithParameters(messages,
//						"inventario.saveDocuments.dialogo.report2", resultados)+" "+"imágenes");
//				byte[] content=documentBean.getThumbnail();
//				if(content!=null&& documentBean.getFileName()!=null){
//					OutputStream document = new FileOutputStream(lastDirectory+"\\"+documentBean.getFileName());
//					document.write(documentClient.getAttachedByteStream(documentBean));
//				}
//			}
		
				
		
	}

	/**
	 * Guardar los valores del inventario en un documento
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * 
	 */
	protected String getContentOfDocument(TaskMonitorDialog progressDialog) throws ParserConfigurationException, TransformerException {
		doc = createDocument();
		// try {
		Element patrimonio = (Element) doc.createElement("patrimonio");
		doc.appendChild(patrimonio);

		patrimonio.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		patrimonio
				.setAttribute("xsi:noNamespaceSchemaLocation", NOMBRE_ESQUEMA);
		patrimonio.setAttribute("id_municipio", municipio.getId());

		ConfigParameters configParameters = new ConfigParameters();
		configParameters.setLimit(Integer.MAX_VALUE);
		configParameters.setOffset(0);
		Collection<InmuebleBean> inmueblesUrbanos=new ArrayList<InmuebleBean>();
		Collection<InmuebleBean> inmueblesRusticos=new ArrayList<InmuebleBean>();
		Collection<ViaBean> viasUrbanas =new ArrayList<ViaBean>();
		Collection<ViaBean> viasRusticas =new ArrayList<ViaBean>();
		Collection<DerechoRealBean> derechosReales=new ArrayList<DerechoRealBean>();
		Collection<MuebleBean> historicoArt =new ArrayList<MuebleBean>();
		Collection<ValorMobiliarioBean> valoresMobiliarios=new ArrayList<ValorMobiliarioBean>();
		Collection<CreditoDerechoBean> credYDer=new ArrayList<CreditoDerechoBean>();
		Collection<VehiculoBean> vehiculos =new ArrayList<VehiculoBean>();
		Collection<SemovienteBean> semovientes=new ArrayList<SemovienteBean>();
		Collection<CampoFiltro> filtros=new ArrayList<CampoFiltro>();
		Collection<MuebleBean> noAnteriores =new ArrayList<MuebleBean>();
		Collection<BienRevertible> bienesRevertibles=new ArrayList<BienRevertible>();
		Collection<Lote> lotes=new ArrayList<Lote>();
		
		if (jRadioAllBienes.isSelected()) {
			Object[] listafeatures=null;
			//doc debe tener todos los bienes
			progressDialog.report(GeopistaUtil.i18n_getname(
					"inventario.saveinventario.getbienes", messages));
			progressDialog.setSize(480, 100);
			//BIENES
			try {
			inmueblesUrbanos = inventarioClient
							.getBienesInventario(Const.ACTION_GET_INMUEBLES,
									Const.SUPERPATRON_BIENES,
									Const.PATRON_INMUEBLES_URBANOS, null, null,
									listafeatures, configParameters);
			inmueblesUrbanos=getBienesActualVersion(inmueblesUrbanos);

			inmueblesRusticos = inventarioClient
					.getBienesInventario(Const.ACTION_GET_INMUEBLES,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_INMUEBLES_RUSTICOS, null, null,
							listafeatures, configParameters);
			inmueblesRusticos=getBienesActualVersion(inmueblesRusticos);

					
			viasUrbanas = inventarioClient
					.getBienesInventario(Const.ACTION_GET_VIAS,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_VIAS_PUBLICAS_URBANAS, null, null,
							listafeatures, configParameters);
			viasUrbanas=getBienesActualVersion(viasUrbanas);
			
			viasRusticas = inventarioClient
					.getBienesInventario(Const.ACTION_GET_VIAS,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_VIAS_PUBLICAS_RUSTICAS, null, null,
							listafeatures, configParameters);
			viasRusticas=getBienesActualVersion(viasRusticas);
			
			derechosReales=inventarioClient.getBienesInventario(
					Const.ACTION_GET_DERECHOS_REALES,
							Const.SUPERPATRON_BIENES, Const.PATRON_DERECHOS_REALES,
							null, null, listafeatures, configParameters);
			derechosReales=getBienesActualVersion(derechosReales);

			historicoArt = inventarioClient
					.getBienesInventario(Const.ACTION_GET_MUEBLES,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_MUEBLES_HISTORICOART, null, null,
							listafeatures, configParameters);
			historicoArt=getBienesActualVersion(historicoArt);
			
			valoresMobiliarios = inventarioClient
					.getBienesInventario(Const.ACTION_GET_VALORES_MOBILIARIOS,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_VALOR_MOBILIARIO, null, null,
							listafeatures, configParameters);
			valoresMobiliarios=getBienesActualVersion(valoresMobiliarios);

			credYDer = inventarioClient
					.getBienesInventario(Const.ACTION_GET_CREDITOS_DERECHOS,
							Const.SUPERPATRON_BIENES,
							Const.PATRON_CREDITOS_DERECHOS_PERSONALES, null,
							null, listafeatures, configParameters);
			credYDer=getBienesActualVersion(credYDer);
			
			vehiculos = inventarioClient
					.getBienesInventario(Const.ACTION_GET_VEHICULOS,
							Const.SUPERPATRON_BIENES, Const.PATRON_VEHICULOS,
							null, null, listafeatures, configParameters);
			vehiculos=getBienesActualVersion(vehiculos);
			
			semovientes = inventarioClient
					.getBienesInventario(Const.ACTION_GET_SEMOVIENTES,
							Const.SUPERPATRON_BIENES, Const.PATRON_SEMOVIENTES,
							null, null, listafeatures, configParameters);
			semovientes=getBienesActualVersion(semovientes);
//			
			filtros = new ArrayList<CampoFiltro>();
			CampoFiltro filtroNoAnteriores = new CampoFiltro();
			filtroNoAnteriores.setNombre("borrado");
			filtroNoAnteriores.setOperador("=");
			filtroNoAnteriores.setTabla("bienes_inventario");
			filtroNoAnteriores.setValorVarchar("0");
			filtros.add(filtroNoAnteriores);
			
			noAnteriores = inventarioClient.getBienesInventario(
					Const.ACTION_GET_MUEBLES, Const.SUPERPATRON_BIENES,
					Const.PATRON_BIENES_MUEBLES, null, filtros, listafeatures,
					configParameters);
			noAnteriores=getBienesActualVersion(noAnteriores);
			filtros.remove(filtroNoAnteriores);
			
			//BIENES REVERTIBLES
			CampoFiltro filtroReversible = new CampoFiltro();
			filtroReversible.setNombre("borrado");
			filtroReversible.setOperador("=");
			filtroReversible.setTabla("bienes_reversibles");
			filtroReversible.setValorVarchar("0");
			filtros.add(filtroReversible);
			bienesRevertibles = inventarioClient
					.getBienesRevertibles(null, null, filtros, configParameters);
			bienesRevertibles=getBienesActualVersion(bienesRevertibles);

			filtros.remove(filtroReversible);
			
			//LOTES		
			CampoFiltro filtroLotes = new CampoFiltro();
			filtroLotes.setNombre("borrado");
			filtroLotes.setOperador("=");
			filtroLotes.setTabla("lotes");
			filtroLotes.setValorVarchar("0");
			filtros.add(filtroLotes);
			lotes = inventarioClient.getLotes(null, null,filtros, configParameters);
			filtros.remove(filtroLotes);
		
		} catch (Exception ex) {
			logger.error(messages.getString("inventario.getinventario.error"));
			ErrorDialog.show(this, "ERROR",
					messages.getString("inventario.getinventario.error"),
					StringUtil.stackTrace(ex));
			
		}
			
			

		} else if (jRadioListOfBienes.isSelected()) {
			//doc debe tener los bienes de la lista
			BienesJPanel bienesPanel=getInventarioFrame().getInventarioJPanel().getBienesPanel();
			Collection<BienBean> bienes=bienesPanel.getListaBienes();
			if(bienes!=null){
				for (BienBean bien : bienes) {
					
					if (bien instanceof CreditoDerechoBean) {
						credYDer.add((CreditoDerechoBean) bien);
					}
					if (bien instanceof DerechoRealBean) {
						derechosReales.add((DerechoRealBean) bien);
					}
					if (bien instanceof InmuebleBean) {
						if (bien.getTipo().equals(
								Const.PATRON_INMUEBLES_RUSTICOS))
							inmueblesRusticos.add((InmuebleBean) bien);
						else
							inmueblesUrbanos.add((InmuebleBean) bien);
					}
	
					if (bien instanceof SemovienteBean) {
						semovientes.add((SemovienteBean) bien);
					}
					if (bien instanceof VehiculoBean) {
						vehiculos.add((VehiculoBean) bien);
					}
					if (bien instanceof ViaBean) {
						if (Const.PATRON_VIAS_PUBLICAS_RUSTICAS.equals(((ViaBean) bien)
								.getTipo()))
							viasRusticas.add((ViaBean) bien);
						else
							viasUrbanas.add((ViaBean) bien);
					}
	
					if (bien instanceof MuebleBean) {
						if (Const.PATRON_MUEBLES_HISTORICOART.equals(((MuebleBean) bien)
								.getTipo()))
							historicoArt.add((MuebleBean) bien);
						else
							noAnteriores.add((MuebleBean) bien);
					}
					if (bien instanceof ValorMobiliarioBean) {
						valoresMobiliarios.add((ValorMobiliarioBean) bien);
					}
				}
			}
			Collection<Lote> listLotes=bienesPanel.getLotes();	
			if(listLotes!=null)
				lotes.addAll(listLotes);
			Collection<BienRevertible> listBienesRevertibles=bienesPanel.getBienesReversibles();	
			if(listBienesRevertibles!=null)
				bienesRevertibles.addAll(listBienesRevertibles);

		}
		
		
		resumen.put(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+TOTALES, credYDer.size());
		insertarCreditosYDerechos(credYDer, progressDialog, patrimonio);	
		
		resumen.put(Const.PATRON_DERECHOS_REALES+BIENES+TOTALES, derechosReales.size());
		insertarDerechosReales(derechosReales,  progressDialog, patrimonio);	

		resumen.put(Const.PATRON_INMUEBLES_RUSTICOS+BIENES+TOTALES, inmueblesRusticos.size());
		insertarInmuebles(inmueblesRusticos, progressDialog, patrimonio,Const.PATRON_INMUEBLES_RUSTICOS);

	
		resumen.put(Const.PATRON_INMUEBLES_URBANOS+BIENES+TOTALES, inmueblesUrbanos.size());
		insertarInmuebles(inmueblesUrbanos, progressDialog, patrimonio,Const.PATRON_INMUEBLES_URBANOS);
		
		resumen.put(Const.PATRON_MUEBLES_HISTORICOART+BIENES+TOTALES, historicoArt.size());
		insertarMueblesHistoricoArt(historicoArt,  progressDialog, patrimonio);	
		
		resumen.put("otros_muebles"+BIENES+TOTALES, noAnteriores.size());
		insertarMueblesNoAnteriores(noAnteriores,  progressDialog, patrimonio);		
		
		resumen.put(Const.PATRON_SEMOVIENTES+BIENES+TOTALES, semovientes.size());
		insertarSemovientes(semovientes,  progressDialog, patrimonio);	
		
		resumen.put(Const.PATRON_VALOR_MOBILIARIO+BIENES+TOTALES,valoresMobiliarios.size());
		insertarValoresMobiliarios(valoresMobiliarios,  progressDialog, patrimonio);	
		
	
		resumen.put(Const.PATRON_VEHICULOS+BIENES+TOTALES, vehiculos.size());
		insertarVehiculos(vehiculos, progressDialog, patrimonio);
		
		resumen.put(Const.PATRON_VIAS_PUBLICAS_RUSTICAS+BIENES+TOTALES, viasRusticas.size());
		insertarVias(viasRusticas, progressDialog, patrimonio, Const.PATRON_VIAS_PUBLICAS_RUSTICAS);	
		
		resumen.put(Const.PATRON_VIAS_PUBLICAS_URBANAS+BIENES+TOTALES, viasUrbanas.size());
		insertarVias(viasUrbanas,  progressDialog, patrimonio, Const.PATRON_VIAS_PUBLICAS_URBANAS);	
	
		resumen.put("lotes"+BIENES+TOTALES, lotes.size());
		insertarLotes(lotes,progressDialog,patrimonio);
		
		resumen.put("bienes_revertibles"+BIENES+TOTALES, bienesRevertibles.size());
		insertarBienesRevertibles(bienesRevertibles,progressDialog,patrimonio);	
	
		try {
			saveFiles( progressDialog);
		}catch (Exception ex) {
			
			logger.error(
					"Se ha producido un error al guardar los documentos adjuntos",
					ex);
			ErrorDialog
					.show(progressDialog,
							"ERROR",
							messages.getString("inventario.saveinventario.documentos.error"),
							StringUtil.stackTrace(ex));
		}
		finally{
			//clear documents
			documents.clear();
		}

		insertarResumen(progressDialog,patrimonio);
		
		return parseDocumentToXML(doc);
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
	 * Cambia el lenguaje de los textos
	 * 
	 * @param messages
	 */
	public void changeScreenLang(ResourceBundle messages) {

		try {
			setTitle(GeopistaUtil.i18n_getname(
					"inventario.saveinventario.title", messages));
			// jPanelLoad.setToolTipText(GeopistaUtil.i18n_getname(
			// "inventario.loadinventario.title", messages));
			jLabelRadio.setText(GeopistaUtil.i18n_getname("radio.info",
					messages));
			jButtonCancelar.setText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonCancelar.setToolTipText(GeopistaUtil.i18n_getname(
					"OKCancelPanel.Cancel", messages));
			jButtonGuardar.setText(GeopistaUtil.i18n_getname(
					"document.infodocument.botones.guardar", messages)
					+ " "+GeopistaUtil.i18n_getname(
					"inventario.InventarioJPanel.name", messages));
			jButtonGuardar.setToolTipText(GeopistaUtil.i18n_getname(
					"document.infodocument.botones.guardar", messages)
					+ " "+GeopistaUtil.i18n_getname(
					"inventario.InventarioJPanel.name", messages));
			jRadioAllBienes.setText(GeopistaUtil.i18n_getname(
					"radio.ALLBIENES", messages));
			jRadioListOfBienes.setText(GeopistaUtil.i18n_getname(
					"radio.LISTOFBIENES", messages));
//			String[] literalMunicipio = { this.municipio.getNombre() };


		} catch (Exception ex) {
			logger.error("Falta algun recurso:", ex);
		}
	}

	/**
	 * Almacena un bien de tipo CreditoDerechoBean
	 * 
	 * @param creditoDerechoBean
	 * @param elementoPatrimonio
	 */
	private void insertarCreditoYDerecho( CreditoDerechoBean creditoDerechoBean,Element elementoPatrimonio)
			{

		insertarDatosComunes(creditoDerechoBean, elementoPatrimonio);
		
		saveCuentaAmortizacion(creditoDerechoBean.getCuentaAmortizacion(),elementoPatrimonio); // todo está en el xml pero no en la  aplicacion
		saveSeguro(creditoDerechoBean.getSeguro(),elementoPatrimonio);

		insertarDatosCreditoDerecho(creditoDerechoBean, elementoPatrimonio);
		// creditoDerecho.setRegistro(getRegistro, elementoPatrimonio); todo
		// está en el xml pero no en la aplicacion
		// actualizarDatosContable(creditoDerecho,elementoPatrimonio); todo está
		// en el xml pero no en la aplicacion
		saveObservaciones(creditoDerechoBean.getObservaciones(),elementoPatrimonio);
//TODO:Falta saber cuales son, (supongo que los comentarios que no llevan como nombre un dominio) y por qué aparecen sin los dos puntos entre la separación del nombre y la descripción, cuando en LoadInventario se añade la observacion de esta forma
		//saveDatosAdicionales(creditoDerechoBean.getObservaciones(),elementoPatrimonio);
	}

	/**
	 * Inserta en el documento un bien de tipo DerechoReal
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarDerechoReal(DerechoRealBean derechoReal,Element elementoPatrimonio){

		insertarDatosComunes(derechoReal, elementoPatrimonio);	
		saveCuentaAmortizacion(derechoReal.getCuentaAmortizacion(),elementoPatrimonio);// todo está en el xml pero no en la aplicacion

		saveSeguro(derechoReal.getSeguro(),elementoPatrimonio);
		
		insertarDatosDerecho(derechoReal, elementoPatrimonio);
		saveDatosRegistro(derechoReal.getRegistro(),elementoPatrimonio);
		// actualizarDatosContable(derechoReal,elementoPatrimonio);todo está en
		// el xml pero no en la aplicacion
		saveObservaciones(derechoReal.getObservaciones(), elementoPatrimonio);
//		saveDatosAdicionales(derechoReal.getObservaciones(),elementoPatrimonio);
	}

	/**
	 * Almacena una observación con algún campo de los que se encuentra en la
	 * url pero no en BD
	 */
	private void insertarObservacion(Collection<Observacion>observaciones, Element nodoBien,String etiqueta) {
		if(observaciones!=null)
		{
			for (Observacion observacion: observaciones) {
	
				Element elementNew = doc.createElement(etiqueta);
				nodoBien.appendChild(elementNew);
				elementNew
						.setTextContent(observacion
								.getDescripcion()
								.substring(
										observacion.getDescripcion().indexOf(
												":") + 2,
										observacion.getDescripcion().length()));//EL 2 elimina el espacio introducido en la descripcion
			}
			if(observaciones.size()==0){
				Element elementNew = doc.createElement(etiqueta);
				nodoBien.appendChild(elementNew);
				elementNew.setTextContent("");
			}
		}
	}

	/**
	 * exportar un inmueble
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarInmueble(InmuebleBean inmuebleBean, Element inmuebleElement){
	
		logger.info("Exportando:" + inmuebleBean.getNombre());
		
		insertarDatosComunes(inmuebleBean, inmuebleElement);
		saveCuentaAmortizacion(inmuebleBean.getCuentaAmortizacion(), inmuebleElement);

		insertarDatosInmueble(inmuebleBean, inmuebleElement);
		saveDatosRegistro(inmuebleBean.getRegistro(), inmuebleElement);

		if (inmuebleBean.getTipo().equals(Const.PATRON_INMUEBLES_URBANOS)){
			insertarDatosInmuebleUrbano(inmuebleBean, inmuebleElement);	
		} else if (inmuebleBean.getTipo().equals(Const.PATRON_INMUEBLES_RUSTICOS)) {
			insertarDatosInmuebleRustico(inmuebleBean, inmuebleElement);
		}
		saveSeguro(inmuebleBean.getSeguro(), inmuebleElement);
		
		insertarMejoras(inmuebleBean.getMejoras(), inmuebleElement);
		saveObservaciones(inmuebleBean.getObservaciones(), inmuebleElement);

		insertarRefCatastrales(inmuebleBean.getReferenciasCatastrales(),
				inmuebleElement);
		insertarUsosFuncionales(inmuebleBean.getUsosFuncionales(), inmuebleElement);
//		
//		saveDatosAdicionales(inmuebleBean.getObservaciones(),inmuebleElement);

	}
//
	/**
	 * Crea un bien de tipo HistoricoArtistico
	 * 
	 * @param elementoPatrimonio
	 */
	private void insertarMueble(MuebleBean mueble ,Element elementoPatrimonio)
			 {
		
		insertarDatosComunes(mueble, elementoPatrimonio);
		saveCuentaAmortizacion(mueble.getCuentaAmortizacion(),elementoPatrimonio);
		insertarDatosMueble(mueble, elementoPatrimonio);
		saveSeguro(mueble.getSeguro(),elementoPatrimonio);

		// está en el xml pero no en la aplicacion
		saveObservaciones(mueble.getObservaciones(),elementoPatrimonio);
//		saveDatosAdicionales(mueble.getObservaciones(),elementoPatrimonio);


	}

	/**
	 * exporta un bien de tipo Semoviente
	 *@param semoviente
	 * @param elementoPatrimonio
	 */
	private void insertarSemoviente(SemovienteBean semovienteBean,Element elementoPatrimonio)
			{

		insertarDatosComunes(semovienteBean, elementoPatrimonio);
		saveCuentaAmortizacion(semovienteBean.getCuentaAmortizacion(),elementoPatrimonio);
		insertarDatosSemoviente(semovienteBean, elementoPatrimonio);
		saveSeguro(semovienteBean.getSeguro(),elementoPatrimonio);
		// está en el xml pero no en la aplicacion
		saveObservaciones(semovienteBean.getObservaciones(),elementoPatrimonio);
//		saveDatosAdicionales(semovienteBean.getObservaciones(),elementoPatrimonio);

	}

	/**
	 * Inserta un bien de tipo ValorMobiliario
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarValorMobiliario(ValorMobiliarioBean valorMobiliario,Element elementoPatrimonio)
			{

		insertarDatosComunes(valorMobiliario, elementoPatrimonio);
		saveCuentaAmortizacion(valorMobiliario.getCuentaAmortizacion(),elementoPatrimonio);
		insertarDatosValorMobiliario(valorMobiliario, elementoPatrimonio);
		// mobiliario.setRegistro(getDatosRegistro(elementoPatrimonio)); //TODO
		// no existe en la clase el registrp
		saveSeguro(valorMobiliario.getSeguro(),elementoPatrimonio);
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		saveObservaciones(valorMobiliario.getObservaciones(),elementoPatrimonio);
//		saveDatosAdicionales(valorMobiliario.getObservaciones(),elementoPatrimonio);

	}

	/**
	 * Exporta un bien de tipo Vehiculo
	 * 
	 * @param vehiculoBean
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarVehiculo(VehiculoBean vehiculoBean,Element elementoPatrimonio)
		{
		
		insertarDatosComunes(vehiculoBean, elementoPatrimonio);
		saveCuentaAmortizacion(vehiculoBean.getCuentaAmortizacion(),elementoPatrimonio);
		insertarDatosVehiculo(vehiculoBean, elementoPatrimonio);
		saveSeguro(vehiculoBean.getSeguro(),elementoPatrimonio);
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		saveObservaciones(vehiculoBean.getObservaciones(),elementoPatrimonio);
//		saveDatosAdicionales(vehiculoBean.getObservaciones(),elementoPatrimonio);

	}

	/**
	 * Almacena un bien de tipo Via Rustica
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarViaRustica(BienBean viaRustica,Element elementoPatrimonio){

		insertarDatosComunes(viaRustica, elementoPatrimonio);
		saveCuentaAmortizacion(viaRustica.getCuentaAmortizacion(),elementoPatrimonio);
		insertarDatosVia((ViaBean) viaRustica, elementoPatrimonio);
		saveSeguro(viaRustica.getSeguro(),elementoPatrimonio);
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		insertarMejoras(viaRustica.getMejoras(), elementoPatrimonio);
		insertarRefCatastrales(((ViaBean) viaRustica).getReferenciasCatastrales(), elementoPatrimonio);
		saveObservaciones(viaRustica.getObservaciones(),elementoPatrimonio);
//		saveDatosAdicionales(viaRustica.getObservaciones(),elementoPatrimonio);

	}

	/**
	 * Almacena un bien de tipo Via Urbana
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarViaUrbana(BienBean viaUrbana, Element elementoPatrimonio)  {

		insertarDatosComunes(viaUrbana, elementoPatrimonio);
		saveCuentaAmortizacion(viaUrbana.getCuentaAmortizacion(), elementoPatrimonio);
		insertarDatosVia((ViaBean) viaUrbana, elementoPatrimonio);
		saveSeguro(viaUrbana.getSeguro(),elementoPatrimonio);
		// actualizarDatosContables(historicoArtistico,elementoPatrimonio);//todo
		// está en el xml pero no en la aplicacion
		insertarMejoras(viaUrbana.getMejoras(), elementoPatrimonio);
		insertarRefCatastrales(((ViaBean) viaUrbana).getReferenciasCatastrales(), elementoPatrimonio);
		saveObservaciones(viaUrbana.getObservaciones(), elementoPatrimonio);
//		saveDatosAdicionales(viaUrbana.getObservaciones(),elementoPatrimonio);

	}

	/**
	 * Exporta un bien de tipo BienRevertible
	 * 
	 * @param elementoPatrimonio
	 * @return
	 */
	private void insertarBienRevertible(BienRevertible bienRevertible,Element elementoPatrimonio)
			 {
		insertarDatosBienRevertible(bienRevertible, elementoPatrimonio);
		saveCuentaAmortizacion(bienRevertible.getCuentaAmortizacion(),elementoPatrimonio);
		saveObservaciones(bienRevertible.getObservaciones(),elementoPatrimonio);
		
//		saveDatosAdicionales(bienRevertible.getObservaciones(), elementoPatrimonio)
		
	}

	/**
	 * Exporta a un doc un bien de tipo Lote
	 * @param lote
	 */
	private void insertarLote(Lote lote,Element elementoPatrimonio){
		insertarDatosLote(lote, elementoPatrimonio);	
		saveSeguro(lote.getSeguro(),elementoPatrimonio);

	}

	/***
	 * Almacena el listado de los documentos y devuelve el numero de documentos procesados
	 * 
	 * @param element
	 * @return
	 * @throws Exception 
	 */
	private int saveListadoDocumentos(Documentable bien,Element elementoPatrimonio) {

		int numDocumentosProcesados=0;
		//listado_documentos
		Element listado_documentos= doc.createElement("listado_documentos");
		elementoPatrimonio.appendChild(listado_documentos);
		Collection<DocumentBean> documentos = null;
		try {
			documentos = documentClient.getAttachedDocuments(bien);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(documentos!=null){
			for (DocumentBean documentoBean : documentos){
				//listado_documentos
				Element documento = doc.createElement("documento");
				listado_documentos.appendChild(documento);
				documento.setTextContent(documentoBean.getFileName());
				documents.add(documentoBean);
				numDocumentosProcesados++;
//				for (DocumentBean documentBean : documentos) {
//					
//					BufferedWriter document;
//					document = new java.io.BufferedWriter(new java.io.FileWriter(lastDirectory+"\\"+documentBean.getFileName()));
//					document.write(documentClient.getAttachedByteStream(documentBean).toString());					
//			
//				}
			}
		}
		return numDocumentosProcesados;

	}	

	private void saveDatosAdicionales(Collection<Observacion> observaciones,
			Element elementoPatrimonio) {
		if(observaciones!=null){
			//datos_adicionales
			Element datos_adicionales = doc.createElement("datos_adicionales");
			elementoPatrimonio.appendChild(datos_adicionales);
			for (Observacion observacion : observaciones) {
				String[] nombreDescripcion= observacion.getDescripcion().split(":");
					if(nombreDescripcion.length==2){
						if(!Estructuras.existDomain(nombreDescripcion[0])){
							//datos_adicionales
							Element dato_adicional = doc.createElement("dato_adicional");
							datos_adicionales.appendChild(dato_adicional);
								//nombre
								Element nombre = doc.createElement("nombre");
								dato_adicional.appendChild(nombre);
								nombre.setTextContent(nombreDescripcion[0]);
								
								//descripcion
								Element descripcion = doc.createElement("descripcion");
								dato_adicional.appendChild(descripcion);
								descripcion.setTextContent(nombreDescripcion[1]);
						}
					}
			}
		}		
	}

	/**
	 * Almacena los datos de amortizacion
	 * 
	 * @param datos
	 * @return
	 */
	private void saveCuentaAmortizacion(CuentaAmortizacion cuentaAmortizacion,
			Element elementoPatrimonio){
		if(cuentaAmortizacion!=null){
			Element amortizacion = doc.createElement("amortizacion");
			elementoPatrimonio.appendChild(amortizacion);
	
			Element cuenta = doc.createElement("cuenta");
			amortizacion.appendChild(cuenta);
			cuenta.setTextContent(cuentaAmortizacion.getCuenta());
	
			Element descripcion = doc.createElement("descripcion");
			amortizacion.appendChild(descripcion);
			descripcion.setTextContent(cuentaAmortizacion.getDescripcion());
	
			Element anios = doc.createElement("anios");
			amortizacion.appendChild(anios);
			anios.setTextContent(Integer.toString(cuentaAmortizacion.getAnnos()));
	
			Element porcentaje = doc.createElement("porcentaje");
			amortizacion.appendChild(porcentaje);
			porcentaje.setTextContent(Double.toString(cuentaAmortizacion.getPorcentaje()));
	
			Element total_amortizado = doc.createElement("total_amortizado");
			amortizacion.appendChild(total_amortizado);
			total_amortizado.setTextContent(formateador14.format(cuentaAmortizacion.getTotalAmortizado()));
	
			Element cat_amortizacion = doc.createElement("cat_amortizacion");
			amortizacion.appendChild(cat_amortizacion);
				//cat_amortizacion//tipo
				Element cat_amortizacion_tipo = doc.createElement("tipo");
				cat_amortizacion_tipo.appendChild(cat_amortizacion);
				cat_amortizacion_tipo.setTextContent(Estructuras.getListaTipoAmortizacion().getDominioPadre());
				// cat_amortizacion//id
				Element cat_amortizacion_id = doc.createElement("id");
				cat_amortizacion.appendChild(cat_amortizacion_id);
				cat_amortizacion_id.setTextContent(cuentaAmortizacion.getTipoAmortizacion());
		}

	}

	/**
	 * Método que inserta los datos generales de un bien credito y derecho
	 * en un documento
	 * 
	 * @param credito a insertar
	 * @param datos
	 */
	private void insertarDatosCreditoDerecho(CreditoDerechoBean creditoYDerecho,
			Element datos) {
		
		//datos_credito
		Element datos_credito = doc.createElement("datos_credito");
		datos.appendChild(datos_credito);
		// cat_cred_der
		Element cat_cred_der= doc.createElement("cat_cred_der");
		datos_credito.appendChild(cat_cred_der);
			//cat_clase_der//tipo
			Element  cat_cred_der_tipo=doc.createElement("tipo");
			 cat_cred_der.appendChild( cat_cred_der_tipo);
			 cat_cred_der_tipo.setTextContent(Estructuras.getListaConceptosCreditosDerechos().getDominioPadre());
			// cat_cred_der//id
			Element  cat_cred_der_id=doc.createElement("id");
			cat_cred_der.appendChild(cat_cred_der_id);
			cat_cred_der_id.setTextContent(creditoYDerecho.getConcepto());
		// deudor
		Element deudor= doc.createElement("deudor");
		datos_credito.appendChild(deudor);
		deudor.setTextContent(creditoYDerecho.getDeudor());
		//destino
		Element destino=doc.createElement("destino");
		datos_credito.appendChild(destino);
		destino.setTextContent(creditoYDerecho.getDestino());
		//importe
		Element importe= doc.createElement("importe");
		datos_credito.appendChild(importe);
		importe.setTextContent(creditoYDerecho.getImporte()!=null?formateador14.format(creditoYDerecho.getImporte()):Double.toString(0.0));
		//fecha_vencimiento
		Element fecha_vencimiento= doc.createElement("fecha_vencimiento");
		datos_credito.appendChild(fecha_vencimiento);
		fecha_vencimiento.setTextContent(dateToDDMMYYY(creditoYDerecho.getFechaVencimiento()));
		//concepto_desc
		Element concepto_desc= doc.createElement("concepto_desc");
		datos_credito.appendChild(concepto_desc);
		concepto_desc.setTextContent(creditoYDerecho.getConceptoDesc());
		// descripcion
		Element descripcion= doc.createElement("descripcion");
		datos_credito.appendChild(descripcion);
		descripcion.setTextContent(creditoYDerecho.getDescripcion());

		Collection<Observacion>observacionesFechaInicio=getObservacionesFiltradas(creditoYDerecho.getObservaciones(),GeopistaUtil
				.i18n_getname("inventario.loadinventario.fecha_inicio"));
		insertarObservacion(observacionesFechaInicio, datos_credito,"fecha_inicio" );

		Collection<Observacion>observacionesCaracteristicas=getObservacionesFiltradas(creditoYDerecho.getObservaciones(),GeopistaUtil
				.i18n_getname("inventario.loadinventario.caracteristicas"));
		insertarObservacion(observacionesCaracteristicas, datos_credito,"caracteristicas" );
		
		Collection<Observacion>observacionesAmortAnual=getObservacionesFiltradas(creditoYDerecho.getObservaciones(),GeopistaUtil
				.i18n_getname("inventario.loadinventario.amortizacion_anual"));
		insertarObservacion(observacionesAmortAnual, datos_credito,"amortizacion_anual");
		
		Collection<Observacion>observacionesAcreedor=getObservacionesFiltradas(creditoYDerecho.getObservaciones(),GeopistaUtil
				.i18n_getname("inventario.loadinventario.acreedor"));
		insertarObservacion(observacionesAcreedor, datos_credito,"acreedor");

		// cat_clase_cre se encuentra en el xml pero no en la base de datos
		Element cat_clase_cred = doc.createElement("cat_clase_cred");
		datos_credito.appendChild(cat_clase_cred);
			//cat_clase_cre//tipo
			Element cat_clase_cre_tipo = doc.createElement("tipo");
			cat_clase_cred.appendChild(cat_clase_cre_tipo);
			cat_clase_cre_tipo.setTextContent(Estructuras.getListaClaseCredito().getDominioPadre());
			// cat_clase_cre//id
			Element cat_clase_cred_id = doc.createElement("id");
			cat_clase_cred.appendChild(cat_clase_cred_id);
			cat_clase_cred_id.setTextContent(creditoYDerecho.getTipo());
			
		// cat_subclase se encuentra en el xml pero no en la base de datos
		Element cat_subclase = doc.createElement("cat_subclase");
		datos_credito.appendChild(cat_subclase);
			//cat_subclase//tipo
			Element cat_subclase_tipo = doc.createElement("tipo");
			cat_subclase.appendChild(cat_subclase_tipo);
			cat_clase_cre_tipo.setTextContent(Estructuras.getListaSubclaseCredito().getDominioPadre());
			// cat_subclase//id
			Element cat_subclase_id = doc.createElement("id");
			cat_subclase.appendChild(cat_subclase_id);
			cat_subclase_id.setTextContent(creditoYDerecho.getSubClase());
		//arrendamiento
		Element arrendamiento= doc.createElement("arrendamiento");
		datos_credito.appendChild(arrendamiento);
		arrendamiento.setTextContent(creditoYDerecho.isArrendamiento()?"Si":"No");

	}

	/**
	 * Guarda los datos de los derechos reales
	 * 
	 * @param derechoReal
	 * @param datos
	 */
	private void insertarDatosDerecho(DerechoRealBean derechoReal,
			Element datos){

		// datos_inmuebles
		Element datos_derecho = doc.createElement("datos_derecho");
		datos.appendChild(datos_derecho);
		//destino
		Element destino = doc.createElement("destino");
		datos_derecho .appendChild(destino);
		destino.setTextContent(derechoReal.getDestino());
		//bien
		Element bien = doc.createElement("bien");
		datos_derecho .appendChild(bien);
		bien.setTextContent(derechoReal.getBien());
		//coste
		Element coste = doc.createElement("coste");
		datos_derecho .appendChild(coste);
		coste.setTextContent(derechoReal.getCosteAdquisicion()!=null?formateador14.format(derechoReal.getCosteAdquisicion()):Double.toString(0.0));
		//destino
		Element valor = doc.createElement("valor");
		datos_derecho .appendChild(valor);
		valor.setTextContent(derechoReal.getValorActual()!=null?formateador14.format(derechoReal.getValorActual()):Double.toString(0.0));		
		// cat_clase_der se encuentra en el xml pero no en la base de datos
		Element cat_clase_der = doc.createElement("cat_clase_der");
		datos_derecho .appendChild(cat_clase_der);
			//cat_clase_der//tipo
			Element cat_clase_der_tipo=doc.createElement("tipo");
			cat_clase_der.appendChild(cat_clase_der_tipo);
			cat_clase_der_tipo.setTextContent(Estructuras.getListaClaseDerechosReales().getDominioPadre());
			//cat_clase_der//id
			Element cat_clase_der_id=doc.createElement("id");
			cat_clase_der.appendChild(cat_clase_der_id);
			cat_clase_der_id.setTextContent(derechoReal.getTipo());


//			Collection<Observacion> observacionesUso = getObservacionesFiltradas(
//					derechoReal.getObservaciones(),
//					GeopistaUtil.i18n_getname("inventario.loadinventario.cat_clase_der"));
//			insertarObservacion(observacionesUso, datos_derecho,"descripcion");

	}

	/**
	 * inserta los datos de inmueble
	 * 
	 * @param bienBean
	 * @param datos
	 */
	private void insertarDatosInmueble(InmuebleBean inmuebleBean,
			Element datos) {

		// datos_inmuebles
		Element datos_inmuebles = doc.createElement("datos_inmuebles");
		datos.appendChild(datos_inmuebles);

		// direccion
		Element direccion = doc.createElement("direccion");
		datos_inmuebles.appendChild(direccion);
		direccion.setTextContent(inmuebleBean.getDireccion());
		// lindero_norte
		Element lindero_norte = doc.createElement("lindero_norte");
		datos_inmuebles.appendChild(lindero_norte);
		lindero_norte.setTextContent(inmuebleBean.getLinderoNorte());
		// lindero_sur
		Element lindero_sur = doc.createElement("lindero_sur");
		datos_inmuebles.appendChild(lindero_sur);
		lindero_sur.setTextContent(inmuebleBean.getLinderoSur());
		// lindero_este
		Element lindero_este = doc.createElement("lindero_este");
		datos_inmuebles.appendChild(lindero_este);
		lindero_este.setTextContent(inmuebleBean.getLinderoEste());
		// lindero_oeste
		Element lindero_oeste = doc.createElement("lindero_oeste");
		datos_inmuebles.appendChild(lindero_oeste);
		lindero_oeste.setTextContent(inmuebleBean.getLinderoOeste());
		//registro
		Element registro = doc.createElement("registro");
		datos_inmuebles.appendChild(registro);
		registro.setTextContent(inmuebleBean.getRegistroDesc());
		// calificacion
		Element calificacion = doc.createElement("calificacion");
		datos_inmuebles.appendChild(calificacion);
		calificacion.setTextContent(inmuebleBean.getCalificacion());
		// superficie_registral_suelo
		Element superficie_registral_suelo = doc
				.createElement("superficie_registral_suelo");
		datos_inmuebles.appendChild(superficie_registral_suelo);
		superficie_registral_suelo.setTextContent(formateador8.format(inmuebleBean.getSuperficieRegistralSuelo()));
		// superficie_catastral_suelo
		Element superficie_catastral_suelo = doc
				.createElement("superficie_catastral_suelo");
		datos_inmuebles.appendChild(superficie_catastral_suelo);
		superficie_catastral_suelo.setTextContent(formateador8.format(inmuebleBean
				.getSuperficieCatastralSuelo()));
		// superficie_real_suelo
		Element superficie_real_suelo = doc
				.createElement("superficie_real_suelo");
		datos_inmuebles.appendChild(superficie_real_suelo);
		superficie_real_suelo.setTextContent(formateador8.format(inmuebleBean
				.getSuperficieRealSuelo()));
		// superficie_registral_construccion
		Element superficie_registral_construccion = doc
				.createElement("superficie_registral_construccion");
		datos_inmuebles.appendChild(superficie_registral_construccion);
		superficie_registral_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieRegistralConstruccion()));
		// superficie_catastral_construccion
		Element superficie_catastral_construccion = doc
				.createElement("superficie_catastral_construccion");
		datos_inmuebles.appendChild(superficie_catastral_construccion);
		superficie_catastral_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieCatastralConstruccion()));		
		// superficie_catastral_suelo
		Element superficie_real_construccion = doc
				.createElement("superficie_real_construccion");
		datos_inmuebles.appendChild(superficie_real_construccion);
		superficie_real_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieRealConstruccion()));
		
		// derechosrealesfavor
		Element derechosrealesfavor = doc.createElement("derechosrealesfavor");
		datos_inmuebles.appendChild(derechosrealesfavor);
		derechosrealesfavor.setTextContent(inmuebleBean.getDerechosRealesFavor());
		// valor_derechos_favor
		Element valor_derechos_favor = doc
				.createElement("valor_derechos_favor");
		datos_inmuebles.appendChild(valor_derechos_favor);
		valor_derechos_favor.setTextContent(formateador8.format(inmuebleBean
				.getValorDerechosFavor()));
		Element derechosrealescontra = doc
				.createElement("derechosrealescontra");
		datos_inmuebles.appendChild(derechosrealescontra);
		derechosrealescontra.setTextContent(inmuebleBean.getDerechosRealesContra());
		// valor_derechos_contra
		Element valor_derechos_contra = doc
				.createElement("valor_derechos_contra");
		datos_inmuebles.appendChild(valor_derechos_contra);
		valor_derechos_contra.setTextContent(formateador8.format(inmuebleBean
				.getValorDerechosContra()));
		// derechospersonales
		Element derechospersonales = doc.createElement("derechospersonales");
		datos_inmuebles.appendChild(derechospersonales);
		derechospersonales.setTextContent(inmuebleBean.getDerechosPersonales());
		// fechaobra
		Element fechaobra = doc.createElement("fechaobra");
		datos_inmuebles.appendChild(fechaobra);
		fechaobra.setTextContent(dateToDDMMYYY(inmuebleBean
				.getFechaObra()));
		// destino
		Element destino = doc.createElement("destino");
		datos_inmuebles.appendChild(destino);
		destino.setTextContent(inmuebleBean.getDestino());
		if(inmuebleBean.getPropiedad()!=null&&inmuebleBean.getPropiedad()!=""){
			//cat_propiedad
			 Element cat_propiedad = doc.createElement("cat_propiedad");
			 datos_inmuebles.appendChild(cat_propiedad);
			 	//cat_propiedad//tipo 
				Element cat_propiedad_tipo = doc.createElement("tipo");
				cat_propiedad.appendChild(cat_propiedad_tipo);
				cat_propiedad_tipo.setTextContent(Estructuras.getListaPropiedadPatrimonial().getDominioPadre());
				//cat_propiedad//id
				Element cat_propiedad_id = doc.createElement("id");
				cat_propiedad.appendChild(cat_propiedad_id);
				cat_propiedad_id.setTextContent(inmuebleBean.getPropiedad());
		}
		// ref_catastral
		if (inmuebleBean.getRefCatastral() != null) {
			if (inmuebleBean.getRefCatastral().length() > 14) {
				logger.debug("Referencia catastral demasiado grande transformada a:"
						+ inmuebleBean.getRefCatastral());
				inmuebleBean.setRefCatastral(inmuebleBean.getRefCatastral()
							.substring(0, 14));
			}
			Element ref_catastral = doc.createElement("ref_catastral");
			datos_inmuebles.appendChild(ref_catastral);
			ref_catastral.setTextContent(inmuebleBean.getRefCatastral());

		} 
//			else {
//				logger.debug("Referencia catastral is null ");
//				throw new NullPointerException();
//			}
		// num_plantas
		Element num_plantas = doc.createElement("num_plantas");
		datos_inmuebles.appendChild(num_plantas);
		num_plantas.setTextContent(inmuebleBean.getNumPlantas());
		
		if(inmuebleBean.getEstadoConservacion()!=null&&inmuebleBean.getEstadoConservacion()!=""){
			// cat_est_cons
			Element cat_est_cons = doc.createElement("cat_est_cons");
			datos_inmuebles.appendChild(cat_est_cons);
				// cat_est_cons//tipo 
				Element cat_est_cons_tipo = doc.createElement("tipo");
				cat_est_cons.appendChild(cat_est_cons_tipo);
				cat_est_cons_tipo.setTextContent(Estructuras.getListaEstadoConservacion().getDominioPadre());
				// cat_est_cons//id
				Element cat_est_cons_id = doc.createElement("id");
				cat_est_cons.appendChild(cat_est_cons_id);
				cat_est_cons_id.setTextContent(inmuebleBean.getEstadoConservacion());
		}
		if(inmuebleBean.getTipoConstruccion()!=null&& inmuebleBean.getTipoConstruccion()!=""){
			// cat_construccion
			Element cat_construccion = doc.createElement("cat_construccion");
			datos_inmuebles.appendChild(cat_construccion);
				//cat_construccion//tipo 
				Element cat_construccion_tipo = doc.createElement("tipo");
				cat_construccion.appendChild(cat_construccion_tipo);
				cat_construccion_tipo.setTextContent(Estructuras.getListaTipoConstruccion().getDominioPadre());
				//cat_construccion//id
				Element cat_construccion_id = doc.createElement("id");
				cat_construccion.appendChild(cat_construccion_id);
				cat_construccion_id.setTextContent(inmuebleBean.getTipoConstruccion());
		}
		if(inmuebleBean.getCubierta()!=null && inmuebleBean.getCubierta()!=""){
			//cat_cubierta
			Element cat_cubierta = doc.createElement("cat_cubierta");
			datos_inmuebles.appendChild(cat_cubierta);
				//cat_construccion//tipo 
				Element cat_cubierta_tipo = doc.createElement("tipo");
				cat_cubierta.appendChild(cat_cubierta_tipo);
				cat_cubierta_tipo.setTextContent(Estructuras.getListaTipoCubierta().getDominioPadre());
				//cat_construccion//id
				Element cat_cubierta_id = doc.createElement("id");
				cat_cubierta.appendChild(cat_cubierta_id);
				cat_cubierta_id.setTextContent(inmuebleBean.getCubierta());	
		}
		if(inmuebleBean.getCarpinteria()!=null&& inmuebleBean.getCarpinteria()!=""){
			//cat_carpinteria
			Element cat_carpinteria = doc.createElement("cat_carpinteria");
			datos_inmuebles.appendChild(cat_carpinteria);
				//cat_carpinteria//tipo 
				Element cat_carpinteria_tipo = doc.createElement("tipo");
				cat_carpinteria.appendChild(cat_carpinteria_tipo);
				cat_carpinteria_tipo.setTextContent(Estructuras.getListaTipoCarpinteria().getDominioPadre());
				//cat_construccion//id
				Element cat_carpinteria_id = doc.createElement("id");
				cat_carpinteria.appendChild(cat_carpinteria_id);
				cat_carpinteria_id.setTextContent(inmuebleBean.getCarpinteria());
		}
		if(inmuebleBean.getFachada()!=null&&inmuebleBean.getFachada()!=""){
			//cat_fachada
			Element cat_fachada = doc.createElement("cat_fachada");
			datos_inmuebles.appendChild(cat_fachada);
				//cat_construccion//tipo 
				Element cat_fachada_tipo = doc.createElement("tipo");
				cat_fachada.appendChild(cat_fachada_tipo);
				cat_fachada_tipo.setTextContent(Estructuras.getListaTipoFachada().getDominioPadre());
				//cat_construccion//id
				Element cat_fachada_id = doc.createElement("id");
				cat_fachada.appendChild(cat_fachada_id);
				cat_fachada_id.setTextContent(inmuebleBean.getFachada());
		}
		// edificabilidad
		Element edificabilidad = doc.createElement("edificabilidad");
		datos_inmuebles.appendChild(edificabilidad);
		edificabilidad.setTextContent(inmuebleBean.getEdificabilidad()!=null?formateador8.format(inmuebleBean.getEdificabilidad()):Double.toString(0.0));	
		// fachada_desc
		Element fachada_desc=doc.createElement("fachada_desc");
		datos_inmuebles.appendChild(fachada_desc);
		fachada_desc.setTextContent(inmuebleBean.getFachadaDesc());
		//cubierta_desc
		Element cubierta_desc=doc.createElement("cubierta_desc");
		datos_inmuebles.appendChild(cubierta_desc);
		cubierta_desc.setTextContent(inmuebleBean.getCubiertaDesc());

		//carpinteria_desc
		Element carpinteria_desc=doc.createElement("carpinteria_desc");
		datos_inmuebles.appendChild(carpinteria_desc);
		carpinteria_desc.setTextContent(inmuebleBean.getCarpinteriaDesc());
		//tipoconstruccion_desc
		Element tipoconstruccion_desc = doc.createElement("tipoconstruccion_desc");
		datos_inmuebles.appendChild(tipoconstruccion_desc);
		tipoconstruccion_desc.setTextContent(inmuebleBean.getTipoConstruccionDesc());
		// estadoconservacion_desc
		Element estadoconservacion_desc = doc
				.createElement("estadoconservacion_desc");
		datos_inmuebles.appendChild(estadoconservacion_desc);
		estadoconservacion_desc.setTextContent(inmuebleBean.getEstadoConservacionDesc());
		
		// superficie_ocupada_construccion
		Element superficie_ocupada_construccion = doc
				.createElement("superficie_ocupada_construccion");
		datos_inmuebles.appendChild(superficie_ocupada_construccion);
		superficie_ocupada_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieOcupadaConstruccion()));
		// superficie_construida_construccion
		Element superficie_construida_construccion = doc
				.createElement("superficie_construida_construccion");
		datos_inmuebles.appendChild(superficie_construida_construccion);
		superficie_construida_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieConstruidaConstruccion()));
		// superficie_enplanta_construccion
		Element superficie_enplanta_construccion = doc
				.createElement("superficie_enplanta_construccion");
		datos_inmuebles.appendChild(superficie_enplanta_construccion);
		superficie_enplanta_construccion.setTextContent(formateador8.format(inmuebleBean.getSuperficieEnPlantaConstruccion()));

		// valor_adquisicion_suelo
		Element valor_adquisicion_suelo = doc
				.createElement("valor_adquisicion_suelo");
		datos_inmuebles.appendChild(valor_adquisicion_suelo);
		valor_adquisicion_suelo.setTextContent(formateador14.format(inmuebleBean
				.getValorAdquisicionSuelo()));
		// valor_catastral_suelo
		Element valor_catastral_suelo = doc
				.createElement("valor_catastral_suelo");
		datos_inmuebles.appendChild(valor_catastral_suelo);
		valor_catastral_suelo.setTextContent(formateador14.format(inmuebleBean
				.getValorCatastralSuelo()));
		// valor_actual_suelo
		Element valor_actual_suelo = doc.createElement("valor_actual_suelo");
		datos_inmuebles.appendChild(valor_actual_suelo);
		valor_actual_suelo.setTextContent(formateador14.format(inmuebleBean
				.getValorActualSuelo()));

		// valor_adquisicion_construccion
		Element valor_adquisicion_construccion = doc
				.createElement("valor_adquisicion_construccion");
		datos_inmuebles.appendChild(valor_adquisicion_construccion);
		valor_adquisicion_construccion.setTextContent(formateador14.format(inmuebleBean.getValorAdquisicionConstruccion()));
		// valor_catastral_construccion
		Element valor_catastral_construccion = doc
				.createElement("valor_catastral_construccion");
		datos_inmuebles.appendChild(valor_catastral_construccion);
		valor_catastral_construccion.setTextContent(formateador14.format(inmuebleBean.getValorCatastralConstruccion()));
		// valor_actual_construccion
		Element valor_actual_construccion = doc
				.createElement("valor_actual_construccion");
		datos_inmuebles.appendChild(valor_actual_construccion);
		valor_actual_construccion.setTextContent(formateador14.format(inmuebleBean
				.getValorActualConstruccion()));

		// valor_adquisicion_inmueble
		Element valor_adquisicion_inmueble = doc
				.createElement("valor_adquisicion_inmueble");
		datos_inmuebles.appendChild(valor_adquisicion_inmueble);
		valor_adquisicion_inmueble.setTextContent(formateador14.format(inmuebleBean
				.getValorAdquisicionInmueble()));
		// valor_catastral_inmueble
		Element valor_catastral_inmueble = doc
				.createElement("valor_catastral_inmueble");
		datos_inmuebles.appendChild(valor_catastral_inmueble);
		valor_catastral_inmueble.setTextContent(formateador14.format(inmuebleBean
				.getValorCatastralInmueble()));
		// valor_actual_inmueble
		Element valor_actual_inmueble = doc
				.createElement("valor_actual_inmueble");
		datos_inmuebles.appendChild(valor_actual_inmueble);
		valor_actual_inmueble.setTextContent(formateador14.format(inmuebleBean
				.getValorActualInmueble()));
		// numero_orden
		Element numero_orden = doc.createElement("numero_orden");
		datos_inmuebles.appendChild(numero_orden);
		numero_orden.setTextContent(inmuebleBean.getNumeroOrden());
		// numero_propiedad
		Element numero_propiedad = doc.createElement("numero_propiedad");
		datos_inmuebles.appendChild(numero_propiedad);
		numero_propiedad.setTextContent(inmuebleBean.getNumeroPropiedad());
		// anio_valor_catastral
		Element anio_valor_catastral = doc
				.createElement("anio_valor_catastral");
		datos_inmuebles.appendChild(anio_valor_catastral);
		anio_valor_catastral.setTextContent(inmuebleBean
				.getAnioValorCatastral()!=null?Integer.toString(inmuebleBean
				.getAnioValorCatastral()):Integer.toString(0));
		
		Element edificabilidad_descripcion = doc.createElement("edificabilidad_descripcion");
		datos_inmuebles.appendChild(edificabilidad_descripcion);
		edificabilidad_descripcion.setTextContent(inmuebleBean.getEdificabilidadDesc());
		
		Element fecha_adquisicion_obra = doc.createElement("fecha_adquisicion_obra");
		datos_inmuebles.appendChild(fecha_adquisicion_obra);
		fecha_adquisicion_obra.setTextContent(dateToDDMMYYY(inmuebleBean.getFechaAdquisicionObra()));
		
//		Collection<Observacion> observacionesAVC = getObservacionesFiltradas(
//				inmuebleBean.getObservaciones(),
//				GeopistaUtil.i18n_getname("inventario.loadinventario.anio_valor_catastral"));
//		insertarObservacion(observacionesAVC, datos_inmuebles,"anio_valor_catastral");
		
//		Collection<Observacion> observacionesED = getObservacionesFiltradas(
//				inmuebleBean.getObservaciones(),
//				GeopistaUtil.i18n_getname("inventario.loadinventario.edificabilidad_descripcion"));
//		insertarObservacion(observacionesED, datos_inmuebles,"edificabilidad_descripcion");
		

	}

	/**
	 * Exporta los datos de Inmueble rustico
	 * 
	 * @param inmueble
	 * @param datos
	 * @return
	 */

	private void insertarDatosInmuebleRustico(InmuebleBean inmuebleBean,
			Element datos) {

		//datos_rusticos
		Element datos_rusticos=doc.createElement("datos_rusticos");
		datos.appendChild(datos_rusticos);	
		
		InmuebleRusticoBean rustico = inmuebleBean.getInmuebleRustico();
		
		//poligono
		Element poligono=doc.createElement("poligono");
		datos_rusticos.appendChild(poligono);
		poligono.setTextContent(rustico.getPoligono());
		
		//subparcela
		Element subparcela=doc.createElement("subparcela");
		datos_rusticos.appendChild(subparcela);
		subparcela.setTextContent(rustico.getSubparcela());
		
		//paraje
		Element paraje=doc.createElement("paraje");
		datos_rusticos.appendChild(paraje);
		paraje.setTextContent(rustico.getParaje());
		
		//parcela
		Element parcela=doc.createElement("parcela");
		datos_rusticos.appendChild(parcela);
		parcela.setTextContent(rustico.getParcela());

		if(rustico.getAprovechamiento()!=null&&rustico.getAprovechamiento()!=""){
			//cat_aprovechamiento
			Element cat_aprovechamiento=doc.createElement("cat_aprovechamiento");
			datos_rusticos.appendChild(cat_aprovechamiento);
				//cat_aprovechamiento//tipo 
				Element cat_aprovechamiento_tipo=doc.createElement("tipo");
				cat_aprovechamiento.appendChild(cat_aprovechamiento_tipo);
				cat_aprovechamiento_tipo.setTextContent(Estructuras.getListaAprovechamiento().getDominioPadre());
				//cat_aprovechamiento//id
				Element cat_aprovechamiento_id=doc.createElement("id");
				cat_aprovechamiento.appendChild(cat_aprovechamiento_id);
				cat_aprovechamiento_id.setTextContent(rustico.getAprovechamiento());
		}

		// cat_clase_rust se encuentra en el xml pero no en la base de datos
		Element cat_clase_rust=doc.createElement("cat_clase_rust");
		datos_rusticos.appendChild(cat_clase_rust);
			//cat_clase_urb//tipo 
			Element cat_clase_rust_tipo=doc.createElement("tipo");
			cat_clase_rust.appendChild(cat_clase_rust_tipo);
			cat_clase_rust_tipo.setTextContent(Estructuras.getListaClaseRustica().getDominioPadre());
			//cat_clase_rust//id
			Element cat_clase_rust_id=doc.createElement("id");
			cat_clase_rust.appendChild(cat_clase_rust_id);
			cat_clase_rust_id.setTextContent(inmuebleBean.getTipo());


			Collection<Observacion> observacionesUso = getObservacionesFiltradas(
					inmuebleBean.getObservaciones(),
					GeopistaUtil.i18n_getname("inventario.loadinventario.cultivo"));
			insertarObservacion(observacionesUso, datos_rusticos, "cultivo");
			
			Collection<Observacion> observacionesZona = getObservacionesFiltradas(
					inmuebleBean.getObservaciones(),
					GeopistaUtil.i18n_getname("inventario.loadinventario.zona"));
			
			insertarObservacion(observacionesZona, datos_rusticos,"zona");

	}

	/**
	 * Actualiza los datos de Inmueble urbano
	 * 
	 * @param bienBean
	 * @param datos
	 */

	private void insertarDatosInmuebleUrbano(InmuebleBean inmuebleBean,
			Element datos){

		// datos_urbanos
		Element datos_urbanos = doc.createElement("datos_urbanos");
		datos.appendChild(datos_urbanos);

		InmuebleUrbanoBean urbano = inmuebleBean.getInmuebleUrbano();
		// parcela
		Element parcela = doc.createElement("parcela");
		datos_urbanos.appendChild(parcela);
		parcela.setTextContent(urbano.getParcela());
		//
		Element manzana = doc.createElement("manzana");
		datos_urbanos.appendChild(manzana);
		manzana.setTextContent(urbano.getManzana());

		// cat_clase_urb
		Element cat_clase_urb = doc.createElement("cat_clase_urb");
		datos_urbanos.appendChild(cat_clase_urb);

			Element cat_clase_urb_tipo = doc.createElement("tipo");
			cat_clase_urb.appendChild(cat_clase_urb_tipo);
			cat_clase_urb_tipo.setTextContent(Estructuras.getListaClaseUrbana().getDominioPadre());
	
			Element cat_clase_urb_id = doc.createElement("id");
			cat_clase_urb.appendChild(cat_clase_urb_id);
			cat_clase_urb_id.setTextContent(inmuebleBean.getTipo());

		Collection<Observacion> observacionesUso = getObservacionesFiltradas(
				inmuebleBean.getObservaciones(),
				GeopistaUtil.i18n_getname("inventario.datosGenerales.tag7"));
		insertarObservacion(observacionesUso, datos_urbanos, "uso");
	}

	/**
	 * Almacena los datos de Seguro
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */

	private void saveSeguro(Seguro seguro, Element elementoPatrimonio) {

		if(seguro!=null){
			Element datos_seguros = doc.createElement("datos_seguros");
			elementoPatrimonio.appendChild(datos_seguros);
	
			Element nombre = doc.createElement("nombre");
			datos_seguros.appendChild(nombre);
			nombre.setTextContent(seguro.getCompannia().getNombre());
	
			Element descripcion = doc.createElement("descripcion");
			datos_seguros.appendChild(descripcion);
			descripcion.setTextContent(seguro.getCompannia().getDescripcion());
	
			Element prima = doc.createElement("prima");
			datos_seguros.appendChild(prima);
			prima.setTextContent(seguro.getPrima()!=null?formateador12.format(seguro.getPrima()):Double.toString(0.0));
	
			Element poliza = doc.createElement("poliza");
			datos_seguros.appendChild(poliza);
			poliza.setTextContent(seguro.getPoliza()!=null?formateador12.format(seguro.getPoliza()):Double.toString(0.0));
	
			Element fecha_inicio = doc.createElement("fecha_inicio");
			datos_seguros.appendChild(fecha_inicio);
			fecha_inicio.setTextContent(dateToDDMMYYY(seguro
					.getFechaInicio()));
			
			Element fecha_vencimiento = doc.createElement("fecha_vencimiento");
			datos_seguros.appendChild(fecha_vencimiento);
			fecha_vencimiento.setTextContent(dateToDDMMYYY(seguro
					.getFechaVencimiento()));
			
			Element descripcion_seguro = doc.createElement("descripcion_seguro");
			datos_seguros.appendChild(descripcion_seguro);
			descripcion_seguro.setTextContent(seguro.getDescripcion());

		}
	}

	private void saveObservaciones(Collection observaciones, Element datos) {
		if(observaciones!=null){
			for (Object observacion : observaciones) {
				
				Element datos_observaciones	=doc.createElement("datos_observaciones");			
				datos.appendChild(datos_observaciones);			
					//datos_observaciones_descripcion
					Element datos_observaciones_descripcion	=doc.createElement("descripcion");
					datos_observaciones.appendChild(datos_observaciones_descripcion);	
					datos_observaciones_descripcion.setTextContent(((Observacion) observacion).getDescripcion());
					//datos_observaciones_fecha
					Element datos_observaciones_fecha=doc.createElement("fecha");
					datos_observaciones.appendChild(datos_observaciones_fecha);	
					datos_observaciones_fecha.setTextContent(dateToDDMMYYY(((Observacion) observacion).getFecha()));	
					//datos_observaciones_fecha
					Element fecha_ultima_modificacion=doc.createElement("fecha_ultima_modificacion");
					datos_observaciones.appendChild(fecha_ultima_modificacion);	
					fecha_ultima_modificacion.setTextContent(dateToDDMMYYY(((Observacion) observacion).getFechaUltimaModificacion()));	
			
			}	
		}
	}

	/**
	 * Exporta los datos de Semoviente
	 * 
	 * @param semoviente
	 * @param datos
	 * @return
	 */

	private void insertarDatosSemoviente(SemovienteBean semoviente, Element datos) {
		
		//datos_semovientes
		Element datos_semovientes = doc.createElement("datos_semovientes");
		datos.appendChild(datos_semovientes);
		
		//cat_propiedad
		Element cat_propiedad = doc.createElement("cat_propiedad");
		datos_semovientes.appendChild(cat_propiedad);
			//cat_propiedad//tipo
			Element cat_propiedad_tipo = doc.createElement("tipo");
			cat_propiedad.appendChild(cat_propiedad_tipo);
			cat_propiedad_tipo.setTextContent(Estructuras.getListaPropiedadPatrimonial().getDominioPadre());
			//cat_propiedad//id
			Element cat_propiedad_id = doc.createElement("id");
			cat_propiedad.appendChild(cat_propiedad_id);
			cat_propiedad_id.setTextContent(semoviente.getPropiedad());
			
		//cat_clase
		Element cat_clase = doc.createElement("cat_clase");
		datos_semovientes.appendChild(cat_clase);
			//cat_propiedad//tipo
			Element cat_clase_tipo = doc.createElement("tipo");
			cat_clase.appendChild(cat_clase_tipo);
			cat_clase_tipo.setTextContent(Estructuras.getListaRazaSemoviente().getDominioPadre());
			//cat_propiedad//id
			Element cat_clase_id = doc.createElement("id");
			cat_clase.appendChild(cat_clase_id);
			cat_clase_id.setTextContent(semoviente.getRaza());
		
		Collection<Observacion> raza = getObservacionesFiltradas(
				semoviente.getObservaciones(),
				GeopistaUtil.i18n_getname("inventario.loadinventario.raza"));
		insertarObservacion(raza, datos_semovientes,"raza");
		
		//cat_crec_desarrollo
		Element cat_crec_desarrollo = doc.createElement("cat_crec_desarrollo");
		datos_semovientes.appendChild(cat_crec_desarrollo);
			//cat_est_cons//tipo
			Element cat_crec_desarrollo_tipo = doc.createElement("tipo");
			cat_crec_desarrollo.appendChild(cat_crec_desarrollo_tipo);
			cat_crec_desarrollo_tipo.setTextContent(Estructuras.getListaCrecDesarrollo().getDominioPadre());
			//cat_crec_desarrollo//id
			Element cat_crec_desarrollo_id = doc.createElement("id");
			cat_crec_desarrollo.appendChild(cat_crec_desarrollo_id);
			Collection<Observacion> etapaDesarrollo=getObservacionesFiltradas(semoviente.getObservaciones(), GeopistaUtil
											.i18n_getname("inventario.loadinventario.cat_crec_desarrollo"));
		insertarObservacion(etapaDesarrollo, cat_crec_desarrollo_id,"id");
		//cat_est_cons
		Element cat_est_cons = doc.createElement("cat_est_cons");
		datos_semovientes.appendChild(cat_est_cons);
			//cat_est_cons//tipo
			Element cat_est_cons_tipo = doc.createElement("tipo");
			cat_est_cons.appendChild(cat_est_cons_tipo);
			cat_est_cons_tipo.setTextContent(Estructuras.getListaEstadoConservacion().getDominioPadre());
			//cat_est_cons//id
			Element cat_est_cons_id = doc.createElement("id");
			cat_est_cons.appendChild(cat_est_cons_id);
			cat_est_cons_id.setTextContent(semoviente.getConservacion());
		
		//identificacion
		Element identificacion = doc.createElement("identificacion");
		datos_semovientes.appendChild(identificacion);
		identificacion.setTextContent(semoviente.getIdentificacion());
		//especie
		Element especie = doc.createElement("especie");
		datos_semovientes.appendChild(especie);
		especie.setTextContent(semoviente.getEspecie());
		//descripcion
		Element descripcion = doc.createElement("descripcion");
		datos_semovientes.appendChild(descripcion);
		descripcion.setTextContent(semoviente.getDescripcion());
		//destino
		Element destino = doc.createElement("destino");
		datos_semovientes.appendChild(destino);
		destino.setTextContent(semoviente.getDestino());
		//cantidad
		Element cantidad= doc.createElement("cantidad");
		datos_semovientes.appendChild(cantidad);
		cantidad.setTextContent(semoviente.getCantidad()!=null?Long.toString(semoviente.getCantidad()):Long.toString(0));
		//coste_adquisicion
		Element coste_adquisicion = doc.createElement("coste_adquisicion");
		datos_semovientes.appendChild(coste_adquisicion);
		coste_adquisicion.setTextContent(semoviente.getCosteAdquisicion()!=null?formateador14.format(semoviente.getCosteAdquisicion()):Double.toString(0.0));
		//valor_actual
		Element valor_actual = doc.createElement("valor_actual");
		datos_semovientes.appendChild(valor_actual);
		valor_actual.setTextContent(semoviente.getValorActual()!=null?formateador14.format(semoviente.getValorActual()):Double.toString(0.0));
		//fecha_nacimiento
		Element fecha_nacimiento = doc.createElement("fecha_nacimiento");
		datos_semovientes.appendChild(fecha_nacimiento);
		fecha_nacimiento.setTextContent(dateToDDMMYYY(semoviente.getFechaNacimiento()));
	
	}
//
	/**
	 * Almacena los datos de Valor Mobiliario
	 * 
	 * @param mobiliario
	 * @param datos
	 */

	private void insertarDatosValorMobiliario(ValorMobiliarioBean mobiliario,
			Element datos) {
		
		//datos_mobiliario
		Element datos_mobiliario = doc.createElement("datos_mobiliario");
		datos.appendChild(datos_mobiliario);
		
		//depositado_en
		Element depositado_en = doc.createElement("depositado_en");
		datos_mobiliario.appendChild(depositado_en);
		depositado_en.setTextContent(mobiliario.getDepositadoEn());
		//emitido_por
		Element emitido_por = doc.createElement("emitido_por");
		datos_mobiliario.appendChild(emitido_por);
		emitido_por.setTextContent(mobiliario.getEmitidoPor());
		//numero
		Element numero = doc.createElement("numero");
		datos_mobiliario.appendChild(numero);
		numero.setTextContent(mobiliario.getNumero());
		//serie
		Element serie = doc.createElement("serie");
		datos_mobiliario.appendChild(serie);
		serie.setTextContent(mobiliario.getSerie());
		//num_titulos
		Element num_titulos = doc.createElement("num_titulos");
		datos_mobiliario.appendChild(num_titulos);
		num_titulos.setTextContent(mobiliario.getNumTitulos()!=null?Integer.toString(mobiliario.getNumTitulos()):Integer.toString(0));
		//destino
		Element destino = doc.createElement("destino");
		datos_mobiliario.appendChild(destino);
		destino.setTextContent(mobiliario.getDestino());
		//cat_mobiliario
		Element cat_mobiliario = doc.createElement("cat_mobiliario");
		datos_mobiliario.appendChild(cat_mobiliario);
			// cat_est_cons//tipo 
			Element cat_mobiliario_tipo = doc.createElement("tipo");
			cat_mobiliario.appendChild(cat_mobiliario_tipo);
			cat_mobiliario_tipo.setTextContent(Estructuras.getListaClasesValorMobiliario().getDominioPadre());
			// cat_est_cons//id
			Element cat_mobiliario_id = doc.createElement("id");
			cat_mobiliario.appendChild(cat_mobiliario_id);
			cat_mobiliario_id.setTextContent(mobiliario.getTipo());
	
		//coste_adquisicion
		Element coste_adquisicion = doc.createElement("coste_adquisicion");
		datos_mobiliario.appendChild(coste_adquisicion);
		coste_adquisicion.setTextContent(mobiliario.getCosteAdquisicion()!=null?formateador14.format(mobiliario.getCosteAdquisicion()):Double.toString(0.0));
		//valor_actual
		Element valor_actual = doc.createElement("valor_actual");
		datos_mobiliario.appendChild(valor_actual);
		valor_actual.setTextContent(mobiliario.getValorActual()!=null?formateador14.format(mobiliario.getValorActual()):Double.toString(0.0));
		//precio
		Element precio = doc.createElement("precio");
		datos_mobiliario.appendChild(precio);
		precio.setTextContent(mobiliario.getPrecio()!=null?formateador14.format(mobiliario.getPrecio()):Double.toString(0.0));
		//capital
		Element capital = doc.createElement("capital");
		datos_mobiliario.appendChild(capital);
		capital.setTextContent(mobiliario.getCapital()!=null?formateador14.format(mobiliario.getCapital()):Double.toString(0.0));
		//fecha_acuerdo
		Element fecha_acuerdo= doc.createElement("fecha_acuerdo");
		datos_mobiliario.appendChild(fecha_acuerdo);
		fecha_acuerdo.setTextContent(dateToDDMMYYY(mobiliario.getFechaAcuerdo()));
		
	}
//
	/**
	 * Almacena los datos de Vehiculo
	 * 
	 * @param vehiculo
	 * @param datos
	 */

	private void insertarDatosVehiculo(VehiculoBean vehiculo, Element datos)
			{


		//datos_vehiculo
		Element datos_vehiculo = doc.createElement("datos_vehiculo");
		datos.appendChild(datos_vehiculo);
		//matricula_vieja
		Element matricula_vieja = doc.createElement("matricula_vieja");
		datos_vehiculo .appendChild(matricula_vieja);
		matricula_vieja.setTextContent(vehiculo.getMatriculaVieja());
		//matricula_nueva
		Element matricula_nueva= doc.createElement("matricula_nueva");
		datos_vehiculo .appendChild(matricula_nueva);
		matricula_nueva.setTextContent(vehiculo.getMatriculaNueva());
		//num_bastidor
		Element num_bastidor = doc.createElement("num_bastidor");
		datos_vehiculo .appendChild(num_bastidor);
		num_bastidor.setTextContent(vehiculo.getNumBastidor());
		//marca
		Element marca = doc.createElement("marca");
		datos_vehiculo .appendChild(marca);
		marca.setTextContent(vehiculo.getMarca());
		//motor
		Element motor = doc.createElement("motor");
		datos_vehiculo .appendChild(motor);
		motor.setTextContent(vehiculo.getMotor());
		//fuerza
		Element fuerza = doc.createElement("fuerza");
		datos_vehiculo .appendChild(fuerza);
		fuerza.setTextContent(vehiculo.getFuerza());
		//servicio
		Element servicio = doc.createElement("servicio");
		datos_vehiculo .appendChild(servicio);
		servicio.setTextContent(vehiculo.getServicio());
		//destino
		Element destino = doc.createElement("destino");
		datos_vehiculo .appendChild(destino);
		destino.setTextContent(vehiculo.getDestino());

		if(vehiculo.getTipoVehiculo()!=null&&vehiculo.getTipoVehiculo()!=""){
			// cat_vehiculo
			Element cat_vehiculo = doc.createElement("cat_vehiculo");
			datos_vehiculo.appendChild(cat_vehiculo);
				// cat_vehiculo//tipo
				Element cat_vehiculo_tipo = doc.createElement("tipo");
				cat_vehiculo.appendChild(cat_vehiculo_tipo);
				cat_vehiculo_tipo.setTextContent(Estructuras.getListaTiposVehiculo().getDominioPadre());
				// cat_vehiculo//id
				Element cat_vehiculo_id = doc.createElement("id");
				cat_vehiculo.appendChild(cat_vehiculo_id);
				cat_vehiculo_id.setTextContent(vehiculo.getTipoVehiculo());
		}
		if(vehiculo.getEstadoConservacion()!=null&&vehiculo.getEstadoConservacion()!=""){
		//cat_est_cons
		Element cat_est_cons = doc.createElement("cat_est_cons");
		datos_vehiculo.appendChild(cat_est_cons);
			//cat_est_cons//tipo
			Element cat_est_cons_tipo = doc.createElement("tipo");
			cat_est_cons.appendChild(cat_est_cons_tipo);
			cat_est_cons_tipo.setTextContent(Estructuras.getListaEstadoConservacion().getDominioPadre());
			//cat_est_cons//id
			Element cat_est_cons_id = doc.createElement("id");
			cat_est_cons.appendChild(cat_est_cons_id);
			cat_est_cons_id.setTextContent(vehiculo.getEstadoConservacion());
		}
		if (vehiculo.getTraccion()!=null&&vehiculo.getTraccion()!="") {
			
			//cat_traccion
			Element cat_traccion = doc.createElement("cat_traccion");
			datos_vehiculo.appendChild(cat_traccion);
				//cat_traccion//tipo
				Element cat_traccion_tipo = doc.createElement("tipo");
				cat_traccion.appendChild(cat_traccion_tipo);
				cat_traccion_tipo.setTextContent(Estructuras.getListaTraccion().getDominioPadre());
				//cat_traccion//id
				Element cat_traccion_id = doc.createElement("id");
				cat_traccion.appendChild(cat_traccion_id);
				cat_traccion_id.setTextContent(vehiculo.getTraccion());
		}
		if (vehiculo.getPropiedad()!=null&&vehiculo.getPropiedad()!="") {
			
			//cat_propiedad
			Element cat_propiedad = doc.createElement("cat_propiedad");
			datos_vehiculo.appendChild(cat_propiedad);
				//cat_propiedad//tipo
				Element cat_propiedad_tipo = doc.createElement("tipo");
				cat_propiedad.appendChild(cat_propiedad_tipo);
				cat_propiedad_tipo.setTextContent(Estructuras.getListaPropiedadPatrimonial().getDominioPadre());
				//cat_propiedad//id
				Element cat_propiedad_id = doc.createElement("id");
				cat_propiedad.appendChild(cat_propiedad_id);
				cat_propiedad_id.setTextContent(vehiculo.getPropiedad());
		}	
		//coste_adquisicion
		Element coste_adquisicion = doc.createElement("coste_adquisicion");
		datos_vehiculo .appendChild(coste_adquisicion);
		coste_adquisicion.setTextContent(vehiculo.getCosteAdquisicion()!=null?formateador14.format(vehiculo.getCosteAdquisicion()):Double.toString(0.0));
		//valor_actual
		Element valor_actual = doc.createElement("valor_actual");
		datos_vehiculo .appendChild(valor_actual);
		valor_actual.setTextContent(vehiculo.getValorActual()!=null?formateador14.format(vehiculo.getValorActual()):Double.toString(0.0));
		saveObservaciones(vehiculo.getObservaciones(),datos);

	}
	/**
	 * Almacena los datos de Via
	 * 
	 * @param via
	 * @param datos
	 */

	private void insertarDatosVia(ViaBean via, Element datos) {
		
		// datos_inmuebles
		Element datos_via = doc.createElement("datos_via");
		datos.appendChild(datos_via);

		// categoria
		Element categoria = doc.createElement("categoria");
		datos_via.appendChild(categoria);
		categoria.setTextContent(via.getCategoria());
		// codigo
		Element codigo = doc.createElement("codigo");
		datos_via.appendChild(codigo);
		codigo.setTextContent(via.getCodigo());
		//nombre
		Element nombre = doc.createElement("nombre");
		datos_via.appendChild(nombre);
		nombre.setTextContent(via.getNombreVia());
		//inicio
		Element inicio= doc.createElement("inicio");
		datos_via.appendChild(inicio);
		inicio.setTextContent(via.getInicioVia());
		//fin
		Element fin = doc.createElement("fin");
		datos_via.appendChild(fin);
		fin.setTextContent(via.getFinVia());
		//destino
		Element destino = doc.createElement("destino");
		datos_via.appendChild(destino);
		destino.setTextContent(via.getDestino());
		//num_apliques
		Element num_apliques = doc.createElement("num_apliques");
		datos_via.appendChild(num_apliques);
		num_apliques.setTextContent(Long.toString(via.getNumApliques()));
		//num_bancos
		Element num_bancos = doc.createElement("num_bancos");
		datos_via.appendChild(num_bancos);
		num_bancos.setTextContent(Long.toString(via.getNumBancos()));
		//num_papeleras
		Element num_papeleras = doc.createElement("num_papeleras");
		datos_via.appendChild(num_papeleras);
		num_papeleras.setTextContent(Long.toString(via.getNumPapeleras()));
		//metros_pavimentados
		Element metros_pavimentados = doc.createElement("metros_pavimentados");
		datos_via.appendChild(metros_pavimentados);
		metros_pavimentados.setTextContent(formateador14.format(via.getMetrosPavimentados()));
		//metros_no_pavimentados
		Element metros_no_pavimentados = doc.createElement("metros_no_pavimentados");
		datos_via.appendChild(metros_no_pavimentados);
		metros_no_pavimentados.setTextContent(formateador14.format(via.getMetrosNoPavimentados()));
		//zonas_verdes
		Element zonas_verdes = doc.createElement("zonas_verdes");
		datos_via.appendChild(zonas_verdes);
		zonas_verdes.setTextContent(formateador14.format(via.getZonasVerdes()));
		//longitud
		Element longitud = doc.createElement("longitud");
		datos_via.appendChild(longitud);
		longitud.setTextContent(formateador14.format(via.getLongitud()));
		//ancho
		Element ancho = doc.createElement("ancho");
		datos_via.appendChild(ancho);
		ancho.setTextContent(formateador14.format(via.getAncho()));
		//valor_actual
		Element valor_actual= doc.createElement("valor_actual");
		datos_via.appendChild(valor_actual);
		valor_actual.setTextContent(formateador14.format(via.getValorActual()));
	}

	/**
	 * Almacena los datos del bien revertible
	 * 
	 * @param bienRevertible
	 * @param datos
	 * @return
	 * 
	 */

	private void insertarDatosBienRevertible(BienRevertible bienRevertible,
			Element datos) {
		// bienes inventario
		Element datos_bienes_revertibles = doc.createElement("datos_bienes_revertibles");
		datos.appendChild(datos_bienes_revertibles);
		
		if(bienRevertible.getClase()!=null&&bienRevertible.getClase()!=""){
			// cat_clase se encuentra en el xml pero no en la pantalla
			Element cat_clase= doc.createElement("cat_clase");
			datos_bienes_revertibles.appendChild(cat_clase);
				//cat_clase//tipo
				Element cat_clase_tipo = doc.createElement("tipo");
				cat_clase.appendChild(cat_clase_tipo);
				cat_clase_tipo.setTextContent(Estructuras.getListaClaseBienRevertible().getDominioPadre());
				//cat_clase//id
				Element cat_clase_id = doc.createElement("id");
				cat_clase.appendChild(cat_clase_id);
				cat_clase_id.setTextContent(bienRevertible.getClase());
		}	
		// numinventario
		Element numInventario = doc.createElement("numinventario");
		datos_bienes_revertibles.appendChild(numInventario);
		numInventario.setTextContent(bienRevertible.getNumInventario());
	
		//listado_bienes
		Element listado_bienes = doc.createElement("listado_bienes");
		datos_bienes_revertibles.appendChild(listado_bienes);
		
		Collection<BienBean>bienes=bienRevertible.getBienes();
		if(bienes!=null){
			for (BienBean bien : bienes) {
				//id_bien_inventario
				Element id_bien_inventario= doc.createElement("id_bien_inventario");
				listado_bienes.appendChild(id_bien_inventario);
				id_bien_inventario.setTextContent(bien.getNumInventario());
			}
		}
		
		//descripcion_bien
		Element descripcion_bien = doc.createElement("descripcion_bien");
		datos_bienes_revertibles.appendChild(descripcion_bien);
		descripcion_bien.setTextContent(bienRevertible.getDescripcion_bien());
		//fecha_transmision
		Element fecha_transmision = doc.createElement("fecha_transmision");
		datos_bienes_revertibles.appendChild(fecha_transmision);
		fecha_transmision.setTextContent(dateToDDMMYYY(bienRevertible.getFechaTransmision()));
		//fecha_inicio
		Element fecha_inicio = doc.createElement("fecha_inicio");
		datos_bienes_revertibles.appendChild(fecha_inicio);
		fecha_inicio.setTextContent(dateToDDMMYYY(bienRevertible.getFechaInicio()));
		//fecha_vencimiento
		Element fecha_vencimiento= doc.createElement("fecha_vencimiento");
		datos_bienes_revertibles.appendChild(fecha_vencimiento);
		fecha_vencimiento.setTextContent(dateToDDMMYYY(bienRevertible.getFechaVencimiento()));
		//poseedor
		Element poseedor = doc.createElement("poseedor");
		datos_bienes_revertibles.appendChild(poseedor);
		poseedor.setTextContent(bienRevertible.getPoseedor());
		//titulo_posesion
		Element titulo_posesion = doc.createElement("titulo_posesion");
		datos_bienes_revertibles.appendChild(titulo_posesion);
		titulo_posesion.setTextContent(bienRevertible.getTituloPosesion());
		//condiciones_reversion
		Element condiciones_reversion = doc.createElement("condiciones_reversion");
		datos_bienes_revertibles.appendChild(condiciones_reversion);
		condiciones_reversion.setTextContent(bienRevertible.getCondicionesReversion());
		if(bienRevertible.getCatTransmision()!=null&&bienRevertible.getCatTransmision()!="")
		{
			//cat_transmision
			Element cat_transmision= doc.createElement("cat_transmision");
			datos_bienes_revertibles.appendChild(cat_transmision);
				//cat_transmision//tipo
				Element cat_transmision_tipo = doc.createElement("tipo");
				cat_transmision.appendChild(cat_transmision_tipo);
				cat_transmision_tipo.setTextContent(Estructuras.getListaTransmision().getDominioPadre());
				//cat_clase//id
				Element cat_transmision_id = doc.createElement("id");
				cat_transmision.appendChild(cat_transmision_id);
				cat_transmision_id.setTextContent(bienRevertible.getCatTransmision());
		}	
		
		//importe
		Element importe = doc.createElement("importe");
		datos_bienes_revertibles.appendChild(importe);
		importe.setTextContent(bienRevertible.getImporte()!=null?formateador14.format(bienRevertible.getImporte()):Double.toString(0.0));
		
		int numDocumentosBienRevertible=saveListadoDocumentos(bienRevertible,datos_bienes_revertibles);
		resumen.put("bienes_revertibles"+DOCUMENTOS+TOTALES,bienRevertible.getDocumentos()!=null?bienRevertible.getDocumentos().size():0);
		resumen.put("bienes_revertibles"+DOCUMENTOS+PROCESADOS,numDocumentosBienRevertible);
		
		//fecha_alta
		Element fecha_alta= doc.createElement("fecha_alta");
		datos_bienes_revertibles.appendChild(fecha_alta);
		fecha_alta.setTextContent(dateToDDMMYYY(bienRevertible.getFechaAlta()));
		//fecha_baja
		Element fecha_baja = doc.createElement("fecha_baja");
		datos_bienes_revertibles.appendChild(fecha_baja);
		fecha_baja.setTextContent(dateToDDMMYYY(bienRevertible.getFechaBaja()));
		//fecha_ultima_modificacion
		Element fecha_ultima_modificacion = doc.createElement("fecha_ultima_modificacion");
		datos_bienes_revertibles.appendChild(fecha_ultima_modificacion);
		fecha_ultima_modificacion.setTextContent(dateToDDMMYYY(bienRevertible.getFechaUltimaModificacion()));
		//descripcion
		Element descripcion = doc.createElement("descripcion");
		datos_bienes_revertibles.appendChild(descripcion);
		descripcion.setTextContent(bienRevertible.getDetalles());
		//nombre
		Element nombre = doc.createElement("nombre");
		datos_bienes_revertibles.appendChild(nombre);
		nombre.setTextContent(bienRevertible.getNombre());
		//organizacion
		Element organizacion = doc.createElement("organizacion");
		datos_bienes_revertibles.appendChild(organizacion);
		organizacion.setTextContent(bienRevertible.getOrganizacion());
		//patrimonio_municipal_suelo
		Element patrimonio_municipal_suelo = doc.createElement("patrimonio_municipal_suelo");
		datos_bienes_revertibles.appendChild(patrimonio_municipal_suelo);
		patrimonio_municipal_suelo.setTextContent(bienRevertible.isPatrimonioMunicipalSuelo()?"1":"0");
		//fecha_adquisicion
		Element fecha_adquisicion = doc.createElement("fecha_adquisicion");
		datos_bienes_revertibles.appendChild(fecha_adquisicion);
		fecha_adquisicion.setTextContent(dateToDDMMYYY(bienRevertible.getFecha_adquisicion()));
		//fecha_aprobacion_pleno
		Element fecha_aprobacion_pleno = doc.createElement("fecha_aprobacion_pleno");
		datos_bienes_revertibles.appendChild(fecha_aprobacion_pleno);
		fecha_aprobacion_pleno.setTextContent(dateToDDMMYYY(bienRevertible.getFecha_aprobacion_pleno()));
		if(bienRevertible.getAdquisicion()!=null&&bienRevertible.getAdquisicion()!=""){
			//cat_adquisicion
			Element cat_adquisicion= doc.createElement("cat_adquisicion");
			datos_bienes_revertibles.appendChild(cat_adquisicion);
				//cat_adquisicion//tipo
				Element cat_adquisicion_tipo = doc.createElement("tipo");
				cat_adquisicion.appendChild(cat_adquisicion_tipo);
				cat_adquisicion_tipo.setTextContent(Estructuras.getListaFormaAdquisicion().getDominioPadre());
				//cat_adquisicion//id
				Element cat_adquisicion_id = doc.createElement("id");
				cat_adquisicion.appendChild( cat_adquisicion_id);
				 cat_adquisicion_id.setTextContent(bienRevertible.getAdquisicion());			
		}
		if(bienRevertible.getDiagnosis()!=null&&bienRevertible.getDiagnosis()!=""){
				// datos_diagnosis
				 Element datos_diagnosis = doc.createElement("datos_diagnosis");
				 datos_bienes_revertibles.appendChild(datos_diagnosis);	
				 	// cat_diagnosis
				 	Element cat_diagnosis = doc.createElement("cat_diagnosis");
				 	datos_diagnosis.appendChild(cat_diagnosis);
				 		// cat_diagnosis//tipo
				 		Element cat_diagnosis_tipo = doc.createElement("tipo");
				 		cat_diagnosis.appendChild(cat_diagnosis_tipo);
				 		cat_diagnosis_tipo.setTextContent(Estructuras.getListaDiagnosis().getDominioPadre());
						//cat_diagnosis//id
				 		Element cat_diagnosis_id = doc.createElement("id");
				 		cat_diagnosis.appendChild(cat_diagnosis_id);
				 		cat_diagnosis_id.setTextContent(bienRevertible.getDiagnosis());
					// descripcion_diagnosis
				 	Collection<Observacion>diagnosisDescription=getObservacionesFiltradas(bienRevertible.getObservaciones(),
				 			GeopistaUtil
				 			.i18n_getname("inventario.loadinventario.descripcion_diagnosis"));
					insertarObservacion(diagnosisDescription, datos_diagnosis, "descripcion_diagnosis");
		}
		Collection<Observacion> obsUltimoInventario=getObservacionesFiltradas(bienRevertible.getObservaciones(),
				GeopistaUtil.i18n_getname("inventario.loadinventario.ultimo_inventario"));
		insertarObservacion(obsUltimoInventario, datos_bienes_revertibles, "ultimo_inventario");
		//revision_actual
		Element revision_actual = doc.createElement("revision_actual");
		datos_bienes_revertibles.appendChild(revision_actual);
		revision_actual.setTextContent(Long.toString(bienRevertible.getRevisionActual()));
		
	}


	/**
	 * Almacena los datos del lote
	 * @param lote
	 * @param datos
	 */

	private void insertarDatosLote(Lote lote, Element datos)
			{

		Element nombre = doc.createElement("nombre");
		datos.appendChild(nombre);
		nombre.setTextContent(lote.getNombre_lote());			

		Element num_lote = doc.createElement("num_lote");
		datos.appendChild(num_lote);
		num_lote.setTextContent(Long.toString(lote.getId_lote()));	
		
		Element fecha_alta = doc.createElement("fecha_alta");
		datos.appendChild(fecha_alta);
		fecha_alta.setTextContent(dateToDDMMYYY(lote.getFecha_alta()));	
		
		Element fecha_baja = doc.createElement("fecha_baja");
		datos.appendChild(fecha_baja);
		fecha_baja.setTextContent(dateToDDMMYYY(lote.getFecha_baja()));

		Element fecha_ultima_modificacion = doc.createElement("fecha_ultima_modificacion");
		datos.appendChild(fecha_ultima_modificacion);
		fecha_ultima_modificacion.setTextContent(dateToDDMMYYY(lote.getFecha_ultima_modificacion()));	

		int numDocumentosLotes=saveListadoDocumentos(lote,datos);
		resumen.put("lotes"+DOCUMENTOS+TOTALES,lote.getDocumentos()!=null?lote.getDocumentos().size():0);
		resumen.put("lotes"+DOCUMENTOS+PROCESADOS,numDocumentosLotes);
		
		Element descripcion = doc.createElement("descripcion");
		datos.appendChild(descripcion);
		descripcion.setTextContent(lote.getDescripcion());	
		
		Element destino = doc.createElement("destino");
		datos.appendChild(destino);
		destino.setTextContent(lote.getDestino());	

		//bienes_lote
		Element listado_bienes = doc.createElement("bienes_lote");
		datos.appendChild(listado_bienes);
		
		Collection<BienBean>bienes=lote.getBienes();		
		if(bienes!=null){
			for (BienBean bien : bienes) {
				//id_bien_inventario
				Element id_bien_inventario= doc.createElement("id_inventario");
				listado_bienes.appendChild(id_bien_inventario);
				id_bien_inventario.setTextContent(bien.getNumInventario());
			}
		}

	}

	/***
	 * saveDatosRegisto
	 */
	private void saveDatosRegistro(RegistroBean registro,
			Element elementoPatrimonio) {

		if(registro!=null){
			Element datos_registro = doc.createElement("datos_registro");
			elementoPatrimonio.appendChild(datos_registro);
	
			Element registro_tomo = doc.createElement("registro_tomo");
			datos_registro.appendChild(registro_tomo);
			registro_tomo.setTextContent(registro.getTomo());
			
			Element registro_folio = doc.createElement("registro_folio");
			datos_registro.appendChild(registro_folio);
			registro_folio.setTextContent(registro.getFolio());
			
			Element registro_libro = doc.createElement("registro_libro");
			datos_registro.appendChild(registro_libro);
			registro_libro.setTextContent(registro.getLibro());
			
			Element registro_finca = doc.createElement("registro_finca");
			datos_registro.appendChild(registro_finca);
			registro_finca.setTextContent(registro.getFinca());
			
			Element registro_inscripcion = doc.createElement("registro_inscripcion");
			datos_registro.appendChild(registro_inscripcion);
			registro_inscripcion.setTextContent(registro.getInscripcion());
	
			Element registro_protocolo = doc.createElement("registro_protocolo");
			datos_registro.appendChild(registro_protocolo);
			registro_protocolo.setTextContent(registro.getProtocolo());
			
			Element registro_notario = doc.createElement("registro_notario");
			datos_registro.appendChild(registro_notario);
			registro_notario.setTextContent(registro.getNotario());
	
			Element registro_propiedad = doc.createElement("registro_propiedad");
			datos_registro.appendChild(registro_propiedad);
			registro_propiedad.setTextContent(registro.getProtocolo());
			

		}
	}

	/**
	 * Almacena las mejoras de un bien
	 * 
	 * @param mejoras
	 * @param elementoPatrimonio
	 */
	private void insertarMejoras(Collection<Mejora> mejoras, Element elementoPatrimonio) {

		if(mejoras!=null){
			Element datos_mejoras = doc.createElement("datos_mejoras");
			elementoPatrimonio.appendChild(datos_mejoras);
	
			for (Mejora mejora : mejoras) {
				Element descripcion = doc.createElement("descripcion");
				datos_mejoras.appendChild(descripcion);
				descripcion.setTextContent(mejora.getDescripcion());
	
				Element fecha = doc.createElement("fecha");
				datos_mejoras.appendChild(fecha);
				fecha.setTextContent(dateToDDMMYYY(mejora.getFechaEntrada()));
	
				Element fecha_ejecucion = doc.createElement("fecha_ejecucion");
				datos_mejoras.appendChild(fecha_ejecucion);
				fecha_ejecucion.setTextContent(dateToDDMMYYY(mejora.getFechaEjecucion()));
	
				Element fecha_ultima_modificacion = doc
						.createElement("fecha_ultima_modificacion");
				datos_mejoras.appendChild(fecha_ultima_modificacion);
				fecha_ultima_modificacion.setTextContent(dateToDDMMYYY(mejora.getFechaUltimaModificacion()));
	
				Element importe = doc.createElement("importe");
				datos_mejoras.appendChild(importe);
				importe.setTextContent(formateador14.format(mejora.getImporte()));
	
			}
		}

	}

	/**
	 * Exporta los datos de tipo mueble
	 * 
	 * @param bienBean
	 * @param datos
	 * @return
	 */
	private void insertarDatosMueble(MuebleBean muebleBean, Element elementoPatrimonio)
			{
		
		// datos_inmuebles
		Element datos_mueble = doc.createElement("datos_muebles");
		elementoPatrimonio.appendChild(datos_mueble);
		if(muebleBean.getTipo()!=null&&muebleBean.getTipo()!=""){
			// cat_clase se encuentra en el xml pero no en la base de datos
			 Element cat_clase = doc.createElement("cat_clase");
			 datos_mueble.appendChild(cat_clase);
			 	//cat_clase_der//tipo
			 	Element cat_clase_tipo=doc.createElement("tipo");
			 	cat_clase.appendChild(cat_clase_tipo);
			 	cat_clase_tipo.setTextContent(Estructuras.getListaClaseMuebles().getDominioPadre());
				//cat_clase_rust//id
			 	Element cat_clase_der_id=doc.createElement("id");
			 	cat_clase.appendChild(cat_clase_der_id);
			 	cat_clase_der_id.setTextContent(muebleBean.getTipo());
		} 	
		if(muebleBean.getEstadoConservacion()!=null&&muebleBean.getEstadoConservacion()!=""){
			// cat_est_cons
			Element cat_est_cons = doc.createElement("cat_est_cons");
			datos_mueble.appendChild(cat_est_cons);
				// cat_est_cons//tipo Estado de conservación
				Element cat_est_cons_tipo = doc.createElement("tipo");
				cat_est_cons.appendChild(cat_est_cons_tipo);
				cat_est_cons_tipo.setTextContent(Estructuras.getListaEstadoConservacion().getDominioPadre());
				// cat_est_cons//id
				Element cat_est_cons_id = doc.createElement("id");
				cat_est_cons.appendChild(cat_est_cons_id);
				cat_est_cons_id.setTextContent(muebleBean.getEstadoConservacion());
		}
		//caracteristicas
		Element caracteristicas = doc.createElement("caracteristicas");
		datos_mueble.appendChild(caracteristicas);
		caracteristicas.setTextContent(muebleBean.getCaracteristicas());
		//coste_adquisicion
		Element coste_adquisicion = doc.createElement("coste_adquisicion");
		datos_mueble.appendChild(coste_adquisicion);
		coste_adquisicion.setTextContent(muebleBean.getCosteAdquisicion()!=null?formateador14.format(muebleBean.getCosteAdquisicion()):Double.toString(0.0));
		//destino
		Element destino= doc.createElement("destino");
		datos_mueble.appendChild(destino);
		destino.setTextContent(muebleBean.getDestino());
		//fecha_fin_garantia
		Element fecha_fin_garantia = doc.createElement("fecha_fin_garantia");
		datos_mueble.appendChild(fecha_fin_garantia);
		fecha_fin_garantia.setTextContent(dateToDDMMYYY(muebleBean.getFechaFinGarantia()));
		//caracteristicas
		Element num_serie = doc.createElement("num_serie");
		datos_mueble.appendChild(num_serie);
		num_serie.setTextContent(muebleBean.getNumSerie());
		//caracteristicas
		Element marca = doc.createElement("marca");
		datos_mueble.appendChild(marca);
		marca.setTextContent(muebleBean.getMarca());
		//caracteristicas
		Element modelo = doc.createElement("modelo");
		datos_mueble.appendChild(modelo);
		modelo.setTextContent(muebleBean.getModelo());
		//direccion
		Element direccion = doc.createElement("direccion");
		datos_mueble.appendChild(direccion);
		direccion.setTextContent(muebleBean.getDireccion());
		//caracteristicas
		Element ubicacion= doc.createElement("ubicacion");
		datos_mueble.appendChild(ubicacion);
		ubicacion.setTextContent(muebleBean.getUbicacion());
		//valor_actual
		Element valor_actual = doc.createElement("valor_actual");
		datos_mueble.appendChild(valor_actual);
		valor_actual.setTextContent(muebleBean.getValorActual()!=null?formateador14.format(muebleBean.getValorActual()):Double.toString(0.0));
		//material
		Element material= doc.createElement("material");
		datos_mueble.appendChild(material);
		material.setTextContent(muebleBean.getMaterial());
		//caracteristicas
		Element autor = doc.createElement("autor");
		datos_mueble.appendChild(autor);
		autor.setTextContent(muebleBean.getAutor());
		if(muebleBean.getPropiedad()!=null&&muebleBean.getPropiedad()!=""){
			if(muebleBean.getPropiedad()!=null&&muebleBean.getPropiedad()!=""){
				// cat_propiedad
				Element cat_propiedad = doc.createElement("cat_propiedad");
				datos_mueble.appendChild(cat_propiedad);
					//cat_propiedad//tipo
					Element cat_propiedad_tipo = doc.createElement("tipo");
					cat_propiedad.appendChild(cat_propiedad_tipo);
					cat_propiedad_tipo.setTextContent(Estructuras.getListaPropiedadPatrimonial().getDominioPadre());
					//cat_propiedad//id
					Element cat_propiedad_id = doc.createElement("id");
					cat_propiedad.appendChild(cat_propiedad_id);
					cat_propiedad_id.setTextContent(muebleBean.getPropiedad());
			}
		}

	}

	/**
	 * Almacena Referencias Catastrales
	 * 
	 * @param inmueble
	 * @param elementoPatrimonio
	 */
	private void insertarRefCatastrales(Collection<ReferenciaCatastral> refCatastrales,
			Element elementoPatrimonio) {
		if(refCatastrales!=null){
				for (ReferenciaCatastral refCatastral : refCatastrales) {
		
					Element datos_refcatastrales = doc
							.createElement("datos_refcatastrales");
					elementoPatrimonio.appendChild(datos_refcatastrales);
		
					if (((ReferenciaCatastral) refCatastral).getRefCatastral().length() > 14) {
						logger.debug("Referencia catastral demasiado grande transformada a:"
								+  refCatastral.getRefCatastral());
						 refCatastral.setRefCatastral(((ReferenciaCatastral) refCatastral)
										.getRefCatastral().substring(0, 14));
					}
		
					Element ref_catastral = doc.createElement("ref_catastral");
					datos_refcatastrales.appendChild(ref_catastral);
					ref_catastral.setTextContent(refCatastral.getRefCatastral());
		
					Element descripcion = doc.createElement("descripcion");
					datos_refcatastrales.appendChild(descripcion);
					descripcion.setTextContent(refCatastral.getDescripcion());
				}
		}
	}

	/**
	 * Almacenar Usos Funcionales
	 * 
	 * @param inmueble
	 * @param elementoPatrimonio
	 */
	private void insertarUsosFuncionales(Collection<UsoFuncional> usosFuncionales,
			Element elementoPatrimonio) {

		if(usosFuncionales!=null){
			Element datos_usos = doc.createElement("datos_usos");
			elementoPatrimonio.appendChild(datos_usos);
	
			for (UsoFuncional usoFuncional : usosFuncionales) {
	
				Element cat_usu_func = doc.createElement("cat_uso_func");
				datos_usos.appendChild(cat_usu_func);
					// cat_usu_func//tipo
					Element cat_usu_func_tipo = doc.createElement("tipo");
					cat_usu_func_tipo.appendChild(cat_usu_func);
					cat_usu_func_tipo.setTextContent(Estructuras.getListaUsosFuncionales().getDominioPadre());
					// cat_uso_jur//id
					Element cat_usu_func_id = doc.createElement("id");
					cat_usu_func.appendChild(cat_usu_func_id);
					cat_usu_func_id.setTextContent(usoFuncional.getUso());
	
				Element superficie = doc.createElement("superficie");
				datos_usos.appendChild(superficie);
				superficie.setTextContent(formateador8.format(usoFuncional.getSuperficie()));
	
				Element fecha = doc.createElement("fecha");
				datos_usos.appendChild(fecha);
				fecha.setTextContent(dateToDDMMYYY(((UsoFuncional) usoFuncional).getFecha()));
			}
		}
	}

	@Override
	protected void guardarFichero() {
		Writer output = null;

		try {
			GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
			filter.addExtension("xml");
			filter.setDescription(messages
					.getString("inventario.loadinventario.fichero"));
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			if (lastDirectory != null) {
				File currentDirectory = lastDirectory;
				fc.setCurrentDirectory(currentDirectory);
			}
			fc.setName(filter.getDescription());
			if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			// Si pulsa guardar-->
			//GUARDAR INVENTARIO
			lastDirectory = fc.getCurrentDirectory();
			String contenidoString = "";
			contenidoString = saveBienes();

//			// Si No hay bienes
//			if (contenidoString.length() == 0) {
//				JOptionPane optionPane = new JOptionPane(
//						GeopistaUtil.i18n_getname(
//								"inventario.load.seleccionar", messages),
//						JOptionPane.WARNING_MESSAGE);
//				JDialog dialog = optionPane.createDialog(this, GeopistaUtil
//						.i18n_getname("inventario.loadinventario.title",
//								messages));
//				dialog.setVisible(true);
//				return;
//			}
			
			 String nombreFichero= fc.getSelectedFile().getName();
             /** quitamos la extension que haya puesto el usuario */
             int index= nombreFichero.indexOf(".");
             if (index != -1){
            	 nombreFichero= nombreFichero.substring(0, index);
             }
             /** nos quedamos sólo con el path, sin el nombre del fichero */
             String pathDestino= fc.getSelectedFile().getPath();
             index= pathDestino.indexOf(nombreFichero);
             if (index != -1){
                 pathDestino= pathDestino.substring(0, index);
             }
             /** al nombre del fichero le añadimos la extension correspondiente al formato del fichero xml*/
             nombreFichero+=".xml";
           /** añadimos al path, el nombre del fichero con extension */
             pathDestino+= nombreFichero;
             
			output = new java.io.BufferedWriter(new java.io.FileWriter(pathDestino,false));//TODO: Aqui se añade la extension fija ".xml"
			output.write(contenidoString);
			JOptionPane optionOk = new JOptionPane(
					messages.getString("inventario.load.fichero.saved.ok")
							+ ":\n " + fc.getSelectedFile().getPath(),
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialogOk = optionOk.createDialog(this.getParent(),
					messages.getString("inventario.save.fichero.title"));
			dialogOk.setVisible(true);
		} catch (Exception ex) {
			logger.error(messages.getString("inventario.saveinventario.error"));
			ErrorDialog.show(this, "ERROR",
					messages.getString("inventario.saveinventario.error"),
					StringUtil.stackTrace(ex));
		} finally {
			try {
				output.close();
			} catch (Exception ex) {
			}
		}

	}

	private String saveBienes() {
		initProgressDialog(GeopistaUtil.i18n_getname("inventario.saveinventario.runDialogo.title"));

		return getContentOfDoc();
	}

	private void insertarInmuebles(Collection<InmuebleBean> inmuebles,
			TaskMonitorDialog progressDialog, Element patrimonio,
			String tipoInmueble) {
		int numeroElemento = 0;
		String inmuebleType=null;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(inmuebles.size()) };

		for (InmuebleBean inmueble : inmuebles) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			// Insertamos en el doc los Inmuebles 
			if (tipoInmueble.equals(Const.PATRON_INMUEBLES_URBANOS)) 
				inmuebleType="inmuebles_urbanos";
			else if (tipoInmueble.equals(Const.PATRON_INMUEBLES_RUSTICOS))
				inmuebleType="inmuebles_rusticos";
			
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);
			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados) +" "+ inmuebleType);
			
			Element inmuebleElement = doc.createElement(inmuebleType);
			patrimonio.appendChild(inmuebleElement);

			long startMils = Calendar.getInstance().getTimeInMillis();

			insertarInmueble(inmueble, inmuebleElement);		
			
			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + inmueble.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		
		}
		//Guarda el numero de elementos porcesado
		resumen.put(tipoInmueble+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ inmuebleType);
	}
	private void insertarDerechosReales(Collection<DerechoRealBean> derechosReales,
			TaskMonitorDialog progressDialog, Element patrimonio){
		
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(derechosReales.size()) };
		for (DerechoRealBean derechoReal : derechosReales) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados) +" "+ "derechos_reales");	
			
			Element inmuebleElement = doc.createElement("derechos_reales");
			
			long startMils = Calendar.getInstance().getTimeInMillis();

			insertarDerechoReal(derechoReal, inmuebleElement);
			
			patrimonio.appendChild(inmuebleElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + derechoReal.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_DERECHOS_REALES+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "derechos_reales");
		
	}
	
	private void insertarVias(Collection<ViaBean> bienes,
			TaskMonitorDialog progressDialog, Element patrimonio,
			String tipoVia) {
		int numeroElemento = 0;
		String inmuebleType=null;
		Object[] resultados={ new Integer(numeroElemento),
			new Integer(bienes.size()) };
		for (ViaBean vias : bienes) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			// Insertamos en el doc los Inmuebles 
			if (tipoVia.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS)) 
				inmuebleType="vias_urbanas";
			else if (tipoVia.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS))
				inmuebleType="vias_rusticas";
			
			numeroElemento++;
			resultados[0] = new Integer(numeroElemento); 

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ inmuebleType);
			
			Element inmuebleElement = doc.createElement(inmuebleType);
			
			long startMils = Calendar.getInstance().getTimeInMillis();

			if (inmuebleType.equals("vias_urbanas")){
				insertarViaUrbana(vias, inmuebleElement);	
			} else if (inmuebleType.equals("vias_rusticas")) {
				insertarViaRustica(vias, inmuebleElement);
			}
			
			patrimonio.appendChild(inmuebleElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + vias.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos procesados
		resumen.put(tipoVia+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ inmuebleType);

	}
	
	private void insertarVehiculos(Collection<VehiculoBean> bienes,
			TaskMonitorDialog progressDialog, Element patrimonio) {
		int numeroElemento = 0;

		Object[] resultados={ new Integer(numeroElemento),
				new Integer(bienes.size()) };
		for (VehiculoBean vehiculo : bienes) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0] = new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "vehiculos");
			
			Element inmuebleElement = doc.createElement("vehiculos");
			patrimonio.appendChild(inmuebleElement);
			
			long startMils = Calendar.getInstance().getTimeInMillis();
			insertarVehiculo(vehiculo, inmuebleElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + vehiculo.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_VEHICULOS+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "vehiculos");

	}
	
	private void insertarSemovientes(Collection<SemovienteBean> bienes,
			TaskMonitorDialog progressDialog, Element patrimonio)  {
		int numeroElemento = 0;

		Object[] resultados={ new Integer(numeroElemento),
				new Integer(bienes.size()) };
		for (SemovienteBean semoviente: bienes) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0] = new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "semovientes");
			
			Element inmuebleElement = doc.createElement("semovientes");
			
			long startMils = Calendar.getInstance().getTimeInMillis();

			insertarSemoviente(semoviente,inmuebleElement);
			
			patrimonio.appendChild(inmuebleElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + semoviente.getNombre()+ " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_SEMOVIENTES+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "semovientes");

	}
	
	
	private void insertarBienesRevertibles(Collection<BienRevertible>bienesRevertible,
			TaskMonitorDialog progressDialog, Element patrimonio) {
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(bienesRevertible.size()) };
		for (BienRevertible bieneRevertible : bienesRevertible) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {	
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "bienes revertibles");
			
			Element bienBeanElement = doc.createElement("bienes_revertibles");
			patrimonio.appendChild(bienBeanElement);
	
			long startMils = Calendar.getInstance().getTimeInMillis();
			insertarBienRevertible(bieneRevertible , bienBeanElement);
			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + bieneRevertible.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put("bienes_revertibles"+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "bienes revertibles");

	}
	private void insertarCreditosYDerechos(Collection<CreditoDerechoBean> creditosDerechos,
			TaskMonitorDialog progressDialog, Element patrimonio){
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(creditosDerechos.size()) };
		for (CreditoDerechoBean creditoDerecho : creditosDerechos) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "creditos derechos");
			
			Element patrimonioElement = doc.createElement("creditos_derechos");
			
			long startMils = Calendar.getInstance().getTimeInMillis();
			
			insertarCreditoYDerecho(creditoDerecho , patrimonioElement);
			patrimonio.appendChild(patrimonioElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + creditoDerecho.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "creditos derechos");

	}
	private void insertarMueblesHistoricoArt(Collection<MuebleBean> muebles,
			TaskMonitorDialog progressDialog, Element patrimonio){
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(muebles.size()) };
		for (MuebleBean muebleHistArt: muebles) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "historicos artisticos");
			
			Element patrimonioElement = doc.createElement("historicos_artisticos");
			
			long startMils = Calendar.getInstance().getTimeInMillis();
			insertarMueble(muebleHistArt, patrimonioElement);
			patrimonio.appendChild(patrimonioElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + muebleHistArt.getNombre()+" :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_MUEBLES_HISTORICOART+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "historicos artisticos");

	}
	
	private void insertarLotes(Collection<Lote> lotes,
			TaskMonitorDialog progressDialog, Element patrimonio){
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(lotes.size()) };
		for (Lote lote: lotes) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "lotes");
			
			Element patrimonioElement = doc.createElement("lotes");
			patrimonio.appendChild(patrimonioElement);
			long startMils = Calendar.getInstance().getTimeInMillis();
			insertarLote(lote, patrimonioElement);
			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + lote.getNombre_lote()+" :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put("lotes"+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "lotes");

	}
	
	private void insertarMueblesNoAnteriores(Collection<MuebleBean> muebles,
			TaskMonitorDialog progressDialog, Element patrimonio) {
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(muebles.size()) };
		
		for (MuebleBean mueblesNoAnterior : muebles) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {		
			numeroElemento++;
			resultados[0]= new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "otros_muebles");
			
			Element patrimonioElement = doc.createElement("otros_muebles");
			
			long startMils = Calendar.getInstance().getTimeInMillis();

			insertarMueble(mueblesNoAnterior, patrimonioElement);
			
			patrimonio.appendChild(patrimonioElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + mueblesNoAnterior.getNombre()+ " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put("otros_muebles"+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "otros_muebles");
	}
	
	private void insertarValoresMobiliarios(Collection<ValorMobiliarioBean> valoresMoviliario,
			TaskMonitorDialog progressDialog, Element patrimonio) {
		int numeroElemento = 0;
		Object[] resultados = { new Integer(numeroElemento),
				new Integer(valoresMoviliario.size()) };
		for (ValorMobiliarioBean valorMoviliario : valoresMoviliario) {// &&
													// !progressDialog.isCancelRequested();
													// i++) {
			
			numeroElemento++;
			resultados[0]=new Integer(numeroElemento);

			progressDialog.report(getStringWithParameters(messages,
					"inventario.save.dialogo.report2", resultados)+ " "+ "valores mobiliarios");
			
			Element patrimonioElement = doc.createElement("valores_mobiliarios");
			
			long startMils = Calendar.getInstance().getTimeInMillis();
			
			insertarValorMobiliario(valorMoviliario , patrimonioElement);
			patrimonio.appendChild(patrimonioElement);

			long endMils = Calendar.getInstance().getTimeInMillis();
			logger.info("End Time:" + valorMoviliario.getNombre() + " :"
					+ (endMils - startMils) / 1000 + " segundos");
		}
		//Guarda el numero de elementos porcesado
		resumen.put(Const.PATRON_VALOR_MOBILIARIO+BIENES+PROCESADOS, numeroElemento);
		progressDialog.report(getStringWithParameters(messages,
				"inventario.save.dialogo.report3", resultados)+" "+ "valores mobiliarios");

	}
	
	
	private Collection<Observacion> getObservacionesFiltradas(
			Collection<Observacion> observaciones, String observacionName) {
		Collection<Observacion> observacionesFiltered = new ArrayList<Observacion>();
		if (observaciones != null) {
			for (Observacion observacion : observaciones) {
				if (observacion.getDescripcion().startsWith(observacionName)) {
					observacionesFiltered.add(observacion);
				}
			}
		}
		return observacionesFiltered;
	}
private void insertarResumen(TaskMonitorDialog progressDialog, Element patrimonio) {
		
		int totalBienes=0;
		int procesadosBienes=0;
		int totalDocumentos=0;
		int procesadosDocumentos=0;
		
		//resumen_contenido
		Element resumen_contenido = doc.createElement("resumen_contenido");
		patrimonio.appendChild(resumen_contenido );
		//bienes
		Element bienes = doc.createElement("bienes");
		resumen_contenido.appendChild(bienes);
				
		//creditos_derechos
		Element creditos_derechos = doc.createElement("creditos_derechos");
		bienes.appendChild(creditos_derechos);
		
			Element totalBienCreDer = doc.createElement("total");
			creditos_derechos.appendChild(totalBienCreDer);
			totalBienCreDer.setTextContent(Integer.toString(resumen.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+TOTALES);
			Element procesadosCreDer=doc.createElement("procesados");
			creditos_derechos.appendChild(procesadosCreDer);
			procesadosCreDer.setTextContent(Integer.toString(resumen.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES+BIENES+PROCESADOS);
		//derechos_reales
		Element derechos_reales = doc.createElement("derechos_reales");
		bienes.appendChild(derechos_reales);
		
			Element totalBienDerRea = doc.createElement("total");
			derechos_reales.appendChild(totalBienDerRea);
			totalBienDerRea.setTextContent(Integer.toString(resumen.get(Const.PATRON_DERECHOS_REALES+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_DERECHOS_REALES+BIENES+TOTALES);
			Element procesadosDerRea=doc.createElement("procesados");
			derechos_reales.appendChild(procesadosDerRea);
			procesadosDerRea.setTextContent(Integer.toString(resumen.get(Const.PATRON_DERECHOS_REALES+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_DERECHOS_REALES+BIENES+PROCESADOS);
						
		//inmuebles_rusticos
		Element inmuebles_rusticos = doc.createElement("inmuebles_rusticos");
		bienes.appendChild(inmuebles_rusticos);
		
			Element totalBienImRu = doc.createElement("total");
			inmuebles_rusticos.appendChild(totalBienImRu);
			totalBienImRu.setTextContent(Integer.toString(resumen.get(Const.PATRON_INMUEBLES_RUSTICOS+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_INMUEBLES_RUSTICOS+BIENES+TOTALES);
			Element procesadosImRu=doc.createElement("procesados");
			inmuebles_rusticos.appendChild(procesadosImRu);
			procesadosImRu.setTextContent(Integer.toString(resumen.get(Const.PATRON_INMUEBLES_RUSTICOS+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_INMUEBLES_RUSTICOS+BIENES+PROCESADOS);
			
		//inmuebles_urbanos
		Element inmuebles_urbanos = doc.createElement("inmuebles_urbanos");
		bienes.appendChild(inmuebles_urbanos);
		
			Element totalBienImUr = doc.createElement("total");
			inmuebles_urbanos.appendChild(totalBienImUr);
			totalBienImUr.setTextContent(Integer.toString(resumen.get(Const.PATRON_INMUEBLES_URBANOS+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_INMUEBLES_URBANOS+BIENES+TOTALES);
			Element procesadosImUr=doc.createElement("procesados");
			inmuebles_urbanos.appendChild(procesadosImUr);
			procesadosImUr.setTextContent(Integer.toString(resumen.get(Const.PATRON_INMUEBLES_URBANOS+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_INMUEBLES_URBANOS+BIENES+PROCESADOS);
		
		//historico_artisticos
		Element historico_artisticos = doc.createElement("historico_artisticos");
		bienes.appendChild(historico_artisticos);

			Element totalBienHiAr = doc.createElement("total");
			historico_artisticos.appendChild(totalBienHiAr);
			totalBienHiAr.setTextContent(Integer.toString(resumen.get(Const.PATRON_MUEBLES_HISTORICOART+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_MUEBLES_HISTORICOART+BIENES+TOTALES);
			Element procesadosHiAr=doc.createElement("procesados");
			historico_artisticos.appendChild(procesadosHiAr);
			procesadosHiAr.setTextContent(Integer.toString(resumen.get(Const.PATRON_MUEBLES_HISTORICOART+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_MUEBLES_HISTORICOART+BIENES+PROCESADOS);
			
		//otros_muebles
		Element otros_muebles = doc.createElement("otros_muebles");
		bienes.appendChild(otros_muebles);

			Element totalBienOtMu = doc.createElement("total");
			otros_muebles.appendChild(totalBienOtMu);
			totalBienOtMu.setTextContent(Integer.toString(resumen.get("otros_muebles"+BIENES+TOTALES)));
			totalBienes+=resumen.get("otros_muebles"+BIENES+TOTALES);
			Element procesadosOtMu=doc.createElement("procesados");
			otros_muebles.appendChild(procesadosOtMu);
			procesadosOtMu.setTextContent(Integer.toString(resumen.get("otros_muebles"+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get("otros_muebles"+BIENES+PROCESADOS);
			
		//semovientes
		Element semovientes= doc.createElement("semovientes");
		bienes.appendChild(semovientes);

			Element totalBienSemo = doc.createElement("total");
			semovientes.appendChild(totalBienSemo);
			totalBienSemo.setTextContent(Integer.toString(resumen.get(Const.PATRON_SEMOVIENTES+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_SEMOVIENTES+BIENES+TOTALES);
			Element procesadosSemo=doc.createElement("procesados");
			semovientes.appendChild(procesadosSemo);
			procesadosSemo.setTextContent(Integer.toString(resumen.get(Const.PATRON_SEMOVIENTES+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_SEMOVIENTES+BIENES+PROCESADOS);
		
		//valores_mobiliarios
		Element valores_mobiliarios = doc.createElement("valores_mobiliarios");
		bienes.appendChild(valores_mobiliarios);

			Element totalBienVaMo = doc.createElement("total");
			valores_mobiliarios.appendChild(totalBienVaMo);
			totalBienVaMo.setTextContent(Integer.toString(resumen.get(Const.PATRON_VALOR_MOBILIARIO+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_VALOR_MOBILIARIO+BIENES+TOTALES);
			Element procesadosVaMo=doc.createElement("procesados");
			valores_mobiliarios.appendChild(procesadosVaMo);
			procesadosVaMo.setTextContent(Integer.toString(resumen.get(Const.PATRON_VALOR_MOBILIARIO+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_VALOR_MOBILIARIO+BIENES+PROCESADOS);
			
		//vehiculos
		Element vehiculos = doc.createElement("vehiculos");
		bienes.appendChild(vehiculos);

			Element totalBienVeHi = doc.createElement("total");
			vehiculos.appendChild(totalBienVeHi);
			totalBienVeHi.setTextContent(Integer.toString(resumen.get(Const.PATRON_VEHICULOS+BIENES+TOTALES)));
			totalBienes+=resumen.get(Const.PATRON_VEHICULOS+BIENES+TOTALES);
			Element procesadosVeHi=doc.createElement("procesados");
			vehiculos.appendChild(procesadosVeHi);
			procesadosVeHi.setTextContent(Integer.toString(resumen.get(Const.PATRON_VEHICULOS+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get(Const.PATRON_VEHICULOS+BIENES+PROCESADOS);
			
		//lotes
		Element lotes = doc.createElement("lotes");
		bienes.appendChild(lotes);

			Element totalBienLote = doc.createElement("total");
			lotes.appendChild(totalBienLote);
			totalBienLote.setTextContent(Integer.toString(resumen.get("lotes"+BIENES+TOTALES)));
			totalBienes+=resumen.get("lotes"+BIENES+TOTALES);
			Element procesadosLote=doc.createElement("procesados");
			lotes.appendChild(procesadosLote);
			procesadosLote.setTextContent(Integer.toString(resumen.get("lotes"+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get("lotes"+BIENES+PROCESADOS);
		
		//bienes_revertibles
		Element bienes_revertibles = doc.createElement("bienes_revertibles");
		bienes.appendChild(bienes_revertibles);

			Element totalBienReVe = doc.createElement("total");
			bienes_revertibles.appendChild(totalBienReVe);
			totalBienReVe.setTextContent(Integer.toString(resumen.get("bienes_revertibles"+BIENES+TOTALES)));
			totalBienes+=resumen.get("bienes_revertibles"+BIENES+TOTALES);
			Element procesadosReVe=doc.createElement("procesados");
			bienes_revertibles.appendChild(procesadosReVe);
			procesadosReVe.setTextContent(Integer.toString(resumen.get("bienes_revertibles"+BIENES+PROCESADOS)));
			procesadosBienes+=resumen.get("bienes_revertibles"+BIENES+PROCESADOS);			
		
		//documentos
		Element documentos = doc.createElement("documentos");
		resumen_contenido.appendChild(documentos);
					
		//creditos_derechosDoc
		Element creditos_derechosDoc = doc.createElement("creditos_derechos");
		documentos.appendChild(creditos_derechosDoc);
			
			Element totalDocCreDer = doc.createElement("total");
			creditos_derechosDoc.appendChild(totalDocCreDer);
			totalDocCreDer.setTextContent(resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ TOTALES) != null ? Integer.toString(resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ TOTALES)) : Integer.toString(0));
			totalDocumentos += resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ TOTALES) != null ? resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ TOTALES) : 0;
			Element procesadosDocCreDer=doc.createElement("procesados");
			creditos_derechosDoc.appendChild(procesadosDocCreDer);
			procesadosDocCreDer.setTextContent(resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ PROCESADOS) != null ? Integer.toString(resumen
					.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
							+ PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen
						.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
								+ PROCESADOS) != null ? resumen
						.get(Const.PATRON_CREDITOS_DERECHOS_PERSONALES + DOCUMENTOS
								+ PROCESADOS) : 0;
		
		//derechos_reales
		Element derechos_realesDoc = doc.createElement("derechos_reales");
		documentos.appendChild(derechos_realesDoc);
			
			Element totalDocDerRea = doc.createElement("total");
			derechos_realesDoc.appendChild(totalDocDerRea);
			totalDocDerRea.setTextContent(resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ TOTALES) != null ? Integer.toString(resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen
								.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
										+ TOTALES) != null ? resumen
								.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
										+ TOTALES) : 0;
			Element procesadosDocDerRea=doc.createElement("procesados");
			derechos_realesDoc.appendChild(procesadosDocDerRea);
			procesadosDocDerRea.setTextContent(resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ PROCESADOS) != null ? Integer.toString(resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos += resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ PROCESADOS) != null ? resumen
					.get(Const.PATRON_DERECHOS_REALES + DOCUMENTOS
							+ PROCESADOS) : 0;
							
		//inmuebles_rusticos	
		Element inmuebles_rusticosDoc = doc.createElement("inmuebles_rusticos");
		documentos.appendChild(inmuebles_rusticosDoc);
			
			Element totalDocImRu = doc.createElement("total");
			inmuebles_rusticosDoc.appendChild(totalDocImRu);
			totalDocImRu.setTextContent(resumen.get(Const.PATRON_INMUEBLES_RUSTICOS+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_INMUEBLES_RUSTICOS + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
				
			totalDocumentos += resumen.get(Const.PATRON_INMUEBLES_RUSTICOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_INMUEBLES_RUSTICOS + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocImRu=doc.createElement("procesados");
			inmuebles_rusticosDoc.appendChild(procesadosDocImRu);
			procesadosDocImRu.setTextContent(resumen.get(Const.PATRON_INMUEBLES_RUSTICOS
					+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_INMUEBLES_RUSTICOS
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos += resumen.get(Const.PATRON_INMUEBLES_RUSTICOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_INMUEBLES_RUSTICOS + DOCUMENTOS + PROCESADOS) : 0;
				
		//inmuebles_urbanos
		Element inmuebles_urbanosDoc = doc.createElement("inmuebles_urbanos");
		documentos.appendChild(inmuebles_urbanosDoc);
			
			Element totalDocImUr = doc.createElement("total");
			inmuebles_urbanosDoc.appendChild(totalDocImUr);
			totalDocImUr.setTextContent(resumen.get(Const.PATRON_INMUEBLES_URBANOS+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_INMUEBLES_URBANOS + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get(Const.PATRON_INMUEBLES_URBANOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_INMUEBLES_URBANOS + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocImUr=doc.createElement("procesados");
			inmuebles_urbanosDoc.appendChild(procesadosDocImUr);
			procesadosDocImUr.setTextContent(resumen.get(Const.PATRON_INMUEBLES_URBANOS
					+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_INMUEBLES_URBANOS
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get(Const.PATRON_INMUEBLES_URBANOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_INMUEBLES_URBANOS + DOCUMENTOS + PROCESADOS) : 0;
			
		//historico_artisticos
		Element historico_artisticosDoc = doc.createElement("historico_artisticos");
		documentos.appendChild(historico_artisticosDoc);

			Element totalDocHiAr = doc.createElement("total");
			historico_artisticosDoc.appendChild(totalDocHiAr);
			totalDocHiAr.setTextContent(resumen.get(Const.PATRON_MUEBLES_HISTORICOART+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_MUEBLES_HISTORICOART + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get(Const.PATRON_MUEBLES_HISTORICOART
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_MUEBLES_HISTORICOART + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocHiAr=doc.createElement("procesados");
			historico_artisticosDoc.appendChild(procesadosDocHiAr);
			procesadosDocHiAr.setTextContent(resumen.get(Const.PATRON_MUEBLES_HISTORICOART
					+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_MUEBLES_HISTORICOART 
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get(Const.PATRON_MUEBLES_HISTORICOART
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_MUEBLES_HISTORICOART + DOCUMENTOS + PROCESADOS) : 0;
				
		//otros_muebles
		Element otros_mueblesDoc = doc.createElement("otros_muebles");
		documentos.appendChild(otros_mueblesDoc);

			Element totalDocOtMu = doc.createElement("total");
			otros_mueblesDoc.appendChild(totalDocOtMu);
			totalDocOtMu.setTextContent(resumen.get("otros_muebles"+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get("otros_muebles" + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get("otros_muebles" 
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get("otros_muebles"  + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocOtMu=doc.createElement("procesados");
			otros_mueblesDoc.appendChild(procesadosDocOtMu);
			procesadosDocOtMu.setTextContent(resumen.get("otros_muebles"
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get("otros_muebles" 
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get("otros_muebles"
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get("otros_muebles" + DOCUMENTOS + PROCESADOS) : 0;
				
		//semovientes
		Element semovientesDoc= doc.createElement("semovientes");
		documentos.appendChild(semovientesDoc);

			Element totalDocSemo = doc.createElement("total");
			semovientesDoc.appendChild(totalDocSemo);
			totalDocSemo.setTextContent(resumen.get(Const.PATRON_SEMOVIENTES+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_SEMOVIENTES + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get(Const.PATRON_SEMOVIENTES 
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_SEMOVIENTES  + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocSemo=doc.createElement("procesados");
			semovientesDoc.appendChild(procesadosDocSemo);
			procesadosDocSemo.setTextContent(resumen.get(Const.PATRON_SEMOVIENTES
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_SEMOVIENTES
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get(Const.PATRON_SEMOVIENTES
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_SEMOVIENTES+ DOCUMENTOS + PROCESADOS) : 0;
		//valores_mobiliarios
		Element valores_mobiliariosDoc = doc.createElement("valores_mobiliarios");
		documentos.appendChild(valores_mobiliariosDoc);

			Element totalDocVaMo = doc.createElement("total");
			valores_mobiliariosDoc.appendChild(totalDocVaMo);
			totalDocVaMo.setTextContent(resumen.get(Const.PATRON_VALOR_MOBILIARIO+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_VALOR_MOBILIARIO + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get(Const.PATRON_VALOR_MOBILIARIO 
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_VALOR_MOBILIARIO  + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocVaMo=doc.createElement("procesados");
			valores_mobiliariosDoc.appendChild(procesadosDocVaMo);
			procesadosDocVaMo.setTextContent(resumen.get(Const.PATRON_VALOR_MOBILIARIO
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_VALOR_MOBILIARIO
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get(Const.PATRON_VALOR_MOBILIARIO
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_VALOR_MOBILIARIO+ DOCUMENTOS + PROCESADOS) : 0;
				
		//vehiculos
		Element vehiculosDoc = doc.createElement("vehiculos");
		documentos.appendChild(vehiculosDoc);

			Element totalDocVeHi = doc.createElement("total");
			vehiculosDoc.appendChild(totalDocVeHi);
			totalDocVeHi.setTextContent(resumen.get(Const.PATRON_VEHICULOS+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get(Const.PATRON_VEHICULOS + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get(Const.PATRON_VEHICULOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_VEHICULOS  + DOCUMENTOS + TOTALES) : 0;
			Element procesadosDocVeHi=doc.createElement("procesados");
			vehiculosDoc.appendChild(procesadosDocVeHi);
			procesadosDocVeHi.setTextContent(resumen.get(Const.PATRON_VEHICULOS
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get(Const.PATRON_VEHICULOS
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get(Const.PATRON_VEHICULOS
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get(Const.PATRON_VEHICULOS+ DOCUMENTOS + PROCESADOS) : 0;
				
		//lotes
		Element lotesDoc = doc.createElement("lotes");
		documentos.appendChild(lotesDoc);

			Element totalDocLote = doc.createElement("total");
			lotesDoc.appendChild(totalDocLote);
			totalDocLote.setTextContent(resumen.get("lotes"+ DOCUMENTOS
					+ TOTALES) != null ? Integer.toString(resumen
							.get("lotes" + DOCUMENTOS
									+ TOTALES)) : Integer.toString(0));
			totalDocumentos+=resumen.get("lotes"
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get("lotes" + DOCUMENTOS + TOTALES) : 0;
							
			Element procesadosDocLote=doc.createElement("procesados");
			lotesDoc.appendChild(procesadosDocLote);
			procesadosDocLote.setTextContent(resumen.get("lotes"
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
							.toString(resumen.get("lotes"
									+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
			procesadosDocumentos+=resumen.get("lotes"
					+ DOCUMENTOS + TOTALES) != null ? resumen
							.get("lotes"+ DOCUMENTOS + PROCESADOS) : 0;
			
		//bienes_revertibles
		Element bienes_revertiblesDoc = doc.createElement("bienes_revertibles");
		documentos.appendChild(bienes_revertiblesDoc);

				Element totalDocReVe = doc.createElement("total");
				bienes_revertiblesDoc.appendChild(totalDocReVe);
				totalDocReVe.setTextContent(resumen.get("bienes_revertibles"+ DOCUMENTOS
						+ TOTALES) != null ? Integer.toString(resumen
								.get("bienes_revertibles" + DOCUMENTOS
										+ TOTALES)) : Integer.toString(0));
				totalBienes+=resumen.get("bienes_revertibles"
						+ DOCUMENTOS + TOTALES) != null ? resumen
								.get("bienes_revertibles" + DOCUMENTOS + TOTALES) : 0;
				Element procesadosDocReVe=doc.createElement("procesados");
				bienes_revertiblesDoc.appendChild(procesadosDocReVe);
				procesadosDocReVe.setTextContent(resumen.get("bienes_revertibles"
						+ DOCUMENTOS + PROCESADOS) != null ? Integer
								.toString(resumen.get("bienes_revertibles"
										+ DOCUMENTOS + PROCESADOS)) : Integer.toString(0));
				procesadosDocumentos+=resumen.get("bienes_revertibles"
						+ DOCUMENTOS + TOTALES) != null ? resumen
								.get("bienes_revertibles"+ DOCUMENTOS + PROCESADOS) : 0;
				
		//totales
		Element totales = doc.createElement("totales");
		resumen_contenido.appendChild(totales);
		
			//total_bienes
			Element total_bienes = doc.createElement("total_bienes");
			totales.appendChild(total_bienes);
			total_bienes.setTextContent(Integer.toString(totalBienes));
			//total_bienes_procesados
			Element total_bienes_procesados = doc.createElement("total_bienes_procesados");
			totales.appendChild(total_bienes_procesados);
			total_bienes_procesados.setTextContent(Integer.toString(procesadosBienes));
			//total_documentos
			Element total_documentos = doc.createElement("total_documentos");
			totales.appendChild(total_documentos);
			total_documentos.setTextContent(Integer.toString(totalDocumentos));
			//total_documentos_procesados
			Element total_documentos_procesados = doc.createElement("total_documentos_procesados");
			totales.appendChild(total_documentos_procesados);
			total_documentos_procesados.setTextContent(Integer.toString(procesadosDocumentos));
		
	}


	private void insertarDatosComunes(BienBean inmuebleBean,
			Element datos){

	
		// bienes inventario
		Element bienesInventario = doc.createElement("bienes_inventario");
		datos.appendChild(bienesInventario);
		// numinventario
		Element numInventario = doc.createElement("numinventario");
		bienesInventario.appendChild(numInventario);
		numInventario.setTextContent(inmuebleBean.getNumInventario());

		// fecha_alta
		Element fecha_alta = doc.createElement("fecha_alta");
		bienesInventario.appendChild(fecha_alta);
		fecha_alta.setTextContent(dateToDDMMYYY(inmuebleBean
				.getFechaAlta()));
		// fecha_baja
		Element fecha_baja = doc.createElement("fecha_baja");
		bienesInventario.appendChild(fecha_baja);
		fecha_baja.setTextContent(dateToDDMMYYY(inmuebleBean
				.getFechaBaja()));
		// fecha_ultima_modificacion
		Element fecha_ultima_modificacion = doc
				.createElement("fecha_ultima_modificacion");
		bienesInventario.appendChild(fecha_ultima_modificacion);
		fecha_ultima_modificacion.setTextContent(dateToDDMMYYY(inmuebleBean.getFechaUltimaModificacion()));
		// descripcion
		Element descripcion = doc.createElement("descripcion");
		bienesInventario.appendChild(descripcion);
		descripcion.setTextContent(inmuebleBean.getDescripcion());
		// nombre
		Element nombre = doc.createElement("nombre");
		bienesInventario.appendChild(nombre);
		nombre.setTextContent(inmuebleBean.getNombre());
		if(inmuebleBean.getUso()!=null&&inmuebleBean.getUso()!=""){
			// cat_uso_jur
			Element cat_uso_jur = doc.createElement("cat_uso_jur");
			bienesInventario.appendChild(cat_uso_jur);
				// cat_uso_jur//tipo
				Element cat_uso_jur_tipo = doc.createElement("tipo");
				cat_uso_jur.appendChild(cat_uso_jur_tipo);
				cat_uso_jur_tipo.setTextContent(Estructuras.getListaUsoJuridico().getDominioPadre());
				// cat_uso_jur//id
				Element cat_uso_jur_id = doc.createElement("id");
				cat_uso_jur.appendChild(cat_uso_jur_id);
				cat_uso_jur_id.setTextContent(inmuebleBean.getUso());
		}
		// organizacion
		Element organizacion = doc.createElement("organizacion");
		bienesInventario.appendChild(organizacion);
		organizacion.setTextContent(inmuebleBean.getOrganizacion());
		// patrimonio_municipal_suelo
		Element patrimonio_municipal_suelo = doc
				.createElement("patrimonio_municipal_suelo");
		bienesInventario.appendChild(patrimonio_municipal_suelo);
		patrimonio_municipal_suelo.setTextContent(inmuebleBean.getPatrimonioMunicipalSuelo()?"1":"0");
		// fecha_aprobacion_pleno
		Element fecha_aprobacion_pleno = doc
				.createElement("fecha_aprobacion_pleno");
		bienesInventario.appendChild(fecha_aprobacion_pleno);
		fecha_aprobacion_pleno.setTextContent(dateToDDMMYYY(inmuebleBean.getFechaAprobacionPleno()));
		
		if (inmuebleBean.getAdquisicion()!=null&&inmuebleBean.getAdquisicion()!="") {

			// cat_adquisicion
			Element cat_adquisicion = doc.createElement("cat_adquisicion");
			bienesInventario.appendChild(cat_adquisicion);
				// cat_adquisicion//tipo
				Element cat_adquisicion_tipo = doc.createElement("tipo");
				cat_adquisicion.appendChild(cat_adquisicion_tipo);
				cat_adquisicion_tipo.setTextContent(Estructuras.getListaFormaAdquisicion().getDominioPadre());
				// cat_adquisicion//id
				Element cat_adquisicion_id = doc.createElement("id");
				cat_adquisicion.appendChild(cat_adquisicion_id);
				cat_adquisicion_id.setTextContent(inmuebleBean.getAdquisicion());
		}
		Collection<Observacion> observaciones = inmuebleBean.getObservaciones();
		Collection<Observacion> observacionesDiagnosis=getObservacionesFiltradas(
					observaciones,
					GeopistaUtil
							.i18n_getname("inventario.loadinventario.diagnosis"));
		Collection<Observacion> observacionesDiagnosisDesc = getObservacionesFiltradas(
					observaciones,
					GeopistaUtil
							.i18n_getname("inventario.loadinventario.descripcion_diagnosis"));
		
		if (observacionesDiagnosis.size()!=0) {
			
			// datos_diagnosis
			Element datos_diagnosis = doc.createElement("datos_diagnosis");
			bienesInventario.appendChild(datos_diagnosis);
			for (Observacion observacion : observacionesDiagnosis) {				
				// cat_diagnosis
				Element cat_diagnosis = doc.createElement("cat_diagnosis");
				datos_diagnosis.appendChild(cat_diagnosis);
					// cat_diagnosis//tipo
					Element cat_diagnosis_tipo = doc.createElement("tipo");
					cat_diagnosis.appendChild(cat_diagnosis_tipo);
					cat_diagnosis_tipo.setTextContent(Estructuras.getListaDiagnosis().getDominioPadre());
					// cat_uso_jur//id
					Element cat_diagnosis_id = doc.createElement("id");
					cat_diagnosis.appendChild(cat_diagnosis_id);
					cat_diagnosis_id.setTextContent(Long.toString(observacion.getId()));
					
			}			
			// descripcion_diagnosis
			insertarObservacion(observacionesDiagnosisDesc, datos_diagnosis, "descripcion_diagnosis");
			
		}			
		Collection<Observacion> observacionesUltimo = getObservacionesFiltradas(
				observaciones,
				GeopistaUtil
				.i18n_getname("inventario.loadinventario.ultimo_inventario"));
		insertarObservacion(observacionesUltimo, bienesInventario, "ultimo_inventario");
		
		int numDocumentosInmueble=saveListadoDocumentos(inmuebleBean,bienesInventario);
		resumen.put(inmuebleBean.getTipo()+DOCUMENTOS+TOTALES,inmuebleBean.getDocumentos()!=null?inmuebleBean.getDocumentos().size():0);
		resumen.put(inmuebleBean.getTipo()+DOCUMENTOS+PROCESADOS,numDocumentosInmueble);
		
		// revision_actual
		Element revision_actual = doc.createElement("revision_actual");
		bienesInventario.appendChild(revision_actual);
		revision_actual.setTextContent(Long.toString(inmuebleBean
				.getRevisionActual()));
		// revision_actual
		Element revision_expirada = doc.createElement("revision_expirada");
		bienesInventario.appendChild(revision_expirada);
		revision_expirada.setTextContent(Long.toString(inmuebleBean.getRevisionExpirada()));
		// frutos
		Element frutos = doc.createElement("frutos");
		bienesInventario.appendChild(frutos);
		frutos.setTextContent(inmuebleBean.getFrutos());
		// importe_frutos
		Element importe_frutos = doc.createElement("importe_frutos");
		bienesInventario.appendChild(importe_frutos);
		importe_frutos.setTextContent(inmuebleBean.getImporteFrutos()!=null?formateador14.format(inmuebleBean.getImporteFrutos()):Double.toString(0.0));

		Collection<Observacion> observacionesModificado = getObservacionesFiltradas(
				observaciones,
				GeopistaUtil
						.i18n_getname("inventario.loadinventario.modificado"));
		Element elementNew = doc.createElement("modificado");
		
		for (Observacion observacion: observacionesModificado) {
				
				bienesInventario.appendChild(elementNew);
				elementNew.setTextContent(observacion.getDescripcion());
		}
		if(observacionesModificado.size()==0){
			if(inmuebleBean.getFechaUltimaModificacion()==null ||inmuebleBean.getFechaAlta()==inmuebleBean.getFechaUltimaModificacion()  )
				elementNew.setTextContent("No");
			else
				elementNew.setTextContent("Si");
		}
	}

	@Override
	protected void addEspecificComponents() {
		jRadioAllBienes = new JRadioButton();
		jRadioListOfBienes = new JRadioButton();
	        
	     jRadioAllBienes.setBounds(new Rectangle(80, 85, 150, 20));
	     jRadioAllBienes.setSelected(true);
	     jRadioAllBienes.addActionListener(new java.awt.event.ActionListener() {
	    	 public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 if(jRadioAllBienes.isSelected())
	    			 jRadioListOfBienes.setSelected(false);
	             }
	        });
	     jRadioListOfBienes.setBounds(new Rectangle(80, 120, 150, 20));
	     jRadioListOfBienes.addActionListener(new java.awt.event.ActionListener() {
	    	 public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		 if(jRadioListOfBienes.isSelected())
	    			 jRadioAllBienes.setSelected(false);
	           }
	        });
	     
	     jPanelSave.add(jRadioAllBienes,null);
         jPanelSave.add(jRadioListOfBienes, null); 
	}
	
	/**
	 * Devuelve un vector de documentos
	 * @return documents
	 */
	public ArrayList<DocumentBean> getDocuments() {
		return documents;
	}
	
	/**
	 * Convierte una fecha tipo date al formato ddMMyyyy
	 * @param d
	 * @return fecha formateda
	 */
	public String dateToDDMMYYY(Date d)
	    {
	    	if(d==null) return null;
	    	
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        return sdf.format(d);
	    }

	 /**
     * Método que devuelve una colleccion de bienes actualizados a la ultima version
     * @param c Collection de bienes
     */
    public Collection getBienesActualVersion(Collection c){
    	int numBienes=0;
        Collection cRet= new ArrayList();
        if (c != null){
	    	Object[] arrayBienes = c.toArray();
	    	int  n = arrayBienes.length;
	    	/*if (arrayBienes.length>MAX_REGISTROS){
	    		JOptionPane.showMessageDialog(this, "Demasiados objetos en la lista se mostraran los "+MAX_REGISTROS+" primeros\n"+aplicacion.getI18nString("inventario.mensajes.tag2"));
				n=MAX_REGISTROS;
	    	}*/
	    
	    	for (int i=0;i<n;i++){
	    		Versionable bien = (Versionable) arrayBienes[i];
	    		//logger.info("BIEN:"+bien.getNumInventario());
	    		if (!bien.isVersionado()){
	    			//if ((bien.isBorrado()) || (bien.isEliminado()))
	    			//	cRetBorrados.add(bien);	    			    		
	    			//else 
	    				cRet.add(bien);
	    			numBienes++;
	    	
	    		}
	    	}
        }
        return cRet;
    }
}
