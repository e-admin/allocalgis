package com.geopista.app.utilidades;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.SegPassword;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.security.sso.global.SSOConstants;

public class PasswordManager {

	
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PasswordManager.class);

	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private int dias;
	
	private SegPassword segPassword = null;
	String url;
	
	
	public PasswordManager(){
		segPassword = new SegPassword();
		//-----NUEVO----->
		if(SSOAuthManager.isSSOActive()) url=AppContext.getApplicationContext().getString(SSOConstants.SSO_SERVER_URL);
		else url = AppContext.getApplicationContext().getString("geopista.conexion.servidor");
		//---FIN NUEVO--->
	}
	
	public String getMessage(String id)
    {		    
        return aplicacion.getI18nString(id);
    }
	
	
	/**
	 * Comprobamos el numero de intentos
	 */
	public void comprobarIntentos(String username){
		try {			
			if (segPassword.getIntentos_reiterados() >= (segPassword.getIntentos()-1) && (!segPassword.isBloqueado())){
				StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","bloquea");
			    
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Verificamos la contraseña
	 * @param idApp
	 * @param segPassword
	 * @param intentos
	 */
	public boolean verificacionContraseña(String idApp,boolean autenticado) {
		
    	if (AppContext.getApplicationContext().getBlackboard().get("intentos") != null){
    		segPassword.setIntentos(((Integer) AppContext.getApplicationContext().getBlackboard().get("intentos")).intValue());
    	}
    	if (AppContext.getApplicationContext().getBlackboard().get("bloqueado") != null) 
    		segPassword.setBloqueado(Boolean.valueOf((String) AppContext.getApplicationContext().getBlackboard().get("bloqueado")).booleanValue());

    	if (AppContext.getApplicationContext().getBlackboard().get("intentos_reiterados") != null) 
    		segPassword.setIntentos_reiterados(((Integer) AppContext.getApplicationContext().getBlackboard().get("intentos_reiterados")).intValue());

    	if (AppContext.getApplicationContext().getBlackboard().get("usuarioNoExiste") != null) 
    		segPassword.setUsuarioNoExiste(Boolean.valueOf((String) AppContext.getApplicationContext().getBlackboard().get("usuarioNoExiste")).booleanValue());
    	if (!segPassword.isUsuarioNoExiste()){
    	if ((segPassword.getIntentos_reiterados() >= 0) && (segPassword.getIntentos() != 0) &&
    				((segPassword.getIntentos_reiterados() < segPassword.getIntentos()-1)) && (!autenticado) && (!segPassword.isBloqueado())){
    		try {
				StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","actualizaIntentos");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int intentos = segPassword.getIntentos_reiterados()+1;
			JOptionPane optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion1.intentos")+" "+intentos+" "+getMessage("CAuthDialog.descripcion2.intentos")+" "+
											segPassword.getIntentos()+" "+
											getMessage("CAuthDialog.descripcion3.intentos"), 
											JOptionPane.INFORMATION_MESSAGE);
            
            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.intentos"));
            dialog.setVisible(true);
            return false;
		}
    	else{ 
	    	if (segPassword.isBloqueado()){
	        	//autenticado = false;
	            JOptionPane optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.usuarioBloqueado"), JOptionPane.INFORMATION_MESSAGE);
	                       
	            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.usuarioBloqueado"));
	            
	            dialog.setVisible(true);
	            return false;
	        }
	    	else{
	    		if (!idApp.equals(AppConstants.idApp)){
	    			if (segPassword.isCaducado()){
			        	//autenticado = false;
			            JOptionPane optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.contraseniaCaducada"), JOptionPane.INFORMATION_MESSAGE);
			                       
			            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.contraseniaCaducada"));			            
			            dialog.setVisible(true);
			            		            
			            lanzarAdministracion(dialog);
			            return false;
	    			}    			
	    			else{
	    				if (segPassword.isAviso()){
	    					JOptionPane optionPane = null;
	    					if (dias == 0)
	    						optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
	    					else
	    						optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion1.cambioContrasenia")+" "+dias+" "+getMessage("CAuthDialog.descripcion2.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
	    		                       
	    		            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.cambioContrasenia"));
	    		            dialog.setVisible(true);
	    		            
	    		            lanzarAdministracion(dialog);
	        			}
	    			}
		        }
	    	}
    	}
//    	try {
//			StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","reiniciaIntentos");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	return true;
    	}
    	autenticado = false;
    	return false;
	}
	
	
	
	public SegPassword getSegPassword() {
		return segPassword;
	}

	/**
	 * Obtenemos la informacion de seguridad de la contraseña
	 * @param user
	 * @throws Exception
	 */
	public void seguridadContrasenia(String user) throws Exception{

/*    	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    	Connection conn = aplicacion.getConnection(false);

        PreparedStatement ps = conn.prepareStatement("seguridadContrasenia");
        ResultSet  rs = null;
        try {             
            ps.setString(1, user.toUpperCase());
            rs =ps.executeQuery();
            if (rs.next()){
            	
            	segPassword.setUser(user);
            	segPassword.setBloqueado(rs.getBoolean("bloqueado"));
            	Date fechaModificacion = rs.getDate("fecha_proxima_modificacion");
            	Date fechaActual = new Date();
            	// Si la fecha de modificación es mayor:
            	if (fechaModificacion.compareTo(fechaActual) > 0){
            		segPassword.setCaducado(false);
            	}
            	else{
            		segPassword.setCaducado(true);
            	}
            	long diferencia = fechaModificacion.getTime() - fechaActual.getTime();
            	dias = (int) Math.floor(diferencia / (1000 * 60 * 60 * 24));
            	//System.out.println(dias);
            	if (dias > rs.getInt("aviso")){
            		segPassword.setAviso(false);
            	}
            	else{
            		segPassword.setAviso(true);
            	}
            	segPassword.setIntentos(rs.getInt("intentos"));
            	segPassword.setIntentos_reiterados(rs.getInt("intentos_reiterados"));  
            }
            else{
            	segPassword.setUser(user);
            	segPassword.setBloqueado(false);
            	segPassword.setCaducado(false);
            	segPassword.setAviso(false);
            }

       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR en seguridadContrasenia :"+sw.toString());
            throw e;

        }finally{
        	try{rs.close();}catch(Exception e){};
     	   	try{ps.close();}catch(Exception e){};
     	   	try{conn.close();}catch(Exception e){};
        }*/
    }
	
	/**
	 * Lanzamos la herramienta de Administracion
	 * @param dialog
	 */
	public void lanzarAdministracion(JDialog dialog){
    	
    	Object[] options = {getMessage("CAuthDialog.descripcion.si"), getMessage("CAuthDialog.descripcion.no")};
        
        int opcionElegida = JOptionPane
		.showOptionDialog(dialog, getMessage("CAuthDialog.descripcion.irAdministracion"), getMessage("CAuthDialog.title.irAdministracion"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
        if (opcionElegida == 0) {
        	Runtime ejecutor=Runtime.getRuntime();	
        	try {
        		AppContext appContext = (AppContext) AppContext.getApplicationContext();
        		String comando = "javaws "+appContext.getString(AppContext.URL_TOMCAT)+"/software/localgis_administrador.jnlp";
        		Process proceso=ejecutor.exec(comando);
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
    }
}
