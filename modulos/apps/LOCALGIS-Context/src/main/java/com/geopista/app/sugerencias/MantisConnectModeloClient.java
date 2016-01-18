/**
 * MantisConnectModeloClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.sugerencias;


import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import biz.futureware.mantisconnect.IssueData;
import biz.futureware.mantisconnect.IssueNoteData;
import biz.futureware.mantisconnect.MantisConnectLocator;
import biz.futureware.mantisconnect.MantisConnectPortType;
import biz.futureware.mantisconnect.ObjectRef;
import biz.futureware.mantisconnect.ProjectData;

import com.geopista.app.AppContext;
import com.geopista.app.mantis.MantisConstants;

public class MantisConnectModeloClient {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MantisConnectModeloClient.class);
		
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext(); 
	
	private static String MANTIS_DEFAULTUSER="cau";
	private static String MANTIS_DEFAULTPASS="caumodelo";

	
	public static void createIssue(Sugerencia sugerencia) throws Exception{
		
		String username = null;
		if (aplicacion.getString(MantisConstants.USER_MANTIS) == null)
			username = MantisConstants.DEFAULT_USER_MANTIS;
		else
			username = aplicacion.getString(MantisConstants.USER_MANTIS);

		String password = null;
		if (aplicacion.getString(MantisConstants.PASSWORD_MANTIS) == null)
			password = MantisConstants.DEFAULT_PASSWORD_MANTIS;
		else
			password = aplicacion.getString(MantisConstants.PASSWORD_MANTIS);

		String url = null;
		if (aplicacion.getString(MantisConstants.URL_MANTIS) == null)
			url = MantisConstants.DEFAULT_URL_MANTIS;
		else
			url = aplicacion.getString(MantisConstants.URL_MANTIS);

		url=url+Constantes.MANTIS_URL_API;
		
		if (sugerencia.getUsuario()!=null) {
			username=sugerencia.getUsuario();
			password=sugerencia.getPassword();
		}
		
		
		try{	
			MantisConnectLocator locator=new MantisConnectLocator();
			//logger.info("Conectando a "+MANTISURL+" con usuario"+username +"/"+password);
			logger.info("Conectando a "+url+" con usuario:"+username);
			MantisConnectPortType mc=locator.getMantisConnectPort(new URL(url));
			logger.info("Conexion Mantis OK");
			//ProjectData[] projects=mc.mc_projects_get_user_accessible("administrator", "root");
			logger.debug("Listando proyectos disponibles...");
			ProjectData[] projects=mc.mc_projects_get_user_accessible(username, password);
			for (ProjectData p:projects) {
				logger.info("Proyecto " +p.getName()+ " ID: "+p.getId());
				String[] categorias=mc.mc_project_get_categories(username, password, p.getId());
				for (String cat:categorias){
					logger.info(" Categoría: "+cat);
				}
			}
			logger.info("Insertando issue...");
			IssueData issue=new IssueData();
			String idProyecto,nombreProyecto;
			idProyecto=sugerencia.getProyecto().substring(0,sugerencia.getProyecto().indexOf('-'));
			nombreProyecto=sugerencia.getProyecto().substring(sugerencia.getProyecto().indexOf('-')+1,sugerencia.getProyecto().length());
			//issue.setProject(new ObjectRef(new BigInteger(Constantes.MANTIS_DEFAULTPROJECTID), Constantes.MANTIS_DEFAULTPROJECT));
			issue.setProject(new ObjectRef(new BigInteger(idProyecto), nombreProyecto));
			issue.setCategory(Constantes.MANTIS_DEFAULTCATEGORY);
			//issue.setSummary("Issue creado desde MantisConnect WS Client");
			if (sugerencia.getTipo().equals("SUGERENCIA")) {
			      issue.setSummary("[SUGERENCIA] "+sugerencia.getAsunto());
			}else issue.setSummary("[INCIDENCIA] "+sugerencia.getAsunto());
			
			//isue.setDescription("Testando MantisConnect. Probando adjuntos");
						issue.setDescription(sugerencia.getDescripcion()+"\n"+sugerencia.getEntorno());
			
			issue.setOs(System.getProperty("os.name"));
			issue.setOs_build(System.getProperty("os.version"));
			
			issue.setPlatform(System.getProperty("java.version")+ " "+System.getProperty("java.vendor"));

			IssueNoteData[] notes=new IssueNoteData[1] ;
			IssueNoteData note=new IssueNoteData();
			note.setText("Sugerencia creada desde plataforma LocalGIS " + sugerencia.getDetallesAdicionales());
			notes[0]=note;
			issue.setNotes(notes);
			


			BigInteger issue_id=mc.mc_issue_add(username, password, issue);
			logger.debug("Issue creado");
			if (sugerencia.getFicheroAdjunto()!=null && (!sugerencia.getFicheroAdjunto().equals(""))) {
				logger.info("Añadiendo adjunto a issue "+issue_id+"...");
				//String filename="C://temp/logo.gif";
				String filename=sugerencia.getFicheroAdjunto();
				File file=new File(filename);
				byte[] content=Utils.readFile(file);
				mc.mc_issue_attachment_add(username, password, issue_id, filename, Utils.getMimeType(file), content);
				
			}
			logger.info("Registro sugerencia/incidencia completado");
		}catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}


	}

	public static List<Proyecto> getProyectos(String username, String password) throws Exception {
		List<Proyecto> lstProyectos=new ArrayList<Proyecto>();
		String url = null;
		
		if (username==null) {
			if (aplicacion.getString(MantisConstants.USER_MANTIS) == null)
				username = MantisConstants.DEFAULT_USER_MANTIS;
			else
				username = aplicacion.getString(MantisConstants.USER_MANTIS);
		}

		if (password==null){
			if (aplicacion.getString(MantisConstants.PASSWORD_MANTIS) == null)
				password = MantisConstants.DEFAULT_PASSWORD_MANTIS;
			else
				password = aplicacion.getString(MantisConstants.PASSWORD_MANTIS);
		}

		if (aplicacion.getString(MantisConstants.URL_MANTIS) == null)
			url = MantisConstants.DEFAULT_URL_MANTIS;
		else
			url = aplicacion.getString(MantisConstants.URL_MANTIS);

		url=url+Constantes.MANTIS_URL_API;
		
		try{	
			MantisConnectLocator locator=new MantisConnectLocator();
			logger.info("Conectando a "+url+" con usuario:"+username);
			MantisConnectPortType mc=locator.getMantisConnectPort(new URL(url));
			logger.info("Conexion Mantis OK");
			logger.debug("Listando proyectos disponibles...");
			ProjectData[] projects=mc.mc_projects_get_user_accessible(username, password);
			Proyecto proyecto=null;
			for (ProjectData p:projects) {
				logger.info("Proyecto " +p.getName()+ " ID: "+p.getId());
				proyecto= new Proyecto();
				proyecto.setId(p.getId().toString());
				proyecto.setNombre(p.getName());
				lstProyectos.add(proyecto);
			}
		
		}catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return lstProyectos;
	}

}