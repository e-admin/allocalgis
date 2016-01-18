/**
 * UpdaterUserInferfaceHook.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.util.Collection;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;

/**
 * 
 * Hook for interaction with the user
 * @author juacas
 *
 */
public interface UpdaterUserInferfaceHook
{
String ROLE = UpdaterUserInferfaceHook.class.getName(); // For Plexus resolution
/**
 * Shows an informative list of modules that are to be installed.
 * @param pack List of needed modules
 * @param installedModules current status of the system. May be used for checking optional upgrades
 * @return
 */
public boolean confirmInstalls(Collection<Module> pack, ModuleDependencyTree installedModules);
public boolean confirmUpgrades(Collection<Module> pack, ModuleDependencyTree installedModules);
public boolean confirmRemoves(Collection<Module> pack, ModuleDependencyTree installedModules);
public void showMessage(String string);
public void notifyActivity(String string);
public void reportProgress(String msg, int current, int total);
}
