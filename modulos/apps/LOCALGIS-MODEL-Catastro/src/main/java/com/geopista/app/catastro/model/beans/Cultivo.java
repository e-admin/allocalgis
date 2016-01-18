/**
 * Cultivo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.AppContext;


public class Cultivo implements Serializable
{
	public static final String TIPO_MOVIMIENTO_FINAL ="F";
	public static final String TIPO_MOVIMIENTO_ALTA ="A";
	public static final String TIPO_MOVIMIENTO_BAJA ="B";
	public static final String TIPO_MOVIMIENTO_MODIF ="M";
	public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
	 
    /**
     * Id del cultivo
     */
    private IdCultivo idCultivo = new IdCultivo();
    /**
     * Datos relativos al expediente
     */
    private Expediente datosExpediente = new Expediente();
    
    /**
     * Código de la delegación
     */
    private String codDelegacionMEH;
    
    /**
     * Código de municipio según DGC
     */
    private String codMunicipioDGC;
    /**
     * Naturaleza del suelo
     */
    private String tipoSuelo;
    
    
    /**
     * Código de la subparcela 
     */
    private String codSubparcela;
    
    /**
     * Tipo de subparcela
     */
    private String tipoSubparcela;
    /**
     * Superficie de la subparcela
     */
    private Long superficieParcela = new Long(0); 
    
   
    /**
     * Denominación de la clase de cultivo
     */
    private String denominacionCultivo;
    
    /**
     * Intensidad productiva
     */
    private Integer intensidadProductiva;
    /**
     * Código de bonificacion
     */
    private String codBonificacion;
    
    /**
     * Año de fin de la bonificación
     */
    private Integer ejercicioFinBonificacion = null;
    /**
     * Valor catastral
     */
    private Double valorCatastral = new Double(0);
    
    /**
     * Código de modalidad de reparto, que se informará solamente si el cultivo es elemento
     * común. La composición del código es la siguiente:
     * Primer carácter: a todos o a alguno (T/A),
     * segundo carácter: cargos (C), 
     * tercer carácter: proporción del reparto, por partes iguales, por superficie de los locales, 
     * por coeficientes de propiedad, o por coeficientes específicamente determinados (1/2/3/4)
     */
    private String codModalidadReparto;
    
    private ArrayList lstRepartos = new ArrayList();
    
    // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
    /**
     * Constructor por defecto
     *
     */
    public Cultivo()
    {
        
    }    
    
    
    /**
     * @return Returns the codBonificacion.
     */
    public String getCodBonificacion()
    {
        return codBonificacion;
    }
    
    
    
    
    public String getTIPO_MOVIMIENTO() {
		//return "F";
    	return TIPO_MOVIMIENTO;
	}


	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
		
		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//expediente de variaciones
			if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_ALTA) || 
					TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_MODIF)){
				//se marca que el elemento esta modificado, por lo tanto es editable
				setElementoModificado(true);
				
			}
			else if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_BAJA)){
				setElementoModificado(false);
			}
		}
	}


	/**
     * @param codBonificacion The codBonificacion to set.
     */
    public void setCodBonificacion(String codBonificacion)
    {
        this.codBonificacion = codBonificacion;
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
     * @return Returns the codModalidadReparto.
     */
    public String getCodModalidadReparto()
    {
        return codModalidadReparto;
    }
    
    
    
    /**
     * @param codModalidadReparto The codModalidadReparto to set.
     */
    public void setCodModalidadReparto(String codModalidadReparto)
    {
        this.codModalidadReparto = codModalidadReparto;
    }
    
    
    
    /**
     * @return Returns the codMunicipioDGC.
     */
    public String getCodMunicipioDGC()
    {
        return codMunicipioDGC;
    }
    
    
    
    /**
     * @param codMunicipioDGC The codMunicipioDGC to set.
     */
    public void setCodMunicipioDGC(String codMunicipioDGC)
    {
        this.codMunicipioDGC = codMunicipioDGC;
    }
    
    
    
    /**
     * @return Returns the codSubparcela.
     */
    public String getCodSubparcela()
    {
        return codSubparcela;
    }
    
    
    
    /**
     * @param codSubparcela The codSubparcela to set.
     */
    public void setCodSubparcela(String codSubparcela)
    {
        this.codSubparcela = codSubparcela;
    }
    
    
    
    /**
     * @return Returns the denominacionCultivo.
     */
    public String getDenominacionCultivo()
    {
        return denominacionCultivo;
    }
    
    
    
    /**
     * @param denominacionCultivo The denominacionCultivo to set.
     */
    public void setDenominacionCultivo(String denominacionCultivo)
    {
        this.denominacionCultivo = denominacionCultivo;
    }
    
    
    
    /**
     * @return Returns the ejercicioFinBonificacion.
     */
    public Integer getEjercicioFinBonificacion()
    {
        return ejercicioFinBonificacion;
    }
    
    
    
    /**
     * @param ejercicioFinBonificacion The ejercicioFinBonificacion to set.
     */
    public void setEjercicioFinBonificacion(Integer ejercicioFinBonificacion)
    {
        this.ejercicioFinBonificacion = ejercicioFinBonificacion;
    }
    
    
    
    /**
     * @return Returns the intensidadProductiva.
     */
    public Integer getIntensidadProductiva()
    {
        return intensidadProductiva;
    }
    
    
    
    /**
     * @param intensidadProductiva The intensidadProductiva to set.
     */
    public void setIntensidadProductiva(Integer intensidadProductiva)
    {
        this.intensidadProductiva = intensidadProductiva;
    }
    
    
    
    /**
     * @return Returns the superficieParcela.
     */
    public Long getSuperficieParcela()
    {
        return superficieParcela;
    }
    
    
    
    /**
     * @param superficieParcela The superficieParcela to set.
     */
    public void setSuperficieParcela(Long superficieParcela)
    {
        this.superficieParcela = superficieParcela;
    }
    
    
    
    /**
     * @return Returns the tipoSubparcela.
     */
    public String getTipoSubparcela()
    {
        return tipoSubparcela;
    }
    
    
    
    /**
     * @param tipoSubparcela The tipoSubparcela to set.
     */
    public void setTipoSubparcela(String tipoSubparcela)
    {
        this.tipoSubparcela = tipoSubparcela;
    }
    
    
    
    /**
     * @return Returns the tipoSuelo.
     */
    public String getTipoSuelo()
    {
        return tipoSuelo;
    }
    
    
    
    /**
     * @param tipoSuelo The tipoSuelo to set.
     */
    public void setTipoSuelo(String tipoSuelo)
    {
        this.tipoSuelo = tipoSuelo;
    }
    
    /**
     * @return Returns the idCultivo.
     */
    public IdCultivo getIdCultivo()
    {
        return idCultivo;
    }
    
    
    
    /**
     * @param idCultivo The idCultivo to set.
     */
    public void setIdCultivo(IdCultivo idCultivo)
    {
        this.idCultivo = idCultivo;
    }
    
    
    /**
     * @return Returns the valorCatastral.
     */
    public Double getValorCatastral()
    {
        return valorCatastral;
    }
    
    
    
    /**
     * @param valorCatastral The valorCatastral to set.
     */
    public void setValorCatastral(Double valorCatastral)
    {
        this.valorCatastral = valorCatastral;
    }
    
    /**
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente()
    {
        return datosExpediente;
    }
    
    /**
     * @param datosExpediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente datosExpediente)
    {
        this.datosExpediente = datosExpediente;
    }
    
    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return idCultivo.hashCode();
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return idCultivo.equals(obj);
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return idCultivo.toString();
    }


    /**
     * @return Returns the lstRepartos.
     */
    public ArrayList getLstRepartos()
    {
        return lstRepartos;
    }


    /**
     * @param lstRepartos The lstRepartos to set.
     */
    public void setLstRepartos(ArrayList lstRepartos)
    {
        this.lstRepartos = lstRepartos;
    }
    
    public Cultivo clone(Cultivo cultivo)
    {
    	Cultivo cultivoNuevo = new Cultivo();
    	
    	cultivoNuevo.setCodBonificacion(cultivo.getCodBonificacion());
    	cultivoNuevo.setCodDelegacionMEH(cultivo.getCodDelegacionMEH());
    	cultivoNuevo.setCodModalidadReparto(cultivo.getCodModalidadReparto());
    	cultivoNuevo.setCodMunicipioDGC(cultivo.getCodMunicipioDGC());
    	cultivoNuevo.setCodSubparcela(cultivo.getCodSubparcela());
    	cultivoNuevo.setDenominacionCultivo(cultivo.getDenominacionCultivo());
    	cultivoNuevo.setEjercicioFinBonificacion(cultivo.getEjercicioFinBonificacion());
    	
    	cultivoNuevo.setIdCultivo(new IdCultivo());
    	cultivoNuevo.getIdCultivo().setCalifCultivo(cultivo.getIdCultivo().getCalifCultivo());
    	cultivoNuevo.getIdCultivo().setNumOrden(cultivo.getIdCultivo().getNumOrden());
    	cultivoNuevo.getIdCultivo().setParcelaCatastral(cultivo.getIdCultivo().getParcelaCatastral().getRefCatastral());
    	
    	 	
    	cultivoNuevo.setIntensidadProductiva(cultivo.getIntensidadProductiva());
    	cultivoNuevo.setSuperficieParcela(cultivo.getSuperficieParcela());
    	cultivoNuevo.setTipoSubparcela(cultivo.getTipoSubparcela());
    	cultivoNuevo.setTipoSuelo(cultivo.getTipoSuelo());
    	cultivoNuevo.setValorCatastral(cultivo.getValorCatastral());
    	cultivoNuevo.setTIPO_MOVIMIENTO(cultivo.getTIPO_MOVIMIENTO());
    	
    	return cultivoNuevo;
    }
    
    public boolean isElementoModificado() {
		return elementoModificado;
	}

	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
	}
	
	public boolean isCatastroTemporal(){
		
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		//return true;
	}
	public boolean isNotCatastroTemporal(){
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return true;
		}
		
		//return false;
	}
	
	public Boolean isElemModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean isCultivoAltaModif(){
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean cultivoAltaModif = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF)){
			cultivoAltaModif= true;
		}
		return cultivoAltaModif;
	}
	
	public Boolean isCultivoElim() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean cultivoElim = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			cultivoElim = true;
		}
		return cultivoElim;
	}
	
	
}
