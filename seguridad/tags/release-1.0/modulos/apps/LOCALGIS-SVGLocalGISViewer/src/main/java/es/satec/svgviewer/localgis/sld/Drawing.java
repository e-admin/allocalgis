package es.satec.svgviewer.localgis.sld;

import java.util.Hashtable;

/**
 * This is the top level interface of <tt>Fill</tt> and <tt>Stroke</tt> defining the methods
 * <tt>getGraphicFill()</tt> and <tt>getCssParameters()</tt> that are common to both.
 * <p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public class Drawing {

    protected Hashtable cssParams = null;

    /**
     * Constructs a new instance of <tt>Drawing</tt>.
     * 
     * @param cssParams
     */
    Drawing(Hashtable cssParams) {
        this.cssParams = cssParams;
    }

    /**
     * A simple SVG/CSS2 styling parameters are given with the CssParameter element. <br>
     * This method is for technical use. The user should access the specialized methods of the
     * derived classes.
     * 
     * @return the CssParameters
     */
    public Hashtable getCssParameters() {
        return cssParams;
    }

    /**
     * A simple SVG/CSS2 styling parameters are given with the CssParameter element. <br>
     * This method sets CssParameters.
     * 
     * @param cssParameters
     *            the CssParameters
     */
    void setCssParameters(Hashtable cssParameters) {
        this.cssParams = cssParameters;
    }

    /**
     * Simple SVG/CSS2 styling parameters are given with the CssParameter element. This method adds
     * a CssParameter to a given set of CssParameters.
     * <p>
     * 
     * @param key
     *            the key of the object to insert
     * @param value
     *            the value of the object to insert
     */
    void addCssParameter( String key, Object value ) {
        cssParams.put( key, value );
    }

    /**
     * Simple SVG/CSS2 styling parameters are given with the CssParameter element.
     * <p>
     * This method adds a CssParameter to a given set of CssParameters.
     * 
     * @param key
     *            the key of the object to remove
     */
    void removeCssParameter( Object key ) {
        cssParams.remove( key );
    }

}