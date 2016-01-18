package com.localgis.module.updater;

import java.util.Collection;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;

public class DebugPrompter implements UpdaterUserInferfaceHook
{

    
    public boolean confirmInstalls(Collection<Module> pack, ModuleDependencyTree installedModules)
    {
	// TODO Auto-generated method stub
	return false;
    }

    
    public boolean confirmUpgrades(Collection<Module> pack, ModuleDependencyTree installedModules)
    {
	// TODO Auto-generated method stub
	return false;
    }

    
    public boolean confirmRemoves(Collection<Module> pack, ModuleDependencyTree installedModules)
    {
	// TODO Auto-generated method stub
	return false;
    }

    
    public void showMessage(String string)
    {
	System.out.println(string);
    }

    
    public void notifyActivity(String string)
    {
	System.out.println("============================================================");
	System.out.println("|"+string);
	System.out.println("============================================================");
    }

	
	public void reportProgress(String msg, int current, int total)
	{
		System.out.print(msg+" ("+current+"/"+total+")\r");	
	}

}
/**
 * DebugPrompter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
