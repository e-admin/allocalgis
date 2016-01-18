/**   
 * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
 * All Rights Reserved
 * www.cincom.com
 *
 * JRXMLADataSourceConnection.java
 * @authors MPenningroth, HMays.
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.XMLADataSourceConnectionEditor;
import it.businesslogic.ireport.data.olap.CustomHTTPAuthenticator;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.util.Misc;
import java.net.Authenticator;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.query.JRXmlaQueryExecuterFactory;

public class JRXMLADataSourceConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String url;
    
    private String username;
    
    private String password = null;
    
    private boolean savePassword;
    
    
    /** Creates a new instance of JRXMLADataSourceConnection */
    public JRXMLADataSourceConnection() {
        
    }
    
    /**
     * Returns the url of XMLA Server.
     * @return Value of property url.
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * Setter method for url.
     * @param <code>String</code> assigns value of the  <code>url </code>
     */
    public void setUrl(String url) {
        this.url = url;
    }

    
    public java.util.HashMap getProperties() {
        java.util.HashMap map = new java.util.HashMap();
        map.put("url", Misc.nvl(this.getUrl(),"") );
        map.put("datasource", Misc.nvl(this.getDatasource(),"") );
        map.put("catalog", Misc.nvl(this.getCatalog(),"") );
        map.put("cube", Misc.nvl(this.getCube(),"") ); 
        map.put("Username", Misc.nvl(this.getUsername(),""));
        if (this.isSavePassword())
            map.put("Password", Misc.nvl(this.getPassword(),""));
        else map.put("Password","");
        map.put("SavePassword", ""+this.isSavePassword());
        
        return map;
    }
    /**
     * Assigns the values of Url, Datasource, Catalog, Cube
     */
    public void loadProperties(java.util.HashMap map) {
        this.setUrl( (String)map.get("url"));
        this.setDatasource((String)map.get("datasource"));
        this.setCatalog((String)map.get("catalog"));
        this.setCube((String)map.get("cube")); 
        
        this.setUsername( (String)map.get("Username"));
        this.setSavePassword(  (""+map.get("SavePassword")).equals("true") );
        if (this.isSavePassword())
            this.setPassword( Misc.nvl((String)map.get("Password"),""));
    }
    
    /**
     * Holds value of property datasource.
     */
    private String datasource;
    
    /**
     * Getter for property datasource.
     * @return Value of property datasource.
     */
    public String getDatasource() {
        return this.datasource;
    }
    
    /**
     * Setter for property datasource.
     * @param datasource New value of property datasource.
     */
    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }
    
    /**
     * Holds value of property catalog.
     */
    private String catalog;
    
    /**
     * Getter for property catalog.
     * @return Value of property catalog.
     */
    public String getCatalog() {
        return this.catalog;
    }
    
    /**
     * Setter for property catalog.
     * @param catalog New value of property catalog.
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    
    /**
     * Holds value of property cube.
     */
    private String cube;
    
    /**
     * Getter for property cube.
     * @return Value of property cube.
     */
    public String getCube() {
        return this.cube;
    }
    
    /**
     * Setter for property cube.
     * @param cube New value of property cube.
     */
    public void setCube(String cube) {
        this.cube = cube;
    }
    

    /*
   * Return true if this ireport connection can be used using getJRDataSource
   * I.E. you can see JDBCConnection
   */
  public boolean isJRDataSource() { return false; }
  

  /**
     * This method is call before the datasource is used and permit to add special parameters to the map
     *
     */
    public Map getSpecialParameters(Map map) throws net.sf.jasperreports.engine.JRException
    {
       //System.out.println("Starting XMLA MDX Query");
      
        map.put(JRXmlaQueryExecuterFactory.PARAM_XMLA_URL, getUrl());
        map.put(JRXmlaQueryExecuterFactory.PARAM_XMLA_DS, getDatasource());
        map.put(JRXmlaQueryExecuterFactory.PARAM_XMLA_CAT, getCatalog());
        
        
        
        map.put(net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory.PARAMETER_XMLA_URL, getUrl());
        map.put(net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory.PARAMETER_XMLA_DATASOURCE, getDatasource());
        map.put(net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory.PARAMETER_XMLA_CATALOG, getCatalog());
        
        
        if (username != null && username.length() > 0)
        {
            String tmpPassword = getPassword();
            Authenticator.setDefault(new CustomHTTPAuthenticator( username,tmpPassword));
            map.put(net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory.PARAMETER_XMLA_USER, getUsername());
            map.put(net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuterFactory.PARAMETER_XMLA_PASSWORD, tmpPassword);
        }
        
        //System.out.println("XMLA MDX Query completed");
        
        return map;
    }
    
    /**
     * This method is call after the datasource is used to dispose special parameters
     * (i.e. closing an Hibernate session create as parameter with a getSpecialParameters...
     *
     */
    public Map disposeSpecialParameters(Map map)
    {
        return map;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
       if (isSavePassword()) return password;
       else
       {
           // Ask for password...
           try {
                    return it.businesslogic.ireport.gui.PasswordDialog.askPassword();
           } catch (Exception ex) {
                ex.printStackTrace();
           }
       }
       return "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSavePassword() {
        return savePassword;
    }

    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }
    

    public String getDescription(){ return I18n.getString("connectionType.xmlaServer", "XMLA Server"); }
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new XMLADataSourceConnectionEditor();
    }
    
    
    public void test() throws Exception
    {
        /**   
             * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
             * All Rights Reserved
             * www.cincom.com
             *
             */
            String urlstr = this.getUrl();
            Authenticator.setDefault(new CustomHTTPAuthenticator( getUsername(), getPassword() ) );
        
                String dataSource = this.getDatasource();
                String catalog = this.getCatalog();
                rex.metadata.ServerMetadata smd = new rex.metadata.ServerMetadata(urlstr,MainFrame.getMainInstance());
                if (smd.isValidUrl() == false) {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getString("messages.connectionDialog.connectionXMLATestFailed.InvalidUrl","Connection test failed! Unable to connect to url."),
                            "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                rex.graphics.datasourcetree.elements.DataSourceTreeElement dste[] = smd.discoverDataSources();
                if (dste == null || dste.length == 0) {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getString("messages.connectionDialog.connectionXMLATestFailed.NoDatasources","Connection test failed! No datasources found."),
                            "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                boolean found = false;
                int intI =0;
                if (dataSource != null && dataSource.length() > 0) {
                    while (!found && intI < dste.length) {
                        if (dataSource.compareTo(dste[intI].getDataSourceInfo()) == 0){
                            found = true;
                        } else {
                            intI++;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connectionDialog.connectionXMLATestFailed.NoDatasourceFound","Connection test failed! Datasource {0} not found.", new Object[]{dataSource+""} ),
                            "",JOptionPane.INFORMATION_MESSAGE);
                        return;                        
                    }
                    if (catalog != null && catalog.length() > 0) {
                        found = false;
                        rex.graphics.datasourcetree.elements.DataSourceTreeElement cats[] = dste[intI].getChildren();
                        if (cats == null || cats.length == 0) {
                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestFailed.NoCatalogs","Connection test failed! No catalogs found in datasource."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                            return;                                                    
                        }
                        intI = 0;
                        while (!found && intI < cats.length) {
                            if (catalog.compareTo(
                              ((rex.graphics.datasourcetree.elements.CatalogElement)cats[intI]).toString()) == 0){
                                found = true;
                            }
                            else{
                                intI++;
                            }
                        }
                        if (!found) {
                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.connectionDialog.connectionXMLATestFailed.NoCatalogFound","Connection test failed! Catalog {0} not found in datasource.", new Object[]{catalog+""} ),
                                "",JOptionPane.INFORMATION_MESSAGE);
                            return;                                                                                
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful.NoCatalog","Connection test successful! Connected to server and found datasource, but no catalog specified."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful.NoDatasource","Connection test successful! Connected to server, but no datasource specified."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                        I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful","Connection test successful! Catalog found in datasource on xmla server."),
                        "",JOptionPane.INFORMATION_MESSAGE);                    
                /* end of modification */
                
    }
}
