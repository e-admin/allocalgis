/**
 * GeopistaImportarInfraPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.infraestructuras;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.help.HelpBroker;
import javax.help.HelpSet;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarInfraPanel extends JPanel implements WizardPanel
{
    private JRadioButton rdbIgnorar = new JRadioButton();

    private JRadioButton rdbMostrar = new JRadioButton();

    private StringBuffer textoEditor = null;

    private JScrollPane scpErrores = new JScrollPane();

    private JOptionPane CuadroDialogo;

    private JLabel lblImagen = new JLabel();

    private JLabel lblTipoFichero = new JLabel();

    private JLabel lblInfraestructura = new JLabel();

    private JComboBox cmbInfraestructura = new JComboBox();

    private JComboBox cmbTipoFichero = new JComboBox();

    private JLabel lblFichero = new JLabel();

    private JLabel lblDatos = new JLabel();

    private JLabel lblSelec = new JLabel();

    private JButton btnAbrir = new JButton();

    private JLabel lblErrores = new JLabel();

    private JEditorPane ediError = new JEditorPane();

    private JTextField txtFichero = new JTextField();

    private JOptionPane OpCuadroDialogo;

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    public int errorFich = 0;

    private GeopistaValidarImportacion valImport = new GeopistaValidarImportacion();

    private Blackboard blackImportar = aplicacion.getBlackboard();

    public boolean acceso;

    public int permisoAcceso = 0;

    private JLabel lblfuente = new JLabel();

    private JLabel lblorganismo = new JLabel();

    private JLabel lblpersona = new JLabel();

    private JTextField txtfuente = new JTextField();

    private JTextField txtorganismo = new JTextField();

    private JTextField txtpersona = new JTextField();

    private boolean continuar;

    private String nombreTabla;

    private String tipoInfraestructura;

    private boolean valorCorrecto;

    private String rutaFichero;

    private int tipoFichero = 0;

    private JSeparator jSeparator2 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();

    private JSeparator jSeparator4 = new JSeparator();

    private JSeparator jSeparator3 = new JSeparator();

    private GeopistaLayer capaImportar = null;

    private WizardContext wizardContext;

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion
            .getString("url.herramientas.gestoreventos"));

    private JLabel lblFecha = new JLabel();

    private JTextField txtfecha = new JTextField();

    private JLabel lbltipo = new JLabel();

    private int returnVal = 0;

    private JFileChooser fc = new JFileChooser();

    private JComboBox jComboBox1 = new JComboBox();

    private SortedMap nucleoValuesMap = null;

    //private JComboBox NucleoBox = new JComboBox();

    private JLabel txtEtiquetaSeleccionarNucleo = null;

    private JLabel txtAvisoNoInsercionNucleo = null;

    private static final int TIPO_INFRAESTRUCTURAS_ABASTECIMIENTO = 0;

    private static final int TIPO_INFRAESTRUCTURAS_SANEAMIENTO = 1;

    //Saneamiento
    private static final int INFRAESTRUCTURA_SANEAMIENTOAUTONOMO = 0;
    private static final int INFRAESTRUCTURA_DEPURADORAS = 1;
    private static final int INFRAESTRUCTURA_ELEMENTOSDESANEAMIENTO = 2;
    private static final int INFRAESTRUCTURA_EMISARIOS = 3;
    private static final int INFRAESTRUCTURA_TRAMOSSANEAMIENTO = 4;
    private static final int INFRAESTRUCTURA_COLECTORES = 5;
    
    
    //Abastecimiento
    private static final int INFRAESTRUCTURA_CAPTACIONES = 0;
    private static final int INFRAESTRUCTURA_CONDUCCIONES = 1;
    private static final int INFRAESTRUCTURA_DEPOSITOS = 2;
    private static final int INFRAESTRUCTURA_PIEZAS = 3;
    private static final int INFRAESTRUCTURA_POTABILIZADORAS = 4;
    private static final int INFRAESTRUCTURA_TRAMOSDEABASTECIMENTO = 5;

    private Connection conexion = null;

    private int idMunicipio = 0;
    

    public GeopistaImportarInfraPanel(String tipoInfraestructura)
        {
        	this.tipoInfraestructura=tipoInfraestructura;
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
                                        
                                        jbInit();
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

    private void jbInit() throws Exception
    {
        blackImportar.put("infraestructurasEditor", geopistaEditor1);
        setName(aplicacion.getI18nString("importar.infraestructura.titulo.1"));
        try
        {
            idMunicipio = Integer
                    .parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("idMunicipioNoValido"));
            wizardContext.cancelWizard();
            return;
        }
        try

        {
            String helpHS = "ayuda.hs";
            HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
            HelpBroker hb = hs.createHelpBroker();
            hb.enableHelpKey(this, "infraestructurasImportar01.htm", hs);
        } catch (Exception excp)
        {
        }
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rdbIgnorar);
        grupo.add(rdbMostrar);
        rdbIgnorar.setText(aplicacion.getI18nString("importar.ignorar.entidad"));
        rdbMostrar.setText(aplicacion.getI18nString("importar.mostrar.entidad"));
        rdbMostrar.setBounds(new java.awt.Rectangle(135, 268, 253, 20));
        rdbIgnorar.setBounds(new java.awt.Rectangle(431, 268, 301, 20));
        rdbMostrar.setSelected(true);
        this.jComboBox1.addItem(aplicacion.getI18nString("fichero.jml"));
        this.jComboBox1.addItem(aplicacion.getI18nString("fichero.shape"));

        this.setLayout(null);

        scpErrores.setBounds(new java.awt.Rectangle(135, 324, 595, 171));
        lblInfraestructura.setText(aplicacion
                .getI18nString("GeopistaImportarInfraPanel.nombreInfra"));
        lblInfraestructura.setBounds(new java.awt.Rectangle(430, 190, 95, 20));
        cmbInfraestructura.setBounds(new java.awt.Rectangle(530, 195, 200, 20));

        lblImagen.setIcon(IconLoader.icon("infraestructuras.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        lblFichero.setText(aplicacion
                .getI18nString("GeopistaImportarInfraPanel.FicheroImportar"));
        lblFichero.setBounds(new java.awt.Rectangle(135, 215, 90, 20));

        txtFichero.setBounds(new java.awt.Rectangle(240, 220, 457, 20));

        txtFichero.setEnabled(false);

        btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
        btnAbrir.setBounds(new java.awt.Rectangle(705, 220, 25, 25));
        btnAbrir.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnAbrir_actionPerformed(e);
                }
            });
        cmbTipoFichero.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    cmbTipoFichero_actionPerformed(e);
                }

            });

        lblErrores.setText(aplicacion
                .getI18nString("importar.planeamiento.errores.validacion"));
        lblErrores.setBounds(new java.awt.Rectangle(135, 297, 593, 20));

        ediError.setEditable(false);
        ediError.setContentType("text/html");

        lblTipoFichero.setText(aplicacion
                .getI18nString("importar.identificador.tipo.infraestructura"));
        lblTipoFichero.setBounds(new java.awt.Rectangle(135, 190, 90, 20));
        cmbTipoFichero.setBounds(new java.awt.Rectangle(240, 195, 182, 20));
       
        if (tipoInfraestructura.equals("A")){
            	cmbInfraestructura.removeAllItems();
                cmbInfraestructura.addItem(aplicacion.getI18nString("captaciones"));
                cmbInfraestructura.addItem(aplicacion.getI18nString("conducciones"));
                cmbInfraestructura.addItem(aplicacion.getI18nString("depositos"));
                cmbInfraestructura.addItem(aplicacion.getI18nString("piezas"));
                cmbInfraestructura.addItem(aplicacion.getI18nString("potabilizadoras"));
                cmbInfraestructura.addItem(aplicacion.getI18nString("tramosabastecimiento"));
                cmbTipoFichero.addItem(aplicacion.getI18nString("abastecimiento"));
        }else{
            cmbTipoFichero.addItem(aplicacion.getI18nString("saneamiento"));
            cmbInfraestructura.removeAllItems();
        	cmbInfraestructura.addItem(aplicacion.getI18nString("saneamientoautonomo"));
        	cmbInfraestructura.addItem(aplicacion.getI18nString("depuradoras"));
        	cmbInfraestructura.addItem(aplicacion.getI18nString("elementossaneamiento"));
        	cmbInfraestructura.addItem(aplicacion.getI18nString("emisarios"));
        	cmbInfraestructura.addItem(aplicacion.getI18nString("tramossaneamiento"));
        	cmbInfraestructura.addItem(aplicacion.getI18nString("colectores"));
        }

        txtFichero.setBackground(new Color(200, 200, 200));
        lblfuente.setText(aplicacion.getI18nString("GeopistaImportarInfraPanel.Fuente"));
        lblorganismo.setText(aplicacion
                .getI18nString("GeopistaImportarInfraPanel.Organismo"));
        lblpersona
                .setText(aplicacion.getI18nString("GeopistaImportarInfraPanel.Persona"));

        lblFecha.setText(aplicacion.getI18nString("GeopistaImportarInfraPanel.Fecha"));
        lblFecha.setBounds(new Rectangle(135, 125, 95, 20));
        jComboBox1.setBounds(new Rectangle(550, 130, 180, 20));
        //NucleoBox.setBounds(241, 244, 133, 21);

        txtfecha.setBounds(new Rectangle(240, 130, 145, 20));
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String date = (String) formatter.format(new Date());
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
        lblSelec.setBounds(new Rectangle(135, 165, 260, 20));
        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso1.datos"));
        lblSelec.setText(aplicacion.getI18nString("importar.usuario.paso1.seleccion"));
        txtfecha.setText(date);

        txtfecha.setEnabled(false);
        txtfecha.setBackground(new Color(200, 200, 200));
        lbltipo.setText(aplicacion
                .getI18nString("importar.informacion.referencia.tipo.fichero"));
        lbltipo.setBounds(new Rectangle(430, 125, 120, 20));

        lblpersona.setBounds(new Rectangle(135, 100, 95, 20));
        lblorganismo.setBounds(new Rectangle(135, 75, 95, 20));
        lblfuente.setBounds(new Rectangle(135, 50, 95, 20));
        txtorganismo.setBounds(new Rectangle(240, 80, 490, 20));
        txtfuente.setBounds(new Rectangle(240, 55, 490, 20));
        txtpersona.setBounds(new Rectangle(240, 105, 490, 20));

        jSeparator2.setBounds(new java.awt.Rectangle(134, 291, 605, 2));
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        jSeparator3.setBounds(new Rectangle(135, 160, 605, 2));

        this.setSize(750, 600);
        this.add(cmbTipoFichero, null);
        this.add(lblInfraestructura, null);
        this.add(cmbInfraestructura, null);
        this.add(lblTipoFichero, null);
        this.add(jSeparator2, null);
        this.add(jSeparator5, null);
        this.add(lblDatos, null);
        this.add(lblSelec, null);
        this.add(jSeparator4, null);
        this.add(jSeparator3, null);
        this.add(rdbIgnorar, null);
        this.add(rdbMostrar, null);
        this.add(jComboBox1, null);
        this.add(lbltipo, null);
        this.add(txtfecha, null);
        this.add(lblFecha, null);
        this.add(txtpersona, null);
        this.add(txtorganismo, null);
        this.add(txtfuente, null);
        this.add(lblpersona, null);
        this.add(lblorganismo, null);
        this.add(lblfuente, null);
        this.add(txtFichero, null);
        this.add(lblErrores, null);
        this.add(btnAbrir, null);
        this.add(lblFichero, null);
        this.add(lblImagen, null);
        scpErrores.getViewport().add(ediError, null);
        this.add(scpErrores, null);

        //this.add(NucleoBox, null);
        txtEtiquetaSeleccionarNucleo = new JLabel();
        txtEtiquetaSeleccionarNucleo.setBounds(137, 245, 93, 20);
        this.add(txtEtiquetaSeleccionarNucleo, null);
        txtEtiquetaSeleccionarNucleo.setText(aplicacion.getI18nString("SeleccioneNucleo"));
        txtAvisoNoInsercionNucleo = new JLabel();
        txtAvisoNoInsercionNucleo.setBounds(382, 246, 314, 20);
        this.add(txtAvisoNoInsercionNucleo, null);
        txtAvisoNoInsercionNucleo
                .setText(aplicacion.getI18nString("NoSeleccionaNucleo"));
        
        
	    //NucleoBox.setVisible(false);
	    txtEtiquetaSeleccionarNucleo.setVisible(false);
	    txtAvisoNoInsercionNucleo.setVisible(false);
     	
        
        cmbInfraestructura.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    cmbInfraestructura_actionPerformed(e);
                }

            });

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

        GeopistaPermission geopistaPerm = new GeopistaPermission(
                "Geopista.Infraestructuras.Importar Datos");
        acceso = aplicacion.checkPermission(geopistaPerm, "Infraestructuras");
        if (acceso)
        {
            btnAbrir.setEnabled(true);
        } else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            wizardContext.cancelWizard();
            return;
        }
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        String idNucleo = null;
        blackImportar.put("mostrarError", !rdbIgnorar.isSelected());
        /*if(nucleoValuesMap!=null)
        {
	        Object nucleoSelectItem =  NucleoBox.getSelectedItem();
	        idNucleo = (String) nucleoValuesMap.get(nucleoSelectItem);
	        
        }*/
        //blackImportar.put("idNucleoInfraestucturas",idNucleo);
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

        if (acceso)
        {
            if (continuar)
            {
                return true;
            } else
            {
                return false;
            }
        } else
        {
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

        if (jComboBox1.getSelectedIndex() == 0)
        {
            filter.addExtension("jml");
            filter.setDescription(aplicacion.getI18nString("ficheroJml"));
        } else
        {
            filter.addExtension("shp");
            filter.setDescription(aplicacion.getI18nString("ficheroShape"));
        }
        fc.setFileFilter(filter);
        fc.setFileSelectionMode(0);
        fc.setAcceptAllFileFilterUsed(false);

        File currentDirectory = (File) blackImportar
                .get(Constantes.LAST_IMPORT_DIRECTORY);

        fc.setCurrentDirectory(currentDirectory);

        returnVal = fc.showOpenDialog(this);

        blackImportar.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));

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

                                    if (returnVal == JFileChooser.APPROVE_OPTION)
                                    {
                                        progressDialog
                                                .report(aplicacion
                                                        .getI18nString("GeopistaImportarInfraPanel02.CargandoMapaInfraestructuras"));
                                        String cadenaTexto = "";
                                        String nombreFichero;
                                        rutaFichero = fc.getSelectedFile().getPath();
                                        txtFichero.setText(rutaFichero);
                                        cadenaTexto = "<font face=SansSerif size=3>"
                                                + aplicacion
                                                        .getI18nString("validar.comenzar")
                                                + " <b>" + fc.getSelectedFile().getName()
                                                + "</b>";
                                        cadenaTexto = cadenaTexto
                                                + "<p>"
                                                + aplicacion
                                                        .getI18nString("OperacionMinutos")
                                                + " ...</p></font>";
                                        ediError.setText(cadenaTexto);
                                        cadenaTexto = "";
                                        String rutaMapa = null;
                                        if (tipoInfraestructura.equals("A"))
                                        {
                                            rutaMapa = "url.mapa.infraestructuras.abastecimiento";
                                        } else
                                        {
                                            rutaMapa = "url.mapa.infraestructuras.saneamiento";
                                        }
                                        try
                                        {
                                            geopistaEditor1.loadMap(aplicacion
                                                    .getString(rutaMapa));
                                        } catch (Exception e)
                                        {
                                            JOptionPane
                                                    .showMessageDialog(
                                                            aplicacion.getMainFrame(),
                                                            aplicacion
                                                                    .getI18nString("errorCargaMapaInfraestructuras"));
                                            throw e;
                                        }
                                        try
                                        {
                                            capaImportar = (GeopistaLayer) geopistaEditor1
                                                    .loadData(rutaFichero,
                                                            "Infraestructuras");
                                            capaImportar.setSystemId("sourceImportLayer");
                                        } catch (Exception e)
                                        {
                                            JOptionPane
                                                    .showMessageDialog(
                                                            aplicacion.getMainFrame(),
                                                            aplicacion
                                                                    .getI18nString("errorCargaCapaInfraestructuras"));
                                            throw e;
                                        }

                                        progressDialog.report(aplicacion
                                                .getI18nString("ValidandoDatos"));
                                        capaImportar.setActiva(true);
                                        capaImportar.setVisible(true);
                                        if (jComboBox1.getSelectedIndex() == 0)
                                        {
                                            tipoFichero = 0;
                                            blackImportar.put("tipoF", "jml");
                                        } else
                                        {
                                            tipoFichero = 1;
                                            blackImportar.put("tipoF", "shp");
                                        }
                                        FeatureSchema esquema = capaImportar
                                                .getFeatureCollectionWrapper()
                                                .getFeatureSchema();

                                        textoEditor = new StringBuffer();
                                        if (tipoInfraestructura.equals("A"))
                                        {
                                            // bloque de Abastecimiento
                                            switch (cmbInfraestructura.getSelectedIndex())
                                                {
                                                case INFRAESTRUCTURA_CAPTACIONES:
                                                    continuar = valImport
                                                            .compruebaCaptaciones(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "captaciones";
                                                    break;
                                                case INFRAESTRUCTURA_CONDUCCIONES:
                                                    continuar = valImport
                                                            .compruebaConducciones(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "conducciones";
                                                    break;
                                                case INFRAESTRUCTURA_DEPOSITOS:
                                                    continuar = valImport
                                                            .compruebaDepositos(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "depositos";
                                                    break;
                                                case INFRAESTRUCTURA_PIEZAS:
                                                    continuar = valImport
                                                            .compruebaPiezas(textoEditor,
                                                                    esquema, tipoFichero);

                                                    nombreTabla = "piezas";
                                                    break;
                                                case INFRAESTRUCTURA_POTABILIZADORAS:
                                                    continuar = valImport
                                                            .compruebaPotabilizadoras(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "potabilizadoras";
                                                    break;
                                                case INFRAESTRUCTURA_TRAMOSDEABASTECIMENTO:
                                                    continuar = valImport
                                                            .compruebaTramosAbastecimiento(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "tramosabastecimiento";
                                                    break;
                                                default:
                                                    continuar = false;
                                                }
                                        } else
                                        {
                                            // bloque de Saneamiento
                                            switch (cmbInfraestructura.getSelectedIndex())
                                                {
                                                case INFRAESTRUCTURA_COLECTORES:
                                                    continuar = valImport
                                                            .compruebaColectores(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "colectores";
                                                    break;
                                                case INFRAESTRUCTURA_DEPURADORAS:
                                                    continuar = valImport
                                                            .compruebaDepuradoras(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "depuradoras";
                                                    break;
                                                case INFRAESTRUCTURA_ELEMENTOSDESANEAMIENTO:
                                                    continuar = valImport
                                                            .compruebaElementosSaneamiento(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "elementossaneamiento";
                                                    break;
                                                case INFRAESTRUCTURA_EMISARIOS:
                                                    continuar = valImport
                                                            .compruebaEmisarios(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "emisarios";
                                                    break;
                                                case INFRAESTRUCTURA_TRAMOSSANEAMIENTO:
                                                    continuar = valImport
                                                            .compruebaTramosSaneamiento(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "tramossaneamiento";
                                                    break;
                                                case INFRAESTRUCTURA_SANEAMIENTOAUTONOMO:
                                                    continuar = valImport
                                                            .compruebaSaneamientoAutonomo(
                                                                    textoEditor, esquema,
                                                                    tipoFichero);

                                                    nombreTabla = "saneamientoautonomo";
                                                    break;
                                                default:
                                                    continuar = false;
                                                }
                                        }

                                        cadenaTexto = cadenaTexto
                                                + "<p><font face=SansSerif size=3>"
                                                + aplicacion.getI18nString("validando")
                                                + " <b>"
                                                + aplicacion.getI18nString(nombreTabla)
                                                + "</b></font></p>";
                                        ediError.setText(cadenaTexto);

                                        if ((continuar) && (!nombreTabla.equals("")))
                                        {
                                            cadenaTexto = cadenaTexto
                                                    + aplicacion
                                                            .getI18nString("validar.fin.correcto");

                                            blackImportar.put("nombreTabla", nombreTabla);
                                            blackImportar.put(
                                                    "rutaFicheroImportarInfraPanel",
                                                    rutaFichero);
                                            blackImportar
                                                    .put(
                                                            "numeroRegistros",
                                                            capaImportar
                                                                    .getFeatureCollectionWrapper()
                                                                    .size());
                                        } else
                                        {
                                            cadenaTexto = textoEditor.toString();
                                            cadenaTexto = cadenaTexto
                                                    + aplicacion
                                                            .getI18nString("validar.fin.incorrecto");
                                        }

                                        wizardContext.inputChanged();
                                        cadenaTexto = cadenaTexto
                                                + aplicacion.getI18nString("validar.fin");
                                        ediError.setText(cadenaTexto);
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
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

    }

    private void cmbInfraestructura_actionPerformed(ActionEvent e)
    {
       /* btnAbrir.setEnabled(true);*/
        if (tipoInfraestructura.equals("A"))
        {
            if(cmbInfraestructura.getSelectedIndex() == INFRAESTRUCTURA_PIEZAS ||
               cmbInfraestructura.getSelectedIndex() == INFRAESTRUCTURA_TRAMOSDEABASTECIMENTO)
            {
                loadNucleoData();
                blackImportar.put("totalNucleos",nucleoValuesMap);
                //NucleoBox.setVisible(true);
                //txtEtiquetaSeleccionarNucleo.setVisible(true);
                //txtAvisoNoInsercionNucleo.setVisible(true);
            }
            else
            {
                blackImportar.put("totalNucleos",null);
            }
            /*else
            {
	            NucleoBox.setVisible(false);
	            txtEtiquetaSeleccionarNucleo.setVisible(false);
	            txtAvisoNoInsercionNucleo.setVisible(false);
            }*/
        } else
        {
            if (cmbInfraestructura.getSelectedIndex() == INFRAESTRUCTURA_COLECTORES ||
                cmbInfraestructura.getSelectedIndex() == INFRAESTRUCTURA_TRAMOSSANEAMIENTO ||
                cmbInfraestructura.getSelectedIndex() == INFRAESTRUCTURA_ELEMENTOSDESANEAMIENTO)
            {
                loadNucleoData();
                blackImportar.put("totalNucleos",nucleoValuesMap);
                //NucleoBox.setVisible(true);
                //txtEtiquetaSeleccionarNucleo.setVisible(true);
                //txtAvisoNoInsercionNucleo.setVisible(true);
            }
            else
            {
                blackImportar.put("totalNucleos",null);
            }
                /*else
            }
            {
                NucleoBox.setVisible(false);
                txtEtiquetaSeleccionarNucleo.setVisible(false);
                txtAvisoNoInsercionNucleo.setVisible(false);
            }*/
        }
    }

    private void loadNucleoData()
    {
        if (nucleoValuesMap == null)
        {
            nucleoValuesMap = new TreeMap();
            ResultSet rs = null;
            PreparedStatement ps = null;

            //nucleoValuesMap.put("", null);
            try
            {
                if (conexion == null)
                    conexion = aplicacion.getConnection();

                ps = conexion.prepareStatement("idnucleosdiseminados");

                ps.setInt(1, idMunicipio);

                ps.execute();
                rs = ps.getResultSet();

                while (rs.next())
                {
                    nucleoValuesMap.put(String.valueOf(rs
                            .getString("codigoine")),rs.getString("nombreoficial"));
                }

                //Set nucleoValuesSet = nucleoValuesMap.keySet();
                /*Iterator nucleoValuesIterator = nucleoValuesSet.iterator();
                while (nucleoValuesIterator.hasNext())
                {
                    NucleoBox.addItem(nucleoValuesIterator.next());
                }*/
            } catch (Exception e)
            {
                e.printStackTrace();
                nucleoValuesMap = null;
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("ErrorSacarNucleos"));
                
            } finally
            {
                aplicacion.closeConnection(null, ps, null, rs);
            }
        }
        if(nucleoValuesMap.size()==1)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("NoImportacionFaltanNucleos"));
            btnAbrir.setEnabled(false);
        }
    }

    private void cmbTipoFichero_actionPerformed(ActionEvent e)
    {

        cmbInfraestructura.removeAllItems();
       // if (cmbTipoFichero.getSelectedIndex() == 0)
        if (tipoInfraestructura.equals("A"))
        {
            cmbInfraestructura.addItem(aplicacion.getI18nString("captaciones"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("conducciones"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("depositos"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("piezas"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("potabilizadoras"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("tramosabastecimiento"));
        } else
        {
            cmbInfraestructura.addItem(aplicacion.getI18nString("colectores"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("depuradoras"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("elementossaneamiento"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("emisarios"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("tramossaneamiento"));
            cmbInfraestructura.addItem(aplicacion.getI18nString("saneamientoautonomo"));
        }

    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}
