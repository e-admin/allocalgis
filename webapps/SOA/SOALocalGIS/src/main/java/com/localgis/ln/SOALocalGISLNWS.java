/**
 * SOALocalGISLNWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ln;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.ws.security.handler.WSHandlerConstants;
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
import com.localgis.model.dao.DAOFactoryWS;
import com.localgis.model.dao.ISOALocalGISDAOWS;
import com.localgis.model.ot.BienPreAltaOT;
import com.localgis.model.ot.CalleOT;
import com.localgis.model.ot.NumeroOT;
import com.localgis.model.ot.PoiOT;
import com.localgis.model.ot.URLsPlano;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.Constants;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class SOALocalGISLNWS implements ISOALocalGISLNWS {

	class WSException extends RuntimeException
	{

		private static final long serialVersionUID = 1L;
		private String msg;

		public WSException(String string)
		{
			this.msg=string;
		}

		/**
		 * @return the msg
		 */
		 public String getMessage()
		 {
			 return msg;
		 }

	}
	
	public Collection obtenerListaCapas(int idMunicipio, MessageContext context) {

		Connection connection = null;
		try {  

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_READ);
			return soaLocalGISDao.obtenerListaCapas(connection, idMunicipio);
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		}
	}

	public String bajaPOI(int idContenido, MessageContext context) {
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();		
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_DELETE);
			return soaLocalGISDao.bajaPOI(connection, idContenido);
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{            
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());

		} catch (PoiNoExistenteException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		}    
	}



		public String altaPOI(PoiOT poiOT, MessageContext context){

		Connection connection = null;
		try {      

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();	
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD);
			return soaLocalGISDao.altaPOI(connection, poiOT);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		catch (PoiExistenteException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		catch (TipoNoExistenteException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		catch (SubtipoNoExistenteException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		}  
	}

	public URLsPlano verPlanoPorCoordenadas(int idPlano, double coordX, double coordY,
			int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verPlanoPorCoordenadas(connection, idPlano, 
					coordX, coordY, alturaPlano, anchoPlano, escala, idEntidad);

		} catch (SQLException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (LocalgisInvalidParameterException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());		   
		} catch (LocalgisConfigurationException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisMapNotFoundException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (URLNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}	
		
	public URLsPlano verPlanoPorReferenciaCatastral(int idPlano, String refCatastral,
			int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verPlanoPorReferenciaCatastral(connection, idPlano, refCatastral, alturaPlano, anchoPlano, escala, idEntidad);

		} catch (SQLException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (LocalgisInvalidParameterException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		   
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisMapNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (URLNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
	
	public URLsPlano verPlanoPorIdVia(int idPlano, int idVia,
			int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verPlanoPorIdVia(connection, idPlano, 
					idVia, alturaPlano, anchoPlano, escala, idEntidad);

		} catch (SQLException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (LocalgisInvalidParameterException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());		   
		} catch (LocalgisConfigurationException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisMapNotFoundException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (URLNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
		
	public URLsPlano verPlanoPorIdNumeroPolicia(int idPlano, int idNumeroPolicia,
			int alturaPlano, int anchoPlano, int escala, int idEntidad, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verPlanoPorIdNumeroPolicia(connection, idPlano, 
					idNumeroPolicia, alturaPlano, anchoPlano, escala, idEntidad);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (LocalgisInvalidParameterException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());		   
		} catch (LocalgisConfigurationException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisMapNotFoundException e) 
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (URLNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
	
	public String comprobarPermisoLogin(MessageContext context){

		Connection connection = null;

		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			return soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_LOGIN);

		} catch (SQLException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {

			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (UsuarioNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (PasswordNoValidoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (AclNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (NoPermisoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		}
		finally {

			ConnectionUtilities.closeConnection(connection);

		}

		return null;
	}

	public Integer obtenerUsuario(String nombreUsuario, String passwordUsusario, MessageContext context){

		Connection connection = null;

		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			return soaLocalGISDao.obtenerUsuario(connection, nombreUsuario, passwordUsusario, context);

		} catch (SQLException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {

			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (UsuarioNoExistenteException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		} catch (PasswordNoValidoException e) {

			e.printStackTrace();
			throw new WSException(e.getMessage());        
		}
		finally {
			ConnectionUtilities.closeConnection(connection);
		}

		return null;
	}

	public Boolean validarReferencia(String refCatastral, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VALIDATION_REFCAT);
			return soaLocalGISDao.validarReferencia(connection, refCatastral);                         
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);
		} 
	}

	public Collection consultarCatastro(String refCatastral, MessageContext context) {
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_QUERY_CATASTRO);
			return soaLocalGISDao.consultarCatastro(connection, refCatastral);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}

		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}

	public Collection verPlanosPublicados(int idEntidad,
			MessageContext context) {

		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verPlanosPublicados(connection, idEntidad);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInvalidParameterException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisMapNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
	
	public Collection selectLayersByIdMap(int idMap,
			MessageContext context) {

		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.selectLayersByIdMap(connection, new Integer(idMap));

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LayersNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
	
	public Collection selectColumnsByLayerTranslated(int idLayer, String locale,
			MessageContext context) {

		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.selectColumnsBylayerTranslated(connection, new Integer(idLayer), locale);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ColumnsNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		} 
	}
	
	public Collection verMunicipiosPublicados(MessageContext context) {

		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.verMunicipiosPublicados(connection);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());

		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (MunicipiosNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);
		}

	} 

	public String altaCallejero(CalleOT calle, MessageContext context) {
		
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD);
			return soaLocalGISDao.altaCallejero(connection, calle);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PeticionViaErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		}finally {
			ConnectionUtilities.closeConnection(connection);
		}   
		
	}

	public String bajaCallejero(int idVia, int idMunicipio, String idalp, 
			String claseVia, String denominacion, Date fechaEjecucion,
			MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();	
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_DELETE);
			return soaLocalGISDao.bajaCallejero(connection, idVia,idMunicipio, idalp, claseVia, denominacion, fechaEjecucion);
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PeticionViaErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}finally {
			ConnectionUtilities.closeConnection(connection);

		}    
	}

	public String modificacionCallejero(int idVia, CalleOT calle,
			MessageContext context) {
		
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD);
			return soaLocalGISDao.modificacionCallejero(connection, idVia, calle);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (PeticionViaErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());	
		}finally {
			ConnectionUtilities.closeConnection(connection);
		}  
	}

	public String altaNumerero(NumeroOT numero, String tipoVia, String nombreVia, 
			MessageContext context) {
		
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD);
			return soaLocalGISDao.altaNumerero(connection, numero, tipoVia, nombreVia);

		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PeticionNumeroErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}finally {
			ConnectionUtilities.closeConnection(connection);
		} 
	}

	public String modificacionNumerero(NumeroOT numero, String tipoVia, String nombreVia,
			MessageContext context) {
		
		Connection connection = null;
		try {          
			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD);
			return soaLocalGISDao.modificacionNumerero(connection, numero, tipoVia, nombreVia);

		} catch (SQLException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (PeticionNumeroErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		}finally {
			ConnectionUtilities.closeConnection(connection);
		}
	}

	public String bajaNumerero(NumeroOT numero, String tipoVia, String nombreVia, 
			MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_DELETE);
			return soaLocalGISDao.bajaNumerero(connection, numero, tipoVia, nombreVia);
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());		
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PeticionNumeroErroneaException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}finally {
			ConnectionUtilities.closeConnection(connection);
		} 
	}

	public Collection obtenerProvincias(MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.obtenerProvincias(connection);
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
	}

	public Collection obtenerMunicipios(Integer codigoProvinciaINE,
			MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.obtenerMunicipios(connection, codigoProvinciaINE);
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
	}
	
	public Collection obtenerEntidadMunicipios(Integer codigoINE, MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.obtenerEntidadMunicipios(connection, codigoINE);
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
	}

	public Collection obtenerTiposDeVia(MessageContext context) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.obtenerTiposDeVia(connection);
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
	}
	
	public HashMap getURLReportMap(MessageContext context,String imageKey, String idMap, String table, String column,
    		Object selectionId, String scale, int height, int width, String style, String idEntidad, String publicMap, String layerName) {
		
		Connection connection = null;
		try {          

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_VIEW);
			return soaLocalGISDao.getURLReportMap(connection, imageKey, idMap, table, column, selectionId, scale, height, width, style, idEntidad, publicMap, layerName);
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NamingException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (UsuarioNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (PasswordNoValidoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (AclNoExistenteException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (NoPermisoException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (URLReportMapNotFoundException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInvalidParameterException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisConfigurationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisDBException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (LocalgisInitiationException e) {
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}finally {
			ConnectionUtilities.closeConnection(connection);
		} 
	}
/**
 * Inserta un Bien en Prealta
 */
	public String insertBienPreAlta(BienPreAltaOT bienPAOT, MessageContext context){

		Connection connection = null;
		try {      

			DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
			connection = daoFactory.getConnection(daoFactory.getDataSource());
			ISOALocalGISDAOWS soaLocalGISDao = daoFactory.getSOALocalGISDAO();			
			soaLocalGISDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_INSERT_BIEN_PALTA);
			return soaLocalGISDao.insertaBienPAalta(connection, bienPAOT);

		} catch (SQLException e){
			e.printStackTrace();
			throw new WSException(e.getMessage());
		} catch (ParseException e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WSException(e.getMessage());
		}
		finally {
			ConnectionUtilities.closeConnection(connection);

		}  
	}
}