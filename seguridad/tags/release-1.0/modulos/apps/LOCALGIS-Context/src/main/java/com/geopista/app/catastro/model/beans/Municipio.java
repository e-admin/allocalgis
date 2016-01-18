package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;


public class Municipio implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Identificador del municipio en base de datos. Corresponde
     * al identificador de provincia seguido del identificador del
     * municipio de acuerdo con el INE. En total, 5 posiciones
     */
    private int id;
    
    /**
     * Provincia a la que pertenece el municipio
     */
    private transient Provincia provincia = new Provincia();    
    /**
     * Nombre oficial del municipio
     */
    private transient String nombreOficial=new String();
    /**
     * Identificador del municipio de acuerdo a datos de la DGC
     */
    private String idCatastro = new String();
    /**
     * Identificador del municipio de acuerdo a datos del INE
     */
    private String idIne = new String();
    /**
     * Geometria del municipio
     */
    
    private transient String geom = null;
    private String srid = null;
    
    public Municipio()
    {
        
    }
    public Municipio(String id, String nombreOficial, String srid)
    {
       this.id = Integer.parseInt(id);
       this.nombreOficial = nombreOficial;
       this.srid = srid;
    }

    public Municipio(String id, String nombreOficial, String geom,String srid)
    {
       this.id = Integer.parseInt(id);
       this.nombreOficial = nombreOficial;
       this.geom=geom;
       this.srid = srid;
    }

    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return Returns the idCatastro.
     */
    public String getIdCatastro()
    {
        return idCatastro;
    }

    /**
     * @param idCatastro The idCatastro to set.
     */
    public void setIdCatastro(String idCatastro)
    {
        this.idCatastro = idCatastro;
    }

    /**
     * @return Returns the idIne.
     */
    public String getIdIne()
    {
        return idIne;
    }

    /**
     * @param idIne The idIne to set.
     */
    public void setIdIne(String idIne)
    {
        this.idIne = idIne;
    }

    /**
     * @return Returns the nomOficial.
     */
    public String getNombreOficial()
    {
        return nombreOficial;
    }

    /**
     * @param nomOficial The nomOficial to set.
     */
    public void setNombreOficial(String nomOficial)
    {
        this.nombreOficial = nomOficial;
    }

    /**
     * @return Returns the provincia.
     */
    public Provincia getProvincia()
    {
        return provincia;
    }

    /**
     * @param provincia The provincia to set.
     */
    public void setProvincia(Provincia provincia)
    {
        this.provincia = provincia;
    }
    
    /**
     * @return Returns the geometry.
     */    
    public String getGeometry()
    {
        return geom;
    }

    /**
     * @param geom The geometry to set.
     */
    public void setGeometry(String geom)
    {
        this.geom = geom;
    }

    /**
     * @return Returns the srid.
     */
    public String getSrid()
    {
        return srid;
    }

    /**
     * @param srid The srid to set.
     */
    public void setSrid(String srid)
    {
        this.srid = srid;
    }

    public boolean equals (Object o)
    {
        if (!(o instanceof Municipio))
            return false;
        
        if (((Municipio)o).getProvincia()!=null &&
                ((Municipio)o).getProvincia().equals(this.provincia)
                && ((Municipio)o).getIdIne().equals(this.idIne))
        {
            setNombreOficial(((Municipio)o).getNombreOficial());
            return true;
        }            
        else 
            return false;
    }    
    
}
