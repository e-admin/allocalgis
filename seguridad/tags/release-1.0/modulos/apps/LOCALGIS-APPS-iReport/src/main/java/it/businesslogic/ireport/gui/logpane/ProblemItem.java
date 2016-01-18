/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ProblemItem.java
 *
 * Created on February 27, 2007, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.logpane;

import it.businesslogic.ireport.gui.JReportFrame;

/**
 *
 * @author gtoffoli
 */
public class ProblemItem {
    
    public static final int INFORMATION = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    
    
    private String description = null;
    private String where = "";
    private JReportFrame jReportFrame = null;
    private Object problemReference = null;
    
    private int problemType = 1;
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem() {
        this(0,"no description available", null);
    }
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, String description, Object problemReference) {
        this(0,"no description available", null, "");
    }
    
    /** Creates a new instance of ProblemItem */
    public ProblemItem(int problemType, String description, Object problemReference, String where) {
        this.problemType = problemType;
        this.description = description;
        this.problemReference = problemReference;
        this.where = where;
    }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getProblemReference() {
        return problemReference;
    }

    public void setProblemReference(Object problemReference) {
        this.problemReference = problemReference;
    }

    public int getProblemType() {
        return problemType;
    }

    public void setProblemType(int problemType) {
        this.problemType = problemType;
    }
    
    public void resolve()
    {
        
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
    
}
