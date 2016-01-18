/*
 * Created on 07-jun-2004
 *
 */
package com.geopista.style.sld.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.UserStyle;
import org.deegree.services.wfs.filterencoding.Filter;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.ScaleRange;
import com.geopista.style.sld.model.impl.actions.CreateDefaultRuleAction;
import com.geopista.style.sld.model.impl.actions.CreateDefaultScaleRangeAction;
import com.geopista.style.sld.model.impl.actions.CreateDefaultUserStyleAction;
import com.geopista.style.sld.model.impl.actions.CreateRuleAction;
import com.geopista.style.sld.model.impl.actions.DeleteRuleAction;
import com.geopista.style.sld.model.impl.actions.DeleteScaleRangeAction;
import com.geopista.style.sld.model.impl.actions.DeleteUserStyleAction;
import com.geopista.style.sld.model.impl.actions.GetFeatureAttributesAction;
import com.geopista.style.sld.model.impl.actions.GetRuleAction;
import com.geopista.style.sld.model.impl.actions.GetScaleRangeAction;
import com.geopista.style.sld.model.impl.actions.GetScaleRangeListAction;
import com.geopista.style.sld.model.impl.actions.GetUserStyleAction;
import com.geopista.style.sld.model.impl.actions.InsertUserStylesAction;
import com.geopista.style.sld.model.impl.actions.MoveRuleAction;
import com.geopista.style.sld.model.impl.actions.UpdateCustomRuleAction;
import com.geopista.style.sld.model.impl.actions.UpdateCustomUserStyleAction;
import com.geopista.style.sld.model.impl.actions.UpdateSLDStyleAction;
import com.geopista.style.sld.model.impl.actions.UpdateScaleRangeAction;
import com.vividsolutions.jump.workbench.model.Layer;

import es.enxenio.util.exceptions.InternalErrorException;


/**
 * @author enxenio s.l.
 *
 */
