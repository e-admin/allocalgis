/**
 * AbstractLocalGISUpdater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater.impl;

import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

import com.localgis.module.updater.Updater;
import com.localgis.module.updater.UpdaterUserInferfaceHook;
import com.localgis.module.utilitys.UtilsI18N;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleReference;

public abstract class AbstractLocalGISUpdater implements Updater
{
	protected String locale = null;
	protected Artifact binaryArtifact;
	protected UpdaterUserInferfaceHook userInterfaceFacade;
	protected Module module;
	protected Set<Artifact> dependencies;
	protected Properties properties;
	protected boolean overwrite;
	
	
	public UpdaterUserInferfaceHook getUserInterfaceFacade() {
		return userInterfaceFacade;
	}

	public Set<Artifact> getDependencies() {
		return dependencies;
	}

	public Properties getProperties() {
		return properties;
	}

	
	public void register()
	{
		// TODO Auto-generated method stub
	}

	
	public ModuleReference canUpgradeFrom()
	{
		// do nothing
		return null;
	}

	
	public void setUserInterfaceFacade(UpdaterUserInferfaceHook hook)
	{
		this.userInterfaceFacade=hook;
	}

	
	public void setModule(Module module)
	{
		this.module=module;
	}
	
	public Module getModule()
	{
		return this.module;
	}
	
	public void setDependencies(Set<Artifact> dependencies)
	{
		this.dependencies=dependencies;
	}

	
	public void setProperties(Properties properties)
	{
		this.properties=properties;
	}

	
	public void setBinaryArtifact(Artifact jarArtifact)
	{
		this.binaryArtifact=jarArtifact;
	}

	protected Artifact getBinaryArtifact()
	{
		return this.binaryArtifact;
	}
	
	
	/**
	 * Obtiene configuracion establecida para locale
	 * Si NO tiene valor establecer a partir de fichero de propiedades
	 * @return
	 */
	
	public String getConfigLocale () {
		String LOCALGIS_DEFAULT_LOCALE = "localgis_default_locale";
		//No tiene valor establecer valor inicial
		if (this.locale == null && this.properties != null) {
			try{
				this.locale = properties.getProperty(LOCALGIS_DEFAULT_LOCALE);
			}catch (Exception e) {
				//No establecer valor para usar configuracion por defecto
				this.locale = "";
			}
		}
		
		return this.locale;
	}
	
	/**
	 * Obtiene nombre final del artefacto incluyendo version segun se indique 
	 * @param module
	 * @param includeVersion
	 * @return
	 */
	public String getFinalNameArtifact (boolean includeVersion, Module module) {
		String name = "";
		if (module != null) {
			String finalName = module.getArtifact().getFinalName();
			//Nombre por defecto <artefactId>[-<version>] (con o sin version segun se indique)
			String defaultName = module.getArtifact().getArtifactId() + ((includeVersion)? "-" + module.getArtifact().getVersion() : "");
			//Si no se sepecifica nombre concreto usar nombre por defecto
			name = ((finalName != null && !finalName.equals(""))? finalName : defaultName);
		}
		return name;
	}
	
	
	/**
	 * Retorna identificacion completa de artefacto asociado al modulo indicado
	 * @return
	 */
	protected String getFullFinalNameArtifact (Module module) {
		String fullName = "";
		if (module != null) {
			//Obtenemos nombre del artefacto con version si corresponde
			String name = getFinalNameArtifact(true, module);
			fullName = name + "." + module.getArtifact().getPackaging();
		}
		return fullName;
	}
	
	/**
	 * Obtiene mensaje I18N mediante configuracion estandar
	 * @param keyMessage
	 * @return
	 */
	protected String getMessageResource (String keyMessage) {
		return UtilsI18N.getMessageResource(keyMessage, getConfigLocale());
	}
	//Presentacion mensajes pantalla varios formatos
	protected void showMessageInitProcess (String mensaje) {
		this.userInterfaceFacade.showMessage(getMessageResource ("text.resultadoOperacion.inicioProceso")+ " " + mensaje);
	}
	protected void showMessageSuccess (String mensaje) {
		this.userInterfaceFacade.showMessage(getMessageResource ("text.resultadoOperacion.exito")+ " " + mensaje);
	}
	protected void showMessageFailed (String mensaje) {
		this.userInterfaceFacade.showMessage(getMessageResource ("text.resultadoOperacion.fallo")+ " " + mensaje);
	}
	protected void showMessageOk (String mensaje) {
		this.userInterfaceFacade.showMessage(getMessageResource ("text.resultadoOperacion.ok")+ " " + mensaje);
	}
	protected void showMessageError (String mensaje) {
		this.userInterfaceFacade.showMessage(getMessageResource ("text.resultadoOperacion.error")+ " " + mensaje);
	}
	protected void showLog (String mensaje) {
		this.userInterfaceFacade.showMessage(mensaje);
	}
	
	public void setOverwrite(boolean overwrite){
		this.overwrite=overwrite;
	}
}
