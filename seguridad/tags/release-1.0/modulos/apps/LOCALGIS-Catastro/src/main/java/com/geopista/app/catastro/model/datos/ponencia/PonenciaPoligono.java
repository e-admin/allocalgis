package com.geopista.app.catastro.model.datos.ponencia;

import java.io.Serializable;

public class PonenciaPoligono extends Ponencia implements Serializable {
            
    private String codPoligono;
    
    private Double importeMBC;
    
    private Double importeMBR;
    
    private Integer codMBCPlan;
    
    private Integer grupoPlan;
    
    private String usoPlan;    
    
    private Float modPlan;
    
    private Float coefCoordPlan;
    
    private Float edifPlan;
    
    private Double vrb;
    
    private Float flGB;
    
    private String zonaVUB;
    
    private String zonaVRB;
    
    private String diseminado;
    
    private Float flGBUni;
    
    private Integer anioNormas;
    
    private Integer anioRevision;
    
    
  
    public PonenciaPoligono()
    {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @return the codMBCPlan
	 */
	public Integer getCodMBCPlan() {
		return codMBCPlan;
	}

	/**
	 * @param codMBCPlan the codMBCPlan to set
	 */
	public void setCodMBCPlan(Integer codMBCPlan) {
		this.codMBCPlan = codMBCPlan;
	}

	/**
	 * @return the codPoligono
	 */
	public String getCodPoligono() {
		return codPoligono;
	}

	/**
	 * @param codPoligono the codPoligono to set
	 */
	public void setCodPoligono(String codPoligono) {
		this.codPoligono = codPoligono;
	}

	/**
	 * @return the coefCoordPlan
	 */
	public Float getCoefCoordPlan() {
		return coefCoordPlan;
	}

	/**
	 * @param coefCoordPlan the coefCoordPlan to set
	 */
	public void setCoefCoordPlan(Float coefCoordPlan) {
		this.coefCoordPlan = coefCoordPlan;
	}

	/**
	 * @return the diseminado
	 */
	public String getDiseminado() {
		return diseminado;
	}

	/**
	 * @param diseminado the diseminado to set
	 */
	public void setDiseminado(String diseminado) {
		this.diseminado = diseminado;
	}

	/**
	 * @return the edifPlan
	 */
	public Float getEdifPlan() {
		return edifPlan;
	}

	/**
	 * @param edifPlan the edifPlan to set
	 */
	public void setEdifPlan(Float edifPlan) {
		this.edifPlan = edifPlan;
	}

	/**
	 * @return the flGB
	 */
	public Float getFlGB() {
		return flGB;
	}

	/**
	 * @param flGB the flGB to set
	 */
	public void setFlGB(Float flGB) {
		this.flGB = flGB;
	}

	/**
	 * @return the flGBUni
	 */
	public Float getFlGBUni() {
		return flGBUni;
	}

	/**
	 * @param flGBUni the flGBUni to set
	 */
	public void setFlGBUni(Float flGBUni) {
		this.flGBUni = flGBUni;
	}

	/**
	 * @return the grupoPlan
	 */
	public Integer getGrupoPlan() {
		return grupoPlan;
	}

	/**
	 * @param grupoPlan the grupoPlan to set
	 */
	public void setGrupoPlan(Integer grupoPlan) {
		this.grupoPlan = grupoPlan;
	}

	/**
	 * @return the importeMBC
	 */
	public Double getImporteMBC() {
		return importeMBC;
	}

	/**
	 * @param importeMBC the importeMBC to set
	 */
	public void setImporteMBC(Double importeMBC) {
		this.importeMBC = importeMBC;
	}

	/**
	 * @return the importeMBR
	 */
	public Double getImporteMBR() {
		return importeMBR;
	}


	/**
	 * @param importeMBR the importeMBR to set
	 */
	public void setImporteMBR(Double importeMBR) {
		this.importeMBR = importeMBR;
	}

	/**
	 * @return the modPlan
	 */
	public Float getModPlan() {
		return modPlan;
	}

	/**
	 * @param modPlan the modPlan to set
	 */
	public void setModPlan(Float modPlan) {
		this.modPlan = modPlan;
	}

	/**
	 * @return the usoPlan
	 */
	public String getUsoPlan() {
		return usoPlan;
	}


	/**
	 * @param usoPlan the usoPlan to set
	 */
	public void setUsoPlan(String usoPlan) {
		this.usoPlan = usoPlan;
	}


	/**
	 * @return the vrb
	 */
	public Double getVrb() {
		return vrb;
	}

	/**
	 * @param vrb the vrb to set
	 */
	public void setVrb(Double vrb) {
		this.vrb = vrb;
	}

	/**
	 * @return the zonaVRB
	 */
	public String getZonaVRB() {
		return zonaVRB;
	}

	/**
	 * @param zonaVRB the zonaVRB to set
	 */
	public void setZonaVRB(String zonaVRB) {
		this.zonaVRB = zonaVRB;
	}

	/**
	 * @return the zonaVUB
	 */
	public String getZonaVUB() {
		return zonaVUB;
	}

	/**
	 * @param zonaVUB the zonaVUB to set
	 */
	public void setZonaVUB(String zonaVUB) {
		this.zonaVUB = zonaVUB;
	}


	public Integer getAnioNormas() {
		return anioNormas;
	}


	public void setAnioNormas(Integer anioNormas) {
		this.anioNormas = anioNormas;
	}


	public Integer getAnioRevision() {
		return anioRevision;
	}


	public void setAnioRevision(Integer anioRevision) {
		this.anioRevision = anioRevision;
	}
    
}
