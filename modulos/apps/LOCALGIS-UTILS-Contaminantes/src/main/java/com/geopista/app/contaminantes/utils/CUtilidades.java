/**
 * CUtilidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.contaminantes.CPlantilla;
import com.geopista.protocol.contaminantes.Historico;
import com.geopista.util.config.UserPreferenceStore;
//import jimm.datavision.field.ColumnField;
//import jimm.datavision.layout.LayoutEngine;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 20-oct-2004
 * Time: 10:50:01
 * To change this template use File | Settings | File Templates.
 */
public class CUtilidades {
    static Logger logger = Logger.getLogger(CUtilidades.class);
    public static Icon iconoAdd;
    public static Icon iconoRemove;
    public static Icon iconoAbrir;
    public static Icon iconoBuscar;

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public static void inicializar()
        {
            try
            {
                ClassLoader cl =(new CUtilidades()).getClass().getClassLoader();
                iconoAdd= new javax.swing.ImageIcon(cl.getResource("img/add.gif"));
                iconoRemove= new javax.swing.ImageIcon(cl.getResource("img/remove.gif"));
                iconoAbrir= new javax.swing.ImageIcon(cl.getResource("img/abrir.gif"));
                iconoBuscar= new javax.swing.ImageIcon(cl.getResource("img/zoom.gif"));
            }catch(Exception e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("Exception al inicializar las imagenes: " + sw.toString());
            }
        }

    //Comentamos este método ya que da problemas en tiempo de ejecución y no se utiliza

//    public static void crearReportCharSepSource(Vector v, String path, String reportName, String formato) throws Exception{
//
//        CPlantilla XMLTemplate= (CPlantilla)v.get(0);
//        CPlantilla paramXML= (CPlantilla)v.get(1);
//        logger.debug("...................... Plantilla y parametros  ..................");
//        logger.debug("***** XMLTemplate="+XMLTemplate.getPath());
//        logger.debug("***** paramXML="+paramXML.getPath());
//        logger.debug("................................................................");
//        Report r= new Report();
//
//        // Read the report XML from a file or various stream types.
//        logger.debug("r.read("+path + XMLTemplate.getFileName()+")");
//        r.read(path + XMLTemplate.getFileName());
//
//        CharSepSource src = (CharSepSource)r.getDataSource();
//        src.setSepChar(','); // Optional; overrides default and XML file value
//        src.setInput(path + "datos.csv");
//        // If necessary, read parameter values.
//        if (r.hasParameterFields()){
//            try{
//                r.setParameterXMLInput(path + paramXML.getFileName());
//            }catch(Exception e){
//            }
//        }
//
//        if ((reportName.trim().length() == 0) || (reportName.startsWith("."))) reportName= "output";
//
//        logger.debug("******nombreInforme="+reportName);
//        if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoPDF){
//            logger.debug("***** formato PDF *****");
//            if (!reportName.endsWith(".pdf"))
//                reportName= reportName + ".pdf";
//            FileOutputStream fileOut= new FileOutputStream(path + reportName);
//            r.setLayoutEngine(new PDFLE(fileOut));
//        }else if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoTEXTO){
//            logger.debug("***** formato TXT *****");
//            if (!reportName.endsWith(".txt"))
//                reportName= reportName + ".txt";
//            PrintWriter out = new PrintWriter(new FileWriter(path + reportName));
//            r.setLayoutEngine(new CharSepLE(out, '\t'));
//        }
//        /*else if(formatoJCBox.getSelectedIndex() == CConstantesLicencias.formatoHTML){
//            if ((!nombreInforme.endsWith(".html")) && (!nombreInforme.endsWith(".htm")))
//                nombreInforme= nombreInforme + ".html";
//            PrintWriter fileOut = new PrintWriter(new FileWriter(CConstantesPaths.PLANTILLAS_PATH + nombreInforme));
//            r.setLayoutEngine(new HTMLLE(fileOut));
//        }*/
//        else if(new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoPREVIEW){
//            logger.debug("***** formato PREVIEW *****");
//            logger.debug("Parametros="+path + paramXML.getFileName() + " " + path + "datos.csv");
//            LayoutEngine le = new SwingLE(path + paramXML.getFileName(), path + "datos.csv", true) {
//                public void close() {
//                    super.close();
//                    // Cierra la aplicacion
//                    //System.exit(0);
//                }
//            };
//
//            r.setLayoutEngine(le);
//        }
//
//        r.runReport();
//
//    }