public class SLDFacadeImpl implements SLDFacade {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#getUserStyle(java.lang.String,java.util.ArrayList)
	 */
	public UserStyle getUserStyle(String styleName,ArrayList userStyleList) {
		
		GetUserStyleAction getUserStyleAction = new GetUserStyleAction(styleName,userStyleList);
		return (UserStyle) getUserStyleAction.execute();
	}


	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#getRule(int, java.util.ArrayList)
	 */
	public Rule getRule(int position, ArrayList ruleList) {
		
		GetRuleAction getRuleAction = new GetRuleAction(position,ruleList);
		return (Rule) getRuleAction.execute();
	}

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#createDefaultUserStyle()
	 */
	public UserStyle createDefaultUserStyle() {
		
		CreateDefaultUserStyleAction createDefaultUserStyleAction = 
			new CreateDefaultUserStyleAction();
			
		return (UserStyle) createDefaultUserStyleAction.execute();
	}


	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#createDefaultRule()
	 */
	public Rule createDefaultRule() {
		
		CreateDefaultRuleAction createDefaultRuleAction = new CreateDefaultRuleAction();
		return (Rule) createDefaultRuleAction.execute();
	}


	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#deleteUserStyle(java.lang.String,com.geopista.style.sld.model.SLDStyle)
	 */
	public void deleteUserStyle(String styleName, ArrayList userStyleList) {
		
		DeleteUserStyleAction deleteUserStyleAction = 
			new DeleteUserStyleAction(styleName,userStyleList);
		deleteUserStyleAction.execute();

	}


	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#deleteRule(int, org.deegree.graphics.sld.FeatureTypeStyle)
	 */
	public void deleteRule(int position, ArrayList ruleList) {
		
		DeleteRuleAction deleteRuleAction = new DeleteRuleAction(position,ruleList);
		deleteRuleAction.execute();

	}


	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#getScaleRangeList(org.deegree.graphics.sld.FeatureTypeStyle)
	 */	
	public List getScaleRangeList(FeatureTypeStyle featureTypeStyle) {
		
		GetScaleRangeListAction getScaleRangeListAction = 
			new GetScaleRangeListAction(featureTypeStyle);
		return (List) getScaleRangeListAction.doExecute();
	}

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#createDefaultScaleRange()
	 */	
	public ScaleRange createDefaultScaleRange() {
		
		CreateDefaultScaleRangeAction createDefaultScaleRangeAction = 
			new CreateDefaultScaleRangeAction();
		return (ScaleRange) createDefaultScaleRangeAction.doExecute();	
	}

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#deleteScaleRange(int,java.util.List)
	 */		
	public void deleteScaleRange(int position, List scaleRangeList) {
		
		DeleteScaleRangeAction deleteScaleRangeAction = 
			new DeleteScaleRangeAction(position, scaleRangeList);
		deleteScaleRangeAction.doExecute();
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.style.sld.model.SLDFacade#getScaleRange(int,java.util.List)
	 */		
	public ScaleRange getScaleRange(int position, List scaleRangeList) {
		
		GetScaleRangeAction getScaleRangeAction = 
			new GetScaleRangeAction(position, scaleRangeList);
		return (ScaleRange)getScaleRangeAction.doExecute();
	}
	
	public ScaleRange updateScaleRange(Double minScale, Double maxScale, Integer insert,
		ScaleRange scaleRange, List scaleRangeList) {
		
		UpdateScaleRangeAction updateScaleRangeAction = 
			new UpdateScaleRangeAction(minScale,maxScale,insert,scaleRange,scaleRangeList);
		return (ScaleRange)updateScaleRangeAction.doExecute();	
	}

	public Rule updateCustomRule(String ruleName, ScaleRange scaleRange,
		HashMap style, Filter filter, Integer insert, Rule rule) {
			
		UpdateCustomRuleAction updateCustomRuleAction = 
			new UpdateCustomRuleAction(ruleName,scaleRange,style,filter,insert,rule);
		return (Rule)updateCustomRuleAction.doExecute();
	}

	public Rule createRule(String ruleName, HashMap style, String propertyName,
		Integer operationID, List expressions, Integer insert, ScaleRange scaleRange) {
			
		CreateRuleAction createRuleAction = 
			new CreateRuleAction(ruleName,style,
				propertyName,operationID,expressions,insert,scaleRange);
		return (Rule)createRuleAction.doExecute();
	}
	
	public UserStyle updateCustomUserStyle(String styleName,String styleTitle,String styleAbstract,
		Boolean isDefault,List ftsList,List scaleRangelist, Integer insert, UserStyle userStyle, 
		List userStyleList) {
	
		UpdateCustomUserStyleAction updateCustomUserStyleAction = 
			new UpdateCustomUserStyleAction(styleName,styleTitle,styleAbstract,
				isDefault,ftsList,scaleRangelist,insert,userStyle,userStyleList);
		return (UserStyle)updateCustomUserStyleAction.doExecute();		
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.model.Facade#insertUserStyles(java.lang.String,org.deegree.graphics.sld.UserStyle[])
	 */	
	public void insertUserStyles(String layerName, List userStyles) throws InternalErrorException {
		InsertUserStylesAction action = 
			new InsertUserStylesAction(layerName,userStyles);
		try {
			// TODO: Must fetch a conection to the DB  
			action.execute(null);
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.model.Facade#getFeatureAttributes(java.lang.String)
	 */	
	public HashMap getFeatureAttributes(Layer layer) {
		GetFeatureAttributesAction action =  
			new GetFeatureAttributesAction(layer);
		return (HashMap)action.doExecute();
	}

	public SLDStyle updateSLDStyle(SLDStyle sldStyle, ArrayList userStyleList) {
		
		UpdateSLDStyleAction updateSLDStyleAction = new UpdateSLDStyleAction(sldStyle,userStyleList);
		return (SLDStyle) updateSLDStyleAction.execute();
	}

	public void moveRule(int initialPosition,int finalPosition,List ruleList) {
		
		MoveRuleAction moveRuleAction = new MoveRuleAction(initialPosition,finalPosition,ruleList);
		moveRuleAction.execute();
	}
}
