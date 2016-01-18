/**
 * ViasCatastroViasINEDespuesAssociations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
