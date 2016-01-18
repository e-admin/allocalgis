package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaSeleccionarFicheroParcelas extends JPanel implements WizardPanel
{
	private boolean permiso = true;
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();
	private GeopistaEditor geopistaEditor = null;
	private JScrollPane scpErrores = new JScrollPane();
	private JLabel lblImagen = new JLabel();
	private JLabel lblFicheroRustica = new JLabel();
	private JTextField txtFicheroRustica = new JTextField();
	private JTextField txtFicheroUrbana = new JTextField();
	private JButton btnAbrirRustica = new JButton();
	private JButton btnAbrirUrbana = new JButton();
	private JLabel lblErrores = new JLabel();
	private boolean hayErroresFilas = false;
	private javax.swing.JLabel jLabel = null;
	private JLabel lblFicheroUrbana = new JLabel();
	private JEditorPane ediError = new JEditorPane();
	boolean hayErroresFila = false;
	boolean campoMasaCorrectos = true;
	boolean campoHojaCorrecto = true;
	boolean campoParecelaCorrecto = true;
	boolean campoTipoCorrecto = true;
	boolean campoFechaAltaCorrecto = true;
	boolean campoFechaBajaCorrecto = true;
	private WorkbenchContext context = null;
	private String cadenaTexto = null;
	private JCheckBox chkDelete = new JCheckBox();
	private WizardContext wizardContext;
	private JLabel lblFuente = new JLabel();
	private JTextField txtFuente = new JTextField();
	private JLabel lblOrganismo = new JLabel();
	private JTextField txtOrganismo = new JTextField();
	private JLabel lblPersona = new JLabel();
	private JTextField txtPersona = new JTextField();
	private JLabel lblFecha = new JLabel();
	private JTextField txtFecha = new JTextField();
	private JLabel lblTipo = new JLabel();
	private JSeparator jSeparator1 = new JSeparator();
	private JSeparator jSeparator4 = new JSeparator();
	private JSeparator jSeparator2 = new JSeparator();
	private JComboBox jComboBox1 = new JComboBox();
	private JLabel lbldesc = new JLabel();
	private JFileChooser fcRustica = new JFileChooser();
	private JFileChooser fcUrbana = new JFileChooser();
	
	boolean rusticaValida = false;
	boolean urbanaValida = false;
	
	private String nextID = "2";


	public GeopistaSeleccionarFicheroParcelas()
	{
		try
		{
			Locale loc=I18N.getLocaleAsObject();         
			ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.language.ImportadorParcelasi18n",loc,this.getClass().getClassLoader());
			I18N.plugInsResourceBundle.put("ImportadorParcelas",bundle);

			setName(aplicacion.getI18nString("importar.asistente.parcelas.titulo.1"));

			jbInit();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setLayout(null);

		this.jComboBox1.addItem(aplicacion.getI18nString("fichero.shape"));

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
							if(geopistaEditor==null)
							{
								geopistaEditor = new GeopistaEditor();
							}
							blackboard.put("capaParcelasInfoReferencia", null);
							blackboard.put("geopistaEditorInfoReferencia", geopistaEditor);
							context = geopistaEditor.getContext();
							scpErrores.setBounds(new Rectangle(135, 275, 595, 235));

							btnAbrirUrbana.setIcon(IconLoader.icon("abrir.gif"));
							btnAbrirRustica.setIcon(IconLoader.icon("abrir.gif"));
							
							lblImagen.setIcon(IconLoader.icon("catastro.png"));
							lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
							lblImagen.setBorder(BorderFactory.createLineBorder(
									Color.black, 1));

							lblFicheroRustica.setText(I18N.get("ImportadorParcelas","importar.informacion.referencia.fichero.rustica"));
							lblFicheroRustica.setBounds(new Rectangle(135, 210, 180, 20));

							txtFicheroRustica.setBounds(new Rectangle(330, 210, 365, 20));
							txtFicheroRustica.setEditable(false);
							txtFicheroRustica.addPropertyChangeListener(new PropertyChangeListener()
							{
								public void propertyChange(
										PropertyChangeEvent e)
								{
									txtFichero_propertyChange(e);
								}
							});

							btnAbrirRustica.setBounds(new Rectangle(700, 205, 25, 25));
							btnAbrirRustica.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									btnAbrirRustica_actionPerformed(e);
								}
							});
							
							btnAbrirUrbana.setBounds(new Rectangle(700, 180, 25, 25));
							btnAbrirUrbana.addActionListener(new ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									btnAbrirUrbana_actionPerformed(e);
								}
							});

							lblErrores.setText(aplicacion.getI18nString("importar.informacion.referencia.errores.validacion"));
							lblErrores.setBounds(new Rectangle(135, 255, 250, 20));

							lblFicheroUrbana.setText(I18N.get("ImportadorParcelas", "importar.informacion.referencia.fichero.urbana"));
							lblFicheroUrbana.setBounds(new Rectangle(135, 180, 180, 20));

							txtFicheroUrbana.setBounds(new Rectangle(330, 180, 365, 20));
							txtFicheroUrbana.setEditable(false);
							
							ediError.setText("jEditorPane1");
							ediError.setContentType("text/html");
							ediError.setEditable(false);
							lblFuente.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.Fuente"));
							lblFuente.setBounds(new Rectangle(135, 25, 95, 20));
							txtFuente.setBounds(new Rectangle(240, 25, 490, 20));
							lblOrganismo.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.Organismo"));
							lblOrganismo.setBounds(new Rectangle(135, 55, 90, 20));
							txtOrganismo.setBounds(new Rectangle(240, 55, 490, 20));
							lblPersona.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.Persona"));
							lblPersona.setBounds(new Rectangle(135, 80, 85, 20));
							txtPersona.setBounds(new Rectangle(240, 80, 490, 20));
							lblFecha.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.Fecha"));
							lblFecha.setBounds(new Rectangle(135, 110, 90, 25));
							txtFecha.setBounds(new Rectangle(240, 110, 145, 20));
							txtFecha.setEditable(false);
							lblTipo.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.Tipo"));
							lblTipo.setBounds(new Rectangle(400, 110, 135, 20));
							jSeparator1.setBounds(new Rectangle(135, 140, 600, 5));

							DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
							String date = (String) formatter.format(new Date());
							jSeparator4.setBounds(new Rectangle(135, 20, 595, 5));
							jSeparator2.setBounds(new Rectangle(135, 255, 605, 2));
							jComboBox1.setBounds(new Rectangle(550, 110, 180, 20));
							lbldesc.setText(aplicacion.getI18nString("importar.asistente.descripcion.parcelas"));
							lbldesc.setBounds(new Rectangle(135, 150, 600, 20));
							txtFecha.setText(date);

							// Paso los textos etiquetas

							lblFuente.setText(aplicacion.getI18nString("fuente.importacion.caja.texto"));
							lblPersona.setText(aplicacion.getI18nString("persona.importacion.caja.texto"));
							lblOrganismo.setText(aplicacion.getI18nString("organismo.importacion.caja.texto"));
							lblFecha.setText(aplicacion.getI18nString("fecha.importacion.caja.texto"));
							lblTipo.setText(aplicacion.getI18nString("tipo.importacion.caja.texto"));

							// cmbTipoInfo.setBounds(283, 22, 290, 20);
							
							chkDelete.setText(I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.texto"));
							chkDelete.setBounds(new java.awt.Rectangle(135, 230, 500, 20));
							chkDelete.setSelected(false);

							setSize(750, 550);
							add(lbldesc, null);
							add(jComboBox1, null);
							add(jSeparator2, null);
							add(jSeparator4, null);
							add(jSeparator1, null);
							add(lblTipo, null);
							add(txtFecha, null);
							add(lblFecha, null);
							add(txtPersona, null);
							add(lblPersona, null);
							add(txtOrganismo, null);
							add(lblOrganismo, null);
							add(txtFuente, null);
							add(lblFuente, null);
							add(txtFicheroUrbana, null);
							add(lblFicheroUrbana, null);
							add(lblErrores, null);
							add(btnAbrirRustica, null);
							add(btnAbrirUrbana, null);
							add(txtFicheroRustica, null);
							add(lblFicheroRustica, null);
							add(lblImagen, null);
							add(chkDelete, null);

							scpErrores.getViewport().add(ediError, null);
							add(scpErrores, null);
						}
						catch (Exception e)
						{

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

	}// jbinit

	public void enteredFromLeft(Map dataMap)
	{
		if(!aplicacion.isLogged())
		{
			aplicacion.login();
		}
		if(!aplicacion.isLogged())
		{
			wizardContext.cancelWizard();
			return;
		}

		GeopistaPermission paso = new GeopistaPermission("Geopista.InfReferencia.ImportarDatosIne");

		permiso = aplicacion.checkPermission(paso, "Informacion de Referencia");

		try
		{
			// Iniciamos la ayuda
			String helpHS = "ayuda.hs";
			ClassLoader c1 = this.getClass().getClassLoader();
			URL hsURL = HelpSet.findHelpSet(c1, helpHS);
			HelpSet hs = new HelpSet(null, hsURL);
			HelpBroker hb = hs.createHelpBroker();
			// fin de la ayuda
			hb.enableHelpKey(this,
					"InformacionReferenciaSeleccionFicheroImportarDGCatastro", hs);
		} catch (Exception excp)
		{
		}
	}

	/**
	 * Called when the user presses Next on this panel
	 */
	public void exitingToRight() throws Exception
	{
		/*
		SwingWorker worker = new SwingWorker() {
		    public Object construct() {
		    	//add the code for the background thread
		    	
		    	
		    }
		    public void finished() {
		    	//code that you add here will run in the UI thread
		    	
		    }
	 
		};
		worker.start();  //Start the background thread


		
	
		/*
		DoShowDialog doShowDialog = new DoShowDialog();
		try {
		   SwingUtilities.invokeAndWait(doShowDialog);
		}
		catch 
		   (java.lang.reflect.
		      InvocationTargetException e) {
		      e.printStackTrace();
		}
*/
		
		
		
		
		
		blackboard.put("mostrarError", true);
		
		if (chkDelete.isSelected())
		{
			//JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
			//		I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso.parcial"));
			
			int answer = JOptionPane.showConfirmDialog(aplicacion.getMainFrame(), I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso"));
		    if (answer == JOptionPane.YES_OPTION) {		        
		    	blackboard.put("borrarNoIncluidas", true);
		    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
		    			I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso.confirmarborrar"));
		    } 
		    else if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CANCEL_OPTION) {
		    	blackboard.put("borrarNoIncluidas", false);
		    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
		    			I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso.denegarborrar"));
		    }						
		}
		else
		{
			blackboard.put("borrarNoIncluidas", false);
		}
				
		if (!rusticaValida || !urbanaValida)
		{			
			/*String text = JOptionPane.showInputDialog(aplicacion.getMainFrame(), 
					I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso.parcial"));
		    if (text == null) 
		    {	
		    	//wizardContext.inputChanged();
		    	return; //throw new Exception();
		    }		*/    
			
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), I18N.get("ImportadorParcelas", "importar.informacion.referencia.borrar.aviso.parcial"));
		} 
		
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
		return "1";
	}

	public String getInstructions()
	{
		return " ";
	}

	public boolean isInputValid()
	{
		if (!permiso)
		{
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoPermisos"));
			return false;
		} 
		else
		{
			if (rusticaValida || urbanaValida)
				return true;
			else
				return false;	
		}
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
		wizardContext = wd;
	}
	
	private void btnAbrirUrbana_actionPerformed(ActionEvent e)
	{
		validateFile (false);
	}

	private void btnAbrirRustica_actionPerformed(ActionEvent e)
	{
		validateFile (true);		
	}
	
	private void validateFile(final boolean isRustica) {
	
		// inicializamos los valores para cada proceso de importacion
		hayErroresFilas = false;
		hayErroresFila = false;

		campoMasaCorrectos = true;
		campoHojaCorrecto = true;
		campoParecelaCorrecto = true;
		campoTipoCorrecto = true;
		campoFechaAltaCorrecto = true;
		campoFechaBajaCorrecto = true;

		File currentDirectory = (File) blackboard.get(Constantes.LAST_IMPORT_DIRECTORY);

		GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
		filter.addExtension("SHP");
        filter.setDescription("Shapefiles");
		
        final JFileChooser jfc;
        final JTextField jtf;
        if (isRustica)
        {
        	jfc = fcRustica;	
        	jtf = this.txtFicheroRustica;
        }
		else
		{
			jfc = fcUrbana;
			jtf = this.txtFicheroUrbana;
		}
				
		// FILES(*.*)
        jfc.setFileFilter(filter);
    	jfc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL
		jfc.setCurrentDirectory(currentDirectory);
		
		int returnVal = jfc.showOpenDialog(this);
		blackboard.put(Constantes.LAST_IMPORT_DIRECTORY, jfc.getCurrentDirectory());

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			// Cargamos el fichero que hemos obtenido
			String fichero;
			fichero = jfc.getSelectedFile().getPath();
			jtf.setText(fichero); // meto el fichero seleccionado
			// en el campo

			cadenaTexto = "<font face=SansSerif size=3>"
				+ aplicacion.getI18nString("ImportacionComenzar") + "<b>" + " "
				+ jfc.getSelectedFile().getName() + "</b>";
			
			cadenaTexto = cadenaTexto + "<p>" + aplicacion.getI18nString("OperacionMinutos") + " ...</p></font>";
			cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.datos.parcelas");

			ediError.setText(cadenaTexto);
			cadenaTexto="";

			final TaskMonitorDialog progressDialog = 
				new TaskMonitorDialog(aplicacion.getMainFrame(), geopistaEditor.getContext().getErrorHandler());

			progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
			progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
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
								GeopistaLayer layerParcelas = (GeopistaLayer) blackboard.get("capaParcelasInfoReferencia");
								if (layerParcelas != null)
								{
									geopistaEditor.getLayerManager().remove(layerParcelas);
								}

								layerParcelas = (GeopistaLayer) geopistaEditor.loadData(
										jfc.getSelectedFile().getAbsolutePath(),
										aplicacion.getI18nString("importar.informe.parcelas"));
								layerParcelas.setActiva(false);
								layerParcelas.addStyle(new BasicStyle(new Color(64, 64,64)));
								layerParcelas.setVisible(false);

								if (isRustica)
									blackboard.put("capaParcelasInfoReferencia", layerParcelas);
								else
									blackboard.put("capaParcelasInfoReferenciaUrbana", layerParcelas);

								// Obtener el esquema
								FeatureSchema esquema = layerParcelas.getFeatureCollectionWrapper().getFeatureSchema();

								// Localizar los campos
								campoMasaCorrectos = encontrarCampo("MASA",esquema);
								campoHojaCorrecto = encontrarCampo("HOJA",esquema);
								campoParecelaCorrecto = encontrarCampo("PARCELA",esquema);
								campoTipoCorrecto = encontrarCampo("TIPO",esquema);
								campoFechaAltaCorrecto = encontrarCampo("FECHAALTA", esquema);
								campoFechaBajaCorrecto = encontrarCampo("FECHABAJA", esquema);

								if (campoMasaCorrectos 
										&& campoHojaCorrecto
										&& campoParecelaCorrecto
										&& campoTipoCorrecto
										&& campoFechaAltaCorrecto
										&& campoFechaBajaCorrecto)
								{
									
									// A partir de aqui hay que
									// verificar que no hay nulos y es
									// del tipo correcto los valores.
									List listaLayer = layerParcelas.getFeatureCollectionWrapper().getFeatures();
									Iterator itLayer = listaLayer.iterator();

									while (itLayer.hasNext())
									{
										if (hayErroresFila)
										{
											// break;
										}
										Feature f = (Feature) itLayer.next();
										String masa = f.getString("MASA");
										String hoja = f.getString("HOJA");
										String parcela = f.getString("PARCELA");

										String tipo = f.getString("TIPO");
										// Comprobamos que no sea nulo y
										// sea una U o una R
										if ((!tipo.equals("U"))
												&& (!tipo.equals("R"))
												&& (!tipo.equals("D"))
												&& (!tipo.equals("X")))
										{
											// Solo puede haber una R 
											// una U una D o una X.
											cadenaTexto = cadenaTexto
											+ aplicacion.getI18nString("importar.informe.parcelas.rustico")
											+ masa
											+ hoja
											+ parcela
											+ aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
											hayErroresFila = true;
											hayErroresFilas = true;
											// break;
										} 
										else if (!isRustica && (tipo.equals("R") || tipo.equals("D") || tipo.equals("X")))
										{
											cadenaTexto = I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.inicio.parcelario")
											+ I18N.get("ImportadorParcelas", "importar.informacion.referencia.validacion.urbana") + 
											I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.fin.parcelario");
											hayErroresFila = true;
											hayErroresFilas = true;											
										}
										else if (isRustica && tipo.equals("U"))
										{
											cadenaTexto = I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.inicio.parcelario")
											+ I18N.get("ImportadorParcelas", "importar.informacion.referencia.validacion.rustica") + 
											I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.fin.parcelario");
											hayErroresFila = true;
											hayErroresFilas = true;											
										}
										else
										{
											// Comprobamos que la fecha
											// que viene sea fecha
											try
											{
												DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
												Date date = (Date) formatter.parse(f.getString("FECHAALTA"));
												// Comprobamos que la
												// fecha de baja es nula
												// o valida o 9999999
												if (f.getString("FECHABAJA").equals("99999999"))
												{
													// Correcto
													hayErroresFila = false;
												} 
												else
												{
													if ((f.getString("FECHABAJA")) == null)
													{
														hayErroresFila = false;
													} 
													else
													{
														// Comprobamos que sea una fecha correcta
														try
														{
															DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
															Date date1 = (Date) formatter1.parse(f.getString("FECHABAJA"));
														} 
														catch (Exception excp)
														{
															excp.printStackTrace();
															hayErroresFila = true;
															hayErroresFilas = true;
															cadenaTexto = cadenaTexto
															+ aplicacion.getI18nString("importar.informe.parcelas.fecha.baja")
															+ masa
															+ hoja
															+ parcela
															+ aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
															// break;
														}
													}
												}
												//jtf.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
												if (isRustica)
													rusticaValida = true;
												else
													urbanaValida = true;

											} 
											catch (Exception exc)
											{
												// En la fecha de alta
												cadenaTexto = cadenaTexto
												+ aplicacion.getI18nString("importar.informe.parcelas.fecha.alta.validacion")
												+ masa
												+ hoja
												+ parcela
												+ aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
												exc.printStackTrace();
												hayErroresFila = true;
												hayErroresFilas = true;
												// break;
											}
											
											//Se comprueba si la geometría es de tipo polygon y no es empty (únicas válidas en la capa parcelas)
											if (!(f.getGeometry() instanceof Polygon)
													|| f.getGeometry().isEmpty())
											{												
												cadenaTexto = I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.inicio.parcelario")
												+ I18N.get("ImportadorParcelas", "importar.informacion.referencia.validacion.geometria") + 
												I18N.get("ImportadorParcelas", "importar.informe.parcelas.error.fin.parcelario");
												
												hayErroresFila = true;
												hayErroresFilas = true;
											}											
										}
									}
								} 
								else
								{
									// Alguno de los campos no están
									// definidos
									// JOptionPane.showMessageDialog(this,aplicacion.getI18nString("importar.informe.parcelas.algun.campo"));
									cadenaTexto += aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");
									hayErroresFilas = true;									
									
									if (isRustica)
										rusticaValida = false;
									else
										urbanaValida = false;	
								}
							} 
							catch (Exception e)
							{
								hayErroresFilas = true;
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

			if (hayErroresFilas)
			{
				jtf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");			
			}
			else
			{
				jtf.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
				cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informe.parcelas.fichero.correcto")
				+ aplicacion.getI18nString("validacion.finalizada");
			}
			
			ediError.setText(cadenaTexto);
			wizardContext.inputChanged();
		}

	}

	private void cmbTipoFichero_actionPerformed(ActionEvent e)
	{
		txtFicheroRustica.setText("");
	}

	private void txtFichero_propertyChange(PropertyChangeEvent e)
	{

	}

	/**
	 * Busca un campo en la lista de campos, para hacer la importación del
	 * Catastro.
	 * 
	 * @param String
	 *            nombreCampo, campo a buscar en el esquema
	 * @param FeatureSchema
	 *            esquema, nombre del esquema donde se buscará el campo
	 * @return boolean devuelve true si lo encuentra, false en caso contrario
	 */

	public boolean encontrarCampo(String nombreCampo, FeatureSchema esquema)
	{
		boolean encontrado = false;
		// Primero Campos referentes a la parcela catastral.
		for (int i = 0; i < esquema.getAttributeCount(); i++)
		{
			if (esquema.getAttributeName(i).equals(nombreCampo))
			{
				encontrado = true;
				break;
			}
		}
		return encontrado;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting()
	{
		// TODO Auto-generated method stub

	}
	/*
	
	class DoShowDialog implements Runnable {
		   boolean proceedConfirmed;
		   public void run() {
		      Object[] options = {"Continue", "Cancel"};
		         int n = JOptionPane.showOptionDialog
		         (GeopistaSeleccionarFicheroParcelas.this,
		         "Example2: Continue?",
		         "Example2",
		         JOptionPane.YES_NO_OPTION,
		         JOptionPane.QUESTION_MESSAGE,
		            null,
		            options,
		            "Continue");
		         proceedConfirmed =
		            (n == JOptionPane.YES_OPTION);
		   }
		}
*/
	

} // de la clase general.

