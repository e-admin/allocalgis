/**
 * PrompterUserInterface.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.updater;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
/**
 * Console-based user interface.
 * Uses maven (plexus) console interface
 * @author juacas
 *
 */
public class PrompterUserInterface implements UpdaterUserInferfaceHook
{

	private static Logger logger= Logger.getLogger(PrompterUserInterface.class);

	private static final String YES = "Yes";
	private static final String NO = "No";
	private final Prompter prompter;
	private static List<String> yesNoResponses = new Vector<String>() {{ add(YES); add(NO);}};;

	public PrompterUserInterface(Prompter prompter) {
		this.prompter=prompter;
	}

	public boolean confirmInstalls(Collection<Module> pack, ModuleDependencyTree installedModules)
	{
		if (pack.size()==0) return true;
		boolean resp= confirmActionOnCollection(pack,"The following modules will be installed:\n");
		if (resp==true)
			showMessage("OK, Perform Upgrades after installation.");
		return resp;
	}

	public boolean confirmUpgrades(Collection<Module> pack, ModuleDependencyTree installedModules)
	{
		if (pack.size()==0) return true;
		return confirmActionOnCollection(pack,"The following modules can OPTIONALLY be UPGRADED. Upgrade? (Recommended Yes):\n");
	}

	protected boolean confirmActionOnCollection(Collection<Module> pack, String str)
	{
		StringBuilder message=new StringBuilder(str);
		for (Module module : pack)
		{
			message.append(module.toString()).append(":").append(module.getDescription()).append("\n");
		}
		message.append("Continue?");
		try
		{
			String response = this.prompter.prompt(message.toString(), yesNoResponses, NO);
			return YES.equalsIgnoreCase(response);

		} catch (PrompterException e)
		{
			throw new RuntimeException(e);
		}
	}

	public boolean confirmRemoves(Collection<Module> pack, ModuleDependencyTree installedModules) {
		if (pack.size()==0) return true;
		return confirmActionOnCollection(pack,"The following modules will be REMOVED/DISABLED FROM THE SYSTEM:\n");
	}

	public void showMessage(String message)
	{
		String messageFormat = "";
		try
		{
			messageFormat = message;
			this.prompter.showMessage(messageFormat+ "\n");
			logger.info(messageFormat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void notifyActivity(String string)
	{
		String messageFormat = "";
		try
		{
			messageFormat = "\n" + "============================================================" + "\n" +
							"|" + string +
							"\n" + "============================================================";
			this.prompter.showMessage(messageFormat + "\n");
			logger.info(messageFormat);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}

	}

	public void reportProgress(String msg, int current, int total) {
		String messageFormat = "";
		try {
			messageFormat = "\r" + msg + "(" + current + ((total!=0)? ("/" + total):"") + ")";
			this.prompter.showMessage(messageFormat);
			logger.info(messageFormat);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}

}
