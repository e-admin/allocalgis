/**
 * GeopistaImportarCallejeroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.geotools.dbffile.DbfFile;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.GeopistaTraduccionTiposVia;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaPermission;
import com.geopista.sql.GEOPISTAConnection;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarCallejeroPanel extends JPanel implements WizardPanel
{
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion
            .getString("fichero.vacio"));

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JScrollPane scpErrores = new JScrollPane();

    private JLabel lblImagen = new JLabel();

    private JLabel lblFichero = new JLabel();

    private JTextField txtFichero = new JTextField();

    private JButton btnAbrir = new JButton();

    private boolean erroresNumerosPolicia = false;

    private javax.swing.JLabel jLabel = null;

    private JLabel lblTipoFichero = new JLabel();

    private JComboBox cmbTipoInfo = new JComboBox();

    private JEditorPane ediError = new JEditorPane();

    private boolean permiso = true;

    private ArrayList lista = null; // Lista de los tipos de Vias.

    private WizardContext wizardContext;

    private JLabel lblCardVia = new JLabel();

    private JTextField txtCardVia = new JTextField();

    private JButton btnAbrir2 = new JButton();

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

    private JSeparator jSeparator2 = new JSeparator();

    private JLabel lblExplicacion1 = new JLabel();

    private JLabel lblExplicacion2 = new JLabel();

    private JCheckBox chkTiposDeVia = new JCheckBox();

    private JCheckBox chkTramosVia = new JCheckBox();

    private JCheckBox chkTramosViaIne = new JCheckBox();

    private JCheckBox chkNumerosPolicia = new JCheckBox();

    private JLabel lblPendiente = new JLabel();

    private JLabel lblCorrecto = new JLabel();

    private JLabel lblErrores = new JLabel();

    private JSeparator jSeparator3 = new JSeparator();

    private JComboBox jComboBox1 = new JComboBox();

    private JSeparator jSeparator4 = new JSeparator();

    private JCheckBox chkViaINE = new JCheckBox();

    private int valor_combo = 1; // Inidica los fichero que se van a verificar

    private JRadioButton rdbIgnorar = new JRadioButton();

    private JRadioButton rdbMostrar = new JRadioButton();

    private GeopistaLayer capaNumerosPolicia = null;

    public static final String FICHERO_VIAS_INE = "ficheroViaIne";

    public static final int TRAMOS_VIAS_INE_LINEA_LONGITUD = 273;

    public static final int VIAS_INE_LINEA_LONGITUD = 108;

    // 0 -- Tipos de Via
    // 1 -- Tramos de Via INE
    // 2 -- Tramos de Via
    // 3 -- Numeros de Policia
    
	//EPSG
	private JLabel lblEPSG = new JLabel();
	private JComboBox jComboEPSG = new JComboBox();


    public GeopistaImportarCallejeroPanel()
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
                                    setName(aplicacion
                                            .getI18nString("importar.asistente.callejero.titulo.1"));
                                    setLayout(null);
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.jml"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.shape"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.dbf"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.texto"));
                                    jComboBox1.addItem(aplicacion
                                            .getI18nString("fichero.otros"));
                                    scpErrores
                                            .setBounds(new Rectangle(135, 345, 595, 165));
                                    
                                    
                                    cmbTipoInfo.addItem(aplicacion.getI18nString("importar.informacion.referencia.tramos.via.ine"));
                                    cmbTipoInfo.setSelectedIndex(0);

                                    btnAbrir.setIcon(IconLoader.icon("abrir.gif"));

                                    lblImagen.setIcon(IconLoader
                                            .icon("inf_referencia.png"));
                                    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                                    lblImagen.setBorder(BorderFactory.createLineBorder(
                                            Color.black, 1));

                                    lblFichero.setText(aplicacion
                                            .getI18nString("seleccione")
                                            + cmbTipoInfo.getItemAt(0));
                                    lblFichero
                                            .setBounds(new Rectangle(130, 250, 210, 20));

                                    txtFichero
                                            .setBounds(new Rectangle(365, 250, 290, 20));
                                    txtFichero.setEditable(false);
                                    txtFichero
                                            .addPropertyChangeListener(new PropertyChangeListener()
                                                {
                                                    public void propertyChange(
                                                            PropertyChangeEvent e)
                                                    {
                                                        txtFichero_propertyChange(e);
                                                    }
                                                });

                                    btnAbrir.setBounds(new Rectangle(660, 250, 25, 20));
                                    btnAbrir.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnAbrir_actionPerformed(e);
                                            }
                                        });

                                    lblTipoFichero
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.tipo.fichero"));
                                    lblTipoFichero.setBounds(new Rectangle(130, 220, 175,
                                            20));

                                    cmbTipoInfo.setBackground(new Color(255, 255, 255));
                                    cmbTipoInfo.setBounds(new Rectangle(365, 220, 320, 20));
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
                                    lblCardVia
                                            .setBounds(new Rectangle(130, 280, 210, 20));
                                    txtCardVia
                                            .setBounds(new Rectangle(365, 280, 290, 20));
                                    txtCardVia.setEnabled(false);
                                    txtCardVia.setEditable(false);
                                    btnAbrir2.setIcon(IconLoader.icon("abrir.gif"));
                                    btnAbrir2.setBounds(new Rectangle(660, 280, 25, 20));
                                    btnAbrir2.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                btnAbrir2_actionPerformed(e);
                                            }
                                        });
                                    
                                    lblTipo.setText("tipo");
                                    lblTipo.setBounds(new Rectangle(320, 120, 100, 20));
                                    lblTipo.setText(aplicacion.getI18nString("tipo.importacion.caja.texto"));
                                    txtFuente.setBounds(new Rectangle(240, 30, 490, 20));
                                    txtOrganismo
                                            .setBounds(new Rectangle(240, 60, 490, 20));
                                    txtPersona.setBounds(new Rectangle(240, 90, 490, 20));
                                   
                                    
                                    txtFecha.setBounds(new Rectangle(240, 120, 75, 20));
                                    txtFecha.setEditable(false);
                                    jSeparator1.setBounds(new Rectangle(135, 145, 600, 5));
                                   
                                    lblFecha.setText("Fecha");
                                    lblFecha.setBounds(new Rectangle(135, 120, 80, 25));
                                    lblFecha.setText(aplicacion.getI18nString("fecha.importacion.caja.texto"));
                                    
                                    lblPersona.setText("Persona");
                                    lblPersona.setBounds(new Rectangle(135, 90, 85, 20));
                                    lblPersona
                                            .setText(aplicacion
                                                    .getI18nString("persona.importacion.caja.texto"));
                                    lblOrganismo.setText("Organismo");
                                    lblOrganismo.setBounds(new Rectangle(135, 60, 90, 20));
                                    lblOrganismo.setText(aplicacion.getI18nString("organismo.importacion.caja.texto"));
                                    lblFuente.setText("Fuente");
                                    lblFuente.setBounds(new Rectangle(135, 30, 95, 20));
                                    lblFuente.setText(aplicacion.getI18nString("fuente.importacion.caja.texto"));
                                    DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
                                    String date = (String) formatter.format(new Date());
                                    jSeparator2.setBounds(new Rectangle(135, 215, 605, 2));
                                    lblExplicacion1.setText(aplicacion.getI18nString("importar.callejero.explicacion1"));
                                    lblExplicacion1.setBounds(new Rectangle(130, 150,510, 20));
                                    lblExplicacion1
                                            .setVerticalAlignment(SwingConstants.TOP);
                                    lblExplicacion1
                                            .setVerticalTextPosition(SwingConstants.TOP);
                                    lblExplicacion1
                                            .setHorizontalTextPosition(SwingConstants.RIGHT);
                                    lblExplicacion1.setAutoscrolls(true);
                                    lblExplicacion2
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.explicacion2"));
                                    lblExplicacion2.setBounds(new java.awt.Rectangle(130,
                                            172, 450, 20));
                                    chkTramosVia
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.tramos.via"));
                                    chkTramosVia.setBounds(new java.awt.Rectangle(316,
                                            196, 95, 15));
                                    chkTramosVia.setForeground(Color.red);
                                    chkTramosVia.setEnabled(false);
                                    chkTramosViaIne
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.tramos.via.ine"));
                                    chkTramosViaIne.setBounds(new java.awt.Rectangle(131,
                                            196, 150, 15));
                                    chkTramosViaIne.setForeground(Color.red);
                                    chkTramosViaIne.setEnabled(true);
                                    chkNumerosPolicia
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.numeros.policia"));

                                    chkViaINE.setForeground(Color.red);
                                    chkViaINE.setEnabled(false);
                                    chkViaINE.setBounds(624, 196, 110, 15);
                                    chkViaINE
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.viasINE"));

                                    chkNumerosPolicia.setBounds(new java.awt.Rectangle(
                                            466, 196, 155, 15));
                                    chkNumerosPolicia.setForeground(Color.red);
                                    chkNumerosPolicia.setEnabled(false);
                                    lblPendiente
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.pendiente.validar"));
                                    lblPendiente.setBounds(new Rectangle(600, 150, 135,
                                            20));
                                    lblPendiente.setForeground(Color.red);
                                    lblPendiente
                                            .setHorizontalAlignment(SwingConstants.RIGHT);
                                    lblCorrecto
                                            .setText(aplicacion
                                                    .getI18nString("importar.callejero.validado"));
                                    lblCorrecto
                                            .setBounds(new Rectangle(585, 175, 150, 15));
                                    lblCorrecto.setForeground(Color.blue);
                                    lblCorrecto
                                            .setHorizontalAlignment(SwingConstants.RIGHT);
                                    lblErrores
                                            .setText(aplicacion
                                                    .getI18nString("importar.informacion.referencia.errores.validacion"));
                                    lblErrores
                                            .setBounds(new Rectangle(135, 320, 265, 20));
                                    jSeparator3
                                            .setBounds(new Rectangle(130, 320, 605, 2));
                                    jComboBox1
                                            .setBounds(new Rectangle(355, 120, 100, 20));
                                    
                                    jSeparator4.setBounds(new Rectangle(135, 20, 595, 5));
                                    txtFecha.setText(date);

                                    ButtonGroup grupo = new ButtonGroup();
                                    grupo.add(rdbIgnorar);
                                    grupo.add(rdbMostrar);
                                    rdbIgnorar.setText(aplicacion
                                            .getI18nString("importar.ignorar.entidad"));
                                    rdbMostrar.setText(aplicacion
                                            .getI18nString("importar.mostrar.entidad"));
                                    rdbMostrar.setBounds(new java.awt.Rectangle(135, 300,
                                            253, 20));
                                    rdbIgnorar.setBounds(new java.awt.Rectangle(431, 300,
                                            301, 20));
                                    rdbMostrar.setSelected(true);
                                    
                                    
        							//EPSG
        							lblEPSG.setText(aplicacion.getI18nString("GeopistaSeleccionarFicheroParcelas.SistemaCoordenadas"));
        							lblEPSG.setBounds(new java.awt.Rectangle(460, 120, 135, 20));
        							jComboEPSG.setBounds(new java.awt.Rectangle(600, 120, 135, 20));							
        							jComboEPSG.setModel(new DefaultComboBoxModel(
        					                    new Vector(CoordinateSystemRegistry.instance(blackboard).getCoordinateSystems())));


        							

                                    setSize(750, 600);
                                    add(jSeparator4, null);
                                    add(jComboBox1, null);
                                    add(jSeparator3, null);
                                    add(lblErrores, null);
                                    add(lblCorrecto, null);
                                    add(lblPendiente, null);
                                    add(chkNumerosPolicia, null);
                                    add(chkTramosViaIne, null);
                                    add(chkTramosVia, null);
                                    add(lblExplicacion2, null);
                                    add(lblExplicacion1, null);
                                    add(jSeparator2, null);
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
                                    add(btnAbrir2, null);
                                    add(txtCardVia, null);
                                    add(lblCardVia, null);
                                    add(cmbTipoInfo, null);
                                    add(lblTipoFichero, null);
                                    add(btnAbrir, null);
                                    add(txtFichero, null);
                                    add(lblFichero, null);
                                    add(lblImagen, null);
                                    scpErrores.getViewport().add(ediError, null);
                                    add(scpErrores, null);
                                    add(rdbIgnorar, null);
                                    add(rdbMostrar, null);
                                    setVisible(true);
                                    lblCardVia.setVisible(false);
                                    txtCardVia.setVisible(false);
                                    btnAbrir2.setVisible(false);
                                    
                                  //EPSG
        							add(lblEPSG,null);
        							add(jComboEPSG,null);

                                    add(chkViaINE, null);
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
        geopistaEditor1 = null;
        capaNumerosPolicia = null;
        blackboard.put("mostrarError", rdbMostrar.isSelected());
        
    	//EPSG
		blackboard.put("selectedImportEPSG", jComboEPSG.getSelectedItem());

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
            setVisible(false);
            return false;
        }
        return valor_combo == 5;
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
        final JFileChooser fc = new JFileChooser();
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        // 1 Es el Tramos de Via
        // 3 Números de Policía
        if ((valor_combo == 2) || (valor_combo == 3))
        {
            filter.addExtension("SHP");
            filter.setDescription("Shapefiles");
        } else
        {
            filter.addExtension("txt");
            filter.setDescription(aplicacion.getI18nString("importar.informacion.referencia.fichero.texto"));
        }

        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL
        // FILES(*.*)
        File currentDirectory = (File) blackboard
                .get(com.geopista.plugin.Constantes.LAST_IMPORT_DIRECTORY);
        fc.setCurrentDirectory(currentDirectory);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        blackboard.put(com.geopista.plugin.Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());

        // Evaluamos los valores posibles de los combos (1..4)
        if (valor_combo == 2)
        {
            // Tramos Via
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                btnAbrir2.setEnabled(true);
                txtFichero.setText(fc.getSelectedFile().getPath());

            }
        } else
        {
            // Vendrian los valores 1,2,3 de texto, shape, texto.
            if (valor_combo == 1)
            {
                mensajeInicioValidacion(fc.getSelectedFile().getName());
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
                                            // Es el valor de tramos via ine

                                            txtFichero.setText(fc.getSelectedFile().getPath());
                                            // Validar el fichero

                                            // La verificación que todos las
                                            // lineas sean de tamaño 273
                                            if (ficheroTextoIne(txtFichero.getText()))
                                            {
                                                blackboard.put("ficheroTextoTramosViaIne",txtFichero.getText());

                                                valor_combo = 2;
                                                cmbTipoInfo
                                                        .insertItemAt(
                                                                aplicacion.getI18nString("importar.informacion.referencia.tramos.via"), 0);
                                                cmbTipoInfo.removeItemAt(1);
                                                chkTramosViaIne.setForeground(Color.BLUE);
                                                chkTramosViaIne.setSelected(true);
                                                chkTramosVia.setEnabled(true);
                                                showMessage(aplicacion.getI18nString("importar.informe.tramos.via.ine.fichero.correcto"));
                                            } else
                                            {
                                                showMessage(aplicacion.getI18nString("importar.informacion.ficheros.no.correctos"));

                                            }

                                            wizardContext.inputChanged();

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

            } 
            else
            {
                mensajeInicioValidacion(fc.getSelectedFile().getName());
                if (valor_combo == 3)
                {
                    // Valor Combo 2. Numero de Policia
                    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
                            aplicacion.getMainFrame(), geopistaEditor1.getContext()
                                    .getErrorHandler());

                    progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
                    progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
                    progressDialog.addComponentListener(new ComponentAdapter()
                        {
                            public void componentShown(ComponentEvent e)
                            {

                                // Wait for the dialog to appear before starting
                                // the
                                // task. Otherwise
                                // the task might possibly finish before the
                                // dialog
                                // appeared and the
                                // dialog would never close. [Jon Aquino]
                                new Thread(new Runnable()
                                    {
                                        public void run()
                                        {

                                            try
                                            {

                                                txtFichero.setText(fc.getSelectedFile().getPath());
                                                // La verificación que todos las
                                                // lineas sean de tamaño
                                                // 273
                                                // Cargamos el fichero de los
                                                // numeros de policia
                                                try
                                                {
                                                    capaNumerosPolicia = (GeopistaLayer) geopistaEditor1
                                                            .loadData(txtFichero.getText(),
                                                                    aplicacion.getI18nString("importar.informacion.referencia.numeros.de.policia"));
                                                    capaNumerosPolicia.setActiva(false);
                                                    capaNumerosPolicia.addStyle(new BasicStyle(new Color(64, 64, 64)));
                                                    capaNumerosPolicia.setVisible(false);

                                                    // Comprobamos que existen
                                                    // los campos a buscar
                                                    FeatureSchema esquema = capaNumerosPolicia
                                                            .getFeatureCollectionWrapper()
                                                            .getFeatureSchema();
                                                    boolean existeCampoGeometry = encontrarCampo(
                                                            "GEOMETRY", esquema);
                                                    boolean existeCampoFecha = encontrarCampo(
                                                            "FECHAALTA", esquema);
                                                    boolean existeCampoRotulo = encontrarCampo(
                                                            "ROTULO", esquema);

                                                    if (existeCampoGeometry
                                                            && existeCampoFecha
                                                            && existeCampoRotulo)
                                                    {
                                                        // Como existen los
                                                        // campos a verificar
                                                        // que los
                                                        // valores de cada
                                                        // columna sean
                                                        // correctos.
                                                        List listaLayer = capaNumerosPolicia
                                                                .getFeatureCollectionWrapper()
                                                                .getFeatures();
                                                        Iterator itLayer = listaLayer
                                                                .iterator();
                                                        boolean errorNumerosPolicia = false;
                                                        while (itLayer.hasNext())
                                                        {
                                                            Feature f = (Feature) itLayer
                                                                    .next();
                                                            // Otro try para
                                                            // comprobar la
                                                            // fecha de Alta
                                                            try
                                                            {
                                                                DateFormat formatter1 = new SimpleDateFormat(
                                                                        "yyyyMMdd");
                                                                int fecha = 0;
                                                                fecha = Integer
                                                                        .parseInt(f
                                                                                .getString("FECHAALTA"));
                                                                Date date1 = (Date) formatter1.parse(String.valueOf(fecha));

                                                            } catch (Exception exFecha)
                                                            {
                                                                errorNumerosPolicia = true;
                                                                showMessage(aplicacion
                                                                        .getI18nString("importar.informe.campo.fechaalta.error"));
                                                                break;

                                                            }

                                                        } // Del while

                                                        if (!errorNumerosPolicia)
                                                        {
                                                            showMessage(aplicacion
                                                                    .getI18nString("importar.informe.numeros.policia.fichero.correcto"));
                                                            valor_combo = 4;
                                                            chkNumerosPolicia
                                                                    .setSelected(true);
                                                            chkNumerosPolicia
                                                                    .setForeground(Color.BLUE);
                                                            chkViaINE.setEnabled(true);
                                                            blackboard
                                                                    .put(
                                                                            "ficheroNumerosPolicia",
                                                                            txtFichero
                                                                                    .getText());
                                                            cmbTipoInfo
                                                                    .insertItemAt(
                                                                            aplicacion
                                                                                    .getI18nString("importar.informacion.referencia.viasINE"),
                                                                            0);
                                                            cmbTipoInfo.removeItemAt(1);
                                                            showMessage(aplicacion
                                                                    .getI18nString("importar.informe.numeros.policia.fichero.correcto"));

                                                        } else
                                                        {
                                                            showMessage(aplicacion
                                                                    .getI18nString("importar.informacion.ficheros.no.correctos"));
                                                        }

                                                    } else
                                                    {
                                                        showMessage(aplicacion
                                                                .getI18nString("importar.informe.campos.fichero.p1")
                                                                + txtFichero.getText()
                                                                + aplicacion
                                                                        .getI18nString("importar.informe.campos.ficheor.p2"));
                                                    }
                                                } catch (Exception ex)
                                                {
                                                    ex.printStackTrace();
                                                }
                                            } catch (Exception e)
                                            {

                                            } finally
                                            {
                                                progressDialog.setVisible(false);
                                                capaNumerosPolicia = null;

                                            }
                                        }
                                    }).start();
                            }
                        });
                    GUIUtil.centreOnWindow(progressDialog);
                    progressDialog.setVisible(true);

                } else

                if (valor_combo == 4)
                {
                    mensajeInicioValidacion(fc.getSelectedFile().getName());
                    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
                            aplicacion.getMainFrame(), geopistaEditor1.getContext()
                                    .getErrorHandler());

                    progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
                    progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
                    progressDialog.addComponentListener(new ComponentAdapter()
                        {
                            public void componentShown(ComponentEvent e)
                            {

                                // Wait for the dialog to appear before starting
                                // the
                                // task. Otherwise
                                // the task might possibly finish before the
                                // dialog
                                // appeared and the
                                // dialog would never close. [Jon Aquino]
                                new Thread(new Runnable()
                                    {
                                        public void run()
                                        {

                                            try
                                            {
                                                if (validarFicheroViasINE(fc.getSelectedFile().getAbsolutePath()))
                                                {
                                                    blackboard.put(FICHERO_VIAS_INE, fc.getSelectedFile().getAbsolutePath());
                                                    valor_combo = 5;
                                                    cmbTipoInfo.setEnabled(false);
                                                    chkViaINE.setSelected(true);
                                                    chkViaINE.setForeground(Color.BLUE);
                                                    showMessage(aplicacion.getI18nString("importar.informe.vias.ine.fichero.correcto"));

                                                    wizardContext.inputChanged();
                                                } else
                                                {
                                                    showMessage(aplicacion.getI18nString("importar.informacion.ficheros.no.correctos"));
                                                }
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
                }

            }

        }

    }

    private boolean validarFicheroViasINE(String ruta)
    {
        boolean resultado = true;
        try
        {

            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                if (str.length() != VIAS_INE_LINEA_LONGITUD)
                {
                    resultado = false;
                    break;
                }
            }
            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return resultado;
        }

        return resultado;
    }

    private void mensajeInicioValidacion(String fileName)
    {
        String cadenaTexto;
        cadenaTexto = "<font face=SansSerif size=3>"
                + aplicacion.getI18nString("ImportacionComenzar") + "<b>" + " "
                + fileName + "</b>";
        cadenaTexto = cadenaTexto + "<p>" + aplicacion.getI18nString("OperacionMinutos")
                + " ...</p></font>";
        ediError.setText(cadenaTexto);
    }

    private void showMessage(String errorMessage)
    {

        ediError.setText(errorMessage);
    }

    private void txtFichero_propertyChange(PropertyChangeEvent e)
    {
    }

    /**
     * Busca un campo en la lista de campos, para hacer la importación del
     * Catastro.
     * 
     * @param String
     *            nombreCampo, nombre del campo
     * @param FeatureSchema
     *            esquema, Shapefile, jml donde se busca el campo
     * @return boolean , true si lo encuentra false en caso contrario
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

    private void cmbTipoInfo_actionPerformed(ActionEvent e)
    {

        // Poner a actualizar o no
        try
        {
            lblFichero.setText(aplicacion.getI18nString("seleccione")
                    + cmbTipoInfo.getItemAt(0));
            String tema = (String) blackboard.get("tipoImportarTramos");
            if (tema.equals(cmbTipoInfo.getSelectedItem()))
            {
                blackboard.put("actualizarPaneles", "N");
            } else
            {
                blackboard.put("actualizarPaneles", "S");
            }
        } catch (Exception ed)
        {
            blackboard.put("actualizarPaneles", "S");
        }
        // Comprobar si debe aparecer el segundo fichero a cargar
        if (cmbTipoInfo.getSelectedItem().equals(
                aplicacion.getI18nString("importar.informacion.referencia.tramos.via")))
        {
            txtCardVia.setText(null);
            txtFichero.setText(null);
            lblCardVia.setVisible(true);
            txtCardVia.setVisible(true);
            lblCardVia.setText(aplicacion.getI18nString("seleccione.dbf.vias"));
            btnAbrir2.setVisible(true);
            btnAbrir2.setEnabled(false);
            txtCardVia.setText(null);

        } else
        {
            txtCardVia.setText(null);
            this.txtFichero.setText(null);
            this.ediError.setText(null);
            lblCardVia.setVisible(false);
            txtCardVia.setVisible(false);
            txtCardVia.setEnabled(false);
            btnAbrir2.setVisible(false);
            btnAbrir2.setEnabled(false);
            txtCardVia.setText(null);
        }

        // Iniciamos la ayuda
        try
        {
            String helpHS = "ayuda.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            if (cmbTipoInfo.getSelectedItem().equals(
                    aplicacion
                            .getI18nString("importar.informacion.referencia.tramos.via")))
            {
                // tramos de via
                hb.enableHelpKey(this,
                        "InformacionReferenciaImportarTramosViaSeleccionFichero", hs);
            } else
            {
                if (cmbTipoInfo
                        .getSelectedItem()
                        .equals(
                                aplicacion
                                        .getI18nString("importar.informacion.referencia.tramos.via.ine")))
                {
                    // TRAMOS VIA INE
                    hb.enableHelpKey(this,
                            "InformacionReferenciaImportarTramosViaIneSeleccionFichero",
                            hs);
                } else
                {
                    if (cmbTipoInfo
                            .getSelectedItem()
                            .equals(
                                    aplicacion.getI18nString("importar.informacion.referencia.numeros.de.policia")))
                    {
                        // Números de policia
                        hb.enableHelpKey(this,
                                "InformacionReferenciaNumeroPoliciaSeleccionFichero", hs);
                    } else
                    {
                        if (cmbTipoInfo
                                .getSelectedItem()
                                .equals(
                                        aplicacion.getI18nString("importar.informacion.referencia.tipos.via.ine")))
                        {
                            // Tipos Via Ine
                            hb.enableHelpKey(this,
                                    "InformacionReferenciaTiposViaIneSeleccionFichero",
                                    hs);
                        } else
                        {
                            if (cmbTipoInfo
                                    .getSelectedItem()
                                    .equals(
                                            aplicacion.getI18nString("importar.informacion.referencia.distritos.censales")))
                            {
                                // Distritos Censales
                                hb
                                        .enableHelpKey(
                                                this,
                                                "InformacionReferenciaDistritosCensalesSeleccionFichero",
                                                hs);
                            } else
                            {
                                if (cmbTipoInfo
                                        .getSelectedItem()
                                        .equals(
                                                aplicacion.getI18nString("importar.informacion.referencia.secciones.censales")))
                                {
                                    // Secciones Censales
                                    hb
                                            .enableHelpKey(
                                                    this,
                                                    "InformacionReferenciaSeccionesCensalesSeleccionFichero",
                                                    hs);
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

                        }
                    }
                }
            }
        } catch (Exception excp)
        {
        }
        // fin de la ayuda

    }

    private void btnAbrir2_actionPerformed(ActionEvent e)
    {

        final JFileChooser fc = new JFileChooser();
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();

        filter.addExtension("dbf");
        filter.setDescription("dBase");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL
        // FILES(*.*)
        File currentDirectory = (File) blackboard
                .get(com.geopista.plugin.Constantes.LAST_IMPORT_DIRECTORY);
        fc.setCurrentDirectory(currentDirectory);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;

        blackboard.put(com.geopista.plugin.Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());

        // Evaluamos los valores posibles de los combos (1..4)

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
                                    boolean existeCampoShape = false;
                                    boolean existeCampoVia = false;
                                    boolean existeCampoDenomina = false;
                                    boolean existeCampoFechaAlta = false;
                                    boolean existeCampoMinVia = false;
                                    String cadenaTexto = null;
                                    txtCardVia.setText(fc.getSelectedFile().getPath());
                                    // Una vez cargados los dos ficheros se
                                    // validarán.
                                    GeopistaLayer capaTramosVia = null;
                                    // Cargamos el layer de Ejes
                                    try
                                    {
                                        capaTramosVia = (GeopistaLayer) geopistaEditor1
                                                .loadData(
                                                        txtFichero.getText(),
                                                        aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));
                                        capaTramosVia.setActiva(false);
                                        capaTramosVia.addStyle(new BasicStyle(new Color(64, 64, 64)));
                                        capaTramosVia.setVisible(false);

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
                                        
                                        // Verificar que existe el campo de via
                                        // y el campo Shape.
                                        // Para el fichero Ejes.shp
                                        FeatureSchema esquema = capaTramosVia
                                                .getFeatureCollectionWrapper()
                                                .getFeatureSchema();
                                        existeCampoShape = encontrarCampo("GEOMETRY",
                                                esquema);
                                        existeCampoVia = encontrarCampo("VIA", esquema);
                                        // Verificar que existe el campo
                                        // Denomina y FechaAlta en el DBF
                                        existeCampoDenomina = encontrarCampoDBF(
                                                "DENOMINA", txtCardVia.getText());
                                        existeCampoFechaAlta = encontrarCampoDBF(
                                                "FECHAALTA", txtCardVia.getText());

                                        if (existeCampoShape && existeCampoVia)
                                        {
                                            // Empezamos la segunda validación
                                            // del dbf
                                            if (existeCampoDenomina
                                                    && existeCampoFechaAlta)
                                            {
                                                // Empiezo con la validación de
                                                // los registros, no nulos
                                                // y de cada tipo
                                                // Del fichero Shp de ejes que
                                                // el campo via sea no nulo
                                                // y numerico
                                                List listaLayer = capaTramosVia
                                                        .getFeatureCollectionWrapper()
                                                        .getFeatures();
                                                Iterator itLayer = listaLayer.iterator();
                                                boolean errorEjes = false;
                                                while (itLayer.hasNext())
                                                {
                                                    Feature f = (Feature) itLayer.next();
                                                    try
                                                    {
                                                        int via = f.getInteger(3); // Via
                                                    } catch (Exception exc)
                                                    {
                                                        exc.printStackTrace();
                                                        // Sacar mensaje de
                                                        // error.
                                                        errorEjes = true;
                                                        break;
                                                    }

                                                }

                                                if (errorEjes)
                                                {
                                                    // Sacar el mensaje de Error
                                                    cadenaTexto = cadenaTexto
                                                            + aplicacion
                                                                    .getI18nString("importar.informe.campos.via.p1")
                                                            + txtFichero.getText()
                                                            + aplicacion
                                                                    .getI18nString("importar.informe.campos.via.p2");
                                                } else
                                                {
                                                    // Si en ejes no hay error
                                                    // ahora hay que procesar
                                                    // los datos del fichero
                                                    // CardVia.
                                                    if (comprobarValoresCardVia(txtCardVia
                                                            .getText()))
                                                    {
                                                        // Correcto se procederá
                                                        // al siguiente paso
                                                        // Indicar que ha sido
                                                        // correcta la
                                                        // verificación
                                                        // y pasar al siguiente
                                                        // paso.
                                                        // cadenaTexto =
                                                        // cadenaTexto +
                                                        // aplicacion.getI18nString("importar.informe.campos.pasaron.validaciones");
                                                        cadenaTexto = cadenaTexto
                                                                + aplicacion.getI18nString("importar.informe.tramos.via.fichero.correcto");
                                                        // pasamos al blackboard
                                                        // el fichero .shp de
                                                        // ejes
                                                        blackboard.put("rutaFicheroEjes",
                                                                txtFichero.getText());
                                                        blackboard.put(
                                                                "rutaFicheroCarVia",
                                                                txtCardVia.getText());
                                                        valor_combo = 3;
                                                        cmbTipoInfo
                                                                .insertItemAt(
                                                                        aplicacion.getI18nString("importar.informacion.referencia.numeros.de.policia"),
                                                                        0);
                                                        cmbTipoInfo.removeItemAt(1);
                                                        chkTramosVia.setSelected(true);
                                                        chkTramosVia
                                                                .setForeground(Color.BLUE);
                                                        chkNumerosPolicia
                                                                .setEnabled(true);
                                                    } else
                                                    {
                                                        // Valores Incorrectos
                                                        // en la fecha, no se
                                                        // cargarán.
                                                        cadenaTexto = cadenaTexto
                                                                + aplicacion
                                                                        .getI18nString("importar.informe.fichero.fecha.p1")
                                                                + txtCardVia.getText()
                                                                + aplicacion
                                                                        .getI18nString("importar.informe.fichero.fecha.p2");
                                                    }

                                                }

                                            } else
                                            {
                                                // Sacar mensaje que el ficheo
                                                // de vias es incorrecto
                                                cadenaTexto = cadenaTexto
                                                        + aplicacion
                                                                .getI18nString("importar.informe.fichero.fecha.p1")
                                                        + txtCardVia.getText()
                                                        + aplicacion
                                                                .getI18nString("importar.informe.fichero.p3");
                                            }
                                        } else
                                        {
                                            // Sacaremos el mensaje que el
                                            // fichero guia no es correcto.
                                            cadenaTexto = cadenaTexto
                                                    + aplicacion
                                                            .getI18nString("importar.informe.fichero.fecha.p1")
                                                    + txtFichero.getText()
                                                    + aplicacion
                                                            .getI18nString("importar.informe.fichero.p3");

                                        }
                                        ediError.setText(cadenaTexto);
                                    } catch (Exception exp)
                                    {
                                        exp.printStackTrace();
                                    } finally
                                    {
                                        capaTramosVia = null;
                                    }

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

        wizardContext.inputChanged();

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

    /**
     * Comprobar que la fecha de alta sea correcta
     * 
     * @param String
     *            rutaFichero, ruta del fichero donde se verificarán los campos
     *            fecha
     * @return boolean true si las fechas son correctas, false en caso contrario
     */

    public boolean comprobarValoresCardVia(String rutaFichero)
    {
        boolean encontrado = true;

        try
        {
            DbfFile leerDbf = new DbfFile(rutaFichero);
            for (int k = 0; k < leerDbf.getLastRec(); k++)
            {
                StringBuffer valores = leerDbf.GetDbfRec(k);
                try
                {
                    DateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                    int fecha = 0;
                    if (leerDbf.ParseRecordColumn(valores, 4) instanceof Integer)
                    {
                        fecha = ((Integer) leerDbf.ParseRecordColumn(valores, 4)).intValue();
                    } else
                    {
                        fecha = ((Double) leerDbf.ParseRecordColumn(valores, 4)).intValue();
                    }
                    Date date1 = (Date) formatter1.parse(String.valueOf(fecha));
                } catch (Exception parseEx)
                {
                    parseEx.printStackTrace();
                    encontrado = false;
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

    /**
     * Método para determinar si las filas del fichero de texto, cumplen las
     * validaciones Longitud de 35 caracteres Los cinco primeros caracteres
     * estén en dominio tipo de via Normalizado Los cinco segundos caracteres
     * estén en domnio tipo de via
     */

    public ArrayList ficheroTiposVia(String ruta)
    {

        String str = null;
        String tipoViaTraducido = null;
        String tipoViaNormalizado = null;
        String tipoVia = null;
        String descripcion = null;
        String tipoViaNormalizadoTraducido = null;
        ArrayList lista = new ArrayList();
        ArrayList listaVacia = new ArrayList();

        boolean existeViaNormalizada = false;
        boolean existeVia = false;
        boolean resultado = false;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            while ((str = reader.readLine()) != null)
            {
                if (str.length() != 35)
                {
                    resultado = false;
                    break;
                } else
                {
                    // Obtener los dos tipos de vias
                    tipoViaNormalizado = str.substring(0, 5);
                    tipoVia = str.substring(5, 10);
                    descripcion = str.substring(10, 35);
                    tipoViaNormalizadoTraducido = estaEnDominio(
                            "Tipos de via normalizados de Catastro", tipoViaNormalizado, "es_ES");

                    if (tipoViaNormalizadoTraducido == null)
                    {
                        // Error y no se hace más
                        resultado = false;
                        break;
                    } else
                    {
                        // Comprobamos el valor del tipo de via, que exista
                        // dominio
                        tipoViaTraducido = estaEnDominio(
                                "Tipos de via normalizados de Catastro", tipoVia, "es_ES");
                        if (tipoViaTraducido != null)
                        {
                            // Son correctos luego se crea un array de datos
                            // y se pasaría el valor traducido de los dos campos
                            // y la descripcion
                            GeopistaTraduccionTiposVia temporal = new GeopistaTraduccionTiposVia();
                            temporal.setDescripcion(descripcion);
                            temporal.setTipoVia(tipoViaTraducido);
                            temporal.setTipoViaNormalizado(tipoViaNormalizadoTraducido);
                            lista.add(temporal);
                            resultado = true;
                        } else
                        {
                            // Error y no se presenta más
                            resultado = false;
                            break;
                        }
                    }
                }
            }

        } catch (Exception e)
        {
            resultado = false;
            e.printStackTrace();

        }
        if (resultado)
        {
            return lista;
        } else
        {
            return listaVacia;
        }
    }

    /**
     * Método para determinar si el fichero de texto de via Ine es correcto
     * 
     * @param String
     *            ruta, ruta del fichero a comprobar que sea correcto
     * @return boolean true si es correcto, false en caso contrario
     */

    public boolean ficheroTextoIne(String ruta)
    {
        boolean resultado = true;
        try
        {

            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                if (str.length() != TRAMOS_VIAS_INE_LINEA_LONGITUD)
                {
                    resultado = false;
                    break;
                }
            }
            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return resultado;
        }

        return resultado;
    }

    /**
     * Método que devuelve verdadero si existe el codigo de traducción para un
     * valor determinado en un dominio determinado.
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
    public String estaEnDominio(String nombreDominio, String descripcionValor,
            String localidad)
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet r = null;
        String resultado = "";
        try
        {
            conn = this.abrirConexion();
            ps = conn.prepareStatement("tieneDominioAsociado");
            ps.setString(1, descripcionValor);
            ps.setString(2, localidad);
            ps.setString(3, nombreDominio);

            if (!ps.execute())
            {
            } else
            {
                r = ps.getResultSet();
            }

            while (r.next())
            {
                resultado = r.getString("pattern");
            }
            return resultado;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            aplicacion.closeConnection(conn, ps, null, r);
        }
    }

    public GEOPISTAConnection abrirConexion() throws SQLException
    {
        GEOPISTAConnection conn = null;
        try
        {
            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());
            String sConn = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_URL);
            conn = (GEOPISTAConnection) DriverManager.getConnection(sConn);
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = (GEOPISTAConnection) app.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e)
        {
            return null;
        }

        return conn;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        geopistaEditor1 = null;
        capaNumerosPolicia = null;
        // TODO Auto-generated method stub

    }

} // de la clase general.

