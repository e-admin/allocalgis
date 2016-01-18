/**
 * PropertyIsLikeOperation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
//$HeadURL: https://svn.wald.intevation.org/svn/deegree/base/trunk/src/org/deegree/model/filterencoding/PropertyIsLikeOperation.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2006 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
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
 lat/lon GmbH
 Aennchenstr. 19
 53115 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de


 ---------------------------------------------------------------------------*/
package org.deegree.model.filterencoding;

import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.feature.Feature;
import org.w3c.dom.Element;

/**
 * Encapsulates the information of a <PropertyIsLike>-element (as defined in Filter DTD).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: satec $
 * 
 * @version $Revision: 1.1 $, $Date: 2011/09/19 13:47:32 $
 */
public class PropertyIsLikeOperation extends ComparisonOperation {

    private PropertyName propertyName;

    private Literal literal;

    // attributes of <PropertyIsLike>
    private char wildCard;

    private char singleChar;

    private char escapeChar;

    private boolean matchCase;

    /**
     * 
     * @param propertyName
     * @param literal
     * @param wildCard
     * @param singleChar
     * @param escapeChar
     */
    public PropertyIsLikeOperation( PropertyName propertyName, Literal literal, char wildCard,
                                   char singleChar, char escapeChar ) {
        this( propertyName, literal, wildCard, singleChar, escapeChar, true );
    }

    /**
     * 
     * @param propertyName
     * @param literal
     * @param wildCard
     * @param singleChar
     * @param escapeChar
     * @param matchCase
     */
    public PropertyIsLikeOperation( PropertyName propertyName, Literal literal, char wildCard,
                                   char singleChar, char escapeChar, boolean matchCase ) {
        super( OperationDefines.PROPERTYISLIKE );
        this.propertyName = propertyName;
        this.literal = literal;
        this.wildCard = wildCard;
        this.singleChar = singleChar;
        this.escapeChar = escapeChar;
        this.matchCase = matchCase;
    }

    public char getWildCard() {
        return wildCard;
    }

    public char getSingleChar() {
        return singleChar;
    }

    public char getEscapeChar() {
        return escapeChar;
    }

    /**
     * returns matchCase flag
     */
    public boolean isMatchCase() {
        return matchCase;
    }

    /**
     * Given a DOM-fragment, a corresponding Operation-object is built. This method recursively
     * calls other buildFromDOM () - methods to validate the structure of the DOM-fragment.
     * 
     * @throws FilterConstructionException
     *             if the structure of the DOM-fragment is invalid
     */
    public static Operation buildFromDOM( Element element )
                            throws FilterConstructionException {

        // check if root element's name equals 'PropertyIsLike'
        if ( !element.getLocalName().equals( "PropertyIsLike" ) ) {
            throw new FilterConstructionException(
                                                   "Name of element does not equal 'PropertyIsLike'!" );
        }

        ElementList children = XMLTools.getChildElements( element );
        if ( children.getLength() != 2 ) {
            throw new FilterConstructionException( "'PropertyIsLike' requires exactly 2 elements!" );
        }

        PropertyName propertyName = (PropertyName) PropertyName.buildFromDOM( children.item( 0 ) );
        Literal literal = (Literal) Literal.buildFromDOM( children.item( 1 ) );

        // determine the needed attributes
        String wildCard = element.getAttribute( "wildCard" );
        if ( wildCard == null || wildCard.length() == 0 ) {
            throw new FilterConstructionException( "wildCard-Attribute is unspecified!" );
        }
        if ( wildCard.length() != 1 ) {
            throw new FilterConstructionException(
                                                   "wildCard-Attribute must be exactly one character!" );
        }

        // This shouldn't be necessary and can actually cause problems...
        // // always use '%' as wildcard because this is compliant to SQL databases
        // literal = new Literal( StringTools.replace( literal.getValue(), wildCard, "%", true ) );
        // wildCard = "%";
        String singleChar = element.getAttribute( "singleChar" );
        if ( singleChar == null || singleChar.length() == 0 ) {
            throw new FilterConstructionException( "singleChar-Attribute is unspecified!" );
        }
        if ( singleChar.length() != 1 ) {
            throw new FilterConstructionException(
                                                   "singleChar-Attribute must be exactly one character!" );
        }
        String escapeChar = element.getAttribute( "escape" );
        if ( escapeChar == null || escapeChar.length() == 0 ) {
            escapeChar = element.getAttribute( "escapeChar" );
        }
        if ( escapeChar == null || escapeChar.length() == 0 ) {
            throw new FilterConstructionException( "escape-Attribute is unspecified!" );
        }
        if ( escapeChar.length() != 1 ) {
            throw new FilterConstructionException(
                                                   "escape-Attribute must be exactly one character!" );
        }
        boolean matchCase = true;
        String tmp = element.getAttribute( "matchCase" );
        if ( tmp != null && tmp.length() > 0 ) {
            try {
                matchCase = (new Boolean ( tmp )).booleanValue();
            } catch ( Exception e ) {
                // undocumented
            }

        }

        return new PropertyIsLikeOperation( propertyName, literal, wildCard.charAt( 0 ),
                                            singleChar.charAt( 0 ), escapeChar.charAt( 0 ),
                                            matchCase );
    }

