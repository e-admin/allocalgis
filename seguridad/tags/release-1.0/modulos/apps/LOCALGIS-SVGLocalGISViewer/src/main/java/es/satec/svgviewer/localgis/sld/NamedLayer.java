package es.satec.svgviewer.localgis.sld;

/**
 * A NamedLayer uses the "name" attribute to identify a layer known to the WMS and can contain zero
 * or more styles, either NamedStyles or UserStyles. In the absence of any styles the default style
 * for the layer is used.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class NamedLayer extends AbstractLayer {
    /**
     * constructor initializing the class with the <NamedLayer>
     */
    public NamedLayer( String name, AbstractStyle[] styles ) {
        super( name, styles );
    }

}