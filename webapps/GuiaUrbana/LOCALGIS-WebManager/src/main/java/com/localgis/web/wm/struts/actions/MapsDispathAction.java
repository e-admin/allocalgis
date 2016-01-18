/**
 * MapsDispathAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.wm.util.MapComparator;

public class MapsDispathAction extends DispatchAction {
	
    private static final boolean PUBLIC = true;
    private static final boolean PRIVATE = false;
    
    private static final String TYPE_PUBLIC = "publico";
    private static final String TYPE_PRIVATE = "privado";
    
    public ActionForward listMaps(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
    	String type = (String) request.getParameter("type");
    	Hashtable maps;
        HttpSession session = request.getSession();

    	if (type.equals(TYPE_PUBLIC)) {
    		maps = getMaps(idEntidad,PUBLIC);
    		request.setAttribute("availableMaps",maps.get("availableMaps"));
    		request.setAttribute("publishedMaps",maps.get("publishedMaps"));
       		session.setAttribute("tipoMapas", "Mapas públicos");
    	}
    	else if (type.equals(TYPE_PRIVATE)){
    		maps = getMaps(idEntidad,PRIVATE);
    		request.setAttribute("availableMaps",maps.get("availableMaps"));
    		request.setAttribute("publishedMaps",maps.get("publishedMaps"));
      		session.setAttribute("tipoMapas", "Mapas privados");

    	}
    	request.setAttribute("type",type);
    	ActionForward actionForward = mapping.findForward("success");
    	return actionForward;
    }

    private Hashtable getMaps(Integer idEntidad,boolean type) {
    	Hashtable hashtable = new Hashtable();
    	try {
    		LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
    		List geopistaAvailableMapsList = localgisMapsConfigurationManager.getAvailableMaps(idEntidad);            
    		List localgisPublishedMapsList = localgisMapsConfigurationManager.getPublishedMaps(idEntidad,new Boolean(type));
    		ArrayList toRemove = new ArrayList();
    		for (Iterator iteratorAvailable = geopistaAvailableMapsList.iterator(); iteratorAvailable.hasNext();) {
    			GeopistaMap geopistaMap = (GeopistaMap) iteratorAvailable.next();
    			for (Iterator iteratorPublished = localgisPublishedMapsList.iterator(); iteratorPublished.hasNext();) {
    				LocalgisMap localgisMap = (LocalgisMap) iteratorPublished.next();
    				if (geopistaMap.getIdMap().equals(localgisMap.getMapidgeopista())){
    					toRemove.add(geopistaMap);
    				}

    			}
    		}				
    		geopistaAvailableMapsList.removeAll(toRemove);
    		MapComparator mapComparator = new MapComparator();
    		Collections.sort(geopistaAvailableMapsList, mapComparator);
    		Collections.sort(localgisPublishedMapsList, mapComparator);
    		hashtable.put("availableMaps",geopistaAvailableMapsList);
    		hashtable.put("publishedMaps",localgisPublishedMapsList);
    	} catch (LocalgisDBException ex) {
    		ex.printStackTrace();
    	} catch (LocalgisInvalidParameterException ex) {
    		ex.printStackTrace();
    	} catch (LocalgisConfigurationException e) {
    		e.printStackTrace();
    	} catch (LocalgisInitiationException e) {
    		e.printStackTrace();
    	}
    	return hashtable;
    }
}
