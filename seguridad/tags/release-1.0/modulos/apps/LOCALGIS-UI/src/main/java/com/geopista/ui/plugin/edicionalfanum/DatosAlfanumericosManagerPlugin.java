package com.geopista.ui.plugin.edicionalfanum;


import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.LockManager;
import com.geopista.ui.dialogs.TextDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.edit.GeopistaFeatureSchemaPlugIn;
import com.geopista.util.GeopistaUtil;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.*;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.util.Blackboard;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.*;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
