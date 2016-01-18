/**
 * LocalWebLNWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ln;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.ws.security.handler.WSHandlerConstants;
import org.codehaus.xfire.MessageContext;

import com.localgis.exception.AclNoExistenteException;
import com.localgis.exception.NoPermisoException;
import com.localgis.exception.PasswordNoValidoException;
import com.localgis.exception.PoiExistenteException;
import com.localgis.exception.PoiNoExistenteException;
import com.localgis.exception.SubtipoNoExistenteException;
import com.localgis.exception.TipoNoExistenteException;
import com.localgis.exception.UsuarioNoExistenteException;
import com.localgis.model.dao.DAOFactoryWS;
import com.localgis.model.dao.ILocalWebDAOWS;
import com.localgis.model.ot.PoiOT;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.Constants;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class LocalWebLNWS implements ILocalWebLNWS {
	
	public Collection obtenerListaCapas(int idMunicipio, MessageContext context) {
		
		Connection connection = null;
        try {  
        	
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();            
            localWebDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_READ, Constants.ACL_PUNTOSINTERES);            
            return localWebDao.obtenerListaCapas(connection, idMunicipio); 
            
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
            // TODO Auto-generated catch block
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
	
	class WSException extends RuntimeException
    {

        
        /**
         * 
         */
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

	

	public String bajaPOI(int idContenido, MessageContext context) {
		Connection connection = null;
        try {          
        	
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();
            localWebDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_DELETE, Constants.ACL_PUNTOSINTERES);
            return localWebDao.bajaPOI(connection, idContenido);
            
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

	public String verPlano(String nombrePlano, double coordX, double coordY,
			double alturaPlano, double anchoPlano, double escala) {
		Connection connection = null;
        try {          
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();
            return localWebDao.verPlano(connection, nombrePlano, coordX, coordY, alturaPlano, anchoPlano, escala);                         
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
        
        finally {
            ConnectionUtilities.closeConnection(connection);
            
        } 
	}

	public String altaPOI(PoiOT poiOT, MessageContext context){
		
		Connection connection = null;
        try {      
   
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();
            localWebDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_ADD, Constants.ACL_PUNTOSINTERES);
            return localWebDao.altaPOI(connection, poiOT);
            
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

	public String comprobarPermisoLogin(MessageContext context){
		
		Connection connection = null;
		
        try {          
        	
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();
            return localWebDao.comprobarPermiso(connection, (Integer)context.getProperty(WSHandlerConstants.USER), Constants.PERM_LOGIN, Constants.ACL_LOCALWEB);
            
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

public Integer obtenerUsuario(String nombreUsuario, String passwordUsusario){
		
		Connection connection = null;
		
        try {          
        	
            DAOFactoryWS daoFactory = DAOFactoryWS.getDAOFactory();
            connection = daoFactory.getConnection(daoFactory.getDataSource());
            ILocalWebDAOWS localWebDao = daoFactory.getLocalWebDAO();
            return localWebDao.obtenerUsuario(connection, nombreUsuario, passwordUsusario);
            
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

	
}