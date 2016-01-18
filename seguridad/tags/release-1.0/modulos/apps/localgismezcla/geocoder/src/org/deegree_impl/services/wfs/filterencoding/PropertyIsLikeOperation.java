/*----------------    FILE HEADER  ------------------------------------------

This file is part of deegree.
Copyright (C) 2001 by:
EXSE, Department of Geography, University of Bonn
http://www.giub.uni-bonn.de/exse/
lat/lon Fitzke/Fretter/Poth GbR
http://www.lat-lon.de

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact:

Andreas Poth  
lat/lon Fitzke/Fretter/Poth GbR
Meckenheimer Allee 176
53115 Bonn
Germany
E-Mail: poth@lat-lon.de

Jens Fitzke
Department of Geography
University of Bonn
Meckenheimer Allee 166
53115 Bonn
Germany
E-Mail: jens.fitzke@uni-bonn.de

                 
 ---------------------------------------------------------------------------*/
package org.deegree_impl.services.wfs.filterencoding;

import org.w3c.dom.*;
import org.deegree.xml.*;
import org.deegree.model.feature.Feature;
import org.deegree.services.wfs.filterencoding.*;

/**
 * Encapsulates the information of a <PropertyIsLike>-element (as defined in Filter DTD).
 * @author Markus Schneider
 * @version 10.08.2002
 */
public class PropertyIsLikeOperation extends ComparisonOperation {

    private PropertyName propertyName;
    private Literal literal;

    // attributes of <PropertyIsLike>
    private char wildCard;
    private char singleChar;
    private char escapeChar;
    
    public PropertyIsLikeOperation (PropertyName propertyName, Literal literal,
                                    char wildCard, char singleChar,
                                    char escapeChar) {
        super( OperationDefines.PROPERTYISLIKE );
        this.propertyName = propertyName;
        this.literal = literal;
        this.wildCard = wildCard;
        this.singleChar = singleChar;
        this.escapeChar = escapeChar;
    }

    public char getWildCard ()   { return wildCard; }
    public char getSingleChar () { return singleChar; }
    public char getEscapeChar () { return escapeChar; }
    public void setWildCard (char wildCard) { this.wildCard = wildCard; }
    public void setSingleChar (char singleChar) { this.singleChar = singleChar; }
    public void setEscapeChar (char escapeChar) { this.escapeChar = escapeChar; }

    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method
     * recursively calls other buildFromDOM () - methods to validate the structure
     * of the DOM-fragment.
     * @throws FilterConstructionException if the structure of the DOM-fragment is invalid
     */    
    public static Operation buildFromDOM (Element element)
        								throws FilterConstructionException {

        // check if root element's name equals 'PropertyIsLike'
        if (!element.getLocalName().equals ("PropertyIsLike") )
            throw new FilterConstructionException ("Name of element does not equal 'PropertyIsLike'!");

        ElementList children = XMLTools.getChildElements (element);
        if (children.getLength () != 2) throw new FilterConstructionException ("'PropertyIsLike' requires exactly 2 elements!");

        PropertyName propertyName = (PropertyName) PropertyName.buildFromDOM (children.item (0));
        Literal literal = (Literal) Literal.buildFromDOM (children.item (1));
         
        // determine the needed attributes
        String wildCard = element.getAttribute ("wildCard");
        if (wildCard == null) throw new FilterConstructionException ("wildCard-Attribute is unspecified!");
        if (wildCard.length () != 1) throw new FilterConstructionException ("wildCard-Attribute must be exactly one character!");
        String singleChar = element.getAttribute ("singleChar");
        if (singleChar == null) throw new FilterConstructionException ("singleChar-Attribute is unspecified!");
        if (singleChar.length () != 1) throw new FilterConstructionException ("singleChar-Attribute must be exactly one character!");
        String escapeChar = element.getAttribute ("escape");
        if (escapeChar == null ) escapeChar = element.getAttribute ("escapeChar"); 
        if (escapeChar == null) throw new FilterConstructionException ("escape-Attribute is unspecified!");
        if (escapeChar.length () != 1) throw new FilterConstructionException ("escape-Attribute must be exactly one character!");
        
        return new PropertyIsLikeOperation (propertyName, literal, wildCard.charAt (0),
                                            singleChar.charAt (0), escapeChar.charAt (0));
    }
    
