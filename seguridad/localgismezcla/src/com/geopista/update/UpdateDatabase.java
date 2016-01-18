/*
 * Created on 09-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.update;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.JProgressBar;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import com.geopista.app.AppContext;
import com.geopista.update.dialogs.UpdateSystemFiles;
import com.geopista.update.dialogs.UpdateSystemPanel;

/**
 * @author jalopez
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UpdateDatabase
{

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Connection conn = null;

    private static String SYSTEM_VERSION = "system version";

    public void updateDatabase(Document doc, String conectionUrl, String password,
            JProgressBar progressBar, int percentage, boolean update) throws SQLException,
            ClassNotFoundException
    {

        Element raiz = doc.getRootElement();
        Element dataBase = raiz.getChild("Database");
        List updatesDatabase = dataBase.getChildren("version");

        Iterator updatesDatabaseIterator = updatesDatabase.iterator();
        conn = getConnection(conectionUrl, password);
        conn.setAutoCommit(true);

        int filesNumber = updatesDatabase.size();
        int percentageStep = 0;
        if (filesNumber > 0)
        {
            percentageStep = percentage / filesNumber;
        }
        int parcialPercertage = 0;
        
        if (update){

        	try
        	{
        		while (updatesDatabaseIterator.hasNext())
        		{

        			int version = Integer.parseInt(UpdateSystemPanel.getActualVersion());
        			Element updateVersion = (Element) updatesDatabaseIterator.next();
        			Attribute versionElement = updateVersion.getAttribute("id");

        			int currentVersion = Integer.parseInt(versionElement.getValue());

        			if (version < currentVersion)
        			{
        				parcialPercertage += percentageStep;
        				List sqlExpressionList = updateVersion.getChildren("sqlExpression");

        				Iterator sqlExpressionListIterator = sqlExpressionList.iterator();

        				while (sqlExpressionListIterator.hasNext())
        				{
        					String currentSqlExpresion = null;
        					try
        					{
        						Element currentSqlExpresionElement = (Element) sqlExpressionListIterator
        						.next();
        						currentSqlExpresion = currentSqlExpresionElement
        						.getText();
        						Attribute sqlTypeAttribute = currentSqlExpresionElement.getAttribute("type");
        						String sqlType = sqlTypeAttribute==null?null:sqlTypeAttribute.getValue();

        						executeSQLExpression(currentSqlExpresion,sqlType);
        						// conn.commit();

        					} catch (SQLException e)
        					{
        						System.out.println("sentencia sql: " + currentSqlExpresion);
        						e.printStackTrace();
        						
        					}
        				}
        			}
        			UpdateSystemFiles.updateProgressBar(progressBar,
        					percentageStep);

        		}

        	} finally
        	{
        		try
        		{
        			conn.close();
        		} catch (Exception e)
        		{
        		}
        	}

        }
        int finalPercentage = percentage - parcialPercertage;
        if (finalPercentage > 0)
            UpdateSystemFiles.updateProgressBar(progressBar, finalPercentage);
    }

    public Connection getConnection(String url, String password) throws SQLException,
            ClassNotFoundException
    {

        if (conn == null || conn.isClosed())
        {

            String user = "geopista";

            String driver = aplicacion.getString("conexion.driver");

            // Quitamos los drivers
            Enumeration e = DriverManager.getDrivers();
            while (e.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
            DriverManager.registerDriver(new org.postgresql.Driver());

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        }
        return conn;
    }

    public void executeSQLExpression(String sqlExpression, String sqlType) throws SQLException
    {
        PreparedStatement preparedStatement = null;
        try
        {
            preparedStatement = conn.prepareStatement(sqlExpression);
            
            if(sqlType!=null && sqlType.equalsIgnoreCase("select"))
            {
                preparedStatement.executeQuery();
            }
            else
            {
                preparedStatement.executeUpdate();
            }
        } finally
        {
            try
            {
                preparedStatement.close();
            } catch (Exception e)
            {
            }
        }

    }

    public static void main(String[] args)
    {
    }
}
