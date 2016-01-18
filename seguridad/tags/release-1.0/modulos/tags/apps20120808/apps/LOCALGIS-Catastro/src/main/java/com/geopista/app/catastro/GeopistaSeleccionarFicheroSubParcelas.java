package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Clase utilizada para importar el fichero de Subparcelas
 * por ahor este fichero se llama construcciones.shp
 */

public class GeopistaSeleccionarFicheroSubParcelas extends JPanel implements WizardPanel
{
    private static final Log logger = LogFactory.getLog(GeopistaSeleccionarFicheroSubParcelas.class);
    private boolean permiso = true;
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    private GeopistaEditor geopistaEditor = null;
    private JScrollPane scpErrores = new JScrollPane();
    private JLabel lblImagen = new JLabel();
    private JLabel lblFichero = new JLabel();
    private JTextField txtFichero = new JTextField();
    private JButton btnAbrir = new JButton();
    private JLabel lblErrores = new JLabel();
    private boolean hayErroresFilas = false;
    private JLabel lblTipoFichero = new JLabel();
    private JComboBox cmbTipoInfo = new JComboBox();
    private JEditorPane ediError = new JEditorPane();

    private String cadenaTexto = null;
    private JRadioButton rdbIgnorar = new JRadioButton();
    private JRadioButton rdbMostrar = new JRadioButton();
    
    private int valor_combo = 0;
    public static final int CONSTRUCCIONES = 0;

    public static final int CULTIVOS = 1;

