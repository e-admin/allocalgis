/**
 * LinderoCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

public class LinderoCatastro implements Serializable
{
	
	/**
	 * Auto generated serial version. 
	 */
	private static final long serialVersionUID = -5965379074410421243L;
	
	public static String IZ = "IZ";
	public static String FD = "FD";
	public static String DR = "DR";
	
    private String tipoLindero;
    private int codVia;
    private String siglaVia;
    private int numVia;
    private String nombreVia;
    private String letraDuplicado;
    
    
    /**
     * Constructor por defecto
     *
     */
    public LinderoCatastro(String tipo, int codVia, String siglaVia, 
            String nombreVia, int numVia, String letraDuplicado)
    {
        this.tipoLindero = tipo;
        this.codVia = codVia;
        this.siglaVia = siglaVia;
        this.nombreVia = nombreVia;
        this.numVia = numVia;
        this.letraDuplicado = letraDuplicado;
    }

    public LinderoCatastro()
    {
    }
    
    /**
     * @return Returns the codVia.
     */
    public int getCodVia()
    {
        return codVia;
    }

    /**
     * @param codVia The codVia to set.
     */
    public void setCodVia(int codVia)
    {
        this.codVia = codVia;
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
     * @return Returns the nombreVia.
     */
    public String getNombreVia()
    {
        return nombreVia;
    }

    /**
     * @param nombreVia The nombreVia to set.
     */
    public void setNombreVia(String nombreVia)
    {
        this.nombreVia = nombreVia;
    }

    /**
     * @return Returns the numVia.
     */
    public int getNumVia()
    {
        return numVia;
    }

    /**
     * @param numVia The numVia to set.
     */
    public void setNumVia(int numVia)
    {
        this.numVia = numVia;
    }

    /**
     * @return Returns the siglaVia.
     */
    public String getSiglaVia()
    {
        return siglaVia;
    }

    /**
     * @param siglaVia The siglaVia to set.
     */
    public void setSiglaVia(String siglaVia)
    {
        this.siglaVia = siglaVia;
    }

    /**
     * @return Returns the tipoLindero.
     */
    public String getTipoLindero()
    {
        return tipoLindero;
    }

    /**
     * @param tipoLindero The tipoLindero to set.
     */
    public void setTipoLindero(String tipoLindero)
    {
        this.tipoLindero = tipoLindero;
    }

	public void setData(int codvia, String siglas, String nombre, int num, String letradupli ) {
		// TODO Auto-generated method stub
		this.codVia =codvia;
		this.siglaVia = siglas;
		this.nombreVia = nombre;
		this.numVia = num;
		this.letraDuplicado = letradupli;
	}
    
}
