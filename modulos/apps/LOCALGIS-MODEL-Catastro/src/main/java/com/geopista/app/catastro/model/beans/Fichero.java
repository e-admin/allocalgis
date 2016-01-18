/**
 * Fichero.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean que encapsula todos los datos de los ficheros de la aplicacion. Los fichero se generan cuando se produce un
 * envio o recepcion masiva de expediente por parte de la entidad generadora o catastro respectivamente.
 * */

public class Fichero  implements Serializable
{
	/**
	 * Identificador del fichero.
	 */
	private int idFichero;
	
	/**
	 * Identificador del tipo de fichero.
	 */
    private int idTipoFichero;
    
    /**
     * Nombre del fichero.
     */
	private String nombre;
	
	/**
	 * Descripción del fichero.
	 */
    private String descripcion;
    
    /**
     * Fecha de generación del fichero.
     */
	private Date fechaGeneracion;
	
	/**
	 * Fecha de intercambio del fichero.
	 */
	private Date fechaIntercambio;
	
	/**
	 * Contenido del fichero.
	 */
    private String contenido;
    
    /**
     * Url donde guardar los FIN de Retorno o de Entrada Masivos  
     */
    private String url;
    
    /**
     * Fecha de inicio del periodo.
     */
    private Date fechaInicioPeriodo;
    
    /**
     * Fecha de fin del periodo.
     */
    private Date fechaFinPeriodo;
    
    /**
     * Código de la entidad destinataria.
     */
    private int codigoEntidadDestinataria;
    
    /**
     * Código de la entidad generadora.
     */
    private int codigoEntidadGeneradora;
    
    /**
     * Código de envío.
     */
    private String codigoEnvio;

    /**
     * Entidad generadora del fichero
     */
    private EntidadGeneradora entidadGeneradora;

    public static final int FIN_ENTRADA=0;
    public static final int FIN_RETORNO=1;
    public static final int VARPAD=2;

    public static final String NOMBRE_FIN_ENTRDA = "FIN DE ENTRADA";
    public static final String NOMBRE_VARPAD = "VARPAD";

    public static final String DESCRIPCION_FIN_ENTRADA = "Fin de Entrada";
    public static final String DESCRIPCION_VARPAD = "Titularidad";
    
    private static final String NODO_FIN_ENTRADA="";
    //private static final String NODO_FIN_RETORNO="CCIR";
    private static final String NODO_FIN_RETORNO="CFIR";


    public Fichero()
    {

    }


    public Fichero(int idTipoFichero, String nombre, String descripcion, int codigoEntidadGeneradora) {
        this.idTipoFichero = idTipoFichero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.codigoEntidadGeneradora = codigoEntidadGeneradora;
        this.codigoEntidadDestinataria = EntidadGeneradora.CODIGO_CATASTRO;
        this.fechaFinPeriodo = new Date(System.currentTimeMillis());
        this.fechaInicioPeriodo = new Date(System.currentTimeMillis());
        this.fechaIntercambio = new Date(System.currentTimeMillis());
        this.fechaGeneracion = new Date(System.currentTimeMillis());
        this.url = "http://ovc.catastro.meh.es";
    }

    /**
     * Devuelve el identificador del fichero.
     * @return int con el identificador.
     */
    public int getIdFichero()
    {
        return idFichero;
    }

    /**
     * Guarda el identificador del fichero.
     * @param idFichero int con el identificador a cargar.
     */
    public void setIdFichero(int idFichero)
    {
        this.idFichero = idFichero;
    }

    /**
     * Devuelve el identificador del tipo del fichero.
     * @return int con el identificador del fichero.
     */
    public int getIdTipoFichero()
    {
        return idTipoFichero;
    }

    /**
     * Guarda el identificador del tipo del fichero.
     * @param idTipoFichero int con el identificador del tipo de fichero a cargar.
     */
    public void setIdTipoFichero(int idTipoFichero)
    {
        this.idTipoFichero = idTipoFichero;
    }

    /**
     * Devuelve el nombre del fichero.
     * @return String con el nombre.
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * Guarda el nombre del fichero.
     * @param nombre String con el nombre del fichero a cargar.
     */
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    /**
     * Devuelve la descripción del fichero.
     * @return String con la descripción.
     */
    public String getDescripcion()
    {
        return descripcion;
    }

