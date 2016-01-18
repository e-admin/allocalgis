/**
 * TableRowReparto.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.utils;


public class TableRowReparto {


	String identificador;
	String pcatastral1;
	String pcatastral2;
	String cargo;
	Boolean editable;
	
    /**
     * Constructor por defecto
     *
     */
	public TableRowReparto ()
	{
		super();
	}
	
    /**
     * Constructor de la clase
     * 
     * @param columna Column del atributo
     * @param atributo Attribute del atributo
     * @param sqlQuery Cadena en la que se especifica una consulta SQL del atributo
     * @param editable Verdadero si el atributo es editable
     */
	public TableRowReparto (String identificador, Boolean editable)
	{		
		this.identificador = identificador;
		this.editable = editable;
	}
	
	public TableRowReparto (String pcatastral1, String pcatastral2, String cargo, Boolean editable)
	{		
		this.identificador = pcatastral1 + pcatastral2 + cargo;
		this.pcatastral1 = pcatastral1;
		this.pcatastral2 = pcatastral2;
		this.cargo = cargo;
		this.editable = editable;
	}
	
    /**
     * Constructor de la clase
     * 
     * @param columna Column del atributo
     * @param atributo Attribute del atributo
     * @param sqlQuery Cadena en la que se especifica una consulta SQL del atributo
     * @param editable Verdadero si el atributo es editable
     */
	public TableRowReparto (String identificador, boolean editable)
	{		
		this.identificador = identificador;
		this.editable = new Boolean(editable);
	}
	
	public TableRowReparto (String pcatastral1, String pcatastral2, String cargo, boolean editable)
	{		
		this.identificador = pcatastral1 + pcatastral2 + cargo;
		this.pcatastral1 = pcatastral1;
		this.pcatastral2 = pcatastral2;
		this.cargo = cargo;
		this.editable = new Boolean(editable);
	}
	/**
	 * @return Returns the editable.
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * @return the cargo
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * @param cargo the cargo to set
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	/**
	 * @return the pcatastral1
	 */
	public String getPcatastral1() {
		return pcatastral1;
	}

	/**
	 * @param pcatastral1 the pcatastral1 to set
	 */
	public void setPcatastral1(String pcatastral1) {
		this.pcatastral1 = pcatastral1;
	}

	/**
	 * @return the pcatastral2
	 */
	public String getPcatastral2() {
		return pcatastral2;
	}

	/**
	 * @param pcatastral2 the pcatastral2 to set
	 */
	public void setPcatastral2(String pcatastral2) {
		this.pcatastral2 = pcatastral2;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return Returns the sqlQuery.
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param sqlQuery The sqlQuery to set.
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
    
}

