package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.geotools.dbffile.DbfFile;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaMostrarTramosVias extends JPanel implements WizardPanel
{
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private int numeroRegistrosLeidos = 0;

    private String cadenaTexto = "";

    private Blackboard blackboardInformes = aplicacion.getBlackboard();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private javax.swing.JLabel jLabel = null;

    private JLabel lblTipoFichero = new JLabel();

    private JComboBox cmbTipoInfo = new JComboBox();

    private GeopistaLayer capaParcelas = null;

    public Connection con = null;

    private JScrollPane jScrollPane2 = new JScrollPane();

    private Object thisJPanel = this;

    private int idMunicipio = 0;

    //private int srid = 0;

    private WizardContext wizardContext = null;

    private String[] cache = null;

    public GeopistaMostrarTramosVias()
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
                                    setLayout(null);
                                    setSize(new Dimension(750, 600));

                                    scpErrores
                                            .setBounds(new Rectangle(135, 55, 595, 295));

                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));

                                    lblTipoFichero
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.tipo.fichero"));
                                    lblTipoFichero.setBounds(new Rectangle(136, 20, 140,
                                            20));

                                    cmbTipoInfo.setBackground(new Color(255, 255, 255));
                                    cmbTipoInfo
                                            .setBounds(new Rectangle(283, 20, 290, 20));

                                    jScrollPane2.setBounds(new Rectangle(135, 360, 600,
                                            110));
                                    setSize(750, 600);
                                    add(jScrollPane2, null);
                                    add(cmbTipoInfo, null);
                                    add(lblTipoFichero, null);
                                    add(lblImagen, null);
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

    }// jbinit

    public void enteredFromLeft(Map dataMap)
    {
        wizardContext.previousEnabled(false);
        idMunicipio = Integer.parseInt(aplicacion.getString("geopista.DefaultCityId"));
        /*srid = Integer.parseInt((String) blackboardInformes
                .get(GeopistaImportarCallesSeleccionElemento.MUNICIPIOSRID));*/

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarTramosVias.ImportandoDatos"));
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
                                    if (con == null)
                                    {
                                        con = getDBConnection();
                                    }
                                    numeroRegistrosLeidos = 0;
                                    cadenaTexto = "";

                                    // Comparamos los 4 valores del combo
                                    cmbTipoInfo.removeAllItems();
                                    cmbTipoInfo.addItem((String) blackboardInformes
                                            .get("tipoImportarTramos"));
                                    String selectedModule = (String) blackboardInformes
                                            .get("tipoImportarTramos");
                                    scpErrores.setVisible(false);
                                    ArrayList lista = new ArrayList();

                                    // Inicio de lo de exiting to rigth

                                    // pregunto por los
                                    // distritos Censales

                                    if (selectedModule
                                            .equals(aplicacion
                                                    .getI18nString("importar.informacion.referencia.distritos.censales")))
                                    {
                                        setName(aplicacion
                                                .getI18nString("almacenar.distritos.censales.asistente"));

                                        // Iniciamos la ayuda
                                        loadHelp("InformacionReferenciaDistritosCensalesGuardarBaseDatos");
                                        // por actualizar

                                        lista = (ArrayList) blackboardInformes
                                                .get("listaDistritosCensales");

                                        numeroRegistrosLeidos = actualizarDatosIneDistritosCensales(
                                                lista, idMunicipio, progressDialog);

                                    } else
                                    {

                                        if (selectedModule
                                                .equals(aplicacion
                                                        .getI18nString("importar.informacion.referencia.secciones.censales")))
                                        {
                                            setName(aplicacion
                                                    .getI18nString("almacenar.secciones.censales.asistente"));

                                            // Iniciamos la
                                            // ayuda
                                            loadHelp("InformacionReferenciaSeccionesCensalesGuardarBaseDatos");
                                            // secciones
                                            // censales
                                            lista = (ArrayList) blackboardInformes
                                                    .get("listaSeccionesCensales");

                                            numeroRegistrosLeidos = actualizarDatosSeccionesCensales(
                                                    lista, progressDialog,
                                                    "nombre Oficial", idMunicipio);

                                        } else
                                        {
                                            if (selectedModule
                                                    .equals(aplicacion
                                                            .getI18nString("importar.informacion.referencia.subsecciones.censales")))
                                            {
                                                setName(aplicacion
                                                        .getI18nString("almacenar.subsecciones.censales.asistente"));
                                                // Iniciamos la
                                                // ayuda
                                                loadHelp("InformacionReferenciaSubSeccionesCensalesGuardarBaseDatos");
                                                lista = (ArrayList) blackboardInformes
                                                        .get("listaSubSeccionesCensales");

                                                numeroRegistrosLeidos = actualizarDatosSubSeccionesCensales(
                                                        lista, progressDialog,
                                                        idMunicipio);

                                            } else
                                            {
                                                if (selectedModule
                                                        .equals(aplicacion
                                                                .getI18nString("importar.informacion.referencia.entidades.singulares")))
                                                {
                                                    setName(aplicacion
                                                            .getI18nString("almacenar.entidades.singulares.asistente"));
                                                    // Iniciamos
                                                    // la ayuda
                                                    loadHelp("InformacionReferenciaEntidadesSingularesGuardarBaseDatos");
                                                    lista = (ArrayList) blackboardInformes
                                                            .get("listaEntidadesSingulares");

                                                    numeroRegistrosLeidos = actualizarDatosEntidadesSingulares(
                                                            lista, progressDialog,
                                                            idMunicipio);

                                                } else
                                                {
                                                    if (selectedModule
                                                            .equals(aplicacion
                                                                    .getI18nString("importar.informacion.referencia.entidades.nucleos.diseminados")))
                                                    {
                                                        setName(aplicacion
                                                                .getI18nString("almacenar.nucleos.y.diseminados.asistente"));

                                                        // Iniciamos
                                                        // la
                                                        // ayuda
                                                        loadHelp("InformacionReferenciaNucleosDiseminadosSeleccionFichero");
                                                        lista = (ArrayList) blackboardInformes
                                                                .get("listaDiseminados");
                                                        numeroRegistrosLeidos = actualizarDatosDiseminados(
                                                                lista, progressDialog,
                                                                idMunicipio);

                                                    } else
                                                    {
                                                        if (selectedModule
                                                                .equals(aplicacion
                                                                        .getI18nString("importar.informacion.referencia.entidades.colectivas")))
                                                        {
                                                            // Entidades
                                                            // Colectivas

                                                            setName(aplicacion
                                                                    .getI18nString("almacenar.entidades.colectivas.asistente"));
                                                            // Iniciamos
                                                            // la
                                                            // ayuda
                                                            loadHelp("InformacionReferenciaEntidadesColectivasGuardarBaseDatos");
                                                            lista = (ArrayList) blackboardInformes
                                                                    .get("listaEntidadesColectivas");
                                                            numeroRegistrosLeidos = actualizarDatosEntidadesColectivas(
                                                                    lista,
                                                                    progressDialog,
                                                                    idMunicipio);

                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }

                                    // Ponemos los valores de Registros Leídos
                                    // y cadena de texto

                                    // ponemos en el panel los valores de
                                    // registros leidos y fecha y Hora

                                    String fechaFinalizacion = "";

                                    JEditorPane jedResumen = new JEditorPane("text/html",
                                            cadenaTexto);

                                    jedResumen.setBorder(BorderFactory.createEmptyBorder(
                                            0, 0, 0, 0));
                                    jedResumen.setEditable(false);

                                    jScrollPane2.getViewport().add(jedResumen, null);
                                    DateFormat formatter = new SimpleDateFormat(
                                            "dd-MMM-yy hh:mm:ss");
                                    String date = (String) formatter.format(new Date());
                                    fechaFinalizacion = date;
                                    jScrollPane2.setBounds(135, 360, 600, 110);
                                    cadenaTexto = cadenaTexto
                                            + aplicacion
                                                    .getI18nString("importar.progreso.numero.leidos")
                                            + numeroRegistrosLeidos;
                                    cadenaTexto = cadenaTexto
                                            + aplicacion
                                                    .getI18nString("importar.progreso.fecha.fin")
                                            + fechaFinalizacion;

                                    jedResumen.setText(cadenaTexto);

                                    // Fin de lo que había en exiting to rigth
                                } catch (Exception e)
                                {

                                    e.printStackTrace();

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
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    }// del Método de exiting

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

    /**
     * @return null to turn the Next button into a Finish button
     */

    private String nextID = null;

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

    public String getNextID()
    {
        return nextID;
    }

    public void setWizardContext(WizardContext wd)
    {
        this.wizardContext = wd;
    }

    /**
     * Método que dado un valor de la via busca en el dbf CarVia la fecha de
     * alta de la calle
     * 
     * @param int
     *            via, valor a buscar
     * @param String
     *            rutaFicheroCarVia , fichero donde se buscarán los datos
     * @return Date, Para obtener la fecha de valor via que se pasa como
     *         parametro
     */

    public java.util.Date localizarFecha(int via, String rutaFicheroCarVia)
    {
        java.util.Date encontrado = null;

        try
        {
            DbfFile leerDbf = new DbfFile(rutaFicheroCarVia);
            for (int k = 0; k < leerDbf.getLastRec(); k++)
            {
                StringBuffer valores = leerDbf.GetDbfRec(k);
                try
                {
                    int viaDbf = 0;
                    if (leerDbf.ParseRecordColumn(valores, 5) instanceof Integer)
                    {
                        viaDbf = ((Integer) leerDbf.ParseRecordColumn(valores, 5))
                                .intValue();
                    } else
                    {
                        viaDbf = ((Double) leerDbf.ParseRecordColumn(valores, 5))
                                .intValue();
                    }

                    if (viaDbf == via)
                    {
                        DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                        int fecha = 0;
                        if (leerDbf.ParseRecordColumn(valores, 3) instanceof Integer)
                        {
                            fecha = ((Integer) leerDbf.ParseRecordColumn(valores, 3))
                                    .intValue();
                        } else
                        {
                            fecha = ((Double) leerDbf.ParseRecordColumn(valores, 3))
                                    .intValue();
                        }
                        java.util.Date date1 = (java.util.Date) formatter1.parse(String
                                .valueOf(fecha));
                        encontrado = date1;
                        break;
                    }

                } catch (Exception parseEx)
                {
                    parseEx.printStackTrace();
                    encontrado = null;
                    break;
                }

            }
        } catch (Exception ex)
        {
            encontrado = null;
            return encontrado;

        }
        return encontrado;

    }

    /**
     * Método que dado un valor de la via busca en el dbf CarVia el nombre de la
     * calle
     * 
     * @param int
     *            via, codigo a buscar
     * @param String
     *            rutaFicheroCarVia, fichero donde se buscará
     * @return String Denominación
     */

    public String localizarDenominacion(int via, String rutaFicheroCarVia)
    {
        String encontrado = null;

        try
        {
            DbfFile leerDbf = new DbfFile(rutaFicheroCarVia);
            for (int k = 0; k < leerDbf.getLastRec(); k++)
            {
                StringBuffer valores = leerDbf.GetDbfRec(k);
                try
                {
                    int viaDbf = 0;
                    if (leerDbf.ParseRecordColumn(valores, 5) instanceof Integer)
                    {
                        viaDbf = ((Integer) leerDbf.ParseRecordColumn(valores, 5))
                                .intValue();
                    } else
                    {
                        viaDbf = ((Double) leerDbf.ParseRecordColumn(valores, 5))
                                .intValue();
                    }

                    if (viaDbf == via)
                    {
                        encontrado = ((String) leerDbf.ParseRecordColumn(valores, 2));
                        break;
                    }

                } catch (Exception parseEx)
                {
                    parseEx.printStackTrace();
                    encontrado = null;
                    break;
                }

            }
        } catch (Exception ex)
        {
            encontrado = null;
            return encontrado;

        }
        return encontrado;

    }

    /**
     * Devuelve una clase con los valores necesarios de los TramosViaIne
     * 
     * @param String
     *            seccion
     * @param String
     *            subseccion
     * @return GeopistaDatosTramosViaIne, Id seccion, IdSubseccion y el Máximo +
     *         1 Id de la tabla tamos
     */
    public GeopistaDatosTramosViaIne obtenerDatosViasIne(String seccion, String subseccion)
    {

        GeopistaDatosTramosViaIne resultado = new GeopistaDatosTramosViaIne();
        try
        {
            ResultSet r = null;
            PreparedStatement ps = con.prepareStatement("tramosViasInemaxtramosViaIne");
            // Se ejecuta la consulta
            if (ps.execute())
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    resultado.setId(r.getInt(1));
                }
                r.close();
                ps.close();
            }
            // Vamos con la seccion
            ps = con.prepareStatement("tramosViaIneseccionTramosViaIne");
            ps.setString(1, seccion);
            if (ps.execute())
            {
                r = ps.getResultSet();
                while (r.next())
                {
                    resultado.setIdSeccion(r.getInt(1));
                }
                r.close();
                ps.close();
            }
            resultado.setIdSubSeccion(0);
        } catch (Exception e)
        {
            resultado = null;
        }
        return resultado;
    }

    /**
     * Establece la conexion con la base de datos
     * 
     * @return Connection, conexion
     */
    public static Connection getDBConnection() throws SQLException
    {

        ApplicationContext app = AppContext.getApplicationContext();
        Connection conn = app.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @param int
     *            Codigo del Municipio
     * @return int numero de registros leidos
     */

    public int actualizarDatosIneDistritosCensales(ArrayList datos, int idMunicipio,
            TaskMonitorDialog progressDialog) throws SQLException
    {
        // Obtener el máximo de los distrtos censales

        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("distritoscensales",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoIne = datosClase.getCampo1();
            try
            {
                if (!searchCode(codigoIne))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarDistritoCensal");
                        ps.setString(1, datosClase.getCampo2());
                        ps.setString(2, codigoIne);
                        ps.setInt(3, idMunicipio);
                        ps.setInt(4, 0);
                        ps.setInt(5, 0);
                        ps.setInt(6, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                        con.rollback();
                    }

                } else
                {
                    try
                    {
                        ps = con.prepareStatement("actualizarDistritoCensal");
                        ps.setString(1, datosClase.getCampo2());
                        ps.setString(2, codigoIne);
                        ps.setInt(3, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;
    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @return int numero de registros leidos
     */

    public int actualizarDatosSeccionesCensales(ArrayList datos,
            TaskMonitorDialog progressDialog, String nombreOficial, int idMunicipio)
            throws SQLException
    {
        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("seccionescensales",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoine = datosClase.getCampo1();
            try
            {
                if (!searchCode(codigoine))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarSeccionCensal");
                        ps.setInt(1, Integer.parseInt(datosClase.getCampo3())); // El
                        // id
                        // distrito
                        ps.setString(2, codigoine);
                        ps.setString(3, datosClase.getCampo2());
                        ps.setInt(4, 0); // area
                        ps.setInt(5, 0); // longitud
                        ps.setInt(6, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                        con.rollback();
                    }

                } else
                {
                    try
                    {
                        ps = con.prepareStatement("actualizarSeccionCensal");
                        ps.setString(1, datosClase.getCampo2());
                        ps.setString(2, codigoine);
                        ps.setInt(3, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;
    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @return int numero de registros leidos
     */

    public int actualizarDatosSubSeccionesCensales(ArrayList datos,
            TaskMonitorDialog progressDialog, int idMunicipio) throws SQLException
    {

        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("subseccionescensales",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoine = datosClase.getCampo1();
            
            try
            {
                if (!searchCode(codigoine))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarSubSeccionCensal");
                        ps.setString(1, codigoine);
                        ps.setString(2, datosClase.getCampo2());
                        ps.setInt(3, 0); // area
                        ps.setInt(4, 0); // longitud
                        ps.setInt(5, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                } else
                {
                    	registrosLeidos++;
                    // no se puede actualizar nada por lo que este bloque queda
                    // vacio de momento
                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;

    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @return int numero de registros leidos
     */

    public int actualizarDatosEntidadesSingulares(ArrayList datos,
            TaskMonitorDialog progressDialog, int idMunicipio) throws SQLException
    {
        
        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("entidadessingulares",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoine = datosClase.getCampo1();
            try
            {
                if (!searchCode(codigoine))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarEntidadSingular");
                        ps.setInt(1, idMunicipio);
                        ps.setString(2, codigoine);
                        ps.setString(3, datosClase.getCampo3());
                        ps.setString(4, datosClase.getCampo2());
                        ps.setString(5, "");
                        ps.setString(6, datosClase.getCampo4());
                        ps.setString(7, datosClase.getCampo5());
                        ps.setInt(8, 0); // area
                        ps.setInt(9, 0); // longitud
                        ps.setInt(10, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                } else
                {
                    try
                    {
	                    ps = con.prepareStatement("actualizarEntidadSingular");
	                    ps.setString(1,datosClase.getCampo3());
	                    ps.setString(2,datosClase.getCampo2());
	                    ps.setString(3,datosClase.getCampo4());
	                    ps.setString(4,datosClase.getCampo5());
	                    ps.setString(5,codigoine);
	                    ps.setInt(6, idMunicipio);
	                    ps.executeUpdate();
	                    con.commit();
	                    registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }
                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;
      
    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @return int numero de registros leidos
     */

    public int actualizarDatosEntidadesColectivas(ArrayList datos,
            TaskMonitorDialog progressDialog, int idMunicipio) throws SQLException
    {
        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("entidadescolectivas",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoine = datosClase.getCampo1();
            try
            {
                if (!searchCode(codigoine))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarEntidadColectiva");
                        ps.setString(1, codigoine);
                        ps.setString(2, datosClase.getCampo3());
                        ps.setString(3, datosClase.getCampo2());
                        ps.setString(4, "");
                        ps.setString(5, "");
                        ps.setInt(6, idMunicipio);
                        ps.setInt(7, 0); // area
                        ps.setInt(8, 0); // longitud
                        ps.setInt(9, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                } else
                {
                    try
                    {
	                    ps = con.prepareStatement("actualizarEntidadColectiva");
	                    ps.setString(1,datosClase.getCampo3());
                        ps.setString(2,datosClase.getCampo2());
	                    ps.setString(3,codigoine);
	                    ps.setInt(4,idMunicipio);
	                    ps.executeUpdate();
	                    con.commit();
	                    registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }
                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;
        
    }

    /**
     * Método que devuelve -1 si ha habido algun error en la insercción
     * 
     * @param ArrayList
     * @return int numero de registros leidos
     */

    public int actualizarDatosDiseminados(ArrayList datos,
            TaskMonitorDialog progressDialog, int idMunicipio) throws SQLException
    {
        int registrosLeidos = 0;
        int registrosErrorneos = 0;
        PreparedStatement ps = null;

        String[] codeList = comprobarDuplicacionRegistros("nucleos_y_diseminados",
                idMunicipio);

        // Con el Id insertamos los valores en la tabla
        Iterator i = datos.iterator();
        con.setAutoCommit(true);
        while (i.hasNext())
        {

            progressDialog.report(registrosLeidos + registrosErrorneos, datos.size(),
                    aplicacion.getI18nString("ImportandoEntidad"));
            GeopistaDatosImportarIne datosClase = (GeopistaDatosImportarIne) i.next();
            String codigoine = datosClase.getCampo1();
            try
            {
                if (!searchCode(codigoine))
                {

                    try
                    {
                        ps = con.prepareStatement("insertarDiseminado");
                  //      ps.setInt(1,Integer.parseInt(codigoine));
                        ps.setInt(1, Integer.parseInt(datosClase.getCampo4()));
                        ps.setString(2, codigoine);
                        ps.setString(3, datosClase.getCampo3());
                        ps.setString(4, datosClase.getCampo2());
                        ps.setString(5, null);
                        ps.setString(6, null);
                        ps.setInt(7, 0);
                        ps.setInt(8, 0);
                        ps.setInt(9, 0); // area
                        ps.setInt(10, 0); // longitud
                        ps.setInt(11, idMunicipio);
                        ps.executeUpdate();
                        con.commit();
                        registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }

                } else
                {
                    try
                    {
	                    ps = con.prepareStatement("actualizarDiseminado");
	                    ps.setString(1,datosClase.getCampo3());
                        ps.setString(2,datosClase.getCampo2());
	                    ps.setString(3,codigoine);
	                    ps.setInt(4, idMunicipio);
	                    ps.executeUpdate();
	                    con.commit();
	                    registrosLeidos++;
                    } catch (Exception e)
                    {
                        registrosErrorneos++;
                        e.printStackTrace();
                    }
                }

            } finally
            {
                aplicacion.closeConnection(null, ps, null, null);
            }
        }
        return registrosLeidos;
           
    }

    private void loadHelp(String blockHelp)
    {
        try
        {
            String helpHS = "ayuda.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            hb.enableHelpKey((Component) thisJPanel, blockHelp, hs);
        } catch (Exception excp)
        {
        }
    }

    public String[] comprobarDuplicacionRegistros(String tabla, int idMunicipio)
            throws SQLException
    {
        ArrayList codeList = new ArrayList();
        ResultSet rst = null;
        String sql = "select" + tabla + "Registros";
        PreparedStatement ps2 = null;
        try
        {
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, idMunicipio);
            if (ps2.execute())
            {
                rst = ps2.getResultSet();
            }
            while (rst.next())
            {
                codeList.add(rst.getString("codigoine"));
            }
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("GeopistaMostrarTramosVias.ErrorAccesoBaseDatos"));
            wizardContext.cancelWizard();
            throw new SQLException(e.getMessage());
        } finally
        {
            aplicacion.closeConnection(null, ps2, null, rst);
        }

        cache = new String[codeList.size()];

        int i = 0;
        for (Iterator features = codeList.iterator(); features.hasNext();)
        {
            String code = (String) features.next();
            cache[i++] = code;
        }
        Arrays.sort(cache);

        return cache;
    }

    private boolean searchCode(String newCode)
    {
        int codeIndex = Arrays.binarySearch(cache, newCode);
        if (codeIndex < 0)
            return false;

        return true;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

} // de la clase general.

