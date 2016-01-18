package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Titular;

public class BienInmuebleJuridico implements Serializable
{
    
    private BienInmuebleCatastro bienInmueble = new BienInmuebleCatastro();
    private Expediente datosExpediente = new Expediente();
    private ArrayList lstTitulares = new ArrayList();
    private ArrayList lstComBienes; //= new ArrayList();
    
    /**
     * booleano que indica si el bien inmueble se ha eliminado del expediente
     * en la pantalla de edición-
     */
    private boolean delete = false;
    
    /**
     * @return Returns the bienInmueble.
     */
    public BienInmuebleCatastro getBienInmueble()
    {
        return bienInmueble;
    }
    /**
     * @param bienInmueble The bienInmueble to set.
     */
    public void setBienInmueble(BienInmuebleCatastro bienInmueble)
    {
        this.bienInmueble = bienInmueble;
    }
    /**
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente()
    {
        return datosExpediente;
    }
    /**
     * @param expediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente expediente)
    {
        this.datosExpediente = expediente;
    }    
    
    /**
     * @return Returns the lstTitulares.
     */
    public ArrayList getLstTitulares()
    {
        return lstTitulares;
    }
    /**
     * @param lstTitulares The lstTitulares to set.
     */
    public void setLstTitulares(ArrayList lstTitulares)
    {
        this.lstTitulares = lstTitulares;
    }
    /**
     * @return Returns the lstComBienes.
     */
    public ArrayList getLstComBienes()
    {
        return lstComBienes;
    }
    /**
     * @param lstComBienes The lstComBienes to set.
     */
    public void setLstComBienes(ArrayList lstComBienes)
    {
        this.lstComBienes = lstComBienes;
    }
    
    
    
    
    public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public BienInmuebleJuridico clone(BienInmuebleJuridico bij)
    {
    	BienInmuebleJuridico bijNuevo = new BienInmuebleJuridico();
    	
    	bijNuevo.setBienInmueble(bij.getBienInmueble().clone(bij.getBienInmueble()));
    	
    	bijNuevo.setLstComBienes(bij.getLstComBienes());
    	
    	if (bij.getLstTitulares() != null)
    	{
    		ArrayList lstTitulares = new ArrayList();
    		for (int i=0;i<bij.getLstTitulares().size();i++)
    		{
    			Titular titular = (Titular) bij.getLstTitulares().get(i);
    			Titular titularActual = titular.clone(titular);
    			
    			lstTitulares.add(titularActual);
    		}
    		bijNuevo.setLstTitulares(lstTitulares);
    	}
    	
    	
    	return bijNuevo;
    }
    
}
