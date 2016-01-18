package com.geopista.app.acteconomicas;

import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.app.AppContext;
import com.geopista.app.inicio.GeopistaInicio;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.security.GeopistaPermission;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import javax.swing.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;

import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 05-jun-2006
 * Time: 10:27:17
 */
/**
 * Clase utilizada para importar el fichero de datos economicos
 * esta pantalla comprueba unicamente que los datos sean correctos
 */
public class ImportarActividadesEconomicas extends JPanel implements  WizardPanel
{
    Logger logger =Logger.getLogger(ImportarActividadesEconomicas.class);
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private boolean tienePermiso;
    private Blackboard blackboard = aplicacion.getBlackboard();
    private boolean hayErroresFilas = true;
    private String cadenaTexto = null;
    private WizardContext wizardContext;
    private String errorMessage = "";
    private int filasFichero;

    //Componentes de la pantalla
    private JScrollPane jScrollPaneErrores = new JScrollPane();
    private JLabel jLabelImagen = new JLabel();
    private JLabel jLabelFichero = new JLabel();
    private JTextField jTextFieldFichero = new JTextField();
    private JButton jButtonAbrir = new JButton();
    private JLabel jLabelErrores = new JLabel();
    private JLabel jLabelTipoFichero = new JLabel();
    private JComboBox jComboBoxTipoInfo = new JComboBox();
    private JEditorPane jEditorPaneError = new JEditorPane();

    private JComboBox jComboBoxFicheroTexto = new JComboBox();
    private JLabel jLabelComent = new JLabel();

    private JCheckBox geoLocal = new JCheckBox();
    private JCheckBox jChekcBoxManual = new JCheckBox();


    //private final int LONG_FICHERO=554;


