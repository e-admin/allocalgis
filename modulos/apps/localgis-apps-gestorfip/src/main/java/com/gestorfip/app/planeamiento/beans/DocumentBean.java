/**
 * DocumentBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gestorfip.app.planeamiento.beans.DocumentBean;

import java.io.Serializable;
import java.util.Date;


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
    private long id= -1;
    private long size= -1;
    private String fileName;
    private Date fechaEntradaSistema;
    private Date fechaUltimaModificacion;  
    private String tipo;
    private String comentario;
    private byte[] content;
    private int publico= 0;
    private String idMunicipio;
    private char is_imgdoctext= 'D';
    private byte[] thumbnail;
    private int oculto= 0;



    public long getId() {
        return id;
    }

    public void setId(long id) {
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
         obj.thumbnail=thumbnail;
         obj.tipo=tipo;
         obj.oculto=oculto;
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




}
