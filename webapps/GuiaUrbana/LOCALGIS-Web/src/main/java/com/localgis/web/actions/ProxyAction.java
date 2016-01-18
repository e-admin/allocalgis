/**
 * ProxyAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.util.EncodeUtil;

//import edu.umn.gis.mapscript.mapObj;

public class ProxyAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        try
        { 
        	
        	String urlFinal = request.getParameter("urlValor");
        	String parametro = request.getParameter("request");
        	String esRuta = request.getParameter("get_ruta");
        	    
        	if((urlFinal != null)&&(!urlFinal.equals("")))
        	{
        		
        		EncodeUtil encode_url = new EncodeUtil();
        	
        		urlFinal = encode_url.encodeURl(urlFinal);
        	}
        	if((parametro!=null)&&(!parametro.equals(""))&&(!parametro.equals("null")))
        	{
        		PostMethod method = new PostMethod(urlFinal);

        		method.setParameter("request",parametro);
        		HttpClient client = new HttpClient();

                int statusCode = client.executeMethod(method);
                
                
                
                if (statusCode == 200)
                {
                	HttpSession session = request.getSession();
                	byte array1 [] = method.getResponseBody();
                	
                	String cadenaUTF = new String(array1, "UTF-8");
                	
                	if(esRuta != null)
                	{
                		if((esRuta.equals("1")) || (esRuta.equals("2")))
                		{
                			String cadenaRecorrido="";
	                		String gmlRuta = "<wfs:FeatureCollection xmlns:wfs=\"http://www.opengis.net/wfs\">";
	                		StringReader stringr = new StringReader(cadenaUTF);
	                		SAXBuilder builder=new SAXBuilder(); 
	                		Integer cuenta = 0;
	                		Document doc=builder.build(stringr);
	                		
	                        Element raiz=doc.getRootElement();
	                        
	                        List features=raiz.getChildren();                        
	                        Iterator i = features.iterator();
	                        while (i.hasNext()){
	                            Element e= (Element)i.next();
	                            if(e.getName().toUpperCase().equals("PROCESSOUTPUTS"))
	                            {
	                            	List output = e.getChildren();
	                            	Iterator outputIt = output.iterator();
	                                while (outputIt.hasNext()){
	                                	 Element eoutput= (Element)outputIt.next();
	                                	 if(eoutput.getName().toUpperCase().equals("OUTPUT"))
	                                     {
	                                		List data = eoutput.getChildren();
	                                		Iterator dataIt = data.iterator();
	                                		while (dataIt.hasNext()){
	                                			Element edata= (Element)dataIt.next();
	                                			 if(edata.getName().toUpperCase().equals("DATA"))
	                                			 {
	                                				List complexData = edata.getChildren();
	                                          		Iterator complexDataIt = complexData.iterator();
	                                          		while (complexDataIt.hasNext()){
	                                         			 Element ecomplexData= (Element)complexDataIt.next();
	                                         			 if(ecomplexData.getName().toUpperCase().equals("COMPLEXDATA"))
	                                         			 {
	                                         				List RoutePath = ecomplexData.getChildren();
	                                                 		Iterator routePathIt = RoutePath.iterator();
	                                                 		
	                                                 		 while (routePathIt.hasNext()){
	                                                 			Element eRoutePath= (Element)routePathIt.next();
	                                                 			
	                                                 			 if(eRoutePath.getName().toUpperCase().equals("ROUTEPATH"))
	                                                 			 {
	                                                 				List lineString = eRoutePath.getChildren();
	                                                         		Iterator lineStringIt = lineString.iterator();
	                                                         		 while (lineStringIt.hasNext()){
	                                                         			 Element eLineString= (Element)lineStringIt.next();
	                                                         			 if(eLineString.getName().toUpperCase().equals("LINESTRING"))
	                                                         			 {
	                                                         				cuenta = cuenta + 1;
	                                                         				List coordinates = eLineString.getChildren();
	                                                                 		Iterator coordinatesIt = coordinates.iterator();
	                                                                 		 while (coordinatesIt.hasNext()){
	                                                                 			 Element eCoordinates= (Element)coordinatesIt.next();
	                                                                 			 if(eCoordinates.getName().toUpperCase().equals("COORDINATES"))
	                                                                 			 {
	                                                                 				String coordenadas = eCoordinates.getText();
	                                                                 				gmlRuta = gmlRuta + "<gml:featureMember xmlns:gml=\"http://www.opengis.net/gml\"><feature:features xmlns:feature=\"http://mapserver.gis.umn.edu/mapserver\" fid=\""+cuenta+"\"><feature:geometry>";
	                                                                 				gmlRuta = gmlRuta + "<gml:LineString><gml:coordinates decimal=\".\" cs=\", \" ts=\" \">"+coordenadas+"</gml:coordinates></gml:LineString></feature:geometry></feature:features></gml:featureMember>";
	                                                                 				
	                                                                 			 }
	                                                                 		 }
	                                                         				
	                                                         			 }
	                                                         			if((eLineString.getName().toUpperCase().equals("POLYGON"))&&(esRuta.equals("2")))
	                                                         			 {
	                                                         				cuenta = cuenta + 1;
	                                                         				List coordinates = eLineString.getChildren();
	                                                                 		Iterator coordinatesIt = coordinates.iterator();
	                                                                 		 while (coordinatesIt.hasNext()){
	                                                                 			 Element eCoordinates= (Element)coordinatesIt.next();
	                                                                 			 if(eCoordinates.getName().toUpperCase().equals("OUTERBOUNDARYIS"))
	                                                                 			 {
	                                                                 				List gmlLinearRing = eCoordinates.getChildren();
	                                                                 				Iterator gmlLinearRingIt = gmlLinearRing.iterator();
	   	                                                                 		 	while (gmlLinearRingIt.hasNext()){
		   	                                                                 		 	Element eLinearRing= (Element)gmlLinearRingIt.next();
		   	                                                                 		 	if(eLinearRing.getName().toUpperCase().equals("LINEARRING"))
			                                                                 			{
			   	                                                                 		 	List coordPolygon = eLinearRing.getChildren();
			                                                                 				Iterator coordPolygonIt = coordPolygon.iterator();
			   	                                                                 		 	while (coordPolygonIt.hasNext()){
				   	                                                                 		 	Element ecoordPolygon= (Element)coordPolygonIt.next();
				   	                                                                 		 	if(ecoordPolygon.getName().toUpperCase().equals("COORDINATES"))
					                                                                 			{
				   	                                                                 		 		/*List coordPolygon = eCoordinates.getChildren();
				   	                                                                 		 		Iterator gmlLinearRingIt = gmlLinearRing.iterator();*/
				   	                                                                 		 		String coordenadas =ecoordPolygon.getText();
				   	                                                                 		 		//String coordenadas = eCoordinatesPolygon.getText();
					                                                                 				gmlRuta = gmlRuta + "<gml:featureMember xmlns:gml=\"http://www.opengis.net/gml\"><feature:features xmlns:feature=\"http://mapserver.gis.umn.edu/mapserver\" fid=\""+cuenta+"\"><feature:geometry>";
					                                                                 				gmlRuta = gmlRuta + "<gml:polygonMember><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates decimal=\".\" cs=\", \" ts=\" \">"+coordenadas+"</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon></gml:polygonMember></feature:geometry></feature:features></gml:featureMember>";
					                                                                 			 }
			   	                                                                 		 	}
		   	                                                                 		 		/*List coordPolygon = eCoordinates.getChildren();
		   	                                                                 		 		Iterator gmlLinearRingIt = gmlLinearRing.iterator();*/
		   	                                                                 		 		//String coordenadas ="";
		   	                                                                 		 		//String coordenadas = eCoordinatesPolygon.getText();
			                                                                 				
			                                                                 			 }
	   	                                                                 		 	}
	                                                                 				
	                                                                 			 }
	                                                                 		 }
	                                                         				
	                                                         			 }
	                                                         		 }
	                                                         		// break;
	                                                 			 }
	                                                 			 if(esRuta.equals("1"))
	                                                 			 {
		                                                 			if(eRoutePath.getName().toUpperCase().equals("DESCRIPTIONGROUP"))
		                                                			 {
		                                                 				 Integer arrayRutaId = 0;
		                                                 				cadenaRecorrido += "<descripcion>";
		                                                 				List descriptionGroup = eRoutePath.getChildren();
		                                                         		Iterator descriptionGroupIt = descriptionGroup.iterator();
		                                                         		 while (descriptionGroupIt.hasNext()){
		                                                         			
		                                                         			 Element eDescGroup= (Element)descriptionGroupIt.next();
		                                                         			 if(eDescGroup.getName().toUpperCase().equals("DESCRIPTIONS"))
		                                                         			 {
		                                                         				
		                                                         				cadenaRecorrido += "<arrayroute id=\""+arrayRutaId+"\">";
		                                                         				arrayRutaId = arrayRutaId + 1;
		                                                         				List defineGroup = eDescGroup.getChildren();
		                                                         				Iterator defineGroupIt = defineGroup.iterator();
		                                                         				Integer cuentaRecorrido = 0;
		                                                         				while (defineGroupIt.hasNext()){
		                                                         					Element eDefine= (Element)defineGroupIt.next();
		                                                         					if(eDefine.getName().toUpperCase().equals("DESCRIPTION")){
		                                                         						
		                                                         						String infoCalle = "";
			                                                         					String infoOrientacion = "";
			                                                         					
			                                                         					List listaDato = eDefine.getChildren();
			                                                             				Iterator listaDatoIt = listaDato.iterator();
			                                                             				while (listaDatoIt.hasNext()){
			                                                             					
			                                                             					Element eDatoRuta= (Element)listaDatoIt.next();
			                                                             					if(eDatoRuta.getName().toUpperCase().equals("NAME"))
			                                                             					 {
			                                                             						infoCalle =  eDatoRuta.getText();
			                                                             					 }
			                                                             					 else if(eDatoRuta.getName().toUpperCase().equals("ORIENTATION")){
			                                                             						 if(cuentaRecorrido != 0){
			                                                             							 if(eDatoRuta.getText().equals("LEFT"))infoOrientacion =  "Gire a la izquierda en la calle ";
			                                                             							 else if(eDatoRuta.getText().equals("RIGHT"))infoOrientacion =  "Gire a la derecha en la calle ";
			                                                             							 else infoOrientacion = "Continue por la calle ";
			                                                             						 }else infoOrientacion = "Salida desde la calle ";
			                                                             					 }
			                                                             					else if(eDatoRuta.getName().toUpperCase().equals("LENGTH")){
			                                                             						cadenaRecorrido += "<calle>" + infoOrientacion + infoCalle + " durante " + eDatoRuta.getText() + " metros </calle>";
			                                                             						cuentaRecorrido = cuentaRecorrido + 1;
			                                                             					}
			                                                             					else{
			                                                             						
			                                                             					}
			                                                             					 
			                                                             				
			                                                             				}
		                                                         					
			                                                             				
			                                                         				}
			                                                         				
		                                                         				}
		                                                         				cadenaRecorrido +="</arrayroute>";
		                                                         			}
		                                                         			
		                                                         		 }
		                                                         		cadenaRecorrido += "</descripcion>";
		                                                			 }
	                                                 			 }//ruta 
	                                                 		 }
	                                         			 }
	                                         			// break;
	                                         		 }
	                                				 //break;
	                                			 }
	                                		}
	                                		//break;
	                                     }
	                                }
	                                //break;
	                            }
	                        }
	
	                        gmlRuta = gmlRuta + cadenaRecorrido + "</wfs:FeatureCollection>";
	                        if(esRuta.equals("2"))session.setAttribute("gmlArea",gmlRuta);
	                        else session.setAttribute("gmlRutaPrint",gmlRuta);
	                        
	                        //System.out.println(gmlRuta);
	                        
	                        session.setAttribute("resPotal",gmlRuta);
	                    
                		}//ruta
                		
                		
                		
                		
                	}
                	else session.setAttribute("resPotal",cadenaUTF);
                	
                	/*if((esRuta == null)||(!esRuta.equals("1")))
                	{
                		//System.out.println(cadenaUTF);
                		session.setAttribute("resPotal",cadenaUTF);
                	}*/
                	
                	
                    return (mapping.findForward("exito"));
                }
                
                else
                {
                	return null;
                }
        	}
        	else
        	{
        		
	        		GetMethod method = new GetMethod(urlFinal);
	        		HttpClient client = new HttpClient();
	        		
	                int statusCode = client.executeMethod(method);
	                
	                if (statusCode == 200)
	                {
	                	HttpSession session = request.getSession();
	                    byte array1 [] = method.getResponseBody();
	                 	String cadenaISO = new String(array1,"ISO-8859-1");//
	                 	int index=cadenaISO.indexOf("<WMT_MS_Capabilities");
	                 	if(index==-1){//No encontrado
	                 		index=0;
	                 		cadenaISO = new String(array1,"UTF-8");//
	                 	}
	                 	cadenaISO=cadenaISO.substring(index);
	                 	session.setAttribute("resPotal",cadenaISO);
	                    return (mapping.findForward("exito"));
	                }
	                
	                else
	                {
	                	return null;
	                }
        	
        	}
        	
        	
        	
        	
         
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return (mapping.findForward("error"));
            
        }
        catch(Throwable t1)
        {
            t1.printStackTrace();
            return (mapping.findForward("error"));
            
        }
   }
	
	
}



