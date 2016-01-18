/**
 * ILocalWebDAOWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.localgis.exception.AclNoExistenteException;
import com.localgis.exception.NoPermisoException;
import com.localgis.exception.PasswordNoValidoException;
import com.localgis.exception.PoiExistenteException;
import com.localgis.exception.PoiNoExistenteException;
import com.localgis.exception.SubtipoNoExistenteException;
import com.localgis.exception.TipoNoExistenteException;
import com.localgis.exception.UsuarioNoExistenteException;
import com.localgis.model.ot.PoiOT;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


public interface ILocalWebDAOWS
{ 
        public Collection obtenerListaCapas (Connection connection, int idMunicipio) throws SQLException, ParseException;
        public String altaPOI(Connection connection, PoiOT poiOT) throws SQLException, ParseException, PoiExistenteException, TipoNoExistenteException, SubtipoNoExistenteException;
        public String bajaPOI(Connection connection, int idContenido) throws SQLException, ParseException, PoiNoExistenteException;
        public String verPlano(Connection connection, String nombrePlano, double coordX, double coordY, double alturaPlano, double anchoPlano, double escala) throws SQLException, ParseException;
        public String comprobarPermiso(Connection connection, Integer idUsuario, String constPermiso, String constAcl) throws SQLException, UsuarioNoExistenteException, PasswordNoValidoException, NumberFormatException, AclNoExistenteException, NoPermisoException;
        public Integer obtenerUsuario(Connection connection, String nombreUsuario, String passwordUsuario) throws PasswordNoValidoException, SQLException, UsuarioNoExistenteException;
}


