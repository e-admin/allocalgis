package com.geopista.app.catastro.model.datos.economicos;

import java.io.Serializable;


public class DatosEconomicosBien implements Serializable {
    /**
     * Precio o valor declarado
     */
    private Double precioDeclarado;
    /**
     * Origen del Precio o valor declarado
     */
    private String origenPrecioDeclarado;
    
    /**
     * Precio administrativo de venta
     */
    private Double precioVenta;
    
    /**
     * Indicador del tipo de propiedad fórmulas 5 y 6: 
     * T: sobre todo          
     * S: sobre suelo         
     * C: sobre construcción            
     * V: sobre vuelo         
     * I: sobre inmueble    
     * N: no informado
     * 
     */
    private String indTipoPropiedad;
    
    /**
     * Número de orden del inmueble en la escritura de división horizontal
     */
    private String numOrdenHorizontal;
    
    
    
    /**
     * Año del valor catastral y la base liquidable
     */
    private Integer anioValorCat;
    
    
    /**
     * Año de finalización de la valoración según precio administrativo de venta
     */
    private Integer anioFinValoracion;
    
    /**
     * Valor catastral
     */
    private Double valorCatastral;
    
    /**
     * Valor catastral del suelo
     */
    private Double valorCatastralSuelo;
    
    
    /**
     * Valor catastral de la construcción
     */
    private Double valorCatastralConstruccion;
    
    
    /**
     * Base liquidable 
     */
    private Long baseLiquidable;
    
    
    
    /**
     * Uso
     */
    private String uso;
    
    
    
    /**
     * Superficie asociada al cargo en procedimientos de valoración de fincas construidas
     */
    private Long superficieCargoFincaConstruida;
    
    /**
     * Superficie asociada al cargo en procedimientos de valoración de solares o parcelas rústicas
     */
    private Long superficieCargoFincaRustica;
    
    
    /**
     * Coeficiente de participación
     */
    private Float coefParticipacion;
    
    /**
     * Antigüedad
     */
    private Integer anioAntiguedad;
    
    
    //estan en base de datos pero no se de donde se extraen:
    /**
     * Importe, en caso de existir, del valor base expresado en céntimos de euro, en
     * caso de revisiones totales o valoraciones colectivas totales posteriores a 1997
     */
    private Long valorBase;
    /**
     * Procedencia del valor base en caso de valoración colectiva posterior a  1997.
     */
    private String procedenciaValorBase;
    /**
     * Ejercicio de efectos IBI del Valor Catastral, en caso de procedimientos de
     * valoración colectiva total posterior a 2002.
     */
    private Integer ejercicioIBI;
    /**
     * Valor Catastral en el ejercicio de efectos IBI, expresado en céntimos de euro,
     * en caso de procedimientos de valoración colectiva total posterior a 2002.
     */
    private Long valorCatastralIBI;
    /**
     * Ejercicio de revisión total o valoración colectiva total.
     */
    private Integer ejercicioRevision;
    /**
     * Ejercicio de revisión parcial o valoración colectiva parcial.
     */
    private Integer ejercicioRevisionParcial;
    /**
     * Periodo total de Vigencia de la reducción, de acuerdo con el TRLRHL es un valor
     * fijo de 10 años.
     */
    private Integer periodoTotal;
    
    /**
     * Importe bonificacion rustica
     */
    private Long importeBonificacionRustica;
    /**
     * Clave bonificacion rústica
     */
    private String claveBonificacionRustica;
    
    /**
     * Constructor por defecto
     */
    public DatosEconomicosBien()
    {
        
    }
    
    /**
     * @return Returns the anioAntiguedad.
     */
    public Integer getAnioAntiguedad() {
        return anioAntiguedad;
    }
    
    
    /**
     * @param anioAntiguedad The anioAntiguedad to set.
     */
    public void setAnioAntiguedad(Integer anioAntiguedad) {
        this.anioAntiguedad = anioAntiguedad;
    }
    
    
    /**
     * @return Returns the anioValorCat.
     */
    public Integer getAnioValorCat() {
        return anioValorCat;
    }
    
    
    /**
     * @param anioValorCat The anioValorCat to set.
     */
    public void setAnioValorCat(Integer anioValorCat) {
        this.anioValorCat = anioValorCat;
    }
    
    
    /**
     * @return Returns the baseLiquidable.
     */
    public Long getBaseLiquidable() {
        return baseLiquidable;
    }
    
    
    /**
     * @param baseLiquidable The baseLiquidable to set.
     */
    public void setBaseLiquidable(Long baseLiquidable) {
        this.baseLiquidable = baseLiquidable;
    }
    
    
    
