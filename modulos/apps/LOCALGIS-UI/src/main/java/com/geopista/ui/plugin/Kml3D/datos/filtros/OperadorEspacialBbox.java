/**
 * OperadorEspacialBbox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos.filtros;


public class OperadorEspacialBbox extends OperadorEspacial {

	
	private String SRS = "";
	private String minX = "";
	private String maxX = "";
	private String minY = "";
	private String maxY = "";
	
	
	public OperadorEspacialBbox (OPERADOR_ESPACIAL operadorEspacial)
	{
		super(operadorEspacial);
	}
	
	
	
	public static OperadorEspacialBbox creaOperadorDesdeTexto ( String textoItem )
	{
		OperadorEspacialBbox operadorEspacialBbox = new OperadorEspacialBbox ( OPERADOR_ESPACIAL.BBOX );
		String [] trozos = textoItem.split ( "srsName=\"" );
		String [] trozosDatos = trozos[1].split("\"");
		operadorEspacialBbox.setSRS ( trozosDatos[0] );
		
		
		// Sacamos las X
		String [] trozosX = trozos[1].split ( "<gml:X>" );
		trozosDatos = trozosX[1].split ( "</gml:X>" );
		operadorEspacialBbox.setMinX ( trozosDatos[0] );
		
		trozosDatos = trozosX[2].split ( "</gml:X>" );
		operadorEspacialBbox.setMaxX ( trozosDatos[0] );
		
		
		// Sacamos las Y
		String [] trozosY = trozos[1].split ( "<gml:Y>" );
		trozosDatos = trozosY[1].split ( "</gml:Y>" );
		operadorEspacialBbox.setMinY ( trozosDatos[0] );
		
		trozosDatos = trozosY[2].split ( "</gml:Y>" );
		operadorEspacialBbox.setMaxY ( trozosDatos[0] );		
		
		
		return operadorEspacialBbox;
	}
	
	
	
	
	public String getTexto()
	{
		
		String texto = "BBOX ( <gml:Box srsName=\"" + 
			this.SRS +
			"\" xmlns:gml=\"http://www.opengis.net/gml\"> <gml:coord> <gml:X>" +
			this.minX + 
			"</gml:X> <gml:Y>" +
			this.minY +
			"</gml:Y></gml:coord> <gml:coord> <gml:X>" +
			this.maxX +
			"</gml:X> <gml:Y>" +
			this.maxY + 
			"</gml:Y></gml:coord></gml:Box>)";
		
		return texto;
	}
	
	
	
	public Elemento clone()
	{
		OperadorEspacialBbox operadorEspacialBbox = new OperadorEspacialBbox ( OPERADOR_ESPACIAL.BBOX );
		operadorEspacialBbox.setSRS ( this.SRS );
		operadorEspacialBbox.setMinX ( this.minX );
		operadorEspacialBbox.setMaxX ( this.maxX );
		operadorEspacialBbox.setMinY ( this.minY );
		operadorEspacialBbox.setMaxY ( this.maxY );
		
		return operadorEspacialBbox;
	}
	
	


	public String getSRS() {
		return SRS;
	}


	public void setSRS(String sRS) {
		SRS = sRS;
	}


	public String getMinX() {
		return minX;
	}


	public void setMinX(String minX) {
		this.minX = minX;
	}


	public String getMaxX() {
		return maxX;
	}


	public void setMaxX(String maxX) {
		this.maxX = maxX;
	}


	public String getMinY() {
		return minY;
	}


	public void setMinY(String minY) {
		this.minY = minY;
	}


	public String getMaxY() {
		return maxY;
	}


	public void setMaxY(String maxY) {
		this.maxY = maxY;
	}
	
	
	
	public String creaFiltro()
	{ 
		String texto = "";
		if ( this.getOperando() != null )
		{
			texto = this.getOperando().creaFiltro();
		}
		
		texto += " " + this.getTexto();
		return texto;
	}
	
}
