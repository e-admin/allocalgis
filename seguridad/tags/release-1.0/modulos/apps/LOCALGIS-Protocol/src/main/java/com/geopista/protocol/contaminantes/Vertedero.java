package com.geopista.protocol.contaminantes;



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
 * Date: 18-oct-2004
 * Time: 17:13:13
 */
public class Vertedero implements Cloneable{
    String id;
    String tipo;
    String titularidad;
    String gAdm;
    String pExistentes;
    Long capacidad;
    Float gOcupacion;
    String posiAmplia;
    String estado;
    Integer vidaUtil;
    Vector residuos;

    public Vertedero() {
        residuos = new Vector();
    }

    public Long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Long capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getgAdm() {
        return gAdm;
    }

    public void setgAdm(String gAdm) {
        this.gAdm = gAdm;
    }

    public Float getgOcupacion() {
        return gOcupacion;
    }

    public void setgOcupacion(Float gOcupacion) {
        this.gOcupacion = gOcupacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpExistentes() {
        return pExistentes;
    }

    public void setpExistentes(String pExistentes) {
        this.pExistentes = pExistentes;
    }

    public String getPosiAmplia() {
        return posiAmplia;
    }

    public void setPosiAmplia(String posiAmplia) {
        this.posiAmplia = posiAmplia;
    }

    public Vector getResiduos() {
        return residuos;
    }

    public void setResiduos(Vector residuos) {
        this.residuos = residuos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitularidad() {
        return titularidad;
    }

    public void setTitularidad(String titularidad) {
        this.titularidad = titularidad;
    }

    public Integer getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(Integer vidaUtil) {
        this.vidaUtil = vidaUtil;
    }
    public void addResiduo(Residuo residuo)
    {
        residuos.add(residuo);
    }


     public Object clone()
     {
            Vertedero obj=null;
            try{
                   obj=(Vertedero)super.clone();
             }catch(CloneNotSupportedException ex){
             }

             obj.setCapacidad(this.getCapacidad());
             obj.setEstado(this.getEstado());
             obj.setgAdm(this.getgAdm());
             obj.setgOcupacion(this.getgOcupacion());
             obj.setId(this.getId());
             obj.setpExistentes(this.getpExistentes());
             obj.setPosiAmplia(this.getPosiAmplia());
             obj.setResiduos((Vector)this.getResiduos().clone());
             obj.setTipo(this.getTipo());
             obj.setTitularidad(this.getTitularidad());
             obj.setVidaUtil(this.getVidaUtil());
             return obj;
     }

     public boolean copy(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertedero)) return false;

        final Vertedero v = (Vertedero) o;
        setCapacidad(v.getCapacidad());
        setEstado(v.getEstado());
        setgAdm(v.getgAdm());
        setgOcupacion(v.getgOcupacion());
        setId(v.getId());
        setpExistentes(v.getpExistentes());
        setPosiAmplia(v.getPosiAmplia());
        setResiduos(v.getResiduos());
        setTipo(v.getTipo());
        setTitularidad(v.getTitularidad());
        setVidaUtil(v.getVidaUtil());
        return true;
    }

}
