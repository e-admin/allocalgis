package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog;

import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.Icon;
import com.geopista.ui.cursortool.NClickTool;
import com.geopista.ui.cursortool.editing.FeatureDrawingUtil;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.CalcularRutaClick;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;

public class RouteEngineDrawPointTool extends NClickTool{
	
	public static Point pointDrawed = null; 

	private RouteEngineDrawPointTool() {
		super(1);
	}
	
	protected Shape getShape() throws NoninvertibleTransformException {
		//Don't want anything to show up when the user drags. [Jon Aquino]
		return null;
	}

	public static CursorTool create(LayerNamePanelProxy layerNamePanelProxy) {
		FeatureDrawingUtil featureDrawingUtil =
			new FeatureDrawingUtil(layerNamePanelProxy);

		//Don't allow snapping. The user will get confused if he tries to draw
		//a point near another point and sees nothing happen because
		//snapping is happening. [Jon Aquino]
		return featureDrawingUtil.prepare(
			new RouteEngineDrawPointTool(), false);
	}

	public Icon getIcon() {
		return IconLoader.icon("DrawPoint.gif");
	}

	protected void gestureFinished() throws Exception {
		reportNothingToUndoYet();
		this.pointDrawed = getPoint();

		
		execute(
		CalcularRutaClick.createAddCommand(
				getPoint(), this
		));

	}
	
	

	protected Point getPoint()
		throws NoninvertibleTransformException {
		return new GeometryFactory().createPoint(
			(Coordinate)getCoordinates().get(0));
	}	
	
	public Point getPonitToDraw(){
		return this.pointDrawed;
	}
	
	public void setPointToDrawI(Point point){
		this.pointDrawed = point;
	}
}
