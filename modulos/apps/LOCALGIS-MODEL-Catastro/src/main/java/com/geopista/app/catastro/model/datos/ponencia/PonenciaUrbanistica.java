/**
 * PonenciaUrbanistica.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.ponencia;

public class PonenciaUrbanistica extends Ponencia
{
    
    private String codZona;
    private String denominacion;
    private String codCalificacion;
    private String codZonificacion;
    private String codOcupacion;
    private String codOrdenacion;
    private Integer longitud;
    private Integer fondo;
    private Integer supMinima;
    private Float numPlantas;    
    private Float numPlantasSolatInt;
    private TipoValor edificabilidad;
    
    
    public PonenciaUrbanistica()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * @return Returns the codCalificacion.
     */
    public String getCodCalificacion()
    {
        return codCalificacion;
    }
    
    
    /**
     * @param codCalificacion The codCalificacion to set.
     */
    public void setCodCalificacion(String codCalificacion)
    {
        this.codCalificacion = codCalificacion;
    }
    
    
    /**
     * @return Returns the codOcupacion.
     */
    public String getCodOcupacion()
    {
        return codOcupacion;
    }
    
    
    /**
     * @param codOcupacion The codOcupacion to set.
     */
    public void setCodOcupacion(String codOcupacion)
    {
        this.codOcupacion = codOcupacion;
    }
    
    
    /**
     * @return Returns the codOrdenacion.
     */
    public String getCodOrdenacion()
    {
        return codOrdenacion;
    }
    
    
    /**
     * @param codOrdenacion The codOrdenacion to set.
     */
    public void setCodOrdenacion(String codOrdenacion)
    {
        this.codOrdenacion = codOrdenacion;
    }
    
    
    /**
     * @return Returns the codZona.
     */
    public String getCodZona()
    {
        return codZona;
    }
    
    
    /**
     * @param codZona The codZona to set.
     */
    public void setCodZona(String codZona)
    {
        this.codZona = codZona;
    }
    
    
    /**
     * @return Returns the codZonificacion.
     */
    public String getCodZonificacion()
    {
        return codZonificacion;
    }
    
    
    /**
     * @param codZonificacion The codZonificacion to set.
     */
    public void setCodZonificacion(String codZonificacion)
    {
        this.codZonificacion = codZonificacion;
    }
    
    
    /**
     * @return Returns the denominacion.
     */
    public String getDenominacion()
    {
        return denominacion;
    }
    
    
    /**
     * @param denominacion The denominacion to set.
     */
    public void setDenominacion(String denominacion)
    {
        this.denominacion = denominacion;
    }
    
    
    /**
     * @return Returns the edificabilidad.
     */
    public TipoValor getEdificabilidad()
    {
        return edificabilidad;
    }
    
    
    /**
     * @param edificabilidad The edificabilidad to set.
     */
    public void setEdificabilidad(TipoValor edificabilidad)
    {
        this.edificabilidad = edificabilidad;
    }
    
    
    /**
     * @return Returns the fondo.
     */
    public Integer getFondo()
    {
        return fondo;
    }
    
    
    /**
     * @param fondo The fondo to set.
     */
    public void setFondo(Integer fondo)
    {
        this.fondo = fondo;
    }
    
    
    /**
     * @return Returns the longitud.
     */
    public Integer getLongitud()
    {
        return longitud;
    }
    
    
    /**
     * @param longitud The longitud to set.
     */
    public void setLongitud(Integer longitud)
    {
        this.longitud = longitud;
    }
    
    
    /**
     * @return Returns the numPlantas.
     */
    public Float getNumPlantas()
    {
        return numPlantas;
    }
    
    
    /**
     * @param numPlantas The numPlantas to set.
     */
    public void setNumPlantas(Float numPlantas)
    {
        this.numPlantas = numPlantas;
    }
    
        
    /**
     * @return Returns the supMinima.
     */
    public Integer getSupMinima()
    {
        return supMinima;
    }
    
    
    /**
     * @param supMinima The supMinima to set.
     */
    public void setSupMinima(Integer supMinima)
    {
        this.supMinima = supMinima;
    }


	/**
	 * @return the numPlantasSolatInt
	 */
	public Float getNumPlantasSolatInt() {
		return numPlantasSolatInt;
	}


	/**
	 * @param numPlantasSolatInt the numPlantasSolatInt to set
	 */
	public void setNumPlantasSolatInt(Float numPlantasSolatInt) {
		this.numPlantasSolatInt = numPlantasSolatInt;
	}
    
}
