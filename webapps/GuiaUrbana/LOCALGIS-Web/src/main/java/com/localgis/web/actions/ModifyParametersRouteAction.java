/**
 * ModifyParametersRouteAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.core.config.Configuration;
import com.localgis.web.util.EncodeUtil;

//import edu.umn.gis.mapscript.mapObj;

public class ModifyParametersRouteAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    {
        try
        { 
        	
        	String urlFinal = Configuration.getPropertyString(Configuration.PROPERTY_URL_WPS_RUTAS);
        	urlFinal = urlFinal.substring(0,urlFinal.lastIndexOf("/"));
        	urlFinal = urlFinal+"/ModifyParameters";
        	String parametro = request.getQueryString();
        	    
        	if((urlFinal != null)&&(!urlFinal.equals("")))
        	{
        		
        		EncodeUtil encode_url = new EncodeUtil();
        	
        		urlFinal = encode_url.encodeURl(urlFinal);
        	}
        	
        	if((parametro!=null)&&(!parametro.equals(""))&&(!parametro.equals("null")))
        	{
        		PostMethod method = new PostMethod(urlFinal);

        		String[] tokensList = parametro.split("&");
        		int n = tokensList.length;
        		for (int i=0;i<n;i++){
        			String[] values = tokensList[i].split("=");
        			if (values.length == 2)
        				method.setParameter(values[0],values[1]);
        			else
        				method.setParameter(values[0],"");
        		}
        		
        		HttpClient client = new HttpClient();

                int statusCode = client.executeMethod(method);
                
                
                System.out.println("statusCode "+statusCode);
                if (statusCode == 200)
                {
                	
                }
        	}
        	return mapping.findForward("exito");
        }catch(Exception e){
        	e.printStackTrace();
        	return mapping.findForward("error");
        }
    }
}



