package com.geopista.app.inforeferencia;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.geopista.app.AppContext;
import javax.swing.JSeparator;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.inicio.GeopistaInicio;
import com.geopista.editor.GeopistaEditor;
import com.geopista.io.GeoGMLInputTemplate;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.geotools.dbffile.DbfFile;

import javax.swing.JTextArea;
import javax.swing.JMenuItem;
import com.vividsolutions.jump.util.Blackboard;

/**
 * GeopistaImportarToponimosPanel
 * 
 * Pide el fichero de toponimos que se va a importar
 * 
 */
public class GeopistaImportarToponimosPanel1 extends JPanel implements WizardPanel
{
    ApplicationContext aplicacion = AppContext.getApplicationContext();

    private boolean hayErrores = false;

    private Blackboard blackboard = aplicacion.getBlackboard();

    private WizardContext wizardContext;

    private GeopistaLayer layer08 = null;

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JSeparator jSeparator1 = new JSeparator();

    private JSeparator jSeparator3 = new JSeparator();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private JLabel lblFichero = new JLabel();

    private JTextField txtFichero = new JTextField();

    private JButton btnAbrir = new JButton();

    private JLabel lblErrores = new JLabel();

    private javax.swing.JLabel lblFuente = new JLabel(aplicacion
            .getI18nString("GeopistaImportarToponimosPanel1.Fuente"));

    private JEditorPane ediError = new JEditorPane();

    private JComboBox jComboBox1 = new JComboBox();

    private JTextField txtFuente = new JTextField();

    private JTextField txtOrganismo = new JTextField();

    private JTextField txtPersona = new JTextField();

    private JTextField txtFecha = new JTextField();

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor();

    private String fichero;

    private boolean permiso = true;

    private JFileChooser fc = new JFileChooser();

    private String cadenaTexto = null;
    
    private JRadioButton rdbIgnorar = new JRadioButton();

    private JRadioButton rdbMostrar = new JRadioButton();

