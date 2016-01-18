package com.geopista.app.catastro.model.datos.ponencia;

import java.io.Serializable;

public class Ponencia implements Serializable
{
    
    private String idMunicipio;
    private String codDelegacionMEH;
    private String codProvinciaINE;
    private String codMunicipioINE;
    private String codMunicipioMEH;
    private Integer anioAprobacion;
    
    public Ponencia()
    {
        super();        
    }
    
    /**
     * @return Returns the anioAprobacion.
     */
    public Integer getAnioAprobacion()
    {
        return anioAprobacion;
    }
    
    
    /**
     * @param anioAprobacion The anioAprobacion to set.
     */
    public void setAnioAprobacion(Integer anioAprobacion)
    {
        this.anioAprobacion = anioAprobacion;
    }
    
    
    
    /**
     * @return Returns the codDelegacionMEH.
     */
    public String getCodDelegacionMEH()
    {
        return codDelegacionMEH;
    }
    
    
    
    /**
     * @param codDelegacionMEH The codDelegacionMEH to set.
     */
    public void setCodDelegacionMEH(String codDelegacionMEH)
    {
        this.codDelegacionMEH = codDelegacionMEH;
    }
    
    
    
    /**
     * @return Returns the codMunicipioINE.
     */
    public String getCodMunicipioINE()
    {
        return codMunicipioINE;
    }
    
    
    
    /**
     * @param codMunicipioINE The codMunicipioINE to set.
     */
    public void setCodMunicipioINE(String codMunicipioINE)
    {
        this.codMunicipioINE = codMunicipioINE;
    }
    
    
    /**
     * @return Returns the codProvinciaINE.
     */
    public String getCodProvinciaINE()
    {
        return codProvinciaINE;
    }
    
    
    /**
     * @param codProvinciaINE The codProvinciaINE to set.
     */
    public void setCodProvinciaINE(String codProvinciaINE)
    {
        this.codProvinciaINE = codProvinciaINE;
    }
    
    
    /**
     * @return Returns the idMunicipio.
     */
    public String getIdMunicipio()
    {
        return idMunicipio;
    }
    
    
    /**
     * @param idMunicipio The idMunicipio to set.
     */
    public void setIdMunicipio(String idMunicipio)
    {
        this.idMunicipio = idMunicipio;
    }

	/**
	 * @return the codMunicipioMEH
	 */
	public String getCodMunicipioMEH() {
		return codMunicipioMEH;
	}

	/**
	 * @param codMunicipioMEH the codMunicipioMEH to set
	 */
	public void setCodMunicipioMEH(String codMunicipioMEH) {
		this.codMunicipioMEH = codMunicipioMEH;
	}
    
}
