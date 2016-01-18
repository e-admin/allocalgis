package com.geopista.app.sugerencias;


import java.math.BigInteger;
import java.net.URL;
import java.io.File;




import biz.futureware.mantis.rpc.soap.client.*;

public class MantisConnectModeloClient {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MantisConnectModeloClient.class);
	
	private static String MANTISURL="http://pamod-app2.c.ovd.interhost.com:9090/mantis/api/soap/mantisconnect.php";
	private static String MANTIS_DEFAULTPROJECT="LOCALGIS-MODELO";
	private static String MANTIS_DEFAULTPROJECTID="1";
	private static String MANTIS_DEFAULTCATEGORY="prueba";
	
	private static String MANTIS_DEFAULTUSER="cau";
	private static String MANTIS_DEFAULTPASS="caumodelo";

	
	public static void createIssue(Sugerencia sugerencia) throws Exception{
		
		String username=MANTIS_DEFAULTUSER;
		String password=MANTIS_DEFAULTPASS;
		
		if (sugerencia.getUsuario()!=null) {
			username=sugerencia.getUsuario();
			password=sugerencia.getPassword();
		}
		
		
		try{	
			MantisConnectLocator locator=new MantisConnectLocator();
			//logger.info("Conectando a "+MANTISURL+" con usuario"+username +"/"+password);
			logger.info("Conectando a "+MANTISURL+" con usuario:"+username);
			MantisConnectPortType mc=locator.getMantisConnectPort(new URL("http://pamod-app2.c.ovd.interhost.com:9090/mantis/api/soap/mantisconnect.php"));
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
			issue.setProject(new ObjectRef(new BigInteger(MANTIS_DEFAULTPROJECTID),MANTIS_DEFAULTPROJECT));
			issue.setCategory(MANTIS_DEFAULTCATEGORY);
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
			if (sugerencia.getFicheroAdjunto()!=null) {
				logger.info("Añadiendo adjunto a issue "+issue_id+"...");
				String filename="C://temp/logo.gif";
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

}