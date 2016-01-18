/**
 * DocumentBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.document;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.ui.plugin.edit.LCGIII_DocumentManagerPlugin;
import com.geopista.util.config.UserPreferenceStore;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;






/**
 * Creado por SATEC.
 * User: angeles
 * Date: 24-abr-2006
 * Time: 14:44:44
 */
public class DocumentBean implements Serializable, Cloneable{

    private static final Log logger = LogFactory.getLog(DocumentBean.class);
    private static final long	serialVersionUID	= 3675121612249975115L;
    public static final char IMG_CODE='I';
    public static final char DOC_CODE='D';
    public static final char TEXT_CODE='T';
    public static final char ALL_CODE='A';
    public static final String WORD = "DOC";
    public static final String PPT = "PPT";
    public static final String PDF = "PDF";
    public static final String TXT = "TXT";
    public static final String XML = "XML";
    public static final String HTML = "HTML";
    public static final String BMP = "BMP";
    public static final String JPG = "JPG";
    public static final String GIF = "GIF";
    public static final String PNG = "PNG";
    private String id=null;
    private long size= -1;
    private String fileName;
    private Date fechaEntradaSistema;
    private Date fechaUltimaModificacion;  
    private String tipo;
    private String comentario;
    private String serverPath;
    private byte[] content;
    private int publico= 0;
    private String idMunicipio;
    private char is_imgdoctext= 'D';
    private byte[] thumbnail;
    private int oculto= 0;
    private String user;
    private String idEntidad;
    private Boolean propietario;



    public Boolean getPropietario() {
		return propietario;
	}

	public void setPropietario(Boolean propietario) {
		this.propietario = propietario;
	}

