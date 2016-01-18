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
 * SortField.java
 *
 * Created on November 13, 2006, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport;

/**
 *
 * @author gtoffoli
 */
public class SortField {
    
    private String fieldName = "";
    private boolean desc = false;
    
    /** Creates a new instance of SortField */
    public SortField() {
    }
    
    public SortField(SortField sf) {
        
        this.fieldName = sf.getFieldName();
        this.desc = sf.isDesc();
    }
    
    public SortField(String fieldName, boolean desc) {
        
        this.fieldName = fieldName;
        this.desc = desc;
    }
    
    public SortField(String fieldName) {
        this( fieldName, false);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
    
}
