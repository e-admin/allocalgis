/**
 * 
 */
package com.geopista.protocol.administrador;


/**
 * @author seilagamo
 *
 */
public class EntidadMunicipio {
    
    private String id_entidad;
    private String id_municipio;
    private int srid;
    
    public EntidadMunicipio(){        
    }
    
    public EntidadMunicipio (String id_entidad, String id_municipio) {
        this.id_entidad = id_entidad;
        this.id_municipio = id_municipio;
    }

    public EntidadMunicipio (String id_entidad, String id_municipio, int srid) {
        this.id_entidad = id_entidad;
        this.id_municipio = id_municipio;
        this.srid = srid;
    }
    
    /**
     * @return the id_entidad
     */
    public String getId_entidad() {
        return id_entidad;
    }

    
    /**
     * @param id_entidad the id_entidad to set
     */
    public void setId_entidad(String id_entidad) {
        this.id_entidad = id_entidad;
    }

    
    /**
     * @return the id_municipio
     */
    public String getId_municipio() {
        return id_municipio;
    }

    
    /**
     * @param id_municipio the id_municipio to set
     */
    public void setId_municipio(String id_municipio) {
        this.id_municipio = id_municipio;
    }

    
    /**
     * @return the srid
     */
    public int getSrid() {
        return srid;
    }

    
    /**
     * @param srid the srid to set
     */
    public void setSrid(int srid) {
        this.srid = srid;
    }
}
