/**
 * RouteEngineWICDrawPointTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.withincost.utils;

import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.Icon;


import com.geopista.ui.cursortool.NClickTool;
import com.geopista.ui.cursortool.editing.FeatureDrawingUtil;
import com.geopista.ui.plugin.routeenginetools.withincost.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.withincost.WithinCostPlugIn;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.workbench.ui.LayerNamePanelProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;

public class RouteEngineWICDrawPointTool extends NClickTool{
	
	public static Point pointDrawed = null; 

	private RouteEngineWICDrawPointTool() {
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
			new RouteEngineWICDrawPointTool(), false);
	}

	public Icon getIcon() {
		return IconLoader.icon("DrawPoint.gif");
	}

	protected void gestureFinished() throws Exception {
		reportNothingToUndoYet();
		this.pointDrawed = getPoint();

		
		execute(
		WithinCostPlugIn.createAddCommand(
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
