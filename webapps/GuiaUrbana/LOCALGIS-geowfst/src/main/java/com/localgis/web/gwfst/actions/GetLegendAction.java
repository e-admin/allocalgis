/**
 * GetLegendAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.gwfst.actionsforms.GetLegendActionForm;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Acción de recuperación de la legenda asociada
 */
public class GetLegendAction extends Action {

    /**
     * Logger
     */
	private static Log log = LogFactory.getLog(GetLegendAction.class);    
	
	/**
	 * Constantes
	 */    
    private static final String SUCCESS_PAGE = "success";
    private static final String ERROR_PAGE = "error";
    
	/**
	 * Recupera la leyenda asociada
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @throws Exception
	 * @return ActionForward: Devuelve la siguiente acción
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GetLegendActionForm formBean = (GetLegendActionForm)form;
        
        /*
         * Obtenemos el manager de localgis
         */
        LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
        
        String configurationLocalgisWebStr = (String)request.getAttribute("configurationLocalgisWeb");
        Boolean configurationLocalgisWeb = new Boolean(configurationLocalgisWebStr != null && (configurationLocalgisWebStr.equals("public")||configurationLocalgisWebStr.equals("incidencias")));

        if (formBean.getIdEntidad() == null || formBean.getIdEntidad().intValue() < 0 || formBean.getIdLayerGeopista() == null || formBean.getIdLayerGeopista().intValue() < 0) {
            log.error("Los parametros no son correctos. IdEntidad ["+formBean.getIdEntidad()+"], idLayerGeopista ["+formBean.getIdLayerGeopista()+"]");
        }

        LocalgisLegend localgisLegend;
        LocalgisLegendKey localgisLegendKey = new LocalgisLegendKey();
        localgisLegendKey.setIdentidad(formBean.getIdEntidad());
        localgisLegendKey.setLayeridgeopista(formBean.getIdLayerGeopista());
        localgisLegendKey.setMappublicBoolean(configurationLocalgisWeb);
        try {
            localgisLegend = localgisLayerManager.getLegend(localgisLegendKey);
        } catch (LocalgisDBException e) {
            log.error("Error al obtener la legenda para el mapa ["+configurationLocalgisWebStr+"], idLayerGeopista ["+formBean.getIdLayerGeopista()+"], idEntidad ["+formBean.getIdEntidad()+"]");
            return mapping.findForward(ERROR_PAGE);
        }
        if (localgisLegend != null) {
            request.setAttribute("localgisLegend", localgisLegend);
            return mapping.findForward(SUCCESS_PAGE);
        } else {
            return mapping.findForward(ERROR_PAGE);
        }
    }

}
