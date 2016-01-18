/**
 * ISOALocalGISDAOWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.codehaus.xfire.MessageContext;

import com.localgis.exception.AclNoExistenteException;
import com.localgis.exception.ColumnsNotFoundException;
import com.localgis.exception.LayersNotFoundException;
import com.localgis.exception.MunicipiosNotFoundException;
import com.localgis.exception.NoPermisoException;
import com.localgis.exception.PasswordNoValidoException;
import com.localgis.exception.PeticionNumeroErroneaException;
import com.localgis.exception.PeticionViaErroneaException;
import com.localgis.exception.PoiExistenteException;
import com.localgis.exception.PoiNoExistenteException;
import com.localgis.exception.SubtipoNoExistenteException;
import com.localgis.exception.TipoNoExistenteException;
import com.localgis.exception.URLNoExistenteException;
import com.localgis.exception.URLReportMapNotFoundException;
import com.localgis.exception.UsuarioNoExistenteException;
import com.localgis.model.ot.BienPreAltaOT;
import com.localgis.model.ot.CalleOT;
import com.localgis.model.ot.NumeroOT;
import com.localgis.model.ot.PoiOT;
import com.localgis.model.ot.URLsPlano;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

 
public interface ISOALocalGISDAOWS
{ 
        public Collection obtenerListaCapas (Connection connection, int idMunicipio) throws SQLException, ParseException;
        public String insertaBienPAalta(Connection connection, BienPreAltaOT poiOT) throws Exception, SQLException,ParseException;
        public String altaPOI(Connection connection, PoiOT poiOT) throws SQLException, ParseException, PoiExistenteException, TipoNoExistenteException, SubtipoNoExistenteException;
        public String bajaPOI(Connection connection, int idContenido) throws SQLException, ParseException, PoiNoExistenteException;
        public URLsPlano verPlanoPorCoordenadas(Connection connection, int idPlano, double coordX, double coordY, int alturaPlano, int anchoPlano, int escala, int idEntidad) throws SQLException, ParseException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException, LocalgisMapNotFoundException, URLNoExistenteException;
        public URLsPlano verPlanoPorReferenciaCatastral(Connection connection, int idPlano, String refCatastral, int alturaPlano, int anchoPlano, int escala, int idEntidad) throws SQLException, ParseException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException, LocalgisMapNotFoundException, URLNoExistenteException;
        public URLsPlano verPlanoPorIdVia(Connection connection, int idPlano, int idVia, int alturaPlano, int anchoPlano, int escala, int idEntidad) throws SQLException, ParseException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException, LocalgisMapNotFoundException, URLNoExistenteException;
        public URLsPlano verPlanoPorIdNumeroPolicia(Connection connection, int idPlano, int idNumeroPolicia, int alturaPlano, int anchoPlano, int escala, int idEntidad) throws SQLException, ParseException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException, LocalgisMapNotFoundException, URLNoExistenteException;        
        public String comprobarPermiso(Connection connection, Integer idUsuario, String constPermiso) throws SQLException, UsuarioNoExistenteException, PasswordNoValidoException, NumberFormatException, AclNoExistenteException, NoPermisoException;
        public Integer obtenerUsuario(Connection connection, String nombreUsuario, String passwordUsuario, MessageContext context) throws PasswordNoValidoException, SQLException, UsuarioNoExistenteException;
        public Boolean validarReferencia(Connection connection, String refCatastral) throws SQLException;
        public Collection consultarCatastro(Connection connection, String refCatastral) throws SQLException;
        public Collection verPlanosPublicados(Connection connection, int idEntidad) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisMapNotFoundException;
        
        public Collection verMunicipiosPublicados(Connection connection) throws LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException, MunicipiosNotFoundException;
        public String altaCallejero(Connection connection, CalleOT calle) throws SQLException, PeticionViaErroneaException;
        public String bajaCallejero(Connection connection, int idVia, int idMunicipio, String idalp, String claseVia, String denominacion, Date fechaEjecucion) throws SQLException, PeticionViaErroneaException;
        public String modificacionCallejero(Connection connection, int idVia, CalleOT calle) throws SQLException, PeticionViaErroneaException;
        public String altaNumerero(Connection connection, NumeroOT numero, String tipoVia, String nombreVia) throws SQLException, PeticionNumeroErroneaException;
        public String modificacionNumerero(Connection connection, NumeroOT numero, String tipoVia, String nombreVia) throws SQLException, PeticionNumeroErroneaException;
        public String bajaNumerero(Connection connection, NumeroOT numero, String tipoVia, String nombreVia) throws SQLException, PeticionNumeroErroneaException;
        public Collection obtenerProvincias(Connection connection) throws SQLException;
        public Collection obtenerMunicipios(Connection connection, Integer codigoProvinciaINE) throws SQLException;
        public Collection obtenerEntidadMunicipios(Connection connection, Integer codigoINE) throws SQLException;
        public Collection obtenerTiposDeVia(Connection connection) throws SQLException;
        public Collection selectLayersByIdMap(Connection connection, Integer idMap) throws LayersNotFoundException,	LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException;
        public Collection selectColumnsBylayerTranslated(Connection connection, Integer idLayer, String locale) throws ColumnsNotFoundException, LocalgisDBException, LocalgisConfigurationException, LocalgisInitiationException;
        public HashMap getURLReportMap(Connection connection,String imageKey, String idMap, String table, String column,	Object selectionId, String scale, int height, int width, String style, String idMunicipio, String publicMap, String layerName) throws URLReportMapNotFoundException, NumberFormatException, LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException, LocalgisInitiationException; 
}

