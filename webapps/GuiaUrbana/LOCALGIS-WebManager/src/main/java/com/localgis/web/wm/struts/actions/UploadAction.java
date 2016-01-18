/**
 * UploadAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.wm.config.LocalgisWebManagerConfiguration;
import com.localgis.web.wm.dwr.CSS;
import com.localgis.web.wm.struts.forms.UploadForm;
import com.localgis.web.wm.util.LocalgisManagerBuilderSingleton;

public class UploadAction extends Action {

	private static final String SUCCESSCSS = "successCSS";
	private static final String SUCCESSLEGENG = "successLegend";
	private static final String SUCCESSCABECERA = "successCabecera";
	private static final String ERROR = "error";

	private Logger logger = Logger.getLogger(UploadAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("Entering action UploadAction...");
		UploadForm uploadForm = (UploadForm) form;
		FormFile formFile =uploadForm.getFile();
		
		if (formFile==null)
			formFile=uploadForm.getCabecera();
		
		if (formFile==null)
			formFile=uploadForm.getCabecera2();
		
		if (formFile != null) {
			String ext = request.getParameter("ext");
			if ("css".equals(ext)) {
				logger.debug("Uploading css file ...");
				String contentType = formFile.getContentType();
				logger.debug("Content-Type :" + contentType);
				if ("text/css".equals(contentType)) {
					String css = new String(formFile.getFileData());
					request.getSession().setAttribute("css",css);
					logger.debug("Saving " + formFile.getFileName());
					
					//Lo guardamos directamente, evitamos la previsualiacion
					//por la complicacion que tiene
					CSS cssSave=new CSS();
					cssSave.saveCSS(css,(Integer) request.getSession().getAttribute("idEntidad"));
					ActionForward actionForward = mapping.findForward(SUCCESSCSS);
					return actionForward;
				}
				else {
					logger.error("This file is no a valid css file");
					ActionMessages actionMessages = new ActionMessages();
					actionMessages.add("message", new ActionMessage("message.noValidFile","css"));
			        saveMessages(request, actionMessages);
			        ActionForward actionForward = mapping.findForward(ERROR);
			        return actionForward;
				
				}
			}else if("img".equals(ext)) {
				logger.debug("Uploading  image file ...");
				String contentType = formFile.getContentType();
				logger.debug("Content-Type :" + contentType);
				if (contentType.indexOf("image/")==0) {
					saveLegend(uploadForm, formFile,request.getSession());
					Double imageNumber = new Double(System.currentTimeMillis() * 256);
			        //BASE64Encoder encoder = new BASE64Encoder();
					//request.setAttribute("image",new String(encoder.encode(imageNumber.toString().getBytes())) );					
	                Base64 encoder=new Base64();
					request.setAttribute("image",new String(encoder.encode(imageNumber.toString().getBytes())) );
					ActionForward actionForward = mapping.findForward(SUCCESSLEGENG);
					return actionForward;
				}
				else {
					logger.error("This file is no a valid image file");
					ActionMessages actionMessages = new ActionMessages();
					actionMessages.add("message", new ActionMessage("message.noValidFile","de imagen"));
			        saveMessages(request, actionMessages);
			        ActionForward actionForward = mapping.findForward(ERROR);
			        return actionForward;				
				}
			}
			else if("cabecera".equals(ext) || ("cabecera2").equals(ext)) {
				logger.debug("Uploading  Cabecera file ...");
				String contentType = formFile.getContentType();
				logger.debug("Content-Type :" + contentType);
				if (contentType.indexOf("image/")==0) {
					saveCabecera(uploadForm, formFile,request.getSession());
					ActionForward actionForward = mapping.findForward(SUCCESSCABECERA);
					return actionForward;
				}
				else {
					logger.error("This file is no a valid image file");
					ActionMessages actionMessages = new ActionMessages();
					actionMessages.add("message", new ActionMessage("message.noValidFile","de imagen"));
			        saveMessages(request, actionMessages);
			        ActionForward actionForward = mapping.findForward(ERROR);
			        return actionForward;				
				}
			}
			ActionForward actionForward = mapping.findForward(ERROR);
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("message", new ActionMessage("message.uploadError"));
	        saveMessages(request, actionMessages);
			return actionForward;
		}
		else {
			ActionForward actionForward = mapping.findForward(ERROR);
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add("message", new ActionMessage("message.uploadError"));
	        saveMessages(request, actionMessages);
			return actionForward;
		}
	}
	

	private void saveLegend(UploadForm uploadForm,FormFile formFile, HttpSession httpSession) throws Exception {
		LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
		Integer layer = uploadForm.getLayer();
		Integer municipio = uploadForm.getMunicipio();
		
		if (municipio==null)
			municipio=(Integer)httpSession.getAttribute("idEntidad"); 
		
		String type = uploadForm.getType();
		Boolean mapPublic;
		if (type.equals("publico")) {
			mapPublic = new Boolean(true);
		} else {
			mapPublic = new Boolean(false);
		}
		LocalgisLegendKey legendKey = new LocalgisLegendKey();
		legendKey.setIdentidad(municipio);
		legendKey.setLayeridgeopista(layer);
		legendKey.setMappublic(mapPublic.booleanValue()?new Short((short)1):new Short((short)0));
		LocalgisLegend localgisLegend =localgisLayerManager.getLegend(legendKey);
		byte[] fileData = formFile.getFileData();
		if (localgisLegend == null) {
		    localgisLayerManager.addLegend(layer, municipio, mapPublic, fileData);
            localgisLegend = new LocalgisLegend();
            localgisLegend.setIdentidad(municipio);
            localgisLegend.setLayeridgeopista(layer);
            localgisLegend.setMappublic(mapPublic.booleanValue()?new Short((short)1):new Short((short)0));
            localgisLegend.setImg(fileData);
		}
		else {
		    localgisLayerManager.updateLegend(legendKey, fileData);
            localgisLegend.setImg(fileData);
		}
        httpSession.setAttribute("localgisLegend", localgisLegend);
	}
	
	/**
	 * Almacenamiento de la cabecera
	 * @param uploadForm
	 * @param formFile
	 * @param httpSession
	 * @throws Exception
	 */
	private void saveCabecera(UploadForm uploadForm,FormFile formFile, HttpSession httpSession) throws Exception {

        String pathEscudos=LocalgisWebManagerConfiguration.getPropertyString(LocalgisWebManagerConfiguration.PROPERTY_GUIAURBANA_PATH_ESCUDOS);

        pathEscudos+="/"+httpSession.getAttribute("idEntidad")+".png";
		byte[] fileData = formFile.getFileData();
		File file=new File(pathEscudos);
		FileOutputStream out=new FileOutputStream(file);
		out.write(fileData);
		out.close();

	}
		
}
