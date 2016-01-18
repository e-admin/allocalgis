package com.geopista.app.catastro;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import com.geopista.editor.GeopistaEditor;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarRustica2005 extends JPanel implements WizardPanel
{
    //AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private String rutaFichero;
    
    private JLabel lblFichero = new JLabel();
    
    private JTextField txtFichero = new JTextField();
    
    private JButton btnAbrir = new JButton();
    
    private JLabel lblErrores = new JLabel();
    
    private JScrollPane scpErrores = new JScrollPane();
    
    private JLabel lblImagen = new JLabel();
    
    private JEditorPane ediError = new JEditorPane();
    
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
    
    private GeopistaEditor geopistaEditor1 = new GeopistaEditor();
    
    private JLabel lbltipo = new JLabel();
    
    private JTextField txtfecha = new JTextField();
    
    private JLabel lblFecha = new JLabel();
    
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    
    private Blackboard blackImportar = aplicacion.getBlackboard();
    
    private JSeparator jSeparator2 = new JSeparator();
    
    private JSeparator jSeparator5 = new JSeparator();
    
    private JLabel lblDatos = new JLabel();
    
    private JLabel lblSelec = new JLabel();
    
    private JLabel lblTipoFichero = new JLabel();
    
    private JComboBox cmbTipoFichero = new JComboBox();
    
    
    private JComboBox cmbIdPlanHide = new JComboBox();
    
    private StringBuffer textoEditor = null;   
    
    private int returnVal = 0;
    
    private JFileChooser fc = new JFileChooser();    
    
    public static int LONGITUD_REGISTRO = 720;
    
    public static byte[] inicioCabecera = new byte[]{0x30, 0x31};
    
    public static byte[] inicioRegistro = new byte[]{0x36, 0x30};
    
    public static byte[] inicioCola = new byte[]{0x39, 0x30};
    
    public GeopistaImportarRustica2005()
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
                .getI18nString("importar.asistente.rustica.2005.titulo1"));
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
        lblTipoFichero.setText(aplicacion.getI18nString("importar.asistente.urbana.tipo.padron"));
        
        lblTipoFichero.setBounds(new java.awt.Rectangle(135, 190, 90, 20));
        cmbTipoFichero.setBounds(new java.awt.Rectangle(240,195,488,20));
        cmbTipoFichero.addItem(aplicacion.getI18nString("importar.asistente.rustica.tipocmb.padron"));
        
        
        lbltipo.setBounds(new Rectangle(430, 125, 120, 20));
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        String date = (String) formatter.format(new Date());
        txtfecha.setText(date);
        jSeparator2.setBounds(new Rectangle(135, 270, 605, 2));
        lblFecha.setText(aplicacion.getI18nString("GeopistaImportarAmbitos01Panel.Fecha"));
        jComboBox1.addItem(aplicacion.getI18nString("fichero.texto"));
        
        jSeparator1.setBounds(new Rectangle(135, 160, 605, 2));
        jComboBox1.setBounds(new Rectangle(550, 130, 180, 20));
        
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        
        
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
        GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Importar.Padron.Rustica.2005");
        
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
        blackImportar.put("SujetoSinDNI",0);
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
        filter.addExtension("txt");
        filter.setDescription(aplicacion.getI18nString("importar.informacion.referencia.fichero.texto"));
        
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
                                String cadenaTexto = "";
                                textoEditor = new StringBuffer();
                                
                                rutaFichero = fc.getSelectedFile().getPath();
                                txtFichero.setText(rutaFichero);
                                
                                //Se recorre el fichero
                                int cuenta_lineas=0;
                                File file = new File(rutaFichero);                                 
                                InputStream is = new FileInputStream(file);                      
                                
                                continuar = true;
                                
                                long numRegistros = file.length()/LONGITUD_REGISTRO;
                                
                                if (file.length()%LONGITUD_REGISTRO!=0)
                                {
                                    cadenaTexto = cadenaTexto
                                        + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                        + aplicacion
                                        .getI18nString("importar.asistente.urbana.longitud.registros")
                                        + "</b></font></p>";
                                    continuar = false;
                                }
                                else
                                {                                    
                                    byte[] bytes = new byte[LONGITUD_REGISTRO];                                                
                                    int len;
                                    
                                    while ((len = is.read(bytes)) > 0 && continuar)
                                    {
                                        cuenta_lineas ++;
                                        if (cuenta_lineas==1)
                                        {
                                            if( bytes[0]!=inicioCabecera[0] || bytes[1]!=inicioCabecera[1])
                                            {
                                                cadenaTexto = cadenaTexto
                                                    + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                    + aplicacion
                                                    .getI18nString("importar.asistente.urbana.cabecera")
                                                    + "</b></font></p>";
                                                continuar = false; //El registro de cabecera no comienza x 01
                                                break;
                                            }
                                        }
                                        
                                        else if (cuenta_lineas == numRegistros)
                                        {
                                            if ( bytes[0]!=inicioCola[0] || bytes[1]!=inicioCola[1])
                                            {                                                        
                                                cadenaTexto = cadenaTexto
                                                    + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                    + aplicacion
                                                    .getI18nString("importar.asistente.urbana.cola")
                                                    + "</b></font></p>";
                                                continuar = false;
                                            }   
                                        }
                                        
                                        else if (bytes[0]!=inicioRegistro[0] || bytes[1]!=inicioRegistro[1])
                                        {                                                  
                                            cadenaTexto = cadenaTexto
                                                + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                + aplicacion
                                                .getI18nString("importar.asistente.urbana.incorrecto")
                                                + " " + cuenta_lineas + ")"
                                                + "</b></font></p>";
                                            continuar = false;
                                            break;
                                        }                                                    
                                        
                                    }
                                    
                                    if (continuar == true)
                                    {
                                        cadenaTexto = cadenaTexto
                                            + aplicacion
                                            .getI18nString("validar.fin.correcto");
                                        blackImportar.put("rutaFicheroImportar", rutaFichero);
                                    }  
                                }                                                        
                                
                                wizardContext.inputChanged();
                                cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");
                                ediError.setText(cadenaTexto);
                                
                            }
                            else
                            {
                                //Aprove option
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

