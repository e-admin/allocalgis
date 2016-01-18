package com.geopista.ui.plugin.inventario.edit.saveimageas;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.SaveImageAsPlugIn;
import com.geopista.ui.plugin.inventario.edit.saveimageas.images.IconLoader;

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
