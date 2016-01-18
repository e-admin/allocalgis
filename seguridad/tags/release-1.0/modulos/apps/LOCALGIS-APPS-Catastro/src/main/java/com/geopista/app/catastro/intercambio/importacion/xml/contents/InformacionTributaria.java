package com.geopista.app.catastro.intercambio.importacion.xml.contents;

public class InformacionTributaria
{
    //bajo el nodo dcbl

    private String ert = new String();
    private String erp = new String();
    
    /**
     * Constructor por defecto
     *
     */
    public InformacionTributaria(){
        
    }

    /**
     * @return Returns the erp.
     */
    public String getErp()
    {
        return erp;
    }

    /**
     * @param erp The erp to set.
     */
    public void setErp(String erp)
    {
        this.erp = erp;
    }

    /**
     * @return Returns the ert.
     */
    public String getErt()
    {
        return ert;
    }

    /**
     * @param ert The ert to set.
     */
    public void setErt(String ert)
    {
        this.ert = ert;
    }
    
    
}
