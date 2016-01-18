/**
 * WebEIELReport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.reports;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.reports.maps.MapImageFactory;
import com.vividsolutions.jump.workbench.ui.task.ITaskMonitor;

public class WebEIELReport {
	
	private static final Log logger = LogFactory.getLog(WebEIELReport.class);
	public WebEIELReport(){
		
    	/*Connection conn=null;
    	byte[] output=null;
		try {
			
			ConstantesLocalGISEIEL.SUREPORT_DIR="c:/Program Files/LocalGIS/plantillas.entidad/77/eiel/subreports/";
			ConstantesLocalGISEIEL.IMAGE_DIR="c:/Program Files/LocalGIS/plantillas.entidad/77/eiel/img/";
			ConstantesLocalGISEIEL.PATH_PLANTILLAS_EIEL="c:/Program Files/LocalGIS/plantillas.entidad/77/eiel";
			//String jrxmlfile="c:\\tmp\\plantillas\\eiel\\EIEL_equipamientos_EQ.jrxml";
			String jrxmlfile="c:/Program Files/LocalGIS/plantillas.entidad/77/eiel/EIEL_equipamientos_EQ.jrxml";
			//String jrxmlfile="plantillas/eiel\\EIEL_equipamientos_EQ.jrxml";
			//String jrxmlfile="c:\\Satec\\Proyectos\\LOCALGIS\\LOCALGIS.MODELO.EIEL\\3Codigo\\eielmodelo\\classes\\plantillas\\eiel\\EIEL_equipamientos_EQ.jrxml";

			
			String idMunicipio="33001";
			String idEntidad="77";
			HashMap listaFiltros=new HashMap();
			//listaFiltros.put("filtro_cc","and (eiel_t_cc.codprov='33' and eiel_t_cc.codmunic='001' and eiel_t_cc.clave='CC' and eiel_t_cc.codentidad='0401' and eiel_t_cc.codpoblamiento='01' and eiel_t_cc.orden_cc='017')");
			listaFiltros.put("filtro_cc","and (eiel_c_cc.id=6802)");
			String tipoElemento="CC";
			
			String locale="es_ES";
			String codEntidad="000";
			String codNucleo="000";
			String nucleoSeleccionado="000_000_Todos";
			String imprimirImagenes="No";
			String usarWMSExternos="No";
			String format="PDF";

			
			String user="postgres";
			String pass="A86m4F2e";
			String url="jdbc:postgresql://pamod-balanceada.c.ovd.interhost.com:5432/geopista";
			String driver="org.postgresql.Driver";
			conn = JREIELDatabase.getConnection(url,user,pass,driver);
			output=preGenerateReport(jrxmlfile,idMunicipio,idEntidad,listaFiltros,tipoElemento,
							locale,codEntidad,codNucleo,nucleoSeleccionado,imprimirImagenes,usarWMSExternos,conn,format);
			System.out.println("Size====>"+output.length);
			
			FileOutputStream fileOuputStream =  new FileOutputStream("C:\\tmp\\testing2.pdf"); 
		    fileOuputStream.write(output);
		    fileOuputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	 
		try{if (conn!=null)conn.close();}catch(Exception ex){}
		System.exit(1);
		*/
	}
	
	
	/**
	 * Compilamos el informe y los subinformes asociados
	 * @return
	 * @throws Exception
	 */
	 public static byte[] preGenerateReport(final String jrxmlfile, String idMunicipio,String idEntidad,final HashMap<String,String> listaFiltros, 
			 	final String tipoElemento, final String locale,
	    		final String codEntidad,final String codNucleo,final String nucleoSeleccionado,final String imprimirImagenes,
	    		final String usarWMSExternos, Connection conn,String format) throws Exception{
		 
		 
		 boolean SERVER_SIDE=true;
		 
		 //Valores por defecto para este tipo de informes que se ejecutan via web.
		 String pathDestino=null;
		 JFrame desktop=null; 
		 ITaskMonitor progressDialog=null;
		 
		 String formato="PDF";
		 if (format.equals("PDF"))
			 formato=String.valueOf(ConstantesLocalGISEIEL.formatoPDFRAW);
		 else if (format.equals("XML"))
			 formato=String.valueOf(ConstantesLocalGISEIEL.formatoXMLRAW);	 

		 
		 ArrayList<String> listaSubReports = new ArrayList<String>();
		 logger.debug("Cargando report: " + jrxmlfile);
		 JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
		 UtilsReport.obtenerSubreports(jasperDesign, listaSubReports,null,SERVER_SIDE);
		    
		 return generateReport(jrxmlfile, pathDestino, formato,idMunicipio,idEntidad,listaFiltros,tipoElemento,desktop,
					locale,codEntidad,codNucleo,nucleoSeleccionado,imprimirImagenes,usarWMSExternos,progressDialog,conn);
	 }

	
	
	 public static byte[] generateReport(final String jrxmlfile, final String pathDestino, final String formato, 
	    		String idMunicipio,String idEntidad,final HashMap<String,String> listaFiltros, final String tipoElemento, final JFrame desktop, final String locale,
	    		final String codEntidad,final String codNucleo,final String nucleoSeleccionado,final String imprimirImagenes,
	    		final String usarWMSExternos,ITaskMonitor progressDialog, Connection conn) throws Exception{
		 
		 	
		 	byte[] output=null;
		 	
		 	try{
			 	/** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */
		        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
		        JasperReport jasperReport= JasperCompileManager .compileReport(jasperDesign);
		        //JasperReport jasperReport = loadReport(jrxmlfile);
		        
		        MapImageFactory.init(jasperReport);
		        
		        
		        String filtroSQL="";
		       
		        Map<String,Object> parametros= new HashMap();
		        parametros.put("locale", locale);
		        parametros.put("id_municipio", idMunicipio);
		        parametros.put("ID_ENTIDAD", idEntidad);
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
		        		parametros.put(nombreFiltro, " and 1=1");	 
	     			//parametros.put(nombreFiltro, "and 1=1 LIMIT 3");	 
		        }
	
		        /** Rellenamos el report */
		        
		        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, conn);
		        
		        
		       
		        if (progressDialog!=null)
		        	progressDialog.setVisible(false);
		        if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPREVIEW)){
		            /** Visualizacion del report en un dialogo */
		            JDialog viewer= new JDialog(desktop/*new JFrame()*/, true/*modal*/);
		            //try{viewer.setTitle(((AppContext) AppContext.getApplicationContext()).getI18nString("inventario.informes.tag14"));}catch(Exception e){};
		            try{viewer.setTitle("Previsualizacion");}catch(Exception e){};
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
		        else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoPDFRAW)){

		            output=JasperExportManager.exportReportToPdf(jasperPrint);
		        } 
		        else if (formato.equalsIgnoreCase(""+ConstantesLocalGISEIEL.formatoXMLRAW)){

		            output=JasperExportManager.exportReportToXml(jasperPrint).getBytes();
		        } 
		 	}
		 	finally{
		 		
		 	}
		 	
		 	return output;
	 }
	 
	 
	    	
	
	public static void main(String args[]){
		
		new WebEIELReport();
	}
}
