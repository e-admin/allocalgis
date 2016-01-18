/**
 * DAOFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public abstract class DAOFactory implements IDAOFactory
{
    
    public abstract ILocalGISPlanesObraDAO getLocalGISPlanesObraDAO();
    public abstract ILocalGISNotesDAO getLocalGISNotesDAO();
    public abstract ILocalGISInterventionsDAO getLocalGISInterventionsDAO();
    public abstract IUserValidationDAO getUserValidationDAO();
    
    public static DAOFactory getDAOFactory()
    {
        
        String classDaoFactory = null;
        Context initCtx;
        try
        {
            initCtx = new InitialContext( );
            classDaoFactory = (String) initCtx.lookup("java:comp/env/civilWorkDaoFactory");
            return (DAOFactory) Class.forName(classDaoFactory).newInstance();
            
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (NamingException e)
        {
            e.printStackTrace();
        }
       
        return null;
    }
}