	public String getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(String idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getFechaEntradaSistema() {
        return fechaEntradaSistema;
    }

    public void setFechaEntradaSistema(Date fechaEntradaSistema) {
        this.fechaEntradaSistema = fechaEntradaSistema;
    }

    public Date getFechaUltimaModificacion() {
        return fechaUltimaModificacion;
    }

    public void setFechaUltimaModificacion(Date fechaUltimaModificacion) {
        this.fechaUltimaModificacion = fechaUltimaModificacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPublico() {
        return publico;
    }

    public void setPublico(int publico) {
        this.publico = publico;
    }
     public void setPublico(boolean publico) {
        this.publico = publico?1:0;
    }
    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public boolean isPublic()
    {
        return publico == 1;
    }

    public boolean isImagen() {
        return is_imgdoctext==IMG_CODE;
    }

    public void setIsImagen() {
        is_imgdoctext = IMG_CODE;
    }

    public boolean isDocument() {
        return is_imgdoctext==DOC_CODE;
    }

    public void setIsDocument() {
        is_imgdoctext = DOC_CODE;
    }
    public boolean isTexto() {
        return is_imgdoctext==TEXT_CODE;
    }

    public void setIsTexto() {
        is_imgdoctext = TEXT_CODE;
    }
    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Object clone() {
        DocumentBean  obj=null;
        try{
               obj=(DocumentBean)super.clone();
         }catch(CloneNotSupportedException ex){
           logger.error("Error al clonar el objeto ListaPermisos. "+ex.toString());
         }
         obj.id=id;
         obj.is_imgdoctext =is_imgdoctext;
         obj.publico = publico;
         obj.size = size;
         obj.comentario = comentario;
         obj.content= content;
         obj.fechaEntradaSistema =fechaEntradaSistema;
         obj.fechaUltimaModificacion =fechaUltimaModificacion;
         obj.fileName = fileName;
         obj.idMunicipio = idMunicipio;
         obj.idEntidad = idEntidad;
         obj.user=user;
         obj.thumbnail=thumbnail;
         obj.tipo=tipo;
         obj.oculto=oculto;
         obj.propietario=propietario;
        return obj;
    }

    public char getIs_imgdoctext() {
        return is_imgdoctext;
    }

    public void setIs_imgdoctext(char is_imgdoctext) {
        this.is_imgdoctext = is_imgdoctext;
    }

    public int getOculto() {
        return oculto;
    }

    public void setOculto(int oculto) {
        this.oculto = oculto;
    }
     public void setOculto(boolean oculto) {
        this.oculto = oculto?1:0;
    }

    public boolean isOculto()
    {
        return oculto == 1;
    }
    public Image getImage(int x, int y){
    	if (this.isImagen())
    	{
    		 try{
    			    if (!getFileName().startsWith(("http"))){
    			    	
    				
    				if (this.isAlfrescoUuid(this.id, this.idMunicipio)){
    					
    					String urlAlfresco=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL, "",false) + 
    						WebAppConstants.ALFRESCO_WEBAPP_NAME + ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME;
    						
    					DocumentClient documentClient= new DocumentClient(urlAlfresco);
    					documentClient.initializeRelativeDirectoryPathAndAccess(this.idMunicipio,AlfrescoConstants.APP_INVENTORY);
    					
    					AlfrescoKey key=new AlfrescoKey(this.id, AlfrescoKey.KEY_UUID);
	       		  		this.setContent(documentClient.getAttachedByteStreamFromAlfresco(key).getContent());
	       		  		return com.geopista.protocol.document.Thumbnail.escalarImagen(this.getContent(),x, y);
	       		  		
    				}
    				else{
	       		  		AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	       		  		String sUrl=aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
	       		  		ServletConstants.DOCUMENT_SERVLET_NAME;
	       		  		DocumentClient documentClient= new DocumentClient(sUrl);
	       		  		this.setContent(documentClient.getAttachedByteStream(this));
	       		  		return com.geopista.protocol.document.Thumbnail.escalarImagen(this.getContent(),x, y);
    				}
    			    }
    			    else{
    			    	InputStream is = null;
    			    	URL url = new URL(getFileName());
    			    	byte[] imageBytes=null;
    			    	try {    			    	  
    			    	  is = url.openStream ();
    			    	  imageBytes = IOUtils.toByteArray(is);
    			    	}
    			    	catch (IOException e) {
    			    	  logger.error("Error al leer los bytes de la imagen"+url.toExternalForm(),e);
    			    	}
    			    	finally {
    			    	  if (is != null) { is.close(); }
    			    	}
    			    	if (imageBytes!=null)
    			    		return com.geopista.protocol.document.Thumbnail.escalarImagen(imageBytes,x, y);
    			    	else
    			    		return null;
    			    }
	       		  		
        	 }catch(Exception e){
       			 logger.error("Error al tratar de obtener la imagen asociada al bien",e);
       		 }
    		 
    	}
    	return getIcon(x,y).getImage();
    }
    public ImageIcon getIcon(){
    	return getIcon(20,20);
    }
    public ImageIcon getIcon( int x, int y){
    	 String extension="";
         try
         {
             extension=this.getFileName().substring(this.getFileName().lastIndexOf(".")+1);
         }
         catch(Exception e)
         {
             //Excepción no tratada
         }
         if(extension.equalsIgnoreCase(WORD))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconWord;
         else if(extension.equalsIgnoreCase(PPT))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconPPT;
         else if(extension.equalsIgnoreCase(PDF))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconPDF;
         else if(extension.equalsIgnoreCase(TXT))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconTxt;
         else if(extension.equalsIgnoreCase(XML))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconXML;
         else if(extension.equalsIgnoreCase(HTML))
             return (ImageIcon)LCGIII_DocumentManagerPlugin.iconHTML;
         else if (this.isImagen()){
        	 	 try{
        			 return (new ImageIcon(com.geopista.protocol.document.Thumbnail.escalarImagen(this.getThumbnail(),x, y)));
        		 }catch(Exception e){}
         }
         return (ImageIcon)LCGIII_DocumentManagerPlugin.iconDefault;
    }
    public void setTypeByExtension(){
		 String extension="";
	     try
	     {
	            extension=this.getFileName().substring(this.getFileName().lastIndexOf(".")+1);
	            if (extension.equalsIgnoreCase(GIF) || extension.equalsIgnoreCase(PNG) ||
	            		extension.equalsIgnoreCase(BMP) || extension.equalsIgnoreCase(JPG)  )
	                setIsImagen();
	            else
		            setIsDocument();
	     }
	     catch(Exception e)
	     {
	            setIsDocument();
	     }
  }

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	/**
	 * Comprueba si el documento es de Alfresco
	 * @param uuid: Identificador unívoco del documento
	 * @param municipalityId: Identificador del municipio
	 * @return Boolean: Resultado de la comprobacion
	 */
	public Boolean isAlfrescoUuid(String uuid, String municipalityId){
		if (uuid!=null){
			if(municipalityId!=null){
				return !uuid.startsWith(municipalityId + "_");
			}
			else{
				return !uuid.contains("_");
			}			
		}
		return false;
	}
	
}
