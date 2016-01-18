/**
 * SaveImageAsButtonPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.inventario.edit.saveimageas;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.inventario.edit.saveimageas.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.SaveImageAsPlugIn;

public class SaveImageAsButtonPlugIn extends SaveImageAsPlugIn {
	public Icon getIcon() {                         
		return IconLoader.icon("saveimageas.png");
	}
	@SuppressWarnings("unchecked")
	public String getName(){
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.inventario.edit.saveimageas.languages.saveimageasi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("saveimageas",bundle2);
    	String name = I18N.get("saveimageas","name");
    	return name;
    }
	public void initialize(PlugInContext context) throws Exception
    {
        
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar().addPlugIn(
          		getIcon(),this,
          		null,
            context.getWorkbenchContext());

    }
}