    public GeopistaImportarToponimosPanel1()
        {
            try
            {
                jbInit();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    private void jbInit() throws Exception
    {
        setName(aplicacion.getI18nString("importar.asistente.toponimos.titulo.1"));
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
                                    lblTipo = new JLabel();
                                    lblComent = new JLabel();
                                    lblFecha = new JLabel();
                                    lblPersona = new JLabel();
                                    lblOrganismo = new JLabel();
                                    setLayout(null);

                                    // this.jComboBox1.addItem(appContext.getI18nString("fichero.jml"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.shape"));
                                    /*
                                     * this.jComboBox1.addItem(appContext.getI18nString("fichero.dbf"));
                                     * this.jComboBox1.addItem(appContext.getI18nString("fichero.texto"));
                                     * this.jComboBox1.addItem(appContext.getI18nString("fichero.otros"));
                                     */
                                    jSeparator1
                                            .setBounds(new Rectangle(135, 145, 600, 5));
                                    jSeparator3
                                            .setBounds(new Rectangle(140, 240, 590, 5));
                                    lblComent.setBounds(140, 155, 600, 25);
                                    lblComent
                                            .setText(aplicacion
                                                    .getI18nString("importar.toponimos.descripcion.fichero"));
                                    add(jSeparator1);
                                    add(jSeparator3);
                                    add(lblComent, null);
                                    add(jComboBox1);
                                    scpErrores.setBounds(new java.awt.Rectangle(135, 270,
                                            595, 240));
                                    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));

                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));

                                    lblFichero
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.FicheroImportar"));
                                    lblFichero.setBounds(new java.awt.Rectangle(139, 190,
                                            140, 20));

                                    txtFichero.setBounds(new java.awt.Rectangle(285, 190,
                                            407, 20));

                                    btnAbrir.setBounds(new java.awt.Rectangle(698, 188,
                                            35, 25));
                                    btnAbrir.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnAbrir_actionPerformed(e);
                                            }
                                        });

                                    lblErrores
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.ErroresValidación"));
                                    lblErrores.setBounds(new java.awt.Rectangle(135, 250,
                                            410, 20));
                                    lblTipo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.tipo"));
                                    lblTipo.setBounds(new Rectangle(430, 120, 115, 20));
                                    lblTipo
                                            .setText(aplicacion
                                                    .getI18nString("tipo.importacion.caja.texto"));
                                    txtFuente.setBounds(new Rectangle(240, 30, 490, 20));
                                    txtOrganismo
                                            .setBounds(new Rectangle(240, 60, 490, 20));
                                    txtPersona.setBounds(new Rectangle(240, 90, 490, 20));
                                    txtFecha.setBounds(new Rectangle(240, 120, 145, 20));
                                    txtFecha.setEditable(false);
                                    jSeparator1
                                            .setBounds(new Rectangle(135, 145, 600, 5));
                                    lblFecha
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Fecha"));
                                    lblFecha.setBounds(new Rectangle(135, 118, 90, 25));
                                    lblFecha
                                            .setText(aplicacion
                                                    .getI18nString("fecha.importacion.caja.texto"));
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Persona"));
                                    lblPersona.setBounds(new Rectangle(135, 90, 85, 20));
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("persona.importacion.caja.texto"));
                                    lblOrganismo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Organismo"));
                                    lblOrganismo
                                            .setBounds(new Rectangle(135, 60, 90, 20));
                                    lblOrganismo
                                            .setText(aplicacion
                                                    .getI18nString("organismo.importacion.caja.texto"));
                                    lblFuente
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Fuente"));
                                    lblFuente.setBounds(135, 29, 95, 20);
                                    lblFuente
                                            .setText(aplicacion
                                                    .getI18nString("fuente.importacion.caja.texto"));
                                    DateFormat formatter = new SimpleDateFormat(
                                            "dd-MMM-yy");
                                    String date = (String) formatter.format(new Date());
                                    jComboBox1
                                            .setBounds(new Rectangle(550, 120, 180, 20));
                                    txtFecha.setText(date);

                                    // Cargamos el mapa de toponimos

                                    setSize(750, 600);
                                    add(lblErrores, null);
                                    add(btnAbrir, null);
                                    add(txtFichero, null);
                                    add(lblFichero, null);
                                    add(lblImagen, null);
                                    add(lblFuente, null);
                                    add(lblOrganismo, null);
                                    scpErrores.getViewport().add(ediError, null);
                                    add(scpErrores, null);

                                    ediError.setContentType("text/html");
                                    ediError.setEditable(false);
                                    ediError.setEnabled(false);
                                    lblOrganismo.setBounds(135, 60, 90, 20);
                                    lblOrganismo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Organismo"));
                                    lblPersona.setBounds(135, 90, 85, 20);
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Persona"));
                                    lblFecha.setBounds(135, 118, 90, 25);
                                    lblFecha
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Fecha"));
                                    lblTipo.setBounds(430, 120, 115, 20);
                                    lblTipo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarToponimosPanel1.Tipo"));
                                    
                                    ButtonGroup grupo = new ButtonGroup();
                                    grupo.add(rdbIgnorar);
                                    grupo.add(rdbMostrar);
                                    rdbIgnorar.setText(aplicacion.getI18nString("importar.ignorar.entidad"));
                                    rdbMostrar.setText(aplicacion.getI18nString("importar.mostrar.entidad"));
                                    rdbMostrar.setBounds(new java.awt.Rectangle(135, 217, 253, 20));
                                    rdbIgnorar.setBounds(new java.awt.Rectangle(431, 217, 301, 20));
                                    rdbMostrar.setSelected(true);
                                    add(txtFuente, null);
                                    add(lblOrganismo, null);
                                    add(lblPersona, null);
                                    add(lblFecha, null);
                                    add(lblTipo, null);
                                    add(jComboBox1, null);
                                    add(txtOrganismo, null);
                                    add(txtPersona, null);
                                    add(txtFecha, null);
                                    add(lblFuente, null);
                                    add(rdbIgnorar, null);
                                    add(rdbMostrar, null);
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

    }// jbinit

    public void enteredFromLeft(Map dataMap)
    {
        if (!aplicacion.isLogged())
        {
            aplicacion.login();
        }
        if (!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }

        GeopistaPermission paso = new GeopistaPermission(
                "Geopista.InfReferencia.ImportarDatosToponimos");
        permiso = aplicacion.checkPermission(paso, "Informacion de Referencia");

    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        // Meter en el blackboard el layer08 que es el fichero
        blackboard.put("importarToponimos", fichero);
        blackboard.put("mostrarError", rdbMostrar.isSelected());
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
        if (hayErrores)
        {
            return false;
        } else
        {
            if (permiso)
            {
                if (txtFichero.getText().length() != 0)
                {
                    return true;
                } else
                {
                    return false;
                }
            } else
            {
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("NoPermisos"));
                wizardContext.cancelWizard();
                return false;
            }

        }
    }

    private JLabel lblOrganismo = null;

    private JLabel lblPersona = null;

    private JLabel lblFecha = null;

    private JLabel lblTipo = null;

    private JLabel lblComent = null;

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

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    /**
     * btnAbrir_actionPerformed
     * 
     * Abre el cuadro de diálogo para escoger el fichero Shapefile.
     * 
     * @param e :
     *            Evento
     * @retun Nada
     */
    private void btnAbrir_actionPerformed(ActionEvent e)
    {
        boolean validarEsquema = false;

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("shp");
        filter.setDescription("Ficheros ShapeFiles");
        fc.setFileFilter(filter);

        // QUITA LA OPCION ALL FILES(*.*)
        fc.setAcceptAllFileFilterUsed(false);

        File currentDirectory = (File) blackboard
                .get(GeopistaInicio.LAST_IMPORT_DIRECTORY);

        fc.setCurrentDirectory(currentDirectory);

        int returnVal = fc.showOpenDialog(this);

        blackboard.put(GeopistaInicio.LAST_IMPORT_DIRECTORY, fc
                .getCurrentDirectory());

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), geopistaEditor1.getContext().getErrorHandler());

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
                                        cadenaTexto = "";
                                        ediError.setText(cadenaTexto);
                                        hayErrores = false;
                                        fichero = fc.getSelectedFile().getPath();
                                        txtFichero.setText(fichero);

                                        // meto el fichero seleccionado en el
                                        // campo
                                        String fichero2 = fichero.substring(0, fichero
                                                .length() - 4)
                                                + ".dbf";

                                        cadenaTexto = "<font face=SansSerif size=3>"
                                                + aplicacion
                                                        .getI18nString("ImportacionComenzar")
                                                + "<b>" + " "
                                                + fc.getSelectedFile().getName() + "</b>";
                                        cadenaTexto = cadenaTexto
                                                + "<p>"
                                                + aplicacion
                                                        .getI18nString("OperacionMinutos")
                                                + " ...</p></font>";
                                        cadenaTexto = cadenaTexto
                                                + aplicacion
                                                        .getI18nString("importar.datos.toponimos");
                                        ediError.setText(cadenaTexto);
                                        cadenaTexto="";
                                        // buscamos que existan los dos campos :
                                        // TOPONIMO - TIPO
                                        boolean toponimo = encontrarCampoDBF("TOPONIMO",
                                                fichero2);
                                        boolean tipo = encontrarCampoDBF("TIPO", fichero2);
                                        boolean idioma = encontrarCampoDBF("IDIOMA",
                                                fichero2);
                                        boolean subtipo = encontrarCampoDBF("SUBTIPO",
                                                fichero2);

                                        if (!toponimo)
                                        {
                                            // Error de topónimo
                                            cadenaTexto = cadenaTexto
                                                    + aplicacion
                                                            .getI18nString("importar.toponimos.falta.campo.toponimo");
                                            hayErrores = true;
                                        } else
                                        {
                                            if (!tipo)
                                            {
                                                // No existe el campo tipo
                                                cadenaTexto = cadenaTexto
                                                        + aplicacion
                                                                .getI18nString("importar.toponimos.falta.campo.tipo");
                                                hayErrores = true;
                                            } else
                                            {
                                                if (!idioma)
                                                {
                                                    cadenaTexto = cadenaTexto
                                                            + aplicacion
                                                                    .getI18nString("importar.toponimos.falta.campo.idioma");
                                                    hayErrores = true;
                                                } else
                                                {
                                                    if (!subtipo)
                                                    {
                                                        cadenaTexto = cadenaTexto
                                                                + aplicacion
                                                                        .getI18nString("importar.toponimos.falta.campo.subtipo");
                                                        hayErrores = true;
                                                    } else
                                                    {
                                                        // Verificar que ambos
                                                        // sean correctos en
                                                        // todas las líneas
                                                        // Cargo en memoria el
                                                        // layer
                                                        try
                                                        {
                                                            layer08 = (GeopistaLayer) geopistaEditor1
                                                                    .loadData(
                                                                            fc
                                                                                    .getSelectedFile()
                                                                                    .getAbsolutePath(),
                                                                            "");
                                                        } catch (Exception aj)
                                                        {
                                                            aj.printStackTrace();
                                                        }
                                                        layer08.setActiva(false);
                                                        layer08.addStyle(new BasicStyle(
                                                                new Color(64, 64, 64)));
                                                        layer08.setVisible(false);
                                                        //                                		               
                                                        // hay que recorrer el
                                                        // layer08
                                                        int i = 0;
                                                        List listaLayer = layer08
                                                                .getFeatureCollectionWrapper()
                                                                .getFeatures();
                                                        Iterator itLayer = listaLayer
                                                                .iterator();
                                                        hayErrores = false;

                                                        while (itLayer.hasNext())
                                                        {
                                                            Feature f = (Feature) itLayer
                                                                    .next();

                                                            if ((f.getString("TOPONIMO")
                                                                    .trim().length()) != 0)
                                                            {
                                                                // Comprobamos
                                                                // que el tipo
                                                                // sea un número
                                                                try
                                                                {
                                                                    int numero = Integer
                                                                            .parseInt(f
                                                                                    .getString("TIPO"));
                                                                } catch (Exception ehh)
                                                                {
                                                                    cadenaTexto = cadenaTexto
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.en.fila")
                                                                            + "<font face=SansSerif size=3 color=#ff0000> "
                                                                            + i
                                                                            + "  </font>  "
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.falta.valor.tipo.1");
                                                                    hayErrores = true;
                                                                }

                                                                // Comprobamos
                                                                // que el
                                                                // subTipo sea
                                                                // numérico
                                                                // tambien
                                                                try
                                                                {
                                                                    int numero2 = Integer
                                                                            .parseInt(f
                                                                                    .getString("SUBTIPO"));
                                                                } catch (Exception ehhh)
                                                                {
                                                                    cadenaTexto = cadenaTexto
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.en.fila")
                                                                            + "<font face=SansSerif size=3 color=#ff0000> "
                                                                            + i
                                                                            + "  </font>  "
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.falta.valor.subtipo.1");
                                                                    hayErrores = true;
                                                                }

                                                                // Comprobamos
                                                                // que el idioma
                                                                // es uno de los
                                                                // cinco
                                                                if ((!f.getString(
                                                                        "IDIOMA").equals(
                                                                        "es_ES"))
                                                                        && (!f
                                                                                .getString(
                                                                                        "IDIOMA")
                                                                                .equals(
                                                                                        "ga_ES"))
                                                                        && (!f
                                                                                .getString(
                                                                                        "IDIOMA")
                                                                                .equals(
                                                                                        "ca_ES"))
                                                                        && (!f
                                                                                .getString(
                                                                                        "IDIOMA")
                                                                                .equals(
                                                                                        "eu_ES"))
                                                                        && (!f
                                                                                .getString(
                                                                                        "IDIOMA")
                                                                                .equals(
                                                                                        "va_ES")))
                                                                {
                                                                    cadenaTexto = cadenaTexto
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.en.fila")
                                                                            + "<font face=SansSerif size=3 color=#ff0000> "
                                                                            + i
                                                                            + "  </font>  "
                                                                            + aplicacion
                                                                                    .getI18nString("importar.toponimos.falta.valor.idioma.1");
                                                                    hayErrores = true;
                                                                }

                                                            } else
                                                            {
                                                                cadenaTexto = cadenaTexto
                                                                        + aplicacion
                                                                                .getI18nString("importar.toponimos.en.fila")
                                                                        + "<font face=SansSerif size=3 color=#ff0000> "
                                                                        + i
                                                                        + "   </font>  "
                                                                        + aplicacion
                                                                                .getI18nString("importar.toponimos.falta.valor.toponimo.1");
                                                                hayErrores = true;
                                                            }// si
                                                            // len(toponimo)=0

                                                            i++;
                                                        }// Del While d
                                                        // iterar las
                                                        // features
                                                    }// del Subtipo
                                                }// del Idioma

                                            }// del Tipo

                                        } // del toponimo

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

            if (!hayErrores)
            {
                // No hay Errores poner el mensaje de no error
                cadenaTexto = cadenaTexto
                        + aplicacion.getI18nString("importar.toponimos.fichero.correcto");
                cadenaTexto = cadenaTexto
                        + aplicacion.getI18nString("validacion.finalizada");

            }
            ediError.setText(cadenaTexto);
            wizardContext.inputChanged();
        }
    }

    /**
     * Busca un campo en un fichero DBF
     * 
     * @param String
     *            nombreCampo, campo a buscar
     * @param String
     *            rutaFichero, fichero donde se buscará el campo
     * @return boolean, true si lo encuentra y false en caso contrario
     */

    public boolean encontrarCampoDBF(String nombreCampo, String rutaFichero)
    {
        boolean encontrado = false;
        try
        {
            DbfFile leerDbf = new DbfFile(rutaFichero);

            for (int k = 0; k <= leerDbf.getNumFields(); k++)
            {
                if ((leerDbf.getFieldName(k).toString()).equals(nombreCampo))
                {
                    encontrado = true;
                    break;
                }

            }
        } catch (Exception ex)
        {
            encontrado = false;
            return encontrado;

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

} // @jve:decl-index=0:visual-constraint="-3,-33"

