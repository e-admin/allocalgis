/**
 * MarkerService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.dwr;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.model.LocalgisMarker;
import com.localgis.web.gwfst.filters.LoginFilterFeatures;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.gwfst.util.MarkersUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Servicio para marcadores
 */
public class MarkerService {

	/**
	 * Logger
	 */
    private static Log logger = LogFactory.getLog(MarkerService.class);
    
    /**
     * Método para añadir un marcador
     * @param mapid
     * @param markname
     * @param marktext
     * @param x
     * @param y
     * @param scale
     * @return
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    public Integer addMarker(Integer mapid, String markname, String marktext, Double x, Double y, Double scale) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            return addMarkerPublic(request, mapid, markname, marktext, x, y, scale);
        } else {
            return addMarkerPrivate(request, mapid, markname, marktext, x, y, scale);
        }
    }
    
    /**
     * Método para actualizar un marcador
     * @param markerid
     * @param mapid
     * @param markname
     * @param marktext
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    public void updateMarker(Integer markerid, Integer mapid, String markname, String marktext) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            updateMarkerPublic(request, markerid, mapid, markname, marktext);
        } else {
            updateMarkerPrivate(request, markerid, mapid, markname, marktext);
        }
    }

    /**
     * Método para eliminar un marcador
     * @param mapid
     * @param markerid
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    public void deleteMarker(Integer mapid, Integer markerid) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            deleteMarkerPublic(request, mapid, markerid);
        } else {
            deleteMarkerPrivate(request, mapid, markerid);
        }
    }

    /**
     * Método para añadir un marcador publico
     * @param request
     * @param mapid
     * @param markname
     * @param marktext
     * @param x
     * @param y
     * @param scale
     * @return
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private Integer addMarkerPublic(HttpServletRequest request, Integer mapid, String markname, String marktext, Double x, Double y, Double scale) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Creando marca de posicion publica en una cookie");
        HttpServletResponse response = WebContextFactory.get().getHttpServletResponse();
        Cookie cookie = MarkersUtils.getMarkerCookie(request, mapid);
        if (cookie == null) {
            cookie = MarkersUtils.createMarkerCookie(mapid);
        }
        
        LocalgisMarker localgisMarker = new LocalgisMarker();
        localgisMarker.setMapid(mapid);
        localgisMarker.setX(x);
        localgisMarker.setY(y);
        localgisMarker.setScale(scale);
        localgisMarker.setMarkname(markname);
        localgisMarker.setMarktext(marktext);
        Integer newMarkerId = MarkersUtils.saveMarkerInCookie(cookie, localgisMarker);
        returnCookie(response, request, cookie);
        return newMarkerId;
    }

    /**
     * Método para añadir un marcador privado
     * @param request
     * @param mapid
     * @param markname
     * @param marktext
     * @param x
     * @param y
     * @param scale
     * @return
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private Integer addMarkerPrivate(HttpServletRequest request, Integer mapid, String markname, String marktext, Double x, Double y, Double scale) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Creando marca de posicion para el usuario "+request.getRemoteUser());
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        String username = (String)request.getSession().getAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE);
        return localgisMapManager.addMarker(mapid, username, x, y, scale, markname, marktext);
    }

    /**
     * Método para eliminar un marcador publico
     * @param request
     * @param mapid
     * @param markerid
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private void deleteMarkerPublic(HttpServletRequest request, Integer mapid, Integer markerid) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Eliminando marca de posicion publica en una cookie");
        HttpServletResponse response = WebContextFactory.get().getHttpServletResponse();
        Cookie cookie = MarkersUtils.getMarkerCookie(request, mapid);
        if (cookie == null) {
            cookie = MarkersUtils.createMarkerCookie(mapid);
        }
        
        LocalgisMarker localgisMarker = new LocalgisMarker();
        localgisMarker.setMapid(mapid);
        localgisMarker.setMarkerid(markerid);
        MarkersUtils.removeMarkerFromCookie(cookie, localgisMarker);
        returnCookie(response, request, cookie);
    }

    /**
     * Método para eliminar un marcador privado
     * @param request
     * @param mapid
     * @param markerid
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private void deleteMarkerPrivate(HttpServletRequest request, Integer mapid, Integer markerid) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Eliminando marca de posicion "+markerid+" para el usuario "+request.getRemoteUser());
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        String username = (String)request.getSession().getAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE);
        localgisMapManager.deleteMarker(markerid, username);
    }

    /**
     * Método para actualizar un marcador publico
     * @param request
     * @param markerid
     * @param mapid
     * @param markname
     * @param marktext
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private void updateMarkerPublic(HttpServletRequest request, Integer markerid, Integer mapid, String markname, String marktext) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Eliminando marca de posicion publica en una cookie");
        HttpServletResponse response = WebContextFactory.get().getHttpServletResponse();
        Cookie cookie = MarkersUtils.getMarkerCookie(request, mapid);
        if (cookie == null) {
            cookie = MarkersUtils.createMarkerCookie(mapid);
        }
        
        LocalgisMarker localgisMarker = new LocalgisMarker();
        localgisMarker.setMapid(mapid);
        localgisMarker.setMarkerid(markerid);
        localgisMarker.setMarkname(markname);
        localgisMarker.setMarktext(marktext);
        MarkersUtils.updateMarkerInCookie(cookie, localgisMarker);
        returnCookie(response, request, cookie);
    }

    /**
     * Método para actualizar un marcador privado
     * @param request
     * @param markerid
     * @param mapid
     * @param markname
     * @param marktext
     * @throws LocalgisInitiationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInvalidParameterException
     * @throws LocalgisDBException
     */
    private void updateMarkerPrivate(HttpServletRequest request, Integer markerid, Integer mapid, String markname, String marktext) throws LocalgisInitiationException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        logger.debug("Modificando marca de posicion "+markerid+" para el usuario "+request.getRemoteUser());
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        String username = (String)request.getSession().getAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE);
        localgisMapManager.updateMarker(markerid, username, markname, marktext);
    }

    /**
     * Método para devolver la cookie al navegador
     * @param response
     * @param request
     * @param cookie
     */
    private void returnCookie(HttpServletResponse response, HttpServletRequest request, Cookie cookie) {
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(MarkersUtils.COOKIE_EXPIRE_TIME);
        response.addCookie(cookie);
    }
    
}
