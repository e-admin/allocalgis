package com.geopista.utils.alfresco.manager.implementations;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.ACL;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.AuthorityFilter;
import org.alfresco.webservice.accesscontrol.GetPermissionsResult;
import org.alfresco.webservice.accesscontrol.HasPermissionsResult;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.accesscontrol.SiblingAuthorityFilter;
import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.Condition;
import org.alfresco.webservice.action.Rule;
import org.alfresco.webservice.action.RuleType;
import org.alfresco.webservice.test.BaseWebServiceSystemTest;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;

import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoAccessControlManager;
import com.geopista.utils.alfresco.manager.interfaces.AlfrescoActionManager;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Clase que gestiona las acciones en Alfresco (los métodos requieren autenticación)
 */
public class AlfrescoActionManagerImpl implements AlfrescoActionManager{
	
	/**
     * Variables
     */
	private static AlfrescoActionManagerImpl instance = new AlfrescoActionManagerImpl();
	   
	/**
     * Constructor
     */
	protected AlfrescoActionManagerImpl() {
	}
	
	/**
     * Solicita la instancia única de la clase (Singleton)
     * @return AlfrescoActionManagerImpl
     */
	public static AlfrescoActionManagerImpl getInstance(){
		return instance;
	}

	public Action [] createAction(AlfrescoKey key, Action [] action) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getActionService().saveActions(AlfrescoManagerUtils.getReference(key), action);
	}
	
//	public Action createAction(AlfrescoKey key) throws AccessControlFault, RemoteException{
//		Action [] actions = createAction(key, new Action [] {});
//		if(actions != null && actions.length>0){
//			return actions[0];
//		}
//		return null;
//	}
	
	
	public Rule [] createRule(AlfrescoKey key, Rule [] rule) throws AccessControlFault, RemoteException{
		return WebServiceFactory.getActionService().saveRules(AlfrescoManagerUtils.getReference(key), rule);
	}

	public Rule [] createRule(AlfrescoKey key) throws AccessControlFault, RemoteException{
		Reference ref = AlfrescoManagerUtils.getReference(key);
		//Rule[] rules1 = WebServiceFactory.getActionService().getRules(ref, null);
		Rule rule = new Rule();
		rule.setRuleTypes(new String [] {RULETYPE_UPDATE, RULETYPE_INBOUND, RULETYPE_OUTBOUND});
		rule.setTitle("Title");
	
//		Random generator = new Random(); 
//		int conditionId = generator.nextInt(1000);
//		Condition condition = new Condition();
//		condition.setId(String.valueOf(conditionId));
//		condition.setConditionName("no-condition");
//		condition.setParameters(new NamedValue[]{new NamedValue("test", false, Constants.TYPE_CONTENT, null)}); 
		Condition condition = new Condition();
		condition.setConditionName("no-condition"); 
		
		
		NamedValue[] aparameters = new NamedValue[]{new NamedValue("test", false, Constants.TYPE_CONTENT, null)}; 
				
		
		NamedValue[] parameters = new NamedValue[]{new NamedValue("aspect-name", false,"{http://www.alfresco.org/model/content/1.0}generalclassifiable", null)};
		Action newAction = new Action();
		newAction.setActionName("add-features");
		newAction.setParameters(parameters);

		// Create the composite action
		Action action = new Action();
		//action.setActionName("import");
		action.setActionName("composite-action");
		action.setActionReference(ref);
		action.setConditions(new Condition[]{condition});
		action.setActions(new Action[]{newAction}); 
		
//		Action action = new Action();
//		//action.setActionName("extract-metadata");		
//		action.setActionName("auto-versioning");
//		action.setParameters(aparameters); 
//		action.setConditions(new Condition[] {condition}); 
		
		rule.setAction(action);
		rule.setExecuteAsynchronously(false);
		rule.setOwningReference(ref);
        rule.setRuleReference(ref);
        
		return WebServiceFactory.getActionService().saveRules(ref, new Rule[] {rule});
	}
	
//	
//	259         assertNull(rules1);
//	260         
//	261         // Create the action
//	262 NamedValue[] parameters = new NamedValue[]{new NamedValue("aspect-name", Constants.ASPECT_CLASSIFIABLE)};
//	263         Action newAction = new Action();
//	264         newAction.setActionName("add-features");
//	265         newAction.setParameters(parameters);
//	266         
//	267         // Create the rule
//	268 Rule newRule = new Rule();
//	269         newRule.setRuleType("incomming");
//	270         newRule.setTitle("This rule adds the classificable aspect");
//	271         newRule.setActions(new Action[]{newAction});
//	272         
//	273         // Save the rule
//	274 Rule[] saveResults1 = this.actionService.saveRules(BaseWebServiceSystemTest.contentReference, new Rule[]{newRule});
//	275         assertNotNull(saveResults1);
//	276         assertEquals(1, saveResults1.length);
//	277         Rule savedRule1 = saveResults1[0];
//	278         assertNotNull(savedRule1);
//	279         assertNotNull(savedRule1.getId());
//	280         assertEquals(BaseWebServiceSystemTest.contentReference.getUuid(), savedRule1.getReference().getUuid());
//	281         assertEquals("incomming", savedRule1.getRuleType());
//	282         assertEquals("This rule adds the classificable aspect", savedRule1.getTitle());
//	283         assertFalse(savedRule1.isExecuteAsynchronously());
//	284         
//	285         // Check the actions of the saved rule
//	286 // TODO
//	287
//	288         // Update the rule
//	289 savedRule1.setTitle("The title has been updated");
//	290         savedRule1.setExecuteAsynchronously(true);
//	291         
//	292         // Save the action
//	293 Rule[] saveResults2 = this.actionService.saveRules(BaseWebServiceSystemTest.contentReference, new Rule[]{savedRule1});
//
//	Read more: http://kickjava.com/src/org/alfresco/webservice/test/ActionServiceSystemTest.java.htm#ixzz1r5ZvKtfx

	
}
