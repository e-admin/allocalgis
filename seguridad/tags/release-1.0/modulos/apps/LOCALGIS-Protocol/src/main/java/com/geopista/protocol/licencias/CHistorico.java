package com.geopista.protocol.licencias;

import com.geopista.protocol.licencias.estados.CEstado;

import java.util.Date;

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
