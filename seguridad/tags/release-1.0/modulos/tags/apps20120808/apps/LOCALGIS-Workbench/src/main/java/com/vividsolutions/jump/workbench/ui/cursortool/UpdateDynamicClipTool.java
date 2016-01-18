package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkBench;
import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class UpdateDynamicClipTool extends NClickTool{
	
	private EnableCheckFactory checkFactory;	  
    private ArrayList clipFeatures = new ArrayList();    
    boolean doubleClick = false;
    protected AbstractSelection selection;    
    private Geometry refGeometry = null; 
    
    private Feature refFeature = null;
    private Layer refLayer = null;
    public final static Color COLOR = Color.black;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    TaskMonitorDialog progressDialog;
    WorkBench workbench;


	public UpdateDynamicClipTool() {
		super(1);
	}
    
    public String getName(){
        return aplicacion.getI18nString("VisualizarUnaFeature");
    }

    public Icon getIcon() {
        return  com.geopista.ui.images.IconLoader.icon("dinamico_clip.gif");
    }
    
    public Cursor getCursor() {
        return createCursor(com.geopista.ui.images.IconLoader.icon("dinamico_clip_cursor.gif").getImage());
    }

    protected void gestureFinished() throws Exception {
    	workbench =((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                getPanel())).getContext().getIWorkbench();
    	progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(),
    			workbench.getContext().getErrorHandler());
        progressDialog.setTitle(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.report(I18N.get("LoadSystemLayers.Cargando"));
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                //Wait for the dialog to appear before starting the task. Otherwise
                //the task might possibly finish before the dialog appeared and the
                //dialog would never close. [Jon Aquino]
                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                        	UpdateDynamic updateDynamic = new UpdateDynamic(getPanel());
                        	updateDynamic.getFeatures(getPoint());
                        }catch(Exception e)
                        {
                        }finally
                        {
                            progressDialog.setVisible(false);
                        } 
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        reportNothingToUndoYet();
        ((WorkbenchGuiComponent) SwingUtilities.getAncestorOfClass(WorkbenchGuiComponent.class,
                getPanel())).getContext().getLayerViewPanel().getRenderingManager().renderAll(true);
       
    }


	protected Point getPoint()
		throws NoninvertibleTransformException {
		return new GeometryFactory().createPoint(
			(Coordinate)getCoordinates().get(0));
	}	

       
}
