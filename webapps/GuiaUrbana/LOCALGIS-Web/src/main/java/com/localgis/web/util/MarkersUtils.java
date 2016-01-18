/**
 * MarkersUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.util;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.model.LocalgisMarker;

/**
 * Clase que proporiciona utilidades para las marcas de posición
 * @author albegarcia
 *
 */
public class MarkersUtils {

    private static String COOKIE_NAME = "POSITION_MARKER_MAP_";
    public static int COOKIE_EXPIRE_TIME = 365*24*60*60;
    
    private static Log logger = LogFactory.getLog(MarkersUtils.class);
    
    /**
     * Método para obtener la cookie asociada a un determinado mapa
     * @param request Request para obtener las cookies
     * @param idMap Identificador del mapa
     * @return La cookie asociada al mapa o null si no existe
     */
    public static Cookie getMarkerCookie(HttpServletRequest request, Integer idMap) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(COOKIE_NAME + idMap)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Método que crea una cookie para los marcadores de un mapa determinado
     * @param idMap Identificador del mapa
     * @return La cookie donde se almacenaran los marcadores
     */
    public static Cookie createMarkerCookie(Integer idMap) {
        Cookie cookie = new Cookie(COOKIE_NAME+idMap, "");
        cookie.setMaxAge(365*24*60*60);
        return cookie;
    }
    
    /**
     * Método que devuelve una listas de marcadores almacenados en una cookie
     * @param cookie Cookie donde estan almacenados los marcadores
     * @return Una lista de marcadores como objetos LocalgisMarker
     */
    public static List getMarkersFromCookie(Cookie cookie) {
        String value = cookie.getValue();
        List result = new ArrayList();
        if (value.trim().equals("")) {
            return result;
        }
        String name = cookie.getName();
        String idMap = name.substring(COOKIE_NAME.length());
        String[] markersStr = value.split(";");
        for (int i = 0; i < markersStr.length; i++) {
            String marker = markersStr[i];
            String[] fields = marker.split("&");
            if (fields.length >= 5) {
                LocalgisMarker localgisMarker = new LocalgisMarker();
                localgisMarker.setMapid(new Integer(idMap));
                localgisMarker.setMarkerid(new Integer(fields[0]));
                localgisMarker.setX(new Double(fields[1]));
                localgisMarker.setY(new Double(fields[2]));
                localgisMarker.setScale(new Double(fields[3]));
                localgisMarker.setMarkname(fields[4]);
                if (fields.length == 6) {
                    localgisMarker.setMarktext(fields[5]);
                } else {
                    localgisMarker.setMarktext("");
                }
                result.add(localgisMarker);
            } else {
                logger.error("El valor del marcador almacenado en la cookie es inválido. Valor = ["+markersStr[i]);
            }
        }
        return result;
    }

    /**
     * Control de validacion para parametro MarkText: Caracter alfanumerico: [a-Z,0-9, _]
     * No incluye ñ ni caracteres acenturados
     * @return
     */
    private static boolean validateMarkText (String markText) {
    	boolean isValid = false;
    	Pattern pat = Pattern.compile("\\w+");
    	Matcher mat = pat.matcher(markText);
    	if (mat.matches())
    		isValid = true;
    	return isValid;
    }
    
    /**
     * Método para salvar una marcador en una cookie
     * @param cookie Cookie donde se desea salvar el marcador
     * @param localgisMarker Marcador que se desea salvar en la cookie
     * @return El identificador del marcador añadido
     * @throws LocalgisInvalidParameterException Si ocurre algun error con los parametros
     */
    public static Integer saveMarkerInCookie(Cookie cookie, LocalgisMarker localgisMarker) throws LocalgisInvalidParameterException {
        String value = cookie.getValue();
        String name = cookie.getName();
        String idMap = name.substring(COOKIE_NAME.length());
       
        /*
         * Validacion informacion de entrada
         */
        if (!validateMarkText(localgisMarker.getMarktext())) 
            throw new LocalgisInvalidParameterException("Valor para enviado para MarkText no valido");
        
        /*
         * Comprobamos que el idMap que esta en el nombre de la cookie es el mismo que el idMap del marcador
         */
        if (!new Integer(idMap).equals(localgisMarker.getMapid())) {
            throw new LocalgisInvalidParameterException("El marcador no se puede almacenar en la cookie puesto que no coincide el identificador del mapa asociado.");
        }
        /*
         * Obtenemos el último marcador almacenado en la cookie para obtener el siguiente identificador de marcador, siempre que el valor de la cookie no sea vacio
         */
        int newMarkerId = 1;
        if (!value.trim().equals("")) {
            String[] markersStr = value.split(";");
            if (markersStr.length > 0) {
                String marker = markersStr[markersStr.length - 1];
                String[] fields = marker.split("&");
                newMarkerId = Integer.parseInt(fields[0]) + 1; 
            }
        }
        String newMarkerStr = newMarkerId + "&" + localgisMarker.getX() + "&" + localgisMarker.getY() + "&" + localgisMarker.getScale() + "&" +
                localgisMarker.getMarkname() + "&" + localgisMarker.getMarktext();
        if (value.trim().equals("")) {
            cookie.setValue(newMarkerStr);
        } else {
            cookie.setValue(value + ";" + newMarkerStr);
        }
        return new Integer(newMarkerId);
    }

