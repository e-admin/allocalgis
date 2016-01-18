/*
 * Created on 02-may-2004
 *
 */
package com.geopista.util;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.geopista.ui.autoforms.AutoDialog;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @deprecated
 */
public class FeatureSchemaDialog {
	public FeatureSchemaDialog(JFrame frame, Feature feature,Layer layer)
	{

	
//		JFrame frame=new JFrame("Esquema");
//		frame.setLocation(200,300);
//		frame.setSize(500,300);
   
	  AutoDialog d= new AutoDialog(frame, "Datos de la capa "+layer.getName()+" con GEOPISTA SCHEMA", true, feature);
	  d.setMaxRows(20);
	  d.buildDialogByTables(false);
  
	  d.setSideBarDescription("Toda la estructura del formulario, incluyendo su validación y comportamiento dinámico se obtiene de la información del sistema: esquema de datos y dominios.");
    try
    {
    
      ImageIcon icon=IconLoader.icon("logo_geopista.png");
            
    	d.setSideBarImage(icon);
    }catch(NullPointerException e)
    {
      e.printStackTrace();
    }
      d.setVisible(true);
      
	  
	  
      
    


}


}