    /**
     * Constructor de la clase
     */
    public GeopistaSeleccionarFicheroSubParcelas()
    {
        try{
           setName(aplicacion.getI18nString("importar.asistente.subparcelas.titulo.1"));
           jbInit();
        }catch (Exception e){
           logger.error("Error al inicializar el importador de subparcelas");
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
        //Para que salga la pantalla del relojito lo ponemos to do en el run
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
                            if(geopistaEditor==null)
                                geopistaEditor = new GeopistaEditor("workbench-properties-simple.xml");
                            blackboard.put("capaParcelasInfoReferencia", null);
                            blackboard.put("geopistaEditorInfoReferencia", geopistaEditor);
                            scpErrores.setBounds(new Rectangle(135, 275, 595, 235));
                            cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.construcciones"));
                            cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informe.cultivos"));
                            btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
                            //lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
                            lblImagen.setIcon(IconLoader.icon("catastro.png"));
                            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                            lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                            lblFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.fichero.importar"));
                            lblFichero.setBounds(new Rectangle(135, 210, 140, 20));
                            txtFichero.setBounds(new Rectangle(290, 210, 365, 20));
                            txtFichero.setEditable(false);
                            btnAbrir.setBounds(new Rectangle(670, 205, 25, 25));
                            btnAbrir.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    abrir();}});
                            lblErrores.setText(aplicacion.getI18nString("importar.informacion.referencia.errores.validacion"));
                            lblErrores.setBounds(new Rectangle(135, 255, 250, 20));
                            lblTipoFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.tipo.fichero"));
                            lblTipoFichero.setBounds(new Rectangle(135, 180, 140, 20));
                            cmbTipoInfo.setBackground(new Color(255, 255, 255));
                            cmbTipoInfo.setBounds(new Rectangle(290, 180, 405, 20));
                            cmbTipoInfo.addActionListener(new ActionListener(){
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                cmbTipoFichero();
                                            }});
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
                            lbldesc.setText(aplicacion.getI18nString("importar.asistente.descripcion.subparcelas"));
                            lbldesc.setBounds(new Rectangle(135, 150, 600, 20));
                            txtFecha.setText(date);
                            lblFuente.setText(aplicacion.getI18nString("fuente.importacion.caja.texto"));
                            lblPersona.setText(aplicacion.getI18nString("persona.importacion.caja.texto"));
                            lblOrganismo.setText(aplicacion.getI18nString("organismo.importacion.caja.texto"));
                            lblFecha.setText(aplicacion.getI18nString("fecha.importacion.caja.texto"));
                            lblTipo.setText(aplicacion.getI18nString("tipo.importacion.caja.texto"));
                            ButtonGroup grupo = new ButtonGroup();
                            grupo.add(rdbIgnorar);
                            grupo.add(rdbMostrar);
                            rdbIgnorar.setText(aplicacion.getI18nString("importar.ignorar.entidad"));
                            rdbMostrar.setText(aplicacion.getI18nString("importar.mostrar.entidad"));
                            rdbMostrar.setBounds(new java.awt.Rectangle(135, 230, 253, 20));
                            rdbIgnorar.setBounds(new java.awt.Rectangle(431, 230, 301, 20));
                            rdbMostrar.setSelected(true);

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
                            add(cmbTipoInfo, null);
                            add(lblTipoFichero, null);
                            add(lblErrores, null);
                            add(btnAbrir, null);
                            add(txtFichero, null);
                            add(lblFichero, null);
                            add(lblImagen, null);
                            add(rdbIgnorar, null);
                            add(rdbMostrar, null);

                            scpErrores.getViewport().add(ediError, null);
                            add(scpErrores, null);
                            } catch (Exception e)
                            {

                            } finally
                            {
                                progressDialog.setVisible(false);
                            }
                        }
                    }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        //addAyudaOnline();
    }// jbinit

	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarSubparcelas";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});
  	}        
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

        /*try
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
        }*/
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {

        if (rdbIgnorar.isSelected())
        {
            blackboard.put("mostrarError", false);
        } else
        {
            blackboard.put("mostrarError", true);
        }
        
        blackboard.put("tipoSubparcela", cmbTipoInfo.getSelectedItem().toString());
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
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            return false;
        } else
        {
            if (hayErroresFilas)
            {
                return false;
            } else
            {

                if ((txtFichero.getText().length()) == 0)
                {
                    return false;
                } else
                {
                    return true;
                }

            }
        }
    }

    private String nextID = "2";

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

    private JFileChooser fc = new JFileChooser();

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    private void abrir()
    {
        // inicializamos los valores para cada proceso de importacion
        hayErroresFilas = false;

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        valor_combo = cmbTipoInfo.getSelectedIndex();
        
        filter.addExtension("SHP");
        filter.setDescription("Shapefiles");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL // FILES(*.*)

        File currentDirectory = (File) blackboard.get(Constantes.LAST_IMPORT_DIRECTORY);
        fc.setCurrentDirectory(currentDirectory);

        int returnVal = fc.showOpenDialog(this);
        blackboard.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        // Cargamos el fichero que hemos obtenido
        String fichero;
        fichero = fc.getSelectedFile().getPath();
        this.txtFichero.setText(fichero); // meto el fichero seleccionado
                                            // en el campo

        cadenaTexto = "<font face=SansSerif size=3>"
                + aplicacion.getI18nString("ImportacionComenzar") + "<b>" + " "
                + fc.getSelectedFile().getName() + "</b>";
        cadenaTexto = cadenaTexto + "<p>"
                + aplicacion.getI18nString("OperacionMinutos") + " ...</p></font>";
        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.datos.subparcelas");

        ediError.setText(cadenaTexto);
        cadenaTexto="";

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), geopistaEditor.getContext().getErrorHandler());

        progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
        progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
        progressDialog.addComponentListener(new ComponentAdapter(){
        	public void componentShown(ComponentEvent e){
        		new Thread(new Runnable(){
        			public void run(){
        				try{

        					GeopistaLayer layerRef = (GeopistaLayer) blackboard.get("capaParcelasInfoReferencia");
        					if (layerRef != null) geopistaEditor.getLayerManager().remove(layerRef);

        					if(valor_combo == CONSTRUCCIONES){

        						layerRef = (GeopistaLayer) geopistaEditor.loadData(fc.getSelectedFile().getAbsolutePath(),
        								aplicacion.getI18nString("importar.informe.construcciones"));
        						layerRef.setActiva(false);
        						layerRef.addStyle(new BasicStyle(new Color(67, 67,67)));
        						layerRef.setVisible(false);
        						blackboard.put("capaParcelasInfoReferencia", layerRef);
        						// Obtener el esquema
        						FeatureSchema esquema = layerRef.getFeatureCollectionWrapper().getFeatureSchema();
        						// Localizar los campos
        						// encontrarCampo("HOJA",esquema) //por ahora el campo hoja no se encuentra en subparcelas
        						if (encontrarCampo("MASA",esquema) && encontrarCampo("PARCELA",esquema)
        								&& encontrarCampo("TIPO",esquema)&& encontrarCampo("FECHAALTA", esquema) 
        								&& encontrarCampo("FECHABAJA", esquema)&& encontrarCampo("CONSTRU", esquema))
        						{
        							// A partir de aqui hay que verificar que no hay nulos y es
        							// del tipo correcto los valores.
        							List listaFeatures = layerRef.getFeatureCollectionWrapper().getFeatures();
        							Iterator itLayer = listaFeatures.iterator();
        							while (itLayer.hasNext()){
        								Feature f = (Feature) itLayer.next();
        								String masa = f.getString("MASA");
        								//String hoja = f.getString("HOJA");
        								String hoja="";
        								String parcela = f.getString("PARCELA");
        								String tipo = f.getString("TIPO");
        								// Comprobamos que no sea nulo y sea una U una R una X o una D
        								if ((!tipo.equals("U")) && (!tipo.equals("R"))&&(!tipo.equals("X"))&&(!tipo.equals("D")))
        								{
        									// Solo puede haber una R o una U.
        									cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informe.parcelas.rustico")
        									+ masa + hoja + parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        									hayErroresFilas = true;
        									continue;
        								}
        								// Comprobamos que la fecha que viene sea fecha
        								try {
        									DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        									formatter.parse(f.getString("FECHAALTA"));
        								} catch (Exception exc){// En la fecha de alta
        									cadenaTexto = cadenaTexto+ aplicacion.getI18nString("importar.informe.parcelas.fecha.alta.validacion")
        									+ masa+ hoja+ parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        									exc.printStackTrace();
        									hayErroresFilas = true;
        									continue;
        								}
        								// Comprobamos que la fecha de baja es nula o valida o 9999999
        								if (f.getString("FECHABAJA")!=null && !f.getString("FECHABAJA").equals("99999999")){
        									// Comprobamos que sea una fecha correcta
        									try {
        										DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        										formatter.parse(f.getString("FECHABAJA"));
        									} catch (Exception excp)
        									{
        										excp.printStackTrace();
        										hayErroresFilas = true;
        										cadenaTexto = cadenaTexto+ aplicacion.getI18nString("importar.informe.parcelas.fecha.baja")
        										+ masa+ hoja + parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        										continue;
        									}
        								}
        							}
        						} else{
        							// Alguno de los campos no están definidos
        							cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");
        							hayErroresFilas = true;
        						}
        					}
        					else if(valor_combo == CULTIVOS){
        						
        						layerRef = (GeopistaLayer) geopistaEditor.loadData(fc.getSelectedFile().getAbsolutePath(),
        								aplicacion.getI18nString("importar.informe.cultivos"));
        						layerRef.setActiva(false);
        						layerRef.addStyle(new BasicStyle(new Color(68, 68,68)));
        						layerRef.setVisible(false);
        						blackboard.put("capaParcelasInfoReferencia", layerRef);
        						// Obtener el esquema
        						FeatureSchema esquema = layerRef.getFeatureCollectionWrapper().getFeatureSchema();
        						// Localizar los campos
        						// encontrarCampo("HOJA",esquema) //por ahora el campo hoja no se encuentra en subparcelas
        						if (encontrarCampo("MASA",esquema) && encontrarCampo("PARCELA",esquema)
        								&& encontrarCampo("TIPO",esquema)&& encontrarCampo("FECHAALTA", esquema) 
        								&& encontrarCampo("FECHABAJA", esquema)&& encontrarCampo("SUBPARCE", esquema))
        						{
        							// A partir de aqui hay que verificar que no hay nulos y es
        							// del tipo correcto los valores.
        							List listaFeatures = layerRef.getFeatureCollectionWrapper().getFeatures();
        							Iterator itLayer = listaFeatures.iterator();
        							while (itLayer.hasNext()){
        								Feature f = (Feature) itLayer.next();
        								String masa = f.getString("MASA");
        								//String hoja = f.getString("HOJA");
        								String hoja="";
        								String parcela = f.getString("PARCELA");
        								String tipo = f.getString("TIPO");
        								// Comprobamos que no sea nulo y sea una U una R una X o una D
        								if ((!tipo.equals("U")) && (!tipo.equals("R"))&&(!tipo.equals("X"))&&(!tipo.equals("D")))
        								{
        									// Solo puede haber una R o una U.
        									cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informe.parcelas.rustico")
        									+ masa + hoja + parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        									hayErroresFilas = true;
        									continue;
        								}
        								// Comprobamos que la fecha que viene sea fecha
        								try {
        									DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        									formatter.parse(f.getString("FECHAALTA"));
        								} catch (Exception exc){// En la fecha de alta
        									cadenaTexto = cadenaTexto+ aplicacion.getI18nString("importar.informe.parcelas.fecha.alta.validacion")
        									+ masa+ hoja+ parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        									exc.printStackTrace();
        									hayErroresFilas = true;
        									continue;
        								}
        								// Comprobamos que la fecha de baja es nula o valida o 9999999
        								if (f.getString("FECHABAJA")!=null && !f.getString("FECHABAJA").equals("99999999")){
        									// Comprobamos que sea una fecha correcta
        									try {
        										DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        										formatter.parse(f.getString("FECHABAJA"));
        									} catch (Exception excp)
        									{
        										excp.printStackTrace();
        										hayErroresFilas = true;
        										cadenaTexto = cadenaTexto+ aplicacion.getI18nString("importar.informe.parcelas.fecha.baja")
        										+ masa+ hoja + parcela + aplicacion.getI18nString("importar.informe.parcelas.fin.rustico");
        										continue;
        									}
        								}
        							}
        						} else{
        							// Alguno de los campos no están definidos
        							cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");
        							hayErroresFilas = true;
        						}

        					}
        				} catch (Exception e){
        					hayErroresFilas = true;
        				} finally
        				{
        					progressDialog.setVisible(false);
        				}
        			}}).start();
        	}
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        if (hayErroresFilas)
        {
             ediError.removeAll();
             cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");
             cadenaTexto = cadenaTexto+ aplicacion.getI18nString("validacion.finalizada");
             ediError.setText(cadenaTexto);
             wizardContext.inputChanged();
        }else
        {
             cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.informe.parcelas.fichero.correcto");
             cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");
        }
        ediError.setText(cadenaTexto);
        wizardContext.inputChanged();

    }

    private void cmbTipoFichero()
    {
        txtFichero.setText("");
    }


    /**
     * Busca un campo en la lista de campos, para hacer la importación del
     * Catastro.
     *
     *
     * @param nombreCampo
     * @param esquema
     * @return
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

    public void exiting()
    {
       //System.exit(0);
        setVisible(false);
    }

} // de la clase general.

