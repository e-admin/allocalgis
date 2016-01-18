package com.geopista.util;

import javax.servlet.ServletContextEvent;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ServletContextListener
        implements
            javax.servlet.ServletContextListener
{

	public static int numero = 0;
	public static int numeroCat = 0;
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)
    {
    System.out.println("Context destroyed: ");

    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)
    {
    System.out.println("Context initialized: ");
    }

}
