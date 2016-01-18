/**
 * AbstractUpdater.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.net.URL;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleRegistry;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.impl.ModuleRegistryImpl;

/**
 * Register dependencies and make the steps to resolve them
 * 
 * @author juacas
 * @deprecated
 */
@Deprecated
public abstract class AbstractUpdater implements Updater
{
    private Module mod;

    /* (non-Javadoc)
     * @see com.localgis.module.updater.Updater#register()
     */
    
    public void register()
    {
		// Load Module Information
		Module mod = getModule();
		// register the information in the installation registry
		ModuleRegistry registry = getModuleRegistry();
		registry.register(mod);
    }
    
    
    public Module getModule()
    {
	if (this.mod==null)
	    {
		URL url = this.getClass().getClassLoader().getResource("META-INF/module.xml");
		Module mod;
		try
		    {
			mod = XMLModuleSerializer.getModuleFromXMLURL(url);
		    } catch (Exception e)
		    {
			//Unexpected
			throw new RuntimeException(e);
		    } 
		this.mod=mod;
	    }
	
	return this.mod;
    }
    /* (non-Javadoc)
     * @see com.localgis.module.updater.Updater#install()
     */
    
    public abstract void install() throws DependencyViolationException;
    /* (non-Javadoc)
     * @see com.localgis.module.updater.Updater#upgrade()
     */
    
    public abstract void upgrade() throws DependencyViolationException;
    
    /* (non-Javadoc)
     * @see com.localgis.module.updater.Updater#uninstall()
     */
    
    public abstract void uninstall() throws DependencyViolationException;
    
    
    private ModuleRegistry getModuleRegistry()
    {
	return ModuleRegistryImpl.getInstance();
    }
}
