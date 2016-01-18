/**
 * MainCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.gestorcatastral;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.acl.AclNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.GeopistaMostrarParcelas;
import com.geopista.app.catastro.GeopistaMostrarSubParcelas;
import com.geopista.app.catastro.GeopistaSeleccionarFicheroParcelas;
import com.geopista.app.catastro.GeopistaSeleccionarFicheroSubParcelas;
import com.geopista.app.catastro.gestorcatastral.images.IconLoader;
import com.geopista.app.catastro.historico.DialogoActualizacionPeriodica;
import com.geopista.app.catastro.historico.DialogoModosConvenios;
import com.geopista.app.catastro.historico.consultaHistorico.ConsultarHistorico;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.importacion.EstadoImportarFXCC;
import com.geopista.app.catastro.intercambio.importacion.EstadoImportarFXCCMasivo;
import com.geopista.app.catastro.intercambio.importacion.ImportarFINRetornoMasivo;
import com.geopista.app.catastro.intercambio.importacion.ImportarFINSalida;
import com.geopista.app.catastro.intercambio.importacion.ImportarFXCC;
import com.geopista.app.catastro.intercambio.importacion.ImportarFXCCMasivo;
import com.geopista.app.catastro.intercambio.importacion.ImportarInfoGraficaPanel;
import com.geopista.app.catastro.intercambio.importacion.ImportarInfoIntercambio;
import com.geopista.app.catastro.intercambio.importacion.ImportarMunicipios;
import com.geopista.app.catastro.intercambio.importacion.ImportarPadronMunicipal;
import com.geopista.app.catastro.intercambio.importacion.ImportarPonencia;
import com.geopista.app.catastro.intercambio.importacion.ImportarSeleccionarFicheroFXCC;
import com.geopista.app.catastro.intercambio.importacion.MostrarFINRetornoMasivo;
import com.geopista.app.catastro.intercambio.importacion.MostrarFINSalida;
import com.geopista.app.catastro.intercambio.importacion.MostrarMunicipios;
import com.geopista.app.catastro.intercambio.importacion.MostrarPadronMunicipal;
import com.geopista.app.catastro.intercambio.importacion.MostrarPonencia;
import com.geopista.app.catastro.intercambio.importacion.MostrarProgresoIntercambio;
import com.geopista.app.catastro.intercambio.importacion.ScreenComponent;
import com.geopista.app.catastro.intercambio.importacion.ValidarEsquemaPanel;
import com.geopista.app.catastro.intercambio.importacion.ValidarFichero;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileValidationPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosConfiguracion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.registroExpedientes.CatastroClient;
import com.geopista.app.catastro.registroExpedientes.MaquinaEstadoFlujo;
import com.geopista.app.catastro.registroExpedientes.asociarBienInmueble.AsociarBienInmueble;
import com.geopista.app.catastro.registroExpedientes.asociarParcela.AsociarParcelas;
import com.geopista.app.catastro.registroExpedientes.busqueda.Busqueda;
import com.geopista.app.catastro.registroExpedientes.busqueda.BusquedaExportacionMasiva;
import com.geopista.app.catastro.registroExpedientes.cambiarUsuarioExp.CambiarUsuarioExp;
import com.geopista.app.catastro.registroExpedientes.configuracion.paneles.GeopistaConfiguracionCertificado;
import com.geopista.app.catastro.registroExpedientes.configuracion.paneles.GeopistaImportacionCertificadoJDialog;
import com.geopista.app.catastro.registroExpedientes.crearExp.CrearExpediente;
import com.geopista.app.catastro.registroExpedientes.exportacionMasiva.ExportacionMasiva;
import com.geopista.app.catastro.registroExpedientes.gestionExp.GestionDeExpedientes;
import com.geopista.app.catastro.registroExpedientes.imagenEstado.MostrarImagenEstado;
import com.geopista.app.catastro.registroExpedientes.modificarExp.ModificarExpediente;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.Estructuras;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosErrorWS;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosWSResponseBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.ErrorWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.app.catastro.servicioWebCatastro.beans.UnidadErrorElementoWSBean;
import com.geopista.app.catastro.servicioWebCatastro.panels.MessageDialog;
import com.geopista.app.catastro.servicioWebCatastro.utils.GestionResponseWS;
import com.geopista.app.catastro.servicioWebCatastro.utils.WSOperations;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.app.utilidades.CMain;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.Municipio;
import com.geopista.protocol.administrador.OperacionesAdministrador;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.GeopistaPermission;
import com.geopista.security.GeopistaPrincipal;
import com.geopista.security.ISecurityPolicy;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.catastro.ws.CatastroWS;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashWindow;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.ws.cliente.TestClient;

/**
 * Clase principal del programa. Inicia la aplicacion, cargando los datos necesarios al inicio y controlando el flujo
 * entre las pantallas ayudandose de la clase MaquinaEstadosFlujo. Es la capa de nivel mas alto de la aplicacion.
 * Tambien en esta capa se crea la gui de la pantalla principal. En esta capa se controlan todos los permisos del usuario
 * que esta intentando acceder.
 *
 */
public class MainCatastro extends CMain implements IMainCatastro,ISecurityPolicy {
	   
	 static Logger logger;
	    static {
	       createDir();
	       logger  = Logger.getLogger(MainCatastro.class);
	    } 		
    
    private Hashtable permisos= new Hashtable();
    private IMultilingue pantallaMultilingue;
    private static boolean hayExp= false;
    private static ArrayList expUsuario = null;

    //Atributos del GUI
    private ApplicationContext aplicacion = (AppContext)AppContext.getApplicationContext();
    private JDesktopPane desktopPane;

    private JMenuBar jMenuBarCatastro = null;
    private JMenu jMenuRegistro = null;
    private JMenu jMenuImportacion = null;
    private JMenu jMenuGenerarFicheros = null;
    private JMenu jMenuAyuda = null;
    private JMenu idiomaMenu;
    private JMenu jMenuConfiguracion;

    private JMenuItem jMenuItemActualizacionesEnviosPeriodicos;
    private JMenuItem jMenuItemConfigModosConvenion;
    private JMenuItem jMenuItemAsociarUsuariosExpedientes;
    private JMenuItem jMenuItemAsociarCertificado;
    private JMenuItem jMenuItemImportarCertificado;
	private JMenuItem crearExpMenuItem;
    private JMenuItem gestionExpMenuItem;
    private JMenuItem jMenuItemModificarExpediente = null;
    private JMenuItem jMenuItemValidarParcelas = null;
    private JMenuItem jMenuItemConsultarIntercambios = null;
    private JMenuItem jMenuItemFicherosMunicipios = null;
    private JMenuItem jMenuItemPonenciaValores = null;
    private JMenuItem jMenuItemPadronCatastral = null;
    private JMenuItem jMenuItemFinSalida = null;
    private JMenuItem jMenuItemFinRetornoMasivo = null;
    private JMenuItem jMenuItemFinEntradaRelativo = null;
    private JMenuItem jMenuItemAcercaDe = null;
    private JMenuItem espanolMenuItem;
    private JMenuItem catalanMenuItem;
    private JMenuItem valencianoMenuItem;
    private JMenuItem gallegoMenuItem;
    private JMenuItem contenidoAyudaMenuItem;

    private JDialog dialog = null;

    ConnectionStatus status;

    private JPanel centerPane = null;

    /**
     * Guarda el último componente cargado en la pantalla
     */
    private Component oldComponent;

    private JMenuItem jMenuItemFicheroIntercambioInfoTitularidad = null;
    private JMenuItem jMenuItemFicheroIntercambioInfoCatastral = null;
    private JMenuItem jMenuItemFicheroIntercambioInfoCatastralRetorno = null;

