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
 * PluginEntry.java
 * 
 * Created on 19 maggio 2004, 7.59
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class PluginEntry {
       
    private String name = "";
    private String description = "";
    private boolean configurable = false;
    private it.businesslogic.ireport.plugin.IReportPlugin plugin = null;
    private javax.swing.Icon icon = null;
    private String className = null;
    private boolean hide = false;
    private boolean loadOnStartup = false;
    private it.businesslogic.ireport.gui.MainFrame mainFrame = null;
    
    /** Creates a new instance of PluginEntry */
    public PluginEntry() {
    }
    
    /** Getter for property configurable.
     * @return Value of property configurable.
     *
     */
    public boolean isConfigurable() {
        return configurable;
    }    
            
    /** Setter for property configurable.
     * @param configurable New value of property configurable.
     *
     */
    public void setConfigurable(boolean configurable) {
        this.configurable = configurable;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property icon.
     * @return Value of property icon.
     *
     */
    public javax.swing.Icon getIcon() {
        return icon;
    }
    
    /** Setter for property icon.
     * @param icon New value of property icon.
     *
     */
    public void setIcon(javax.swing.Icon icon) {
        this.icon = icon;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /** Getter for property plugin.
     * @return Value of property plugin.
     *
     */
    public it.businesslogic.ireport.plugin.IReportPlugin getPlugin()
    {
        if (plugin == null)
        {
            try {
               //plugin = (it.businesslogic.ireport.plugin.IReportPlugin)getClass().forName(getClassName()).newInstance();
               ReportClassLoader rcl = this.mainFrame.getReportClassLoader();
               rcl.rescanAdditionalClasspath();
                
               try {
                plugin = (it.businesslogic.ireport.plugin.IReportPlugin)rcl.findClass(getClassName()).newInstance();
               } catch (java.lang.ClassNotFoundException ex) 
               {
                   plugin = (it.businesslogic.ireport.plugin.IReportPlugin)getClass().forName(getClassName()).newInstance();
               }
               
                plugin.setMainFrame( this.mainFrame );
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return plugin;
    }
    
    /** Setter for property plugin.
     * @param plugin New value of property plugin.
     *
     */
    public void setPlugin(it.businesslogic.ireport.plugin.IReportPlugin plugin) {
        this.plugin = plugin;
    }
    
    /** Getter for property className.
     * @return Value of property className.
     *
     */
    public java.lang.String getClassName() {
        return className;
    }
    
    /** Setter for property className.
     * @param className New value of property className.
     *
     */
    public void setClassName(java.lang.String className) {
        this.className = className;
    }
    
    /** Getter for property hide.
     * @return Value of property hide.
     *
     */
    public boolean isHide() {
        return hide;
    }
    
    /** Setter for property hide.
     * @param hide New value of property hide.
     *
     */
    public void setHide(boolean hide) {
        this.hide = hide;
    }
    
    /** Getter for property loadOnStartup.
     * @return Value of property loadOnStartup.
     *
     */
    public boolean isLoadOnStartup() {
        return loadOnStartup;
    }
    
    /** Setter for property loadOnStartup.
     * @param loadOnStartup New value of property loadOnStartup.
     *
     */
    public void setLoadOnStartup(boolean loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }
    
    public String toString()
    {
        if (this.getName() != null)
        {
            return this.getName();
        }
        return ""+this;
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
