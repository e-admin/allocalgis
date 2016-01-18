package es.satec.localgismobile.ui;

import java.util.Hashtable;

import org.eclipse.swt.widgets.Shell;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGNode;

import es.satec.svgviewer.localgis.MetaInfo;

public abstract class LocalGISApplicationBaseWindow extends LocalGISWindow {

	protected SVGNode currentNode;
	protected MetaInfo metaInfo;
	protected GeopistaSchema metaInfoSchema;
	protected Hashtable params;

	public LocalGISApplicationBaseWindow(Shell parent, SVGNode currentNode, MetaInfo metaInfo, GeopistaSchema metaInfoSchema, Hashtable params) {
		super(parent);
		this.currentNode = currentNode;
		this.metaInfo = metaInfo;
		this.metaInfoSchema = metaInfoSchema;
		this.params = params;
	}

}
