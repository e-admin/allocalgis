/**
 * GeopistaImportarParcelas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

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
import java.util.Date;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
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

public class GeopistaImportarParcelas extends JPanel implements WizardPanel
{
    //AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private int idParcela=0;

    private String rutaFichero;
    private String nombreCapa;

    private int existe;

    private int numRegNoExisten; 

    private JPanel plnVentana = new JPanel();

    private JLabel lblFichero = new JLabel();

    private JTextField txtFichero = new JTextField();

    private JButton btnAbrir = new JButton();

    private JLabel lblErrores = new JLabel();

    private JScrollPane scpErrores = new JScrollPane();
    private GeopistaLayer capaImportar = null;
    private JButton btnAnterior = new JButton();

    private JFileChooser fcSelectorFichero = new JFileChooser();

    private JLabel lblImagen = new JLabel();

    private JEditorPane ediError = new JEditorPane();

    private JOptionPane OpCuadroDialogo;

    public int errorFich = 0;

    public int permisoAcceso = 0;

    public boolean acceso=true;

    private boolean continuar;

    private JTextField txtpersona = new JTextField();

    private JLabel lblpersona = new JLabel();

    private JTextField txtorganismo = new JTextField();

    private JLabel lblorganismo = new JLabel();

    private JTextField txtfuente = new JTextField();

    private JLabel lblfuente = new JLabel();

    private String nombreTabla;

    private GeopistaValidarParcelas valImport = new GeopistaValidarParcelas();

    private Connection conexion = null;

    //private WizardContext wizardContext;
    private ApplicationContext aplicacion = AppContext.getApplicationContext();

    private GeopistaEditor geopistaEditor1 = new GeopistaEditor(aplicacion
            .getString("url.herramientas.gestoreventos"));

    private JLabel lbltipo = new JLabel();

    private JTextField txtfecha = new JTextField();

    private JLabel lblFecha = new JLabel();

    

    private Blackboard blackImportar = aplicacion.getBlackboard();

    private String campo;

    private String dominio;
    
    private int tipoFichero = 0;

    private JSeparator jSeparator2 = new JSeparator();

    private JSeparator jSeparator5 = new JSeparator();

    private JLabel lblDatos = new JLabel();

    private JLabel lblSelec = new JLabel();

    private JLabel lblTipoFichero = new JLabel();

    private JComboBox cmbTipoFichero = new JComboBox();

    private JRadioButton rdbIgnorar = new JRadioButton();

    private JRadioButton rdbMostrar = new JRadioButton();

    private JComboBox cmbIdPlanHide = new JComboBox();

    private StringBuffer textoEditor = null;

    private int idMunicipio = 0;

    private int returnVal = 0;

    private JFileChooser fc = new JFileChooser();
    
    private GeopistaLayer capaParcelas = null;

   // private GeopistaImportarParcelasParcelas Validar = new GeopistaImportarParcelasParcelas();

    //private boolean permiso;
    public GeopistaImportarParcelas()
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

    					                jbInit();
    					            } catch (Exception e)
    					            {
    					                e.printStackTrace();
    					            }finally
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
        setName(aplicacion
                .getI18nString("importar.asistente.parcelas.titulo1"));
        this.setLayout(null);

        lblImagen.setIcon(IconLoader.icon("catastro.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        ediError.setText("jEditorPane1");
        ediError.setContentType("text/html");
        ediError.setEditable(false);
        /*DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String date = (String) formatter.format(new Date());*/
        this.setSize(750, 600);
        this.add(lblImagen, null);

        this.setLayout(null);
        lblFichero.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.FicheroImportar"));
        lblFichero.setBounds(new java.awt.Rectangle(135, 215, 90, 20));

        txtFichero.setBounds(new java.awt.Rectangle(240, 220, 457, 20));

        txtFichero.setEnabled(false);
        txtFichero.setBackground(new Color(200, 200, 200));

        btnAbrir.setBounds(new java.awt.Rectangle(705, 220, 25, 25));
        btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
        btnAbrir.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    btnAbrir_actionPerformed(e);
                }
            });

        lblErrores.setText(aplicacion
                .getI18nString("importar.planeamiento.errores.validacion"));
        lblErrores.setBounds(new java.awt.Rectangle(135, 275, 593, 20));

        scpErrores.setBounds(new java.awt.Rectangle(135, 300, 595, 195));
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
        ediError.setEditable(false);
        ediError.setContentType("text/html");
        lblpersona.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.Persona"));
        lblorganismo.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.Organismo"));
        lblfuente.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.Fuente"));

        lbltipo.setText(aplicacion
                .getI18nString("importar.informacion.referencia.tipo.fichero"));
        lblpersona.setBounds(new Rectangle(135, 100, 95, 20));
        lblorganismo.setBounds(new Rectangle(135, 75, 95, 20));
        lblFecha.setBounds(new Rectangle(135, 125, 95, 20));
        lblfuente.setBounds(new Rectangle(135, 50, 95, 20));

        txtorganismo.setBounds(new Rectangle(240, 80, 490, 20));
        txtfuente.setBounds(new Rectangle(240, 55, 490, 20));
        txtpersona.setBounds(new Rectangle(240, 105, 490, 20));
        txtfecha.setBounds(new Rectangle(240, 130, 145, 20));

        txtfecha.setEnabled(false);
        txtfecha.setBackground(new Color(200, 200, 200));
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
        lblSelec.setBounds(new Rectangle(135, 165, 260, 20));
        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso1.datos"));
        lblSelec.setText(aplicacion.getI18nString("importar.usuario.paso1.seleccion"));
        lblTipoFichero.setText(aplicacion.getI18nString("importar.asistente.parcelas.tipo"));

        lblTipoFichero.setBounds(new java.awt.Rectangle(135, 190, 90, 20));
        cmbTipoFichero.setBounds(new java.awt.Rectangle(240,195,488,20));
        cmbTipoFichero.addItem(aplicacion.getI18nString("importar.asistente.parcelas.tipocmbRus"));

        lbltipo.setBounds(new Rectangle(430, 125, 120, 20));
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String date = (String) formatter.format(new Date());
        txtfecha.setText(date);
        jSeparator2.setBounds(new Rectangle(135, 270, 605, 2));
        lblFecha.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.Fecha"));
        jComboBox1.addItem(aplicacion.getI18nString("fichero.shape"));

        jSeparator1.setBounds(new Rectangle(135, 160, 605, 2));
        jComboBox1.setBounds(new Rectangle(550, 130, 180, 20));

        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rdbIgnorar);
        grupo.add(rdbMostrar);
        rdbIgnorar.setText(aplicacion.getI18nString("importar.ignorar.entidad"));
        rdbMostrar.setText(aplicacion.getI18nString("importar.mostrar.entidad"));
        rdbMostrar.setBounds(new java.awt.Rectangle(135, 245, 253, 20));
        rdbIgnorar.setBounds(new java.awt.Rectangle(431, 245, 301, 20));
        rdbMostrar.setSelected(true);

        this.setSize(750, 600);
        this.add(lblDatos, null);
        this.add(lblSelec, null);
        this.add(lblTipoFichero, null);
        this.add(cmbTipoFichero, null);

        this.add(cmbIdPlanHide, null);
        this.add(jSeparator2, null);
        this.add(jSeparator5, null);
        this.add(jSeparator4, null);
        this.add(jComboBox1, null);
        this.add(jSeparator1, null);
        this.add(lblFecha, null);
        this.add(txtfecha, null);
        this.add(lbltipo, null);
        this.add(geopistaEditor1, null);
        this.add(lblfuente, null);
        this.add(txtfuente, null);
        this.add(lblorganismo, null);
        this.add(txtorganismo, null);
        this.add(lblpersona, null);
        this.add(txtpersona, null);
        this.add(lblImagen, null);
        scpErrores.getViewport().add(ediError, null);
        this.add(scpErrores, null);
        this.add(lblErrores, null);
        this.add(btnAbrir, null);
        this.add(txtFichero, null);
        this.add(lblFichero, null);
        this.add(rdbIgnorar, null);
        this.add(rdbMostrar, null);
    }

   // }// jbinit

    public void enteredFromLeft(Map dataMap)
    {
        if(!aplicacion.isLogged())
        {
                aplicacion.login();
        }
        if(!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Importar.Parcelas");

                    acceso = aplicacion.checkPermission(geopistaPerm, "Catastro");

                    if (acceso)
                    {
                        btnAbrir.setEnabled(true);
                        txtpersona.setEnabled(true);
                        txtorganismo.setEnabled(true);
                        txtfuente.setEnabled(true);
                    } else
                    {
                        btnAbrir.setEnabled(false);
                        txtpersona.setEnabled(false);
                        txtorganismo.setEnabled(false);
                        txtfuente.setEnabled(false);
                    }
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackImportar.put("mostrarError", !rdbIgnorar.isSelected());
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


    private JSeparator jSeparator1 = new JSeparator();

    private JComboBox jComboBox1 = new JComboBox();

    private JSeparator jSeparator4 = new JSeparator();

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

    private WizardContext wizardContext;

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
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
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("SinAcceso"));
            return false;
        }


    }

    private void btnAbrir_actionPerformed(ActionEvent e)
    {

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("shp");
        filter.setDescription(aplicacion.getI18nString("fichero.shape"));

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
                                       progressDialog
                                       .report(aplicacion
                                               .getI18nString("CargandoMapaCatastro"));
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
                               
                                   try
                                   {
                                       capaImportar = (GeopistaLayer) geopistaEditor1
                                               .loadData(rutaFichero,
                                                       "Catastro");
                                       capaImportar.setSystemId("sourceImportLayer");
                                       blackImportar.put("sourceImportLayer", capaImportar);
                                   } catch (Exception e)
                                   {
                                       JOptionPane
                                               .showMessageDialog(
                                                       aplicacion.getMainFrame(),
                                                       aplicacion
                                                               .getI18nString("errorCargaMapaCatastro"));
                                       throw e;
                                   }

                                   progressDialog.report(aplicacion
                                           .getI18nString("ValidandoDatos"));
                                   capaImportar.setActiva(true);
                                   capaImportar.setVisible(true);

                                       try
                                       {
                                           tipoFichero = jComboBox1.getSelectedIndex();

                                           numRegNoExisten = 0;
                                           
                                           FeatureSchema esquema = capaImportar.getFeatureCollectionWrapper().getFeatureSchema();
                                           
                                           continuar = valImport.compruebaParcela(textoEditor, esquema, tipoFichero);
                                           nombreTabla = "parcelas";
                                                 
                                               cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>" + aplicacion.getI18nString("validando") + " <b>" + aplicacion.getI18nString(nombreTabla)
                                                       + "</b></font></p>";
                                               ediError.setText(cadenaTexto);
                                               //cadenaTexto = "";
                                               if (continuar)
                                               {
                                                   cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin.correcto");
                                                   blackImportar.put("rutaFicheroImportar", rutaFichero);
                                                   blackImportar.put("nombreTabla", nombreTabla);
                                                   blackImportar.put("editorGeo", geopistaEditor1);
                                                   blackImportar
                                                   .put(
                                                           "numeroRegistros",
                                                           capaImportar
                                                                   .getFeatureCollectionWrapper()
                                                                   .size());

                                               }
                                               else
                                               {
                                                   cadenaTexto = cadenaTexto + textoEditor;
                                                   cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin.incorrecto");
                                               }
                                               
                                       }
                                       
                                       catch (Exception ex)
                                       {

                                           cadenaTexto = cadenaTexto + aplicacion.getI18nString("errDesconocido") + aplicacion.getI18nString("validar.fin.incorrecto");
                                           continuar = false;
                                           ex.printStackTrace();
                                       }
                                       wizardContext.inputChanged();
                                       cadenaTexto = cadenaTexto + aplicacion.getI18nString("validar.fin");
                                       ediError.setText(cadenaTexto);
                                   }
                                   
                                   progressDialog.report(aplicacion
                                           .getI18nString("GeopistaLoadMapPlugIn.CargandoMapa"));
                                   String rutaMapa = null;
                                   
                                       rutaMapa = "url.mapa.catastro";
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
                                                                   .getI18nString("errorCargaMapaCatastro"));
                                           throw e;
                                       }
                                   
                                   
                                   
                               }
                               catch(Exception e)
                               {
                                   e.printStackTrace();
                               }
                               finally
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

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
} // de la clase general.

