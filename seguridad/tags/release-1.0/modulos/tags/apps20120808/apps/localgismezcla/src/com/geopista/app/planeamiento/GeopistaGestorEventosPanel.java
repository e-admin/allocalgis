/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

package com.geopista.app.planeamiento;

import java.awt.Color;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Esta clase permite consultar, eliminar y modificar los eventos y de avisos de
 * gestión del suelo. Contiene un mapa en el cual el usuario puede seleccionar
 * sobre una de las capas del mismo para consultar los datos del ámbito de
 * gestión seleccionado.
 */
public class GeopistaGestorEventosPanel extends JPanel implements WizardPanel,
ListSelectionListener
{
    private JPanel pnlLocalizador = new JPanel();
    
    private JPanel pnlAverias = new JPanel();
    
    private JScrollPane scpMapa = new JScrollPane();
    
    private JPanel pnlMapa = new JPanel();
    
    private JPanel pnlAvisos = new JPanel();
    
    private JButton btnAnterior = new JButton();
    
    private JLabel lblNumeroAmbito = new JLabel();
    
    private JComboBox cmbNumeroAmbito = new JComboBox();
    
    private JLabel lblNombre = new JLabel();
    
    private JLabel lblTipo = new JLabel();
    
    private JLabel lblEjecutado = new JLabel();
    
    private JPanel pnlHistorico = new JPanel();
    
    private JScrollPane scpHistorico = new JScrollPane();
    
    private DefaultListModel listModelHistoricoOculto = new DefaultListModel();
    
    private JList historicoOcultolst = new JList(listModelHistoricoOculto);
    
    private DefaultListModel listModelHistorico = new DefaultListModel();
    
    private JList historicolst = new JList(listModelHistorico);
    
    private JLabel lblFechaEvento = new JLabel();
    
    private JTextField fechaeventotxt = new JTextField();
    
    private JButton btnNuevoEvento = new JButton();
    
    private JLabel lblAutor = new JLabel();
    
    private JTextField txtAutor = new JTextField();
    
    private JButton btnModificarEvento = new JButton();
    
    private JButton btnEliminarEvento = new JButton();
    
    private JLabel lblObservaciones = new JLabel();
    
    private JScrollPane scpObservaciones = new JScrollPane();
    
    private JTextArea txaObservaciones = new JTextArea();
    
    private JScrollPane scpAvisos = new JScrollPane();
    
    private DefaultListModel listModelOcultoAvisos = new DefaultListModel();
    
    private JList avisosOcultolst = new JList(listModelOcultoAvisos);
    
    private DefaultListModel listModelAvisos = new DefaultListModel();
    
    private JList avisoslst = new JList(listModelAvisos);
    
    private JLabel lblFechaAviso = new JLabel();
    
    private JButton btnEliminarAviso = new JButton();
    
    private JButton btnModificarAviso = new JButton();
    
    private JButton btnNuevoAviso = new JButton();
    
    private JTextField txtFechaAviso = new JTextField();
    
    private JLabel lblPeriodicidad = new JLabel();
    
    private JComboBox cmbPeriodicidad = new JComboBox();
    
    private JLabel lblMensaje = new JLabel();
    
    private JScrollPane scpMensaje = new JScrollPane();
    
    private JTextArea txaMensaje = new JTextArea();
    
    private JTextField txtTipo = new JTextField();
    
    private JTextField txtEjecutado = new JTextField();
    
    private JTextField txtNombre = new JTextField();
    
    private JLabel lblObservacionesAvisos = new JLabel();
    
    private JScrollPane scpObservacionesAvisos = new JScrollPane();
    
    private JTextArea txaObservacionesAvisos = new JTextArea();
    
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    
    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion
            .getString("url.herramientas.gestoreventos"));
    
    private String numeroDeAmbito;
    
    private ActionMap actionMap1 = new ActionMap();
    
    private String dominio;
    
    private JOptionPane OpCuadroDialogo;
    
    private boolean acceso = true;
    
    private GeopistaPostGreGestorEventos gestor = new GeopistaPostGreGestorEventos();
    
    private Connection conexion = null;
    
    private boolean bloquearRefrescar = false;
    
    private JComboBox cmbPeriodicidadOculto = new JComboBox();
    
    private String nextID = null;
    
    private WizardContext wizardContext;
    
    private GeopistaGestorEventosPanel geopistaGestorEventosPanel = null;
    
    public GeopistaGestorEventosPanel()
    {
        try
        {
            geopistaGestorEventosPanel = this;
            jbInit();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {
        bloquearRefrescar = true;
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
                                hb.enableHelpKey(geopistaEditor1,
                                        "planeamientoGestorEventos02", hs);
                                hb.enableHelpKey(geopistaGestorEventosPanel,
                                        "planeamientoGestionEventos01", hs);
                                
                            } catch (Exception excp)
                            {
                            }
                            
                            setLayout(null);
                            
                            pnlLocalizador
                            .setBounds(new Rectangle(5, 10, 395, 50));
                            pnlLocalizador.setBorder(BorderFactory
                                    .createTitledBorder(aplicacion.getI18nString("gestoreventos.localizador")));
                            pnlLocalizador.setLayout(null);
                            
                            pnlAverias.setBounds(new Rectangle(5, 70, 395, 105));
                            pnlAverias.setBorder(BorderFactory
                                    .createTitledBorder(aplicacion.getI18nString("gestoreventos.datosambito")));
                            pnlAverias.setLayout(null);
                            
                            pnlMapa.setBounds(new Rectangle(410, 20, 380, 510));
                            pnlMapa.setBorder(BorderFactory
                                    .createTitledBorder(""));
                            pnlMapa.setLayout(null);
                            
                            historicoOcultolst.setVisible(false);
                            avisosOcultolst.setVisible(false);
                            
                            pnlAvisos.setBounds(new Rectangle(5, 350, 395, 180));
                            pnlAvisos.setBorder(BorderFactory
                                    .createTitledBorder(aplicacion.getI18nString("gestoreventos.avisos")));
                            pnlAvisos.setLayout(null);
                            
                            cmbPeriodicidadOculto.setVisible(false);
                            cmbPeriodicidadOculto.setBounds(new Rectangle(185,
                                    70, 110, 20));
                            
                            lblNumeroAmbito.setText(aplicacion.getI18nString("gestoreventos.numeroambito"));
                            
                            cmbNumeroAmbito.setBackground(Color.white);
                            
                            lblNumeroAmbito.setBounds(new Rectangle(10, 20, 135,
                                    20));
                            
                            cmbNumeroAmbito.setBounds(new Rectangle(145, 20, 175,
                                    20));
                            cmbNumeroAmbito
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    cmbNumeroAmbito_actionPerformed(e);
                                }
                                    });
                            
                            
                            lblNombre.setText(aplicacion.getI18nString("gestoreventos.nombre"));
                            lblNombre.setBounds(new Rectangle(10, 20, 135, 20));
                            
                            lblTipo.setText(aplicacion.getI18nString("gestoreventos.tipogestion"));
                            lblTipo.setBounds(new Rectangle(10, 45, 145, 20));
                            
                            lblEjecutado.setText(aplicacion.getI18nString("gestoreventos.ejecutado"));
                            lblEjecutado
                            .setBounds(new Rectangle(10, 70, 140, 20));
                            
                            cmbPeriodicidad.setBackground(Color.white);
                            
                            cmbPeriodicidad.setBounds(new Rectangle(185, 45, 110,
                                    20));
                            cmbPeriodicidad
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    cmbPeriodicidad_actionPerformed(e);
                                }
                                    });
                            
                            pnlHistorico
                            .setBounds(new Rectangle(5, 185, 395, 155));
                            pnlHistorico.setBorder(BorderFactory
                                    .createTitledBorder(aplicacion.getI18nString("gestoreventos.historico")));
                            pnlHistorico.setLayout(null);
                            
                            scpHistorico.setBounds(new Rectangle(10, 20, 90, 70));
                            lblFechaEvento.setText(aplicacion.getI18nString("gestoreventos.fechaevento"));
                            lblFechaEvento.setBounds(new Rectangle(105, 20, 135,
                                    20));
                            
                            fechaeventotxt.setBounds(new Rectangle(210, 20, 85,
                                    20));
                            
                            btnNuevoEvento.setText(aplicacion.getI18nString("gestoreventos.nuevo"));
                            btnNuevoEvento.setBounds(new Rectangle(300, 20, 85,
                                    20));
                            btnNuevoEvento.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    btnNuevoEvento_actionPerformed(e);
                                }
                                    });
                            
                            lblAutor.setText(aplicacion.getI18nString("gestoreventos.autor"));
                            lblAutor.setBounds(new Rectangle(105, 65, 75, 20));
                            txtAutor.setBounds(new Rectangle(145, 70, 150, 20));
                            btnModificarEvento.setText(aplicacion.getI18nString("gestoreventos.modificar"));
                            btnModificarEvento.setBounds(new Rectangle(300, 45,
                                    85, 20));
                            btnModificarEvento
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    btnModificarEvento_actionPerformed(e);
                                }
                                    });
                            
                            btnEliminarEvento.setText(aplicacion.getI18nString("gestoreventos.eliminar"));
                            btnEliminarEvento.setBounds(new Rectangle(300, 70,
                                    85, 20));
                            btnEliminarEvento
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    btnEliminarEvento_actionPerformed(e);
                                }
                                    });
                            
                            lblObservaciones.setText(aplicacion.getI18nString("gestoreventos.observaciones"));
                            lblObservaciones.setBounds(new Rectangle(10, 95, 90,
                                    20));
                            
                            scpObservaciones.setBounds(new Rectangle(105, 95,
                                    285, 50));
                            txaObservaciones.setLineWrap(true);
                            
                            scpAvisos.setBounds(new Rectangle(10, 20, 90, 65));
                            
                            lblFechaAviso.setText(aplicacion.getI18nString("gestoreventos.fechaaviso"));
                            lblFechaAviso.setBounds(new Rectangle(105, 20, 100,
                                    20));
                            
                            btnEliminarAviso.setText(aplicacion.getI18nString("gestoreventos.eliminar"));
                            btnEliminarAviso.setBounds(new Rectangle(300, 70, 85,
                                    20));
                            btnEliminarAviso
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    btnEliminarAviso_actionPerformed(e);
                                }
                                    });
                            
                            btnModificarAviso.setText(aplicacion.getI18nString("gestoreventos.modificar"));
                            btnModificarAviso.setBounds(new Rectangle(300, 45,
                                    85, 20));
                            btnModificarAviso
                            .addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(
                                        ActionEvent e)
                                {
                                    btnModificarAviso_actionPerformed(e);
                                }
                                    });
                            
                            btnNuevoAviso.setText(aplicacion.getI18nString("gestoreventos.nuevo"));
                            btnNuevoAviso
                            .setBounds(new Rectangle(300, 20, 85, 20));
                            btnNuevoAviso.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    btnNuevoAviso_actionPerformed(e);
                                }
                                    });
                            
                            txtFechaAviso
                            .setBounds(new Rectangle(210, 20, 85, 20));
                            
                            lblPeriodicidad.setText(aplicacion.getI18nString("gestoreventos.periodicidad"));
                            lblPeriodicidad.setBounds(new Rectangle(105, 45, 95,
                                    20));
                            
                            lblMensaje.setText(aplicacion.getI18nString("gestoreventos.mensaje"));
                            lblMensaje.setBounds(new Rectangle(10, 90, 85, 20));
                            
                            scpMensaje.setBounds(new Rectangle(100, 95, 285, 35));
                            txaMensaje.setLineWrap(true);
                            
                            txtTipo.setBounds(new Rectangle(145, 45, 240, 20));
                            txtTipo.setEnabled(false);
                            txtTipo.setDisabledTextColor(Color.black);
                            txtTipo.setBackground(new Color(200, 200, 200));
                            
                            txtEjecutado
                            .setBounds(new Rectangle(145, 70, 65, 20));
                            txtEjecutado.setEnabled(false);
                            txtEjecutado.setDisabledTextColor(Color.black);
                            txtEjecutado.setBackground(new Color(200, 200, 200));
                            
                            txtNombre.setBounds(new Rectangle(145, 20, 240, 20));
                            txtNombre.setEnabled(false);
                            txtNombre.setDisabledTextColor(Color.black);
                            txtNombre.setBackground(new Color(200, 200, 200));
                            lblObservacionesAvisos.setText(aplicacion.getI18nString("gestoreventos.observaciones"));
                            lblObservacionesAvisos.setBounds(new Rectangle(10,
                                    130, 90, 20));
                            scpObservacionesAvisos.setBounds(new Rectangle(100,
                                    135, 285, 35));
                            txaObservacionesAvisos.setLineWrap(true);
                            txaObservacionesAvisos.setWrapStyleWord(true);
                            geopistaEditor1.setBounds(new Rectangle(5, 5, 370,
                                    495));
                            
                            scpObservaciones.getViewport().add(txaObservaciones,
                                    null);
                            pnlHistorico.add(scpObservaciones, null);
                            pnlHistorico.add(lblObservaciones, null);
                            pnlHistorico.add(btnEliminarEvento, null);
                            pnlHistorico.add(btnModificarEvento, null);
                            pnlHistorico.add(txtAutor, null);
                            pnlHistorico.add(lblAutor, null);
                            pnlHistorico.add(btnNuevoEvento, null);
                            pnlHistorico.add(fechaeventotxt, null);
                            pnlHistorico.add(lblFechaEvento, null);
                            
                            scpHistorico.getViewport().add(historicoOcultolst,
                                    null);
                            scpHistorico.getViewport().add(historicolst, null);
                            
                            pnlHistorico.add(scpHistorico, null);
                            
                            add(pnlHistorico, null);
                            
                            scpObservacionesAvisos.getViewport().add(
                                    txaObservacionesAvisos, null);
                            pnlAvisos.add(cmbPeriodicidadOculto, null);
                            pnlAvisos.add(scpObservacionesAvisos, null);
                            pnlAvisos.add(lblObservacionesAvisos, null);
                            scpMensaje.getViewport().add(txaMensaje, null);
                            pnlAvisos.add(scpMensaje, null);
                            pnlAvisos.add(lblMensaje, null);
                            pnlAvisos.add(cmbPeriodicidad, null);
                            pnlAvisos.add(lblPeriodicidad, null);
                            pnlAvisos.add(txtFechaAviso, null);
                            pnlAvisos.add(btnNuevoAviso, null);
                            pnlAvisos.add(btnModificarAviso, null);
                            pnlAvisos.add(btnEliminarAviso, null);
                            pnlAvisos.add(lblFechaAviso, null);
                            scpAvisos.getViewport().add(avisosOcultolst, null);
                            scpAvisos.getViewport().add(avisoslst, null);
                            pnlAvisos.add(scpAvisos, null);
                            add(pnlAvisos, null);
                            pnlMapa.add(geopistaEditor1, null);
                            add(pnlMapa, null);
                            pnlAverias.add(txtNombre, null);
                            pnlAverias.add(txtEjecutado, null);
                            pnlAverias.add(txtTipo, null);
                            pnlAverias.add(lblEjecutado, null);
                            pnlAverias.add(lblTipo, null);
                            pnlAverias.add(lblNombre, null);
                            pnlAverias.add(scpMapa, null);
                            add(pnlAverias, null);
                            pnlLocalizador.add(cmbNumeroAmbito, null);
                            pnlLocalizador.add(lblNumeroAmbito, null);
                            add(pnlLocalizador, null);
                            historicolst
                            .addListSelectionListener(geopistaGestorEventosPanel);
                            avisoslst
                            .addListSelectionListener(geopistaGestorEventosPanel);
                            
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
        
        bloquearRefrescar = false;
        
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
        
        progressDialog.setTitle(aplicacion
                .getI18nString("GeopistaImportacionPanel.CargandoMapaPlaneamiento"));
        
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
                                .getI18nString("GeopistaImportacionPanel.CargandoMapaPlaneamiento"));
                        
                        try
                        {
                            
                            GeopistaPermission geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Modificar Aviso");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnModificarAviso.setEnabled(acceso);
                            
                            geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Nuevo Aviso");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnNuevoAviso.setEnabled(acceso);
                            
                            geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Eliminar Aviso");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnEliminarAviso.setEnabled(acceso);
                            
                            geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Nuevo Historico");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnNuevoEvento.setEnabled(acceso);
                            
                            geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Modificar Historico");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnModificarEvento.setEnabled(acceso);
                            
                            geopistaPerm = new GeopistaPermission(
                            "Geopista.Planeamiento.Eliminar Historico");
                            acceso = aplicacion.checkPermission(geopistaPerm,
                            "Planeamiento");
                            
                            btnEliminarEvento.setEnabled(acceso);
                            
                            try
                            {
                                geopistaEditor1.loadMap(aplicacion
                                        .getString("url.mapa.planeamiento"));
                                
                                
                                
                                
                            } catch (Exception e)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        aplicacion.getMainFrame(),
                                        aplicacion
                                        .getI18nString("errorCargaMapaPlaneamiento"));
                                throw e;
                            }
                            
                            geopistaEditor1.showLayerName(true);
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
                            GeopistaLayer layer07 = (GeopistaLayer) geopistaEditor1
                            .getLayerManager().getLayer(
                                    GeopistaSystemLayers.AMBITOSGESTION);
                            layer07.setActiva(true);
                            layer07.setVisible(true);
                            
                            
                            geopistaEditor1
                            .addGeopistaListener(new GeopistaListener()
                                    {
                                
                                public void selectionChanged(
                                        final AbstractSelection abtractSelection)
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
                                                            
                                                            bloquearRefrescar = true;
                                                            
                                                            progressDialog
                                                            .report(aplicacion
                                                                    .getI18nString("GeopistaGestorEventosPanel.CargandoEventosAvisos"));
                                                            
                                                            try
                                                            {
                                                                
                                                                conexion = GeopistaPostGreGestorEventos
                                                                .getDBConnection();
                                                                txtAutor
                                                                .setText("");
                                                                
                                                                cmbNumeroAmbito
                                                                .removeAllItems();
                                                                ArrayList featuresCollection = (ArrayList) abtractSelection
                                                                .getFeaturesWithSelectedItems(geopistaEditor1
                                                                        .getLayerManager().getLayer(
                                                                                GeopistaSystemLayers.AMBITOSGESTION));
                                                                Iterator featuresCollectionIter = featuresCollection
                                                                .iterator();
                                                                if (!featuresCollectionIter
                                                                        .hasNext())
                                                                {
                                                                    txtEjecutado.setText("");
                                                                    txtAutor.setText("");
                                                                    txtFechaAviso.setText("");
                                                                    fechaeventotxt.setText("");
                                                                    txtNombre.setText("");
                                                                    txtTipo.setText("");
                                                                    txaMensaje.setText("");
                                                                    txaObservaciones.setText("");
                                                                    txaObservacionesAvisos.setText("");
                                                                    if (cmbPeriodicidad.getItemCount() != 0)
                                                                    {
                                                                        cmbPeriodicidad.setSelectedIndex(-1);
                                                                    } else
                                                                    {
                                                                        cmbPeriodicidad.setSelectedIndex(0);
                                                                    }
                                                                    avisoslst.clearSelection();
                                                                    avisosOcultolst.clearSelection();
                                                                    historicolst.clearSelection();
                                                                    historicoOcultolst.clearSelection();
                                                                    
                                                                    avisoslst.removeAll();
                                                                    avisosOcultolst.removeAll();
                                                                    historicolst.removeAll();
                                                                    historicoOcultolst.removeAll();
                                                                    
                                                                    listModelHistorico.removeAllElements();
                                                                    listModelHistoricoOculto.removeAllElements();
                                                                    listModelAvisos.removeAllElements();
                                                                    listModelOcultoAvisos.removeAllElements();
                                                                    
                                                                    return;
                                                                }
                                                                
                                                                String recojoIdAmbito = null;
                                                                while (featuresCollectionIter
                                                                        .hasNext())
                                                                {
                                                                    Feature actualFeature = (Feature) featuresCollectionIter
                                                                    .next();
                                                                    numeroDeAmbito = actualFeature
                                                                    .getString(1); // IDambgest
                                                                    recojoIdAmbito = gestor
                                                                    .getNumero(
                                                                            numeroDeAmbito,
                                                                            conexion);
                                                                    cmbNumeroAmbito
                                                                    .addItem(recojoIdAmbito);
                                                                    
                                                                    if (featuresCollection.size()==1)
                                                                    {
                                                                        geopistaEditor1.zoomTo(actualFeature);
                                                                        //bloquearRefrescar = true;
                                                                    }
                                                                    
                                                                }
                                                                
                                                                if (cmbNumeroAmbito.getItemCount()!=0)
                                                                {
                                                                    bloquearRefrescar = false;
                                                                    cmbNumeroAmbito.setSelectedIndex(0);
                                                                    bloquearRefrescar = true;
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
                                                                
                                                                bloquearRefrescar = false;
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
                                        AbstractSelection abtractSelection)
                                {
                                }
                                    });
                            
                            
                            leerDominios(27);                                    
                            
                            
                            
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
            if (e.getValueIsAdjusting() == false)
            {
                if (historicolst.getSelectedIndex() != -1)
                {
                    String fechaHistorico = historicolst.getSelectedValue().toString();
                    int valorIndexFecha = historicolst.getSelectedIndex();
                    historicoOcultolst.setSelectedIndex(valorIndexFecha);
                    String identificadorEvento = historicoOcultolst.getSelectedValue()
                    .toString();
                    fechaeventotxt.setText(fechaHistorico);
                    
                    String recojoAutorHistorico = gestor.getAutorHistorico(
                            identificadorEvento, conexion);
                    txtAutor.setText(recojoAutorHistorico);
                    String recojoDescriHistorico = gestor.getDescripcionHistorico(
                            identificadorEvento, conexion);
                    txaObservaciones.setText(recojoDescriHistorico);
                }
                
                if (avisoslst.getSelectedIndex() != -1)
                {
                    String fechaAvisoFormat = avisoslst.getSelectedValue().toString();
                    int valorIndexFechaAviso = avisoslst.getSelectedIndex();
                    avisosOcultolst.setSelectedIndex(valorIndexFechaAviso);
                    String identificadorAviso = avisosOcultolst.getSelectedValue().toString();
                    txtFechaAviso.setText(fechaAvisoFormat);
                    String periodicidad = gestor.getPeriodicidadAviso(identificadorAviso,
                            conexion);
                    cmbPeriodicidadOculto.setSelectedItem(periodicidad);
                    int indice = cmbPeriodicidadOculto.getSelectedIndex();
                    cmbPeriodicidad.setSelectedIndex(indice);
                    String comentario = gestor.getComentarioAviso(identificadorAviso,
                            conexion);
                    String mensaje = gestor.getMensajeAviso(identificadorAviso, conexion);
                    
                    txaObservacionesAvisos.setText(comentario);
                    txaMensaje.setText(mensaje);
                }
                
            }
        }catch(SQLException e1)
        {           
            e1.printStackTrace();
            //Informar del error al usuario
        }
    }
    
    private void btnModificarAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.ambitonoseleccionado"));
            } else
            {
                if (avisosOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this,
                            aplicacion.getI18nString("gestoreventos.avisonoseleccionado"));
                } else
                {
                    if (this.txtFechaAviso.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this,
                                aplicacion.getI18nString("gestoreventos.escribafechaaviso"));
                    } else
                    {
                        int seleccionIndice = avisosOcultolst.getSelectedIndex();
                        String fechaAvisoPg;
                        String idAviso = avisosOcultolst.getSelectedValue().toString();
                        String fechaAvisoMod = txtFechaAviso.getText().toString();
                        fechaAvisoPg = showDatePG(fechaAvisoMod);
                        String dominioPeriodicidad = cmbPeriodicidadOculto
                        .getSelectedItem().toString();
                        String comentarioAvisoMod = txaObservacionesAvisos.getText()
                        .toString();
                        String mensajeAvisoMod = txaMensaje.getText().toString();
                        
                        if (!fechaAvisoPg.equals("0"))
                        {
                            gestor.modificarAviso(idAviso, fechaAvisoPg,
                                    dominioPeriodicidad, comentarioAvisoMod,
                                    mensajeAvisoMod, conexion);
                            RefrescarDatos(numeroDeAmbito);
                            avisoslst.setSelectedIndex(seleccionIndice);
                        }
                    }
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
    
    private void btnModificarEvento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.ambitonoseleccionado"));
            } else
            {
                if (historicoOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this,
                            aplicacion.getI18nString("gestoreventos.eventonoseleccionado"));
                } else
                {
                    if (fechaeventotxt.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this,
                                aplicacion.getI18nString("gestoreventos.escribafechaevento"));
                    } else
                    {
                        int seleccionIndice = historicoOcultolst.getSelectedIndex();
                        String idEvento = historicoOcultolst.getSelectedValue()
                        .toString();
                        String fechaHistoricoMod = fechaeventotxt.getText().toString();
                        String autorHistoricoMod = txtAutor.getText().toString();
                        String descriHistoricoMod = txaObservaciones.getText().toString();
                        String fechaHistoricoPg = showDatePG(fechaHistoricoMod);
                        if (!fechaHistoricoPg.equals("0"))
                        {
                            
                            gestor.modificarHistorico(idEvento, fechaHistoricoPg,
                                    autorHistoricoMod, descriHistoricoMod, conexion);
                            RefrescarDatos(numeroDeAmbito);
                            historicolst.setSelectedIndex(seleccionIndice);
                        }
                    }
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
    
    private void btnNuevoEvento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.noambitoseleccionado"));
            } else
            {
                if (fechaeventotxt.getText().toString().equals(""))
                {
                    JOptionPane.showMessageDialog(this,
                            aplicacion.getI18nString("gestoreventos.escribafechaevento"));
                } else
                {
                    
                    int idHistoricoMax = gestor.getMaxIdEvento(conexion);
                    String fechaHistoricoMod = fechaeventotxt.getText().toString();
                    String autorHistoricoMod = txtAutor.getText().toString();
                    String descriHistoricoMod = txaObservaciones.getText().toString();
                    String fechaHistoricoPg = showDatePG(fechaHistoricoMod);
                    if (!fechaHistoricoPg.equals("0"))
                    {
                        if ((int) idHistoricoMax != 0)
                        {
                            gestor.insertarEvento(idHistoricoMax, numeroDeAmbito,
                                    fechaHistoricoPg, autorHistoricoMod,
                                    descriHistoricoMod, conexion);
                            RefrescarDatos(numeroDeAmbito);
                            int cuenta = this.listModelHistorico.size();
                            int i = 1;
                            int numero = 0;
                            String valor;
                            String valor_d;
                            for (i = 1; i < cuenta; i++)
                            {
                                valor = listModelHistoricoOculto.get(i).toString();
                                numero = Integer.parseInt(valor);
                                
                                if (numero == idHistoricoMax)
                                {
                                    historicolst.setSelectedIndex(i);
                                    
                                    fechaeventotxt.setText("");
                                    txtAutor.setText("");
                                    txaObservaciones.setText("");
                                }
                            }
                        }
                    }
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
    
    private void btnNuevoAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.noambitoseleccionado"));
            } else
            {
                if (cmbPeriodicidad.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this,
                            aplicacion.getI18nString("gestoreventos.seleccioneperiodicidad"));
                } else
                {
                    
                    if (this.txtFechaAviso.getText().toString().equals(""))
                    {
                        JOptionPane.showMessageDialog(this,
                                aplicacion.getI18nString("gestoreventos.escribafechaaviso"));
                        
                    } else
                    {
                        
                        int idAvisoMax = gestor.getMaxIdAviso(conexion);
                        String fechaAvisoMod = txtFechaAviso.getText().toString();
                        
                        String dominioPeriodicidad = cmbPeriodicidadOculto
                        .getSelectedItem().toString();
                        String comentarioAvisoMod = txaObservacionesAvisos.getText()
                        .toString();
                        String mensajeAvisoMod = txaMensaje.getText().toString();
                        
                        if ((int) idAvisoMax != 0)
                        {
                            String fechaAvisoPg = showDatePG(fechaAvisoMod);
                            if (!fechaAvisoPg.equals("0"))
                            {
                                gestor.insertarAviso(idAvisoMax, numeroDeAmbito,
                                        fechaAvisoPg, dominioPeriodicidad,
                                        mensajeAvisoMod, comentarioAvisoMod, conexion);
                                RefrescarDatos(numeroDeAmbito);
                                
                                int cuenta = this.listModelAvisos.size();
                                int i = 1;
                                int numero = 0;
                                String valor;                                
                                for (i = 1; i < cuenta; i++)
                                {
                                    valor = this.listModelOcultoAvisos.get(i).toString();
                                    numero = Integer.parseInt(valor);
                                    
                                    if (numero == idAvisoMax)
                                    {
                                        avisoslst.setSelectedIndex(i);
                                        
                                        txtFechaAviso.setText("");
                                        cmbPeriodicidad.setSelectedIndex(0);
                                        txaMensaje.setText("");
                                        txaObservacionesAvisos.setText("");
                                        
                                    }
                                }
                            }
                        }
                    }
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
    
    public void RefrescarDatos(String numeroDeAmbito)
    {
        txtEjecutado.setText("");
        txtAutor.setText("");
        txtFechaAviso.setText("");
        fechaeventotxt.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txaMensaje.setText("");
        txaObservaciones.setText("");
        txaObservacionesAvisos.setText("");
        if (cmbPeriodicidad.getItemCount() != 0)
        {
            cmbPeriodicidad.setSelectedIndex(-1);
        } else
        {
            cmbPeriodicidad.setSelectedIndex(0);
        }
        avisoslst.clearSelection();
        avisosOcultolst.clearSelection();
        historicolst.clearSelection();
        historicoOcultolst.clearSelection();
        
        avisoslst.removeAll();
        avisosOcultolst.removeAll();
        historicolst.removeAll();
        historicoOcultolst.removeAll();
        
        listModelHistorico.removeAllElements();
        listModelHistoricoOculto.removeAllElements();
        listModelAvisos.removeAllElements();
        listModelOcultoAvisos.removeAllElements();
        if (!bloquearRefrescar)
        {
            try
            {
                conexion = gestor.getDBConnection();
                
                
                String recojoNombreAmbito = gestor.getNombre(numeroDeAmbito, conexion);
                txtNombre.setText(recojoNombreAmbito);
                String recojoTipoGestion = gestor.getIdtipogest(numeroDeAmbito, conexion);
                
                if (recojoTipoGestion != "")
                {
                    String descripcionTipoGest = gestor.getDescriTipoGest(
                            recojoTipoGestion, 24, conexion);
                    txtTipo.setText(descripcionTipoGest);
                }
                String recojoEjecutado = gestor.getEjecutado(numeroDeAmbito, conexion);
                if (recojoEjecutado!=null && recojoEjecutado!= "")
                {
                    if (recojoEjecutado.equals("N"))
                    {
                        txtEjecutado.setText(aplicacion.getI18nString("gestoreventos.no"));
                    }
                    else if (recojoEjecutado.equals("S"))
                    {
                        txtEjecutado.setText(aplicacion.getI18nString("gestoreventos.si"));
                    }
                }
                this.numeroDeAmbito = numeroDeAmbito;
                ArrayList recojoHistorico = gestor.getListadoEventos(numeroDeAmbito,
                        conexion);
                
                Iterator alIt = recojoHistorico.iterator();
                while (alIt.hasNext())
                {
                    String fechaEventoFormateada = "";
                    String valorHistorico = alIt.next().toString();
                    listModelHistoricoOculto.addElement(valorHistorico);
                    String fechaEvento = gestor.getFechaEventos(valorHistorico, conexion);
                    fechaEventoFormateada = showDateSP(fechaEvento);
                    listModelHistorico.addElement(fechaEventoFormateada);
                }
                
                ArrayList recojoAvisos = gestor
                .getListadoAvisos(numeroDeAmbito, conexion);
                
                Iterator alItAvisos = recojoAvisos.iterator();
                while (alItAvisos.hasNext())
                {
                    String fechaAvisoFormateada = "";
                    String valorAvisos = alItAvisos.next().toString();
                    listModelOcultoAvisos.addElement(valorAvisos);
                    String fechaAviso = gestor.getFechaAviso(valorAvisos, conexion);
                    fechaAvisoFormateada = showDateSP(fechaAviso);
                    listModelAvisos.addElement(fechaAvisoFormateada);
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    
    private void btnEliminarEvento_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Planeamiento.Eliminar Historico");
             * acceso = appcont.checkPermission(geopistaPerm,"Planeamiento");
             * 
             * if (acceso) {
             */
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.noambitoseleccionado"));
            } else
            {
                if (historicoOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane
                    .showMessageDialog(this, aplicacion.getI18nString("gestoreventos.seleccioneeventoeliminar"));
                    
                } else
                {
                    String laFecha = historicolst.getSelectedValue().toString();
                    int opcionElegida = JOptionPane.showConfirmDialog(this,
                            aplicacion.getI18nString("gestoreventos.seguroborrarevento")+" " + laFecha
                            + "?");
                    
                    if (opcionElegida == JOptionPane.YES_OPTION)
                    {
                        String idEvento = historicoOcultolst.getSelectedValue()
                        .toString();
                        
                        gestor.eliminarHistorico(idEvento, conexion);
                        RefrescarDatos(numeroDeAmbito);
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
        
    }
    
    private void btnEliminarAviso_actionPerformed(ActionEvent e)
    {
        try
        {
            conexion = gestor.getDBConnection();
            /*
             * GeopistaPermission geopistaPerm = new
             * GeopistaPermission("Geopista.Planeamiento.Eliminar Aviso");
             * acceso = appcont.checkPermission(geopistaPerm,"Planeamiento");
             * 
             * if (acceso) {
             */
            if (this.cmbNumeroAmbito.getItemCount() == 0)
            {
                JOptionPane.showMessageDialog(this,
                        aplicacion.getI18nString("gestoreventos.noambitoseleccionado"));
            } else
            {
                
                if (avisosOcultolst.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(this, aplicacion.getI18nString("gestoreventos.seleccioneavisoeliminar"));
                    
                } else
                {
                    String laFecha = avisoslst.getSelectedValue().toString();
                    int opcionElegida = JOptionPane.showConfirmDialog(this,
                            aplicacion.getI18nString("gestoreventos.seguroborraraviso") + " "+ laFecha
                            + "?");
                    
                    if (opcionElegida == JOptionPane.YES_OPTION)
                    {
                        String idAviso = avisosOcultolst.getSelectedValue().toString();
                        
                        gestor.eliminarAviso(idAviso, conexion);
                        RefrescarDatos(numeroDeAmbito);
                    }
                }
            }
            /*
             * } else {
             * OpCuadroDialogo.showMessageDialog(this,appcont.getI18nString("SinAcceso")); }
             */
        } catch (Exception ex)
        {
            ex.printStackTrace();
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
        String dato = fecha;
        String arrayDato[] = dato.split("-");
        String dia = arrayDato[2];
        String mes = arrayDato[1];
        String anyo = arrayDato[0];
        String fechaSP = dia + "-" + mes + "-" + anyo;
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
    
    /**
     * En un combo inserta los dominios correspondientes a la periodicidad del
     * aviso
     * 
     * @param idDomain
     *            identificador del dominio del que se van a obtener los
     *            subdominios
     */
    
    public void leerDominios(int idDomain)
    {
        
        try
        {
            conexion = gestor.getDBConnection();
            try
            {
                ResultSet r = null;
                PreparedStatement ps = conexion.prepareStatement("seleccionDominios");
                ps.setInt(1, idDomain);
                ps.setString(2,aplicacion.getString(AppContext.PREFERENCES_LOCALE_KEY));
                if (!ps.execute())
                {
                    
                } else
                {
                    
                    r = ps.getResultSet();
                    
                    while (r.next())
                    {
                        cmbPeriodicidad.addItem(r.getString(2));
                        cmbPeriodicidadOculto.addItem(r.getString(1));
                    }
                    aplicacion.closeConnection(null, ps, null, r);
                }
            } catch (Exception exc)
            {
                exc.printStackTrace();
            }
        } catch (Exception ex)
        {
            
        } finally
        {
            aplicacion.closeConnection(conexion, null, null, null);
        }
    }
    
    private void cmbNumeroAmbito_actionPerformed(ActionEvent e)
    {
        try
        {
            if (!bloquearRefrescar)
            {
                if (cmbNumeroAmbito.getItemCount() != 0)
                {
                    String recojoIdAmbito = gestor.getIdAmbito(cmbNumeroAmbito
                            .getSelectedItem().toString(), conexion);
                    RefrescarDatos(recojoIdAmbito);
                }
            }
        }catch(SQLException e1)
        {
            e1.printStackTrace();
            //Informar al usuario del error
        }
    }
    
    private void cmbPeriodicidad_actionPerformed(ActionEvent e)
    {
        int indice = 0;
        indice = cmbPeriodicidad.getSelectedIndex();
        if (indice!=0 || (indice ==0 && cmbPeriodicidadOculto.getItemAt(indice)!=null)) cmbPeriodicidadOculto.setSelectedIndex(indice);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        geopistaEditor1 = null;
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.ui.wizard.WizardPanel#setWizardContext(com.geopista.ui.wizard.WizardContext)
     */
    
}