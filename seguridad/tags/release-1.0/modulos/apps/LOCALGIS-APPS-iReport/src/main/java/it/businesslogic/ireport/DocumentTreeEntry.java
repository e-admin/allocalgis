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
 * DocumentTreeEntry.java
 * 
 * Created on 1 giugno 2003, 0.43
 *
 */

package it.businesslogic.ireport;
import it.businesslogic.ireport.gui.*;
/**
 *
 * @author  Administrator
 */
public class DocumentTreeEntry {
    
    private JReportFrame jrf;
    private String filename; // used only if the report is closed (jrf == null)
    
    /** Creates a new instance of DocumentTreeEntry */
    public DocumentTreeEntry(JReportFrame jrf, String filename) {
        this.jrf = jrf;
        this.filename = filename;
    }
    
    public String toString()
    {
        if (filename == null && jrf == null) return "Unknow doc";
        
        if (jrf == null)
        {
            java.io.File file = new java.io.File(filename);
            return file.getName();
        } 

        if (jrf.getReport().getFilename() == null || jrf.getReport().getFilename().equals(""))
            return "["+jrf.getReport().getName()+"]";
        else
        {
            java.io.File file = new java.io.File(jrf.getReport().getFilename());
           return file.getName();
        }
    }
    
    /** Getter for property filename.
     * @return Value of property filename.
     *
     */
    public java.lang.String getFilename() {
        return filename;
    }
    
    /** Setter for property filename.
     * @param filename New value of property filename.
     *
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }
    
    /** Getter for property jrf.
     * @return Value of property jrf.
     *
     */
    public it.businesslogic.ireport.gui.JReportFrame getJrf() {
        return jrf;
    }
    
    /** Setter for property jrf.
     * @param jrf New value of property jrf.
     *
     */
    public void setJrf(it.businesslogic.ireport.gui.JReportFrame jrf) {
        this.jrf = jrf;
    }
    
}
