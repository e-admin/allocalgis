package es.satec.svgviewer.localgis.sld;

import java.util.Vector;

/**
 * A user-defined allows map styling to be defined externally from a system and to be passed around
 * in an interoperable format.
 * <p>
 * </p>
 * A UserStyle is at the same semantic level as a NamedStyle used in the context of a WMS. In a
 * sense, a named style can be thought of as a reference to a hidden UserStyle that is stored inside
 * of a map server.
 * 
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author last edited by: $Author: satec $
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class UserStyle extends AbstractStyle {
    private Vector featureTypeStyles = null;

    private String abstract_ = null;

    private String title = null;

    private boolean default_ = false;

    /**
     * constructor initializing the class with the <UserStyle>
     */
    UserStyle( String name, String title, String abstract_, boolean default_, FeatureTypeStyle[] featureTypeStyles ) {
        super( name );

        this.featureTypeStyles = new Vector();

        setTitle( title );
        setAbstract( abstract_ );
        setDefault( default_ );
        setFeatureTypeStyles( featureTypeStyles );
    }

    /**
     * The Title is a human-readable short description for the style that might be displayed in a
     * GUI pick list.
     * 
     * @return the title of the User-AbstractStyle
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the &lt;Title&gt;
     * 
     * @param title
     *            the title of the User-AbstractStyle
     * 
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * the Abstract is a more exact description that may be a few paragraphs long.
     * 
     * @return the abstract of the User-AbstractStyle
     */
    public String getAbstract() {
        return abstract_;
    }

    /**
     * sets the &lt;Abstract&gt;
     * 
     * @param abstract_
     *            the abstract of the User-AbstractStyle
     */
    public void setAbstract( String abstract_ ) {
        this.abstract_ = abstract_;
    }

    /**
     * The IsDefault element identifies whether a style is the default style of a layer, for use in
     * SLD library mode when rendering or for storing inside of a map server. The default value is
     * <tt>false</tt>.
     * 
     * @return true if the style ist the default style
     */
    public boolean isDefault() {
        return default_;
    }

    /**
     * sets the &lt;Default&gt;
     * 
     * @param default_
     */
    public void setDefault( boolean default_ ) {
        this.default_ = default_;
    }

    /**
     * A UserStyle can contain one or more FeatureTypeStyles which allow the rendering of features
     * of specific types.
     * <p>
     * </p>
     * The FeatureTypeStyle defines the styling that is to be applied to a single feature type of a
     * layer.
     * <p>
     * </p>
     * The FeatureTypeStyle element identifies that explicit separation in SLD between the handling
     * of layers and the handling of features of specific feature types. The layer concept is unique
     * to WMS and SLD, but features are used more generally, such as in WFS and GML, so this
     * explicit separation is important.
     * 
     * @return the FeatureTypeStyles of a User-AbstractStyle
     * 
     */
    public FeatureTypeStyle[] getFeatureTypeStyles() {
    	FeatureTypeStyle[] fts = new FeatureTypeStyle[featureTypeStyles.size()];
    	featureTypeStyles.copyInto(fts);
        return fts;
    }

    /**
     * sets the &lt;FeatureTypeStyle&gt;
     * 
     * @param featureTypeStyles
     *            the FeatureTypeStyles of a User-AbstractStyle
     */
    public void setFeatureTypeStyles( FeatureTypeStyle[] featureTypeStyles ) {
        this.featureTypeStyles.removeAllElements();

        if ( featureTypeStyles != null ) {
            for ( int i = 0; i < featureTypeStyles.length; i++ ) {
                addFeatureTypeStyle( featureTypeStyles[i] );
            }
        }
    }

    /**
     * Adds a &lt;FeatureTypeStyle&gt;
     * 
     * @param featureTypeStyle
     *            a FeatureTypeStyle to add
     */
    public void addFeatureTypeStyle( FeatureTypeStyle featureTypeStyle ) {
        featureTypeStyles.addElement( featureTypeStyle );
    }

    /**
     * Removes a &lt;FeatureTypeStyle&gt;
     */
    public void removeFeatureTypeStyle( FeatureTypeStyle featureTypeStyle ) {
        if ( featureTypeStyles.indexOf( featureTypeStyle ) != -1 ) {
            featureTypeStyles.removeElementAt( featureTypeStyles.indexOf( featureTypeStyle ) );
        }
    }

	public void dispose() {
		FeatureTypeStyle[] featureTypeStyles=getFeatureTypeStyles();
		if (featureTypeStyles!=null){
			for (int i=0;i<featureTypeStyles.length;i++){
				FeatureTypeStyle featureTypeStyle=(FeatureTypeStyle)featureTypeStyles[i];
				featureTypeStyle.dispose();
			}
		}
	}
}