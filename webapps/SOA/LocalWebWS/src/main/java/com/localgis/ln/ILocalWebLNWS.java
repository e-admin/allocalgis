/**
 * ILocalWebLNWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ln;

import java.util.Collection;

import org.codehaus.xfire.MessageContext;

import com.localgis.model.ot.PoiOT;

public interface ILocalWebLNWS {
	
	public Collection obtenerListaCapas(int idMunicipio, MessageContext context);	
	public String altaPOI(PoiOT poi, MessageContext context);
	public String bajaPOI(int idContenido, MessageContext context);
	public String verPlano(String nombrePlano, double coordX, double coordY, double alturaPlano, double anchoPlano, double escala);
}