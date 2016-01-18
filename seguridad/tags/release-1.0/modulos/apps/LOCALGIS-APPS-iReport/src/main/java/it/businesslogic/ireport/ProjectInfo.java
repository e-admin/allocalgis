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
 * ProjectInfo.java
 * 
 * Created on 4 giugno 2003, 1.43
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class ProjectInfo {
    
    private String name="";
    private String sourcesDir="";
    private String outputDir="";
    private String connection="";
    
    /** Creates a new instance of ProjectInfo */
    public ProjectInfo() {
    }
    
    /** Getter for property connection.
     * @return Value of property connection.
     *
     */
    public java.lang.String getConnection() {
        return connection;
    }
    
    /** Setter for property connection.
     * @param connection New value of property connection.
     *
     */
    public void setConnection(java.lang.String connection) {
        this.connection = connection;
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
    
    /** Getter for property outputDir.
     * @return Value of property outputDir.
     *
     */
    public java.lang.String getOutputDir() {
        return outputDir;
    }
    
    /** Setter for property outputDir.
     * @param outputDir New value of property outputDir.
     *
     */
    public void setOutputDir(java.lang.String outputDir) {
        this.outputDir = outputDir;
    }
    
    /** Getter for property sourcesDir.
     * @return Value of property sourcesDir.
     *
     */
    public java.lang.String getSourcesDir() {
        return sourcesDir;
    }
    
    /** Setter for property sourcesDir.
     * @param sourcesDir New value of property sourcesDir.
     *
     */
    public void setSourcesDir(java.lang.String sourcesDir) {
        this.sourcesDir = sourcesDir;
    }
    
    public String toString()
    {
        return this.getName();
    }
    
}
