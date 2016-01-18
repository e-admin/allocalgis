/**
 * Operator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.michaelm.jump.query;


/**
 * IOperator
 * @author Michaël MICHAUD
 * @version 0.1.0 (4 Dec 2004)
 */
 
/**
 * Interface IOperator containing the definition of common operators
 */
public class Operator {
    private String key;
    public char type;  // B=boolean, N=numeric, S=string, E=enumeration, G=geometric
    public double arg = -1d; // arguments for the within distance function
    
    // OPERATOR FOR NUMERIC VALUES AND DATES
    public final static Operator BEQ = new Operator("beq", 'B');
    public final static Operator BNE = new Operator("bne", 'B');
    
    //OPERATOR FOR DATE VALUES
    public final static Operator DEQ = new Operator("deq", 'D'); // Date Equal
    public final static Operator DNE = new Operator("dne", 'D'); // Date Non equal
    public final static Operator DBF = new Operator("dbf",  'D'); // Date before
    public final static Operator DAF = new Operator("daf",  'D'); // Date after
    public final static Operator DBE = new Operator("dbe",  'D'); // Date before & equal
    public final static Operator DAE = new Operator("dae",  'D'); // Date after & equal
    
    // OPERATOR FOR NUMERIC VALUES AND DATES
    public final static Operator EQ = new Operator("eq", 'N');
    public final static Operator NE = new Operator("ne", 'N');
    public final static Operator LT = new Operator("lt", 'N');
    public final static Operator GT = new Operator("gt", 'N');
    public final static Operator LE = new Operator("le", 'N');
    public final static Operator GE = new Operator("ge", 'N');

    // OPERATOR FOR STRING VALUES
    public final static Operator EQUA= new Operator("equa", 'S');
    public final static Operator DIFF = new Operator("diff", 'S');
    public final static Operator STAR = new Operator("star", 'S');
    public final static Operator ENDS = new Operator("ends", 'S');
    public final static Operator MATC = new Operator("matc", 'S');
    public final static Operator FIND = new Operator("find", 'S');
    public final static Operator BEFO = new Operator("befo", 'S');
    public final static Operator AFTE = new Operator("afte", 'S');
        
    // OPERATOR FOR GEOMETRIC ATTRIBUTE
    public final static Operator INTER = new Operator("inter", 'G');
    public final static Operator CONTA = new Operator("conta", 'G');
    public final static Operator WITHI = new Operator("withi", 'G');
    public final static Operator WDIST = new Operator("wdist", 'G', 1000.0);
    public final static Operator TOUCH = new Operator("touch", 'G');
    public final static Operator CROSS = new Operator("cross", 'G');
    public final static Operator OVERL = new Operator("overl", 'G');
    public final static Operator DISJO = new Operator("disjo", 'G');
    
    public static Operator[] BOOLEAN_OP = new Operator[] {BEQ, BNE};
    
    public static Operator[] DATE_OP = new Operator[] {DEQ, DNE, DBF, DAF, DBE, DAE};
    
    public static Operator[] NUMERIC_OP = new Operator[] {
        EQ, NE, LT, GT, LE, GE
    };
        
    public static Operator[] STRING_OP = new Operator[] {
        EQUA, DIFF, STAR, ENDS, MATC, FIND, BEFO, AFTE
    };
        
    public static Operator[] GEOMETRIC_OP = new Operator[] {
        INTER, CONTA, WITHI, WDIST, TOUCH, CROSS, OVERL, DISJO
    };   
    
    public Operator(String key, char type) {
        this.key = key;
        this.type = type;
    }
    
    public Operator(String key, char type, double arg) {
        this.key = key;
        this.type = type;
        this.arg = arg;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer(I18NPlug.get(QueryPlugIn.pluginname, "operator."+key));
        if(arg<0.0) {return sb.toString();}
        else {return sb.toString() + " ("+arg+")";}
    }

}
