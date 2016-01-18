/**
 * GestionMapaAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * GestionMapaAction.java
 *
 * Created on 8 de octubre de 2007, 15:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.localgis.web.wm.struts.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.wm.util.MapComparator;


/**
 *
 * @author arubio
 */
public class GestionMapaAction extends Action {
    
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    
    
    
    private static final String TYPE_PUBLIC = "publico";
    private static final String TYPE_PRIVATE = "privado";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
		if (idEntidad == null) {
			ActionForward actionForward = mapping.findForward("entidadNoSelected");
			return actionForward;
		}
    	String type = (String) request.getParameter("type");
        Hashtable maps = new Hashtable();
        boolean publicMap = false;
        HttpSession session = request.getSession();

        if ((type!=null)&&(type.equals(TYPE_PUBLIC))) {
           publicMap = true;
       		session.setAttribute("tipoMapas", "Mapas públicos");
        }
        else if ((type!=null)&&(type.equals(TYPE_PRIVATE))){
           publicMap = false;
      		session.setAttribute("tipoMapas", "Mapas privados");

        }
        else {
			String message = "El tipo de mapa seleccionado no es correcto";
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("error", new ActionMessage("errors.detail",message));
			saveMessages(request, actionMessages);
			ActionForward actionForward = mapping.findForward(ERROR);
			return actionForward;
        }
        //try {
			try {
				maps = getMaps(idEntidad,publicMap);
			} catch (LocalgisConfigurationException e) {
				e.printStackTrace();
				String message = "No se pudo obtener los mapas disponibles para este municipio, debido a un problema de configuración";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				ActionForward actionForward = mapping.findForward(ERROR);
				return actionForward;
			} catch (LocalgisInitiationException e) {
				e.printStackTrace();
				String message = "No se pudo obtener los mapas disponibles para este municipio, debido a un problema de inicialización";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				ActionForward actionForward = mapping.findForward(ERROR);
				return actionForward;
			} catch (LocalgisInvalidParameterException e) {
				e.printStackTrace();
				String message = "No se pudo obtener los mapas disponibles para este municipio, debido a recepción de un parámetro inválido";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				ActionForward actionForward = mapping.findForward(ERROR);
				return actionForward;
			} catch (LocalgisDBException e) {
				e.printStackTrace();
				String message = "No se pudo obtener los mapas disponibles para este municipio, debido a un problema de acceso a datos";
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("error", new ActionMessage("errors.detail",message));
				saveMessages(request, actionMessages);
				ActionForward actionForward = mapping.findForward(ERROR);
				return actionForward;
			}
        request.setAttribute("availableMaps",maps.get("availableMaps"));
        request.setAttribute("publishedMaps",maps.get("publishedMaps"));
        
        if (request.getSession().getAttribute("publicadorglobal")!=null)        	
        	request.setAttribute("publicadorglobal","true");
        
        request.setAttribute("type",type);
       
       /* try {
			System.out.println(AutoPublish.getMapTypeRelation(1,"LGS_vados"));
		} catch (LocalgisConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalgisInitiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalgisInvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalgisDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocalgisWMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        ActionForward actionForward = mapping.findForward(SUCCESS);
        return actionForward;
    }

    private Hashtable getMaps(Integer idEntidad,boolean type) throws LocalgisConfigurationException, LocalgisInitiationException, LocalgisInvalidParameterException, LocalgisDBException {
        Hashtable hashtable = new Hashtable();
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
        List geopistaAvailableMapsList = localgisMapsConfigurationManager.getAvailableMaps(idEntidad);            
        List localgisPublishedMapsList = localgisMapsConfigurationManager.getPublishedMaps(idEntidad,new Boolean(type));
        ArrayList toRemove = new ArrayList();
        for (Iterator iteratorAvailable = geopistaAvailableMapsList.iterator(); iteratorAvailable.hasNext();) {
        	GeopistaMap geopistaMap = (GeopistaMap) iteratorAvailable.next();
			if(geopistaMap.getName() == null) {
				geopistaMap.setName("Nombre Desconocido " + "ID " + geopistaMap.getIdMap());
			}
        	for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
        		LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
        		if (geopistaMap.getIdMap().equals(localgisMap.getMapidgeopista())){

        			toRemove.add(geopistaMap);
        		}else {
            		if (localgisMap.getName() == null) {
            			localgisMap.setName("Nombre Desconocido " + "ID " + localgisMap.getMapid());
        			}
        		}
        	}
        }				
        geopistaAvailableMapsList.removeAll(toRemove);
        MapComparator mapComparator = new MapComparator();
        Collections.sort(geopistaAvailableMapsList, mapComparator);
        Collections.sort(localgisPublishedMapsList, mapComparator);
        hashtable.put("availableMaps",geopistaAvailableMapsList);
        hashtable.put("publishedMaps",localgisPublishedMapsList);

        return hashtable;
    }    
   
}