    //Comentamos este método ya que da problemas en tiempo de ejecución y no se utiliza
    
//    public static void crearReportBDSource(Hashtable h, String pathPlantilla, String reportName, String formato, String pathReport, String app) throws Exception{
//
//        Connection conn= null;
//
//        try{
//            CPlantilla paramXML= new CPlantilla();
//            if (h.containsKey("PLANTILLA")){
//                CPlantilla XMLTemplate= (CPlantilla)h.get("PLANTILLA");
//                logger.debug("***** XMLTemplate="+XMLTemplate.getPath());
//                Report r= new Report();
//                if ((com.geopista.app.contaminantes.Constantes.DriverClassName != null) && (com.geopista.app.contaminantes.Constantes.DriverClassName.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver"))){
//                    r.setOracle(true);
//                }
//
//
//                /** Eliminamos de la lista de drivers del DriverManager el driver jdbc:pista: que maneja la clase com.geopista.sql.GEOPISTADriver,
//                 *  puesto que nos da problemas para crear una conexion con el driver jdbc:postgresql:
//                 */
//                /*
//                String urlGEOPISTA= "jdbc:pista:";
//                try{
//                    Driver driver= DriverManager.getDriver(urlGEOPISTA);
//                    DriverManager.deregisterDriver(driver);
//                }catch(SQLException ex){
//                    logger.debug(".................Driver " + urlGEOPISTA + " " + ex.getMessage());
//                }
//                */
//                // Eliminamos TODOS, por si cambia la urlGEOPISTA
//                logger.debug("...................... DRIVERS registrados......................");
//                Enumeration e= DriverManager.getDrivers();
//                while (e.hasMoreElements()){
//                    Driver d= (Driver)e.nextElement();
//                    DriverManager.deregisterDriver(d);
//                    logger.debug("Driver="+d.toString());
//                }
//                logger.debug("................................................................");
//
//
//                /** La conexion desde el report se puede hacer:
//                    caso 1.-: pasando al report la conexion
//                    caso 2.-: pasando al report la password de conexion a BD.
//                             El resto de datos que necesita para hacer la conexion a BD los recoge de la plantilla en <source><database>
//                */
//
//                /** caso 1 */
//                Driver dPostgres= (Driver)Class.forName(com.geopista.app.contaminantes.Constantes.DriverClassName).newInstance();
//                DriverManager.registerDriver(dPostgres);
//                //DriverManager.registerDriver(new org.postgresql.Driver());
//                logger.debug("...................... DRIVERS registrados......................");
//                e= DriverManager.getDrivers();
//                while (e.hasMoreElements()){
//                    Driver d= (Driver)e.nextElement();
//                    logger.debug("Driver="+d.toString());
//                }
//                logger.debug("................................................................");
//
//                conn = DriverManager.getConnection(com.geopista.app.contaminantes.Constantes.ConnectionInfo, com.geopista.app.contaminantes.Constantes.DBUser, com.geopista.app.contaminantes.Constantes.DBPassword);
//                r.setDatabaseConnection(conn);
//
//
//                /** caso 2 */
//                /*
//                r.setDatabasePassword(com.geopista.app.contaminantes.Constantes.DBPassword);
//                */
//
//                // Read the report XML from a file or various stream types.
//                logger.debug("r.read("+ pathPlantilla + XMLTemplate.getFileName()+")");
//                r.read(pathPlantilla + XMLTemplate.getFileName());
//
//                // If necessary, read parameter values.
//                if (r.hasParameterFields()){
//                    if (h.containsKey("PARAM")){
//                        paramXML= (CPlantilla)h.get("PARAM");
//                        logger.debug("***** paramXML="+paramXML.getPath());
//                        r.setParameterXMLInput(pathPlantilla + paramXML.getFileName());
//                    }else{
//                        /** No existe el fichero param.xml */
//                        throw new Exception("No se ha podido leer el fichero param.xml");
//                    }
//                }
//
//                logger.debug("******nombreInforme="+reportName);
//                if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoPDF){
//                    logger.debug("***** formato PDF *****");
//                    if (!reportName.endsWith(".pdf"))
//                        reportName= reportName + ".pdf";
//                    FileOutputStream fileOut= new FileOutputStream(pathReport + reportName);
//                    r.setLayoutEngine(new PDFLE(fileOut));
//                }else if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoTEXTO){
//                    logger.debug("***** formato TXT *****");
//                    if (!reportName.endsWith(".txt"))
//                        reportName= reportName + ".txt";
//                    PrintWriter out = new PrintWriter(new FileWriter(pathReport + reportName));
//                    r.setLayoutEngine(new CharSepLE(out, '\t'));
//                }
//                /*else if(formatoJCBox.getSelectedIndex() == CConstantesLicencias.formatoHTML){
//                    if ((!nombreInforme.endsWith(".html")) && (!nombreInforme.endsWith(".htm")))
//                        nombreInforme= nombreInforme + ".html";
//                    PrintWriter fileOut = new PrintWriter(new FileWriter(CConstantesPaths.PLANTILLAS_PATH + nombreInforme));
//                    r.setLayoutEngine(new HTMLLE(fileOut));
//                }*/
//                else if(new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoPREVIEW){
//                    logger.debug("***** formato PREVIEW *****");
//                    /** Fuente de datos BD */
//                    SwingLE le = new SwingLE(pathPlantilla + paramXML.getFileName(), true) {
//                        public void close() {
//                            super.close();
//                            // Cierra la aplicacion
//                            //System.exit(0);
//                        }
//
//                    };
//
//                    r.setLayoutEngine(le);
//                }
//
//                // query para la fuente de datos BD
//                DataSource ds= r.getDataSource();
//                Query query= ds.getQuery();
//
//                String whereclause= "";
//                boolean hasDictionary= false;
//                query.findSelectablesUsed();
//                Iterator iterator= query.selectables();
//                while (iterator.hasNext() && !hasDictionary){
//                    Selectable selectable= (Selectable)iterator.next();
//                    /** ORACLE:   DBUser.DICTIONARY
//                     *  POSTGRES: public.dictionary */
//                    // VALIDO SOLO ORACLE if (selectable.getTable().getName().equalsIgnoreCase(CConstantesLicencias.DBUser+"."+"DICTIONARY")) hasDictionary= true;
//                    if (selectable.getTable().getName().toUpperCase().endsWith("."+"DICTIONARY")) hasDictionary= true;
//                }
//                if (hasDictionary){
//                    if (r.isOracle()){
//                        /** Esto solo es necesario hacerlo si la BD es Oracle, ya que las tablas que aparezcan
//                         * en la clausula where han de aparecer en el from. Para la join con dictionary
//                         * necesitamos la tabla domainnodes, y ésta no está en el from de la query que genera
//                         * Datavision al no tener ninguna columna de esta tabla en la seccion Detail. */
//                        Section section= (r.details()!=null?(r.details().hasNext()?(Section)r.details().next():null):null);
//                        if (section == null) throw new Exception("Plantilla sin seccion DETAIL");
//                        ColumnField cf= new ColumnField(null, r, section, com.geopista.app.contaminantes.Constantes.DBUser.toUpperCase()+"."+"DOMAINNODES.ID_DESCRIPTION", false);
//                        section.addField(cf);
//                    }
//                    /** Annadimos al where la join de la tabla dictionary con la tabla domainnodes */
//                    whereclause= addToWhereClause(app);
//                }
//                query.setWhereClause(whereclause);
//
//                logger.info("QUERY="+query.toString());
//                /** Fin Fuente de datos BD */
//                r.runReport();
//            }else{
//                throw new Exception("No se ha podido leer la plantilla.");
//            }
//
//        }catch (Exception e) {
//            throw e;
//        }
//        finally {
//            if (conn != null) {
//                try {
//                conn.close();
//                }
//                catch (SQLException sqle) {
//                logger.error("SQL exception thrown: " + sqle);
//                }
//            }
//        }
//
//    }

