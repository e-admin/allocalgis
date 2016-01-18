/**
 * GetFeatureInfoProxy.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.localgis.web.servlets;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.server.database.COperacionesAdministrador;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.implementations.AlfrescoManagerImpl;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.model.LocalgisAttributeTranslated;
import com.localgis.web.core.model.LocalgisAttributeValueTranslated;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.filters.LoginFilter;
import com.localgis.web.util.LayerUtils;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;



class ComparatorAttributeAlias implements Comparator
{

	private String locale;



	public ComparatorAttributeAlias(String locale)
	{
		this.locale=locale;
	}



	public int compare(Object obj1, Object obj2)
	{
		String str1=(String) obj1;
		String str2=(String) obj2;
		final Collator collator=Collator.getInstance(new Locale(locale));
		return collator.compare(str1,str2);
	}



	public boolean equals(Object obj)
	{
		return super.equals(obj);
	}
}



public class GetFeatureInfoProxy extends HttpServlet
{

	private static final Logger logger=Logger.getLogger(GetFeatureInfoProxy.class);

	private static String ERROR_PAGE="/empty.jsp";

	private static int TIMEOUT_WFS_SERVER=20000;

	private static String QUERY_LAYERS_PARAM="QUERY_LAYERS";
	private static String ACCION_PARAM="accion";
	private static String CAPAS_ACTIVAS_PARAM="capasactivas";



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request,response);
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//System.out.println("GetFeatureInfoProxy.doPost");
		// Obtenemos los parametros
		String layerName=null;
		String urlWFSServer=request.getParameter("urlWFSServer");
		String bbox=request.getParameter("bbox");
		String language=request.getParameter("language");
		String accion=request.getParameter("accion");
		String locale=null;
		
		
		String capasActivas=request.getParameter("capasActivas");
		String x=request.getParameter("x");
		String y=request.getParameter("y");
		
		
		//Fijamos el srid en sesion
        String srid=null;
        String idEntidad=null;
        Integer idMap=null;
        try {
			srid=(String)request.getSession().getAttribute("srid");		
			if (srid==null)
				srid=Configuration.getPropertyString(Configuration.PROPERTY_DISPLAYPROJECTION);
			idEntidad=String.valueOf((Integer)request.getSession().getAttribute("idEntidad"));		
			locale=String.valueOf(request.getSession().getAttribute("locale"));		
			
			idMap=(Integer)request.getSession().getAttribute("idMap");
			
			if (idMap==null)
				logger.error("El identificador de mapa es nulo al realizar el GetFeatureInfo");
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        LocalgisMapManager localgisMapManager;
		try {
			localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
		} catch (Exception e1) {
			return;
		}
		
        ArrayList listaCompletaCapas=new ArrayList();
        HashMap mapCapasInArea=new HashMap();
        //Revisamos las capas activas
        if ((capasActivas!=null) && (!capasActivas.equals(""))){
			String[] capasActivas2=capasActivas.split(",");
			
			for (int i=0;i<capasActivas2.length;i++){
				String capa=capasActivas2[i];
				
				if (Integer.parseInt(capa)<0)
					continue;
				
			      
				
				LocalgisLayerExt localgisLayer=null;
				try {
					
					localgisLayer=localgisMapManager.getLayerById(Integer.parseInt(capa),idMap,locale);
					//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
					//LocalgisLayerDAO localgisLayerDAO=(LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
					//localgisLayer = localgisLayerDAO.selectLayerById(Integer.parseInt(capa));
					
					if (localgisLayer==null)
						continue;
					//Hay determinadas capas que no tiene mucho sentido sacarlas
					//TTMM (Municipios)	solo las sacamos cuando se piden explicitamente	
					if (capasActivas2.length>1){
						if ((localgisLayer.getLayername().equals("TTMM") ||
								(localgisLayer.getLayername().equals("parroquias")) ||
									(localgisLayer.getLayername().equals("municipios")) ||
										(localgisLayer.getLayername().equals("NP"))))
							continue;
					}					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				String[] datosCapa;
			    datosCapa = LayerUtils.getTableNameFromLayer(localgisLayer);
			    datosCapa[2]=localgisLayer.getLayername();
			    datosCapa[3]=String.valueOf(localgisLayer.getLayerid());
			    datosCapa[4]=String.valueOf(localgisLayer.getNametranslated());
				//System.out.println("DatosCapa:"+datosCapa);
				listaCompletaCapas.add(datosCapa);
			}
        
       
			//mapCapasInArea=null;
			if (listaCompletaCapas.size()>0){
		        String[] bbox3dFields=bbox.split(",");
		        String bbox3d=bbox3dFields[0]+" "+bbox3dFields[1]+","+bbox3dFields[2]+" "+bbox3dFields[3];
		        List capasInArea;
				try {
					capasInArea = localgisMapManager.getLayersInArea(listaCompletaCapas,srid,bbox3d);
					if (capasInArea!=null)
				        for (int i=0;i<capasInArea.size();i++){
				        	mapCapasInArea.put(capasInArea.get(i),capasInArea.get(i));
				        }
				} catch (Exception e) {
					logger.error("No se han podido recuperar las capas dentro del area solicitada. Se asumen todas.");
					mapCapasInArea=null;
				}
		        
		        
			}
        }
		
		byte[] responseBody=null;
		Document document=null;
		
		int total=0;
		
		if (listaCompletaCapas.size()==0)
			total=1;
		else
			total=listaCompletaCapas.size();
		
		
		boolean existenFeatures=false;
		
		for (int i=0;i<total;i++){
			// Hacemos la peticion
			HttpClient httpClient=new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_WFS_SERVER);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_WFS_SERVER);
						
			//Sustituimos la urldel WFS Server para que sea especifico por capa.
			logger.info("Realizando peticion GetFeatureInfoProxy Original: "+urlWFSServer);
			
			
			String idLayer=null;
			String nameTranslated=null;
			if (listaCompletaCapas.size()>0){
				
				
				
				String[] datosCapa=(String[])listaCompletaCapas.get(i);
				
				urlWFSServer=urlWFSServer.replaceAll("LAYERS=\\w+&","LAYERS="+datosCapa[2]+"&");
				urlWFSServer=urlWFSServer.replaceAll("QUERY_LAYERS=\\w+&","QUERY_LAYERS="+datosCapa[2]+"&");
				
				if (mapCapasInArea!=null){
					if (!mapCapasInArea.containsKey(datosCapa[2]))
						continue;
				}
				
				idLayer=datosCapa[3];
				nameTranslated=datosCapa[4];
				logger.info("Realizando peticion GetFeatureInfoProxy Modificada: "+urlWFSServer);
				
			}
			
			HttpMethod httpMethod=new GetMethod(urlWFSServer);
			
			
			
	
			//urlWFSServer=urlWFSServer.replaceAll(", )
			
			try
			{
				int statusCode=httpClient.executeMethod(httpMethod);
				if (statusCode!=HttpStatus.SC_OK)
				{
					logger.error("Error al realizar la peticion a \""+urlWFSServer+"\". Codigo de la respuesta = ["+statusCode+"]");
					request.setAttribute("errorMessageKey","getFeatureInfo.error.badStatusCode");
					request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
					return;
				}
				responseBody=httpMethod.getResponseBody();
			}
			catch (HttpException e)
			{
				logger.error("Error al realizar la peticion a \""+urlWFSServer+"\"",e);
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
			catch (IOException e)
			{
				logger.error("Error de E/S al realizar la peticion a \""+urlWFSServer+"\"",e);
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
			finally
			{
				httpMethod.releaseConnection();
			}
			logger.info("Peticion realizada GetFeatureInfoProxy");
			/*
			 * Obtenemos el contenido, que debería ser algo de esta forma:
			 * 
			 * <?xml version="1.0" encoding="ISO-8859-1"?> <msGMLOutput
			 * xmlns:gml="http://www.opengis.net/gml"
			 * xmlns:xlink="http://www.w3.org/1999/xlink"
			 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			 * <parcelas_layer> <parcelas_feature> <gml:boundedBy> <gml:Box
			 * srsName="EPSG:XXXX"> <gml:coordinates>XXXX</gml:coordinates>
			 * </gml:Box> </gml:boundedBy> <id_localgis>XXXX</id_localgis>
			 * <id>XXXXX</id> <referencia_catastral>XXXX</referencia_catastral>
			 * <id_municipio>XXXX</id_municipio>
			 * <GEOMETRY>POLYGON((XXXX))</GEOMETRY> </parcelas_feature>
			 * </parcelas_layer> </msGMLOutput>
			 */
			ByteArrayInputStream inputStream=new ByteArrayInputStream(responseBody);
			boolean isXMLResponse=true;
			
			try
			{
				//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				//DocumentBuilder builder = factory.newDocumentBuilder();
				//document = builder.parse(inputStream);
				
				//Se cambia la forma de parsear porque las capabilites de Catastro tarda mucho en
				//parsearse.
				DOMParser parser = new DOMParser();
				parser.setFeature( "http://xml.org/sax/features/validation", false );
				parser.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
				parser.parse( new InputSource(inputStream));
				document = parser.getDocument();
				
				
				
				//document=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			}
			catch (SAXException e)
			{
				logger.error("Error al parsear la respuesta del map server",e);
				isXMLResponse=false;
			}
			catch (IOException e)
			{
				logger.error("Error de entrada salida al parsear la respuesta del map server",e);
				isXMLResponse=false;
			}
			/*catch (ParserConfigurationException e)
			{
				logger.error("Error de configuracion en el parser de la respuesta del map server",e);
				isXMLResponse=false;
			}*/
			if (!isXMLResponse)
			{
				logger.error("La respuesta recibida no tiene formato XML");
				// Escribimos los bytes directamente en el response
				response.getOutputStream().write(responseBody);
				response.getOutputStream().flush();
				return;
			}
			
		
		
		
			/*
			 * El nodo raiz debera ser un nodo msGMLOutput, en otro caso es que no
			 * hemos obtenido correctamente la feature
			 */
			Element root=document.getDocumentElement();
			if (!root.getNodeName().equals("msGMLOutput"))
			{
				logger.error("Error al realizar la peticion a \""+urlWFSServer+"\". La raiz del documento es ["+root.getNodeName()+"]");
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
		
		

			/*
			 * Creamos un map para poder traducir los atributos. En el caso de que
			 * estemos haciendo la peticion al map server nuestro tendremos
			 * traducciones. En caso de que sea una peticion a un servidor externo
			 * lo tendremos vacio y no traduciremos nada
			 */
			try
			{
				
				String urlMapServer=Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER);
				String urlMapServerInternal="";
				try {
					urlMapServerInternal = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER_INTERNAL);
				} catch (Exception e) {
				}
				if (urlMapServerInternal==null)
					urlMapServerInternal="";
				
				
				Map attributesTranslation=new HashMap();
				Hashtable attributesValues=null;
				if (urlWFSServer.startsWith(urlMapServer)  || (urlWFSServer.startsWith(urlMapServerInternal) ))
				{
					LocalgisLayerManager localgisLayerManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
					String configurationLocalgisWeb=(String) request.getAttribute("configurationLocalgisWeb");
					boolean publicMaps;
					if (configurationLocalgisWeb!=null&&((configurationLocalgisWeb.equals("public"))||(configurationLocalgisWeb.equals("incidencias"))))
					{
						publicMaps=true;
					}
					else if (configurationLocalgisWeb!=null&&configurationLocalgisWeb.equals("private"))
					{
						publicMaps=false;
					}
					else
					{
						logger.debug("El parámetro de configuracion \"configurationLocalgisWeb\" no esta bien definido");
						request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
						return;
					}
					layerName=getQueryLayersFromURL(urlWFSServer);
	
					List attributes=null;
					if (idLayer==null)
						attributes=localgisLayerManager.getAttributesLayer(layerName,new Boolean(publicMaps),language);
					else
						attributes=localgisLayerManager.getAttributesLayerByIdLayer(Integer.parseInt(idLayer), idMap, new Boolean(publicMaps), language);
					
					Iterator itAttributes=attributes.iterator();
					while (itAttributes.hasNext())
					{
						LocalgisAttributeTranslated attribute=(LocalgisAttributeTranslated) itAttributes.next();
						attributesTranslation.put(attribute.getAttributename(),attribute.getAlias());
					}
	
					// Obtenemos la informacion sobre las traducciones disponibles
					// para los atributos que tienen dominio en la tabla especifica.
					// El objetivo
					// es posteriormente en lugar de presentar el valor se presenta
					// la traduccion
					// por ejemplo en lugar de presentar MT se prense Metro.
					attributesValues=(Hashtable) getServletContext().getAttribute(layerName);
					if (attributesValues==null) attributesValues=getAtributeValues(localgisLayerManager,layerName,language);
	
					if (attributesValues!=null) getServletContext().setAttribute(layerName,attributesValues);
	
				}
	
				accion=getAccionFromURL(urlWFSServer);
				Collection features=getFeatures(root,attributesTranslation,attributesValues,language,accion);
	
				if (features==null||features.isEmpty())
				{					
					//request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
					//return;
					continue;
				}
				existenFeatures=true;
				logger.info("Devolviendo respuesta GetFeatureInfoProxy");
				if ((accion!=null)&&(accion.equals("incidencia"))){
					incidencia(request,response,features,new String(responseBody),layerName);
					return;
				}
				else{
					sendResponse(request,response,features,layerName,nameTranslated,new String(responseBody),x,y,srid,idEntidad,locale);			
					//return;
				}
			}
			catch (LocalgisConfigurationException e)
			{
				logger.error("Error de configuración.",e);
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
			catch (LocalgisInitiationException e)
			{
				logger.error("Error de inicializacion.",e);
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
			catch (LocalgisDBException e)
			{
				logger.error("Error de de base de datos.",e);
				request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
				return;
			}
		}
		
		if (!existenFeatures)
		{					
			request.getRequestDispatcher(ERROR_PAGE).forward(request,response);
			return;			
		}
		//Si no se devuelve nada devolvemos que no existen features
		return;
	}



	private void incidencia(HttpServletRequest request, HttpServletResponse response, Collection features, String gml, String layerName) throws IOException
	{
		System.out.println("GetFeatureInfoProxy.incidencia");
		response.setContentType("text/xml");

		PrintWriter printWriter=response.getWriter();

		Iterator itFeatures=features.iterator();
		while (itFeatures.hasNext())
		{
			printWriter.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			printWriter.println("<incidencia>");
			// SortedMap feature = (SortedMap) itFeatures.next();
			LinkedHashMap feature=(LinkedHashMap) itFeatures.next();
			Iterator itFeature=feature.keySet().iterator();
			while (itFeature.hasNext())
			{
				String attributeName=(String) itFeature.next();
				String attributeValue=(String) feature.get(attributeName);
				if ((attributeName!=null)&&((attributeName.equals("id")))||(attributeName.equals("id_municipio"))||(attributeName.equals("nombre")))
				{
					printWriter.println("<"+attributeName+">");
					printWriter.println(attributeValue);
					printWriter.println("</"+attributeName+">");
				}
			}
			printWriter.println("<layer_name>");
			printWriter.println(layerName);
			printWriter.println("</layer_name>");
			printWriter.println("</incidencia>");
		}
		try
		{
			String encodedGML=URLEncoder.encode(gml.replaceAll("\\c+"," "),"ISO-8859-1");

		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Error al codificar la respuesta GML");
		}
	}



	/**
	 * Obtenemos la lista de valores de atributos (dominios) traducidos
	 * 
	 * @param localgisLayerManager
	 * @param layername
	 * @param language
	 * @return
	 */
	private Hashtable getAtributeValues(LocalgisLayerManager localgisLayerManager, String layername, String language)
	{

		Hashtable atributos=null;

		try
		{
			List attributesValues=localgisLayerManager.getAttributesValuesLayer(layername,language);
			Iterator itAttributesValues=attributesValues.iterator();
			atributos=new Hashtable();
			while (itAttributesValues.hasNext())
			{
				LocalgisAttributeValueTranslated attributeValue=(LocalgisAttributeValueTranslated) itAttributesValues.next();
				String attributeName=attributeValue.getAttributename();
				String pattern=attributeValue.getPattern();
				String traduccion=attributeValue.getTraduccion();
				String locale=attributeValue.getLocale();
				// System.out.println("Datos:"+attributeName+" "+pattern+" "+traduccion);

				// Obtenemos la lista de traducciones de un atributo
				Hashtable traduccionesValues=(Hashtable) atributos.get(attributeName);
				if (traduccionesValues==null)
				{
					traduccionesValues=new Hashtable();
					atributos.put(attributeName,traduccionesValues);
				}
				// Si la traduccion ya existe y el idioma del dominio no es el
				// especificado no la
				// insertamos. Con esto queremos conseguir que si hay
				// traducciones para el idioma
				// elegido nos quedemos con esta y en caso contrario con la
				// castellana.
				String traduccionRecuperada=(String) traduccionesValues.get(pattern);
				if (traduccionRecuperada==null)
					traduccionesValues.put(pattern,traduccion);
				else if ((traduccionRecuperada!=null)&&(attributeValue.getLocale().equals(language))) traduccionesValues.put(pattern,traduccion);

			}
		}
		catch (LocalgisDBException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return atributos;

	}



	private void sendResponse(HttpServletRequest request, HttpServletResponse response, Collection features,
			String layername, String nameTranslated,String gml,String x, String y,String srid,String idEntidad,String locale) throws IOException, LocalgisInitiationException, LocalgisConfigurationException
	{
		response.setContentType("text/html; charset=ISO-8859-1");
		response.setCharacterEncoding("ISO-8859-1");
		
		String nameToPrint=layername;
		if (nameTranslated!=null)
			nameToPrint=nameTranslated;

		PrintWriter printWriter=response.getWriter();

        Boolean publicMap;
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            publicMap = Boolean.TRUE;
        } else {
            publicMap = Boolean.FALSE;
        }

        
		/*printWriter.println("<script>");
		printWriter.println("slideToggleLayer(\"ficha_"+layername+"\");");
		printWriter.println("</script>");*/
        
		
		if (features.size()>0){
			LinkedHashMap feature=(LinkedHashMap)features.toArray()[0];
			String idFeature=(String)feature.get("id");
		
			//printWriter.println("<a href=\"#\" id=\"mostrar\" onclick=\"slideToggle();\">MOSTRAR</a>");
			StringBuffer cadena=new StringBuffer();
			cadena.append("<div id=\"mostrar\">");
			cadena.append("<table><tr>");
			//cadena.append("<td onclick=\"slideToggleLayer('ficha"+layername+"\");\'>"+layername+"</td>");
			cadena.append("<td onclick=\"slideToggleLayer('ficha_"+layername+"');\">"+nameToPrint+"</td>");
			cadena.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>");
			
			//TIP. Aqui se podria colocar un icono para presentar informes de la capa
			//Solo presentamos los informes en los mapas privados.
			com.localgis.web.reports.GetFeatureReport.mostrarInformes(publicMap,cadena,layername,idFeature,x,y,srid,idEntidad,locale);
			
			cadena.append("</tr></table>");
			cadena.append("</div>");
			printWriter.println(cadena);
			/*printWriter.println("<div id=\"caja\">");
			printWriter.println("<a href=\"#\" class=\"close\" onclick=\"slideUp();\">[x]</a>");
			printWriter.println("<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren</p>");
			printWriter.println("</div>");*/
		}
		
		
		//En la mayoria de los casos solo habrá una feature en una capa
		Iterator itFeatures=features.iterator();
		while (itFeatures.hasNext())
		{
			//printWriter.println("<div id=\"ficha_datos_basicos_feature\" class=\"mostrar\" onclick=\"slideToggle();\">");
			//printWriter.println("<div id='ficha_"+layername+"' onclick=\"slideToggleLayer('ficha"+layername+"');\">");
			printWriter.println("<div id='ficha_"+layername+"' onclick=\"slideToggleLayer('ficha_"+layername+"');\">");
			printWriter.println("<table width=\"95%\">");

			// SortedMap feature = (SortedMap) itFeatures.next();
			LinkedHashMap feature=(LinkedHashMap) itFeatures.next();
			Iterator itFeature=feature.keySet().iterator();
			while (itFeature.hasNext())
			{
				String attributeName=(String) itFeature.next();
				String attributeValue=(String) feature.get(attributeName);
				printWriter.println("<tr>");
				printWriter.println("<td align=\"left\"><b>"+attributeName+"</b></td>");
				printWriter.println("<td align=\"left\">"+attributeValue+"</td>");
				printWriter.println("</tr>");
			}

			// Para que salga algo al final y no se corte la ultima linea
			printWriter.println("<tr>");
			printWriter.println("<td align=\"left\"><b>&nbsp;</b></td>");
			printWriter.println("<td align=\"left\">&nbsp;</td>");
			printWriter.println("</tr>");
			printWriter.println("</table>");
			printWriter.println("</div>");
		}

		// ********** SADIM ***********
		// ********** SADIM ***********
		// ********** SADIM ***********
		if (features.size()>0)
		{
			boolean onlyPublicMaps=true;
			if (configurationLocalgisWeb!=null&&configurationLocalgisWeb.equals("private"))
			{
				onlyPublicMaps=false;
			}	
			try
			{
				LocalgisMapManager localgisMapManager=LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
				
				LocalgisLayer localgisLayer=localgisMapManager.getLayerByName(layername);
				
				//DaoManager daoManager=LocalgisManagerBuilderFactory.getDaoManager();
				//LocalgisLayerDAO localgisLayerDAO=(LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
				//LocalgisLayer localgisLayer=localgisLayerDAO.selectLayerByName(layername);

				LinkedHashMap feature=(LinkedHashMap) features.toArray()[0];
				
				//int idFeature=Integer.parseInt(feature.values().toArray()[0].toString());
				//La capa de parcelas de urbana esta mal definida y en lugar de devolver id devuelve Id de Parcela, no se si
				//es lo normal, pero controlamos ese caso nada mas.
				int idFeature=-1;
				if (feature.get("id")!=null){
					idFeature=Integer.parseInt((String)feature.get("id"));
				}
				else{
					if (feature.get("ID de parcela")!=null){
						idFeature=Integer.parseInt((String)feature.get("ID de parcela"));
					}
				}
				int idLayer=localgisLayer.getLayeridgeopista();

				//List<HashMap<String, Object>> anexos=localgisLayerDAO.selectAnexosByIdLayerAndIdFeature(idLayer,idFeature);
				List<HashMap<String, Object>> anexos=null;
				
				if (idFeature!=-1){
					
					//¿DONDE MOSTRAMOS LOS DOCUMENTOS EN LOS PRIVADOS EN LOS PUBLICOS?
					//if (onlyPublicMaps) 
						//anexos=localgisMapManager.selectPublicAnexosByIdLayerAndIdFeature(idLayer,idFeature);
						//anexos=localgisLayerDAO.selectPublicAnexosByIdLayerAndIdFeature(idLayer,idFeature);
					//else
						anexos=localgisMapManager.selectAllAnexosByIdLayerAndIdFeature(idLayer,idFeature);
						//anexos=localgisLayerDAO.selectAllAnexosByIdLayerAndIdFeature(idLayer,idFeature);
				}							
				if (anexos!=null && anexos.size()>0)
				{
					logger.info("El elemento dispone de anexos");
					if(AlfrescoManagerUtils.isAlfrescoServerActive()){
						String user = (String) request.getSession().getAttribute(LoginFilter.LOGIN_ATTRIBUTE);
						AlfrescoManagerImpl.getInstance().setUserCredential(user, COperacionesAdministrador.getUserPassword(user).getDescripcion());		
					}
					
					printWriter.println("<div id=\"ficha_anexos_feature\">");
					printWriter.println("<p class=\"anexos_titulo\">Documentación adjunta</p>");
					printWriter.println("<table width=\"95%\">");

					Iterator<HashMap<String, Object>> itAnexos=anexos.iterator();
					
					while (itAnexos.hasNext())
					{
						HashMap<String, Object> campos=itAnexos.next();
						String idDocumento = (String) campos.get("id_documento");
						printWriter.println("<tr>");
						Timestamp t=(Timestamp) campos.get("fecha_alta");
						String folder=new SimpleDateFormat("yyyyMM").format(t);
						
						/*String publico = String.valueOf((BigDecimal) campos.get("publico"));
						if(publico.equals("0") || configurationLocalgisWeb.equals("private")){
							if(AlfrescoManagerUtils.isAlfrescoServerActive() && AlfrescoManagerUtils.isAlfrescoUuid(idDocumento, (String)campos.get("id_municipio")) && AlfrescoManagerImpl.getInstance().existsNode(new AlfrescoKey(idDocumento, AlfrescoKey.KEY_UUID))){
								printWriter.println("<tr onmouseover=\"this.style.background='#AACBFF';this.style.cursor='pointer'\" onmouseout=\"this.style.background='none';this.style.cursor='default'\">");
								printWriter.println("<td align=\"left\">");
								printWriter.println("<b><a style=\"color:#0000FF;\" href=\"getDocumentoAlfresco.jsp?id="+idDocumento+"&folder="+folder+"&nombre="+URLEncoder.encode(campos.get("nombre").toString(),"UTF-8")+"\">"+campos.get("nombre")+"</a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b>");
								printWriter.println("</td>");
								printWriter.println("<td align=\"left\">");
								printWriter.println("<img height=\"15px\" src=\"img/logo_alfresco.png\"/>");	
								printWriter.println("</td>");
								printWriter.println("</tr>");
							}
							else {*/
								printWriter.println("<tr onmouseover=\"this.style.background='#AACBFF';this.style.cursor='pointer'\" onmouseout=\"this.style.background='none';this.style.cursor='default'\">");
								printWriter.println("<td align=\"left\">");
								String nombreRecurso=(String)campos.get("nombre");
								if ((nombreRecurso!=null) && ( (nombreRecurso.startsWith("http")))){
									if ((nombreRecurso!=null) && ( (nombreRecurso.endsWith(".jpg")) || (nombreRecurso.endsWith(".gif")) || (nombreRecurso.endsWith(".png")))){
										printWriter.println("<b><a style=\"color:#0000FF;\" href=\""+campos.get("nombre")+"\">"+" <img width=100 height=100 src=\""+campos.get("nombre")+"\" alt=\""+campos.get("nombre")+"\" /></a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b>" + campos.get("comentario"));
									}		
									else{
										printWriter.println("<b><a style=\"color:#0000FF;\" href=\""+campos.get("nombre")+"\">"+campos.get("nombre")+"</a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b>" + campos.get("comentario"));									

									}
								}
								else if ((nombreRecurso!=null) && ( (nombreRecurso.endsWith(".jpg")) || (nombreRecurso.endsWith(".gif")) || (nombreRecurso.endsWith(".png")))){
									String idDocumentoText="getDocumento.jsp?id="+idDocumento+"&folder="+folder+"&nombre="+URLEncoder.encode(campos.get("nombre").toString(),"UTF-8");
									printWriter.println("<b><a style=\"color:#0000FF;\" href=\""+idDocumentoText+"\">"+" <img width=100 height=100 src=\""+idDocumentoText+"\" alt=\""+campos.get("nombre")+"\" /></a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b>" + campos.get("comentario"));
								}
								else
									printWriter.println("<b><a style=\"color:#0000FF;\" href=\"getDocumento.jsp?id="+idDocumento+"&folder="+folder+"&nombre="+URLEncoder.encode(campos.get("nombre").toString(),"UTF-8")+"\">"+campos.get("nombre")+"</a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b>" + campos.get("comentario"));
								printWriter.println("</td>");
								printWriter.println("<td align=\"left\">");
								printWriter.println("<img height=\"15px\"/>");	
								printWriter.println("</td>");
								printWriter.println("</tr>");
							//}
						/*printWriter.println("<td align=\"left\"><b><a style=\"color:#0000FF;\" href=\"getDocumento.jsp?id="+campos.get("id_documento")+"&folder="+folder+"&nombre="+URLEncoder.encode(campos.get("nombre").toString(),"UTF-8")+"\">"+campos.get("nombre")+"</a> ("+(int) (1+Math.ceil(Integer.parseInt(campos.get("tamanio").toString())/1000))+" Kb)</b><br/>"+campos.get("comentario")+"</td>");
						printWriter.println("</tr>");*/
						//}
					}

					printWriter.println("</table>");
					printWriter.println("</div><br/><br/>");
					
				}
			}
			catch (Exception ex)
			{
				logger.error("Error obteniendo documentos anexados a la feature seleccionada: ",ex);
				System.out.println("Error obteniendo documentos anexados a la feature seleccionada");
			}
			catch (Throwable ex2){
				logger.error("Error obteniendo documentos anexados a la feature seleccionada: ",ex2);
			}
		}
		// ****************************
		// ****************************
		// ****************************
		
	
		try
		{
			String encodedGML=URLEncoder.encode(gml.replaceAll("\\c+"," "),"ISO-8859-1");
			printWriter.print("<div id=\"gmlResponse\" style=\"display: none;\">");
			printWriter.print(encodedGML);
			printWriter.print("</div>");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error("Error al codificar la respuesta GML");
		}
	}



	
    

	/**
	 * Busca el valor del parametro accion
	 * 
	 * @param url
	 *            URL donde se quiere buscar el valor del parametro
	 * @return El valor del parametro accion
	 */
	private String getAccionFromURL(String url)
	{

		int indexOfParams=url.indexOf("?");
		int indexOfParamQueryLayers=url.indexOf(ACCION_PARAM+"=",indexOfParams);
		int indexOfEndParamQueryLayers=url.indexOf("&",indexOfParamQueryLayers);
		if (indexOfEndParamQueryLayers!=-1)
		{
			return url.substring(indexOfParamQueryLayers+ACCION_PARAM.length()+1,indexOfEndParamQueryLayers);
		}
		else
		{
			return url.substring(indexOfParamQueryLayers+ACCION_PARAM.length()+1);
		}
	}

	/**
	 * Método que obtiene el valor del parametro QUERY_LAYERS a partir de una
	 * URL
	 * 
	 * @param url
	 *            URL donde se quiere buscar el valor del parametro
	 * @return El valor del parametro QUERY_LAYERS
	 */
	private String getQueryLayersFromURL(String url)
	{

		int indexOfParams=url.indexOf("?");
		int indexOfParamQueryLayers=url.indexOf(QUERY_LAYERS_PARAM+"=",indexOfParams);
		int indexOfEndParamQueryLayers=url.indexOf("&",indexOfParamQueryLayers);
		if (indexOfEndParamQueryLayers!=-1)
		{
			return url.substring(indexOfParamQueryLayers+QUERY_LAYERS_PARAM.length()+1,indexOfEndParamQueryLayers);
		}
		else
		{
			return url.substring(indexOfParamQueryLayers+QUERY_LAYERS_PARAM.length()+1);
		}
	}



	/**
	 * Método que procesa un documento XML y devuelve una coleccion de features,
	 * donde cada feature sera una lista ordenada de pares atributo-valor. Las
	 * features se intentaran traducir utilzando el Map pasado como parametro.
	 * Las features se ordenaran segun el idioma pasado como parametro
	 * 
	 * @param root
	 *            Nodo raiz del XML
	 * @param attributesTranslation
	 *            Map para realizar las traducciones
	 * @param locale
	 *            Locales a las que se desea traducir
	 * @return Una coleccion de features
	 */
	private Collection getFeatures(Element root, Map attributesTranslation, Hashtable attributesValues, String locale, String accion)
	{
		/*
		 * Creamos una coleccion donde meteremos todas las features por si la
		 * consulta devolviera varias
		 */
		Collection features=new ArrayList();
		NodeList layersNodeList=root.getChildNodes();
		int layersNodeListLength=layersNodeList.getLength();
		for (int i=0; i<layersNodeListLength; i++)
		{
			/*
			 * Buscamos el primer nodo que sea un elemento, que será el nodo
			 * correpondiente a la capa. Solo debería haber un elemento de este
			 * nivel
			 */
			Node layerNode=layersNodeList.item(i);
			if (layerNode.getNodeType()==Node.ELEMENT_NODE)
			{
				NodeList featuresNodeList=((Element) layerNode).getChildNodes();
				int featuresNodeListLength=featuresNodeList.getLength();
				/*
				 * Buscamos el primer nodo que sea un elemento, que será el nodo
				 * correpondiente a la feature. Pude haber varios
				 */
				for (int j=0; j<featuresNodeListLength; j++)
				{
					Node featureNode=featuresNodeList.item(j);
					if (featureNode.getNodeType()==Node.ELEMENT_NODE)
					{
						NodeList attributesNodeList=((Element) featureNode).getChildNodes();
						int attributesNodeListLength=attributesNodeList.getLength();

						// Cambiamos la estructura para que la ordenacion
						// depenga del orden definido
						// en el gestor de capas.
						// SortedMap feature = new TreeMap(new
						// ComparatorAttributeAlias(locale));
						LinkedHashMap feature=new LinkedHashMap();

						/*
						 * Insertamos en un SortedMap todos los atributos que
						 * encontremos, previamente traducidos, salvo:
						 * id_localgis, GEOMETRY y gml:boundedBy
						 */
						for (int k=0; k<attributesNodeListLength; k++)
						{
							Node attributeNode=attributesNodeList.item(k);
							if (attributeNode.getNodeType()==Node.ELEMENT_NODE)
							{
								if (!(attributeNode.getNodeName().equals("gml:boundedBy")||attributeNode.getNodeName().equals("id_localgis")||attributeNode.getNodeName().equals("GEOMETRY")))
								{
									String nameAttributeTranslated=null;
									if ((accion!=null)&&(accion.equals("incidencia")))
										nameAttributeTranslated=(String) attributeNode.getNodeName();
									else
										nameAttributeTranslated=(String) attributesTranslation.get(attributeNode.getNodeName());
									if (nameAttributeTranslated==null)
									{
										nameAttributeTranslated=attributeNode.getNodeName();
									}
									// En lugar de añadir simplemente el
									// contenido sin traducir lo traducimos
									// por si pertenece a un dominio
									String contenido=attributeNode.getTextContent();
									try
									{
										if (attributesValues==null) attributesValues=new Hashtable();
										Hashtable attributeValuesDomain=(Hashtable) attributesValues.get(attributeNode.getNodeName());
										if (attributeValuesDomain!=null)
										{
											String traduccionDominio=(String) attributeValuesDomain.get(contenido);
											if (traduccionDominio!=null) contenido=traduccionDominio;
										}
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}

									// System.out.println("feature attribute="+nameAttributeTranslated+":"+contenido);

									feature.put(nameAttributeTranslated,contenido);
								}
							}
						}
						features.add(feature);
					}
				}
				// Como solo vamos a tratar una capa salimos del bucle
				break;
			}
		}
		return features;
	}
	
	
	public static void main(String args[]){
		String cadena="http://localhost:8080?id=2%LAYERS=AA&QUERY_LAYERS=BB&CAMPO3=22";
		
		System.out.println("cadena"+cadena);
		cadena=cadena.replaceAll("LAYERS=\\w+&","LAYERS=XX&");
		cadena=cadena.replaceAll("QUERY_LAYERS=\\w+&","QUERY_LAYERS=YY&");
		
		System.out.println("cadena"+cadena);
	}
	
}



	
