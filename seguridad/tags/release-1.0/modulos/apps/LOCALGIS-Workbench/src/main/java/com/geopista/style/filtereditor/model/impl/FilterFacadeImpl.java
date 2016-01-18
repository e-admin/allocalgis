/*
 * Created on 16-sep-2004
 *
 */
package com.geopista.style.filtereditor.model.impl;

import java.util.HashMap;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.deegree.gml.GMLBox;
import org.deegree.gml.GMLException;
import org.deegree.gml.GMLGeometry;
import org.deegree.services.wfs.filterencoding.Filter;
import org.deegree.services.wfs.filterencoding.FilterConstructionException;

import com.geopista.style.filtereditor.model.FilterFacade;
import com.geopista.style.filtereditor.model.exceptions.IncorrectIdentifierException;
import com.geopista.style.filtereditor.model.exceptions.MalformedFilterException;
import com.geopista.style.filtereditor.model.impl.actions.AddBBOXOpAction;
import com.geopista.style.filtereditor.model.impl.actions.AddDistanceBufferOpAction;
import com.geopista.style.filtereditor.model.impl.actions.AddGMLBoxAction;
import com.geopista.style.filtereditor.model.impl.actions.AddGMLGeometryAction;
import com.geopista.style.filtereditor.model.impl.actions.AddLiteralChildAction;
import com.geopista.style.filtereditor.model.impl.actions.AddOperatorChildAction;
import com.geopista.style.filtereditor.model.impl.actions.AddPropertyNameChildAction;
import com.geopista.style.filtereditor.model.impl.actions.ChangeOperatorTypeAction;
import com.geopista.style.filtereditor.model.impl.actions.CreateEmptyFilterAction;
import com.geopista.style.filtereditor.model.impl.actions.CreateFilterTreeAction;
import com.geopista.style.filtereditor.model.impl.actions.DeleteNodeAction;
import com.geopista.style.filtereditor.model.impl.actions.GetFeatureAttributesAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateBBOXOpAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateDistanceBufferOpAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateFilterAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateGMLBoxAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateGMLGeometryAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdateLiteralAction;
import com.geopista.style.filtereditor.model.impl.actions.UpdatePropertyNameAction;
import com.vividsolutions.jump.workbench.model.Layer;

import es.enxenio.util.exceptions.InternalErrorException;
import es.enxenio.util.exceptions.MissingConfigurationParameterException;

/**
 * @author enxenio s.l.
 *
 */
public class FilterFacadeImpl implements FilterFacade {

	public FilterFacadeImpl() throws InternalErrorException,MissingConfigurationParameterException {}
	/* (non-Javadoc)
	 * @see com.geopista.styles.filtereditor.model.FilterFacade#createEmptyFilter()
	 */
	public DefaultTreeModel createEmptyFilter() throws IncorrectIdentifierException {
	
		CreateEmptyFilterAction createEmptyFilterAction = new CreateEmptyFilterAction();
		return (DefaultTreeModel)createEmptyFilterAction.execute();		
	}
	
	public DefaultTreeModel createFilterTree(Filter filter) 
		throws IncorrectIdentifierException,MalformedFilterException {
		
		CreateFilterTreeAction createFilterTreeAction = new CreateFilterTreeAction(filter);
		return (DefaultTreeModel)createFilterTreeAction.execute();
	}
	
	public void deleteNode(DefaultTreeModel filterTree,MutableTreeNode node) {
		
		DeleteNodeAction deleteNodeAction = new DeleteNodeAction(filterTree,node);
		deleteNodeAction.execute();
	}
	
	public void changeOperatorType(DefaultTreeModel filterTree,MutableTreeNode oldNode,int newOperatorID)
		throws IncorrectIdentifierException {
					
		ChangeOperatorTypeAction changeOperatorTypeAction = 
			new ChangeOperatorTypeAction(filterTree,oldNode,newOperatorID);
		changeOperatorTypeAction.execute();
	}

	public void addOperatorChild(DefaultTreeModel filterTree,MutableTreeNode parentNode,int operatorID)
		throws IncorrectIdentifierException {
					
		AddOperatorChildAction addOperatorChildAction = 
			new AddOperatorChildAction(filterTree,parentNode,operatorID);
		addOperatorChildAction.execute();
	}
	
