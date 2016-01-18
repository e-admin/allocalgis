package com.geopista.server.control.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.security.auth.Subject;
import javax.swing.JOptionPane;

import java.io.IOException;
import java.io.Writer;
import java.security.*;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.MunicipalityOperations;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
//import com.satec.geopista.server.control.aplicacion.JAASRole;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.server.administrador.web.SaveSSOAppSession;
import com.geopista.server.database.COperacionesControl;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;
import admcarApp.PasarelaAdmcar;

import org.mortbay.http.HttpRequest;
import org.mortbay.jaas.JAASUserPrincipal;
import org.mortbay.jaas.JAASRole;
import com.geopista.app.catastro.model.beans.Municipio;

import edu.emory.mathcs.backport.java.util.Collections;


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

    //private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Login.class);

    public void doPost (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	try {
			/**Obtenemos la sesion**/
			ISesion sSesion = getSesion(request);
			/**Grabamos el Login**/
			grabarLogin(sSesion, request);
			/**Almacenamos la sesión en Memoria**/
			PasarelaAdmcar.listaSesiones.add(sSesion);
			
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
			
			
			
			//*****************************
			//Ordenacion de los municipios
			//*****************************
			Collections.sort(listMunicipios, new java.util.Comparator(){    		 
			    public int compare(Object o1, Object o2) {
			    	com.geopista.app.catastro.model.beans.Municipio p1 = (com.geopista.app.catastro.model.beans.Municipio) o1;
			    	com.geopista.app.catastro.model.beans.Municipio p2 = (com.geopista.app.catastro.model.beans.Municipio) o2;
			       return p1.getNombreOficial().compareToIgnoreCase(p2.getNombreOficial());
			    }
 
			});
			
			sSesion.setAlMunicipios(listMunicipios);
			

			CResultadoOperacion resultado = new CResultadoOperacion(true, sSesion.getIdSesion());
			Vector vector = new Vector();
			vector.addElement(sSesion.getIdSesion());        
			vector.addElement(idEntidad);
			//vector.addElement(sIdMunicipio);
			vector.addElement(lista);
			vector.addElement(listaEntidades);
			resultado.setVector(vector);
			response.setContentType ("text/html");
			Writer writer = response.getWriter();
			writer.write (resultado.buildResponse());
			writer.flush();
			writer.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
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
        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();

        if (logger.isDebugEnabled())
        {
            logger.debug("doPost(HttpServletRequest, HttpServletResponse) - Nombre del user principal:"+ userPrincipal.getName());
        }
        /**Obtenemos la aplicación donde se ha hecho login **/
        String sIdApp = request.getParameter (EnviarSeguro.IdApp);
   
        /**Creamos la sesión**/
        //ISesion  sSesion= new Sesion(request.getSession().getId(), sIdApp, userPrincipal, userPrincipal.getRoles());
        //----NUEVO---->
        ISesion sSesion;
    	if(!SSOAuthManager.isSSOActive()){	
	        sSesion= new Sesion(request.getSession().getId(), sIdApp, userPrincipal, userPrincipal.getRoles());
    	}    
	    else{ 
	    	sSesion= new Sesion(request.getSession().getId(), SSOAuthManager.SSO_NAME, userPrincipal, userPrincipal.getRoles());
	    }
    	//--FIN NUEVO-->
        /**Obtenemos los datos del usuario almacenados en la base de datos a partir del nombre**/
        //Buscamos el identificador del user principal
        Vector<String> vParametros=new Vector<String>();

        //Codigo de Test. Se podria quitar
		String usuarioConsulta=userPrincipal.getName();
		String cadenaTest="_T_";
		if (userPrincipal.getName().contains(cadenaTest))
			usuarioConsulta=userPrincipal.getName().substring(0,userPrincipal.getName().indexOf("_T_"));
        
        vParametros.add(0,usuarioConsulta.toUpperCase());
        
        CResultadoOperacion rQuery=COperacionesControl.ejecutarQuery("Select id, id_entidad from IUSERUSERHDR WHERE upper(NAME)=?",vParametros);
        try
        {
          if (rQuery.getVector().size()>0)
          {
              sSesion.setIdUser((String)((Vector)rQuery.getVector().get(0)).get(0));
              sSesion.setIdEntidad(((String)((Vector)rQuery.getVector().get(0)).get(1)=="")?"0":(String)((Vector)rQuery.getVector().get(0)).get(1));
          }
        }catch(Exception e){
        	logger.error("Error al obtener la información del usuario "+userPrincipal.getName()+ " de la base de datos",e);
        }
        
        /**Los permisos del usuario son **/
        if (logger.isDebugEnabled())
        {
        	Group grupo=userPrincipal.getRoles();
        	for (Enumeration e=grupo.members();e.hasMoreElements();)
        	{
        		JAASRole sRole=(JAASRole)e.nextElement();
                logger.debug("doPost(HttpServletRequest, HttpServletResponse) - PERMISOS: " + sRole.getName());
            }
        }

        return sSesion;

    }
    /**
     * Graba el inicio de sesion en la base de datos.
     * @param sSesion
     */
    private void grabarLogin(ISesion sSesion, HttpServletRequest request){
    	try
        {
        	//grabamos la conexion en la lista de conexciones
    		//logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion "+sSesion.getIdApp());
            //COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),sSesion.getIdApp());
	    	//----NUEVO---->	
	    	if(!SSOAuthManager.isSSOActive()){
	    		logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion "+sSesion.getIdApp());
	            COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),sSesion.getIdApp());           
	    	}
	    	else if(!sSesion.getUserPrincipal().getName().equals("SSOADMIN")){
	    		logger.info("Grabamos el inicio de sesion "+sSesion.getIdSesion()+" para el usuario "+sSesion.getIdUser()+" en la aplicacion SSO");
	            COperacionesControl.grabarLogin(sSesion.getIdSesion(), sSesion.getIdUser(),SSOAuthManager.SSO_NAME);
	            SaveSSOAppSession.saveSSOAppSession(sSesion.getIdSesion(), request.getParameter (EnviarSeguro.IdApp));
	    	} 
	    	//--FIN NUEVO-->
        }catch(Exception e)
        {
            logger.info("Ya esta grabado el login para sSesion: "+sSesion.getIdSesion()+ ", usuario:"+ sSesion.getIdUser()+ " aplicacion:"+sSesion.getIdApp());
        }

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
	    vParametros.add(0,Integer.parseInt(idEntidad, 10));
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
    

}
