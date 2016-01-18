package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarCallesSeleccionElemento extends JPanel implements
        WizardPanel
{
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private String cadenaTexto = null;

    private Blackboard blackboard = aplicacion.getBlackboard();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private JLabel lblFichero = new JLabel();

    private JTextField txtFichero = new JTextField();

    private JButton btnAbrir = new JButton();

    private JLabel lblErrores = new JLabel();

    private boolean hayErroresFilas = false;

    private boolean erroresNumerosPolicia = false;

    private javax.swing.JLabel jLabel = null;

    private JLabel lblTipoFichero = new JLabel();

    private JComboBox cmbTipoInfo = new JComboBox();

    private JEditorPane ediError = new JEditorPane();

    private boolean permiso;

    private JFileChooser fc = new JFileChooser();

    private int returnVal = 0;

    private int valor_combo = 0;

    private WizardContext wizardContext;

    private JLabel lblCardVia = new JLabel();

    private JTextField txtCardVia = new JTextField();

    private JLabel lblTipo = new JLabel();

    private JTextField txtFuente = new JTextField();

    private JTextField txtOrganismo = new JTextField();

    private JTextField txtPersona = new JTextField();

    private JTextField txtFecha = new JTextField();

    private JSeparator jSeparator1 = new JSeparator();

    private JLabel lblFecha = new JLabel();

    private JLabel lblPersona = new JLabel();

    private JLabel lblOrganismo = new JLabel();

    private JLabel lblFuente = new JLabel();

    //private JLabel lblSrid = new JLabel();

    private JComboBox jComboBox1 = new JComboBox();

    //private JComboBox jComboSrid = new JComboBox();

    private JSeparator jSeparator2 = new JSeparator();

    private JSeparator jSeparator3 = new JSeparator();

    private JLabel lblComent = new JLabel();

    private String errorMessage = "";

    public static final int DISTRITOSCENSALES = 0;

    public static final int SECCIONESCENSALES = 1;

    public static final int SUBSECCIONESCENSALES = 5;//ha sido eliminado por eso pasa a 5 que no existe en el combo, originalmente era el 2

    public static final int ENTIDADESSINGULARES = 2;

    public static final int DISEMINADOS = 3;

    public static final int ENTIDADESCOLECTIVAS = 4;

    //public static final String MUNICIPIOSRID = "MunicipioSrid";
    
    private Connection con = null;

    public GeopistaImportarCallesSeleccionElemento()
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
                                    permiso = true;
                                    setLayout(null);
                                    setName(aplicacion
                                            .getI18nString("importar.asistente.elementos.ine.titulo1"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.texto"));

                                    hayErroresFilas = true;
                                    scpErrores
                                            .setBounds(new Rectangle(135, 300, 595, 240));

                                    cmbTipoInfo
                                            .addItem(aplicacion
                                                    .getI18nString("importar.informacion.referencia.distritos.censales"));
                                    cmbTipoInfo
                                            .addItem(aplicacion
                                                    .getI18nString("importar.informacion.referencia.secciones.censales"));
                                    //cmbTipoInfo
                                    //        .addItem(aplicacion
                                    //                .getI18nString("importar.informacion.referencia.subsecciones.censales"));
                                    cmbTipoInfo
                                            .addItem(aplicacion
                                                    .getI18nString("importar.informacion.referencia.entidades.singulares"));
                                    cmbTipoInfo
                                            .addItem(aplicacion
                                                    .getI18nString("importar.informacion.referencia.entidades.nucleos.diseminados"));
                                    cmbTipoInfo
                                            .addItem(aplicacion
                                                    .getI18nString("importar.informacion.referencia.entidades.colectivas"));

                                    cmbTipoInfo.setSelectedIndex(-1);
                                    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));

                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));

                                    lblFichero
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.fichero.importar"));
                                    lblFichero
                                            .setBounds(new Rectangle(135, 215, 240, 20));

                                    txtFichero
                                            .setBounds(new Rectangle(375, 215, 280, 20));
                                    txtFichero.setEditable(false);

                                    btnAbrir.setBounds(new Rectangle(665, 215, 25, 20));
                                    btnAbrir.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnAbrir_actionPerformed(e);
                                            }
                                        });

                                    lblErrores
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.errores.validacion"));
                                    lblErrores
                                            .setBounds(new Rectangle(135, 280, 410, 20));

                                    lblTipoFichero
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.tipo.fichero"));
                                    lblTipoFichero.setBounds(new Rectangle(140, 185, 230,
                                            20));

                                    cmbTipoInfo.setBackground(new Color(255, 255, 255));
                                    cmbTipoInfo
                                            .setBounds(new Rectangle(375, 185, 315, 20));
                                    cmbTipoInfo.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                cmbTipoInfo_actionPerformed(e);
                                            }
                                        });

                                    ediError.setText("jEditorPane1");
                                    ediError.setContentType("text/html");
                                    ediError.setEditable(false);
                                    lblCardVia
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.fichero.vias"));
                                    lblCardVia.setBounds(new Rectangle(475, 165, 35, 20));
                                    txtCardVia.setBounds(new Rectangle(715, 185, 25, 20));
                                    txtCardVia.setEnabled(false);
                                    txtCardVia.setEditable(false);

                                    lblTipo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.Tipo"));
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
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.Fecha"));
                                    lblFecha.setBounds(new Rectangle(135, 118, 90, 25));
                                    lblFecha
                                            .setText(aplicacion
                                                    .getI18nString("fecha.importacion.caja.texto"));
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.Persona"));
                                    lblPersona.setBounds(new Rectangle(135, 90, 85, 20));
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("persona.importacion.caja.texto"));
                                    lblOrganismo
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.Organismo"));
                                    lblOrganismo
                                            .setBounds(new Rectangle(135, 60, 90, 20));
                                    lblOrganismo
                                            .setText(aplicacion
                                                    .getI18nString("organismo.importacion.caja.texto"));
                                    lblFuente
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.Fuente"));
                                    lblFuente.setBounds(new Rectangle(135, 30, 95, 20));
                                    lblFuente
                                            .setText(aplicacion
                                                    .getI18nString("fuente.importacion.caja.texto"));
                                    DateFormat formatter = new SimpleDateFormat(
                                            "dd-MMM-yy");
                                    String date = (String) formatter.format(new Date());
                                    jComboBox1
                                            .setBounds(new Rectangle(550, 120, 180, 20));
                                    /*jComboSrid
                                            .setBounds(new Rectangle(375, 240, 230, 20));*/
                                    /*lblSrid.setBounds(new Rectangle(135, 240, 230, 20));
                                    lblSrid
                                            .setText(aplicacion
                                                    .getI18nString("GeopistaImportarCallesSeleccionElemento.IntroduzcaSRID"));*/
                                    jSeparator2.setBounds(new Rectangle(130, 20, 600, 5));
                                    jSeparator3
                                            .setBounds(new Rectangle(140, 270, 590, 5));
                                    lblComent.setText(aplicacion
                                            .getI18nString("seleccione.censales"));
                                    lblComent.setBounds(new Rectangle(140, 155, 600, 25));
                                    txtFecha.setText(date);

                                    // cmbTipoInfo.setBounds(283, 22, 290, 20);

                                    /*jComboSrid.addItem("Huso 29 -> 23029");
                                    jComboSrid.addItem("Huso 30 -> 23030");
                                    jComboSrid.addItem("Huso 31 -> 23031");
                                    jComboSrid.addItem("Huso 32 -> 23032");
                                    jComboSrid.setSelectedIndex(-1);
                                    jComboSrid.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnSridAbrir_actionPerformed(e);
                                            }
                                        });*/

                                    setSize(750, 600);
                                    add(lblComent, null);
                                    add(jSeparator3, null);
                                    add(jSeparator2, null);
                                    add(jComboBox1, null);
                                    add(lblFuente, null);
                                    add(lblOrganismo, null);
                                    add(lblPersona, null);
                                    add(lblFecha, null);
                                    add(jSeparator1, null);
                                    add(txtFecha, null);
                                    add(txtPersona, null);
                                    add(txtOrganismo, null);
                                    add(txtFuente, null);
                                    add(lblTipo, null);
                                    add(txtCardVia, null);
                                    add(lblCardVia, null);
                                    add(cmbTipoInfo, null);
                                    add(lblTipoFichero, null);
                                    add(lblErrores, null);
                                    add(btnAbrir, null);
                                    add(txtFichero, null);
                                    add(lblFichero, null);
                                    add(lblImagen, null);
                                    //add(jComboSrid, null);
                                    //add(lblSrid, null);

                                    scpErrores.getViewport().add(ediError, null);
                                    add(scpErrores, null);
                                    lblCardVia.setVisible(false);
                                    txtCardVia.setVisible(false);

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
                "Geopista.InfReferencia.ImportarDatosIne");
        permiso = aplicacion.checkPermission(paso, "Informacion de Referencia");

    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard el mapaCagarTramosVias
        blackboard.put("mapaCargarTramosVias", txtFichero.getText());
        blackboard.put("rutaFicheroCarVia", txtCardVia.getText());
        blackboard.put("tipoImportarTramos", cmbTipoInfo.getSelectedItem());
        /*String Huso = (String) jComboSrid.getSelectedItem();
        String[] srid = Huso.split(">");
        
        
        blackboard.put(MUNICIPIOSRID, srid[srid.length-1].trim());*/
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

        if (permiso)
        {
            /*if (!hayErroresFilas && jComboSrid.getSelectedIndex() != -1)
            {
                return true;
            } else
            {
                return false;
            }*/
            
            if (!hayErroresFilas)
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
            return false;
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

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    private void btnAbrir_actionPerformed(ActionEvent e)
    {

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        valor_combo = cmbTipoInfo.getSelectedIndex();

        filter.addExtension("txt");
        filter.setDescription(aplicacion
                .getI18nString("importar.informacion.referencia.fichero.texto"));

        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);

        File currentDirectory = (File) blackboard
                .get(Constantes.LAST_IMPORT_DIRECTORY);

        fc.setCurrentDirectory(currentDirectory);
        returnVal = fc.showOpenDialog(this);
        blackboard.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());

        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

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
                                    String idMunicipio = aplicacion.getString("geopista.DefaultCityId");
                                    
                                    if(con==null)
                                    {
                                        con = abrirConexion();
                                    }
                                    errorMessage = "";
                                    txtFichero.setText(fc.getSelectedFile().getPath());
                                    hayErroresFilas = false;
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
                                    ediError.setText(cadenaTexto);
                                    cadenaTexto = "";

                                    if (valor_combo == DISTRITOSCENSALES)
                                    {

                                        ArrayList listaDistritosCensales = validarDistritosCensales(txtFichero
                                                .getText());

                                        if (listaDistritosCensales != null)
                                        {
                                            cadenaTexto = cadenaTexto
                                                    + aplicacion
                                                            .getI18nString("importar.datos.distritos.censales.correctos");

                                            // Procesar en
                                            // una lista

                                            blackboard.put("listaDistritosCensales",
                                                    listaDistritosCensales);
                                        } else
                                        {
                                            showErrorMessage();
                                        }

                                    } else
                                    {
                                        if (valor_combo == SECCIONESCENSALES)
                                        {

                                            ArrayList listaSeccionesCensales = validarSeccionesCensales(txtFichero
                                                    .getText(),idMunicipio);

                                            if (listaSeccionesCensales != null)
                                            {
                                                cadenaTexto = cadenaTexto
                                                        + aplicacion
                                                                .getI18nString("importar.datos.secciones.censales.correctos");

                                                blackboard.put("listaSeccionesCensales",
                                                        listaSeccionesCensales);
                                            } else
                                            {
                                                showErrorMessage();

                                            }

                                        } else
                                        {
                                            if (valor_combo == SUBSECCIONESCENSALES)
                                            {

                                                ArrayList listaSubSeccionesCensales = validarSubSeccionesCensales(txtFichero
                                                        .getText());

                                                if (listaSubSeccionesCensales != null)
                                                {
                                                    cadenaTexto = cadenaTexto
                                                            + aplicacion
                                                                    .getI18nString("importar.datos.subsecciones.censales.correctos");

                                                    blackboard.put(
                                                            "listaSubSeccionesCensales",
                                                            listaSubSeccionesCensales);
                                                } else
                                                {
                                                    showErrorMessage();
                                                }

                                            } else
                                            {
                                                if (valor_combo == ENTIDADESSINGULARES)
                                                {
                                                    ArrayList listaEntidadesSingulares = validarEntidadesSingulares(txtFichero
                                                            .getText());

                                                    if (listaEntidadesSingulares != null)
                                                    {
                                                        cadenaTexto = cadenaTexto
                                                                + aplicacion
                                                                        .getI18nString("importar.datos.entidades.singulares.correctos");

                                                        blackboard
                                                                .put(
                                                                        "listaEntidadesSingulares",
                                                                        listaEntidadesSingulares);
                                                    } else
                                                    {
                                                        showErrorMessage();
                                                    }

                                                } else
                                                {
                                                    if (valor_combo == DISEMINADOS)
                                                    {
                                                        ArrayList listaDiseminados = validarDiseminados(txtFichero
                                                                .getText(),idMunicipio);

                                                        if (listaDiseminados != null)
                                                        {
                                                            cadenaTexto = cadenaTexto
                                                                    + aplicacion
                                                                            .getI18nString("importar.datos.nucleos.diseminados.correctos");

                                                            blackboard.put(
                                                                    "listaDiseminados",
                                                                    listaDiseminados);
                                                        } else
                                                        {
                                                            showErrorMessage();
                                                        }

                                                    } else
                                                    {

                                                        if (valor_combo == ENTIDADESCOLECTIVAS)
                                                        {
                                                            ArrayList listaEntidadesColectivas = validarEntidadesColectivas(txtFichero
                                                                    .getText());

                                                            if (listaEntidadesColectivas != null)
                                                            {
                                                                cadenaTexto = cadenaTexto
                                                                        + aplicacion
                                                                                .getI18nString("importar.datos.entidades.colectivas.correctos");
                                                                blackboard
                                                                        .put(
                                                                                "listaEntidadesColectivas",
                                                                                listaEntidadesColectivas);
                                                            } else
                                                            {
                                                                showErrorMessage();
                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                } catch (Exception e)
                                {

                                } finally
                                {
                                    aplicacion.closeConnection(con, null, null, null);
                                    progressDialog.setVisible(false);
                                    
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");
        ediError.setText(cadenaTexto);
        wizardContext.inputChanged();

    }

    private void cmbTipoInfo_actionPerformed(ActionEvent e)
    {

        // Poner a actualizar o no

        String tema = (String) blackboard.get("tipoImportarTramos");
        lblFichero.setText(aplicacion.getI18nString("seleccione") + " "
                + cmbTipoInfo.getSelectedItem());

        txtCardVia.setText(null);
        this.txtFichero.setText(null);
        this.ediError.setText(null);
        lblCardVia.setVisible(false);
        txtCardVia.setVisible(false);
        txtCardVia.setEnabled(false);
        txtCardVia.setText(null);

        // Iniciamos la ayuda
        try
        {
            String helpHS = "ayuda.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();

            if (cmbTipoInfo
                    .getSelectedItem()
                    .equals(
                            aplicacion
                                    .getI18nString("importar.informacion.referencia.distritos.censales")))
            {
                // Distritos Censales
                hb.enableHelpKey(this,
                        "InformacionReferenciaDistritosCensalesSeleccionFichero", hs);
            } else
            {
                if (cmbTipoInfo
                        .getSelectedItem()
                        .equals(
                                aplicacion
                                        .getI18nString("importar.informacion.referencia.secciones.censales")))
                {
                    // Secciones Censales
                    hb.enableHelpKey(this,
                            "InformacionReferenciaSeccionesCensalesSeleccionFichero", hs);
                } else
                {
                    if (cmbTipoInfo
                            .getSelectedItem()
                            .equals(
                                    aplicacion
                                            .getI18nString("importar.informacion.referencia.subsecciones.censales")))
                    {
                        hb
                                .enableHelpKey(
                                        this,
                                        "InformacionReferenciaSubSeccionesCensalesSeleccionFichero",
                                        hs);
                    } else
                    {
                        if (cmbTipoInfo
                                .getSelectedItem()
                                .equals(
                                        aplicacion
                                                .getI18nString("importar.informacion.referencia.entidades.singulares")))
                        {
                            // Entidades Singulares
                            hb
                                    .enableHelpKey(
                                            this,
                                            "InformacionReferenciaEntidadesSingularesSeleccionFichero",
                                            hs);
                        } else
                        {
                            // Nucleos y diseminados
                            if (cmbTipoInfo
                                    .getSelectedItem()
                                    .equals(
                                            aplicacion
                                                    .getI18nString("importar.informacion.referencia.entidades.nucleos.diseminados")))
                            {
                                hb
                                        .enableHelpKey(
                                                this,
                                                "InformacionReferenciaNucleosDiseminadosSeleccionFichero",
                                                hs);
                            } else
                            {
                                if (cmbTipoInfo
                                        .getSelectedItem()
                                        .equals(
                                                aplicacion
                                                        .getI18nString("importar.informacion.referencia.entidades.colectivas")))
                                {
                                    hb
                                            .enableHelpKey(
                                                    this,
                                                    "InformacionReferenciaEntidadesColectivasSeleccionFichero",
                                                    hs);
                                } else
                                {
                                }
                            }
                        }
                    }

                }
            }

        } catch (Exception excp)
        {
        }
        // fin de la ayuda

    }

    private void btnSridAbrir_actionPerformed(ActionEvent e)
    {
        wizardContext.inputChanged();
    }

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones 2 puntos y coma, para indicar que hay dos campos el primer
     * punto y coma sea numérico
     * 
     */

    public ArrayList validarDistritosCensales(String ruta)
    {

        int fila = 1;
        ArrayList lista = new ArrayList();
        String str = null;
        boolean resultado = true;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            // Procesamos para ver que todo las filas tengan puntos y comas
            fila = 1;
            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");
                if (campos.length == 0)
                {
                    // mensaje de error
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else
                {
                    if (campos.length != 2)
                    {
                        // mensaje de error
                        if (campos.length < 2)
                        {
                            makeErrorMessage(fila, "importar.falta.operador.campo");
                        } else
                        {
                            makeErrorMessage(fila, "importar.sobra.operador.campo");
                        }
                        resultado = false;
                    } else
                    {
                        if (resultado)
                        {
                            GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();
                            importarDatos.setCampo1(campos[0].trim());
                            importarDatos.setCampo2(campos[1].trim());
                            lista.add(importarDatos);
                        }
                    }
                }
                fila++;
            }

        } catch (Exception e)
        {
            errorMessage = errorMessage + aplicacion.getI18nString("importar.en.la.fila")
                    + " " + fila + " "
                    + aplicacion.getI18nString("importar.distrito.no.numerico");
            resultado = false;
        }

        if (!resultado)
        {
            // borramos lista para descargar memoria
            lista = null;
        }

        return lista;
    }

   
  

    public Connection abrirConexion() throws SQLException
    {
        Connection conn = null;
        try
        {
            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = aplicacion.getString("geopista.conexion.url");
            conn = DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }

        return conn;

    }

   
    /**
     * Método para crear la lista de distritos censales 2 puntos y coma, para
     * indicar que hay dos campos el primer punto y coma sea numérico
     * 
     * @param String
     *            ruta del fichero
     * @return ArrayList con una lista con todos los valores a importar
     */

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones 3 puntos y coma, para indicar que hay dos campos
     * 
     * @param ruta
     *            nombre del fichero a importar
     * @return String cadena con los errores detectados
     */

    public ArrayList validarSeccionesCensales(String ruta, String idMunicipio)
    {

        ArrayList lista = new ArrayList();
        int fila = 1;
        String str = null;
        boolean resultado = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));

            // Procesamos para ver que todo las filas tengan puntos y comas
            resultado = true;
            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");
                if (campos.length == 0)
                {
                    // mensaje de error
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else
                {
                    if (campos.length != 3)
                    {
                        // mensaje de error
                        if (campos.length < 3)
                        {
                            makeErrorMessage(fila, "importar.falta.operador.campo");
                        } else
                        {
                            makeErrorMessage(fila, "importar.sobra.operador.campo");
                        }
                        resultado = false;
                    } else
                    {
                        
                        int idDistritoCensal = buscarDistritoCensal(campos[2].trim(),idMunicipio);
                        if (idDistritoCensal == -1)
                        {
                            makeErrorMessage(fila, "importar.distrito.no.identificador");
                            resultado = false;
                        }
                        if (resultado)
                        {
                            GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();
                            importarDatos.setCampo1(campos[0].trim());
                            importarDatos.setCampo2(campos[1].trim());
                            importarDatos.setCampo3(String.valueOf(idDistritoCensal));

                            lista.add(importarDatos);
                        }
                    }
                }
                fila++;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            makeErrorMessage(fila, "importar.distrito.no.numerico");
            resultado = false;
        }

        if (!resultado)
        {
            lista = null;
        }

        return lista;
    }

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones 2 puntos y coma, para indicar que hay dos campos
     * 
     * @param ruta
     *            nombre del fichero a importar
     * @return String cadena con los errores detectados
     */

    public ArrayList validarSubSeccionesCensales(String ruta)
    {

        ArrayList lista = new ArrayList();
        int fila = 1;
        String str = null;
        boolean resultado = true;

        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(ruta));
            // Procesamos para ver que todo las filas tengan puntos y comas

            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");

                if (campos.length == 0)
                {
                    // mensaje de error
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else
                {
                    if (campos.length != 2)
                    {
                        // mensaje de error
                        if (campos.length < 2)
                        {
                            makeErrorMessage(fila, "importar.falta.operador.campo");
                        } else
                        {
                            makeErrorMessage(fila, "importar.sobra.operador.campo");
                        }
                        resultado = false;
                    } else
                    {
                        int idDistritoCensal = buscarSeccionCensal(campos[1].trim());
                        if (idDistritoCensal == -1)
                        {
                            makeErrorMessage(fila, "importar.seccion.no.identificador");
                            resultado = false;
                        }
                        if (resultado)
                        {
                            GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();

                            importarDatos.setCampo1(campos[0].trim());
                            importarDatos.setCampo2(String.valueOf(idDistritoCensal));

                            lista.add(importarDatos);
                        }
                    }

                }
                fila++;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            makeErrorMessage(fila, "importar.subseccion.no.numerico");
            resultado = false;
        } finally
        {
            try
            {
                reader.close();
            } catch (Exception e)
            {
            }
        }
        if (!resultado)
        {
            lista = null;
        }

        return lista;
    }

    /**
     * Método que devuelve el Id Del distrito censal a buscar
     * 
     * @param String
     *            nombreDominio, nombre del dominio
     * @param String
     *            descripción, del valor que está en el diccionario
     * @param String
     *            localidad es_ES
     * @return boolean estaEnDominio, verdadero si lo encuentra y falso en caso
     *         contrario
     */
    public int buscarDistritoCensal(String idDistrito, String idmunicipio)
    {
        
        PreparedStatement ps = null;
        ResultSet r = null;
        int resultado = -1;
        try
        {
            
            ps = con.prepareStatement("buscarIdDistrito");
            ps.setInt(1,Integer.parseInt(idmunicipio));
            ps.setString(2, idDistrito);
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
            }

            while (r.next())
            {
                resultado = r.getInt(1);
            }
            return resultado;
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            aplicacion.closeConnection(null, ps, null, r);
        }
    }

    public int buscarSeccionCensal(String idSeccion)
    {
        
        PreparedStatement ps = null;
        ResultSet r = null;
        int resultado = -1;
        try
        {
           
            ps = con.prepareStatement("buscarIdSeccion");
            ps.setString(1, idSeccion);
            if (ps.execute())
            {
                r = ps.getResultSet();
            }

            while (r.next())
            {
                resultado = r.getInt(1);
            }
            return resultado;
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            aplicacion.closeConnection(null, ps, null, r);
        }
    }

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones 4 puntos y coma, para indicar que hay dos campos
     * 
     * @param ruta
     *            nombre del fichero a importar
     * @return String cadena con los errores detectados
     */

    public ArrayList validarEntidadesSingulares(String ruta)
    {

        ArrayList lista = new ArrayList();
        int fila = 1;
        String str = null;
        boolean resultado = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            // Procesamos para ver que todo las filas tengan puntos y comas
            resultado = true;
            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");
                if (campos.length == 0)
                {
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else if (campos.length != 5)
                {
                    // mensaje de error
                    if (campos.length < 5)
                    {
                        makeErrorMessage(fila, "importar.falta.operador.campo");
                    } else
                    {
                        makeErrorMessage(fila, "importar.sobra.operador.campo");
                    }
                    resultado = false;
                } else
                {
                    if (resultado)
                    {
                        GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();
                        importarDatos.setCampo1(campos[0].trim());
                        importarDatos.setCampo2(campos[1].trim());
                        importarDatos.setCampo3(campos[2].trim());
                        importarDatos.setCampo4(campos[3].trim());
                        importarDatos.setCampo5(campos[4].trim());
                        lista.add(importarDatos);
                    }
                }

                fila++;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            makeErrorMessage(fila, "importar.subseccion.no.numerico");
            resultado = false;
        }

        if (!resultado)
        {
            lista = null;
        }

        return lista;
    }

    /**
     * Método para determinar si las filas del fichero de Nucleos y Diseminados
     * 
     * @param ruta
     *            nombre del fichero a importar
     * @return String cadena con los errores detectados
     */

    public ArrayList validarDiseminados(String ruta,String idMunicipio)
    {

        ArrayList lista = new ArrayList();
        int fila = 1;
        String str = null;
        boolean resultado = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            // Procesamos para ver que todo las filas tengan puntos y comas
            resultado = true;
            fila = 1;
            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");

                if (campos.length == 0)
                {
                    // mensaje de error
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else
                {

                    if (campos.length != 5)
                    {
                        // mensaje de error
                        if (campos.length < 5)
                        {
                            makeErrorMessage(fila, "importar.falta.operador.campo");
                        } else
                        {
                            makeErrorMessage(fila, "importar.sobra.operador.campo");
                        }
                        resultado = false;
                    } else
                    {
                        int idDiseminado = buscarIdEntidad(campos[3].trim(),campos[4].trim(),idMunicipio);
                        if (idDiseminado == -1)
                        {
                            makeErrorMessage(fila, "importar.diseminado.no.identificador");
                            resultado = false;
                        }
                        if (resultado)
                        {
                            GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();
                            importarDatos.setCampo1(campos[0].trim());
                            importarDatos.setCampo2(campos[1].trim());
                            importarDatos.setCampo3(campos[2].trim());
                            importarDatos.setCampo4(String.valueOf(idDiseminado));
                            lista.add(importarDatos);
                        }
                    }
                }
                fila++;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            makeErrorMessage(fila, "importar.diseminado.no.numerico");
            resultado = false;
        }

        if (!resultado)
        {
            lista = null;
        }

        return lista;
    }

    /**
     * Método que devuelve el Id Del de la Entidad Singular a buscar
     * 
     * @param int
     *            idDiseminado
     * @return int el codigo de la entidad singular
     */
    public int buscarIdEntidad(String idDiseminado, String idEntidadColectiva, String idMunicipio)
    {
        PreparedStatement ps = null;
        ResultSet r = null;
        int resultado = -1;
        try
        {
            ps = con.prepareStatement("buscarIdEntidad");
            ps.setString(1, idDiseminado);
            ps.setString(2, idEntidadColectiva);
            ps.setInt(3,Integer.parseInt(idMunicipio));
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
            }

            while (r.next())
            {
                resultado = r.getInt(1);
            }
            return resultado;
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        } finally
        {
            aplicacion.closeConnection(null, ps, null, r);
        }
    }

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones de las entidades colectivas
     * 
     * @param ruta
     *            nombre del fichero a importar
     * @return String cadena con los errores detectados
     */

    public ArrayList validarEntidadesColectivas(String ruta)
    {

        ArrayList lista = new ArrayList();
        String str = null;
        int fila = 1;
        boolean resultado = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            // Procesamos para ver que todo las filas tengan puntos y comas
            resultado = true;
            fila = 1;
            while ((str = reader.readLine()) != null)
            {
                str = str.replaceAll(";;","; ;");
                String[] campos = str.split(";");

                if (campos.length == 0)
                {
                    // mensaje de error
                    makeErrorMessage(fila, "importar.no.existen.separadores");
                    resultado = false;
                } else
                {

                    if (campos.length != 3)
                    {
                        // mensaje de error
                        if (campos.length < 3)
                        {
                            makeErrorMessage(fila, "importar.falta.operador.campo");
                        } else
                        {
                            makeErrorMessage(fila, "importar.sobra.operador.campo");
                        }
                        resultado = false;
                    } else
                    {
                        if (resultado)
                        {
                            GeopistaDatosImportarIne importarDatos = new GeopistaDatosImportarIne();
                            importarDatos.setCampo1(campos[0].trim());
                            importarDatos.setCampo2(campos[1].trim());
                            importarDatos.setCampo3(campos[2].trim());
                            lista.add(importarDatos);
                        }
                    }
                }
            }
            fila++;

        } catch (Exception e)
        {
            e.printStackTrace();
            makeErrorMessage(fila, "importar.entidad.colectiva.no.numerico");
            resultado = false;
        }
        if (!resultado)
        {
            lista = null;
        }

        return lista;
    }

    private void showErrorMessage()
    {
        hayErroresFilas = true;
        cadenaTexto = cadenaTexto + errorMessage
                + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");

    }

    private void makeErrorMessage(int fila, String localErrorMessage)
    {
        errorMessage = errorMessage + aplicacion.getI18nString("importar.en.la.fila")
                + " " + fila + " " + aplicacion.getI18nString(localErrorMessage);
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

} // de la clase general.

