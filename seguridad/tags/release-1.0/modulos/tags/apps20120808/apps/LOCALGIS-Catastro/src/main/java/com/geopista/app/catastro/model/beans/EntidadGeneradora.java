package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * Bean que encapsula todos los datos de las entidades generadoras de la aplicacion.
 * */

public class EntidadGeneradora implements Serializable {
	/**
	 * Código de la entidad generadora.
	 */
    private int codigo;
    
    /**
     * Tipo de la entidad generadora: A, C, E.
     */
    private String tipo;
    
    /**
     * Descripción de la entidad generadora.
     */
    private String descripcion;
    
    /**
     * Nombre de la entidad generadora.
     */
    private String nombre;
    
    /**
     * identificador de la entidad generadora.
     */
    private long idEntidadGeneradora;

    /**
     * Tipos de entidad generadora: Ayuntamientos
     */
    private static final char AYUNTAMIENTO = 'A';
    /**
     * Tipos de entidad generadora: Entidades con convenio
     */
    private static final char CONVENIO = 'C';
    /**
     * Tipos de entidad generadora: Empresas
     */
    private static final char EMPRESA = 'E';

    public static final int CODIGO_CATASTRO = 998;

    public EntidadGeneradora(){ }

    /**
     * Devuelve el código de la entidad generadora.
     * @return int con el código de la entidad.
     */
    public int getCodigo(){
        return codigo;
    }

    /**
     * Guarda el código de la entidad generadora.
     * @param codigo int con el código a cargar.
     */
    public void setCodigo(int codigo){
        this.codigo = codigo;
    }

    /**
     * Devuelve el tipo de la entidad generadora.
     * @return String el tipo de la entidad.
     */
    public String getTipo(){
        return tipo;
    }

    /**
     * Guarda el tipo de la entidad generadora.
     * @param tipo String con el tipo de la entidad a cargar.
     */
    public void setTipo(String tipo){
    	if(tipo!=null){
    		this.tipo = tipo.toUpperCase();
    	}
    }

    /**
     * Devuelve la descripción de la entidad generadora.
     * @return String con la descripción de la entidad.
     */
    public String getDescripcion(){
        return descripcion;
    }

    /**
     * Guarda la descripción de la entidad generadora.
     * @param descripcion String con la descripción de la entidad a cargar.
     */
    public void setDescripcion(String descripcion){
    	if (descripcion!=null){
    		this.descripcion = descripcion.toUpperCase();
    	}
    }

    /**
     * Devuelve el nombre de la entidad generadora.
     * @return String con el nombre de la entidad.
     */
    public String getNombre(){
        return nombre;
    }

    /**
     * Guarda el nombre de la entidad generadora.
     * @param nombre String con el nombre de la entidad a cargar.
     */
    public void setNombre(String nombre){
    	if(nombre!=null){
    		this.nombre = nombre.toUpperCase();
    	}
    }

    /**
     * Devuelve el identificador de la entidad generadora.
     * @return long con el identificador de la entidad.
     */
    public long getIdEntidadGeneradora(){
        return idEntidadGeneradora;
    }

    /**
     * Guarda el identificador de la entidad generadora.
     * @param idEntidadGeneradora long el identificador de la entidad a cargar.
     */
    public void setIdEntidadGeneradora(long idEntidadGeneradora){
        this.idEntidadGeneradora = idEntidadGeneradora;
    }
}
