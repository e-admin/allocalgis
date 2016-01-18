
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.geopista.ui.plugin.wfs;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.wfs.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class WfsClientPlugIn extends ThreadedBasePlugIn 
{
    

    private static AppContext aplicacion = (AppContext) AppContext
            .getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();

    //VARIABLES NECESARIAS QUE SE PASAN DESDE LOS PANELES

    
    public void initialize(PlugInContext context) throws Exception
    {
        
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar().addPlugIn(
          		getIcon(), this,
          		null,
            context.getWorkbenchContext());

    }

    public String getName(){
        Locale loc=Locale.getDefault();      	 
    	ResourceBundle bundle2 = ResourceBundle.getBundle("com.geopista.ui.plugin.wfs.languages.WfsDialogi18n",loc,this.getClass().getClassLoader());    	
        I18N.plugInsResourceBundle.put("WfsDialog",bundle2);
    	String name = I18N.get("WfsDialog","WfsClient");
    	return name;
    }
    public boolean execute(PlugInContext context) 
    {
    	try{
	        AppContext appContext = (AppContext) AppContext.getApplicationContext();
	        reportNothingToUndoYet(context);
	        GUIUtil.centreOnWindow(dialog(context));
	        dialog(context).setVisible(true);
    	}catch (Exception e){
    		e.printStackTrace();
    	}finally{
    		return true;
    	}
    }
    
    private WfsDialog dialog(PlugInContext context) {
        
        return WfsDialog.instance(context.getWorkbenchContext().getIWorkbench(),context);
    }
    
    
    public Icon getIcon() {                         
        return IconLoader.icon("WFS-MNEicon.png");
    }
    
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
 
}