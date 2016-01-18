/**
 * DatosConfiguracion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 02-mar-2007
 * Time: 14:59:31
 * To change this template use File | Settings | File Templates.
 */

/**
 * Bean que encapsula todos los parametros de configuracion de la aplicacion.
 * */

public class DatosConfiguracion implements Serializable
{
    private int frecuenciaActualizacion;
    private int frecuenciaEnvio;
    private String formaConvenio;
    private String modoTrabajo;
    private String tipoConvenio;
    private int mostrarAvisoAct;
    private int mostrarAvisoEnvio;
    
    private String modoGenerarFXCC = null;

    public final static String CONVENIO_NINGUNO = "NINGU";
    public final static String CONVENIO_TITULARIDAD = "901";
    public final static String CONVENIO_FISICO_ECONOMICO = "902";
    public final static String CONVENIO_DELEGACION_COMPETENCIAS = "D";
    public final static String CONVENIO_ENCOMIENDA_GESTION = "E";
    public final static String MODO_TRABAJO_ACOPLADO = "A";
    public final static String MODO_TRABAJO_DESACOPLADO = "D";    
    
    public final static String MODO_GENERAR_FXCC_FICHERO = "F";
    public static final String MODO_GENERAR_FXCC_DIRECTORIO = "D";

    public DatosConfiguracion()
    {

    }

    public int getFrecuenciaActualizacion()
    {
        return frecuenciaActualizacion;
    }

    public void setFrecuenciaActualizacion(int frecuenciaActualizacion)
    {
        this.frecuenciaActualizacion = frecuenciaActualizacion;
    }

    public int getFrecuenciaEnvio()
    {
        return frecuenciaEnvio;
    }

    public void setFrecuenciaEnvio(int frecuenciaEnvio)
    {
        this.frecuenciaEnvio = frecuenciaEnvio;
    }

    public String getFormaConvenio()
    {
        return formaConvenio;
    }

    public void setFormaConvenio(String formaConvenio)
    {
        this.formaConvenio = formaConvenio;
    }

    public String getModoTrabajo()
    {
        return modoTrabajo;
    }

    public void setModoTrabajo(String modoTrabajo)
    {
        this.modoTrabajo = modoTrabajo;
    }

    public String getTipoConvenio()
    {
        return tipoConvenio;
    }

    public void setTipoConvenio(String tipoConvenio)
    {
        this.tipoConvenio = tipoConvenio;
    }

    public int getMostrarAvisoAct()
    {
        return mostrarAvisoAct;
    }

    public void setMostrarAvisoAct(int mostrarAvisoAct)
    {
        this.mostrarAvisoAct = mostrarAvisoAct;
    }

    public int getMostrarAvisoEnvio()
    {
        return mostrarAvisoEnvio;
    }

    public void setMostrarAvisoEnvio(int mostrarAvisoEnvio)
    {
        this.mostrarAvisoEnvio = mostrarAvisoEnvio;
    }

	public String getModoGenerarFXCC() {
		return modoGenerarFXCC;
	}

	public void setModoGenerarFXCC(String modoGenerarFXCC) {
		this.modoGenerarFXCC = modoGenerarFXCC;
	}
}
