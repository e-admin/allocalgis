package es.satec.svgviewer.localgis.sld;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: satec $
 * 
 * @version. $Revision: 1.1 $, $Date: 2011/09/19 13:48:12 $
 */
public abstract class AbstractStyle {

    protected String name = null;

    /**
     * Creates a new AbstractStyle object.
     * 
     * @param name
     */
    AbstractStyle( String name ) {
        this.name = name;
    }

    /**
     * The given Name is equivalent to the name of a WMS named style and is used to reference the
     * style externally when an SLD is used in library mode and identifies the named style to
     * redefine when an SLD is inserted into a WMS.
     * 
     * @return the name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name attribute's value of the AbstractStyle.
     * 
     * @param name
     *            the name of the style
     *            <p>
     * 
     */
    public void setName( String name ) {
        this.name = name;
    }

}