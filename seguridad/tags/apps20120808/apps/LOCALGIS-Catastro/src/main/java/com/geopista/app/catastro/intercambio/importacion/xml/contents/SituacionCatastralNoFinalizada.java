package com.geopista.app.catastro.intercambio.importacion.xml.contents;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;

public class SituacionCatastralNoFinalizada extends UnidadDatosIntercambio
{
    private BienInmuebleCatastro bienInmuebleNoFin;
    
    /**
     * @return Returns the bienInmuebleNoFin.
     */
    public BienInmuebleCatastro getBienInmuebleNoFin()
    {   
        return bienInmuebleNoFin;
    }
    
    /**
     * @param bienInmuebleNoFin The bienInmuebleNoFin to set.
     */
    public void setBienInmuebleNoFin(BienInmuebleCatastro bienInmuebleNoFin)
    {
        this.bienInmuebleNoFin = bienInmuebleNoFin;
    }    
}
