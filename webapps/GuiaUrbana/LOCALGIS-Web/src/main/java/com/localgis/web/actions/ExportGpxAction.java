/**
 * ExportGpxAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.ExportGpxActionForm;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.util.LayerUtils;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.satec.gpx.GpxGenerator;

public class ExportGpxAction extends Action {
	
	private static Log log = LogFactory.getLog(ExportGpxAction.class);
    @SuppressWarnings("unused")
	private static String ERROR_PAGE = "error";
    private static final String[] GEOMETRIAS_CAPA = {"gml:LineStringPropertyType", "gml:MultiLineStringPropertyType"};
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportGpxActionForm formBean = (ExportGpxActionForm)form;
        String idCapa = request.getParameter("idlayer");
        Integer idMapa = formBean.getIdMap();
        
        String[] datosCapa;
        ArrayList<String> listaGeometrias = new ArrayList<String>();
        
        //Obtener todas las capas del mapa
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();
        @SuppressWarnings("unchecked")
		List<LocalgisLayerExt> listaCapas = localgisMapManager.getMapLayers(idMapa);
        for(int i = 0; i < listaCapas.size(); i++) {
        	//Verificamos las que tengan geometría "LINESTRING" o "MULTILINESTRING"
        	if(StringUtils.lastIndexOfAny(listaCapas.get(i).getGeometrytype(), GEOMETRIAS_CAPA) != -1) {
        		//...y que coincida su nombre con el id que tenemos
        		if(listaCapas.get(i).getLayerid()==Integer.parseInt(idCapa)) {
	        		datosCapa = LayerUtils.getTableNameFromLayer(listaCapas.get(i));
	        		log.debug("idCapa=" + idCapa + ", tabla asociada=" + datosCapa[0] + ", municipio=" + datosCapa[1]);
	        		listaGeometrias = (ArrayList<String>) localgisMapManager.getGeometryFromLayer(datosCapa[0], datosCapa[1]);
        		}
        	}
        }
        
        //Generar objeto GpxType como ByteArray
        GpxGenerator gpxGen = new GpxGenerator();
		ByteArrayOutputStream gpxOut = gpxGen.generateGpxOutputStream(listaGeometrias);
		                
		//Escribir
        response.setContentLength(gpxOut.size());
        response.setContentType("application/x-file-download");
        String filename = idCapa + ".gpx";
        filename = filename.replace(" ", "_");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        
        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(gpxOut.toByteArray());
        outStream.flush();
        
        return null;
    }
    
      
}
