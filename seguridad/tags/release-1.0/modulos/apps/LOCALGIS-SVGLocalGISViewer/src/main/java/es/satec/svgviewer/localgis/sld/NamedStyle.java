package es.satec.svgviewer.localgis.sld;

/**
 * A NamedStyle uses the "name" attribute to identify a style known to the WMS
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class NamedStyle extends AbstractStyle {
    /**
     * Creates a new NamedStyle object.
     * 
     * @param name
     */
    public NamedStyle( String name ) {
        super( name );
    }

}