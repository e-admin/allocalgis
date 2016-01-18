/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * JRSpringLoadedHibernateConnection.java
 * 
 */

package it.businesslogic.ireport.connection;

import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JRSpringLoadedHibernateConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;

import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 
 * @author Jeffrey Payne
 *
 */

public class JRSpringLoadedHibernateConnection extends JRHibernateConnection {
	
	private final static String PROP_KEY_SPRING_CONFIG = "spring.loaded.hibernate.spring.config";
	private final static String PROP_KEY_SESSION_FACTORY_ID = "spring.loaded.hibernate.session.factory.id";
	
	private String springConfig = null;
	private String sessionFactoryBeanId = null;
	

	
	public ApplicationContext getApplicationContext() {
		
                StringTokenizer parser = new StringTokenizer(getSpringConfig(), ",");
                String[] configs = new String[parser.countTokens()];
                int iCount = 0;
                while (parser.hasMoreTokens()) {
                        configs[iCount++] = parser.nextToken();
                }
                return new ClassPathXmlApplicationContext(configs);
	}

	public String getSessionFactoryBeanId() {
		return sessionFactoryBeanId;
	}


	public void setSessionFactoryBeanId(String sessionFactoryBeanId) {
		this.sessionFactoryBeanId = sessionFactoryBeanId;
	}


	public String getSpringConfig() {
		return springConfig;
	}


	public void setSpringConfig(String springConfig) {
		this.springConfig = springConfig;
	}
	
	 public SessionFactory getSessionFactory() {
		 
		 return (SessionFactory)getApplicationContext().getBean(getSessionFactoryBeanId());
		 
	 }
	 
        /*
         *  This method return all properties used by this connection
         */
        public java.util.HashMap getProperties()
        {    
            java.util.HashMap map = new java.util.HashMap();
            map.put(PROP_KEY_SESSION_FACTORY_ID, getSessionFactoryBeanId());
            map.put(PROP_KEY_SPRING_CONFIG, getSpringConfig());
            return map;
        }

        public void loadProperties(java.util.HashMap map)
        {
            setSessionFactoryBeanId((String)map.get(PROP_KEY_SESSION_FACTORY_ID));
            setSpringConfig((String)map.get(PROP_KEY_SPRING_CONFIG));
        }
        
        public String getDescription(){ return I18n.getString("connectionType.hibernateSpring", "Spring loaded Hibernate connection"); }
	
        
        public IReportConnectionEditor getIReportConnectionEditor()
        {
            return new JRSpringLoadedHibernateConnectionEditor();
        }
         
        
        public void test() throws Exception
        {
            try {
                    Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
                    
                    SessionFactory sf = getSessionFactory();
                    if (sf == null) {
                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.noSessionFactoryReturned","No session factory returned.  Check your session factory bean id against the spring configuration."),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.hibernateConnectionTestSuccessful","iReport successfully created a Hibernate session factory from your Spring configuration."),"",JOptionPane.INFORMATION_MESSAGE);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),e.getMessage(), I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);

            }
        }
}
