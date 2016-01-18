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
 * IReportPlugin.java
 * 
 * Created on 19 maggio 2004, 7.23
 *
 */

package it.businesslogic.ireport.plugin;

import it.businesslogic.ireport.gui.MainFrame;
/**
 * This class must be extended by all iReport plugin.
 * To install a plugin into iReport, put the plugin xml in the plugin directory of iReport.
 * See plugin documentation on how to create a plugin for iReport
 * 
 * This is the first very simple interface to plugin. I hope to don't change it, but we can't say
 * what it'll happen in he future...
 *
 * @author  Administrator
 */
public abstract class IReportPlugin {
    
    MainFrame mainFrame = null;
    String name = "";
        
    /**
     *  This method is called when the plugin is selected from the plugin menu
     */
    public abstract void call();
    
    /**
     *  This method is called when the plugin configuration button on plugin list is selected.
     *  Configuration file of plugin should be stored in IREPORT_USER_HOME_DIR/plugins/
     */
    public void configure(){}

    /**
     *  Retrive the plugin name. Please note that the plugin name must be unique and should be used as
     *  filename for the configuration file if needed. This name can be different from the name specified
     *  in XML, that is the name used for the menu item.
     */
    public String getName(){
        return name;
    }
    
    /** Getter for property mainFrame.
     * @return Value of property mainFrame.
     *
     */
    public it.businesslogic.ireport.gui.MainFrame getMainFrame() {
        return mainFrame;
    }
    
    /** Setter for property mainFrame.
     * @param mainFrame New value of property mainFrame.
     *
     */
    public void setMainFrame(it.businesslogic.ireport.gui.MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
}
