/**
 * CalleAfectada.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import com.geopista.protocol.ListaEstructuras;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-jul-2005
 * Time: 10:04:29
 */
public class CalleAfectada {
    private String  id;
    private String numExpediente;
	private long idSolicitud;
    private String tipoViaIne;
    private String nombre;
    private String numero;
    private String comentario;
    private Integer idVia;

     //Extensiï¿½n para impresion
    private ListaEstructuras estructuraTipoViaIne;
    private String locale;

    public CalleAfectada() {
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getIdVia() {
        return idVia;
    }

    public void setIdVia(Integer idVia) {
        this.idVia = idVia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getTipoViaIne() {
        return tipoViaIne;
    }

    public void setTipoViaIne(String tipoViaIne) {
        this.tipoViaIne = tipoViaIne;
    }

    //Parametros solo para impresiï¿½n
    public void setEstructuraTipoViaIne(ListaEstructuras estructuraTipoViaIne) {
        this.estructuraTipoViaIne = estructuraTipoViaIne;
    }
    //Parametros solo para impresiï¿½n
    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String toString()
    {
        try
        {
            return (getTipoViaIne()==null||estructuraTipoViaIne==null?"":estructuraTipoViaIne.getDomainNode(getTipoViaIne()).getTerm(locale))
                           + " " + getNombre()+ (getComentario()!=null?" - "+getComentario():"");
        }catch(Exception e)
        {
            return "";
        }
    }

}
