/**
 * GeopistaInformeUrbanistico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jimm.datavision.layout.swing.SwingLE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

/*public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{*/

public class GeopistaInformeUrbanistico  extends JPanel implements WizardPanel
{  
	public SwingLE le = new SwingLE();
	
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(GeopistaInformeUrbanistico.class);
	
	private static final String COMPILED_REPORT_DIR_NAME = "compiled";
	
	private static final String[] reportFiles = new String[] {
		"InformeUrbanistico.jrxml",
		"InformeUrbanistico_planeamientoRef.jrxml",
		"InformeUrbanistico_ambGes.jrxml",
		"InformeUrbanistico_califSuelo.jrxml",
		"InformeUrbanistico_clasSuelo.jrxml"
	};
	
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
	public int parcela=0;
	public Image mapa;

	
	public GeopistaInformeUrbanistico(int parcela, Image mapa) {
		this.parcela=parcela;
		this.mapa = mapa;
		try	{
			jbInit();
		}
		catch(Exception e) {
			logger.error("Error al inicializar el informe urbanistico", e);
		}   
	}
	
	private void jbInit() throws Exception {
		try {

			// Establecemos la conexion con los que vengan en la aplicación 
			Connection conn = abrirConexion();
			
			JasperPrint report = fillReport(conn);
			
			if (report!=null)
				JasperViewer.viewReport(report);
			else
				JOptionPane.showMessageDialog(this,"Error al generar el informe");
			
		} catch (Exception e){
			logger.error("Excepcion mientras se procesaba el informe", e);
		}

	} //del JbInit
	
	/**
	 * Abre la conexion con la base de datos
	 */
	public Connection abrirConexion() throws Exception {
		String url = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
		String user = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
		 String pass =aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
		//String pass = UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
		String driver = aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
		
		Connection conn = null;
		
		try {
			//Quitamos los drivers
			Enumeration e = DriverManager.getDrivers();
			while (e.hasMoreElements())
			{
				DriverManager.deregisterDriver((Driver)e.nextElement());
			}
			DriverManager.registerDriver(new org.postgresql.Driver());

			conn = getDBConnection (user,pass,url, driver);			
		}catch (Exception e){
			JOptionPane.showMessageDialog(this,aplicacion.getI18nString("acceso.no.permitido"));
		}
		
		return conn;
	};

	public static Connection getDBConnection (String username, String password,
			String thinConn, String driverClass) throws SQLException{
		Connection con = null;
		
		try {
			Class.forName(driverClass);
			con= DriverManager.getConnection(thinConn, username, password);
			con.setAutoCommit(false);
		} catch (Exception e){
			logger.error("Error al obtener la conexion con la base de datos.", e);
		}
		
		return con;
	}
	
	private JasperPrint fillReport(Connection dbConnection) throws ReportNotFoundException{
		JasperReport compiledReport = getCompiledReport();		
		
		JasperPrint jasperPrint = null;
		
		String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);
		String compiledReportPath = localPath + File.separator +
			COMPILED_REPORT_DIR_NAME + File.separator;
		
		HashMap parametersMap = new HashMap();
		parametersMap.put("SUBREPORT_DIR", compiledReportPath);
		parametersMap.put("ID_ENTIDAD", String.valueOf(aplicacion.getIdEntidad()));
		parametersMap.put("numeroParcela", new Integer(parcela));
		parametersMap.put("mapaParcela", mapa);
		parametersMap.put("idMunicipio",
				new Integer(aplicacion.getString("localgis.DefaultCityId")));
		
		try {
			jasperPrint = JasperFillManager.fillReport(compiledReport, parametersMap, 
				dbConnection);
		} catch (JRException e) {
			logger.error("Se ha producido un error al rellenar el informe ",e);			
			return jasperPrint;
		}
		
		return jasperPrint;
	}
	
	private JasperReport getCompiledReport() throws ReportNotFoundException{
		JasperReport  mainReport = null;
		
		for (int i = 0; i < reportFiles.length; i++){
			InputStream reportFileStream;
			String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);

			String reportPath = localPath + File.separator +
				UserPreferenceConstants.REPORT_DIR_NAME + File.separator + reportFiles[i];
			
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
				return null;
			}
			
			//report.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_NO_DATA_SECTION);
			//JasperReport 4 y 5
			report.setWhenNoDataType(WhenNoDataTypeEnum.NO_DATA_SECTION);
			
			
			if (i != 0){
				String compiledReportName = reportFiles[i].substring(0,
						reportFiles[i].indexOf(".jrxml")) + ".jasper";
				

				String compiledReportDir = localPath + File.separator +
					COMPILED_REPORT_DIR_NAME;
				
				File compiledReportDirFile = new File(compiledReportDir);
				if (!compiledReportDirFile.exists()){
					compiledReportDirFile.mkdirs();
				}
				
				String compiledReportPath = localPath + File.separator +
					COMPILED_REPORT_DIR_NAME + File.separator + compiledReportName;

				try {
					JRSaver.saveObject(report, compiledReportPath);
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				mainReport = report;
			}
		}
		
		return mainReport;
	}
	
	
	public void enteredFromLeft(Map dataMap){ }

	public void exitingToRight() throws Exception
	{
		//Para establecer el driver de Geopista

	}

	/**
	 * Tip: Delegate to an InputChangedFirer.
	 * @param listener a party to notify when the input changes (usually the
	 * WizardDialog, which needs to know when to update the enabled state of
	 * the buttons.
	 */
	public void add(InputChangedListener listener)
	{

	}

	public void setWizardContext(WizardContext wd)
	{
	}

	public void remove(InputChangedListener listener)
	{

	}

	public String getTitle()
	{
		return "";
	}

	public String getID()
	{
		return "2";
	}

	public String getInstructions()
	{
		return "";
	}

	public boolean isInputValid()
	{
		return true;
	}

	private String nextID=null;
	public void setNextID(String nextID)
	{
		this.nextID=nextID;
	}
	/**
	 * @return null to turn the Next button into a Finish button
	 */
	public String getNextID()
	{

		return nextID;
	}

	/* (non-Javadoc)
	 * @see com.geopista.ui.wizard.WizardPanel#exiting()
	 */
	public void exiting()
	{
		// TODO Auto-generated method stub

	}


} //de la Clase


