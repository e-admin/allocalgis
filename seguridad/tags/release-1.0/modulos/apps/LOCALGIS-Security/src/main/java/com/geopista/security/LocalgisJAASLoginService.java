package com.geopista.security;

import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.eclipse.jetty.plus.jaas.JAASGroup;
import org.eclipse.jetty.plus.jaas.JAASLoginService;
import org.eclipse.jetty.plus.jaas.JAASRole;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.log.Log;

import com.geopista.security.dnie.utils.CertificateUtils;
import com.localgis.security.model.LocalgisJAASGroup;

public class LocalgisJAASLoginService extends JAASLoginService{

	protected HashMap userMap;
	//private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LocalgisJAASLoginService.class);
    	
	 /* ---------------------------------------------------- */
    protected class UserInfo
    {
        String name;
        JAASUserPrincipal principal;
        LoginContext context;
        

        public UserInfo (String name, JAASUserPrincipal principal, LoginContext context)
        {
            this.name = name;
            this.principal = principal;
            this.context = context;
        }

        public String getName ()
        {
            return name;
        }

        public JAASUserPrincipal getJAASUserPrincipal ()
        {
            return principal;
        }

        public LoginContext getLoginContext ()
        {
            return context;
        }
    }
	
	public LocalgisJAASLoginService() {
		super();		
		System.setProperty("java.security.auth.login.config", LocalgisJAASLoginService.class.getResource("/config/jaasconfig.conf").toString());
		userMap = new HashMap();
	}

	@Override
	public UserIdentity login(String username, Object credentials) {
		  try
	        {

	        	//logger.info("Usuario que entra:"+username);
	            UserInfo info = null;
	            synchronized (this){ 
	            	info=(UserInfo)userMap.get(username);
	            }
	            //user has been previously authenticated, but
	            //re-authentication has been requested, so flow that
	            //thru all the way to the login module mechanism and
	            //remove their previously authenticated status
	            //TODO: ensure cache state and "logged in status" are synchronized
	            
	            if (info != null)
	            {
	            	//logger.info("!!!!!!!Reautenticacion:"+username);
	            	synchronized (this){ 
	            		userMap.remove (username);
	            	}
	            }

	            AbstractLocalgisCallbackHandler callbackHandler = new DefaultLocalgisCallbackHandler();
	            //----NUEVO---->
	           // X509Certificate credential = B64Code.decode(credentials);
				if(credentials instanceof X509Certificate[]){			
					X509Certificate [] certificate = (X509Certificate[]) credentials;					
				 	username = CertificateUtils.getNIFfromSubjectDN(certificate[0].getSubjectDN().getName());	
				 	credentials = certificate[0];
				 	//BORRAR
				 	System.out.println(CertificateUtils.getNIFfromSubjectDN(certificate[0].getSubjectDN().getName()));	
				 	//FIN BORRAR
				}
				//--FIN NUEVO-->
	        	callbackHandler.setUserName(username);
	            callbackHandler.setCredential(credentials);	     

	            //set up the login context
	            Subject subject = new Subject();
	            LoginContext loginContext = new LoginContext(_loginModuleName, subject, callbackHandler);
	            
	            loginContext.login();

	            //login success
	            JAASUserPrincipal userPrincipal = new JAASUserPrincipal(username, subject, loginContext);
	            //JAASUserPrincipal userPrincipal = new JAASUserPrincipal(username, subject, null);
	            subject.getPrincipals().add(userPrincipal);
			
	            synchronized (this){ 
	            	userMap.put (username, new UserInfo (username, _defaultUser, loginContext));
	            }  
	            
	            String[] roles = getRoles(getGroup(loginContext.getSubject()));
	            return _identityService.newUserIdentity(loginContext.getSubject(), userPrincipal, roles);
	        }
	        catch (LoginException e)
	        {
	            Log.warn (e);
	            return null;
	        }
	}