    /**
     * Método para eliminar una marcador de una cookie
     * @param cookie Cookie donde se desea salvar el marcador
     * @param localgisMarker Marcador que se desea eliminar de la cookie
     * @throws LocalgisInvalidParameterException Si ocurre algun error con los parametros
     */
    public static void removeMarkerFromCookie(Cookie cookie, LocalgisMarker localgisMarker) throws LocalgisInvalidParameterException {
        String value = cookie.getValue();
        String name = cookie.getName();
        String idMap = name.substring(COOKIE_NAME.length());
        /*
         * Comprobamos que el idMap que esta en el nombre de la cookie es el mismo que el idMap del marcador
         */
        if (!new Integer(idMap).equals(localgisMarker.getMapid())) {
            throw new LocalgisInvalidParameterException("El marcador no se puede eliminar de la cookie puesto que no coincide el identificador del mapa asociado.");
        }
        String newValue = "";
        if (!value.trim().equals("")) {
            String[] markersStr = value.split(";");
            for (int i = 0; i < markersStr.length; i++) {
                String marker = markersStr[i];
                String[] fields = marker.split("&");
                if (fields.length == 6) {
                    Integer markerId = new Integer(fields[0]);
                    if (!markerId.equals(localgisMarker.getMarkerid())) {
                        if (!newValue.trim().equals("")) {
                            newValue += ";";
                        }
                        newValue += marker;
                    }
                } else {
                    logger.error("El valor del marcador almacenado en la cookie es inválido. Valor = ["+markersStr[i]);
                }
            }
        }
        cookie.setValue(newValue);
    }

    /**
     * Método para actualizar una marcador en una cookie
     * @param cookie Cookie donde se desea actualizar el marcador
     * @param localgisMarker Marcador que se desea actualizar en la cookie
     * @throws LocalgisInvalidParameterException Si ocurre algun error con los parametros
     */
    public static void updateMarkerInCookie(Cookie cookie, LocalgisMarker localgisMarker) throws LocalgisInvalidParameterException {
        String value = cookie.getValue();
        String name = cookie.getName();
        String idMap = name.substring(COOKIE_NAME.length());
        /*
         * Comprobamos que el idMap que esta en el nombre de la cookie es el mismo que el idMap del marcador
         */
        if (!new Integer(idMap).equals(localgisMarker.getMapid())) {
            throw new LocalgisInvalidParameterException("El marcador no se puede actualizar en la cookie puesto que no coincide el identificador del mapa asociado.");
        }
        String newValue = "";
        if (!value.trim().equals("")) {
            String[] markersStr = value.split(";");
            for (int i = 0; i < markersStr.length; i++) {
                String marker = markersStr[i];
                String[] fields = marker.split("&");
                if (fields.length == 6) {
                    if (!newValue.trim().equals("")) {
                        newValue += ";";
                    }
                    Integer markerId = new Integer(fields[0]);
                    if (!markerId.equals(localgisMarker.getMarkerid())) {
                        newValue += marker;
                    } else {
                        String updatedMarkerStr = markerId + "&" + fields[1] + "&" + fields[2] + "&" + fields[3] + "&" +
                        localgisMarker.getMarkname() + "&" + localgisMarker.getMarktext();
                        newValue += updatedMarkerStr;
                    }
                } else {
                    logger.error("El valor del marcador almacenado en la cookie es inválido. Valor = ["+markersStr[i]);
                }
            }
        }
        cookie.setValue(newValue);
    }

}
