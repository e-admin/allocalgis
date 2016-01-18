package com.geopista.app.eiel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportsContext;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.reports.maps.MapImageEIELFactory;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.app.utilidades.UtilsDrivers;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class JREIEL {
	protected static Logger logger = Logger.getLogger(JREIEL.class);
	 
	
    
    private static ArrayList searchSubreports(JRBaseReport report){
		JRBand[] bands = new JRBand[10];

		bands[0] = report.getDetail();
		bands[1] = report.getBackground();
		bands[2] = report.getColumnFooter();
		bands[3] = report.getColumnHeader();
		bands[4] = report.getLastPageFooter();
		bands[5] = report.getNoData();
		bands[6] = report.getPageFooter();
		bands[7] = report.getPageHeader();
		bands[8] = report.getSummary();
		bands[9] = report.getTitle();

		ArrayList subreportNames = new ArrayList();

		for (int i = 0; i < bands.length; i++){
			if (bands[i] == null){
				continue;
			}

			JRElement[] elements = bands[i].getElements();

			if (elements == null){
				continue;
			}
			for (int j = 0; j < elements.length; j++){
				if (elements[j] instanceof JRSubreport){
					JRSubreport subreport = (JRSubreport) elements[j];
					JRExpression expression = subreport.getExpression();

					String reportName = getSubReportNameFromExpression(expression);
					if (reportName != null){
						subreportNames.add(reportName);
					}
				}
			}
		}

		return subreportNames;
	}
    
    
    private static String getSubReportNameFromExpression(JRExpression expression){
		// Lo ideal es evaluar la expresion, pero de momento no 
		// es posible, así que se presupone que en la expresión
		// aparece el nombre del informe entre comillas, al final
		// de la misma. Esto no siempre tiene por que ser cierto.
		String expressionText = expression.getText();
		int lastQuoteIndex = expressionText.lastIndexOf('\"');
		if (lastQuoteIndex != -1){
			String lastQuoteRemovedExpression = 
				expressionText.substring(0, lastQuoteIndex);

			int previousQuoteIndex = lastQuoteRemovedExpression.lastIndexOf('\"');
			if (previousQuoteIndex != -1){
				String reportName = lastQuoteRemovedExpression.substring(
						previousQuoteIndex + 1);

				if (reportName.indexOf(".jasper") != -1){
					reportName = reportName.replaceAll("\\.jasper", ".jrxml");
					return reportName;
				}
			}
		}

		return null;
	}
    public static JasperReport loadReport(String reportName) throws ReportNotFoundException{
		JasperReport mainReport = compileReport(reportName);

		ArrayList subreportFiles = searchSubreports(mainReport);

		for (int i = 0; i < subreportFiles.size(); i++){
			compileReport((String) subreportFiles.get(i));
		}

		return mainReport;
	}
    
    private static JasperReport compileReport(String reportName) throws ReportNotFoundException{
		InputStream reportFileStream;

		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

		String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);

		String baseReportPath = localPath + File.separator +
		AppContext.REPORT_DIR_NAME + File.separator;

		String reportPath = baseReportPath + reportName;

		try {
			reportFileStream = new FileInputStream(reportPath);
		} catch (FileNotFoundException e1) {
			logger.error("No se ha encontrado el fichero con el informe");
			throw new ReportNotFoundException();
		}

		JasperReport report = null;

		try {
			report = JasperCompileManager.compileReport(reportFileStream);
		} catch (JRException e) {
			logger.error("Error al compilar el informe", e);
		}

		//report.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION);

		String compiledReportName = reportName.substring(0,reportName.indexOf(".jrxml"))
		+ ".jasper";

		String compiledReportDir = ReportsContext.getInstance().getBaseCompiledReportsPath();

		String compiledReportPath = compiledReportDir + File.separator + compiledReportName;

		File compiledReportDirFile = new File(compiledReportDir);
		if (!compiledReportDirFile.exists()){
			compiledReportDirFile.mkdirs();
		}

		try {
			JRSaver.saveObject(report, compiledReportPath);
		} catch (JRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return report;
	}
    /**
     * Realiza el report con una conexión directa a la base de datos
     * @param jrxmlfile Fichero con la plantilla
     * @param pathDestino PathDestino donde se guardará
     * @param formato Formato
     * @param bienes listado de bienes
     * @param desktop
     * @param locale 
     * @param panel 
     * @throws Exception
     */
    public static boolean generarReportConnectDB(final String jrxmlfile, final String pathDestino, final String formato, 
    		final HashMap<String,String> listaFiltros, final String tipoElemento, final JFrame desktop, final String locale,
    		final String codEntidad,final String codNucleo,final String nucleoSeleccionado,final String imprimirImagenes,
    		final String usarWMSExternos, final JPanel panel) throws Exception{
    	
			boolean resultado=true;
			
			final TaskMonitorDialog progressDialog = new TaskMonitorDialog(desktop, null);
			progressDialog.setTitle("TaskMonitorDialog.Wait");
			progressDialog.addComponentListener(new ComponentAdapter() {
				public void componentShown(ComponentEvent e) {
					new Thread(new Runnable() {
						public void run() {
							try {
								
								ProcessCancel processCancel=null;			
		        				if (progressDialog!=null){				
		        					if (progressDialog!=null){
		        						processCancel=new ProcessCancel(progressDialog);
		        						processCancel.start();
		        					}
		        				}

		        				Connection conn=null;
								progressDialog.report("Generando informe....");
						    	try{
						    		
						    		/** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */
							        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
							        JasperReport jasperReport= JasperCompileManager .compileReport(jasperDesign);
						    		//JasperReport jasperReport = loadReport(jrxmlfile);
							        
							        try {
										AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
										int idMapaInformes1 = ConstantesLocalGISEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_INFORMES);
										int idMapaInformes2 = ConstantesLocalGISEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL);
										aplicacion.getBlackboard().put("mapa.informes",idMapaInformes1);
										aplicacion.getBlackboard().put("mapa.informes.alternativo",idMapaInformes2);
										
										aplicacion.getBlackboard().put("informes.usarwmsexternos",usarWMSExternos);
									} catch (Exception e1) {
									}
							        
							        MapImageEIELFactory.init(jasperReport);
							        
							        
							        String filtroSQL="";
							        conn = getConnection();
							        Map<String,Object> parametros= new HashMap();
							        parametros.put("locale", locale);
							        parametros.put("id_municipio", new Integer(AppContext.getIdMunicipio()).toString());
							        parametros.put("ID_ENTIDAD", new Integer(AppContext.getIdEntidad()).toString());
							        parametros.put("tipo_infra", tipoElemento);
							                
							        parametros.put("SUBREPORT_DIR", ConstantesLocalGISEIEL.SUREPORT_DIR);
							        parametros.put("IMAGE_DIR", ConstantesLocalGISEIEL.IMAGE_DIR);
							        
							        parametros.put("nucleo_seleccionado", nucleoSeleccionado);
							        
							        if ((imprimirImagenes!=null) && (imprimirImagenes.equals("Si"))){
							        	parametros.put("IMPRIMIR_IMAGENES", new Boolean(true));
							        }
							        else{
							        	parametros.put("IMPRIMIR_IMAGENES", new Boolean(false));
							        }
							        
							        //**********************************************************
							        //Introducimos los valores de codigo entidad y codigo nucleo
							        //**********************************************************
							        if ((codEntidad!=null) && (codNucleo!=null))
							        	if (!((codEntidad.equals("000")) && (codNucleo.equals("000")))){
							        		parametros.put("id_entidad", codEntidad);
							        		parametros.put("id_poblamiento", codNucleo);
							        	}
							        

							        //Posibilidades de filtro
							        //No recuperar ninguno: El valor por defecto tiene que ser "LIMIT 0".      
							        //Recuperar todos     : Fijo el valor del filtro al valor "and 1=1"
							        //Recuperar filtros   : Fijo el valor del filtro seleccionado por el usuario
							        
							        Iterator<String> it=listaFiltros.keySet().iterator();
							        while (it.hasNext()){
							        	String nombreFiltro=it.next();
							        	String valorFiltro=listaFiltros.get(nombreFiltro);
							        	if (!valorFiltro.equals(""))
							        		parametros.put(nombreFiltro, valorFiltro);	 
							        	else
							        		parametros.put(nombreFiltro, "and 1=1");	 
						        			//parametros.put(nombreFiltro, "and 1=1 LIMIT 3");	 
							        }

							        /** Rellenamos el report */
							        
							        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, conn);
							        
							        progressDialog.setVisible(false);
							        if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPREVIEW)){
							            /** Visualizacion del report en un dialogo */
							            JDialog viewer= new JDialog(desktop/*new JFrame()*/, true/*modal*/);
							            try{viewer.setTitle(((AppContext) AppContext.getApplicationContext()).getI18nString("inventario.informes.tag14"));}catch(Exception e){};
							            viewer.setSize(800,600);
							            viewer.setLocationRelativeTo(null);
							            JRViewer jrv= new JRViewer(jasperPrint);
							            viewer.getContentPane().add(jrv);
							            viewer.setVisible(true);
							            /** Visualizacion del report en el JasperViewer */
							            //JasperViewer.viewReport(jasperPrint, false);
							        }else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPDF)){
							            // Generamos el fichero PDF
							            JasperExportManager.exportReportToPdfFile(jasperPrint, pathDestino);
							          
							        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoXML)){
							            JasperExportManager.exportReportToXmlFile(jasperPrint, pathDestino, /*isEmbeddingImages*/false);
							        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPRINT)){
							            /** Imprimimos el report */
							            JasperPrintManager.printReport(jasperPrint, /*withPrintDialog*/true);
							        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoHTML)){
							            /** Or create HTML Report  */
							            JasperExportManager.exportReportToHtmlFile(jasperPrint, pathDestino);
							        }
						    	}finally{
						    		try{conn.close();}catch(Exception ex){}
						    	}
								
								
							} catch (Exception e) {
								logger.error("Error ", e);
								ErrorDialog.show(desktop, "ERROR","ERROR",	StringUtil.stackTrace(e));
								return;
							} finally {
								progressDialog.setVisible(false);
								progressDialog.dispose();
							}
						}
					}).start();
				}
			});
			//GUIUtil.centreOnWindow(progressDialog);
			progressDialog.setVisible(true);
    		
			
			
			
			if (progressDialog.isCancelRequested())
				resultado=false;
			
			return resultado;
    		
    		
	        

    }
    
    public static void generarReportCuadrosConnectDB( String jrxmlfile, HashMap paramHash, String pathDestino, String formato, 
    		 JFrame desktop, String locale) throws Exception{
    	Connection conn=null;
    	try{

	        /** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */	
	        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
	        jasperDesign.setLanguage(JRReport.LANGUAGE_JAVA);
	        JasperReport jasperReport= JasperCompileManager .compileReport(jasperDesign);
	        
	        String filtroSQL="";
	        conn = getConnection();
	        Map<String,Object> parametros= new HashMap();
	        parametros.put("FASE", (String)paramHash.get("fase"));
	        parametros.put("ISLA", (String)paramHash.get("isla"));
	        parametros.put("DIPU", (String)paramHash.get("dipu"));
	        parametros.put("CODPROV", (String)paramHash.get("codprov"));
	        parametros.put("MUNI", (String)paramHash.get("muni"));
	        parametros.put("CODMUNI", (String)paramHash.get("codmuni"));
	        parametros.put("WHERE", (String)paramHash.get("where"));
	        parametros.put("IMG", ConstantesLocalGISEIEL.PATH_PLANTILLAS_CUADROS_IMG_EIEL);
	        
	        

	        /** Rellenamos el report */
	        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, conn);
	
	        if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPREVIEW)){
	            /** Visualizacion del report en un dialogo */
	            JDialog viewer= new JDialog(desktop/*new JFrame()*/, true/*modal*/);
	            try{viewer.setTitle(((AppContext) AppContext.getApplicationContext()).getI18nString("inventario.informes.tag14"));}catch(Exception e){};
	            viewer.setSize(800,600);
	            viewer.setLocationRelativeTo(null);
	            JRViewer jrv= new JRViewer(jasperPrint);
	            viewer.getContentPane().add(jrv);
	            viewer.setVisible(true);
	            /** Visualizacion del report en el JasperViewer */
	            //JasperViewer.viewReport(jasperPrint, false);
	        }else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPDF)){
	            // Generamos el fichero PDF
	            JasperExportManager.exportReportToPdfFile(jasperPrint, pathDestino);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoXML)){
	            JasperExportManager.exportReportToXmlFile(jasperPrint, pathDestino, /*isEmbeddingImages*/false);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPRINT)){
	            /** Imprimimos el report */
	            JasperPrintManager.printReport(jasperPrint, /*withPrintDialog*/true);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoHTML)){
	            /** Or create HTML Report  */
	            JasperExportManager.exportReportToHtmlFile(jasperPrint, pathDestino);
	        }
    	}finally{
    		try{conn.close();}catch(Exception ex){}
    	}

    }
    
    
    /**
     * Obtiene una conexión con la base de datos
     * @return
     */
    protected static Connection getConnection() {
    	AppContext appContext = (AppContext) AppContext.getApplicationContext();
		String url = appContext.getString("conexion.url.jasper");
		String user = appContext.getString("conexion.user");
		String pass = appContext.getUserPreference("conexion.pass","",false);
		String driver = appContext.getString("conexion.driver");

		Connection conn = null;

		try {
			if (url.startsWith("jdbc:rmi")) {
	            conn = getDBConnection (user,pass,url, "org.objectweb.rmijdbc.Driver");         
			} else {
	            conn = getDBConnection (user,pass,url, driver);         
			}

		} catch (SQLException e1) {
//			throw new ReportProcessingException();
			logger.error("Contraseña del usuario:"+appContext.getUserPreference("conexion.pass","",false));
			e1.printStackTrace();
		}

		return conn;
	};
	private static Connection getDBConnection (String username, String password,
			String thinConn, String driverClass) throws SQLException{
		Connection con = null;

		try {
			UtilsDrivers.registerDriver(driverClass);
			//Class.forName(driverClass);
			
			con= DriverManager.getConnection(thinConn, username, password);
			con.setAutoCommit(false);
		} catch (Exception e){
			logger.error("Error al obtener la conexion con la base de datos.", e);
		}

		return con;
	}
	
}

