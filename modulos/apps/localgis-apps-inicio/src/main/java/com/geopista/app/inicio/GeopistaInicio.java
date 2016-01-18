/**
 * GeopistaInicio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package com.geopista.app.inicio;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.catastro.GeopistaEditarCatastro;
import com.geopista.app.catastro.GeopistaEditarCatastroPanel;
import com.geopista.app.catastro.GeopistaFinImportacionPanel;
import com.geopista.app.catastro.GeopistaImportarCallesSeleccionElemento;
import com.geopista.app.catastro.GeopistaImportarFINCARUPanel;
import com.geopista.app.catastro.GeopistaImportarFINURBPanel;
import com.geopista.app.catastro.GeopistaImportarPadron;
import com.geopista.app.catastro.GeopistaImportarPadronFin;
import com.geopista.app.catastro.GeopistaImportarPadronFin2006;
import com.geopista.app.catastro.GeopistaImportarPadronRusticaFin;
import com.geopista.app.catastro.GeopistaImportarParcelas;
import com.geopista.app.catastro.GeopistaImportarParcelas02;
import com.geopista.app.catastro.GeopistaImportarParcelasMapa;
import com.geopista.app.catastro.GeopistaImportarRustica2005;
import com.geopista.app.catastro.GeopistaImportarUrbana2005;
import com.geopista.app.catastro.GeopistaImportarUrbana2006;
import com.geopista.app.catastro.GeopistaInformesCatastralesPanel;
import com.geopista.app.catastro.GeopistaMostrarPadron;
import com.geopista.app.catastro.GeopistaMostrarParcelas;
import com.geopista.app.catastro.GeopistaMostrarTramosVias;
import com.geopista.app.catastro.GeopistaSeleccionarFicheroParcelas;
import com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasDespuesMenuPanel;
import com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasDespuesPanel;
import com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasPanel;
import com.geopista.app.inforeferencia.GeopistaEditarInfoReferenciaPanel;
import com.geopista.app.inforeferencia.GeopistaEnlazarPoliciasViasPanel;
import com.geopista.app.inforeferencia.GeopistaFusionarViasConPanel;
import com.geopista.app.inforeferencia.GeopistaFusionarViasPanel;
import com.geopista.app.inforeferencia.GeopistaGenerarMapaPanel;
import com.geopista.app.inforeferencia.GeopistaImportarCallejeroPanel;
import com.geopista.app.inforeferencia.GeopistaImportarOrtofoto;
import com.geopista.app.inforeferencia.GeopistaImportarOrtofoto2;
import com.geopista.app.inforeferencia.GeopistaImportarToponimosPanel;
import com.geopista.app.inforeferencia.GeopistaImportarToponimosPanel1;
import com.geopista.app.inforeferencia.GeopistaInformeNumerosPoliciaCatastroINEPanel;
import com.geopista.app.inforeferencia.GeopistaMostrarCallejeroPanel;
import com.geopista.app.infraestructuras.GeopistaEditarRedAbastecimientoPanel;
import com.geopista.app.infraestructuras.GeopistaEditarRedSaneamientoPanel;
import com.geopista.app.infraestructuras.GeopistaImportarInfraFin;
import com.geopista.app.infraestructuras.GeopistaImportarInfraPanel;
import com.geopista.app.infraestructuras.GeopistaImportarInfraPanel02;
import com.geopista.app.infraestructuras.GeopistaInfraestructurasGestorEventosPanel;
import com.geopista.app.patrimonio.GeopistaDiferentesSuperficiesPanel;
import com.geopista.app.patrimonio.GeopistaEditarPatrimonioPanel;
import com.geopista.app.patrimonio.GeopistaGenerarMapaPatrimonio;
import com.geopista.app.patrimonio.GeopistaSigapPanel;
import com.geopista.app.planeamiento.GeopistaEditarAmbitosGestionPanel;
import com.geopista.app.planeamiento.GeopistaEditarDominiosPlaneamiento;
import com.geopista.app.planeamiento.GeopistaEditarPlaneamientoPanel;
import com.geopista.app.planeamiento.GeopistaExpedirInformeUrbanisticoPanel;
import com.geopista.app.planeamiento.GeopistaGenerarMapaPlaneamientoPanel;
import com.geopista.app.planeamiento.GeopistaGestorEventosPanel;
import com.geopista.app.planeamiento.GeopistaImportacionLog;
import com.geopista.app.planeamiento.GeopistaImportacionPanel;
import com.geopista.app.planeamiento.GeopistaImportarAmbitos01Panel;
import com.geopista.app.planeamiento.GeopistaImportarPlaneamiento01Panel;
import com.geopista.app.planeamiento.GeopistaImportarPlaneamiento02Panel;
import com.geopista.app.planeamiento.GeopistaInformeParcelasAfectadasPanel;
import com.geopista.app.reports.GeopistaDiscrepanciasIBI;
import com.geopista.app.reports.GeopistaGeneradorListadosCampos;
import com.geopista.app.reports.GeopistaGeneradorListadosDatosGenerales;
import com.geopista.app.reports.GeopistaGeneradorListadosDatosGeneralesInfraestructuras;
import com.geopista.app.reports.GeopistaGeneradorListadosEncabezado;
import com.geopista.app.reports.GeopistaGeneradorListadosSeleccionInforme;
import com.geopista.app.reports.GeopistaGeneradorListadosUnionesTablas;
import com.geopista.app.reports.GeopistaGeneradorListadosWhere;
import com.geopista.app.reports.GeopistaLanzarDisenadorInformes;
import com.geopista.app.reports.wizard.GenericReportWizard;
import com.geopista.app.reports.wizard.ReportWizard;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.editor.GeopistaEditor;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

public class GeopistaInicio extends JFrame implements HyperlinkListener, ActionListener {
	/**
	 * Logger for this class
	 */
	 private static final Log logger;
	    static {
	       createDir();
	       logger  = LogFactory.getLog(GeopistaInicio.class);
	    } 	
	//private static final Log logger = LogFactory.getLog(GeopistaInicio.class);

	private JEditorPane browser = new JEditorPane ( ) ; // The main HTML pane
