/*
 * Created on 18-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;

import com.geopista.feature.GeopistaFeature;

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ViasCatastroViasINEAssociations
{
    GeopistaFeature catastroFeature = null;
    DatosViasINE datosViasINE = null;
    
    public ViasCatastroViasINEAssociations(GeopistaFeature catastroFeature, DatosViasINE datosViasINE )
    {
        this.catastroFeature = catastroFeature;
        this.datosViasINE = datosViasINE;
    }
    /**
     * @return Returns the catastroFeature.
     */
    public GeopistaFeature getCatastroFeature()
    {
        return catastroFeature;
    }
    /**
     * @param catastroFeature The catastroFeature to set.
     */
    public void setCatastroFeature(GeopistaFeature catastroFeature)
    {
        this.catastroFeature = catastroFeature;
    }
    /**
     * @return Returns the datosViasINE.
     */
    public DatosViasINE getDatosViasINE()
    {
        return datosViasINE;
    }
    /**
     * @param datosViasINE The datosViasINE to set.
     */
    public void setDatosViasINE(DatosViasINE datosViasINE)
    {
        this.datosViasINE = datosViasINE;
    }
}
