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
 * UpgradeSearch.java
 * 
 * Created on 22 maggio 2005, 2.00
 *
 */

package it.businesslogic.ireport.plugin.checkupdate;

import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import java.net.Proxy;
import java.net.URLConnection;

/**
 *
 * @author  Administrator
 */
public class UpgradeSearch implements Runnable {
    
    private CheckUpdateDialog win = null;
    public void run() {
        try {
            //Thread.currentThread().setContextClassLoader(it.businesslogic.ireport.gui.MainFrame.getMainInstance().getReportClassLoader() );
                        
            
            
            java.util.Properties props = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties();
            
            
            if (props.getProperty("update.useProxy", "false").equals("true"))
            {
                System.getProperties().put( "proxySet", "true" );
                
                String urlProxy = props.getProperty("update.proxyUrl", "");
                String port = "8080";
                String server = urlProxy;
                if (urlProxy.indexOf(":") > 0)
                {
                    port = urlProxy.substring(urlProxy.indexOf(":") + 1);
                    server = urlProxy.substring(0, urlProxy.indexOf(":"));
                }
                
                System.getProperties().put( "proxyHost", server );
                System.getProperties().put( "proxyPort", port );

                MainFrame.getMainInstance().logOnConsole("Using proxy: " + urlProxy);
                
            }
            
            java.net.URL url = new java.net.URL("http://ireport.sf.net/lastversion.php");

            
            
            byte[] webBuffer = new byte[100];
            URLConnection uConn = url.openConnection();
            
            if (props.getProperty("update.useProxy", "false").equals("true") &&
                props.getProperty("update.useAuth", "false").equals("true"))
            {
                String password = props.getProperty("update.username", "") + ":" + props.getProperty("update.password", "");
                
                org.w3c.tools.codec.Base64Encoder b = new org.w3c.tools.codec.Base64Encoder(password);
		String encodedPassword = b.processString();
                uConn.setRequestProperty( "Proxy-Authorization", encodedPassword );
            }
            
            //uConn.setReadTimeout(1000);
            java.io.InputStream is = uConn.getInputStream();
            int readed = is.read(webBuffer);
            String version = new String(webBuffer,0,readed);
            if (version.toLowerCase().startsWith("ireport")) {
                if (version.compareTo(it.businesslogic.ireport.gui.MainFrame.constTitle) > 0) {
                    
                    final String fversion = version;
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        javax.swing.JOptionPane.showMessageDialog(it.businesslogic.ireport.gui.MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.upgradeSearch.newVersion",
                                "{0} is available on http://ireport.sf.net!", new Object[]{fversion}) );
                    }});
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        
        if (win != null)   win.setVisible(false);
    }

    public CheckUpdateDialog getWin() {
        return win;
    }

    public void setWin(CheckUpdateDialog win) {
        this.win = win;
    }
}

