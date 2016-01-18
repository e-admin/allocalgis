package com.geopista.app.catastro.intercambio.importacion.utils;

import com.geopista.app.catastro.model.beans.Expediente;

public class ParcelaAfectada
{
    private int idFinca;
    private Expediente expediente;
    private String refParcela;  
    
    public ParcelaAfectada(){
        
    }   
    
    public ParcelaAfectada(Expediente expediente, String refParcela)
    {
        this.expediente = expediente;
        this.refParcela = refParcela;          
    }
    
    public ParcelaAfectada(String refParcela)
    {
        this.refParcela = refParcela;          
    }
    
    /**
     * @return Returns the expediente.
     */
    public Expediente getExpediente()
    {
        return expediente;
    }
    /**
     * @param refExpediente The expediente to set.
     */
    public void setExpediente(Expediente expediente)
    {
        this.expediente = expediente;
    }
    /**
     * @return Returns the refParcela.
     */
    public String getRefParcela()
    {
        return refParcela;
    }
    /**
     * @param refParcela The refParcela to set.
     */
    public void setRefParcela(String refParcela)
    {
        this.refParcela = refParcela;
    }    
       

    /**
     * @return Returns the idFinca.
     */
    public int getIdFinca()
    {
        return idFinca;
    }

    /**
     * @param idFinca The idFinca to set.
     */
    public void setIdFinca(int idFinca)
    {
        this.idFinca = idFinca;
    }
    
    public boolean equals (Object o)
    {
        if(! (o instanceof ParcelaAfectada))
            return false;
        if (((ParcelaAfectada)o).getRefParcela().equals(refParcela)
                && ((ParcelaAfectada)o).getExpediente()!=null && expediente !=null
                && ((ParcelaAfectada)o).getExpediente().getIdExpediente() == expediente.getIdExpediente())
            return true;
        else 
            return false;
        
    }
    
    public int hashCode ()
    {
        int hash = 0;
        
        return hash;
    }
    
}
