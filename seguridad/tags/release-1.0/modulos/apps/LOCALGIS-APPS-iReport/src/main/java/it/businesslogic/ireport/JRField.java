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
 * JRField.java
 * 
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.util.Misc;
import java.util.Iterator;

public class JRField
{
	private String name;
	private String description="";
	private String classType;
        
        private java.util.List properties = new java.util.ArrayList();
        
	
	public  JRField(String name, String classType)
	{
		this.name = name;
		this.classType = classType;
	}
	
	public String toString()
	{
            return name;
	}
        
        /** Getter for property classType.
         * @return Value of property classType.
         *
         */
        public java.lang.String getClassType() {
            return classType;
        }
        
        /** Setter for property classType.
         * @param classType New value of property classType.
         *
         */
        public void setClassType(java.lang.String classType) {
            this.classType = Misc.getJRFieldType( classType );
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
        
        public JRField cloneMe()
        {
        	JRField jrf = new JRField(name, classType);
        	
        	jrf.setDescription( description );
        	
                Iterator iter = getProperties().iterator();
                while (iter.hasNext())
                {
                    JRProperty p = (JRProperty)iter.next();
                    jrf.getProperties().add( p.cloneMe() );
                }
            
        	return jrf;	
        }

    public java.util.List getProperties() {
        return properties;
    }

    public void setProperties(java.util.List properties) {
        this.properties = properties;
    }

        
}
