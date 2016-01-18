package com.geopista.protocol.contaminantes;

import java.util.Date;
import java.util.Vector;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 21-oct-2004
 * Time: 13:19:56
 */
public class Inspeccion {
    String id;
    String id_res;
    Integer nfolios;
    Date finicio;
    Date ffin;
    Date finidatos;
    Date ffindatos;
    Integer nrec;
    String pfijos;
    String pmoviles;
    String sustancias;
    String cmin;
    String cmax;
    boolean zlatente=false;
    String motlantente;
    boolean zsaturada=false;
    String motsaturada;
    String friesgo;
    String medidas;
    String resultados;
    String obs;
    Vector anexos;
    int estado= -1;

    public String getCmax() {
        return cmax;
    }

    public void setCmax(String cmax) {
        this.cmax = cmax;
    }

    public String getCmin() {
        return cmin;
    }

    public void setCmin(String cmin) {
        this.cmin = cmin;
    }

    public Date getFfin() {
        return ffin;
    }

    public void setFfin(Date ffin) {
        this.ffin = ffin;
    }

    public Date getFfindatos() {
        return ffindatos;
    }

    public void setFfindatos(Date ffindatos) {
        this.ffindatos = ffindatos;
    }

    public Date getFinicio() {
        return finicio;
    }

    public void setFinicio(Date finicio) {
        this.finicio = finicio;
    }

    public Date getFinidatos() {
        return finidatos;
    }

    public void setFinidatos(Date finidatos) {
        this.finidatos = finidatos;
    }

    public String getFriesgo() {
        return friesgo;
    }

    public void setFriesgo(String friesgo) {
        this.friesgo = friesgo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_res() {
        return id_res;
    }

    public void setId_res(String id_res) {
        this.id_res = id_res;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public String getMotlantente() {
        return motlantente;
    }

    public void setMotlantente(String motlantente) {
        this.motlantente = motlantente;
    }

    public String getMotsaturada() {
        return motsaturada;
    }

    public void setMotsaturada(String motsaturada) {
        this.motsaturada = motsaturada;
    }

    public Integer getNfolios() {
        return nfolios;
    }

    public void setNfolios(Integer nfolios) {
        this.nfolios = nfolios;
    }

    public Integer getNrec() {
        return nrec;
    }

    public void setNrec(Integer nrec) {
        this.nrec = nrec;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPfijos() {
        return pfijos;
    }

    public void setPfijos(String pfijos) {
        this.pfijos = pfijos;
    }

    public String getPmoviles() {
        return pmoviles;
    }

    public void setPmoviles(String pmoviles) {
        this.pmoviles = pmoviles;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public String getSustancias() {
        return sustancias;
    }

    public void setSustancias(String sustancias) {
        this.sustancias = sustancias;
    }

    public boolean isZlatente() {
        return zlatente;
    }

    public void setZlatente(boolean zlatente) {
        this.zlatente = zlatente;
    }

    public boolean isZsaturada() {
        return zsaturada;
    }

    public void setZsaturada(boolean zsaturada) {
        this.zsaturada = zsaturada;
    }


    public void addAnexo(CAnexo anexo)
    {
        if (anexos==null) anexos= new Vector();
        if (anexos==null) return;
        anexos.add(anexo);
    }

    public void setAnexos(Vector anexos) {
        this.anexos= anexos;
    }

    public Vector getAnexos() {
        return anexos;
    }

    public void setEstado(int estado) {
        this.estado= estado;
    }

    public int getEstado() {
        return estado;
    }



    public boolean copy(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inspeccion)) return false;

        final Inspeccion inspeccion = (Inspeccion) o;
        id=inspeccion.getId();
        id_res=inspeccion.getId_res();
        nfolios=inspeccion.getNfolios();
        finicio=inspeccion.getFinicio();
        ffin=inspeccion.getFfin();
        finidatos=inspeccion.getFinidatos();
        ffindatos=inspeccion.getFfindatos();
        nrec=inspeccion.getNrec();
        pfijos=inspeccion.getPfijos();
        pmoviles=inspeccion.getPmoviles();
        sustancias=inspeccion.getSustancias();
        cmin=inspeccion.getCmin();
        cmax=inspeccion.getCmax();
        zlatente=inspeccion.isZlatente();
        motlantente=inspeccion.getMotlantente();
        zsaturada=inspeccion.isZsaturada();
        motsaturada=inspeccion.getMotsaturada();
        friesgo=inspeccion.getFriesgo();
        medidas=inspeccion.getMedidas();
        resultados=inspeccion.getResultados();
        obs=inspeccion.getObs();
        anexos=inspeccion.getAnexos();
        estado=inspeccion.getEstado();

        return true;
    }

}
