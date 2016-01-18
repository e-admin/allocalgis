/*
 * Created on 09-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.update.dialogs;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.GZIPInputStream;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JEditorPane;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.pdfbox.examples.pdmodel.ReplaceString;
import org.xml.sax.DTDHandler;
import org.xml.sax.helpers.DefaultHandler;
import com.geopista.app.AppContext;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.server.ortofoto.ImportadorOrtofotos;
import com.geopista.util.GeopistaUtil;
import com.ice.tar.TarInputStream;
import com.ice.tar.TarEntry;
import com.lowagie.tools.concat_pdf;

class KillProcess extends TimerTask {

    private String processName;
    private Process process;
    private StreamGlobberParametros outputGlobber;
    private StreamGlobberParametros errorGlobber;
    
    public KillProcess(String processName, Process process, StreamGlobberParametros outputGlobber, StreamGlobberParametros errorGlobber) {
        this.processName = processName;
        this.process = process;
        this.outputGlobber = outputGlobber;
        this.errorGlobber = errorGlobber;
    }
    
    public void run() {
        System.out.println("Matamos al proceso \""+processName+"\" por timeout.");
        process.destroy();
        outputGlobber.interrupt();
        errorGlobber.interrupt();
    }
    
}

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UpdateSystemFiles
{
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private static String VERIFY_ADM_CAR_FILE = "admcar/lib/server.jar";
    
    private static String VERIFY_GEOPISTA_WMS_MANAGER_FILE = "webapps/GeoPISTA-WMSManager/WEB-INF/struts-config.xml";
    
    private static String VERIFY_GEOPISTA_WMS_MANAGER_INTRANET_FILE = "webapps/GeoPISTA-WMSManager/WEB-INF/struts-config.xml";
    
    private static String VERIFY_GUIAURBANA = "webapps/guiaurbana/WEB-INF/struts-config.xml";
    
    private static String VERIFY_VISUALIZADOR = "webapps/visualizador/WEB-INF/struts-config-visualizador.xml";
    
    private static String VERIFY_SOFTWARE = "webapps/software/geopista.jnlp";
    
    private static String TEMP_DIRECTORY = "temp/updates";
    
    private static String UPDATE_FILE_XML = "update.xml";
    
    public static String GEOPISTA_ROOT = "%GEOPISTA_ROOT%";
    
    public static String MAPSERVER_FILE = "%MAPSERVER_FILE%";
    
    public static String GEOPISTA_ROOT_ENCODED = "%GEOPISTA_ROOT_ENCODED%";

    public static String DIR_ORTOFOTOS = "%DIR_ORTOFOTOS%";
    
    public static String PUERTO_INST = "%PUERTO_INST%";
    
    public static String PUERTO_CONTROL = "%PUERTO_CONTROL%";
    
    public static String PATH_JDK = "%JDK15%";

    public static String URL_BD = "%URL_BD%";

    public static String PASSWORD = "%PASSWORD%";

    public static String HOST_DB = "%HOST_DB%";

    public static String PORT_DB = "%PORT_DB%";

    public static String NAME_DB = "%NAME_DB%";

    public static final String TOMCAT = "%TOMCAT%";

    public static final String IP_MAPSERVER = "%IP_MAPSERVER%";

    public static final String PORT_MAPSERVER = "%PORT_MAPSERVER%";
    
    public static final String TOMCAT_DIRECTORY = "%TOMCAT_DIRECTORY%";

    public static String SUBSTITUTIONS_STRINGS = "Substitutions Strings"; 
    
    private boolean tomcatStop = false;
    
    private boolean tomcatErrorStop = false;
    
    private boolean admCarStop = false;
    
    private boolean admCarErrorStop = false;
    
    public static String ADM_CAR_BASE_PATH = "admcar";
    
    public static String GEOPISTA_WMS_MANAGER_BASE_PATH = "webapps/GeoPISTA-WMSManager";
    
    public static String GEOPISTA_WMS_MANAGER_INTRANET_BASE_PATH = "webapps/GeoPISTA-WMSManager-Intranet";
    
    public static String GUIAURBANA_BASE_PATH = "webapps/guiaurbana";
    
    public static String VISUALIZADOR_BASE_PATH = "webapps/visualizador";
    
    public static String SOFTWARE_BASE_PATH = "webapps/software";
      
    public static String DEFAULT_WINDOWS_INSTALL_PATH = "C:/Archivos de programa/LocalGIS";
    
    public static String DEFAULT_LINUX_INSTALL_PATH = "/usr/local/LocalGIS";
    
    private static String BACKUP_DIRECTORY = "backup";
    
    private static String WEB_BASE_PATH = "webapps";
  
//    public static final String TOMCAT_DIR = "/apache-tomcat-6.0.14";

    public boolean verifyInstallSystemDirectory()
    {
        return true;
    }
    
    public boolean verifyAllSystemInstall(String directoryPathString)
    {
        int contador = 0;
        if (verifyAdmCarInstall(directoryPathString) == true)
            contador++;
        if (verifyGeopistaWmsManager(directoryPathString) == true)
            contador++;
        if (verifyGeopistaWmsManagerIntranet(directoryPathString) == true)
            contador++;
        if (verifyGuiaurbana(directoryPathString) == true)
            contador++;
        if (verifyVisualizador(directoryPathString) == true)
            contador++;
        if (verifySoftware(directoryPathString) == true)
            contador++;
        
        if (contador == 0)
            return false;
        return true;
    }
    
    public boolean verifyModuloInstall(String directoryPathString, String fileVerify)
    {
    	//Si el campo de verificacion es vacio no realizamos la comprobacion
    	//if ((fileVerify==null || (fileVerify.equals(""))))
    	//		return true;
        File verifyDirectory = new File(directoryPathString, fileVerify);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }

    public boolean verifyAdmCarInstall(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString, VERIFY_ADM_CAR_FILE);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public boolean verifyGeopistaWmsManager(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString,
                VERIFY_GEOPISTA_WMS_MANAGER_FILE);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public boolean verifyGeopistaWmsManagerIntranet(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString,
                VERIFY_GEOPISTA_WMS_MANAGER_FILE);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public boolean verifyGuiaurbana(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString, VERIFY_GUIAURBANA);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public boolean verifyVisualizador(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString, VERIFY_VISUALIZADOR);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public boolean verifySoftware(String directoryPathString)
    {
        File verifyDirectory = new File(directoryPathString, VERIFY_SOFTWARE);
        if (!verifyDirectory.exists())
        {
            return false;
        }
        return true;
    }
    
    public void copyRemoteFile(String sourceJar, String targetJar)
    {
        File targetRemoteFile = new File(targetJar);
        copyRemoteFile(sourceJar, targetRemoteFile);
    }
    
    public void copyRemoteFile(String sourceFile, File targetFile)
    {
        FileOutputStream targetOutput = null;
        InputStream inputStream = null;
        try
        {
        	System.out.println("Copiando archivo remoto:"+sourceFile);
            URL sourceJarURL = new URL(sourceFile);
            URLConnection remoteFileConnection = sourceJarURL.openConnection();
            inputStream = remoteFileConnection.getInputStream();
            
            if (!targetFile.getParentFile().exists())
            {
                targetFile.getParentFile().mkdirs();
            }
            targetOutput = new FileOutputStream(targetFile);
            
            int len;
            byte[] buf = new byte[2048];
            
            while ((len = inputStream.read(buf)) != -1)
            {
                
                targetOutput.write(buf, 0, len);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            Object[] possibleValues = { aplicacion.getI18nString("ErrorNewAttemptDialog.newAttempt"),
                    aplicacion.getI18nString("ErrorNewAttemptDialog.Cancel"), aplicacion.getI18nString("ErrorNewAttemptDialog.omit") };
            int reinstalationSystem = JOptionPane
                    .showOptionDialog(
                            aplicacion.getMainFrame(),
                            aplicacion
                            .getI18nString("UpdateSystemPanel.ErrorComunicacionServidor"),
                            aplicacion
                                    .getI18nString("UpdateSystemPanel.ComunicacionServidor"),
                            0, JOptionPane.QUESTION_MESSAGE, null, possibleValues,
                            possibleValues[0]);
            
            if (reinstalationSystem==0){
            	copyRemoteFile(sourceFile, targetFile);
            	return;
            }else if (reinstalationSystem==1){
                System.exit(1);
            }else if (reinstalationSystem==2){
            	return;
            }
/*            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorComunicacionServidor"),
                    aplicacion.getI18nString("UpdateSystemPanel.ComunicacionServidor"),
                    JOptionPane.ERROR_MESSAGE);*/
            
            try
            {
                targetOutput.close();
                boolean deleteFile = targetFile.delete();
                if (!deleteFile)
                    throw new IOException();
            } catch (Exception e1)
            {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("UpdateSystemPanel.ErrorBorrandoTemporal")
                        + " " + targetFile.getAbsolutePath(), aplicacion
                        .getI18nString("UpdateSystemPanel.BorrandoTemporal"),
                        JOptionPane.ERROR_MESSAGE);
            }
            
        } finally
        {
            try
            {
                targetOutput.close();
            } catch (Exception e)
            {
                
            }
            try
            {
                inputStream.close();
            } catch (Exception e)
            {
                
            }
        }
    }
    
    public String updateFileVersion(String nombreModulo, Element webApps,String fileVerify, 
    		List versions, String geopistaDirectory,
    		String version, String httpBase, JProgressBar progressBar, int percentage,
    		String basePath, Connection connection) throws IOException, JDOMException
    		{
    	return updateFileVersion(nombreModulo, webApps,fileVerify, versions, geopistaDirectory, version, 
    			httpBase, progressBar, percentage, basePath, connection, null);
    		}
    
    
    public String updateFileVersion(String nombreModulo, Element webApps,String fileVerify, 
    		List versions, String geopistaDirectory,
            String version, String httpBase, JProgressBar progressBar, int percentage,
            String basePath, Connection connection, String tomcatDirectory) throws IOException, JDOMException
            {
        
    	if (tomcatDirectory == null){
    		tomcatDirectory = geopistaDirectory + "/apache-tomcat-6.0.14";
    	}
    	
        if (versions == null)
            return "";
        Iterator versionsIterator = versions.iterator();
        
        int filesNumber = versions.size();
        int percentageStep = 0;
        if (filesNumber > 0)
        {
            percentageStep = percentage / filesNumber;
        }
        int parcialPercertage = 0;
        
        while (versionsIterator.hasNext())
        {
            Element currentVersionElement = (Element) versionsIterator.next();
            Attribute idVersionAttribute = currentVersionElement.getAttribute("id");
            Attribute idInstallAttribute = currentVersionElement.getAttribute("install");
            Attribute idForceAttribute = currentVersionElement.getAttribute("force");
            String versionId = idVersionAttribute.getValue();
            int versionIdInt = Integer.parseInt(versionId);
            int currentLocalVersion = Integer.parseInt(UpdateSystemPanel
                    .getActualVersion());
            
            if (currentLocalVersion < versionIdInt)
            {
                parcialPercertage += percentageStep;   
                
                if (idForceAttribute != null && idForceAttribute.getValue().equals("yes")) {
                    if (!nombreModulo.equals("/apache-tomcat-6.0.14") && !nombreModulo.equals("/mapserver"))
//                        addWebAplication(nombreModulo, geopistaDirectory + TOMCAT_DIR, webApps);
                    	 addWebAplication(nombreModulo, tomcatDirectory, webApps);
                    // Podría ocurrir que deseásemos en una nueva versión
                    // añadir el context al tomcat
                    // porque se tratase de una aplicación antigua asociada
                    // al tomcat 5.0
                    Attribute setContext = currentVersionElement.getAttribute("setContext");
                    if (setContext != null && (setContext.getValue()).equals("yes"))
//                        addWebAplication(nombreModulo, geopistaDirectory + TOMCAT_DIR, webApps);
                    	addWebAplication(nombreModulo, tomcatDirectory, webApps);
                } else {
                    // Primero miro si tenemos el atributo install="yes". En
                    // este caso se debe comprobar
                    // que el módulo no está instalado
                    if (idInstallAttribute != null && (idInstallAttribute.getValue()).equals("yes")) {
                        if (!verifyModuloInstall(geopistaDirectory, fileVerify)) {
                            if (!nombreModulo.equals("/apache-tomcat-6.0.14") && !nombreModulo.equals("/mapserver"))
//                                addWebAplication(nombreModulo, geopistaDirectory + TOMCAT_DIR, webApps);
                            	addWebAplication(nombreModulo, tomcatDirectory, webApps);
                        } else {
                            // informamos de que ya está instalado el módulo
                            updateProgressBar(progressBar, 8);
                            return ("UpdateSystemPanel." + nombreModulo.substring(1) + "Instalado");
                        }

                    } else { // Se trata de una actualización. Comprobamos
                                // que el módulo está instalado
                        if (!verifyModuloInstall(geopistaDirectory, fileVerify)) {
                            // informamos de que no está instalado el módulo,
                            // por tanto
                            // no se puede actualizar
                            updateProgressBar(progressBar, 8);
                            return ("UpdateSystemPanel." + nombreModulo.substring(1) + "NoInstalado");
                        }
                        // Podría ocurrir que deseásemos en una nueva versión
                        // añadir el context al tomcat
                        // porque se tratase de una aplicación antigua asociada
                        // al tomcat 5.0
                        Attribute setContext = currentVersionElement.getAttribute("setContext");
                        if (setContext != null && (setContext.getValue()).equals("yes"))
//                            addWebAplication(nombreModulo, geopistaDirectory + TOMCAT_DIR, webApps);
                        	addWebAplication(nombreModulo, tomcatDirectory, webApps);
                    }
                }
                //Este debe ir el primero para que se hagan los borrados antes de copiar los nuevos ficheros
		        Element deleteFilesNode = currentVersionElement.getChild("deleteFiles");
		        if (deleteFilesNode != null)
		        {
		        	List deleteFiles = deleteFilesNode.getChildren("file");
		            deleteFiles(deleteFiles, geopistaDirectory, basePath);
		        }
		        //Borra, por si es necesario las entradas de context del fichero server.xml que hagan falta
		        Element deleteElementContext = currentVersionElement.getChild("deleteContext");
		        if (deleteElementContext != null)
		        {
		        	List listaContext = deleteElementContext.getChildren("path");
//		        	deleteContext(listaContext, geopistaDirectory+TOMCAT_DIR);
		        	deleteContext(listaContext, tomcatDirectory);
		        }
		        List fileList = currentVersionElement.getChildren("file");
		        updateFiles(fileList, geopistaDirectory, version, httpBase, progressBar,
		                        percentageStep, basePath);
		                
		        // para el administrador de cartografia existiran nodos jnlp
		        Element jnlpNode = currentVersionElement.getChild("jnlps");
		        updateJnlps(jnlpNode, geopistaDirectory, version, httpBase, basePath);
		                
		        //Ejecuta un comando.
		        Element execNodePrevious = currentVersionElement.getChild("executePrevious");
		        execute(execNodePrevious,geopistaDirectory, version, basePath, connection);
		        
		        //Descomprime los ficheros ZIP en la ruta indicada
		        List zipFileElements = currentVersionElement.getChildren("zipfile");
		        extractZipFiles (zipFileElements, geopistaDirectory, version, httpBase, basePath);
		
		        //Descomprime los ficheros TAR en la ruta indicada
		        List tarFileElements = currentVersionElement.getChildren("tarfile");
		        extractTarFiles (tarFileElements, geopistaDirectory, version, httpBase, basePath);

		        //Ejecuta un comando.
		        Element execNode = currentVersionElement.getChild("execute");
		        execute(execNode,geopistaDirectory, version, basePath, connection);
		
		        //Extrae los ficheros de un jar a una ruta indicada
		        List jarFileElements = currentVersionElement.getChildren("jarfiles");
		        extractJarFiles (jarFileElements, geopistaDirectory, version, httpBase, basePath);
		                
		        // Reemplaza dentro de un fichero todas las coincidencias con un texto por otro dado 
		        Element modifyTextFilesNode = currentVersionElement.getChild("modifytextfiles");
		        modifyTextFiles(modifyTextFilesNode, geopistaDirectory, version, httpBase, basePath);
		                
		        //Añade texto a un fichero de texto
		        Element appendTextFilesNode = currentVersionElement.getChild("appendlinefiles");
		        appendTextFiles(appendTextFilesNode, geopistaDirectory, version, httpBase, basePath);
		
		//      reemplaza archivos xml conservando nodos de los reemplazados             
		        List replaceXmlFileNode = currentVersionElement.getChildren("replaceXmlFile");
		        replaceXML(replaceXmlFileNode, geopistaDirectory, version, httpBase, basePath);
		                
		        List xmlFilesNode = currentVersionElement.getChildren("xmlfile");
		        xmlFile(xmlFilesNode, geopistaDirectory, version, basePath);
		                
		        Element installServiceNode = currentVersionElement.getChild("installservice");
		        installService(installServiceNode, geopistaDirectory, version, basePath);

		        //Ejecuta un comando.
		        Element execNodeLast = currentVersionElement.getChild("executeLast");
		        execute(execNodeLast,geopistaDirectory, version, basePath, connection);
            }
            
        }
        int finalPercentage = percentage - parcialPercertage;
        if (finalPercentage > 0)
            updateProgressBar(progressBar, finalPercentage);
        return "";
        }
    
    public void updateFiles(List remoteFiles, String geopistaDirectory, String version,
            String sourceJarsUrl, JProgressBar progressBar, int percentage,
            String basePath) throws IOException
            {
        
        Iterator jarFilesIterator = remoteFiles.iterator();
        int filesNumber = remoteFiles.size();
        int percentageStep = 0;
        if (filesNumber > 0)
        {
            percentageStep = percentage / filesNumber;
        }
        int parcialPercertage = 0;
        while (jarFilesIterator.hasNext())
        {
            parcialPercertage += percentageStep;
            Element currentJarElement = (Element) jarFilesIterator.next();
            String os = currentJarElement.getAttributeValue("os");
            
            String sys = System.getProperty("os.name");

            
            if (os!=null)
            {
            	if(sys.toUpperCase().indexOf("WINDOWS")==-1){
            		//Linux
            		if(os.equalsIgnoreCase("windows"))
            			continue;
                }
            	else{
            		//Windows
            		if(os.equalsIgnoreCase("linux"))
            			continue;
            	}
            }
            Element sourceElement = currentJarElement.getChild("sourceFile");
            String sourceJar = sourceElement.getText();
            sourceJar = replaceStrings(sourceJar);
            Element targetElement = currentJarElement.getChild("targetFile");
            String targetJar = targetElement.getText();
            targetJar = replaceStrings(targetJar);
            
            File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
            File finalTempDirectory = new File(baseTempDirectory, version);
            
            if (!finalTempDirectory.exists())
            {
                finalTempDirectory.mkdirs();
            }
            
            File sourceFile = new File(finalTempDirectory, sourceJar);
            
            if (!sourceFile.exists())
            {
                copyRemoteFile(sourceJarsUrl + "/" + sourceJar, sourceFile);
            }
            
            File targetPath = new File(geopistaDirectory, basePath);
            File targetFile = null;
            if (targetElement.getAttribute("base")!=null
            		&& (targetElement.getAttributeValue("base").equals("true")))
            
            {            	
            	targetFile = new File (targetJar);
            }
            else
            {
            	targetFile = new File(targetPath, targetJar);
            }
            
            copyFile(targetFile, sourceFile);
            updateProgressBar(progressBar, percentageStep);
        }
        
        int finalPercentage = percentage - parcialPercertage;
        if (finalPercentage > 0)
            updateProgressBar(progressBar, finalPercentage);
            }
    
    public void modifyTextFiles(Element textFilesElementsNode, String geopistaDirectory, String version, String httpBase, String basePath) throws IOException
    {
        if (textFilesElementsNode == null)
            return;

        /*
         * Reemplazo de variables en ficheros de texto
         */
        List replaceStringFiles = textFilesElementsNode.getChildren("replaceStringFile");
        Iterator replaceStringFilesIterator = replaceStringFiles.iterator();
        
        while(replaceStringFilesIterator.hasNext()) {
            Element currentReplaceStringElement = (Element) replaceStringFilesIterator.next();

            /*
             * Comprobacion del sistema operativo
             */
            String os = currentReplaceStringElement.getAttributeValue("os");
            String sys = System.getProperty("os.name");

            if (os!=null)
            {
                if(sys.toUpperCase().indexOf("WINDOWS")==-1){
                    //Linux
                    if(os.equalsIgnoreCase("windows"))
                        continue;
                }
                else{
                    //Windows
                    if(os.equalsIgnoreCase("linux"))
                        continue;
                }
            }
            String currentReplaceString = currentReplaceStringElement.getText();
            
            File currentReplaceStringDirectory = new File(geopistaDirectory,basePath);
            File currentReplaceStringFile = new File(currentReplaceStringDirectory,currentReplaceString);
            
            replaceStringFiles(currentReplaceStringFile);
        }
        /*
         * Fin de reemplazo de variables de ficheros de texto
         */
        
        List textFiles = textFilesElementsNode.getChildren("textfile");
        Iterator textFilesIterator = textFiles.iterator();
        
        while (textFilesIterator.hasNext())
        {
            File targetFile = null;
            
            Element currentTextFileElement = (Element) textFilesIterator.next();
            String os = currentTextFileElement.getAttributeValue("os");
            
            String sys = System.getProperty("os.name");

            
            if (os!=null)
            {
            	if(sys.toUpperCase().indexOf("WINDOWS")==-1){
            		//Linux
            		if(os.equalsIgnoreCase("windows"))
            			continue;
                }
            	else{
            		//Windows
            		if(os.equalsIgnoreCase("linux"))
            			continue;
            	}
            }
            Element textFileElement = currentTextFileElement.getChild("file");
            String sourceTextFileName = textFileElement.getText();
            
            
            
            //Buscamos o creamos directorio temporal
            File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
            
            
            File targetPath = new File(geopistaDirectory, basePath);
            targetFile = new File(targetPath, sourceTextFileName);
            
            //Comprobamos si la existencia del fichero es obligatoria
            
            Element obligatoryElement = currentTextFileElement.getChild("obligatory");
            if(obligatoryElement != null)
            {
                String obligatory = obligatoryElement.getText();
                if(obligatory != null && obligatory.equals("false"))
                {
                    if(!targetFile.exists()) continue;
                }
            }
            
            
            // copiamos el jnlp destino en una carpeta de seguridad
            File backupPath = new File(targetPath, BACKUP_DIRECTORY);
            if (!backupPath.exists())
            {
                backupPath.mkdirs();
            }
            File backupFile = new File(backupPath, sourceTextFileName
                    + System.currentTimeMillis());
            copyFile(backupFile, targetFile);
            
            List modificationsElement = currentTextFileElement.getChildren("modification");
            Iterator modificationsElementIterator = modificationsElement.iterator();
            
            HashMap changes = new HashMap();
            
            while(modificationsElementIterator.hasNext())
            {
                Element modificationNode = (Element) modificationsElementIterator.next();
                
                Element oldStringElement = (Element) modificationNode.getChild("oldstring");
                Element newStringElement = (Element) modificationNode.getChild("newstring");
                String oldString = oldStringElement.getText();
                String newString = newStringElement.getText();
                
                changes.put(oldString,replaceStrings(newString));
            }
            
            
            
            readFile(backupFile.getAbsolutePath(), targetFile.getAbsolutePath(), changes);
        }
        
    }
    
    public void appendTextFiles(Element appendTextFilesElementsNode, String geopistaDirectory, String version, String httpBase, String basePath) throws IOException
    {
        if (appendTextFilesElementsNode == null)
            return;
        
        List textFiles = appendTextFilesElementsNode.getChildren("textfile");
        Iterator textFilesIterator = textFiles.iterator();
        
        while (textFilesIterator.hasNext())
        {
            File targetFile = null;
            
            Element currentTextFileElement = (Element) textFilesIterator.next();
            Element textFileElement = currentTextFileElement.getChild("file");
            String sourceTextFileName = textFileElement.getText();
            
                      
            File targetPath = new File(geopistaDirectory, basePath);
            targetFile = new File(targetPath, sourceTextFileName);
           
            
            // copiamos el fichero destino en una carpeta de seguridad
            File backupPath = new File(targetPath, BACKUP_DIRECTORY);
            if (!backupPath.exists())
            {
                backupPath.mkdirs();
            }
            File backupFile = new File(backupPath, sourceTextFileName
                    + System.currentTimeMillis());
            copyFile(backupFile, targetFile);
            
            List appendLineElement = currentTextFileElement.getChildren("appendline");
            Iterator appendLineElementIterator = appendLineElement.iterator();
           
            StringBuffer newLine = new StringBuffer();
            int i = 0;
            while(appendLineElementIterator.hasNext())
            {
                Element appendLineNode = (Element) appendLineElementIterator.next();
                
                if (i!=0)
                    newLine.append("\n");
                newLine.append(appendLineNode.getText());
                
                i++;
            }
                       
            addLinesToFile(backupFile.getAbsolutePath(), targetFile.getAbsolutePath(), newLine.toString());
        }
        
    }
    
    public void replaceStringFiles (File replaceFile) throws IOException
    {
        
        String newLine=null;
        StringBuffer fileContent = new StringBuffer();
        
        FileReader readFile = null;
        FileWriter writeFile = null;
        BufferedReader bufferedreadBackupFile = null;
        BufferedWriter bufferedwriterFile = null;
        try
        {
            readFile = new FileReader(replaceFile);
            bufferedreadBackupFile = new BufferedReader(readFile);
            String nextLine = null;
            
            while ((nextLine = bufferedreadBackupFile.readLine()) != null)
            {
                fileContent.append(nextLine+"\n");  
            }
        }finally{
            bufferedreadBackupFile.close();
        }
        
        try
        {
            writeFile =new FileWriter(replaceFile);
            bufferedwriterFile = new BufferedWriter(writeFile);
            bufferedwriterFile.write(replaceStrings(fileContent.toString()));
            
            
        }finally{
            bufferedwriterFile.close();
        }
    }
    
    public String replaceStrings(String sourceString)
    {
        Hashtable substitutionsStrings = (Hashtable) aplicacion.getBlackboard().get(SUBSTITUTIONS_STRINGS);
        Set substitutionsStringsSet = substitutionsStrings.keySet();
        Iterator substitutionsStringsIterator  = substitutionsStringsSet.iterator();
        
        while (substitutionsStringsIterator.hasNext())
        {
            String currentKey = (String) substitutionsStringsIterator.next();
            if(sourceString.indexOf(currentKey)!=-1)
            {
                String replaceString = (String)substitutionsStrings.get(currentKey);
                replaceString = replaceString.replaceAll("\\\\", "\\\\\\\\");
                sourceString = sourceString.replaceAll(currentKey, replaceString);
            }
            
        }
        
        return sourceString;
    }
    
    public void extractZipFiles(List zipFiles, String geopistaDirectory, 
            String version, String httpBase, String basePath) throws IOException
            {
        if (zipFiles == null)
            return;
        
        Iterator zipFilesIterator = zipFiles.iterator();
     
        while (zipFilesIterator.hasNext())
        {
            File targetFile = null;
            
            Element currentZipElement = (Element) zipFilesIterator.next();
            Element sourceElement = currentZipElement.getChild("sourceZipFile");
            String sourceZipFile = sourceElement.getText();
            sourceZipFile = replaceStrings(sourceZipFile);
            
            Element targetElement = currentZipElement.getChild("targetDirectory");
            String targetDirectory = targetElement.getText();
            targetDirectory = replaceStrings(targetDirectory);
            
            String os = currentZipElement.getAttributeValue("os");
            
            String sys = System.getProperty("os.name");

            
            if (os!=null)
            {
            	if(sys.toUpperCase().indexOf("WINDOWS")==-1){
            		//Linux
            		if(os.equalsIgnoreCase("windows"))
            			continue;
                }
            	else{
            		//Windows
            		if(os.equalsIgnoreCase("linux"))
            			continue;
            	}
            }
                        
            File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
            File finalTempDirectory = new File(baseTempDirectory, version);
            
            if (!finalTempDirectory.exists())
            {
                finalTempDirectory.mkdirs();
            }
            
            File sourceFile = new File(finalTempDirectory, sourceZipFile);
            
            if (!sourceFile.exists())
            {
            	
                copyRemoteFile(httpBase + "/" + sourceZipFile, sourceFile);
            }
            
            File targetPath = new File(geopistaDirectory, basePath);
            
            
            
            
            if (targetElement.getAttribute("base")!=null
            		&& (targetElement.getAttributeValue("base").equals("true")))
            
            {            	
            	targetFile = new File (targetDirectory);
            }
            else
            {
            	targetFile = new File(targetPath, targetDirectory);
            }
            
                                                        
            
            //Descomprimir el zip en la ruta indicada
            try 
            {                    
            	ZipFile zf = new ZipFile(sourceFile);    
            	Enumeration entries = zf.entries();

            	while(entries.hasMoreElements()) 
            	{
            		ZipEntry entry = (ZipEntry)entries.nextElement();

            		String nomAbsoluto = targetFile.getAbsolutePath()+"/"+entry.getName();

            		if(entry.isDirectory())
            		{
            			(new File(nomAbsoluto)).mkdirs();
            			continue;
            		}
            		else
            		{
            			new File (nomAbsoluto.substring(0, nomAbsoluto.lastIndexOf("/"))).mkdirs();
            		}


            		copyInputStream(zf.getInputStream(entry),
            				new BufferedOutputStream(new FileOutputStream(nomAbsoluto)));

            	}

            	zf.close();   


            } catch (Exception e) 
            {
            	e.printStackTrace();
            	return;                    
            }

            List replaceStringFiles = currentZipElement.getChildren("replaceStringFile");
            Iterator replaceStringFilesIterator = replaceStringFiles.iterator();
            
            while(replaceStringFilesIterator.hasNext())
            {
                Element currentReplaceStringElement = (Element) replaceStringFilesIterator.next();
                String currentReplaceString = currentReplaceStringElement.getText();
                
                File currentReplaceStringDirectory = new File(geopistaDirectory,basePath);
                File currentReplaceStringFile = new File(currentReplaceStringDirectory,currentReplaceString);
                                
                replaceStringFiles(currentReplaceStringFile);
                
                
            }
        }        
        
            }

    public void extractTarFiles(List tarFiles, String geopistaDirectory,
            String version, String httpBase, String basePath) throws IOException
    {
        if (tarFiles == null)
            return;

        Iterator tarFilesIterator = tarFiles.iterator();

        while (tarFilesIterator.hasNext())
        {
            Element currentTarElement = (Element) tarFilesIterator.next();
            Element sourceElement = currentTarElement.getChild("sourceTarFile");
            String sourceTarFile = sourceElement.getText();

            Element targetElement = currentTarElement.getChild("targetDirectory");
            String targetDirectory = targetElement.getText();

            String os = currentTarElement.getAttributeValue("os");
            String sys = System.getProperty("os.name");

            if (os!=null)
            {
            	if(sys.toUpperCase().indexOf("WINDOWS")==-1)
                {
            		//Linux
            		if(os.equalsIgnoreCase("windows"))
            			continue;
                }
            	else
                {
            		//Windows
            		if(os.equalsIgnoreCase("linux"))
            			continue;
            	}
            }

            readTar(sourceTarFile,targetDirectory, geopistaDirectory,
                    version, httpBase,basePath,targetElement);

            List replaceStringFiles = currentTarElement.getChildren("replaceStringFile");
            Iterator replaceStringFilesIterator = replaceStringFiles.iterator();
            while(replaceStringFilesIterator.hasNext())
            {
                Element currentReplaceStringElement = (Element) replaceStringFilesIterator.next();
                String currentReplaceString = currentReplaceStringElement.getText();

                File currentReplaceStringDirectory = new File(geopistaDirectory,basePath);
                File currentReplaceStringFile = new File(currentReplaceStringDirectory,currentReplaceString);

                replaceStringFiles(currentReplaceStringFile);
            }
        }
    }

   //encontre en internet   ===========PROBAR
  public static InputStream getInputStream(String tarFileName) throws Exception
   {
          if(tarFileName.substring(tarFileName.lastIndexOf(".") + 1, tarFileName.lastIndexOf(".") + 3).equalsIgnoreCase("gz")){
             System.out.println("Creating an GZIPInputStream for the file");
             return new GZIPInputStream(new FileInputStream(new File(tarFileName)));
          }else{
             System.out.println("Creating an InputStream for the file");
             return new FileInputStream(new File(tarFileName));
          }
       }

       public void readTar(String tarFileName,String untarDir, String geopistaDirectory,
                                  String version, String httpBase, String basePath, Element targetElement) throws IOException
       {
          //System.out.println("Reading TarInputStream... (using classes from http://www.trustice.com/java/tar/)");
            File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
            File finalTempDirectory = new File(baseTempDirectory, version);

            if (!finalTempDirectory.exists())
            {
                finalTempDirectory.mkdirs();
            }

            File sourceFile = new File(finalTempDirectory, tarFileName);

            if (!sourceFile.exists())
            {
                copyRemoteFile(httpBase + "/" + tarFileName, sourceFile);
            }

            File targetPath = new File(geopistaDirectory, basePath);
            File targetFile = null;
            if (targetElement.getAttribute("base")!=null
            		&& (targetElement.getAttributeValue("base").equals("true")))

            {
            	targetFile = new File (untarDir);
            }
            else
            {
            	targetFile = new File(targetPath, untarDir);
            }
           InputStream in = null;
           try
           {
                in =getInputStream(sourceFile.getAbsolutePath());
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }

          TarInputStream tin = new TarInputStream(in);
          TarEntry tarEntry = tin.getNextEntry();
          if(new File(targetFile.getAbsolutePath()).exists())
          {
              while (tarEntry != null){
                 File destPath = new File(targetFile.getAbsolutePath() + File.separatorChar + tarEntry.getName());
                 System.out.println("Processing " + destPath.getAbsoluteFile());
                 if(!tarEntry.isDirectory()){
                    FileOutputStream fout = new FileOutputStream(destPath);
                    tin.copyEntryContents(fout);
                    fout.close();
                 }else{
                    destPath.mkdir();
                 }
                 tarEntry = tin.getNextEntry();
              }
              tin.close();
          }else{
             System.out.println("That destination directory doesn't exist! " + untarDir);
          }
       }

    public void extractJarFiles(List jarFiles, String geopistaDirectory,
            String version, String httpBase, String basePath) throws IOException
            {
        if (jarFiles == null)
            return;
        
        Iterator jarFilesIterator = jarFiles.iterator();
        
        while (jarFilesIterator.hasNext())
        {
            try
            {
                Element currentJarElement = (Element) jarFilesIterator.next();
                
                //extraemos de cada jar los archivos que desemos
                List jarfileElement =currentJarElement.getChildren("jarfile");
                Iterator jarfileElementIterator = jarfileElement.iterator();
                while (jarfileElementIterator.hasNext())
                {
                    Element currentJarfile = (Element) jarfileElementIterator.next();
                
                    //nombre del jar
                    Element sourceJarElement = currentJarfile.getChild("sourceJarFile");
                    String sourceJarFileName = sourceJarElement.getText();
                    
                    //extraemos todos los archivos que queremos sacar del jar y las rutas donde los queremos colocar
                    List fileElement =currentJarfile.getChildren("file");
                    extractFiles (fileElement, geopistaDirectory, version, httpBase, basePath,sourceJarFileName);
                    
                }   
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }        
        
            }
    
    public void extractFiles(List listFiles, String geopistaDirectory, 
            String version, String httpBase, String basePath,String sourceJarFileName) throws IOException
            {
                if (listFiles == null)
                    return;
        
                Iterator filesIterator = listFiles.iterator();
                while(filesIterator.hasNext()){
                    Element fileElement =(Element)filesIterator.next();
                    
                  //nombre del fichero dentro del jar
                  Element sourceFileElement = fileElement.getChild("sourceFile");
                  String sourceFileName = sourceFileElement.getText();
                  
                  //destino donde se copiara el fichero extraido
                  Element targetElement = fileElement.getChild("targetDirectory");
                  String targetDirName = targetElement.getText();
                  
                  
                  File targetPath = new File(geopistaDirectory, basePath);
                  File targetDirFile = new File(targetPath, targetDirName);
                  
                  URL urljar = new URL(httpBase + "/" + sourceJarFileName);
                  GeopistaUtil
                  .extractResources(
                          "jar:" + urljar.toExternalForm() + "!/" + sourceFileName, //$NON-NLS-1$
                          targetDirFile, urljar, //$NON-NLS-1$
                          null);
                }

            }
    
    
    
    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException
    {
        if (in ==null)
            return;
        
        byte[] buffer = new byte[1024];
        int len;
        
        while((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        
        in.close();
        out.close();
    }
    
    
    
    public static void readFile(String originalPath, String finalPath,HashMap modificaciones) throws IOException{
        
        String newLine=null;
        
        FileReader readFile = null;
        FileWriter writeFile = null;
        BufferedReader bufferedreadBackupFile = null;
        BufferedWriter bufferedwriterFile = null;
        try
        {
            readFile = new FileReader(originalPath);
            writeFile =new FileWriter(finalPath);
            bufferedreadBackupFile = new BufferedReader(readFile);
            bufferedwriterFile = new BufferedWriter(writeFile);
            
            String nextLine = null;
            
            Set keymodify=modificaciones.keySet();
            
            while ((nextLine = bufferedreadBackupFile.readLine()) != null)
            {
                Iterator keymodifyIter= keymodify.iterator();
                while(keymodifyIter.hasNext()){
                    String mofifytext=(String)keymodifyIter.next();
                    if(nextLine.indexOf(mofifytext)!=-1){
                        String newtext=(String)modificaciones.get(mofifytext);
                        if (nextLine.indexOf(newtext)==-1){
                            int beginIndex = nextLine.indexOf(mofifytext);
                            String initialString = nextLine.substring(0,beginIndex);
                            String finalString = nextLine.substring(beginIndex+mofifytext.length());
                            newLine=initialString + newtext + finalString;
                        }
                        
                    }
                    
                }
                if(newLine!=null){
                    bufferedwriterFile.write(newLine);
                    newLine=null;
                }else{
                    bufferedwriterFile.write(nextLine);
                }
                bufferedwriterFile.newLine();
            }
        }finally{
            bufferedwriterFile.close();
            bufferedreadBackupFile.close();
        }
    }
    
    
    public void addLinesToFile(String originalPath, String finalPath,String appendedLines) throws IOException
    {        
        FileReader readFile = null;
        FileWriter writeFile = null;
        BufferedReader bufferedreadBackupFile = null;
        BufferedWriter bufferedwriterFile = null;
        try
        {
            readFile = new FileReader(originalPath);
            writeFile =new FileWriter(finalPath);
            bufferedreadBackupFile = new BufferedReader(readFile);
            bufferedwriterFile = new BufferedWriter(writeFile);
            
            String nextLine = null;
            while ((nextLine = bufferedreadBackupFile.readLine()) != null)
            {
                bufferedwriterFile.write(nextLine);            
                bufferedwriterFile.newLine();
            }
            bufferedwriterFile.write(replaceStrings(appendedLines));
            
        }finally{
            bufferedwriterFile.close();
            bufferedreadBackupFile.close();
        }
    }    
    

    public void updateJnlps(Element jnlpFilesElement, String geopistaDirectory,
    		String version, String sourceJarsUrl, String basePath) throws IOException,
    		JDOMException
    		{
    	if (jnlpFilesElement == null)
    		return;
    	List jnlpFiles = jnlpFilesElement.getChildren("jnlp");

    	Iterator jarFilesIterator = jnlpFiles.iterator(); 

    	while (jarFilesIterator.hasNext())
    	{
    		File targetFile = null;
    		try
    		{
    			Element currentJnlpElement = (Element) jarFilesIterator.next();
    			Element sourceElement = currentJnlpElement.getChild("sourceFile");
    			String sourceJnlp = sourceElement.getText();
    			Element targetElement = currentJnlpElement.getChild("targetFile");
    			String targetJnlp = targetElement.getText();

    			boolean isNewjnlp = false;
    			Attribute newJnlp = currentJnlpElement.getAttribute("new");
    			if(newJnlp!=null)
    			{
    				isNewjnlp = newJnlp.getBooleanValue();
    			}

    			File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
    			File finalTempDirectory = new File(baseTempDirectory, version);

    			if (!finalTempDirectory.exists())
    			{
    				finalTempDirectory.mkdirs();
    			}

    			File sourceFile = new File(finalTempDirectory, sourceJnlp);


    			copyRemoteFile(sourceJarsUrl + "/" + targetJnlp, sourceFile);


    			File targetPath = new File(geopistaDirectory, basePath);
    			targetFile = new File(targetPath, targetJnlp);

    			// copiamos el jnlp destino en una carpeta de seguridad
    			File backupPath = new File(targetPath, BACKUP_DIRECTORY);
    			if (!backupPath.exists())
    			{
    				backupPath.mkdirs();
    			}
    			File backupFile = new File(backupPath, targetJnlp
    					+ System.currentTimeMillis());

    			if(isNewjnlp)
    			{
    				copyFile(backupFile, new File(targetPath, sourceJnlp));
    			}
    			else
    			{
    				copyFile(backupFile, targetFile);
    			}

    			// abrimos el archivo origen xml para leer los dos nodos que
    			// necesitamos conservar
    			SAXBuilder builder = new SAXBuilder();

    			// tenemos que sacar los valores de los xml destino por fuerza
    			// bruta porque los xml no son correctos
    			FileReader readBackupFile = new FileReader(backupFile);
    			BufferedReader bufferedreadBackupFile = new BufferedReader(readBackupFile);
    			String nextLine = null;
    			byte[] buf = new byte[2048];

    			StringBuffer xmlStringBuffer = new StringBuffer();

    			while ((nextLine = bufferedreadBackupFile.readLine()) != null)
    			{
    				xmlStringBuffer.append(nextLine);
    			}
    			String xmlText = xmlStringBuffer.toString();

    			String codebaseTemp = xmlText
    			.substring(xmlText.indexOf("codebase=\"") + 10);
    			String codeBase = codebaseTemp.substring(0, codebaseTemp.indexOf("\""));

    			String description = xmlText.substring(
    					xmlText.indexOf("<description>") + 13, xmlText
    					.indexOf("</description>"));

    			// abrimos el nuevo xml

    			Document docNew = builder.build(sourceFile);
    			Element raizNew = docNew.getRootElement();
    			Attribute codeBaseAttributeNew = raizNew.getAttribute("codebase");
    			Element informationElementNew = raizNew.getChild("information");


    			Element descriptionElementNew =
    				informationElementNew.getChild("description"); 
    			String descriptionNew = descriptionElementNew.getText();
    			String initDescription = descriptionNew.substring(0,descriptionNew.indexOf("(")+1);
    			String finalDescription = descriptionNew.substring(descriptionNew.indexOf(")"));
    			codeBase = modifyPortCodebase(codeBase);
    			String newDescription = initDescription+codeBase+finalDescription;
    			descriptionElementNew.setText(newDescription);
    			codeBaseAttributeNew.setValue(codeBase);


    			FileOutputStream outputStrean = null;
    			try
    			{
    				outputStrean = new FileOutputStream(targetFile);
    				XMLOutputter outp = new XMLOutputter();
    				outp.output(docNew, outputStrean);
    			} finally
    			{
    				try
    				{
    					outputStrean.close();
    				} catch (Exception e)
    				{

    				}
    			}
    		} catch (Exception e)
    		{
    			JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
    					.getI18nString("UpdateSystemPanel.ErrorJnlp")
    					+ " " + targetFile.getAbsolutePath());
    		}

    	}

    		}

    
    public void deleteFiles(List deleteFiles, String geopistaDirectory, String module)
    {
        Iterator deleteFilesIterator = deleteFiles.iterator();
        while (deleteFilesIterator.hasNext())
        {
            Element currentDeleteElement = (Element) deleteFilesIterator.next();
            
            String currentDeleteFileName = currentDeleteElement.getText().trim();
            
            List excludedList = currentDeleteElement.getChildren("exclude");
            
            File modulePath = new File(geopistaDirectory, module);
            File deleteFile = new File(modulePath, currentDeleteFileName);
            
            if (deleteFile.exists())
            {
                try
                {
                    if (deleteFile.isDirectory())
                    {                         
                        boolean deleteResult = deleteDirectory(deleteFile, excludedList);
                        if (!deleteResult)
                        {
                            throw new IOException();
                        }
                        
                    }
                    else if (excludedList == null || (excludedList!=null && !excludedList.contains(deleteFile.getName())))                        
                    {
                        boolean deleteResult = deleteFile.delete();
                        
                        if (!deleteResult)
                        {
                            throw new IOException();
                        }
                    }
                } catch (Exception e)
                {
                    JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                            .getI18nString("UpdateSystemPanel.ErrorBorrarFichero")
                            + " " + deleteFile.getAbsolutePath(), aplicacion
                            .getI18nString("UpdateSystemPanel.Borrando"),
                            JOptionPane.ERROR_MESSAGE);
                }
                
            }
        }
    }
    
    private Set getExcludedFiles(File directory, List excludedList)
    {
        Iterator excludedListIterator = excludedList.iterator();
        Set excludedFiles = new HashSet();
        
        while (excludedListIterator.hasNext())
        {
            Element excludedNode = (Element)excludedListIterator.next();
            
            MatchFilter filter = new MatchFilter(excludedNode.getText());
            excludedFiles.addAll( Arrays.asList(directory.list(filter)));            
        }
        
        return excludedFiles;
    }
    
    public void deleteTempFiles(String geopistaDirectory) throws IOException
    {
        File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
        deleteDirectory(baseTempDirectory);
        baseTempDirectory.delete();
    }
    
    public boolean deleteDirectory(File directory) throws IOException
    {
        File[] deleteFiles = directory.listFiles();
        if (deleteFiles != null)
        {            
            for (int n = 0; n < deleteFiles.length; n++)
            {                
                File currentDeletingFile = deleteFiles[n];
                if (currentDeletingFile.isDirectory())
                {
                    deleteDirectory(currentDeletingFile);
                }
                
                boolean deleteCurrenFile = currentDeletingFile.delete();
                if (!deleteCurrenFile)
                    throw new IOException(aplicacion
                            .getI18nString("UpdateSystemPanel.ErrorAlBorrarDirectorio"));
                
            }
        }
        
        return true;
    }
    
    
    public boolean deleteDirectory(File directory, List excludedList) throws IOException
    {
        Set excludedFiles = new HashSet();        
        if (excludedList !=null)
        {
            excludedFiles = getExcludedFiles(directory, excludedList); 
        }
        
        
        File[] deleteFiles = directory.listFiles();
        if (deleteFiles != null)
        {
            
            for (int n = 0; n < deleteFiles.length; n++)
            {                
                File currentDeletingFile = deleteFiles[n];
                if (currentDeletingFile.isDirectory())
                {
                    deleteDirectory(currentDeletingFile, excludedList);
                }                
                
                //Si el fichero esta en la lista de excluidos, no se borra
                if(excludedFiles!=null && !excludedFiles.contains(currentDeletingFile.getName())) 
                {
                    boolean deleteCurrentFile =  currentDeletingFile.delete();
                    
                    if (!deleteCurrentFile)
                        throw new IOException(aplicacion
                                .getI18nString("UpdateSystemPanel.ErrorAlBorrarDirectorio"));
                }
            }
        }
        
        return true;
    }
    
    public void copyFile(File destination, File source) throws IOException
    {
        
        java.io.FileInputStream inStream = new java.io.FileInputStream(source);
        
        if (!destination.getParentFile().exists())
        {
            destination.getParentFile().mkdirs();
        }
        
        java.io.FileOutputStream outStream = new java.io.FileOutputStream(destination);
        
        int len;
        byte[] buf = new byte[2048];
        
        while ((len = inStream.read(buf)) != -1)
        {
            outStream.write(buf, 0, len);
        }
        outStream.close();
        inStream.close();
        
    }
    
    public void stopTomcatService()
    {
        try
        {
            if (tomcatStop)
                return;
            tomcatStop = true;
            String os = System.getProperty("os.name");
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net stop Tomcat5");
            }
            
        } catch (IOException e)
        {
            tomcatErrorStop = true;
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorDetenerTomcat"), aplicacion
                    .getI18nString("UpdateSystemPanel.DetenerTomcat"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void startTomcatService()
    {
        try
        {
            if (tomcatErrorStop || !tomcatStop)
                return;
            String os = System.getProperty("os.name");
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net start Tomcat5");
            }
            
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorIniciarTomcat"), aplicacion
                    .getI18nString("UpdateSystemPanel.IniciarTomcat"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void stopPostgresService() throws IOException
    {
        String os = System.getProperty("os.name");
        if (os.toUpperCase().indexOf("WINDOWS") != -1)
        {
            Process startUp = Runtime.getRuntime().exec("net stop pgsql-8.0");
        }
        
    }
    
    public void stopAdmcarService() throws IOException
    {
        
        if (admCarStop)
            return;
        admCarStop = true;
        String os = System.getProperty("os.name");
        
        try
        {
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net stop admcar");
            }
            
        } catch (IOException e)
        {
            admCarErrorStop = true;
            JOptionPane
            .showMessageDialog(
                    aplicacion.getMainFrame(),
                    aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorDetenerAdministradorCartografia"),
                    aplicacion.getI18nString("UpdateSystemPanel.DetenerAdmCar"),
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void startAdmcarService() throws IOException
    {
        
        if (admCarErrorStop || !admCarStop)
            return;
        
        String os = System.getProperty("os.name");
        
        try
        {
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net start admcar");
            }
            
        } catch (IOException e)
        {
            admCarErrorStop = true;
            JOptionPane
            .showMessageDialog(
                    aplicacion.getMainFrame(),
                    aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorDetenerAdministradorCartografia"),
                    aplicacion.getI18nString("UpdateSystemPanel.DetenerAdmCar"),
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void testConnection() throws IOException, JDOMException
    {
        URL sourceJarFile = UpdateSystemPanel.class.getResource("update.xml");
        
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(sourceJarFile);
        Element raiz = doc.getRootElement();
        String httpBase = raiz.getAttributeValue("httpBase");
        testUrlConnection(httpBase);
        
    }
    
    public static void testUrlConnection(String url) throws IOException
    {
        InputStream jarInputStream = null;
        URL sourceJarURL = new URL(url);
        URLConnection jarConnection = sourceJarURL.openConnection();
        jarInputStream = jarConnection.getInputStream();
    }
    
    public static void updateProgressBar(JProgressBar progressBar, int percentage)
    {
        progressBar.setValue(progressBar.getValue() + percentage);
        progressBar.update(progressBar.getGraphics());
    }
    
    
    class MatchFilter implements FilenameFilter 
    {
        private String cadena;
        
        public MatchFilter( String cadena ) 
        {
            //Por si vienen asteriscos en el name, los sustituimos por .*
            Pattern pattern=Pattern.compile("\\*");
            cadena = pattern.matcher(cadena).replaceAll(".*");
            this.cadena = cadena;
            
        }
        public boolean accept(File dir, String name) 
        {               
            return (name.matches(cadena));           
        }
    }
    
    public void xmlFile(List xmlFileNodes, String geopistaDirectory, String version, String basePath) throws IOException, JDOMException
    {
        if(xmlFileNodes == null || xmlFileNodes.size()==0) return;
        
        File applicactionDirectory = new File(geopistaDirectory, basePath);
        
        
        Iterator xmlFileNodesIterator = xmlFileNodes.iterator();
        while(xmlFileNodesIterator.hasNext())
        {
            Element currentXmlFileElement = (Element) xmlFileNodesIterator.next();
            Element fileNameElement = currentXmlFileElement.getChild("filename");
            String fileName = fileNameElement.getText();
            
            File sourceFile = new File(applicactionDirectory,fileName);
                                    
            SAXBuilder sourceFileDocument = new SAXBuilder();
            
            Document rootElement = sourceFileDocument.build(sourceFile);
            
            File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
            File finalTempDirectory = new File(baseTempDirectory, version);
            
            if (!finalTempDirectory.exists())
            {
                finalTempDirectory.mkdirs();
            }
            File backupFile = new File(finalTempDirectory, fileName
                    + System.currentTimeMillis());
            
            copyFile(backupFile, sourceFile);
            
            Element deleteNode = currentXmlFileElement.getChild("delete");
            if(deleteNode!=null)
            {
                List deleteNodeElements = deleteNode.getChildren("nodetodelete");
                Iterator deleteNodeElementsIterator = deleteNodeElements.iterator();
                while(deleteNodeElementsIterator.hasNext())
                {
                    Element currentDeleteXpathElement = (Element) deleteNodeElementsIterator.next();
                    String currentDeleteXpath = currentDeleteXpathElement.getText();
                    
                    List selectNodesToDelete = XPath.selectNodes(rootElement, currentDeleteXpath);
                    
                    Iterator selectNodesTodeleteIterator = selectNodesToDelete.iterator();
                    while(selectNodesTodeleteIterator.hasNext())
                    {
                        Element currentDeleteElement = (Element) selectNodesTodeleteIterator.next();
                        rootElement.getRootElement().removeContent(currentDeleteElement);
                    }
                }
            }
            
            
            
            Element insertNode = currentXmlFileElement.getChild("insert");
            if(insertNode!=null)
            {
                List insertNodesElements = insertNode.getChildren("value");
                Iterator insertNodesElementsIterator = insertNodesElements.iterator();
                while(insertNodesElementsIterator.hasNext())
                {
                    Element currentInsertElement = (Element) insertNodesElementsIterator.next();
                    Element nodeToInsertElement = currentInsertElement.getChild("nodetoinsert");
                    Element valueToInsertElement =  (Element) currentInsertElement.getChild("valuetoinsert").getChildren().get(0);
                    
                    
                    String nodeToInsert = nodeToInsertElement.getText();
                    
                    List selectNodesToInsert = XPath.selectNodes(rootElement, nodeToInsert);
                    Iterator selectNodesToInsertIterator = selectNodesToInsert.iterator();
                    while(selectNodesToInsertIterator.hasNext())
                    {
                        valueToInsertElement.detach();
                        Element currentSelectNodeToInsertElement = (Element) selectNodesToInsertIterator.next();
                        currentSelectNodeToInsertElement.addContent(valueToInsertElement);
                    }
                    
                }
            }
            
            FileOutputStream fileOutputStream = null;
            try
            {
                XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                fileOutputStream = new FileOutputStream(sourceFile);
                out.output(rootElement,fileOutputStream);
            }finally
            {
                fileOutputStream.close();
            }
            
        }
    }    

    public void replaceXML(List xmlFile, String geopistaDirectory,
    		String version, String sourceJarsUrl, String basePath) throws IOException,
    		JDOMException
    		{
    	if (xmlFile == null)
    		return;
    	Iterator xmlFileIter=xmlFile.iterator();
    	while(xmlFileIter.hasNext()){
    		Element xmlFileElement =(Element)xmlFileIter.next();
    		//este sera el antiguo .xml
    		Element pathXMLold=xmlFileElement.getChild("oldXmlFile");
    		String pathxmlold=pathXMLold.getTextTrim();
    		//este sera el nuevo .xml
    		Element pathXMLnew=xmlFileElement.getChild("newXmlFile");
    		String pathxmlnew=pathXMLnew.getTextTrim();

    		//cogemos el .xml antiguo lo copiamos a la carpeta de backup
    		File targetPath = new File(geopistaDirectory, basePath);
    		File webINFxml = new File(targetPath, pathxmlold);
    		//copiamos el .xml antiguo en una carpeta de seguridad
    		File backupPath = new File(targetPath, BACKUP_DIRECTORY);
    		if (!backupPath.exists())
    		{
    			backupPath.mkdirs();
    		}
    		File backupFile = new File(backupPath, pathxmlold + System.currentTimeMillis());
    		copyFile(backupFile, webINFxml);

    		SAXBuilder builder = new SAXBuilder();
    		Document oldDoc = builder.build(webINFxml);

    		//traemos a temp los archivos que queremos del servidor
    		File baseTempDirectory = new File(geopistaDirectory, TEMP_DIRECTORY);
    		File finalTempDirectory = new File(baseTempDirectory, version);
    		File sourceFile = new File(finalTempDirectory, pathxmlnew);
    		copyRemoteFile(sourceJarsUrl + "/" + pathxmlnew, sourceFile);

    		//Tenemos que coger el nodo de el xml de temp y cambiarlo por el nodo que queremos conservar
    		Document newDoc = builder.build(sourceFile);


    		//este seran los nodos a conservar
    		List listapreserveNODE=xmlFileElement.getChildren("maintenanceNode");
    		Iterator listapreserveNODEIter=listapreserveNODE.iterator();

    		while(listapreserveNODEIter.hasNext()){
    			Element nombrenodo=(Element)listapreserveNODEIter.next();
    			String nombrenodostr=nombrenodo.getTextTrim();

    			//borramos los nodos con ese valor en el documento destino
    			List currentDeleteNodes = XPath.selectNodes(newDoc, nombrenodostr);
    			Iterator currentDeleteNodesIterator = currentDeleteNodes.iterator();
    			while(currentDeleteNodesIterator.hasNext())
    			{
    				Element currentDeleteNode = (Element) currentDeleteNodesIterator.next();
    				newDoc.getRootElement().removeContent(currentDeleteNode);
    			}

    			//añadimos los nodos que queremos conservar al documento destino
    			List currentNodes = XPath.selectNodes(oldDoc, nombrenodostr);
    			Iterator currentNodesIterator = currentNodes.iterator();
    			while(currentNodesIterator.hasNext())
    			{
    				Element currentMaintenanceElement = (Element) currentNodesIterator.next();
    				currentMaintenanceElement.detach();
    				newDoc.getRootElement().addContent(currentMaintenanceElement);

    			}

    		}

    		// grabamos en disco el documento final con los nodos sustituidos
    		FileOutputStream outputStrean = null;
    		try
    		{
    			outputStrean = new FileOutputStream(webINFxml);
    			XMLOutputter outp = new XMLOutputter();
    			outp.output(newDoc, outputStrean);
    		} finally
    		{
    			try
    			{
    				outputStrean.close();
    			}catch (Exception e)
    			{

    			}
    		}
    	}
    }
    
    /**
     * Se mete una entrada en server.xml para cada aplicación web que vendrá definido para 
     * cada módulo dentro del elemento webApps.
     */
    public void addWebAplication(String path, String tomcatPath, Element webApps) throws IOException,
            JDOMException
    {
        File configurationFile = null;
        try
        {
            
            configurationFile = new File(tomcatPath,"conf");
            configurationFile = new File(configurationFile,"server.xml");
            
            // copiamos el jnlp destino en una carpeta de seguridad
            File backupPath = new File(tomcatPath, BACKUP_DIRECTORY);
            if (!backupPath.exists())
            {
                backupPath.mkdirs();
            }
            File backupFile = new File(backupPath, "server.xml"
                    + System.currentTimeMillis());
            copyFile(backupFile, configurationFile);
            
            // abrimos el archivo origen xml para leer los dos nodos que
            // necesitamos conservar
            SAXBuilder builder = new SAXBuilder(false);
            
            
            Document docNew = builder.build(configurationFile);
            Element rootElement = docNew.getRootElement();
            Element serviceNode = rootElement.getChild("Service"); 
            Element engineNode = serviceNode.getChild("Engine");
            Element hostNode = engineNode.getChild("Host");
            
            
            List contextList = hostNode.getChildren("Context");
            Iterator contextListIterator = contextList.iterator();
            ArrayList deleteNodes = new ArrayList();
            while(contextListIterator.hasNext())
            {
                Element currentContext = (Element) contextListIterator.next();
                String pathAttribute = currentContext.getAttribute("path").getValue();
                if(pathAttribute != null && pathAttribute.equalsIgnoreCase(path))
                {
                    deleteNodes.add(currentContext);
                }
                
            }
            
            Iterator deleteNodesIterator = deleteNodes.iterator();
            while(deleteNodesIterator.hasNext())
            {
                Element currentDeleteNodes = (Element) deleteNodesIterator.next();
                hostNode.removeContent(currentDeleteNodes);
                
            }
          
            //Inserto el texto correspondiente a la nueva aplicación en server.xml
            //Para ello cojo todos los elementos dentro de la etiqueta webApp y guardo tanto
            //los elementos como sus atributos
            Element context = null;
            List listaWebApps = webApps.getChildren();
            Iterator itWebApps = listaWebApps.iterator();
            while(itWebApps.hasNext()){
            	Element elemento = (Element)itWebApps.next();
            	String cadena = elemento.getName();
                context = new Element(cadena);
                Iterator listaAtributos = elemento.getAttributes().iterator();
                //Primero pintamos los atributos del elemento Context
                while (listaAtributos.hasNext()){
                	Attribute atributo = (Attribute)listaAtributos.next();
                	//Si algún atributo consiste en una variable de las que hay que sustituir, reemplazo
                	//ésta por su valor
                    context.setAttribute(atributo.getName(), replaceStrings(atributo.getValue()));
                }
                hostNode.addContent(context);

                //Luego para los elementos dentro de Context
            	List listaHijos = elemento.getChildren();
            	Iterator itListaHijos = listaHijos.iterator();
                while(itListaHijos.hasNext()){
                	Element elemento2 = (Element)itListaHijos.next();
                	String cadena2 = elemento2.getName();
                	Element context2 = new Element(cadena2);
                    Iterator listaAtributos2 = elemento2.getAttributes().iterator();
                    //Primero pintamos los atributos del elemento Context
                    while (listaAtributos2.hasNext()){
                    	Attribute atributo = (Attribute)listaAtributos2.next();
                    	//Si algún atributo consiste en una variable de las que hay que sustituir, reemplazo
                    	//ésta por su valor
                        context2.setAttribute(atributo.getName(), replaceStrings(atributo.getValue()));
                    }
	                if (context2 != null)
	                    context.addContent(context2);
                }
            }
            
            
            FileOutputStream outputStrean = null;
            try
            {
                outputStrean = new FileOutputStream(configurationFile);
                XMLOutputter outp = new XMLOutputter();
                outp.output(docNew, outputStrean);
                	
            } finally
            {
                try
                {
                    outputStrean.close();
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.FileConfigurationTomcatError")
                    + " " + configurationFile.getAbsolutePath()+"\n"+e.getMessage());
        }
    }
    
    /**
     * Se borra el context con el path que pasemos del fichero server.xml.
     */
    public void deleteContext(List pathList, String tomcatPath) throws IOException,
            JDOMException
    {
        File configurationFile = null;
        try
        {
            
            configurationFile = new File(tomcatPath,"conf");
            configurationFile = new File(configurationFile,"server.xml");
            
            // copiamos el jnlp destino en una carpeta de seguridad
            File backupPath = new File(tomcatPath, BACKUP_DIRECTORY);
            if (!backupPath.exists())
            {
                backupPath.mkdirs();
            }
            File backupFile = new File(backupPath, "server.xml"
                    + System.currentTimeMillis());
            copyFile(backupFile, configurationFile);
            
            // abrimos el archivo origen xml para leer los dos nodos que
            // necesitamos conservar
            SAXBuilder builder = new SAXBuilder(false);
            
            
            Document docNew = builder.build(configurationFile);
            Element rootElement = docNew.getRootElement();
            Element serviceNode = rootElement.getChild("Service"); 
            Element engineNode = serviceNode.getChild("Engine");
            Element hostNode = engineNode.getChild("Host");
            
            
            List contextList = hostNode.getChildren("Context");
            Iterator contextListIterator = contextList.iterator();
            ArrayList deleteNodes = new ArrayList();
            while(contextListIterator.hasNext())
            {
                Element currentContext = (Element) contextListIterator.next();
                String pathAttribute = currentContext.getAttribute("path").getValue();
                Iterator pathListIterator = pathList.iterator();
                while(pathListIterator.hasNext()){
                	Element path = (Element)pathListIterator.next();
	                if(pathAttribute != null && pathAttribute.equals(path.getValue()))
	                {
	                    deleteNodes.add(currentContext);
	                }
                }
            }
            
            Iterator deleteNodesIterator = deleteNodes.iterator();
            while(deleteNodesIterator.hasNext())
            {
                Element currentDeleteNodes = (Element) deleteNodesIterator.next();
                hostNode.removeContent(currentDeleteNodes);
                
            }
            FileOutputStream outputStrean = null;
            try
            {
                outputStrean = new FileOutputStream(configurationFile);
                XMLOutputter outp = new XMLOutputter();
                outp.output(docNew, outputStrean);
                	
            } finally
            {
                try
                {
                    outputStrean.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.FileConfigurationTomcatError")
                    + " " + configurationFile.getAbsolutePath());
        }
    }
    
    
    public void stopMapServerService()
    {
        try
        {
            String os = System.getProperty("os.name");
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net stop ApacheMS4WWebServer");
            }
            
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorIniciarMapServer"), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorMapServer"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void startMapServerService()
    {
        try
        {

            String os = System.getProperty("os.name");
            if (os.toUpperCase().indexOf("WINDOWS") != -1)
            {
                Process startUp = Runtime.getRuntime().exec("net start ApacheMS4WWebServer");
            }
            
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("UpdateSystemPanel.ErrorIniciarTomcat"), aplicacion
                    .getI18nString("UpdateSystemPanel.IniciarTomcat"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }    

    /**
     * Instalacion de un servicio de windows.
     * @param installServiceNode
     * @param geopistaDirectory
     * @param version
     * @param basePath
     * @throws IOException
     * @throws JDOMException
     */
    public void installService(Element installServiceNode, String geopistaDirectory, String version, String basePath) throws IOException, JDOMException
    {
        if(installServiceNode == null) return;
                
        List serviceFiles = installServiceNode.getChildren("service");
        Iterator serviceFilesIterator = serviceFiles.iterator();

        while(serviceFilesIterator.hasNext())
        {
        	        
            Element currentServiceElement = (Element) serviceFilesIterator.next();
            String service = currentServiceElement.getText();
        	System.out.println("Revisando servicio:"+service);
            try{
            	//Intentamos modificar el nombre del servicio por si dispone de alguna
            	//variable de reemplazo por ejemplo %GEOPISTA_ROOT%
            	service=replaceStrings(service);
            }
            catch(Exception e){            	
            }
            
            try
            {
                String os = currentServiceElement.getAttributeValue("os");
                
                String sys = System.getProperty("os.name");

                if (os!=null)
                {
                	if(sys.toUpperCase().indexOf("WINDOWS")==-1){
                		//Linux
                		if(os.equalsIgnoreCase("windows"))
                			continue;
                    }
                	else{
                		//Windows
                		if(os.equalsIgnoreCase("linux"))
                			continue;
                	}
                }
                try {
                	System.out.println("Instalando servicio:"+service);
					Process startUp = Runtime.getRuntime().exec(service);
					//StreamGobblerParametros errorGobbler = new
					//StreamGobblerParametros(startUp.getErrorStream(), "ERROR");

					// any output?
					//StreamGobblerParametros outputGobbler = new
					    //StreamGobblerParametros(startUp.getInputStream(), "OUTPUT");
					    

					// kick them off
					//errorGobbler.start();
					//outputGobbler.start();
					startUp.waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } catch (IOException e)
            {
            	e.printStackTrace();
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("UpdateSystemPanel.ErrorIniciarMapServer"), aplicacion
                        .getI18nString("UpdateSystemPanel.ProblemasMapServer"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }      
    
    /**
     * Instalacion de un servicio de windows.
     * @param installServiceNode
     * @param geopistaDirectory
     * @param version
     * @param basePath
     * @throws IOException
     * @throws JDOMException
     */
    public void execute(Element installServiceNode, String geopistaDirectory, String version, String basePath, Connection connection) throws IOException, JDOMException
    {
        if(installServiceNode == null) return;
                
        List serviceFiles = installServiceNode.getChildren("exec");
        Iterator serviceFilesIterator = serviceFiles.iterator();

        while(serviceFilesIterator.hasNext())
        {
        	        
            Element currentServiceElement = (Element) serviceFilesIterator.next();
            String service = currentServiceElement.getText();
        	System.out.println("Ejecutando comando:"+service);
            try{
            	//Intentamos modificar el nombre del servicio por si dispone de alguna
            	//variable de reemplazo por ejemplo %GEOPISTA_ROOT%
            	service=replaceStrings(service);
            }
            catch(Exception e){            	
            }
            
            try
            {
                String os = currentServiceElement.getAttributeValue("os");
                String type= currentServiceElement.getAttributeValue("type");
                
                if ((type!=null) && (type.equals("reflection"))){
                    boolean error = false;
                    Method method;
                    try {
                        method = this.getClass().getMethod(service, new Class[] {Connection.class, String.class});
                        method.invoke(this, new Object[] {connection, geopistaDirectory});
                    } catch (SecurityException e) {
                        System.out.println("Restriccion de seguridad violada al intentar llamar al metodo \""+service+"\".");
                        e.printStackTrace();
                        error = true;
                    } catch (NoSuchMethodException e) {
                        System.out.println("No existe el metodo \""+service+"\".");
                        e.printStackTrace();
                        error = true;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Argumentos incorrectos al llamar al metodo \""+service+"\".");
                        e.printStackTrace();
                        error = true;
                    } catch (IllegalAccessException e) {
                        System.out.println("Acceso ilegal al llamar al metodo \""+service+"\".");
                        e.printStackTrace();
                        error = true;
                    } catch (InvocationTargetException e) {
                        System.out.println("Excepcion \""+e.getCause().getClass()+"\" al llamar al metodo \""+service+"\".");
                        e.printStackTrace();
                        error = true;
                    }
                    if (error) {
                        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Error al invocar el comando", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
	                String sys = System.getProperty("os.name");
	
	                if (os!=null)
	                {
	                	if(sys.toUpperCase().indexOf("WINDOWS")==-1){
	                		//Linux
	                		if(os.equalsIgnoreCase("windows"))
	                			continue;
	                    }
	                	else{
	                		//Windows
	                		if(os.equalsIgnoreCase("linux"))
	                			continue;
	                	}
	                }
	                try {
	                	System.out.println("Ejecutando:"+service);
						Process startUp = Runtime.getRuntime().exec(service);
						StreamGlobberParametros errorGlobber = new
						StreamGlobberParametros(startUp.getErrorStream(), "ERROR");
	
						// any output?
						StreamGlobberParametros outputGlobber = new StreamGlobberParametros(startUp.getInputStream(), "OUTPUT");
	
						// kick them off
						errorGlobber.start();
						outputGlobber.start();
						
						//Timer para el proceso
						Timer timer = new Timer();
						timer.schedule(new KillProcess(service, startUp, outputGlobber, errorGlobber), 60000);
						
						startUp.waitFor();
						// Si el proceso acaba se cancela el timer
						timer.cancel();
						
						System.out.println("Finalizado:"+service);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } 
            } catch (IOException e)
            {
            	e.printStackTrace();
                JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                        .getI18nString("UpdateSystemPanel.ErrorIniciarMapServer"), aplicacion
                        .getI18nString("UpdateSystemPanel.ProblemasMapServer"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }        
    
    private String modifyPortCodebase(String cadena){
    	String[] cadenaDiv = cadena.split("/");
    	String host = cadenaDiv[2];
    	String[] hostDiv = host.split(":");
    	StringBuffer st = new StringBuffer();
    	st.append(cadenaDiv[0]);
    	st.append("//");
    	st.append(hostDiv[0]);
    	st.append(":");
    	st.append(this.PUERTO_INST);
    	st.append("/");
    	st.append(cadenaDiv[3]);
    	st.append("/");
    	return this.replaceStrings(st.toString());
     }
    
    /**
     * Actualiza los municipios que estuvieran previamente configurados en
     * Geopista de forma que en la base de datos se informe del SRID para cada
     * uno de ellos.
     */
    public void executeActualizacionMunicipios(Connection connection, String geopistaDirectory) {
        boolean error = false;
        File file = new File(geopistaDirectory+"/admcar/classes/config/srid.properties");
        String pattern = "geopista.srid.";
        /*
         * Leemos el fichero e insertamos todos los srids de todos los municipios
         */
        try {
            Properties properties = new Properties();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE municipios SET srid = ? WHERE id = ?");
            properties.load(new FileInputStream(file));
            Iterator itKeys = properties.keySet().iterator();
            while (itKeys.hasNext()) {
                String key = (String) itKeys.next();
                if (key.startsWith(pattern)) {
                    try {
                        Integer idMunicipio = new Integer(key.substring(pattern.length()));
                        preparedStatement.setString(1, properties.getProperty(key));
                        preparedStatement.setInt(2, idMunicipio.intValue());
                        preparedStatement.executeUpdate();
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("El fichero \"admcar/classes/config/srid.properties\" no existe o no se puede leer.");
            e.printStackTrace();
            error = true;
        } catch (IOException e) {
            System.out.println("Error de I/O al leer el fichero \"admcar/classes/config/srid.properties\".");
            e.printStackTrace();
            error = true;
        } catch (SQLException e) {
            System.out.println("Error de base de datos al actualizar los municipios.");
            e.printStackTrace();
            error = true;
        }
        if (error) {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Error al actualizar municipios", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void changeStyleNames(Connection connection,String nothing) throws Exception 
    {
    	
		String sSQL = "select * from styles";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = connection;
			ps = conn.prepareStatement(sSQL);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				do{
					String sOldXML = rs.getString("xml");
					String sNewXML = getActualicedXMLStyle(sOldXML);
					
					rs.updateString("xml",sNewXML);
					rs.updateRow();
	            }while (rs.next());
			}
		} catch (SQLException e) {
			System.out.println("Error de base de datos los nombres de los estilos SLD.");
            e.printStackTrace();
          //  JOptionPane.showMessageDialog(aplicacion.getMainFrame(), "Error al estilos SLD", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
    
    private String getActualicedXMLStyle (String sXML)
    {
    	Vector<String> styleNames = new Vector<String>();
    	Namespace ns = Namespace.getNamespace("http://www.opengis.net/sld");
    	String sLayerName="";

    	try {
    		SAXBuilder builder = new SAXBuilder(false);
			ByteArrayInputStream input = new ByteArrayInputStream(sXML.getBytes()) ;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			Document docNew = builder.build(input);
			Element rootElement = docNew.getRootElement();
			Element el1= rootElement.getChild("NamedLayer",ns);
			
			sLayerName=el1.getChildText("Name", ns);
			List lNames = el1.getChildren();
            Iterator iterator = lNames.iterator();
            while (iterator.hasNext()) {
                Element oneUserStyle = (Element) iterator.next();
                if (oneUserStyle.getName()=="UserStyle")
                {
                	if (!oneUserStyle.getChildText("Name",ns).startsWith(sLayerName.trim()+":_:"))
                	{
                		oneUserStyle.getChild("Name",ns).setText(sLayerName.trim()+":_:"+oneUserStyle.getChildText("Name",ns));
                		
                                if (oneUserStyle.getChild("Title",ns)!=null)
                                {
                                    oneUserStyle.getChild("Title",ns).setText(sLayerName.trim()+":_:"+oneUserStyle.getChildText("Name",ns));
                                }

                                if (oneUserStyle.getChild("Abstract",ns)!=null)
                                {
                                    oneUserStyle.getChild("Abstract",ns).setText(sLayerName.trim()+":_:"+oneUserStyle.getChildText("Name",ns));
                                }
                	}
                }
            }
            XMLOutputter outp = new XMLOutputter();
            return outp.outputString(docNew);
            
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sXML;
    }
}
