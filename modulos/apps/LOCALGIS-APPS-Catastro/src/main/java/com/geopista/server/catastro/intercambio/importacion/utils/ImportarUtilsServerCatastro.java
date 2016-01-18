/**
 * ImportarUtilsServerCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.utils;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.SAXParser;
import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.intercambio.importacion.utils.ErrorHandlerImpl;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.FindTagXMLHandler;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ErrorXML;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.editor.GeopistaEditor;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACLayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.catastro.intercambio.importacion.xml.handlers.ConsultaCatastroServerRequestXMLHandler;
import com.geopista.util.GeopistaCommonUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImportarUtilsServerCatastro
{
    public static final String HTML_ROJO="<p><font face=SansSerif size=3 color=#ff0000><b>";
    public static final String HTML_VERDE="<p><font face=SansSerif size=3 color=#009900><b>";
    public static final String HTML_NUEVO_PARRAFO="<p><font face=SansSerif size=3>";
    public static final String HTML_FIN_PARRAFO="</b></font></p>";
    public static final String HTML_SALTO="<BR>";
    public static final boolean BORDERS_OFF = false;
    public static final boolean BORDERS_ON = true; 
    
    public final static String LAST_IMPORT_DIRECTORY = "lastImportDirectory";
    public final static String FILE_TO_IMPORT ="fileToImport";
    public final static String FILE_TYPE ="fileType";
    public static final String FILE_TXT_MULTILINE = "txtMultiline";
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    
    
    public static Boolean res =null;
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value Valor a mostrar
     * @return String con la cadena HTML
     */
    public static String getStringHtmlFormattedText (String field, String value)
    {
        return getStringHtmlFormattedText(field, value).toString();
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value Valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, String value)
    {
        StringBuffer sb = new StringBuffer();        
        sb.append("<p><b>").append(field).append(": </b>").append(value).append("</p>");
        return sb;
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value int con el valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, int value)
    {
        return getStringBufferHtmlFormattedText(field, new Integer(value).toString());
    }
    
    /**
     * Formatea una cadena en HTML en forma "clave: valor", con la clave en negrita
     * 
     * @param field Texto a resaltar en negrita (nombre del campo)
     * @param value long con el valor a mostrar
     * @return StringBuffer con la cadena HTML
     */
    public static StringBuffer getStringBufferHtmlFormattedText (String field, long value)
    {
        return getStringBufferHtmlFormattedText(field, new Long(value).toString());
    }
    /**
     * Recupera la fecha actual
     * @param frm Patrón de generación de la fecha
     * @return Fecha actual en formato String
     */
    public static String getDate(String frm)
    {
        return (String) new SimpleDateFormat(frm).format(new Date());
        
    }
    /**
     * Recupera la fecha actual con patrón "dd-MMM-yyyy HH:mm:ss"
     * @return Fecha actual en formato String
     */
    public static String getDate()
    {
        return getDate("dd-MMM-yyyy HH:mm:ss");
    }
    
    /**
     * Resalta un texto, creando un nuevo párrafo en negrita y verde, formateado en HTML
     * 
     * @param text Texto a resaltar
     * @return String con el texto resaltado en HTML
     */
    public static String getEnhancedInformationMessage(String text)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(HTML_SALTO).append(HTML_VERDE).append(text).append(HTML_FIN_PARRAFO);
        
        return sb.toString();
    }
    
    
    /**
     * Método para determinar si un fichero de texto tiene el tamaño esperado
     * 
     * @param String path Ruta del fichero a comprobar que sea correcto
     * @param int size Tamaño esperado de cada registro del fichero
     * @return boolean true si es correcto, false en caso contrario
     */
    
    public static boolean testFileSize(String path, int size)
    {
        boolean hasExpectedLength = true;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                if (str.length() != size)
                {
                    hasExpectedLength = false;
                    break;
                }
            }
            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return hasExpectedLength;
        }
        return hasExpectedLength;
    }
    
    /**
     * Devuelve una lista de filas erroneas dentro de un fichero de tipo texto
     * 
     * @param path Ruta donde se encuentra el fichero a comprobar
     * @param lstTypes Lista con los posibles comienzos de registro
     * @param 
     * @return ArrayList con la lista de índices de los registros erroneos
     * 
     */
    public static ArrayList getErroneousRows(String path, ArrayList lstTypes, boolean includeBorders)
    {
        ArrayList lst = new ArrayList();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            int rowIndex = 0;
            while ((str = reader.readLine()) != null)
            {
                rowIndex++;
                
                if (!lstTypes.contains(str.substring(0,2)))
                {
                    lst.add(new Integer(rowIndex));
                }
            }
            
            //Si en la lista de tipos no se han incluido los tipos de la cabecera y de la cola, se
            //eliminan de la lista de erroneos
            if (!includeBorders)
            {
                lst.remove(new Integer (rowIndex));
                lst.remove(new Integer(1));
            }
            
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return lst;
    }
    
    /**
     * Comprueba si un fichero de texto tiene cabecera válida
     * 
     * @param path Fichero a comprobar
     * @param headerType Cadena de inicio del registro de cabecera
     * @return True si el fichero tiene cabecera válida
     */
    public static boolean testHeader(String path, String headerType)
    {
        boolean hasHeader = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            
            if ((str = reader.readLine()) != null)
            {   
                if (str.startsWith(headerType))
                {
                    hasHeader = true;
                }
            }
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return hasHeader;
        
    }
    
    /**
     * Comprueba si un fichero de texto tiene cola válida
     * 
     * @param path Fichero a comprobar
     * @param tailType Cadena de inicio del registro de cola
     * @return True si el fichero tiene cola válida
     */
    public static boolean testTail(String path, String tailType)
    {
        boolean hasTail = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            
            //al recorrer todo el fichero, hasTail tomará el resultado de la comprobación
            //para el último registro
            while ((str = reader.readLine()) != null)
            {   
                if (str.startsWith(tailType))
                {
                    hasTail = true;
                }
            }
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return hasTail;    
    }    
    
    /**
     * Comprueba si un XML cumple con el esquema XSD que incluye
     * 
     * @param xmlFilePath Ruta del fichero XML a validar
     * @return true si se valida correctamente
     */
    public static boolean validateXMLwithXSD(String xmlFilePath)
    {
        return validateXMLwithXSD(xmlFilePath, null);
    }
    
    /**
     * Comprueba si un XML cumple con el esquema XSD que incluye y si su elemento
     * raíz concuerda con el esperado
     * 
     * @param xmlFilePath Ruta del fichero XML a validar
     * @param type Tipo del elemento raíz del documento
     * @return true si se valida correctamente
     */
    public static boolean validateXMLwithXSD(String xmlFilePath, String type)
    {
        boolean res = false;
        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(
                "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
        "http://www.w3.org/2001/XMLSchema");
        
//        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", 
//        		new File("D:\\Trabajos\\Geocatastro\\Servicios web\\ConsultaCatastroResponse.xsd"));
        
        
        try {
            DocumentBuilder builder =
                factory.newDocumentBuilder();
            builder.setErrorHandler(new ImportErrorHandlerServerCatastro());
            Document document = builder.parse(new File(xmlFilePath));
            
            //Se comprueba si el tipo de fichero xml es el esperado
            if (type!=null)  
            {
                if (document.getFirstChild().getLocalName().equals(type))
                    res = true;  
            }else{            
                res = true;
            }           
            
        } catch (SAXException sxe) {
            
            Exception  x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
            
        } catch (ParserConfigurationException pce) {
            
            pce.printStackTrace();
            
        } catch (IOException ioe) {
            
            ioe.printStackTrace();
        }
        
        return res;
    }
    
    public static boolean validateSAXStringWithXSD(String xmlInput, String type)
    {
        boolean res = true;
        
        XMLReader parser = new SAXParser();
        
        try
        {
        	File xmlTemp = File.createTempFile("responseXML_","_temp");
            
            FileOutputStream outXML = new FileOutputStream(xmlTemp.getAbsolutePath());
            outXML.write(xmlInput.getBytes());
            outXML.flush();
            outXML.close();
        	
            ArrayList lst = new ArrayList();
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            
            parser.setContentHandler(new FindTagXMLHandler(parser, lst, type));
            parser.setEntityResolver(new ResolverImplServerCatastro());
            parser.setErrorHandler(new ErrorHandlerImpl());
            parser.parse(xmlTemp.getAbsolutePath());
            if(lst.size()>0){
            	res = true;
            }
            else{
            	res = false;
            }
            //res = lst.size()>0?true:false;            
            
        } catch (SAXNotRecognizedException e)
        {  
            e.printStackTrace();
            res = false;
        } catch (SAXNotSupportedException e)
        {
            e.printStackTrace();
            res = false;
        }
        catch (IOException e)
        {  
            e.printStackTrace();
            res = false;
        } catch (SAXException e)
        {
            e.printStackTrace();
            res = false;
        }
        return res;
    }
    
    public static boolean validateSAXdocumentWithXSD(String xmlFilePath, String type)
    {
        boolean res = true;
        
        XMLReader parser = new SAXParser();
        
        try
        {
            ArrayList lst = new ArrayList();
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            
            parser.setContentHandler(new FindTagXMLHandler(parser, lst, type));
            parser.setEntityResolver(new ResolverImplServerCatastro());
            parser.setErrorHandler(new ErrorHandlerImpl());
            parser.parse(xmlFilePath);
            if(lst.size()>0){
            	res = true;
            }
            else{
            	res = false;
            }          
            
        } catch (SAXNotRecognizedException e)
        {  
            e.printStackTrace();
            res = false;
        } catch (SAXNotSupportedException e)
        {
            e.printStackTrace();
            res = false;
        }
        catch (IOException e)
        {  
            e.printStackTrace();
            res = false;
        } catch (SAXException e)
        {
            e.printStackTrace();
            res = false;
        }
        return res;
    }
    
    public static String validateLowSAXdocumentWithXSD(String xmlFilePath, String type)
    {
        String res = "";
        
        XMLReader parser = new SAXParser();
        
        try
        {
            ArrayList lst = new ArrayList();
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            
            parser.setContentHandler(new FindTagXMLHandler(parser, lst, type));
            parser.setEntityResolver(new ResolverImplServerCatastro());
            parser.setErrorHandler(new ErrorHandlerImpl());
            parser.parse(xmlFilePath);
                   
            
        } catch (SAXNotRecognizedException e)
        {  
            res = e.getMessage();
            
        } catch (SAXNotSupportedException e)
        {
        	res = e.getMessage();
        }
        catch (IOException e)
        {  
        	res = e.getMessage();
        	
        } catch (SAXException e)
        {
        	res = e.getMessage();
        }
        return res;
    }
        
    
    /**
     * Comprueba si la cabecera de un fichero de texto verifica que la información que 
     * contiene es del tipo esperado 
     *  
     * @param path Ruta del fichero de texto a comprobar
     * @param type Tipo que se espera encontrar 
     * @param pos Posición en la que se espera encontrar el tipo
     * @return true si en la cabecera del fichero se encuentra el texto esperado en la posición
     * indicada
     */
    public static boolean testType(String path, String type, int pos)
    {
        boolean isType = false;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = null;
            
            //comprueba la cabecera
            if ((str = reader.readLine()) != null)
            {   
                if (str.substring(pos, pos+type.length()).equalsIgnoreCase(type))
                {
                    isType = true;
                }
            }
            reader.close();
            
        } catch (Exception e)
        {
            e.printStackTrace();          
        } 
        
        return isType;
    }
    
    
    
    
    /**
     * Convierte un InputStream en String
     * @param is
     * @return
     */
    //LCGIII.Desplazado al paquete LOCALGIS-Utils
    /*public String parseISToString(java.io.InputStream is){
        java.io.DataInputStream din = new java.io.DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try{
            String line = null;
            while((line=din.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch(Exception ex){
            ex.getMessage();
        }finally{
            try{
                is.close();
            }catch(Exception ex){}
        }
        return sb.toString();
    }*/
    
    /**
     * Convierte un String en in InputStream
     * @param xml
     * @return
     */
    public static java.io.InputStream parseStringToIS(String xml){
        if(xml==null) return null;
        xml = xml.trim();
        java.io.InputStream in = null;
        try{
            in = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
        }catch(Exception ex){
        }
        return in;
    }
    
    /**
     * Transforma de base64 a cadena de caracteres ASCII
     * @param base64 Cadena en base 64 binario
     * @return Cadena de caracteres ASCII equivalente a la cadena en base 64
     */
    public static String base64ToAscii (String base64)
    {    
        String ascii = null;
        byte[] bytes;
        try{
            bytes = new sun.misc.BASE64Decoder().decodeBuffer(base64);
            ascii = new String(bytes);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return ascii;
    }
   
    /**
     * Transforma una cadena ascii a base 64
     * @param ascii Cadena en ASCII a transformar
     * @return Cadena de caracteres en base 64 equivalente a la cadena ASCII
     */
    public static String asciiToBase64 (String ascii)
    {         
        return new sun.misc.BASE64Encoder().encode(ascii.getBytes());
    }
    
    
    public static void cargarCapas(ArrayList namesLayerFamilies, GeopistaEditor geopistaEditor){
    	
        String sUrlPrefix = AppContext.getApplicationContext().getString("geopista.conexion.servidor");
        AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(
                sUrlPrefix + "AdministradorCartografiaServlet");
		ACLayerFamily[] layersFamilies = (ACLayerFamily[])administradorCartografiaClient.getLayerFamilies();
		ArrayList layerFamiliesFinded = new ArrayList();
		
		 for (int indice = 0;indice<layersFamilies.length;indice++){
				ACLayerFamily layerFamily = layersFamilies[indice];
				for(Iterator iteratorNamesLF = namesLayerFamilies.iterator();iteratorNamesLF.hasNext();){
					if(layerFamily.getName().equalsIgnoreCase((String)iteratorNamesLF.next())){
						layerFamiliesFinded.add(layerFamily);
					}
				}
		 }
		 
		 ArrayList listaErrPerm = new ArrayList();
		 Iterator iteratorLayerFamiliesFinded = layerFamiliesFinded.iterator();
	      while(iteratorLayerFamiliesFinded.hasNext())
	        {
	          ACLayerFamily idLayerFamily = (ACLayerFamily) iteratorLayerFamiliesFinded.next();
	          try {
		          String[] layerIDs = administradorCartografiaClient.getLayerIDs(idLayerFamily.getId());
		          
		          for(int n=layerIDs.length-1; n>=0;n--)
		          {	              
		              boolean layerRepeated = false;
		              List currentLayers = geopistaEditor.getLayerManager().getLayers();
		              Iterator currentLayersIterator = currentLayers.iterator();
		              while (currentLayersIterator.hasNext())
		              {
		                  Layer currentLayer = (Layer) currentLayersIterator.next();
		                  if(currentLayer instanceof GeopistaLayer)
		                  {
		                      String currentSystemId = ((GeopistaLayer) currentLayer).getSystemId();
		                      if(currentSystemId.trim().equals(layerIDs[n]))
		                      {
		                          layerRepeated = true;
		                          break;
		                      }
		                  }
		              }
		              
		              if(layerRepeated) continue;
		              
		              IGeopistaLayer layer = null;
		              try
		              {	
		                GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		                Map properties = new HashMap();
		                properties.put("mapadestino",(GeopistaMap) geopistaEditor.getTask());
		                properties.put("nodofiltro",FilterLeaf.equal("1",new Integer(1)));
		                serverDataSource.setProperties(properties);
		                GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();
		                
		                Collection exceptions = new ArrayList();
		                //preparamos la url de la layer
		                URL urlLayer = new URL("geopistalayer://default/"+layerIDs[n]);                
		                geopistaConnection.executeQuery(urlLayer.toString(),exceptions,null);
		                
		                if(exceptions.size()>0)
		                {
		                	//En caso de que sea una excepcion de permisos, 
		                	Iterator recorreExcepcion = exceptions.iterator();
		                	while(recorreExcepcion.hasNext()){
		                		// Revisar el efecto del continue
		                		Exception e = (Exception)recorreExcepcion.next();
		                    	if(e.getCause().getLocalizedMessage().toString().equals("PermissionException: Geopista.Layer.Leer")){
		                    		listaErrPerm.add(e);                   		
		                    	}
		                    	else{
		                    		JOptionPane.showMessageDialog((Component) geopistaEditor.getGuiComponent(),AppContext.getApplicationContext().getI18nString("LoadSystemLayers.CapaErronea"));
		                    	}         	
		                	}
		            		continue;                  	
		                }
		                layer = geopistaConnection.getLayer();   
		                DataSourceQuery dataSourceQuery = new DataSourceQuery();
		                dataSourceQuery.setQuery(urlLayer.toString());
		                dataSourceQuery.setDataSource(serverDataSource);
		                layer.setDataSourceQuery(dataSourceQuery);    
		               
		              }catch(Exception e)
		              {
		                  JOptionPane.showMessageDialog((Component) geopistaEditor.getGuiComponent(),AppContext.getApplicationContext().getI18nString("LoadSystemLayers.CapaErronea"));
		                  continue;
		              }
		              
		                if(layer!=null&&!layer.getSystemId().equalsIgnoreCase("error"))
		                {	                	
		                  geopistaEditor.getLayerManager().addLayer(idLayerFamily.getName(),layer);
		                  ((LayerFamily) geopistaEditor.getLayerManager().getCategory(idLayerFamily.getName())).setSystemLayerFamily(true);
		                  ((LayerFamily) geopistaEditor.getLayerManager().getCategory(idLayerFamily.getName())).setSystemId(String.valueOf(idLayerFamily.getId())); 
		                  layer.setVisible(false);
		                }
		                else{
		                		JOptionPane.showMessageDialog((Component) geopistaEditor.getGuiComponent(),AppContext.getApplicationContext().getI18nString("LoadSystemLayers.CapaErronea"));
		                }
		          }
		        
	    	} catch (ACException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    }


	public InputSource resolveEntity(String arg0, String arg1)
			throws SAXException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
    public void cargarResponseXML(InputStream responseString, TaskMonitorDialog progressDialog) 
    throws JDOMException, IOException, SAXException {
        
        //Guardar respuesta en un fichero temporal
        
        //Leer el fichero con consultaCatastroRequestXMLHandler
                
        File xmlTemp = File.createTempFile("responseXML_","_temp");
        
        FileOutputStream outXML = new FileOutputStream(xmlTemp.getAbsolutePath());
        int c = 0;
        while ( (c = responseString.read()) >= 0 )
            outXML.write(c);
        outXML.flush();
        outXML.close();
        
        if(!validateSAXdocumentWithXSD(xmlTemp.getAbsolutePath(),"ConsultaCatastroResponse")){        
            //Mostrar error         
            DataExceptionCatastro dataException = new DataExceptionCatastro(I18N.get("Importacion","importar.intercambio.ErroresValidacion"));                  
        }
        else{
            
            blackboard.put("Validacion", true);
            
            XMLReader parser = new SAXParser();                            
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            
            ArrayList instancias = new ArrayList ();
            parser.setContentHandler(new ConsultaCatastroServerRequestXMLHandler(parser, instancias, progressDialog));
            parser.parse(xmlTemp.getAbsolutePath());

            if(instancias.size()>0){
                
                Iterator it = instancias.iterator();

                StringBuffer errorString = new StringBuffer();
                while (it.hasNext()){
                    Object instancia = it.next();
                    if(instancia instanceof ErrorXML){
                        //Mostrar Error                 
                        errorString.append("[Error] : " + ((ErrorXML)instancia).getCodigo() + ": " + ((ErrorXML)instancia).getDescripcion() + "\n");
                    }
                }
                ErrorDialog.show(application.getMainFrame(), "ERROR", 
                        I18N.get("Importacion","importar.intercambio.ErroresRespuesta"), errorString.toString());
            }            
        }
    }
    public static String crearXMLConsultaCatastro(Expediente expediente, ArrayList lstReferencias){

    	StringBuffer finalXML = new StringBuffer();
    	StringBuffer xml = new StringBuffer();

    	xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	xml.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Header></soap:Header><soap:Body Id=\"MsgBody\">");
    	//finalXML.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>");

    	//finalXML.append("<ConsultaCatastroRequest NS1:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/ConsultaCatastroRequest.xsd\" xmlns=\"http://www.catastro.meh.es/\" xmlns:NS1=\"http://www.w3.org/2001/XMLSchema-instance\">");
    	//finalXML.append("<ConsultaCatastroRequest xmlns=\"http://www.catastro.meh.es/\">");
    	//finalXML.append("<ConsultaCatastroRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/ConsultaCatastroRequest.xsd\" xmlns=\"http://www.catastro.meh.es/\">");
    	finalXML.append("<ConsultaCatastroRequest xmlns=\"http://www.catastro.meh.es/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/consultacatastrorequest.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
    	//finalXML.append("<ConsultaCatastroRequest xmlns=\"http://www.catastro.meh.es/\" xsi:schemaLocation=\"D:\\Trabajos\\Geocatastro\\EsquemasNuevo\\consultacatastrorequest.xsd\">");
    	
    	finalXML.append("<control>");
    	finalXML.append("<ieg>");
    	if (expediente.getEntidadGeneradora().getTipo().length()>1){
    		finalXML.append("<teg>").append("</teg>");
    	}
    	else{
    		finalXML.append("<teg>").append(expediente.getEntidadGeneradora().getTipo()).append("</teg>");
    	}
    	finalXML.append("<ceg><engf>");
    	
    	if (new Long(expediente.getEntidadGeneradora().getIdEntidadGeneradora()).toString().length()>2){
    		finalXML.append("<cd>").append("</cd>");
    	}
    	else{
    		finalXML.append("<cd>").append(expediente.getEntidadGeneradora().getIdEntidadGeneradora()).append("</cd>");
    	}
    	if (expediente.getCodigoEntidadRegistroDGCOrigenAlteracion().toString().length()>3){
    		finalXML.append("<eng>").append("</eng>");
    	}
    	else{
    		finalXML.append("<eng>").append(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion().intValue()).append("</eng>");
    	}
    	finalXML.append("</engf></ceg>");
    	if (expediente.getEntidadGeneradora().getNombre().length()>27){
    		finalXML.append("<neg>").append("</neg>");
    	}
    	else{
    		finalXML.append("<neg>").append(expediente.getEntidadGeneradora().getNombre()).append("</neg>");
    	}
    	    	
    	finalXML.append("</ieg>");
    	
    	finalXML.append("<ifi>");  
    	
    	NumberFormat formatterFecha = new DecimalFormat("00");
    	finalXML.append("<ffi>").append(expediente.getFechaMovimiento().getYear()).append("-").append(formatterFecha.format(expediente.getFechaMovimiento().getMonth())).append("-").append(formatterFecha.format(expediente.getFechaMovimiento().getDay())).append("</ffi>");
    	finalXML.append("<hfi>").append(expediente.getHoraMovimiento()).append("</hfi>");
    	finalXML.append("<tfi>WTCE</tfi>");
    	finalXML.append("</ifi>");

    	finalXML.append("<sol>");
    	if(expediente.getNifPresentador().length()>9){
    		finalXML.append("<nif>").append("</nif>");
    	}
    	else {    		
    		finalXML.append("<nif>").append(GeopistaCommonUtils.completarConCeros(expediente.getNifPresentador(),9)).append("</nif>");
    	}
    	if ( expediente.getNombreCompletoPresentador().length()>60 || expediente.getNombreCompletoPresentador().trim().length()==0 ){
    		finalXML.append("<nom>").append("</nom>");
    	}
    	else{
    		finalXML.append("<nom>").append(expediente.getNombreCompletoPresentador().toUpperCase()).append("</nom>");
    	}
    	finalXML.append("</sol>");
    	
    	finalXML.append("</control>");
    	
    	finalXML.append("<pregunta>");
    	finalXML.append("<exp>");
    	finalXML.append("<tint>R</tint>");
    	finalXML.append("<texp>").append(expediente.getTipoExpediente().getCodigoTipoExpediente()).append("</texp>");
    	finalXML.append("<expg>");
    	finalXML.append("<aexpg>").append(expediente.getAnnoExpedienteGerencia()).append("</aexpg>");
    	finalXML.append("<rexpg>").append(expediente.getReferenciaExpedienteAdminOrigen()).append("</rexpg>");
    	finalXML.append("<ero>").append(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()).append("</ero>");
    	finalXML.append("</expg>");
    	finalXML.append("<expec>");
    	finalXML.append("<aexpec>").append(expediente.getAnnoExpedienteAdminOrigenAlteracion()).append("</aexpec>");
    	finalXML.append("<rexpec>").append(expediente.getReferenciaExpedienteAdminOrigen()).append("</rexpec>");
    	finalXML.append("<eoa>").append(expediente.getCodigoEntidadRegistroDGCOrigenAlteracion()).append("</eoa>");
    	finalXML.append("</expec>");
    	finalXML.append("<fre>").append(expediente.getFechaRegistro().getYear()).append("-").append(formatterFecha.format(expediente.getFechaRegistro().getMonth())).append("-").append(formatterFecha.format(expediente.getFechaRegistro().getDay())).append("</fre>");    	
    	finalXML.append("<inr>");
    	
    	finalXML.append("<npn>");
    	finalXML.append("<not>");
    	finalXML.append("<cp>").append(expediente.getCodProvinciaNotaria()).append("</cp>");
    	finalXML.append("<cpb>").append(expediente.getCodPoblacionNotaria()).append("</cpb>");
    	finalXML.append("<cnt>").append(expediente.getCodNotaria()).append("</cnt>");
    	finalXML.append("</not>");
    	finalXML.append("<pn>");
    	finalXML.append("<aprt>").append(expediente.getAnnoProtocoloNotarial()).append("</aprt>");
    	finalXML.append("<prt>").append(expediente.getProtocoloNotarial()).append("</prt>");
    	finalXML.append("</pn>");
    	finalXML.append("</npn>");
    	
    	finalXML.append("</inr>");
    	finalXML.append("<decl>");
    	finalXML.append("<doco>").append(expediente.getTipoDocumentoOrigenAlteracion()).append("</doco>");
    	finalXML.append("<idoco>").append(expediente.getDescripcionAlteracion()).append("</idoco>");
    	finalXML.append("<nbi>");
    	finalXML.append("<nbu>").append(expediente.getNumBienesInmueblesUrbanos()).append("</nbu>");
    	finalXML.append("<nbr>").append(expediente.getNumBienesInmueblesRusticos()).append("</nbr>");
    	finalXML.append("<nbce>").append(expediente.getNumBienesInmueblesCaractEsp()).append("</nbce>");
    	finalXML.append("</nbi>");
    	finalXML.append("</decl>");
    	finalXML.append("<cdeac>").append(expediente.getCodigoDescriptivoAlteracion()).append("</cdeac>");
    	finalXML.append("<deac>").append(expediente.getDescripcionAlteracion()).append("</deac>");
    	finalXML.append("<dec>");
    	finalXML.append("<idp>");
    	finalXML.append("<nif>").append(expediente.getNifPresentador()).append("</nif>");
    	finalXML.append("<nom>").append(expediente.getNombreCompletoPresentador()).append("</nom>");
    	finalXML.append("</idp>");
    	finalXML.append("<dfn>");
    	finalXML.append("<loine>");
    	finalXML.append("<cp>").append(expediente.getDireccionPresentador().getProvinciaINE()).append("</cp>");
    	finalXML.append("<cm>").append(expediente.getDireccionPresentador().getMunicipioINE()).append("</cm>");
    	finalXML.append("</loine>");
    	finalXML.append("<cmc>").append(expediente.getDireccionPresentador().getCodigoMunicipioDGC()).append("</cmc>");
    	finalXML.append("<np>").append(expediente.getDireccionPresentador().getNombreProvincia()).append("</np>");
    	finalXML.append("<nm>").append(expediente.getDireccionPresentador().getNombreMunicipio()).append("</nm>");
    	finalXML.append("<nem>").append(expediente.getDireccionPresentador().getNombreEntidadMenor()).append("</nem>");
    	finalXML.append("<dir>");
    	finalXML.append("<cv>").append(expediente.getDireccionPresentador().getCodigoVia()).append("</cv>");
    	finalXML.append("<tv>").append(expediente.getDireccionPresentador().getTipoVia()).append("</tv>");
    	finalXML.append("<nv>").append(expediente.getDireccionPresentador().getNombreVia()).append("</nv>");
    	finalXML.append("<pnp>").append(expediente.getDireccionPresentador().getPrimerNumero()).append("</pnp>");
    	finalXML.append("<plp>").append(expediente.getDireccionPresentador().getPrimeraLetra()).append("</plp>");
    	finalXML.append("<snp>").append(expediente.getDireccionPresentador().getSegundoNumero()).append("</snp>");
    	finalXML.append("<slp>").append(expediente.getDireccionPresentador().getSegundaLetra()).append("</slp>");
    	
    	NumberFormat formatterCodigo = new DecimalFormat("000.00");
    	finalXML.append("<km>").append(formatterCodigo.format(expediente.getDireccionPresentador().getKilometro()).replace('.',',')).append("</km>");
    	finalXML.append("<td>").append(expediente.getDireccionPresentador().getDireccionNoEstructurada()).append("</td>");
    	finalXML.append("</dir>");
    	finalXML.append("<loint>");
    	finalXML.append("<bq>").append(expediente.getDireccionPresentador().getBloque()).append("</bq>");
    	finalXML.append("<es>").append(expediente.getDireccionPresentador().getEscalera()).append("</es>");
    	finalXML.append("<pt>").append(expediente.getDireccionPresentador().getPlanta()).append("</pt>");
    	finalXML.append("<pu>").append(expediente.getDireccionPresentador().getPuerta()).append("</pu>");
    	finalXML.append("</loint>");
    	finalXML.append("<pos>");
    	finalXML.append("<dp>").append(expediente.getDireccionPresentador().getCodigoPostal()).append("</dp>");
    	finalXML.append("<ac>").append(expediente.getDireccionPresentador().getApartadoCorreos()).append("</ac>");
    	finalXML.append("</pos>");
    	finalXML.append("</dfn>");
    	finalXML.append("</dec>");
    	finalXML.append("</exp>");

    	for(Iterator iterReferencia=lstReferencias.iterator();iterReferencia.hasNext();){

    		Object object = iterReferencia.next();
    		
    		if (object instanceof FincaCatastro){    			

    			FincaCatastro parcela = (FincaCatastro)object;

    			finalXML.append("<finca>");
    			finalXML.append("<locat>");
    			finalXML.append("<cd>").append(parcela.getCodDelegacionMEH()).append("</cd>");
    			finalXML.append("<cmc>").append(parcela.getCodMunicipioDGC()).append("</cmc>");
    			finalXML.append("</locat>");
    			finalXML.append("<idf>");
    			finalXML.append("<cn>BI</cn>");
    			finalXML.append("<pc>");
    			finalXML.append("<pc1>").append(parcela.getRefFinca().getRefCatastral1()).append("</pc1>");
    			finalXML.append("<pc2>").append(parcela.getRefFinca().getRefCatastral2()).append("</pc2>");
    			finalXML.append("</pc>");
    			finalXML.append("</idf>");
    			finalXML.append("</finca>");
    			
    		}
    		else if (object instanceof BienInmuebleCatastro){
    			
    			BienInmuebleCatastro bienInmueble = (BienInmuebleCatastro)object;
    			finalXML.append("<finca>");
    			finalXML.append("<locat>");
    			finalXML.append("<idbi>");
    			finalXML.append("<cn>").append(bienInmueble.getClaseBienInmueble()).append("</cn>");    			
    			finalXML.append("<rc>");
    			finalXML.append("<pc1>").append(bienInmueble.getIdBienInmueble().getParcelaCatastral().getRefCatastral1()).append("</pc1>");
    			finalXML.append("<pc2>").append(bienInmueble.getIdBienInmueble().getParcelaCatastral().getRefCatastral2()).append("</pc2>");
    			finalXML.append("<car>").append(bienInmueble.getIdBienInmueble().getNumCargo()).append("</car>");
    			finalXML.append("<cc1>").append(bienInmueble.getIdBienInmueble().getDigControl1()).append("</cc1>");
    			finalXML.append("<cc2>").append(bienInmueble.getIdBienInmueble().getDigControl2()).append("</cc2>");
    			finalXML.append("</rc>");
    			finalXML.append("</idbi>");
    			finalXML.append("</locat>");
    			finalXML.append("</finca>");
    		}
    	}

    	finalXML.append("</pregunta>");
    	finalXML.append("</ConsultaCatastroRequest>");
    	
    	StringBuffer xmlValidate = new StringBuffer();
    	xmlValidate.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	xmlValidate.append(finalXML.toString());
    	
    	if (validateSAXStringWithXSD(xmlValidate.toString(), "ConsultaCatastroRequest")){
    		xml.append(finalXML);
        	xml.append("</soap:Body></soap:Envelope>");
        	return xml.toString();
    	}
    	    	
    	return  null;
    }
}

	
