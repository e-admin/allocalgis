/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.model;

import java.io.Serializable;
import java.util.List;

import org.deegree.graphics.sld.Rule;

/**
 * @author enxenio s.l.
 *
 */
public interface ScaleRange extends Serializable, Comparable {

	public Double getMinScale();
	
	public void setMinScale(Double minScale);
	
	public Double getMaxScale();
	
	public void setMaxScale(Double maxScale);
	
	public List getPointList();
	
	public void setPointList(List pointList);
	
	public List getLineList();
	
	public void setLineList(List lineList);

	public List getPolygonList();
	
	public void setPolygonList(List polygonList);

	public List getTextList();
	
	public void setTextList(List textList);

	public Rule getRule(int position, String symbolizerType);
	
	public void addRule(Rule rule);
	
	public void removeRule(int position, String symbolizerType);

	public List getRuleList(String symbolizerType);
	
	int compareTo(Object o);
}
