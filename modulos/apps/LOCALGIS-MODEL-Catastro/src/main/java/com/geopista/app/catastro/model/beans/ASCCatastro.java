/**
 * ASCCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.util.ArrayList;

public class ASCCatastro
{
    private String codDelegacion;
    private String nombreGerencia;
    private String codMunicipio;
    private String nombreMunicipio;
    
    private String codVia;
    private String sigla;
    private String nomVia;
    private String numPolicia;
    private String letraDuplicado;
    
    private ReferenciaCatastral refCatastral=new ReferenciaCatastral("");
    
    private ArrayList lstLinderos=new ArrayList();
    
    private String escalaCaptura;
    private String fechaCaptura;
    
    private String supParcela;
    private String supSobreRasante;
    private String supBajoRasante;
    private String supConstruida;
    
    private int numPlantasSignificativas;
    
    private ArrayList lstPlantas = new ArrayList();
    
    private String ejercicioExpediente;
    private String refExpedienteAdministrativo;
    private String codEntidadColaboradora;
    
    public ASCCatastro (){
    }

    /**
     * @return Returns the codDelegacion.
     */
    public String getCodDelegacion()
    {
        return codDelegacion;
    }

    /**
     * @param codDelegacion The codDelegacion to set.
     */
    public void setCodDelegacion(String codDelegacion)
    {
        this.codDelegacion = codDelegacion;
    }

    /**
     * @return Returns the codMunicipio.
     */
    public String getCodMunicipio()
    {
        return codMunicipio;
    }

    /**
     * @param codMunicipio The codMunicipio to set.
     */
    public void setCodMunicipio(String codMunicipio)
    {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return Returns the codVia.
     */
    public String getCodVia()
    {
        return codVia;
    }

    /**
     * @param codVia The codVia to set.
     */
    public void setCodVia(String codVia)
    {
        this.codVia = codVia;
    }

    /**
     * @return Returns the escalaCaptura.
     */
    public String getEscalaCaptura()
    {
        return escalaCaptura;
    }

    /**
     * @param escalaCaptura The escalaCaptura to set.
     */
    public void setEscalaCaptura(String escalaCaptura)
    {
        this.escalaCaptura = escalaCaptura;
    }

    /**
     * @return Returns the fechaCaptura.
     */
    public String getFechaCaptura()
    {
        return fechaCaptura;
    }

    /**
     * @param fechaCaptura The fechaCaptura to set.
     */
    public void setFechaCaptura(String fechaCaptura)
    {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * @return Returns the letraDuplicado.
     */
    public String getLetraDuplicado()
    {
        return letraDuplicado;
    }

    /**
     * @param letraDuplicado The letraDuplicado to set.
     */
    public void setLetraDuplicado(String letraDuplicado)
    {
        this.letraDuplicado = letraDuplicado;
    }

    /**
     * @return Returns the lstLinderos.
     */
    public ArrayList getLstLinderos()
    {
        return lstLinderos;
    }

    /**
     * @param lstLinderos The lstLinderos to set.
     */
    public void setLstLinderos(ArrayList lstLinderos)
    {
        this.lstLinderos = lstLinderos;
    }

    /**
     * @return Returns the lstPlantas.
     */
    public ArrayList getLstPlantas()
    {
        return lstPlantas;
    }

    /**
     * @param lstPlantas The lstPlantas to set.
     */
    public void setLstPlantas(ArrayList lstPlantas)
    {
        this.lstPlantas = lstPlantas;
    }

    /**
     * @return Returns the nombreGerencia.
     */
    public String getNombreGerencia()
    {
        return nombreGerencia;
    }

    /**
     * @param nombreGerencia The nombreGerencia to set.
     */
    public void setNombreGerencia(String nombreGerencia)
    {
        this.nombreGerencia = nombreGerencia;
    }

    /**
     * @return Returns the nombreMunicipio.
     */
    public String getNombreMunicipio()
    {
        return nombreMunicipio;
    }

    /**
     * @param nombreMunicipio The nombreMunicipio to set.
     */
    public void setNombreMunicipio(String nombreMunicipio)
    {
        this.nombreMunicipio = nombreMunicipio;
    }

    /**
     * @return Returns the nomVia.
     */
    public String getNomVia()
    {
        return nomVia;
    }

    /**
     * @param nomVia The nomVia to set.
     */
    public void setNomVia(String nomVia)
    {
        this.nomVia = nomVia;
    }

    /**
     * @return Returns the numPlantasSignificativas.
     */
    public int getNumPlantasSignificativas()
    {
        return numPlantasSignificativas;
    }

    /**
     * @param numPlantasSignificativas The numPlantasSignificativas to set.
     */
    public void setNumPlantasSignificativas(int numPlantasSignificativas)
    {
        this.numPlantasSignificativas = numPlantasSignificativas;
    }

    /**
     * @return Returns the numPolicia.
     */
    public String getNumPolicia()
    {
        return numPolicia;
    }

    /**
     * @param numPolicia The numPolicia to set.
     */
    public void setNumPolicia(String numPolicia)
    {
        this.numPolicia = numPolicia;
    }

    /**
     * @return Returns the refCatastral.
     */
    public ReferenciaCatastral getRefCatastral()
    {
        return refCatastral;
    }

    /**
     * @param refCatastral The refCatastral to set.
     */
    public void setRefCatastral(ReferenciaCatastral refCatastral)
    {
        this.refCatastral = refCatastral;
    }

    /**
     * @return Returns the sigla.
     */
    public String getSigla()
    {
        return sigla;
    }

    /**
     * @param sigla The sigla to set.
     */
    public void setSigla(String sigla)
    {
        this.sigla = sigla;
    }

    /**
     * @return Returns the supBajoRasante.
     */
    public String getSupBajoRasante()
    {
        return supBajoRasante;
    }

    /**
     * @param supBajoRasante The supBajoRasante to set.
     */
    public void setSupBajoRasante(String supBajoRasante)
    {
        this.supBajoRasante = supBajoRasante;
    }

    /**
     * @return Returns the supConstruida.
     */
    public String getSupConstruida()
    {
        return supConstruida;
    }

    /**
     * @param supConstruida The supConstruida to set.
     */
    public void setSupConstruida(String supConstruida)
    {
        this.supConstruida = supConstruida;
    }

    /**
     * @return Returns the supParcela.
     */
    public String getSupParcela()
    {
        return supParcela;
    }

    /**
     * @param supParcela The supParcela to set.
     */
    public void setSupParcela(String supParcela)
    {
        this.supParcela = supParcela;
    }

    /**
     * @return Returns the supSobreRasante.
     */
    public String getSupSobreRasante()
    {
        return supSobreRasante;
    }

    /**
     * @param supSobreRasante The supSobreRasante to set.
     */
    public void setSupSobreRasante(String supSobreRasante)
    {
        this.supSobreRasante = supSobreRasante;
    }

    /**
     * @return Returns the codEntidadColaboradora.
     */
    public String getCodEntidadColaboradora()
    {
        return codEntidadColaboradora;
    }

    /**
     * @param codEntidadColaboradora The codEntidadColaboradora to set.
     */
    public void setCodEntidadColaboradora(String codEntidadColaboradora)
    {
        this.codEntidadColaboradora = codEntidadColaboradora;
    }

    /**
     * @return Returns the ejercicioExpediente.
     */
    public String getEjercicioExpediente()
    {
        return ejercicioExpediente;
    }

    /**
     * @param ejercicioExpediente The ejercicioExpediente to set.
     */
    public void setEjercicioExpediente(String ejercicioExpediente)
    {
        this.ejercicioExpediente = ejercicioExpediente;
    }

    /**
     * @return Returns the refExpedienteAdministrativo.
     */
    public String getRefExpedienteAdministrativo()
    {
        return refExpedienteAdministrativo;
    }

    /**
     * @param refExpedienteAdministrativo The refExpedienteAdministrativo to set.
     */
    public void setRefExpedienteAdministrativo(String refExpedienteAdministrativo)
    {
        this.refExpedienteAdministrativo = refExpedienteAdministrativo;
    }
    
}