	@Override
	public void logout(UserIdentity user) {
		 try
	        {
	            if (!(user instanceof JAASUserPrincipal))
	                throw new IllegalArgumentException (user + " is not a JAASUserPrincipal");

		    String key = ((JAASUserPrincipal)user).getName();
	            UserInfo info = (UserInfo)userMap.get(key);

	            if (info == null)
	                Log.warn ("Logout called for user="+user+" who is NOT in the authentication cache");

	            info.getLoginContext().logout();
		    userMap.remove (key);

		    Log.info(user+" has been LOGGED OUT");
	        }
	        catch (LoginException e)
	        {
	            Log.warn (e);
	        }
	}
//
//	private String[] getRoles (Subject subject)
//	{
//	     //get all the roles of the various types
//	     String[] roleClassNames = getRoleClassNames();
//	     Collection<String> groups = new LinkedHashSet<String>();
//	     try
//	     {
//	         for (String roleClassName : roleClassNames)
//	         { 
//	             Class load_class = Thread.currentThread().getContextClassLoader().loadClass(roleClassName);
//	             Set<JAASGroup> rolesForType = subject.getPrincipals(load_class);
//	             for (JAASGroup jaasGroup : rolesForType)
//	             {
//	            	Enumeration jaasRoles = jaasGroup.members();
//	            	while(jaasRoles.hasMoreElements()){	      
//	            		//HashMap jaasRoles = (HashMap)jaasRolesMap.nextElement();
//	            		//Iterator jaasRole = jaasRoles.keySet().iterator();
//	            		JAASRole role = (JAASRole) jaasRoles.nextElement();
//	            		groups.add(role.getName());	                
//	            	}
//	             }
//	         }
//	              
//	         return groups.toArray(new String[groups.size()]);
//	     }
//	     catch (ClassNotFoundException e)
//	     {
//	         throw new RuntimeException(e);
//	     }
//	 }
	
//	private String[] getRoles (Subject subject)
//	{
//	     //get all the roles of the various types
//	     String[] roleClassNames = getRoleClassNames();
//	     Collection<String> groups = new LinkedHashSet<String>();
//	     try
//	     {
//	         for (String roleClassName : roleClassNames)
//	         { 	        	 
//	             Class load_class = Thread.currentThread().getContextClassLoader().loadClass(roleClassName);
//	             Set<LocalgisJAASGroup> rolesForType = (Set<LocalgisJAASGroup>) subject.getPrincipals(load_class);
//	             if(rolesForType.size()>0){
//	            	 Iterator it = rolesForType.iterator();
//	            	 while(it.hasNext()){
//	            		 Object o = it.next();
//	            		 if(o instanceof JAASGroup){
//	            			JAASGroup jaasGroup = (JAASGroup) o;
//	            			Enumeration jaasRoles = jaasGroup.members();
//		 		            while(jaasRoles.hasMoreElements()){	      
//		 		            	JAASRole role = (JAASRole) jaasRoles.nextElement();
//		 		            	groups.add(role.getName());	                
//		 		            }
//	            		 }
//	            		 else{// if(o instanceof LocalgisJAASGroup){	            		
//		            		LocalgisJAASGroup jaasGroup = (LocalgisJAASGroup) o;
//		            		Enumeration jaasRoles = jaasGroup.members();
//			 		        while(jaasRoles.hasMoreElements()){	      
//			 		        	JAASRole role = (JAASRole) jaasRoles.nextElement();
//			 		        	groups.add(role.getName());	                
//			 		        }		            		 
//	            		 }
//	            	 }
//		         }
//	         }
//	         return groups.toArray(new String[groups.size()]);
//	     }
//	     catch (ClassNotFoundException e)
//	     {
//	         throw new RuntimeException(e);
//	     }
//	 }
	
	private String[] getRoles (Group group)
	{
		Collection<String> groups = new LinkedHashSet<String>();
		Enumeration jaasRoles = group.members();
		while(jaasRoles.hasMoreElements()){	      
			JAASRole role = (JAASRole) jaasRoles.nextElement();
			groups.add(role.getName());	                
		}	
	    return groups.toArray(new String[groups.size()]);
	 }
	
	private Group getGroup (Subject subject)
	{
	     //get all the roles of the various types
	    // String[] roleClassNames = getRoleClassNames();
	     String[] roleClassNames = new String[]{"org.eclipse.jetty.plus.jaas.JAASGroup", "com.localgis.security.model.LocalgisJAASGroup"};
	     try
	     {
	         for (String roleClassName : roleClassNames)
	         { 
	             Class load_class = Thread.currentThread().getContextClassLoader().loadClass(roleClassName);
	             Set<Group> rolesForType = subject.getPrincipals(load_class);
	             if(rolesForType.size()>0){
	            	Iterator<Group> it = rolesForType.iterator();
	            	while(it.hasNext()){ 
	            		Group group = it.next();
	            		if(group instanceof LocalgisJAASGroup){
		            		LocalgisJAASGroup localgisJAASGroup = new LocalgisJAASGroup(group.getName()); 
		            	 	localgisJAASGroup.setMembers(group.members());
		            	 	return localgisJAASGroup;	
	            		}
		            	else{	
		            	 	return group;
		            	}
	            	}
		         }
	         }
	     }
	     catch (ClassNotFoundException e)
	     {
	         throw new RuntimeException(e);
	     }
	     return null;
	 }


	
}
