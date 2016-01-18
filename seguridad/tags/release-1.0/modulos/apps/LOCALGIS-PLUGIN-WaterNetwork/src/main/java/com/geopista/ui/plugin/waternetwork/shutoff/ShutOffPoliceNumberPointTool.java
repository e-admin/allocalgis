package com.geopista.ui.plugin.waternetwork.shutoff;

import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.Icon;
import com.geopista.ui.cursortool.NClickTool;
import com.geopista.ui.cursortool.editing.FeatureDrawingUtil;
import com.geopista.ui.plugin.waternetwork.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;

public class ShutOffPoliceNumberPointTool extends NClickTool{
	
	public static Point pointDrawed = null; 

	private ShutOffPoliceNumberPointTool() {
		super(1);
	}
	
	protected Shape getShape() throws NoninvertibleTransformException {
		return null;
	}

	public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
		FeatureDrawingUtil featureDrawingUtil =
			new FeatureDrawingUtil(layerNamePanelProxy);
		return featureDrawingUtil.prepare(
			new ShutOffPoliceNumberPointTool(), false);
	}

	public Icon getIcon(){
		return IconLoader.icon("DrawPoint.gif");
	}

	protected void gestureFinished() throws Exception {
		reportNothingToUndoYet();		
		execute(ShutOffPoliceNumberPlugIn.createAddCommand(getPoint(), this));
	}
	
	protected Point getPoint()
		throws NoninvertibleTransformException {
		return new GeometryFactory().createPoint(
			(Coordinate)getCoordinates().get(0));
	}	
}