    public static String addToWhereClause(String app){

        if (app == null) return "";
        /* NOTA: Sacamos la traduccion del tipo de obra/actividad de la tabla DICTIONARY */
        String where= "DICTIONARY.ID_VOCABLO=DOMAINNODES.ID_DESCRIPTION AND ";

        if (app.equalsIgnoreCase("ACTIVIDAD_CONTAMINANTE")){
            /* domains.name='TIPO_ACTIVIDAD_CONTAMINANTE' domains.id=72 */
            where+= "DOMAINNODES.PATTERN=ACTIVIDAD_CONTAMINANTE.ID_TIPO_ACTIVIDAD AND " +
                    "DOMAINNODES.ID_DOMAIN=72 AND ";
        }else if (app.equalsIgnoreCase("ARBOLADO")){
            /* domains.name='TIPO_ZONA_ARBOLADA' domains.id=147 */
            where+= "DOMAINNODES.PATTERN=ZONAS_ARBOLADAS.ID_TIPO AND " +
                    "DOMAINNODES.ID_DOMAIN=147 AND ";
        }

        //"DICTIONARY.LOCALE='" + com.geopista.app.contaminantes.Constantes.Locale + "' " +
        where+= "DICTIONARY.LOCALE='es_ES'";

        return where;
    }



