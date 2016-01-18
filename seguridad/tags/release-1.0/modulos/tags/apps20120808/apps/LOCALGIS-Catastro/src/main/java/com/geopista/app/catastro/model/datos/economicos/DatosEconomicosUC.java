package com.geopista.app.catastro.model.datos.economicos;

import java.io.Serializable;

import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;


public class DatosEconomicosUC implements Serializable
{
    
    /**
     * Código de tramo de  vía en la ponencia de valores asignado por la DGC
     * (Procedimiento de valoración colectiva anteriores a 2005).
     */
    private PonenciaTramos tramoPonencia;
    
    /**
     * Código de la vía pública de la ponencia de valores asignado por la DGC
     * (Procedimiento de valoración colectiva anteriores a 2005).
     */
    private String codViaPonencia;
    
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores N, 2 o 3.
     */
    private String numFachadas;
    
    /**
     * Valor del coeficiente corrector por finca afectada por cargas singulares. un
     * entero y dos decimales.
     * 
     */
    private Float coefCargasSingulares;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorDepreciacion;
    /**
     * Según codificación establcida por las Normas Técnicas de Valoración.
     */
    private String correctorConservacion;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorLongFachada;
    /**
     * Indicador de aplicación del coeficiente corrector por situaciones especiales de
     * caracter extrínsico (futuros viales, inconcreción urbanística, etc). S/N
     */
    private Boolean correctorSitEspeciales;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     * Puede tomar los valores: S o N.
     */
    private Boolean correctorNoLucrativo;
    
    
    /**
     * Zona de Valor en la ponencia de valores (Procedimiento de valoración colectiva
     * 2005 y posteriores).
     */
    private PonenciaZonaValor zonaValor;
    
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosEconomicosUC()
    {
        
    }
    
    /**
     * @return Returns the tramoPonencia.
     */
    public PonenciaTramos getTramoPonencia()
    {
        return tramoPonencia;
    }
    
    
    
    /**
     * @param tramoPonencia The tramoPonencia to set.
     */
    public void setTramoPonencia(PonenciaTramos tramoPonencia)
    {
        this.tramoPonencia = tramoPonencia;
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
     * @return Returns the coefCargasSingulares.
     */
    public Float getCoefCargasSingulares()
    {
        return coefCargasSingulares;
    }
    
    
    
    /**
     * @param coefCargasSingulares The coefCargasSingulares to set.
     */
    public void setCoefCargasSingulares(Float coefCargasSingulares)
    {
        this.coefCargasSingulares = coefCargasSingulares;
    }
    
    
    
    /**
     * @return Returns the correctorConservacion.
     */
    public String getCorrectorConservacion()
    {
        return correctorConservacion;
    }
    
    
    
    /**
     * @param correctorConservacion The correctorConservacion to set.
     */
    public void setCorrectorConservacion(String correctorConservacion)
    {
        this.correctorConservacion = correctorConservacion;
    }

    /**
    * Metodo especial para parsear los objeto con java2xml, no usar, utilizar el is
    * */
    public String getCorrectorDepreciacion()
    {
        if(correctorDepreciacion!=null&&correctorDepreciacion.booleanValue())
        {
            return "S";
        }
        return "N";
    }
    
    
    /**
     * @return Returns the correctorDepreciacion.
     */
    public Boolean isCorrectorDepreciacion()
    {
        return correctorDepreciacion;
    }
    
    
    
    /**
     * @param correctorDepreciacion The correctorDepreciacion to set.
     */
    public void setCorrectorDepreciacion(Boolean correctorDepreciacion)
    {
        this.correctorDepreciacion = correctorDepreciacion;
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
        if(correctorNoLucrativo!=null&&correctorNoLucrativo.booleanValue())
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
    
}
