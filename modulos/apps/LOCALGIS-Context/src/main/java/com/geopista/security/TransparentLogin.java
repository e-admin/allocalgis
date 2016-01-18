/**
 * TransparentLogin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.utilidades.CAuthDialog;
import com.geopista.security.sso.SSOAuthManager;

public class TransparentLogin {


	static Logger logger = Logger.getLogger(TransparentLogin.class);
	
	/**
	 * Comprueba que el usuario esta autorizado para acceder a la aplicacion y
	 * sus permisos para las diferentes acciones.
	 * @param securityPolicy 
	 * @param fromInicio 
	 * @param b 
	 * @param solicitarEntidad 
	 */
	public boolean showAuth(ISecurityPolicy securityPolicy, boolean fromInicio,boolean global, 
															boolean solicitarEntidad, boolean transparent) {
		try {
			
			boolean resultado = false;
			
			if (securityPolicy!=null)
				securityPolicy.resetSecurityPolicy();

			boolean mostrarPantallaAutenticacion=true;
			if (transparent){
				if (Constantes.contrasenna!=null){
				logger.error("Autenticando automaticamente por desconexion");
				 com.geopista.security.SecurityManager.login(Constantes.nombreUsuario, Constantes.contrasenna,
						 securityPolicy.getIdApp());
				 
				 SSOAuthManager.saveSSORegistry();
				 
				 //Cargamos los datos del municipio que tenía seleccionado el usuario para que en el servidor
				 //se vuelvan a reestablecer los datos.
				 boolean comprobarUsuarioYaAutenticado=false;
				 boolean solicitarMunicipio=true;
				 if (!global){
					 if (solicitarEntidad){
						 AppContext.seleccionarMunicipio(securityPolicy.getFrame(),solicitarMunicipio,comprobarUsuarioYaAutenticado,transparent);
					 }
					 else if ((AppContext.getIdCurrentEntidad()!=-1) && (AppContext.getIdCurrentEntidad()!=0)){
						 AppContext.seleccionarMunicipio(securityPolicy.getFrame(),solicitarMunicipio,comprobarUsuarioYaAutenticado,transparent); 
					 }
					 else{
						 logger.info("No es necesario solicitar identificador de entidad ni de municipio");
					 }
				 }
					 mostrarPantallaAutenticacion=false;
				}
				else{
					logger.error("No podemos autenticar automaticamente porque el sistema no dispone de contraseña");
				}
			}
			
			
			if (mostrarPantallaAutenticacion){
			
				logger.error("Mostrando pantalla de autenticacion por desconexion");
				
				CAuthDialog auth = new com.geopista.app.utilidades.CAuthDialog(
						securityPolicy.getFrame(), true, securityPolicy.getLogin(),
						securityPolicy.getIdApp(),
						Integer.parseInt(securityPolicy.getIdMunicipio()),
						securityPolicy.getAplicacion().getI18NResource());
				/*
				 * FUNCIONA com.geopista.app.utilidades.CAuthDialog auth = new
				 * com.geopista.app.utilidades.CAuthDialog(this, true,
				 * CConstantesComando.loginLicenciasUrl,Constantes.idApp,
				 * ConstantesLocalGISEIEL.IdEntidad, aplicacion.getI18NResource());
				 */
				// // CAuthDialog auth = new CAuthDialog(this, true,
				// ConstantesLocalGISEIEL.localgisEIEL,ConstantesLocalGISEIEL.idApp,
				// ConstantesLocalGISEIEL.IdMunicipio,
				// aplicacion.getI18NResource());
				auth.setBounds(30, 60, 315, 155);
				if (fromInicio) {
					resultado = auth.showD(true);
					if (!resultado)
						return false;
				} else {
					auth.show();
				}
				
				boolean comprobarUsuarioYaAutenticado=false;
				boolean solicitarMunicipio=true;
				if (!global){
					 if (solicitarEntidad){
						 AppContext.seleccionarMunicipio(securityPolicy.getFrame(),solicitarMunicipio,comprobarUsuarioYaAutenticado,transparent);
					 }
					 else if ((AppContext.getIdCurrentEntidad()!=-1) && (AppContext.getIdCurrentEntidad()!=0)){
						 AppContext.seleccionarMunicipio(securityPolicy.getFrame(),solicitarMunicipio,comprobarUsuarioYaAutenticado,transparent); 
					 }
					else{
						logger.error("No podemos autenticar automaticamente porque el sistema no dispone de contraseña");
					}
				 }
				
				securityPolicy.setPolicy();
			}
			return true;

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al autenticar al usuario " + sw.toString());
			JOptionPane optionPane = new JOptionPane(
					"Error al inicializar: \n"
							+ ((e.getMessage() != null && e.getMessage()
									.length() >= 0) ? e.getMessage()
									: e.toString()), JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog(securityPolicy.getFrame(), "ERROR");
			dialog.setVisible(true);
			return false;
		}
	}
}
