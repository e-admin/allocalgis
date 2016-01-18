/**
 * DatosAlfanumericosManagerPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.edicionalfanum;


import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;

public class DatosAlfanumericosManagerPlugin extends ThreadedBasePlugIn
{
    private static final Log logger = LogFactory.getLog(DatosAlfanumericosManagerPlugin.class);
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Feature localFeature = null;


   
    public void initialize(PlugInContext context) throws Exception
    {
        Blackboard Identificadores = aplicacion.getBlackboard();
        Vector vExtendedForm = (Vector)Identificadores.get(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM);
        if (vExtendedForm==null) vExtendedForm=new Vector();
        vExtendedForm.add(new com.geopista.ui.plugin.edicionalfanum.DatosAlfanumericosExtendedForm());

        Identificadores.put(GeopistaFeatureSchemaPlugIn.IDENT_EXTENDED_FORM,vExtendedForm);

    }

    public boolean execute(PlugInContext context) throws Exception
    {
        
        return false;
    }


    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
