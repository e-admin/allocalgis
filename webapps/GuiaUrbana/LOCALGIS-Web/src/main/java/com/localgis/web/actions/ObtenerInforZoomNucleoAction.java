/**
 * ObtenerInforZoomNucleoAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.GeopistaNucleo;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;

public class ObtenerInforZoomNucleoAction extends Action {

    private static Log log = LogFactory.getLog(PrintMapAction.class);
    
    private StringBuffer cadenaDatos = new StringBuffer();
    //private static String SUCCESS_PAGE = "success";
    //private static String ERROR_PAGE = "error";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        
	    Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
	    
	    //Obtenemos los datos que tenemos que pintar
	    cadenaDatos =  getCadenaMunNucleos(idEntidad);             
     
        response.setContentType("text/html; charset=UTF-8");
		PrintWriter printWriter=response.getWriter();
		//se los pasamos al js
		printWriter.println(cadenaDatos);
     
       
        return null;
    }
    
    
	private StringBuffer getCadenaMunNucleos (Integer idEntidad) throws LocalgisDBException, LocalgisInitiationException, LocalgisConfigurationException{
		StringBuffer cadena=new StringBuffer();
		//List municipos=null;
		List nucleosMunicipio= null;
		String geomNucleo = "";
		 String geom_left= "";
	     String geom_bottom= "";
	     String geom_right= "";
	     String geom_top= "";

		LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
        List  municipios = localgisEntidadSupramunicipalManager.getMunicipiosByIdEntidad(idEntidad);
        
        Collections.sort(municipios);
        Iterator iterator = municipios.iterator();
         
       
        while (iterator.hasNext()) {
        
            GeopistaMunicipio municipio = (GeopistaMunicipio) iterator.next();
         //   cadena.append("<htm >");



         //   cadena.append("<head>");

        //    cadena.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        //    cadena.append("</head>");
        //    cadena.append("<body>");
			cadena.append("<div id=\"mostrar\">");
			cadena.append("<table>");
			cadena.append("<tr>");
			cadena.append("<td align=\"center\"><B>--&nbsp;&nbsp;"+ municipio.getNombreoficial().toUpperCase()+"&nbsp;&nbsp;--</B> </td>");		
			cadena.append("</tr> ");
			nucleosMunicipio = localgisEntidadSupramunicipalManager.getNucleosMunicipio(Integer.parseInt (municipio.getIdProvincia()+municipio.getIdIne()));
			
			Collections.sort(nucleosMunicipio);
			
			
			Iterator iteratorNuc = nucleosMunicipio.iterator();
			 while (iteratorNuc.hasNext()) {
				GeopistaNucleo nucleo = (GeopistaNucleo) iteratorNuc.next();	
				geomNucleo = nucleo.getGeometria();
				
				if(geomNucleo!= null && geomNucleo!=""){
					 String[] valor=geomNucleo.split(",");
					if(valor.length==4){
						 geom_right= valor[0].toString().substring(1,  valor[0].toString().length());
						 geom_top= valor[1].toString().substring(0,  valor[1].toString().length()-1);
						 geom_left= valor[2].toString().substring(1,  valor[2].toString().length());
						 geom_bottom= valor[3].toString().substring(0,  valor[3].toString().length()-1);
					 }					 			
				}							
				cadena.append("<tr>");
				cadena.append("<td align=\"left\">&nbsp;&nbsp;<a href=\"javascript:void(null);\" onclick=\"parent.map.zoomToExtent(new OpenLayers.Bounds("+geom_left+","+ geom_bottom+","+ geom_right+", "+geom_top+")); \">"+nucleo.getNombreoficial().toUpperCase()+"</a></td>");
				//cadena.append("<td align=\"left\">&nbsp;&nbsp;<a href=\"javascript:void(null);\" onclick=\"parent.map.zoomToExtent(new OpenLayers.Bounds("+geom_left+","+ geom_bottom+","+ geom_right+", "+geom_top+")); \">"+nucleo.getNombreoficial().toUpperCase()+"</a></td>");
				cadena.append("</tr>");
			 }		
			 
		 
			cadena.append("</table>");
			cadena.append("</div>");
			
			
        }
     //   cadena.append("</body>");
     //   cadena.append("</html>");
        return cadena;
       
				
	}
}
