package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;


public class DatosFisicosFinca implements Serializable {
    /**
     * Coordenada X de un punto interior de la parcela (con dos decimales sin
     * separador).
     */
    //private Integer xCoord;
	private Float xCoord;
    /**
     * Coordenada Y de un punto interior de la parcela (con dos decimales sin
     * separador).
     */
    //private Integer yCoord;
	private Float yCoord;

    private String SRS;
    
    /**
     * Datos extraídos del fichero FINURB-DGC.
     */
    private Long  supBajoRasante;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     */
    private Long  supSobreRasante;
    /**
     * Superficie construida total. Será igual a la suma de las superficies de los
     * elementos constructivos de la finca (inluida la que corresponde imputar de sus
     * porches y terrazas).
     */
    private Long  supTotal;
    /**
     * Datos extraídos del fichero FINURB-DGC.
     */
    private Long  supCubierta;
    /**
     * Superficie de la finca o parcela catastral (en metros cuadrados)
     * 
     */
    private Long  supFinca;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosFisicosFinca()
    {
        
    }
    
    /**
     * @return Returns the supBajoRasante.
     */
    public Long getSupBajoRasante()
    {
        return supBajoRasante;
    }
    
    
    /**
     * @param supBajoRasante The supBajoRasante to set.
     */
    public void setSupBajoRasante(Long supBajoRasante)
    {
        this.supBajoRasante = supBajoRasante;
    }
    
    
    /**
     * @return Returns the supCubierta.
     */
    public Long getSupCubierta()
    {
        return supCubierta;
    }
    
    
    /**
     * @param supCubierta The supCubierta to set.
     */
    public void setSupCubierta(Long supCubierta)
    {
        this.supCubierta = supCubierta;
    }
    
    
    /**
     * @return Returns the supFinca.
     */
    public Long getSupFinca()
    {
        return supFinca;
    }
    
    
    /**
     * @param supFinca The supFinca to set.
     */
    public void setSupFinca(Long supFinca)
    {
        this.supFinca = supFinca;
    }
    
    
    /**
     * @return Returns the supSobreRasante.
     */
    public Long getSupSobreRasante()
    {
        return supSobreRasante;
    }
    
    
    /**
     * @param supSobreRasante The supSobreRasante to set.
     */
    public void setSupSobreRasante(Long supSobreRasante)
    {
        this.supSobreRasante = supSobreRasante;
    }
    
    
    /**
     * @return Returns the supTotal.
     */
    public Long getSupTotal()
    {
        return supTotal;
    }
    
    
    /**
     * @param supTotal The supTotal to set.
     */
    public void setSupTotal(Long supTotal)
    {
        this.supTotal = supTotal;
    }
    
    
    /**
     * @return Returns the xCoord.
     */
    /*public Integer getXCoord()
    {
        return xCoord;
    }*/
    
    public Float getXCoord()
    {
        return xCoord;
    }
    
    
    /**
     * @param coord The xCoord to set.
     */
    /*public void setXCoord(Integer coord)
    {
        xCoord = coord;
    }*/
    
    public void setXCoord(Float coord)
    {
        xCoord = coord;
    }
    
    
    /**
     * @return Returns the yCoord.
     */
    /*public Integer getYCoord()
    {
        return yCoord;
    }*/
    
    public Float getYCoord()
    {
        return yCoord;
    }
    
    /**
     * @param coord The yCoord to set.
     */
    /*public void setYCoord(Integer coord)
    {
        yCoord = coord;
    }*/
    
    public void setYCoord(Float coord)
    {
        yCoord = coord;
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


    public String getSRS() {
        return SRS;
    }

    public void setSRS(String SRS) {
        this.SRS = SRS;
    }
}