   /**
    * returns the name of the property that shall be compared to the 
    * literal
    */ 
    public PropertyName getPropertyName()
    {
    	return propertyName;
    }
    
   /**
    * returns the literal the property shall be compared to
    */ 
    public Literal getLiteral()
    {
    	return literal;
    }
        
    
    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML () {
        StringBuffer sb = new StringBuffer (500);
        sb.append ("<ogc:").append (getOperatorName ()).append (" wildCard=\"").
           append (wildCard).append ("\" singleChar=\"").append (singleChar).
           append ("\" escape=\"").append (escapeChar).append ("\">").
           append (propertyName.toXML ()).append (literal.toXML ());
        sb.append ("</ogc:").append (getOperatorName ()).append (">");
        return sb;
    }

    /**
     * Calculates the <tt>PropertyIsLike</tt>'s logical value based on the
     * certain property values of the given <tt>Feature</tt>.
     * <p>
     * @param feature that determines the property values
     * @return true, if the <tt>Literal</tt> matches the <tt>PropertyName</tt>'s value
     * @throws FilterEvaluationException if the evaluation could not be
     *         performed (for example a specified Property did not exist)
     */
    public boolean evaluate (Feature feature) throws FilterEvaluationException {

        Object value1 = null;
        Object value2 = null;
        try {
            value1 = propertyName.evaluate (feature);        
            value2 = literal.getValue ();
        } catch(Exception e) {
            System.out.println(e);	
        }
        return matches (value2.toString (), value1.toString ());
    }

    /**
     * Checks if a given <tt>String<tt> matches a pattern that is a sequence
     * of:
     * <ul>
     *   <li>standard characters</li>
     *   <li>wildcard characters (like * in most shells)</li>
     *   <li>singlechar characters (like ? in most shells)</li>
     * </ul>
     * @param pattern the pattern to compare to
     * @param buffer the <tt>String</tt> to test
     * @return true, if the <tt>String</tt> matches the pattern
     */ 
    public boolean matches (String pattern, String buffer) {
        // match was successful if both the pattern and the buffer are empty
        if (pattern.length () == 0 && buffer.length () == 0) return true;        

        // build the prefix that has to match the beginning of the buffer
        // prefix ends at the first (unescaped!) wildcard / singlechar character
        StringBuffer sb = new StringBuffer ();
        boolean escapeMode = false;
        int length = pattern.length ();
        char specialChar = '\0';
        
        for (int i = 0; i < length; i++) {
            char c = pattern.charAt (i);

            if (escapeMode) {
                // just append every character (except the escape character)
                if (c != escapeChar) sb.append(c);
                escapeMode = false;
            }
            else {
                // escapeChar means: switch to escapeMode
                if (c == escapeChar) escapeMode = true;
                // wildCard / singleChar means: prefix ends here
                else if (c == wildCard || c == singleChar) {
                    specialChar = c;
                    break;
                }
                else sb.append(c);
            }
        }
        String prefix = sb.toString ();
        int skip = prefix.length ();
        
        // the buffer must begin with the prefix or else there is no match
        if (!buffer.startsWith (prefix)) return false;
        
        if (specialChar == wildCard) {
            // the prefix is terminated by a wildcard-character        
            pattern = pattern.substring (skip + 1, pattern.length ());
            // try to find a match for the rest of the pattern
            for (int i = skip; i <= buffer.length (); i++) {
                String rest = buffer.substring (i, buffer.length ());
                if (matches (pattern, rest)) return true;
            }
        } else if (specialChar == singleChar) {
            // the prefix is terminated by a singlechar-character
            pattern = pattern.substring (skip + 1, pattern.length ());
            if (skip + 1 > buffer.length ()) return false;
            String rest = buffer.substring (skip + 1, buffer.length ());
            if (matches (pattern, rest)) return true;
        } else if (specialChar == '\0') {
            // the prefix is terminated by the end of the pattern
            if (buffer.length () == prefix.length ()) return true;
        }
        return false;
    }    
}
