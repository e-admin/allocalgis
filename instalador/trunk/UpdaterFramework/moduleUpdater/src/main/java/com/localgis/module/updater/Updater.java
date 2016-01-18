/**
 * Updater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.util.Properties;
import java.util.Set;

import org.apache.maven.artifact.Artifact;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleReference;
import com.localgis.tools.modules.exception.DependencyViolationException;

public interface Updater
{
    /**
     * Notify the registry about this module and its dependencies
     */
    public abstract void register();
    /**
     * Perform the initial installation actions for this type of updater
     */
    public abstract void install() throws DependencyViolationException;
    /**
     * Perform the updating actions from the previous versions
     */
    public abstract void upgrade() throws DependencyViolationException;
    /**
     * Perform the uninstall actions of the current version
     * @throws DependencyViolationException
     */
    public abstract void uninstall() throws DependencyViolationException;
    /**
     * Perform the upgrade processes neeeded to leave the system with this module installed and operative
     */
    @Deprecated
    public abstract void installOrUpgrade();
    /**
     * Returns the previous version this updater can upgrade from
     * @return
     */
    public ModuleReference canUpgradeFrom();
    /**
     * Returns the module this Updater is responsible for 
     */
    public Module getModule();
    
    public void setUserInterfaceFacade(UpdaterUserInferfaceHook hook);

    public abstract void setModule(Module module);
    /**
     * Store the list of dependencies this module needs
     * @param dependencies
     */
    public abstract void setDependencies(Set<org.apache.maven.artifact.Artifact> dependencies);
    /**
     * Store the configuration properties for the updater's operations
     * @param properties
     */
    public abstract void setProperties(Properties properties);
    
    /**
     * Check resources and services for correctness
     */
    public abstract boolean checkInstallationValidity();
    
    public abstract boolean checkUninstallationValidity();
    
    /**
     * Artifact con el contenido binario raiz que utiliza el instalador.
     * @param jarArtifact
     */
    public abstract void setBinaryArtifact(Artifact jarArtifact);
   
    /**
     * Returns config locale I18N
     * @return
     */
    public abstract String getConfigLocale ();
    
    public void setOverwrite(boolean overwrite);
    
}