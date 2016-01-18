/**
 * ISOALocalGISLNWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ln;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;

import org.codehaus.xfire.MessageContext;

import com.localgis.model.ot.BienPreAltaOT;
import com.localgis.model.ot.CalleOT;
import com.localgis.model.ot.NumeroOT;
import com.localgis.model.ot.PoiOT;
import com.localgis.model.ot.URLsPlano;
 
public interface ISOALocalGISLNWS {
		
	public URLsPlano verPlanoPorCoordenadas(int idPlano, double coordX, double coordY, int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context);
	public URLsPlano verPlanoPorReferenciaCatastral(int idPlano, String refCatastral, int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context);
	public URLsPlano verPlanoPorIdVia(int idPlano, int idVia, int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context);
	public URLsPlano verPlanoPorIdNumeroPolicia(int idPlano, int idNumeroPolicia, int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context);	
	public Boolean validarReferencia(String refCatastral, MessageContext context);
	public Collection consultarCatastro(String refCatastral, MessageContext context);
	public Collection obtenerListaCapas(int idMunicipio, MessageContext context);	
	public String altaPOI(PoiOT poi, MessageContext context);
	public String bajaPOI(int idContenido, MessageContext context);
	public String insertBienPreAlta(BienPreAltaOT bienPAOT, MessageContext context);
	public Collection verPlanosPublicados(int idEntidad, MessageContext context);
	public Collection verMunicipiosPublicados(MessageContext context);
	public String altaCallejero(CalleOT calle, MessageContext context);	
	public String bajaCallejero(int idVia, int idMunicipio, String idalp, String claseVia, String denominacion, Date fechaEjecucion, MessageContext context);
	public String modificacionCallejero(int idVia, CalleOT calle, MessageContext context);
	public String altaNumerero(NumeroOT numero, String tipoVia, String nombreVia, MessageContext context);
	public String modificacionNumerero(NumeroOT numero, String tipoVia, String nombreVia, MessageContext context);
	public String bajaNumerero(NumeroOT numero, String tipoVia, String nombreVia, MessageContext context);
	public Collection obtenerProvincias(MessageContext context);
	public Collection obtenerMunicipios(Integer codigoProvinciaINE, MessageContext context);
	public Collection obtenerEntidadMunicipios(Integer codigoINE, MessageContext context);
	public Collection obtenerTiposDeVia(MessageContext context);
	public Collection selectLayersByIdMap(int idMap, MessageContext context);
	public Collection selectColumnsByLayerTranslated(int idLayer, String locale, MessageContext context);
	public HashMap getURLReportMap(MessageContext context,String imageKey, String idMap, String table, String column, Object selectionId, String scale, int height, int width, String style, String idMunicipio, String publicMap, String layerName); 
	
}