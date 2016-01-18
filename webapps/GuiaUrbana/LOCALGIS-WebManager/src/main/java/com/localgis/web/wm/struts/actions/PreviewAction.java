/**
 * PreviewAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



public class PreviewAction extends Action {

	private Logger logger = Logger.getLogger(PreviewAction.class);
	
	private static final String SUCCESS = "success";
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Entering action PreviewAction...");
    	String css = (String)request.getSession().getAttribute("css"); 
    	if (css!=null) {
    		request.setAttribute("css", css);
    		ActionForward actionForward = mapping.findForward(SUCCESS);
    		logger.debug("Exit PreviewAction ... OK");
    		return actionForward;
    	}
    	logger.error("No css in session context");
    	return null;
    }

	
}
