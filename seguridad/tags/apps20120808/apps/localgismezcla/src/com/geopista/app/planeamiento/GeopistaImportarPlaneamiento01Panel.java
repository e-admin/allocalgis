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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
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
import java.io.File;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.app.inicio.GeopistaInicio;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarPlaneamiento01Panel extends JPanel implements WizardPanel
{

    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Connection conexion = null;

    private String campo;

    private String dominio;

    private int existe;

    private int numRegNoExisten;

    private String rutaFichero;

    

    private int idDominio;

    private String nombreCampo;

    private JPanel pnlVentana = new JPanel();

    private JLabel lblFichero = new JLabel();

    private JTextField txtFichero = new JTextField();

    private JButton btnAbrir = new JButton();

    private JLabel lblErrores = new JLabel();

    private JScrollPane scpErrores = new JScrollPane();

    private JButton btnAnterior = new JButton();

    private JLabel lblImagen = new JLabel();

    private JEditorPane ediError = new JEditorPane();

    public int errorFich = 0;

    public int permisoAcceso = 0;

    private boolean acceso = true;

    private JTextField txtorganismo = new JTextField();

    private JLabel lblorganismo = new JLabel();

    private JTextField txtpersona = new JTextField();

    private JLabel lblpersona = new JLabel();

    private JTextField txtfuente = new JTextField();

    private JLabel lblFecha = new JLabel();

    private JTextField txtfecha = new JTextField();

    private boolean continuar;

    private String nombreTabla;

    private GeopistaValidarImportacion valImport = new GeopistaValidarImportacion();

    private Blackboard blackImportar = aplicacion.getBlackboard();

    private String nombreCampoDominio;

    private JComboBox jComboBox1 = new JComboBox();

    private WizardContext wizardContext;

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor();

    private JSeparator jSeparator2 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();

    private JSeparator jSeparator4 = new JSeparator();

    private JSeparator jSeparator3 = new JSeparator();

    private JLabel lblfuente = new JLabel();

    private JLabel lbltipo = new JLabel();

    private JLabel lblDatos = new JLabel();

    private JLabel lblSelec = new JLabel();

    private JLabel lblTipoFichero = new JLabel();

    private JLabel lblPlaneamiento = new JLabel();

    private JComboBox cmbTipoFichero = new JComboBox();

    private JComboBox cmbPlaneamiento = new JComboBox();

    private JRadioButton rdbIgnorar = new JRadioButton();

    private JRadioButton rdbMostrar = new JRadioButton();

    private JComboBox cmbIdPlanHide = new JComboBox();

    private StringBuffer textoEditor = null;

    private int tipoFichero = 0;

    private int idDomi;

    private String nombreCampoDomi;

    private int idMunicipio = 0;

    private int returnVal = 0;

    private JFileChooser fc = new JFileChooser();

    private GeopistaLayer capaPlaneamiento = null;

    public GeopistaImportarPlaneamiento01Panel()
        {
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

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
        setName(aplicacion.getI18nString("importar.ambitos.planeamiento.titulo.1"));

        try
        {
            idMunicipio = Integer.parseInt(aplicacion.getString("geopista.DefaultCityId"));
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("idMunicipioNoValido"));
            wizardContext.cancelWizard();
            return;
        }

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rdbIgnorar);
        grupo.add(rdbMostrar);
        rdbIgnorar.setText(aplicacion.getI18nString("importar.ignorar.entidad"));
        rdbMostrar.setText(aplicacion.getI18nString("importar.mostrar.entidad"));

        this.jComboBox1.addItem(aplicacion.getI18nString("fichero.jml"));
        this.jComboBox1.addItem(aplicacion.getI18nString("fichero.shape"));
        lblpersona.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento01Panel.Persona"));
        lblorganismo.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento01Panel.Organismo"));

        lblpersona.setBounds(new Rectangle(135, 100, 95, 20));
        lblorganismo.setBounds(new Rectangle(135, 75, 95, 20));

        txtorganismo.setBounds(new Rectangle(240, 80, 490, 20));
        txtfuente.setBounds(new Rectangle(240, 55, 490, 20));
        txtpersona.setBounds(new Rectangle(240, 105, 490, 20));

        blackImportar.put("tipoImport", "municipal");

        try
        {
            String helpHS = "ayuda.hs";
            HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
            HelpBroker hb = hs.createHelpBroker();
            hb.enableHelpKey(this, "planeamientoImportarAmbitosPlaneamiento01", hs);
        } catch (Exception excp)
        {
            // si falla la carga de la ayuda seguimos con el resto del codigo
        }

        this.setLayout(null);

        lblFichero.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento01Panel.FicheroImportar"));
        lblFichero.setBounds(new java.awt.Rectangle(135, 215, 90, 20));

        txtFichero.setBounds(new java.awt.Rectangle(240, 220, 457, 20));

        txtFichero.setEnabled(false);

        jSeparator2.setBounds(new java.awt.Rectangle(135, 270, 605, 2));
        btnAbrir.setBounds(new java.awt.Rectangle(705, 220, 25, 25));
        btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
        btnAbrir.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnAbrir_actionPerformed(e);
                }
            });

        lblErrores.setText(aplicacion.getI18nString("importar.planeamiento.errores.validacion"));
        lblErrores.setBounds(new java.awt.Rectangle(135, 275, 593, 20));

        scpErrores.setBounds(new java.awt.Rectangle(135, 300, 595, 195));

        txtfecha.setBounds(new Rectangle(240, 130, 145, 20));
        txtfecha.setEnabled(false);
        txtfecha.setBackground(new Color(200, 200, 200));
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String date = (String) formatter.format(new Date());
        txtfecha.setText(date);

        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
        lblfuente.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento01Panel.Fuente"));
        lblfuente.setBounds(new Rectangle(135, 50, 95, 20));

        txtFichero.setBackground(new Color(200, 200, 200));
        ediError.setEditable(false);
        ediError.setContentType("text/html");
        lblFecha.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento01Panel.Fuente.Fecha"));
        lblFecha.setBounds(new Rectangle(135, 125, 95, 20));
        jComboBox1.setBounds(new Rectangle(550, 130, 180, 20));
        jSeparator3.setBounds(new Rectangle(135, 160, 605, 2));
        lbltipo.setText(aplicacion.getI18nString("importar.informacion.referencia.tipo.fichero"));
        lbltipo.setBounds(new Rectangle(430, 125, 120, 20));
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
        lblSelec.setBounds(new Rectangle(135, 165, 260, 20));
        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso1.datos"));
        lblSelec.setText(aplicacion.getI18nString("importar.usuario.paso1.seleccion"));
        lblTipoFichero.setText(aplicacion.getI18nString("importar.identificador.tipo"));
        lblPlaneamiento.setText(aplicacion.getI18nString("importar.identificador.planeamiento"));
        lblTipoFichero.setBounds(new java.awt.Rectangle(135, 190, 90, 20));
        cmbTipoFichero.setBounds(new java.awt.Rectangle(240, 195, 182, 20));
        cmbTipoFichero.addItem(aplicacion.getI18nString("tabla_planeamiento"));
        cmbTipoFichero.addItem(aplicacion.getI18nString("ambitos_planeamiento"));
        cmbTipoFichero.addItem(aplicacion.getI18nString("calificacion_suelo"));
        cmbTipoFichero.addItem(aplicacion.getI18nString("clasificacion_suelo"));
        cmbTipoFichero.addItem(aplicacion.getI18nString("sistemas_generales"));

        lblPlaneamiento.setBounds(new java.awt.Rectangle(430, 190, 95, 20));
        cmbPlaneamiento.setBounds(new java.awt.Rectangle(530, 195, 200, 20));
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        rdbMostrar.setBounds(new java.awt.Rectangle(135, 245, 253, 20));
        rdbIgnorar.setBounds(new java.awt.Rectangle(431, 245, 301, 20));
        cmbIdPlanHide.setVisible(false);
        cmbIdPlanHide.removeAllItems();
        cmbPlaneamiento.removeAllItems();

        this.add(rdbIgnorar, null);
        this.add(rdbMostrar, null);
        this.add(jSeparator5, null);
        this.add(lblSelec, null);
        this.add(txtfuente, null);
        this.add(txtorganismo, null);
        this.add(txtpersona, null);
        this.add(lblpersona, null);
        this.add(lblorganismo, null);
        this.add(lblfuente, null);
        this.add(lblFecha, null);
        this.add(txtfecha, null);
        this.add(jComboBox1, null);
        this.add(lbltipo, null);
        this.add(lblDatos, null);
        this.setSize(750, 600);
        this.add(jSeparator2, null);
        this.add(lblImagen, null);
        this.add(jSeparator3, null);
        scpErrores.getViewport().add(ediError, null);
        this.add(lblTipoFichero, null);
        this.add(cmbTipoFichero, null);
        this.add(lblPlaneamiento, null);
        this.add(cmbPlaneamiento, null);

        this.add(scpErrores, null);
        this.add(lblErrores, null);
        this.add(btnAbrir, null);
        this.add(txtFichero, null);
        this.add(lblFichero, null);
        this.add(jSeparator4, null);
        this.add(cmbIdPlanHide, null);
        rdbMostrar.setSelected(true);
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
        GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.Importar");
        acceso = aplicacion.checkPermission(geopistaPerm, "Planeamiento");

        if (acceso)
        {
            btnAbrir.setEnabled(true);
        } else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoPermisos"));
            wizardContext.cancelWizard();
            return;
        }
        try
        {
            if (conexion == null)
            {
                conexion = valImport.getDBConnection();
                blackImportar.put("Conexion",conexion);
            }

            ArrayList recojoIDPLAN = valImport.getIDPlan(idMunicipio, conexion);
            cmbIdPlanHide.setBounds(560, 166, 85, 24);
            Iterator alIt = recojoIDPLAN.iterator();
            while (alIt.hasNext())
            {
                cmbIdPlanHide.addItem(alIt.next().toString());
            }

            ArrayList recojoNombrePLAN = valImport.getNombrePlan(idMunicipio, conexion);

            Iterator alItN = recojoNombrePLAN.iterator();
            while (alItN.hasNext())
            {
                cmbPlaneamiento.addItem(alItN.next().toString());
            }
        } catch (Exception exp)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoConexionBaseDatos"));
            wizardContext.cancelWizard();
            return;
        }
        if (cmbIdPlanHide.getItemCount() != 0)
        {
            cmbIdPlanHide.setSelectedIndex(0);

        }
        if (cmbPlaneamiento.getItemCount() != 0)
        {
            cmbPlaneamiento.setSelectedIndex(0);
        }
        cmbPlaneamiento.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    cmbPlaneamiento_actionPerformed(e);
                }
            });

    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {

        blackImportar.put("mostrarError", !rdbIgnorar.isSelected());

        if (cmbIdPlanHide.getSelectedIndex() != -1)
        {
            blackImportar.put("identificadorPlan", cmbIdPlanHide.getSelectedItem().toString());
        }

        permisoAcceso = 0;
        if (txtFichero.getText().toString().length() == 0)
        {
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("SeleccionFichero"));
            errorFich = 1;
        } else
        {
            errorFich = 0;
        }

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

    private String nextID;

    public void setNextID(String nextID)
    {
        if (numRegNoExisten != 0)
        {
            nextID = "2";
        } else
        {
            nextID = "3";
        }
        this.nextID = nextID;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        if (numRegNoExisten != 0)
        {
            nextID = "2";
        } else
        {
            nextID = "3";
        }
        if (nextID == null)
        {
            if ((errorFich == 1) || (permisoAcceso == 1))
            {
                return null;
            } else
            {
                return "2";
            }

        } else
        {
            return nextID;
        }
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
            blackImportar.put("tipoF", "jml");
        } else
        {
            filter.addExtension("shp");
            filter.setDescription(aplicacion.getI18nString("ficheroShape"));
            blackImportar.put("tipoF", "shp");
        }

        fc.setFileFilter(filter);
        fc.setFileSelectionMode(0);
        fc.setAcceptAllFileFilterUsed(false);

        File currentDirectory = (File) blackImportar.get(GeopistaInicio.LAST_IMPORT_DIRECTORY);

        fc.setCurrentDirectory(currentDirectory);

        returnVal = fc.showOpenDialog(this);

        blackImportar.put(GeopistaInicio.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

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

                                    if (returnVal == JFileChooser.APPROVE_OPTION)
                                    {
                                        String cadenaTexto = "";
                                        textoEditor = new StringBuffer();
                                        String nombreFichero;
                                        int indexAtr = 0;
                                        rutaFichero = fc.getSelectedFile().getPath();
                                        txtFichero.setText(rutaFichero);
                                        cadenaTexto = "<font face=SansSerif size=3>" + aplicacion.getI18nString("validar.comenzar") + " <b>" + fc.getSelectedFile().getName() + "</b>";
                                        cadenaTexto = cadenaTexto + "<p>" + aplicacion.getI18nString("OperacionMinutos") + " ...</p></font>";

                                        try
                                        {
                                            tipoFichero = jComboBox1.getSelectedIndex();

                                            numRegNoExisten = 0;

                                            try
                                            {
                                                capaPlaneamiento = (GeopistaLayer) geopistaEditor1.loadData(rutaFichero, "Planeamiento");
                                                blackImportar.put("CapaPlaneamiento",capaPlaneamiento);
                                            } catch (Exception e)
                                            {
                                                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("errorCargaCapaPlaneamiento"));
                                                throw e;
                                            }
                                            capaPlaneamiento.setActiva(true);
                                            capaPlaneamiento.setVisible(true);

                                            FeatureSchema esquema = capaPlaneamiento.getFeatureCollectionWrapper().getFeatureSchema();
                                            

                                            if ((cmbPlaneamiento.getItemCount() != 0) || (cmbTipoFichero.getSelectedIndex() == 0))
                                            {

                                                switch (cmbTipoFichero.getSelectedIndex())
                                                    {
                                                    case 1:
                                                        continuar = valImport.compruebaAmbitos(textoEditor, esquema, tipoFichero);
                                                        nombreTabla = "ambitos_planeamiento";
                                                        break;
                                                    case 2:
                                                        continuar = valImport.compruebaCalificacion(textoEditor, esquema, tipoFichero);
                                                        nombreTabla = "calificacion_suelo";
                                                        break;
                                                    case 3:
                                                        continuar = valImport.compruebaClasificacion(textoEditor, esquema, tipoFichero);
                                                        nombreTabla = "clasificacion_suelo";
                                                        break;
                                                    case 4:
                                                        continuar = valImport.compruebaSistemasGenerales(textoEditor, esquema, tipoFichero);
                                                        nombreTabla = "sistemas_generales";
                                                        break;
                                                    case 0:
                                                        continuar = valImport.compruebaTablaPlaneamiento(textoEditor, esquema, tipoFichero);
                                                        nombreTabla = "tabla_planeamiento";
                                                        break;
                                                    default:
                                                        continuar = false;
                                                    }

                                                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>" + aplicacion.getI18nString("validando") + " <b>" + aplicacion.getI18nString(nombreTabla)
                                                        + "</b></font></p>";
                                                ediError.setText(cadenaTexto);
                                                cadenaTexto = "";
                                                if (continuar)
                                                {
                                                    cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin.correcto");
                                                    blackImportar.put("rutaFicheroImportar", rutaFichero);
                                                    blackImportar.put("nombreTablaSelec", nombreTabla);

                                                    ArrayList recojoValorDom = valImport.getCampo(nombreTabla, tipoFichero);
                                                    

                                                    if (recojoValorDom!=null)
                                                    {
                                                        idDominio = Integer.parseInt(recojoValorDom.get(0).toString());
                                                        nombreCampo = recojoValorDom.get(1).toString();
                                                        
                                                        indexAtr = esquema.getAttributeIndex(nombreCampo);
                                                        blackImportar.put("indiceCampo", indexAtr);
                                                        blackImportar.put("idDomiBlack", idDominio);
                                                        blackImportar.put("fieldName",nombreCampo);

                                                        ArrayList valoresBD = valImport.leerDominios(idDominio, conexion);
                                                        List coleccion = capaPlaneamiento.getFeatureCollectionWrapper().getFeatures();
                                                        Iterator coleccionIter = coleccion.iterator();

                                                        while (coleccionIter.hasNext())
                                                        {

                                                            existe = 0;
                                                            Feature actualFeature = (Feature) coleccionIter.next();
                                                            campo = actualFeature.getAttribute(indexAtr).toString().trim();

                                                            Iterator alIterator = valoresBD.iterator();
                                                            while (alIterator.hasNext())
                                                            {
                                                                dominio = alIterator.next().toString().trim();
                                                                if (campo.equals(dominio))
                                                                {

                                                                    existe = existe + 1;
                                                                    break;
                                                                }
                                                            }
                                                            if (existe == 0)
                                                            {
                                                                numRegNoExisten = numRegNoExisten + 1;
                                                            }
                                                        }
                                                    }
                                                } else
                                                {
                                                    cadenaTexto = cadenaTexto + textoEditor;
                                                    cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin.incorrecto");
                                                }
                                            } else
                                            {
                                                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3 color=#ff0000><b>" + aplicacion.getI18nString("ImportarTablaPlaneamiento")
                                                        + "</b></font></p>";
                                                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("ImportarTablaPlaneamiento"));
                                            }
                                        } catch (Exception ex)
                                        {

                                            cadenaTexto = cadenaTexto + aplicacion.getI18nString("errDesconocido") + aplicacion.getI18nString("validar.fin.incorrecto");
                                            continuar = false;
                                            ex.printStackTrace();
                                        }
                                        wizardContext.inputChanged();
                                        cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin");
                                        ediError.setText(cadenaTexto);

                                    }
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("SeHanProducidoErrores"));
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

    private void cmbPlaneamiento_actionPerformed(ActionEvent e)
    {
        cmbIdPlanHide.setSelectedIndex(cmbPlaneamiento.getSelectedIndex());
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

}