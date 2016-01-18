package com.localgis.webservices.civilwork.model.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public abstract class DAOFactory implements IDAOFactory
{
    

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
