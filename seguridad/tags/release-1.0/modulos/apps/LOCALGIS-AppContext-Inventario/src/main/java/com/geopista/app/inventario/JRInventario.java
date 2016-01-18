package com.geopista.app.inventario;

import javax.swing.*;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JRViewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import java.io.File;
import java.io.PrintWriter;

import com.geopista.app.AppContext;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.Lote;
import com.geopista.protocol.inventario.Versionable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 18-oct-2006
 * Time: 12:04:17
 * To change this template use File | Settings | File Templates.
 */
public class JRInventario {
	protected static Logger logger = Logger.getLogger(JRInventario.class);
	 
	/**
	 * realiza el informe utilizando el listado de bienes
	 * @param jrxmlfile
	 * @param pathDestino
	 * @param formato
	 * @param bienes
	 * @param desktop
	 * @param locale
	 * @throws Exception
	 */
    public static void generarReport(String jrxmlfile, String pathDestino, String formato, Collection<BienBean> bienes, JFrame desktop, String locale) throws Exception{

        /** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */
        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
        JasperReport jasperReport= JasperCompileManager.compileReport(jasperDesign);

        Map parametros= new HashMap();
        parametros.put("pathSubReportRefCatastrales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportRefCatastrales.jasper");
        parametros.put("pathSubReportMejoras", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportMejoras.jasper");
        parametros.put("pathSubReportObservaciones", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportObservaciones.jasper");
        parametros.put("pathSubReportUsosFuncionales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportUsosFuncionales.jasper");
        parametros.put("pathSubReportDocumentos", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportDocumentos.jasper");
        parametros.put("pathSubReportImagenes", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportImagenes.jasper");
        parametros.put("pathPlantillaCreditoDerecho", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportCreditos.jasper");
        parametros.put("locale", locale);
        parametros.put("id_municipio", new Integer(AppContext.getIdMunicipio()).toString());
        
     
        /** Rellenamos el report */
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, new JRInventarioDataSource(bienes, locale));

        if (formato.equalsIgnoreCase(""+Constantes.formatoPREVIEW)){
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
        }else if (formato.equalsIgnoreCase(""+Constantes.formatoPDF)){
            // Generamos el fichero PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, pathDestino);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoXML)){
            JasperExportManager.exportReportToXmlFile(jasperPrint, pathDestino, /*isEmbeddingImages*/false);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoPRINT)){
            /** Imprimimos el report */
            JasperPrintManager.printReport(jasperPrint, /*withPrintDialog*/true);
        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoHTML)){
            /** Or create HTML Report  */
            JasperExportManager.exportReportToHtmlFile(jasperPrint, pathDestino);
        }

    }
    
    /**
     * Realiza el report con una conexión directa a la base de datos
     * @param jrxmlfile Fichero con la plantilla
     * @param pathDestino PathDestino donde se guardará
     * @param formato Formato
     * @param bienes listado de bienes
     * @param desktop
     * @param locale 
     * @throws Exception
     */
    public static void generarReportConnectDB(String jrxmlfile, String pathDestino, String formato, Collection bienes, JFrame desktop, String locale) throws Exception{
    	Connection conn=null;
    	try{
	        /** Cargamos el JasperDesign desde el XML (jrxml) y lo compilamos para generar un JasperReport */
	        JasperDesign jasperDesign = JRXmlLoader.load(jrxmlfile);
	        JasperReport jasperReport= JasperCompileManager.compileReport(jasperDesign);
	        
	        conn = getConnection();
	
	        Map parametros= new HashMap();
	        parametros.put("pathSubReportRefCatastrales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportRefCatastrales.jasper");
	        parametros.put("pathSubReportMejoras", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportMejoras.jasper");
	        parametros.put("pathSubReportObservaciones", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportObservaciones.jasper");
	        parametros.put("pathSubReportUsosFuncionales", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportUsosFuncionales.jasper");
	        parametros.put("pathSubReportDocumentos", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportDocumentos.jasper");
	        parametros.put("pathSubReportImagenes", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportImagenes.jasper");
	        parametros.put("pathPlantillaCreditoDerecho", Constantes.PATH_PLANTILLAS_INVENTARIO+File.separator+"subReportCreditos.jasper");
	        parametros.put("ayuntamiento", "EXCMO. AYUNTAMIENTO DE "+Constantes.Provincia.toUpperCase());
	        parametros.put("municipio", Constantes.IdMunicipio + " - " + Constantes.Municipio + " (" + Constantes.Provincia + ")");
	        parametros.put("titulo","FICHA DE INVENTARIO DEL PATRIMONIO");
	        parametros.put("locale", locale);
	        parametros.put("id_municipio", new Integer(AppContext.getIdMunicipio()).toString());
	        
	        String listaBienes="";
	      
	        for (java.util.Iterator it=bienes.iterator();it.hasNext();){
	        	Object bien=it.next();
	        	if (bien instanceof Lote)
	        		listaBienes+="'"+((Lote)bien).getId_lote()+"',";
	        	else
	        		listaBienes+="'"+((Versionable)bien).getNumInventario()+"',";
	        }
	        listaBienes=listaBienes.substring(0, listaBienes.length()-1);
	        parametros.put("lista_bienes", listaBienes);
		      
	        //if (field.getName().startsWith("IMG_")){
	            /** En la plantilla jrxml, el campo de una imagen tiene que tener el siguien formato:
	             *  IMG_nombreFichero.ext (IMG_logo_geopista.gif) */
	            //String nombreFichero= field.getName().substring(4);
	            //return Constantes.PATH_PLANTILLAS_INVENTARIO + File.separator +"img" + File.separator + nombreFichero;
	        //}
	        //if (field.getName().equalsIgnoreCase("bienes_inventario.tipo"))
	 //          try{return bien.getTipo()!=null?Estructuras.getListaSubtipoBienesPatrimonio().getDomainNode(bien.getTipo()).getTerm(locale).toUpperCase():"";}catch(Exception e){ return "";}
	        
	        //if (field.getName().equalsIgnoreCase("bienes_inventario.uso"))
	   //        try{return bien.getUso()!=null?Estructuras.getListaUsoJuridico().getDomainNode(bien.getUso()).getTerm(locale):"";}catch(Exception e){ return "";}
	        
	        //if (field.getName().equalsIgnoreCase("bienes_inventario.adquisicion"))
	        //  try{return bien.getAdquisicion()!=null?Estructuras.getListaFormaAdquisicion().getDomainNode(bien.getAdquisicion()).getTerm(locale):"";}catch(Exception e){return "";}
	        
	        // if (field.getName().equalsIgnoreCase("cuentaContable"))
	        //	return bien.getCuentaContable();
	        
	         
	     
	        /** Rellenamos el report */
	        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parametros/*new HashMap()*/, conn);
	
	        if (formato.equalsIgnoreCase(""+Constantes.formatoPREVIEW)){
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
	        }else if (formato.equalsIgnoreCase(""+Constantes.formatoPDF)){
	            // Generamos el fichero PDF
	            JasperExportManager.exportReportToPdfFile(jasperPrint, pathDestino);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoXML)){
	            JasperExportManager.exportReportToXmlFile(jasperPrint, pathDestino, /*isEmbeddingImages*/false);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoPRINT)){
	            /** Imprimimos el report */
	            JasperPrintManager.printReport(jasperPrint, /*withPrintDialog*/true);
	        }/*Implementado pero nunca se ejecuta*/else if (formato.equalsIgnoreCase(""+Constantes.formatoHTML)){
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
			e1.printStackTrace();
		}

		return conn;
	};
	private static Connection getDBConnection (String username, String password,
			String thinConn, String driverClass) throws SQLException{
		Connection con = null;

		try {
			registerDriver(driverClass);
			//Class.forName(driverClass);
			DriverManager.setLogWriter(new PrintWriter((System.err)));
			con= DriverManager.getConnection(thinConn, username, password);
			con.setAutoCommit(false);
		} catch (Exception e){
			logger.error("Error al obtener la conexion con la base de datos.", e);
		}

		return con;
	}
	
	private static void registerDriver(String driverClass) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Enumeration enDrivers = DriverManager.getDrivers();
	    while(enDrivers.hasMoreElements()){
	    	DriverManager.deregisterDriver((java.sql.Driver)enDrivers.nextElement());
	    }
	    java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();
	
	    DriverManager.registerDriver(driver);
	}
}
