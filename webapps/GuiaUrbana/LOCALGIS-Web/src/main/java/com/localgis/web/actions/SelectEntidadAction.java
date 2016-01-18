/**
 * SelectEntidadAction.java
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

import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class SelectEntidadAction extends Action {

    private static Log log = LogFactory.getLog(SelectEntidadAction.class);
    
    private static String SUCCESS_PAGE = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*
         * Obtenemos el manager de localgis
         */
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapsConfigurationManager();
        
        List entidades = localgisMapsConfigurationManager.getEntidadesSupramunicipales();
        log.debug("Entidades obtenidos correctamente");
        request.setAttribute("entidades", entidades);

        
        
        
        return mapping.findForward(SUCCESS_PAGE);
    }

}
