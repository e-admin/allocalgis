package com.geopista.ui.wms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;
import com.vividsolutions.wms.WMService;

public class FeatureInfoRequest {
	/**
	 *  Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(MapRequest.class);
	private WMService service;
	//coordenadas
    private double x;
    private double y;
    private String WMSLayers;
    private String format;
    private double height;
    private double width;
    private  BoundingBox bbox;
    
    
    /**Constructor
     */
    public FeatureInfoRequest(){
    }//fin del constructor


    
    /**Getters y Setters
     * 
     */
    
    
	public WMService getService() {
		return service;
	}


	public void setService(WMService service) {
		this.service = service;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}



	public String getWMSLayers() {
		return WMSLayers;
	}



	public void setWMSLayers(String layers) {
		WMSLayers = layers;
	}


	
	
	
	

public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
	}
	
	
	



public double getHeight() {
		return height;
	}



	public void setHeight(double height) {
		this.height = height;
	}



	public double getWidth() {
		return width;
	}



	public void setWidth(double width) {
		this.width = width;
	}
	
	


public BoundingBox getBbox() {
		return bbox;
	}



	public void setBbox(BoundingBox bbox) {
		this.bbox = bbox;
	}




public String callGetFeatureInfo() throws SAXException, IOException{
		//url del servidor
		String url=service.getServerUrl()+"service=wms&version="; 
		
		//versión
		if(service.getVersion()!=null)
			url+=service.getVersion();
		else
			url+="1.1.1";
		
		//tipo de petición
		url+="&request=getfeatureinfo";
		
		//coordenadas
		url+="&x="+x+"&y="+y;
			
			
		//nombre de la capa para la que se realiza la petición
		if(WMSLayers!=null)
			url+="&query_layers="+WMSLayers+"&layers="+WMSLayers;
			else
			url+="&query_layers=&layers=";
		
		
		//formato de la respuesta
		if(format!=null)
		url+="&info_format="+format;
		
		
		//dimensiones
		url+="&WIDTH="+this.width+"&HEIGHT="+this.height;
		
		
		//bbox
		url+="&bbox="+getBbox().getMinX()+","+getBbox().getMinY()+","+
		getBbox().getMaxX()+","+getBbox().getMaxY();
		
		//SRS
		url+="&SRS="+getBbox().getSRS();
		
		//STYLES
		url+="&STYLES=";

		//FORMAT. Esto esta puesto a fuego. Podria provocar problemas
		//en un servidor WMS que no admitiera este formato.
		url+="&FORMAT=image/jpeg";
		
		url=url.trim();
		System.out.println("URL GetFeatureInfo: "+url);
	    String texto="";
	    URL URL = new URL(url);
		InputStream inStream = URL.openStream();
		byte[] buffer = new byte[1024];
		int numBytes = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((numBytes = inStream.read(buffer, 0, 1024)) != -1){
			baos.write(buffer, 0, numBytes);
		}
		 texto = new String(baos.toByteArray());
		
		inStream.close();  
	    System.out.println(texto);
	    
	    
		return texto;		
	}//fin del método callGetFeatureInfo


	
	
	
	
	
	
	
	
	
/*
public void showNodes(Node nnn){
	NodeList nodes=nnn.getChildNodes();
	 for( int i=0; i < nodes.getLength(); i++ ) {
		  Node n = nodes.item( i );
	        if( n.getNodeType() == Node.ELEMENT_NODE ) {
	        	System.out.println(n.getLocalName());
	        	showNodes(n);
	        	
	        }//fin del for
	 }//fin del for	
}*/
  
  
  
}//fin de la clase
