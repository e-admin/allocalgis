/**
 * CHistorico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.licencias;

import java.util.Date;

import com.geopista.protocol.licencias.estados.CEstado;

/**
 * @author SATEC
 * @version $Revision: 1.1 $
 *          <p/>
 *          Autor:$Author: miriamperez $
 *          Fecha Ultima Modificacion:$Date: 2009/07/03 12:31:55 $
 *          $Name:  $
 *          $RCSfile: CHistorico.java,v $
 *          $Revision: 1.1 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CHistorico  implements java.io.Serializable{

    private long idHistorico;
    private CExpedienteLicencia expediente;
    private CEstado estado;
    private CSolicitudLicencia solicitud;
    private Date fechaHistorico;
    private String usuario;
    private String apunte;
    private int hasBeen;
    private String sistema;
    /** un historico del sistema puede ser GENERICO (cuando se produce cualquier cambio en el expediente),
     * o por CAMBIO_ESTADO (cuando se produce un cambio de estado en el expediente).
     */
    private int generico= 0;


    public CHistorico() {
    }

    public long getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(long idHistorico) {
        this.idHistorico = idHistorico;
    }


    public CSolicitudLicencia getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(CSolicitudLicencia solicitud) {
        this.solicitud = solicitud;
    }

    public CExpedienteLicencia getExpediente() {
        return expediente;
    }

    public void setExpediente(CExpedienteLicencia expediente) {
        this.expediente = expediente;
    }

    public CEstado getEstado() {
        return estado;
    }

    public void setEstado(CEstado estado) {
        this.estado = estado;
    }


    public String getApunte() {
        return apunte;
    }

    public void setApunte(String apunte) {
        this.apunte = apunte;
    }

    public Date getFechaHistorico() {
        return fechaHistorico;
    }

    public void setFechaHistorico(Date fechaHistorico) {
        this.fechaHistorico = fechaHistorico;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getHasBeen() {
        return hasBeen;
    }

    public void setHasBeen(int hasbenn) {
        this.hasBeen = hasbenn;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public int getGenerico() {
        return generico;
    }

    public void setGenerico(int i) {
        this.generico= i;
    }

}
