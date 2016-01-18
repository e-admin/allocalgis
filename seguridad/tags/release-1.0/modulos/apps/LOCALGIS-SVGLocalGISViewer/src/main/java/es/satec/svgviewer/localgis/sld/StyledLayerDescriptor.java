package es.satec.svgviewer.localgis.sld;

import java.util.Vector;

/**
 * StyledLayerDescriptor: This is a sequence of styled layers, represented at the first level by
 * Layer and UserLayer elements. A "version" attribute has been added to allow the formatting of
 * static-file
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @author last edited by: $Author: satec $
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */
public class StyledLayerDescriptor {

	private Vector layers = null;

	private String version = null;

	private String abstract_ = null;

	private String name = null;

	private String title = null;

	/**
	 * @param name
	 * @param title
	 * @param version
	 * @param abstract_
	 * @param layers
	 */
	StyledLayerDescriptor( String name, String title, String version, String abstract_, AbstractLayer[] layers ) {
		this.layers = new Vector( layers.length );
		setLayers( layers );
		setVersion( version );
		setAbstract( abstract_ );
		setName( name );
		setTitle( title );
	}

	/**
	 * constructor initializing the class with the <StyledLayerDescriptor>
	 */
	public StyledLayerDescriptor( AbstractLayer[] layers, String version ) {
		this.layers = new Vector( layers.length );
		setLayers( layers );
		setVersion( version );
	}

	/**
	 * @return the Layers as Array
	 */
	public AbstractLayer[] getLayers() {
		AbstractLayer[] al = new AbstractLayer[layers.size()];
		layers.copyInto(al);
		return al;
	}

	/**
	 * Sets Layers
	 * 
	 * @param layers
	 *            the Layers as Array
	 */
	public void setLayers( AbstractLayer[] layers ) {
		this.layers.removeAllElements();

		if ( layers != null ) {
			for ( int i = 0; i < layers.length; i++ ) {
				this.layers.addElement( layers[i] );
			}
		}
	}

	/**
	 * adds the <Layer>
	 * 
	 * @param layer
	 *            a Layer to add
	 */
	public void addLayer( AbstractLayer layer ) {
		layers.addElement( layer );
	}

	/**
	 * removes the <Layer>
	 * 
	 * @param layer
	 *            a Layer to remove
	 */
	public void removeLayer( AbstractLayer layer ) {
		if ( layers.indexOf( layer ) != -1 ) {
			layers.removeElementAt( layers.indexOf( layer ) );
		}
	}

	/**
	 * A NamedLayer uses the "name" attribute to identify a layer known to the WMS and can contain
	 * zero or more styles, either NamedStyles or UserStyles. In the absence of any styles the
	 * default style for the layer is used.
	 * 
	 * @return the NamedLayers as Array
	 */
	public NamedLayer[] getNamedLayers() {
		Vector list = new Vector( layers.size() );
		for ( int i = 0; i < layers.size(); i++ ) {
			if ( layers.elementAt( i ) instanceof NamedLayer ) {
				list.addElement( (NamedLayer) layers.elementAt( i ) );
			}
		}
		NamedLayer[] nl = new NamedLayer[list.size()];
		list.copyInto(nl);
		return nl;
	}

	/**
	 * The version attribute gives the SLD version of an SLD document, to facilitate backward
	 * compatibility with static documents stored in various different versions of the SLD spec. The
	 * string has the format x.y.z, the same as in other OpenGIS Web Server specs. For example, an
	 * SLD document stored according to this spec would have the version string 0.7.2.
	 * 
	 * @return the version of the SLD as String
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * sets the <Version>
	 * 
	 * @param version
	 *            the version of the SLD
	 * 
	 */
	public void setVersion( String version ) {
		this.version = version;
	}

	/**
	 * @return Returns the abstract_.
	 */
	public String getAbstract() {
		return abstract_;
	}

	/**
	 * @param abstract_
	 *            The abstract_ to set.
	 */
	public void setAbstract( String abstract_ ) {
		this.abstract_ = abstract_;
	}

	/**
	 * @return Returns the name.
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 * 
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * @return Returns the title.
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 * 
	 */
	public void setTitle( String title ) {
		this.title = title;
	}

	public void dispose(){		
		AbstractLayer[] abstractLayers=getLayers();
		if (abstractLayers!=null){
			for (int i=0;i<getLayers().length;i++){
				AbstractLayer aLayer=abstractLayers[i];		
				aLayer.dispose();
			}
		}
	}
}