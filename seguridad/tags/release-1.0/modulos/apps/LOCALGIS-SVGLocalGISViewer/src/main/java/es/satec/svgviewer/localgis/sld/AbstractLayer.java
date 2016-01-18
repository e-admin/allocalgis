package es.satec.svgviewer.localgis.sld;

import java.util.Vector;

public abstract class AbstractLayer {

    protected Vector styles = null;

    protected String name = null;

    /**
     * constructor initializing the class with the <NamedLayer>
     */
    AbstractLayer( String name, AbstractStyle[] styles ) {
        this.styles = new Vector();
        setName( name );
        setStyles( styles );
    }

    /**
     * The Name element identifies the well-known name of the layer being referenced, and is
     * required. All possible well-known names are usually identified in the capabilities document
     * for a server.
     * 
     * @return the name of the layer
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * sets the <Name>
     * 
     * @param name
     *            the name of the layer
     * 
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Returns the styles associated to the Layer. This may be UserStyles or NamedStyles
     * <p>
     * </p>
     * A UserStyle is at the same semantic level as a NamedStyle used in the context of a WMS. In a
     * sense, a named style can be thought of as a reference to a hidden UserStyle that is stored
     * inside of a map server.
     * 
     * @return the Styles of the Layer as ArrayList
     * 
     */
    public AbstractStyle[] getStyles() {
    	AbstractStyle[] as = new AbstractStyle[styles.size()];
    	styles.copyInto(as);
        return as;
    }

    /**
     * Adds styles to the Layer.
     * 
     * @param styles
     *            the styles for the layer as Array
     */
    public void setStyles( AbstractStyle[] styles ) {
        this.styles.removeAllElements();

        if ( styles != null ) {
        	for (int i=0; i<styles.length; i++) {
        		this.styles.addElement(styles[i]);
        	}
        }
    }

    /**
     * @see org.deegree.graphics.sld.AbstractLayer#getStyles()
     * @param style
     *            a style to add
     */
    public void addStyle( AbstractStyle style ) {
        styles.addElement( style );
    }

    /**
     * @see org.deegree.graphics.sld.AbstractLayer#getStyles()
     * @param style
     *            a style to remove
     */
    public void removeStyle( AbstractStyle style ) {
        styles.removeElementAt( styles.indexOf( style ) );
    }

    /**
     * returns a STring-Representation of the layer
     * 
     * @return the layer as String
     */
    public String toString() {
        String ret = getClass().getName() + "\n";
        ret = "name = " + name + "\n";
        ret += ( "styles = " + styles + "\n" );

        return ret;
    }

	public void dispose() {
		AbstractStyle[] abstractStyles=getStyles();
		if (abstractStyles!=null){
			for (int i=0;i<abstractStyles.length;i++){
				UserStyle uStyle=(UserStyle)abstractStyles[i];
				uStyle.dispose();		
			}
		}
	}

}