    /** Bajamos la plantilla seleccionada y el fichero de parámetros, que siempre sera el mismo
     * independientemente de la plantilla seleccionada.
     * Lo hacemos directamente con una conexion HTTP, por si acaso el fichero es muy grande.
     * NOTA: Plantilla tipo: puede tener un fichero de parametros y 2 imagenes (derecha e izquierda). */
     public static Hashtable bajarXMLFiles(CPlantilla p){
         String path= "";
         String url= "";
         String pathDestino= "";
         String name= "";

         try {
             Hashtable hPlantillas = new Hashtable();
             path= p.getPath();
             name= p.getFileName();

             /* Comprobamos que el path de las plantillas y param.xml exista */
             if (!new File(path).exists()) {
                 new File(path).mkdirs();
             }

             /** bajamos la plantilla */
             pathDestino= path + name;
             url= com.geopista.protocol.CConstantesComando.servidorUrl + path + name;
             boolean resultado = GetURLFile(url, pathDestino, "", 0);
             if (resultado) {
                 CPlantilla plantilla = new CPlantilla(name);
                 plantilla.setPath(path);
                 hPlantillas.put("PLANTILLA", plantilla);
             }

             pathDestino= "";
             url= "";
             /** bajamos el fichero param.xml */
             pathDestino= path + "param.xml";
             url= com.geopista.protocol.CConstantesComando.servidorUrl + path + "param.xml";
             resultado= GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                 CPlantilla param = new CPlantilla("param.xml");
                 param.setPath(path);
                 hPlantillas.put("PARAM",param);
             }

             /* Comprobamos que el path de las imagenes exista */
             if (!new File(path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH).exists()) {
                 new File(path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH).mkdirs();
             }

             pathDestino= "";
             url= "";
             /** bajamos la imagenes */
             pathDestino= path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH + "derecha.gif";
             url=  com.geopista.protocol.CConstantesComando.servidorUrl + path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH + "derecha.gif";
             resultado = GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                 CPlantilla imagen = new CPlantilla("derecha.gif");
                 imagen.setPath(path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH);
                 hPlantillas.put("IMG1", imagen);
             }

             pathDestino= "";
             url= "";
             pathDestino= path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH + "izquierda.gif";
             url=  com.geopista.protocol.CConstantesComando.servidorUrl + path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH + "izquierda.gif";
             resultado = GetURLFile(url, pathDestino, "", 0);
             if (resultado){
                 CPlantilla imagen = new CPlantilla("izquierda.gif");
                 imagen.setPath(path + com.geopista.protocol.contaminantes.CConstantes.IMAGE_PATH);
                 hPlantillas.put("IMG2",imagen);
             }

