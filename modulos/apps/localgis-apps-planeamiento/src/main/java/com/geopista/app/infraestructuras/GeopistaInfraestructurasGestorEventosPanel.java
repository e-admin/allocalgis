/**
 * GeopistaInfraestructurasGestorEventosPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.infraestructuras;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase permite consultar, eliminar y modificar los datos de
 * mantenimiento, averias y avisos de infraestructuras. Contiene un mapa en el
 * cual el usuario puede seleccionar sobre una de las capas del mismo para
 * consultar los datos de la infraestructura seleccionada.
 */
public class GeopistaInfraestructurasGestorEventosPanel extends JPanel implements
        WizardPanel, ListSelectionListener
{
    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private GeopistaInfraPostGreEventos gestor = new GeopistaInfraPostGreEventos();

    private JPanel pnlMantenimiento = new JPanel();

    private JScrollPane scpMapa = new JScrollPane();

    private JPanel pnlMapa = new JPanel();

    private JButton btnAnterior = new JButton();

    private JLabel lblFecha = new JLabel();

    private JPanel pnlAverias = new JPanel();

    private JScrollPane scpHistorico = new JScrollPane();

    private boolean acceso = true;

    public Connection conexion = null;

    private long IDVIA = 0;

    /* LISTA AVERIAS */
    private DefaultListModel listModelAveriasOculto = new DefaultListModel();

    private JList averiasOcultolst = new JList(listModelAveriasOculto);

    private DefaultListModel listModelAverias = new DefaultListModel();

    private JList averiaslst = new JList(listModelAverias);

    /* LISTA DE DATOS DE MANTENIMIENTO */

    private DefaultListModel listModelMantenimientoOculto = new DefaultListModel();

    private JList mantenimientoOcultolst = new JList(listModelMantenimientoOculto);

    private DefaultListModel listModelMantenimiento = new DefaultListModel();

    private JList mantenimientolst = new JList(listModelMantenimiento);

    private JLabel lblFechaNotificacion = new JLabel();

    private JTextField fechanotificaciontxt = new JTextField();

    private JLabel lblObservaciones = new JLabel();

    private JScrollPane scpObservaciones = new JScrollPane();

    private JTextArea txaObservaciones = new JTextArea();

    private DefaultListModel listModelAvisosOculto = new DefaultListModel();

    private JList avisosOcultolst = new JList(listModelAvisosOculto);

    private DefaultListModel listModelAvisos = new DefaultListModel();

    private JList avisoslst = new JList(listModelAvisos);

    private GeopistaEditor geopistaEditor1 = null;

    private int entidad = 0;

    private int id_elemento = 0;

    private String id_elemento_St;

    private String nombreCapa;

    private ActionMap actionMap1 = new ActionMap();

    private String dominio;

    private JOptionPane OpCuadroDialogo;

    private JScrollPane scpMantenimiento = new JScrollPane();

    private JTextField fechaMantenimientotxt = new JTextField();

    private JLabel actuacionlbl = new JLabel();

    private JComboBox cmbTipoActuacion = new JComboBox();

    private JLabel lblObservacionesMant = new JLabel();

    private JScrollPane scpObservacionesMantenimiento = new JScrollPane();

    private JTextArea txaObservacionesMantenimiento = new JTextArea();

    private JTextField fechareparaciontxt = new JTextField();

    private JLabel lblFechaReparacion = new JLabel();

    private JLabel lbltipoaveria = new JLabel();

    private JComboBox cmbtipoaveria = new JComboBox();

    private JLabel lblgravedad = new JLabel();

    private JTextField gravedadtxt = new JTextField();

    private JLabel lblsolucion = new JLabel();

    private JComboBox cmbsolucion = new JComboBox();

    private JLabel lbldireccion = new JLabel();

    private JPanel pnlAvisos = new JPanel();

    private JScrollPane scpAvisos = new JScrollPane();

    private JTextField fechaAvisotxt = new JTextField();

    private JLabel lblFechaAviso = new JLabel();

    private JLabel lblFechaAviso1 = new JLabel();

    private JButton btnEliminarMantenimiento = new JButton();

    private JButton btnModificarMantenimiento = new JButton();

    private JButton btnNuevoMantenimiento = new JButton();

    private JButton btnEliminarAveria = new JButton();

    private JButton btnModificarAveria = new JButton();

    private JButton btnNuevoAveria = new JButton();

    private JButton btnEliminarAviso = new JButton();

    private JButton btnModificarAviso = new JButton();

    private JButton btnNuevoAviso = new JButton();

    private JScrollPane scpMotivo = new JScrollPane();

    private JTextArea txaMotivo = new JTextArea();

    private JCheckBox activarAvisochk = new JCheckBox();

    private JComboBox cmbTipoActuacionOculto = new JComboBox();

    private JComboBox cmbTipoAveriaOculto = new JComboBox();

    private JComboBox cmbsolucionOculto = new JComboBox();

    private String nextID = null;

    private JComboBox cmbLocalizacion = new JComboBox();

    private JComboBox jcomboHide = new JComboBox();

    private WizardContext wizardContext;

    private GeopistaInfraestructurasGestorEventosPanel geopistaInfraestructurasGestorEventosPanel = null;

    public GeopistaInfraestructurasGestorEventosPanel()
        {
            geopistaInfraestructurasGestorEventosPanel = this;
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

                                    try
                                    {
                                        String helpHS = "ayuda.hs";
                                        HelpSet hs = com.geopista.app.help.HelpLoader
                                                .getHelpSet(helpHS);
                                        HelpBroker hb = hs.createHelpBroker();
                                        hb
                                                .enableHelpKey(
                                                        geopistaInfraestructurasGestorEventosPanel,
                                                        "infraestructurasGestionEventos01.htm",
                                                        hs);
                                    } catch (Exception excp)
                                    {
                                    }

                                    setLayout(null);

                                    pnlMantenimiento.setBounds(new Rectangle(5, 10, 395,
                                            165));
                                    pnlMantenimiento
                                            .setBorder(BorderFactory
                                                    .createTitledBorder(aplicacion.getI18nString("gestoreventos.datosmantenimiento")));
                                    pnlMantenimiento.setLayout(null);

                                    pnlMapa.setBounds(new Rectangle(410, 18, 380, 510));
                                    pnlMapa.setBorder(BorderFactory
                                            .createTitledBorder(""));
                                    pnlMapa.setLayout(null);

                                    averiasOcultolst.setVisible(false);
                                    jcomboHide.setVisible(false);
                                    lblFecha.setText(aplicacion.getI18nString("Fecha")
                                            + ":");
                                    lblFecha.setBounds(new Rectangle(105, 20, 40, 20));

                                    pnlAverias.setBounds(new Rectangle(5, 180, 395, 220));
                                    pnlAverias.setBorder(BorderFactory
                                            .createTitledBorder(aplicacion.getI18nString("gestoreventos.datosaverias")));
                                    pnlAverias.setLayout(null);

                                    scpHistorico.setBounds(new Rectangle(10, 20, 90, 95));

                                    lblFechaNotificacion.setText(aplicacion
                                            .getI18nString("FechaNotificacion")
                                            + ":");
                                    lblFechaNotificacion.setBounds(new Rectangle(105, 20,
                                            135, 20));

                                    fechanotificaciontxt.setBounds(new Rectangle(210, 20,
                                            85, 20));

                                    lblObservaciones.setText(aplicacion
                                            .getI18nString("Observaciones")
                                            + ":");
                                    lblObservaciones.setBounds(new Rectangle(15, 145, 90,
                                            20));

                                    scpObservaciones.setBounds(new Rectangle(15, 165,
                                            370, 40));
                                    txaObservaciones.setLineWrap(true);

                                    geopistaEditor1 = new GeopistaEditor(aplicacion
                                            .getString("url.herramientas.gestoreventos"));
                                    geopistaEditor1.setBounds(new Rectangle(5, 5, 360,
                                            495));
                                    geopistaEditor1.setBorder(BorderFactory
                                            .createEmptyBorder(0, 0, 0, 0));
                                    scpMantenimiento.setBounds(new Rectangle(10, 20, 90,
                                            125));
                                    fechaMantenimientotxt.setBounds(new Rectangle(150,
                                            20, 85, 20));
                                    actuacionlbl.setText(aplicacion
                                            .getI18nString("TipoActuacion")
                                            + ":");
                                    actuacionlbl
                                            .setBounds(new Rectangle(105, 45, 155, 20));

                                    cmbTipoActuacion.setBounds(new Rectangle(105, 65,
                                            180, 20));
                                    lblObservacionesMant.setText(aplicacion
                                            .getI18nString("Observaciones")
                                            + ":");
                                    lblObservacionesMant.setBounds(new Rectangle(105, 90,
                                            90, 20));
                                    scpObservacionesMantenimiento
                                            .setBounds(new Rectangle(105, 110, 285, 35));
                                    fechareparaciontxt.setBounds(new Rectangle(210, 45,
                                            85, 20));
                                    lblFechaReparacion.setText(aplicacion
                                            .getI18nString("FechaReparacion")
                                            + ":");
                                    lblFechaReparacion.setBounds(new Rectangle(105, 45,
                                            135, 20));
                                    lbltipoaveria.setText(aplicacion
                                            .getI18nString("TipoAveria")
                                            + ":");
                                    lbltipoaveria
                                            .setBounds(new Rectangle(105, 70, 75, 15));

                                    cmbtipoaveria.setBounds(new Rectangle(195, 70, 100,
                                            20));
                                    lblgravedad.setText(aplicacion
                                            .getI18nString("Gravedad")
                                            + ":");
                                    lblgravedad.setBounds(new Rectangle(105, 95, 60, 15));
                                    gravedadtxt.setBounds(new Rectangle(165, 95, 35, 20));
                                    lblsolucion.setText(aplicacion
                                            .getI18nString("Solucion")
                                            + ":");
                                    lblsolucion.setBounds(new Rectangle(205, 95, 60, 15));
                                    cmbsolucion
                                            .setBounds(new Rectangle(265, 95, 120, 20));
                                    lbldireccion.setText(aplicacion
                                            .getI18nString("Direccion")
                                            + ":");
                                    lbldireccion
                                            .setBounds(new Rectangle(15, 120, 60, 15));
                                    pnlAvisos.setBounds(new Rectangle(5, 405, 395, 125));
                                    pnlAvisos.setBorder(BorderFactory
                                            .createTitledBorder(aplicacion.getI18nString("gestoreventos.datosavisos")));
                                    pnlAvisos.setLayout(null);
                                    lblFechaAviso.setText(aplicacion
                                            .getI18nString("FechaAviso")
                                            + ":");
                                    lblFechaAviso1.setText(aplicacion
                                            .getI18nString("Motivo")
                                            + ":");
                                    btnEliminarMantenimiento.setText(aplicacion
                                            .getI18nString("Delete"));
                                    btnEliminarMantenimiento.setBounds(new Rectangle(300,
                                            70, 85, 20));
                                    btnModificarMantenimiento.setText(aplicacion
                                            .getI18nString("Modificar"));
                                    btnModificarMantenimiento.setBounds(new Rectangle(
                                            300, 45, 85, 20));

                                    btnNuevoMantenimiento.setText(aplicacion
                                            .getI18nString("Nuevo"));
                                    btnNuevoMantenimiento.setBounds(new Rectangle(300,
                                            20, 85, 20));

                                    btnEliminarAveria.setText(aplicacion
                                            .getI18nString("Delete"));
                                    btnEliminarAveria.setBounds(new Rectangle(300, 70,
                                            85, 20));
                                    btnModificarAveria.setText(aplicacion
                                            .getI18nString("Modificar"));
                                    btnModificarAveria.setBounds(new Rectangle(300, 45,
                                            85, 20));

                                    btnNuevoAveria.setText(aplicacion
                                            .getI18nString("Nuevo"));
                                    btnNuevoAveria.setBounds(new Rectangle(300, 20, 85,
                                            20));

                                    btnEliminarAviso.setText(aplicacion
                                            .getI18nString("Delete"));
                                    btnModificarAviso.setText(aplicacion
                                            .getI18nString("Modificar"));

                                    activarAvisochk.setText(aplicacion
                                            .getI18nString("ActivarAviso"));

                                    pnlAverias.add(jcomboHide, null);
                                    pnlAverias.add(cmbLocalizacion, null);
                                    pnlAverias.add(btnNuevoAveria, null);
                                    pnlAverias.add(btnModificarAveria, null);
                                    pnlAverias.add(btnEliminarAveria, null);
                                    pnlAverias.add(lbldireccion, null);
                                    pnlAverias.add(cmbsolucion, null);
                                    pnlAverias.add(lblsolucion, null);
                                    pnlAverias.add(gravedadtxt, null);
                                    pnlAverias.add(lblgravedad, null);
                                    pnlAverias.add(cmbtipoaveria, null);
                                    pnlAverias.add(lbltipoaveria, null);
                                    pnlAverias.add(lblFechaReparacion, null);
                                    pnlAverias.add(fechareparaciontxt, null);
                                    scpObservaciones.getViewport().add(txaObservaciones,
                                            null);
                                    pnlAverias.add(scpObservaciones, null);
                                    pnlAverias.add(lblObservaciones, null);
                                    pnlAverias.add(fechanotificaciontxt, null);
                                    pnlAverias.add(lblFechaNotificacion, null);
                                    pnlAverias.add(scpHistorico, null);

                                    scpHistorico.getViewport()
                                            .add(averiasOcultolst, null);
                                    scpHistorico.getViewport().add(averiaslst, null);

                                    averiaslst
                                            .addListSelectionListener(geopistaInfraestructurasGestorEventosPanel);

                                    pnlAvisos.add(activarAvisochk, null);
                                    scpMotivo.getViewport().add(txaMotivo, null);
                                    pnlAvisos.add(scpMotivo, null);
                                    pnlAvisos.add(btnNuevoAviso, null);
                                    pnlAvisos.add(btnModificarAviso, null);
                                    pnlAvisos.add(btnEliminarAviso, null);
                                    pnlAvisos.add(lblFechaAviso, null);
                                    pnlAvisos.add(fechaAvisotxt, null);
                                    scpAvisos.getViewport().add(avisoslst, null);
                                    pnlAvisos.add(scpAvisos, null);
                                    pnlAvisos.add(lblFechaAviso1, null);
                                    avisoslst
                                            .addListSelectionListener(geopistaInfraestructurasGestorEventosPanel);
                                    add(cmbsolucionOculto, null);
                                    add(cmbTipoAveriaOculto, null);
                                    add(pnlAvisos, null);
                                    add(pnlAverias, null);
                                    pnlMapa.add(geopistaEditor1, null);
                                    add(pnlMapa, null);
                                    add(pnlMantenimiento, null);
                                    add(cmbTipoActuacionOculto, null);
                                    cmbTipoActuacionOculto.setVisible(false);
                                    cmbTipoAveriaOculto.setVisible(false);
                                    cmbsolucionOculto.setVisible(false);
                                    scpMantenimiento.getViewport().add(mantenimientolst,
                                            null);
                                    mantenimientolst
                                            .addListSelectionListener(geopistaInfraestructurasGestorEventosPanel);
                                    pnlMantenimiento.add(btnNuevoMantenimiento, null);
                                    pnlMantenimiento.add(btnModificarMantenimiento, null);
                                    pnlMantenimiento.add(btnEliminarMantenimiento, null);
                                    scpObservacionesMantenimiento.getViewport().add(
                                            txaObservacionesMantenimiento, null);
                                    pnlMantenimiento.add(scpObservacionesMantenimiento,
                                            null);
                                    pnlMantenimiento.add(lblObservacionesMant, null);
                                    pnlMantenimiento.add(cmbTipoActuacion, null);
                                    pnlMantenimiento.add(actuacionlbl, null);
                                    pnlMantenimiento.add(fechaMantenimientotxt, null);
                                    pnlMantenimiento.add(scpMantenimiento, null);
                                    pnlMantenimiento.add(lblFecha, null);
                                    pnlMantenimiento.add(scpMapa, null);

                                    jcomboHide
                                            .setBounds(new Rectangle(255, 150, 125, 15));
                                    cmbLocalizacion.setBounds(new Rectangle(95, 125, 290,
                                            20));
                                    lblFechaAviso1.setBounds(new Rectangle(100, 35, 190,
                                            20));

                                    lblFechaAviso
                                            .setBounds(new Rectangle(100, 10, 95, 30));
                                    fechaAvisotxt
                                            .setBounds(new Rectangle(200, 20, 85, 20));
                                    btnNuevoAviso
                                            .setBounds(new Rectangle(300, 20, 85, 20));
                                    btnModificarAviso.setBounds(new Rectangle(300, 45,
                                            85, 20));
                                    scpAvisos.setBounds(new Rectangle(10, 20, 85, 90));
                                    scpMotivo.setBounds(new Rectangle(100, 55, 195, 35));
                                    btnEliminarAviso.setBounds(new Rectangle(300, 70, 85,
                                            20));
                                    activarAvisochk.setBounds(new Rectangle(100, 95, 190,
                                            15));

                                    cmbsolucionOculto.setBounds(new Rectangle(240, 545,
                                            105, 20));
                                    cmbTipoAveriaOculto.setBounds(new Rectangle(140, 545,
                                            95, 20));
                                    cmbTipoActuacionOculto.setBounds(new Rectangle(25,
                                            545, 100, 20));

                                    setSize(new Dimension(501, 591));
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
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), geopistaEditor1.getContext().getErrorHandler());

        progressDialog
                .setTitle(aplicacion
                        .getI18nString("GeopistaImportarInfraPanel02.CargandoMapaInfraestructuras"));

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
                                progressDialog
                                        .report(aplicacion
                                                .getI18nString("GeopistaImportarInfraPanel02.CargandoMapaInfraestructuras"));

                                try
                                {
                                    GeopistaPermission geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Nuevo.Historico");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnNuevoMantenimiento.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Nuevo.Averia");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnNuevoAveria.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Nuevo.Aviso");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnNuevoAviso.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Modificar.Averia");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnModificarAveria.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Modificar.Historico");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnModificarMantenimiento.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Modificar.Aviso");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnModificarAviso.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Eliminar.Historico");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnEliminarMantenimiento.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Eliminar.Averia");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnEliminarAveria.setEnabled(acceso);

                                    geopistaPerm = new GeopistaPermission(
                                            "Geopista.Infraestructuras.Eliminar.Aviso");
                                    acceso = aplicacion.checkPermission(geopistaPerm,
                                            "Infraestructuras");

                                    btnEliminarAviso.setEnabled(acceso);

                                    
                                    geopistaEditor1
                                            .addCursorTool("Zoom In/Out",
                                                    "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
                                    geopistaEditor1
                                            .addCursorTool("Pan",
                                                    "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
                                    geopistaEditor1
                                            .addCursorTool("Measure",
                                                    "com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool");

                                    geopistaEditor1
                                            .addCursorTool("Select tool",
                                                    "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");
                                    geopistaEditor1.showLayerName(true);
                                    
                                    geopistaEditor1
                                            .addGeopistaListener(new GeopistaListener()
                                                {

                                                    public void selectionChanged(
                                                            final IAbstractSelection abtractSelection)
                                                    {
                                                        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
                                                                aplicacion.getMainFrame(),
                                                                geopistaEditor1
                                                                        .getContext()
                                                                        .getErrorHandler());

                                                        progressDialog
                                                                .setTitle(aplicacion
                                                                        .getI18nString("GeopistaGestorEventosPanel.CargandoEventosAvisos"));

                                                        progressDialog
                                                                .addComponentListener(new ComponentAdapter()
                                                                    {
                                                                        public void componentShown(
                                                                                ComponentEvent e)
                                                                        {

                                                                            new Thread(
                                                                                    new Runnable()
                                                                                        {
                                                                                            public void run()
                                                                                            {
                                                                                                progressDialog
                                                                                                        .report(aplicacion
                                                                                                                .getI18nString("GeopistaGestorEventosPanel.CargandoEventosAvisos"));

                                                                                                try
                                                                                                {

                                                                                                    ArrayList layersCollection = (ArrayList) abtractSelection
                                                                                                            .getLayersWithSelectedItems();
                                                                                                    Iterator layersCollectionIter = layersCollection
                                                                                                            .iterator();
                                                                                                    if (!layersCollectionIter
                                                                                                            .hasNext())
                                                                                                        return;

                                                                                                    Object nombreCapaObject = layersCollectionIter
                                                                                                            .next();
                                                                                                    if (nombreCapaObject instanceof GeopistaLayer)
                                                                                                    {
                                                                                                        GeopistaLayer currentLayer = ((GeopistaLayer) nombreCapaObject);
                                                                                                        String nombreCapa = currentLayer
                                                                                                                .getSystemId();
                                                                                                        if (nombreCapa
                                                                                                                .equals("piezas"))
                                                                                                        {
                                                                                                            entidad = 1;

                                                                                                        }

                                                                                                        if (nombreCapa
                                                                                                                .equals("captaciones"))
                                                                                                        {
                                                                                                            entidad = 2;

                                                                                                        }

                                                                                                        if (nombreCapa
                                                                                                                .equals("depositos"))
                                                                                                        {
                                                                                                            entidad = 3;

                                                                                                        }

                                                                                                        if (nombreCapa
                                                                                                                .equals("potabilizadoras"))
                                                                                                        {
                                                                                                            entidad = 4;

                                                                                                        }

                                                                                                        if (nombreCapa
                                                                                                                .equals("tramosabastecimiento"))
                                                                                                        {
                                                                                                            entidad = 5;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("conducciones"))
                                                                                                        {
                                                                                                            entidad = 6;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("elementossaneamiento"))
                                                                                                        {
                                                                                                            entidad = 9;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("depuradoras"))
                                                                                                        {
                                                                                                            entidad = 10;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("tramossaneamiento"))
                                                                                                        {
                                                                                                            entidad = 11;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("emisarios"))
                                                                                                        {
                                                                                                            entidad = 12;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("colectores"))
                                                                                                        {
                                                                                                            entidad = 13;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("saneamientoautonomo"))
                                                                                                        {
                                                                                                            entidad = 14;

                                                                                                        }
                                                                                                        if (nombreCapa
                                                                                                                .equals("vias"))
                                                                                                        {
                                                                                                            entidad = 0;

                                                                                                        }
                                                                                                    }
                                                                                                    if (entidad != 0)
                                                                                                    {
                                                                                                        ArrayList featuresCollectionB = (ArrayList) abtractSelection
                                                                                                                .getFeaturesWithSelectedItems();
                                                                                                        Iterator featuresCollectionIterB = featuresCollectionB
                                                                                                                .iterator();
                                                                                                        if (!featuresCollectionIterB
                                                                                                                .hasNext())
                                                                                                            return;
                                                                                                        Feature actualFeature = (Feature) featuresCollectionIterB
                                                                                                                .next();

                                                                                                        if (actualFeature
                                                                                                                .getSchema() instanceof GeopistaSchema)
                                                                                                        {
                                                                                                            GeopistaSchema geopistaSchema = (GeopistaSchema) actualFeature
                                                                                                                    .getSchema();

                                                                                                            id_elemento_St = actualFeature
                                                                                                                    .getString(geopistaSchema
                                                                                                                            .getAttributeByColumn("id"));
                                                                                                        }
                                                                                                        try
                                                                                                        {
                                                                                                            id_elemento = Integer
                                                                                                                    .parseInt(id_elemento_St);
                                                                                                        } catch (Exception eG)
                                                                                                        {
                                                                                                            id_elemento = 0;
                                                                                                        }

                                                                                                        RefrescarDatos(entidad,id_elemento);
                                                                                                    } else
                                                                                                    {
                                                                                                        id_elemento = 0;
                                                                                                    }
                                                                                                } catch (Exception exc)
                                                                                                {
                                                                                                    exc
                                                                                                            .printStackTrace();
                                                                                                    JOptionPane
                                                                                                            .showMessageDialog(
                                                                                                                    aplicacion
                                                                                                                            .getMainFrame(),
                                                                                                                    aplicacion
                                                                                                                            .getI18nString("SeHanProducidoErroresGestor"));

                                                                                                } finally
                                                                                                {

                                                                                                    progressDialog
                                                                                                            .setVisible(false);
                                                                                                    aplicacion
                                                                                                            .closeConnection(
                                                                                                                    conexion,
                                                                                                                    null,
                                                                                                                    null,
                                                                                                                    null);
                                                                                                }
                                                                                            }
                                                                                        })
                                                                                    .start();
                                                                        }
                                                                    });
                                                        GUIUtil
                                                                .centreOnWindow(progressDialog);
                                                        progressDialog.setVisible(true);

                                                    }

                                                    public void featureAdded(
                                                            FeatureEvent e)
                                                    {
                                                    }

                                                    public void featureRemoved(
                                                            FeatureEvent e)
                                                    {

                                                    }

                                                    public void featureModified(
                                                            FeatureEvent e)
                                                    {
                                                    }

                                                    public void featureActioned(
                                                            IAbstractSelection abtractSelection)
                                                    {
                                                    }

                                                });

                                    try
                                    {
                                        geopistaEditor1
                                                .loadMap(aplicacion
                                                        .getString("url.mapa.infraestructuras.mapa"));
                                    } catch (Exception e)
                                    {
                                        // Informar al usuario del error y
                                        // salirse
                                    }
                                    List layerList = geopistaEditor1.getLayerManager()
                                            .getLayers();
                                    Iterator layerListIter = layerList.iterator();
                                    while (layerListIter.hasNext())
                                    {
                                        Object currentLayerObject = layerListIter.next();
                                        if (currentLayerObject instanceof GeopistaLayer)
                                        {
                                            ((GeopistaLayer) currentLayerObject)
                                                    .setActiva(true);
                                        }
                                    }

                                    leerVias();
                                    leerDominios(0, 58);
                                    leerDominios(2, 57);
                                    leerDominios(1, 56);
                                    btnEliminarMantenimiento
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnEliminarMantenimiento_actionPerformed(e);
                                                    }
                                                });
                                    btnEliminarAveria
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnEliminarAveria_actionPerformed(e);
                                                    }
                                                });
                                    btnEliminarAviso
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnEliminarAviso_actionPerformed(e);
                                                    }
                                                });
                                    cmbLocalizacion
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        cmbLocalizacion_actionPerformed(e);
                                                    }
                                                });
                                    cmbsolucion.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                cmbsolucion_actionPerformed(e);
                                            }
                                        });
                                    cmbtipoaveria.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                cmbtipoaveria_actionPerformed(e);
                                            }
                                        });
                                    cmbTipoActuacion
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        cmbTipoActuacion_actionPerformed(e);
                                                    }
                                                });
                                    btnModificarAviso
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnModificarAviso_actionPerformed(e);
                                                    }
                                                });
                                    btnNuevoAviso.setText(aplicacion
                                            .getI18nString("Nuevo"));
                                    btnNuevoAviso.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnNuevoAviso_actionPerformed(e);
                                            }
                                        });
                                    btnNuevoAveria.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnNuevoAveria_actionPerformed(e);
                                            }
                                        });
                                    btnNuevoMantenimiento
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnNuevoMantenimiento_actionPerformed(e);
                                                    }
                                                });
                                    btnModificarMantenimiento
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnModificarMantenimiento_actionPerformed(e);
                                                    }
                                                });
                                    btnModificarAveria
                                            .addActionListener(new ActionListener()
                                                {
                                                    public void actionPerformed(
                                                            ActionEvent e)
                                                    {
                                                        btnModificarAveria_actionPerformed(e);
                                                    }
                                                });
                                } catch (Exception exc)
                                {
                                    exc.printStackTrace();
                                    JOptionPane
                                            .showMessageDialog(
                                                    aplicacion.getMainFrame(),
                                                    aplicacion
                                                            .getI18nString("SeHanProducidoErroresGestor"));
                                    wizardContext.cancelWizard();
                                    return;
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
        return "";
    }

    public String getID()
    {
        return "1";
    }

    public String getInstructions()
    {
        return "";
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

    public void valueChanged(ListSelectionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            if (e.getValueIsAdjusting() == false)
            {

                if (mantenimientolst.getSelectedIndex() != -1)
                {
                    //limpiar casillas de texto
                    fechaMantenimientotxt.setText("");
                    txaObservacionesMantenimiento.setText("");

                    String fechaMantenimiento = mantenimientolst.getSelectedValue()
                            .toString();
                    int valorIdFechaMantenimiento = mantenimientolst.getSelectedIndex();
                    mantenimientoOcultolst.setSelectedIndex(valorIdFechaMantenimiento);

                    int idMantenimiento = ((Integer) mantenimientoOcultolst
                            .getSelectedValue()).intValue();
                    fechaMantenimientotxt.setText(fechaMantenimiento);
                    String tipoActuacion = gestor.getTipoActuacion(idMantenimiento,
                            conexion);

                    cmbTipoActuacionOculto.setSelectedItem(tipoActuacion);
                    int indice = cmbTipoActuacionOculto.getSelectedIndex();
                    cmbTipoActuacion.setSelectedIndex(indice);

                    String observacionesMantenimiento = gestor
                            .getObservacionesMantenimiento(idMantenimiento, conexion);
                    txaObservacionesMantenimiento.setText(observacionesMantenimiento);

                }
                if (averiaslst.getSelectedIndex() != -1)
                {
                    //limpiar casillas de texto
                    fechanotificaciontxt.setText("");
                    fechareparaciontxt.setText("");
                    gravedadtxt.setText("");
                    txaObservaciones.setText("");
                    cmbtipoaveria.setSelectedIndex(-1);
                    
                    String fechaAveria = averiaslst.getSelectedValue().toString();
                    int valorIndexFechaAveria = averiaslst.getSelectedIndex();
                    averiasOcultolst.setSelectedIndex(valorIndexFechaAveria);
                    fechanotificaciontxt.setText(fechaAveria);
                    int idAveria = ((Integer) averiasOcultolst.getSelectedValue())
                            .intValue();
                    String fechaReparacion = gestor
                            .getFechaReparacion(idAveria, conexion);
                    fechaReparacion = showDateSP(fechaReparacion);
                    fechareparaciontxt.setText(fechaReparacion);
                    String observacionesAverias = gestor.getObservacionesAveria(idAveria,
                            conexion);
                    txaObservaciones.setText(observacionesAverias);
                    String tipoAveria = gestor.getTipoAveria(idAveria, conexion);

                    cmbTipoAveriaOculto.setSelectedItem(tipoAveria);
                    int indice = cmbTipoAveriaOculto.getSelectedIndex();
                    cmbtipoaveria.setSelectedIndex(indice);
                    String gravedad = gestor.getGravedad(idAveria, conexion);
                    gravedadtxt.setText(gravedad);
                    long idLoc = gestor.getIdLoc(idAveria, conexion);
                    jcomboHide.setSelectedItem(String.valueOf(idLoc));
                    cmbLocalizacion.setSelectedIndex(jcomboHide.getSelectedIndex());
                    String tipoSolucion = gestor.getTipoSolucion(idAveria, conexion);
                    cmbsolucionOculto.setSelectedItem(tipoSolucion);
                    indice = cmbsolucionOculto.getSelectedIndex();
                    cmbsolucion.setSelectedIndex(indice);
                }

                if (avisoslst.getSelectedIndex() != -1)
                {
                    //limpiar casillas de texto
                    fechaAvisotxt.setText("");
                    txaMotivo.setText("");
                    
                    
                    String fechaAviso = avisoslst.getSelectedValue().toString();
                    int valorIndexFechaAviso = avisoslst.getSelectedIndex();
                    avisosOcultolst.setSelectedIndex(valorIndexFechaAviso);
                    fechaAvisotxt.setText(fechaAviso);
                    int idAviso = ((Integer) avisosOcultolst.getSelectedValue())
                            .intValue();
                    int aviso = gestor.getActivo(idAviso, conexion);
                    if (aviso == 1)
                    {
                        activarAvisochk.setSelected(true);
                    } else
                    {
                        activarAvisochk.setSelected(false);
                    }

                    String motivo = gestor.getMotivoAviso(idAviso, conexion);
                    txaMotivo.setText(motivo);
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }

    }

    private void btnModificarAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Modificar.Aviso");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if (acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (avisosOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgAvisoMod"));
                } else
                {

                    if (fechaAvisotxt.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, aplicacion
                                .getI18nString("MsgFechaAviso"));
                    } else
                    {
                        int seleccionIndice = avisosOcultolst.getSelectedIndex();
                        String identificador = avisosOcultolst.getSelectedValue()
                                .toString();
                        int idAviso = 0;
                        try
                        {
                            idAviso = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idAviso = 0;
                        }
                        String fechaAvisoAl = fechaAvisotxt.getText().toString();
                        fechaAvisoAl = showDatePG(fechaAvisoAl);
                        String motivoAl = txaMotivo.getText().toString();
                        int activoAl = 0;
                        if (activarAvisochk.isSelected())
                        {
                            activoAl = 1;
                        } else
                        {
                            activoAl = 0;
                        }
                        if (!fechaAvisoAl.equals("0"))
                        {
                            if (idAviso != 0)
                            {
                                gestor.modificarAviso(idAviso, entidad, id_elemento,
                                        fechaAvisoAl, motivoAl, activoAl, conexion);
                                RefrescarDatos(entidad,id_elemento);
                                avisosOcultolst.setSelectedIndex(seleccionIndice);
                                avisoslst.setSelectedIndex(seleccionIndice);
                            }
                        }
                    }
                }
            }
            // }
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }

    }

    private void btnNuevoMantenimiento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Nuevo.Historico");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if (acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (fechaMantenimientotxt.getText().toString().equals(""))
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgFecha"));
                } else
                {

                    int idMaxMantenimiento = gestor.getMaxIdMantenimiento(conexion);
                    String fechaMantenimientoAl = fechaMantenimientotxt.getText()
                            .toString();
                    fechaMantenimientoAl = showDatePG(fechaMantenimientoAl);
                    int tipoActuacionAl = Integer.parseInt(cmbTipoActuacionOculto
                            .getSelectedItem().toString());
                    String observacionesMantenimientoAl = txaObservacionesMantenimiento
                            .getText().toString();
                    if (!fechaMantenimientoAl.equals("0"))
                    {
                        if ((int) idMaxMantenimiento != 0)
                        {
                            gestor.insertarMantenimiento(idMaxMantenimiento, entidad,
                                    id_elemento, tipoActuacionAl, fechaMantenimientoAl,
                                    observacionesMantenimientoAl, conexion);
                            RefrescarDatos(entidad,id_elemento);
                        }
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnNuevoAveria_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Nuevo.Averia");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if(acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (fechanotificaciontxt.getText().toString().equals(""))
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgFechaAveria"));
                } else
                {

                    int idMaxAveria = gestor.getMaxIdAveria(conexion);
                    String fechaAveriaAl = fechanotificaciontxt.getText().toString();
                    fechaAveriaAl = showDatePG(fechaAveriaAl);
                    String fechaReparacionAl = fechareparaciontxt.getText().toString();
                    if (!fechaReparacionAl.equals(""))
                    {
                        fechaReparacionAl = showDatePG(fechaReparacionAl);
                    }
                    int tipoAveriaAl = Integer.parseInt(cmbTipoAveriaOculto
                            .getSelectedItem().toString());

                    String gravedadSt = gravedadtxt.getText();
                    String observacionesAl = txaObservaciones.getText().toString();
                    int gravedad = 0;
                    try
                    {
                        gravedad = Integer.parseInt(gravedadSt);
                    } catch (Exception eG)
                    {
                        gravedad = 0;
                    }
                    int tipoSolucionAl = 0;

                    tipoSolucionAl = Integer.parseInt(cmbsolucionOculto.getSelectedItem()
                            .toString());

                    int localizacionAl = 0;
                    if (jcomboHide.getSelectedIndex() != -1)
                    {
                        localizacionAl = Integer.parseInt(jcomboHide.getSelectedItem()
                                .toString());
                    } else
                    {
                        localizacionAl = 0;
                    }
                    if (!fechaAveriaAl.equals("0"))
                    {
                        if ((int) idMaxAveria != 0)
                        {
                            gestor.insertarAveria(idMaxAveria, entidad, id_elemento,
                                    gravedad, tipoAveriaAl, fechaAveriaAl,
                                    localizacionAl, tipoSolucionAl, fechaReparacionAl,
                                    observacionesAl, conexion);
                            RefrescarDatos(entidad,id_elemento);
                        }
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnNuevoAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Nuevo.Aviso");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if (acceso) {
             */

            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (fechaAvisotxt.getText().toString().equals(""))
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgFechaAviso"));
                } else
                {

                    int idMaxAviso = gestor.getMaxIdAviso(conexion);
                    String fechaAvisoAl = fechaAvisotxt.getText().toString();
                    fechaAvisoAl = showDatePG(fechaAvisoAl);
                    String motivoAvisoAl = txaMotivo.getText().toString();
                    int activoAl = 0;
                    if (activarAvisochk.isSelected())
                    {
                        activoAl = 1;
                    } else
                    {
                        activoAl = 0;
                    }
                    if (!fechaAvisoAl.equals("0"))
                    {
                        if ((int) idMaxAviso != 0)
                        {
                            gestor.insertarAviso(idMaxAviso, entidad, id_elemento,
                                    fechaAvisoAl, motivoAvisoAl, activoAl, conexion);
                            RefrescarDatos(entidad,id_elemento);
                        }
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnModificarAveria_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Modificar.Averia");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if(acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (averiasOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgAveriaMod"));
                } else
                {
                    if (fechanotificaciontxt.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, aplicacion
                                .getI18nString("MsgFechaNotif"));
                    } else
                    {
                        int seleccionIndice = averiasOcultolst.getSelectedIndex();
                        String identificador = averiasOcultolst.getSelectedValue()
                                .toString();
                        int idAveria = 0;
                        try
                        {
                            idAveria = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idAveria = 0;
                        }
                        String observacionesAl = txaObservaciones.getText().toString();
                        String fechaNotificacionAl = fechanotificaciontxt.getText()
                                .toString();
                        fechaNotificacionAl = showDatePG(fechaNotificacionAl);
                        String fechaReparacionAl = fechareparaciontxt.getText()
                                .toString();
                        fechaReparacionAl = showDatePG(fechaReparacionAl);
                        int tipoAveriaAl = Integer.parseInt(cmbTipoAveriaOculto
                                .getSelectedItem().toString());
                        /*
                         * int localizacionAl =
                         * Integer.parseInt(jcomboHide.getSelectedItem().toString());
                         */
                        int tipoSolucionAl = 0;

                        // tipoSolucionAl =
                        // Integer.parseInt(cmbsolucionOculto.getSelectedItem().toString());

                        int localizacionAl = 0;

                        if (jcomboHide.getSelectedIndex() != -1)
                        {
                            localizacionAl = Integer.parseInt(jcomboHide
                                    .getSelectedItem().toString());
                        } else
                        {
                            localizacionAl = 0;
                        }
                        String gravedadSt = gravedadtxt.getText();
                        int gravedadAl = 0;
                        try
                        {
                            gravedadAl = Integer.parseInt(gravedadSt);
                        } catch (Exception eG)
                        {
                            gravedadAl = 0;
                        }

                        // int tipoSolucionAl = 0;
                        tipoSolucionAl = Integer.parseInt(cmbsolucionOculto
                                .getSelectedItem().toString());
                        tipoSolucionAl = tipoSolucionAl + 1;
                        if (!fechaNotificacionAl.equals("0"))
                        {
                            if (idAveria != 0)
                            {
                                gestor.modificarAveria(idAveria, entidad, id_elemento,
                                        gravedadAl, tipoAveriaAl, fechaNotificacionAl,
                                        localizacionAl, tipoSolucionAl,
                                        fechaReparacionAl, observacionesAl, conexion);
                                RefrescarDatos(entidad,id_elemento);
                                averiasOcultolst.setSelectedIndex(seleccionIndice);
                                averiaslst.setSelectedIndex(seleccionIndice);
                            }
                        }
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnModificarMantenimiento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Modificar.Historico");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if (acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (mantenimientoOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgMantenimientoMod"));
                } else
                {

                    if (fechaMantenimientotxt.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this, aplicacion
                                .getI18nString("MsgFecha"));
                    } else
                    {
                        int seleccionIndice = mantenimientoOcultolst.getSelectedIndex();
                        String identificador = mantenimientoOcultolst.getSelectedValue()
                                .toString();
                        int idMantenimiento = 0;
                        try
                        {
                            idMantenimiento = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idMantenimiento = 0;
                        }
                        String fechaMantenimientoAl = fechaMantenimientotxt.getText()
                                .toString();
                        fechaMantenimientoAl = showDatePG(fechaMantenimientoAl);
                        int tipoActuacionAl = Integer.parseInt(cmbTipoActuacionOculto
                                .getSelectedItem().toString());
                        String observacionesMantenimientoAl = txaObservacionesMantenimiento
                                .getText().toString();
                        if (!fechaMantenimientoAl.equals("0"))
                        {
                            if (idMantenimiento != 0)
                            {
                                gestor.modificarMantenimiento(idMantenimiento, entidad,
                                        id_elemento, tipoActuacionAl,
                                        fechaMantenimientoAl,
                                        observacionesMantenimientoAl, conexion);
                                RefrescarDatos(entidad,id_elemento);
                                mantenimientoOcultolst.setSelectedIndex(seleccionIndice);
                                mantenimientolst.setSelectedIndex(seleccionIndice);
                            }
                        }
                    }// else si esta escrito la fecha
                }// si ha seleccionado en el listado
            }// si entidad !=0
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnEliminarMantenimiento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (mantenimientoOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgMantenimientoDel"));
                } else
                {
                    String laFecha = mantenimientolst.getSelectedValue().toString();
                    int opcionElegida = JOptionPane.showConfirmDialog(this, "¿"
                            + aplicacion.getI18nString("MsgConfirmarMantenimiento") + " "
                            + laFecha + "?");

                    if (opcionElegida == JOptionPane.YES_OPTION)
                    {
                        int seleccionIndice = mantenimientoOcultolst.getSelectedIndex();
                        String identificador = mantenimientoOcultolst.getSelectedValue()
                                .toString();
                        int idMantenimiento = 0;
                        try
                        {
                            idMantenimiento = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idMantenimiento = 0;
                        }

                        gestor.eliminarMantenimiento(idMantenimiento, conexion);
                        RefrescarDatos(entidad,id_elemento);
                        if (listModelMantenimiento.isEmpty())
                        {
                            mantenimientolst.setSelectedIndex(-1);
                            mantenimientoOcultolst.setSelectedIndex(-1);
                        } else
                        {
                            mantenimientolst.setSelectedIndex(0);
                            mantenimientoOcultolst.setSelectedIndex(0);
                        }
                    }
                }
            }
       } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    private void btnEliminarAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();

            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Infraestructuras.Eliminar.Aviso");
             * acceso =
             * appcont.checkPermission(geopistaPerm,"Infraestructuras");
             * 
             * if (acceso) {
             */
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (avisosOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgAvisoDel"));
                } else
                {
                    String laFecha = avisoslst.getSelectedValue().toString();
                    int opcionElegida = JOptionPane.showConfirmDialog(this, "¿"
                            + aplicacion.getI18nString("MsgConfirmarAviso") + " "
                            + laFecha + "?");

                    if (opcionElegida == JOptionPane.YES_OPTION)
                    {
                        int seleccionIndice = avisosOcultolst.getSelectedIndex();
                        String identificador = avisosOcultolst.getSelectedValue()
                                .toString();
                        int idAviso = 0;
                        try
                        {
                            idAviso = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idAviso = 0;
                        }

                        gestor.eliminarAviso(idAviso, conexion);
                        RefrescarDatos(entidad,id_elemento);
                        if (listModelMantenimiento.isEmpty())
                        {
                            avisoslst.setSelectedIndex(-1);
                            avisosOcultolst.setSelectedIndex(-1);
                        } else
                        {
                            avisoslst.setSelectedIndex(0);
                            avisosOcultolst.setSelectedIndex(0);
                        }
                    }
                }
                // }
            }
        } catch (Exception Ex)
        {
            Ex.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    public void RefrescarDatos(int entidad, int idElemento)
    {
        try
        {

            conexion = GeopistaInfraPostGreEventos.getDBConnection();
            txaMotivo.setText("");
            txaObservaciones.setText("");
            txaObservacionesMantenimiento.setText("");
            fechanotificaciontxt.setText("");
            fechaMantenimientotxt.setText("");
            fechareparaciontxt.setText("");
            gravedadtxt.setText("");
            // direcciontxt.setText("");
            fechaAvisotxt.setText("");
            cmbTipoActuacion.setSelectedIndex(-1);
            if (cmbTipoActuacion.getItemCount() != 0)
            {
                cmbTipoActuacion.setSelectedIndex(0);
            } else
            {
                cmbTipoActuacion.setSelectedIndex(-1);
            }
            if (cmbtipoaveria.getItemCount() != 0)
            {
                cmbtipoaveria.setSelectedIndex(0);
            } else
            {
                cmbtipoaveria.setSelectedIndex(-1);
            }
            if (cmbsolucion.getItemCount() != 0)
            {
                cmbsolucion.setSelectedIndex(0);
            } else
            {
                cmbsolucion.setSelectedIndex(-1);
            }

            activarAvisochk.setSelected(false);

            avisoslst.removeAll();
            avisosOcultolst.removeAll();
            averiaslst.removeAll();
            averiasOcultolst.removeAll();
            mantenimientolst.removeAll();
            mantenimientoOcultolst.removeAll();

            listModelAvisos.removeAllElements();
            listModelAvisosOculto.removeAllElements();
            listModelAverias.removeAllElements();
            listModelAveriasOculto.removeAllElements();
            listModelMantenimiento.removeAllElements();
            listModelMantenimientoOculto.removeAllElements();

            /* DATOS DE MANTENIMIENTO */
            ArrayList recojoMantenimiento = gestor.getListadoMantenimiento(entidad,idElemento,
                    conexion);

            Iterator alIt = recojoMantenimiento.iterator();
            while (alIt.hasNext())
            {
                Integer idMantenimiento = (Integer) alIt.next();
                listModelMantenimientoOculto.addElement(idMantenimiento);
                // código para pasar de integer a int:
                int idMantenimientoInt = ((Integer) idMantenimiento).intValue();
                String fechaMantenimiento = gestor.getFechaMantenimiento(
                        idMantenimientoInt, conexion);
                String fechaMantenimientoFormateada = showDateSP(fechaMantenimiento);
                listModelMantenimiento.addElement(fechaMantenimientoFormateada);
            }

            /* DATOS DE AVERIAS */
            ArrayList recojoAverias = gestor.getListadoAverias(entidad,idElemento, conexion);

            Iterator alItA = recojoAverias.iterator();
            while (alItA.hasNext())
            {
                Integer idAveria = (Integer) alItA.next();
                listModelAveriasOculto.addElement(idAveria);
                // código para pasar de integer a int:
                int idAveriaInt = ((Integer) idAveria).intValue();
                String fechaAveria = gestor.getFechaAveria(idAveriaInt, conexion);
                String fechaAveriaFormateada = showDateSP(fechaAveria);
                listModelAverias.addElement(fechaAveriaFormateada);
            }
            // long idLoc = gestor.getIdLocalizacion();
            /* DATOS DE AVISOS */

            ArrayList recojoAvisos = gestor.getListadoAvisos(entidad,idElemento, conexion);

            Iterator alItB = recojoAvisos.iterator();
            while (alItB.hasNext())
            {
                Integer idAviso = (Integer) alItB.next();
                listModelAvisosOculto.addElement(idAviso);
                int idAvisoInt = ((Integer) idAviso).intValue();
                String fechaAviso = gestor.getFechaAviso(idAvisoInt, conexion);
                String fechaAvisoFormateada = showDateSP(fechaAviso);
                listModelAvisos.addElement(fechaAvisoFormateada);
            }
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    /**
     * Cambia el formato de fecha al formato español. De POSTGRE lo recibe
     * aaaa-mm-dd y lo formatea a dd-mm-aaaa
     * 
     * @params fecha Es la fecha que se quiere formatear en la base de datos
     * @return Devuelve la fecha con el formato dd-mm-aaaa
     */
    public String showDateSP(String fecha)
    {
        String fechaSP="";
        if (fecha!=null && !fecha.equals("")){
	        String dato = fecha;
	        String arrayDato[] = dato.split("-");
	        String dia = arrayDato[2];
	        String mes = arrayDato[1];
	        String anyo = arrayDato[0];
	        fechaSP= dia + "-" + mes + "-" + anyo;
        }
	   return fechaSP;

    }

    /**
     * Comprueba que la fecha introducida por el usuario es correcta y la da el
     * formato de fecha que utiliza POSTGRE
     * 
     * @params fecha Es la fecha que se quiere comprobar
     * @return Devuelve la fecha con el formato de fecha de POSTGRE
     */
    public String showDatePG(String fecha)
    {
        int diaIncorrecto = 0;
        int anyoBisiesto = 0;
        String dato = fecha;
        String arrayDato[] = dato.split("-");
        int elementos = arrayDato.length;

        if (elementos == 3)
        {
            String dia = arrayDato[0];
            String mes = arrayDato[1];
            String anyo = arrayDato[2];
            if ((dia.length() != 2) || (mes.length() != 2) || (anyo.length() != 4))
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgFormatoFecha"));
                return "0";
            }

            else
            {
                Pattern p = Pattern.compile("\\D");// busca caracteres que no
                // sean números
                Matcher m = p.matcher(dia + mes + anyo);
                if (m.find())
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgCaracterFecha"));
                    return "0";
                }

                else
                {
                    int diaN = 0;
                    int mesN = 0;
                    int anyoN = 0;
                    try
                    {
                        mesN = Integer.parseInt(mes);
                        if ((mesN > 12) || (mesN == 0))
                        {
                            JOptionPane.showMessageDialog(this, aplicacion
                                    .getI18nString("MsgMesNoValido"));
                            return "0";
                        } else
                        {
                            try
                            {
                                diaN = Integer.parseInt(dia);
                                if (diaN == 0)
                                {
                                    diaIncorrecto = 1;
                                }
                                if ((mesN == 1) || (mesN == 3) || (mesN == 5)
                                        || (mesN == 7) || (mesN == 8) || (mesN == 10)
                                        || (mesN == 12))
                                {
                                    if (diaN > 31)
                                    {
                                        diaIncorrecto = 1;
                                    }
                                } else if (mesN == 2)
                                {
                                    if (diaN > 29)
                                    {
                                        diaIncorrecto = 1;
                                    }
                                    if (diaN == 29)
                                    {
                                        anyoN = Integer.parseInt(anyo);
                                        if (anyoN % 4 == 0)
                                        {
                                            anyoBisiesto = 0;
                                        } else
                                        {
                                            anyoBisiesto = 1;
                                        }
                                    }
                                } else
                                {
                                    if (diaN > 30)
                                    {
                                        diaIncorrecto = 1;
                                    }
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                return "0";
                            }
                            if ((diaIncorrecto == 0) && (anyoBisiesto == 0))
                            {
                                String fechaPG = anyo + "-" + mes + "-" + dia;
                                return fechaPG;
                            } else
                            {
                                if (diaIncorrecto != 0)
                                {
                                    JOptionPane.showMessageDialog(this, aplicacion
                                            .getI18nString("MsgDiaNoValido"));
                                } else
                                {
                                    if (anyoBisiesto != 0)
                                    {
                                        JOptionPane.showMessageDialog(this, aplicacion
                                                .getI18nString("MsgAnyoBisiesto"));
                                    }
                                }
                                return "0";
                            }
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        return "0";
                    }

                }
            }
        }// if elementos =) 3
        else
        {
            JOptionPane.showMessageDialog(this, aplicacion
                    .getI18nString("MsgFormatoFecha"));
            return "0";
        }

    }

    private void btnEliminarAveria_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();
            if (entidad == 0)
            {
                JOptionPane.showMessageDialog(this, aplicacion
                        .getI18nString("MsgEntidad"));
            } else
            {
                if (averiasOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion
                            .getI18nString("MsgAveriaDel"));
                } else
                {
                    String laFecha = averiaslst.getSelectedValue().toString();
                    int opcionElegida = JOptionPane.showConfirmDialog(this, "¿"
                            + aplicacion.getI18nString("MsgConfirmarAveria") + " "
                            + laFecha + "?");

                    if (opcionElegida == JOptionPane.YES_OPTION)
                    {
                        int seleccionIndice = averiasOcultolst.getSelectedIndex();
                        String identificador = averiasOcultolst.getSelectedValue()
                                .toString();
                        int idAveria = 0;
                        try
                        {
                            idAveria = Integer.parseInt(identificador);
                        } catch (Exception eG)
                        {
                            idAveria = 0;
                        }

                        gestor.eliminarAveria(idAveria, conexion);
                        RefrescarDatos(entidad,id_elemento);

                        if (listModelMantenimiento.isEmpty())
                        {
                            averiaslst.setSelectedIndex(-1);
                            averiasOcultolst.setSelectedIndex(-1);
                        } else
                        {
                            averiaslst.setSelectedIndex(0);
                            averiasOcultolst.setSelectedIndex(0);
                        }
                    }
                }
            }
        } catch (Exception eX)
        {
            eX.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }

    /**
     * Listado de los subdominios del dominio padre especificado
     * 
     * @param valor
     *            identificador del combo en el que se insertarán los
     *            subdominios
     * @param idDomain
     *            identificador del dominio del que se van a obtener los
     *            subdominios
     */

    public void leerDominios(int valor, int idDomain) throws SQLException
    {
        ResultSet r = null;
        PreparedStatement ps = null;
        try
        {
            conexion = GeopistaInfraPostGreEventos.getDBConnection();
            ps = conexion.prepareStatement("seleccionDominios");
            ps.setInt(1, idDomain);
            ps.setString(2,aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY));
            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
                switch (valor)
                    {
                    case 0:
                        while (r.next())
                        {
                            cmbTipoActuacion.addItem(r.getString(2));
                            cmbTipoActuacionOculto.addItem(r.getString(1));
                        }
                    case 1:
                        while (r.next())
                        {
                            cmbtipoaveria.addItem(r.getString(2));
                            cmbTipoAveriaOculto.addItem(r.getString(1));
                        }
                    case 2:
                        while (r.next())
                        {
                            cmbsolucion.addItem(r.getString(2));
                            cmbsolucionOculto.addItem(r.getString(1));
                        }
                    }

            }
        } finally
        {
            aplicacion.closeConnection(conexion, ps, null, r);
        }
    }

    public void leerVias() throws SQLException
    {

        conexion = GeopistaInfraPostGreEventos.getDBConnection();

        ResultSet r = null;
        PreparedStatement ps = conexion.prepareStatement("seleccionVias");
        if (!ps.execute())
        {
        } else
        {
            r = ps.getResultSet();
            while (r.next())
            {
                cmbLocalizacion.addItem(r.getString("nombrecatastro"));
                jcomboHide.addItem(r.getString("id"));
            }
        }

    }

    private void cmbTipoActuacion_actionPerformed(ActionEvent e)
    {
        int indice = 0;
        indice = cmbTipoActuacion.getSelectedIndex();
        cmbTipoActuacionOculto.setSelectedIndex(indice);
    }

    private void cmbtipoaveria_actionPerformed(ActionEvent e)
    {
        int indice = 0;
        indice = cmbtipoaveria.getSelectedIndex();
        cmbTipoAveriaOculto.setSelectedIndex(indice);
    }

    private void cmbsolucion_actionPerformed(ActionEvent e)
    {
        int indice = 0;
        indice = cmbsolucion.getSelectedIndex();
        cmbsolucionOculto.setSelectedIndex(indice);
    }

    private void cmbLocalizacion_actionPerformed(ActionEvent e)
    {
        int indice = 0;
        indice = cmbLocalizacion.getSelectedIndex();
        jcomboHide.setSelectedIndex(indice);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub

    }

}