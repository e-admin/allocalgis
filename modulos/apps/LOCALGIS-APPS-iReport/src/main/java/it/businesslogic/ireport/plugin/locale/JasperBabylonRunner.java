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
 * JasperBabylonRunner.java
 * 
 * Created on 22 maggio 2005, 2.00
 *
 */

package it.businesslogic.ireport.plugin.locale;

import it.businesslogic.ireport.gui.MainFrame;

import com.jaspersoft.babylon.JasperBabylonClient;

/**
 *
 * @author  Administrator
 */
public class JasperBabylonRunner implements Runnable {
    
    public static String IREPORT_APP_ID="1";
    private JasperBabylonRunnerListener runnerListener;
    private int operation = 0;
    private JasperBabylonClient jbc = new JasperBabylonClient();
    
    public static final int OP_LIST_LOCALES = 1;
    public static final int OP_GET_LOCALE = 2;
    public static final int OP_PUT_LOCALE = 3;
    
    private String localeId = null;
    private java.util.Properties props = null;
    
    public void listLocales()
    {
        operation = OP_LIST_LOCALES;
        jbc.setAppId( IREPORT_APP_ID  );
        jbc.setJasperBabylonUrl( MainFrame.getMainInstance().getProperties().getProperty("jasperBabylonURL","http://www.jasperforge.org/jasperbabylon"));
        Thread t = new Thread(this);
        t.start();
    }
    
    public void getLocale(String localeId)
    {
        operation = OP_GET_LOCALE;
        jbc.setAppId( IREPORT_APP_ID  );
        jbc.setJasperBabylonUrl( MainFrame.getMainInstance().getProperties().getProperty("jasperBabylonURL","http://www.jasperforge.org/jasperbabylon"));
        this.localeId = localeId;
        Thread t = new Thread(this);
        t.start();
    }
    
    
    public void putLocale(String localeId, java.util.Properties props)
    {
        operation = OP_PUT_LOCALE;
        jbc.setAppId( IREPORT_APP_ID  );
        jbc.setJasperBabylonUrl( MainFrame.getMainInstance().getProperties().getProperty("jasperBabylonURL","http://www.jasperforge.org/jasperbabylon"));
        jbc.setUsername(MainFrame.getMainInstance().getProperties().getProperty("jasperBabylonUsername")  );
        jbc.setPassword(MainFrame.getMainInstance().getProperties().getProperty("jasperBabylonPassword")  );
        this.props = props;
        this.localeId = localeId;
        Thread t = new Thread(this);
        t.start();
        
    }
    
    public void run() {
        try {
            switch (operation)
            {
                case OP_LIST_LOCALES:
                    java.util.List list = jbc.listLocales();
                    this.getRunnerListener().listLocalesComplete(list);
                    break;
                
                case OP_GET_LOCALE:
                    java.util.Properties p = jbc.getLocale( this.localeId );
                    this.getRunnerListener().getLocaleComplete(p);
                    break;
                
                case OP_PUT_LOCALE:
                    String s = jbc.putLocale(this.localeId, props);
                    this.getRunnerListener().putLocaleComplete(s);
                    break;
            }
            
            
        } catch (Exception ex)
        {
            this.getRunnerListener().operationError(operation, ex.getMessage());
        }
    }

    public JasperBabylonRunnerListener getRunnerListener() {
        return runnerListener;
    }

    public void setRunnerListener(JasperBabylonRunnerListener runnerListener) {
        this.runnerListener = runnerListener;
    }
}

