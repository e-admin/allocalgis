package com.geopista.app.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.reports.elements.filter.ImageKeyElementFilter;
import com.geopista.app.reports.elements.filter.InteractiveMapImageReferencingParameterFilter;
import com.geopista.app.reports.elements.filter.JasperElementFilter;
import com.geopista.app.reports.elements.filter.MapImageElementFilter;
import com.geopista.app.reports.elements.filter.MapImageReferencingParameterFilter;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.reports.exceptions.ReportProcessingException;
import com.geopista.app.reports.maps.MapImageTypesParser;
import com.geopista.app.reports.maps.dao.MapReportDAO;
import com.geopista.app.utilidades.UtilsDrivers;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;

public class ReportsManager {

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ReportsManager.class);

	protected static AppContext appContext = (AppContext) AppContext.getApplicationContext();

	private SortedMap mapImageTypes;
	// Creamos la instancia directamente para que sea thread-safe
	private static ReportsManager instance = new ReportsManager();
	
	protected ReportsManager(){
	}

	public static ReportsManager getInstance(){
		return instance;
	}

	public List getReportList(){
		List reportList = new ArrayList();

		String baseReportsPath = ReportsContext.getInstance().getBaseReportsPath();

		File baseReportsDir = new File (baseReportsPath);
		if (baseReportsDir.exists() && baseReportsDir.isDirectory()){
			FilenameFilter jasperFilenameFilter = new JasperReportFileNameFilter();
			String[] reportFilenames = baseReportsDir.list(jasperFilenameFilter);

			if (reportFilenames != null){
				reportList = Arrays.asList(reportFilenames);
			}
		}
		else {
			// Ruta de informes no encontrada
		}

		return reportList;
	}

	public List getReportInfoList(){
		return getReportInfoList(true);
	}

	public List getReportInfoList(boolean filterSubreports){
		List reportList = new ArrayList();
		List filteredReportList = new ArrayList();
		Set subReportsSet = new HashSet();

		String baseReportsPath = ReportsContext.getInstance().getBaseReportsPath();

		File baseReportsDir = new File (baseReportsPath);
		if (baseReportsDir.exists() && baseReportsDir.isDirectory()){
			FilenameFilter jasperFilenameFilter = new JasperReportFileNameFilter();
			String[] reportFilenames = baseReportsDir.list(jasperFilenameFilter);

			for (int i = 0; i < reportFilenames.length; i++){
				File reportFile = new File(baseReportsPath, reportFilenames[i]);
				try {
					JasperDesign reportDesign = JRXmlLoader.load(reportFile);
					String reportName = reportDesign.getName();

					ReportInfo reportInfo = new ReportInfo();
					reportInfo.setFilename(reportFilenames[i]);
					reportInfo.setDescription(reportName);

					reportList.add(reportInfo);

					// Añadiendo subInformes
					List subReportsList = searchSubreports(reportDesign);
					if (subReportsList != null){
						subReportsSet.addAll(subReportsList);
					}
				} catch (JRException e) {
					// Report not found
					e.printStackTrace();
				}
			}

			if (filterSubreports){
				for (int i = 0; i < reportList.size(); i++){
					ReportInfo reportInfo = (ReportInfo) reportList.get(i);
					if (!subReportsSet.contains(reportInfo.getFilename())){
						filteredReportList.add(reportInfo);
					}
				}
			}
			else {
				filteredReportList = reportList;
			}
		}
		else {
			// Ruta de informes no encontrada
		}

		return filteredReportList;
	}

	public JasperReport loadReport(String reportName) throws ReportNotFoundException{
		JasperReport mainReport = null;

		try {
			JasperDesign jasperDesign;
			jasperDesign = compileReport(reportName, false);
			ArrayList<String> listaSubReports = new ArrayList<String>();
			getSubreports(jasperDesign, listaSubReports, true);
			mainReport = JasperCompileManager .compileReport(jasperDesign);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mainReport;
	}
	
	private void getSubreports(JasperDesign jasperDesign, ArrayList<String> listaSubReports, Boolean subReports) {

		JRChild hijo = null;
		JasperDesign jasperDesignSub = null;
		String nombre = "";
		String reportPath;
		if (subReports)
			reportPath = ReportsContext.getInstance().getBaseReportsPath() + File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS + File.separator;
		else	
			reportPath = ReportsContext.getInstance().getBaseReportsPath() + File.separator;

		JRBand[] bands = new JRBand[10];

		bands[0] = jasperDesign.getDetail();
		bands[1] = jasperDesign.getBackground();
		bands[2] = jasperDesign.getColumnFooter();
		bands[3] = jasperDesign.getColumnHeader();
		bands[4] = jasperDesign.getLastPageFooter();
		bands[5] = jasperDesign.getNoData();
		bands[6] = jasperDesign.getPageFooter();
		bands[7] = jasperDesign.getPageHeader();
		bands[8] = jasperDesign.getSummary();
		bands[9] = jasperDesign.getTitle();

		logger.debug("Procesando informe " + jasperDesign.getName());
		
		for(int x = 0; x < bands.length; x++) {
			if(bands[x] == null) {
				continue;
			}
			ArrayList<JRChild> elementosBanda = (ArrayList<JRChild>) bands[x].getChildren();
			for(int i = 0; i < elementosBanda.size(); i++) {
				hijo = elementosBanda.get(i);
				//Si es un sub-report lo procesamos
				if(hijo instanceof JRDesignSubreport) {
					nombre = getNombreSubreport((JRDesignSubreport) hijo);
					if(nombre != null) {
						//Si no está en la lista lo añadimos y lo procesamos. Los ya procesados no se repiten
						if(!listaSubReports.contains(nombre)) {
							listaSubReports.add(nombre);
							try {
								compileReport(nombre, true);
								jasperDesignSub = JRXmlLoader.load(reportPath + nombre);
								getSubreports(jasperDesignSub, listaSubReports, true);
							} catch(Exception e) {
								logger.warn("Error procesando subinforme " + nombre);
							}
						}
					}
				}
			}
		}
	}
	
	private String getNombreSubreport(JRDesignSubreport subreport) {
		JRDesignExpression expresion = (JRDesignExpression) subreport.getExpression();
		String strExpresion = expresion.getText();
		
		int posComillas = strExpresion.indexOf("\"");
		int posPunto = strExpresion.lastIndexOf(".");
		
		if(posComillas == -1 || posPunto == -1) {
			return null;
		}
		
		String nombre = strExpresion.substring(posComillas + 1, posPunto) + ".jrxml";
		
		return nombre;
	}
	
	private ArrayList getSubReports(JRReport report){
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
		report.getGroups();

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
					/*subreport.get*/
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

	private ArrayList searchSubreports(JRBaseReport report){
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

	private String getSubReportNameFromExpression(JRExpression expression){
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

	private JasperDesign compileReport(String reportName, Boolean subReports) throws ReportNotFoundException{
		JasperDesign jasperDesign = null;
		String reportPath;
		JasperReport report = null;
		String idAppType = (String)AppContext.getApplicationContext().getBlackboard().get(AppContext.idAppType);

		if (subReports)
			reportPath = ReportsContext.getInstance().getBaseReportsPath(idAppType) + File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS + File.separator + reportName;
		else	
			reportPath = ReportsContext.getInstance().getBaseReportsPath(idAppType) + File.separator + reportName;


		try {
			jasperDesign = JRXmlLoader.load(reportPath);
			report = JasperCompileManager .compileReport(jasperDesign);
		} catch (JRException e) {
			logger.error("Error al compilar el informe", e);
		}

		//report.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION);

		String compiledReportName = reportName.substring(0,reportName.indexOf(".jrxml"))
		+ ".jasper";

		String compiledReportDir = ReportsContext.getInstance().getBaseCompiledReportsPath(idAppType);
		String compiledReportPath;
		
		if (subReports)
			compiledReportDir = compiledReportDir + File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS;
			
		compiledReportPath= compiledReportDir + File.separator + compiledReportName;
		
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

		return jasperDesign;
	}
	
	private JasperReport loadSubReport(String reportPath) {
		InputStream reportFileStream;

	

		try {
			reportFileStream = new FileInputStream(reportPath);
		} catch (FileNotFoundException e1) {
			logger.error("No se ha encontrado el fichero con el informe:"+reportPath);
			return null;
		}

		JasperReport report = null;

		try {
			report = JasperCompileManager.compileReport(reportFileStream);
		} catch (JRException e) {
			logger.error("Error al compilar el informe", e);
		}

		
		return report;
	}

	public void previewReport(JasperReport report, Map parameters) throws ReportProcessingException{
		Connection conn = getConnection();
        try{
        	JasperPrint filledReport = fillReport(report, parameters, conn);
        	JasperViewer.viewReport(filledReport, false);
        }finally{
        	try{conn.close();}catch(Exception ex){}
        }
	}

	public JasperPrint fillReport(JasperReport report, Map parameters, 
			Connection dbConnection) throws ReportProcessingException {
		JasperPrint jasperPrint = null;

		try {
			jasperPrint = JasperFillManager.fillReport(report, parameters, 
					dbConnection);
		} catch (JRException e) {
			logger.error("Se ha producido un error al rellenar el informe ", e);
			e.printStackTrace();
			throw new ReportProcessingException();
		}

		return jasperPrint;
	}

	protected Connection getConnection() {
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
			e1.printStackTrace();
		}

		return conn;
	};

	private Connection getDBConnection (String username, String password,
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

	public List getImageElementsReferencingParameter(JasperReport report, String parameterName){
		ArrayList elementsReferencingParameter = new ArrayList();

		List reportElementsList = getReportElements(report);		
		for (int i = 0; i < reportElementsList.size(); i++){
			JRElement element = (JRElement) reportElementsList.get(i);

			if (element instanceof JRImage){
				JRImage image = (JRImage) element;
				JRExpression expression = image.getExpression();
				if (expression == null){
					continue;
				}

				String parameterExpression = "$P{" + parameterName +"}";
				if (expression.getText().indexOf(parameterExpression) != -1){
					elementsReferencingParameter.add(image);
				}
			}
		}

		return elementsReferencingParameter;
	}

	
	private List getReportElements(JasperReport report){
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

		ArrayList reportElementsList = new ArrayList();

		for (int i = 0; i < bands.length; i++){
			if (bands[i] == null){
				continue;
			}

			JRElement[] elements = bands[i].getElements();

			if (elements == null){
				continue;
			}

			reportElementsList.addAll(Arrays.asList(elements));
		}

		return reportElementsList;
	}

	private List getReportElements(JRReport report, JasperElementFilter elementFilter){
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

		ArrayList reportElementsList = new ArrayList();

		for (int i = 0; i < bands.length; i++){
			if (bands[i] == null){
				continue;
			}

			JRElement[] elements = bands[i].getElements();

			if (elements == null){
				continue;
			}

			for (int j = 0; j < elements.length; j++){
				if (elementFilter.matches(elements[j])){
					reportElementsList.add(elements[j]);
				}
			}
		}

		return reportElementsList;
	}

	public JRImage getImageElementByKey(JRReport report, String imageKey){
		ImageKeyElementFilter JasperImageKeyElementFilter = 
			new ImageKeyElementFilter(imageKey);

		
		
		//Recoremos la lista de subreports que pudiera tener el informe para buscar la imagen
		ArrayList subreports=getSubReports(report);
		
		//Buscamos la imagen en los informes hijo
		Iterator it=subreports.iterator();
		while (it.hasNext()){
			try {
				String reportName=(String)it.next();
				AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
				String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);
				String baseReportPath = localPath + File.separator +AppContext.REPORT_DIR_NAME + File.separator;
				String reportPath = baseReportPath + reportName;				
				JRReport subreport=loadSubReport(reportPath);
				//Si no lo encontramos lo buscamos en otro path
				if (subreport==null){
					return null;
				//String reportPath = SUREPORT_DIR + reportName;	
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Lo buscamos en el padre
		List images = getReportElements(report, JasperImageKeyElementFilter);

		if (images == null || images.size() == 0){
			return null;
		}
		else {
			// El key de los elementos es unico. Solo debiera haber como mucho un elemento en la lista
			return (JRImage) images.get(0);
		}
	}

	public List findMapImages(JRReport report){
		MapImageElementFilter jasperMapImageElementFilter = 
			new MapImageElementFilter();
		List images = getReportElements(report, jasperMapImageElementFilter);

		return images;
	}

	public List findImagesReferencingParameter(JRReport report,
			JRParameter parameter) {
		MapImageReferencingParameterFilter mapImageReferencingParameterFilter =
			new MapImageReferencingParameterFilter(parameter);

		List images = getReportElements(report, mapImageReferencingParameterFilter); 

		return images;
	}

	public List findInteractiveMapImagesReferencingParameter(JRReport report,
			JRParameter parameter, String scaleType) {
		InteractiveMapImageReferencingParameterFilter mapImageReferencingParameterFilter =
			new InteractiveMapImageReferencingParameterFilter(parameter, scaleType);

		List images = getReportElements(report, mapImageReferencingParameterFilter); 

		return images;
	}

	public class JasperReportFileNameFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (name.endsWith(".jrxml") || name.endsWith(".jxml")){
				return true;
			}

			return false;
		}
	}
	
	public SortedMap getMapImageTypes() {
		if (mapImageTypes == null) {
			mapImageTypes = new MapImageTypesParser().getMapImageTypes();
		}
	    return mapImageTypes;
	}
	public void logout(){
		try {
			if (appContext.isLogged()){
				SecurityManager.logout();
				SSOAuthManager.clearRegistrySesion();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List getMapasPrivadosPublicados() {
		Connection conn = null;
		try {
			conn = appContext.getConnection();
			return new MapReportDAO().getMapasPrivadosPublicados(conn, AppContext.getIdEntidad());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
	    return new ArrayList();
	}

	public List getCapasMapa(int idMap) {
		Connection conn = null;
		try {
			conn = appContext.getConnection();
			return new MapReportDAO().getCapasMapa(conn, idMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
	    return new ArrayList();
	}

	public List getColumnasCapa(int idCapa) {
		Connection conn = null;
		try {
			conn = appContext.getConnection();
			return new MapReportDAO().getColumnasCapa(conn, idCapa);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
	    return new ArrayList();
	}
	
	 
}
