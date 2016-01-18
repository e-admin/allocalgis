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
 * ExtendedBeanDataSourceFactory.java
 * 
 * Created on 22 giugno 2003, 23.57
 *
 */

package it.businesslogic.ireport.examples;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import it.businesslogic.ireport.examples.beans.*;
import it.businesslogic.ireport.connection.JRExtendedBeanDataSource;
import java.util.*;

/**
 *
 * @author  Administrator
 */
public class ExtendedBeanDataSourceFactory {
    
    // This is the method to call to get the datasource.
    // The method must be static.....    
    public  JRDataSource createDatasource()
    {
        Vector v = createVector(); 
        
        return new JRExtendedBeanDataSource( v );
    }    
    
    public static Vector createVector()
    {
        Vector v = new Vector();
        
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        AddressBean address = new AddressBean();
        address.setStreet("Via Buonarroti, 20");
        address.setCountry("Italy");
        person.setAddress( address );
        
        v.add(person);
                
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        
        v.add(person);
        
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        
        v.add(person);
        
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        
        v.add(person);
        
        return v;
    }
}
