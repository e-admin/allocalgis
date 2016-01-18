package es.satec.svgviewer.localgis.sld;

import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyColor;

public class SLDTinyChange {

	private SVGNode changedNode;
	
	private int changedAttribute;
	
	private Object oldValue;

	/**
	 * @param changedNode
	 * @param changedAttribute
	 * @param oldValue
	 */
	public SLDTinyChange(SVGNode changedNode, int changedAttribute, Object oldValue) {
		super();
		this.changedNode = changedNode;
		this.changedAttribute = changedAttribute;
		this.oldValue = oldValue;
	}

	public int getChangedAttribute() {
		return changedAttribute;
	}

	public void setChangedAttribute(int changedAttribute) {
		this.changedAttribute = changedAttribute;
	}

	public SVGNode getChangedNode() {
		return changedNode;
	}

	public void setChangedNode(SVGNode changedNode) {
		this.changedNode = changedNode;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	
	public void restore() {
		switch (changedAttribute) {
		case SVG.ATT_FILL:
			changedNode.fill = (TinyColor) oldValue;
			break;
		case SVG.ATT_FILL_OPACITY:
			changedNode.fillOpacity = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_OPACITY:
			changedNode.opacity = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_STROKE:
			changedNode.stroke = (TinyColor) oldValue;
			break;
		case SVG.ATT_STROKE_DASHARRAY:
			changedNode.strokeDashArray = (int[]) oldValue;
			break;
		case SVG.ATT_STROKE_DASHOFFSET:
			changedNode.strokeDashOffset = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_STROKE_LINECAP:
			changedNode.strokeLineCap = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_STROKE_LINEJOIN:
			changedNode.strokeLineJoin = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_STROKE_OPACITY:
			changedNode.strokeOpacity = ((Integer) oldValue).intValue();
			break;
		case SVG.ATT_STROKE_WIDTH:
			changedNode.strokeWidth = ((Integer) oldValue).intValue();
			break;
		}
	}
}
