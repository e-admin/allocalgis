/**
 * OperadorEspacialDwithin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos.filtros;


public class OperadorEspacialDwithin extends OperadorEspacial {

	
	private String SRS = "";
	private String CoorX = "";
	private String CoorY = "";
	private String CoorZ = "";
	private String distancia = "";
	
	
	
	public OperadorEspacialDwithin (OPERADOR_ESPACIAL operadorEspacial)
	{
		super(operadorEspacial);
	}

	
	
	
	public static OperadorEspacialDwithin creaOperadorDesdeTexto ( String texto )	
	{
		OperadorEspacialDwithin operadorEspacialDwithin= new OperadorEspacialDwithin ( OPERADOR_ESPACIAL.DWITHIN );
		
		String [] trozos = texto.split ( "distance of" );
		trozos = trozos[1].split ( " \\( <gml:Point" );
		operadorEspacialDwithin.setDistancia ( trozos[0] );
		
		trozos = texto.split ( "srsName=\"" );
		String [] trozosValores = trozos[1].split ( "\"");
		operadorEspacialDwithin.setSRS ( trozosValores[0] );
		
		String [] trozosCoordenadas = trozos[1].split ( "ts=\" \">" );
		trozosValores = trozosCoordenadas[1].split ( "</gml:coordinates>" );
		String [] coordenadas = trozosValores[0].split ( "," );
		
		operadorEspacialDwithin.setCoorX ( coordenadas[0] );
		operadorEspacialDwithin.setCoorY ( coordenadas[1] );
		operadorEspacialDwithin.setCoorZ ( coordenadas[2] );
		
		return operadorEspacialDwithin;
	}
	
	
	
	public String getTexto()
	{
		String texto = "DWITHIN distance of" +
			this.distancia + 
			" ( <gml:Point srsName=\"" +
			this.SRS +
			"\" xmlns:gml=\"http://www.opengis.net/gml\"> <gml:coordinates cs=\",\" decimal=\".\" ts=\" \">" +
			this.CoorX +
			"," +
			this.CoorY +
			"," +
			this.CoorZ +
			"</gml:coordinates></gml:Point>)";

		return texto;
	}
	
	
	
	public Elemento clone()
	{
		OperadorEspacialDwithin operadorEspacialDwithin = new OperadorEspacialDwithin ( OPERADOR_ESPACIAL.DWITHIN );
		
		operadorEspacialDwithin.setSRS( this.SRS );
		operadorEspacialDwithin.setCoorX( this.CoorX );
		operadorEspacialDwithin.setCoorY( this.CoorY );
		operadorEspacialDwithin.setCoorZ( this.CoorZ );
		operadorEspacialDwithin.setDistancia( this.distancia );
		
		return operadorEspacialDwithin;
	}
	
	
	
	public String getSRS() {
		return SRS;
	}

	public void setSRS(String sRS) {
		SRS = sRS;
	}

	public String getCoorX() {
		return CoorX;
	}

	public void setCoorX(String coorX) {
		CoorX = coorX;
	}

	public String getCoorY() {
		return CoorY;
	}

	public void setCoorY(String coorY) {
		CoorY = coorY;
	}

	public String getCoorZ() {
		return CoorZ;
	}

	public void setCoorZ(String coorZ) {
		CoorZ = coorZ;
	}

	public String getDistancia() {
		return distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
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
