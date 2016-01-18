/**
 * SVGMetadataElem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
