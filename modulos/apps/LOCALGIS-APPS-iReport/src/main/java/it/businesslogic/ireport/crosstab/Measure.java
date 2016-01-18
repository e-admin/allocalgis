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
 * Measure.java
 * 
 * Created on 12 febbraio 2003, 21.45
 *
 */

package it.businesslogic.ireport.crosstab;

public class Measure
{
	private String name;
	private String classType;
	private String calculation;
	private String expression;
	private String incrementerFactoryClass = "";
        private String percentageOf ="None";
        private String percentageCalculatorClass = "";
        
        
        public Measure(String name)
        {
            this(name,"java.lang.String");
        }
        
        public Measure(String name, String classType)
	{
		this.name = name;
		
		this.classType= classType;
		calculation= "";
		expression= "";
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
        
        public Measure cloneMe()
        {
        	Measure jrv = new Measure( name, classType);
        	
		jrv.setCalculation( calculation );
		jrv.setExpression( expression );
        	jrv.setIncrementerFactoryClass( getIncrementerFactoryClass());
                jrv.setPercentageOf( getPercentageOf());
        	jrv.setPercentageCalculatorClass( getPercentageCalculatorClass());
        	
        	return jrv;
        }

    public String getPercentageOf() {
        return percentageOf;
    }

    public void setPercentageOf(String percentageOf) {
        this.percentageOf = percentageOf;
    }

    public String getPercentageCalculatorClass() {
        return percentageCalculatorClass;
    }

    public void setPercentageCalculatorClass(String percentageCalculatorClass) {
        this.percentageCalculatorClass = percentageCalculatorClass;
    }

}