    private JMenuItem jMenuItemAsociarInfoGrafica = null;
	private JMenuItem jMenuItemImportarSubparcelas = null;
	private JMenuItem jMenuItemImportarDatosCatastro = null;
	private JMenuItem jMenuItemImportarFXCC = null;
	private JMenuItem jMenuItemImportarFXCCMasivo = null;
	private JMenu jMenuItemImportFXCC = null;
	private String catastroUrl = "";
	
	
    public static final int DIM_X = 800;
    public static final int DIM_Y = 600;
    public static final Rectangle PICTURE_BORDER = new Rectangle(15, 5, 100, 440);
    public static final String BIG_PICTURE_LOCATION = "catastro.png";
    public static final String SMALL_PICTURE_LOCATION = "catastro_small.png";
    public static final String CATASTRO_LOGO = "catastro_logo.gif";




    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }
    
    public MainCatastro() {
    	aplicacion.setUrl(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.CATASTRO_WEBAPP_NAME);
    	    	
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        aplicacion.setMainFrame(this);
        try
        {
            initLookAndFeel();
        }
        catch (Exception e) {}
        SplashWindow splashWindow = showSplash();
        //Cotesa
        Locale loc= I18N.getLocaleAsObject();
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.ModuloCatastrali18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("ModuloCatastral",bundle);
        bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);


        Locale locRegExp= I18N.getLocaleAsObject();
        ResourceBundle bundleRegExp = ResourceBundle.getBundle("com.geopista.app.catastro.registroExpedientes.language.RegistroExp",locRegExp,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("RegistroExpedientes",bundleRegExp);

        inicializaElementos();
        configureApp();
        //Icono
        this.setIconImage(IconLoader.icon(CATASTRO_LOGO).getImage());
        this.setTitle(I18N.get("ModuloCatastral","modulocatastral.titulo"));
        try
        {
			show();
            /** deshabilitamos la barra de tareas */
            try
            {
                UtilRegistroExp.menuBarSetEnabled(false, this);
                UtilRegistroExp.inicializarIconos();

            }
            catch(Exception e) {};
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (splashWindow != null) splashWindow.setVisible(false);
			
		
			SSOAuthManager.ssoAuthManager(ConstantesCatastro.idApp);								
            if (!AppContext.getApplicationContext().isOnlyLogged()){  
           
           
                if (!showAuth()){
                	dispose();
                	return;
                }
            }
        
            setPolicy();
            
            
			
            
            if(!tienePermisos("LocalGIS.Catastro.Login")){
            	noPermApp();
            	return;
            }  
         
            if (!AppContext.seleccionarMunicipio((Frame)this)){
            	stopApp();
            	return;
            }
            ConstantesCatastro.IdMunicipio = AppContext.getIdMunicipio();
            
            /** cargamos la provincia y el municipio */
            Municipio municipio = (new OperacionesAdministrador(catastroUrl)).getMunicipio(new Integer(ConstantesCatastro.IdMunicipio).toString());
            if (municipio!=null)
            {
            	ConstantesCatastro.Municipio = municipio.getNombre();
            	ConstantesCatastro.Provincia= municipio.getProvincia();

            	com.geopista.security.GeopistaPrincipal principal= com.geopista.security.SecurityManager.getPrincipal();
            	if(principal!=null)
            		setTitle(getTitle()+ " - " + ConstantesCatastro.Municipio + " (" + ConstantesCatastro.Provincia+") - " + I18N.get("RegistroExpedientes","modulocatastral.cauthdialog.jlabelnombre") + principal);
            	com.geopista.security.SecurityManager.setIdMunicipio(new Integer(ConstantesCatastro.IdMunicipio).toString());
            }

            /** dialogo de espera de carga */
            final JFrame desktop= (JFrame)this;
            final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
           // progressDialog.setTitle(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
            progressDialog.setTitle("TaskMonitorDialog.Wait");
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                progressDialog.report(I18N.get("RegistroExpedientes",
                                "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                                String idUser = ConstantesRegExp.clienteCatastro.inicializaBlackBoard();
                                aplicacion.getBlackboard().put("idUserCatastro", idUser);
                                DatosConfiguracion datos = ConstantesRegExp.clienteCatastro.getParametrosConfiguracion();
                                if(datos!=null)
                                {
                                    ConstantesCatastro.modoTrabajo = datos.getModoTrabajo();
                                    ConstantesCatastro.tipoConvenio = datos.getTipoConvenio();
                                    ConstantesCatastro.formaConvenio = datos.getFormaConvenio();
                                }
                                if(!ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_NINGUNO))
                                {
                                    /** cargamos las estructuras */
                                    while (!Estructuras.isCargada())
                                    {
                                        if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
                                        try {Thread.sleep(500);} catch (Exception e) {}
                                    }
                                    /**Comprobamos los expedientes del usuario*/
                                    ConstantesCatastro.tiposExpedientes = ConstantesRegExp.clienteCatastro.
                                            getTiposExpedientes(ConstantesCatastro.tipoConvenio);
                                    ConstantesRegistroExp.maquinaEstadosFlujo = new MaquinaEstadoFlujo();
                                    expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo);
                                    if((expUsuario!=null)&&(expUsuario.size()>0))
                                    {
                                        hayExp= true;
                                    }
                                    comprobarActualizacionesYEnvios();
                                }
                            }
                            catch(Exception e)
                            {
                                logger.error("Error ", e);
                                ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                return;
                            }
                            finally
                            {
                                progressDialog.setVisible(false);                                
                                progressDialog.dispose();
                            }
                        }
                  }).start();
              }
           });
           GUIUtil.centreOnWindow(progressDialog);
           progressDialog.setVisible(true);
           if(hayExp)
           {
               muestraBuscarExpedientesUsuario(expUsuario);
           }
           else
           {
               UtilRegistroExp.menuBarSetEnabled(true, desktop);
               desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           }
           GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
           ((IMainCatastro)desktop).applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
           
           this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        catch (Exception e)
        {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println("ERROR:" + sw.toString());
			logger.error("Exception: " + sw.toString());
		}
    }

    private void noPermApp(){
    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"No tiene permisos para entrar. Se cerrará el aplicativo");
    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	 System.exit(1);
    }
    
    /**
     * Funcion que inicializa la gui de la pantalla principal, asociandole eventos para que tenga un tamaño minimo y
     * la accion de cierre.
     */
    private void inicializaElementos()
    {
        //Creacion de objetos.
        desktopPane= new JDesktopPane();
        setJMenuBar(getJMenuBarCatastro());
        desktopPane.setMinimumSize(null);
		desktopPane.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
        
        
        //Gestion de conexiones y desconexiones contra el administrador
  		//de cartografia.
  		status=new ConnectionStatus(this,false);
  		status.init();	
  		aplicacion.getBlackboard().put(UserPreferenceConstants.CONNECT_STATUS,status);
      		
        getContentPane().add(status.getJPanelStatus(), BorderLayout.SOUTH);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);

        //Evento para que la ventana al minimizar tenga un tamaño minimo.
        addComponentListener(new java.awt.event.ComponentAdapter()
        {
          public void componentResized(ComponentEvent e)
          {
                UtilRegistroExp.ajustaVentana(aplicacion.getMainFrame(), DIM_X, DIM_Y+50);
          }
        });

        //Evento al cerrar la ventana.
        addWindowListener(new java.awt.event.WindowAdapter()
        {
			public void windowClosing(java.awt.event.WindowEvent evt)
            {
				exitForm(evt);
			}
		});
        
       
    }
    
   
    
    private JMenuItem logMenuItem = null;
    
    /**
     * This method initializes logMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getLogMenuItem()
    {
        if (logMenuItem == null)
        {
            logMenuItem = new JMenuItem();
            logMenuItem.setEnabled(aplicacion.isOnline());
            logMenuItem.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                        if (aplicacion.isLogged())
                            aplicacion.logout();
                        else
                            aplicacion.login();
                    }
                });
        }
        return logMenuItem;
    }

   
	private JMenuItem jMenuItemValidarEsquema;

    /**
     * Funcion que inicializa el menu principal de la aplicacion.
     */
    private JMenuBar getJMenuBarCatastro()
    {
        if(jMenuBarCatastro==null)
        {
            jMenuBarCatastro = new JMenuBar();
            jMenuBarCatastro.add(getJMenuRegistro());
            jMenuBarCatastro.add(getJMenuImportacion());
            jMenuBarCatastro.add(getJMenuGenerarFicheros());
            jMenuBarCatastro.add(getJMenuConfiguracion());
            jMenuBarCatastro.add(getJMenuAyuda());
            jMenuBarCatastro.setBorderPainted(false);
        }
        return jMenuBarCatastro;
    }

    /**
     * This method initializes jMenuRegistro
     *
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuRegistro()
    {
        if (jMenuRegistro == null)
        {
            jMenuRegistro = new JMenu();
            jMenuRegistro.setText(I18N.get("ModuloCatastral","modulocatastral.menu.gestionexpedientes"));
            jMenuRegistro.add(getJMenuItemCrearExpediente());
            jMenuRegistro.add(getJMenuItemConsultarExpediente());
            jMenuRegistro.add(getJMenuItemModificarExpediente());
            jMenuRegistro.add(getJMenuItemValidarParcelas());
            jMenuRegistro.add(getJMenuItemConsultarIntercambios());
        }
        return jMenuRegistro;
    }

    /**
     * This method initializes jMenuImportacion
     *
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuImportacion()
    {
        if (jMenuImportacion == null)
        {
            jMenuImportacion = new JMenu();
            jMenuImportacion.setText(I18N.get("ModuloCatastral","modulocatastral.menu.importacionficheros"));
            jMenuImportacion.add(getJMenuItemImportarDatosCatastro());
            jMenuImportacion.add(getJMenuItemImportarSubparcelas());
            jMenuImportacion.add(getJMenuItemImportFXCC());
            jMenuImportacion.add(getJMenuItemPonenciaValores());
            jMenuImportacion.add(getJMenuItemPadronCatastral());
            jMenuImportacion.add(getJMenuItemFinSalida());
            jMenuImportacion.add(getJMenuItemFinRetornoMasivo());
            jMenuImportacion.addSeparator();
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoTitularidad());
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoCatastral());
            jMenuImportacion.add(getJMenuItemFicheroIntercambioInfoCatastralRetorno());

        }
        return jMenuImportacion;
    }

    /**
     * This method initializes jMenuGenerarFicheros
     *
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuGenerarFicheros()
    {
        if (jMenuGenerarFicheros == null)
        {
            jMenuGenerarFicheros = new JMenu();
            jMenuGenerarFicheros.setText(I18N.get("ModuloCatastral","modulocatastral.menu.generacionyenvio"));
            jMenuGenerarFicheros.add(getJMenuItemFinEntradaRelativo());
            jMenuGenerarFicheros.add(getJMenuItemValidarEsquema());
        }
        return jMenuGenerarFicheros;
    }

    private JMenu getJMenuConfiguracion()
    {
        if(jMenuConfiguracion==null)
        {
            jMenuConfiguracion= new JMenu();
            jMenuConfiguracion.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.Configuracion"));
            jMenuConfiguracion.add(getJMenuItemActualizacionesEnviosPeriodicos());
            jMenuConfiguracion.add(getJMenuItemConfigModosConvenion());
            jMenuConfiguracion.add(getJMenuItemAsociarUsuariosExpedientes());
            jMenuConfiguracion.add(getJMenuItemAsociarCertificado());
            jMenuConfiguracion.add(getJMenuItemImportarCertificado());
        }						   
        return jMenuConfiguracion;
    }

    private JMenu getJMenuIdiomas()
    {
        if(idiomaMenu==null)
        {
            idiomaMenu = new JMenu();
            idiomaMenu.setText("Idioma");
            idiomaMenu.add(getJMenuItemEspanol());
            idiomaMenu.add(getJMenuItemCatalan());
            idiomaMenu.add(getJMenuItemValenciano());
            idiomaMenu.add(getJMenuItemGallego());
        }
        return idiomaMenu;
    }

    /**
     * This method initializes jMenuAyuda
     *
     * @return javax.swing.JMenu
     */
    private JMenu getJMenuAyuda()
    {
        if (jMenuAyuda == null)
        {
            jMenuAyuda = new JMenu();
            jMenuAyuda.setText(I18N.get("ModuloCatastral","modulocatastral.menu.ayuda"));
            jMenuAyuda.add(getJMenuItemAyuda());
            jMenuAyuda.add(getJMenuItemAcercaDe());
        }
        return jMenuAyuda;
    }

    /**
     * This method initializes jMenuItemCrearExpediente
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemCrearExpediente()
    {
        if (crearExpMenuItem == null)
        {
            crearExpMenuItem = new JMenuItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.crearExpMenuItem"));
            crearExpMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    crearExpMenuItemActionPerformed();
                }
            });
        }
        return crearExpMenuItem;
    }

    /**
     * This method initializes jMenuItemConsultarExpediente
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemConsultarExpediente()
    {
        if (gestionExpMenuItem == null)
        {
            gestionExpMenuItem = new JMenuItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.gestionExpMenuItem"));
            gestionExpMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    final JFrame desktop= (JFrame)MainCatastro.this;
                    final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);                    
                    progressDialog.setTitle("TaskMonitorDialog.Wait");
                    progressDialog.addComponentListener(new ComponentAdapter()
                    {
                        public void componentShown(ComponentEvent e)
                        {
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    try
                                    {
                                        progressDialog.report(I18N.get("RegistroExpedientes",
                                        "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                                        try
                                        {
                                            expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo);
                                        }
                                        catch(Exception exp)
                                        {
                                            exp.printStackTrace();
                                        }
                                        muestraBuscarExpedientesUsuario(expUsuario);
                                    }
                                    catch(Exception e)
                                    {
                                        logger.error("Error ", e);
                                        ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                        return;
                                    }
                                    finally
                                    {
                                        progressDialog.setVisible(false);
                                        progressDialog.dispose();
                                    }
                                }
                          }).start();
                      }
                   });
                   GUIUtil.centreOnWindow(progressDialog);
                   progressDialog.setVisible(true);
                }
            });
        }
        return gestionExpMenuItem;
    }

    private JMenuItem getJMenuItemModificarExpediente()
    {
        if (jMenuItemModificarExpediente == null)
        {
            jMenuItemModificarExpediente = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.modificarexpediente"));
            jMenuItemModificarExpediente.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    final JFrame desktop= (JFrame)MainCatastro.this;
                    final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
                    progressDialog.setTitle("TaskMonitorDialog.Wait");
                    progressDialog.addComponentListener(new ComponentAdapter()
                    {
                        public void componentShown(ComponentEvent e)
                        {
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    try
                                    {
                                        progressDialog.report(I18N.get("RegistroExpedientes",
                                        "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                                        try
                                        {
                                            expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo);
                                        }
                                        catch(Exception exp)
                                        {
                                            exp.printStackTrace();
                                        }
                                        muestraBuscarExpedientesUsuario(expUsuario);
                                    }
                                    catch(Exception e)
                                    {
                                        logger.error("Error ", e);
                                        ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                        return;
                                    }
                                    finally
                                    {
                                        progressDialog.setVisible(false);
                                        progressDialog.dispose();
                                    }
                                }
                          }).start();
                      }
                   });
                   GUIUtil.centreOnWindow(progressDialog);
                   progressDialog.setVisible(true);                    
                }
            });
        }
        return jMenuItemModificarExpediente;
    }

    /**
     * This method initializes jMenuItemValidarParcelas
     *
     * @return javax.swing.JMenu
     */
    private JMenuItem getJMenuItemValidarParcelas()
    {
        if (jMenuItemValidarParcelas == null)
        {             
            jMenuItemValidarParcelas = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.validarparcelas"));
            jMenuItemValidarParcelas.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    muestraValidarParcelas();
                }
            });
            
        }
        return jMenuItemValidarParcelas;
    }

    private void muestraValidarParcelas()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final FileValidationPanel fileValidation= new FileValidationPanel();
        llamadaAInternalFrame(encapsulaEnJInternalFrame(fileValidation, null),
                false, true);
        
        JButton linkBotonVerExpediente = fileValidation.getJButtonVerExpediente();
        linkBotonVerExpediente.setActionCommand("VerExpedienteMain");
        linkBotonVerExpediente.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = fileValidation.getExpedienteSeleccionado();
                
                //Mira si tiene permisos para el expediente seleccionado, recogiendo los 
                //expedientes para los que sí que tiene
                try
                {
                    expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo);
                    
                    int index = expUsuario.indexOf(exp);
                    if (index>=0)
                        exp = (Expediente)expUsuario.get(index);                        
                    else
                        System.out.println ("Usuario sin permisos");
                    
                    expUsuario = null;                    
                    
                    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
                    final Expediente expediente = exp;

                    progressDialog.setTitle("TaskMonitorDialog.Wait");
                    progressDialog.report(I18N.get("Expedientes","CargandoPantallaDeGestionDeExpedientes"));
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
                                        
                                        GestionExpedientePanel g = new GestionExpedientePanel(expediente, true);
                                        openComponent(g, g.getJPanelBotones(), true, false);                                        
                                        
                                    } 
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    } 
                                    finally
                                    {
                                        progressDialog.setVisible(false);
                                        progressDialog.dispose();
                                    }
                                }
                                    }).start();
                        }
                            });
                    GUIUtil.centreOnWindow(progressDialog);
                    progressDialog.setVisible(true);
                    
                                        
                    if (desktopPane.getAllFrames().length!=0 &&
                            desktopPane.getAllFrames()[desktopPane.getAllFrames().length-1] 
                                                   instanceof JInternalFrame)
                    {
                        ((JInternalFrame)desktopPane.getAllFrames()
                                [desktopPane.getAllFrames().length-1]).setSelected(true);                       
                        ((JInternalFrame)desktopPane.getAllFrames()[desktopPane.getAllFrames().length-1]).toFront();
                        
                        repaint();
                    }
                    
                    
                } catch (Exception e)
                {
                    
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * This method initializes jMenuItemConsultarIntercambios
     *
     * @return javax.swing.JMenu
     */
    private JMenuItem getJMenuItemConsultarIntercambios()
    {
        if (jMenuItemConsultarIntercambios == null)
        {
            jMenuItemConsultarIntercambios = new JMenuItem(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.consultarHistoricoMenuItem"));
            jMenuItemConsultarIntercambios.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    consultarHistoricoMenuItemActionPerformed();
                }
             });
        }
        return jMenuItemConsultarIntercambios;
    }

    /**
     * This method initializes jMenuFicherosMunicipios
     *
     * @return javax.swing.JMenu
     */
    private JMenuItem getJMenuFicherosMunicipios()
    {
        if (jMenuItemFicherosMunicipios == null)
        {
            jMenuItemFicherosMunicipios = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.ficherosmunicipios"));
            jMenuItemFicherosMunicipios.setEnabled(false);
            jMenuItemFicherosMunicipios.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarMunicipios(),
                            new MostrarMunicipios()}, false);
                }
                    });
        }
        return jMenuItemFicherosMunicipios;
    }

    /**
     * This method initializes jMenuItemPonenciaValores
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemPonenciaValores()
    {
        if (jMenuItemPonenciaValores == null)
        {
            jMenuItemPonenciaValores = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.ponenciavalores"));
            jMenuItemPonenciaValores.setEnabled(true);
            jMenuItemPonenciaValores.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarPonencia(),
                            new MostrarPonencia()}, false);
                }
                    });
        }
        return jMenuItemPonenciaValores;
    }
    
    /**
     * This method initializes jMenuItemImportarSubparcelas
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemImportarSubparcelas()
    {
        if (jMenuItemImportarSubparcelas  == null)
        {
        	jMenuItemImportarSubparcelas = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.importarsubparcelas"));
        	jMenuItemImportarSubparcelas.setEnabled(true);
        	jMenuItemImportarSubparcelas.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new GeopistaSeleccionarFicheroSubParcelas(),
                            new GeopistaMostrarSubParcelas()}, false);
                }
                    });
        }
        return jMenuItemImportarSubparcelas;
    }
    
    /**
     * This method initializes jMenuItemImportarSubparcelas
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemImportFXCC()
    {
    	 if (jMenuItemImportFXCC  == null)
         {
    		 jMenuItemImportFXCC = new JMenu(I18N.get("ModuloCatastral","modulocatastral.submenu.importarFXCC"));
    		 jMenuItemImportFXCC.setEnabled(true);

    		 jMenuItemImportFXCC.add(getJMenuItemImportarFXCC());
    		 jMenuItemImportFXCC.add(getJMenuItemImportarFXCCMasivo());
         	
         }
    	return jMenuItemImportFXCC;
    }
    
    /**
     * This method initializes jMenuItemImportarSubparcelas
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemImportarFXCC()
    {
        if (jMenuItemImportarFXCC  == null)
        {
        	jMenuItemImportarFXCC = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.importarFXCC"));
        	jMenuItemImportarFXCC.setEnabled(true);
        	jMenuItemImportarFXCC.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                	
                	ImportarFXCC panelImportarFXCC  = new ImportarFXCC();
                    openWizard (new WizardPanel[] {
                    		panelImportarFXCC, 
                    		new ImportarSeleccionarFicheroFXCC(),
                    		new EstadoImportarFXCC()}, true);
                    
                    panelImportarFXCC.getPanelImportarFXCC().cargarMapa();
                }
                    });
        }
        return jMenuItemImportarFXCC;
    }
    
    /**
     * This method initializes jMenuItemImportarSubparcelas
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemImportarFXCCMasivo()
    {
        if (jMenuItemImportarFXCCMasivo  == null)
        {
        	jMenuItemImportarFXCCMasivo = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.importarMasivoFXCC"));
        	jMenuItemImportarFXCCMasivo.setEnabled(true);
        	jMenuItemImportarFXCCMasivo.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                    		new ImportarFXCCMasivo(),
                    		new EstadoImportarFXCCMasivo()}, false);
                }
                    });
        }
        return jMenuItemImportarFXCCMasivo;
    }
    
   
    
    /**
     * This method initializes jMenuItemImportarSubparcelas
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemImportarDatosCatastro()
    {
        if (jMenuItemImportarDatosCatastro  == null)
        {
        	jMenuItemImportarDatosCatastro = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.importardatoscatastro"));
        	jMenuItemImportarDatosCatastro.setEnabled(true);
        	jMenuItemImportarDatosCatastro.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                    		new GeopistaSeleccionarFicheroParcelas(),
                    		new GeopistaMostrarParcelas()}, false);
                   
                }
                    });
        }
        return jMenuItemImportarDatosCatastro;
    }

    /**
     * This method initializes jMenuItemPadronCatastral
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemPadronCatastral()
    {
        if (jMenuItemPadronCatastral == null)
        {
            jMenuItemPadronCatastral = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.padroncatastral"));
            jMenuItemPadronCatastral.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarPadronMunicipal(),
                            new MostrarPadronMunicipal()}, false);
                }
                    });
        }
        return jMenuItemPadronCatastral;
    }

    /**
     * This method initializes jMenuItemFinSalida
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFinSalida()
    {
        if (jMenuItemFinSalida == null)
        {
            jMenuItemFinSalida = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finsalida"));
            jMenuItemFinSalida.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                	Boolean expSitFinales = true;
                	AppContext.getApplicationContext().getBlackboard().put("expSitFinales", expSitFinales);
                	
                    openWizard (new WizardPanel[] {
                            new ImportarFINSalida(),
                            new MostrarFINSalida(),
                            new ValidarFichero()}, false);
                }
                    });
        }
        return jMenuItemFinSalida;
    }

    /**
     * This method initializes jMenuItemFinRetornoMasivo
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFinRetornoMasivo()
    {
        if (jMenuItemFinRetornoMasivo == null)
        {
            jMenuItemFinRetornoMasivo = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finretorno"));
            jMenuItemFinRetornoMasivo.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openWizard (new WizardPanel[] {
                            new ImportarFINRetornoMasivo(),
                            new MostrarFINRetornoMasivo(),
                            new ValidarFichero()}, false);
                }
                    });
        }
        return jMenuItemFinRetornoMasivo;
    }

    /**
     * This method initializes jMenuItemFicheroIntercambioInfoTitularidad
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoTitularidad()
    {
        if (jMenuItemFicheroIntercambioInfoTitularidad == null)
        {
            jMenuItemFicheroIntercambioInfoTitularidad = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infotitularidad"));
            jMenuItemFicheroIntercambioInfoTitularidad.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.infotitularidad"),
                            new WizardPanel[]
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infotitularidad")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infotitularidad"))
                                            });
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoTitularidad;
    }

    /**
     * This method initializes jMenuItemFicheroIntercambioInfoCatastral
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoCatastral()
    {
        if (jMenuItemFicheroIntercambioInfoCatastral == null)
        {
            jMenuItemFicheroIntercambioInfoCatastral = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infocatastral"));
            jMenuItemFicheroIntercambioInfoCatastral.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.infocatastral"),
                            new WizardPanel[]
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infocatastral")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.infocatastral"))
                                            });
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoCatastral;
    }

    /**
     * This method initializes jMenuItemFicheroIntercambioInfoCatastralRetorno
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFicheroIntercambioInfoCatastralRetorno()
    {
        if (jMenuItemFicheroIntercambioInfoCatastralRetorno == null)
        {
            jMenuItemFicheroIntercambioInfoCatastralRetorno = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.inforetorno"));
            jMenuItemFicheroIntercambioInfoCatastralRetorno.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    openModalWizard(I18N.get("ModuloCatastral","dialog.titulo.inforetorno"),
                            new WizardPanel[]
                                            {
                            new ImportarInfoIntercambio(I18N.get("ModuloCatastral","panel.titulo.inforetorno")),
                            new MostrarProgresoIntercambio(I18N.get("ModuloCatastral","panel.titulo.inforetorno"))
                                            });
                }
                    });
        }
        return jMenuItemFicheroIntercambioInfoCatastralRetorno;
    }

    /**
     * This method initializes jMenuItemAsociarInfoGrafica
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAsociarInfoGrafica()
    {
        if (jMenuItemAsociarInfoGrafica == null)
        {
            jMenuItemAsociarInfoGrafica = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.infografica"));
            jMenuItemAsociarInfoGrafica.setEnabled(false);
            jMenuItemAsociarInfoGrafica.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {
                    ScreenComponent sc = new ScreenComponent();
                    ImportarInfoGraficaPanel g =
                        new ImportarInfoGraficaPanel(new Expediente());
                    sc.addComponent(g);
                    sc.setFillerPanel(g.getJPanelBotones());

                    openModalComponent(I18N.get("ModuloCatastral","dialog.titulo.infografica"), sc);

                 
                }
                    });
        }
        return jMenuItemAsociarInfoGrafica;
    }

    /**
     * This method initializes jMenuItemFinEntradaRelativo
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemFinEntradaRelativo()
    {
        if (jMenuItemFinEntradaRelativo == null)
        {
            jMenuItemFinEntradaRelativo = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.finentrada"));
            jMenuItemFinEntradaRelativo.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    final JFrame desktop= (JFrame)MainCatastro.this;
                    final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);                    
                    progressDialog.setTitle("TaskMonitorDialog.Wait");
                    progressDialog.addComponentListener(new ComponentAdapter()
                    {
                        public void componentShown(ComponentEvent e)
                        {
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    try
                                    {
                                        progressDialog.report(I18N.get("RegistroExpedientes",
                                        "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                                        
                                        ArrayList listaEstados = new ArrayList();
                                        listaEstados.add(ConstantesCatastro.ESTADO_MODIFICADO);
                                        listaEstados.add(ConstantesCatastro.ESTADO_FINALIZADO);
                                        try
                                        {
                                            expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuarioFinEntradaMasivo(listaEstados, ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo);
                                        }
                                        catch(Exception exp)
                                        {
                                            exp.printStackTrace();
                                        }
                                        muestraBuscarExpedientesUsuarioExportacionMasiva(expUsuario);
                                    }
                                    catch(Exception e)
                                    {
                                        logger.error("Error ", e);
                                        ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                        return;
                                    }
                                    finally
                                    {
                                        progressDialog.setVisible(false);
                                        progressDialog.dispose();
                                    }
                                }
                          }).start();
                      }
                   });
                   GUIUtil.centreOnWindow(progressDialog);
                   progressDialog.setVisible(true);
                
                	
               	
                   //mostrarExportacionMasiva();

                   
               }
          });
       }
       return jMenuItemFinEntradaRelativo;
   }
    
    private JMenuItem getJMenuItemValidarEsquema()
    {
    	if (jMenuItemValidarEsquema == null)
    	{
    		jMenuItemValidarEsquema = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.validar"));
    		jMenuItemValidarEsquema.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				openWizard (new WizardPanel[] {
    						new ValidarEsquemaPanel()}, false);
    			}
    		});
    	}
    	return jMenuItemValidarEsquema;
    }

    /**
     * This method initializes jMenuItemAcercaDe
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemAcercaDe()
    {
        if (jMenuItemAcercaDe == null)
        {
            jMenuItemAcercaDe = new JMenuItem(I18N.get("ModuloCatastral","modulocatastral.submenu.acercade"));
            jMenuItemAcercaDe.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt){
                	aboutJMenuItemActionPerformed();
                }
            });
        }
        return jMenuItemAcercaDe;
    }

    private void aboutJMenuItemActionPerformed() {//GEN-FIRST:event_acercaJMenuItemActionPerformed

   	 if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        CAcercaDeJDialog acercaDe= new CAcercaDeJDialog(this, true);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        acercaDe.setSize(255, 240);
        acercaDe.setLocation(d.width / 2 - acercaDe.getSize().width / 2, d.height / 2 - acercaDe.getSize().height / 2);
        acercaDe.setResizable(false);
        acercaDe.show();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
 	}//GEN-LAST:event_acercaJMenuItemActionPerformed
    
    
    private JMenuItem getJMenuItemAyuda()
    {
        if (contenidoAyudaMenuItem == null)
        {
            contenidoAyudaMenuItem = new JMenuItem(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.MainCatastro.contenidoAyudaMenuItem"));
            contenidoAyudaMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    mostrarAyudaActionPerformed();
                }
            });
        }
        return contenidoAyudaMenuItem;
    }

    private JMenuItem getJMenuItemEspanol()
    {
        if (espanolMenuItem == null)
        {
            espanolMenuItem = new JMenuItem("Castellano");
            espanolMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    espanolMenuItemActionPerformed();
                }
            });
        }
        return espanolMenuItem;
    }

    private JMenuItem getJMenuItemCatalan()
    {
        if (catalanMenuItem == null)
        {
            catalanMenuItem = new JMenuItem("Catalan");
            catalanMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    catalanMenuItemActionPerformed();
                }
            });
        }
        return catalanMenuItem;
    }

    private JMenuItem getJMenuItemValenciano()
    {
        if (valencianoMenuItem == null)
        {
            valencianoMenuItem = new JMenuItem("Valenciano");
            valencianoMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    valencianoMenuItemActionPerformed();
                }
            });
        }
        return valencianoMenuItem;
    }

    private JMenuItem getJMenuItemGallego()
    {
        if (gallegoMenuItem == null)
        {
            gallegoMenuItem = new JMenuItem("Gallego");
            gallegoMenuItem.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    gallegoMenuItemActionPerformed();
                }
            });
        }
        return gallegoMenuItem;
    }


    private JMenuItem getJMenuItemActualizacionesEnviosPeriodicos()
    {
        if(jMenuItemActualizacionesEnviosPeriodicos==null)
        {
            jMenuItemActualizacionesEnviosPeriodicos = new JMenuItem();
            jMenuItemActualizacionesEnviosPeriodicos.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.actualizacionYEnviosPeriodicosMenuItem"));
            jMenuItemActualizacionesEnviosPeriodicos.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    jMenuItemActualizacionesEnviosPeriodicosActionPerformed();
                }
            });
        }
        return jMenuItemActualizacionesEnviosPeriodicos;
    }

    private JMenuItem getJMenuItemConfigModosConvenion()
    {
        if(jMenuItemConfigModosConvenion==null)
        {
            jMenuItemConfigModosConvenion = new JMenuItem();
            jMenuItemConfigModosConvenion.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.modosYConveniosMenuItem"));
            jMenuItemConfigModosConvenion.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    jMenuItemConfigModosConvenionActionPerformed();
                }
            });
        }
        return jMenuItemConfigModosConvenion;
    }

    public JMenuItem getJMenuItemAsociarCertificado() {
    	 if(jMenuItemAsociarCertificado==null)
         {
    		 jMenuItemAsociarCertificado = new JMenuItem();
    		 jMenuItemAsociarCertificado.setText(I18N.get("RegistroExpedientes",
                         "Catastro.RegistroExpedientes.MainCatastro.AsociarCertificadoMenuItem"));
    		 jMenuItemAsociarCertificado.addActionListener(new ActionListener()
             {
                 public void actionPerformed(ActionEvent e)
                 {
 				    mostrarAsociarCertificado();
                 }
             });
         }
		return jMenuItemAsociarCertificado;
	}
    public void setjMenuItemAsociarCertificado(JMenuItem jMenuItemAsociarCertificado) {
		this.jMenuItemAsociarCertificado = jMenuItemAsociarCertificado;
	}
	
    public JMenuItem getJMenuItemImportarCertificado() {
   	 if(jMenuItemImportarCertificado==null)
        {
   		jMenuItemImportarCertificado = new JMenuItem();
   		jMenuItemImportarCertificado.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.ImportarCertificadoMenuItem"));
   		jMenuItemImportarCertificado.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    mostrarImportarCertificado();
                }
            });
        }
		return jMenuItemImportarCertificado;
	}
    
	
	
	public void setjMenuItemImportarCertificado(JMenuItem jMenuItemAsociarCertificado) {
		this.jMenuItemImportarCertificado = jMenuItemAsociarCertificado;
	}
	
    private JMenuItem getJMenuItemAsociarUsuariosExpedientes()
    {
        if(jMenuItemAsociarUsuariosExpedientes==null)
        {
            jMenuItemAsociarUsuariosExpedientes = new JMenuItem();
            jMenuItemAsociarUsuariosExpedientes.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.AsociarExpedientesUsuariosMenuItem"));
            jMenuItemAsociarUsuariosExpedientes.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
				    mostrarAsociarUsuariosExpediente();
                }
            });
        }
        return jMenuItemAsociarUsuariosExpedientes;
    }

    /**
     * Las acciones a realizar cuando se pulsa el boton de Gestion de Expediente, o cuando el flujo del programa hace
     * que se muestre esta pantalla. Se crea un nuevo objeto de gestion de expediente. Se linkean los botones que
     * pueden llevar a otra pantalla segun el flujo del programa y se llama a mostrarInternalFrame para que se muestre.
     * Los valores de maxTam y unica son true, se maximiza, y true, no puede haber mas de una ventana cuando
     * esta se inicia.
     *
     * @param exp El expediente con el que se va a trabajar.
     */
    private void gestionExpMenuItemActionPerformed(Expediente exp)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final GestionDeExpedientes gest= new GestionDeExpedientes(this, exp);

        gest.addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                UtilRegistroExp.menuBarSetEnabled(true, MainCatastro.this);
                siguienteFrame(null,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_CERRAR,gest);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        JButton linkVerEstadoBotonGestionExpPanel = gest.getVerEstadoButton();
        linkVerEstadoBotonGestionExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = gest.getExpediente();
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_VER_ESTADO,gest);
            }
        });

        JButton linkAsociarParcelasBotonGestionExpPanel = gest.getAsociarParcelasYBienesInmuebleButton();
        linkAsociarParcelasBotonGestionExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = gest.getExpediente();
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ASOCIAR_PARCELA_BIEN_INMUEBLE,gest);
            }
        });

        JButton linkModificarBotonGestionExpPanel = gest.getModificarInfCatastralButton();
        linkModificarBotonGestionExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = gest.getExpediente();
                if(exp.isTipoTramitaExpSitFinales()){
                	//expediente de situaciones finales
                	aplicacion.getBlackboard().put("expSitFinales",true);
                }
                else{
                	//expediente orientado a variaciones
                	aplicacion.getBlackboard().put("expSitFinales",false);
                }
                aplicacion.getBlackboard().put("catastroTemporal", true);
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_INF_CATASTRAL,gest);
            }
        });
        
        
        JButton linkAsociarDatosGraficosBotonGestionExpPanel = gest.getAsociarDatosGraficosButton();
        linkAsociarDatosGraficosBotonGestionExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	Expediente exp = gest.getExpediente();
            	AppContext.getApplicationContext().getBlackboard().put("catastroTemporal", true);
            	if(exp.isTipoTramitaExpSitFinales()){
            		//expediente de situaciones finales
            		aplicacion.getBlackboard().put("expSitFinales",true);
            	}
            	else{
            		//expediente orientado a variaciones
            		aplicacion.getBlackboard().put("expSitFinales",false);
                }
                
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_ASOCIAR_DATOS_GRAFICOS,gest);
            }
        });
     
        
        JButton linkAsociarBotonGestionExpPanel = gest.getAsocExp_ConsultaCatastroButton();
        linkAsociarBotonGestionExpPanel.addActionListener(new ActionListener()
                {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                final Expediente expediente = gest.getExpediente();
                
                
 
                try {
                	
                	HashMap certificadoHashMap = (HashMap) aplicacion.getBlackboard().get(ConstantesCatastro.CERTIFICADO_DATOS);
            		
            		if(certificadoHashMap == null){
            			// no se ha establecido el certificado
            			 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "No se han establecido los datos del Certificado");
            		}
            		else{
	                	Object datosWsResp = null;
	                	if(expediente.getIdEstado() == ConstantesCatastro.ESTADO_REGISTRADO){
	                		
	                		Date fechaMovimiento = new Date();
	                		expediente.setFechaMovimiento(fechaMovimiento);
	                		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
	                		expediente.setHoraMovimiento(formato.format(fechaMovimiento));
	                		
	                		String nombreSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_NOMBRE_SOLICITANTE);
	                		String nifSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_DNI_SOLICITANTE);
	                    	CatastroWS catastrows = new CatastroWS();
	                        String XML = catastrows.buildCreacionExpedienteRequest(expediente, 
	                        		nombreSolicitante, nifSolicitante);
	                        System.out.println(XML);                           
	                        TestClient test = new TestClient(certificadoHashMap);
	                        datosWsResp = test.asociacionExpediente(XML, aplicacion, expediente);
	 	                       
                    		if( datosWsResp instanceof DatosWSResponseBean){
                    			DatosWSResponseBean datosWsResponse = (DatosWSResponseBean)datosWsResp;
	 	                     	if(datosWsResponse != null){
			 	                    GestionResponseWS gestionResponseWS = new GestionResponseWS();
			 	                    String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, aplicacion);
				                 	                    
			 	                    MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
			 	                    messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
			 	                    messageDialog.show();	
	                    	 
		                 	                  
			 	                    if (datosWsResponse.getRespuesta().getLstUnidadError() == null ||
		 	                    		datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
			 	                    	//actualizamos la Base de datos
	 	                	   
		 	                    		datosWsResponse.getRespuesta().getExpediente().setIdEstado(expediente.getIdEstado());
		 	                    		expediente.setAnnoExpedienteGerencia(datosWsResponse.getRespuesta().getExpediente().getAnnoExpedienteGerencia());
		 	                    		expediente.setReferenciaExpedienteGerencia(datosWsResponse.getRespuesta().getExpediente().getReferenciaExpedienteGerencia());
			     	                	 expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(datosWsResponse.getRespuesta().getExpediente().getCodigoEntidadRegistroDGCOrigenAlteracion());
			     	                	 expediente.setAnnoExpedienteAdminOrigenAlteracion(datosWsResponse.getRespuesta().getExpediente().getAnnoExpedienteAdminOrigenAlteracion());
			     	                	 expediente.setReferenciaExpedienteAdminOrigen(datosWsResponse.getRespuesta().getExpediente().getReferenciaExpedienteAdminOrigen());
			     	                	 expediente.setCodigoDescriptivoAlteracion(datosWsResponse.getRespuesta().getExpediente().getCodigoDescriptivoAlteracion());
		     	                	 
			 	                    	 siguienteFrame(expediente,
			 	                    			 ConstantesCatastro.FRAME_ORIGEN_ASOCIADO_EXP,gest);
			 	                    	 
			 	                    	 gest.actualizarBotonAsociarExpediente();
			 	                    }     
	 	                     	}	
                    		}
	 	                    else{
	 	                    	DatosErrorWS datosError = (DatosErrorWS)datosWsResp;
	 	                    	
	 	                    	GestionResponseWS gestionResponseWS = new GestionResponseWS();
	 	                    	String mensaje = gestionResponseWS.gestionErrorComunicacion(datosError);
				                 	
	 	                    	 MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
			 	                 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje.toString());
			 	                 messageDialog.show();	
	 	                    }

	                	}
	                	else {
	                		String nombreSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_NOMBRE_SOLICITANTE);
	                		String nifSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_DNI_SOLICITANTE);
	                		
	                		CatastroWS catastrows = new CatastroWS();
	                		
	                		String XML = XML = catastrows.buildConsultaCatastroRequest(expediente,
	                				nombreSolicitante, nifSolicitante);
	                		System.out.println(XML);
	                		TestClient test = new TestClient(certificadoHashMap);
	                		datosWsResp = test.consultaCatastro(XML, aplicacion, expediente);
	                		if( datosWsResp instanceof DatosWSResponseBean){
	                			DatosWSResponseBean datosWsResponse = (DatosWSResponseBean)datosWsResp;
		                		if(datosWsResponse != null){
			                	    GestionResponseWS gestionResponseWS = new GestionResponseWS();
			    	                String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, aplicacion);
			    	                    
			    	                MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.consultaCatastro.title");
			    	                messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
			    	                messageDialog.show();	
		                       	 
		    	                
			    	                if (datosWsResponse.getRespuesta().getLstUnidadError() == null ||
			  	                		   datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
			    	                	// la consulta ha ido correctamente
				 	                    	   	                	
			    	                	datosWsResponse.getRespuesta().getUdsa().getExpediente().setIdEstado(expediente.getIdEstado());
			    	                	
			    	                	if(datosWsResponse.getControl().getLstIdentificadoresDialogo() != null &&
			    	                			!datosWsResponse.getControl().getLstIdentificadoresDialogo().isEmpty()){
			    	                		//actualizamos la Base de datos con los identificadores de dialogo  	
			    	                		updateIdentificadoresDialogoExpediente(datosWsResponse.getControl().getLstIdentificadoresDialogo(), expediente);
			    	                		
			    	                		if(expediente.getListaReferencias() != null && !expediente.getListaReferencias().isEmpty()){
			    	                			
			    	                			for(int i=0; i<expediente.getListaReferencias().size(); i++){
			    	                				Object obj = expediente.getListaReferencias().get(i);
			    	                				if(obj instanceof FincaCatastro){
			    	                					
			    	                					for(int j=0; j<datosWsResponse.getControl().getLstIdentificadoresDialogo().size(); j++){
			    	                						IdentificadorDialogo objIdenDialogo = (IdentificadorDialogo)datosWsResponse.getControl().getLstIdentificadoresDialogo().get(j);
			    	                						
			    	                						if(objIdenDialogo.getFincaBien() instanceof FincaCatastro &&
			    	                								((FincaCatastro)obj).getRefFinca().getRefCatastral().
			    	                									equals(((FincaCatastro)objIdenDialogo.getFincaBien()).getRefFinca().getRefCatastral())){
			    	                							
			    	                							((FincaCatastro) obj).setIdentificadorDialogo(objIdenDialogo.getIdentificadorDialogo());
			    	                							((FincaCatastro) obj).setActualizadoOVC(false);
			    	                							objIdenDialogo.setFincaCatastro(true);
			    	                							objIdenDialogo.setBienCatastro(false);
	
			    	                						}
			    	                						
			    	                					}
			    	                			
			    	                				}
			    	                				else if(obj instanceof BienInmuebleCatastro){
			    	                					
			    	                					for(int j=0; j<datosWsResponse.getControl().getLstIdentificadoresDialogo().size(); j++){
			    	                						IdentificadorDialogo objIdenDialogo = (IdentificadorDialogo)datosWsResponse.getControl().getLstIdentificadoresDialogo().get(j);
			    	                						
			    	                						if(objIdenDialogo.getFincaBien() instanceof BienInmuebleCatastro){
			    	                							BienInmuebleCatastro bien = (BienInmuebleCatastro)obj;
			    	                							String idBienInmueble = bien.getIdBienInmueble().getParcelaCatastral().getRefCatastral()+
									    	                							 bien.getIdBienInmueble().getNumCargo() +
									    	                							 bien.getIdBienInmueble().getDigControl1() +
									    	                							 bien.getIdBienInmueble().getDigControl2();
			    	                							String idBi= ((BienInmuebleCatastro)objIdenDialogo.getFincaBien()).getIdBienInmueble().getIdBienInmueble();
			    	                							if(idBienInmueble.equals(idBi)){
			    	                								
			    	                								((BienInmuebleCatastro) obj).setIdentificadorDialogo(objIdenDialogo.getIdentificadorDialogo());
			    	                								((BienInmuebleCatastro) obj).setActualizadoOVC(false);
			    	                							}
			    	                						}
			    	                						
			    	                						objIdenDialogo.setFincaCatastro(false);
		    	                							objIdenDialogo.setBienCatastro(true);
			    	                					}
			    	                				}
	
			    	                			}
			    	                		}
			 
			    	                	}  
	
			    	                	siguienteFrame(expediente,ConstantesCatastro.FRAME_ORIGEN_OBTENER_INF_CATASTRO,gest);
			    	                	
			    	                	//se actualiza la informacion catastral
			    	                	ImportacionOperations oper = new ImportacionOperations();
			    	                	  for(int i=0; i<datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().size(); i++){
				                    		  oper.insertarDatosSalida((UnidadDatosIntercambio)datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().get(i),
					                    			  false,datosWsResponse.getRespuesta().getUdsa().getExpediente());
				                    	  }
	
			                 		}  
		                		}
	                		}
	                		else{
	                			DatosErrorWS datosError = (DatosErrorWS)datosWsResp;
	 	                    	
	 	                    	GestionResponseWS gestionResponseWS = new GestionResponseWS();
	 	                    	String mensaje = gestionResponseWS.gestionErrorComunicacion(datosError);
				                 	
	 	                    	 MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
			 	                 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje.toString());
			 	                 messageDialog.show();	
	                		}
	                	}
            		}
   	
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println(e.toString());
					logger.error(e.getMessage());
					
				}

            }
                });
        
        
        JButton linkActualizaCatastroBotonGestionExpPanel = gest.getActualizacionCatastralButton();
        linkActualizaCatastroBotonGestionExpPanel.addActionListener(new ActionListener()
                {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	final Expediente expediente = gest.getExpediente();
               
                
            	try {
            		
            		HashMap certificadoHashMap = (HashMap) aplicacion.getBlackboard().get(ConstantesCatastro.CERTIFICADO_DATOS);
            		
            		if(certificadoHashMap == null){
            			// no se ha establecido el certificado
            			 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "No se han establecido los datos del Certificado");
            		}
            		else{
            			AppContext.getApplicationContext().getBlackboard().put("catastroTemporal", false);
	            		Object datosWsResp = null;
	            		
	            		Date fechaMovimiento = new Date();
	            		expediente.setFechaMovimiento(fechaMovimiento);
	            		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
	            		expediente.setHoraMovimiento(formato.format(fechaMovimiento));
	            		
	            		String nombreSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_NOMBRE_SOLICITANTE);
                		String nifSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_DNI_SOLICITANTE);
                		
	            		CatastroWS catastrows = new CatastroWS();
	               	 	String XML = catastrows.buildActualizaCatastroRequest(expediente, nombreSolicitante, nifSolicitante);
	               	 	System.out.println(XML);
	                     TestClient test = new TestClient(certificadoHashMap);
	                     datosWsResp = test.actualizacionCatastro(XML, aplicacion, expediente);
	                    
	                     if( datosWsResp instanceof DatosWSResponseBean){
	                    	 DatosWSResponseBean datosWsResponse = (DatosWSResponseBean)datosWsResp;
		                     if(datosWsResponse != null){
			                	  GestionResponseWS gestionResponseWS = new GestionResponseWS();
			                      String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, aplicacion);
			                      
			                      MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.actualizaciónCatastro.title");
			                      messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
			                      messageDialog.show();	
	
			                      if (datosWsResponse.getRespuesta().getLstUnidadError() == null ||
			  	                		   datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
			    	                	// la consulta ha ido correctamente
				 	                    	   
			                    	 Expediente exp = gest.getExpediente();
			                    	 siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_ACTUALIZAR_CATASTRO,gest);
		
			                     }
			                      else{
			                    	  boolean isInfoCatModif = false;
			                    	  for(int i=0; i<datosWsResponse.getRespuesta().getLstUnidadError().size(); i++){
			                    		  UnidadErrorElementoWSBean unidadError = (UnidadErrorElementoWSBean)datosWsResponse.getRespuesta().getLstUnidadError().get(i);
			                    		  for(int j=0; j<unidadError.getLstErrores().size(); j++){
			                    			  ErrorWSBean errorWS = (ErrorWSBean)unidadError.getLstErrores().get(j);
			                    			  if(errorWS!= null && errorWS.getCodigo() != null && errorWS.getCodigo().equals(ErrorWSBean.WS_ERROR_REFERENCIA_NO_ACTUALIZADA) ){
			                    				  isInfoCatModif = true;
			                    				  expediente.setIdEstado(ConstantesCatastro.ESTADO_RELLENADO);
			                    				  expediente.setFechaAlteracion(null);
			                    				  expediente.setExistenciaInformacionGrafica(null);
			                    				  
			                    				  
			                    				  if (unidadError.getIdentificador().getOrigen().getBienInmueble() != null){
			                    					  BienInmuebleCatastro bien = unidadError.getIdentificador().getOrigen().getBienInmueble();
			                    					  
			                    					  for(int h=0; h<expediente.getListaReferencias().size(); h++){
	
				                    					  if(expediente.getListaReferencias().get(h) instanceof BienInmuebleCatastro){
				                    						  
				                    						  if ( ((BienInmuebleCatastro)expediente.getListaReferencias().get(h)).getIdBienInmueble().getParcelaCatastral().getRefCatastral()
				                    								  	.equals(bien.getIdBienInmueble().getParcelaCatastral().getRefCatastral())){
				                    							  ((BienInmuebleCatastro)expediente.getListaReferencias().get(h)).setIdentificadorDialogo("");
				                    					  	}
				                    					  }
				                    				  }
			                    					  
			                    				  }
			                    				  
			                    				  if(unidadError.getIdentificador().getOrigen().getFinca() != null){
			                    					  FincaCatastro finca = unidadError.getIdentificador().getOrigen().getFinca();
			                    					  
			                    					  for(int h=0; h<expediente.getListaReferencias().size(); h++){
	
				                    					  if(expediente.getListaReferencias().get(h) instanceof FincaCatastro){
				                    						  if( ((FincaCatastro)expediente.getListaReferencias().get(h)).getRefFinca().getRefCatastral()
				                    								  .equals(finca.getRefFinca().getRefCatastral())   ){
				                    							  ((FincaCatastro)expediente.getListaReferencias().get(h)).setIdentificadorDialogo("");
				                    							  ((FincaCatastro)expediente.getListaReferencias().get(h)).setActualizadoOVC(false);
				                    						  }
				                    					  }
				                    					 
				                    				  }
			                    				  }
			                    				
			    		                    	  updateExpedienteEnBD(expediente);
			                    				  //siguienteFrame(expediente,ConstantesCatastro.FRAME_ORIGEN_ACTUALIZAR_CATASTRO,gest);
			                    			  }
			                    		  }
	
			                    	  }
			                    	  
			                    	  if (isInfoCatModif){
			                    		  siguienteFrame(expediente,ConstantesCatastro.FRAME_ORIGEN_ACTUALIZAR_CATASTRO,gest);
			                    	  }
			                    	  
			                      }
		                     }
		                     
	                     }
	                     else{
	                    	 DatosErrorWS datosError = (DatosErrorWS)datosWsResp;
	 	                    	
 	                    	 GestionResponseWS gestionResponseWS = new GestionResponseWS();
 	                    	 String mensaje = gestionResponseWS.gestionErrorComunicacion(datosError);
			                 	
 	                    	 MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
		 	                 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje.toString());
		 	                 messageDialog.show();	
	                    	 
	                     }
            		}
            	}
            	catch (Exception e) {
					e.printStackTrace();
					System.err.println(e.toString());
					logger.error(e.getMessage());
					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
							I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion"), "Error",
							JOptionPane.ERROR_MESSAGE);

				}
            	
            }
                });
        
        
        
        JButton linkConsultaEstadoExpedienteBotonGestionExpPanel = gest.getConsultaEstadoExpedienteButton();
        linkConsultaEstadoExpedienteBotonGestionExpPanel.addActionListener(new ActionListener()
                {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	final Expediente expediente = gest.getExpediente();
            
            	try {
            		HashMap certificadoHashMap = (HashMap) aplicacion.getBlackboard().get(ConstantesCatastro.CERTIFICADO_DATOS);
            		
            		if(certificadoHashMap == null){
            			// no se ha establecido el certificado
            			 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "No se han establecido los datos del Certificado");
            		}
            		else{
	            		Object datosWsResp = null;
	            		
	            		Date fechaMovimiento = new Date();
	            		expediente.setFechaMovimiento(fechaMovimiento);
	            		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
	            		expediente.setHoraMovimiento(formato.format(fechaMovimiento));
	            		
	            		
	            		String nombreSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_NOMBRE_SOLICITANTE);
                		String nifSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_DNI_SOLICITANTE);
                		
	            		CatastroWS catastrows = new CatastroWS();
	               	 	String XML = catastrows.buildConsultaEstadoExpedienteRequest(expediente,  nombreSolicitante, nifSolicitante);
	               	 	System.out.println(XML);
	               	 	TestClient test = new TestClient(certificadoHashMap);
	               	 	datosWsResp = test.consultaEstadoExpediente(XML, aplicacion, expediente);
	                     
	                     if( datosWsResp instanceof DatosWSResponseBean){
	                    	 DatosWSResponseBean datosWsResponse = (DatosWSResponseBean)datosWsResp;
		                     if(datosWsResponse != null){
			                    
		                    	 //	se han producido errores
		                    	  GestionResponseWS gestionResponseWS = new GestionResponseWS();
		                          String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, aplicacion);
		                          
		                          MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.consultaEstado.title");
		                          messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
		                          messageDialog.show();	
			                     
		                          if (datosWsResponse.getRespuesta().getLstUnidadError() == null ||
			  	                		   datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
		                        	  
		                        	  if (datosWsResponse.getRespuesta().getUdsa().getExpediente().getTipoDeIntercambio().equals(ConstantesCatastro.EXP_FINALIZADO)){
			                    	 
				                    	 // se actualizan los datos de catastrales
				                    	 WSOperations wsOper = new WSOperations();
				                    	 
				                    	  for(int i=0; i<datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().size(); i++){
				                    		  wsOper.insertarDatosCatastrales((UnidadDatosIntercambio)datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().get(i));
				                    	  }
		//		                    	 wsOper.insertarDatosCatastrales(datosWsResponse.getRespuesta().getUdsa().getUnidadDatosIntercambio());
				                    	 
				                    	 Expediente exp = gest.getExpediente();
				                    	 
				                    	  //se modifica catastro_temporal
				                    	// actualizaCatastroTemporal(exp, ConstantesCatastro.tipoConvenio);
				                    	 
				                    	 //se actualiza el estado del expediente
				                         siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_CONSULTA_ESTADO,gest);
		                        	  }
			                     }	
		                     }
	                     }
	                     else{
	                    	 	DatosErrorWS datosError = (DatosErrorWS)datosWsResp;
	 	                    	
	 	                    	GestionResponseWS gestionResponseWS = new GestionResponseWS();
	 	                    	String mensaje = gestionResponseWS.gestionErrorComunicacion(datosError);
				                 	
	 	                    	 MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
			 	                 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje.toString());
			 	                 messageDialog.show();	
	                     }
            		}
            	}
            	catch (Exception e) {
					e.printStackTrace();
					System.err.println(e.toString());
					logger.error(e.getMessage());
					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
							I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion"), "Error",
							JOptionPane.ERROR_MESSAGE);

				}
            	
            }
                });
        
        
        
        
        JButton linkConstularBotonGestionExpPanel = gest.getModificarExpedienteButton();
        linkConstularBotonGestionExpPanel.addActionListener(new ActionListener()
                {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = gest.getExpediente();
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_MODIFICAR_DATOS_EXP,gest);
            }
                });

        JButton linkExportarFicheroBotonGestionExpPanel = gest.getExportarFicheroButton();
        linkExportarFicheroBotonGestionExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp = gest.getExpediente();
                siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_EXPORTAR_FICHERO,gest);
            }
        });

        llamadaAInternalFrame(gest, true, true);

        zoomMapa();
    }

    /**
     * Las acciones a realizar cuando se pulsa el boton de Crear Expediente, o cuando el flujo del programa hace
     * que se muestre esta pantalla. Se crea un nuevo objeto de crear expediente. Se linkean los botones que
     * pueden llevar a otra pantalla segun el flujo del programa y se llama a mostrarInternalFrame para que se muestre.
     *  Los valores de maxTam y unica son false, no se maximiza, y true, no puede haber mas de una ventana cuando
     * esta se inicia.
     */
    public void crearExpMenuItemActionPerformed()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final CrearExpediente crearExp= new CrearExpediente(this, desktopPane.getHeight());
       
        
        JButton linkBotonSiguienteCrearExpPanel = crearExp.getBotonSiguiente();
        linkBotonSiguienteCrearExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(crearExp.recopilarDatos())
                {	
                	// se guarda el modo de trabajo
                    // true -> Modo acoplado
                    // false -> Modo desacoplado
                	if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
                      	crearExp.getExpediente().setModoAcoplado(true);
                	}
                	else{
                      	crearExp.getExpediente().setModoAcoplado(false);
                	}
                	
                    Expediente exp = crearExp.getExpediente();
                    if(exp!=null)
                    {
                        siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_CREAR_EXP_ACCION_SIGUIENTE,crearExp);
                    }
                }
            }
        });

        JButton linkBotonGuardarCrearExpPanel = crearExp.getBotonGuardar();
        linkBotonGuardarCrearExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(crearExp.recopilarDatos())
                {
                	// se guarda el modo de trabajo
                    // true -> Modo acoplado
                    // false -> Modo desacoplado
                	if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
                      	crearExp.getExpediente().setModoAcoplado(true);
                	}
                	else{
                      	crearExp.getExpediente().setModoAcoplado(false);
                	}
                	
                    Expediente exp = crearExp.getExpediente();
                    if(exp!=null)
                    {
                        siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_CREAR_EXP_ACCION_GUARDAR,crearExp);
                    }
                }
            }
        });

        llamadaAInternalFrame(crearExp, false, true);
    }

    /**
     * Las acciones a realizar cuando se inicia la aplicacion y hay expedientes pendiente. Se crea un nuevo objeto
     * de gestion de expediente. Se linkean los botones que pueden llevar a otra pantalla segun el flujo del programa
     * y se llama a mostrarInternalFrame para que se muestre. Los valores de maxTam y unica son false, no se maximiza,
     * y true, no puede haber mas de una ventana cuando esta se inicia.
     *
     * @param expedientes ArrayList con todos los expedientes a mostrar.
     */
    private void muestraBuscarExpedientesUsuarioExportacionMasiva(final ArrayList listaExpedientes)
    {
    	if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
    	final BusquedaExportacionMasiva  busquedaImportacionMasiva = new BusquedaExportacionMasiva(this, listaExpedientes);
    	
    	JButton linkBotonExportarTodoBuscarPanel= busquedaImportacionMasiva.getExportarTodoJButton();
    	linkBotonExportarTodoBuscarPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {

            	ArrayList listaExp = busquedaImportacionMasiva.getTodosExpedientes();
            	ArrayList lstExp = new ArrayList();
            	 for (int i=0; i<listaExp.size(); i++ ){
            		 lstExp.add((Expediente)listaExp.get(i));
            	 }
 
            	 ArrayList listaIdExpedientes = new ArrayList();
                 for (int i=0; i<listaExp.size(); i++ ){
                 	listaIdExpedientes.add(((Expediente)listaExp.get(i)).getIdExpediente());
  	
                 }
            	busquedaImportacionMasiva.cierraInternalFrame();
            	mostrarExportacionMasiva(lstExp, listaIdExpedientes, true);
            }
        });
        
        JButton linkBotonExportarBuscarPanel= busquedaImportacionMasiva.getExportarJButton();
        linkBotonExportarBuscarPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ArrayList listaExp= busquedaImportacionMasiva.getExpedienteSeleccionado();
                ArrayList listaIdExpedientes = new ArrayList();
                for (int i=0; i<listaExp.size(); i++ ){
                	listaIdExpedientes.add(((Expediente)listaExp.get(i)).getIdExpediente());
 	
                }
                
                if(listaExp!=null && !listaExp.isEmpty())
                {
                	busquedaImportacionMasiva.cierraInternalFrame();
                	mostrarExportacionMasiva(listaExp, listaIdExpedientes, false);
                   
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgSelecExpTabla"));
                }
            }
        });
    	
        llamadaAInternalFrame(busquedaImportacionMasiva, false, true);
    }

    /**
     * Las acciones a realizar cuando se inicia la aplicacion y hay expedientes pendiente. Se crea un nuevo objeto
     * de gestion de expediente. Se linkean los botones que pueden llevar a otra pantalla segun el flujo del programa
     * y se llama a mostrarInternalFrame para que se muestre. Los valores de maxTam y unica son false, no se maximiza,
     * y true, no puede haber mas de una ventana cuando esta se inicia.
     *
     * @param expedientes ArrayList con todos los expedientes a mostrar.
     */
    private void muestraBuscarExpedientesUsuario(ArrayList expedientes)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final Busqueda busqueda= new Busqueda(this, expedientes);

        JButton linkBotonBuscarBuscarPanel = busqueda.getNuevoJButton();
        linkBotonBuscarBuscarPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                siguienteFrame(null, ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_NUEVO, busqueda);
            }
        });

        JButton linkBotonConsultaEstadoExpedienteBuscarPanel= busqueda.getConsultaEstadoExpedienteButton();
        linkBotonConsultaEstadoExpedienteBuscarPanel.addActionListener(new ActionListener()
        {
        	 public void actionPerformed(java.awt.event.ActionEvent evt)
             {
             	final Expediente expediente = busqueda.getExpedienteSeleccionado();
             	
             	try {
             		HashMap certificadoHashMap = (HashMap) aplicacion.getBlackboard().get(ConstantesCatastro.CERTIFICADO_DATOS);
            		
             		if(certificadoHashMap == null){
            			// no se ha establecido el certificado
            			 JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "No se han establecido los datos del Certificado");
            		}
            		else{
            			Object datosWsResp = null;
	             		
	             		Date fechaMovimiento = new Date();
	             		expediente.setFechaMovimiento(fechaMovimiento);
	             		SimpleDateFormat formato = new SimpleDateFormat("hh:mm:ss");
	             		expediente.setHoraMovimiento(formato.format(fechaMovimiento));
	             		
	             		String nombreSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_NOMBRE_SOLICITANTE);
                		String nifSolicitante = (String)certificadoHashMap.get(ConstantesCatastro.CERTIFICADO_DNI_SOLICITANTE);
                		     		
	             		CatastroWS catastrows = new CatastroWS();
	                	 	String XML = catastrows.buildConsultaEstadoExpedienteRequest(expediente,  
	                	 			nombreSolicitante, nifSolicitante);
	                	 	System.out.println(XML);
	                	 	TestClient test = new TestClient(certificadoHashMap);
	                	 	datosWsResp = test.consultaEstadoExpediente(XML, AppContext.getApplicationContext(), expediente);
	                     
	                    	if( datosWsResp instanceof DatosWSResponseBean){
	 	                    	 DatosWSResponseBean datosWsResponse = (DatosWSResponseBean)datosWsResp;
	 		                     if(datosWsResponse != null){
	                    	  
	 		                    	 //	se han producido errores
	 		                    	 GestionResponseWS gestionResponseWS = new GestionResponseWS();
	 		                    	 String mensaje = gestionResponseWS.gestionResponseExpediente(datosWsResponse, AppContext.getApplicationContext());
	                           
	 		                    	 MessageDialog messageDialog = new MessageDialog(AppContext.getApplicationContext(), "comunicacion.catastro.consultaEstado.title");
	 		                    	 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje);
	 		                    	 messageDialog.show();	
	                           
	                           
	 		                    	 if (datosWsResponse.getRespuesta().getLstUnidadError() == null ||
	 		                    		 datosWsResponse.getRespuesta().getLstUnidadError().isEmpty()){
	                        	  
	 		                    		 if (datosWsResponse.getRespuesta().getUdsa().getExpediente().getTipoDeIntercambio().equals(ConstantesCatastro.EXP_FINALIZADO)){
		                    	 
	 		                    			 // se actualizan los datos de catastrales
	 		                    			 WSOperations wsOper = new WSOperations();
				                    	 
	 		                    			 for(int i=0; i<datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().size(); i++){
				                    		  wsOper.insertarDatosCatastrales((UnidadDatosIntercambio)datosWsResponse.getRespuesta().getUdsa().getLstUnidadDatosIntercambio().get(i));
	 		                    			 }
			                    	 
	 		                    			 Expediente exp = busqueda.getExpedienteSeleccionado();
				                    	 
	 		                    			 //se modifica catastro_temporal
	 		                    			 //actualizaCatastroTemporal(exp, ConstantesCatastro.tipoConvenio);
			                     	 
	 		                    			 //se actualiza el estado del expediente
	 		                    			 siguienteFrame(exp,ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_CONSULTA_ESTADO,busqueda);
	 		                    		 }
	 		                    	 }
	 		                     }
	                    	}
	                    	else{
		                    	 	DatosErrorWS datosError = (DatosErrorWS)datosWsResp;
		 	                    	
		 	                    	GestionResponseWS gestionResponseWS = new GestionResponseWS();
		 	                    	String mensaje = gestionResponseWS.gestionErrorComunicacion(datosError);
					                 	
		 	                    	 MessageDialog messageDialog = new MessageDialog(aplicacion, "comunicacion.catastro.creacionExpediente.title");
				 	                 messageDialog.getJEditorPaneResultadoImportacion().setText(mensaje.toString());
				 	                 messageDialog.show();	
		                     }
            		}
             	}
             	catch (Exception e) {
 					e.printStackTrace();
 					System.err.println(e.toString());
 					logger.error(e.getMessage());
 					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
 							I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion"), "Error",
 							JOptionPane.ERROR_MESSAGE);

 				}
                
             } 

        });
        
        
        
        JButton linkBotonAceptarBuscarPanel= busqueda.getAceptarJButton();
        linkBotonAceptarBuscarPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Expediente exp= busqueda.getExpedienteSeleccionado();
                if(exp!=null)
                {
                	 if(exp.isTipoTramitaExpSitFinales()){
                     	//expediente de situaciones finales
                     	aplicacion.getBlackboard().put("expSitFinales",true);
                     }
                     else{
                     	//expediente orientado a variaciones
                     	aplicacion.getBlackboard().put("expSitFinales",false);
                     }
                    siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_BUSQUEDA_ACCION_ACEPTAR, busqueda);
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgSelecExpTabla"));
                }
            }
        });
        
        JButton linkBotonBorrarBuscarPanel= busqueda.getJButtonBorrar();
        linkBotonBorrarBuscarPanel.addActionListener(new ActionListener()
        {
        	public void actionPerformed(java.awt.event.ActionEvent evt)
        	{

        		String msg1= I18N.get("RegistroExpedientes",
        		"Catastro.RegistroExpedientes.MainCatastro.msg1");
        		String msg2= I18N.get("RegistroExpedientes",
        		"Catastro.RegistroExpedientes.MainCatastro.msg2");

        		Object[] options = {msg1, msg2};
        		
        		if (JOptionPane.showOptionDialog(null, I18N.get("RegistroExpedientes",
        		"Catastro.RegistroExpedientes.MainCatastro.msgExpDeleteConfirm"), I18N.get("RegistroExpedientes",
        		"Catastro.RegistroExpedientes.MainCatastro.msgExpDeleteTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
        		null, //don't use a custom Icon
        		options, //the titles of buttons
        		options[1])==JOptionPane.OK_OPTION)
        		{

        			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);


        			progressDialog. setTitle("TaskMonitorDialog.Wait");
        			progressDialog.report(I18N.get("Expedientes","CargandoPantallaDeGestionDeExpedientes"));
        			progressDialog.addComponentListener(new ComponentAdapter()
        			{
        				public void componentShown(ComponentEvent e)
        				{                
        					// Wait for the dialog to appear before starting the
        					// task. Otherwise the task might possibly finish before the dialog
        					// appeared and the dialog would never close. [Jon Aquino]
        					new Thread(new Runnable()
        					{
        						public void run()
        						{
        							try
        							{

        								Expediente exp= busqueda.getExpedienteSeleccionado();
        								if(exp!=null)
        								{
        									if (exp.getIdEstado() != ConstantesRegExp.ESTADO_GENERADO && 
        											exp.getIdEstado() != ConstantesRegExp.ESTADO_CERRADO &&
        											exp.getIdEstado() != ConstantesRegExp.ESTADO_ENVIADO){




        										if ( borrarExpedienteAction(exp)){   

        											mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
        													"Catastro.RegistroExpedientes.MainCatastro.msgExpDelete"));
        										}


        									}
        									else{
        										mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
        										"Catastro.RegistroExpedientes.MainCatastro.msgExpDeleteType"));
        									}
        								}
        								else
        								{
        									mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
        									"Catastro.RegistroExpedientes.MainCatastro.msgSelecExpTabla"));
        								}
        							}
        							catch (Exception e)
        							{
        								e.printStackTrace();
        							} 
        							finally
        							{
        								progressDialog.setVisible(false);
        								progressDialog.dispose();
        							}
        						}
        					}).start();
        				}
        			});
        			GUIUtil.centreOnWindow(progressDialog);
        			progressDialog.setVisible(true);

        			siguienteFrame(null, ConstantesCatastro.FRAME_ORIGEN_GESTION_EXP_ACCION_CERRAR, busqueda);
        		}
        	}

        });


        llamadaAInternalFrame(busqueda, false, true);
    }
    
    private boolean borrarExpedienteAction(Expediente exp){
    	
    	boolean borrado = false;
    	borrado = borrarExpedienteEnBD(exp);
    	return borrado;
    }
    
    public boolean borrarExpedienteEnBD(Expediente expediente)
    {
        try
        {
            Boolean mensaje = ((Boolean)ConstantesRegExp.clienteCatastro.borrarExpediente(expediente)).booleanValue();
            if (mensaje != null){
            	return mensaje.booleanValue();
            }
            else{
            	return false;
            }
        }
        catch(ACException e)
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",e.getCause().getMessage()));
            return false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
     /**
     * Las acciones a realizar cuando se pulsa el boton ver estado en la pantalla de Gestion de Expediente. Se crea un
     * nuevo objeto de mostrar imagen estado y se llama a mostrarInternalFrame para que se muestre, los valores de
     * maxTam y unica son false, se muestra delante de la pantalla existente y no al maximo tamaño. En caso de no haber
     * ningun estado seleccionado avisa al usuario para que seleccione uno.
     *
     * @param estado El estado del expediente.
     */
    private void mostrarImagenEstado(String estado)
    {
        if(estado!=null)
        {
            MostrarImagenEstado mosImagenEst= new MostrarImagenEstado(this, estado);
            llamadaAInternalFrame(mosImagenEst, false, false);
        }
        else
        {
            mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgSeleccionarEstado"));
        }
    }

    /**
     * Las acciones a realizar cuando se pulsa el boton asociar parcela en la pantalla de Gestion de Expediente,
     * o cuando el flujo del programa hace que se muestre esta pantalla. Se crea un nuevo objeto de asociar Parcelas.
     * Se linkean los botones que pueden llevar a otra pantalla segun el flujo del programa y se llama a
     * mostrarInternalFrame para que se muestre. Los valores de maxTam y unica son true, se maximiza, y true,
     * no puede haber mas de una ventana cuando esta se inicia.
     *
     * @param exp El expediente
     */
    private void mostrarAsociarParcelas(Expediente exp)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final AsociarParcelas mosImagenEst= new AsociarParcelas(this, exp);

        JButton linkBotonGuardarAsociarParcelasPanel = mosImagenEst.getGuardarExpedienteJButton();
        linkBotonGuardarAsociarParcelasPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(mosImagenEst.checkeaReferEstado())
                {
                    mosImagenEst.recopilaDatos();
                    Expediente exp = mosImagenEst.getExpediente();
                    if(exp!=null)
                    {
                    	for(int i=0; i<exp.getListaReferencias().size(); i++){
                    		if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
                    			((FincaCatastro)exp.getListaReferencias().get(i)).setIdentificadorDialogo("");
                    		}
                    		
                    	}
                        siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_GUARDAR, mosImagenEst);
                    }
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgSeleccionarParcela"));
                }
            }
        });

        JButton linkBotonSalirAsociarParcelasPanel = mosImagenEst.getSalirJButton();
        linkBotonSalirAsociarParcelasPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(muestraDialogoEleccion(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgAsociarParcelas")))
                {
                    Expediente exp = mosImagenEst.getExpediente();
                    siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_PARCELA_ACCION_SALIR, mosImagenEst);
                }
            }
        });

        llamadaAInternalFrame(mosImagenEst, true, true);
        zoomMapa();        
    }

    private void mostrarAsociarBienInmueble(Expediente exp)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final AsociarBienInmueble asociarBI= new AsociarBienInmueble(this, exp);

        JButton linkBotonGuardarAsociarParcelasPanel = asociarBI.getGuardarExpedienteJButton();
        linkBotonGuardarAsociarParcelasPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(asociarBI.checkeaReferEstado())
                {
                    asociarBI.recopilaDatos();
                    Expediente exp = asociarBI.getExpediente();
                    if(exp!=null)
                    {
                    	for(int i=0; i<exp.getListaReferencias().size(); i++){
                    		if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
                    			((FincaCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(false);
                    			((FincaCatastro)exp.getListaReferencias().get(i)).setIdentificadorDialogo("");
                    		}
                    		else if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
                    			((BienInmuebleCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(false);
                    			((BienInmuebleCatastro)exp.getListaReferencias().get(i)).setIdentificadorDialogo("");
                    		}
                    		else if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
                    			((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble().setActualizadoOVC(false);
                    			((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble().setIdentificadorDialogo("");
                    		}
                    	}
                        siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_GUARDAR, asociarBI);
                    }
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgSeleccionBienInmueble"));
                }
            }
        });

        JButton linkBotonSalirAsociarParcelasPanel = asociarBI.getSalirJButton();
        linkBotonSalirAsociarParcelasPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(muestraDialogoEleccion(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msgAsociarParcelas")))
                {
                    Expediente exp = asociarBI.getExpediente();
                    siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_ASOCIAR_BIEN_INMUEBLE_ACCION_SALIR, asociarBI);
                }
            }
        });

        llamadaAInternalFrame(asociarBI, false, true);
    }


    /**
     * Metodo que muestra la pantalla de los ficheros enviados a catastro y la respuesta de este. Se crea el objeto y
     * se llama a mostrar internal frame con el frame, el valor de maximizable a false y el valor de unica a true.
     */
    private void consultarHistoricoMenuItemActionPerformed()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        ConsultarHistorico mosImagenEst= new ConsultarHistorico(this);
        llamadaAInternalFrame(mosImagenEst, false, true);
    }

    private void mostrarModificarExpediente(Expediente exp)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final ModificarExpediente modificarExp = new ModificarExpediente(this, exp);

        JButton linkBotonGuardarModificarExpPanel = modificarExp.getGuardarJButton();
        linkBotonGuardarModificarExpPanel.addActionListener(new ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if(modificarExp.recopilarDatos())
                {
                    Expediente exp = modificarExp.getExpediente();
                    if(exp!=null)
                    {
                        siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_GUARDAR, modificarExp);
                    }
                }
            }
        });

        JButton linkBotonSalirModificarExpPanel = modificarExp.getSalirJButton();
        linkBotonSalirModificarExpPanel.addActionListener(new ActionListener()
        {
        	public void actionPerformed(java.awt.event.ActionEvent evt)
        	{

        		Expediente exp = modificarExp.getExpediente();
        		siguienteFrame(exp, ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_EXP_ACCION_SALIR, modificarExp);

        	}
        });
        llamadaAInternalFrame(modificarExp, true, true);
    }

    private void mostrarModificarInfCatastral(final Expediente exp)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
        
       
        progressDialog. setTitle("TaskMonitorDialog.Wait");
        progressDialog.report(I18N.get("Expedientes","CargandoPantallaDeGestionDeExpedientes"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {                
                // Wait for the dialog to appear before starting the
                // task. Otherwise the task might possibly finish before the dialog
                // appeared and the dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {
                            GestionExpedientePanel g = new GestionExpedientePanel(exp, true);
                            openComponent2(g, g.getJPanelBotones(), true, true);                                                
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        } 
                        finally
                        {
                            progressDialog.setVisible(false);
                            progressDialog.dispose();
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
        
        if (desktopPane.getAllFrames().length==1 &&
                desktopPane.getAllFrames()[0] instanceof JInternalFrame)
        {
            try{
                Object o = ((JScrollPane)(((JPanel)(((JInternalFrame)desktopPane.getAllFrames()[0]).
                        getContentPane()))).getComponent(0)).getViewport().getComponent(0);
                if (o instanceof ScreenComponent)
                {
                    JButton btnSalir = ((ScreenComponent)o).getBtnSalir();
                    
                    btnSalir.addActionListener(new ActionListener()
                            {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {  
                            if(exp!=null)
                            {
                                try
                                {
                                	
                                    Expediente expediente= (Expediente)ConstantesRegExp.clienteCatastro.getParcelasExpediente(exp, ConstantesCatastro.tipoConvenio);
                                    siguienteFrame(expediente, ConstantesCatastro.FRAME_ORIGEN_MODIFICAR_INF_CATASTRAL_ACCION_SALIR, desktopPane.getAllFrames()[0]);
                                }
                                catch(ACException e)
                                {
                                    JOptionPane.showMessageDialog(aplicacion.getMainFrame(),e.getCause().getMessage());
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }                             
                            }                            
                        }
                            });                
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            } 
            repaint();
        }
    }

    private void mostrarExportacionMasiva(ArrayList listaExp, final ArrayList listaIdExpedientes, final boolean todosExpedientes)
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        final ExportacionMasiva expMas = new ExportacionMasiva(this, listaExp);
        
        JButton linkBotonGenerarExportaMasivoPanel = expMas.getAceptarJButton();
        linkBotonGenerarExportaMasivoPanel.addActionListener(new ActionListener()
        {
        	 public void actionPerformed(java.awt.event.ActionEvent evt)
             {
                 String directorio = expMas.getDirectorio();
                 String nombreFinEntrada=expMas.getNombreFinEntrada();
                 String nombreVARPAD=expMas.getNombreVARPAD();
                 if(directorio!=null && !directorio.equals("")&&
                 		nombreFinEntrada!=null && !nombreFinEntrada.equals("")&&
                 		nombreVARPAD!=null && !nombreVARPAD.equals(""))
                 {
                	 exportacionMasivaDesacoplado(expMas, directorio, nombreFinEntrada, nombreVARPAD, listaIdExpedientes, todosExpedientes);

                 }
                 else
                 {
                     mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                         "Catastro.RegistroExpedientes.MainCatastro.directorioNull"));
                 }
             }
         });

        llamadaAInternalFrame(expMas, false, true);
    }

    private void mostrarAsociarCertificado()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        try
        {
        	GeopistaConfiguracionCertificado configuracionCertificadoDialogo = new GeopistaConfiguracionCertificado();
        	configuracionCertificadoDialogo.setModal(true);
        	configuracionCertificadoDialogo.setLocation(this.getWidth()/2-configuracionCertificadoDialogo.getWidth()/2, this.getHeight()/2-configuracionCertificadoDialogo.getHeight()/2);
        	configuracionCertificadoDialogo.setResizable(false);
        	configuracionCertificadoDialogo.show();
     		
     		
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void mostrarImportarCertificado()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        try
        {
        	GeopistaImportacionCertificadoJDialog configuracionImportacionCertificadoDialogo = new GeopistaImportacionCertificadoJDialog();
           
        	configuracionImportacionCertificadoDialogo.setLocation(this.getWidth()/2-configuracionImportacionCertificadoDialogo.getWidth()/2, this.getHeight()/2-configuracionImportacionCertificadoDialogo.getHeight()/2);
        	configuracionImportacionCertificadoDialogo.setResizable(false);
        	configuracionImportacionCertificadoDialogo.setModal(true);
        	configuracionImportacionCertificadoDialogo.show();
     		
     		
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void mostrarAsociarUsuariosExpediente()
    {
        if (desktopPane.getAllFramesInLayer(JDesktopPane.getLayer(new JInternalFrame())).length>0) return;
        try
        {
        	ArrayList expedientes = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio,ConstantesCatastro.modoTrabajo);
            final CambiarUsuarioExp cambioUsuExp = new CambiarUsuarioExp(this, expedientes);
            llamadaAInternalFrame(cambioUsuExp, false, true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void mostrarAsociarDatosGraficos(Expediente exp)
    {
        try
        {
            ScreenComponent sc = new ScreenComponent();
            ImportarInfoGraficaPanel g =new ImportarInfoGraficaPanel(exp);
            sc.addComponent(g);
            sc.setFillerPanel(g.getJPanelBotones());

            openModalComponent(I18N.get("ModuloCatastral","dialog.titulo.infografica"),sc);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Acciones que se realizan cuando se pulsa el boton de cambio de idioma a castellano. Se modifica la variable locale
     * que es la que indiaca a toda la aplicacion el idioma usado y se llama a setLang para actualizar recursivamente
     * todos los componentes de la gui que se esta visualizando.
     */
    private void espanolMenuItemActionPerformed()
    {
        ConstantesCatastro.Locale= "es_ES";
        setLang(ConstantesCatastro.Locale);
        JOptionPane.showMessageDialog(this, "Cambiando a español");
    }

    /**
     * Acciones que se realizan cuando se pulsa el boton de cambio de idioma a catalan. Se modifica la variable locale
     * que es la que indiaca a toda la aplicacion el idioma usado y se llama a setLang para actualizar recursivamente
     * todos los componentes de la gui que se esta visualizando.
     */
    private void catalanMenuItemActionPerformed()
    {
        ConstantesCatastro.Locale= "ca_ES";
        setLang(ConstantesCatastro.Locale);
        JOptionPane.showMessageDialog(this, "Cambiando a catalan");
    }

    /**
     * Acciones que se realizan cuando se pulsa el boton de cambio de idioma a valenciano. Se modifica la variable locale
     * que es la que indiaca a toda la aplicacion el idioma usado y se llama a setLang para actualizar recursivamente
     * todos los componentes de la gui que se esta visualizando.
     */
    private void valencianoMenuItemActionPerformed()
    {
        ConstantesCatastro.Locale= "va_ES";
        setLang(ConstantesCatastro.Locale);
        JOptionPane.showMessageDialog(this, "Cambiando a valenciano");
    }

    /**
     * Acciones que se realizan cuando se pulsa el boton de cambio de idioma a gallego. Se modifica la variable locale
     * que es la que indiaca a toda la aplicacion el idioma usado y se llama a setLang para actualizar recursivamente
     * todos los componentes de la gui que se esta visualizando.
     */
    private void gallegoMenuItemActionPerformed()
    {
        ConstantesCatastro.Locale= "gl_ES";
        setLang(ConstantesCatastro.Locale);
        JOptionPane.showMessageDialog(this, "Cambiando a gallego");
    }

	public boolean mostrarAyudaActionPerformed()
    {
		HelpSet hs = null;
		ClassLoader loader = this.getClass().getClassLoader();
		URL url;
		try
        {
            String helpSetFile = "help/catastro/CatastroHelp_" + ConstantesCatastro.Locale.substring(0,2) + ".hs";
			url = HelpSet.findHelpSet(loader, helpSetFile);
			if (url == null)
            {
                url=new URL("help/catastro/CatastroHelp_" + ConstantesCatastro.LocalCastellano.substring(0,2) + ".hs");
   			}
            hs = new HelpSet(loader, url);

			// ayuda sensible al contexto
			hs.setHomeID(ConstantesCatastro.helpSetHomeID);


		} catch (Exception ex)
        {
			logger.error("Exception: " + ex.toString());

			return false;
		}
		HelpBroker hb = hs.createHelpBroker();

		hb.setDisplayed(true);
		new CSH.DisplayHelpFromSource(hb);
		return true;
	}

    private void comprobarActualizacionesYEnvios()
    {
        try
        {
        	ConstantesRegExp.clienteCatastro.comprobarActualizacionYEnvios();
        }
        catch(ACException e)
        {
            String msgs = e.getCause().getMessage();
            String msgsArray[]=msgs.split(";");
            for(int i=0; i<msgsArray.length;i++)
            {
                JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",
                        (msgsArray[i])));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jMenuItemActualizacionesEnviosPeriodicosActionPerformed()
    {
        DialogoActualizacionPeriodica dialogo = new com.geopista.app.catastro.historico.DialogoActualizacionPeriodica(this,true);
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
    }

    private void jMenuItemConfigModosConvenionActionPerformed()
    {
        DialogoModosConvenios dialogo = new com.geopista.app.catastro.historico.DialogoModosConvenios(this,true, this);
		dialogo.setLocation(this.getWidth()/2-dialogo.getWidth()/2, this.getHeight()/2-dialogo.getHeight()/2);
		dialogo.setResizable(false);
		dialogo.show();
    }

    /**
     * Muestra un dialogo preguntandole al usuario por el msg5 pasado como parametro, si el usuario pulsa "no" se
     * devuelve el valor false, si pulsa el valor "si" se devuelve el valor true.
     */
    private boolean muestraDialogoEleccion(String msg5)
    {
        String msg1= I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msg1");
        String msg2= I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msg2");
        String msg4= I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.msg4");
        Object[] options = {msg1, msg2};

        if (JOptionPane.showOptionDialog(this, msg5, msg4, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //don't use a custom Icon
                options, //the titles of buttons
                options[1])!=JOptionPane.OK_OPTION)
        {
            return false;
        }

        return true;
    }

    /**
     * Muestra un dialogo con el mensaje de advertencia msg pasado por parametro.
     */
    private void mostrarMensajeDialogo(String msg)
    {
        JOptionPane.showMessageDialog(this, msg);
    }

    protected void openComponent (JComponent jComp, boolean resize, boolean unica)
    {
        openComponent(jComp, null, resize, unica);
    }

    protected void openComponent(JComponent jComp, JPanel p, boolean resize, boolean unica)
    {
        if (aplicacion.isOnline())
        {
            ScreenComponent sc = new ScreenComponent();
            sc.addComponent(jComp);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(sc,null);
            sc.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            if (p!=null)
                sc.setFillerPanel(p);

            llamadaAInternalFrame(JIFrame, resize, unica);
        }
        else
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
    }

    protected void openComponent2(JComponent jComp, JPanel p, boolean resize, boolean unica)
    {
        if (aplicacion.isOnline())
        {
            final ScreenComponent sc = new ScreenComponent();
            sc.addComponent(jComp);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(sc,null);
            sc.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("finished".equals(command))
                    {
                    	cierraInternalFrame2(JIFrame);
                        //Eliminacion de la informacion
                        JPanel panelInterno=(JPanel)sc.getComponentFromPanel();
                        if (panelInterno instanceof GestionExpedientePanel){
                        	GestionExpedientePanel gep=(GestionExpedientePanel)panelInterno;
                        	gep.cleanup();
                        }                                                                   
                    }
                }
            });
            if (p!=null)
                sc.setFillerPanel(p);

            llamadaAInternalFrame(JIFrame, resize, unica);
        }
        else
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
    }    
    
    
    protected void openModalComponent(String title, JPanel panel)
    {
        if (aplicacion.isOnline())
        {
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(panel,title);
            ((ScreenComponent)panel).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            llamadaAInternalFrame(JIFrame, false, false);
            
            //Trata de poner por delante la pantalla que se acaba de lanzar
            try{
                if (desktopPane.getAllFrames().length!=0 &&
                        desktopPane.getAllFrames()[desktopPane.getAllFrames().length-1] 
                                                   instanceof JInternalFrame)
                {
                    ((JInternalFrame)desktopPane.getAllFrames()
                            [desktopPane.getAllFrames().length-1]).setSelected(true);                       
                    repaint();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
        else{
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
                    aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }

    protected void openWizard(WizardPanel[] wp, boolean resize)
    {
        if (aplicacion.isOnline()){
            WizardComponent d = new WizardComponent(aplicacion, "", null);
            d.init(wp);

            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(d,null);
            if(resize){
           	 JIFrame.setMaximizable(resize);
                JIFrame.setResizable(resize);
           }
            d.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });

            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            llamadaAInternalFrame(JIFrame, false, true);
        }
        else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }
       

    protected void openModalWizard(String title, WizardPanel[] wp)
    {
        if (aplicacion.isOnline())
        {
            WizardComponent d = new WizardComponent(aplicacion, "", null);
            d.init(wp);

            //Elimina el panel blanco con título que aparece en la zona superior de la pantalla
            d.setWhiteBorder(false);
            final JInternalFrame JIFrame = encapsulaEnJInternalFrame(d,title);
            ((WizardComponent)d).addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e){

                    String command = e.getActionCommand();
                    if ("canceled".equals(command)|| "finished".equals(command))
                    {
                        cierraInternalFrame(JIFrame);
                    }
                }
            });
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            llamadaAInternalFrame(JIFrame, false, true);
        }
        else{
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(),aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
        }
    }

    private JInternalFrame encapsulaEnJInternalFrame(Component comp, String title)
    {
        final JInternalFrame JIFrame = new JInternalFrame();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        UtilRegistroExp.menuBarSetEnabled(false, this);
        if(title!=null)
        {
            JIFrame.setTitle(title);
        }
        
        JScrollPane internalFrameScrollPane = new JScrollPane();
        internalFrameScrollPane.setViewportView(comp);
        JIFrame.getContentPane().add(internalFrameScrollPane);
        JIFrame.addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
                cierraInternalFrame(JIFrame);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        JIFrame.setClosable(true);
        JIFrame.pack();
        JIFrame.setVisible(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return JIFrame;
    }

    private void cierraInternalFrame(JInternalFrame JIFrame)
    {
        if(desktopPane.getAllFrames().length>0)
        {
            desktopPane.getDesktopManager().closeFrame(JIFrame);
            desktopPane.repaint();
        }
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        UtilRegistroExp.menuBarSetEnabled(true, this);
    }

    private void cierraInternalFrame2(JInternalFrame JIFrame)
    {
        if(desktopPane.getAllFrames().length>0)
        {
            desktopPane.getDesktopManager().closeFrame(JIFrame);
            desktopPane.repaint();
        }
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        UtilRegistroExp.menuBarSetEnabled(false, this);
    }
    
    /**
     * Funcion que muestra la JInternalFrame pasada como parametro. En caso de que el valor de isMaxTam sea true la
     * internalFrame sera maximizada, sino no. En caso de que el valor unica sea true, se comprueba si hay otra ventana
     * abierta, si es asi no hace nada, sino muestra la internalFrame pasada por parametro. Si el valor de unica es false
     * se muestra aunque haya otra ventana.
     */
    private void  llamadaAInternalFrame(JInternalFrame iFrame, boolean isMaxTam, boolean unica)
    {
		try
        {
            desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int numInternalFrames = desktopPane.getAllFrames().length;
            if (numInternalFrames == 0 || !unica)
            {
                iFrame.setFrameIcon(new javax.swing.ImageIcon(IconLoader.icon(CATASTRO_LOGO).getImage()));
                desktopPane.add(iFrame);
                if(isMaxTam)
                {
                    desktopPane.getDesktopManager().maximizeFrame(iFrame);
                }
                iFrame.show();                   
			}
			desktopPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
        catch (Exception ex)
        {
		}
    }

    /**
     * Funcion que controla el flujo del programa y calcula los diferentes estados por los que va pasando un expediente.
     * Los parametros son el expediente (puede ser null si no se ha creado), la pantalla actual, y una constante definida
     * en ConstantesCatastro indicando de donde proviene este expediente. Con esos datos se llama a maquinaEstadosFlujo
     * que calculara la accion a realizar tras la peticion del usuario y actualizara el estado del expediente si fuera
     *  necesario. Si es necesario despues de que el expediente se
     * actualice, se guarda o actualiza en base de datos.
     */
    private void siguienteFrame(Expediente exp, int frameOrigen, JInternalFrame iFrame)
    {
        int resul = ConstantesRegistroExp.maquinaEstadosFlujo.controlDeFlujo(exp, frameOrigen);
        switch(resul)
        {
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CREA_EXP:
            {
                exp = insertaExpedienteEnBD(exp);
                if(exp!=null)
                {
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    gestionExpMenuItemActionPerformed(exp);
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP:
            {
                exp = updateExpedienteEnBD(exp);
                if(exp!=null)
                {
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    gestionExpMenuItemActionPerformed(exp);
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                gestionExpMenuItemActionPerformed(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS_CREAR_EXP:
            {
                exp = insertaExpedienteEnBD(exp);
                if(exp!=null)
                {
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    mostrarAsociarParcelas(exp);
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE_CREAR_EXP:
            {
                exp = insertaExpedienteEnBD(exp);
                if(exp!=null)
                {
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    mostrarAsociarBienInmueble(exp);
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_PARCELAS:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                mostrarAsociarParcelas(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_BIEN_INMUEBLE:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                mostrarAsociarBienInmueble(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_VER_ESTADO:
            {
                mostrarImagenEstado(Estructuras.getListaEstadosExpediente().getDomainNode(String.valueOf(exp.getIdEstado()))
                    .getTerm(ConstantesCatastro.Locale));
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_CREAR_EXP:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                crearExpMenuItemActionPerformed();
                break;
            }
            case ConstantesCatastro.FRAME_ACCION_DEFAULT:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                UtilRegistroExp.menuBarSetEnabled(true, this);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_MODIFICAR_DATOS_EXP:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                mostrarModificarExpediente(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                mostrarModificarInfCatastral(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_SINCRONIZAR: {
                //TODO llamar a los metodos de Importacion para el FIN Retorno de sincronizacion

                if(sincronizarExpediente(exp)){

                    exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));

                    if(escribeCatastroTemporal(exp,ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo)){
                        exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
                        updateExpedienteEnBD(exp);
                        desktopPane.getDesktopManager().closeFrame(iFrame);
                        mostrarModificarInfCatastral(exp);
                    }
                    else{
                        updateExpedienteEnBD(exp);
                        mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.errorEscribirCatastroTemporal"));
                    }
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.MainCatastro.errorSincronizar"));
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_MODIFICAR_INF_CATASTRAL_ESCRIBIR_CATASTRO_TEMPORAL:
            {
            	
            	if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
            		
            		Expediente expAux = new Expediente();
            		for (int i=0; i<exp.getListaReferencias().size();i++){
            			expAux.setIdExpediente(exp.getIdExpediente());
            			expAux.getListaReferencias().add(exp.getListaReferencias().get(i));
            			
            		}
            		if(!existeReferenciaCatastroTemporal(expAux)){
            			for (int i=0; i<exp.getListaReferencias().size();i++){
	        				if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
	        					((FincaCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(true);
	        				}
	        				
	        				if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
	        					((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble().setActualizadoOVC(true);
	        				}
	        				if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
	        					((BienInmuebleCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(true);
	        				}
            			}
        			}
            	}
            	
            	if(escribeCatastroTemporalTramitaVariaciones(exp,ConstantesCatastro.tipoConvenio, 
                		(Boolean)aplicacion.getBlackboard().get("catastroTemporal"),
                		(Boolean)aplicacion.getBlackboard().get("expSitFinales"),
                		 ConstantesCatastro.modoTrabajo)){
                    exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
                    updateExpedienteEnBD(exp);
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    mostrarModificarInfCatastral(exp);
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.MainCatastro.errorEscribirCatastroTemporal"));
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_EXPORTAR:
            {
            
                try{
                	//if(exportacionMasiva(exp, null, null, null, null, true))
                	ArrayList lstExp = new ArrayList();
                	lstExp.add(exp);
                    if(exportacionMasiva(exp, "C:\\LocalGIS\\datos", "FinEntrada", "VARPAD",lstExp, true)){
                    
                        mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                                "Catastro.RegistroExpedientes.MainCatastro.exportacionCorrecta"));
//                      exp.setIdEstado(ConstantesCatastro.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
//                      updateExpedienteEnBD(exp);
//                      ConstantesCatastro.maquinaEstadosFlujo.actualizaBotones((int)exp.getIdEstado());
                        ((GestionDeExpedientes)iFrame).inicializaDatos();
                        ((GestionDeExpedientes)iFrame).enableBotones();
                        iFrame.repaint();
                    }
                    else
                    {
                        mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                                "Catastro.RegistroExpedientes.MainCatastro.errorExportacion"));
                    }
                }
                catch(Exception e){
                    //En el modo acoplado nunca se producira la NoFileToExportException
                }

                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_UPDATE_EXP_ELIMINA_CATASTRO_TEMPORAL:
            {
                eliminarCatastroTemporal(exp);
                exp = updateExpedienteEnBD(exp);
                if(exp!=null)
                {
                    desktopPane.getDesktopManager().closeFrame(iFrame);
                    gestionExpMenuItemActionPerformed(exp);
                }
                break;
            }

            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS:
            {
                mostrarAsociarDatosGraficos(exp);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_SINCRONIZAR:
            {
                //TODO llamar a los metodos de Importacion para el FIN Retorno de sincronizacion

                if(sincronizarExpediente(exp))
                {
                    exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
                    if(escribeCatastroTemporal(exp,ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo))
                    {
                        exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
                        updateExpedienteEnBD(exp);
                        mostrarAsociarDatosGraficos(exp);
                    }
                    else
                    {
                        updateExpedienteEnBD(exp);
                        mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.errorEscribirCatastroTemporal"));
                    }
                    ((GestionDeExpedientes)iFrame).inicializaDatos();
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.MainCatastro.errorSincronizar"));
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_ASOCIAR_DATOS_GRAFICOS_ESCRIBIR_CATASTRO_TEMPORAL:
            {
            	if(ConstantesCatastro.modoTrabajo.equals(DatosConfiguracion.MODO_TRABAJO_ACOPLADO)){
            		
            		Expediente expAux = new Expediente();
            		for (int i=0; i<exp.getListaReferencias().size();i++){
            			expAux.setIdExpediente(exp.getIdExpediente());
            			expAux.getListaReferencias().add(exp.getListaReferencias().get(i));
            			
            		}
            		if(!existeReferenciaCatastroTemporal(expAux)){
            			for (int i=0; i<exp.getListaReferencias().size();i++){
	        				if(exp.getListaReferencias().get(i) instanceof FincaCatastro){
	        					((FincaCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(true);
	        				}
	        				
	        				if(exp.getListaReferencias().get(i) instanceof BienInmuebleJuridico){
	        					((BienInmuebleJuridico)exp.getListaReferencias().get(i)).getBienInmueble().setActualizadoOVC(true);
	        				}
	        				if(exp.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
	        					((BienInmuebleCatastro)exp.getListaReferencias().get(i)).setActualizadoOVC(true);
	        				}
            			}
        			}
            		
            	}

            	 if(escribeCatastroTemporalTramitaVariaciones(exp,ConstantesCatastro.tipoConvenio, 
                 		(Boolean)aplicacion.getBlackboard().get("catastroTemporal"),
                 		(Boolean)aplicacion.getBlackboard().get("expSitFinales"),
                 		 ConstantesCatastro.modoTrabajo)){
            		 
            	
//                if(escribeCatastroTemporal(exp,ConstantesCatastro.tipoConvenio, ConstantesCatastro.modoTrabajo))
//                {
                    exp.setIdEstado(ConstantesRegistroExp.maquinaEstadosFlujo.estadoSiguiente(exp.getIdEstado()));
                    updateExpedienteEnBD(exp);
                    ((GestionDeExpedientes)iFrame).inicializaDatos();                    
                    mostrarAsociarDatosGraficos(exp);
                }
                else
                {
                    mostrarMensajeDialogo(I18N.get("RegistroExpedientes",
                    "Catastro.RegistroExpedientes.MainCatastro.errorEscribirCatastroTemporal"));
                }
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_BUSCAR_EXPEDIENTES:
            {
                desktopPane.getDesktopManager().closeFrame(iFrame);
                final JFrame desktop= (JFrame)this;
                final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
                
                progressDialog.setTitle("TaskMonitorDialog.Wait");
                progressDialog.addComponentListener(new ComponentAdapter()
                {
                    public void componentShown(ComponentEvent e)
                    {
                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    progressDialog.report(I18N.get("RegistroExpedientes",
                                    "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                                    try
                                    {
                                    	expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio,  ConstantesCatastro.modoTrabajo);
                                    }
                                    catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                    muestraBuscarExpedientesUsuario(expUsuario);
                                }
                                catch(Exception e)
                                {
                                    logger.error("Error ", e);
                                    ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                                    return;
                                }
                                finally
                                {
                                    progressDialog.setVisible(false);
                                    progressDialog.dispose();
                                }
                            }
                      }).start();
                  }
                });
                GUIUtil.centreOnWindow(progressDialog);
                progressDialog.setVisible(true);
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_ACTUALIZA_CATASTRO:
            {
            	updateExpedienteEnBD(exp);
            	desktopPane.getDesktopManager().closeFrame(iFrame);
                gestionExpMenuItemActionPerformed(exp);

                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CONSULTA_ESTADO_CATASTRO:
            {
            	updateExpedienteEnBD(exp);
            	desktopPane.getDesktopManager().closeFrame(iFrame);
                gestionExpMenuItemActionPerformed(exp);

                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_BUSQUEDA_CONSULTA_ESTADO:
            {
            	updateExpedienteEnBD(exp);
            	desktopPane.getDesktopManager().closeFrame(iFrame);
            	actualizaExpBusquedaExpedientes();
            	
                break;
            }
            case ConstantesCatastro.FRAME_DESTINO_GESTION_EXP_CONSULTA_CATASTRO:
            {
            	updateExpedienteEnBD(exp);
            	desktopPane.getDesktopManager().closeFrame(iFrame);
            	gestionExpMenuItemActionPerformed(exp);
            	
                break;
            }
            
        }
    }

	public void actualizaExpBusquedaExpedientes(){
		
		final JFrame desktop= (JFrame)MainCatastro.this;
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);                    
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {	
            public void componentShown(ComponentEvent e)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            progressDialog.report(I18N.get("RegistroExpedientes",
                            "Catastro.RegistroExpedientes.MainCatastro.msgComprobandoExpediente"));
                            try
                            {
                                expUsuario = (ArrayList)ConstantesRegExp.clienteCatastro.
                                								getExpedientesUsuario("false", ConstantesCatastro.tipoConvenio,ConstantesCatastro.modoTrabajo);
                            }
                            catch(Exception exp)
                            {
                                exp.printStackTrace();
                            }
                            muestraBuscarExpedientesUsuario(expUsuario);
                        }
                        catch(Exception e)
                        {
                            logger.error("Error ", e);
                            ErrorDialog.show(desktop, "ERROR", "ERROR", StringUtil.stackTrace(e));
                            return;
                        }
                        finally
                        {
                            progressDialog.setVisible(false);
                            progressDialog.dispose();
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);
    }

		
    /**
     * Funcion que llama al ClienteCatastro (El objeto que realiza las peticiones a la base de datos) para insertar
     * los datos del expediente, que el usuario ha estado tratando, en base de datos.
     */
    public Expediente insertaExpedienteEnBD(Expediente expediente)
    {
        try
        {
            expediente= (Expediente)ConstantesRegExp.clienteCatastro.crearExpediente(expediente);
            return expediente;
        }
        catch(ACException e)
        {
            JOptionPane.showMessageDialog(this,I18N.get("RegistroExpedientes",e.getCause().getMessage()));
            return null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    
    /**
     * Funcion que llama al ClienteCatastro (El objeto que realiza las peticiones a la base de datos) para actualizar
     * los datos del expediente, que el usuario ha estado tratando, en base de datos.
     */
    public Expediente updateExpedienteEnBD(Expediente expediente)
    {
        try
        {
            expediente= ConstantesRegExp.clienteCatastro.updateExpediente(expediente);
            return expediente;
        }
        catch(ACException e)
        {
            JOptionPane.showMessageDialog(this,e.getCause().getMessage());
            return null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    

    
    /**
     * Funcion que llama al ClienteCatastro (El objeto que realiza las peticiones a la base de datos) para actualizar
     * los datos del expediente, que el usuario ha estado tratando, en base de datos.
     */
    public void updateIdentificadoresDialogoExpediente(ArrayList lstIdentificadoresDialogo, Expediente exp)
    {
        try
        {
        	ConstantesRegExp.clienteCatastro.updateIdentificadoresDialogoExpediente(lstIdentificadoresDialogo, exp);
        }
        catch(ACException e)
        {
            JOptionPane.showMessageDialog(this,e.getCause().getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
    }

    public boolean exportacionMasiva(Expediente exp, String directorio, String nombreFicheroFinEntrada, String nombreVARPAD,
			ArrayList listaExpedientes, boolean todosExpedientes) {
		try
		{
		if((directorio.length() + nombreFicheroFinEntrada.length()+4) > 250 &&(directorio.length() + nombreVARPAD.length()+4) > 200){
		JOptionPane.showMessageDialog(this, "La ruta de exportación es demasiado larga.");
		return false;
		}
		
		ConstantesRegExp.clienteCatastro.exportacionMasiva(exp, ConstantesCatastro.modoTrabajo,
		directorio, nombreFicheroFinEntrada, nombreVARPAD, ConstantesCatastro.tipoConvenio,
		listaExpedientes, todosExpedientes);
		
		return true;
		}
        catch(Exception e) {
            if(e instanceof ACException){
            	e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No hay expedientes para exportar.");
                return false;
            }
            else {
                ErrorDialog.show(this, "ERROR", "ERROR", StringUtil.stackTrace(e));
                e.printStackTrace();
                return false;
            }
        }
    }


    public boolean escribeCatastroTemporal(Expediente exp, String convenio, String modoTrabajo) {
        try {

        	ConstantesRegExp.clienteCatastro.escribirEnCatastroTemporal(exp, convenio, modoTrabajo);
            return true;

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean existeReferenciaCatastroTemporal(Expediente exp) {
        try {

            boolean existe = ConstantesRegExp.clienteCatastro.existeReferenciaCatastroTemporal(exp);
            return existe;

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizaCatastroTemporal(Expediente exp, String convenio) {
        try {

        	ConstantesRegExp.clienteCatastro.actualizaCatastroTemporal(exp, convenio);
            return true;

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
   
    public boolean escribeCatastroTemporalTramitaVariaciones(Expediente exp, String convenio,
    		Boolean isCatastroTemporal, Boolean isExpSitFinales, String modoTrabajo) {
        try {
        	//AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        	//aplicacion.getApplicationContext().getBlackboard().put("catastroTemporal", isCatastroTemporal);
        	ConstantesRegExp.clienteCatastro.escribirEnCatastroTemporalTramiVariacion(exp, convenio, isCatastroTemporal, isExpSitFinales, modoTrabajo);
            return true;

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sincronizarExpediente(Expediente exp)
    {
        try
        {
        	ConstantesRegExp.clienteCatastro.sincronizarExpediente(exp);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCatastroTemporal(Expediente exp)
    {
        try
        {
        	ConstantesRegExp.clienteCatastro.elimiarCatastroTemporalExp(exp);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();

            return false;
        }
    }

    private void exportacionMasivaDesacoplado(final JInternalFrame expMas,final String directorio, final String nombreFicheroFinEntrada, final String nombreFicheroVARPAD,
												final ArrayList listaExpedientes, final boolean todosExpedientes)
    {

        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(this, null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter(){

            public void componentShown(ComponentEvent e){

                new Thread(new Runnable(){

                    public void run(){
                        progressDialog.report(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.MainCatastro.ExportacionMasiva"));

                        if(exportacionMasiva(null, directorio, nombreFicheroFinEntrada, nombreFicheroVARPAD,
            					listaExpedientes, todosExpedientes )){
			                mostrarMensajeDialogo(I18N.get("RegistroExpedientes", "Catastro.RegistroExpedientes.MainCatastro.exportacionCorrecta"));
			                desktopPane.getDesktopManager().closeFrame(expMas);
			            }

                              progressDialog.setVisible(false);
                              progressDialog.dispose();
                          }
                }).start();
            }
        });

        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        UtilRegistroExp.menuBarSetEnabled(true, this);        
    }

    /**
     * Metodo que inicializa los parametros principales de la aplicacion para conectarse con el servidor, inicializa
     * el locale, y actualiza el idMunicipio segun las variables de entorno.
     */
    private boolean configureApp()
    {
        try
        {
			try
            {
                PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.ini", 3000);

            }catch(Exception e){};

            try
            {
            	
            	 String _urlServidor = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);
            	 catastroUrl				= _urlServidor + WebAppConstants.CATASTRO_WEBAPP_NAME;
               ConstantesRegExp.clienteCatastro = new CatastroClient(catastroUrl+ServletConstants.CATASTRO_SERVLET_NAME);
				
               ConstantesCatastro.Locale = aplicacion.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES");

               try
               {
                   ConstantesCatastro.IdMunicipio=new Integer(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();
               }
               catch (Exception e)
               {
                   mostrarMensajeDialogo( "Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                   System.out.println("Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                   logger.error("Valor de id municipio no valido:"+e.toString()+aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
                   System.exit(-1);
               }
            }
            catch(Exception e)
            {
                	StringWriter sw = new StringWriter();
			        PrintWriter pw = new PrintWriter(sw);
			        e.printStackTrace(pw);
                    mostrarMensajeDialogo( "Excepcion al cargar el fichero de configuración:\n"+sw.toString());
			        logger.error("Exception: " + sw.toString());
                    System.exit(-1);
			        return false;
            }
			setLang(ConstantesCatastro.Locale);
			return true;
		}
        catch (Exception ex)
        {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}

    /**
     * Asigna el nuevo idioma a la variable locale y cambia el idioma de las guis que se estan mostrando.
     */
    private void setLang(String locale)
    {
        try
        {
        	UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, locale);
        }
        catch(Exception e)
        {
            logger.error("Exception: " + e.toString());
        }
        renombrarComponentes();
        try {((IMultilingue)desktopPane.getComponents()[0]).renombrarComponentes();}catch (Exception e) {}
    }

    /**
     * Cambia las etiquetas de la interfaz segun el idoma que se haya elegido.
     */
    private void renombrarComponentes()
    {
        jMenuRegistro.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.gestionExpMenu"));
        crearExpMenuItem.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.crearExpMenuItem"));
        gestionExpMenuItem.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.gestionExpMenuItem"));
        jMenuItemConsultarIntercambios.setText(I18N.get("RegistroExpedientes",
                        "Catastro.RegistroExpedientes.MainCatastro.consultarHistoricoMenuItem"));
      
    }

    /**
     * Comprueba que el usuario esta autorizado para acceder a la aplicacion y sus permisos para las diferentes
     *  acciones.
     */
    private boolean showAuth()
    {
        try
        {
            CAuthDialog auth = new CAuthDialog(this, true,
            			catastroUrl,ConstantesCatastro.idApp,
                        ConstantesCatastro.IdMunicipio, aplicacion.getI18NResource());
            auth.setBounds(30, 60, 315, 155);            
            auth.show();
            
            //CAMBIADO
            
            /*com.geopista.security.SecurityManager.setHeartBeatTime(5000);
            GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
            applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());*/
            setPolicy();
        }
        catch(Exception e)
        {
             StringWriter sw = new StringWriter();
			 PrintWriter pw = new PrintWriter(sw);
			 e.printStackTrace(pw);
             logger.error("ERROR al autenticar al usuario "+sw.toString());
             JOptionPane optionPane = new JOptionPane("Error al inicializar: \n"
                    +((e.getMessage()!=null && e.getMessage().length()>=0)?e.getMessage():e.toString()), JOptionPane.ERROR_MESSAGE);
             JDialog dialog = optionPane.createDialog(this, "ERROR");
	         dialog.show();
            return false;
        }
		return true;
	}
    public void setPolicy() throws AclNotFoundException, Exception{
    	 com.geopista.security.SecurityManager.setHeartBeatTime(5000);
         GeopistaAcl acl = com.geopista.security.SecurityManager.getPerfil("RegistroExpedientes");
         applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());
         
         acl = com.geopista.security.SecurityManager.getPerfil("App.LocalGIS.Catastro");
         applySecurityPolicy(acl, com.geopista.security.SecurityManager.getPrincipal());

    }
    /**
     * Comprueba los permisos del usuario.
     */
    public boolean applySecurityPolicy(GeopistaAcl acl, GeopistaPrincipal principal)
    {
		try
        {
            if ((acl == null) || (principal == null))
            {
				return false;
			}
            ConstantesRegistroExp.principal = principal;
            setPermisos(acl.getPermissions(principal));
            if (tienePermisos("Catastro.RegistroExpediente.Tecnico"))
            {
                jMenuItemConsultarIntercambios.setEnabled(false);
                jMenuConfiguracion.setEnabled(false);
                jMenuGenerarFicheros.setEnabled(false);
            }
            if(ConstantesCatastro.tipoConvenio.equalsIgnoreCase(DatosConfiguracion.CONVENIO_NINGUNO))
            {
                this.getJMenuBar().getMenu(0).setEnabled(false);
                this.getJMenuBar().getMenu(1).setEnabled(false);
                this.getJMenuBar().getMenu(2).setEnabled(false);
            }
            if(ConstantesCatastro.modoTrabajo.equalsIgnoreCase(DatosConfiguracion.MODO_TRABAJO_ACOPLADO))
            {
                this.getJMenuBar().getMenu(2).setEnabled(false);
                this.getJMenuBar().getMenu(3).getMenuComponent(3).setEnabled(true);
            }
            else{
          
            	this.getJMenuBar().getMenu(3).getMenuComponent(3).setEnabled(false);
            }
            return true;
		}
        catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
			return false;
		}

	}

    /**
     * Crea hash de permisos del usuario.
     */
    private void setPermisos(Enumeration e)
    {
    	if (permisos==null)
    		permisos= new Hashtable();
        if (e!=null){
	        while (e.hasMoreElements())
	        {
	            GeopistaPermission geopistaPermission = (GeopistaPermission) e.nextElement();
	            String permissionName = geopistaPermission.getName();
	            if (!permisos.containsKey(permissionName))
	            {
	                permisos.put(permissionName, "");
	            }
	        }
        }
    }

    /**
     * Funcion que comprueba si el usuario tiene permisos para una tarea en especial.
     */
    private boolean tienePermisos(String permiso)
    {
        if (permisos.containsKey(permiso))
        {
            return true;
        }
        return false;
    }

    private void zoomMapa()
    {
        try
        {
            if(ConstantesRegistroExp.geopistaEditor!=null)
            {
                WorkbenchContext wb=ConstantesRegistroExp.geopistaEditor.getContext();
                PlugInContext plugInContext = wb.createPlugInContext();
                plugInContext.getLayerViewPanel().getViewport().zoomToFullExtent();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Acciones realizadas cuando se cierra la aplicacion.
     *
     * @param evt El evento capturado.
     */
    private void exitForm(java.awt.event.WindowEvent evt)
    {
        try
        {
        	//-----NUEVO----->	
            if(!SSOAuthManager.isSSOActive()) 
         	   com.geopista.security.SecurityManager.logout();
            //---FIN NUEVO--->
        }
        catch (Exception ex)
        {
            logger.warn("Exception: " + ex.toString());
        }
        System.exit(0);
    }

    private void  stopApp(){
    	JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Inicio de aplicación cancelado. Se cerrará el aplicativo.");
    	 this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    	 System.exit(1);
    }
    /*Main de la aplicacion*/

    public static void main(String args[])
    {
    	MainCatastro aux = new MainCatastro();
    }
    
    // Metodos que implementan la interfaz ISecurityPolicy
    @Override
	public void resetSecurityPolicy() {	
	}
  	  	
  	@Override
  	public ApplicationContext getAplicacion() {
  		return aplicacion;
  	}
  	@Override
  	public String getIdApp() {
  		return ConstantesCatastro.idApp;
  	}
  	@Override
  	public String getIdMunicipio() {
  		return String.valueOf(Constantes.idEntidad);
  	}
  	@Override
  	public String getLogin() {
  		return Constantes.url;
  	}
  	@Override
  	public JFrame getFrame() {
  		return this;
  	}
}
