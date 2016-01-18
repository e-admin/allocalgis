/**
 * JREIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.awt.Dialog;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.reports.WebEIELReport;
import com.geopista.app.reports.ReportsContext;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class JREIEL {
	protected static Logger logger = Logger.getLogger(JREIEL.class);
	 
	
    
    private static ArrayList searchSubreports(JRBaseReport report){
		JRBand[] bands = new JRBand[10];

		JRBand designBand=getDetailBand(report);
		
		bands[0] = designBand;
				
		
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
    
    /**
	 * Obtiene la banda de detalle. La implementacion ha cambiado a partir de la version 4
	 * @param jasperDesignBuilder
	 * @param subReport
	 * @return
	 * @throws Exception
	 */
	public static JRBand getDetailBand(JRBaseReport report )
    {
			//Version 2.0
			//return report.getDetail();
		
			//Version 4.0
            JRSection jrSection = report.getDetailSection();
            for(JRBand jrBand : jrSection.getBands())
             {
                 if(jrBand instanceof JRDesignBand)
                 {
                     return (JRBand)jrBand;
                 }
             }
            return null;
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

		String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);

		String baseReportPath = localPath + File.separator +
				UserPreferenceConstants.REPORT_DIR_NAME + File.separator;

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
    		final String usarWMSExternos) throws Exception{
    	
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
		        				
		        				//Llamada interna para generar el informe
		        				generarReportConnectDB_Core(jrxmlfile, pathDestino, formato, 
		        						new Integer(AppContext.getIdMunicipio()).toString(),new Integer(AppContext.getIdEntidad()).toString(),
		        						listaFiltros, tipoElemento, desktop, locale,codEntidad,codNucleo,nucleoSeleccionado,imprimirImagenes,usarWMSExternos,progressDialog);
		        				
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
    
    public static void generarReportConnectDB_Core(final String jrxmlfile, final String pathDestino, final String formato, 
    		String idMunicipio,String idEntidad,final HashMap<String,String> listaFiltros, final String tipoElemento, final JFrame desktop, final String locale,
    		final String codEntidad,final String codNucleo,final String nucleoSeleccionado,final String imprimirImagenes,
    		final String usarWMSExternos,TaskMonitorDialog progressDialog) throws Exception{
    	
    	Connection conn=null;
    	if (progressDialog!=null)
    		progressDialog.report("Generando informe....");
    	try{
    		
    		
	        
	        try {
				AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
				if (InitEIEL.clienteLocalGISEIEL!=null){
					int idMapaInformes1 = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL_INFORMES);
					aplicacion.getBlackboard().put("mapa.informes",idMapaInformes1);										
					int idMapaInformes2 = InitEIEL.clienteLocalGISEIEL.getIdMapa(ConstantesLocalGISEIEL.NOMBRE_MAPA_EIEL);										
					aplicacion.getBlackboard().put("mapa.informes.alternativo",idMapaInformes2);
				}
				if (aplicacion!=null)
					aplicacion.getBlackboard().put("informes.usarwmsexternos",usarWMSExternos);
			} catch (Exception e1) {
				//e1.printStackTrace();
			}
	        
	        conn = JREIELDatabase.getConnection();
	        WebEIELReport.generateReport(jrxmlfile, pathDestino, formato,idMunicipio,idEntidad,listaFiltros, tipoElemento,
	        		desktop, locale,codEntidad,codNucleo,nucleoSeleccionado,imprimirImagenes,usarWMSExternos,progressDialog,conn);
	        
	        
    	}finally{ 
    		try{if (conn!=null)conn.close();}catch(Exception ex){}
    	}
		
		
	
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
	        conn = JREIELDatabase.getConnection();
	        Map<String,Object> parametros= new HashMap();
	        parametros.put("FASE", (String)paramHash.get("fase"));
	        parametros.put("ISLA", (String)paramHash.get("isla"));
	        parametros.put("DIPU", (String)paramHash.get("dipu"));
	        parametros.put("CODPROV", (String)paramHash.get("codprov"));
	        parametros.put("MUNI", (String)paramHash.get("muni"));
	        parametros.put("CODMUNI", (String)paramHash.get("codmuni"));
	        parametros.put("WHERE", (String)paramHash.get("where"));
	        parametros.put("IMG", ConstantesLocalGISEIEL.PATH_PLANTILLAS_CUADROS_IMG_EIEL);
	        
	        
	        logger.info("Generando Informe:"+jrxmlfile);
	        /** Rellenamos el report */
	        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, conn);
	
	        if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPREVIEW)){
	            /** Visualizacion del report en un dialogo */
	            //JDialog viewer= new JDialog(desktop/*new JFrame()*/, true/*modal*/);
	            JDialog viewer= new JDialog(desktop, "", Dialog.ModalityType.DOCUMENT_MODAL);
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
	        logger.info("Informe generado:"+jrxmlfile);
    	}finally{
    		try{conn.close();}catch(Exception ex){}
    	}

    }
    
    
   
	
	
}

