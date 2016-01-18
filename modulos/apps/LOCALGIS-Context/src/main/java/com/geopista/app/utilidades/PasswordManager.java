/**
 * PasswordManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import java.awt.HeadlessException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.SegPassword;
import com.geopista.protocol.net.EnviarSeguro;

public class PasswordManager {

	
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PasswordManager.class);

	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private int dias;

	private SegPassword segPassword = null;
	String url;
	
	
	public PasswordManager(){
		segPassword = new SegPassword();
		url=AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.PRINCIPAL_WEBAPP_NAME;
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
				StringReader result= EnviarSeguro.enviarPlano(url + ServletConstants.CSERVLETINTENTOS_SERVLET_NAME,"actualizaIntentos");
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
			        	autenticado = false;
			            JOptionPane optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.contraseniaCaducada"), JOptionPane.INFORMATION_MESSAGE);
			                       
			            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.contraseniaCaducada"));			            
			            dialog.setVisible(false);
			            		            			            
			            return lanzarAdministracion(dialog, getMessage("CAuthDialog.descripcion.contraseniaCaducada"), true);
	    			}    			
	    			else{
	    				if (segPassword.isAviso()){
	    					JOptionPane optionPane = null;
	    					String mensaje;
	    					if (dias == 0){
	    						optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
	    						mensaje = getMessage("CAuthDialog.descripcion.cambioContrasenia");
	    					}
	    					else{
	    						optionPane = new JOptionPane(getMessage("CAuthDialog.descripcion.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
	    						mensaje = getMessage("CAuthDialog.descripcion1.cambioContrasenia")+" "+dias+" "+getMessage("CAuthDialog.descripcion2.cambioContrasenia");
	    					}           
	    		            JDialog dialog = optionPane.createDialog(getMessage("CAuthDialog.title.cambioContrasenia"));
	    		            dialog.setVisible(false);
	    		            
	    		            return lanzarAdministracion(dialog, mensaje, false);
	        			}
	    			}
		        }
	    	}
    	}
    	try {
			StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","reiniciaIntentos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

    	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    	Connection conn = aplicacion.getConnection(false);
    	ResultSet  rs = null;
    	PreparedStatement ps = null;
        try {          
        	ps = conn.prepareStatement("SELECT id_entidad FROM iuseruserhdr where name = '"+user.toUpperCase()+"'");
        	rs =ps.executeQuery();        	
            if (rs.next()){
            	int id_entidad = rs.getInt("id_entidad");
            	if (id_entidad != 0){
		            ps = conn.prepareStatement("seguridadContrasenia");
		            ps.setString(1, user.toUpperCase());
            	}
            	else
            		ps = conn.prepareStatement("select bloqueado, fecha_proxima_modificacion, intentos_reiterados from iuseruserhdr where name ='"+user.toUpperCase()+"'");
		            
	            rs =ps.executeQuery();
	            if (rs.next()){
	            	
	            	segPassword.setUser(user);
	            	segPassword.setBloqueado(rs.getBoolean("bloqueado"));
	            	Date fechaModificacion = rs.getDate("fecha_proxima_modificacion");
	            	Date fechaActual = new Date();
	            	// Si la fecha de modificación es mayor:
	            	if ((fechaModificacion != null) && fechaModificacion.compareTo(fechaActual) > 0){
	            		segPassword.setCaducado(false);
	            		long diferencia = fechaModificacion.getTime() - fechaActual.getTime();
	            		dias = (int) Math.floor(diferencia / (1000 * 60 * 60 * 24));
	            	}
	            	else{
	            		segPassword.setCaducado(true);
	            		dias = 0;
	            	}
	            	//System.out.println(dias);
	            	if (id_entidad != 0){
		            	if (dias > rs.getInt("aviso")){
		            		segPassword.setAviso(false);
		            	}
		            	else{
		            		segPassword.setAviso(true);
		            	}
		            	segPassword.setIntentos(rs.getInt("intentos"));
	            	}
	            	else{
	            		if (dias > AppConstants.DIAS_AVISO){
		            		segPassword.setAviso(false);
		            	}
		            	else{
		            		segPassword.setAviso(true);
		            	}
		            	segPassword.setIntentos(AppConstants.INTENTOS_MINIMOS);
	            	}
	            		
	            	
	            	segPassword.setIntentos_reiterados(rs.getInt("intentos_reiterados"));  
	            	AppContext.getApplicationContext().getBlackboard().put("intentos", segPassword.getIntentos());
	                AppContext.getApplicationContext().getBlackboard().put("bloqueado",new Boolean(segPassword.isBloqueado()).toString());
	                AppContext.getApplicationContext().getBlackboard().put("intentos_reiterados",segPassword.getIntentos_reiterados());
	            }
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
        }
    }
	
	/**
	 * Lanzamos la herramienta de Administracion
	 * @param dialog
	 */
	public boolean lanzarAdministracion(JDialog dialog, String mensaje, Boolean caducada){
    	
    	Object[] options = {getMessage("CAuthDialog.descripcion.si"), getMessage("CAuthDialog.descripcion.no")};

        int opcionElegida = JOptionPane
		.showOptionDialog(dialog, mensaje+"\n"+getMessage("CAuthDialog.descripcion.irAdministracion"), getMessage("CAuthDialog.title.contraseniaCaducada"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
        if (opcionElegida == 0) {
        	JDialogCambioPassword jDialog = new JDialogCambioPassword(segPassword.getUser());
        	jDialog.setResizable(false);
        	jDialog.setModal(true);
        	jDialog.setLocationRelativeTo(dialog);
        	jDialog.setVisible(true);
        	return jDialog.isValido();
        }
        else
        	if (!caducada)
        		return true;
        	else
        		return false;
    }
}
