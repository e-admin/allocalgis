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
 * SampleJRDataSourceFactory.java
 * 
 * Created on 22 giugno 2003, 23.57
 *
 */

package it.businesslogic.ireport.connection;
import java.util.Vector;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

/**
 *
 * @author  Administrator
 */
public class SampleJRDataSourceFactory {
    
    // This is the method to call to get the datasource.
    // The method must be static.....    
    public  JRDataSource createDatasource()
    {
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(4,2);
        
        SampleBean person = new SampleBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        tm.setValueAt(person, 0, 0);
        tm.setValueAt("Test value row 1 col 1", 0, 1);
        
        person = new SampleBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        tm.setValueAt(person, 1, 0);
        tm.setValueAt("Test value row 2 col 1", 1, 1);
        
        person = new SampleBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        tm.setValueAt(person, 2, 0);
        tm.setValueAt("Test value row 3 col 1", 2, 1);
        
        person = new SampleBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        tm.setValueAt(person, 3, 0);
        tm.setValueAt("Test value row 4 col 1", 3, 1);
        
        return new JRTableModelDataSource(tm);
    }    
    
     public  JRDataSource createBeanCollectionDatasource()
    {
        
        
        return new JRBeanCollectionDataSource(createBeanCollection());
    }    
     
     public static  Vector  createBeanCollection()
     {
            java.util.Vector coll = new java.util.Vector();
        
        SampleBean person = new SampleBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        coll.add(person);
        
        person = new SampleBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        coll.add(person);
        
        person = new SampleBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        coll.add(person);
        
        person = new SampleBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        coll.add(person);
    
        return coll;
     }
}
