/**
 * FilterFacade.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model;

import java.util.HashMap;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLException;
import org.deegree.gml.GMLGeometry;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;

import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.exceptions.MalformedFilterException;
import com.geopista.style.filtereditor.model.impl.BBOXOp;
import com.geopista.style.filtereditor.model.impl.DistanceBufferOp;
import com.geopista.style.filtereditor.model.impl.Literal;
import com.geopista.style.filtereditor.model.impl.PropertyName;
import com.vividsolutions.jump.workbench.model.Layer;
/**
 * @author enxenio s.l.
 *
 */
public interface FilterFacade {
	
	public DefaultTreeModel createEmptyFilter() throws IncorrectIdentifierException;	
	
	public DefaultTreeModel createFilterTree(Filter filter) 
		throws IncorrectIdentifierException,MalformedFilterException;
		
	public void deleteNode(DefaultTreeModel filterTree,MutableTreeNode node);
	
	public void changeOperatorType(DefaultTreeModel filterTree, MutableTreeNode oldNode, int newOperatorID)
		throws IncorrectIdentifierException;
		
	public void addOperatorChild(DefaultTreeModel filterTree, MutableTreeNode parentNode, int newOperatorID)
		throws IncorrectIdentifierException;
		
	public PropertyName addPropertyNameChild();
	
/*	public HashMap createFeatureAttributesMap() 
		throws InternalErrorException,InstanceNotFoundException;*/
		
	public Literal addLiteralChild();
	
	public PropertyName updatePropertyName(PropertyName propertyName, MutableTreeNode parentNode, Integer insert, String value);

	public Literal updateLiteral(Literal literal, MutableTreeNode parentNode, Integer insert, String value);

	public DistanceBufferOp addDistanceBufferOp(int operatorID)
		throws IncorrectIdentifierException;
		
	public DistanceBufferOp updateDistanceBufferOp(DistanceBufferOp distanceBufferOp, double distance,GMLGeometry geometry,MutableTreeNode oldNode,MutableTreeNode parentNode,DefaultTreeModel filterTree,Integer insert,Integer index);
	
	public BBOXOp addBBOXOp(int operatorID)
		throws IncorrectIdentifierException;
		
	public BBOXOp updateBBOXOp(BBOXOp bboxOp, GMLBox gmlBox,MutableTreeNode oldNode,MutableTreeNode parentNode,DefaultTreeModel filterTree,Integer insert,Integer index);
	
	public GMLGeometry addGMLGeometry() throws GMLException;
	
	public GMLGeometry updateGMLGeometry(GMLGeometry gmlGeometry,String srs,String coordinates);
	
	public GMLBox addGMLBox();
	
	public GMLBox updateGMLBox(GMLBox gmlBox,double minx, double miny, double maxx, double maxy, String srs);
	
	public Filter updateFilter(MutableTreeNode rootNode) throws MalformedFilterException,FilterConstructionException;
			
	public HashMap getFeatureAttributes(Layer layer);

}