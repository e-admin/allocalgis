/**
 * GeopistaMostrarCallejeroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import java.util.Hashtable;
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
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.GeopistaDatosTramosViaIne;
import com.geopista.app.utilidades.ProcessCancel;
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
import com.vividsolutions.jump.coordsys.CoordinateSystem;
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
                                	
                                	ProcessCancel processCancel=null;			
			        				if (progressDialog!=null){				
			        					if (progressDialog!=null){
			        						processCancel=new ProcessCancel(progressDialog);
			        						processCancel.start();
			        					}
			        				}

                                    cadenaTexto = "";
                                    String ruta = (String) blackboard.get("mapaCargarTramosVias");
                                    String rutaPolicia = (String) blackboard.get("ficheroNumerosPolicia");

                                    String rutaCarvia = (String) blackboard.get("rutaFicheroCarVia");
                                    String rutaEjes = (String) blackboard.get("rutaFicheroEjes");
                                    ruta = rutaEjes;
                                    rutaEjes = rutaEjes.substring(0,
                                            rutaEjes.length() - 3) + "dbf";
                                    
    				                String idMunicipioSesion=((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
                                    int idMunicipio = Integer.parseInt(idMunicipioSesion);


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

                                            
                                            

                                            // Layer08 estarï¿½ el fichero de
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
                                            // datos del Mapa de Informaciï¿½n
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

                                        addMessage("importar.informacion.referencia.viasINE",numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        numeroRegistrosLeidos = insertarDatosTramosViasINE(idMunicipio, progressDialog);

                                        addMessage("importar.informacion.referencia.tramos.via.ine",numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        numeroRegistrosLeidos = insertarTramosVias(progressDialog);

                                        addMessage("importar.informacion.referencia.tramos.via",numeroRegistrosLeidos);

                                        //if (com.geopista.app.administrador.init.Constantes.MODO_REAL)
                                        	numeroRegistrosLeidos = introducirNumerosPolicia(progressDialog);

                                        addMessage("importar.informacion.referencia.numeros.de.policia",numeroRegistrosLeidos);

                                        jedResumen.setText(cadenaTexto);

                                        blackboard.put(
                                                "geopistaEditorEnlazarPoliciaCalles",
                                                geopistaEditor2);
                                        
                                        processCancel.terminateProcess();
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
    }// del Mï¿½todo de exiting

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
     * Mï¿½todo que dado un valor de la via busca en el dbf CarVia la fecha de
     * alta de la calle
     *
     * @param int
     *            via, valor a buscar
     * @param String
     *            rutaFicheroCarVia , fichero donde se buscarï¿½n los datos
     * @return Date, Para obtener la fecha de valor via que se pasa como
     *         parametro
     */

    public static java.util.Date localizarFecha(int via, String rutaFicheroCarVia)
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
     * Mï¿½todo que dado un valor de la via busca en el dbf CarVia el nombre de la
     * calle
     *
     * @param int
     *            via, codigo a buscar
     * @param String
     *            rutaFicheroCarVia, fichero donde se buscarï¿½
     * @return String Denominaciï¿½n
     */

    public static String localizarDenominacion(int via, String rutaFicheroCarVia)
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
     * @return GeopistaDatosTramosViaIne, Id seccion, IdSubseccion y el Mï¿½ximo +
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
     * Devuelve una clase con los valores necesarios de los TramosViaIne
     * 
     * @param String
     *            seccion
     * @param String
     *            subseccion
     * @return GeopistaDatosTramosViaIne, Id seccion, IdSubseccion y el Máximo +
     *         1 Id de la tabla tramos
     */
    public static GeopistaDatosTramosViaIne obtenerDatosViasIne(String seccion, String subseccion, Connection con)
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
     * Mï¿½todo que elimina la informaciï¿½n de las tablas Numeros_policia,
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
     * Mï¿½todo que insertar los datos en la tabla de tramos de Via Ine
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
            
            
            
            try{            	
            	String idMunicipioCadena=str.substring(0,5);            	
            	if (!idMunicipioCadena.equals(String.valueOf(idMunicipio))){
            		continue;	
            	}
            }
            catch (Exception e){
            }
            
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

                // Aï¿½adimos a la capa de tramosviaIne los valores que se
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
        int registrosOtrosMunicipios=0;

        String ruta = (String) blackboard.get(GeopistaImportarCallejeroPanel.FICHERO_VIAS_INE);
        File sourceFile = new File(ruta);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String str = null;
        int numReg = (int) sourceFile.length()
                / GeopistaImportarCallejeroPanel.VIAS_INE_LINEA_LONGITUD;
        progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
        
        while ((str = reader.readLine()) != null)
        {
            // Obtenemos los valores
            progressDialog
                    .report(
                            registrosErroneos + registrosCorrectos + 1,
                            numReg,aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
            
            
            try{            	
            	String idMunicipioCadena=str.substring(0,5);            	
            	if (!idMunicipioCadena.equals(String.valueOf(idMunicipio))){
            		registrosOtrosMunicipios++;
            		continue;	
            	}
            }
            catch (Exception e){
            }
            
            
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
        blackboard.put("registrosOtrosMunicipios",registrosOtrosMunicipios);
        return registrosCorrectos;
    }

    /**
     * Mï¿½todo que inserta a travï¿½s del geopista Editor los tramos de vias a
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
                // Con el Cï¿½digo de la Via debo localizar la denominaciï¿½n en
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
                
                currentFeature.setAttribute(featureSchema
                        .getAttributeByColumn("valido"), 1);
                
                String idMunicipioAttName=featureSchema.getAttributeByColumn("id_municipio");
                String idMunicipioSesion=((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
				currentFeature.setAttribute(idMunicipioAttName,idMunicipioSesion);


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
                 * Fin de la validaciï¿½n manual
                 */
                if (validateResult)
                {
                    localCapaTramosVias.getFeatureCollectionWrapper().add(currentFeature);
                }

            }
            geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
            progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));

            
          //EPSG
			CoordinateSystem originalEPSG=null;
			try {
				originalEPSG=(CoordinateSystem)blackboard.get("selectedImportEPSG");						
				//blackboard.remove("selectedImportEPSG");
			} catch (Exception e1) {
			}
            
            Map driverProperties = geopistaServerDataSource.getProperties();
            Object lastResfreshValue = driverProperties
                    .get(Constantes.REFRESH_INSERT_FEATURES);
            FeatureCollection featureCollectionAux = null;
            try
            {
                driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(true));
				driverProperties.put("selectedImportEPSG",originalEPSG);

                GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
				ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
				//String sridDefecto = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
				String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
				logger.info("Insercion de vias:"+sridDefecto);
            	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
            	driverPropertiesUpdate.put("srid_destino",new Integer(sridDefecto));
            	Iterator itFeature = localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee().getFeatures().iterator();
   	    	 	featureCollectionAux = AddNewLayerPlugIn.createBlankFeatureCollection();
	           
   	    	 	
   	    	 	GeopistaSchema featureSchema = (GeopistaSchema) localCapaTramosVias.getFeatureCollectionWrapper().getFeatureSchema();
	            String attrMuni = featureSchema.getAttributeByColumn("id_municipio");
	    	 	String sMunicipio = ((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
   	    	 	while (itFeature.hasNext()){
   	    	 		Feature feature = (Feature)itFeature.next();
   	    	 		if (sMunicipio.equals(feature.getAttribute(attrMuni)))
   	    	 			featureCollectionAux.add((Feature)feature.clone());
   	    	 	}
   	    	 	AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.IMPORTACIONES, true);
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
                    driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,
                            lastResfreshValue);
                } else
                {
                    driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
                }
            }

        return registrosCorrectos;
    }

    /**
     * Mï¿½todo para introducir los numeros de policia a traves del editor
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
        boolean lastInsertionState = geopistaValidatePlugIn.isMakeInsertion();


        int registrosCorrectos = 0;
        int registrosErroneos = 0;
        GeopistaLayer localCapaNumerosPolicia = (GeopistaLayer) geopistaEditor2
                .getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
        GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localCapaNumerosPolicia
                .getDataSourceQuery().getDataSource();

        firingEvents = localCapaNumerosPolicia.getLayerManager().isFiringEvents();


        List listaLayer = layer09.getFeatureCollectionWrapper().getFeatures();
        Iterator itLayer = listaLayer.iterator();
        progressDialog.report(aplicacion
                .getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
  
        localCapaNumerosPolicia.getLayerManager().setFiringEvents(false);
        while (itLayer.hasNext())
        {
            // Obtener una feature
            progressDialog.report(registrosCorrectos + registrosErroneos, listaLayer
                    .size(), aplicacion
                    .getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
            Feature f = (Feature) itLayer.next();
            
            String TTGGSS=f.getString("TTGGSS").trim();
		    
		    //Los numeros de policia tienen identificador TTGGSS=189401 el resto no es necesario cargarlo.
		    if (!TTGGSS.equals("189401"))
		    	continue;
		    
            Geometry geometria = f.getGeometry();
            
            

            GeopistaSchema featureSchema = (GeopistaSchema) localCapaNumerosPolicia
                    .getFeatureCollectionWrapper().getFeatureSchema();
            GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);

            currentFeature.setGeometry(f.getGeometry());
            currentFeature.setAttribute(featureSchema.getAttributeByColumn("rotulo"), f
                    .getString("ROTULO").trim());
            ((GeopistaFeature) currentFeature).setLayer(localCapaNumerosPolicia);
            
            
            String idMunicipioAttName=featureSchema.getAttributeByColumn("id_municipio");
            String idMunicipioSesion=((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
			currentFeature.setAttribute(idMunicipioAttName,idMunicipioSesion);
			currentFeature.setAttribute(featureSchema.getAttributeByColumn("valido"), 1);


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
             * Fin de la validaciï¿½n manual
             */
            if (validateResult)
            {
                localCapaNumerosPolicia.getFeatureCollectionWrapper().add(currentFeature);
            }

        }
        
        geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
        progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
        
        //EPSG
		CoordinateSystem originalEPSG=null;
		try {
			originalEPSG=(CoordinateSystem)blackboard.get("selectedImportEPSG");						
			//blackboard.remove("selectedImportEPSG");
		} catch (Exception e1) {
		}
		Map driverProperties = geopistaServerDataSource.getProperties();
        Object lastResfreshValue = driverProperties
                .get(Constantes.REFRESH_INSERT_FEATURES);
        FeatureCollection featureCollectionAux = null;
        try
        {
            driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(true));
			driverProperties.put("selectedImportEPSG",originalEPSG);

            GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
			ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY);
			//String sridDefecto = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
			String sridDefecto = geoConn.getSRIDDefecto(false, Integer.parseInt(iSesion.getIdEntidad()));
			logger.info("Insercion de vias:"+sridDefecto);
        	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
        	driverPropertiesUpdate.put("srid_destino",new Integer(sridDefecto));
        	Iterator itFeature = localCapaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee().getFeatures().iterator();
	    	 	featureCollectionAux = AddNewLayerPlugIn.createBlankFeatureCollection();
           
	    	 	
	    	GeopistaSchema featureSchema = (GeopistaSchema) localCapaNumerosPolicia.getFeatureCollectionWrapper().getFeatureSchema();
            String attrMuni = featureSchema.getAttributeByColumn("id_municipio");
    	 	String sMunicipio = ((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
	    	 	while (itFeature.hasNext()){
	    	 		Feature feature = (Feature)itFeature.next();
	    	 		if (sMunicipio.equals(feature.getAttribute(attrMuni)))
	    	 			featureCollectionAux.add((Feature)feature.clone());
	    	 	}
	    	 	AppContext.getApplicationContext().getBlackboard().put(UserPreferenceConstants.IMPORTACIONES, true);
        	new GeopistaConnection(driverPropertiesUpdate).executeUpdate(
        			localCapaNumerosPolicia.getDataSourceQuery().getQuery(),
        			featureCollectionAux, progressDialog);
        }catch(Exception e){
        	e.printStackTrace();
        } finally
        {

            try
            {
                geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
                localCapaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee().clear();
                localCapaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee().addAll(featureCollectionAux.getFeatures());
            } catch (Exception e)
            {
            }
            try
            {
            	localCapaNumerosPolicia.getLayerManager().setFiringEvents(firingEvents);
            } catch (Exception e)
            {
            }

            if (lastResfreshValue != null)
            {
                driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,
                        lastResfreshValue);
            } else
            {
                driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
            }

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
    
    
    //METODOS PARA CARGAR CALLEJEROS DESDE LINEA DE COMANDO
    
    
    /**
     * Método que insertar los datos en la tabla de tramos de Via Ine
     * 
     * @return número de registros correctos
     */
    public static int insertarDatosTramosViasINE(AppContext aplicacion, Blackboard blackboard, Connection con, boolean isGraphic, TaskMonitorDialog progressDialog) throws FileNotFoundException, IOException,SQLException
    {
        int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
        PreparedStatement std = null;
        int registrosCorrectos = 0;
        int registrosErroneos = 0;
        String str = null;

        String subseccion = "";
        String ruta = (String) blackboard.get("ficheroTextoTramosViaIne");
        File sourceFile = new File(ruta);
        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));

        int numReg = (int) sourceFile.length() / GeopistaImportarCallejeroPanel.TRAMOS_VIAS_INE_LINEA_LONGITUD;
        if(isGraphic)
        	progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE"));
        else
        	logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE"));
        boolean encontradoMunicipio=false;
		while ((str = reader.readLine()) != null){
			if(str.substring(0,5).trim().equals(String.valueOf(idMunicipio))){
				encontradoMunicipio=true;
	        	if(isGraphic)
	        		progressDialog.report(registrosErroneos + registrosCorrectos + 1,numReg,aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE"));
	        	else
	        		logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoTramosViasINE")+" "+(registrosErroneos+registrosCorrectos+1)+"/"+numReg);
	            try{
	                // Obtenemos los valores
	                String seccion = str.substring(72, 75);
	                String tipoNumeracion = str.substring(262,263);
	                String ein = str.substring(263, 267);
	                String cein = str.substring(267, 268);
	                String esn = str.substring(268, 272);
	                String cesn = str.substring(272,273);
	                GeopistaDatosTramosViaIne datos = new GeopistaDatosTramosViaIne();
	                datos = obtenerDatosViasIne(seccion, subseccion, con);
	
	                // Añadimos a la capa de tramosviaIne los valores que se obtienen Insertamos los valores
	
	                std = con.prepareStatement("tramosViasIneInsertar");
	
	                //std.setInt(1, 0); // Id_Via 
	                std.setInt(1, datos.getId()); // Id_Via 
	                std.setInt(2, datos.getIdSeccion()); // id_seccion
	                //std.setInt(3, 0); // id_Subseccion 
	                std.setInt(3, datos.getIdSubSeccion()); // id_Subseccion
	                //std.setInt(4, 0); // id_Pseudovia - estaba comentada
	                if (!(tipoNumeracion.trim()).equals("")){
	                    std.setInt(5, Integer.parseInt(tipoNumeracion));
	                } else{
	                    std.setInt(5, 0);
	                }
	                if ((ein.trim()).equals("")){
	                    std.setInt(6, 0);
	                } else{
	                    std.setInt(6, Integer.parseInt(ein));
	                }
	
	                if ((cein.trim()).equals("")){
	                    std.setString(7, null);
	                } else{
	                    std.setString(7, cein);
	                }
	
	                if ((esn.trim()).equals("")){
	                    std.setInt(8, 0);
	                } else{
	                    std.setInt(8, Integer.parseInt(esn));
	                }
	
	                if ((cesn.trim()).equals("")){
	                    std.setString(9, "");
	                } else{
	                    std.setString(9, cesn);
	                }
	
	                std.setInt(10, idMunicipio);
	
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
			}else{
				if(encontradoMunicipio)
					break;
			}

        }

        return registrosCorrectos;

    }
    
    /**
     * Método que borra los registros vias ine de la base de datos para un municipio dado
     * 
     * @param aplicacion
     * @param con
     * @param blackboard
     * @throws SQLException
     * @throws Exception
     */
    
    public static void borrarViasINE(AppContext aplicacion, Blackboard blackboard, Connection con) throws SQLException, Exception
	{
		int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
		PreparedStatement std = null;
		
		try{			
			std = con.prepareStatement("viasIneBorrar");
			std.setInt(1, idMunicipio);
			
			std.executeUpdate();
			
		}catch (SQLException sqlex){
			System.err.println("Ha ocurrido una SQLException: " + sqlex.getMessage());
			System.err.println("Se necesita tener la query \"viasIneBorrar\" en la tabla query_catalog en la base de datos. ");
			System.err.println("Por favor actualizarse la base de datos gracias a los ficheros presentes en la carpeta \"sql\"");
			throw sqlex;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		} finally{
			std.close();
			std=null;
			con.commit();
		}
		
		blackboard.put("viasINEInsertadas", null);
	}
    
    
    /**
     * Método que borra los registros tramos vias INE de la base de datos para un municipio dado
     * 
     * @param aplicacion
     * @param con
     * @param blackboard
     * @throws SQLException
     * @throws Exception
     */
    
    public static void borrarTramosViasINE(AppContext aplicacion, Blackboard blackboard, Connection con) throws SQLException, Exception
	{
		int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
		PreparedStatement std = null;
		
		try{			
			std = con.prepareStatement("tramosViasIneBorrar");
			std.setInt(1, idMunicipio);
			
			std.executeUpdate();
		}catch (SQLException sqlex){
			System.err.println("Ha ocurrido una SQLException: " + sqlex.getMessage());
			System.err.println("Se necesita tener la query \"tramosViasIneBorrar\" en la tabla query_catalog en la base de datos. ");
			System.err.println("Por favor actualizarse la base de datos gracias a los ficheros presentes en la carpeta \"sql\"");
			throw sqlex;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		} finally{
			std.close();
			std=null;
			con.commit();
		}
	}
    

    public static int insertarViasINE(AppContext aplicacion, String ruta, Connection con, Blackboard blackboard, boolean isGraphic, TaskMonitorDialog progressDialog) throws SQLException, IOException, FileNotFoundException
	{
        int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
		PreparedStatement std = null;
		ArrayList viasIne = new ArrayList();
		int registrosCorrectos = 0;
		int registrosErroneos = 0;
		
		//String ruta = (String) blackboard.get(GeopistaImportarCallejeroPanel.FICHERO_VIAS_INE);
		File sourceFile = new File(ruta);
		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		String str = null;
		int numReg = (int) sourceFile.length()  / GeopistaImportarCallejeroPanel.VIAS_INE_LINEA_LONGITUD;
		
		if(isGraphic)
			progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
		else
			logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
		boolean encontradoMunicipio=false;
		while ((str = reader.readLine()) != null){
			
			//********************************************
			//Si el fichero tiene todas las vias de la provincia, solo 
			//cargamos las que en los cinco primeros dígitos tiene el
			//codigo del municipio
			//********************************************
			if(str.substring(0,5).trim().equals(String.valueOf(idMunicipio))){
				encontradoMunicipio=true;
			    // Obtenemos los valores
				if(isGraphic)
					progressDialog.report(registrosErroneos + registrosCorrectos + 1,numReg,aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE"));
				else
					logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.insertandoViasINE")+" "+(registrosErroneos+registrosCorrectos+1)+"/"+numReg);
			    try{
			        DatosViasINE datosViasIne = new DatosViasINE();
			        datosViasIne.setCodigoViaINE(str.substring(22, 27).trim());
			        datosViasIne.setIdMunicipio(idMunicipio+"");
			        datosViasIne.setTipoVia(str.substring(27, 32).trim());
			        datosViasIne.setPosicionVia(str.substring(32, 33).trim());
			        datosViasIne.setNombreVia(str.substring(33, 83).trim());
			        datosViasIne.setNombreCorto(str.substring(83, 108).trim());
			
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
			
			        viasIne.add(datosViasIne);
			
			        registrosCorrectos++;
			
			    } catch (Exception e){
			        registrosErroneos++;
			        e.printStackTrace();
			        // si ocurre algun problema en una de la iteraciones seguimos con la siguiente
			
			    } finally{		
			        std.close();
			        con.commit();
			    }
			}else{
				if(encontradoMunicipio)
					break;
			}
		}
		blackboard.put("viasINEInsertadas", viasIne);
		return registrosCorrectos;
	}

    /**
     * Insert into the database information retrieved from the CarVia file. 
     * 
     * @param rutaFicheroCarVia
     * @param geopistaEditor2
     * @param con
     * @param idMunicipio
     */
    public static void insertarCarVias(String rutaFicheroCarVia, GeopistaEditor geopistaEditor2, Connection con, int idMunicipio){
		try{
			
			DbfFile leerDbf = new DbfFile(rutaFicheroCarVia);
			GeopistaLayer localCapaTramosVias = (GeopistaLayer) geopistaEditor2.getLayerManager().getLayer(GeopistaSystemLayers.TRAMOSVIAS);
			GeopistaSchema featureSchema = (GeopistaSchema) localCapaTramosVias.getFeatureCollectionWrapper().getFeatureSchema();
			List myCarVias = new ArrayList();
			Hashtable targetHash = new Hashtable();
			
			for (int k = 0; k < leerDbf.getLastRec(); k++){
				StringBuffer valores = leerDbf.GetDbfRec(k);
				GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);
				try{
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

					DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
					int fecha = 0;
					if (leerDbf.ParseRecordColumn(valores, 5) instanceof Integer)
					{
						fecha = ((Integer) leerDbf.ParseRecordColumn(valores, 5))
								.intValue();
					} else
					{
						fecha = ((Double) leerDbf.ParseRecordColumn(valores, 5))
								.intValue();
					}
					java.util.Date date1 = (java.util.Date) formatter1.parse(String
							.valueOf(fecha));


					currentFeature.setAttribute(featureSchema.getAttributeByColumn("numvia"),new Integer(viaDbf));
					currentFeature.setAttribute(featureSchema.getAttributeByColumn("FechaBaja"), date1);
					myCarVias.add(currentFeature);


				} catch (Exception parseEx)
				{
					parseEx.printStackTrace();
					if (leerDbf !=null) 
					{
						leerDbf.close();
					}
					break;
				}
			}

			if (leerDbf !=null) 
			{
				leerDbf.close();
			}


			// now we go through the loop and add for each currentFeature its name and its Since Date
			
			for (int i=0; i<myCarVias.size(); i++){
				GeopistaFeature aCurrentFeature = (GeopistaFeature) myCarVias.get(i);

				aCurrentFeature.setAttribute(featureSchema.getAttributeByColumn("denominacion"), localizarDenominacion(((Integer)aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue(), rutaFicheroCarVia));
				aCurrentFeature.setAttribute(featureSchema.getAttributeByColumn("FechaAlta"), localizarFecha(((Integer)aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue(), rutaFicheroCarVia));

				myCarVias.remove(i);
				myCarVias.add(i, aCurrentFeature);
			}
			
			// now myCarVias is loaded with streets from carvia. we need to check
			// that those streets haven't been inserted previously in the database (ej: Rustico - Urbano)
	        try{
	        	
	        	if (con == null) {
	        		con =  AppContext.getApplicationContext().getConnection();
	        	}
	        	PreparedStatement std = con.prepareStatement("selectViasSinRepresentacion");
	        	
  				std.setInt(1, idMunicipio);
  				
  				if (std.execute()){
	  				ResultSet rs = std.getResultSet();
			              
			        while (rs.next()){
			        	Hashtable featureData = new Hashtable();
			            	  
			            try {
			            	  
			            	int iBlankSpacePos = rs.getObject(2).toString().indexOf(" ");
			            	
			            	featureData.put("denominacion",rs.getObject(3).toString().trim());
			            	featureData.put("tipovianormalizadocatastro", rs.getObject(2).toString().trim());
			            	
			            	String currentIdVia = rs.getObject(1).toString();
				            	  
			            	targetHash.put(currentIdVia,featureData);
				            
			            }catch (Exception e){
			            }
			        }
			      rs.close();
			      std.close();
			      con.commit();
  				}
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }
			
			for (int i=0; i<myCarVias.size(); i++){
				GeopistaFeature aCurrentFeature = (GeopistaFeature) myCarVias.get(i);
				
				String sDenominacionCarVia = (String) aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("denominacion"));
				
				for (int j=0; j<targetHash.size(); j++){
					Hashtable featureData = (Hashtable) targetHash.get(j);
					
					String sDenominacionDDBB = featureData.get("tipovianormalizadocatastro") + " " + featureData.get("denominacion");
					
					if (sDenominacionCarVia.equals(sDenominacionDDBB)){
						myCarVias.remove(i);
					}
				}
			}
			

			// insertar en la bbdd
			PreparedStatement std = null;

			try {

				String sStreetType = "";
				String sStreetName = "";
				String sStreetID = "";
				int iSpacePos = 0;

				for (int i=0; i<myCarVias.size(); i++){
					sStreetType = "";
					GeopistaFeature aCurrentFeature = (GeopistaFeature) myCarVias.get(i);
					std = con.prepareStatement("ViasCatastroInsertar");
					sStreetID = idMunicipio + "" + ((Integer) aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue();
					std.setInt(1, Integer.parseInt(sStreetID));
					std.setInt(2, ((Integer) aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue());
					sStreetName = (String) aCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("denominacion"));
					iSpacePos = sStreetName.indexOf(" ");
					if (iSpacePos==2){
						sStreetType = sStreetName.substring(0, 2);
						sStreetName = sStreetName.substring(3);
					}
					std.setString(3,sStreetType);
					std.setString(4, sStreetName);
					std.setInt(5, idMunicipio);

					std.executeUpdate();
				}
			} catch (Exception e)
			{
				e.printStackTrace();

			} finally
			{
				
				std.close();  
				con.commit();
			}


			// delete vias referenced in tramosvia table
			std = null;

			try {
				std = con.prepareStatement("DeleteReferencedVias");
				std.setInt(1, idMunicipio);
				std.setInt(2, idMunicipio);
				std.executeUpdate();
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				std.close();
				con.commit();
			}
		} catch (IOException ioe){
			System.out.println("Demasiados ficheros abiertos!!");
		} catch (Exception ex)
		{
		}

	}
    
    
    /**
     * Método que inserta a través del geopista Editor los tramos de vias a
     * partir del fichero de ejes y carvia
     * 
     */
	public static int insertarTramosVias(String carVia, AppContext aplicacion, Blackboard blackboard, GeopistaValidatePlugin geopistaValidatePlugIn, GeopistaEditor geopistaEditor2, GeopistaLayer capaTramosVias, boolean isGraphic, TaskMonitorDialog progressDialog) throws Exception
	{
		boolean manualModification = false;
		if(isGraphic)
			manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
		boolean firingEvents = false;
		boolean lastInsertionState = geopistaValidatePlugIn.isMakeInsertion();
		geopistaValidatePlugIn.setMakeInsertion(false);

		int registrosCorrectos = 0;
		int registrosErroneos = 0;

		// Recorrer todas las features del Layer08.
		List listaLayerSHP = capaTramosVias.getFeatureCollectionWrapper().getFeatures();

		GeopistaLayer localCapaTramosVias = (GeopistaLayer) geopistaEditor2.getLayerManager().getLayer(GeopistaSystemLayers.TRAMOSVIAS);
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localCapaTramosVias.getDataSourceQuery().getDataSource();

		GeopistaSchema featureSchema = (GeopistaSchema) localCapaTramosVias.getFeatureCollectionWrapper().getFeatureSchema();

		//si hay tramos de vias de otras importaciones los eliminamos.
		if(localCapaTramosVias.getFeatureCollectionWrapper().getFeatures().size()>0){
			boolean bExist = false;
			if(isGraphic)
				progressDialog.report(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
			else
				logger.info(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
			
			
			
			localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee();
			List myFeaturesList = new ArrayList(localCapaTramosVias.getFeatureCollectionWrapper().getFeatures());
			
			ArrayList listFeaturesToDelete = new ArrayList();
			for (int j=0; j<myFeaturesList.size(); j++){
				try {
					bExist = false;
					GeopistaFeature deleteCurrentFeature = (GeopistaFeature) myFeaturesList.get(j);
					int iViaTemp1 = ((java.math.BigDecimal) deleteCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue();
					for (int i=0; i<listaLayerSHP.size(); i++){
						
						Feature aFeature = (Feature) listaLayerSHP.get(i);
						int iViaTemp2 = Integer.parseInt(aFeature.getString("VIA"));
						if (iViaTemp2==iViaTemp1){
							
							 Geometry geomActual=(Geometry)deleteCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("GEOMETRY"));
							 Geometry geomNew=(Geometry)aFeature.getGeometry();
							 if (geomActual.equalsExact(geomNew)){
								 //System.out.println("Geometrias iguales");
								 bExist=true;								 
								 break;
							 }
							//deleteCurrentFeature.getAttribute(featureSchema.getAttributeByColumn("numvia"))).intValue();
							//Un tramo de via ya existe para la misma via. 
							// We need to add that tramo de via in the feature list in order to be able to know the via geometry
							//bExist = true;
							//break;
						}
					}
					if (bExist){
						boolean quietly=true;
						deleteCurrentFeature.setDeleted(true);
						listFeaturesToDelete.add(deleteCurrentFeature);
						//localCapaTramosVias.getFeatureCollectionWrapper().remove(deleteCurrentFeature,quietly);
					}
				}catch (Exception ex){
					System.out.println("Exception: " + ex.getMessage());
					continue;
				}
			}
			//Borramos los elementos que han sido identificados. 
            geopistaServerDataSource.getConnection().executeUpdate(localCapaTramosVias.getDataSourceQuery().getQuery(), localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
            localCapaTramosVias.getFeatureCollectionWrapper().removeAll(listFeaturesToDelete);
		}

		firingEvents = localCapaTramosVias.getLayerManager().isFiringEvents();
		localCapaTramosVias.getLayerManager().setFiringEvents(false);

		Iterator itLayer = listaLayerSHP.iterator();
		if(isGraphic)
			progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias"));
		else
			logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias"));

		while (itLayer.hasNext()){
			if(isGraphic)
				progressDialog.report(registrosCorrectos + registrosErroneos,listaLayerSHP.size(),aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias"));
			else
				logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.ImportandoTramosVias")+" "+(registrosCorrectos+registrosErroneos+1)+"/"+listaLayerSHP.size());
				// Obtener una feature

			Feature f = (Feature) itLayer.next();
			int via = Integer.parseInt(f.getString("VIA"));
			Geometry geometria = f.getGeometry();
 
			// Con el Código de la Via debo localizar la denominación en CarVia

			String nombreCalle = localizarDenominacion(via, carVia);

			java.util.Date fechaAlta = localizarFecha(via, carVia);
			// Si la ha encontrado actualizar los valores y dejar el ID.
			GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);

			currentFeature.setGeometry(f.getGeometry());
			currentFeature.setAttribute(featureSchema.getAttributeByColumn("numvia"),new Integer(via));
			currentFeature.setAttribute(featureSchema.getAttributeByColumn("denominacion"), nombreCalle);
			currentFeature.setAttribute(featureSchema.getAttributeByColumn("FechaAlta"), new Date());
			((GeopistaFeature) currentFeature).setLayer(localCapaTramosVias);

			boolean validateResult = false;
			boolean cancelImport = false;

			SchemaValidator validator = new SchemaValidator(null);

			while (!(validateResult = validator.validateFeature(currentFeature))){
				if (!manualModification){
					break;
				}
				FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(localCapaTramosVias, currentFeature);
				if (featureDialog.wasOKPressed()){
					Feature clonefeature = featureDialog.getModifiedFeature();
					currentFeature.setAttributes(clonefeature.getAttributes());
				} else{
					Object[] possibleValues = {aplicacion.getI18nString("CancelarEsteElemento"),aplicacion.getI18nString("CancelarTodaLaImportacion"),aplicacion.getI18nString("IgnorarFuturosErrores") };
					int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
										aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"),0, JOptionPane.QUESTION_MESSAGE, null,possibleValues, possibleValues[0]);
					if (selectedValueCancel == 2)
						manualModification = false;
						if (selectedValueCancel == 1)
							cancelImport = true;
					break;
				}
			}

			if (validateResult){
				registrosCorrectos++;
			} else{
				registrosErroneos++;
			}
			if (cancelImport == true)
				break;
			/**
			 * Fin de la validación manual
			 */
			if (validateResult){
				localCapaTramosVias.getFeatureCollectionWrapper().add(currentFeature);
			}
		}

		geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
			if(isGraphic)
				progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
			else
				logger.info(aplicacion.getI18nString("GrabandoDatosBaseDatos"));

			Map driverProperties = geopistaServerDataSource.getProperties();
			Object lastResfreshValue = driverProperties.get(Constantes.REFRESH_INSERT_FEATURES);
			try{
				driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,new Boolean(true));
				geopistaServerDataSource.getConnection().executeUpdate(localCapaTramosVias.getDataSourceQuery().getQuery(),localCapaTramosVias.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
			} finally{
				try{
					geopistaValidatePlugIn.setMakeInsertion(lastInsertionState);
				} catch (Exception e){
				}
				try{
					localCapaTramosVias.getLayerManager().setFiringEvents(firingEvents);
				} catch (Exception e){
			}

			if (lastResfreshValue != null){
				driverProperties.put(Constantes.REFRESH_INSERT_FEATURES,lastResfreshValue);
			} else{
				driverProperties.remove(Constantes.REFRESH_INSERT_FEATURES);
			}
		}

		return registrosCorrectos;
	}
    
    /**
	 * Método para borrar los numeros de policia
	 */
	public static void borrarNumerosPolicia(AppContext aplicacion, Blackboard blackboard, GeopistaEditor geopistaEditor2, boolean isGraphic, TaskMonitorDialog progressDialog) throws Exception
    {
		//localCapaNumerosPolicia tiene los valores ya existentes en la BBDD (geometria iguales)
		GeopistaLayer localCapaNumerosPolicia = (GeopistaLayer) geopistaEditor2.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
		GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localCapaNumerosPolicia.getDataSourceQuery().getDataSource();
		
		GeopistaSchema featureSchema = (GeopistaSchema) localCapaNumerosPolicia.getFeatureCollectionWrapper().getFeatureSchema();
		
		//si hay numeros de policia de otras importaciones las eliminamos
		if(localCapaNumerosPolicia.getFeatureCollectionWrapper().getFeatures().size()>0){
			boolean bExist = false;
			if(isGraphic)
				progressDialog.report(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
			else
				logger.info(aplicacion.getI18nString("GeopistaImportarToponimosPanel.BorrandoRegistros"));
		
			localCapaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee();
			List myFeaturesList = new ArrayList(localCapaNumerosPolicia.getFeatureCollectionWrapper().getFeatures());
			
			ArrayList listFeaturesToDelete = new ArrayList();
			
			for (int j=0; j<myFeaturesList.size(); j++){
				try {
					GeopistaFeature deleteCurrentFeature = (GeopistaFeature) myFeaturesList.get(j);
					
					deleteCurrentFeature.setDeleted(true);
					listFeaturesToDelete.add(deleteCurrentFeature);
				
				}catch (Exception ex){
					System.out.println("Exception: " + ex.getMessage());
					continue;
				}
			}
			
			System.out.println("Borramos " + listFeaturesToDelete.size() + " numeros de policia. ");
			//Borramos los elementos que han sido identificados. 
			geopistaServerDataSource.getConnection().executeUpdate(localCapaNumerosPolicia.getDataSourceQuery().getQuery(), localCapaNumerosPolicia.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
	        localCapaNumerosPolicia.getFeatureCollectionWrapper().removeAll(listFeaturesToDelete);
		}
		// fin del proceso que borra las importaciones previas
    }
	/**
     * Método para introducir los numeros de policia a traves del editor
     * 
     */

	public static int introducirNumerosPolicia(AppContext aplicacion, Blackboard blackboard, GeopistaEditor geopistaEditor2, GeopistaLayer layer09, boolean isGraphic, TaskMonitorDialog progressDialog) throws Exception
    {
		boolean manualModification = false;
		if(isGraphic)
			manualModification = ((Boolean) blackboard.get("mostrarError")).booleanValue();
		boolean firingEvents = false;
		int registrosCorrectos = 0;
		int registrosErroneos = 0;
		GeopistaLayer localCapaNumerosPolicia = (GeopistaLayer) geopistaEditor2.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
		firingEvents = localCapaNumerosPolicia.getLayerManager().isFiringEvents();

		List listaLayer = layer09.getFeatureCollectionWrapper().getFeatures();
		Iterator itLayer = listaLayer.iterator();
		if(isGraphic)
			progressDialog.report(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
		else
			logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
		try{
		localCapaNumerosPolicia.getLayerManager().setFiringEvents(false);
		while (itLayer.hasNext()){
		    // Obtener una feature
			if(isGraphic)
				progressDialog.report(registrosCorrectos + registrosErroneos, listaLayer.size(), aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia"));
			else
				logger.info(aplicacion.getI18nString("GeopistaMostrarCallejeroPanel.NumerosPolicia")+" "+(registrosCorrectos+registrosErroneos+1)+"/"+listaLayer.size());
		    Feature f = (Feature) itLayer.next();
		    
		    
		    String TTGGSS=f.getString("TTGGSS").trim();
		    
		    //Los numeros de policia tienen identificador TTGGSS=189401 el resto no es necesario cargarlo.
		    if (!TTGGSS.equals("189401"))
		    	continue;
		    
		    Geometry geometria = f.getGeometry();
		
		    GeopistaSchema featureSchema = (GeopistaSchema) localCapaNumerosPolicia.getFeatureCollectionWrapper().getFeatureSchema();
		    GeopistaFeature currentFeature = new GeopistaFeature(featureSchema);
		
		    currentFeature.setGeometry(f.getGeometry());
		    currentFeature.setAttribute(featureSchema.getAttributeByColumn("rotulo"), f.getString("ROTULO").trim());
		    ((GeopistaFeature) currentFeature).setLayer(localCapaNumerosPolicia);
		    
		    String idMunicipioAttName=featureSchema.getAttributeByColumn("id_municipio");
            String idMunicipioSesion=((ISesion)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SESION_KEY)).getIdMunicipio();
			currentFeature.setAttribute(idMunicipioAttName,idMunicipioSesion);

		
		    boolean validateResult = false;
		    boolean cancelImport = false;
		
		    SchemaValidator validator = new SchemaValidator(null);
		
		    while (!(validateResult = validator.validateFeature(currentFeature))){
		        if (!manualModification){
		            break;
		        }
		        FeatureDialog featureDialog = GeopistaUtil.showFeatureDialog(localCapaNumerosPolicia, currentFeature);
		        if (featureDialog.wasOKPressed()){
		            Feature clonefeature = featureDialog.getModifiedFeature();
		            currentFeature.setAttributes(clonefeature.getAttributes());
		        } else{
		            Object[] possibleValues = {aplicacion.getI18nString("CancelarEsteElemento"),aplicacion.getI18nString("CancelarTodaLaImportacion"),aplicacion.getI18nString("IgnorarFuturosErrores") };
		            int selectedValueCancel = JOptionPane.showOptionDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("GeopistaImportacionLog.LaCapaContieneFeatures"),
		                            aplicacion.getI18nString("GeopistaImportacionLog.BorrarDuplicar"),0, JOptionPane.QUESTION_MESSAGE, null,possibleValues, possibleValues[0]);
		            if (selectedValueCancel == 2)
		                manualModification = false;
		            if (selectedValueCancel == 1)
		                cancelImport = true;
		            break;
		        }		
		    }
		
		    if (validateResult){
		        registrosCorrectos++;
		    } else{
		        registrosErroneos++;
		    }
		    if (cancelImport == true)
		        break;
		    /**
		     * Fin de la validación manual
		     */
		    if (validateResult){
		        localCapaNumerosPolicia.getFeatureCollectionWrapper().add(currentFeature);
		    }		
		}
		}finally{
		    localCapaNumerosPolicia.getLayerManager().setFiringEvents(firingEvents);
		}
		
		return registrosCorrectos;
    }
    
    public static void loadEditorPlugins(AppContext aplicacion, GeopistaEditor geopistaEditor,GeopistaValidatePlugin geopistaValidatePlugIn){
    	geopistaEditor.addPlugIn(geopistaValidatePlugIn);
    	geopistaEditor.addCursorTool("Zoom In/Out","com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    	geopistaEditor.addCursorTool("Pan","com.vividsolutions.jump.workbench.ui.zoom.PanTool");
    }
    
    public static GeopistaLayer loadFeatures(AppContext aplicacion, Blackboard blackboard, GeopistaEditor geopistaEditor2, GeopistaLayer capaTramosVias, GeopistaValidatePlugin geopistaValidatePlugIn, GeopistaLayer layer09, boolean isGraphic, TaskMonitorDialog progressDialog) throws Exception{
        String ruta = (String) blackboard.get("mapaCargarTramosVias");//ejes.shp
        String rutaPolicia = (String) blackboard.get("ficheroNumerosPolicia");//elemtext.shp
        String rutaCarvia = (String) blackboard.get("rutaFicheroCarVia");//carvia.dbf
        String rutaEjes = (String) blackboard.get("rutaFicheroEjes");
        ruta = rutaEjes;
        rutaEjes = rutaEjes.substring(0,rutaEjes.length() - 3)+ "dbf";//ejes.dbf
            try{
            	// Layer09 contiene elemtext (npolicia)
                // Layer08 estará el fichero de ejes.shp
            	if(isGraphic)
            		progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoTramosVia"));
            	else
            		logger.info(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoTramosVia"));
                capaTramosVias = (GeopistaLayer) geopistaEditor2.loadData(ruta,aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));
                //ahora hay Ejes en capaTramosVia
                capaTramosVias.setActiva(false);
                capaTramosVias.addStyle(new BasicStyle(new Color(100, 100, 64)));
                capaTramosVias.setVisible(true);
                // Pondremos en Layer09 el fichero numeros de policia
                if(isGraphic)
                	progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.NumerosPolicia"));
                else
                	logger.info(aplicacion.getI18nString("GeopistaMostrarTramosVias.NumerosPolicia"));
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
                // Cargamos el mapa con los datos del Mapa de Información de Ref.
                if(isGraphic)
                	progressDialog.report(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoCallejero"));
                else
                	logger.info(aplicacion.getI18nString("GeopistaMostrarTramosVias.CargandoCallejero"));

                	geopistaEditor2.loadMap(aplicacion.getString("url.mapa.callejero"));
            }catch(Exception e){
            	throw e;
            }
            return capaTramosVias;
    }
    
    public static String insertLayers(AppContext aplicacion, Blackboard blackboard, 
			Connection con, GeopistaValidatePlugin geopistaValidatePlugIn, 
			GeopistaEditor geopistaEditor2, GeopistaLayer capaTramosVias, GeopistaLayer layer09, boolean isGraphic, TaskMonitorDialog progressDialog) throws Exception{
	    int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
	    // Abrir una transaccion
	    String texto="";
	    int numeroRegistrosLeidos = 0;
	    String ruta = (String) blackboard.get(GeopistaImportarCallejeroPanel.FICHERO_VIAS_INE);
	    try{
	    	// If we already passed through this municipio, we don't need to insert another time las vias INE
	    	if (blackboard.get("viasINEInsertadas")==null){
	        	numeroRegistrosLeidos = insertarViasINE(aplicacion, ruta, con, blackboard, isGraphic, progressDialog);
		        texto=addMessage("importar.informacion.referencia.viasINE",numeroRegistrosLeidos,texto, aplicacion);
		        numeroRegistrosLeidos = insertarDatosTramosViasINE(aplicacion, blackboard, con, isGraphic, progressDialog);
		        texto=addMessage("importar.informacion.referencia.tramos.via.ine",numeroRegistrosLeidos,texto, aplicacion);
	    	}
	    	
	        String carVia = (String) blackboard.get("rutaFicheroCarVia");
	
	        numeroRegistrosLeidos = insertarTramosVias(carVia, aplicacion, blackboard, geopistaValidatePlugIn, geopistaEditor2, capaTramosVias, isGraphic, progressDialog);
	        texto=addMessage("importar.informacion.referencia.tramos.via",numeroRegistrosLeidos,texto, aplicacion);
	        
	        //insertarCarVias(carVia, geopistaEditor2, con, idMunicipio);
	        
	        numeroRegistrosLeidos = introducirNumerosPolicia(aplicacion, blackboard, geopistaEditor2, layer09, isGraphic, progressDialog);
	        texto=addMessage("importar.informacion.referencia.numeros.de.policia",numeroRegistrosLeidos,texto, aplicacion);
	        
	    }catch(Exception e){
	    	System.out.println("No se pueden importar los num policia");
	    	throw e;
	    	
	    }
    return texto;   	
}

    private static String addMessage(String message, int numeroRegistrosLeidos, String cadenaTexto, AppContext aplicacion)
    {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");

        cadenaTexto = cadenaTexto + aplicacion.getI18nString("importar.fichero.progreso")
                + aplicacion.getI18nString(message);
        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.progreso.numero.leidos")
                + numeroRegistrosLeidos;

        cadenaTexto = cadenaTexto
                + aplicacion.getI18nString("importar.progreso.fecha.fin")
                + formatter.format(new Date());
        return cadenaTexto;

    } 
    
} // de la clase general.