    /**
     * Guarda la descripción del fichero.
     * @param descripcion String con la descripción del fichero a cargar.
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la fecha de generación del fichero.
     * @return Date con la fecha.
     */
    public Date getFechaGeneracion()
    {
        return fechaGeneracion;
    }

    /**
     * Guarda la fecha de generación del fichero.
     * @param fechaGeneracion Date con la fecha de generación a cargar.
     */
    public void setFechaGeneracion(Date fechaGeneracion)
    {
        this.fechaGeneracion = fechaGeneracion;
    }

    /**
     * Devuelve la fecha de intercambio del fichero.
     * @return Date con la fecha de intercambio.
     */
    public Date getFechaIntercambio()
    {
        return fechaIntercambio;
    }

    /**
     * Guarda la fecha de intercambio del fichero.
     * @param fechaIntercambio Date con la fecha de intercambio a cargar.
     */
    public void setFechaIntercambio(Date fechaIntercambio)
    {
        this.fechaIntercambio = fechaIntercambio;
    }

    /**
     * Devuelve el contenido del fichero.
     * @return String con el contenido del fichero.
     */
    public String getContenido()
    {
        return contenido;
    }

    /**
     * Guarda el contenido del fichero.
     * @param contenido String con el contenido del fichero a cargar.
     */
    public void setContenido(String contenido)
    {
        this.contenido = contenido;
    }

    /**
     * Devuelve la url del fichero.
     * @return String con la url del fichero.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Guarda la url del fichero.
     * @param url String con la url a cargar.
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Devuelve la fecha de incio del periodo.
     * @return Date con la fecha de inicio.
     */
    public Date getFechaInicioPeriodo()
    {
        return fechaInicioPeriodo;
    }

    /**
     * Guarda la fecha de inicio del periodo.
     * @param fechaInicioPeriodo Date con la fecha de inicio del periodo a cargar.
     */
    public void setFechaInicioPeriodo(Date fechaInicioPeriodo)
    {
        this.fechaInicioPeriodo = fechaInicioPeriodo;
    }

    /**
     * Devuelve la fecha de fin del periodo.
     * @return Date con la fecha de fin de periodo.
     */
    public Date getFechaFinPeriodo()
    {
        return fechaFinPeriodo;
    }

    /**
     * Guarda la fecha de fin de periodo del fichero.
     * @param fechaFinPeriodo Date con la fecga de fin de periodo a cargar.
     */
    public void setFechaFinPeriodo(Date fechaFinPeriodo)
    {
        this.fechaFinPeriodo = fechaFinPeriodo;
    }

    /**
     * Devuelve el código de la entidad Destinataria del fichero.
     * @return int con el código de la entidad destinataria.
     */
    public int getCodigoEntidadDestinataria()
    {
        return codigoEntidadDestinataria;
    }

    /**
     * Guarda el código de la entidad destinataria del fichero.
     * @param codigoEntidadDestinataria int con el código de la entidad destinataria a cargar.
     */
    public void setCodigoEntidadDestinataria(int codigoEntidadDestinataria)
    {
        this.codigoEntidadDestinataria = codigoEntidadDestinataria;
    }

    /**
     * Devuelve el código de la entidad generadora.
     * @return int con el código de la entidad generadora.
     */
    public int getCodigoEntidadGeneradora()
    {
        return codigoEntidadGeneradora;
    }

    /**
     * Guarda el código de la entidad generadora.
     * @param codigoEntidadGeneradora int con el código de la entidad generadora a cargar.
     */
    public void setCodigoEntidadGeneradora(int codigoEntidadGeneradora)
    {
        this.codigoEntidadGeneradora = codigoEntidadGeneradora;
    }

    /**
     * Devuelve el código del envio.
     * @return String con el código de envío.
     */
    public String getCodigoEnvio()
    {
        return codigoEnvio;
    }

    /**
     * Guarda el código de envío.
     * @param codigoEnvio String con el código de envío a cargar.
     */
    public void setCodigoEnvio(String codigoEnvio)
    {
        this.codigoEnvio = codigoEnvio;
    }

    /**
     * Devuelve la entidad generadora del fichero.
     * @return EntidadGeneradora del fichero.
     */
    public EntidadGeneradora getEntidadGeneradora()
    {
        return entidadGeneradora;
    }

    /**
     * Guarda la entidad generadora del fichero
     * @param entidadGeneradora EntidadGeneradora a cargar.
     */
    public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora)
    {
        this.entidadGeneradora = entidadGeneradora;
    }}