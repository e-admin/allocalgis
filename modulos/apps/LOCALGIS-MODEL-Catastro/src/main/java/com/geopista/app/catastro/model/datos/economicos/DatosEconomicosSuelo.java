/**
 * DatosEconomicosSuelo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.economicos;



import java.io.Serializable;

import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;



public class DatosEconomicosSuelo implements Serializable
{ 
    
    /**
     * Código de tramo de vía en la Ponencia de Valores asignado por la DGC
     * (Procedimiento de Valoracion colectiva anteriores a 2005).
     */
    private String codTramoPonencia;
    /**
     * Código de la vía Pública de la Ponencia de Valores asignado por la DGC
     * (Procedimiento de Valoracion colectiva anteriores a 2005).
     */
    private String codViaPonencia;
    /**
     * Número de fachadas.
     * Puede tomar los valores N, 2 o 3.
     */
    private String numFachadas;
    
    /**
     * Código del tipo de valor a aplicar.
     * Puede tomar los valores: 0, 1, 2, 3, 4, 5, 6, 7, 8 o 9.
     */
    private String codTipoValor;
    /**
     * 1 Entero y 2 Decimales
     */
    private Float correctorAprecDeprec;
    /**
     * 1 entero y dos decimales
     */
    private Float correctorCargasSingulares;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorDeprecFuncional;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorDesmonte;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorFormaIrregular;
    /**
     * Corrector inedificabilidad temporal.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorInedificabilidad;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorLongFachada;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorSitEspeciales;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorSupDistinta;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorNoLucrativo;
    /**
     * Indicador de aplicación de coeficiente corrector por suelo reservado para la
     * edificación de VPO
     */
    private Boolean correctorVPO;
    
    /**
     * Zona urbanística según codificación DGC. (Procedimiento de Valoracion colectiva
     * 2005 y posteriores).
     */
    private PonenciaUrbanistica zonaUrbanistica = new PonenciaUrbanistica();
    /**
     * Zona de valor en la Ponencia de Valores. (Procedimiento de Valoracion colectiva
     * 2005 y posteriores).
     */
    private PonenciaZonaValor zonaValor = new PonenciaZonaValor();
    
    /**
     * Zona de valor en la Ponencia de Valores. (Procedimiento de Valoracion colectiva
     * anterior al 2005).
     */
    private PonenciaTramos tramos = new PonenciaTramos();
    
    Integer coefAprecDeprec;
    
    
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosEconomicosSuelo()
    {
        
    }
    
    /**
     * @return Returns the codTipoValor.
     */
    public String getCodTipoValor()
    {
        return codTipoValor;
    }
    
    
    
    
    /**
     * @param codTipoValor The codTipoValor to set.
     */
    public void setCodTipoValor(String codTipoValor)
    {
        this.codTipoValor = codTipoValor;
    }
    
    
    
    
    /**
     * @return Returns the codTramoPonencia.
     */
    public String getCodTramoPonencia()
    {
        return codTramoPonencia;
    }
    
    
    
    
    /**
     * @param codTramoPonencia The codTramoPonencia to set.
     */
    public void setCodTramoPonencia(String codTramoPonencia)
    {
        this.codTramoPonencia = codTramoPonencia;
    }
    
    
    
    
    /**
     * @return Returns the codViaPonencia.
     */
    public String getCodViaPonencia()
    {
        return codViaPonencia;
    }
    
    
    
    
    /**
     * @param codViaPonencia The codViaPonencia to set.
     */
    public void setCodViaPonencia(String codViaPonencia)
    {
        this.codViaPonencia = codViaPonencia;
    }
    
    
    
    
    /**
     * @return Returns the coefAprecDeprec.
     */
    public Integer getCoefAprecDeprec()
    {
        return coefAprecDeprec;
    }
    
    
    
    
    /**
     * @param coefAprecDeprec The coefAprecDeprec to set.
     */
    public void setCoefAprecDeprec(Integer coefAprecDeprec)
    {
        this.coefAprecDeprec = coefAprecDeprec;
    }
    
    
    
    
    /**
     * @return Returns the correctorAprecDeprec.
     */
    public Float getCorrectorAprecDeprec()
    {
        return correctorAprecDeprec;
    }
    
    
    
    
    /**
     * @param correctorAprecDeprec The correctorAprecDeprec to set.
     */
    public void setCorrectorAprecDeprec(Float correctorAprecDeprec)
    {
        this.correctorAprecDeprec = correctorAprecDeprec;
    }
    
    
    
    
    /**
     * @return Returns the correctorCargasSingulares.
     */
    public Float getCorrectorCargasSingulares()
    {
        return correctorCargasSingulares;
    }
    
    
    
    
    /**
     * @param correctorCargasSingulares The correctorCargasSingulares to set.
     */
    public void setCorrectorCargasSingulares(Float correctorCargasSingulares)
    {
        this.correctorCargasSingulares = correctorCargasSingulares;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorDeprecFuncional()
    {
        if(correctorDeprecFuncional!=null &&correctorDeprecFuncional.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorDeprecFuncional.
     */
    public Boolean isCorrectorDeprecFuncional()
    {
        return correctorDeprecFuncional;
    }
    
    
    
    
    /**
     * @param correctorDeprecFuncional The correctorDeprecFuncional to set.
     */
    public void setCorrectorDeprecFuncional(Boolean correctorDeprecFuncional)
    {
        this.correctorDeprecFuncional = correctorDeprecFuncional;
    }
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorDesmonte()
    {
        if(correctorDesmonte!=null &&correctorDesmonte.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    
    /**
     * @return Returns the correctorDesmonte.
     */
    public Boolean isCorrectorDesmonte()
    {
        return correctorDesmonte;
    }
    
    
    
    
    /**
     * @param correctorDesmonte The correctorDesmonte to set.
     */
    public void setCorrectorDesmonte(Boolean correctorDesmonte)
    {
        this.correctorDesmonte = correctorDesmonte;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorFormaIrregular()
    {
        if(correctorFormaIrregular!=null &&correctorFormaIrregular.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorFormaIrregular.
     */
    public Boolean isCorrectorFormaIrregular()
    {
        return correctorFormaIrregular;
    }
    
    
    
    
    /**
     * @param correctorFormaIrregular The correctorFormaIrregular to set.
     */
    public void setCorrectorFormaIrregular(Boolean correctorFormaIrregular)
    {
        this.correctorFormaIrregular = correctorFormaIrregular;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorInedificabilidad()
    {
        if(correctorInedificabilidad!=null&&correctorInedificabilidad.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorInedificabilidad.
     */
    public Boolean isCorrectorInedificabilidad()
    {
        return correctorInedificabilidad;
    }
    
    
    
    
    /**
     * @param correctorInedificabilidad The correctorInedificabilidad to set.
     */
    public void setCorrectorInedificabilidad(Boolean correctorInedificabilidad)
    {
        this.correctorInedificabilidad = correctorInedificabilidad;
    }

    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorLongFachada()
    {
        if(correctorLongFachada!=null&&correctorLongFachada.booleanValue())
        {
            return "S";
        }
        return "N";        
    }
    
    
    
    /**
     * @return Returns the correctorLongFachada.
     */
    public Boolean isCorrectorLongFachada()
    {
        return correctorLongFachada;
    }
    
    
    
    
    /**
     * @param correctorLongFachada The correctorLongFachada to set.
     */
    public void setCorrectorLongFachada(Boolean correctorLongFachada)
    {
        this.correctorLongFachada = correctorLongFachada;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorNoLucrativo()
    {
        if(correctorNoLucrativo!=null && correctorNoLucrativo.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorNoLucrativo.
     */
    public Boolean isCorrectorNoLucrativo()
    {
        return correctorNoLucrativo;
    }
    
    
    
    
    /**
     * @param correctorNoLucrativo The correctorNoLucrativo to set.
     */
    public void setCorrectorNoLucrativo(Boolean correctorNoLucrativo)
    {
        this.correctorNoLucrativo = correctorNoLucrativo;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorSitEspeciales()
    {
        if(correctorSitEspeciales!=null&&correctorSitEspeciales.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorSitEspeciales.
     */
    public Boolean isCorrectorSitEspeciales()
    {
        return correctorSitEspeciales;
    }
    
    
    
    
    /**
     * @param correctorSitEspeciales The correctorSitEspeciales to set.
     */
    public void setCorrectorSitEspeciales(Boolean correctorSitEspeciales)
    {
        this.correctorSitEspeciales = correctorSitEspeciales;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorSupDistinta()
    {
        if(correctorSupDistinta!=null&&correctorSupDistinta.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorSupDistinta.
     */
    public Boolean isCorrectorSupDistinta()
    {
        return correctorSupDistinta;
    }
    
    
    
    
    /**
     * @param correctorSupDistinta The correctorSupDistinta to set.
     */
    public void setCorrectorSupDistinta(Boolean correctorSupDistinta)
    {
        this.correctorSupDistinta = correctorSupDistinta;
    }
    
    
    /**
     * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
     * */
    public String getCorrectorVPO()
    {
        if(correctorVPO!=null&&correctorVPO.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    /**
     * @return Returns the correctorVPO.
     */
    public Boolean isCorrectorVPO()
    {
        return correctorVPO;
    }
    
    
    
    
    /**
     * @param correctorVPO The correctorVPO to set.
     */
    public void setCorrectorVPO(Boolean correctorVPO)
    {
        this.correctorVPO = correctorVPO;
    }
    
    
    
    
    /**
     * @return Returns the numFachadas.
     */
    public String getNumFachadas()
    {
        return numFachadas;
    }
    
    
    
    
    /**
     * @param numFachadas The numFachadas to set.
     */
    public void setNumFachadas(String numFachadas)
    {
        this.numFachadas = numFachadas;
    }
    
    
    
    
    /**
     * @return Returns the zonaUrbanistica.
     */
    public PonenciaUrbanistica getZonaUrbanistica()
    {
        return zonaUrbanistica;
    }
    
    
    
    
    /**
     * @param zonaUrbanistica The zonaUrbanistica to set.
     */
    public void setZonaUrbanistica(PonenciaUrbanistica zonaUrbanistica)
    {
        this.zonaUrbanistica = zonaUrbanistica;
    }
    
    
    
    
    /**
     * @return Returns the zonaValor.
     */
    public PonenciaZonaValor getZonaValor()
    {
        return zonaValor;
    }
    
    
    
    
    /**
     * @param zonaValor The zonaValor to set.
     */
    public void setZonaValor(PonenciaZonaValor zonaValor)
    {
        this.zonaValor = zonaValor;
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

	public PonenciaTramos getTramos() {
		return tramos;
	}

	public void setTramos(PonenciaTramos tramos) {
		this.tramos = tramos;
	}
    
    
}
