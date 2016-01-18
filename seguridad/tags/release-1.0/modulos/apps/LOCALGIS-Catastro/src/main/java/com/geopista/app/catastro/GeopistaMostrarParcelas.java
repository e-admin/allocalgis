package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaMostrarParcelas extends JPanel implements WizardPanel
{

	private static final long serialVersionUID = 1L;
	
	private String nextID = null;

	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(GeopistaMostrarParcelas.class);

	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	private Blackboard blackboard = aplicacion.getBlackboard();

	private JScrollPane scpErrores = new JScrollPane();

	private JLabel lblImagen = new JLabel();

	private JLabel lblTipoFichero = new JLabel();

	private JComboBox cmbTipoInfo = new JComboBox();

	private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

	private GeopistaEditor geopistaEditor = null;

	private GeopistaLayer capaParcelasFuente = null;

	private GeopistaLayer capaParcelas = null;

	private static final int CANCELAR = 1;

	private JScrollPane jScrollPane2 = new JScrollPane();

	private JEditorPane jedResumen = new JEditorPane();

	private int numeroRegistrosLeidos = 1;

	private String cadenaTexto = "";

	private int registrosErroneos = 0;
	private int registrosCorrectos = 0;
	private WizardContext wizardContext;

	private String relleno = "00000";
	
	private String refCatAttName = new String();

	public GeopistaMostrarParcelas()
	{
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
				.getMainFrame(), null);

		progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
		progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{

				// Wait for the dialog to appear before starting the
				// task. Otherwise
				// the task might possibly finish before the dialog
				// appeared and the
				// dialog would never close. [Jon Aquino]
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							setName(aplicacion.getI18nString("importar.asistente.parcelas.titulo.2"));
							geopistaEditor = (GeopistaEditor) blackboard.get("geopistaEditorInfoReferencia");
							jbInit();
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(null);

		scpErrores.setBounds(new java.awt.Rectangle(130,50,600,323));
		scpErrores.getViewport().setLayout(null);

		cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.parcelas"));

		//lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
		lblImagen.setIcon(IconLoader.icon("catastro.png"));
		lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
		lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		lblTipoFichero.setText(aplicacion
				.getI18nString("importar.informacion.referencia.tipo.fichero"));
		lblTipoFichero.setBounds(new Rectangle(136, 22, 140, 20));

		cmbTipoInfo.setBackground(new Color(255, 255, 255));
		cmbTipoInfo.setBounds(new Rectangle(283, 20, 290, 20));
		geopistaEditor.setBounds(new Rectangle(130,50,600,323));

		jScrollPane2.setBounds(new java.awt.Rectangle(131,379,600,132));

		geopistaEditor.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
		geopistaEditor.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
		ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
		geopistaEditor.addPlugIn(zoomToFullExtentPlugIn);

		geopistaEditor.addPlugIn(geopistaValidatePlugIn);

		this.setSize(750, 550);
		jScrollPane2.getViewport().add(jedResumen, null);
		this.add(jScrollPane2, null);
		this.add(cmbTipoInfo, null);
		this.add(lblTipoFichero, null);
		this.add(lblImagen, null);
		scpErrores.getViewport().add(geopistaEditor, null);
		this.add(scpErrores, null);


	}

	public void enteredFromLeft(Map dataMap)
	{
		wizardContext.previousEnabled(false);
		jedResumen.setContentType("text/html");
		this.jedResumen.setEditable(false);
		try
		{
			// Iniciamos la ayuda
			String helpHS = "ayuda.hs";
			ClassLoader c1 = this.getClass().getClassLoader();
			URL hsURL = HelpSet.findHelpSet(c1, helpHS);
			HelpSet hs = new HelpSet(null, hsURL);
			HelpBroker hb = hs.createHelpBroker();
			// fin de la ayuda
			hb.enableHelpKey(this, "InformacionReferenciaDatosCatastrosAlmacenados", hs);
		} catch (Exception excp)
		{
		}

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
				.getMainFrame(), geopistaEditor.getContext().getErrorHandler());

		progressDialog.setTitle(aplicacion.getI18nString("ImportandoDatos"));

		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{

				// Wait for the dialog to appear before starting the
				// task. Otherwise
				// the task might possibly finish before the dialog
				// appeared and the
				// dialog would never close. [Jon Aquino]
				new Thread(new Runnable()
				{
					public void run()
					{

						boolean lastValue = geopistaValidatePlugIn.isMakeInsertion();
						boolean manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
						boolean deleteNotIncluded = ((Boolean) blackboard.get("borrarNoIncluidas")).booleanValue();

						boolean firingEvents = false;
						try
						{
							//numeroRegistrosLeidos = 1;
							//registrosCorrectos = 0;
							//registrosErroneos = 0;
							progressDialog.report(aplicacion.getI18nString("CargandoMapaInforeferencia"));
							// Leemos el mapa
							// Cargar la capa auxiliar.
							if (capaParcelas == null)
							{
								try
								{                                        	
									geopistaEditor.loadMap(aplicacion.getString("url.mapa.inforeferencia"));
								}
								catch(Exception e)
								{
									JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("errorCargaMapa"));
									throw e;
								}
								// seleccionamos la capa de parcelas.
								capaParcelas = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer("parcelas");
								capaParcelas.setActiva(false);
								capaParcelas.setVisible(false);
								firingEvents = capaParcelas.getLayerManager().isFiringEvents();
							}

							//Urbana
							capaParcelasFuente = (GeopistaLayer) blackboard.get("capaParcelasInfoReferenciaUrbana");
							loadLayer (capaParcelasFuente, true, deleteNotIncluded);

							//Rustica
							capaParcelasFuente = (GeopistaLayer) blackboard.get("capaParcelasInfoReferencia");
							loadLayer (capaParcelasFuente, false, deleteNotIncluded);

							deleteFeatures (deleteNotIncluded);


						} catch (Exception e){
							e.printStackTrace();
							JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("SeHanProducidoErrores"));
							wizardContext.cancelWizard();
							return;
						} finally
						{
							try
							{
								geopistaValidatePlugIn.setMakeInsertion(lastValue);
								capaParcelas.getLayerManager().setFiringEvents(firingEvents);
							}catch(Exception e)
							{
								//si se producen problemas tenemos que seguir para cerrar la ventana taskmonitor
							}
							progressDialog.setVisible(false);
						}
					}
					private void deleteFeatures(boolean deleteNotIncluded) {

						//Una vez guardados los registros en base de datos, si se ha optado por eliminar los que 
						//no están en los ficheros recién importados, se borran los que no tienen el campo "modificado" a 1
						if (deleteNotIncluded)
						{	
							List listaLayerParcelas = new ArrayList (capaParcelas.getFeatureCollectionWrapper().getFeatures());
							Vector v = new Vector();
							
							Iterator it = listaLayerParcelas.iterator();
							while (it.hasNext())
							{
								GeopistaFeature gf = (GeopistaFeature)it.next();
								if (gf.getAttribute(((GeopistaSchema) capaParcelas.getFeatureCollectionWrapper().
										getFeatureSchema()).getAttributeByColumn("modificado")).toString().equals("0"))
								{
									
									Feature currentFeature = searchRefCatastral(gf, geopistaFeatureComparator);
									currentFeature.setAttribute(((GeopistaSchema) capaParcelas.getFeatureCollectionWrapper().
														getFeatureSchema()).getAttributeByColumn("fecha_baja"), 
													//new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
														new Date());
									
									capaParcelas.getFeatureCollectionWrapper().add(currentFeature);
									
								}
							}
							
							progressDialog.report(aplicacion.getI18nString("importar.parcelas.GrabandoDatosBaseDatos"));

							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaParcelas
							.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
							try
							{
								driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(false));
								geopistaServerDataSource.getConnection().executeUpdate(capaParcelas.getDataSourceQuery().getQuery(),
										capaParcelas.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
							}
							catch (Exception e){
								e.printStackTrace();
							}
							finally
							{
								if(lastResfreshValue!=null)
								{
									driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,lastResfreshValue);
								}
								else
								{
									driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
								}
							}							
						}						
						
					}
					private void loadLayer(GeopistaLayer capaParcelasFuente, 
							boolean isUrbana, boolean deleteNotIncluded	) throws Exception {

						boolean firingEvents = false;

						if(capaParcelasFuente!=null) 
						{	
							int selectedValue = 0;
							
							if (selectedValue == CANCELAR)
							{
								wizardContext.cancelWizard();
								return;
							}

							firingEvents = capaParcelas.getLayerManager().isFiringEvents();
							capaParcelas.getLayerManager().setFiringEvents(false);
							geopistaValidatePlugIn.setMakeInsertion(false);

							//sacamos el esquema de la capa parcelas 
							GeopistaSchema featureSchema = (GeopistaSchema) capaParcelas
							.getFeatureCollectionWrapper().getFeatureSchema();

							// Extraemos los nombres en el locale actual de los atributos
							String primerNumeroAttName = featureSchema.getAttributeByColumn("primer_numero");
							String fechaAltaAttName = featureSchema.getAttributeByColumn("fecha_alta");
							String fechaBajaAttnName = featureSchema.getAttributeByColumn("fecha_baja");
							String modificadoAttnName = featureSchema.getAttributeByColumn("modificado");
							refCatAttName = featureSchema.getAttributeByColumn("referencia_catastral");
							//String tipoAttName = featureSchema.getAttributeByColumn("tipo");
							String idViaAttName = featureSchema.getAttributeByColumn("id_via");
							
							List listaLayerParcelas = capaParcelas.getFeatureCollectionWrapper().getFeatures();

							// Configures cache of features
							setSearchableList(listaLayerParcelas, geopistaFeatureComparator);

							// Pasamos los valores
							// Recorrer todas las features del Layer.
							List listaLayer = capaParcelasFuente.getFeatureCollectionWrapper().getFeatures();
							Iterator itLayer = listaLayer.iterator();
							progressDialog.report(aplicacion.getI18nString("importar.parcelas.ImportandoDatos"));
							while (itLayer.hasNext())
							{
								progressDialog.report(numeroRegistrosLeidos,listaLayer.size(),aplicacion.getI18nString("importar.parcelas.ImportandoEntidad"));
								// Obtener una feature
								Feature f = (Feature) itLayer.next();
								String masa = f.getString("MASA").trim();
								String hoja = f.getString("HOJA").trim();
								String parcela = f.getString("PARCELA").trim();
								String tipo = f.getString("TIPO").trim();
								String idVia = f.getString("VIA").trim();
								String primerNumero = f.getString("NUMERO").trim();
								Geometry geometria = f.getGeometry();

								String refCat = null;
								if (tipo.equals("U")){

									masa = GeopistaFunctionUtils.completarConCeros(masa.trim(), 5);
									if(masa.length()>5) 
										masa = masa.substring(masa.length()-5);

									parcela = GeopistaFunctionUtils.completarConCeros(parcela.trim(), 2);
									if(parcela.length()>2) 
										parcela = parcela.substring(parcela.length()-2);

									hoja = GeopistaFunctionUtils.completarConCeros(hoja.trim(), 7);
									if(hoja.length()>7) 
										hoja = hoja.substring(hoja.length()-7);

									refCat = (masa.trim() + parcela.trim() + hoja.trim()).trim();
								}
								else if (tipo.equals("D")){
									//Para los diseminados la referencia catastral se compone de forma distinta la de
									//urbana. 

									masa = GeopistaFunctionUtils.completarConCeros(masa.trim(), 4);
									if(masa.length()>4) 
										masa = masa.substring(masa.length()-4);

									parcela = GeopistaFunctionUtils.completarConCeros(parcela.trim(), 3);
									if(parcela.length()>3) 
										parcela = parcela.substring(parcela.length()-3);

									hoja = GeopistaFunctionUtils.completarConCeros(hoja.trim(), 7);
									if(hoja.length()>7) 
										hoja = hoja.substring(hoja.length()-7);

									refCat = (masa.trim() + parcela.trim() + hoja.trim()).trim();
								}
								else if (tipo.equals("R")||tipo.equals("X")){

									String delegacion = f.getAttribute("DELEGACIO").toString();
									delegacion = GeopistaFunctionUtils.completarConCeros(delegacion.trim(), 2);
									if(delegacion.length()>2) 
										delegacion = delegacion.substring(delegacion.length()-2);

									String municipio =f.getAttribute("MUNICIPIO").toString();
									municipio = GeopistaFunctionUtils.completarConCeros(municipio.trim(), 3);
									if(municipio.length()>3) 
										municipio = municipio.substring(municipio.length()-3);

									masa = GeopistaFunctionUtils.completarConCeros(masa.trim(), 3);
									if(masa.length()>3) masa = masa.substring(masa.length()-3);

									parcela = GeopistaFunctionUtils.completarConCeros(parcela.trim(), 5);
									if(parcela.length()>5) 
										parcela = parcela.substring(parcela.length()-5);

									hoja = hoja.trim();
									try{
										int numeroHoja = Integer.parseInt(hoja);
										if (numeroHoja == 0){
											hoja = "A";
										}
									}
									catch(Exception e){

									}
									if(hoja.length()>1) hoja = hoja.substring(hoja.length()-1);

									refCat = delegacion.trim() + municipio.trim() + hoja.trim() + masa.trim() + parcela.trim();
								}

								// Fecha Alta
								DateFormat sourceDateFormat = new SimpleDateFormat("yyyyMMdd");
								DateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy");

								Date dateAlta = null;
								String initDate = null;
								Date dateBaja = null;
								String endDate = null;

								try
								{
									dateAlta = (Date) sourceDateFormat.parse(f.getString("FECHAALTA"));
									initDate = targetDateFormat.format(dateAlta);
								}
								catch(Exception e)
								{
									//si se produce algún error con el formato de la fecha la ponemos a null;
									if (logger.isDebugEnabled())
									{
										logger
										.debug("run() - Error al traspasar la fecha. Se considera fecha Null : Feature f = "
												+ f
												+ ", Date dateAlta = "
												+ dateAlta
												+ ", String initDate = "
												+ initDate);
									}
								}

								if(!f.getString("FECHABAJA").trim().equals("99999999"))
								{
									try
									{
										dateBaja = (Date) sourceDateFormat.parse(f.getString("FECHABAJA"));
										endDate = targetDateFormat.format(dateBaja);
									}catch(Exception e)
									{
										//si se produce algún error con el formato de la fecha la ponemos a null;

										if (logger.isDebugEnabled())
										{
											logger
											.debug("run() - Error al traspasar la fecha. Se considera fecha Null : Feature f = "
													+ f
													+ ", Date dateBaja = "
													+ dateBaja
													+ ", String endDate = "
													+ endDate);
										}
									}
								}

								/*
								 * Busca la referencia catastral entre las existentes ya en el mapa
								 */
								boolean makeInsertion = false;
								Feature currentFeature = searchRefCatastral(f, featureComparator);

								// Si NO la ha encontrado actualizar los valores y dejar el ID.
								if (currentFeature == null)
								{             
									//String refCat = (masa + parcela + hoja).trim();
									currentFeature = new BasicFeature(featureSchema);
									currentFeature.setAttribute(refCatAttName, refCat);
									makeInsertion=true;
								}


								currentFeature.setGeometry(geometria);							
								currentFeature.setAttribute(idViaAttName, idVia);
								currentFeature.setAttribute(primerNumeroAttName, primerNumero);
								currentFeature.setAttribute(fechaAltaAttName,dateAlta);
								currentFeature.setAttribute(fechaBajaAttnName,dateBaja);    								
								currentFeature.setAttribute(modificadoAttnName, new Integer(deleteNotIncluded?1:0));
								
								((GeopistaFeature) currentFeature).setLayer(capaParcelas);

								/*
								 * Valida manualmente.
								 */
								boolean validateResult = false;
								boolean cancelImport = false;

								SchemaValidator validator= new SchemaValidator(null);

								while(!(validateResult = validator.validateFeature(currentFeature)))
								{								
									FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(capaParcelas, currentFeature);
									if (featureDialog.wasOKPressed())
									{
										Feature clonefeature = featureDialog.getModifiedFeature();
										currentFeature.setAttributes(clonefeature.getAttributes());
									}
									else
									{
										Object[] possibleValues = { aplicacion.getI18nString("CancelarEsteElemento"), aplicacion.getI18nString("CancelarTodaLaImportacion"), aplicacion.getI18nString("IgnorarFuturosErrores") };
										int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
												possibleValues, possibleValues[0]);
										//if(selectedValueCancel==2) 
										//	manualModification=false;
										if(selectedValueCancel==1) 
											cancelImport=true;
										break;
									}
								}

								if(validateResult)
								{
									registrosCorrectos++;
								}
								else
								{
									registrosErroneos++;  
								}
								if(cancelImport==true) 
									break;

								/**
								 * Fin de la validación manual
								 */
								if(makeInsertion && validateResult)
								{
									capaParcelas.getFeatureCollectionWrapper().add(currentFeature);
								}

								numeroRegistrosLeidos++;
							} 

							progressDialog.report("Grabando");

							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaParcelas
							.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
							try
							{
								driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(true));
								AppContext.getApplicationContext().getBlackboard().put(AppContext.IMPORTACIONES, true);
								geopistaServerDataSource.getConnection().executeUpdate(capaParcelas.getDataSourceQuery().getQuery(),
										capaParcelas.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
							}
							catch (Exception e){
								e.printStackTrace();
							}
							finally
							{
								if(lastResfreshValue!=null)
								{
									driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,lastResfreshValue);
								}
								else
								{
									driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
								}
							}
							

							// ponemos en el panel los valores de registros leidos y fecha y
							// Hora

							DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
							String fechaFinalizacion = (String) formatter.format(new Date());

							//
							cadenaTexto = // cadenaTexto +
								aplicacion.getI18nString("importar.progreso.numero.leidos")
								+ (numeroRegistrosLeidos-1);

							cadenaTexto = cadenaTexto
							+ aplicacion.getI18nString("importar.progreso.numero.insertados")
							+ registrosCorrectos;

							cadenaTexto = cadenaTexto
							+ aplicacion.getI18nString("importar.progreso.numero.no.insertados")
							+ registrosErroneos;

							cadenaTexto = cadenaTexto
							+ aplicacion.getI18nString("importar.progreso.fecha.fin")
							+ fechaFinalizacion;
							jedResumen.setText(cadenaTexto);

							// fin pasar los valores
						}
					}
					/**
					 * genera la cache para realizar las busquedas de catastro
					 * @see GeopistaMostrarParcelas.searchRefCatastral
					 */
					private Feature[] cache;
					private void setSearchableList(List listaLayerParcelas,Comparator comparator)
					{
						cache = new Feature[listaLayerParcelas.size()];

						int i=0;
						for (Iterator features = listaLayerParcelas.iterator(); features.hasNext();)
						{
							Feature feature = (Feature) features.next();
							cache[i++]=feature;
						}
						Arrays.sort(cache,comparator);
					}
					/**
					 * utiliza una cache para realizar la busqueda por identificador catastral
					 * @param featureSchema
					 * @param listaLayerParcelas
					 * @param refCat
					 * @return
					 */


					private Feature searchRefCatastral(Feature refCatFeature, Comparator comparator)
					{
						int featureIndex = Arrays.binarySearch(cache, refCatFeature, comparator);
						if(featureIndex<0) 
							return null;

						return  cache[featureIndex];
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);

		if (capaParcelas!=null)
			capaParcelas.setVisible(true);

	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception
	{
		blackboard.put("capaParcelasInfoReferencia",null);
		blackboard.put("capaParcelasInfoReferenciaUrbana",null);
		geopistaEditor = null;
		capaParcelas = null;
		capaParcelasFuente = null;
	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * 
	 * @param listener
	 *            a party to notify when the input changes (usually the
	 *            WizardDialog, which needs to know when to update the enabled
	 *            state of the buttons.
	 */
	public void add(InputChangedListener listener)
	{

	}

	public void remove(InputChangedListener listener)
	{

	}

	public String getTitle()
	{
		return this.getName();
	}

	public String getID()
	{
		return "2";
	}

	public String getInstructions()
	{
		return " ";
	}

	public boolean isInputValid()
	{
		return true;
	}

	public void setNextID(String nextID)
	{
		this.nextID = nextID;
	}

	/**
	 * @return null to turn the Next button into a Finish button
	 */
	public String getNextID()
	{
		return nextID;
	}

	public void setWizardContext(WizardContext wd)
	{
		this.wizardContext = wd;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting()
	{	
		// TODO Auto-generated method stub

	}
	
	Comparator geopistaFeatureComparator = new Comparator(){
		public int compare(Object o1, Object o2) {
			Feature f1 = (Feature) o1;
			Feature f2 = (Feature) o2;

			String f1ReferenciaCatastral = f1.getString(refCatAttName);
			String f2ReferenciaCatastral = f2.getString(refCatAttName);

			return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
		}
	};

	Comparator featureComparator = new Comparator(){
		public int compare(Object o1, Object o2) {
			Feature f1 = (Feature) o1;
			Feature f2 = (Feature) o2;

			String f1ReferenciaCatastral = f1.getString(refCatAttName);
			String masa = f2.getString("MASA");
			String hoja = f2.getString("HOJA");
			String tipo = f2.getString("TIPO");
			String parcela = f2.getString("PARCELA");
			String f2ReferenciaCatastral = null;

			if(tipo.equals("U")|| tipo.equals("D")){

				masa = GeopistaFunctionUtils.completarConCeros(masa.trim(), 5);
				if(masa.length()>5) 
					masa = masa.substring(masa.length()-5);

				parcela = GeopistaFunctionUtils.completarConCeros(parcela.trim(), 2);
				if(parcela.length()>2) 
					parcela = parcela.substring(parcela.length()-2);

				hoja = GeopistaFunctionUtils.completarConCeros(hoja.trim(), 7);
				if(hoja.length()>7) 
					hoja = hoja.substring(hoja.length()-7);

				f2ReferenciaCatastral = (masa.trim() + parcela.trim() + hoja.trim()).trim();
			}
			else if (tipo.equals("R")|| tipo.equals("X")){

				String delegacion = f2.getAttribute("DELEGACIO").toString();
				delegacion = GeopistaFunctionUtils.completarConCeros(delegacion.trim(), 2);
				if(delegacion.length()>2) 
					delegacion = delegacion.substring(delegacion.length()-2);

				String municipio = f2.getAttribute("MUNICIPIO").toString();
				municipio = GeopistaFunctionUtils.completarConCeros(municipio.trim(), 3);
				if(municipio.length()>3) 
					municipio = municipio.substring(municipio.length()-3);

				masa = GeopistaFunctionUtils.completarConCeros(masa.trim(), 3);
				if(masa.length()>3) 
					masa = masa.substring(masa.length()-3);

				parcela = GeopistaFunctionUtils.completarConCeros(parcela.trim(), 5);
				if(parcela.length()>5) 
					parcela = parcela.substring(parcela.length()-5);

				hoja = hoja.trim();
				try{
					int numeroHoja = Integer.parseInt(hoja);
					if (numeroHoja == 0){
						hoja = "A";
					}
				}
				catch(Exception e){

				}
				if(hoja.length()>1) 
					hoja = hoja.substring(hoja.length()-1);

				f2ReferenciaCatastral = delegacion.trim() + municipio.trim() + hoja.trim() + masa.trim() + parcela.trim();

			}

			return f1ReferenciaCatastral.compareToIgnoreCase(f2ReferenciaCatastral);
		}
	};

} // de la clase general
