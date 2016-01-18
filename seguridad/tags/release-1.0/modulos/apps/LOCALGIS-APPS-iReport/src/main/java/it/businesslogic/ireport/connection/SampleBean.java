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
 * SampleBean.java
 * 
 * Created on 23 giugno 2003, 0.38
 *
 */

package it.businesslogic.ireport.connection;

/**
 *
 * @author  Administrator
 */
public class SampleBean {
    
    private String firstName;
    
    private String lastName;
    
    private String email;
    
    /** Creates a new instance of SampleBean */
    public SampleBean() {
    }
    
    /** Getter for property email.
     * @return Value of property email.
     *
     */
    public java.lang.String getEmail() {
        return email;
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     *
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     *
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     *
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     *
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     *
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
}
