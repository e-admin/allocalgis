/**
 * PonenciaTramos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.ponencia;

public class PonenciaTramos extends Ponencia
{
    
    private String codVia;
    private String codTramo;
    private String denominacion;
    private PonenciaPoligono ponPoligono;
    private PonenciaUrbanistica ponUrbanistica;    
    private String situacion;
    private Integer maxPar;
    private String cMaxPar;
    private Integer minPar;
    private String cMinPar;
    private Integer maxImpar;
    private String cMaxImpar;
    private Integer minImpar;
    private String cMinImpar;
    private Double valorUnitario;
    private TipoValor banda;
    private Float corrApDepSuelo;
    private Float corrApDepConst;
    private String valorSuelo;
    private String agua;
    private String electricidad;
    private String alumbrado;
    private String desmonte;
    private String pavimentacion;
    private String alcantarillado;
    private Double costesUrbanizacion;
    
    public PonenciaTramos()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @return Returns the agua.
     */
    public String getAgua()
    {
        return agua;
    }
    
    /**
     * @param agua The agua to set.
     */
    public void setAgua(String agua)
    {
        this.agua = agua;
    }
    
    /**
     * @return Returns the alcantarillado.
     */
    public String getAlcantarillado()
    {
        return alcantarillado;
    }
    
    /**
     * @param alcantarillado The alcantarillado to set.
     */
    public void setAlcantarillado(String alcantarillado)
    {
        this.alcantarillado = alcantarillado;
    }
    
    /**
     * @return Returns the alumbrado.
     */
    public String getAlumbrado()
    {
        return alumbrado;
    }
    
    /**
     * @param alumbrado The alumbrado to set.
     */
    public void setAlumbrado(String alumbrado)
    {
        this.alumbrado = alumbrado;
    }
    
    /**
     * @return Returns the banda.
     */
    public TipoValor getBanda()
    {
        return banda;
    }
    
    /**
     * @param banda The banda to set.
     */
    public void setBanda(TipoValor banda)
    {
        this.banda = banda;
    }
    
    /**
     * @return Returns the cMaxPar.
     */
    public String getCMaxPar()
    {
        return cMaxPar;
    }
    
    /**
     * @param maxPar The cMaxPar to set.
     */
    public void setCMaxPar(String maxPar)
    {
        cMaxPar = maxPar;
    }
    
    /**
     * @return Returns the cMinPar.
     */
    public String getCMinPar()
    {
        return cMinPar;
    }
    
    /**
     * @param minPar The cMinPar to set.
     */
    public void setCMinPar(String minPar)
    {
        cMinPar = minPar;
    }
    
    /**
     * @return Returns the codTramo.
     */
    public String getCodTramo()
    {
        return codTramo;
    }
    
    /**
     * @param codTramo The codTramo to set.
     */
    public void setCodTramo(String codTramo)
    {
        this.codTramo = codTramo;
    }
    
    /**
     * @return Returns the codVia.
     */
    public String getCodVia()
    {
        return codVia;
    }
    
    /**
     * @param codVia The codVia to set.
     */
    public void setCodVia(String codVia)
    {
        this.codVia = codVia;
    }
    
        
    /**
     * @return Returns the costesUrbanizacion.
     */
    public Double getCostesUrbanizacion()
    {
        return costesUrbanizacion;
    }
    
    /**
     * @param costesUrbanizacion The costesUrbanizacion to set.
     */
    public void setCostesUrbanizacion(Double costesUrbanizacion)
    {
        this.costesUrbanizacion = costesUrbanizacion;
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
     * @return Returns the desmonte.
     */
    public String getDesmonte()
    {
        return desmonte;
    }
    
    /**
     * @param desmonte The desmonte to set.
     */
    public void setDesmonte(String desmonte)
    {
        this.desmonte = desmonte;
    }
    
    /**
     * @return Returns the electricidad.
     */
    public String getElectricidad()
    {
        return electricidad;
    }
    
    /**
     * @param electricidad The electricidad to set.
     */
    public void setElectricidad(String electricidad)
    {
        this.electricidad = electricidad;
    }
    
    /**
     * @return Returns the maxPar.
     */
    public Integer getMaxPar()
    {
        return maxPar;
    }
    
    /**
     * @param maxPar The maxPar to set.
     */
    public void setMaxPar(Integer maxPar)
    {
        this.maxPar = maxPar;
    }
    
    /**
     * @return Returns the minPar.
     */
    public Integer getMinPar()
    {
        return minPar;
    }
    
    /**
     * @param minPar The minPar to set.
     */
    public void setMinPar(Integer minPar)
    {
        this.minPar = minPar;
    }
    
    /**
     * @return Returns the pavimentacion.
     */
    public String getPavimentacion()
    {
        return pavimentacion;
    }
    
    /**
     * @param pavimentacion The pavimentacion to set.
     */
    public void setPavimentacion(String pavimentacion)
    {
        this.pavimentacion = pavimentacion;
    }
    
    /**
     * @return Returns the ponPoligono.
     */
    public PonenciaPoligono getPonPoligono()
    {
        return ponPoligono;
    }
    
    /**
     * @param ponPoligono The ponPoligono to set.
     */
    public void setPonPoligono(PonenciaPoligono ponPoligono)
    {
        this.ponPoligono = ponPoligono;
    }
    
    /**
     * @return Returns the ponUrbanistica.
     */
    public PonenciaUrbanistica getPonUrbanistica()
    {
        return ponUrbanistica;
    }
    
    /**
     * @param ponUrbanistica The ponUrbanistica to set.
     */
    public void setPonUrbanistica(PonenciaUrbanistica ponUrbanistica)
    {
        this.ponUrbanistica = ponUrbanistica;
    }
    
    /**
     * @return Returns the situacion.
     */
    public String getSituacion()
    {
        return situacion;
    }
    
    /**
     * @param situacion The situacion to set.
     */
    public void setSituacion(String situacion)
    {
        this.situacion = situacion;
    }
    
    /**
     * @return Returns the valorSuelo.
     */
    public String getValorSuelo()
    {
        return valorSuelo;
    }
    
    /**
     * @param valorSuelo The valorSuelo to set.
     */
    public void setValorSuelo(String valorSuelo)
    {
        this.valorSuelo = valorSuelo;
    }
    
    /**
     * @return Returns the valorUnitario.
     */
    public Double getValorUnitario()
    {
        return valorUnitario;
    }
    
    /**
     * @param valorUnitario The valorUnitario to set.
     */
    public void setValorUnitario(Double valorUnitario)
    {
        this.valorUnitario = valorUnitario;
    }

	/**
	 * @return the cMaxImpar
	 */
	public String getCMaxImpar() {
		return cMaxImpar;
	}

	/**
	 * @param maxImpar the cMaxImpar to set
	 */
	public void setCMaxImpar(String maxImpar) {
		cMaxImpar = maxImpar;
	}

	/**
	 * @return the cMinImpar
	 */
	public String getCMinImpar() {
		return cMinImpar;
	}

	/**
	 * @param minImpar the cMinImpar to set
	 */
	public void setCMinImpar(String minImpar) {
		cMinImpar = minImpar;
	}

	/**
	 * @return the maxImpar
	 */
	public Integer getMaxImpar() {
		return maxImpar;
	}

	/**
	 * @param maxImpar the maxImpar to set
	 */
	public void setMaxImpar(Integer maxImpar) {
		this.maxImpar = maxImpar;
	}

	/**
	 * @return the minImpar
	 */
	public Integer getMinImpar() {
		return minImpar;
	}

	/**
	 * @param minImpar the minImpar to set
	 */
	public void setMinImpar(Integer minImpar) {
		this.minImpar = minImpar;
	}

	/**
	 * @return the corrApDepConst
	 */
	public Float getCorrApDepConst() {
		return corrApDepConst;
	}

	/**
	 * @param corrApDepConst the corrApDepConst to set
	 */
	public void setCorrApDepConst(Float corrApDepConst) {
		this.corrApDepConst = corrApDepConst;
	}

	/**
	 * @return the corrApDepSuelo
	 */
	public Float getCorrApDepSuelo() {
		return corrApDepSuelo;
	}

	/**
	 * @param corrApDepSuelo the corrApDepSuelo to set
	 */
	public void setCorrApDepSuelo(Float corrApDepSuelo) {
		this.corrApDepSuelo = corrApDepSuelo;
	}
    
}
