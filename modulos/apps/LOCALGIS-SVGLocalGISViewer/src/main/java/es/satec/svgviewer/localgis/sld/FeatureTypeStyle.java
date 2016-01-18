/**
 * FeatureTypeStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

import java.util.Vector;

/**
 * The FeatureTypeStyle defines the styling that is to be applied to a single feature type of a
 * layer). This element may also be externally re-used outside of the scope of WMSes and layers.
 * <p>
 * </p>
 * The FeatureTypeStyle element identifies that explicit separation in SLD between the handling of
 * layers and the handling of features of specific feature types. The layer concept is unique to WMS
 * and SLD, but features are used more generally, such as in WFS and GML, so this explicit
 * separation is important.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class FeatureTypeStyle {
    private Vector rules = null;

    private Vector semanticTypeIdentifier = null;

    private String abstract_ = null;

    private String featureTypeName = null;

    private String name = null;

    private String title = null;

    /**
     * default constructor
     */
    FeatureTypeStyle() {
        semanticTypeIdentifier = new Vector();
        rules = new Vector();
    }

    /**
     * constructor initializing the class with the <FeatureTypeStyle>
     */
    FeatureTypeStyle( String name, String title, String abstract_, String featureTypeName,
                      String[] semanticTypeIdentifier, Rule[] rules ) {
        this();
        setName( name );
        setTitle( title );
        setAbstract( abstract_ );
        setFeatureTypeName( featureTypeName );
        setSemanticTypeIdentifier( semanticTypeIdentifier );
        setRules( rules );
    }

    /**
     * The Name element does not have an explicit use at present, though it conceivably might be
     * used to reference a feature style in some feature-style library.
     * 
     * @return name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The Name element does not have an explicit use at present, though it conceivably might be
     * used to reference a feature style in some feature-style library. Sets the <Name> o
     * 
     * @param name
     *            the name
     * 
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * human-readable information about the style
     * 
     * @return the title of the FeatureTypeStyle
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the &lt;Title&gt;
     * 
     * @param title
     *            the title of the FeatureTypeStyle
     * 
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * human-readable information about the style
     * 
     * @return an abstract of the FeatureTypeStyle
     */
    public String getAbstract() {
        return abstract_;
    }

    /**
     * sets &lt;Abstract&gt;
     * 
     * @param abstract_
     *            an abstract of the FeatureTypeStyle
     */
    public void setAbstract( String abstract_ ) {
        this.abstract_ = abstract_;
    }

    /**
     * returns the name of the affected feature type
     * 
     * @return the name of the FeatureTypeStyle as String
     * 
     */
    public String getFeatureTypeName() {
        return featureTypeName;
    }

    /**
     * sets the name of the affected feature type
     * 
     * @param featureTypeName
     *            the name of the FeatureTypeStyle
     * 
     */
    public void setFeatureTypeName( String featureTypeName ) {
        this.featureTypeName = featureTypeName;
    }

    /**
     * The SemanticTypeIdentifier is experimental and is intended to be used to identify what the
     * feature style is suitable to be used for using community- controlled name(s). For example, a
     * single style may be suitable to use with many different feature types. The syntax of the
     * SemanticTypeIdentifier string is undefined, but the strings generic:line, generic:polygon,
     * generic:point, generic:text, generic:raster, and generic:any are reserved to indicate that a
     * FeatureTypeStyle may be used with any feature type with the corresponding default geometry
     * type (i.e., no feature properties are referenced in the feature-type style).
     * 
     * @return the SemanticTypeIdentifiers from the FeatureTypeStyle as String-Array
     * 
     */
    public String[] getSemanticTypeIdentifier() {
    	String[] sti = new String[semanticTypeIdentifier.size()];
    	semanticTypeIdentifier.copyInto(sti);
        return sti;
    }

    /**
     * Sets the SemanticTypeIdentifiers.
     * 
     * @param semanticTypeIdentifiers
     *            SemanticTypeIdentifiers for the FeatureTypeStyle
     */
    public void setSemanticTypeIdentifier( String[] semanticTypeIdentifiers ) {
        semanticTypeIdentifier.removeAllElements();

        if ( semanticTypeIdentifiers != null ) {
            for ( int i = 0; i < semanticTypeIdentifiers.length; i++ ) {
                semanticTypeIdentifier.addElement( semanticTypeIdentifiers[i] );
            }
        }
    }

    /**
     * adds the &lt;SemanticTypeIdentifier&gt;
     * 
     * @param semanticTypeIdentifier
     *            SemanticTypeIdentifier to add
     */
    public void addSemanticTypeIdentifier( String semanticTypeIdentifier ) {
        this.semanticTypeIdentifier.addElement( semanticTypeIdentifier );
    }

    /**
     * Removes an &lt;SemanticTypeIdentifier&gt;.
     * 
     * @param semanticTypeIdentifier
     *            SemanticTypeIdentifier to remove
     */
    public void removeSemanticTypeIdentifier( String semanticTypeIdentifier ) {
        this.semanticTypeIdentifier.removeElementAt( this.semanticTypeIdentifier.indexOf( semanticTypeIdentifier ) );
    }

    /**
     * Rules are used to group rendering instructions by feature-property conditions and map scales.
     * Rule definitions are placed immediately inside of feature-style definitions.
     * 
     * @return the rules of the FeatureTypeStyle as Array
     * 
     */
    public Rule[] getRules() {
    	Rule[] r = new Rule[rules.size()];
    	rules.copyInto(r);
    	return r;
    }

    /**
     * sets the &lt;Rules&gt;
     * 
     * @param rules
     *            the rules of the FeatureTypeStyle as Array
     */
    public void setRules( Rule[] rules ) {
        this.rules.removeAllElements();

        if ( rules != null ) {
            for ( int i = 0; i < rules.length; i++ ) {
                this.rules.addElement( rules[i] );
            }
        }
    }

    /**
     * adds the &lt;Rules&gt;
     * 
     * @param rule
     *            a rule
     */
    public void addRule( Rule rule ) {
        rules.addElement( rule );
    }

    /**
     * removes a rule
     * 
     * @param rule
     *            a rule
     */
    public void removeRule( Rule rule ) {
        rules.removeElementAt( rules.indexOf( rule ) );
    }

	public void dispose() {
		Rule[] rules=getRules();
		if (rules!=null){
			for (int i=0;i<rules.length;i++){
				Rule rule=rules[i];
				rule.dispose();
			}
		}
		
	}

}
