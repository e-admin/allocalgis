/**
 * UnidadDatosIntercambio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.intercambio.importacion.xml.contents;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;

public class UnidadDatosIntercambio implements Serializable
{
    
    /**
     * Entidad generadora del fichero de salida
     */
    private EntidadGeneradora entidadGeneradora;
    
    /**
     * Finca de catastro obligatoria
     */
    private FincaCatastro fincaCatastro;
    /**
     * Lista de suelos
     */
    private ArrayList lstSuelos;
    /**
     * Lista de Unidades Constructivas
     */
    private ArrayList lstUCs;
    
    /**
     * Lista obligatoria de bienes inmuebles
     */
    private ArrayList lstBienesInmuebles;
    /**
     * Lista de construcciones
     */
    private ArrayList lstConstrucciones;
    /**
     * Lista de cultivos
     */
    private ArrayList lstCultivos;
    /**
     * Lista de repartos
     */
    private ArrayList lstRepartos;    
    
    /**
     * FXCC
     */
    private FX_CC fxcc;
    
    /**
     * Lista de Imágenes
     */
    private ArrayList lstImagenes = null;
    
    
    public UnidadDatosIntercambio()
    {
        
    }

    /**
     * @return Returns the entidadGeneradora.
     */
    public EntidadGeneradora getEntidadGeneradora()
    {
        return entidadGeneradora;
    }

    /**
     * @param entidadGeneradora The entidadGeneradora to set.
     */
    public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora)
    {
        this.entidadGeneradora = entidadGeneradora;
    }

    /**
     * @return Returns the fincaCatastro.
     */
    public FincaCatastro getFincaCatastro()
    {
        return fincaCatastro;
    }

    /**
     * @param fincaCatastro The fincaCatastro to set.
     */
    public void setFincaCatastro(FincaCatastro fincaCatastro)
    {
        this.fincaCatastro = fincaCatastro;
    }

    /**
     * @return Returns the lstBienesInmuebles.
     */
    public ArrayList getLstBienesInmuebles()
    {
        return lstBienesInmuebles;
    }

    /**
     * @param lstBienesInmuebles The lstBienesInmuebles to set.
     */
    public void setLstBienesInmuebles(ArrayList lstBienesInmuebles)
    {
        this.lstBienesInmuebles = lstBienesInmuebles;
    }

    /**
     * @return Returns the lstConstrucciones.
     */
    public ArrayList getLstConstrucciones()
    {
        return lstConstrucciones;
    }

    /**
     * @param lstConstrucciones The lstConstrucciones to set.
     */
    public void setLstConstrucciones(ArrayList lstConstrucciones)
    {
        this.lstConstrucciones = lstConstrucciones;
    }

    /**
     * @return Returns the lstCultivos.
     */
    public ArrayList getLstCultivos()
    {
        return lstCultivos;
    }

    /**
     * @param lstCultivos The lstCultivos to set.
     */
    public void setLstCultivos(ArrayList lstCultivos)
    {
        this.lstCultivos = lstCultivos;
    }

    /**
     * @return Returns the lstRepartos.
     */
    public ArrayList getLstRepartos()
    {
        return lstRepartos;
    }

    /**
     * @param lstRepartos The lstRepartos to set.
     */
    public void setLstRepartos(ArrayList lstRepartos)
    {
        this.lstRepartos = lstRepartos;
    }

    /**
     * @return Returns the lstSuelos.
     */
    public ArrayList getLstSuelos()
    {
        return lstSuelos;
    }

    /**
     * @param lstSuelos The lstSuelos to set.
     */
    public void setLstSuelos(ArrayList lstSuelos)
    {
        this.lstSuelos = lstSuelos;
    }

    /**
     * @return Returns the lstUCs.
     */
    public ArrayList getLstUCs()
    {
        return lstUCs;
    }

    /**
     * @param lstUCs The lstUCs to set.
     */
    public void setLstUCs(ArrayList lstUCs)
    {
        this.lstUCs = lstUCs;
    }

    /**
     * @return Returns the fxcc.
     */
    public FX_CC getFxcc()
    {
        return fxcc;
    }

    /**
     * @param fxcc The fxcc to set.
     */
    public void setFxcc(FX_CC fxcc)
    {
        this.fxcc = fxcc;
    }
    
    public void borrarCampos(){
    	
    	setEntidadGeneradora(null);
    	setFincaCatastro(null);
    	setFxcc(null);
    	setLstBienesInmuebles(null);
    	setLstConstrucciones(null);
    	setLstCultivos(null);
    	setLstRepartos(null);
    	setLstSuelos(null);
    	setLstUCs(null);
    	
    }

	public ArrayList getLstImagenes() {
		return lstImagenes;
	}

	public void setLstImagenes(ArrayList lstImagenes) {
		this.lstImagenes = lstImagenes;
	}
}
