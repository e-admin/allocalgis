/**
 * Login.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.web;

import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.plus.jaas.JAASGroup;
import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.app.administrador.MunicipalityOperations;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;
import com.geopista.server.administrador.web.SaveSSOAppSession;
import com.geopista.server.database.COperacionesControl;
import com.localgis.security.model.LocalgisJAASGroup;
import com.localgis.security.model.LocalgisJAASUserPrincipal;
import com.localgis.server.SessionsContextShared;


/**
 * Esta clase es la primera que se llama al entrar en una aplicación
 * su función consiste en crear la sesión del usuario dentro del sistema
 * Para ello regoge del user principal el nombre del usuario y obtiene
 * los datos de este de la base de datos.
 * También obtiene el listado de entidades y municipios.
 * 
 * @author angeles
 *
 */
public class Login extends HttpServlet
{
  private static final long serialVersionUID = 1L;
	/**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Login.class);
    
    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException
    {
    	response.getWriter().append("Authentication Successful!");    	
    }
    
    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	
    	String sPassword=null;
    	try {
    	/**Obtenemos la sesion**/
        ISesion sSesion = getSesion(request);
        /**Grabamos el Login**/
			sPassword=grabarLogin(sSesion, request);
        /**Almacenamos la sesión en Memoria**/
        //PasarelaAdmcar.listaSesiones.add(sSesion);
        
//    	try {
//			JGroupsReceiverAdapter jGroupsReceiverAdapter = new JGroupsReceiverAdapter();
//		   	JGroupsChannel.getJChannel().connect("Cluster");
//		   	JGroupsChannel.getJChannel().setReceiver(jGroupsReceiverAdapter);    		   	
//		   	PasarelaAdmcar.listaSesiones = (ListaSesiones) jGroupsReceiverAdapter.getObject();
//		   	PasarelaAdmcar.listaSesiones.add(sSesion);		   	
//		   	JGroupsChannel.getJChannel().send(new Message(JGroupsChannel.getJChannel().getView().getMembers().get(0), JGroupsChannel.getJChannel().getAddress(), PasarelaAdmcar.listaSesiones));
//		} catch (Exception e) {
//			System.out.println(e);
//		}	    
        
        
      
        /**Obtenemos las listas correspondientes**/
        List lista = new ArrayList();
        List listMunicipios = new ArrayList();
        List listaEntidades = null;
        String idEntidad = "0";
        if (sSesion.getIdEntidad().equals("0")){
            listaEntidades = getEntidades();
            //idEntidad = (String)((Object[])listaEntidades.get(0))[0];
        }
        else
        	idEntidad=sSesion.getIdEntidad();
        
        lista = getMunicipiosEntidad(idEntidad);
        MunicipalityOperations municipalityOperations = new MunicipalityOperations();
        listMunicipios = municipalityOperations.construirListaMunicipios(lista);
        sSesion.setAlMunicipios(listMunicipios);
        
        PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
        PasarelaAdmcar.listaSesiones.add(sSesion);
        SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);
                

        CResultadoOperacion resultado = new CResultadoOperacion(true, sSesion.getIdSesion());
        Vector vector = new Vector();
        vector.addElement(sSesion.getIdSesion());        
        vector.addElement(idEntidad);
        //vector.addElement(sIdMunicipio);
        vector.addElement(lista);
        vector.addElement(listaEntidades);
		vector.addElement(sPassword);
        resultado.setVector(vector);
        response.setContentType ("text/html");
        Writer writer = response.getWriter();
        writer.write (resultado.buildResponse());
        writer.flush();
	    writer.close();
		} catch (Throwable e) {
			logger.error("Se ha producido una excepcion al realizar el login",e);
			throw new ServletException();
		}
    }

    /**
     * Obtiene la sesion del request y de la base de datos
     * @param request
     * @return
     */
    private ISesion getSesion(HttpServletRequest request){
    	/**Obtenemos el principal***/
    	JAASUserPrincipal jaasUserPrincipal = (JAASUserPrincipal) request.getUserPrincipal();
    	//Principal jaasUserPrincipal = (Principal) request.getUserPrincipal();
    	//GeopistaJAASUserPrincipal userPrincipal = new GeopistaJAASUserPrincipal(jaasUserPrincipal.getName(), jaasUserPrincipal.getSubject(), null);
    	
    	Subject subject = new Subject(false, jaasUserPrincipal.getSubject().getPrincipals(), jaasUserPrincipal.getSubject().getPublicCredentials(), jaasUserPrincipal.getSubject().getPrivateCredentials());
    	
    	Set<Principal> principals = new HashSet<Principal>();
    	
    	Iterator<Principal> it = subject.getPrincipals().iterator();
    	while(it.hasNext()){
    		Principal principal = it.next();
    		if(principal instanceof JAASGroup){
    			principals.add(getGroup(jaasUserPrincipal.getSubject()));
    		}
    		else if(!(principal instanceof JAASUserPrincipal)){    			
    			principals.add(principal);
    		}
    	}
    	
    	subject.getPrincipals().removeAll(subject.getPrincipals());
    	subject.getPrincipals().addAll(principals); 
    	//subject.getPublicCredentials().removeAll(subject.getPublicCredentials());
    	//subject.getPrivateCredentials().removeAll(subject.getPrivateCredentials());
    	LocalgisJAASUserPrincipal userPrincipal = new LocalgisJAASUserPrincipal(jaasUserPrincipal.getName(), subject, null);
    	
    	
        if (logger.isDebugEnabled())
        {
            logger.debug("doPost(HttpServletRequest, HttpServletResponse) - Nombre del user principal:"+ jaasUserPrincipal.getName()+" IP origen:"+request.getRemoteAddr());
        }
        /**Obtenemos la aplicación donde se ha hecho login **/
        String sIdApp = request.getParameter (EnviarSeguro.IdApp);
   
        /**Creamos la sesión**/
        //ISesion  sSesion= new Sesion(request.getSession().getId(), sIdApp, userPrincipal, userPrincipal.getRoles());
        //----NUEVO---->
        ISesion sSesion;
    	if(!SSOAuthManager.isSSOActive()){	
	        //sSesion= new Sesion(request.getSession().getId(), sIdApp, userPrincipal, userPrincipal.getRoles());
    		sSesion= new Sesion(request.getSession().getId(),sIdApp, userPrincipal, getGroup(jaasUserPrincipal.getSubject()));
            
    	}    
	    else{ 
	    	//sSesion= new Sesion(request.getSession().getId(), SSOAuthManager.SSO_NAME, userPrincipal, userPrincipal.getRoles());
	    	sSesion= new Sesion(request.getSession().getId(), SSOConstants.SSO_NAME, userPrincipal, getGroup(jaasUserPrincipal.getSubject()));
	    }
    	//--FIN NUEVO-->
        /**Obtenemos los datos del usuario almacenados en la base de datos a partir del nombre**/
        //Buscamos el identificador del user principal
        Vector<String> vParametros=new Vector<String>();

        //Codigo de Test. Se podria quitar
		String usuarioConsulta=jaasUserPrincipal.getName();
		String cadenaTest="_T_";
		if (jaasUserPrincipal.getName().contains(cadenaTest))
			usuarioConsulta=jaasUserPrincipal.getName().substring(0,jaasUserPrincipal.getName().indexOf("_T_"));
        
        vParametros.add(0,usuarioConsulta.toUpperCase());
        
        if(!jaasUserPrincipal.getName().equals(SSOConstants.SSO_USERNAME)){
	        CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("Select id, id_entidad from IUSERUSERHDR WHERE upper(NAME)=?",vParametros);
	        try
	        {
	          if (rQuery.getVector().size()>0)
	          {
	              sSesion.setIdUser((String)((Vector)rQuery.getVector().get(0)).get(0));
	              sSesion.setIdEntidad(((String)((Vector)rQuery.getVector().get(0)).get(1)=="")?"0":(String)((Vector)rQuery.getVector().get(0)).get(1));
	          }
	        }catch(Exception e){
	        	logger.error("Error al obtener la información del usuario "+jaasUserPrincipal.getName()+ " de la base de datos",e);
	        }
        }
        else{
        	sSesion.setIdUser("SSOADMIN");
            sSesion.setIdEntidad("0");
        }
        
        /**Los permisos del usuario son **/
//        if (logger.isDebugEnabled())
//        {
//        	Group grupo=userPrincipal.getRoles();
//        	for (Enumeration e=grupo.members();e.hasMoreElements();)
//        	{
//        		JAASRole sRole=(JAASRole)e.nextElement();
//                logger.debug("doPost(HttpServletRequest, HttpServletResponse) - PERMISOS: " + sRole.getName());
//            }
//        }

        return sSesion;

    }
    /**
     * Graba el inicio de sesion en la base de datos.
     * @param sSesion
     */
    private String grabarLogin(ISesion sSesion, HttpServletRequest request){
    	String sPassword=null;
    	try
        {
        	//grabamos la conexion en la lista de conexciones
    		//logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion "+sSesion.getIdApp());
            //COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),sSesion.getIdApp());
	    	//----NUEVO---->	
	    	if(!SSOAuthManager.isSSOActive()){
	    		logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion "+sSesion.getIdApp()+" IP:"+request.getRemoteAddr());
	    		sSesion.setIP(request.getRemoteAddr());
	            COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),sSesion.getIdApp(),request);           
	    	}
	    	else if(!sSesion.getUserPrincipal().getName().equals(SSOConstants.SSO_USERNAME)){
	    		logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion "+ request.getParameter (EnviarSeguro.IdApp));
	    		sSesion.setIP(request.getRemoteAddr());
	            COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),SSOConstants.SSO_NAME,request);
	            sPassword=COperacionesControl.getPassword(sSesion.getIdUser());
	            SaveSSOAppSession.saveSSOAppSession(sSesion.getIdSesion(), request.getParameter (EnviarSeguro.IdApp));
	    	} 
	    	//--FIN NUEVO-->
        }catch(Exception e)
        {
            logger.info("Ya esta grabado el login para sSesion: "+sSesion.getIdSesion()+ ", usuario:"+ sSesion.getIdUser()+ " aplicacion:"+sSesion.getIdApp());
        }
    	return sPassword;

    }

    /* ------------------------------------------------ */
    /** Create user for test
     * @exception javax.servlet.ServletException
     */
    public void init ()
        throws ServletException
    {


    }


    /* ------------------------------------------------ */
    /** Destroy servlet, drop tables.
     */
    public void destroy ()
    {
    }

    public List getMunicipiosEntidad(String idEntidad) {
    	List alList = new ArrayList();
	    Vector vParametros=new Vector();
	    vParametros.add(0,Integer.parseInt(idEntidad));
	    CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("select m.id,m.nombreoficial,m.srid from municipios m, entidades_municipios e where m.id = e.id_municipio and e.id_entidad=?",vParametros);
	    try
	    {
	    	if (rQuery.getVector().size()>0)
	    	{
	    		Enumeration enMunicipios = rQuery.getVector().elements();
	    		while (enMunicipios.hasMoreElements()){
	    			Vector vector = (Vector)enMunicipios.nextElement();
	    			Object[] datosMunicipio = vector.toArray();
	    			alList.add(datosMunicipio);
		        }
	      }
	    }catch(Exception e){e.getMessage();}
	    finally{
	    	return alList;
	    }
    }
    /**
     * Obtiene la lista de entidades cuando el ususario es 
     * superusuario
     * @return
     */
    public List getEntidades() {
    	List alList = new ArrayList();
	    Vector vParametros=new Vector();
	    CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("select * from entidad_supramunicipal order by nombreoficial",vParametros);
	    try
	    {
	    	if (rQuery.getVector().size()>0)
	    	{
	    		Enumeration enEntidades = rQuery.getVector().elements();
	    		while (enEntidades.hasMoreElements()){
	    			Vector vector = (Vector)enEntidades.nextElement();
	    			Object[] datosEntidades = vector.toArray();
	    			alList.add(datosEntidades);
		        }
	      }
	    }catch(Exception e){e.getMessage();}
	    finally{
	    	return alList;
	    }
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