	public PropertyName addPropertyNameChild() {
		
		AddPropertyNameChildAction addPropertyNameChildAction = new AddPropertyNameChildAction();
		return (PropertyName)addPropertyNameChildAction.execute();
	}
	
/*	public HashMap createFeatureAttributesMap() 
		throws InternalErrorException,InstanceNotFoundException{
		HashMap featureAttributeMap = null;
		try {
			Connection connection = dataSource.getConnection();
			CreateFeatureAttributesMapAction createFeatureAttributesMapAction = 
				new CreateFeatureAttributesMapAction();
			featureAttributeMap = (HashMap)createFeatureAttributesMapAction.execute(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return featureAttributeMap;
	}
*/	
	public Literal addLiteralChild() {
		
		AddLiteralChildAction addLiteralChildAction = new AddLiteralChildAction();
		return (Literal)addLiteralChildAction.execute();
	}
	
	public PropertyName updatePropertyName(PropertyName propertyName, MutableTreeNode parentNode,
		Integer insert,String value) {
			
		UpdatePropertyNameAction updatePropertyNameAction = 
			new UpdatePropertyNameAction(propertyName,parentNode,insert,value);
		return (PropertyName)updatePropertyNameAction.execute();
	}

	public Literal updateLiteral(Literal literal, MutableTreeNode parentNode,
		Integer insert,String value) {
			
		UpdateLiteralAction updateLiteralAction = 
			new UpdateLiteralAction(literal,parentNode,insert,value);
		return (Literal)updateLiteralAction.execute();
	}
	
	public DistanceBufferOp addDistanceBufferOp(int operatorID) 
		throws IncorrectIdentifierException {
		
		AddDistanceBufferOpAction addDistanceBufferOpAction = 
			new AddDistanceBufferOpAction(operatorID);
		return (DistanceBufferOp)addDistanceBufferOpAction.execute();
	}
	
	public DistanceBufferOp updateDistanceBufferOp(DistanceBufferOp distanceBufferOp, double distance, GMLGeometry geometry,MutableTreeNode oldNode,MutableTreeNode parentNode,DefaultTreeModel filterTree,Integer insert,Integer index) {
		
		UpdateDistanceBufferOpAction updateDistanceBufferOpAction = 
			new UpdateDistanceBufferOpAction(distanceBufferOp,distance,geometry,oldNode,parentNode,filterTree,insert,index);
		return (DistanceBufferOp)updateDistanceBufferOpAction.execute();
	}

	public BBOXOp addBBOXOp(int operatorID) 
		throws IncorrectIdentifierException {
		
		AddBBOXOpAction addBBOXOpAction = 
			new AddBBOXOpAction(operatorID);
		return (BBOXOp)addBBOXOpAction.execute();
	}

	public BBOXOp updateBBOXOp(BBOXOp bboxOp, GMLBox gmlBox,MutableTreeNode oldNode, MutableTreeNode parentNode, DefaultTreeModel filterTree,Integer insert,Integer index) {
		
		UpdateBBOXOpAction updateBBOXOpAction = 
			new UpdateBBOXOpAction(bboxOp,gmlBox,oldNode,parentNode,filterTree,insert,index);
		return (BBOXOp)updateBBOXOpAction.execute();
	}
	
	public GMLGeometry addGMLGeometry() throws GMLException {
		
		AddGMLGeometryAction addGMLGeometryAction = new AddGMLGeometryAction();
		return (GMLGeometry)addGMLGeometryAction.execute();
	}
	
	public GMLGeometry updateGMLGeometry(GMLGeometry gmlGeometry,String srs,String coordinates) {
		
		UpdateGMLGeometryAction updateGMLGeometryAction = new UpdateGMLGeometryAction(gmlGeometry,srs,coordinates);
		return (GMLGeometry)updateGMLGeometryAction.execute();
	}
	
	public GMLBox addGMLBox() {
		
		AddGMLBoxAction addGMLBoxAction = new AddGMLBoxAction();
		return (GMLBox)addGMLBoxAction.execute();
	}
	
	public GMLBox updateGMLBox(GMLBox gmlBox,double minx, double miny, double maxx, double maxy, String srs) {
		
		UpdateGMLBoxAction updateGMLBoxAction = new UpdateGMLBoxAction(gmlBox,minx,miny,maxx,maxy,srs);
		return (GMLBox)updateGMLBoxAction.execute();
	}
	
	public Filter updateFilter(MutableTreeNode rootNode) throws MalformedFilterException,FilterConstructionException {
		
		UpdateFilterAction updateFilterAction = new UpdateFilterAction(rootNode);
		return (Filter)updateFilterAction.execute();
	}
		
	public HashMap getFeatureAttributes(Layer layer) {
		GetFeatureAttributesAction action =  
			new GetFeatureAttributesAction(layer);
		return (HashMap)action.doExecute();
	}

}
