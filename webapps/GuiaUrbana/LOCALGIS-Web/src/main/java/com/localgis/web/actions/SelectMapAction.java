/**
 * SelectMapAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.SelectMapActionForm;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class SelectMapAction extends Action {

    private static Log log = LogFactory.getLog(SelectMapAction.class);
    
    private static String SUCCESS_PAGE = "success";
    private static String ERROR_PAGE = "error";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SelectMapActionForm formBean = (SelectMapActionForm)form;
        
        /* Validamos el id del municipio */
        if (formBean.getIdEntidad() == null || formBean.getIdEntidad().intValue() <= 0) {
            log.error("El municipio seleccionado no es correcto");
            request.setAttribute("errorMessageKey", "selectMap.error.entidadNotValid");
            return mapping.findForward(ERROR_PAGE);
        }

        /*
         * Obtenemos el manager de localgis
         */
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
        
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        boolean publicMaps;
        if ((configurationLocalgisWeb.equals("public"))||(configurationLocalgisWeb.equals("incidencias"))) {
            log.debug("Obteniendo los mapas publicos para el municipio ["+formBean.getIdEntidad()+"]");
            publicMaps = true;
            request.getSession().setAttribute("tipoVisor", "Visor público");
        } else if (configurationLocalgisWeb.equals("private")) {
            log.debug("Obteniendo los mapas privados para el municipio/entidad ["+formBean.getIdEntidad()+"]");
            publicMaps = false;
            request.getSession().setAttribute("tipoVisor", "Visor privado");
        } else {
            log.debug("El parámetro de configuracion \"configurationLocalgisWeb\" no esta bien definido");
            request.setAttribute("errorMessageKey", "selectMap.error.badconfiguration");
            request.getSession().setAttribute("tipoVisor", "Visor público");
            return mapping.findForward(ERROR_PAGE);
            
        }
        List maps = localgisMapsConfigurationManager.getPublishedMaps(formBean.getIdEntidad(), new Boolean(publicMaps));
        request.setAttribute("maps", maps);
        request.setAttribute("idEntidad", formBean.getIdEntidad());
        
        
        //*******************************
        //Estilos por defecto de entidad
        //*******************************
        LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();
        LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();

        //Obtenemos la hoja de estilos, si la hubiera
        String customCSS = localgisEntidadSupramunicipalManager.getCSS(formBean.getIdEntidad());

        if (customCSS != null) {
        	//Quitamos las dobles comillas porque da problemas al parsear los estilos
        	customCSS=customCSS.replaceAll("\"","");
            request.setAttribute("customCSS", customCSS);
        }
        
        //Obtenemos el municipio
        GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(formBean.getIdEntidad());
        request.setAttribute("nombreEntidad", geopistaEntidad.getNombreoficial());
        
        return mapping.findForward(SUCCESS_PAGE);
    }

}
