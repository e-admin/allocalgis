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
 * JRVariable.java
 * 
 * Created on 12 febbraio 2003, 21.45
 *
 */

package it.businesslogic.ireport;

public class JRVariable
{
	private String name;
	private String classType;
	private String resetType;
	private String resetGroup;
	private String calculation;
	private String expression;
	private String initialValueExpression; 
	private boolean builtin;
	private String incrementerFactoryClass = "";
        private String incrementType = "None";
        private String incrementGroup = "";
        
	public JRVariable(String name, boolean builtin)
	{
                this(name,"java.lang.String", builtin);
	}
        
        public JRVariable(String name, String classType, boolean builtin)
	{
		this.name = name;
		this.builtin = builtin;
		
		this.classType= classType;
		resetType= "";
		resetGroup= "";
		calculation= "";
		expression= "";
		initialValueExpression= ""; 
	}
        
        /** Getter for property builtin.
         * @return Value of property builtin.
         *
         */
        public boolean isBuiltin() {
            return builtin;
        }
        
        /** Setter for property builtin.
         * @param builtin New value of property builtin.
         *
         */
        public void setBuiltin(boolean builtin) {
            this.builtin = builtin;
        }
        
        /** Getter for property calculation.
         * @return Value of property calculation.
         *
         */
        public java.lang.String getCalculation() {
            return calculation;
        }
        
        /** Setter for property calculation.
         * @param calculation New value of property calculation.
         *
         */
        public void setCalculation(java.lang.String calculation) {
            this.calculation = calculation;
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
            this.classType = classType;
        }
        
        /** Getter for property expression.
         * @return Value of property expression.
         *
         */
        public java.lang.String getExpression() {
            return expression;
        }
        
        /** Setter for property expression.
         * @param expression New value of property expression.
         *
         */
        public void setExpression(java.lang.String expression) {
            this.expression = expression;
        }
        
        /** Getter for property initialValueExpression.
         * @return Value of property initialValueExpression.
         *
         */
        public java.lang.String getInitialValueExpression() {
            return initialValueExpression;
        }
        
        /** Setter for property initialValueExpression.
         * @param initialValueExpression New value of property initialValueExpression.
         *
         */
        public void setInitialValueExpression(java.lang.String initialValueExpression) {
            this.initialValueExpression = initialValueExpression;
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
        
        /** Getter for property resetGroup.
         * @return Value of property resetGroup.
         *
         */
        public java.lang.String getResetGroup() {
            return resetGroup;
        }
        
        /** Setter for property resetGroup.
         * @param resetGroup New value of property resetGroup.
         *
         */
        public void setResetGroup(java.lang.String resetGroup) {
            this.resetGroup = resetGroup;
        }
        
        /** Getter for property resetType.
         * @return Value of property resetType.
         *
         */
        public java.lang.String getResetType() {
            return resetType;
        }
        
        /** Setter for property resetType.
         * @param resetType New value of property resetType.
         *
         */
        public void setResetType(java.lang.String resetType) {
            this.resetType = resetType;
        }
        
        public String toString()
	{
		return name;
	}
        
        /** Getter for property incrementerFactoryClass.
         * @return Value of property incrementerFactoryClass.
         *
         */
        public java.lang.String getIncrementerFactoryClass() {
            return incrementerFactoryClass;
        }
        
        /** Setter for property incrementerFactoryClass.
         * @param incrementerFactoryClass New value of property incrementerFactoryClass.
         *
         */
        public void setIncrementerFactoryClass(java.lang.String incrementerFactoryClass) {
            this.incrementerFactoryClass = incrementerFactoryClass;
        }
        
        public JRVariable cloneMe()
        {
        	JRVariable jrv = new JRVariable( name, classType, builtin);
        	
        	jrv.setResetType( resetType );
		jrv.setResetGroup( resetGroup );
		jrv.setCalculation( calculation );
		jrv.setExpression( expression );
		jrv.setInitialValueExpression( initialValueExpression ); 
        	jrv.setIncrementerFactoryClass( incrementerFactoryClass );
        	jrv.setIncrementGroup( getIncrementGroup());
        	
        	return jrv;
        }

    public String getIncrementType() {
        return incrementType;
    }

    public void setIncrementType(String incrementType) {
        this.incrementType = incrementType;
    }

    public String getIncrementGroup() {
        return incrementGroup;
    }

    public void setIncrementGroup(String incrementGroup) {
        this.incrementGroup = incrementGroup;
    }
}

