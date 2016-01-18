package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * Representa un fichero de con datos de padrón para la importación
 * 
 * @author COTESA
 *
 */
public class PadronFile implements Serializable {
    
    private String fecha;
    private String hora;
    private String codigo;
    private String procedencia;
    private String anio;
    private String descripcion;
    private long valorTotal;
    private long valorCatastral;
    private long valorSuelo;
    private long valorConstruccion;
    private long baseLiquidable;
    
    /**
     * Constructor por defecto de la clase
     */
    public PadronFile()
    {        
    }
    
    /**
     * @return Returns the anio.
     */
    public String getAnio()
    {
        return anio;
    }
    /**
     * @param anio The anio to set.
     */
    public void setAnio(String anio)
    {
        this.anio = anio;
    }
    /**
     * @return Returns the baseLiquidable.
     */
    public long getBaseLiquidable()
    {
        return baseLiquidable;
    }
    /**
     * @param baseLiquidable The baseLiquidable to set.
     */
    public void setBaseLiquidable(long baseLiquidable)
    {
        this.baseLiquidable = baseLiquidable;
    }
    /**
     * @return Returns the codigo.
     */
    public String getCodigo()
    {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }
    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion()
    {
        return descripcion;
    }
    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha()
    {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }
    /**
     * @return Returns the hora.
     */
    public String getHora()
    {
        return hora;
    }

    /**
     * @param hora The hora to set.
     */
    public void setHora(String hora)
    {
        this.hora = hora;
    }

    /**
     * @return Returns the procedencia.
     */
    public String getProcedencia()
    {
        return procedencia;
    }
    /**
     * @param procedencia The procedencia to set.
     */
    public void setProcedencia(String procedencia)
    {
        this.procedencia = procedencia;
    }
    /**
     * @return Returns the valorTotal.
     */
    public long getValorTotal()
    {
        return valorTotal;
    }
    /**
     * @param total The valorTotal to set.
     */
    public void setValorTotal(long valorTotal)
    {
        this.valorTotal = valorTotal;
    }
    /**
     * @return Returns the valorCatastral.
     */
    public long getValorCatastral()
    {
        return valorCatastral;
    }
    /**
     * @param valorCatastral The valorCatastral to set.
     */
    public void setValorCatastral(long valorCatastral)
    {
        this.valorCatastral = valorCatastral;
    }
    /**
     * @return Returns the valorConstruccion.
     */
    public long getValorConstruccion()
    {
        return valorConstruccion;
    }
    /**
     * @param valorConstruccion The valorConstruccion to set.
     */
    public void setValorConstruccion(long valorConstruccion)
    {
        this.valorConstruccion = valorConstruccion;
    }
    /**
     * @return Returns the valorSuelo.
     */
    public long getValorSuelo()
    {
        return valorSuelo;
    }
    /**
     * @param valorSuelo The valorSuelo to set.
     */
    public void setValorSuelo(long valorSuelo)
    {
        this.valorSuelo = valorSuelo;
    }
    
    public void setHeaderInformation(String linea)
    {
        this.setFecha(linea.substring(39,47));
        this.setHora(linea.substring(47,53));
        this.setCodigo(linea.substring(3,12));
        this.setProcedencia(linea.substring(12,39));
        this.setDescripcion(linea.substring(57,96));
        this.setAnio(linea.substring(120,124));        
    }
    
    public void setTailInformation(String linea)
    {
        this.setValorTotal(Long.parseLong(linea.substring(2,9)));
        this.setValorCatastral(Long.parseLong(linea.substring(44,58)));
        this.setValorSuelo(Long.parseLong(linea.substring(58,72)));
        this.setValorConstruccion(Long.parseLong(linea.substring(72,86)));
        this.setBaseLiquidable(Long.parseLong(linea.substring(86,100)));        
    }
}
