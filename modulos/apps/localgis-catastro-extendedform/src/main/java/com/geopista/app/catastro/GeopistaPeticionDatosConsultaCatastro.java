/**
 * GeopistaPeticionDatosConsultaCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

public class GeopistaPeticionDatosConsultaCatastro
{
	
	String referenciaCatastral;
	
    String parcelaCatastral1;

    String parcelaCatastral2;

    String cargo;

    String cc1;

    String cc2;

    String ineProvincia;

    String IneMunicipio;

    String codigoPoligono;

    String codigoParcela;

    String tipo;

    /**
     * @return Returns the cc1.
     */
    public String getCc1()
    {
        return cc1;
    }

    /**
     * @param cc1
     *            The cc1 to set.
     */
    public void setCc1(String cc1)
    {
        this.cc1 = cc1;
    }

    /**
     * @return Returns the cc2.
     */
    public String getCc2()
    {
        return cc2;
    }

    /**
     * @param cc2
     *            The cc2 to set.
     */
    public void setCc2(String cc2)
    {
        this.cc2 = cc2;
    }

    /**
     * @return Returns the codigoParcela.
     */
    public String getCodigoParcela()
    {
        return codigoParcela;
    }

    /**
     * @param codigoParcela
     *            The codigoParcela to set.
     */
    public void setCodigoParcela(String codigoParcela)
    {
        this.codigoParcela = codigoParcela;
    }

    /**
     * @return Returns the codigoPoligono.
     */
    public String getCodigoPoligono()
    {
        return codigoPoligono;
    }

    /**
     * @param codigoPoligono
     *            The codigoPoligono to set.
     */
    public void setCodigoPoligono(String codigoPoligono)
    {
        this.codigoPoligono = codigoPoligono;
    }

    /**
     * @return Returns the ineMunicipio.
     */
    public String getIneMunicipio()
    {
        return IneMunicipio;
    }

    /**
     * @param ineMunicipio
     *            The ineMunicipio to set.
     */
    public void setIneMunicipio(String ineMunicipio)
    {
        IneMunicipio = ineMunicipio;
    }

    /**
     * @return Returns the ineProvincia.
     */
    public String getIneProvincia()
    {
        return ineProvincia;
    }

    /**
     * @param ineProvincia
     *            The ineProvincia to set.
     */
    public void setIneProvincia(String ineProvincia)
    {
        this.ineProvincia = ineProvincia;
    }

    /**
     * @return Returns the parcelaCatastral1.
     */
    public String getParcelaCatastral1()
    {
        return parcelaCatastral1;
    }

    /**
     * @param parcelaCatastral1
     *            The parcelaCatastral1 to set.
     */
    public void setParcelaCatastral1(String parcelaCatastral1)
    {
        this.parcelaCatastral1 = parcelaCatastral1;
    }

    /**
     * @return Returns the parcelaCatastral2.
     */
    public String getParcelaCatastral2()
    {
        return parcelaCatastral2;
    }

    /**
     * @param parcelaCatastral2
     *            The parcelaCatastral2 to set.
     */
    public void setParcelaCatastral2(String parcelaCatastral2)
    {
        this.parcelaCatastral2 = parcelaCatastral2;
    }

    /**
     * @return Returns the tipo.
     */
    public String getTipo()
    {
        return tipo;
    }

    /**
     * @param tipo
     *            The tipo to set.
     */
    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    /**
     * @return Returns the cargo.
     */
    public String getCargo()
    {
        return cargo;
    }

    /**
     * @param cargo
     *            The cargo to set.
     */
    public void setCargo(String cargo)
    {
        this.cargo = cargo;
    }
    
    
    /**
     * @return Returns the parcelaCatastral1.
     */
    public String getReferenciaCatastral()
    {
        return referenciaCatastral;
    }

    /**
     * @param parcelaCatastral1
     *            The parcelaCatastral1 to set.
     */
    public void setReferenciaCatastral(String referenciaCatastral)
    {
        this.referenciaCatastral = referenciaCatastral;
    }
};