    /**
     * @return Returns the coefParticipacion.
     */
    public Float getCoefParticipacion() {
        return coefParticipacion;
    }
    
    
    /**
     * @param coefParticipacion The coefParticipacion to set.
     */
    public void setCoefParticipacion(Float coefParticipacion) {
        this.coefParticipacion = coefParticipacion;
    }
    
    
    /**
     * @return Returns the indTipoPropiedad.
     */
    public String getIndTipoPropiedad() {
        return indTipoPropiedad;
    }
    
    
    /**
     * @param indTipoPropiedad The indTipoPropiedad to set.
     */
    public void setIndTipoPropiedad(String indTipoPropiedad) {
        this.indTipoPropiedad = indTipoPropiedad;
    }
    
    
    /**
     * @return Returns the numOrdenHorizontal.
     */
    public String getNumOrdenHorizontal() {
        return numOrdenHorizontal;
    }
    
    
    /**
     * @param numOrdenHorizontal The numOrdenHorizontal to set.
     */
    public void setNumOrdenHorizontal(String numOrdenHorizontal) {
        this.numOrdenHorizontal = numOrdenHorizontal;
    }
    
    
    /**
     * @return Returns the origenPrecioDeclarado.
     */
    public String getOrigenPrecioDeclarado() {
        return origenPrecioDeclarado;
    }
    
    
    /**
     * @param origenPrecioDeclarado The origenPrecioDeclarado to set.
     */
    public void setOrigenPrecioDeclarado(String origenPrecioDeclarado) {
        this.origenPrecioDeclarado = origenPrecioDeclarado;
    }
    
    
    /**
     * @return Returns the precioDeclarado.
     */
    public Double getPrecioDeclarado() {
        return precioDeclarado;
    }
    
    
    /**
     * @param precioDeclarado The precioDeclarado to set.
     */
    public void setPrecioDeclarado(Double precioDeclarado) {
        this.precioDeclarado = precioDeclarado;
    }
    
    
    /**
     * @return Returns the superficieCargoFincaConstruida.
     */
    public Long getSuperficieCargoFincaConstruida() {
        return superficieCargoFincaConstruida;
    }
    
    
    /**
     * @param superficieCargoFincaConstruida The superficieCargoFincaConstruida to set.
     */
    public void setSuperficieCargoFincaConstruida(
            Long superficieCargoFincaConstruida) {
        this.superficieCargoFincaConstruida = superficieCargoFincaConstruida;
    }
    
    
    /**
     * @return Returns the superficieCargoFincaRustica.
     */
    public Long getSuperficieCargoFincaRustica() {
        return superficieCargoFincaRustica;
    }
    
    
    /**
     * @param superficieCargoFincaRustica The superficieCargoFincaRustica to set.
     */
    public void setSuperficieCargoFincaRustica(Long superficieCargoFincaRustica) {
        this.superficieCargoFincaRustica = superficieCargoFincaRustica;
    }
    
    
    /**
     * @return Returns the uso.
     */
    public String getUso() {
        return uso;
    }
    
    
    /**
     * @param uso The uso to set.
     */
    public void setUso(String uso) {
        this.uso = uso;
    }
    
    
    /**
     * @return Returns the valorCatastral.
     */
    public Double getValorCatastral() {
        return valorCatastral;
    }
    
    
    /**
     * @param valorCatastral The valorCatastral to set.
     */
    public void setValorCatastral(Double valorCatastral) {
        this.valorCatastral = valorCatastral;
    }
    
    
    /**
     * @return Returns the valorCatastralConstruccion.
     */
    public Double getValorCatastralConstruccion() {
        return valorCatastralConstruccion;
    }
    
    
    /**
     * @param valorCatastralConstruccion The valorCatastralConstruccion to set.
     */
    public void setValorCatastralConstruccion(Double valorCatastralConstruccion) {
        this.valorCatastralConstruccion = valorCatastralConstruccion;
    }
    
    
    /**
     * @return Returns the valorCatastralSuelo.
     */
    public Double getValorCatastralSuelo() {
        return valorCatastralSuelo;
    }
    
    
    /**
     * @param valorCatastralSuelo The valorCatastralSuelo to set.
     */
    public void setValorCatastralSuelo(Double valorCatastralSuelo) {
        this.valorCatastralSuelo = valorCatastralSuelo;
    }
    
    
    /**
     * @return Returns the anioFinValoracion.
     */
    public Integer getAnioFinValoracion() {
        return anioFinValoracion;
    }
    
    
    /**
     * @param anioFinValoracion The anioFinValoracion to set.
     */
    public void setAnioFinValoracion(Integer anioFinValoracion) {
        this.anioFinValoracion = anioFinValoracion;
    }
    
    
    /**
     * @return Returns the precioVenta.
     */
    public Double getPrecioVenta() {
        return precioVenta;
    }
    
    
    /**
     * @param precioVenta The precioVenta to set.
     */
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    /**
     * @return Returns the claveBonificacionRustica.
     */
    public String getClaveBonificacionRustica()
    {
        return claveBonificacionRustica;
    }
    /**
     * @param claveBonificacionRustica The claveBonificacionRustica to set.
     */
    public void setClaveBonificacionRustica(String claveBonificacionRustica)
    {
        this.claveBonificacionRustica = claveBonificacionRustica;
    }
    /**
     * @return Returns the importeBonificacionRustica.
     */
    public Long getImporteBonificacionRustica()
    {
        return importeBonificacionRustica;
    }
    /**
     * @param importeBonificacionRustica The importeBonificacionRustica to set.
     */
    public void setImporteBonificacionRustica(Long importeBonificacionRustica)
    {
        this.importeBonificacionRustica = importeBonificacionRustica;
    }
    
    
    
    
    public String getProcedenciaValorBase() {
		return procedenciaValorBase;
	}

	public void setProcedenciaValorBase(String procedenciaValorBase) {
		this.procedenciaValorBase = procedenciaValorBase;
	}

	public Long getValorBase() {
		return valorBase;
	}

	public void setValorBase(Long valorBase) {
		this.valorBase = valorBase;
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