    /**
     * returns the name of the property that shall be compared to the literal
     * 
     */
    public PropertyName getPropertyName() {
        return propertyName;
    }

    /**
     * returns the literal the property shall be compared to
     * 
     */
    public Literal getLiteral() {
        return literal;
    }

    /** Produces an indented XML representation of this object. */
    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer( 500 );
        sb.append( "<ogc:" ).append( getOperatorName() ).append( " wildCard=\"" ).append( wildCard );
        sb.append( "\" singleChar=\"" ).append( singleChar ).append( "\" escape=\"" );
        sb.append( escapeChar ).append( "\" matchCase=\"" ).append( matchCase ).append( "\">" );
        sb.append( propertyName.toXML() ).append( literal.toXML() );
        sb.append( "</ogc:" ).append( getOperatorName() ).append( ">" );
        return sb;
    }

    /**
     * Calculates the <tt>PropertyIsLike</tt>'s logical value based on the certain property
     * values of the given <tt>Feature</tt>.
     * <p>
     * 
     * @param feature
     *            that determines the property values
     * @return true, if the <tt>Literal</tt> matches the <tt>PropertyName</tt>'s value
     * @throws FilterEvaluationException
     *             if the evaluation could not be performed (for example a specified Property did
     *             not exist)
     */
    public boolean evaluate( Feature feature )
                            throws FilterEvaluationException {

        Object value1 = null;
        Object value2 = null;
        try {
            value1 = propertyName.evaluate( feature );
            value2 = literal.getValue();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if ((value1 == null) || (value2 == null)) {
        	return false;
        }
        if ( isMatchCase() ) {
            return matches( value2.toString(), value1.toString() );
        }
        return matches( value2.toString().toUpperCase(), value1.toString().toUpperCase() );
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
    public boolean matches( String pattern, String buffer ) {
        // match was successful if both the pattern and the buffer are empty
        if ( pattern.length() == 0 && buffer.length() == 0 )
            return true;

        // build the prefix that has to match the beginning of the buffer
        // prefix ends at the first (unescaped!) wildcard / singlechar character
        StringBuffer sb = new StringBuffer();
        boolean escapeMode = false;
        int length = pattern.length();
        char specialChar = '\0';

        for ( int i = 0; i < length; i++ ) {
            char c = pattern.charAt( i );

            if ( escapeMode ) {
                // just append every character (except the escape character)
                if ( c != escapeChar )
                    sb.append( c );
                escapeMode = false;
            } else {
                // escapeChar means: switch to escapeMode
                if ( c == escapeChar )
                    escapeMode = true;
                // wildCard / singleChar means: prefix ends here
                else if ( c == wildCard || c == singleChar ) {
                    specialChar = c;
                    break;
                } else
                    sb.append( c );
            }
        }
        String prefix = sb.toString();
        int skip = prefix.length();

        // the buffer must begin with the prefix or else there is no match
        if ( !buffer.startsWith( prefix ) )
            return false;

        if ( specialChar == wildCard ) {
            // the prefix is terminated by a wildcard-character
            pattern = pattern.substring( skip + 1, pattern.length() );
            // try to find a match for the rest of the pattern
            for ( int i = skip; i <= buffer.length(); i++ ) {
                String rest = buffer.substring( i, buffer.length() );
                if ( matches( pattern, rest ) )
                    return true;
            }
        } else if ( specialChar == singleChar ) {
            // the prefix is terminated by a singlechar-character
            pattern = pattern.substring( skip + 1, pattern.length() );
            if ( skip + 1 > buffer.length() )
                return false;
            String rest = buffer.substring( skip + 1, buffer.length() );
            if ( matches( pattern, rest ) )
                return true;
        } else if ( specialChar == '\0' ) {
            // the prefix is terminated by the end of the pattern
            if ( buffer.length() == prefix.length() )
                return true;
        }
        return false;
    }
}

/***************************************************************************************************
 * <code>
 Changes to this class. What the people have been up to:
 
 $Log: PropertyIsLikeOperation.java,v $
 Revision 1.1  2011/09/19 13:47:32  satec
 MODELO EIEL

 Revision 1.3  2010/05/03 08:41:19  satec
 *** empty log message ***

 Revision 1.2  2009/05/29 10:54:34  roger
 Arreglos null pointer de nodos con atributos extendidos vacios, causados por nodos nuevos

 Revision 1.1  2009/03/31 15:54:49  roger
 Creación de módulo FIlter SLD que implementa los filtros OGC sobre Features SVG

 Revision 1.15  2007/01/11 16:27:25  mschneider
 Added new ComparisonOperation: PropertyIsInstanceOfOperation.
 Revision 1.14 2006/11/21 17:58:43 poth useless import
 removed

 Revision 1.13 2006/11/09 22:41:26 mschneider Removed % hack.
 
 Revision 1.12 2006/10/05 17:06:52 poth ensured that '%' will be used as wildcard / setter methods removed

 Revision 1.11 2006/06/30 13:48:18 poth bug fix - export of 'matchCase' attribute added / missing footer added
 </code>
 **************************************************************************************************/