    /**
     * Constructor de la clase
     */
    public ImportarActividadesEconomicas()
    {
           try{
                jbInit();
            } catch (Exception e){
                logger.error("Error al importar actividades economicas",e);
            }
    }
    /**
     * Inicializa los componenetes de la pantalla
     * @throws Exception
     */
    private void jbInit() throws Exception
    {
        setLayout(null);
        setName(aplicacion.getI18nString("importar.asistente.elementos.acteconomicas.titulo1"));
        jComboBoxFicheroTexto.addItem(aplicacion.getI18nString("fichero.texto"));
        jScrollPaneErrores.setBounds(new Rectangle(135, 200, 595, 300));
        jComboBoxTipoInfo.addItem(aplicacion.getI18nString("importar.acteconomicas"));
        jComboBoxTipoInfo.setSelectedIndex(0);
        jButtonAbrir.setIcon(IconLoader.icon("abrir.gif"));
        jLabelImagen.setIcon(IconLoader.icon("inf_referencia.png"));
        jLabelImagen.setBounds(new Rectangle(15, 20, 110, 490));
        jLabelImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jLabelFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.fichero.importar"));
        jLabelFichero.setBounds(new Rectangle(135, 115, 240, 20));
        jTextFieldFichero.setBounds(new Rectangle(375, 115, 280, 20));
        jTextFieldFichero.setEditable(false);
        geoLocal.setText(aplicacion.getI18nString("importar.informacion.referencia.georreferenciar.local"));
        jChekcBoxManual.setText(aplicacion.getI18nString("importar.mostrar.entidad"));
        geoLocal.setBounds(new Rectangle(175,150,200,20));
        jChekcBoxManual.setBounds(new Rectangle(400,150,300,20));
        jButtonAbrir.setBounds(new Rectangle(665, 115, 25, 20));
        jButtonAbrir.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                abrir();
            }
        });
        jLabelErrores.setText(aplicacion.getI18nString("importar.informacion.referencia.errores.validacion"));
        jLabelErrores.setBounds(new Rectangle(135, 180, 510, 20));
        jLabelTipoFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.tipo.fichero"));
        jLabelTipoFichero.setBounds(new Rectangle(140, 85, 130,20));
        jComboBoxTipoInfo.setBackground(new Color(255, 255, 255));
        jComboBoxTipoInfo.setBounds(new Rectangle(375, 85, 215, 20));
        jEditorPaneError.setContentType("text/html");
        jEditorPaneError.setEditable(false);
        add(jLabelComent, null);
        add(jComboBoxFicheroTexto, null);
        add(jComboBoxTipoInfo, null);
        add(jLabelTipoFichero, null);
        add(geoLocal, null);
        add(jChekcBoxManual,null);
        add(jLabelErrores, null);
        add(jButtonAbrir, null);
        add(jTextFieldFichero, null);
        add(jLabelFichero, null);
        add(jLabelImagen, null);
        jScrollPaneErrores.getViewport().add(jEditorPaneError, null);
        add(jScrollPaneErrores, null);
        addAyudaOnline();
    }
 	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarActividadesEconomicas#Inserci.C3.B3n_de_datos";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});
  	}
    public void enteredFromLeft(Map dataMap)
    {
        if (!aplicacion.isLogged())
            aplicacion.login();
        if (!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        //permisos
        //TODO tendremos que poner aquí el permiso correspondiente
        //GeopistaPermission paso = new GeopistaPermission(
        //        "Geopista.InfReferencia.Importar Padron");
        //tienePermiso = aplicacion.checkPermission(paso, "Informacion de Referencia");
        tienePermiso=true;
    }

    /**
     * Lo que queremos hacer antes de salir de la pantalla
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard el mapaCagarTramosVias
        blackboard.put("ficheroActividades", jTextFieldFichero.getText());
        blackboard.put("georreferenciarLocal", new  Boolean(geoLocal.isSelected()));
        blackboard.put("mostrarError", new Boolean(jChekcBoxManual.isSelected()));
    }

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
        if (tienePermiso)
        {
            return (!hayErroresFilas);
        } else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            return false;
        }
    }

    private String nextID = "2";

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
        wizardContext = wd;
    }

    private void abrir()
    {

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("xls");
        filter.setDescription(aplicacion.getI18nString("acteconomicas.fichero.tipoFichero"));
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
        File currentDirectory = (File) blackboard.get(GeopistaInicio.LAST_IMPORT_DIRECTORY);
        fc.setCurrentDirectory(currentDirectory);
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;
        blackboard.put(GeopistaInicio.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
        progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
        progressDialog.addComponentListener(new ComponentAdapter(){
           public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                     public void run(){
                     try{
                        errorMessage = "";
                        jTextFieldFichero.setText(fc.getSelectedFile().getPath());
                        cadenaTexto= "<font face=SansSerif size=3>"+ aplicacion.getI18nString("ImportacionComenzar")
                                            + "<b> "+ fc.getSelectedFile().getName() + "</b>"
                                            + "<p>"+ aplicacion.getI18nString("OperacionMinutos") + " ...</p></font>";
                        jEditorPaneError.setText(cadenaTexto);
                        ArrayList listaActividades = validarActividades(jTextFieldFichero.getText());
                        if (listaActividades!=null && listaActividades.size()==filasFichero )
                        {
                              cadenaTexto+=aplicacion.getI18nString("importar.datos.acteconomicas.correctos");
                              blackboard.put("listaActividades",listaActividades);
                              blackboard.put("totalActividades",new Integer(listaActividades.size()));
                              hayErroresFilas = false;
                        } else {
                             hayErroresFilas=true;
                             showErrorMessage();
                        }
                 } catch (Exception e)
                 {
                      logger.error("Exxception:",e);
                 } finally
                 {
                      progressDialog.setVisible(false);
                  }
          }}).start();
       }
            });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);
       cadenaTexto+=aplicacion.getI18nString("validacion.finalizada");
       jEditorPaneError.setText(cadenaTexto);
       wizardContext.inputChanged();

    }
    /**
     * Método para determinar si las filas del fichero de texto cumplen las
     * validaciones
     *
     */
    public ArrayList validarActividades(String ruta)
    {
        logger.info("Validando datos del fichero: "+ruta);
        ArrayList lista = new ArrayList();

        try
        {
            if(ruta==null || !ruta.toUpperCase().endsWith(".XLS")){
                errorMessage += aplicacion.getI18nString("acteconomicas.fichero.extensionInvalida");
                return null;
            }

            filasFichero=0;
            POIFSFileSystem fs= new POIFSFileSystem(new FileInputStream(ruta));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);

            int j=1;
            HSSFRow row = sheet.getRow(j);
            while (row!=null)
            {
                filasFichero++;
                try
                {
                    DatosImportarActividades importarActividades =
                            new DatosImportarActividades(row);
                    lista.add(importarActividades);
                }catch(Exception e)
                {
                    errorMessage += aplicacion.getI18nString("importar.en.la.fila")+
                                  " "+filasFichero+ " " +e.toString();
                    e.printStackTrace();
                }
                j++;
                row = sheet.getRow(j);
            }
            fs=null;
            wb=null;
            sheet=null;
        }
        catch (Exception e)  {

            errorMessage = errorMessage + aplicacion.getI18nString("importar.en.la.fila")
                    + " " + filasFichero + " "+e.toString()
                    + aplicacion.getI18nString("importar.acteconomicas.incorrecto");
            return null;
        }

        return lista;
    }

    private void showErrorMessage()
    {
        //hayErroresFilas = true;
        cadenaTexto+= errorMessage + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");

    }

     /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
         logger = null;
         aplicacion = null;
         blackboard.put("listaActividades",null);
         blackboard = null;
         cadenaTexto = null;
         wizardContext = null;
         errorMessage = null;
         jScrollPaneErrores.removeAll();
         jScrollPaneErrores=null;
         jLabelImagen = null;
         jLabelFichero =null;
         jTextFieldFichero = null;
         jButtonAbrir = null;
         jLabelErrores = null;
         jLabelTipoFichero = null;
         jComboBoxTipoInfo = null;
         jEditorPaneError.removeAll();
         jEditorPaneError=null;
         jComboBoxFicheroTexto = null;
         jLabelComent = null;
         geoLocal = null;
         jChekcBoxManual = null;
    }

} // de la clase general.

