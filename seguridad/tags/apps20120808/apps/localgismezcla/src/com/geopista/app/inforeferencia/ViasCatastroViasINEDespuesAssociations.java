/*
 * Created on 18-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.inforeferencia;


public class ViasCatastroViasINEDespuesAssociations{
	DatosViasCatastro datosViasCatastro = null;
    DatosViasINE datosViasINE = null;
    
    public ViasCatastroViasINEDespuesAssociations(DatosViasCatastro datosViasCatastro, DatosViasINE datosViasINE ){
        this.datosViasCatastro = datosViasCatastro;
        this.datosViasINE = datosViasINE;
    }

    public DatosViasCatastro getDatosViasCatastro(){
        return datosViasCatastro;
    }

    public void setDatosViasCatastro(DatosViasCatastro datosViasCatastro){
        this.datosViasCatastro = datosViasCatastro;
    }

    public DatosViasINE getDatosViasINE(){
        return datosViasINE;
    }

    public void setDatosViasINE(DatosViasINE datosViasINE){
        this.datosViasINE = datosViasINE;
    }
}
