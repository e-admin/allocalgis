package com.geopista.app.catastro.model.datos.ponencia;

import java.io.Serializable;

public class TipoValor implements Serializable
{
    
    private Float usoComercial;
    private Float usoResidencial;
    private Float usoOficinas;
    private Float usoIndustrial;
    private Float usoTuristico;
    private Float otrosUsos1;
    private Float otrosUsos2;
    private Float otrosUsos3;
    private Float zonaVerde;
    private Float equipamientos;
    
    public TipoValor()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @return Returns the equipamientos.
     */
    public Float getEquipamientos()
    {
        return equipamientos;
    }
    
    /**
     * @param equipamientos The equipamientos to set.
     */
    public void setEquipamientos(Float equipamientos)
    {
        this.equipamientos = equipamientos;
    }
    
    /**
     * @return Returns the otrosUsos1.
     */
    public Float getOtrosUsos1()
    {
        return otrosUsos1;
    }
    
    /**
     * @param otrosUsos1 The otrosUsos1 to set.
     */
    public void setOtrosUsos1(Float otrosUsos1)
    {
        this.otrosUsos1 = otrosUsos1;
    }
    
    /**
     * @return Returns the otrosUsos2.
     */
    public Float getOtrosUsos2()
    {
        return otrosUsos2;
    }
    
    /**
     * @param otrosUsos2 The otrosUsos2 to set.
     */
    public void setOtrosUsos2(Float otrosUsos2)
    {
        this.otrosUsos2 = otrosUsos2;
    }
    
    /**
     * @return Returns the otrosUsos3.
     */
    public Float getOtrosUsos3()
    {
        return otrosUsos3;
    }
    
    /**
     * @param otrosUsos3 The otrosUsos3 to set.
     */
    public void setOtrosUsos3(Float otrosUsos3)
    {
        this.otrosUsos3 = otrosUsos3;
    }
    
    /**
     * @return Returns the usoComercial.
     */
    public Float getUsoComercial()
    {
        return usoComercial;
    }
    
    /**
     * @param usoComercial The usoComercial to set.
     */
    public void setUsoComercial(Float usoComercial)
    {
        this.usoComercial = usoComercial;
    }
    
    /**
     * @return Returns the usoIndustrial.
     */
    public Float getUsoIndustrial()
    {
        return usoIndustrial;
    }
    
    /**
     * @param usoIndustrial The usoIndustrial to set.
     */
    public void setUsoIndustrial(Float usoIndustrial)
    {
        this.usoIndustrial = usoIndustrial;
    }
    
    /**
     * @return Returns the usoOficinas.
     */
    public Float getUsoOficinas()
    {
        return usoOficinas;
    }
    
    /**
     * @param usoOficinas The usoOficinas to set.
     */
    public void setUsoOficinas(Float usoOficinas)
    {
        this.usoOficinas = usoOficinas;
    }
    
    /**
     * @return Returns the usoResidencial.
     */
    public Float getUsoResidencial()
    {
        return usoResidencial;
    }
    
    /**
     * @param usoResidencial The usoResidencial to set.
     */
    public void setUsoResidencial(Float usoResidencial)
    {
        this.usoResidencial = usoResidencial;
    }
    
    /**
     * @return Returns the usoTuristico.
     */
    public Float getUsoTuristico()
    {
        return usoTuristico;
    }
    
    /**
     * @param usoTuristico The usoTuristico to set.
     */
    public void setUsoTuristico(Float usoTuristico)
    {
        this.usoTuristico = usoTuristico;
    }
    
    /**
     * @return Returns the zonaVerde.
     */
    public Float getZonaVerde()
    {
        return zonaVerde;
    }
    
    /**
     * @param zonaVerde The zonaVerde to set.
     */
    public void setZonaVerde(Float zonaVerde)
    {
        this.zonaVerde = zonaVerde;
    }
    
    
}
