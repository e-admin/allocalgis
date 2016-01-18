package com.geopista.app.catastro.intercambio.importacion.xml.contents;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosRetorno;

public class ConsultaCatastroRequest
{
    
    /**
     * Entidad generadora del fichero de salida
     */
    private UnidadDatosRetorno unidadDatosRetorno;
    
    /**
     * Finca de catastro obligatoria
     */
    
    private ArrayList lstErrores;
   
    public ConsultaCatastroRequest()
    {
        
    }

	/**
	 * @return the lstErrores
	 */
	public ArrayList getLstErrores() {
		return lstErrores;
	}

	/**
	 * @param lstErrores the lstErrores to set
	 */
	public void setLstErrores(ArrayList lstErrores) {
		this.lstErrores = lstErrores;
	}

	/**
	 * @return the unidadDatosRetorno
	 */
	public UnidadDatosRetorno getUnidadDatosRetorno() {
		return unidadDatosRetorno;
	}

	/**
	 * @param unidadDatosRetorno the unidadDatosRetorno to set
	 */
	public void setUnidadDatosRetorno(UnidadDatosRetorno unidadDatosRetorno) {
		this.unidadDatosRetorno = unidadDatosRetorno;
	}

    /**
     * @return Returns the entidadGeneradora.
     */
    }
