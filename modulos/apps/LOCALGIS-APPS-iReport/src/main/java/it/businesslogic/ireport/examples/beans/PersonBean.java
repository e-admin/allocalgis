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
 * PersonBean.java
 * 
 * Created on 8 luglio 2004, 1.41
 *
 */

package it.businesslogic.ireport.examples.beans;

/**
 *
 * @author  Administrator
 */
public class PersonBean {
    
    private String firstName;
    
    private String lastName;
    
    private HobbyBean[] hobbies;
    
    private AddressBean address;
    
    /** Creates a new instance of PersonBean */
    public PersonBean() {
	this(null);
    }
    
    public PersonBean(String name) {
    	this.setFirstName( name );
        hobbies = new HobbyBean[0];
    }
    
    /**
     * Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /**
     * Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /**
     * Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Getter for property hobbies.
     * @return Value of property hobbies.
     */
    public it.businesslogic.ireport.examples.beans.HobbyBean[] getHobbies() {
        return this.hobbies;
    }
    
    /**
     * Setter for property hobbies.
     * @param hobbies New value of property hobbies.
     */
    public void setHobbies(it.businesslogic.ireport.examples.beans.HobbyBean[] hobbies) {
        this.hobbies = hobbies;
    }
    
    /**
     * Getter for property address.
     * @return Value of property address.
     */
    public it.businesslogic.ireport.examples.beans.AddressBean getAddress() {
        return address;
    }
    
    /**
     * Setter for property address.
     * @param address New value of property address.
     */
    public void setAddress(it.businesslogic.ireport.examples.beans.AddressBean address) {
        this.address = address;
    }
    
}
