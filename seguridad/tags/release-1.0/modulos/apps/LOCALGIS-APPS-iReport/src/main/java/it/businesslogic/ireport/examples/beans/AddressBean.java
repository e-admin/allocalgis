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
 * AddressBean.java
 * 
 * Created on 8 luglio 2004, 2.19
 *
 */

package it.businesslogic.ireport.examples.beans;

/**
 *
 * @author  Administrator
 */
public class AddressBean {
    
    private String street;
    
    private String country;
    
    private String state;
    
    /** Creates a new instance of AddressBean */
    public AddressBean() {
    }
    
    /**
     * Getter for property country.
     * @return Value of property country.
     */
    public java.lang.String getCountry() {
        return country;
    }
    
    /**
     * Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }
    
    /**
     * Getter for property state.
     * @return Value of property state.
     */
    public java.lang.String getState() {
        return state;
    }
    
    /**
     * Setter for property state.
     * @param state New value of property state.
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }
    
    /**
     * Getter for property street.
     * @return Value of property street.
     */
    public java.lang.String getStreet() {
        return street;
    }
    
    /**
     * Setter for property street.
     * @param street New value of property street.
     */
    public void setStreet(java.lang.String street) {
        this.street = street;
    }
    
}
