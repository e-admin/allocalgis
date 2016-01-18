package com.geopista.app.catastro.model.datos.ponencia;

public class PonenciaZonaValor extends Ponencia
{
    
    private String codZonaValor;
    private TipoValor importesZonaValor;
    private Float valorUnitario;
    private Float valorZonaVerde;
    private Float valorEquipamientos;
    private Float valorSinDesarrollar;
    
    
    public PonenciaZonaValor()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * @return Returns the codZonaValor.
     */
    public String getCodZonaValor()
    {
        return codZonaValor;
    }
    
    
    /**
     * @param codZonaValor The codZonaValor to set.
     */
    public void setCodZonaValor(String codZonaValor)
    {
        this.codZonaValor = codZonaValor;
    }
    
    
    /**
     * @return Returns the importesZonaValor.
     */
    public TipoValor getImportesZonaValor()
    {
        return importesZonaValor;
    }
    
    
    /**
     * @param importesZonaValor The importesZonaValor to set.
     */
    public void setImportesZonaValor(TipoValor importesZonaValor)
    {
        this.importesZonaValor = importesZonaValor;
    }
    
    
    /**
     * @return Returns the valorEquipamientos.
     */
    public Float getValorEquipamientos()
    {
        return valorEquipamientos;
    }
    
    
    /**
     * @param valorEquipamientos The valorEquipamientos to set.
     */
    public void setValorEquipamientos(Float valorEquipamientos)
    {
        this.valorEquipamientos = valorEquipamientos;
    }
    
    
    /**
     * @return Returns the valorSinDesarrollar.
     */
    public Float getValorSinDesarrollar()
    {
        return valorSinDesarrollar;
    }
    
    
    /**
     * @param valorSinDesarrollar The valorSinDesarrollar to set.
     */
    public void setValorSinDesarrollar(Float valorSinDesarrollar)
    {
        this.valorSinDesarrollar = valorSinDesarrollar;
    }
    
    
    /**
     * @return Returns the valorUnitario.
     */
    public Float getValorUnitario()
    {
        return valorUnitario;
    }
    
    
    /**
     * @param valorUnitario The valorUnitario to set.
     */
    public void setValorUnitario(Float valorUnitario)
    {
        this.valorUnitario = valorUnitario;
    }
    
    
    /**
     * @return Returns the valorZonaVerde.
     */
    public Float getValorZonaVerde()
    {
        return valorZonaVerde;
    }
    
    
    /**
     * @param valorZonaVerde The valorZonaVerde to set.
     */
    public void setValorZonaVerde(Float valorZonaVerde)
    {
        this.valorZonaVerde = valorZonaVerde;
    }
    
}