             return hPlantillas;

         } catch (Exception ex) {
             StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw);
             ex.printStackTrace(pw);
             logger.error("Exception: " + sw.toString());
             return new Hashtable();
         }

     }



    public static boolean GetURLFile(String urlString, String localFileName, String proxyServer, int proxyPort) {
        BufferedInputStream fileStream = null;
        RandomAccessFile outFile = null;

        try {
            URL theUrl;

            if ((proxyServer.length() > 0) && (proxyPort > 0)) {
                // use HTTP proxy, even for FTP downloads
                theUrl = new URL("http", proxyServer, proxyPort, urlString);
            } else {
                theUrl = new URL(urlString);
            }


            System.out.println("Attempting to connect to " + theUrl);

            // go get the file
            URLConnection con = theUrl.openConnection();

            // if we were able to connect (we would have errored out
            // by now if not), try to get the file.
            // Use a BufferedInputStream instead of a BufferedReader,
            // because a BufferedReader won't retrieve non-text files
            // properly
            fileStream = new BufferedInputStream(con.getInputStream());

            // if we got the remote file, create the local file that
            // we can write the information to
            outFile = new RandomAccessFile(localFileName, "rw");

            System.out.println("Downloading to local file " + localFileName);

            // write to the file in bytes (in case it's not text)
            int howManyBytes;
            byte[] bytesIn = new byte[4096];
            while ((howManyBytes = fileStream.read(bytesIn)) >= 0) {
                outFile.write(bytesIn, 0, howManyBytes);
                //stringBuf.append(bytesIn, 0, howManyBytes); // to send to a StringBuffer
                //System.out.write(bytesIn, 0, howManyBytes);  // to send to the console
            }

            // close up the streams
            fileStream.close();
            outFile.close();

            System.out.println("Finished downloading file to " + localFileName);
            return true;


        } catch (MalformedURLException e) {
            System.out.println("ERROR: Invalid URL: " + urlString);
        } catch (NoRouteToHostException e) {
            System.out.println("ERROR: URL cannot be reached: " + urlString);
        } catch (ConnectException e) {
            System.out.println("ERROR: Connection error: " + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException. Fichero/Path no se encuentra en origen/destino.");
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println(e.getMessage());
        } finally {
            // make sure the streams got closed, in case of an error
            try {
                fileStream.close();
                outFile.close();
            } catch (Exception e) {
            }
        }

        //if we got here, there was some kind of error
        return false;

    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


    public static File showSaveFileDialog(boolean b, ResourceBundle messages, JFrame desktop, String formato){
        if (!b) return null;
        JFileChooser chooser = new JFileChooser();
        com.geopista.app.utilidades.GeoPistaFileFilter filter= new com.geopista.app.utilidades.GeoPistaFileFilter();

        if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoPDF){
            filter.addExtension("pdf");
            filter.setDescription(messages.getString("jMenuItemActividadesInformes.JFileChooser.filter.setDescription") + " PDF");
            chooser.setFileFilter(filter);
        }else if (new Integer(formato).intValue() == com.geopista.app.contaminantes.Constantes.formatoTEXTO){
            filter.addExtension("txt");
            filter.setDescription(messages.getString("jMenuItemActividadesInformes.JFileChooser.filter.setDescription") + " TXT");
            chooser.setFileFilter(filter);
        }

       /** Permite multiples selecciones */
       chooser.setMultiSelectionEnabled(false);

       int returnVal = chooser.showSaveDialog(desktop);

       if (returnVal == JFileChooser.APPROVE_OPTION) {
           File selectedFile= chooser.getSelectedFile();
           return selectedFile;
       }
       return null;

    }

    public static boolean isWindows(){
           String osName = System.getProperty ("os.name");
           osName = osName.toLowerCase();
           return osName.indexOf ("windows") != -1;
    }	//	isWindows


    public static void generarFichaHistoricoMenu(JFrame frame, Vector vHist, File selectedFile,
                                                 ResourceBundle literales){
        try{
            // Eliminamos la extension, si la tiene
            String fileName= selectedFile.getName();
            String path= selectedFile.getAbsolutePath();
            int index= path.indexOf(fileName);
            if (index != -1){
                path= path.substring(0, index);
            }

            // Siempre sera un fichero .txt
            index= fileName.indexOf(".");
            if (index != -1){
                String name= fileName.substring(0, index);
                name+= ".txt";
                selectedFile= new File(path+name);
            }else{
                // Annadimos la extension .txt
                fileName+= ".txt";
                selectedFile= new File(path+fileName);
            }
            // Si ya existe, borramos el fichero de datos
            if (selectedFile.exists()) {
              selectedFile.delete();
            }

            // creamos el fichero de datos delimitado por comas
            Writer stream= new FileWriter(selectedFile, true);

            /** Generamos el listado */

            /** Generamos la cabecera */
            String cabecera= literales.getString("JFrameHistorico.titulo1");
            stream.write(cabecera);
            stream.write(10);
            stream.write(10);
            String columnas= literales.getString("JFrameHistorico.columna1") + ", " +
                    literales.getString("JFrameHistorico.columna2") + ", " +
                    literales.getString("JFrameHistorico.columna3") + ", " +
                    literales.getString("JFrameHistorico.columna4") + ", " +
                    literales.getString("JFrameHistorico.columna5") + ", " +
                    literales.getString("JFrameHistorico.columna6") + ", " +
                    literales.getString("JFrameHistorico.columna7") + ", " +
                    literales.getString("JFrameHistorico.columna8");

            stream.write(columnas);
            stream.write(10);

            for (Enumeration e= vHist.elements(); e.hasMoreElements();){
                try{
			        Historico aux =(Historico) e.nextElement();
                    SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
                    String sFecha =df.format(aux.getFecha());
                    String sTipo="";
                    DomainNode nodeTipo=com.geopista.app.contaminantes.Estructuras.getListaTipoMedioambiental().getDomainNode(new Integer(aux.getTipo_medioambiental()).toString());
                    if (nodeTipo!=null)
                      sTipo=nodeTipo.getTerm(com.geopista.app.contaminantes.Constantes.Locale);
                    String sAccion="";
                    DomainNode nodeAccion=com.geopista.app.contaminantes.Estructuras.getListaTipoAccion().getDomainNode(new Integer(aux.getAccion()).toString());
                    if (nodeAccion!=null)
                       sAccion=nodeAccion.getTerm(com.geopista.app.contaminantes.Constantes.Locale);
                    String datos= sFecha + ", " +
                                  aux.getNombre_Usuario() + ", " +
                                  sTipo + ", " +
                                  sAccion + ", " +
                                  (aux.getApunte()==null?"":aux.getApunte()) + ", " +
                                  ((aux.getSistema()==1)?"1":"0") + ", " +
                                  aux.getId_elemento() + ", " +
                                  aux.getId_historico();
                    stream.write(datos);
                    // salto de linea
                    stream.write(10);
                }catch (Exception ex){}
            }

            stream.write(10);
            stream.close();
            JOptionPane.showMessageDialog(frame, literales.getString("JFrameHistorico.mensaje2"));

        }catch(Exception e){
            JOptionPane.showMessageDialog(frame, literales.getString("JFrameHistorico.mensaje1"));

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
        }


    }

    public static boolean bajarInformeFiles(CPlantilla p, String rutaInforme){
        String path = "";
        String url = "";
        String pathDestino = "";
        String name = "";

        try {

            path = p.getPath();
            name = p.getFileName();

            String localPath = UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH, true);
            pathDestino = localPath + UserPreferenceConstants.REPORT_DIR_NAME + File.separator + rutaInforme;
            
            /* Comprobamos que el path de las plantillas y param.xml exista */
            if (!new File(pathDestino).exists()) {
                new File(pathDestino).mkdirs();
            }

            /** bajamos la plantilla */
            
            pathDestino = pathDestino + name;
            
            url = com.geopista.protocol.CConstantesComando.servidorUrl + path + name;
            
            return CUtilidades.GetURLFile(url, pathDestino, "", 0);

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }

    }




}
