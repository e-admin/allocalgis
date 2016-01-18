package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.dbffile.DbfFile;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.GeopistaDatosTramosViaIne;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;

public class GeopistaMostrarCallejeroPanel extends JPanel implements WizardPanel
{
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private String cadenaTexto = "";

    private Blackboard blackboard = aplicacion.getBlackboard();

    private JEditorPane jedResumen = new JEditorPane("text/html", cadenaTexto);

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

    private javax.swing.JLabel jLabel = null;

    private GeopistaEditor geopistaEditor2 = null;

    private GeopistaLayer capaTramosVias = null;

    private GeopistaLayer layer09 = null;

    private Connection con = null;

    private JScrollPane jScrollPane2 = new JScrollPane();

    private String nextID = "3";

    private WizardContext wizardContext;

    private static final int CANCELAR = 1;
    
    private static final Log logger = LogFactory.getLog(GeopistaMostrarCallejeroPanel.class);

    public GeopistaMostrarCallejeroPanel()
        {
            jbInit();
        }

    private void jbInit()
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
                                            .setBounds(new Rectangle(135, 20, 595, 330));

                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));

                                    jScrollPane2.setBounds(new Rectangle(135, 360, 600,
                                            110));

                                    setSize(750, 600);
                                    add(jScrollPane2, null);

                                    jedResumen.setBorder(BorderFactory.createEmptyBorder(
                                            0, 0, 0, 0));
                                    jedResumen.setEditable(false);

                                    jScrollPane2.setBounds(135, 360, 600, 110);

                                    add(lblImagen, null);

                                    add(scpErrores, null);
                                    jScrollPane2.getViewport().add(jedResumen, null);
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
        try
        {
            if (con == null)
            {
                con = getDBConnection();
            }
        } catch (SQLException e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoConexionBaseDatos"));
            wizardContext.cancelWizard();
            return;
        }
        setName(aplicacion.getI18nString("importar.asistente.callejero.titulo.2"));
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

                                    cadenaTexto = "";
                                    String ruta = (String) blackboard.get("mapaCargarTramosVias");
                                    String rutaPolicia = (String) blackboard.get("ficheroNumerosPolicia");

                                    String rutaCarvia = (String) blackboard.get("rutaFicheroCarVia");
                                    String rutaEjes = (String) blackboard.get("rutaFicheroEjes");
                                    ruta = rutaEjes;
                                    rutaEjes = rutaEjes.substring(0,
                                            rutaEjes.length() - 3)
                                            + "dbf";
                                    int idMunicipio = Integer.parseInt(aplicacion
                                            .getString("geopista.DefaultCityId"));

                                    // Cargamos el editor
                                    if (capaTramosVias == null)
                                    {
                                        try
                                        {
                                            geopistaEditor2 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
                                            geopistaEditor2.addPlugIn(geopistaValidatePlugIn);
                                            geopistaEditor2.addCursorTool("Zoom In/Out","com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
                                            geopistaEditor2.addCursorTool("Pan","com.vividsolutions.jump.workbench.ui.zoom.PanTool");
                                            geopistaEditor2.addPlugIn("com.geopista.ui.plugin.zoom.ZoomToLayerPlugIn");
                                            geopistaEditor2.addPlugIn("com.vividsolutions.jump.workbench.ui.zoom.ZoomToLayerPlugIn");
                                            geopistaEditor2.addPlugIn("com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn");
                                            geopistaEditor2.addPlugIn("com.geopista.ui.plugin.edit.EditSelectedFeaturePlugIn");
                                            geopistaEditor2.addCursorTool("Feature Info", "com.geopista.ui.cursortool.GeopistaFeatureInfoTool");
                                            geopistaEditor2.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

                                            
                                            

                                            // Layer08 estar� el fichero de
                                            // ejes.shp

                                            progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoTramosVia"));
                                            capaTramosVias = (GeopistaLayer) geopistaEditor2.loadData(ruta,aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));
                                            capaTramosVias.setActiva(false);
                                            capaTramosVias.addStyle(new BasicStyle(new Color(100, 100, 64)));
                                            capaTramosVias.setVisible(true);
                                            // Pondremos en Layer09 el
                                            // fichero numeros de policia
                                            progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.NumerosPolicia"));
                                            layer09 = (GeopistaLayer) geopistaEditor2.loadData(rutaPolicia,aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));

                                            //Convertimos las geometrias de los numeros de policia a puntos

                                            List allLayerFeatures = layer09.getFeatureCollectionWrapper().getFeatures();

                                            for (Iterator i = allLayerFeatures.iterator(); i.hasNext();) {

                                                Feature f = (Feature) i.next();
                                                Geometry geom = f.getGeometry();
                                                f.setGeometry(geom.getCentroid());
                                            }


                                            layer09.setActiva(false);
                                            layer09.addStyle(new BasicStyle(new Color(100, 100, 64)));
                                            layer09.setVisible(true);
                                            // Cargamos el mapa con los
                                            // datos del Mapa de Informaci�n
                                            // de
                                            // Ref.
                                            progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoCallejero"));
                                            //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                            logger.info("Cargando mapa de callejero");
                                            geopistaEditor2.loadMap(aplicacion.getString("url.mapa.callejero"));
                                            //else
                                            //	geopistaEditor2.loadMap("geopista:///362");
                                            	
                                            
                                            scpErrores.getViewport().add(geopistaEditor2,null);
                                            scpErrores.setVisible(true);


                                            int selectedValue = 0;
                                            if(geopistaEditor2
                                            .getLayerManager().getLayer("numeros_policia").getFeatureCollectionWrapper().getFeatures().size()>0)
                                            {
                                                Object[] possibleValues = { aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.Continuar"), aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.Cancelar") };
                                                selectedValue = JOptionPane.showOptionDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.LaCapaNumerosDePoliciaContieneFeatures"), aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"), 0, JOptionPane.QUESTION_MESSAGE, null,
                                                        possibleValues, possibleValues[0]);
                                            }

                                            if (selectedValue == CANCELAR)
                                            {
                                                wizardContext.cancelWizard();
                                                return;
                                            }

                                        } catch (Exception e)
                                        {
                                            JOptionPane.showMessageDialog(aplicacion
                                                    .getMainFrame(), aplicacion
                                                    .getI18nString("errorCargaMapa"));
                                            throw e;
                                        }

                                        // Abrir una transaccion

                                        int numeroRegistrosLeidos = 0;
                                      

                                       
                                        numeroRegistrosLeidos = insertarViasINE(idMunicipio, progressDialog);

                                        addMessage(
                                                "importar.informacion.referencia.viasINE",
                                                numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        	numeroRegistrosLeidos = insertarDatosTramosViasINE(idMunicipio, progressDialog);

                                        addMessage(
                                                "importar.informacion.referencia.tramos.via.ine",
                                                numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        	numeroRegistrosLeidos = insertarTramosVias(progressDialog);

                                        addMessage("importar.informacion.referencia.tramos.via",
                                                numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        	numeroRegistrosLeidos = introducirNumerosPolicia(progressDialog);

                                        addMessage("importar.informacion.referencia.numeros.de.policia",
                                                numeroRegistrosLeidos);

                                        jedResumen.setText(cadenaTexto);

                                        blackboard.put(
                                                "geopistaEditorEnlazarPoliciaCalles",
                                                geopistaEditor2);
                                    }

                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(aplicacion
                                            .getMainFrame(), aplicacion
                                            .getI18nString("SeHanProducidoErrores"));
                                    wizardContext.cancelWizard();
                                    return;
                                } finally
                                {
                                    progressDialog.setVisible(false);
                                    geopistaEditor2.getToolBar().updateEnabledState();
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        geopistaEditor2.setVisible(true);
        
        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                .getI18nString("Inforeferencia.GuardarCambios1"));        		

        
        try {
			//Hacemos zoom a la maxima extension
			WorkbenchContext wb=geopistaEditor2.getContext();
			PlugInContext plugInContext = wb.createPlugInContext();
			plugInContext.getLayerViewPanel().getViewport().zoomToFullExtent();
		} catch (NoninvertibleTransformException e1) {

		}

    }

    private void addMessage(String message, int numeroRegistrosLeidos)
    {
        DateFormat formatter = formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");

        cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.fichero.progreso")
                + aplicacion.getI18nString(message);
        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.progreso.numero.leidos")
                + numeroRegistrosLeidos;

        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.progreso.fecha.fin")
                + formatter.format(new Date());

    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        layer09 = null;
        capaTramosVias = null;
    }// del M�todo de exiting

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
     * M�todo que dado un valor de la via busca en el dbf CarVia la fecha de
     * alta de la calle
     *
     * @param int
     *            via, valor a buscar
     * @param String
     *            rutaFicheroCarVia , fichero donde se buscar�n los datos
     * @return Date, Para obtener la fecha de valor via que se pasa como
     *         parametro
     */

    public java.util.Date localizarFecha(int via, String rutaFicheroCarVia)
    {
        java.util.Date encontrado = null;

        try
        {
            DbfFile leerDbf = new DbfFile(rutaFicheroCarVia);
            //System.out.println("abro");
            for (int k = 0; k < leerDbf.getLastRec(); k++)
            {
                StringBuffer valores = leerDbf.GetDbfRec(k);
                try
                {
                    int viaDbf = 0;
                    if (leerDbf.ParseRecordColumn(valores, 2) instanceof Integer)
                    {
                        viaDbf = ((Integer) leerDbf.ParseRecordColumn(valores, 2))
                                .intValue();
                    } else
                    {
                        viaDbf = ((Double) leerDbf.ParseRecordColumn(valores, 2))
                                .intValue();
                    }

                    if (viaDbf == via)
                    {
                        DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                        int fecha = 0;
                        if (leerDbf.ParseRecordColumn(valores, 4) instanceof Integer)
                        {
                            fecha = ((Integer) leerDbf.ParseRecordColumn(valores, 4))
                                    .intValue();
                        } else
                        {
                            fecha = ((Double) leerDbf.ParseRecordColumn(valores, 4))
                                    .intValue();
                        }
                        java.util.Date date1 = (java.util.Date) formatter1.parse(String
                                .valueOf(fecha));
                        encontrado = date1;

                        leerDbf.close();
                        //System.out.println("cierro");
                        break;
                    }

                } catch (Exception parseEx)
                {
                    parseEx.printStackTrace();
                    encontrado = null;
                    if (leerDbf !=null)
                    {
                        //System.out.println("cierro");
                        leerDbf.close();
                    }
                    break;
                }
            }
            if (leerDbf !=null)
            {
                //System.out.println("cierro");
                leerDbf.close();
            }
        } catch (Exception ex)
        {
            encontrado = null;
        }
        return encontrado;

    }

    /**
     * M�todo que dado un valor de la via busca en el dbf CarVia el nombre de la
     * calle
     *
     * @param int
     *            via, codigo a buscar
     * @param String
     *            rutaFicheroCarVia, fichero donde se buscar�
     * @return String Denominaci�n
     */

    public String localizarDenominacion(int via, String rutaFicheroCarVia)
    {
        String encontrado = null;

        try
        {
            DbfFile leerDbf = new DbfFile(rutaFicheroCarVia);
            //System.out.println("abro");
            for (int k = 0; k < leerDbf.getLastRec(); k++)
            {
                StringBuffer valores = leerDbf.GetDbfRec(k);
                try
                {
                    int viaDbf = 0;
                    if (leerDbf.ParseRecordColumn(valores, 2) instanceof Integer)
                    {
                        viaDbf = ((Integer) leerDbf.ParseRecordColumn(valores, 2))
                                .intValue();
                    } else
                    {
                        viaDbf = ((Double) leerDbf.ParseRecordColumn(valores, 2))
                                .intValue();
                    }

                    if (viaDbf == via)
                    {
                        encontrado = ((String) leerDbf.ParseRecordColumn(valores, 3));
                        if(encontrado!=null)
                        {
                            int firstBlank = encontrado.indexOf(" ");
                            encontrado = encontrado.substring(firstBlank).trim();
                        }
                        //System.out.println("cierro");
                        leerDbf.close();
                        break;
                    }

                } catch (Exception parseEx)
                {
                    parseEx.printStackTrace();
                    encontrado = null;
                    if (leerDbf !=null)
                    {
                        //System.out.println("cierro");
                        leerDbf.close();
                    }
                    break;
                }

            }
            if (leerDbf !=null)
            {
                //System.out.println("cierro");
                leerDbf.close();
            }

        }
        catch (IOException ioe){

            System.out.println("Demasiados ficheros abiertos!!");
            //ioe.printStackTrace();
        }


        catch (Exception ex)
        {
            encontrado = null;

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
     * @return GeopistaDatosTramosViaIne, Id seccion, IdSubseccion y el M�ximo +
     *         1 Id de la tabla tramos
     */
    public GeopistaDatosTramosViaIne obtenerDatosViasIne(String seccion, String subseccion)
    {

        GeopistaDatosTramosViaIne resultado = new GeopistaDatosTramosViaIne();
        try
        {
            ResultSet r = null;
            // Vamos con la seccion
            //Select id from seccionescensales where codigoine=?
            PreparedStatement ps = con
                    .prepareStatement("tramosViaIneseccionTramosViaIne");
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
        conn.setAutoCommit(false);
        return conn;
    }

    /**
     * M�todo que elimina la informaci�n de las tablas Numeros_policia,
     * tramosviaine y tramosvia
     */
    public int borrarTablasVias()
    {

        try
        {
            ResultSet rst1 = null;
            PreparedStatement ps1 = con.prepareStatement("borrarTramosViaIne");
            ps1.executeUpdate();
            ps1.close();
            ps1 = con.prepareStatement("borrarTramosVias");
            ps1.executeUpdate();
            ps1.close();
            ps1 = con.prepareStatement("borrarNumerosPolicia");
            ps1.executeUpdate();
            ps1.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    /**
     * M�todo que insertar los datos en la tabla de tramos de Via Ine
     *
     * @param idMunicipio,
     *            identificador del municipio
     * @return 0 si ha sido correcto o -1 si ha habido error
     */

    public int insertarDatosTramosViasINE(int idMunicipio,
            TaskMonitorDialog progressDialog) throws FileNotFoundException, IOException,
            SQLException
    {

        PreparedStatement std = null;
        int registrosCorrectos = 0;
        int registrosErroneos = 0;
        String str = null;

        String subseccion = "";
        String ruta = (String) blackboard.get("ficheroTextoTramosViaIne");
        File sourceFile = new File(ruta);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));

        int numReg = (int) sourceFile.length()
                / GeopistaImportarCallejeroPanel.TRAMOS_VIAS_INE_LINEA_LONGITUD;
        progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE"));
        while ((str = reader.readLine()) != null)
        {
            progressDialog
                    .report(
                            registrosErroneos + registrosCorrectos + 1,
                            numReg,
                            aplicacion
                                    .getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE"));
            try
            {
                // Obtenemos los valores
            	//Codigo de la unidad poblacional
            	String cun=str.substring(13,13+7);
            	String idViaIne=str.substring(160,160+5);
            	String nombreCortoVia=str.substring(165,165+25);
                String seccion = str.substring(72, 75);
                String tipoNumeracion = str.substring(262,263);
                String ein = str.substring(263, 267);
                String cein = str.substring(267, 268);
                String esn = str.substring(268, 272);
                String cesn = str.substring(272,273);
                GeopistaDatosTramosViaIne datos = new GeopistaDatosTramosViaIne();
                datos = obtenerDatosViasIne(seccion, subseccion);

                // A�adimos a la capa de tramosviaIne los valores que se
                // obtienen.
                // Insertamos los valores

                std = con.prepareStatement("tramosViasIneInsertar");

                //std.setInt(1, 0); // Id_Via
                //std.setInt(1, datos.getId()); // Id_Via
                std.setInt(1, Integer.parseInt(idViaIne)); // Id_Via
                
                std.setInt(2, datos.getIdSeccion()); // id_seccion
                //std.setInt(3, 0); // id_Subseccion
                std.setInt(3, datos.getIdSubSeccion()); // id_Subseccion
                //std.setInt(4, 0); // id_Pseudovia - estaba comentada
                if (!(tipoNumeracion.trim()).equals(""))
                {
                    std.setInt(5, Integer.parseInt(tipoNumeracion));
                } else
                {
                    std.setInt(5, 0);
                }
                if ((ein.trim()).equals(""))
                {
                    std.setInt(6, 0);
                } else
                {
                    std.setInt(6, Integer.parseInt(ein));
                }

                if ((cein.trim()).equals(""))
                {
                    std.setString(7, null);
                } else
                {
                    std.setString(7, cein);
                }

                if ((esn.trim()).equals(""))
                {
                    std.setInt(8, 0);
                } else
                {
                    std.setInt(8, Integer.parseInt(esn));
                }

                if ((cesn.trim()).equals(""))
                {
                    std.setString(9, "");
                } else
                {
                    std.setString(9, cesn);

                }

                std.setInt(10, idMunicipio);
                std.setString(11, cun);
                std.setString(12,nombreCortoVia);

                std.executeUpdate();
                registrosCorrectos++;

            } catch (Exception e)
            {
                registrosErroneos++;
                e.printStackTrace();

            } finally
            {
                std.close();
                con.commit();
            }

        }

        return registrosCorrectos;

    }

    public int insertarViasINE(int idMunicipio, TaskMonitorDialog progressDialog)
            throws SQLException, IOException, FileNotFoundException
    {
        PreparedStatement std = null;
        ArrayList viasIne = new ArrayList();
        int registrosCorrectos = 0;
        int registrosErroneos = 0;

        String ruta = (String) blackboard
                .get(GeopistaImportarCallejeroPanel.FICHERO_VIAS_INE);
        File sourceFile = new File(ruta);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String str = null;
        int numReg = (int) sourceFile.length()
                / GeopistaImportarCallejeroPanel.VIAS_INE_LINEA_LONGITUD;
        progressDialog
        .report(aplicacion
                        .getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
        while ((str = reader.readLine()) != null)
        {
            // Obtenemos los valores
            progressDialog
                    .report(
                            registrosErroneos + registrosCorrectos + 1,
                            numReg,
                            aplicacion
                                    .getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
            try
            {
                DatosViasINE datosViasIne = new DatosViasINE();
                datosViasIne.setCodigoViaINE(str.substring(22, 27).trim());
                datosViasIne.setTipoVia(str.substring(27, 32).trim());
                datosViasIne.setPosicionVia(str.substring(32, 33).trim());
                datosViasIne.setNombreVia(str.substring(33, 83).trim());
                datosViasIne.setNombreCorto(str.substring(83, 108).trim());

                //if (com.geopista.app.administrador.init.Constantes.MODO_REAL){
                //"insert into viasine(id,codigoviaine,id_municipio, nombrecortoINE,nombreINE,posiciontipovia,tipovia) values (seq_viasine.nextval,?,?,?,?,?,?)");
                std = con.prepareStatement("viasIneInsertar");

                std.setInt(1, Integer.parseInt(datosViasIne.getCodigoViaINE()));
                std.setInt(2, idMunicipio);
                std.setString(3, datosViasIne.getNombreCorto());
                std.setString(4, datosViasIne.getNombreVia());
                std.setInt(5, Integer.parseInt(datosViasIne.getPosicionVia()));
                std.setString(6, datosViasIne.getTipoVia());

                //std.executeUpdate();
                //std.executeQuery();
                std.execute();
                //}

                viasIne.add(datosViasIne);

                registrosCorrectos++;

            } catch (Exception e)
            {
                registrosErroneos++;
                e.printStackTrace();
                // si ocurre algun problema en una de la iteraciones
                // seguimos con la siguiente

            } finally
            {

                if (std!=null) std.close();
                con.commit();
            }
        }
        blackboard.put("viasINEInsertadas", viasIne);
        return registrosCorrectos;
    }

    /**
     * M�todo que inserta a trav�s del geopista Editor los tramos de vias a
     * partir del fichero de ejes y carvia
     *
     * @param int
     *            idMunicipio codigo del municipio
     */
    public int insertarTramosVias(TaskMonitorDialog progressDialog) throws Exception
    {
        boolean manualModification = ((Boolean) blackboard.get("mostrarError"))
                .booleanValue();
        boolean firingEvents = false;

        boolean lastInsertionState = geopistaValidatePlugIn.isMakeInsertion();
        geopistaValidatePlugIn.setMakeInsertion(false);

        int registrosCorrectos = 0;
        int registrosErroneos = 0;
        ArrayList tramosViasLista = new ArrayList();

            String carVia = (String) blackboard.get("rutaFicheroCarVia");
            // Recorrer todas las features del Layer08.
            List listaLayer = capaTramosVias.getFeatureCollectionWrapper().getFeatures();

            GeopistaLayer localCapaTramosVias = (GeopistaLayer) geopistaEditor2
                    .getLayerManager().getLayer(GeopistaSystemLayers.TRAMOSVIAS);
            GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localCapaTramosVias
            .getDataSourceQuery().getDataSource();

            //si hay tramos de vias de otras importaciones los eliminamos.
/*            if(localCapaTramosVias.getFeatureCollectionWrapper().getFeatures().size()>0)
            {
                progressDialog.report(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
                Iterator lastFeaturesIter = localCapaTramosVias.getFeatureCollectionWrapper().getFeatures().iterator();
                while (lastFeaturesIter.hasNext())
                {
                    GeopistaFeature deleteCurrentFeature = (GeopistaFeature) lastFeaturesIter.next();
                    deleteCurrentFeature.setDeleted(true);
                }

                geopistaServerDataSource.getConnection().executeUpdate(localCapaTramosVias.getDataSourceQuery().getQuery(), localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
                localCapaTramosVias.getFeatureCollectionWrapper().removeAll(localCapaTramosVias.getFeatureCollectionWrapper().getFeatures());
            }
*/

            firingEvents = localCapaTramosVias.getLayerManager().isFiringEvents();
            localCapaTramosVias.getLayerManager().setFiringEvents(false);

            Iterator itLayer = listaLayer.iterator();
            progressDialog
            .report(aplicacion
                            .getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias"));
            while (itLayer.hasNext())
            {
                progressDialog
                        .report(
                                registrosCorrectos + registrosErroneos,
                                listaLayer.size(),
                                aplicacion
                                        .getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias"));
                // Obtener una feature

                Feature f = (Feature) itLayer.next();
                int via = Integer.parseInt(f.getString("VIA"));
                Geometry geometria = f.getGeometry();
                // Con el C�digo de la Via debo localizar la denominaci�n en
                // CarVia

                String nombreCalle = localizarDenominacion(via, carVia);

                java.util.Date fechaAlta = localizarFecha(via, carVia);
                // Si la ha encontrado actualizar los valores y dejar el ID.
                GeopistaSchema featureSchema = (GeopistaSchema) localCapaTramosVias
                        .getFeatureCollectionWrapper().getFeatureSchema();
                GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);

                if(!f.getString("FECHABAJA").trim().equals("99999999")) continue;
                currentFeature.setGeometry(f.getGeometry());
                currentFeature.setAttribute(featureSchema.getAttributeByColumn("numvia"),
                        new Integer(via));
                currentFeature.setAttribute(featureSchema
                        .getAttributeByColumn("denominacion"), nombreCalle);
                currentFeature.setAttribute(featureSchema
                        .getAttributeByColumn("fechaalta"), new Date());
                ((GeopistaFeature) currentFeature).setLayer(localCapaTramosVias);

                boolean validateResult = false;
                boolean cancelImport = false;

                SchemaValidator validator = new SchemaValidator(null);

                while (!(validateResult = validator.validateFeature(currentFeature)))
                {
                    if (!manualModification)
                    {
                        break;
                    }
                    FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(
                            localCapaTramosVias, currentFeature);
                    if (featureDialog.wasOKPressed())
                    {
                        Feature clonefeature = featureDialog.getModifiedFeature();
                        currentFeature.setAttributes(clonefeature.getAttributes());
                    } else
                    {
                        Object[] possibleValues = {
                                aplicacion.getI18nString("CancelarEsteElemento"),
                                aplicacion.getI18nString("CancelarTodaLaImportacion"),
                                aplicacion.getI18nString("IgnorarFuturosErrores") };
                        int selectedValueCancel = JOptionPane
                                .showOptionDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                                .getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
                                        aplicacion
                                                .getI18nString("GeopistaImportacionLog.BorrarDuplicar"),
                                        0, JOptionPane.QUESTION_MESSAGE, null,
                                        possibleValues, possibleValues[0]);
                        if (selectedValueCancel == 2)
                            manualModification = false;
                        if (selectedValueCancel == 1)
                            cancelImport = true;
                        break;
                    }

                }

                if (validateResult)
                {
                    registrosCorrectos++;
                } else
                {
                    registrosErroneos++;
                }
                if (cancelImport == true)
                    break;
                /**
                 * Fin de la validaci�n manual
                 */
                if (validateResult)
                {
                    localCapaTramosVias.getFeatureCollectionWrapper().add(currentFeature);
                }

            }
            geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
            progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));

            Map driverProperties = geopistaServerDataSource.getProperties();
            Object lastResfreshValue = driverProperties
                    .get(GeopistaConnection.REFRESH_INSERT_FEATURES);
            FeatureCollection featureCollectionAux = null;
            try
            {
                driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,
                        new Boolean(true));
                GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
				ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
				//String sridDefecto = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
				String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
				logger.info("Insercion de vias:"+sridDefecto);
            	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
            	driverPropertiesUpdate.put("srid_destino",new Integer(sridDefecto));
            	Iterator itFeature = localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee().getFeatures().iterator();
   	    	 	featureCollectionAux = AddNewLayerPlugIn.createBlankFeatureCollection();
	            GeopistaSchema featureSchema = (GeopistaSchema) localCapaTramosVias
                    .getFeatureCollectionWrapper().getFeatureSchema();
	            String attrMuni = featureSchema.getAttributeByColumn("id_municipio");
	    	 	String sMunicipio = ((ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY)).getIdMunicipio();
   	    	 	while (itFeature.hasNext()){
   	    	 		Feature feature = (Feature)itFeature.next();
   	    	 		if (sMunicipio.equals(feature.getAttribute(attrMuni)))
   	    	 			featureCollectionAux.add((Feature)feature.clone());
   	    	 	}
   	    	 	AppContext.getApplicationContext().getBlackboard().put(AppContext.IMPORTACIONES, true);
            	new GeopistaConnection(driverPropertiesUpdate).executeUpdate(
            			localCapaTramosVias.getDataSourceQuery().getQuery(),
            			featureCollectionAux, progressDialog);
            }catch(Exception e){
            	e.printStackTrace();
            } finally
            {
                try
                {
                    geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
                	localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee().clear();
                	localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee().addAll(featureCollectionAux.getFeatures());
                } catch (Exception e)
                {
                }
                try
                {
                    localCapaTramosVias.getLayerManager().setFiringEvents(firingEvents);
                } catch (Exception e)
                {
                }

                if (lastResfreshValue != null)
                {
                    driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,
                            lastResfreshValue);
                } else
                {
                    driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
                }
            }

        return registrosCorrectos;
    }

    /**
     * M�todo para introducir los numeros de policia a traves del editor
     *
     * @param int
     *            idMunicipo
     */

    public int introducirNumerosPolicia(TaskMonitorDialog progressDialog)
            throws Exception
    {

        boolean manualModification = ((Boolean) blackboard.get("mostrarError"))
                .booleanValue();
        boolean firingEvents = false;



        int registrosCorrectos = 0;
        int registrosErroneos = 0;
        GeopistaLayer localCapaNumerosPolicia = (GeopistaLayer) geopistaEditor2
                .getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);


        firingEvents = localCapaNumerosPolicia.getLayerManager().isFiringEvents();


        List listaLayer = layer09.getFeatureCollectionWrapper().getFeatures();
        Iterator itLayer = listaLayer.iterator();
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
        try
        {
        localCapaNumerosPolicia.getLayerManager().setFiringEvents(false);
        while (itLayer.hasNext())
        {
            // Obtener una feature
            progressDialog.report(registrosCorrectos + registrosErroneos, listaLayer
                    .size(), aplicacion
                    .getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
            Feature f = (Feature) itLayer.next();
            Geometry geometria = f.getGeometry();

            GeopistaSchema featureSchema = (GeopistaSchema) localCapaNumerosPolicia
                    .getFeatureCollectionWrapper().getFeatureSchema();
            GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);

            currentFeature.setGeometry(f.getGeometry());
            currentFeature.setAttribute(featureSchema.getAttributeByColumn("rotulo"), f
                    .getString("ROTULO").trim());
            ((GeopistaFeature) currentFeature).setLayer(localCapaNumerosPolicia);

            boolean validateResult = false;
            boolean cancelImport = false;

            SchemaValidator validator = new SchemaValidator(null);

            while (!(validateResult = validator.validateFeature(currentFeature)))
            {
                if (!manualModification)
                {
                    break;
                }
                FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(
                        localCapaNumerosPolicia, currentFeature);
                if (featureDialog.wasOKPressed())
                {
                    Feature clonefeature = featureDialog.getModifiedFeature();
                    currentFeature.setAttributes(clonefeature.getAttributes());
                } else
                {
                    Object[] possibleValues = {
                            aplicacion.getI18nString("CancelarEsteElemento"),
                            aplicacion.getI18nString("CancelarTodaLaImportacion"),
                            aplicacion.getI18nString("IgnorarFuturosErrores") };
                    int selectedValueCancel = JOptionPane
                            .showOptionDialog(
                                    aplicacion.getMainFrame(),
                                    aplicacion
                                            .getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
                                    aplicacion
                                            .getI18nString("GeopistaImportacionLog.BorrarDuplicar"),
                                    0, JOptionPane.QUESTION_MESSAGE, null,
                                    possibleValues, possibleValues[0]);
                    if (selectedValueCancel == 2)
                        manualModification = false;
                    if (selectedValueCancel == 1)
                        cancelImport = true;
                    break;
                }

            }

            if (validateResult)
            {
                registrosCorrectos++;
            } else
            {
                registrosErroneos++;
            }
            if (cancelImport == true)
                break;
            /**
             * Fin de la validaci�n manual
             */
            if (validateResult)
            {
                localCapaNumerosPolicia.getFeatureCollectionWrapper().add(currentFeature);
            }

        }
        }finally
        {
            localCapaNumerosPolicia.getLayerManager().setFiringEvents(firingEvents);
        }

        return registrosCorrectos;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        layer09 = null;
        capaTramosVias = null;
        geopistaEditor2 = null;
        geopistaValidatePlugIn  = null;
        // TODO Auto-generated method stub

    }
    public static void main(String args[]){
    	String cadena="00089";
    	
    	int entero=Integer.parseInt(cadena);
    	System.out.println("Cadena:"+entero);
    }

} // de la clase general.