//	private GeopistaEditor geopistaEditorBean;

//	Contexto de la aplicación
	private ApplicationContext appContext;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	public final static String LAST_IMPORT_DIRECTORY = "lastImportDirectory";
	private Blackboard blackboardInformes = aplicacion.getBlackboard();
	private GeopistaEditor geo2 = null;
	private JScrollPane browserScrolled;
	
	private static boolean fromInicio=true;


    public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }	
	public GeopistaInicio ( ) {
//		setTitle ("GEOPISTA") ;
		aplicacion.setMainFrame(this);  
		aplicacion.initHeartBeat(); // Forces to start HeartBeat
		/*try
    {
      ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
      geopistaEditorBean = (GeopistaEditor)Class.forName("com.geopista.editor.GeopistaEditor").newInstance();
      geopistaEditorBean.addPlugIn(zoomToFullExtentPlugIn);
      geopistaEditorBean.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
      geopistaEditorBean.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");


    }catch(Exception e)
    {
	if (logger.isDebugEnabled())
	{
	logger.debug("GeopistaInicio() - Excepcion editor");
	}
	logger.error("GeopistaInicio()", e);
    }*/

		// inicializa el contexto de aplicación para informar a todos
		// los componentes.

		appContext= new AppContext();
		//appContext.setEditor(geopistaEditorBean);
		appContext.setMainFrame(this);

		setBounds ( 0, 0 , 820, 620 ) ;
		browser.setEditable ( false ) ;
		browser.addHyperlinkListener ( this ) ;
		browser.setContentType("text/html");

		// JPanel panel = new JPanel ( ) ;
		// panel.setLayout ( new BoxLayout ( panel, BoxLayout.X_AXIS ) ) ;
		Container cp = this.getContentPane() ;
		cp.setLayout(new CardLayout());
		// cp.add ( panel, "North" ) ;
		//  cp.add ( new JScrollPane ( browser, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ) ) ;
		cp.add ( browserScrolled= new JScrollPane ( browser ) ,"primerplano") ;

		//ASO pongo el icono de geopista.
		ClassLoader cl = this.getClass().getClassLoader();
		java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(cl.getResource("img/geopista.gif"));
		setIconImage(img);
		//
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			jbInit();
		}
		catch(Exception e)
		{
			logger.error("GeopistaInicio()", e);
		}

		
		

	}
	
	public void login(){
		// ******************
		// Mostramos la pantalla de autenticación del usuario.
		// ******************
		// --------NUEVO-------------->
//		if (SSOAuthManager.isSSOActive()) {
//			com.geopista.app.administrador.init.Constantes.url = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//			CConstantesComando.loginGeopistaInicio = AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
//		}
		
	    com.geopista.protocol.CConstantesComando.loginGeopistaInicio = aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+ WebAppConstants.GEOPISTA_WEBAPP_NAME;

		SSOAuthManager.ssoAuthManager("GeopistaInicio");
		if (!AppContext.getApplicationContext().isOnlyLogged()) {
			// -------FIN-NUEVO----------->
			if (fromInicio) {
				if (!showAuth()) {
					dispose();
					return;
				}
			} else {
				showAuth();
			}
			// --------NUEVO-------------->
		}
		// -------FIN-NUEVO----------->
		
		//setPolicy();
		
		if (!AppContext.seleccionarMunicipio((Frame) this)) {
			stopApp();
			return;
		}
	}
	
	private void stopApp() {
		JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
				"Inicio de aplicación cancelado. Se cerrará el aplicativo");
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		System.exit(1);
		
	}
	
	public static void main (String[] args) {

		String initURL="inicio_lcgIII.htm";
		String otherURL="";
		        
		if (args.length>0)
		{
			for (int i=0;i< args.length;i++)
			{
				if (args[i].equalsIgnoreCase("-URL"))
				{
					otherURL=args[i+1];
					break;
				}
			}
		
			if (!otherURL.equals(""))
			{
				URL direccion=HTMLLoader.getPath(otherURL);
				if (direccion!=null)
					initURL=otherURL;
			}
		}

		URL initPage= HTMLLoader.getPath(initURL);
		GeopistaInicio appFrame = new GeopistaInicio();
		appFrame.show() ;

		logger.info("main() - mostrando pagina " + initURL + "= "
				+ initPage);

		appFrame.displayPage(initPage);
		appFrame.setTitle(":: LocalGIS ::");
		appFrame.setVisible(true);
		appFrame.setSize(850,660);

		appFrame.setVisible(true);

		appFrame.login();
		
		
		// No entiendo porque esta este código tras el setVisible anterior
//		if ( args.length > 0 ) {
//		try
//		{
//		initPage = new URL(args[0]) ;
//		appFrame.displayPage ( initPage ) ;
//		} catch (MalformedURLException e)
//		{
//		e.printStackTrace();
//		}

//		}
	}
	
	public void displayPage ( URL page ) {
//		if ( page != null && page.trim().length() > 0 ) {
//		File localFile = new File ( page ) ;
//		if (localFile.exists ( ) && localFile.isFile ())
//		{
//		page = "file:///" + localFile.getAbsolutePath();
		try {
			browser.setPage(page);
		}
		catch ( Exception e1 ) {
			browser.setText ( "Could not load page:" + page + "\n" +
					"Error:" + e1.getMessage ( ) ) ;
			logger.debug("displayPage(page = " + page
					+ ") - No se puede cargar recurso imagen:");
		}
////		}
//		else
//		{
//		try {
//		URL url = new URL (page) ;
//		browser.setPage (url) ;
//		}
//		catch (Exception e) {
//		browser.setText ("Could not load page:" + page + "\n" +
//		"Error:" + e.getMessage()) ;
//		}
//		}
//		}
//		else {
//		browser.setText ("Could not load page:" + page) ;
//		}
	}

	public void hyperlinkUpdate (HyperlinkEvent e) {
		setTitle(":: LocalGIS ::");
		if ( e.getEventType ( ) == HyperlinkEvent.EventType.ACTIVATED ) {
			try {
				String description = e.getDescription().substring(e.getDescription().lastIndexOf('/')+1);
				if (logger.isDebugEnabled())
				{
					logger.debug("hyperlinkUpdate(HyperlinkEvent)" + description);
				}
				URL url = e.getURL ( ) ;
				String url1 = url.toString();
				if (url1.endsWith("htm")) {
					browser.setPage ( url ) ;
				}else if (description.equals("noDisponible")){
					ResourceBundle rb = ResourceBundle.getBundle("Geopista");
					String mensaje = rb.getString("opcionNoDisponible");
					JOptionPane dialog = new JOptionPane();
					JOptionPane.showConfirmDialog( this, mensaje,":: LocalGIS ::", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null );
					this.getContentPane().add(dialog);
				}else if (description.equals("com.geopista.app.browser.GeopistaBrowser")){
					GeopistaBrowser.openURL(aplicacion.getString("geopista.conexion.guiaurbana","http://localhost:8080/guiaurbana/index.jsp"));
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasPanel")){
					WizardComponent d = new WizardComponent(appContext,"Inforeferencia: Asociar Vías de Catastro e INE ", null);
					d.init(new WizardPanel[] {new GeopistaAsociarCatastroINEViasPanel(), new GeopistaInformeNumerosPoliciaCatastroINEPanel()});
					showAppPanel(d);
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasDespuesPanel")){
					WizardComponent d = new WizardComponent(appContext,"Inforeferencia: Asociar Vías de Catastro e INE ", null);
					//	d.init(new WizardPanel[] {new GeopistaAsociarCatastroINEViasPanel(), new GeopistaInformeNumerosPoliciaCatastroINEPanel()});
						d.init(new WizardPanel[] {new GeopistaAsociarCatastroINEViasDespuesMenuPanel(), 
								new GeopistaAsociarCatastroINEViasDespuesPanel()});
					//	d.init(new WizardPanel[] { new GeopistaAsociarCatastroINEViasDespuesPanel()});

						showAppPanel(d);
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaFusionarViasPanel")){
					GeopistaFusionarViasPanel panel = new GeopistaFusionarViasPanel("1",null);
					WizardComponent d = new WizardComponent(appContext,"Inforeferencia: Fusionar Vías ", null);
					d.init(new WizardPanel[] {panel});
					showAppPanel(d);
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaAsociarDatosINE01Panel")){
					/*WizardDialog d = new WizardDialog(this,
               "Asociar Datos del INE al callejero", null);
              d.init(new WizardPanel[] {
                new GeopistaAsociarDatosINE01Panel(), new GeopistaAsociarDatosINE02Panel()});
                d.setSize(750,600);
               d.setVisible(true);*/
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaEnlazarNumPoliciaPanel"))
				{

					/* WizardDialog d = new WizardDialog(this,
               "Enlazar números de policía y callejero", null);
              d.init(new WizardPanel[] {
                new GeopistaEnlazarNumPoliciaPanel()});
                d.setSize(750,600);
               d.setVisible(true);*/
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarCatastroPanel"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Inforeferencia: Importar datos de la D.G. del Catastro", null);
						d.init(new WizardPanel[] {new GeopistaSeleccionarFicheroParcelas(), new GeopistaMostrarParcelas()});//,new GeopistaFusionarViasPanel("3",null)} , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarSubparcelasPanel"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Inforeferencia: Importar datos de la D.G. del Catastro", null);
						d.init(new WizardPanel[] {(WizardPanel)Class.forName("com.geopista.app.catastro.GeopistaSeleccionarFicheroSubParcelas").newInstance(),(WizardPanel)Class.forName("com.geopista.app.catastro.GeopistaMostrarSubParcelas").newInstance()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
                }else if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarToponimosPanel"))
                {

                    //GeopistaImportarToponimosPanel panel = new GeopistaImportarToponimosPanel(this.geopistaEditorBean);

                    WizardComponent d = new WizardComponent(appContext,
                            "Inforeferencia: Importar topónimos y otros ficheros", null);
                    d.init(new WizardPanel[] {
                            new GeopistaImportarToponimosPanel1(),new GeopistaImportarToponimosPanel()});
                    showAppPanel(d);
                }else if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarOrtofoto")) {

                    WizardComponent d = new WizardComponent(appContext,
                            "Inforeferencia: Importar ortofoto", null);
                    GeopistaImportarOrtofoto2 geoImpOrto2 = new GeopistaImportarOrtofoto2();
                    d.init(new WizardPanel[] {
                            new GeopistaImportarOrtofoto(geoImpOrto2), geoImpOrto2});
                    showAppPanel(d);
                }else   if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarINEPanel"))
                {

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Inforeferencia: Importar información de referencia del INE", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarCallesSeleccionElemento(),new GeopistaMostrarTramosVias()} // , new GeopistaMostrarParcelas()}// , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}else   if (description.equals("com.geopista.app.inforeferencia.GeopistaEditarInfoReferenciaPanel"))
				{
					if(aplicacion.isOnline())
					{
						GeopistaEditarInfoReferenciaPanel panel = new  GeopistaEditarInfoReferenciaPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaUnirDatosCallesPanel"))
				{
				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarCallejeroPanel")){ 
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Inforeferencia: Importar datos del Callejero", null);
						d.init(new WizardPanel[] { 
								new GeopistaImportarCallejeroPanel(), new GeopistaMostrarCallejeroPanel(), new GeopistaFusionarViasConPanel(), new GeopistaEnlazarPoliciasViasPanel(),new GeopistaAsociarCatastroINEViasPanel()}

						);
						setTitle(getTitle() + " " + aplicacion.getI18nString("importar.callejero.titulo"));
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaGenerarInformePanel"))
				{
					if (aplicacion.isOnline()){
//						WizardComponent d = new WizardComponent(appContext,
//								"Inforeferencia: Informe de información de referencia", null);
//
//						blackboardInformes.put("tipoBanner","inf_referencia.png");
//
//						d.init(new WizardPanel[] { new GeopistaGeneradorListadosSeleccionInforme(),
//								new GeopistaGeneradorListadosDatosGenerales(), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
//						);
//						showAppPanel(d);
						
						ReportWizard reportWizard = new GenericReportWizard(appContext);						
						reportWizard.init();
						
						showAppPanel(reportWizard);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}else if (description.equals("com.geopista.app.inforeferencia.GeopistaGenerarInformeJaspertReportsPanel"))
				{
					WizardComponent d = new WizardComponent(appContext,
							"Inforeferencia: Informe de información de referencia", null);

					blackboardInformes.put("tipoBanner","inf_referencia.png");

					d.init(new WizardPanel[] { new GeopistaGeneradorListadosSeleccionInforme(),
							new GeopistaGeneradorListadosDatosGenerales(), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
					);
					showAppPanel(d);
//					JOptionPane.showMessageDialog(this,"Invocando iReport");
//					launchIReport();

				}else  if (description.equals("com.geopista.app.inforeferencia.GeopistaGenerarMapaPanel"))
				{

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Información de Referencia: Generar Mapa de Inforamción de Referencia", null);
						GeopistaGenerarMapaPanel geo = new  GeopistaGenerarMapaPanel();
						d.init(new WizardPanel[] {
								geo
						});
						geo2=geo.getGeopistaEditor();



						d = GeopistaPrintPlugIn.addPanels(d,geo2.getContext());
						blackboardInformes.put("geopistaPrintPlugInPrintDialog",d);
						d.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WizardComponent comp = (WizardComponent)e.getSource();
								int id= e.getID();
								String command = e.getActionCommand();
								if ("finished".equals(command)){

									GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
									GeopistaUtil.executePlugIn(geopistaPrint,geo2.getContext(),new TaskMonitorManager());
								}

							}
						});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}





					// panel = new  GeopistaEditarInfoReferenciaPanel();

				}

				//IMPORTAR DATOS DE PADRON DE HABITANTES
				else   if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarPadronPanel"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Inforeferencia: Importar información del padron de habitantes", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarPadron(),new GeopistaMostrarPadron(false),
								new com.geopista.app.inforeferencia.ReferenciarViasPanel()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}
				//IMPORTAR DATOS DE PADRON DE HABITANTES GRABANDO ESTADISTICAS
				else   if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarPadronPanel.GrabarEstadisticas"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Inforeferencia: Importar información del padron de habitantes", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarPadron(),new GeopistaMostrarPadron(true)
/*añadido nuevo panel*/			,new com.geopista.app.inforeferencia.ReferenciarViasPanel()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}
				//IMPORTAR DATOS DE ACTIVIDADES ECONOMICAS
				else   if (description.equals("com.geopista.app.inforeferencia.GeopistaImportarActividadesEconomicas"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Inforeferencia: Importar datos actividades economicas", null);
						d.init(new WizardPanel[] {
								(WizardPanel)Class.forName("com.geopista.app.acteconomicas.ImportarActividadesEconomicas").newInstance(),
								(WizardPanel)Class.forName("com.geopista.app.acteconomicas.GeoReferenciarActividades").newInstance(),
								(WizardPanel)Class.forName("com.geopista.app.acteconomicas.MostrarActividadesEconomicas").newInstance()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}


				//CATASTRO
				else if (description.equals("com.geopista.app.catastro.GeopistaEditarCatastroPanel"))
				{
					if (aplicacion.isOnline())
					{
						GeopistaEditarCatastroPanel panel = new  GeopistaEditarCatastroPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}


				else if (description.equals("com.geopista.app.catastro.GeopistaImportarUrbana2005"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Urbana 2005", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarUrbana2005(), new GeopistaImportarPadronFin()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}	
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaImportarRustica2005"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Rústica 2005", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarRustica2005(), new GeopistaImportarPadronRusticaFin()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaImportarUrbana2006"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar Padrón Urbana 2006", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarUrbana2006(), new GeopistaImportarPadronFin2006()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (description.equals("com.geopista.app.catastro.GeopistaInformesCatastralesPanel"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Catastro: Informes Catastrales", null);
						d.init(new WizardPanel[] {
								new GeopistaInformesCatastralesPanel()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}else if (description.equals("com.geopista.app.catastro.GeopistaConsultarCatastroPanel"))
				{

					if (aplicacion.isOnline()){
						/*WizardComponent d = new WizardComponent(appContext, "Catastro: Consulta Catastro", null);
           d.init(new WizardPanel[] {
                new GeopistaConsultarCatastroPanel()});
           showAppPanel(d);*/
						GeopistaEditarCatastro panel = new GeopistaEditarCatastro();
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}else if (description.equals("com.geopista.app.reports.GeopistaDiscrepanciasIBI"))
				{
					if (aplicacion.isOnline()){
						try {
							GeopistaDiscrepanciasIBI ficha2 = new GeopistaDiscrepanciasIBI();
						}catch(Exception ex){		logger.error("hyperlinkUpdate(HyperlinkEvent)", ex);}
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}else  if (description.equals("ValidarCatastro"))
				{

					/* WizardDialog d = new WizardDialog(this,
              "CATASTRO", null);
          d.init(new WizardPanel[] {
            new GeopistaValidarReferenciaPanel(), new GeopistaReferenciaValidadaPanel()});
         d.setSize(750,600);
         d.setVisible(true);*/
				}
				else if (description.equals("com.geopista.app.catastro.GeopistaImportarFINURBPanel"))
				{
					if (aplicacion.isOnline())
					{   
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar FINURB", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarFINURBPanel(), new GeopistaFinImportacionPanel()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				else if (description.equals("com.geopista.app.catastro.GeopistaImportarFINCARUPanel"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext, "Catastro: Importar FINCARU", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarFINCARUPanel(), new GeopistaFinImportacionPanel()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				//INFRAESTRUCTURAS
				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaImportarInfraPanel"))
				{
					if(aplicacion.isOnline())
					{

						WizardComponent d = new WizardComponent(appContext, "Infraestructuras: Importar Infraestructuras", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarInfraPanel("A"),new GeopistaImportarInfraPanel02(), new  GeopistaImportarInfraFin()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}
				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaImportarInfraPanelSaneamiento"))
				{
					if(aplicacion.isOnline())
					{

						WizardComponent d = new WizardComponent(appContext, "Infraestructuras: Importar Infraestructuras", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarInfraPanel("S"),new GeopistaImportarInfraPanel02(), new  GeopistaImportarInfraFin()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}
				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaInfraestructurasGestorEventos"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext, "Infraestructuras", null);
						d.init(new WizardPanel[] {
								new GeopistaInfraestructurasGestorEventosPanel()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}
				}

				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaEditarRedAbastecimientoPanel"))
				{
					if(aplicacion.isOnline())
					{
						GeopistaEditarRedAbastecimientoPanel panel= new GeopistaEditarRedAbastecimientoPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaEditarRedSaneamientoPanel"))
				{
					if(aplicacion.isOnline())
					{
						GeopistaEditarRedSaneamientoPanel panel=  new GeopistaEditarRedSaneamientoPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}

				}

				else if (e.getDescription().equals("com.geopista.app.infraestructuras.GeopistaGenerarMapa"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Infraestructuars: Generar Mapa de Infraestructuras", null);
						com.geopista.app.infraestructuras.GeopistaGenerarMapaPanel geo = new  com.geopista.app.infraestructuras.GeopistaGenerarMapaPanel();
						d.init(new WizardPanel[] {
								geo
						});
						geo2=geo.getGeopistaEditor();



						d = GeopistaPrintPlugIn.addPanels(d,geo2.getContext());
						blackboardInformes.put("geopistaPrintPlugInPrintDialog",d);
						d.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WizardComponent comp = (WizardComponent)e.getSource();
								int id= e.getID();
								String command = e.getActionCommand();
								if ("finished".equals(command)){

									GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
									GeopistaUtil.executePlugIn(geopistaPrint,geo2.getContext(),new TaskMonitorManager());
								}

							}
						});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}
				//INFRAESTRUCTURAS
				/*else if (description.equals("com.geopista.app.infraestructuras.GeopistaImportarInfraPanel"))
      {

           WizardDialog d = new WizardDialog(this,
           "Importar Infraestructuras", null);
            d.init(new WizardPanel[] {
            new GeopistaImportarInfraPanel()});
            d.setSize(750,600);
            d.setVisible(true);
      }else if (description.equals("com.geopista.app.infraestructuras.GeopistaEstadisticas01Panel"))
      {

           WizardDialog d = new WizardDialog(this,
           "Generar Estadísticas", null);
            d.init(new WizardPanel[] {
            new GeopistaEstadisticas01Panel(), new GeopistaEstadisticas02Panel()});
            d.setSize(750,600);
            d.setVisible(true);
      }else if (description.equals("com.geopista.app.infraestructuras.GeopistaMantenimientoPanel"))
      {

           WizardDialog d = new WizardDialog(this,
           "Histórico, Estado y Gestor de Avisos", null);
            d.init(new WizardPanel[] {
            new GeopistaMantenimientoPanel()});
            d.setSize(750,600);
            d.setVisible(true);
      }else if (description.equals("com.geopista.app.infraestructuras.GeopistaGenerarInformeInfraPanel"))
      {

           WizardDialog d = new WizardDialog(this,
           "Generar Informe de Infraestructuras", null);
            d.init(new WizardPanel[] {
            new GeopistaGenerarInformeInfraPanel()});
            d.setSize(750,600);
            d.setVisible(true);
      }else if (description.equals("com.geopista.app.infraestructuras.GeopistaMapaInfraPanel"))
      {

           WizardDialog d = new WizardDialog(this,
           "Mapa Informe de Infraestructuras", null);
            d.init(new WizardPanel[] {
            new GeopistaMapaInfraPanel()});
            d.setSize(750,600);
            d.setVisible(true);
      }*/
//				PLANEAMIENTO

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaImportarPlaneamiento01Panel"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext,
								"Planeamiento: Importar Planeamiento", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarPlaneamiento01Panel(), new GeopistaImportarPlaneamiento02Panel(), new GeopistaImportacionPanel(), new GeopistaImportacionLog()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				} else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaImportarAmbitos01Panel"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext,
								"Planeamiento: Importar Ambitos Planeamiento", null);
						d.init(new WizardPanel[] {
								new GeopistaImportarAmbitos01Panel(), new GeopistaImportarPlaneamiento02Panel(), new GeopistaImportacionPanel(), new GeopistaImportacionLog()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaEditorDominios01Panel"))
				{
					appContext.setMainFrame(this);
					WizardComponent d = new WizardComponent(appContext,
							"Planeamiento: Editor Dominios", null);
					
					GeopistaEditarDominiosPlaneamiento panel=new GeopistaEditarDominiosPlaneamiento();
					panel.setWizardContext(d);
					panel.jbInit();
					d.init(new WizardPanel[] {
							panel});
					showAppPanel(d);
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaEditarPlaneamientoPanel"))
				{
					if(aplicacion.isOnline())
					{
						GeopistaEditarPlaneamientoPanel panel = new  GeopistaEditarPlaneamientoPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaEditarAmbitosGestionPanel"))
				{
					if(aplicacion.isOnline())
					{
						GeopistaEditarAmbitosGestionPanel panel = new  GeopistaEditarAmbitosGestionPanel();
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaGestorEventosPanel"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext,
								"Planeamiento: Gestor Avisos y Eventos", null);
						d.init(new WizardPanel[] {
								new GeopistaGestorEventosPanel()});
						showAppPanel(d);
					}

					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaExpedirInformeUrbanisticoPanel"))
				{

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Planeamiento: Informe Urbanistico", null);
						d.init(new WizardPanel[] {
								new GeopistaExpedirInformeUrbanisticoPanel()});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));
					}

				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaGenerarMapaPlaneamientoPanel"))
				{

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Planeamiento: Generar Mapa de Planeamiento", null);
						GeopistaGenerarMapaPlaneamientoPanel geo = new GeopistaGenerarMapaPlaneamientoPanel();
						geo.setWizardContext(d);
						geo.jbInit();
						d.init(new WizardPanel[] {
								geo
						});
						geo2=geo.getGeopistaEditor();

						d = GeopistaPrintPlugIn.addPanels(d,geo2.getContext());
						blackboardInformes.put("geopistaPrintPlugInPrintDialog",d);
						d.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WizardComponent comp = (WizardComponent)e.getSource();
								int id= e.getID();
								String command = e.getActionCommand();
								if ("finished".equals(command)){

									GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
									GeopistaUtil.executePlugIn(geopistaPrint,geo2.getContext(),new TaskMonitorManager());
								}

							}
						});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaInformeParcelasAfectadasPanel"))
				{

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Planeamiento: Informe Parcelas Afectadas", null);
						
						GeopistaInformeParcelasAfectadasPanel panel=new GeopistaInformeParcelasAfectadasPanel();
						panel.setWizardContext(d);
						panel.jbInit();
						d.init(new WizardPanel[] {
								panel});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}

				else if (e.getDescription().equals("com.geopista.app.planeamiento.GeopistaExpedirInformeUrbanisticoPanel"))
				{
					WizardComponent d = new WizardComponent(appContext, "Planeamiento", null);
					d.init(new WizardPanel[] {
							new GeopistaExpedirInformeUrbanisticoPanel()});
					showAppPanel(d);

				}

				else if (description.equals("com.geopista.app.infraestructuras.informe"))
				{

					if (aplicacion.isOnline()){
						blackboardInformes.put("tipoBanner","infraestructuras.png");
						WizardComponent d = new WizardComponent(appContext, "Generador de Listados", null);
						d.init(new WizardPanel[] { new GeopistaGeneradorListadosSeleccionInforme(),
								new GeopistaGeneradorListadosDatosGeneralesInfraestructuras("A"), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}

				else if (description.equals("com.geopista.app.infraestructuras.informeSaneamiento"))
				{

					if (aplicacion.isOnline()){
						blackboardInformes.put("tipoBanner","infraestructuras.png");
						WizardComponent d = new WizardComponent(appContext, "Generador de Listados", null);
						d.init(new WizardPanel[] { new GeopistaGeneradorListadosSeleccionInforme(),
								new GeopistaGeneradorListadosDatosGeneralesInfraestructuras("S"), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}


				else if (description.equals("com.geopista.app.reports.inicio"))
				{

					if (aplicacion.isOnline()){
						blackboardInformes.put("tipoBanner","patrimonio.png");
						WizardComponent d = new WizardComponent(appContext, "Generador de Listados", null);
						d.init(new WizardPanel[] { new GeopistaGeneradorListadosSeleccionInforme(),
								new GeopistaGeneradorListadosDatosGenerales(), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}
				else if (description.equals("com.geopista.app.patrimonio.lanza"))
				{

					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Generador de Listados", null);
						d.init(new WizardPanel[] {
								new GeopistaDiferentesSuperficiesPanel()} //, new GeopistaSupfNoCoincidentesPanel() ()}
						);
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}


				}
				else if (description.equals("com.geopista.app.patrimonio.lanzaSigap"))
				{
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext, "Ficha de Patrimonio", null);
						d.init(new WizardPanel[] {
								new GeopistaSigapPanel()}
						);

						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}

				}

				else if (e.getDescription().equals("com.geopista.app.catastro.GeopistaImportarParcelas"))
				{
					if(aplicacion.isOnline())
					{
						WizardComponent d = new WizardComponent(appContext,
								"Catastro: Importar Parcelas", null);
						d.init(new WizardPanel[] {
								//new GeopistaImportarParcelas(), new GeopistaImportarParcelas02(), new GeopistaImportarParcelas03()});
								new GeopistaImportarParcelas(), new GeopistaImportarParcelasMapa(), new GeopistaImportarParcelas02()});
						showAppPanel(d);
					}
					else
					{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}
				}
				/*else if (e.getDescription().equals("com.geopista.app.metadatos.CMainMetadatos"))
				{

					CMainMetadatos licencias = new  CMainMetadatos();

				}*/
				/*else if (e.getDescription().equals("com.geopista.app.contaminantes.CMainContaminantes"))

				{

					CMainContaminantes licencias = new  CMainContaminantes();

				}*/
				/*else if (e.getDescription().equals("com.geopista.app.catastro.gestorcatastral.MainCatastro")){
					
					MainCatastro catastro = new MainCatastro();
				}
				else if (e.getDescription().equals("com.geopista.app.inventario.MainInventario")){
					
					com.geopista.app.inventario.MainInventario inventario = new com.geopista.app.inventario.MainInventario();
				}
				
				else if (e.getDescription().equals("com.geopista.app.licencias.CMainLicencias"))
				{

					CMainLicencias licencias = new  CMainLicencias();

				}
				*/
				else if (e.getDescription().equals("com.geopista.app.patrimonio.EditarPatrimonio"))
				{
					GeopistaEditarPatrimonioPanel panel = new  GeopistaEditarPatrimonioPanel();

				}
				else if (e.getDescription().equals("com.geopista.app.patrimonio.GeopistaGenerarMapaPatrimonio")) {
					if (aplicacion.isOnline()){
						WizardComponent d = new WizardComponent(appContext,
								"Patrimonio: Generar Mapa de Patrimonio", null);
						GeopistaGenerarMapaPatrimonio geo = new  GeopistaGenerarMapaPatrimonio();
						d.init(new WizardPanel[] {
								geo
						});
						geo2=geo.getGeopistaEditor();



						d = GeopistaPrintPlugIn.addPanels(d,geo2.getContext());
						blackboardInformes.put("geopistaPrintPlugInPrintDialog",d);
						d.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WizardComponent comp = (WizardComponent)e.getSource();
								int id= e.getID();
								String command = e.getActionCommand();
								if ("finished".equals(command)){

									GeopistaPrintPlugIn geopistaPrint = new GeopistaPrintPlugIn();
									GeopistaUtil.executePlugIn(geopistaPrint,geo2.getContext(),new TaskMonitorManager());
								}

							}
						});
						showAppPanel(d);
					}else{
						JOptionPane.showMessageDialog(this,aplicacion.getI18nString("mensaje.no.conectado.a.la.base.datos"));

					}



				}


				//GENERALES
				else if (url1.endsWith("Dialog")) {
					if (logger.isDebugEnabled())
					{
						logger.debug("hyperlinkUpdate(HyperlinkEvent) - Dialog");
					}
					JDialog dialog = (JDialog)Class.forName(e.getDescription()).newInstance();
					this.setVisible(false);
					dialog.setVisible(true);
					dialog.setLocation(150, 90);
				} else  if (url1.endsWith("Panel")) {
					JPanel otroD = (JPanel)Class.forName(e.getDescription()).newInstance();
					otroD.setVisible(true);
					otroD.setLocation(150, 90);
					this.setVisible(false);
				}else  if (url1.endsWith("Frame")) {
					if (logger.isDebugEnabled())
					{
						logger.debug("hyperlinkUpdate(HyperlinkEvent) - Frame");
					}
					JFrame otroD = (JFrame)Class.forName(e.getDescription()).newInstance();
					otroD.setVisible(true);
					otroD.setLocation(150, 90);
					this.setVisible(false);
				}
			}
			catch ( Exception exc ) {
				logger.error("hyperlinkUpdate(HyperlinkEvent)", exc);
			}
		}
	}
	
	
	/**
	 *
	 */
	private WizardComponent old;
	private void showAppPanel(WizardComponent d)
	{
		if(!d.isCanceled())
		{
			if (old!=null)
				getContentPane().remove(old);
			getContentPane().add(d,"appPanel");
			((CardLayout)getContentPane().getLayout()).show(getContentPane(),"appPanel");
			old=d;
			
			//AppContext.seleccionarMunicipio((Frame)this);
		}
	}
	
	/*
	 * No está documentado que hace esto.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed ( ActionEvent e ) {
		URL page ;
		try
		{
			page = new URL("");
			displayPage ( page ) ;
		} catch (MalformedURLException e1)
		{
			browser.setText ( "Page could not be loaded:\n" +
					"Error:" + e1.getMessage ( ) ) ;
		}

	}

	private void jbInit() throws Exception
	{
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt){System.exit(0);}
		});

	}

	private void launchIReport(){
		// Obtengo la ruta donde estan las librerias .jar necesarias para
		// lanzar la aplicacion a partir del classpath actual
		
		System.out.println("Iniciando la invocacion de iReport");
		
		char cpSeparator = System.getProperty( "path.separator" ).charAt(0);
		String classPath = System.getProperty( "java.class.path" );
		String[] classPathEntries = classPath.split(Character.toString(cpSeparator));
		
		JOptionPane.showMessageDialog(this, "Classpath actual: " + classPath);
		JOptionPane.showMessageDialog(this, "Libarypath actual: " + System.getProperty( "java.library.path" ));
		
		String baseLibPath = null;
		
		for (int i  = 0 ; i < classPathEntries.length; i++){
			String classPathEntry = classPathEntries[i];
			int index = classPathEntry.indexOf("productos");
			if (index != -1){
				int endIndex = index - 1;
				if (endIndex > 0){
					baseLibPath = classPathEntry.substring(0, endIndex);
					//break;
				}
			}
		}
		
		String libPath = baseLibPath + File.separator + "productos" +
			File.separator + "ireport";
		
		System.out.println("Ruta base con las librerias de ireport: " + libPath);
		JOptionPane.showMessageDialog(this,"Ruta base con las librerias de ireport: " + libPath);
		
		StringBuffer sbClassPath = new StringBuffer();
		
		File productClassPathDir = new File(libPath);
		String[] fileList = productClassPathDir.list();

		if (fileList != null){
			for (int i = 0; i < fileList.length; i++){
				if (fileList[i].endsWith(".jar") || fileList[i].endsWith(".zip")){
					if (i != 0){
						sbClassPath.append(cpSeparator + libPath + File.separator 
								+ fileList[i]);
					}
					else {
						sbClassPath.append(libPath + File.separator + fileList[i]);
					}
				}
			}
		}
		
		String jre15Home = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.PREFERENCES_JRE15_HOME_KEY,
				UserPreferenceConstants.JRE15_DEFAULT_HOME, true);
		
		String iReportHome = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
				UserPreferenceConstants.DEFAULT_DATA_PATH, true);
		
		String[] cmdarray = new String[5];
		cmdarray[0] = jre15Home + File.separator + "bin" + File.separator + "java.exe";
		cmdarray[1] = "-cp";
		cmdarray[2] = "\"" + sbClassPath.toString() + "\"";
		cmdarray[3] = "-Direport.home=\"" + iReportHome + "\"";
		cmdarray[3] = cmdarray[3].replaceAll("\\\\", "\\\\\\\\");
		cmdarray[4] = "com.geopista.reports.launcher.Launcher";
		
		System.out.println("Ejecutable de java: " + cmdarray[0]);
		System.out.println("Classpath: " + cmdarray[2]);
		System.out.println("iReport Home: " + cmdarray[3]);
		System.out.println("Clase principal: " + cmdarray[4]);		
		
		File workDirFile = new File(iReportHome);
		try {
			Process proc = Runtime.getRuntime().exec(cmdarray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Comprueba que el usuario esta autorizado para acceder a la aplicacion y
	 * sus permisos para las diferentes acciones.
	 */
	private  boolean showAuth() {
		try {
			boolean resultado = false;
			
			//resetSecurityPolicy();
			CAuthDialog auth = new com.geopista.app.utilidades.CAuthDialog(
					this, true, CConstantesComando.loginGeopistaInicio,
					"GeopistaInicio",
					0,
					aplicacion.getI18NResource());
			/*
			 * FUNCIONA com.geopista.app.utilidades.CAuthDialog auth = new
			 * com.geopista.app.utilidades.CAuthDialog(this, true,
			 * CConstantesComando.loginLicenciasUrl,Constantes.idApp,
			 * ConstantesLocalGISEIEL.IdEntidad, aplicacion.getI18NResource());
			 */
			// // CAuthDialog auth = new CAuthDialog(this, true,
			// ConstantesLocalGISEIEL.localgisEIEL,ConstantesLocalGISEIEL.idApp,
			// ConstantesLocalGISEIEL.IdMunicipio,
			// aplicacion.getI18NResource());
			auth.setBounds(30, 60, 315, 155);
			if (fromInicio) {
				resultado = auth.showD(true);
				if (!resultado)
					return false;
			} else {
				auth.show();
			}
			// com.geopista.security.SecurityManager.setHeartBeatTime(1000000);
			// CAMBIADO
			/*
			 * com.geopista.security.SecurityManager.setHeartBeatTime(10000);
			 * GeopistaAcl acl =
			 * com.geopista.security.SecurityManager.getPerfil(
			 * "EIEL");//TODO:CAMBIAR cuando exista el ACL EIEL
			 * applySecurityPolicy(acl,
			 * com.geopista.security.SecurityManager.getPrincipal());
			 */
			//TOTOD setPolicy();

			return true;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al autenticar al usuario " + sw.toString());
			JOptionPane optionPane = new JOptionPane(
					"Error al inicializar: \n"
							+ ((e.getMessage() != null && e.getMessage()
									.length() >= 0) ? e.getMessage()
									: e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(this, "ERROR");
			dialog.setVisible(true);
			return false;
		}
	}


}
