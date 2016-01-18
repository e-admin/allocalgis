package com.tinyline.svg;

import com.tinyline.tiny2d.TinyString;

public class SVGMetadataElem extends SVGNode {

	public TinyString content;

	SVGMetadataElem() {}

	public SVGMetadataElem(SVGMetadataElem svgMetadataElem) {
		super(svgMetadataElem);
		if(svgMetadataElem.content != null) {
			content = new TinyString(svgMetadataElem.content.data);
		}
	}

	public SVGNode copyNode() {
		return new SVGMetadataElem(this);
	}

	public int createOutline() {
		return 0;
	}

	public void paint(SVGRaster svgraster) {
	}

	public void setContent(char[] ac, int i, int j) {
		if(ac != null) {
			content = new TinyString(ac, i, j);
		}
	}

	public void setContent(TinyString content) {
		this.content = content;
	}

	public void copyGeometryTo(SVGNode destNode) {
	}
}
