package com.localgis.server;

import java.security.AccessControlContext;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

import org.eclipse.jetty.plus.jaas.JAASGroup;
import org.eclipse.jetty.plus.jaas.JAASPrincipal;
import org.eclipse.jetty.plus.jaas.JAASRole;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;
import org.eclipse.jetty.util.security.Password;

import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.DefaultLocalgisCallbackHandler;
import com.localgis.security.model.LocalgisJAASGroup;
import com.localgis.security.model.LocalgisJAASUserPrincipal;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SessionsContextShared extends ContextShared {
	
	private static SessionsContextShared instance = new SessionsContextShared();
	
	protected SessionsContextShared(){	}
	
	public static SessionsContextShared getContextShared(){
		return instance;
	}
	
	@Override
	public XStream getXStreamSerializer(){		
		XStream xStreamSerializer = new XStream(new DomDriver());						
		xStreamSerializer.registerConverter(new MapConverter(xStreamSerializer.getMapper()));
		//XStream xStreamSerializer = new XStream();		
		
		xStreamSerializer.alias("ListaSesiones", ListaSesiones.class);
		xStreamSerializer.alias("Hashtable", Hashtable.class);
		xStreamSerializer.alias("HashSet", HashSet.class);
		xStreamSerializer.alias("HashMap", HashMap.class);
		xStreamSerializer.alias("LinkedList", LinkedList.class);
		xStreamSerializer.alias("Sesion", Sesion.class);	
		xStreamSerializer.alias("Enumeration", Enumeration.class);
		xStreamSerializer.alias("Iterator", Iterator.class);
		xStreamSerializer.alias("Entry", Map.Entry.class);
		xStreamSerializer.alias("Collections", Collections.class);
				
		xStreamSerializer.alias("Subject", Subject.class);
		xStreamSerializer.alias("Principal", Principal.class);
		xStreamSerializer.alias("LoginContext", LoginContext.class);
		xStreamSerializer.alias("Subject", Subject.class);	
		xStreamSerializer.alias("Date", Date.class);
		xStreamSerializer.alias("JAASGroup", JAASGroup.class);
		xStreamSerializer.alias("JAASRole", JAASRole.class);
		xStreamSerializer.alias("Password", Password.class);
		xStreamSerializer.alias("LocalgisJAASGroup", LocalgisJAASGroup.class);
		xStreamSerializer.alias("LocalgisJAASUserPrincipal", LocalgisJAASUserPrincipal.class);
		xStreamSerializer.alias("Group", Group.class);
		xStreamSerializer.alias("JAASPrincipal", JAASPrincipal.class);
		xStreamSerializer.alias("JAASUserPrincipal", JAASUserPrincipal.class);
		xStreamSerializer.alias("AccessControlContext", AccessControlContext.class);
		xStreamSerializer.alias("DefaultLocalgisCallbackHandler", DefaultLocalgisCallbackHandler.class);
							
		return xStreamSerializer;
	}
		
}
