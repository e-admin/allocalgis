/**
 * MD_DataIdentification.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;

import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 20-ago-2004
 * Time: 14:24:38
 */
public class MD_DataIdentification {
    String identification_id;
    CI_Citation citacion;
    String resumen;
    String purpose;
    String characterset;
    CI_ResponsibleParty responsibleParty;
    String rolecode_id;
    Vector idiomas;
    Vector rEspacial;
    Vector categorias;
    Vector graficos;
    Long resolucion=null;
    EX_Extent extent=null;
    MD_LegalConstraint constraint=null;

    public MD_DataIdentification() {
    }

    public String getCharacterset() {
        return characterset;
    }

    public void setCharacterset(String characterset) {
        this.characterset = characterset;
    }

    public CI_Citation getCitacion() {
        return citacion;
    }

    public void setCitacion(CI_Citation citacion) {
        this.citacion = citacion;
    }

    public String getIdentification_id() {
        return identification_id;
    }

    public void setIdentification_id(String identificacion_id) {
        this.identification_id = identificacion_id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getRolecode_id() {
        return rolecode_id;
    }

    public void setRolecode_id(String rolecode_id) {
        this.rolecode_id = rolecode_id;
    }

    public Vector getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Vector idiomas) {
        this.idiomas = idiomas;
    }
    public void addIdioma(String sCodeIdioma)
    {
        if (idiomas==null) idiomas=new Vector();
        idiomas.add(sCodeIdioma);
    }

    public Vector getrEspacial() {
        return rEspacial;
    }

    public void setrEspacial(Vector rEspacial) {
        this.rEspacial = rEspacial;
    }

     public void addrEspacial(String sCode)
    {
        if (rEspacial==null) rEspacial=new Vector();
        rEspacial.add(sCode);
    }

    public Vector getCategorias() {
        return categorias;
    }

    public void setCategorias(Vector categorias) {
        this.categorias = categorias;
    }

    public void addCategoria(String sCode)
    {
       if (categorias==null) categorias=new Vector();
       categorias.add(sCode);
    }


    public Vector getGraficos() {
        return graficos;
    }

    public void setGraficos(Vector graficos) {
        this.graficos = graficos;
    }
    public void addGrafico(String sCode)
    {
       if (graficos==null) graficos=new Vector();
       graficos.add(sCode);
    }

    public Long getResolucion() {
        return resolucion;
    }

    public void setResolucion(Long resolucion) {
        this.resolucion = resolucion;
    }

    public EX_Extent getExtent() {
        return extent;
    }

    public void setExtent(EX_Extent extent) {
        this.extent = extent;
    }

    public MD_LegalConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(MD_LegalConstraint constraint) {
        this.constraint = constraint;
    }

    public CI_ResponsibleParty getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(CI_ResponsibleParty responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

}
