/**
 * DAOFactoryWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class DAOFactoryWS implements IDAOFactoryWS
{

    public abstract ILocalWebDAOWS getLocalWebDAO() throws NamingException ;
    
    public static DAOFactoryWS getDAOFactory()
    {
        String classDaoFactory = null;
        Context initCtx;
        try
        {
            initCtx = new InitialContext( );
            classDaoFactory = (String) initCtx.lookup("java:comp/env/daoFactory");
            return (DAOFactoryWS) Class.forName(classDaoFactory).newInstance();
            
        } catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NamingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        return null;
    }
}
